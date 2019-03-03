package com.android.framework.protobuf.nano;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ReadOnlyBufferException;

public final class CodedOutputByteBufferNano
{
  public static final int LITTLE_ENDIAN_32_SIZE = 4;
  public static final int LITTLE_ENDIAN_64_SIZE = 8;
  private static final int MAX_UTF8_EXPANSION = 3;
  private final ByteBuffer buffer;
  
  private CodedOutputByteBufferNano(ByteBuffer paramByteBuffer)
  {
    buffer = paramByteBuffer;
    buffer.order(ByteOrder.LITTLE_ENDIAN);
  }
  
  private CodedOutputByteBufferNano(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this(ByteBuffer.wrap(paramArrayOfByte, paramInt1, paramInt2));
  }
  
  public static int computeBoolSize(int paramInt, boolean paramBoolean)
  {
    return computeTagSize(paramInt) + computeBoolSizeNoTag(paramBoolean);
  }
  
  public static int computeBoolSizeNoTag(boolean paramBoolean)
  {
    return 1;
  }
  
  public static int computeBytesSize(int paramInt1, int paramInt2)
  {
    return computeTagSize(paramInt1) + computeBytesSizeNoTag(paramInt2);
  }
  
  public static int computeBytesSize(int paramInt, byte[] paramArrayOfByte)
  {
    return computeTagSize(paramInt) + computeBytesSizeNoTag(paramArrayOfByte);
  }
  
  public static int computeBytesSizeNoTag(int paramInt)
  {
    return computeRawVarint32Size(paramInt) + paramInt;
  }
  
  public static int computeBytesSizeNoTag(byte[] paramArrayOfByte)
  {
    return computeRawVarint32Size(paramArrayOfByte.length) + paramArrayOfByte.length;
  }
  
  public static int computeDoubleSize(int paramInt, double paramDouble)
  {
    return computeTagSize(paramInt) + computeDoubleSizeNoTag(paramDouble);
  }
  
  public static int computeDoubleSizeNoTag(double paramDouble)
  {
    return 8;
  }
  
  public static int computeEnumSize(int paramInt1, int paramInt2)
  {
    return computeTagSize(paramInt1) + computeEnumSizeNoTag(paramInt2);
  }
  
  public static int computeEnumSizeNoTag(int paramInt)
  {
    return computeRawVarint32Size(paramInt);
  }
  
  static int computeFieldSize(int paramInt1, int paramInt2, Object paramObject)
  {
    switch (paramInt2)
    {
    default: 
      paramObject = new StringBuilder();
      paramObject.append("Unknown type: ");
      paramObject.append(paramInt2);
      throw new IllegalArgumentException(paramObject.toString());
    case 18: 
      return computeSInt64Size(paramInt1, ((Long)paramObject).longValue());
    case 17: 
      return computeSInt32Size(paramInt1, ((Integer)paramObject).intValue());
    case 16: 
      return computeSFixed64Size(paramInt1, ((Long)paramObject).longValue());
    case 15: 
      return computeSFixed32Size(paramInt1, ((Integer)paramObject).intValue());
    case 14: 
      return computeEnumSize(paramInt1, ((Integer)paramObject).intValue());
    case 13: 
      return computeUInt32Size(paramInt1, ((Integer)paramObject).intValue());
    case 12: 
      return computeBytesSize(paramInt1, (byte[])paramObject);
    case 11: 
      return computeMessageSize(paramInt1, (MessageNano)paramObject);
    case 10: 
      return computeGroupSize(paramInt1, (MessageNano)paramObject);
    case 9: 
      return computeStringSize(paramInt1, (String)paramObject);
    case 8: 
      return computeBoolSize(paramInt1, ((Boolean)paramObject).booleanValue());
    case 7: 
      return computeFixed32Size(paramInt1, ((Integer)paramObject).intValue());
    case 6: 
      return computeFixed64Size(paramInt1, ((Long)paramObject).longValue());
    case 5: 
      return computeInt32Size(paramInt1, ((Integer)paramObject).intValue());
    case 4: 
      return computeUInt64Size(paramInt1, ((Long)paramObject).longValue());
    case 3: 
      return computeInt64Size(paramInt1, ((Long)paramObject).longValue());
    case 2: 
      return computeFloatSize(paramInt1, ((Float)paramObject).floatValue());
    }
    return computeDoubleSize(paramInt1, ((Double)paramObject).doubleValue());
  }
  
