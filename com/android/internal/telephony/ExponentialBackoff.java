package com.android.internal.telephony;

import android.os.Handler;
import android.os.Looper;
import com.android.internal.annotations.VisibleForTesting;

public class ExponentialBackoff
{
  private long mCurrentDelayMs;
  private final Handler mHandler;
  private HandlerAdapter mHandlerAdapter = new HandlerAdapter()
  {
    public boolean postDelayed(Runnable paramAnonymousRunnable, long paramAnonymousLong)
    {
      return mHandler.postDelayed(paramAnonymousRunnable, paramAnonymousLong);
    }
    
    public void removeCallbacks(Runnable paramAnonymousRunnable)
    {
      mHandler.removeCallbacks(paramAnonymousRunnable);
    }
  };
  private long mMaximumDelayMs;
  private int mMultiplier;
  private int mRetryCounter = 0;
  private final Runnable mRunnable;
  private long mStartDelayMs;
  
  public ExponentialBackoff(long paramLong1, long paramLong2, int paramInt, Handler paramHandler, Runnable paramRunnable)
  {
    mStartDelayMs = paramLong1;
    mMaximumDelayMs = paramLong2;
    mMultiplier = paramInt;
    mHandler = paramHandler;
    mRunnable = paramRunnable;
  }
  
  public ExponentialBackoff(long paramLong1, long paramLong2, int paramInt, Looper paramLooper, Runnable paramRunnable)
  {
    this(paramLong1, paramLong2, paramInt, new Handler(paramLooper), paramRunnable);
  }
  
  public long getCurrentDelay()
  {
    return mCurrentDelayMs;
  }
  
  public void notifyFailed()
  {
    mRetryCounter += 1;
    long l = Math.min(mMaximumDelayMs, (mStartDelayMs * Math.pow(mMultiplier, mRetryCounter)));
    mCurrentDelayMs = (((1.0D + Math.random()) / 2.0D * l));
    mHandlerAdapter.removeCallbacks(mRunnable);
    mHandlerAdapter.postDelayed(mRunnable, mCurrentDelayMs);
  }
  
  @VisibleForTesting
  public void setHandlerAdapter(HandlerAdapter paramHandlerAdapter)
  {
    mHandlerAdapter = paramHandlerAdapter;
  }
  
  public void start()
  {
    mRetryCounter = 0;
    mCurrentDelayMs = mStartDelayMs;
    mHandlerAdapter.removeCallbacks(mRunnable);
    mHandlerAdapter.postDelayed(mRunnable, mCurrentDelayMs);
  }
  
  public void stop()
  {
    mRetryCounter = 0;
    mHandlerAdapter.removeCallbacks(mRunnable);
  }
  
  public static abstract interface HandlerAdapter
  {
    public abstract boolean postDelayed(Runnable paramRunnable, long paramLong);
    
    public abstract void removeCallbacks(Runnable paramRunnable);
  }
}
