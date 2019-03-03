package android.os;

import android.content.Context;

public class DeviceIdleManager
{
  private final Context mContext;
  private final IDeviceIdleController mService;
  
  public DeviceIdleManager(Context paramContext, IDeviceIdleController paramIDeviceIdleController)
  {
    mContext = paramContext;
    mService = paramIDeviceIdleController;
  }
  
  public String[] getSystemPowerWhitelist()
  {
    try
    {
      String[] arrayOfString = mService.getSystemPowerWhitelist();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return new String[0];
  }
  
  public String[] getSystemPowerWhitelistExceptIdle()
  {
    try
    {
      String[] arrayOfString = mService.getSystemPowerWhitelistExceptIdle();
      return arrayOfString;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return new String[0];
  }
}
