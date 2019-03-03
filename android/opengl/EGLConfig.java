package android.opengl;

public class EGLConfig
  extends EGLObjectHandle
{
  private EGLConfig(long paramLong)
  {
    super(paramLong);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof EGLConfig)) {
      return false;
    }
    paramObject = (EGLConfig)paramObject;
    if (getNativeHandle() != paramObject.getNativeHandle()) {
      bool = false;
    }
    return bool;
  }
}
