package android.os.health;

import android.content.Context;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.app.IBatteryStats;
import com.android.internal.app.IBatteryStats.Stub;

public class SystemHealthManager
{
  private final IBatteryStats mBatteryStats;
  
  public SystemHealthManager()
  {
    this(IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats")));
  }
  
  public SystemHealthManager(IBatteryStats paramIBatteryStats)
  {
    mBatteryStats = paramIBatteryStats;
  }
  
  public static SystemHealthManager from(Context paramContext)
  {
    return (SystemHealthManager)paramContext.getSystemService("systemhealth");
  }
  
  public HealthStats takeMyUidSnapshot()
  {
    return takeUidSnapshot(Process.myUid());
  }
  
  public HealthStats takeUidSnapshot(int paramInt)
  {
    try
    {
      HealthStats localHealthStats = mBatteryStats.takeUidSnapshot(paramInt).getHealthStats();
      return localHealthStats;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public HealthStats[] takeUidSnapshots(int[] paramArrayOfInt)
  {
    try
    {
      HealthStatsParceler[] arrayOfHealthStatsParceler = mBatteryStats.takeUidSnapshots(paramArrayOfInt);
      HealthStats[] arrayOfHealthStats = new HealthStats[paramArrayOfInt.length];
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++) {
        arrayOfHealthStats[j] = arrayOfHealthStatsParceler[j].getHealthStats();
      }
      return arrayOfHealthStats;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw new RuntimeException(paramArrayOfInt);
    }
  }
}
