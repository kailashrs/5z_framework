package android.graphics;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PixelFormat
{
  @Deprecated
  public static final int A_8 = 8;
  @Deprecated
  public static final int JPEG = 256;
  @Deprecated
  public static final int LA_88 = 10;
  @Deprecated
  public static final int L_8 = 9;
  public static final int OPAQUE = -1;
  public static final int RGBA_1010102 = 43;
  @Deprecated
  public static final int RGBA_4444 = 7;
  @Deprecated
  public static final int RGBA_5551 = 6;
  public static final int RGBA_8888 = 1;
  public static final int RGBA_F16 = 22;
  public static final int RGBX_8888 = 2;
  @Deprecated
  public static final int RGB_332 = 11;
  public static final int RGB_565 = 4;
  public static final int RGB_888 = 3;
  public static final int TRANSLUCENT = -3;
  public static final int TRANSPARENT = -2;
  public static final int UNKNOWN = 0;
  @Deprecated
  public static final int YCbCr_420_SP = 17;
  @Deprecated
  public static final int YCbCr_422_I = 20;
  @Deprecated
  public static final int YCbCr_422_SP = 16;
  public int bitsPerPixel;
  public int bytesPerPixel;
  
  public PixelFormat() {}
  
  public static boolean formatHasAlpha(int paramInt)
  {
    if ((paramInt != 1) && (paramInt != 10) && (paramInt != 22) && (paramInt != 43)) {
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
    }
    return true;
  }
  
  public static String formatToString(int paramInt)
  {
    if (paramInt != 20)
    {
      if (paramInt != 22)
      {
        if (paramInt != 43)
        {
          if (paramInt != 256)
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
                  switch (paramInt)
                  {
                  default: 
                    return Integer.toString(paramInt);
                  case 17: 
                    return "YCbCr_420_SP";
                  }
                  return "YCbCr_422_SP";
                case 11: 
                  return "RGB_332";
                case 10: 
                  return "LA_88";
                case 9: 
                  return "L_8";
                case 8: 
                  return "A_8";
                case 7: 
                  return "RGBA_4444";
                }
                return "RGBA_5551";
              case 4: 
                return "RGB_565";
              case 3: 
                return "RGB_888";
              case 2: 
                return "RGBX_8888";
              case 1: 
                return "RGBA_8888";
              }
              return "UNKNOWN";
            case -2: 
              return "TRANSPARENT";
            }
            return "TRANSLUCENT";
          }
          return "JPEG";
        }
        return "RGBA_1010102";
      }
      return "RGBA_F16";
    }
    return "YCbCr_422_I";
  }
  
  public static void getPixelFormatInfo(int paramInt, PixelFormat paramPixelFormat)
  {
    if (paramInt != 20)
    {
      if (paramInt != 22)
      {
        if (paramInt != 43)
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
                paramPixelFormat = new StringBuilder();
                paramPixelFormat.append("unknown pixel format ");
                paramPixelFormat.append(paramInt);
                throw new IllegalArgumentException(paramPixelFormat.toString());
              case 17: 
                bitsPerPixel = 12;
                bytesPerPixel = 1;
              }
              break;
            case 8: 
            case 9: 
            case 11: 
              bitsPerPixel = 8;
              bytesPerPixel = 1;
            }
            break;
          case 4: 
            bitsPerPixel = 16;
            bytesPerPixel = 2;
            break;
          case 3: 
            bitsPerPixel = 24;
            bytesPerPixel = 3;
            break;
          }
        }
        else
        {
          bitsPerPixel = 32;
          bytesPerPixel = 4;
        }
      }
      else
      {
        bitsPerPixel = 64;
        bytesPerPixel = 8;
      }
    }
    else
    {
      bitsPerPixel = 16;
      bytesPerPixel = 1;
    }
  }
  
  public static boolean isPublicFormat(int paramInt)
  {
    if ((paramInt != 22) && (paramInt != 43)) {
      switch (paramInt)
      {
      default: 
        return false;
      }
    }
    return true;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Format {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Opacity {}
}
