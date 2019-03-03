package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.Uid;

public abstract class PowerCalculator
{
  public PowerCalculator() {}
  
  public abstract void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt);
  
  public void calculateRemaining(BatterySipper paramBatterySipper, BatteryStats paramBatteryStats, long paramLong1, long paramLong2, int paramInt) {}
  
  public void reset() {}
}
