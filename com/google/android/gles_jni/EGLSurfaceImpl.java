package com.google.android.gles_jni;

import javax.microedition.khronos.egl.EGLSurface;

public class EGLSurfaceImpl
  extends EGLSurface
{
  long mEGLSurface;
  
  public EGLSurfaceImpl()
  {
    mEGLSurface = 0L;
  }
  
  public EGLSurfaceImpl(long paramLong)
  {
    mEGLSurface = paramLong;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (EGLSurfaceImpl)paramObject;
      if (mEGLSurface != mEGLSurface) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return 31 * 17 + (int)(mEGLSurface ^ mEGLSurface >>> 32);
  }
}
