package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.ControllerActivityCounter;
import android.os.BatteryStats.LongCounter;
import android.os.BatteryStats.Uid;

public class WifiPowerCalculator
  extends PowerCalculator
{
  private static final boolean DEBUG = false;
  private static final String TAG = "WifiPowerCalculator";
  private final double mIdleCurrentMa;
  private final double mRxCurrentMa;
  private double mTotalAppPowerDrain = 0.0D;
  private long mTotalAppRunningTime = 0L;
  private final double mTxCurrentMa;
  
  public WifiPowerCalculator(PowerProfile paramPowerProfile)
  {
    mIdleCurrentMa = paramPowerProfile.getAveragePower("wifi.controller.idle");
    mTxCurrentMa = paramPowerProfile.getAveragePower("wifi.controller.tx");
    mRxCurrentMa = paramPowerProfile.getAveragePower("wifi.controller.rx");
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    BatteryStats.ControllerActivityCounter localControllerActivityCounter = paramUid.getWifiControllerActivity();
    if (localControllerActivityCounter == null) {
      return;
    }
    paramLong1 = localControllerActivityCounter.getIdleTimeCounter().getCountLocked(paramInt);
    long l = localControllerActivityCounter.getTxTimeCounters()[0].getCountLocked(paramInt);
    paramLong2 = localControllerActivityCounter.getRxTimeCounter().getCountLocked(paramInt);
    wifiRunningTimeMs = (paramLong1 + paramLong2 + l);
    mTotalAppRunningTime += wifiRunningTimeMs;
    wifiPowerMah = ((paramLong1 * mIdleCurrentMa + l * mTxCurrentMa + paramLong2 * mRxCurrentMa) / 3600000.0D);
    mTotalAppPowerDrain += wifiPowerMah;
    wifiRxPackets = paramUid.getNetworkActivityPackets(2, paramInt);
    wifiTxPackets = paramUid.getNetworkActivityPackets(3, paramInt);
    wifiRxBytes = paramUid.getNetworkActivityBytes(2, paramInt);
    wifiTxBytes = paramUid.getNetworkActivityBytes(3, paramInt);
  }
  
  public void calculateRemaining(BatterySipper paramBatterySipper, BatteryStats paramBatteryStats, long paramLong1, long paramLong2, int paramInt)
  {
    paramBatteryStats = paramBatteryStats.getWifiControllerActivity();
    long l = paramBatteryStats.getIdleTimeCounter().getCountLocked(paramInt);
    paramLong1 = paramBatteryStats.getTxTimeCounters()[0].getCountLocked(paramInt);
    paramLong2 = paramBatteryStats.getRxTimeCounter().getCountLocked(paramInt);
    wifiRunningTimeMs = Math.max(0L, l + paramLong2 + paramLong1 - mTotalAppRunningTime);
    double d = paramBatteryStats.getPowerCounter().getCountLocked(paramInt) / 3600000.0D;
    if (d == 0.0D) {
      d = (l * mIdleCurrentMa + paramLong1 * mTxCurrentMa + paramLong2 * mRxCurrentMa) / 3600000.0D;
    }
    wifiPowerMah = Math.max(0.0D, d - mTotalAppPowerDrain);
  }
  
  public void reset()
  {
    mTotalAppPowerDrain = 0.0D;
    mTotalAppRunningTime = 0L;
  }
}
