package android.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64OutputStream
  extends FilterOutputStream
{
  private static byte[] EMPTY = new byte[0];
  private int bpos = 0;
  private byte[] buffer = null;
  private final Base64.Coder coder;
  private final int flags;
  
  public Base64OutputStream(OutputStream paramOutputStream, int paramInt)
  {
    this(paramOutputStream, paramInt, true);
  }
  
  public Base64OutputStream(OutputStream paramOutputStream, int paramInt, boolean paramBoolean)
  {
    super(paramOutputStream);
    flags = paramInt;
    if (paramBoolean) {
      coder = new Base64.Encoder(paramInt, null);
    } else {
      coder = new Base64.Decoder(paramInt, null);
    }
  }
  
  private byte[] embiggen(byte[] paramArrayOfByte, int paramInt)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length >= paramInt)) {
      return paramArrayOfByte;
    }
    return new byte[paramInt];
  }
  
  private void flushBuffer()
    throws IOException
  {
    if (bpos > 0)
    {
      internalWrite(buffer, 0, bpos, false);
      bpos = 0;
    }
  }
  
  private void internalWrite(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    throws IOException
  {
    coder.output = embiggen(coder.output, coder.maxOutputSize(paramInt2));
    if (coder.process(paramArrayOfByte, paramInt1, paramInt2, paramBoolean))
    {
      out.write(coder.output, 0, coder.op);
      return;
    }
    throw new Base64DataException("bad base-64");
  }
  
  public void close()
    throws IOException
  {
    Object localObject1 = null;
    try
    {
      flushBuffer();
      internalWrite(EMPTY, 0, 0, true);
    }
    catch (IOException localIOException1) {}
    Object localObject2;
    try
    {
      if ((flags & 0x10) == 0) {
        out.close();
      } else {
        out.flush();
      }
      localObject2 = localIOException1;
    }
    catch (IOException localIOException2)
    {
      localObject2 = localIOException1;
      if (localIOException1 != null) {
        localObject2 = localIOException2;
      }
    }
    if (localObject2 == null) {
      return;
    }
    throw ((Throwable)localObject2);
  }
  
  public void write(int paramInt)
    throws IOException
  {
    if (buffer == null) {
      buffer = new byte['Ѐ'];
    }
    if (bpos >= buffer.length)
    {
      internalWrite(buffer, 0, bpos, false);
      bpos = 0;
    }
    byte[] arrayOfByte = buffer;
    int i = bpos;
    bpos = (i + 1);
    arrayOfByte[i] = ((byte)(byte)paramInt);
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 <= 0) {
      return;
    }
    flushBuffer();
    internalWrite(paramArrayOfByte, paramInt1, paramInt2, false);
  }
}
