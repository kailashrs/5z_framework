package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;

public class RectShape
  extends Shape
{
  private RectF mRect = new RectF();
  
  public RectShape() {}
  
  public RectShape clone()
    throws CloneNotSupportedException
  {
    RectShape localRectShape = (RectShape)super.clone();
    mRect = new RectF(mRect);
    return localRectShape;
  }
  
  public void draw(Canvas paramCanvas, Paint paramPaint)
  {
    paramCanvas.drawRect(mRect, paramPaint);
  }
  
  public void getOutline(Outline paramOutline)
  {
    RectF localRectF = rect();
    paramOutline.setRect((int)Math.ceil(left), (int)Math.ceil(top), (int)Math.floor(right), (int)Math.floor(bottom));
  }
  
  protected void onResize(float paramFloat1, float paramFloat2)
  {
    mRect.set(0.0F, 0.0F, paramFloat1, paramFloat2);
  }
  
  protected final RectF rect()
  {
    return mRect;
  }
}
