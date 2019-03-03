package android.hardware.camera2.marshal.impl;

import android.graphics.Rect;
import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableRect
  implements MarshalQueryable<Rect>
{
  private static final int SIZE = 16;
  
  public MarshalQueryableRect() {}
  
  public Marshaler<Rect> createMarshaler(TypeReference<Rect> paramTypeReference, int paramInt)
  {
    return new MarshalerRect(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<Rect> paramTypeReference, int paramInt)
  {
    boolean bool = true;
    if ((paramInt != 1) || (!Rect.class.equals(paramTypeReference.getType()))) {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerRect
    extends Marshaler<Rect>
  {
    protected MarshalerRect(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 16;
    }
    
    public void marshal(Rect paramRect, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putInt(left);
      paramByteBuffer.putInt(top);
      paramByteBuffer.putInt(paramRect.width());
      paramByteBuffer.putInt(paramRect.height());
    }
    
    public Rect unmarshal(ByteBuffer paramByteBuffer)
    {
      int i = paramByteBuffer.getInt();
      int j = paramByteBuffer.getInt();
      return new Rect(i, j, i + paramByteBuffer.getInt(), j + paramByteBuffer.getInt());
    }
  }
}
