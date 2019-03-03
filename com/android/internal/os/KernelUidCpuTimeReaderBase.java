package com.android.internal.os;

import android.os.SystemClock;

public abstract class KernelUidCpuTimeReaderBase<T extends Callback>
{
  protected static final boolean DEBUG = false;
  private static final long DEFAULT_THROTTLE_INTERVAL = 10000L;
  private final String TAG = getClass().getSimpleName();
  private long mLastTimeReadMs = Long.MIN_VALUE;
  private long mThrottleInterval = 10000L;
  
  public KernelUidCpuTimeReaderBase() {}
  
  public void readDelta(T paramT)
  {
    if (SystemClock.elapsedRealtime() < mLastTimeReadMs + mThrottleInterval) {
      return;
    }
    readDeltaImpl(paramT);
    mLastTimeReadMs = SystemClock.elapsedRealtime();
  }
  
  protected abstract void readDeltaImpl(T paramT);
  
  public void setThrottleInterval(long paramLong)
  {
    if (paramLong >= 0L) {
      mThrottleInterval = paramLong;
    }
  }
  
  public static abstract interface Callback {}
}
