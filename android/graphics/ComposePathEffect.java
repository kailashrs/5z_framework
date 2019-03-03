package android.graphics;

public class ComposePathEffect
  extends PathEffect
{
  public ComposePathEffect(PathEffect paramPathEffect1, PathEffect paramPathEffect2)
  {
    native_instance = nativeCreate(native_instance, native_instance);
  }
  
  private static native long nativeCreate(long paramLong1, long paramLong2);
}
