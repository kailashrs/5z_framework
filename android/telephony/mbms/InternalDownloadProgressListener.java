package android.telephony.mbms;

import android.os.Binder;
import android.os.RemoteException;
import java.util.concurrent.Executor;

public class InternalDownloadProgressListener
  extends IDownloadProgressListener.Stub
{
  private final DownloadProgressListener mAppListener;
  private final Executor mExecutor;
  private volatile boolean mIsStopped = false;
  
  public InternalDownloadProgressListener(DownloadProgressListener paramDownloadProgressListener, Executor paramExecutor)
  {
    mAppListener = paramDownloadProgressListener;
    mExecutor = paramExecutor;
  }
  
  public void onProgressUpdated(final DownloadRequest paramDownloadRequest, final FileInfo paramFileInfo, final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4)
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
          mAppListener.onProgressUpdated(paramDownloadRequest, paramFileInfo, paramInt1, paramInt2, paramInt3, paramInt4);
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
