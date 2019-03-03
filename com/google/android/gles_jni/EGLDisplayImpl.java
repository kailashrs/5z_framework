package com.google.android.gles_jni;

import javax.microedition.khronos.egl.EGLDisplay;

public class EGLDisplayImpl
  extends EGLDisplay
{
  long mEGLDisplay;
  
  public EGLDisplayImpl(long paramLong)
  {
    mEGLDisplay = paramLong;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (EGLDisplayImpl)paramObject;
      if (mEGLDisplay != mEGLDisplay) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return 31 * 17 + (int)(mEGLDisplay ^ mEGLDisplay >>> 32);
  }
}
