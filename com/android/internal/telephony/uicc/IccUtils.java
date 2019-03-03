package com.android.internal.telephony.uicc;

import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.telephony.Rlog;
import com.android.internal.telephony.EncodeException;
import com.android.internal.telephony.GsmAlphabet;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class IccUtils
{
  private static final char[] HEX_CHARS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
  static final String LOG_TAG = "IccUtils";
  
  public IccUtils() {}
  
  public static String adnStringFieldToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 == 0) {
      return "";
    }
    int i;
    if ((paramInt2 >= 1) && (paramArrayOfByte[paramInt1] == Byte.MIN_VALUE))
    {
      i = (paramInt2 - 1) / 2;
      localObject1 = null;
      try
      {
        String str = new java/lang/String;
        str.<init>(paramArrayOfByte, paramInt1 + 1, i * 2, "utf-16be");
        localObject1 = str;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Rlog.e("IccUtils", "implausible UnsupportedEncodingException", localUnsupportedEncodingException);
      }
      if (localObject1 != null)
      {
        for (paramInt1 = ((String)localObject1).length(); (paramInt1 > 0) && (((String)localObject1).charAt(paramInt1 - 1) == 65535); paramInt1--) {}
        return ((String)localObject1).substring(0, paramInt1);
      }
    }
    int j = 0;
    int k = 0;
    int m = 0;
    int n;
    int i1;
    int i2;
    if ((paramInt2 >= 3) && (paramArrayOfByte[paramInt1] == -127))
    {
      n = paramArrayOfByte[(paramInt1 + 1)] & 0xFF;
      i = n;
      if (n > paramInt2 - 3) {
        i = paramInt2 - 3;
      }
      i1 = (char)((paramArrayOfByte[(paramInt1 + 2)] & 0xFF) << 7);
      n = paramInt1 + 3;
      i2 = 1;
    }
    else
    {
      i2 = j;
      i1 = k;
      i = m;
      n = paramInt1;
      if (paramInt2 >= 4)
      {
        i2 = j;
        i1 = k;
        i = m;
        n = paramInt1;
        if (paramArrayOfByte[paramInt1] == -126)
        {
          n = paramArrayOfByte[(paramInt1 + 1)] & 0xFF;
          i = n;
          if (n > paramInt2 - 4) {
            i = paramInt2 - 4;
          }
          i1 = (char)((paramArrayOfByte[(paramInt1 + 2)] & 0xFF) << 8 | paramArrayOfByte[(paramInt1 + 3)] & 0xFF);
          n = paramInt1 + 4;
          i2 = 1;
        }
      }
    }
    if (i2 != 0)
    {
      localObject1 = new StringBuilder();
      while (i > 0)
      {
        paramInt1 = i;
        paramInt2 = n;
        if (paramArrayOfByte[n] < 0)
        {
          ((StringBuilder)localObject1).append((char)((paramArrayOfByte[n] & 0x7F) + i1));
          paramInt2 = n + 1;
          paramInt1 = i - 1;
        }
        for (i = 0; (i < paramInt1) && (paramArrayOfByte[(paramInt2 + i)] >= 0); i++) {}
        ((StringBuilder)localObject1).append(GsmAlphabet.gsm8BitUnpackedToString(paramArrayOfByte, paramInt2, i));
        n = paramInt2 + i;
        i = paramInt1 - i;
      }
      return ((StringBuilder)localObject1).toString();
    }
    Object localObject2 = Resources.getSystem();
    Object localObject1 = "";
    try
    {
      localObject2 = ((Resources)localObject2).getString(17040078);
      localObject1 = localObject2;
    }
    catch (Resources.NotFoundException localNotFoundException) {}
    return GsmAlphabet.gsm8BitUnpackedToString(paramArrayOfByte, n, paramInt2, ((String)localObject1).trim());
  }
  
  public static String bcdPlmnToString(byte[] paramArrayOfByte, int paramInt)
  {
    if (paramInt + 3 > paramArrayOfByte.length) {
      return null;
    }
    int i = (byte)(paramArrayOfByte[(0 + paramInt)] << 4 | paramArrayOfByte[(0 + paramInt)] >> 4 & 0xF);
    int j = (byte)(paramArrayOfByte[(1 + paramInt)] << 4 | paramArrayOfByte[(2 + paramInt)] & 0xF);
    int k = paramArrayOfByte[(2 + paramInt)];
    String str = bytesToHexString(new byte[] { i, j, (byte)(paramArrayOfByte[(1 + paramInt)] >> 4 & 0xF | k & 0xF0) });
    paramArrayOfByte = str;
    if (str.contains("F")) {
      paramArrayOfByte = str.replaceAll("F", "");
    }
    return paramArrayOfByte;
  }
  
  public static void bcdToBytes(String paramString, byte[] paramArrayOfByte)
  {
    Object localObject = paramString;
    if (paramString.length() % 2 != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("0");
      localObject = ((StringBuilder)localObject).toString();
    }
    int i = Math.min(paramArrayOfByte.length * 2, ((String)localObject).length());
    int j = 0;
    for (int k = 0; j + 1 < i; k++)
    {
      paramArrayOfByte[k] = ((byte)(byte)(charToByte(((String)localObject).charAt(j + 1)) << 4 | charToByte(((String)localObject).charAt(j))));
      j += 2;
    }
  }
  
  public static byte[] bcdToBytes(String paramString)
  {
    byte[] arrayOfByte = new byte[(paramString.length() + 1) / 2];
    bcdToBytes(paramString, arrayOfByte);
    return arrayOfByte;
  }
  
  public static String bcdToString(byte[] paramArrayOfByte)
  {
    return bcdToString(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public static String bcdToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramInt2 * 2);
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++)
    {
      int j = paramArrayOfByte[i] & 0xF;
      if (j > 9) {
        break;
      }
      localStringBuilder.append((char)(48 + j));
      j = paramArrayOfByte[i] >> 4 & 0xF;
      if (j != 15)
      {
        if (j > 9) {
          break;
        }
        localStringBuilder.append((char)(48 + j));
      }
    }
    return localStringBuilder.toString();
  }
  
  public static String bchToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramInt2 * 2);
    for (int i = paramInt1; i < paramInt1 + paramInt2; i++)
    {
      int j = paramArrayOfByte[i];
      localStringBuilder.append(HEX_CHARS[(j & 0xF)]);
      j = paramArrayOfByte[i];
      localStringBuilder.append(HEX_CHARS[(j >> 4 & 0xF)]);
    }
    return localStringBuilder.toString();
  }
  
  private static int bitToRGB(int paramInt)
  {
    if (paramInt == 1) {
      return -1;
    }
    return -16777216;
  }
  
  private static int byteNumForInt(int paramInt, boolean paramBoolean)
  {
    if (paramInt >= 0)
    {
      if (paramBoolean)
      {
        if (paramInt <= 127) {
          return 1;
        }
        if (paramInt <= 32767) {
          return 2;
        }
        if (paramInt <= 8388607) {
          return 3;
        }
      }
      else
      {
        if (paramInt <= 255) {
          return 1;
        }
        if (paramInt <= 65535) {
          return 2;
        }
        if (paramInt <= 16777215) {
          return 3;
        }
      }
      return 4;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("value must be 0 or positive: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static int byteNumForSignedInt(int paramInt)
  {
    return byteNumForInt(paramInt, true);
  }
  
  public static int byteNumForUnsignedInt(int paramInt)
  {
    return byteNumForInt(paramInt, false);
  }
  
  public static String byteToHex(byte paramByte)
  {
    return new String(new char[] { HEX_CHARS[((paramByte & 0xFF) >>> 4)], HEX_CHARS[(paramByte & 0xF)] });
  }
  
  public static String bytesToHexString(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder(2 * paramArrayOfByte.length);
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      int j = paramArrayOfByte[i];
      localStringBuilder.append(HEX_CHARS[(j >> 4 & 0xF)]);
      j = paramArrayOfByte[i];
      localStringBuilder.append(HEX_CHARS[(0xF & j)]);
    }
    return localStringBuilder.toString();
  }
  
  public static int bytesToInt(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 <= 4)
    {
      if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
      {
        int i = 0;
        for (int j = 0; j < paramInt2; j++) {
          i = i << 8 | paramArrayOfByte[(paramInt1 + j)] & 0xFF;
        }
        if (i >= 0) {
          return i;
        }
        paramArrayOfByte = new StringBuilder();
        paramArrayOfByte.append("src cannot be parsed as a positive integer: ");
        paramArrayOfByte.append(i);
        throw new IllegalArgumentException(paramArrayOfByte.toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Out of the bounds: src=[");
      localStringBuilder.append(paramArrayOfByte.length);
      localStringBuilder.append("], offset=");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", length=");
      localStringBuilder.append(paramInt2);
      throw new IndexOutOfBoundsException(localStringBuilder.toString());
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("length must be <= 4 (only 32-bit integer supported): ");
    paramArrayOfByte.append(paramInt2);
    throw new IllegalArgumentException(paramArrayOfByte.toString());
  }
  
  public static long bytesToRawLong(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramInt2 <= 8)
    {
      if ((paramInt1 >= 0) && (paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
      {
        long l = 0L;
        for (int i = 0; i < paramInt2; i++) {
          l = l << 8 | paramArrayOfByte[(paramInt1 + i)] & 0xFF;
        }
        return l;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Out of the bounds: src=[");
      localStringBuilder.append(paramArrayOfByte.length);
      localStringBuilder.append("], offset=");
      localStringBuilder.append(paramInt1);
      localStringBuilder.append(", length=");
      localStringBuilder.append(paramInt2);
      throw new IndexOutOfBoundsException(localStringBuilder.toString());
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("length must be <= 8 (only 64-bit long supported): ");
    paramArrayOfByte.append(paramInt2);
    throw new IllegalArgumentException(paramArrayOfByte.toString());
  }
  
  public static int cdmaBcdByteToInt(byte paramByte)
  {
    int i = 0;
    if ((paramByte & 0xF0) <= 144) {
      i = (paramByte >> 4 & 0xF) * 10;
    }
    int j = i;
    if ((paramByte & 0xF) <= 9) {
      j = i + (paramByte & 0xF);
    }
    return j;
  }
  
  public static String cdmaBcdToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder(paramInt2);
    int i = 0;
    while (i < paramInt2)
    {
      int j = paramArrayOfByte[paramInt1] & 0xF;
      int k = j;
      if (j > 9) {
        k = 0;
      }
      localStringBuilder.append((char)(48 + k));
      j = i + 1;
      if (j == paramInt2) {
        break;
      }
      k = paramArrayOfByte[paramInt1] >> 4 & 0xF;
      i = k;
      if (k > 9) {
        i = 0;
      }
      localStringBuilder.append((char)(48 + i));
      i = j + 1;
      paramInt1++;
    }
    return localStringBuilder.toString();
  }
  
  private static byte charToByte(char paramChar)
  {
    if ((paramChar >= '0') && (paramChar <= '9')) {
      return (byte)(paramChar - '0');
    }
    if ((paramChar >= 'A') && (paramChar <= 'F')) {
      return (byte)(paramChar - '7');
    }
    if ((paramChar >= 'a') && (paramChar <= 'f')) {
      return (byte)(paramChar - 'W');
    }
    return 0;
  }
  
  public static byte countTrailingZeros(byte paramByte)
  {
    if (paramByte == 0) {
      return 8;
    }
    int i = paramByte & 0xFF;
    paramByte = 7;
    if ((i & 0xF) != 0) {
      paramByte = (byte)(7 - 4);
    }
    byte b1 = paramByte;
    if ((i & 0x33) != 0) {
      b1 = (byte)(paramByte - 2);
    }
    byte b2 = b1;
    if ((i & 0x55) != 0)
    {
      paramByte = (byte)(b1 - 1);
      b2 = paramByte;
    }
    return b2;
  }
  
  private static int[] getCLUT(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    int[] arrayOfInt = new int[paramInt2];
    int i = paramInt1;
    for (int j = 0;; j++)
    {
      int k = i + 1;
      int m = paramArrayOfByte[i];
      int n = k + 1;
      k = paramArrayOfByte[k];
      i = n + 1;
      arrayOfInt[j] = ((m & 0xFF) << 16 | 0xFF000000 | (k & 0xFF) << 8 | paramArrayOfByte[n] & 0xFF);
      if (i >= paramInt2 * 3 + paramInt1) {
        return arrayOfInt;
      }
    }
  }
  
  public static String getDecimalSubstring(String paramString)
  {
    for (int i = 0; (i < paramString.length()) && (Character.isDigit(paramString.charAt(i))); i++) {}
    return paramString.substring(0, i);
  }
  
  public static int gsmBcdByteToInt(byte paramByte)
  {
    int i = 0;
    if ((paramByte & 0xF0) <= 144) {
      i = paramByte >> 4 & 0xF;
    }
    int j = i;
    if ((paramByte & 0xF) <= 9) {
      j = i + (paramByte & 0xF) * 10;
    }
    return j;
  }
  
  public static int hexCharToInt(char paramChar)
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
    localStringBuilder.append("invalid hex char '");
    localStringBuilder.append(paramChar);
    localStringBuilder.append("'");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public static byte[] hexStringToBytes(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i / 2];
    for (int j = 0; j < i; j += 2) {
      arrayOfByte[(j / 2)] = ((byte)(byte)(hexCharToInt(paramString.charAt(j)) << 4 | hexCharToInt(paramString.charAt(j + 1))));
    }
    return arrayOfByte;
  }
  
  private static int intToBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, boolean paramBoolean)
  {
    int i = byteNumForInt(paramInt1, paramBoolean);
    if ((paramInt2 >= 0) && (paramInt2 + i <= paramArrayOfByte.length))
    {
      int j = i - 1;
      int k = paramInt1;
      paramInt1 = j;
      while (paramInt1 >= 0)
      {
        paramArrayOfByte[(paramInt2 + paramInt1)] = ((byte)(byte)(k & 0xFF));
        paramInt1--;
        k >>>= 8;
      }
      return i;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Not enough space to write. Required bytes: ");
    paramArrayOfByte.append(i);
    throw new IndexOutOfBoundsException(paramArrayOfByte.toString());
  }
  
  private static int[] mapTo2OrderBitColor(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    if (8 % paramInt3 != 0)
    {
      Rlog.e("IccUtils", "not event number of color");
      return mapToNon2OrderBitColor(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    }
    int i = 1;
    if (paramInt3 != 4)
    {
      if (paramInt3 != 8) {
        switch (paramInt3)
        {
        default: 
          break;
        case 2: 
          i = 3;
          break;
        case 1: 
          i = 1;
          break;
        }
      } else {
        i = 255;
      }
    }
    else {
      i = 15;
    }
    int[] arrayOfInt = new int[paramInt2];
    int j = 0;
    int k = 8 / paramInt3;
    while (j < paramInt2)
    {
      int m = paramArrayOfByte[paramInt1];
      int n = 0;
      while (n < k)
      {
        arrayOfInt[j] = paramArrayOfInt[(m >> (k - n - 1) * paramInt3 & i)];
        n++;
        j++;
      }
      paramInt1++;
    }
    return arrayOfInt;
  }
  
  private static int[] mapToNon2OrderBitColor(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3)
  {
    if (8 % paramInt3 == 0)
    {
      Rlog.e("IccUtils", "not odd number of color");
      return mapTo2OrderBitColor(paramArrayOfByte, paramInt1, paramInt2, paramArrayOfInt, paramInt3);
    }
    return new int[paramInt2];
  }
  
  public static String networkNameToString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (((paramArrayOfByte[paramInt1] & 0x80) == 128) && (paramInt2 >= 1))
    {
      String str1;
      String str2;
      switch (paramArrayOfByte[paramInt1] >>> 4 & 0x7)
      {
      default: 
        str1 = "";
        break;
      case 1: 
        try
        {
          str1 = new java/lang/String;
          str1.<init>(paramArrayOfByte, paramInt1 + 1, paramInt2 - 1, "utf-16");
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          Rlog.e("IccUtils", "implausible UnsupportedEncodingException", localUnsupportedEncodingException);
          str2 = "";
        }
      case 0: 
        str2 = GsmAlphabet.gsm7BitPackedToString(paramArrayOfByte, paramInt1 + 1, ((paramInt2 - 1) * 8 - (paramArrayOfByte[paramInt1] & 0x7)) / 7);
      }
      paramInt1 = paramArrayOfByte[paramInt1];
      return str2;
    }
    return "";
  }
  
  public static Bitmap parseToBnW(byte[] paramArrayOfByte, int paramInt)
  {
    paramInt = 0 + 1;
    int i = paramArrayOfByte[0] & 0xFF;
    int j = paramInt + 1;
    int k = paramArrayOfByte[paramInt] & 0xFF;
    int m = i * k;
    int[] arrayOfInt = new int[m];
    int n = 0;
    paramInt = 7;
    int i1 = 0;
    while (n < m)
    {
      int i2 = j;
      if (n % 8 == 0)
      {
        i1 = paramArrayOfByte[j];
        paramInt = 7;
        i2 = j + 1;
      }
      arrayOfInt[n] = bitToRGB(i1 >> paramInt & 0x1);
      n++;
      paramInt--;
      j = i2;
    }
    if (n != m) {
      Rlog.e("IccUtils", "parse end and size error");
    }
    return Bitmap.createBitmap(arrayOfInt, i, k, Bitmap.Config.ARGB_8888);
  }
  
  public static Bitmap parseToRGB(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean)
  {
    int i = 0 + 1;
    paramInt = paramArrayOfByte[0] & 0xFF;
    int j = i + 1;
    i = paramArrayOfByte[i] & 0xFF;
    int k = j + 1;
    j = paramArrayOfByte[j] & 0xFF;
    int m = k + 1;
    k = paramArrayOfByte[k] & 0xFF;
    int n = m + 1;
    m = paramArrayOfByte[m];
    int i1 = n + 1;
    int[] arrayOfInt = getCLUT(paramArrayOfByte, (m & 0xFF) << 8 | paramArrayOfByte[n] & 0xFF, k);
    if (true == paramBoolean) {
      arrayOfInt[(k - 1)] = 0;
    }
    if (8 % j == 0) {}
    for (paramArrayOfByte = mapTo2OrderBitColor(paramArrayOfByte, i1, paramInt * i, arrayOfInt, j);; paramArrayOfByte = mapToNon2OrderBitColor(paramArrayOfByte, i1, paramInt * i, arrayOfInt, j)) {
      break;
    }
    return Bitmap.createBitmap(paramArrayOfByte, paramInt, i, Bitmap.Config.RGB_565);
  }
  
  public static int signedIntToBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    return intToBytes(paramInt1, paramArrayOfByte, paramInt2, true);
  }
  
  public static byte[] signedIntToBytes(int paramInt)
  {
    if (paramInt >= 0)
    {
      localObject = new byte[byteNumForSignedInt(paramInt)];
      signedIntToBytes(paramInt, (byte[])localObject, 0);
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("value must be 0 or positive: ");
    ((StringBuilder)localObject).append(paramInt);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  static byte[] stringToAdnStringField(String paramString)
  {
    int i = 0;
    boolean bool = false;
    try
    {
      while (i < paramString.length())
      {
        GsmAlphabet.countGsmSeptets(paramString.charAt(i), true);
        i++;
      }
    }
    catch (EncodeException localEncodeException)
    {
      bool = true;
    }
    return stringToAdnStringField(paramString, bool);
  }
  
  static byte[] stringToAdnStringField(String paramString, boolean paramBoolean)
  {
    if (!paramBoolean) {
      return GsmAlphabet.stringToGsm8BitPacked(paramString);
    }
    byte[] arrayOfByte = paramString.getBytes(Charset.forName("UTF-16BE"));
    paramString = new byte[arrayOfByte.length + 1];
    paramString[0] = ((byte)Byte.MIN_VALUE);
    System.arraycopy(arrayOfByte, 0, paramString, 1, arrayOfByte.length);
    return paramString;
  }
  
  public static String stripTrailingFs(String paramString)
  {
    if (paramString == null) {
      paramString = null;
    } else {
      paramString = paramString.replaceAll("(?i)f*$", "");
    }
    return paramString;
  }
  
  public static int unsignedIntToBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    return intToBytes(paramInt1, paramArrayOfByte, paramInt2, false);
  }
  
  public static byte[] unsignedIntToBytes(int paramInt)
  {
    if (paramInt >= 0)
    {
      localObject = new byte[byteNumForUnsignedInt(paramInt)];
      unsignedIntToBytes(paramInt, (byte[])localObject, 0);
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("value must be 0 or positive: ");
    ((StringBuilder)localObject).append(paramInt);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
}
