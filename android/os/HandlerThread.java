package android.os;

public class HandlerThread
  extends Thread
{
  private Handler mHandler;
  Looper mLooper;
  int mPriority;
  int mTid = -1;
  
  public HandlerThread(String paramString)
  {
    super(paramString);
    mPriority = 0;
  }
  
  public HandlerThread(String paramString, int paramInt)
  {
    super(paramString);
    mPriority = paramInt;
  }
  
  public Looper getLooper()
  {
    if (!isAlive()) {
      return null;
    }
    try
    {
      for (;;)
      {
        if (isAlive())
        {
          Looper localLooper = mLooper;
          if (localLooper == null) {
            try
            {
              wait();
            }
            catch (InterruptedException localInterruptedException)
            {
              for (;;) {}
            }
          }
        }
      }
      return mLooper;
    }
    finally {}
  }
  
  public Handler getThreadHandler()
  {
    if (mHandler == null) {
      mHandler = new Handler(getLooper());
    }
    return mHandler;
  }
  
  public int getThreadId()
  {
    return mTid;
  }
  
  protected void onLooperPrepared() {}
  
  public boolean quit()
  {
    Looper localLooper = getLooper();
    if (localLooper != null)
    {
      localLooper.quit();
      return true;
    }
    return false;
  }
  
  public boolean quitSafely()
  {
    Looper localLooper = getLooper();
    if (localLooper != null)
    {
      localLooper.quitSafely();
      return true;
    }
    return false;
  }
  
  public void run()
  {
    mTid = Process.myTid();
    Looper.prepare();
    try
    {
      mLooper = Looper.myLooper();
      notifyAll();
      Process.setThreadPriority(mPriority);
      onLooperPrepared();
      Looper.loop();
      mTid = -1;
      return;
    }
    finally {}
  }
}
