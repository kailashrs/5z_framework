package android.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStream
  extends FilterInputStream
{
  private static final int BUFFER_SIZE = 2048;
  private static byte[] EMPTY = new byte[0];
  private final Base64.Coder coder;
  private boolean eof = false;
  private byte[] inputBuffer = new byte['ࠀ'];
  private int outputEnd;
  private int outputStart;
  
  public Base64InputStream(InputStream paramInputStream, int paramInt)
  {
    this(paramInputStream, paramInt, false);
  }
  
  public Base64InputStream(InputStream paramInputStream, int paramInt, boolean paramBoolean)
  {
    super(paramInputStream);
    if (paramBoolean) {
      coder = new Base64.Encoder(paramInt, null);
    } else {
      coder = new Base64.Decoder(paramInt, null);
    }
    coder.output = new byte[coder.maxOutputSize(2048)];
    outputStart = 0;
    outputEnd = 0;
  }
  
  private void refill()
    throws IOException
  {
    if (eof) {
      return;
    }
    int i = in.read(inputBuffer);
    boolean bool;
    if (i == -1)
    {
      eof = true;
      bool = coder.process(EMPTY, 0, 0, true);
    }
    else
    {
      bool = coder.process(inputBuffer, 0, i, false);
    }
    if (bool)
    {
      outputEnd = coder.op;
      outputStart = 0;
      return;
    }
    throw new Base64DataException("bad base-64");
  }
  
  public int available()
  {
    return outputEnd - outputStart;
  }
  
  public void close()
    throws IOException
  {
    in.close();
    inputBuffer = null;
  }
  
  public void mark(int paramInt)
  {
    throw new UnsupportedOperationException();
  }
  
  public boolean markSupported()
  {
    return false;
  }
  
  public int read()
    throws IOException
  {
    if (outputStart >= outputEnd) {
      refill();
    }
    if (outputStart >= outputEnd) {
      return -1;
    }
    byte[] arrayOfByte = coder.output;
    int i = outputStart;
    outputStart = (i + 1);
    return arrayOfByte[i] & 0xFF;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (outputStart >= outputEnd) {
      refill();
    }
    if (outputStart >= outputEnd) {
      return -1;
    }
    paramInt2 = Math.min(paramInt2, outputEnd - outputStart);
    System.arraycopy(coder.output, outputStart, paramArrayOfByte, paramInt1, paramInt2);
    outputStart += paramInt2;
    return paramInt2;
  }
  
  public void reset()
  {
    throw new UnsupportedOperationException();
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    if (outputStart >= outputEnd) {
      refill();
    }
    if (outputStart >= outputEnd) {
      return 0L;
    }
    paramLong = Math.min(paramLong, outputEnd - outputStart);
    outputStart = ((int)(outputStart + paramLong));
    return paramLong;
  }
}
