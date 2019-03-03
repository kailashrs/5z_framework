package android.hardware.location;

import android.annotation.SystemApi;

@SystemApi
public final class GeofenceHardwareRequest
{
  static final int GEOFENCE_TYPE_CIRCLE = 0;
  private int mLastTransition = 4;
  private double mLatitude;
  private double mLongitude;
  private int mMonitorTransitions = 7;
  private int mNotificationResponsiveness = 5000;
  private double mRadius;
  private int mSourceTechnologies = 1;
  private int mType;
  private int mUnknownTimer = 30000;
  
  public GeofenceHardwareRequest() {}
  
  public static GeofenceHardwareRequest createCircularGeofence(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    GeofenceHardwareRequest localGeofenceHardwareRequest = new GeofenceHardwareRequest();
    localGeofenceHardwareRequest.setCircularGeofence(paramDouble1, paramDouble2, paramDouble3);
    return localGeofenceHardwareRequest;
  }
  
  private void setCircularGeofence(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    mLatitude = paramDouble1;
    mLongitude = paramDouble2;
    mRadius = paramDouble3;
    mType = 0;
  }
  
  public int getLastTransition()
  {
    return mLastTransition;
  }
  
  public double getLatitude()
  {
    return mLatitude;
  }
  
  public double getLongitude()
  {
    return mLongitude;
  }
  
  public int getMonitorTransitions()
  {
    return mMonitorTransitions;
  }
  
  public int getNotificationResponsiveness()
  {
    return mNotificationResponsiveness;
  }
  
  public double getRadius()
  {
    return mRadius;
  }
  
  public int getSourceTechnologies()
  {
    return mSourceTechnologies;
  }
  
  int getType()
  {
    return mType;
  }
  
  public int getUnknownTimer()
  {
    return mUnknownTimer;
  }
  
  public void setLastTransition(int paramInt)
  {
    mLastTransition = paramInt;
  }
  
  public void setMonitorTransitions(int paramInt)
  {
    mMonitorTransitions = paramInt;
  }
  
  public void setNotificationResponsiveness(int paramInt)
  {
    mNotificationResponsiveness = paramInt;
  }
  
  public void setSourceTechnologies(int paramInt)
  {
    paramInt &= 0x1F;
    if (paramInt != 0)
    {
      mSourceTechnologies = paramInt;
      return;
    }
    throw new IllegalArgumentException("At least one valid source technology must be set.");
  }
  
  public void setUnknownTimer(int paramInt)
  {
    mUnknownTimer = paramInt;
  }
}
