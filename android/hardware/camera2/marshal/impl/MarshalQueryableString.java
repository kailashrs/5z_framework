package android.hardware.camera2.marshal.impl;

import android.hardware.camera2.marshal.MarshalQueryable;
import android.hardware.camera2.marshal.Marshaler;
import android.hardware.camera2.utils.TypeReference;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class MarshalQueryableString
  implements MarshalQueryable<String>
{
  private static final boolean DEBUG = false;
  private static final byte NUL = 0;
  private static final String TAG = MarshalQueryableString.class.getSimpleName();
  
  public MarshalQueryableString() {}
  
  public Marshaler<String> createMarshaler(TypeReference<String> paramTypeReference, int paramInt)
  {
    return new MarshalerString(paramTypeReference, paramInt);
  }
  
  public boolean isTypeMappingSupported(TypeReference<String> paramTypeReference, int paramInt)
  {
    boolean bool;
    if ((paramInt == 0) && (String.class.equals(paramTypeReference.getType()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private class MarshalerString
    extends Marshaler<String>
  {
    protected MarshalerString(int paramInt)
    {
      super(paramInt, i);
    }
    
    public int calculateMarshalSize(String paramString)
    {
      return paramString.getBytes(MarshalQueryableString.PreloadHolder.UTF8_CHARSET).length + 1;
    }
    
    public int getNativeSize()
    {
      return NATIVE_SIZE_DYNAMIC;
    }
    
    public void marshal(String paramString, ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.put(paramString.getBytes(MarshalQueryableString.PreloadHolder.UTF8_CHARSET));
      paramByteBuffer.put((byte)0);
    }
    
    public String unmarshal(ByteBuffer paramByteBuffer)
    {
      paramByteBuffer.mark();
      int i = 0;
      int k;
      for (int j = 0;; j++)
      {
        k = i;
        if (!paramByteBuffer.hasRemaining()) {
          break;
        }
        if (paramByteBuffer.get() == 0)
        {
          k = 1;
          break;
        }
      }
      if (k != 0)
      {
        paramByteBuffer.reset();
        byte[] arrayOfByte = new byte[j + 1];
        paramByteBuffer.get(arrayOfByte, 0, j + 1);
        return new String(arrayOfByte, 0, j, MarshalQueryableString.PreloadHolder.UTF8_CHARSET);
      }
      throw new UnsupportedOperationException("Strings must be null-terminated");
    }
  }
  
  private static class PreloadHolder
  {
    public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
    
    private PreloadHolder() {}
  }
}
