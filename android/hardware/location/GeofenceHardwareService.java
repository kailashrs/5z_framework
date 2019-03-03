package android.hardware.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.IFusedGeofenceHardware;
import android.location.IGpsGeofenceHardware;
import android.os.Binder;
import android.os.IBinder;

public class GeofenceHardwareService
  extends Service
{
  private IBinder mBinder = new IGeofenceHardware.Stub()
  {
    public boolean addCircularFence(int paramAnonymousInt, GeofenceHardwareRequestParcelable paramAnonymousGeofenceHardwareRequestParcelable, IGeofenceHardwareCallback paramAnonymousIGeofenceHardwareCallback)
    {
      mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Location Hardware permission not granted to access hardware geofence");
      GeofenceHardwareService.this.checkPermission(Binder.getCallingPid(), Binder.getCallingUid(), paramAnonymousInt);
      return mGeofenceHardwareImpl.addCircularFence(paramAnonymousInt, paramAnonymousGeofenceHardwareRequestParcelable, paramAnonymousIGeofenceHardwareCallback);
    }
    
    public int[] getMonitoringTypes()
    {
      mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Location Hardware permission not granted to access hardware geofence");
      return mGeofenceHardwareImpl.getMonitoringTypes();
    }
    
    public int getStatusOfMonitoringType(int paramAnonymousInt)
    {
      mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Location Hardware permission not granted to access hardware geofence");
      return mGeofenceHardwareImpl.getStatusOfMonitoringType(paramAnonymousInt);
    }
    
    public boolean pauseGeofence(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Location Hardware permission not granted to access hardware geofence");
      GeofenceHardwareService.this.checkPermission(Binder.getCallingPid(), Binder.getCallingUid(), paramAnonymousInt2);
      return mGeofenceHardwareImpl.pauseGeofence(paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public boolean registerForMonitorStateChangeCallback(int paramAnonymousInt, IGeofenceHardwareMonitorCallback paramAnonymousIGeofenceHardwareMonitorCallback)
    {
      mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Location Hardware permission not granted to access hardware geofence");
      GeofenceHardwareService.this.checkPermission(Binder.getCallingPid(), Binder.getCallingUid(), paramAnonymousInt);
      return mGeofenceHardwareImpl.registerForMonitorStateChangeCallback(paramAnonymousInt, paramAnonymousIGeofenceHardwareMonitorCallback);
    }
    
    public boolean removeGeofence(int paramAnonymousInt1, int paramAnonymousInt2)
    {
      mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Location Hardware permission not granted to access hardware geofence");
      GeofenceHardwareService.this.checkPermission(Binder.getCallingPid(), Binder.getCallingUid(), paramAnonymousInt2);
      return mGeofenceHardwareImpl.removeGeofence(paramAnonymousInt1, paramAnonymousInt2);
    }
    
    public boolean resumeGeofence(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
    {
      mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Location Hardware permission not granted to access hardware geofence");
      GeofenceHardwareService.this.checkPermission(Binder.getCallingPid(), Binder.getCallingUid(), paramAnonymousInt2);
      return mGeofenceHardwareImpl.resumeGeofence(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
    }
    
    public void setFusedGeofenceHardware(IFusedGeofenceHardware paramAnonymousIFusedGeofenceHardware)
    {
      mGeofenceHardwareImpl.setFusedGeofenceHardware(paramAnonymousIFusedGeofenceHardware);
    }
    
    public void setGpsGeofenceHardware(IGpsGeofenceHardware paramAnonymousIGpsGeofenceHardware)
    {
      mGeofenceHardwareImpl.setGpsHardwareGeofence(paramAnonymousIGpsGeofenceHardware);
    }
    
    public boolean unregisterForMonitorStateChangeCallback(int paramAnonymousInt, IGeofenceHardwareMonitorCallback paramAnonymousIGeofenceHardwareMonitorCallback)
    {
      mContext.enforceCallingPermission("android.permission.LOCATION_HARDWARE", "Location Hardware permission not granted to access hardware geofence");
      GeofenceHardwareService.this.checkPermission(Binder.getCallingPid(), Binder.getCallingUid(), paramAnonymousInt);
      return mGeofenceHardwareImpl.unregisterForMonitorStateChangeCallback(paramAnonymousInt, paramAnonymousIGeofenceHardwareMonitorCallback);
    }
  };
  private Context mContext;
  private GeofenceHardwareImpl mGeofenceHardwareImpl;
  
  public GeofenceHardwareService() {}
  
  private void checkPermission(int paramInt1, int paramInt2, int paramInt3)
  {
    if (mGeofenceHardwareImpl.getAllowedResolutionLevel(paramInt1, paramInt2) >= mGeofenceHardwareImpl.getMonitoringResolutionLevel(paramInt3)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Insufficient permissions to access hardware geofence for type: ");
    localStringBuilder.append(paramInt3);
    throw new SecurityException(localStringBuilder.toString());
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    return mBinder;
  }
  
  public void onCreate()
  {
    mContext = this;
    mGeofenceHardwareImpl = GeofenceHardwareImpl.getInstance(mContext);
  }
  
  public void onDestroy()
  {
    mGeofenceHardwareImpl = null;
  }
  
  public boolean onUnbind(Intent paramIntent)
  {
    return false;
  }
}
