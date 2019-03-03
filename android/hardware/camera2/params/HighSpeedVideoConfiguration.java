package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Range;
import android.util.Size;
import com.android.internal.util.Preconditions;

public final class HighSpeedVideoConfiguration
{
  private static final int HIGH_SPEED_MAX_MINIMAL_FPS = 120;
  private final int mBatchSizeMax;
  private final int mFpsMax;
  private final int mFpsMin;
  private final Range<Integer> mFpsRange;
  private final int mHeight;
  private final Size mSize;
  private final int mWidth;
  
  public HighSpeedVideoConfiguration(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (paramInt4 >= 120)
    {
      mFpsMax = paramInt4;
      mWidth = Preconditions.checkArgumentPositive(paramInt1, "width must be positive");
      mHeight = Preconditions.checkArgumentPositive(paramInt2, "height must be positive");
      mFpsMin = Preconditions.checkArgumentPositive(paramInt3, "fpsMin must be positive");
      mSize = new Size(mWidth, mHeight);
      mBatchSizeMax = Preconditions.checkArgumentPositive(paramInt5, "batchSizeMax must be positive");
      mFpsRange = new Range(Integer.valueOf(mFpsMin), Integer.valueOf(mFpsMax));
      return;
    }
    throw new IllegalArgumentException("fpsMax must be at least 120");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = false;
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof HighSpeedVideoConfiguration))
    {
      paramObject = (HighSpeedVideoConfiguration)paramObject;
      boolean bool2 = bool1;
      if (mWidth == mWidth)
      {
        bool2 = bool1;
        if (mHeight == mHeight)
        {
          bool2 = bool1;
          if (mFpsMin == mFpsMin)
          {
            bool2 = bool1;
            if (mFpsMax == mFpsMax)
            {
              bool2 = bool1;
              if (mBatchSizeMax == mBatchSizeMax) {
                bool2 = true;
              }
            }
          }
        }
      }
      return bool2;
    }
    return false;
  }
  
  public int getBatchSizeMax()
  {
    return mBatchSizeMax;
  }
  
  public int getFpsMax()
  {
    return mFpsMax;
  }
  
  public int getFpsMin()
  {
    return mFpsMin;
  }
  
  public Range<Integer> getFpsRange()
  {
    return mFpsRange;
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public Size getSize()
  {
    return mSize;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCode(new int[] { mWidth, mHeight, mFpsMin, mFpsMax });
  }
}
