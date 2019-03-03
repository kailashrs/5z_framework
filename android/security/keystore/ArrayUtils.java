package android.security.keystore;

import libcore.util.EmptyArray;

public abstract class ArrayUtils
{
  private ArrayUtils() {}
  
  public static byte[] cloneIfNotEmpty(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
      paramArrayOfByte = (byte[])paramArrayOfByte.clone();
    }
    return paramArrayOfByte;
  }
  
  public static String[] cloneIfNotEmpty(String[] paramArrayOfString)
  {
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0)) {
      paramArrayOfString = (String[])paramArrayOfString.clone();
    }
    return paramArrayOfString;
  }
  
  public static byte[] concat(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2, int paramInt3, int paramInt4)
  {
    if (paramInt2 == 0) {
      return subarray(paramArrayOfByte2, paramInt3, paramInt4);
    }
    if (paramInt4 == 0) {
      return subarray(paramArrayOfByte1, paramInt1, paramInt2);
    }
    byte[] arrayOfByte = new byte[paramInt2 + paramInt4];
    System.arraycopy(paramArrayOfByte1, paramInt1, arrayOfByte, 0, paramInt2);
    System.arraycopy(paramArrayOfByte2, paramInt3, arrayOfByte, paramInt2, paramInt4);
    return arrayOfByte;
  }
  
  public static byte[] concat(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = 0;
    int j;
    if (paramArrayOfByte1 != null) {
      j = paramArrayOfByte1.length;
    } else {
      j = 0;
    }
    if (paramArrayOfByte2 != null) {
      i = paramArrayOfByte2.length;
    }
    for (;;)
    {
      break;
    }
    return concat(paramArrayOfByte1, 0, j, paramArrayOfByte2, 0, i);
  }
  
  public static int[] concat(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if ((paramArrayOfInt1 != null) && (paramArrayOfInt1.length != 0))
    {
      if ((paramArrayOfInt2 != null) && (paramArrayOfInt2.length != 0))
      {
        int[] arrayOfInt = new int[paramArrayOfInt1.length + paramArrayOfInt2.length];
        System.arraycopy(paramArrayOfInt1, 0, arrayOfInt, 0, paramArrayOfInt1.length);
        System.arraycopy(paramArrayOfInt2, 0, arrayOfInt, paramArrayOfInt1.length, paramArrayOfInt2.length);
        return arrayOfInt;
      }
      return paramArrayOfInt1;
    }
    return paramArrayOfInt2;
  }
  
  public static String[] nullToEmpty(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null) {
      paramArrayOfString = EmptyArray.STRING;
    }
    return paramArrayOfString;
  }
  
  public static byte[] subarray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0) {
      return EmptyArray.BYTE;
    }
    if ((paramInt1 == 0) && (paramInt2 == paramArrayOfByte.length)) {
      return paramArrayOfByte;
    }
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
}
