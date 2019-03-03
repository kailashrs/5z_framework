package android.hardware.camera2.marshal;

import android.util.Rational;
import com.android.internal.util.Preconditions;

public final class MarshalHelpers
{
  public static final int SIZEOF_BYTE = 1;
  public static final int SIZEOF_DOUBLE = 8;
  public static final int SIZEOF_FLOAT = 4;
  public static final int SIZEOF_INT32 = 4;
  public static final int SIZEOF_INT64 = 8;
  public static final int SIZEOF_RATIONAL = 8;
  
  private MarshalHelpers()
  {
    throw new AssertionError();
  }
  
  public static int checkNativeType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown nativeType ");
      localStringBuilder.append(paramInt);
      throw new UnsupportedOperationException(localStringBuilder.toString());
    }
    return paramInt;
  }
  
  public static int checkNativeTypeEquals(int paramInt1, int paramInt2)
  {
    if (paramInt1 == paramInt2) {
      return paramInt2;
    }
    throw new UnsupportedOperationException(String.format("Expected native type %d, but got %d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) }));
  }
  
  public static <T> Class<T> checkPrimitiveClass(Class<T> paramClass)
  {
    Preconditions.checkNotNull(paramClass, "klass must not be null");
    if (isPrimitiveClass(paramClass)) {
      return paramClass;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unsupported class '");
    localStringBuilder.append(paramClass);
    localStringBuilder.append("'; expected a metadata primitive class");
    throw new UnsupportedOperationException(localStringBuilder.toString());
  }
  
  public static int getPrimitiveTypeSize(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown type, can't get size for ");
      localStringBuilder.append(paramInt);
      throw new UnsupportedOperationException(localStringBuilder.toString());
    case 5: 
      return 8;
    case 4: 
      return 8;
    case 3: 
      return 8;
    case 2: 
      return 4;
    case 1: 
      return 4;
    }
    return 1;
  }
  
  public static <T> boolean isPrimitiveClass(Class<T> paramClass)
  {
    if (paramClass == null) {
      return false;
    }
    if ((paramClass != Byte.TYPE) && (paramClass != Byte.class))
    {
      if ((paramClass != Integer.TYPE) && (paramClass != Integer.class))
      {
        if ((paramClass != Float.TYPE) && (paramClass != Float.class))
        {
          if ((paramClass != Long.TYPE) && (paramClass != Long.class))
          {
            if ((paramClass != Double.TYPE) && (paramClass != Double.class)) {
              return paramClass == Rational.class;
            }
            return true;
          }
          return true;
        }
        return true;
      }
      return true;
    }
    return true;
  }
  
  public static String toStringNativeType(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UNKNOWN(");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(")");
      return localStringBuilder.toString();
    case 5: 
      return "TYPE_RATIONAL";
    case 4: 
      return "TYPE_DOUBLE";
    case 3: 
      return "TYPE_INT64";
    case 2: 
      return "TYPE_FLOAT";
    case 1: 
      return "TYPE_INT32";
    }
    return "TYPE_BYTE";
  }
  
  public static <T> Class<T> wrapClassIfPrimitive(Class<T> paramClass)
  {
    if (paramClass == Byte.TYPE) {
      return Byte.class;
    }
    if (paramClass == Integer.TYPE) {
      return Integer.class;
    }
    if (paramClass == Float.TYPE) {
      return Float.class;
    }
    if (paramClass == Long.TYPE) {
      return Long.class;
    }
    if (paramClass == Double.TYPE) {
      return Double.class;
    }
    return paramClass;
  }
}
