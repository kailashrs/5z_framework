package android.graphics;

public class ImageFormat
{
  public static final int DEPTH16 = 1144402265;
  public static final int DEPTH_POINT_CLOUD = 257;
  public static final int FLEX_RGBA_8888 = 42;
  public static final int FLEX_RGB_888 = 41;
  public static final int JPEG = 256;
  public static final int NV16 = 16;
  public static final int NV21 = 17;
  public static final int PRIVATE = 34;
  public static final int RAW10 = 37;
  public static final int RAW12 = 38;
  public static final int RAW_DEPTH = 4098;
  public static final int RAW_PRIVATE = 36;
  public static final int RAW_SENSOR = 32;
  public static final int RGB_565 = 4;
  public static final int UNKNOWN = 0;
  public static final int Y16 = 540422489;
  public static final int Y8 = 538982489;
  public static final int YUV_420_888 = 35;
  public static final int YUV_422_888 = 39;
  public static final int YUV_444_888 = 40;
  public static final int YUY2 = 20;
  public static final int YV12 = 842094169;
  
  public ImageFormat() {}
  
  public static int getBitsPerPixel(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          return -1;
        case 842094169: 
          return 12;
        case 540422489: 
        case 1144402265: 
          return 16;
        case 538982489: 
          return 8;
        case 35: 
          return 12;
        case 32: 
        case 4098: 
          return 16;
        case 20: 
          return 16;
        }
        return 16;
      case 42: 
        return 32;
      case 41: 
        return 24;
      case 40: 
        return 24;
      case 39: 
        return 16;
      case 38: 
        return 12;
      }
      return 10;
    case 17: 
      return 12;
    }
    return 16;
  }
  
  public static boolean isPublicFormat(int paramInt)
  {
    if ((paramInt != 4) && (paramInt != 20) && (paramInt != 32) && (paramInt != 4098) && (paramInt != 842094169) && (paramInt != 1144402265)) {
      switch (paramInt)
      {
      default: 
        switch (paramInt)
        {
        default: 
          switch (paramInt)
          {
          default: 
            return false;
          }
          break;
        }
        break;
      }
    }
    return true;
  }
}
