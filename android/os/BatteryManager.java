package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import com.android.internal.app.IBatteryStats;
import com.android.internal.app.IBatteryStats.Stub;

public class BatteryManager
{
  public static final String ACTION_CHARGING = "android.os.action.CHARGING";
  public static final String ACTION_DISCHARGING = "android.os.action.DISCHARGING";
  public static final int BATTERY_HEALTH_COLD = 7;
  public static final int BATTERY_HEALTH_DEAD = 4;
  public static final int BATTERY_HEALTH_GOOD = 2;
  public static final int BATTERY_HEALTH_OVERHEAT = 3;
  public static final int BATTERY_HEALTH_OVER_VOLTAGE = 5;
  public static final int BATTERY_HEALTH_UNKNOWN = 1;
  public static final int BATTERY_HEALTH_UNSPECIFIED_FAILURE = 6;
  public static final int BATTERY_PLUGGED_AC = 1;
  public static final int BATTERY_PLUGGED_ANY = 7;
  public static final int BATTERY_PLUGGED_USB = 2;
  public static final int BATTERY_PLUGGED_WIRELESS = 4;
  public static final int BATTERY_PROPERTY_CAPACITY = 4;
  public static final int BATTERY_PROPERTY_CHARGE_COUNTER = 1;
  public static final int BATTERY_PROPERTY_CURRENT_AVERAGE = 3;
  public static final int BATTERY_PROPERTY_CURRENT_NOW = 2;
  public static final int BATTERY_PROPERTY_ENERGY_COUNTER = 5;
  public static final int BATTERY_PROPERTY_STATUS = 6;
  public static final int BATTERY_STATUS_CHARGING = 2;
  public static final int BATTERY_STATUS_DISCHARGING = 3;
  public static final int BATTERY_STATUS_FULL = 5;
  public static final int BATTERY_STATUS_NOT_CHARGING = 4;
  public static final int BATTERY_STATUS_NOT_QUICK_CHARGING = 11;
  public static final int BATTERY_STATUS_NOT_QUICK_CHARGING_V1 = 8;
  public static final int BATTERY_STATUS_QUICK_CHARGING = 10;
  public static final int BATTERY_STATUS_QUICK_CHARGING_V1 = 9;
  public static final int BATTERY_STATUS_UNKNOWN = 1;
  public static final String EXTRA_BATTERY_LOW = "battery_low";
  public static final String EXTRA_CHARGE_COUNTER = "charge_counter";
  @SystemApi
  public static final String EXTRA_EVENTS = "android.os.extra.EVENTS";
  @SystemApi
  public static final String EXTRA_EVENT_TIMESTAMP = "android.os.extra.EVENT_TIMESTAMP";
  public static final String EXTRA_HEALTH = "health";
  public static final String EXTRA_ICON_SMALL = "icon-small";
  public static final String EXTRA_INVALID_CHARGER = "invalid_charger";
  public static final String EXTRA_LEVEL = "level";
  public static final String EXTRA_MAX_CHARGING_CURRENT = "max_charging_current";
  public static final String EXTRA_MAX_CHARGING_VOLTAGE = "max_charging_voltage";
  public static final String EXTRA_PLUGGED = "plugged";
  public static final String EXTRA_PRESENT = "present";
  public static final String EXTRA_SCALE = "scale";
  public static final String EXTRA_SEQUENCE = "seq";
  public static final String EXTRA_STATUS = "status";
  public static final String EXTRA_TECHNOLOGY = "technology";
  public static final String EXTRA_TEMPERATURE = "temperature";
  public static final String EXTRA_VOLTAGE = "voltage";
  private final IBatteryPropertiesRegistrar mBatteryPropertiesRegistrar;
  private final IBatteryStats mBatteryStats;
  private final Context mContext;
  
  public BatteryManager()
  {
    mContext = null;
    mBatteryStats = IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
    mBatteryPropertiesRegistrar = IBatteryPropertiesRegistrar.Stub.asInterface(ServiceManager.getService("batteryproperties"));
  }
  
  public BatteryManager(Context paramContext, IBatteryStats paramIBatteryStats, IBatteryPropertiesRegistrar paramIBatteryPropertiesRegistrar)
  {
    mContext = paramContext;
    mBatteryStats = paramIBatteryStats;
    mBatteryPropertiesRegistrar = paramIBatteryPropertiesRegistrar;
  }
  
  public static boolean isPlugWired(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 2) {
      if (paramInt == 1) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private long queryProperty(int paramInt)
  {
    Object localObject = mBatteryPropertiesRegistrar;
    long l = Long.MIN_VALUE;
    if (localObject == null) {
      return Long.MIN_VALUE;
    }
    try
    {
      localObject = new android/os/BatteryProperty;
      ((BatteryProperty)localObject).<init>();
      if (mBatteryPropertiesRegistrar.getProperty(paramInt, (BatteryProperty)localObject) == 0) {
        l = ((BatteryProperty)localObject).getLong();
      }
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long computeChargeTimeRemaining()
  {
    try
    {
      long l = mBatteryStats.computeChargeTimeRemaining();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public Intent getBatteryInfo()
  {
    try
    {
      Intent localIntent = mBatteryStats.getBatteryInfo();
      return localIntent;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public int getIntProperty(int paramInt)
  {
    long l = queryProperty(paramInt);
    if ((l == Long.MIN_VALUE) && (mContext != null) && (mContext.getApplicationInfo().targetSdkVersion >= 28)) {
      return Integer.MIN_VALUE;
    }
    return (int)l;
  }
  
  public long getLongProperty(int paramInt)
  {
    return queryProperty(paramInt);
  }
  
  public boolean isCharging()
  {
    try
    {
      boolean bool = mBatteryStats.isCharging();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
}
