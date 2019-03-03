package android.os;

import android.content.Context;
import android.util.Log;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class HardwarePropertiesManager
{
  public static final int DEVICE_TEMPERATURE_BATTERY = 2;
  public static final int DEVICE_TEMPERATURE_CPU = 0;
  public static final int DEVICE_TEMPERATURE_GPU = 1;
  public static final int DEVICE_TEMPERATURE_SKIN = 3;
  private static final String TAG = HardwarePropertiesManager.class.getSimpleName();
  public static final int TEMPERATURE_CURRENT = 0;
  public static final int TEMPERATURE_SHUTDOWN = 2;
  public static final int TEMPERATURE_THROTTLING = 1;
  public static final int TEMPERATURE_THROTTLING_BELOW_VR_MIN = 3;
  public static final float UNDEFINED_TEMPERATURE = -3.4028235E38F;
  private final Context mContext;
  private final IHardwarePropertiesManager mService;
  
  public HardwarePropertiesManager(Context paramContext, IHardwarePropertiesManager paramIHardwarePropertiesManager)
  {
    mContext = paramContext;
    mService = paramIHardwarePropertiesManager;
  }
  
  public CpuUsageInfo[] getCpuUsages()
  {
    try
    {
      CpuUsageInfo[] arrayOfCpuUsageInfo = mService.getCpuUsages(mContext.getOpPackageName());
      return arrayOfCpuUsageInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public float[] getDeviceTemperatures(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      Log.w(TAG, "Unknown device temperature type.");
      return new float[0];
    }
    switch (paramInt2)
    {
    default: 
      Log.w(TAG, "Unknown device temperature source.");
      return new float[0];
    }
    try
    {
      float[] arrayOfFloat = mService.getDeviceTemperatures(mContext.getOpPackageName(), paramInt1, paramInt2);
      return arrayOfFloat;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public float[] getFanSpeeds()
  {
    try
    {
      float[] arrayOfFloat = mService.getFanSpeeds(mContext.getOpPackageName());
      return arrayOfFloat;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DeviceTemperatureType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TemperatureSource {}
}
