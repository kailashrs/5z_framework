package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract interface LineBackgroundSpan
  extends ParagraphStyle
{
  public abstract void drawBackground(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, CharSequence paramCharSequence, int paramInt6, int paramInt7, int paramInt8);
}
