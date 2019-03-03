package android.view;

import android.graphics.Rect;

public class Gravity
{
  public static final int AXIS_CLIP = 8;
  public static final int AXIS_PULL_AFTER = 4;
  public static final int AXIS_PULL_BEFORE = 2;
  public static final int AXIS_SPECIFIED = 1;
  public static final int AXIS_X_SHIFT = 0;
  public static final int AXIS_Y_SHIFT = 4;
  public static final int BOTTOM = 80;
  public static final int CENTER = 17;
  public static final int CENTER_HORIZONTAL = 1;
  public static final int CENTER_VERTICAL = 16;
  public static final int CLIP_HORIZONTAL = 8;
  public static final int CLIP_VERTICAL = 128;
  public static final int DISPLAY_CLIP_HORIZONTAL = 16777216;
  public static final int DISPLAY_CLIP_VERTICAL = 268435456;
  public static final int END = 8388613;
  public static final int FILL = 119;
  public static final int FILL_HORIZONTAL = 7;
  public static final int FILL_VERTICAL = 112;
  public static final int HORIZONTAL_GRAVITY_MASK = 7;
  public static final int LEFT = 3;
  public static final int NO_GRAVITY = 0;
  public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;
  public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;
  public static final int RIGHT = 5;
  public static final int START = 8388611;
  public static final int TOP = 48;
  public static final int VERTICAL_GRAVITY_MASK = 112;
  
  public Gravity() {}
  
  public static void apply(int paramInt1, int paramInt2, int paramInt3, Rect paramRect1, int paramInt4, int paramInt5, Rect paramRect2)
  {
    int i = paramInt1 & 0x6;
    if (i != 0)
    {
      if (i != 2)
      {
        if (i != 4)
        {
          left += paramInt4;
          right += paramInt4;
        }
        else
        {
          right -= paramInt4;
          left = (right - paramInt2);
          if (((paramInt1 & 0x8) == 8) && (left < left)) {
            left = left;
          }
        }
      }
      else
      {
        left += paramInt4;
        right = (left + paramInt2);
        if (((paramInt1 & 0x8) == 8) && (right > right)) {
          right = right;
        }
      }
    }
    else
    {
      left = (left + (right - left - paramInt2) / 2 + paramInt4);
      right = (left + paramInt2);
      if ((paramInt1 & 0x8) == 8)
      {
        if (left < left) {
          left = left;
        }
        if (right > right) {
          right = right;
        }
      }
    }
    paramInt2 = paramInt1 & 0x60;
    if (paramInt2 != 0)
    {
      if (paramInt2 != 32)
      {
        if (paramInt2 != 64)
        {
          top += paramInt5;
          bottom += paramInt5;
        }
        else
        {
          bottom -= paramInt5;
          top = (bottom - paramInt3);
          if (((paramInt1 & 0x80) == 128) && (top < top)) {
            top = top;
          }
        }
      }
      else
      {
        top += paramInt5;
        bottom = (top + paramInt3);
        if (((paramInt1 & 0x80) == 128) && (bottom > bottom)) {
          bottom = bottom;
        }
      }
    }
    else
    {
      top = (top + (bottom - top - paramInt3) / 2 + paramInt5);
      bottom = (top + paramInt3);
      if ((paramInt1 & 0x80) == 128)
      {
        if (top < top) {
          top = top;
        }
        if (bottom > bottom) {
          bottom = bottom;
        }
      }
    }
  }
  
  public static void apply(int paramInt1, int paramInt2, int paramInt3, Rect paramRect1, int paramInt4, int paramInt5, Rect paramRect2, int paramInt6)
  {
    apply(getAbsoluteGravity(paramInt1, paramInt6), paramInt2, paramInt3, paramRect1, paramInt4, paramInt5, paramRect2);
  }
  
  public static void apply(int paramInt1, int paramInt2, int paramInt3, Rect paramRect1, Rect paramRect2)
  {
    apply(paramInt1, paramInt2, paramInt3, paramRect1, 0, 0, paramRect2);
  }
  
  public static void apply(int paramInt1, int paramInt2, int paramInt3, Rect paramRect1, Rect paramRect2, int paramInt4)
  {
    apply(getAbsoluteGravity(paramInt1, paramInt4), paramInt2, paramInt3, paramRect1, 0, 0, paramRect2);
  }
  
