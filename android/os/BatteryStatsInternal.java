package android.os;

public abstract class BatteryStatsInternal
{
  public BatteryStatsInternal() {}
  
  public abstract String[] getMobileIfaces();
  
  public abstract String[] getWifiIfaces();
  
  public abstract void noteJobsDeferred(int paramInt1, int paramInt2, long paramLong);
}
