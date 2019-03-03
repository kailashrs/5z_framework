package com.android.internal.telephony.gsm;

import android.content.Context;
import android.os.Message;
import com.android.internal.telephony.CellBroadcastHandler;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.InboundSmsHandler;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsConstants.MessageClass;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsHeader.PortAddrs;
import com.android.internal.telephony.SmsMessageBase;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.VisualVoicemailSmsFilter;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccController;

public class GsmInboundSmsHandler
  extends InboundSmsHandler
{
  private final UsimDataDownloadHandler mDataDownloadHandler;
  
  private GsmInboundSmsHandler(Context paramContext, SmsStorageMonitor paramSmsStorageMonitor, Phone paramPhone)
  {
    super("GsmInboundSmsHandler", paramContext, paramSmsStorageMonitor, paramPhone, GsmCellBroadcastHandler.makeGsmCellBroadcastHandler(paramContext, paramPhone));
    mCi.setOnNewGsmSms(getHandler(), 1, null);
    mDataDownloadHandler = new UsimDataDownloadHandler(mCi);
  }
  
  public static GsmInboundSmsHandler makeInboundSmsHandler(Context paramContext, SmsStorageMonitor paramSmsStorageMonitor, Phone paramPhone)
  {
    paramContext = new GsmInboundSmsHandler(paramContext, paramSmsStorageMonitor, paramPhone);
    paramContext.start();
    return paramContext;
  }
  
  private static int resultToCause(int paramInt)
  {
    if ((paramInt != -1) && (paramInt != 1))
    {
      if (paramInt != 3) {
        return 255;
      }
      return 211;
    }
    return 0;
  }
  
  private void updateMessageWaitingIndicator(int paramInt)
  {
    int i;
    if (paramInt < 0)
    {
      i = -1;
    }
    else
    {
      i = paramInt;
      if (paramInt > 255) {
        i = 255;
      }
    }
    mPhone.setVoiceMessageCount(i);
    IccRecords localIccRecords = UiccController.getInstance().getIccRecords(mPhone.getPhoneId(), 1);
    if (localIccRecords != null)
    {
      log("updateMessageWaitingIndicator: updating SIM Records");
      localIccRecords.setVoiceMessageWaiting(1, i);
    }
    else
    {
      log("updateMessageWaitingIndicator: SIM Records not found");
    }
  }
  
  protected void acknowledgeLastIncomingSms(boolean paramBoolean, int paramInt, Message paramMessage)
  {
    mPhone.mCi.acknowledgeLastIncomingGsmSms(paramBoolean, resultToCause(paramInt), paramMessage);
  }
  
  protected int dispatchMessageRadioSpecific(SmsMessageBase paramSmsMessageBase)
  {
    Object localObject = (SmsMessage)paramSmsMessageBase;
    if (((SmsMessage)localObject).isTypeZero())
    {
      int i = -1;
      paramSmsMessageBase = ((SmsMessage)localObject).getUserDataHeader();
      int j = i;
      if (paramSmsMessageBase != null)
      {
        j = i;
        if (portAddrs != null) {
          j = portAddrs.destPort;
        }
      }
      paramSmsMessageBase = mContext;
      localObject = ((SmsMessage)localObject).getPdu();
      i = mPhone.getSubId();
      VisualVoicemailSmsFilter.filter(paramSmsMessageBase, new byte[][] { localObject }, "3gpp", j, i);
      log("Received short message type 0, Don't display or store it. Send Ack");
      return 1;
    }
    if (((SmsMessage)localObject).isUsimDataDownload())
    {
      paramSmsMessageBase = mPhone.getUsimServiceTable();
      return mDataDownloadHandler.handleUsimDataDownload(paramSmsMessageBase, (SmsMessage)localObject);
    }
    boolean bool = false;
    StringBuilder localStringBuilder;
    if (((SmsMessage)localObject).isMWISetMessage())
    {
      updateMessageWaitingIndicator(((SmsMessage)localObject).getNumOfVoicemails());
      bool = ((SmsMessage)localObject).isMwiDontStore();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Received voice mail indicator set SMS shouldStore=");
      localStringBuilder.append(bool ^ true);
      log(localStringBuilder.toString());
    }
    else if (((SmsMessage)localObject).isMWIClearMessage())
    {
      updateMessageWaitingIndicator(0);
      bool = ((SmsMessage)localObject).isMwiDontStore();
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Received voice mail indicator clear SMS shouldStore=");
      localStringBuilder.append(bool ^ true);
      log(localStringBuilder.toString());
    }
    if (bool) {
      return 1;
    }
    if ((!mStorageMonitor.isStorageAvailable()) && (((SmsMessage)localObject).getMessageClass() != SmsConstants.MessageClass.CLASS_0)) {
      return 3;
    }
    return dispatchNormalMessage(paramSmsMessageBase);
  }
  
  protected boolean is3gpp2()
  {
    return false;
  }
  
  protected void onQuitting()
  {
    mPhone.mCi.unSetOnNewGsmSms(getHandler());
    mCellBroadcastHandler.dispose();
    log("unregistered for 3GPP SMS");
    super.onQuitting();
  }
  
  protected void onUpdatePhoneObject(Phone paramPhone)
  {
    super.onUpdatePhoneObject(paramPhone);
    log("onUpdatePhoneObject: dispose of old CellBroadcastHandler and make a new one");
    mCellBroadcastHandler.dispose();
    mCellBroadcastHandler = GsmCellBroadcastHandler.makeGsmCellBroadcastHandler(mContext, paramPhone);
  }
}
