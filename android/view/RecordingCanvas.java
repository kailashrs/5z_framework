package android.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Canvas.VertexMode;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.TemporaryBuffer;
import android.text.GraphicsOperations;
import android.text.MeasuredParagraph;
import android.text.PrecomputedText;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import dalvik.annotation.optimization.FastNative;

public class RecordingCanvas
  extends Canvas
{
  public RecordingCanvas(long paramLong)
  {
    super(paramLong);
  }
  
  @FastNative
  private static native void nDrawArc(long paramLong1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean, long paramLong2);
  
  @FastNative
  private static native void nDrawBitmap(long paramLong1, Bitmap paramBitmap, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, long paramLong2, int paramInt1, int paramInt2);
  
  @FastNative
  private static native void nDrawBitmap(long paramLong1, Bitmap paramBitmap, float paramFloat1, float paramFloat2, long paramLong2, int paramInt1, int paramInt2, int paramInt3);
  
  @FastNative
  private static native void nDrawBitmap(long paramLong1, int[] paramArrayOfInt, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, int paramInt3, int paramInt4, boolean paramBoolean, long paramLong2);
  
  @FastNative
  private static native void nDrawBitmapMatrix(long paramLong1, Bitmap paramBitmap, long paramLong2, long paramLong3);
  
  @FastNative
  private static native void nDrawBitmapMesh(long paramLong1, Bitmap paramBitmap, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int[] paramArrayOfInt, int paramInt4, long paramLong2);
  
  @FastNative
  private static native void nDrawCircle(long paramLong1, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong2);
  
  @FastNative
  private static native void nDrawColor(long paramLong, int paramInt1, int paramInt2);
  
  @FastNative
  private static native void nDrawLine(long paramLong1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong2);
  
  @FastNative
  private static native void nDrawLines(long paramLong1, float[] paramArrayOfFloat, int paramInt1, int paramInt2, long paramLong2);
  
  @FastNative
  private static native void nDrawNinePatch(long paramLong1, long paramLong2, long paramLong3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong4, int paramInt1, int paramInt2);
  
  @FastNative
  private static native void nDrawOval(long paramLong1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong2);
  
  @FastNative
  private static native void nDrawPaint(long paramLong1, long paramLong2);
  
  @FastNative
  private static native void nDrawPath(long paramLong1, long paramLong2, long paramLong3);
  
  @FastNative
  private static native void nDrawPoint(long paramLong1, float paramFloat1, float paramFloat2, long paramLong2);
  
  @FastNative
  private static native void nDrawPoints(long paramLong1, float[] paramArrayOfFloat, int paramInt1, int paramInt2, long paramLong2);
  
  @FastNative
  private static native void nDrawRect(long paramLong1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong2);
  
  @FastNative
  private static native void nDrawRegion(long paramLong1, long paramLong2, long paramLong3);
  
  @FastNative
  private static native void nDrawRoundRect(long paramLong1, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, long paramLong2);
  
  @FastNative
  private static native void nDrawText(long paramLong1, String paramString, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, int paramInt3, long paramLong2);
  
  @FastNative
  private static native void nDrawText(long paramLong1, char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, int paramInt3, long paramLong2);
  
  @FastNative
  private static native void nDrawTextOnPath(long paramLong1, String paramString, long paramLong2, float paramFloat1, float paramFloat2, int paramInt, long paramLong3);
  
  @FastNative
  private static native void nDrawTextOnPath(long paramLong1, char[] paramArrayOfChar, int paramInt1, int paramInt2, long paramLong2, float paramFloat1, float paramFloat2, int paramInt3, long paramLong3);
  
  @FastNative
  private static native void nDrawTextRun(long paramLong1, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, boolean paramBoolean, long paramLong2);
  
  @FastNative
  private static native void nDrawTextRun(long paramLong1, char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, boolean paramBoolean, long paramLong2, long paramLong3);
  
  @FastNative
  private static native void nDrawVertices(long paramLong1, int paramInt1, int paramInt2, float[] paramArrayOfFloat1, int paramInt3, float[] paramArrayOfFloat2, int paramInt4, int[] paramArrayOfInt, int paramInt5, short[] paramArrayOfShort, int paramInt6, int paramInt7, long paramLong2);
  
  public final void drawARGB(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    drawColor(Color.argb(paramInt1, paramInt2, paramInt3, paramInt4));
  }
  
  public final void drawArc(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean, Paint paramPaint)
  {
    nDrawArc(mNativeCanvasWrapper, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramBoolean, paramPaint.getNativeInstance());
  }
  
  public final void drawArc(RectF paramRectF, float paramFloat1, float paramFloat2, boolean paramBoolean, Paint paramPaint)
  {
    drawArc(left, top, right, bottom, paramFloat1, paramFloat2, paramBoolean, paramPaint);
  }
  
  public final void drawBitmap(Bitmap paramBitmap, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    throwIfCannotDraw(paramBitmap);
    long l1 = mNativeCanvasWrapper;
    if (paramPaint != null) {}
    for (long l2 = paramPaint.getNativeInstance();; l2 = 0L) {
      break;
    }
    nDrawBitmap(l1, paramBitmap, paramFloat1, paramFloat2, l2, mDensity, mScreenDensity, mDensity);
  }
  
  public final void drawBitmap(Bitmap paramBitmap, Matrix paramMatrix, Paint paramPaint)
  {
    long l1 = mNativeCanvasWrapper;
    long l2 = paramMatrix.ni();
    long l3;
    if (paramPaint != null) {
      l3 = paramPaint.getNativeInstance();
    } else {
      l3 = 0L;
    }
    nDrawBitmapMatrix(l1, paramBitmap, l2, l3);
  }
  
  public final void drawBitmap(Bitmap paramBitmap, Rect paramRect1, Rect paramRect2, Paint paramPaint)
  {
    if (paramRect2 != null)
    {
      throwIfCannotDraw(paramBitmap);
      if (paramPaint == null) {}
      for (long l = 0L;; l = paramPaint.getNativeInstance()) {
        break;
      }
      int i;
      int j;
      int k;
      if (paramRect1 == null)
      {
        i = 0;
        j = 0;
        k = paramBitmap.getWidth();
      }
      for (int m = paramBitmap.getHeight();; m = bottom)
      {
        break;
        i = left;
        k = right;
        j = top;
      }
      nDrawBitmap(mNativeCanvasWrapper, paramBitmap, i, j, k, m, left, top, right, bottom, l, mScreenDensity, mDensity);
      return;
    }
    throw new NullPointerException();
  }
  
  public final void drawBitmap(Bitmap paramBitmap, Rect paramRect, RectF paramRectF, Paint paramPaint)
  {
    if (paramRectF != null)
    {
      throwIfCannotDraw(paramBitmap);
      if (paramPaint == null) {}
      for (long l = 0L;; l = paramPaint.getNativeInstance()) {
        break;
      }
      float f1;
      float f2;
      float f3;
      if (paramRect == null)
      {
        f1 = 0.0F;
        f2 = 0.0F;
        f3 = paramBitmap.getWidth();
      }
      for (float f4 = paramBitmap.getHeight();; f4 = bottom)
      {
        break;
        f1 = left;
        f3 = right;
        f2 = top;
      }
      nDrawBitmap(mNativeCanvasWrapper, paramBitmap, f1, f2, f3, f4, left, top, right, bottom, l, mScreenDensity, mDensity);
      return;
    }
    throw new NullPointerException();
  }
  
  @Deprecated
  public final void drawBitmap(int[] paramArrayOfInt, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, int paramInt3, int paramInt4, boolean paramBoolean, Paint paramPaint)
  {
    if (paramInt3 >= 0)
    {
      if (paramInt4 >= 0)
      {
        if (Math.abs(paramInt2) >= paramInt3)
        {
          int i = paramInt1 + (paramInt4 - 1) * paramInt2;
          int j = paramArrayOfInt.length;
          if ((paramInt1 >= 0) && (paramInt1 + paramInt3 <= j) && (i >= 0) && (i + paramInt3 <= j))
          {
            if ((paramInt3 != 0) && (paramInt4 != 0))
            {
              long l1 = mNativeCanvasWrapper;
              if (paramPaint != null) {}
              for (long l2 = paramPaint.getNativeInstance();; l2 = 0L) {
                break;
              }
              nDrawBitmap(l1, paramArrayOfInt, paramInt1, paramInt2, paramFloat1, paramFloat2, paramInt3, paramInt4, paramBoolean, l2);
              return;
            }
            return;
          }
          throw new ArrayIndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("abs(stride) must be >= width");
      }
      throw new IllegalArgumentException("height must be >= 0");
    }
    throw new IllegalArgumentException("width must be >= 0");
  }
  
  @Deprecated
  public final void drawBitmap(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, Paint paramPaint)
  {
    drawBitmap(paramArrayOfInt, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramBoolean, paramPaint);
  }
  
  public final void drawBitmapMesh(Bitmap paramBitmap, int paramInt1, int paramInt2, float[] paramArrayOfFloat, int paramInt3, int[] paramArrayOfInt, int paramInt4, Paint paramPaint)
  {
    if ((paramInt1 | paramInt2 | paramInt3 | paramInt4) >= 0)
    {
      if ((paramInt1 != 0) && (paramInt2 != 0))
      {
        int i = (paramInt1 + 1) * (paramInt2 + 1);
        checkRange(paramArrayOfFloat.length, paramInt3, i * 2);
        if (paramArrayOfInt != null) {
          checkRange(paramArrayOfInt.length, paramInt4, i);
        }
        long l1 = mNativeCanvasWrapper;
        if (paramPaint != null) {}
        for (long l2 = paramPaint.getNativeInstance();; l2 = 0L) {
          break;
        }
        nDrawBitmapMesh(l1, paramBitmap, paramInt1, paramInt2, paramArrayOfFloat, paramInt3, paramArrayOfInt, paramInt4, l2);
        return;
      }
      return;
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public final void drawCircle(float paramFloat1, float paramFloat2, float paramFloat3, Paint paramPaint)
  {
    nDrawCircle(mNativeCanvasWrapper, paramFloat1, paramFloat2, paramFloat3, paramPaint.getNativeInstance());
  }
  
  public final void drawColor(int paramInt)
  {
    nDrawColor(mNativeCanvasWrapper, paramInt, SRC_OVERnativeInt);
  }
  
  public final void drawColor(int paramInt, PorterDuff.Mode paramMode)
  {
    nDrawColor(mNativeCanvasWrapper, paramInt, nativeInt);
  }
  
  public final void drawLine(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Paint paramPaint)
  {
    nDrawLine(mNativeCanvasWrapper, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramPaint.getNativeInstance());
  }
  
  public final void drawLines(float[] paramArrayOfFloat, int paramInt1, int paramInt2, Paint paramPaint)
  {
    nDrawLines(mNativeCanvasWrapper, paramArrayOfFloat, paramInt1, paramInt2, paramPaint.getNativeInstance());
  }
  
  public final void drawLines(float[] paramArrayOfFloat, Paint paramPaint)
  {
    drawLines(paramArrayOfFloat, 0, paramArrayOfFloat.length, paramPaint);
  }
  
  public final void drawOval(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Paint paramPaint)
  {
    nDrawOval(mNativeCanvasWrapper, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramPaint.getNativeInstance());
  }
  
  public final void drawOval(RectF paramRectF, Paint paramPaint)
  {
    if (paramRectF != null)
    {
      drawOval(left, top, right, bottom, paramPaint);
      return;
    }
    throw new NullPointerException();
  }
  
  public final void drawPaint(Paint paramPaint)
  {
    nDrawPaint(mNativeCanvasWrapper, paramPaint.getNativeInstance());
  }
  
  public final void drawPatch(NinePatch paramNinePatch, Rect paramRect, Paint paramPaint)
  {
    Bitmap localBitmap = paramNinePatch.getBitmap();
    throwIfCannotDraw(localBitmap);
    if (paramPaint == null) {}
    for (long l = 0L;; l = paramPaint.getNativeInstance()) {
      break;
    }
    nDrawNinePatch(mNativeCanvasWrapper, localBitmap.getNativeInstance(), mNativeChunk, left, top, right, bottom, l, mDensity, paramNinePatch.getDensity());
  }
  
  public final void drawPatch(NinePatch paramNinePatch, RectF paramRectF, Paint paramPaint)
  {
    Bitmap localBitmap = paramNinePatch.getBitmap();
    throwIfCannotDraw(localBitmap);
    if (paramPaint == null) {}
    for (long l = 0L;; l = paramPaint.getNativeInstance()) {
      break;
    }
    nDrawNinePatch(mNativeCanvasWrapper, localBitmap.getNativeInstance(), mNativeChunk, left, top, right, bottom, l, mDensity, paramNinePatch.getDensity());
  }
  
  public final void drawPath(Path paramPath, Paint paramPaint)
  {
    if ((isSimplePath) && (rects != null)) {
      nDrawRegion(mNativeCanvasWrapper, rects.mNativeRegion, paramPaint.getNativeInstance());
    } else {
      nDrawPath(mNativeCanvasWrapper, paramPath.readOnlyNI(), paramPaint.getNativeInstance());
    }
  }
  
  public final void drawPicture(Picture paramPicture)
  {
    paramPicture.endRecording();
    int i = save();
    paramPicture.draw(this);
    restoreToCount(i);
  }
  
  public final void drawPicture(Picture paramPicture, Rect paramRect)
  {
    save();
    translate(left, top);
    if ((paramPicture.getWidth() > 0) && (paramPicture.getHeight() > 0)) {
      scale(paramRect.width() / paramPicture.getWidth(), paramRect.height() / paramPicture.getHeight());
    }
    drawPicture(paramPicture);
    restore();
  }
  
  public final void drawPicture(Picture paramPicture, RectF paramRectF)
  {
    save();
    translate(left, top);
    if ((paramPicture.getWidth() > 0) && (paramPicture.getHeight() > 0)) {
      scale(paramRectF.width() / paramPicture.getWidth(), paramRectF.height() / paramPicture.getHeight());
    }
    drawPicture(paramPicture);
    restore();
  }
  
  public final void drawPoint(float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    nDrawPoint(mNativeCanvasWrapper, paramFloat1, paramFloat2, paramPaint.getNativeInstance());
  }
  
  public final void drawPoints(float[] paramArrayOfFloat, int paramInt1, int paramInt2, Paint paramPaint)
  {
    nDrawPoints(mNativeCanvasWrapper, paramArrayOfFloat, paramInt1, paramInt2, paramPaint.getNativeInstance());
  }
  
  public final void drawPoints(float[] paramArrayOfFloat, Paint paramPaint)
  {
    drawPoints(paramArrayOfFloat, 0, paramArrayOfFloat.length, paramPaint);
  }
  
  @Deprecated
  public final void drawPosText(String paramString, float[] paramArrayOfFloat, Paint paramPaint)
  {
    drawPosText(paramString.toCharArray(), 0, paramString.length(), paramArrayOfFloat, paramPaint);
  }
  
  @Deprecated
  public final void drawPosText(char[] paramArrayOfChar, int paramInt1, int paramInt2, float[] paramArrayOfFloat, Paint paramPaint)
  {
    if ((paramInt1 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfChar.length) && (paramInt2 * 2 <= paramArrayOfFloat.length))
    {
      for (int i = 0; i < paramInt2; i++) {
        drawText(paramArrayOfChar, paramInt1 + i, 1, paramArrayOfFloat[(i * 2)], paramArrayOfFloat[(i * 2 + 1)], paramPaint);
      }
      return;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public final void drawRGB(int paramInt1, int paramInt2, int paramInt3)
  {
    drawColor(Color.rgb(paramInt1, paramInt2, paramInt3));
  }
  
  public final void drawRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, Paint paramPaint)
  {
    nDrawRect(mNativeCanvasWrapper, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramPaint.getNativeInstance());
  }
  
  public final void drawRect(Rect paramRect, Paint paramPaint)
  {
    drawRect(left, top, right, bottom, paramPaint);
  }
  
  public final void drawRect(RectF paramRectF, Paint paramPaint)
  {
    nDrawRect(mNativeCanvasWrapper, left, top, right, bottom, paramPaint.getNativeInstance());
  }
  
  public final void drawRoundRect(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, Paint paramPaint)
  {
    nDrawRoundRect(mNativeCanvasWrapper, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramPaint.getNativeInstance());
  }
  
  public final void drawRoundRect(RectF paramRectF, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    drawRoundRect(left, top, right, bottom, paramFloat1, paramFloat2, paramPaint);
  }
  
  public final void drawText(CharSequence paramCharSequence, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    if ((paramInt1 | paramInt2 | paramInt2 - paramInt1 | paramCharSequence.length() - paramInt2) >= 0)
    {
      if ((!(paramCharSequence instanceof String)) && (!(paramCharSequence instanceof SpannedString)) && (!(paramCharSequence instanceof SpannableString)))
      {
        if ((paramCharSequence instanceof GraphicsOperations))
        {
          ((GraphicsOperations)paramCharSequence).drawText(this, paramInt1, paramInt2, paramFloat1, paramFloat2, paramPaint);
        }
        else
        {
          char[] arrayOfChar = TemporaryBuffer.obtain(paramInt2 - paramInt1);
          TextUtils.getChars(paramCharSequence, paramInt1, paramInt2, arrayOfChar, 0);
          nDrawText(mNativeCanvasWrapper, arrayOfChar, 0, paramInt2 - paramInt1, paramFloat1, paramFloat2, mBidiFlags, paramPaint.getNativeInstance());
          TemporaryBuffer.recycle(arrayOfChar);
        }
      }
      else {
        nDrawText(mNativeCanvasWrapper, paramCharSequence.toString(), paramInt1, paramInt2, paramFloat1, paramFloat2, mBidiFlags, paramPaint.getNativeInstance());
      }
      return;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public final void drawText(String paramString, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    nDrawText(mNativeCanvasWrapper, paramString, 0, paramString.length(), paramFloat1, paramFloat2, mBidiFlags, paramPaint.getNativeInstance());
  }
  
  public final void drawText(String paramString, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    if ((paramInt1 | paramInt2 | paramInt2 - paramInt1 | paramString.length() - paramInt2) >= 0)
    {
      nDrawText(mNativeCanvasWrapper, paramString, paramInt1, paramInt2, paramFloat1, paramFloat2, mBidiFlags, paramPaint.getNativeInstance());
      return;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public final void drawText(char[] paramArrayOfChar, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    if ((paramInt1 | paramInt2 | paramInt1 + paramInt2 | paramArrayOfChar.length - paramInt1 - paramInt2) >= 0)
    {
      nDrawText(mNativeCanvasWrapper, paramArrayOfChar, paramInt1, paramInt2, paramFloat1, paramFloat2, mBidiFlags, paramPaint.getNativeInstance());
      return;
    }
    throw new IndexOutOfBoundsException();
  }
  
  public final void drawTextOnPath(String paramString, Path paramPath, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    if (paramString.length() > 0) {
      nDrawTextOnPath(mNativeCanvasWrapper, paramString, paramPath.readOnlyNI(), paramFloat1, paramFloat2, mBidiFlags, paramPaint.getNativeInstance());
    }
  }
  
  public final void drawTextOnPath(char[] paramArrayOfChar, int paramInt1, int paramInt2, Path paramPath, float paramFloat1, float paramFloat2, Paint paramPaint)
  {
    if (paramInt1 >= 0) {
      if (paramInt1 + paramInt2 <= paramArrayOfChar.length)
      {
        nDrawTextOnPath(mNativeCanvasWrapper, paramArrayOfChar, paramInt1, paramInt2, paramPath.readOnlyNI(), paramFloat1, paramFloat2, mBidiFlags, paramPaint.getNativeInstance());
        return;
      }
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public final void drawTextRun(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, boolean paramBoolean, Paint paramPaint)
  {
    if (paramCharSequence != null)
    {
      if (paramPaint != null)
      {
        if ((paramInt1 | paramInt2 | paramInt3 | paramInt4 | paramInt1 - paramInt3 | paramInt2 - paramInt1 | paramInt4 - paramInt2 | paramCharSequence.length() - paramInt4) >= 0)
        {
          if ((!(paramCharSequence instanceof String)) && (!(paramCharSequence instanceof SpannedString)) && (!(paramCharSequence instanceof SpannableString)))
          {
            if ((paramCharSequence instanceof GraphicsOperations))
            {
              ((GraphicsOperations)paramCharSequence).drawTextRun(this, paramInt1, paramInt2, paramInt3, paramInt4, paramFloat1, paramFloat2, paramBoolean, paramPaint);
            }
            else
            {
              int i = paramInt4 - paramInt3;
              char[] arrayOfChar = TemporaryBuffer.obtain(i);
              TextUtils.getChars(paramCharSequence, paramInt3, paramInt4, arrayOfChar, 0);
              long l1 = 0L;
              long l2 = l1;
              if ((paramCharSequence instanceof PrecomputedText))
              {
                paramCharSequence = (PrecomputedText)paramCharSequence;
                paramInt4 = paramCharSequence.findParaIndex(paramInt1);
                l2 = l1;
                if (paramInt2 <= paramCharSequence.getParagraphEnd(paramInt4)) {
                  l2 = paramCharSequence.getMeasuredParagraph(paramInt4).getNativePtr();
                }
              }
              nDrawTextRun(mNativeCanvasWrapper, arrayOfChar, paramInt1 - paramInt3, paramInt2 - paramInt1, 0, i, paramFloat1, paramFloat2, paramBoolean, paramPaint.getNativeInstance(), l2);
              TemporaryBuffer.recycle(arrayOfChar);
            }
          }
          else {
            nDrawTextRun(mNativeCanvasWrapper, paramCharSequence.toString(), paramInt1, paramInt2, paramInt3, paramInt4, paramFloat1, paramFloat2, paramBoolean, paramPaint.getNativeInstance());
          }
          return;
        }
        throw new IndexOutOfBoundsException();
      }
      throw new NullPointerException("paint is null");
    }
    throw new NullPointerException("text is null");
  }
  
  public final void drawTextRun(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, float paramFloat2, boolean paramBoolean, Paint paramPaint)
  {
    if (paramArrayOfChar != null)
    {
      if (paramPaint != null)
      {
        if ((paramInt1 | paramInt2 | paramInt3 | paramInt4 | paramInt1 - paramInt3 | paramInt3 + paramInt4 - (paramInt1 + paramInt2) | paramArrayOfChar.length - (paramInt3 + paramInt4)) >= 0)
        {
          nDrawTextRun(mNativeCanvasWrapper, paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4, paramFloat1, paramFloat2, paramBoolean, paramPaint.getNativeInstance(), 0L);
          return;
        }
        throw new IndexOutOfBoundsException();
      }
      throw new NullPointerException("paint is null");
    }
    throw new NullPointerException("text is null");
  }
  
  public final void drawVertices(Canvas.VertexMode paramVertexMode, int paramInt1, float[] paramArrayOfFloat1, int paramInt2, float[] paramArrayOfFloat2, int paramInt3, int[] paramArrayOfInt, int paramInt4, short[] paramArrayOfShort, int paramInt5, int paramInt6, Paint paramPaint)
  {
    checkRange(paramArrayOfFloat1.length, paramInt2, paramInt1);
    if (isHardwareAccelerated()) {
      return;
    }
    if (paramArrayOfFloat2 != null) {
      checkRange(paramArrayOfFloat2.length, paramInt3, paramInt1);
    }
    if (paramArrayOfInt != null) {
      checkRange(paramArrayOfInt.length, paramInt4, paramInt1 / 2);
    }
    if (paramArrayOfShort != null) {
      checkRange(paramArrayOfShort.length, paramInt5, paramInt6);
    }
    nDrawVertices(mNativeCanvasWrapper, nativeInt, paramInt1, paramArrayOfFloat1, paramInt2, paramArrayOfFloat2, paramInt3, paramArrayOfInt, paramInt4, paramArrayOfShort, paramInt5, paramInt6, paramPaint.getNativeInstance());
  }
}
