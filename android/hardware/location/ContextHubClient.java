package android.hardware.location;

import android.annotation.SystemApi;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.util.concurrent.atomic.AtomicBoolean;

@SystemApi
public class ContextHubClient
  implements Closeable
{
  private final ContextHubInfo mAttachedHub;
  private IContextHubClient mClientProxy = null;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final AtomicBoolean mIsClosed = new AtomicBoolean(false);
  
  ContextHubClient(ContextHubInfo paramContextHubInfo)
  {
    mAttachedHub = paramContextHubInfo;
    mCloseGuard.open("close");
  }
  
  public void close()
  {
    if (!mIsClosed.getAndSet(true))
    {
      mCloseGuard.close();
      try
      {
        mClientProxy.close();
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public ContextHubInfo getAttachedHub()
  {
    return mAttachedHub;
  }
  
  public int sendMessageToNanoApp(NanoAppMessage paramNanoAppMessage)
  {
    Preconditions.checkNotNull(paramNanoAppMessage, "NanoAppMessage cannot be null");
    try
    {
      int i = mClientProxy.sendMessageToNanoApp(paramNanoAppMessage);
      return i;
    }
    catch (RemoteException paramNanoAppMessage)
    {
      throw paramNanoAppMessage.rethrowFromSystemServer();
    }
  }
  
  void setClientProxy(IContextHubClient paramIContextHubClient)
  {
    Preconditions.checkNotNull(paramIContextHubClient, "IContextHubClient cannot be null");
    if (mClientProxy == null)
    {
      mClientProxy = paramIContextHubClient;
      return;
    }
    throw new IllegalStateException("Cannot change client proxy multiple times");
  }
}
