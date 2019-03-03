package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R.styleable;

@RemoteViews.RemoteView
public class MessagingLinearLayout
  extends ViewGroup
{
  private int mMaxDisplayedLines = Integer.MAX_VALUE;
  private MessagingLayout mMessagingLayout;
  private int mSpacing;
  
  public MessagingLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MessagingLinearLayout, 0, 0);
    int i = paramContext.getIndexCount();
    for (int j = 0; j < i; j++) {
      if (paramContext.getIndex(j) == 0) {
        mSpacing = paramContext.getDimensionPixelSize(j, 0);
      }
    }
    paramContext.recycle();
  }
  
  public static boolean isGone(View paramView)
  {
    if (paramView.getVisibility() == 8) {
      return true;
    }
    paramView = paramView.getLayoutParams();
    return ((paramView instanceof LayoutParams)) && (hide);
  }
  
  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    if ((getLayoutParamshide) && (!((MessagingChild)paramView).isHidingAnimated())) {
      return true;
    }
    return super.drawChild(paramCanvas, paramView, paramLong);
  }
  
  protected LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-1, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    return new LayoutParams(mContext, paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    LayoutParams localLayoutParams = new LayoutParams(width, height);
    if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
      localLayoutParams.copyMarginsFrom((ViewGroup.MarginLayoutParams)paramLayoutParams);
    }
    return localLayoutParams;
  }
  
  public MessagingLayout getMessagingLayout()
  {
    return mMessagingLayout;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = mPaddingLeft;
    int j = mPaddingRight;
    int k = getLayoutDirection();
    int m = getChildCount();
    paramInt2 = mPaddingTop;
    paramBoolean = isShown();
    int n = 1;
    for (paramInt4 = 0; paramInt4 < m; paramInt4++)
    {
      View localView = getChildAt(paramInt4);
      if (localView.getVisibility() != 8)
      {
        LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
        MessagingChild localMessagingChild = (MessagingChild)localView;
        int i1 = localView.getMeasuredWidth();
        int i2 = localView.getMeasuredHeight();
        int i3;
        if (k == 1) {
          i3 = paramInt3 - paramInt1 - j - i1 - rightMargin;
        } else {
          i3 = i + leftMargin;
        }
        if (hide)
        {
          if ((paramBoolean) && (visibleBefore))
          {
            localView.layout(i3, paramInt2, i3 + i1, lastVisibleHeight + paramInt2);
            localMessagingChild.hideAnimated();
          }
          visibleBefore = false;
        }
        else
        {
          visibleBefore = true;
          lastVisibleHeight = i2;
          int i4 = paramInt2;
          if (n == 0) {
            i4 = paramInt2 + mSpacing;
          }
          paramInt2 = i4 + topMargin;
          localView.layout(i3, paramInt2, i3 + i1, paramInt2 + i2);
          paramInt2 += bottomMargin + i2;
          n = 0;
        }
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt2);
    if (View.MeasureSpec.getMode(paramInt2) == 0) {
      for (;;)
      {
        i = Integer.MAX_VALUE;
      }
    }
    int j = mPaddingLeft;
    int k = mPaddingRight;
    int m = getChildCount();
    for (int n = 0; n < m; n++) {
      getChildAtgetLayoutParamshide = true;
    }
    int i1 = mPaddingTop;
    int i2 = mPaddingBottom;
    int i3 = mMaxDisplayedLines;
    int i4 = m - 1;
    n = j + k;
    j = i1 + i2;
    i2 = 1;
    while ((i4 >= 0) && (j < i))
    {
      if (getChildAt(i4).getVisibility() == 8)
      {
        i1 = j;
      }
      else
      {
        View localView = getChildAt(i4);
        LayoutParams localLayoutParams = (LayoutParams)getChildAt(i4).getLayoutParams();
        MessagingChild localMessagingChild = null;
        i1 = mSpacing;
        k = i1;
        if ((localView instanceof MessagingChild))
        {
          localMessagingChild = (MessagingChild)localView;
          localMessagingChild.setMaxDisplayedLines(i3);
          k = i1 + localMessagingChild.getExtraSpacing();
        }
        if (i2 != 0) {
          k = 0;
        }
        measureChildWithMargins(localView, paramInt1, 0, paramInt2, j - mPaddingTop - mPaddingBottom + k);
        int i5 = Math.max(j, j + localView.getMeasuredHeight() + topMargin + bottomMargin + k);
        int i6 = 0;
        i2 = 0;
        k = i3;
        if (localMessagingChild != null)
        {
          i2 = localMessagingChild.getMeasuredType();
          k = i3 - localMessagingChild.getConsumedLines();
        }
        if (i2 == 1) {
          i3 = 1;
        } else {
          i3 = 0;
        }
        if (i2 == 2) {
          i7 = 1;
        } else {
          i7 = 0;
        }
        i1 = j;
        i2 = n;
        if (i5 > i) {
          break label480;
        }
        i1 = j;
        i2 = n;
        if (i7 != 0) {
          break label480;
        }
        j = i5;
        int i7 = Math.max(n, localView.getMeasuredWidth() + leftMargin + rightMargin + mPaddingLeft + mPaddingRight);
        hide = false;
        i1 = j;
        i2 = i7;
        if (i3 != 0) {
          break label480;
        }
        i1 = j;
        i3 = k;
        i2 = i6;
        n = i7;
        if (k <= 0)
        {
          i1 = j;
          i2 = i7;
          break label480;
        }
      }
      i4--;
      j = i1;
    }
    i2 = n;
    i1 = j;
    label480:
    setMeasuredDimension(resolveSize(Math.max(getSuggestedMinimumWidth(), i2), paramInt1), Math.max(getSuggestedMinimumHeight(), i1));
  }
  
  @RemotableViewMethod
  public void setMaxDisplayedLines(int paramInt)
  {
    mMaxDisplayedLines = paramInt;
  }
  
  public void setMessagingLayout(MessagingLayout paramMessagingLayout)
  {
    mMessagingLayout = paramMessagingLayout;
  }
  
  public static class LayoutParams
    extends ViewGroup.MarginLayoutParams
  {
    public boolean hide = false;
    public int lastVisibleHeight;
    public boolean visibleBefore = false;
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }
    
    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
  }
  
  public static abstract interface MessagingChild
  {
    public static final int MEASURED_NORMAL = 0;
    public static final int MEASURED_SHORTENED = 1;
    public static final int MEASURED_TOO_SMALL = 2;
    
    public abstract int getConsumedLines();
    
    public int getExtraSpacing()
    {
      return 0;
    }
    
    public abstract int getMeasuredType();
    
    public abstract void hideAnimated();
    
    public abstract boolean isHidingAnimated();
    
    public abstract void setMaxDisplayedLines(int paramInt);
  }
}
