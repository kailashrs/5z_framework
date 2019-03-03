package android.content.pm;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class LimitedLengthInputStream
  extends FilterInputStream
{
  private final long mEnd;
  private long mOffset;
  
  public LimitedLengthInputStream(InputStream paramInputStream, long paramLong1, long paramLong2)
    throws IOException
  {
    super(paramInputStream);
    if (paramInputStream != null)
    {
      if (paramLong1 >= 0L)
      {
        if (paramLong2 >= 0L)
        {
          if (paramLong2 <= Long.MAX_VALUE - paramLong1)
          {
            mEnd = (paramLong1 + paramLong2);
            skip(paramLong1);
            mOffset = paramLong1;
            return;
          }
          throw new IOException("offset + length > Long.MAX_VALUE");
        }
        throw new IOException("length < 0");
      }
      throw new IOException("offset < 0");
    }
    throw new IOException("in == null");
  }
  
  public int read()
    throws IOException
  {
    try
    {
      long l1 = mOffset;
      long l2 = mEnd;
      if (l1 >= l2) {
        return -1;
      }
      mOffset += 1L;
      int i = super.read();
      return i;
    }
    finally {}
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (mOffset >= mEnd) {
      return -1;
    }
    Arrays.checkOffsetAndCount(paramArrayOfByte.length, paramInt1, paramInt2);
    if (mOffset <= Long.MAX_VALUE - paramInt2)
    {
      int i = paramInt2;
      if (mOffset + paramInt2 > mEnd) {
        i = (int)(mEnd - mOffset);
      }
      paramInt1 = super.read(paramArrayOfByte, paramInt1, i);
      mOffset += paramInt1;
      return paramInt1;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("offset out of bounds: ");
    paramArrayOfByte.append(mOffset);
    paramArrayOfByte.append(" + ");
    paramArrayOfByte.append(paramInt2);
    throw new IOException(paramArrayOfByte.toString());
  }
}
