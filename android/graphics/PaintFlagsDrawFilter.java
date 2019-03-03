package android.graphics;

public class PaintFlagsDrawFilter
  extends DrawFilter
{
  public PaintFlagsDrawFilter(int paramInt1, int paramInt2)
  {
    mNativeInt = nativeConstructor(paramInt1, paramInt2);
  }
  
  private static native long nativeConstructor(int paramInt1, int paramInt2);
}
