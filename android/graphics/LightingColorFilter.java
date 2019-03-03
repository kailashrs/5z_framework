package android.graphics;

public class LightingColorFilter
  extends ColorFilter
{
  private int mAdd;
  private int mMul;
  
  public LightingColorFilter(int paramInt1, int paramInt2)
  {
    mMul = paramInt1;
    mAdd = paramInt2;
  }
  
  private static native long native_CreateLightingFilter(int paramInt1, int paramInt2);
  
  long createNativeInstance()
  {
    return native_CreateLightingFilter(mMul, mAdd);
  }
  
  public int getColorAdd()
  {
    return mAdd;
  }
  
  public int getColorMultiply()
  {
    return mMul;
  }
  
  public void setColorAdd(int paramInt)
  {
    if (mAdd != paramInt)
    {
      mAdd = paramInt;
      discardNativeInstance();
    }
  }
  
  public void setColorMultiply(int paramInt)
  {
    if (mMul != paramInt)
    {
      mMul = paramInt;
      discardNativeInstance();
    }
  }
}
