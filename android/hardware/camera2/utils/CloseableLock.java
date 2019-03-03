package android.hardware.camera2.utils;

import android.util.Log;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CloseableLock
  implements AutoCloseable
{
  private static final boolean VERBOSE = false;
  private final String TAG = "CloseableLock";
  private volatile boolean mClosed = false;
  private final Condition mCondition = mLock.newCondition();
  private boolean mExclusive = false;
  private final ReentrantLock mLock = new ReentrantLock();
  private final ThreadLocal<Integer> mLockCount = new ThreadLocal()
  {
    protected Integer initialValue()
    {
      return Integer.valueOf(0);
    }
  };
  private final String mName;
  private int mSharedLocks = 0;
  
  public CloseableLock()
  {
    mName = "";
  }
  
  public CloseableLock(String paramString)
  {
    mName = paramString;
  }
  
  private void log(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("CloseableLock[");
    localStringBuilder.append(mName);
    localStringBuilder.append("]");
    Log.v(localStringBuilder.toString(), paramString);
  }
  
  public ScopedLock acquireExclusiveLock()
  {
    try
    {
      mLock.lock();
      boolean bool = mClosed;
      if (bool) {
        return null;
      }
      int i = ((Integer)mLockCount.get()).intValue();
      if ((!mExclusive) && (i > 0))
      {
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        localIllegalStateException.<init>("Cannot acquire exclusive lock while holding shared lock");
        throw localIllegalStateException;
      }
      while ((i == 0) && ((mExclusive) || (mSharedLocks > 0)))
      {
        mCondition.awaitUninterruptibly();
        bool = mClosed;
        if (bool) {
          return null;
        }
      }
      mExclusive = true;
      i = ((Integer)mLockCount.get()).intValue();
      mLockCount.set(Integer.valueOf(i + 1));
      return new ScopedLock(null);
    }
    finally
    {
      mLock.unlock();
    }
  }
  
  public ScopedLock acquireLock()
  {
    try
    {
      mLock.lock();
      boolean bool = mClosed;
      if (bool) {
        return null;
      }
      int i = ((Integer)mLockCount.get()).intValue();
      if ((mExclusive) && (i > 0))
      {
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        localIllegalStateException.<init>("Cannot acquire shared lock while holding exclusive lock");
        throw localIllegalStateException;
      }
      while (mExclusive)
      {
        mCondition.awaitUninterruptibly();
        bool = mClosed;
        if (bool) {
          return null;
        }
      }
      mSharedLocks += 1;
      i = ((Integer)mLockCount.get()).intValue();
      mLockCount.set(Integer.valueOf(i + 1));
      return new ScopedLock(null);
    }
    finally
    {
      mLock.unlock();
    }
  }
  
  public void close()
  {
    if (mClosed) {
      return;
    }
    if (acquireExclusiveLock() == null) {
      return;
    }
    if (((Integer)mLockCount.get()).intValue() == 1) {
      try
      {
        mLock.lock();
        mClosed = true;
        mExclusive = false;
        mSharedLocks = 0;
        mLockCount.remove();
        mCondition.signalAll();
        return;
      }
      finally
      {
        mLock.unlock();
      }
    }
    throw new IllegalStateException("Cannot close while one or more acquired locks are being held by this thread; release all other locks first");
  }
  
  public void releaseLock()
  {
    if (((Integer)mLockCount.get()).intValue() > 0) {
      try
      {
        mLock.lock();
        if (!mClosed)
        {
          if (!mExclusive) {
            mSharedLocks -= 1;
          } else {
            if (mSharedLocks != 0) {
              break label140;
            }
          }
          int i = ((Integer)mLockCount.get()).intValue() - 1;
          mLockCount.set(Integer.valueOf(i));
          if ((i == 0) && (mExclusive))
          {
            mExclusive = false;
            mCondition.signalAll();
          }
          else if ((i == 0) && (mSharedLocks == 0))
          {
            mCondition.signalAll();
          }
          return;
          label140:
          AssertionError localAssertionError = new java/lang/AssertionError;
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("Too many shared locks ");
          ((StringBuilder)localObject1).append(mSharedLocks);
          localAssertionError.<init>(((StringBuilder)localObject1).toString());
          throw localAssertionError;
        }
        Object localObject1 = new java/lang/IllegalStateException;
        ((IllegalStateException)localObject1).<init>("Do not release after the lock has been closed");
        throw ((Throwable)localObject1);
      }
      finally
      {
        mLock.unlock();
      }
    }
    throw new IllegalStateException("Cannot release lock that was not acquired by this thread");
  }
  
  public class ScopedLock
    implements AutoCloseable
  {
    private ScopedLock() {}
    
    public void close()
    {
      releaseLock();
    }
  }
}
