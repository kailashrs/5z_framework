package com.android.internal.util;

public class BitwiseOutputStream
{
  private byte[] mBuf;
  private int mEnd;
  private int mPos;
  
  public BitwiseOutputStream(int paramInt)
  {
    mBuf = new byte[paramInt];
    mEnd = (paramInt << 3);
    mPos = 0;
  }
  
  private void possExpand(int paramInt)
  {
    if (mPos + paramInt < mEnd) {
      return;
    }
    byte[] arrayOfByte = new byte[mPos + paramInt >>> 2];
    System.arraycopy(mBuf, 0, arrayOfByte, 0, mEnd >>> 3);
    mBuf = arrayOfByte;
    mEnd = (arrayOfByte.length << 3);
  }
  
  public void skip(int paramInt)
  {
    possExpand(paramInt);
    mPos += paramInt;
  }
  
  public byte[] toByteArray()
  {
    int i = mPos;
    if ((mPos & 0x7) > 0) {
      j = 1;
    } else {
      j = 0;
    }
    int j = (i >>> 3) + j;
    byte[] arrayOfByte = new byte[j];
    System.arraycopy(mBuf, 0, arrayOfByte, 0, j);
    return arrayOfByte;
  }
  
  public void write(int paramInt1, int paramInt2)
    throws BitwiseOutputStream.AccessException
  {
    if ((paramInt1 >= 0) && (paramInt1 <= 8))
    {
      possExpand(paramInt1);
      int i = mPos >>> 3;
      int j = 16 - (mPos & 0x7) - paramInt1;
      paramInt2 = (paramInt2 & -1 >>> 32 - paramInt1) << j;
      mPos += paramInt1;
      localObject = mBuf;
      localObject[i] = ((byte)(byte)(localObject[i] | paramInt2 >>> 8));
      if (j < 8)
      {
        localObject = mBuf;
        paramInt1 = i + 1;
        localObject[paramInt1] = ((byte)(byte)(localObject[paramInt1] | paramInt2 & 0xFF));
      }
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("illegal write (");
    ((StringBuilder)localObject).append(paramInt1);
    ((StringBuilder)localObject).append(" bits)");
    throw new AccessException(((StringBuilder)localObject).toString());
  }
  
  public void writeByteArray(int paramInt, byte[] paramArrayOfByte)
    throws BitwiseOutputStream.AccessException
  {
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      int j = Math.min(8, paramInt - (i << 3));
      if (j > 0) {
        write(j, (byte)(paramArrayOfByte[i] >>> 8 - j));
      }
    }
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
