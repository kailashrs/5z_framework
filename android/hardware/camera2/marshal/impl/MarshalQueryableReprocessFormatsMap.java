package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.ReprocessFormatsMap;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class MarshalQueryableReprocessFormatsMap
  implements MarshalQueryable<ReprocessFormatsMap>
{
  public MarshalQueryableReprocessFormatsMap() {}
  
  public Marshaler<ReprocessFormatsMap> createMarshaler(TypeReference<ReprocessFormatsMap> paramTypeReference, int paramInt)
  {
    return new MarshalerReprocessFormatsMap(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<ReprocessFormatsMap> paramTypeReference, int paramInt)
  {
    boolean bool = true;
    if ((paramInt != 1) || (!paramTypeReference.getType().equals(ReprocessFormatsMap.class))) {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerReprocessFormatsMap
    extends Marshaler<ReprocessFormatsMap>
  {
    protected MarshalerReprocessFormatsMap(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int calculateMarshalSize(ReprocessFormatsMap paramReprocessFormatsMap)
    {
      int i = 0;
      int[] arrayOfInt = paramReprocessFormatsMap.getInputs();
      int j = arrayOfInt.length;
      for (int k = 0; k < j; k++) {
        i = i + 1 + 1 + paramReprocessFormatsMap.getOutputs(arrayOfInt[k]).length;
      }
      return i * 4;
    }
    
    public int getNativeSize()
    {
      return NATIVE_SIZE_DYNAMIC;
    }
    
    public void marshal(ReprocessFormatsMap paramReprocessFormatsMap, ByteBuffer paramByteBuffer)
    {
      for (int k : StreamConfigurationMap.imageFormatToInternal(paramReprocessFormatsMap.getInputs()))
      {
        paramByteBuffer.putInt(k);
        int[] arrayOfInt2 = StreamConfigurationMap.imageFormatToInternal(paramReprocessFormatsMap.getOutputs(k));
        paramByteBuffer.putInt(arrayOfInt2.length);
        int m = arrayOfInt2.length;
        for (k = 0; k < m; k++) {
          paramByteBuffer.putInt(arrayOfInt2[k]);
        }
      }
    }
    
    public ReprocessFormatsMap unmarshal(ByteBuffer paramByteBuffer)
    {
      int i = paramByteBuffer.remaining() / 4;
      if (paramByteBuffer.remaining() % 4 == 0)
      {
        int[] arrayOfInt = new int[i];
        paramByteBuffer.asIntBuffer().get(arrayOfInt);
        return new ReprocessFormatsMap(arrayOfInt);
      }
      throw new AssertionError("ReprocessFormatsMap was not TYPE_INT32");
    }
  }
}
