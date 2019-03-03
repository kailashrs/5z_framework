package com.android.internal.telephony.imsphone;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import com.android.ims.ImsException;
import com.android.ims.ImsUtInterface;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.MmiCode.State;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccRecords;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImsPhoneMmiCode
  extends Handler
  implements MmiCode
{
  private static final String ACTION_ACTIVATE = "*";
  private static final String ACTION_DEACTIVATE = "#";
  private static final String ACTION_ERASURE = "##";
  private static final String ACTION_INTERROGATE = "*#";
  private static final String ACTION_REGISTER = "**";
  private static final int CLIR_DEFAULT = 0;
  private static final int CLIR_INVOCATION = 1;
  private static final int CLIR_NOT_PROVISIONED = 0;
  private static final int CLIR_PRESENTATION_ALLOWED_TEMPORARY = 4;
  private static final int CLIR_PRESENTATION_RESTRICTED_TEMPORARY = 3;
  private static final int CLIR_PROVISIONED_PERMANENT = 1;
  private static final int CLIR_SUPPRESSION = 2;
  private static final char END_OF_USSD_COMMAND = '#';
  private static final int EVENT_GET_CLIR_COMPLETE = 6;
  private static final int EVENT_QUERY_CF_COMPLETE = 1;
  private static final int EVENT_QUERY_COMPLETE = 3;
  private static final int EVENT_QUERY_ICB_COMPLETE = 10;
  private static final int EVENT_SET_CFF_COMPLETE = 4;
  private static final int EVENT_SET_COMPLETE = 0;
  private static final int EVENT_SUPP_SVC_QUERY_COMPLETE = 7;
  private static final int EVENT_USSD_CANCEL_COMPLETE = 5;
  private static final int EVENT_USSD_COMPLETE = 2;
  static final String IcbAnonymousMmi = "Anonymous Incoming Call Barring";
  static final String IcbDnMmi = "Specific Incoming Call Barring";
  static final String LOG_TAG = "ImsPhoneMmiCode";
  private static final int MATCH_GROUP_ACTION = 2;
  private static final int MATCH_GROUP_DIALING_NUMBER = 12;
  private static final int MATCH_GROUP_POUND_STRING = 1;
  private static final int MATCH_GROUP_PWD_CONFIRM = 11;
  private static final int MATCH_GROUP_SERVICE_CODE = 3;
  private static final int MATCH_GROUP_SIA = 5;
  private static final int MATCH_GROUP_SIB = 7;
  private static final int MATCH_GROUP_SIC = 9;
  private static final int MAX_LENGTH_SHORT_CODE = 2;
  private static final int NUM_PRESENTATION_ALLOWED = 0;
  private static final int NUM_PRESENTATION_RESTRICTED = 1;
  private static final String SC_BAIC = "35";
  private static final String SC_BAICa = "157";
  private static final String SC_BAICr = "351";
  private static final String SC_BAOC = "33";
  private static final String SC_BAOIC = "331";
  private static final String SC_BAOICxH = "332";
  private static final String SC_BA_ALL = "330";
  private static final String SC_BA_MO = "333";
  private static final String SC_BA_MT = "353";
  private static final String SC_BS_MT = "156";
  private static final String SC_CFB = "67";
  private static final String SC_CFNR = "62";
  private static final String SC_CFNRy = "61";
  private static final String SC_CFU = "21";
  private static final String SC_CFUT = "22";
  private static final String SC_CF_All = "002";
  private static final String SC_CF_All_Conditional = "004";
  private static final String SC_CLIP = "30";
  private static final String SC_CLIR = "31";
  private static final String SC_CNAP = "300";
  private static final String SC_COLP = "76";
  private static final String SC_COLR = "77";
  private static final String SC_PIN = "04";
  private static final String SC_PIN2 = "042";
  private static final String SC_PUK = "05";
  private static final String SC_PUK2 = "052";
  private static final String SC_PWD = "03";
  private static final String SC_WAIT = "43";
  public static final String UT_BUNDLE_KEY_CLIR = "queryClir";
  public static final String UT_BUNDLE_KEY_SSINFO = "imsSsInfo";
  private static Pattern sPatternSuppService = Pattern.compile("((\\*|#|\\*#|\\*\\*|##)(\\d{2,3})(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*)(\\*([^*#]*))?)?)?)?#)(.*)");
  private static String[] sTwoDigitNumberPattern;
  private String mAction;
  private ResultReceiver mCallbackReceiver;
  private Context mContext;
  private String mDialingNumber;
  private IccRecords mIccRecords;
  private boolean mIsCallFwdReg;
  private boolean mIsPendingUSSD;
  private boolean mIsSsInfo = false;
  private boolean mIsUssdRequest;
  private CharSequence mMessage;
  private ImsPhone mPhone;
  private String mPoundString;
  private String mPwd;
  private String mSc;
  private String mSia;
  private String mSib;
  private String mSic;
  private MmiCode.State mState = MmiCode.State.PENDING;
  
  public ImsPhoneMmiCode(ImsPhone paramImsPhone)
  {
    super(paramImsPhone.getHandler().getLooper());
    mPhone = paramImsPhone;
    mContext = paramImsPhone.getContext();
    mIccRecords = mPhone.mDefaultPhone.getIccRecords();
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
  
  private String getActionStringFromReqType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 4: 
      return "##";
    case 3: 
      return "**";
    case 2: 
      return "*#";
    case 1: 
      return "#";
    }
    return "*";
  }
  
  private CharSequence getErrorMessage(AsyncResult paramAsyncResult)
  {
    paramAsyncResult = getMmiErrorMessage(paramAsyncResult);
    if (paramAsyncResult == null) {
      paramAsyncResult = mContext.getText(17040449);
    }
    return paramAsyncResult;
  }
  
  private CharSequence getImsErrorMessage(AsyncResult paramAsyncResult)
  {
    ImsException localImsException = (ImsException)exception;
    CharSequence localCharSequence = getMmiErrorMessage(paramAsyncResult);
    if (localCharSequence != null) {
      return localCharSequence;
    }
    if (localImsException.getMessage() != null) {
      return localImsException.getMessage();
    }
    return getErrorMessage(paramAsyncResult);
  }
  
  private CharSequence getMmiErrorMessage(AsyncResult paramAsyncResult)
  {
    if ((exception instanceof ImsException))
    {
      int i = ((ImsException)exception).getCode();
      if (i != 241)
      {
        if (i != 802)
        {
          switch (i)
          {
          default: 
            return null;
          case 825: 
            return mContext.getText(17041078);
          case 824: 
            return mContext.getText(17041079);
          case 823: 
            return mContext.getText(17041080);
          }
          return mContext.getText(17041077);
        }
        return mContext.getText(17040982);
      }
      return mContext.getText(17040451);
    }
    if ((exception instanceof CommandException))
    {
      paramAsyncResult = (CommandException)exception;
      if (paramAsyncResult.getCommandError() == CommandException.Error.FDN_CHECK_FAILURE) {
        return mContext.getText(17040451);
      }
      if (paramAsyncResult.getCommandError() == CommandException.Error.SS_MODIFIED_TO_DIAL) {
        return mContext.getText(17041077);
      }
      if (paramAsyncResult.getCommandError() == CommandException.Error.SS_MODIFIED_TO_USSD) {
        return mContext.getText(17041080);
      }
      if (paramAsyncResult.getCommandError() == CommandException.Error.SS_MODIFIED_TO_SS) {
        return mContext.getText(17041079);
      }
      if (paramAsyncResult.getCommandError() == CommandException.Error.SS_MODIFIED_TO_DIAL_VIDEO) {
        return mContext.getText(17041078);
      }
    }
    return null;
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
      if (mSc.equals("03")) {
        return mContext.getText(17039410);
      }
      if (mSc.equals("43")) {
        return mContext.getText(17039401);
      }
      if (mSc.equals("30")) {
        return mContext.getText(17039394);
      }
      if (mSc.equals("31")) {
        return mContext.getText(17039395);
      }
      if (mSc.equals("76")) {
        return mContext.getText(17039399);
      }
      if (mSc.equals("77")) {
        return mContext.getText(17039400);
      }
      if (mSc.equals("156")) {
        return "Specific Incoming Call Barring";
      }
      if (mSc.equals("157")) {
        return "Anonymous Incoming Call Barring";
      }
    }
    return "";
  }
  
  private String getScStringFromScType(int paramInt)
  {
    switch (paramInt)
    {
    case 6: 
    default: 
      return null;
    case 22: 
      return "157";
    case 21: 
      return "156";
    case 20: 
      return "353";
    case 19: 
      return "333";
    case 18: 
      return "330";
    case 17: 
      return "351";
    case 16: 
      return "35";
    case 15: 
      return "332";
    case 14: 
      return "331";
    case 13: 
      return "33";
    case 12: 
      return "43";
    case 11: 
      return "300";
    case 10: 
      return "77";
    case 9: 
      return "76";
    case 8: 
      return "31";
    case 7: 
      return "30";
    case 5: 
      return "004";
    case 4: 
      return "002";
    case 3: 
      return "62";
    case 2: 
      return "61";
    case 1: 
      return "67";
    }
    return "21";
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
  
  static boolean isScMatchesSuppServType(String paramString)
  {
    boolean bool1 = false;
    paramString = sPatternSuppService.matcher(paramString);
    boolean bool2 = bool1;
    if (paramString.matches())
    {
      paramString = makeEmptyNull(paramString.group(3));
      if (paramString.equals("22"))
      {
        bool2 = true;
      }
      else
      {
        bool2 = bool1;
        if (paramString.equals("156")) {
          bool2 = true;
        }
      }
    }
    return bool2;
  }
  
  private boolean isServiceClassVoiceVideoOrNone(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 0)
    {
      bool2 = bool1;
      if (paramInt != 1) {
        if (paramInt == 80) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
      }
    }
    return bool2;
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
  
  private static boolean isShortCode(String paramString, ImsPhone paramImsPhone)
  {
    if (paramString == null) {
      return false;
    }
    if (paramString.length() == 0) {
      return false;
    }
    if (PhoneNumberUtils.isLocalEmergencyNumber(paramImsPhone.getContext(), paramString)) {
      return false;
    }
    return isShortCodeUSSD(paramString, paramImsPhone);
  }
  
  private static boolean isShortCodeUSSD(String paramString, ImsPhone paramImsPhone)
  {
    if ((paramString != null) && (paramString.length() <= 2))
    {
      if (paramImsPhone.isInCall()) {
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
    Rlog.d("ImsPhoneMmiCode", "isTwoDigitShortCode");
    if ((paramString != null) && (paramString.length() <= 2))
    {
      if (sTwoDigitNumberPattern == null) {
        sTwoDigitNumberPattern = paramContext.getResources().getStringArray(17236053);
      }
      for (paramContext : sTwoDigitNumberPattern)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Two Digit Number Pattern ");
        localStringBuilder.append(paramContext);
        Rlog.d("ImsPhoneMmiCode", localStringBuilder.toString());
        if (paramString.equals(paramContext))
        {
          Rlog.d("ImsPhoneMmiCode", "Two Digit Number Pattern -true");
          return true;
        }
      }
      Rlog.d("ImsPhoneMmiCode", "Two Digit Number Pattern -false");
      return false;
    }
    return false;
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
    String str1 = PhoneNumberUtils.stringFromStringAndTOA(number, toa);
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
  
  static ImsPhoneMmiCode newFromDialString(String paramString, ImsPhone paramImsPhone)
  {
    return newFromDialString(paramString, paramImsPhone, null);
  }
  
  static ImsPhoneMmiCode newFromDialString(String paramString, ImsPhone paramImsPhone, ResultReceiver paramResultReceiver)
  {
    ImsPhoneMmiCode localImsPhoneMmiCode = null;
    String str = paramString;
    if (paramImsPhone.getDefaultPhone().getServiceState().getVoiceRoaming())
    {
      str = paramString;
      if (paramImsPhone.getDefaultPhone().supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming()) {
        str = convertCdmaMmiCodesTo3gppMmiCodes(paramString);
      }
    }
    paramString = sPatternSuppService.matcher(str);
    if (paramString.matches())
    {
      localImsPhoneMmiCode = new ImsPhoneMmiCode(paramImsPhone);
      mPoundString = makeEmptyNull(paramString.group(1));
      mAction = makeEmptyNull(paramString.group(2));
      mSc = makeEmptyNull(paramString.group(3));
      mSia = makeEmptyNull(paramString.group(5));
      mSib = makeEmptyNull(paramString.group(7));
      mSic = makeEmptyNull(paramString.group(9));
      mPwd = makeEmptyNull(paramString.group(11));
      mDialingNumber = makeEmptyNull(paramString.group(12));
      mCallbackReceiver = paramResultReceiver;
      paramString = localImsPhoneMmiCode;
      if (mDialingNumber != null)
      {
        paramString = localImsPhoneMmiCode;
        if (mDialingNumber.endsWith("#"))
        {
          paramString = localImsPhoneMmiCode;
          if (str.endsWith("#"))
          {
            paramString = new ImsPhoneMmiCode(paramImsPhone);
            mPoundString = str;
          }
        }
      }
    }
    else if (str.endsWith("#"))
    {
      paramString = new ImsPhoneMmiCode(paramImsPhone);
      mPoundString = str;
    }
    else if (isTwoDigitShortCode(paramImsPhone.getContext(), str))
    {
      paramString = null;
    }
    else
    {
      paramString = localImsPhoneMmiCode;
      if (isShortCode(str, paramImsPhone))
      {
        paramString = new ImsPhoneMmiCode(paramImsPhone);
        mDialingNumber = str;
      }
    }
    return paramString;
  }
  
  static ImsPhoneMmiCode newFromUssdUserInput(String paramString, ImsPhone paramImsPhone)
  {
    paramImsPhone = new ImsPhoneMmiCode(paramImsPhone);
    mMessage = paramString;
    mState = MmiCode.State.PENDING;
    mIsPendingUSSD = true;
    return paramImsPhone;
  }
  
  static ImsPhoneMmiCode newNetworkInitiatedUssd(String paramString, boolean paramBoolean, ImsPhone paramImsPhone)
  {
    paramImsPhone = new ImsPhoneMmiCode(paramImsPhone);
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
    return paramImsPhone;
  }
  
  private void onIcbQueryComplete(AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("onIcbQueryComplete mmi=");
    localStringBuilder1.append(this);
    Rlog.d("ImsPhoneMmiCode", localStringBuilder1.toString());
    localStringBuilder1 = new StringBuilder(getScString());
    localStringBuilder1.append("\n");
    if (exception != null)
    {
      mState = MmiCode.State.FAILED;
      if ((exception instanceof ImsException)) {
        localStringBuilder1.append(getImsErrorMessage(paramAsyncResult));
      } else {
        localStringBuilder1.append(getErrorMessage(paramAsyncResult));
      }
    }
    else
    {
      paramAsyncResult = (ImsSsInfo[])result;
      if (paramAsyncResult.length == 0)
      {
        localStringBuilder1.append(mContext.getText(17040978));
      }
      else
      {
        int i = 0;
        int j = paramAsyncResult.length;
        while (i < j)
        {
          if (paramAsyncResult[i].getIcbNum() != null)
          {
            StringBuilder localStringBuilder2 = new StringBuilder();
            localStringBuilder2.append("Num: ");
            localStringBuilder2.append(paramAsyncResult[i].getIcbNum());
            localStringBuilder2.append(" status: ");
            localStringBuilder2.append(paramAsyncResult[i].getStatus());
            localStringBuilder2.append("\n");
            localStringBuilder1.append(localStringBuilder2.toString());
          }
          else if (paramAsyncResult[i].getStatus() == 1)
          {
            localStringBuilder1.append(mContext.getText(17040979));
          }
          else
          {
            localStringBuilder1.append(mContext.getText(17040978));
          }
          i++;
        }
      }
      mState = MmiCode.State.COMPLETE;
    }
    mMessage = localStringBuilder1;
    mPhone.onMMIDone(this);
  }
  
  private void onQueryCfComplete(AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    if (exception != null)
    {
      mState = MmiCode.State.FAILED;
      if ((exception instanceof ImsException)) {
        localStringBuilder.append(getImsErrorMessage(paramAsyncResult));
      } else {
        localStringBuilder.append(getErrorMessage(paramAsyncResult));
      }
    }
    else
    {
      paramAsyncResult = (CallForwardInfo[])result;
      int i = 1;
      if ((paramAsyncResult != null) && (paramAsyncResult.length != 0))
      {
        SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
        while (i <= 128)
        {
          int j = 0;
          int k = paramAsyncResult.length;
          while (j < k)
          {
            if ((serviceClass & i) != 0)
            {
              localSpannableStringBuilder.append(makeCFQueryResultMessage(paramAsyncResult[j], i));
              localSpannableStringBuilder.append("\n");
            }
            j++;
          }
          i <<= 1;
        }
        localStringBuilder.append(localSpannableStringBuilder);
      }
      else
      {
        localStringBuilder.append(mContext.getText(17040978));
        if (mIccRecords != null) {
          mPhone.setVoiceCallForwardingFlag(1, false, null);
        }
      }
      mState = MmiCode.State.COMPLETE;
    }
    mMessage = localStringBuilder;
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("onQueryCfComplete: mmi=");
    paramAsyncResult.append(this);
    Rlog.d("ImsPhoneMmiCode", paramAsyncResult.toString());
    mPhone.onMMIDone(this);
  }
  
  private void onQueryClirComplete(AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    mState = MmiCode.State.FAILED;
    if (exception != null)
    {
      if ((exception instanceof ImsException)) {
        localStringBuilder.append(getImsErrorMessage(paramAsyncResult));
      }
    }
    else
    {
      int[] arrayOfInt = ((Bundle)result).getIntArray("queryClir");
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("onQueryClirComplete: CLIR param n=");
      paramAsyncResult.append(arrayOfInt[0]);
      paramAsyncResult.append(" m=");
      paramAsyncResult.append(arrayOfInt[1]);
      Rlog.d("ImsPhoneMmiCode", paramAsyncResult.toString());
      switch (arrayOfInt[1])
      {
      case 2: 
      default: 
        localStringBuilder.append(mContext.getText(17040449));
        mState = MmiCode.State.FAILED;
        break;
      case 4: 
        switch (arrayOfInt[0])
        {
        default: 
          localStringBuilder.append(mContext.getText(17040449));
          mState = MmiCode.State.FAILED;
          break;
        case 2: 
          localStringBuilder.append(mContext.getText(17039388));
          mState = MmiCode.State.COMPLETE;
          break;
        case 1: 
          localStringBuilder.append(mContext.getText(17039389));
          mState = MmiCode.State.COMPLETE;
          break;
        case 0: 
          localStringBuilder.append(mContext.getText(17039388));
          mState = MmiCode.State.COMPLETE;
        }
        break;
      case 3: 
        switch (arrayOfInt[0])
        {
        default: 
          localStringBuilder.append(mContext.getText(17040449));
          mState = MmiCode.State.FAILED;
          break;
        case 2: 
          localStringBuilder.append(mContext.getText(17039390));
          mState = MmiCode.State.COMPLETE;
          break;
        case 1: 
          localStringBuilder.append(mContext.getText(17039391));
          mState = MmiCode.State.COMPLETE;
          break;
        case 0: 
          localStringBuilder.append(mContext.getText(17039391));
          mState = MmiCode.State.COMPLETE;
        }
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
    paramAsyncResult.append("onQueryClirComplete mmi=");
    paramAsyncResult.append(this);
    Rlog.d("ImsPhoneMmiCode", paramAsyncResult.toString());
    mPhone.onMMIDone(this);
  }
  
  private void onQueryComplete(AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    if (exception != null)
    {
      mState = MmiCode.State.FAILED;
      if ((exception instanceof ImsException)) {
        localStringBuilder.append(getImsErrorMessage(paramAsyncResult));
      } else {
        localStringBuilder.append(getErrorMessage(paramAsyncResult));
      }
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
    paramAsyncResult.append("onQueryComplete mmi=");
    paramAsyncResult.append(this);
    Rlog.d("ImsPhoneMmiCode", paramAsyncResult.toString());
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
        paramMessage = (CommandException)exception;
        if (paramMessage.getCommandError() == CommandException.Error.PASSWORD_INCORRECT)
        {
          localStringBuilder.append(mContext.getText(17040539));
        }
        else
        {
          paramAsyncResult = getMmiErrorMessage(paramAsyncResult);
          if (paramAsyncResult != null) {
            localStringBuilder.append(paramAsyncResult);
          } else if (paramMessage.getMessage() != null) {
            localStringBuilder.append(paramMessage.getMessage());
          } else {
            localStringBuilder.append(mContext.getText(17040449));
          }
        }
      }
      else if ((exception instanceof ImsException))
      {
        localStringBuilder.append(getImsErrorMessage(paramAsyncResult));
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
    paramMessage.append("onSetComplete: mmi=");
    paramMessage.append(this);
    Rlog.d("ImsPhoneMmiCode", paramMessage.toString());
    mPhone.onMMIDone(this);
  }
  
  private void onSuppSvcQueryComplete(AsyncResult paramAsyncResult)
  {
    StringBuilder localStringBuilder = new StringBuilder(getScString());
    localStringBuilder.append("\n");
    mState = MmiCode.State.FAILED;
    if (exception != null)
    {
      if ((exception instanceof ImsException)) {
        localStringBuilder.append(getImsErrorMessage(paramAsyncResult));
      } else {
        localStringBuilder.append(getErrorMessage(paramAsyncResult));
      }
    }
    else if ((result instanceof Bundle))
    {
      Rlog.d("ImsPhoneMmiCode", "onSuppSvcQueryComplete: Received CLIP/COLP/COLR Response.");
      ImsSsInfo localImsSsInfo = (ImsSsInfo)((Bundle)result).getParcelable("imsSsInfo");
      if (localImsSsInfo != null)
      {
        paramAsyncResult = new StringBuilder();
        paramAsyncResult.append("onSuppSvcQueryComplete: ImsSsInfo mStatus = ");
        paramAsyncResult.append(localImsSsInfo.getStatus());
        Rlog.d("ImsPhoneMmiCode", paramAsyncResult.toString());
        if (localImsSsInfo.getProvisionStatus() == 0)
        {
          localStringBuilder.append(mContext.getText(17040982));
          mState = MmiCode.State.COMPLETE;
        }
        else if (localImsSsInfo.getStatus() == 0)
        {
          localStringBuilder.append(mContext.getText(17040978));
          mState = MmiCode.State.COMPLETE;
        }
        else if (localImsSsInfo.getStatus() == 1)
        {
          localStringBuilder.append(mContext.getText(17040979));
          mState = MmiCode.State.COMPLETE;
        }
        else
        {
          localStringBuilder.append(mContext.getText(17040449));
        }
      }
      else
      {
        localStringBuilder.append(mContext.getText(17040449));
      }
    }
    else
    {
      Rlog.d("ImsPhoneMmiCode", "onSuppSvcQueryComplete: Received Call Barring Response.");
      if (((int[])result)[0] == 1)
      {
        localStringBuilder.append(mContext.getText(17040979));
        mState = MmiCode.State.COMPLETE;
      }
      else
      {
        localStringBuilder.append(mContext.getText(17040978));
        mState = MmiCode.State.COMPLETE;
      }
    }
    mMessage = localStringBuilder;
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("onSuppSvcQueryComplete mmi=");
    paramAsyncResult.append(this);
    Rlog.d("ImsPhoneMmiCode", paramAsyncResult.toString());
    mPhone.onMMIDone(this);
  }
  
  private void processIcbMmiCodeForUpdate()
  {
    String str = mSia;
    String[] arrayOfString = null;
    if (str != null) {
      arrayOfString = str.split("\\$");
    }
    int i = callBarAction(str);
    try
    {
      mPhone.mCT.getUtInterface().updateCallBarring(10, i, obtainMessage(0, this), arrayOfString);
    }
    catch (ImsException localImsException)
    {
      Rlog.d("ImsPhoneMmiCode", "processIcbMmiCodeForUpdate:Could not get UT handle for updating ICB.");
    }
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
  
  public int callBarAction(String paramString)
  {
    if (isActivate()) {
      return 1;
    }
    if (isDeactivate()) {
      return 0;
    }
    if (isRegister())
    {
      if (!isEmptyOrNull(paramString)) {
        return 3;
      }
      throw new RuntimeException("invalid action");
    }
    if (isErasure()) {
      return 4;
    }
    throw new RuntimeException("invalid action");
  }
  
  public void cancel()
  {
    if ((mState != MmiCode.State.COMPLETE) && (mState != MmiCode.State.FAILED))
    {
      mState = MmiCode.State.CANCELLED;
      if (mIsPendingUSSD) {
        mPhone.cancelUSSD();
      } else {
        mPhone.onMMIDone(this);
      }
      return;
    }
  }
  
  int getCLIRMode()
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
  
  String getDialingNumber()
  {
    return mDialingNumber;
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
    int i = what;
    if (i != 10) {
      switch (i)
      {
      default: 
        break;
      case 7: 
        onSuppSvcQueryComplete((AsyncResult)obj);
        break;
      case 6: 
        onQueryClirComplete((AsyncResult)obj);
        break;
      case 5: 
        mPhone.onMMIDone(this);
        break;
      case 4: 
        AsyncResult localAsyncResult = (AsyncResult)obj;
        if ((exception == null) && (arg1 == 1))
        {
          boolean bool;
          if (arg2 == 1) {
            bool = true;
          } else {
            bool = false;
          }
          if (siToServiceClass(mSib) == 80)
          {
            mPhone.setVideoCallForwardingPreference(bool);
            mPhone.notifyCallForwardingIndicator();
          }
          else if (mIccRecords != null)
          {
            mPhone.setVoiceCallForwardingFlag(1, bool, mDialingNumber);
          }
        }
        onSetComplete(paramMessage, localAsyncResult);
        break;
      case 3: 
        onQueryComplete((AsyncResult)obj);
        break;
      case 2: 
        paramMessage = (AsyncResult)obj;
        if (exception == null) {
          break;
        }
        mState = MmiCode.State.FAILED;
        mMessage = getErrorMessage(paramMessage);
        mPhone.onMMIDone(this);
        break;
      case 1: 
        onQueryCfComplete((AsyncResult)obj);
        break;
      case 0: 
        onSetComplete(paramMessage, (AsyncResult)obj);
        break;
      }
    } else {
      onIcbQueryComplete((AsyncResult)obj);
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
  
  boolean isSupportedOverImsPhone()
  {
    if (isShortCode()) {
      return true;
    }
    if ((!isServiceCodeCallForwarding(mSc)) && (!isServiceCodeCallBarring(mSc)) && ((mSc == null) || (!mSc.equals("43"))) && ((mSc == null) || (!mSc.equals("31"))) && ((mSc == null) || (!mSc.equals("30"))) && ((mSc == null) || (!mSc.equals("77"))) && ((mSc == null) || (!mSc.equals("76"))) && ((mSc == null) || (!mSc.equals("156"))) && ((mSc == null) || (!mSc.equals("157"))))
    {
      if ((!isPinPukCommand()) && ((mSc == null) || ((!mSc.equals("03")) && (!mSc.equals("30")) && (!mSc.equals("31")))))
      {
        if (mPoundString != null) {
          return true;
        }
      }
      else {
        return false;
      }
    }
    else {
      try
      {
        int i = siToServiceClass(mSib);
        return (i == 0) || (i == 1) || (i == 80);
      }
      catch (RuntimeException localRuntimeException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Invalid service class ");
        localStringBuilder.append(localRuntimeException);
        Rlog.d("ImsPhoneMmiCode", localStringBuilder.toString());
      }
    }
    return false;
  }
  
  boolean isTemporaryModeCLIR()
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
  
  void onUssdFinished(String paramString, boolean paramBoolean)
  {
    if (mState == MmiCode.State.PENDING)
    {
      if (TextUtils.isEmpty(paramString))
      {
        mMessage = mContext.getText(17040448);
        paramString = new StringBuilder();
        paramString.append("onUssdFinished: no message; using: ");
        paramString.append(mMessage);
        Rlog.v("ImsPhoneMmiCode", paramString.toString());
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("onUssdFinished: message: ");
        localStringBuilder.append(paramString);
        Rlog.v("ImsPhoneMmiCode", localStringBuilder.toString());
        mMessage = paramString;
      }
      mIsUssdRequest = paramBoolean;
      if (!paramBoolean) {
        mState = MmiCode.State.COMPLETE;
      }
      mPhone.onMMIDone(this);
    }
  }
  
  void onUssdFinishedError()
  {
    if (mState == MmiCode.State.PENDING)
    {
      mState = MmiCode.State.FAILED;
      mMessage = mContext.getText(17040449);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onUssdFinishedError: mmi=");
      localStringBuilder.append(this);
      Rlog.d("ImsPhoneMmiCode", localStringBuilder.toString());
      mPhone.onMMIDone(this);
    }
  }
  
  void parseSsData(ImsSsData paramImsSsData)
  {
    Object localObject1;
    if (result != 0) {
      localObject1 = new ImsException(null, result);
    } else {
      localObject1 = null;
    }
    mSc = getScStringFromScType(serviceType);
    mAction = getActionStringFromReqType(requestType);
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("parseSsData msc = ");
    ((StringBuilder)localObject2).append(mSc);
    ((StringBuilder)localObject2).append(", action = ");
    ((StringBuilder)localObject2).append(mAction);
    ((StringBuilder)localObject2).append(", ex = ");
    ((StringBuilder)localObject2).append(localObject1);
    Rlog.d("ImsPhoneMmiCode", ((StringBuilder)localObject2).toString());
    int i = requestType;
    boolean bool1 = false;
    switch (i)
    {
    default: 
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Invaid requestType in SSData : ");
      ((StringBuilder)localObject1).append(requestType);
      Rlog.e("ImsPhoneMmiCode", ((StringBuilder)localObject1).toString());
      break;
    case 2: 
      if (paramImsSsData.isTypeClir())
      {
        Rlog.d("ImsPhoneMmiCode", "CLIR INTERROGATION");
        localObject2 = new Bundle();
        ((Bundle)localObject2).putIntArray("queryClir", paramImsSsData.getSuppServiceInfo());
        onQueryClirComplete(new AsyncResult(null, localObject2, (Throwable)localObject1));
      }
      else if (paramImsSsData.isTypeCF())
      {
        Rlog.d("ImsPhoneMmiCode", "CALL FORWARD INTERROGATION");
        onQueryCfComplete(new AsyncResult(null, mPhone.handleCfQueryResult(paramImsSsData.getCallForwardInfo()), (Throwable)localObject1));
      }
      else if (paramImsSsData.isTypeBarring())
      {
        onSuppSvcQueryComplete(new AsyncResult(null, paramImsSsData.getSuppServiceInfo(), (Throwable)localObject1));
      }
      else if ((!paramImsSsData.isTypeColr()) && (!paramImsSsData.isTypeClip()) && (!paramImsSsData.isTypeColp()))
      {
        if (paramImsSsData.isTypeIcb()) {
          onIcbQueryComplete(new AsyncResult(null, paramImsSsData.getImsSpecificSuppServiceInfo(), (Throwable)localObject1));
        } else {
          onQueryComplete(new AsyncResult(null, paramImsSsData.getSuppServiceInfo(), (Throwable)localObject1));
        }
      }
      else
      {
        paramImsSsData = new ImsSsInfo(paramImsSsData.getSuppServiceInfo()[0], null);
        localObject2 = new Bundle();
        ((Bundle)localObject2).putParcelable("imsSsInfo", paramImsSsData);
        onSuppSvcQueryComplete(new AsyncResult(null, localObject2, (Throwable)localObject1));
      }
      break;
    case 0: 
    case 1: 
    case 3: 
    case 4: 
      if ((result == 0) && (paramImsSsData.isTypeUnConditional()))
      {
        boolean bool2;
        if (requestType != 0)
        {
          bool2 = bool1;
          if (requestType != 3) {}
        }
        else
        {
          bool2 = bool1;
          if (isServiceClassVoiceVideoOrNone(serviceClass)) {
            bool2 = true;
          }
        }
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("setCallForwardingFlag cffEnabled: ");
        ((StringBuilder)localObject2).append(bool2);
        Rlog.d("ImsPhoneMmiCode", ((StringBuilder)localObject2).toString());
        if (mIccRecords != null)
        {
          Rlog.d("ImsPhoneMmiCode", "setVoiceCallForwardingFlag done from SS Info.");
          if (serviceClass == 80)
          {
            Rlog.d("ImsPhoneMmiCode", "setVideoCallForwardingFlag done from SS Info.");
            mPhone.setVideoCallForwardingPreference(bool2);
            mPhone.notifyCallForwardingIndicator();
          }
          else
          {
            Rlog.d("ImsPhoneMmiCode", "setVoiceCallForwardingFlag done from SS Info.");
            mPhone.setVoiceCallForwardingFlag(1, bool2, null);
          }
        }
        else
        {
          Rlog.e("ImsPhoneMmiCode", "setCallForwardingFlag aborted. sim records is null.");
        }
      }
      onSetComplete(null, new AsyncResult(null, paramImsSsData.getCallForwardInfo(), (Throwable)localObject1));
    }
  }
  
  public void processCode()
    throws CallStateException
  {
    try
    {
      if (!isShortCode())
      {
        boolean bool = isServiceCodeCallForwarding(mSc);
        int i = 1;
        Object localObject1;
        int n;
        if (bool)
        {
          Rlog.d("ImsPhoneMmiCode", "processCode: is CF");
          localObject1 = mSia;
          int j = scToCallForwardReason(mSc);
          int k = siToServiceClass(mSib);
          int m = siToTime(mSic);
          if (isInterrogate())
          {
            mPhone.getCallForwardingOption(j, k, obtainMessage(1, this));
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
                    break label249;
                  }
                  n = 4;
                }
              }
            }
            int i1;
            if ((j != 0) && (j != 4)) {
              i1 = 0;
            } else {
              i1 = 1;
            }
            int i2 = i;
            if (n != 1) {
              if (n == 3) {
                i2 = i;
              } else {
                i2 = 0;
              }
            }
            Rlog.d("ImsPhoneMmiCode", "processCode: is CF setCallForward");
            mPhone.setCallForwardingOption(n, j, (String)localObject1, k, m, obtainMessage(4, i1, i2, this));
          }
          return;
          label249:
          localObject1 = new java/lang/RuntimeException;
          ((RuntimeException)localObject1).<init>("invalid action");
          throw ((Throwable)localObject1);
        }
        else if (isServiceCodeCallBarring(mSc))
        {
          String str = mSia;
          localObject1 = scToBarringFacility(mSc);
          n = siToServiceClass(mSib);
          if (isInterrogate())
          {
            mPhone.getCallBarring((String)localObject1, obtainMessage(7, this), n);
          }
          else
          {
            if ((!isActivate()) && (!isDeactivate()))
            {
              localObject1 = new java/lang/RuntimeException;
              ((RuntimeException)localObject1).<init>("Invalid or Unsupported MMI Code");
              throw ((Throwable)localObject1);
            }
            mPhone.setCallBarring((String)localObject1, isActivate(), str, obtainMessage(0, this), n);
          }
        }
        else if ((mSc != null) && (mSc.equals("31")))
        {
          bool = isActivate();
          if (bool)
          {
            try
            {
              mPhone.mCT.getUtInterface().updateCLIR(1, obtainMessage(0, this));
            }
            catch (ImsException localImsException1)
            {
              Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCLIR.");
            }
          }
          else
          {
            bool = isDeactivate();
            if (bool)
            {
              try
              {
                mPhone.mCT.getUtInterface().updateCLIR(2, obtainMessage(0, this));
              }
              catch (ImsException localImsException2)
              {
                Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCLIR.");
              }
            }
            else
            {
              bool = isInterrogate();
              if (bool)
              {
                try
                {
                  mPhone.mCT.getUtInterface().queryCLIR(obtainMessage(6, this));
                }
                catch (ImsException localImsException3)
                {
                  Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for queryCLIR.");
                }
              }
              else
              {
                RuntimeException localRuntimeException1 = new java/lang/RuntimeException;
                localRuntimeException1.<init>("Invalid or Unsupported MMI Code");
                throw localRuntimeException1;
              }
            }
          }
        }
        else if ((mSc != null) && (mSc.equals("30")))
        {
          bool = isInterrogate();
          if (bool)
          {
            try
            {
              mPhone.mCT.getUtInterface().queryCLIP(obtainMessage(7, this));
            }
            catch (ImsException localImsException4)
            {
              Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for queryCLIP.");
            }
          }
          else
          {
            if ((!isActivate()) && (!isDeactivate()))
            {
              RuntimeException localRuntimeException2 = new java/lang/RuntimeException;
              localRuntimeException2.<init>("Invalid or Unsupported MMI Code");
              throw localRuntimeException2;
            }
            try
            {
              mPhone.mCT.getUtInterface().updateCLIP(isActivate(), obtainMessage(0, this));
            }
            catch (ImsException localImsException5)
            {
              Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCLIP.");
            }
          }
        }
        else if ((mSc != null) && (mSc.equals("76")))
        {
          bool = isInterrogate();
          if (bool)
          {
            try
            {
              mPhone.mCT.getUtInterface().queryCOLP(obtainMessage(7, this));
            }
            catch (ImsException localImsException6)
            {
              Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for queryCOLP.");
            }
          }
          else
          {
            if ((!isActivate()) && (!isDeactivate()))
            {
              RuntimeException localRuntimeException3 = new java/lang/RuntimeException;
              localRuntimeException3.<init>("Invalid or Unsupported MMI Code");
              throw localRuntimeException3;
            }
            try
            {
              mPhone.mCT.getUtInterface().updateCOLP(isActivate(), obtainMessage(0, this));
            }
            catch (ImsException localImsException7)
            {
              Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCOLP.");
            }
          }
        }
        else if ((mSc != null) && (mSc.equals("77")))
        {
          bool = isActivate();
          if (bool)
          {
            try
            {
              mPhone.mCT.getUtInterface().updateCOLR(1, obtainMessage(0, this));
            }
            catch (ImsException localImsException8)
            {
              Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCOLR.");
            }
          }
          else
          {
            bool = isDeactivate();
            if (bool)
            {
              try
              {
                mPhone.mCT.getUtInterface().updateCOLR(0, obtainMessage(0, this));
              }
              catch (ImsException localImsException9)
              {
                Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for updateCOLR.");
              }
            }
            else
            {
              bool = isInterrogate();
              if (bool)
              {
                try
                {
                  mPhone.mCT.getUtInterface().queryCOLR(obtainMessage(7, this));
                }
                catch (ImsException localImsException10)
                {
                  Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for queryCOLR.");
                }
              }
              else
              {
                RuntimeException localRuntimeException4 = new java/lang/RuntimeException;
                localRuntimeException4.<init>("Invalid or Unsupported MMI Code");
                throw localRuntimeException4;
              }
            }
          }
        }
        else
        {
          if (mSc != null)
          {
            bool = mSc.equals("156");
            if (bool) {
              try
              {
                if (isInterrogate()) {
                  mPhone.mCT.getUtInterface().queryCallBarring(10, obtainMessage(10, this));
                } else {
                  processIcbMmiCodeForUpdate();
                }
              }
              catch (ImsException localImsException11)
              {
                for (;;)
                {
                  Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for ICB.");
                }
              }
            }
          }
          if (mSc != null)
          {
            bool = mSc.equals("157");
            if (bool)
            {
              n = 0;
              try
              {
                if (isInterrogate())
                {
                  mPhone.mCT.getUtInterface().queryCallBarring(6, obtainMessage(10, this));
                }
                else
                {
                  if (isActivate()) {
                    n = 1;
                  } else if (isDeactivate()) {
                    n = 0;
                  }
                  mPhone.mCT.getUtInterface().updateCallBarring(6, n, obtainMessage(0, this), null);
                }
              }
              catch (ImsException localImsException12)
              {
                Rlog.d("ImsPhoneMmiCode", "processCode: Could not get UT handle for ICBa.");
              }
              return;
            }
          }
          if ((mSc == null) || (!mSc.equals("43"))) {
            break label1298;
          }
          n = siToServiceClass(mSib);
          if ((!isActivate()) && (!isDeactivate()))
          {
            if (isInterrogate())
            {
              mPhone.getCallWaiting(obtainMessage(3, this));
            }
            else
            {
              localObject2 = new java/lang/RuntimeException;
              ((RuntimeException)localObject2).<init>("Invalid or Unsupported MMI Code");
              throw ((Throwable)localObject2);
            }
          }
          else {
            mPhone.setCallWaiting(isActivate(), n, obtainMessage(0, this));
          }
        }
        return;
        label1298:
        if (mPoundString != null)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("processCode: Sending pound string '");
          ((StringBuilder)localObject2).append(mDialingNumber);
          ((StringBuilder)localObject2).append("' over CS pipe.");
          Rlog.d("ImsPhoneMmiCode", ((StringBuilder)localObject2).toString());
          localObject2 = new com/android/internal/telephony/CallStateException;
          ((CallStateException)localObject2).<init>("cs_fallback");
          throw ((Throwable)localObject2);
        }
        Rlog.d("ImsPhoneMmiCode", "processCode: invalid or unsupported MMI");
        localObject2 = new java/lang/RuntimeException;
        ((RuntimeException)localObject2).<init>("Invalid or Unsupported MMI Code");
        throw ((Throwable)localObject2);
      }
      else
      {
        Rlog.d("ImsPhoneMmiCode", "processCode: isShortCode");
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("processCode: Sending short code '");
        ((StringBuilder)localObject2).append(mDialingNumber);
        ((StringBuilder)localObject2).append("' over CS pipe.");
        Rlog.d("ImsPhoneMmiCode", ((StringBuilder)localObject2).toString());
        localObject2 = new com/android/internal/telephony/CallStateException;
        ((CallStateException)localObject2).<init>("cs_fallback");
        throw ((Throwable)localObject2);
      }
    }
    catch (RuntimeException localRuntimeException5)
    {
      mState = MmiCode.State.FAILED;
      mMessage = mContext.getText(17040449);
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("processCode: RuntimeException = ");
      ((StringBuilder)localObject2).append(localRuntimeException5);
      Rlog.d("ImsPhoneMmiCode", ((StringBuilder)localObject2).toString());
      mPhone.onMMIDone(this);
    }
  }
  
  public void processImsSsData(AsyncResult paramAsyncResult)
    throws ImsException
  {
    try
    {
      parseSsData((ImsSsData)result);
      return;
    }
    catch (ClassCastException|NullPointerException paramAsyncResult)
    {
      throw new ImsException("Exception in parsing SS Data", 0);
    }
  }
  
  void sendUssd(String paramString)
  {
    mIsPendingUSSD = true;
    mPhone.sendUSSD(paramString, obtainMessage(2, this));
  }
  
  public void setIsSsInfo(boolean paramBoolean)
  {
    mIsSsInfo = paramBoolean;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder("ImsPhoneMmiCode {");
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
      localStringBuilder2.append(mSia);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mSib != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" sib=");
      localStringBuilder2.append(mSib);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mSic != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" sic=");
      localStringBuilder2.append(mSic);
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mPoundString != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" poundString=");
      localStringBuilder2.append(Rlog.pii("ImsPhoneMmiCode", mPoundString));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mDialingNumber != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" dialingNumber=");
      localStringBuilder2.append(Rlog.pii("ImsPhoneMmiCode", mDialingNumber));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mPwd != null)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(" pwd=");
      localStringBuilder2.append(Rlog.pii("ImsPhoneMmiCode", mPwd));
      localStringBuilder1.append(localStringBuilder2.toString());
    }
    if (mCallbackReceiver != null) {
      localStringBuilder1.append(" hasReceiver");
    }
    localStringBuilder1.append("}");
    return localStringBuilder1.toString();
  }
}
