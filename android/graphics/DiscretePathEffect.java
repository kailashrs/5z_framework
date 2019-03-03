package android.graphics;

public class DiscretePathEffect
  extends PathEffect
{
  public DiscretePathEffect(float paramFloat1, float paramFloat2)
  {
    native_instance = nativeCreate(paramFloat1, paramFloat2);
  }
  
  private static native long nativeCreate(float paramFloat1, float paramFloat2);
}
