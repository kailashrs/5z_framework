package android.graphics;

public class PorterDuffColorFilter
  extends ColorFilter
{
  private int mColor;
  private PorterDuff.Mode mMode;
  
  public PorterDuffColorFilter(int paramInt, PorterDuff.Mode paramMode)
  {
    mColor = paramInt;
    mMode = paramMode;
  }
  
  private static native long native_CreatePorterDuffFilter(int paramInt1, int paramInt2);
  
  long createNativeInstance()
  {
    return native_CreatePorterDuffFilter(mColor, mMode.nativeInt);
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (PorterDuffColorFilter)paramObject;
      if ((mColor != mColor) || (mMode.nativeInt != mMode.nativeInt)) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getColor()
  {
    return mColor;
  }
  
  public PorterDuff.Mode getMode()
  {
    return mMode;
  }
  
  public int hashCode()
  {
    return 31 * mMode.hashCode() + mColor;
  }
  
  public void setColor(int paramInt)
  {
    if (mColor != paramInt)
    {
      mColor = paramInt;
      discardNativeInstance();
    }
  }
  
  public void setMode(PorterDuff.Mode paramMode)
  {
    if (paramMode != null)
    {
      mMode = paramMode;
      discardNativeInstance();
      return;
    }
    throw new IllegalArgumentException("mode must be non-null");
  }
}
