package com.google.android.gles_jni;

import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL;

public class EGLContextImpl
  extends EGLContext
{
  long mEGLContext;
  private GLImpl mGLContext;
  
  public EGLContextImpl(long paramLong)
  {
    mEGLContext = paramLong;
    mGLContext = new GLImpl();
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (EGLContextImpl)paramObject;
      if (mEGLContext != mEGLContext) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public GL getGL()
  {
    return mGLContext;
  }
  
  public int hashCode()
  {
    return 31 * 17 + (int)(mEGLContext ^ mEGLContext >>> 32);
  }
}
