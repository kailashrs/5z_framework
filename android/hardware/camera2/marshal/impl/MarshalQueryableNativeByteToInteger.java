package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableNativeByteToInteger
  implements MarshalQueryable<Integer>
{
  private static final int UINT8_MASK = 255;
  
  public MarshalQueryableNativeByteToInteger() {}
  
  public Marshaler<Integer> createMarshaler(TypeReference<Integer> paramTypeReference, int paramInt)
  {
    return new MarshalerNativeByteToInteger(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<Integer> paramTypeReference, int paramInt)
  {
    boolean bool;
    if (((Integer.class.equals(paramTypeReference.getType())) || (Integer.TYPE.equals(paramTypeReference.getType()))) && (paramInt == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerNativeByteToInteger
    extends Marshaler<Integer>
  {
    protected MarshalerNativeByteToInteger(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 1;
    }
    
    public void marshal(Integer paramInteger, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put((byte)paramInteger.intValue());
    }
    
    public Integer unmarshal(ByteBuffer paramByteBuffer)
    {
      return Integer.valueOf(paramByteBuffer.get() & 0xFF);
    }
  }
}
