package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableBoolean
  implements MarshalQueryable<Boolean>
{
  public MarshalQueryableBoolean() {}
  
  public Marshaler<Boolean> createMarshaler(TypeReference<Boolean> paramTypeReference, int paramInt)
  {
    return new MarshalerBoolean(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<Boolean> paramTypeReference, int paramInt)
  {
    boolean bool;
    if (((Boolean.class.equals(paramTypeReference.getType())) || (Boolean.TYPE.equals(paramTypeReference.getType()))) && (paramInt == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerBoolean
    extends Marshaler<Boolean>
  {
    protected MarshalerBoolean(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 1;
    }
    
    public void marshal(Boolean paramBoolean, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put((byte)paramBoolean.booleanValue());
    }
    
    public Boolean unmarshal(ByteBuffer paramByteBuffer)
    {
      boolean bool;
      if (paramByteBuffer.get() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return Boolean.valueOf(bool);
    }
  }
}
