package com.android.internal.util;

import android.os.SystemClock;

public class TokenBucket
{
  private int mAvailable;
  private final int mCapacity;
  private final int mFillDelta;
  private long mLastFill;
  
  public TokenBucket(int paramInt1, int paramInt2)
  {
    this(paramInt1, paramInt2, paramInt2);
  }
  
  public TokenBucket(int paramInt1, int paramInt2, int paramInt3)
  {
    mFillDelta = Preconditions.checkArgumentPositive(paramInt1, "deltaMs must be strictly positive");
    mCapacity = Preconditions.checkArgumentPositive(paramInt2, "capacity must be strictly positive");
    mAvailable = Math.min(Preconditions.checkArgumentNonnegative(paramInt3), mCapacity);
    mLastFill = scaledTime();
  }
  
  private void fill()
  {
    long l = scaledTime();
    int i = (int)(l - mLastFill);
    mAvailable = Math.min(mCapacity, mAvailable + i);
    mLastFill = l;
  }
  
  private long scaledTime()
  {
    return SystemClock.elapsedRealtime() / mFillDelta;
  }
  
  public int available()
  {
    fill();
    return mAvailable;
  }
  
  public int capacity()
  {
    return mCapacity;
  }
  
  public int get(int paramInt)
  {
    fill();
    if (paramInt <= 0) {
      return 0;
    }
    if (paramInt > mAvailable)
    {
      paramInt = mAvailable;
      mAvailable = 0;
      return paramInt;
    }
    mAvailable -= paramInt;
    return paramInt;
  }
  
  public boolean get()
  {
    boolean bool = true;
    if (get(1) != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean has()
  {
    fill();
    boolean bool;
    if (mAvailable > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void reset(int paramInt)
  {
    Preconditions.checkArgumentNonnegative(paramInt);
    mAvailable = Math.min(paramInt, mCapacity);
    mLastFill = scaledTime();
  }
}
