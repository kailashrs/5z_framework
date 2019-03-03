package android.os;

public abstract class BatteryManagerInternal
{
  public BatteryManagerInternal() {}
  
  public abstract int getBatteryChargeCounter();
  
  public abstract int getBatteryFullCharge();
  
  public abstract int getBatteryLevel();
  
  public abstract boolean getBatteryLevelLow();
  
  public abstract int getInvalidCharger();
  
  public abstract int getPlugType();
  
  public abstract boolean isPowered(int paramInt);
}
