package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spanned;

public class DrawableMarginSpan
  implements LeadingMarginSpan, LineHeightSpan
{
  private static final int STANDARD_PAD_WIDTH = 0;
  private final Drawable mDrawable;
  private final int mPad;
  
  public DrawableMarginSpan(Drawable paramDrawable)
  {
    this(paramDrawable, 0);
  }
  
  public DrawableMarginSpan(Drawable paramDrawable, int paramInt)
  {
    mDrawable = paramDrawable;
    mPad = paramInt;
  }
  
  public void chooseHeight(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Paint.FontMetricsInt paramFontMetricsInt)
  {
    if (paramInt2 == ((Spanned)paramCharSequence).getSpanEnd(this))
    {
      paramInt1 = mDrawable.getIntrinsicHeight();
      paramInt2 = paramInt1 - (descent + paramInt4 - ascent - paramInt3);
      if (paramInt2 > 0) {
        descent += paramInt2;
      }
      paramInt1 -= bottom + paramInt4 - top - paramInt3;
      if (paramInt1 > 0) {
        bottom += paramInt1;
      }
    }
  }
  
  public void drawLeadingMargin(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, CharSequence paramCharSequence, int paramInt6, int paramInt7, boolean paramBoolean, Layout paramLayout)
  {
    paramInt2 = paramLayout.getLineTop(paramLayout.getLineForOffset(((Spanned)paramCharSequence).getSpanStart(this)));
    paramInt4 = mDrawable.getIntrinsicWidth();
    paramInt3 = mDrawable.getIntrinsicHeight();
    mDrawable.setBounds(paramInt1, paramInt2, paramInt1 + paramInt4, paramInt2 + paramInt3);
    mDrawable.draw(paramCanvas);
  }
  
  public int getLeadingMargin(boolean paramBoolean)
  {
    return mDrawable.getIntrinsicWidth() + mPad;
  }
}
