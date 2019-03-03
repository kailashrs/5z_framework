package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RectF;

public class OvalShape
  extends RectShape
{
  public OvalShape() {}
  
  public OvalShape clone()
    throws CloneNotSupportedException
  {
    return (OvalShape)super.clone();
  }
  
  public void draw(Canvas paramCanvas, Paint paramPaint)
  {
    paramCanvas.drawOval(rect(), paramPaint);
  }
  
  public void getOutline(Outline paramOutline)
  {
    RectF localRectF = rect();
    paramOutline.setOval((int)Math.ceil(left), (int)Math.ceil(top), (int)Math.floor(right), (int)Math.floor(bottom));
  }
}
