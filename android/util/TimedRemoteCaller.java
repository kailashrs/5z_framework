package android.util;

import android.os.SystemClock;
import com.android.internal.annotations.GuardedBy;
import java.util.concurrent.TimeoutException;

public abstract class TimedRemoteCaller<T>
{
  public static final long DEFAULT_CALL_TIMEOUT_MILLIS = 5000L;
  @GuardedBy("mLock")
  private final SparseIntArray mAwaitedCalls = new SparseIntArray(1);
  private final long mCallTimeoutMillis;
  private final Object mLock = new Object();
  @GuardedBy("mLock")
  private final SparseArray<T> mReceivedCalls = new SparseArray(1);
  @GuardedBy("mLock")
  private int mSequenceCounter;
  
  public TimedRemoteCaller(long paramLong)
  {
    mCallTimeoutMillis = paramLong;
  }
  
  protected final T getResultTimed(int paramInt)
    throws TimeoutException
  {
    long l1 = SystemClock.uptimeMillis();
    try
    {
      for (;;)
      {
        synchronized (mLock)
        {
          Object localObject2;
          if (mReceivedCalls.indexOfKey(paramInt) >= 0)
          {
            localObject2 = mReceivedCalls.removeReturnOld(paramInt);
            return localObject2;
          }
          long l2 = SystemClock.uptimeMillis();
          l2 = mCallTimeoutMillis - (l2 - l1);
          if (l2 > 0L)
          {
            mLock.wait(l2);
          }
          else
          {
            mAwaitedCalls.delete(paramInt);
            TimeoutException localTimeoutException = new java/util/concurrent/TimeoutException;
            localObject2 = new java/lang/StringBuilder;
            ((StringBuilder)localObject2).<init>();
            ((StringBuilder)localObject2).append("No response for sequence: ");
            ((StringBuilder)localObject2).append(paramInt);
            localTimeoutException.<init>(((StringBuilder)localObject2).toString());
            throw localTimeoutException;
          }
        }
      }
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  protected final int onBeforeRemoteCall()
  {
    synchronized (mLock)
    {
      int i;
      do
      {
        i = mSequenceCounter;
        mSequenceCounter = (i + 1);
      } while (mAwaitedCalls.get(i) != 0);
      mAwaitedCalls.put(i, 1);
      return i;
    }
  }
  
  protected final void onRemoteMethodResult(T paramT, int paramInt)
  {
    synchronized (mLock)
    {
      int i;
      if (mAwaitedCalls.get(paramInt) != 0) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0)
      {
        mAwaitedCalls.delete(paramInt);
        mReceivedCalls.put(paramInt, paramT);
        mLock.notifyAll();
      }
      return;
    }
  }
}
