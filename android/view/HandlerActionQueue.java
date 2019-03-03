package android.view;

import android.os.Handler;
import com.android.internal.util.GrowingArrayUtils;

public class HandlerActionQueue
{
  private HandlerAction[] mActions;
  private int mCount;
  
  public HandlerActionQueue() {}
  
  public void executeActions(Handler paramHandler)
  {
    try
    {
      HandlerAction[] arrayOfHandlerAction = mActions;
      int i = 0;
      int j = mCount;
      while (i < j)
      {
        HandlerAction localHandlerAction = arrayOfHandlerAction[i];
        paramHandler.postDelayed(action, delay);
        i++;
      }
      mActions = null;
      mCount = 0;
      return;
    }
    finally {}
  }
  
  public long getDelay(int paramInt)
  {
    if (paramInt < mCount) {
      return mActions[paramInt].delay;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public Runnable getRunnable(int paramInt)
  {
    if (paramInt < mCount) {
      return mActions[paramInt].action;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public void post(Runnable paramRunnable)
  {
    postDelayed(paramRunnable, 0L);
  }
  
  public void postDelayed(Runnable paramRunnable, long paramLong)
  {
    paramRunnable = new HandlerAction(paramRunnable, paramLong);
    try
    {
      if (mActions == null) {
        mActions = new HandlerAction[4];
      }
      mActions = ((HandlerAction[])GrowingArrayUtils.append(mActions, mCount, paramRunnable));
      mCount += 1;
      return;
    }
    finally {}
  }
  
  public void removeCallbacks(Runnable paramRunnable)
  {
    try
    {
      int i = mCount;
      int j = 0;
      HandlerAction[] arrayOfHandlerAction = mActions;
      for (int k = 0; k < i; k++) {
        if (!arrayOfHandlerAction[k].matches(paramRunnable))
        {
          if (j != k) {
            arrayOfHandlerAction[j] = arrayOfHandlerAction[k];
          }
          j++;
        }
      }
      mCount = j;
      while (j < i)
      {
        arrayOfHandlerAction[j] = null;
        j++;
      }
      return;
    }
    finally {}
  }
  
  public int size()
  {
    return mCount;
  }
  
  private static class HandlerAction
  {
    final Runnable action;
    final long delay;
    
    public HandlerAction(Runnable paramRunnable, long paramLong)
    {
      action = paramRunnable;
      delay = paramLong;
    }
    
    public boolean matches(Runnable paramRunnable)
    {
      boolean bool;
      if (((paramRunnable == null) && (action == null)) || ((action != null) && (action.equals(paramRunnable)))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
}
