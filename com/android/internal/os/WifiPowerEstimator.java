package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.Uid;

public class WifiPowerEstimator
  extends PowerCalculator
{
  private static final boolean DEBUG = false;
  private static final String TAG = "WifiPowerEstimator";
  private long mTotalAppWifiRunningTimeMs = 0L;
  private final double mWifiPowerBatchScan;
  private final double mWifiPowerOn;
  private final double mWifiPowerPerPacket;
  private final double mWifiPowerScan;
  
  public WifiPowerEstimator(PowerProfile paramPowerProfile)
  {
    mWifiPowerPerPacket = getWifiPowerPerPacket(paramPowerProfile);
    mWifiPowerOn = paramPowerProfile.getAveragePower("wifi.on");
    mWifiPowerScan = paramPowerProfile.getAveragePower("wifi.scan");
    mWifiPowerBatchScan = paramPowerProfile.getAveragePower("wifi.batchedscan");
  }
  
  private static double getWifiPowerPerPacket(PowerProfile paramPowerProfile)
  {
    return paramPowerProfile.getAveragePower("wifi.active") / 3600.0D / 61.03515625D;
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    BatteryStats.Uid localUid = paramUid;
    paramLong2 = paramLong1;
    int i = paramInt;
    wifiRxPackets = localUid.getNetworkActivityPackets(2, i);
    wifiTxPackets = localUid.getNetworkActivityPackets(3, i);
    wifiRxBytes = localUid.getNetworkActivityBytes(2, i);
    wifiTxBytes = localUid.getNetworkActivityBytes(3, i);
    double d1 = wifiRxPackets + wifiTxPackets;
    double d2 = mWifiPowerPerPacket;
    wifiRunningTimeMs = (localUid.getWifiRunningTime(paramLong2, i) / 1000L);
    mTotalAppWifiRunningTimeMs += wifiRunningTimeMs;
    double d3 = wifiRunningTimeMs * mWifiPowerOn / 3600000.0D;
    double d4 = localUid.getWifiScanTime(paramLong2, i) / 1000L * mWifiPowerScan / 3600000.0D;
    double d5 = 0.0D;
    for (i = 0; i < 5; i++) {
      d5 += paramUid.getWifiBatchedScanTime(i, paramLong1, paramInt) / 1000L * mWifiPowerBatchScan / 3600000.0D;
    }
    wifiPowerMah = (d1 * d2 + d3 + d4 + d5);
  }
  
  public void calculateRemaining(BatterySipper paramBatterySipper, BatteryStats paramBatteryStats, long paramLong1, long paramLong2, int paramInt)
  {
    paramLong1 = paramBatteryStats.getGlobalWifiRunningTime(paramLong1, paramInt) / 1000L;
    double d = (paramLong1 - mTotalAppWifiRunningTimeMs) * mWifiPowerOn / 3600000.0D;
    wifiRunningTimeMs = paramLong1;
    wifiPowerMah = Math.max(0.0D, d);
  }
  
  public void reset()
  {
    mTotalAppWifiRunningTimeMs = 0L;
  }
}
