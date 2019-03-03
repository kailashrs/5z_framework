package android.opengl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ETC1Util
{
  public ETC1Util() {}
  
  public static ETC1Texture compressTexture(Buffer paramBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(ETC1.getEncodedDataSize(paramInt1, paramInt2)).order(ByteOrder.nativeOrder());
    ETC1.encodeImage(paramBuffer, paramInt1, paramInt2, paramInt3, paramInt4, localByteBuffer);
    return new ETC1Texture(paramInt1, paramInt2, localByteBuffer);
  }
  
  public static ETC1Texture createTexture(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte['က'];
    if (paramInputStream.read(arrayOfByte, 0, 16) == 16)
    {
      ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder());
      localByteBuffer.put(arrayOfByte, 0, 16).position(0);
      if (ETC1.isValid(localByteBuffer))
      {
        int i = ETC1.getWidth(localByteBuffer);
        int j = ETC1.getHeight(localByteBuffer);
        int k = ETC1.getEncodedDataSize(i, j);
        localByteBuffer = ByteBuffer.allocateDirect(k).order(ByteOrder.nativeOrder());
        int m = 0;
        while (m < k)
        {
          int n = Math.min(arrayOfByte.length, k - m);
          if (paramInputStream.read(arrayOfByte, 0, n) == n)
          {
            localByteBuffer.put(arrayOfByte, 0, n);
            m += n;
          }
          else
          {
            throw new IOException("Unable to read PKM file data.");
          }
        }
        localByteBuffer.position(0);
        return new ETC1Texture(i, j, localByteBuffer);
      }
      throw new IOException("Not a PKM file.");
    }
    throw new IOException("Unable to read PKM file header.");
  }
  
  public static boolean isETC1Supported()
  {
    int[] arrayOfInt1 = new int[20];
    GLES10.glGetIntegerv(34466, arrayOfInt1, 0);
    int i = arrayOfInt1[0];
    int[] arrayOfInt2 = arrayOfInt1;
    if (i > arrayOfInt1.length) {
      arrayOfInt2 = new int[i];
    }
    GLES10.glGetIntegerv(34467, arrayOfInt2, 0);
    for (int j = 0; j < i; j++) {
      if (arrayOfInt2[j] == 36196) {
        return true;
      }
    }
    return false;
  }
  
  public static void loadTexture(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, ETC1Texture paramETC1Texture)
  {
    if (paramInt4 == 6407)
    {
      if ((paramInt5 != 33635) && (paramInt5 != 5121)) {
        throw new IllegalArgumentException("Unsupported fallbackType");
      }
      int i = paramETC1Texture.getWidth();
      int j = paramETC1Texture.getHeight();
      ByteBuffer localByteBuffer = paramETC1Texture.getData();
      if (isETC1Supported())
      {
        GLES10.glCompressedTexImage2D(paramInt1, paramInt2, 36196, i, j, paramInt3, localByteBuffer.remaining(), localByteBuffer);
      }
      else
      {
        int k;
        if (paramInt5 != 5121) {
          k = 1;
        } else {
          k = 0;
        }
        if (k != 0) {
          k = 2;
        } else {
          k = 3;
        }
        int m = k * i;
        paramETC1Texture = ByteBuffer.allocateDirect(m * j).order(ByteOrder.nativeOrder());
        ETC1.decodeImage(localByteBuffer, paramETC1Texture, i, j, k, m);
        GLES10.glTexImage2D(paramInt1, paramInt2, paramInt4, i, j, paramInt3, paramInt4, paramInt5, paramETC1Texture);
      }
      return;
    }
    throw new IllegalArgumentException("fallbackFormat must be GL_RGB");
  }
  
  public static void loadTexture(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, InputStream paramInputStream)
    throws IOException
  {
    loadTexture(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, createTexture(paramInputStream));
  }
  
  public static void writeTexture(ETC1Texture paramETC1Texture, OutputStream paramOutputStream)
    throws IOException
  {
    ByteBuffer localByteBuffer1 = paramETC1Texture.getData();
    int i = localByteBuffer1.position();
    try
    {
      int j = paramETC1Texture.getWidth();
      int k = paramETC1Texture.getHeight();
      ByteBuffer localByteBuffer2 = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder());
      ETC1.formatHeader(localByteBuffer2, j, k);
      paramETC1Texture = new byte['က'];
      localByteBuffer2.get(paramETC1Texture, 0, 16);
      paramOutputStream.write(paramETC1Texture, 0, 16);
      k = ETC1.getEncodedDataSize(j, k);
      j = 0;
      while (j < k)
      {
        int m = Math.min(paramETC1Texture.length, k - j);
        localByteBuffer1.get(paramETC1Texture, 0, m);
        paramOutputStream.write(paramETC1Texture, 0, m);
        j += m;
      }
      return;
    }
    finally
    {
      localByteBuffer1.position(i);
    }
  }
  
  public static class ETC1Texture
  {
    private ByteBuffer mData;
    private int mHeight;
    private int mWidth;
    
    public ETC1Texture(int paramInt1, int paramInt2, ByteBuffer paramByteBuffer)
    {
      mWidth = paramInt1;
      mHeight = paramInt2;
      mData = paramByteBuffer;
    }
    
    public ByteBuffer getData()
    {
      return mData;
    }
    
    public int getHeight()
    {
      return mHeight;
    }
    
    public int getWidth()
    {
      return mWidth;
    }
  }
}
