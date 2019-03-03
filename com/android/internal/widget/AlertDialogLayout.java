package com.android.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class AlertDialogLayout
  extends LinearLayout
{
  public AlertDialogLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public AlertDialogLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public AlertDialogLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public AlertDialogLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (int j = 0; j < paramInt1; j++)
    {
      View localView = getChildAt(j);
      if (localView.getVisibility() != 8)
      {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)localView.getLayoutParams();
        if (width == -1)
        {
          int k = height;
          height = localView.getMeasuredHeight();
          measureChildWithMargins(localView, i, 0, paramInt2, 0);
          height = k;
        }
      }
    }
  }
  
  private int resolveMinimumHeight(View paramView)
  {
    int i = paramView.getMinimumHeight();
    if (i > 0) {
      return i;
    }
    if ((paramView instanceof ViewGroup))
    {
      paramView = (ViewGroup)paramView;
      if (paramView.getChildCount() == 1) {
        return resolveMinimumHeight(paramView.getChildAt(0));
      }
    }
    return 0;
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
  }
  
  private boolean tryOnMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    Object localObject1 = null;
    Object localObject2 = null;
    Object localObject3 = null;
    View localView;
    for (int j = 0; j < i; j++)
    {
      localView = getChildAt(j);
      if (localView.getVisibility() != 8)
      {
        k = localView.getId();
        if (k != 16908826)
        {
          if ((k != 16908873) && (k != 16908880))
          {
            if (k != 16909491) {
              return false;
            }
            localObject3 = localView;
          }
          else
          {
            if (localObject1 != null) {
              return false;
            }
            localObject1 = localView;
          }
        }
        else {
          localObject2 = localView;
        }
      }
    }
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = View.MeasureSpec.getSize(paramInt2);
    int i1 = View.MeasureSpec.getMode(paramInt1);
    int i2 = 0;
    j = getPaddingTop() + getPaddingBottom();
    int i3 = j;
    if (localObject3 != null)
    {
      localObject3.measure(paramInt1, 0);
      i3 = j + localObject3.getMeasuredHeight();
      i2 = combineMeasuredStates(0, localObject3.getMeasuredState());
    }
    j = 0;
    int i4 = 0;
    int k = i2;
    int i5 = i3;
    if (localObject2 != null)
    {
      localObject2.measure(paramInt1, 0);
      j = resolveMinimumHeight(localObject2);
      i4 = localObject2.getMeasuredHeight() - j;
      i5 = i3 + j;
      k = combineMeasuredStates(i2, localObject2.getMeasuredState());
    }
    int i6 = 0;
    if (localObject1 != null)
    {
      if (m == 0) {
        i3 = 0;
      } else {
        i3 = View.MeasureSpec.makeMeasureSpec(Math.max(0, n - i5), m);
      }
      localObject1.measure(paramInt1, i3);
      i6 = localObject1.getMeasuredHeight();
      i5 += i6;
      k = combineMeasuredStates(k, localObject1.getMeasuredState());
    }
    int i7 = n - i5;
    i3 = i7;
    i2 = k;
    n = i5;
    if (localObject2 != null)
    {
      n = Math.min(i7, i4);
      i3 = i7;
      i2 = j;
      if (n > 0)
      {
        i3 = i7 - n;
        i2 = j + n;
      }
      localObject2.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i2, 1073741824));
      n = i5 - j + localObject2.getMeasuredHeight();
      i2 = combineMeasuredStates(k, localObject2.getMeasuredState());
    }
    if ((localObject1 != null) && (i3 > 0))
    {
      localObject1.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(i6 + i3, m));
      n = n - i6 + localObject1.getMeasuredHeight();
      i2 = combineMeasuredStates(i2, localObject1.getMeasuredState());
      i3 -= i3;
    }
    k = 0;
    i5 = 0;
    while (i5 < i)
    {
      localView = getChildAt(i5);
      j = k;
      if (localView.getVisibility() != 8) {
        j = Math.max(k, localView.getMeasuredWidth());
      }
      i5++;
      k = j;
    }
    setMeasuredDimension(resolveSizeAndState(k + (getPaddingLeft() + getPaddingRight()), paramInt1, i2), resolveSizeAndState(n, paramInt2, 0));
    if (i1 != 1073741824) {
      forceUniformWidth(i, paramInt2);
    }
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Object localObject = this;
    int i = mPaddingLeft;
    int j = paramInt3 - paramInt1;
    int k = mPaddingRight;
    int m = mPaddingRight;
    paramInt1 = getMeasuredHeight();
    int n = getChildCount();
    int i1 = getGravity();
    int i2 = i1 & 0x70;
    if (i2 != 16)
    {
      if (i2 != 80) {
        paramInt1 = mPaddingTop;
      } else {
        paramInt1 = mPaddingTop + paramInt4 - paramInt2 - paramInt1;
      }
    }
    else {
      paramInt1 = mPaddingTop + (paramInt4 - paramInt2 - paramInt1) / 2;
    }
    localObject = getDividerDrawable();
    paramInt2 = 0;
    if (localObject == null) {
      paramInt3 = 0;
    } else {
      paramInt3 = ((Drawable)localObject).getIntrinsicHeight();
    }
    paramInt4 = n;
    for (n = paramInt2;; n++)
    {
      AlertDialogLayout localAlertDialogLayout = this;
      if (n >= paramInt4) {
        break;
      }
      View localView = localAlertDialogLayout.getChildAt(n);
      if (localView != null) {
        if (localView.getVisibility() != 8)
        {
          int i3 = localView.getMeasuredWidth();
          int i4 = localView.getMeasuredHeight();
          LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)localView.getLayoutParams();
          int i5 = gravity;
          paramInt2 = i5;
          if (i5 < 0) {
            paramInt2 = i1 & 0x800007;
          }
          paramInt2 = Gravity.getAbsoluteGravity(paramInt2, getLayoutDirection()) & 0x7;
          if (paramInt2 != 1)
          {
            if (paramInt2 != 5) {
              paramInt2 = leftMargin + i;
            } else {
              paramInt2 = j - k - i3 - rightMargin;
            }
          }
          else {
            paramInt2 = (j - i - m - i3) / 2 + i + leftMargin - rightMargin;
          }
          i5 = paramInt1;
          if (localAlertDialogLayout.hasDividerBeforeChildAt(n)) {
            i5 = paramInt1 + paramInt3;
          }
          paramInt1 = i5 + topMargin;
          localAlertDialogLayout.setChildFrame(localView, paramInt2, paramInt1, i3, i4);
          paramInt1 += i4 + bottomMargin;
        }
        else {}
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (!tryOnMeasure(paramInt1, paramInt2)) {
      super.onMeasure(paramInt1, paramInt2);
    }
  }
}
