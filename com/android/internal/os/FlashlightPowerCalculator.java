package com.android.internal.os;

import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;

public class FlashlightPowerCalculator
  extends PowerCalculator
{
  private final double mFlashlightPowerOnAvg;
  
  public FlashlightPowerCalculator(PowerProfile paramPowerProfile)
  {
    mFlashlightPowerOnAvg = paramPowerProfile.getAveragePower("camera.flashlight");
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    paramUid = paramUid.getFlashlightTurnedOnTimer();
    if (paramUid != null)
    {
      paramLong1 = paramUid.getTotalTimeLocked(paramLong1, paramInt) / 1000L;
      flashlightTimeMs = paramLong1;
      flashlightPowerMah = (paramLong1 * mFlashlightPowerOnAvg / 3600000.0D);
    }
    else
    {
      flashlightTimeMs = 0L;
      flashlightPowerMah = 0.0D;
    }
  }
}
