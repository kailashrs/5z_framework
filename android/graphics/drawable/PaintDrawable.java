package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import com.android.internal.R.styleable;
import org.xmlpull.v1.XmlPullParser;

public class PaintDrawable
  extends ShapeDrawable
{
  public PaintDrawable() {}
  
  public PaintDrawable(int paramInt)
  {
    getPaint().setColor(paramInt);
  }
  
  protected boolean inflateTag(String paramString, Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
  {
    if (paramString.equals("corners"))
    {
      paramString = paramResources.obtainAttributes(paramAttributeSet, R.styleable.DrawableCorners);
      int i = paramString.getDimensionPixelSize(0, 0);
      setCornerRadius(i);
      int j = paramString.getDimensionPixelSize(1, i);
      int k = paramString.getDimensionPixelSize(2, i);
      int m = paramString.getDimensionPixelSize(3, i);
      int n = paramString.getDimensionPixelSize(4, i);
      if ((j != i) || (k != i) || (m != i) || (n != i)) {
        setCornerRadii(new float[] { j, j, k, k, m, m, n, n });
      }
      paramString.recycle();
      return true;
    }
    return super.inflateTag(paramString, paramResources, paramXmlPullParser, paramAttributeSet);
  }
  
  public void setCornerRadii(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat == null)
    {
      if (getShape() != null) {
        setShape(null);
      }
    }
    else {
      setShape(new RoundRectShape(paramArrayOfFloat, null, null));
    }
    invalidateSelf();
  }
  
  public void setCornerRadius(float paramFloat)
  {
    Object localObject = null;
    if (paramFloat > 0.0F)
    {
      float[] arrayOfFloat = new float[8];
      for (int i = 0;; i++)
      {
        localObject = arrayOfFloat;
        if (i >= 8) {
          break;
        }
        arrayOfFloat[i] = paramFloat;
      }
    }
    setCornerRadii(localObject);
  }
}
