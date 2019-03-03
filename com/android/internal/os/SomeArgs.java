package com.android.internal.os;

public final class SomeArgs
{
  private static final int MAX_POOL_SIZE = 10;
  static final int WAIT_FINISHED = 2;
  static final int WAIT_NONE = 0;
  static final int WAIT_WAITING = 1;
  private static SomeArgs sPool;
  private static Object sPoolLock = new Object();
  private static int sPoolSize;
  public Object arg1;
  public Object arg2;
  public Object arg3;
  public Object arg4;
  public Object arg5;
  public Object arg6;
  public Object arg7;
  public Object arg8;
  public Object arg9;
  public int argi1;
  public int argi2;
  public int argi3;
  public int argi4;
  public int argi5;
  public int argi6;
  private boolean mInPool;
  private SomeArgs mNext;
  int mWaitState = 0;
  
  private SomeArgs() {}
  
  private void clear()
  {
    arg1 = null;
    arg2 = null;
    arg3 = null;
    arg4 = null;
    arg5 = null;
    arg6 = null;
    arg7 = null;
    argi1 = 0;
    argi2 = 0;
    argi3 = 0;
    argi4 = 0;
    argi5 = 0;
    argi6 = 0;
  }
  
  public static SomeArgs obtain()
  {
    synchronized (sPoolLock)
    {
      if (sPoolSize > 0)
      {
        localSomeArgs = sPool;
        sPool = sPoolmNext;
        mNext = null;
        mInPool = false;
        sPoolSize -= 1;
        return localSomeArgs;
      }
      SomeArgs localSomeArgs = new com/android/internal/os/SomeArgs;
      localSomeArgs.<init>();
      return localSomeArgs;
    }
  }
  
  public void complete()
  {
    try
    {
      if (mWaitState == 1)
      {
        mWaitState = 2;
        notifyAll();
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("Not waiting");
      throw localIllegalStateException;
    }
    finally {}
  }
  
  public void recycle()
  {
    if (!mInPool)
    {
      if (mWaitState != 0) {
        return;
      }
      synchronized (sPoolLock)
      {
        clear();
        if (sPoolSize < 10)
        {
          mNext = sPool;
          mInPool = true;
          sPool = this;
          sPoolSize += 1;
        }
        return;
      }
    }
    throw new IllegalStateException("Already recycled.");
  }
}
