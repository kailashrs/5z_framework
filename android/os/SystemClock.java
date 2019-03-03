package android.os;

import android.app.IAlarmManager;
import android.app.IAlarmManager.Stub;
import android.util.Slog;
import dalvik.annotation.optimization.CriticalNative;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;

public final class SystemClock
{
  private static final String TAG = "SystemClock";
  
  private SystemClock() {}
  
  public static Clock currentNetworkTimeClock()
  {
    new SimpleClock(ZoneOffset.UTC)
    {
      public long millis()
      {
        return SystemClock.currentNetworkTimeMillis();
      }
    };
  }
  
  public static long currentNetworkTimeMillis()
  {
    IAlarmManager localIAlarmManager = IAlarmManager.Stub.asInterface(ServiceManager.getService("alarm"));
    if (localIAlarmManager != null) {
      try
      {
        long l = localIAlarmManager.currentNetworkTimeMillis();
        return l;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
      catch (ParcelableException localParcelableException)
      {
        localParcelableException.maybeRethrow(DateTimeException.class);
        throw new RuntimeException(localParcelableException);
      }
    }
    throw new RuntimeException(new DeadSystemException());
  }
  
  @CriticalNative
  public static native long currentThreadTimeMicro();
  
  @CriticalNative
  public static native long currentThreadTimeMillis();
  
  @CriticalNative
  public static native long currentTimeMicro();
  
  @CriticalNative
  public static native long elapsedRealtime();
  
  public static Clock elapsedRealtimeClock()
  {
    new SimpleClock(ZoneOffset.UTC)
    {
      public long millis()
      {
        return SystemClock.elapsedRealtime();
      }
    };
  }
  
  @CriticalNative
  public static native long elapsedRealtimeNanos();
  
  public static boolean setCurrentTimeMillis(long paramLong)
  {
    IAlarmManager localIAlarmManager = IAlarmManager.Stub.asInterface(ServiceManager.getService("alarm"));
    if (localIAlarmManager == null) {
      return false;
    }
    try
    {
      boolean bool = localIAlarmManager.setTime(paramLong);
      return bool;
    }
    catch (SecurityException localSecurityException)
    {
      Slog.e("SystemClock", "Unable to set RTC", localSecurityException);
    }
    catch (RemoteException localRemoteException)
    {
      Slog.e("SystemClock", "Unable to set RTC", localRemoteException);
    }
    return false;
  }
  
  public static void sleep(long paramLong)
  {
    long l1 = uptimeMillis();
    long l2 = paramLong;
    int i = 0;
    int j;
    long l3;
    do
    {
      try
      {
        Thread.sleep(l2);
        j = i;
      }
      catch (InterruptedException localInterruptedException)
      {
        j = 1;
      }
      l3 = l1 + paramLong - uptimeMillis();
      l2 = l3;
      i = j;
    } while (l3 > 0L);
    if (j != 0) {
      Thread.currentThread().interrupt();
    }
  }
  
  public static Clock uptimeClock()
  {
    new SimpleClock(ZoneOffset.UTC)
    {
      public long millis()
      {
        return SystemClock.uptimeMillis();
      }
    };
  }
  
  @CriticalNative
  public static native long uptimeMillis();
  
  @Deprecated
  public static Clock uptimeMillisClock()
  {
    return uptimeClock();
  }
}
