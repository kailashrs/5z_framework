package com.android.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import com.android.internal.R.styleable;

public class WeightedLinearLayout
  extends LinearLayout
{
  private float mMajorWeightMax;
  private float mMajorWeightMin;
  private float mMinorWeightMax;
  private float mMinorWeightMin;
  
  public WeightedLinearLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public WeightedLinearLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    paramContext = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.WeightedLinearLayout);
    mMajorWeightMin = paramContext.getFloat(1, 0.0F);
    mMinorWeightMin = paramContext.getFloat(3, 0.0F);
    mMajorWeightMax = paramContext.getFloat(0, 0.0F);
    mMinorWeightMax = paramContext.getFloat(2, 0.0F);
    paramContext.recycle();
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    DisplayMetrics localDisplayMetrics = getContext().getResources().getDisplayMetrics();
    int i = widthPixels;
    if (i < heightPixels) {
      j = 1;
    } else {
      j = 0;
    }
    int k = View.MeasureSpec.getMode(paramInt1);
    super.onMeasure(paramInt1, paramInt2);
    int m = getMeasuredWidth();
    int n = 0;
    int i1 = View.MeasureSpec.makeMeasureSpec(m, 1073741824);
    float f1;
    if (j != 0) {
      f1 = mMinorWeightMin;
    } else {
      f1 = mMajorWeightMin;
    }
    float f2;
    if (j != 0) {
      f2 = mMinorWeightMax;
    } else {
      f2 = mMajorWeightMax;
    }
    paramInt1 = n;
    int j = i1;
    if (k == Integer.MIN_VALUE)
    {
      paramInt1 = (int)(i * f1);
      i = (int)(i * f1);
      if ((f1 > 0.0F) && (m < paramInt1))
      {
        j = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
        paramInt1 = 1;
      }
      else
      {
        paramInt1 = n;
        j = i1;
        if (f2 > 0.0F)
        {
          paramInt1 = n;
          j = i1;
          if (m > i)
          {
            j = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
            paramInt1 = 1;
          }
        }
      }
    }
    if (paramInt1 != 0) {
      super.onMeasure(j, paramInt2);
    }
  }
}
