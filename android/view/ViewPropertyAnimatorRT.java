package android.view;

import android.animation.TimeInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.android.internal.view.animation.FallbackLUTInterpolator;
import java.util.ArrayList;

class ViewPropertyAnimatorRT
{
  private static final Interpolator sLinearInterpolator = new LinearInterpolator();
  private RenderNodeAnimator[] mAnimators = new RenderNodeAnimator[12];
  private final View mView;
  
  ViewPropertyAnimatorRT(View paramView)
  {
    mView = paramView;
  }
  
  private boolean canHandleAnimator(ViewPropertyAnimator paramViewPropertyAnimator)
  {
    if (paramViewPropertyAnimator.getUpdateListener() != null) {
      return false;
    }
    if (paramViewPropertyAnimator.getListener() != null) {
      return false;
    }
    if (!mView.isHardwareAccelerated()) {
      return false;
    }
    return !paramViewPropertyAnimator.hasActions();
  }
  
  private void cancelAnimators(ArrayList<ViewPropertyAnimator.NameValuesHolder> paramArrayList)
  {
    int i = paramArrayList.size();
    for (int j = 0; j < i; j++)
    {
      int k = RenderNodeAnimator.mapViewPropertyToRenderProperty(getmNameConstant);
      if (mAnimators[k] != null)
      {
        mAnimators[k].cancel();
        mAnimators[k] = null;
      }
    }
  }
  
  private void doStartAnimation(ViewPropertyAnimator paramViewPropertyAnimator)
  {
    int i = mPendingAnimations.size();
    long l1 = paramViewPropertyAnimator.getStartDelay();
    long l2 = paramViewPropertyAnimator.getDuration();
    Object localObject1 = paramViewPropertyAnimator.getInterpolator();
    Object localObject2 = localObject1;
    if (localObject1 == null) {
      localObject2 = sLinearInterpolator;
    }
    localObject1 = localObject2;
    if (!RenderNodeAnimator.isNativeInterpolator((TimeInterpolator)localObject2)) {
      localObject1 = new FallbackLUTInterpolator((TimeInterpolator)localObject2, l2);
    }
    for (int j = 0; j < i; j++)
    {
      localObject2 = (ViewPropertyAnimator.NameValuesHolder)mPendingAnimations.get(j);
      int k = RenderNodeAnimator.mapViewPropertyToRenderProperty(mNameConstant);
      localObject2 = new RenderNodeAnimator(k, mFromValue + mDeltaValue);
      ((RenderNodeAnimator)localObject2).setStartDelay(l1);
      ((RenderNodeAnimator)localObject2).setDuration(l2);
      ((RenderNodeAnimator)localObject2).setInterpolator((TimeInterpolator)localObject1);
      ((RenderNodeAnimator)localObject2).setTarget(mView);
      ((RenderNodeAnimator)localObject2).start();
      mAnimators[k] = localObject2;
    }
    mPendingAnimations.clear();
  }
  
  public void cancelAll()
  {
    for (int i = 0; i < mAnimators.length; i++) {
      if (mAnimators[i] != null)
      {
        mAnimators[i].cancel();
        mAnimators[i] = null;
      }
    }
  }
  
  public boolean startAnimation(ViewPropertyAnimator paramViewPropertyAnimator)
  {
    cancelAnimators(mPendingAnimations);
    if (!canHandleAnimator(paramViewPropertyAnimator)) {
      return false;
    }
    doStartAnimation(paramViewPropertyAnimator);
    return true;
  }
}
