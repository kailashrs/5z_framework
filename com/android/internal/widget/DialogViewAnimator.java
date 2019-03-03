package com.android.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ViewAnimator;
import java.util.ArrayList;

public class DialogViewAnimator
  extends ViewAnimator
{
  private final ArrayList<View> mMatchParentChildren = new ArrayList(1);
  
  public DialogViewAnimator(Context paramContext)
  {
    super(paramContext);
  }
  
  public DialogViewAnimator(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if ((View.MeasureSpec.getMode(paramInt1) == 1073741824) && (View.MeasureSpec.getMode(paramInt2) == 1073741824)) {
      i = 0;
    } else {
      i = 1;
    }
    int j = getChildCount();
    int k = 0;
    int m = 0;
    int n = 0;
    Object localObject2;
    for (int i1 = 0; i1 < j; i1++)
    {
      localObject1 = getChildAt(i1);
      if ((!getMeasureAllChildren()) && (((View)localObject1).getVisibility() == 8)) {
        continue;
      }
      localObject2 = (FrameLayout.LayoutParams)((View)localObject1).getLayoutParams();
      if (width == -1) {
        i2 = 1;
      } else {
        i2 = 0;
      }
      int i3;
      if (height == -1) {
        i3 = 1;
      } else {
        i3 = 0;
      }
      if ((i != 0) && ((i2 != 0) || (i3 != 0))) {
        mMatchParentChildren.add(localObject1);
      }
      measureChildWithMargins((View)localObject1, paramInt1, 0, paramInt2, 0);
      int i4 = 0;
      i5 = i4;
      i6 = m;
      if (i != 0)
      {
        i5 = i4;
        i6 = m;
        if (i2 == 0)
        {
          i6 = Math.max(m, ((View)localObject1).getMeasuredWidth() + leftMargin + rightMargin);
          i5 = 0x0 | ((View)localObject1).getMeasuredWidthAndState() & 0xFF000000;
        }
      }
      int i2 = i5;
      m = k;
      if (i != 0)
      {
        i2 = i5;
        m = k;
        if (i3 == 0)
        {
          m = Math.max(k, ((View)localObject1).getMeasuredHeight() + topMargin + bottomMargin);
          i2 = i5 | ((View)localObject1).getMeasuredHeightAndState() >> 16 & 0xFF00;
        }
      }
      n = combineMeasuredStates(n, i2);
      k = m;
      m = i6;
    }
    int i5 = getPaddingLeft();
    int i = getPaddingRight();
    int i6 = Math.max(k + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight());
    i5 = Math.max(m + (i5 + i), getSuggestedMinimumWidth());
    Object localObject1 = getForeground();
    m = i6;
    k = i5;
    if (localObject1 != null)
    {
      m = Math.max(i6, ((Drawable)localObject1).getMinimumHeight());
      k = Math.max(i5, ((Drawable)localObject1).getMinimumWidth());
    }
    setMeasuredDimension(resolveSizeAndState(k, paramInt1, n), resolveSizeAndState(m, paramInt2, n << 16));
    i5 = mMatchParentChildren.size();
    for (k = 0; k < i5; k++)
    {
      localObject2 = (View)mMatchParentChildren.get(k);
      localObject1 = (ViewGroup.MarginLayoutParams)((View)localObject2).getLayoutParams();
      if (width == -1) {
        m = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - leftMargin - rightMargin, 1073741824);
      } else {
        m = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + leftMargin + rightMargin, width);
      }
      if (height == -1) {
        i6 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom() - topMargin - bottomMargin, 1073741824);
      } else {
        i6 = getChildMeasureSpec(paramInt2, getPaddingTop() + getPaddingBottom() + topMargin + bottomMargin, height);
      }
      ((View)localObject2).measure(m, i6);
    }
    mMatchParentChildren.clear();
  }
}
