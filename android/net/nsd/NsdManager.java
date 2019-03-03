package android.net.nsd;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.AsyncChannel;
import com.android.internal.util.Preconditions;
import java.util.concurrent.CountDownLatch;

public final class NsdManager
{
  public static final String ACTION_NSD_STATE_CHANGED = "android.net.nsd.STATE_CHANGED";
  private static final int BASE = 393216;
  private static final boolean DBG = false;
  public static final int DISABLE = 393241;
  public static final int DISCOVER_SERVICES = 393217;
  public static final int DISCOVER_SERVICES_FAILED = 393219;
  public static final int DISCOVER_SERVICES_STARTED = 393218;
  public static final int ENABLE = 393240;
  private static final SparseArray<String> EVENT_NAMES;
  public static final String EXTRA_NSD_STATE = "nsd_state";
  public static final int FAILURE_ALREADY_ACTIVE = 3;
  public static final int FAILURE_INTERNAL_ERROR = 0;
  public static final int FAILURE_MAX_LIMIT = 4;
  private static final int FIRST_LISTENER_KEY = 1;
  public static final int NATIVE_DAEMON_EVENT = 393242;
  public static final int NSD_STATE_DISABLED = 1;
  public static final int NSD_STATE_ENABLED = 2;
  public static final int PROTOCOL_DNS_SD = 1;
  public static final int REGISTER_SERVICE = 393225;
  public static final int REGISTER_SERVICE_FAILED = 393226;
  public static final int REGISTER_SERVICE_SUCCEEDED = 393227;
  public static final int RESOLVE_SERVICE = 393234;
  public static final int RESOLVE_SERVICE_FAILED = 393235;
  public static final int RESOLVE_SERVICE_SUCCEEDED = 393236;
  public static final int SERVICE_FOUND = 393220;
  public static final int SERVICE_LOST = 393221;
  public static final int STOP_DISCOVERY = 393222;
  public static final int STOP_DISCOVERY_FAILED = 393223;
  public static final int STOP_DISCOVERY_SUCCEEDED = 393224;
  private static final String TAG = NsdManager.class.getSimpleName();
  public static final int UNREGISTER_SERVICE = 393228;
  public static final int UNREGISTER_SERVICE_FAILED = 393229;
  public static final int UNREGISTER_SERVICE_SUCCEEDED = 393230;
  private final AsyncChannel mAsyncChannel = new AsyncChannel();
  private final CountDownLatch mConnected = new CountDownLatch(1);
  private final Context mContext;
  private ServiceHandler mHandler;
  private int mListenerKey = 1;
  private final SparseArray mListenerMap = new SparseArray();
  private final Object mMapLock = new Object();
  private final INsdManager mService;
  private final SparseArray<NsdServiceInfo> mServiceMap = new SparseArray();
  
  static
  {
    EVENT_NAMES = new SparseArray();
    EVENT_NAMES.put(393217, "DISCOVER_SERVICES");
    EVENT_NAMES.put(393218, "DISCOVER_SERVICES_STARTED");
    EVENT_NAMES.put(393219, "DISCOVER_SERVICES_FAILED");
    EVENT_NAMES.put(393220, "SERVICE_FOUND");
    EVENT_NAMES.put(393221, "SERVICE_LOST");
    EVENT_NAMES.put(393222, "STOP_DISCOVERY");
    EVENT_NAMES.put(393223, "STOP_DISCOVERY_FAILED");
    EVENT_NAMES.put(393224, "STOP_DISCOVERY_SUCCEEDED");
    EVENT_NAMES.put(393225, "REGISTER_SERVICE");
    EVENT_NAMES.put(393226, "REGISTER_SERVICE_FAILED");
    EVENT_NAMES.put(393227, "REGISTER_SERVICE_SUCCEEDED");
    EVENT_NAMES.put(393228, "UNREGISTER_SERVICE");
    EVENT_NAMES.put(393229, "UNREGISTER_SERVICE_FAILED");
    EVENT_NAMES.put(393230, "UNREGISTER_SERVICE_SUCCEEDED");
    EVENT_NAMES.put(393234, "RESOLVE_SERVICE");
    EVENT_NAMES.put(393235, "RESOLVE_SERVICE_FAILED");
    EVENT_NAMES.put(393236, "RESOLVE_SERVICE_SUCCEEDED");
    EVENT_NAMES.put(393240, "ENABLE");
    EVENT_NAMES.put(393241, "DISABLE");
    EVENT_NAMES.put(393242, "NATIVE_DAEMON_EVENT");
  }
  
  public NsdManager(Context paramContext, INsdManager paramINsdManager)
  {
    mService = paramINsdManager;
    mContext = paramContext;
    init();
  }
  
  private static void checkListener(Object paramObject)
  {
    Preconditions.checkNotNull(paramObject, "listener cannot be null");
  }
  
  private static void checkProtocol(int paramInt)
  {
    boolean bool = true;
    if (paramInt != 1) {
      bool = false;
    }
    Preconditions.checkArgument(bool, "Unsupported protocol");
  }
  
