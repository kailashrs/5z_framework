package com.android.internal.telephony;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.SQLException;
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
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Telephony.Carriers;
import android.telecom.VideoProfile;
import android.telephony.CarrierConfigManager;
import android.telephony.CellLocation;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.NetworkScanRequest;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UssdResponse;
import android.telephony.cdma.CdmaCellLocation;
import android.text.TextUtils;
import android.util.Log;
import com.android.ims.ImsManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.cdma.CdmaMmiCode;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.cdma.EriManager;
import com.android.internal.telephony.dataconnection.DcTracker;
import com.android.internal.telephony.gsm.GsmMmiCode;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppType;
import com.android.internal.telephony.uicc.IccException;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccVmNotSupportedException;
import com.android.internal.telephony.uicc.IsimRecords;
import com.android.internal.telephony.uicc.IsimUiccRecords;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.uicc.UiccSlot;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GsmCdmaPhone
  extends Phone
{
  public static final int CANCEL_ECM_TIMER = 1;
  private static final boolean DBG = true;
  private static final int DEFAULT_ECM_EXIT_TIMER_VALUE = 300000;
  private static final int IMEI_14_DIGIT = 14;
  protected static final String INTENT_MODEM_RESTART = "com.asus.modem.crash";
  protected static final String INTENT_USSD_CONNECTION_TIMEOUT = "asus.intent.ussd.connection.timeout";
  private static final int INVALID_SYSTEM_SELECTION_CODE = -1;
  private static final String IS683A_FEATURE_CODE = "*228";
  private static final int IS683A_FEATURE_CODE_NUM_DIGITS = 4;
  private static final int IS683A_SYS_SEL_CODE_NUM_DIGITS = 2;
  private static final int IS683A_SYS_SEL_CODE_OFFSET = 4;
  private static final int IS683_CONST_1900MHZ_A_BLOCK = 2;
  private static final int IS683_CONST_1900MHZ_B_BLOCK = 3;
  private static final int IS683_CONST_1900MHZ_C_BLOCK = 4;
  private static final int IS683_CONST_1900MHZ_D_BLOCK = 5;
  private static final int IS683_CONST_1900MHZ_E_BLOCK = 6;
  private static final int IS683_CONST_1900MHZ_F_BLOCK = 7;
  private static final int IS683_CONST_800MHZ_A_BAND = 0;
  private static final int IS683_CONST_800MHZ_B_BAND = 1;
  public static final String LOG_TAG = "GsmCdmaPhone";
  public static final String PROPERTY_CDMA_HOME_OPERATOR_NUMERIC = "ro.cdma.home.operator.numeric";
  private static final int REPORTING_HYSTERESIS_DB = 2;
  private static final int REPORTING_HYSTERESIS_KBPS = 50;
  private static final int REPORTING_HYSTERESIS_MILLIS = 3000;
  public static final int RESTART_ECM_TIMER = 0;
  private static final int UPDATE_IMS_CONFIG_DELAY = 2000;
  private static final int UPDATE_IMS_CONFIG_MAX_RETRY = 5;
  private static final boolean VDBG = false;
  private static final String VM_NUMBER = "vm_number_key";
  private static final String VM_NUMBER_CDMA = "vm_number_key_cdma";
  private static final String VM_SIM_IMSI = "vm_sim_imsi_key";
  private static Pattern pOtaSpNumSchema = Pattern.compile("[,\\s]+");
  private boolean mBroadcastEmergencyCallStateChanges = false;
  private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = new StringBuilder();
      paramAnonymousContext.append("mBroadcastReceiver: action ");
      paramAnonymousContext.append(paramAnonymousIntent.getAction());
      Rlog.d("GsmCdmaPhone", paramAnonymousContext.toString());
      if ((paramAnonymousIntent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED")) && (paramAnonymousIntent.getExtras() != null) && (paramAnonymousIntent.getExtras().getInt("android.telephony.extra.SLOT_INDEX", -1) == mPhoneId)) {
        sendMessage(obtainMessage(43));
      }
    }
  };
  private CarrierKeyDownloadManager mCDM;
  private CarrierInfoManager mCIM;
  public GsmCdmaCallTracker mCT;
  private CarrierIdentifier mCarrerIdentifier;
  private String mCarrierOtaSpNumSchema;
  private CdmaSubscriptionSourceManager mCdmaSSM;
  public int mCdmaSubscriptionSource = -1;
  private Registrant mEcmExitRespRegistrant;
  private final RegistrantList mEcmTimerResetRegistrants = new RegistrantList();
  private final RegistrantList mEriFileLoadedRegistrants = new RegistrantList();
  public EriManager mEriManager;
  private String mEsn;
  private Runnable mExitEcmRunnable = new Runnable()
  {
    public void run()
    {
      exitEmergencyCallbackMode();
    }
  };
  private IccPhoneBookInterfaceManager mIccPhoneBookIntManager;
  private IccSmsInterfaceManager mIccSmsInterfaceManager;
  private String mImei;
  private String mImeiSv;
  private IsimUiccRecords mIsimUiccRecords;
  private String mMeid;
  private ArrayList<MmiCode> mPendingMMIs = new ArrayList();
  private int mPrecisePhoneType;
  private boolean mResetModemOnRadioTechnologyChange = false;
  private int mRilVersion;
  public ServiceStateTracker mSST;
  private SIMRecords mSimRecords;
  private RegistrantList mSsnRegistrants = new RegistrantList();
  private String mVmNumber;
  private PowerManager.WakeLock mWakeLock;
  private int updateImsConfigRetryCount = 0;
  
  public GsmCdmaPhone(Context paramContext, CommandsInterface paramCommandsInterface, PhoneNotifier paramPhoneNotifier, int paramInt1, int paramInt2, TelephonyComponentFactory paramTelephonyComponentFactory)
  {
    this(paramContext, paramCommandsInterface, paramPhoneNotifier, false, paramInt1, paramInt2, paramTelephonyComponentFactory);
  }
  
  public GsmCdmaPhone(Context paramContext, CommandsInterface paramCommandsInterface, PhoneNotifier paramPhoneNotifier, boolean paramBoolean, int paramInt1, int paramInt2, TelephonyComponentFactory paramTelephonyComponentFactory)
  {
    super(str, paramPhoneNotifier, paramContext, paramCommandsInterface, paramBoolean, paramInt1, paramTelephonyComponentFactory);
    mPrecisePhoneType = paramInt2;
    initOnce(paramCommandsInterface);
    initRatSpecific(paramInt2);
    mCarrierActionAgent = mTelephonyComponentFactory.makeCarrierActionAgent(this);
    mCarrierSignalAgent = mTelephonyComponentFactory.makeCarrierSignalAgent(this);
    mSST = mTelephonyComponentFactory.makeServiceStateTracker(this, mCi);
    mDcTracker = mTelephonyComponentFactory.makeDcTracker(this);
    mCarrerIdentifier = mTelephonyComponentFactory.makeCarrierIdentifier(this);
    mSST.registerForNetworkAttached(this, 19, null);
    mDeviceStateMonitor = mTelephonyComponentFactory.makeDeviceStateMonitor(this);
    paramContext = new StringBuilder();
    paramContext.append("GsmCdmaPhone: constructor: sub = ");
    paramContext.append(mPhoneId);
    logd(paramContext.toString());
  }
  
  private static boolean checkOtaSpNumBasedOnSysSelCode(int paramInt, String[] paramArrayOfString)
  {
    int i = 0;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3;
    try
    {
      int j = Integer.parseInt(paramArrayOfString[1]);
      for (;;)
      {
        bool3 = bool2;
        if (i >= j) {
          break;
        }
        if ((!TextUtils.isEmpty(paramArrayOfString[(i + 2)])) && (!TextUtils.isEmpty(paramArrayOfString[(i + 3)])))
        {
          int k = Integer.parseInt(paramArrayOfString[(i + 2)]);
          int m = Integer.parseInt(paramArrayOfString[(i + 3)]);
          if ((paramInt >= k) && (paramInt <= m))
          {
            bool3 = true;
            break;
          }
        }
        i++;
      }
    }
    catch (NumberFormatException paramArrayOfString)
    {
      Rlog.e("GsmCdmaPhone", "checkOtaSpNumBasedOnSysSelCode, error", paramArrayOfString);
      bool3 = bool1;
    }
    return bool3;
  }
  
  private static int extractSelCodeFromOtaSpNum(String paramString)
  {
    int i = paramString.length();
    int j = -1;
    int k = j;
    if (paramString.regionMatches(0, "*228", 0, 4))
    {
      k = j;
      if (i >= 6) {
        k = Integer.parseInt(paramString.substring(4, 6));
      }
    }
    paramString = new StringBuilder();
    paramString.append("extractSelCodeFromOtaSpNum ");
    paramString.append(k);
    Rlog.d("GsmCdmaPhone", paramString.toString());
    return k;
  }
  
  private UiccProfile getUiccProfile()
  {
    return UiccController.getInstance().getUiccProfileForPhone(mPhoneId);
  }
  
  private String getVmSimImsi()
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("vm_sim_imsi_key");
    localStringBuilder.append(getPhoneId());
    return localSharedPreferences.getString(localStringBuilder.toString(), null);
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
        Rlog.d("GsmCdmaPhone", "reject failed", paramString);
        notifySuppServiceFailed(PhoneInternalInterface.SuppService.REJECT);
      }
    }
    else if (getBackgroundCall().getState() != Call.State.IDLE)
    {
      logd("MmiCode 0: hangupWaitingOrBackground");
      mCT.hangupWaitingOrBackground();
    }
    return true;
  }
  
  private boolean handleCallHoldIncallSupplementaryService(String paramString)
  {
    int i = paramString.length();
    if (i > 2) {
      return false;
    }
    Object localObject = getForegroundCall();
    if (i > 1) {
      try
      {
        i = paramString.charAt(1) - '0';
        paramString = mCT.getConnectionByIndex((GsmCdmaCall)localObject, i);
        if ((paramString != null) && (i >= 1) && (i <= 19))
        {
          localObject = new java/lang/StringBuilder;
          ((StringBuilder)localObject).<init>();
          ((StringBuilder)localObject).append("MmiCode 2: separate call ");
          ((StringBuilder)localObject).append(i);
          logd(((StringBuilder)localObject).toString());
          mCT.separate(paramString);
        }
        else
        {
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("separate: invalid call index ");
          paramString.append(i);
          logd(paramString.toString());
          notifySuppServiceFailed(PhoneInternalInterface.SuppService.SEPARATE);
        }
      }
      catch (CallStateException paramString)
      {
        Rlog.d("GsmCdmaPhone", "separate failed", paramString);
        notifySuppServiceFailed(PhoneInternalInterface.SuppService.SEPARATE);
      }
    } else {
      try
      {
        if (getRingingCall().getState() != Call.State.IDLE)
        {
          logd("MmiCode 2: accept ringing call");
          mCT.acceptCall();
        }
        else
        {
          logd("MmiCode 2: switchWaitingOrHoldingAndActive");
          mCT.switchWaitingOrHoldingAndActive();
        }
      }
      catch (CallStateException paramString)
      {
        Rlog.d("GsmCdmaPhone", "switch failed", paramString);
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
    GsmCdmaCall localGsmCdmaCall = getForegroundCall();
    if (i > 1) {
      try
      {
        i = paramString.charAt(1) - '0';
        if ((i >= 1) && (i <= 19))
        {
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("MmiCode 1: hangupConnectionByIndex ");
          paramString.append(i);
          logd(paramString.toString());
          mCT.hangupConnectionByIndex(localGsmCdmaCall, i);
        }
      }
      catch (CallStateException paramString)
      {
        break label133;
      }
    }
    if (localGsmCdmaCall.getState() != Call.State.IDLE)
    {
      logd("MmiCode 1: hangup foreground");
      mCT.hangup(localGsmCdmaCall);
    }
    else
    {
      logd("MmiCode 1: switchWaitingOrHoldingAndActive");
      mCT.switchWaitingOrHoldingAndActive();
    }
    break label150;
    label133:
    Rlog.d("GsmCdmaPhone", "hangup failed", paramString);
    notifySuppServiceFailed(PhoneInternalInterface.SuppService.HANGUP);
    label150:
    return true;
  }
  
  private boolean handleCcbsIncallSupplementaryService(String paramString)
  {
    if (paramString.length() > 1) {
      return false;
    }
    Rlog.i("GsmCdmaPhone", "MmiCode 5: CCBS not supported!");
    notifySuppServiceFailed(PhoneInternalInterface.SuppService.UNKNOWN);
    return true;
  }
  
  private void handleCfuQueryResult(CallForwardInfo[] paramArrayOfCallForwardInfo)
  {
    if ((IccRecords)mIccRecords.get() != null)
    {
      boolean bool = false;
      int i;
      int j;
      if ((paramArrayOfCallForwardInfo != null) && (paramArrayOfCallForwardInfo.length != 0))
      {
        i = 0;
        j = paramArrayOfCallForwardInfo.length;
      }
      while (i < j) {
        if ((serviceClass & 0x1) != 0)
        {
          if (status == 1) {
            bool = true;
          }
          setVoiceCallForwardingFlag(1, bool, number);
        }
        else
        {
          i++;
          continue;
          setVoiceCallForwardingFlag(1, false, null);
        }
      }
    }
  }
  
  private boolean handleEctIncallSupplementaryService(String paramString)
  {
    if (paramString.length() != 1) {
      return false;
    }
    logd("MmiCode 4: explicit call transfer");
    explicitCallTransfer();
    return true;
  }
  
  private void handleEnterEmergencyCallbackMode(Message paramMessage)
  {
    paramMessage = new StringBuilder();
    paramMessage.append("handleEnterEmergencyCallbackMode, isInEcm()=");
    paramMessage.append(isInEcm());
    Rlog.d("GsmCdmaPhone", paramMessage.toString());
    if (!isInEcm())
    {
      setIsInEcm(true);
      sendEmergencyCallbackModeChange();
      long l = SystemProperties.getLong("ro.cdma.ecmexittimer", 300000L);
      postDelayed(mExitEcmRunnable, l);
      mWakeLock.acquire();
    }
  }
  
  private void handleExitEmergencyCallbackMode(Message paramMessage)
  {
    paramMessage = (AsyncResult)obj;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("handleExitEmergencyCallbackMode,ar.exception , isInEcm=");
    localStringBuilder.append(exception);
    localStringBuilder.append(isInEcm());
    Rlog.d("GsmCdmaPhone", localStringBuilder.toString());
    removeCallbacks(mExitEcmRunnable);
    if (mEcmExitRespRegistrant != null) {
      mEcmExitRespRegistrant.notifyRegistrant(paramMessage);
    }
    if (exception == null)
    {
      if (isInEcm()) {
        setIsInEcm(false);
      }
      if (mWakeLock.isHeld()) {
        mWakeLock.release();
      }
      sendEmergencyCallbackModeChange();
      mDcTracker.setInternalDataEnabled(true);
      notifyEmergencyCallRegistrants(false);
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
  
  private void handleRadioAvailable()
  {
    mCi.getBasebandVersion(obtainMessage(6));
    mCi.getDeviceIdentity(obtainMessage(21));
    mCi.getRadioCapability(obtainMessage(35));
    startLceAfterRadioIsAvailable();
  }
  
  private void handleRadioOffOrNotAvailable()
  {
    if (isPhoneTypeGsm()) {
      for (int i = mPendingMMIs.size() - 1; i >= 0; i--) {
        if (((GsmMmiCode)mPendingMMIs.get(i)).isPendingUSSD()) {
          ((GsmMmiCode)mPendingMMIs.get(i)).onUssdFinishedError();
        }
      }
    }
    mRadioOffOrNotAvailableRegistrants.notifyRegistrants();
  }
  
  private void handleRadioOn()
  {
    mCi.getVoiceRadioTechnology(obtainMessage(40));
    if (!isPhoneTypeGsm()) {
      mCdmaSubscriptionSource = mCdmaSSM.getCdmaSubscriptionSource();
    }
    logd("[ABSP][handleRadioOn] -> Skip setPreferredNetworkTypeIfSimLoaded...");
  }
  
  private void initOnce(CommandsInterface paramCommandsInterface)
  {
    if ((paramCommandsInterface instanceof SimulatedRadioControl)) {
      mSimulatedRadioControl = ((SimulatedRadioControl)paramCommandsInterface);
    }
    mCT = mTelephonyComponentFactory.makeGsmCdmaCallTracker(this);
    mIccPhoneBookIntManager = mTelephonyComponentFactory.makeIccPhoneBookInterfaceManager(this);
    mWakeLock = ((PowerManager)mContext.getSystemService("power")).newWakeLock(1, "GsmCdmaPhone");
    mIccSmsInterfaceManager = mTelephonyComponentFactory.makeIccSmsInterfaceManager(this);
    mCi.registerForAvailable(this, 1, null);
    mCi.registerForOffOrNotAvailable(this, 8, null);
    mCi.registerForOn(this, 5, null);
    mCi.setOnSuppServiceNotification(this, 2, null);
    mCi.setOnUSSD(this, 7, null);
    mCi.setOnSs(this, 36, null);
    mCdmaSSM = mTelephonyComponentFactory.getCdmaSubscriptionSourceManagerInstance(mContext, mCi, this, 27, null);
    mEriManager = mTelephonyComponentFactory.makeEriManager(this, mContext, 0);
    mCi.setEmergencyCallbackMode(this, 25, null);
    mCi.registerForExitEmergencyCallbackMode(this, 26, null);
    mCi.registerForModemReset(this, 45, null);
    mCarrierOtaSpNumSchema = TelephonyManager.from(mContext).getOtaSpNumberSchemaForPhone(getPhoneId(), "");
    mResetModemOnRadioTechnologyChange = SystemProperties.getBoolean("persist.radio.reset_on_switch", false);
    mCi.registerForRilConnected(this, 41, null);
    mCi.registerForVoiceRadioTechChanged(this, 39, null);
    mContext.registerReceiver(mBroadcastReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    mCDM = new CarrierKeyDownloadManager(this);
    mCIM = new CarrierInfoManager();
  }
  
  private void initRatSpecific(int paramInt)
  {
    mPendingMMIs.clear();
    mIccPhoneBookIntManager.updateIccRecords(null);
    mEsn = null;
    mMeid = null;
    mPrecisePhoneType = paramInt;
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Precise phone type ");
    ((StringBuilder)localObject1).append(mPrecisePhoneType);
    logd(((StringBuilder)localObject1).toString());
    Object localObject2 = TelephonyManager.from(mContext);
    localObject1 = getUiccProfile();
    if (isPhoneTypeGsm())
    {
      mCi.setPhoneType(1);
      ((TelephonyManager)localObject2).setPhoneType(getPhoneId(), 1);
      if (localObject1 != null) {
        ((UiccProfile)localObject1).setVoiceRadioTech(3);
      }
    }
    else
    {
      mCdmaSubscriptionSource = mCdmaSSM.getCdmaSubscriptionSource();
      mIsPhoneInEcmState = getInEcmMode();
      if (mIsPhoneInEcmState) {
        mCi.exitEmergencyCallbackMode(obtainMessage(26));
      }
      mCi.setPhoneType(2);
      ((TelephonyManager)localObject2).setPhoneType(getPhoneId(), 2);
      if (localObject1 != null) {
        ((UiccProfile)localObject1).setVoiceRadioTech(6);
      }
      Object localObject3 = SystemProperties.get("ro.cdma.home.operator.alpha");
      localObject1 = SystemProperties.get("ro.cdma.home.operator.numeric");
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("init: operatorAlpha='");
      localStringBuilder.append((String)localObject3);
      localStringBuilder.append("' operatorNumeric='");
      localStringBuilder.append((String)localObject1);
      localStringBuilder.append("'");
      logd(localStringBuilder.toString());
      if (!TextUtils.isEmpty((CharSequence)localObject3))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("init: set 'gsm.sim.operator.alpha' to operator='");
        localStringBuilder.append((String)localObject3);
        localStringBuilder.append("'");
        logd(localStringBuilder.toString());
        ((TelephonyManager)localObject2).setSimOperatorNameForPhone(mPhoneId, (String)localObject3);
      }
      if (!TextUtils.isEmpty((CharSequence)localObject1))
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("init: set 'gsm.sim.operator.numeric' to operator='");
        ((StringBuilder)localObject3).append((String)localObject1);
        ((StringBuilder)localObject3).append("'");
        logd(((StringBuilder)localObject3).toString());
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("update icc_operator_numeric=");
        ((StringBuilder)localObject3).append((String)localObject1);
        logd(((StringBuilder)localObject3).toString());
        ((TelephonyManager)localObject2).setSimOperatorNumericForPhone(mPhoneId, (String)localObject1);
        SubscriptionController.getInstance().setMccMnc((String)localObject1, getSubId());
        setIsoCountryProperty((String)localObject1);
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("update mccmnc=");
        ((StringBuilder)localObject2).append((String)localObject1);
        logd(((StringBuilder)localObject2).toString());
        MccTable.updateMccMncConfiguration(mContext, (String)localObject1, false);
      }
      updateCurrentCarrierInProvider((String)localObject1);
    }
  }
  
  private boolean invalidMeid(String paramString)
  {
    boolean bool;
    if ((!TextUtils.isEmpty(paramString)) && (!paramString.equals("0")) && (!paramString.startsWith("00000000"))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isCarrierOtaSpNum(String paramString)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    int i = extractSelCodeFromOtaSpNum(paramString);
    if (i == -1) {
      return false;
    }
    if (!TextUtils.isEmpty(mCarrierOtaSpNumSchema))
    {
      Matcher localMatcher = pOtaSpNumSchema.matcher(mCarrierOtaSpNumSchema);
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("isCarrierOtaSpNum,schema");
      ((StringBuilder)localObject).append(mCarrierOtaSpNumSchema);
      Rlog.d("GsmCdmaPhone", ((StringBuilder)localObject).toString());
      if (localMatcher.find())
      {
        localObject = pOtaSpNumSchema.split(mCarrierOtaSpNumSchema);
        if ((!TextUtils.isEmpty(localObject[0])) && (localObject[0].equals("SELC")))
        {
          if (i != -1)
          {
            bool4 = checkOtaSpNumBasedOnSysSelCode(i, (String[])localObject);
          }
          else
          {
            Rlog.d("GsmCdmaPhone", "isCarrierOtaSpNum,sysSelCodeInt is invalid");
            bool4 = bool1;
          }
        }
        else if ((!TextUtils.isEmpty(localObject[0])) && (localObject[0].equals("FC")))
        {
          i = Integer.parseInt(localObject[1]);
          if (paramString.regionMatches(0, localObject[2], 0, i)) {
            bool4 = true;
          } else {
            Rlog.d("GsmCdmaPhone", "isCarrierOtaSpNum,not otasp number");
          }
        }
        else
        {
          paramString = new StringBuilder();
          paramString.append("isCarrierOtaSpNum,ota schema not supported");
          paramString.append(localObject[0]);
          Rlog.d("GsmCdmaPhone", paramString.toString());
          bool4 = bool1;
        }
      }
      else
      {
        paramString = new StringBuilder();
        paramString.append("isCarrierOtaSpNum,ota schema pattern not right");
        paramString.append(mCarrierOtaSpNumSchema);
        Rlog.d("GsmCdmaPhone", paramString.toString());
        bool4 = bool2;
      }
    }
    else
    {
      Rlog.d("GsmCdmaPhone", "isCarrierOtaSpNum,ota schema pattern empty");
      bool4 = bool3;
    }
    return bool4;
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
  
  private boolean isImsUtEnabledOverCdma()
  {
    boolean bool;
    if ((isPhoneTypeCdmaLte()) && (mImsPhone != null) && (mImsPhone.isUtEnabled())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static boolean isIs683OtaSpDialStr(String paramString)
  {
    boolean bool = false;
    if (paramString.length() == 4)
    {
      if (paramString.equals("*228")) {
        bool = true;
      }
    }
    else {
      switch (extractSelCodeFromOtaSpNum(paramString))
      {
      default: 
        break;
      case 0: 
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
        bool = true;
      }
    }
    return bool;
  }
  
  private boolean isManualSelProhibitedInGlobalMode()
  {
    boolean bool1 = false;
    Object localObject = getContext().getResources().getString(17040867);
    boolean bool2 = bool1;
    if (!TextUtils.isEmpty((CharSequence)localObject))
    {
      localObject = ((String)localObject).split(";");
      bool2 = bool1;
      if (localObject != null) {
        if ((localObject.length != 1) || (!localObject[0].equalsIgnoreCase("true")))
        {
          bool2 = bool1;
          if (localObject.length == 2)
          {
            bool2 = bool1;
            if (!TextUtils.isEmpty(localObject[1]))
            {
              bool2 = bool1;
              if (localObject[0].equalsIgnoreCase("true"))
              {
                bool2 = bool1;
                if (!isMatchGid(localObject[1])) {}
              }
            }
          }
        }
        else
        {
          bool2 = true;
        }
      }
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("isManualNetSelAllowedInGlobal in current carrier is ");
    ((StringBuilder)localObject).append(bool2);
    logd(((StringBuilder)localObject).toString());
    return bool2;
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
    Rlog.d("GsmCdmaPhone", localStringBuilder.toString());
  }
  
  private void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.e("GsmCdmaPhone", localStringBuilder.toString());
  }
  
  private void logi(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.i("GsmCdmaPhone", localStringBuilder.toString());
  }
  
  private void onIncomingUSSD(int paramInt, String paramString)
  {
    if (!isPhoneTypeGsm()) {
      loge("onIncomingUSSD: not expected on GSM");
    }
    int i = 0;
    boolean bool;
    if (paramInt == 1) {
      bool = true;
    } else {
      bool = false;
    }
    int j;
    if ((paramInt != 0) && (paramInt != 1)) {
      j = 1;
    } else {
      j = 0;
    }
    if (paramInt == 2) {
      i = 1;
    }
    Object localObject1 = null;
    paramInt = 0;
    int k = mPendingMMIs.size();
    Object localObject2;
    for (;;)
    {
      localObject2 = localObject1;
      if (paramInt >= k) {
        break;
      }
      if (((GsmMmiCode)mPendingMMIs.get(paramInt)).isPendingUSSD())
      {
        localObject2 = (GsmMmiCode)mPendingMMIs.get(paramInt);
        break;
      }
      paramInt++;
    }
    if (localObject2 != null)
    {
      if (i != 0) {
        ((GsmMmiCode)localObject2).onUssdRelease();
      } else if (j != 0) {
        ((GsmMmiCode)localObject2).onUssdFinishedError();
      } else {
        ((GsmMmiCode)localObject2).onUssdFinished(paramString, bool);
      }
    }
    else if ((j == 0) && (paramString != null))
    {
      onNetworkInitiatedUssd(GsmMmiCode.newNetworkInitiatedUssd(paramString, bool, this, (UiccCardApplication)mUiccApplication.get()));
    }
    else if (j != 0)
    {
      paramString = new StringBuilder();
      paramString.append("USSD connection timeout, phoneId = ");
      paramString.append(getPhoneId());
      loge(paramString.toString());
      paramString = new Intent("asus.intent.ussd.connection.timeout");
      paramString.putExtra("phone", getPhoneId());
      mContext.sendBroadcast(paramString);
    }
  }
  
  private void onNetworkInitiatedUssd(MmiCode paramMmiCode)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onNetworkInitiatedUssd: mmi=");
    localStringBuilder.append(paramMmiCode);
    Rlog.v("GsmCdmaPhone", localStringBuilder.toString());
    mMmiCompleteRegistrants.notifyRegistrants(new AsyncResult(null, paramMmiCode, null));
  }
  
  private void processIccRecordEvents(int paramInt)
  {
    if (paramInt == 1)
    {
      logi("processIccRecordEvents: EVENT_CFI");
      notifyCallForwardingIndicator();
    }
  }
  
  private void registerForIccRecordEvents()
  {
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    if (localIccRecords == null) {
      return;
    }
    if (isPhoneTypeGsm())
    {
      localIccRecords.registerForNetworkSelectionModeAutomatic(this, 28, null);
      localIccRecords.registerForRecordsEvents(this, 29, null);
      localIccRecords.registerForRecordsLoaded(this, 3, null);
      localIccRecords.registerForImsiReady(this, 1045, null);
    }
    else
    {
      localIccRecords.registerForRecordsLoaded(this, 22, null);
      if (isPhoneTypeCdmaLte()) {
        localIccRecords.registerForRecordsLoaded(this, 3, null);
      }
    }
  }
  
  private void sendEmergencyCallbackModeChange()
  {
    Intent localIntent = new Intent("android.intent.action.EMERGENCY_CALLBACK_MODE_CHANGED");
    localIntent.putExtra("phoneinECMState", isInEcm());
    SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, getPhoneId());
    ActivityManager.broadcastStickyIntent(localIntent, -1);
    logd("sendEmergencyCallbackModeChange");
  }
  
  private void sendUssdResponse(String paramString, CharSequence paramCharSequence, int paramInt, ResultReceiver paramResultReceiver)
  {
    paramString = new UssdResponse(paramString, paramCharSequence);
    paramCharSequence = new Bundle();
    paramCharSequence.putParcelable("USSD_RESPONSE", paramString);
    paramResultReceiver.send(paramInt, paramCharSequence);
  }
  
  private void setIsoCountryProperty(String paramString)
  {
    TelephonyManager localTelephonyManager = TelephonyManager.from(mContext);
    if (TextUtils.isEmpty(paramString))
    {
      logd("setIsoCountryProperty: clear 'gsm.sim.operator.iso-country'");
      localTelephonyManager.setSimCountryIsoForPhone(mPhoneId, "");
    }
    else
    {
      Object localObject = "";
      try
      {
        paramString = MccTable.countryCodeForMcc(Integer.parseInt(paramString.substring(0, 3)));
      }
      catch (StringIndexOutOfBoundsException paramString)
      {
        Rlog.e("GsmCdmaPhone", "setIsoCountryProperty: countryCodeForMcc error", paramString);
        paramString = (String)localObject;
      }
      catch (NumberFormatException paramString)
      {
        for (;;)
        {
          Rlog.e("GsmCdmaPhone", "setIsoCountryProperty: countryCodeForMcc error", paramString);
          paramString = (String)localObject;
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("setIsoCountryProperty: set 'gsm.sim.operator.iso-country' to iso=");
      ((StringBuilder)localObject).append(paramString);
      logd(((StringBuilder)localObject).toString());
      localTelephonyManager.setSimCountryIsoForPhone(mPhoneId, paramString);
    }
  }
  
  private void setVmSimImsi(String paramString)
  {
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("vm_sim_imsi_key");
    localStringBuilder.append(getPhoneId());
    localEditor.putString(localStringBuilder.toString(), paramString);
    localEditor.apply();
  }
  
  private void storeVoiceMailNumber(String paramString)
  {
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
    StringBuilder localStringBuilder;
    if (isPhoneTypeGsm())
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("vm_number_key");
      localStringBuilder.append(getPhoneId());
      localEditor.putString(localStringBuilder.toString(), paramString);
      localEditor.apply();
      setVmSimImsi(getSubscriberId());
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("vm_number_key_cdma");
      localStringBuilder.append(getPhoneId());
      localEditor.putString(localStringBuilder.toString(), paramString);
      localEditor.apply();
    }
  }
  
  private void switchPhoneType(int paramInt)
  {
    removeCallbacks(mExitEcmRunnable);
    initRatSpecific(paramInt);
    mSST.updatePhoneType();
    if (paramInt == 1) {
      localObject = "GSM";
    } else {
      localObject = "CDMA";
    }
    setPhoneName((String)localObject);
    onUpdateIccAvailability();
    mCT.updatePhoneType();
    Object localObject = mCi.getRadioState();
    if (((CommandsInterface.RadioState)localObject).isAvailable())
    {
      handleRadioAvailable();
      if (((CommandsInterface.RadioState)localObject).isOn()) {
        handleRadioOn();
      }
    }
    if ((!((CommandsInterface.RadioState)localObject).isAvailable()) || (!((CommandsInterface.RadioState)localObject).isOn())) {
      handleRadioOffOrNotAvailable();
    }
  }
  
  private void switchVoiceRadioTech(int paramInt)
  {
    Object localObject = getPhoneName();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Switching Voice Phone : ");
    localStringBuilder.append((String)localObject);
    localStringBuilder.append(" >>> ");
    if (ServiceState.isGsm(paramInt)) {
      localObject = "GSM";
    } else {
      localObject = "CDMA";
    }
    localStringBuilder.append((String)localObject);
    logd(localStringBuilder.toString());
    if (ServiceState.isCdma(paramInt))
    {
      localObject = mUiccController.getUiccCardApplication(mPhoneId, 2);
      if ((localObject != null) && (((UiccCardApplication)localObject).getType() == IccCardApplicationStatus.AppType.APPTYPE_RUIM)) {
        switchPhoneType(2);
      } else {
        switchPhoneType(6);
      }
    }
    else
    {
      if (!ServiceState.isGsm(paramInt)) {
        break label129;
      }
      switchPhoneType(1);
    }
    return;
    label129:
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("deleteAndCreatePhone: newVoiceRadioTech=");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(" is not CDMA or GSM (error) - aborting!");
    loge(((StringBuilder)localObject).toString());
  }
  
  private void syncClirSetting()
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("clir_key");
    localStringBuilder.append(getPhoneId());
    int i = localSharedPreferences.getInt(localStringBuilder.toString(), -1);
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("syncClirSetting: clir_key");
    localStringBuilder.append(getPhoneId());
    localStringBuilder.append("=");
    localStringBuilder.append(i);
    Rlog.i("GsmCdmaPhone", localStringBuilder.toString());
    if (i >= 0) {
      mCi.setCLIR(i, null);
    }
  }
  
  private void unregisterForIccRecordEvents()
  {
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    if (localIccRecords == null) {
      return;
    }
    localIccRecords.unregisterForNetworkSelectionModeAutomatic(this);
    localIccRecords.unregisterForRecordsEvents(this);
    localIccRecords.unregisterForRecordsLoaded(this);
    localIccRecords.unregisterForImsiReady(this);
  }
  
  private boolean updateCurrentCarrierInProvider(String paramString)
  {
    if ((!isPhoneTypeCdma()) && ((!isPhoneTypeCdmaLte()) || (mUiccController.getUiccCardApplication(mPhoneId, 1) != null)))
    {
      logd("updateCurrentCarrierInProvider not updated X retVal=true");
      return true;
    }
    logd("CDMAPhone: updateCurrentCarrierInProvider called");
    if (!TextUtils.isEmpty(paramString)) {
      try
      {
        Uri localUri = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
        Object localObject = new android/content/ContentValues;
        ((ContentValues)localObject).<init>();
        ((ContentValues)localObject).put("numeric", paramString);
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("updateCurrentCarrierInProvider from system: numeric=");
        localStringBuilder.append(paramString);
        logd(localStringBuilder.toString());
        getContext().getContentResolver().insert(localUri, (ContentValues)localObject);
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("update mccmnc=");
        ((StringBuilder)localObject).append(paramString);
        logd(((StringBuilder)localObject).toString());
        MccTable.updateMccMncConfiguration(mContext, paramString, false);
        return true;
      }
      catch (SQLException paramString)
      {
        Rlog.e("GsmCdmaPhone", "Can't store current operator", paramString);
      }
    }
    return false;
  }
  
  public void acceptCall(int paramInt)
    throws CallStateException
  {
    Phone localPhone = mImsPhone;
    if ((localPhone != null) && (localPhone.getRingingCall().isRinging())) {
      localPhone.acceptCall(paramInt);
    } else {
      mCT.acceptCall();
    }
  }
  
  public void activateCellBroadcastSms(int paramInt, Message paramMessage)
  {
    loge("[GsmCdmaPhone] activateCellBroadcastSms() is obsolete; use SmsManager");
    paramMessage.sendToTarget();
  }
  
  public void addParticipant(String paramString)
    throws CallStateException
  {
    Object localObject = mImsPhone;
    int i;
    if ((isImsUseEnabled()) && (localObject != null) && ((((Phone)localObject).isVolteEnabled()) || (((Phone)localObject).isWifiCallingEnabled()) || (((Phone)localObject).isVideoEnabled())) && (((Phone)localObject).getServiceState().getState() == 0)) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      try
      {
        logd("addParticipant :: Trying to add participant in IMS call");
        ((Phone)localObject).addParticipant(paramString);
      }
      catch (CallStateException paramString)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("addParticipant :: IMS PS call exception ");
        ((StringBuilder)localObject).append(paramString);
        loge(((StringBuilder)localObject).toString());
      }
    } else {
      loge("addParticipant :: IMS is disabled so unable to add participant with IMS call");
    }
  }
  
  public boolean canConference()
  {
    if ((mImsPhone != null) && (mImsPhone.canConference())) {
      return true;
    }
    if (isPhoneTypeGsm()) {
      return mCT.canConference();
    }
    loge("canConference: not possible in CDMA");
    return false;
  }
  
  public boolean canTransfer()
  {
    if (isPhoneTypeGsm()) {
      return mCT.canTransfer();
    }
    loge("canTransfer: not possible in CDMA");
    return false;
  }
  
  public void changeCallBarringPassword(String paramString1, String paramString2, String paramString3, Message paramMessage)
  {
    if (isPhoneTypeGsm()) {
      mCi.changeBarringPassword(paramString1, paramString2, paramString3, paramMessage);
    } else {
      loge("changeCallBarringPassword: not possible in CDMA");
    }
  }
  
  public void clearDisconnected()
  {
    mCT.clearDisconnected();
  }
  
  public void conference()
  {
    if ((mImsPhone != null) && (mImsPhone.canConference()))
    {
      logd("conference() - delegated to IMS phone");
      try
      {
        mImsPhone.conference();
      }
      catch (CallStateException localCallStateException)
      {
        loge(localCallStateException.toString());
      }
      return;
    }
    if (isPhoneTypeGsm()) {
      mCT.conference();
    } else {
      loge("conference: not possible in CDMA");
    }
  }
  
  public Connection dial(String paramString, PhoneInternalInterface.DialArgs paramDialArgs)
    throws CallStateException
  {
    if ((!isPhoneTypeGsm()) && (uusInfo != null)) {
      throw new CallStateException("Sending UUS information NOT supported in CDMA!");
    }
    boolean bool1 = isEmergencyNumber(paramString);
    Object localObject1 = mImsPhone;
    boolean bool2 = ((CarrierConfigManager)mContext.getSystemService("carrier_config")).getConfigForSubId(getSubId()).getBoolean("carrier_use_ims_first_for_emergency_bool");
    boolean bool3 = isImsUseEnabled();
    boolean bool4 = false;
    if ((bool3) && (localObject1 != null) && ((((Phone)localObject1).isVolteEnabled()) || (((Phone)localObject1).isWifiCallingEnabled()) || ((((Phone)localObject1).isVideoEnabled()) && (VideoProfile.isVideo(videoState)))) && (((Phone)localObject1).getServiceState().getState() == 0) && (!shallDialOnCircuitSwitch(intentExtras))) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    if ((localObject1 != null) && (bool1) && (bool2) && (ImsManager.getInstance(mContext, mPhoneId).isNonTtyOrTtyOnVolteEnabled()) && (((Phone)localObject1).isImsAvailable())) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    Object localObject2 = PhoneNumberUtils.extractNetworkPortionAlt(PhoneNumberUtils.stripSeparators(paramString));
    boolean bool5;
    if (((((String)localObject2).startsWith("*")) || (((String)localObject2).startsWith("#"))) && (((String)localObject2).endsWith("#"))) {
      bool5 = true;
    } else {
      bool5 = false;
    }
    boolean bool6 = bool4;
    if (localObject1 != null)
    {
      bool6 = bool4;
      if (((Phone)localObject1).isUtEnabled()) {
        bool6 = true;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("useImsForCall=");
    localStringBuilder.append(bool3);
    localStringBuilder.append(", useImsForEmergency=");
    localStringBuilder.append(bool2);
    localStringBuilder.append(", useImsForUt=");
    localStringBuilder.append(bool6);
    localStringBuilder.append(", isUt=");
    localStringBuilder.append(bool5);
    localStringBuilder.append(", imsPhone=");
    localStringBuilder.append(localObject1);
    localStringBuilder.append(", imsPhone.isVolteEnabled()=");
    if (localObject1 != null) {
      localObject2 = Boolean.valueOf(((Phone)localObject1).isVolteEnabled());
    } else {
      localObject2 = "N/A";
    }
    localStringBuilder.append(localObject2);
    localStringBuilder.append(", imsPhone.isVowifiEnabled()=");
    if (localObject1 != null) {
      localObject2 = Boolean.valueOf(((Phone)localObject1).isWifiCallingEnabled());
    } else {
      localObject2 = "N/A";
    }
    localStringBuilder.append(localObject2);
    localStringBuilder.append(", imsPhone.isVideoEnabled()=");
    if (localObject1 != null) {
      localObject2 = Boolean.valueOf(((Phone)localObject1).isVideoEnabled());
    } else {
      localObject2 = "N/A";
    }
    localStringBuilder.append(localObject2);
    localStringBuilder.append(", imsPhone.getServiceState().getState()=");
    if (localObject1 != null) {
      localObject2 = Integer.valueOf(((Phone)localObject1).getServiceState().getState());
    } else {
      localObject2 = "N/A";
    }
    localStringBuilder.append(localObject2);
    logd(localStringBuilder.toString());
    Phone.checkWfcWifiOnlyModeBeforeDial(mImsPhone, mPhoneId, mContext);
    if (((bool3) && (!bool5)) || ((bool5) && (bool6) && (localObject1 != null) && (((Phone)localObject1).getServiceState().getState() == 0)) || (bool2)) {
      try
      {
        logd("Trying IMS PS call");
        localObject2 = ((Phone)localObject1).dial(paramString, paramDialArgs);
        return localObject2;
      }
      catch (CallStateException localCallStateException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("IMS PS call exception ");
        localStringBuilder.append(localCallStateException);
        localStringBuilder.append("useImsForCall =");
        localStringBuilder.append(bool3);
        localStringBuilder.append(", imsPhone =");
        localStringBuilder.append(localObject1);
        logd(localStringBuilder.toString());
        if ((!"cs_fallback".equals(localCallStateException.getMessage())) && (!bool1))
        {
          paramString = new CallStateException(localCallStateException.getMessage());
          paramString.setStackTrace(localCallStateException.getStackTrace());
          throw paramString;
        }
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("IMS call failed with Exception: ");
        ((StringBuilder)localObject1).append(localCallStateException.getMessage());
        ((StringBuilder)localObject1).append(". Falling back to CS.");
        logi(((StringBuilder)localObject1).toString());
      }
    }
    if ((mSST != null) && (mSST.mSS.getState() == 1) && (mSST.mSS.getDataRegState() != 0) && (!bool1)) {
      throw new CallStateException("cannot dial in current state");
    }
    if ((mSST != null) && (mSST.mSS.getState() == 3) && (!VideoProfile.isVideo(videoState)) && (!bool1) && ((!bool5) || (!bool6))) {
      throw new CallStateException(2, "cannot dial voice call in airplane mode");
    }
    if ((mSST != null) && (mSST.mSS.getState() == 1) && ((mSST.mSS.getDataRegState() != 0) || (!ServiceState.isLte(mSST.mSS.getRilDataRadioTechnology()))) && (!VideoProfile.isVideo(videoState)) && (!bool1)) {
      throw new CallStateException(1, "cannot dial voice call in out of service");
    }
    logd("Trying (non-IMS) CS call");
    if (isPhoneTypeGsm()) {
      return dialInternal(paramString, new PhoneInternalInterface.DialArgs.Builder().setIntentExtras(intentExtras).build());
    }
    return dialInternal(paramString, paramDialArgs);
  }
  
  protected Connection dialInternal(String paramString, PhoneInternalInterface.DialArgs paramDialArgs)
    throws CallStateException
  {
    return dialInternal(paramString, paramDialArgs, null);
  }
  
  protected Connection dialInternal(String paramString, PhoneInternalInterface.DialArgs paramDialArgs, ResultReceiver paramResultReceiver)
    throws CallStateException
  {
    paramString = PhoneNumberUtils.stripSeparators(paramString);
    if (isPhoneTypeGsm())
    {
      if (handleInCallMmiCommands(paramString)) {
        return null;
      }
      GsmMmiCode localGsmMmiCode = GsmMmiCode.newFromDialString(PhoneNumberUtils.extractNetworkPortionAlt(paramString), this, (UiccCardApplication)mUiccApplication.get(), paramResultReceiver);
      paramResultReceiver = new StringBuilder();
      paramResultReceiver.append("dialInternal: dialing w/ mmi '");
      paramResultReceiver.append(localGsmMmiCode);
      paramResultReceiver.append("'...");
      logd(paramResultReceiver.toString());
      if (localGsmMmiCode == null) {
        return mCT.dial(paramString, uusInfo, intentExtras);
      }
      if (localGsmMmiCode.isTemporaryModeCLIR()) {
        return mCT.dial(mDialingNumber, localGsmMmiCode.getCLIRMode(), uusInfo, intentExtras);
      }
      mPendingMMIs.add(localGsmMmiCode);
      mMmiRegistrants.notifyRegistrants(new AsyncResult(null, localGsmMmiCode, null));
      localGsmMmiCode.processCode();
      return null;
    }
    return mCT.dial(paramString);
  }
  
  public void disableLocationUpdates()
  {
    mSST.disableLocationUpdates();
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("GsmCdmaPhone extends:");
    super.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPrecisePhoneType=");
    paramFileDescriptor.append(mPrecisePhoneType);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mSimRecords=");
    paramFileDescriptor.append(mSimRecords);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mIsimUiccRecords=");
    paramFileDescriptor.append(mIsimUiccRecords);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCT=");
    paramFileDescriptor.append(mCT);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mSST=");
    paramFileDescriptor.append(mSST);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mPendingMMIs=");
    paramFileDescriptor.append(mPendingMMIs);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mIccPhoneBookIntManager=");
    paramFileDescriptor.append(mIccPhoneBookIntManager);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCdmaSSM=");
    paramFileDescriptor.append(mCdmaSSM);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCdmaSubscriptionSource=");
    paramFileDescriptor.append(mCdmaSubscriptionSource);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mEriManager=");
    paramFileDescriptor.append(mEriManager);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mWakeLock=");
    paramFileDescriptor.append(mWakeLock);
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" isInEcm()=");
    paramFileDescriptor.append(isInEcm());
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" mCarrierOtaSpNumSchema=");
    paramFileDescriptor.append(mCarrierOtaSpNumSchema);
    paramPrintWriter.println(paramFileDescriptor.toString());
    if (!isPhoneTypeGsm())
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" getCdmaEriIconIndex()=");
      paramFileDescriptor.append(getCdmaEriIconIndex());
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" getCdmaEriIconMode()=");
      paramFileDescriptor.append(getCdmaEriIconMode());
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" getCdmaEriText()=");
      paramFileDescriptor.append(getCdmaEriText());
      paramPrintWriter.println(paramFileDescriptor.toString());
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append(" isMinInfoReady()=");
      paramFileDescriptor.append(isMinInfoReady());
      paramPrintWriter.println(paramFileDescriptor.toString());
    }
    paramFileDescriptor = new StringBuilder();
    paramFileDescriptor.append(" isCspPlmnEnabled()=");
    paramFileDescriptor.append(isCspPlmnEnabled());
    paramPrintWriter.println(paramFileDescriptor.toString());
    paramPrintWriter.flush();
  }
  
  public void enableEnhancedVoicePrivacy(boolean paramBoolean, Message paramMessage)
  {
    if (isPhoneTypeGsm()) {
      loge("enableEnhancedVoicePrivacy: not expected on GSM");
    } else {
      mCi.setPreferredVoicePrivacy(paramBoolean, paramMessage);
    }
  }
  
  public void enableLocationUpdates()
  {
    mSST.enableLocationUpdates();
  }
  
  public void exitEmergencyCallbackMode()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("exitEmergencyCallbackMode: mImsPhone=");
    localStringBuilder.append(mImsPhone);
    localStringBuilder.append(" isPhoneTypeGsm=");
    localStringBuilder.append(isPhoneTypeGsm());
    Rlog.d("GsmCdmaPhone", localStringBuilder.toString());
    if (isPhoneTypeGsm())
    {
      if (mImsPhone != null) {
        mImsPhone.exitEmergencyCallbackMode();
      }
    }
    else
    {
      if (mWakeLock.isHeld()) {
        mWakeLock.release();
      }
      mCi.exitEmergencyCallbackMode(obtainMessage(26));
    }
  }
  
  public void explicitCallTransfer()
  {
    if (isPhoneTypeGsm()) {
      mCT.explicitCallTransfer();
    } else {
      loge("explicitCallTransfer: not possible in CDMA");
    }
  }
  
  protected void finalize()
  {
    logd("GsmCdmaPhone finalized");
    if ((mWakeLock != null) && (mWakeLock.isHeld()))
    {
      Rlog.e("GsmCdmaPhone", "UNEXPECTED; mWakeLock is held when finalizing.");
      mWakeLock.release();
    }
  }
  
  public void getAvailableNetworks(Message paramMessage)
  {
    if ((!isPhoneTypeGsm()) && (!isPhoneTypeCdmaLte())) {
      loge("getAvailableNetworks: not possible in CDMA");
    } else {
      mCi.getAvailableNetworks(paramMessage);
    }
  }
  
  public GsmCdmaCall getBackgroundCall()
  {
    return mCT.mBackgroundCall;
  }
  
  public void getCallBarring(String paramString1, String paramString2, Message paramMessage, int paramInt)
  {
    if (isPhoneTypeGsm())
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && (localPhone.isUtEnabled()))
      {
        localPhone.getCallBarring(paramString1, paramString2, paramMessage, paramInt);
        return;
      }
      mCi.queryFacilityLock(paramString1, paramString2, paramInt, paramMessage);
    }
    else
    {
      loge("getCallBarringOption: not possible in CDMA");
    }
  }
  
  public void getCallForwardingOption(int paramInt1, int paramInt2, Message paramMessage)
  {
    if ((!isPhoneTypeGsm()) && (!isImsUtEnabledOverCdma()))
    {
      loge("getCallForwardingOption: not possible in CDMA without IMS");
    }
    else
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && ((localPhone.getServiceState().getState() == 0) || (localPhone.isUtEnabled())))
      {
        localPhone.getCallForwardingOption(paramInt1, paramInt2, paramMessage);
        return;
      }
      if (isValidCommandInterfaceCFReason(paramInt1))
      {
        logd("requesting call forwarding query.");
        if (paramInt1 == 0) {
          paramMessage = obtainMessage(13, paramMessage);
        }
        mCi.queryCallForwardStatus(paramInt1, paramInt2, null, paramMessage);
      }
    }
  }
  
  public void getCallForwardingOption(int paramInt, Message paramMessage)
  {
    getCallForwardingOption(paramInt, 0, paramMessage);
  }
  
  public CallTracker getCallTracker()
  {
    return mCT;
  }
  
  public void getCallWaiting(Message paramMessage)
  {
    if ((!isPhoneTypeGsm()) && (!isImsUtEnabledOverCdma()))
    {
      mCi.queryCallWaiting(1, paramMessage);
    }
    else
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && ((localPhone.getServiceState().getState() == 0) || (localPhone.isUtEnabled())))
      {
        localPhone.getCallWaiting(paramMessage);
        return;
      }
      mCi.queryCallWaiting(0, paramMessage);
    }
  }
  
  public int getCarrierId()
  {
    return mCarrerIdentifier.getCarrierId();
  }
  
  public int getCarrierIdListVersion()
  {
    return mCarrerIdentifier.getCarrierListVersion();
  }
  
  public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int paramInt)
  {
    return CarrierInfoManager.getCarrierInfoForImsiEncryption(paramInt, mContext);
  }
  
  public String getCarrierName()
  {
    return mCarrerIdentifier.getCarrierName();
  }
  
  public int getCdmaEriIconIndex()
  {
    if (isPhoneTypeGsm()) {
      return super.getCdmaEriIconIndex();
    }
    return getServiceState().getCdmaEriIconIndex();
  }
  
  public int getCdmaEriIconMode()
  {
    if (isPhoneTypeGsm()) {
      return super.getCdmaEriIconMode();
    }
    return getServiceState().getCdmaEriIconMode();
  }
  
  public String getCdmaEriText()
  {
    if (isPhoneTypeGsm()) {
      return super.getCdmaEriText();
    }
    int i = getServiceState().getCdmaRoamingIndicator();
    int j = getServiceState().getCdmaDefaultRoamingIndicator();
    return mEriManager.getCdmaEriText(i, j);
  }
  
  public String getCdmaMin()
  {
    return mSST.getCdmaMin();
  }
  
  public String getCdmaPrlVersion()
  {
    return mSST.getPrlVersion();
  }
  
  public void getCellBroadcastSmsConfig(Message paramMessage)
  {
    loge("[GsmCdmaPhone] getCellBroadcastSmsConfig() is obsolete; use SmsManager");
    paramMessage.sendToTarget();
  }
  
  public CellLocation getCellLocation(WorkSource paramWorkSource)
  {
    if (isPhoneTypeGsm()) {
      return mSST.getCellLocation(paramWorkSource);
    }
    CdmaCellLocation localCdmaCellLocation = (CdmaCellLocation)mSST.mCellLoc;
    paramWorkSource = localCdmaCellLocation;
    if (Settings.Secure.getInt(getContext().getContentResolver(), "location_mode", 0) == 0)
    {
      paramWorkSource = new CdmaCellLocation();
      paramWorkSource.setCellLocationData(localCdmaCellLocation.getBaseStationId(), Integer.MAX_VALUE, Integer.MAX_VALUE, localCdmaCellLocation.getSystemId(), localCdmaCellLocation.getNetworkId());
    }
    return paramWorkSource;
  }
  
  public String getCountryIso()
  {
    int i = getSubId();
    SubscriptionInfo localSubscriptionInfo = SubscriptionManager.from(getContext()).getActiveSubscriptionInfo(i);
    if (localSubscriptionInfo == null) {
      return null;
    }
    return localSubscriptionInfo.getCountryIso().toUpperCase();
  }
  
  public PhoneInternalInterface.DataActivityState getDataActivityState()
  {
    PhoneInternalInterface.DataActivityState localDataActivityState = PhoneInternalInterface.DataActivityState.NONE;
    if (mSST.getCurrentDataConnectionState() == 0) {
      switch (3.$SwitchMap$com$android$internal$telephony$DctConstants$Activity[mDcTracker.getActivity().ordinal()])
      {
      default: 
        localDataActivityState = PhoneInternalInterface.DataActivityState.NONE;
        break;
      case 4: 
        localDataActivityState = PhoneInternalInterface.DataActivityState.DORMANT;
        break;
      case 3: 
        localDataActivityState = PhoneInternalInterface.DataActivityState.DATAINANDOUT;
        break;
      case 2: 
        localDataActivityState = PhoneInternalInterface.DataActivityState.DATAOUT;
        break;
      case 1: 
        localDataActivityState = PhoneInternalInterface.DataActivityState.DATAIN;
      }
    }
    return localDataActivityState;
  }
  
  public PhoneConstants.DataState getDataConnectionState(String paramString)
  {
    PhoneConstants.DataState localDataState = PhoneConstants.DataState.DISCONNECTED;
    if (mSST == null) {
      localDataState = PhoneConstants.DataState.DISCONNECTED;
    } else if ((mSST.getCurrentDataConnectionState() != 0) && ((isPhoneTypeCdma()) || (isPhoneTypeCdmaLte()) || ((isPhoneTypeGsm()) && (!paramString.equals("emergency"))))) {
      localDataState = PhoneConstants.DataState.DISCONNECTED;
    } else {
      switch (3.$SwitchMap$com$android$internal$telephony$DctConstants$State[mDcTracker.getState(paramString).ordinal()])
      {
      default: 
        localDataState = PhoneConstants.DataState.DISCONNECTED;
        break;
      case 3: 
        localDataState = PhoneConstants.DataState.CONNECTING;
        break;
      case 1: 
      case 2: 
        if (((mCT.mState != PhoneConstants.State.IDLE) && (!mSST.isConcurrentVoiceAndDataAllowed())) || (mDcTracker.getOtherSimOnVoiceCall())) {
          localDataState = PhoneConstants.DataState.SUSPENDED;
        } else {
          localDataState = PhoneConstants.DataState.CONNECTED;
        }
        break;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getDataConnectionState apnType=");
    localStringBuilder.append(paramString);
    localStringBuilder.append(" ret=");
    localStringBuilder.append(localDataState);
    logd(localStringBuilder.toString());
    return localDataState;
  }
  
  public boolean getDataRoamingEnabled()
  {
    return mDcTracker.getDataRoamingEnabled();
  }
  
  public String getDeviceId()
  {
    boolean bool = ((CarrierConfigManager)mContext.getSystemService("carrier_config")).getConfigForSubId(getSubId()).getBoolean("force_imei_bool");
    if ((!isPhoneTypeGsm()) && (!bool))
    {
      Object localObject1 = getMeid();
      if (localObject1 != null)
      {
        localObject2 = localObject1;
        if (!((String)localObject1).matches("^0*$")) {}
      }
      else
      {
        loge("getDeviceId(): MEID is not initialized use ESN");
        localObject2 = getEsn();
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("getDeviceId() phoneId: ");
      ((StringBuilder)localObject1).append(mPhoneId);
      ((StringBuilder)localObject1).append(", is Gsm: ");
      ((StringBuilder)localObject1).append(isPhoneTypeGsm());
      ((StringBuilder)localObject1).append(", MEID: ");
      ((StringBuilder)localObject1).append((String)localObject2);
      logd(((StringBuilder)localObject1).toString());
      return localObject2;
    }
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("getDeviceId() phoneId: ");
    ((StringBuilder)localObject2).append(mPhoneId);
    ((StringBuilder)localObject2).append(", is Gsm: ");
    ((StringBuilder)localObject2).append(isPhoneTypeGsm());
    ((StringBuilder)localObject2).append(", IMEI: ");
    ((StringBuilder)localObject2).append(getImei());
    logd(((StringBuilder)localObject2).toString());
    return getImei();
  }
  
  public String getDeviceSvn()
  {
    if ((!isPhoneTypeGsm()) && (!isPhoneTypeCdmaLte()))
    {
      loge("getDeviceSvn(): return 0");
      return "0";
    }
    return mImeiSv;
  }
  
  public String getDtmfToneDelayKey()
  {
    String str;
    if (isPhoneTypeGsm()) {
      str = "gsm_dtmf_tone_delay_int";
    } else {
      str = "cdma_dtmf_tone_delay_int";
    }
    return str;
  }
  
  public void getEnhancedVoicePrivacy(Message paramMessage)
  {
    if (isPhoneTypeGsm()) {
      loge("getEnhancedVoicePrivacy: not expected on GSM");
    } else {
      mCi.getPreferredVoicePrivacy(paramMessage);
    }
  }
  
  public String getEsn()
  {
    if (isPhoneTypeGsm())
    {
      loge("[GsmCdmaPhone] getEsn() is a CDMA method");
      return "0";
    }
    return mEsn;
  }
  
  public GsmCdmaCall getForegroundCall()
  {
    return mCT.mForegroundCall;
  }
  
  public String getFullIccSerialNumber()
  {
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    Object localObject = localIccRecords;
    if (!isPhoneTypeGsm())
    {
      localObject = localIccRecords;
      if (localIccRecords == null) {
        localObject = mUiccController.getIccRecords(mPhoneId, 1);
      }
    }
    if (localObject != null) {
      localObject = ((IccRecords)localObject).getFullIccId();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public String getGroupIdLevel1()
  {
    boolean bool = isPhoneTypeGsm();
    String str = null;
    if (bool)
    {
      IccRecords localIccRecords = (IccRecords)mIccRecords.get();
      if (localIccRecords != null) {
        str = localIccRecords.getGid1();
      }
      return str;
    }
    if (isPhoneTypeCdma())
    {
      loge("GID1 is not available in CDMA");
      return null;
    }
    if (mSimRecords != null) {
      str = mSimRecords.getGid1();
    } else {
      str = "";
    }
    return str;
  }
  
  public String getGroupIdLevel2()
  {
    boolean bool = isPhoneTypeGsm();
    String str = null;
    if (bool)
    {
      IccRecords localIccRecords = (IccRecords)mIccRecords.get();
      if (localIccRecords != null) {
        str = localIccRecords.getGid2();
      }
      return str;
    }
    if (isPhoneTypeCdma())
    {
      loge("GID2 is not available in CDMA");
      return null;
    }
    if (mSimRecords != null) {
      str = mSimRecords.getGid2();
    } else {
      str = "";
    }
    return str;
  }
  
  public IccCard getIccCard()
  {
    Object localObject = getUiccProfile();
    if (localObject != null) {
      return localObject;
    }
    localObject = mUiccController.getUiccSlotForPhone(mPhoneId);
    if ((localObject != null) && (!((UiccSlot)localObject).isStateUnknown())) {
      return new IccCard(IccCardConstants.State.ABSENT);
    }
    return new IccCard(IccCardConstants.State.UNKNOWN);
  }
  
  public IccPhoneBookInterfaceManager getIccPhoneBookInterfaceManager()
  {
    return mIccPhoneBookIntManager;
  }
  
  public boolean getIccRecordsLoaded()
  {
    UiccProfile localUiccProfile = getUiccProfile();
    boolean bool;
    if ((localUiccProfile != null) && (localUiccProfile.getIccRecordsLoaded())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String getIccSerialNumber()
  {
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    Object localObject = localIccRecords;
    if (!isPhoneTypeGsm())
    {
      localObject = localIccRecords;
      if (localIccRecords == null) {
        localObject = mUiccController.getIccRecords(mPhoneId, 1);
      }
    }
    if (localObject != null) {
      localObject = ((IccRecords)localObject).getIccId();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public IccSmsInterfaceManager getIccSmsInterfaceManager()
  {
    return mIccSmsInterfaceManager;
  }
  
  public String getImei()
  {
    String str;
    if (mPhoneId == 0) {
      str = SystemProperties.get("persist.radio.device.imei");
    } else {
      str = SystemProperties.get("persist.radio.device.imei2");
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getImei() is ");
    localStringBuilder.append(str);
    localStringBuilder.append(", phoneId: ");
    localStringBuilder.append(mPhoneId);
    localStringBuilder.append(", Gsm: ");
    localStringBuilder.append(isPhoneTypeGsm());
    logd(localStringBuilder.toString());
    if ((((CarrierConfigManager)mContext.getSystemService("carrier_config")).getConfigForSubId(getSubId()).getBoolean("config_enable_display_14digit_imei")) && (!TextUtils.isEmpty(str)) && (str.length() > 14)) {
      return str.substring(0, 14);
    }
    return str;
  }
  
  public IsimRecords getIsimRecords()
  {
    return mIsimUiccRecords;
  }
  
  public String getLine1AlphaTag()
  {
    boolean bool = isPhoneTypeGsm();
    String str = null;
    if (bool)
    {
      IccRecords localIccRecords = (IccRecords)mIccRecords.get();
      if (localIccRecords != null) {
        str = localIccRecords.getMsisdnAlphaTag();
      }
      return str;
    }
    loge("getLine1AlphaTag: not possible in CDMA");
    return null;
  }
  
  public String getLine1Number()
  {
    if (isPhoneTypeGsm())
    {
      Object localObject = (IccRecords)mIccRecords.get();
      if (localObject != null) {
        localObject = ((IccRecords)localObject).getMsisdnNumber();
      } else {
        localObject = null;
      }
      return localObject;
    }
    return mSST.getMdnNumber();
  }
  
  public int getLteOnCdmaMode()
  {
    int i = super.getLteOnCdmaMode();
    UiccCardApplication localUiccCardApplication = mUiccController.getUiccCardApplication(mPhoneId, 2);
    if ((localUiccCardApplication != null) && (localUiccCardApplication.getType() == IccCardApplicationStatus.AppType.APPTYPE_RUIM) && (i == 1)) {
      return 0;
    }
    return i;
  }
  
  public String getMeid()
  {
    return mMeid;
  }
  
  public String getMsisdn()
  {
    boolean bool = isPhoneTypeGsm();
    IccRecords localIccRecords = null;
    Object localObject = null;
    if (bool)
    {
      localIccRecords = (IccRecords)mIccRecords.get();
      if (localIccRecords != null) {
        localObject = localIccRecords.getMsisdnNumber();
      }
      return localObject;
    }
    if (isPhoneTypeCdmaLte())
    {
      localObject = localIccRecords;
      if (mSimRecords != null) {
        localObject = mSimRecords.getMsisdnNumber();
      }
      return localObject;
    }
    loge("getMsisdn: not expected on CDMA");
    return null;
  }
  
  public boolean getMute()
  {
    return mCT.getMute();
  }
  
  public String getNai()
  {
    IccRecords localIccRecords = mUiccController.getIccRecords(mPhoneId, 2);
    Object localObject;
    if (Log.isLoggable("GsmCdmaPhone", 2))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("IccRecords is ");
      ((StringBuilder)localObject).append(localIccRecords);
      Rlog.v("GsmCdmaPhone", ((StringBuilder)localObject).toString());
    }
    if (localIccRecords != null) {
      localObject = localIccRecords.getNAI();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public void getNeighboringCids(Message paramMessage, WorkSource paramWorkSource)
  {
    if (isPhoneTypeGsm())
    {
      mCi.getNeighboringCids(paramMessage, paramWorkSource);
    }
    else if (paramMessage != null)
    {
      paramWorkSource = new CommandException(CommandException.Error.REQUEST_NOT_SUPPORTED);
      forMessageexception = paramWorkSource;
      paramMessage.sendToTarget();
    }
  }
  
  public String getOperatorNumeric()
  {
    StringBuilder localStringBuilder = null;
    Object localObject1 = null;
    Object localObject2;
    if (isPhoneTypeGsm())
    {
      localObject2 = (IccRecords)mIccRecords.get();
      if (localObject2 != null) {
        localObject1 = ((IccRecords)localObject2).getOperatorNumeric();
      }
    }
    else
    {
      localObject2 = null;
      if (mCdmaSubscriptionSource == 1)
      {
        localObject1 = SystemProperties.get("ro.cdma.home.operator.numeric");
      }
      else
      {
        localObject1 = localStringBuilder;
        if (mCdmaSubscriptionSource == 0)
        {
          localObject1 = (UiccCardApplication)mUiccApplication.get();
          if ((localObject1 != null) && (((UiccCardApplication)localObject1).getType() == IccCardApplicationStatus.AppType.APPTYPE_RUIM))
          {
            logd("Legacy RUIM app present");
            localObject2 = (IccRecords)mIccRecords.get();
          }
          else
          {
            localObject2 = mSimRecords;
          }
          if ((localObject2 != null) && (localObject2 == mSimRecords))
          {
            localObject1 = ((IccRecords)localObject2).getOperatorNumeric();
          }
          else
          {
            IccRecords localIccRecords = (IccRecords)mIccRecords.get();
            localObject1 = localStringBuilder;
            localObject2 = localIccRecords;
            if (localIccRecords != null)
            {
              localObject1 = localStringBuilder;
              localObject2 = localIccRecords;
              if ((localIccRecords instanceof RuimRecords))
              {
                localObject1 = ((RuimRecords)localIccRecords).getRUIMOperatorNumeric();
                localObject2 = localIccRecords;
              }
            }
          }
        }
      }
      if (localObject1 == null)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("getOperatorNumeric: Cannot retrieve operatorNumeric: mCdmaSubscriptionSource = ");
        localStringBuilder.append(mCdmaSubscriptionSource);
        localStringBuilder.append(" mIccRecords = ");
        if (localObject2 != null) {
          localObject2 = Boolean.valueOf(((IccRecords)localObject2).getRecordsLoaded());
        } else {
          localObject2 = null;
        }
        localStringBuilder.append(localObject2);
        loge(localStringBuilder.toString());
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("getOperatorNumeric: mCdmaSubscriptionSource = ");
      ((StringBuilder)localObject2).append(mCdmaSubscriptionSource);
      ((StringBuilder)localObject2).append(" operatorNumeric = ");
      ((StringBuilder)localObject2).append((String)localObject1);
      logd(((StringBuilder)localObject2).toString());
    }
    return localObject1;
  }
  
  public void getOutgoingCallerIdDisplay(Message paramMessage)
  {
    if (isPhoneTypeGsm())
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && ((localPhone.getServiceState().getState() == 0) || (localPhone.isUtEnabled())))
      {
        localPhone.getOutgoingCallerIdDisplay(paramMessage);
        return;
      }
      mCi.getCLIR(paramMessage);
    }
    else
    {
      loge("getOutgoingCallerIdDisplay: not possible in CDMA");
    }
  }
  
  public List<? extends MmiCode> getPendingMmiCodes()
  {
    return mPendingMMIs;
  }
  
  public int getPhoneType()
  {
    if (mPrecisePhoneType == 1) {
      return 1;
    }
    return 2;
  }
  
  public String getPlmn()
  {
    boolean bool = isPhoneTypeGsm();
    IccRecords localIccRecords = null;
    Object localObject = null;
    if (bool)
    {
      localIccRecords = (IccRecords)mIccRecords.get();
      if (localIccRecords != null) {
        localObject = localIccRecords.getPnnHomeName();
      }
      return localObject;
    }
    if (isPhoneTypeCdma())
    {
      loge("Plmn is not available in CDMA");
      return null;
    }
    localObject = localIccRecords;
    if (mSimRecords != null) {
      localObject = mSimRecords.getPnnHomeName();
    }
    return localObject;
  }
  
  public Call getRingingCall()
  {
    Phone localPhone = mImsPhone;
    if ((localPhone != null) && (localPhone.getRingingCall().isRinging())) {
      return localPhone.getRingingCall();
    }
    return mCT.mRingingCall;
  }
  
  public SIMRecords getSIMRecords()
  {
    return mSimRecords;
  }
  
  public ServiceState getServiceState()
  {
    if (((mSST == null) || (mSST.mSS.getState() != 0)) && (mImsPhone != null))
    {
      ServiceState localServiceState;
      if (mSST == null) {
        localServiceState = new ServiceState();
      } else {
        localServiceState = mSST.mSS;
      }
      return ServiceState.mergeServiceStates(localServiceState, mImsPhone.getServiceState());
    }
    if (mSST != null) {
      return mSST.mSS;
    }
    return new ServiceState();
  }
  
  public ServiceStateTracker getServiceStateTracker()
  {
    return mSST;
  }
  
  public PhoneConstants.State getState()
  {
    if (mImsPhone != null)
    {
      PhoneConstants.State localState = mImsPhone.getState();
      if (localState != PhoneConstants.State.IDLE) {
        return localState;
      }
    }
    return mCT.mState;
  }
  
  public String getSubscriberId()
  {
    String str = null;
    if (isPhoneTypeCdma())
    {
      str = mSST.getImsi();
    }
    else
    {
      IccRecords localIccRecords = mUiccController.getIccRecords(mPhoneId, 1);
      if (localIccRecords != null) {
        str = localIccRecords.getIMSI();
      }
    }
    return str;
  }
  
  public String getSystemProperty(String paramString1, String paramString2)
  {
    if (getUnitTestMode()) {
      return null;
    }
    return TelephonyManager.getTelephonyProperty(mPhoneId, paramString1, paramString2);
  }
  
  public UiccCardApplication getUiccCardApplication()
  {
    if (isPhoneTypeGsm()) {
      return mUiccController.getUiccCardApplication(mPhoneId, 1);
    }
    return mUiccController.getUiccCardApplication(mPhoneId, 2);
  }
  
  public String getVoiceMailAlphaTag()
  {
    Object localObject = "";
    if (isPhoneTypeGsm())
    {
      localObject = (IccRecords)mIccRecords.get();
      if (localObject != null) {
        localObject = ((IccRecords)localObject).getVoiceMailAlphaTag();
      } else {
        localObject = "";
      }
    }
    if ((localObject != null) && (((String)localObject).length() != 0)) {
      return localObject;
    }
    return mContext.getText(17039364).toString();
  }
  
  public String getVoiceMailNumber()
  {
    if (isPhoneTypeGsm())
    {
      localObject1 = (IccRecords)mIccRecords.get();
      if (localObject1 != null) {
        localObject1 = ((IccRecords)localObject1).getVoiceMailNumber();
      } else {
        localObject1 = "";
      }
      localObject2 = localObject1;
      if (TextUtils.isEmpty((CharSequence)localObject1))
      {
        localObject2 = PreferenceManager.getDefaultSharedPreferences(getContext());
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("vm_number_key");
        ((StringBuilder)localObject1).append(getPhoneId());
        localObject2 = ((SharedPreferences)localObject2).getString(((StringBuilder)localObject1).toString(), null);
      }
    }
    else
    {
      localObject2 = PreferenceManager.getDefaultSharedPreferences(getContext());
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("vm_number_key_cdma");
      ((StringBuilder)localObject1).append(getPhoneId());
      localObject2 = ((SharedPreferences)localObject2).getString(((StringBuilder)localObject1).toString(), null);
    }
    Object localObject1 = localObject2;
    if (TextUtils.isEmpty((CharSequence)localObject2))
    {
      PersistableBundle localPersistableBundle = ((CarrierConfigManager)getContext().getSystemService("carrier_config")).getConfig();
      localObject1 = localObject2;
      if (localPersistableBundle != null)
      {
        localObject1 = localPersistableBundle.getString("default_vm_number_string");
        localObject2 = localPersistableBundle.getString("default_vm_number_roaming_string");
        if ((!TextUtils.isEmpty((CharSequence)localObject2)) && (mSST.mSS.getRoaming())) {
          localObject1 = localObject2;
        }
      }
    }
    Object localObject2 = localObject1;
    if (!isPhoneTypeGsm())
    {
      localObject2 = localObject1;
      if (TextUtils.isEmpty((CharSequence)localObject1))
      {
        localObject1 = ((CarrierConfigManager)getContext().getSystemService("carrier_config")).getConfig();
        if ((localObject1 != null) && (((PersistableBundle)localObject1).getBoolean("config_telephony_use_own_number_for_voicemail_bool"))) {
          localObject2 = getLine1Number();
        } else {
          localObject2 = "*86";
        }
      }
    }
    return localObject2;
  }
  
  @VisibleForTesting
  public PowerManager.WakeLock getWakeLock()
  {
    return mWakeLock;
  }
  
  public boolean handleInCallMmiCommands(String paramString)
    throws CallStateException
  {
    if (!isPhoneTypeGsm())
    {
      loge("method handleInCallMmiCommands is NOT supported in CDMA!");
      return false;
    }
    Phone localPhone = mImsPhone;
    if ((localPhone != null) && (localPhone.getServiceState().getState() == 0)) {
      return localPhone.handleInCallMmiCommands(paramString);
    }
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
    int i = what;
    boolean bool = false;
    Object localObject1;
    switch (i)
    {
    default: 
      switch (i)
      {
      default: 
        Object localObject2;
        switch (i)
        {
        default: 
          switch (i)
          {
          default: 
            switch (i)
            {
            default: 
              switch (i)
              {
              default: 
                int j;
                switch (i)
                {
                default: 
                  switch (i)
                  {
                  default: 
                    super.handleMessage(paramMessage);
                    break;
                  case 1046: 
                    paramMessage = ImsManager.getInstance(mContext, mPhoneId);
                    if ((paramMessage.isServiceAvailable()) && (getIccRecordsLoaded()))
                    {
                      paramMessage.updateImsServiceConfig(true);
                      updateImsConfigRetryCount = 0;
                    }
                    else if (updateImsConfigRetryCount < 5)
                    {
                      paramMessage = new StringBuilder();
                      paramMessage.append("ImsManager/IccRecords Loaded are not available to update CarrierConfig. retry count:");
                      paramMessage.append(updateImsConfigRetryCount);
                      logd(paramMessage.toString());
                      sendMessageDelayed(obtainMessage(1046), 2000L);
                      updateImsConfigRetryCount += 1;
                    }
                    break;
                  case 1045: 
                    Rlog.d("GsmCdmaPhone", "[ASIM] GSMPhone EVENT_SIM_IMSI_READY.");
                    if (!"CMCC".equalsIgnoreCase(SystemProperties.get("ro.config.CID", "")))
                    {
                      Rlog.d("GsmCdmaPhone", "[ASIM] CID is not CMCC so break.");
                    }
                    else
                    {
                      i = PREFERRED_NT_MODE;
                      try
                      {
                        j = TelephonyManager.getIntAtIndex(mContext.getContentResolver(), "preferred_network_mode", mPhoneId);
                        i = j;
                        paramMessage = new java/lang/StringBuilder;
                        i = j;
                        paramMessage.<init>();
                        i = j;
                        paramMessage.append("[ASIM] SubId = ");
                        i = j;
                        paramMessage.append(getSubId());
                        i = j;
                        paramMessage.append(", Old networkType: ");
                        i = j;
                        paramMessage.append(j);
                        i = j;
                        Rlog.d("GsmCdmaPhone", paramMessage.toString());
                        i = j;
                      }
                      catch (Settings.SettingNotFoundException paramMessage)
                      {
                        Rlog.e("GsmCdmaPhone", "[ASIM] Settings Exception Reading Value At Index for Settings.Global.PREFERRED_NETWORK_MODE");
                      }
                      if ((isCmccAndDisableNt()) && (i != 22))
                      {
                        paramMessage = new StringBuilder();
                        paramMessage.append("[ASIM] SubId = ");
                        paramMessage.append(getSubId());
                        paramMessage.append(", New networkType: ");
                        paramMessage.append(22);
                        Rlog.d("GsmCdmaPhone", paramMessage.toString());
                        TelephonyManager.putIntAtIndex(mContext.getContentResolver(), "preferred_network_mode", mPhoneId, 22);
                        paramMessage = mContext.getContentResolver();
                        localObject1 = new StringBuilder();
                        ((StringBuilder)localObject1).append("preferred_network_mode");
                        ((StringBuilder)localObject1).append(getSubId());
                        Settings.Global.putInt(paramMessage, ((StringBuilder)localObject1).toString(), 22);
                        setPreferredNetworkType(22, null);
                      }
                    }
                    break;
                  }
                  break;
                case 45: 
                  localObject1 = new StringBuilder();
                  ((StringBuilder)localObject1).append("Event EVENT_MODEM_RESET Received isInEcm = ");
                  ((StringBuilder)localObject1).append(isInEcm());
                  ((StringBuilder)localObject1).append(" isPhoneTypeGsm = ");
                  ((StringBuilder)localObject1).append(isPhoneTypeGsm());
                  ((StringBuilder)localObject1).append(" mImsPhone = ");
                  ((StringBuilder)localObject1).append(mImsPhone);
                  logd(((StringBuilder)localObject1).toString());
                  if (isInEcm()) {
                    if (isPhoneTypeGsm())
                    {
                      if (mImsPhone != null) {
                        mImsPhone.handleExitEmergencyCallbackMode();
                      }
                    }
                    else {
                      handleExitEmergencyCallbackMode(paramMessage);
                    }
                  }
                  if ((SystemProperties.getInt("persist.sys.modem.restart", 0) == 1) && (getPhoneId() == 0) && (!"user".equalsIgnoreCase(SystemProperties.get("ro.build.type", ""))))
                  {
                    paramMessage = new Intent("com.asus.modem.crash");
                    mContext.sendBroadcast(paramMessage);
                  }
                  break;
                case 44: 
                  logd("cdma_roaming_mode change is done");
                  break;
                case 43: 
                  if (!mContext.getResources().getBoolean(17957057)) {
                    mCi.getVoiceRadioTechnology(obtainMessage(40));
                  }
                  paramMessage = ImsManager.getInstance(mContext, mPhoneId);
                  if ((paramMessage.isServiceAvailable()) && (getIccRecordsLoaded()))
                  {
                    paramMessage.updateImsServiceConfig(true);
                  }
                  else
                  {
                    logd("ImsManager/IccRecords Loaded are not available to update CarrierConfig.");
                    sendMessageDelayed(obtainMessage(1046), 2000L);
                    updateImsConfigRetryCount += 1;
                  }
                  localObject1 = ((CarrierConfigManager)getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId());
                  if (localObject1 != null)
                  {
                    bool = ((PersistableBundle)localObject1).getBoolean("broadcast_emergency_call_state_changes_bool");
                    paramMessage = new StringBuilder();
                    paramMessage.append("broadcastEmergencyCallStateChanges = ");
                    paramMessage.append(bool);
                    logd(paramMessage.toString());
                    setBroadcastEmergencyCallStateChanges(bool);
                  }
                  else
                  {
                    loge("didn't get broadcastEmergencyCallStateChanges from carrier config");
                  }
                  if (localObject1 != null)
                  {
                    j = ((PersistableBundle)localObject1).getInt("cdma_roaming_mode_int");
                    i = Settings.Global.getInt(getContext().getContentResolver(), "roaming_settings", -1);
                    switch (j)
                    {
                    default: 
                      break;
                    case 0: 
                    case 1: 
                    case 2: 
                      paramMessage = new StringBuilder();
                      paramMessage.append("cdma_roaming_mode is going to changed to ");
                      paramMessage.append(j);
                      logd(paramMessage.toString());
                      setCdmaRoamingPreference(j, obtainMessage(44));
                      break;
                    case -1: 
                      if (i != j)
                      {
                        paramMessage = new StringBuilder();
                        paramMessage.append("cdma_roaming_mode is going to changed to ");
                        paramMessage.append(i);
                        logd(paramMessage.toString());
                        setCdmaRoamingPreference(i, obtainMessage(44));
                      }
                      break;
                    }
                    paramMessage = new StringBuilder();
                    paramMessage.append("Invalid cdma_roaming_mode settings: ");
                    paramMessage.append(j);
                    loge(paramMessage.toString());
                  }
                  else
                  {
                    loge("didn't get the cdma_roaming_mode changes from the carrier config.");
                  }
                  prepareEri();
                  mSST.pollState();
                  break;
                case 42: 
                  phoneObjectUpdater(arg1);
                  break;
                case 41: 
                  paramMessage = (AsyncResult)obj;
                  if ((exception == null) && (result != null))
                  {
                    mRilVersion = ((Integer)result).intValue();
                  }
                  else
                  {
                    logd("Unexpected exception on EVENT_RIL_CONNECTED");
                    mRilVersion = -1;
                  }
                  break;
                case 39: 
                case 40: 
                  if (what == 39) {
                    localObject1 = "EVENT_VOICE_RADIO_TECH_CHANGED";
                  } else {
                    localObject1 = "EVENT_REQUEST_VOICE_RADIO_TECH_DONE";
                  }
                  paramMessage = (AsyncResult)obj;
                  if (exception == null)
                  {
                    if ((result != null) && (((int[])result).length != 0))
                    {
                      i = ((int[])result)[0];
                      paramMessage = new StringBuilder();
                      paramMessage.append((String)localObject1);
                      paramMessage.append(": newVoiceTech=");
                      paramMessage.append(i);
                      logd(paramMessage.toString());
                      phoneObjectUpdater(i);
                    }
                    else
                    {
                      paramMessage = new StringBuilder();
                      paramMessage.append((String)localObject1);
                      paramMessage.append(": has no tech!");
                      loge(paramMessage.toString());
                    }
                  }
                  else
                  {
                    localObject2 = new StringBuilder();
                    ((StringBuilder)localObject2).append((String)localObject1);
                    ((StringBuilder)localObject2).append(": exception=");
                    ((StringBuilder)localObject2).append(exception);
                    loge(((StringBuilder)localObject2).toString());
                  }
                  break;
                }
                break;
              case 36: 
                paramMessage = (AsyncResult)obj;
                logd("Event EVENT_SS received");
                if (isPhoneTypeGsm()) {
                  new GsmMmiCode(this, (UiccCardApplication)mUiccApplication.get()).processSsData(paramMessage);
                }
                break;
              case 35: 
                localObject1 = (AsyncResult)obj;
                paramMessage = (RadioCapability)result;
                if (exception != null) {
                  Rlog.d("GsmCdmaPhone", "get phone radio capability fail, no need to change mRadioCapability");
                } else {
                  radioCapabilityUpdated(paramMessage);
                }
                localObject1 = new StringBuilder();
                ((StringBuilder)localObject1).append("EVENT_GET_RADIO_CAPABILITY: phone rc: ");
                ((StringBuilder)localObject1).append(paramMessage);
                Rlog.d("GsmCdmaPhone", ((StringBuilder)localObject1).toString());
              }
              break;
            case 29: 
              processIccRecordEvents(((Integer)obj).result).intValue());
              break;
            case 28: 
              paramMessage = (AsyncResult)obj;
              if (mSST.mSS.getIsManualSelection())
              {
                setNetworkSelectionModeAutomatic((Message)result);
                logd("SET_NETWORK_SELECTION_AUTOMATIC: set to automatic");
              }
              else
              {
                logd("SET_NETWORK_SELECTION_AUTOMATIC: already automatic, ignore");
              }
              break;
            case 27: 
              logd("EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED");
              mCdmaSubscriptionSource = mCdmaSSM.getCdmaSubscriptionSource();
              break;
            case 26: 
              handleExitEmergencyCallbackMode(paramMessage);
              break;
            case 25: 
              handleEnterEmergencyCallbackMode(paramMessage);
            }
            break;
          case 22: 
            logd("Event EVENT_RUIM_RECORDS_LOADED Received");
            updateCurrentCarrierInProvider();
            break;
          case 21: 
            paramMessage = (AsyncResult)obj;
            if (exception == null)
            {
              paramMessage = (String[])result;
              mImei = paramMessage[0];
              mImeiSv = paramMessage[1];
              mEsn = paramMessage[2];
              mMeid = paramMessage[3];
              if (invalidMeid(mMeid))
              {
                paramMessage = new StringBuilder();
                paramMessage.append("[ABSP] Change mMeid[");
                paramMessage.append(mMeid);
                paramMessage.append("] to null");
                logd(paramMessage.toString());
                mMeid = null;
              }
              paramMessage = new StringBuilder();
              paramMessage.append("[ACTCC] EVENT_GET_DEVICE_IDENTITY_DONE mImei: ");
              paramMessage.append(mImei);
              paramMessage.append(", mImeiSv:");
              paramMessage.append(mImeiSv);
              paramMessage.append(", mEsn:");
              paramMessage.append(mEsn);
              paramMessage.append(", mMeid:");
              paramMessage.append(mMeid);
              logd(paramMessage.toString());
            }
            break;
          case 20: 
            localObject1 = (AsyncResult)obj;
            if (((isPhoneTypeGsm()) && (IccVmNotSupportedException.class.isInstance(exception))) || ((!isPhoneTypeGsm()) && (IccException.class.isInstance(exception))))
            {
              storeVoiceMailNumber(mVmNumber);
              exception = null;
            }
            paramMessage = (Message)userObj;
            if (paramMessage != null)
            {
              AsyncResult.forMessage(paramMessage, result, exception);
              paramMessage.sendToTarget();
            }
            break;
          case 19: 
            logd("Event EVENT_REGISTERED_TO_NETWORK Received");
            if (isPhoneTypeGsm()) {
              syncClirSetting();
            }
            break;
          case 18: 
            localObject1 = (AsyncResult)obj;
            if (exception == null) {
              saveClirSetting(arg1);
            }
            paramMessage = (Message)userObj;
            if (paramMessage != null)
            {
              AsyncResult.forMessage(paramMessage, result, exception);
              paramMessage.sendToTarget();
            }
            break;
          }
          break;
        case 13: 
          localObject1 = (AsyncResult)obj;
          if (exception == null) {
            handleCfuQueryResult((CallForwardInfo[])result);
          }
          paramMessage = (Message)userObj;
          if (paramMessage != null)
          {
            AsyncResult.forMessage(paramMessage, result, exception);
            paramMessage.sendToTarget();
          }
          break;
        case 12: 
          localObject2 = (AsyncResult)obj;
          IccRecords localIccRecords = (IccRecords)mIccRecords.get();
          localObject1 = (Cfu)userObj;
          if ((exception == null) && (localIccRecords != null))
          {
            if (arg1 == 1) {
              bool = true;
            }
            setVoiceCallForwardingFlag(1, bool, mSetCfNumber);
          }
          if (mOnComplete != null)
          {
            AsyncResult.forMessage(mOnComplete, result, exception);
            mOnComplete.sendToTarget();
          }
          break;
        }
        break;
      case 10: 
        paramMessage = (AsyncResult)obj;
        if (exception == null) {
          mImeiSv = ((String)result);
        }
        break;
      case 9: 
        paramMessage = (AsyncResult)obj;
        if (exception == null)
        {
          mImei = ((String)result);
          paramMessage = new StringBuilder();
          paramMessage.append("[ACTCC] EVENT_GET_IMEI_DONE mImei: ");
          paramMessage.append(mImei);
          paramMessage.append(", mPhoneId: ");
          paramMessage.append(mPhoneId);
          logd(paramMessage.toString());
        }
        break;
      case 8: 
        logd("Event EVENT_RADIO_OFF_OR_NOT_AVAILABLE Received");
        handleRadioOffOrNotAvailable();
        break;
      case 7: 
        paramMessage = (String[])obj).result;
        if (paramMessage.length > 1) {
          try
          {
            onIncomingUSSD(Integer.parseInt(paramMessage[0]), paramMessage[1]);
          }
          catch (NumberFormatException paramMessage)
          {
            Rlog.w("GsmCdmaPhone", "error parsing USSD");
          }
        }
        break;
      case 6: 
        paramMessage = (AsyncResult)obj;
        if (exception == null)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Baseband version: ");
          ((StringBuilder)localObject1).append(result);
          logd(((StringBuilder)localObject1).toString());
          TelephonyManager.from(mContext).setBasebandVersionForPhone(getPhoneId(), (String)result);
          SystemProperties.set("gps.version.driver", (String)result);
        }
        break;
      case 5: 
        logd("Event EVENT_RADIO_ON Received");
        handleRadioOn();
      }
      break;
    case 3: 
      updateCurrentCarrierInProvider();
      paramMessage = getVmSimImsi();
      localObject1 = getSubscriberId();
      if (((!isPhoneTypeGsm()) || (paramMessage != null)) && (localObject1 != null) && (!((String)localObject1).equals(paramMessage)))
      {
        storeVoiceMailNumber(null);
        setVmSimImsi(null);
        setVideoCallForwardingPreference(false);
      }
      mSimRecordsLoadedRegistrants.notifyRegistrants();
      break;
    case 2: 
      logd("Event EVENT_SSN Received");
      if (isPhoneTypeGsm())
      {
        localObject1 = (AsyncResult)obj;
        paramMessage = result;
        mSsnRegistrants.notifyRegistrants((AsyncResult)localObject1);
      }
      break;
    case 1: 
      handleRadioAvailable();
    }
  }
  
  public boolean handlePinMmi(String paramString)
  {
    if (isPhoneTypeGsm()) {
      paramString = GsmMmiCode.newFromDialString(paramString, this, (UiccCardApplication)mUiccApplication.get());
    } else {
      paramString = CdmaMmiCode.newFromDialString(paramString, this, (UiccCardApplication)mUiccApplication.get());
    }
    if ((paramString != null) && (paramString.isPinPukCommand()))
    {
      mPendingMMIs.add(paramString);
      mMmiRegistrants.notifyRegistrants(new AsyncResult(null, paramString, null));
      try
      {
        paramString.processCode();
      }
      catch (CallStateException paramString) {}
      return true;
    }
    loge("Mmi is null or unrecognized!");
    return false;
  }
  
  public void handleTimerInEmergencyCallbackMode(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleTimerInEmergencyCallbackMode, unsupported action ");
      localStringBuilder.append(paramInt);
      Rlog.e("GsmCdmaPhone", localStringBuilder.toString());
      break;
    case 1: 
      removeCallbacks(mExitEcmRunnable);
      mEcmTimerResetRegistrants.notifyResult(Boolean.TRUE);
      break;
    case 0: 
      long l = SystemProperties.getLong("ro.cdma.ecmexittimer", 300000L);
      postDelayed(mExitEcmRunnable, l);
      mEcmTimerResetRegistrants.notifyResult(Boolean.FALSE);
    }
  }
  
  public boolean handleUssdRequest(String paramString, ResultReceiver paramResultReceiver)
  {
    if ((isPhoneTypeGsm()) && (mPendingMMIs.size() <= 0))
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && ((localPhone.getServiceState().getState() == 0) || (localPhone.isUtEnabled()))) {
        try
        {
          logd("handleUssdRequest: attempting over IMS");
          boolean bool = localPhone.handleUssdRequest(paramString, paramResultReceiver);
          return bool;
        }
        catch (CallStateException localCallStateException)
        {
          if (!"cs_fallback".equals(localCallStateException.getMessage())) {
            return false;
          }
          logd("handleUssdRequest: fallback to CS required");
        }
      }
      try
      {
        PhoneInternalInterface.DialArgs.Builder localBuilder = new com/android/internal/telephony/PhoneInternalInterface$DialArgs$Builder;
        localBuilder.<init>();
        dialInternal(paramString, localBuilder.build(), paramResultReceiver);
        return true;
      }
      catch (Exception paramResultReceiver)
      {
        paramString = new StringBuilder();
        paramString.append("handleUssdRequest: exception");
        paramString.append(paramResultReceiver);
        logd(paramString.toString());
        return false;
      }
    }
    sendUssdResponse(paramString, null, -1, paramResultReceiver);
    return true;
  }
  
  public boolean isCmccAndDisableNt()
  {
    boolean bool1 = false;
    boolean bool2 = bool1;
    if (SystemProperties.getInt("ro.asus.disable_cmcc_nt", 0) == 1)
    {
      bool2 = bool1;
      if (isCmccSim() == 1) {
        bool2 = true;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ASIM] isDisableNtForCmcc(): ret = ");
    localStringBuilder.append(bool2);
    Rlog.d("GsmCdmaPhone", localStringBuilder.toString());
    return bool2;
  }
  
  public int isCmccSim()
  {
    int i = -1;
    Object localObject = (IccRecords)mIccRecords.get();
    int j = i;
    if (localObject != null)
    {
      localObject = ((IccRecords)localObject).getOperatorNumeric();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[ASIM] mccmnc = ");
      localStringBuilder.append((String)localObject);
      Rlog.d("GsmCdmaPhone", localStringBuilder.toString());
      if ((!TextUtils.isEmpty((CharSequence)localObject)) && (("46000".equals(localObject)) || ("46002".equals(localObject)) || ("46004".equals(localObject)) || ("46007".equals(localObject)) || ("46008".equals(localObject))))
      {
        j = 1;
      }
      else
      {
        j = i;
        if (!TextUtils.isEmpty((CharSequence)localObject)) {
          j = 0;
        }
      }
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[ASIM] isCmcc = ");
    ((StringBuilder)localObject).append(j);
    Rlog.d("GsmCdmaPhone", ((StringBuilder)localObject).toString());
    return j;
  }
  
  public boolean isCspPlmnEnabled()
  {
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    boolean bool;
    if (localIccRecords != null) {
      bool = localIccRecords.isCspPlmnEnabled();
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDataEnabled()
  {
    return mDcTracker.isDataEnabled();
  }
  
  public boolean isEriFileLoaded()
  {
    return mEriManager.isEriFileLoaded();
  }
  
  public boolean isInCall()
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
  
  public boolean isInEmergencyCall()
  {
    if (isPhoneTypeGsm()) {
      return false;
    }
    return mCT.isInEmergencyCall();
  }
  
  public boolean isMinInfoReady()
  {
    return mSST.isMinInfoReady();
  }
  
  public boolean isNotificationOfWfcCallRequired(String paramString)
  {
    Object localObject = ((CarrierConfigManager)mContext.getSystemService("carrier_config")).getConfigForSubId(getSubId());
    boolean bool1 = true;
    int i;
    if ((localObject != null) && (((PersistableBundle)localObject).getBoolean("notify_international_call_on_wfc_bool"))) {
      i = 1;
    } else {
      i = 0;
    }
    if (i == 0) {
      return false;
    }
    localObject = mImsPhone;
    boolean bool2 = isEmergencyNumber(paramString);
    if ((!isImsUseEnabled()) || (localObject == null) || (((Phone)localObject).isVolteEnabled()) || (!((Phone)localObject).isWifiCallingEnabled()) || (bool2) || (!PhoneNumberUtils.isInternationalNumber(paramString, getCountryIso()))) {
      bool1 = false;
    }
    return bool1;
  }
  
  public boolean isOtaSpNumber(String paramString)
  {
    if (isPhoneTypeGsm()) {
      return super.isOtaSpNumber(paramString);
    }
    boolean bool1 = false;
    paramString = PhoneNumberUtils.extractNetworkPortionAlt(paramString);
    if (paramString != null)
    {
      boolean bool2 = isIs683OtaSpDialStr(paramString);
      bool1 = bool2;
      if (!bool2) {
        bool1 = isCarrierOtaSpNum(paramString);
      }
    }
    paramString = new StringBuilder();
    paramString.append("isOtaSpNumber ");
    paramString.append(bool1);
    Rlog.d("GsmCdmaPhone", paramString.toString());
    return bool1;
  }
  
  public boolean isPhoneTypeCdma()
  {
    boolean bool;
    if (mPrecisePhoneType == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPhoneTypeCdmaLte()
  {
    boolean bool;
    if (mPrecisePhoneType == 6) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPhoneTypeGsm()
  {
    int i = mPrecisePhoneType;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUserDataEnabled()
  {
    return mDcTracker.isUserDataEnabled();
  }
  
  public boolean isUtEnabled()
  {
    Phone localPhone = mImsPhone;
    if (localPhone != null) {
      return localPhone.isUtEnabled();
    }
    logd("isUtEnabled: called for GsmCdma");
    return false;
  }
  
  public boolean needsOtaServiceProvisioning()
  {
    boolean bool1 = isPhoneTypeGsm();
    boolean bool2 = false;
    if (bool1) {
      return false;
    }
    if (mSST.getOtasp() != 3) {
      bool2 = true;
    }
    return bool2;
  }
  
  public void notifyCallForwardingIndicator()
  {
    mNotifier.notifyCallForwardingChanged(this);
  }
  
  public void notifyDisconnect(Connection paramConnection)
  {
    mDisconnectRegistrants.notifyResult(paramConnection);
    mNotifier.notifyDisconnectCause(paramConnection.getDisconnectCause(), paramConnection.getPreciseDisconnectCause());
  }
  
  public void notifyEcbmTimerReset(Boolean paramBoolean)
  {
    mEcmTimerResetRegistrants.notifyResult(paramBoolean);
  }
  
  public void notifyEmergencyCallRegistrants(boolean paramBoolean)
  {
    mEmergencyCallToggledRegistrants.notifyResult(Integer.valueOf(paramBoolean));
  }
  
  public void notifyLocationChanged()
  {
    mNotifier.notifyCellLocation(this);
  }
  
  public void notifyNewRingingConnection(Connection paramConnection)
  {
    super.notifyNewRingingConnectionP(paramConnection);
  }
  
  public void notifyPhoneStateChanged()
  {
    mNotifier.notifyPhoneState(this);
  }
  
  public void notifyPreciseCallStateChanged()
  {
    super.notifyPreciseCallStateChangedP();
  }
  
  public void notifyServiceStateChanged(ServiceState paramServiceState)
  {
    super.notifyServiceStateChangedP(paramServiceState);
  }
  
  public void notifySuppServiceFailed(PhoneInternalInterface.SuppService paramSuppService)
  {
    mSuppServiceFailedRegistrants.notifyResult(paramSuppService);
  }
  
  public void notifyUnknownConnection(Connection paramConnection)
  {
    super.notifyUnknownConnectionP(paramConnection);
  }
  
  public void onMMIDone(MmiCode paramMmiCode)
  {
    Object localObject;
    if ((!mPendingMMIs.remove(paramMmiCode)) && ((!isPhoneTypeGsm()) || ((!paramMmiCode.isUssdRequest()) && (!((GsmMmiCode)paramMmiCode).isSsInfo()))))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onMMIDone: invalid response or already handled; ignoring: ");
      ((StringBuilder)localObject).append(paramMmiCode);
      Rlog.i("GsmCdmaPhone", ((StringBuilder)localObject).toString());
    }
    else
    {
      localObject = paramMmiCode.getUssdCallbackReceiver();
      if (localObject != null)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("onMMIDone: invoking callback: ");
        localStringBuilder.append(paramMmiCode);
        Rlog.i("GsmCdmaPhone", localStringBuilder.toString());
        int i;
        if (paramMmiCode.getState() == MmiCode.State.COMPLETE) {
          i = 100;
        } else {
          i = -1;
        }
        sendUssdResponse(paramMmiCode.getDialString(), paramMmiCode.getMessage(), i, (ResultReceiver)localObject);
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("onMMIDone: notifying registrants: ");
        ((StringBuilder)localObject).append(paramMmiCode);
        Rlog.i("GsmCdmaPhone", ((StringBuilder)localObject).toString());
        mMmiCompleteRegistrants.notifyRegistrants(new AsyncResult(null, paramMmiCode, null));
      }
    }
  }
  
  protected void onUpdateIccAvailability()
  {
    if (mUiccController == null) {
      return;
    }
    if ((isPhoneTypeGsm()) || (isPhoneTypeCdmaLte()))
    {
      localObject1 = mUiccController.getUiccCardApplication(mPhoneId, 3);
      localObject2 = null;
      if (localObject1 != null)
      {
        localObject2 = (IsimUiccRecords)((UiccCardApplication)localObject1).getIccRecords();
        logd("New ISIM application found");
      }
      mIsimUiccRecords = ((IsimUiccRecords)localObject2);
    }
    if (mSimRecords != null) {
      mSimRecords.unregisterForRecordsLoaded(this);
    }
    if ((!isPhoneTypeCdmaLte()) && (!isPhoneTypeCdma()))
    {
      mSimRecords = null;
    }
    else
    {
      localObject1 = mUiccController.getUiccCardApplication(mPhoneId, 1);
      localObject2 = null;
      if (localObject1 != null) {
        localObject2 = (SIMRecords)((UiccCardApplication)localObject1).getIccRecords();
      }
      mSimRecords = ((SIMRecords)localObject2);
      if (mSimRecords != null) {
        mSimRecords.registerForRecordsLoaded(this, 3, null);
      }
    }
    Object localObject1 = getUiccCardApplication();
    Object localObject2 = localObject1;
    if (!isPhoneTypeGsm())
    {
      localObject2 = localObject1;
      if (localObject1 == null)
      {
        logd("can't find 3GPP2 application; trying APP_FAM_3GPP");
        localObject2 = mUiccController.getUiccCardApplication(mPhoneId, 1);
      }
    }
    localObject1 = (UiccCardApplication)mUiccApplication.get();
    if (localObject1 != localObject2)
    {
      if (localObject1 != null)
      {
        logd("Removing stale icc objects.");
        if (mIccRecords.get() != null)
        {
          unregisterForIccRecordEvents();
          mIccPhoneBookIntManager.updateIccRecords(null);
        }
        mIccRecords.set(null);
        mUiccApplication.set(null);
      }
      if (localObject2 != null)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("New Uicc application found. type = ");
        ((StringBuilder)localObject1).append(((UiccCardApplication)localObject2).getType());
        logd(((StringBuilder)localObject1).toString());
        mUiccApplication.set(localObject2);
        mIccRecords.set(((UiccCardApplication)localObject2).getIccRecords());
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("mIccRecords = ");
        ((StringBuilder)localObject2).append(mIccRecords);
        logd(((StringBuilder)localObject2).toString());
        registerForIccRecordEvents();
        mIccPhoneBookIntManager.updateIccRecords((IccRecords)mIccRecords.get());
        updateDataConnectionTracker();
      }
    }
  }
  
  protected void phoneObjectUpdater(int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("phoneObjectUpdater: newVoiceRadioTech=");
    ((StringBuilder)localObject).append(paramInt);
    logd(((StringBuilder)localObject).toString());
    int i;
    if (!ServiceState.isLte(paramInt))
    {
      i = paramInt;
      if (paramInt != 0) {}
    }
    else
    {
      localObject = ((CarrierConfigManager)getContext().getSystemService("carrier_config")).getConfigForSubId(getSubId());
      if (localObject != null)
      {
        i = ((PersistableBundle)localObject).getInt("volte_replacement_rat_int");
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("phoneObjectUpdater: volteReplacementRat=");
        ((StringBuilder)localObject).append(i);
        logd(((StringBuilder)localObject).toString());
        if (i != 0) {
          paramInt = i;
        }
        i = paramInt;
      }
      else
      {
        loge("phoneObjectUpdater: didn't get volteReplacementRat from carrier config");
        i = paramInt;
      }
    }
    if ((mRilVersion == 6) && (getLteOnCdmaMode() == 1))
    {
      if (getPhoneType() == 2)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("phoneObjectUpdater: LTE ON CDMA property is set. Use CDMA Phone newVoiceRadioTech=");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" mActivePhone=");
        ((StringBuilder)localObject).append(getPhoneName());
        logd(((StringBuilder)localObject).toString());
        return;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("phoneObjectUpdater: LTE ON CDMA property is set. Switch to CDMALTEPhone newVoiceRadioTech=");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(" mActivePhone=");
      ((StringBuilder)localObject).append(getPhoneName());
      logd(((StringBuilder)localObject).toString());
      paramInt = 6;
    }
    else
    {
      if (isShuttingDown())
      {
        logd("Device is shutting down. No need to switch phone now.");
        return;
      }
      bool1 = ServiceState.isCdma(i);
      bool2 = ServiceState.isGsm(i);
      if (((bool1) && (getPhoneType() == 2)) || ((bool2) && (getPhoneType() == 1)))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("phoneObjectUpdater: No change ignore, newVoiceRadioTech=");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" mActivePhone=");
        ((StringBuilder)localObject).append(getPhoneName());
        logd(((StringBuilder)localObject).toString());
        return;
      }
      paramInt = i;
      if (!bool1)
      {
        paramInt = i;
        if (!bool2)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("phoneObjectUpdater: newVoiceRadioTech=");
          ((StringBuilder)localObject).append(i);
          ((StringBuilder)localObject).append(" doesn't match either CDMA or GSM - error! No phone change");
          loge(((StringBuilder)localObject).toString());
          return;
        }
      }
    }
    if (paramInt == 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("phoneObjectUpdater: Unknown rat ignore,  newVoiceRadioTech=Unknown. mActivePhone=");
      ((StringBuilder)localObject).append(getPhoneName());
      logd(((StringBuilder)localObject).toString());
      return;
    }
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (mResetModemOnRadioTechnologyChange)
    {
      bool1 = bool2;
      if (mCi.getRadioState().isOn())
      {
        bool1 = true;
        logd("phoneObjectUpdater: Setting Radio Power to Off");
        mCi.setRadioPower(false, null);
      }
    }
    switchVoiceRadioTech(paramInt);
    if ((mResetModemOnRadioTechnologyChange) && (bool1))
    {
      logd("phoneObjectUpdater: Resetting Radio");
      mCi.setRadioPower(bool1, null);
    }
    localObject = getUiccProfile();
    if (localObject != null) {
      ((UiccProfile)localObject).setVoiceRadioTech(paramInt);
    }
    localObject = new Intent("android.intent.action.RADIO_TECHNOLOGY");
    ((Intent)localObject).putExtra("phoneName", getPhoneName());
    SubscriptionManager.putPhoneIdAndSubIdExtra((Intent)localObject, mPhoneId);
    ActivityManager.broadcastStickyIntent((Intent)localObject, -1);
  }
  
  public void prepareEri()
  {
    if (mEriManager == null)
    {
      Rlog.e("GsmCdmaPhone", "PrepareEri: Trying to access stale objects");
      return;
    }
    mEriManager.loadEriFile();
    if (mEriManager.isEriFileLoaded())
    {
      logd("ERI read, notify registrants");
      mEriFileLoadedRegistrants.notifyRegistrants();
    }
  }
  
  public void registerForCallWaiting(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCT.registerForCallWaiting(paramHandler, paramInt, paramObject);
  }
  
  public void registerForCdmaOtaStatusChange(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForCdmaOtaProvision(paramHandler, paramInt, paramObject);
  }
  
  public void registerForEcmTimerReset(Handler paramHandler, int paramInt, Object paramObject)
  {
    mEcmTimerResetRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForEriFileLoaded(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mEriFileLoadedRegistrants.add(paramHandler);
  }
  
  public void registerForSimRecordsLoaded(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSimRecordsLoadedRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSubscriptionInfoReady(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSST.registerForSubscriptionInfoReady(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSuppServiceNotification(Handler paramHandler, int paramInt, Object paramObject)
  {
    mSsnRegistrants.addUnique(paramHandler, paramInt, paramObject);
    if (mSsnRegistrants.size() == 1) {
      mCi.setSuppServiceNotifications(true, null);
    }
  }
  
  public void rejectCall()
    throws CallStateException
  {
    mCT.rejectCall();
  }
  
  public void resetCarrierKeysForImsiEncryption()
  {
    mCIM.resetCarrierKeysForImsiEncryption(mContext, mPhoneId);
  }
  
  public void sendBurstDtmf(String paramString, int paramInt1, int paramInt2, Message paramMessage)
  {
    if (isPhoneTypeGsm())
    {
      loge("[GsmCdmaPhone] sendBurstDtmf() is a CDMA method");
    }
    else
    {
      int i = 1;
      int k;
      for (int j = 0;; j++)
      {
        k = i;
        if (j >= paramString.length()) {
          break;
        }
        if (!PhoneNumberUtils.is12Key(paramString.charAt(j)))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("sendDtmf called with invalid character '");
          localStringBuilder.append(paramString.charAt(j));
          localStringBuilder.append("'");
          Rlog.e("GsmCdmaPhone", localStringBuilder.toString());
          k = 0;
          break;
        }
      }
      if ((mCT.mState == PhoneConstants.State.OFFHOOK) && (k != 0)) {
        mCi.sendBurstDtmf(paramString, paramInt1, paramInt2, paramMessage);
      }
    }
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
    else if (mCT.mState == PhoneConstants.State.OFFHOOK)
    {
      mCi.sendDtmf(paramChar, null);
    }
  }
  
  public void sendEmergencyCallStateChange(boolean paramBoolean)
  {
    if (mBroadcastEmergencyCallStateChanges)
    {
      Object localObject = new Intent("android.intent.action.EMERGENCY_CALL_STATE_CHANGED");
      ((Intent)localObject).putExtra("phoneInEmergencyCall", paramBoolean);
      SubscriptionManager.putPhoneIdAndSubIdExtra((Intent)localObject, getPhoneId());
      ActivityManager.broadcastStickyIntent((Intent)localObject, -1);
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("sendEmergencyCallStateChange: callActive ");
      ((StringBuilder)localObject).append(paramBoolean);
      Rlog.d("GsmCdmaPhone", ((StringBuilder)localObject).toString());
    }
  }
  
  public void sendUssdResponse(String paramString)
  {
    if (isPhoneTypeGsm())
    {
      GsmMmiCode localGsmMmiCode = GsmMmiCode.newFromUssdUserInput(paramString, this, (UiccCardApplication)mUiccApplication.get());
      mPendingMMIs.add(localGsmMmiCode);
      mMmiRegistrants.notifyRegistrants(new AsyncResult(null, localGsmMmiCode, null));
      localGsmMmiCode.sendUssd(paramString);
    }
    else
    {
      loge("sendUssdResponse: not possible in CDMA");
    }
  }
  
  public void setBroadcastEmergencyCallStateChanges(boolean paramBoolean)
  {
    mBroadcastEmergencyCallStateChanges = paramBoolean;
  }
  
  public void setCallBarring(String paramString1, boolean paramBoolean, String paramString2, Message paramMessage, int paramInt)
  {
    if (isPhoneTypeGsm())
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && (localPhone.isUtEnabled()) && (TextUtils.isEmpty(paramString2)))
      {
        localPhone.setCallBarring(paramString1, paramBoolean, paramString2, paramMessage, paramInt);
        return;
      }
      mCi.setFacilityLock(paramString1, paramBoolean, paramString2, paramInt, paramMessage);
    }
    else
    {
      loge("setCallBarringOption: not possible in CDMA");
    }
  }
  
  public void setCallForwardingOption(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, Message paramMessage)
  {
    if ((!isPhoneTypeGsm()) && (!isImsUtEnabledOverCdma()))
    {
      loge("setCallForwardingOption: not possible in CDMA without IMS");
    }
    else
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && ((localPhone.getServiceState().getState() == 0) || (localPhone.isUtEnabled())))
      {
        localPhone.setCallForwardingOption(paramInt1, paramInt2, paramString, paramInt3, paramInt4, paramMessage);
        return;
      }
      if ((isValidCommandInterfaceCFAction(paramInt1)) && (isValidCommandInterfaceCFReason(paramInt2)))
      {
        if (paramInt2 == 0)
        {
          paramMessage = new Cfu(paramString, paramMessage);
          paramMessage = obtainMessage(12, isCfEnable(paramInt1), 0, paramMessage);
        }
        mCi.setCallForward(paramInt1, paramInt2, paramInt3, paramString, paramInt4, paramMessage);
      }
    }
  }
  
  public void setCallForwardingOption(int paramInt1, int paramInt2, String paramString, int paramInt3, Message paramMessage)
  {
    setCallForwardingOption(paramInt1, paramInt2, paramString, 1, paramInt3, paramMessage);
  }
  
  public void setCallWaiting(boolean paramBoolean, Message paramMessage)
  {
    if ((!isPhoneTypeGsm()) && (!isImsUtEnabledOverCdma()))
    {
      loge("method setCallWaiting is NOT supported in CDMA without IMS!");
    }
    else
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && ((localPhone.getServiceState().getState() == 0) || (localPhone.isUtEnabled())))
      {
        localPhone.setCallWaiting(paramBoolean, paramMessage);
        return;
      }
      mCi.setCallWaiting(paramBoolean, 1, paramMessage);
    }
  }
  
  public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo paramImsiEncryptionInfo)
  {
    CarrierInfoManager.setCarrierInfoForImsiEncryption(paramImsiEncryptionInfo, mContext, mPhoneId);
  }
  
  public void setCarrierTestOverride(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7)
  {
    Object localObject = null;
    if (isPhoneTypeGsm()) {
      localObject = (IccRecords)mIccRecords.get();
    } else if (isPhoneTypeCdmaLte()) {
      localObject = mSimRecords;
    } else {
      loge("setCarrierTestOverride fails in CDMA only");
    }
    if (localObject != null) {
      ((IccRecords)localObject).setCarrierTestOverride(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, paramString7);
    }
  }
  
  public void setCellBroadcastSmsConfig(int[] paramArrayOfInt, Message paramMessage)
  {
    loge("[GsmCdmaPhone] setCellBroadcastSmsConfig() is obsolete; use SmsManager");
    paramMessage.sendToTarget();
  }
  
  public void setDataRoamingEnabled(boolean paramBoolean)
  {
    mDcTracker.setDataRoamingEnabledByUser(paramBoolean);
  }
  
  public void setImsRegistrationState(boolean paramBoolean)
  {
    mSST.setImsRegistrationState(paramBoolean);
  }
  
  protected void setIsInEmergencyCall()
  {
    if (!isPhoneTypeGsm()) {
      mCT.setIsInEmergencyCall();
    }
  }
  
  public boolean setLine1Number(String paramString1, String paramString2, Message paramMessage)
  {
    if (isPhoneTypeGsm())
    {
      IccRecords localIccRecords = (IccRecords)mIccRecords.get();
      if (localIccRecords != null)
      {
        localIccRecords.setMsisdnNumber(paramString1, paramString2, paramMessage);
        return true;
      }
      return false;
    }
    loge("setLine1Number: not possible in CDMA");
    return false;
  }
  
  public void setLinkCapacityReportingCriteria(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
  {
    mCi.setLinkCapacityReportingCriteria(3000, 50, 50, paramArrayOfInt1, paramArrayOfInt2, paramInt, null);
  }
  
  public void setMute(boolean paramBoolean)
  {
    mCT.setMute(paramBoolean);
  }
  
  public void setOnEcbModeExitResponse(Handler paramHandler, int paramInt, Object paramObject)
  {
    mEcmExitRespRegistrant = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public boolean setOperatorBrandOverride(String paramString)
  {
    if (mUiccController == null) {
      return false;
    }
    UiccCard localUiccCard = mUiccController.getUiccCard(getPhoneId());
    if (localUiccCard == null) {
      return false;
    }
    boolean bool = localUiccCard.setOperatorBrandOverride(paramString);
    if (bool)
    {
      paramString = (IccRecords)mIccRecords.get();
      if (paramString != null) {
        TelephonyManager.from(mContext).setSimOperatorNameForPhone(getPhoneId(), paramString.getServiceProviderName());
      }
      if (mSST != null) {
        mSST.pollState();
      }
    }
    return bool;
  }
  
  public void setOutgoingCallerIdDisplay(int paramInt, Message paramMessage)
  {
    if (isPhoneTypeGsm())
    {
      Phone localPhone = mImsPhone;
      if ((localPhone != null) && ((localPhone.getServiceState().getState() == 0) || (localPhone.isUtEnabled())))
      {
        localPhone.setOutgoingCallerIdDisplay(paramInt, paramMessage);
        return;
      }
      mCi.setCLIR(paramInt, obtainMessage(18, paramInt, 0, paramMessage));
    }
    else
    {
      loge("setOutgoingCallerIdDisplay: not possible in CDMA");
    }
  }
  
  public void setRadioPower(boolean paramBoolean)
  {
    mSST.setRadioPower(paramBoolean);
  }
  
  public void setSignalStrengthReportingCriteria(int[] paramArrayOfInt, int paramInt)
  {
    mCi.setSignalStrengthReportingCriteria(3000, 2, paramArrayOfInt, paramInt, null);
  }
  
  public void setTTYMode(int paramInt, Message paramMessage)
  {
    super.setTTYMode(paramInt, paramMessage);
    if (mImsPhone != null) {
      mImsPhone.setTTYMode(paramInt, paramMessage);
    }
  }
  
  public void setUiTTYMode(int paramInt, Message paramMessage)
  {
    if (mImsPhone != null) {
      mImsPhone.setUiTTYMode(paramInt, paramMessage);
    }
  }
  
  public void setUserDataEnabled(boolean paramBoolean)
  {
    Phone[] arrayOfPhone = PhoneFactory.getPhones();
    int i = arrayOfPhone.length;
    for (int j = 0; j < i; j++) {
      mDcTracker.setUserDataEnabled(paramBoolean);
    }
  }
  
  public void setVoiceMailNumber(String paramString1, String paramString2, Message paramMessage)
  {
    mVmNumber = paramString2;
    paramString2 = obtainMessage(20, 0, 0, paramMessage);
    paramMessage = (IccRecords)mIccRecords.get();
    if (paramMessage != null) {
      paramMessage.setVoiceMailNumber(paramString1, mVmNumber, paramString2);
    }
  }
  
  public void setVoiceMessageWaiting(int paramInt1, int paramInt2)
  {
    if (isPhoneTypeGsm())
    {
      IccRecords localIccRecords = (IccRecords)mIccRecords.get();
      if (localIccRecords != null) {
        localIccRecords.setVoiceMessageWaiting(paramInt1, paramInt2);
      } else {
        logd("SIM Records not found, MWI not updated");
      }
    }
    else
    {
      setVoiceMessageCount(paramInt2);
    }
  }
  
  protected boolean shallDialOnCircuitSwitch(Bundle paramBundle)
  {
    boolean bool = true;
    if ((paramBundle == null) || (paramBundle.getInt("org.codeaurora.extra.CALL_DOMAIN", 0) != 1)) {
      bool = false;
    }
    return bool;
  }
  
  public boolean shouldForceAutoNetworkSelect()
  {
    int i = Phone.PREFERRED_NT_MODE;
    int j = getSubId();
    if (!SubscriptionManager.isValidSubscriptionId(j)) {
      return false;
    }
    ContentResolver localContentResolver = mContext.getContentResolver();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("preferred_network_mode");
    localStringBuilder.append(j);
    i = Settings.Global.getInt(localContentResolver, localStringBuilder.toString(), i);
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("shouldForceAutoNetworkSelect in mode = ");
    localStringBuilder.append(i);
    logd(localStringBuilder.toString());
    if ((isManualSelProhibitedInGlobalMode()) && ((i == 10) || (i == 7)))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Should force auto network select mode = ");
      localStringBuilder.append(i);
      logd(localStringBuilder.toString());
      return true;
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Should not force auto network select mode = ");
    localStringBuilder.append(i);
    logd(localStringBuilder.toString());
    return false;
  }
  
  public void startDtmf(char paramChar)
  {
    if (!PhoneNumberUtils.is12Key(paramChar))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("startDtmf called with invalid character '");
      localStringBuilder.append(paramChar);
      localStringBuilder.append("'");
      loge(localStringBuilder.toString());
    }
    else
    {
      mCi.startDtmf(paramChar, null);
    }
  }
  
  public void startNetworkScan(NetworkScanRequest paramNetworkScanRequest, Message paramMessage)
  {
    mCi.startNetworkScan(paramNetworkScanRequest, paramMessage);
  }
  
  public void stopDtmf()
  {
    mCi.stopDtmf(null);
  }
  
  public void stopNetworkScan(Message paramMessage)
  {
    mCi.stopNetworkScan(paramMessage);
  }
  
  public boolean supports3gppCallForwardingWhileRoaming()
  {
    PersistableBundle localPersistableBundle = ((CarrierConfigManager)getContext().getSystemService("carrier_config")).getConfig();
    if (localPersistableBundle != null) {
      return localPersistableBundle.getBoolean("support_3gpp_call_forwarding_while_roaming_bool", true);
    }
    return true;
  }
  
  public void switchHoldingAndActive()
    throws CallStateException
  {
    mCT.switchWaitingOrHoldingAndActive();
  }
  
  public void unregisterForCallWaiting(Handler paramHandler)
  {
    mCT.unregisterForCallWaiting(paramHandler);
  }
  
  public void unregisterForCdmaOtaStatusChange(Handler paramHandler)
  {
    mCi.unregisterForCdmaOtaProvision(paramHandler);
  }
  
  public void unregisterForEcmTimerReset(Handler paramHandler)
  {
    mEcmTimerResetRegistrants.remove(paramHandler);
  }
  
  public void unregisterForEriFileLoaded(Handler paramHandler)
  {
    mEriFileLoadedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSimRecordsLoaded(Handler paramHandler)
  {
    mSimRecordsLoadedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSubscriptionInfoReady(Handler paramHandler)
  {
    mSST.unregisterForSubscriptionInfoReady(paramHandler);
  }
  
  public void unregisterForSuppServiceNotification(Handler paramHandler)
  {
    mSsnRegistrants.remove(paramHandler);
    if (mSsnRegistrants.size() == 0) {
      mCi.setSuppServiceNotifications(false, null);
    }
  }
  
  public void unsetOnEcbModeExitResponse(Handler paramHandler)
  {
    mEcmExitRespRegistrant.clear();
  }
  
  public boolean updateCurrentCarrierInProvider()
  {
    long l = SubscriptionManager.getDefaultDataSubscriptionId();
    String str = getOperatorNumeric();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("updateCurrentCarrierInProvider: mSubId = ");
    ((StringBuilder)localObject).append(getSubId());
    ((StringBuilder)localObject).append(" currentDds = ");
    ((StringBuilder)localObject).append(l);
    ((StringBuilder)localObject).append(" operatorNumeric = ");
    ((StringBuilder)localObject).append(str);
    logd(((StringBuilder)localObject).toString());
    if ((!TextUtils.isEmpty(str)) && (getSubId() == l)) {
      try
      {
        localObject = Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "current");
        ContentValues localContentValues = new android/content/ContentValues;
        localContentValues.<init>();
        localContentValues.put("numeric", str);
        mContext.getContentResolver().insert((Uri)localObject, localContentValues);
        return true;
      }
      catch (SQLException localSQLException)
      {
        Rlog.e("GsmCdmaPhone", "Can't store current operator", localSQLException);
      }
    }
    return false;
  }
  
  public void updatePhoneObject(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("updatePhoneObject: radioTechnology=");
    localStringBuilder.append(paramInt);
    logd(localStringBuilder.toString());
    sendMessage(obtainMessage(42, paramInt, 0, null));
  }
  
  public void updateServiceLocation()
  {
    mSST.enableSingleLocationUpdate();
  }
  
  public void updateVoiceMail()
  {
    if (isPhoneTypeGsm())
    {
      int i = 0;
      Object localObject = (IccRecords)mIccRecords.get();
      if (localObject != null) {
        i = ((IccRecords)localObject).getVoiceMessageCount();
      }
      int j = i;
      if (i == -2) {
        j = getStoredVoiceMessageCount();
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("updateVoiceMail countVoiceMessages = ");
      ((StringBuilder)localObject).append(j);
      ((StringBuilder)localObject).append(" subId ");
      ((StringBuilder)localObject).append(getSubId());
      logd(((StringBuilder)localObject).toString());
      setVoiceMessageCount(j);
    }
    else
    {
      setVoiceMessageCount(getStoredVoiceMessageCount());
    }
  }
  
  private static class Cfu
  {
    final Message mOnComplete;
    final String mSetCfNumber;
    
    Cfu(String paramString, Message paramMessage)
    {
      mSetCfNumber = paramString;
      mOnComplete = paramMessage;
    }
  }
}
