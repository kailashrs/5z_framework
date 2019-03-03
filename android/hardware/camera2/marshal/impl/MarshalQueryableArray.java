package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.MarshalRegistry;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Log;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MarshalQueryableArray<T>
  implements MarshalQueryable<T>
{
  private static final boolean DEBUG = false;
  private static final String TAG = MarshalQueryableArray.class.getSimpleName();
  
  public MarshalQueryableArray() {}
  
  public Marshaler<T> createMarshaler(TypeReference<T> paramTypeReference, int paramInt)
  {
    return new MarshalerArray(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<T> paramTypeReference, int paramInt)
  {
    return paramTypeReference.getRawType().isArray();
  }
  
  private class MarshalerArray
    extends Marshaler<T>
  {
    private final Class<T> mClass;
    private final Class<?> mComponentClass;
    private final Marshaler<?> mComponentMarshaler;
    
    protected MarshalerArray(int paramInt)
    {
      super(paramInt, i);
      mClass = paramInt.getRawType();
      this$1 = paramInt.getComponentType();
      mComponentMarshaler = MarshalRegistry.getMarshaler(MarshalQueryableArray.this, mNativeType);
      mComponentClass = getRawType();
    }
    
    private <TElem> int calculateElementMarshalSize(Marshaler<TElem> paramMarshaler, Object paramObject, int paramInt)
    {
      return paramMarshaler.calculateMarshalSize(Array.get(paramObject, paramInt));
    }
    
    private Object copyListToArray(ArrayList<?> paramArrayList, Object paramObject)
    {
      return paramArrayList.toArray((Object[])paramObject);
    }
    
    private <TElem> void marshalArrayElement(Marshaler<TElem> paramMarshaler, ByteBuffer paramByteBuffer, Object paramObject, int paramInt)
    {
      paramMarshaler.marshal(Array.get(paramObject, paramInt), paramByteBuffer);
    }
    
    public int calculateMarshalSize(T paramT)
    {
      int i = mComponentMarshaler.getNativeSize();
      int j = Array.getLength(paramT);
      if (i != Marshaler.NATIVE_SIZE_DYNAMIC) {
        return i * j;
      }
      int k = 0;
      for (i = 0; i < j; i++) {
        k += calculateElementMarshalSize(mComponentMarshaler, paramT, i);
      }
      return k;
    }
    
    public int getNativeSize()
    {
      return NATIVE_SIZE_DYNAMIC;
    }
    
    public void marshal(T paramT, ByteBuffer paramByteBuffer)
    {
      int i = Array.getLength(paramT);
      for (int j = 0; j < i; j++) {
        marshalArrayElement(mComponentMarshaler, paramByteBuffer, paramT, j);
      }
    }
    
    public T unmarshal(ByteBuffer paramByteBuffer)
    {
      int i = mComponentMarshaler.getNativeSize();
      int j;
      Object localObject;
      if (i != Marshaler.NATIVE_SIZE_DYNAMIC)
      {
        j = paramByteBuffer.remaining();
        int k = j / i;
        if (j % i == 0)
        {
          localObject = Array.newInstance(mComponentClass, k);
          for (j = 0; j < k; j++) {
            Array.set(localObject, j, mComponentMarshaler.unmarshal(paramByteBuffer));
          }
        }
        else
        {
          paramByteBuffer = new StringBuilder();
          paramByteBuffer.append("Arrays for ");
          paramByteBuffer.append(mTypeReference);
          paramByteBuffer.append(" must be packed tighly into a multiple of ");
          paramByteBuffer.append(i);
          paramByteBuffer.append("; but there are ");
          paramByteBuffer.append(j % i);
          paramByteBuffer.append(" left over bytes");
          throw new UnsupportedOperationException(paramByteBuffer.toString());
        }
      }
      else
      {
        localObject = new ArrayList();
        while (paramByteBuffer.hasRemaining()) {
          ((ArrayList)localObject).add(mComponentMarshaler.unmarshal(paramByteBuffer));
        }
        j = ((ArrayList)localObject).size();
        localObject = copyListToArray((ArrayList)localObject, Array.newInstance(mComponentClass, j));
      }
      if (paramByteBuffer.remaining() != 0)
      {
        String str = MarshalQueryableArray.TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Trailing bytes (");
        localStringBuilder.append(paramByteBuffer.remaining());
        localStringBuilder.append(") left over after unpacking ");
        localStringBuilder.append(mClass);
        Log.e(str, localStringBuilder.toString());
      }
      return mClass.cast(localObject);
    }
  }
}
