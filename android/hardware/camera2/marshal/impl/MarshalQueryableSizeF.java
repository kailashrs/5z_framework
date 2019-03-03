package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.SizeF;
import java.nio.ByteBuffer;

public class MarshalQueryableSizeF
  implements MarshalQueryable<SizeF>
{
  private static final int SIZE = 8;
  
  public MarshalQueryableSizeF() {}
  
  public Marshaler<SizeF> createMarshaler(TypeReference<SizeF> paramTypeReference, int paramInt)
  {
    return new MarshalerSizeF(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<SizeF> paramTypeReference, int paramInt)
  {
    boolean bool;
    if ((paramInt == 2) && (SizeF.class.equals(paramTypeReference.getType()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerSizeF
    extends Marshaler<SizeF>
  {
    protected MarshalerSizeF(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 8;
    }
    
    public void marshal(SizeF paramSizeF, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putFloat(paramSizeF.getWidth());
      paramByteBuffer.putFloat(paramSizeF.getHeight());
    }
    
    public SizeF unmarshal(ByteBuffer paramByteBuffer)
    {
      return new SizeF(paramByteBuffer.getFloat(), paramByteBuffer.getFloat());
    }
  }
}
