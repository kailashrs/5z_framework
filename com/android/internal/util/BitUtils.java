package com.android.internal.util;

import android.text.TextUtils;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.IntFunction;

public final class BitUtils
{
  private BitUtils() {}
  
  public static long bitAt(int paramInt)
  {
    return 1L << paramInt;
  }
  
  public static int bytesToBEInt(byte[] paramArrayOfByte)
  {
    return (uint8(paramArrayOfByte[0]) << 24) + (uint8(paramArrayOfByte[1]) << 16) + (uint8(paramArrayOfByte[2]) << 8) + uint8(paramArrayOfByte[3]);
  }
  
  public static int bytesToLEInt(byte[] paramArrayOfByte)
  {
    return Integer.reverseBytes(bytesToBEInt(paramArrayOfByte));
  }
  
  public static String flagsToString(int paramInt, IntFunction<String> paramIntFunction)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; paramInt != 0; i++)
    {
      int j = 1 << Integer.numberOfTrailingZeros(paramInt);
      paramInt &= j;
      if (i > 0) {
        localStringBuilder.append(", ");
      }
      localStringBuilder.append((String)paramIntFunction.apply(j));
    }
    TextUtils.wrap(localStringBuilder, "[", "]");
    return localStringBuilder.toString();
  }
  
  public static int getUint16(ByteBuffer paramByteBuffer, int paramInt)
  {
    return uint16(paramByteBuffer.getShort(paramInt));
  }
  
  public static long getUint32(ByteBuffer paramByteBuffer, int paramInt)
  {
    return uint32(paramByteBuffer.getInt(paramInt));
  }
  
  public static int getUint8(ByteBuffer paramByteBuffer, int paramInt)
  {
    return uint8(paramByteBuffer.get(paramInt));
  }
  
  public static boolean isBitSet(long paramLong, int paramInt)
  {
    boolean bool;
    if ((bitAt(paramInt) & paramLong) != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean maskedEquals(byte paramByte1, byte paramByte2, byte paramByte3)
  {
    boolean bool;
    if ((paramByte1 & paramByte3) == (paramByte2 & paramByte3)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean maskedEquals(long paramLong1, long paramLong2, long paramLong3)
  {
    boolean bool;
    if ((paramLong1 & paramLong3) == (paramLong2 & paramLong3)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean maskedEquals(UUID paramUUID1, UUID paramUUID2, UUID paramUUID3)
  {
    if (paramUUID3 == null) {
      return Objects.equals(paramUUID1, paramUUID2);
    }
    boolean bool;
    if ((maskedEquals(paramUUID1.getLeastSignificantBits(), paramUUID2.getLeastSignificantBits(), paramUUID3.getLeastSignificantBits())) && (maskedEquals(paramUUID1.getMostSignificantBits(), paramUUID2.getMostSignificantBits(), paramUUID3.getMostSignificantBits()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean maskedEquals(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    boolean bool = false;
    if ((paramArrayOfByte1 != null) && (paramArrayOfByte2 != null))
    {
      if (paramArrayOfByte1.length == paramArrayOfByte2.length) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool, "Inputs must be of same size");
      if (paramArrayOfByte3 == null) {
        return Arrays.equals(paramArrayOfByte1, paramArrayOfByte2);
      }
      if (paramArrayOfByte1.length == paramArrayOfByte3.length) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool, "Mask must be of same size as inputs");
      for (int i = 0; i < paramArrayOfByte3.length; i++) {
        if (!maskedEquals(paramArrayOfByte1[i], paramArrayOfByte2[i], paramArrayOfByte3[i])) {
          return false;
        }
      }
      return true;
    }
    if (paramArrayOfByte1 == paramArrayOfByte2) {
      bool = true;
    }
    return bool;
  }
  
  public static long packBits(int[] paramArrayOfInt)
  {
    long l = 0L;
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      l |= 1 << paramArrayOfInt[j];
    }
    return l;
  }
  
  public static void put(ByteBuffer paramByteBuffer, int paramInt, byte[] paramArrayOfByte)
  {
    int i = paramByteBuffer.position();
    paramByteBuffer.position(paramInt);
    paramByteBuffer.put(paramArrayOfByte);
    paramByteBuffer.position(i);
  }
  
  public static int uint16(byte paramByte1, byte paramByte2)
  {
    return (paramByte1 & 0xFF) << 8 | paramByte2 & 0xFF;
  }
  
  public static int uint16(short paramShort)
  {
    return 0xFFFF & paramShort;
  }
  
  public static long uint32(int paramInt)
  {
    return paramInt & 0xFFFFFFFF;
  }
  
  public static int uint8(byte paramByte)
  {
    return paramByte & 0xFF;
  }
  
  public static int[] unpackBits(long paramLong)
  {
    int[] arrayOfInt = new int[Long.bitCount(paramLong)];
    int i = 0;
    int j = 0;
    while (paramLong > 0L)
    {
      int k = i;
      if ((paramLong & 1L) == 1L)
      {
        arrayOfInt[i] = j;
        k = i + 1;
      }
      paramLong >>= 1;
      j++;
      i = k;
    }
    return arrayOfInt;
  }
}
