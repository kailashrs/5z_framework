package android.text.style;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.Layout;
import android.text.Spanned;

public class IconMarginSpan
  implements LeadingMarginSpan, LineHeightSpan
{
  private final Bitmap mBitmap;
  private final int mPad;
  
  public IconMarginSpan(Bitmap paramBitmap)
  {
    this(paramBitmap, 0);
  }
  
  public IconMarginSpan(Bitmap paramBitmap, int paramInt)
  {
    mBitmap = paramBitmap;
    mPad = paramInt;
  }
  
  public void chooseHeight(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Paint.FontMetricsInt paramFontMetricsInt)
  {
    if (paramInt2 == ((Spanned)paramCharSequence).getSpanEnd(this))
    {
      paramInt1 = mBitmap.getHeight();
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
    paramInt3 = paramLayout.getLineTop(paramLayout.getLineForOffset(((Spanned)paramCharSequence).getSpanStart(this)));
    if (paramInt2 < 0) {
      paramInt1 -= mBitmap.getWidth();
    }
    paramCanvas.drawBitmap(mBitmap, paramInt1, paramInt3, paramPaint);
  }
  
  public int getLeadingMargin(boolean paramBoolean)
  {
    return mBitmap.getWidth() + mPad;
  }
}
