package android.graphics;

public class CornerPathEffect
  extends PathEffect
{
  public CornerPathEffect(float paramFloat)
  {
    native_instance = nativeCreate(paramFloat);
  }
  
  private static native long nativeCreate(float paramFloat);
}
