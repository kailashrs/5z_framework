package android.graphics;

import com.android.internal.util.VirtualRefBasePtr;

public final class CanvasProperty<T>
{
  private VirtualRefBasePtr mProperty;
  
  private CanvasProperty(long paramLong)
  {
    mProperty = new VirtualRefBasePtr(paramLong);
  }
  
  public static CanvasProperty<Float> createFloat(float paramFloat)
  {
    return new CanvasProperty(nCreateFloat(paramFloat));
  }
  
  public static CanvasProperty<Paint> createPaint(Paint paramPaint)
  {
    return new CanvasProperty(nCreatePaint(paramPaint.getNativeInstance()));
  }
  
  private static native long nCreateFloat(float paramFloat);
  
  private static native long nCreatePaint(long paramLong);
  
  public long getNativeContainer()
  {
    return mProperty.get();
  }
}