  public static void applyDisplay(int paramInt, Rect paramRect1, Rect paramRect2)
  {
    if ((0x10000000 & paramInt) != 0)
    {
      if (top < top) {
        top = top;
      }
      if (bottom > bottom) {
        bottom = bottom;
      }
    }
    else
    {
      int i = 0;
      if (top < top) {
        i = top - top;
      } else if (bottom > bottom) {
        i = bottom - bottom;
      }
      if (i != 0) {
        if (paramRect2.height() > bottom - top)
        {
          top = top;
          bottom = bottom;
        }
        else
        {
          top += i;
          bottom += i;
        }
      }
    }
    if ((0x1000000 & paramInt) != 0)
    {
      if (left < left) {
        left = left;
      }
      if (right > right) {
        right = right;
      }
    }
    else
    {
      paramInt = 0;
      if (left < left) {
        paramInt = left - left;
      } else if (right > right) {
        paramInt = right - right;
      }
      if (paramInt != 0) {
        if (paramRect2.width() > right - left)
        {
          left = left;
          right = right;
        }
        else
        {
          left += paramInt;
          right += paramInt;
        }
      }
    }
  }
  
  public static void applyDisplay(int paramInt1, Rect paramRect1, Rect paramRect2, int paramInt2)
  {
    applyDisplay(getAbsoluteGravity(paramInt1, paramInt2), paramRect1, paramRect2);
  }
  
  public static int getAbsoluteGravity(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    paramInt1 = i;
    if ((0x800000 & i) > 0)
    {
      if ((i & 0x800003) == 8388611)
      {
        paramInt1 = i & 0xFF7FFFFC;
        if (paramInt2 == 1) {
          paramInt1 |= 0x5;
        } else {
          paramInt1 |= 0x3;
        }
      }
      else
      {
        paramInt1 = i;
        if ((i & 0x800005) == 8388613)
        {
          paramInt1 = i & 0xFF7FFFFA;
          if (paramInt2 == 1) {
            paramInt1 |= 0x3;
          } else {
            paramInt1 |= 0x5;
          }
        }
      }
      paramInt1 &= 0xFF7FFFFF;
    }
    return paramInt1;
  }
  
  public static boolean isHorizontal(int paramInt)
  {
    boolean bool;
    if ((paramInt > 0) && ((0x800007 & paramInt) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isVertical(int paramInt)
  {
    boolean bool;
    if ((paramInt > 0) && ((paramInt & 0x70) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static String toString(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramInt & 0x77) == 119)
    {
      localStringBuilder.append("FILL");
      localStringBuilder.append(' ');
    }
    else
    {
      if ((paramInt & 0x70) == 112)
      {
        localStringBuilder.append("FILL_VERTICAL");
        localStringBuilder.append(' ');
      }
      else
      {
        if ((paramInt & 0x30) == 48)
        {
          localStringBuilder.append("TOP");
          localStringBuilder.append(' ');
        }
        if ((paramInt & 0x50) == 80)
        {
          localStringBuilder.append("BOTTOM");
          localStringBuilder.append(' ');
        }
      }
      if ((paramInt & 0x7) == 7)
      {
        localStringBuilder.append("FILL_HORIZONTAL");
        localStringBuilder.append(' ');
      }
      else
      {
        if ((paramInt & 0x800003) == 8388611)
        {
          localStringBuilder.append("START");
          localStringBuilder.append(' ');
        }
        else if ((paramInt & 0x3) == 3)
        {
          localStringBuilder.append("LEFT");
          localStringBuilder.append(' ');
        }
        if ((paramInt & 0x800005) == 8388613)
        {
          localStringBuilder.append("END");
          localStringBuilder.append(' ');
        }
        else if ((paramInt & 0x5) == 5)
        {
          localStringBuilder.append("RIGHT");
          localStringBuilder.append(' ');
        }
      }
    }
    if ((paramInt & 0x11) == 17)
    {
      localStringBuilder.append("CENTER");
      localStringBuilder.append(' ');
    }
    else
    {
      if ((paramInt & 0x10) == 16)
      {
        localStringBuilder.append("CENTER_VERTICAL");
        localStringBuilder.append(' ');
      }
      if ((paramInt & 0x1) == 1)
      {
        localStringBuilder.append("CENTER_HORIZONTAL");
        localStringBuilder.append(' ');
      }
    }
    if (localStringBuilder.length() == 0)
    {
      localStringBuilder.append("NO GRAVITY");
      localStringBuilder.append(' ');
    }
    if ((paramInt & 0x10000000) == 268435456)
    {
      localStringBuilder.append("DISPLAY_CLIP_VERTICAL");
      localStringBuilder.append(' ');
    }
    if ((paramInt & 0x1000000) == 16777216)
    {
      localStringBuilder.append("DISPLAY_CLIP_HORIZONTAL");
      localStringBuilder.append(' ');
    }
    localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }
}
