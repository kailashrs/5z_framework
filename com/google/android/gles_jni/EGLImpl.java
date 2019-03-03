package com.google.android.gles_jni;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class EGLImpl
  implements EGL10
{
  private EGLContextImpl mContext = new EGLContextImpl(-1L);
  private EGLDisplayImpl mDisplay = new EGLDisplayImpl(-1L);
  private EGLSurfaceImpl mSurface = new EGLSurfaceImpl(-1L);
  
  static {}
  
  public EGLImpl() {}
  
  private native long _eglCreateContext(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, EGLContext paramEGLContext, int[] paramArrayOfInt);
  
  private native long _eglCreatePbufferSurface(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, int[] paramArrayOfInt);
  
  private native void _eglCreatePixmapSurface(EGLSurface paramEGLSurface, EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject, int[] paramArrayOfInt);
  
  private native long _eglCreateWindowSurface(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject, int[] paramArrayOfInt);
  
  private native long _eglCreateWindowSurfaceTexture(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject, int[] paramArrayOfInt);
  
  private native long _eglGetCurrentContext();
  
  private native long _eglGetCurrentDisplay();
  
  private native long _eglGetCurrentSurface(int paramInt);
  
  private native long _eglGetDisplay(Object paramObject);
  
  private static native void _nativeClassInit();
  
  public static native int getInitCount(EGLDisplay paramEGLDisplay);
  
  public native boolean eglChooseConfig(EGLDisplay paramEGLDisplay, int[] paramArrayOfInt1, EGLConfig[] paramArrayOfEGLConfig, int paramInt, int[] paramArrayOfInt2);
  
  public native boolean eglCopyBuffers(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface, Object paramObject);
  
  public EGLContext eglCreateContext(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, EGLContext paramEGLContext, int[] paramArrayOfInt)
  {
    long l = _eglCreateContext(paramEGLDisplay, paramEGLConfig, paramEGLContext, paramArrayOfInt);
    if (l == 0L) {
      return EGL10.EGL_NO_CONTEXT;
    }
    return new EGLContextImpl(l);
  }
  
  public EGLSurface eglCreatePbufferSurface(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, int[] paramArrayOfInt)
  {
    long l = _eglCreatePbufferSurface(paramEGLDisplay, paramEGLConfig, paramArrayOfInt);
    if (l == 0L) {
      return EGL10.EGL_NO_SURFACE;
    }
    return new EGLSurfaceImpl(l);
  }
  
  public EGLSurface eglCreatePixmapSurface(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject, int[] paramArrayOfInt)
  {
    EGLSurfaceImpl localEGLSurfaceImpl = new EGLSurfaceImpl();
    _eglCreatePixmapSurface(localEGLSurfaceImpl, paramEGLDisplay, paramEGLConfig, paramObject, paramArrayOfInt);
    if (mEGLSurface == 0L) {
      return EGL10.EGL_NO_SURFACE;
    }
    return localEGLSurfaceImpl;
  }
  
  public EGLSurface eglCreateWindowSurface(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, Object paramObject, int[] paramArrayOfInt)
  {
    Surface localSurface = null;
    if ((paramObject instanceof SurfaceView)) {
      localSurface = ((SurfaceView)paramObject).getHolder().getSurface();
    } else if ((paramObject instanceof SurfaceHolder)) {
      localSurface = ((SurfaceHolder)paramObject).getSurface();
    } else if ((paramObject instanceof Surface)) {
      localSurface = (Surface)paramObject;
    }
    if (localSurface != null) {}
    for (long l = _eglCreateWindowSurface(paramEGLDisplay, paramEGLConfig, localSurface, paramArrayOfInt);; l = _eglCreateWindowSurfaceTexture(paramEGLDisplay, paramEGLConfig, paramObject, paramArrayOfInt))
    {
      break;
      if (!(paramObject instanceof SurfaceTexture)) {
        break label123;
      }
    }
    if (l == 0L) {
      return EGL10.EGL_NO_SURFACE;
    }
    return new EGLSurfaceImpl(l);
    label123:
    throw new UnsupportedOperationException("eglCreateWindowSurface() can only be called with an instance of Surface, SurfaceView, SurfaceHolder or SurfaceTexture at the moment.");
  }
  
  public native boolean eglDestroyContext(EGLDisplay paramEGLDisplay, EGLContext paramEGLContext);
  
  public native boolean eglDestroySurface(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface);
  
  public native boolean eglGetConfigAttrib(EGLDisplay paramEGLDisplay, EGLConfig paramEGLConfig, int paramInt, int[] paramArrayOfInt);
  
  public native boolean eglGetConfigs(EGLDisplay paramEGLDisplay, EGLConfig[] paramArrayOfEGLConfig, int paramInt, int[] paramArrayOfInt);
  
  public EGLContext eglGetCurrentContext()
  {
    try
    {
      long l = _eglGetCurrentContext();
      if (l == 0L)
      {
        localObject1 = EGL10.EGL_NO_CONTEXT;
        return localObject1;
      }
      if (mContext.mEGLContext != l)
      {
        localObject1 = new com/google/android/gles_jni/EGLContextImpl;
        ((EGLContextImpl)localObject1).<init>(l);
        mContext = ((EGLContextImpl)localObject1);
      }
      Object localObject1 = mContext;
      return localObject1;
    }
    finally {}
  }
  
  public EGLDisplay eglGetCurrentDisplay()
  {
    try
    {
      long l = _eglGetCurrentDisplay();
      if (l == 0L)
      {
        localObject1 = EGL10.EGL_NO_DISPLAY;
        return localObject1;
      }
      if (mDisplay.mEGLDisplay != l)
      {
        localObject1 = new com/google/android/gles_jni/EGLDisplayImpl;
        ((EGLDisplayImpl)localObject1).<init>(l);
        mDisplay = ((EGLDisplayImpl)localObject1);
      }
      Object localObject1 = mDisplay;
      return localObject1;
    }
    finally {}
  }
  
  public EGLSurface eglGetCurrentSurface(int paramInt)
  {
    try
    {
      long l = _eglGetCurrentSurface(paramInt);
      if (l == 0L)
      {
        localObject1 = EGL10.EGL_NO_SURFACE;
        return localObject1;
      }
      if (mSurface.mEGLSurface != l)
      {
        localObject1 = new com/google/android/gles_jni/EGLSurfaceImpl;
        ((EGLSurfaceImpl)localObject1).<init>(l);
        mSurface = ((EGLSurfaceImpl)localObject1);
      }
      Object localObject1 = mSurface;
      return localObject1;
    }
    finally {}
  }
  
  public EGLDisplay eglGetDisplay(Object paramObject)
  {
    try
    {
      long l = _eglGetDisplay(paramObject);
      if (l == 0L)
      {
        paramObject = EGL10.EGL_NO_DISPLAY;
        return paramObject;
      }
      if (mDisplay.mEGLDisplay != l)
      {
        paramObject = new com/google/android/gles_jni/EGLDisplayImpl;
        paramObject.<init>(l);
        mDisplay = paramObject;
      }
      paramObject = mDisplay;
      return paramObject;
    }
    finally {}
  }
  
  public native int eglGetError();
  
  public native boolean eglInitialize(EGLDisplay paramEGLDisplay, int[] paramArrayOfInt);
  
  public native boolean eglMakeCurrent(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface1, EGLSurface paramEGLSurface2, EGLContext paramEGLContext);
  
  public native boolean eglQueryContext(EGLDisplay paramEGLDisplay, EGLContext paramEGLContext, int paramInt, int[] paramArrayOfInt);
  
  public native String eglQueryString(EGLDisplay paramEGLDisplay, int paramInt);
  
  public native boolean eglQuerySurface(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface, int paramInt, int[] paramArrayOfInt);
  
  public native boolean eglReleaseThread();
  
  public native boolean eglSwapBuffers(EGLDisplay paramEGLDisplay, EGLSurface paramEGLSurface);
  
  public native boolean eglTerminate(EGLDisplay paramEGLDisplay);
  
  public native boolean eglWaitGL();
  
  public native boolean eglWaitNative(int paramInt, Object paramObject);
}
