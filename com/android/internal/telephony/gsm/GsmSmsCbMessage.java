package com.android.internal.telephony.gsm;

import android.content.Context;
import android.content.res.Resources;
import android.telephony.SmsCbEtwsInfo;
import android.telephony.SmsCbLocation;
import android.telephony.SmsCbMessage;
import android.util.Pair;
import com.android.internal.telephony.GsmAlphabet;
import java.io.UnsupportedEncodingException;

public class GsmSmsCbMessage
{
  private static final char CARRIAGE_RETURN = '\r';
  private static final String[] LANGUAGE_CODES_GROUP_0 = { "de", "en", "it", "fr", "es", "nl", "sv", "da", "pt", "fi", "no", "el", "tr", "hu", "pl", null };
  private static final String[] LANGUAGE_CODES_GROUP_2 = { "cs", "he", "ar", "ru", "is", null, null, null, null, null, null, null, null, null, null, null };
  private static final int PDU_BODY_PAGE_LENGTH = 82;
  
  private GsmSmsCbMessage() {}
  
  public static SmsCbMessage createSmsCbMessage(Context paramContext, SmsCbHeader paramSmsCbHeader, SmsCbLocation paramSmsCbLocation, byte[][] paramArrayOfByte)
    throws IllegalArgumentException
  {
    if (paramSmsCbHeader.isEtwsPrimaryNotification()) {
      return new SmsCbMessage(1, paramSmsCbHeader.getGeographicalScope(), paramSmsCbHeader.getSerialNumber(), paramSmsCbLocation, paramSmsCbHeader.getServiceCategory(), null, getEtwsPrimaryMessage(paramContext, paramSmsCbHeader.getEtwsInfo().getWarningType()), 3, paramSmsCbHeader.getEtwsInfo(), paramSmsCbHeader.getCmasInfo());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    int i = paramArrayOfByte.length;
    int j = 0;
    paramContext = null;
    for (int k = 0; k < i; k++)
    {
      Pair localPair = parseBody(paramSmsCbHeader, paramArrayOfByte[k]);
      paramContext = (String)first;
      localStringBuilder.append((String)second);
    }
    if (paramSmsCbHeader.isEmergencyMessage()) {}
    for (k = 3;; k = j) {
      break;
    }
    return new SmsCbMessage(1, paramSmsCbHeader.getGeographicalScope(), paramSmsCbHeader.getSerialNumber(), paramSmsCbLocation, paramSmsCbHeader.getServiceCategory(), paramContext, localStringBuilder.toString(), k, paramSmsCbHeader.getEtwsInfo(), paramSmsCbHeader.getCmasInfo());
  }
  
  private static String getEtwsPrimaryMessage(Context paramContext, int paramInt)
  {
    paramContext = paramContext.getResources();
    switch (paramInt)
    {
    default: 
      return "";
    case 4: 
      return paramContext.getString(17039931);
    case 3: 
      return paramContext.getString(17039932);
    case 2: 
      return paramContext.getString(17039930);
    case 1: 
      return paramContext.getString(17039933);
    }
    return paramContext.getString(17039929);
  }
  
  private static Pair<String, String> parseBody(SmsCbHeader paramSmsCbHeader, byte[] paramArrayOfByte)
  {
    Pair localPair = null;
    Object localObject = null;
    boolean bool1 = false;
    boolean bool2 = false;
    int i = paramSmsCbHeader.getDataCodingScheme();
    int j = (i & 0xF0) >> 4;
    if (j != 9) {
      switch (j)
      {
      default: 
        switch (j)
        {
        default: 
          bool2 = false;
          j = 1;
          localObject = localPair;
          break;
        case 15: 
          if ((i & 0x4) >> 2 == 1)
          {
            j = 2;
          }
          else
          {
            j = 1;
            bool2 = bool1;
          }
          break;
        }
        break;
      case 4: 
      case 5: 
        switch ((i & 0xC) >> 2)
        {
        default: 
          j = 1;
          bool2 = bool1;
          break;
        case 2: 
          j = 3;
          bool2 = bool1;
          break;
        case 1: 
          j = 2;
          bool2 = bool1;
        }
        break;
      case 3: 
        j = 1;
        bool2 = bool1;
        break;
      case 2: 
        j = 1;
        localObject = LANGUAGE_CODES_GROUP_2[(i & 0xF)];
        bool2 = bool1;
        break;
      case 1: 
        bool1 = true;
        bool2 = true;
        if ((i & 0xF) == 1)
        {
          j = 3;
        }
        else
        {
          j = 1;
          bool2 = bool1;
        }
        break;
      case 0: 
        j = 1;
        localObject = LANGUAGE_CODES_GROUP_0[(i & 0xF)];
        bool2 = bool1;
        if (paramSmsCbHeader.isUmtsFormat())
        {
          int k = paramArrayOfByte[6];
          if (paramArrayOfByte.length >= 83 * k + 7)
          {
            paramSmsCbHeader = new StringBuilder();
            i = 0;
            while (i < k)
            {
              int m = 7 + 83 * i;
              int n = paramArrayOfByte[(m + 82)];
              if (n <= 82)
              {
                localPair = unpackBody(paramArrayOfByte, j, m, n, bool2, (String)localObject);
                localObject = (String)first;
                paramSmsCbHeader.append((String)second);
                i++;
              }
              else
              {
                paramSmsCbHeader = new StringBuilder();
                paramSmsCbHeader.append("Page length ");
                paramSmsCbHeader.append(n);
                paramSmsCbHeader.append(" exceeds maximum value ");
                paramSmsCbHeader.append(82);
                throw new IllegalArgumentException(paramSmsCbHeader.toString());
              }
            }
            return new Pair(localObject, paramSmsCbHeader.toString());
          }
          paramSmsCbHeader = new StringBuilder();
          paramSmsCbHeader.append("Pdu length ");
          paramSmsCbHeader.append(paramArrayOfByte.length);
          paramSmsCbHeader.append(" does not match ");
          paramSmsCbHeader.append(k);
          paramSmsCbHeader.append(" pages");
          throw new IllegalArgumentException(paramSmsCbHeader.toString());
        }
        return unpackBody(paramArrayOfByte, j, 6, paramArrayOfByte.length - 6, bool2, (String)localObject);
      }
    }
    paramSmsCbHeader = new StringBuilder();
    paramSmsCbHeader.append("Unsupported GSM dataCodingScheme ");
    paramSmsCbHeader.append(i);
    throw new IllegalArgumentException(paramSmsCbHeader.toString());
  }
  
  private static Pair<String, String> unpackBody(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, String paramString)
  {
    String str1 = null;
    if (paramInt1 != 1)
    {
      if (paramInt1 != 3)
      {
        paramArrayOfByte = str1;
        str1 = paramString;
      }
      else
      {
        int i = paramInt2;
        paramInt1 = paramInt3;
        str1 = paramString;
        if (paramBoolean)
        {
          i = paramInt2;
          paramInt1 = paramInt3;
          str1 = paramString;
          if (paramArrayOfByte.length >= paramInt2 + 2)
          {
            str1 = GsmAlphabet.gsm7BitPackedToString(paramArrayOfByte, paramInt2, 2);
            i = paramInt2 + 2;
            paramInt1 = paramInt3 - 2;
          }
        }
        try
        {
          paramArrayOfByte = new String(paramArrayOfByte, i, 0xFFFE & paramInt1, "utf-16");
        }
        catch (UnsupportedEncodingException paramArrayOfByte)
        {
          throw new IllegalArgumentException("Error decoding UTF-16 message", paramArrayOfByte);
        }
      }
    }
    else
    {
      String str2 = GsmAlphabet.gsm7BitPackedToString(paramArrayOfByte, paramInt2, paramInt3 * 8 / 7);
      paramArrayOfByte = str2;
      str1 = paramString;
      if (paramBoolean)
      {
        paramArrayOfByte = str2;
        str1 = paramString;
        if (str2 != null)
        {
          paramArrayOfByte = str2;
          str1 = paramString;
          if (str2.length() > 2)
          {
            str1 = str2.substring(0, 2);
            paramArrayOfByte = str2.substring(3);
          }
        }
      }
    }
    if (paramArrayOfByte != null) {
      for (paramInt1 = paramArrayOfByte.length() - 1;; paramInt1--)
      {
        paramString = paramArrayOfByte;
        if (paramInt1 < 0) {
          break;
        }
        if (paramArrayOfByte.charAt(paramInt1) != '\r')
        {
          paramString = paramArrayOfByte.substring(0, paramInt1 + 1);
          break;
        }
      }
    }
    paramString = "";
    return new Pair(str1, paramString);
  }
}
