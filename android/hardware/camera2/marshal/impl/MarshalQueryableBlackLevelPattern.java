package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.BlackLevelPattern;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableBlackLevelPattern
  implements MarshalQueryable<BlackLevelPattern>
{
  private static final int SIZE = 16;
  
  public MarshalQueryableBlackLevelPattern() {}
  
  public Marshaler<BlackLevelPattern> createMarshaler(TypeReference<BlackLevelPattern> paramTypeReference, int paramInt)
  {
    return new MarshalerBlackLevelPattern(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<BlackLevelPattern> paramTypeReference, int paramInt)
  {
    boolean bool = true;
    if ((paramInt != 1) || (!BlackLevelPattern.class.equals(paramTypeReference.getType()))) {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerBlackLevelPattern
    extends Marshaler<BlackLevelPattern>
  {
    protected MarshalerBlackLevelPattern(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 16;
    }
    
    public void marshal(BlackLevelPattern paramBlackLevelPattern, ByteBuffer paramByteBuffer)
    {
      for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
          paramByteBuffer.putInt(paramBlackLevelPattern.getOffsetForIndex(j, i));
        }
      }
    }
    
    public BlackLevelPattern unmarshal(ByteBuffer paramByteBuffer)
    {
      int[] arrayOfInt = new int[4];
      for (int i = 0; i < 4; i++) {
        arrayOfInt[i] = paramByteBuffer.getInt();
      }
      return new BlackLevelPattern(arrayOfInt);
    }
  }
}
