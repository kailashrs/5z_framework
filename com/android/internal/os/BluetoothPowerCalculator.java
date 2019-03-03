package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.ControllerActivityCounter;
import android.os.BatteryStats.LongCounter;
import android.os.BatteryStats.Uid;

public class BluetoothPowerCalculator
  extends PowerCalculator
{
  private static final boolean DEBUG = false;
  private static final String TAG = "BluetoothPowerCalculator";
  private double mAppTotalPowerMah = 0.0D;
  private long mAppTotalTimeMs = 0L;
  private final double mIdleMa;
  private final double mRxMa;
  private final double mTxMa;
  
  public BluetoothPowerCalculator(PowerProfile paramPowerProfile)
  {
    mIdleMa = paramPowerProfile.getAveragePower("bluetooth.controller.idle");
    mRxMa = paramPowerProfile.getAveragePower("bluetooth.controller.rx");
    mTxMa = paramPowerProfile.getAveragePower("bluetooth.controller.tx");
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    BatteryStats.ControllerActivityCounter localControllerActivityCounter = paramUid.getBluetoothControllerActivity();
    if (localControllerActivityCounter == null) {
      return;
    }
    paramLong2 = localControllerActivityCounter.getIdleTimeCounter().getCountLocked(paramInt);
    paramLong1 = localControllerActivityCounter.getRxTimeCounter().getCountLocked(paramInt);
    long l1 = localControllerActivityCounter.getTxTimeCounters()[0].getCountLocked(paramInt);
    long l2 = paramLong2 + l1 + paramLong1;
    double d = localControllerActivityCounter.getPowerCounter().getCountLocked(paramInt) / 3600000.0D;
    if (d == 0.0D) {
      d = (paramLong2 * mIdleMa + paramLong1 * mRxMa + l1 * mTxMa) / 3600000.0D;
    }
    bluetoothPowerMah = d;
    bluetoothRunningTimeMs = l2;
    btRxBytes = paramUid.getNetworkActivityBytes(4, paramInt);
    btTxBytes = paramUid.getNetworkActivityBytes(5, paramInt);
    mAppTotalPowerMah += d;
    mAppTotalTimeMs += l2;
  }
  
  public void calculateRemaining(BatterySipper paramBatterySipper, BatteryStats paramBatteryStats, long paramLong1, long paramLong2, int paramInt)
  {
    paramBatteryStats = paramBatteryStats.getBluetoothControllerActivity();
    long l = paramBatteryStats.getIdleTimeCounter().getCountLocked(paramInt);
    paramLong1 = paramBatteryStats.getTxTimeCounters()[0].getCountLocked(paramInt);
    paramLong2 = paramBatteryStats.getRxTimeCounter().getCountLocked(paramInt);
    double d = paramBatteryStats.getPowerCounter().getCountLocked(paramInt) / 3600000.0D;
    if (d == 0.0D) {
      d = (l * mIdleMa + paramLong2 * mRxMa + paramLong1 * mTxMa) / 3600000.0D;
    }
    bluetoothPowerMah = Math.max(0.0D, d - mAppTotalPowerMah);
    bluetoothRunningTimeMs = Math.max(0L, l + paramLong1 + paramLong2 - mAppTotalTimeMs);
  }
  
  public void reset()
  {
    mAppTotalPowerMah = 0.0D;
    mAppTotalTimeMs = 0L;
  }
}
