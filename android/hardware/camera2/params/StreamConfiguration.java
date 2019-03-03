package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;
import android.util.Size;
import com.android.internal.util.Preconditions;

public final class StreamConfiguration
{
  private final int mFormat;
  private final int mHeight;
  private final boolean mInput;
  private final int mWidth;
  
  public StreamConfiguration(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    mFormat = StreamConfigurationMap.checkArgumentFormatInternal(paramInt1);
    mWidth = Preconditions.checkArgumentPositive(paramInt2, "width must be positive");
    mHeight = Preconditions.checkArgumentPositive(paramInt3, "height must be positive");
    mInput = paramBoolean;
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
    if ((paramObject instanceof StreamConfiguration))
    {
      paramObject = (StreamConfiguration)paramObject;
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
            if (mInput == mInput) {
              bool2 = true;
            }
          }
        }
      }
      return bool2;
    }
    return false;
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
    return HashCodeHelpers.hashCode(new int[] { mFormat, mWidth, mHeight, mInput });
  }
  
  public boolean isInput()
  {
    return mInput;
  }
  
  public boolean isOutput()
  {
    return mInput ^ true;
  }
}
