package android.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

class RoundScrollbarRenderer
{
  private static final int DEFAULT_THUMB_COLOR = 1291845631;
  private static final int DEFAULT_TRACK_COLOR = 654311423;
  private static final int MAX_SCROLLBAR_ANGLE_SWIPE = 16;
  private static final int MIN_SCROLLBAR_ANGLE_SWIPE = 6;
  private static final int SCROLLBAR_ANGLE_RANGE = 90;
  private static final float WIDTH_PERCENTAGE = 0.02F;
  private final View mParent;
  private final RectF mRect = new RectF();
  private final Paint mThumbPaint = new Paint();
  private final Paint mTrackPaint = new Paint();
  
  public RoundScrollbarRenderer(View paramView)
  {
    mThumbPaint.setAntiAlias(true);
    mThumbPaint.setStrokeCap(Paint.Cap.ROUND);
    mThumbPaint.setStyle(Paint.Style.STROKE);
    mTrackPaint.setAntiAlias(true);
    mTrackPaint.setStrokeCap(Paint.Cap.ROUND);
    mTrackPaint.setStyle(Paint.Style.STROKE);
    mParent = paramView;
  }
  
  private static int applyAlpha(int paramInt, float paramFloat)
  {
    return Color.argb((int)(Color.alpha(paramInt) * paramFloat), Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt));
  }
  
  private static float clamp(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramFloat1 < paramFloat2) {
      return paramFloat2;
    }
    if (paramFloat1 > paramFloat3) {
      return paramFloat3;
    }
    return paramFloat1;
  }
  
  private void setThumbColor(int paramInt)
  {
    if (mThumbPaint.getColor() != paramInt) {
      mThumbPaint.setColor(paramInt);
    }
  }
  
  private void setTrackColor(int paramInt)
  {
    if (mTrackPaint.getColor() != paramInt) {
      mTrackPaint.setColor(paramInt);
    }
  }
  
  public void drawRoundScrollbars(Canvas paramCanvas, float paramFloat, Rect paramRect)
  {
    if (paramFloat == 0.0F) {
      return;
    }
    float f1 = mParent.computeVerticalScrollRange();
    float f2 = mParent.computeVerticalScrollExtent();
    if ((f2 > 0.0F) && (f1 > f2))
    {
      float f3 = Math.max(0, mParent.computeVerticalScrollOffset());
      float f4 = mParent.computeVerticalScrollExtent();
      f2 = mParent.getWidth() * 0.02F;
      mThumbPaint.setStrokeWidth(f2);
      mTrackPaint.setStrokeWidth(f2);
      setThumbColor(applyAlpha(1291845631, paramFloat));
      setTrackColor(applyAlpha(654311423, paramFloat));
      paramFloat = clamp(f4 / f1 * 90.0F, 6.0F, 16.0F);
      f1 = clamp((90.0F - paramFloat) * f3 / (f1 - f4) - 45.0F, -45.0F, 45.0F - paramFloat);
      mRect.set(left - f2 / 2.0F, top, right - f2 / 2.0F, bottom);
      paramCanvas.drawArc(mRect, -45.0F, 90.0F, false, mTrackPaint);
      paramCanvas.drawArc(mRect, f1, paramFloat, false, mThumbPaint);
      return;
    }
  }
}
