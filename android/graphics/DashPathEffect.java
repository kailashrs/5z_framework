package android.graphics;

public class DashPathEffect
  extends PathEffect
{
  public DashPathEffect(float[] paramArrayOfFloat, float paramFloat)
  {
    if (paramArrayOfFloat.length >= 2)
    {
      native_instance = nativeCreate(paramArrayOfFloat, paramFloat);
      return;
    }
    throw new ArrayIndexOutOfBoundsException();
  }
  
  private static native long nativeCreate(float[] paramArrayOfFloat, float paramFloat);
}
