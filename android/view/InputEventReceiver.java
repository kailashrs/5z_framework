package android.view;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import android.util.SparseIntArray;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

public abstract class InputEventReceiver
{
  private static final String TAG = "InputEventReceiver";
  Choreographer mChoreographer;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private InputChannel mInputChannel;
  private MessageQueue mMessageQueue;
  private long mReceiverPtr;
  private final SparseIntArray mSeqMap = new SparseIntArray();
  
  public InputEventReceiver(InputChannel paramInputChannel, Looper paramLooper)
  {
    if (paramInputChannel != null)
    {
      if (paramLooper != null)
      {
        mInputChannel = paramInputChannel;
        mMessageQueue = paramLooper.getQueue();
        mReceiverPtr = nativeInit(new WeakReference(this), paramInputChannel, mMessageQueue);
        mCloseGuard.open("dispose");
        return;
      }
      throw new IllegalArgumentException("looper must not be null");
    }
    throw new IllegalArgumentException("inputChannel must not be null");
  }
  
  private void dispatchBatchedInputEventPending()
  {
    onBatchedInputEventPending();
  }
  
  private void dispatchInputEvent(int paramInt1, InputEvent paramInputEvent, int paramInt2)
  {
    mSeqMap.put(paramInputEvent.getSequenceNumber(), paramInt1);
    onInputEvent(paramInputEvent, paramInt2);
  }
  
  private void dispatchMotionEventInfo(int paramInt1, int paramInt2)
  {
    try
    {
      if (mChoreographer == null) {
        mChoreographer = Choreographer.getInstance();
      }
      if (mChoreographer != null) {
        mChoreographer.setMotionEventInfo(paramInt1, paramInt2);
      }
    }
    catch (Exception localException)
    {
      Log.e("InputEventReceiver", "cannot invoke setMotionEventInfo.");
    }
  }
  
  private void dispose(boolean paramBoolean)
  {
    if (mCloseGuard != null)
    {
      if (paramBoolean) {
        mCloseGuard.warnIfOpen();
      }
      mCloseGuard.close();
    }
    if (mReceiverPtr != 0L)
    {
      nativeDispose(mReceiverPtr);
      mReceiverPtr = 0L;
    }
    mInputChannel = null;
    mMessageQueue = null;
  }
  
  private static native boolean nativeConsumeBatchedInputEvents(long paramLong1, long paramLong2);
  
  private static native void nativeDispose(long paramLong);
  
  private static native void nativeFinishInputEvent(long paramLong, int paramInt, boolean paramBoolean);
  
  private static native long nativeInit(WeakReference<InputEventReceiver> paramWeakReference, InputChannel paramInputChannel, MessageQueue paramMessageQueue);
  
  public final boolean consumeBatchedInputEvents(long paramLong)
  {
    if (mReceiverPtr == 0L)
    {
      Log.w("InputEventReceiver", "Attempted to consume batched input events but the input event receiver has already been disposed.");
      return false;
    }
    return nativeConsumeBatchedInputEvents(mReceiverPtr, paramLong);
  }
  
  public void dispose()
  {
    dispose(false);
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
  
  public final void finishInputEvent(InputEvent paramInputEvent, boolean paramBoolean)
  {
    if (paramInputEvent != null)
    {
      if (mReceiverPtr == 0L)
      {
        Log.w("InputEventReceiver", "Attempted to finish an input event but the input event receiver has already been disposed.");
      }
      else
      {
        int i = mSeqMap.indexOfKey(paramInputEvent.getSequenceNumber());
        if (i < 0)
        {
          Log.w("InputEventReceiver", "Attempted to finish an input event that is not in progress.");
        }
        else
        {
          int j = mSeqMap.valueAt(i);
          mSeqMap.removeAt(i);
          nativeFinishInputEvent(mReceiverPtr, j, paramBoolean);
        }
      }
      paramInputEvent.recycleIfNeededAfterDispatch();
      return;
    }
    throw new IllegalArgumentException("event must not be null");
  }
  
  public void onBatchedInputEventPending()
  {
    consumeBatchedInputEvents(-1L);
  }
  
  public void onInputEvent(InputEvent paramInputEvent, int paramInt)
  {
    finishInputEvent(paramInputEvent, false);
  }
  
  public static abstract interface Factory
  {
    public abstract InputEventReceiver createInputEventReceiver(InputChannel paramInputChannel, Looper paramLooper);
  }
}
