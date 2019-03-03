package android.graphics;

public class PathEffect
{
  long native_instance;
  
  public PathEffect() {}
  
  private static native void nativeDestructor(long paramLong);
  
  protected void finalize()
    throws Throwable
  {
    nativeDestructor(native_instance);
    native_instance = 0L;
  }
}
