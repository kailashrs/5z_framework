package android.graphics;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Outline
{
  public static final int MODE_CONVEX_PATH = 2;
  public static final int MODE_EMPTY = 0;
  public static final int MODE_ROUND_RECT = 1;
  private static final float RADIUS_UNDEFINED = Float.NEGATIVE_INFINITY;
  public float mAlpha;
  public int mMode = 0;
  public Path mPath;
  public float mRadius = Float.NEGATIVE_INFINITY;
  public final Rect mRect = new Rect();
  
  public Outline() {}
  
  public Outline(Outline paramOutline)
  {
    set(paramOutline);
  }
  
  public boolean canClip()
  {
    boolean bool;
    if (mMode != 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public float getAlpha()
  {
    return mAlpha;
  }
  
  public float getRadius()
  {
    return mRadius;
  }
  
  public boolean getRect(Rect paramRect)
  {
    if (mMode != 1) {
      return false;
    }
    paramRect.set(mRect);
    return true;
  }
  
  public boolean isEmpty()
  {
    boolean bool;
    if (mMode == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void offset(int paramInt1, int paramInt2)
  {
    if (mMode == 1) {
      mRect.offset(paramInt1, paramInt2);
    } else if (mMode == 2) {
      mPath.offset(paramInt1, paramInt2);
    }
  }
  
  public void set(Outline paramOutline)
  {
    mMode = mMode;
    if (mMode == 2)
    {
      if (mPath == null) {
        mPath = new Path();
      }
      mPath.set(mPath);
    }
    mRect.set(mRect);
    mRadius = mRadius;
    mAlpha = mAlpha;
  }
  
  public void setAlpha(float paramFloat)
  {
    mAlpha = paramFloat;
  }
  
  public void setConvexPath(Path paramPath)
  {
    if (paramPath.isEmpty())
    {
      setEmpty();
      return;
    }
    if (paramPath.isConvex())
    {
      if (mPath == null) {
        mPath = new Path();
      }
      mMode = 2;
      mPath.set(paramPath);
      mRect.setEmpty();
      mRadius = Float.NEGATIVE_INFINITY;
      return;
    }
    throw new IllegalArgumentException("path must be convex");
  }
  
  public void setEmpty()
  {
    if (mPath != null) {
      mPath.rewind();
    }
    mMode = 0;
    mRect.setEmpty();
    mRadius = Float.NEGATIVE_INFINITY;
  }
  
  public void setOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt1 < paramInt3) && (paramInt2 < paramInt4))
    {
      if (paramInt4 - paramInt2 == paramInt3 - paramInt1)
      {
        setRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, (paramInt4 - paramInt2) / 2.0F);
        return;
      }
      if (mPath == null) {
        mPath = new Path();
      } else {
        mPath.rewind();
      }
      mMode = 2;
      mPath.addOval(paramInt1, paramInt2, paramInt3, paramInt4, Path.Direction.CW);
      mRect.setEmpty();
      mRadius = Float.NEGATIVE_INFINITY;
      return;
    }
    setEmpty();
  }
  
  public void setOval(Rect paramRect)
  {
    setOval(left, top, right, bottom);
  }
  
  public void setRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    setRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, 0.0F);
  }
  
  public void setRect(Rect paramRect)
  {
    setRect(left, top, right, bottom);
  }
  
  public void setRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat)
  {
    if ((paramInt1 < paramInt3) && (paramInt2 < paramInt4))
    {
      if (mMode == 2) {
        mPath.rewind();
      }
      mMode = 1;
      mRect.set(paramInt1, paramInt2, paramInt3, paramInt4);
      mRadius = paramFloat;
      return;
    }
    setEmpty();
  }
  
  public void setRoundRect(Rect paramRect, float paramFloat)
  {
    setRoundRect(left, top, right, bottom, paramFloat);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Mode {}
}
