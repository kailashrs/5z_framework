package com.android.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;

public class PreferenceImageView
  extends ImageView
{
  public PreferenceImageView(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public PreferenceImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public PreferenceImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public PreferenceImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1, paramInt2);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j;
    int m;
    if (i != Integer.MIN_VALUE)
    {
      j = paramInt1;
      if (i != 0) {}
    }
    else
    {
      k = View.MeasureSpec.getSize(paramInt1);
      m = getMaxWidth();
      j = paramInt1;
      if (m != Integer.MAX_VALUE) {
        if (m >= k)
        {
          j = paramInt1;
          if (i != 0) {}
        }
        else
        {
          j = View.MeasureSpec.makeMeasureSpec(m, Integer.MIN_VALUE);
        }
      }
    }
    int k = View.MeasureSpec.getMode(paramInt2);
    if (k != Integer.MIN_VALUE)
    {
      paramInt1 = paramInt2;
      if (k != 0) {}
    }
    else
    {
      i = View.MeasureSpec.getSize(paramInt2);
      m = getMaxHeight();
      paramInt1 = paramInt2;
      if (m != Integer.MAX_VALUE) {
        if (m >= i)
        {
          paramInt1 = paramInt2;
          if (k != 0) {}
        }
        else
        {
          paramInt1 = View.MeasureSpec.makeMeasureSpec(m, Integer.MIN_VALUE);
        }
      }
    }
    super.onMeasure(j, paramInt1);
  }
}
