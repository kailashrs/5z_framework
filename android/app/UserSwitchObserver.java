package android.app;

import android.os.IRemoteCallback;
import android.os.RemoteException;

public class UserSwitchObserver
  extends IUserSwitchObserver.Stub
{
  public UserSwitchObserver() {}
  
  public void onForegroundProfileSwitch(int paramInt)
    throws RemoteException
  {}
  
  public void onLockedBootComplete(int paramInt)
    throws RemoteException
  {}
  
  public void onUserSwitchComplete(int paramInt)
    throws RemoteException
  {}
  
  public void onUserSwitching(int paramInt, IRemoteCallback paramIRemoteCallback)
    throws RemoteException
  {
    if (paramIRemoteCallback != null) {
      paramIRemoteCallback.sendResult(null);
    }
  }
}
