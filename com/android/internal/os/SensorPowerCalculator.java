package com.android.internal.os;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryStats;
import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;
import android.os.BatteryStats.Uid.Sensor;
import android.util.SparseArray;
import java.util.List;

public class SensorPowerCalculator
  extends PowerCalculator
{
  private final double mGpsPower;
  private final List<Sensor> mSensors;
  
  public SensorPowerCalculator(PowerProfile paramPowerProfile, SensorManager paramSensorManager, BatteryStats paramBatteryStats, long paramLong, int paramInt)
  {
    mSensors = paramSensorManager.getSensorList(-1);
    mGpsPower = getAverageGpsPower(paramPowerProfile, paramBatteryStats, paramLong, paramInt);
  }
  
  private double getAverageGpsPower(PowerProfile paramPowerProfile, BatteryStats paramBatteryStats, long paramLong, int paramInt)
  {
    double d1 = paramPowerProfile.getAveragePowerOrDefault("gps.on", -1.0D);
    if (d1 != -1.0D) {
      return d1;
    }
    d1 = 0.0D;
    long l1 = 0L;
    double d2 = 0.0D;
    for (int i = 0; i < 2; i++)
    {
      long l2 = paramBatteryStats.getGpsSignalQualityTime(i, paramLong, paramInt);
      l1 += l2;
      d2 += paramPowerProfile.getAveragePower("gps.signalqualitybased", i) * l2;
    }
    if (l1 != 0L) {
      d1 = d2 / l1;
    }
    return d1;
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    paramUid = paramUid.getSensorStats();
    int i = paramUid.size();
    for (int j = 0; j < i; j++)
    {
      Object localObject = (BatteryStats.Uid.Sensor)paramUid.valueAt(j);
      int k = paramUid.keyAt(j);
      paramLong2 = ((BatteryStats.Uid.Sensor)localObject).getSensorTime().getTotalTimeLocked(paramLong1, paramInt) / 1000L;
      if (k != 55536)
      {
        int m = mSensors.size();
        for (int n = 0; n < m; n++)
        {
          localObject = (Sensor)mSensors.get(n);
          if (((Sensor)localObject).getHandle() == k)
          {
            sensorPowerMah += (float)paramLong2 * ((Sensor)localObject).getPower() / 3600000.0F;
            break;
          }
        }
      }
      else
      {
        gpsTimeMs = paramLong2;
        gpsPowerMah = (gpsTimeMs * mGpsPower / 3600000.0D);
      }
    }
  }
}
