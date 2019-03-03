package com.android.internal.policy;

import android.graphics.Rect;

public class DockedDividerUtils
{
  public DockedDividerUtils() {}
  
  public static void calculateBoundsForPosition(int paramInt1, int paramInt2, Rect paramRect, int paramInt3, int paramInt4, int paramInt5)
  {
    boolean bool = false;
    paramRect.set(0, 0, paramInt3, paramInt4);
    switch (paramInt2)
    {
    default: 
      break;
    case 4: 
      top = (paramInt1 + paramInt5);
      break;
    case 3: 
      left = (paramInt1 + paramInt5);
      break;
    case 2: 
      bottom = paramInt1;
      break;
    case 1: 
      right = paramInt1;
    }
    if ((paramInt2 != 1) && (paramInt2 != 2)) {
      break label101;
    }
    bool = true;
    label101:
    sanitizeStackBounds(paramRect, bool);
  }
  
  public static int calculateMiddlePosition(boolean paramBoolean, Rect paramRect, int paramInt1, int paramInt2, int paramInt3)
  {
    int i;
    if (paramBoolean) {
      i = top;
    } else {
      i = left;
    }
    if (paramBoolean) {
      paramInt1 = paramInt2 - bottom;
    } else {
      paramInt1 -= right;
    }
    return (paramInt1 - i) / 2 + i - paramInt3 / 2;
  }
  
  public static int calculatePositionForBounds(Rect paramRect, int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return 0;
    case 4: 
      return top - paramInt2;
    case 3: 
      return left - paramInt2;
    case 2: 
      return bottom;
    }
    return right;
  }
  
  public static int getDockSideFromCreatedMode(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      if (paramBoolean2) {
        return 2;
      }
      return 1;
    }
    if (paramBoolean2) {
      return 4;
    }
    return 3;
  }
  
  public static int invertDockSide(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return -1;
    case 4: 
      return 2;
    case 3: 
      return 1;
    case 2: 
      return 4;
    }
    return 3;
  }
  
  public static void sanitizeStackBounds(Rect paramRect, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if (left >= right) {
        left = (right - 1);
      }
      if (top >= bottom) {
        top = (bottom - 1);
      }
    }
    else
    {
      if (right <= left) {
        right = (left + 1);
      }
      if (bottom <= top) {
        bottom = (top + 1);
      }
    }
  }
}
