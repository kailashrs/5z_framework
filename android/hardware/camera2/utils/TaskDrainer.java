package android.hardware.camera2.utils;

import com.android.internal.util.Preconditions;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

public class TaskDrainer<T>
{
  private static final String TAG = "TaskDrainer";
  private final boolean DEBUG = false;
  private boolean mDrainFinished = false;
  private boolean mDraining = false;
  private final Set<T> mEarlyFinishedTaskSet = new HashSet();
  private final Executor mExecutor;
  private final DrainListener mListener;
  private final Object mLock = new Object();
  private final String mName;
  private final Set<T> mTaskSet = new HashSet();
  
  public TaskDrainer(Executor paramExecutor, DrainListener paramDrainListener)
  {
    mExecutor = ((Executor)Preconditions.checkNotNull(paramExecutor, "executor must not be null"));
    mListener = ((DrainListener)Preconditions.checkNotNull(paramDrainListener, "listener must not be null"));
    mName = null;
  }
  
  public TaskDrainer(Executor paramExecutor, DrainListener paramDrainListener, String paramString)
  {
    mExecutor = ((Executor)Preconditions.checkNotNull(paramExecutor, "executor must not be null"));
    mListener = ((DrainListener)Preconditions.checkNotNull(paramDrainListener, "listener must not be null"));
    mName = paramString;
  }
  
  private void checkIfDrainFinished()
  {
    if ((mTaskSet.isEmpty()) && (mDraining) && (!mDrainFinished))
    {
      mDrainFinished = true;
      postDrained();
    }
  }
  
  private void postDrained()
  {
    mExecutor.execute(new _..Lambda.TaskDrainer.Jb53sDskEXp_qIjiikQeCRx0wJs(this));
  }
  
  public void beginDrain()
  {
    synchronized (mLock)
    {
      if (!mDraining)
      {
        mDraining = true;
        checkIfDrainFinished();
      }
      return;
    }
  }
  
  public void taskFinished(T paramT)
  {
    synchronized (mLock)
    {
      if ((!mTaskSet.remove(paramT)) && (!mEarlyFinishedTaskSet.add(paramT)))
      {
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Task ");
        localStringBuilder.append(paramT);
        localStringBuilder.append(" was already finished");
        localIllegalStateException.<init>(localStringBuilder.toString());
        throw localIllegalStateException;
      }
      checkIfDrainFinished();
      return;
    }
  }
  
  public void taskStarted(T paramT)
  {
    synchronized (mLock)
    {
      if (!mDraining)
      {
        if ((!mEarlyFinishedTaskSet.remove(paramT)) && (!mTaskSet.add(paramT)))
        {
          IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Task ");
          localStringBuilder.append(paramT);
          localStringBuilder.append(" was already started");
          localIllegalStateException.<init>(localStringBuilder.toString());
          throw localIllegalStateException;
        }
        return;
      }
      paramT = new java/lang/IllegalStateException;
      paramT.<init>("Can't start more tasks after draining has begun");
      throw paramT;
    }
  }
  
  public static abstract interface DrainListener
  {
    public abstract void onDrained();
  }
}
