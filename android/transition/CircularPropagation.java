package android.transition;

import android.graphics.Rect;
import android.view.ViewGroup;

public class CircularPropagation
  extends VisibilityPropagation
{
  private static final String TAG = "CircularPropagation";
  private float mPropagationSpeed = 3.0F;
  
  public CircularPropagation() {}
  
  private static double distance(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return Math.hypot(paramFloat3 - paramFloat1, paramFloat4 - paramFloat2);
  }
  
  public long getStartDelay(ViewGroup paramViewGroup, Transition paramTransition, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if ((paramTransitionValues1 == null) && (paramTransitionValues2 == null)) {
      return 0L;
    }
    int i = 1;
    if ((paramTransitionValues2 != null) && (getViewVisibility(paramTransitionValues1) != 0)) {
      paramTransitionValues1 = paramTransitionValues2;
    } else {
      i = -1;
    }
    int j = getViewX(paramTransitionValues1);
    int k = getViewY(paramTransitionValues1);
    paramTransitionValues1 = paramTransition.getEpicenter();
    int m;
    int n;
    if (paramTransitionValues1 != null)
    {
      m = paramTransitionValues1.centerX();
      n = paramTransitionValues1.centerY();
    }
    else
    {
      paramTransitionValues1 = new int[2];
      paramViewGroup.getLocationOnScreen(paramTransitionValues1);
      m = Math.round(paramTransitionValues1[0] + paramViewGroup.getWidth() / 2 + paramViewGroup.getTranslationX());
      n = Math.round(paramTransitionValues1[1] + paramViewGroup.getHeight() / 2 + paramViewGroup.getTranslationY());
    }
    double d = distance(j, k, m, n) / distance(0.0F, 0.0F, paramViewGroup.getWidth(), paramViewGroup.getHeight());
    long l1 = paramTransition.getDuration();
    long l2 = l1;
    if (l1 < 0L) {
      l2 = 300L;
    }
    return Math.round((float)(i * l2) / mPropagationSpeed * d);
  }
  
  public void setPropagationSpeed(float paramFloat)
  {
    if (paramFloat != 0.0F)
    {
      mPropagationSpeed = paramFloat;
      return;
    }
    throw new IllegalArgumentException("propagationSpeed may not be 0");
  }
}
