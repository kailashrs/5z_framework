package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

public class MarshalQueryablePair<T1, T2>
  implements MarshalQueryable<Pair<T1, T2>>
{
  public MarshalQueryablePair() {}
  
  public Marshaler<Pair<T1, T2>> createMarshaler(TypeReference<Pair<T1, T2>> paramTypeReference, int paramInt)
  {
    return new MarshalerPair(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<Pair<T1, T2>> paramTypeReference, int paramInt)
  {
    return Pair.class.equals(paramTypeReference.getRawType());
  }
  
  private class MarshalerPair
    extends Marshaler<Pair<T1, T2>>
  {
    private final Class<? super Pair<T1, T2>> mClass;
    private final Constructor<Pair<T1, T2>> mConstructor;
    private final Marshaler<T1> mNestedTypeMarshalerFirst;
    private final Marshaler<T2> mNestedTypeMarshalerSecond;
    
    /* Error */
    protected MarshalerPair(int paramInt)
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: putfield 29	android/hardware/camera2/marshal/impl/MarshalQueryablePair$MarshalerPair:this$0	Landroid/hardware/camera2/marshal/impl/MarshalQueryablePair;
      //   5: aload_0
      //   6: aload_1
      //   7: aload_2
      //   8: iload_3
      //   9: invokespecial 32	android/hardware/camera2/marshal/Marshaler:<init>	(Landroid/hardware/camera2/marshal/MarshalQueryable;Landroid/hardware/camera2/utils/TypeReference;I)V
      //   12: aload_0
      //   13: aload_2
      //   14: invokevirtual 38	android/hardware/camera2/utils/TypeReference:getRawType	()Ljava/lang/Class;
      //   17: putfield 40	android/hardware/camera2/marshal/impl/MarshalQueryablePair$MarshalerPair:mClass	Ljava/lang/Class;
      //   20: aload_2
      //   21: invokevirtual 44	android/hardware/camera2/utils/TypeReference:getType	()Ljava/lang/reflect/Type;
      //   24: checkcast 46	java/lang/reflect/ParameterizedType
      //   27: astore_2
      //   28: aload_2
      //   29: invokeinterface 50 1 0
      //   34: iconst_0
      //   35: aaload
      //   36: astore_1
      //   37: aload_0
      //   38: aload_1
      //   39: invokestatic 54	android/hardware/camera2/utils/TypeReference:createSpecializedTypeReference	(Ljava/lang/reflect/Type;)Landroid/hardware/camera2/utils/TypeReference;
      //   42: aload_0
      //   43: getfield 58	android/hardware/camera2/marshal/impl/MarshalQueryablePair$MarshalerPair:mNativeType	I
      //   46: invokestatic 64	android/hardware/camera2/marshal/MarshalRegistry:getMarshaler	(Landroid/hardware/camera2/utils/TypeReference;I)Landroid/hardware/camera2/marshal/Marshaler;
      //   49: putfield 66	android/hardware/camera2/marshal/impl/MarshalQueryablePair$MarshalerPair:mNestedTypeMarshalerFirst	Landroid/hardware/camera2/marshal/Marshaler;
      //   52: aload_2
      //   53: invokeinterface 50 1 0
      //   58: iconst_1
      //   59: aaload
      //   60: astore_1
      //   61: aload_0
      //   62: aload_1
      //   63: invokestatic 54	android/hardware/camera2/utils/TypeReference:createSpecializedTypeReference	(Ljava/lang/reflect/Type;)Landroid/hardware/camera2/utils/TypeReference;
      //   66: aload_0
      //   67: getfield 58	android/hardware/camera2/marshal/impl/MarshalQueryablePair$MarshalerPair:mNativeType	I
      //   70: invokestatic 64	android/hardware/camera2/marshal/MarshalRegistry:getMarshaler	(Landroid/hardware/camera2/utils/TypeReference;I)Landroid/hardware/camera2/marshal/Marshaler;
      //   73: putfield 68	android/hardware/camera2/marshal/impl/MarshalQueryablePair$MarshalerPair:mNestedTypeMarshalerSecond	Landroid/hardware/camera2/marshal/Marshaler;
      //   76: aload_0
      //   77: aload_0
      //   78: getfield 40	android/hardware/camera2/marshal/impl/MarshalQueryablePair$MarshalerPair:mClass	Ljava/lang/Class;
      //   81: iconst_2
      //   82: anewarray 70	java/lang/Class
      //   85: dup
      //   86: iconst_0
      //   87: ldc 72
      //   89: aastore
      //   90: dup
      //   91: iconst_1
      //   92: ldc 72
      //   94: aastore
      //   95: invokevirtual 76	java/lang/Class:getConstructor	([Ljava/lang/Class;)Ljava/lang/reflect/Constructor;
      //   98: putfield 78	android/hardware/camera2/marshal/impl/MarshalQueryablePair$MarshalerPair:mConstructor	Ljava/lang/reflect/Constructor;
      //   101: return
      //   102: astore_1
      //   103: new 80	java/lang/AssertionError
      //   106: dup
      //   107: aload_1
      //   108: invokespecial 83	java/lang/AssertionError:<init>	(Ljava/lang/Object;)V
      //   111: athrow
      //   112: astore_1
      //   113: new 80	java/lang/AssertionError
      //   116: dup
      //   117: ldc 85
      //   119: aload_1
      //   120: invokespecial 88	java/lang/AssertionError:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   123: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	124	0	this	MarshalerPair
      //   0	124	1	this$1	MarshalQueryablePair
      //   0	124	2	paramInt	int
      //   8	1	3	i	int
      // Exception table:
      //   from	to	target	type
      //   76	101	102	java/lang/NoSuchMethodException
      //   20	28	112	java/lang/ClassCastException
    }
    
    public int calculateMarshalSize(Pair<T1, T2> paramPair)
    {
      int i = getNativeSize();
      if (i != NATIVE_SIZE_DYNAMIC) {
        return i;
      }
      return mNestedTypeMarshalerFirst.calculateMarshalSize(first) + mNestedTypeMarshalerSecond.calculateMarshalSize(second);
    }
    
    public int getNativeSize()
    {
      int i = mNestedTypeMarshalerFirst.getNativeSize();
      int j = mNestedTypeMarshalerSecond.getNativeSize();
      if ((i != NATIVE_SIZE_DYNAMIC) && (j != NATIVE_SIZE_DYNAMIC)) {
        return i + j;
      }
      return NATIVE_SIZE_DYNAMIC;
    }
    
    public void marshal(Pair<T1, T2> paramPair, ByteBuffer paramByteBuffer)
    {
      if (first != null)
      {
        if (second != null)
        {
          mNestedTypeMarshalerFirst.marshal(first, paramByteBuffer);
          mNestedTypeMarshalerSecond.marshal(second, paramByteBuffer);
          return;
        }
        throw new UnsupportedOperationException("Pair#second must not be null");
      }
      throw new UnsupportedOperationException("Pair#first must not be null");
    }
    
    public Pair<T1, T2> unmarshal(ByteBuffer paramByteBuffer)
    {
      Object localObject = mNestedTypeMarshalerFirst.unmarshal(paramByteBuffer);
      paramByteBuffer = mNestedTypeMarshalerSecond.unmarshal(paramByteBuffer);
      try
      {
        paramByteBuffer = (Pair)mConstructor.newInstance(new Object[] { localObject, paramByteBuffer });
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
