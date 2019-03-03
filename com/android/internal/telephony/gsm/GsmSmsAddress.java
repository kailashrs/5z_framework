package com.android.internal.telephony.gsm;

import android.telephony.PhoneNumberUtils;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsAddress;
import java.text.ParseException;

public class GsmSmsAddress
  extends SmsAddress
{
  static final int OFFSET_ADDRESS_LENGTH = 0;
  static final int OFFSET_ADDRESS_VALUE = 2;
  static final int OFFSET_TOA = 1;
  
  public GsmSmsAddress(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws ParseException
  {
    origBytes = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, origBytes, 0, paramInt2);
    int i = origBytes[0] & 0xFF;
    int j = origBytes[1] & 0xFF;
    ton = (j >> 4 & 0x7);
    if ((j & 0x80) == 128)
    {
      if (isAlphanumeric())
      {
        paramInt1 = i * 4 / 7;
        address = GsmAlphabet.gsm7BitPackedToString(origBytes, 2, paramInt1);
      }
      else
      {
        paramInt1 = origBytes[(paramInt2 - 1)];
        if ((i & 0x1) == 1)
        {
          paramArrayOfByte = origBytes;
          i = paramInt2 - 1;
          paramArrayOfByte[i] = ((byte)(byte)(paramArrayOfByte[i] | 0xF0));
        }
        address = PhoneNumberUtils.calledPartyBCDToString(origBytes, 1, paramInt2 - 1, 2);
        origBytes[(paramInt2 - 1)] = ((byte)paramInt1);
      }
      return;
    }
    paramArrayOfByte = new StringBuilder();
    paramArrayOfByte.append("Invalid TOA - high bit must be set. toa = ");
    paramArrayOfByte.append(j);
    throw new ParseException(paramArrayOfByte.toString(), paramInt1 + 1);
  }
  
  public String getAddressString()
  {
    return address;
  }
  
  public boolean isAlphanumeric()
  {
    boolean bool;
    if (ton == 5) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCphsVoiceMessageClear()
  {
    boolean bool;
    if ((isCphsVoiceMessageIndicatorAddress()) && ((origBytes[2] & 0xFF) == 16)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isCphsVoiceMessageIndicatorAddress()
  {
    byte[] arrayOfByte = origBytes;
    boolean bool = false;
    if (((arrayOfByte[0] & 0xFF) == 4) && (isAlphanumeric()) && ((origBytes[1] & 0xF) == 0)) {
      bool = true;
    }
    return bool;
  }
  
  public boolean isCphsVoiceMessageSet()
  {
    boolean bool;
    if ((isCphsVoiceMessageIndicatorAddress()) && ((origBytes[2] & 0xFF) == 17)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isNetworkSpecific()
  {
    boolean bool;
    if (ton == 3) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
