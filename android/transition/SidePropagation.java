package android.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class SidePropagation
  extends VisibilityPropagation
{
  private static final String TAG = "SlidePropagation";
  private float mPropagationSpeed = 3.0F;
  private int mSide = 80;
  
  public SidePropagation() {}
  
  private int distance(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    int i = mSide;
    int j = 1;
    int k = 1;
    if (i == 8388611)
    {
      if (paramView.getLayoutDirection() != 1) {
        k = 0;
      }
      if (k != 0) {
        k = 5;
      } else {
        k = 3;
      }
    }
    else if (mSide == 8388613)
    {
      if (paramView.getLayoutDirection() == 1) {
        k = j;
      } else {
        k = 0;
      }
      if (k != 0) {
        k = 3;
      } else {
        k = 5;
      }
    }
    else
    {
      k = mSide;
    }
    j = 0;
    if (k != 3)
    {
      if (k != 5)
      {
        if (k != 48)
        {
          if (k != 80) {
            paramInt1 = j;
          } else {
            paramInt1 = paramInt2 - paramInt6 + Math.abs(paramInt3 - paramInt1);
          }
        }
        else {
          paramInt1 = paramInt8 - paramInt2 + Math.abs(paramInt3 - paramInt1);
        }
      }
      else {
        paramInt1 = paramInt1 - paramInt5 + Math.abs(paramInt4 - paramInt2);
      }
    }
    else {
      paramInt1 = paramInt7 - paramInt1 + Math.abs(paramInt4 - paramInt2);
    }
    return paramInt1;
  }
  
  private int getMaxDistance(ViewGroup paramViewGroup)
  {
    int i = mSide;
    if ((i != 3) && (i != 5) && (i != 8388611) && (i != 8388613)) {
      return paramViewGroup.getHeight();
    }
    return paramViewGroup.getWidth();
  }
  
  public long getStartDelay(ViewGroup paramViewGroup, Transition paramTransition, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if ((paramTransitionValues1 == null) && (paramTransitionValues2 == null)) {
      return 0L;
    }
    int i = 1;
    Rect localRect = paramTransition.getEpicenter();
    if ((paramTransitionValues2 != null) && (getViewVisibility(paramTransitionValues1) != 0)) {
      paramTransitionValues1 = paramTransitionValues2;
    } else {
      i = -1;
    }
    int j = getViewX(paramTransitionValues1);
    int k = getViewY(paramTransitionValues1);
    paramTransitionValues1 = new int[2];
    paramViewGroup.getLocationOnScreen(paramTransitionValues1);
    int m = paramTransitionValues1[0] + Math.round(paramViewGroup.getTranslationX());
    int n = paramTransitionValues1[1] + Math.round(paramViewGroup.getTranslationY());
    int i1 = m + paramViewGroup.getWidth();
    int i2 = n + paramViewGroup.getHeight();
    int i3;
    int i4;
    int i5;
    if (localRect != null)
    {
      i3 = localRect.centerX();
      i4 = localRect.centerY();
      i5 = i3;
    }
    else
    {
      i5 = (m + i1) / 2;
      i3 = (n + i2) / 2;
      i4 = i3;
    }
    float f = distance(paramViewGroup, j, k, i5, i4, m, n, i1, i2) / getMaxDistance(paramViewGroup);
    long l1 = paramTransition.getDuration();
    long l2 = l1;
    if (l1 < 0L) {
      l2 = 300L;
    }
    return Math.round((float)(i * l2) / mPropagationSpeed * f);
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
  
  public void setSide(int paramInt)
  {
    mSide = paramInt;
  }
}
