package android.graphics;

public class DrawFilter
{
  public long mNativeInt;
  
  public DrawFilter() {}
  
  private static native void nativeDestructor(long paramLong);
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      nativeDestructor(mNativeInt);
      mNativeInt = 0L;
      return;
    }
    finally
    {
      super.finalize();
    }
  }
}
