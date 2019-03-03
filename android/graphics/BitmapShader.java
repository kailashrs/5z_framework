package android.graphics;

public class BitmapShader
  extends Shader
{
  public Bitmap mBitmap;
  private int mTileX;
  private int mTileY;
  
  private BitmapShader(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    if (paramBitmap != null)
    {
      if ((paramBitmap == mBitmap) && (paramInt1 == mTileX) && (paramInt2 == mTileY)) {
        return;
      }
      mBitmap = paramBitmap;
      mTileX = paramInt1;
      mTileY = paramInt2;
      return;
    }
    throw new IllegalArgumentException("Bitmap must be non-null");
  }
  
  public BitmapShader(Bitmap paramBitmap, Shader.TileMode paramTileMode1, Shader.TileMode paramTileMode2)
  {
    this(paramBitmap, nativeInt, nativeInt);
  }
  
  private static native long nativeCreate(long paramLong, Bitmap paramBitmap, int paramInt1, int paramInt2);
  
  protected Shader copy()
  {
    BitmapShader localBitmapShader = new BitmapShader(mBitmap, mTileX, mTileY);
    copyLocalMatrix(localBitmapShader);
    return localBitmapShader;
  }
  
  long createNativeInstance(long paramLong)
  {
    return nativeCreate(paramLong, mBitmap, mTileX, mTileY);
  }
}
