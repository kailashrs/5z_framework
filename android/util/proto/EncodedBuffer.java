package android.util.proto;

import android.util.Log;
import java.util.ArrayList;

public final class EncodedBuffer
{
  private static final String TAG = "EncodedBuffer";
  private int mBufferCount;
  private final ArrayList<byte[]> mBuffers = new ArrayList();
  private final int mChunkSize;
  private int mReadBufIndex;
  private byte[] mReadBuffer;
  private int mReadIndex;
  private int mReadLimit = -1;
  private int mReadableSize = -1;
  private int mWriteBufIndex;
  private byte[] mWriteBuffer;
  private int mWriteIndex;
  
  public EncodedBuffer()
  {
    this(0);
  }
  
  public EncodedBuffer(int paramInt)
  {
    int i = paramInt;
    if (paramInt <= 0) {
      i = 8192;
    }
    mChunkSize = i;
    mWriteBuffer = new byte[mChunkSize];
    mBuffers.add(mWriteBuffer);
    mBufferCount = 1;
  }
  
  private static int dumpByteString(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte)
  {
    Object localObject1 = new StringBuffer();
    int i = paramArrayOfByte.length;
    int j = 0;
    while (j < i)
    {
      Object localObject2;
      if (j % 16 == 0)
      {
        localObject2 = localObject1;
        if (j != 0)
        {
          Log.d(paramString1, ((StringBuffer)localObject1).toString());
          localObject2 = new StringBuffer();
        }
        ((StringBuffer)localObject2).append(paramString2);
        ((StringBuffer)localObject2).append('[');
        ((StringBuffer)localObject2).append(paramInt + j);
        ((StringBuffer)localObject2).append(']');
        ((StringBuffer)localObject2).append(' ');
      }
      else
      {
        ((StringBuffer)localObject1).append(' ');
        localObject2 = localObject1;
      }
      int k = paramArrayOfByte[j];
      int m = (byte)(k >> 4 & 0xF);
      if (m < 10) {
        ((StringBuffer)localObject2).append((char)(48 + m));
      } else {
        ((StringBuffer)localObject2).append((char)(87 + m));
      }
      k = (byte)(k & 0xF);
      if (k < 10) {
        ((StringBuffer)localObject2).append((char)(48 + k));
      } else {
        ((StringBuffer)localObject2).append((char)(87 + k));
      }
      j++;
      localObject1 = localObject2;
    }
    Log.d(paramString1, ((StringBuffer)localObject1).toString());
    return i;
  }
  
  public static void dumpByteString(String paramString1, String paramString2, byte[] paramArrayOfByte)
  {
    dumpByteString(paramString1, paramString2, 0, paramArrayOfByte);
  }
  
  public static int getRawVarint32Size(int paramInt)
  {
    if ((paramInt & 0xFFFFFF80) == 0) {
      return 1;
    }
    if ((paramInt & 0xC000) == 0) {
      return 2;
    }
    if ((0xFFE00000 & paramInt) == 0) {
      return 3;
    }
    if ((0xF0000000 & paramInt) == 0) {
      return 4;
    }
    return 5;
  }
  
  public static int getRawVarint64Size(long paramLong)
  {
    if ((0xFFFFFFFFFFFFFF80 & paramLong) == 0L) {
      return 1;
    }
    if ((0xFFFFFFFFFFFFC000 & paramLong) == 0L) {
      return 2;
    }
    if ((0xFFFFFFFFFFE00000 & paramLong) == 0L) {
      return 3;
    }
    if ((0xFFFFFFFFF0000000 & paramLong) == 0L) {
      return 4;
    }
    if ((0xFFFFFFF800000000 & paramLong) == 0L) {
      return 5;
    }
    if ((0xFFFFFC0000000000 & paramLong) == 0L) {
      return 6;
    }
    if ((0xFFFE000000000000 & paramLong) == 0L) {
      return 7;
    }
    if ((0xFF00000000000000 & paramLong) == 0L) {
      return 8;
    }
    if ((0x8000000000000000 & paramLong) == 0L) {
      return 9;
    }
    return 10;
  }
  
  public static int getRawZigZag32Size(int paramInt)
  {
    return getRawVarint32Size(zigZag32(paramInt));
  }
  
  public static int getRawZigZag64Size(long paramLong)
  {
    return getRawVarint64Size(zigZag64(paramLong));
  }
  
  private void nextWriteBuffer()
  {
    mWriteBufIndex += 1;
    if (mWriteBufIndex >= mBufferCount)
    {
      mWriteBuffer = new byte[mChunkSize];
      mBuffers.add(mWriteBuffer);
      mBufferCount += 1;
    }
    else
    {
      mWriteBuffer = ((byte[])mBuffers.get(mWriteBufIndex));
    }
    mWriteIndex = 0;
  }
  
