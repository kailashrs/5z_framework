package android.graphics;

public final class LargeBitmap
{
  private long mNativeLargeBitmap;
  private boolean mRecycled;
  
  private LargeBitmap(long paramLong)
  {
    mNativeLargeBitmap = paramLong;
    mRecycled = false;
  }
  
  private void checkRecycled(String paramString)
  {
    if (!mRecycled) {
      return;
    }
    throw new IllegalStateException(paramString);
  }
  
  private static native void nativeClean(long paramLong);
  
  private static native Bitmap nativeDecodeRegion(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, BitmapFactory.Options paramOptions);
  
  private static native int nativeGetHeight(long paramLong);
  
  private static native int nativeGetWidth(long paramLong);
  
  public Bitmap decodeRegion(Rect paramRect, BitmapFactory.Options paramOptions)
  {
    checkRecycled("decodeRegion called on recycled large bitmap");
    if ((left >= 0) && (top >= 0) && (right <= getWidth()) && (bottom <= getHeight())) {
      return nativeDecodeRegion(mNativeLargeBitmap, left, top, right - left, bottom - top, paramOptions);
    }
    throw new IllegalArgumentException("rectangle is not inside the image");
  }
  
  protected void finalize()
  {
    recycle();
  }
  
  public int getHeight()
  {
    checkRecycled("getHeight called on recycled large bitmap");
    return nativeGetHeight(mNativeLargeBitmap);
  }
  
  public int getWidth()
  {
    checkRecycled("getWidth called on recycled large bitmap");
    return nativeGetWidth(mNativeLargeBitmap);
  }
  
  public final boolean isRecycled()
  {
    return mRecycled;
  }
  
  public void recycle()
  {
    if (!mRecycled)
    {
      nativeClean(mNativeLargeBitmap);
      mRecycled = true;
    }
  }
}
