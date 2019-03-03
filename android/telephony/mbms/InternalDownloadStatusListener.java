package android.telephony.mbms;

import android.os.Binder;
import android.os.RemoteException;
import java.util.concurrent.Executor;

public class InternalDownloadStatusListener
  extends IDownloadStatusListener.Stub
{
  private final DownloadStatusListener mAppListener;
  private final Executor mExecutor;
  private volatile boolean mIsStopped = false;
  
  public InternalDownloadStatusListener(DownloadStatusListener paramDownloadStatusListener, Executor paramExecutor)
  {
    mAppListener = paramDownloadStatusListener;
    mExecutor = paramExecutor;
  }
  
  public void onStatusUpdated(final DownloadRequest paramDownloadRequest, final FileInfo paramFileInfo, final int paramInt)
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
          mAppListener.onStatusUpdated(paramDownloadRequest, paramFileInfo, paramInt);
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
