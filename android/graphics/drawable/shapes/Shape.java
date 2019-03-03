package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;

public abstract class Shape
  implements Cloneable
{
  private float mHeight;
  private float mWidth;
  
  public Shape() {}
  
  public Shape clone()
    throws CloneNotSupportedException
  {
    return (Shape)super.clone();
  }
  
  public abstract void draw(Canvas paramCanvas, Paint paramPaint);
  
  public final float getHeight()
  {
    return mHeight;
  }
  
  public void getOutline(Outline paramOutline) {}
  
  public final float getWidth()
  {
    return mWidth;
  }
  
  public boolean hasAlpha()
  {
    return true;
  }
  
  protected void onResize(float paramFloat1, float paramFloat2) {}
  
  public final void resize(float paramFloat1, float paramFloat2)
  {
    float f = paramFloat1;
    if (paramFloat1 < 0.0F) {
      f = 0.0F;
    }
    paramFloat1 = paramFloat2;
    if (paramFloat2 < 0.0F) {
      paramFloat1 = 0.0F;
    }
    if ((mWidth != f) || (mHeight != paramFloat1))
    {
      mWidth = f;
      mHeight = paramFloat1;
      onResize(f, paramFloat1);
    }
  }
}
