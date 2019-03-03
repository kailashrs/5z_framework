package android.graphics;

public class RegionIterator
{
  private long mNativeIter;
  
  public RegionIterator(Region paramRegion)
  {
    mNativeIter = nativeConstructor(paramRegion.ni());
  }
  
  private static native long nativeConstructor(long paramLong);
  
  private static native void nativeDestructor(long paramLong);
  
  private static native boolean nativeNext(long paramLong, Rect paramRect);
  
  protected void finalize()
    throws Throwable
  {
    nativeDestructor(mNativeIter);
    mNativeIter = 0L;
  }
  
  public final boolean next(Rect paramRect)
  {
    if (paramRect != null) {
      return nativeNext(mNativeIter, paramRect);
    }
    throw new NullPointerException("The Rect must be provided");
  }
}
