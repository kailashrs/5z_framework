package android.view;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.LongSparseArray;
import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

public final class InputQueue
{
  private final LongSparseArray<ActiveInputEvent> mActiveEventArray = new LongSparseArray(20);
  private final Pools.Pool<ActiveInputEvent> mActiveInputEventPool = new Pools.SimplePool(20);
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private long mPtr = nativeInit(new WeakReference(this), Looper.myQueue());
  
  public InputQueue()
  {
    mCloseGuard.open("dispose");
  }
  
  private void finishInputEvent(long paramLong, boolean paramBoolean)
  {
    int i = mActiveEventArray.indexOfKey(paramLong);
    if (i >= 0)
    {
      ActiveInputEvent localActiveInputEvent = (ActiveInputEvent)mActiveEventArray.valueAt(i);
      mActiveEventArray.removeAt(i);
      mCallback.onFinishedInputEvent(mToken, paramBoolean);
      recycleActiveInputEvent(localActiveInputEvent);
    }
  }
  
  private static native void nativeDispose(long paramLong);
  
  private static native long nativeInit(WeakReference<InputQueue> paramWeakReference, MessageQueue paramMessageQueue);
  
  private static native long nativeSendKeyEvent(long paramLong, KeyEvent paramKeyEvent, boolean paramBoolean);
  
  private static native long nativeSendMotionEvent(long paramLong, MotionEvent paramMotionEvent);
  
  private ActiveInputEvent obtainActiveInputEvent(Object paramObject, FinishedInputEventCallback paramFinishedInputEventCallback)
  {
    ActiveInputEvent localActiveInputEvent1 = (ActiveInputEvent)mActiveInputEventPool.acquire();
    ActiveInputEvent localActiveInputEvent2 = localActiveInputEvent1;
    if (localActiveInputEvent1 == null) {
      localActiveInputEvent2 = new ActiveInputEvent(null);
    }
    mToken = paramObject;
    mCallback = paramFinishedInputEventCallback;
    return localActiveInputEvent2;
  }
  
  private void recycleActiveInputEvent(ActiveInputEvent paramActiveInputEvent)
  {
    paramActiveInputEvent.recycle();
    mActiveInputEventPool.release(paramActiveInputEvent);
  }
  
  public void dispose()
  {
    dispose(false);
  }
  
  public void dispose(boolean paramBoolean)
  {
    if (mCloseGuard != null)
    {
      if (paramBoolean) {
        mCloseGuard.warnIfOpen();
      }
      mCloseGuard.close();
    }
    if (mPtr != 0L)
    {
      nativeDispose(mPtr);
      mPtr = 0L;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      dispose(true);
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public long getNativePtr()
  {
    return mPtr;
  }
  
  public void sendInputEvent(InputEvent paramInputEvent, Object paramObject, boolean paramBoolean, FinishedInputEventCallback paramFinishedInputEventCallback)
  {
    paramObject = obtainActiveInputEvent(paramObject, paramFinishedInputEventCallback);
    long l;
    if ((paramInputEvent instanceof KeyEvent)) {
      l = nativeSendKeyEvent(mPtr, (KeyEvent)paramInputEvent, paramBoolean);
    } else {
      l = nativeSendMotionEvent(mPtr, (MotionEvent)paramInputEvent);
    }
    mActiveEventArray.put(l, paramObject);
  }
  
  private final class ActiveInputEvent
  {
    public InputQueue.FinishedInputEventCallback mCallback;
    public Object mToken;
    
    private ActiveInputEvent() {}
    
    public void recycle()
    {
      mToken = null;
      mCallback = null;
    }
  }
  
  public static abstract interface Callback
  {
    public abstract void onInputQueueCreated(InputQueue paramInputQueue);
    
    public abstract void onInputQueueDestroyed(InputQueue paramInputQueue);
  }
  
  public static abstract interface FinishedInputEventCallback
  {
    public abstract void onFinishedInputEvent(Object paramObject, boolean paramBoolean);
  }
}
