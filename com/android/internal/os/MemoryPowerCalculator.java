package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;
import android.util.LongSparseArray;

public class MemoryPowerCalculator
  extends PowerCalculator
{
  private static final boolean DEBUG = false;
  public static final String TAG = "MemoryPowerCalculator";
  private final double[] powerAverages;
  
  public MemoryPowerCalculator(PowerProfile paramPowerProfile)
  {
    int i = paramPowerProfile.getNumElements("memory.bandwidths");
    powerAverages = new double[i];
    for (int j = 0; j < i; j++)
    {
      powerAverages[j] = paramPowerProfile.getAveragePower("memory.bandwidths", j);
      double d = powerAverages[j];
    }
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt) {}
  
  public void calculateRemaining(BatterySipper paramBatterySipper, BatteryStats paramBatteryStats, long paramLong1, long paramLong2, int paramInt)
  {
    double d1 = 0.0D;
    paramLong2 = 0L;
    paramBatteryStats = paramBatteryStats.getKernelMemoryStats();
    for (int i = 0; (i < paramBatteryStats.size()) && (i < powerAverages.length); i++)
    {
      double d2 = powerAverages[((int)paramBatteryStats.keyAt(i))];
      long l = ((BatteryStats.Timer)paramBatteryStats.valueAt(i)).getTotalTimeLocked(paramLong1, paramInt);
      d1 += l * d2 / 60000.0D / 60.0D;
      paramLong2 += l;
    }
    usagePowerMah = d1;
    usageTimeMs = paramLong2;
  }
}
