package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.net.NetworkStats;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.preference.PreferenceManager;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.SettingNotFoundException;
import android.service.carrier.CarrierIdentifier;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentityCdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellLocation;
import android.telephony.ClientRequestStats;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhysicalChannelConfig;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.VoLteServiceState;
import android.text.TextUtils;
import com.android.ims.ImsCall;
import com.android.ims.ImsManager;
import com.android.internal.telephony.dataconnection.DataConnectionReasons;
import com.android.internal.telephony.dataconnection.DcTracker;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.internal.telephony.test.SimulatedRadioControl;
import com.android.internal.telephony.uicc.IccCardApplicationStatus.AppType;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IsimRecords;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UsimServiceTable;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Phone
  extends Handler
  implements PhoneInternalInterface
{
  private static final int ALREADY_IN_AUTO_SELECTION = 1;
  public static final String ASUS_NON_DDS_4G = "asus_nondds4g";
  private static final String CDMA_NON_ROAMING_LIST_OVERRIDE_PREFIX = "cdma_non_roaming_list_";
  private static final String CDMA_ROAMING_LIST_OVERRIDE_PREFIX = "cdma_roaming_list_";
  public static final String CF_ID = "cf_id_key";
  public static final String CF_STATUS = "cf_status_key";
  public static final String CF_VIDEO = "cf_key_video";
  public static final String CLIR_KEY = "clir_key";
  public static final String CS_FALLBACK = "cs_fallback";
  public static final String DATA_DISABLED_ON_BOOT_KEY = "disabled_on_boot_key";
  public static final String DATA_ROAMING_IS_USER_SETTING_KEY = "data_roaming_is_user_setting_key";
  private static final int DEFAULT_REPORT_INTERVAL_MS = 200;
  private static final String DNS_SERVER_CHECK_DISABLED_KEY = "dns_server_check_disabled_key";
  protected static final int EVENT_CALL_RING = 14;
  private static final int EVENT_CALL_RING_CONTINUE = 15;
  protected static final int EVENT_CARRIER_CONFIG_CHANGED = 43;
  protected static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 27;
  private static final int EVENT_CHECK_FOR_NETWORK_AUTOMATIC = 38;
  private static final int EVENT_CONFIG_LCE = 37;
  protected static final int EVENT_EMERGENCY_CALLBACK_MODE_ENTER = 25;
  protected static final int EVENT_EXIT_EMERGENCY_CALLBACK_RESPONSE = 26;
  protected static final int EVENT_GET_BASEBAND_VERSION_DONE = 6;
  protected static final int EVENT_GET_CALL_FORWARD_DONE = 13;
  protected static final int EVENT_GET_DEVICE_IDENTITY_DONE = 21;
  protected static final int EVENT_GET_IMEISV_DONE = 10;
  protected static final int EVENT_GET_IMEI_DONE = 9;
  protected static final int EVENT_GET_RADIO_CAPABILITY = 35;
  private static final int EVENT_GET_SIM_STATUS_DONE = 11;
  private static final int EVENT_ICC_CHANGED = 30;
  protected static final int EVENT_ICC_RECORD_EVENTS = 29;
  private static final int EVENT_INITIATE_SILENT_REDIAL = 32;
  protected static final int EVENT_LAST = 45;
  private static final int EVENT_MMI_DONE = 4;
  protected static final int EVENT_MODEM_RESET = 45;
  protected static final int EVENT_NV_READY = 23;
  protected static final int EVENT_RADIO_AVAILABLE = 1;
  private static final int EVENT_RADIO_NOT_AVAILABLE = 33;
  protected static final int EVENT_RADIO_OFF_OR_NOT_AVAILABLE = 8;
  protected static final int EVENT_RADIO_ON = 5;
  protected static final int EVENT_REGISTERED_TO_NETWORK = 19;
  protected static final int EVENT_REQUEST_VOICE_RADIO_TECH_DONE = 40;
  protected static final int EVENT_RIL_CONNECTED = 41;
  protected static final int EVENT_RUIM_RECORDS_LOADED = 22;
  protected static final int EVENT_SET_CALL_FORWARD_DONE = 12;
  protected static final int EVENT_SET_CLIR_COMPLETE = 18;
  private static final int EVENT_SET_ENHANCED_VP = 24;
  protected static final int EVENT_SET_NETWORK_AUTOMATIC = 28;
  private static final int EVENT_SET_NETWORK_AUTOMATIC_COMPLETE = 17;
  private static final int EVENT_SET_NETWORK_MANUAL_COMPLETE = 16;
  protected static final int EVENT_SET_ROAMING_PREFERENCE_DONE = 44;
  protected static final int EVENT_SET_VM_NUMBER_DONE = 20;
  protected static final int EVENT_SIM_IMSI_READY = 1045;
  protected static final int EVENT_SIM_RECORDS_LOADED = 3;
  private static final int EVENT_SRVCC_STATE_CHANGED = 31;
  protected static final int EVENT_SS = 36;
  protected static final int EVENT_SSN = 2;
  private static final int EVENT_UNSOL_OEM_HOOK_RAW = 34;
  protected static final int EVENT_UPDATE_IMS_CONFIG = 1046;
  protected static final int EVENT_UPDATE_PHONE_OBJECT = 42;
  protected static final int EVENT_USSD = 7;
  protected static final int EVENT_VOICE_RADIO_TECH_CHANGED = 39;
  public static final String EXTRA_KEY_ALERT_MESSAGE = "alertMessage";
  public static final String EXTRA_KEY_ALERT_SHOW = "alertShow";
  public static final String EXTRA_KEY_ALERT_TITLE = "alertTitle";
  public static final String EXTRA_KEY_NOTIFICATION_MESSAGE = "notificationMessage";
  private static final String GSM_NON_ROAMING_LIST_OVERRIDE_PREFIX = "gsm_non_roaming_list_";
  private static final String GSM_ROAMING_LIST_OVERRIDE_PREFIX = "gsm_roaming_list_";
  private static final boolean LCE_PULL_MODE = true;
  private static final String LOG_TAG = "Phone";
  public static final String NETWORK_SELECTION_KEY = "network_selection_key";
  public static final String NETWORK_SELECTION_NAME_KEY = "network_selection_name_key";
  public static final String NETWORK_SELECTION_SHORT_KEY = "network_selection_short_key";
  private static final String VM_COUNT = "vm_count_key";
  private static final String VM_ID = "vm_id_key";
  protected static final Object lockForRadioTechnologyChange = new Object();
  protected final int USSD_MAX_QUEUE = 10;
  private final String mActionAttached;
  private final String mActionDetached;
  private Phone mAnotherPhone = null;
  private final AppSmsManager mAppSmsManager;
  private int mCallRingContinueToken;
  private int mCallRingDelay;
  protected CarrierActionAgent mCarrierActionAgent;
  protected CarrierSignalAgent mCarrierSignalAgent;
  public CommandsInterface mCi;
  protected final Context mContext;
  public DcTracker mDcTracker;
  protected DeviceStateMonitor mDeviceStateMonitor;
  protected final RegistrantList mDisconnectRegistrants = new RegistrantList();
  private boolean mDnsCheckDisabled;
  private boolean mDoesRilSendMultipleCallRing;
  protected final RegistrantList mEmergencyCallToggledRegistrants = new RegistrantList();
  private final RegistrantList mHandoverRegistrants = new RegistrantList();
  protected final AtomicReference<IccRecords> mIccRecords = new AtomicReference();
  private BroadcastReceiver mImsIntentReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context arg1, Intent paramAnonymousIntent)
    {
      Object localObject = Phone.this;
      ??? = new StringBuilder();
      ???.append("mImsIntentReceiver: action ");
      ???.append(paramAnonymousIntent.getAction());
      ((Phone)localObject).logd("Phone", ???.toString());
      if (paramAnonymousIntent.hasExtra("android:phone_id"))
      {
        int i = paramAnonymousIntent.getIntExtra("android:phone_id", -1);
        ??? = Phone.this;
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("mImsIntentReceiver: extraPhoneId = ");
        ((StringBuilder)localObject).append(i);
        ???.logd("Phone", ((StringBuilder)localObject).toString());
        if ((i == -1) || (i != getPhoneId())) {
          return;
        }
      }
      synchronized (Phone.lockForRadioTechnologyChange)
      {
        if (paramAnonymousIntent.getAction().equals("com.android.ims.IMS_SERVICE_UP"))
        {
          Phone.access$102(Phone.this, true);
          Phone.this.updateImsPhone();
          ImsManager.getInstance(mContext, mPhoneId).updateImsServiceConfig(true);
        }
        else if (paramAnonymousIntent.getAction().equals("com.android.ims.IMS_SERVICE_DOWN"))
        {
          Phone.access$102(Phone.this, false);
          Phone.this.updateImsPhone();
        }
        return;
      }
    }
  };
  protected Phone mImsPhone = null;
  private boolean mImsServiceReady = false;
  private final RegistrantList mIncomingRingRegistrants = new RegistrantList();
  protected boolean mIsPhoneInEcmState = false;
  protected boolean mIsVideoCapable = false;
  private boolean mIsVoiceCapable = true;
  private int mLceStatus = -1;
  private Looper mLooper;
  protected final RegistrantList mMmiCompleteRegistrants = new RegistrantList();
  protected final RegistrantList mMmiRegistrants = new RegistrantList();
  private String mName;
  private final RegistrantList mNewRingingConnectionRegistrants = new RegistrantList();
  protected PhoneNotifier mNotifier;
  protected int mPhoneId;
  protected Registrant mPostDialHandler;
  private final RegistrantList mPreciseCallStateRegistrants = new RegistrantList();
  protected final AtomicReference<RadioCapability> mRadioCapability = new AtomicReference();
  protected final RegistrantList mRadioOffOrNotAvailableRegistrants = new RegistrantList();
  private final RegistrantList mServiceStateRegistrants = new RegistrantList();
  private SimActivationTracker mSimActivationTracker;
  protected final RegistrantList mSimRecordsLoadedRegistrants = new RegistrantList();
  protected SimulatedRadioControl mSimulatedRadioControl;
  public SmsStorageMonitor mSmsStorageMonitor;
  public SmsUsageMonitor mSmsUsageMonitor;
  protected final RegistrantList mSuppServiceFailedRegistrants = new RegistrantList();
  protected TelephonyComponentFactory mTelephonyComponentFactory;
  TelephonyTester mTelephonyTester;
  protected AtomicReference<UiccCardApplication> mUiccApplication = new AtomicReference();
  protected UiccController mUiccController = null;
  private boolean mUnitTestMode;
  protected final RegistrantList mUnknownConnectionRegistrants = new RegistrantList();
  private final RegistrantList mVideoCapabilityChangedRegistrants = new RegistrantList();
  protected int mVmCount = 0;
  
  protected Phone(String paramString, PhoneNotifier paramPhoneNotifier, Context paramContext, CommandsInterface paramCommandsInterface, boolean paramBoolean)
  {
    this(paramString, paramPhoneNotifier, paramContext, paramCommandsInterface, paramBoolean, Integer.MAX_VALUE, TelephonyComponentFactory.getInstance());
  }
  
  protected Phone(String paramString, PhoneNotifier paramPhoneNotifier, Context paramContext, CommandsInterface paramCommandsInterface, boolean paramBoolean, int paramInt, TelephonyComponentFactory paramTelephonyComponentFactory)
  {
    mPhoneId = paramInt;
    mName = paramString;
    mNotifier = paramPhoneNotifier;
    mContext = paramContext;
    mLooper = Looper.myLooper();
    mCi = paramCommandsInterface;
    paramString = new StringBuilder();
    paramString.append(getClass().getPackage().getName());
    paramString.append(".action_detached");
    mActionDetached = paramString.toString();
    paramString = new StringBuilder();
    paramString.append(getClass().getPackage().getName());
    paramString.append(".action_attached");
    mActionAttached = paramString.toString();
    mAppSmsManager = paramTelephonyComponentFactory.makeAppSmsManager(paramContext);
    if (Build.IS_DEBUGGABLE) {
      mTelephonyTester = new TelephonyTester(this);
    }
    setUnitTestMode(paramBoolean);
    mDnsCheckDisabled = PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("dns_server_check_disabled_key", false);
    mCi.setOnCallRing(this, 14, null);
    mIsVoiceCapable = mContext.getResources().getBoolean(17957076);
    mDoesRilSendMultipleCallRing = SystemProperties.getBoolean("ro.telephony.call_ring.multiple", true);
    paramString = new StringBuilder();
    paramString.append("mDoesRilSendMultipleCallRing=");
    paramString.append(mDoesRilSendMultipleCallRing);
    Rlog.d("Phone", paramString.toString());
    mCallRingDelay = SystemProperties.getInt("ro.telephony.call_ring.delay", 3000);
    paramString = new StringBuilder();
    paramString.append("mCallRingDelay=");
    paramString.append(mCallRingDelay);
    Rlog.d("Phone", paramString.toString());
    if (getPhoneType() == 5) {
      return;
    }
    paramString = getLocaleFromCarrierProperties(mContext);
    if ((paramString != null) && (!TextUtils.isEmpty(paramString.getCountry())))
    {
      paramPhoneNotifier = paramString.getCountry();
      try
      {
        Settings.Global.getInt(mContext.getContentResolver(), "wifi_country_code");
      }
      catch (Settings.SettingNotFoundException paramString)
      {
        ((WifiManager)mContext.getSystemService("wifi")).setCountryCode(paramPhoneNotifier);
      }
    }
    mTelephonyComponentFactory = paramTelephonyComponentFactory;
    mSmsStorageMonitor = mTelephonyComponentFactory.makeSmsStorageMonitor(this);
    mSmsUsageMonitor = mTelephonyComponentFactory.makeSmsUsageMonitor(paramContext);
    mUiccController = UiccController.getInstance();
    mUiccController.registerForIccChanged(this, 30, null);
    mSimActivationTracker = mTelephonyComponentFactory.makeSimActivationTracker(this);
    if (getPhoneType() != 3) {
      mCi.registerForSrvccStateChanged(this, 31, null);
    }
    mCi.setOnUnsolOemHookRaw(this, 34, null);
    mCi.startLceService(200, true, obtainMessage(37));
  }
  
  private void checkCorrectThread(Handler paramHandler)
  {
    if (paramHandler.getLooper() == mLooper) {
      return;
    }
    throw new RuntimeException("com.android.internal.telephony.Phone must be used from within one thread");
  }
  
  public static void checkWfcWifiOnlyModeBeforeDial(Phone paramPhone, int paramInt, Context paramContext)
    throws CallStateException
  {
    if ((paramPhone == null) || (!paramPhone.isWifiCallingEnabled()))
    {
      paramPhone = ImsManager.getInstance(paramContext, paramInt);
      if ((paramPhone.isWfcEnabledByPlatform()) && (paramPhone.isWfcEnabledByUser()) && (paramPhone.getWfcMode() == 0)) {
        paramInt = 1;
      } else {
        paramInt = 0;
      }
      if (paramInt != 0) {}
    }
    else
    {
      return;
    }
    throw new CallStateException(1, "WFC Wi-Fi Only Mode: IMS not registered");
  }
  
  private void clearSavedNetworkSelection()
  {
    Object localObject1 = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("network_selection_key");
    ((StringBuilder)localObject2).append(getSubId());
    localObject2 = ((SharedPreferences.Editor)localObject1).remove(((StringBuilder)localObject2).toString());
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("network_selection_name_key");
    ((StringBuilder)localObject1).append(getSubId());
    localObject1 = ((SharedPreferences.Editor)localObject2).remove(((StringBuilder)localObject1).toString());
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("network_selection_short_key");
    ((StringBuilder)localObject2).append(getSubId());
    ((SharedPreferences.Editor)localObject1).remove(((StringBuilder)localObject2).toString()).commit();
  }
  
  private int getCallForwardingIndicatorFromSharedPref()
  {
    int i = 0;
    int j = getSubId();
    Object localObject1;
    if (SubscriptionController.getInstance().isActiveSubId(j))
    {
      localObject1 = PreferenceManager.getDefaultSharedPreferences(mContext);
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("cf_status_key");
      ((StringBuilder)localObject2).append(j);
      int k = ((SharedPreferences)localObject1).getInt(((StringBuilder)localObject2).toString(), -1);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("getCallForwardingIndicatorFromSharedPref: for subId ");
      ((StringBuilder)localObject2).append(j);
      ((StringBuilder)localObject2).append("= ");
      ((StringBuilder)localObject2).append(k);
      logd("Phone", ((StringBuilder)localObject2).toString());
      i = k;
      if (k == -1)
      {
        localObject2 = ((SharedPreferences)localObject1).getString("cf_id_key", null);
        i = k;
        if (localObject2 != null)
        {
          if (((String)localObject2).equals(getSubscriberId()))
          {
            i = ((SharedPreferences)localObject1).getInt("cf_status_key", 0);
            boolean bool = true;
            if (i != 1) {
              bool = false;
            }
            setCallForwardingIndicatorInSharedPref(bool);
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("getCallForwardingIndicatorFromSharedPref: ");
            ((StringBuilder)localObject2).append(i);
            logd("Phone", ((StringBuilder)localObject2).toString());
          }
          else
          {
            logd("Phone", "getCallForwardingIndicatorFromSharedPref: returning DISABLED as status for matching subscriberId not found");
            i = k;
          }
          localObject1 = ((SharedPreferences)localObject1).edit();
          ((SharedPreferences.Editor)localObject1).remove("cf_id_key");
          ((SharedPreferences.Editor)localObject1).remove("cf_status_key");
          ((SharedPreferences.Editor)localObject1).apply();
        }
      }
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("getCallForwardingIndicatorFromSharedPref: invalid subId ");
      ((StringBuilder)localObject1).append(j);
      loge("Phone", ((StringBuilder)localObject1).toString());
    }
    return i;
  }
  
  protected static boolean getInEcmMode()
  {
    return SystemProperties.getBoolean("ril.cdma.inecmmode", false);
  }
  
  private static Locale getLocaleFromCarrierProperties(Context paramContext)
  {
    String str = SystemProperties.get("ro.carrier");
    if ((str != null) && (str.length() != 0) && (!"unknown".equals(str)))
    {
      paramContext = paramContext.getResources().getTextArray(17235974);
      for (int i = 0; i < paramContext.length; i += 3) {
        if (str.equals(paramContext[i].toString())) {
          return Locale.forLanguageTag(paramContext[(i + 1)].toString().replace('_', '-'));
        }
      }
      return null;
    }
    return null;
  }
  
  private boolean getRoamingOverrideHelper(String paramString1, String paramString2)
  {
    String str = getIccSerialNumber();
    if ((!TextUtils.isEmpty(str)) && (!TextUtils.isEmpty(paramString2)))
    {
      SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append(str);
      paramString1 = localSharedPreferences.getStringSet(localStringBuilder.toString(), null);
      if (paramString1 == null) {
        return false;
      }
      return paramString1.contains(paramString2);
    }
    return false;
  }
  
  private OperatorInfo getSavedNetworkSelection()
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("network_selection_key");
    ((StringBuilder)localObject1).append(getSubId());
    localObject1 = localSharedPreferences.getString(((StringBuilder)localObject1).toString(), "");
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("network_selection_name_key");
    ((StringBuilder)localObject2).append(getSubId());
    localObject2 = localSharedPreferences.getString(((StringBuilder)localObject2).toString(), "");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("network_selection_short_key");
    localStringBuilder.append(getSubId());
    return new OperatorInfo((String)localObject2, localSharedPreferences.getString(localStringBuilder.toString(), ""), (String)localObject1);
  }
  
  private static int getVideoState(Call paramCall)
  {
    int i = 0;
    paramCall = paramCall.getEarliestConnection();
    if (paramCall != null) {
      i = paramCall.getVideoState();
    }
    return i;
  }
  
  private void handleSetSelectNetwork(AsyncResult paramAsyncResult)
  {
    if (!(userObj instanceof NetworkSelectMessage))
    {
      loge("Phone", "unexpected result from user object.");
      return;
    }
    NetworkSelectMessage localNetworkSelectMessage = (NetworkSelectMessage)userObj;
    if (message != null)
    {
      AsyncResult.forMessage(message, result, exception);
      message.sendToTarget();
    }
  }
  
  private void handleSrvccStateChanged(int[] paramArrayOfInt)
  {
    logd("Phone", "handleSrvccStateChanged");
    ArrayList localArrayList = null;
    Phone localPhone = mImsPhone;
    Call.SrvccState localSrvccState = Call.SrvccState.NONE;
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length != 0))
    {
      int i = paramArrayOfInt[0];
      switch (i)
      {
      default: 
        return;
      case 2: 
      case 3: 
        paramArrayOfInt = Call.SrvccState.FAILED;
        break;
      case 1: 
        paramArrayOfInt = Call.SrvccState.COMPLETED;
        if (localPhone != null) {
          localPhone.notifySrvccState(paramArrayOfInt);
        } else {
          logd("Phone", "HANDOVER_COMPLETED: mImsPhone null");
        }
        break;
      case 0: 
        paramArrayOfInt = Call.SrvccState.STARTED;
        if (localPhone != null)
        {
          localArrayList = localPhone.getHandoverConnection();
          migrateFrom(localPhone);
        }
        else
        {
          logd("Phone", "HANDOVER_STARTED: mImsPhone null");
        }
        break;
      }
      getCallTracker().notifySrvccState(paramArrayOfInt, localArrayList);
      notifyVoLteServiceStateChanged(new VoLteServiceState(i));
    }
  }
  
  private boolean isVideoCallOrConference(Call paramCall)
  {
    boolean bool1 = paramCall.isMultiparty();
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if ((paramCall instanceof ImsPhoneCall))
    {
      paramCall = ((ImsPhoneCall)paramCall).getImsCall();
      if ((paramCall == null) || ((paramCall.isVideoCall()) || (!paramCall.wasVideoCall()))) {
        bool2 = false;
      }
      return bool2;
    }
    return false;
  }
  
  private void logd(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString2);
    Rlog.d(paramString1, localStringBuilder.toString());
  }
  
  private void loge(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString2);
    Rlog.e(paramString1, localStringBuilder.toString());
  }
  
  private void logi(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[");
    localStringBuilder.append(getPhoneId());
    localStringBuilder.append("]");
    localStringBuilder.append(paramString2);
    Rlog.i(paramString1, localStringBuilder.toString());
  }
  
  private void notifyIncomingRing()
  {
    if (!mIsVoiceCapable) {
      return;
    }
    AsyncResult localAsyncResult = new AsyncResult(null, this, null);
    mIncomingRingRegistrants.notifyRegistrants(localAsyncResult);
  }
  
  private void notifyMessageWaitingIndicator()
  {
    if (!mIsVoiceCapable) {
      return;
    }
    mNotifier.notifyMessageWaitingChanged(this);
  }
  
  private void onCheckForNetworkSelectionModeAutomatic(Message paramMessage)
  {
    paramMessage = (AsyncResult)obj;
    Message localMessage = (Message)userObj;
    int i = 1;
    int j = 1;
    int k = i;
    if (exception == null)
    {
      k = i;
      if (result != null) {
        try
        {
          int m = ((int[])result)[0];
          k = j;
          if (m == 0) {
            k = 0;
          }
        }
        catch (Exception localException)
        {
          k = i;
        }
      }
    }
    NetworkSelectMessage localNetworkSelectMessage = new NetworkSelectMessage(null);
    message = localMessage;
    operatorNumeric = "";
    operatorAlphaLong = "";
    operatorAlphaShort = "";
    if (k != 0)
    {
      paramMessage = obtainMessage(17, localNetworkSelectMessage);
      mCi.setNetworkSelectionModeAutomatic(paramMessage);
    }
    else
    {
      logd("Phone", "setNetworkSelectionModeAutomatic - already auto, ignoring");
      if (message != null) {
        message.arg1 = 1;
      }
      userObj = localNetworkSelectMessage;
      handleSetSelectNetwork(paramMessage);
    }
    updateSavedNetworkOperator(localNetworkSelectMessage);
  }
  
  private List<CellInfo> privatizeCellInfoList(List<CellInfo> paramList)
  {
    if (paramList == null) {
      return null;
    }
    Object localObject1 = paramList;
    if (Settings.Secure.getInt(getContext().getContentResolver(), "location_mode", 0) == 0)
    {
      localObject1 = new ArrayList(paramList.size());
      paramList = paramList.iterator();
      while (paramList.hasNext())
      {
        Object localObject2 = (CellInfo)paramList.next();
        if ((localObject2 instanceof CellInfoCdma))
        {
          CellInfoCdma localCellInfoCdma = (CellInfoCdma)localObject2;
          localObject2 = localCellInfoCdma.getCellIdentity();
          localObject2 = new CellIdentityCdma(((CellIdentityCdma)localObject2).getNetworkId(), ((CellIdentityCdma)localObject2).getSystemId(), ((CellIdentityCdma)localObject2).getBasestationId(), Integer.MAX_VALUE, Integer.MAX_VALUE);
          localCellInfoCdma = new CellInfoCdma(localCellInfoCdma);
          localCellInfoCdma.setCellIdentity((CellIdentityCdma)localObject2);
          ((ArrayList)localObject1).add(localCellInfoCdma);
        }
        else
        {
          ((ArrayList)localObject1).add(localObject2);
        }
      }
    }
    return localObject1;
  }
  
  private void restoreSavedNetworkSelection(Message paramMessage)
  {
    OperatorInfo localOperatorInfo = getSavedNetworkSelection();
    if ((localOperatorInfo != null) && (!TextUtils.isEmpty(localOperatorInfo.getOperatorNumeric()))) {
      selectNetworkManually(localOperatorInfo, true, paramMessage);
    } else {
      setNetworkSelectionModeAutomatic(paramMessage);
    }
  }
  
  private void sendIncomingCallRingNotification(int paramInt)
  {
    if ((mIsVoiceCapable) && (!mDoesRilSendMultipleCallRing) && (paramInt == mCallRingContinueToken))
    {
      logd("Phone", "Sending notifyIncomingRing");
      notifyIncomingRing();
      sendMessageDelayed(obtainMessage(15, paramInt, 0), mCallRingDelay);
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Ignoring ring notification request, mDoesRilSendMultipleCallRing=");
      localStringBuilder.append(mDoesRilSendMultipleCallRing);
      localStringBuilder.append(" token=");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" mCallRingContinueToken=");
      localStringBuilder.append(mCallRingContinueToken);
      localStringBuilder.append(" mIsVoiceCapable=");
      localStringBuilder.append(mIsVoiceCapable);
      logd("Phone", localStringBuilder.toString());
    }
  }
  
  private void setCallForwardingIndicatorInSharedPref(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 1;
    } else {
      i = 0;
    }
    int j = getSubId();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setCallForwardingIndicatorInSharedPref: Storing status = ");
    localStringBuilder.append(i);
    localStringBuilder.append(" in pref ");
    localStringBuilder.append("cf_status_key");
    localStringBuilder.append(j);
    logd("Phone", localStringBuilder.toString());
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("cf_status_key");
    localStringBuilder.append(j);
    localEditor.putInt(localStringBuilder.toString(), i);
    localEditor.apply();
  }
  
  private void setRoamingOverrideHelper(List<String> paramList, String paramString1, String paramString2)
  {
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(paramString2);
    paramString1 = localStringBuilder.toString();
    if ((paramList != null) && (!paramList.isEmpty())) {
      localEditor.putStringSet(paramString1, new HashSet(paramList)).commit();
    } else {
      localEditor.remove(paramString1).commit();
    }
  }
  
  private void setUnitTestMode(boolean paramBoolean)
  {
    mUnitTestMode = paramBoolean;
  }
  
  private void updateImsPhone()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("updateImsPhone mImsServiceReady=");
    localStringBuilder.append(mImsServiceReady);
    logd("Phone", localStringBuilder.toString());
    if ((mImsServiceReady) && (mImsPhone == null))
    {
      mImsPhone = PhoneFactory.makeImsPhone(mNotifier, this);
      CallManager.getInstance().registerPhone(mImsPhone);
      mImsPhone.registerForSilentRedial(this, 32, null);
      if ((mAnotherPhone != null) && (mAnotherPhone.mDcTracker != null)) {
        mAnotherPhone.mDcTracker.bridgeTheOtherImsPhone(this);
      }
    }
    else if ((!mImsServiceReady) && (mImsPhone != null))
    {
      CallManager.getInstance().unregisterPhone(mImsPhone);
      mImsPhone.unregisterForSilentRedial(this);
      mImsPhone.dispose();
      mImsPhone = null;
    }
  }
  
  private void updateSavedNetworkOperator(NetworkSelectMessage paramNetworkSelectMessage)
  {
    int i = getSubId();
    if (SubscriptionController.getInstance().isActiveSubId(i))
    {
      SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("network_selection_key");
      localStringBuilder.append(i);
      localEditor.putString(localStringBuilder.toString(), operatorNumeric);
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("network_selection_name_key");
      localStringBuilder.append(i);
      localEditor.putString(localStringBuilder.toString(), operatorAlphaLong);
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("network_selection_short_key");
      localStringBuilder.append(i);
      localEditor.putString(localStringBuilder.toString(), operatorAlphaShort);
      if (!localEditor.commit()) {
        loge("Phone", "failed to commit network selection preference");
      }
    }
    else
    {
      paramNetworkSelectMessage = new StringBuilder();
      paramNetworkSelectMessage.append("Cannot update network selection preference due to invalid subId ");
      paramNetworkSelectMessage.append(i);
      loge("Phone", paramNetworkSelectMessage.toString());
    }
  }
  
  public void addParticipant(String paramString)
    throws CallStateException
  {
    paramString = new StringBuilder();
    paramString.append("addParticipant :: No-Op base implementation. ");
    paramString.append(this);
    throw new CallStateException(paramString.toString());
  }
  
  public void bridgeTheOtherPhone(Phone paramPhone)
  {
    mAnotherPhone = paramPhone;
    mDcTracker.bridgeTheOtherPhone(paramPhone);
  }
  
  public void callEndCleanupHandOverCallIfAny() {}
  
  public void cancelUSSD() {}
  
  public void carrierActionReportDefaultNetworkStatus(boolean paramBoolean)
  {
    mCarrierActionAgent.carrierActionReportDefaultNetworkStatus(paramBoolean);
  }
  
  public void carrierActionSetMeteredApnsEnabled(boolean paramBoolean)
  {
    mCarrierActionAgent.carrierActionSetMeteredApnsEnabled(paramBoolean);
  }
  
  public void carrierActionSetRadioEnabled(boolean paramBoolean)
  {
    mCarrierActionAgent.carrierActionSetRadioEnabled(paramBoolean);
  }
  
  protected Connection dialInternal(String paramString, PhoneInternalInterface.DialArgs paramDialArgs)
    throws CallStateException
  {
    return null;
  }
  
  public void disableDnsCheck(boolean paramBoolean)
  {
    mDnsCheckDisabled = paramBoolean;
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
    localEditor.putBoolean("dns_server_check_disabled_key", paramBoolean);
    localEditor.apply();
  }
  
  public void dispose() {}
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Phone: subId=");
    localStringBuilder.append(getSubId());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mPhoneId=");
    localStringBuilder.append(mPhoneId);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mCi=");
    localStringBuilder.append(mCi);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mDnsCheckDisabled=");
    localStringBuilder.append(mDnsCheckDisabled);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mDcTracker=");
    localStringBuilder.append(mDcTracker);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mDoesRilSendMultipleCallRing=");
    localStringBuilder.append(mDoesRilSendMultipleCallRing);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mCallRingContinueToken=");
    localStringBuilder.append(mCallRingContinueToken);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mCallRingDelay=");
    localStringBuilder.append(mCallRingDelay);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mIsVoiceCapable=");
    localStringBuilder.append(mIsVoiceCapable);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mIccRecords=");
    localStringBuilder.append(mIccRecords.get());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mUiccApplication=");
    localStringBuilder.append(mUiccApplication.get());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mSmsStorageMonitor=");
    localStringBuilder.append(mSmsStorageMonitor);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mSmsUsageMonitor=");
    localStringBuilder.append(mSmsUsageMonitor);
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.flush();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mLooper=");
    localStringBuilder.append(mLooper);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mContext=");
    localStringBuilder.append(mContext);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mNotifier=");
    localStringBuilder.append(mNotifier);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mSimulatedRadioControl=");
    localStringBuilder.append(mSimulatedRadioControl);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" mUnitTestMode=");
    localStringBuilder.append(mUnitTestMode);
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" isDnsCheckDisabled()=");
    localStringBuilder.append(isDnsCheckDisabled());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getUnitTestMode()=");
    localStringBuilder.append(getUnitTestMode());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getState()=");
    localStringBuilder.append(getState());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getIccSerialNumber()=");
    localStringBuilder.append(getIccSerialNumber());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getIccRecordsLoaded()=");
    localStringBuilder.append(getIccRecordsLoaded());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getMessageWaitingIndicator()=");
    localStringBuilder.append(getMessageWaitingIndicator());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getCallForwardingIndicator()=");
    localStringBuilder.append(getCallForwardingIndicator());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" isInEmergencyCall()=");
    localStringBuilder.append(isInEmergencyCall());
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.flush();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" isInEcm()=");
    localStringBuilder.append(isInEcm());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getPhoneName()=");
    localStringBuilder.append(getPhoneName());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getPhoneType()=");
    localStringBuilder.append(getPhoneType());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getVoiceMessageCount()=");
    localStringBuilder.append(getVoiceMessageCount());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" getActiveApnTypes()=");
    localStringBuilder.append(getActiveApnTypes());
    paramPrintWriter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(" needsOtaServiceProvisioning=");
    localStringBuilder.append(needsOtaServiceProvisioning());
    paramPrintWriter.println(localStringBuilder.toString());
    paramPrintWriter.flush();
    paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    if (mImsPhone != null)
    {
      try
      {
        mImsPhone.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception localException1)
      {
        localException1.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    if (mDcTracker != null)
    {
      try
      {
        mDcTracker.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception localException2)
      {
        localException2.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    if (getServiceStateTracker() != null)
    {
      try
      {
        getServiceStateTracker().dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception localException3)
      {
        localException3.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    if (mCarrierActionAgent != null)
    {
      try
      {
        mCarrierActionAgent.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception localException4)
      {
        localException4.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    if (mCarrierSignalAgent != null)
    {
      try
      {
        mCarrierSignalAgent.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception localException5)
      {
        localException5.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    if (getCallTracker() != null)
    {
      try
      {
        getCallTracker().dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception localException6)
      {
        localException6.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    if (mSimActivationTracker != null)
    {
      try
      {
        mSimActivationTracker.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception localException7)
      {
        localException7.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    if (mDeviceStateMonitor != null)
    {
      paramPrintWriter.println("DeviceStateMonitor:");
      mDeviceStateMonitor.dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
    if ((mCi != null) && ((mCi instanceof RIL)))
    {
      try
      {
        ((RIL)mCi).dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Exception paramFileDescriptor)
      {
        paramFileDescriptor.printStackTrace();
      }
      paramPrintWriter.flush();
      paramPrintWriter.println("++++++++++++++++++++++++++++++++");
    }
  }
  
  public void enableEnhancedVoicePrivacy(boolean paramBoolean, Message paramMessage) {}
  
  public void exitEmergencyCallbackMode() {}
  
  public String getActionAttached()
  {
    return mActionAttached;
  }
  
  public String getActionDetached()
  {
    return mActionDetached;
  }
  
  public String getActiveApnHost(String paramString)
  {
    return mDcTracker.getActiveApnString(paramString);
  }
  
  public String[] getActiveApnTypes()
  {
    if (mDcTracker == null) {
      return null;
    }
    return mDcTracker.getActiveApnTypes();
  }
  
  public List<CellInfo> getAllCellInfo(WorkSource paramWorkSource)
  {
    return privatizeCellInfoList(getServiceStateTracker().getAllCellInfo(paramWorkSource));
  }
  
  public void getAllowedCarriers(Message paramMessage)
  {
    mCi.getAllowedCarriers(paramMessage);
  }
  
  public AppSmsManager getAppSmsManager()
  {
    return mAppSmsManager;
  }
  
  protected int getAsusNwTypeFilter(int paramInt)
  {
    int i = SubscriptionController.getInstance().getPhoneId(SubscriptionManager.getDefaultDataSubscriptionId());
    int j = getPhoneId();
    if ((SubscriptionManager.isValidPhoneId(i)) && (SubscriptionManager.isValidPhoneId(j)))
    {
      if (i == j) {
        return paramInt;
      }
      i = paramInt;
      if (Settings.Global.getInt(mContext.getContentResolver(), "asus_nondds4g", 0) == 0)
      {
        i = RadioAccessFamily.getNetworkTypeFromRaf(RadioAccessFamily.getRafFromNetworkType(paramInt) & 0xFFF7BFFF);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("[ABSP] change ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" to ");
        localStringBuilder.append(i);
        localStringBuilder.append(" @ phone");
        localStringBuilder.append(j);
        Rlog.i("Phone", localStringBuilder.toString());
      }
      return i;
    }
    return paramInt;
  }
  
  public boolean getCallForwardingIndicator()
  {
    int i = getPhoneType();
    boolean bool = false;
    if (i == 2)
    {
      loge("Phone", "getCallForwardingIndicator: not possible in CDMA");
      return false;
    }
    Object localObject = (IccRecords)mIccRecords.get();
    i = -1;
    if (localObject != null) {
      i = ((IccRecords)localObject).getVoiceCallForwardingFlag();
    }
    int j = i;
    if (i == -1) {
      j = getCallForwardingIndicatorFromSharedPref();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getCallForwardingIndicator: iccForwardingFlag=");
    if (localObject != null) {
      localObject = Integer.valueOf(((IccRecords)localObject).getVoiceCallForwardingFlag());
    } else {
      localObject = "null";
    }
    localStringBuilder.append(localObject);
    localStringBuilder.append(", sharedPrefFlag=");
    localStringBuilder.append(getCallForwardingIndicatorFromSharedPref());
    Rlog.v("Phone", localStringBuilder.toString());
    if ((j != 1) && (!getVideoCallForwardingPreference())) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public void getCallForwardingOption(int paramInt1, int paramInt2, Message paramMessage) {}
  
  public CallTracker getCallTracker()
  {
    return null;
  }
  
  public CarrierActionAgent getCarrierActionAgent()
  {
    return mCarrierActionAgent;
  }
  
  public int getCarrierId()
  {
    return -1;
  }
  
  public int getCarrierIdListVersion()
  {
    return -1;
  }
  
  public ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int paramInt)
  {
    return null;
  }
  
  public String getCarrierName()
  {
    return null;
  }
  
  public CarrierSignalAgent getCarrierSignalAgent()
  {
    return mCarrierSignalAgent;
  }
  
  public int getCdmaEriIconIndex()
  {
    return -1;
  }
  
  public int getCdmaEriIconMode()
  {
    return -1;
  }
  
  public String getCdmaEriText()
  {
    return "GSM nw, no ERI";
  }
  
  public String getCdmaMin()
  {
    return null;
  }
  
  public String getCdmaPrlVersion()
  {
    return null;
  }
  
  public CellLocation getCellLocation()
  {
    return getCellLocation(null);
  }
  
  public List<ClientRequestStats> getClientRequestStats()
  {
    return mCi.getClientRequestStats();
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public Uri[] getCurrentSubscriberUris()
  {
    return null;
  }
  
  public IccCardApplicationStatus.AppType getCurrentUiccAppType()
  {
    UiccCardApplication localUiccCardApplication = (UiccCardApplication)mUiccApplication.get();
    if (localUiccCardApplication != null) {
      return localUiccCardApplication.getType();
    }
    return IccCardApplicationStatus.AppType.APPTYPE_UNKNOWN;
  }
  
  public int getDataActivationState()
  {
    return mSimActivationTracker.getDataActivationState();
  }
  
  public PhoneConstants.DataState getDataConnectionState()
  {
    return getDataConnectionState("default");
  }
  
  public Phone getDefaultPhone()
  {
    return this;
  }
  
  public void getEnhancedVoicePrivacy(Message paramMessage) {}
  
  public String getFullIccSerialNumber()
  {
    Object localObject = (IccRecords)mIccRecords.get();
    if (localObject != null) {
      localObject = ((IccRecords)localObject).getFullIccId();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public Handler getHandler()
  {
    return this;
  }
  
  public ArrayList<Connection> getHandoverConnection()
  {
    return null;
  }
  
  public IccCard getIccCard()
  {
    return null;
  }
  
  public IccFileHandler getIccFileHandler()
  {
    Object localObject = (UiccCardApplication)mUiccApplication.get();
    if (localObject == null)
    {
      logd("Phone", "getIccFileHandler: uiccApplication == null, return null");
      localObject = null;
    }
    else
    {
      localObject = ((UiccCardApplication)localObject).getIccFileHandler();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getIccFileHandler: fh=");
    localStringBuilder.append(localObject);
    logd("Phone", localStringBuilder.toString());
    return localObject;
  }
  
  public IccRecords getIccRecords()
  {
    return (IccRecords)mIccRecords.get();
  }
  
  public boolean getIccRecordsLoaded()
  {
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    boolean bool;
    if (localIccRecords != null) {
      bool = localIccRecords.getRecordsLoaded();
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String getIccSerialNumber()
  {
    Object localObject = (IccRecords)mIccRecords.get();
    if (localObject != null) {
      localObject = ((IccRecords)localObject).getIccId();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public IccSmsInterfaceManager getIccSmsInterfaceManager()
  {
    return null;
  }
  
  public Phone getImsPhone()
  {
    return mImsPhone;
  }
  
  public int getImsRegistrationTech()
  {
    Object localObject = mImsPhone;
    int i = -1;
    if (localObject != null) {
      i = ((Phone)localObject).getImsRegistrationTech();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("getImsRegistrationTechnology =");
    ((StringBuilder)localObject).append(i);
    logd("Phone", ((StringBuilder)localObject).toString());
    return i;
  }
  
  public IsimRecords getIsimRecords()
  {
    loge("Phone", "getIsimRecords() is only supported on LTE devices");
    return null;
  }
  
  public int getLceStatus()
  {
    return mLceStatus;
  }
  
  public LinkProperties getLinkProperties(String paramString)
  {
    return mDcTracker.getLinkProperties(paramString);
  }
  
  public Locale getLocaleFromSimAndCarrierPrefs()
  {
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    if ((localIccRecords != null) && (localIccRecords.getSimLanguage() != null)) {
      return new Locale(localIccRecords.getSimLanguage());
    }
    return getLocaleFromCarrierProperties(mContext);
  }
  
  public int getLteOnCdmaMode()
  {
    return mCi.getLteOnCdmaMode();
  }
  
  public boolean getMessageWaitingIndicator()
  {
    boolean bool;
    if (mVmCount != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void getModemActivityInfo(Message paramMessage)
  {
    mCi.getModemActivityInfo(paramMessage);
  }
  
  public String getModemUuId()
  {
    Object localObject = getRadioCapability();
    if (localObject == null) {
      localObject = "";
    } else {
      localObject = ((RadioCapability)localObject).getLogicalModemUuid();
    }
    return localObject;
  }
  
  public String getMsisdn()
  {
    return null;
  }
  
  public String getNai()
  {
    return null;
  }
  
  public NetworkCapabilities getNetworkCapabilities(String paramString)
  {
    return mDcTracker.getNetworkCapabilities(paramString);
  }
  
  public void getNetworkSelectionMode(Message paramMessage)
  {
    mCi.getNetworkSelectionMode(paramMessage);
  }
  
  public String getOperatorNumeric()
  {
    return "";
  }
  
  public String[] getPcscfAddress(String paramString)
  {
    return mDcTracker.getPcscfAddress(paramString);
  }
  
  public int getPhoneId()
  {
    return mPhoneId;
  }
  
  public String getPhoneName()
  {
    return mName;
  }
  
  public abstract int getPhoneType();
  
  public String getPlmn()
  {
    return null;
  }
  
  public Registrant getPostDialHandler()
  {
    return mPostDialHandler;
  }
  
  public void getPreferredNetworkType(Message paramMessage)
  {
    mCi.getPreferredNetworkType(paramMessage);
  }
  
  public int getRadioAccessFamily()
  {
    RadioCapability localRadioCapability = getRadioCapability();
    int i;
    if (localRadioCapability == null) {
      i = 1;
    } else {
      i = localRadioCapability.getRadioAccessFamily();
    }
    return i;
  }
  
  public RadioCapability getRadioCapability()
  {
    return (RadioCapability)mRadioCapability.get();
  }
  
  public SIMRecords getSIMRecords()
  {
    return null;
  }
  
  public ServiceStateTracker getServiceStateTracker()
  {
    return null;
  }
  
  public SignalStrength getSignalStrength()
  {
    ServiceStateTracker localServiceStateTracker = getServiceStateTracker();
    if (localServiceStateTracker == null) {
      return new SignalStrength();
    }
    return localServiceStateTracker.getSignalStrength();
  }
  
  public int getSimType()
  {
    int i = 0;
    Object localObject = mUiccController.getUiccCard(mPhoneId);
    if (localObject != null) {
      i = ((UiccCard)localObject).getSimType();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("[ASIM] PhoneBase SIM Type: ");
    ((StringBuilder)localObject).append(i);
    logd("Phone", ((StringBuilder)localObject).toString());
    return i;
  }
  
  public SimulatedRadioControl getSimulatedRadioControl()
  {
    return mSimulatedRadioControl;
  }
  
  public void getSmscAddress(Message paramMessage)
  {
    mCi.getSmscAddress(paramMessage);
  }
  
  public abstract PhoneConstants.State getState();
  
  protected int getStoredVoiceMessageCount()
  {
    int i = 0;
    int j = 0;
    int k = 0;
    int m = getSubId();
    Object localObject1;
    if (SubscriptionController.getInstance().isActiveSubId(m))
    {
      localObject1 = PreferenceManager.getDefaultSharedPreferences(mContext);
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("vm_count_key");
      ((StringBuilder)localObject2).append(m);
      j = ((SharedPreferences)localObject1).getInt(((StringBuilder)localObject2).toString(), -2);
      if (j != -2)
      {
        i = j;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("getStoredVoiceMessageCount: from preference for subId ");
        ((StringBuilder)localObject1).append(m);
        ((StringBuilder)localObject1).append("= ");
        ((StringBuilder)localObject1).append(i);
        logd("Phone", ((StringBuilder)localObject1).toString());
      }
      else
      {
        localObject2 = ((SharedPreferences)localObject1).getString("vm_id_key", null);
        if (localObject2 != null)
        {
          String str = getSubscriberId();
          if ((str != null) && (str.equals(localObject2)))
          {
            i = ((SharedPreferences)localObject1).getInt("vm_count_key", 0);
            setVoiceMessageCount(i);
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("getStoredVoiceMessageCount: from preference = ");
            ((StringBuilder)localObject2).append(i);
            logd("Phone", ((StringBuilder)localObject2).toString());
          }
          else
          {
            logd("Phone", "getStoredVoiceMessageCount: returning 0 as count for matching subscriberId not found");
            i = k;
          }
          localObject1 = ((SharedPreferences)localObject1).edit();
          ((SharedPreferences.Editor)localObject1).remove("vm_id_key");
          ((SharedPreferences.Editor)localObject1).remove("vm_count_key");
          ((SharedPreferences.Editor)localObject1).apply();
        }
      }
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("getStoredVoiceMessageCount: invalid subId ");
      ((StringBuilder)localObject1).append(m);
      loge("Phone", ((StringBuilder)localObject1).toString());
      i = j;
    }
    return i;
  }
  
  public int getSubId()
  {
    return SubscriptionController.getInstance().getSubIdUsingPhoneId(mPhoneId);
  }
  
  public String getSystemProperty(String paramString1, String paramString2)
  {
    if (getUnitTestMode()) {
      return null;
    }
    return SystemProperties.get(paramString1, paramString2);
  }
  
  public UiccCard getUiccCard()
  {
    return mUiccController.getUiccCard(mPhoneId);
  }
  
  public boolean getUnitTestMode()
  {
    return mUnitTestMode;
  }
  
  public UsimServiceTable getUsimServiceTable()
  {
    Object localObject = (IccRecords)mIccRecords.get();
    if (localObject != null) {
      localObject = ((IccRecords)localObject).getUsimServiceTable();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public boolean getVideoCallForwardingPreference()
  {
    logd("Phone", "Get video call forwarding info from preferences");
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("cf_key_video");
    localStringBuilder.append(getSubId());
    return localSharedPreferences.getBoolean(localStringBuilder.toString(), false);
  }
  
  public int getVoiceActivationState()
  {
    return mSimActivationTracker.getVoiceActivationState();
  }
  
  public int getVoiceMessageCount()
  {
    return mVmCount;
  }
  
  public int getVoicePhoneServiceState()
  {
    Phone localPhone = mImsPhone;
    if ((localPhone != null) && (localPhone.getServiceState().getState() == 0)) {
      return 0;
    }
    return getServiceState().getState();
  }
  
  public NetworkStats getVtDataUsage(boolean paramBoolean)
  {
    if (mImsPhone == null) {
      return null;
    }
    return mImsPhone.getVtDataUsage(paramBoolean);
  }
  
  protected void handleExitEmergencyCallbackMode() {}
  
  public void handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      switch (what)
      {
      default: 
        throw new RuntimeException("unexpected event not handled");
      }
      break;
    }
    handleSetSelectNetwork((AsyncResult)obj);
    return;
    onCheckForNetworkSelectionModeAutomatic(paramMessage);
    return;
    paramMessage = (AsyncResult)obj;
    Object localObject;
    if (exception != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("config LCE service failed: ");
      ((StringBuilder)localObject).append(exception);
      logd("Phone", ((StringBuilder)localObject).toString());
    }
    else
    {
      mLceStatus = ((Integer)((ArrayList)result).get(0)).intValue();
      return;
      localObject = (AsyncResult)obj;
      if (exception == null)
      {
        paramMessage = (byte[])result;
        mNotifier.notifyOemHookRawEventForSubscriber(getSubId(), paramMessage);
      }
      else
      {
        paramMessage = new StringBuilder();
        paramMessage.append("OEM hook raw exception: ");
        paramMessage.append(exception);
        loge("Phone", paramMessage.toString());
        return;
        logd("Phone", "Event EVENT_INITIATE_SILENT_REDIAL Received");
        paramMessage = (AsyncResult)obj;
        if ((exception == null) && (result != null))
        {
          localObject = (String)result;
          if (TextUtils.isEmpty((CharSequence)localObject)) {
            return;
          }
          try
          {
            paramMessage = new com/android/internal/telephony/PhoneInternalInterface$DialArgs$Builder;
            paramMessage.<init>();
            dialInternal((String)localObject, paramMessage.build());
          }
          catch (CallStateException paramMessage)
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("silent redial failed: ");
            ((StringBuilder)localObject).append(paramMessage);
            loge("Phone", ((StringBuilder)localObject).toString());
          }
          return;
          paramMessage = (AsyncResult)obj;
          if (exception == null)
          {
            handleSrvccStateChanged((int[])result);
          }
          else
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Srvcc exception: ");
            ((StringBuilder)localObject).append(exception);
            loge("Phone", ((StringBuilder)localObject).toString());
            return;
            onUpdateIccAvailability();
            return;
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("Event EVENT_CALL_RING_CONTINUE Received state=");
            ((StringBuilder)localObject).append(getState());
            logd("Phone", ((StringBuilder)localObject).toString());
            if (getState() == PhoneConstants.State.RINGING)
            {
              sendIncomingCallRingNotification(arg1);
              return;
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("Event EVENT_CALL_RING Received state=");
              ((StringBuilder)localObject).append(getState());
              logd("Phone", ((StringBuilder)localObject).toString());
              if (obj).exception == null)
              {
                paramMessage = getState();
                if ((!mDoesRilSendMultipleCallRing) && ((paramMessage == PhoneConstants.State.RINGING) || (paramMessage == PhoneConstants.State.IDLE)))
                {
                  mCallRingContinueToken += 1;
                  sendIncomingCallRingNotification(mCallRingContinueToken);
                }
                else
                {
                  notifyIncomingRing();
                }
              }
            }
          }
        }
      }
    }
  }
  
  public boolean hasMatchedTetherApnSetting()
  {
    return mDcTracker.hasMatchedTetherApnSetting();
  }
  
  @Deprecated
  public void invokeOemRilRequestRaw(byte[] paramArrayOfByte, Message paramMessage)
  {
    mCi.invokeOemRilRequestRaw(paramArrayOfByte, paramMessage);
  }
  
  @Deprecated
  public void invokeOemRilRequestStrings(String[] paramArrayOfString, Message paramMessage)
  {
    mCi.invokeOemRilRequestStrings(paramArrayOfString, paramMessage);
  }
  
  public boolean isConcurrentVoiceAndDataAllowed()
  {
    ServiceStateTracker localServiceStateTracker = getServiceStateTracker();
    boolean bool;
    if (localServiceStateTracker == null) {
      bool = false;
    } else {
      bool = localServiceStateTracker.isConcurrentVoiceAndDataAllowed();
    }
    return bool;
  }
  
  public boolean isCspPlmnEnabled()
  {
    return false;
  }
  
  public boolean isDataAllowed()
  {
    boolean bool;
    if ((mDcTracker != null) && (mDcTracker.isDataAllowed(null))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDataAllowed(DataConnectionReasons paramDataConnectionReasons)
  {
    boolean bool;
    if ((mDcTracker != null) && (mDcTracker.isDataAllowed(paramDataConnectionReasons))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDnsCheckDisabled()
  {
    return mDnsCheckDisabled;
  }
  
  public boolean isEmergencyNumber(String paramString)
  {
    return PhoneNumberUtils.isEmergencyNumber(getSubId(), paramString);
  }
  
  public boolean isImsAvailable()
  {
    if (mImsPhone == null) {
      return false;
    }
    return mImsPhone.isImsAvailable();
  }
  
  public boolean isImsRegistered()
  {
    Object localObject = mImsPhone;
    boolean bool = false;
    if (localObject != null)
    {
      bool = ((Phone)localObject).isImsRegistered();
    }
    else
    {
      localObject = getServiceStateTracker();
      if (localObject != null) {
        bool = ((ServiceStateTracker)localObject).isImsRegistered();
      }
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("isImsRegistered =");
    ((StringBuilder)localObject).append(bool);
    logd("Phone", ((StringBuilder)localObject).toString());
    return bool;
  }
  
  public boolean isImsUseEnabled()
  {
    ImsManager localImsManager = ImsManager.getInstance(mContext, mPhoneId);
    boolean bool;
    if (((localImsManager.isVolteEnabledByPlatform()) && (localImsManager.isEnhanced4gLteModeSettingEnabledByUser())) || ((localImsManager.isWfcEnabledByPlatform()) && (localImsManager.isWfcEnabledByUser()) && (localImsManager.isNonTtyOrTtyOnVolteEnabled()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isImsVideoCallOrConferencePresent()
  {
    boolean bool = false;
    if (mImsPhone != null) {
      if ((!isVideoCallOrConference(mImsPhone.getForegroundCall())) && (!isVideoCallOrConference(mImsPhone.getBackgroundCall())) && (!isVideoCallOrConference(mImsPhone.getRingingCall()))) {
        bool = false;
      } else {
        bool = true;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("isImsVideoCallOrConferencePresent: ");
    localStringBuilder.append(bool);
    logd("Phone", localStringBuilder.toString());
    return bool;
  }
  
  public boolean isInEcm()
  {
    return mIsPhoneInEcmState;
  }
  
  public boolean isInEmergencyCall()
  {
    return false;
  }
  
  protected boolean isMatchGid(String paramString)
  {
    String str = getGroupIdLevel1();
    int i = paramString.length();
    return (!TextUtils.isEmpty(str)) && (str.length() >= i) && (str.substring(0, i).equalsIgnoreCase(paramString));
  }
  
  public boolean isMccMncMarkedAsNonRoaming(String paramString)
  {
    return getRoamingOverrideHelper("gsm_non_roaming_list_", paramString);
  }
  
  public boolean isMccMncMarkedAsRoaming(String paramString)
  {
    return getRoamingOverrideHelper("gsm_roaming_list_", paramString);
  }
  
  public boolean isMinInfoReady()
  {
    return false;
  }
  
  public boolean isOtaSpNumber(String paramString)
  {
    return false;
  }
  
  public boolean isRadioAvailable()
  {
    return mCi.getRadioState().isAvailable();
  }
  
  public boolean isRadioOn()
  {
    return mCi.getRadioState().isOn();
  }
  
  public boolean isShuttingDown()
  {
    return getServiceStateTracker().isDeviceShuttingDown();
  }
  
  public boolean isSidMarkedAsNonRoaming(int paramInt)
  {
    return getRoamingOverrideHelper("cdma_non_roaming_list_", Integer.toString(paramInt));
  }
  
  public boolean isSidMarkedAsRoaming(int paramInt)
  {
    return getRoamingOverrideHelper("cdma_roaming_list_", Integer.toString(paramInt));
  }
  
  public boolean isUtEnabled()
  {
    if (mImsPhone != null) {
      return mImsPhone.isUtEnabled();
    }
    return false;
  }
  
  public boolean isVideoEnabled()
  {
    Phone localPhone = mImsPhone;
    if (localPhone != null) {
      return localPhone.isVideoEnabled();
    }
    return false;
  }
  
  public boolean isVolteEnabled()
  {
    Object localObject = mImsPhone;
    boolean bool = false;
    if (localObject != null) {
      bool = ((Phone)localObject).isVolteEnabled();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("isImsRegistered =");
    ((StringBuilder)localObject).append(bool);
    logd("Phone", ((StringBuilder)localObject).toString());
    return bool;
  }
  
  public boolean isWifiCallingEnabled()
  {
    Object localObject = mImsPhone;
    boolean bool = false;
    if (localObject != null) {
      bool = ((Phone)localObject).isWifiCallingEnabled();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("isWifiCallingEnabled =");
    ((StringBuilder)localObject).append(bool);
    logd("Phone", ((StringBuilder)localObject).toString());
    return bool;
  }
  
  protected void migrate(RegistrantList paramRegistrantList1, RegistrantList paramRegistrantList2)
  {
    paramRegistrantList2.removeCleared();
    int i = 0;
    int j = paramRegistrantList2.size();
    while (i < j)
    {
      Message localMessage = ((Registrant)paramRegistrantList2.get(i)).messageForRegistrant();
      if (localMessage != null)
      {
        if (obj != CallManager.getInstance().getRegistrantIdentifier()) {
          paramRegistrantList1.add((Registrant)paramRegistrantList2.get(i));
        }
      }
      else {
        logd("Phone", "msg is null");
      }
      i++;
    }
  }
  
  protected void migrateFrom(Phone paramPhone)
  {
    migrate(mHandoverRegistrants, mHandoverRegistrants);
    migrate(mPreciseCallStateRegistrants, mPreciseCallStateRegistrants);
    migrate(mNewRingingConnectionRegistrants, mNewRingingConnectionRegistrants);
    migrate(mIncomingRingRegistrants, mIncomingRingRegistrants);
    migrate(mDisconnectRegistrants, mDisconnectRegistrants);
    migrate(mServiceStateRegistrants, mServiceStateRegistrants);
    migrate(mMmiCompleteRegistrants, mMmiCompleteRegistrants);
    migrate(mMmiRegistrants, mMmiRegistrants);
    migrate(mUnknownConnectionRegistrants, mUnknownConnectionRegistrants);
    migrate(mSuppServiceFailedRegistrants, mSuppServiceFailedRegistrants);
    if (paramPhone.isInEmergencyCall()) {
      setIsInEmergencyCall();
    }
  }
  
  public boolean needsOtaServiceProvisioning()
  {
    return false;
  }
  
  public void notifyCallForwardingIndicator() {}
  
  public void notifyCellInfo(List<CellInfo> paramList)
  {
    mNotifier.notifyCellInfo(this, privatizeCellInfoList(paramList));
  }
  
  public void notifyDataActivationStateChanged(int paramInt)
  {
    mNotifier.notifyDataActivationStateChanged(this, paramInt);
  }
  
  public void notifyDataActivity()
  {
    mNotifier.notifyDataActivity(this);
  }
  
  public void notifyDataConnection(String paramString)
  {
    for (String str : getActiveApnTypes()) {
      mNotifier.notifyDataConnection(this, paramString, str, getDataConnectionState(str));
    }
  }
  
  public void notifyDataConnection(String paramString1, String paramString2)
  {
    mNotifier.notifyDataConnection(this, paramString1, paramString2, getDataConnectionState(paramString2));
  }
  
  public void notifyDataConnection(String paramString1, String paramString2, PhoneConstants.DataState paramDataState)
  {
    mNotifier.notifyDataConnection(this, paramString1, paramString2, paramDataState);
  }
  
  public void notifyDataConnectionFailed(String paramString1, String paramString2)
  {
    mNotifier.notifyDataConnectionFailed(this, paramString1, paramString2);
  }
  
  protected void notifyDisconnectP(Connection paramConnection)
  {
    paramConnection = new AsyncResult(null, paramConnection, null);
    mDisconnectRegistrants.notifyRegistrants(paramConnection);
  }
  
  public void notifyForVideoCapabilityChanged(boolean paramBoolean)
  {
    mIsVideoCapable = paramBoolean;
    AsyncResult localAsyncResult = new AsyncResult(null, Boolean.valueOf(paramBoolean), null);
    mVideoCapabilityChangedRegistrants.notifyRegistrants(localAsyncResult);
  }
  
  public void notifyHandoverStateChanged(Connection paramConnection)
  {
    paramConnection = new AsyncResult(null, paramConnection, null);
    mHandoverRegistrants.notifyRegistrants(paramConnection);
  }
  
  public void notifyNewRingingConnectionP(Connection paramConnection)
  {
    if (!mIsVoiceCapable) {
      return;
    }
    paramConnection = new AsyncResult(null, paramConnection, null);
    mNewRingingConnectionRegistrants.notifyRegistrants(paramConnection);
  }
  
  public void notifyOtaspChanged(int paramInt)
  {
    mNotifier.notifyOtaspChanged(this, paramInt);
  }
  
  public void notifyPhysicalChannelConfiguration(List<PhysicalChannelConfig> paramList)
  {
    mNotifier.notifyPhysicalChannelConfiguration(this, paramList);
  }
  
  protected void notifyPreciseCallStateChangedP()
  {
    AsyncResult localAsyncResult = new AsyncResult(null, this, null);
    mPreciseCallStateRegistrants.notifyRegistrants(localAsyncResult);
    mNotifier.notifyPreciseCallState(this);
  }
  
  public void notifyPreciseDataConnectionFailed(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    mNotifier.notifyPreciseDataConnectionFailed(this, paramString1, paramString2, paramString3, paramString4);
  }
  
  protected void notifyServiceStateChangedP(ServiceState paramServiceState)
  {
    paramServiceState = new AsyncResult(null, paramServiceState, null);
    mServiceStateRegistrants.notifyRegistrants(paramServiceState);
    mNotifier.notifyServiceState(this);
  }
  
  public void notifySignalStrength()
  {
    mNotifier.notifySignalStrength(this);
  }
  
  public void notifySrvccState(Call.SrvccState paramSrvccState) {}
  
  public void notifyUnknownConnectionP(Connection paramConnection)
  {
    mUnknownConnectionRegistrants.notifyResult(paramConnection);
  }
  
  public void notifyUserMobileDataStateChanged(boolean paramBoolean)
  {
    mNotifier.notifyUserMobileDataStateChanged(this, paramBoolean);
  }
  
  public void notifyVoLteServiceStateChanged(VoLteServiceState paramVoLteServiceState)
  {
    mNotifier.notifyVoLteServiceStateChanged(this, paramVoLteServiceState);
  }
  
  public void notifyVoiceActivationStateChanged(int paramInt)
  {
    mNotifier.notifyVoiceActivationStateChanged(this, paramInt);
  }
  
  public void nvReadItem(int paramInt, Message paramMessage)
  {
    mCi.nvReadItem(paramInt, paramMessage);
  }
  
  public void nvResetConfig(int paramInt, Message paramMessage)
  {
    mCi.nvResetConfig(paramInt, paramMessage);
  }
  
  public void nvWriteCdmaPrl(byte[] paramArrayOfByte, Message paramMessage)
  {
    mCi.nvWriteCdmaPrl(paramArrayOfByte, paramMessage);
  }
  
  public void nvWriteItem(int paramInt, String paramString, Message paramMessage)
  {
    mCi.nvWriteItem(paramInt, paramString, paramMessage);
  }
  
  protected abstract void onUpdateIccAvailability();
  
  public void queryAvailableBandMode(Message paramMessage)
  {
    mCi.queryAvailableBandMode(paramMessage);
  }
  
  public void queryCdmaRoamingPreference(Message paramMessage)
  {
    mCi.queryCdmaRoamingPreference(paramMessage);
  }
  
  public void queryTTYMode(Message paramMessage)
  {
    mCi.queryTTYMode(paramMessage);
  }
  
  public void radioCapabilityUpdated(RadioCapability paramRadioCapability)
  {
    mRadioCapability.set(paramRadioCapability);
    if (SubscriptionManager.isValidSubscriptionId(getSubId())) {
      sendSubscriptionSettings(mContext.getResources().getBoolean(17957117) ^ true);
    }
  }
  
  public void registerFoT53ClirlInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerFoT53ClirlInfo(paramHandler, paramInt, paramObject);
  }
  
  public void registerForAllDataDisconnected(Handler paramHandler, int paramInt, Object paramObject)
  {
    mDcTracker.registerForAllDataDisconnected(paramHandler, paramInt, paramObject);
  }
  
  public void registerForCallWaiting(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForCdmaOtaStatusChange(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForDataEnabledChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mDcTracker.registerForDataEnabledChanged(paramHandler, paramInt, paramObject);
  }
  
  public void registerForDisconnect(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("registerForDisconnect, handler = ");
    localStringBuilder.append(paramHandler);
    localStringBuilder.append("msg = ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("object = ");
    localStringBuilder.append(paramObject);
    Rlog.d("Phone", localStringBuilder.toString());
    mDisconnectRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForDisplayInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForDisplayInfo(paramHandler, paramInt, paramObject);
  }
  
  public void registerForEcmTimerReset(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForEmergencyCallToggle(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mEmergencyCallToggledRegistrants.add(paramHandler);
  }
  
  public void registerForHandoverStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mHandoverRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForInCallVoicePrivacyOff(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForInCallVoicePrivacyOff(paramHandler, paramInt, paramObject);
  }
  
  public void registerForInCallVoicePrivacyOn(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForInCallVoicePrivacyOn(paramHandler, paramInt, paramObject);
  }
  
  public void registerForIncomingRing(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mIncomingRingRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForLineControlInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForLineControlInfo(paramHandler, paramInt, paramObject);
  }
  
  public void registerForMmiComplete(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mMmiCompleteRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForMmiInitiate(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mMmiRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForNewRingingConnection(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mNewRingingConnectionRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForNumberInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForNumberInfo(paramHandler, paramInt, paramObject);
  }
  
  public void registerForOnHoldTone(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForPreciseCallStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mPreciseCallStateRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForRadioCapabilityChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForRadioCapabilityChanged(paramHandler, paramInt, paramObject);
  }
  
  public void registerForRadioOffOrNotAvailable(Handler paramHandler, int paramInt, Object paramObject)
  {
    mRadioOffOrNotAvailableRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForRedirectedNumberInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForRedirectedNumberInfo(paramHandler, paramInt, paramObject);
  }
  
  public void registerForResendIncallMute(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForResendIncallMute(paramHandler, paramInt, paramObject);
  }
  
  public void registerForRingbackTone(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForRingbackTone(paramHandler, paramInt, paramObject);
  }
  
  public void registerForServiceStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    mServiceStateRegistrants.add(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSignalInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForSignalInfo(paramHandler, paramInt, paramObject);
  }
  
  public void registerForSilentRedial(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForSimRecordsLoaded(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForSubscriptionInfoReady(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForSuppServiceFailed(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mSuppServiceFailedRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForT53AudioControlInfo(Handler paramHandler, int paramInt, Object paramObject)
  {
    mCi.registerForT53AudioControlInfo(paramHandler, paramInt, paramObject);
  }
  
  public void registerForTtyModeReceived(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void registerForUnknownConnection(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mUnknownConnectionRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void registerForVideoCapabilityChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    checkCorrectThread(paramHandler);
    mVideoCapabilityChangedRegistrants.addUnique(paramHandler, paramInt, paramObject);
    notifyForVideoCapabilityChanged(mIsVideoCapable);
  }
  
  public void resetCarrierKeysForImsiEncryption() {}
  
  public void saveClirSetting(int paramInt)
  {
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("clir_key");
    localStringBuilder.append(getPhoneId());
    localEditor.putInt(localStringBuilder.toString(), paramInt);
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("saveClirSetting: clir_key");
    localStringBuilder.append(getPhoneId());
    localStringBuilder.append("=");
    localStringBuilder.append(paramInt);
    logi("Phone", localStringBuilder.toString());
    if (!localEditor.commit()) {
      loge("Phone", "Failed to commit CLIR preference");
    }
  }
  
  public void selectNetworkManually(OperatorInfo paramOperatorInfo, boolean paramBoolean, Message paramMessage)
  {
    NetworkSelectMessage localNetworkSelectMessage = new NetworkSelectMessage(null);
    message = paramMessage;
    operatorNumeric = paramOperatorInfo.getOperatorNumeric();
    operatorAlphaLong = paramOperatorInfo.getOperatorAlphaLong();
    operatorAlphaShort = paramOperatorInfo.getOperatorAlphaShort();
    paramMessage = obtainMessage(16, localNetworkSelectMessage);
    if (paramOperatorInfo.getRadioTech().equals(""))
    {
      mCi.setNetworkSelectionModeManual(paramOperatorInfo.getOperatorNumeric(), paramMessage);
    }
    else
    {
      CommandsInterface localCommandsInterface = mCi;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramOperatorInfo.getOperatorNumeric());
      localStringBuilder.append("+");
      localStringBuilder.append(paramOperatorInfo.getRadioTech());
      localCommandsInterface.setNetworkSelectionModeManual(localStringBuilder.toString(), paramMessage);
    }
    if (paramBoolean) {
      updateSavedNetworkOperator(localNetworkSelectMessage);
    } else {
      clearSavedNetworkSelection();
    }
  }
  
  public void sendBurstDtmf(String paramString, int paramInt1, int paramInt2, Message paramMessage) {}
  
  public void sendDialerSpecialCode(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("android_secret_code://");
      localStringBuilder.append(paramString);
      paramString = new Intent("android.provider.Telephony.SECRET_CODE", Uri.parse(localStringBuilder.toString()));
      paramString.addFlags(16777216);
      mContext.sendBroadcast(paramString);
    }
  }
  
  public abstract void sendEmergencyCallStateChange(boolean paramBoolean);
  
  public void sendSubscriptionSettings(boolean paramBoolean)
  {
    setPreferredNetworkType(PhoneFactory.calculatePreferredNetworkType(mContext, getSubId()), null);
    if (paramBoolean) {
      restoreSavedNetworkSelection(null);
    }
  }
  
  public void setAllowedCarriers(List<CarrierIdentifier> paramList, Message paramMessage)
  {
    mCi.setAllowedCarriers(paramList, paramMessage);
  }
  
  public void setBandMode(int paramInt, Message paramMessage)
  {
    mCi.setBandMode(paramInt, paramMessage);
  }
  
  public abstract void setBroadcastEmergencyCallStateChanges(boolean paramBoolean);
  
  public void setCallForwardingOption(int paramInt1, int paramInt2, String paramString, int paramInt3, int paramInt4, Message paramMessage) {}
  
  public void setCarrierInfoForImsiEncryption(ImsiEncryptionInfo paramImsiEncryptionInfo) {}
  
  public void setCarrierTestOverride(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7) {}
  
  public void setCdmaRoamingPreference(int paramInt, Message paramMessage)
  {
    mCi.setCdmaRoamingPreference(paramInt, paramMessage);
  }
  
  public void setCdmaSubscription(int paramInt, Message paramMessage)
  {
    mCi.setCdmaSubscriptionSource(paramInt, paramMessage);
  }
  
  public void setCellInfoListRate(int paramInt, WorkSource paramWorkSource)
  {
    mCi.setCellInfoListRate(paramInt, null, paramWorkSource);
  }
  
  public void setDataActivationState(int paramInt)
  {
    mSimActivationTracker.setDataActivationState(paramInt);
  }
  
  public void setEchoSuppressionEnabled() {}
  
  public void setGlobalSystemProperty(String paramString1, String paramString2)
  {
    if (getUnitTestMode()) {
      return;
    }
    TelephonyManager.setTelephonyProperty(paramString1, paramString2);
  }
  
  public void setImsRegistrationState(boolean paramBoolean) {}
  
  public void setInternalDataEnabled(boolean paramBoolean, Message paramMessage)
  {
    mDcTracker.setInternalDataEnabled(paramBoolean, paramMessage);
  }
  
  public void setIsInEcm(boolean paramBoolean)
  {
    setGlobalSystemProperty("ril.cdma.inecmmode", String.valueOf(paramBoolean));
    mIsPhoneInEcmState = paramBoolean;
  }
  
  protected void setIsInEmergencyCall() {}
  
  public void setLinkCapacityReportingCriteria(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt) {}
  
  public void setNetworkSelectionModeAutomatic(Message paramMessage)
  {
    logd("Phone", "setNetworkSelectionModeAutomatic, querying current mode");
    Message localMessage = obtainMessage(38);
    obj = paramMessage;
    mCi.getNetworkSelectionMode(localMessage);
  }
  
  public void setOnEcbModeExitResponse(Handler paramHandler, int paramInt, Object paramObject) {}
  
  public void setOnPostDialCharacter(Handler paramHandler, int paramInt, Object paramObject)
  {
    mPostDialHandler = new Registrant(paramHandler, paramInt, paramObject);
  }
  
  public boolean setOperatorBrandOverride(String paramString)
  {
    return false;
  }
  
  protected void setPhoneName(String paramString)
  {
    mName = paramString;
  }
  
  public void setPolicyDataEnabled(boolean paramBoolean)
  {
    mDcTracker.setPolicyDataEnabled(paramBoolean);
  }
  
  public void setPreferredNetworkType(int paramInt, Message paramMessage)
  {
    int i = getRadioAccessFamily();
    int j = RadioAccessFamily.getRafFromNetworkType(paramInt);
    if ((i != 1) && (j != 1))
    {
      int k = RadioAccessFamily.getNetworkTypeFromRaf(j & i);
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("setPreferredNetworkType: networkType = ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" modemRaf = ");
      localStringBuilder.append(i);
      localStringBuilder.append(" rafFromType = ");
      localStringBuilder.append(j);
      localStringBuilder.append(" filteredType = ");
      localStringBuilder.append(k);
      logd("Phone", localStringBuilder.toString());
      paramInt = getAsusNwTypeFilter(k);
      mCi.setPreferredNetworkType(paramInt, paramMessage);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setPreferredNetworkType: Abort, unknown RAF: ");
    localStringBuilder.append(i);
    localStringBuilder.append(" ");
    localStringBuilder.append(j);
    logd("Phone", localStringBuilder.toString());
    if (paramMessage != null)
    {
      AsyncResult.forMessage(paramMessage, null, new CommandException(CommandException.Error.GENERIC_FAILURE));
      paramMessage.sendToTarget();
    }
  }
  
  protected void setPreferredNetworkTypeIfSimLoaded()
  {
    int i = getSubId();
    if (SubscriptionManager.from(mContext).isActiveSubId(i)) {
      setPreferredNetworkType(PhoneFactory.calculatePreferredNetworkType(mContext, getSubId()), null);
    }
  }
  
  public void setRadioCapability(RadioCapability paramRadioCapability, Message paramMessage)
  {
    mCi.setRadioCapability(paramRadioCapability, paramMessage);
  }
  
  public void setRadioIndicationUpdateMode(int paramInt1, int paramInt2)
  {
    if (mDeviceStateMonitor != null) {
      mDeviceStateMonitor.setIndicationUpdateMode(paramInt1, paramInt2);
    }
  }
  
  public boolean setRoamingOverride(List<String> paramList1, List<String> paramList2, List<String> paramList3, List<String> paramList4)
  {
    String str = getIccSerialNumber();
    if (TextUtils.isEmpty(str)) {
      return false;
    }
    setRoamingOverrideHelper(paramList1, "gsm_roaming_list_", str);
    setRoamingOverrideHelper(paramList2, "gsm_non_roaming_list_", str);
    setRoamingOverrideHelper(paramList3, "cdma_roaming_list_", str);
    setRoamingOverrideHelper(paramList4, "cdma_non_roaming_list_", str);
    paramList1 = getServiceStateTracker();
    if (paramList1 != null) {
      paramList1.pollState();
    }
    return true;
  }
  
  public void setSignalStrengthReportingCriteria(int[] paramArrayOfInt, int paramInt) {}
  
  public void setSimPowerState(int paramInt)
  {
    mCi.setSimCardPower(paramInt, null);
  }
  
  public void setSmscAddress(String paramString, Message paramMessage)
  {
    mCi.setSmscAddress(paramString, paramMessage);
  }
  
  public void setSystemProperty(String paramString1, String paramString2)
  {
    if (getUnitTestMode()) {
      return;
    }
    TelephonyManager.setTelephonyProperty(mPhoneId, paramString1, paramString2);
  }
  
  public void setTTYMode(int paramInt, Message paramMessage)
  {
    mCi.setTTYMode(paramInt, paramMessage);
  }
  
  public void setUiTTYMode(int paramInt, Message paramMessage)
  {
    logd("Phone", "unexpected setUiTTYMode method call");
  }
  
  public void setVideoCallForwardingPreference(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Set video call forwarding info to preferences enabled = ");
    localStringBuilder.append(paramBoolean);
    localStringBuilder.append("subId = ");
    localStringBuilder.append(getSubId());
    logd("Phone", localStringBuilder.toString());
    SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("cf_key_video");
    localStringBuilder.append(getSubId());
    localEditor.putBoolean(localStringBuilder.toString(), paramBoolean);
    localEditor.commit();
  }
  
  public void setVoiceActivationState(int paramInt)
  {
    mSimActivationTracker.setVoiceActivationState(paramInt);
  }
  
  public void setVoiceCallForwardingFlag(int paramInt, boolean paramBoolean, String paramString)
  {
    setCallForwardingIndicatorInSharedPref(paramBoolean);
    IccRecords localIccRecords = (IccRecords)mIccRecords.get();
    if (localIccRecords != null) {
      localIccRecords.setVoiceCallForwardingFlag(paramInt, paramBoolean, paramString);
    }
  }
  
  protected void setVoiceCallForwardingFlag(IccRecords paramIccRecords, int paramInt, boolean paramBoolean, String paramString)
  {
    setCallForwardingIndicatorInSharedPref(paramBoolean);
    paramIccRecords.setVoiceCallForwardingFlag(paramInt, paramBoolean, paramString);
  }
  
  public void setVoiceMessageCount(int paramInt)
  {
    mVmCount = paramInt;
    int i = getSubId();
    Object localObject;
    if (SubscriptionController.getInstance().isActiveSubId(i))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("setVoiceMessageCount: Storing Voice Mail Count = ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(" for mVmCountKey = ");
      ((StringBuilder)localObject).append("vm_count_key");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(" in preferences.");
      logd("Phone", ((StringBuilder)localObject).toString());
      localObject = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("vm_count_key");
      localStringBuilder.append(i);
      ((SharedPreferences.Editor)localObject).putInt(localStringBuilder.toString(), paramInt);
      ((SharedPreferences.Editor)localObject).apply();
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("setVoiceMessageCount in sharedPreference: invalid subId ");
      ((StringBuilder)localObject).append(i);
      loge("Phone", ((StringBuilder)localObject).toString());
    }
    notifyMessageWaitingIndicator();
  }
  
  public void setVoiceMessageWaiting(int paramInt1, int paramInt2)
  {
    loge("Phone", "Error! This function should never be executed, inactive Phone.");
  }
  
  public void shutdownRadio()
  {
    getServiceStateTracker().requestShutdown();
  }
  
  public void startLceAfterRadioIsAvailable()
  {
    mCi.startLceService(200, true, obtainMessage(37));
  }
  
  public void startMonitoringImsService()
  {
    if (getPhoneType() == 3) {
      return;
    }
    synchronized (lockForRadioTechnologyChange)
    {
      IntentFilter localIntentFilter = new android/content/IntentFilter;
      localIntentFilter.<init>();
      ImsManager localImsManager = ImsManager.getInstance(mContext, getPhoneId());
      if ((localImsManager != null) && (!localImsManager.isDynamicBinding()))
      {
        localIntentFilter.addAction("com.android.ims.IMS_SERVICE_UP");
        localIntentFilter.addAction("com.android.ims.IMS_SERVICE_DOWN");
      }
      mContext.registerReceiver(mImsIntentReceiver, localIntentFilter);
      if ((localImsManager != null) && ((localImsManager.isDynamicBinding()) || (localImsManager.isServiceAvailable())))
      {
        mImsServiceReady = true;
        updateImsPhone();
      }
      return;
    }
  }
  
  public void startRingbackTone() {}
  
  public void stopRingbackTone() {}
  
  public boolean supportsConversionOfCdmaCallerIdMmiCodesWhileRoaming()
  {
    PersistableBundle localPersistableBundle = ((CarrierConfigManager)getContext().getSystemService("carrier_config")).getConfig();
    if (localPersistableBundle != null) {
      return localPersistableBundle.getBoolean("convert_cdma_caller_id_mmi_codes_while_roaming_on_3gpp_bool", false);
    }
    return false;
  }
  
  public void unregisterForAllDataDisconnected(Handler paramHandler)
  {
    mDcTracker.unregisterForAllDataDisconnected(paramHandler);
  }
  
  public void unregisterForCallWaiting(Handler paramHandler) {}
  
  public void unregisterForCdmaOtaStatusChange(Handler paramHandler) {}
  
  public void unregisterForDataEnabledChanged(Handler paramHandler)
  {
    mDcTracker.unregisterForDataEnabledChanged(paramHandler);
  }
  
  public void unregisterForDisconnect(Handler paramHandler)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("unregisterForDisconnect, handler = ");
    localStringBuilder.append(paramHandler);
    Rlog.d("Phone", localStringBuilder.toString());
    mDisconnectRegistrants.remove(paramHandler);
  }
  
  public void unregisterForDisplayInfo(Handler paramHandler)
  {
    mCi.unregisterForDisplayInfo(paramHandler);
  }
  
  public void unregisterForEcmTimerReset(Handler paramHandler) {}
  
  public void unregisterForEmergencyCallToggle(Handler paramHandler)
  {
    mEmergencyCallToggledRegistrants.remove(paramHandler);
  }
  
  public void unregisterForHandoverStateChanged(Handler paramHandler)
  {
    mHandoverRegistrants.remove(paramHandler);
  }
  
  public void unregisterForInCallVoicePrivacyOff(Handler paramHandler)
  {
    mCi.unregisterForInCallVoicePrivacyOff(paramHandler);
  }
  
  public void unregisterForInCallVoicePrivacyOn(Handler paramHandler)
  {
    mCi.unregisterForInCallVoicePrivacyOn(paramHandler);
  }
  
  public void unregisterForIncomingRing(Handler paramHandler)
  {
    mIncomingRingRegistrants.remove(paramHandler);
  }
  
  public void unregisterForLineControlInfo(Handler paramHandler)
  {
    mCi.unregisterForLineControlInfo(paramHandler);
  }
  
  public void unregisterForMmiComplete(Handler paramHandler)
  {
    checkCorrectThread(paramHandler);
    mMmiCompleteRegistrants.remove(paramHandler);
  }
  
  public void unregisterForMmiInitiate(Handler paramHandler)
  {
    mMmiRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNewRingingConnection(Handler paramHandler)
  {
    mNewRingingConnectionRegistrants.remove(paramHandler);
  }
  
  public void unregisterForNumberInfo(Handler paramHandler)
  {
    mCi.unregisterForNumberInfo(paramHandler);
  }
  
  public void unregisterForOnHoldTone(Handler paramHandler) {}
  
  public void unregisterForPreciseCallStateChanged(Handler paramHandler)
  {
    mPreciseCallStateRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRadioCapabilityChanged(Handler paramHandler)
  {
    mCi.unregisterForRadioCapabilityChanged(this);
  }
  
  public void unregisterForRadioOffOrNotAvailable(Handler paramHandler)
  {
    mRadioOffOrNotAvailableRegistrants.remove(paramHandler);
  }
  
  public void unregisterForRedirectedNumberInfo(Handler paramHandler)
  {
    mCi.unregisterForRedirectedNumberInfo(paramHandler);
  }
  
  public void unregisterForResendIncallMute(Handler paramHandler)
  {
    mCi.unregisterForResendIncallMute(paramHandler);
  }
  
  public void unregisterForRingbackTone(Handler paramHandler)
  {
    mCi.unregisterForRingbackTone(paramHandler);
  }
  
  public void unregisterForServiceStateChanged(Handler paramHandler)
  {
    mServiceStateRegistrants.remove(paramHandler);
  }
  
  public void unregisterForSignalInfo(Handler paramHandler)
  {
    mCi.unregisterForSignalInfo(paramHandler);
  }
  
  public void unregisterForSilentRedial(Handler paramHandler) {}
  
  public void unregisterForSimRecordsLoaded(Handler paramHandler) {}
  
  public void unregisterForSubscriptionInfoReady(Handler paramHandler) {}
  
  public void unregisterForSuppServiceFailed(Handler paramHandler)
  {
    mSuppServiceFailedRegistrants.remove(paramHandler);
  }
  
  public void unregisterForT53AudioControlInfo(Handler paramHandler)
  {
    mCi.unregisterForT53AudioControlInfo(paramHandler);
  }
  
  public void unregisterForT53ClirInfo(Handler paramHandler)
  {
    mCi.unregisterForT53ClirInfo(paramHandler);
  }
  
  public void unregisterForTtyModeReceived(Handler paramHandler) {}
  
  public void unregisterForUnknownConnection(Handler paramHandler)
  {
    mUnknownConnectionRegistrants.remove(paramHandler);
  }
  
  public void unregisterForVideoCapabilityChanged(Handler paramHandler)
  {
    mVideoCapabilityChangedRegistrants.remove(paramHandler);
  }
  
  public void unsetOnEcbModeExitResponse(Handler paramHandler) {}
  
  public boolean updateCurrentCarrierInProvider()
  {
    return false;
  }
  
  public void updateDataConnectionTracker()
  {
    mDcTracker.update();
  }
  
  public void updatePhoneObject(int paramInt) {}
  
  public void updateVoiceMail()
  {
    loge("Phone", "updateVoiceMail() should be overridden");
  }
  
  private static class NetworkSelectMessage
  {
    public Message message;
    public String operatorAlphaLong;
    public String operatorAlphaShort;
    public String operatorNumeric;
    
    private NetworkSelectMessage() {}
  }
}
