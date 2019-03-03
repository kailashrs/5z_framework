package android.telephony.mbms;

import android.os.Binder;
import java.util.List;
import java.util.concurrent.Executor;

public class InternalDownloadSessionCallback
  extends IMbmsDownloadSessionCallback.Stub
{
  private final MbmsDownloadSessionCallback mAppCallback;
  private final Executor mExecutor;
  private volatile boolean mIsStopped = false;
  
  public InternalDownloadSessionCallback(MbmsDownloadSessionCallback paramMbmsDownloadSessionCallback, Executor paramExecutor)
  {
    mAppCallback = paramMbmsDownloadSessionCallback;
    mExecutor = paramExecutor;
  }
  
  public void onError(final int paramInt, final String paramString)
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
  
  public void onFileServicesUpdated(final List<FileServiceInfo> paramList)
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
          mAppCallback.onFileServicesUpdated(paramList);
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
  
  public void stop()
  {
    mIsStopped = true;
  }
}
