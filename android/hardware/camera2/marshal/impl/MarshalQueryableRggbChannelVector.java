package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.RggbChannelVector;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableRggbChannelVector
  implements MarshalQueryable<RggbChannelVector>
{
  private static final int SIZE = 16;
  
  public MarshalQueryableRggbChannelVector() {}
  
  public Marshaler<RggbChannelVector> createMarshaler(TypeReference<RggbChannelVector> paramTypeReference, int paramInt)
  {
    return new MarshalerRggbChannelVector(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<RggbChannelVector> paramTypeReference, int paramInt)
  {
    boolean bool;
    if ((paramInt == 2) && (RggbChannelVector.class.equals(paramTypeReference.getType()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerRggbChannelVector
    extends Marshaler<RggbChannelVector>
  {
    protected MarshalerRggbChannelVector(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 16;
    }
    
    public void marshal(RggbChannelVector paramRggbChannelVector, ByteBuffer paramByteBuffer)
    {
      for (int i = 0; i < 4; i++) {
        paramByteBuffer.putFloat(paramRggbChannelVector.getComponent(i));
      }
    }
    
    public RggbChannelVector unmarshal(ByteBuffer paramByteBuffer)
    {
      return new RggbChannelVector(paramByteBuffer.getFloat(), paramByteBuffer.getFloat(), paramByteBuffer.getFloat(), paramByteBuffer.getFloat());
    }
  }
}
