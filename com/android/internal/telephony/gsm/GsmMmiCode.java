package com.android.internal.telephony.gsm;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.ResultReceiver;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.text.BidiFormatter;
import android.text.SpannableStringBuilder;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.MmiCode.State;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.util.ArrayUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GsmMmiCode
  extends Handler
  implements MmiCode
{
  static final String ACTION_ACTIVATE = "*";
  static final String ACTION_DEACTIVATE = "#";
  static final String ACTION_ERASURE = "##";
  static final String ACTION_INTERROGATE = "*#";
  static final String ACTION_REGISTER = "**";
  static final char END_OF_USSD_COMMAND = '#';
  static final int EVENT_GET_CLIR_COMPLETE = 2;
  static final int EVENT_QUERY_CF_COMPLETE = 3;
  static final int EVENT_QUERY_COMPLETE = 5;
  static final int EVENT_SET_CFF_COMPLETE = 6;
  static final int EVENT_SET_COMPLETE = 1;
  static final int EVENT_USSD_CANCEL_COMPLETE = 7;
  static final int EVENT_USSD_COMPLETE = 4;
  static final String LOG_TAG = "GsmMmiCode";
  static final int MATCH_GROUP_ACTION = 2;
  static final int MATCH_GROUP_DIALING_NUMBER = 12;
  static final int MATCH_GROUP_POUND_STRING = 1;
  static final int MATCH_GROUP_PWD_CONFIRM = 11;
  static final int MATCH_GROUP_SERVICE_CODE = 3;
  static final int MATCH_GROUP_SIA = 5;
  static final int MATCH_GROUP_SIB = 7;
  static final int MATCH_GROUP_SIC = 9;
  static final int MAX_LENGTH_SHORT_CODE = 2;
  static final String SC_BAIC = "35";
  static final String SC_BAICr = "351";
  static final String SC_BAOC = "33";
  static final String SC_BAOIC = "331";
  static final String SC_BAOICxH = "332";
  static final String SC_BA_ALL = "330";
  static final String SC_BA_MO = "333";
  static final String SC_BA_MT = "353";
  static final String SC_CFB = "67";
  static final String SC_CFNR = "62";
  static final String SC_CFNRy = "61";
  static final String SC_CFU = "21";
  static final String SC_CF_All = "002";
  static final String SC_CF_All_Conditional = "004";
  static final String SC_CLIP = "30";
  static final String SC_CLIR = "31";
  static final String SC_PIN = "04";
  static final String SC_PIN2 = "042";
  static final String SC_PUK = "05";
  static final String SC_PUK2 = "052";
  static final String SC_PWD = "03";
  static final String SC_WAIT = "43";
  static Pattern sPatternSuppService = Pattern.compile("((\\*|#|\\*#|\\*\\*|##)(\\d{2,3})(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*))?)?)?)?#)(.*)");
  private static String[] sTwoDigitNumberPattern;
  String mAction;
  private ResultReceiver mCallbackReceiver;
  Context mContext;
  public String mDialingNumber;
  IccRecords mIccRecords;
  private boolean mIsCallFwdReg;
  private boolean mIsPendingUSSD;
  private boolean mIsSsInfo = false;
  private boolean mIsUssdRequest;
  CharSequence mMessage;
  GsmCdmaPhone mPhone;
  String mPoundString;
  String mPwd;
  String mSc;
  String mSia;
  String mSib;
  String mSic;
  MmiCode.State mState = MmiCode.State.PENDING;
  UiccCardApplication mUiccApplication;
  
  public GsmMmiCode(GsmCdmaPhone paramGsmCdmaPhone, UiccCardApplication paramUiccCardApplication)
  {
    super(paramGsmCdmaPhone.getHandler().getLooper());
    mPhone = paramGsmCdmaPhone;
    mContext = paramGsmCdmaPhone.getContext();
    mUiccApplication = paramUiccCardApplication;
    if (paramUiccCardApplication != null) {
      mIccRecords = paramUiccCardApplication.getIccRecords();
    }
  }
  
  private static String convertCdmaMmiCodesTo3gppMmiCodes(String paramString)
  {
    Object localObject = sPatternCdmaMmiCodeWhileRoaming.matcher(paramString);
    String str1 = paramString;
    if (((Matcher)localObject).matches())
    {
      String str2 = makeEmptyNull(((Matcher)localObject).group(1));
      String str3 = ((Matcher)localObject).group(2);
      localObject = makeEmptyNull(((Matcher)localObject).group(3));
      if ((str2.equals("67")) && (localObject != null))
      {
        paramString = new StringBuilder();
        paramString.append("#31#");
        paramString.append(str3);
        paramString.append((String)localObject);
        str1 = paramString.toString();
      }
      else
      {
        str1 = paramString;
        if (str2.equals("82"))
        {
          str1 = paramString;
          if (localObject != null)
          {
            paramString = new StringBuilder();
            paramString.append("*31#");
            paramString.append(str3);
            paramString.append((String)localObject);
            str1 = paramString.toString();
          }
        }
      }
    }
    return str1;
  }
  
  private CharSequence createQueryCallBarringResultMessage(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder(mContext.getText(17040980));
    int i = 1;
    while (i <= 128)
    {
      if ((i & paramInt) != 0)
      {
        localStringBuilder.append("\n");
        localStringBuilder.append(serviceClassToCFString(i & paramInt));
      }
      i <<= 1;
    }
    return localStringBuilder;
  }
  
  private CharSequence createQueryCallWaitingResultMessage(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder(mContext.getText(17040980));
    int i = 1;
    while (i <= 128)
    {
      if ((i & paramInt) != 0)
      {
        localStringBuilder.append("\n");
        localStringBuilder.append(serviceClassToCFString(i & paramInt));
      }
      i <<= 1;
    }
    return localStringBuilder;
  }
  
  private String formatLtr(String paramString)
  {
    BidiFormatter localBidiFormatter = BidiFormatter.getInstance();
    if (paramString != null) {
      paramString = localBidiFormatter.unicodeWrap(paramString, TextDirectionHeuristics.LTR, true);
    }
    return paramString;
  }
  
  private String getActionStringFromReqType(SsData.RequestType paramRequestType)
  {
    switch (1.$SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType[paramRequestType.ordinal()])
    {
    default: 
      return "";
    case 5: 
      return "*#";
    case 4: 
      return "##";
    case 3: 
      return "**";
    case 2: 
      return "#";
    }
    return "*";
  }
  
  private CharSequence getErrorMessage(AsyncResult paramAsyncResult)
  {
    if ((exception instanceof CommandException))
    {
      paramAsyncResult = ((CommandException)exception).getCommandError();
      if (paramAsyncResult == CommandException.Error.FDN_CHECK_FAILURE)
      {
        Rlog.i("GsmMmiCode", "FDN_CHECK_FAILURE");
        return mContext.getText(17040451);
      }
      if (paramAsyncResult == CommandException.Error.USSD_MODIFIED_TO_DIAL)
      {
        Rlog.i("GsmMmiCode", "USSD_MODIFIED_TO_DIAL");
        return mContext.getText(17041081);
      }
      if (paramAsyncResult == CommandException.Error.USSD_MODIFIED_TO_SS)
      {
        Rlog.i("GsmMmiCode", "USSD_MODIFIED_TO_SS");
        return mContext.getText(17041083);
      }
      if (paramAsyncResult == CommandException.Error.USSD_MODIFIED_TO_USSD)
      {
        Rlog.i("GsmMmiCode", "USSD_MODIFIED_TO_USSD");
        return mContext.getText(17041084);
      }
      if (paramAsyncResult == CommandException.Error.SS_MODIFIED_TO_DIAL)
      {
        Rlog.i("GsmMmiCode", "SS_MODIFIED_TO_DIAL");
        return mContext.getText(17041077);
      }
      if (paramAsyncResult == CommandException.Error.SS_MODIFIED_TO_USSD)
      {
        Rlog.i("GsmMmiCode", "SS_MODIFIED_TO_USSD");
        return mContext.getText(17041080);
      }
      if (paramAsyncResult == CommandException.Error.SS_MODIFIED_TO_SS)
      {
        Rlog.i("GsmMmiCode", "SS_MODIFIED_TO_SS");
        return mContext.getText(17041079);
      }
      if (paramAsyncResult == CommandException.Error.OEM_ERROR_1)
      {
        Rlog.i("GsmMmiCode", "OEM_ERROR_1 USSD_MODIFIED_TO_DIAL_VIDEO");
        return mContext.getText(17041082);
      }
    }
    return mContext.getText(17040449);
  }
  
  private CharSequence getScString()
  {
    if (mSc != null)
    {
      if (isServiceCodeCallBarring(mSc)) {
        return mContext.getText(17039387);
      }
      if (isServiceCodeCallForwarding(mSc)) {
        return mContext.getText(17039393);
      }
      if (mSc.equals("30")) {
        return mContext.getText(17039394);
      }
      if (mSc.equals("31")) {
        return mContext.getText(17039395);
      }
      if (mSc.equals("03")) {
        return mContext.getText(17039410);
      }
      if (mSc.equals("43")) {
        return mContext.getText(17039401);
      }
      if (isPinPukCommand()) {
        return mContext.getText(17039409);
      }
    }
    return "";
  }
  
  private String getScStringFromScType(SsData.ServiceType paramServiceType)
  {
    switch (1.$SwitchMap$com$android$internal$telephony$gsm$SsData$ServiceType[paramServiceType.ordinal()])
    {
    default: 
      return "";
    case 17: 
      return "353";
    case 16: 
      return "333";
    case 15: 
      return "330";
    case 14: 
      return "351";
    case 13: 
      return "35";
    case 12: 
      return "332";
    case 11: 
      return "331";
    case 10: 
      return "33";
    case 9: 
      return "43";
    case 8: 
      return "31";
    case 7: 
      return "30";
    case 6: 
      return "004";
    case 5: 
      return "002";
    case 4: 
      return "62";
    case 3: 
      return "61";
    case 2: 
      return "67";
    }
    return "21";
  }
  
  private void handlePasswordError(int paramInt)
  {
    mState = MmiCode.State.FAILED;
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    localStringBuilder.append(mContext.getText(paramInt));
    mMessage = localStringBuilder;
    mPhone.onMMIDone(this);
  }
  
  private static boolean isEmptyOrNull(CharSequence paramCharSequence)
  {
    boolean bool;
    if ((paramCharSequence != null) && (paramCharSequence.length() != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isFacToDial()
  {
    Object localObject = ((CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config")).getConfigForSubId(mPhone.getSubId());
    if (localObject != null)
    {
      localObject = ((PersistableBundle)localObject).getStringArray("feature_access_codes_string_array");
      if (!ArrayUtils.isEmpty((Object[])localObject))
      {
        int i = localObject.length;
        for (int j = 0; j < i; j++) {
          if (localObject[j].equals(mSc)) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  private boolean isServiceClassVoiceorNone(int paramInt)
  {
    boolean bool;
    if (((paramInt & 0x1) == 0) && (paramInt != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  static boolean isServiceCodeCallBarring(String paramString)
  {
    Object localObject = Resources.getSystem();
    if (paramString != null)
    {
      localObject = ((Resources)localObject).getStringArray(17235994);
      if (localObject != null)
      {
        int i = localObject.length;
        for (int j = 0; j < i; j++) {
          if (paramString.equals(localObject[j])) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  static boolean isServiceCodeCallForwarding(String paramString)
  {
    boolean bool;
    if ((paramString != null) && ((paramString.equals("21")) || (paramString.equals("67")) || (paramString.equals("61")) || (paramString.equals("62")) || (paramString.equals("002")) || (paramString.equals("004")))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isShortCode(String paramString, GsmCdmaPhone paramGsmCdmaPhone)
  {
    if (paramString == null) {
      return false;
    }
    if (paramString.length() == 0) {
      return false;
    }
    if (PhoneNumberUtils.isLocalEmergencyNumber(paramGsmCdmaPhone.getContext(), paramGsmCdmaPhone.getSubId(), paramString))
    {
      Rlog.d("GsmMmiCode", "add subId for  isLocalEmergencyNumber ");
      return false;
    }
    return isShortCodeUSSD(paramString, paramGsmCdmaPhone);
  }
  
  private static boolean isShortCodeUSSD(String paramString, GsmCdmaPhone paramGsmCdmaPhone)
  {
    if ((paramString != null) && (paramString.length() <= 2))
    {
      if (paramGsmCdmaPhone.isInCall()) {
        return true;
      }
      if ((paramString.length() != 2) || (paramString.charAt(0) != '1')) {
        return true;
      }
    }
    return false;
  }
  
  private static boolean isTwoDigitShortCode(Context paramContext, String paramString)
  {
    Rlog.d("GsmMmiCode", "isTwoDigitShortCode");
    if ((paramString != null) && (paramString.length() <= 2))
    {
      if (sTwoDigitNumberPattern == null) {
        sTwoDigitNumberPattern = paramContext.getResources().getStringArray(17236053);
      }
      for (String str : sTwoDigitNumberPattern)
      {
        paramContext = new StringBuilder();
        paramContext.append("Two Digit Number Pattern ");
        paramContext.append(str);
        Rlog.d("GsmMmiCode", paramContext.toString());
        if (paramString.equals(str))
        {
          Rlog.d("GsmMmiCode", "Two Digit Number Pattern -true");
          return true;
        }
      }
      Rlog.d("GsmMmiCode", "Two Digit Number Pattern -false");
      return false;
    }
    return false;
  }
  
  public static boolean isVoiceUnconditionalForwarding(int paramInt1, int paramInt2)
  {
    boolean bool;
    if (((paramInt1 != 0) && (paramInt1 != 4)) || (((paramInt2 & 0x1) == 0) && (paramInt2 != 0))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private CharSequence makeCFQueryResultMessage(CallForwardInfo paramCallForwardInfo, int paramInt)
  {
    int i = reason;
    boolean bool = false;
    if (i == 2) {
      i = 1;
    } else {
      i = 0;
    }
    CharSequence localCharSequence1;
    if (status == 1) {
      if (i != 0) {
        localCharSequence1 = mContext.getText(17039647);
      }
    }
    for (;;)
    {
      break;
      localCharSequence1 = mContext.getText(17039646);
      continue;
      if ((status == 0) && (isEmptyOrNull(number))) {
        localCharSequence1 = mContext.getText(17039648);
      } else if (i != 0) {
        localCharSequence1 = mContext.getText(17039650);
      } else {
        localCharSequence1 = mContext.getText(17039649);
      }
    }
    CharSequence localCharSequence2 = serviceClassToCFString(serviceClass & paramInt);
    String str1 = formatLtr(PhoneNumberUtils.stringFromStringAndTOA(number, toa));
    String str2 = Integer.toString(timeSeconds);
    if ((reason == 0) && ((serviceClass & paramInt) == 1))
    {
      if (status == 1) {
        bool = true;
      }
      if (mIccRecords != null) {
        mPhone.setVoiceCallForwardingFlag(1, bool, number);
      }
    }
    return TextUtils.replace(localCharSequence1, new String[] { "{0}", "{1}", "{2}" }, new CharSequence[] { localCharSequence2, str1, str2 });
  }
  
  private static String makeEmptyNull(String paramString)
  {
    if ((paramString != null) && (paramString.length() == 0)) {
      return null;
    }
    return paramString;
  }
  
  public static GsmMmiCode newFromDialString(String paramString, GsmCdmaPhone paramGsmCdmaPhone, UiccCardApplication paramUiccCardApplication)
  {
    return newFromDialString(paramString, paramGsmCdmaPhone, paramUiccCardApplication, null);
  }
  
  public static GsmMmiCode newFromDialString(String paramString, GsmCdmaPhone paramGsmCdmaPhone, UiccCardApplication paramUiccCardApplication, ResultReceiver paramResultReceiver)
  {
    GsmMmiCode localGsmMmiCode = null;
    String str = paramString;
    if (paramGsmCdmaPhone.getServiceState().getVoiceRoaming())
    {
      str = paramString;
      if (paramGsmCdmaPhone.supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming()) {
        str = convertCdmaMmiCodesTo3gppMmiCodes(paramString);
      }
    }
    paramString = sPatternSuppService.matcher(str);
    if (paramString.matches())
    {
      localGsmMmiCode = new GsmMmiCode(paramGsmCdmaPhone, paramUiccCardApplication);
      mPoundString = makeEmptyNull(paramString.group(1));
      mAction = makeEmptyNull(paramString.group(2));
      mSc = makeEmptyNull(paramString.group(3));
      mSia = makeEmptyNull(paramString.group(5));
      mSib = makeEmptyNull(paramString.group(7));
      mSic = makeEmptyNull(paramString.group(9));
      mPwd = makeEmptyNull(paramString.group(11));
      mDialingNumber = makeEmptyNull(paramString.group(12));
      if ((mDialingNumber != null) && (mDialingNumber.endsWith("#")) && (str.endsWith("#")))
      {
        paramString = new GsmMmiCode(paramGsmCdmaPhone, paramUiccCardApplication);
        mPoundString = str;
      }
      else
      {
        paramString = localGsmMmiCode;
        if (localGsmMmiCode.isFacToDial()) {
          paramString = null;
        }
      }
    }
    else if (str.endsWith("#"))
    {
      paramString = new GsmMmiCode(paramGsmCdmaPhone, paramUiccCardApplication);
      mPoundString = str;
    }
    else if (isTwoDigitShortCode(paramGsmCdmaPhone.getContext(), str))
    {
      paramString = null;
    }
    else
    {
      paramString = localGsmMmiCode;
      if (isShortCode(str, paramGsmCdmaPhone))
      {
        paramString = new GsmMmiCode(paramGsmCdmaPhone, paramUiccCardApplication);
        mDialingNumber = str;
      }
    }
    if (paramString != null) {
      mCallbackReceiver = paramResultReceiver;
    }
    return paramString;
  }
  
  public static GsmMmiCode newFromUssdUserInput(String paramString, GsmCdmaPhone paramGsmCdmaPhone, UiccCardApplication paramUiccCardApplication)
  {
    paramGsmCdmaPhone = new GsmMmiCode(paramGsmCdmaPhone, paramUiccCardApplication);
    mMessage = paramString;
    mState = MmiCode.State.PENDING;
    mIsPendingUSSD = true;
    return paramGsmCdmaPhone;
  }
  
  public static GsmMmiCode newNetworkInitiatedUssd(String paramString, boolean paramBoolean, GsmCdmaPhone paramGsmCdmaPhone, UiccCardApplication paramUiccCardApplication)
  {
    paramGsmCdmaPhone = new GsmMmiCode(paramGsmCdmaPhone, paramUiccCardApplication);
    mMessage = paramString;
    mIsUssdRequest = paramBoolean;
    if (paramBoolean)
    {
      mIsPendingUSSD = true;
      mState = MmiCode.State.PENDING;
    }
    else
    {
      mState = MmiCode.State.COMPLETE;
    }
    return paramGsmCdmaPhone;
  }
  
  private void onGetClirComplete(AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    if (exception != null)
    {
      mState = MmiCode.State.FAILED;
      localStringBuilder.append(getErrorMessage(paramAsyncResult));
    }
    else
    {
      paramAsyncResult = (int[])result;
      switch (paramAsyncResult[1])
      {
      default: 
        break;
      case 4: 
        switch (paramAsyncResult[0])
        {
        default: 
          localStringBuilder.append(mContext.getText(17039388));
          break;
        case 2: 
          localStringBuilder.append(mContext.getText(17039388));
          break;
        case 1: 
          localStringBuilder.append(mContext.getText(17039389));
        }
        mState = MmiCode.State.COMPLETE;
        break;
      case 3: 
        switch (paramAsyncResult[0])
        {
        default: 
          localStringBuilder.append(mContext.getText(17039391));
          break;
        case 2: 
          localStringBuilder.append(mContext.getText(17039390));
          break;
        case 1: 
          localStringBuilder.append(mContext.getText(17039391));
        }
        mState = MmiCode.State.COMPLETE;
        break;
      case 2: 
        localStringBuilder.append(mContext.getText(17040449));
        mState = MmiCode.State.FAILED;
        break;
      case 1: 
        localStringBuilder.append(mContext.getText(17039392));
        mState = MmiCode.State.COMPLETE;
        break;
      case 0: 
        localStringBuilder.append(mContext.getText(17040982));
        mState = MmiCode.State.COMPLETE;
      }
    }
    mMessage = localStringBuilder;
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("onGetClirComplete: mmi=");
    paramAsyncResult.append(this);
    Rlog.d("GsmMmiCode", paramAsyncResult.toString());
    mPhone.onMMIDone(this);
  }
  
  private void onQueryCfComplete(AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    if (exception != null)
    {
      mState = MmiCode.State.FAILED;
      localStringBuilder.append(getErrorMessage(paramAsyncResult));
    }
    else
    {
      paramAsyncResult = (CallForwardInfo[])result;
      int i = paramAsyncResult.length;
      int j = 1;
      if (i == 0)
      {
        localStringBuilder.append(mContext.getText(17040978));
        if (mIccRecords != null) {
          mPhone.setVoiceCallForwardingFlag(1, false, null);
        }
      }
      else
      {
        SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
        while (j <= 128)
        {
          i = 0;
          int k = paramAsyncResult.length;
          while (i < k)
          {
            if ((serviceClass & j) != 0)
            {
              localSpannableStringBuilder.append(makeCFQueryResultMessage(paramAsyncResult[i], j));
              localSpannableStringBuilder.append("\n");
            }
            i++;
          }
          j <<= 1;
        }
        localStringBuilder.append(localSpannableStringBuilder);
      }
      mState = MmiCode.State.COMPLETE;
    }
    mMessage = localStringBuilder;
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("onQueryCfComplete: mmi=");
    paramAsyncResult.append(this);
    Rlog.d("GsmMmiCode", paramAsyncResult.toString());
    mPhone.onMMIDone(this);
  }
  
  private void onQueryComplete(AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    if (exception != null)
    {
      mState = MmiCode.State.FAILED;
      localStringBuilder.append(getErrorMessage(paramAsyncResult));
    }
    else
    {
      paramAsyncResult = (int[])result;
      if (paramAsyncResult.length != 0)
      {
        if (paramAsyncResult[0] == 0) {
          localStringBuilder.append(mContext.getText(17040978));
        } else if (mSc.equals("43")) {
          localStringBuilder.append(createQueryCallWaitingResultMessage(paramAsyncResult[1]));
        } else if (isServiceCodeCallBarring(mSc)) {
          localStringBuilder.append(createQueryCallBarringResultMessage(paramAsyncResult[0]));
        } else if (paramAsyncResult[0] == 1) {
          localStringBuilder.append(mContext.getText(17040979));
        } else {
          localStringBuilder.append(mContext.getText(17040449));
        }
      }
      else {
        localStringBuilder.append(mContext.getText(17040449));
      }
      mState = MmiCode.State.COMPLETE;
    }
    mMessage = localStringBuilder;
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("onQueryComplete: mmi=");
    paramAsyncResult.append(this);
    Rlog.d("GsmMmiCode", paramAsyncResult.toString());
    mPhone.onMMIDone(this);
  }
  
  private void onSetComplete(Message paramMessage, AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    if (exception != null)
    {
      mState = MmiCode.State.FAILED;
      if ((exception instanceof CommandException))
      {
        CommandException.Error localError = ((CommandException)exception).getCommandError();
        if (localError == CommandException.Error.PASSWORD_INCORRECT)
        {
          if (isPinPukCommand())
          {
            if ((!mSc.equals("05")) && (!mSc.equals("052"))) {
              localStringBuilder.append(mContext.getText(17039609));
            } else {
              localStringBuilder.append(mContext.getText(17039610));
            }
            int i = arg1;
            if (i <= 0)
            {
              Rlog.d("GsmMmiCode", "onSetComplete: PUK locked, cancel as lock screen will handle this");
              mState = MmiCode.State.CANCELLED;
            }
            else if (i > 0)
            {
              paramMessage = new StringBuilder();
              paramMessage.append("onSetComplete: attemptsRemaining=");
              paramMessage.append(i);
              Rlog.d("GsmMmiCode", paramMessage.toString());
              localStringBuilder.append(mContext.getResources().getQuantityString(18153493, i, new Object[] { Integer.valueOf(i) }));
            }
          }
          else
          {
            localStringBuilder.append(mContext.getText(17040539));
          }
        }
        else if (localError == CommandException.Error.SIM_PUK2)
        {
          localStringBuilder.append(mContext.getText(17039609));
          localStringBuilder.append("\n");
          localStringBuilder.append(mContext.getText(17040458));
        }
        else if (localError == CommandException.Error.REQUEST_NOT_SUPPORTED)
        {
          if (mSc.equals("04")) {
            localStringBuilder.append(mContext.getText(17039919));
          }
        }
        else if (localError == CommandException.Error.FDN_CHECK_FAILURE)
        {
          Rlog.i("GsmMmiCode", "FDN_CHECK_FAILURE");
          localStringBuilder.append(mContext.getText(17040451));
        }
        else if (localError == CommandException.Error.MODEM_ERR)
        {
          if ((isServiceCodeCallForwarding(mSc)) && (mPhone.getServiceState().getVoiceRoaming()) && (!mPhone.supports3gppCallForwardingWhileRoaming())) {
            localStringBuilder.append(mContext.getText(17040450));
          } else {
            localStringBuilder.append(getErrorMessage(paramAsyncResult));
          }
        }
        else if (localError == CommandException.Error.NOT_PROVISIONED)
        {
          localStringBuilder.append(mContext.getText(17040982));
        }
        else
        {
          localStringBuilder.append(getErrorMessage(paramAsyncResult));
        }
      }
      else
      {
        localStringBuilder.append(mContext.getText(17040449));
      }
    }
    else if (isActivate())
    {
      mState = MmiCode.State.COMPLETE;
      if (mIsCallFwdReg) {
        localStringBuilder.append(mContext.getText(17040983));
      } else {
        localStringBuilder.append(mContext.getText(17040979));
      }
      if (mSc.equals("31")) {
        mPhone.saveClirSetting(1);
      }
    }
    else if (isDeactivate())
    {
      mState = MmiCode.State.COMPLETE;
      localStringBuilder.append(mContext.getText(17040978));
      if (mSc.equals("31")) {
        mPhone.saveClirSetting(2);
      }
    }
    else if (isRegister())
    {
      mState = MmiCode.State.COMPLETE;
      localStringBuilder.append(mContext.getText(17040983));
    }
    else if (isErasure())
    {
      mState = MmiCode.State.COMPLETE;
      localStringBuilder.append(mContext.getText(17040981));
    }
    else
    {
      mState = MmiCode.State.FAILED;
      localStringBuilder.append(mContext.getText(17040449));
    }
    mMessage = localStringBuilder;
    paramMessage = new StringBuilder();
    paramMessage.append("onSetComplete mmi=");
    paramMessage.append(this);
    Rlog.d("GsmMmiCode", paramMessage.toString());
    mPhone.onMMIDone(this);
  }
  
  static String scToBarringFacility(String paramString)
  {
    if (paramString != null)
    {
      if (paramString.equals("33")) {
        return "AO";
      }
      if (paramString.equals("331")) {
        return "OI";
      }
      if (paramString.equals("332")) {
        return "OX";
      }
      if (paramString.equals("35")) {
        return "AI";
      }
      if (paramString.equals("351")) {
        return "IR";
      }
      if (paramString.equals("330")) {
        return "AB";
      }
      if (paramString.equals("333")) {
        return "AG";
      }
      if (paramString.equals("353")) {
        return "AC";
      }
      throw new RuntimeException("invalid call barring sc");
    }
    throw new RuntimeException("invalid call barring sc");
  }
  
  private static int scToCallForwardReason(String paramString)
  {
    if (paramString != null)
    {
      if (paramString.equals("002")) {
        return 4;
      }
      if (paramString.equals("21")) {
        return 0;
      }
      if (paramString.equals("67")) {
        return 1;
      }
      if (paramString.equals("62")) {
        return 3;
      }
      if (paramString.equals("61")) {
        return 2;
      }
      if (paramString.equals("004")) {
        return 5;
      }
      throw new RuntimeException("invalid call forward sc");
    }
    throw new RuntimeException("invalid call forward sc");
  }
  
  private CharSequence serviceClassToCFString(int paramInt)
  {
    if (paramInt != 4)
    {
      if (paramInt != 8)
      {
        if (paramInt != 16)
        {
          if (paramInt != 32)
          {
            if (paramInt != 64)
            {
              if (paramInt != 128)
              {
                switch (paramInt)
                {
                default: 
                  return null;
                case 2: 
                  return mContext.getText(17040970);
                }
                return mContext.getText(17040977);
              }
              return mContext.getText(17040974);
            }
            return mContext.getText(17040975);
          }
          return mContext.getText(17040971);
        }
        return mContext.getText(17040972);
      }
      return mContext.getText(17040976);
    }
    return mContext.getText(17040973);
  }
  
  private static int siToServiceClass(String paramString)
  {
    if ((paramString != null) && (paramString.length() != 0))
    {
      int i = Integer.parseInt(paramString, 10);
      if (i != 16)
      {
        if (i != 99)
        {
          switch (i)
          {
          default: 
            switch (i)
            {
            default: 
              switch (i)
              {
              default: 
                StringBuilder localStringBuilder = new StringBuilder();
                localStringBuilder.append("unsupported MMI service code ");
                localStringBuilder.append(paramString);
                throw new RuntimeException(localStringBuilder.toString());
              case 26: 
                return 17;
              case 25: 
                return 32;
              }
              return 16;
            case 22: 
              return 80;
            case 21: 
              return 160;
            case 20: 
              return 48;
            }
            return 5;
          case 13: 
            return 4;
          case 12: 
            return 12;
          case 11: 
            return 1;
          }
          return 13;
        }
        return 64;
      }
      return 8;
    }
    return 0;
  }
  
  private static int siToTime(String paramString)
  {
    if ((paramString != null) && (paramString.length() != 0)) {
      return Integer.parseInt(paramString, 10);
    }
    return 0;
  }
  
  public void cancel()
  {
    if ((mState != MmiCode.State.COMPLETE) && (mState != MmiCode.State.FAILED))
    {
      mState = MmiCode.State.CANCELLED;
      if (mIsPendingUSSD) {
        mPhone.mCi.cancelPendingUssd(obtainMessage(7, this));
      } else {
        mPhone.onMMIDone(this);
      }
      return;
    }
  }
  
  public int getCLIRMode()
  {
    if ((mSc != null) && (mSc.equals("31")))
    {
      if (isActivate()) {
        return 2;
      }
      if (isDeactivate()) {
        return 1;
      }
    }
    return 0;
  }
  
  public String getDialString()
  {
    return mPoundString;
  }
  
  public CharSequence getMessage()
  {
    return mMessage;
  }
  
  public Phone getPhone()
  {
    return mPhone;
  }
  
  public String getServiceCode()
  {
    return mSc;
  }
  
  public MmiCode.State getState()
  {
    return mState;
  }
  
  public ResultReceiver getUssdCallbackReceiver()
  {
    return mCallbackReceiver;
  }
  
  public void handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      break;
    case 7: 
      mPhone.onMMIDone(this);
      break;
    case 6: 
      AsyncResult localAsyncResult = (AsyncResult)obj;
      if ((exception == null) && (arg1 == 1))
      {
        boolean bool;
        if (arg2 == 1) {
          bool = true;
        } else {
          bool = false;
        }
        if (mIccRecords != null) {
          mPhone.setVoiceCallForwardingFlag(1, bool, mDialingNumber);
        }
      }
      onSetComplete(paramMessage, localAsyncResult);
      break;
    case 5: 
      onQueryComplete((AsyncResult)obj);
      break;
    case 4: 
      paramMessage = (AsyncResult)obj;
      if (exception != null)
      {
        mState = MmiCode.State.FAILED;
        mMessage = getErrorMessage(paramMessage);
        mPhone.onMMIDone(this);
      }
      break;
    case 3: 
      onQueryCfComplete((AsyncResult)obj);
      break;
    case 2: 
      onGetClirComplete((AsyncResult)obj);
      break;
    case 1: 
      onSetComplete(paramMessage, (AsyncResult)obj);
    }
  }
  
  boolean isActivate()
  {
    boolean bool;
    if ((mAction != null) && (mAction.equals("*"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCancelable()
  {
    return mIsPendingUSSD;
  }
  
  boolean isDeactivate()
  {
    boolean bool;
    if ((mAction != null) && (mAction.equals("#"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isErasure()
  {
    boolean bool;
    if ((mAction != null) && (mAction.equals("##"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isInterrogate()
  {
    boolean bool;
    if ((mAction != null) && (mAction.equals("*#"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isMMI()
  {
    boolean bool;
    if (mPoundString != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPendingUSSD()
  {
    return mIsPendingUSSD;
  }
  
  public boolean isPinPukCommand()
  {
    boolean bool;
    if ((mSc != null) && ((mSc.equals("04")) || (mSc.equals("042")) || (mSc.equals("05")) || (mSc.equals("052")))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isRegister()
  {
    boolean bool;
    if ((mAction != null) && (mAction.equals("**"))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isShortCode()
  {
    boolean bool;
    if ((mPoundString == null) && (mDialingNumber != null) && (mDialingNumber.length() <= 2)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSsInfo()
  {
    return mIsSsInfo;
  }
  
  public boolean isTemporaryModeCLIR()
  {
    boolean bool;
    if ((mSc != null) && (mSc.equals("31")) && (mDialingNumber != null) && ((isActivate()) || (isDeactivate()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUssdRequest()
  {
    return mIsUssdRequest;
  }
  
  public void onUssdFinished(String paramString, boolean paramBoolean)
  {
    if (mState == MmiCode.State.PENDING)
    {
      if (TextUtils.isEmpty(paramString))
      {
        Rlog.d("GsmMmiCode", "onUssdFinished: no network provided message; using default.");
        mMessage = mContext.getText(17040448);
      }
      else
      {
        mMessage = paramString;
      }
      mIsUssdRequest = paramBoolean;
      if (!paramBoolean) {
        mState = MmiCode.State.COMPLETE;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onUssdFinished: ussdMessage=");
      localStringBuilder.append(paramString);
      Rlog.d("GsmMmiCode", localStringBuilder.toString());
      mPhone.onMMIDone(this);
    }
  }
  
  public void onUssdFinishedError()
  {
    if (mState == MmiCode.State.PENDING)
    {
      mState = MmiCode.State.FAILED;
      mMessage = mContext.getText(17040449);
      Rlog.d("GsmMmiCode", "onUssdFinishedError");
      mPhone.onMMIDone(this);
    }
  }
  
  public void onUssdRelease()
  {
    if (mState == MmiCode.State.PENDING)
    {
      mState = MmiCode.State.COMPLETE;
      mMessage = null;
      Rlog.d("GsmMmiCode", "onUssdRelease");
      mPhone.onMMIDone(this);
    }
  }
  
  void parseSsData(SsData paramSsData)
  {
    Object localObject = CommandException.fromRilErrno(result);
    mSc = getScStringFromScType(serviceType);
    mAction = getActionStringFromReqType(requestType);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("parseSsData msc = ");
    localStringBuilder.append(mSc);
    localStringBuilder.append(", action = ");
    localStringBuilder.append(mAction);
    localStringBuilder.append(", ex = ");
    localStringBuilder.append(localObject);
    Rlog.d("GsmMmiCode", localStringBuilder.toString());
    switch (1.$SwitchMap$com$android$internal$telephony$gsm$SsData$RequestType[requestType.ordinal()])
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Invaid requestType in SSData : ");
      ((StringBuilder)localObject).append(requestType);
      Rlog.e("GsmMmiCode", ((StringBuilder)localObject).toString());
      break;
    case 5: 
      if (serviceType.isTypeClir())
      {
        Rlog.d("GsmMmiCode", "CLIR INTERROGATION");
        onGetClirComplete(new AsyncResult(null, ssInfo, (Throwable)localObject));
      }
      else if (serviceType.isTypeCF())
      {
        Rlog.d("GsmMmiCode", "CALL FORWARD INTERROGATION");
        onQueryCfComplete(new AsyncResult(null, cfInfo, (Throwable)localObject));
      }
      else
      {
        onQueryComplete(new AsyncResult(null, ssInfo, (Throwable)localObject));
      }
      break;
    case 1: 
    case 2: 
    case 3: 
    case 4: 
      if ((result == 0) && (serviceType.isTypeUnConditional()))
      {
        boolean bool;
        if (((requestType == SsData.RequestType.SS_ACTIVATION) || (requestType == SsData.RequestType.SS_REGISTRATION)) && (isServiceClassVoiceorNone(serviceClass))) {
          bool = true;
        } else {
          bool = false;
        }
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("setVoiceCallForwardingFlag cffEnabled: ");
        localStringBuilder.append(bool);
        Rlog.d("GsmMmiCode", localStringBuilder.toString());
        if (mIccRecords != null)
        {
          mPhone.setVoiceCallForwardingFlag(1, bool, null);
          Rlog.d("GsmMmiCode", "setVoiceCallForwardingFlag done from SS Info.");
        }
        else
        {
          Rlog.e("GsmMmiCode", "setVoiceCallForwardingFlag aborted. sim records is null.");
        }
      }
      onSetComplete(null, new AsyncResult(null, cfInfo, (Throwable)localObject));
    }
  }
  
  public void processCode()
    throws CallStateException
  {
    try
    {
      if (isShortCode())
      {
        Rlog.d("GsmMmiCode", "processCode: isShortCode");
        sendUssd(mDialingNumber);
      }
      else
      {
        if (mDialingNumber != null) {
          break label1285;
        }
        if ((mSc != null) && (mSc.equals("30")))
        {
          Rlog.d("GsmMmiCode", "processCode: is CLIP");
          if (isInterrogate())
          {
            mPhone.mCi.queryCLIP(obtainMessage(5, this));
          }
          else
          {
            localObject1 = new java/lang/RuntimeException;
            ((RuntimeException)localObject1).<init>("Invalid or Unsupported MMI Code");
            throw ((Throwable)localObject1);
          }
        }
        else
        {
          localObject1 = mSc;
          int i = 1;
          if ((localObject1 != null) && (mSc.equals("31")))
          {
            Rlog.d("GsmMmiCode", "processCode: is CLIR");
            if (isActivate())
            {
              mPhone.mCi.setCLIR(1, obtainMessage(1, this));
            }
            else if (isDeactivate())
            {
              mPhone.mCi.setCLIR(2, obtainMessage(1, this));
            }
            else if (isInterrogate())
            {
              mPhone.mCi.getCLIR(obtainMessage(2, this));
            }
            else
            {
              localObject1 = new java/lang/RuntimeException;
              ((RuntimeException)localObject1).<init>("Invalid or Unsupported MMI Code");
              throw ((Throwable)localObject1);
            }
          }
          else
          {
            int n;
            if (isServiceCodeCallForwarding(mSc))
            {
              Rlog.d("GsmMmiCode", "processCode: is CF");
              localObject1 = mSia;
              int j = siToServiceClass(mSib);
              int k = scToCallForwardReason(mSc);
              int m = siToTime(mSic);
              if (isInterrogate())
              {
                mPhone.mCi.queryCallForwardStatus(k, j, (String)localObject1, obtainMessage(3, this));
              }
              else
              {
                if (isActivate())
                {
                  if (isEmptyOrNull((CharSequence)localObject1))
                  {
                    n = 1;
                    mIsCallFwdReg = false;
                  }
                  else
                  {
                    n = 3;
                    mIsCallFwdReg = true;
                  }
                }
                else
                {
                  if (isDeactivate()) {
                    n = 0;
                  }
                  for (;;)
                  {
                    break;
                    if (isRegister())
                    {
                      n = 3;
                    }
                    else
                    {
                      if (!isErasure()) {
                        break label462;
                      }
                      n = 4;
                    }
                  }
                }
                int i1 = i;
                if (n != 1) {
                  if (n == 3) {
                    i1 = i;
                  } else {
                    i1 = 0;
                  }
                }
                Rlog.d("GsmMmiCode", "processCode: is CF setCallForward");
                mPhone.mCi.setCallForward(n, k, j, (String)localObject1, m, obtainMessage(6, isVoiceUnconditionalForwarding(k, j), i1, this));
              }
              return;
              label462:
              localObject1 = new java/lang/RuntimeException;
              ((RuntimeException)localObject1).<init>("invalid action");
              throw ((Throwable)localObject1);
            }
            else
            {
              Object localObject2;
              if (isServiceCodeCallBarring(mSc))
              {
                localObject2 = mSia;
                n = siToServiceClass(mSib);
                localObject1 = scToBarringFacility(mSc);
                if (isInterrogate())
                {
                  mPhone.mCi.queryFacilityLock((String)localObject1, (String)localObject2, n, obtainMessage(5, this));
                }
                else
                {
                  if ((!isActivate()) && (!isDeactivate()))
                  {
                    localObject1 = new java/lang/RuntimeException;
                    ((RuntimeException)localObject1).<init>("Invalid or Unsupported MMI Code");
                    throw ((Throwable)localObject1);
                  }
                  mPhone.mCi.setFacilityLock((String)localObject1, isActivate(), (String)localObject2, n, obtainMessage(1, this));
                }
              }
              else
              {
                String str;
                if ((mSc != null) && (mSc.equals("03")))
                {
                  str = mSib;
                  localObject2 = mSic;
                  if ((!isActivate()) && (!isRegister()))
                  {
                    localObject1 = new java/lang/RuntimeException;
                    ((RuntimeException)localObject1).<init>("Invalid or Unsupported MMI Code");
                    throw ((Throwable)localObject1);
                  }
                  mAction = "**";
                  if (mSia == null) {
                    localObject1 = "AB";
                  } else {
                    localObject1 = scToBarringFacility(mSia);
                  }
                  if (((String)localObject2).equals(mPwd)) {
                    mPhone.mCi.changeBarringPassword((String)localObject1, str, (String)localObject2, obtainMessage(1, this));
                  } else {
                    handlePasswordError(17040539);
                  }
                }
                else if ((mSc != null) && (mSc.equals("43")))
                {
                  n = siToServiceClass(mSia);
                  if ((!isActivate()) && (!isDeactivate()))
                  {
                    if (isInterrogate())
                    {
                      mPhone.mCi.queryCallWaiting(n, obtainMessage(5, this));
                    }
                    else
                    {
                      localObject1 = new java/lang/RuntimeException;
                      ((RuntimeException)localObject1).<init>("Invalid or Unsupported MMI Code");
                      throw ((Throwable)localObject1);
                    }
                  }
                  else {
                    mPhone.mCi.setCallWaiting(isActivate(), n, obtainMessage(1, this));
                  }
                }
                else if (isPinPukCommand())
                {
                  str = mSia;
                  localObject1 = mSib;
                  n = ((String)localObject1).length();
                  if (isRegister())
                  {
                    if (!((String)localObject1).equals(mSic)) {
                      handlePasswordError(17040439);
                    } else if ((n >= 4) && (n <= 8))
                    {
                      if ((mSc.equals("04")) && (mUiccApplication != null) && (mUiccApplication.getState() == IccCardApplicationStatus.AppState.APPSTATE_PUK))
                      {
                        handlePasswordError(17040457);
                      }
                      else if (mUiccApplication != null)
                      {
                        localObject2 = new java/lang/StringBuilder;
                        ((StringBuilder)localObject2).<init>();
                        ((StringBuilder)localObject2).append("processCode: process mmi service code using UiccApp sc=");
                        ((StringBuilder)localObject2).append(mSc);
                        Rlog.d("GsmMmiCode", ((StringBuilder)localObject2).toString());
                        if (mSc.equals("04"))
                        {
                          mUiccApplication.changeIccLockPassword(str, (String)localObject1, obtainMessage(1, this));
                        }
                        else if (mSc.equals("042"))
                        {
                          mUiccApplication.changeIccFdnPassword(str, (String)localObject1, obtainMessage(1, this));
                        }
                        else if (mSc.equals("05"))
                        {
                          mUiccApplication.supplyPuk(str, (String)localObject1, obtainMessage(1, this));
                        }
                        else if (mSc.equals("052"))
                        {
                          mUiccApplication.supplyPuk2(str, (String)localObject1, obtainMessage(1, this));
                        }
                        else
                        {
                          localObject1 = new java/lang/RuntimeException;
                          localObject2 = new java/lang/StringBuilder;
                          ((StringBuilder)localObject2).<init>();
                          ((StringBuilder)localObject2).append("uicc unsupported service code=");
                          ((StringBuilder)localObject2).append(mSc);
                          ((RuntimeException)localObject1).<init>(((StringBuilder)localObject2).toString());
                          throw ((Throwable)localObject1);
                        }
                      }
                      else
                      {
                        localObject1 = new java/lang/RuntimeException;
                        ((RuntimeException)localObject1).<init>("No application mUiccApplicaiton is null");
                        throw ((Throwable)localObject1);
                      }
                    }
                    else {
                      handlePasswordError(17040141);
                    }
                  }
                  else
                  {
                    localObject1 = new java/lang/RuntimeException;
                    localObject2 = new java/lang/StringBuilder;
                    ((StringBuilder)localObject2).<init>();
                    ((StringBuilder)localObject2).append("Ivalid register/action=");
                    ((StringBuilder)localObject2).append(mAction);
                    ((RuntimeException)localObject1).<init>(((StringBuilder)localObject2).toString());
                    throw ((Throwable)localObject1);
                  }
                }
                else
                {
                  if (mPoundString == null) {
                    break label1263;
                  }
                  sendUssd(mPoundString);
                }
              }
            }
          }
        }
      }
      return;
      label1263:
      Rlog.d("GsmMmiCode", "processCode: Invalid or Unsupported MMI Code");
      localObject1 = new java/lang/RuntimeException;
      ((RuntimeException)localObject1).<init>("Invalid or Unsupported MMI Code");
      throw ((Throwable)localObject1);
      label1285:
      localObject1 = new java/lang/RuntimeException;
      ((RuntimeException)localObject1).<init>("Invalid or Unsupported MMI Code");
      throw ((Throwable)localObject1);
    }
    catch (RuntimeException localRuntimeException)
    {
      mState = MmiCode.State.FAILED;
      mMessage = mContext.getText(17040449);
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("processCode: RuntimeException=");
      ((StringBuilder)localObject1).append(localRuntimeException);
      Rlog.d("GsmMmiCode", ((StringBuilder)localObject1).toString());
      mPhone.onMMIDone(this);
    }
  }
  
  public void processSsData(AsyncResult paramAsyncResult)
  {
    Rlog.d("GsmMmiCode", "In processSsData");
    mIsSsInfo = true;
    try
    {
      parseSsData((SsData)result);
    }
    catch (NullPointerException paramAsyncResult)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Null Pointer Exception in parsing SS Data : ");
      localStringBuilder.append(paramAsyncResult);
      Rlog.e("GsmMmiCode", localStringBuilder.toString());
    }
    catch (ClassCastException localClassCastException)
    {
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("Class Cast Exception in parsing SS Data : ");
      paramAsyncResult.append(localClassCastException);
      Rlog.e("GsmMmiCode", paramAsyncResult.toString());
    }
  }
  
  public void sendUssd(String paramString)
  {
    mIsPendingUSSD = true;
    mPhone.mCi.sendUSSD(paramString, obtainMessage(4, this));
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder("GsmMmiCode {");
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("State=");
    localStringBuilder2.append(getState());
    localStringBuilder1.append(localStringBuilder2.toString());
    if (mAction != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" action=");
      localStringBuilder2.append(mAction);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mSc != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" sc=");
      localStringBuilder2.append(mSc);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mSia != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" sia=");
      localStringBuilder2.append(Rlog.pii("GsmMmiCode", mSia));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mSib != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" sib=");
      localStringBuilder2.append(Rlog.pii("GsmMmiCode", mSib));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mSic != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" sic=");
      localStringBuilder2.append(Rlog.pii("GsmMmiCode", mSic));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mPoundString != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" poundString=");
      localStringBuilder2.append(Rlog.pii("GsmMmiCode", mPoundString));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mDialingNumber != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" dialingNumber=");
      localStringBuilder2.append(Rlog.pii("GsmMmiCode", mDialingNumber));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mPwd != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" pwd=");
      localStringBuilder2.append(Rlog.pii("GsmMmiCode", mPwd));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mCallbackReceiver != null) {
      localStringBuilder1.append(" hasReceiver");
    }
    localStringBuilder1.append("}");
    return localStringBuilder1.toString();
  }
}
