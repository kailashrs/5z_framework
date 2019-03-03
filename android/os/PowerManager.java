package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public final class PowerManager
{
  public static final int ACQUIRE_CAUSES_WAKEUP = 268435456;
  public static final String ACTION_DEVICE_IDLE_MODE_CHANGED = "android.os.action.DEVICE_IDLE_MODE_CHANGED";
  public static final String ACTION_LIGHT_DEVICE_IDLE_MODE_CHANGED = "android.os.action.LIGHT_DEVICE_IDLE_MODE_CHANGED";
  public static final String ACTION_POWER_SAVE_MODE_CHANGED = "android.os.action.POWER_SAVE_MODE_CHANGED";
  public static final String ACTION_POWER_SAVE_MODE_CHANGED_INTERNAL = "android.os.action.POWER_SAVE_MODE_CHANGED_INTERNAL";
  public static final String ACTION_POWER_SAVE_MODE_CHANGING = "android.os.action.POWER_SAVE_MODE_CHANGING";
  public static final String ACTION_POWER_SAVE_TEMP_WHITELIST_CHANGED = "android.os.action.POWER_SAVE_TEMP_WHITELIST_CHANGED";
  public static final String ACTION_POWER_SAVE_WHITELIST_CHANGED = "android.os.action.POWER_SAVE_WHITELIST_CHANGED";
  @SystemApi
  @Deprecated
  public static final String ACTION_SCREEN_BRIGHTNESS_BOOST_CHANGED = "android.os.action.SCREEN_BRIGHTNESS_BOOST_CHANGED";
  public static final int BRIGHTNESS_DEFAULT = -1;
  public static final int BRIGHTNESS_OFF = 0;
  public static final int BRIGHTNESS_ON = 255;
  public static final int DOZE_WAKE_LOCK = 64;
  public static final int DRAW_WAKE_LOCK = 128;
  public static final String EXTRA_POWER_SAVE_MODE = "mode";
  @Deprecated
  public static final int FULL_WAKE_LOCK = 26;
  public static final int GO_TO_SLEEP_FLAG_NO_DOZE = 1;
  public static final int GO_TO_SLEEP_REASON_ACCESSIBILITY = 7;
  public static final int GO_TO_SLEEP_REASON_APPLICATION = 0;
  public static final int GO_TO_SLEEP_REASON_DEVICE_ADMIN = 1;
  public static final int GO_TO_SLEEP_REASON_HDMI = 5;
  public static final int GO_TO_SLEEP_REASON_LID_SWITCH = 3;
  public static final int GO_TO_SLEEP_REASON_POWER_BUTTON = 4;
  public static final int GO_TO_SLEEP_REASON_SLEEP_BUTTON = 6;
  public static final int GO_TO_SLEEP_REASON_TIMEOUT = 2;
  public static final int GO_TO_SLEEP_REASON_TIMEOUT_POSTPONED = 100;
  public static final int LOCATION_MODE_ALL_DISABLED_WHEN_SCREEN_OFF = 2;
  public static final int LOCATION_MODE_FOREGROUND_ONLY = 3;
  public static final int LOCATION_MODE_GPS_DISABLED_WHEN_SCREEN_OFF = 1;
  public static final int LOCATION_MODE_NO_CHANGE = 0;
  public static final int ON_AFTER_RELEASE = 536870912;
  public static final int PARTIAL_WAKE_LOCK = 1;
  public static final int PROXIMITY_SCREEN_OFF_WAKE_LOCK = 32;
  public static final String REBOOT_QUIESCENT = "quiescent";
  public static final String REBOOT_RECOVERY = "recovery";
  public static final String REBOOT_RECOVERY_UPDATE = "recovery-update";
  public static final String REBOOT_REQUESTED_BY_DEVICE_OWNER = "deviceowner";
  public static final String REBOOT_SAFE_MODE = "safemode";
  public static final int RELEASE_FLAG_TIMEOUT = 65536;
  public static final int RELEASE_FLAG_WAIT_FOR_NO_PROXIMITY = 1;
  @Deprecated
  public static final int SCREEN_BRIGHT_WAKE_LOCK = 10;
  @Deprecated
  public static final int SCREEN_DIM_WAKE_LOCK = 6;
  public static final String SHUTDOWN_BATTERY_THERMAL_STATE = "thermal,battery";
  public static final String SHUTDOWN_LOW_BATTERY = "battery";
  public static final int SHUTDOWN_REASON_BATTERY_THERMAL = 6;
  public static final int SHUTDOWN_REASON_LOW_BATTERY = 5;
  public static final int SHUTDOWN_REASON_REBOOT = 2;
  public static final int SHUTDOWN_REASON_SHUTDOWN = 1;
  public static final int SHUTDOWN_REASON_THERMAL_SHUTDOWN = 4;
  public static final int SHUTDOWN_REASON_UNKNOWN = 0;
  public static final int SHUTDOWN_REASON_USER_REQUESTED = 3;
  public static final String SHUTDOWN_USER_REQUESTED = "userrequested";
  private static final String TAG = "PowerManager";
  public static final int UNIMPORTANT_FOR_LOGGING = 1073741824;
  @SystemApi
  public static final int USER_ACTIVITY_EVENT_ACCESSIBILITY = 3;
  @SystemApi
  public static final int USER_ACTIVITY_EVENT_BUTTON = 1;
  @SystemApi
  public static final int USER_ACTIVITY_EVENT_OTHER = 0;
  @SystemApi
  public static final int USER_ACTIVITY_EVENT_TOUCH = 2;
  @SystemApi
  public static final int USER_ACTIVITY_FLAG_INDIRECT = 2;
  @SystemApi
  public static final int USER_ACTIVITY_FLAG_NO_CHANGE_LIGHTS = 1;
  public static final int WAKE_LOCK_LEVEL_MASK = 65535;
  public static final int WAKE_UP_REASON_FINGERPRINT = 106;
  public static final int WAKE_UP_REASON_GESTURE = 104;
  public static final int WAKE_UP_REASON_INSTANTCAMERA = 105;
  public static final int WAKE_UP_REASON_LID_UNLID = 103;
  public static final int WAKE_UP_REASON_PLUGIN_UNPLUG = 102;
  public static final int WAKE_UP_REASON_POWER_BUTTON = 101;
  public static final int WAKE_UP_REASON_WINDOWMANAGER = 107;
  final Context mContext;
  final Handler mHandler;
  IDeviceIdleController mIDeviceIdleController;
  final IPowerManager mService;
  
  public PowerManager(Context paramContext, IPowerManager paramIPowerManager, Handler paramHandler)
  {
    mContext = paramContext;
    mService = paramIPowerManager;
    mHandler = paramHandler;
  }
  
  public static void validateWakeLockParameters(int paramInt, String paramString)
  {
    paramInt = 0xFFFF & paramInt;
    if ((paramInt != 1) && (paramInt != 6) && (paramInt != 10) && (paramInt != 26) && (paramInt != 32) && (paramInt != 64) && (paramInt != 128)) {
      throw new IllegalArgumentException("Must specify a valid wake lock level.");
    }
    if (paramString != null) {
      return;
    }
    throw new IllegalArgumentException("The tag must not be null.");
  }
  
  public void boostScreenBrightness(long paramLong)
  {
    try
    {
      mService.boostScreenBrightness(paramLong);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getDefaultScreenBrightnessForVrSetting()
  {
    return mContext.getResources().getInteger(17694864);
  }
  
  public int getDefaultScreenBrightnessSetting()
  {
    return mContext.getResources().getInteger(17694867);
  }
  
  public int getLastShutdownReason()
  {
    try
    {
      int i = mService.getLastShutdownReason();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getLocationPowerSaveMode()
  {
    PowerSaveState localPowerSaveState = getPowerSaveState(1);
    if (!globalBatterySaverEnabled) {
      return 0;
    }
    return gpsMode;
  }
  
  public int getMaximumScreenBrightnessForVrSetting()
  {
    return mContext.getResources().getInteger(17694865);
  }
  
  public int getMaximumScreenBrightnessSetting()
  {
    return mContext.getResources().getInteger(17694868);
  }
  
  public int getMinimumScreenBrightnessForVrSetting()
  {
    return mContext.getResources().getInteger(17694866);
  }
  
  public int getMinimumScreenBrightnessSetting()
  {
    return mContext.getResources().getInteger(17694869);
  }
  
  public PowerSaveState getPowerSaveState(int paramInt)
  {
    try
    {
      PowerSaveState localPowerSaveState = mService.getPowerSaveState(paramInt);
      return localPowerSaveState;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void goToSleep(long paramLong)
  {
    goToSleep(paramLong, 0, 0);
  }
  
  public void goToSleep(long paramLong, int paramInt1, int paramInt2)
  {
    try
    {
      mService.goToSleep(paramLong, paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isDeviceIdleMode()
  {
    try
    {
      boolean bool = mService.isDeviceIdleMode();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public boolean isIgnoringBatteryOptimizations(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 253	android/os/PowerManager:mIDeviceIdleController	Landroid/os/IDeviceIdleController;
    //   6: ifnonnull +15 -> 21
    //   9: aload_0
    //   10: ldc -1
    //   12: invokestatic 261	android/os/ServiceManager:getService	(Ljava/lang/String;)Landroid/os/IBinder;
    //   15: invokestatic 267	android/os/IDeviceIdleController$Stub:asInterface	(Landroid/os/IBinder;)Landroid/os/IDeviceIdleController;
    //   18: putfield 253	android/os/PowerManager:mIDeviceIdleController	Landroid/os/IDeviceIdleController;
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_0
    //   24: getfield 253	android/os/PowerManager:mIDeviceIdleController	Landroid/os/IDeviceIdleController;
    //   27: aload_1
    //   28: invokeinterface 272 2 0
    //   33: istore_2
    //   34: iload_2
    //   35: ireturn
    //   36: astore_1
    //   37: aload_1
    //   38: invokevirtual 197	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   41: athrow
    //   42: astore_1
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_1
    //   46: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	47	0	this	PowerManager
    //   0	47	1	paramString	String
    //   33	2	2	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   23	34	36	android/os/RemoteException
    //   2	21	42	finally
    //   21	23	42	finally
    //   43	45	42	finally
  }
  
  public boolean isInteractive()
  {
    try
    {
      boolean bool = mService.isInteractive();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isLightDeviceIdleMode()
  {
    try
    {
      boolean bool = mService.isLightDeviceIdleMode();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isPowerSaveMode()
  {
    try
    {
      boolean bool = mService.isPowerSaveMode();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  @Deprecated
  public boolean isScreenBrightnessBoosted()
  {
    return false;
  }
  
  @Deprecated
  public boolean isScreenOn()
  {
    return isInteractive();
  }
  
  public boolean isSustainedPerformanceModeSupported()
  {
    return mContext.getResources().getBoolean(17957053);
  }
  
  public boolean isWakeLockLevelSupported(int paramInt)
  {
    try
    {
      boolean bool = mService.isWakeLockLevelSupported(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void nap(long paramLong)
  {
    try
    {
      mService.nap(paramLong);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public WakeLock newWakeLock(int paramInt, String paramString)
  {
    validateWakeLockParameters(paramInt, paramString);
    return new WakeLock(paramInt, paramString, mContext.getOpPackageName());
  }
  
  public void reboot(String paramString)
  {
    try
    {
      mService.reboot(false, paramString, true);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void rebootSafeMode()
  {
    try
    {
      mService.rebootSafeMode(false, true);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setDozeAfterScreenOff(boolean paramBoolean)
  {
    try
    {
      mService.setDozeAfterScreenOff(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean setPowerSaveMode(boolean paramBoolean)
  {
    try
    {
      paramBoolean = mService.setPowerSaveMode(paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void shutdown(boolean paramBoolean1, String paramString, boolean paramBoolean2)
  {
    try
    {
      mService.shutdown(paramBoolean1, paramString, paramBoolean2);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void userActivity(long paramLong, int paramInt1, int paramInt2)
  {
    try
    {
      mService.userActivity(paramLong, paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void userActivity(long paramLong, boolean paramBoolean)
  {
    userActivity(paramLong, 0, paramBoolean);
  }
  
  public void wakeUp(long paramLong)
  {
    try
    {
      mService.wakeUp(paramLong, "wakeUp", mContext.getOpPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void wakeUp(long paramLong, String paramString)
  {
    try
    {
      mService.wakeUp(paramLong, paramString, mContext.getOpPackageName());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LocationPowerSaveMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ServiceType
  {
    public static final int ANIMATION = 3;
    public static final int AOD = 14;
    public static final int BATTERY_STATS = 9;
    public static final int DATA_SAVER = 10;
    public static final int FORCE_ALL_APPS_STANDBY = 11;
    public static final int FORCE_BACKGROUND_CHECK = 12;
    public static final int FULL_BACKUP = 4;
    public static final int GPS = 1;
    public static final int KEYVALUE_BACKUP = 5;
    public static final int NETWORK_FIREWALL = 6;
    public static final int NULL = 0;
    public static final int OPTIONAL_SENSORS = 13;
    public static final int SCREEN_BRIGHTNESS = 7;
    public static final int SOUND = 8;
    public static final int VIBRATION = 2;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ShutdownReason {}
  
  public final class WakeLock
  {
    private int mExternalCount;
    private int mFlags;
    private boolean mHeld;
    private String mHistoryTag;
    private int mInternalCount;
    private final String mPackageName;
    private boolean mRefCounted = true;
    private final Runnable mReleaser = new Runnable()
    {
      public void run()
      {
        release(65536);
      }
    };
    private String mTag;
    private final IBinder mToken;
    private final String mTraceName;
    private WorkSource mWorkSource;
    
    WakeLock(int paramInt, String paramString1, String paramString2)
    {
      mFlags = paramInt;
      mTag = paramString1;
      mPackageName = paramString2;
      mToken = new Binder();
      this$1 = new StringBuilder();
      append("WakeLock (");
      append(mTag);
      append(")");
      mTraceName = PowerManager.this.toString();
    }
    
    private void acquireLocked()
    {
      mInternalCount += 1;
      mExternalCount += 1;
      if ((!mRefCounted) || (mInternalCount == 1))
      {
        mHandler.removeCallbacks(mReleaser);
        Trace.asyncTraceBegin(131072L, mTraceName, 0);
      }
      try
      {
        mService.acquireWakeLock(mToken, mFlags, mTag, mPackageName, mWorkSource, mHistoryTag);
        mHeld = true;
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void acquire()
    {
      synchronized (mToken)
      {
        acquireLocked();
        return;
      }
    }
    
    public void acquire(long paramLong)
    {
      synchronized (mToken)
      {
        acquireLocked();
        mHandler.postDelayed(mReleaser, paramLong);
        return;
      }
    }
    
    protected void finalize()
      throws Throwable
    {
      synchronized (mToken)
      {
        if (mHeld)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("WakeLock finalized while still held: ");
          localStringBuilder.append(mTag);
          Log.wtf("PowerManager", localStringBuilder.toString());
          Trace.asyncTraceEnd(131072L, mTraceName, 0);
          try
          {
            mService.releaseWakeLock(mToken, 0);
          }
          catch (RemoteException localRemoteException)
          {
            throw localRemoteException.rethrowFromSystemServer();
          }
        }
        return;
      }
    }
    
    public void forceReleaseSuspiciousWakelocks(String paramString, long paramLong)
    {
      try
      {
        mService.forceReleaseSuspiciousWakelocks(paramString, paramLong);
      }
      catch (RemoteException paramString) {}
    }
    
    public List<String> getSuspiciousWakelocks(long paramLong)
    {
      try
      {
        List localList = mService.getSuspiciousWakelocks(paramLong);
        return localList;
      }
      catch (RemoteException localRemoteException) {}
      return null;
    }
    
    public String getTag()
    {
      return mTag;
    }
    
    public boolean isHeld()
    {
      synchronized (mToken)
      {
        boolean bool = mHeld;
        return bool;
      }
    }
    
    public void release()
    {
      release(0);
    }
    
    public void release(int paramInt)
    {
      synchronized (mToken)
      {
        if (mInternalCount > 0) {
          mInternalCount -= 1;
        }
        if ((0x10000 & paramInt) == 0) {
          mExternalCount -= 1;
        }
        if ((!mRefCounted) || (mInternalCount == 0))
        {
          mHandler.removeCallbacks(mReleaser);
          if (mHeld)
          {
            Trace.asyncTraceEnd(131072L, mTraceName, 0);
            try
            {
              mService.releaseWakeLock(mToken, paramInt);
              mHeld = false;
            }
            catch (RemoteException localRemoteException)
            {
              throw localRemoteException.rethrowFromSystemServer();
            }
          }
        }
        if ((mRefCounted) && (mExternalCount < 0))
        {
          RuntimeException localRuntimeException = new java/lang/RuntimeException;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("WakeLock under-locked ");
          localStringBuilder.append(mTag);
          localRuntimeException.<init>(localStringBuilder.toString());
          throw localRuntimeException;
        }
        return;
      }
    }
    
    public void setHistoryTag(String paramString)
    {
      mHistoryTag = paramString;
    }
    
    public void setReferenceCounted(boolean paramBoolean)
    {
      synchronized (mToken)
      {
        mRefCounted = paramBoolean;
        return;
      }
    }
    
    public void setTag(String paramString)
    {
      mTag = paramString;
    }
    
    public void setUnimportantForLogging(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x40000000;
      } else {
        mFlags &= 0xBFFFFFFF;
      }
    }
    
    public void setWorkSource(WorkSource paramWorkSource)
    {
      IBinder localIBinder = mToken;
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
          break label171;
        }
      }
      int i = 1;
      if (localWorkSource == null)
      {
        if (mWorkSource == null) {
          i = 0;
        }
        mWorkSource = null;
      }
      else if (mWorkSource == null)
      {
        i = 1;
        paramWorkSource = new android/os/WorkSource;
        paramWorkSource.<init>(localWorkSource);
        mWorkSource = paramWorkSource;
      }
      else
      {
        boolean bool1 = true ^ mWorkSource.equals(localWorkSource);
        i = bool1;
        if (bool1)
        {
          mWorkSource.set(localWorkSource);
          i = bool1;
        }
      }
      if (i != 0)
      {
        boolean bool2 = mHeld;
        if (bool2) {
          try
          {
            mService.updateWakeLockWorkSource(mToken, mWorkSource, mHistoryTag);
          }
          catch (RemoteException paramWorkSource)
          {
            throw paramWorkSource.rethrowFromSystemServer();
          }
        }
      }
      return;
      label171:
      throw paramWorkSource;
    }
    
    public String toString()
    {
      synchronized (mToken)
      {
        Object localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("WakeLock{");
        ((StringBuilder)localObject1).append(Integer.toHexString(System.identityHashCode(this)));
        ((StringBuilder)localObject1).append(" held=");
        ((StringBuilder)localObject1).append(mHeld);
        ((StringBuilder)localObject1).append(", refCount=");
        ((StringBuilder)localObject1).append(mInternalCount);
        ((StringBuilder)localObject1).append("}");
        localObject1 = ((StringBuilder)localObject1).toString();
        return localObject1;
      }
    }
    
    public Runnable wrap(Runnable paramRunnable)
    {
      acquire();
      return new _..Lambda.PowerManager.WakeLock.VvFzmRZ4ZGlXx7u3lSAJ_T_YUjw(this, paramRunnable);
    }
    
    public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
    {
      synchronized (mToken)
      {
        paramLong = paramProtoOutputStream.start(paramLong);
        paramProtoOutputStream.write(1138166333441L, mTag);
        paramProtoOutputStream.write(1138166333442L, mPackageName);
        paramProtoOutputStream.write(1133871366147L, mHeld);
        paramProtoOutputStream.write(1120986464260L, mInternalCount);
        if (mWorkSource != null) {
          mWorkSource.writeToProto(paramProtoOutputStream, 1146756268037L);
        }
        paramProtoOutputStream.end(paramLong);
        return;
      }
    }
  }
}
