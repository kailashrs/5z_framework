package com.android.internal.util;

public class HexDump
{
  private static final char[] HEX_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  private static final char[] HEX_LOWER_CASE_DIGITS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
  
  public HexDump() {}
  
  public static StringBuilder appendByteAsHex(StringBuilder paramStringBuilder, byte paramByte, boolean paramBoolean)
  {
    char[] arrayOfChar;
    if (paramBoolean) {
      arrayOfChar = HEX_DIGITS;
    } else {
      arrayOfChar = HEX_LOWER_CASE_DIGITS;
    }
    paramStringBuilder.append(arrayOfChar[(paramByte >> 4 & 0xF)]);
    paramStringBuilder.append(arrayOfChar[(paramByte & 0xF)]);
    return paramStringBuilder;
  }
  
  public static String dumpHexString(byte[] paramArrayOfByte)
  {
    return dumpHexString(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public static String dumpHexString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    byte[] arrayOfByte = new byte[16];
    localStringBuilder.append("\n0x");
    localStringBuilder.append(toHexString(paramInt1));
    int i = 0;
    int j = paramInt1;
    int k;
    for (;;)
    {
      k = 0;
      int m = 0;
      if (j >= paramInt1 + paramInt2) {
        break;
      }
      k = i;
      if (i == 16)
      {
        localStringBuilder.append(" ");
        for (i = m; i < 16; i++) {
          if ((arrayOfByte[i] > 32) && (arrayOfByte[i] < 126)) {
            localStringBuilder.append(new String(arrayOfByte, i, 1));
          } else {
            localStringBuilder.append(".");
          }
        }
        localStringBuilder.append("\n0x");
        localStringBuilder.append(toHexString(j));
        k = 0;
      }
      i = paramArrayOfByte[j];
      localStringBuilder.append(" ");
      localStringBuilder.append(HEX_DIGITS[(i >>> 4 & 0xF)]);
      localStringBuilder.append(HEX_DIGITS[(i & 0xF)]);
      arrayOfByte[k] = ((byte)i);
      j++;
      i = k + 1;
    }
    if (i != 16)
    {
      for (paramInt1 = 0; paramInt1 < (16 - i) * 3 + 1; paramInt1++) {
        localStringBuilder.append(" ");
      }
      for (paramInt1 = k; paramInt1 < i; paramInt1++) {
        if ((arrayOfByte[paramInt1] > 32) && (arrayOfByte[paramInt1] < 126)) {
          localStringBuilder.append(new String(arrayOfByte, paramInt1, 1));
        } else {
          localStringBuilder.append(".");
        }
      }
    }
    return localStringBuilder.toString();
  }
  
  public static byte[] hexStringToByteArray(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i / 2];
    for (int j = 0; j < i; j += 2) {
      arrayOfByte[(j / 2)] = ((byte)(byte)(toByte(paramString.charAt(j)) << 4 | toByte(paramString.charAt(j + 1))));
    }
    return arrayOfByte;
  }
  
  private static int toByte(char paramChar)
  {
    if ((paramChar >= '0') && (paramChar <= '9')) {
      return paramChar - '0';
    }
    if ((paramChar >= 'A') && (paramChar <= 'F')) {
      return paramChar - 'A' + 10;
    }
    if ((paramChar >= 'a') && (paramChar <= 'f')) {
      return paramChar - 'a' + 10;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid hex char '");
    localStringBuilder.append(paramChar);
    localStringBuilder.append("'");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public static byte[] toByteArray(byte paramByte)
  {
    return new byte[] { paramByte };
  }
  
  public static byte[] toByteArray(int paramInt)
  {
    int i = (byte)(paramInt & 0xFF);
    int j = (byte)(paramInt >> 8 & 0xFF);
    int k = (byte)(paramInt >> 16 & 0xFF);
    return new byte[] { (byte)(paramInt >> 24 & 0xFF), k, j, i };
  }
  
  public static String toHexString(byte paramByte)
  {
    return toHexString(toByteArray(paramByte));
  }
  
  public static String toHexString(int paramInt)
  {
    return toHexString(toByteArray(paramInt));
  }
  
  public static String toHexString(byte[] paramArrayOfByte)
  {
    return toHexString(paramArrayOfByte, 0, paramArrayOfByte.length, true);
  }
  
  public static String toHexString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return toHexString(paramArrayOfByte, paramInt1, paramInt2, true);
  }
  
  public static String toHexString(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    char[] arrayOfChar1;
    if (paramBoolean) {
      arrayOfChar1 = HEX_DIGITS;
    } else {
      arrayOfChar1 = HEX_LOWER_CASE_DIGITS;
    }
    char[] arrayOfChar2 = new char[paramInt2 * 2];
    int i = 0;
    for (int j = paramInt1; j < paramInt1 + paramInt2; j++)
    {
      int k = paramArrayOfByte[j];
      int m = i + 1;
      arrayOfChar2[i] = ((char)arrayOfChar1[(k >>> 4 & 0xF)]);
      i = m + 1;
      arrayOfChar2[m] = ((char)arrayOfChar1[(k & 0xF)]);
    }
    return new String(arrayOfChar2);
  }
  
  public static String toHexString(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return toHexString(paramArrayOfByte, 0, paramArrayOfByte.length, paramBoolean);
  }
}
