package android.graphics;

import android.content.res.AssetManager.AssetInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Movie
{
  private long mNativeMovie;
  
  private Movie(long paramLong)
  {
    if (paramLong != 0L)
    {
      mNativeMovie = paramLong;
      return;
    }
    throw new RuntimeException("native movie creation failed");
  }
  
  public static native Movie decodeByteArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public static Movie decodeFile(String paramString)
  {
    try
    {
      paramString = new FileInputStream(paramString);
      return decodeTempStream(paramString);
    }
    catch (FileNotFoundException paramString) {}
    return null;
  }
  
  public static Movie decodeStream(InputStream paramInputStream)
  {
    if (paramInputStream == null) {
      return null;
    }
    if ((paramInputStream instanceof AssetManager.AssetInputStream)) {
      return nativeDecodeAsset(((AssetManager.AssetInputStream)paramInputStream).getNativeAsset());
    }
    return nativeDecodeStream(paramInputStream);
  }
  
  private static Movie decodeTempStream(InputStream paramInputStream)
  {
    Object localObject = null;
    try
    {
      Movie localMovie = decodeStream(paramInputStream);
      localObject = localMovie;
      paramInputStream.close();
      localObject = localMovie;
    }
    catch (IOException paramInputStream) {}
    return localObject;
  }
  
  private native void nDraw(long paramLong1, float paramFloat1, float paramFloat2, long paramLong2);
  
  private static native Movie nativeDecodeAsset(long paramLong);
  
  private static native Movie nativeDecodeStream(InputStream paramInputStream);
  
  private static native void nativeDestructor(long paramLong);
  
  public void draw(Canvas paramCanvas, float paramFloat1, float paramFloat2)
  {
    nDraw(paramCanvas.getNativeCanvasWrapper(), paramFloat1, paramFloat2, 0L);
  }
  
  public void draw(Canvas paramCanvas, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    long l1 = paramCanvas.getNativeCanvasWrapper();
    if (paramPaint != null) {}
    for (long l2 = paramPaint.getNativeInstance();; l2 = 0L) {
      break;
    }
    nDraw(l1, paramFloat1, paramFloat2, l2);
  }
  
  public native int duration();
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      nativeDestructor(mNativeMovie);
      mNativeMovie = 0L;
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public native int height();
  
  public native boolean isOpaque();
  
  public native boolean setTime(int paramInt);
  
  public native int width();
}
