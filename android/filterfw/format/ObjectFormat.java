package android.filterfw.format;

import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.NativeBuffer;

public class ObjectFormat
{
  public ObjectFormat() {}
  
  private static int bytesPerSampleForClass(Class paramClass, int paramInt)
  {
    if (paramInt == 2)
    {
      if (NativeBuffer.class.isAssignableFrom(paramClass)) {
        try
        {
          paramInt = ((NativeBuffer)paramClass.newInstance()).getElementSize();
          return paramInt;
        }
        catch (Exception localException)
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Could not determine the size of an element in a native object-based frame of type ");
          localStringBuilder.append(paramClass);
          localStringBuilder.append("! Perhaps it is missing a default constructor?");
          throw new RuntimeException(localStringBuilder.toString());
        }
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Native object-based formats must be of a NativeBuffer subclass! (Received class: ");
      localStringBuilder.append(paramClass);
      localStringBuilder.append(").");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    return 1;
  }
  
  public static MutableFrameFormat fromClass(Class paramClass, int paramInt)
  {
    return fromClass(paramClass, 0, paramInt);
  }
  
  public static MutableFrameFormat fromClass(Class paramClass, int paramInt1, int paramInt2)
  {
    MutableFrameFormat localMutableFrameFormat = new MutableFrameFormat(8, paramInt2);
    localMutableFrameFormat.setObjectClass(getBoxedClass(paramClass));
    if (paramInt1 != 0) {
      localMutableFrameFormat.setDimensions(paramInt1);
    }
    localMutableFrameFormat.setBytesPerSample(bytesPerSampleForClass(paramClass, paramInt2));
    return localMutableFrameFormat;
  }
  
  public static MutableFrameFormat fromObject(Object paramObject, int paramInt)
  {
    if (paramObject == null) {
      paramObject = new MutableFrameFormat(8, paramInt);
    } else {
      paramObject = fromClass(paramObject.getClass(), 0, paramInt);
    }
    return paramObject;
  }
  
  public static MutableFrameFormat fromObject(Object paramObject, int paramInt1, int paramInt2)
  {
    if (paramObject == null) {
      paramObject = new MutableFrameFormat(8, paramInt2);
    } else {
      paramObject = fromClass(paramObject.getClass(), paramInt1, paramInt2);
    }
    return paramObject;
  }
  
  private static Class getBoxedClass(Class paramClass)
  {
    if (paramClass.isPrimitive())
    {
      if (paramClass == Boolean.TYPE) {
        return Boolean.class;
      }
      if (paramClass == Byte.TYPE) {
        return Byte.class;
      }
      if (paramClass == Character.TYPE) {
        return Character.class;
      }
      if (paramClass == Short.TYPE) {
        return Short.class;
      }
      if (paramClass == Integer.TYPE) {
        return Integer.class;
      }
      if (paramClass == Long.TYPE) {
        return Long.class;
      }
      if (paramClass == Float.TYPE) {
        return Float.class;
      }
      if (paramClass == Double.TYPE) {
        return Double.class;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown primitive type: ");
      localStringBuilder.append(paramClass.getSimpleName());
      localStringBuilder.append("!");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    return paramClass;
  }
}
