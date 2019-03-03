package com.android.internal.widget;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public class BackgroundFallback
{
  private Drawable mBackgroundFallback;
  
  public BackgroundFallback() {}
  
  private boolean isOpaque(Drawable paramDrawable)
  {
    boolean bool;
    if ((paramDrawable != null) && (paramDrawable.getOpacity() == -1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean viewsCoverEntireWidth(View paramView1, View paramView2, int paramInt)
  {
    boolean bool;
    if ((paramView1.getLeft() <= 0) && (paramView1.getRight() >= paramView2.getLeft()) && (paramView2.getRight() >= paramInt)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void draw(ViewGroup paramViewGroup1, ViewGroup paramViewGroup2, Canvas paramCanvas, View paramView1, View paramView2, View paramView3)
  {
    if (!hasFallback()) {
      return;
    }
    int i = paramViewGroup1.getWidth();
    int j = paramViewGroup1.getHeight();
    int k = paramViewGroup2.getLeft();
    int m = paramViewGroup2.getTop();
    int n = 0;
    int i1 = 0;
    int i2 = paramViewGroup2.getChildCount();
    int i3 = j;
    int i4 = i;
    int i5 = 0;
    int i8;
    while (i5 < i2)
    {
      paramViewGroup1 = paramViewGroup2.getChildAt(i5);
      Drawable localDrawable = paramViewGroup1.getBackground();
      if (paramViewGroup1 == paramView1)
      {
        if ((localDrawable == null) && ((paramViewGroup1 instanceof ViewGroup)) && (((ViewGroup)paramViewGroup1).getChildCount() == 0))
        {
          i6 = i4;
          i7 = n;
          i8 = i1;
          i9 = i3;
          break label229;
        }
      }
      else
      {
        i6 = i4;
        i7 = n;
        i8 = i1;
        i9 = i3;
        if (paramViewGroup1.getVisibility() != 0) {
          break label229;
        }
        if (!isOpaque(localDrawable))
        {
          i6 = i4;
          i7 = n;
          i8 = i1;
          i9 = i3;
          break label229;
        }
      }
      int i6 = Math.min(i4, paramViewGroup1.getLeft() + k);
      i9 = Math.min(i3, paramViewGroup1.getTop() + m);
      int i7 = Math.max(n, paramViewGroup1.getRight() + k);
      i8 = Math.max(i1, paramViewGroup1.getBottom() + m);
      label229:
      i5++;
      i4 = i6;
      n = i7;
      i1 = i8;
      i3 = i9;
    }
    int i9 = 1;
    i5 = 0;
    i2 = i4;
    i4 = n;
    while (i5 < 2)
    {
      if (i5 == 0) {
        paramViewGroup1 = paramView2;
      } else {
        paramViewGroup1 = paramView3;
      }
      if ((paramViewGroup1 != null) && (paramViewGroup1.getVisibility() == 0) && (paramViewGroup1.getAlpha() == 1.0F) && (isOpaque(paramViewGroup1.getBackground())))
      {
        n = i2;
        if (paramViewGroup1.getTop() <= 0)
        {
          n = i2;
          if (paramViewGroup1.getBottom() >= j)
          {
            n = i2;
            if (paramViewGroup1.getLeft() <= 0)
            {
              n = i2;
              if (paramViewGroup1.getRight() >= i2) {
                n = 0;
              }
            }
          }
        }
        i2 = i4;
        if (paramViewGroup1.getTop() <= 0)
        {
          i2 = i4;
          if (paramViewGroup1.getBottom() >= j)
          {
            i2 = i4;
            if (paramViewGroup1.getLeft() <= i4)
            {
              i2 = i4;
              if (paramViewGroup1.getRight() >= i) {
                i2 = i;
              }
            }
          }
        }
        i4 = i3;
        if (paramViewGroup1.getTop() <= 0)
        {
          i4 = i3;
          if (paramViewGroup1.getBottom() >= i3)
          {
            i4 = i3;
            if (paramViewGroup1.getLeft() <= 0)
            {
              i4 = i3;
              if (paramViewGroup1.getRight() >= i) {
                i4 = 0;
              }
            }
          }
        }
        i3 = i1;
        if (paramViewGroup1.getTop() <= i1)
        {
          i3 = i1;
          if (paramViewGroup1.getBottom() >= j)
          {
            i3 = i1;
            if (paramViewGroup1.getLeft() <= 0)
            {
              i3 = i1;
              if (paramViewGroup1.getRight() >= i) {
                i3 = j;
              }
            }
          }
        }
        if ((paramViewGroup1.getTop() <= 0) && (paramViewGroup1.getBottom() >= i4)) {
          i1 = 1;
        } else {
          i1 = 0;
        }
        i1 = i9 & i1;
        i8 = n;
        n = i4;
      }
      else
      {
        i9 = 0;
        n = i3;
        i3 = i1;
        i8 = i2;
        i2 = i4;
        i1 = i9;
      }
      i5++;
      i9 = i1;
      i4 = i2;
      i2 = i8;
      i1 = i3;
      i3 = n;
    }
    n = i3;
    if (i9 != 0) {
      if (!viewsCoverEntireWidth(paramView2, paramView3, i))
      {
        n = i3;
        if (!viewsCoverEntireWidth(paramView3, paramView2, i)) {}
      }
      else
      {
        n = 0;
      }
    }
    if ((i2 < i4) && (n < i1))
    {
      if (n > 0)
      {
        mBackgroundFallback.setBounds(0, 0, i, n);
        mBackgroundFallback.draw(paramCanvas);
      }
      if (i2 > 0)
      {
        mBackgroundFallback.setBounds(0, n, i2, j);
        mBackgroundFallback.draw(paramCanvas);
      }
      if (i4 < i)
      {
        mBackgroundFallback.setBounds(i4, n, i, j);
        mBackgroundFallback.draw(paramCanvas);
      }
      if (i1 < j)
      {
        mBackgroundFallback.setBounds(i2, i1, i4, j);
        mBackgroundFallback.draw(paramCanvas);
      }
      return;
    }
  }
  
  public boolean hasFallback()
  {
    boolean bool;
    if (mBackgroundFallback != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setDrawable(Drawable paramDrawable)
  {
    mBackgroundFallback = paramDrawable;
  }
}
