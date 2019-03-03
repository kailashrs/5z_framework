package com.android.internal.util;

public class BitwiseInputStream
{
  private byte[] mBuf;
  private int mEnd;
  private int mPos;
  
  public BitwiseInputStream(byte[] paramArrayOfByte)
  {
    mBuf = paramArrayOfByte;
    mEnd = (paramArrayOfByte.length << 3);
    mPos = 0;
  }
  
  public int available()
  {
    return mEnd - mPos;
  }
  
  public int read(int paramInt)
    throws BitwiseInputStream.AccessException
  {
    int i = mPos >>> 3;
    int j = 16 - (mPos & 0x7) - paramInt;
    if ((paramInt >= 0) && (paramInt <= 8) && (mPos + paramInt <= mEnd))
    {
      int k = (mBuf[i] & 0xFF) << 8;
      int m = k;
      if (j < 8) {
        m = k | mBuf[(i + 1)] & 0xFF;
      }
      mPos += paramInt;
      return m >>> j & -1 >>> 32 - paramInt;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("illegal read (pos ");
    localStringBuilder.append(mPos);
    localStringBuilder.append(", end ");
    localStringBuilder.append(mEnd);
    localStringBuilder.append(", bits ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(")");
    throw new AccessException(localStringBuilder.toString());
  }
  
  public byte[] readByteArray(int paramInt)
    throws BitwiseInputStream.AccessException
  {
    int i = 0;
    if ((paramInt & 0x7) > 0) {
      j = 1;
    } else {
      j = 0;
    }
    int k = (paramInt >>> 3) + j;
    byte[] arrayOfByte = new byte[k];
    for (int j = i; j < k; j++)
    {
      i = Math.min(8, paramInt - (j << 3));
      arrayOfByte[j] = ((byte)(byte)(read(i) << 8 - i));
    }
    return arrayOfByte;
  }
  
  public void skip(int paramInt)
    throws BitwiseInputStream.AccessException
  {
    if (mPos + paramInt <= mEnd)
    {
      mPos += paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("illegal skip (pos ");
    localStringBuilder.append(mPos);
    localStringBuilder.append(", end ");
    localStringBuilder.append(mEnd);
    localStringBuilder.append(", bits ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(")");
    throw new AccessException(localStringBuilder.toString());
  }
  
  public static class AccessException
    extends Exception
  {
    public AccessException(String paramString)
    {
      super();
    }
  }
}
