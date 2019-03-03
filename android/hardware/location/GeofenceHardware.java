package android.hardware.location;

import android.annotation.SystemApi;
import android.location.Location;
import android.os.Build.VERSION;
import android.os.RemoteException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

@SystemApi
public final class GeofenceHardware
{
  public static final int GEOFENCE_ENTERED = 1;
  public static final int GEOFENCE_ERROR_ID_EXISTS = 2;
  public static final int GEOFENCE_ERROR_ID_UNKNOWN = 3;
  public static final int GEOFENCE_ERROR_INSUFFICIENT_MEMORY = 6;
  public static final int GEOFENCE_ERROR_INVALID_TRANSITION = 4;
  public static final int GEOFENCE_ERROR_TOO_MANY_GEOFENCES = 1;
  public static final int GEOFENCE_EXITED = 2;
  public static final int GEOFENCE_FAILURE = 5;
  public static final int GEOFENCE_SUCCESS = 0;
  public static final int GEOFENCE_UNCERTAIN = 4;
  public static final int MONITORING_TYPE_FUSED_HARDWARE = 1;
  public static final int MONITORING_TYPE_GPS_HARDWARE = 0;
  public static final int MONITOR_CURRENTLY_AVAILABLE = 0;
  public static final int MONITOR_CURRENTLY_UNAVAILABLE = 1;
  public static final int MONITOR_UNSUPPORTED = 2;
  static final int NUM_MONITORS = 2;
  public static final int SOURCE_TECHNOLOGY_BLUETOOTH = 16;
  public static final int SOURCE_TECHNOLOGY_CELL = 8;
  public static final int SOURCE_TECHNOLOGY_GNSS = 1;
  public static final int SOURCE_TECHNOLOGY_SENSORS = 4;
  public static final int SOURCE_TECHNOLOGY_WIFI = 2;
  private HashMap<GeofenceHardwareCallback, GeofenceHardwareCallbackWrapper> mCallbacks = new HashMap();
  private HashMap<GeofenceHardwareMonitorCallback, GeofenceHardwareMonitorCallbackWrapper> mMonitorCallbacks = new HashMap();
  private IGeofenceHardware mService;
  
  public GeofenceHardware(IGeofenceHardware paramIGeofenceHardware)
  {
    mService = paramIGeofenceHardware;
  }
  
  private GeofenceHardwareCallbackWrapper getCallbackWrapper(GeofenceHardwareCallback paramGeofenceHardwareCallback)
  {
    synchronized (mCallbacks)
    {
      GeofenceHardwareCallbackWrapper localGeofenceHardwareCallbackWrapper1 = (GeofenceHardwareCallbackWrapper)mCallbacks.get(paramGeofenceHardwareCallback);
      GeofenceHardwareCallbackWrapper localGeofenceHardwareCallbackWrapper2 = localGeofenceHardwareCallbackWrapper1;
      if (localGeofenceHardwareCallbackWrapper1 == null)
      {
        localGeofenceHardwareCallbackWrapper2 = new android/hardware/location/GeofenceHardware$GeofenceHardwareCallbackWrapper;
        localGeofenceHardwareCallbackWrapper2.<init>(this, paramGeofenceHardwareCallback);
        mCallbacks.put(paramGeofenceHardwareCallback, localGeofenceHardwareCallbackWrapper2);
      }
      return localGeofenceHardwareCallbackWrapper2;
    }
  }
  
  private GeofenceHardwareMonitorCallbackWrapper getMonitorCallbackWrapper(GeofenceHardwareMonitorCallback paramGeofenceHardwareMonitorCallback)
  {
    synchronized (mMonitorCallbacks)
    {
      GeofenceHardwareMonitorCallbackWrapper localGeofenceHardwareMonitorCallbackWrapper1 = (GeofenceHardwareMonitorCallbackWrapper)mMonitorCallbacks.get(paramGeofenceHardwareMonitorCallback);
      GeofenceHardwareMonitorCallbackWrapper localGeofenceHardwareMonitorCallbackWrapper2 = localGeofenceHardwareMonitorCallbackWrapper1;
      if (localGeofenceHardwareMonitorCallbackWrapper1 == null)
      {
        localGeofenceHardwareMonitorCallbackWrapper2 = new android/hardware/location/GeofenceHardware$GeofenceHardwareMonitorCallbackWrapper;
        localGeofenceHardwareMonitorCallbackWrapper2.<init>(this, paramGeofenceHardwareMonitorCallback);
        mMonitorCallbacks.put(paramGeofenceHardwareMonitorCallback, localGeofenceHardwareMonitorCallbackWrapper2);
      }
      return localGeofenceHardwareMonitorCallbackWrapper2;
    }
  }
  
