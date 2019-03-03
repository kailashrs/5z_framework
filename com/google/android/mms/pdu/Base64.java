package com.google.android.mms.pdu;

public class Base64
{
  static final int BASELENGTH = 255;
  static final int FOURBYTE = 4;
  static final byte PAD = 61;
  private static byte[] base64Alphabet = new byte['ÿ'];
  
  static
  {
    for (int i = 0; i < 255; i++) {
      base64Alphabet[i] = ((byte)-1);
    }
    for (i = 90; i >= 65; i--) {
      base64Alphabet[i] = ((byte)(byte)(i - 65));
    }
    for (i = 122; i >= 97; i--) {
      base64Alphabet[i] = ((byte)(byte)(i - 97 + 26));
    }
    for (i = 57; i >= 48; i--) {
      base64Alphabet[i] = ((byte)(byte)(i - 48 + 52));
    }
    base64Alphabet[43] = ((byte)62);
    base64Alphabet[47] = ((byte)63);
  }
  
  public Base64() {}
  
  public static byte[] decodeBase64(byte[] paramArrayOfByte)
  {
    paramArrayOfByte = discardNonBase64(paramArrayOfByte);
    int i = paramArrayOfByte.length;
    int j = 0;
    if (i == 0) {
      return new byte[0];
    }
    int k = paramArrayOfByte.length / 4;
    int m = 0;
    i = paramArrayOfByte.length;
    int n;
    while (paramArrayOfByte[(i - 1)] == 61)
    {
      n = i - 1;
      i = n;
      if (n == 0) {
        return new byte[0];
      }
    }
    byte[] arrayOfByte = new byte[i - k];
    i = m;
    while (j < k)
    {
      n = j * 4;
      int i1 = paramArrayOfByte[(n + 2)];
      int i2 = paramArrayOfByte[(n + 3)];
      m = base64Alphabet[paramArrayOfByte[n]];
      n = base64Alphabet[paramArrayOfByte[(n + 1)]];
      if ((i1 != 61) && (i2 != 61))
      {
        i1 = base64Alphabet[i1];
        i2 = base64Alphabet[i2];
        arrayOfByte[i] = ((byte)(byte)(m << 2 | n >> 4));
        arrayOfByte[(i + 1)] = ((byte)(byte)((n & 0xF) << 4 | i1 >> 2 & 0xF));
        arrayOfByte[(i + 2)] = ((byte)(byte)(i1 << 6 | i2));
      }
      else if (i1 == 61)
      {
        arrayOfByte[i] = ((byte)(byte)(m << 2 | n >> 4));
      }
      else if (i2 == 61)
      {
        i2 = base64Alphabet[i1];
        arrayOfByte[i] = ((byte)(byte)(m << 2 | n >> 4));
        arrayOfByte[(i + 1)] = ((byte)(byte)((n & 0xF) << 4 | i2 >> 2 & 0xF));
      }
      i += 3;
      j++;
    }
    return arrayOfByte;
  }
  
  static byte[] discardNonBase64(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte.length];
    int i = 0;
    int j = 0;
    while (j < paramArrayOfByte.length)
    {
      int k = i;
      if (isBase64(paramArrayOfByte[j]))
      {
        arrayOfByte[i] = paramArrayOfByte[j];
        k = i + 1;
      }
      j++;
      i = k;
    }
    paramArrayOfByte = new byte[i];
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte, 0, i);
    return paramArrayOfByte;
  }
  
  private static boolean isBase64(byte paramByte)
  {
    if (paramByte == 61) {
      return true;
    }
    return base64Alphabet[paramByte] != -1;
  }
}
