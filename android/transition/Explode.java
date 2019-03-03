package android.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import java.util.Map;

public class Explode
  extends Visibility
{
  private static final String PROPNAME_SCREEN_BOUNDS = "android:explode:screenBounds";
  private static final String TAG = "Explode";
  private static final TimeInterpolator sAccelerate = new AccelerateInterpolator();
  private static final TimeInterpolator sDecelerate = new DecelerateInterpolator();
  private int[] mTempLoc = new int[2];
  
  public Explode()
  {
    setPropagation(new CircularPropagation());
  }
  
  public Explode(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setPropagation(new CircularPropagation());
  }
  
  private static double calculateMaxDistance(View paramView, int paramInt1, int paramInt2)
  {
    paramInt1 = Math.max(paramInt1, paramView.getWidth() - paramInt1);
    paramInt2 = Math.max(paramInt2, paramView.getHeight() - paramInt2);
    return Math.hypot(paramInt1, paramInt2);
  }
  
  private void calculateOut(View paramView, Rect paramRect, int[] paramArrayOfInt)
  {
    paramView.getLocationOnScreen(mTempLoc);
    int i = mTempLoc[0];
    int j = mTempLoc[1];
    Rect localRect = getEpicenter();
    int k;
    int m;
    if (localRect == null)
    {
      k = paramView.getWidth() / 2 + i + Math.round(paramView.getTranslationX());
      m = paramView.getHeight() / 2 + j + Math.round(paramView.getTranslationY());
    }
    else
    {
      k = localRect.centerX();
      m = localRect.centerY();
    }
    int n = paramRect.centerX();
    int i1 = paramRect.centerY();
    double d1 = n - k;
    double d2 = i1 - m;
    double d3 = d1;
    double d4 = d2;
    if (d1 == 0.0D)
    {
      d3 = d1;
      d4 = d2;
      if (d2 == 0.0D)
      {
        d3 = Math.random() * 2.0D - 1.0D;
        d4 = Math.random() * 2.0D - 1.0D;
      }
    }
    d1 = Math.hypot(d3, d4);
    d3 /= d1;
    d1 = d4 / d1;
    d4 = calculateMaxDistance(paramView, k - i, m - j);
    paramArrayOfInt[0] = ((int)Math.round(d4 * d3));
    paramArrayOfInt[1] = ((int)Math.round(d4 * d1));
  }
  
  private void captureValues(TransitionValues paramTransitionValues)
  {
    View localView = view;
    localView.getLocationOnScreen(mTempLoc);
    int i = mTempLoc[0];
    int j = mTempLoc[1];
    int k = localView.getWidth();
    int m = localView.getHeight();
    values.put("android:explode:screenBounds", new Rect(i, j, k + i, m + j));
  }
  
  public void captureEndValues(TransitionValues paramTransitionValues)
  {
    super.captureEndValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public void captureStartValues(TransitionValues paramTransitionValues)
  {
    super.captureStartValues(paramTransitionValues);
    captureValues(paramTransitionValues);
  }
  
  public Animator onAppear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if (paramTransitionValues2 == null) {
      return null;
    }
    paramTransitionValues1 = (Rect)values.get("android:explode:screenBounds");
    float f1 = paramView.getTranslationX();
    float f2 = paramView.getTranslationY();
    calculateOut(paramViewGroup, paramTransitionValues1, mTempLoc);
    float f3 = mTempLoc[0];
    float f4 = mTempLoc[1];
    return TranslationAnimationCreator.createAnimation(paramView, paramTransitionValues2, left, top, f1 + f3, f2 + f4, f1, f2, sDecelerate, this);
  }
  
  public Animator onDisappear(ViewGroup paramViewGroup, View paramView, TransitionValues paramTransitionValues1, TransitionValues paramTransitionValues2)
  {
    if (paramTransitionValues1 == null) {
      return null;
    }
    paramTransitionValues2 = (Rect)values.get("android:explode:screenBounds");
    int i = left;
    int j = top;
    float f1 = paramView.getTranslationX();
    float f2 = paramView.getTranslationY();
    float f3 = f1;
    float f4 = f2;
    int[] arrayOfInt = (int[])view.getTag(16909498);
    float f5 = f3;
    float f6 = f4;
    if (arrayOfInt != null)
    {
      f5 = f3 + (arrayOfInt[0] - left);
      f6 = f4 + (arrayOfInt[1] - top);
      paramTransitionValues2.offsetTo(arrayOfInt[0], arrayOfInt[1]);
    }
    calculateOut(paramViewGroup, paramTransitionValues2, mTempLoc);
    return TranslationAnimationCreator.createAnimation(paramView, paramTransitionValues1, i, j, f1, f2, f5 + mTempLoc[0], f6 + mTempLoc[1], sAccelerate, this);
  }
}
