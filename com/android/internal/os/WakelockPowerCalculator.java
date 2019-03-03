package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;
import android.os.BatteryStats.Uid.Wakelock;
import android.util.ArrayMap;

public class WakelockPowerCalculator
  extends PowerCalculator
{
  private static final boolean DEBUG = false;
  private static final String TAG = "WakelockPowerCalculator";
  private final double mPowerWakelock;
  private long mTotalAppWakelockTimeMs = 0L;
  
  public WakelockPowerCalculator(PowerProfile paramPowerProfile)
  {
    mPowerWakelock = paramPowerProfile.getAveragePower("cpu.idle");
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    paramUid = paramUid.getWakelockStats();
    int i = paramUid.size();
    paramLong2 = 0L;
    for (int j = 0; j < i; j++)
    {
      BatteryStats.Timer localTimer = ((BatteryStats.Uid.Wakelock)paramUid.valueAt(j)).getWakeTime(0);
      if (localTimer != null) {
        paramLong2 += localTimer.getTotalTimeLocked(paramLong1, paramInt);
      }
    }
    wakeLockTimeMs = (paramLong2 / 1000L);
    mTotalAppWakelockTimeMs += wakeLockTimeMs;
    wakeLockPowerMah = (wakeLockTimeMs * mPowerWakelock / 3600000.0D);
  }
  
  public void calculateRemaining(BatterySipper paramBatterySipper, BatteryStats paramBatteryStats, long paramLong1, long paramLong2, int paramInt)
  {
    paramLong1 = paramBatteryStats.getBatteryUptime(paramLong2) / 1000L - (mTotalAppWakelockTimeMs + paramBatteryStats.getScreenOnTime(paramLong1, paramInt) / 1000L);
    if (paramLong1 > 0L)
    {
      double d = paramLong1 * mPowerWakelock / 3600000.0D;
      wakeLockTimeMs += paramLong1;
      wakeLockPowerMah += d;
    }
  }
  
  public void reset()
  {
    mTotalAppWakelockTimeMs = 0L;
  }
}
