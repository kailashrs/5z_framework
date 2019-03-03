package com.android.internal.telephony.cdma;

import android.hardware.radio.V1_0.CdmaSmsMessage;
import com.android.internal.telephony.cdma.sms.SmsEnvelope;
import java.util.ArrayList;

public class SmsMessageConverter
{
  private static final String LOGGABLE_TAG = "CDMA:SMS";
  static final String LOG_TAG = "SmsMessageConverter";
  private static final boolean VDBG = false;
  
  public SmsMessageConverter() {}
  
  public static SmsMessage newCdmaSmsMessageFromRil(CdmaSmsMessage paramCdmaSmsMessage)
  {
    SmsEnvelope localSmsEnvelope = new SmsEnvelope();
    com.android.internal.telephony.cdma.sms.CdmaSmsAddress localCdmaSmsAddress = new com.android.internal.telephony.cdma.sms.CdmaSmsAddress();
    com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress localCdmaSmsSubaddress = new com.android.internal.telephony.cdma.sms.CdmaSmsSubaddress();
    teleService = teleserviceId;
    boolean bool = isServicePresent;
    int i = 0;
    if (bool) {
      messageType = 1;
    } else if (teleService == 0) {
      messageType = 2;
    } else {
      messageType = 0;
    }
    serviceCategory = serviceCategory;
    int j = address.digitMode;
    digitMode = ((byte)(0xFF & j));
    numberMode = ((byte)(address.numberMode & 0xFF));
    ton = address.numberType;
    numberPlan = ((byte)(0xFF & address.numberPlan));
    int k = (byte)address.digits.size();
    numberOfDigits = k;
    byte[] arrayOfByte = new byte[k];
    for (int m = 0; m < k; m++)
    {
      arrayOfByte[m] = ((Byte)address.digits.get(m)).byteValue();
      if (j == 0) {
        arrayOfByte[m] = SmsMessage.convertDtmfToAscii(arrayOfByte[m]);
      }
    }
    origBytes = arrayOfByte;
    type = subAddress.subaddressType;
    odd = ((byte)(byte)subAddress.odd);
    k = (byte)subAddress.digits.size();
    m = k;
    if (k < 0) {
      m = 0;
    }
    arrayOfByte = new byte[m];
    for (k = 0; k < m; k++) {
      arrayOfByte[k] = ((Byte)subAddress.digits.get(k)).byteValue();
    }
    origBytes = arrayOfByte;
    k = bearerData.size();
    m = k;
    if (k < 0) {
      m = 0;
    }
    arrayOfByte = new byte[m];
    for (k = i; k < m; k++) {
      arrayOfByte[k] = ((Byte)bearerData.get(k)).byteValue();
    }
    bearerData = arrayOfByte;
    origAddress = localCdmaSmsAddress;
    origSubaddress = localCdmaSmsSubaddress;
    return new SmsMessage(localCdmaSmsAddress, localSmsEnvelope);
  }
  
  public static android.telephony.SmsMessage newSmsMessageFromCdmaSmsMessage(CdmaSmsMessage paramCdmaSmsMessage)
  {
    return new android.telephony.SmsMessage(newCdmaSmsMessageFromRil(paramCdmaSmsMessage));
  }
}
