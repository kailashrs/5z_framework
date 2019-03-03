package android.graphics;

public class MaskFilter
{
  long native_instance;
  
  public MaskFilter() {}
  
  private static native void nativeDestructor(long paramLong);
  
  protected void finalize()
    throws Throwable
  {
    nativeDestructor(native_instance);
    native_instance = 0L;
  }
}
