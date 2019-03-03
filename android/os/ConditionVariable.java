package android.os;

public class ConditionVariable
{
  private volatile boolean mCondition;
  
  public ConditionVariable()
  {
    mCondition = false;
  }
  
  public ConditionVariable(boolean paramBoolean)
  {
    mCondition = paramBoolean;
  }
  
  public void block()
  {
    try
    {
      for (;;)
      {
        boolean bool = mCondition;
        if (!bool) {
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
      return;
    }
    finally {}
  }
  
  public boolean block(long paramLong)
  {
    if (paramLong != 0L) {
      try
      {
        long l1 = System.currentTimeMillis();
        long l2 = l1 + paramLong;
        for (paramLong = l1;; paramLong = System.currentTimeMillis())
        {
          bool = mCondition;
          if ((bool) || (paramLong >= l2)) {
            break;
          }
          try
          {
            wait(l2 - paramLong);
          }
          catch (InterruptedException localInterruptedException) {}
        }
        boolean bool = mCondition;
        return bool;
      }
      finally {}
    }
    block();
    return true;
  }
  
  public void close()
  {
    try
    {
      mCondition = false;
      return;
    }
    finally {}
  }
  
  public void open()
  {
    try
    {
      boolean bool = mCondition;
      mCondition = true;
      if (!bool) {
        notifyAll();
      }
      return;
    }
    finally {}
  }
}
