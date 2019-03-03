package android.filterfw.format;

import android.filterfw.core.MutableFrameFormat;

public class PrimitiveFormat
{
  public PrimitiveFormat() {}
  
  public static MutableFrameFormat createByteFormat(int paramInt)
  {
    return createFormat(2, paramInt);
  }
  
  public static MutableFrameFormat createByteFormat(int paramInt1, int paramInt2)
  {
    return createFormat(2, paramInt1, paramInt2);
  }
  
  public static MutableFrameFormat createDoubleFormat(int paramInt)
  {
    return createFormat(6, paramInt);
  }
  
  public static MutableFrameFormat createDoubleFormat(int paramInt1, int paramInt2)
  {
    return createFormat(6, paramInt1, paramInt2);
  }
  
  public static MutableFrameFormat createFloatFormat(int paramInt)
  {
    return createFormat(5, paramInt);
  }
  
  public static MutableFrameFormat createFloatFormat(int paramInt1, int paramInt2)
  {
    return createFormat(5, paramInt1, paramInt2);
  }
  
  private static MutableFrameFormat createFormat(int paramInt1, int paramInt2)
  {
    MutableFrameFormat localMutableFrameFormat = new MutableFrameFormat(paramInt1, paramInt2);
    localMutableFrameFormat.setDimensionCount(1);
    return localMutableFrameFormat;
  }
  
  private static MutableFrameFormat createFormat(int paramInt1, int paramInt2, int paramInt3)
  {
    MutableFrameFormat localMutableFrameFormat = new MutableFrameFormat(paramInt1, paramInt3);
    localMutableFrameFormat.setDimensions(paramInt2);
    return localMutableFrameFormat;
  }
  
  public static MutableFrameFormat createInt16Format(int paramInt)
  {
    return createFormat(3, paramInt);
  }
  
  public static MutableFrameFormat createInt16Format(int paramInt1, int paramInt2)
  {
    return createFormat(3, paramInt1, paramInt2);
  }
  
  public static MutableFrameFormat createInt32Format(int paramInt)
  {
    return createFormat(4, paramInt);
  }
  
  public static MutableFrameFormat createInt32Format(int paramInt1, int paramInt2)
  {
    return createFormat(4, paramInt1, paramInt2);
  }
}
