package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Path;
import android.text.style.ParagraphStyle;

public class BoringLayout
  extends Layout
  implements TextUtils.EllipsizeCallback
{
  int mBottom;
  private int mBottomPadding;
  int mDesc;
  private String mDirect;
  private int mEllipsizedCount;
  private int mEllipsizedStart;
  private int mEllipsizedWidth;
  private float mMax;
  private Paint mPaint;
  private int mTopPadding;
  
  public BoringLayout(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, Metrics paramMetrics, boolean paramBoolean)
  {
    super(paramCharSequence, paramTextPaint, paramInt, paramAlignment, paramFloat1, paramFloat2);
    mEllipsizedWidth = paramInt;
    mEllipsizedStart = 0;
    mEllipsizedCount = 0;
    init(paramCharSequence, paramTextPaint, paramAlignment, paramMetrics, paramBoolean, true);
  }
  
  public BoringLayout(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt1, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, Metrics paramMetrics, boolean paramBoolean, TextUtils.TruncateAt paramTruncateAt, int paramInt2)
  {
    super(paramCharSequence, paramTextPaint, paramInt1, paramAlignment, paramFloat1, paramFloat2);
    boolean bool;
    if ((paramTruncateAt != null) && (paramTruncateAt != TextUtils.TruncateAt.MARQUEE))
    {
      replaceWith(TextUtils.ellipsize(paramCharSequence, paramTextPaint, paramInt2, paramTruncateAt, true, this), paramTextPaint, paramInt1, paramAlignment, paramFloat1, paramFloat2);
      mEllipsizedWidth = paramInt2;
      bool = false;
    }
    else
    {
      mEllipsizedWidth = paramInt1;
      mEllipsizedStart = 0;
      mEllipsizedCount = 0;
      bool = true;
    }
    init(getText(), paramTextPaint, paramAlignment, paramMetrics, paramBoolean, bool);
  }
  
  private static boolean hasAnyInterestingChars(CharSequence paramCharSequence, int paramInt)
  {
    char[] arrayOfChar = TextUtils.obtain(500);
    int i = 0;
    while (i < paramInt) {
      try
      {
        int j = Math.min(i + 500, paramInt);
        TextUtils.getChars(paramCharSequence, i, j, arrayOfChar, 0);
        int k = 0;
        while (k < j - i)
        {
          char c = arrayOfChar[k];
          if ((c != '\n') && (c != '\t'))
          {
            boolean bool = TextUtils.couldAffectRtl(c);
            if (!bool)
            {
              k++;
              continue;
            }
          }
          return true;
        }
        i += 500;
      }
      finally
      {
        TextUtils.recycle(arrayOfChar);
      }
    }
    TextUtils.recycle(arrayOfChar);
    return false;
  }
  
  public static Metrics isBoring(CharSequence paramCharSequence, TextPaint paramTextPaint)
  {
    return isBoring(paramCharSequence, paramTextPaint, TextDirectionHeuristics.FIRSTSTRONG_LTR, null);
  }
  
  public static Metrics isBoring(CharSequence paramCharSequence, TextPaint paramTextPaint, Metrics paramMetrics)
  {
    return isBoring(paramCharSequence, paramTextPaint, TextDirectionHeuristics.FIRSTSTRONG_LTR, paramMetrics);
  }
  
  public static Metrics isBoring(CharSequence paramCharSequence, TextPaint paramTextPaint, TextDirectionHeuristic paramTextDirectionHeuristic, Metrics paramMetrics)
  {
    int i = paramCharSequence.length();
    if (hasAnyInterestingChars(paramCharSequence, i)) {
      return null;
    }
    if ((paramTextDirectionHeuristic != null) && (paramTextDirectionHeuristic.isRtl(paramCharSequence, 0, i))) {
      return null;
    }
    if (((paramCharSequence instanceof Spanned)) && (((Spanned)paramCharSequence).getSpans(0, i, ParagraphStyle.class).length > 0)) {
      return null;
    }
    paramTextDirectionHeuristic = paramMetrics;
    if (paramTextDirectionHeuristic == null) {
      paramTextDirectionHeuristic = new Metrics();
    }
    for (;;)
    {
      break;
      paramTextDirectionHeuristic.reset();
    }
    paramTextPaint.set(paramTextPaint);
    paramMetrics = TextLine.obtain();
    paramMetrics.set(paramTextPaint, paramCharSequence, 0, i, 1, Layout.DIRS_ALL_LEFT_TO_RIGHT, false, null);
    width = ((int)Math.ceil(paramMetrics.metrics(paramTextDirectionHeuristic)));
    TextLine.recycle(paramMetrics);
    return paramTextDirectionHeuristic;
  }
  
  public static BoringLayout make(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, Metrics paramMetrics, boolean paramBoolean)
  {
    return new BoringLayout(paramCharSequence, paramTextPaint, paramInt, paramAlignment, paramFloat1, paramFloat2, paramMetrics, paramBoolean);
  }
  
  public static BoringLayout make(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt1, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, Metrics paramMetrics, boolean paramBoolean, TextUtils.TruncateAt paramTruncateAt, int paramInt2)
  {
    return new BoringLayout(paramCharSequence, paramTextPaint, paramInt1, paramAlignment, paramFloat1, paramFloat2, paramMetrics, paramBoolean, paramTruncateAt, paramInt2);
  }
  
  public void draw(Canvas paramCanvas, Path paramPath, Paint paramPaint, int paramInt)
  {
    if ((mDirect != null) && (paramPath == null)) {
      paramCanvas.drawText(mDirect, 0.0F, mBottom - mDesc, mPaint);
    } else {
      super.draw(paramCanvas, paramPath, paramPaint, paramInt);
    }
  }
  
  public void ellipsized(int paramInt1, int paramInt2)
  {
    mEllipsizedStart = paramInt1;
    mEllipsizedCount = (paramInt2 - paramInt1);
  }
  
  public int getBottomPadding()
  {
    return mBottomPadding;
  }
  
  public int getEllipsisCount(int paramInt)
  {
    return mEllipsizedCount;
  }
  
  public int getEllipsisStart(int paramInt)
  {
    return mEllipsizedStart;
  }
  
  public int getEllipsizedWidth()
  {
    return mEllipsizedWidth;
  }
  
  public int getHeight()
  {
    return mBottom;
  }
  
  public boolean getLineContainsTab(int paramInt)
  {
    return false;
  }
  
  public int getLineCount()
  {
    return 1;
  }
  
  public int getLineDescent(int paramInt)
  {
    return mDesc;
  }
  
  public final Layout.Directions getLineDirections(int paramInt)
  {
    return Layout.DIRS_ALL_LEFT_TO_RIGHT;
  }
  
  public float getLineMax(int paramInt)
  {
    return mMax;
  }
  
  public int getLineStart(int paramInt)
  {
    if (paramInt == 0) {
      return 0;
    }
    return getText().length();
  }
  
  public int getLineTop(int paramInt)
  {
    if (paramInt == 0) {
      return 0;
    }
    return mBottom;
  }
  
  public float getLineWidth(int paramInt)
  {
    float f;
    if (paramInt == 0) {
      f = mMax;
    } else {
      f = 0.0F;
    }
    return f;
  }
  
  public int getParagraphDirection(int paramInt)
  {
    return 1;
  }
  
  public int getTopPadding()
  {
    return mTopPadding;
  }
  
  void init(CharSequence paramCharSequence, TextPaint paramTextPaint, Layout.Alignment paramAlignment, Metrics paramMetrics, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (((paramCharSequence instanceof String)) && (paramAlignment == Layout.Alignment.ALIGN_NORMAL)) {
      mDirect = paramCharSequence.toString();
    } else {
      mDirect = null;
    }
    mPaint = paramTextPaint;
    int i;
    if (paramBoolean1) {
      i = bottom - top;
    }
    for (mDesc = bottom;; mDesc = descent)
    {
      break;
      i = descent - ascent;
    }
    mBottom = i;
    if (paramBoolean2)
    {
      mMax = width;
    }
    else
    {
      paramAlignment = TextLine.obtain();
      paramAlignment.set(paramTextPaint, paramCharSequence, 0, paramCharSequence.length(), 1, Layout.DIRS_ALL_LEFT_TO_RIGHT, false, null);
      mMax = ((int)Math.ceil(paramAlignment.metrics(null)));
      TextLine.recycle(paramAlignment);
    }
    if (paramBoolean1)
    {
      mTopPadding = (top - ascent);
      mBottomPadding = (bottom - descent);
    }
  }
  
  public BoringLayout replaceOrMake(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, Metrics paramMetrics, boolean paramBoolean)
  {
    replaceWith(paramCharSequence, paramTextPaint, paramInt, paramAlignment, paramFloat1, paramFloat2);
    mEllipsizedWidth = paramInt;
    mEllipsizedStart = 0;
    mEllipsizedCount = 0;
    init(paramCharSequence, paramTextPaint, paramAlignment, paramMetrics, paramBoolean, true);
    return this;
  }
  
  public BoringLayout replaceOrMake(CharSequence paramCharSequence, TextPaint paramTextPaint, int paramInt1, Layout.Alignment paramAlignment, float paramFloat1, float paramFloat2, Metrics paramMetrics, boolean paramBoolean, TextUtils.TruncateAt paramTruncateAt, int paramInt2)
  {
    boolean bool;
    if ((paramTruncateAt != null) && (paramTruncateAt != TextUtils.TruncateAt.MARQUEE))
    {
      replaceWith(TextUtils.ellipsize(paramCharSequence, paramTextPaint, paramInt2, paramTruncateAt, true, this), paramTextPaint, paramInt1, paramAlignment, paramFloat1, paramFloat2);
      mEllipsizedWidth = paramInt2;
      bool = false;
    }
    else
    {
      replaceWith(paramCharSequence, paramTextPaint, paramInt1, paramAlignment, paramFloat1, paramFloat2);
      mEllipsizedWidth = paramInt1;
      mEllipsizedStart = 0;
      mEllipsizedCount = 0;
      bool = true;
    }
    init(getText(), paramTextPaint, paramAlignment, paramMetrics, paramBoolean, bool);
    return this;
  }
  
  public static class Metrics
    extends Paint.FontMetricsInt
  {
    public int width;
    
    public Metrics() {}
    
    private void reset()
    {
      top = 0;
      bottom = 0;
      ascent = 0;
      descent = 0;
      width = 0;
      leading = 0;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(super.toString());
      localStringBuilder.append(" width=");
      localStringBuilder.append(width);
      return localStringBuilder.toString();
    }
  }
}
