package android.media;

import android.os.Handler;
import android.view.Surface;
import dalvik.system.CloseGuard;

public final class RemoteDisplay
{
  public static final int DISPLAY_ERROR_CONNECTION_DROPPED = 2;
  public static final int DISPLAY_ERROR_UNKOWN = 1;
  public static final int DISPLAY_FLAG_SECURE = 1;
  private final CloseGuard mGuard = CloseGuard.get();
  private final Handler mHandler;
  private final Listener mListener;
  private final String mOpPackageName;
  private long mPtr;
  
  private RemoteDisplay(Listener paramListener, Handler paramHandler, String paramString)
  {
    mListener = paramListener;
    mHandler = paramHandler;
    mOpPackageName = paramString;
  }
  
  private void dispose(boolean paramBoolean)
  {
    if (mPtr != 0L)
    {
      if (mGuard != null) {
        if (paramBoolean) {
          mGuard.warnIfOpen();
        } else {
          mGuard.close();
        }
      }
      nativeDispose(mPtr);
      mPtr = 0L;
    }
  }
  
  public static RemoteDisplay listen(String paramString1, Listener paramListener, Handler paramHandler, String paramString2)
  {
    if (paramString1 != null)
    {
      if (paramListener != null)
      {
        if (paramHandler != null)
        {
          paramListener = new RemoteDisplay(paramListener, paramHandler, paramString2);
          paramListener.startListening(paramString1);
          return paramListener;
        }
        throw new IllegalArgumentException("handler must not be null");
      }
      throw new IllegalArgumentException("listener must not be null");
    }
    throw new IllegalArgumentException("iface must not be null");
  }
  
  private native void nativeDispose(long paramLong);
  
  private native long nativeListen(String paramString1, String paramString2);
  
  private native void nativePause(long paramLong);
  
  private native void nativeResume(long paramLong);
  
  private void notifyDisplayConnected(final Surface paramSurface, final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4)
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        mListener.onDisplayConnected(paramSurface, paramInt1, paramInt2, paramInt3, paramInt4);
      }
    });
  }
  
  private void notifyDisplayDisconnected()
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        mListener.onDisplayDisconnected();
      }
    });
  }
  
  private void notifyDisplayError(final int paramInt)
  {
    mHandler.post(new Runnable()
    {
      public void run()
      {
        mListener.onDisplayError(paramInt);
      }
    });
  }
  
  private void startListening(String paramString)
  {
    mPtr = nativeListen(paramString, mOpPackageName);
    if (mPtr != 0L)
    {
      mGuard.open("dispose");
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not start listening for remote display connection on \"");
    localStringBuilder.append(paramString);
    localStringBuilder.append("\"");
    throw new IllegalStateException(localStringBuilder.toString());
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
  
  public void pause()
  {
    nativePause(mPtr);
  }
  
  public void resume()
  {
    nativeResume(mPtr);
  }
  
  public static abstract interface Listener
  {
    public abstract void onDisplayConnected(Surface paramSurface, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
    
    public abstract void onDisplayDisconnected();
    
    public abstract void onDisplayError(int paramInt);
  }
}