  private void removeCallback(GeofenceHardwareCallback paramGeofenceHardwareCallback)
  {
    synchronized (mCallbacks)
    {
      mCallbacks.remove(paramGeofenceHardwareCallback);
      return;
    }
  }
  
  private void removeMonitorCallback(GeofenceHardwareMonitorCallback paramGeofenceHardwareMonitorCallback)
  {
    synchronized (mMonitorCallbacks)
    {
      mMonitorCallbacks.remove(paramGeofenceHardwareMonitorCallback);
      return;
    }
  }
  
  public boolean addGeofence(int paramInt1, int paramInt2, GeofenceHardwareRequest paramGeofenceHardwareRequest, GeofenceHardwareCallback paramGeofenceHardwareCallback)
  {
    try
    {
      if (paramGeofenceHardwareRequest.getType() == 0)
      {
        IGeofenceHardware localIGeofenceHardware = mService;
        GeofenceHardwareRequestParcelable localGeofenceHardwareRequestParcelable = new android/hardware/location/GeofenceHardwareRequestParcelable;
        localGeofenceHardwareRequestParcelable.<init>(paramInt1, paramGeofenceHardwareRequest);
        return localIGeofenceHardware.addCircularFence(paramInt2, localGeofenceHardwareRequestParcelable, getCallbackWrapper(paramGeofenceHardwareCallback));
      }
      paramGeofenceHardwareRequest = new java/lang/IllegalArgumentException;
      paramGeofenceHardwareRequest.<init>("Geofence Request type not supported");
      throw paramGeofenceHardwareRequest;
    }
    catch (RemoteException paramGeofenceHardwareRequest) {}
    return false;
  }
  
  public int[] getMonitoringTypes()
  {
    try
    {
      int[] arrayOfInt = mService.getMonitoringTypes();
      return arrayOfInt;
    }
    catch (RemoteException localRemoteException) {}
    return new int[0];
  }
  
