package android.os;

import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BinderInternal;
import com.android.internal.util.StatLogger;
import java.util.HashMap;
import java.util.Map;

public final class ServiceManager
{
  private static final int GET_SERVICE_LOG_EVERY_CALLS_CORE = SystemProperties.getInt("debug.servicemanager.log_calls_core", 100);
  private static final int GET_SERVICE_LOG_EVERY_CALLS_NON_CORE = SystemProperties.getInt("debug.servicemanager.log_calls", 200);
  private static final long GET_SERVICE_SLOW_THRESHOLD_US_CORE;
  private static final long GET_SERVICE_SLOW_THRESHOLD_US_NON_CORE;
  private static final int SLOW_LOG_INTERVAL_MS = 5000;
  private static final int STATS_LOG_INTERVAL_MS = 5000;
  private static final String TAG = "ServiceManager";
  private static HashMap<String, IBinder> sCache;
  @GuardedBy("sLock")
  private static int sGetServiceAccumulatedCallCount;
  @GuardedBy("sLock")
  private static int sGetServiceAccumulatedUs;
  @GuardedBy("sLock")
  private static long sLastSlowLogActualTime;
  @GuardedBy("sLock")
  private static long sLastSlowLogUptime;
  @GuardedBy("sLock")
  private static long sLastStatsLogUptime;
  private static final Object sLock = new Object();
  private static IServiceManager sServiceManager;
  public static final StatLogger sStatLogger = new StatLogger(new String[] { "getService()" });
  
  static
  {
    sCache = new HashMap();
    GET_SERVICE_SLOW_THRESHOLD_US_CORE = SystemProperties.getInt("debug.servicemanager.slow_call_core_ms", 10) * 1000;
    GET_SERVICE_SLOW_THRESHOLD_US_NON_CORE = SystemProperties.getInt("debug.servicemanager.slow_call_ms", 50) * 1000;
  }
  
  public ServiceManager() {}
  
  public static void addService(String paramString, IBinder paramIBinder)
  {
    addService(paramString, paramIBinder, false, 8);
  }
  
  public static void addService(String paramString, IBinder paramIBinder, boolean paramBoolean)
  {
    addService(paramString, paramIBinder, paramBoolean, 8);
  }
  
  public static void addService(String paramString, IBinder paramIBinder, boolean paramBoolean, int paramInt)
  {
    try
    {
      getIServiceManager().addService(paramString, paramIBinder, paramBoolean, paramInt);
    }
    catch (RemoteException paramString)
    {
      Log.e("ServiceManager", "error in addService", paramString);
    }
  }
  
  public static IBinder checkService(String paramString)
  {
    try
    {
      IBinder localIBinder = (IBinder)sCache.get(paramString);
      if (localIBinder != null) {
        return localIBinder;
      }
      paramString = Binder.allowBlocking(getIServiceManager().checkService(paramString));
      return paramString;
    }
    catch (RemoteException paramString)
    {
      Log.e("ServiceManager", "error in checkService", paramString);
    }
    return null;
  }
  
  private static IServiceManager getIServiceManager()
  {
    if (sServiceManager != null) {
      return sServiceManager;
    }
    sServiceManager = ServiceManagerNative.asInterface(Binder.allowBlocking(BinderInternal.getContextObject()));
    return sServiceManager;
  }
  
  public static IBinder getService(String paramString)
  {
    try
    {
      IBinder localIBinder = (IBinder)sCache.get(paramString);
      if (localIBinder != null) {
        return localIBinder;
      }
      paramString = Binder.allowBlocking(rawGetService(paramString));
      return paramString;
    }
    catch (RemoteException paramString)
    {
      Log.e("ServiceManager", "error in getService", paramString);
    }
    return null;
  }
  
  public static IBinder getServiceOrThrow(String paramString)
    throws ServiceManager.ServiceNotFoundException
  {
    IBinder localIBinder = getService(paramString);
    if (localIBinder != null) {
      return localIBinder;
    }
    throw new ServiceNotFoundException(paramString);
  }
  
  public static void initServiceCache(Map<String, IBinder> paramMap)
  {
    if (sCache.size() == 0)
    {
      sCache.putAll(paramMap);
      return;
    }
    throw new IllegalStateException("setServiceCache may only be called once");
  }
  
  public static String[] listServices()
  {
    try
    {
      String[] arrayOfString = getIServiceManager().listServices(15);
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("ServiceManager", "error in listServices", localRemoteException);
    }
    return null;
  }
  
  private static IBinder rawGetService(String paramString)
    throws RemoteException
  {
    long l1 = sStatLogger.getTime();
    IBinder localIBinder = getIServiceManager().getService(paramString);
    int i = (int)sStatLogger.logDurationStat(0, l1);
    boolean bool = UserHandle.isCore(Process.myUid());
    if (bool) {
      l1 = GET_SERVICE_SLOW_THRESHOLD_US_CORE;
    } else {
      l1 = GET_SERVICE_SLOW_THRESHOLD_US_NON_CORE;
    }
    long l2;
    int j;
    int k;
    synchronized (sLock)
    {
      sGetServiceAccumulatedUs += i;
      sGetServiceAccumulatedCallCount += 1;
      l2 = SystemClock.uptimeMillis();
      if (i >= l1) {
        try
        {
          if ((l2 > sLastSlowLogUptime + 5000L) || (sLastSlowLogActualTime < i))
          {
            EventLogTags.writeServiceManagerSlow(i / 1000, paramString);
            sLastSlowLogUptime = l2;
            sLastSlowLogActualTime = i;
          }
        }
        finally
        {
          break label236;
        }
      }
      if (bool) {
        i = GET_SERVICE_LOG_EVERY_CALLS_CORE;
      } else {
        i = GET_SERVICE_LOG_EVERY_CALLS_NON_CORE;
      }
      if ((sGetServiceAccumulatedCallCount >= i) && (l2 >= sLastStatsLogUptime + 5000L))
      {
        j = sGetServiceAccumulatedCallCount;
        i = sGetServiceAccumulatedUs / 1000;
        l1 = sLastStatsLogUptime;
        k = (int)(l2 - l1);
      }
    }
    try
    {
      EventLogTags.writeServiceManagerStats(j, i, k);
      sGetServiceAccumulatedCallCount = 0;
      sGetServiceAccumulatedUs = 0;
      sLastStatsLogUptime = l2;
      return localIBinder;
    }
    finally
    {
      for (;;) {}
    }
    paramString = finally;
    label236:
    throw paramString;
  }
  
  public static class ServiceNotFoundException
    extends Exception
  {
    public ServiceNotFoundException(String paramString)
    {
      super();
    }
  }
  
  static abstract interface Stats
  {
    public static final int COUNT = 1;
    public static final int GET_SERVICE = 0;
  }
}
