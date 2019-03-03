package com.android.internal.util;

import java.io.IOException;
import java.io.InputStream;
import libcore.io.Streams;

public class SizedInputStream
  extends InputStream
{
  private long mLength;
  private final InputStream mWrapped;
  
  public SizedInputStream(InputStream paramInputStream, long paramLong)
  {
    mWrapped = paramInputStream;
    mLength = paramLong;
  }
  
  public void close()
    throws IOException
  {
    super.close();
    mWrapped.close();
  }
  
  public int read()
    throws IOException
  {
    return Streams.readSingleByte(this);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (mLength <= 0L) {
      return -1;
    }
    int i = paramInt2;
    if (paramInt2 > mLength) {
      i = (int)mLength;
    }
    paramInt1 = mWrapped.read(paramArrayOfByte, paramInt1, i);
    if (paramInt1 == -1)
    {
      if (mLength > 0L)
      {
        paramArrayOfByte = new StringBuilder();
        paramArrayOfByte.append("Unexpected EOF; expected ");
        paramArrayOfByte.append(mLength);
        paramArrayOfByte.append(" more bytes");
        throw new IOException(paramArrayOfByte.toString());
      }
    }
    else {
      mLength -= paramInt1;
    }
    return paramInt1;
  }
}
