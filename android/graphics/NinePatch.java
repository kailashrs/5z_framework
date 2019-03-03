package android.graphics;

public class NinePatch
{
  private final Bitmap mBitmap;
  public long mNativeChunk;
  private Paint mPaint;
  private String mSrcName;
  
  public NinePatch(Bitmap paramBitmap, byte[] paramArrayOfByte)
  {
    this(paramBitmap, paramArrayOfByte, null);
  }
  
  public NinePatch(Bitmap paramBitmap, byte[] paramArrayOfByte, String paramString)
  {
    mBitmap = paramBitmap;
    mSrcName = paramString;
    mNativeChunk = validateNinePatchChunk(paramArrayOfByte);
  }
  
  public NinePatch(NinePatch paramNinePatch)
  {
    mBitmap = mBitmap;
    mSrcName = mSrcName;
    if (mPaint != null) {
      mPaint = new Paint(mPaint);
    }
    mNativeChunk = mNativeChunk;
  }
  
  public static native boolean isNinePatchChunk(byte[] paramArrayOfByte);
  
  private static native void nativeFinalize(long paramLong);
  
  private static native long nativeGetTransparentRegion(Bitmap paramBitmap, long paramLong, Rect paramRect);
  
  private static native long validateNinePatchChunk(byte[] paramArrayOfByte);
  
  public void draw(Canvas paramCanvas, Rect paramRect)
  {
    paramCanvas.drawPatch(this, paramRect, mPaint);
  }
  
  public void draw(Canvas paramCanvas, Rect paramRect, Paint paramPaint)
  {
    paramCanvas.drawPatch(this, paramRect, paramPaint);
  }
  
  public void draw(Canvas paramCanvas, RectF paramRectF)
  {
    paramCanvas.drawPatch(this, paramRectF, mPaint);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mNativeChunk != 0L)
      {
        nativeFinalize(mNativeChunk);
        mNativeChunk = 0L;
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  public Bitmap getBitmap()
  {
    return mBitmap;
  }
  
  public int getDensity()
  {
    return mBitmap.mDensity;
  }
  
  public int getHeight()
  {
    return mBitmap.getHeight();
  }
  
  public String getName()
  {
    return mSrcName;
  }
  
  public Paint getPaint()
  {
    return mPaint;
  }
  
  public final Region getTransparentRegion(Rect paramRect)
  {
    long l = nativeGetTransparentRegion(mBitmap, mNativeChunk, paramRect);
    if (l != 0L) {
      paramRect = new Region(l);
    } else {
      paramRect = null;
    }
    return paramRect;
  }
  
  public int getWidth()
  {
    return mBitmap.getWidth();
  }
  
  public final boolean hasAlpha()
  {
    return mBitmap.hasAlpha();
  }
  
  public void setPaint(Paint paramPaint)
  {
    mPaint = paramPaint;
  }
  
  public static class InsetStruct
  {
    public final Rect opticalRect;
    public final float outlineAlpha;
    public final float outlineRadius;
    public final Rect outlineRect;
    
    InsetStruct(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, float paramFloat1, int paramInt9, float paramFloat2)
    {
      opticalRect = new Rect(paramInt1, paramInt2, paramInt3, paramInt4);
      opticalRect.scale(paramFloat2);
      outlineRect = scaleInsets(paramInt5, paramInt6, paramInt7, paramInt8, paramFloat2);
      outlineRadius = (paramFloat1 * paramFloat2);
      outlineAlpha = (paramInt9 / 255.0F);
    }
    
    public static Rect scaleInsets(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat)
    {
      if (paramFloat == 1.0F) {
        return new Rect(paramInt1, paramInt2, paramInt3, paramInt4);
      }
      Rect localRect = new Rect();
      left = ((int)Math.ceil(paramInt1 * paramFloat));
      top = ((int)Math.ceil(paramInt2 * paramFloat));
      right = ((int)Math.ceil(paramInt3 * paramFloat));
      bottom = ((int)Math.ceil(paramInt4 * paramFloat));
      return localRect;
    }
  }
}
