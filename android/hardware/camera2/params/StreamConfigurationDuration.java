package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Size;
import com.android.internal.util.Preconditions;

public final class StreamConfigurationDuration
{
  private final long mDurationNs;
  private final int mFormat;
  private final int mHeight;
  private final int mWidth;
  
  public StreamConfigurationDuration(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    mFormat = StreamConfigurationMap.checkArgumentFormatInternal(paramInt1);
    mWidth = Preconditions.checkArgumentPositive(paramInt2, "width must be positive");
    mHeight = Preconditions.checkArgumentPositive(paramInt3, "height must be positive");
    mDurationNs = Preconditions.checkArgumentNonnegative(paramLong, "durationNs must be non-negative");
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
    if ((paramObject instanceof StreamConfigurationDuration))
    {
      paramObject = (StreamConfigurationDuration)paramObject;
      boolean bool2 = bool1;
      if (mFormat == mFormat)
      {
        bool2 = bool1;
        if (mWidth == mWidth)
        {
          bool2 = bool1;
          if (mHeight == mHeight)
          {
            bool2 = bool1;
            if (mDurationNs == mDurationNs) {
              bool2 = true;
            }
          }
        }
      }
      return bool2;
    }
    return false;
  }
  
  public long getDuration()
  {
    return mDurationNs;
  }
  
  public final int getFormat()
  {
    return mFormat;
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public Size getSize()
  {
    return new Size(mWidth, mHeight);
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCode(new int[] { mFormat, mWidth, mHeight, (int)mDurationNs, (int)(mDurationNs >>> 32) });
  }
}