  private static int zigZag32(int paramInt)
  {
    return paramInt << 1 ^ paramInt >> 31;
  }
  
  private static long zigZag64(long paramLong)
  {
    return paramLong << 1 ^ paramLong >> 63;
  }
  
  public void dumpBuffers(String paramString)
  {
    int i = mBuffers.size();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("{");
      localStringBuilder.append(k);
      localStringBuilder.append("} ");
      j += dumpByteString(paramString, localStringBuilder.toString(), j, (byte[])mBuffers.get(k));
    }
  }
  
  public void editRawFixed32(int paramInt1, int paramInt2)
  {
    ((byte[])mBuffers.get(paramInt1 / mChunkSize))[(paramInt1 % mChunkSize)] = ((byte)(byte)paramInt2);
    ((byte[])mBuffers.get((paramInt1 + 1) / mChunkSize))[((paramInt1 + 1) % mChunkSize)] = ((byte)(byte)(paramInt2 >> 8));
    ((byte[])mBuffers.get((paramInt1 + 2) / mChunkSize))[((paramInt1 + 2) % mChunkSize)] = ((byte)(byte)(paramInt2 >> 16));
    ((byte[])mBuffers.get((paramInt1 + 3) / mChunkSize))[((paramInt1 + 3) % mChunkSize)] = ((byte)(byte)(paramInt2 >> 24));
  }
  
  public byte[] getBytes(int paramInt)
  {
    byte[] arrayOfByte = new byte[paramInt];
    int i = paramInt / mChunkSize;
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      System.arraycopy((byte[])mBuffers.get(k), 0, arrayOfByte, j, mChunkSize);
      j += mChunkSize;
    }
    paramInt -= mChunkSize * i;
    if (paramInt > 0) {
      System.arraycopy((byte[])mBuffers.get(k), 0, arrayOfByte, j, paramInt);
    }
    return arrayOfByte;
  }
  
  public int getChunkCount()
  {
    return mBuffers.size();
  }
  
  public String getDebugString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("EncodedBuffer( mChunkSize=");
    localStringBuilder.append(mChunkSize);
    localStringBuilder.append(" mBuffers.size=");
    localStringBuilder.append(mBuffers.size());
    localStringBuilder.append(" mBufferCount=");
    localStringBuilder.append(mBufferCount);
    localStringBuilder.append(" mWriteIndex=");
    localStringBuilder.append(mWriteIndex);
    localStringBuilder.append(" mWriteBufIndex=");
    localStringBuilder.append(mWriteBufIndex);
    localStringBuilder.append(" mReadBufIndex=");
    localStringBuilder.append(mReadBufIndex);
    localStringBuilder.append(" mReadIndex=");
    localStringBuilder.append(mReadIndex);
    localStringBuilder.append(" mReadableSize=");
    localStringBuilder.append(mReadableSize);
    localStringBuilder.append(" mReadLimit=");
    localStringBuilder.append(mReadLimit);
    localStringBuilder.append(" )");
    return localStringBuilder.toString();
  }
  
  public int getRawFixed32At(int paramInt)
  {
    return ((byte[])mBuffers.get(paramInt / mChunkSize))[(paramInt % mChunkSize)] & 0xFF | (((byte[])mBuffers.get((paramInt + 1) / mChunkSize))[((paramInt + 1) % mChunkSize)] & 0xFF) << 8 | (((byte[])mBuffers.get((paramInt + 2) / mChunkSize))[((paramInt + 2) % mChunkSize)] & 0xFF) << 16 | (0xFF & ((byte[])mBuffers.get((paramInt + 3) / mChunkSize))[((paramInt + 3) % mChunkSize)]) << 24;
  }
  
  public int getReadPos()
  {
    return mReadBufIndex * mChunkSize + mReadIndex;
  }
  
  public int getReadableSize()
  {
    return mReadableSize;
  }
  
  public int getWriteBufIndex()
  {
    return mWriteBufIndex;
  }
  
  public int getWriteIndex()
  {
    return mWriteIndex;
  }
  
  public int getWritePos()
  {
    return mWriteBufIndex * mChunkSize + mWriteIndex;
  }
  
  public byte readRawByte()
  {
    if ((mReadBufIndex <= mBufferCount) && ((mReadBufIndex != mBufferCount - 1) || (mReadIndex < mReadLimit)))
    {
      if (mReadIndex >= mChunkSize)
      {
        mReadBufIndex += 1;
        mReadBuffer = ((byte[])mBuffers.get(mReadBufIndex));
        mReadIndex = 0;
      }
      localObject = mReadBuffer;
      int i = mReadIndex;
      mReadIndex = (i + 1);
      return localObject[i];
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Trying to read too much data mReadBufIndex=");
    ((StringBuilder)localObject).append(mReadBufIndex);
    ((StringBuilder)localObject).append(" mBufferCount=");
    ((StringBuilder)localObject).append(mBufferCount);
    ((StringBuilder)localObject).append(" mReadIndex=");
    ((StringBuilder)localObject).append(mReadIndex);
    ((StringBuilder)localObject).append(" mReadLimit=");
    ((StringBuilder)localObject).append(mReadLimit);
    throw new IndexOutOfBoundsException(((StringBuilder)localObject).toString());
  }
  
  public int readRawFixed32()
  {
    return readRawByte() & 0xFF | (readRawByte() & 0xFF) << 8 | (readRawByte() & 0xFF) << 16 | (readRawByte() & 0xFF) << 24;
  }
  
  public long readRawUnsigned()
  {
    int i = 0;
    long l = 0L;
    do
    {
      int j = readRawByte();
      l |= (j & 0x7F) << i;
      if ((j & 0x80) == 0) {
        return l;
      }
      i += 7;
    } while (i <= 64);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Varint too long -- ");
    localStringBuilder.append(getDebugString());
    throw new ProtoParseException(localStringBuilder.toString());
  }
  
  public void rewindRead()
  {
    mReadBuffer = ((byte[])mBuffers.get(0));
    mReadBufIndex = 0;
    mReadIndex = 0;
  }
  
  public void rewindWriteTo(int paramInt)
  {
    if (paramInt <= getWritePos())
    {
      mWriteBufIndex = (paramInt / mChunkSize);
      mWriteIndex = (paramInt % mChunkSize);
      if ((mWriteIndex == 0) && (mWriteBufIndex != 0))
      {
        mWriteIndex = mChunkSize;
        mWriteBufIndex -= 1;
      }
      mWriteBuffer = ((byte[])mBuffers.get(mWriteBufIndex));
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("rewindWriteTo only can go backwards");
    localStringBuilder.append(paramInt);
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void skipRead(int paramInt)
  {
    if (paramInt >= 0)
    {
      if (paramInt == 0) {
        return;
      }
      if (paramInt <= mChunkSize - mReadIndex)
      {
        mReadIndex += paramInt;
      }
      else
      {
        paramInt -= mChunkSize - mReadIndex;
        mReadIndex = (paramInt % mChunkSize);
        if (mReadIndex == 0)
        {
          mReadIndex = mChunkSize;
          mReadBufIndex += paramInt / mChunkSize;
        }
        else
        {
          mReadBufIndex += 1 + paramInt / mChunkSize;
        }
        mReadBuffer = ((byte[])mBuffers.get(mReadBufIndex));
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("skipRead with negative amount=");
    localStringBuilder.append(paramInt);
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public void startEditing()
  {
    mReadableSize = (mWriteBufIndex * mChunkSize + mWriteIndex);
    mReadLimit = mWriteIndex;
    mWriteBuffer = ((byte[])mBuffers.get(0));
    mWriteIndex = 0;
    mWriteBufIndex = 0;
    mReadBuffer = mWriteBuffer;
    mReadBufIndex = 0;
    mReadIndex = 0;
  }
  
  public void writeFromThisBuffer(int paramInt1, int paramInt2)
  {
    if (mReadLimit >= 0)
    {
      if (paramInt1 >= getWritePos())
      {
        if (paramInt1 + paramInt2 <= mReadableSize)
        {
          if (paramInt2 == 0) {
            return;
          }
          if (paramInt1 == mWriteBufIndex * mChunkSize + mWriteIndex)
          {
            if (paramInt2 <= mChunkSize - mWriteIndex)
            {
              mWriteIndex += paramInt2;
            }
            else
            {
              paramInt1 = paramInt2 - (mChunkSize - mWriteIndex);
              mWriteIndex = (paramInt1 % mChunkSize);
              if (mWriteIndex == 0)
              {
                mWriteIndex = mChunkSize;
                mWriteBufIndex += paramInt1 / mChunkSize;
              }
              else
              {
                mWriteBufIndex += 1 + paramInt1 / mChunkSize;
              }
              mWriteBuffer = ((byte[])mBuffers.get(mWriteBufIndex));
            }
          }
          else
          {
            int i = paramInt1 / mChunkSize;
            localObject = (byte[])mBuffers.get(i);
            paramInt1 %= mChunkSize;
            while (paramInt2 > 0)
            {
              if (mWriteIndex >= mChunkSize) {
                nextWriteBuffer();
              }
              int j = i;
              int k = paramInt1;
              if (paramInt1 >= mChunkSize)
              {
                j = i + 1;
                localObject = (byte[])mBuffers.get(j);
                k = 0;
              }
              i = Math.min(paramInt2, Math.min(mChunkSize - mWriteIndex, mChunkSize - k));
              System.arraycopy((byte[])localObject, k, mWriteBuffer, mWriteIndex, i);
              mWriteIndex += i;
              paramInt1 = k + i;
              paramInt2 -= i;
              i = j;
            }
          }
          return;
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Trying to move more data than there is -- srcOffset=");
        ((StringBuilder)localObject).append(paramInt1);
        ((StringBuilder)localObject).append(" size=");
        ((StringBuilder)localObject).append(paramInt2);
        ((StringBuilder)localObject).append(" ");
        ((StringBuilder)localObject).append(getDebugString());
        throw new IllegalArgumentException(((StringBuilder)localObject).toString());
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Can only move forward in the buffer -- srcOffset=");
      ((StringBuilder)localObject).append(paramInt1);
      ((StringBuilder)localObject).append(" size=");
      ((StringBuilder)localObject).append(paramInt2);
      ((StringBuilder)localObject).append(" ");
      ((StringBuilder)localObject).append(getDebugString());
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    throw new IllegalStateException("writeFromThisBuffer before startEditing");
  }
  
  public void writeRawBuffer(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
      writeRawBuffer(paramArrayOfByte, 0, paramArrayOfByte.length);
    }
  }
  
  public void writeRawBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte == null) {
      return;
    }
    int i;
    if (paramInt2 < mChunkSize - mWriteIndex) {
      i = paramInt2;
    } else {
      i = mChunkSize - mWriteIndex;
    }
    int j = paramInt1;
    int k = paramInt2;
    if (i > 0)
    {
      System.arraycopy(paramArrayOfByte, paramInt1, mWriteBuffer, mWriteIndex, i);
      mWriteIndex += i;
      k = paramInt2 - i;
      j = paramInt1 + i;
    }
    while (k > 0)
    {
      nextWriteBuffer();
      if (k < mChunkSize) {
        paramInt1 = k;
      } else {
        paramInt1 = mChunkSize;
      }
      System.arraycopy(paramArrayOfByte, j, mWriteBuffer, mWriteIndex, paramInt1);
      mWriteIndex += paramInt1;
      k -= paramInt1;
      j += paramInt1;
    }
  }
  
  public void writeRawByte(byte paramByte)
  {
    if (mWriteIndex >= mChunkSize) {
      nextWriteBuffer();
    }
    byte[] arrayOfByte = mWriteBuffer;
    int i = mWriteIndex;
    mWriteIndex = (i + 1);
    arrayOfByte[i] = ((byte)paramByte);
  }
  
  public void writeRawFixed32(int paramInt)
  {
    writeRawByte((byte)paramInt);
    writeRawByte((byte)(paramInt >> 8));
    writeRawByte((byte)(paramInt >> 16));
    writeRawByte((byte)(paramInt >> 24));
  }
  
  public void writeRawFixed64(long paramLong)
  {
    writeRawByte((byte)(int)paramLong);
    writeRawByte((byte)(int)(paramLong >> 8));
    writeRawByte((byte)(int)(paramLong >> 16));
    writeRawByte((byte)(int)(paramLong >> 24));
    writeRawByte((byte)(int)(paramLong >> 32));
    writeRawByte((byte)(int)(paramLong >> 40));
    writeRawByte((byte)(int)(paramLong >> 48));
    writeRawByte((byte)(int)(paramLong >> 56));
  }
  
  public void writeRawVarint32(int paramInt)
  {
    for (;;)
    {
      if ((paramInt & 0xFFFFFF80) == 0)
      {
        writeRawByte((byte)paramInt);
        return;
      }
      writeRawByte((byte)(paramInt & 0x7F | 0x80));
      paramInt >>>= 7;
    }
  }
  
  public void writeRawVarint64(long paramLong)
  {
    for (;;)
    {
      if ((0xFFFFFFFFFFFFFF80 & paramLong) == 0L)
      {
        writeRawByte((byte)(int)paramLong);
        return;
      }
      writeRawByte((byte)(int)(0x7F & paramLong | 0x80));
      paramLong >>>= 7;
    }
  }
  
  public void writeRawZigZag32(int paramInt)
  {
    writeRawVarint32(zigZag32(paramInt));
  }
  
  public void writeRawZigZag64(long paramLong)
  {
    writeRawVarint64(zigZag64(paramLong));
  }
}
