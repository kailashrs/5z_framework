package android.hardware.camera2.params;

import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Size;
import com.android.internal.util.Preconditions;

public final class MeteringRectangle
{
  public static final int METERING_WEIGHT_DONT_CARE = 0;
  public static final int METERING_WEIGHT_MAX = 1000;
  public static final int METERING_WEIGHT_MIN = 0;
  private final int mHeight;
  private final int mWeight;
  private final int mWidth;
  private final int mX;
  private final int mY;
  
  public MeteringRectangle(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    mX = Preconditions.checkArgumentNonnegative(paramInt1, "x must be nonnegative");
    mY = Preconditions.checkArgumentNonnegative(paramInt2, "y must be nonnegative");
    mWidth = Preconditions.checkArgumentNonnegative(paramInt3, "width must be nonnegative");
    mHeight = Preconditions.checkArgumentNonnegative(paramInt4, "height must be nonnegative");
    mWeight = Preconditions.checkArgumentInRange(paramInt5, 0, 1000, "meteringWeight");
  }
  
  public MeteringRectangle(Point paramPoint, Size paramSize, int paramInt)
  {
    Preconditions.checkNotNull(paramPoint, "xy must not be null");
    Preconditions.checkNotNull(paramSize, "dimensions must not be null");
    mX = Preconditions.checkArgumentNonnegative(x, "x must be nonnegative");
    mY = Preconditions.checkArgumentNonnegative(y, "y must be nonnegative");
    mWidth = Preconditions.checkArgumentNonnegative(paramSize.getWidth(), "width must be nonnegative");
    mHeight = Preconditions.checkArgumentNonnegative(paramSize.getHeight(), "height must be nonnegative");
    mWeight = Preconditions.checkArgumentNonnegative(paramInt, "meteringWeight must be nonnegative");
  }
  
  public MeteringRectangle(Rect paramRect, int paramInt)
  {
    Preconditions.checkNotNull(paramRect, "rect must not be null");
    mX = Preconditions.checkArgumentNonnegative(left, "rect.left must be nonnegative");
    mY = Preconditions.checkArgumentNonnegative(top, "rect.top must be nonnegative");
    mWidth = Preconditions.checkArgumentNonnegative(paramRect.width(), "rect.width must be nonnegative");
    mHeight = Preconditions.checkArgumentNonnegative(paramRect.height(), "rect.height must be nonnegative");
    mWeight = Preconditions.checkArgumentNonnegative(paramInt, "meteringWeight must be nonnegative");
  }
  
  public boolean equals(MeteringRectangle paramMeteringRectangle)
  {
    boolean bool1 = false;
    if (paramMeteringRectangle == null) {
      return false;
    }
    boolean bool2 = bool1;
    if (mX == mX)
    {
      bool2 = bool1;
      if (mY == mY)
      {
        bool2 = bool1;
        if (mWidth == mWidth)
        {
          bool2 = bool1;
          if (mHeight == mHeight)
          {
            bool2 = bool1;
            if (mWeight == mWeight) {
              bool2 = true;
            }
          }
        }
      }
    }
    return bool2;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof MeteringRectangle)) && (equals((MeteringRectangle)paramObject))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getMeteringWeight()
  {
    return mWeight;
  }
  
  public Rect getRect()
  {
    return new Rect(mX, mY, mX + mWidth, mY + mHeight);
  }
  
  public Size getSize()
  {
    return new Size(mWidth, mHeight);
  }
  
  public Point getUpperLeftPoint()
  {
    return new Point(mX, mY);
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public int getX()
  {
    return mX;
  }
  
  public int getY()
  {
    return mY;
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCode(new int[] { mX, mY, mWidth, mHeight, mWeight });
  }
  
  public String toString()
  {
    return String.format("(x:%d, y:%d, w:%d, h:%d, wt:%d)", new Object[] { Integer.valueOf(mX), Integer.valueOf(mY), Integer.valueOf(mWidth), Integer.valueOf(mHeight), Integer.valueOf(mWeight) });
  }
}
