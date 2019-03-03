package com.android.internal.telephony.gsm;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.cat.ComprehensionTlvTag;
import com.android.internal.telephony.uicc.IccIoResult;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UsimServiceTable;
import com.android.internal.telephony.uicc.UsimServiceTable.UsimService;

public class UsimDataDownloadHandler
  extends Handler
{
  private static final int BER_SMS_PP_DOWNLOAD_TAG = 209;
  private static final int DEV_ID_NETWORK = 131;
  private static final int DEV_ID_UICC = 129;
  private static final int EVENT_SEND_ENVELOPE_RESPONSE = 2;
  private static final int EVENT_START_DATA_DOWNLOAD = 1;
  private static final int EVENT_WRITE_SMS_COMPLETE = 3;
  private static final String TAG = "UsimDataDownloadHandler";
  private final CommandsInterface mCi;
  
  public UsimDataDownloadHandler(CommandsInterface paramCommandsInterface)
  {
    mCi = paramCommandsInterface;
  }
  
  private void acknowledgeSmsWithError(int paramInt)
  {
    mCi.acknowledgeLastIncomingGsmSms(false, paramInt, null);
  }
  
  private static int getEnvelopeBodyLength(int paramInt1, int paramInt2)
  {
    if (paramInt2 > 127) {
      i = 2;
    } else {
      i = 1;
    }
    int i = paramInt2 + 5 + i;
    paramInt2 = i;
    if (paramInt1 != 0) {
      paramInt2 = i + 2 + paramInt1;
    }
    return paramInt2;
  }
  
  private void handleDataDownload(SmsMessage paramSmsMessage)
  {
    int i = paramSmsMessage.getDataCodingScheme();
    int j = paramSmsMessage.getProtocolIdentifier();
    paramSmsMessage = paramSmsMessage.getPdu();
    int k = paramSmsMessage[0] & 0xFF;
    int m = k + 1;
    int n = paramSmsMessage.length - m;
    int i1 = getEnvelopeBodyLength(k, n);
    if (i1 > 127) {
      i2 = 2;
    } else {
      i2 = 1;
    }
    byte[] arrayOfByte = new byte[i1 + 1 + i2];
    int i3 = 0 + 1;
    arrayOfByte[0] = ((byte)-47);
    int i2 = i3;
    if (i1 > 127)
    {
      arrayOfByte[i3] = ((byte)-127);
      i2 = i3 + 1;
    }
    i3 = i2 + 1;
    arrayOfByte[i2] = ((byte)(byte)i1);
    i1 = i3 + 1;
    arrayOfByte[i3] = ((byte)(byte)(0x80 | ComprehensionTlvTag.DEVICE_IDENTITIES.value()));
    i2 = i1 + 1;
    arrayOfByte[i1] = ((byte)2);
    i1 = i2 + 1;
    arrayOfByte[i2] = ((byte)-125);
    i3 = i1 + 1;
    arrayOfByte[i1] = ((byte)-127);
    i2 = i3;
    if (k != 0)
    {
      i2 = i3 + 1;
      arrayOfByte[i3] = ((byte)(byte)ComprehensionTlvTag.ADDRESS.value());
      i3 = i2 + 1;
      arrayOfByte[i2] = ((byte)(byte)k);
      System.arraycopy(paramSmsMessage, 1, arrayOfByte, i3, k);
      i2 = i3 + k;
    }
    i3 = i2 + 1;
    arrayOfByte[i2] = ((byte)(byte)(0x80 | ComprehensionTlvTag.SMS_TPDU.value()));
    i2 = i3;
    if (n > 127)
    {
      arrayOfByte[i3] = ((byte)-127);
      i2 = i3 + 1;
    }
    i3 = i2 + 1;
    arrayOfByte[i2] = ((byte)(byte)n);
    System.arraycopy(paramSmsMessage, m, arrayOfByte, i3, n);
    if (i3 + n != arrayOfByte.length)
    {
      Rlog.e("UsimDataDownloadHandler", "startDataDownload() calculated incorrect envelope length, aborting.");
      acknowledgeSmsWithError(255);
      return;
    }
    paramSmsMessage = IccUtils.bytesToHexString(arrayOfByte);
    mCi.sendEnvelopeWithStatus(paramSmsMessage, obtainMessage(2, new int[] { i, j }));
  }
  
  private static boolean is7bitDcs(int paramInt)
  {
    boolean bool;
    if (((paramInt & 0x8C) != 0) && ((paramInt & 0xF4) != 240)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private void sendSmsAckForEnvelopeResponse(IccIoResult paramIccIoResult, int paramInt1, int paramInt2)
  {
    int i = sw1;
    int j = sw2;
    if (((i == 144) && (j == 0)) || (i == 145))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("USIM data download succeeded: ");
      ((StringBuilder)localObject).append(paramIccIoResult.toString());
      Rlog.d("UsimDataDownloadHandler", ((StringBuilder)localObject).toString());
    }
    for (boolean bool = true;; bool = false)
    {
      break;
      if ((i == 147) && (j == 0))
      {
        Rlog.e("UsimDataDownloadHandler", "USIM data download failed: Toolkit busy");
        acknowledgeSmsWithError(212);
        return;
      }
      if ((i != 98) && (i != 99))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unexpected SW1/SW2 response from UICC: ");
        ((StringBuilder)localObject).append(paramIccIoResult.toString());
        Rlog.e("UsimDataDownloadHandler", ((StringBuilder)localObject).toString());
        bool = false;
        break;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("USIM data download failed: ");
      ((StringBuilder)localObject).append(paramIccIoResult.toString());
      Rlog.e("UsimDataDownloadHandler", ((StringBuilder)localObject).toString());
    }
    Object localObject = payload;
    if ((localObject != null) && (localObject.length != 0))
    {
      if (bool)
      {
        paramIccIoResult = new byte[localObject.length + 5];
        i = 0 + 1;
        paramIccIoResult[0] = ((byte)0);
        j = i + 1;
        paramIccIoResult[i] = ((byte)7);
      }
      else
      {
        paramIccIoResult = new byte[localObject.length + 6];
        j = 0 + 1;
        paramIccIoResult[0] = ((byte)0);
        i = j + 1;
        paramIccIoResult[j] = ((byte)-43);
        paramIccIoResult[i] = ((byte)7);
        j = i + 1;
      }
      i = j + 1;
      paramIccIoResult[j] = ((byte)(byte)paramInt2);
      paramInt2 = i + 1;
      paramIccIoResult[i] = ((byte)(byte)paramInt1);
      if (is7bitDcs(paramInt1))
      {
        j = localObject.length * 8 / 7;
        paramInt1 = paramInt2 + 1;
        paramIccIoResult[paramInt2] = ((byte)(byte)j);
      }
      else
      {
        paramInt1 = paramInt2 + 1;
        paramIccIoResult[paramInt2] = ((byte)(byte)localObject.length);
      }
      System.arraycopy((byte[])localObject, 0, paramIccIoResult, paramInt1, localObject.length);
      mCi.acknowledgeIncomingGsmSmsWithPdu(bool, IccUtils.bytesToHexString(paramIccIoResult), null);
      return;
    }
    if (bool) {
      mCi.acknowledgeLastIncomingGsmSms(true, 0, null);
    } else {
      acknowledgeSmsWithError(213);
    }
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject;
    switch (what)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Ignoring unexpected message, what=");
      ((StringBuilder)localObject).append(what);
      Rlog.e("UsimDataDownloadHandler", ((StringBuilder)localObject).toString());
      break;
    case 3: 
      paramMessage = (AsyncResult)obj;
      if (exception == null)
      {
        Rlog.d("UsimDataDownloadHandler", "Successfully wrote SMS-PP message to UICC");
        mCi.acknowledgeLastIncomingGsmSms(true, 0, null);
      }
      else
      {
        Rlog.d("UsimDataDownloadHandler", "Failed to write SMS-PP message to UICC", exception);
        mCi.acknowledgeLastIncomingGsmSms(false, 255, null);
      }
      break;
    case 2: 
      paramMessage = (AsyncResult)obj;
      if (exception != null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("UICC Send Envelope failure, exception: ");
        ((StringBuilder)localObject).append(exception);
        Rlog.e("UsimDataDownloadHandler", ((StringBuilder)localObject).toString());
        acknowledgeSmsWithError(213);
        return;
      }
      localObject = (int[])userObj;
      sendSmsAckForEnvelopeResponse((IccIoResult)result, localObject[0], localObject[1]);
      break;
    case 1: 
      handleDataDownload((SmsMessage)obj);
    }
  }
  
  int handleUsimDataDownload(UsimServiceTable paramUsimServiceTable, SmsMessage paramSmsMessage)
  {
    if ((paramUsimServiceTable != null) && (paramUsimServiceTable.isAvailable(UsimServiceTable.UsimService.DATA_DL_VIA_SMS_PP)))
    {
      Rlog.d("UsimDataDownloadHandler", "Received SMS-PP data download, sending to UICC.");
      return startDataDownload(paramSmsMessage);
    }
    Rlog.d("UsimDataDownloadHandler", "DATA_DL_VIA_SMS_PP service not available, storing message to UICC.");
    paramUsimServiceTable = IccUtils.bytesToHexString(PhoneNumberUtils.networkPortionToCalledPartyBCDWithLength(paramSmsMessage.getServiceCenterAddress()));
    mCi.writeSmsToSim(3, paramUsimServiceTable, IccUtils.bytesToHexString(paramSmsMessage.getPdu()), obtainMessage(3));
    return -1;
  }
  
  public int startDataDownload(SmsMessage paramSmsMessage)
  {
    if (sendMessage(obtainMessage(1, paramSmsMessage))) {
      return -1;
    }
    Rlog.e("UsimDataDownloadHandler", "startDataDownload failed to send message to start data download.");
    return 2;
  }
}
