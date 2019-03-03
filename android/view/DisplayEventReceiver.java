package android.view;

import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;

public abstract class DisplayEventReceiver
{
  private static final String TAG = "DisplayEventReceiver";
  public static final int VSYNC_SOURCE_APP = 0;
  public static final int VSYNC_SOURCE_SURFACE_FLINGER = 1;
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private MessageQueue mMessageQueue;
  private long mReceiverPtr;
  
  public DisplayEventReceiver(Looper paramLooper)
  {
    this(paramLooper, 0);
  }
  
  public DisplayEventReceiver(Looper paramLooper, int paramInt)
  {
    if (paramLooper != null)
    {
      mMessageQueue = paramLooper.getQueue();
      mReceiverPtr = nativeInit(new WeakReference(this), mMessageQueue, paramInt);
      mCloseGuard.open("dispose");
      return;
    }
    throw new IllegalArgumentException("looper must not be null");
  }
  
  private void dispatchHotplug(long paramLong, int paramInt, boolean paramBoolean)
  {
    onHotplug(paramLong, paramInt, paramBoolean);
  }
  
  private void dispatchVsync(long paramLong, int paramInt1, int paramInt2)
  {
    onVsync(paramLong, paramInt1, paramInt2);
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
    mMessageQueue = null;
  }
  
  private static native void nativeDispose(long paramLong);
  
  private static native long nativeInit(WeakReference<DisplayEventReceiver> paramWeakReference, MessageQueue paramMessageQueue, int paramInt);
  
  @FastNative
  private static native void nativeScheduleVsync(long paramLong);
  
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
  
  public void onHotplug(long paramLong, int paramInt, boolean paramBoolean) {}
  
  public void onVsync(long paramLong, int paramInt1, int paramInt2) {}
  
  public void scheduleVsync()
  {
    if (mReceiverPtr == 0L) {
      Log.w("DisplayEventReceiver", "Attempted to schedule a vertical sync pulse but the display event receiver has already been disposed.");
    } else {
      nativeScheduleVsync(mReceiverPtr);
    }
  }
}
