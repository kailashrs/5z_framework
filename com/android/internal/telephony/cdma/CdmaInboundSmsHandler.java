package com.android.internal.telephony.cdma;

import android.content.Context;
import android.content.res.Resources;
import android.os.Message;
import com.android.internal.telephony.CellBroadcastHandler;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsConstants.MessageClass;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.WspTypeDecoder;
import com.android.internal.util.HexDump;
import java.util.Arrays;

public class CdmaInboundSmsHandler
  extends InboundSmsHandler
{
  private final boolean mCheckForDuplicatePortsInOmadmWapPush = Resources.getSystem().getBoolean(17956945);
  private byte[] mLastAcknowledgedSmsFingerprint;
  private byte[] mLastDispatchedSmsFingerprint;
  private final CdmaServiceCategoryProgramHandler mServiceCategoryProgramHandler;
  private final CdmaSMSDispatcher mSmsDispatcher;
  
  private CdmaInboundSmsHandler(Context paramContext, SmsStorageMonitor paramSmsStorageMonitor, Phone paramPhone, CdmaSMSDispatcher paramCdmaSMSDispatcher)
  {
    super("CdmaInboundSmsHandler", paramContext, paramSmsStorageMonitor, paramPhone, CellBroadcastHandler.makeCellBroadcastHandler(paramContext, paramPhone));
    mSmsDispatcher = paramCdmaSMSDispatcher;
    mServiceCategoryProgramHandler = CdmaServiceCategoryProgramHandler.makeScpHandler(paramContext, mCi);
    mCi.setOnNewCdmaSms(getHandler(), 1, null);
  }
  
  private static boolean checkDuplicatePortOmadmWapPush(byte[] paramArrayOfByte, int paramInt)
  {
    paramInt += 4;
    byte[] arrayOfByte = new byte[paramArrayOfByte.length - paramInt];
    System.arraycopy(paramArrayOfByte, paramInt, arrayOfByte, 0, arrayOfByte.length);
    paramArrayOfByte = new WspTypeDecoder(arrayOfByte);
    if (!paramArrayOfByte.decodeUintvarInteger(2)) {
      return false;
    }
    if (!paramArrayOfByte.decodeContentType(2 + paramArrayOfByte.getDecodedDataLength())) {
      return false;
    }
    return "application/vnd.syncml.notification".equals(paramArrayOfByte.getValueString());
  }
  
  private void handleVoicemailTeleservice(SmsMessage paramSmsMessage)
  {
    int i = paramSmsMessage.getNumOfVoicemails();
    paramSmsMessage = new StringBuilder();
    paramSmsMessage.append("Voicemail count=");
    paramSmsMessage.append(i);
    log(paramSmsMessage.toString());
    int j;
    if (i < 0)
    {
      j = -1;
    }
    else
    {
      j = i;
      if (i > 99) {
        j = 99;
      }
    }
    mPhone.setVoiceMessageCount(j);
  }
  
  public static CdmaInboundSmsHandler makeInboundSmsHandler(Context paramContext, SmsStorageMonitor paramSmsStorageMonitor, Phone paramPhone, CdmaSMSDispatcher paramCdmaSMSDispatcher)
  {
    paramContext = new CdmaInboundSmsHandler(paramContext, paramSmsStorageMonitor, paramPhone, paramCdmaSMSDispatcher);
    paramContext.start();
    return paramContext;
  }
  
  private int processCdmaWapPdu(byte[] paramArrayOfByte, int paramInt, String paramString1, String paramString2, long paramLong)
  {
    int i = 0 + 1;
    int j = paramArrayOfByte[0] & 0xFF;
    if (j != 0)
    {
      log("Received a WAP SMS which is not WDP. Discard.");
      return 1;
    }
    int k = i + 1;
    int m = paramArrayOfByte[i] & 0xFF;
    i = k + 1;
    int n = paramArrayOfByte[k] & 0xFF;
    if (n >= m)
    {
      paramArrayOfByte = new StringBuilder();
      paramArrayOfByte.append("WDP bad segment #");
      paramArrayOfByte.append(n);
      paramArrayOfByte.append(" expecting 0-");
      paramArrayOfByte.append(m - 1);
      loge(paramArrayOfByte.toString());
      return 1;
    }
    int i1 = 0;
    if (n == 0)
    {
      k = i + 1;
      i1 = paramArrayOfByte[i];
      i = k + 1;
      i1 = (i1 & 0xFF) << 8 | paramArrayOfByte[k] & 0xFF;
      k = i + 1;
      int i2 = paramArrayOfByte[i];
      i = k + 1;
      k = 0xFF & paramArrayOfByte[k] | (i2 & 0xFF) << 8;
      if ((mCheckForDuplicatePortsInOmadmWapPush) && (checkDuplicatePortOmadmWapPush(paramArrayOfByte, i))) {
        i += 4;
      }
    }
    else
    {
      k = 0;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Received WAP PDU. Type = ");
    ((StringBuilder)localObject).append(j);
    ((StringBuilder)localObject).append(", originator = ");
    ((StringBuilder)localObject).append(paramString1);
    ((StringBuilder)localObject).append(", src-port = ");
    ((StringBuilder)localObject).append(i1);
    ((StringBuilder)localObject).append(", dst-port = ");
    ((StringBuilder)localObject).append(k);
    ((StringBuilder)localObject).append(", ID = ");
    ((StringBuilder)localObject).append(paramInt);
    ((StringBuilder)localObject).append(", segment# = ");
    ((StringBuilder)localObject).append(n);
    ((StringBuilder)localObject).append('/');
    ((StringBuilder)localObject).append(m);
    log(((StringBuilder)localObject).toString());
    localObject = new byte[paramArrayOfByte.length - i];
    System.arraycopy(paramArrayOfByte, i, (byte[])localObject, 0, paramArrayOfByte.length - i);
    return addTrackerToRawTableAndSendMessage(TelephonyComponentFactory.getInstance().makeInboundSmsTracker((byte[])localObject, paramLong, k, true, paramString1, paramString2, paramInt, n, m, true, HexDump.toHexString((byte[])localObject)), false);
  }
  
  private static int resultToCause(int paramInt)
  {
    if ((paramInt != -1) && (paramInt != 1))
    {
      switch (paramInt)
      {
      default: 
        return 39;
      case 4: 
        return 4;
      }
      return 35;
    }
    return 0;
  }
  
  protected void acknowledgeLastIncomingSms(boolean paramBoolean, int paramInt, Message paramMessage)
  {
    paramInt = resultToCause(paramInt);
    mPhone.mCi.acknowledgeLastIncomingCdmaSms(paramBoolean, paramInt, paramMessage);
    if (paramInt == 0) {
      mLastAcknowledgedSmsFingerprint = mLastDispatchedSmsFingerprint;
    }
    mLastDispatchedSmsFingerprint = null;
  }
  
  protected int dispatchMessageRadioSpecific(SmsMessageBase paramSmsMessageBase)
  {
    SmsMessage localSmsMessage = (SmsMessage)paramSmsMessageBase;
    if (1 == localSmsMessage.getMessageType()) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      log("Broadcast type message");
      paramSmsMessageBase = localSmsMessage.parseBroadcastSms();
      if (paramSmsMessageBase != null) {
        mCellBroadcastHandler.dispatchSmsMessage(paramSmsMessageBase);
      } else {
        loge("error trying to parse broadcast SMS");
      }
      return 1;
    }
    mLastDispatchedSmsFingerprint = localSmsMessage.getIncomingSmsFingerprint();
    if ((mLastAcknowledgedSmsFingerprint != null) && (Arrays.equals(mLastDispatchedSmsFingerprint, mLastAcknowledgedSmsFingerprint))) {
      return 1;
    }
    localSmsMessage.parseSms();
    int i = localSmsMessage.getTeleService();
    if (i != 65002) {
      if (i != 262144)
      {
        switch (i)
        {
        default: 
          paramSmsMessageBase = new StringBuilder();
          paramSmsMessageBase.append("unsupported teleservice 0x");
          paramSmsMessageBase.append(Integer.toHexString(i));
          loge(paramSmsMessageBase.toString());
          return 4;
        case 4102: 
          mServiceCategoryProgramHandler.dispatchSmsMessage(localSmsMessage);
          return 1;
        case 4100: 
          break;
        case 4098: 
        case 4101: 
          if (!localSmsMessage.isStatusReportMessage()) {
            break;
          }
          mSmsDispatcher.sendStatusReportMessage(localSmsMessage);
          return 1;
        }
      }
      else
      {
        handleVoicemailTeleservice(localSmsMessage);
        return 1;
      }
    }
    if ((!mStorageMonitor.isStorageAvailable()) && (localSmsMessage.getMessageClass() != SmsConstants.MessageClass.CLASS_0)) {
      return 3;
    }
    if (4100 == i) {
      return processCdmaWapPdu(localSmsMessage.getUserData(), mMessageRef, localSmsMessage.getOriginatingAddress(), localSmsMessage.getDisplayOriginatingAddress(), localSmsMessage.getTimestampMillis());
    }
    if (65002 == i)
    {
      if (!localSmsMessage.processCdmaCTWdpHeader(localSmsMessage)) {
        return 1;
      }
      return processCdmaWapPdu(localSmsMessage.getUserData(), mMessageRef, localSmsMessage.getOriginatingAddress(), localSmsMessage.getDisplayOriginatingAddress(), localSmsMessage.getTimestampMillis());
    }
    return dispatchNormalMessage(paramSmsMessageBase);
  }
  
  protected boolean is3gpp2()
  {
    return true;
  }
  
  protected void onQuitting()
  {
    mPhone.mCi.unSetOnNewCdmaSms(getHandler());
    mCellBroadcastHandler.dispose();
    log("unregistered for 3GPP2 SMS");
    super.onQuitting();
  }
  
  protected void onUpdatePhoneObject(Phone paramPhone)
  {
    super.onUpdatePhoneObject(paramPhone);
    mCellBroadcastHandler.updatePhoneObject(paramPhone);
  }
}
