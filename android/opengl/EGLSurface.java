package android.opengl;

public class EGLSurface
  extends EGLObjectHandle
{
  private EGLSurface(long paramLong)
  {
    super(paramLong);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof EGLSurface)) {
      return false;
    }
    paramObject = (EGLSurface)paramObject;
    if (getNativeHandle() != paramObject.getNativeHandle()) {
      bool = false;
    }
    return bool;
  }
}