  public int getStatusOfMonitoringType(int paramInt)
  {
    try
    {
      paramInt = mService.getStatusOfMonitoringType(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException) {}
    return 2;
  }
  
  public boolean pauseGeofence(int paramInt1, int paramInt2)
  {
    try
    {
      boolean bool = mService.pauseGeofence(paramInt1, paramInt2);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean registerForMonitorStateChangeCallback(int paramInt, GeofenceHardwareMonitorCallback paramGeofenceHardwareMonitorCallback)
  {
    try
    {
      boolean bool = mService.registerForMonitorStateChangeCallback(paramInt, getMonitorCallbackWrapper(paramGeofenceHardwareMonitorCallback));
      return bool;
    }
    catch (RemoteException paramGeofenceHardwareMonitorCallback) {}
    return false;
  }
  
  public boolean removeGeofence(int paramInt1, int paramInt2)
  {
    try
    {
      boolean bool = mService.removeGeofence(paramInt1, paramInt2);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean resumeGeofence(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      boolean bool = mService.resumeGeofence(paramInt1, paramInt2, paramInt3);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean unregisterForMonitorStateChangeCallback(int paramInt, GeofenceHardwareMonitorCallback paramGeofenceHardwareMonitorCallback)
  {
    boolean bool1 = false;
    try
    {
      boolean bool2 = mService.unregisterForMonitorStateChangeCallback(paramInt, getMonitorCallbackWrapper(paramGeofenceHardwareMonitorCallback));
      if (bool2)
      {
        bool1 = bool2;
        removeMonitorCallback(paramGeofenceHardwareMonitorCallback);
      }
      bool1 = bool2;
    }
    catch (RemoteException paramGeofenceHardwareMonitorCallback) {}
    return bool1;
  }
  
  class GeofenceHardwareCallbackWrapper
    extends IGeofenceHardwareCallback.Stub
  {
    private WeakReference<GeofenceHardwareCallback> mCallback;
    
    GeofenceHardwareCallbackWrapper(GeofenceHardwareCallback paramGeofenceHardwareCallback)
    {
      mCallback = new WeakReference(paramGeofenceHardwareCallback);
    }
    
    public void onGeofenceAdd(int paramInt1, int paramInt2)
    {
      GeofenceHardwareCallback localGeofenceHardwareCallback = (GeofenceHardwareCallback)mCallback.get();
      if (localGeofenceHardwareCallback != null) {
        localGeofenceHardwareCallback.onGeofenceAdd(paramInt1, paramInt2);
      }
    }
    
    public void onGeofencePause(int paramInt1, int paramInt2)
    {
      GeofenceHardwareCallback localGeofenceHardwareCallback = (GeofenceHardwareCallback)mCallback.get();
      if (localGeofenceHardwareCallback != null) {
        localGeofenceHardwareCallback.onGeofencePause(paramInt1, paramInt2);
      }
    }
    
    public void onGeofenceRemove(int paramInt1, int paramInt2)
    {
      GeofenceHardwareCallback localGeofenceHardwareCallback = (GeofenceHardwareCallback)mCallback.get();
      if (localGeofenceHardwareCallback != null)
      {
        localGeofenceHardwareCallback.onGeofenceRemove(paramInt1, paramInt2);
        GeofenceHardware.this.removeCallback(localGeofenceHardwareCallback);
      }
    }
    
    public void onGeofenceResume(int paramInt1, int paramInt2)
    {
      GeofenceHardwareCallback localGeofenceHardwareCallback = (GeofenceHardwareCallback)mCallback.get();
      if (localGeofenceHardwareCallback != null) {
        localGeofenceHardwareCallback.onGeofenceResume(paramInt1, paramInt2);
      }
    }
    
    public void onGeofenceTransition(int paramInt1, int paramInt2, Location paramLocation, long paramLong, int paramInt3)
    {
      GeofenceHardwareCallback localGeofenceHardwareCallback = (GeofenceHardwareCallback)mCallback.get();
      if (localGeofenceHardwareCallback != null) {
        localGeofenceHardwareCallback.onGeofenceTransition(paramInt1, paramInt2, paramLocation, paramLong, paramInt3);
      }
    }
  }
  
  class GeofenceHardwareMonitorCallbackWrapper
    extends IGeofenceHardwareMonitorCallback.Stub
  {
    private WeakReference<GeofenceHardwareMonitorCallback> mCallback;
    
    GeofenceHardwareMonitorCallbackWrapper(GeofenceHardwareMonitorCallback paramGeofenceHardwareMonitorCallback)
    {
      mCallback = new WeakReference(paramGeofenceHardwareMonitorCallback);
    }
    
    public void onMonitoringSystemChange(GeofenceHardwareMonitorEvent paramGeofenceHardwareMonitorEvent)
    {
      GeofenceHardwareMonitorCallback localGeofenceHardwareMonitorCallback = (GeofenceHardwareMonitorCallback)mCallback.get();
      if (localGeofenceHardwareMonitorCallback == null) {
        return;
      }
      int i = paramGeofenceHardwareMonitorEvent.getMonitoringType();
      boolean bool;
      if (paramGeofenceHardwareMonitorEvent.getMonitoringStatus() == 0) {
        bool = true;
      } else {
        bool = false;
      }
      localGeofenceHardwareMonitorCallback.onMonitoringSystemChange(i, bool, paramGeofenceHardwareMonitorEvent.getLocation());
      if (Build.VERSION.SDK_INT >= 21) {
        localGeofenceHardwareMonitorCallback.onMonitoringSystemChange(paramGeofenceHardwareMonitorEvent);
      }
    }
  }
}
