package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalHelpers;
import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class MarshalQueryableEnum<T extends Enum<T>>
  implements MarshalQueryable<T>
{
  private static final boolean DEBUG = false;
  private static final String TAG = MarshalQueryableEnum.class.getSimpleName();
  private static final int UINT8_MASK = 255;
  private static final int UINT8_MAX = 255;
  private static final int UINT8_MIN = 0;
  private static final HashMap<Class<? extends Enum>, int[]> sEnumValues = new HashMap();
  
  public MarshalQueryableEnum() {}
  
  private static <T extends Enum<T>> T getEnumFromValue(Class<T> paramClass, int paramInt)
  {
    int[] arrayOfInt = (int[])sEnumValues.get(paramClass);
    boolean bool = false;
    if (arrayOfInt != null)
    {
      int i = -1;
      for (int j = 0;; j++)
      {
        k = i;
        if (j >= arrayOfInt.length) {
          break;
        }
        if (arrayOfInt[j] == paramInt)
        {
          k = j;
          break;
        }
      }
    }
    int k = paramInt;
    Enum[] arrayOfEnum = (Enum[])paramClass.getEnumConstants();
    if ((k >= 0) && (k < arrayOfEnum.length)) {
      return arrayOfEnum[k];
    }
    if (arrayOfInt != null) {
      bool = true;
    }
    throw new IllegalArgumentException(String.format("Argument 'value' (%d) was not a valid enum value for type %s (registered? %b)", new Object[] { Integer.valueOf(paramInt), paramClass, Boolean.valueOf(bool) }));
  }
  
  private static <T extends Enum<T>> int getEnumValue(T paramT)
  {
    int[] arrayOfInt = (int[])sEnumValues.get(paramT.getClass());
    int i = paramT.ordinal();
    if (arrayOfInt != null) {
      return arrayOfInt[i];
    }
    return i;
  }
  
  public static <T extends Enum<T>> void registerEnumValues(Class<T> paramClass, int[] paramArrayOfInt)
  {
    if (((Enum[])paramClass.getEnumConstants()).length == paramArrayOfInt.length)
    {
      sEnumValues.put(paramClass, paramArrayOfInt);
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Expected values array to be the same size as the enumTypes values ");
    localStringBuilder.append(paramArrayOfInt.length);
    localStringBuilder.append(" for type ");
    localStringBuilder.append(paramClass);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public Marshaler<T> createMarshaler(TypeReference<T> paramTypeReference, int paramInt)
  {
    return new MarshalerEnum(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<T> paramTypeReference, int paramInt)
  {
    if (((paramInt == 1) || (paramInt == 0)) && ((paramTypeReference.getType() instanceof Class)))
    {
      paramTypeReference = (Class)paramTypeReference.getType();
      if (paramTypeReference.isEnum()) {
        try
        {
          paramTypeReference.getDeclaredConstructor(new Class[] { String.class, Integer.TYPE });
          return true;
        }
        catch (SecurityException localSecurityException)
        {
          String str1 = TAG;
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Can't marshal class ");
          localStringBuilder.append(paramTypeReference);
          localStringBuilder.append("; not accessible");
          Log.e(str1, localStringBuilder.toString());
        }
        catch (NoSuchMethodException localNoSuchMethodException)
        {
          String str2 = TAG;
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Can't marshal class ");
          localStringBuilder.append(paramTypeReference);
          localStringBuilder.append("; no default constructor");
          Log.e(str2, localStringBuilder.toString());
        }
      }
    }
    return false;
  }
  
  private class MarshalerEnum
    extends Marshaler<T>
  {
    private final Class<T> mClass;
    
    protected MarshalerEnum(int paramInt)
    {
      super(paramInt, i);
      mClass = paramInt.getRawType();
    }
    
    public int getNativeSize()
    {
      return MarshalHelpers.getPrimitiveTypeSize(mNativeType);
    }
    
    public void marshal(T paramT, ByteBuffer paramByteBuffer)
    {
      int i = MarshalQueryableEnum.getEnumValue(paramT);
      if (mNativeType == 1)
      {
        paramByteBuffer.putInt(i);
      }
      else
      {
        if (mNativeType != 0) {
          break label72;
        }
        if ((i < 0) || (i > 255)) {
          break label48;
        }
        paramByteBuffer.put((byte)i);
      }
      return;
      label48:
      throw new UnsupportedOperationException(String.format("Enum value %x too large to fit into unsigned byte", new Object[] { Integer.valueOf(i) }));
      label72:
      throw new AssertionError();
    }
    
    public T unmarshal(ByteBuffer paramByteBuffer)
    {
      int i;
      switch (mNativeType)
      {
      default: 
        throw new AssertionError("Unexpected native type; impossible since its not supported");
      case 1: 
        i = paramByteBuffer.getInt();
        break;
      case 0: 
        i = paramByteBuffer.get() & 0xFF;
      }
      return MarshalQueryableEnum.getEnumFromValue(mClass, i);
    }
  }
}
