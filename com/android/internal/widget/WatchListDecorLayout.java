package com.android.internal.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import java.util.ArrayList;

public class WatchListDecorLayout
  extends FrameLayout
  implements ViewTreeObserver.OnScrollChangedListener
{
  private View mBottomPanel;
  private int mForegroundPaddingBottom = 0;
  private int mForegroundPaddingLeft = 0;
  private int mForegroundPaddingRight = 0;
  private int mForegroundPaddingTop = 0;
  private ListView mListView;
  private final ArrayList<View> mMatchParentChildren = new ArrayList(1);
  private ViewTreeObserver mObserver;
  private int mPendingScroll;
  private View mTopPanel;
  
  public WatchListDecorLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public WatchListDecorLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public WatchListDecorLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private void applyMeasureToChild(View paramView, int paramInt1, int paramInt2)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    if (width == -1) {
      paramInt1 = View.MeasureSpec.makeMeasureSpec(Math.max(0, getMeasuredWidth() - getPaddingLeftWithForeground() - getPaddingRightWithForeground() - leftMargin - rightMargin), 1073741824);
    } else {
      paramInt1 = getChildMeasureSpec(paramInt1, getPaddingLeftWithForeground() + getPaddingRightWithForeground() + leftMargin + rightMargin, width);
    }
    if (height == -1) {
      paramInt2 = View.MeasureSpec.makeMeasureSpec(Math.max(0, getMeasuredHeight() - getPaddingTopWithForeground() - getPaddingBottomWithForeground() - topMargin - bottomMargin), 1073741824);
    } else {
      paramInt2 = getChildMeasureSpec(paramInt2, getPaddingTopWithForeground() + getPaddingBottomWithForeground() + topMargin + bottomMargin, height);
    }
    paramView.measure(paramInt1, paramInt2);
  }
  
  private int getPaddingBottomWithForeground()
  {
    int i;
    if (isForegroundInsidePadding()) {
      i = Math.max(mPaddingBottom, mForegroundPaddingBottom);
    } else {
      i = mPaddingBottom + mForegroundPaddingBottom;
    }
    return i;
  }
  
  private int getPaddingLeftWithForeground()
  {
    int i;
    if (isForegroundInsidePadding()) {
      i = Math.max(mPaddingLeft, mForegroundPaddingLeft);
    } else {
      i = mPaddingLeft + mForegroundPaddingLeft;
    }
    return i;
  }
  
  private int getPaddingRightWithForeground()
  {
    int i;
    if (isForegroundInsidePadding()) {
      i = Math.max(mPaddingRight, mForegroundPaddingRight);
    } else {
      i = mPaddingRight + mForegroundPaddingRight;
    }
    return i;
  }
  
  private int getPaddingTopWithForeground()
  {
    int i;
    if (isForegroundInsidePadding()) {
      i = Math.max(mPaddingTop, mForegroundPaddingTop);
    } else {
      i = mPaddingTop + mForegroundPaddingTop;
    }
    return i;
  }
  
  private int measureAndGetHeight(View paramView, int paramInt1, int paramInt2)
  {
    if (paramView != null)
    {
      if (paramView.getVisibility() != 8)
      {
        applyMeasureToChild(mBottomPanel, paramInt1, paramInt2);
        return paramView.getMeasuredHeight();
      }
      if (getMeasureAllChildren()) {
        applyMeasureToChild(mBottomPanel, paramInt1, paramInt2);
      }
    }
    return 0;
  }
  
  private void setScrolling(View paramView, float paramFloat)
  {
    if (paramView.getTranslationY() != paramFloat) {
      paramView.setTranslationY(paramFloat);
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    int i = 0;
    mPendingScroll = 0;
    while (i < getChildCount())
    {
      View localView = getChildAt(i);
      if ((localView instanceof ListView))
      {
        if (mListView == null)
        {
          mListView = ((ListView)localView);
          mListView.setNestedScrollingEnabled(true);
          mObserver = mListView.getViewTreeObserver();
          mObserver.addOnScrollChangedListener(this);
        }
        else
        {
          throw new IllegalArgumentException("only one ListView child allowed");
        }
      }
      else
      {
        int j = getLayoutParamsgravity & 0x70;
        if ((j == 48) && (mTopPanel == null)) {
          mTopPanel = localView;
        } else if ((j == 80) && (mBottomPanel == null)) {
          mBottomPanel = localView;
        }
      }
      i++;
    }
  }
  
  public void onDetachedFromWindow()
  {
    mListView = null;
    mBottomPanel = null;
    mTopPanel = null;
    if (mObserver != null)
    {
      if (mObserver.isAlive()) {
        mObserver.removeOnScrollChangedListener(this);
      }
      mObserver = null;
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = 0;
    if ((j == 1073741824) && (View.MeasureSpec.getMode(paramInt2) == 1073741824)) {
      m = 0;
    } else {
      m = 1;
    }
    mMatchParentChildren.clear();
    int n = 0;
    int i1 = 0;
    j = 0;
    int i2 = 0;
    while (i2 < i)
    {
      View localView = getChildAt(i2);
      int i3;
      if ((!getMeasureAllChildren()) && (localView.getVisibility() == 8))
      {
        i3 = n;
      }
      else
      {
        measureChildWithMargins(localView, paramInt1, 0, paramInt2, 0);
        localObject = (FrameLayout.LayoutParams)localView.getLayoutParams();
        i1 = Math.max(i1, localView.getMeasuredWidth() + leftMargin + rightMargin);
        i3 = Math.max(n, localView.getMeasuredHeight() + topMargin + bottomMargin);
        j = combineMeasuredStates(j, localView.getMeasuredState());
        if ((m != 0) && ((width == -1) || (height == -1))) {
          mMatchParentChildren.add(localView);
        }
        n = i1;
        i1 = n;
      }
      i2++;
      n = i3;
    }
    i2 = getPaddingLeftWithForeground();
    int m = getPaddingRightWithForeground();
    n = Math.max(n + (getPaddingTopWithForeground() + getPaddingBottomWithForeground()), getSuggestedMinimumHeight());
    i1 = Math.max(i1 + (i2 + m), getSuggestedMinimumWidth());
    Object localObject = getForeground();
    i2 = n;
    m = i1;
    if (localObject != null)
    {
      i2 = Math.max(n, ((Drawable)localObject).getMinimumHeight());
      m = Math.max(i1, ((Drawable)localObject).getMinimumWidth());
    }
    setMeasuredDimension(resolveSizeAndState(m, paramInt1, j), resolveSizeAndState(i2, paramInt2, j << 16));
    if (mListView != null)
    {
      if (mPendingScroll != 0)
      {
        mListView.scrollListBy(mPendingScroll);
        mPendingScroll = 0;
      }
      j = Math.max(mListView.getPaddingTop(), measureAndGetHeight(mTopPanel, paramInt1, paramInt2));
      m = Math.max(mListView.getPaddingBottom(), measureAndGetHeight(mBottomPanel, paramInt1, paramInt2));
      if ((j != mListView.getPaddingTop()) || (m != mListView.getPaddingBottom()))
      {
        mPendingScroll += mListView.getPaddingTop() - j;
        mListView.setPadding(mListView.getPaddingLeft(), j, mListView.getPaddingRight(), m);
      }
    }
    m = mMatchParentChildren.size();
    if (m > 1) {
      for (j = k; j < m; j++)
      {
        localObject = (View)mMatchParentChildren.get(j);
        if ((mListView == null) || ((localObject != mTopPanel) && (localObject != mBottomPanel))) {
          applyMeasureToChild((View)localObject, paramInt1, paramInt2);
        }
      }
    }
  }
  
  public void onScrollChanged()
  {
    if (mListView == null) {
      return;
    }
    View localView;
    if (mTopPanel != null) {
      if (mListView.getChildCount() > 0)
      {
        if (mListView.getFirstVisiblePosition() == 0)
        {
          localView = mListView.getChildAt(0);
          setScrolling(mTopPanel, localView.getY() - mTopPanel.getHeight() - mTopPanel.getTop());
        }
        else
        {
          setScrolling(mTopPanel, -mTopPanel.getHeight());
        }
      }
      else {
        setScrolling(mTopPanel, 0.0F);
      }
    }
    if (mBottomPanel != null) {
      if (mListView.getChildCount() > 0)
      {
        if (mListView.getLastVisiblePosition() >= mListView.getCount() - 1)
        {
          localView = mListView.getChildAt(mListView.getChildCount() - 1);
          setScrolling(mBottomPanel, Math.max(0.0F, localView.getY() + localView.getHeight() - mBottomPanel.getTop()));
        }
        else
        {
          setScrolling(mBottomPanel, mBottomPanel.getHeight());
        }
      }
      else {
        setScrolling(mBottomPanel, 0.0F);
      }
    }
  }
  
  public void setForegroundGravity(int paramInt)
  {
    if (getForegroundGravity() != paramInt)
    {
      super.setForegroundGravity(paramInt);
      Drawable localDrawable = getForeground();
      if ((getForegroundGravity() == 119) && (localDrawable != null))
      {
        Rect localRect = new Rect();
        if (localDrawable.getPadding(localRect))
        {
          mForegroundPaddingLeft = left;
          mForegroundPaddingTop = top;
          mForegroundPaddingRight = right;
          mForegroundPaddingBottom = bottom;
        }
      }
      else
      {
        mForegroundPaddingLeft = 0;
        mForegroundPaddingTop = 0;
        mForegroundPaddingRight = 0;
        mForegroundPaddingBottom = 0;
      }
    }
  }
}
