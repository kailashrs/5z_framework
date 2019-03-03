package android.opengl;

public class EGLDisplay
  extends EGLObjectHandle
{
  private EGLDisplay(long paramLong)
  {
    super(paramLong);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof EGLDisplay)) {
      return false;
    }
    paramObject = (EGLDisplay)paramObject;
    if (getNativeHandle() != paramObject.getNativeHandle()) {
      bool = false;
    }
    return bool;
  }
}
