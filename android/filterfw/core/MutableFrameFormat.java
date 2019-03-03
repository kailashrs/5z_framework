package android.filterfw.core;

import java.util.Arrays;

public class MutableFrameFormat
  extends FrameFormat
{
  public MutableFrameFormat() {}
  
  public MutableFrameFormat(int paramInt1, int paramInt2)
  {
    super(paramInt1, paramInt2);
  }
  
  public void setBaseType(int paramInt)
  {
    mBaseType = paramInt;
    mBytesPerSample = bytesPerSampleOf(paramInt);
  }
  
  public void setBytesPerSample(int paramInt)
  {
    mBytesPerSample = paramInt;
    mSize = -1;
  }
  
  public void setDimensionCount(int paramInt)
  {
    mDimensions = new int[paramInt];
  }
  
  public void setDimensions(int paramInt)
  {
    mDimensions = new int[] { paramInt };
    mSize = -1;
  }
  
  public void setDimensions(int paramInt1, int paramInt2)
  {
    mDimensions = new int[] { paramInt1, paramInt2 };
    mSize = -1;
  }
  
  public void setDimensions(int paramInt1, int paramInt2, int paramInt3)
  {
    mDimensions = new int[] { paramInt1, paramInt2, paramInt3 };
    mSize = -1;
  }
  
  public void setDimensions(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      paramArrayOfInt = null;
    } else {
      paramArrayOfInt = Arrays.copyOf(paramArrayOfInt, paramArrayOfInt.length);
    }
    mDimensions = paramArrayOfInt;
    mSize = -1;
  }
  
  public void setMetaValue(String paramString, Object paramObject)
  {
    if (mMetaData == null) {
      mMetaData = new KeyValueMap();
    }
    mMetaData.put(paramString, paramObject);
  }
  
  public void setObjectClass(Class paramClass)
  {
    mObjectClass = paramClass;
  }
  
  public void setTarget(int paramInt)
  {
    mTarget = paramInt;
  }
}
