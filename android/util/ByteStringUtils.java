package android.util;

public final class ByteStringUtils
{
  private static final char[] HEX_LOWERCASE_ARRAY = "0123456789abcdef".toCharArray();
  private static final char[] HEX_UPPERCASE_ARRAY = "0123456789ABCDEF".toCharArray();
  
  private ByteStringUtils() {}
  
  public static byte[] fromHexToByteArray(String paramString)
  {
    if ((paramString != null) && (paramString.length() != 0) && (paramString.length() % 2 == 0))
    {
      paramString = paramString.toCharArray();
      byte[] arrayOfByte = new byte[paramString.length / 2];
      for (int i = 0; i < arrayOfByte.length; i++) {
        arrayOfByte[i] = ((byte)(byte)(getIndex(paramString[(i * 2)]) << 4 & 0xF0 | getIndex(paramString[(i * 2 + 1)]) & 0xF));
      }
      return arrayOfByte;
    }
    return null;
  }
  
  private static int getIndex(char paramChar)
  {
    int i = 0;
    while (i < HEX_UPPERCASE_ARRAY.length) {
      if ((HEX_UPPERCASE_ARRAY[i] != paramChar) && (HEX_LOWERCASE_ARRAY[i] != paramChar)) {
        i++;
      } else {
        return i;
      }
    }
    return -1;
  }
  
  public static String toHexString(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length != 0) && (paramArrayOfByte.length % 2 == 0))
    {
      int i = paramArrayOfByte.length;
      char[] arrayOfChar = new char[2 * i];
      for (int j = 0; j < i; j++)
      {
        int k = paramArrayOfByte[j] & 0xFF;
        arrayOfChar[(j * 2)] = ((char)HEX_UPPERCASE_ARRAY[(k >>> 4)]);
        arrayOfChar[(j * 2 + 1)] = ((char)HEX_UPPERCASE_ARRAY[(k & 0xF)]);
      }
      return new String(arrayOfChar);
    }
    return null;
  }
}
