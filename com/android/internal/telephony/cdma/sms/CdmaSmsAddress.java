package com.android.internal.telephony.cdma.sms;

import android.util.SparseBooleanArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.SmsAddress;
import com.android.internal.util.HexDump;

public class CdmaSmsAddress
  extends SmsAddress
{
  public static final int DIGIT_MODE_4BIT_DTMF = 0;
  public static final int DIGIT_MODE_8BIT_CHAR = 1;
  public static final int NUMBERING_PLAN_ISDN_TELEPHONY = 1;
  public static final int NUMBERING_PLAN_UNKNOWN = 0;
  public static final int NUMBER_MODE_DATA_NETWORK = 1;
  public static final int NUMBER_MODE_NOT_DATA_NETWORK = 0;
  public static final int SMS_ADDRESS_MAX = 36;
  public static final int SMS_SUBADDRESS_MAX = 36;
  public static final int TON_ABBREVIATED = 6;
  public static final int TON_ALPHANUMERIC = 5;
  public static final int TON_INTERNATIONAL_OR_IP = 1;
  public static final int TON_NATIONAL_OR_EMAIL = 2;
  public static final int TON_NETWORK = 3;
  public static final int TON_RESERVED = 7;
  public static final int TON_SUBSCRIBER = 4;
  public static final int TON_UNKNOWN = 0;
  private static final SparseBooleanArray numericCharDialableMap;
  private static final char[] numericCharsDialable = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 42, 35 };
  private static final char[] numericCharsSugar = { 40, 41, 32, 45, 43, 46, 47, 92 };
  public int digitMode;
  public int numberMode;
  public int numberOfDigits;
  public int numberPlan;
  
  static
  {
    numericCharDialableMap = new SparseBooleanArray(numericCharsDialable.length + numericCharsSugar.length);
    for (int i = 0; i < numericCharsDialable.length; i++) {
      numericCharDialableMap.put(numericCharsDialable[i], true);
    }
    for (i = 0; i < numericCharsSugar.length; i++) {
      numericCharDialableMap.put(numericCharsSugar[i], false);
    }
  }
  
  public CdmaSmsAddress() {}
  
  private static String filterNumericSugar(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      int k = numericCharDialableMap.indexOfKey(c);
      if (k < 0) {
        return null;
      }
      if (numericCharDialableMap.valueAt(k)) {
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }
  
  private static String filterWhitespace(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if ((c != ' ') && (c != '\r') && (c != '\n') && (c != '\t')) {
        localStringBuilder.append(c);
      }
    }
    return localStringBuilder.toString();
  }
  
  public static CdmaSmsAddress parse(String paramString)
  {
    CdmaSmsAddress localCdmaSmsAddress = new CdmaSmsAddress();
    address = paramString;
    ton = 0;
    digitMode = 0;
    numberPlan = 0;
    numberMode = 0;
    Object localObject = filterNumericSugar(paramString);
    if ((!paramString.contains("+")) && (localObject != null))
    {
      paramString = parseToDtmf((String)localObject);
    }
    else
    {
      digitMode = 1;
      numberMode = 1;
      String str = filterWhitespace(paramString);
      if (paramString.contains("@"))
      {
        ton = 2;
        localObject = str;
      }
      else
      {
        localObject = str;
        if (paramString.contains("+"))
        {
          localObject = str;
          if (filterNumericSugar(paramString) != null)
          {
            ton = 1;
            numberPlan = 1;
            numberMode = 0;
            localObject = filterNumericSugar(paramString);
          }
        }
      }
      paramString = UserData.stringToAscii((String)localObject);
    }
    if (paramString == null) {
      return null;
    }
    origBytes = paramString;
    numberOfDigits = paramString.length;
    return localCdmaSmsAddress;
  }
  
  @VisibleForTesting
  public static byte[] parseToDtmf(String paramString)
  {
    int i = paramString.length();
    byte[] arrayOfByte = new byte[i];
    int j = 0;
    while (j < i)
    {
      int k = paramString.charAt(j);
      if ((k >= 49) && (k <= 57))
      {
        k -= 48;
      }
      else if (k == 48)
      {
        k = 10;
      }
      else if (k == 42)
      {
        k = 11;
      }
      else
      {
        if (k != 35) {
          break label95;
        }
        k = 12;
      }
      arrayOfByte[j] = ((byte)(byte)k);
      j++;
      continue;
      label95:
      return null;
    }
    return arrayOfByte;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("CdmaSmsAddress ");
    StringBuilder localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append("{ digitMode=");
    localStringBuilder2.append(digitMode);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", numberMode=");
    localStringBuilder2.append(numberMode);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", numberPlan=");
    localStringBuilder2.append(numberPlan);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", numberOfDigits=");
    localStringBuilder2.append(numberOfDigits);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", ton=");
    localStringBuilder2.append(ton);
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", address=\"");
    localStringBuilder2.append(address);
    localStringBuilder2.append("\"");
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder2 = new StringBuilder();
    localStringBuilder2.append(", origBytes=");
    localStringBuilder2.append(HexDump.toHexString(origBytes));
    localStringBuilder1.append(localStringBuilder2.toString());
    localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }
}