  private static void checkServiceInfo(NsdServiceInfo paramNsdServiceInfo)
  {
    Preconditions.checkNotNull(paramNsdServiceInfo, "NsdServiceInfo cannot be null");
    Preconditions.checkStringNotEmpty(paramNsdServiceInfo.getServiceName(), "Service name cannot be empty");
    Preconditions.checkStringNotEmpty(paramNsdServiceInfo.getServiceType(), "Service type cannot be empty");
  }
  
  private static void fatal(String paramString)
  {
    Log.e(TAG, paramString);
    throw new RuntimeException(paramString);
  }
  
  private int getListenerKey(Object paramObject)
  {
    checkListener(paramObject);
    synchronized (mMapLock)
    {
      int i = mListenerMap.indexOfValue(paramObject);
      boolean bool;
      if (i != -1) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool, "listener not registered");
      i = mListenerMap.keyAt(i);
      return i;
    }
  }
  
  private Messenger getMessenger()
  {
    try
    {
      Messenger localMessenger = mService.getMessenger();
      return localMessenger;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private static String getNsdServiceInfoType(NsdServiceInfo paramNsdServiceInfo)
  {
    if (paramNsdServiceInfo == null) {
      return "?";
    }
    return paramNsdServiceInfo.getServiceType();
  }
  
  private void init()
  {
    Messenger localMessenger = getMessenger();
    if (localMessenger == null) {
      fatal("Failed to obtain service Messenger");
    }
    HandlerThread localHandlerThread = new HandlerThread("NsdManager");
    localHandlerThread.start();
    mHandler = new ServiceHandler(localHandlerThread.getLooper());
    mAsyncChannel.connect(mContext, mHandler, localMessenger);
    try
    {
      mConnected.await();
    }
    catch (InterruptedException localInterruptedException)
    {
      fatal("Interrupted wait at init");
    }
  }
  
  public static String nameOf(int paramInt)
  {
    String str = (String)EVENT_NAMES.get(paramInt);
    if (str == null) {
      return Integer.toString(paramInt);
    }
    return str;
  }
  
  private int nextListenerKey()
  {
    mListenerKey = Math.max(1, mListenerKey + 1);
    return mListenerKey;
  }
  
  private int putListener(Object paramObject, NsdServiceInfo paramNsdServiceInfo)
  {
    checkListener(paramObject);
    synchronized (mMapLock)
    {
      boolean bool;
      if (mListenerMap.indexOfValue(paramObject) == -1) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool, "listener already in use");
      int i = nextListenerKey();
      mListenerMap.put(i, paramObject);
      mServiceMap.put(i, paramNsdServiceInfo);
      return i;
    }
  }
  
  private void removeListener(int paramInt)
  {
    synchronized (mMapLock)
    {
      mListenerMap.remove(paramInt);
      mServiceMap.remove(paramInt);
      return;
    }
  }
  
  @VisibleForTesting
  public void disconnect()
  {
    mAsyncChannel.disconnect();
    mHandler.getLooper().quitSafely();
  }
  
  public void discoverServices(String paramString, int paramInt, DiscoveryListener paramDiscoveryListener)
  {
    Preconditions.checkStringNotEmpty(paramString, "Service type cannot be empty");
    checkProtocol(paramInt);
    NsdServiceInfo localNsdServiceInfo = new NsdServiceInfo();
    localNsdServiceInfo.setServiceType(paramString);
    paramInt = putListener(paramDiscoveryListener, localNsdServiceInfo);
    mAsyncChannel.sendMessage(393217, 0, paramInt, localNsdServiceInfo);
  }
  
  public void registerService(NsdServiceInfo paramNsdServiceInfo, int paramInt, RegistrationListener paramRegistrationListener)
  {
    boolean bool;
    if (paramNsdServiceInfo.getPort() > 0) {
      bool = true;
    } else {
      bool = false;
    }
    Preconditions.checkArgument(bool, "Invalid port number");
    checkServiceInfo(paramNsdServiceInfo);
    checkProtocol(paramInt);
    paramInt = putListener(paramRegistrationListener, paramNsdServiceInfo);
    mAsyncChannel.sendMessage(393225, 0, paramInt, paramNsdServiceInfo);
  }
  
  public void resolveService(NsdServiceInfo paramNsdServiceInfo, ResolveListener paramResolveListener)
  {
    checkServiceInfo(paramNsdServiceInfo);
    int i = putListener(paramResolveListener, paramNsdServiceInfo);
    mAsyncChannel.sendMessage(393234, 0, i, paramNsdServiceInfo);
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    try
    {
      mService.setEnabled(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void stopServiceDiscovery(DiscoveryListener paramDiscoveryListener)
  {
    int i = getListenerKey(paramDiscoveryListener);
    mAsyncChannel.sendMessage(393222, 0, i);
  }
  
  public void unregisterService(RegistrationListener paramRegistrationListener)
  {
    int i = getListenerKey(paramRegistrationListener);
    mAsyncChannel.sendMessage(393228, 0, i);
  }
  
  public static abstract interface DiscoveryListener
  {
    public abstract void onDiscoveryStarted(String paramString);
    
    public abstract void onDiscoveryStopped(String paramString);
    
    public abstract void onServiceFound(NsdServiceInfo paramNsdServiceInfo);
    
    public abstract void onServiceLost(NsdServiceInfo paramNsdServiceInfo);
    
    public abstract void onStartDiscoveryFailed(String paramString, int paramInt);
    
    public abstract void onStopDiscoveryFailed(String paramString, int paramInt);
  }
  
  public static abstract interface RegistrationListener
  {
    public abstract void onRegistrationFailed(NsdServiceInfo paramNsdServiceInfo, int paramInt);
    
    public abstract void onServiceRegistered(NsdServiceInfo paramNsdServiceInfo);
    
    public abstract void onServiceUnregistered(NsdServiceInfo paramNsdServiceInfo);
    
    public abstract void onUnregistrationFailed(NsdServiceInfo paramNsdServiceInfo, int paramInt);
  }
  
  public static abstract interface ResolveListener
  {
    public abstract void onResolveFailed(NsdServiceInfo paramNsdServiceInfo, int paramInt);
    
    public abstract void onServiceResolved(NsdServiceInfo paramNsdServiceInfo);
  }
  
  @VisibleForTesting
  class ServiceHandler
    extends Handler
  {
    ServiceHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      int j = arg2;
      if (i != 69632)
      {
        if (i != 69634)
        {
          if (i != 69636) {
            synchronized (mMapLock)
            {
              Object localObject2 = mListenerMap.get(j);
              NsdServiceInfo localNsdServiceInfo = (NsdServiceInfo)mServiceMap.get(j);
              if (localObject2 == null)
              {
                ??? = NsdManager.TAG;
                localObject2 = new StringBuilder();
                ((StringBuilder)localObject2).append("Stale key ");
                ((StringBuilder)localObject2).append(arg2);
                Log.d((String)???, ((StringBuilder)localObject2).toString());
                return;
              }
              switch (i)
              {
              case 393222: 
              case 393225: 
              case 393228: 
              case 393231: 
              case 393232: 
              case 393233: 
              case 393234: 
              default: 
                localObject2 = NsdManager.TAG;
                ??? = new StringBuilder();
                ((StringBuilder)???).append("Ignored ");
                ((StringBuilder)???).append(paramMessage);
                Log.d((String)localObject2, ((StringBuilder)???).toString());
                break;
              case 393236: 
                NsdManager.this.removeListener(j);
                ((NsdManager.ResolveListener)localObject2).onServiceResolved((NsdServiceInfo)obj);
                break;
              case 393235: 
                NsdManager.this.removeListener(j);
                ((NsdManager.ResolveListener)localObject2).onResolveFailed(localNsdServiceInfo, arg1);
                break;
              case 393230: 
                NsdManager.this.removeListener(arg2);
                ((NsdManager.RegistrationListener)localObject2).onServiceUnregistered(localNsdServiceInfo);
                break;
              case 393229: 
                NsdManager.this.removeListener(j);
                ((NsdManager.RegistrationListener)localObject2).onUnregistrationFailed(localNsdServiceInfo, arg1);
                break;
              case 393227: 
                ((NsdManager.RegistrationListener)localObject2).onServiceRegistered((NsdServiceInfo)obj);
                break;
              case 393226: 
                NsdManager.this.removeListener(j);
                ((NsdManager.RegistrationListener)localObject2).onRegistrationFailed(localNsdServiceInfo, arg1);
                break;
              case 393224: 
                NsdManager.this.removeListener(j);
                ((NsdManager.DiscoveryListener)localObject2).onDiscoveryStopped(NsdManager.getNsdServiceInfoType(localNsdServiceInfo));
                break;
              case 393223: 
                NsdManager.this.removeListener(j);
                ((NsdManager.DiscoveryListener)localObject2).onStopDiscoveryFailed(NsdManager.getNsdServiceInfoType(localNsdServiceInfo), arg1);
                break;
              case 393221: 
                ((NsdManager.DiscoveryListener)localObject2).onServiceLost((NsdServiceInfo)obj);
                break;
              case 393220: 
                ((NsdManager.DiscoveryListener)localObject2).onServiceFound((NsdServiceInfo)obj);
                break;
              case 393219: 
                NsdManager.this.removeListener(j);
                ((NsdManager.DiscoveryListener)localObject2).onStartDiscoveryFailed(NsdManager.getNsdServiceInfoType(localNsdServiceInfo), arg1);
                break;
              case 393218: 
                paramMessage = NsdManager.getNsdServiceInfoType((NsdServiceInfo)obj);
                ((NsdManager.DiscoveryListener)localObject2).onDiscoveryStarted(paramMessage);
              }
              return;
            }
          }
          Log.e(NsdManager.TAG, "Channel lost");
          return;
        }
        mConnected.countDown();
        return;
      }
      mAsyncChannel.sendMessage(69633);
    }
  }
}
