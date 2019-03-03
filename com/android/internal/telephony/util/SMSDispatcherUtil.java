package com.android.internal.telephony.util;

import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase.SubmitPduBase;

public final class SMSDispatcherUtil
{
  private SMSDispatcherUtil() {}
  
  public static GsmAlphabet.TextEncodingDetails calculateLength(boolean paramBoolean1, CharSequence paramCharSequence, boolean paramBoolean2)
  {
    if (paramBoolean1) {
      return calculateLengthCdma(paramCharSequence, paramBoolean2);
    }
    return calculateLengthGsm(paramCharSequence, paramBoolean2);
  }
  
  public static GsmAlphabet.TextEncodingDetails calculateLengthCdma(CharSequence paramCharSequence, boolean paramBoolean)
  {
    return com.android.internal.telephony.cdma.SmsMessage.calculateLength(paramCharSequence, paramBoolean, false);
  }
  
  public static GsmAlphabet.TextEncodingDetails calculateLengthGsm(CharSequence paramCharSequence, boolean paramBoolean)
  {
    return com.android.internal.telephony.gsm.SmsMessage.calculateLength(paramCharSequence, paramBoolean);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean paramBoolean1, String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean2)
  {
    if (paramBoolean1) {
      return getSubmitPduCdma(paramString1, paramString2, paramInt, paramArrayOfByte, paramBoolean2);
    }
    return getSubmitPduGsm(paramString1, paramString2, paramInt, paramArrayOfByte, paramBoolean2);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean paramBoolean1, String paramString1, String paramString2, String paramString3, boolean paramBoolean2, SmsHeader paramSmsHeader)
  {
    if (paramBoolean1) {
      return getSubmitPduCdma(paramString1, paramString2, paramString3, paramBoolean2, paramSmsHeader);
    }
    return getSubmitPduGsm(paramString1, paramString2, paramString3, paramBoolean2);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPdu(boolean paramBoolean1, String paramString1, String paramString2, String paramString3, boolean paramBoolean2, SmsHeader paramSmsHeader, int paramInt1, int paramInt2)
  {
    if (paramBoolean1) {
      return getSubmitPduCdma(paramString1, paramString2, paramString3, paramBoolean2, paramSmsHeader, paramInt1);
    }
    return getSubmitPduGsm(paramString1, paramString2, paramString3, paramBoolean2, paramInt2);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPduCdma(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(paramString1, paramString2, paramInt, paramArrayOfByte, paramBoolean);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPduCdma(String paramString1, String paramString2, String paramString3, boolean paramBoolean, SmsHeader paramSmsHeader)
  {
    return com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean, paramSmsHeader);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPduCdma(String paramString1, String paramString2, String paramString3, boolean paramBoolean, SmsHeader paramSmsHeader, int paramInt)
  {
    return com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean, paramSmsHeader, paramInt);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(paramString1, paramString2, paramInt, paramArrayOfByte, paramBoolean);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    return com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean);
  }
  
  public static SmsMessageBase.SubmitPduBase getSubmitPduGsm(String paramString1, String paramString2, String paramString3, boolean paramBoolean, int paramInt)
  {
    return com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(paramString1, paramString2, paramString3, paramBoolean, paramInt);
  }
}
