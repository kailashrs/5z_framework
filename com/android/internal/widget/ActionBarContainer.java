package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.android.internal.R.styleable;

public class ActionBarContainer
  extends FrameLayout
{
  private View mActionBarView;
  private View mActionContextView;
  private Drawable mBackground;
  private int mHeight;
  private boolean mIsSplit;
  private boolean mIsStacked;
  private boolean mIsTransitioning;
  private Drawable mSplitBackground;
  private Drawable mStackedBackground;
  private View mTabContainer;
  
  public ActionBarContainer(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public ActionBarContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBackground(new ActionBarBackgroundDrawable(null));
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ActionBar);
    mBackground = paramContext.getDrawable(2);
    mStackedBackground = paramContext.getDrawable(18);
    mHeight = paramContext.getDimensionPixelSize(4, -1);
    int i = getId();
    boolean bool = true;
    if (i == 16909396)
    {
      mIsSplit = true;
      mSplitBackground = paramContext.getDrawable(19);
    }
    paramContext.recycle();
    if (mIsSplit)
    {
      if (mSplitBackground == null) {}
    }
    else {
      for (;;)
      {
        bool = false;
        break;
        if ((mBackground == null) && (mStackedBackground == null)) {
          break;
        }
      }
    }
    setWillNotDraw(bool);
  }
  
  private int getMeasuredHeightWithMargins(View paramView)
  {
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)paramView.getLayoutParams();
    return paramView.getMeasuredHeight() + topMargin + bottomMargin;
  }
  
  private static boolean isCollapsed(View paramView)
  {
    boolean bool;
    if ((paramView != null) && (paramView.getVisibility() != 8) && (paramView.getMeasuredHeight() != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    boolean bool1 = false;
    Drawable localDrawable = mBackground;
    boolean bool2 = bool1;
    if (localDrawable != null)
    {
      bool2 = bool1;
      if (localDrawable.isStateful()) {
        bool2 = false | localDrawable.setState(arrayOfInt);
      }
    }
    localDrawable = mStackedBackground;
    bool1 = bool2;
    if (localDrawable != null)
    {
      bool1 = bool2;
      if (localDrawable.isStateful()) {
        bool1 = bool2 | localDrawable.setState(arrayOfInt);
      }
    }
    localDrawable = mSplitBackground;
    bool2 = bool1;
    if (localDrawable != null)
    {
      bool2 = bool1;
      if (localDrawable.isStateful()) {
        bool2 = bool1 | localDrawable.setState(arrayOfInt);
      }
    }
    if (bool2) {
      invalidate();
    }
  }
  
  public View getTabContainer()
  {
    return mTabContainer;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    super.jumpDrawablesToCurrentState();
    if (mBackground != null) {
      mBackground.jumpToCurrentState();
    }
    if (mStackedBackground != null) {
      mStackedBackground.jumpToCurrentState();
    }
    if (mSplitBackground != null) {
      mSplitBackground.jumpToCurrentState();
    }
  }
  
  public void onFinishInflate()
  {
    super.onFinishInflate();
    mActionBarView = findViewById(16908697);
    mActionContextView = findViewById(16908702);
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    super.onHoverEvent(paramMotionEvent);
    return true;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool;
    if ((!mIsTransitioning) && (!super.onInterceptTouchEvent(paramMotionEvent))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    View localView = mTabContainer;
    if ((localView != null) && (localView.getVisibility() != 8)) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    }
    if ((localView != null) && (localView.getVisibility() != 8))
    {
      paramInt2 = getMeasuredHeight();
      FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)localView.getLayoutParams();
      localView.layout(paramInt1, paramInt2 - localView.getMeasuredHeight() - bottomMargin, paramInt3, paramInt2 - bottomMargin);
    }
    paramInt1 = 0;
    paramInt2 = 0;
    if (mIsSplit)
    {
      if (mSplitBackground != null)
      {
        mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        paramInt1 = 1;
      }
    }
    else
    {
      if (mBackground != null)
      {
        if (mActionBarView.getVisibility() == 0) {
          mBackground.setBounds(mActionBarView.getLeft(), mActionBarView.getTop(), mActionBarView.getRight(), mActionBarView.getBottom());
        } else if ((mActionContextView != null) && (mActionContextView.getVisibility() == 0)) {
          mBackground.setBounds(mActionContextView.getLeft(), mActionContextView.getTop(), mActionContextView.getRight(), mActionContextView.getBottom());
        } else {
          mBackground.setBounds(0, 0, 0, 0);
        }
        paramInt2 = 1;
      }
      mIsStacked = paramBoolean;
      paramInt1 = paramInt2;
      if (paramBoolean)
      {
        paramInt1 = paramInt2;
        if (mStackedBackground != null)
        {
          mStackedBackground.setBounds(localView.getLeft(), localView.getTop(), localView.getRight(), localView.getBottom());
          paramInt1 = 1;
        }
      }
    }
    if (paramInt1 != 0) {
      invalidate();
    }
  }
  
  public void onMeasure(int paramInt1, int paramInt2)
  {
    int i = paramInt2;
    if (mActionBarView == null)
    {
      i = paramInt2;
      if (View.MeasureSpec.getMode(paramInt2) == Integer.MIN_VALUE)
      {
        i = paramInt2;
        if (mHeight >= 0) {
          i = View.MeasureSpec.makeMeasureSpec(Math.min(mHeight, View.MeasureSpec.getSize(paramInt2)), Integer.MIN_VALUE);
        }
      }
    }
    super.onMeasure(paramInt1, i);
    if (mActionBarView == null) {
      return;
    }
    if ((mTabContainer != null) && (mTabContainer.getVisibility() != 8))
    {
      int j = getChildCount();
      paramInt1 = 0;
      for (paramInt2 = 0; paramInt2 < j; paramInt2++)
      {
        View localView = getChildAt(paramInt2);
        if (localView != mTabContainer)
        {
          int k;
          if (isCollapsed(localView)) {
            k = 0;
          } else {
            k = getMeasuredHeightWithMargins(localView);
          }
          paramInt1 = Math.max(paramInt1, k);
        }
      }
      if (View.MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
        paramInt2 = View.MeasureSpec.getSize(i);
      } else {
        paramInt2 = Integer.MAX_VALUE;
      }
      setMeasuredDimension(getMeasuredWidth(), Math.min(getMeasuredHeightWithMargins(mTabContainer) + paramInt1, paramInt2));
    }
  }
  
  public void onResolveDrawables(int paramInt)
  {
    super.onResolveDrawables(paramInt);
    if (mBackground != null) {
      mBackground.setLayoutDirection(paramInt);
    }
    if (mStackedBackground != null) {
      mStackedBackground.setLayoutDirection(paramInt);
    }
    if (mSplitBackground != null) {
      mSplitBackground.setLayoutDirection(paramInt);
    }
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    super.onTouchEvent(paramMotionEvent);
    return true;
  }
  
  public void setPrimaryBackground(Drawable paramDrawable)
  {
    if (mBackground != null)
    {
      mBackground.setCallback(null);
      unscheduleDrawable(mBackground);
    }
    mBackground = paramDrawable;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      if (mActionBarView != null) {
        mBackground.setBounds(mActionBarView.getLeft(), mActionBarView.getTop(), mActionBarView.getRight(), mActionBarView.getBottom());
      }
    }
    boolean bool1 = mIsSplit;
    boolean bool2 = false;
    if (bool1) {
      if (mSplitBackground != null) {}
    }
    do
    {
      bool1 = true;
      break;
      bool1 = bool2;
      break;
      bool1 = bool2;
      if (mBackground != null) {
        break;
      }
      bool1 = bool2;
    } while (mStackedBackground == null);
    setWillNotDraw(bool1);
    invalidate();
  }
  
  public void setSplitBackground(Drawable paramDrawable)
  {
    if (mSplitBackground != null)
    {
      mSplitBackground.setCallback(null);
      unscheduleDrawable(mSplitBackground);
    }
    mSplitBackground = paramDrawable;
    boolean bool1 = false;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      if ((mIsSplit) && (mSplitBackground != null)) {
        mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
      }
    }
    if (mIsSplit) {
      if (mSplitBackground != null) {}
    }
    boolean bool2;
    do
    {
      bool2 = true;
      break;
      bool2 = bool1;
      break;
      bool2 = bool1;
      if (mBackground != null) {
        break;
      }
      bool2 = bool1;
    } while (mStackedBackground == null);
    setWillNotDraw(bool2);
    invalidate();
  }
  
  public void setStackedBackground(Drawable paramDrawable)
  {
    if (mStackedBackground != null)
    {
      mStackedBackground.setCallback(null);
      unscheduleDrawable(mStackedBackground);
    }
    mStackedBackground = paramDrawable;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      if ((mIsStacked) && (mStackedBackground != null)) {
        mStackedBackground.setBounds(mTabContainer.getLeft(), mTabContainer.getTop(), mTabContainer.getRight(), mTabContainer.getBottom());
      }
    }
    boolean bool1 = mIsSplit;
    boolean bool2 = false;
    if (bool1) {
      if (mSplitBackground != null) {}
    }
    do
    {
      bool1 = true;
      break;
      bool1 = bool2;
      break;
      bool1 = bool2;
      if (mBackground != null) {
        break;
      }
      bool1 = bool2;
    } while (mStackedBackground == null);
    setWillNotDraw(bool1);
    invalidate();
  }
  
  public void setTabContainer(ScrollingTabContainerView paramScrollingTabContainerView)
  {
    if (mTabContainer != null) {
      removeView(mTabContainer);
    }
    mTabContainer = paramScrollingTabContainerView;
    if (paramScrollingTabContainerView != null)
    {
      addView(paramScrollingTabContainerView);
      ViewGroup.LayoutParams localLayoutParams = paramScrollingTabContainerView.getLayoutParams();
      width = -1;
      height = -2;
      paramScrollingTabContainerView.setAllowCollapse(false);
    }
  }
  
  public void setTransitioning(boolean paramBoolean)
  {
    mIsTransitioning = paramBoolean;
    int i;
    if (paramBoolean) {
      i = 393216;
    } else {
      i = 262144;
    }
    setDescendantFocusability(i);
  }
  
  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    boolean bool;
    if (paramInt == 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (mBackground != null) {
      mBackground.setVisible(bool, false);
    }
    if (mStackedBackground != null) {
      mStackedBackground.setVisible(bool, false);
    }
    if (mSplitBackground != null) {
      mSplitBackground.setVisible(bool, false);
    }
  }
  
  public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback, int paramInt)
  {
    if (paramInt != 0) {
      return super.startActionModeForChild(paramView, paramCallback, paramInt);
    }
    return null;
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if (((paramDrawable == mBackground) && (!mIsSplit)) || ((paramDrawable == mStackedBackground) && (mIsStacked)) || ((paramDrawable == mSplitBackground) && (mIsSplit)) || (super.verifyDrawable(paramDrawable))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private class ActionBarBackgroundDrawable
    extends Drawable
  {
    private ActionBarBackgroundDrawable() {}
    
    public void draw(Canvas paramCanvas)
    {
      if (mIsSplit)
      {
        if (mSplitBackground != null) {
          mSplitBackground.draw(paramCanvas);
        }
      }
      else
      {
        if (mBackground != null) {
          mBackground.draw(paramCanvas);
        }
        if ((mStackedBackground != null) && (mIsStacked)) {
          mStackedBackground.draw(paramCanvas);
        }
      }
    }
    
    public int getOpacity()
    {
      if (mIsSplit)
      {
        if ((mSplitBackground != null) && (mSplitBackground.getOpacity() == -1)) {
          return -1;
        }
      }
      else
      {
        if ((mIsStacked) && ((mStackedBackground == null) || (mStackedBackground.getOpacity() != -1))) {
          return 0;
        }
        if ((!ActionBarContainer.isCollapsed(mActionBarView)) && (mBackground != null) && (mBackground.getOpacity() == -1)) {
          return -1;
        }
      }
      return 0;
    }
    
    public void getOutline(Outline paramOutline)
    {
      if (mIsSplit)
      {
        if (mSplitBackground != null) {
          mSplitBackground.getOutline(paramOutline);
        }
      }
      else if (mBackground != null) {
        mBackground.getOutline(paramOutline);
      }
    }
    
    public void setAlpha(int paramInt) {}
    
    public void setColorFilter(ColorFilter paramColorFilter) {}
  }
}
