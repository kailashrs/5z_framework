package android.net;

import android.annotation.SystemApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.INetworkActivityListener;
import android.os.INetworkActivityListener.Stub;
import android.os.INetworkManagementService;
import android.os.INetworkManagementService.Stub;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseIntArray;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import libcore.net.event.NetworkEventDispatcher;

public class ConnectivityManager
{
  @Deprecated
  public static final String ACTION_BACKGROUND_DATA_SETTING_CHANGED = "android.net.conn.BACKGROUND_DATA_SETTING_CHANGED";
  public static final String ACTION_CAPTIVE_PORTAL_SIGN_IN = "android.net.conn.CAPTIVE_PORTAL";
  public static final String ACTION_CAPTIVE_PORTAL_TEST_COMPLETED = "android.net.conn.CAPTIVE_PORTAL_TEST_COMPLETED";
  public static final String ACTION_DATA_ACTIVITY_CHANGE = "android.net.conn.DATA_ACTIVITY_CHANGE";
  public static final String ACTION_PROMPT_LOST_VALIDATION = "android.net.conn.PROMPT_LOST_VALIDATION";
  public static final String ACTION_PROMPT_UNVALIDATED = "android.net.conn.PROMPT_UNVALIDATED";
  public static final String ACTION_RESTRICT_BACKGROUND_CHANGED = "android.net.conn.RESTRICT_BACKGROUND_CHANGED";
  public static final String ACTION_TETHER_STATE_CHANGED = "android.net.conn.TETHER_STATE_CHANGED";
  private static final NetworkRequest ALREADY_UNREGISTERED = new NetworkRequest.Builder().clearCapabilities().build();
  private static final int BASE = 524288;
  public static final int CALLBACK_AVAILABLE = 524290;
  public static final int CALLBACK_CAP_CHANGED = 524294;
  public static final int CALLBACK_IP_CHANGED = 524295;
  public static final int CALLBACK_LOSING = 524291;
  public static final int CALLBACK_LOST = 524292;
  public static final int CALLBACK_PRECHECK = 524289;
  public static final int CALLBACK_RESUMED = 524298;
  public static final int CALLBACK_SUSPENDED = 524297;
  public static final int CALLBACK_UNAVAIL = 524293;
  @Deprecated
  public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
  public static final String CONNECTIVITY_ACTION_SUPL = "android.net.conn.CONNECTIVITY_CHANGE_SUPL";
  @Deprecated
  public static final int DEFAULT_NETWORK_PREFERENCE = 1;
  private static final int EXPIRE_LEGACY_REQUEST = 524296;
  public static final String EXTRA_ACTIVE_LOCAL_ONLY = "localOnlyArray";
  public static final String EXTRA_ACTIVE_TETHER = "tetherArray";
  public static final String EXTRA_ADD_TETHER_TYPE = "extraAddTetherType";
  public static final String EXTRA_AVAILABLE_TETHER = "availableArray";
  public static final String EXTRA_CAPTIVE_PORTAL = "android.net.extra.CAPTIVE_PORTAL";
  public static final String EXTRA_CAPTIVE_PORTAL_PROBE_SPEC = "android.net.extra.CAPTIVE_PORTAL_PROBE_SPEC";
  public static final String EXTRA_CAPTIVE_PORTAL_URL = "android.net.extra.CAPTIVE_PORTAL_URL";
  public static final String EXTRA_CAPTIVE_PORTAL_USER_AGENT = "android.net.extra.CAPTIVE_PORTAL_USER_AGENT";
  public static final String EXTRA_DEVICE_TYPE = "deviceType";
  public static final String EXTRA_ERRORED_TETHER = "erroredArray";
  public static final String EXTRA_EXTRA_INFO = "extraInfo";
  public static final String EXTRA_INET_CONDITION = "inetCondition";
  public static final String EXTRA_IS_ACTIVE = "isActive";
  public static final String EXTRA_IS_CAPTIVE_PORTAL = "captivePortal";
  public static final String EXTRA_IS_FAILOVER = "isFailover";
  public static final String EXTRA_NETWORK = "android.net.extra.NETWORK";
  @Deprecated
  public static final String EXTRA_NETWORK_INFO = "networkInfo";
  public static final String EXTRA_NETWORK_REQUEST = "android.net.extra.NETWORK_REQUEST";
  public static final String EXTRA_NETWORK_TYPE = "networkType";
  public static final String EXTRA_NO_CONNECTIVITY = "noConnectivity";
  public static final String EXTRA_OTHER_NETWORK_INFO = "otherNetwork";
  public static final String EXTRA_PROVISION_CALLBACK = "extraProvisionCallback";
  public static final String EXTRA_REALTIME_NS = "tsNanos";
  public static final String EXTRA_REASON = "reason";
  public static final String EXTRA_REM_TETHER_TYPE = "extraRemTetherType";
  public static final String EXTRA_RUN_PROVISION = "extraRunProvision";
  public static final String EXTRA_SET_ALARM = "extraSetAlarm";
  public static final String INET_CONDITION_ACTION = "android.net.conn.INET_CONDITION_ACTION";
  private static final int LISTEN = 1;
  public static final int MAX_NETWORK_TYPE = 17;
  public static final int MAX_RADIO_TYPE = 17;
  private static final int MIN_NETWORK_TYPE = 0;
  public static final int MULTIPATH_PREFERENCE_HANDOVER = 1;
  public static final int MULTIPATH_PREFERENCE_PERFORMANCE = 4;
  public static final int MULTIPATH_PREFERENCE_RELIABILITY = 2;
  public static final int MULTIPATH_PREFERENCE_UNMETERED = 7;
  public static final int NETID_UNSET = 0;
  public static final String PRIVATE_DNS_DEFAULT_MODE_FALLBACK = "opportunistic";
  public static final String PRIVATE_DNS_MODE_OFF = "off";
  public static final String PRIVATE_DNS_MODE_OPPORTUNISTIC = "opportunistic";
  public static final String PRIVATE_DNS_MODE_PROVIDER_HOSTNAME = "hostname";
  private static final int REQUEST = 2;
  public static final int REQUEST_ID_UNSET = 0;
  public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
  public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
  public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;
  private static final String TAG = "ConnectivityManager";
  @SystemApi
  public static final int TETHERING_BLUETOOTH = 2;
  public static final int TETHERING_INVALID = -1;
  @SystemApi
  public static final int TETHERING_USB = 1;
  @SystemApi
  public static final int TETHERING_WIFI = 0;
  public static final int TETHERING_WIGIG = 3;
  public static final int TETHER_ERROR_DISABLE_NAT_ERROR = 9;
  public static final int TETHER_ERROR_ENABLE_NAT_ERROR = 8;
  public static final int TETHER_ERROR_IFACE_CFG_ERROR = 10;
  public static final int TETHER_ERROR_MASTER_ERROR = 5;
  public static final int TETHER_ERROR_NO_ERROR = 0;
  public static final int TETHER_ERROR_PROVISION_FAILED = 11;
  public static final int TETHER_ERROR_SERVICE_UNAVAIL = 2;
  public static final int TETHER_ERROR_TETHER_IFACE_ERROR = 6;
  public static final int TETHER_ERROR_UNAVAIL_IFACE = 4;
  public static final int TETHER_ERROR_UNKNOWN_IFACE = 1;
  public static final int TETHER_ERROR_UNSUPPORTED = 3;
  public static final int TETHER_ERROR_UNTETHER_IFACE_ERROR = 7;
  @Deprecated
  public static final int TYPE_BLUETOOTH = 7;
  @Deprecated
  public static final int TYPE_DUMMY = 8;
  @Deprecated
  public static final int TYPE_ETHERNET = 9;
  @Deprecated
  public static final int TYPE_MOBILE = 0;
  @Deprecated
  public static final int TYPE_MOBILE_CBS = 12;
  @Deprecated
  public static final int TYPE_MOBILE_DUN = 4;
  @Deprecated
  public static final int TYPE_MOBILE_EMERGENCY = 15;
  @Deprecated
  public static final int TYPE_MOBILE_FOTA = 10;
  @Deprecated
  public static final int TYPE_MOBILE_HIPRI = 5;
  @Deprecated
  public static final int TYPE_MOBILE_IA = 14;
  @Deprecated
  public static final int TYPE_MOBILE_IMS = 11;
  @Deprecated
  public static final int TYPE_MOBILE_MMS = 2;
  @Deprecated
  public static final int TYPE_MOBILE_SUPL = 3;
  public static final int TYPE_NONE = -1;
  @Deprecated
  public static final int TYPE_PROXY = 16;
  @Deprecated
  public static final int TYPE_VPN = 17;
  @Deprecated
  public static final int TYPE_WIFI = 1;
  @Deprecated
  public static final int TYPE_WIFI_P2P = 13;
  @Deprecated
  public static final int TYPE_WIMAX = 6;
  private static CallbackHandler sCallbackHandler;
  private static final HashMap<NetworkRequest, NetworkCallback> sCallbacks = new HashMap();
  private static ConnectivityManager sInstance;
  private static HashMap<NetworkCapabilities, LegacyRequest> sLegacyRequests = new HashMap();
  private static final SparseIntArray sLegacyTypeToCapability;
  private static final SparseIntArray sLegacyTypeToTransport = new SparseIntArray();
  private final Context mContext;
  private INetworkManagementService mNMService;
  private INetworkPolicyManager mNPManager;
  private final ArrayMap<OnNetworkActiveListener, INetworkActivityListener> mNetworkActivityListeners = new ArrayMap();
  private final IConnectivityManager mService;
  
