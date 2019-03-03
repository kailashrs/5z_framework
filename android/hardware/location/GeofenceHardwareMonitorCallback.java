package android.hardware.location;

import android.annotation.SystemApi;
import android.location.Location;

@SystemApi
public abstract class GeofenceHardwareMonitorCallback
{
  public GeofenceHardwareMonitorCallback() {}
  
  @Deprecated
  public void onMonitoringSystemChange(int paramInt, boolean paramBoolean, Location paramLocation) {}
  
  public void onMonitoringSystemChange(GeofenceHardwareMonitorEvent paramGeofenceHardwareMonitorEvent) {}
}
