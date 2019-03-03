package android.util;

import com.android.internal.util.Preconditions;

public final class Size
{
  private final int mHeight;
  private final int mWidth;
  
  public Size(int paramInt1, int paramInt2)
  {
    mWidth = paramInt1;
    mHeight = paramInt2;
  }
  
  private static NumberFormatException invalidSize(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid Size: \"");
    localStringBuilder.append(paramString);
    localStringBuilder.append("\"");
    throw new NumberFormatException(localStringBuilder.toString());
  }
  
  public static Size parseSize(String paramString)
    throws NumberFormatException
  {
    Preconditions.checkNotNull(paramString, "string must not be null");
    int i = paramString.indexOf('*');
    int j = i;
    if (i < 0) {
      j = paramString.indexOf('x');
    }
    if (j >= 0) {
      try
      {
        Size localSize = new Size(Integer.parseInt(paramString.substring(0, j)), Integer.parseInt(paramString.substring(j + 1)));
        return localSize;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw invalidSize(paramString);
      }
    }
    throw invalidSize(paramString);
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
    if ((paramObject instanceof Size))
    {
      paramObject = (Size)paramObject;
      boolean bool2 = bool1;
      if (mWidth == mWidth)
      {
        bool2 = bool1;
        if (mHeight == mHeight) {
          bool2 = true;
        }
      }
      return bool2;
    }
    return false;
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
    return mHeight ^ (mWidth << 16 | mWidth >>> 16);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mWidth);
    localStringBuilder.append("x");
    localStringBuilder.append(mHeight);
    return localStringBuilder.toString();
  }
}
