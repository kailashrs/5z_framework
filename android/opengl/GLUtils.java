package android.opengl;

import android.graphics.Bitmap;

public final class GLUtils
{
  private GLUtils() {}
  
  public static String getEGLErrorString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(paramInt));
      return localStringBuilder.toString();
    case 12302: 
      return "EGL_CONTEXT_LOST";
    case 12301: 
      return "EGL_BAD_SURFACE";
    case 12300: 
      return "EGL_BAD_PARAMETER";
    case 12299: 
      return "EGL_BAD_NATIVE_WINDOW";
    case 12298: 
      return "EGL_BAD_NATIVE_PIXMAP";
    case 12297: 
      return "EGL_BAD_MATCH";
    case 12296: 
      return "EGL_BAD_DISPLAY";
    case 12295: 
      return "EGL_BAD_CURRENT_SURFACE";
    case 12294: 
      return "EGL_BAD_CONTEXT";
    case 12293: 
      return "EGL_BAD_CONFIG";
    case 12292: 
      return "EGL_BAD_ATTRIBUTE";
    case 12291: 
      return "EGL_BAD_ALLOC";
    case 12290: 
      return "EGL_BAD_ACCESS";
    case 12289: 
      return "EGL_NOT_INITIALIZED";
    }
    return "EGL_SUCCESS";
  }
  
  public static int getInternalFormat(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      if (!paramBitmap.isRecycled())
      {
        int i = native_getInternalFormat(paramBitmap);
        if (i >= 0) {
          return i;
        }
        throw new IllegalArgumentException("Unknown internalformat");
      }
      throw new IllegalArgumentException("bitmap is recycled");
    }
    throw new NullPointerException("getInternalFormat can't be used with a null Bitmap");
  }
  
  public static int getType(Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      if (!paramBitmap.isRecycled())
      {
        int i = native_getType(paramBitmap);
        if (i >= 0) {
          return i;
        }
        throw new IllegalArgumentException("Unknown type");
      }
      throw new IllegalArgumentException("bitmap is recycled");
    }
    throw new NullPointerException("getType can't be used with a null Bitmap");
  }
  
  private static native int native_getInternalFormat(Bitmap paramBitmap);
  
  private static native int native_getType(Bitmap paramBitmap);
  
  private static native int native_texImage2D(int paramInt1, int paramInt2, int paramInt3, Bitmap paramBitmap, int paramInt4, int paramInt5);
  
  private static native int native_texSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Bitmap paramBitmap, int paramInt5, int paramInt6);
  
  public static void texImage2D(int paramInt1, int paramInt2, int paramInt3, Bitmap paramBitmap, int paramInt4)
  {
    if (paramBitmap != null)
    {
      if (!paramBitmap.isRecycled())
      {
        if (native_texImage2D(paramInt1, paramInt2, paramInt3, paramBitmap, -1, paramInt4) == 0) {
          return;
        }
        throw new IllegalArgumentException("invalid Bitmap format");
      }
      throw new IllegalArgumentException("bitmap is recycled");
    }
    throw new NullPointerException("texImage2D can't be used with a null Bitmap");
  }
  
  public static void texImage2D(int paramInt1, int paramInt2, int paramInt3, Bitmap paramBitmap, int paramInt4, int paramInt5)
  {
    if (paramBitmap != null)
    {
      if (!paramBitmap.isRecycled())
      {
        if (native_texImage2D(paramInt1, paramInt2, paramInt3, paramBitmap, paramInt4, paramInt5) == 0) {
          return;
        }
        throw new IllegalArgumentException("invalid Bitmap format");
      }
      throw new IllegalArgumentException("bitmap is recycled");
    }
    throw new NullPointerException("texImage2D can't be used with a null Bitmap");
  }
  
  public static void texImage2D(int paramInt1, int paramInt2, Bitmap paramBitmap, int paramInt3)
  {
    if (paramBitmap != null)
    {
      if (!paramBitmap.isRecycled())
      {
        if (native_texImage2D(paramInt1, paramInt2, -1, paramBitmap, -1, paramInt3) == 0) {
          return;
        }
        throw new IllegalArgumentException("invalid Bitmap format");
      }
      throw new IllegalArgumentException("bitmap is recycled");
    }
    throw new NullPointerException("texImage2D can't be used with a null Bitmap");
  }
  
  public static void texSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Bitmap paramBitmap)
  {
    if (paramBitmap != null)
    {
      if (!paramBitmap.isRecycled())
      {
        if (native_texSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramBitmap, -1, getType(paramBitmap)) == 0) {
          return;
        }
        throw new IllegalArgumentException("invalid Bitmap format");
      }
      throw new IllegalArgumentException("bitmap is recycled");
    }
    throw new NullPointerException("texSubImage2D can't be used with a null Bitmap");
  }
  
  public static void texSubImage2D(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Bitmap paramBitmap, int paramInt5, int paramInt6)
  {
    if (paramBitmap != null)
    {
      if (!paramBitmap.isRecycled())
      {
        if (native_texSubImage2D(paramInt1, paramInt2, paramInt3, paramInt4, paramBitmap, paramInt5, paramInt6) == 0) {
          return;
        }
        throw new IllegalArgumentException("invalid Bitmap format");
      }
      throw new IllegalArgumentException("bitmap is recycled");
    }
    throw new NullPointerException("texSubImage2D can't be used with a null Bitmap");
  }
}
