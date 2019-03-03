package com.android.internal.os;

import android.os.BatteryStats.Uid;
import android.os.BatteryStats.Uid.Proc;
import android.util.ArrayMap;
import android.util.Log;

public class CpuPowerCalculator
  extends PowerCalculator
{
  private static final boolean DEBUG = false;
  private static final long MICROSEC_IN_HR = 3600000000L;
  private static final String TAG = "CpuPowerCalculator";
  private final PowerProfile mProfile;
  
  public CpuPowerCalculator(PowerProfile paramPowerProfile)
  {
    mProfile = paramPowerProfile;
  }
  
  public void calculateApp(BatterySipper paramBatterySipper, BatteryStats.Uid paramUid, long paramLong1, long paramLong2, int paramInt)
  {
    int i = paramInt;
    cpuTimeMs = ((paramUid.getUserCpuTimeUs(i) + paramUid.getSystemCpuTimeUs(i)) / 1000L);
    int j = mProfile.getNumCpuClusters();
    double d1 = 0.0D;
    for (int k = 0; k < j; k++)
    {
      int m = mProfile.getNumSpeedStepsInCpuCluster(k);
      for (n = 0; n < m; n++) {
        d1 += paramUid.getTimeAtCpuSpeed(k, n, i) * mProfile.getAveragePowerForCpuCore(k, n);
      }
    }
    d1 += paramUid.getCpuActiveTime() * 1000L * mProfile.getAveragePower("cpu.active");
    Object localObject1 = paramUid.getCpuClusterTimes();
    double d2 = d1;
    if (localObject1 != null)
    {
      if (localObject1.length == j) {
        for (k = 0;; k++)
        {
          d2 = d1;
          if (k >= j) {
            break;
          }
          d1 += localObject1[k] * 1000L * mProfile.getAveragePowerForCpuCluster(k);
        }
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("UID ");
      ((StringBuilder)localObject2).append(paramUid.getUid());
      ((StringBuilder)localObject2).append(" CPU cluster # mismatch: Power Profile # ");
      ((StringBuilder)localObject2).append(j);
      ((StringBuilder)localObject2).append(" actual # ");
      ((StringBuilder)localObject2).append(localObject1.length);
      Log.w("CpuPowerCalculator", ((StringBuilder)localObject2).toString());
      d2 = d1;
    }
    cpuPowerMah = (d2 / 3.6E9D);
    d2 = 0.0D;
    cpuFgTimeMs = 0L;
    Object localObject2 = paramUid.getProcessStats();
    i = ((ArrayMap)localObject2).size();
    k = 0;
    paramUid = (BatteryStats.Uid)localObject1;
    int n = j;
    for (;;)
    {
      j = paramInt;
      if (k >= i) {
        break;
      }
      localObject1 = (BatteryStats.Uid.Proc)((ArrayMap)localObject2).valueAt(k);
      String str = (String)((ArrayMap)localObject2).keyAt(k);
      cpuFgTimeMs += ((BatteryStats.Uid.Proc)localObject1).getForegroundTime(j);
      paramLong1 = ((BatteryStats.Uid.Proc)localObject1).getUserTime(j) + ((BatteryStats.Uid.Proc)localObject1).getSystemTime(j) + ((BatteryStats.Uid.Proc)localObject1).getForegroundTime(j);
      if ((packageWithHighestDrain != null) && (!packageWithHighestDrain.startsWith("*")))
      {
        d1 = d2;
        if (d2 < paramLong1)
        {
          d1 = d2;
          if (!str.startsWith("*"))
          {
            d1 = paramLong1;
            packageWithHighestDrain = str;
          }
        }
      }
      else
      {
        d1 = paramLong1;
        packageWithHighestDrain = str;
      }
      k++;
      d2 = d1;
    }
    if (cpuFgTimeMs > cpuTimeMs) {
      cpuTimeMs = cpuFgTimeMs;
    }
  }
}
