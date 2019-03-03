package com.android.internal.telephony.dataconnection;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkConfig;
import android.net.NetworkRequest;
import android.net.NetworkUtils;
import android.net.ProxyInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RegistrantList;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.provider.Telephony.Carriers;
import android.telephony.CarrierConfigManager;
import android.telephony.CellLocation;
import android.telephony.PcoData;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.SubscriptionManager.OnSubscriptionsChangedListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.data.DataProfile;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.LocalLog;
import android.util.Pair;
import android.util.SparseArray;
import android.view.Window;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.CallTracker;
import com.android.internal.telephony.CarrierActionAgent;
import com.android.internal.telephony.CarrierSignalAgent;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.CommandsInterface.RadioState;
import com.android.internal.telephony.DctConstants.Activity;
import com.android.internal.telephony.DctConstants.State;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants.DataState;
import com.android.internal.telephony.PhoneConstants.State;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RILConstants;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.SettingsObserver;
import com.android.internal.telephony.metrics.TelephonyMetrics;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.AsyncChannel;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DcTracker
  extends Handler
{
  static final String APN_ID = "apn_id";
  private static final int DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS_DEFAULT = 60000;
  private static final int DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS_DEFAULT = 360000;
  private static final String DATA_STALL_ALARM_TAG_EXTRA = "data.stall.alram.tag";
  private static final boolean DATA_STALL_NOT_SUSPECTED = false;
  private static final boolean DATA_STALL_SUSPECTED = true;
  protected static final boolean DBG = true;
  private static final String DEBUG_PROV_APN_ALARM = "persist.debug.prov_apn_alarm";
  private static final long DEFAULT_IDLE_MS = 900000L;
  private static final int EVENT_SIM_RECORDS_LOADED = 100;
  private static final String INTENT_DATA_STALL_ALARM = "com.android.internal.telephony.data-stall";
  protected static final String INTENT_DISCONNECT_AFTER_SREEN_OFF = "com.android.internal.telephony.dataconnectiontracker.intent_disconnect_after_screen_off";
  private static final String INTENT_PROVISIONING_APN_ALARM = "com.android.internal.telephony.provisioning_apn_alarm";
  private static final String INTENT_RECONNECT_ALARM = "com.android.internal.telephony.data-reconnect";
  private static final String INTENT_RECONNECT_ALARM_EXTRA_REASON = "reconnect_alarm_extra_reason";
  private static final String INTENT_RECONNECT_ALARM_EXTRA_TYPE = "reconnect_alarm_extra_type";
  private static final String INTENT_SAR_VOICE_MODE = "asus.intent.action.VOICE_MODE";
  private static final String INTENT_TIMEOUT_DATA_DISCONNECTED_EVENT = "asus.intent.action.data_disconnect_timeout";
  private static final int NUMBER_SENT_DNS_PACKETS_OF_HANG = 5;
  private static final int NUMBER_SENT_PACKETS_OF_HANG = 15;
  private static int O3D_TIMEOUT = 5000;
  private static final int POLL_NETSTAT_MILLIS = 1000;
  private static final int POLL_NETSTAT_SCREEN_OFF_MILLIS = 600000;
  private static final int POLL_PDP_MILLIS = 5000;
  static final Uri PREFERAPN_NO_UPDATE_URI_USING_SUBID;
  private static final int PROVISIONING_APN_ALARM_DELAY_IN_MS_DEFAULT = 900000;
  private static final String PROVISIONING_APN_ALARM_TAG_EXTRA = "provisioning.apn.alarm.tag";
  private static final int PROVISIONING_SPINNER_TIMEOUT_MILLIS = 120000;
  private static final String PUPPET_MASTER_RADIO_STRESS_TEST = "gsm.defaultpdpcontext.active";
  private static final String PowerSaver_Disconnect = "powersaver_wifi_disconnect";
  private static final String PowerSaver_Reconnect = "powersaver_wifi_reconnect";
  private static final boolean RADIO_TESTS = false;
  private static final int SAR_POWER_HEAD = 1;
  private static final int SAR_POWER_NORMAL = 0;
  private static final int SAR_VOICE_MODE_EARPICE = 1;
  private static final int SAR_VOICE_MODE_OTHER = 0;
  private static final int SAR_VOICE_MODE_SPEAKER = 2;
  private static final boolean VDBG = false;
  private static final boolean VDBG_STALL = false;
  protected static int mResetAttachRetry = 3;
  protected static int mResetDetachRetry = 3;
  private static int sEnableFailFastRefCounter = 0;
  protected String LOG_TAG = "DCT";
  private ApnContext apnContextStored = null;
  public AtomicBoolean isCleanupRequired = new AtomicBoolean(false);
  private boolean isDdsSwitchWaitingOriDefaultDisconnect = false;
  private DctConstants.Activity mActivity = DctConstants.Activity.NONE;
  protected AlarmManager mAlarmManager;
  private ArrayList<ApnSetting> mAllApnSettings = null;
  private RegistrantList mAllDataDisconnectedRegistrants = new RegistrantList();
  private final ConcurrentHashMap<String, ApnContext> mApnContexts = new ConcurrentHashMap();
  private final SparseArray<ApnContext> mApnContextsById = new SparseArray();
  private ApnChangeObserver mApnObserver;
  private HashMap<String, Integer> mApnToDataConnectionId = new HashMap();
  private AtomicBoolean mAttached = new AtomicBoolean(false);
  private AtomicBoolean mAutoAttachOnCreation = new AtomicBoolean(false);
  protected boolean mAutoAttachOnCreationConfig = false;
  private boolean mCanSetPreferApn = false;
  private final ConnectivityManager mCm;
  private HashMap<Integer, DcAsyncChannel> mDataConnectionAcHashMap = new HashMap();
  private final Handler mDataConnectionTracker;
  private HashMap<Integer, DataConnection> mDataConnections = new HashMap();
  private final DataEnabledSettings mDataEnabledSettings;
  private final LocalLog mDataRoamingLeakageLog = new LocalLog(50);
  private final DataServiceManager mDataServiceManager;
  private PendingIntent mDataStallAlarmIntent = null;
  private int mDataStallAlarmTag = (int)SystemClock.elapsedRealtime();
  private volatile boolean mDataStallDetectionEnabled = true;
  private DnsTxRxSum mDataStallDnsTxRxSum = new DnsTxRxSum(0L, 0L);
  private TxRxSum mDataStallTxRxSum = new TxRxSum(0L, 0L);
  private DcTesterFailBringUpAll mDcTesterFailBringUpAll;
  private DcController mDcc;
  private ArrayList<Message> mDisconnectAllCompleteMsgList = new ArrayList();
  private int mDisconnectPendingCount = 0;
  private long mDnsSentSinceLastRecv;
  private ApnSetting mEmergencyApn = null;
  private volatile boolean mFailFast = false;
  protected final AtomicReference<IccRecords> mIccRecords = new AtomicReference();
  private PendingIntent mIdleIntent;
  private boolean mInVoiceCall = false;
  private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver()
  {
    private long getWhenScreenTurnedoffTimeout()
    {
      return 900000L;
    }
    
    private boolean shouldDeviceStayAwake(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      boolean bool;
      if ((paramAnonymousInt1 & paramAnonymousInt2) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean shouldMobileStayConnected(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      int i = Settings.Global.getInt(mPhone.getContext().getContentResolver(), "mobile_sleep_policy", 2);
      if (i == 2) {
        return true;
      }
      if ((i == 1) && (paramAnonymousInt2 != 0)) {
        return true;
      }
      return shouldDeviceStayAwake(paramAnonymousInt1, paramAnonymousInt2);
    }
    
    private void updateSAR2Modem()
    {
      int i = 0;
      if (mSARVoiceMode == 1) {
        i = 1;
      }
      DcTracker localDcTracker = DcTracker.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("updateSAR2Modem: SARPower=");
      localStringBuilder.append(i);
      localDcTracker.log(localStringBuilder.toString());
    }
    
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      paramAnonymousContext = paramAnonymousIntent.getAction();
      int i = Settings.Global.getInt(mPhone.getContext().getContentResolver(), "stay_on_while_plugged_in", 0);
      if (paramAnonymousContext.equals("android.intent.action.SCREEN_ON"))
      {
        log("screen on");
        DcTracker.access$002(DcTracker.this, true);
        DcTracker.this.stopNetStatPoll();
        DcTracker.this.startNetStatPoll();
        DcTracker.this.restartDataStallAlarm();
        mAlarmManager.cancel(mIdleIntent);
        if (mReallyDisconnected)
        {
          sendMessage(obtainMessage(270836, 1, 0));
          DcTracker.access$502(DcTracker.this, false);
        }
      }
      else
      {
        Object localObject;
        if (paramAnonymousContext.equals("powersaver_wifi_reconnect"))
        {
          paramAnonymousIntent = DcTracker.this;
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Receive PowerSaver Reconnect : ");
          ((StringBuilder)localObject).append(paramAnonymousContext);
          paramAnonymousIntent.loge(((StringBuilder)localObject).toString());
          mAlarmManager.cancel(mIdleIntent);
          if (mReallyDisconnected)
          {
            sendMessage(obtainMessage(270836, 1, 0));
            DcTracker.access$502(DcTracker.this, false);
          }
        }
        else if (paramAnonymousContext.equals("android.intent.action.SCREEN_OFF"))
        {
          log("screen off");
          DcTracker.access$002(DcTracker.this, false);
          DcTracker.this.stopNetStatPoll();
          DcTracker.this.startNetStatPoll();
          DcTracker.this.restartDataStallAlarm();
          if (!shouldMobileStayConnected(i, mPluggedType))
          {
            paramAnonymousContext = DcTracker.this;
            paramAnonymousIntent = new StringBuilder();
            paramAnonymousIntent.append("setting ACTION_DEVICE_IDLE: ");
            paramAnonymousIntent.append(900000L);
            paramAnonymousIntent.append(" ms");
            paramAnonymousContext.log(paramAnonymousIntent.toString());
            mAlarmManager.setExact(0, System.currentTimeMillis() + 900000L, mIdleIntent);
          }
        }
        else if (paramAnonymousContext.equals("powersaver_wifi_disconnect"))
        {
          if (!shouldMobileStayConnected(i, mPluggedType))
          {
            paramAnonymousIntent = DcTracker.this;
            paramAnonymousContext = new StringBuilder();
            paramAnonymousContext.append("PowerSaver disconnect ACTION_DEVICE_IDLE: ");
            paramAnonymousContext.append(900000L);
            paramAnonymousContext.append(" ms");
            paramAnonymousIntent.log(paramAnonymousContext.toString());
            mAlarmManager.setExact(0, System.currentTimeMillis() + 900000L, mIdleIntent);
          }
        }
        else if (paramAnonymousContext.startsWith("com.android.internal.telephony.data-reconnect"))
        {
          localObject = DcTracker.this;
          paramAnonymousContext = new StringBuilder();
          paramAnonymousContext.append("Reconnect alarm. Previous state was ");
          paramAnonymousContext.append(mState);
          ((DcTracker)localObject).log(paramAnonymousContext.toString());
          DcTracker.this.onActionIntentReconnectAlarm(paramAnonymousIntent);
        }
        else if (paramAnonymousContext.equals("com.android.internal.telephony.data-stall"))
        {
          log("Data stall alarm");
          DcTracker.this.onActionIntentDataStallAlarm(paramAnonymousIntent);
        }
        else if (paramAnonymousIntent.getAction().equals("com.android.internal.telephony.dataconnectiontracker.intent_disconnect_after_screen_off"))
        {
          DcTracker.this.handleDisconnectAfterScreenOff();
        }
        else
        {
          int j;
          if (paramAnonymousContext.equals("android.intent.action.BATTERY_CHANGED"))
          {
            boolean bool = mIsScreenOn;
            j = paramAnonymousIntent.getIntExtra("plugged", 0);
            paramAnonymousIntent = DcTracker.this;
            paramAnonymousContext = new StringBuilder();
            paramAnonymousContext.append("ACTION_BATTERY_CHANGED pluggedType: ");
            paramAnonymousContext.append(j);
            paramAnonymousIntent.log(paramAnonymousContext.toString());
            if (((bool ^ true)) && (shouldMobileStayConnected(i, mPluggedType)) && (!shouldMobileStayConnected(i, j)))
            {
              long l = System.currentTimeMillis();
              paramAnonymousIntent = DcTracker.this;
              paramAnonymousContext = new StringBuilder();
              paramAnonymousContext.append("setting ACTION_DEVICE_IDLE timer for ");
              paramAnonymousContext.append(900000L);
              paramAnonymousContext.append("ms");
              paramAnonymousIntent.log(paramAnonymousContext.toString());
              mAlarmManager.setExact(0, l + 900000L, mIdleIntent);
            }
            DcTracker.access$702(DcTracker.this, j);
          }
          else if (paramAnonymousContext.equals("com.android.internal.telephony.provisioning_apn_alarm"))
          {
            log("Provisioning apn alarm");
            DcTracker.this.onActionIntentProvisioningApnAlarm(paramAnonymousIntent);
          }
          else if (paramAnonymousContext.equals("android.telephony.action.CARRIER_CONFIG_CHANGED"))
          {
            if ((mIccRecords.get() != null) && (((IccRecords)mIccRecords.get()).getRecordsLoaded()))
            {
              DcTracker.this.setDefaultDataRoamingEnabled();
              mDataEnabledSettings.setDefaultMobileDataEnabled();
            }
          }
          else if (paramAnonymousContext.equals("android.net.wifi.WIFI_AP_STATE_CHANGED"))
          {
            j = paramAnonymousIntent.getIntExtra("wifi_state", 11);
            paramAnonymousIntent = DcTracker.this;
            paramAnonymousContext = new StringBuilder();
            paramAnonymousContext.append("WIFI_AP_STATE_CHANGED_ACTION: wifiApState = ");
            paramAnonymousContext.append(j);
            paramAnonymousIntent.log(paramAnonymousContext.toString());
            if (j == 13) {
              DcTracker.access$1502(DcTracker.this, true);
            } else if (j == 11) {
              DcTracker.access$1502(DcTracker.this, false);
            }
            updateSAR2Modem();
          }
          else if (paramAnonymousContext.equals("asus.intent.action.VOICE_MODE"))
          {
            DcTracker.access$1602(DcTracker.this, paramAnonymousIntent.getIntExtra("mode", 0));
            paramAnonymousContext = DcTracker.this;
            paramAnonymousIntent = new StringBuilder();
            paramAnonymousIntent.append("onReceive: asus.intent.action.VOICE_MODE, mode=");
            paramAnonymousIntent.append(mSARVoiceMode);
            paramAnonymousIntent.append(", mLastSARVoiceMode=");
            paramAnonymousIntent.append(mLastSARVoiceMode);
            paramAnonymousContext.log(paramAnonymousIntent.toString());
            if (mLastSARVoiceMode != mSARVoiceMode)
            {
              DcTracker.access$1702(DcTracker.this, mSARVoiceMode);
              updateSAR2Modem();
            }
          }
          else if (paramAnonymousContext.equals("android.intent.action.BOOT_COMPLETED"))
          {
            log("Boot completed, set SAR_POWER_HEAD to modem.");
            mPhone.mCi.setTransmitPower(1, 0, obtainMessage(270436));
          }
          else if (paramAnonymousContext.equals("asus.intent.action.data_disconnect_timeout"))
          {
            log("[ABSP] Waiting old default data connection disconnect timeout ...");
            DcTracker.this.onRetrySetupDefaultData();
          }
          else
          {
            paramAnonymousIntent = DcTracker.this;
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("onReceive: Unknown action=");
            ((StringBuilder)localObject).append(paramAnonymousContext);
            paramAnonymousIntent.log(((StringBuilder)localObject).toString());
          }
        }
      }
    }
  };
  private boolean mIsDisposed = false;
  private boolean mIsProvisioning = false;
  private boolean mIsPsRestricted = false;
  private boolean mIsScreenOn = true;
  private boolean mIsWiFiAPON = false;
  private int mLastSARVoiceMode = -1;
  private boolean mMeteredApnDisabled = false;
  private boolean mMvnoMatched = false;
  private boolean mNetStatPollEnabled = false;
  private int mNetStatPollPeriod;
  private int mNoRecvPollCount = 0;
  private PendingIntent mOldDefaultDataDisconnAlarmIntent = null;
  private final DctOnSubscriptionsChangedListener mOnSubscriptionsChangedListener = new DctOnSubscriptionsChangedListener(null);
  protected boolean mOtherSimOnVoiceCall = false;
  protected final Phone mPhone;
  private int mPluggedType;
  private final Runnable mPollNetStat = new Runnable()
  {
    public void run()
    {
      DcTracker.this.updateDataActivity();
      if (mIsScreenOn) {
        DcTracker.access$2002(DcTracker.this, Settings.Global.getInt(mResolver, "pdp_watchdog_poll_interval_ms", 1000));
      } else {
        DcTracker.access$2002(DcTracker.this, Settings.Global.getInt(mResolver, "pdp_watchdog_long_poll_interval_ms", 600000));
      }
      if (mNetStatPollEnabled) {
        mDataConnectionTracker.postDelayed(this, mNetStatPollPeriod);
      }
    }
  };
  private ApnSetting mPreferredApn = null;
  private final PriorityQueue<ApnContext> mPrioritySortedApnContexts = new PriorityQueue(5, new Comparator()
  {
    public int compare(ApnContext paramAnonymousApnContext1, ApnContext paramAnonymousApnContext2)
    {
      return priority - priority;
    }
  });
  private final String mProvisionActionName;
  private BroadcastReceiver mProvisionBroadcastReceiver;
  private PendingIntent mProvisioningApnAlarmIntent = null;
  private int mProvisioningApnAlarmTag = (int)SystemClock.elapsedRealtime();
  private ProgressDialog mProvisioningSpinner;
  private String mProvisioningUrl = null;
  private boolean mReallyDisconnected = false;
  private PendingIntent mReconnectIntent = null;
  private AsyncChannel mReplyAc = new AsyncChannel();
  private String mRequestedApnType = "default";
  private boolean mReregisterOnReconnectFailure = false;
  private ContentResolver mResolver;
  private long mRxPkts;
  private int mSARVoiceMode = 0;
  private long mSentSinceLastRecv;
  private int mSetDataProfileStatus = 0;
  private final SettingsObserver mSettingsObserver;
  private SIMRecords mSimRecords;
  private DctConstants.State mState = DctConstants.State.IDLE;
  private SubscriptionManager mSubscriptionManager;
  private final int mTransportType;
  private long mTxPkts;
  private final UiccController mUiccController;
  private AtomicInteger mUniqueIdGenerator = new AtomicInteger(0);
  private boolean mVoiceAndDataNotAllowAndInVoice = false;
  
  static
  {
    PREFERAPN_NO_UPDATE_URI_USING_SUBID = Uri.parse("content://telephony/carriers/preferapn_no_update/subId/");
  }
  
  @VisibleForTesting
  public DcTracker()
  {
    mAlarmManager = null;
    mCm = null;
    mPhone = null;
    mUiccController = null;
    mDataConnectionTracker = null;
    mProvisionActionName = null;
    mSettingsObserver = new SettingsObserver(null, this);
    mDataEnabledSettings = null;
    mTransportType = 0;
    mDataServiceManager = null;
  }
  
  public DcTracker(Phone paramPhone, int paramInt)
  {
    mPhone = paramPhone;
    log("DCT.constructor");
    mTransportType = paramInt;
    mDataServiceManager = new DataServiceManager(paramPhone, paramInt);
    mResolver = mPhone.getContext().getContentResolver();
    mUiccController = UiccController.getInstance();
    mUiccController.registerForIccChanged(this, 270369, null);
    mAlarmManager = ((AlarmManager)mPhone.getContext().getSystemService("alarm"));
    mCm = ((ConnectivityManager)mPhone.getContext().getSystemService("connectivity"));
    Object localObject = new IntentFilter();
    ((IntentFilter)localObject).addAction("android.intent.action.SCREEN_ON");
    ((IntentFilter)localObject).addAction("android.intent.action.SCREEN_OFF");
    ((IntentFilter)localObject).addAction("com.android.internal.telephony.data-stall");
    ((IntentFilter)localObject).addAction("com.android.internal.telephony.provisioning_apn_alarm");
    ((IntentFilter)localObject).addAction("android.telephony.action.CARRIER_CONFIG_CHANGED");
    ((IntentFilter)localObject).addAction("asus.intent.action.data_disconnect_timeout");
    ((IntentFilter)localObject).addAction("com.android.internal.telephony.dataconnectiontracker.intent_disconnect_after_screen_off");
    ((IntentFilter)localObject).addAction("android.intent.action.BATTERY_CHANGED");
    ((IntentFilter)localObject).addAction("powersaver_wifi_reconnect");
    ((IntentFilter)localObject).addAction("powersaver_wifi_disconnect");
    ((IntentFilter)localObject).addAction("asus.intent.action.VOICE_MODE");
    ((IntentFilter)localObject).addAction("android.intent.action.BOOT_COMPLETED");
    ((IntentFilter)localObject).addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
    mDataEnabledSettings = new DataEnabledSettings(paramPhone);
    mPhone.getContext().registerReceiver(mIntentReceiver, (IntentFilter)localObject, null, mPhone);
    localObject = PreferenceManager.getDefaultSharedPreferences(mPhone.getContext());
    mAutoAttachOnCreation.set(((SharedPreferences)localObject).getBoolean("disabled_on_boot_key", false));
    mSubscriptionManager = SubscriptionManager.from(mPhone.getContext());
    mSubscriptionManager.addOnSubscriptionsChangedListener(mOnSubscriptionsChangedListener);
    localObject = new HandlerThread("DcHandlerThread");
    ((HandlerThread)localObject).start();
    localObject = new Handler(((HandlerThread)localObject).getLooper());
    mDcc = DcController.makeDcc(mPhone, this, mDataServiceManager, (Handler)localObject);
    mDcTesterFailBringUpAll = new DcTesterFailBringUpAll(mPhone, (Handler)localObject);
    mAlarmManager = ((AlarmManager)mPhone.getContext().getSystemService("alarm"));
    localObject = new Intent("com.android.internal.telephony.dataconnectiontracker.intent_disconnect_after_screen_off", null);
    mIdleIntent = PendingIntent.getBroadcast(mPhone.getContext(), 0, (Intent)localObject, 0);
    mDataConnectionTracker = this;
    registerForAllEvents();
    update();
    mApnObserver = new ApnChangeObserver();
    paramPhone.getContext().getContentResolver().registerContentObserver(Telephony.Carriers.CONTENT_URI, true, mApnObserver);
    initApnContexts();
    localObject = mApnContexts.values().iterator();
    while (((Iterator)localObject).hasNext())
    {
      ApnContext localApnContext = (ApnContext)((Iterator)localObject).next();
      IntentFilter localIntentFilter = new IntentFilter();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("com.android.internal.telephony.data-reconnect.");
      localStringBuilder.append(localApnContext.getApnType());
      localIntentFilter.addAction(localStringBuilder.toString());
      mPhone.getContext().registerReceiver(mIntentReceiver, localIntentFilter, null, mPhone);
    }
    initEmergencyApnSetting();
    addEmergencyApnSetting();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("com.android.internal.telephony.PROVISION");
    ((StringBuilder)localObject).append(paramPhone.getPhoneId());
    mProvisionActionName = ((StringBuilder)localObject).toString();
    mSettingsObserver = new SettingsObserver(mPhone.getContext(), this);
    registerSettingsObserver();
  }
  
  private ApnContext addApnContext(String paramString, NetworkConfig paramNetworkConfig)
  {
    paramNetworkConfig = new ApnContext(mPhone, paramString, LOG_TAG, paramNetworkConfig, this);
    mApnContexts.put(paramString, paramNetworkConfig);
    mApnContextsById.put(ApnContext.apnIdForApnName(paramString), paramNetworkConfig);
    mPrioritySortedApnContexts.add(paramNetworkConfig);
    return paramNetworkConfig;
  }
  
  private void addEmergencyApnSetting()
  {
    if (mEmergencyApn != null) {
      if (mAllApnSettings == null)
      {
        mAllApnSettings = new ArrayList();
      }
      else
      {
        int i = 0;
        Iterator localIterator = mAllApnSettings.iterator();
        int j;
        for (;;)
        {
          j = i;
          if (!localIterator.hasNext()) {
            break;
          }
          if (ArrayUtils.contains(nexttypes, "emergency"))
          {
            j = 1;
            break;
          }
        }
        if (j == 0) {
          mAllApnSettings.add(mEmergencyApn);
        } else {
          log("addEmergencyApnSetting - E-APN setting is already present");
        }
      }
    }
  }
  
  private String apnListToString(ArrayList<ApnSetting> paramArrayList)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = 0;
    int j = paramArrayList.size();
    while (i < j)
    {
      localStringBuilder.append('[');
      localStringBuilder.append(((ApnSetting)paramArrayList.get(i)).toString());
      localStringBuilder.append(']');
      i++;
    }
    return localStringBuilder.toString();
  }
  
  private void applyNewState(ApnContext paramApnContext, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    int j = 0;
    int k = 0;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("applyNewState(");
    ((StringBuilder)localObject).append(paramApnContext.getApnType());
    ((StringBuilder)localObject).append(", ");
    ((StringBuilder)localObject).append(paramBoolean1);
    ((StringBuilder)localObject).append("(");
    ((StringBuilder)localObject).append(paramApnContext.isEnabled());
    ((StringBuilder)localObject).append("), ");
    ((StringBuilder)localObject).append(paramBoolean2);
    ((StringBuilder)localObject).append("(");
    ((StringBuilder)localObject).append(paramApnContext.getDependencyMet());
    ((StringBuilder)localObject).append("))");
    localObject = ((StringBuilder)localObject).toString();
    log((String)localObject);
    paramApnContext.requestLog((String)localObject);
    int m;
    if (paramApnContext.isReady())
    {
      m = 1;
      if ((paramBoolean1) && (paramBoolean2))
      {
        localObject = paramApnContext.getState();
        switch (5.$SwitchMap$com$android$internal$telephony$DctConstants$State[localObject.ordinal()])
        {
        default: 
          break;
        case 3: 
        case 5: 
        case 6: 
        case 7: 
          k = 1;
          paramApnContext.setReason("dataEnabled");
          break;
        case 1: 
        case 2: 
        case 4: 
          log("applyNewState: 'ready' so return");
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("applyNewState state=");
          localStringBuilder.append(localObject);
          localStringBuilder.append(", so return");
          paramApnContext.requestLog(localStringBuilder.toString());
          return;
        }
      }
      else if (paramBoolean2)
      {
        paramApnContext.setReason("dataDisabled");
        if (((paramApnContext.getApnType() == "dun") && (teardownForDun())) || (paramApnContext.getState() != DctConstants.State.CONNECTED))
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Clean up the connection. Apn type = ");
          ((StringBuilder)localObject).append(paramApnContext.getApnType());
          ((StringBuilder)localObject).append(", state = ");
          ((StringBuilder)localObject).append(paramApnContext.getState());
          localObject = ((StringBuilder)localObject).toString();
          log((String)localObject);
          paramApnContext.requestLog((String)localObject);
          m = 1;
          k = j;
        }
        else
        {
          m = 0;
          k = j;
        }
      }
      else
      {
        paramApnContext.setReason("dependencyUnmet");
        k = j;
      }
    }
    else
    {
      m = i;
      k = j;
      if (paramBoolean1)
      {
        m = i;
        k = j;
        if (paramBoolean2)
        {
          if (paramApnContext.isEnabled()) {
            paramApnContext.setReason("dependencyMet");
          } else {
            paramApnContext.setReason("dataEnabled");
          }
          if (paramApnContext.getState() == DctConstants.State.FAILED) {
            paramApnContext.setState(DctConstants.State.IDLE);
          }
          k = 1;
          m = i;
        }
      }
    }
    paramApnContext.setEnabled(paramBoolean1);
    paramApnContext.setDependencyMet(paramBoolean2);
    if (m != 0) {
      cleanUpConnection(true, paramApnContext);
    }
    if (k != 0)
    {
      if ((TelephonyManager.getDefault().getPhoneCount() > 1) && (paramApnContext.getApnType() == "default")) {
        if (!isDdsSwitchWaitingOriDefaultDisconnect)
        {
          if (mPhone != null)
          {
            if (1 - mPhone.getPhoneId() > 0) {
              m = 1;
            } else {
              m = 0;
            }
            localObject = PhoneFactory.getPhone(m);
            if ((localObject != null) && (mDcTracker != null) && (((Phone)localObject).getDataConnectionState() != PhoneConstants.DataState.DISCONNECTED))
            {
              log("[ABSP][applyNewState] Waiting pdp disconnect done in other phone ...");
              isDdsSwitchWaitingOriDefaultDisconnect = true;
              apnContextStored = paramApnContext;
              startOldDefaultDataDisconnectTimeoutAlarm();
              return;
            }
          }
        }
        else
        {
          log("[ABSP][applyNewState] delay setup [Default] pdp for wating pdp disconnect in original default sub ...");
          return;
        }
      }
      paramApnContext.resetErrorCodeRetries();
      trySetupData(paramApnContext);
    }
  }
  
  private void broadcastDataStallDetected(int paramInt)
  {
    Intent localIntent = new Intent("android.intent.action.DATA_STALL_DETECTED");
    SubscriptionManager.putPhoneIdAndSubIdExtra(localIntent, mPhone.getPhoneId());
    localIntent.putExtra("recoveryAction", paramInt);
    mPhone.getContext().sendBroadcast(localIntent, "android.permission.READ_PHONE_STATE");
  }
  
  private ArrayList<ApnSetting> buildWaitingApns(String paramString, int paramInt)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("buildWaitingApns: E requestedApnType=");
    ((StringBuilder)localObject1).append(paramString);
    log(((StringBuilder)localObject1).toString());
    localObject1 = new ArrayList();
    if (paramString.equals("dun"))
    {
      localObject2 = fetchDunApns();
      if (((ArrayList)localObject2).size() > 0)
      {
        localObject2 = ((ArrayList)localObject2).iterator();
        while (((Iterator)localObject2).hasNext())
        {
          ((ArrayList)localObject1).add((ApnSetting)((Iterator)localObject2).next());
          paramString = new StringBuilder();
          paramString.append("buildWaitingApns: X added APN_TYPE_DUN apnList=");
          paramString.append(localObject1);
          log(paramString.toString());
        }
        return sortApnListByPreferred((ArrayList)localObject1);
      }
    }
    Object localObject2 = mPhone.getOperatorNumeric();
    boolean bool;
    try
    {
      bool = mPhone.getContext().getResources().getBoolean(17956936);
      bool = true ^ bool;
    }
    catch (Resources.NotFoundException localNotFoundException)
    {
      log("buildWaitingApns: usePreferred NotFoundException set to true");
      bool = true;
    }
    if (bool) {
      mPreferredApn = getPreferredApn();
    }
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("buildWaitingApns: usePreferred=");
    ((StringBuilder)localObject3).append(bool);
    ((StringBuilder)localObject3).append(" canSetPreferApn=");
    ((StringBuilder)localObject3).append(mCanSetPreferApn);
    ((StringBuilder)localObject3).append(" mPreferredApn=");
    ((StringBuilder)localObject3).append(mPreferredApn);
    ((StringBuilder)localObject3).append(" operator=");
    ((StringBuilder)localObject3).append((String)localObject2);
    ((StringBuilder)localObject3).append(" radioTech=");
    ((StringBuilder)localObject3).append(paramInt);
    ((StringBuilder)localObject3).append(" IccRecords r=");
    ((StringBuilder)localObject3).append(mIccRecords);
    log(((StringBuilder)localObject3).toString());
    if ((bool) && (mCanSetPreferApn) && (mPreferredApn != null) && (mPreferredApn.canHandleType(paramString)))
    {
      localObject3 = new StringBuilder();
      ((StringBuilder)localObject3).append("buildWaitingApns: Preferred APN:");
      ((StringBuilder)localObject3).append((String)localObject2);
      ((StringBuilder)localObject3).append(":");
      ((StringBuilder)localObject3).append(mPreferredApn.numeric);
      ((StringBuilder)localObject3).append(":");
      ((StringBuilder)localObject3).append(mPreferredApn);
      log(((StringBuilder)localObject3).toString());
      if (mPreferredApn.numeric.equals(localObject2))
      {
        if (ServiceState.bitmaskHasTech(mPreferredApn.networkTypeBitmask, ServiceState.rilRadioTechnologyToNetworkType(paramInt)))
        {
          ((ArrayList)localObject1).add(mPreferredApn);
          localObject1 = sortApnListByPreferred((ArrayList)localObject1);
          paramString = new StringBuilder();
          paramString.append("buildWaitingApns: X added preferred apnList=");
          paramString.append(localObject1);
          log(paramString.toString());
          return localObject1;
        }
        log("buildWaitingApns: no preferred APN");
        setPreferredApn(-1);
        mPreferredApn = null;
      }
      else
      {
        log("buildWaitingApns: no preferred APN");
        setPreferredApn(-1);
        mPreferredApn = null;
      }
    }
    if (mAllApnSettings != null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("buildWaitingApns: mAllApnSettings=");
      ((StringBuilder)localObject2).append(mAllApnSettings);
      log(((StringBuilder)localObject2).toString());
      localObject2 = mAllApnSettings.iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (ApnSetting)((Iterator)localObject2).next();
        if (((ApnSetting)localObject3).canHandleType(paramString))
        {
          StringBuilder localStringBuilder;
          if (ServiceState.bitmaskHasTech(networkTypeBitmask, ServiceState.rilRadioTechnologyToNetworkType(paramInt)))
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("buildWaitingApns: adding apn=");
            localStringBuilder.append(localObject3);
            log(localStringBuilder.toString());
            ((ArrayList)localObject1).add(localObject3);
          }
          else
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("buildWaitingApns: bearerBitmask:");
            localStringBuilder.append(bearerBitmask);
            localStringBuilder.append(" or networkTypeBitmask:");
            localStringBuilder.append(networkTypeBitmask);
            localStringBuilder.append("do not include radioTech:");
            localStringBuilder.append(paramInt);
            log(localStringBuilder.toString());
          }
        }
        else
        {
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("buildWaitingApns: couldn't handle requested ApnType=");
          ((StringBuilder)localObject3).append(paramString);
          log(((StringBuilder)localObject3).toString());
        }
      }
    }
    loge("mAllApnSettings is null!");
    localObject1 = sortApnListByPreferred((ArrayList)localObject1);
    paramString = new StringBuilder();
    paramString.append("buildWaitingApns: ");
    paramString.append(((ArrayList)localObject1).size());
    paramString.append(" APNs in the list: ");
    paramString.append(localObject1);
    log(paramString.toString());
    return localObject1;
  }
  
  private void cancelReconnectAlarm(ApnContext paramApnContext)
  {
    if (paramApnContext == null) {
      return;
    }
    PendingIntent localPendingIntent = paramApnContext.getReconnectIntent();
    if (localPendingIntent != null)
    {
      ((AlarmManager)mPhone.getContext().getSystemService("alarm")).cancel(localPendingIntent);
      paramApnContext.setReconnectIntent(null);
    }
  }
  
  private void checkDataRoamingStatus(boolean paramBoolean)
  {
    if ((!paramBoolean) && (!getDataRoamingEnabled()) && (mPhone.getServiceState().getDataRoaming()))
    {
      Iterator localIterator = mApnContexts.values().iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (ApnContext)localIterator.next();
        if (((ApnContext)localObject).getState() == DctConstants.State.CONNECTED)
        {
          LocalLog localLocalLog = mDataRoamingLeakageLog;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("PossibleRoamingLeakage  connection params: ");
          if (((ApnContext)localObject).getDcAc() != null) {
            localObject = getDcAcmLastConnectionParams;
          } else {
            localObject = "";
          }
          localStringBuilder.append(localObject);
          localLocalLog.log(localStringBuilder.toString());
        }
      }
    }
  }
  
  private DcAsyncChannel checkForCompatibleConnectedApnContext(ApnContext paramApnContext)
  {
    String str = paramApnContext.getApnType();
    ArrayList localArrayList = null;
    if ("dun".equals(str)) {
      localArrayList = sortApnListByPreferred(fetchDunApns());
    }
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("checkForCompatibleConnectedApnContext: apnContext=");
    ((StringBuilder)localObject1).append(paramApnContext);
    log(((StringBuilder)localObject1).toString());
    Object localObject2 = null;
    localObject1 = null;
    Iterator localIterator1 = mApnContexts.values().iterator();
    while (localIterator1.hasNext())
    {
      ApnContext localApnContext = (ApnContext)localIterator1.next();
      DcAsyncChannel localDcAsyncChannel = localApnContext.getDcAc();
      Object localObject3 = localObject2;
      Object localObject4 = localObject1;
      if (localDcAsyncChannel != null)
      {
        ApnSetting localApnSetting = localApnContext.getApnSetting();
        localObject3 = new StringBuilder();
        ((StringBuilder)localObject3).append("apnSetting: ");
        ((StringBuilder)localObject3).append(localApnSetting);
        log(((StringBuilder)localObject3).toString());
        int i;
        if ((localArrayList != null) && (localArrayList.size() > 0))
        {
          Iterator localIterator2 = localArrayList.iterator();
          for (;;)
          {
            localObject3 = localObject2;
            localObject4 = localObject1;
            if (!localIterator2.hasNext()) {
              break;
            }
            localObject3 = localObject2;
            localObject4 = localObject1;
            if (((ApnSetting)localIterator2.next()).equals(localApnSetting))
            {
              i = 5.$SwitchMap$com$android$internal$telephony$DctConstants$State[localApnContext.getState().ordinal()];
              if (i != 1)
              {
                switch (i)
                {
                default: 
                  localObject3 = localObject2;
                  localObject4 = localObject1;
                  break;
                case 3: 
                case 4: 
                  localObject3 = localDcAsyncChannel;
                  localObject4 = localApnContext;
                  break;
                }
              }
              else
              {
                paramApnContext = new StringBuilder();
                paramApnContext.append("checkForCompatibleConnectedApnContext: found dun conn=");
                paramApnContext.append(localDcAsyncChannel);
                paramApnContext.append(" curApnCtx=");
                paramApnContext.append(localApnContext);
                log(paramApnContext.toString());
                return localDcAsyncChannel;
              }
            }
            localObject2 = localObject3;
            localObject1 = localObject4;
          }
        }
        localObject3 = localObject2;
        localObject4 = localObject1;
        if (localApnSetting != null)
        {
          localObject3 = localObject2;
          localObject4 = localObject1;
          if (localApnSetting.canHandleType(str))
          {
            i = 5.$SwitchMap$com$android$internal$telephony$DctConstants$State[localApnContext.getState().ordinal()];
            if (i != 1)
            {
              switch (i)
              {
              default: 
                localObject3 = localObject2;
                localObject4 = localObject1;
                break;
              case 3: 
              case 4: 
                localObject3 = localDcAsyncChannel;
                localObject4 = localApnContext;
                break;
              }
            }
            else
            {
              paramApnContext = new StringBuilder();
              paramApnContext.append("checkForCompatibleConnectedApnContext: found canHandle conn=");
              paramApnContext.append(localDcAsyncChannel);
              paramApnContext.append(" curApnCtx=");
              paramApnContext.append(localApnContext);
              log(paramApnContext.toString());
              return localDcAsyncChannel;
            }
          }
        }
      }
      localObject2 = localObject3;
      localObject1 = localObject4;
    }
    if (localObject2 != null)
    {
      paramApnContext = new StringBuilder();
      paramApnContext.append("checkForCompatibleConnectedApnContext: found potential conn=");
      paramApnContext.append(localObject2);
      paramApnContext.append(" curApnCtx=");
      paramApnContext.append(localObject1);
      log(paramApnContext.toString());
      return localObject2;
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("checkForCompatibleConnectedApnContext: NO conn apnContext=");
    ((StringBuilder)localObject1).append(paramApnContext);
    log(((StringBuilder)localObject1).toString());
    return null;
  }
  
  private boolean cleanUpAllConnections(boolean paramBoolean, String paramString)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("cleanUpAllConnections: tearDown=");
    ((StringBuilder)localObject1).append(paramBoolean);
    ((StringBuilder)localObject1).append(" reason=");
    ((StringBuilder)localObject1).append(paramString);
    log(((StringBuilder)localObject1).toString());
    boolean bool1 = false;
    int i = 0;
    if (!TextUtils.isEmpty(paramString)) {
      if ((!paramString.equals("specificDisabled")) && (!paramString.equals("roamingOn")) && (!paramString.equals("carrierActionDisableMeteredApn")) && (!paramString.equals("SinglePdnArbitration")) && (!paramString.equals("pdpReset"))) {
        i = 0;
      } else {
        i = 1;
      }
    }
    Iterator localIterator = mApnContexts.values().iterator();
    while (localIterator.hasNext())
    {
      localObject1 = (ApnContext)localIterator.next();
      boolean bool2;
      if (i != 0)
      {
        bool2 = bool1;
        if (!((ApnContext)localObject1).getApnType().equals("ims"))
        {
          Object localObject2 = ((ApnContext)localObject1).getApnSetting();
          bool2 = bool1;
          if (localObject2 != null)
          {
            bool2 = bool1;
            if (((ApnSetting)localObject2).isMetered(mPhone))
            {
              if (!((ApnContext)localObject1).isDisconnected()) {
                bool1 = true;
              }
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("clean up metered ApnContext Type: ");
              ((StringBuilder)localObject2).append(((ApnContext)localObject1).getApnType());
              log(((StringBuilder)localObject2).toString());
              ((ApnContext)localObject1).setReason(paramString);
              cleanUpConnection(paramBoolean, (ApnContext)localObject1);
              bool2 = bool1;
            }
          }
        }
      }
      else
      {
        if ((paramString.equals("SinglePdnArbitration")) && (((ApnContext)localObject1).getApnType().equals("ims"))) {
          continue;
        }
        if (!((ApnContext)localObject1).isDisconnected()) {
          bool1 = true;
        }
        ((ApnContext)localObject1).setReason(paramString);
        cleanUpConnection(paramBoolean, (ApnContext)localObject1);
        bool2 = bool1;
      }
      bool1 = bool2;
    }
    stopNetStatPoll();
    stopDataStallAlarm();
    mRequestedApnType = "default";
    paramString = new StringBuilder();
    paramString.append("cleanUpConnection: mDisconnectPendingCount = ");
    paramString.append(mDisconnectPendingCount);
    log(paramString.toString());
    if ((paramBoolean) && (mDisconnectPendingCount == 0))
    {
      notifyDataDisconnectComplete();
      notifyAllDataDisconnected();
    }
    return bool1;
  }
  
  private void cleanUpConnection(boolean paramBoolean, ApnContext paramApnContext)
  {
    if (paramApnContext == null)
    {
      log("cleanUpConnection: apn context is null");
      return;
    }
    Object localObject1 = paramApnContext.getDcAc();
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("cleanUpConnection: tearDown=");
    ((StringBuilder)localObject2).append(paramBoolean);
    ((StringBuilder)localObject2).append(" reason=");
    ((StringBuilder)localObject2).append(paramApnContext.getReason());
    paramApnContext.requestLog(((StringBuilder)localObject2).toString());
    if (paramBoolean)
    {
      if (paramApnContext.isDisconnected())
      {
        paramApnContext.setState(DctConstants.State.IDLE);
        if (!paramApnContext.isReady())
        {
          if (localObject1 != null)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("cleanUpConnection: teardown, disconnected, !ready");
            ((StringBuilder)localObject2).append(" apnContext=");
            ((StringBuilder)localObject2).append(paramApnContext);
            log(((StringBuilder)localObject2).toString());
            paramApnContext.requestLog("cleanUpConnection: teardown, disconnected, !ready");
            ((DcAsyncChannel)localObject1).tearDown(paramApnContext, "", null);
          }
          paramApnContext.setDataConnectionAc(null);
        }
      }
      else if (localObject1 != null)
      {
        if (paramApnContext.getState() != DctConstants.State.DISCONNECTING)
        {
          int i = 0;
          int j = i;
          if ("dun".equals(paramApnContext.getApnType()))
          {
            j = i;
            if (teardownForDun())
            {
              log("cleanUpConnection: disconnectAll DUN connection");
              j = 1;
            }
          }
          i = paramApnContext.getConnectionGeneration();
          Object localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("cleanUpConnection: tearing down");
          if (j != 0) {
            localObject2 = " all";
          } else {
            localObject2 = "";
          }
          ((StringBuilder)localObject3).append((String)localObject2);
          ((StringBuilder)localObject3).append(" using gen#");
          ((StringBuilder)localObject3).append(i);
          localObject3 = ((StringBuilder)localObject3).toString();
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append((String)localObject3);
          ((StringBuilder)localObject2).append("apnContext=");
          ((StringBuilder)localObject2).append(paramApnContext);
          log(((StringBuilder)localObject2).toString());
          paramApnContext.requestLog((String)localObject3);
          localObject2 = obtainMessage(270351, new Pair(paramApnContext, Integer.valueOf(i)));
          if (j != 0) {
            paramApnContext.getDcAc().tearDownAll(paramApnContext.getReason(), (Message)localObject2);
          } else {
            paramApnContext.getDcAc().tearDown(paramApnContext, paramApnContext.getReason(), (Message)localObject2);
          }
          paramApnContext.setState(DctConstants.State.DISCONNECTING);
          mDisconnectPendingCount += 1;
        }
      }
      else
      {
        paramApnContext.setState(DctConstants.State.IDLE);
        paramApnContext.requestLog("cleanUpConnection: connected, bug no DCAC");
        mPhone.notifyDataConnection(paramApnContext.getReason(), paramApnContext.getApnType());
      }
    }
    else
    {
      if (localObject1 != null) {
        ((DcAsyncChannel)localObject1).reqReset();
      }
      paramApnContext.setState(DctConstants.State.IDLE);
      mPhone.notifyDataConnection(paramApnContext.getReason(), paramApnContext.getApnType());
      paramApnContext.setDataConnectionAc(null);
    }
    if (localObject1 != null) {
      cancelReconnectAlarm(paramApnContext);
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("cleanUpConnection: X tearDown=");
    ((StringBuilder)localObject2).append(paramBoolean);
    ((StringBuilder)localObject2).append(" reason=");
    ((StringBuilder)localObject2).append(paramApnContext.getReason());
    localObject1 = ((StringBuilder)localObject2).toString();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(" apnContext=");
    ((StringBuilder)localObject2).append(paramApnContext);
    ((StringBuilder)localObject2).append(" dcac=");
    ((StringBuilder)localObject2).append(paramApnContext.getDcAc());
    log(((StringBuilder)localObject2).toString());
    paramApnContext.requestLog((String)localObject1);
  }
  
  private void cleanUpConnectionsOnUpdatedApns(boolean paramBoolean, String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("cleanUpConnectionsOnUpdatedApns: tearDown=");
    ((StringBuilder)localObject).append(paramBoolean);
    log(((StringBuilder)localObject).toString());
    if ((mAllApnSettings != null) && (mAllApnSettings.isEmpty()))
    {
      cleanUpAllConnections(paramBoolean, "apnChanged");
    }
    else
    {
      if (mPhone.getServiceState().getRilDataRadioTechnology() == 0) {
        return;
      }
      localObject = mApnContexts.values().iterator();
      while (((Iterator)localObject).hasNext())
      {
        ApnContext localApnContext = (ApnContext)((Iterator)localObject).next();
        ArrayList localArrayList1 = localApnContext.getWaitingApns();
        ArrayList localArrayList2 = buildWaitingApns(localApnContext.getApnType(), mPhone.getServiceState().getRilDataRadioTechnology());
        if ((localArrayList1 != null) && ((localArrayList2.size() != localArrayList1.size()) || (!containsAllApns(localArrayList1, localArrayList2))))
        {
          localApnContext.setWaitingApns(localArrayList2);
          if (!localApnContext.isDisconnected())
          {
            localApnContext.setReason(paramString);
            cleanUpConnection(true, localApnContext);
          }
        }
      }
    }
    if (!isConnected())
    {
      stopNetStatPoll();
      stopDataStallAlarm();
    }
    mRequestedApnType = "default";
    paramString = new StringBuilder();
    paramString.append("mDisconnectPendingCount = ");
    paramString.append(mDisconnectPendingCount);
    log(paramString.toString());
    if ((paramBoolean) && (mDisconnectPendingCount == 0))
    {
      notifyDataDisconnectComplete();
      notifyAllDataDisconnected();
    }
  }
  
  private void completeConnection(ApnContext paramApnContext)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("completeConnection: successful, notify the world apnContext=");
    ((StringBuilder)localObject).append(paramApnContext);
    log(((StringBuilder)localObject).toString());
    if ((mIsProvisioning) && (!TextUtils.isEmpty(mProvisioningUrl)))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("completeConnection: MOBILE_PROVISIONING_ACTION url=");
      ((StringBuilder)localObject).append(mProvisioningUrl);
      log(((StringBuilder)localObject).toString());
      localObject = Intent.makeMainSelectorActivity("android.intent.action.MAIN", "android.intent.category.APP_BROWSER");
      ((Intent)localObject).setData(Uri.parse(mProvisioningUrl));
      ((Intent)localObject).setFlags(272629760);
      try
      {
        mPhone.getContext().startActivity((Intent)localObject);
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("completeConnection: startActivityAsUser failed");
        ((StringBuilder)localObject).append(localActivityNotFoundException);
        loge(((StringBuilder)localObject).toString());
      }
    }
    mIsProvisioning = false;
    mProvisioningUrl = null;
    if (mProvisioningSpinner != null) {
      sendMessage(obtainMessage(270378, mProvisioningSpinner));
    }
    mPhone.notifyDataConnection(paramApnContext.getReason(), paramApnContext.getApnType());
    startNetStatPoll();
    startDataStallAlarm(false);
  }
  
  private boolean containsAllApns(ArrayList<ApnSetting> paramArrayList1, ArrayList<ApnSetting> paramArrayList2)
  {
    paramArrayList2 = paramArrayList2.iterator();
    while (paramArrayList2.hasNext())
    {
      ApnSetting localApnSetting = (ApnSetting)paramArrayList2.next();
      int i = 0;
      Iterator localIterator = paramArrayList1.iterator();
      int j;
      for (;;)
      {
        j = i;
        if (!localIterator.hasNext()) {
          break;
        }
        if (((ApnSetting)localIterator.next()).equals(localApnSetting, mPhone.getServiceState().getDataRoamingFromRegistration()))
        {
          j = 1;
          break;
        }
      }
      if (j == 0) {
        return false;
      }
    }
    return true;
  }
  
  private ArrayList<ApnSetting> createApnList(Cursor paramCursor)
  {
    Object localObject = new ArrayList();
    ArrayList localArrayList = new ArrayList();
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    if (paramCursor.moveToFirst()) {
      do
      {
        ApnSetting localApnSetting = makeApnSetting(paramCursor);
        if (localApnSetting != null) {
          if (localApnSetting.hasMvnoParams())
          {
            if ((localIccRecords != null) && (ApnSetting.mvnoMatches(localIccRecords, mvnoType, mvnoMatchData))) {
              localArrayList.add(localApnSetting);
            }
          }
          else {
            ((ArrayList)localObject).add(localApnSetting);
          }
        }
      } while (paramCursor.moveToNext());
    }
    if (localArrayList.isEmpty())
    {
      paramCursor = (Cursor)localObject;
      mMvnoMatched = false;
    }
    else
    {
      paramCursor = localArrayList;
      mMvnoMatched = true;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("createApnList: X result=");
    ((StringBuilder)localObject).append(paramCursor);
    log(((StringBuilder)localObject).toString());
    return paramCursor;
  }
  
  private DcAsyncChannel createDataConnection()
  {
    log("createDataConnection E");
    int i = mUniqueIdGenerator.getAndIncrement();
    DataConnection localDataConnection = DataConnection.makeDataConnection(mPhone, i, this, mDataServiceManager, mDcTesterFailBringUpAll, mDcc);
    mDataConnections.put(Integer.valueOf(i), localDataConnection);
    DcAsyncChannel localDcAsyncChannel = new DcAsyncChannel(localDataConnection, LOG_TAG);
    int j = localDcAsyncChannel.fullyConnectSync(mPhone.getContext(), this, localDataConnection.getHandler());
    if (j == 0)
    {
      mDataConnectionAcHashMap.put(Integer.valueOf(localDcAsyncChannel.getDataConnectionIdSync()), localDcAsyncChannel);
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("createDataConnection: Could not connect to dcac=");
      localStringBuilder.append(localDcAsyncChannel);
      localStringBuilder.append(" status=");
      localStringBuilder.append(j);
      loge(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("createDataConnection() X id=");
    localStringBuilder.append(i);
    localStringBuilder.append(" dc=");
    localStringBuilder.append(localDataConnection);
    log(localStringBuilder.toString());
    return localDcAsyncChannel;
  }
  
  private static DataProfile createDataProfile(ApnSetting paramApnSetting)
  {
    return createDataProfile(paramApnSetting, profileId);
  }
  
  @VisibleForTesting
  public static DataProfile createDataProfile(ApnSetting paramApnSetting, int paramInt)
  {
    int i = ServiceState.convertNetworkTypeBitmaskToBearerBitmask(networkTypeBitmask);
    int j;
    if (i == 0) {
      j = 0;
    }
    for (;;)
    {
      break;
      if (ServiceState.bearerBitmapHasCdma(i)) {
        j = 2;
      } else {
        j = 1;
      }
    }
    return new DataProfile(paramInt, apn, protocol, authType, user, password, j, maxConnsTime, maxConns, waitTime, carrierEnabled, typesBitmap, roamingProtocol, i, mtu, mvnoType, mvnoMatchData, modemCognitive);
  }
  
  private boolean dataConnectionNotInUse(DcAsyncChannel paramDcAsyncChannel)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("dataConnectionNotInUse: check if dcac is inuse dcac=");
    ((StringBuilder)localObject).append(paramDcAsyncChannel);
    log(((StringBuilder)localObject).toString());
    Iterator localIterator = mApnContexts.values().iterator();
    while (localIterator.hasNext())
    {
      localObject = (ApnContext)localIterator.next();
      if (((ApnContext)localObject).getDcAc() == paramDcAsyncChannel)
      {
        paramDcAsyncChannel = new StringBuilder();
        paramDcAsyncChannel.append("dataConnectionNotInUse: in use by apnContext=");
        paramDcAsyncChannel.append(localObject);
        log(paramDcAsyncChannel.toString());
        return false;
      }
    }
    log("dataConnectionNotInUse: tearDownAll");
    paramDcAsyncChannel.tearDownAll("No connection", null);
    log("dataConnectionNotInUse: not in use return true");
    return true;
  }
  
  private void dedupeApnSettings()
  {
    new ArrayList();
    for (int i = 0; i < mAllApnSettings.size() - 1; i++)
    {
      ApnSetting localApnSetting1 = (ApnSetting)mAllApnSettings.get(i);
      int j = i + 1;
      while (j < mAllApnSettings.size())
      {
        ApnSetting localApnSetting2 = (ApnSetting)mAllApnSettings.get(j);
        if (localApnSetting1.similar(localApnSetting2))
        {
          localApnSetting1 = mergeApns(localApnSetting1, localApnSetting2);
          mAllApnSettings.set(i, localApnSetting1);
          mAllApnSettings.remove(j);
        }
        else
        {
          j++;
        }
      }
    }
  }
  
  private void destroyDataConnections()
  {
    if (mDataConnections != null)
    {
      log("destroyDataConnections: clear mDataConnectionList");
      mDataConnections.clear();
    }
    else
    {
      log("destroyDataConnections: mDataConnecitonList is empty, ignore");
    }
  }
  
  private void doRecovery()
  {
    if (getOverallState() == DctConstants.State.CONNECTED)
    {
      int i = getRecoveryAction();
      int j = mPhone.getServiceState().getRilDataRadioTechnology();
      if ((j == 14) || (j == 19)) {
        i = 4;
      }
      TelephonyMetrics.getInstance().writeDataStallEvent(mPhone.getPhoneId(), i);
      broadcastDataStallDetected(i);
      switch (i)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("doRecovery: Invalid recoveryAction=");
        localStringBuilder.append(i);
        throw new RuntimeException(localStringBuilder.toString());
      case 4: 
        log("start PS recovery on LTE/LTE-CA(4G+) network ==>[re-register to LTE/LTE-CA(4G+)]");
        SetAttachDetach(false);
        break;
      case 3: 
        EventLog.writeEvent(50121, mSentSinceLastRecv);
        log("restarting radio");
        restartRadio();
        putRecoveryAction(0);
        break;
      case 2: 
        EventLog.writeEvent(50120, mSentSinceLastRecv);
        log("doRecovery() re-register");
        mPhone.getServiceStateTracker().reRegisterNetwork(null);
        putRecoveryAction(3);
        break;
      case 1: 
        EventLog.writeEvent(50119, mSentSinceLastRecv);
        log("doRecovery() cleanup all connections");
        cleanUpAllConnections("pdpReset");
        putRecoveryAction(2);
        break;
      case 0: 
        EventLog.writeEvent(50118, mSentSinceLastRecv);
        log("doRecovery() get data call list");
        mDataServiceManager.getDataCallList(obtainMessage());
        putRecoveryAction(1);
      }
      mSentSinceLastRecv = 0L;
      mDnsSentSinceLastRecv = 0L;
    }
  }
  
  private DcAsyncChannel findDataConnectionAcByCid(int paramInt)
  {
    Iterator localIterator = mDataConnectionAcHashMap.values().iterator();
    while (localIterator.hasNext())
    {
      DcAsyncChannel localDcAsyncChannel = (DcAsyncChannel)localIterator.next();
      if (localDcAsyncChannel.getCidSync() == paramInt) {
        return localDcAsyncChannel;
      }
    }
    return null;
  }
  
  private DcAsyncChannel findFreeDataConnection()
  {
    Object localObject = mDataConnectionAcHashMap.values().iterator();
    while (((Iterator)localObject).hasNext())
    {
      DcAsyncChannel localDcAsyncChannel = (DcAsyncChannel)((Iterator)localObject).next();
      if ((localDcAsyncChannel.isInactiveSync()) && (dataConnectionNotInUse(localDcAsyncChannel)))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("findFreeDataConnection: found free DataConnection= dcac=");
        ((StringBuilder)localObject).append(localDcAsyncChannel);
        log(((StringBuilder)localObject).toString());
        return localDcAsyncChannel;
      }
    }
    log("findFreeDataConnection: NO free DataConnection");
    return null;
  }
  
  private int getApnProfileID(String paramString)
  {
    if (TextUtils.equals(paramString, "ims")) {
      return 2;
    }
    if (TextUtils.equals(paramString, "fota")) {
      return 3;
    }
    if (TextUtils.equals(paramString, "cbs")) {
      return 4;
    }
    if (TextUtils.equals(paramString, "ia")) {
      return 0;
    }
    if (TextUtils.equals(paramString, "dun")) {
      return 1;
    }
    return 0;
  }
  
  private int getCellLocationId()
  {
    int i = -1;
    CellLocation localCellLocation = mPhone.getCellLocation();
    int j = i;
    if (localCellLocation != null) {
      if ((localCellLocation instanceof GsmCellLocation))
      {
        j = ((GsmCellLocation)localCellLocation).getCid();
      }
      else
      {
        j = i;
        if ((localCellLocation instanceof CdmaCellLocation)) {
          j = ((CdmaCellLocation)localCellLocation).getBaseStationId();
        }
      }
    }
    return j;
  }
  
  private boolean getDefaultDataRoamingEnabled()
  {
    CarrierConfigManager localCarrierConfigManager = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    return "true".equalsIgnoreCase(SystemProperties.get("ro.com.android.dataroaming", "false")) | localCarrierConfigManager.getConfigForSubId(mPhone.getSubId()).getBoolean("carrier_default_data_roaming_enabled_bool");
  }
  
  private ApnSetting getPreferredApn()
  {
    Object localObject1;
    if ((mAllApnSettings != null) && (!mAllApnSettings.isEmpty()))
    {
      localObject1 = Long.toString(mPhone.getSubId());
      localObject1 = Uri.withAppendedPath(PREFERAPN_NO_UPDATE_URI_USING_SUBID, (String)localObject1);
      localObject1 = mPhone.getContext().getContentResolver().query((Uri)localObject1, new String[] { "_id", "name", "apn" }, null, null, "name ASC");
      int i = 0;
      if (localObject1 != null) {
        mCanSetPreferApn = true;
      } else {
        mCanSetPreferApn = false;
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("getPreferredApn: mRequestedApnType=");
      ((StringBuilder)localObject2).append(mRequestedApnType);
      ((StringBuilder)localObject2).append(" cursor=");
      ((StringBuilder)localObject2).append(localObject1);
      ((StringBuilder)localObject2).append(" cursor.count=");
      if (localObject1 != null) {
        i = ((Cursor)localObject1).getCount();
      }
      ((StringBuilder)localObject2).append(i);
      log(((StringBuilder)localObject2).toString());
      if ((mCanSetPreferApn) && (((Cursor)localObject1).getCount() > 0))
      {
        ((Cursor)localObject1).moveToFirst();
        i = ((Cursor)localObject1).getInt(((Cursor)localObject1).getColumnIndexOrThrow("_id"));
        Iterator localIterator = mAllApnSettings.iterator();
        while (localIterator.hasNext())
        {
          localObject2 = (ApnSetting)localIterator.next();
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("getPreferredApn: apnSetting=");
          localStringBuilder.append(localObject2);
          log(localStringBuilder.toString());
          if ((id == i) && (((ApnSetting)localObject2).canHandleType(mRequestedApnType)))
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("getPreferredApn: X found apnSetting");
            localStringBuilder.append(localObject2);
            log(localStringBuilder.toString());
            ((Cursor)localObject1).close();
            return localObject2;
          }
        }
      }
      if (localObject1 != null) {
        ((Cursor)localObject1).close();
      }
      log("getPreferredApn: X not found");
      return null;
    }
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("getPreferredApn: mAllApnSettings is ");
    if (mAllApnSettings == null) {
      localObject1 = "null";
    } else {
      localObject1 = "empty";
    }
    ((StringBuilder)localObject2).append((String)localObject1);
    log(((StringBuilder)localObject2).toString());
    return null;
  }
  
  private int getPreferredApnSetId()
  {
    ContentResolver localContentResolver = mPhone.getContext().getContentResolver();
    Object localObject = Telephony.Carriers.CONTENT_URI;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("preferapnset/subId/");
    localStringBuilder.append(mPhone.getSubId());
    localObject = localContentResolver.query(Uri.withAppendedPath((Uri)localObject, localStringBuilder.toString()), new String[] { "apn_set_id" }, null, null, null);
    if (((Cursor)localObject).getCount() < 1)
    {
      loge("getPreferredApnSetId: no APNs found");
      return 0;
    }
    ((Cursor)localObject).moveToFirst();
    return ((Cursor)localObject).getInt(0);
  }
  
  private int getRecoveryAction()
  {
    return Settings.System.getInt(mResolver, "radio.data.stall.recovery.action", 0);
  }
  
  private IccRecords getUiccRecords(int paramInt)
  {
    return mUiccController.getIccRecords(mPhone.getPhoneId(), paramInt);
  }
  
  private ApnContext getValidApnContext(AsyncResult paramAsyncResult, String paramString)
  {
    if ((paramAsyncResult != null) && ((userObj instanceof Pair)))
    {
      paramAsyncResult = (Pair)userObj;
      ApnContext localApnContext = (ApnContext)first;
      if (localApnContext != null)
      {
        int i = localApnContext.getConnectionGeneration();
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("getValidApnContext (");
        localStringBuilder.append(paramString);
        localStringBuilder.append(") on ");
        localStringBuilder.append(localApnContext);
        localStringBuilder.append(" got ");
        localStringBuilder.append(i);
        localStringBuilder.append(" vs ");
        localStringBuilder.append(second);
        log(localStringBuilder.toString());
        if (i == ((Integer)second).intValue()) {
          return localApnContext;
        }
        paramAsyncResult = new StringBuilder();
        paramAsyncResult.append("ignoring obsolete ");
        paramAsyncResult.append(paramString);
        log(paramAsyncResult.toString());
        return null;
      }
    }
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append(paramString);
    paramAsyncResult.append(": No apnContext");
    throw new RuntimeException(paramAsyncResult.toString());
  }
  
  private void handleDisconnectAfterScreenOff()
  {
    if ((((ConnectivityManager)mPhone.getContext().getSystemService("connectivity")).getTetheredIfaces().length <= 0) || (!isDefaultConnected()))
    {
      sendMessage(obtainMessage(270836, 0, 0));
      mReallyDisconnected = true;
    }
  }
  
  private void handlePcoData(AsyncResult paramAsyncResult)
  {
    if (exception != null)
    {
      localObject1 = LOG_TAG;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("PCO_DATA exception: ");
      ((StringBuilder)localObject2).append(exception);
      Rlog.e((String)localObject1, ((StringBuilder)localObject2).toString());
      return;
    }
    paramAsyncResult = (PcoData)result;
    Object localObject2 = new ArrayList();
    Object localObject1 = mDcc.getActiveDcByCid(cid);
    if (localObject1 != null) {
      ((ArrayList)localObject2).add(localObject1);
    }
    Object localObject3;
    Object localObject4;
    if (((ArrayList)localObject2).size() == 0)
    {
      localObject3 = LOG_TAG;
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("PCO_DATA for unknown cid: ");
      ((StringBuilder)localObject1).append(cid);
      ((StringBuilder)localObject1).append(", inferring");
      Rlog.e((String)localObject3, ((StringBuilder)localObject1).toString());
      localObject1 = mDataConnections.values().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject4 = (DataConnection)((Iterator)localObject1).next();
        int i = ((DataConnection)localObject4).getCid();
        if (i == cid)
        {
          ((ArrayList)localObject2).clear();
          ((ArrayList)localObject2).add(localObject4);
          break;
        }
        if (i == -1)
        {
          localObject3 = mApnContexts.keySet().iterator();
          while (((Iterator)localObject3).hasNext()) {
            if (((ApnContext)((Iterator)localObject3).next()).getState() == DctConstants.State.CONNECTING)
            {
              ((ArrayList)localObject2).add(localObject4);
              break;
            }
          }
        }
      }
    }
    if (((ArrayList)localObject2).size() == 0)
    {
      Rlog.e(LOG_TAG, "PCO_DATA - couldn't infer cid");
      return;
    }
    localObject2 = ((ArrayList)localObject2).iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (DataConnection)((Iterator)localObject2).next();
      if (mApnContexts.size() == 0) {
        break;
      }
      localObject1 = mApnContexts.keySet().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject4 = ((ApnContext)((Iterator)localObject1).next()).getApnType();
        localObject3 = new Intent("com.android.internal.telephony.CARRIER_SIGNAL_PCO_VALUE");
        ((Intent)localObject3).putExtra("apnType", (String)localObject4);
        ((Intent)localObject3).putExtra("apnProto", bearerProto);
        ((Intent)localObject3).putExtra("pcoId", pcoId);
        ((Intent)localObject3).putExtra("pcoValue", contents);
        mPhone.getCarrierSignalAgent().notifyCarrierSignalReceivers((Intent)localObject3);
      }
    }
  }
  
  private void handleStartNetStatPoll(DctConstants.Activity paramActivity)
  {
    startNetStatPoll();
    startDataStallAlarm(false);
    setActivity(paramActivity);
  }
  
  private void handleStopNetStatPoll(DctConstants.Activity paramActivity)
  {
    stopNetStatPoll();
    stopDataStallAlarm();
    setActivity(paramActivity);
  }
  
  private void initApnContexts()
  {
    log("initApnContexts: E");
    String[] arrayOfString = mPhone.getContext().getResources().getStringArray(17236068);
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      Object localObject1 = new NetworkConfig(arrayOfString[j]);
      Object localObject2;
      switch (type)
      {
      case 1: 
      case 6: 
      case 7: 
      case 8: 
      case 9: 
      case 13: 
      default: 
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("initApnContexts: skipping unknown type=");
        ((StringBuilder)localObject2).append(type);
        log(((StringBuilder)localObject2).toString());
        break;
      case 15: 
        localObject2 = addApnContext("emergency", (NetworkConfig)localObject1);
        break;
      case 14: 
        localObject2 = addApnContext("ia", (NetworkConfig)localObject1);
        break;
      case 12: 
        localObject2 = addApnContext("cbs", (NetworkConfig)localObject1);
        break;
      case 11: 
        localObject2 = addApnContext("ims", (NetworkConfig)localObject1);
        break;
      case 10: 
        localObject2 = addApnContext("fota", (NetworkConfig)localObject1);
        break;
      case 5: 
        localObject2 = addApnContext("hipri", (NetworkConfig)localObject1);
        break;
      case 4: 
        localObject2 = addApnContext("dun", (NetworkConfig)localObject1);
        break;
      case 3: 
        localObject2 = addApnContext("supl", (NetworkConfig)localObject1);
        break;
      case 2: 
        localObject2 = addApnContext("mms", (NetworkConfig)localObject1);
        break;
      case 0: 
        localObject2 = addApnContext("default", (NetworkConfig)localObject1);
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("initApnContexts: apnContext=");
      ((StringBuilder)localObject1).append(localObject2);
      log(((StringBuilder)localObject1).toString());
    }
  }
  
  private void initEmergencyApnSetting()
  {
    Cursor localCursor = mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "filtered"), null, "type=\"emergency\"", null, null);
    if (localCursor != null)
    {
      if ((localCursor.getCount() > 0) && (localCursor.moveToFirst())) {
        mEmergencyApn = makeApnSetting(localCursor);
      }
      localCursor.close();
    }
  }
  
  private boolean isConnected()
  {
    Iterator localIterator = mApnContexts.values().iterator();
    while (localIterator.hasNext()) {
      if (((ApnContext)localIterator.next()).getState() == DctConstants.State.CONNECTED) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isDataRoamingFromUserAction()
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mPhone.getContext());
    if ((!localSharedPreferences.contains("data_roaming_is_user_setting_key")) && (Settings.Global.getInt(mResolver, "device_provisioned", 0) == 0)) {
      localSharedPreferences.edit().putBoolean("data_roaming_is_user_setting_key", false).commit();
    }
    return localSharedPreferences.getBoolean("data_roaming_is_user_setting_key", true);
  }
  
  private boolean isHigherPriorityApnContextActive(ApnContext paramApnContext)
  {
    if (paramApnContext.getApnType().equals("ims")) {
      return false;
    }
    Iterator localIterator = mPrioritySortedApnContexts.iterator();
    while (localIterator.hasNext())
    {
      ApnContext localApnContext = (ApnContext)localIterator.next();
      if (!localApnContext.getApnType().equals("ims"))
      {
        if (paramApnContext.getApnType().equalsIgnoreCase(localApnContext.getApnType())) {
          return false;
        }
        if ((localApnContext.isEnabled()) && (localApnContext.getState() != DctConstants.State.FAILED)) {
          return true;
        }
      }
    }
    return false;
  }
  
  private boolean isOnlySingleDcAllowed(int paramInt)
  {
    Object localObject1 = null;
    Object localObject2 = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    Object localObject3 = localObject1;
    if (localObject2 != null)
    {
      localObject2 = ((CarrierConfigManager)localObject2).getConfig();
      localObject3 = localObject1;
      if (localObject2 != null) {
        localObject3 = ((PersistableBundle)localObject2).getIntArray("only_single_dc_allowed_int_array");
      }
    }
    boolean bool1 = false;
    boolean bool2 = Build.IS_DEBUGGABLE;
    int i = 0;
    boolean bool3 = bool1;
    if (bool2)
    {
      bool3 = bool1;
      if (SystemProperties.getBoolean("persist.telephony.test.singleDc", false)) {
        bool3 = true;
      }
    }
    bool1 = bool3;
    if (localObject3 != null) {
      for (;;)
      {
        bool1 = bool3;
        if (i >= localObject3.length) {
          break;
        }
        bool1 = bool3;
        if (bool3) {
          break;
        }
        if (paramInt == localObject3[i]) {
          bool3 = true;
        }
        i++;
      }
    }
    localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("isOnlySingleDcAllowed(");
    ((StringBuilder)localObject3).append(paramInt);
    ((StringBuilder)localObject3).append("): ");
    ((StringBuilder)localObject3).append(bool1);
    log(((StringBuilder)localObject3).toString());
    return bool1;
  }
  
  private boolean isPhoneStateIdle()
  {
    for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++)
    {
      Object localObject = PhoneFactory.getPhone(i);
      if ((localObject != null) && (((Phone)localObject).getState() != PhoneConstants.State.IDLE))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("isPhoneStateIdle false: Voice call active on phone ");
        ((StringBuilder)localObject).append(i);
        log(((StringBuilder)localObject).toString());
        return false;
      }
    }
    return true;
  }
  
  private boolean isProvisioningApn(String paramString)
  {
    paramString = (ApnContext)mApnContexts.get(paramString);
    if (paramString != null) {
      return paramString.isProvisioningApn();
    }
    return false;
  }
  
  private void loge(String paramString)
  {
    String str = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.e(str, localStringBuilder.toString());
  }
  
  private ApnSetting makeApnSetting(Cursor paramCursor)
  {
    String[] arrayOfString = parseTypes(paramCursor.getString(paramCursor.getColumnIndexOrThrow("type")));
    int i = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("network_type_bitmask"));
    int j = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("_id"));
    String str1 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("numeric"));
    String str2 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("name"));
    String str3 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("apn"));
    String str4 = NetworkUtils.trimV4AddrZeros(paramCursor.getString(paramCursor.getColumnIndexOrThrow("proxy")));
    String str5 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("port"));
    String str6 = NetworkUtils.trimV4AddrZeros(paramCursor.getString(paramCursor.getColumnIndexOrThrow("mmsc")));
    String str7 = NetworkUtils.trimV4AddrZeros(paramCursor.getString(paramCursor.getColumnIndexOrThrow("mmsproxy")));
    String str8 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("mmsport"));
    String str9 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("user"));
    String str10 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("password"));
    int k = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("authtype"));
    String str11 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("protocol"));
    String str12 = paramCursor.getString(paramCursor.getColumnIndexOrThrow("roaming_protocol"));
    boolean bool1;
    if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("carrier_enabled")) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    int m = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("profile_id"));
    boolean bool2;
    if (paramCursor.getInt(paramCursor.getColumnIndexOrThrow("modem_cognitive")) == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    return new ApnSetting(j, str1, str2, str3, str4, str5, str6, str7, str8, str9, str10, k, arrayOfString, str11, str12, bool1, i, m, bool2, paramCursor.getInt(paramCursor.getColumnIndexOrThrow("max_conns")), paramCursor.getInt(paramCursor.getColumnIndexOrThrow("wait_time")), paramCursor.getInt(paramCursor.getColumnIndexOrThrow("max_conns_time")), paramCursor.getInt(paramCursor.getColumnIndexOrThrow("mtu")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("mvno_type")), paramCursor.getString(paramCursor.getColumnIndexOrThrow("mvno_match_data")), paramCursor.getInt(paramCursor.getColumnIndexOrThrow("apn_set_id")));
  }
  
  private ApnSetting mergeApns(ApnSetting paramApnSetting1, ApnSetting paramApnSetting2)
  {
    int i = id;
    Object localObject1 = new ArrayList();
    ((ArrayList)localObject1).addAll(Arrays.asList(types));
    for (localObject3 : types)
    {
      if (!((ArrayList)localObject1).contains(localObject3)) {
        ((ArrayList)localObject1).add(localObject3);
      }
      if (((String)localObject3).equals("default")) {
        i = id;
      }
    }
    if (TextUtils.isEmpty(mmsc)) {}
    for (??? = mmsc;; ??? = mmsc) {
      break;
    }
    if (TextUtils.isEmpty(mmsProxy)) {}
    for (Object localObject3 = mmsProxy;; localObject3 = mmsProxy) {
      break;
    }
    if (TextUtils.isEmpty(mmsPort)) {}
    for (String str1 = mmsPort;; str1 = mmsPort) {
      break;
    }
    if (TextUtils.isEmpty(proxy)) {}
    for (String str2 = proxy;; str2 = proxy) {
      break;
    }
    if (TextUtils.isEmpty(port)) {}
    for (String str3 = port;; str3 = port) {
      break;
    }
    if (protocol.equals("IPV4V6")) {}
    for (String str4 = protocol;; str4 = protocol) {
      break;
    }
    if (roamingProtocol.equals("IPV4V6")) {}
    for (String str5 = roamingProtocol;; str5 = roamingProtocol) {
      break;
    }
    if ((networkTypeBitmask != 0) && (networkTypeBitmask != 0)) {
      ??? = networkTypeBitmask | networkTypeBitmask;
    } else {
      ??? = 0;
    }
    ??? = ???;
    if (??? == 0)
    {
      if ((bearerBitmask != 0) && (bearerBitmask != 0)) {
        ??? = bearerBitmask | bearerBitmask;
      } else {
        ??? = 0;
      }
      ??? = ServiceState.convertBearerBitmaskToNetworkTypeBitmask(???);
    }
    String str6 = numeric;
    String str7 = carrier;
    String str8 = apn;
    String str9 = user;
    String str10 = password;
    ??? = authType;
    localObject1 = (String[])((ArrayList)localObject1).toArray(new String[0]);
    boolean bool1 = carrierEnabled;
    int m = profileId;
    boolean bool2;
    if ((!modemCognitive) && (!modemCognitive)) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    return new ApnSetting(i, str6, str7, str8, str2, str3, (String)???, (String)localObject3, str1, str9, str10, ???, (String[])localObject1, str4, str5, bool1, ???, m, bool2, maxConns, waitTime, maxConnsTime, mtu, mvnoType, mvnoMatchData, apnSetId);
  }
  
  private void notifyAllDataDisconnected()
  {
    sEnableFailFastRefCounter = 0;
    mFailFast = false;
    mAllDataDisconnectedRegistrants.notifyRegistrants();
  }
  
  private void notifyDataConnection(String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("notifyDataConnection: reason=");
    ((StringBuilder)localObject).append(paramString);
    log(((StringBuilder)localObject).toString());
    Iterator localIterator = mApnContexts.values().iterator();
    while (localIterator.hasNext())
    {
      ApnContext localApnContext = (ApnContext)localIterator.next();
      if ((mAttached.get()) && (localApnContext.isReady()))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("notifyDataConnection: type:");
        ((StringBuilder)localObject).append(localApnContext.getApnType());
        log(((StringBuilder)localObject).toString());
        Phone localPhone = mPhone;
        if (paramString != null) {
          localObject = paramString;
        } else {
          localObject = localApnContext.getReason();
        }
        localPhone.notifyDataConnection((String)localObject, localApnContext.getApnType());
      }
    }
    notifyOffApnsOfAvailability(paramString);
  }
  
  private void notifyDataDisconnectComplete()
  {
    log("notifyDataDisconnectComplete");
    Iterator localIterator = mDisconnectAllCompleteMsgList.iterator();
    while (localIterator.hasNext()) {
      ((Message)localIterator.next()).sendToTarget();
    }
    mDisconnectAllCompleteMsgList.clear();
  }
  
  private void notifyNoData(DcFailCause paramDcFailCause, ApnContext paramApnContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("notifyNoData: type=");
    localStringBuilder.append(paramApnContext.getApnType());
    log(localStringBuilder.toString());
    if ((isPermanentFailure(paramDcFailCause)) && (!paramApnContext.getApnType().equals("default"))) {
      mPhone.notifyDataConnectionFailed(paramApnContext.getReason(), paramApnContext.getApnType());
    }
  }
  
  private void onActionIntentDataStallAlarm(Intent paramIntent)
  {
    Message localMessage = obtainMessage(270353, paramIntent.getAction());
    arg1 = paramIntent.getIntExtra("data.stall.alram.tag", 0);
    sendMessage(localMessage);
  }
  
  private void onActionIntentProvisioningApnAlarm(Intent paramIntent)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("onActionIntentProvisioningApnAlarm: action=");
    ((StringBuilder)localObject).append(paramIntent.getAction());
    log(((StringBuilder)localObject).toString());
    localObject = obtainMessage(270375, paramIntent.getAction());
    arg1 = paramIntent.getIntExtra("provisioning.apn.alarm.tag", 0);
    sendMessage((Message)localObject);
  }
  
  private void onActionIntentReconnectAlarm(Intent paramIntent)
  {
    Message localMessage = obtainMessage(270383);
    localMessage.setData(paramIntent.getExtras());
    sendMessage(localMessage);
  }
  
  private void onApnChanged()
  {
    DctConstants.State localState1 = getOverallState();
    DctConstants.State localState2 = DctConstants.State.IDLE;
    boolean bool = true;
    int i;
    if ((localState1 != localState2) && (localState1 != DctConstants.State.FAILED)) {
      i = 0;
    } else {
      i = 1;
    }
    if ((mPhone instanceof GsmCdmaPhone)) {
      ((GsmCdmaPhone)mPhone).updateCurrentCarrierInProvider();
    }
    log("onApnChanged: createAllApnList and cleanUpAllConnections");
    createAllApnList();
    setInitialAttachApn();
    if (i != 0) {
      bool = false;
    }
    cleanUpConnectionsOnUpdatedApns(bool, "apnChanged");
    if (mPhone.getSubId() == SubscriptionManager.getDefaultDataSubscriptionId()) {
      setupDataOnConnectableApns("apnChanged");
    }
  }
  
  private void onCleanUpAllConnections(String paramString)
  {
    cleanUpAllConnections(true, paramString);
  }
  
  private void onCleanUpConnection(boolean paramBoolean, int paramInt, String paramString)
  {
    log("onCleanUpConnection");
    ApnContext localApnContext = (ApnContext)mApnContextsById.get(paramInt);
    if (localApnContext != null)
    {
      localApnContext.setReason(paramString);
      cleanUpConnection(paramBoolean, localApnContext);
    }
  }
  
  private void onDataConnectionAttached()
  {
    log("onDataConnectionAttached");
    mAttached.set(true);
    if (getOverallState() == DctConstants.State.CONNECTED)
    {
      log("onDataConnectionAttached: start polling notify attached");
      startNetStatPoll();
      startDataStallAlarm(false);
      notifyDataConnection("dataAttached");
    }
    else
    {
      notifyOffApnsOfAvailability("dataAttached");
    }
    if (mAutoAttachOnCreationConfig) {
      mAutoAttachOnCreation.set(true);
    }
    setupDataOnConnectableApns("dataAttached");
  }
  
  private void onDataConnectionDetached()
  {
    log("onDataConnectionDetached: stop polling and notify detached");
    stopNetStatPoll();
    stopDataStallAlarm();
    notifyDataConnection("dataDetached");
    mAttached.set(false);
  }
  
  private void onDataConnectionRedirected(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      Object localObject = new Intent("com.android.internal.telephony.CARRIER_SIGNAL_REDIRECTED");
      ((Intent)localObject).putExtra("redirectionUrl", paramString);
      mPhone.getCarrierSignalAgent().notifyCarrierSignalReceivers((Intent)localObject);
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Notify carrier signal receivers with redirectUrl: ");
      ((StringBuilder)localObject).append(paramString);
      log(((StringBuilder)localObject).toString());
    }
  }
  
  private void onDataRatChange()
  {
    if ((isConnected()) && (mInVoiceCall)) {
      if ((mVoiceAndDataNotAllowAndInVoice) && (mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()))
      {
        mVoiceAndDataNotAllowAndInVoice = false;
        startNetStatPoll();
        startDataStallAlarm(false);
        notifyDataConnection("dataRatChange");
      }
      else if ((!mVoiceAndDataNotAllowAndInVoice) && (!mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()))
      {
        mVoiceAndDataNotAllowAndInVoice = true;
        stopNetStatPoll();
        stopDataStallAlarm();
        notifyDataConnection("dataRatChange");
      }
    }
  }
  
  private void onDataReconnect(Bundle paramBundle)
  {
    Object localObject1 = paramBundle.getString("reconnect_alarm_extra_reason");
    Object localObject2 = paramBundle.getString("reconnect_alarm_extra_type");
    int i = mPhone.getSubId();
    int j = paramBundle.getInt("subscription", -1);
    paramBundle = new StringBuilder();
    paramBundle.append("onDataReconnect: currSubId = ");
    paramBundle.append(j);
    paramBundle.append(" phoneSubId=");
    paramBundle.append(i);
    log(paramBundle.toString());
    if ((mSubscriptionManager.isActiveSubId(j)) && (j == i))
    {
      paramBundle = (ApnContext)mApnContexts.get(localObject2);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onDataReconnect: mState=");
      localStringBuilder.append(mState);
      localStringBuilder.append(" reason=");
      localStringBuilder.append((String)localObject1);
      localStringBuilder.append(" apnType=");
      localStringBuilder.append((String)localObject2);
      localStringBuilder.append(" apnContext=");
      localStringBuilder.append(paramBundle);
      localStringBuilder.append(" mDataConnectionAsyncChannels=");
      localStringBuilder.append(mDataConnectionAcHashMap);
      log(localStringBuilder.toString());
      if ((paramBundle != null) && (paramBundle.isEnabled()))
      {
        paramBundle.setReason((String)localObject1);
        localObject2 = paramBundle.getState();
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("onDataReconnect: apnContext state=");
        ((StringBuilder)localObject1).append(localObject2);
        log(((StringBuilder)localObject1).toString());
        if ((localObject2 != DctConstants.State.FAILED) && (localObject2 != DctConstants.State.IDLE))
        {
          log("onDataReconnect: keep associated");
        }
        else
        {
          log("onDataReconnect: state is FAILED|IDLE, disassociate");
          localObject1 = paramBundle.getDcAc();
          if (localObject1 != null)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("onDataReconnect: tearDown apnContext=");
            ((StringBuilder)localObject2).append(paramBundle);
            log(((StringBuilder)localObject2).toString());
            ((DcAsyncChannel)localObject1).tearDown(paramBundle, "", null);
          }
          paramBundle.setDataConnectionAc(null);
          paramBundle.setState(DctConstants.State.IDLE);
        }
        sendMessage(obtainMessage(270339, paramBundle));
        paramBundle.setReconnectIntent(null);
      }
      return;
    }
    log("receive ReconnectAlarm but subId incorrect, ignore");
  }
  
  private void onDataRoamingOff()
  {
    log("onDataRoamingOff");
    if (!getDataRoamingEnabled())
    {
      setInitialAttachApn();
      setDataProfilesAsNeeded();
      notifyOffApnsOfAvailability("roamingOff");
      setupDataOnConnectableApns("roamingOff");
    }
    else
    {
      notifyDataConnection("roamingOff");
    }
  }
  
  private void onDataRoamingOnOrSettingsChanged(int paramInt)
  {
    log("onDataRoamingOnOrSettingsChanged");
    boolean bool;
    if (paramInt == 270384) {
      bool = true;
    } else {
      bool = false;
    }
    if (!mPhone.getServiceState().getDataRoaming())
    {
      log("device is not roaming. ignored the request.");
      return;
    }
    checkDataRoamingStatus(bool);
    if (getDataRoamingEnabled())
    {
      log("onDataRoamingOnOrSettingsChanged: setup data on roaming");
      setupDataOnConnectableApns("roamingOn");
      notifyDataConnection("roamingOn");
    }
    else
    {
      log("onDataRoamingOnOrSettingsChanged: Tear down data connection on roaming.");
      String str1 = mPhone.getOperatorNumeric();
      String str2 = mPhone.getServiceState().getOperatorNumeric();
      if ((mPhone.getServiceState().getDataRoaming()) && (str1 != null) && ((str1.startsWith("22288")) || (str1.startsWith("22299"))) && (str2 != null) && (str2.startsWith("222")))
      {
        log("[ABSP][onDataRoamingOnOrSettingsChanged] National roaming when use WINDTre sim, should not tear down pdp");
      }
      else
      {
        cleanUpAllConnections(true, "roamingOn");
        notifyOffApnsOfAvailability("roamingOn");
      }
    }
  }
  
  private void onDataServiceBindingChanged(boolean paramBoolean)
  {
    if (paramBoolean) {
      mDcc.start();
    } else {
      mDcc.dispose();
    }
  }
  
  private void onDataSetupComplete(AsyncResult paramAsyncResult)
  {
    Object localObject1 = DcFailCause.UNKNOWN;
    int i = 0;
    ApnContext localApnContext = getValidApnContext(paramAsyncResult, "onDataSetupComplete");
    if (localApnContext == null) {
      return;
    }
    Object localObject3;
    int j;
    ApnSetting localApnSetting;
    Object localObject4;
    Object localObject2;
    if (exception == null)
    {
      localObject3 = localApnContext.getDcAc();
      if (localObject3 == null)
      {
        log("onDataSetupComplete: no connection to DC, handle as error");
        localObject1 = DcFailCause.CONNECTION_TO_DATACONNECTIONAC_BROKEN;
        j = 1;
      }
      else
      {
        localApnSetting = localApnContext.getApnSetting();
        localObject4 = new StringBuilder();
        ((StringBuilder)localObject4).append("onDataSetupComplete: success apn=");
        if (localApnSetting == null) {
          localObject1 = "unknown";
        } else {
          localObject1 = apn;
        }
        ((StringBuilder)localObject4).append((String)localObject1);
        log(((StringBuilder)localObject4).toString());
        if ((localApnSetting != null) && (proxy != null) && (proxy.length() != 0)) {
          try
          {
            localObject4 = port;
            localObject1 = localObject4;
            if (TextUtils.isEmpty((CharSequence)localObject4)) {
              localObject1 = "8080";
            }
            localObject4 = new android/net/ProxyInfo;
            ((ProxyInfo)localObject4).<init>(proxy, Integer.parseInt((String)localObject1), null);
            ((DcAsyncChannel)localObject3).setLinkPropertiesHttpProxySync((ProxyInfo)localObject4);
          }
          catch (NumberFormatException localNumberFormatException)
          {
            localObject4 = new StringBuilder();
            ((StringBuilder)localObject4).append("onDataSetupComplete: NumberFormatException making ProxyProperties (");
            ((StringBuilder)localObject4).append(port);
            ((StringBuilder)localObject4).append("): ");
            ((StringBuilder)localObject4).append(localNumberFormatException);
            loge(((StringBuilder)localObject4).toString());
          }
        }
        if (TextUtils.equals(localApnContext.getApnType(), "default"))
        {
          try
          {
            SystemProperties.set("gsm.defaultpdpcontext.active", "true");
          }
          catch (RuntimeException localRuntimeException1)
          {
            log("Failed to set PUPPET_MASTER_RADIO_STRESS_TEST to true");
          }
          if ((mCanSetPreferApn) && (mPreferredApn == null))
          {
            log("onDataSetupComplete: PREFERRED APN is null");
            mPreferredApn = localApnSetting;
            if (mPreferredApn != null) {
              setPreferredApn(mPreferredApn.id);
            }
          }
        }
        else
        {
          try
          {
            SystemProperties.set("gsm.defaultpdpcontext.active", "false");
          }
          catch (RuntimeException localRuntimeException2)
          {
            log("Failed to set PUPPET_MASTER_RADIO_STRESS_TEST to false");
          }
        }
        localApnContext.setState(DctConstants.State.CONNECTED);
        checkDataRoamingStatus(false);
        boolean bool = localApnContext.isProvisioningApn();
        localObject2 = ConnectivityManager.from(mPhone.getContext());
        if (mProvisionBroadcastReceiver != null)
        {
          mPhone.getContext().unregisterReceiver(mProvisionBroadcastReceiver);
          mProvisionBroadcastReceiver = null;
        }
        if ((bool) && (!mIsProvisioning))
        {
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("onDataSetupComplete: successful, BUT send connected to prov apn as mIsProvisioning:");
          ((StringBuilder)localObject4).append(mIsProvisioning);
          ((StringBuilder)localObject4).append(" == false && (isProvisioningApn:");
          ((StringBuilder)localObject4).append(bool);
          ((StringBuilder)localObject4).append(" == true");
          log(((StringBuilder)localObject4).toString());
          mProvisionBroadcastReceiver = new ProvisionNotificationBroadcastReceiver(((ConnectivityManager)localObject2).getMobileProvisioningUrl(), TelephonyManager.getDefault().getNetworkOperatorName());
          mPhone.getContext().registerReceiver(mProvisionBroadcastReceiver, new IntentFilter(mProvisionActionName));
          ((ConnectivityManager)localObject2).setProvisioningNotificationVisible(true, 0, mProvisionActionName);
          setRadio(false);
        }
        else
        {
          ((ConnectivityManager)localObject2).setProvisioningNotificationVisible(false, 0, mProvisionActionName);
          completeConnection(localApnContext);
        }
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("onDataSetupComplete: SETUP complete type=");
        ((StringBuilder)localObject2).append(localApnContext.getApnType());
        ((StringBuilder)localObject2).append(", reason:");
        ((StringBuilder)localObject2).append(localApnContext.getReason());
        log(((StringBuilder)localObject2).toString());
        j = i;
        if (Build.IS_DEBUGGABLE)
        {
          int k = SystemProperties.getInt("persist.radio.test.pco", -1);
          j = i;
          if (k != -1)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("PCO testing: read pco value from persist.radio.test.pco ");
            ((StringBuilder)localObject2).append(k);
            log(((StringBuilder)localObject2).toString());
            int m = (byte)k;
            localObject2 = new Intent("com.android.internal.telephony.CARRIER_SIGNAL_PCO_VALUE");
            ((Intent)localObject2).putExtra("apnType", "default");
            ((Intent)localObject2).putExtra("apnProto", "IPV4V6");
            ((Intent)localObject2).putExtra("pcoId", 65280);
            ((Intent)localObject2).putExtra("pcoValue", new byte[] { m });
            mPhone.getCarrierSignalAgent().notifyCarrierSignalReceivers((Intent)localObject2);
            j = i;
          }
        }
      }
    }
    else
    {
      localObject4 = (DcFailCause)result;
      localObject2 = localApnContext.getApnSetting();
      if (localObject2 == null) {
        localObject2 = "unknown";
      } else {
        localObject2 = apn;
      }
      log(String.format("onDataSetupComplete: error apn=%s cause=%s", new Object[] { localObject2, localObject4 }));
      if (((DcFailCause)localObject4).isEventLoggable())
      {
        j = getCellLocationId();
        EventLog.writeEvent(50105, new Object[] { Integer.valueOf(((DcFailCause)localObject4).ordinal()), Integer.valueOf(j), Integer.valueOf(TelephonyManager.getDefault().getNetworkType()) });
      }
      localApnSetting = localApnContext.getApnSetting();
      Phone localPhone = mPhone;
      localObject3 = localApnContext.getReason();
      String str = localApnContext.getApnType();
      if (localApnSetting != null) {
        localObject2 = apn;
      } else {
        localObject2 = "unknown";
      }
      localPhone.notifyPreciseDataConnectionFailed((String)localObject3, str, (String)localObject2, ((DcFailCause)localObject4).toString());
      localObject2 = new Intent("com.android.internal.telephony.CARRIER_SIGNAL_REQUEST_NETWORK_FAILED");
      ((Intent)localObject2).putExtra("errorCode", ((DcFailCause)localObject4).getErrorCode());
      ((Intent)localObject2).putExtra("apnType", localApnContext.getApnType());
      mPhone.getCarrierSignalAgent().notifyCarrierSignalReceivers((Intent)localObject2);
      if ((((DcFailCause)localObject4).isRestartRadioFail(mPhone.getContext(), mPhone.getSubId())) || (localApnContext.restartOnError(((DcFailCause)localObject4).getErrorCode())))
      {
        log("Modem restarted.");
        sendRestartRadio();
      }
      if (isPermanentFailure((DcFailCause)localObject4))
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("cause = ");
        ((StringBuilder)localObject2).append(localObject4);
        ((StringBuilder)localObject2).append(", mark apn as permanent failed. apn = ");
        ((StringBuilder)localObject2).append(localApnSetting);
        log(((StringBuilder)localObject2).toString());
        localApnContext.markApnPermanentFailed(localApnSetting);
      }
      j = 1;
    }
    if (j != 0) {
      onDataSetupCompleteError(paramAsyncResult);
    }
    if (!mDataEnabledSettings.isInternalDataEnabled()) {
      cleanUpAllConnections("dataDisabled");
    }
  }
  
  private void onDataSetupCompleteError(AsyncResult paramAsyncResult)
  {
    ApnContext localApnContext = getValidApnContext(paramAsyncResult, "onDataSetupCompleteError");
    if (localApnContext == null) {
      return;
    }
    long l = localApnContext.getDelayForNextApn(mFailFast);
    if (l >= 0L)
    {
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("onDataSetupCompleteError: Try next APN. delay = ");
      paramAsyncResult.append(l);
      log(paramAsyncResult.toString());
      localApnContext.setState(DctConstants.State.SCANNING);
      startAlarmForReconnect(l, localApnContext);
    }
    else
    {
      localApnContext.setState(DctConstants.State.FAILED);
      mPhone.notifyDataConnection("apnFailed", localApnContext.getApnType());
      localApnContext.setDataConnectionAc(null);
      log("onDataSetupCompleteError: Stop retrying APNs.");
    }
  }
  
  private void onDataStallAlarm(int paramInt)
  {
    StringBuilder localStringBuilder;
    if (mDataStallAlarmTag != paramInt)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("onDataStallAlarm: ignore, tag=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" expecting ");
      localStringBuilder.append(mDataStallAlarmTag);
      log(localStringBuilder.toString());
      return;
    }
    updateDataStallInfo();
    int i = Settings.Global.getInt(mResolver, "pdp_watchdog_trigger_packet_count", 15);
    boolean bool = false;
    if ((mSentSinceLastRecv < i) && (mDnsSentSinceLastRecv < 5))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("onDataStallAlarm: tag=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" Sent ");
      localStringBuilder.append(String.valueOf(mSentSinceLastRecv));
      localStringBuilder.append(" pkts since last received, < watchdogTrigger=");
      localStringBuilder.append(i);
      localStringBuilder.append(" Sent(DNS) ");
      localStringBuilder.append(String.valueOf(mDnsSentSinceLastRecv));
      localStringBuilder.append(" DNS pkts since last received, < watchdogTrigger(DNS)=");
      localStringBuilder.append(5);
      log(localStringBuilder.toString());
    }
    else
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("onDataStallAlarm: tag=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" do recovery action=");
      localStringBuilder.append(getRecoveryAction());
      log(localStringBuilder.toString());
      bool = true;
      sendMessage(obtainMessage(270354));
    }
    startDataStallAlarm(bool);
  }
  
  private void onDeviceProvisionedChange()
  {
    if (isDataEnabled())
    {
      reevaluateDataConnections();
      onTrySetupData("dataEnabled");
    }
    else
    {
      onCleanUpAllConnections("specificDisabled");
    }
  }
  
  private void onDisconnectDcRetrying(AsyncResult paramAsyncResult)
  {
    ApnContext localApnContext = getValidApnContext(paramAsyncResult, "onDisconnectDcRetrying");
    if (localApnContext == null) {
      return;
    }
    localApnContext.setState(DctConstants.State.RETRYING);
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("onDisconnectDcRetrying: apnContext=");
    paramAsyncResult.append(localApnContext);
    log(paramAsyncResult.toString());
    mPhone.notifyDataConnection(localApnContext.getReason(), localApnContext.getApnType());
  }
  
  private void onDisconnectDone(AsyncResult paramAsyncResult)
  {
    paramAsyncResult = getValidApnContext(paramAsyncResult, "onDisconnectDone");
    if (paramAsyncResult == null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onDisconnectDone: EVENT_DISCONNECT_DONE apnContext=");
    localStringBuilder.append(paramAsyncResult);
    log(localStringBuilder.toString());
    paramAsyncResult.setState(DctConstants.State.IDLE);
    mPhone.notifyDataConnection(paramAsyncResult.getReason(), paramAsyncResult.getApnType());
    if ((isDisconnected()) && (mPhone.getServiceStateTracker().processPendingRadioPowerOffAfterDataOff()))
    {
      log("onDisconnectDone: radio will be turned off, no retries");
      paramAsyncResult.setApnSetting(null);
      paramAsyncResult.setDataConnectionAc(null);
      if (mDisconnectPendingCount > 0) {
        mDisconnectPendingCount -= 1;
      }
      if (mDisconnectPendingCount == 0)
      {
        notifyDataDisconnectComplete();
        notifyAllDataDisconnected();
      }
      return;
    }
    if ((mAttached.get()) && (paramAsyncResult.isReady()) && (retryAfterDisconnected(paramAsyncResult)))
    {
      try
      {
        SystemProperties.set("gsm.defaultpdpcontext.active", "false");
      }
      catch (RuntimeException localRuntimeException)
      {
        log("Failed to set PUPPET_MASTER_RADIO_STRESS_TEST to false");
      }
      log("onDisconnectDone: attached, ready and retry after disconnect");
      long l = paramAsyncResult.getRetryAfterDisconnectDelay();
      if (l > 0L) {
        startAlarmForReconnect(l, paramAsyncResult);
      }
    }
    else
    {
      boolean bool = mPhone.getContext().getResources().getBoolean(17957015);
      if ((paramAsyncResult.isProvisioningApn()) && (bool))
      {
        log("onDisconnectDone: restartRadio after provisioning");
        restartRadio();
      }
      paramAsyncResult.setApnSetting(null);
      paramAsyncResult.setDataConnectionAc(null);
      if (isOnlySingleDcAllowed(mPhone.getServiceState().getRilDataRadioTechnology()))
      {
        log("onDisconnectDone: isOnlySigneDcAllowed true so setup single apn");
        setupDataOnConnectableApns("SinglePdnArbitration");
      }
      else
      {
        log("onDisconnectDone: not retrying");
      }
    }
    if (mDisconnectPendingCount > 0) {
      mDisconnectPendingCount -= 1;
    }
    if (mDisconnectPendingCount == 0)
    {
      paramAsyncResult.setConcurrentVoiceAndDataAllowed(mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed());
      notifyDataDisconnectComplete();
      notifyAllDataDisconnected();
      mPhone.notifyDataConnection("specificDisabled", "default");
    }
  }
  
  private void onEnableApn(int paramInt1, int paramInt2)
  {
    ApnContext localApnContext = (ApnContext)mApnContextsById.get(paramInt1);
    if (localApnContext == null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("onEnableApn(");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", ");
      localStringBuilder.append(paramInt2);
      localStringBuilder.append("): NO ApnContext");
      loge(localStringBuilder.toString());
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onEnableApn: apnContext=");
    localStringBuilder.append(localApnContext);
    localStringBuilder.append(" call applyNewState");
    log(localStringBuilder.toString());
    boolean bool = true;
    if (paramInt2 != 1) {
      bool = false;
    }
    applyNewState(localApnContext, bool, localApnContext.getDependencyMet());
    if ((paramInt2 == 0) && (isOnlySingleDcAllowed(mPhone.getServiceState().getRilDataRadioTechnology())) && (!isHigherPriorityApnContextActive(localApnContext)))
    {
      log("onEnableApn: isOnlySingleDcAllowed true & higher priority APN disabled");
      setupDataOnConnectableApns("SinglePdnArbitration");
    }
  }
  
  private void onRadioAvailable()
  {
    log("onRadioAvailable");
    if (mPhone.getSimulatedRadioControl() != null)
    {
      notifyDataConnection(null);
      log("onRadioAvailable: We're on the simulator; assuming data is connected");
    }
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    if ((localIccRecords != null) && (localIccRecords.getRecordsLoaded())) {
      notifyOffApnsOfAvailability(null);
    }
    if (getOverallState() != DctConstants.State.IDLE) {
      cleanUpConnection(true, null);
    }
    log("onRadioAvailable, set SAR_POWER_HEAD to modem.");
    mPhone.mCi.setTransmitPower(1, 0, obtainMessage(270436));
  }
  
  private void onRadioOffOrNotAvailable()
  {
    mReregisterOnReconnectFailure = false;
    mAutoAttachOnCreation.set(false);
    if (mPhone.getSimulatedRadioControl() != null)
    {
      log("We're on the simulator; assuming radio off is meaningless");
    }
    else
    {
      log("onRadioOffOrNotAvailable: is off and clean up all connections");
      cleanUpAllConnections(false, "radioTurnedOff");
    }
    notifyOffApnsOfAvailability(null);
  }
  
  private void onRetrySetupDefaultData()
  {
    if ((apnContextStored != null) && (isDdsSwitchWaitingOriDefaultDisconnect == true))
    {
      log("[ABSP][onRetrySetupDefaultData] Trigger setup data call");
      stopOldDefaultDataDisconnectTimeoutAlarm();
      apnContextStored.resetErrorCodeRetries();
      trySetupData(apnContextStored);
      apnContextStored = null;
      isDdsSwitchWaitingOriDefaultDisconnect = false;
    }
  }
  
  private void onSetCarrierDataEnabled(AsyncResult paramAsyncResult)
  {
    if (exception != null)
    {
      String str = LOG_TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CarrierDataEnable exception: ");
      localStringBuilder.append(exception);
      Rlog.e(str, localStringBuilder.toString());
      return;
    }
    boolean bool = ((Boolean)result).booleanValue();
    if (bool != mDataEnabledSettings.isCarrierDataEnabled())
    {
      paramAsyncResult = new StringBuilder();
      paramAsyncResult.append("carrier Action: set metered apns enabled: ");
      paramAsyncResult.append(bool);
      log(paramAsyncResult.toString());
      mDataEnabledSettings.setCarrierDataEnabled(bool);
      if (!bool)
      {
        mPhone.notifyOtaspChanged(5);
        cleanUpAllConnections(true, "carrierActionDisableMeteredApn");
      }
      else
      {
        int i = mPhone.getServiceStateTracker().getOtasp();
        mPhone.notifyOtaspChanged(i);
        reevaluateDataConnections();
        setupDataOnConnectableApns("dataEnabled");
      }
    }
  }
  
  private void onSetDependencyMet(String paramString, boolean paramBoolean)
  {
    if ("hipri".equals(paramString)) {
      return;
    }
    Object localObject = (ApnContext)mApnContexts.get(paramString);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onSetDependencyMet: ApnContext not found in onSetDependencyMet(");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(paramBoolean);
      ((StringBuilder)localObject).append(")");
      loge(((StringBuilder)localObject).toString());
      return;
    }
    applyNewState((ApnContext)localObject, ((ApnContext)localObject).isEnabled(), paramBoolean);
    if ("default".equals(paramString))
    {
      paramString = (ApnContext)mApnContexts.get("hipri");
      if (paramString != null) {
        applyNewState(paramString, paramString.isEnabled(), paramBoolean);
      }
    }
  }
  
  private void onSetInternalDataEnabled(boolean paramBoolean, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onSetInternalDataEnabled: enabled=");
    localStringBuilder.append(paramBoolean);
    log(localStringBuilder.toString());
    int i = 1;
    mDataEnabledSettings.setInternalDataEnabled(paramBoolean);
    if (paramBoolean)
    {
      log("onSetInternalDataEnabled: changed to enabled, try to setup data call");
      onTrySetupData("dataEnabled");
    }
    else
    {
      i = 0;
      log("onSetInternalDataEnabled: changed to disabled, cleanUpAllConnections");
      cleanUpAllConnections("dataDisabled", paramMessage);
    }
    if ((i != 0) && (paramMessage != null)) {
      paramMessage.sendToTarget();
    }
  }
  
  private void onSetPolicyDataEnabled(boolean paramBoolean)
  {
    boolean bool = isDataEnabled();
    if (mDataEnabledSettings.isPolicyDataEnabled() != paramBoolean)
    {
      mDataEnabledSettings.setPolicyDataEnabled(paramBoolean);
      if (bool != isDataEnabled()) {
        if (!bool)
        {
          reevaluateDataConnections();
          onTrySetupData("dataEnabled");
        }
        else
        {
          onCleanUpAllConnections("specificDisabled");
        }
      }
    }
  }
  
  private void onSetUserDataEnabled(boolean paramBoolean)
  {
    if (mDataEnabledSettings.isUserDataEnabled() != paramBoolean)
    {
      mDataEnabledSettings.setUserDataEnabled(paramBoolean);
      if ((!getDataRoamingEnabled()) && (mPhone.getServiceState().getDataRoaming())) {
        if (paramBoolean) {
          notifyOffApnsOfAvailability("roamingOn");
        } else {
          notifyOffApnsOfAvailability("dataDisabled");
        }
      }
      mPhone.notifyUserMobileDataStateChanged(paramBoolean);
      if (paramBoolean)
      {
        reevaluateDataConnections();
        onTrySetupData("dataEnabled");
      }
      else
      {
        onCleanUpAllConnections("specificDisabled");
      }
    }
  }
  
  private void onSimNotReady()
  {
    log("onSimNotReady");
    cleanUpAllConnections(true, "simNotReady");
    mAllApnSettings = null;
    mAutoAttachOnCreationConfig = false;
    mAutoAttachOnCreation.set(false);
    mOnSubscriptionsChangedListener.mPreviousSubId.set(-1);
  }
  
  private boolean onTrySetupData(ApnContext paramApnContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onTrySetupData: apnContext=");
    localStringBuilder.append(paramApnContext);
    log(localStringBuilder.toString());
    return trySetupData(paramApnContext);
  }
  
  private boolean onTrySetupData(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("onTrySetupData: reason=");
    localStringBuilder.append(paramString);
    log(localStringBuilder.toString());
    setupDataOnConnectableApns(paramString);
    return true;
  }
  
  private void onUpdateIcc()
  {
    if (mUiccController == null) {
      return;
    }
    IccRecords localIccRecords1 = mPhone.getIccRecords();
    IccRecords localIccRecords2 = (IccRecords)mIccRecords.get();
    if (localIccRecords2 != localIccRecords1)
    {
      if (localIccRecords2 != null)
      {
        log("Removing stale icc objects.");
        localIccRecords2.unregisterForRecordsLoaded(this);
        mIccRecords.set(null);
      }
      if (localIccRecords1 != null)
      {
        if (mSubscriptionManager.isActiveSubId(mPhone.getSubId()))
        {
          log("New records found.");
          mIccRecords.set(localIccRecords1);
          localIccRecords1.registerForRecordsLoaded(this, 270338, null);
        }
      }
      else {
        onSimNotReady();
      }
    }
  }
  
  private void onVoiceCallEnded()
  {
    log("onVoiceCallEnded");
    mInVoiceCall = false;
    if (isConnected()) {
      if (mVoiceAndDataNotAllowAndInVoice)
      {
        startNetStatPoll();
        startDataStallAlarm(false);
        notifyDataConnection("2GVoiceCallEnded");
      }
      else
      {
        resetPollStats();
      }
    }
    if (mPhone.getPhoneId() != mSubscriptionManager.getDefaultDataPhoneId())
    {
      log("onVoiceCallEnded try setup data for default data sim");
      Phone localPhone = PhoneFactory.getPhone(mSubscriptionManager.getDefaultDataPhoneId());
      if (localPhone != null)
      {
        String str = localPhone.getOperatorNumeric();
        if ((str != null) && ((str.equals("46000")) || (str.equals("46002")) || (str.equals("46007")) || (str.equals("46008"))))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("onVoiceCallEnded setupDataOnConnectableApns for default data sim with CMCCC[");
          localStringBuilder.append(str);
          localStringBuilder.append("]");
          log(localStringBuilder.toString());
          mDcTracker.setupDataOnConnectableApns("2GVoiceCallEnded");
        }
      }
    }
    mVoiceAndDataNotAllowAndInVoice = false;
    setupDataOnConnectableApns("2GVoiceCallEnded");
  }
  
  private void onVoiceCallStarted()
  {
    log("onVoiceCallStarted");
    mInVoiceCall = true;
    if ((isConnected()) && (!mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()))
    {
      log("onVoiceCallStarted stop polling");
      mVoiceAndDataNotAllowAndInVoice = true;
      stopNetStatPoll();
      stopDataStallAlarm();
      notifyDataConnection("2GVoiceCallStarted");
    }
  }
  
  private String[] parseTypes(String paramString)
  {
    if ((paramString != null) && (!paramString.equals("")))
    {
      paramString = paramString.split(",");
    }
    else
    {
      paramString = new String[1];
      paramString[0] = "*";
    }
    return paramString;
  }
  
  private void putRecoveryAction(int paramInt)
  {
    Settings.System.putInt(mResolver, "radio.data.stall.recovery.action", paramInt);
  }
  
  private void reevaluateDataConnections()
  {
    if (mDataEnabledSettings.isDataEnabled())
    {
      Iterator localIterator = mApnContexts.values().iterator();
      while (localIterator.hasNext())
      {
        ApnContext localApnContext = (ApnContext)localIterator.next();
        if (localApnContext.isConnectedOrConnecting())
        {
          Object localObject = localApnContext.getDcAc();
          if (localObject != null)
          {
            localObject = ((DcAsyncChannel)localObject).getNetworkCapabilitiesSync();
            if ((localObject != null) && (!((NetworkCapabilities)localObject).hasCapability(13)) && (!((NetworkCapabilities)localObject).hasCapability(11)))
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Tearing down restricted metered net:");
              ((StringBuilder)localObject).append(localApnContext);
              log(((StringBuilder)localObject).toString());
              localApnContext.setReason("dataEnabled");
              cleanUpConnection(true, localApnContext);
            }
            else if ((localApnContext.getApnSetting().isMetered(mPhone)) && (localObject != null) && (((NetworkCapabilities)localObject).hasCapability(11)))
            {
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Tearing down unmetered net:");
              ((StringBuilder)localObject).append(localApnContext);
              log(((StringBuilder)localObject).toString());
              localApnContext.setReason("dataEnabled");
              cleanUpConnection(true, localApnContext);
            }
          }
        }
      }
    }
  }
  
  private void registerForAllEvents()
  {
    if (mTransportType == 1)
    {
      mPhone.mCi.registerForAvailable(this, 270337, null);
      mPhone.mCi.registerForOffOrNotAvailable(this, 270342, null);
      mPhone.mCi.registerForPcoData(this, 270381, null);
    }
    mPhone.getCallTracker().registerForVoiceCallEnded(this, 270344, null);
    mPhone.getCallTracker().registerForVoiceCallStarted(this, 270343, null);
    registerServiceStateTrackerEvents();
    mPhone.mCi.registerForPcoData(this, 270381, null);
    mPhone.getCarrierActionAgent().registerForCarrierAction(0, this, 270382, null, false);
    mDataServiceManager.registerForServiceBindingChanged(this, 270385, null);
  }
  
  private void registerSettingsObserver()
  {
    mSettingsObserver.unobserve();
    String str = "";
    if (TelephonyManager.getDefault().getSimCount() > 1) {
      str = Integer.toString(mPhone.getPhoneId());
    }
    SettingsObserver localSettingsObserver = mSettingsObserver;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("data_roaming");
    localStringBuilder.append(str);
    localSettingsObserver.observe(Settings.Global.getUriFor(localStringBuilder.toString()), 270384);
    mSettingsObserver.observe(Settings.Global.getUriFor("device_provisioned"), 270379);
    mSettingsObserver.observe(Settings.Global.getUriFor("device_provisioning_mobile_data"), 270379);
  }
  
  private void resetPollStats()
  {
    mTxPkts = -1L;
    mRxPkts = -1L;
    mNetStatPollPeriod = 1000;
  }
  
  private void restartDataStallAlarm()
  {
    if (!isConnected()) {
      return;
    }
    if (RecoveryAction.isAggressiveRecovery(getRecoveryAction()))
    {
      log("restartDataStallAlarm: action is pending. not resetting the alarm.");
      return;
    }
    log("restartDataStallAlarm: stop then start.");
    stopDataStallAlarm();
    startDataStallAlarm(false);
  }
  
  private void restartRadio()
  {
    log("restartRadio: ************TURN OFF RADIO**************");
    cleanUpAllConnections(true, "radioTurnedOff");
    mPhone.getServiceStateTracker().powerOffRadioSafely(this);
    SystemProperties.set("net.ppp.reset-by-timeout", String.valueOf(Integer.parseInt(SystemProperties.get("net.ppp.reset-by-timeout", "0")) + 1));
  }
  
  private boolean retryAfterDisconnected(ApnContext paramApnContext)
  {
    boolean bool1 = true;
    boolean bool2;
    if (!"radioTurnedOff".equals(paramApnContext.getReason()))
    {
      bool2 = bool1;
      if (isOnlySingleDcAllowed(mPhone.getServiceState().getRilDataRadioTechnology()))
      {
        bool2 = bool1;
        if (!isHigherPriorityApnContextActive(paramApnContext)) {}
      }
    }
    else
    {
      bool2 = false;
    }
    return bool2;
  }
  
  private void setActivity(DctConstants.Activity paramActivity)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setActivity = ");
    localStringBuilder.append(paramActivity);
    log(localStringBuilder.toString());
    mActivity = paramActivity;
    mPhone.notifyDataActivity();
  }
  
  private void setDataProfilesAsNeeded()
  {
    log("setDataProfilesAsNeeded");
    if ((mAllApnSettings != null) && (!mAllApnSettings.isEmpty()))
    {
      ArrayList localArrayList = new ArrayList();
      Iterator localIterator = mAllApnSettings.iterator();
      while (localIterator.hasNext())
      {
        Object localObject = (ApnSetting)localIterator.next();
        if (modemCognitive)
        {
          localObject = createDataProfile((ApnSetting)localObject);
          if (!localArrayList.contains(localObject)) {
            localArrayList.add(localObject);
          }
        }
      }
      if (localArrayList.size() > 0) {
        mDataServiceManager.setDataProfile(localArrayList, mPhone.getServiceState().getDataRoamingFromRegistration(), null);
      }
    }
  }
  
  private void setDataRoamingFromUserAction(boolean paramBoolean)
  {
    PreferenceManager.getDefaultSharedPreferences(mPhone.getContext()).edit().putBoolean("data_roaming_is_user_setting_key", paramBoolean).commit();
  }
  
  private void setDefaultDataRoamingEnabled()
  {
    String str = "data_roaming";
    int i = 0;
    int j = 0;
    Object localObject;
    if (TelephonyManager.getDefault().getSimCount() != 1)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("data_roaming");
      ((StringBuilder)localObject).append(mPhone.getPhoneId());
      localObject = ((StringBuilder)localObject).toString();
      try
      {
        Settings.Global.getInt(mResolver, (String)localObject);
      }
      catch (Settings.SettingNotFoundException localSettingNotFoundException)
      {
        j = 1;
      }
    }
    else
    {
      localObject = localSettingNotFoundException;
      j = i;
      if (!isDataRoamingFromUserAction())
      {
        j = 1;
        localObject = localSettingNotFoundException;
      }
    }
    if (j != 0)
    {
      j = getDefaultDataRoamingEnabled();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setDefaultDataRoamingEnabled: ");
      localStringBuilder.append((String)localObject);
      localStringBuilder.append("default value: ");
      localStringBuilder.append(j);
      log(localStringBuilder.toString());
      Settings.Global.putInt(mResolver, (String)localObject, j);
      mSubscriptionManager.setDataRoaming(j, mPhone.getSubId());
    }
  }
  
  private void setPreferredApn(int paramInt)
  {
    if (!mCanSetPreferApn)
    {
      log("setPreferredApn: X !canSEtPreferApn");
      return;
    }
    Object localObject = Long.toString(mPhone.getSubId());
    Uri localUri = Uri.withAppendedPath(PREFERAPN_NO_UPDATE_URI_USING_SUBID, (String)localObject);
    log("setPreferredApn: delete");
    localObject = mPhone.getContext().getContentResolver();
    ((ContentResolver)localObject).delete(localUri, null, null);
    if (paramInt >= 0)
    {
      log("setPreferredApn: insert");
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("apn_id", Integer.valueOf(paramInt));
      ((ContentResolver)localObject).insert(localUri, localContentValues);
    }
  }
  
  private void setRadio(boolean paramBoolean)
  {
    ITelephony localITelephony = ITelephony.Stub.asInterface(ServiceManager.checkService("phone"));
    try
    {
      localITelephony.setRadio(paramBoolean);
    }
    catch (Exception localException) {}
  }
  
  private boolean setupData(ApnContext paramApnContext, int paramInt, boolean paramBoolean)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("setupData: apnContext=");
    ((StringBuilder)localObject1).append(paramApnContext);
    log(((StringBuilder)localObject1).toString());
    paramApnContext.requestLog("setupData");
    localObject1 = null;
    ApnSetting localApnSetting1 = paramApnContext.getNextApnSetting();
    if (localApnSetting1 == null)
    {
      log("setupData: return for no apn found!");
      return false;
    }
    int i = profileId;
    int j = i;
    if (i == 0) {
      j = getApnProfileID(paramApnContext.getApnType());
    }
    Object localObject2;
    if (paramApnContext.getApnType().equals("dun"))
    {
      localObject2 = localApnSetting1;
      if (ServiceState.isGsm(mPhone.getServiceState().getRilDataRadioTechnology())) {}
    }
    Object localObject3;
    for (;;)
    {
      break;
      localObject3 = checkForCompatibleConnectedApnContext(paramApnContext);
      localObject1 = localObject3;
      localObject2 = localApnSetting1;
      if (localObject3 != null)
      {
        ApnSetting localApnSetting2 = ((DcAsyncChannel)localObject3).getApnSettingSync();
        localObject1 = localObject3;
        localObject2 = localApnSetting1;
        if (localApnSetting2 != null)
        {
          localObject2 = localApnSetting2;
          localObject1 = localObject3;
        }
      }
    }
    if (localObject1 == null)
    {
      if (isOnlySingleDcAllowed(paramInt))
      {
        if (isHigherPriorityApnContextActive(paramApnContext))
        {
          log("setupData: Higher priority ApnContext active.  Ignoring call");
          return false;
        }
        if ((!paramApnContext.getApnType().equals("ims")) && (cleanUpAllConnections(true, "SinglePdnArbitration")))
        {
          log("setupData: Some calls are disconnecting first. Wait and retry");
          return false;
        }
        log("setupData: Single pdp. Continue setting up data call.");
      }
      localObject3 = findFreeDataConnection();
      localObject1 = localObject3;
      if (localObject3 == null) {
        localObject1 = createDataConnection();
      }
      localObject3 = localObject1;
      if (localObject1 == null)
      {
        log("setupData: No free DataConnection and couldn't create one, WEIRD");
        return false;
      }
    }
    else
    {
      localObject3 = localObject1;
    }
    i = paramApnContext.incAndGetConnectionGeneration();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("setupData: dcac=");
    ((StringBuilder)localObject1).append(localObject3);
    ((StringBuilder)localObject1).append(" apnSetting=");
    ((StringBuilder)localObject1).append(localObject2);
    ((StringBuilder)localObject1).append(" gen#=");
    ((StringBuilder)localObject1).append(i);
    log(((StringBuilder)localObject1).toString());
    paramApnContext.setDataConnectionAc((DcAsyncChannel)localObject3);
    paramApnContext.setApnSetting((ApnSetting)localObject2);
    paramApnContext.setState(DctConstants.State.CONNECTING);
    mPhone.notifyDataConnection(paramApnContext.getReason(), paramApnContext.getApnType());
    localObject1 = obtainMessage();
    what = 270336;
    obj = new Pair(paramApnContext, Integer.valueOf(i));
    ((DcAsyncChannel)localObject3).bringUp(paramApnContext, j, paramInt, paramBoolean, (Message)localObject1, i);
    log("setupData: initing!");
    return true;
  }
  
  private void setupDataOnConnectableApns(String paramString, RetryFailures paramRetryFailures)
  {
    Object localObject1 = new StringBuilder(120);
    Object localObject2 = mPrioritySortedApnContexts.iterator();
    while (((Iterator)localObject2).hasNext())
    {
      ApnContext localApnContext = (ApnContext)((Iterator)localObject2).next();
      ((StringBuilder)localObject1).append(localApnContext.getApnType());
      ((StringBuilder)localObject1).append(":[state=");
      ((StringBuilder)localObject1).append(localApnContext.getState());
      ((StringBuilder)localObject1).append(",enabled=");
      ((StringBuilder)localObject1).append(localApnContext.isEnabled());
      ((StringBuilder)localObject1).append("] ");
    }
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("setupDataOnConnectableApns: ");
    ((StringBuilder)localObject2).append(paramString);
    ((StringBuilder)localObject2).append(" ");
    ((StringBuilder)localObject2).append(localObject1);
    log(((StringBuilder)localObject2).toString());
    localObject1 = mPrioritySortedApnContexts.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (ApnContext)((Iterator)localObject1).next();
      if ((((ApnContext)localObject2).getState() == DctConstants.State.FAILED) || (((ApnContext)localObject2).getState() == DctConstants.State.SCANNING)) {
        if (paramRetryFailures == RetryFailures.ALWAYS) {
          ((ApnContext)localObject2).releaseDataConnection(paramString);
        } else if ((!((ApnContext)localObject2).isConcurrentVoiceAndDataAllowed()) && (mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed())) {
          ((ApnContext)localObject2).releaseDataConnection(paramString);
        }
      }
      if (((ApnContext)localObject2).isConnectable())
      {
        log("isConnectable() call trySetupData");
        ((ApnContext)localObject2).setReason(paramString);
        trySetupData((ApnContext)localObject2);
      }
    }
  }
  
  private void startAlarmForReconnect(long paramLong, ApnContext paramApnContext)
  {
    Object localObject1 = paramApnContext.getApnType();
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("com.android.internal.telephony.data-reconnect.");
    ((StringBuilder)localObject2).append((String)localObject1);
    localObject2 = new Intent(((StringBuilder)localObject2).toString());
    ((Intent)localObject2).putExtra("reconnect_alarm_extra_reason", paramApnContext.getReason());
    ((Intent)localObject2).putExtra("reconnect_alarm_extra_type", (String)localObject1);
    ((Intent)localObject2).addFlags(268435456);
    ((Intent)localObject2).putExtra("subscription", mPhone.getSubId());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("startAlarmForReconnect: delay=");
    ((StringBuilder)localObject1).append(paramLong);
    ((StringBuilder)localObject1).append(" action=");
    ((StringBuilder)localObject1).append(((Intent)localObject2).getAction());
    ((StringBuilder)localObject1).append(" apn=");
    ((StringBuilder)localObject1).append(paramApnContext);
    log(((StringBuilder)localObject1).toString());
    localObject2 = PendingIntent.getBroadcast(mPhone.getContext(), 0, (Intent)localObject2, 134217728);
    paramApnContext.setReconnectIntent((PendingIntent)localObject2);
    mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + paramLong, (PendingIntent)localObject2);
  }
  
  private void startDataStallAlarm(boolean paramBoolean)
  {
    int i = getRecoveryAction();
    Object localObject;
    if ((mDataStallDetectionEnabled) && (getOverallState() == DctConstants.State.CONNECTED))
    {
      if ((!mIsScreenOn) && (!paramBoolean) && (!RecoveryAction.isAggressiveRecovery(i))) {
        i = Settings.Global.getInt(mResolver, "data_stall_alarm_non_aggressive_delay_in_ms", 360000);
      } else {
        i = Settings.Global.getInt(mResolver, "data_stall_alarm_aggressive_delay_in_ms", 60000);
      }
      mDataStallAlarmTag += 1;
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("startDataStallAlarm: tag=");
      ((StringBuilder)localObject).append(mDataStallAlarmTag);
      ((StringBuilder)localObject).append(" delay=");
      ((StringBuilder)localObject).append(i / 1000);
      ((StringBuilder)localObject).append("s");
      log(((StringBuilder)localObject).toString());
      localObject = new Intent("com.android.internal.telephony.data-stall");
      ((Intent)localObject).putExtra("data.stall.alram.tag", mDataStallAlarmTag);
      mDataStallAlarmIntent = PendingIntent.getBroadcast(mPhone.getContext(), 0, (Intent)localObject, 134217728);
      mAlarmManager.set(3, SystemClock.elapsedRealtime() + i, mDataStallAlarmIntent);
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("startDataStallAlarm: NOT started, no connection tag=");
      ((StringBuilder)localObject).append(mDataStallAlarmTag);
      log(((StringBuilder)localObject).toString());
    }
  }
  
  private void startNetStatPoll()
  {
    if ((getOverallState() == DctConstants.State.CONNECTED) && (!mNetStatPollEnabled))
    {
      log("startNetStatPoll");
      resetPollStats();
      mNetStatPollEnabled = true;
      mPollNetStat.run();
    }
    if (mPhone != null) {
      mPhone.notifyDataActivity();
    }
  }
  
  private void startOldDefaultDataDisconnectTimeoutAlarm()
  {
    int i = O3D_TIMEOUT;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[ABSP][startOldDefaultDataDisconnectTimeoutAlarm] delay=");
    ((StringBuilder)localObject).append(i / 1000);
    ((StringBuilder)localObject).append("s");
    log(((StringBuilder)localObject).toString());
    localObject = new Intent("asus.intent.action.data_disconnect_timeout");
    mOldDefaultDataDisconnAlarmIntent = PendingIntent.getBroadcast(mPhone.getContext(), 0, (Intent)localObject, 134217728);
    mAlarmManager.set(3, SystemClock.elapsedRealtime() + i, mOldDefaultDataDisconnAlarmIntent);
  }
  
  private void startProvisioningApnAlarm()
  {
    int i = Settings.Global.getInt(mResolver, "provisioning_apn_alarm_delay_in_ms", 900000);
    int j = i;
    if (Build.IS_DEBUGGABLE)
    {
      String str = System.getProperty("persist.debug.prov_apn_alarm", Integer.toString(i));
      try
      {
        j = Integer.parseInt(str);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("startProvisioningApnAlarm: e=");
        localStringBuilder.append(localNumberFormatException);
        loge(localStringBuilder.toString());
        j = i;
      }
    }
    mProvisioningApnAlarmTag += 1;
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("startProvisioningApnAlarm: tag=");
    ((StringBuilder)localObject).append(mProvisioningApnAlarmTag);
    ((StringBuilder)localObject).append(" delay=");
    ((StringBuilder)localObject).append(j / 1000);
    ((StringBuilder)localObject).append("s");
    log(((StringBuilder)localObject).toString());
    localObject = new Intent("com.android.internal.telephony.provisioning_apn_alarm");
    ((Intent)localObject).putExtra("provisioning.apn.alarm.tag", mProvisioningApnAlarmTag);
    mProvisioningApnAlarmIntent = PendingIntent.getBroadcast(mPhone.getContext(), 0, (Intent)localObject, 134217728);
    mAlarmManager.set(2, SystemClock.elapsedRealtime() + j, mProvisioningApnAlarmIntent);
  }
  
  private void stopDataStallAlarm()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("stopDataStallAlarm: current tag=");
    localStringBuilder.append(mDataStallAlarmTag);
    localStringBuilder.append(" mDataStallAlarmIntent=");
    localStringBuilder.append(mDataStallAlarmIntent);
    log(localStringBuilder.toString());
    mDataStallAlarmTag += 1;
    if (mDataStallAlarmIntent != null)
    {
      mAlarmManager.cancel(mDataStallAlarmIntent);
      mDataStallAlarmIntent = null;
    }
  }
  
  private void stopNetStatPoll()
  {
    mNetStatPollEnabled = false;
    removeCallbacks(mPollNetStat);
    log("stopNetStatPoll");
    if (mPhone != null) {
      mPhone.notifyDataActivity();
    }
  }
  
  private void stopOldDefaultDataDisconnectTimeoutAlarm()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ABSP][stopOldDefaultDataDisconnectTimeoutAlarm][");
    boolean bool;
    if (mOldDefaultDataDisconnAlarmIntent != null) {
      bool = true;
    } else {
      bool = false;
    }
    localStringBuilder.append(bool);
    localStringBuilder.append("]");
    log(localStringBuilder.toString());
    if (mOldDefaultDataDisconnAlarmIntent != null)
    {
      mAlarmManager.cancel(mOldDefaultDataDisconnAlarmIntent);
      mOldDefaultDataDisconnAlarmIntent = null;
    }
  }
  
  private void stopProvisioningApnAlarm()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("stopProvisioningApnAlarm: current tag=");
    localStringBuilder.append(mProvisioningApnAlarmTag);
    localStringBuilder.append(" mProvsioningApnAlarmIntent=");
    localStringBuilder.append(mProvisioningApnAlarmIntent);
    log(localStringBuilder.toString());
    mProvisioningApnAlarmTag += 1;
    if (mProvisioningApnAlarmIntent != null)
    {
      mAlarmManager.cancel(mProvisioningApnAlarmIntent);
      mProvisioningApnAlarmIntent = null;
    }
  }
  
  private boolean teardownForDun()
  {
    boolean bool1 = ServiceState.isCdma(mPhone.getServiceState().getRilDataRadioTechnology());
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (fetchDunApns().size() <= 0) {
      bool2 = false;
    }
    return bool2;
  }
  
  private boolean trySetupData(ApnContext paramApnContext)
  {
    if (mPhone.getSimulatedRadioControl() != null)
    {
      paramApnContext.setState(DctConstants.State.CONNECTED);
      mPhone.notifyDataConnection(paramApnContext.getReason(), paramApnContext.getApnType());
      log("trySetupData: X We're on the simulator; assuming connected retValue=true");
      return true;
    }
    Object localObject1 = new DataConnectionReasons();
    boolean bool = isDataAllowed(paramApnContext, (DataConnectionReasons)localObject1);
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("trySetupData for APN type ");
    ((StringBuilder)localObject2).append(paramApnContext.getApnType());
    ((StringBuilder)localObject2).append(", reason: ");
    ((StringBuilder)localObject2).append(paramApnContext.getReason());
    ((StringBuilder)localObject2).append(". ");
    ((StringBuilder)localObject2).append(((DataConnectionReasons)localObject1).toString());
    localObject2 = ((StringBuilder)localObject2).toString();
    log((String)localObject2);
    paramApnContext.requestLog((String)localObject2);
    if (bool)
    {
      if (paramApnContext.getState() == DctConstants.State.FAILED)
      {
        log("trySetupData: make a FAILED ApnContext IDLE so its reusable");
        paramApnContext.requestLog("trySetupData: make a FAILED ApnContext IDLE so its reusable");
        paramApnContext.setState(DctConstants.State.IDLE);
      }
      int i = mPhone.getServiceState().getRilDataRadioTechnology();
      paramApnContext.setConcurrentVoiceAndDataAllowed(mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed());
      if (paramApnContext.getState() == DctConstants.State.IDLE)
      {
        localObject2 = buildWaitingApns(paramApnContext.getApnType(), i);
        if (((ArrayList)localObject2).isEmpty())
        {
          notifyNoData(DcFailCause.MISSING_UNKNOWN_APN, paramApnContext);
          notifyOffApnsOfAvailability(paramApnContext.getReason());
          log("trySetupData: X No APN found retValue=false");
          paramApnContext.requestLog("trySetupData: X No APN found retValue=false");
          return false;
        }
        paramApnContext.setWaitingApns((ArrayList)localObject2);
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("trySetupData: Create from mAllApnSettings : ");
        ((StringBuilder)localObject2).append(apnListToString(mAllApnSettings));
        log(((StringBuilder)localObject2).toString());
      }
      bool = setupData(paramApnContext, i, ((DataConnectionReasons)localObject1).contains(DataConnectionReasons.DataAllowedReasonType.UNMETERED_APN));
      notifyOffApnsOfAvailability(paramApnContext.getReason());
      paramApnContext = new StringBuilder();
      paramApnContext.append("trySetupData: X retValue=");
      paramApnContext.append(bool);
      log(paramApnContext.toString());
      return bool;
    }
    if ((!paramApnContext.getApnType().equals("default")) && (paramApnContext.isConnectable())) {
      mPhone.notifyDataConnectionFailed(paramApnContext.getReason(), paramApnContext.getApnType());
    }
    notifyOffApnsOfAvailability(paramApnContext.getReason());
    localObject1 = new StringBuilder();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("trySetupData failed. apnContext = [type=");
    ((StringBuilder)localObject2).append(paramApnContext.getApnType());
    ((StringBuilder)localObject2).append(", mState=");
    ((StringBuilder)localObject2).append(paramApnContext.getState());
    ((StringBuilder)localObject2).append(", apnEnabled=");
    ((StringBuilder)localObject2).append(paramApnContext.isEnabled());
    ((StringBuilder)localObject2).append(", mDependencyMet=");
    ((StringBuilder)localObject2).append(paramApnContext.getDependencyMet());
    ((StringBuilder)localObject2).append("] ");
    ((StringBuilder)localObject1).append(((StringBuilder)localObject2).toString());
    if (!paramApnContext.isConnectable())
    {
      ((StringBuilder)localObject1).append(" isConnectable = false.");
      mPhone.notifyDataConnection(paramApnContext.getReason(), paramApnContext.getApnType());
    }
    if (!mDataEnabledSettings.isDataEnabled())
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("isDataEnabled() = false. ");
      ((StringBuilder)localObject2).append(mDataEnabledSettings);
      ((StringBuilder)localObject1).append(((StringBuilder)localObject2).toString());
    }
    if (paramApnContext.getState() == DctConstants.State.SCANNING)
    {
      paramApnContext.setState(DctConstants.State.FAILED);
      ((StringBuilder)localObject1).append(" Stop retrying.");
    }
    log(((StringBuilder)localObject1).toString());
    paramApnContext.requestLog(((StringBuilder)localObject1).toString());
    return false;
  }
  
  private void unregisterForAllEvents()
  {
    if (mTransportType == 1)
    {
      mPhone.mCi.unregisterForAvailable(this);
      mPhone.mCi.unregisterForOffOrNotAvailable(this);
      mPhone.mCi.unregisterForPcoData(this);
    }
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    if (localIccRecords != null)
    {
      localIccRecords.unregisterForRecordsLoaded(this);
      mIccRecords.set(null);
    }
    mPhone.getCallTracker().unregisterForVoiceCallEnded(this);
    mPhone.getCallTracker().unregisterForVoiceCallStarted(this);
    unregisterServiceStateTrackerEvents();
    mPhone.mCi.unregisterForPcoData(this);
    mPhone.getCarrierActionAgent().unregisterForCarrierAction(this, 0);
    mDataServiceManager.unregisterForServiceBindingChanged(this);
  }
  
  private void updateDataActivity()
  {
    TxRxSum localTxRxSum = new TxRxSum(mTxPkts, mRxPkts);
    Object localObject = new TxRxSum();
    ((TxRxSum)localObject).updateDataActivityTxRxSum();
    mTxPkts = txPkts;
    mRxPkts = rxPkts;
    if ((mNetStatPollEnabled) && ((txPkts > 0L) || (rxPkts > 0L)))
    {
      long l1 = mTxPkts - txPkts;
      long l2 = mRxPkts - rxPkts;
      if ((l1 > 0L) && (l2 > 0L)) {
        localObject = DctConstants.Activity.DATAINANDOUT;
      }
      for (;;)
      {
        break;
        if ((l1 > 0L) && (l2 == 0L)) {
          localObject = DctConstants.Activity.DATAOUT;
        } else if ((l1 == 0L) && (l2 > 0L)) {
          localObject = DctConstants.Activity.DATAIN;
        } else if (mActivity == DctConstants.Activity.DORMANT) {
          localObject = mActivity;
        } else {
          localObject = DctConstants.Activity.NONE;
        }
      }
      if ((mActivity != localObject) && (mIsScreenOn))
      {
        mActivity = ((DctConstants.Activity)localObject);
        mPhone.notifyDataActivity();
      }
    }
  }
  
  private void updateDataStallInfo()
  {
    Object localObject = new TxRxSum(mDataStallTxRxSum);
    mDataStallTxRxSum.updateDataStallTxRxSum();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("updateDataStallInfo: mDataStallTxRxSum=");
    localStringBuilder.append(mDataStallTxRxSum);
    localStringBuilder.append(" preTxRxSum=");
    localStringBuilder.append(localObject);
    log(localStringBuilder.toString());
    long l1 = mDataStallTxRxSum.txPkts - txPkts;
    long l2 = mDataStallTxRxSum.rxPkts - rxPkts;
    if ((l1 > 0L) && (l2 > 0L))
    {
      log("updateDataStallInfo: IN/OUT");
      mSentSinceLastRecv = 0L;
    }
    else if ((l1 > 0L) && (l2 == 0L))
    {
      if (isPhoneStateIdle()) {
        mSentSinceLastRecv += l1;
      } else {
        mSentSinceLastRecv = 0L;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("updateDataStallInfo: OUT sent=");
      localStringBuilder.append(l1);
      localStringBuilder.append(" mSentSinceLastRecv=");
      localStringBuilder.append(mSentSinceLastRecv);
      log(localStringBuilder.toString());
    }
    else if ((l1 == 0L) && (l2 > 0L))
    {
      log("updateDataStallInfo: IN");
      mSentSinceLastRecv = 0L;
    }
    else
    {
      log("updateDataStallInfo: NONE");
    }
    if (mSentSinceLastRecv >= 0L)
    {
      int i = Settings.Global.getInt(mResolver, "pdp_watchdog_trigger_packet_count", 15);
      if (mSentSinceLastRecv < i)
      {
        localObject = new DnsTxRxSum(mDataStallDnsTxRxSum);
        mDataStallDnsTxRxSum.updateDnsTxRxSum();
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("[ABSP][DNS] updateDnsDataStallInfo: mDataStallDnsTxRxSum=");
        localStringBuilder.append(mDataStallDnsTxRxSum);
        localStringBuilder.append(" preDnsTxRxSum=");
        localStringBuilder.append(localObject);
        log(localStringBuilder.toString());
        long l3 = mDataStallDnsTxRxSum.dnstxPkts - dnstxPkts;
        long l4 = mDataStallDnsTxRxSum.dnsrxPkts - dnsrxPkts;
        long l5 = l4;
        if (l3 > 40L)
        {
          i = 0;
          if (l3 > l4) {
            i = 100 - (int)(100L * l4 / l3);
          }
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("[ABSP][DNS] updateDnsDataStallInfo: DNS Loss rate[");
          localStringBuilder.append(i);
          localStringBuilder.append("%]");
          log(localStringBuilder.toString());
          l5 = l4;
          if (i > 95) {
            l5 = 0L;
          }
        }
        if ((l3 > 0L) && (l5 > 0L))
        {
          log("[ABSP][DNS] updateDnsDataStallInfo: IN/OUT");
          mDnsSentSinceLastRecv = 0L;
          putRecoveryAction(0);
        }
        else if ((l3 > 0L) && (l5 == 0L))
        {
          if (isPhoneStateIdle()) {
            mDnsSentSinceLastRecv += l3;
          } else {
            mDnsSentSinceLastRecv = 0L;
          }
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("[ABSP][DNS] updateDnsDataStallInfo: OUT DNS sent=");
          localStringBuilder.append(l3);
          localStringBuilder.append(" mDnsSentSinceLastRecv=");
          localStringBuilder.append(mDnsSentSinceLastRecv);
          log(localStringBuilder.toString());
        }
        else if ((l3 == 0L) && (l5 > 0L))
        {
          mDnsSentSinceLastRecv = 0L;
          putRecoveryAction(0);
        }
        else if ((l1 != 0L) || (l2 != 0L))
        {
          putRecoveryAction(0);
        }
      }
    }
  }
  
  protected void SetAttachDetach(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AAPN SetAttachDetach ON/OFF:");
    localStringBuilder.append(paramBoolean);
    log(localStringBuilder.toString());
    Object localObject;
    if (paramBoolean)
    {
      int i = RILConstants.PREFERRED_NETWORK_MODE;
      try
      {
        int j = TelephonyManager.getIntAtIndex(mResolver, "preferred_network_mode", mPhone.getPhoneId());
        i = j;
      }
      catch (Settings.SettingNotFoundException localSettingNotFoundException)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Settings Exception Reading Value At Index/PhoneId");
        ((StringBuilder)localObject).append(mPhone.getPhoneId());
        ((StringBuilder)localObject).append(" for Settings.Global.PREFERRED_NETWORK_MODE");
        loge(((StringBuilder)localObject).toString());
      }
      localObject = obtainMessage(262170, Integer.valueOf(0));
      if ((i == 22) || (i == 20)) {
        log("Current Preferred NW Mode is 4G/LTE/LTE-CA-4G+");
      }
      mPhone.setPreferredNetworkType(i, (Message)localObject);
    }
    else
    {
      localObject = obtainMessage(262169, Integer.valueOf(0));
      mPhone.setPreferredNetworkType(11, (Message)localObject);
    }
  }
  
  protected boolean allowInitialAttachForOperator()
  {
    return true;
  }
  
  public void bridgeTheOtherImsPhone(Phone paramPhone)
  {
    if ((paramPhone.getImsPhone() != null) && (paramPhone.getImsPhone().getCallTracker() != null))
    {
      log("bridgeTheOtherPhone ims phone!");
      paramPhone.getImsPhone().getCallTracker().registerForVoiceCallStarted(this, 270839, null);
      paramPhone.getImsPhone().getCallTracker().registerForVoiceCallEnded(this, 270840, null);
    }
    else
    {
      log("bridgeTheOtherPhone ims phone or imscalltracker is null!");
    }
  }
  
  public void bridgeTheOtherPhone(Phone paramPhone)
  {
    log("bridgeTheOtherPhone");
    paramPhone.getCallTracker().registerForVoiceCallStarted(this, 270837, null);
    paramPhone.getCallTracker().registerForVoiceCallEnded(this, 270838, null);
  }
  
  public void cleanUpAllConnections(String paramString)
  {
    cleanUpAllConnections(paramString, null);
  }
  
  public void cleanUpAllConnections(String paramString, Message paramMessage)
  {
    log("cleanUpAllConnections");
    if (paramMessage != null) {
      mDisconnectAllCompleteMsgList.add(paramMessage);
    }
    paramMessage = obtainMessage(270365);
    obj = paramString;
    sendMessage(paramMessage);
  }
  
  protected void createAllApnList()
  {
    mMvnoMatched = false;
    mAllApnSettings = new ArrayList();
    mIccRecords.get();
    Object localObject1 = mPhone.getOperatorNumeric();
    Object localObject2;
    if (localObject1 != null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("numeric = '");
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("'");
      localObject2 = ((StringBuilder)localObject2).toString();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("createAllApnList: selection=");
      localStringBuilder.append((String)localObject2);
      log(localStringBuilder.toString());
      localObject2 = mPhone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "filtered"), null, (String)localObject2, null, "_id");
      if (localObject2 != null)
      {
        if (((Cursor)localObject2).getCount() > 0) {
          mAllApnSettings = createApnList((Cursor)localObject2);
        }
        ((Cursor)localObject2).close();
      }
    }
    addEmergencyApnSetting();
    dedupeApnSettings();
    if (mAllApnSettings.isEmpty())
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("createAllApnList: No APN found for carrier: ");
      ((StringBuilder)localObject2).append((String)localObject1);
      log(((StringBuilder)localObject2).toString());
      mPreferredApn = null;
    }
    else
    {
      mPreferredApn = getPreferredApn();
      if ((mPreferredApn != null) && (!mPreferredApn.numeric.equals(localObject1)))
      {
        mPreferredApn = null;
        setPreferredApn(-1);
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("createAllApnList: mPreferredApn=");
      ((StringBuilder)localObject1).append(mPreferredApn);
      log(((StringBuilder)localObject1).toString());
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("createAllApnList: X mAllApnSettings=");
    ((StringBuilder)localObject1).append(mAllApnSettings);
    log(((StringBuilder)localObject1).toString());
    setDataProfilesAsNeeded();
  }
  
  public void dispose()
  {
    log("DCT.dispose");
    if (mProvisionBroadcastReceiver != null)
    {
      mPhone.getContext().unregisterReceiver(mProvisionBroadcastReceiver);
      mProvisionBroadcastReceiver = null;
    }
    if (mProvisioningSpinner != null)
    {
      mProvisioningSpinner.dismiss();
      mProvisioningSpinner = null;
    }
    cleanUpAllConnections(true, null);
    Iterator localIterator = mDataConnectionAcHashMap.values().iterator();
    while (localIterator.hasNext()) {
      ((DcAsyncChannel)localIterator.next()).disconnect();
    }
    mDataConnectionAcHashMap.clear();
    mIsDisposed = true;
    mPhone.getContext().unregisterReceiver(mIntentReceiver);
    mUiccController.unregisterForIccChanged(this);
    mSettingsObserver.unobserve();
    mSubscriptionManager.removeOnSubscriptionsChangedListener(mOnSubscriptionsChangedListener);
    mDcc.dispose();
    mDcTesterFailBringUpAll.dispose();
    mPhone.getContext().getContentResolver().unregisterContentObserver(mApnObserver);
    mApnContexts.clear();
    mApnContextsById.clear();
    mPrioritySortedApnContexts.clear();
    unregisterForAllEvents();
    destroyDataConnections();
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("DcTracker:");
    paramPrintWriter.println(" RADIO_TESTS=false");
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mDataEnabledSettings=");
    ((StringBuilder)localObject1).append(mDataEnabledSettings);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" isDataAllowed=");
    ((StringBuilder)localObject1).append(isDataAllowed(null));
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    paramPrintWriter.flush();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mRequestedApnType=");
    ((StringBuilder)localObject1).append(mRequestedApnType);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mPhone=");
    ((StringBuilder)localObject1).append(mPhone.getPhoneName());
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mActivity=");
    ((StringBuilder)localObject1).append(mActivity);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mState=");
    ((StringBuilder)localObject1).append(mState);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mTxPkts=");
    ((StringBuilder)localObject1).append(mTxPkts);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mRxPkts=");
    ((StringBuilder)localObject1).append(mRxPkts);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mNetStatPollPeriod=");
    ((StringBuilder)localObject1).append(mNetStatPollPeriod);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mNetStatPollEnabled=");
    ((StringBuilder)localObject1).append(mNetStatPollEnabled);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mDataStallTxRxSum=");
    ((StringBuilder)localObject1).append(mDataStallTxRxSum);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mDataStallAlarmTag=");
    ((StringBuilder)localObject1).append(mDataStallAlarmTag);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mDataStallDetectionEnabled=");
    ((StringBuilder)localObject1).append(mDataStallDetectionEnabled);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mSentSinceLastRecv=");
    ((StringBuilder)localObject1).append(mSentSinceLastRecv);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mNoRecvPollCount=");
    ((StringBuilder)localObject1).append(mNoRecvPollCount);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mResolver=");
    ((StringBuilder)localObject1).append(mResolver);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mReconnectIntent=");
    ((StringBuilder)localObject1).append(mReconnectIntent);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mAutoAttachOnCreation=");
    ((StringBuilder)localObject1).append(mAutoAttachOnCreation.get());
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mIsScreenOn=");
    ((StringBuilder)localObject1).append(mIsScreenOn);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mUniqueIdGenerator=");
    ((StringBuilder)localObject1).append(mUniqueIdGenerator);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    paramPrintWriter.println(" mDataRoamingLeakageLog= ");
    mDataRoamingLeakageLog.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.flush();
    paramPrintWriter.println(" ***************************************");
    localObject1 = mDcc;
    if (localObject1 != null) {
      ((DcController)localObject1).dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    } else {
      paramPrintWriter.println(" mDcc=null");
    }
    paramPrintWriter.println(" ***************************************");
    if (mDataConnections != null)
    {
      localObject2 = mDataConnections.entrySet();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(" mDataConnections: count=");
      ((StringBuilder)localObject1).append(((Set)localObject2).size());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject2 = ((Set)localObject2).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (Map.Entry)((Iterator)localObject2).next();
        paramPrintWriter.printf(" *** mDataConnection[%d] \n", new Object[] { ((Map.Entry)localObject1).getKey() });
        ((DataConnection)((Map.Entry)localObject1).getValue()).dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
    }
    else
    {
      paramPrintWriter.println("mDataConnections=null");
    }
    paramPrintWriter.println(" ***************************************");
    paramPrintWriter.flush();
    localObject1 = mApnToDataConnectionId;
    if (localObject1 != null)
    {
      localObject2 = ((HashMap)localObject1).entrySet();
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(" mApnToDataConnectonId size=");
      ((StringBuilder)localObject1).append(((Set)localObject2).size());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      localObject1 = ((Set)localObject2).iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (Map.Entry)((Iterator)localObject1).next();
        paramPrintWriter.printf(" mApnToDataConnectonId[%s]=%d\n", new Object[] { ((Map.Entry)localObject2).getKey(), ((Map.Entry)localObject2).getValue() });
      }
    }
    else
    {
      paramPrintWriter.println("mApnToDataConnectionId=null");
    }
    paramPrintWriter.println(" ***************************************");
    paramPrintWriter.flush();
    localObject1 = mApnContexts;
    if (localObject1 != null)
    {
      localObject1 = ((ConcurrentHashMap)localObject1).entrySet();
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(" mApnContexts size=");
      ((StringBuilder)localObject2).append(((Set)localObject1).size());
      paramPrintWriter.println(((StringBuilder)localObject2).toString());
      localObject1 = ((Set)localObject1).iterator();
      while (((Iterator)localObject1).hasNext()) {
        ((ApnContext)((Map.Entry)((Iterator)localObject1).next()).getValue()).dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      paramPrintWriter.println(" ***************************************");
    }
    else
    {
      paramPrintWriter.println(" mApnContexts=null");
    }
    paramPrintWriter.flush();
    Object localObject2 = mAllApnSettings;
    if (localObject2 != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(" mAllApnSettings size=");
      ((StringBuilder)localObject1).append(((ArrayList)localObject2).size());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      for (int i = 0; i < ((ArrayList)localObject2).size(); i++) {
        paramPrintWriter.printf(" mAllApnSettings[%d]: %s\n", new Object[] { Integer.valueOf(i), ((ArrayList)localObject2).get(i) });
      }
      paramPrintWriter.flush();
    }
    else
    {
      paramPrintWriter.println(" mAllApnSettings=null");
    }
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mPreferredApn=");
    ((StringBuilder)localObject1).append(mPreferredApn);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mIsPsRestricted=");
    ((StringBuilder)localObject1).append(mIsPsRestricted);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mIsDisposed=");
    ((StringBuilder)localObject1).append(mIsDisposed);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mIntentReceiver=");
    ((StringBuilder)localObject1).append(mIntentReceiver);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mReregisterOnReconnectFailure=");
    ((StringBuilder)localObject1).append(mReregisterOnReconnectFailure);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" canSetPreferApn=");
    ((StringBuilder)localObject1).append(mCanSetPreferApn);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mApnObserver=");
    ((StringBuilder)localObject1).append(mApnObserver);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" getOverallState=");
    ((StringBuilder)localObject1).append(getOverallState());
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mDataConnectionAsyncChannels=%s\n");
    ((StringBuilder)localObject1).append(mDataConnectionAcHashMap);
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(" mAttached=");
    ((StringBuilder)localObject1).append(mAttached.get());
    paramPrintWriter.println(((StringBuilder)localObject1).toString());
    mDataEnabledSettings.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.flush();
  }
  
  @VisibleForTesting
  public ArrayList<ApnSetting> fetchDunApns()
  {
    if (SystemProperties.getBoolean("net.tethering.noprovisioning", false))
    {
      log("fetchDunApns: net.tethering.noprovisioning=true ret: empty list");
      return new ArrayList(0);
    }
    int i = mPhone.getServiceState().getRilDataRadioTechnology();
    Object localObject1 = (IccRecords)mIccRecords.get();
    Object localObject2 = mPhone.getOperatorNumeric();
    Object localObject3 = new ArrayList();
    ArrayList localArrayList = new ArrayList();
    Object localObject4 = Settings.Global.getString(mResolver, "tether_dun_apn");
    if (!TextUtils.isEmpty((CharSequence)localObject4)) {
      ((ArrayList)localObject3).addAll(ApnSetting.arrayFromString((String)localObject4));
    }
    if ((((ArrayList)localObject3).isEmpty()) && (!ArrayUtils.isEmpty(mAllApnSettings)))
    {
      Iterator localIterator = mAllApnSettings.iterator();
      while (localIterator.hasNext())
      {
        localObject4 = (ApnSetting)localIterator.next();
        if (((ApnSetting)localObject4).canHandleType("dun")) {
          ((ArrayList)localObject3).add(localObject4);
        }
      }
    }
    localObject4 = ((ArrayList)localObject3).iterator();
    while (((Iterator)localObject4).hasNext())
    {
      localObject3 = (ApnSetting)((Iterator)localObject4).next();
      if (ServiceState.bitmaskHasTech(networkTypeBitmask, ServiceState.rilRadioTechnologyToNetworkType(i))) {
        if (numeric.equals(localObject2)) {
          if (((ApnSetting)localObject3).hasMvnoParams())
          {
            if ((localObject1 != null) && (ApnSetting.mvnoMatches((IccRecords)localObject1, mvnoType, mvnoMatchData))) {
              localArrayList.add(localObject3);
            }
          }
          else if (!mMvnoMatched) {
            localArrayList.add(localObject3);
          }
        }
      }
    }
    if ((mPreferredApn != null) && (mPreferredApn.canHandleType("dun")) && (mPreferredApn.canHandleType("default")))
    {
      localArrayList.add(mPreferredApn);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("AAPN fetchDunApn: PreferredApn support dun and default type,dunSetting=");
      ((StringBuilder)localObject2).append(mPreferredApn);
      log(((StringBuilder)localObject2).toString());
    }
    else if ((mPreferredApn != null) && (mPreferredApn.canHandleType("*")))
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("AAPN fetchDunApn: PreferredApn apn type is *, return null for dun");
      ((StringBuilder)localObject2).append(mPreferredApn);
      log(((StringBuilder)localObject2).toString());
    }
    else if (mAllApnSettings != null)
    {
      localObject3 = mAllApnSettings.iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localObject1 = (ApnSetting)((Iterator)localObject3).next();
        if (((ApnSetting)localObject1).canHandleType("dun"))
        {
          localArrayList.add(localObject1);
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("AAPN fetchDunApn: find dunSetting=");
          ((StringBuilder)localObject2).append(localObject1);
          log(((StringBuilder)localObject2).toString());
        }
      }
    }
    return localArrayList;
  }
  
  protected void finalize()
  {
    if (mPhone != null) {
      log("finalize");
    }
  }
  
  public String getActiveApnString(String paramString)
  {
    paramString = (ApnContext)mApnContexts.get(paramString);
    if (paramString != null)
    {
      paramString = paramString.getApnSetting();
      if (paramString != null) {
        return apn;
      }
    }
    return null;
  }
  
  public String[] getActiveApnTypes()
  {
    log("get all active apn types");
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = mApnContexts.values().iterator();
    while (localIterator.hasNext())
    {
      ApnContext localApnContext = (ApnContext)localIterator.next();
      if ((mAttached.get()) && (localApnContext.isReady())) {
        localArrayList.add(localApnContext.getApnType());
      }
    }
    return (String[])localArrayList.toArray(new String[0]);
  }
  
  public DctConstants.Activity getActivity()
  {
    return mActivity;
  }
  
  public int getApnPriority(String paramString)
  {
    ApnContext localApnContext = (ApnContext)mApnContexts.get(paramString);
    if (localApnContext == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Request for unsupported mobile name: ");
      localStringBuilder.append(paramString);
      loge(localStringBuilder.toString());
    }
    return priority;
  }
  
  protected boolean getAttachedStatus()
  {
    return mAttached.get();
  }
  
  public boolean getAutoAttachOnCreation()
  {
    return mAutoAttachOnCreation.get();
  }
  
  public boolean getDataRoamingEnabled()
  {
    int i = mPhone.getPhoneId();
    int j = TelephonyManager.getDefault().getSimCount();
    boolean bool = false;
    if (j == 1)
    {
      if (Settings.Global.getInt(mResolver, "data_roaming", getDefaultDataRoamingEnabled()) != 0) {
        bool = true;
      }
    }
    else
    {
      ContentResolver localContentResolver = mResolver;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("data_roaming");
      localStringBuilder.append(i);
      if (Settings.Global.getInt(localContentResolver, localStringBuilder.toString(), getDefaultDataRoamingEnabled()) != 0) {
        bool = true;
      } else {
        bool = false;
      }
    }
    return bool;
  }
  
  public LinkProperties getLinkProperties(String paramString)
  {
    Object localObject = (ApnContext)mApnContexts.get(paramString);
    if (localObject != null)
    {
      localObject = ((ApnContext)localObject).getDcAc();
      if (localObject != null)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("return link properites for ");
        localStringBuilder.append(paramString);
        log(localStringBuilder.toString());
        return ((DcAsyncChannel)localObject).getLinkPropertiesSync();
      }
    }
    log("return new LinkProperties");
    return new LinkProperties();
  }
  
  public NetworkCapabilities getNetworkCapabilities(String paramString)
  {
    Object localObject = (ApnContext)mApnContexts.get(paramString);
    if (localObject != null)
    {
      DcAsyncChannel localDcAsyncChannel = ((ApnContext)localObject).getDcAc();
      if (localDcAsyncChannel != null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("get active pdp is not null, return NetworkCapabilities for ");
        ((StringBuilder)localObject).append(paramString);
        log(((StringBuilder)localObject).toString());
        return localDcAsyncChannel.getNetworkCapabilitiesSync();
      }
    }
    log("return new NetworkCapabilities");
    return new NetworkCapabilities();
  }
  
  public boolean getOtherSimOnVoiceCall()
  {
    return mOtherSimOnVoiceCall;
  }
  
  public DctConstants.State getOverallState()
  {
    int i = 0;
    int j = 1;
    int k = 0;
    Iterator localIterator = mApnContexts.values().iterator();
    while (localIterator.hasNext())
    {
      ApnContext localApnContext = (ApnContext)localIterator.next();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("getOverallState: ");
      localStringBuilder.append(localApnContext.isLingering());
      log(localStringBuilder.toString());
      int m;
      int n;
      if (!localApnContext.isEnabled())
      {
        m = i;
        n = j;
        if (!localApnContext.isLingering()) {}
      }
      else
      {
        k = 1;
        switch (5.$SwitchMap$com$android$internal$telephony$DctConstants$State[localApnContext.getState().ordinal()])
        {
        default: 
          k = 1;
          m = i;
          n = j;
          break;
        case 5: 
        case 6: 
          n = 0;
          m = i;
          break;
        case 3: 
        case 4: 
          m = 1;
          n = 0;
          break;
        case 1: 
        case 2: 
          return DctConstants.State.CONNECTED;
        }
      }
      i = m;
      j = n;
    }
    if (k == 0) {
      return DctConstants.State.IDLE;
    }
    if (i != 0) {
      return DctConstants.State.CONNECTING;
    }
    if (j == 0) {
      return DctConstants.State.IDLE;
    }
    return DctConstants.State.FAILED;
  }
  
  public String[] getPcscfAddress(String paramString)
  {
    log("getPcscfAddress()");
    if (paramString == null)
    {
      log("apnType is null, return null");
      return null;
    }
    if (TextUtils.equals(paramString, "emergency"))
    {
      paramString = (ApnContext)mApnContextsById.get(9);
    }
    else
    {
      if (!TextUtils.equals(paramString, "ims")) {
        break label163;
      }
      paramString = (ApnContext)mApnContextsById.get(5);
    }
    if (paramString == null)
    {
      log("apnContext is null, return null");
      return null;
    }
    paramString = paramString.getDcAc();
    if (paramString != null)
    {
      paramString = paramString.getPcscfAddr();
      if (paramString != null) {
        for (int i = 0; i < paramString.length; i++)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Pcscf[");
          localStringBuilder.append(i);
          localStringBuilder.append("]: ");
          localStringBuilder.append(paramString[i]);
          log(localStringBuilder.toString());
        }
      }
      return paramString;
    }
    return null;
    label163:
    log("apnType is invalid, return null");
    return null;
  }
  
  public DctConstants.State getState(String paramString)
  {
    Iterator localIterator = mDataConnections.values().iterator();
    while (localIterator.hasNext())
    {
      DataConnection localDataConnection = (DataConnection)localIterator.next();
      ApnSetting localApnSetting = localDataConnection.getApnSetting();
      if ((localApnSetting != null) && (localApnSetting.canHandleType(paramString)))
      {
        if (localDataConnection.isActive()) {
          return DctConstants.State.CONNECTED;
        }
        if (localDataConnection.isActivating()) {
          return DctConstants.State.CONNECTING;
        }
        if (localDataConnection.isInactive()) {
          return DctConstants.State.IDLE;
        }
        if (localDataConnection.isDisconnecting()) {
          return DctConstants.State.DISCONNECTING;
        }
      }
    }
    return DctConstants.State.IDLE;
  }
  
  public long getSubId()
  {
    return mPhone.getSubId();
  }
  
  public void handleMessage(Message paramMessage)
  {
    int i = what;
    int j = 0;
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    int k = 0;
    boolean bool6 = false;
    boolean bool7 = true;
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
                  Object localObject1;
                  switch (i)
                  {
                  default: 
                    switch (i)
                    {
                    default: 
                      switch (i)
                      {
                      default: 
                        localObject1 = new StringBuilder();
                        ((StringBuilder)localObject1).append("Unhandled event=");
                        ((StringBuilder)localObject1).append(paramMessage);
                        Rlog.e("DcTracker", ((StringBuilder)localObject1).toString());
                        break;
                      case 270436: 
                        localObject1 = new StringBuilder();
                        ((StringBuilder)localObject1).append("EVENT_SET_TRANSMIT_POWER done, msg = ");
                        ((StringBuilder)localObject1).append(paramMessage);
                        log(((StringBuilder)localObject1).toString());
                        break;
                      case 69636: 
                        localObject1 = new StringBuilder();
                        ((StringBuilder)localObject1).append("DISCONNECTED_CONNECTED: msg=");
                        ((StringBuilder)localObject1).append(paramMessage);
                        log(((StringBuilder)localObject1).toString());
                        paramMessage = (DcAsyncChannel)obj;
                        mDataConnectionAcHashMap.remove(Integer.valueOf(paramMessage.getDataConnectionIdSync()));
                        paramMessage.disconnected();
                        break;
                      case 100: 
                        onRecordsLoaded();
                        if (mSimRecords == null) {
                          break;
                        }
                        mSimRecords.unregisterForRecordsLoaded(this);
                        mSimRecords = null;
                      }
                      break;
                    case 270838: 
                    case 270840: 
                      mOtherSimOnVoiceCall = false;
                      log("onOtherSimVoiceCallEnded");
                      if (!isConnected()) {
                        break;
                      }
                      startNetStatPoll();
                      startDataStallAlarm(false);
                      notifyDataConnection("2GVoiceCallEnded");
                      break;
                    case 270837: 
                    case 270839: 
                      mOtherSimOnVoiceCall = true;
                      log("onOtherSimVoiceCallStarted");
                      if (!isConnected()) {
                        break;
                      }
                      stopNetStatPoll();
                      stopDataStallAlarm();
                      notifyDataConnection("2GVoiceCallStarted");
                      break;
                    case 270836: 
                      if (arg1 == 1) {
                        bool6 = true;
                      }
                      paramMessage = new StringBuilder();
                      paramMessage.append("CMD_SET_ENABLE_UNDER_SLEEP_POLICY enabled=");
                      paramMessage.append(bool6);
                      log(paramMessage.toString());
                      onSetEnabledUnderSleepPolicy(bool6);
                    }
                    break;
                  case 270385: 
                    onDataServiceBindingChanged(((Boolean)obj).result).booleanValue());
                    break;
                  case 270383: 
                    onDataReconnect(paramMessage.getData());
                    break;
                  case 270382: 
                    onSetCarrierDataEnabled((AsyncResult)obj);
                    break;
                  case 270381: 
                    handlePcoData((AsyncResult)obj);
                    break;
                  case 270380: 
                    localObject1 = (String)obj;
                    paramMessage = new StringBuilder();
                    paramMessage.append("dataConnectionTracker.handleMessage: EVENT_REDIRECTION_DETECTED=");
                    paramMessage.append((String)localObject1);
                    log(paramMessage.toString());
                    onDataConnectionRedirected((String)localObject1);
                    break;
                  case 270379: 
                    onDeviceProvisionedChange();
                    break;
                  case 270378: 
                    if (mProvisioningSpinner != obj) {
                      return;
                    }
                    mProvisioningSpinner.dismiss();
                    mProvisioningSpinner = null;
                    break;
                  case 270377: 
                    if (mPhone.getServiceState().getRilDataRadioTechnology() == 0) {
                      return;
                    }
                    cleanUpConnectionsOnUpdatedApns(false, "nwTypeChanged");
                    setupDataOnConnectableApns("nwTypeChanged", RetryFailures.ONLY_ON_CHANGE);
                    onDataRatChange();
                    break;
                  case 270376: 
                    if (arg1 == 1) {
                      handleStartNetStatPoll((DctConstants.Activity)obj);
                    } else if (arg1 == 0) {
                      handleStopNetStatPoll((DctConstants.Activity)obj);
                    }
                    break;
                  case 270375: 
                    log("EVENT_PROVISIONING_APN_ALARM");
                    localObject1 = (ApnContext)mApnContextsById.get(0);
                    if ((((ApnContext)localObject1).isProvisioningApn()) && (((ApnContext)localObject1).isConnectedOrConnecting()))
                    {
                      if (mProvisioningApnAlarmTag == arg1)
                      {
                        log("EVENT_PROVISIONING_APN_ALARM: Disconnecting");
                        mIsProvisioning = false;
                        mProvisioningUrl = null;
                        stopProvisioningApnAlarm();
                        sendCleanUpConnection(true, (ApnContext)localObject1);
                      }
                      else
                      {
                        localObject1 = new StringBuilder();
                        ((StringBuilder)localObject1).append("EVENT_PROVISIONING_APN_ALARM: ignore stale tag, mProvisioningApnAlarmTag:");
                        ((StringBuilder)localObject1).append(mProvisioningApnAlarmTag);
                        ((StringBuilder)localObject1).append(" != arg1:");
                        ((StringBuilder)localObject1).append(arg1);
                        log(((StringBuilder)localObject1).toString());
                      }
                    }
                    else {
                      log("EVENT_PROVISIONING_APN_ALARM: Not connected ignore");
                    }
                    break;
                  case 270374: 
                    log("CMD_IS_PROVISIONING_APN");
                    localObject1 = null;
                    try
                    {
                      Bundle localBundle = paramMessage.getData();
                      if (localBundle != null) {
                        localObject1 = (String)localBundle.get("apnType");
                      }
                      if (TextUtils.isEmpty((CharSequence)localObject1))
                      {
                        loge("CMD_IS_PROVISIONING_APN: apnType is empty");
                        bool6 = false;
                      }
                      else
                      {
                        bool6 = isProvisioningApn((String)localObject1);
                      }
                    }
                    catch (ClassCastException localClassCastException)
                    {
                      loge("CMD_IS_PROVISIONING_APN: NO provisioning url ignoring");
                      bool6 = false;
                    }
                    localObject2 = new StringBuilder();
                    ((StringBuilder)localObject2).append("CMD_IS_PROVISIONING_APN: ret=");
                    ((StringBuilder)localObject2).append(bool6);
                    log(((StringBuilder)localObject2).toString());
                    localObject2 = mReplyAc;
                    if (bool6) {
                      j = 1;
                    }
                    ((AsyncChannel)localObject2).replyToMessage(paramMessage, 270374, j);
                    break;
                  case 270373: 
                    paramMessage = paramMessage.getData();
                    if (paramMessage != null) {
                      try
                      {
                        mProvisioningUrl = ((String)paramMessage.get("provisioningUrl"));
                      }
                      catch (ClassCastException paramMessage)
                      {
                        localObject2 = new StringBuilder();
                        ((StringBuilder)localObject2).append("CMD_ENABLE_MOBILE_PROVISIONING: provisioning url not a string");
                        ((StringBuilder)localObject2).append(paramMessage);
                        loge(((StringBuilder)localObject2).toString());
                        mProvisioningUrl = null;
                      }
                    }
                    if (TextUtils.isEmpty(mProvisioningUrl))
                    {
                      loge("CMD_ENABLE_MOBILE_PROVISIONING: provisioning url is empty, ignoring");
                      mIsProvisioning = false;
                      mProvisioningUrl = null;
                    }
                    else
                    {
                      paramMessage = new StringBuilder();
                      paramMessage.append("CMD_ENABLE_MOBILE_PROVISIONING: provisioningUrl=");
                      paramMessage.append(mProvisioningUrl);
                      loge(paramMessage.toString());
                      mIsProvisioning = true;
                      startProvisioningApnAlarm();
                    }
                    break;
                  case 270372: 
                    k = sEnableFailFastRefCounter;
                    if (arg1 == 1) {
                      j = 1;
                    } else {
                      j = -1;
                    }
                    sEnableFailFastRefCounter = k + j;
                    paramMessage = new StringBuilder();
                    paramMessage.append("CMD_SET_ENABLE_FAIL_FAST_MOBILE_DATA:  sEnableFailFastRefCounter=");
                    paramMessage.append(sEnableFailFastRefCounter);
                    log(paramMessage.toString());
                    if (sEnableFailFastRefCounter < 0)
                    {
                      paramMessage = new StringBuilder();
                      paramMessage.append("CMD_SET_ENABLE_FAIL_FAST_MOBILE_DATA: sEnableFailFastRefCounter:");
                      paramMessage.append(sEnableFailFastRefCounter);
                      paramMessage.append(" < 0");
                      loge(paramMessage.toString());
                      sEnableFailFastRefCounter = 0;
                    }
                    if (sEnableFailFastRefCounter > 0) {
                      bool6 = true;
                    } else {
                      bool6 = false;
                    }
                    paramMessage = new StringBuilder();
                    paramMessage.append("CMD_SET_ENABLE_FAIL_FAST_MOBILE_DATA: enabled=");
                    paramMessage.append(bool6);
                    paramMessage.append(" sEnableFailFastRefCounter=");
                    paramMessage.append(sEnableFailFastRefCounter);
                    log(paramMessage.toString());
                    if (mFailFast == bool6) {
                      return;
                    }
                    mFailFast = bool6;
                    if (!bool6) {
                      bool6 = bool7;
                    } else {
                      bool6 = false;
                    }
                    mDataStallDetectionEnabled = bool6;
                    if ((mDataStallDetectionEnabled) && (getOverallState() == DctConstants.State.CONNECTED) && ((!mInVoiceCall) || (mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed())))
                    {
                      log("CMD_SET_ENABLE_FAIL_FAST_MOBILE_DATA: start data stall");
                      stopDataStallAlarm();
                      startDataStallAlarm(false);
                    }
                    else
                    {
                      log("CMD_SET_ENABLE_FAIL_FAST_MOBILE_DATA: stop data stall");
                      stopDataStallAlarm();
                    }
                    break;
                  case 270371: 
                    onDataSetupCompleteError((AsyncResult)obj);
                    break;
                  case 270370: 
                    localObject2 = new StringBuilder();
                    ((StringBuilder)localObject2).append("DataConnectionTracker.handleMessage: EVENT_DISCONNECT_DC_RETRYING msg=");
                    ((StringBuilder)localObject2).append(paramMessage);
                    log(((StringBuilder)localObject2).toString());
                    onDisconnectDcRetrying((AsyncResult)obj);
                    break;
                  case 270369: 
                    onUpdateIcc();
                    break;
                  case 270368: 
                    bool6 = bool1;
                    if (arg1 == 1) {
                      bool6 = true;
                    }
                    onSetPolicyDataEnabled(bool6);
                    break;
                  case 270367: 
                    bool6 = bool2;
                    if (arg1 == 1) {
                      bool6 = true;
                    }
                    localObject2 = new StringBuilder();
                    ((StringBuilder)localObject2).append("CMD_SET_DEPENDENCY_MET met=");
                    ((StringBuilder)localObject2).append(bool6);
                    log(((StringBuilder)localObject2).toString());
                    paramMessage = paramMessage.getData();
                    if (paramMessage == null) {
                      return;
                    }
                    paramMessage = (String)paramMessage.get("apnType");
                    if (paramMessage != null) {
                      onSetDependencyMet(paramMessage, bool6);
                    }
                    break;
                  case 270366: 
                    bool6 = bool3;
                    if (arg1 == 1) {
                      bool6 = true;
                    }
                    paramMessage = new StringBuilder();
                    paramMessage.append("CMD_SET_USER_DATA_ENABLE enabled=");
                    paramMessage.append(bool6);
                    log(paramMessage.toString());
                    onSetUserDataEnabled(bool6);
                    break;
                  case 270365: 
                    if ((obj != null) && (!(obj instanceof String))) {
                      obj = null;
                    }
                    onCleanUpAllConnections((String)obj);
                  }
                  break;
                case 270363: 
                  bool6 = bool4;
                  if (arg1 == 1) {
                    bool6 = true;
                  }
                  onSetInternalDataEnabled(bool6, (Message)obj);
                  break;
                case 270362: 
                  restartRadio();
                }
                break;
              case 270360: 
                if (arg1 == 0) {
                  bool6 = bool5;
                } else {
                  bool6 = true;
                }
                localObject2 = new StringBuilder();
                ((StringBuilder)localObject2).append("EVENT_CLEAN_UP_CONNECTION tearDown=");
                ((StringBuilder)localObject2).append(bool6);
                log(((StringBuilder)localObject2).toString());
                if ((obj instanceof ApnContext)) {
                  cleanUpConnection(bool6, (ApnContext)obj);
                } else {
                  onCleanUpConnection(bool6, arg2, (String)obj);
                }
                break;
              case 270359: 
                paramMessage = new StringBuilder();
                paramMessage.append("EVENT_PS_RESTRICT_DISABLED ");
                paramMessage.append(mIsPsRestricted);
                log(paramMessage.toString());
                mIsPsRestricted = false;
                if (isConnected())
                {
                  startNetStatPoll();
                  startDataStallAlarm(false);
                  return;
                }
                if (mState == DctConstants.State.FAILED)
                {
                  cleanUpAllConnections(false, "psRestrictEnabled");
                  mReregisterOnReconnectFailure = false;
                }
                paramMessage = (ApnContext)mApnContextsById.get(0);
                if (paramMessage != null)
                {
                  paramMessage.setReason("psRestrictEnabled");
                  trySetupData(paramMessage);
                }
                else
                {
                  loge("**** Default ApnContext not found ****");
                  if (!Build.IS_DEBUGGABLE) {
                    return;
                  }
                }
                throw new RuntimeException("Default ApnContext not found");
              case 270358: 
                paramMessage = new StringBuilder();
                paramMessage.append("EVENT_PS_RESTRICT_ENABLED ");
                paramMessage.append(mIsPsRestricted);
                log(paramMessage.toString());
                stopNetStatPoll();
                stopDataStallAlarm();
                mIsPsRestricted = true;
              }
              break;
            case 270355: 
              onApnChanged();
              break;
            case 270354: 
              doRecovery();
              break;
            case 270353: 
              onDataStallAlarm(arg1);
              break;
            case 270352: 
              onDataConnectionAttached();
              break;
            case 270351: 
              localObject2 = new StringBuilder();
              ((StringBuilder)localObject2).append("DataConnectionTracker.handleMessage: EVENT_DISCONNECT_DONE msg=");
              ((StringBuilder)localObject2).append(paramMessage);
              log(((StringBuilder)localObject2).toString());
              onDisconnectDone((AsyncResult)obj);
              if ((mPhone == null) || (TelephonyManager.getDefault().getPhoneCount() <= 1)) {
                return;
              }
              j = k;
              if (1 - mPhone.getPhoneId() > 0) {
                j = 1;
              }
              paramMessage = PhoneFactory.getPhone(j);
              if ((paramMessage != null) && (mDcTracker != null) && (paramMessage.getDataConnectionState() == PhoneConstants.DataState.DISCONNECTED)) {
                mDcTracker.onRetrySetupDefaultData();
              }
              break;
            }
            break;
          case 270349: 
            onEnableApn(arg1, arg2);
            break;
          case 270348: 
            onDataRoamingOff();
            break;
          case 270347: 
            onDataRoamingOnOrSettingsChanged(what);
          }
          break;
        case 270345: 
          onDataConnectionDetached();
          break;
        case 270344: 
          onVoiceCallEnded();
          break;
        case 270343: 
          onVoiceCallStarted();
          break;
        case 270342: 
          onRadioOffOrNotAvailable();
        }
        break;
      case 270339: 
        if ((obj instanceof ApnContext)) {
          onTrySetupData((ApnContext)obj);
        } else if ((obj instanceof String)) {
          onTrySetupData((String)obj);
        } else {
          loge("EVENT_TRY_SETUP request w/o apnContext or String");
        }
        break;
      case 270338: 
        mSimRecords = mPhone.getSIMRecords();
        if (((mIccRecords.get() instanceof RuimRecords)) && (mSimRecords != null)) {
          mSimRecords.registerForRecordsLoaded(this, 100, null);
        } else {
          onRecordsLoaded();
        }
        break;
      case 270337: 
        onRadioAvailable();
        break;
      }
      onDataSetupComplete((AsyncResult)obj);
      break;
    case 262170: 
      if (obj).exception == null)
      {
        log("AAPN LTE Attach");
        mResetAttachRetry = 3;
      }
      else if (mResetAttachRetry > 0)
      {
        mResetAttachRetry -= 1;
        SetAttachDetach(true);
        log("AAPN LTE Attach exception retry");
      }
      else
      {
        log("AAPN LTE Attach fail");
      }
      break;
    }
    if (obj).exception == null)
    {
      log("AAPN LTE Detach");
      SetAttachDetach(true);
      mResetDetachRetry = 3;
    }
    else if (mResetDetachRetry > 0)
    {
      mResetDetachRetry -= 1;
      SetAttachDetach(false);
      log("AAPN LTE Detach exception retry");
    }
    else
    {
      log("AAPN LTE Detach fail");
    }
  }
  
  public boolean hasMatchedTetherApnSetting()
  {
    ArrayList localArrayList = fetchDunApns();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("hasMatchedTetherApnSetting: APNs=");
    localStringBuilder.append(localArrayList);
    log(localStringBuilder.toString());
    boolean bool;
    if (localArrayList.size() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isApnSupported(String paramString)
  {
    if (paramString == null)
    {
      loge("isApnSupported: name=null");
      return false;
    }
    if ((ApnContext)mApnContexts.get(paramString) == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Request for unsupported mobile name: ");
      localStringBuilder.append(paramString);
      loge(localStringBuilder.toString());
      return false;
    }
    return true;
  }
  
  boolean isDataAllowed(ApnContext paramApnContext, DataConnectionReasons paramDataConnectionReasons)
  {
    DataConnectionReasons localDataConnectionReasons = new DataConnectionReasons();
    boolean bool1 = mDataEnabledSettings.isInternalDataEnabled();
    boolean bool2 = getAttachedStatus();
    boolean bool3 = mPhone.getServiceStateTracker().getDesiredPowerState();
    boolean bool4 = mPhone.getServiceStateTracker().getPowerStateFromCarrier();
    int i = mPhone.getServiceState().getRilDataRadioTechnology();
    if (i == 18)
    {
      bool3 = true;
      bool4 = true;
    }
    int j;
    if ((mIccRecords.get() != null) && (((IccRecords)mIccRecords.get()).getRecordsLoaded())) {
      j = 1;
    } else {
      j = 0;
    }
    boolean bool5 = SubscriptionManager.isValidSubscriptionId(SubscriptionManager.getDefaultDataSubscriptionId());
    int k;
    if ((paramApnContext != null) && (!ApnSetting.isMeteredApnType(paramApnContext.getApnType(), mPhone))) {
      k = 0;
    } else {
      k = 1;
    }
    Object localObject = PhoneConstants.State.IDLE;
    if (mPhone.getCallTracker() != null) {
      localObject = mPhone.getCallTracker().getState();
    }
    if ((paramApnContext != null) && (paramApnContext.getApnType().equals("emergency")) && (paramApnContext.isConnectable()))
    {
      if (paramDataConnectionReasons != null) {
        paramDataConnectionReasons.add(DataConnectionReasons.DataAllowedReasonType.EMERGENCY_APN);
      }
      return true;
    }
    if ((paramApnContext != null) && (!paramApnContext.isConnectable())) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.APN_NOT_CONNECTABLE);
    }
    if ((paramApnContext != null) && ((paramApnContext.getApnType().equals("default")) || (paramApnContext.getApnType().equals("ia"))) && (i == 18)) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.ON_IWLAN);
    }
    if (isEmergency()) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.IN_ECBM);
    }
    if ((!bool2) && (!mAutoAttachOnCreation.get())) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.NOT_ATTACHED);
    }
    if (j == 0) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.RECORD_NOT_LOADED);
    }
    if ((localObject != PhoneConstants.State.IDLE) && (!mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()))
    {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.INVALID_PHONE_STATE);
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.CONCURRENT_VOICE_DATA_NOT_ALLOWED);
    }
    if (!bool1) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.INTERNAL_DATA_DISABLED);
    }
    if (!bool5) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.DEFAULT_DATA_UNSELECTED);
    }
    if (((mPhone.getPhoneType() == 1) && (mPhone.getServiceStateTracker().isRoamAliasEqualNull()) ? mPhone.getServiceState().getDataRoamingFromRegistration() : mPhone.getServiceState().getDataRoaming()) && (!getDataRoamingEnabled()))
    {
      String str = mPhone.getOperatorNumeric();
      localObject = mPhone.getServiceState().getOperatorNumeric();
      if ((mPhone.getServiceState().getDataRoaming()) && (str != null) && ((str.startsWith("22288")) || (str.startsWith("22299"))) && (localObject != null) && (((String)localObject).startsWith("222"))) {
        log("[ABSP][isDataAllowed] National roaming when use WINDTre sim, allow setup pdp when national roaming");
      } else {
        localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.ROAMING_DISABLED);
      }
    }
    if (mIsPsRestricted) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.PS_RESTRICTED);
    }
    if (!bool3) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.UNDESIRED_POWER_STATE);
    }
    if (!bool4) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.RADIO_DISABLED_BY_CARRIER);
    }
    if (!mDataEnabledSettings.isDataEnabled()) {
      localDataConnectionReasons.add(DataConnectionReasons.DataDisallowedReasonType.DATA_DISABLED);
    }
    if (localDataConnectionReasons.containsHardDisallowedReasons())
    {
      if (paramDataConnectionReasons != null) {
        paramDataConnectionReasons.copyFrom(localDataConnectionReasons);
      }
      return false;
    }
    if ((k == 0) && (!localDataConnectionReasons.allowed())) {
      localDataConnectionReasons.add(DataConnectionReasons.DataAllowedReasonType.UNMETERED_APN);
    }
    if ((paramApnContext != null) && (!paramApnContext.hasNoRestrictedRequests(true)) && (localDataConnectionReasons.contains(DataConnectionReasons.DataDisallowedReasonType.DATA_DISABLED))) {
      localDataConnectionReasons.add(DataConnectionReasons.DataAllowedReasonType.RESTRICTED_REQUEST);
    }
    if (localDataConnectionReasons.allowed()) {
      localDataConnectionReasons.add(DataConnectionReasons.DataAllowedReasonType.NORMAL);
    }
    if (paramDataConnectionReasons != null) {
      paramDataConnectionReasons.copyFrom(localDataConnectionReasons);
    }
    return localDataConnectionReasons.allowed();
  }
  
  public boolean isDataAllowed(DataConnectionReasons paramDataConnectionReasons)
  {
    return isDataAllowed(null, paramDataConnectionReasons);
  }
  
  @VisibleForTesting
  public boolean isDataEnabled()
  {
    return mDataEnabledSettings.isDataEnabled();
  }
  
  protected boolean isDefaultConnected()
  {
    ApnContext localApnContext = (ApnContext)mApnContexts.get("default");
    if (localApnContext == null) {
      return false;
    }
    return localApnContext.getState() == DctConstants.State.CONNECTED;
  }
  
  public boolean isDisconnected()
  {
    Iterator localIterator = mApnContexts.values().iterator();
    while (localIterator.hasNext()) {
      if (!((ApnContext)localIterator.next()).isDisconnected()) {
        return false;
      }
    }
    return true;
  }
  
  boolean isEmergency()
  {
    boolean bool;
    if ((!mPhone.isInEcm()) && (!mPhone.isInEmergencyCall())) {
      bool = false;
    } else {
      bool = true;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("isEmergency: result=");
    localStringBuilder.append(bool);
    log(localStringBuilder.toString());
    return bool;
  }
  
  boolean isPermanentFailure(DcFailCause paramDcFailCause)
  {
    boolean bool;
    if ((paramDcFailCause.isPermanentFailure(mPhone.getContext(), mPhone.getSubId())) && ((!mAttached.get()) || (paramDcFailCause != DcFailCause.SIGNAL_LOST))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUserDataEnabled()
  {
    if (mDataEnabledSettings.isProvisioning()) {
      return mDataEnabledSettings.isProvisioningDataEnabled();
    }
    return mDataEnabledSettings.isUserDataEnabled();
  }
  
  protected void log(String paramString)
  {
    String str = LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(mPhone.getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString);
    Rlog.d(str, localStringBuilder.toString());
  }
  
  protected void notifyOffApnsOfAvailability(String paramString)
  {
    Iterator localIterator = mApnContexts.values().iterator();
    while (localIterator.hasNext())
    {
      ApnContext localApnContext = (ApnContext)localIterator.next();
      if ((!mAttached.get()) || (!localApnContext.isReady()))
      {
        Phone localPhone = mPhone;
        String str;
        if (paramString != null) {
          str = paramString;
        } else {
          str = localApnContext.getReason();
        }
        localPhone.notifyDataConnection(str, localApnContext.getApnType(), PhoneConstants.DataState.DISCONNECTED);
      }
    }
  }
  
  void onRecordsLoaded()
  {
    int i = mPhone.getSubId();
    if (mSubscriptionManager.isActiveSubId(i))
    {
      onRecordsLoadedOrSubIdChanged();
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Ignoring EVENT_RECORDS_LOADED as subId is not valid: ");
      localStringBuilder.append(i);
      log(localStringBuilder.toString());
    }
  }
  
  protected void onRecordsLoadedOrSubIdChanged()
  {
    log("onRecordsLoadedOrSubIdChanged: createAllApnList");
    mAutoAttachOnCreationConfig = mPhone.getContext().getResources().getBoolean(17956895);
    createAllApnList();
    setInitialAttachApn();
    if (mPhone.mCi.getRadioState().isOn())
    {
      log("onRecordsLoadedOrSubIdChanged: notifying data availability");
      notifyOffApnsOfAvailability("simLoaded");
    }
    setupDataOnConnectableApns("simLoaded");
  }
  
  protected void onSetEnabledUnderSleepPolicy(boolean paramBoolean)
  {
    ApnContext localApnContext1 = (ApnContext)mApnContexts.get("default");
    ApnContext localApnContext2 = (ApnContext)mApnContexts.get("supl");
    if (localApnContext1 == null) {
      return;
    }
    if (localApnContext2 != null) {
      if (paramBoolean) {
        localApnContext2.setEnableUnderPolicy(true);
      } else {
        localApnContext2.setEnableUnderPolicy(false);
      }
    }
    if (paramBoolean)
    {
      localApnContext1.setEnableUnderPolicy(true);
      trySetupData(localApnContext1);
    }
    else
    {
      localApnContext1.setEnableUnderPolicy(false);
      cleanUpConnection(true, localApnContext1);
      stopNetStatPoll();
      stopDataStallAlarm();
    }
  }
  
  public void registerForAllDataDisconnected(Handler paramHandler, int paramInt, Object paramObject)
  {
    mAllDataDisconnectedRegistrants.addUnique(paramHandler, paramInt, paramObject);
    if (isDisconnected())
    {
      log("notify All Data Disconnected");
      notifyAllDataDisconnected();
    }
  }
  
  public void registerForDataEnabledChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mDataEnabledSettings.registerForDataEnabledChanged(paramHandler, paramInt, paramObject);
  }
  
  public void registerServiceStateTrackerEvents()
  {
    mPhone.getServiceStateTracker().registerForDataConnectionAttached(this, 270352, null);
    mPhone.getServiceStateTracker().registerForDataConnectionDetached(this, 270345, null);
    mPhone.getServiceStateTracker().registerForDataRoamingOn(this, 270347, null);
    mPhone.getServiceStateTracker().registerForDataRoamingOff(this, 270348, null, true);
    mPhone.getServiceStateTracker().registerForPsRestrictedEnabled(this, 270358, null);
    mPhone.getServiceStateTracker().registerForPsRestrictedDisabled(this, 270359, null);
    mPhone.getServiceStateTracker().registerForDataRegStateOrRatChanged(this, 270377, null);
  }
  
  public void releaseNetwork(NetworkRequest paramNetworkRequest, LocalLog paramLocalLog)
  {
    int i = ApnContext.apnIdForNetworkRequest(paramNetworkRequest);
    ApnContext localApnContext = (ApnContext)mApnContextsById.get(i);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DcTracker.releaseNetwork for ");
    localStringBuilder.append(paramNetworkRequest);
    localStringBuilder.append(" found ");
    localStringBuilder.append(localApnContext);
    paramLocalLog.log(localStringBuilder.toString());
    if (localApnContext != null) {
      localApnContext.releaseNetwork(paramNetworkRequest, paramLocalLog);
    }
  }
  
  public void requestNetwork(NetworkRequest paramNetworkRequest, LocalLog paramLocalLog)
  {
    int i = ApnContext.apnIdForNetworkRequest(paramNetworkRequest);
    ApnContext localApnContext = (ApnContext)mApnContextsById.get(i);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DcTracker.requestNetwork for ");
    localStringBuilder.append(paramNetworkRequest);
    localStringBuilder.append(" found ");
    localStringBuilder.append(localApnContext);
    paramLocalLog.log(localStringBuilder.toString());
    if (localApnContext != null) {
      localApnContext.requestNetwork(paramNetworkRequest, paramLocalLog);
    }
  }
  
  void sendCleanUpConnection(boolean paramBoolean, ApnContext paramApnContext)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("sendCleanUpConnection: tearDown=");
    ((StringBuilder)localObject).append(paramBoolean);
    ((StringBuilder)localObject).append(" apnContext=");
    ((StringBuilder)localObject).append(paramApnContext);
    log(((StringBuilder)localObject).toString());
    localObject = obtainMessage(270360);
    arg1 = paramBoolean;
    arg2 = 0;
    obj = paramApnContext;
    sendMessage((Message)localObject);
  }
  
  void sendRestartRadio()
  {
    log("sendRestartRadio:");
    sendMessage(obtainMessage(270362));
  }
  
  public void sendStartNetStatPoll(DctConstants.Activity paramActivity)
  {
    Message localMessage = obtainMessage(270376);
    arg1 = 1;
    obj = paramActivity;
    sendMessage(localMessage);
  }
  
  public void sendStopNetStatPoll(DctConstants.Activity paramActivity)
  {
    Message localMessage = obtainMessage(270376);
    arg1 = 0;
    obj = paramActivity;
    sendMessage(localMessage);
  }
  
  public void setDataRoamingEnabledByUser(boolean paramBoolean)
  {
    int i = mPhone.getSubId();
    int j = mPhone.getPhoneId();
    Object localObject;
    if (getDataRoamingEnabled() != paramBoolean)
    {
      if (TelephonyManager.getDefault().getSimCount() == 1)
      {
        Settings.Global.putInt(mResolver, "data_roaming", paramBoolean);
        setDataRoamingFromUserAction(true);
      }
      else
      {
        localObject = mResolver;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("data_roaming");
        localStringBuilder.append(j);
        Settings.Global.putInt((ContentResolver)localObject, localStringBuilder.toString(), paramBoolean);
      }
      mSubscriptionManager.setDataRoaming(paramBoolean, i);
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("setDataRoamingEnabledByUser: set phoneId=");
      ((StringBuilder)localObject).append(j);
      ((StringBuilder)localObject).append(" isRoaming=");
      ((StringBuilder)localObject).append(paramBoolean);
      log(((StringBuilder)localObject).toString());
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("setDataRoamingEnabledByUser: unchanged phoneId=");
      ((StringBuilder)localObject).append(j);
      ((StringBuilder)localObject).append(" isRoaming=");
      ((StringBuilder)localObject).append(paramBoolean);
      log(((StringBuilder)localObject).toString());
    }
  }
  
  public void setEnabled(int paramInt, boolean paramBoolean)
  {
    Message localMessage = obtainMessage(270349);
    arg1 = paramInt;
    arg2 = paramBoolean;
    sendMessage(localMessage);
  }
  
  protected void setInitialAttachApn()
  {
    Object localObject1 = null;
    Iterator localIterator = null;
    Object localObject2 = null;
    ApnSetting localApnSetting = null;
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("setInitialApn: E mPreferredApn=");
    ((StringBuilder)localObject3).append(mPreferredApn);
    log(((StringBuilder)localObject3).toString());
    Object localObject4;
    Object localObject5;
    if ((mPreferredApn != null) && (mPreferredApn.canHandleType("ia")))
    {
      localObject3 = mPreferredApn;
      localObject4 = localIterator;
      localObject5 = localApnSetting;
    }
    else
    {
      localObject3 = localObject1;
      localObject4 = localIterator;
      localObject5 = localApnSetting;
      if (mAllApnSettings != null)
      {
        localObject3 = localObject1;
        localObject4 = localIterator;
        localObject5 = localApnSetting;
        if (!mAllApnSettings.isEmpty())
        {
          localApnSetting = (ApnSetting)mAllApnSettings.get(0);
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("setInitialApn: firstApnSetting=");
          ((StringBuilder)localObject3).append(localApnSetting);
          log(((StringBuilder)localObject3).toString());
          localIterator = mAllApnSettings.iterator();
          for (;;)
          {
            localObject3 = localObject1;
            localObject4 = localObject2;
            localObject5 = localApnSetting;
            if (!localIterator.hasNext()) {
              break;
            }
            localObject3 = (ApnSetting)localIterator.next();
            if (((ApnSetting)localObject3).canHandleType("ia"))
            {
              localObject5 = new StringBuilder();
              ((StringBuilder)localObject5).append("setInitialApn: iaApnSetting=");
              ((StringBuilder)localObject5).append(localObject3);
              log(((StringBuilder)localObject5).toString());
              localObject4 = localObject2;
              localObject5 = localApnSetting;
              break;
            }
            localObject5 = localObject2;
            if (localObject2 == null)
            {
              localObject5 = localObject2;
              if (((ApnSetting)localObject3).canHandleType("default"))
              {
                localObject5 = new StringBuilder();
                ((StringBuilder)localObject5).append("setInitialApn: defaultApnSetting=");
                ((StringBuilder)localObject5).append(localObject3);
                log(((StringBuilder)localObject5).toString());
                localObject5 = localObject3;
              }
            }
            localObject2 = localObject5;
          }
        }
      }
    }
    if ((localObject3 == null) && (localObject4 == null) && (!allowInitialAttachForOperator()))
    {
      log("Abort Initial attach");
      return;
    }
    localObject2 = null;
    if (localObject3 != null)
    {
      log("setInitialAttachApn: using iaApnSetting");
    }
    else if (mPreferredApn != null)
    {
      log("setInitialAttachApn: using mPreferredApn");
      localObject3 = mPreferredApn;
    }
    else if (localObject4 != null)
    {
      log("setInitialAttachApn: using defaultApnSetting");
      localObject3 = localObject4;
    }
    else
    {
      localObject3 = localObject2;
      if (localObject5 != null)
      {
        log("setInitialAttachApn: using firstApnSetting");
        localObject3 = localObject5;
      }
    }
    if (localObject3 == null)
    {
      log("setInitialAttachApn: X There in no available apn");
    }
    else
    {
      localObject5 = new StringBuilder();
      ((StringBuilder)localObject5).append("setInitialAttachApn: X selected Apn=");
      ((StringBuilder)localObject5).append(localObject3);
      log(((StringBuilder)localObject5).toString());
      localObject5 = numeric;
      if ((!"44051".equals(localObject5)) && (!"28602".equals(localObject5)) && (!"22210".equals(localObject5))) {
        mDataServiceManager.setInitialAttachApn(createDataProfile((ApnSetting)localObject3), mPhone.getServiceState().getDataRoamingFromRegistration(), null);
      }
    }
  }
  
  public boolean setInternalDataEnabled(boolean paramBoolean)
  {
    return setInternalDataEnabled(paramBoolean, null);
  }
  
  public boolean setInternalDataEnabled(boolean paramBoolean, Message paramMessage)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setInternalDataEnabled(");
    localStringBuilder.append(paramBoolean);
    localStringBuilder.append(")");
    log(localStringBuilder.toString());
    paramMessage = obtainMessage(270363, paramMessage);
    arg1 = paramBoolean;
    sendMessage(paramMessage);
    return true;
  }
  
  public void setPolicyDataEnabled(boolean paramBoolean)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("setPolicyDataEnabled: ");
    ((StringBuilder)localObject).append(paramBoolean);
    log(((StringBuilder)localObject).toString());
    localObject = obtainMessage(270368);
    arg1 = paramBoolean;
    sendMessage((Message)localObject);
  }
  
  public void setUserDataEnabled(boolean paramBoolean)
  {
    Message localMessage = obtainMessage(270366);
    arg1 = paramBoolean;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setDataEnabled: sendMessage: enable=");
    localStringBuilder.append(paramBoolean);
    log(localStringBuilder.toString());
    sendMessage(localMessage);
  }
  
  protected void setupDataOnConnectableApns(String paramString)
  {
    setupDataOnConnectableApns(paramString, RetryFailures.ALWAYS);
  }
  
  @VisibleForTesting
  public ArrayList<ApnSetting> sortApnListByPreferred(ArrayList<ApnSetting> paramArrayList)
  {
    if ((paramArrayList != null) && (paramArrayList.size() > 1))
    {
      final int i = getPreferredApnSetId();
      if (i != 0) {
        paramArrayList.sort(new Comparator()
        {
          public int compare(ApnSetting paramAnonymousApnSetting1, ApnSetting paramAnonymousApnSetting2)
          {
            if (apnSetId == i) {
              return -1;
            }
            if (apnSetId == i) {
              return 1;
            }
            return 0;
          }
        });
      }
      return paramArrayList;
    }
    return paramArrayList;
  }
  
  public void unregisterForAllDataDisconnected(Handler paramHandler)
  {
    mAllDataDisconnectedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDataEnabledChanged(Handler paramHandler)
  {
    mDataEnabledSettings.unregisterForDataEnabledChanged(paramHandler);
  }
  
  public void unregisterServiceStateTrackerEvents()
  {
    mPhone.getServiceStateTracker().unregisterForDataConnectionAttached(this);
    mPhone.getServiceStateTracker().unregisterForDataConnectionDetached(this);
    mPhone.getServiceStateTracker().unregisterForDataRoamingOn(this);
    mPhone.getServiceStateTracker().unregisterForDataRoamingOff(this);
    mPhone.getServiceStateTracker().unregisterForPsRestrictedEnabled(this);
    mPhone.getServiceStateTracker().unregisterForPsRestrictedDisabled(this);
    mPhone.getServiceStateTracker().unregisterForDataRegStateOrRatChanged(this);
  }
  
  public void update()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("update sub = ");
    localStringBuilder.append(mPhone.getSubId());
    log(localStringBuilder.toString());
    log("update(): Active DDS, register for all events now!");
    onUpdateIcc();
    mAutoAttachOnCreation.set(false);
    ((GsmCdmaPhone)mPhone).updateCurrentCarrierInProvider();
  }
  
  public void updateRecords()
  {
    onUpdateIcc();
  }
  
  private class ApnChangeObserver
    extends ContentObserver
  {
    public ApnChangeObserver()
    {
      super();
    }
    
    public void onChange(boolean paramBoolean)
    {
      sendMessage(obtainMessage(270355));
    }
  }
  
  private class DctOnSubscriptionsChangedListener
    extends SubscriptionManager.OnSubscriptionsChangedListener
  {
    public final AtomicInteger mPreviousSubId = new AtomicInteger(-1);
    
    private DctOnSubscriptionsChangedListener() {}
    
    public void onSubscriptionsChanged()
    {
      log("SubscriptionListener.onSubscriptionInfoChanged");
      int i = mPhone.getSubId();
      if (mSubscriptionManager.isActiveSubId(i)) {
        DcTracker.this.registerSettingsObserver();
      }
      if ((mSubscriptionManager.isActiveSubId(i)) && (mPreviousSubId.getAndSet(i) != i)) {
        onRecordsLoadedOrSubIdChanged();
      }
    }
  }
  
  public class DnsTxRxSum
  {
    public long dnsrxPkts;
    public long dnstxPkts;
    
    public DnsTxRxSum()
    {
      dnsreset();
    }
    
    public DnsTxRxSum(long paramLong1, long paramLong2)
    {
      dnstxPkts = paramLong1;
      dnsrxPkts = paramLong2;
    }
    
    public DnsTxRxSum(DnsTxRxSum paramDnsTxRxSum)
    {
      dnstxPkts = dnstxPkts;
      dnsrxPkts = dnsrxPkts;
    }
    
    public void dnsreset()
    {
      dnstxPkts = -1L;
      dnsrxPkts = -1L;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{dnstxSum=");
      localStringBuilder.append(dnstxPkts);
      localStringBuilder.append(" dnsrxSum=");
      localStringBuilder.append(dnsrxPkts);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void updateDnsTxRxSum()
    {
      dnstxPkts = TrafficStats.getMobileDnsTxPackets();
      dnsrxPkts = TrafficStats.getMobileDnsRxPackets();
    }
  }
  
  private class ProvisionNotificationBroadcastReceiver
    extends BroadcastReceiver
  {
    private final String mNetworkOperator;
    private final String mProvisionUrl;
    
    public ProvisionNotificationBroadcastReceiver(String paramString1, String paramString2)
    {
      mNetworkOperator = paramString2;
      mProvisionUrl = paramString1;
    }
    
    private void enableMobileProvisioning()
    {
      Message localMessage = obtainMessage(270373);
      localMessage.setData(Bundle.forPair("provisioningUrl", mProvisionUrl));
      sendMessage(localMessage);
    }
    
    private void setEnableFailFastMobileData(int paramInt)
    {
      sendMessage(obtainMessage(270372, paramInt, 0));
    }
    
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      log("onReceive : ProvisionNotificationBroadcastReceiver");
      DcTracker.access$2702(DcTracker.this, new ProgressDialog(paramContext));
      mProvisioningSpinner.setTitle(mNetworkOperator);
      mProvisioningSpinner.setMessage(paramContext.getText(17040337));
      mProvisioningSpinner.setIndeterminate(true);
      mProvisioningSpinner.setCancelable(true);
      mProvisioningSpinner.getWindow().setType(2009);
      mProvisioningSpinner.show();
      sendMessageDelayed(obtainMessage(270378, mProvisioningSpinner), 120000L);
      DcTracker.this.setRadio(true);
      setEnableFailFastMobileData(1);
      enableMobileProvisioning();
    }
  }
  
  private static class RecoveryAction
  {
    public static final int CLEANUP = 1;
    public static final int GET_DATA_CALL_LIST = 0;
    public static final int RADIO_RESTART = 3;
    public static final int REREGISTER = 2;
    public static final int REREGISTER_TO_LTE = 4;
    
    private RecoveryAction() {}
    
    private static boolean isAggressiveRecovery(int paramInt)
    {
      boolean bool1 = true;
      boolean bool2 = bool1;
      if (paramInt != 1)
      {
        bool2 = bool1;
        if (paramInt != 2) {
          if (paramInt == 3) {
            bool2 = bool1;
          } else {
            bool2 = false;
          }
        }
      }
      return bool2;
    }
  }
  
  private static enum RetryFailures
  {
    ALWAYS,  ONLY_ON_CHANGE;
    
    private RetryFailures() {}
  }
  
  public static class TxRxSum
  {
    public long rxPkts;
    public long txPkts;
    
    public TxRxSum()
    {
      reset();
    }
    
    public TxRxSum(long paramLong1, long paramLong2)
    {
      txPkts = paramLong1;
      rxPkts = paramLong2;
    }
    
    public TxRxSum(TxRxSum paramTxRxSum)
    {
      txPkts = txPkts;
      rxPkts = rxPkts;
    }
    
    public void reset()
    {
      txPkts = -1L;
      rxPkts = -1L;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{txSum=");
      localStringBuilder.append(txPkts);
      localStringBuilder.append(" rxSum=");
      localStringBuilder.append(rxPkts);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public void updateDataActivityTxRxSum()
    {
      txPkts = TrafficStats.getMobileTxPackets();
      rxPkts = TrafficStats.getMobileRxPackets();
    }
    
    public void updateDataStallTxRxSum()
    {
      txPkts = TrafficStats.getMobileTcpTxPackets();
      rxPkts = TrafficStats.getMobileTcpRxPackets();
    }
  }
}
