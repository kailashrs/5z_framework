package com.android.server;

import android.os.ConditionVariable;
import android.os.SystemClock;

abstract class ResettableTimeout
{
  private ConditionVariable mLock = new ConditionVariable();
  private volatile long mOffAt;
  private volatile boolean mOffCalled;
  private Thread mThread;
  
  ResettableTimeout() {}
  
  public void cancel()
  {
    try
    {
      mOffAt = 0L;
      if (mThread != null)
      {
        mThread.interrupt();
        mThread = null;
      }
      if (!mOffCalled)
      {
        mOffCalled = true;
        off();
      }
      return;
    }
    finally {}
  }
  
  public void go(long paramLong)
  {
    try
    {
      mOffAt = (SystemClock.uptimeMillis() + paramLong);
      boolean bool;
      if (mThread == null)
      {
        bool = false;
        mLock.close();
        T localT = new com/android/server/ResettableTimeout$T;
        localT.<init>(this, null);
        mThread = localT;
        mThread.start();
        mLock.block();
        mOffCalled = false;
      }
      else
      {
        bool = true;
        mThread.interrupt();
      }
      on(bool);
      return;
    }
    finally {}
  }
  
  public abstract void off();
  
  public abstract void on(boolean paramBoolean);
  
  private class T
    extends Thread
  {
    private T() {}
    
    public void run()
    {
      mLock.open();
      for (;;)
      {
        try
        {
          long l = mOffAt - SystemClock.uptimeMillis();
          if (l <= 0L)
          {
            ResettableTimeout.access$302(ResettableTimeout.this, true);
            off();
            ResettableTimeout.access$402(ResettableTimeout.this, null);
            return;
          }
          try
          {
            sleep(l);
          }
          catch (InterruptedException localInterruptedException) {}
        }
        finally {}
      }
    }
  }
}
