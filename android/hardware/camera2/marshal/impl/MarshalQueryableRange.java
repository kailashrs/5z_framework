package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Range;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public class MarshalQueryableRange<T extends Comparable<? super T>>
  implements MarshalQueryable<Range<T>>
{
  private static final int RANGE_COUNT = 2;
  
  public MarshalQueryableRange() {}
  
  public Marshaler<Range<T>> createMarshaler(TypeReference<Range<T>> paramTypeReference, int paramInt)
  {
    return new MarshalerRange(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<Range<T>> paramTypeReference, int paramInt)
  {
    return Range.class.equals(paramTypeReference.getRawType());
  }
  
  private class MarshalerRange
    extends Marshaler<Range<T>>
  {
    private final Class<? super Range<T>> mClass;
    private final Constructor<Range<T>> mConstructor;
    private final Marshaler<T> mNestedTypeMarshaler;
    
    /* Error */
    protected MarshalerRange(int paramInt)
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: putfield 27	android/hardware/camera2/marshal/impl/MarshalQueryableRange$MarshalerRange:this$0	Landroid/hardware/camera2/marshal/impl/MarshalQueryableRange;
      //   5: aload_0
      //   6: aload_1
      //   7: aload_2
      //   8: iload_3
      //   9: invokespecial 30	android/hardware/camera2/marshal/Marshaler:<init>	(Landroid/hardware/camera2/marshal/MarshalQueryable;Landroid/hardware/camera2/utils/TypeReference;I)V
      //   12: aload_0
      //   13: aload_2
      //   14: invokevirtual 36	android/hardware/camera2/utils/TypeReference:getRawType	()Ljava/lang/Class;
      //   17: putfield 38	android/hardware/camera2/marshal/impl/MarshalQueryableRange$MarshalerRange:mClass	Ljava/lang/Class;
      //   20: aload_2
      //   21: invokevirtual 42	android/hardware/camera2/utils/TypeReference:getType	()Ljava/lang/reflect/Type;
      //   24: checkcast 44	java/lang/reflect/ParameterizedType
      //   27: astore_1
      //   28: aload_1
      //   29: invokeinterface 48 1 0
      //   34: iconst_0
      //   35: aaload
      //   36: astore_1
      //   37: aload_0
      //   38: aload_1
      //   39: invokestatic 52	android/hardware/camera2/utils/TypeReference:createSpecializedTypeReference	(Ljava/lang/reflect/Type;)Landroid/hardware/camera2/utils/TypeReference;
      //   42: aload_0
      //   43: getfield 56	android/hardware/camera2/marshal/impl/MarshalQueryableRange$MarshalerRange:mNativeType	I
      //   46: invokestatic 62	android/hardware/camera2/marshal/MarshalRegistry:getMarshaler	(Landroid/hardware/camera2/utils/TypeReference;I)Landroid/hardware/camera2/marshal/Marshaler;
      //   49: putfield 64	android/hardware/camera2/marshal/impl/MarshalQueryableRange$MarshalerRange:mNestedTypeMarshaler	Landroid/hardware/camera2/marshal/Marshaler;
      //   52: aload_0
      //   53: aload_0
      //   54: getfield 38	android/hardware/camera2/marshal/impl/MarshalQueryableRange$MarshalerRange:mClass	Ljava/lang/Class;
      //   57: iconst_2
      //   58: anewarray 66	java/lang/Class
      //   61: dup
      //   62: iconst_0
      //   63: ldc 68
      //   65: aastore
      //   66: dup
      //   67: iconst_1
      //   68: ldc 68
      //   70: aastore
      //   71: invokevirtual 72	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
      //   74: putfield 74	android/hardware/camera2/marshal/impl/MarshalQueryableRange$MarshalerRange:mConstructor	Ljava/lang/reflect/Constructor;
      //   77: return
      //   78: astore_1
      //   79: new 76	java/lang/AssertionError
      //   82: dup
      //   83: aload_1
      //   84: invokespecial 79	java/lang/AssertionError:<init>	(Ljava/lang/Object;)V
      //   87: athrow
      //   88: astore_1
      //   89: new 76	java/lang/AssertionError
      //   92: dup
      //   93: ldc 81
      //   95: aload_1
      //   96: invokespecial 84	java/lang/AssertionError:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   99: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	100	0	this	MarshalerRange
      //   0	100	1	this$1	MarshalQueryableRange
      //   0	100	2	paramInt	int
      //   8	1	3	i	int
      // Exception table:
      //   from	to	target	type
      //   52	77	78	java/lang/NoSuchMethodException
      //   20	28	88	java/lang/ClassCastException
    }
    
    public int calculateMarshalSize(Range<T> paramRange)
    {
      int i = getNativeSize();
      if (i != NATIVE_SIZE_DYNAMIC) {
        return i;
      }
      return mNestedTypeMarshaler.calculateMarshalSize(paramRange.getLower()) + mNestedTypeMarshaler.calculateMarshalSize(paramRange.getUpper());
    }
    
    public int getNativeSize()
    {
      int i = mNestedTypeMarshaler.getNativeSize();
      if (i != NATIVE_SIZE_DYNAMIC) {
        return i * 2;
      }
      return NATIVE_SIZE_DYNAMIC;
    }
    
    public void marshal(Range<T> paramRange, ByteBuffer paramByteBuffer)
    {
      mNestedTypeMarshaler.marshal(paramRange.getLower(), paramByteBuffer);
      mNestedTypeMarshaler.marshal(paramRange.getUpper(), paramByteBuffer);
    }
    
    public Range<T> unmarshal(ByteBuffer paramByteBuffer)
    {
      Comparable localComparable = (Comparable)mNestedTypeMarshaler.unmarshal(paramByteBuffer);
      paramByteBuffer = (Comparable)mNestedTypeMarshaler.unmarshal(paramByteBuffer);
      try
      {
        paramByteBuffer = (Range)mConstructor.newInstance(new Object[] { localComparable, paramByteBuffer });
        return paramByteBuffer;
      }
      catch (InvocationTargetException paramByteBuffer)
      {
        throw new AssertionError(paramByteBuffer);
      }
      catch (IllegalArgumentException paramByteBuffer)
      {
        throw new AssertionError(paramByteBuffer);
      }
      catch (IllegalAccessException paramByteBuffer)
      {
        throw new AssertionError(paramByteBuffer);
      }
      catch (InstantiationException paramByteBuffer)
      {
        throw new AssertionError(paramByteBuffer);
      }
    }
  }
}
