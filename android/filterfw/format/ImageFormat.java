package android.filterfw.format;

import android.filterfw.core.MutableFrameFormat;
import android.graphics.Bitmap;

public class ImageFormat
{
  public static final int COLORSPACE_GRAY = 1;
  public static final String COLORSPACE_KEY = "colorspace";
  public static final int COLORSPACE_RGB = 2;
  public static final int COLORSPACE_RGBA = 3;
  public static final int COLORSPACE_YUV = 4;
  
  public ImageFormat() {}
  
  public static int bytesPerSampleForColorspace(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown colorspace id ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append("!");
      throw new RuntimeException(localStringBuilder.toString());
    case 4: 
      return 3;
    case 3: 
      return 4;
    case 2: 
      return 3;
    }
    return 1;
  }
  
  public static MutableFrameFormat create(int paramInt)
  {
    return create(0, 0, paramInt, bytesPerSampleForColorspace(paramInt), 0);
  }
  
  public static MutableFrameFormat create(int paramInt1, int paramInt2)
  {
    return create(0, 0, paramInt1, bytesPerSampleForColorspace(paramInt1), paramInt2);
  }
  
  public static MutableFrameFormat create(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    return create(paramInt1, paramInt2, paramInt3, bytesPerSampleForColorspace(paramInt3), paramInt4);
  }
  
  public static MutableFrameFormat create(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    MutableFrameFormat localMutableFrameFormat = new MutableFrameFormat(2, paramInt5);
    localMutableFrameFormat.setDimensions(paramInt1, paramInt2);
    localMutableFrameFormat.setBytesPerSample(paramInt4);
    localMutableFrameFormat.setMetaValue("colorspace", Integer.valueOf(paramInt3));
    if (paramInt5 == 1) {
      localMutableFrameFormat.setObjectClass(Bitmap.class);
    }
    return localMutableFrameFormat;
  }
}
