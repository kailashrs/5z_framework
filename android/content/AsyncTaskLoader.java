package android.content;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.OperationCanceledException;
import android.os.SystemClock;
import android.util.TimeUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

@Deprecated
public abstract class AsyncTaskLoader<D>
  extends Loader<D>
{
  static final boolean DEBUG = false;
  static final String TAG = "AsyncTaskLoader";
  volatile AsyncTaskLoader<D>.LoadTask mCancellingTask;
  private final Executor mExecutor;
  Handler mHandler;
  long mLastLoadCompleteTime = -10000L;
  volatile AsyncTaskLoader<D>.LoadTask mTask;
  long mUpdateThrottle;
  
  public AsyncTaskLoader(Context paramContext)
  {
    this(paramContext, AsyncTask.THREAD_POOL_EXECUTOR);
  }
  
  public AsyncTaskLoader(Context paramContext, Executor paramExecutor)
  {
    super(paramContext);
    mExecutor = paramExecutor;
  }
  
  public void cancelLoadInBackground() {}
  
  void dispatchOnCancelled(AsyncTaskLoader<D>.LoadTask paramAsyncTaskLoader, D paramD)
  {
    onCanceled(paramD);
    if (mCancellingTask == paramAsyncTaskLoader)
    {
      rollbackContentChanged();
      mLastLoadCompleteTime = SystemClock.uptimeMillis();
      mCancellingTask = null;
      deliverCancellation();
      executePendingTask();
    }
  }
  
  void dispatchOnLoadComplete(AsyncTaskLoader<D>.LoadTask paramAsyncTaskLoader, D paramD)
  {
    if (mTask != paramAsyncTaskLoader)
    {
      dispatchOnCancelled(paramAsyncTaskLoader, paramD);
    }
    else if (isAbandoned())
    {
      onCanceled(paramD);
    }
    else
    {
      commitContentChanged();
      mLastLoadCompleteTime = SystemClock.uptimeMillis();
      mTask = null;
      deliverResult(paramD);
    }
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    if (mTask != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mTask=");
      paramPrintWriter.print(mTask);
      paramPrintWriter.print(" waiting=");
      paramPrintWriter.println(mTask.waiting);
    }
    if (mCancellingTask != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCancellingTask=");
      paramPrintWriter.print(mCancellingTask);
      paramPrintWriter.print(" waiting=");
      paramPrintWriter.println(mCancellingTask.waiting);
    }
    if (mUpdateThrottle != 0L)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mUpdateThrottle=");
      TimeUtils.formatDuration(mUpdateThrottle, paramPrintWriter);
      paramPrintWriter.print(" mLastLoadCompleteTime=");
      TimeUtils.formatDuration(mLastLoadCompleteTime, SystemClock.uptimeMillis(), paramPrintWriter);
      paramPrintWriter.println();
    }
  }
  
  void executePendingTask()
  {
    if ((mCancellingTask == null) && (mTask != null))
    {
      if (mTask.waiting)
      {
        mTask.waiting = false;
        mHandler.removeCallbacks(mTask);
      }
      if ((mUpdateThrottle > 0L) && (SystemClock.uptimeMillis() < mLastLoadCompleteTime + mUpdateThrottle))
      {
        mTask.waiting = true;
        mHandler.postAtTime(mTask, mLastLoadCompleteTime + mUpdateThrottle);
        return;
      }
      mTask.executeOnExecutor(mExecutor, (Void[])null);
    }
  }
  
  public boolean isLoadInBackgroundCanceled()
  {
    boolean bool;
    if (mCancellingTask != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public abstract D loadInBackground();
  
  protected boolean onCancelLoad()
  {
    if (mTask != null)
    {
      if (!mStarted) {
        mContentChanged = true;
      }
      if (mCancellingTask != null)
      {
        if (mTask.waiting)
        {
          mTask.waiting = false;
          mHandler.removeCallbacks(mTask);
        }
        mTask = null;
        return false;
      }
      if (mTask.waiting)
      {
        mTask.waiting = false;
        mHandler.removeCallbacks(mTask);
        mTask = null;
        return false;
      }
      boolean bool = mTask.cancel(false);
      if (bool)
      {
        mCancellingTask = mTask;
        cancelLoadInBackground();
      }
      mTask = null;
      return bool;
    }
    return false;
  }
  
  public void onCanceled(D paramD) {}
  
  protected void onForceLoad()
  {
    super.onForceLoad();
    cancelLoad();
    mTask = new LoadTask();
    executePendingTask();
  }
  
  protected D onLoadInBackground()
  {
    return loadInBackground();
  }
  
  public void setUpdateThrottle(long paramLong)
  {
    mUpdateThrottle = paramLong;
    if (paramLong != 0L) {
      mHandler = new Handler();
    }
  }
  
  public void waitForLoader()
  {
    LoadTask localLoadTask = mTask;
    if (localLoadTask != null) {
      localLoadTask.waitForLoader();
    }
  }
  
  final class LoadTask
    extends AsyncTask<Void, Void, D>
    implements Runnable
  {
    private final CountDownLatch mDone = new CountDownLatch(1);
    boolean waiting;
    
    LoadTask() {}
    
    protected D doInBackground(Void... paramVarArgs)
    {
      try
      {
        paramVarArgs = onLoadInBackground();
        return paramVarArgs;
      }
      catch (OperationCanceledException paramVarArgs)
      {
        if (isCancelled()) {
          return null;
        }
        throw paramVarArgs;
      }
    }
    
    protected void onCancelled(D paramD)
    {
      try
      {
        dispatchOnCancelled(this, paramD);
        return;
      }
      finally
      {
        mDone.countDown();
      }
    }
    
    protected void onPostExecute(D paramD)
    {
      try
      {
        dispatchOnLoadComplete(this, paramD);
        return;
      }
      finally
      {
        mDone.countDown();
      }
    }
    
    public void run()
    {
      waiting = false;
      executePendingTask();
    }
    
    public void waitForLoader()
    {
      try
      {
        mDone.await();
      }
      catch (InterruptedException localInterruptedException) {}
    }
  }
}
