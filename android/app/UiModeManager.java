package android.app;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class UiModeManager
{
  public static String ACTION_ENTER_CAR_MODE = "android.app.action.ENTER_CAR_MODE";
  public static String ACTION_ENTER_DESK_MODE = "android.app.action.ENTER_DESK_MODE";
  public static String ACTION_EXIT_CAR_MODE = "android.app.action.EXIT_CAR_MODE";
  public static String ACTION_EXIT_DESK_MODE = "android.app.action.EXIT_DESK_MODE";
  public static final int DISABLE_CAR_MODE_GO_HOME = 1;
  public static final int ENABLE_CAR_MODE_ALLOW_SLEEP = 2;
  public static final int ENABLE_CAR_MODE_GO_CAR_HOME = 1;
  public static final int MODE_NIGHT_AUTO = 0;
  public static final int MODE_NIGHT_NO = 1;
  public static final int MODE_NIGHT_YES = 2;
  private static final String TAG = "UiModeManager";
  private IUiModeManager mService = IUiModeManager.Stub.asInterface(ServiceManager.getServiceOrThrow("uimode"));
  
  UiModeManager()
    throws ServiceManager.ServiceNotFoundException
  {}
  
  public void disableCarMode(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.disableCarMode(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void enableCarMode(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.enableCarMode(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public int getCurrentModeType()
  {
    if (mService != null) {
      try
      {
        int i = mService.getCurrentModeType();
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return 1;
  }
  
  public int getNightMode()
  {
    if (mService != null) {
      try
      {
        int i = mService.getNightMode();
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return -1;
  }
  
  public boolean isNightModeLocked()
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isNightModeLocked();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return true;
  }
  
  public boolean isUiModeLocked()
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isUiModeLocked();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return true;
  }
  
  public void setNightMode(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setNightMode(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface NightMode {}
}
