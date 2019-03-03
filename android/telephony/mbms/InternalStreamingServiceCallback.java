package android.telephony.mbms;

import android.os.Binder;
import android.os.RemoteException;
import java.util.concurrent.Executor;

public class InternalStreamingServiceCallback
  extends IStreamingServiceCallback.Stub
{
  private final StreamingServiceCallback mAppCallback;
  private final Executor mExecutor;
  private volatile boolean mIsStopped = false;
  
  public InternalStreamingServiceCallback(StreamingServiceCallback paramStreamingServiceCallback, Executor paramExecutor)
  {
    mAppCallback = paramStreamingServiceCallback;
    mExecutor = paramExecutor;
  }
  
  public void onBroadcastSignalStrengthUpdated(final int paramInt)
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
          mAppCallback.onBroadcastSignalStrengthUpdated(paramInt);
          return;
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    });
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
  
  public void onMediaDescriptionUpdated()
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
          mAppCallback.onMediaDescriptionUpdated();
          return;
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    });
  }
  
  public void onStreamMethodUpdated(final int paramInt)
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
          mAppCallback.onStreamMethodUpdated(paramInt);
          return;
        }
        finally
        {
          Binder.restoreCallingIdentity(l);
        }
      }
    });
  }
  
  public void onStreamStateUpdated(final int paramInt1, final int paramInt2)
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
          mAppCallback.onStreamStateUpdated(paramInt1, paramInt2);
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
