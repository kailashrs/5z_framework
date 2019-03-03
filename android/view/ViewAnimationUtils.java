package android.view;

import android.animation.Animator;
import android.animation.RevealAnimator;

public final class ViewAnimationUtils
{
  private ViewAnimationUtils() {}
  
  public static Animator createCircularReveal(View paramView, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    return new RevealAnimator(paramView, paramInt1, paramInt2, paramFloat1, paramFloat2);
  }
}
