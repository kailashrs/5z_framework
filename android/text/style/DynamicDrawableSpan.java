package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;

public abstract class DynamicDrawableSpan
  extends ReplacementSpan
{
  public static final int ALIGN_BASELINE = 1;
  public static final int ALIGN_BOTTOM = 0;
  private WeakReference<Drawable> mDrawableRef;
  protected final int mVerticalAlignment;
  
  public DynamicDrawableSpan()
  {
    mVerticalAlignment = 0;
  }
  
  protected DynamicDrawableSpan(int paramInt)
  {
    mVerticalAlignment = paramInt;
  }
  
  private Drawable getCachedDrawable()
  {
    Object localObject = mDrawableRef;
    Drawable localDrawable = null;
    if (localObject != null) {
      localDrawable = (Drawable)((WeakReference)localObject).get();
    }
    localObject = localDrawable;
    if (localDrawable == null)
    {
      localObject = getDrawable();
      mDrawableRef = new WeakReference(localObject);
    }
    return localObject;
  }
  
  public void draw(Canvas paramCanvas, CharSequence paramCharSequence, int paramInt1, int paramInt2, float paramFloat, int paramInt3, int paramInt4, int paramInt5, Paint paramPaint)
  {
    paramCharSequence = getCachedDrawable();
    paramCanvas.save();
    paramInt2 = paramInt5 - getBoundsbottom;
    paramInt1 = paramInt2;
    if (mVerticalAlignment == 1) {
      paramInt1 = paramInt2 - getFontMetricsIntdescent;
    }
    paramCanvas.translate(paramFloat, paramInt1);
    paramCharSequence.draw(paramCanvas);
    paramCanvas.restore();
  }
  
  public abstract Drawable getDrawable();
  
  public int getSize(Paint paramPaint, CharSequence paramCharSequence, int paramInt1, int paramInt2, Paint.FontMetricsInt paramFontMetricsInt)
  {
    paramPaint = getCachedDrawable().getBounds();
    if (paramFontMetricsInt != null)
    {
      ascent = (-bottom);
      descent = 0;
      top = ascent;
      bottom = 0;
    }
    return right;
  }
  
  public int getVerticalAlignment()
  {
    return mVerticalAlignment;
  }
}
