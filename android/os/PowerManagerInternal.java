package android.os;

import java.util.function.Consumer;

public abstract class PowerManagerInternal
{
  public static final int WAKEFULNESS_ASLEEP = 0;
  public static final int WAKEFULNESS_AWAKE = 1;
  public static final int WAKEFULNESS_DOZING = 3;
  public static final int WAKEFULNESS_DREAMING = 2;
  
  public PowerManagerInternal() {}
  
  public static boolean isInteractive(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1) {
      if (paramInt == 2) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  public static int wakefulnessToProtoEnum(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return paramInt;
    case 3: 
      return 3;
    case 2: 
      return 2;
    case 1: 
      return 1;
    }
    return 0;
  }
  
  public static String wakefulnessToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 3: 
      return "Dozing";
    case 2: 
      return "Dreaming";
    case 1: 
      return "Awake";
    }
    return "Asleep";
  }
  
  public abstract void finishUidChanges();
  
  public abstract PowerSaveState getLowPowerState(int paramInt);
  
  public abstract void powerHint(int paramInt1, int paramInt2);
  
  public void registerLowPowerModeObserver(final int paramInt, final Consumer<PowerSaveState> paramConsumer)
  {
    registerLowPowerModeObserver(new LowPowerModeListener()
    {
      public int getServiceType()
      {
        return paramInt;
      }
      
      public void onLowPowerModeChanged(PowerSaveState paramAnonymousPowerSaveState)
      {
        paramConsumer.accept(paramAnonymousPowerSaveState);
      }
    });
  }
  
  public abstract void registerLowPowerModeObserver(LowPowerModeListener paramLowPowerModeListener);
  
  public abstract boolean setDeviceIdleMode(boolean paramBoolean);
  
  public abstract void setDeviceIdleTempWhitelist(int[] paramArrayOfInt);
  
  public abstract void setDeviceIdleWhitelist(int[] paramArrayOfInt);
  
  public abstract void setDozeOverrideFromDreamManager(int paramInt1, int paramInt2);
  
  public abstract void setDrawWakeLockOverrideFromSidekick(boolean paramBoolean);
  
  public abstract void setEarlyWakeup(boolean paramBoolean);
  
  public abstract void setInadvertentTouch(boolean paramBoolean);
  
  public abstract boolean setLightDeviceIdleMode(boolean paramBoolean);
  
  public abstract void setMaximumScreenOffTimeoutFromDeviceAdmin(int paramInt, long paramLong);
  
  public abstract void setScreenBrightnessOverrideFromWindowManager(int paramInt);
  
  public abstract void setUserActivityTimeoutOverrideFromWindowManager(long paramLong);
  
  public abstract void setUserInactiveOverrideFromWindowManager();
  
  public abstract void startUidChanges();
  
  public abstract void uidActive(int paramInt);
  
  public abstract void uidGone(int paramInt);
  
  public abstract void uidIdle(int paramInt);
  
  public abstract void updateUidProcState(int paramInt1, int paramInt2);
  
  public static abstract interface LowPowerModeListener
  {
    public abstract int getServiceType();
    
    public abstract void onLowPowerModeChanged(PowerSaveState paramPowerSaveState);
  }
}
