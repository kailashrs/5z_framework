package android.view;

import android.os.Looper;
import android.os.MessageQueue;
import com.android.internal.util.VirtualRefBasePtr;
import java.lang.ref.WeakReference;

public class FrameMetricsObserver
{
  private FrameMetrics mFrameMetrics;
  Window.OnFrameMetricsAvailableListener mListener;
  private MessageQueue mMessageQueue;
  VirtualRefBasePtr mNative;
  private WeakReference<Window> mWindow;
  
  FrameMetricsObserver(Window paramWindow, Looper paramLooper, Window.OnFrameMetricsAvailableListener paramOnFrameMetricsAvailableListener)
  {
    if (paramLooper != null)
    {
      mMessageQueue = paramLooper.getQueue();
      if (mMessageQueue != null)
      {
        mFrameMetrics = new FrameMetrics();
        mWindow = new WeakReference(paramWindow);
        mListener = paramOnFrameMetricsAvailableListener;
        return;
      }
      throw new IllegalStateException("invalid looper, null message queue\n");
    }
    throw new NullPointerException("looper cannot be null");
  }
  
  private void notifyDataAvailable(int paramInt)
  {
    Window localWindow = (Window)mWindow.get();
    if (localWindow != null) {
      mListener.onFrameMetricsAvailable(localWindow, mFrameMetrics, paramInt);
    }
  }
}
