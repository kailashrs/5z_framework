package com.android.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RemoteViews.RemoteView;

@RemoteViews.RemoteView
public class RemeasuringLinearLayout
  extends LinearLayout
{
  public RemeasuringLinearLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public RemeasuringLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public RemeasuringLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public RemeasuringLinearLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    int i = getChildCount();
    paramInt2 = 0;
    paramInt1 = 0;
    while (paramInt1 < i)
    {
      View localView = getChildAt(paramInt1);
      int j = paramInt2;
      if (localView != null) {
        if (localView.getVisibility() == 8)
        {
          j = paramInt2;
        }
        else
        {
          LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)localView.getLayoutParams();
          j = Math.max(paramInt2, localView.getMeasuredHeight() + paramInt2 + topMargin + bottomMargin);
        }
      }
      paramInt1++;
      paramInt2 = j;
    }
    setMeasuredDimension(getMeasuredWidth(), paramInt2);
  }
}
