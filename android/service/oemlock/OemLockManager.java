package android.service.oemlock;

import android.annotation.SystemApi;
import android.os.RemoteException;

@SystemApi
public class OemLockManager
{
  private IOemLockService mService;
  
  public OemLockManager(IOemLockService paramIOemLockService)
  {
    mService = paramIOemLockService;
  }
  
  public boolean isDeviceOemUnlocked()
  {
    try
    {
      boolean bool = mService.isDeviceOemUnlocked();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isOemUnlockAllowed()
  {
    try
    {
      boolean bool = mService.isOemUnlockAllowed();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isOemUnlockAllowedByCarrier()
  {
    try
    {
      boolean bool = mService.isOemUnlockAllowedByCarrier();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isOemUnlockAllowedByUser()
  {
    try
    {
      boolean bool = mService.isOemUnlockAllowedByUser();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setOemUnlockAllowedByCarrier(boolean paramBoolean, byte[] paramArrayOfByte)
  {
    try
    {
      mService.setOemUnlockAllowedByCarrier(paramBoolean, paramArrayOfByte);
      return;
    }
    catch (RemoteException paramArrayOfByte)
    {
      throw paramArrayOfByte.rethrowFromSystemServer();
    }
  }
  
  public void setOemUnlockAllowedByUser(boolean paramBoolean)
  {
    try
    {
      mService.setOemUnlockAllowedByUser(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
}
