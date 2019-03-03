package android.graphics;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.view.Surface.OutOfResourcesException;
import java.lang.ref.WeakReference;

public class SurfaceTexture
{
  private final Looper mCreatorLooper = Looper.myLooper();
  private long mFrameAvailableListener;
  private boolean mIsSingleBuffered;
  private Handler mOnFrameAvailableHandler;
  private long mProducer;
  private long mSurfaceTexture;
  
  public SurfaceTexture(int paramInt)
  {
    this(paramInt, false);
  }
  
  public SurfaceTexture(int paramInt, boolean paramBoolean)
  {
    mIsSingleBuffered = paramBoolean;
    nativeInit(false, paramInt, paramBoolean, new WeakReference(this));
  }
  
  public SurfaceTexture(boolean paramBoolean)
  {
    mIsSingleBuffered = paramBoolean;
    nativeInit(true, 0, paramBoolean, new WeakReference(this));
  }
  
  private native int nativeAttachToGLContext(int paramInt);
  
  private native int nativeDetachFromGLContext();
  
  private native void nativeFinalize();
  
  private native long nativeGetTimestamp();
  
  private native void nativeGetTransformMatrix(float[] paramArrayOfFloat);
  
  private native void nativeInit(boolean paramBoolean1, int paramInt, boolean paramBoolean2, WeakReference<SurfaceTexture> paramWeakReference)
    throws Surface.OutOfResourcesException;
  
  private native boolean nativeIsReleased();
  
  private native void nativeRelease();
  
  private native void nativeReleaseTexImage();
  
  private native void nativeSetDefaultBufferSize(int paramInt1, int paramInt2);
  
  private native void nativeUpdateTexImage();
  
  private static void postEventFromNative(WeakReference<SurfaceTexture> paramWeakReference)
  {
    paramWeakReference = (SurfaceTexture)paramWeakReference.get();
    if (paramWeakReference != null)
    {
      paramWeakReference = mOnFrameAvailableHandler;
      if (paramWeakReference != null) {
        paramWeakReference.sendEmptyMessage(0);
      }
    }
  }
  
  public void attachToGLContext(int paramInt)
  {
    if (nativeAttachToGLContext(paramInt) == 0) {
      return;
    }
    throw new RuntimeException("Error during attachToGLContext (see logcat for details)");
  }
  
  public void detachFromGLContext()
  {
    if (nativeDetachFromGLContext() == 0) {
      return;
    }
    throw new RuntimeException("Error during detachFromGLContext (see logcat for details)");
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      nativeFinalize();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public long getTimestamp()
  {
    return nativeGetTimestamp();
  }
  
  public void getTransformMatrix(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat.length == 16)
    {
      nativeGetTransformMatrix(paramArrayOfFloat);
      return;
    }
    throw new IllegalArgumentException();
  }
  
  public boolean isReleased()
  {
    return nativeIsReleased();
  }
  
  public boolean isSingleBuffered()
  {
    return mIsSingleBuffered;
  }
  
  public void release()
  {
    nativeRelease();
  }
  
  public void releaseTexImage()
  {
    nativeReleaseTexImage();
  }
  
  public void setDefaultBufferSize(int paramInt1, int paramInt2)
  {
    nativeSetDefaultBufferSize(paramInt1, paramInt2);
  }
  
  public void setOnFrameAvailableListener(OnFrameAvailableListener paramOnFrameAvailableListener)
  {
    setOnFrameAvailableListener(paramOnFrameAvailableListener, null);
  }
  
  public void setOnFrameAvailableListener(final OnFrameAvailableListener paramOnFrameAvailableListener, Handler paramHandler)
  {
    if (paramOnFrameAvailableListener != null)
    {
      if (paramHandler != null) {
        paramHandler = paramHandler.getLooper();
      }
      for (;;)
      {
        break;
        if (mCreatorLooper != null) {
          paramHandler = mCreatorLooper;
        } else {
          paramHandler = Looper.getMainLooper();
        }
      }
      mOnFrameAvailableHandler = new Handler(paramHandler, null, true)
      {
        public void handleMessage(Message paramAnonymousMessage)
        {
          paramOnFrameAvailableListener.onFrameAvailable(SurfaceTexture.this);
        }
      };
    }
    else
    {
      mOnFrameAvailableHandler = null;
    }
  }
  
  public void updateTexImage()
  {
    nativeUpdateTexImage();
  }
  
  public static abstract interface OnFrameAvailableListener
  {
    public abstract void onFrameAvailable(SurfaceTexture paramSurfaceTexture);
  }
  
  @Deprecated
  public static class OutOfResourcesException
    extends Exception
  {
    public OutOfResourcesException() {}
    
    public OutOfResourcesException(String paramString)
    {
      super();
    }
  }
}
