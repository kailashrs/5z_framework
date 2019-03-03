package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ActionMenuPresenter;
import android.widget.ActionMenuView;
import com.android.internal.R.styleable;

public abstract class AbsActionBarView
  extends ViewGroup
{
  private static final int FADE_DURATION = 200;
  private static final TimeInterpolator sAlphaInterpolator = new DecelerateInterpolator();
  protected ActionMenuPresenter mActionMenuPresenter;
  protected int mContentHeight;
  private boolean mEatingHover;
  private boolean mEatingTouch;
  protected ActionMenuView mMenuView;
  protected final Context mPopupContext;
  protected boolean mSplitActionBar;
  protected ViewGroup mSplitView;
  protected boolean mSplitWhenNarrow;
  protected final VisibilityAnimListener mVisAnimListener = new VisibilityAnimListener();
  protected Animator mVisibilityAnim;
  
  public AbsActionBarView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
    paramAttributeSet = new TypedValue();
    if ((paramContext.getTheme().resolveAttribute(16843917, paramAttributeSet, true)) && (resourceId != 0)) {
      mPopupContext = new ContextThemeWrapper(paramContext, resourceId);
    } else {
      mPopupContext = paramContext;
    }
  }
  
  protected static int next(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramBoolean) {
      paramInt1 -= paramInt2;
    } else {
      paramInt1 += paramInt2;
    }
    return paramInt1;
  }
  
  public void animateToVisibility(int paramInt)
  {
    setupAnimatorToVisibility(paramInt, 200L).start();
  }
  
  public boolean canShowOverflowMenu()
  {
    boolean bool;
    if ((isOverflowReserved()) && (getVisibility() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void dismissPopupMenus()
  {
    if (mActionMenuPresenter != null) {
      mActionMenuPresenter.dismissPopupMenus();
    }
  }
  
  public int getAnimatedVisibility()
  {
    if (mVisibilityAnim != null) {
      return mVisAnimListener.mFinalVisibility;
    }
    return getVisibility();
  }
  
  public int getContentHeight()
  {
    return mContentHeight;
  }
  
  public boolean hideOverflowMenu()
  {
    if (mActionMenuPresenter != null) {
      return mActionMenuPresenter.hideOverflowMenu();
    }
    return false;
  }
  
  public boolean isOverflowMenuShowPending()
  {
    if (mActionMenuPresenter != null) {
      return mActionMenuPresenter.isOverflowMenuShowPending();
    }
    return false;
  }
  
  public boolean isOverflowMenuShowing()
  {
    if (mActionMenuPresenter != null) {
      return mActionMenuPresenter.isOverflowMenuShowing();
    }
    return false;
  }
  
  public boolean isOverflowReserved()
  {
    boolean bool;
    if ((mActionMenuPresenter != null) && (mActionMenuPresenter.isOverflowReserved())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected int measureChildView(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt1, Integer.MIN_VALUE), paramInt2);
    return Math.max(0, paramInt1 - paramView.getMeasuredWidth() - paramInt3);
  }
  
  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    TypedArray localTypedArray = getContext().obtainStyledAttributes(null, R.styleable.ActionBar, 16843470, 0);
    setContentHeight(localTypedArray.getLayoutDimension(4, 0));
    localTypedArray.recycle();
    if (mSplitWhenNarrow) {
      setSplitToolbar(getContext().getResources().getBoolean(17957118));
    }
    if (mActionMenuPresenter != null) {
      mActionMenuPresenter.onConfigurationChanged(paramConfiguration);
    }
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if (i == 9) {
      mEatingHover = false;
    }
    if (!mEatingHover)
    {
      boolean bool = super.onHoverEvent(paramMotionEvent);
      if ((i == 9) && (!bool)) {
        mEatingHover = true;
      }
    }
    if ((i == 10) || (i == 3)) {
      mEatingHover = false;
    }
    return true;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if (i == 0) {
      mEatingTouch = false;
    }
    if (!mEatingTouch)
    {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if ((i == 0) && (!bool)) {
        mEatingTouch = true;
      }
    }
    if ((i == 1) || (i == 3)) {
      mEatingTouch = false;
    }
    return true;
  }
  
  protected int positionChild(View paramView, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    paramInt2 = (paramInt3 - j) / 2 + paramInt2;
    if (paramBoolean) {
      paramView.layout(paramInt1 - i, paramInt2, paramInt1, paramInt2 + j);
    } else {
      paramView.layout(paramInt1, paramInt2, paramInt1 + i, paramInt2 + j);
    }
    if (paramBoolean) {
      paramInt1 = -i;
    } else {
      paramInt1 = i;
    }
    return paramInt1;
  }
  
  public void postShowOverflowMenu()
  {
    post(new Runnable()
    {
      public void run()
      {
        showOverflowMenu();
      }
    });
  }
  
  public void setContentHeight(int paramInt)
  {
    mContentHeight = paramInt;
    requestLayout();
  }
  
  public void setSplitToolbar(boolean paramBoolean)
  {
    mSplitActionBar = paramBoolean;
  }
  
  public void setSplitView(ViewGroup paramViewGroup)
  {
    mSplitView = paramViewGroup;
  }
  
  public void setSplitWhenNarrow(boolean paramBoolean)
  {
    mSplitWhenNarrow = paramBoolean;
  }
  
  public void setVisibility(int paramInt)
  {
    if (paramInt != getVisibility())
    {
      if (mVisibilityAnim != null) {
        mVisibilityAnim.end();
      }
      super.setVisibility(paramInt);
    }
  }
  
  public Animator setupAnimatorToVisibility(int paramInt, long paramLong)
  {
    if (mVisibilityAnim != null) {
      mVisibilityAnim.cancel();
    }
    Object localObject1;
    Object localObject2;
    if (paramInt == 0)
    {
      if (getVisibility() != 0)
      {
        setAlpha(0.0F);
        if ((mSplitView != null) && (mMenuView != null)) {
          mMenuView.setAlpha(0.0F);
        }
      }
      localObjectAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, new float[] { 1.0F });
      localObjectAnimator.setDuration(paramLong);
      localObjectAnimator.setInterpolator(sAlphaInterpolator);
      if ((mSplitView != null) && (mMenuView != null))
      {
        localObject1 = new AnimatorSet();
        localObject2 = ObjectAnimator.ofFloat(mMenuView, View.ALPHA, new float[] { 1.0F });
        ((ObjectAnimator)localObject2).setDuration(paramLong);
        ((AnimatorSet)localObject1).addListener(mVisAnimListener.withFinalVisibility(paramInt));
        ((AnimatorSet)localObject1).play(localObjectAnimator).with((Animator)localObject2);
        return localObject1;
      }
      localObjectAnimator.addListener(mVisAnimListener.withFinalVisibility(paramInt));
      return localObjectAnimator;
    }
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(this, View.ALPHA, new float[] { 0.0F });
    localObjectAnimator.setDuration(paramLong);
    localObjectAnimator.setInterpolator(sAlphaInterpolator);
    if ((mSplitView != null) && (mMenuView != null))
    {
      localObject2 = new AnimatorSet();
      localObject1 = ObjectAnimator.ofFloat(mMenuView, View.ALPHA, new float[] { 0.0F });
      ((ObjectAnimator)localObject1).setDuration(paramLong);
      ((AnimatorSet)localObject2).addListener(mVisAnimListener.withFinalVisibility(paramInt));
      ((AnimatorSet)localObject2).play(localObjectAnimator).with((Animator)localObject1);
      return localObject2;
    }
    localObjectAnimator.addListener(mVisAnimListener.withFinalVisibility(paramInt));
    return localObjectAnimator;
  }
  
  public boolean showOverflowMenu()
  {
    if (mActionMenuPresenter != null) {
      return mActionMenuPresenter.showOverflowMenu();
    }
    return false;
  }
  
  protected class VisibilityAnimListener
    implements Animator.AnimatorListener
  {
    private boolean mCanceled = false;
    int mFinalVisibility;
    
    protected VisibilityAnimListener() {}
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      mCanceled = true;
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      if (mCanceled) {
        return;
      }
      mVisibilityAnim = null;
      setVisibility(mFinalVisibility);
      if ((mSplitView != null) && (mMenuView != null)) {
        mMenuView.setVisibility(mFinalVisibility);
      }
    }
    
    public void onAnimationRepeat(Animator paramAnimator) {}
    
    public void onAnimationStart(Animator paramAnimator)
    {
      setVisibility(0);
      mVisibilityAnim = paramAnimator;
      mCanceled = false;
    }
    
    public VisibilityAnimListener withFinalVisibility(int paramInt)
    {
      mFinalVisibility = paramInt;
      return this;
    }
  }
}
