package android.net.wifi;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.NetworkRequest.Builder;
import android.net.wifi.hotspot2.IProvisioningCallback.Stub;
import android.net.wifi.hotspot2.OsuProvider;
import android.net.wifi.hotspot2.PasspointConfiguration;
import android.net.wifi.hotspot2.ProvisioningCallback;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.WorkSource;
import android.util.Log;
import android.util.SeempLog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AsyncChannel;
import com.android.server.net.NetworkPinner;
import dalvik.system.CloseGuard;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class WifiManager
{
  public static final String ACTION_PASSPOINT_DEAUTH_IMMINENT = "android.net.wifi.action.PASSPOINT_DEAUTH_IMMINENT";
  public static final String ACTION_PASSPOINT_ICON = "android.net.wifi.action.PASSPOINT_ICON";
  public static final String ACTION_PASSPOINT_OSU_PROVIDERS_LIST = "android.net.wifi.action.PASSPOINT_OSU_PROVIDERS_LIST";
  public static final String ACTION_PASSPOINT_SUBSCRIPTION_REMEDIATION = "android.net.wifi.action.PASSPOINT_SUBSCRIPTION_REMEDIATION";
  public static final String ACTION_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK";
  public static final String ACTION_REQUEST_DISABLE = "android.net.wifi.action.REQUEST_DISABLE";
  public static final String ACTION_REQUEST_ENABLE = "android.net.wifi.action.REQUEST_ENABLE";
  public static final String ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE = "android.net.wifi.action.REQUEST_SCAN_ALWAYS_AVAILABLE";
  public static final String ACTION_WIFI_DISCONNECT_IN_PROGRESS = "com.qualcomm.qti.net.wifi.WIFI_DISCONNECT_IN_PROGRESS";
  public static final String ASUS_ACTION_WIFI_AUTH_FAIL_FOR_QRCODE = "asus_action_wifi_auth_fail_for_qrcode";
  public static final String ASUS_ACTION_WIFI_AUTH_FAIL_FOR_QRCODE_SSID = "asus_action_wifi_auth_fail_for_qrcode_ssid";
  private static final int ASUS_MAX_RSSI = -64;
  private static final int ASUS_MIN_RSSI = -88;
  private static final int BAR2_RSSI = -77;
  private static final int BAR3_RSSI = -70;
  private static final int BASE = 151552;
  @Deprecated
  public static final String BATCHED_SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.BATCHED_RESULTS";
  public static final int BUSY = 2;
  public static final int CANCEL_WPS = 151566;
  public static final int CANCEL_WPS_FAILED = 151567;
  public static final int CANCEL_WPS_SUCCEDED = 151568;
  @SystemApi
  public static final int CHANGE_REASON_ADDED = 0;
  @SystemApi
  public static final int CHANGE_REASON_CONFIG_CHANGE = 2;
  @SystemApi
  public static final int CHANGE_REASON_REMOVED = 1;
  @SystemApi
  public static final String CONFIGURED_NETWORKS_CHANGED_ACTION = "android.net.wifi.CONFIGURED_NETWORKS_CHANGE";
  public static final int CONNECT_NETWORK = 151553;
  public static final int CONNECT_NETWORK_FAILED = 151554;
  public static final int CONNECT_NETWORK_SUCCEEDED = 151555;
  public static final int DATA_ACTIVITY_IN = 1;
  public static final int DATA_ACTIVITY_INOUT = 3;
  public static final int DATA_ACTIVITY_NONE = 0;
  public static final int DATA_ACTIVITY_NOTIFICATION = 1;
  public static final int DATA_ACTIVITY_OUT = 2;
  public static final boolean DEFAULT_POOR_NETWORK_AVOIDANCE_ENABLED = false;
  public static final int DISABLE_NETWORK = 151569;
  public static final int DISABLE_NETWORK_FAILED = 151570;
  public static final int DISABLE_NETWORK_SUCCEEDED = 151571;
  public static final String DPP_EVENT_ACTION = "com.qualcomm.qti.net.wifi.DPP_EVENT";
  public static final int ERROR = 0;
  @Deprecated
  public static final int ERROR_AUTHENTICATING = 1;
  @Deprecated
  public static final int ERROR_AUTH_FAILURE_EAP_FAILURE = 3;
  @Deprecated
  public static final int ERROR_AUTH_FAILURE_NONE = 0;
  @Deprecated
  public static final int ERROR_AUTH_FAILURE_TIMEOUT = 1;
  @Deprecated
  public static final int ERROR_AUTH_FAILURE_WRONG_PSWD = 2;
  public static final String EXTRA_ANQP_ELEMENT_DATA = "android.net.wifi.extra.ANQP_ELEMENT_DATA";
  @Deprecated
  public static final String EXTRA_BSSID = "bssid";
  public static final String EXTRA_BSSID_LONG = "android.net.wifi.extra.BSSID_LONG";
  @SystemApi
  public static final String EXTRA_CHANGE_REASON = "changeReason";
  public static final String EXTRA_COUNTRY_CODE = "country_code";
  public static final String EXTRA_DELAY = "android.net.wifi.extra.DELAY";
  public static final String EXTRA_DPP_EVENT_DATA = "dppEventData";
  public static final String EXTRA_DPP_EVENT_TYPE = "dppEventType";
  public static final String EXTRA_ESS = "android.net.wifi.extra.ESS";
  public static final String EXTRA_FILENAME = "android.net.wifi.extra.FILENAME";
  public static final String EXTRA_ICON = "android.net.wifi.extra.ICON";
  public static final String EXTRA_LINK_PROPERTIES = "linkProperties";
  @SystemApi
  public static final String EXTRA_MULTIPLE_NETWORKS_CHANGED = "multipleChanges";
  public static final String EXTRA_NETWORK_CAPABILITIES = "networkCapabilities";
  public static final String EXTRA_NETWORK_INFO = "networkInfo";
  public static final String EXTRA_NEW_RSSI = "newRssi";
  @Deprecated
  public static final String EXTRA_NEW_STATE = "newState";
  @SystemApi
  public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = "previous_wifi_state";
  public static final String EXTRA_PREVIOUS_WIFI_STATE = "previous_wifi_state";
  public static final String EXTRA_RESULTS_UPDATED = "resultsUpdated";
  public static final String EXTRA_SCAN_AVAILABLE = "scan_enabled";
  public static final String EXTRA_SUBSCRIPTION_REMEDIATION_METHOD = "android.net.wifi.extra.SUBSCRIPTION_REMEDIATION_METHOD";
  @Deprecated
  public static final String EXTRA_SUPPLICANT_CONNECTED = "connected";
  @Deprecated
  public static final String EXTRA_SUPPLICANT_ERROR = "supplicantError";
  @Deprecated
  public static final String EXTRA_SUPPLICANT_ERROR_REASON = "supplicantErrorReason";
  public static final String EXTRA_URL = "android.net.wifi.extra.URL";
  public static final String EXTRA_WIFI_AP_FAILURE_REASON = "wifi_ap_error_code";
  public static final String EXTRA_WIFI_AP_INTERFACE_NAME = "wifi_ap_interface_name";
  public static final String EXTRA_WIFI_AP_MODE = "wifi_ap_mode";
  @SystemApi
  public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
  public static final String EXTRA_WIFI_AP_USER_LIST = "user_list";
  @SystemApi
  public static final String EXTRA_WIFI_CONFIGURATION = "wifiConfiguration";
  @SystemApi
  public static final String EXTRA_WIFI_CREDENTIAL_EVENT_TYPE = "et";
  @SystemApi
  public static final String EXTRA_WIFI_CREDENTIAL_SSID = "ssid";
  public static final String EXTRA_WIFI_DATA_STALL_REASON = "data_stall_reasoncode";
  @Deprecated
  public static final String EXTRA_WIFI_INFO = "wifiInfo";
  public static final String EXTRA_WIFI_STATE = "wifi_state";
  public static final int FORGET_NETWORK = 151556;
  public static final int FORGET_NETWORK_FAILED = 151557;
  public static final int FORGET_NETWORK_SUCCEEDED = 151558;
  public static final int HOTSPOT_FAILED = 2;
  public static final int HOTSPOT_OBSERVER_REGISTERED = 3;
  public static final int HOTSPOT_STARTED = 0;
  public static final int HOTSPOT_STOPPED = 1;
  public static final int IFACE_IP_MODE_CONFIGURATION_ERROR = 0;
  public static final int IFACE_IP_MODE_LOCAL_ONLY = 2;
  public static final int IFACE_IP_MODE_TETHERED = 1;
  public static final int IFACE_IP_MODE_UNSPECIFIED = -1;
  public static final int INVALID_ARGS = 8;
  private static final int INVALID_KEY = 0;
  public static final int IN_PROGRESS = 1;
  public static final String LINK_CONFIGURATION_CHANGED_ACTION = "android.net.wifi.LINK_CONFIGURATION_CHANGED";
  private static final int MAX_ACTIVE_LOCKS = 50;
  private static final int MAX_RSSI = -55;
  private static final int MIN_RSSI = -100;
  public static final String NETWORK_IDS_CHANGED_ACTION = "android.net.wifi.NETWORK_IDS_CHANGED";
  public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
  public static final int NOT_AUTHORIZED = 9;
  public static final String RSSI_CHANGED_ACTION = "android.net.wifi.RSSI_CHANGED";
  public static final int RSSI_LEVELS = 5;
  public static final int RSSI_PKTCNT_FETCH = 151572;
  public static final int RSSI_PKTCNT_FETCH_FAILED = 151574;
  public static final int RSSI_PKTCNT_FETCH_SUCCEEDED = 151573;
  public static final int SAP_START_FAILURE_GENERAL = 0;
  public static final int SAP_START_FAILURE_NO_CHANNEL = 1;
  public static final int SAVE_NETWORK = 151559;
  public static final int SAVE_NETWORK_FAILED = 151560;
  public static final int SAVE_NETWORK_SUCCEEDED = 151561;
  public static final String SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.SCAN_RESULTS";
  public static final int START_WPS = 151562;
  public static final int START_WPS_SUCCEEDED = 151563;
  @Deprecated
  public static final String SUPPLICANT_CONNECTION_CHANGE_ACTION = "android.net.wifi.supplicant.CONNECTION_CHANGE";
  @Deprecated
  public static final String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";
  private static final String TAG = "WifiManager";
  @SystemApi
  public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
  @SystemApi
  public static final int WIFI_AP_STATE_DISABLED = 11;
  @SystemApi
  public static final int WIFI_AP_STATE_DISABLING = 10;
  @SystemApi
  public static final int WIFI_AP_STATE_ENABLED = 13;
  @SystemApi
  public static final int WIFI_AP_STATE_ENABLING = 12;
  @SystemApi
  public static final int WIFI_AP_STATE_FAILED = 14;
  public static final String WIFI_AP_UPDATE_REQUEST_ACTION = "android.net.wifi.WIFI_AP_UPDATE_REQUEST_ACTION";
  public static final String WIFI_AP_USER_UPDATE_ACTION = "android.net.wifi.WIFI_AP_USER_UPDATE_ACTION";
  public static final String WIFI_COUNTRY_CODE_CHANGED_ACTION = "android.net.wifi.COUNTRY_CODE_CHANGED";
  @SystemApi
  public static final String WIFI_CREDENTIAL_CHANGED_ACTION = "android.net.wifi.WIFI_CREDENTIAL_CHANGED";
  @SystemApi
  public static final int WIFI_CREDENTIAL_FORGOT = 1;
  @SystemApi
  public static final int WIFI_CREDENTIAL_SAVED = 0;
  public static final String WIFI_DATA_STALL = "com.qualcomm.qti.net.wifi.WIFI_DATA_STALL";
  public static final int WIFI_FEATURE_ADDITIONAL_STA = 2048;
  public static final int WIFI_FEATURE_AP_STA = 32768;
  public static final int WIFI_FEATURE_AWARE = 64;
  public static final int WIFI_FEATURE_BATCH_SCAN = 512;
  public static final int WIFI_FEATURE_CONFIG_NDO = 2097152;
  public static final int WIFI_FEATURE_CONTROL_ROAMING = 8388608;
  public static final int WIFI_FEATURE_D2AP_RTT = 256;
  public static final int WIFI_FEATURE_D2D_RTT = 128;
  public static final int WIFI_FEATURE_EPR = 16384;
  public static final int WIFI_FEATURE_HAL_EPNO = 262144;
  public static final int WIFI_FEATURE_IE_WHITELIST = 16777216;
  public static final int WIFI_FEATURE_INFRA = 1;
  public static final int WIFI_FEATURE_INFRA_5G = 2;
  public static final int WIFI_FEATURE_LINK_LAYER_STATS = 65536;
  public static final int WIFI_FEATURE_LOGGER = 131072;
  public static final int WIFI_FEATURE_MKEEP_ALIVE = 1048576;
  public static final int WIFI_FEATURE_MOBILE_HOTSPOT = 16;
  public static final int WIFI_FEATURE_P2P = 8;
  public static final int WIFI_FEATURE_PASSPOINT = 4;
  public static final int WIFI_FEATURE_PNO = 1024;
  public static final int WIFI_FEATURE_RSSI_MONITOR = 524288;
  public static final int WIFI_FEATURE_SCANNER = 32;
  public static final int WIFI_FEATURE_SCAN_RAND = 33554432;
  public static final int WIFI_FEATURE_TDLS = 4096;
  public static final int WIFI_FEATURE_TDLS_OFFCHANNEL = 8192;
  public static final int WIFI_FEATURE_TRANSMIT_POWER = 4194304;
  public static final int WIFI_FEATURE_TX_POWER_LIMIT = 67108864;
  public static final int WIFI_FREQUENCY_BAND_2GHZ = 2;
  public static final int WIFI_FREQUENCY_BAND_5GHZ = 1;
  public static final int WIFI_FREQUENCY_BAND_AUTO = 0;
  public static final int WIFI_MODE_FULL = 1;
  public static final int WIFI_MODE_FULL_HIGH_PERF = 3;
  public static final int WIFI_MODE_NO_LOCKS_HELD = 0;
  public static final int WIFI_MODE_SCAN_ONLY = 2;
  public static final String WIFI_SCAN_AVAILABLE = "wifi_scan_available";
  public static final String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
  public static final int WIFI_STATE_DISABLED = 1;
  public static final int WIFI_STATE_DISABLING = 0;
  public static final int WIFI_STATE_ENABLED = 3;
  public static final int WIFI_STATE_ENABLING = 2;
  public static final int WIFI_STATE_UNKNOWN = 4;
  public static final int WPS_AUTH_FAILURE = 6;
  public static final int WPS_COMPLETED = 151565;
  public static final int WPS_FAILED = 151564;
  public static final int WPS_OVERLAP_ERROR = 3;
  public static final int WPS_TIMED_OUT = 7;
  public static final int WPS_TKIP_ONLY_PROHIBITED = 5;
  public static final int WPS_WEP_PROHIBITED = 4;
  private static final Object sServiceHandlerDispatchLock = new Object();
  private int mActiveLockCount;
  private AsyncChannel mAsyncChannel;
  private CountDownLatch mConnected;
  private Context mContext;
  @GuardedBy("mLock")
  private LocalOnlyHotspotCallbackProxy mLOHSCallbackProxy;
  @GuardedBy("mLock")
  private LocalOnlyHotspotObserverProxy mLOHSObserverProxy;
  private int mListenerKey = 1;
  private final SparseArray mListenerMap = new SparseArray();
  private final Object mListenerMapLock = new Object();
  private final Object mLock = new Object();
  private Looper mLooper;
  IWifiManager mService;
  private final int mTargetSdkVersion;
  
  public WifiManager(Context paramContext, IWifiManager paramIWifiManager, Looper paramLooper)
  {
    mContext = paramContext;
    mService = paramIWifiManager;
    mLooper = paramLooper;
    mTargetSdkVersion = getApplicationInfotargetSdkVersion;
  }
  
  private int addOrUpdateNetwork(WifiConfiguration paramWifiConfiguration)
  {
    try
    {
      int i = mService.addOrUpdateNetwork(paramWifiConfiguration, mContext.getOpPackageName());
      return i;
    }
    catch (RemoteException paramWifiConfiguration)
    {
      throw paramWifiConfiguration.rethrowFromSystemServer();
    }
  }
  
  public static int calculateSignalLevel(int paramInt1, int paramInt2)
  {
    if (5 == paramInt2)
    {
      if (paramInt1 < -88) {
        return 0;
      }
      if (paramInt1 < -77) {
        return 1;
      }
      if (paramInt1 < -70) {
        return 2;
      }
      if (paramInt1 < -64) {
        return 3;
      }
      return 4;
    }
    if (paramInt1 <= -100) {
      return 0;
    }
    if (paramInt1 >= -55) {
      return paramInt2 - 1;
    }
    float f = paramInt2 - 1;
    return (int)((paramInt1 + 100) * f / 45.0F);
  }
  
  public static int compareSignalLevel(int paramInt1, int paramInt2)
  {
    return paramInt1 - paramInt2;
  }
  
  private AsyncChannel getChannel()
  {
    try
    {
      if (mAsyncChannel == null)
      {
        Messenger localMessenger = getWifiServiceMessenger();
        if (localMessenger != null)
        {
          Object localObject3 = new com/android/internal/util/AsyncChannel;
          ((AsyncChannel)localObject3).<init>();
          mAsyncChannel = ((AsyncChannel)localObject3);
          localObject3 = new java/util/concurrent/CountDownLatch;
          ((CountDownLatch)localObject3).<init>(1);
          mConnected = ((CountDownLatch)localObject3);
          localObject3 = new android/net/wifi/WifiManager$ServiceHandler;
          ((ServiceHandler)localObject3).<init>(this, mLooper);
          mAsyncChannel.connect(mContext, (Handler)localObject3, localMessenger);
          try
          {
            mConnected.await();
          }
          catch (InterruptedException localInterruptedException)
          {
            Log.e("WifiManager", "interrupted wait at init");
          }
        }
        else
        {
          localObject1 = new java/lang/IllegalStateException;
          ((IllegalStateException)localObject1).<init>("getWifiServiceMessenger() returned null!  This is invalid.");
          throw ((Throwable)localObject1);
        }
      }
      Object localObject1 = mAsyncChannel;
      return localObject1;
    }
    finally {}
  }
  
  private int getSupportedFeatures()
  {
    try
    {
      int i = mService.getSupportedFeatures();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private boolean isFeatureSupported(int paramInt)
  {
    boolean bool;
    if ((getSupportedFeatures() & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private int putListener(Object paramObject)
  {
    if (paramObject == null) {
      return 0;
    }
    synchronized (mListenerMapLock)
    {
      int i;
      do
      {
        i = mListenerKey;
        mListenerKey = (i + 1);
      } while (i == 0);
      mListenerMap.put(i, paramObject);
      return i;
    }
  }
  
  private Object removeListener(int paramInt)
  {
    if (paramInt == 0) {
      return null;
    }
    synchronized (mListenerMapLock)
    {
      Object localObject2 = mListenerMap.get(paramInt);
      mListenerMap.remove(paramInt);
      return localObject2;
    }
  }
  
  private void stopLocalOnlyHotspot()
  {
    synchronized (mLock)
    {
      if (mLOHSCallbackProxy == null) {
        return;
      }
      mLOHSCallbackProxy = null;
      try
      {
        mService.stopLocalOnlyHotspot();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public int addNetwork(WifiConfiguration paramWifiConfiguration)
  {
    if (paramWifiConfiguration == null) {
      return -1;
    }
    networkId = -1;
    return addOrUpdateNetwork(paramWifiConfiguration);
  }
  
  public boolean addOrUpdateApLocalAllowList(WifiConfiguration paramWifiConfiguration)
  {
    try
    {
      boolean bool = mService.addOrUpdateApLocalAllowList(paramWifiConfiguration);
      return bool;
    }
    catch (RemoteException paramWifiConfiguration) {}
    return false;
  }
  
  public void addOrUpdatePasspointConfiguration(PasspointConfiguration paramPasspointConfiguration)
  {
    try
    {
      if (mService.addOrUpdatePasspointConfiguration(paramPasspointConfiguration, mContext.getOpPackageName())) {
        return;
      }
      paramPasspointConfiguration = new java/lang/IllegalArgumentException;
      paramPasspointConfiguration.<init>();
      throw paramPasspointConfiguration;
    }
    catch (RemoteException paramPasspointConfiguration)
    {
      throw paramPasspointConfiguration.rethrowFromSystemServer();
    }
  }
  
  public void cancelLocalOnlyHotspotRequest()
  {
    synchronized (mLock)
    {
      stopLocalOnlyHotspot();
      return;
    }
  }
  
  public void cancelWps(WpsCallback paramWpsCallback)
  {
    if (paramWpsCallback != null) {
      paramWpsCallback.onFailed(0);
    }
  }
  
  public void connect(int paramInt, ActionListener paramActionListener)
  {
    if (paramInt >= 0)
    {
      getChannel().sendMessage(151553, paramInt, putListener(paramActionListener));
      return;
    }
    throw new IllegalArgumentException("Network id cannot be negative");
  }
  
  @SystemApi
  public void connect(WifiConfiguration paramWifiConfiguration, ActionListener paramActionListener)
  {
    if (paramWifiConfiguration != null)
    {
      getChannel().sendMessage(151553, -1, putListener(paramActionListener), paramWifiConfiguration);
      return;
    }
    throw new IllegalArgumentException("config cannot be null");
  }
  
  public MulticastLock createMulticastLock(String paramString)
  {
    return new MulticastLock(paramString, null);
  }
  
  public WifiLock createWifiLock(int paramInt, String paramString)
  {
    return new WifiLock(paramInt, paramString, null);
  }
  
  public WifiLock createWifiLock(String paramString)
  {
    return new WifiLock(1, paramString, null);
  }
  
  public void deauthenticateNetwork(long paramLong, boolean paramBoolean)
  {
    try
    {
      mService.deauthenticateNetwork(paramLong, paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void disable(int paramInt, ActionListener paramActionListener)
  {
    if (paramInt >= 0)
    {
      getChannel().sendMessage(151569, paramInt, putListener(paramActionListener));
      return;
    }
    throw new IllegalArgumentException("Network id cannot be negative");
  }
  
  public void disableEphemeralNetwork(String paramString)
  {
    if (paramString != null) {
      try
      {
        mService.disableEphemeralNetwork(paramString, mContext.getOpPackageName());
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("SSID cannot be null");
  }
  
  public boolean disableNetwork(int paramInt)
  {
    try
    {
      boolean bool = mService.disableNetwork(paramInt, mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean disconnect()
  {
    try
    {
      mService.disconnect(mContext.getOpPackageName());
      return true;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean disconnectClient(WifiConfiguration paramWifiConfiguration)
  {
    try
    {
      boolean bool = mService.disconnectClient(paramWifiConfiguration);
      return bool;
    }
    catch (RemoteException paramWifiConfiguration) {}
    return false;
  }
  
  public int dppAddBootstrapQrCode(String paramString)
  {
    try
    {
      int i = mService.dppAddBootstrapQrCode(paramString);
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int dppBootstrapGenerate(WifiDppConfig paramWifiDppConfig)
  {
    try
    {
      int i = mService.dppBootstrapGenerate(paramWifiDppConfig);
      return i;
    }
    catch (RemoteException paramWifiDppConfig)
    {
      throw paramWifiDppConfig.rethrowFromSystemServer();
    }
  }
  
  public int dppBootstrapRemove(int paramInt)
  {
    try
    {
      paramInt = mService.dppBootstrapRemove(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int dppConfiguratorAdd(String paramString1, String paramString2, int paramInt)
  {
    try
    {
      paramInt = mService.dppConfiguratorAdd(paramString1, paramString2, paramInt);
      return paramInt;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public String dppConfiguratorGetKey(int paramInt)
  {
    try
    {
      String str = mService.dppConfiguratorGetKey(paramInt);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int dppConfiguratorRemove(int paramInt)
  {
    try
    {
      paramInt = mService.dppConfiguratorRemove(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String dppGetUri(int paramInt)
  {
    try
    {
      String str = mService.dppGetUri(paramInt);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int dppListen(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      paramInt = mService.dppListen(paramString, paramInt, paramBoolean1, paramBoolean2);
      return paramInt;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int dppStartAuth(WifiDppConfig paramWifiDppConfig)
  {
    try
    {
      int i = mService.dppStartAuth(paramWifiDppConfig);
      return i;
    }
    catch (RemoteException paramWifiDppConfig)
    {
      throw paramWifiDppConfig.rethrowFromSystemServer();
    }
  }
  
  public void dppStopListen()
  {
    try
    {
      mService.dppStopListen();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean enableNetwork(int paramInt, boolean paramBoolean)
  {
    int i;
    if ((paramBoolean) && (mTargetSdkVersion < 21)) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      NetworkRequest localNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addCapability(15).addTransportType(1).build();
      NetworkPinner.pin(mContext, localNetworkRequest);
    }
    try
    {
      paramBoolean = mService.enableNetwork(paramInt, paramBoolean, mContext.getOpPackageName());
      if ((i != 0) && (!paramBoolean)) {
        NetworkPinner.unpin();
      }
      return paramBoolean;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void enableVerboseLogging(int paramInt)
  {
    try
    {
      mService.enableVerboseLogging(paramInt);
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("enableVerboseLogging ");
      localStringBuilder.append(localException.toString());
      Log.e("WifiManager", localStringBuilder.toString());
    }
  }
  
  public void enableWifiConnectivityManager(boolean paramBoolean)
  {
    try
    {
      mService.enableWifiConnectivityManager(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void enableWifiCoverageExtendFeature(boolean paramBoolean)
  {
    try
    {
      mService.enableWifiCoverageExtendFeature(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void enable_hotspotWhiteList(boolean paramBoolean)
  {
    try
    {
      mService.enable_hotspotWhiteList(paramBoolean);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void factoryReset()
  {
    try
    {
      mService.factoryReset(mContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mAsyncChannel != null) {
        mAsyncChannel.disconnect();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public void forget(int paramInt, ActionListener paramActionListener)
  {
    if (paramInt >= 0)
    {
      getChannel().sendMessage(151556, paramInt, putListener(paramActionListener));
      return;
    }
    throw new IllegalArgumentException("Network id cannot be negative");
  }
  
  public List<WifiConfiguration> getAllMatchingWifiConfigs(ScanResult paramScanResult)
  {
    try
    {
      paramScanResult = mService.getAllMatchingWifiConfigs(paramScanResult);
      return paramScanResult;
    }
    catch (RemoteException paramScanResult)
    {
      throw paramScanResult.rethrowFromSystemServer();
    }
  }
  
  public List<WifiConfiguration> getApAllowList()
  {
    try
    {
      List localList = mService.getApAllowList();
      return localList;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public String getCapabilities(String paramString)
  {
    try
    {
      paramString = mService.getCapabilities(paramString);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<WifiConfiguration> getConfiguredNetworks()
  {
    try
    {
      Object localObject = mService.getConfiguredNetworks();
      if (localObject == null) {
        return Collections.emptyList();
      }
      localObject = ((ParceledListSlice)localObject).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public WifiInfo getConnectionInfo()
  {
    try
    {
      WifiInfo localWifiInfo = mService.getConnectionInfo(mContext.getOpPackageName());
      return localWifiInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public WifiActivityEnergyInfo getControllerActivityEnergyInfo(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 493	android/net/wifi/WifiManager:mService	Landroid/net/wifi/IWifiManager;
    //   4: ifnonnull +5 -> 9
    //   7: aconst_null
    //   8: areturn
    //   9: aload_0
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield 493	android/net/wifi/WifiManager:mService	Landroid/net/wifi/IWifiManager;
    //   15: invokeinterface 847 1 0
    //   20: astore_2
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_2
    //   24: areturn
    //   25: astore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_2
    //   29: athrow
    //   30: astore_2
    //   31: aload_2
    //   32: invokevirtual 554	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   35: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	36	0	this	WifiManager
    //   0	36	1	paramInt	int
    //   20	4	2	localWifiActivityEnergyInfo	WifiActivityEnergyInfo
    //   25	4	2	localObject	Object
    //   30	2	2	localRemoteException	RemoteException
    // Exception table:
    //   from	to	target	type
    //   11	23	25	finally
    //   26	28	25	finally
    //   9	11	30	android/os/RemoteException
    //   28	30	30	android/os/RemoteException
  }
  
  public String getCountryCode()
  {
    try
    {
      String str = mService.getCountryCode();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Network getCurrentNetwork()
  {
    try
    {
      Network localNetwork = mService.getCurrentNetwork();
      return localNetwork;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getCurrentNetworkWpsNfcConfigurationToken()
  {
    return null;
  }
  
  public DhcpInfo getDhcpInfo()
  {
    try
    {
      DhcpInfo localDhcpInfo = mService.getDhcpInfo();
      return localDhcpInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean getEnableAutoJoinWhenAssociated()
  {
    return false;
  }
  
  public List<OsuProvider> getMatchingOsuProviders(ScanResult paramScanResult)
  {
    try
    {
      paramScanResult = mService.getMatchingOsuProviders(paramScanResult);
      return paramScanResult;
    }
    catch (RemoteException paramScanResult)
    {
      throw paramScanResult.rethrowFromSystemServer();
    }
  }
  
  public WifiConfiguration getMatchingWifiConfig(ScanResult paramScanResult)
  {
    try
    {
      paramScanResult = mService.getMatchingWifiConfig(paramScanResult);
      return paramScanResult;
    }
    catch (RemoteException paramScanResult)
    {
      throw paramScanResult.rethrowFromSystemServer();
    }
  }
  
  public List<PasspointConfiguration> getPasspointConfigurations()
  {
    try
    {
      List localList = mService.getPasspointConfigurations();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public List<WifiConfiguration> getPrivilegedConfiguredNetworks()
  {
    try
    {
      Object localObject = mService.getPrivilegedConfiguredNetworks();
      if (localObject == null) {
        return Collections.emptyList();
      }
      localObject = ((ParceledListSlice)localObject).getList();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<ScanResult> getScanResults()
  {
    SeempLog.record(55);
    try
    {
      List localList = mService.getScanResults(mContext.getOpPackageName());
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void getTxPacketCount(TxPacketCountListener paramTxPacketCountListener)
  {
    getChannel().sendMessage(151572, 0, putListener(paramTxPacketCountListener));
  }
  
  public int getVerboseLoggingLevel()
  {
    try
    {
      int i = mService.getVerboseLoggingLevel();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public WifiConfiguration getWifiApConfiguration()
  {
    try
    {
      WifiConfiguration localWifiConfiguration = mService.getWifiApConfiguration();
      return localWifiConfiguration;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public int getWifiApState()
  {
    try
    {
      int i = mService.getWifiApEnabledState();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Messenger getWifiServiceMessenger()
  {
    try
    {
      Messenger localMessenger = mService.getWifiServiceMessenger(mContext.getOpPackageName());
      return localMessenger;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getWifiState()
  {
    try
    {
      int i = mService.getWifiEnabledState();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean initializeMulticastFiltering()
  {
    try
    {
      mService.initializeMulticastFiltering();
      return true;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean is5GHzBandSupported()
  {
    return isFeatureSupported(2);
  }
  
  public boolean isAdditionalStaSupported()
  {
    return isFeatureSupported(2048);
  }
  
  @SystemApi
  public boolean isDeviceToApRttSupported()
  {
    return isFeatureSupported(256);
  }
  
  @SystemApi
  public boolean isDeviceToDeviceRttSupported()
  {
    return isFeatureSupported(128);
  }
  
  public boolean isDualBandSupported()
  {
    try
    {
      boolean bool = mService.isDualBandSupported();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isDualModeSupported()
  {
    try
    {
      boolean bool = mService.needs5GHzToAnyApBandConversion();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isEnableWhiteList()
  {
    try
    {
      boolean bool = mService.isEnableWhiteList();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isEnhancedPowerReportingSupported()
  {
    return isFeatureSupported(65536);
  }
  
  public boolean isExtendingWifi()
  {
    try
    {
      boolean bool = mService.isExtendingWifi();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isMulticastEnabled()
  {
    try
    {
      boolean bool = mService.isMulticastEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isOffChannelTdlsSupported()
  {
    return isFeatureSupported(8192);
  }
  
  public boolean isP2pSupported()
  {
    return isFeatureSupported(8);
  }
  
  public boolean isPasspointSupported()
  {
    return isFeatureSupported(4);
  }
  
  @SystemApi
  public boolean isPortableHotspotSupported()
  {
    return isFeatureSupported(16);
  }
  
  public boolean isPreferredNetworkOffloadSupported()
  {
    return isFeatureSupported(1024);
  }
  
  public boolean isScanAlwaysAvailable()
  {
    try
    {
      boolean bool = mService.isScanAlwaysAvailable();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isTdlsSupported()
  {
    return isFeatureSupported(4096);
  }
  
  @SystemApi
  public boolean isWifiApEnabled()
  {
    boolean bool;
    if (getWifiApState() == 13) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isWifiAwareSupported()
  {
    return isFeatureSupported(64);
  }
  
  public boolean isWifiCoverageExtendFeatureEnabled()
  {
    try
    {
      boolean bool = mService.isWifiCoverageExtendFeatureEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isWifiEnabled()
  {
    boolean bool;
    if (getWifiState() == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public boolean isWifiScannerSupported()
  {
    return isFeatureSupported(32);
  }
  
  public int matchProviderWithCurrentNetwork(String paramString)
  {
    try
    {
      int i = mService.matchProviderWithCurrentNetwork(paramString);
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean pingSupplicant()
  {
    return isWifiEnabled();
  }
  
  public void queryPasspointIcon(long paramLong, String paramString)
  {
    try
    {
      mService.queryPasspointIcon(paramLong, paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean reassociate()
  {
    try
    {
      mService.reassociate(mContext.getOpPackageName());
      return true;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean reconnect()
  {
    try
    {
      mService.reconnect(mContext.getOpPackageName());
      return true;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void registerSoftApCallback(SoftApCallback paramSoftApCallback, Handler paramHandler)
  {
    if (paramSoftApCallback != null)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("registerSoftApCallback: callback=");
      ((StringBuilder)localObject).append(paramSoftApCallback);
      ((StringBuilder)localObject).append(", handler=");
      ((StringBuilder)localObject).append(paramHandler);
      Log.v("WifiManager", ((StringBuilder)localObject).toString());
      if (paramHandler == null) {
        paramHandler = mContext.getMainLooper();
      } else {
        paramHandler = paramHandler.getLooper();
      }
      Binder localBinder = new Binder();
      try
      {
        localObject = mService;
        SoftApCallbackProxy localSoftApCallbackProxy = new android/net/wifi/WifiManager$SoftApCallbackProxy;
        localSoftApCallbackProxy.<init>(paramHandler, paramSoftApCallback);
        ((IWifiManager)localObject).registerSoftApCallback(localBinder, localSoftApCallbackProxy, paramSoftApCallback.hashCode());
        return;
      }
      catch (RemoteException paramSoftApCallback)
      {
        throw paramSoftApCallback.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("callback cannot be null");
  }
  
  public boolean removeApLocalAllowList(WifiConfiguration paramWifiConfiguration)
  {
    try
    {
      boolean bool = mService.removeApLocalAllowList(paramWifiConfiguration);
      return bool;
    }
    catch (RemoteException paramWifiConfiguration) {}
    return false;
  }
  
  public boolean removeNetwork(int paramInt)
  {
    try
    {
      boolean bool = mService.removeNetwork(paramInt, mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void removePasspointConfiguration(String paramString)
  {
    try
    {
      if (mService.removePasspointConfiguration(paramString, mContext.getOpPackageName())) {
        return;
      }
      paramString = new java/lang/IllegalArgumentException;
      paramString.<init>();
      throw paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void restoreBackupData(byte[] paramArrayOfByte)
  {
    try
    {
      mService.restoreBackupData(paramArrayOfByte);
      return;
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void restoreSupplicantBackupData(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    try
    {
      mService.restoreSupplicantBackupData(paramArrayOfByte1, paramArrayOfByte2);
      return;
    }
    catch (RemoteException paramArrayOfByte1)
    {
      throw paramArrayOfByte1.rethrowFromSystemServer();
    }
  }
  
  public byte[] retrieveBackupData()
  {
    try
    {
      byte[] arrayOfByte = mService.retrieveBackupData();
      return arrayOfByte;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void save(WifiConfiguration paramWifiConfiguration, ActionListener paramActionListener)
  {
    if (paramWifiConfiguration != null)
    {
      getChannel().sendMessage(151559, 0, putListener(paramActionListener), paramWifiConfiguration);
      return;
    }
    throw new IllegalArgumentException("config cannot be null");
  }
  
  @Deprecated
  public boolean saveConfiguration()
  {
    return true;
  }
  
  public void setCountryCode(String paramString)
  {
    try
    {
      mService.setCountryCode(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean setEnableAutoJoinWhenAssociated(boolean paramBoolean)
  {
    return false;
  }
  
  public void setTdlsEnabled(InetAddress paramInetAddress, boolean paramBoolean)
  {
    try
    {
      mService.enableTdls(paramInetAddress.getHostAddress(), paramBoolean);
      return;
    }
    catch (RemoteException paramInetAddress)
    {
      throw paramInetAddress.rethrowFromSystemServer();
    }
  }
  
  public void setTdlsEnabledWithMacAddress(String paramString, boolean paramBoolean)
  {
    try
    {
      mService.enableTdlsWithMacAddress(paramString, paramBoolean);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean setWifiApConfiguration(WifiConfiguration paramWifiConfiguration)
  {
    try
    {
      boolean bool = mService.setWifiApConfiguration(paramWifiConfiguration, mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException paramWifiConfiguration)
    {
      throw paramWifiConfiguration.rethrowFromSystemServer();
    }
  }
  
  public boolean setWifiEnabled(boolean paramBoolean)
  {
    try
    {
      paramBoolean = mService.setWifiEnabled(mContext.getOpPackageName(), paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void startLocalOnlyHotspot(LocalOnlyHotspotCallback paramLocalOnlyHotspotCallback, Handler paramHandler)
  {
    Object localObject = mLock;
    if (paramHandler == null) {
      try
      {
        paramHandler = mContext.getMainLooper();
      }
      finally
      {
        break label116;
      }
    } else {
      paramHandler = paramHandler.getLooper();
    }
    LocalOnlyHotspotCallbackProxy localLocalOnlyHotspotCallbackProxy = new android/net/wifi/WifiManager$LocalOnlyHotspotCallbackProxy;
    localLocalOnlyHotspotCallbackProxy.<init>(this, paramHandler, paramLocalOnlyHotspotCallback);
    try
    {
      String str = mContext.getOpPackageName();
      paramLocalOnlyHotspotCallback = mService;
      Messenger localMessenger = localLocalOnlyHotspotCallbackProxy.getMessenger();
      paramHandler = new android/os/Binder;
      paramHandler.<init>();
      int i = paramLocalOnlyHotspotCallback.startLocalOnlyHotspot(localMessenger, paramHandler, str);
      if (i != 0)
      {
        localLocalOnlyHotspotCallbackProxy.notifyFailed(i);
        return;
      }
      mLOHSCallbackProxy = localLocalOnlyHotspotCallbackProxy;
      return;
    }
    catch (RemoteException paramLocalOnlyHotspotCallback)
    {
      throw paramLocalOnlyHotspotCallback.rethrowFromSystemServer();
    }
    label116:
    throw paramLocalOnlyHotspotCallback;
  }
  
  @Deprecated
  public boolean startScan()
  {
    return startScan(null);
  }
  
  @SystemApi
  public boolean startScan(WorkSource paramWorkSource)
  {
    try
    {
      paramWorkSource = mContext.getOpPackageName();
      boolean bool = mService.startScan(paramWorkSource);
      return bool;
    }
    catch (RemoteException paramWorkSource)
    {
      throw paramWorkSource.rethrowFromSystemServer();
    }
  }
  
  public boolean startSoftAp(WifiConfiguration paramWifiConfiguration)
  {
    try
    {
      boolean bool = mService.startSoftAp(paramWifiConfiguration);
      return bool;
    }
    catch (RemoteException paramWifiConfiguration)
    {
      throw paramWifiConfiguration.rethrowFromSystemServer();
    }
  }
  
  public void startSubscriptionProvisioning(OsuProvider paramOsuProvider, ProvisioningCallback paramProvisioningCallback, Handler paramHandler)
  {
    if (paramHandler == null) {
      paramHandler = Looper.getMainLooper();
    } else {
      paramHandler = paramHandler.getLooper();
    }
    try
    {
      IWifiManager localIWifiManager = mService;
      ProvisioningCallbackProxy localProvisioningCallbackProxy = new android/net/wifi/WifiManager$ProvisioningCallbackProxy;
      localProvisioningCallbackProxy.<init>(paramHandler, paramProvisioningCallback);
      localIWifiManager.startSubscriptionProvisioning(paramOsuProvider, localProvisioningCallbackProxy);
      return;
    }
    catch (RemoteException paramOsuProvider)
    {
      throw paramOsuProvider.rethrowFromSystemServer();
    }
  }
  
  public void startWps(WpsInfo paramWpsInfo, WpsCallback paramWpsCallback)
  {
    if (paramWpsCallback != null) {
      paramWpsCallback.onFailed(0);
    }
  }
  
  public boolean stopSoftAp()
  {
    try
    {
      boolean bool = mService.stopSoftAp();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void unregisterLocalOnlyHotspotObserver()
  {
    synchronized (mLock)
    {
      if (mLOHSObserverProxy == null) {
        return;
      }
      mLOHSObserverProxy = null;
      try
      {
        mService.stopWatchLocalOnlyHotspot();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void unregisterSoftApCallback(SoftApCallback paramSoftApCallback)
  {
    if (paramSoftApCallback != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unregisterSoftApCallback: callback=");
      localStringBuilder.append(paramSoftApCallback);
      Log.v("WifiManager", localStringBuilder.toString());
      try
      {
        mService.unregisterSoftApCallback(paramSoftApCallback.hashCode());
        return;
      }
      catch (RemoteException paramSoftApCallback)
      {
        throw paramSoftApCallback.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("callback cannot be null");
  }
  
  public void updateInterfaceIpState(String paramString, int paramInt)
  {
    try
    {
      mService.updateInterfaceIpState(paramString, paramInt);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int updateNetwork(WifiConfiguration paramWifiConfiguration)
  {
    if ((paramWifiConfiguration != null) && (networkId >= 0)) {
      return addOrUpdateNetwork(paramWifiConfiguration);
    }
    return -1;
  }
  
  public void watchLocalOnlyHotspot(LocalOnlyHotspotObserver paramLocalOnlyHotspotObserver, Handler paramHandler)
  {
    Object localObject1 = mLock;
    if (paramHandler == null) {
      try
      {
        paramHandler = mContext.getMainLooper();
      }
      finally
      {
        break label102;
      }
    } else {
      paramHandler = paramHandler.getLooper();
    }
    Object localObject2 = new android/net/wifi/WifiManager$LocalOnlyHotspotObserverProxy;
    ((LocalOnlyHotspotObserverProxy)localObject2).<init>(this, paramHandler, paramLocalOnlyHotspotObserver);
    mLOHSObserverProxy = ((LocalOnlyHotspotObserverProxy)localObject2);
    try
    {
      localObject2 = mService;
      paramHandler = mLOHSObserverProxy.getMessenger();
      paramLocalOnlyHotspotObserver = new android/os/Binder;
      paramLocalOnlyHotspotObserver.<init>();
      ((IWifiManager)localObject2).startWatchLocalOnlyHotspot(paramHandler, paramLocalOnlyHotspotObserver);
      mLOHSObserverProxy.registered();
      return;
    }
    catch (RemoteException paramLocalOnlyHotspotObserver)
    {
      mLOHSObserverProxy = null;
      throw paramLocalOnlyHotspotObserver.rethrowFromSystemServer();
    }
    label102:
    throw paramLocalOnlyHotspotObserver;
  }
  
  @SystemApi
  public static abstract interface ActionListener
  {
    public abstract void onFailure(int paramInt);
    
    public abstract void onSuccess();
  }
  
  public static class LocalOnlyHotspotCallback
  {
    public static final int ERROR_GENERIC = 2;
    public static final int ERROR_INCOMPATIBLE_MODE = 3;
    public static final int ERROR_NO_CHANNEL = 1;
    public static final int ERROR_TETHERING_DISALLOWED = 4;
    public static final int REQUEST_REGISTERED = 0;
    
    public LocalOnlyHotspotCallback() {}
    
    public void onFailed(int paramInt) {}
    
    public void onStarted(WifiManager.LocalOnlyHotspotReservation paramLocalOnlyHotspotReservation) {}
    
    public void onStopped() {}
  }
  
  private static class LocalOnlyHotspotCallbackProxy
  {
    private final Handler mHandler;
    private final Looper mLooper;
    private final Messenger mMessenger;
    private final WeakReference<WifiManager> mWifiManager;
    
    LocalOnlyHotspotCallbackProxy(WifiManager paramWifiManager, Looper paramLooper, final WifiManager.LocalOnlyHotspotCallback paramLocalOnlyHotspotCallback)
    {
      mWifiManager = new WeakReference(paramWifiManager);
      mLooper = paramLooper;
      mHandler = new Handler(paramLooper)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          Object localObject = new StringBuilder();
          ((StringBuilder)localObject).append("LocalOnlyHotspotCallbackProxy: handle message what: ");
          ((StringBuilder)localObject).append(what);
          ((StringBuilder)localObject).append(" msg: ");
          ((StringBuilder)localObject).append(paramAnonymousMessage);
          Log.d("WifiManager", ((StringBuilder)localObject).toString());
          localObject = (WifiManager)mWifiManager.get();
          if (localObject == null)
          {
            Log.w("WifiManager", "LocalOnlyHotspotCallbackProxy: handle message post GC");
            return;
          }
          switch (what)
          {
          default: 
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("LocalOnlyHotspotCallbackProxy unhandled message.  type: ");
            ((StringBuilder)localObject).append(what);
            Log.e("WifiManager", ((StringBuilder)localObject).toString());
            break;
          case 2: 
            int i = arg1;
            paramAnonymousMessage = new StringBuilder();
            paramAnonymousMessage.append("LocalOnlyHotspotCallbackProxy: failed to start.  reason: ");
            paramAnonymousMessage.append(i);
            Log.w("WifiManager", paramAnonymousMessage.toString());
            paramLocalOnlyHotspotCallback.onFailed(i);
            Log.w("WifiManager", "done with the callback...");
            break;
          case 1: 
            Log.w("WifiManager", "LocalOnlyHotspotCallbackProxy: hotspot stopped");
            paramLocalOnlyHotspotCallback.onStopped();
            break;
          case 0: 
            WifiConfiguration localWifiConfiguration = (WifiConfiguration)obj;
            if (localWifiConfiguration == null)
            {
              Log.e("WifiManager", "LocalOnlyHotspotCallbackProxy: config cannot be null.");
              paramLocalOnlyHotspotCallback.onFailed(2);
              return;
            }
            paramAnonymousMessage = paramLocalOnlyHotspotCallback;
            Objects.requireNonNull(localObject);
            paramAnonymousMessage.onStarted(new WifiManager.LocalOnlyHotspotReservation((WifiManager)localObject, localWifiConfiguration));
          }
        }
      };
      mMessenger = new Messenger(mHandler);
    }
    
    public Messenger getMessenger()
    {
      return mMessenger;
    }
    
    public void notifyFailed(int paramInt)
      throws RemoteException
    {
      Message localMessage = Message.obtain();
      what = 2;
      arg1 = paramInt;
      mMessenger.send(localMessage);
    }
  }
  
  public static class LocalOnlyHotspotObserver
  {
    public LocalOnlyHotspotObserver() {}
    
    public void onRegistered(WifiManager.LocalOnlyHotspotSubscription paramLocalOnlyHotspotSubscription) {}
    
    public void onStarted(WifiConfiguration paramWifiConfiguration) {}
    
    public void onStopped() {}
  }
  
  private static class LocalOnlyHotspotObserverProxy
  {
    private final Handler mHandler;
    private final Looper mLooper;
    private final Messenger mMessenger;
    private final WeakReference<WifiManager> mWifiManager;
    
    LocalOnlyHotspotObserverProxy(WifiManager paramWifiManager, Looper paramLooper, final WifiManager.LocalOnlyHotspotObserver paramLocalOnlyHotspotObserver)
    {
      mWifiManager = new WeakReference(paramWifiManager);
      mLooper = paramLooper;
      mHandler = new Handler(paramLooper)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          Object localObject = new StringBuilder();
          ((StringBuilder)localObject).append("LocalOnlyHotspotObserverProxy: handle message what: ");
          ((StringBuilder)localObject).append(what);
          ((StringBuilder)localObject).append(" msg: ");
          ((StringBuilder)localObject).append(paramAnonymousMessage);
          Log.d("WifiManager", ((StringBuilder)localObject).toString());
          localObject = (WifiManager)mWifiManager.get();
          if (localObject == null)
          {
            Log.w("WifiManager", "LocalOnlyHotspotObserverProxy: handle message post GC");
            return;
          }
          int i = what;
          if (i != 3)
          {
            switch (i)
            {
            default: 
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("LocalOnlyHotspotObserverProxy unhandled message.  type: ");
              ((StringBuilder)localObject).append(what);
              Log.e("WifiManager", ((StringBuilder)localObject).toString());
              break;
            case 1: 
              paramLocalOnlyHotspotObserver.onStopped();
              break;
            case 0: 
              paramAnonymousMessage = (WifiConfiguration)obj;
              if (paramAnonymousMessage == null)
              {
                Log.e("WifiManager", "LocalOnlyHotspotObserverProxy: config cannot be null.");
                return;
              }
              paramLocalOnlyHotspotObserver.onStarted(paramAnonymousMessage);
              break;
            }
          }
          else
          {
            paramAnonymousMessage = paramLocalOnlyHotspotObserver;
            Objects.requireNonNull(localObject);
            paramAnonymousMessage.onRegistered(new WifiManager.LocalOnlyHotspotSubscription((WifiManager)localObject));
          }
        }
      };
      mMessenger = new Messenger(mHandler);
    }
    
    public Messenger getMessenger()
    {
      return mMessenger;
    }
    
    public void registered()
      throws RemoteException
    {
      Message localMessage = Message.obtain();
      what = 3;
      mMessenger.send(localMessage);
    }
  }
  
  public class LocalOnlyHotspotReservation
    implements AutoCloseable
  {
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final WifiConfiguration mConfig;
    
    @VisibleForTesting
    public LocalOnlyHotspotReservation(WifiConfiguration paramWifiConfiguration)
    {
      mConfig = paramWifiConfiguration;
      mCloseGuard.open("close");
    }
    
    public void close()
    {
      try
      {
        WifiManager.this.stopLocalOnlyHotspot();
        mCloseGuard.close();
      }
      catch (Exception localException)
      {
        Log.e("WifiManager", "Failed to stop Local Only Hotspot.");
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        if (mCloseGuard != null) {
          mCloseGuard.warnIfOpen();
        }
        close();
        return;
      }
      finally
      {
        super.finalize();
      }
    }
    
    public WifiConfiguration getWifiConfiguration()
    {
      return mConfig;
    }
  }
  
  public class LocalOnlyHotspotSubscription
    implements AutoCloseable
  {
    private final CloseGuard mCloseGuard = CloseGuard.get();
    
    @VisibleForTesting
    public LocalOnlyHotspotSubscription()
    {
      mCloseGuard.open("close");
    }
    
    public void close()
    {
      try
      {
        unregisterLocalOnlyHotspotObserver();
        mCloseGuard.close();
      }
      catch (Exception localException)
      {
        Log.e("WifiManager", "Failed to unregister LocalOnlyHotspotObserver.");
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      try
      {
        if (mCloseGuard != null) {
          mCloseGuard.warnIfOpen();
        }
        close();
        return;
      }
      finally
      {
        super.finalize();
      }
    }
  }
  
  public class MulticastLock
  {
    private final IBinder mBinder;
    private boolean mHeld;
    private int mRefCount;
    private boolean mRefCounted;
    private String mTag;
    
    private MulticastLock(String paramString)
    {
      mTag = paramString;
      mBinder = new Binder();
      mRefCount = 0;
      mRefCounted = true;
      mHeld = false;
    }
    
    public void acquire()
    {
      synchronized (mBinder)
      {
        if (mRefCounted)
        {
          int i = mRefCount + 1;
          mRefCount = i;
          if (i != 1) {
            break label146;
          }
        }
        else
        {
          boolean bool = mHeld;
          if (bool) {
            break label146;
          }
        }
        try
        {
          mService.acquireMulticastLock(mBinder, mTag);
          synchronized (WifiManager.this)
          {
            if (mActiveLockCount < 50)
            {
              WifiManager.access$708(WifiManager.this);
              mHeld = true;
            }
            else
            {
              mService.releaseMulticastLock();
              UnsupportedOperationException localUnsupportedOperationException = new java/lang/UnsupportedOperationException;
              localUnsupportedOperationException.<init>("Exceeded maximum number of wifi locks");
              throw localUnsupportedOperationException;
            }
          }
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
        label146:
        return;
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      super.finalize();
      setReferenceCounted(false);
      release();
    }
    
    public boolean isHeld()
    {
      synchronized (mBinder)
      {
        boolean bool = mHeld;
        return bool;
      }
    }
    
    public void release()
    {
      synchronized (mBinder)
      {
        if (mRefCounted)
        {
          int i = mRefCount - 1;
          mRefCount = i;
          if (i != 0) {
            break label98;
          }
        }
        else
        {
          boolean bool = mHeld;
          if (!bool) {
            break label98;
          }
        }
        try
        {
          mService.releaseMulticastLock();
          synchronized (WifiManager.this)
          {
            WifiManager.access$710(WifiManager.this);
            mHeld = false;
          }
          if (mRefCount < 0) {
            break label108;
          }
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
        label98:
        return;
        label108:
        RuntimeException localRuntimeException = new java/lang/RuntimeException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("MulticastLock under-locked ");
        localStringBuilder.append(mTag);
        localRuntimeException.<init>(localStringBuilder.toString());
        throw localRuntimeException;
      }
    }
    
    public void setReferenceCounted(boolean paramBoolean)
    {
      mRefCounted = paramBoolean;
    }
    
    public String toString()
    {
      synchronized (mBinder)
      {
        String str1 = Integer.toHexString(System.identityHashCode(this));
        if (mHeld) {
          str2 = "held; ";
        } else {
          str2 = "";
        }
        Object localObject2;
        if (mRefCounted)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("refcounted: refcount = ");
          ((StringBuilder)localObject2).append(mRefCount);
          localObject2 = ((StringBuilder)localObject2).toString();
        }
        else
        {
          localObject2 = "not refcounted";
        }
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("MulticastLock{ ");
        localStringBuilder.append(str1);
        localStringBuilder.append("; ");
        localStringBuilder.append(str2);
        localStringBuilder.append((String)localObject2);
        localStringBuilder.append(" }");
        String str2 = localStringBuilder.toString();
        return str2;
      }
    }
  }
  
  private static class ProvisioningCallbackProxy
    extends IProvisioningCallback.Stub
  {
    private final ProvisioningCallback mCallback;
    private final Handler mHandler;
    
    ProvisioningCallbackProxy(Looper paramLooper, ProvisioningCallback paramProvisioningCallback)
    {
      mHandler = new Handler(paramLooper);
      mCallback = paramProvisioningCallback;
    }
    
    public void onProvisioningFailure(int paramInt)
    {
      mHandler.post(new _..Lambda.WifiManager.ProvisioningCallbackProxy.rgPeSRj_1qriYZtaCu57EZHtc_Q(this, paramInt));
    }
    
    public void onProvisioningStatus(int paramInt)
    {
      mHandler.post(new _..Lambda.WifiManager.ProvisioningCallbackProxy.0_NXiwyrbrT_579x_6QMO0y3rzc(this, paramInt));
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SapStartFailure {}
  
  private class ServiceHandler
    extends Handler
  {
    ServiceHandler(Looper paramLooper)
    {
      super();
    }
    
    private void dispatchMessageToListeners(Message paramMessage)
    {
      Object localObject = WifiManager.this.removeListener(arg2);
      switch (what)
      {
      default: 
        break;
      case 151574: 
        if (localObject != null) {
          ((WifiManager.TxPacketCountListener)localObject).onFailure(arg1);
        }
        break;
      case 151573: 
        if (localObject != null)
        {
          paramMessage = (RssiPacketCountInfo)obj;
          if (paramMessage != null) {
            ((WifiManager.TxPacketCountListener)localObject).onSuccess(txgood + txbad);
          } else {
            ((WifiManager.TxPacketCountListener)localObject).onFailure(0);
          }
        }
        break;
      case 151555: 
      case 151558: 
      case 151561: 
      case 151571: 
        if (localObject != null) {
          ((WifiManager.ActionListener)localObject).onSuccess();
        }
        break;
      case 151554: 
      case 151557: 
      case 151560: 
      case 151570: 
        if (localObject != null) {
          ((WifiManager.ActionListener)localObject).onFailure(arg1);
        }
        break;
      case 69636: 
        Log.e("WifiManager", "Channel connection lost");
        WifiManager.access$502(WifiManager.this, null);
        getLooper().quit();
        break;
      case 69634: 
        break;
      case 69632: 
        if (arg1 == 0)
        {
          mAsyncChannel.sendMessage(69633);
        }
        else
        {
          Log.e("WifiManager", "Failed to set up channel connection");
          WifiManager.access$502(WifiManager.this, null);
        }
        mConnected.countDown();
      }
    }
    
    public void handleMessage(Message paramMessage)
    {
      synchronized (WifiManager.sServiceHandlerDispatchLock)
      {
        dispatchMessageToListeners(paramMessage);
        return;
      }
    }
  }
  
  public static abstract interface SoftApCallback
  {
    public abstract void onNumClientsChanged(int paramInt);
    
    public abstract void onStaConnected(String paramString, int paramInt);
    
    public abstract void onStaDisconnected(String paramString, int paramInt);
    
    public abstract void onStateChanged(int paramInt1, int paramInt2);
  }
  
  private static class SoftApCallbackProxy
    extends ISoftApCallback.Stub
  {
    private final WifiManager.SoftApCallback mCallback;
    private final Handler mHandler;
    
    SoftApCallbackProxy(Looper paramLooper, WifiManager.SoftApCallback paramSoftApCallback)
    {
      mHandler = new Handler(paramLooper);
      mCallback = paramSoftApCallback;
    }
    
    public void onNumClientsChanged(int paramInt)
      throws RemoteException
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SoftApCallbackProxy: onNumClientsChanged: numClients=");
      localStringBuilder.append(paramInt);
      Log.v("WifiManager", localStringBuilder.toString());
      mHandler.post(new _..Lambda.WifiManager.SoftApCallbackProxy.f44R8L0UcqgnIaD5lXMmeuRHCWI(this, paramInt));
    }
    
    public void onStaConnected(String paramString, int paramInt)
      throws RemoteException
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SoftApCallbackProxy: [");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("]onStaConnected Macaddr =");
      localStringBuilder.append(paramString);
      Log.v("WifiManager", localStringBuilder.toString());
      mHandler.post(new _..Lambda.WifiManager.SoftApCallbackProxy.vo4E4HQhX8ezRZP1e1kxdx6MvpE(this, paramString, paramInt));
    }
    
    public void onStaDisconnected(String paramString, int paramInt)
      throws RemoteException
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SoftApCallbackProxy: [");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("]onStaDisconnected Macaddr =");
      localStringBuilder.append(paramString);
      Log.v("WifiManager", localStringBuilder.toString());
      mHandler.post(new _..Lambda.WifiManager.SoftApCallbackProxy.X5LJgdNUCXHctJ7m4_CGDjDEfkU(this, paramString, paramInt));
    }
    
    public void onStateChanged(int paramInt1, int paramInt2)
      throws RemoteException
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("SoftApCallbackProxy: onStateChanged: state=");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", failureReason=");
      localStringBuilder.append(paramInt2);
      Log.v("WifiManager", localStringBuilder.toString());
      mHandler.post(new _..Lambda.WifiManager.SoftApCallbackProxy.vmSW5veUpC52oRINBy419US5snk(this, paramInt1, paramInt2));
    }
  }
  
  public static abstract interface TxPacketCountListener
  {
    public abstract void onFailure(int paramInt);
    
    public abstract void onSuccess(int paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface WifiApState {}
  
  public class WifiLock
  {
    private final IBinder mBinder;
    private boolean mHeld;
    int mLockType;
    private int mRefCount;
    private boolean mRefCounted;
    private String mTag;
    private WorkSource mWorkSource;
    
    private WifiLock(int paramInt, String paramString)
    {
      mTag = paramString;
      mLockType = paramInt;
      mBinder = new Binder();
      mRefCount = 0;
      mRefCounted = true;
      mHeld = false;
    }
    
    public void acquire()
    {
      synchronized (mBinder)
      {
        if (mRefCounted)
        {
          int i = mRefCount + 1;
          mRefCount = i;
          if (i != 1) {
            break label160;
          }
        }
        else
        {
          boolean bool = mHeld;
          if (bool) {
            break label160;
          }
        }
        try
        {
          mService.acquireWifiLock(mBinder, mLockType, mTag, mWorkSource);
          synchronized (WifiManager.this)
          {
            if (mActiveLockCount < 50)
            {
              WifiManager.access$708(WifiManager.this);
              mHeld = true;
            }
            else
            {
              mService.releaseWifiLock(mBinder);
              UnsupportedOperationException localUnsupportedOperationException = new java/lang/UnsupportedOperationException;
              localUnsupportedOperationException.<init>("Exceeded maximum number of wifi locks");
              throw localUnsupportedOperationException;
            }
          }
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
        label160:
        return;
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      super.finalize();
      synchronized (mBinder)
      {
        boolean bool = mHeld;
        if (bool) {
          try
          {
            mService.releaseWifiLock(mBinder);
            synchronized (WifiManager.this)
            {
              WifiManager.access$710(WifiManager.this);
            }
          }
          catch (RemoteException localRemoteException)
          {
            throw localRemoteException.rethrowFromSystemServer();
          }
        }
        return;
      }
    }
    
    public boolean isHeld()
    {
      synchronized (mBinder)
      {
        boolean bool = mHeld;
        return bool;
      }
    }
    
    public void release()
    {
      synchronized (mBinder)
      {
        if (mRefCounted)
        {
          int i = mRefCount - 1;
          mRefCount = i;
          if (i != 0) {
            break label103;
          }
        }
        else
        {
          boolean bool = mHeld;
          if (!bool) {
            break label103;
          }
        }
        try
        {
          mService.releaseWifiLock(mBinder);
          synchronized (WifiManager.this)
          {
            WifiManager.access$710(WifiManager.this);
            mHeld = false;
          }
          if (mRefCount < 0) {
            break label113;
          }
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
        label103:
        return;
        label113:
        RuntimeException localRuntimeException = new java/lang/RuntimeException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("WifiLock under-locked ");
        localStringBuilder.append(mTag);
        localRuntimeException.<init>(localStringBuilder.toString());
        throw localRuntimeException;
      }
    }
    
    public void setReferenceCounted(boolean paramBoolean)
    {
      mRefCounted = paramBoolean;
    }
    
    public void setWorkSource(WorkSource paramWorkSource)
    {
      IBinder localIBinder = mBinder;
      WorkSource localWorkSource = paramWorkSource;
      if (paramWorkSource != null)
      {
        localWorkSource = paramWorkSource;
        try
        {
          if (paramWorkSource.isEmpty()) {
            localWorkSource = null;
          }
        }
        finally
        {
          break label173;
        }
      }
      int i = 1;
      if (localWorkSource == null)
      {
        mWorkSource = null;
      }
      else
      {
        localWorkSource.clearNames();
        paramWorkSource = mWorkSource;
        i = 1;
        if (paramWorkSource == null)
        {
          if (mWorkSource == null) {
            i = 0;
          }
          paramWorkSource = new android/os/WorkSource;
          paramWorkSource.<init>(localWorkSource);
          mWorkSource = paramWorkSource;
        }
        else
        {
          boolean bool1 = mWorkSource.equals(localWorkSource) ^ true;
          i = bool1;
          if (bool1)
          {
            mWorkSource.set(localWorkSource);
            i = bool1;
          }
        }
      }
      if (i != 0)
      {
        boolean bool2 = mHeld;
        if (bool2) {
          try
          {
            mService.updateWifiLockWorkSource(mBinder, mWorkSource);
          }
          catch (RemoteException paramWorkSource)
          {
            throw paramWorkSource.rethrowFromSystemServer();
          }
        }
      }
      return;
      label173:
      throw paramWorkSource;
    }
    
    public String toString()
    {
      synchronized (mBinder)
      {
        String str1 = Integer.toHexString(System.identityHashCode(this));
        if (mHeld) {
          str2 = "held; ";
        } else {
          str2 = "";
        }
        Object localObject2;
        if (mRefCounted)
        {
          localObject2 = new java/lang/StringBuilder;
          ((StringBuilder)localObject2).<init>();
          ((StringBuilder)localObject2).append("refcounted: refcount = ");
          ((StringBuilder)localObject2).append(mRefCount);
          localObject2 = ((StringBuilder)localObject2).toString();
        }
        else
        {
          localObject2 = "not refcounted";
        }
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("WifiLock{ ");
        localStringBuilder.append(str1);
        localStringBuilder.append("; ");
        localStringBuilder.append(str2);
        localStringBuilder.append((String)localObject2);
        localStringBuilder.append(" }");
        String str2 = localStringBuilder.toString();
        return str2;
      }
    }
  }
  
  public static abstract class WpsCallback
  {
    public WpsCallback() {}
    
    public abstract void onFailed(int paramInt);
    
    public abstract void onStarted(String paramString);
    
    public abstract void onSucceeded();
  }
}
