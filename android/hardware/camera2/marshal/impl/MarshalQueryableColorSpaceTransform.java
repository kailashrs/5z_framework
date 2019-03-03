package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableColorSpaceTransform
  implements MarshalQueryable<ColorSpaceTransform>
{
  private static final int ELEMENTS_INT32 = 18;
  private static final int SIZE = 72;
  
  public MarshalQueryableColorSpaceTransform() {}
  
  public Marshaler<ColorSpaceTransform> createMarshaler(TypeReference<ColorSpaceTransform> paramTypeReference, int paramInt)
  {
    return new MarshalerColorSpaceTransform(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<ColorSpaceTransform> paramTypeReference, int paramInt)
  {
    boolean bool;
    if ((paramInt == 5) && (ColorSpaceTransform.class.equals(paramTypeReference.getType()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerColorSpaceTransform
    extends Marshaler<ColorSpaceTransform>
  {
    protected MarshalerColorSpaceTransform(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 72;
    }
    
    public void marshal(ColorSpaceTransform paramColorSpaceTransform, ByteBuffer paramByteBuffer)
    {
      int[] arrayOfInt = new int[18];
      int i = 0;
      paramColorSpaceTransform.copyElements(arrayOfInt, 0);
      while (i < 18)
      {
        paramByteBuffer.putInt(arrayOfInt[i]);
        i++;
      }
    }
    
    public ColorSpaceTransform unmarshal(ByteBuffer paramByteBuffer)
    {
      int[] arrayOfInt = new int[18];
      for (int i = 0; i < 18; i++) {
        arrayOfInt[i] = paramByteBuffer.getInt();
      }
      return new ColorSpaceTransform(arrayOfInt);
    }
  }
}
