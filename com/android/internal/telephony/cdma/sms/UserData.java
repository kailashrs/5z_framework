package com.android.internal.telephony.cdma.sms;

import android.util.SparseIntArray;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.util.HexDump;

public class UserData
{
  public static final int ASCII_CR_INDEX = 13;
  public static final char[] ASCII_MAP = { 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126 };
  public static final int ASCII_MAP_BASE_INDEX = 32;
  public static final int ASCII_MAP_MAX_INDEX = 32 + ASCII_MAP.length - 1;
  public static final int ASCII_NL_INDEX = 10;
  public static final int ENCODING_7BIT_ASCII = 2;
  public static final int ENCODING_GSM_7BIT_ALPHABET = 9;
  public static final int ENCODING_GSM_DCS = 10;
  public static final int ENCODING_GSM_DCS_16BIT = 2;
  public static final int ENCODING_GSM_DCS_7BIT = 0;
  public static final int ENCODING_GSM_DCS_8BIT = 1;
  public static final int ENCODING_IA5 = 3;
  public static final int ENCODING_IS91_EXTENDED_PROTOCOL = 1;
  public static final int ENCODING_KOREAN = 6;
  public static final int ENCODING_LATIN = 8;
  public static final int ENCODING_LATIN_HEBREW = 7;
  public static final int ENCODING_OCTET = 0;
  public static final int ENCODING_SHIFT_JIS = 5;
  public static final int ENCODING_UNICODE_16 = 4;
  public static final int IS91_MSG_TYPE_CLI = 132;
  public static final int IS91_MSG_TYPE_SHORT_MESSAGE = 133;
  public static final int IS91_MSG_TYPE_SHORT_MESSAGE_FULL = 131;
  public static final int IS91_MSG_TYPE_VOICEMAIL_STATUS = 130;
  public static final int PRINTABLE_ASCII_MIN_INDEX = 32;
  static final byte UNENCODABLE_7_BIT_CHAR = 32;
  public static final SparseIntArray charToAscii = new SparseIntArray();
  public int msgEncoding;
  public boolean msgEncodingSet = false;
  public int msgType;
  public int numFields;
  public int paddingBits;
  public byte[] payload;
  public String payloadStr;
  public SmsHeader userDataHeader;
  
  static
  {
    for (int i = 0; i < ASCII_MAP.length; i++) {
      charToAscii.put(ASCII_MAP[i], 32 + i);
    }
    charToAscii.put(10, 10);
    charToAscii.put(13, 13);
  }
  
  public UserData() {}
  
  public static byte[] stringToAscii(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i];
    for (int j = 0; j < i; j++)
    {
      int k = charToAscii.get(paramString.charAt(j), -1);
      if (k == -1) {
        return null;
      }
      arrayOfByte[j] = ((byte)(byte)k);
    }
    return arrayOfByte;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("UserData ");
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("{ msgEncoding=");
    if (msgEncodingSet) {
      localObject = Integer.valueOf(msgEncoding);
    } else {
      localObject = "unset";
    }
    localStringBuilder2.append(localObject);
    localStringBuilder1.append(localStringBuilder2.toString());
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", msgType=");
    ((StringBuilder)localObject).append(msgType);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", paddingBits=");
    ((StringBuilder)localObject).append(paddingBits);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", numFields=");
    ((StringBuilder)localObject).append(numFields);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", userDataHeader=");
    ((StringBuilder)localObject).append(userDataHeader);
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", payload='");
    ((StringBuilder)localObject).append(HexDump.toHexString(payload));
    ((StringBuilder)localObject).append("'");
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(", payloadStr='");
    ((StringBuilder)localObject).append(payloadStr);
    ((StringBuilder)localObject).append("'");
    localStringBuilder1.append(((StringBuilder)localObject).toString());
    localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }
}