  public static int computeFixed32Size(int paramInt1, int paramInt2)
  {
    return computeTagSize(paramInt1) + computeFixed32SizeNoTag(paramInt2);
  }
  
  public static int computeFixed32SizeNoTag(int paramInt)
  {
    return 4;
  }
  
  public static int computeFixed64Size(int paramInt, long paramLong)
  {
    return computeTagSize(paramInt) + computeFixed64SizeNoTag(paramLong);
  }
  
  public static int computeFixed64SizeNoTag(long paramLong)
  {
    return 8;
  }
  
  public static int computeFloatSize(int paramInt, float paramFloat)
  {
    return computeTagSize(paramInt) + computeFloatSizeNoTag(paramFloat);
  }
  
  public static int computeFloatSizeNoTag(float paramFloat)
  {
    return 4;
  }
  
  public static int computeGroupSize(int paramInt, MessageNano paramMessageNano)
  {
    return computeTagSize(paramInt) * 2 + computeGroupSizeNoTag(paramMessageNano);
  }
  
  public static int computeGroupSizeNoTag(MessageNano paramMessageNano)
  {
    return paramMessageNano.getSerializedSize();
  }
  
  public static int computeInt32Size(int paramInt1, int paramInt2)
  {
    return computeTagSize(paramInt1) + computeInt32SizeNoTag(paramInt2);
  }
  
  public static int computeInt32SizeNoTag(int paramInt)
  {
    if (paramInt >= 0) {
      return computeRawVarint32Size(paramInt);
    }
    return 10;
  }
  
  public static int computeInt64Size(int paramInt, long paramLong)
  {
    return computeTagSize(paramInt) + computeInt64SizeNoTag(paramLong);
  }
  
  public static int computeInt64SizeNoTag(long paramLong)
  {
    return computeRawVarint64Size(paramLong);
  }
  
  public static int computeMessageSize(int paramInt, MessageNano paramMessageNano)
  {
    return computeTagSize(paramInt) + computeMessageSizeNoTag(paramMessageNano);
  }
  
  public static int computeMessageSizeNoTag(MessageNano paramMessageNano)
  {
    int i = paramMessageNano.getSerializedSize();
    return computeRawVarint32Size(i) + i;
  }
  
  public static int computeRawVarint32Size(int paramInt)
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
  
  public static int computeRawVarint64Size(long paramLong)
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
  
  public static int computeSFixed32Size(int paramInt1, int paramInt2)
  {
    return computeTagSize(paramInt1) + computeSFixed32SizeNoTag(paramInt2);
  }
  
  public static int computeSFixed32SizeNoTag(int paramInt)
  {
    return 4;
  }
  
  public static int computeSFixed64Size(int paramInt, long paramLong)
  {
    return computeTagSize(paramInt) + computeSFixed64SizeNoTag(paramLong);
  }
  
  public static int computeSFixed64SizeNoTag(long paramLong)
  {
    return 8;
  }
  
  public static int computeSInt32Size(int paramInt1, int paramInt2)
  {
    return computeTagSize(paramInt1) + computeSInt32SizeNoTag(paramInt2);
  }
  
  public static int computeSInt32SizeNoTag(int paramInt)
  {
    return computeRawVarint32Size(encodeZigZag32(paramInt));
  }
  
  public static int computeSInt64Size(int paramInt, long paramLong)
  {
    return computeTagSize(paramInt) + computeSInt64SizeNoTag(paramLong);
  }
  
  public static int computeSInt64SizeNoTag(long paramLong)
  {
    return computeRawVarint64Size(encodeZigZag64(paramLong));
  }
  
  public static int computeStringSize(int paramInt, String paramString)
  {
    return computeTagSize(paramInt) + computeStringSizeNoTag(paramString);
  }
  
  public static int computeStringSizeNoTag(String paramString)
  {
    int i = encodedLength(paramString);
    return computeRawVarint32Size(i) + i;
  }
  
  public static int computeTagSize(int paramInt)
  {
    return computeRawVarint32Size(WireFormatNano.makeTag(paramInt, 0));
  }
  
