package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.android.internal.R.styleable;

public class ViewAnimator
  extends FrameLayout
{
  boolean mAnimateFirstTime = true;
  boolean mFirstTime = true;
  Animation mInAnimation;
  Animation mOutAnimation;
  int mWhichChild = 0;
  
  public ViewAnimator(Context paramContext)
  {
    super(paramContext);
    initViewAnimator(paramContext, null);
  }
  
  public ViewAnimator(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ViewAnimator);
    int i = localTypedArray.getResourceId(0, 0);
    if (i > 0) {
      setInAnimation(paramContext, i);
    }
    i = localTypedArray.getResourceId(1, 0);
    if (i > 0) {
      setOutAnimation(paramContext, i);
    }
    setAnimateFirstView(localTypedArray.getBoolean(2, true));
    localTypedArray.recycle();
    initViewAnimator(paramContext, paramAttributeSet);
  }
  
  private void initViewAnimator(Context paramContext, AttributeSet paramAttributeSet)
  {
    if (paramAttributeSet == null)
    {
      mMeasureAllChildren = true;
      return;
    }
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.FrameLayout);
    setMeasureAllChildren(paramContext.getBoolean(0, true));
    paramContext.recycle();
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (getChildCount() == 1) {
      paramView.setVisibility(0);
    } else {
      paramView.setVisibility(8);
    }
    if ((paramInt >= 0) && (mWhichChild >= paramInt)) {
      setDisplayedChild(mWhichChild + 1);
    }
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return ViewAnimator.class.getName();
  }
  
  public boolean getAnimateFirstView()
  {
    return mAnimateFirstTime;
  }
  
  public int getBaseline()
  {
    int i;
    if (getCurrentView() != null) {
      i = getCurrentView().getBaseline();
    } else {
      i = super.getBaseline();
    }
    return i;
  }
  
  public View getCurrentView()
  {
    return getChildAt(mWhichChild);
  }
  
  public int getDisplayedChild()
  {
    return mWhichChild;
  }
  
  public Animation getInAnimation()
  {
    return mInAnimation;
  }
  
  public Animation getOutAnimation()
  {
    return mOutAnimation;
  }
  
  public void removeAllViews()
  {
    super.removeAllViews();
    mWhichChild = 0;
    mFirstTime = true;
  }
  
  public void removeView(View paramView)
  {
    int i = indexOfChild(paramView);
    if (i >= 0) {
      removeViewAt(i);
    }
  }
  
  public void removeViewAt(int paramInt)
  {
    super.removeViewAt(paramInt);
    int i = getChildCount();
    if (i == 0)
    {
      mWhichChild = 0;
      mFirstTime = true;
    }
    else if (mWhichChild >= i)
    {
      setDisplayedChild(i - 1);
    }
    else if (mWhichChild == paramInt)
    {
      setDisplayedChild(mWhichChild);
    }
  }
  
  public void removeViewInLayout(View paramView)
  {
    removeView(paramView);
  }
  
  public void removeViews(int paramInt1, int paramInt2)
  {
    super.removeViews(paramInt1, paramInt2);
    if (getChildCount() == 0)
    {
      mWhichChild = 0;
      mFirstTime = true;
    }
    else if ((mWhichChild >= paramInt1) && (mWhichChild < paramInt1 + paramInt2))
    {
      setDisplayedChild(mWhichChild);
    }
  }
  
  public void removeViewsInLayout(int paramInt1, int paramInt2)
  {
    removeViews(paramInt1, paramInt2);
  }
  
  public void setAnimateFirstView(boolean paramBoolean)
  {
    mAnimateFirstTime = paramBoolean;
  }
  
  @RemotableViewMethod
  public void setDisplayedChild(int paramInt)
  {
    mWhichChild = paramInt;
    int i = getChildCount();
    int j = 1;
    if (paramInt >= i) {
      mWhichChild = 0;
    } else if (paramInt < 0) {
      mWhichChild = (getChildCount() - 1);
    }
    if (getFocusedChild() != null) {
      paramInt = j;
    } else {
      paramInt = 0;
    }
    showOnly(mWhichChild);
    if (paramInt != 0) {
      requestFocus(2);
    }
  }
  
  public void setInAnimation(Context paramContext, int paramInt)
  {
    setInAnimation(AnimationUtils.loadAnimation(paramContext, paramInt));
  }
  
  public void setInAnimation(Animation paramAnimation)
  {
    mInAnimation = paramAnimation;
  }
  
  public void setOutAnimation(Context paramContext, int paramInt)
  {
    setOutAnimation(AnimationUtils.loadAnimation(paramContext, paramInt));
  }
  
  public void setOutAnimation(Animation paramAnimation)
  {
    mOutAnimation = paramAnimation;
  }
  
  @RemotableViewMethod
  public void showNext()
  {
    setDisplayedChild(mWhichChild + 1);
  }
  
  void showOnly(int paramInt)
  {
    boolean bool;
    if ((mFirstTime) && (!mAnimateFirstTime)) {
      bool = false;
    } else {
      bool = true;
    }
    showOnly(paramInt, bool);
  }
  
  void showOnly(int paramInt, boolean paramBoolean)
  {
    int i = getChildCount();
    for (int j = 0; j < i; j++)
    {
      View localView = getChildAt(j);
      if (j == paramInt)
      {
        if ((paramBoolean) && (mInAnimation != null)) {
          localView.startAnimation(mInAnimation);
        }
        localView.setVisibility(0);
        mFirstTime = false;
      }
      else
      {
        if ((paramBoolean) && (mOutAnimation != null) && (localView.getVisibility() == 0)) {
          localView.startAnimation(mOutAnimation);
        } else if (localView.getAnimation() == mInAnimation) {
          localView.clearAnimation();
        }
        localView.setVisibility(8);
      }
    }
  }
  
  @RemotableViewMethod
  public void showPrevious()
  {
    setDisplayedChild(mWhichChild - 1);
  }
}
