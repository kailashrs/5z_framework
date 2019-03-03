package android.graphics;

public class EmbossMaskFilter
  extends MaskFilter
{
  @Deprecated
  public EmbossMaskFilter(float[] paramArrayOfFloat, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramArrayOfFloat.length >= 3)
    {
      native_instance = nativeConstructor(paramArrayOfFloat, paramFloat1, paramFloat2, paramFloat3);
      return;
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  private static native long nativeConstructor(float[] paramArrayOfFloat, float paramFloat1, float paramFloat2, float paramFloat3);
}
