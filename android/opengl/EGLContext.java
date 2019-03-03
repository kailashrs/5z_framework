package android.opengl;

public class EGLContext
  extends EGLObjectHandle
{
  private EGLContext(long paramLong)
  {
    super(paramLong);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof EGLContext)) {
      return false;
    }
    paramObject = (EGLContext)paramObject;
    if (getNativeHandle() != paramObject.getNativeHandle()) {
      bool = false;
    }
    return bool;
  }
}
