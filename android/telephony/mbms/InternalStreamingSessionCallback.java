package android.telephony.mbms;

import android.os.Binder;
import android.os.RemoteException;
import java.util.List;
import java.util.concurrent.Executor;

public class InternalStreamingSessionCallback
  extends IMbmsStreamingSessionCallback.Stub
{
  private final MbmsStreamingSessionCallback mAppCallback;
  private final Executor mExecutor;
  private volatile boolean mIsStopped = false;
  
  public InternalStreamingSessionCallback(MbmsStreamingSessionCallback paramMbmsStreamingSessionCallback, Executor paramExecutor)
  {
    mAppCallback = paramMbmsStreamingSessionCallback;
    mExecutor = paramExecutor;
  }
  
  public void onError(final int paramInt, final String paramString)
    throws RemoteException
  {
    if (mIsStopped) {
      return;
    }
    mExecutor.execute(new Runnable()
    {
      public void run()
      {
        long l = Binder.clearCallingIdentity();
        try
        {
          mAppCallback.onError(paramInt, paramString);
          return;
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    });
  }
  
  public void onMiddlewareReady()
    throws RemoteException
  {
    if (mIsStopped) {
      return;
    }
    mExecutor.execute(new Runnable()
    {
      public void run()
      {
        long l = Binder.clearCallingIdentity();
        try
        {
          mAppCallback.onMiddlewareReady();
          return;
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    });
  }
  
  public void onStreamingServicesUpdated(final List<StreamingServiceInfo> paramList)
    throws RemoteException
  {
    if (mIsStopped) {
      return;
    }
    mExecutor.execute(new Runnable()
    {
      public void run()
      {
        long l = Binder.clearCallingIdentity();
        try
        {
          mAppCallback.onStreamingServicesUpdated(paramList);
          return;
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    });
  }
  
  public void stop()
  {
    mIsStopped = true;
  }
}
