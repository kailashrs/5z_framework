package com.android.internal.telephony;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.BaseBundle;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.provider.Telephony.ServiceStateTable;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.DataSpecificRegistrationStates;
import android.telephony.NetworkRegistrationState;
import android.telephony.PhysicalChannelConfig;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionManager.OnSubscriptionsChangedListener;
import android.telephony.TelephonyManager;
import android.telephony.VoiceSpecificRegistrationStates;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.LocalLog;
import android.util.Pair;
import android.util.SparseArray;
import android.util.StatsLog;
import android.util.TimeUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.cdma.CdmaSubscriptionSourceManager;
import com.android.internal.telephony.cdma.EriManager;
import com.android.internal.telephony.dataconnection.DcTracker;
import com.android.internal.telephony.dataconnection.TransportManager;
import com.android.internal.telephony.metrics.TelephonyMetrics;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppState;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.util.TimeStampedValue;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.PatternSyntaxException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ServiceStateTracker
  extends Handler
{
  private static final String ACTION_ASUS_CARRIER_LABEL = "com.asus.intent.action.AsusCarrierLabel";
  private static final String ACTION_RADIO_OFF = "android.intent.action.ACTION_RADIO_OFF";
  public static final int CS_DISABLED = 1004;
  public static final int CS_EMERGENCY_ENABLED = 1006;
  public static final int CS_ENABLED = 1003;
  public static final int CS_NORMAL_ENABLED = 1005;
  public static final int CS_NOTIFICATION = 999;
  public static final int CS_REJECT_CAUSE_ENABLED = 2001;
  public static final int CS_REJECT_CAUSE_NOTIFICATION = 111;
  static final boolean DBG = true;
  public static final int DEFAULT_GPRS_CHECK_PERIOD_MILLIS = 60000;
  public static final String DEFAULT_MNC = "00";
  protected static final int EVENT_ALL_DATA_DISCONNECTED = 49;
  protected static final int EVENT_CDMA_PRL_VERSION_CHANGED = 40;
  protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 39;
  protected static final int EVENT_CHANGE_IMS_STATE = 45;
  protected static final int EVENT_CHECK_REPORT_GPRS = 22;
  protected static final int EVENT_ERI_FILE_LOADED = 36;
  protected static final int EVENT_GET_CELL_INFO_LIST = 43;
  protected static final int EVENT_GET_LOC_DONE = 15;
  protected static final int EVENT_GET_PREFERRED_NETWORK_TYPE = 19;
  protected static final int EVENT_GET_SIGNAL_STRENGTH = 3;
  public static final int EVENT_ICC_CHANGED = 42;
  protected static final int EVENT_IMS_CAPABILITY_CHANGED = 48;
  protected static final int EVENT_IMS_SERVICE_STATE_CHANGED = 53;
  protected static final int EVENT_IMS_STATE_CHANGED = 46;
  protected static final int EVENT_IMS_STATE_DONE = 47;
  protected static final int EVENT_LOCATION_UPDATES_ENABLED = 18;
  protected static final int EVENT_NETWORK_STATE_CHANGED = 2;
  protected static final int EVENT_NITZ_TIME = 11;
  protected static final int EVENT_NV_READY = 35;
  protected static final int EVENT_OTA_PROVISION_STATUS_CHANGE = 37;
  protected static final int EVENT_PHONE_TYPE_SWITCHED = 50;
  protected static final int EVENT_PHYSICAL_CHANNEL_CONFIG = 55;
  protected static final int EVENT_POLL_SIGNAL_STRENGTH = 10;
  protected static final int EVENT_POLL_STATE_CDMA_SUBSCRIPTION = 34;
  protected static final int EVENT_POLL_STATE_GPRS = 5;
  protected static final int EVENT_POLL_STATE_NETWORK_SELECTION_MODE = 14;
  protected static final int EVENT_POLL_STATE_OPERATOR = 6;
  protected static final int EVENT_POLL_STATE_REGISTRATION = 4;
  protected static final int EVENT_RADIO_ON = 41;
  protected static final int EVENT_RADIO_POWER_FROM_CARRIER = 51;
  protected static final int EVENT_RADIO_POWER_OFF_DONE = 54;
  protected static final int EVENT_RADIO_STATE_CHANGED = 1;
  protected static final int EVENT_RESET_PREFERRED_NETWORK_TYPE = 21;
  protected static final int EVENT_RESTRICTED_STATE_CHANGED = 23;
  protected static final int EVENT_RUIM_READY = 26;
  protected static final int EVENT_RUIM_RECORDS_LOADED = 27;
  protected static final int EVENT_SET_PREFERRED_NETWORK_TYPE = 20;
  protected static final int EVENT_SET_RADIO_POWER_OFF = 38;
  protected static final int EVENT_SIGNAL_STRENGTH_UPDATE = 12;
  protected static final int EVENT_SIM_NOT_INSERTED = 52;
  protected static final int EVENT_SIM_READY = 17;
  protected static final int EVENT_SIM_RECORDS_LOADED = 16;
  protected static final int EVENT_UNSOL_CELL_INFO_LIST = 44;
  private static final int INVALID_LTE_EARFCN = -1;
  public static final String INVALID_MCC = "000";
  private static final long LAST_CELL_INFO_LIST_MAX_AGE_MS = 2000L;
  static final String LOG_TAG = "SST";
  private static final int MS_PER_HOUR = 3600000;
  private static final int POLL_PERIOD_MILLIS = 20000;
  private static final String PROP_FORCE_ROAMING = "telephony.test.forceRoaming";
  public static final int PS_DISABLED = 1002;
  public static final int PS_ENABLED = 1001;
  public static final int PS_NOTIFICATION = 888;
  protected static final String REGISTRATION_DENIED_AUTH = "Authentication Failure";
  protected static final String REGISTRATION_DENIED_GEN = "General";
  public static final String UNACTIVATED_MIN2_VALUE = "000000";
  public static final String UNACTIVATED_MIN_VALUE = "1111110111";
  private static final boolean VDBG = false;
  private boolean mAlarmSwitch = false;
  private final LocalLog mAttachLog;
  protected RegistrantList mAttachedRegistrants = new RegistrantList();
  private CarrierServiceStateTracker mCSST;
  private RegistrantList mCdmaForSubscriptionInfoReadyRegistrants;
  private CdmaSubscriptionSourceManager mCdmaSSM;
  public CellLocation mCellLoc;
  private CommandsInterface mCi;
  private final ContentResolver mCr;
  private String mCurDataSpn;
  private String mCurPlmn;
  private boolean mCurShowPlmn;
  private boolean mCurShowSpn;
  private String mCurSpn;
  private String mCurrentCarrier;
  private int mCurrentOtaspMode;
  private RegistrantList mDataRegStateOrRatChangedRegistrants = new RegistrantList();
  private boolean mDataRoaming;
  private RegistrantList mDataRoamingOffRegistrants = new RegistrantList();
  private RegistrantList mDataRoamingOnRegistrants = new RegistrantList();
  private int mDefaultRoamingIndicator;
  private boolean mDesiredPowerState;
  protected RegistrantList mDetachedRegistrants = new RegistrantList();
  private boolean mDeviceShuttingDown;
  private boolean mDontPollSignalStrength = false;
  private ArrayList<Pair<Integer, Integer>> mEarfcnPairListForRsrpBoost;
  private boolean mEmergencyOnly;
  private boolean mGsmRoaming;
  private final HandlerThread mHandlerThread;
  private HbpcdUtils mHbpcdUtils;
  private int[] mHomeNetworkId;
  private int[] mHomeSystemId;
  private IccRecords mIccRecords = null;
  private boolean mImsRegistered;
  private boolean mImsRegistrationOnOff = false;
  private BroadcastReceiver mIntentReceiver;
  private boolean mIsEriTextLoaded;
  private boolean mIsInPrl;
  private boolean mIsMinInfoReady;
  private boolean mIsSimReady;
  private boolean mIsSubscriptionFromRuim;
  private List<CellInfo> mLastCellInfoList = null;
  private long mLastCellInfoListTime;
  private List<PhysicalChannelConfig> mLastPhysicalChannelConfigList = null;
  private SignalStrength mLastSignalStrength;
  private final LocaleTracker mLocaleTracker;
  private int mLteRsrpBoost;
  private final Object mLteRsrpBoostLock;
  private int mMaxDataCalls;
  private String mMdn;
  private String mMin;
  private RegistrantList mNetworkAttachedRegistrants = new RegistrantList();
  private RegistrantList mNetworkDetachedRegistrants = new RegistrantList();
  private CellLocation mNewCellLoc;
  private int mNewMaxDataCalls;
  private int mNewReasonDataDenied;
  private int mNewRejectCode;
  private ServiceState mNewSS;
  private final NitzStateMachine mNitzState;
  private Notification mNotification;
  private final SstSubscriptionsChangedListener mOnSubscriptionsChangedListener;
  private boolean mPendingRadioPowerOffAfterDataOff = false;
  private int mPendingRadioPowerOffAfterDataOffTag = 0;
  protected final GsmCdmaPhone mPhone;
  private final LocalLog mPhoneTypeLog;
  @VisibleForTesting
  public int[] mPollingContext;
  private boolean mPowerOffDelayNeed;
  private int mPreferredNetworkType;
  private int mPrevSubId;
  private String mPrlVersion;
  private RegistrantList mPsRestrictDisabledRegistrants = new RegistrantList();
  private RegistrantList mPsRestrictEnabledRegistrants = new RegistrantList();
  private boolean mRadioDisabledByCarrier = false;
  private PendingIntent mRadioOffIntent = null;
  private final LocalLog mRadioPowerLog;
  private final LocalLog mRatLog;
  private final RatRatcheter mRatRatcheter;
  private int mReasonDataDenied;
  private final SparseArray<NetworkRegistrationManager> mRegStateManagers;
  private String mRegistrationDeniedReason;
  private int mRegistrationState;
  private int mRejectCode;
  private boolean mReportedGprsNoReg;
  public RestrictedState mRestrictedState;
  private RoamAliasAsus.RoamAliasItem[] mRoamAlias;
  private int mRoamingIndicator;
  private final LocalLog mRoamingLog;
  public ServiceState mSS;
  private SignalStrength mSignalStrength;
  private boolean mSpnUpdatePending;
  private boolean mStartedGprsRegCheck;
  @VisibleForTesting
  public int mSubId;
  private SubscriptionController mSubscriptionController;
  private SubscriptionManager mSubscriptionManager;
  private final TransportManager mTransportManager;
  private UiccCardApplication mUiccApplcation = null;
  private UiccController mUiccController = null;
  private boolean mVoiceCapable;
  private RegistrantList mVoiceRoamingOffRegistrants = new RegistrantList();
  private RegistrantList mVoiceRoamingOnRegistrants = new RegistrantList();
  private boolean mWantContinuousLocationUpdates;
  private boolean mWantSingleLocationUpdate;
  private int radio_off_count;
  
  public ServiceStateTracker(GsmCdmaPhone paramGsmCdmaPhone, CommandsInterface paramCommandsInterface)
  {
    boolean bool = true;
    mPowerOffDelayNeed = true;
    mDeviceShuttingDown = false;
    mSpnUpdatePending = false;
    mCurSpn = null;
    mCurDataSpn = null;
    mCurPlmn = null;
    mCurShowPlmn = false;
    mCurShowSpn = false;
    mSubId = -1;
    mPrevSubId = -1;
    mImsRegistered = false;
    mOnSubscriptionsChangedListener = new SstSubscriptionsChangedListener(null);
    mRoamingLog = new LocalLog(10);
    mAttachLog = new LocalLog(10);
    mPhoneTypeLog = new LocalLog(10);
    mRatLog = new LocalLog(20);
    mRadioPowerLog = new LocalLog(20);
    mRoamAlias = null;
    mMaxDataCalls = 1;
    mNewMaxDataCalls = 1;
    mReasonDataDenied = -1;
    mNewReasonDataDenied = -1;
    mGsmRoaming = false;
    mDataRoaming = false;
    mEmergencyOnly = false;
    mIsSimReady = false;
    mIntentReceiver = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if (paramAnonymousIntent.getAction().equals("android.telephony.action.CARRIER_CONFIG_CHANGED"))
        {
          ServiceStateTracker.this.onCarrierConfigChanged();
          return;
        }
        if (!mPhone.isPhoneTypeGsm())
        {
          paramAnonymousContext = ServiceStateTracker.this;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Ignoring intent ");
          localStringBuilder.append(paramAnonymousIntent);
          localStringBuilder.append(" received on CDMA phone");
          paramAnonymousContext.loge(localStringBuilder.toString());
          return;
        }
        if (paramAnonymousIntent.getAction().equals("android.intent.action.LOCALE_CHANGED"))
        {
          updateSpnDisplay();
          pollState();
        }
        else if (paramAnonymousIntent.getAction().equals("android.intent.action.ACTION_RADIO_OFF"))
        {
          ServiceStateTracker.access$902(ServiceStateTracker.this, false);
          paramAnonymousContext = mPhone.mDcTracker;
          powerOffRadioSafely(paramAnonymousContext);
        }
        else if (paramAnonymousIntent.getAction().equals("com.asus.intent.action.AsusCarrierLabel"))
        {
          log("[ASIM] force updateSpnDisplay...");
          updateSpnDisplay(true);
        }
      }
    };
    mCurrentOtaspMode = 0;
    mRegistrationState = -1;
    mCdmaForSubscriptionInfoReadyRegistrants = new RegistrantList();
    mHomeSystemId = null;
    mHomeNetworkId = null;
    mIsMinInfoReady = false;
    mIsEriTextLoaded = false;
    mIsSubscriptionFromRuim = false;
    mHbpcdUtils = null;
    mCurrentCarrier = null;
    mRegStateManagers = new SparseArray();
    mEarfcnPairListForRsrpBoost = null;
    mLteRsrpBoost = 0;
    mLteRsrpBoostLock = new Object();
    radio_off_count = 0;
    mLastSignalStrength = null;
    mNitzState = TelephonyComponentFactory.getInstance().makeNitzStateMachine(paramGsmCdmaPhone);
    mPhone = paramGsmCdmaPhone;
    mCi = paramCommandsInterface;
    mRatRatcheter = new RatRatcheter(mPhone);
    mVoiceCapable = mPhone.getContext().getResources().getBoolean(17957076);
    mUiccController = UiccController.getInstance();
    mUiccController.registerForIccChanged(this, 42, null);
    mCi.setOnSignalStrengthUpdate(this, 12, null);
    mCi.registerForCellInfoList(this, 44, null);
    mCi.registerForPhysicalChannelConfiguration(this, 55, null);
    mSubscriptionController = SubscriptionController.getInstance();
    mSubscriptionManager = SubscriptionManager.from(paramGsmCdmaPhone.getContext());
    mSubscriptionManager.addOnSubscriptionsChangedListener(mOnSubscriptionsChangedListener);
    mRestrictedState = new RestrictedState();
    mTransportManager = new TransportManager();
    paramCommandsInterface = mTransportManager.getAvailableTransports().iterator();
    while (paramCommandsInterface.hasNext())
    {
      i = ((Integer)paramCommandsInterface.next()).intValue();
      mRegStateManagers.append(i, new NetworkRegistrationManager(i, paramGsmCdmaPhone));
      ((NetworkRegistrationManager)mRegStateManagers.get(i)).registerForNetworkRegistrationStateChanged(this, 2, null);
    }
    mHandlerThread = new HandlerThread(LocaleTracker.class.getSimpleName());
    mHandlerThread.start();
    mLocaleTracker = TelephonyComponentFactory.getInstance().makeLocaleTracker(mPhone, mHandlerThread.getLooper());
    mCi.registerForImsNetworkStateChanged(this, 46, null);
    mCi.registerForRadioStateChanged(this, 1, null);
    mCi.setOnNITZTime(this, 11, null);
    mCr = paramGsmCdmaPhone.getContext().getContentResolver();
    int i = Settings.Global.getInt(mCr, "airplane_mode_on", 0);
    int j = Settings.Global.getInt(mCr, "enable_cellular_on_boot", 1);
    if ((j <= 0) || (i > 0)) {
      bool = false;
    }
    mDesiredPowerState = bool;
    paramCommandsInterface = mRadioPowerLog;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("init : airplane mode = ");
    ((StringBuilder)localObject).append(i);
    ((StringBuilder)localObject).append(" enableCellularOnBoot = ");
    ((StringBuilder)localObject).append(j);
    paramCommandsInterface.log(((StringBuilder)localObject).toString());
    setSignalStrengthDefaultValues();
    mPhone.getCarrierActionAgent().registerForCarrierAction(1, this, 51, null, false);
    paramCommandsInterface = mPhone.getContext();
    localObject = new IntentFilter();
    ((IntentFilter)localObject).addAction("android.intent.action.LOCALE_CHANGED");
    paramCommandsInterface.registerReceiver(mIntentReceiver, (IntentFilter)localObject);
    localObject = new IntentFilter();
    ((IntentFilter)localObject).addAction("android.intent.action.ACTION_RADIO_OFF");
    ((IntentFilter)localObject).addAction("com.asus.intent.action.AsusCarrierLabel");
    paramCommandsInterface.registerReceiver(mIntentReceiver, (IntentFilter)localObject);
    localObject = new IntentFilter();
    ((IntentFilter)localObject).addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
    paramCommandsInterface.registerReceiver(mIntentReceiver, (IntentFilter)localObject);
    mPhone.notifyOtaspChanged(0);
    mCi.setOnRestrictedStateChanged(this, 23, null);
    updatePhoneType();
    mCSST = new CarrierServiceStateTracker(paramGsmCdmaPhone, this);
    registerForNetworkAttached(mCSST, 101, null);
    registerForNetworkDetached(mCSST, 102, null);
    registerForDataConnectionAttached(mCSST, 103, null);
    registerForDataConnectionDetached(mCSST, 104, null);
  }
  
  private void cancelAllNotifications()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("cancelAllNotifications: mPrevSubId=");
    ((StringBuilder)localObject).append(mPrevSubId);
    log(((StringBuilder)localObject).toString());
    localObject = (NotificationManager)mPhone.getContext().getSystemService("notification");
    if (SubscriptionManager.isValidSubscriptionId(mPrevSubId))
    {
      ((NotificationManager)localObject).cancel(Integer.toString(mPrevSubId), 888);
      ((NotificationManager)localObject).cancel(Integer.toString(mPrevSubId), 999);
      ((NotificationManager)localObject).cancel(Integer.toString(mPrevSubId), 111);
    }
  }
  
  private boolean containsEarfcnInEarfcnRange(ArrayList<Pair<Integer, Integer>> paramArrayList, int paramInt)
  {
    if (paramArrayList != null)
    {
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        paramArrayList = (Pair)localIterator.next();
        if ((paramInt >= ((Integer)first).intValue()) && (paramInt <= ((Integer)second).intValue())) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean currentMccEqualsSimMcc(ServiceState paramServiceState)
  {
    String str = ((TelephonyManager)mPhone.getContext().getSystemService("phone")).getSimOperatorNumericForPhone(getPhoneId());
    paramServiceState = paramServiceState.getOperatorNumeric();
    boolean bool1 = true;
    boolean bool2;
    try
    {
      bool2 = str.substring(0, 3).equals(paramServiceState.substring(0, 3));
    }
    catch (Exception paramServiceState)
    {
      bool2 = bool1;
    }
    return bool2;
  }
  
  private void dumpCellInfoList(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(" mLastCellInfoList={");
    if (mLastCellInfoList != null)
    {
      int i = 1;
      Iterator localIterator = mLastCellInfoList.iterator();
      while (localIterator.hasNext())
      {
        CellInfo localCellInfo = (CellInfo)localIterator.next();
        if (i == 0) {
          paramPrintWriter.print(",");
        }
        i = 0;
        paramPrintWriter.print(localCellInfo.toString());
      }
    }
    paramPrintWriter.println("}");
  }
  
  private void dumpEarfcnPairList(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.print(" mEarfcnPairListForRsrpBoost={");
    if (mEarfcnPairListForRsrpBoost != null)
    {
      int i = mEarfcnPairListForRsrpBoost.size();
      Iterator localIterator = mEarfcnPairListForRsrpBoost.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        paramPrintWriter.print("(");
        paramPrintWriter.print(first);
        paramPrintWriter.print(",");
        paramPrintWriter.print(second);
        paramPrintWriter.print(")");
        i--;
        if (i != 0) {
          paramPrintWriter.print(",");
        }
      }
    }
    paramPrintWriter.println("}");
  }
  
  private String fixUnknownMcc(String paramString, int paramInt)
  {
    if (paramInt <= 0) {
      return paramString;
    }
    Object localObject1 = mNitzState.getSavedTimeZoneId();
    int i = 0;
    boolean bool;
    if (localObject1 != null)
    {
      localObject1 = TimeZone.getTimeZone(mNitzState.getSavedTimeZoneId());
      bool = true;
    }
    else
    {
      localObject1 = mNitzState.getCachedNitzData();
      if (localObject1 == null)
      {
        localObject1 = null;
      }
      else
      {
        localObject1 = TimeZoneLookupHelper.guessZoneByNitzStatic((NitzData)localObject1);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("fixUnknownMcc(): guessNitzTimeZone returned ");
        Object localObject2;
        if (localObject1 == null) {
          localObject2 = localObject1;
        } else {
          localObject2 = ((TimeZone)localObject1).getID();
        }
        localStringBuilder.append(localObject2);
        log(localStringBuilder.toString());
      }
      bool = false;
    }
    int j = 0;
    if (localObject1 != null) {
      j = ((TimeZone)localObject1).getRawOffset() / 3600000;
    }
    localObject1 = mNitzState.getCachedNitzData();
    int k;
    if ((localObject1 != null) && (((NitzData)localObject1).isDst())) {
      k = 1;
    } else {
      k = 0;
    }
    localObject1 = mHbpcdUtils;
    if (k != 0) {
      i = 1;
    }
    paramInt = ((HbpcdUtils)localObject1).getMcc(paramInt, j, i, bool);
    if (paramInt > 0)
    {
      paramString = new StringBuilder();
      paramString.append(Integer.toString(paramInt));
      paramString.append("00");
      paramString = paramString.toString();
    }
    return paramString;
  }
  
  private int[] getBandwidthsFromConfigs(List<PhysicalChannelConfig> paramList)
  {
    return paramList.stream().map(_..Lambda.ServiceStateTracker.WWHOcG5P4_jgjzPPgLwm_wN15OM.INSTANCE).mapToInt(_..Lambda.ServiceStateTracker.UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
  }
  
  private PersistableBundle getCarrierConfig()
  {
    Object localObject = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    if (localObject != null)
    {
      localObject = ((CarrierConfigManager)localObject).getConfigForSubId(mPhone.getSubId());
      if (localObject != null) {
        return localObject;
      }
    }
    return CarrierConfigManager.getDefaultConfig();
  }
  
  private int getLteEarfcn(CellIdentity paramCellIdentity)
  {
    int i = -1;
    int j = i;
    if (paramCellIdentity != null) {
      if (paramCellIdentity.getType() != 3) {
        j = i;
      } else {
        j = ((CellIdentityLte)paramCellIdentity).getEarfcn();
      }
    }
    return j;
  }
  
  private void getSubscriptionInfoAndStartPollingThreads()
  {
    mCi.getCDMASubscription(obtainMessage(34));
    pollState();
  }
  
  private UiccCardApplication getUiccCardApplication()
  {
    if (mPhone.isPhoneTypeGsm()) {
      return mUiccController.getUiccCardApplication(mPhone.getPhoneId(), 1);
    }
    return mUiccController.getUiccCardApplication(mPhone.getPhoneId(), 2);
  }
  
  private String getVowifiOverride()
  {
    String str = null;
    Object localObject1 = null;
    StringBuilder localStringBuilder = null;
    Object localObject2 = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    if (localObject2 != null)
    {
      localObject1 = str;
      try
      {
        localObject2 = ((CarrierConfigManager)localObject2).getConfigForSubId(mPhone.getSubId());
        localObject1 = localStringBuilder;
        if (localObject2 != null)
        {
          localObject1 = str;
          str = ((PersistableBundle)localObject2).getString("vowifi_name_override");
          localObject1 = str;
          localStringBuilder = new java/lang/StringBuilder;
          localObject1 = str;
          localStringBuilder.<init>();
          localObject1 = str;
          localStringBuilder.append("[APLMN][getVowifiOverride] override =");
          localObject1 = str;
          localStringBuilder.append(str);
          localObject1 = str;
          log(localStringBuilder.toString());
          localObject1 = str;
        }
      }
      catch (Exception localException)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("getVowifiOverride: carrier config error: ");
        localStringBuilder.append(localException);
        loge(localStringBuilder.toString());
      }
    }
    return localObject1;
  }
  
  private void handleCdmaSubscriptionSource(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Subscription Source : ");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    }
    mIsSubscriptionFromRuim = bool;
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("isFromRuim: ");
    localStringBuilder.append(mIsSubscriptionFromRuim);
    log(localStringBuilder.toString());
    saveCdmaSubscriptionSource(paramInt);
    if (!mIsSubscriptionFromRuim) {
      sendMessage(obtainMessage(35));
    }
  }
  
  private boolean iccCardExists()
  {
    boolean bool = false;
    if (mUiccApplcation != null) {
      if (mUiccApplcation.getState() != IccCardApplicationStatus.AppState.APPSTATE_UNKNOWN) {
        bool = true;
      } else {
        bool = false;
      }
    }
    return bool;
  }
  
  private boolean isGprsConsistent(int paramInt1, int paramInt2)
  {
    boolean bool;
    if ((paramInt2 == 0) && (paramInt1 != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isHomeSid(int paramInt)
  {
    if (mHomeSystemId != null) {
      for (int i = 0; i < mHomeSystemId.length; i++) {
        if (paramInt == mHomeSystemId[i]) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean isInHomeSidNid(int paramInt1, int paramInt2)
  {
    if (isSidsAllZeros()) {
      return true;
    }
    if (mHomeSystemId.length != mHomeNetworkId.length) {
      return true;
    }
    if (paramInt1 == 0) {
      return true;
    }
    for (int i = 0; i < mHomeSystemId.length; i++) {
      if ((mHomeSystemId[i] == paramInt1) && ((mHomeNetworkId[i] == 0) || (mHomeNetworkId[i] == 65535) || (paramInt2 == 0) || (paramInt2 == 65535) || (mHomeNetworkId[i] == paramInt2))) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isInNetwork(BaseBundle paramBaseBundle, String paramString1, String paramString2)
  {
    paramBaseBundle = paramBaseBundle.getStringArray(paramString2);
    return (paramBaseBundle != null) && (Arrays.asList(paramBaseBundle).contains(paramString1));
  }
  
  private boolean isInvalidOperatorNumeric(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (paramString.length() >= 5) && (!paramString.startsWith("000"))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isOperatorConsideredNonRoaming(ServiceState paramServiceState)
  {
    String str = paramServiceState.getOperatorNumeric();
    Object localObject = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    CharSequence localCharSequence = null;
    paramServiceState = localCharSequence;
    if (localObject != null)
    {
      localObject = ((CarrierConfigManager)localObject).getConfigForSubId(mPhone.getSubId());
      paramServiceState = localCharSequence;
      if (localObject != null) {
        paramServiceState = ((PersistableBundle)localObject).getStringArray("non_roaming_operator_string_array");
      }
    }
    if ((!ArrayUtils.isEmpty(paramServiceState)) && (str != null))
    {
      int i = paramServiceState.length;
      for (int j = 0; j < i; j++)
      {
        localCharSequence = paramServiceState[j];
        if ((!TextUtils.isEmpty(localCharSequence)) && (str.startsWith(localCharSequence))) {
          return true;
        }
      }
      return false;
    }
    return false;
  }
  
  private boolean isOperatorConsideredRoaming(ServiceState paramServiceState)
  {
    String str = paramServiceState.getOperatorNumeric();
    Object localObject = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    CharSequence localCharSequence = null;
    paramServiceState = localCharSequence;
    if (localObject != null)
    {
      localObject = ((CarrierConfigManager)localObject).getConfigForSubId(mPhone.getSubId());
      paramServiceState = localCharSequence;
      if (localObject != null) {
        paramServiceState = ((PersistableBundle)localObject).getStringArray("roaming_operator_string_array");
      }
    }
    if ((!ArrayUtils.isEmpty(paramServiceState)) && (str != null))
    {
      int i = paramServiceState.length;
      for (int j = 0; j < i; j++)
      {
        localCharSequence = paramServiceState[j];
        if ((!TextUtils.isEmpty(localCharSequence)) && (str.startsWith(localCharSequence))) {
          return true;
        }
      }
      return false;
    }
    return false;
  }
  
  private boolean isRoamIndForHomeSystem(String paramString)
  {
    Object localObject = Resources.getSystem().getStringArray(17235996);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("isRoamIndForHomeSystem: homeRoamIndicators=");
    localStringBuilder.append(Arrays.toString((Object[])localObject));
    log(localStringBuilder.toString());
    if (localObject != null)
    {
      int i = localObject.length;
      for (int j = 0; j < i; j++) {
        if (localObject[j].equals(paramString)) {
          return true;
        }
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("isRoamIndForHomeSystem: No match found against list for roamInd=");
      ((StringBuilder)localObject).append(paramString);
      log(((StringBuilder)localObject).toString());
      return false;
    }
    log("isRoamIndForHomeSystem: No list found");
    return false;
  }
  
  private boolean isRoamingBetweenOperators(boolean paramBoolean, ServiceState paramServiceState)
  {
    if ((paramBoolean) && (!isSameOperatorNameFromSimAndSS(paramServiceState))) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    return paramBoolean;
  }
  
  private boolean isSameNamedOperators(ServiceState paramServiceState)
  {
    boolean bool;
    if ((currentMccEqualsSimMcc(paramServiceState)) && (isSameOperatorNameFromSimAndSS(paramServiceState))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isSameOperatorNameFromSimAndSS(ServiceState paramServiceState)
  {
    String str1 = ((TelephonyManager)mPhone.getContext().getSystemService("phone")).getSimOperatorNameForPhone(getPhoneId());
    String str2 = paramServiceState.getOperatorAlphaLong();
    paramServiceState = paramServiceState.getOperatorAlphaShort();
    boolean bool1 = TextUtils.isEmpty(str1);
    boolean bool2 = false;
    int i;
    if ((!bool1) && (str1.equalsIgnoreCase(str2))) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if ((!TextUtils.isEmpty(str1)) && (str1.equalsIgnoreCase(paramServiceState))) {
      j = 1;
    } else {
      j = 0;
    }
    if ((i == 0) && (j == 0)) {
      break label108;
    }
    bool2 = true;
    label108:
    return bool2;
  }
  
  private static boolean isValidLteBandwidthKhz(int paramInt)
  {
    return (paramInt == 1400) || (paramInt == 3000) || (paramInt == 5000) || (paramInt == 10000) || (paramInt == 15000) || (paramInt == 20000);
  }
  
  private void logAttachChange()
  {
    mAttachLog.log(mSS.toString());
  }
  
  private void logPhoneTypeChange()
  {
    mPhoneTypeLog.log(Integer.toString(mPhone.getPhoneType()));
  }
  
  private void logRatChange()
  {
    mRatLog.log(mSS.toString());
  }
  
  private void logRoamingChange()
  {
    mRoamingLog.log(mSS.toString());
  }
  
  private void modemTriggeredPollState()
  {
    pollState(true);
  }
  
  private boolean networkCountryIsoChanged(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1))
    {
      log("countryIsoChanged: no new country ISO code");
      return false;
    }
    if (TextUtils.isEmpty(paramString2))
    {
      log("countryIsoChanged: no previous country ISO code");
      return true;
    }
    return paramString1.equals(paramString2) ^ true;
  }
  
  private void notifyCdmaSubscriptionInfoReady()
  {
    if (mCdmaForSubscriptionInfoReadyRegistrants != null)
    {
      log("CDMA_SUBSCRIPTION: call notifyRegistrants()");
      mCdmaForSubscriptionInfoReadyRegistrants.notifyRegistrants();
    }
  }
  
  private void onCarrierConfigChanged()
  {
    PersistableBundle localPersistableBundle = ((CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config")).getConfigForSubId(mPhone.getSubId());
    if (localPersistableBundle != null)
    {
      updateLteEarfcnLists(localPersistableBundle);
      updateReportingCriteria(localPersistableBundle);
    }
  }
  
  private void onRestrictedStateChanged(AsyncResult paramAsyncResult)
  {
    RestrictedState localRestrictedState = new RestrictedState();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onRestrictedStateChanged: E rs ");
    localStringBuilder.append(mRestrictedState);
    log(localStringBuilder.toString());
    if ((exception == null) && (result != null))
    {
      int i = ((Integer)result).intValue();
      boolean bool1 = false;
      boolean bool2;
      if (((i & 0x1) == 0) && ((i & 0x4) == 0)) {
        bool2 = false;
      } else {
        bool2 = true;
      }
      localRestrictedState.setCsEmergencyRestricted(bool2);
      if ((mUiccApplcation != null) && (mUiccApplcation.getState() == IccCardApplicationStatus.AppState.APPSTATE_READY))
      {
        if (((i & 0x2) == 0) && ((i & 0x4) == 0)) {
          bool2 = false;
        } else {
          bool2 = true;
        }
        localRestrictedState.setCsNormalRestricted(bool2);
        bool2 = bool1;
        if ((i & 0x10) != 0) {
          bool2 = true;
        }
        localRestrictedState.setPsRestricted(bool2);
      }
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("onRestrictedStateChanged: new rs ");
      paramAsyncResult.append(localRestrictedState);
      log(paramAsyncResult.toString());
      if ((!mRestrictedState.isPsRestricted()) && (localRestrictedState.isPsRestricted()))
      {
        mPsRestrictEnabledRegistrants.notifyRegistrants();
        setNotification(1001);
      }
      else if ((mRestrictedState.isPsRestricted()) && (!localRestrictedState.isPsRestricted()))
      {
        mPsRestrictDisabledRegistrants.notifyRegistrants();
        setNotification(1002);
      }
      if (mRestrictedState.isCsRestricted())
      {
        if (!localRestrictedState.isAnyCsRestricted()) {
          setNotification(1004);
        } else if (!localRestrictedState.isCsNormalRestricted()) {
          setNotification(1006);
        } else if (!localRestrictedState.isCsEmergencyRestricted()) {
          setNotification(1005);
        }
      }
      else if ((mRestrictedState.isCsEmergencyRestricted()) && (!mRestrictedState.isCsNormalRestricted()))
      {
        if (!localRestrictedState.isAnyCsRestricted()) {
          setNotification(1004);
        } else if (localRestrictedState.isCsRestricted()) {
          setNotification(1003);
        } else if (localRestrictedState.isCsNormalRestricted()) {
          setNotification(1005);
        }
      }
      else if ((!mRestrictedState.isCsEmergencyRestricted()) && (mRestrictedState.isCsNormalRestricted()))
      {
        if (!localRestrictedState.isAnyCsRestricted()) {
          setNotification(1004);
        } else if (localRestrictedState.isCsRestricted()) {
          setNotification(1003);
        } else if (localRestrictedState.isCsEmergencyRestricted()) {
          setNotification(1006);
        }
      }
      else if (localRestrictedState.isCsRestricted()) {
        setNotification(1003);
      } else if (localRestrictedState.isCsEmergencyRestricted()) {
        setNotification(1006);
      } else if (localRestrictedState.isCsNormalRestricted()) {
        setNotification(1005);
      }
      mRestrictedState = localRestrictedState;
    }
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("onRestrictedStateChanged: X rs ");
    paramAsyncResult.append(mRestrictedState);
    log(paramAsyncResult.toString());
  }
  
  private void overrideOperatorName()
  {
    int i = getCombinedRegState();
    if ((mPhone.getImsPhone() != null) && (mPhone.getImsPhone().isWifiCallingEnabled()) && (i == 0))
    {
      String str = getVowifiOverride();
      if (!TextUtils.isEmpty(str))
      {
        mNewSS.setOperatorAlphaLong(str);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("[ABSP]overrideOperatorName: change alphaName: ");
        localStringBuilder.append(str);
        loge(localStringBuilder.toString());
      }
    }
  }
  
  private void pollStateDone()
  {
    if (!mPhone.isPhoneTypeGsm()) {
      updateRoamingState();
    }
    if ((Build.IS_DEBUGGABLE) && (SystemProperties.getBoolean("telephony.test.forceRoaming", false)))
    {
      mNewSS.setVoiceRoaming(true);
      mNewSS.setDataRoaming(true);
    }
    useDataRegStateForDataOnlyDevices();
    resetServiceStateInIwlanMode();
    if ((Build.IS_DEBUGGABLE) && (mPhone.mTelephonyTester != null)) {
      mPhone.mTelephonyTester.overrideServiceState(mNewSS);
    }
    overrideOperatorName();
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Poll ServiceState done:  oldSS=[");
    ((StringBuilder)localObject1).append(mSS);
    ((StringBuilder)localObject1).append("] newSS=[");
    ((StringBuilder)localObject1).append(mNewSS);
    ((StringBuilder)localObject1).append("] oldMaxDataCalls=");
    ((StringBuilder)localObject1).append(mMaxDataCalls);
    ((StringBuilder)localObject1).append(" mNewMaxDataCalls=");
    ((StringBuilder)localObject1).append(mNewMaxDataCalls);
    ((StringBuilder)localObject1).append(" oldReasonDataDenied=");
    ((StringBuilder)localObject1).append(mReasonDataDenied);
    ((StringBuilder)localObject1).append(" mNewReasonDataDenied=");
    ((StringBuilder)localObject1).append(mNewReasonDataDenied);
    log(((StringBuilder)localObject1).toString());
    boolean bool1;
    if ((mSS.getVoiceRegState() != 0) && (mNewSS.getVoiceRegState() == 0)) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2;
    if ((mSS.getVoiceRegState() == 0) && (mNewSS.getVoiceRegState() != 0)) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3;
    if ((mSS.getDataRegState() != 0) && (mNewSS.getDataRegState() == 0)) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    boolean bool4;
    if ((mSS.getDataRegState() == 0) && (mNewSS.getDataRegState() != 0)) {
      bool4 = true;
    } else {
      bool4 = false;
    }
    boolean bool5;
    if (mSS.getDataRegState() != mNewSS.getDataRegState()) {
      bool5 = true;
    } else {
      bool5 = false;
    }
    int i;
    if (mSS.getVoiceRegState() != mNewSS.getVoiceRegState()) {
      i = 1;
    } else {
      i = 0;
    }
    boolean bool6 = mNewCellLoc.equals(mCellLoc) ^ true;
    int j;
    if (mNewSS.getDataRegState() == 0) {
      j = 1;
    } else {
      j = 0;
    }
    if (j != 0) {
      mRatRatcheter.ratchet(mSS, mNewSS, bool6);
    }
    boolean bool7;
    if (mSS.getRilVoiceRadioTechnology() != mNewSS.getRilVoiceRadioTechnology()) {
      bool7 = true;
    } else {
      bool7 = false;
    }
    boolean bool8;
    if (mSS.getRilDataRadioTechnology() != mNewSS.getRilDataRadioTechnology()) {
      bool8 = true;
    } else {
      bool8 = false;
    }
    boolean bool9 = mNewSS.equals(mSS) ^ true;
    boolean bool10;
    if ((!mSS.getVoiceRoaming()) && (mNewSS.getVoiceRoaming())) {
      bool10 = true;
    } else {
      bool10 = false;
    }
    boolean bool11;
    if ((mSS.getVoiceRoaming()) && (!mNewSS.getVoiceRoaming())) {
      bool11 = true;
    } else {
      bool11 = false;
    }
    boolean bool12;
    if ((!mSS.getDataRoaming()) && (mNewSS.getDataRoaming())) {
      bool12 = true;
    } else {
      bool12 = false;
    }
    boolean bool13;
    if ((mSS.getDataRoaming()) && (!mNewSS.getDataRoaming())) {
      bool13 = true;
    } else {
      bool13 = false;
    }
    if (mRejectCode != mNewRejectCode) {
      j = 1;
    } else {
      j = 0;
    }
    boolean bool14;
    if (mSS.getCssIndicator() != mNewSS.getCssIndicator()) {
      bool14 = true;
    } else {
      bool14 = false;
    }
    boolean bool15;
    label792:
    boolean bool17;
    if (mPhone.isPhoneTypeCdmaLte())
    {
      if ((mNewSS.getDataRegState() == 0) && (((ServiceState.isLte(mSS.getRilDataRadioTechnology())) && (mNewSS.getRilDataRadioTechnology() == 13)) || ((mSS.getRilDataRadioTechnology() == 13) && (ServiceState.isLte(mNewSS.getRilDataRadioTechnology()))))) {
        bool15 = true;
      } else {
        bool15 = false;
      }
      if (!ServiceState.isLte(mNewSS.getRilDataRadioTechnology())) {
        if (mNewSS.getRilDataRadioTechnology() != 13) {
          break label792;
        }
      }
      if ((!ServiceState.isLte(mSS.getRilDataRadioTechnology())) && (mSS.getRilDataRadioTechnology() != 13)) {
        bool16 = true;
      } else {
        bool16 = false;
      }
      if ((mNewSS.getRilDataRadioTechnology() >= 4) && (mNewSS.getRilDataRadioTechnology() <= 8)) {
        bool17 = true;
      } else {
        bool17 = false;
      }
    }
    else
    {
      bool16 = false;
      bool17 = false;
      bool15 = false;
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("pollStateDone: hasRegistered=");
    ((StringBuilder)localObject1).append(bool1);
    ((StringBuilder)localObject1).append(" hasDeregistered=");
    ((StringBuilder)localObject1).append(bool2);
    ((StringBuilder)localObject1).append(" hasDataAttached=");
    ((StringBuilder)localObject1).append(bool3);
    ((StringBuilder)localObject1).append(" hasDataDetached=");
    ((StringBuilder)localObject1).append(bool4);
    ((StringBuilder)localObject1).append(" hasDataRegStateChanged=");
    ((StringBuilder)localObject1).append(bool5);
    ((StringBuilder)localObject1).append(" hasRilVoiceRadioTechnologyChanged= ");
    ((StringBuilder)localObject1).append(bool7);
    ((StringBuilder)localObject1).append(" hasRilDataRadioTechnologyChanged=");
    ((StringBuilder)localObject1).append(bool8);
    ((StringBuilder)localObject1).append(" hasChanged=");
    ((StringBuilder)localObject1).append(bool9);
    ((StringBuilder)localObject1).append(" hasVoiceRoamingOn=");
    ((StringBuilder)localObject1).append(bool10);
    ((StringBuilder)localObject1).append(" hasVoiceRoamingOff=");
    ((StringBuilder)localObject1).append(bool11);
    ((StringBuilder)localObject1).append(" hasDataRoamingOn=");
    ((StringBuilder)localObject1).append(bool12);
    ((StringBuilder)localObject1).append(" hasDataRoamingOff=");
    ((StringBuilder)localObject1).append(bool13);
    ((StringBuilder)localObject1).append(" hasLocationChanged=");
    ((StringBuilder)localObject1).append(bool6);
    ((StringBuilder)localObject1).append(" has4gHandoff = ");
    ((StringBuilder)localObject1).append(bool15);
    ((StringBuilder)localObject1).append(" hasMultiApnSupport=");
    ((StringBuilder)localObject1).append(bool16);
    ((StringBuilder)localObject1).append(" hasLostMultiApnSupport=");
    ((StringBuilder)localObject1).append(bool17);
    ((StringBuilder)localObject1).append(" hasCssIndicatorChanged=");
    ((StringBuilder)localObject1).append(bool14);
    log(((StringBuilder)localObject1).toString());
    if ((i == 0) && (!bool5)) {
      break label1209;
    }
    if (mPhone.isPhoneTypeGsm()) {
      i = 50114;
    } else {
      i = 50116;
    }
    EventLog.writeEvent(i, new Object[] { Integer.valueOf(mSS.getVoiceRegState()), Integer.valueOf(mSS.getDataRegState()), Integer.valueOf(mNewSS.getVoiceRegState()), Integer.valueOf(mNewSS.getDataRegState()) });
    label1209:
    if (mPhone.isPhoneTypeGsm())
    {
      if (bool7)
      {
        i = -1;
        localObject1 = (GsmCellLocation)mNewCellLoc;
        if (localObject1 != null) {
          i = ((GsmCellLocation)localObject1).getCid();
        }
        EventLog.writeEvent(50123, new Object[] { Integer.valueOf(i), Integer.valueOf(mSS.getRilVoiceRadioTechnology()), Integer.valueOf(mNewSS.getRilVoiceRadioTechnology()) });
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("RAT switched ");
        ((StringBuilder)localObject1).append(ServiceState.rilRadioTechnologyToString(mSS.getRilVoiceRadioTechnology()));
        ((StringBuilder)localObject1).append(" -> ");
        ((StringBuilder)localObject1).append(ServiceState.rilRadioTechnologyToString(mNewSS.getRilVoiceRadioTechnology()));
        ((StringBuilder)localObject1).append(" at cell ");
        ((StringBuilder)localObject1).append(i);
        log(((StringBuilder)localObject1).toString());
      }
      if (bool14) {
        mPhone.notifyDataConnection("cssIndicatorChanged");
      }
      mReasonDataDenied = mNewReasonDataDenied;
      mMaxDataCalls = mNewMaxDataCalls;
      mRejectCode = mNewRejectCode;
    }
    ServiceState localServiceState = mPhone.getServiceState();
    localObject1 = mSS;
    mSS = mNewSS;
    mNewSS = ((ServiceState)localObject1);
    mNewSS.setStateOutOfService();
    localObject1 = mCellLoc;
    mCellLoc = mNewCellLoc;
    mNewCellLoc = ((CellLocation)localObject1);
    if (bool7) {
      updatePhoneObject();
    }
    TelephonyManager localTelephonyManager = (TelephonyManager)mPhone.getContext().getSystemService("phone");
    if (bool8)
    {
      localTelephonyManager.setDataNetworkTypeForPhone(mPhone.getPhoneId(), mSS.getRilDataRadioTechnology());
      StatsLog.write(76, ServiceState.rilRadioTechnologyToNetworkType(mSS.getRilDataRadioTechnology()), mPhone.getPhoneId());
      if (18 == mSS.getRilDataRadioTechnology()) {
        log("pollStateDone: IWLAN enabled");
      }
    }
    boolean bool16 = bool11;
    if ((bool7) || (bool8))
    {
      mSignalStrength.fixType();
      notifySignalStrength();
    }
    if (bool1)
    {
      mNetworkAttachedRegistrants.notifyRegistrants();
      mNitzState.handleNetworkAvailable();
    }
    if (bool2)
    {
      mNetworkDetachedRegistrants.notifyRegistrants();
      mNitzState.handleNetworkUnavailable();
    }
    if (j != 0) {
      setNotification(2001);
    }
    if (bool9)
    {
      updateSpnDisplay();
      localTelephonyManager.setNetworkOperatorNameForPhone(mPhone.getPhoneId(), mSS.getOperatorAlpha());
      String str1 = localTelephonyManager.getNetworkOperatorForPhone(mPhone.getPhoneId());
      String str2 = localTelephonyManager.getNetworkCountryIso(mPhone.getPhoneId());
      Object localObject2 = mSS.getOperatorNumeric();
      localObject1 = localObject2;
      if (!mPhone.isPhoneTypeGsm())
      {
        localObject1 = localObject2;
        if (isInvalidOperatorNumeric((String)localObject2)) {
          localObject1 = fixUnknownMcc((String)localObject2, mSS.getCdmaSystemId());
        }
      }
      localTelephonyManager.setNetworkOperatorNumericForPhone(mPhone.getPhoneId(), (String)localObject1);
      if (isInvalidOperatorNumeric((String)localObject1))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("operatorNumeric ");
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" is invalid");
        log(((StringBuilder)localObject2).toString());
        mLocaleTracker.updateOperatorNumericAsync("");
        mNitzState.handleNetworkUnavailable();
      }
      else if (mSS.getRilDataRadioTechnology() != 18)
      {
        if (!mPhone.isPhoneTypeGsm()) {
          setOperatorIdd((String)localObject1);
        }
        mLocaleTracker.updateOperatorNumericSync((String)localObject1);
        localObject2 = mLocaleTracker.getCurrentCountry();
        bool14 = iccCardExists();
        bool17 = networkCountryIsoChanged((String)localObject2, str2);
        if ((bool14) && (bool17)) {
          bool11 = true;
        } else {
          bool11 = false;
        }
        long l = System.currentTimeMillis();
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Before handleNetworkCountryCodeKnown: countryChanged=");
        localStringBuilder.append(bool11);
        localStringBuilder.append(" iccCardExist=");
        localStringBuilder.append(bool14);
        localStringBuilder.append(" countryIsoChanged=");
        localStringBuilder.append(bool17);
        localStringBuilder.append(" operatorNumeric=");
        localStringBuilder.append((String)localObject1);
        localStringBuilder.append(" prevOperatorNumeric=");
        localStringBuilder.append(str1);
        localStringBuilder.append(" countryIsoCode=");
        localStringBuilder.append((String)localObject2);
        localStringBuilder.append(" prevCountryIsoCode=");
        localStringBuilder.append(str2);
        localStringBuilder.append(" ltod=");
        localStringBuilder.append(TimeUtils.logTimeOfDay(l));
        log(localStringBuilder.toString());
        mNitzState.handleNetworkCountryCodeSet(bool11);
      }
      j = mPhone.getPhoneId();
      if (mPhone.isPhoneTypeGsm()) {
        bool11 = mSS.getVoiceRoaming();
      } else if ((!mSS.getVoiceRoaming()) && (!mSS.getDataRoaming())) {
        bool11 = false;
      } else {
        bool11 = true;
      }
      localTelephonyManager.setNetworkRoamingForPhone(j, bool11);
      setRoamingType(mSS);
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Broadcasting ServiceState : ");
      ((StringBuilder)localObject1).append(mSS);
      log(((StringBuilder)localObject1).toString());
      if (!localServiceState.equals(mPhone.getServiceState())) {
        mPhone.notifyServiceStateChanged(mPhone.getServiceState());
      }
      mPhone.getContext().getContentResolver().insert(Telephony.ServiceStateTable.getUriForSubscriptionId(mPhone.getSubId()), Telephony.ServiceStateTable.getContentValuesForServiceState(mSS));
      TelephonyMetrics.getInstance().writeServiceStateChanged(mPhone.getPhoneId(), mSS);
    }
    if ((bool3) || (bool15) || (bool4) || (bool1) || (bool2)) {
      logAttachChange();
    }
    if ((bool3) || (bool15)) {
      mAttachedRegistrants.notifyRegistrants();
    }
    if (bool4) {
      mDetachedRegistrants.notifyRegistrants();
    }
    if ((bool8) || (bool7)) {
      logRatChange();
    }
    if ((bool5) || (bool8))
    {
      notifyDataRegStateRilRadioTechnologyChanged();
      if (18 == mSS.getRilDataRadioTechnology()) {
        mPhone.notifyDataConnection("iwlanAvailable");
      } else {
        mPhone.notifyDataConnection(null);
      }
    }
    if ((bool10) || (bool16) || (bool12) || (bool13)) {
      logRoamingChange();
    }
    if (bool10) {
      mVoiceRoamingOnRegistrants.notifyRegistrants();
    }
    if (bool16) {
      mVoiceRoamingOffRegistrants.notifyRegistrants();
    }
    if (bool12) {
      mDataRoamingOnRegistrants.notifyRegistrants();
    }
    if (bool13) {
      mDataRoamingOffRegistrants.notifyRegistrants();
    }
    if (bool6) {
      mPhone.notifyLocationChanged();
    }
    if (mPhone.isPhoneTypeGsm()) {
      if (!isGprsConsistent(mSS.getDataRegState(), mSS.getVoiceRegState()))
      {
        if ((!mStartedGprsRegCheck) && (!mReportedGprsNoReg))
        {
          mStartedGprsRegCheck = true;
          j = Settings.Global.getInt(mPhone.getContext().getContentResolver(), "gprs_register_check_period_ms", 60000);
          sendMessageDelayed(obtainMessage(22), j);
        }
      }
      else {
        mReportedGprsNoReg = false;
      }
    }
  }
  
  private void processCellLocationInfo(CellLocation paramCellLocation, CellIdentity paramCellIdentity)
  {
    int i;
    int j;
    int k;
    int m;
    int n;
    int i1;
    if (mPhone.isPhoneTypeGsm())
    {
      i = -1;
      j = -1;
      k = -1;
      m = i;
      n = j;
      i1 = k;
      if (paramCellIdentity != null)
      {
        n = paramCellIdentity.getType();
        if (n != 1)
        {
          switch (n)
          {
          default: 
            m = i;
            n = j;
            i1 = k;
            break;
          case 5: 
            n = ((CellIdentityTdscdma)paramCellIdentity).getCid();
            i1 = ((CellIdentityTdscdma)paramCellIdentity).getLac();
            m = i;
            break;
          case 4: 
            n = ((CellIdentityWcdma)paramCellIdentity).getCid();
            i1 = ((CellIdentityWcdma)paramCellIdentity).getLac();
            m = ((CellIdentityWcdma)paramCellIdentity).getPsc();
            break;
          case 3: 
            n = ((CellIdentityLte)paramCellIdentity).getCi();
            i1 = ((CellIdentityLte)paramCellIdentity).getTac();
            m = i;
            break;
          }
        }
        else
        {
          n = ((CellIdentityGsm)paramCellIdentity).getCid();
          i1 = ((CellIdentityGsm)paramCellIdentity).getLac();
          m = i;
        }
      }
      ((GsmCellLocation)paramCellLocation).setLacAndCid(i1, n);
      ((GsmCellLocation)paramCellLocation).setPsc(m);
    }
    else
    {
      int i2 = -1;
      int i3 = Integer.MAX_VALUE;
      j = Integer.MAX_VALUE;
      int i4 = 0;
      int i5 = 0;
      k = i2;
      i1 = i3;
      n = j;
      i = i4;
      m = i5;
      if (paramCellIdentity != null) {
        if (paramCellIdentity.getType() != 2)
        {
          k = i2;
          i1 = i3;
          n = j;
          i = i4;
          m = i5;
        }
        else
        {
          k = ((CellIdentityCdma)paramCellIdentity).getBasestationId();
          i1 = ((CellIdentityCdma)paramCellIdentity).getLatitude();
          n = ((CellIdentityCdma)paramCellIdentity).getLongitude();
          i = ((CellIdentityCdma)paramCellIdentity).getSystemId();
          m = ((CellIdentityCdma)paramCellIdentity).getNetworkId();
        }
      }
      i3 = i1;
      j = n;
      if (i1 == 0)
      {
        i3 = i1;
        j = n;
        if (n == 0)
        {
          i3 = Integer.MAX_VALUE;
          j = Integer.MAX_VALUE;
        }
      }
      ((CdmaCellLocation)paramCellLocation).setCellLocationData(k, i3, j, i, m);
    }
  }
  
  private void queueNextSignalStrengthPoll()
  {
    if (mDontPollSignalStrength) {
      return;
    }
    Message localMessage = obtainMessage();
    what = 10;
    sendMessageDelayed(localMessage, 20000L);
  }
  
  private boolean regCodeIsRoaming(int paramInt)
  {
    boolean bool;
    if (5 == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private int regCodeToServiceState(int paramInt)
  {
    if ((paramInt != 1) && (paramInt != 5)) {
      return 1;
    }
    return 0;
  }
  
  private void saveCdmaSubscriptionSource(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Storing cdma subscription source: ");
    localStringBuilder.append(paramInt);
    log(localStringBuilder.toString());
    Settings.Global.putInt(mPhone.getContext().getContentResolver(), "subscription_mode", paramInt);
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Read from settings: ");
    localStringBuilder.append(Settings.Global.getInt(mPhone.getContext().getContentResolver(), "subscription_mode", -1));
    log(localStringBuilder.toString());
  }
  
  private int selectResourceForRejectCode(int paramInt, boolean paramBoolean)
  {
    int i = 0;
    if (paramInt != 6) {
      switch (paramInt)
      {
      default: 
        paramInt = i;
        break;
      case 3: 
        if (paramBoolean) {
          paramInt = 17040445;
        } else {
          paramInt = 17040444;
        }
        break;
      case 2: 
        if (paramBoolean) {
          paramInt = 17040447;
        } else {
          paramInt = 17040446;
        }
        break;
      case 1: 
        if (paramBoolean) {
          paramInt = 17040441;
        } else {
          paramInt = 17040440;
        }
        break;
      }
    } else if (paramBoolean) {
      paramInt = 17040443;
    } else {
      paramInt = 17040442;
    }
    return paramInt;
  }
  
  private void setPhyCellInfoFromCellIdentity(ServiceState paramServiceState, CellIdentity paramCellIdentity)
  {
    if (paramCellIdentity == null)
    {
      log("Could not set ServiceState channel number. CellIdentity null");
      return;
    }
    paramServiceState.setChannelNumber(paramCellIdentity.getChannelNumber());
    if ((paramCellIdentity instanceof CellIdentityLte))
    {
      CellIdentityLte localCellIdentityLte = (CellIdentityLte)paramCellIdentity;
      paramCellIdentity = null;
      Object localObject;
      int j;
      if (!ArrayUtils.isEmpty(mLastPhysicalChannelConfigList))
      {
        localObject = getBandwidthsFromConfigs(mLastPhysicalChannelConfigList);
        int i = localObject.length;
        for (j = 0;; j++)
        {
          paramCellIdentity = (CellIdentity)localObject;
          if (j >= i) {
            break;
          }
          int k = localObject[j];
          if (!isValidLteBandwidthKhz(k))
          {
            paramCellIdentity = new StringBuilder();
            paramCellIdentity.append("Invalid LTE Bandwidth in RegistrationState, ");
            paramCellIdentity.append(k);
            loge(paramCellIdentity.toString());
            paramCellIdentity = null;
            break;
          }
        }
      }
      if (paramCellIdentity != null)
      {
        localObject = paramCellIdentity;
        if (paramCellIdentity.length != 1) {}
      }
      else
      {
        j = localCellIdentityLte.getBandwidth();
        if (isValidLteBandwidthKhz(j))
        {
          localObject = new int[] { j };
        }
        else if (j == Integer.MAX_VALUE)
        {
          localObject = paramCellIdentity;
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Invalid LTE Bandwidth in RegistrationState, ");
          ((StringBuilder)localObject).append(j);
          loge(((StringBuilder)localObject).toString());
          localObject = paramCellIdentity;
        }
      }
      if (localObject != null) {
        paramServiceState.setCellBandwidths((int[])localObject);
      }
    }
  }
  
  private void setRoamingOff()
  {
    mNewSS.setVoiceRoaming(false);
    mNewSS.setDataRoaming(false);
    mNewSS.setCdmaEriIconIndex(1);
  }
  
  private void setRoamingOn()
  {
    mNewSS.setVoiceRoaming(true);
    mNewSS.setDataRoaming(true);
    mNewSS.setCdmaEriIconIndex(0);
    mNewSS.setCdmaEriIconMode(0);
  }
  
  private void setSignalStrengthDefaultValues()
  {
    mSignalStrength = new SignalStrength(true);
  }
  
  private void setTimeFromNITZString(String paramString, long paramLong)
  {
    long l = SystemClock.elapsedRealtime();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("NITZ: ");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(",");
    ((StringBuilder)localObject).append(paramLong);
    ((StringBuilder)localObject).append(" start=");
    ((StringBuilder)localObject).append(l);
    ((StringBuilder)localObject).append(" delay=");
    ((StringBuilder)localObject).append(l - paramLong);
    Rlog.d("SST", ((StringBuilder)localObject).toString());
    paramString = NitzData.parse(paramString);
    if (paramString != null) {
      try
      {
        localObject = new com/android/internal/telephony/util/TimeStampedValue;
        ((TimeStampedValue)localObject).<init>(paramString, paramLong);
        mNitzState.handleNitzReceived((TimeStampedValue)localObject);
        paramLong = SystemClock.elapsedRealtime();
        paramString = new StringBuilder();
        paramString.append("NITZ: end=");
        paramString.append(paramLong);
        paramString.append(" dur=");
        paramString.append(paramLong - l);
        Rlog.d("SST", paramString.toString());
      }
      finally
      {
        paramLong = SystemClock.elapsedRealtime();
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("NITZ: end=");
        ((StringBuilder)localObject).append(paramLong);
        ((StringBuilder)localObject).append(" dur=");
        ((StringBuilder)localObject).append(paramLong - l);
        Rlog.d("SST", ((StringBuilder)localObject).toString());
      }
    }
  }
  
  private void updateLteEarfcnLists(PersistableBundle paramPersistableBundle)
  {
    synchronized (mLteRsrpBoostLock)
    {
      mLteRsrpBoost = paramPersistableBundle.getInt("lte_earfcns_rsrp_boost_int", 0);
      mEarfcnPairListForRsrpBoost = convertEarfcnStringArrayToPairList(paramPersistableBundle.getStringArray("boosted_lte_earfcns_string_array"));
      return;
    }
  }
  
  private void updateOperatorNameFromEri()
  {
    Object localObject1;
    if (mPhone.isPhoneTypeCdma())
    {
      if ((mCi.getRadioState().isOn()) && (!mIsSubscriptionFromRuim))
      {
        if (mSS.getVoiceRegState() == 0) {
          localObject1 = mPhone.getCdmaEriText();
        } else {
          localObject1 = mPhone.getContext().getText(17040936).toString();
        }
        mSS.setOperatorAlphaLong((String)localObject1);
      }
    }
    else if (mPhone.isPhoneTypeCdmaLte())
    {
      int i;
      if ((mUiccController.getUiccCard(getPhoneId()) != null) && (mUiccController.getUiccCard(getPhoneId()).getOperatorBrandOverride() != null)) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i == 0) && (mCi.getRadioState().isOn()) && (mPhone.isEriFileLoaded()) && ((!ServiceState.isLte(mSS.getRilVoiceRadioTechnology())) || (mPhone.getContext().getResources().getBoolean(17956868))) && (!mIsSubscriptionFromRuim))
      {
        localObject1 = mSS.getOperatorAlpha();
        if (mSS.getVoiceRegState() == 0)
        {
          localObject1 = mPhone.getCdmaEriText();
        }
        else if (mSS.getVoiceRegState() == 3)
        {
          if (mIccRecords != null) {
            localObject1 = mIccRecords.getServiceProviderName();
          } else {
            localObject1 = null;
          }
          Object localObject2 = localObject1;
          localObject1 = localObject2;
          if (TextUtils.isEmpty(localObject2)) {
            localObject1 = SystemProperties.get("ro.cdma.home.operator.alpha");
          }
        }
        else if (mSS.getDataRegState() != 0)
        {
          localObject1 = mPhone.getContext().getText(17040936).toString();
        }
        mSS.setOperatorAlphaLong((String)localObject1);
      }
      if ((mUiccApplcation != null) && (mUiccApplcation.getState() == IccCardApplicationStatus.AppState.APPSTATE_READY) && (mIccRecords != null) && (getCombinedRegState() == 0) && (!ServiceState.isLte(mSS.getRilVoiceRadioTechnology())))
      {
        boolean bool = ((RuimRecords)mIccRecords).getCsimSpnDisplayCondition();
        i = mSS.getCdmaEriIconIndex();
        if ((bool) && (i == 1) && (isInHomeSidNid(mSS.getCdmaSystemId(), mSS.getCdmaNetworkId())) && (mIccRecords != null)) {
          mSS.setOperatorAlphaLong(mIccRecords.getServiceProviderName());
        }
      }
    }
  }
  
  private void updateReportingCriteria(PersistableBundle paramPersistableBundle)
  {
    mPhone.setSignalStrengthReportingCriteria(paramPersistableBundle.getIntArray("lte_rsrp_thresholds_int_array"), 3);
    mPhone.setSignalStrengthReportingCriteria(paramPersistableBundle.getIntArray("wcdma_rscp_thresholds_int_array"), 2);
  }
  
  private void updateServiceStateLteEarfcnBoost(ServiceState paramServiceState, int paramInt)
  {
    Object localObject = mLteRsrpBoostLock;
    if (paramInt != -1) {
      try
      {
        if (containsEarfcnInEarfcnRange(mEarfcnPairListForRsrpBoost, paramInt)) {
          paramServiceState.setLteEarfcnRsrpBoost(mLteRsrpBoost);
        }
      }
      finally
      {
        break label47;
      }
    }
    paramServiceState.setLteEarfcnRsrpBoost(0);
    return;
    label47:
    throw paramServiceState;
  }
  
  protected final boolean alwaysOnHomeNetwork(BaseBundle paramBaseBundle)
  {
    return paramBaseBundle.getBoolean("force_home_network_bool");
  }
  
  protected void cancelPollState()
  {
    mPollingContext = new int[1];
  }
  
  protected void checkCorrectThread()
  {
    if (Thread.currentThread() == getLooper().getThread()) {
      return;
    }
    throw new RuntimeException("ServiceStateTracker must be used from within one thread");
  }
  
  ArrayList<Pair<Integer, Integer>> convertEarfcnStringArrayToPairList(String[] paramArrayOfString)
  {
    ArrayList localArrayList = new ArrayList();
    if (paramArrayOfString != null)
    {
      int i = 0;
      while (i < paramArrayOfString.length) {
        try
        {
          Object localObject = paramArrayOfString[i].split("-");
          if (localObject.length != 2) {
            return null;
          }
          int j = Integer.parseInt(localObject[0]);
          int k = Integer.parseInt(localObject[1]);
          if (j > k) {
            return null;
          }
          localObject = new android/util/Pair;
          ((Pair)localObject).<init>(Integer.valueOf(j), Integer.valueOf(k));
          localArrayList.add(localObject);
          i++;
        }
        catch (NumberFormatException paramArrayOfString)
        {
          return null;
        }
        catch (PatternSyntaxException paramArrayOfString)
        {
          return null;
        }
      }
    }
    return localArrayList;
  }
  
  public void disableLocationUpdates()
  {
    mWantContinuousLocationUpdates = false;
    if ((!mWantSingleLocationUpdate) && (!mWantContinuousLocationUpdates)) {
      mCi.setLocationUpdates(false, null);
    }
  }
  
  protected void disableSingleLocationUpdate()
  {
    mWantSingleLocationUpdate = false;
    if ((!mWantSingleLocationUpdate) && (!mWantContinuousLocationUpdates)) {
      mCi.setLocationUpdates(false, null);
    }
  }
  
  public void dispose()
  {
    mCi.unSetOnSignalStrengthUpdate(this);
    mUiccController.unregisterForIccChanged(this);
    mCi.unregisterForCellInfoList(this);
    mCi.unregisterForPhysicalChannelConfiguration(this);
    mSubscriptionManager.removeOnSubscriptionsChangedListener(mOnSubscriptionsChangedListener);
    mHandlerThread.quit();
    mCi.unregisterForImsNetworkStateChanged(this);
    mPhone.getCarrierActionAgent().unregisterForCarrierAction(this, 1);
    if (mCSST != null)
    {
      mCSST.dispose();
      mCSST = null;
    }
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("ServiceStateTracker:");
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mSubId=");
    ((StringBuilder)localObject).append(mSubId);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mSS=");
    ((StringBuilder)localObject).append(mSS);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mNewSS=");
    ((StringBuilder)localObject).append(mNewSS);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mVoiceCapable=");
    ((StringBuilder)localObject).append(mVoiceCapable);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mRestrictedState=");
    ((StringBuilder)localObject).append(mRestrictedState);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mPollingContext=");
    localStringBuilder.append(mPollingContext);
    localStringBuilder.append(" - ");
    if (mPollingContext != null) {
      localObject = Integer.valueOf(mPollingContext[0]);
    } else {
      localObject = "";
    }
    localStringBuilder.append(localObject);
    paramPrintWriter.println(localStringBuilder.toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mDesiredPowerState=");
    ((StringBuilder)localObject).append(mDesiredPowerState);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mDontPollSignalStrength=");
    ((StringBuilder)localObject).append(mDontPollSignalStrength);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mSignalStrength=");
    ((StringBuilder)localObject).append(mSignalStrength);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mLastSignalStrength=");
    ((StringBuilder)localObject).append(mLastSignalStrength);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mRestrictedState=");
    ((StringBuilder)localObject).append(mRestrictedState);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mPendingRadioPowerOffAfterDataOff=");
    ((StringBuilder)localObject).append(mPendingRadioPowerOffAfterDataOff);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mPendingRadioPowerOffAfterDataOffTag=");
    ((StringBuilder)localObject).append(mPendingRadioPowerOffAfterDataOffTag);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCellLoc=");
    ((StringBuilder)localObject).append(Rlog.pii(false, mCellLoc));
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mNewCellLoc=");
    ((StringBuilder)localObject).append(Rlog.pii(false, mNewCellLoc));
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mLastCellInfoListTime=");
    ((StringBuilder)localObject).append(mLastCellInfoListTime);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    dumpCellInfoList(paramPrintWriter);
    paramPrintWriter.flush();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mPreferredNetworkType=");
    ((StringBuilder)localObject).append(mPreferredNetworkType);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mMaxDataCalls=");
    ((StringBuilder)localObject).append(mMaxDataCalls);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mNewMaxDataCalls=");
    ((StringBuilder)localObject).append(mNewMaxDataCalls);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mReasonDataDenied=");
    ((StringBuilder)localObject).append(mReasonDataDenied);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mNewReasonDataDenied=");
    ((StringBuilder)localObject).append(mNewReasonDataDenied);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mGsmRoaming=");
    ((StringBuilder)localObject).append(mGsmRoaming);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mDataRoaming=");
    ((StringBuilder)localObject).append(mDataRoaming);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mEmergencyOnly=");
    ((StringBuilder)localObject).append(mEmergencyOnly);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    paramPrintWriter.flush();
    mNitzState.dumpState(paramPrintWriter);
    paramPrintWriter.flush();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mStartedGprsRegCheck=");
    ((StringBuilder)localObject).append(mStartedGprsRegCheck);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mReportedGprsNoReg=");
    ((StringBuilder)localObject).append(mReportedGprsNoReg);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mNotification=");
    ((StringBuilder)localObject).append(mNotification);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCurSpn=");
    ((StringBuilder)localObject).append(mCurSpn);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCurDataSpn=");
    ((StringBuilder)localObject).append(mCurDataSpn);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCurShowSpn=");
    ((StringBuilder)localObject).append(mCurShowSpn);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCurPlmn=");
    ((StringBuilder)localObject).append(mCurPlmn);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCurShowPlmn=");
    ((StringBuilder)localObject).append(mCurShowPlmn);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    paramPrintWriter.flush();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCurrentOtaspMode=");
    ((StringBuilder)localObject).append(mCurrentOtaspMode);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mRoamingIndicator=");
    ((StringBuilder)localObject).append(mRoamingIndicator);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mIsInPrl=");
    ((StringBuilder)localObject).append(mIsInPrl);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mDefaultRoamingIndicator=");
    ((StringBuilder)localObject).append(mDefaultRoamingIndicator);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mRegistrationState=");
    ((StringBuilder)localObject).append(mRegistrationState);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mMdn=");
    ((StringBuilder)localObject).append(mMdn);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mHomeSystemId=");
    ((StringBuilder)localObject).append(mHomeSystemId);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mHomeNetworkId=");
    ((StringBuilder)localObject).append(mHomeNetworkId);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mMin=");
    ((StringBuilder)localObject).append(mMin);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mPrlVersion=");
    ((StringBuilder)localObject).append(mPrlVersion);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mIsMinInfoReady=");
    ((StringBuilder)localObject).append(mIsMinInfoReady);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mIsEriTextLoaded=");
    ((StringBuilder)localObject).append(mIsEriTextLoaded);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mIsSubscriptionFromRuim=");
    ((StringBuilder)localObject).append(mIsSubscriptionFromRuim);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCdmaSSM=");
    ((StringBuilder)localObject).append(mCdmaSSM);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mRegistrationDeniedReason=");
    ((StringBuilder)localObject).append(mRegistrationDeniedReason);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mCurrentCarrier=");
    ((StringBuilder)localObject).append(mCurrentCarrier);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    paramPrintWriter.flush();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mImsRegistered=");
    ((StringBuilder)localObject).append(mImsRegistered);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mImsRegistrationOnOff=");
    ((StringBuilder)localObject).append(mImsRegistrationOnOff);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mAlarmSwitch=");
    ((StringBuilder)localObject).append(mAlarmSwitch);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mRadioDisabledByCarrier");
    ((StringBuilder)localObject).append(mRadioDisabledByCarrier);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mPowerOffDelayNeed=");
    ((StringBuilder)localObject).append(mPowerOffDelayNeed);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mDeviceShuttingDown=");
    ((StringBuilder)localObject).append(mDeviceShuttingDown);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mSpnUpdatePending=");
    ((StringBuilder)localObject).append(mSpnUpdatePending);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(" mLteRsrpBoost=");
    ((StringBuilder)localObject).append(mLteRsrpBoost);
    paramPrintWriter.println(((StringBuilder)localObject).toString());
    dumpEarfcnPairList(paramPrintWriter);
    mLocaleTracker.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.println(" Roaming Log:");
    paramPrintWriter = new IndentingPrintWriter(paramPrintWriter, "  ");
    paramPrintWriter.increaseIndent();
    mRoamingLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.println(" Attach Log:");
    paramPrintWriter.increaseIndent();
    mAttachLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.println(" Phone Change Log:");
    paramPrintWriter.increaseIndent();
    mPhoneTypeLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.println(" Rat Change Log:");
    paramPrintWriter.increaseIndent();
    mRatLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.decreaseIndent();
    paramPrintWriter.println(" Radio power Log:");
    paramPrintWriter.increaseIndent();
    mRadioPowerLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    mNitzState.dumpLogs(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }
  
  public void enableLocationUpdates()
  {
    if ((!mWantSingleLocationUpdate) && (!mWantContinuousLocationUpdates))
    {
      mWantContinuousLocationUpdates = true;
      mCi.setLocationUpdates(true, obtainMessage(18));
      return;
    }
  }
  
  public void enableSingleLocationUpdate()
  {
    if ((!mWantSingleLocationUpdate) && (!mWantContinuousLocationUpdates))
    {
      mWantSingleLocationUpdate = true;
      mCi.setLocationUpdates(true, obtainMessage(18));
      return;
    }
  }
  
  public List<CellInfo> getAllCellInfo(WorkSource arg1)
  {
    CellInfoResult localCellInfoResult = new CellInfoResult(null);
    if (mCi.getRilVersion() >= 8)
    {
      if (isCallerOnDifferentThread())
      {
        if (SystemClock.elapsedRealtime() - mLastCellInfoListTime > 2000L)
        {
          Message localMessage = obtainMessage(43, localCellInfoResult);
          synchronized (lockObj)
          {
            list = null;
            mCi.getCellInfoList(localMessage, ???);
            try
            {
              lockObj.wait(5000L);
            }
            catch (InterruptedException ???)
            {
              ???.printStackTrace();
            }
          }
        }
        log("SST.getAllCellInfo(): return last, back to back calls");
        list = mLastCellInfoList;
      }
      else
      {
        log("SST.getAllCellInfo(): return last, same thread can't block");
        list = mLastCellInfoList;
      }
    }
    else
    {
      log("SST.getAllCellInfo(): not implemented");
      list = null;
    }
    synchronized (lockObj)
    {
      if (list != null)
      {
        ??? = list;
        return ???;
      }
      log("SST.getAllCellInfo(): X size=0 list=null");
      return null;
    }
  }
  
  public String getCdmaMin()
  {
    return mMin;
  }
  
  public CellLocation getCellLocation(WorkSource paramWorkSource)
  {
    if ((((GsmCellLocation)mCellLoc).getLac() >= 0) && (((GsmCellLocation)mCellLoc).getCid() >= 0)) {
      return mCellLoc;
    }
    Object localObject1 = getAllCellInfo(paramWorkSource);
    if (localObject1 != null)
    {
      paramWorkSource = new GsmCellLocation();
      localObject1 = ((List)localObject1).iterator();
      while (((Iterator)localObject1).hasNext())
      {
        Object localObject2 = (CellInfo)((Iterator)localObject1).next();
        if ((localObject2 instanceof CellInfoGsm))
        {
          localObject1 = ((CellInfoGsm)localObject2).getCellIdentity();
          paramWorkSource.setLacAndCid(((CellIdentityGsm)localObject1).getLac(), ((CellIdentityGsm)localObject1).getCid());
          paramWorkSource.setPsc(((CellIdentityGsm)localObject1).getPsc());
          return paramWorkSource;
        }
        if ((localObject2 instanceof CellInfoWcdma))
        {
          localObject1 = ((CellInfoWcdma)localObject2).getCellIdentity();
          paramWorkSource.setLacAndCid(((CellIdentityWcdma)localObject1).getLac(), ((CellIdentityWcdma)localObject1).getCid());
          paramWorkSource.setPsc(((CellIdentityWcdma)localObject1).getPsc());
          return paramWorkSource;
        }
        if (((localObject2 instanceof CellInfoLte)) && ((paramWorkSource.getLac() < 0) || (paramWorkSource.getCid() < 0)))
        {
          localObject2 = ((CellInfoLte)localObject2).getCellIdentity();
          if ((((CellIdentityLte)localObject2).getTac() != Integer.MAX_VALUE) && (((CellIdentityLte)localObject2).getCi() != Integer.MAX_VALUE))
          {
            paramWorkSource.setLacAndCid(((CellIdentityLte)localObject2).getTac(), ((CellIdentityLte)localObject2).getCi());
            paramWorkSource.setPsc(0);
          }
        }
      }
      return paramWorkSource;
    }
    return mCellLoc;
  }
  
  protected int getCombinedRegState()
  {
    int i = mSS.getVoiceRegState();
    int j = mSS.getDataRegState();
    int k;
    if (i != 1)
    {
      k = i;
      if (i != 3) {}
    }
    else
    {
      k = i;
      if (j == 0)
      {
        log("getCombinedRegState: return STATE_IN_SERVICE as Data is in service");
        k = j;
      }
    }
    return k;
  }
  
  public int getCurrentDataConnectionState()
  {
    return mSS.getDataRegState();
  }
  
  public boolean getDesiredPowerState()
  {
    return mDesiredPowerState;
  }
  
  protected String getHomeOperatorNumeric()
  {
    String str1 = ((TelephonyManager)mPhone.getContext().getSystemService("phone")).getSimOperatorNumericForPhone(mPhone.getPhoneId());
    String str2 = str1;
    if (!mPhone.isPhoneTypeGsm())
    {
      str2 = str1;
      if (TextUtils.isEmpty(str1)) {
        str2 = SystemProperties.get("ro.cdma.home.operator.numeric", "");
      }
    }
    return str2;
  }
  
  public String getImsi()
  {
    String str = ((TelephonyManager)mPhone.getContext().getSystemService("phone")).getSimOperatorNumericForPhone(mPhone.getPhoneId());
    if ((!TextUtils.isEmpty(str)) && (!TextUtils.isEmpty(getCdmaMin())))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(str);
      localStringBuilder.append(getCdmaMin());
      return localStringBuilder.toString();
    }
    return null;
  }
  
  public LocaleTracker getLocaleTracker()
  {
    return mLocaleTracker;
  }
  
  public String getMdnNumber()
  {
    return mMdn;
  }
  
  public int getOtasp()
  {
    if (!mPhone.getIccRecordsLoaded())
    {
      log("getOtasp: otasp uninitialized due to sim not loaded");
      return 0;
    }
    boolean bool = mPhone.isPhoneTypeGsm();
    int i = 3;
    if (bool)
    {
      log("getOtasp: otasp not needed for GSM");
      return 3;
    }
    if ((mIsSubscriptionFromRuim) && (mMin == null)) {
      return 2;
    }
    if ((mMin != null) && (mMin.length() >= 6))
    {
      if ((!mMin.equals("1111110111")) && (!mMin.substring(0, 6).equals("000000")) && (!SystemProperties.getBoolean("test_cdma_setup", false))) {
        break label174;
      }
      i = 2;
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("getOtasp: bad mMin='");
      localStringBuilder.append(mMin);
      localStringBuilder.append("'");
      log(localStringBuilder.toString());
      i = 1;
    }
    label174:
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getOtasp: state=");
    localStringBuilder.append(i);
    log(localStringBuilder.toString());
    return i;
  }
  
  protected Phone getPhone()
  {
    return mPhone;
  }
  
  protected int getPhoneId()
  {
    return mPhone.getPhoneId();
  }
  
  public boolean getPowerStateFromCarrier()
  {
    return mRadioDisabledByCarrier ^ true;
  }
  
  public String getPrlVersion()
  {
    return mPrlVersion;
  }
  
  public SignalStrength getSignalStrength()
  {
    return mSignalStrength;
  }
  
  public String getSystemProperty(String paramString1, String paramString2)
  {
    return TelephonyManager.getTelephonyProperty(mPhone.getPhoneId(), paramString1, paramString2);
  }
  
  public void handleMessage(Message arg1)
  {
    int i = what;
    int j = -1;
    boolean bool = false;
    Object localObject1;
    Object localObject3;
    switch (i)
    {
    case 7: 
    case 8: 
    case 9: 
    case 13: 
    case 24: 
    case 25: 
    case 28: 
    case 29: 
    case 30: 
    case 31: 
    case 32: 
    case 33: 
    case 41: 
    default: 
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Unhandled message with number: ");
      ((StringBuilder)localObject1).append(what);
      log(((StringBuilder)localObject1).toString());
      break;
    case 55: 
      ??? = (AsyncResult)obj;
      if (exception == null)
      {
        ??? = (List)result;
        mPhone.notifyPhysicalChannelConfiguration(???);
        mLastPhysicalChannelConfigList = ???;
        if (RatRatcheter.updateBandwidths(getBandwidthsFromConfigs(???), mSS)) {
          mPhone.notifyServiceStateChanged(mSS);
        }
      }
      break;
    case 54: 
      log("EVENT_RADIO_POWER_OFF_DONE");
      if ((mDeviceShuttingDown) && (mCi.getRadioState().isAvailable())) {
        mCi.requestShutdown(null);
      }
      break;
    case 53: 
      log("EVENT_IMS_SERVICE_STATE_CHANGED");
      if (mSS.getState() != 0) {
        mPhone.notifyServiceStateChanged(mPhone.getServiceState());
      }
      break;
    case 52: 
      log("EVENT_SIM_NOT_INSERTED");
      cancelAllNotifications();
      mMdn = null;
      mMin = null;
      mIsMinInfoReady = false;
      break;
    case 51: 
      ??? = (AsyncResult)obj;
      if (exception == null)
      {
        bool = ((Boolean)result).booleanValue();
        ??? = new StringBuilder();
        ???.append("EVENT_RADIO_POWER_FROM_CARRIER: ");
        ???.append(bool);
        log(???.toString());
        setRadioPowerFromCarrier(bool);
      }
      break;
    case 49: 
      j = SubscriptionManager.getDefaultDataSubscriptionId();
      ProxyController.getInstance().unregisterForAllDataDisconnected(j, this);
      try
      {
        if (mPendingRadioPowerOffAfterDataOff)
        {
          log("EVENT_ALL_DATA_DISCONNECTED, turn radio off now.");
          hangupAndPowerOff();
          mPendingRadioPowerOffAfterDataOff = false;
        }
        else
        {
          log("EVENT_ALL_DATA_DISCONNECTED is stale");
        }
      }
      finally {}
    case 48: 
      log("EVENT_IMS_CAPABILITY_CHANGED");
      updateSpnDisplay();
      try
      {
        mPhone.notifySignalStrength();
        log("notifySignalStrength for System UI for IMS icon due to IMS_CAPABILITY_CHANGED");
      }
      catch (Exception ???)
      {
        log("notifySignalStrength error from IMS_CAPABILITY_CHANGED");
      }
    case 47: 
      ??? = (AsyncResult)obj;
      if (exception == null)
      {
        if (((int[])result)[0] == 1) {
          bool = true;
        }
        mImsRegistered = bool;
      }
      break;
    case 46: 
      mCi.getImsRegistrationState(obtainMessage(47));
      break;
    case 45: 
      log("EVENT_CHANGE_IMS_STATE:");
      setPowerStateToDesired();
      break;
    case 44: 
      localObject1 = (AsyncResult)obj;
      if (exception != null)
      {
        ??? = new StringBuilder();
        ???.append("EVENT_UNSOL_CELL_INFO_LIST: error ignoring, e=");
        ???.append(exception);
        log(???.toString());
      }
      else
      {
        ??? = (List)result;
        mLastCellInfoListTime = SystemClock.elapsedRealtime();
        mLastCellInfoList = ???;
        mPhone.notifyCellInfo(???);
      }
      break;
    case 43: 
      localObject1 = (AsyncResult)obj;
      CellInfoResult localCellInfoResult = (CellInfoResult)userObj;
      synchronized (lockObj)
      {
        if (exception != null)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("EVENT_GET_CELL_INFO_LIST: error ret null, e=");
          localStringBuilder.append(exception);
          log(localStringBuilder.toString());
          list = null;
        }
        else
        {
          list = ((List)result);
        }
        mLastCellInfoListTime = SystemClock.elapsedRealtime();
        mLastCellInfoList = list;
        lockObj.notify();
      }
    case 42: 
      onUpdateIccAvailability();
      if ((mUiccApplcation != null) && (mUiccApplcation.getState() != IccCardApplicationStatus.AppState.APPSTATE_READY))
      {
        mIsSimReady = false;
        updateSpnDisplay();
      }
      break;
    case 40: 
      ??? = (AsyncResult)obj;
      if (exception == null) {
        mPrlVersion = Integer.toString(((int[])result)[0]);
      }
      break;
    case 39: 
      handleCdmaSubscriptionSource(mCdmaSSM.getCdmaSubscriptionSource());
      break;
    case 38: 
      try
      {
        if ((mPendingRadioPowerOffAfterDataOff) && (arg1 == mPendingRadioPowerOffAfterDataOffTag))
        {
          log("EVENT_SET_RADIO_OFF, turn radio off now.");
          hangupAndPowerOff();
          mPendingRadioPowerOffAfterDataOffTag += 1;
          mPendingRadioPowerOffAfterDataOff = false;
        }
        else
        {
          localObject3 = new java/lang/StringBuilder;
          ((StringBuilder)localObject3).<init>();
          ((StringBuilder)localObject3).append("EVENT_SET_RADIO_OFF is stale arg1=");
          ((StringBuilder)localObject3).append(arg1);
          ((StringBuilder)localObject3).append("!= tag=");
          ((StringBuilder)localObject3).append(mPendingRadioPowerOffAfterDataOffTag);
          log(((StringBuilder)localObject3).toString());
        }
      }
      finally {}
    case 37: 
      ??? = (AsyncResult)obj;
      if (exception == null)
      {
        j = ((int[])result)[0];
        if ((j == 8) || (j == 10))
        {
          log("EVENT_OTA_PROVISION_STATUS_CHANGE: Complete, Reload MDN");
          mCi.getCDMASubscription(obtainMessage(34));
        }
      }
      break;
    case 36: 
      log("ERI file has been loaded, repolling.");
      pollState();
      break;
    case 35: 
      updatePhoneObject();
      mCi.getNetworkSelectionMode(obtainMessage(14));
      getSubscriptionInfoAndStartPollingThreads();
      break;
    case 34: 
      if (!mPhone.isPhoneTypeGsm())
      {
        ??? = (AsyncResult)obj;
        if (exception == null)
        {
          ??? = (String[])result;
          if ((??? != null) && (???.length >= 5))
          {
            mMdn = ???[0];
            parseSidNid(???[1], ???[2]);
            mMin = ???[3];
            mPrlVersion = ???[4];
            ??? = new StringBuilder();
            ???.append("GET_CDMA_SUBSCRIPTION: MDN=");
            ???.append(mMdn);
            log(???.toString());
            mIsMinInfoReady = true;
            updateOtaspState();
            notifyCdmaSubscriptionInfoReady();
            if ((!mIsSubscriptionFromRuim) && (mIccRecords != null))
            {
              log("GET_CDMA_SUBSCRIPTION set imsi in mIccRecords");
              mIccRecords.setImsi(getImsi());
            }
            else
            {
              log("GET_CDMA_SUBSCRIPTION either mIccRecords is null or NV type device - not setting Imsi in mIccRecords");
            }
          }
          else
          {
            localObject3 = new StringBuilder();
            ((StringBuilder)localObject3).append("GET_CDMA_SUBSCRIPTION: error parsing cdmaSubscription params num=");
            ((StringBuilder)localObject3).append(???.length);
            log(((StringBuilder)localObject3).toString());
          }
        }
      }
      break;
    case 27: 
      if (!mPhone.isPhoneTypeGsm())
      {
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("EVENT_RUIM_RECORDS_LOADED: what=");
        ((StringBuilder)localObject3).append(what);
        log(((StringBuilder)localObject3).toString());
        updatePhoneObject();
        if (mPhone.isPhoneTypeCdma())
        {
          updateSpnDisplay();
        }
        else
        {
          ??? = (RuimRecords)mIccRecords;
          if (??? != null)
          {
            if (???.isProvisioned())
            {
              mMdn = ???.getMdn();
              mMin = ???.getMin();
              parseSidNid(???.getSid(), ???.getNid());
              mPrlVersion = ???.getPrlVersion();
              mIsMinInfoReady = true;
            }
            updateOtaspState();
            notifyCdmaSubscriptionInfoReady();
          }
          pollState();
          updateSpnDisplay();
        }
      }
      break;
    case 26: 
      if (mPhone.getLteOnCdmaMode() == 1)
      {
        log("Receive EVENT_RUIM_READY");
        pollState();
      }
      else
      {
        log("Receive EVENT_RUIM_READY and Send Request getCDMASubscription.");
        getSubscriptionInfoAndStartPollingThreads();
      }
      mCi.getNetworkSelectionMode(obtainMessage(14));
      break;
    case 23: 
      if (mPhone.isPhoneTypeGsm())
      {
        log("EVENT_RESTRICTED_STATE_CHANGED");
        onRestrictedStateChanged((AsyncResult)obj);
      }
      break;
    case 22: 
      if ((mPhone.isPhoneTypeGsm()) && (mSS != null) && (!isGprsConsistent(mSS.getDataRegState(), mSS.getVoiceRegState())))
      {
        localObject3 = (GsmCellLocation)mPhone.getCellLocation();
        ??? = mSS.getOperatorNumeric();
        if (localObject3 != null) {
          j = ((GsmCellLocation)localObject3).getCid();
        }
        EventLog.writeEvent(50107, new Object[] { ???, Integer.valueOf(j) });
        mReportedGprsNoReg = true;
      }
      mStartedGprsRegCheck = false;
      break;
    case 21: 
      ??? = (AsyncResult)obj;
      if (userObj != null)
      {
        forMessageuserObj).exception = exception;
        ((Message)userObj).sendToTarget();
      }
      break;
    case 20: 
      ??? = obtainMessage(21, obj).userObj);
      mCi.setPreferredNetworkType(mPreferredNetworkType, ???);
      break;
    case 19: 
      ??? = (AsyncResult)obj;
      if (exception == null) {
        mPreferredNetworkType = ((int[])result)[0];
      } else {
        mPreferredNetworkType = 7;
      }
      ??? = obtainMessage(20, userObj);
      mCi.setPreferredNetworkType(7, ???);
      break;
    case 18: 
      if (obj).exception == null) {
        ((NetworkRegistrationManager)mRegStateManagers.get(1)).getNetworkRegistrationState(1, obtainMessage(15, null));
      }
      break;
    case 17: 
      mOnSubscriptionsChangedListener.mPreviousSubId.set(-1);
      mPrevSubId = -1;
      mIsSimReady = true;
      pollState();
      queueNextSignalStrengthPoll();
      break;
    case 16: 
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("EVENT_SIM_RECORDS_LOADED: what=");
      ((StringBuilder)localObject3).append(what);
      log(((StringBuilder)localObject3).toString());
      mRoamAlias = RoamAliasAsus.getMatchRoamAlias(getHomeOperatorNumeric());
      pollState();
      updatePhoneObject();
      updateOtaspState();
      if (mPhone.isPhoneTypeGsm()) {
        updateSpnDisplay();
      }
      break;
    case 15: 
      ??? = (AsyncResult)obj;
      if (exception == null)
      {
        ??? = ((NetworkRegistrationState)result).getCellIdentity();
        processCellLocationInfo(mCellLoc, ???);
        mPhone.notifyLocationChanged();
      }
      disableSingleLocationUpdate();
      break;
    case 14: 
      log("EVENT_POLL_STATE_NETWORK_SELECTION_MODE");
      localObject3 = (AsyncResult)obj;
      if (mPhone.isPhoneTypeGsm()) {
        handlePollStateResult(what, (AsyncResult)localObject3);
      } else if ((exception == null) && (result != null))
      {
        if (((int[])result)[0] == 1) {
          mPhone.setNetworkSelectionModeAutomatic(null);
        }
      }
      else {
        log("Unable to getNetworkSelectionMode");
      }
      break;
    case 12: 
      ??? = (AsyncResult)obj;
      mDontPollSignalStrength = true;
      onSignalStrengthResult(???);
      break;
    case 11: 
      ??? = (AsyncResult)obj;
      setTimeFromNITZString((String)((Object[])result)[0], ((Long)((Object[])result)[1]).longValue());
      break;
    case 10: 
      mCi.getSignalStrength(obtainMessage(3));
      break;
    case 4: 
    case 5: 
    case 6: 
      localObject3 = (AsyncResult)obj;
      handlePollStateResult(what, (AsyncResult)localObject3);
      break;
    case 3: 
      if (!mCi.getRadioState().isOn()) {
        return;
      }
      onSignalStrengthResult((AsyncResult)obj);
      queueNextSignalStrengthPoll();
      break;
    case 2: 
      modemTriggeredPollState();
      break;
    case 1: 
    case 50: 
      if ((!mPhone.isPhoneTypeGsm()) && (mCi.getRadioState() == CommandsInterface.RadioState.RADIO_ON))
      {
        handleCdmaSubscriptionSource(mCdmaSSM.getCdmaSubscriptionSource());
        queueNextSignalStrengthPoll();
      }
      setPowerStateToDesired();
      modemTriggeredPollState();
    }
  }
  
  protected void handlePollStateResult(int paramInt, AsyncResult paramAsyncResult)
  {
    if (userObj != mPollingContext) {
      return;
    }
    Object localObject;
    if (exception != null)
    {
      localObject = null;
      if ((exception instanceof IllegalStateException))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("handlePollStateResult exception ");
        localStringBuilder.append(exception);
        log(localStringBuilder.toString());
      }
      if ((exception instanceof CommandException)) {
        localObject = ((CommandException)exception).getCommandError();
      }
      if (localObject == CommandException.Error.RADIO_NOT_AVAILABLE)
      {
        cancelPollState();
        return;
      }
      if (localObject != CommandException.Error.OP_NOT_ALLOWED_BEFORE_REG_NW)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("RIL implementation has returned an error where it must succeed");
        ((StringBuilder)localObject).append(exception);
        loge(((StringBuilder)localObject).toString());
      }
    }
    else
    {
      try
      {
        handlePollStateResultMessage(paramInt, paramAsyncResult);
      }
      catch (RuntimeException paramAsyncResult)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Exception while polling service state. Probably malformed RIL response.");
        ((StringBuilder)localObject).append(paramAsyncResult);
        loge(((StringBuilder)localObject).toString());
      }
    }
    paramAsyncResult = mPollingContext;
    paramInt = 0;
    paramAsyncResult[0] -= 1;
    if (mPollingContext[0] == 0)
    {
      mNewSS.setEmergencyOnly(mEmergencyOnly);
      if (mPhone.isPhoneTypeGsm())
      {
        updateRoamingState();
      }
      else
      {
        boolean bool1 = false;
        boolean bool2 = bool1;
        if (!isSidsAllZeros())
        {
          bool2 = bool1;
          if (isHomeSid(mNewSS.getCdmaSystemId())) {
            bool2 = true;
          }
        }
        if (mIsSubscriptionFromRuim)
        {
          bool1 = isRoamingBetweenOperators(mNewSS.getVoiceRoaming(), mNewSS);
          if (bool1 != mNewSS.getVoiceRoaming())
          {
            paramAsyncResult = new StringBuilder();
            paramAsyncResult.append("isRoamingBetweenOperators=");
            paramAsyncResult.append(bool1);
            paramAsyncResult.append(". Override CDMA voice roaming to ");
            paramAsyncResult.append(bool1);
            log(paramAsyncResult.toString());
            mNewSS.setVoiceRoaming(bool1);
          }
        }
        if (ServiceState.isCdma(mNewSS.getRilDataRadioTechnology()))
        {
          if (mNewSS.getVoiceRegState() == 0) {
            paramInt = 1;
          }
          if (paramInt != 0)
          {
            bool1 = mNewSS.getVoiceRoaming();
            if (mNewSS.getDataRoaming() != bool1)
            {
              paramAsyncResult = new StringBuilder();
              paramAsyncResult.append("Data roaming != Voice roaming. Override data roaming to ");
              paramAsyncResult.append(bool1);
              log(paramAsyncResult.toString());
              mNewSS.setDataRoaming(bool1);
            }
          }
          else
          {
            bool1 = isRoamIndForHomeSystem(Integer.toString(mRoamingIndicator));
            if (mNewSS.getDataRoaming() == bool1)
            {
              paramAsyncResult = new StringBuilder();
              paramAsyncResult.append("isRoamIndForHomeSystem=");
              paramAsyncResult.append(bool1);
              paramAsyncResult.append(", override data roaming to ");
              paramAsyncResult.append(bool1 ^ true);
              log(paramAsyncResult.toString());
              mNewSS.setDataRoaming(bool1 ^ true);
            }
          }
        }
        mNewSS.setCdmaDefaultRoamingIndicator(mDefaultRoamingIndicator);
        mNewSS.setCdmaRoamingIndicator(mRoamingIndicator);
        bool1 = true;
        if (TextUtils.isEmpty(mPrlVersion)) {
          bool1 = false;
        }
        if ((bool1) && (mNewSS.getRilVoiceRadioTechnology() != 0))
        {
          if (!isSidsAllZeros()) {
            if ((!bool2) && (!mIsInPrl)) {
              mNewSS.setCdmaRoamingIndicator(mDefaultRoamingIndicator);
            } else if ((bool2) && (!mIsInPrl))
            {
              if (ServiceState.isLte(mNewSS.getRilVoiceRadioTechnology()))
              {
                log("Turn off roaming indicator as voice is LTE");
                mNewSS.setCdmaRoamingIndicator(1);
              }
              else
              {
                mNewSS.setCdmaRoamingIndicator(2);
              }
            }
            else if ((!bool2) && (mIsInPrl)) {
              mNewSS.setCdmaRoamingIndicator(mRoamingIndicator);
            } else if (mRoamingIndicator <= 2) {
              mNewSS.setCdmaRoamingIndicator(1);
            } else {
              mNewSS.setCdmaRoamingIndicator(mRoamingIndicator);
            }
          }
        }
        else
        {
          log("Turn off roaming indicator if !isPrlLoaded or voice RAT is unknown");
          mNewSS.setCdmaRoamingIndicator(1);
        }
        paramInt = mNewSS.getCdmaRoamingIndicator();
        mNewSS.setCdmaEriIconIndex(mPhone.mEriManager.getCdmaEriIconIndex(paramInt, mDefaultRoamingIndicator));
        mNewSS.setCdmaEriIconMode(mPhone.mEriManager.getCdmaEriIconMode(paramInt, mDefaultRoamingIndicator));
        paramAsyncResult = new StringBuilder();
        paramAsyncResult.append("Set CDMA Roaming Indicator to: ");
        paramAsyncResult.append(mNewSS.getCdmaRoamingIndicator());
        paramAsyncResult.append(". voiceRoaming = ");
        paramAsyncResult.append(mNewSS.getVoiceRoaming());
        paramAsyncResult.append(". dataRoaming = ");
        paramAsyncResult.append(mNewSS.getDataRoaming());
        paramAsyncResult.append(", isPrlLoaded = ");
        paramAsyncResult.append(bool1);
        paramAsyncResult.append(". namMatch = ");
        paramAsyncResult.append(bool2);
        paramAsyncResult.append(" , mIsInPrl = ");
        paramAsyncResult.append(mIsInPrl);
        paramAsyncResult.append(", mRoamingIndicator = ");
        paramAsyncResult.append(mRoamingIndicator);
        paramAsyncResult.append(", mDefaultRoamingIndicator= ");
        paramAsyncResult.append(mDefaultRoamingIndicator);
        log(paramAsyncResult.toString());
      }
      pollStateDone();
    }
  }
  
  protected void handlePollStateResultMessage(int paramInt, AsyncResult paramAsyncResult)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    boolean bool;
    if (paramInt != 14)
    {
      int i;
      int j;
      int k;
      switch (paramInt)
      {
      default: 
        paramAsyncResult = new StringBuilder();
        paramAsyncResult.append("handlePollStateResultMessage: Unexpected RIL response received: ");
        paramAsyncResult.append(paramInt);
        loge(paramAsyncResult.toString());
        break;
      case 6: 
        if (mPhone.isPhoneTypeGsm())
        {
          localObject1 = (String[])result;
          if ((localObject1 != null) && (localObject1.length >= 3))
          {
            paramAsyncResult = (AsyncResult)localObject2;
            if (mUiccController.getUiccCard(getPhoneId()) != null) {
              paramAsyncResult = mUiccController.getUiccCard(getPhoneId()).getOperatorBrandOverride();
            }
            if (paramAsyncResult != null)
            {
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("EVENT_POLL_STATE_OPERATOR: use brandOverride=");
              ((StringBuilder)localObject2).append(paramAsyncResult);
              log(((StringBuilder)localObject2).toString());
              mNewSS.setOperatorName(paramAsyncResult, paramAsyncResult, localObject1[2]);
            }
            else
            {
              mNewSS.setOperatorName(localObject1[0], localObject1[1], localObject1[2]);
              paramAsyncResult = PlmnTableAsus.GetCustomPlmn(localObject1[2], localObject1[1]);
              if (!TextUtils.isEmpty(paramAsyncResult)) {
                mNewSS.setOperatorName(paramAsyncResult, localObject1[1], localObject1[2]);
              }
            }
          }
        }
        else
        {
          localObject2 = (String[])result;
          if ((localObject2 != null) && (localObject2.length >= 3))
          {
            if ((localObject2[2] == null) || (localObject2[2].length() < 5) || ("00000".equals(localObject2[2])))
            {
              localObject2[2] = SystemProperties.get("ro.cdma.home.operator.numeric", "00000");
              paramAsyncResult = new StringBuilder();
              paramAsyncResult.append("RIL_REQUEST_OPERATOR.response[2], the numeric,  is bad. Using SystemProperties 'ro.cdma.home.operator.numeric'= ");
              paramAsyncResult.append(localObject2[2]);
              log(paramAsyncResult.toString());
            }
            if (!mIsSubscriptionFromRuim)
            {
              mNewSS.setOperatorName(localObject2[0], localObject2[1], localObject2[2]);
            }
            else
            {
              paramAsyncResult = (AsyncResult)localObject1;
              if (mUiccController.getUiccCard(getPhoneId()) != null) {
                paramAsyncResult = mUiccController.getUiccCard(getPhoneId()).getOperatorBrandOverride();
              }
              if (paramAsyncResult != null)
              {
                mNewSS.setOperatorName(paramAsyncResult, paramAsyncResult, localObject2[2]);
              }
              else
              {
                mNewSS.setOperatorName(localObject2[0], localObject2[1], localObject2[2]);
                paramAsyncResult = PlmnTableAsus.GetCustomPlmn(localObject2[2], localObject2[1]);
                if (!TextUtils.isEmpty(paramAsyncResult)) {
                  mNewSS.setOperatorName(paramAsyncResult, localObject2[1], localObject2[2]);
                }
              }
            }
          }
          else
          {
            log("EVENT_POLL_STATE_OPERATOR_CDMA: error parsing opNames");
          }
        }
        break;
      case 5: 
        paramAsyncResult = (NetworkRegistrationState)result;
        localObject1 = paramAsyncResult.getDataSpecificStates();
        i = paramAsyncResult.getRegState();
        j = regCodeToServiceState(i);
        k = ServiceState.networkTypeToRilRadioTechnology(paramAsyncResult.getAccessNetworkTechnology());
        mNewSS.setDataRegState(j);
        mNewSS.setRilDataRadioTechnology(k);
        mNewSS.addNetworkRegistrationState(paramAsyncResult);
        if (j == 1) {
          mLastPhysicalChannelConfigList = null;
        }
        setPhyCellInfoFromCellIdentity(mNewSS, paramAsyncResult.getCellIdentity());
        if (mPhone.isPhoneTypeGsm())
        {
          mNewReasonDataDenied = paramAsyncResult.getReasonForDenial();
          mNewMaxDataCalls = maxDataCalls;
          mDataRoaming = regCodeIsRoaming(i);
          mNewSS.setDataRoamingFromRegistration(mDataRoaming);
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("handlPollStateResultMessage: GsmSST dataServiceState=");
          ((StringBuilder)localObject1).append(j);
          ((StringBuilder)localObject1).append(" regState=");
          ((StringBuilder)localObject1).append(i);
          ((StringBuilder)localObject1).append(" dataRadioTechnology=");
          ((StringBuilder)localObject1).append(k);
          log(((StringBuilder)localObject1).toString());
        }
        else if (mPhone.isPhoneTypeCdma())
        {
          bool = regCodeIsRoaming(i);
          mNewSS.setDataRoaming(bool);
          mNewSS.setDataRoamingFromRegistration(bool);
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("handlPollStateResultMessage: cdma dataServiceState=");
          ((StringBuilder)localObject1).append(j);
          ((StringBuilder)localObject1).append(" regState=");
          ((StringBuilder)localObject1).append(i);
          ((StringBuilder)localObject1).append(" dataRadioTechnology=");
          ((StringBuilder)localObject1).append(k);
          log(((StringBuilder)localObject1).toString());
        }
        else
        {
          paramInt = mSS.getRilDataRadioTechnology();
          if (((paramInt == 0) && (k != 0)) || ((ServiceState.isCdma(paramInt)) && (ServiceState.isLte(k))) || ((ServiceState.isLte(paramInt)) && (ServiceState.isCdma(k)))) {
            mCi.getSignalStrength(obtainMessage(3));
          }
          bool = regCodeIsRoaming(i);
          mNewSS.setDataRoaming(bool);
          mNewSS.setDataRoamingFromRegistration(bool);
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("handlPollStateResultMessage: CdmaLteSST dataServiceState=");
          ((StringBuilder)localObject1).append(j);
          ((StringBuilder)localObject1).append(" registrationState=");
          ((StringBuilder)localObject1).append(i);
          ((StringBuilder)localObject1).append(" dataRadioTechnology=");
          ((StringBuilder)localObject1).append(k);
          log(((StringBuilder)localObject1).toString());
        }
        updateServiceStateLteEarfcnBoost(mNewSS, getLteEarfcn(paramAsyncResult.getCellIdentity()));
        break;
      case 4: 
        paramAsyncResult = (NetworkRegistrationState)result;
        localObject1 = paramAsyncResult.getVoiceSpecificStates();
        int m = paramAsyncResult.getRegState();
        paramInt = cssSupported;
        int n = ServiceState.networkTypeToRilRadioTechnology(paramAsyncResult.getAccessNetworkTechnology());
        mNewSS.setVoiceRegState(regCodeToServiceState(m));
        mNewSS.setCssIndicator(paramInt);
        mNewSS.setRilVoiceRadioTechnology(n);
        mNewSS.addNetworkRegistrationState(paramAsyncResult);
        setPhyCellInfoFromCellIdentity(mNewSS, paramAsyncResult.getCellIdentity());
        int i1 = paramAsyncResult.getReasonForDenial();
        mEmergencyOnly = paramAsyncResult.isEmergencyEnabled();
        if (mPhone.isPhoneTypeGsm())
        {
          mGsmRoaming = regCodeIsRoaming(m);
          mNewRejectCode = i1;
          mPhone.getContext().getResources().getBoolean(17957076);
        }
        else
        {
          paramInt = roamingIndicator;
          i = systemIsInPrl;
          k = defaultRoamingIndicator;
          mRegistrationState = m;
          if ((regCodeIsRoaming(m)) && (!isRoamIndForHomeSystem(Integer.toString(paramInt)))) {
            bool = true;
          } else {
            bool = false;
          }
          mNewSS.setVoiceRoaming(bool);
          mRoamingIndicator = paramInt;
          if (i == 0) {
            bool = false;
          } else {
            bool = true;
          }
          mIsInPrl = bool;
          mDefaultRoamingIndicator = k;
          k = 0;
          j = 0;
          localObject1 = paramAsyncResult.getCellIdentity();
          i = k;
          paramInt = j;
          if (localObject1 != null)
          {
            i = k;
            paramInt = j;
            if (((CellIdentity)localObject1).getType() == 2)
            {
              i = ((CellIdentityCdma)localObject1).getSystemId();
              paramInt = ((CellIdentityCdma)localObject1).getNetworkId();
            }
          }
          mNewSS.setCdmaSystemAndNetworkId(i, paramInt);
          if (i1 == 0) {
            mRegistrationDeniedReason = "General";
          } else if (i1 == 1) {
            mRegistrationDeniedReason = "Authentication Failure";
          } else {
            mRegistrationDeniedReason = "";
          }
          if (mRegistrationState == 3)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Registration denied, ");
            ((StringBuilder)localObject1).append(mRegistrationDeniedReason);
            log(((StringBuilder)localObject1).toString());
          }
        }
        processCellLocationInfo(mNewCellLoc, paramAsyncResult.getCellIdentity());
        paramAsyncResult = new StringBuilder();
        paramAsyncResult.append("handlPollVoiceRegResultMessage: regState=");
        paramAsyncResult.append(m);
        paramAsyncResult.append(" radioTechnology=");
        paramAsyncResult.append(n);
        log(paramAsyncResult.toString());
        break;
      }
    }
    else
    {
      localObject1 = (int[])result;
      paramAsyncResult = mNewSS;
      if (localObject1[0] == 1) {
        bool = true;
      } else {
        bool = false;
      }
      paramAsyncResult.setIsManualSelection(bool);
      if ((localObject1[0] == 1) && (mPhone.shouldForceAutoNetworkSelect()))
      {
        mPhone.setNetworkSelectionModeAutomatic(null);
        log(" Forcing Automatic Network Selection, manual selection is not allowed");
      }
    }
  }
  
  protected void hangupAndPowerOff()
  {
    if ((!mPhone.isPhoneTypeGsm()) || (mPhone.isInCall()))
    {
      mPhone.mCT.mRingingCall.hangupIfAlive();
      mPhone.mCT.mBackgroundCall.hangupIfAlive();
      mPhone.mCT.mForegroundCall.hangupIfAlive();
    }
    mCi.setRadioPower(false, obtainMessage(54));
  }
  
  protected boolean inSameCountry(String paramString)
  {
    if ((!TextUtils.isEmpty(paramString)) && (paramString.length() >= 5))
    {
      String str1 = getHomeOperatorNumeric();
      if ((!TextUtils.isEmpty(str1)) && (str1.length() >= 5))
      {
        String str2 = paramString.substring(0, 3);
        paramString = str1.substring(0, 3);
        str2 = MccTable.countryCodeForMcc(Integer.parseInt(str2));
        paramString = MccTable.countryCodeForMcc(Integer.parseInt(paramString));
        if ((!str2.isEmpty()) && (!paramString.isEmpty()))
        {
          boolean bool1 = paramString.equals(str2);
          if (bool1) {
            return bool1;
          }
          boolean bool2;
          if (("us".equals(paramString)) && ("vi".equals(str2)))
          {
            bool2 = true;
          }
          else
          {
            bool2 = bool1;
            if ("vi".equals(paramString))
            {
              bool2 = bool1;
              if ("us".equals(str2)) {
                bool2 = true;
              }
            }
          }
          return bool2;
        }
        return false;
      }
      return false;
    }
    return false;
  }
  
  protected boolean isCallerOnDifferentThread()
  {
    boolean bool;
    if (Thread.currentThread() != getLooper().getThread()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isConcurrentVoiceAndDataAllowed()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ABSP][isConcurrentVoiceAndDataAllowed] mSS.getCssIndicator()=");
    localStringBuilder.append(mSS.getCssIndicator());
    log(localStringBuilder.toString());
    int i = mSS.getCssIndicator();
    boolean bool = true;
    if (i == 1) {
      return true;
    }
    if (mPhone.isPhoneTypeGsm())
    {
      if ((mSS.getRilDataRadioTechnology() < 3) || (mSS.getRilVoiceRadioTechnology() == 16)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public boolean isDeviceShuttingDown()
  {
    return mDeviceShuttingDown;
  }
  
  public boolean isImsRegistered()
  {
    return mImsRegistered;
  }
  
  public boolean isMinInfoReady()
  {
    return mIsMinInfoReady;
  }
  
  protected final boolean isNonRoamingInCdmaNetwork(BaseBundle paramBaseBundle, String paramString)
  {
    return isInNetwork(paramBaseBundle, paramString, "cdma_nonroaming_networks_string_array");
  }
  
  protected final boolean isNonRoamingInGsmNetwork(BaseBundle paramBaseBundle, String paramString)
  {
    return isInNetwork(paramBaseBundle, paramString, "gsm_nonroaming_networks_string_array");
  }
  
  public boolean isRadioOn()
  {
    boolean bool;
    if (mCi.getRadioState() == CommandsInterface.RadioState.RADIO_ON) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRoamAliasEqualNull()
  {
    if (mRoamAlias == null) {
      Rlog.d("SST", "[ABSP][isRoamAliasEqualNull] mRoamAlias is null");
    }
    boolean bool;
    if (mRoamAlias == null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected final boolean isRoamingInCdmaNetwork(BaseBundle paramBaseBundle, String paramString)
  {
    return isInNetwork(paramBaseBundle, paramString, "cdma_roaming_networks_string_array");
  }
  
  protected final boolean isRoamingInGsmNetwork(BaseBundle paramBaseBundle, String paramString)
  {
    return isInNetwork(paramBaseBundle, paramString, "gsm_roaming_networks_string_array");
  }
  
  protected boolean isSidsAllZeros()
  {
    if (mHomeSystemId != null) {
      for (int i = 0; i < mHomeSystemId.length; i++) {
        if (mHomeSystemId[i] != 0) {
          return false;
        }
      }
    }
    return true;
  }
  
  protected final void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.d("SST", localStringBuilder.toString());
  }
  
  protected final void loge(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("] ");
    localStringBuilder.append(paramString);
    Rlog.e("SST", localStringBuilder.toString());
  }
  
  protected void notifyDataRegStateRilRadioTechnologyChanged()
  {
    int i = mSS.getRilDataRadioTechnology();
    int j = mSS.getDataRegState();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("notifyDataRegStateRilRadioTechnologyChanged: drs=");
    localStringBuilder.append(j);
    localStringBuilder.append(" rat=");
    localStringBuilder.append(i);
    log(localStringBuilder.toString());
    mPhone.setSystemProperty("gsm.network.type", ServiceState.rilRadioTechnologyToString(i));
    mDataRegStateOrRatChangedRegistrants.notifyResult(new Pair(Integer.valueOf(j), Integer.valueOf(i)));
  }
  
  protected boolean notifySignalStrength()
  {
    boolean bool1 = false;
    boolean bool2 = false;
    if (!mSignalStrength.equals(mLastSignalStrength))
    {
      bool1 = bool2;
      try
      {
        mPhone.notifySignalStrength();
        bool2 = true;
        bool1 = true;
        mLastSignalStrength = mSignalStrength;
        bool1 = bool2;
      }
      catch (NullPointerException localNullPointerException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("updateSignalStrength() Phone already destroyed: ");
        localStringBuilder.append(localNullPointerException);
        localStringBuilder.append("SignalStrength not notified");
        loge(localStringBuilder.toString());
      }
    }
    return bool1;
  }
  
  public void onImsCapabilityChanged()
  {
    sendMessage(obtainMessage(48));
  }
  
  public void onImsServiceStateChanged()
  {
    sendMessage(obtainMessage(53));
  }
  
  protected boolean onSignalStrengthResult(AsyncResult paramAsyncResult)
  {
    boolean bool1 = false;
    int i = mSS.getRilDataRadioTechnology();
    int j = mSS.getRilVoiceRadioTechnology();
    boolean bool2;
    if ((i == 18) || (!ServiceState.isGsm(i)))
    {
      bool2 = bool1;
      if (j != 18)
      {
        bool2 = bool1;
        if (!ServiceState.isGsm(j)) {}
      }
    }
    else
    {
      bool2 = true;
    }
    if ((exception == null) && (result != null))
    {
      mSignalStrength = ((SignalStrength)result);
      mSignalStrength.validateInput();
      if ((i == 0) && (j == 0)) {
        mSignalStrength.fixType();
      } else {
        mSignalStrength.setGsm(bool2);
      }
      mSignalStrength.setLteRsrpBoost(mSS.getLteEarfcnRsrpBoost());
      paramAsyncResult = getCarrierConfig();
      mSignalStrength.setUseOnlyRsrpForLteLevel(paramAsyncResult.getBoolean("use_only_rsrp_for_lte_signal_bar_bool"));
      mSignalStrength.setLteRsrpThresholds(paramAsyncResult.getIntArray("lte_rsrp_thresholds_int_array"));
      mSignalStrength.setWcdmaDefaultSignalMeasurement(paramAsyncResult.getString("wcdma_default_signal_strength_measurement_string"));
      mSignalStrength.setWcdmaRscpThresholds(paramAsyncResult.getIntArray("wcdma_rscp_thresholds_int_array"));
      mCi.logSignal(mSignalStrength);
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onSignalStrengthResult() Exception from RIL : ");
      localStringBuilder.append(exception);
      log(localStringBuilder.toString());
      mSignalStrength = new SignalStrength(bool2);
    }
    return notifySignalStrength();
  }
  
  protected void onUpdateIccAvailability()
  {
    if (mUiccController == null) {
      return;
    }
    UiccCardApplication localUiccCardApplication = getUiccCardApplication();
    if (mUiccApplcation != localUiccCardApplication)
    {
      if (mUiccApplcation != null)
      {
        log("Removing stale icc objects.");
        updateSpnDisplay();
        mUiccApplcation.unregisterForReady(this);
        if (mIccRecords != null) {
          mIccRecords.unregisterForRecordsLoaded(this);
        }
        mIccRecords = null;
        mUiccApplcation = null;
        mRoamAlias = null;
      }
      if (localUiccCardApplication != null)
      {
        log("New card found");
        mUiccApplcation = localUiccCardApplication;
        mIccRecords = mUiccApplcation.getIccRecords();
        if (mPhone.isPhoneTypeGsm())
        {
          mUiccApplcation.registerForReady(this, 17, null);
          if (mIccRecords != null) {
            mIccRecords.registerForRecordsLoaded(this, 16, null);
          }
        }
        else if (mIsSubscriptionFromRuim)
        {
          mUiccApplcation.registerForReady(this, 26, null);
          if (mIccRecords != null) {
            mIccRecords.registerForRecordsLoaded(this, 27, null);
          }
        }
      }
    }
  }
  
  protected void parseSidNid(String paramString1, String paramString2)
  {
    int i = 0;
    int j;
    if (paramString1 != null)
    {
      localObject = paramString1.split(",");
      mHomeSystemId = new int[localObject.length];
      for (j = 0; j < localObject.length; j++) {
        try
        {
          mHomeSystemId[j] = Integer.parseInt(localObject[j]);
        }
        catch (NumberFormatException localNumberFormatException1)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("error parsing system id: ");
          localStringBuilder.append(localNumberFormatException1);
          loge(localStringBuilder.toString());
        }
      }
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("CDMA_SUBSCRIPTION: SID=");
    ((StringBuilder)localObject).append(paramString1);
    log(((StringBuilder)localObject).toString());
    if (paramString2 != null)
    {
      paramString1 = paramString2.split(",");
      mHomeNetworkId = new int[paramString1.length];
      for (j = i; j < paramString1.length; j++) {
        try
        {
          mHomeNetworkId[j] = Integer.parseInt(paramString1[j]);
        }
        catch (NumberFormatException localNumberFormatException2)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("CDMA_SUBSCRIPTION: error parsing network id: ");
          ((StringBuilder)localObject).append(localNumberFormatException2);
          loge(((StringBuilder)localObject).toString());
        }
      }
    }
    paramString1 = new StringBuilder();
    paramString1.append("CDMA_SUBSCRIPTION: NID=");
    paramString1.append(paramString2);
    log(paramString1.toString());
  }
  
  public void pollState()
  {
    pollState(false);
  }
  
  public void pollState(boolean paramBoolean)
  {
    mPollingContext = new int[1];
    mPollingContext[0] = 0;
    int i = 0;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("pollState: modemTriggered=");
    ((StringBuilder)localObject).append(paramBoolean);
    log(((StringBuilder)localObject).toString());
    switch (2.$SwitchMap$com$android$internal$telephony$CommandsInterface$RadioState[mCi.getRadioState().ordinal()])
    {
    default: 
      break;
    case 2: 
      int j = 1;
      radio_off_count += 1;
      mNewSS.setStateOff();
      mNewCellLoc.setStateInvalid();
      setSignalStrengthDefaultValues();
      mNitzState.handleNetworkUnavailable();
      if ((!mDeviceShuttingDown) && ((paramBoolean) || (18 == mSS.getRilDataRadioTechnology())))
      {
        i = j;
        if (paramBoolean)
        {
          i = j;
          if (radio_off_count == 1)
          {
            log("[ABSP][pollState] modemTriggered and radio_off_count[1]");
            pollStateDone();
            i = j;
          }
        }
      }
      else
      {
        pollStateDone();
      }
      break;
    case 1: 
      mNewSS.setStateOutOfService();
      mNewCellLoc.setStateInvalid();
      setSignalStrengthDefaultValues();
      mNitzState.handleNetworkUnavailable();
      pollStateDone();
      break;
    }
    if (i == 0)
    {
      radio_off_count = 0;
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("[ABSP][pollState] set radio_off_count[");
      ((StringBuilder)localObject).append(radio_off_count);
      ((StringBuilder)localObject).append("]");
      log(((StringBuilder)localObject).toString());
    }
    localObject = mPollingContext;
    localObject[0] += 1;
    ((NetworkRegistrationManager)mRegStateManagers.get(1)).getNetworkRegistrationState(2, obtainMessage(5, mPollingContext));
    localObject = mPollingContext;
    localObject[0] += 1;
    ((NetworkRegistrationManager)mRegStateManagers.get(1)).getNetworkRegistrationState(1, obtainMessage(4, mPollingContext));
    if (mPhone.isPhoneTypeGsm())
    {
      localObject = mPollingContext;
      localObject[0] += 1;
      mCi.getNetworkSelectionMode(obtainMessage(14, mPollingContext));
    }
    localObject = mPollingContext;
    localObject[0] += 1;
    mCi.getOperator(obtainMessage(6, mPollingContext));
  }
  
  public void powerOffRadioSafely(DcTracker paramDcTracker)
  {
    try
    {
      if (!mPendingRadioPowerOffAfterDataOff)
      {
        int i = SubscriptionManager.getDefaultDataSubscriptionId();
        if ((paramDcTracker.isDisconnected()) && ((i == mPhone.getSubId()) || ((i != mPhone.getSubId()) && (ProxyController.getInstance().isDataDisconnected(i)))))
        {
          paramDcTracker.cleanUpAllConnections("radioTurnedOff");
          log("Data disconnected, turn off radio right away.");
          hangupAndPowerOff();
        }
        else
        {
          if ((mPhone.isPhoneTypeGsm()) && (mPhone.isInCall()))
          {
            mPhone.mCT.mRingingCall.hangupIfAlive();
            mPhone.mCT.mBackgroundCall.hangupIfAlive();
            mPhone.mCT.mForegroundCall.hangupIfAlive();
          }
          paramDcTracker.cleanUpAllConnections("radioTurnedOff");
          if ((i != mPhone.getSubId()) && (!ProxyController.getInstance().isDataDisconnected(i)))
          {
            log("Data is active on DDS.  Wait for all data disconnect");
            ProxyController.getInstance().registerForAllDataDisconnected(i, this, 49, null);
            mPendingRadioPowerOffAfterDataOff = true;
          }
          paramDcTracker = Message.obtain(this);
          what = 38;
          i = mPendingRadioPowerOffAfterDataOffTag + 1;
          mPendingRadioPowerOffAfterDataOffTag = i;
          arg1 = i;
          if (sendMessageDelayed(paramDcTracker, 30000L))
          {
            log("Wait upto 30s for data to disconnect, then turn off radio.");
            mPendingRadioPowerOffAfterDataOff = true;
          }
          else
          {
            log("Cannot send delayed Msg, turn off radio right away.");
            hangupAndPowerOff();
            mPendingRadioPowerOffAfterDataOff = false;
          }
        }
      }
      return;
    }
    finally {}
  }
  
  public boolean processPendingRadioPowerOffAfterDataOff()
  {
    try
    {
      if (mPendingRadioPowerOffAfterDataOff)
      {
        log("Process pending request to turn radio off.");
        mPendingRadioPowerOffAfterDataOffTag += 1;
        hangupAndPowerOff();
        mPendingRadioPowerOffAfterDataOff = false;
        return true;
      }
      return false;
    }
    finally {}
  }
  
  public void reRegisterNetwork(Message paramMessage)
  {
    mCi.getPreferredNetworkType(obtainMessage(19, paramMessage));
  }
  
  public void registerForDataConnectionAttached(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mAttachedRegistrants.add(paramHandler);
    if (getCurrentDataConnectionState() == 0) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForDataConnectionDetached(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mDetachedRegistrants.add(paramHandler);
    if (getCurrentDataConnectionState() != 0) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForDataRegStateOrRatChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mDataRegStateOrRatChangedRegistrants.add(paramHandler);
    notifyDataRegStateRilRadioTechnologyChanged();
  }
  
  public void registerForDataRoamingOff(Handler paramHandler, int paramInt, Object paramObject, boolean paramBoolean)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mDataRoamingOffRegistrants.add(paramHandler);
    if ((paramBoolean) && (!mSS.getDataRoaming())) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForDataRoamingOn(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mDataRoamingOnRegistrants.add(paramHandler);
    if (mSS.getDataRoaming()) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForNetworkAttached(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mNetworkAttachedRegistrants.add(paramHandler);
    if (mSS.getVoiceRegState() == 0) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForNetworkDetached(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mNetworkDetachedRegistrants.add(paramHandler);
    if (mSS.getVoiceRegState() != 0) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForPsRestrictedDisabled(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mPsRestrictDisabledRegistrants.add(paramHandler);
    if (mRestrictedState.isPsRestricted()) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForPsRestrictedEnabled(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mPsRestrictEnabledRegistrants.add(paramHandler);
    if (mRestrictedState.isPsRestricted()) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForSubscriptionInfoReady(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mCdmaForSubscriptionInfoReadyRegistrants.add(paramHandler);
    if (isMinInfoReady()) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForVoiceRoamingOff(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoiceRoamingOffRegistrants.add(paramHandler);
    if (!mSS.getVoiceRoaming()) {
      paramHandler.notifyRegistrant();
    }
  }
  
  public void registerForVoiceRoamingOn(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mVoiceRoamingOnRegistrants.add(paramHandler);
    if (mSS.getVoiceRoaming()) {
      paramHandler.notifyRegistrant();
    }
  }
  
  @VisibleForTesting
  public void requestShutdown()
  {
    if (mDeviceShuttingDown == true) {
      return;
    }
    mDeviceShuttingDown = true;
    mDesiredPowerState = false;
    setPowerStateToDesired();
  }
  
  protected void resetServiceStateInIwlanMode()
  {
    if (mCi.getRadioState() == CommandsInterface.RadioState.RADIO_OFF)
    {
      int i = 0;
      log("set service state as POWER_OFF");
      if (18 == mNewSS.getRilDataRadioTechnology())
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("pollStateDone: mNewSS = ");
        ((StringBuilder)localObject).append(mNewSS);
        log(((StringBuilder)localObject).toString());
        log("pollStateDone: reset iwlan RAT value");
        i = 1;
      }
      Object localObject = mNewSS.getOperatorAlphaLong();
      mNewSS.setStateOff();
      if (i != 0)
      {
        mNewSS.setRilDataRadioTechnology(18);
        mNewSS.setDataRegState(0);
        mNewSS.setOperatorAlphaLong((String)localObject);
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("pollStateDone: mNewSS = ");
        ((StringBuilder)localObject).append(mNewSS);
        log(((StringBuilder)localObject).toString());
      }
    }
  }
  
  public void setImsRegistrationState(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ImsRegistrationState - registered : ");
    localStringBuilder.append(paramBoolean);
    log(localStringBuilder.toString());
    if ((mImsRegistrationOnOff) && (!paramBoolean) && (mAlarmSwitch))
    {
      mImsRegistrationOnOff = paramBoolean;
      ((AlarmManager)mPhone.getContext().getSystemService("alarm")).cancel(mRadioOffIntent);
      mAlarmSwitch = false;
      sendMessage(obtainMessage(45));
      return;
    }
    mImsRegistrationOnOff = paramBoolean;
  }
  
  @VisibleForTesting
  public void setNotification(int paramInt)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("setNotification: create notification ");
    ((StringBuilder)localObject1).append(paramInt);
    log(((StringBuilder)localObject1).toString());
    if (!SubscriptionManager.isValidSubscriptionId(mSubId))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("cannot setNotification on invalid subid mSubId=");
      ((StringBuilder)localObject1).append(mSubId);
      loge(((StringBuilder)localObject1).toString());
      return;
    }
    if (!mPhone.getContext().getResources().getBoolean(17957075))
    {
      log("Ignore all the notifications");
      return;
    }
    Context localContext = mPhone.getContext();
    localObject1 = (CarrierConfigManager)localContext.getSystemService("carrier_config");
    if (localObject1 != null)
    {
      localObject1 = ((CarrierConfigManager)localObject1).getConfig();
      if ((localObject1 != null) && (((PersistableBundle)localObject1).getBoolean("disable_voice_barring_notification_bool", false)) && ((paramInt == 1003) || (paramInt == 1005) || (paramInt == 1006)))
      {
        log("Voice/emergency call barred notification disabled");
        return;
      }
    }
    localObject1 = "";
    Object localObject2 = "";
    int i = 999;
    int j = 17301642;
    boolean bool;
    if (((TelephonyManager)mPhone.getContext().getSystemService("phone")).getPhoneCount() > 1) {
      bool = true;
    } else {
      bool = false;
    }
    int k = mSubscriptionController.getSlotIndex(mSubId) + 1;
    if (paramInt != 2001)
    {
      switch (paramInt)
      {
      default: 
        break;
      case 1006: 
        localObject2 = localContext.getText(17039413);
        if (bool) {
          localObject1 = localContext.getString(17039416, new Object[] { Integer.valueOf(k) });
        } else {
          localObject1 = localContext.getText(17039415);
        }
        break;
      case 1005: 
        localObject2 = localContext.getText(17039414);
        if (bool) {
          localObject1 = localContext.getString(17039416, new Object[] { Integer.valueOf(k) });
        } else {
          localObject1 = localContext.getText(17039415);
        }
        break;
      case 1004: 
        break;
      case 1003: 
        localObject2 = localContext.getText(17039411);
        if (bool) {
          localObject1 = localContext.getString(17039416, new Object[] { Integer.valueOf(k) });
        } else {
          localObject1 = localContext.getText(17039415);
        }
        break;
      case 1002: 
        i = 888;
        break;
      case 1001: 
        if (SubscriptionManager.getDefaultDataSubscriptionId() != mPhone.getSubId()) {
          return;
        }
        i = 888;
        localObject2 = localContext.getText(17039412);
        if (bool) {
          localObject1 = localContext.getString(17039416, new Object[] { Integer.valueOf(k) });
        } else {
          localObject1 = localContext.getText(17039415);
        }
        break;
      }
    }
    else
    {
      i = 111;
      k = selectResourceForRejectCode(mRejectCode, bool);
      if (k == 0)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("setNotification: mRejectCode=");
        ((StringBuilder)localObject1).append(mRejectCode);
        ((StringBuilder)localObject1).append(" is not handled.");
        loge(((StringBuilder)localObject1).toString());
        return;
      }
      j = 17303718;
      localObject2 = localContext.getString(k, new Object[] { Integer.valueOf(mPhone.getPhoneId() + 1) });
      localObject1 = null;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setNotification, create notification, notifyType: ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(", title: ");
    localStringBuilder.append(localObject2);
    localStringBuilder.append(", details: ");
    localStringBuilder.append(localObject1);
    localStringBuilder.append(", subId: ");
    localStringBuilder.append(mSubId);
    localStringBuilder.append(", phoneId: ");
    localStringBuilder.append(mPhone.getPhoneId());
    log(localStringBuilder.toString());
    mNotification = new Notification.Builder(localContext).setWhen(System.currentTimeMillis()).setAutoCancel(true).setSmallIcon(j).setTicker((CharSequence)localObject2).setColor(localContext.getResources().getColor(17170876)).setContentTitle((CharSequence)localObject2).setStyle(new Notification.BigTextStyle().bigText((CharSequence)localObject1)).setContentText((CharSequence)localObject1).setChannel("alert").build();
    localObject1 = (NotificationManager)localContext.getSystemService("notification");
    if ((paramInt != 1002) && (paramInt != 1004))
    {
      j = 0;
      if ((mSS.isEmergencyOnly()) && (paramInt == 1006))
      {
        paramInt = 1;
      }
      else if (paramInt == 2001)
      {
        paramInt = 1;
      }
      else
      {
        paramInt = j;
        if (mSS.getState() == 0) {
          paramInt = 1;
        }
      }
      if (paramInt != 0) {
        ((NotificationManager)localObject1).notify(Integer.toString(mSubId), i, mNotification);
      }
    }
    else
    {
      ((NotificationManager)localObject1).cancel(Integer.toString(mSubId), i);
    }
  }
  
  protected void setOperatorIdd(String paramString)
  {
    paramString = mHbpcdUtils.getIddByMcc(Integer.parseInt(paramString.substring(0, 3)));
    if ((paramString != null) && (!paramString.isEmpty())) {
      mPhone.setGlobalSystemProperty("gsm.operator.idpstring", paramString);
    } else {
      mPhone.setGlobalSystemProperty("gsm.operator.idpstring", "+");
    }
  }
  
  protected void setPowerStateToDesired()
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("mDeviceShuttingDown=");
    ((StringBuilder)localObject).append(mDeviceShuttingDown);
    ((StringBuilder)localObject).append(", mDesiredPowerState=");
    ((StringBuilder)localObject).append(mDesiredPowerState);
    ((StringBuilder)localObject).append(", getRadioState=");
    ((StringBuilder)localObject).append(mCi.getRadioState());
    ((StringBuilder)localObject).append(", mPowerOffDelayNeed=");
    ((StringBuilder)localObject).append(mPowerOffDelayNeed);
    ((StringBuilder)localObject).append(", mAlarmSwitch=");
    ((StringBuilder)localObject).append(mAlarmSwitch);
    ((StringBuilder)localObject).append(", mRadioDisabledByCarrier=");
    ((StringBuilder)localObject).append(mRadioDisabledByCarrier);
    localObject = ((StringBuilder)localObject).toString();
    log((String)localObject);
    mRadioPowerLog.log((String)localObject);
    if ((mPhone.isPhoneTypeGsm()) && (mAlarmSwitch))
    {
      log("mAlarmSwitch == true");
      ((AlarmManager)mPhone.getContext().getSystemService("alarm")).cancel(mRadioOffIntent);
      mAlarmSwitch = false;
    }
    if ((mDesiredPowerState) && (!mRadioDisabledByCarrier) && (mCi.getRadioState() == CommandsInterface.RadioState.RADIO_OFF)) {
      mCi.setRadioPower(true, null);
    } else if (((!mDesiredPowerState) || (mRadioDisabledByCarrier)) && (mCi.getRadioState().isOn()))
    {
      if ((mPhone.isPhoneTypeGsm()) && (mPowerOffDelayNeed))
      {
        if ((mImsRegistrationOnOff) && (!mAlarmSwitch))
        {
          log("mImsRegistrationOnOff == true");
          Context localContext = mPhone.getContext();
          localObject = (AlarmManager)localContext.getSystemService("alarm");
          mRadioOffIntent = PendingIntent.getBroadcast(localContext, 0, new Intent("android.intent.action.ACTION_RADIO_OFF"), 0);
          mAlarmSwitch = true;
          log("Alarm setting");
          ((AlarmManager)localObject).set(2, SystemClock.elapsedRealtime() + 3000L, mRadioOffIntent);
        }
        else
        {
          powerOffRadioSafely(mPhone.mDcTracker);
        }
      }
      else {
        powerOffRadioSafely(mPhone.mDcTracker);
      }
    }
    else if ((mDeviceShuttingDown) && (mCi.getRadioState().isAvailable())) {
      mCi.requestShutdown(null);
    }
  }
  
  public void setRadioPower(boolean paramBoolean)
  {
    mDesiredPowerState = paramBoolean;
    setPowerStateToDesired();
  }
  
  public void setRadioPowerFromCarrier(boolean paramBoolean)
  {
    mRadioDisabledByCarrier = (paramBoolean ^ true);
    setPowerStateToDesired();
  }
  
  protected void setRoamingType(ServiceState paramServiceState)
  {
    int i;
    if (paramServiceState.getVoiceRegState() == 0) {
      i = 1;
    } else {
      i = 0;
    }
    int k;
    if (i != 0) {
      if (paramServiceState.getVoiceRoaming())
      {
        if (mPhone.isPhoneTypeGsm())
        {
          if (inSameCountry(paramServiceState.getVoiceOperatorNumeric())) {
            paramServiceState.setVoiceRoamingType(2);
          } else {
            paramServiceState.setVoiceRoamingType(3);
          }
        }
        else
        {
          int[] arrayOfInt = mPhone.getContext().getResources().getIntArray(17235997);
          if ((arrayOfInt != null) && (arrayOfInt.length > 0))
          {
            paramServiceState.setVoiceRoamingType(2);
            j = paramServiceState.getCdmaRoamingIndicator();
            for (k = 0; k < arrayOfInt.length; k++) {
              if (j == arrayOfInt[k])
              {
                paramServiceState.setVoiceRoamingType(3);
                break;
              }
            }
          }
          else if (inSameCountry(paramServiceState.getVoiceOperatorNumeric()))
          {
            paramServiceState.setVoiceRoamingType(2);
          }
          else
          {
            paramServiceState.setVoiceRoamingType(3);
          }
        }
      }
      else {
        paramServiceState.setVoiceRoamingType(0);
      }
    }
    if (paramServiceState.getDataRegState() == 0) {
      k = 1;
    } else {
      k = 0;
    }
    int j = paramServiceState.getRilDataRadioTechnology();
    if (k != 0) {
      if (!paramServiceState.getDataRoaming()) {
        paramServiceState.setDataRoamingType(0);
      } else if (mPhone.isPhoneTypeGsm())
      {
        if (ServiceState.isGsm(j))
        {
          if (i != 0) {
            paramServiceState.setDataRoamingType(paramServiceState.getVoiceRoamingType());
          } else {
            paramServiceState.setDataRoamingType(1);
          }
        }
        else {
          paramServiceState.setDataRoamingType(1);
        }
      }
      else if (ServiceState.isCdma(j))
      {
        if (i != 0) {
          paramServiceState.setDataRoamingType(paramServiceState.getVoiceRoamingType());
        } else {
          paramServiceState.setDataRoamingType(1);
        }
      }
      else if (inSameCountry(paramServiceState.getDataOperatorNumeric())) {
        paramServiceState.setDataRoamingType(2);
      } else {
        paramServiceState.setDataRoamingType(3);
      }
    }
  }
  
  public void unregisterForDataConnectionAttached(Handler paramHandler)
  {
    mAttachedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDataConnectionDetached(Handler paramHandler)
  {
    mDetachedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDataRegStateOrRatChanged(Handler paramHandler)
  {
    mDataRegStateOrRatChangedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDataRoamingOff(Handler paramHandler)
  {
    mDataRoamingOffRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDataRoamingOn(Handler paramHandler)
  {
    mDataRoamingOnRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNetworkAttached(Handler paramHandler)
  {
    mNetworkAttachedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNetworkDetached(Handler paramHandler)
  {
    mNetworkDetachedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForPsRestrictedDisabled(Handler paramHandler)
  {
    mPsRestrictDisabledRegistrants.remove(paramHandler);
  }
  
  public void unregisterForPsRestrictedEnabled(Handler paramHandler)
  {
    mPsRestrictEnabledRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSubscriptionInfoReady(Handler paramHandler)
  {
    mCdmaForSubscriptionInfoReadyRegistrants.remove(paramHandler);
  }
  
  public void unregisterForVoiceRoamingOff(Handler paramHandler)
  {
    mVoiceRoamingOffRegistrants.remove(paramHandler);
  }
  
  public void unregisterForVoiceRoamingOn(Handler paramHandler)
  {
    mVoiceRoamingOnRegistrants.remove(paramHandler);
  }
  
  protected void updateCarrierMccMncConfiguration(String paramString1, String paramString2, Context paramContext)
  {
    if (((paramString1 == null) && (!TextUtils.isEmpty(paramString2))) || ((paramString1 != null) && (!paramString1.equals(paramString2))))
    {
      paramString2 = new StringBuilder();
      paramString2.append("update mccmnc=");
      paramString2.append(paramString1);
      paramString2.append(" fromServiceState=true");
      log(paramString2.toString());
      MccTable.updateMccMncConfiguration(paramContext, paramString1, true);
    }
  }
  
  protected void updateOtaspState()
  {
    int i = getOtasp();
    int j = mCurrentOtaspMode;
    mCurrentOtaspMode = i;
    if (j != mCurrentOtaspMode)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("updateOtaspState: call notifyOtaspChanged old otaspMode=");
      localStringBuilder.append(j);
      localStringBuilder.append(" new otaspMode=");
      localStringBuilder.append(mCurrentOtaspMode);
      log(localStringBuilder.toString());
      mPhone.notifyOtaspChanged(mCurrentOtaspMode);
    }
  }
  
  protected void updatePhoneObject()
  {
    if (mPhone.getContext().getResources().getBoolean(17957057))
    {
      int i;
      if ((mSS.getVoiceRegState() != 0) && (mSS.getVoiceRegState() != 2)) {
        i = 0;
      } else {
        i = 1;
      }
      if (i == 0)
      {
        log("updatePhoneObject: Ignore update");
        return;
      }
      mPhone.updatePhoneObject(mSS.getRilVoiceRadioTechnology());
    }
  }
  
  @VisibleForTesting
  public void updatePhoneType()
  {
    if ((mSS != null) && (mSS.getVoiceRoaming())) {
      mVoiceRoamingOffRegistrants.notifyRegistrants();
    }
    if ((mSS != null) && (mSS.getDataRoaming())) {
      mDataRoamingOffRegistrants.notifyRegistrants();
    }
    if ((mSS != null) && (mSS.getVoiceRegState() == 0)) {
      mNetworkDetachedRegistrants.notifyRegistrants();
    }
    if ((mSS != null) && (mSS.getDataRegState() == 0)) {
      mDetachedRegistrants.notifyRegistrants();
    }
    mSS = new ServiceState();
    mSS.setStateOutOfService();
    mNewSS = new ServiceState();
    mSS.setPhoneId(mPhone.getPhoneId());
    mNewSS.setPhoneId(mPhone.getPhoneId());
    mLastCellInfoListTime = 0L;
    mLastCellInfoList = null;
    mSignalStrength = new SignalStrength();
    mStartedGprsRegCheck = false;
    mReportedGprsNoReg = false;
    mMdn = null;
    mMin = null;
    mPrlVersion = null;
    mIsMinInfoReady = false;
    mNitzState.handleNetworkUnavailable();
    cancelPollState();
    if (mPhone.isPhoneTypeGsm())
    {
      if (mCdmaSSM != null) {
        mCdmaSSM.dispose(this);
      }
      mCi.unregisterForCdmaPrlChanged(this);
      mPhone.unregisterForEriFileLoaded(this);
      mCi.unregisterForCdmaOtaProvision(this);
      mPhone.unregisterForSimRecordsLoaded(this);
      mCellLoc = new GsmCellLocation();
      mNewCellLoc = new GsmCellLocation();
    }
    else
    {
      mPhone.registerForSimRecordsLoaded(this, 16, null);
      mCellLoc = new CdmaCellLocation();
      mNewCellLoc = new CdmaCellLocation();
      mCdmaSSM = CdmaSubscriptionSourceManager.getInstance(mPhone.getContext(), mCi, this, 39, null);
      boolean bool;
      if (mCdmaSSM.getCdmaSubscriptionSource() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      mIsSubscriptionFromRuim = bool;
      mCi.registerForCdmaPrlChanged(this, 40, null);
      mPhone.registerForEriFileLoaded(this, 36, null);
      mCi.registerForCdmaOtaProvision(this, 37, null);
      mHbpcdUtils = new HbpcdUtils(mPhone.getContext());
      updateOtaspState();
      mSS.setCdmaEriIconIndex(-1);
      mSS.setCdmaEriIconMode(-1);
      mNewSS.setCdmaEriIconIndex(-1);
      mNewSS.setCdmaEriIconMode(-1);
    }
    onUpdateIccAvailability();
    mPhone.setSystemProperty("gsm.network.type", ServiceState.rilRadioTechnologyToString(0));
    mCi.getSignalStrength(obtainMessage(3));
    sendMessage(obtainMessage(50));
    logPhoneTypeChange();
    notifyDataRegStateRilRadioTechnologyChanged();
  }
  
  protected void updateRoamingState()
  {
    boolean bool1 = mPhone.isPhoneTypeGsm();
    boolean bool2 = false;
    if (bool1)
    {
      if ((!mGsmRoaming) && (!mDataRoaming)) {
        break label36;
      }
      bool2 = true;
      label36:
      bool1 = bool2;
      if (mGsmRoaming)
      {
        bool1 = bool2;
        if (!isOperatorConsideredRoaming(mNewSS)) {
          if (!isSameNamedOperators(mNewSS))
          {
            bool1 = bool2;
            if (!isOperatorConsideredNonRoaming(mNewSS)) {}
          }
          else
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("updateRoamingState: resource override set non roaming.isSameNamedOperators=");
            ((StringBuilder)localObject1).append(isSameNamedOperators(mNewSS));
            ((StringBuilder)localObject1).append(",isOperatorConsideredNonRoaming=");
            ((StringBuilder)localObject1).append(isOperatorConsideredNonRoaming(mNewSS));
            log(((StringBuilder)localObject1).toString());
            bool1 = false;
          }
        }
      }
      Object localObject1 = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
      if (localObject1 != null) {
        try
        {
          localObject1 = ((CarrierConfigManager)localObject1).getConfigForSubId(mPhone.getSubId());
          if (alwaysOnHomeNetwork((BaseBundle)localObject1))
          {
            log("updateRoamingState: carrier config override always on home network");
            bool2 = false;
          }
          else if (isNonRoamingInGsmNetwork((BaseBundle)localObject1, mNewSS.getOperatorNumeric()))
          {
            localObject1 = new java/lang/StringBuilder;
            ((StringBuilder)localObject1).<init>();
            ((StringBuilder)localObject1).append("updateRoamingState: carrier config override set non roaming:");
            ((StringBuilder)localObject1).append(mNewSS.getOperatorNumeric());
            log(((StringBuilder)localObject1).toString());
            bool2 = false;
          }
          else
          {
            bool2 = bool1;
            if (isRoamingInGsmNetwork((BaseBundle)localObject1, mNewSS.getOperatorNumeric()))
            {
              localObject1 = new java/lang/StringBuilder;
              ((StringBuilder)localObject1).<init>();
              ((StringBuilder)localObject1).append("updateRoamingState: carrier config override set roaming:");
              ((StringBuilder)localObject1).append(mNewSS.getOperatorNumeric());
              log(((StringBuilder)localObject1).toString());
              bool2 = true;
            }
          }
        }
        catch (Exception localException1)
        {
          for (;;)
          {
            loge("updateRoamingState: unable to access carrier config service");
            bool2 = bool1;
          }
        }
      }
      log("updateRoamingState: no carrier config service available");
      bool2 = bool1;
      bool1 = bool2;
      if (bool2 == true) {
        if (mRoamAlias != null)
        {
          bool1 = bool2;
          if (!RoamAliasAsus.isSameOperatorsAsus(mNewSS.getOperatorNumeric(), mRoamAlias)) {}
        }
        else
        {
          bool2 = false;
          bool1 = bool2;
          if (mRoamAlias == null)
          {
            log("updateRoamingState: mRoamAlias == null ");
            bool1 = bool2;
          }
        }
      }
      mNewSS.setVoiceRoaming(bool1);
      mNewSS.setDataRoaming(bool1);
    }
    else
    {
      Object localObject2 = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
      if (localObject2 != null) {
        try
        {
          Object localObject3 = ((CarrierConfigManager)localObject2).getConfigForSubId(mPhone.getSubId());
          localObject2 = Integer.toString(mNewSS.getCdmaSystemId());
          if (alwaysOnHomeNetwork((BaseBundle)localObject3))
          {
            log("updateRoamingState: carrier config override always on home network");
            setRoamingOff();
          }
          else if ((!isNonRoamingInGsmNetwork((BaseBundle)localObject3, mNewSS.getOperatorNumeric())) && (!isNonRoamingInCdmaNetwork((BaseBundle)localObject3, (String)localObject2)))
          {
            if ((isRoamingInGsmNetwork((BaseBundle)localObject3, mNewSS.getOperatorNumeric())) || (isRoamingInCdmaNetwork((BaseBundle)localObject3, (String)localObject2)))
            {
              localObject3 = new java/lang/StringBuilder;
              ((StringBuilder)localObject3).<init>();
              ((StringBuilder)localObject3).append("updateRoamingState: carrier config override set roaming:");
              ((StringBuilder)localObject3).append(mNewSS.getOperatorNumeric());
              ((StringBuilder)localObject3).append(", ");
              ((StringBuilder)localObject3).append((String)localObject2);
              log(((StringBuilder)localObject3).toString());
              setRoamingOn();
            }
          }
          else
          {
            localObject3 = new java/lang/StringBuilder;
            ((StringBuilder)localObject3).<init>();
            ((StringBuilder)localObject3).append("updateRoamingState: carrier config override set non-roaming:");
            ((StringBuilder)localObject3).append(mNewSS.getOperatorNumeric());
            ((StringBuilder)localObject3).append(", ");
            ((StringBuilder)localObject3).append((String)localObject2);
            log(((StringBuilder)localObject3).toString());
            setRoamingOff();
          }
        }
        catch (Exception localException2)
        {
          loge("updateRoamingState: unable to access carrier config service");
        }
      } else {
        log("updateRoamingState: no carrier config service available");
      }
      if ((Build.IS_DEBUGGABLE) && (SystemProperties.getBoolean("telephony.test.forceRoaming", false)))
      {
        mNewSS.setVoiceRoaming(true);
        mNewSS.setDataRoaming(true);
      }
    }
  }
  
  protected void updateSpnDisplay()
  {
    updateSpnDisplay(false);
  }
  
  protected void updateSpnDisplay(boolean paramBoolean)
  {
    updateOperatorNameFromEri();
    Object localObject1 = null;
    Object localObject2 = null;
    int i = 0;
    int j = getCombinedRegState();
    Object localObject3 = localObject1;
    Object localObject5 = localObject2;
    int k = i;
    int m;
    int n;
    Object localObject4;
    if (mPhone.getImsPhone() != null)
    {
      localObject3 = localObject1;
      localObject5 = localObject2;
      k = i;
      if (mPhone.getImsPhone().isWifiCallingEnabled())
      {
        localObject3 = localObject1;
        localObject5 = localObject2;
        k = i;
        if (j == 0)
        {
          localObject2 = mPhone.getContext().getResources().getStringArray(17236102);
          m = 0;
          n = 0;
          int i1 = 0;
          int i2 = 0;
          int i3 = 0;
          localObject3 = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
          k = i2;
          if (localObject3 != null)
          {
            n = m;
            try
            {
              localObject3 = ((CarrierConfigManager)localObject3).getConfigForSubId(mPhone.getSubId());
              n = i1;
              k = i3;
              if (localObject3 != null)
              {
                n = m;
                m = ((PersistableBundle)localObject3).getInt("wfc_spn_format_idx_int");
                n = m;
                k = ((PersistableBundle)localObject3).getInt("wfc_data_spn_format_idx_int");
                n = m;
              }
            }
            catch (Exception localException)
            {
              localObject1 = new StringBuilder();
              ((StringBuilder)localObject1).append("updateSpnDisplay: carrier config error: ");
              ((StringBuilder)localObject1).append(localException);
              loge(((StringBuilder)localObject1).toString());
              k = i2;
            }
          }
          localObject4 = localObject2[n];
          localObject5 = localObject2[k];
          localObject2 = getVowifiOverride();
          k = i;
          if (!TextUtils.isEmpty((CharSequence)localObject2))
          {
            k = 1;
            localObject4 = localObject2;
            localObject5 = localObject2;
          }
        }
      }
    }
    boolean bool1;
    if (mPhone.isPhoneTypeGsm())
    {
      localObject1 = mIccRecords;
      if (localObject1 != null) {
        n = ((IccRecords)localObject1).getDisplayRule(mSS);
      } else {
        n = 0;
      }
      Object localObject6 = mPhone.getOperatorNumeric();
      if ((!mGsmRoaming) && (!mDataRoaming)) {
        bool1 = false;
      } else {
        bool1 = true;
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("[APLMN][updateSpnDisplay] simNumeric: ");
      ((StringBuilder)localObject2).append((String)localObject6);
      ((StringBuilder)localObject2).append(", roaming: ");
      ((StringBuilder)localObject2).append(bool1);
      log(((StringBuilder)localObject2).toString());
      m = n;
      if (!bool1)
      {
        m = n;
        if (localObject6 != null)
        {
          m = n;
          if (((String)localObject6).startsWith("460"))
          {
            if (localObject1 != null) {
              n = ((IccRecords)localObject1).getDisplayRule(mSS);
            } else {
              n = 0;
            }
            m = n;
          }
        }
      }
      n = 0;
      i = 0;
      if ((j != 1) && (j != 2))
      {
        if (j == 0)
        {
          localObject2 = mSS.getOperatorAlpha();
          if ((!TextUtils.isEmpty((CharSequence)localObject2)) && ((m & 0x2) == 2)) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          localObject6 = new StringBuilder();
          ((StringBuilder)localObject6).append("[APLMN][updateSpnDisplay] showPlmn: ");
          ((StringBuilder)localObject6).append(bool1);
          ((StringBuilder)localObject6).append(", plmn: ");
          ((StringBuilder)localObject6).append((String)localObject2);
          ((StringBuilder)localObject6).append(", rule: ");
          ((StringBuilder)localObject6).append(m);
          log(((StringBuilder)localObject6).toString());
        }
        else
        {
          bool1 = true;
          localObject2 = Resources.getSystem().getText(17040249).toString();
          localObject6 = new StringBuilder();
          ((StringBuilder)localObject6).append("updateSpnDisplay: radio is off w/ showPlmn=");
          ((StringBuilder)localObject6).append(true);
          ((StringBuilder)localObject6).append(" plmn=");
          ((StringBuilder)localObject6).append((String)localObject2);
          log(((StringBuilder)localObject6).toString());
        }
      }
      else
      {
        bool1 = true;
        if ((mPhone.getContext().getResources().getBoolean(17956935)) && (!mIsSimReady)) {
          n = 1;
        } else {
          n = 0;
        }
        if ((mEmergencyOnly) && (n == 0))
        {
          localObject2 = Resources.getSystem().getText(17039918).toString();
          n = i;
        }
        else
        {
          localObject2 = Resources.getSystem().getText(17040249).toString();
          n = 1;
        }
        localObject6 = new StringBuilder();
        ((StringBuilder)localObject6).append("updateSpnDisplay: radio is on but out of service, set plmn='");
        ((StringBuilder)localObject6).append((String)localObject2);
        ((StringBuilder)localObject6).append("'");
        log(((StringBuilder)localObject6).toString());
      }
      if (localObject1 != null) {
        localObject1 = ((IccRecords)localObject1).getServiceProviderName();
      } else {
        localObject1 = "";
      }
      localObject6 = localObject1;
      boolean bool2;
      if ((n == 0) && (!TextUtils.isEmpty((CharSequence)localObject1)) && ((m & 0x1) == 1)) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if (k != 0)
      {
        log("[APLMN][updateSpnDisplay] Show spn [GT 4G VoWiFi] in status bar");
        localObject1 = localObject4;
      }
      if ((!TextUtils.isEmpty((CharSequence)localObject1)) && (!TextUtils.isEmpty((CharSequence)localObject4)) && (!TextUtils.isEmpty((CharSequence)localObject5)))
      {
        log("[APLMN] In Wi-Fi Calling mode show SPN+WiFi...");
        localObject1 = ((String)localObject1).trim();
        localObject4 = String.format((String)localObject4, new Object[] { localObject1 });
        localObject6 = String.format((String)localObject5, new Object[] { localObject1 });
        bool2 = true;
        bool1 = false;
      }
      else
      {
        if ((!TextUtils.isEmpty((CharSequence)localObject2)) && (!TextUtils.isEmpty((CharSequence)localObject4))) {
          localObject5 = String.format((String)localObject4, new Object[] { ((String)localObject2).trim() });
        }
        do
        {
          do
          {
            localObject4 = localObject1;
            localObject2 = localObject5;
            break label995;
            if (mSS.getVoiceRegState() == 3) {
              break;
            }
            localObject5 = localObject2;
          } while (!bool1);
          localObject5 = localObject2;
        } while (!TextUtils.equals((CharSequence)localObject1, (CharSequence)localObject2));
        localObject4 = null;
        bool2 = false;
      }
      label995:
      n = -1;
      localObject1 = SubscriptionManager.getSubId(mPhone.getPhoneId());
      if ((localObject1 != null) && (localObject1.length > 0)) {
        n = localObject1[0];
      }
      if ((mSubId == n) && (bool1 == mCurShowPlmn) && (bool2 == mCurShowSpn) && (TextUtils.equals((CharSequence)localObject4, mCurSpn)) && (TextUtils.equals((CharSequence)localObject6, mCurDataSpn)) && (TextUtils.equals((CharSequence)localObject2, mCurPlmn)) && (!paramBoolean)) {
        break label1305;
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("updateSpnDisplay: changed sending intent rule=");
      ((StringBuilder)localObject1).append(m);
      ((StringBuilder)localObject1).append(" showPlmn='%b' plmn='%s' showSpn='%b' spn='%s' dataSpn='%s' subId='%d' forceUpdate='%b'");
      log(String.format(((StringBuilder)localObject1).toString(), new Object[] { Boolean.valueOf(bool1), localObject2, Boolean.valueOf(bool2), localObject4, localObject6, Integer.valueOf(n), Boolean.valueOf(paramBoolean) }));
      localObject1 = new Intent("android.provider.Telephony.SPN_STRINGS_UPDATED");
      ((Intent)localObject1).putExtra("showSpn", bool2);
      ((Intent)localObject1).putExtra("spn", (String)localObject4);
      ((Intent)localObject1).putExtra("spnData", (String)localObject6);
      ((Intent)localObject1).putExtra("showPlmn", bool1);
      ((Intent)localObject1).putExtra("plmn", (String)localObject2);
      SubscriptionManager.putPhoneIdAndSubIdExtra((Intent)localObject1, mPhone.getPhoneId());
      mPhone.getContext().sendStickyBroadcastAsUser((Intent)localObject1, UserHandle.ALL);
      if (!mSubscriptionController.setPlmnSpn(mPhone.getPhoneId(), bool1, (String)localObject2, bool2, (String)localObject4)) {
        mSpnUpdatePending = true;
      }
      label1305:
      mSubId = n;
      mCurShowSpn = bool2;
      mCurShowPlmn = bool1;
      mCurSpn = ((String)localObject4);
      mCurDataSpn = ((String)localObject6);
      mCurPlmn = ((String)localObject2);
    }
    else
    {
      localObject1 = mSS.getOperatorAlpha();
      if (localObject1 != null) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      k = -1;
      localObject2 = SubscriptionManager.getSubId(mPhone.getPhoneId());
      n = k;
      if (localObject2 != null)
      {
        n = k;
        if (localObject2.length > 0) {
          n = localObject2[0];
        }
      }
      if ((!TextUtils.isEmpty((CharSequence)localObject1)) && (!TextUtils.isEmpty((CharSequence)localObject4)))
      {
        localObject2 = String.format((String)localObject4, new Object[] { ((String)localObject1).trim() });
      }
      else
      {
        localObject2 = localObject1;
        if (mCi.getRadioState() == CommandsInterface.RadioState.RADIO_OFF)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("updateSpnDisplay: overwriting plmn from ");
          ((StringBuilder)localObject2).append((String)localObject1);
          ((StringBuilder)localObject2).append(" to null as radio state is off");
          log(((StringBuilder)localObject2).toString());
          localObject2 = null;
        }
      }
      if (j == 1)
      {
        localObject2 = Resources.getSystem().getText(17040249).toString();
        localObject4 = new StringBuilder();
        ((StringBuilder)localObject4).append("updateSpnDisplay: radio is on but out of svc, set plmn='");
        ((StringBuilder)localObject4).append((String)localObject2);
        ((StringBuilder)localObject4).append("'");
        log(((StringBuilder)localObject4).toString());
      }
      if ((mSubId != n) || (!TextUtils.equals((CharSequence)localObject2, mCurPlmn)) || (paramBoolean))
      {
        log(String.format("updateSpnDisplay: changed sending intent showPlmn='%b' plmn='%s' subId='%d' forceUpdate='%b'", new Object[] { Boolean.valueOf(bool1), localObject2, Integer.valueOf(n), Boolean.valueOf(false) }));
        localObject4 = new Intent("android.provider.Telephony.SPN_STRINGS_UPDATED");
        ((Intent)localObject4).putExtra("showSpn", false);
        ((Intent)localObject4).putExtra("spn", "");
        ((Intent)localObject4).putExtra("showPlmn", bool1);
        ((Intent)localObject4).putExtra("plmn", (String)localObject2);
        SubscriptionManager.putPhoneIdAndSubIdExtra((Intent)localObject4, mPhone.getPhoneId());
        mPhone.getContext().sendStickyBroadcastAsUser((Intent)localObject4, UserHandle.ALL);
        if (!mSubscriptionController.setPlmnSpn(mPhone.getPhoneId(), bool1, (String)localObject2, false, "")) {
          mSpnUpdatePending = true;
        }
      }
      mSubId = n;
      mCurShowSpn = false;
      mCurShowPlmn = bool1;
      mCurSpn = "";
      mCurPlmn = ((String)localObject2);
    }
  }
  
  protected void useDataRegStateForDataOnlyDevices()
  {
    if (!mVoiceCapable)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("useDataRegStateForDataOnlyDevice: VoiceRegState=");
      localStringBuilder.append(mNewSS.getVoiceRegState());
      localStringBuilder.append(" DataRegState=");
      localStringBuilder.append(mNewSS.getDataRegState());
      log(localStringBuilder.toString());
      mNewSS.setVoiceRegState(mNewSS.getDataRegState());
    }
  }
  
  private class CellInfoResult
  {
    List<CellInfo> list;
    Object lockObj = new Object();
    
    private CellInfoResult() {}
  }
  
  private class SstSubscriptionsChangedListener
    extends SubscriptionManager.OnSubscriptionsChangedListener
  {
    public final AtomicInteger mPreviousSubId = new AtomicInteger(-1);
    
    private SstSubscriptionsChangedListener() {}
    
    public void onSubscriptionsChanged()
    {
      log("SubscriptionListener.onSubscriptionInfoChanged");
      int i = mPhone.getSubId();
      ServiceStateTracker.access$102(ServiceStateTracker.this, mPreviousSubId.get());
      if (mPreviousSubId.getAndSet(i) != i)
      {
        if (mSubscriptionController.isActiveSubId(i))
        {
          Object localObject1 = mPhone.getContext();
          mPhone.notifyPhoneStateChanged();
          mPhone.notifyCallForwardingIndicator();
          boolean bool = ((Context)localObject1).getResources().getBoolean(17957117);
          log("[ABSP][onSubscriptionsChanged] -> sendSubscriptionSettings sync preferred NW mode ...");
          mPhone.sendSubscriptionSettings(bool ^ true);
          mPhone.setSystemProperty("gsm.network.type", ServiceState.rilRadioTechnologyToString(mSS.getRilDataRadioTechnology()));
          if (mSpnUpdatePending)
          {
            mSubscriptionController.setPlmnSpn(mPhone.getPhoneId(), mCurShowPlmn, mCurPlmn, mCurShowSpn, mCurSpn);
            ServiceStateTracker.access$302(ServiceStateTracker.this, false);
          }
          Object localObject2 = PreferenceManager.getDefaultSharedPreferences((Context)localObject1);
          Object localObject3 = ((SharedPreferences)localObject2).getString("network_selection_key", "");
          Object localObject4 = ((SharedPreferences)localObject2).getString("network_selection_name_key", "");
          localObject1 = ((SharedPreferences)localObject2).getString("network_selection_short_key", "");
          if ((!TextUtils.isEmpty((CharSequence)localObject3)) || (!TextUtils.isEmpty((CharSequence)localObject4)) || (!TextUtils.isEmpty((CharSequence)localObject1)))
          {
            localObject2 = ((SharedPreferences)localObject2).edit();
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("network_selection_key");
            localStringBuilder.append(i);
            ((SharedPreferences.Editor)localObject2).putString(localStringBuilder.toString(), (String)localObject3);
            localObject3 = new StringBuilder();
            ((StringBuilder)localObject3).append("network_selection_name_key");
            ((StringBuilder)localObject3).append(i);
            ((SharedPreferences.Editor)localObject2).putString(((StringBuilder)localObject3).toString(), (String)localObject4);
            localObject4 = new StringBuilder();
            ((StringBuilder)localObject4).append("network_selection_short_key");
            ((StringBuilder)localObject4).append(i);
            ((SharedPreferences.Editor)localObject2).putString(((StringBuilder)localObject4).toString(), (String)localObject1);
            ((SharedPreferences.Editor)localObject2).remove("network_selection_key");
            ((SharedPreferences.Editor)localObject2).remove("network_selection_name_key");
            ((SharedPreferences.Editor)localObject2).remove("network_selection_short_key");
            ((SharedPreferences.Editor)localObject2).commit();
          }
          updateSpnDisplay();
        }
        mPhone.updateVoiceMail();
        if (mSubscriptionController.getSlotIndex(i) == -1) {
          sendMessage(obtainMessage(52));
        }
      }
    }
  }
}
