package com.android.framework.protobuf.nano;

import java.io.IOException;

public final class CodedInputByteBufferNano
{
  private static final int DEFAULT_RECURSION_LIMIT = 64;
  private static final int DEFAULT_SIZE_LIMIT = 67108864;
  private final byte[] buffer;
  private int bufferPos;
  private int bufferSize;
  private int bufferSizeAfterLimit;
  private int bufferStart;
  private int currentLimit = Integer.MAX_VALUE;
  private int lastTag;
  private int recursionDepth;
  private int recursionLimit = 64;
  private int sizeLimit = 67108864;
  
  private CodedInputByteBufferNano(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    buffer = paramArrayOfByte;
    bufferStart = paramInt1;
    bufferSize = (paramInt1 + paramInt2);
    bufferPos = paramInt1;
  }
  
  public static int decodeZigZag32(int paramInt)
  {
    return paramInt >>> 1 ^ -(paramInt & 0x1);
  }
  
  public static long decodeZigZag64(long paramLong)
  {
    return paramLong >>> 1 ^ -(1L & paramLong);
  }
  
  public static CodedInputByteBufferNano newInstance(byte[] paramArrayOfByte)
  {
    return newInstance(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public static CodedInputByteBufferNano newInstance(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return new CodedInputByteBufferNano(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  private void recomputeBufferSizeAfterLimit()
  {
    bufferSize += bufferSizeAfterLimit;
    int i = bufferSize;
    if (i > currentLimit)
    {
      bufferSizeAfterLimit = (i - currentLimit);
      bufferSize -= bufferSizeAfterLimit;
    }
    else
    {
      bufferSizeAfterLimit = 0;
    }
  }
  
  public void checkLastTagWas(int paramInt)
    throws InvalidProtocolBufferNanoException
  {
    if (lastTag == paramInt) {
      return;
    }
    throw InvalidProtocolBufferNanoException.invalidEndTag();
  }
  
  public int getAbsolutePosition()
  {
    return bufferPos;
  }
  
  public byte[] getBuffer()
  {
    return buffer;
  }
  
  public int getBytesUntilLimit()
  {
    if (currentLimit == Integer.MAX_VALUE) {
      return -1;
    }
    int i = bufferPos;
    return currentLimit - i;
  }
  
  public byte[] getData(int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0) {
      return WireFormatNano.EMPTY_BYTES;
    }
    byte[] arrayOfByte = new byte[paramInt2];
    int i = bufferStart;
    System.arraycopy(buffer, i + paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public int getPosition()
  {
    return bufferPos - bufferStart;
  }
  
  public boolean isAtEnd()
  {
    boolean bool;
    if (bufferPos == bufferSize) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void popLimit(int paramInt)
  {
    currentLimit = paramInt;
    recomputeBufferSizeAfterLimit();
  }
  
  public int pushLimit(int paramInt)
    throws InvalidProtocolBufferNanoException
  {
    if (paramInt >= 0)
    {
      int i = paramInt + bufferPos;
      paramInt = currentLimit;
      if (i <= paramInt)
      {
        currentLimit = i;
        recomputeBufferSizeAfterLimit();
        return paramInt;
      }
      throw InvalidProtocolBufferNanoException.truncatedMessage();
    }
    throw InvalidProtocolBufferNanoException.negativeSize();
  }
  
  public boolean readBool()
    throws IOException
  {
    boolean bool;
    if (readRawVarint32() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public byte[] readBytes()
    throws IOException
  {
    int i = readRawVarint32();
    if ((i <= bufferSize - bufferPos) && (i > 0))
    {
      byte[] arrayOfByte = new byte[i];
      System.arraycopy(buffer, bufferPos, arrayOfByte, 0, i);
      bufferPos += i;
      return arrayOfByte;
    }
    if (i == 0) {
      return WireFormatNano.EMPTY_BYTES;
    }
    return readRawBytes(i);
  }
  
  public double readDouble()
    throws IOException
  {
    return Double.longBitsToDouble(readRawLittleEndian64());
  }
  
  public int readEnum()
    throws IOException
  {
    return readRawVarint32();
  }
  
  public int readFixed32()
    throws IOException
  {
    return readRawLittleEndian32();
  }
  
  public long readFixed64()
    throws IOException
  {
    return readRawLittleEndian64();
  }
  
  public float readFloat()
    throws IOException
  {
    return Float.intBitsToFloat(readRawLittleEndian32());
  }
  
  public void readGroup(MessageNano paramMessageNano, int paramInt)
    throws IOException
  {
    if (recursionDepth < recursionLimit)
    {
      recursionDepth += 1;
      paramMessageNano.mergeFrom(this);
      checkLastTagWas(WireFormatNano.makeTag(paramInt, 4));
      recursionDepth -= 1;
      return;
    }
    throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
  }
  
  public int readInt32()
    throws IOException
  {
    return readRawVarint32();
  }
  
  public long readInt64()
    throws IOException
  {
    return readRawVarint64();
  }
  
  public void readMessage(MessageNano paramMessageNano)
    throws IOException
  {
    int i = readRawVarint32();
    if (recursionDepth < recursionLimit)
    {
      i = pushLimit(i);
      recursionDepth += 1;
      paramMessageNano.mergeFrom(this);
      checkLastTagWas(0);
      recursionDepth -= 1;
      popLimit(i);
      return;
    }
    throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
  }
  
  Object readPrimitiveField(int paramInt)
    throws IOException
  {
    switch (paramInt)
    {
    case 10: 
    case 11: 
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown type ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    case 18: 
      return Long.valueOf(readSInt64());
    case 17: 
      return Integer.valueOf(readSInt32());
    case 16: 
      return Long.valueOf(readSFixed64());
    case 15: 
      return Integer.valueOf(readSFixed32());
    case 14: 
      return Integer.valueOf(readEnum());
    case 13: 
      return Integer.valueOf(readUInt32());
    case 12: 
      return readBytes();
    case 9: 
      return readString();
    case 8: 
      return Boolean.valueOf(readBool());
    case 7: 
      return Integer.valueOf(readFixed32());
    case 6: 
      return Long.valueOf(readFixed64());
    case 5: 
      return Integer.valueOf(readInt32());
    case 4: 
      return Long.valueOf(readUInt64());
    case 3: 
      return Long.valueOf(readInt64());
    case 2: 
      return Float.valueOf(readFloat());
    }
    return Double.valueOf(readDouble());
  }
  
  public byte readRawByte()
    throws IOException
  {
    if (bufferPos != bufferSize)
    {
      byte[] arrayOfByte = buffer;
      int i = bufferPos;
      bufferPos = (i + 1);
      return arrayOfByte[i];
    }
    throw InvalidProtocolBufferNanoException.truncatedMessage();
  }
  
  public byte[] readRawBytes(int paramInt)
    throws IOException
  {
    if (paramInt >= 0)
    {
      if (bufferPos + paramInt <= currentLimit)
      {
        if (paramInt <= bufferSize - bufferPos)
        {
          byte[] arrayOfByte = new byte[paramInt];
          System.arraycopy(buffer, bufferPos, arrayOfByte, 0, paramInt);
          bufferPos += paramInt;
          return arrayOfByte;
        }
        throw InvalidProtocolBufferNanoException.truncatedMessage();
      }
      skipRawBytes(currentLimit - bufferPos);
      throw InvalidProtocolBufferNanoException.truncatedMessage();
    }
    throw InvalidProtocolBufferNanoException.negativeSize();
  }
  
  public int readRawLittleEndian32()
    throws IOException
  {
    return readRawByte() & 0xFF | (readRawByte() & 0xFF) << 8 | (readRawByte() & 0xFF) << 16 | (readRawByte() & 0xFF) << 24;
  }
  
  public long readRawLittleEndian64()
    throws IOException
  {
    int i = readRawByte();
    int j = readRawByte();
    int k = readRawByte();
    int m = readRawByte();
    int n = readRawByte();
    int i1 = readRawByte();
    int i2 = readRawByte();
    int i3 = readRawByte();
    return i & 0xFF | (j & 0xFF) << 8 | (k & 0xFF) << 16 | (m & 0xFF) << 24 | (n & 0xFF) << 32 | (i1 & 0xFF) << 40 | (i2 & 0xFF) << 48 | (0xFF & i3) << 56;
  }
  
  public int readRawVarint32()
    throws IOException
  {
    int i = readRawByte();
    if (i >= 0) {
      return i;
    }
    int j = i & 0x7F;
    i = readRawByte();
    if (i >= 0)
    {
      i = j | i << 7;
    }
    else
    {
      i = j | (i & 0x7F) << 7;
      j = readRawByte();
      if (j >= 0)
      {
        i |= j << 14;
      }
      else
      {
        i |= (j & 0x7F) << 14;
        j = readRawByte();
        if (j >= 0)
        {
          i |= j << 21;
        }
        else
        {
          int k = readRawByte();
          j = i | (j & 0x7F) << 21 | k << 28;
          i = j;
          if (k < 0)
          {
            for (i = 0; i < 5; i++) {
              if (readRawByte() >= 0) {
                return j;
              }
            }
            throw InvalidProtocolBufferNanoException.malformedVarint();
          }
        }
      }
    }
    return i;
  }
  
  public long readRawVarint64()
    throws IOException
  {
    int i = 0;
    long l = 0L;
    while (i < 64)
    {
      int j = readRawByte();
      l |= (j & 0x7F) << i;
      if ((j & 0x80) == 0) {
        return l;
      }
      i += 7;
    }
    throw InvalidProtocolBufferNanoException.malformedVarint();
  }
  
  public int readSFixed32()
    throws IOException
  {
    return readRawLittleEndian32();
  }
  
  public long readSFixed64()
    throws IOException
  {
    return readRawLittleEndian64();
  }
  
  public int readSInt32()
    throws IOException
  {
    return decodeZigZag32(readRawVarint32());
  }
  
  public long readSInt64()
    throws IOException
  {
    return decodeZigZag64(readRawVarint64());
  }
  
  public String readString()
    throws IOException
  {
    int i = readRawVarint32();
    if ((i <= bufferSize - bufferPos) && (i > 0))
    {
      String str = new String(buffer, bufferPos, i, InternalNano.UTF_8);
      bufferPos += i;
      return str;
    }
    return new String(readRawBytes(i), InternalNano.UTF_8);
  }
  
  public int readTag()
    throws IOException
  {
    if (isAtEnd())
    {
      lastTag = 0;
      return 0;
    }
    lastTag = readRawVarint32();
    if (lastTag != 0) {
      return lastTag;
    }
    throw InvalidProtocolBufferNanoException.invalidTag();
  }
  
  public int readUInt32()
    throws IOException
  {
    return readRawVarint32();
  }
  
  public long readUInt64()
    throws IOException
  {
    return readRawVarint64();
  }
  
  public void resetSizeCounter() {}
  
  public void rewindToPosition(int paramInt)
  {
    if (paramInt <= bufferPos - bufferStart)
    {
      if (paramInt >= 0)
      {
        bufferPos = (bufferStart + paramInt);
        return;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Bad position ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Position ");
    localStringBuilder.append(paramInt);
    localStringBuilder.append(" is beyond current ");
    localStringBuilder.append(bufferPos - bufferStart);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public int setRecursionLimit(int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = recursionLimit;
      recursionLimit = paramInt;
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Recursion limit cannot be negative: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public int setSizeLimit(int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = sizeLimit;
      sizeLimit = paramInt;
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Size limit cannot be negative: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public boolean skipField(int paramInt)
    throws IOException
  {
    switch (WireFormatNano.getTagWireType(paramInt))
    {
    default: 
      throw InvalidProtocolBufferNanoException.invalidWireType();
    case 5: 
      readRawLittleEndian32();
      return true;
    case 4: 
      return false;
    case 3: 
      skipMessage();
      checkLastTagWas(WireFormatNano.makeTag(WireFormatNano.getTagFieldNumber(paramInt), 4));
      return true;
    case 2: 
      skipRawBytes(readRawVarint32());
      return true;
    case 1: 
      readRawLittleEndian64();
      return true;
    }
    readInt32();
    return true;
  }
  
  public void skipMessage()
    throws IOException
  {
    for (;;)
    {
      int i = readTag();
      if ((i == 0) || (!skipField(i))) {
        break;
      }
    }
  }
  
  public void skipRawBytes(int paramInt)
    throws IOException
  {
    if (paramInt >= 0)
    {
      if (bufferPos + paramInt <= currentLimit)
      {
        if (paramInt <= bufferSize - bufferPos)
        {
          bufferPos += paramInt;
          return;
        }
        throw InvalidProtocolBufferNanoException.truncatedMessage();
      }
      skipRawBytes(currentLimit - bufferPos);
      throw InvalidProtocolBufferNanoException.truncatedMessage();
    }
    throw InvalidProtocolBufferNanoException.negativeSize();
  }
}
