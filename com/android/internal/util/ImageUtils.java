package com.android.internal.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtils
{
  private static final int ALPHA_TOLERANCE = 50;
  private static final int COMPACT_BITMAP_SIZE = 64;
  private static final int TOLERANCE = 20;
  private int[] mTempBuffer;
  private Bitmap mTempCompactBitmap;
  private Canvas mTempCompactBitmapCanvas;
  private Paint mTempCompactBitmapPaint;
  private final Matrix mTempMatrix = new Matrix();
  
  public ImageUtils() {}
  
  public static Bitmap buildScaledBitmap(Drawable paramDrawable, int paramInt1, int paramInt2)
  {
    if (paramDrawable == null) {
      return null;
    }
    int i = paramDrawable.getIntrinsicWidth();
    int j = paramDrawable.getIntrinsicHeight();
    if ((i <= paramInt1) && (j <= paramInt2) && ((paramDrawable instanceof BitmapDrawable))) {
      return ((BitmapDrawable)paramDrawable).getBitmap();
    }
    if ((j > 0) && (i > 0))
    {
      float f = Math.min(1.0F, Math.min(paramInt1 / i, paramInt2 / j));
      paramInt1 = (int)(i * f);
      paramInt2 = (int)(j * f);
      Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
      Canvas localCanvas = new Canvas(localBitmap);
      paramDrawable.setBounds(0, 0, paramInt1, paramInt2);
      paramDrawable.draw(localCanvas);
      return localBitmap;
    }
    return null;
  }
  
  private void ensureBufferSize(int paramInt)
  {
    if ((mTempBuffer == null) || (mTempBuffer.length < paramInt)) {
      mTempBuffer = new int[paramInt];
    }
  }
  
  public static boolean isGrayscale(int paramInt)
  {
    boolean bool = true;
    if ((paramInt >> 24 & 0xFF) < 50) {
      return true;
    }
    int i = paramInt >> 16 & 0xFF;
    int j = paramInt >> 8 & 0xFF;
    paramInt = 0xFF & paramInt;
    if ((Math.abs(i - j) >= 20) || (Math.abs(i - paramInt) >= 20) || (Math.abs(j - paramInt) >= 20)) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isGrayscale(Bitmap paramBitmap)
  {
    int i = paramBitmap.getHeight();
    int j = paramBitmap.getWidth();
    int k;
    Bitmap localBitmap;
    if (i <= 64)
    {
      k = i;
      m = j;
      localBitmap = paramBitmap;
      if (j <= 64) {}
    }
    else
    {
      if (mTempCompactBitmap == null)
      {
        mTempCompactBitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        mTempCompactBitmapCanvas = new Canvas(mTempCompactBitmap);
        mTempCompactBitmapPaint = new Paint(1);
        mTempCompactBitmapPaint.setFilterBitmap(true);
      }
      mTempMatrix.reset();
      mTempMatrix.setScale(64.0F / j, 64.0F / i, 0.0F, 0.0F);
      mTempCompactBitmapCanvas.drawColor(0, PorterDuff.Mode.SRC);
      mTempCompactBitmapCanvas.drawBitmap(paramBitmap, mTempMatrix, mTempCompactBitmapPaint);
      localBitmap = mTempCompactBitmap;
      k = 64;
      m = 64;
    }
    i = k * m;
    ensureBufferSize(i);
    localBitmap.getPixels(mTempBuffer, 0, m, 0, 0, m, k);
    for (int m = 0; m < i; m++) {
      if (!isGrayscale(mTempBuffer[m])) {
        return false;
      }
    }
    return true;
  }
}
