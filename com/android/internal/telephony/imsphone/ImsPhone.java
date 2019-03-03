package com.android.internal.telephony.imsphone;

import android.app.ActivityManager;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.NetworkStats;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.ResultReceiver;
import android.os.SystemProperties;
import android.telecom.Connection.RttTextStream;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UssdResponse;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.text.TextUtils;
import com.android.ims.ImsCall;
import com.android.ims.ImsEcbm;
import com.android.ims.ImsEcbmStateListener;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.ims.ImsUtInterface;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.Call.SrvccState;
import com.android.internal.telephony.Call.State;
import com.android.internal.telephony.CallForwardInfo;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.CallTracker;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandException.Error;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.MmiCode.State;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants.State;
import com.android.internal.telephony.PhoneInternalInterface.DialArgs;
import com.android.internal.telephony.PhoneInternalInterface.DialArgs.Builder;
import com.android.internal.telephony.PhoneInternalInterface.SuppService;
import com.android.internal.telephony.PhoneNotifier;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.internal.telephony.uicc.IccRecords;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.codeaurora.ims.utils.QtiImsExtUtils;

public class ImsPhone
  extends ImsPhoneBase
{
  static final int CANCEL_ECM_TIMER = 1;
  private static final boolean DBG = true;
  private static final int DEFAULT_ECM_EXIT_TIMER_VALUE = 300000;
  private static final int EVENT_DEFAULT_PHONE_DATA_STATE_CHANGED = 52;
  private static final int EVENT_GET_CALL_BARRING_DONE = 47;
  private static final int EVENT_GET_CALL_WAITING_DONE = 49;
  private static final int EVENT_GET_CLIR_DONE = 51;
  private static final int EVENT_SERVICE_STATE_CHANGED = 53;
  private static final int EVENT_SET_CALL_BARRING_DONE = 46;
  private static final int EVENT_SET_CALL_WAITING_DONE = 48;
  private static final int EVENT_SET_CLIR_DONE = 50;
  private static final int EVENT_VOICE_CALL_ENDED = 54;
  private static final String LOG_TAG = "ImsPhone";
  static final int RESTART_ECM_TIMER = 0;
  private static final boolean VDBG = false;
  ImsPhoneCallTracker mCT;
  private Uri[] mCurrentSubscriberUris;
  Phone mDefaultPhone;
  private Registrant mEcmExitRespRegistrant;
  private Runnable mExitEcmRunnable = new Runnable()
  {
    public void run()
    {
      exitEmergencyCallbackMode();
    }
  };
  ImsExternalCallTracker mExternalCallTracker;
  private ImsEcbmStateListener mImsEcbmStateListener = new ImsEcbmStateListener()
  {
    public void onECBMEntered()
    {
      ImsPhone.this.logd("onECBMEntered");
      ImsPhone.this.handleEnterEmergencyCallbackMode();
    }
    
    public void onECBMExited()
    {
      ImsPhone.this.logd("onECBMExited");
      handleExitEmergencyCallbackMode();
    }
  };
  private boolean mImsRegistered = false;
  private String mLastDialString;
  private ArrayList<ImsPhoneMmiCode> mPendingMMIs = new ArrayList();
  private BroadcastReceiver mResultReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (getResultCode() == -1)
      {
        paramAnonymousContext = paramAnonymousIntent.getCharSequenceExtra("alertTitle");
        Object localObject = paramAnonymousIntent.getCharSequenceExtra("alertMessage");
        paramAnonymousIntent = paramAnonymousIntent.getCharSequenceExtra("notificationMessage");
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.setClassName("com.android.settings", "com.android.settings.Settings$WifiCallingSettingsActivity");
        localIntent.putExtra("alertShow", true);
        localIntent.putExtra("alertTitle", paramAnonymousContext);
        localIntent.putExtra("alertMessage", (CharSequence)localObject);
        localObject = PendingIntent.getActivity(mContext, 0, localIntent, 134217728);
        paramAnonymousContext = new Notification.Builder(mContext).setSmallIcon(17301642).setContentTitle(paramAnonymousContext).setContentText(paramAnonymousIntent).setAutoCancel(true).setContentIntent((PendingIntent)localObject).setStyle(new Notification.BigTextStyle().bigText(paramAnonymousIntent)).setChannelId("wfc").build();
        ((NotificationManager)mContext.getSystemService("notification")).notify("wifi_calling", 1, paramAnonymousContext);
      }
    }
  };
  private boolean mRoaming = false;
  private BroadcastReceiver mRttReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("org.codeaurora.intent.action.send.rtt.text".equals(paramAnonymousIntent.getAction()))
      {
        Rlog.d("ImsPhone", "RTT: Received ACTION_SEND_RTT_TEXT");
        paramAnonymousContext = paramAnonymousIntent.getStringExtra("org.codeaurora.intent.action.rtt.textvalue");
        sendRttMessage(paramAnonymousContext);
      }
      else if ("org.codeaurora.intent.action.send.rtt.operation".equals(paramAnonymousIntent.getAction()))
      {
        Rlog.d("ImsPhone", "RTT: Received ACTION_RTT_OPERATION");
        int i = paramAnonymousIntent.getIntExtra("org.codeaurora.intent.action.rtt.operation.type", 0);
        ImsPhone.this.checkIfModifyRequestOrResponse(i);
      }
      else
      {
        Rlog.d("ImsPhone", "RTT: unknown intent");
      }
    }
  };
  private ServiceState mSS = new ServiceState();
  private final RegistrantList mSilentRedialRegistrants = new RegistrantList();
  private RegistrantList mSsnRegistrants = new RegistrantList();
  private boolean mVolteMode;
  private PowerManager.WakeLock mWakeLock;
  
  public ImsPhone(Context paramContext, PhoneNotifier paramPhoneNotifier, Phone paramPhone)
  {
    this(paramContext, paramPhoneNotifier, paramPhone, false);
  }
  
  @VisibleForTesting
  public ImsPhone(Context paramContext, PhoneNotifier paramPhoneNotifier, Phone paramPhone, boolean paramBoolean)
  {
    super("ImsPhone", paramContext, paramPhoneNotifier, paramBoolean);
    mDefaultPhone = paramPhone;
    mExternalCallTracker = TelephonyComponentFactory.getInstance().makeImsExternalCallTracker(this);
    mCT = TelephonyComponentFactory.getInstance().makeImsPhoneCallTracker(this);
    mCT.registerPhoneStateListener(mExternalCallTracker);
    mExternalCallTracker.setCallPuller(mCT);
    mSS.setStateOff();
    mPhoneId = mDefaultPhone.getPhoneId();
    paramPhoneNotifier = ImsManager.getInstance(paramContext, mPhoneId);
    if (paramPhoneNotifier != null) {
      mVolteMode = paramPhoneNotifier.isVolteEnabledByPlatform();
    } else {
      logd("Could not get ImsManager instance!");
    }
    mWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, "ImsPhone");
    mWakeLock.setReferenceCounted(false);
    if (mDefaultPhone.getServiceStateTracker() != null) {
      mDefaultPhone.getServiceStateTracker().registerForDataRegStateOrRatChanged(this, 52, null);
    }
    setServiceState(1);
    mDefaultPhone.registerForServiceStateChanged(this, 53, null);
    paramContext = new IntentFilter();
    paramContext.addAction("org.codeaurora.intent.action.send.rtt.text");
    paramContext.addAction("org.codeaurora.intent.action.send.rtt.operation");
    mDefaultPhone.getContext().registerReceiver(mRttReceiver, paramContext);
  }
  
  private boolean canSendRttModifyRequest()
  {
    if (getForegroundCall().getImsCall() == null)
    {
      Rlog.d("ImsPhone", "RTT: imsCall null");
      return false;
    }
    return true;
  }
  
  private void checkIfModifyRequestOrResponse(int paramInt)
  {
    if ((isRttSupported()) && ((isRttOn()) || (isInEmergencyCall())) && (isFgCallActive()))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("RTT: checkIfModifyRequestOrResponse data =  ");
      localStringBuilder.append(paramInt);
      Rlog.d("ImsPhone", localStringBuilder.toString());
      switch (paramInt)
      {
      default: 
        break;
      case 4: 
        packRttModifyRequestToProfile(0);
        break;
      case 2: 
      case 3: 
        sendRttModifyResponse(paramInt);
        break;
      case 1: 
        packRttModifyRequestToProfile(1);
      }
      return;
    }
  }
  
  private Connection dialInternal(String paramString, PhoneInternalInterface.DialArgs paramDialArgs, ResultReceiver paramResultReceiver)
    throws CallStateException
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (intentExtras != null)
    {
      bool1 = intentExtras.getBoolean("org.codeaurora.extra.DIAL_CONFERENCE_URI", false);
      bool2 = intentExtras.getBoolean("org.codeaurora.extra.SKIP_SCHEMA_PARSING", false);
    }
    String str = paramString;
    Object localObject = str;
    if (!bool1)
    {
      localObject = str;
      if (!bool2) {
        localObject = PhoneNumberUtils.stripSeparators(paramString);
      }
    }
    if (handleInCallMmiCommands((String)localObject)) {
      return null;
    }
    if (!(paramDialArgs instanceof ImsDialArgs)) {
      paramDialArgs = ImsPhone.ImsDialArgs.Builder.from(paramDialArgs);
    } else {
      paramDialArgs = ImsPhone.ImsDialArgs.Builder.from((ImsDialArgs)paramDialArgs);
    }
    paramDialArgs.setClirMode(mCT.getClirMode());
    if (mDefaultPhone.getPhoneType() == 2) {
      return mCT.dial(paramString, paramDialArgs.build());
    }
    localObject = PhoneNumberUtils.extractNetworkPortionAlt((String)localObject);
    paramResultReceiver = ImsPhoneMmiCode.newFromDialString((String)localObject, this, paramResultReceiver);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("dialInternal: dialing w/ mmi '");
    ((StringBuilder)localObject).append(paramResultReceiver);
    ((StringBuilder)localObject).append("'...");
    logd(((StringBuilder)localObject).toString());
    if (paramResultReceiver == null) {
      return mCT.dial(paramString, paramDialArgs.build());
    }
    if (paramResultReceiver.isTemporaryModeCLIR())
    {
      paramDialArgs.setClirMode(paramResultReceiver.getCLIRMode());
      return mCT.dial(paramResultReceiver.getDialingNumber(), paramDialArgs.build());
    }
    if (paramResultReceiver.isSupportedOverImsPhone())
    {
      mPendingMMIs.add(paramResultReceiver);
      mMmiRegistrants.notifyRegistrants(new AsyncResult(null, paramResultReceiver, null));
      try
      {
        paramResultReceiver.processCode();
      }
      catch (CallStateException paramString)
      {
        if ("cs_fallback".equals(paramString.getMessage())) {
          break label299;
        }
      }
      return null;
      label299:
      logi("dialInternal: fallback to GSM required.");
      mPendingMMIs.remove(paramResultReceiver);
      throw paramString;
    }
    logi("dialInternal: USSD not supported by IMS; fallback to CS.");
    throw new CallStateException("cs_fallback");
  }
  
  private int getActionFromCFAction(int paramInt)
  {
    switch (paramInt)
    {
    case 2: 
    default: 
      return -1;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  private int getCBTypeFromFacility(String paramString)
  {
    if ("AO".equals(paramString)) {
      return 2;
    }
    if ("OI".equals(paramString)) {
      return 3;
    }
    if ("OX".equals(paramString)) {
      return 4;
    }
    if ("AI".equals(paramString)) {
      return 1;
    }
    if ("IR".equals(paramString)) {
      return 5;
    }
    if ("AB".equals(paramString)) {
      return 7;
    }
    if ("AG".equals(paramString)) {
      return 8;
    }
    if ("AC".equals(paramString)) {
      return 9;
    }
    return 0;
  }
  
  private int getCFReasonFromCondition(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 3;
    case 5: 
      return 5;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  private CallForwardInfo getCallForwardInfo(ImsCallForwardInfo paramImsCallForwardInfo)
  {
    CallForwardInfo localCallForwardInfo = new CallForwardInfo();
    status = paramImsCallForwardInfo.getStatus();
    reason = getCFReasonFromCondition(paramImsCallForwardInfo.getCondition());
    toa = paramImsCallForwardInfo.getToA();
    number = paramImsCallForwardInfo.getNumber();
    timeSeconds = paramImsCallForwardInfo.getTimeSeconds();
    if (mServiceClass == 80) {
      serviceClass = paramImsCallForwardInfo.getServiceClass();
    } else {
      serviceClass = 1;
    }
    return localCallForwardInfo;
  }
  
  private CommandException getCommandException(int paramInt, String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getCommandException code= ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(", errorString= ");
    ((StringBuilder)localObject).append(paramString);
    logd(((StringBuilder)localObject).toString());
    localObject = CommandException.Error.GENERIC_FAILURE;
    if (paramInt != 241) {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          break;
        case 825: 
          localObject = CommandException.Error.SS_MODIFIED_TO_DIAL_VIDEO;
          break;
        case 824: 
          localObject = CommandException.Error.SS_MODIFIED_TO_SS;
          break;
        case 823: 
          localObject = CommandException.Error.SS_MODIFIED_TO_USSD;
          break;
        case 822: 
          localObject = CommandException.Error.SS_MODIFIED_TO_DIAL;
          break;
        case 821: 
          localObject = CommandException.Error.PASSWORD_INCORRECT;
        }
        break;
      case 802: 
        localObject = CommandException.Error.RADIO_NOT_AVAILABLE;
        break;
      case 801: 
        localObject = CommandException.Error.REQUEST_NOT_SUPPORTED;
        break;
      }
    } else {
      localObject = CommandException.Error.FDN_CHECK_FAILURE;
    }
    return new CommandException((CommandException.Error)localObject, paramString);
  }
  
  private CommandException getCommandException(Throwable paramThrowable)
  {
    if ((paramThrowable instanceof ImsException))
    {
      paramThrowable = getCommandException(((ImsException)paramThrowable).getCode(), paramThrowable.getMessage());
    }
    else
    {
      logd("getCommandException generic failure");
      paramThrowable = new CommandException(CommandException.Error.GENERIC_FAILURE);
    }
    return paramThrowable;
  }
  
  private int getConditionFromCFReason(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return -1;
    case 5: 
      return 5;
    case 4: 
      return 4;
    case 3: 
      return 3;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  private boolean getCurrentRoaming()
  {
    return ((TelephonyManager)mContext.getSystemService("phone")).isNetworkRoaming();
  }
  
  private boolean handleCallDeflectionIncallSupplementaryService(String paramString)
  {
    if (paramString.length() > 1) {
      return false;
    }
    if (getRingingCall().getState() != Call.State.IDLE)
    {
      logd("MmiCode 0: rejectCall");
      try
      {
        mCT.rejectCall();
      }
      catch (CallStateException paramString)
      {
        Rlog.d("ImsPhone", "reject failed", paramString);
        notifySuppServiceFailed(PhoneInternalInterface.SuppService.REJECT);
      }
    }
    else if (getBackgroundCall().getState() != Call.State.IDLE)
    {
      logd("MmiCode 0: hangupWaitingOrBackground");
      try
      {
        mCT.hangup(getBackgroundCall());
      }
      catch (CallStateException paramString)
      {
        Rlog.d("ImsPhone", "hangup failed", paramString);
      }
    }
    return true;
  }
  
  private boolean handleCallHoldIncallSupplementaryService(String paramString)
  {
    int i = paramString.length();
    if (i > 2) {
      return false;
    }
    if (i > 1)
    {
      logd("separate not supported");
      notifySuppServiceFailed(PhoneInternalInterface.SuppService.SEPARATE);
    }
    else
    {
      try
      {
        if (getRingingCall().getState() != Call.State.IDLE)
        {
          logd("MmiCode 2: accept ringing call");
          mCT.acceptCall(2);
        }
        else
        {
          logd("MmiCode 2: switchWaitingOrHoldingAndActive");
          mCT.switchWaitingOrHoldingAndActive();
        }
      }
      catch (CallStateException paramString)
      {
        Rlog.d("ImsPhone", "switch failed", paramString);
        notifySuppServiceFailed(PhoneInternalInterface.SuppService.SWITCH);
      }
    }
    return true;
  }
  
  private boolean handleCallWaitingIncallSupplementaryService(String paramString)
  {
    int i = paramString.length();
    if (i > 2) {
      return false;
    }
    paramString = getForegroundCall();
    if (i > 1)
    {
      try
      {
        logd("not support 1X SEND");
        notifySuppServiceFailed(PhoneInternalInterface.SuppService.HANGUP);
      }
      catch (CallStateException paramString)
      {
        break label88;
      }
    }
    else if (paramString.getState() != Call.State.IDLE)
    {
      logd("MmiCode 1: hangup foreground");
      mCT.hangup(paramString);
    }
    else
    {
      logd("MmiCode 1: switchWaitingOrHoldingAndActive");
      mCT.switchWaitingOrHoldingAndActive();
    }
    break label105;
    label88:
    Rlog.d("ImsPhone", "hangup failed", paramString);
    notifySuppServiceFailed(PhoneInternalInterface.SuppService.HANGUP);
    label105:
    return true;
  }
  
  private int[] handleCbQueryResult(ImsSsInfo[] paramArrayOfImsSsInfo)
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 0;
    if (paramArrayOfImsSsInfo[0].getStatus() == 1) {
      arrayOfInt[0] = 1;
    }
    return arrayOfInt;
  }
  
  private boolean handleCcbsIncallSupplementaryService(String paramString)
  {
    if (paramString.length() > 1) {
      return false;
    }
    logi("MmiCode 5: CCBS not supported!");
    notifySuppServiceFailed(PhoneInternalInterface.SuppService.UNKNOWN);
    return true;
  }
  
  private int[] handleCwQueryResult(ImsSsInfo[] paramArrayOfImsSsInfo)
  {
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = 0;
    if (paramArrayOfImsSsInfo[0].getStatus() == 1)
    {
      arrayOfInt[0] = 1;
      arrayOfInt[1] = 1;
    }
    return arrayOfInt;
  }
  
  private boolean handleEctIncallSupplementaryService(String paramString)
  {
    if (paramString.length() != 1) {
      return false;
    }
    logd("MmiCode 4: not support explicit call transfer");
    notifySuppServiceFailed(PhoneInternalInterface.SuppService.TRANSFER);
    return true;
  }
  
  private void handleEnterEmergencyCallbackMode()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleEnterEmergencyCallbackMode,mIsPhoneInEcmState= ");
    localStringBuilder.append(isInEcm());
    logd(localStringBuilder.toString());
    if (!isInEcm())
    {
      setIsInEcm(true);
      sendEmergencyCallbackModeChange();
      long l = SystemProperties.getLong("ro.cdma.ecmexittimer", 300000L);
      postDelayed(mExitEcmRunnable, l);
      mWakeLock.acquire();
    }
  }
  
  private boolean handleMultipartyIncallSupplementaryService(String paramString)
  {
    if (paramString.length() > 1) {
      return false;
    }
    logd("MmiCode 3: merge calls");
    conference();
    return true;
  }
  
  private boolean isCfEnable(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1) {
      if (paramInt == 3) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private boolean isInFullRttMode()
  {
    int i = QtiImsExtUtils.getRttOperatingMode(mContext);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RTT: isInFullRttMode mode = ");
    localStringBuilder.append(i);
    Rlog.d("ImsPhone", localStringBuilder.toString());
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  private boolean isValidCommandInterfaceCFAction(int paramInt)
  {
    switch (paramInt)
    {
    case 2: 
    default: 
      return false;
    }
    return true;
  }
  
  private boolean isValidCommandInterfaceCFReason(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    }
    return true;
  }
  
  private void logd(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.d("ImsPhone", localStringBuilder.toString());
  }
  
  private void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.e("ImsPhone", localStringBuilder.toString());
  }
  
  private void logi(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.i("ImsPhone", localStringBuilder.toString());
  }
  
  private void logv(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.v("ImsPhone", localStringBuilder.toString());
  }
  
  private boolean mapRequestToResponse(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return false;
    case 3: 
      return false;
    }
    return true;
  }
  
  private void onNetworkInitiatedUssd(ImsPhoneMmiCode paramImsPhoneMmiCode)
  {
    logd("onNetworkInitiatedUssd");
    mMmiCompleteRegistrants.notifyRegistrants(new AsyncResult(null, paramImsPhoneMmiCode, null));
  }
  
  private void packRttModifyRequestToProfile(int paramInt)
  {
    if (!canSendRttModifyRequest())
    {
      Rlog.d("ImsPhone", "RTT: cannot send rtt modify request");
      return;
    }
    ImsCallProfile localImsCallProfile = getForegroundCall().getImsCall().getCallProfile();
    localImsCallProfile = new ImsCallProfile(mServiceType, mCallType);
    mMediaProfile.setRttMode(paramInt);
    Rlog.d("ImsPhone", "RTT: packRttModifyRequestToProfile");
    sendRttModifyRequest(localImsCallProfile);
  }
  
  private void processWfcDisconnectForNotification(ImsReasonInfo paramImsReasonInfo)
  {
    Object localObject1 = (CarrierConfigManager)mContext.getSystemService("carrier_config");
    if (localObject1 == null)
    {
      loge("processDisconnectReason: CarrierConfigManager is not ready");
      return;
    }
    Object localObject2 = ((CarrierConfigManager)localObject1).getConfigForSubId(getSubId());
    if (localObject2 == null)
    {
      paramImsReasonInfo = new StringBuilder();
      paramImsReasonInfo.append("processDisconnectReason: no config for subId ");
      paramImsReasonInfo.append(getSubId());
      loge(paramImsReasonInfo.toString());
      return;
    }
    String[] arrayOfString1 = ((PersistableBundle)localObject2).getStringArray("wfc_operator_error_codes_string_array");
    if (arrayOfString1 == null) {
      return;
    }
    String[] arrayOfString2 = mContext.getResources().getStringArray(17236100);
    String[] arrayOfString3 = mContext.getResources().getStringArray(17236101);
    int i = 0;
    for (int j = 0; j < arrayOfString1.length; j++)
    {
      String[] arrayOfString4 = arrayOfString1[j].split("\\|");
      if (arrayOfString4.length != 2)
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("Invalid carrier config: ");
        ((StringBuilder)localObject3).append(arrayOfString1[j]);
        loge(((StringBuilder)localObject3).toString());
      }
      do
      {
        do
        {
          break;
        } while (!mExtraMessage.startsWith(arrayOfString4[i]));
        k = arrayOfString4[i].length();
      } while ((Character.isLetterOrDigit(arrayOfString4[i].charAt(k - 1))) && (mExtraMessage.length() > k) && (Character.isLetterOrDigit(mExtraMessage.charAt(k))));
      Object localObject3 = mContext.getText(17041220);
      int k = Integer.parseInt(arrayOfString4[1]);
      if ((k >= 0) && (k < arrayOfString2.length) && (k < arrayOfString3.length))
      {
        localObject2 = mExtraMessage;
        localObject1 = mExtraMessage;
        if (!arrayOfString2[k].isEmpty()) {
          localObject2 = String.format(arrayOfString2[k], new Object[] { mExtraMessage });
        }
        if (!arrayOfString3[k].isEmpty()) {
          localObject1 = String.format(arrayOfString3[k], new Object[] { mExtraMessage });
        }
        paramImsReasonInfo = new Intent("com.android.ims.REGISTRATION_ERROR");
        paramImsReasonInfo.putExtra("alertTitle", (CharSequence)localObject3);
        paramImsReasonInfo.putExtra("alertMessage", (String)localObject2);
        paramImsReasonInfo.putExtra("notificationMessage", (String)localObject1);
        mContext.sendOrderedBroadcast(paramImsReasonInfo, null, mResultReceiver, null, -1, null, null);
        break;
      }
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("Invalid index: ");
      ((StringBuilder)localObject3).append(arrayOfString1[j]);
      loge(((StringBuilder)localObject3).toString());
    }
  }
  
  private void sendEmergencyCallbackModeChange()
  {
    Object localObject = new Intent("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
    ((Intent)localObject).putExtra("phoneinECMState", isInEcm());
    SubscriptionManager.putPhoneIdAndSubIdExtra((Intent)localObject, getPhoneId());
    ActivityManager.broadcastStickyIntent((Intent)localObject, -1);
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("sendEmergencyCallbackModeChange: isInEcm=");
    ((StringBuilder)localObject).append(isInEcm());
    logd(((StringBuilder)localObject).toString());
  }
  
  private void sendErrorResponse(Message paramMessage)
  {
    logd("sendErrorResponse");
    if (paramMessage != null)
    {
      AsyncResult.forMessage(paramMessage, null, new CommandException(CommandException.Error.GENERIC_FAILURE));
      paramMessage.sendToTarget();
    }
  }
  
  private void sendResponse(Message paramMessage, Object paramObject, Throwable paramThrowable)
  {
    if (paramMessage != null)
    {
      CommandException localCommandException = null;
      if (paramThrowable != null) {
        localCommandException = getCommandException(paramThrowable);
      }
      AsyncResult.forMessage(paramMessage, paramObject, localCommandException);
      paramMessage.sendToTarget();
    }
  }
  
  private void sendUssdResponse(String paramString, CharSequence paramCharSequence, int paramInt, ResultReceiver paramResultReceiver)
  {
    paramString = new UssdResponse(paramString, paramCharSequence);
    paramCharSequence = new Bundle();
    paramCharSequence.putParcelable("USSD_RESPONSE", paramString);
    paramResultReceiver.send(paramInt, paramCharSequence);
  }
  
  private void updateDataServiceState()
  {
    if ((mSS != null) && (mDefaultPhone.getServiceStateTracker() != null) && (mDefaultPhone.getServiceStateTracker().mSS != null))
    {
      ServiceState localServiceState = mDefaultPhone.getServiceStateTracker().mSS;
      mSS.setDataRegState(localServiceState.getDataRegState());
      mSS.setRilDataRadioTechnology(localServiceState.getRilDataRadioTechnology());
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("updateDataServiceState: defSs = ");
      localStringBuilder.append(localServiceState);
      localStringBuilder.append(" imsSs = ");
      localStringBuilder.append(mSS);
      logd(localStringBuilder.toString());
    }
  }
  
  private void updateRoamingState(boolean paramBoolean)
  {
    Object localObject;
    if (mCT.getState() == PhoneConstants.State.IDLE)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("updateRoamingState now: ");
      ((StringBuilder)localObject).append(paramBoolean);
      logd(((StringBuilder)localObject).toString());
      mRoaming = paramBoolean;
      localObject = ImsManager.getInstance(mContext, mPhoneId);
      ((ImsManager)localObject).setWfcMode(((ImsManager)localObject).getWfcMode(paramBoolean), paramBoolean);
      paramBoolean = ((ImsManager)localObject).isVolteEnabledByPlatform();
      if (mVolteMode != paramBoolean)
      {
        mVolteMode = paramBoolean;
        ((ImsManager)localObject).updateImsServiceConfig(true);
      }
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("updateRoamingState postponed: ");
      ((StringBuilder)localObject).append(paramBoolean);
      logd(((StringBuilder)localObject).toString());
      mCT.registerForVoiceCallEnded(this, 54, null);
    }
  }
  
  public void acceptCall(int paramInt)
    throws CallStateException
  {
    mCT.acceptCall(paramInt);
  }
  
  public void addParticipant(String paramString)
    throws CallStateException
  {
    mCT.addParticipant(paramString);
  }
  
  public void callEndCleanupHandOverCallIfAny()
  {
    mCT.callEndCleanupHandOverCallIfAny();
  }
  
  public boolean canConference()
  {
    return mCT.canConference();
  }
  
  public boolean canDial()
  {
    return mCT.canDial();
  }
  
  public boolean canTransfer()
  {
    return mCT.canTransfer();
  }
  
  public void cancelUSSD()
  {
    mCT.cancelUSSD();
  }
  
  public void clearDisconnected()
  {
    mCT.clearDisconnected();
  }
  
  public void conference()
  {
    mCT.conference();
  }
  
  public Connection dial(String paramString, PhoneInternalInterface.DialArgs paramDialArgs)
    throws CallStateException
  {
    return dialInternal(paramString, paramDialArgs, null);
  }
  
  public void dispose()
  {
    logd("dispose");
    mPendingMMIs.clear();
    mExternalCallTracker.tearDown();
    mCT.unregisterPhoneStateListener(mExternalCallTracker);
    mCT.unregisterForVoiceCallEnded(this);
    mCT.dispose();
    if ((mDefaultPhone != null) && (mDefaultPhone.getServiceStateTracker() != null))
    {
      mDefaultPhone.getServiceStateTracker().unregisterForDataRegStateOrRatChanged(this);
      mDefaultPhone.unregisterForServiceStateChanged(this);
      mDefaultPhone.getContext().unregisterReceiver(mRttReceiver);
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("ImsPhone extends:");
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.flush();
    paramPrintWriter.println("ImsPhone:");
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mDefaultPhone = ");
    paramFileDescriptor.append(mDefaultPhone);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mPendingMMIs = ");
    paramFileDescriptor.append(mPendingMMIs);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mPostDialHandler = ");
    paramFileDescriptor.append(mPostDialHandler);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mSS = ");
    paramFileDescriptor.append(mSS);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mWakeLock = ");
    paramFileDescriptor.append(mWakeLock);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mIsPhoneInEcmState = ");
    paramFileDescriptor.append(isInEcm());
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mEcmExitRespRegistrant = ");
    paramFileDescriptor.append(mEcmExitRespRegistrant);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mSilentRedialRegistrants = ");
    paramFileDescriptor.append(mSilentRedialRegistrants);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mImsRegistered = ");
    paramFileDescriptor.append(mImsRegistered);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mRoaming = ");
    paramFileDescriptor.append(mRoaming);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append("  mSsnRegistrants = ");
    paramFileDescriptor.append(mSsnRegistrants);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramPrintWriter.flush();
  }
  
  public void exitEmergencyCallbackMode()
  {
    if (mWakeLock.isHeld()) {
      mWakeLock.release();
    }
    logd("exitEmergencyCallbackMode()");
    try
    {
      mCT.getEcbmInterface().exitEmergencyCallbackMode();
    }
    catch (ImsException localImsException)
    {
      localImsException.printStackTrace();
    }
  }
  
  public void explicitCallTransfer()
  {
    mCT.explicitCallTransfer();
  }
  
  public ImsPhoneCall getBackgroundCall()
  {
    return mCT.mBackgroundCall;
  }
  
  public void getCallBarring(String paramString, Message paramMessage)
  {
    getCallBarring(paramString, paramMessage, 0);
  }
  
  public void getCallBarring(String paramString, Message paramMessage, int paramInt)
  {
    getCallBarring(paramString, "", paramMessage, paramInt);
  }
  
  public void getCallBarring(String paramString1, String paramString2, Message paramMessage, int paramInt)
  {
    paramString2 = new StringBuilder();
    paramString2.append("getCallBarring facility=");
    paramString2.append(paramString1);
    paramString2.append(", serviceClass = ");
    paramString2.append(paramInt);
    logd(paramString2.toString());
    paramString2 = obtainMessage(47, paramMessage);
    try
    {
      mCT.getUtInterface().queryCallBarring(getCBTypeFromFacility(paramString1), paramString2, paramInt);
    }
    catch (ImsException paramString1)
    {
      sendErrorResponse(paramMessage, paramString1);
    }
  }
  
  public void getCallForwardingOption(int paramInt1, int paramInt2, Message paramMessage)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getCallForwardingOption reason=");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append("serviceclass =");
    ((StringBuilder)localObject).append(paramInt2);
    Rlog.d("ImsPhone", ((StringBuilder)localObject).toString());
    if (isValidCommandInterfaceCFReason(paramInt1))
    {
      Rlog.d("ImsPhone", "requesting call forwarding query.");
      localObject = obtainMessage(13, paramMessage);
      try
      {
        mCT.getUtInterface().queryCallForward(getConditionFromCFReason(paramInt1), null, paramInt2, (Message)localObject);
      }
      catch (ImsException localImsException)
      {
        sendErrorResponse(paramMessage, localImsException);
      }
    }
    else if (paramMessage != null)
    {
      sendErrorResponse(paramMessage);
    }
  }
  
  public void getCallForwardingOption(int paramInt, Message paramMessage)
  {
    getCallForwardingOption(paramInt, 1, paramMessage);
  }
  
  public CallTracker getCallTracker()
  {
    return mCT;
  }
  
  public void getCallWaiting(Message paramMessage)
  {
    logd("getCallWaiting");
    Message localMessage = obtainMessage(49, paramMessage);
    try
    {
      mCT.getUtInterface().queryCallWaiting(localMessage);
    }
    catch (ImsException localImsException)
    {
      sendErrorResponse(paramMessage, localImsException);
    }
  }
  
  public Uri[] getCurrentSubscriberUris()
  {
    return mCurrentSubscriberUris;
  }
  
  public Phone getDefaultPhone()
  {
    return mDefaultPhone;
  }
  
  public ImsExternalCallTracker getExternalCallTracker()
  {
    return mExternalCallTracker;
  }
  
  public ImsPhoneCall getForegroundCall()
  {
    return mCT.mForegroundCall;
  }
  
  public ArrayList<Connection> getHandoverConnection()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(getForegroundCallmConnections);
    localArrayList.addAll(getBackgroundCallmConnections);
    localArrayList.addAll(getRingingCallmConnections);
    if (localArrayList.size() > 0) {
      return localArrayList;
    }
    return null;
  }
  
  public IccRecords getIccRecords()
  {
    return mDefaultPhone.getIccRecords();
  }
  
  @VisibleForTesting
  public ImsEcbmStateListener getImsEcbmStateListener()
  {
    return mImsEcbmStateListener;
  }
  
  public int getImsRegistrationTech()
  {
    return mCT.getImsRegistrationTech();
  }
  
  public boolean getMute()
  {
    return mCT.getMute();
  }
  
  public void getOutgoingCallerIdDisplay(Message paramMessage)
  {
    logd("getCLIR");
    Message localMessage = obtainMessage(51, paramMessage);
    try
    {
      mCT.getUtInterface().queryCLIR(localMessage);
    }
    catch (ImsException localImsException)
    {
      sendErrorResponse(paramMessage, localImsException);
    }
  }
  
  public List<? extends ImsPhoneMmiCode> getPendingMmiCodes()
  {
    return mPendingMMIs;
  }
  
  public int getPhoneId()
  {
    return mDefaultPhone.getPhoneId();
  }
  
  public ImsPhoneCall getRingingCall()
  {
    return mCT.mRingingCall;
  }
  
  public ServiceState getServiceState()
  {
    return mSS;
  }
  
  public PhoneConstants.State getState()
  {
    return mCT.getState();
  }
  
  public int getSubId()
  {
    return mDefaultPhone.getSubId();
  }
  
  public NetworkStats getVtDataUsage(boolean paramBoolean)
  {
    return mCT.getVtDataUsage(paramBoolean);
  }
  
  @VisibleForTesting
  public PowerManager.WakeLock getWakeLock()
  {
    return mWakeLock;
  }
  
  public CallForwardInfo[] handleCfQueryResult(ImsCallForwardInfo[] paramArrayOfImsCallForwardInfo)
  {
    IccRecords localIccRecords = null;
    Object localObject = localIccRecords;
    if (paramArrayOfImsCallForwardInfo != null)
    {
      localObject = localIccRecords;
      if (paramArrayOfImsCallForwardInfo.length != 0) {
        localObject = new CallForwardInfo[paramArrayOfImsCallForwardInfo.length];
      }
    }
    localIccRecords = mDefaultPhone.getIccRecords();
    int i;
    int j;
    if ((paramArrayOfImsCallForwardInfo != null) && (paramArrayOfImsCallForwardInfo.length != 0))
    {
      i = 0;
      j = paramArrayOfImsCallForwardInfo.length;
    }
    while (i < j)
    {
      if (paramArrayOfImsCallForwardInfo[i].getCondition() == 0)
      {
        boolean bool;
        if (mServiceClass == 80)
        {
          if (paramArrayOfImsCallForwardInfo[i].getStatus() == 1) {
            bool = true;
          } else {
            bool = false;
          }
          setVideoCallForwardingPreference(bool);
          notifyCallForwardingIndicator();
        }
        else if (localIccRecords != null)
        {
          if (paramArrayOfImsCallForwardInfo[i].getStatus() == 1) {
            bool = true;
          } else {
            bool = false;
          }
          setVoiceCallForwardingFlag(localIccRecords, 1, bool, paramArrayOfImsCallForwardInfo[i].getNumber());
        }
      }
      localObject[i] = getCallForwardInfo(paramArrayOfImsCallForwardInfo[i]);
      i++;
      continue;
      if (localIccRecords != null) {
        setVoiceCallForwardingFlag(localIccRecords, 1, false, null);
      }
    }
    return localObject;
  }
  
  protected void handleExitEmergencyCallbackMode()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleExitEmergencyCallbackMode: mIsPhoneInEcmState = ");
    localStringBuilder.append(isInEcm());
    logd(localStringBuilder.toString());
    if (isInEcm()) {
      setIsInEcm(false);
    }
    removeCallbacks(mExitEcmRunnable);
    if (mEcmExitRespRegistrant != null) {
      mEcmExitRespRegistrant.notifyResult(Boolean.TRUE);
    }
    if (mWakeLock.isHeld()) {
      mWakeLock.release();
    }
    sendEmergencyCallbackModeChange();
    ((GsmCdmaPhone)mDefaultPhone).notifyEmergencyCallRegistrants(false);
  }
  
  public boolean handleInCallMmiCommands(String paramString)
  {
    if (!isInCall()) {
      return false;
    }
    if (TextUtils.isEmpty(paramString)) {
      return false;
    }
    boolean bool = false;
    switch (paramString.charAt(0))
    {
    default: 
      break;
    case '5': 
      bool = handleCcbsIncallSupplementaryService(paramString);
      break;
    case '4': 
      bool = handleEctIncallSupplementaryService(paramString);
      break;
    case '3': 
      bool = handleMultipartyIncallSupplementaryService(paramString);
      break;
    case '2': 
      bool = handleCallHoldIncallSupplementaryService(paramString);
      break;
    case '1': 
      bool = handleCallWaitingIncallSupplementaryService(paramString);
      break;
    case '0': 
      bool = handleCallDeflectionIncallSupplementaryService(paramString);
    }
    return bool;
  }
  
  public void handleMessage(Message paramMessage)
  {
    AsyncResult localAsyncResult = (AsyncResult)obj;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("handleMessage what=");
    ((StringBuilder)localObject).append(what);
    logd(((StringBuilder)localObject).toString());
    int i = what;
    boolean bool1;
    IccRecords localIccRecords;
    switch (i)
    {
    default: 
      switch (i)
      {
      default: 
        super.handleMessage(paramMessage);
        break;
      case 54: 
        logd("Voice call ended. Handle pending updateRoamingState.");
        mCT.unregisterForVoiceCallEnded(this);
        bool1 = getCurrentRoaming();
        if (mRoaming == bool1) {
          return;
        }
        updateRoamingState(bool1);
        break;
      case 53: 
        paramMessage = (ServiceState)obj).result;
        if ((mRoaming == paramMessage.getRoaming()) || ((paramMessage.getVoiceRegState() != 0) && (paramMessage.getDataRegState() != 0))) {
          return;
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Roaming state changed- ");
        ((StringBuilder)localObject).append(mRoaming);
        logd(((StringBuilder)localObject).toString());
        updateRoamingState(paramMessage.getRoaming());
        break;
      case 52: 
        logd("EVENT_DEFAULT_PHONE_DATA_STATE_CHANGED");
        updateDataServiceState();
        break;
      case 51: 
        localObject = (Bundle)result;
        paramMessage = null;
        if (localObject != null) {
          paramMessage = ((Bundle)localObject).getIntArray("queryClir");
        }
        sendResponse((Message)userObj, paramMessage, exception);
        break;
      case 50: 
        if (exception == null) {
          saveClirSetting(arg1);
        }
        break;
      case 47: 
      case 49: 
        localIccRecords = null;
        localObject = localIccRecords;
        if (exception == null) {
          if (what == 47)
          {
            localObject = handleCbQueryResult((ImsSsInfo[])result);
          }
          else
          {
            localObject = localIccRecords;
            if (what == 49) {
              localObject = handleCwQueryResult((ImsSsInfo[])result);
            }
          }
        }
        sendResponse((Message)userObj, localObject, exception);
        break;
      }
      sendResponse((Message)userObj, null, exception);
      break;
    case 13: 
      paramMessage = null;
      if (exception == null) {
        paramMessage = handleCfQueryResult((ImsCallForwardInfo[])result);
      }
      sendResponse((Message)userObj, paramMessage, exception);
      break;
    case 12: 
      localIccRecords = mDefaultPhone.getIccRecords();
      localObject = (Cf)userObj;
      if ((mIsCfu) && (exception == null))
      {
        i = mServiceClass;
        boolean bool2 = false;
        bool1 = false;
        if (i == 80)
        {
          if (arg1 == 1) {
            bool1 = true;
          }
          setVideoCallForwardingPreference(bool1);
          notifyCallForwardingIndicator();
        }
        else if ((localIccRecords != null) && (mServiceClass == 1))
        {
          bool1 = bool2;
          if (arg1 == 1) {
            bool1 = true;
          }
          setVoiceCallForwardingFlag(localIccRecords, 1, bool1, mSetCfNumber);
        }
      }
      sendResponse(mOnComplete, null, exception);
    }
  }
  
  void handleTimerInEmergencyCallbackMode(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleTimerInEmergencyCallbackMode, unsupported action ");
      localStringBuilder.append(paramInt);
      loge(localStringBuilder.toString());
      break;
    case 1: 
      removeCallbacks(mExitEcmRunnable);
      ((GsmCdmaPhone)mDefaultPhone).notifyEcbmTimerReset(Boolean.TRUE);
      break;
    case 0: 
      long l = SystemProperties.getLong("ro.cdma.ecmexittimer", 300000L);
      postDelayed(mExitEcmRunnable, l);
      ((GsmCdmaPhone)mDefaultPhone).notifyEcbmTimerReset(Boolean.FALSE);
    }
  }
  
  public boolean handleUssdRequest(String paramString, ResultReceiver paramResultReceiver)
    throws CallStateException
  {
    Object localObject;
    if (mPendingMMIs.size() > 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("handleUssdRequest: queue full: ");
      ((StringBuilder)localObject).append(Rlog.pii("ImsPhone", paramString));
      logi(((StringBuilder)localObject).toString());
      sendUssdResponse(paramString, null, -1, paramResultReceiver);
      return true;
    }
    try
    {
      localObject = new com/android/internal/telephony/imsphone/ImsPhone$ImsDialArgs$Builder;
      ((ImsPhone.ImsDialArgs.Builder)localObject).<init>();
      dialInternal(paramString, ((ImsPhone.ImsDialArgs.Builder)localObject).build(), paramResultReceiver);
    }
    catch (Exception localException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Could not execute USSD ");
      ((StringBuilder)localObject).append(localException);
      Rlog.w("ImsPhone", ((StringBuilder)localObject).toString());
      sendUssdResponse(paramString, null, -1, paramResultReceiver);
      return false;
    }
    catch (CallStateException localCallStateException)
    {
      if ("cs_fallback".equals(localCallStateException.getMessage())) {
        break label182;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not execute USSD ");
    localStringBuilder.append(localCallStateException);
    Rlog.w("ImsPhone", localStringBuilder.toString());
    sendUssdResponse(paramString, null, -1, paramResultReceiver);
    return true;
    label182:
    throw localCallStateException;
  }
  
  void initiateSilentRedial()
  {
    AsyncResult localAsyncResult = new AsyncResult(null, mLastDialString, null);
    mSilentRedialRegistrants.notifyRegistrants(localAsyncResult);
  }
  
  public boolean isFgCallActive()
  {
    if (Call.State.ACTIVE != getForegroundCall().getState())
    {
      Rlog.d("ImsPhone", "RTT: isFgCallActive fg call not active");
      return false;
    }
    return true;
  }
  
  public boolean isImsAvailable()
  {
    return mCT.isImsServiceReady();
  }
  
  public boolean isImsRegistered()
  {
    return mImsRegistered;
  }
  
  boolean isInCall()
  {
    Call.State localState1 = getForegroundCall().getState();
    Call.State localState2 = getBackgroundCall().getState();
    Call.State localState3 = getRingingCall().getState();
    boolean bool;
    if ((!localState1.isAlive()) && (!localState2.isAlive()) && (!localState3.isAlive())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isInEcm()
  {
    return mDefaultPhone.isInEcm();
  }
  
  public boolean isInEmergencyCall()
  {
    return mCT.isInEmergencyCall();
  }
  
  public boolean isRttOn()
  {
    if (!QtiImsExtUtils.isRttOn(mContext))
    {
      Rlog.d("ImsPhone", "RTT: RTT is off");
      return false;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RTT: Rtt on = ");
    localStringBuilder.append(QtiImsExtUtils.isRttOn(mContext));
    Rlog.d("ImsPhone", localStringBuilder.toString());
    return true;
  }
  
  public boolean isRttSupported()
  {
    if (!QtiImsExtUtils.isRttSupported(mPhoneId, mContext))
    {
      Rlog.d("ImsPhone", "RTT: RTT is not supported");
      return false;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RTT: rtt supported = ");
    localStringBuilder.append(QtiImsExtUtils.isRttSupported(mPhoneId, mContext));
    localStringBuilder.append(", Rtt mode = ");
    localStringBuilder.append(QtiImsExtUtils.getRttOperatingMode(mContext));
    Rlog.d("ImsPhone", localStringBuilder.toString());
    return true;
  }
  
  public boolean isRttVtCallAllowed(ImsCall paramImsCall)
  {
    int i = QtiImsExtUtils.getRttOperatingMode(mContext);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("RTT: isRttVtCallAllowed mode = ");
    localStringBuilder.append(i);
    Rlog.d("ImsPhone", localStringBuilder.toString());
    return (!paramImsCall.getCallProfile().isVideoCall()) || (QtiImsExtUtils.isRttSupportedOnVtCalls(mPhoneId, mContext));
  }
  
  public boolean isUtEnabled()
  {
    return mCT.isUtEnabled();
  }
  
  public boolean isVideoEnabled()
  {
    return mCT.isVideoCallEnabled();
  }
  
  public boolean isVolteEnabled()
  {
    return mCT.isVolteEnabled();
  }
  
  public boolean isWifiCallingEnabled()
  {
    return mCT.isVowifiEnabled();
  }
  
  public void notifyCallForwardingIndicator()
  {
    mDefaultPhone.notifyCallForwardingIndicator();
  }
  
  public void notifyForVideoCapabilityChanged(boolean paramBoolean)
  {
    mIsVideoCapable = paramBoolean;
    mDefaultPhone.notifyForVideoCapabilityChanged(paramBoolean);
  }
  
  public void notifyIncomingRing()
  {
    logd("notifyIncomingRing");
    sendMessage(obtainMessage(14, new AsyncResult(null, null, null)));
  }
  
  public void notifyNewRingingConnection(Connection paramConnection)
  {
    mDefaultPhone.notifyNewRingingConnectionP(paramConnection);
  }
  
  public void notifySrvccState(Call.SrvccState paramSrvccState)
  {
    mCT.notifySrvccState(paramSrvccState);
  }
  
  public void notifySuppSvcNotification(SuppServiceNotification paramSuppServiceNotification)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("notifySuppSvcNotification: suppSvc = ");
    localStringBuilder.append(paramSuppServiceNotification);
    logd(localStringBuilder.toString());
    paramSuppServiceNotification = new AsyncResult(null, paramSuppServiceNotification, null);
    mSsnRegistrants.notifyRegistrants(paramSuppServiceNotification);
  }
  
  void notifyUnknownConnection(Connection paramConnection)
  {
    mDefaultPhone.notifyUnknownConnectionP(paramConnection);
  }
  
  public void onFeatureCapabilityChanged()
  {
    mDefaultPhone.getServiceStateTracker().onImsCapabilityChanged();
  }
  
  void onIncomingUSSD(int paramInt, String paramString)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("onIncomingUSSD ussdMode=");
    ((StringBuilder)localObject1).append(paramInt);
    logd(((StringBuilder)localObject1).toString());
    int i = 0;
    boolean bool;
    if (paramInt == 1) {
      bool = true;
    } else {
      bool = false;
    }
    int j = i;
    if (paramInt != 0)
    {
      j = i;
      if (paramInt != 1) {
        j = 1;
      }
    }
    Object localObject2 = null;
    paramInt = 0;
    i = mPendingMMIs.size();
    for (;;)
    {
      localObject1 = localObject2;
      if (paramInt >= i) {
        break;
      }
      if (((ImsPhoneMmiCode)mPendingMMIs.get(paramInt)).isPendingUSSD())
      {
        localObject1 = (ImsPhoneMmiCode)mPendingMMIs.get(paramInt);
        break;
      }
      paramInt++;
    }
    if (localObject1 != null)
    {
      if (j != 0) {
        ((ImsPhoneMmiCode)localObject1).onUssdFinishedError();
      } else {
        ((ImsPhoneMmiCode)localObject1).onUssdFinished(paramString, bool);
      }
    }
    else if ((j == 0) && (paramString != null)) {
      onNetworkInitiatedUssd(ImsPhoneMmiCode.newNetworkInitiatedUssd(paramString, bool, this));
    }
  }
  
  public void onMMIDone(ImsPhoneMmiCode paramImsPhoneMmiCode)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("onMMIDone: mmi=");
    ((StringBuilder)localObject).append(paramImsPhoneMmiCode);
    logd(((StringBuilder)localObject).toString());
    if ((mPendingMMIs.remove(paramImsPhoneMmiCode)) || (paramImsPhoneMmiCode.isUssdRequest()) || (paramImsPhoneMmiCode.isSsInfo()))
    {
      localObject = paramImsPhoneMmiCode.getUssdCallbackReceiver();
      if (localObject != null)
      {
        int i;
        if (paramImsPhoneMmiCode.getState() == MmiCode.State.COMPLETE) {
          i = 100;
        } else {
          i = -1;
        }
        sendUssdResponse(paramImsPhoneMmiCode.getDialString(), paramImsPhoneMmiCode.getMessage(), i, (ResultReceiver)localObject);
      }
      else
      {
        logv("onMMIDone: notifyRegistrants");
        mMmiCompleteRegistrants.notifyRegistrants(new AsyncResult(null, paramImsPhoneMmiCode, null));
      }
    }
  }
  
  public void processDisconnectReason(ImsReasonInfo paramImsReasonInfo)
  {
    if ((mCode == 1000) && (mExtraMessage != null) && (ImsManager.getInstance(mContext, mPhoneId).isWfcEnabledByUser())) {
      processWfcDisconnectForNotification(paramImsReasonInfo);
    }
  }
  
  public void registerForSilentRedial(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSilentRedialRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSuppServiceNotification(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSsnRegistrants.addUnique(paramHandler, paramInt, paramObject);
    if (mSsnRegistrants.size() == 1) {
      mDefaultPhone.mCi.setSuppServiceNotifications(true, null);
    }
  }
  
  public void rejectCall()
    throws CallStateException
  {
    mCT.rejectCall();
  }
  
  public void sendDtmf(char paramChar)
  {
    if (!PhoneNumberUtils.is12Key(paramChar))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("sendDtmf called with invalid character '");
      localStringBuilder.append(paramChar);
      localStringBuilder.append("'");
      loge(localStringBuilder.toString());
    }
    else if (mCT.getState() == PhoneConstants.State.OFFHOOK)
    {
      mCT.sendDtmf(paramChar, null);
    }
  }
  
  public void sendEmergencyCallStateChange(boolean paramBoolean)
  {
    mDefaultPhone.sendEmergencyCallStateChange(paramBoolean);
  }
  
  @VisibleForTesting
  public void sendErrorResponse(Message paramMessage, Throwable paramThrowable)
  {
    logd("sendErrorResponse");
    if (paramMessage != null)
    {
      AsyncResult.forMessage(paramMessage, null, getCommandException(paramThrowable));
      paramMessage.sendToTarget();
    }
  }
  
  public void sendRttMessage(String paramString)
  {
    ImsCall localImsCall = getForegroundCall().getImsCall();
    if (localImsCall == null)
    {
      Rlog.d("ImsPhone", "RTT: imsCall null");
      return;
    }
    if ((localImsCall.isRttCall()) && (isFgCallActive()))
    {
      if (TextUtils.isEmpty(paramString))
      {
        Rlog.d("ImsPhone", "RTT: Text null");
        return;
      }
      if (!isRttVtCallAllowed(localImsCall))
      {
        Rlog.d("ImsPhone", "RTT: InCorrect mode");
        return;
      }
      Rlog.d("ImsPhone", "RTT: sendRttMessage");
      localImsCall.sendRttMessage(paramString);
      return;
    }
  }
  
  public void sendRttModifyRequest(ImsCallProfile paramImsCallProfile)
  {
    Rlog.d("ImsPhone", "RTT: sendRttModifyRequest");
    ImsCall localImsCall = getForegroundCall().getImsCall();
    if (localImsCall == null)
    {
      Rlog.d("ImsPhone", "RTT: imsCall null");
      return;
    }
    try
    {
      localImsCall.sendRttModifyRequest(paramImsCallProfile);
    }
    catch (ImsException localImsException)
    {
      paramImsCallProfile = new StringBuilder();
      paramImsCallProfile.append("RTT: sendRttModifyRequest exception = ");
      paramImsCallProfile.append(localImsException);
      Rlog.e("ImsPhone", paramImsCallProfile.toString());
    }
  }
  
  public void sendRttModifyResponse(int paramInt)
  {
    ImsCall localImsCall = getForegroundCall().getImsCall();
    if (localImsCall == null)
    {
      Rlog.d("ImsPhone", "RTT: imsCall null");
      return;
    }
    if (!isRttVtCallAllowed(localImsCall))
    {
      Rlog.d("ImsPhone", "RTT: Not allowed for VT");
      return;
    }
    Rlog.d("ImsPhone", "RTT: sendRttModifyResponse");
    localImsCall.sendRttModifyResponse(mapRequestToResponse(paramInt));
  }
  
  public void sendUSSD(String paramString, Message paramMessage)
  {
    mCT.sendUSSD(paramString, paramMessage);
  }
  
  public void sendUssdResponse(String paramString)
  {
    logd("sendUssdResponse");
    ImsPhoneMmiCode localImsPhoneMmiCode = ImsPhoneMmiCode.newFromUssdUserInput(paramString, this);
    mPendingMMIs.add(localImsPhoneMmiCode);
    mMmiRegistrants.notifyRegistrants(new AsyncResult(null, localImsPhoneMmiCode, null));
    localImsPhoneMmiCode.sendUssd(paramString);
  }
  
  public void setBroadcastEmergencyCallStateChanges(boolean paramBoolean)
  {
    mDefaultPhone.setBroadcastEmergencyCallStateChanges(paramBoolean);
  }
  
  public void setCallBarring(String paramString1, boolean paramBoolean, String paramString2, Message paramMessage)
  {
    setCallBarring(paramString1, paramBoolean, paramString2, paramMessage, 0);
  }
  
  public void setCallBarring(String paramString1, boolean paramBoolean, String paramString2, Message paramMessage, int paramInt)
  {
    paramString2 = new StringBuilder();
    paramString2.append("setCallBarring facility=");
    paramString2.append(paramString1);
    paramString2.append(", lockState=");
    paramString2.append(paramBoolean);
    paramString2.append(", serviceClass = ");
    paramString2.append(paramInt);
    logd(paramString2.toString());
    paramString2 = obtainMessage(46, paramMessage);
    if (paramBoolean) {}
    for (int i = 1;; i = 0) {
      break;
    }
    try
    {
      mCT.getUtInterface().updateCallBarring(getCBTypeFromFacility(paramString1), i, paramString2, null, paramInt);
    }
    catch (ImsException paramString1)
    {
      sendErrorResponse(paramMessage, paramString1);
    }
  }
  
  public void setCallForwardingOption(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, Message paramMessage)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setCallForwardingOption action=");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(", reason=");
    ((StringBuilder)localObject).append(paramInt2);
    ((StringBuilder)localObject).append(" serviceClass=");
    ((StringBuilder)localObject).append(paramInt3);
    logd(((StringBuilder)localObject).toString());
    if ((isValidCommandInterfaceCFAction(paramInt1)) && (isValidCommandInterfaceCFReason(paramInt2)))
    {
      localObject = new Cf(paramString, GsmMmiCode.isVoiceUnconditionalForwarding(paramInt2, paramInt3), paramMessage, paramInt3);
      localObject = obtainMessage(12, isCfEnable(paramInt1), 0, localObject);
      try
      {
        mCT.getUtInterface().updateCallForward(getActionFromCFAction(paramInt1), getConditionFromCFReason(paramInt2), paramString, paramInt3, paramInt4, (Message)localObject);
      }
      catch (ImsException paramString)
      {
        sendErrorResponse(paramMessage, paramString);
      }
    }
    else if (paramMessage != null)
    {
      sendErrorResponse(paramMessage);
    }
  }
  
  public void setCallForwardingOption(int paramInt1, int paramInt2, String paramString, int paramInt3, Message paramMessage)
  {
    setCallForwardingOption(paramInt1, paramInt2, paramString, 1, paramInt3, paramMessage);
  }
  
  public void setCallWaiting(boolean paramBoolean, int paramInt, Message paramMessage)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setCallWaiting enable=");
    ((StringBuilder)localObject).append(paramBoolean);
    logd(((StringBuilder)localObject).toString());
    localObject = obtainMessage(48, paramMessage);
    try
    {
      mCT.getUtInterface().updateCallWaiting(paramBoolean, paramInt, (Message)localObject);
    }
    catch (ImsException localImsException)
    {
      sendErrorResponse(paramMessage, localImsException);
    }
  }
  
  public void setCallWaiting(boolean paramBoolean, Message paramMessage)
  {
    setCallWaiting(paramBoolean, 1, paramMessage);
  }
  
  protected void setCurrentSubscriberUris(Uri[] paramArrayOfUri)
  {
    mCurrentSubscriberUris = paramArrayOfUri;
  }
  
  public void setImsRegistered(boolean paramBoolean)
  {
    mImsRegistered = paramBoolean;
  }
  
  public void setIsInEcm(boolean paramBoolean)
  {
    mDefaultPhone.setIsInEcm(paramBoolean);
  }
  
  public void setMute(boolean paramBoolean)
  {
    mCT.setMute(paramBoolean);
  }
  
  public void setOnEcbModeExitResponse(Handler paramHandler, int paramInt, Object paramObject)
  {
    mEcmExitRespRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public void setOutgoingCallerIdDisplay(int paramInt, Message paramMessage)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setCLIR action= ");
    ((StringBuilder)localObject).append(paramInt);
    logd(((StringBuilder)localObject).toString());
    localObject = obtainMessage(50, paramInt, 0, paramMessage);
    try
    {
      mCT.getUtInterface().updateCLIR(paramInt, (Message)localObject);
    }
    catch (ImsException localImsException)
    {
      sendErrorResponse(paramMessage, localImsException);
    }
  }
  
  @VisibleForTesting
  public void setServiceState(int paramInt)
  {
    try
    {
      int i;
      if (mSS.getVoiceRegState() != paramInt) {
        i = 1;
      } else {
        i = 0;
      }
      mSS.setVoiceRegState(paramInt);
      updateDataServiceState();
      if ((i != 0) && (mDefaultPhone.getServiceStateTracker() != null)) {
        mDefaultPhone.getServiceStateTracker().onImsServiceStateChanged();
      }
      return;
    }
    finally {}
  }
  
  public void setTTYMode(int paramInt, Message paramMessage)
  {
    mCT.setTtyMode(paramInt);
  }
  
  public void setUiTTYMode(int paramInt, Message paramMessage)
  {
    mCT.setUiTTYMode(paramInt, paramMessage);
  }
  
  public void setVoiceCallForwardingFlag(int paramInt, boolean paramBoolean, String paramString)
  {
    IccRecords localIccRecords = mDefaultPhone.getIccRecords();
    if (localIccRecords != null) {
      setVoiceCallForwardingFlag(localIccRecords, paramInt, paramBoolean, paramString);
    }
  }
  
  public void startDtmf(char paramChar)
  {
    if ((!PhoneNumberUtils.is12Key(paramChar)) && ((paramChar < 'A') || (paramChar > 'D')))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("startDtmf called with invalid character '");
      localStringBuilder.append(paramChar);
      localStringBuilder.append("'");
      loge(localStringBuilder.toString());
    }
    else
    {
      mCT.startDtmf(paramChar);
    }
  }
  
  public void stopDtmf()
  {
    mCT.stopDtmf();
  }
  
  public void switchHoldingAndActive()
    throws CallStateException
  {
    mCT.switchWaitingOrHoldingAndActive();
  }
  
  public void unregisterForSilentRedial(Handler paramHandler)
  {
    mSilentRedialRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSuppServiceNotification(Handler paramHandler)
  {
    mSsnRegistrants.remove(paramHandler);
    if (mSsnRegistrants.size() == 0) {
      mDefaultPhone.mCi.setSuppServiceNotifications(false, null);
    }
  }
  
  public void unsetOnEcbModeExitResponse(Handler paramHandler)
  {
    mEcmExitRespRegistrant.clear();
  }
  
  private static class Cf
  {
    final boolean mIsCfu;
    final Message mOnComplete;
    final int mServiceClass;
    final String mSetCfNumber;
    
    Cf(String paramString, boolean paramBoolean, Message paramMessage, int paramInt)
    {
      mSetCfNumber = paramString;
      mIsCfu = paramBoolean;
      mOnComplete = paramMessage;
      mServiceClass = paramInt;
    }
  }
  
  public static class ImsDialArgs
    extends PhoneInternalInterface.DialArgs
  {
    public final int clirMode;
    public final Connection.RttTextStream rttTextStream;
    
    private ImsDialArgs(Builder paramBuilder)
    {
      super();
      rttTextStream = mRttTextStream;
      clirMode = mClirMode;
    }
    
    public static class Builder
      extends PhoneInternalInterface.DialArgs.Builder<Builder>
    {
      private int mClirMode = 0;
      private Connection.RttTextStream mRttTextStream;
      
      public Builder() {}
      
      public static Builder from(PhoneInternalInterface.DialArgs paramDialArgs)
      {
        return (Builder)((Builder)((Builder)new Builder().setUusInfo(uusInfo)).setVideoState(videoState)).setIntentExtras(intentExtras);
      }
      
      public static Builder from(ImsPhone.ImsDialArgs paramImsDialArgs)
      {
        return ((Builder)((Builder)((Builder)new Builder().setUusInfo(uusInfo)).setVideoState(videoState)).setIntentExtras(intentExtras)).setRttTextStream(rttTextStream).setClirMode(clirMode);
      }
      
      public ImsPhone.ImsDialArgs build()
      {
        return new ImsPhone.ImsDialArgs(this, null);
      }
      
      public Builder setClirMode(int paramInt)
      {
        mClirMode = paramInt;
        return this;
      }
      
      public Builder setRttTextStream(Connection.RttTextStream paramRttTextStream)
      {
        mRttTextStream = paramRttTextStream;
        return this;
      }
    }
  }
}
