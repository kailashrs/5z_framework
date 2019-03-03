package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import android.util.Size;
import java.nio.ByteBuffer;

public class MarshalQueryableSize
  implements MarshalQueryable<Size>
{
  private static final int SIZE = 8;
  
  public MarshalQueryableSize() {}
  
  public Marshaler<Size> createMarshaler(TypeReference<Size> paramTypeReference, int paramInt)
  {
    return new MarshalerSize(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<Size> paramTypeReference, int paramInt)
  {
    boolean bool = true;
    if ((paramInt != 1) || (!Size.class.equals(paramTypeReference.getType()))) {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerSize
    extends Marshaler<Size>
  {
    protected MarshalerSize(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 8;
    }
    
    public void marshal(Size paramSize, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putInt(paramSize.getWidth());
      paramByteBuffer.putInt(paramSize.getHeight());
    }
    
    public Size unmarshal(ByteBuffer paramByteBuffer)
    {
      return new Size(paramByteBuffer.getInt(), paramByteBuffer.getInt());
    }
  }
}
