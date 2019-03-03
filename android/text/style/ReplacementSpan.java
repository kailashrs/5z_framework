package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.TextPaint;

public abstract class ReplacementSpan
  extends MetricAffectingSpan
{
  public ReplacementSpan() {}
  
  public abstract void draw(Canvas paramCanvas, CharSequence paramCharSequence, int paramInt1, int paramInt2, float paramFloat, int paramInt3, int paramInt4, int paramInt5, Paint paramPaint);
  
  public abstract int getSize(Paint paramPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, Paint.FontMetricsInt paramFontMetricsInt);
  
  public void updateDrawState(TextPaint paramTextPaint) {}
  
  public void updateMeasureState(TextPaint paramTextPaint) {}
}
