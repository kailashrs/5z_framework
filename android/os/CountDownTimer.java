package android.os;

public abstract class CountDownTimer
{
  private static final int MSG = 1;
  private boolean mCancelled = false;
  private final long mCountdownInterval;
  private Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      synchronized (CountDownTimer.this)
      {
        if (mCancelled) {
          return;
        }
        long l1 = mStopTimeInFuture - SystemClock.elapsedRealtime();
        if (l1 <= 0L)
        {
          onFinish();
        }
        else
        {
          long l2 = SystemClock.elapsedRealtime();
          onTick(l1);
          l2 = SystemClock.elapsedRealtime() - l2;
          if (l1 < mCountdownInterval)
          {
            l2 = l1 - l2;
            l1 = l2;
            if (l2 < 0L) {
              l1 = 0L;
            }
          }
          else
          {
            for (l2 = mCountdownInterval - l2;; l2 += mCountdownInterval)
            {
              l1 = l2;
              if (l2 >= 0L) {
                break;
              }
            }
          }
          sendMessageDelayed(obtainMessage(1), l1);
        }
        return;
      }
    }
  };
  private final long mMillisInFuture;
  private long mStopTimeInFuture;
  
  public CountDownTimer(long paramLong1, long paramLong2)
  {
    mMillisInFuture = paramLong1;
    mCountdownInterval = paramLong2;
  }
  
  public final void cancel()
  {
    try
    {
      mCancelled = true;
      mHandler.removeMessages(1);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public abstract void onFinish();
  
  public abstract void onTick(long paramLong);
  
  public final CountDownTimer start()
  {
    try
    {
      mCancelled = false;
      if (mMillisInFuture <= 0L)
      {
        onFinish();
        return this;
      }
      mStopTimeInFuture = (SystemClock.elapsedRealtime() + mMillisInFuture);
      mHandler.sendMessage(mHandler.obtainMessage(1));
      return this;
    }
    finally {}
  }
}
