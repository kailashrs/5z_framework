package android.graphics;

public class ComposeShader
  extends Shader
{
  private long mNativeInstanceShaderA;
  private long mNativeInstanceShaderB;
  private int mPorterDuffMode;
  Shader mShaderA;
  Shader mShaderB;
  
  private ComposeShader(Shader paramShader1, Shader paramShader2, int paramInt)
  {
    if ((paramShader1 != null) && (paramShader2 != null))
    {
      mShaderA = paramShader1;
      mShaderB = paramShader2;
      mPorterDuffMode = paramInt;
      return;
    }
    throw new IllegalArgumentException("Shader parameters must not be null");
  }
  
  public ComposeShader(Shader paramShader1, Shader paramShader2, PorterDuff.Mode paramMode)
  {
    this(paramShader1, paramShader2, nativeInt);
  }
  
  public ComposeShader(Shader paramShader1, Shader paramShader2, Xfermode paramXfermode)
  {
    this(paramShader1, paramShader2, porterDuffMode);
  }
  
  private static native long nativeCreate(long paramLong1, long paramLong2, long paramLong3, int paramInt);
  
  protected Shader copy()
  {
    ComposeShader localComposeShader = new ComposeShader(mShaderA.copy(), mShaderB.copy(), mPorterDuffMode);
    copyLocalMatrix(localComposeShader);
    return localComposeShader;
  }
  
  long createNativeInstance(long paramLong)
  {
    mNativeInstanceShaderA = mShaderA.getNativeInstance();
    mNativeInstanceShaderB = mShaderB.getNativeInstance();
    return nativeCreate(paramLong, mShaderA.getNativeInstance(), mShaderB.getNativeInstance(), mPorterDuffMode);
  }
  
  protected void verifyNativeInstance()
  {
    if ((mShaderA.getNativeInstance() != mNativeInstanceShaderA) || (mShaderB.getNativeInstance() != mNativeInstanceShaderB)) {
      discardNativeInstance();
    }
  }
}
