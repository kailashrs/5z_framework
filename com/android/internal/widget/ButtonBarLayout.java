package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.android.internal.R.styleable;

public class ButtonBarLayout
  extends LinearLayout
{
  private static final int PEEK_BUTTON_DP = 16;
  private boolean mAllowStacking;
  private int mLastWidthSize = -1;
  private int mMinimumHeight = 0;
  
  public ButtonBarLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ButtonBarLayout);
    mAllowStacking = paramContext.getBoolean(0, true);
    paramContext.recycle();
  }
  
  private int getNextVisibleChildIndex(int paramInt)
  {
    int i = getChildCount();
    while (paramInt < i)
    {
      if (getChildAt(paramInt).getVisibility() == 0) {
        return paramInt;
      }
      paramInt++;
    }
    return -1;
  }
  
  private boolean isStacked()
  {
    int i = getOrientation();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  private void setStacked(boolean paramBoolean)
  {
    setOrientation(paramBoolean);
    int i;
    if (paramBoolean) {
      i = 8388613;
    } else {
      i = 80;
    }
    setGravity(i);
    View localView = findViewById(16909389);
    if (localView != null)
    {
      if (paramBoolean) {
        paramBoolean = true;
      } else {
        paramBoolean = true;
      }
      localView.setVisibility(paramBoolean);
    }
    for (paramBoolean = getChildCount() - 2; !paramBoolean; paramBoolean--) {
      bringChildToFront(getChildAt(paramBoolean));
    }
  }
  
  public int getMinimumHeight()
  {
    return Math.max(mMinimumHeight, super.getMinimumHeight());
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    if (mAllowStacking)
    {
      if ((i > mLastWidthSize) && (isStacked())) {
        setStacked(false);
      }
      mLastWidthSize = i;
    }
    int j = 0;
    if ((!isStacked()) && (View.MeasureSpec.getMode(paramInt1) == 1073741824))
    {
      i = View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE);
      j = 1;
    }
    else
    {
      i = paramInt1;
    }
    super.onMeasure(i, paramInt2);
    i = j;
    if (mAllowStacking)
    {
      i = j;
      if (!isStacked())
      {
        i = j;
        if ((0xFF000000 & getMeasuredWidthAndState()) == 16777216)
        {
          setStacked(true);
          i = 1;
        }
      }
    }
    if (i != 0) {
      super.onMeasure(paramInt1, paramInt2);
    }
    paramInt1 = 0;
    j = getNextVisibleChildIndex(0);
    if (j >= 0)
    {
      View localView = getChildAt(j);
      LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)localView.getLayoutParams();
      paramInt2 = 0 + (getPaddingTop() + localView.getMeasuredHeight() + topMargin + bottomMargin);
      if (isStacked())
      {
        j = getNextVisibleChildIndex(j + 1);
        paramInt1 = paramInt2;
        if (j >= 0) {
          paramInt1 = (int)(paramInt2 + (getChildAt(j).getPaddingTop() + 16.0F * getResourcesgetDisplayMetricsdensity));
        }
      }
      else
      {
        paramInt1 = paramInt2 + getPaddingBottom();
      }
    }
    if (getMinimumHeight() != paramInt1) {
      setMinimumHeight(paramInt1);
    }
  }
  
  public void setAllowStacking(boolean paramBoolean)
  {
    if (mAllowStacking != paramBoolean)
    {
      mAllowStacking = paramBoolean;
      if ((!mAllowStacking) && (getOrientation() == 1)) {
        setStacked(false);
      }
      requestLayout();
    }
  }
}
