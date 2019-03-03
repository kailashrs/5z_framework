package com.android.internal.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.nio.charset.StandardCharsets;

public class ProcFileReader
  implements Closeable
{
  private final byte[] mBuffer;
  private boolean mLineFinished;
  private final InputStream mStream;
  private int mTail;
  
  public ProcFileReader(InputStream paramInputStream)
    throws IOException
  {
    this(paramInputStream, 4096);
  }
  
  public ProcFileReader(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    mStream = paramInputStream;
    mBuffer = new byte[paramInt];
    fillBuf();
  }
  
  private void consumeBuf(int paramInt)
    throws IOException
  {
    System.arraycopy(mBuffer, paramInt, mBuffer, 0, mTail - paramInt);
    mTail -= paramInt;
    if (mTail == 0) {
      fillBuf();
    }
  }
  
  private int fillBuf()
    throws IOException
  {
    int i = mBuffer.length - mTail;
    if (i != 0)
    {
      i = mStream.read(mBuffer, mTail, i);
      if (i != -1) {
        mTail += i;
      }
      return i;
    }
    throw new IOException("attempting to fill already-full buffer");
  }
  
  private NumberFormatException invalidLong(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("invalid long: ");
    localStringBuilder.append(new String(mBuffer, 0, paramInt, StandardCharsets.US_ASCII));
    return new NumberFormatException(localStringBuilder.toString());
  }
  
  private int nextTokenIndex()
    throws IOException
  {
    if (mLineFinished) {
      return -1;
    }
    int i = 0;
    do
    {
      while (i < mTail)
      {
        int j = mBuffer[i];
        if (j == 10)
        {
          mLineFinished = true;
          return i;
        }
        if (j == 32) {
          return i;
        }
        i++;
      }
    } while (fillBuf() > 0);
    throw new ProtocolException("End of stream while looking for token boundary");
  }
  
  private long parseAndConsumeLong(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = mBuffer;
    int i = 0;
    int j;
    if (arrayOfByte[0] == 45) {
      j = 1;
    } else {
      j = 0;
    }
    long l1 = 0L;
    long l2 = l1;
    if (j != 0)
    {
      i = 1;
      l2 = l1;
    }
    while (i < paramInt)
    {
      int k = mBuffer[i] - 48;
      if ((k >= 0) && (k <= 9))
      {
        l1 = 10L * l2 - k;
        if (l1 <= l2)
        {
          l2 = l1;
          i++;
        }
        else
        {
          throw invalidLong(paramInt);
        }
      }
      else
      {
        throw invalidLong(paramInt);
      }
    }
    consumeBuf(paramInt + 1);
    if (j == 0) {
      l2 = -l2;
    }
    return l2;
  }
  
  private String parseAndConsumeString(int paramInt)
    throws IOException
  {
    String str = new String(mBuffer, 0, paramInt, StandardCharsets.US_ASCII);
    consumeBuf(paramInt + 1);
    return str;
  }
  
  public void close()
    throws IOException
  {
    mStream.close();
  }
  
  public void finishLine()
    throws IOException
  {
    boolean bool = mLineFinished;
    int i = 0;
    if (bool)
    {
      mLineFinished = false;
      return;
    }
    do
    {
      while (i < mTail)
      {
        if (mBuffer[i] == 10)
        {
          consumeBuf(i + 1);
          return;
        }
        i++;
      }
    } while (fillBuf() > 0);
    throw new ProtocolException("End of stream while looking for line boundary");
  }
  
  public boolean hasMoreData()
  {
    boolean bool;
    if (mTail > 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int nextInt()
    throws IOException
  {
    long l = nextLong();
    if ((l <= 2147483647L) && (l >= -2147483648L)) {
      return (int)l;
    }
    throw new NumberFormatException("parsed value larger than integer");
  }
  
  public long nextLong()
    throws IOException
  {
    int i = nextTokenIndex();
    if (i != -1) {
      return parseAndConsumeLong(i);
    }
    throw new ProtocolException("Missing required long");
  }
  
  public long nextOptionalLong(long paramLong)
    throws IOException
  {
    int i = nextTokenIndex();
    if (i == -1) {
      return paramLong;
    }
    return parseAndConsumeLong(i);
  }
  
  public String nextString()
    throws IOException
  {
    int i = nextTokenIndex();
    if (i != -1) {
      return parseAndConsumeString(i);
    }
    throw new ProtocolException("Missing required string");
  }
}
