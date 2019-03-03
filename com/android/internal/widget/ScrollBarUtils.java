package com.android.internal.widget;

public class ScrollBarUtils
{
  public ScrollBarUtils() {}
  
  public static int getThumbLength(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramInt2 *= 2;
    paramInt3 = Math.round(paramInt1 * paramInt3 / paramInt4);
    paramInt1 = paramInt3;
    if (paramInt3 < paramInt2) {
      paramInt1 = paramInt2;
    }
    return paramInt1;
  }
  
  public static int getThumbOffset(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    paramInt4 = Math.round((paramInt1 - paramInt2) * paramInt5 / (paramInt4 - paramInt3));
    paramInt3 = paramInt4;
    if (paramInt4 > paramInt1 - paramInt2) {
      paramInt3 = paramInt1 - paramInt2;
    }
    return paramInt3;
  }
}
