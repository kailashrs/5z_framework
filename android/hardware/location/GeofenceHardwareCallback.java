package android.hardware.location;

import android.annotation.SystemApi;
import android.location.Location;

@SystemApi
public abstract class GeofenceHardwareCallback
{
  public GeofenceHardwareCallback() {}
  
  public void onGeofenceAdd(int paramInt1, int paramInt2) {}
  
  public void onGeofencePause(int paramInt1, int paramInt2) {}
  
  public void onGeofenceRemove(int paramInt1, int paramInt2) {}
  
  public void onGeofenceResume(int paramInt1, int paramInt2) {}
  
  public void onGeofenceTransition(int paramInt1, int paramInt2, Location paramLocation, long paramLong, int paramInt3) {}
}
