package android.filterfw.core;

import android.graphics.SurfaceTexture;
import android.media.MediaRecorder;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;

public class GLEnvironment
{
  private int glEnvId;
  private boolean mManageContext = true;
  
  static
  {
    System.loadLibrary("filterfw");
  }
  
  public GLEnvironment()
  {
    nativeAllocate();
  }
  
  private GLEnvironment(NativeAllocatorTag paramNativeAllocatorTag) {}
  
  public static boolean isAnyContextActive()
  {
    return nativeIsAnyContextActive();
  }
  
  private native boolean nativeActivate();
  
  private native boolean nativeActivateSurfaceId(int paramInt);
  
  private native int nativeAddSurface(Surface paramSurface);
  
  private native int nativeAddSurfaceFromMediaRecorder(MediaRecorder paramMediaRecorder);
  
  private native int nativeAddSurfaceWidthHeight(Surface paramSurface, int paramInt1, int paramInt2);
  
  private native boolean nativeAllocate();
  
  private native boolean nativeDeactivate();
  
  private native boolean nativeDeallocate();
  
  private native boolean nativeDisconnectSurfaceMediaSource(MediaRecorder paramMediaRecorder);
  
  private native boolean nativeInitWithCurrentContext();
  
  private native boolean nativeInitWithNewContext();
  
  private native boolean nativeIsActive();
  
  private static native boolean nativeIsAnyContextActive();
  
  private native boolean nativeIsContextActive();
  
  private native boolean nativeRemoveSurfaceId(int paramInt);
  
  private native boolean nativeSetSurfaceTimestamp(long paramLong);
  
  private native boolean nativeSwapBuffers();
  
  public void activate()
  {
    if ((Looper.myLooper() != null) && (Looper.myLooper().equals(Looper.getMainLooper()))) {
      Log.e("FilterFramework", "Activating GL context in UI thread!");
    }
    if ((mManageContext) && (!nativeActivate())) {
      throw new RuntimeException("Could not activate GLEnvironment!");
    }
  }
  
  public void activateSurfaceWithId(int paramInt)
  {
    if (nativeActivateSurfaceId(paramInt)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not activate surface ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void deactivate()
  {
    if ((mManageContext) && (!nativeDeactivate())) {
      throw new RuntimeException("Could not deactivate GLEnvironment!");
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    tearDown();
  }
  
  public void initWithCurrentContext()
  {
    mManageContext = false;
    if (nativeInitWithCurrentContext()) {
      return;
    }
    throw new RuntimeException("Could not initialize GLEnvironment with current context!");
  }
  
  public void initWithNewContext()
  {
    mManageContext = true;
    if (nativeInitWithNewContext()) {
      return;
    }
    throw new RuntimeException("Could not initialize GLEnvironment with new context!");
  }
  
  public boolean isActive()
  {
    return nativeIsActive();
  }
  
  public boolean isContextActive()
  {
    return nativeIsContextActive();
  }
  
  public int registerSurface(Surface paramSurface)
  {
    int i = nativeAddSurface(paramSurface);
    if (i >= 0) {
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Error registering surface ");
    localStringBuilder.append(paramSurface);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public int registerSurfaceFromMediaRecorder(MediaRecorder paramMediaRecorder)
  {
    int i = nativeAddSurfaceFromMediaRecorder(paramMediaRecorder);
    if (i >= 0) {
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Error registering surface from MediaRecorder");
    localStringBuilder.append(paramMediaRecorder);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public int registerSurfaceTexture(SurfaceTexture paramSurfaceTexture, int paramInt1, int paramInt2)
  {
    Object localObject = new Surface(paramSurfaceTexture);
    paramInt1 = nativeAddSurfaceWidthHeight((Surface)localObject, paramInt1, paramInt2);
    ((Surface)localObject).release();
    if (paramInt1 >= 0) {
      return paramInt1;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Error registering surfaceTexture ");
    ((StringBuilder)localObject).append(paramSurfaceTexture);
    ((StringBuilder)localObject).append("!");
    throw new RuntimeException(((StringBuilder)localObject).toString());
  }
  
  public void setSurfaceTimestamp(long paramLong)
  {
    if (nativeSetSurfaceTimestamp(paramLong)) {
      return;
    }
    throw new RuntimeException("Could not set timestamp for current surface!");
  }
  
  public void swapBuffers()
  {
    if (nativeSwapBuffers()) {
      return;
    }
    throw new RuntimeException("Error swapping EGL buffers!");
  }
  
  public void tearDown()
  {
    try
    {
      if (glEnvId != -1)
      {
        nativeDeallocate();
        glEnvId = -1;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void unregisterSurfaceId(int paramInt)
  {
    if (nativeRemoveSurfaceId(paramInt)) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Could not unregister surface ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("!");
    throw new RuntimeException(localStringBuilder.toString());
  }
}
