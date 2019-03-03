package android.app;

import android.os.IRemoteCallback;
import android.os.RemoteException;

public abstract class SynchronousUserSwitchObserver
  extends UserSwitchObserver
{
  public SynchronousUserSwitchObserver() {}
  
  public abstract void onUserSwitching(int paramInt)
    throws RemoteException;
  
  public final void onUserSwitching(int paramInt, IRemoteCallback paramIRemoteCallback)
    throws RemoteException
  {
    try
    {
      onUserSwitching(paramInt);
      return;
    }
    finally
    {
      if (paramIRemoteCallback != null) {
        paramIRemoteCallback.sendResult(null);
      }
    }
  }
}
