package android.animation;

import android.graphics.Rect;

public class RectEvaluator
  implements TypeEvaluator<Rect>
{
  private Rect mRect;
  
  public RectEvaluator() {}
  
  public RectEvaluator(Rect paramRect)
  {
    mRect = paramRect;
  }
  
  public Rect evaluate(float paramFloat, Rect paramRect1, Rect paramRect2)
  {
    int i = left + (int)((left - left) * paramFloat);
    int j = top + (int)((top - top) * paramFloat);
    int k = right + (int)((right - right) * paramFloat);
    int m = bottom + (int)((bottom - bottom) * paramFloat);
    if (mRect == null) {
      return new Rect(i, j, k, m);
    }
    mRect.set(i, j, k, m);
    return mRect;
  }
}
