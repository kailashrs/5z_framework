package android.graphics;

import android.content.res.AssetManager.AssetInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BitmapRegionDecoder
{
  private long mNativeBitmapRegionDecoder;
  private final Object mNativeLock = new Object();
  private boolean mRecycled;
  
  private BitmapRegionDecoder(long paramLong)
  {
    mNativeBitmapRegionDecoder = paramLong;
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
  
  private static native BitmapRegionDecoder nativeNewInstance(long paramLong, boolean paramBoolean);
  
  private static native BitmapRegionDecoder nativeNewInstance(FileDescriptor paramFileDescriptor, boolean paramBoolean);
  
  private static native BitmapRegionDecoder nativeNewInstance(InputStream paramInputStream, byte[] paramArrayOfByte, boolean paramBoolean);
  
  private static native BitmapRegionDecoder nativeNewInstance(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean);
  
  public static BitmapRegionDecoder newInstance(FileDescriptor paramFileDescriptor, boolean paramBoolean)
    throws IOException
  {
    return nativeNewInstance(paramFileDescriptor, paramBoolean);
  }
  
  public static BitmapRegionDecoder newInstance(InputStream paramInputStream, boolean paramBoolean)
    throws IOException
  {
    if ((paramInputStream instanceof AssetManager.AssetInputStream)) {
      return nativeNewInstance(((AssetManager.AssetInputStream)paramInputStream).getNativeAsset(), paramBoolean);
    }
    return nativeNewInstance(paramInputStream, new byte['䀀'], paramBoolean);
  }
  
  public static BitmapRegionDecoder newInstance(String paramString, boolean paramBoolean)
    throws IOException
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    try
    {
      Object localObject3 = new java/io/FileInputStream;
      localObject2 = localObject1;
      ((FileInputStream)localObject3).<init>(paramString);
      paramString = (String)localObject3;
      localObject2 = paramString;
      localObject3 = newInstance(paramString, paramBoolean);
      try
      {
        paramString.close();
      }
      catch (IOException paramString)
      {
        for (;;) {}
      }
      return localObject3;
    }
    finally
    {
      if (localObject2 != null) {
        try
        {
          ((InputStream)localObject2).close();
        }
        catch (IOException localIOException) {}
      }
    }
  }
  
  public static BitmapRegionDecoder newInstance(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    throws IOException
  {
    if (((paramInt1 | paramInt2) >= 0) && (paramArrayOfByte.length >= paramInt1 + paramInt2)) {
      return nativeNewInstance(paramArrayOfByte, paramInt1, paramInt2, paramBoolean);
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public Bitmap decodeRegion(Rect paramRect, BitmapFactory.Options paramOptions)
  {
    BitmapFactory.Options.validate(paramOptions);
    synchronized (mNativeLock)
    {
      checkRecycled("decodeRegion called on recycled region decoder");
      if ((right > 0) && (bottom > 0) && (left < getWidth()) && (top < getHeight()))
      {
        paramRect = nativeDecodeRegion(mNativeBitmapRegionDecoder, left, top, right - left, bottom - top, paramOptions);
        return paramRect;
      }
      paramRect = new java/lang/IllegalArgumentException;
      paramRect.<init>("rectangle is outside the image");
      throw paramRect;
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      recycle();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public int getHeight()
  {
    synchronized (mNativeLock)
    {
      checkRecycled("getHeight called on recycled region decoder");
      int i = nativeGetHeight(mNativeBitmapRegionDecoder);
      return i;
    }
  }
  
  public int getWidth()
  {
    synchronized (mNativeLock)
    {
      checkRecycled("getWidth called on recycled region decoder");
      int i = nativeGetWidth(mNativeBitmapRegionDecoder);
      return i;
    }
  }
  
  public final boolean isRecycled()
  {
    return mRecycled;
  }
  
  public void recycle()
  {
    synchronized (mNativeLock)
    {
      if (!mRecycled)
      {
        nativeClean(mNativeBitmapRegionDecoder);
        mRecycled = true;
      }
      return;
    }
  }
}
