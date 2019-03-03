package com.android.internal.telephony.metrics;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.connectivity.CellularBatteryStats;
import com.android.internal.app.IBatteryStats;
import com.android.internal.app.IBatteryStats.Stub;
import com.android.internal.telephony.nano.TelephonyProto.ModemPowerStats;

public class ModemPowerMetrics
{
  private final IBatteryStats mBatteryStats = IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
  
  public ModemPowerMetrics() {}
  
  private CellularBatteryStats getStats()
  {
    try
    {
      CellularBatteryStats localCellularBatteryStats = mBatteryStats.getCellularBatteryStats();
      return localCellularBatteryStats;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public TelephonyProto.ModemPowerStats buildProto()
  {
    TelephonyProto.ModemPowerStats localModemPowerStats = new TelephonyProto.ModemPowerStats();
    Object localObject = getStats();
    if (localObject != null)
    {
      loggingDurationMs = ((CellularBatteryStats)localObject).getLoggingDurationMs();
      energyConsumedMah = (((CellularBatteryStats)localObject).getEnergyConsumedMaMs() / 3600000.0D);
      numPacketsTx = ((CellularBatteryStats)localObject).getNumPacketsTx();
      cellularKernelActiveTimeMs = ((CellularBatteryStats)localObject).getKernelActiveTimeMs();
      long[] arrayOfLong = ((CellularBatteryStats)localObject).getTimeInRxSignalStrengthLevelMs();
      int i = 0;
      if ((arrayOfLong != null) && (((CellularBatteryStats)localObject).getTimeInRxSignalStrengthLevelMs().length > 0)) {
        timeInVeryPoorRxSignalLevelMs = localObject.getTimeInRxSignalStrengthLevelMs()[0];
      }
      sleepTimeMs = ((CellularBatteryStats)localObject).getSleepTimeMs();
      idleTimeMs = ((CellularBatteryStats)localObject).getIdleTimeMs();
      rxTimeMs = ((CellularBatteryStats)localObject).getRxTimeMs();
      localObject = ((CellularBatteryStats)localObject).getTxTimeMs();
      txTimeMs = new long[localObject.length];
      while (i < localObject.length)
      {
        txTimeMs[i] = localObject[i];
        i++;
      }
    }
    return localModemPowerStats;
  }
}