  static
  {
    sLegacyTypeToTransport.put(0, 0);
    sLegacyTypeToTransport.put(12, 0);
    sLegacyTypeToTransport.put(4, 0);
    sLegacyTypeToTransport.put(10, 0);
    sLegacyTypeToTransport.put(5, 0);
    sLegacyTypeToTransport.put(11, 0);
    sLegacyTypeToTransport.put(2, 0);
    sLegacyTypeToTransport.put(3, 0);
    sLegacyTypeToTransport.put(1, 1);
    sLegacyTypeToTransport.put(13, 1);
    sLegacyTypeToTransport.put(7, 2);
    sLegacyTypeToTransport.put(9, 3);
    sLegacyTypeToCapability = new SparseIntArray();
    sLegacyTypeToCapability.put(12, 5);
    sLegacyTypeToCapability.put(4, 2);
    sLegacyTypeToCapability.put(10, 3);
    sLegacyTypeToCapability.put(11, 4);
    sLegacyTypeToCapability.put(2, 0);
    sLegacyTypeToCapability.put(3, 1);
    sLegacyTypeToCapability.put(13, 6);
  }
  
  public ConnectivityManager(Context paramContext, IConnectivityManager paramIConnectivityManager)
  {
    mContext = ((Context)Preconditions.checkNotNull(paramContext, "missing context"));
    mService = ((IConnectivityManager)Preconditions.checkNotNull(paramIConnectivityManager, "missing IConnectivityManager"));
    sInstance = this;
  }
  
  private static void checkCallbackNotNull(NetworkCallback paramNetworkCallback)
  {
    Preconditions.checkNotNull(paramNetworkCallback, "null NetworkCallback");
  }
  
  private void checkLegacyRoutingApiAccess()
  {
    if (mContext.checkCallingOrSelfPermission("com.android.permission.INJECT_OMADM_SETTINGS") == 0) {
      return;
    }
    unsupportedStartingFrom(23);
  }
  
  private static void checkPendingIntentNotNull(PendingIntent paramPendingIntent)
  {
    Preconditions.checkNotNull(paramPendingIntent, "PendingIntent cannot be null.");
  }
  
  private static void checkTimeout(int paramInt)
  {
    Preconditions.checkArgumentPositive(paramInt, "timeoutMs must be strictly positive.");
  }
  
