package com.android.internal.os;

import android.util.ArrayMap;
import java.util.Map;

public final class RpmStats
{
  public Map<String, PowerStatePlatformSleepState> mPlatformLowPowerStats = new ArrayMap();
  public Map<String, PowerStateSubsystem> mSubsystemLowPowerStats = new ArrayMap();
  
  public RpmStats() {}
  
  public PowerStatePlatformSleepState getAndUpdatePlatformState(String paramString, long paramLong, int paramInt)
  {
    PowerStatePlatformSleepState localPowerStatePlatformSleepState1 = (PowerStatePlatformSleepState)mPlatformLowPowerStats.get(paramString);
    PowerStatePlatformSleepState localPowerStatePlatformSleepState2 = localPowerStatePlatformSleepState1;
    if (localPowerStatePlatformSleepState1 == null)
    {
      localPowerStatePlatformSleepState2 = new PowerStatePlatformSleepState();
      mPlatformLowPowerStats.put(paramString, localPowerStatePlatformSleepState2);
    }
    mTimeMs = paramLong;
    mCount = paramInt;
    return localPowerStatePlatformSleepState2;
  }
  
  public PowerStateSubsystem getSubsystem(String paramString)
  {
    PowerStateSubsystem localPowerStateSubsystem1 = (PowerStateSubsystem)mSubsystemLowPowerStats.get(paramString);
    PowerStateSubsystem localPowerStateSubsystem2 = localPowerStateSubsystem1;
    if (localPowerStateSubsystem1 == null)
    {
      localPowerStateSubsystem2 = new PowerStateSubsystem();
      mSubsystemLowPowerStats.put(paramString, localPowerStateSubsystem2);
    }
    return localPowerStateSubsystem2;
  }
  
  public static class PowerStateElement
  {
    public int mCount;
    public long mTimeMs;
    
    private PowerStateElement(long paramLong, int paramInt)
    {
      mTimeMs = paramLong;
      mCount = paramInt;
    }
  }
  
  public static class PowerStatePlatformSleepState
  {
    public int mCount;
    public long mTimeMs;
    public Map<String, RpmStats.PowerStateElement> mVoters = new ArrayMap();
    
    public PowerStatePlatformSleepState() {}
    
    public void putVoter(String paramString, long paramLong, int paramInt)
    {
      RpmStats.PowerStateElement localPowerStateElement = (RpmStats.PowerStateElement)mVoters.get(paramString);
      if (localPowerStateElement == null)
      {
        mVoters.put(paramString, new RpmStats.PowerStateElement(paramLong, paramInt, null));
      }
      else
      {
        mTimeMs = paramLong;
        mCount = paramInt;
      }
    }
  }
  
  public static class PowerStateSubsystem
  {
    public Map<String, RpmStats.PowerStateElement> mStates = new ArrayMap();
    
    public PowerStateSubsystem() {}
    
    public void putState(String paramString, long paramLong, int paramInt)
    {
      RpmStats.PowerStateElement localPowerStateElement = (RpmStats.PowerStateElement)mStates.get(paramString);
      if (localPowerStateElement == null)
      {
        mStates.put(paramString, new RpmStats.PowerStateElement(paramLong, paramInt, null));
      }
      else
      {
        mTimeMs = paramLong;
        mCount = paramInt;
      }
    }
  }
}
