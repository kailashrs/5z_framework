package android.security;

import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.service.gatekeeper.IGateKeeperService;
import android.service.gatekeeper.IGateKeeperService.Stub;

public abstract class GateKeeper
{
  public static final long INVALID_SECURE_USER_ID = 0L;
  
  private GateKeeper() {}
  
  public static long getSecureUserId()
    throws IllegalStateException
  {
    try
    {
      long l = getService().getSecureUserId(UserHandle.myUserId());
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw new IllegalStateException("Failed to obtain secure user ID from gatekeeper", localRemoteException);
    }
  }
  
  public static IGateKeeperService getService()
  {
    IGateKeeperService localIGateKeeperService = IGateKeeperService.Stub.asInterface(ServiceManager.getService("android.service.gatekeeper.IGateKeeperService"));
    if (localIGateKeeperService != null) {
      return localIGateKeeperService;
    }
    throw new IllegalStateException("Gatekeeper service not available");
  }
}
