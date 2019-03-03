package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;

public class MarshalQueryableStreamConfiguration
  implements MarshalQueryable<StreamConfiguration>
{
  private static final int SIZE = 16;
  
  public MarshalQueryableStreamConfiguration() {}
  
  public Marshaler<StreamConfiguration> createMarshaler(TypeReference<StreamConfiguration> paramTypeReference, int paramInt)
  {
    return new MarshalerStreamConfiguration(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<StreamConfiguration> paramTypeReference, int paramInt)
  {
    boolean bool = true;
    if ((paramInt != 1) || (!paramTypeReference.getType().equals(StreamConfiguration.class))) {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerStreamConfiguration
    extends Marshaler<StreamConfiguration>
  {
    protected MarshalerStreamConfiguration(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int getNativeSize()
    {
      return 16;
    }
    
    public void marshal(StreamConfiguration paramStreamConfiguration, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.putInt(paramStreamConfiguration.getFormat());
      paramByteBuffer.putInt(paramStreamConfiguration.getWidth());
      paramByteBuffer.putInt(paramStreamConfiguration.getHeight());
      paramByteBuffer.putInt(paramStreamConfiguration.isInput());
    }
    
    public StreamConfiguration unmarshal(ByteBuffer paramByteBuffer)
    {
      int i = paramByteBuffer.getInt();
      int j = paramByteBuffer.getInt();
      int k = paramByteBuffer.getInt();
      boolean bool;
      if (paramByteBuffer.getInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return new StreamConfiguration(i, j, k, bool);
    }
  }
}
