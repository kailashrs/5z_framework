package com.android.internal.os;

import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;

public class CameraPowerCalculator
  extends PowerCalculator
{
  private final double mCameraPowerOnAvg;
  
  public CameraPowerCalculator(PowerProfile paramPowerProfile)
  {
    mCameraPowerOnAvg = paramPowerProfile.getAveragePower("camera.avg");
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    paramUid = paramUid.getCameraTurnedOnTimer();
    if (paramUid != null)
    {
      paramLong1 = paramUid.getTotalTimeLocked(paramLong1, paramInt) / 1000L;
      cameraTimeMs = paramLong1;
      cameraPowerMah = (paramLong1 * mCameraPowerOnAvg / 3600000.0D);
    }
    else
    {
      cameraTimeMs = 0L;
      cameraPowerMah = 0.0D;
    }
  }
}
