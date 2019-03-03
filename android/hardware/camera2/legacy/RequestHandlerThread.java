package android.hardware.camera2.legacy;

import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;

public class RequestHandlerThread
  extends HandlerThread
{
  public static final int MSG_POKE_IDLE_HANDLER = -1;
  private Handler.Callback mCallback;
  private volatile Handler mHandler;
  private final ConditionVariable mIdle = new ConditionVariable(true);
  private final MessageQueue.IdleHandler mIdleHandler = new MessageQueue.IdleHandler()
  {
    public boolean queueIdle()
    {
      mIdle.open();
      return false;
    }
  };
  private final ConditionVariable mStarted = new ConditionVariable(false);
  
  public RequestHandlerThread(String paramString, Handler.Callback paramCallback)
  {
    super(paramString, 10);
    mCallback = paramCallback;
  }
  
  public Handler getHandler()
  {
    return mHandler;
  }
  
  public boolean hasAnyMessages(int[] paramArrayOfInt)
  {
    synchronized (mHandler.getLooper().getQueue())
    {
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++)
      {
        int k = paramArrayOfInt[j];
        if (mHandler.hasMessages(k)) {
          return true;
        }
      }
      return false;
    }
  }
  
  protected void onLooperPrepared()
  {
    mHandler = new Handler(getLooper(), mCallback);
    mStarted.open();
  }
  
  public void removeMessages(int[] paramArrayOfInt)
  {
    synchronized (mHandler.getLooper().getQueue())
    {
      int i = paramArrayOfInt.length;
      for (int j = 0; j < i; j++)
      {
        int k = paramArrayOfInt[j];
        mHandler.removeMessages(k);
      }
      return;
    }
  }
  
  public Handler waitAndGetHandler()
  {
    waitUntilStarted();
    return getHandler();
  }
  
  public void waitUntilIdle()
  {
    Handler localHandler = waitAndGetHandler();
    MessageQueue localMessageQueue = localHandler.getLooper().getQueue();
    if (localMessageQueue.isIdle()) {
      return;
    }
    mIdle.close();
    localMessageQueue.addIdleHandler(mIdleHandler);
    localHandler.sendEmptyMessage(-1);
    if (localMessageQueue.isIdle()) {
      return;
    }
    mIdle.block();
  }
  
  public void waitUntilStarted()
  {
    mStarted.block();
  }
}