  public static int computeUInt32Size(int paramInt1, int paramInt2)
  {
    return computeTagSize(paramInt1) + computeUInt32SizeNoTag(paramInt2);
  }
  
  public static int computeUInt32SizeNoTag(int paramInt)
  {
    return computeRawVarint32Size(paramInt);
  }
  
  public static int computeUInt64Size(int paramInt, long paramLong)
  {
    return computeTagSize(paramInt) + computeUInt64SizeNoTag(paramLong);
  }
  
  public static int computeUInt64SizeNoTag(long paramLong)
  {
    return computeRawVarint64Size(paramLong);
  }
  
  private static int encode(CharSequence paramCharSequence, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int i = paramCharSequence.length();
    int j = 0;
    int k = paramInt1 + paramInt2;
    for (paramInt2 = j; (paramInt2 < i) && (paramInt2 + paramInt1 < k); paramInt2++)
    {
      j = paramCharSequence.charAt(paramInt2);
      if (j >= 128) {
        break;
      }
      paramArrayOfByte[(paramInt1 + paramInt2)] = ((byte)(byte)j);
    }
    if (paramInt2 == i) {
      return paramInt1 + i;
    }
    j = paramInt1 + paramInt2;
    paramInt1 = paramInt2;
    paramInt2 = j;
    while (paramInt1 < i)
    {
      int m = paramCharSequence.charAt(paramInt1);
      if ((m < 128) && (paramInt2 < k))
      {
        j = paramInt2 + 1;
        paramArrayOfByte[paramInt2] = ((byte)(byte)m);
        paramInt2 = j;
      }
      for (;;)
      {
        break;
        if ((m < 2048) && (paramInt2 <= k - 2))
        {
          j = paramInt2 + 1;
          paramArrayOfByte[paramInt2] = ((byte)(byte)(0x3C0 | m >>> 6));
          paramInt2 = j + 1;
          paramArrayOfByte[j] = ((byte)(byte)(0x3F & m | 0x80));
        }
        else
        {
          int n;
          if (((m < 55296) || (57343 < m)) && (paramInt2 <= k - 3))
          {
            j = paramInt2 + 1;
            paramArrayOfByte[paramInt2] = ((byte)(byte)(0x1E0 | m >>> 12));
            n = j + 1;
            paramArrayOfByte[j] = ((byte)(byte)(m >>> 6 & 0x3F | 0x80));
            paramInt2 = n + 1;
            paramArrayOfByte[n] = ((byte)(byte)(0x3F & m | 0x80));
          }
          else
          {
            if (paramInt2 > k - 4) {
              break label465;
            }
            j = paramInt1;
            if (paramInt1 + 1 == paramCharSequence.length()) {
              break label428;
            }
            paramInt1++;
            char c = paramCharSequence.charAt(paramInt1);
            j = paramInt1;
            if (!Character.isSurrogatePair(m, c)) {
              break label428;
            }
            j = Character.toCodePoint(m, c);
            n = paramInt2 + 1;
            paramArrayOfByte[paramInt2] = ((byte)(byte)(0xF0 | j >>> 18));
            paramInt2 = n + 1;
            paramArrayOfByte[n] = ((byte)(byte)(j >>> 12 & 0x3F | 0x80));
            n = paramInt2 + 1;
            paramArrayOfByte[paramInt2] = ((byte)(byte)(j >>> 6 & 0x3F | 0x80));
            paramInt2 = n + 1;
            paramArrayOfByte[n] = ((byte)(byte)(0x3F & j | 0x80));
          }
        }
      }
      paramInt1++;
      continue;
      label428:
      paramCharSequence = new StringBuilder();
      paramCharSequence.append("Unpaired surrogate at index ");
      paramCharSequence.append(j - 1);
      throw new IllegalArgumentException(paramCharSequence.toString());
      label465:
      paramCharSequence = new StringBuilder();
      paramCharSequence.append("Failed writing ");
      paramCharSequence.append(m);
      paramCharSequence.append(" at index ");
      paramCharSequence.append(paramInt2);
      throw new ArrayIndexOutOfBoundsException(paramCharSequence.toString());
    }
    return paramInt2;
  }
  
