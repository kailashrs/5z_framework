package android.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.graphics.Path;
import android.view.View;

class TranslationAnimationCreator
{
  TranslationAnimationCreator() {}
  
  static Animator createAnimation(View paramView, TransitionValues paramTransitionValues, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, TimeInterpolator paramTimeInterpolator, Transition paramTransition)
  {
    float f1 = paramView.getTranslationX();
    float f2 = paramView.getTranslationY();
    Object localObject = (int[])view.getTag(16909498);
    if (localObject != null)
    {
      paramFloat1 = localObject[0] - paramInt1;
      paramFloat2 = localObject[1] - paramInt2;
      paramFloat1 += f1;
      paramFloat2 += f2;
    }
    int i = Math.round(paramFloat1 - f1);
    int j = Math.round(paramFloat2 - f2);
    paramView.setTranslationX(paramFloat1);
    paramView.setTranslationY(paramFloat2);
    if ((paramFloat1 == paramFloat3) && (paramFloat2 == paramFloat4)) {
      return null;
    }
    localObject = new Path();
    ((Path)localObject).moveTo(paramFloat1, paramFloat2);
    ((Path)localObject).lineTo(paramFloat3, paramFloat4);
    localObject = ObjectAnimator.ofFloat(paramView, View.TRANSLATION_X, View.TRANSLATION_Y, (Path)localObject);
    paramView = new TransitionPositionListener(paramView, view, paramInt1 + i, paramInt2 + j, f1, f2, null);
    paramTransition.addListener(paramView);
    ((ObjectAnimator)localObject).addListener(paramView);
    ((ObjectAnimator)localObject).addPauseListener(paramView);
    ((ObjectAnimator)localObject).setInterpolator(paramTimeInterpolator);
    return localObject;
  }
  
  private static class TransitionPositionListener
    extends AnimatorListenerAdapter
    implements Transition.TransitionListener
  {
    private final View mMovingView;
    private float mPausedX;
    private float mPausedY;
    private final int mStartX;
    private final int mStartY;
    private final float mTerminalX;
    private final float mTerminalY;
    private int[] mTransitionPosition;
    private final View mViewInHierarchy;
    
    private TransitionPositionListener(View paramView1, View paramView2, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
    {
      mMovingView = paramView1;
      mViewInHierarchy = paramView2;
      mStartX = (paramInt1 - Math.round(mMovingView.getTranslationX()));
      mStartY = (paramInt2 - Math.round(mMovingView.getTranslationY()));
      mTerminalX = paramFloat1;
      mTerminalY = paramFloat2;
      mTransitionPosition = ((int[])mViewInHierarchy.getTag(16909498));
      if (mTransitionPosition != null) {
        mViewInHierarchy.setTagInternal(16909498, null);
      }
    }
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      if (mTransitionPosition == null) {
        mTransitionPosition = new int[2];
      }
      mTransitionPosition[0] = Math.round(mStartX + mMovingView.getTranslationX());
      mTransitionPosition[1] = Math.round(mStartY + mMovingView.getTranslationY());
      mViewInHierarchy.setTagInternal(16909498, mTransitionPosition);
    }
    
    public void onAnimationEnd(Animator paramAnimator) {}
    
    public void onAnimationPause(Animator paramAnimator)
    {
      mPausedX = mMovingView.getTranslationX();
      mPausedY = mMovingView.getTranslationY();
      mMovingView.setTranslationX(mTerminalX);
      mMovingView.setTranslationY(mTerminalY);
    }
    
    public void onAnimationResume(Animator paramAnimator)
    {
      mMovingView.setTranslationX(mPausedX);
      mMovingView.setTranslationY(mPausedY);
    }
    
    public void onTransitionCancel(Transition paramTransition) {}
    
    public void onTransitionEnd(Transition paramTransition)
    {
      mMovingView.setTranslationX(mTerminalX);
      mMovingView.setTranslationY(mTerminalY);
      paramTransition.removeListener(this);
    }
    
    public void onTransitionPause(Transition paramTransition) {}
    
    public void onTransitionResume(Transition paramTransition) {}
    
    public void onTransitionStart(Transition paramTransition) {}
  }
}
