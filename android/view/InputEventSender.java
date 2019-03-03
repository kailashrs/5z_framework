package android.view;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

public abstract class InputEventSender
{
  private static final String TAG = "InputEventSender";
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private InputChannel mInputChannel;
  private MessageQueue mMessageQueue;
  private long mSenderPtr;
  
  public InputEventSender(InputChannel paramInputChannel, Looper paramLooper)
  {
    if (paramInputChannel != null)
    {
      if (paramLooper != null)
      {
        mInputChannel = paramInputChannel;
        mMessageQueue = paramLooper.getQueue();
        mSenderPtr = nativeInit(new WeakReference(this), paramInputChannel, mMessageQueue);
        mCloseGuard.open("dispose");
        return;
      }
      throw new IllegalArgumentException("looper must not be null");
    }
    throw new IllegalArgumentException("inputChannel must not be null");
  }
  
  private void dispatchInputEventFinished(int paramInt, boolean paramBoolean)
  {
    onInputEventFinished(paramInt, paramBoolean);
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
    if (mSenderPtr != 0L)
    {
      nativeDispose(mSenderPtr);
      mSenderPtr = 0L;
    }
    mInputChannel = null;
    mMessageQueue = null;
  }
  
  private static native void nativeDispose(long paramLong);
  
  private static native long nativeInit(WeakReference<InputEventSender> paramWeakReference, InputChannel paramInputChannel, MessageQueue paramMessageQueue);
  
  private static native boolean nativeSendKeyEvent(long paramLong, int paramInt, KeyEvent paramKeyEvent);
  
  private static native boolean nativeSendMotionEvent(long paramLong, int paramInt, MotionEvent paramMotionEvent);
  
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
  
  public void onInputEventFinished(int paramInt, boolean paramBoolean) {}
  
  public final boolean sendInputEvent(int paramInt, InputEvent paramInputEvent)
  {
    if (paramInputEvent != null)
    {
      if (mSenderPtr == 0L)
      {
        Log.w("InputEventSender", "Attempted to send an input event but the input event sender has already been disposed.");
        return false;
      }
      if ((paramInputEvent instanceof KeyEvent)) {
        return nativeSendKeyEvent(mSenderPtr, paramInt, (KeyEvent)paramInputEvent);
      }
      return nativeSendMotionEvent(mSenderPtr, paramInt, (MotionEvent)paramInputEvent);
    }
    throw new IllegalArgumentException("event must not be null");
  }
}
