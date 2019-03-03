package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;

public class ArcShape
  extends RectShape
{
  private final float mStartAngle;
  private final float mSweepAngle;
  
  public ArcShape(float paramFloat1, float paramFloat2)
  {
    mStartAngle = paramFloat1;
    mSweepAngle = paramFloat2;
  }
  
  public ArcShape clone()
    throws CloneNotSupportedException
  {
    return (ArcShape)super.clone();
  }
  
  public void draw(Canvas paramCanvas, Paint paramPaint)
  {
    paramCanvas.drawArc(rect(), mStartAngle, mSweepAngle, true, paramPaint);
  }
  
  public void getOutline(Outline paramOutline) {}
  
  public final float getStartAngle()
  {
    return mStartAngle;
  }
  
  public final float getSweepAngle()
  {
    return mSweepAngle;
  }
}