  private static RuntimeException convertServiceException(ServiceSpecificException paramServiceSpecificException)
  {
    if (errorCode != 1)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown service error code ");
      localStringBuilder.append(errorCode);
      Log.w("ConnectivityManager", localStringBuilder.toString());
      return new RuntimeException(paramServiceSpecificException);
    }
    return new TooManyRequestsException();
  }
  
  public static final void enforceChangePermission(Context paramContext)
  {
    int i = Binder.getCallingUid();
    Settings.checkAndNoteChangeNetworkStateOperation(paramContext, i, Settings.getPackageNameForUid(paramContext, i), true);
  }
  
  public static final void enforceTetherChangePermission(Context paramContext, String paramString)
  {
    Preconditions.checkNotNull(paramContext, "Context cannot be null");
    Preconditions.checkNotNull(paramString, "callingPkg cannot be null");
    if (paramContext.getResources().getStringArray(17236023).length == 2) {
      paramContext.enforceCallingOrSelfPermission("android.permission.TETHER_PRIVILEGED", "ConnectivityService");
    } else {
      Settings.checkAndNoteWriteSettingsOperation(paramContext, Binder.getCallingUid(), paramString, true);
    }
  }
  
  private void expireRequest(NetworkCapabilities paramNetworkCapabilities, int paramInt)
  {
    synchronized (sLegacyRequests)
    {
      LegacyRequest localLegacyRequest = (LegacyRequest)sLegacyRequests.get(paramNetworkCapabilities);
      if (localLegacyRequest == null) {
        return;
      }
      int i = expireSequenceNumber;
      if (expireSequenceNumber == paramInt) {
        removeRequestForFeature(paramNetworkCapabilities);
      }
      paramNetworkCapabilities = new StringBuilder();
      paramNetworkCapabilities.append("expireRequest with ");
      paramNetworkCapabilities.append(i);
      paramNetworkCapabilities.append(", ");
      paramNetworkCapabilities.append(paramInt);
      Log.d("ConnectivityManager", paramNetworkCapabilities.toString());
      return;
    }
  }
  
  private NetworkRequest findRequestForFeature(NetworkCapabilities paramNetworkCapabilities)
  {
    synchronized (sLegacyRequests)
    {
      paramNetworkCapabilities = (LegacyRequest)sLegacyRequests.get(paramNetworkCapabilities);
      if (paramNetworkCapabilities != null)
      {
        paramNetworkCapabilities = networkRequest;
        return paramNetworkCapabilities;
      }
      return null;
    }
  }
  
  public static ConnectivityManager from(Context paramContext)
  {
    return (ConnectivityManager)paramContext.getSystemService("connectivity");
  }
  
  public static String getCallbackName(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 524298: 
      return "CALLBACK_RESUMED";
    case 524297: 
      return "CALLBACK_SUSPENDED";
    case 524296: 
      return "EXPIRE_LEGACY_REQUEST";
    case 524295: 
      return "CALLBACK_IP_CHANGED";
    case 524294: 
      return "CALLBACK_CAP_CHANGED";
    case 524293: 
      return "CALLBACK_UNAVAIL";
    case 524292: 
      return "CALLBACK_LOST";
    case 524291: 
      return "CALLBACK_LOSING";
    case 524290: 
      return "CALLBACK_AVAILABLE";
    }
    return "CALLBACK_PRECHECK";
  }
  
  private CallbackHandler getDefaultHandler()
  {
    synchronized (sCallbacks)
    {
      if (sCallbackHandler == null)
      {
        localCallbackHandler = new android/net/ConnectivityManager$CallbackHandler;
        localCallbackHandler.<init>(this, ConnectivityThread.getInstanceLooper());
        sCallbackHandler = localCallbackHandler;
      }
      CallbackHandler localCallbackHandler = sCallbackHandler;
      return localCallbackHandler;
    }
  }
  
  @Deprecated
  private static ConnectivityManager getInstance()
  {
    if (getInstanceOrNull() != null) {
      return getInstanceOrNull();
    }
    throw new IllegalStateException("No ConnectivityManager yet constructed");
  }
  
  @Deprecated
  static ConnectivityManager getInstanceOrNull()
  {
    return sInstance;
  }
  
  private INetworkManagementService getNetworkManagementService()
  {
    try
    {
      if (mNMService != null)
      {
        localINetworkManagementService = mNMService;
        return localINetworkManagementService;
      }
      mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"));
      INetworkManagementService localINetworkManagementService = mNMService;
      return localINetworkManagementService;
    }
    finally {}
  }
  
  private INetworkPolicyManager getNetworkPolicyManager()
  {
    try
    {
      if (mNPManager != null)
      {
        localINetworkPolicyManager = mNPManager;
        return localINetworkPolicyManager;
      }
      mNPManager = INetworkPolicyManager.Stub.asInterface(ServiceManager.getService("netpolicy"));
      INetworkPolicyManager localINetworkPolicyManager = mNPManager;
      return localINetworkPolicyManager;
    }
    finally {}
  }
  
  @Deprecated
  public static String getNetworkTypeName(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 17: 
      return "VPN";
    case 16: 
      return "PROXY";
    case 15: 
      return "MOBILE_EMERGENCY";
    case 14: 
      return "MOBILE_IA";
    case 13: 
      return "WIFI_P2P";
    case 12: 
      return "MOBILE_CBS";
    case 11: 
      return "MOBILE_IMS";
    case 10: 
      return "MOBILE_FOTA";
    case 9: 
      return "ETHERNET";
    case 8: 
      return "DUMMY";
    case 7: 
      return "BLUETOOTH";
    case 6: 
      return "WIMAX";
    case 5: 
      return "MOBILE_HIPRI";
    case 4: 
      return "MOBILE_DUN";
    case 3: 
      return "MOBILE_SUPL";
    case 2: 
      return "MOBILE_MMS";
    case 1: 
      return "WIFI";
    case 0: 
      return "MOBILE";
    }
    return "NONE";
  }
  
  @Deprecated
  public static Network getProcessDefaultNetwork()
  {
    int i = NetworkUtils.getBoundNetworkForProcess();
    if (i == 0) {
      return null;
    }
    return new Network(i);
  }
  
  private int inferLegacyTypeForNetworkCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    if (paramNetworkCapabilities == null) {
      return -1;
    }
    if (!paramNetworkCapabilities.hasTransport(0)) {
      return -1;
    }
    if (!paramNetworkCapabilities.hasCapability(1)) {
      return -1;
    }
    Object localObject = null;
    int i = -1;
    if (paramNetworkCapabilities.hasCapability(5))
    {
      localObject = "enableCBS";
      i = 12;
    }
    else if (paramNetworkCapabilities.hasCapability(4))
    {
      localObject = "enableIMS";
      i = 11;
    }
    else if (paramNetworkCapabilities.hasCapability(3))
    {
      localObject = "enableFOTA";
      i = 10;
    }
    else if (paramNetworkCapabilities.hasCapability(2))
    {
      localObject = "enableDUN";
      i = 4;
    }
    else if (paramNetworkCapabilities.hasCapability(1))
    {
      localObject = "enableSUPL";
      i = 3;
    }
    else if (paramNetworkCapabilities.hasCapability(12))
    {
      localObject = "enableHIPRI";
      i = 5;
    }
    if (localObject != null)
    {
      localObject = networkCapabilitiesForFeature(0, (String)localObject);
      if ((((NetworkCapabilities)localObject).equalsNetCapabilities(paramNetworkCapabilities)) && (((NetworkCapabilities)localObject).equalsTransportTypes(paramNetworkCapabilities))) {
        return i;
      }
    }
    return -1;
  }
  
  @Deprecated
  public static boolean isNetworkTypeMobile(int paramInt)
  {
    switch (paramInt)
    {
    case 1: 
    case 6: 
    case 7: 
    case 8: 
    case 9: 
    case 13: 
    default: 
      return false;
    }
    return true;
  }
  
  @Deprecated
  public static boolean isNetworkTypeValid(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt <= 17)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @Deprecated
  public static boolean isNetworkTypeWifi(int paramInt)
  {
    return (paramInt == 1) || (paramInt == 13);
  }
  
  private int legacyTypeForNetworkCapabilities(NetworkCapabilities paramNetworkCapabilities)
  {
    if (paramNetworkCapabilities == null) {
      return -1;
    }
    if (paramNetworkCapabilities.hasCapability(5)) {
      return 12;
    }
    if (paramNetworkCapabilities.hasCapability(4)) {
      return 11;
    }
    if (paramNetworkCapabilities.hasCapability(3)) {
      return 10;
    }
    if (paramNetworkCapabilities.hasCapability(2)) {
      return 4;
    }
    if (paramNetworkCapabilities.hasCapability(1)) {
      return 3;
    }
    if (paramNetworkCapabilities.hasCapability(0)) {
      return 2;
    }
    if (paramNetworkCapabilities.hasCapability(12)) {
      return 5;
    }
    if (paramNetworkCapabilities.hasCapability(6)) {
      return 13;
    }
    return -1;
  }
  
  private NetworkCapabilities networkCapabilitiesForFeature(int paramInt, String paramString)
  {
    int i = 1;
    if (paramInt == 0)
    {
      switch (paramString.hashCode())
      {
      default: 
        break;
      case 1998933033: 
        if (paramString.equals("enableDUNAlways")) {
          paramInt = 2;
        }
        break;
      case 1893183457: 
        if (paramString.equals("enableSUPL")) {
          paramInt = 7;
        }
        break;
      case 1892790521: 
        if (paramString.equals("enableFOTA")) {
          paramInt = 3;
        }
        break;
      case -631672240: 
        if (paramString.equals("enableMMS")) {
          paramInt = 6;
        }
        break;
      case -631676084: 
        if (paramString.equals("enableIMS")) {
          paramInt = 5;
        }
        break;
      case -631680646: 
        if (paramString.equals("enableDUN")) {
          paramInt = i;
        }
        break;
      case -631682191: 
        if (paramString.equals("enableCBS")) {
          paramInt = 0;
        }
        break;
      case -1451370941: 
        if (paramString.equals("enableHIPRI")) {
          paramInt = 4;
        }
        break;
      }
      paramInt = -1;
      switch (paramInt)
      {
      default: 
        return null;
      case 7: 
        return networkCapabilitiesForType(3);
      case 6: 
        return networkCapabilitiesForType(2);
      case 5: 
        return networkCapabilitiesForType(11);
      case 4: 
        return networkCapabilitiesForType(5);
      case 3: 
        return networkCapabilitiesForType(10);
      case 1: 
      case 2: 
        return networkCapabilitiesForType(4);
      }
      return networkCapabilitiesForType(12);
    }
    if ((paramInt == 1) && ("p2p".equals(paramString))) {
      return networkCapabilitiesForType(13);
    }
    return null;
  }
  
  public static NetworkCapabilities networkCapabilitiesForType(int paramInt)
  {
    NetworkCapabilities localNetworkCapabilities = new NetworkCapabilities();
    int i = sLegacyTypeToTransport.get(paramInt, -1);
    boolean bool;
    if (i != -1) {
      bool = true;
    } else {
      bool = false;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("unknown legacy type: ");
    localStringBuilder.append(paramInt);
    Preconditions.checkArgument(bool, localStringBuilder.toString());
    localNetworkCapabilities.addTransportType(i);
    localNetworkCapabilities.addCapability(sLegacyTypeToCapability.get(paramInt, 12));
    localNetworkCapabilities.maybeMarkCapabilitiesRestricted();
    return localNetworkCapabilities;
  }
  
  private boolean removeRequestForFeature(NetworkCapabilities paramNetworkCapabilities)
  {
    synchronized (sLegacyRequests)
    {
      paramNetworkCapabilities = (LegacyRequest)sLegacyRequests.remove(paramNetworkCapabilities);
      if (paramNetworkCapabilities == null) {
        return false;
      }
      unregisterNetworkCallback(networkCallback);
      paramNetworkCapabilities.clearDnsBinding();
      return true;
    }
  }
  
  private void renewRequestLocked(LegacyRequest paramLegacyRequest)
  {
    expireSequenceNumber += 1;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("renewing request to seqNum ");
    localStringBuilder.append(expireSequenceNumber);
    Log.d("ConnectivityManager", localStringBuilder.toString());
    sendExpireMsgForFeature(networkCapabilities, expireSequenceNumber, delay);
  }
  
  private NetworkRequest requestNetworkForFeatureLocked(NetworkCapabilities paramNetworkCapabilities)
  {
    int i = legacyTypeForNetworkCapabilities(paramNetworkCapabilities);
    try
    {
      int j = mService.getRestoreDefaultNetworkDelay(i);
      LegacyRequest localLegacyRequest = new LegacyRequest(null);
      networkCapabilities = paramNetworkCapabilities;
      delay = j;
      expireSequenceNumber = 0;
      networkRequest = sendRequestForNetwork(paramNetworkCapabilities, networkCallback, 0, 2, i, getDefaultHandler());
      if (networkRequest == null) {
        return null;
      }
      sLegacyRequests.put(paramNetworkCapabilities, localLegacyRequest);
      sendExpireMsgForFeature(paramNetworkCapabilities, expireSequenceNumber, j);
      return networkRequest;
    }
    catch (RemoteException paramNetworkCapabilities)
    {
      throw paramNetworkCapabilities.rethrowFromSystemServer();
    }
  }
  
  private void sendExpireMsgForFeature(NetworkCapabilities paramNetworkCapabilities, int paramInt1, int paramInt2)
  {
    if (paramInt2 >= 0)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("sending expire msg with seqNum ");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(" and delay ");
      ((StringBuilder)localObject).append(paramInt2);
      Log.d("ConnectivityManager", ((StringBuilder)localObject).toString());
      localObject = getDefaultHandler();
      ((CallbackHandler)localObject).sendMessageDelayed(((CallbackHandler)localObject).obtainMessage(524296, paramInt1, 0, paramNetworkCapabilities), paramInt2);
    }
  }
  
  /* Error */
  private NetworkRequest sendRequestForNetwork(NetworkCapabilities paramNetworkCapabilities, NetworkCallback paramNetworkCallback, int paramInt1, int paramInt2, int paramInt3, CallbackHandler paramCallbackHandler)
  {
    // Byte code:
    //   0: aload_2
    //   1: invokestatic 766	android/net/ConnectivityManager:checkCallbackNotNull	(Landroid/net/ConnectivityManager$NetworkCallback;)V
    //   4: iload 4
    //   6: iconst_2
    //   7: if_icmpeq +16 -> 23
    //   10: aload_1
    //   11: ifnull +6 -> 17
    //   14: goto +9 -> 23
    //   17: iconst_0
    //   18: istore 7
    //   20: goto +6 -> 26
    //   23: iconst_1
    //   24: istore 7
    //   26: iload 7
    //   28: ldc_w 768
    //   31: invokestatic 691	com/android/internal/util/Preconditions:checkArgument	(ZLjava/lang/Object;)V
    //   34: getstatic 328	android/net/ConnectivityManager:sCallbacks	Ljava/util/HashMap;
    //   37: astore 8
    //   39: aload 8
    //   41: monitorenter
    //   42: aload_2
    //   43: invokestatic 772	android/net/ConnectivityManager$NetworkCallback:access$900	(Landroid/net/ConnectivityManager$NetworkCallback;)Landroid/net/NetworkRequest;
    //   46: ifnull +22 -> 68
    //   49: aload_2
    //   50: invokestatic 772	android/net/ConnectivityManager$NetworkCallback:access$900	(Landroid/net/ConnectivityManager$NetworkCallback;)Landroid/net/NetworkRequest;
    //   53: getstatic 310	android/net/ConnectivityManager:ALREADY_UNREGISTERED	Landroid/net/NetworkRequest;
    //   56: if_acmpeq +12 -> 68
    //   59: ldc -35
    //   61: ldc_w 774
    //   64: invokestatic 777	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   67: pop
    //   68: new 779	android/os/Messenger
    //   71: astore 9
    //   73: aload 9
    //   75: aload 6
    //   77: invokespecial 782	android/os/Messenger:<init>	(Landroid/os/Handler;)V
    //   80: new 431	android/os/Binder
    //   83: astore 6
    //   85: aload 6
    //   87: invokespecial 783	android/os/Binder:<init>	()V
    //   90: iload 4
    //   92: iconst_1
    //   93: if_icmpne +21 -> 114
    //   96: aload_0
    //   97: getfield 354	android/net/ConnectivityManager:mService	Landroid/net/IConnectivityManager;
    //   100: aload_1
    //   101: aload 9
    //   103: aload 6
    //   105: invokeinterface 787 4 0
    //   110: astore_1
    //   111: goto +21 -> 132
    //   114: aload_0
    //   115: getfield 354	android/net/ConnectivityManager:mService	Landroid/net/IConnectivityManager;
    //   118: aload_1
    //   119: aload 9
    //   121: iload_3
    //   122: aload 6
    //   124: iload 5
    //   126: invokeinterface 791 6 0
    //   131: astore_1
    //   132: aload_1
    //   133: ifnull +12 -> 145
    //   136: getstatic 328	android/net/ConnectivityManager:sCallbacks	Ljava/util/HashMap;
    //   139: aload_1
    //   140: aload_2
    //   141: invokevirtual 748	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   144: pop
    //   145: aload_2
    //   146: aload_1
    //   147: invokestatic 795	android/net/ConnectivityManager$NetworkCallback:access$902	(Landroid/net/ConnectivityManager$NetworkCallback;Landroid/net/NetworkRequest;)Landroid/net/NetworkRequest;
    //   150: pop
    //   151: aload 8
    //   153: monitorexit
    //   154: aload_1
    //   155: areturn
    //   156: astore_1
    //   157: aload 8
    //   159: monitorexit
    //   160: aload_1
    //   161: athrow
    //   162: astore_1
    //   163: goto +12 -> 175
    //   166: astore_1
    //   167: goto +14 -> 181
    //   170: astore_1
    //   171: goto -14 -> 157
    //   174: astore_1
    //   175: aload_1
    //   176: invokestatic 797	android/net/ConnectivityManager:convertServiceException	(Landroid/os/ServiceSpecificException;)Ljava/lang/RuntimeException;
    //   179: athrow
    //   180: astore_1
    //   181: aload_1
    //   182: invokevirtual 752	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   185: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	186	0	this	ConnectivityManager
    //   0	186	1	paramNetworkCapabilities	NetworkCapabilities
    //   0	186	2	paramNetworkCallback	NetworkCallback
    //   0	186	3	paramInt1	int
    //   0	186	4	paramInt2	int
    //   0	186	5	paramInt3	int
    //   0	186	6	paramCallbackHandler	CallbackHandler
    //   18	9	7	bool	boolean
    //   71	49	9	localMessenger	Messenger
    // Exception table:
    //   from	to	target	type
    //   42	68	156	finally
    //   68	73	156	finally
    //   160	162	162	android/os/ServiceSpecificException
    //   160	162	166	android/os/RemoteException
    //   73	90	170	finally
    //   96	111	170	finally
    //   114	132	170	finally
    //   136	145	170	finally
    //   145	154	170	finally
    //   157	160	170	finally
    //   34	42	174	android/os/ServiceSpecificException
    //   34	42	180	android/os/RemoteException
  }
  
  @Deprecated
  public static boolean setProcessDefaultNetwork(Network paramNetwork)
  {
    int i;
    if (paramNetwork == null) {
      i = 0;
    } else {
      i = netId;
    }
    if (i == NetworkUtils.getBoundNetworkForProcess()) {
      return true;
    }
    if (NetworkUtils.bindProcessToNetwork(i))
    {
      try
      {
        Proxy.setHttpProxySystemProperty(getInstance().getDefaultProxy());
      }
      catch (SecurityException paramNetwork)
      {
        Log.e("ConnectivityManager", "Can't set proxy properties", paramNetwork);
      }
      InetAddress.clearDnsCache();
      NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
      return true;
    }
    return false;
  }
  
  @Deprecated
  public static boolean setProcessDefaultNetworkForHostResolution(Network paramNetwork)
  {
    int i;
    if (paramNetwork == null) {
      i = 0;
    } else {
      i = netId;
    }
    return NetworkUtils.bindProcessToNetworkForHostResolution(i);
  }
  
  private void unsupportedStartingFrom(int paramInt)
  {
    if (Process.myUid() == 1000) {
      return;
    }
    if (mContext.getApplicationInfo().targetSdkVersion < paramInt) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("This method is not supported in target SDK version ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" and above");
    throw new UnsupportedOperationException(localStringBuilder.toString());
  }
  
  public void addDefaultNetworkActiveListener(final OnNetworkActiveListener paramOnNetworkActiveListener)
  {
    INetworkActivityListener.Stub local1 = new INetworkActivityListener.Stub()
    {
      public void onNetworkActive()
        throws RemoteException
      {
        paramOnNetworkActiveListener.onNetworkActive();
      }
    };
    try
    {
      getNetworkManagementService().registerNetworkActivityListener(local1);
      mNetworkActivityListeners.put(paramOnNetworkActiveListener, local1);
      return;
    }
    catch (RemoteException paramOnNetworkActiveListener)
    {
      throw paramOnNetworkActiveListener.rethrowFromSystemServer();
    }
  }
  
  public boolean bindProcessToNetwork(Network paramNetwork)
  {
    return setProcessDefaultNetwork(paramNetwork);
  }
  
  public int checkMobileProvisioning(int paramInt)
  {
    try
    {
      paramInt = mService.checkMobileProvisioning(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void factoryReset()
  {
    try
    {
      mService.factoryReset();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public LinkProperties getActiveLinkProperties()
  {
    try
    {
      LinkProperties localLinkProperties = mService.getActiveLinkProperties();
      return localLinkProperties;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Network getActiveNetwork()
  {
    try
    {
      Network localNetwork = mService.getActiveNetwork();
      return localNetwork;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Network getActiveNetworkForUid(int paramInt)
  {
    return getActiveNetworkForUid(paramInt, false);
  }
  
  public Network getActiveNetworkForUid(int paramInt, boolean paramBoolean)
  {
    try
    {
      Network localNetwork = mService.getActiveNetworkForUid(paramInt, paramBoolean);
      return localNetwork;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public NetworkInfo getActiveNetworkInfo()
  {
    try
    {
      NetworkInfo localNetworkInfo = mService.getActiveNetworkInfo();
      return localNetworkInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public NetworkInfo getActiveNetworkInfoForUid(int paramInt)
  {
    return getActiveNetworkInfoForUid(paramInt, false);
  }
  
  public NetworkInfo getActiveNetworkInfoForUid(int paramInt, boolean paramBoolean)
  {
    try
    {
      NetworkInfo localNetworkInfo = mService.getActiveNetworkInfoForUid(paramInt, paramBoolean);
      return localNetworkInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public NetworkQuotaInfo getActiveNetworkQuotaInfo()
  {
    try
    {
      NetworkQuotaInfo localNetworkQuotaInfo = mService.getActiveNetworkQuotaInfo();
      return localNetworkQuotaInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public NetworkInfo[] getAllNetworkInfo()
  {
    try
    {
      NetworkInfo[] arrayOfNetworkInfo = mService.getAllNetworkInfo();
      return arrayOfNetworkInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Network[] getAllNetworks()
  {
    try
    {
      Network[] arrayOfNetwork = mService.getAllNetworks();
      return arrayOfNetwork;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String getAlwaysOnVpnPackageForUser(int paramInt)
  {
    try
    {
      String str = mService.getAlwaysOnVpnPackage(paramInt);
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean getBackgroundDataSetting()
  {
    return true;
  }
  
  public Network getBoundNetworkForProcess()
  {
    return getProcessDefaultNetwork();
  }
  
  @SystemApi
  public String getCaptivePortalServerUrl()
  {
    try
    {
      String str = mService.getCaptivePortalServerUrl();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int paramInt)
  {
    try
    {
      NetworkCapabilities[] arrayOfNetworkCapabilities = mService.getDefaultNetworkCapabilitiesForUser(paramInt);
      return arrayOfNetworkCapabilities;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ProxyInfo getDefaultProxy()
  {
    return getProxyForNetwork(getBoundNetworkForProcess());
  }
  
  public ProxyInfo getGlobalProxy()
  {
    try
    {
      ProxyInfo localProxyInfo = mService.getGlobalProxy();
      return localProxyInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getLastTetherError(String paramString)
  {
    try
    {
      int i = mService.getLastTetherError(paramString);
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public LinkProperties getLinkProperties(int paramInt)
  {
    try
    {
      LinkProperties localLinkProperties = mService.getLinkPropertiesForType(paramInt);
      return localLinkProperties;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public LinkProperties getLinkProperties(Network paramNetwork)
  {
    try
    {
      paramNetwork = mService.getLinkProperties(paramNetwork);
      return paramNetwork;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean getMobileDataEnabled()
  {
    Object localObject = ServiceManager.getService("phone");
    if (localObject != null) {
      try
      {
        ITelephony localITelephony = ITelephony.Stub.asInterface((IBinder)localObject);
        int i = SubscriptionManager.getDefaultDataSubscriptionId();
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("getMobileDataEnabled()+ subId=");
        ((StringBuilder)localObject).append(i);
        Log.d("ConnectivityManager", ((StringBuilder)localObject).toString());
        boolean bool = localITelephony.isUserDataEnabled(i);
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("getMobileDataEnabled()- subId=");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" retVal=");
        ((StringBuilder)localObject).append(bool);
        Log.d("ConnectivityManager", ((StringBuilder)localObject).toString());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    Log.d("ConnectivityManager", "getMobileDataEnabled()- remote exception retVal=false");
    return false;
  }
  
  public String getMobileProvisioningUrl()
  {
    try
    {
      String str = mService.getMobileProvisioningUrl();
      return str;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getMultipathPreference(Network paramNetwork)
  {
    try
    {
      int i = mService.getMultipathPreference(paramNetwork);
      return i;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  public NetworkCapabilities getNetworkCapabilities(Network paramNetwork)
  {
    try
    {
      paramNetwork = mService.getNetworkCapabilities(paramNetwork);
      return paramNetwork;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public Network getNetworkForType(int paramInt)
  {
    try
    {
      Network localNetwork = mService.getNetworkForType(paramInt);
      return localNetwork;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public NetworkInfo getNetworkInfo(int paramInt)
  {
    try
    {
      NetworkInfo localNetworkInfo = mService.getNetworkInfo(paramInt);
      return localNetworkInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public NetworkInfo getNetworkInfo(Network paramNetwork)
  {
    return getNetworkInfoForUid(paramNetwork, Process.myUid(), false);
  }
  
  public NetworkInfo getNetworkInfoForUid(Network paramNetwork, int paramInt, boolean paramBoolean)
  {
    try
    {
      paramNetwork = mService.getNetworkInfoForUid(paramNetwork, paramInt, paramBoolean);
      return paramNetwork;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public int getNetworkPreference()
  {
    return -1;
  }
  
  public byte[] getNetworkWatchlistConfigHash()
  {
    try
    {
      byte[] arrayOfByte = mService.getNetworkWatchlistConfigHash();
      return arrayOfByte;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("ConnectivityManager", "Unable to get watchlist config hash");
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ProxyInfo getProxyForNetwork(Network paramNetwork)
  {
    try
    {
      paramNetwork = mService.getProxyForNetwork(paramNetwork);
      return paramNetwork;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  public int getRestrictBackgroundStatus()
  {
    try
    {
      int i = getNetworkPolicyManager().getRestrictBackgroundByCaller();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getTetherableBluetoothRegexs()
  {
    try
    {
      String[] arrayOfString = mService.getTetherableBluetoothRegexs();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getTetherableIfaces()
  {
    try
    {
      String[] arrayOfString = mService.getTetherableIfaces();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getTetherableUsbRegexs()
  {
    try
    {
      String[] arrayOfString = mService.getTetherableUsbRegexs();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getTetherableWifiRegexs()
  {
    try
    {
      String[] arrayOfString = mService.getTetherableWifiRegexs();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getTetheredDhcpRanges()
  {
    try
    {
      String[] arrayOfString = mService.getTetheredDhcpRanges();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getTetheredIfaces()
  {
    try
    {
      String[] arrayOfString = mService.getTetheredIfaces();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public String[] getTetheringErroredIfaces()
  {
    try
    {
      String[] arrayOfString = mService.getTetheringErroredIfaces();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isActiveNetworkMetered()
  {
    try
    {
      boolean bool = mService.isActiveNetworkMetered();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isAlwaysOnVpnPackageSupportedForUser(int paramInt, String paramString)
  {
    try
    {
      boolean bool = mService.isAlwaysOnVpnPackageSupported(paramInt, paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isDefaultNetworkActive()
  {
    try
    {
      boolean bool = getNetworkManagementService().isNetworkActive();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean isNetworkSupported(int paramInt)
  {
    try
    {
      boolean bool = mService.isNetworkSupported(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean isTetheringSupported()
  {
    String str = mContext.getOpPackageName();
    try
    {
      boolean bool = mService.isTetheringSupported(str);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
    catch (SecurityException localSecurityException) {}
    return false;
  }
  
  public void registerDefaultNetworkCallback(NetworkCallback paramNetworkCallback)
  {
    registerDefaultNetworkCallback(paramNetworkCallback, getDefaultHandler());
  }
  
  public void registerDefaultNetworkCallback(NetworkCallback paramNetworkCallback, Handler paramHandler)
  {
    sendRequestForNetwork(null, paramNetworkCallback, 0, 2, -1, new CallbackHandler(paramHandler));
  }
  
  public int registerNetworkAgent(Messenger paramMessenger, NetworkInfo paramNetworkInfo, LinkProperties paramLinkProperties, NetworkCapabilities paramNetworkCapabilities, int paramInt, NetworkMisc paramNetworkMisc)
  {
    try
    {
      paramInt = mService.registerNetworkAgent(paramMessenger, paramNetworkInfo, paramLinkProperties, paramNetworkCapabilities, paramInt, paramNetworkMisc);
      return paramInt;
    }
    catch (RemoteException paramMessenger)
    {
      throw paramMessenger.rethrowFromSystemServer();
    }
  }
  
  public void registerNetworkCallback(NetworkRequest paramNetworkRequest, PendingIntent paramPendingIntent)
  {
    checkPendingIntentNotNull(paramPendingIntent);
    try
    {
      mService.pendingListenForNetwork(networkCapabilities, paramPendingIntent);
      return;
    }
    catch (ServiceSpecificException paramNetworkRequest)
    {
      throw convertServiceException(paramNetworkRequest);
    }
    catch (RemoteException paramNetworkRequest)
    {
      throw paramNetworkRequest.rethrowFromSystemServer();
    }
  }
  
  public void registerNetworkCallback(NetworkRequest paramNetworkRequest, NetworkCallback paramNetworkCallback)
  {
    registerNetworkCallback(paramNetworkRequest, paramNetworkCallback, getDefaultHandler());
  }
  
  public void registerNetworkCallback(NetworkRequest paramNetworkRequest, NetworkCallback paramNetworkCallback, Handler paramHandler)
  {
    paramHandler = new CallbackHandler(paramHandler);
    sendRequestForNetwork(networkCapabilities, paramNetworkCallback, 0, 1, -1, paramHandler);
  }
  
  public void registerNetworkFactory(Messenger paramMessenger, String paramString)
  {
    try
    {
      mService.registerNetworkFactory(paramMessenger, paramString);
      return;
    }
    catch (RemoteException paramMessenger)
    {
      throw paramMessenger.rethrowFromSystemServer();
    }
  }
  
  public void releaseNetworkRequest(PendingIntent paramPendingIntent)
  {
    checkPendingIntentNotNull(paramPendingIntent);
    try
    {
      mService.releasePendingNetworkRequest(paramPendingIntent);
      return;
    }
    catch (RemoteException paramPendingIntent)
    {
      throw paramPendingIntent.rethrowFromSystemServer();
    }
  }
  
  public void removeDefaultNetworkActiveListener(OnNetworkActiveListener paramOnNetworkActiveListener)
  {
    paramOnNetworkActiveListener = (INetworkActivityListener)mNetworkActivityListeners.get(paramOnNetworkActiveListener);
    boolean bool;
    if (paramOnNetworkActiveListener != null) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "Listener was not registered.");
    try
    {
      getNetworkManagementService().unregisterNetworkActivityListener(paramOnNetworkActiveListener);
      return;
    }
    catch (RemoteException paramOnNetworkActiveListener)
    {
      throw paramOnNetworkActiveListener.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void reportBadNetwork(Network paramNetwork)
  {
    try
    {
      mService.reportNetworkConnectivity(paramNetwork, true);
      mService.reportNetworkConnectivity(paramNetwork, false);
      return;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  public void reportInetCondition(int paramInt1, int paramInt2)
  {
    try
    {
      mService.reportInetCondition(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void reportNetworkConnectivity(Network paramNetwork, boolean paramBoolean)
  {
    try
    {
      mService.reportNetworkConnectivity(paramNetwork, paramBoolean);
      return;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  public boolean requestBandwidthUpdate(Network paramNetwork)
  {
    try
    {
      boolean bool = mService.requestBandwidthUpdate(paramNetwork);
      return bool;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  public void requestNetwork(NetworkRequest paramNetworkRequest, PendingIntent paramPendingIntent)
  {
    checkPendingIntentNotNull(paramPendingIntent);
    try
    {
      mService.pendingRequestForNetwork(networkCapabilities, paramPendingIntent);
      return;
    }
    catch (ServiceSpecificException paramNetworkRequest)
    {
      throw convertServiceException(paramNetworkRequest);
    }
    catch (RemoteException paramNetworkRequest)
    {
      throw paramNetworkRequest.rethrowFromSystemServer();
    }
  }
  
  public void requestNetwork(NetworkRequest paramNetworkRequest, NetworkCallback paramNetworkCallback)
  {
    requestNetwork(paramNetworkRequest, paramNetworkCallback, getDefaultHandler());
  }
  
  public void requestNetwork(NetworkRequest paramNetworkRequest, NetworkCallback paramNetworkCallback, int paramInt)
  {
    checkTimeout(paramInt);
    requestNetwork(paramNetworkRequest, paramNetworkCallback, paramInt, inferLegacyTypeForNetworkCapabilities(networkCapabilities), getDefaultHandler());
  }
  
  public void requestNetwork(NetworkRequest paramNetworkRequest, NetworkCallback paramNetworkCallback, int paramInt1, int paramInt2, Handler paramHandler)
  {
    paramHandler = new CallbackHandler(paramHandler);
    sendRequestForNetwork(networkCapabilities, paramNetworkCallback, paramInt1, 2, paramInt2, paramHandler);
  }
  
  public void requestNetwork(NetworkRequest paramNetworkRequest, NetworkCallback paramNetworkCallback, Handler paramHandler)
  {
    requestNetwork(paramNetworkRequest, paramNetworkCallback, 0, inferLegacyTypeForNetworkCapabilities(networkCapabilities), new CallbackHandler(paramHandler));
  }
  
  public void requestNetwork(NetworkRequest paramNetworkRequest, NetworkCallback paramNetworkCallback, Handler paramHandler, int paramInt)
  {
    checkTimeout(paramInt);
    requestNetwork(paramNetworkRequest, paramNetworkCallback, paramInt, inferLegacyTypeForNetworkCapabilities(networkCapabilities), new CallbackHandler(paramHandler));
  }
  
  @Deprecated
  public boolean requestRouteToHost(int paramInt1, int paramInt2)
  {
    return requestRouteToHostAddress(paramInt1, NetworkUtils.intToInetAddress(paramInt2));
  }
  
  @Deprecated
  public boolean requestRouteToHostAddress(int paramInt, InetAddress paramInetAddress)
  {
    checkLegacyRoutingApiAccess();
    try
    {
      boolean bool = mService.requestRouteToHostAddress(paramInt, paramInetAddress.getAddress());
      return bool;
    }
    catch (RemoteException paramInetAddress)
    {
      throw paramInetAddress.rethrowFromSystemServer();
    }
  }
  
  public void setAcceptUnvalidated(Network paramNetwork, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      mService.setAcceptUnvalidated(paramNetwork, paramBoolean1, paramBoolean2);
      return;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  public void setAirplaneMode(boolean paramBoolean)
  {
    try
    {
      mService.setAirplaneMode(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean setAlwaysOnVpnPackageForUser(int paramInt, String paramString, boolean paramBoolean)
  {
    try
    {
      paramBoolean = mService.setAlwaysOnVpnPackage(paramInt, paramString, paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setAvoidUnvalidated(Network paramNetwork)
  {
    try
    {
      mService.setAvoidUnvalidated(paramNetwork);
      return;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setBackgroundDataSetting(boolean paramBoolean) {}
  
  public void setGlobalProxy(ProxyInfo paramProxyInfo)
  {
    try
    {
      mService.setGlobalProxy(paramProxyInfo);
      return;
    }
    catch (RemoteException paramProxyInfo)
    {
      throw paramProxyInfo.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void setNetworkPreference(int paramInt) {}
  
  @Deprecated
  public void setProvisioningNotificationVisible(boolean paramBoolean, int paramInt, String paramString)
  {
    try
    {
      mService.setProvisioningNotificationVisible(paramBoolean, paramInt, paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int setUsbTethering(boolean paramBoolean)
  {
    try
    {
      String str = mContext.getOpPackageName();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("setUsbTethering caller:");
      localStringBuilder.append(str);
      Log.i("ConnectivityManager", localStringBuilder.toString());
      int i = mService.setUsbTethering(paramBoolean, str);
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void startCaptivePortalApp(Network paramNetwork)
  {
    try
    {
      mService.startCaptivePortalApp(paramNetwork);
      return;
    }
    catch (RemoteException paramNetwork)
    {
      throw paramNetwork.rethrowFromSystemServer();
    }
  }
  
  public PacketKeepalive startNattKeepalive(Network paramNetwork, int paramInt1, PacketKeepaliveCallback paramPacketKeepaliveCallback, InetAddress paramInetAddress1, int paramInt2, InetAddress paramInetAddress2)
  {
    paramPacketKeepaliveCallback = new PacketKeepalive(paramNetwork, paramPacketKeepaliveCallback, null);
    try
    {
      IConnectivityManager localIConnectivityManager = mService;
      Messenger localMessenger = mMessenger;
      Binder localBinder = new android/os/Binder;
      localBinder.<init>();
      localIConnectivityManager.startNattKeepalive(paramNetwork, paramInt1, localMessenger, localBinder, paramInetAddress1.getHostAddress(), paramInt2, paramInetAddress2.getHostAddress());
      return paramPacketKeepaliveCallback;
    }
    catch (RemoteException paramNetwork)
    {
      Log.e("ConnectivityManager", "Error starting packet keepalive: ", paramNetwork);
      paramPacketKeepaliveCallback.stopLooper();
    }
    return null;
  }
  
  @SystemApi
  public void startTethering(int paramInt, boolean paramBoolean, OnStartTetheringCallback paramOnStartTetheringCallback)
  {
    startTethering(paramInt, paramBoolean, paramOnStartTetheringCallback, null);
  }
  
  @SystemApi
  public void startTethering(int paramInt, boolean paramBoolean, final OnStartTetheringCallback paramOnStartTetheringCallback, Handler paramHandler)
  {
    Preconditions.checkNotNull(paramOnStartTetheringCallback, "OnStartTetheringCallback cannot be null.");
    paramOnStartTetheringCallback = new ResultReceiver(paramHandler)
    {
      protected void onReceiveResult(int paramAnonymousInt, Bundle paramAnonymousBundle)
      {
        if (paramAnonymousInt == 0) {
          paramOnStartTetheringCallback.onTetheringStarted();
        } else {
          paramOnStartTetheringCallback.onTetheringFailed();
        }
      }
    };
    try
    {
      paramHandler = mContext.getOpPackageName();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("startTethering caller:");
      localStringBuilder.append(paramHandler);
      Log.i("ConnectivityManager", localStringBuilder.toString());
      mService.startTethering(paramInt, paramOnStartTetheringCallback, paramBoolean, paramHandler);
    }
    catch (RemoteException paramHandler)
    {
      Log.e("ConnectivityManager", "Exception trying to start tethering.", paramHandler);
      paramOnStartTetheringCallback.send(2, null);
    }
  }
  
  @Deprecated
  public int startUsingNetworkFeature(int paramInt, String arg2)
  {
    checkLegacyRoutingApiAccess();
    Object localObject1 = networkCapabilitiesForFeature(paramInt, ???);
    if (localObject1 == null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Can't satisfy startUsingNetworkFeature for ");
      ((StringBuilder)localObject1).append(paramInt);
      ((StringBuilder)localObject1).append(", ");
      ((StringBuilder)localObject1).append(???);
      Log.d("ConnectivityManager", ((StringBuilder)localObject1).toString());
      return 3;
    }
    synchronized (sLegacyRequests)
    {
      LegacyRequest localLegacyRequest = (LegacyRequest)sLegacyRequests.get(localObject1);
      if (localLegacyRequest != null)
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("renewing startUsingNetworkFeature request ");
        ((StringBuilder)localObject1).append(networkRequest);
        Log.d("ConnectivityManager", ((StringBuilder)localObject1).toString());
        renewRequestLocked(localLegacyRequest);
        if (currentNetwork != null) {
          return 0;
        }
        return 1;
      }
      localObject1 = requestNetworkForFeatureLocked((NetworkCapabilities)localObject1);
      if (localObject1 != null)
      {
        ??? = new StringBuilder();
        ???.append("starting startUsingNetworkFeature for request ");
        ???.append(localObject1);
        Log.d("ConnectivityManager", ???.toString());
        return 1;
      }
      Log.d("ConnectivityManager", " request Failed");
      return 3;
    }
  }
  
  @SystemApi
  public void stopTethering(int paramInt)
  {
    try
    {
      String str = mContext.getOpPackageName();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("stopTethering caller:");
      localStringBuilder.append(str);
      Log.i("ConnectivityManager", localStringBuilder.toString());
      mService.stopTethering(paramInt, str);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public int stopUsingNetworkFeature(int paramInt, String paramString)
  {
    checkLegacyRoutingApiAccess();
    Object localObject = networkCapabilitiesForFeature(paramInt, paramString);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can't satisfy stopUsingNetworkFeature for ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(paramString);
      Log.d("ConnectivityManager", ((StringBuilder)localObject).toString());
      return -1;
    }
    if (removeRequestForFeature((NetworkCapabilities)localObject))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("stopUsingNetworkFeature for ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(", ");
      ((StringBuilder)localObject).append(paramString);
      Log.d("ConnectivityManager", ((StringBuilder)localObject).toString());
    }
    return 1;
  }
  
  public int tether(String paramString)
  {
    try
    {
      String str = mContext.getOpPackageName();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("tether caller:");
      localStringBuilder.append(str);
      Log.i("ConnectivityManager", localStringBuilder.toString());
      int i = mService.tether(paramString, str);
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void unregisterNetworkCallback(PendingIntent paramPendingIntent)
  {
    checkPendingIntentNotNull(paramPendingIntent);
    releaseNetworkRequest(paramPendingIntent);
  }
  
  public void unregisterNetworkCallback(NetworkCallback paramNetworkCallback)
  {
    checkCallbackNotNull(paramNetworkCallback);
    Object localObject1 = new ArrayList();
    synchronized (sCallbacks)
    {
      Object localObject2 = networkRequest;
      boolean bool1 = false;
      if (localObject2 != null) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Preconditions.checkArgument(bool2, "NetworkCallback was not registered");
      boolean bool2 = bool1;
      if (networkRequest != ALREADY_UNREGISTERED) {
        bool2 = true;
      }
      Preconditions.checkArgument(bool2, "NetworkCallback was already unregistered");
      localObject2 = sCallbacks.entrySet().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        Map.Entry localEntry = (Map.Entry)((Iterator)localObject2).next();
        if (localEntry.getValue() == paramNetworkCallback) {
          ((List)localObject1).add((NetworkRequest)localEntry.getKey());
        }
      }
      localObject2 = ((List)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (NetworkRequest)((Iterator)localObject2).next();
        try
        {
          mService.releaseNetworkRequest((NetworkRequest)localObject1);
          sCallbacks.remove(localObject1);
        }
        catch (RemoteException paramNetworkCallback)
        {
          throw paramNetworkCallback.rethrowFromSystemServer();
        }
      }
      NetworkCallback.access$902(paramNetworkCallback, ALREADY_UNREGISTERED);
      return;
    }
  }
  
  public void unregisterNetworkFactory(Messenger paramMessenger)
  {
    try
    {
      mService.unregisterNetworkFactory(paramMessenger);
      return;
    }
    catch (RemoteException paramMessenger)
    {
      throw paramMessenger.rethrowFromSystemServer();
    }
  }
  
  public int untether(String paramString)
  {
    try
    {
      String str = mContext.getOpPackageName();
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("untether caller:");
      localStringBuilder.append(str);
      Log.i("ConnectivityManager", localStringBuilder.toString());
      int i = mService.untether(paramString, str);
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean updateLockdownVpn()
  {
    try
    {
      boolean bool = mService.updateLockdownVpn();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private class CallbackHandler
    extends Handler
  {
    private static final boolean DBG = false;
    private static final String TAG = "ConnectivityManager.CallbackHandler";
    
    CallbackHandler(Handler paramHandler)
    {
      this(((Handler)Preconditions.checkNotNull(paramHandler, "Handler cannot be null.")).getLooper());
    }
    
    CallbackHandler(Looper paramLooper)
    {
      super();
    }
    
    private <T> T getObject(Message paramMessage, Class<T> paramClass)
    {
      return paramMessage.getData().getParcelable(paramClass.getSimpleName());
    }
    
    public void handleMessage(Message paramMessage)
    {
      if (what == 524296)
      {
        ConnectivityManager.this.expireRequest((NetworkCapabilities)obj, arg1);
        return;
      }
      Object localObject1 = (NetworkRequest)getObject(paramMessage, NetworkRequest.class);
      Network localNetwork = (Network)getObject(paramMessage, Network.class);
      synchronized (ConnectivityManager.sCallbacks)
      {
        localObject1 = (ConnectivityManager.NetworkCallback)ConnectivityManager.sCallbacks.get(localObject1);
        if (localObject1 == null)
        {
          ??? = new StringBuilder();
          ((StringBuilder)???).append("callback not found for ");
          ((StringBuilder)???).append(ConnectivityManager.getCallbackName(what));
          ((StringBuilder)???).append(" message");
          Log.w("ConnectivityManager.CallbackHandler", ((StringBuilder)???).toString());
          return;
        }
        switch (what)
        {
        case 524296: 
        default: 
          break;
        case 524298: 
          ((ConnectivityManager.NetworkCallback)localObject1).onNetworkResumed(localNetwork);
          break;
        case 524297: 
          ((ConnectivityManager.NetworkCallback)localObject1).onNetworkSuspended(localNetwork);
          break;
        case 524295: 
          ((ConnectivityManager.NetworkCallback)localObject1).onLinkPropertiesChanged(localNetwork, (LinkProperties)getObject(paramMessage, LinkProperties.class));
          break;
        case 524294: 
          ((ConnectivityManager.NetworkCallback)localObject1).onCapabilitiesChanged(localNetwork, (NetworkCapabilities)getObject(paramMessage, NetworkCapabilities.class));
          break;
        case 524293: 
          ((ConnectivityManager.NetworkCallback)localObject1).onUnavailable();
          break;
        case 524292: 
          ((ConnectivityManager.NetworkCallback)localObject1).onLost(localNetwork);
          break;
        case 524291: 
          ((ConnectivityManager.NetworkCallback)localObject1).onLosing(localNetwork, arg1);
          break;
        case 524290: 
          ((ConnectivityManager.NetworkCallback)localObject1).onAvailable(localNetwork, (NetworkCapabilities)getObject(paramMessage, NetworkCapabilities.class), (LinkProperties)getObject(paramMessage, LinkProperties.class));
          break;
        case 524289: 
          ((ConnectivityManager.NetworkCallback)localObject1).onPreCheck(localNetwork);
        }
        return;
      }
    }
  }
  
  public static abstract interface Errors
  {
    public static final int TOO_MANY_REQUESTS = 1;
  }
  
  private static class LegacyRequest
  {
    Network currentNetwork;
    int delay = -1;
    int expireSequenceNumber;
    ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback()
    {
      public void onAvailable(Network paramAnonymousNetwork)
      {
        currentNetwork = paramAnonymousNetwork;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("startUsingNetworkFeature got Network:");
        localStringBuilder.append(paramAnonymousNetwork);
        Log.d("ConnectivityManager", localStringBuilder.toString());
        ConnectivityManager.setProcessDefaultNetworkForHostResolution(paramAnonymousNetwork);
      }
      
      public void onLost(Network paramAnonymousNetwork)
      {
        if (paramAnonymousNetwork.equals(currentNetwork)) {
          ConnectivityManager.LegacyRequest.this.clearDnsBinding();
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("startUsingNetworkFeature lost Network:");
        localStringBuilder.append(paramAnonymousNetwork);
        Log.d("ConnectivityManager", localStringBuilder.toString());
      }
    };
    NetworkCapabilities networkCapabilities;
    NetworkRequest networkRequest;
    
    private LegacyRequest() {}
    
    private void clearDnsBinding()
    {
      if (currentNetwork != null)
      {
        currentNetwork = null;
        ConnectivityManager.setProcessDefaultNetworkForHostResolution(null);
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MultipathPreference {}
  
  public static class NetworkCallback
  {
    private NetworkRequest networkRequest;
    
    public NetworkCallback() {}
    
    public void onAvailable(Network paramNetwork) {}
    
    public void onAvailable(Network paramNetwork, NetworkCapabilities paramNetworkCapabilities, LinkProperties paramLinkProperties)
    {
      onAvailable(paramNetwork);
      if (!paramNetworkCapabilities.hasCapability(21)) {
        onNetworkSuspended(paramNetwork);
      }
      onCapabilitiesChanged(paramNetwork, paramNetworkCapabilities);
      onLinkPropertiesChanged(paramNetwork, paramLinkProperties);
    }
    
    public void onCapabilitiesChanged(Network paramNetwork, NetworkCapabilities paramNetworkCapabilities) {}
    
    public void onLinkPropertiesChanged(Network paramNetwork, LinkProperties paramLinkProperties) {}
    
    public void onLosing(Network paramNetwork, int paramInt) {}
    
    public void onLost(Network paramNetwork) {}
    
    public void onNetworkResumed(Network paramNetwork) {}
    
    public void onNetworkSuspended(Network paramNetwork) {}
    
    public void onPreCheck(Network paramNetwork) {}
    
    public void onUnavailable() {}
  }
  
  public static abstract interface OnNetworkActiveListener
  {
    public abstract void onNetworkActive();
  }
  
  @SystemApi
  public static abstract class OnStartTetheringCallback
  {
    public OnStartTetheringCallback() {}
    
    public void onTetheringFailed() {}
    
    public void onTetheringStarted() {}
  }
  
  public class PacketKeepalive
  {
    public static final int BINDER_DIED = -10;
    public static final int ERROR_HARDWARE_ERROR = -31;
    public static final int ERROR_HARDWARE_UNSUPPORTED = -30;
    public static final int ERROR_INVALID_INTERVAL = -24;
    public static final int ERROR_INVALID_IP_ADDRESS = -21;
    public static final int ERROR_INVALID_LENGTH = -23;
    public static final int ERROR_INVALID_NETWORK = -20;
    public static final int ERROR_INVALID_PORT = -22;
    public static final int MIN_INTERVAL = 10;
    public static final int NATT_PORT = 4500;
    public static final int NO_KEEPALIVE = -1;
    public static final int SUCCESS = 0;
    private static final String TAG = "PacketKeepalive";
    private final ConnectivityManager.PacketKeepaliveCallback mCallback;
    private final Looper mLooper;
    private final Messenger mMessenger;
    private final Network mNetwork;
    private volatile Integer mSlot;
    
    private PacketKeepalive(Network paramNetwork, ConnectivityManager.PacketKeepaliveCallback paramPacketKeepaliveCallback)
    {
      Preconditions.checkNotNull(paramNetwork, "network cannot be null");
      Preconditions.checkNotNull(paramPacketKeepaliveCallback, "callback cannot be null");
      mNetwork = paramNetwork;
      mCallback = paramPacketKeepaliveCallback;
      paramNetwork = new HandlerThread("PacketKeepalive");
      paramNetwork.start();
      mLooper = paramNetwork.getLooper();
      mMessenger = new Messenger(new Handler(mLooper)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          StringBuilder localStringBuilder;
          if (what != 528397)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Unhandled message ");
            localStringBuilder.append(Integer.toHexString(what));
            Log.e("PacketKeepalive", localStringBuilder.toString());
          }
          else
          {
            int i = arg2;
            if (i == 0) {
              try
              {
                if (mSlot == null)
                {
                  ConnectivityManager.PacketKeepalive.access$302(ConnectivityManager.PacketKeepalive.this, Integer.valueOf(arg1));
                  mCallback.onStarted();
                }
                else
                {
                  ConnectivityManager.PacketKeepalive.access$302(ConnectivityManager.PacketKeepalive.this, null);
                  stopLooper();
                  mCallback.onStopped();
                }
              }
              catch (Exception paramAnonymousMessage)
              {
                break label150;
              }
            }
            stopLooper();
            mCallback.onError(i);
            return;
            label150:
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Exception in keepalive callback(");
            localStringBuilder.append(i);
            localStringBuilder.append(")");
            Log.e("PacketKeepalive", localStringBuilder.toString(), paramAnonymousMessage);
          }
        }
      });
    }
    
    public void stop()
    {
      try
      {
        mService.stopKeepalive(mNetwork, mSlot.intValue());
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("PacketKeepalive", "Error stopping packet keepalive: ", localRemoteException);
        stopLooper();
      }
    }
    
    void stopLooper()
    {
      mLooper.quit();
    }
  }
  
  public static class PacketKeepaliveCallback
  {
    public PacketKeepaliveCallback() {}
    
    public void onError(int paramInt) {}
    
    public void onStarted() {}
    
    public void onStopped() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RestrictBackgroundStatus {}
  
  public static class TooManyRequestsException
    extends RuntimeException
  {
    public TooManyRequestsException() {}
  }
}
