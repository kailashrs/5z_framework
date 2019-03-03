package android.graphics;

public class SumPathEffect
  extends PathEffect
{
  public SumPathEffect(PathEffect paramPathEffect1, PathEffect paramPathEffect2)
  {
    native_instance = nativeCreate(native_instance, native_instance);
  }
  
  private static native long nativeCreate(long paramLong1, long paramLong2);
}
