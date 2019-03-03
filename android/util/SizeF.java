package android.util;

import com.android.internal.util.Preconditions;

public final class SizeF
{
  private final float mHeight;
  private final float mWidth;
  
  public SizeF(float paramFloat1, float paramFloat2)
  {
    mWidth = Preconditions.checkArgumentFinite(paramFloat1, "width");
    mHeight = Preconditions.checkArgumentFinite(paramFloat2, "height");
  }
  
  private static NumberFormatException invalidSizeF(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid SizeF: \"");
    localStringBuilder.append(paramString);
    localStringBuilder.append("\"");
    throw new NumberFormatException(localStringBuilder.toString());
  }
  
  public static SizeF parseSizeF(String paramString)
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
        SizeF localSizeF = new SizeF(Float.parseFloat(paramString.substring(0, j)), Float.parseFloat(paramString.substring(j + 1)));
        return localSizeF;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        throw invalidSizeF(paramString);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        throw invalidSizeF(paramString);
      }
    }
    throw invalidSizeF(paramString);
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
    if ((paramObject instanceof SizeF))
    {
      paramObject = (SizeF)paramObject;
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
  
  public float getHeight()
  {
    return mHeight;
  }
  
  public float getWidth()
  {
    return mWidth;
  }
  
  public int hashCode()
  {
    return Float.floatToIntBits(mWidth) ^ Float.floatToIntBits(mHeight);
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
