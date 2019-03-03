package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;

public final class InputConfiguration
{
  private final int mFormat;
  private final int mHeight;
  private final int mWidth;
  
  public InputConfiguration(int paramInt1, int paramInt2, int paramInt3)
  {
    mWidth = paramInt1;
    mHeight = paramInt2;
    mFormat = paramInt3;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof InputConfiguration)) {
      return false;
    }
    paramObject = (InputConfiguration)paramObject;
    return (paramObject.getWidth() == mWidth) && (paramObject.getHeight() == mHeight) && (paramObject.getFormat() == mFormat);
  }
  
  public int getFormat()
  {
    return mFormat;
  }
  
  public int getHeight()
  {
    return mHeight;
  }
  
  public int getWidth()
  {
    return mWidth;
  }
  
  public int hashCode()
  {
    return HashCodeHelpers.hashCode(new int[] { mWidth, mHeight, mFormat });
  }
  
  public String toString()
  {
    return String.format("InputConfiguration(w:%d, h:%d, format:%d)", new Object[] { Integer.valueOf(mWidth), Integer.valueOf(mHeight), Integer.valueOf(mFormat) });
  }
}
