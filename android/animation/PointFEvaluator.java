package android.animation;

import android.graphics.PointF;

public class PointFEvaluator
  implements TypeEvaluator<PointF>
{
  private PointF mPoint;
  
  public PointFEvaluator() {}
  
  public PointFEvaluator(PointF paramPointF)
  {
    mPoint = paramPointF;
  }
  
  public PointF evaluate(float paramFloat, PointF paramPointF1, PointF paramPointF2)
  {
    float f = x + (x - x) * paramFloat;
    paramFloat = y + (y - y) * paramFloat;
    if (mPoint != null)
    {
      mPoint.set(f, paramFloat);
      return mPoint;
    }
    return new PointF(f, paramFloat);
  }
}
