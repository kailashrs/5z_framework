package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.Uid;

public class MobileRadioPowerCalculator
  extends PowerCalculator
{
  private static final boolean DEBUG = false;
  private static final String TAG = "MobileRadioPowerController";
  private final double[] mPowerBins = new double[6];
  private final double mPowerRadioOn;
  private final double mPowerScan;
  private BatteryStats mStats;
  private long mTotalAppMobileActiveMs = 0L;
  
  public MobileRadioPowerCalculator(PowerProfile paramPowerProfile, BatteryStats paramBatteryStats)
  {
    double d = paramPowerProfile.getAveragePowerOrDefault("radio.active", -1.0D);
    int i = 0;
    int j = 1;
    if (d != -1.0D)
    {
      mPowerRadioOn = d;
    }
    else
    {
      d = 0.0D + paramPowerProfile.getAveragePower("modem.controller.rx");
      for (k = 0; k < mPowerBins.length; k++) {
        d += paramPowerProfile.getAveragePower("modem.controller.tx", k);
      }
      mPowerRadioOn = (d / (mPowerBins.length + 1));
    }
    if (paramPowerProfile.getAveragePowerOrDefault("radio.on", -1.0D) != -1.0D) {
      for (k = i; k < mPowerBins.length; k++) {
        mPowerBins[k] = paramPowerProfile.getAveragePower("radio.on", k);
      }
    }
    d = paramPowerProfile.getAveragePower("modem.controller.idle");
    mPowerBins[0] = (25.0D * d / 180.0D);
    for (int k = j; k < mPowerBins.length; k++) {
      mPowerBins[k] = Math.max(1.0D, d / 256.0D);
    }
    mPowerScan = paramPowerProfile.getAveragePowerOrDefault("radio.scanning", 0.0D);
    mStats = paramBatteryStats;
  }
  
  private double getMobilePowerPerPacket(long paramLong, int paramInt)
  {
    double d1 = mPowerRadioOn / 3600.0D;
    long l = mStats.getNetworkActivityPackets(0, paramInt) + mStats.getNetworkActivityPackets(1, paramInt);
    paramLong = mStats.getMobileRadioActiveTime(paramLong, paramInt) / 1000L;
    double d2;
    if ((l != 0L) && (paramLong != 0L)) {
      d2 = l / paramLong;
    } else {
      d2 = 12.20703125D;
    }
    return d1 / d2 / 3600.0D;
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    mobileRxPackets = paramUid.getNetworkActivityPackets(0, paramInt);
    mobileTxPackets = paramUid.getNetworkActivityPackets(1, paramInt);
    mobileActive = (paramUid.getMobileRadioActiveTime(paramInt) / 1000L);
    mobileActiveCount = paramUid.getMobileRadioActiveCount(paramInt);
    mobileRxBytes = paramUid.getNetworkActivityBytes(0, paramInt);
    mobileTxBytes = paramUid.getNetworkActivityBytes(1, paramInt);
    if (mobileActive > 0L)
    {
      mTotalAppMobileActiveMs += mobileActive;
      mobileRadioPowerMah = (mobileActive * mPowerRadioOn / 3600000.0D);
    }
    else
    {
      mobileRadioPowerMah = ((mobileRxPackets + mobileTxPackets) * getMobilePowerPerPacket(paramLong1, paramInt));
    }
  }
  
  public void calculateRemaining(BatterySipper paramBatterySipper, BatteryStats paramBatteryStats, long paramLong1, long paramLong2, int paramInt)
  {
    double d = 0.0D;
    long l1 = 0L;
    paramLong2 = 0L;
    for (int i = 0; i < mPowerBins.length; i++)
    {
      long l2 = paramBatteryStats.getPhoneSignalStrengthTime(i, paramLong1, paramInt) / 1000L;
      d += l2 * mPowerBins[i] / 3600000.0D;
      l1 += l2;
      if (i == 0) {
        paramLong2 = l2;
      }
    }
    d += paramBatteryStats.getPhoneSignalScanningTime(paramLong1, paramInt) / 1000L * mPowerScan / 3600000.0D;
    paramLong1 = mStats.getMobileRadioActiveTime(paramLong1, paramInt) / 1000L - mTotalAppMobileActiveMs;
    if (paramLong1 > 0L) {
      d += mPowerRadioOn * paramLong1 / 3600000.0D;
    }
    if (d != 0.0D)
    {
      if (l1 != 0L) {
        noCoveragePercent = (paramLong2 * 100.0D / l1);
      }
      mobileActive = paramLong1;
      mobileActiveCount = paramBatteryStats.getMobileRadioActiveUnknownCount(paramInt);
      mobileRadioPowerMah = d;
    }
  }
  
  public void reset()
  {
    mTotalAppMobileActiveMs = 0L;
  }
  
  public void reset(BatteryStats paramBatteryStats)
  {
    reset();
    mStats = paramBatteryStats;
  }
}