  private static void encode(CharSequence paramCharSequence, ByteBuffer paramByteBuffer)
  {
    if (!paramByteBuffer.isReadOnly())
    {
      if (paramByteBuffer.hasArray()) {
        try
        {
          paramByteBuffer.position(encode(paramCharSequence, paramByteBuffer.array(), paramByteBuffer.arrayOffset() + paramByteBuffer.position(), paramByteBuffer.remaining()) - paramByteBuffer.arrayOffset());
        }
        catch (ArrayIndexOutOfBoundsException paramCharSequence)
        {
          paramByteBuffer = new BufferOverflowException();
          paramByteBuffer.initCause(paramCharSequence);
          throw paramByteBuffer;
        }
      } else {
        encodeDirect(paramCharSequence, paramByteBuffer);
      }
      return;
    }
    throw new ReadOnlyBufferException();
  }
  
  private static void encodeDirect(CharSequence paramCharSequence, ByteBuffer paramByteBuffer)
  {
    int i = paramCharSequence.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramCharSequence.charAt(j);
      if (k < 128)
      {
        paramByteBuffer.put((byte)k);
      }
      else if (k < 2048)
      {
        paramByteBuffer.put((byte)(0x3C0 | k >>> 6));
        paramByteBuffer.put((byte)(0x80 | 0x3F & k));
      }
      else if ((k >= 55296) && (57343 >= k))
      {
        int m = j;
        if (j + 1 != paramCharSequence.length())
        {
          j++;
          char c = paramCharSequence.charAt(j);
          m = j;
          if (Character.isSurrogatePair(k, c))
          {
            m = Character.toCodePoint(k, c);
            paramByteBuffer.put((byte)(0xF0 | m >>> 18));
            paramByteBuffer.put((byte)(m >>> 12 & 0x3F | 0x80));
            paramByteBuffer.put((byte)(m >>> 6 & 0x3F | 0x80));
            paramByteBuffer.put((byte)(0x80 | 0x3F & m));
            continue;
          }
        }
        paramCharSequence = new StringBuilder();
        paramCharSequence.append("Unpaired surrogate at index ");
        paramCharSequence.append(m - 1);
        throw new IllegalArgumentException(paramCharSequence.toString());
      }
      else
      {
        paramByteBuffer.put((byte)(0x1E0 | k >>> 12));
        paramByteBuffer.put((byte)(k >>> 6 & 0x3F | 0x80));
        paramByteBuffer.put((byte)(0x80 | 0x3F & k));
      }
    }
  }
  
  public static int encodeZigZag32(int paramInt)
  {
    return paramInt << 1 ^ paramInt >> 31;
  }
  
  public static long encodeZigZag64(long paramLong)
  {
    return paramLong << 1 ^ paramLong >> 63;
  }
  
  private static int encodedLength(CharSequence paramCharSequence)
  {
    int i = paramCharSequence.length();
    int j = i;
    int m;
    int n;
    for (int k = 0;; k++)
    {
      m = j;
      n = k;
      if (k >= i) {
        break;
      }
      m = j;
      n = k;
      if (paramCharSequence.charAt(k) >= '') {
        break;
      }
    }
    for (;;)
    {
      k = m;
      if (n >= i) {
        break label100;
      }
      k = paramCharSequence.charAt(n);
      if (k >= 2048) {
        break;
      }
      m += (127 - k >>> 31);
      n++;
    }
    k = m + encodedLengthGeneral(paramCharSequence, n);
    label100:
    if (k >= i) {
      return k;
    }
    paramCharSequence = new StringBuilder();
    paramCharSequence.append("UTF-8 length does not fit in int: ");
    paramCharSequence.append(k + 4294967296L);
    throw new IllegalArgumentException(paramCharSequence.toString());
  }
  
  private static int encodedLengthGeneral(CharSequence paramCharSequence, int paramInt)
  {
    int i = paramCharSequence.length();
    int j = 0;
    while (paramInt < i)
    {
      int k = paramCharSequence.charAt(paramInt);
      int m;
      if (k < 2048)
      {
        j += (127 - k >>> 31);
        m = paramInt;
      }
      else
      {
        int n = j + 2;
        m = paramInt;
        j = n;
        if (55296 <= k)
        {
          m = paramInt;
          j = n;
          if (k <= 57343) {
            if (Character.codePointAt(paramCharSequence, paramInt) >= 65536)
            {
              m = paramInt + 1;
              j = n;
            }
            else
            {
              paramCharSequence = new StringBuilder();
              paramCharSequence.append("Unpaired surrogate at index ");
              paramCharSequence.append(paramInt);
              throw new IllegalArgumentException(paramCharSequence.toString());
            }
          }
        }
      }
      paramInt = m + 1;
    }
    return j;
  }
  
  public static CodedOutputByteBufferNano newInstance(byte[] paramArrayOfByte)
  {
    return newInstance(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public static CodedOutputByteBufferNano newInstance(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return new CodedOutputByteBufferNano(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void checkNoSpaceLeft()
  {
    if (spaceLeft() == 0) {
      return;
    }
    throw new IllegalStateException("Did not write as much data as expected.");
  }
  
  public int position()
  {
    return buffer.position();
  }
  
  public void reset()
  {
    buffer.clear();
  }
  
  public int spaceLeft()
  {
    return buffer.remaining();
  }
  
  public void writeBool(int paramInt, boolean paramBoolean)
    throws IOException
  {
    writeTag(paramInt, 0);
    writeBoolNoTag(paramBoolean);
  }
  
  public void writeBoolNoTag(boolean paramBoolean)
    throws IOException
  {
    writeRawByte(paramBoolean);
  }
  
  public void writeBytes(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    writeTag(paramInt, 2);
    writeBytesNoTag(paramArrayOfByte);
  }
  
  public void writeBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
    throws IOException
  {
    writeTag(paramInt1, 2);
    writeBytesNoTag(paramArrayOfByte, paramInt2, paramInt3);
  }
  
  public void writeBytesNoTag(byte[] paramArrayOfByte)
    throws IOException
  {
    writeRawVarint32(paramArrayOfByte.length);
    writeRawBytes(paramArrayOfByte);
  }
  
  public void writeBytesNoTag(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    writeRawVarint32(paramInt2);
    writeRawBytes(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public void writeDouble(int paramInt, double paramDouble)
    throws IOException
  {
    writeTag(paramInt, 1);
    writeDoubleNoTag(paramDouble);
  }
  
  public void writeDoubleNoTag(double paramDouble)
    throws IOException
  {
    writeRawLittleEndian64(Double.doubleToLongBits(paramDouble));
  }
  
  public void writeEnum(int paramInt1, int paramInt2)
    throws IOException
  {
    writeTag(paramInt1, 0);
    writeEnumNoTag(paramInt2);
  }
  
  public void writeEnumNoTag(int paramInt)
    throws IOException
  {
    writeRawVarint32(paramInt);
  }
  
  void writeField(int paramInt1, int paramInt2, Object paramObject)
    throws IOException
  {
    switch (paramInt2)
    {
    default: 
      paramObject = new StringBuilder();
      paramObject.append("Unknown type: ");
      paramObject.append(paramInt2);
      throw new IOException(paramObject.toString());
    case 18: 
      writeSInt64(paramInt1, ((Long)paramObject).longValue());
      break;
    case 17: 
      writeSInt32(paramInt1, ((Integer)paramObject).intValue());
      break;
    case 16: 
      writeSFixed64(paramInt1, ((Long)paramObject).longValue());
      break;
    case 15: 
      writeSFixed32(paramInt1, ((Integer)paramObject).intValue());
      break;
    case 14: 
      writeEnum(paramInt1, ((Integer)paramObject).intValue());
      break;
    case 13: 
      writeUInt32(paramInt1, ((Integer)paramObject).intValue());
      break;
    case 12: 
      writeBytes(paramInt1, (byte[])paramObject);
      break;
    case 11: 
      writeMessage(paramInt1, (MessageNano)paramObject);
      break;
    case 10: 
      writeGroup(paramInt1, (MessageNano)paramObject);
      break;
    case 9: 
      writeString(paramInt1, (String)paramObject);
      break;
    case 8: 
      writeBool(paramInt1, ((Boolean)paramObject).booleanValue());
      break;
    case 7: 
      writeFixed32(paramInt1, ((Integer)paramObject).intValue());
      break;
    case 6: 
      writeFixed64(paramInt1, ((Long)paramObject).longValue());
      break;
    case 5: 
      writeInt32(paramInt1, ((Integer)paramObject).intValue());
      break;
    case 4: 
      writeUInt64(paramInt1, ((Long)paramObject).longValue());
      break;
    case 3: 
      writeInt64(paramInt1, ((Long)paramObject).longValue());
      break;
    case 2: 
      writeFloat(paramInt1, ((Float)paramObject).floatValue());
      break;
    case 1: 
      writeDouble(paramInt1, ((Double)paramObject).doubleValue());
    }
  }
  
  public void writeFixed32(int paramInt1, int paramInt2)
    throws IOException
  {
    writeTag(paramInt1, 5);
    writeFixed32NoTag(paramInt2);
  }
  
  public void writeFixed32NoTag(int paramInt)
    throws IOException
  {
    writeRawLittleEndian32(paramInt);
  }
  
  public void writeFixed64(int paramInt, long paramLong)
    throws IOException
  {
    writeTag(paramInt, 1);
    writeFixed64NoTag(paramLong);
  }
  
  public void writeFixed64NoTag(long paramLong)
    throws IOException
  {
    writeRawLittleEndian64(paramLong);
  }
  
  public void writeFloat(int paramInt, float paramFloat)
    throws IOException
  {
    writeTag(paramInt, 5);
    writeFloatNoTag(paramFloat);
  }
  
  public void writeFloatNoTag(float paramFloat)
    throws IOException
  {
    writeRawLittleEndian32(Float.floatToIntBits(paramFloat));
  }
  
  public void writeGroup(int paramInt, MessageNano paramMessageNano)
    throws IOException
  {
    writeTag(paramInt, 3);
    writeGroupNoTag(paramMessageNano);
    writeTag(paramInt, 4);
  }
  
  public void writeGroupNoTag(MessageNano paramMessageNano)
    throws IOException
  {
    paramMessageNano.writeTo(this);
  }
  
  public void writeInt32(int paramInt1, int paramInt2)
    throws IOException
  {
    writeTag(paramInt1, 0);
    writeInt32NoTag(paramInt2);
  }
  
  public void writeInt32NoTag(int paramInt)
    throws IOException
  {
    if (paramInt >= 0) {
      writeRawVarint32(paramInt);
    } else {
      writeRawVarint64(paramInt);
    }
  }
  
  public void writeInt64(int paramInt, long paramLong)
    throws IOException
  {
    writeTag(paramInt, 0);
    writeInt64NoTag(paramLong);
  }
  
  public void writeInt64NoTag(long paramLong)
    throws IOException
  {
    writeRawVarint64(paramLong);
  }
  
  public void writeMessage(int paramInt, MessageNano paramMessageNano)
    throws IOException
  {
    writeTag(paramInt, 2);
    writeMessageNoTag(paramMessageNano);
  }
  
  public void writeMessageNoTag(MessageNano paramMessageNano)
    throws IOException
  {
    writeRawVarint32(paramMessageNano.getCachedSize());
    paramMessageNano.writeTo(this);
  }
  
  public void writeRawByte(byte paramByte)
    throws IOException
  {
    if (buffer.hasRemaining())
    {
      buffer.put(paramByte);
      return;
    }
    throw new OutOfSpaceException(buffer.position(), buffer.limit());
  }
  
  public void writeRawByte(int paramInt)
    throws IOException
  {
    writeRawByte((byte)paramInt);
  }
  
  public void writeRawBytes(byte[] paramArrayOfByte)
    throws IOException
  {
    writeRawBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public void writeRawBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (buffer.remaining() >= paramInt2)
    {
      buffer.put(paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    throw new OutOfSpaceException(buffer.position(), buffer.limit());
  }
  
  public void writeRawLittleEndian32(int paramInt)
    throws IOException
  {
    if (buffer.remaining() >= 4)
    {
      buffer.putInt(paramInt);
      return;
    }
    throw new OutOfSpaceException(buffer.position(), buffer.limit());
  }
  
  public void writeRawLittleEndian64(long paramLong)
    throws IOException
  {
    if (buffer.remaining() >= 8)
    {
      buffer.putLong(paramLong);
      return;
    }
    throw new OutOfSpaceException(buffer.position(), buffer.limit());
  }
  
  public void writeRawVarint32(int paramInt)
    throws IOException
  {
    for (;;)
    {
      if ((paramInt & 0xFFFFFF80) == 0)
      {
        writeRawByte(paramInt);
        return;
      }
      writeRawByte(paramInt & 0x7F | 0x80);
      paramInt >>>= 7;
    }
  }
  
  public void writeRawVarint64(long paramLong)
    throws IOException
  {
    for (;;)
    {
      if ((0xFFFFFFFFFFFFFF80 & paramLong) == 0L)
      {
        writeRawByte((int)paramLong);
        return;
      }
      writeRawByte((int)paramLong & 0x7F | 0x80);
      paramLong >>>= 7;
    }
  }
  
  public void writeSFixed32(int paramInt1, int paramInt2)
    throws IOException
  {
    writeTag(paramInt1, 5);
    writeSFixed32NoTag(paramInt2);
  }
  
  public void writeSFixed32NoTag(int paramInt)
    throws IOException
  {
    writeRawLittleEndian32(paramInt);
  }
  
  public void writeSFixed64(int paramInt, long paramLong)
    throws IOException
  {
    writeTag(paramInt, 1);
    writeSFixed64NoTag(paramLong);
  }
  
  public void writeSFixed64NoTag(long paramLong)
    throws IOException
  {
    writeRawLittleEndian64(paramLong);
  }
  
  public void writeSInt32(int paramInt1, int paramInt2)
    throws IOException
  {
    writeTag(paramInt1, 0);
    writeSInt32NoTag(paramInt2);
  }
  
  public void writeSInt32NoTag(int paramInt)
    throws IOException
  {
    writeRawVarint32(encodeZigZag32(paramInt));
  }
  
  public void writeSInt64(int paramInt, long paramLong)
    throws IOException
  {
    writeTag(paramInt, 0);
    writeSInt64NoTag(paramLong);
  }
  
  public void writeSInt64NoTag(long paramLong)
    throws IOException
  {
    writeRawVarint64(encodeZigZag64(paramLong));
  }
  
  public void writeString(int paramInt, String paramString)
    throws IOException
  {
    writeTag(paramInt, 2);
    writeStringNoTag(paramString);
  }
  
  public void writeStringNoTag(String paramString)
    throws IOException
  {
    try
    {
      int i = computeRawVarint32Size(paramString.length());
      if (i == computeRawVarint32Size(paramString.length() * 3))
      {
        int j = buffer.position();
        if (buffer.remaining() >= i)
        {
          buffer.position(j + i);
          encode(paramString, buffer);
          int k = buffer.position();
          buffer.position(j);
          writeRawVarint32(k - j - i);
          buffer.position(k);
        }
        else
        {
          paramString = new com/android/framework/protobuf/nano/CodedOutputByteBufferNano$OutOfSpaceException;
          paramString.<init>(j + i, buffer.limit());
          throw paramString;
        }
      }
      else
      {
        writeRawVarint32(encodedLength(paramString));
        encode(paramString, buffer);
      }
      return;
    }
    catch (BufferOverflowException paramString)
    {
      OutOfSpaceException localOutOfSpaceException = new OutOfSpaceException(buffer.position(), buffer.limit());
      localOutOfSpaceException.initCause(paramString);
      throw localOutOfSpaceException;
    }
  }
  
  public void writeTag(int paramInt1, int paramInt2)
    throws IOException
  {
    writeRawVarint32(WireFormatNano.makeTag(paramInt1, paramInt2));
  }
  
  public void writeUInt32(int paramInt1, int paramInt2)
    throws IOException
  {
    writeTag(paramInt1, 0);
    writeUInt32NoTag(paramInt2);
  }
  
  public void writeUInt32NoTag(int paramInt)
    throws IOException
  {
    writeRawVarint32(paramInt);
  }
  
  public void writeUInt64(int paramInt, long paramLong)
    throws IOException
  {
    writeTag(paramInt, 0);
    writeUInt64NoTag(paramLong);
  }
  
  public void writeUInt64NoTag(long paramLong)
    throws IOException
  {
    writeRawVarint64(paramLong);
  }
  
  public static class OutOfSpaceException
    extends IOException
  {
    private static final long serialVersionUID = -6947486886997889499L;
    
    OutOfSpaceException(int paramInt1, int paramInt2)
    {
      super();
    }
  }
}
