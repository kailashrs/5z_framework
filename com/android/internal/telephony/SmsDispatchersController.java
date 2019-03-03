package com.android.internal.telephony;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.SmsMessage.MessageClass;
import android.util.Pair;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.cdma.CdmaInboundSmsHandler;
import com.android.internal.telephony.cdma.CdmaSMSDispatcher;
import com.android.internal.telephony.gsm.GsmInboundSmsHandler;
import com.android.internal.telephony.gsm.GsmSMSDispatcher;
import java.util.ArrayList;
import java.util.HashMap;

public class SmsDispatchersController
  extends Handler
{
  private static final int EVENT_IMS_STATE_CHANGED = 12;
  private static final int EVENT_IMS_STATE_DONE = 13;
  private static final int EVENT_RADIO_ON = 11;
  private static final String TAG = "SmsDispatchersController";
  private SMSDispatcher mCdmaDispatcher;
  private CdmaInboundSmsHandler mCdmaInboundSmsHandler;
  private final CommandsInterface mCi;
  private final Context mContext;
  private SMSDispatcher mGsmDispatcher;
  private GsmInboundSmsHandler mGsmInboundSmsHandler;
  private boolean mIms = false;
  private ImsSmsDispatcher mImsSmsDispatcher;
  private String mImsSmsFormat = "unknown";
  private Phone mPhone;
  private final SmsUsageMonitor mUsageMonitor;
  
  public SmsDispatchersController(Phone paramPhone, SmsStorageMonitor paramSmsStorageMonitor, SmsUsageMonitor paramSmsUsageMonitor)
  {
    Rlog.d("SmsDispatchersController", "SmsDispatchersController created");
    mContext = paramPhone.getContext();
    mUsageMonitor = paramSmsUsageMonitor;
    mCi = mCi;
    mPhone = paramPhone;
    mImsSmsDispatcher = new ImsSmsDispatcher(paramPhone, this);
    mCdmaDispatcher = new CdmaSMSDispatcher(paramPhone, this);
    mGsmInboundSmsHandler = GsmInboundSmsHandler.makeInboundSmsHandler(paramPhone.getContext(), paramSmsStorageMonitor, paramPhone);
    mCdmaInboundSmsHandler = CdmaInboundSmsHandler.makeInboundSmsHandler(paramPhone.getContext(), paramSmsStorageMonitor, paramPhone, (CdmaSMSDispatcher)mCdmaDispatcher);
    mGsmDispatcher = new GsmSMSDispatcher(paramPhone, this, mGsmInboundSmsHandler);
    SmsBroadcastUndelivered.initialize(paramPhone.getContext(), mGsmInboundSmsHandler, mCdmaInboundSmsHandler);
    InboundSmsHandler.registerNewMessageNotificationActionHandler(paramPhone.getContext());
    mCi.registerForOn(this, 11, null);
    mCi.registerForImsNetworkStateChanged(this, 12, null);
  }
  
  private Pair<Boolean, Boolean> handleCdmaStatusReport(SMSDispatcher.SmsTracker paramSmsTracker, String paramString, byte[] paramArrayOfByte)
  {
    paramSmsTracker.updateSentMessageStatus(mContext, 0);
    return new Pair(Boolean.valueOf(triggerDeliveryIntent(paramSmsTracker, paramString, paramArrayOfByte)), Boolean.valueOf(true));
  }
  
  private Pair<Boolean, Boolean> handleGsmStatusReport(SMSDispatcher.SmsTracker paramSmsTracker, String paramString, byte[] paramArrayOfByte)
  {
    com.android.internal.telephony.gsm.SmsMessage localSmsMessage = com.android.internal.telephony.gsm.SmsMessage.newFromCDS(paramArrayOfByte);
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    if (localSmsMessage != null)
    {
      int i = localSmsMessage.getStatus();
      if (i < 64)
      {
        bool1 = bool2;
        if (i >= 32) {}
      }
      else
      {
        paramSmsTracker.updateSentMessageStatus(mContext, i);
        bool1 = true;
      }
      bool3 = triggerDeliveryIntent(paramSmsTracker, paramString, paramArrayOfByte);
    }
    return new Pair(Boolean.valueOf(bool3), Boolean.valueOf(bool1));
  }
  
  private void setImsSmsFormat(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      mImsSmsFormat = "unknown";
      break;
    case 2: 
      mImsSmsFormat = "3gpp2";
      break;
    case 1: 
      mImsSmsFormat = "3gpp";
    }
  }
  
  private boolean triggerDeliveryIntent(SMSDispatcher.SmsTracker paramSmsTracker, String paramString, byte[] paramArrayOfByte)
  {
    paramSmsTracker = mDeliveryIntent;
    Intent localIntent = new Intent();
    localIntent.putExtra("pdu", paramArrayOfByte);
    localIntent.putExtra("format", paramString);
    try
    {
      paramSmsTracker.send(mContext, -1, localIntent);
      return true;
    }
    catch (PendingIntent.CanceledException paramSmsTracker) {}
    return false;
  }
  
  private void updateImsInfo(AsyncResult paramAsyncResult)
  {
    paramAsyncResult = (int[])result;
    boolean bool = true;
    setImsSmsFormat(paramAsyncResult[1]);
    if ((paramAsyncResult[0] != 1) || ("unknown".equals(mImsSmsFormat))) {
      bool = false;
    }
    mIms = bool;
    paramAsyncResult = new StringBuilder();
    paramAsyncResult.append("IMS registration state: ");
    paramAsyncResult.append(mIms);
    paramAsyncResult.append(" format: ");
    paramAsyncResult.append(mImsSmsFormat);
    Rlog.d("SmsDispatchersController", paramAsyncResult.toString());
  }
  
  public void dispose()
  {
    mCi.unregisterForOn(this);
    mCi.unregisterForImsNetworkStateChanged(this);
    mGsmDispatcher.dispose();
    mCdmaDispatcher.dispose();
    mGsmInboundSmsHandler.dispose();
    mCdmaInboundSmsHandler.dispose();
  }
  
  public String getImsSmsFormat()
  {
    return mImsSmsFormat;
  }
  
  public int getPremiumSmsPermission(String paramString)
  {
    return mUsageMonitor.getPremiumSmsPermission(paramString);
  }
  
  public SmsUsageMonitor getUsageMonitor()
  {
    return mUsageMonitor;
  }
  
  public void handleMessage(Message paramMessage)
  {
    switch (what)
    {
    default: 
      if (isCdmaMo()) {
        mCdmaDispatcher.handleMessage(paramMessage);
      }
      break;
    case 13: 
      paramMessage = (AsyncResult)obj;
      if (exception == null)
      {
        updateImsInfo(paramMessage);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("IMS State query failed with exp ");
        localStringBuilder.append(exception);
        Rlog.e("SmsDispatchersController", localStringBuilder.toString());
      }
      break;
    case 11: 
    case 12: 
      mCi.getImsRegistrationState(obtainMessage(13));
      break;
    }
    mGsmDispatcher.handleMessage(paramMessage);
  }
  
  public Pair<Boolean, Boolean> handleSmsStatusReport(SMSDispatcher.SmsTracker paramSmsTracker, String paramString, byte[] paramArrayOfByte)
  {
    if (isCdmaFormat(paramString)) {
      return handleCdmaStatusReport(paramSmsTracker, paramString, paramArrayOfByte);
    }
    return handleGsmStatusReport(paramSmsTracker, paramString, paramArrayOfByte);
  }
  
  @VisibleForTesting
  public void injectSmsPdu(android.telephony.SmsMessage paramSmsMessage, String paramString, SmsInjectionCallback paramSmsInjectionCallback, boolean paramBoolean)
  {
    Rlog.d("SmsDispatchersController", "SmsDispatchersController:injectSmsPdu");
    if (paramSmsMessage == null)
    {
      try
      {
        Rlog.e("SmsDispatchersController", "injectSmsPdu: createFromPdu returned null");
        paramSmsInjectionCallback.onSmsInjectedResult(2);
        return;
      }
      catch (Exception paramSmsMessage) {}
    }
    else
    {
      if ((!paramBoolean) && (paramSmsMessage.getMessageClass() != SmsMessage.MessageClass.CLASS_1))
      {
        Rlog.e("SmsDispatchersController", "injectSmsPdu: not class 1");
        paramSmsInjectionCallback.onSmsInjectedResult(2);
        return;
      }
      AsyncResult localAsyncResult = new android/os/AsyncResult;
      localAsyncResult.<init>(paramSmsInjectionCallback, paramSmsMessage, null);
      StringBuilder localStringBuilder;
      if (paramString.equals("3gpp"))
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("SmsDispatchersController:injectSmsText Sending msg=");
        localStringBuilder.append(paramSmsMessage);
        localStringBuilder.append(", format=");
        localStringBuilder.append(paramString);
        localStringBuilder.append("to mGsmInboundSmsHandler");
        Rlog.i("SmsDispatchersController", localStringBuilder.toString());
        mGsmInboundSmsHandler.sendMessage(8, localAsyncResult);
      }
      else if (paramString.equals("3gpp2"))
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("SmsDispatchersController:injectSmsText Sending msg=");
        localStringBuilder.append(paramSmsMessage);
        localStringBuilder.append(", format=");
        localStringBuilder.append(paramString);
        localStringBuilder.append("to mCdmaInboundSmsHandler");
        Rlog.i("SmsDispatchersController", localStringBuilder.toString());
        mCdmaInboundSmsHandler.sendMessage(8, localAsyncResult);
      }
      else
      {
        paramSmsMessage = new java/lang/StringBuilder;
        paramSmsMessage.<init>();
        paramSmsMessage.append("Invalid pdu format: ");
        paramSmsMessage.append(paramString);
        Rlog.e("SmsDispatchersController", paramSmsMessage.toString());
        paramSmsInjectionCallback.onSmsInjectedResult(2);
      }
      return;
    }
    Rlog.e("SmsDispatchersController", "injectSmsPdu failed: ", paramSmsMessage);
    paramSmsInjectionCallback.onSmsInjectedResult(2);
  }
  
  @VisibleForTesting
  public void injectSmsPdu(byte[] paramArrayOfByte, String paramString, SmsInjectionCallback paramSmsInjectionCallback)
  {
    injectSmsPdu(android.telephony.SmsMessage.createFromPdu(paramArrayOfByte, paramString), paramString, paramSmsInjectionCallback, false);
  }
  
  public boolean isCdmaFormat(String paramString)
  {
    return mCdmaDispatcher.getFormat().equals(paramString);
  }
  
  protected boolean isCdmaMo()
  {
    if (!isIms())
    {
      boolean bool;
      if (2 == mPhone.getPhoneType()) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    return isCdmaFormat(mImsSmsFormat);
  }
  
  public boolean isIms()
  {
    return mIms;
  }
  
  protected void sendData(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2)
  {
    if (mImsSmsDispatcher.isAvailable()) {
      mImsSmsDispatcher.sendData(paramString1, paramString2, paramInt, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
    } else if (isCdmaMo()) {
      mCdmaDispatcher.sendData(paramString1, paramString2, paramInt, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
    } else {
      mGsmDispatcher.sendData(paramString1, paramString2, paramInt, paramArrayOfByte, paramPendingIntent1, paramPendingIntent2);
    }
  }
  
  protected void sendMultipartText(String paramString1, String paramString2, ArrayList<String> paramArrayList, ArrayList<PendingIntent> paramArrayList1, ArrayList<PendingIntent> paramArrayList2, Uri paramUri, String paramString3, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    if (mImsSmsDispatcher.isAvailable()) {
      mImsSmsDispatcher.sendMultipartText(paramString1, paramString2, paramArrayList, paramArrayList1, paramArrayList2, paramUri, paramString3, paramBoolean1, -1, false, -1);
    } else if (isCdmaMo()) {
      mCdmaDispatcher.sendMultipartText(paramString1, paramString2, paramArrayList, paramArrayList1, paramArrayList2, paramUri, paramString3, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
    } else {
      mGsmDispatcher.sendMultipartText(paramString1, paramString2, paramArrayList, paramArrayList1, paramArrayList2, paramUri, paramString3, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
    }
  }
  
  public void sendRetrySms(SMSDispatcher.SmsTracker paramSmsTracker)
  {
    Object localObject1 = mFormat;
    if (2 == mPhone.getPhoneType()) {}
    for (Object localObject2 = mCdmaDispatcher;; localObject2 = mGsmDispatcher)
    {
      localObject2 = ((SMSDispatcher)localObject2).getFormat();
      break;
    }
    Object localObject3 = localObject2;
    if (mImsSmsDispatcher.isAvailable())
    {
      localObject3 = localObject2;
      if (!mIsFallBackRetry) {
        localObject3 = mImsSmsDispatcher.getFormat();
      }
    }
    if (((String)localObject1).equals(localObject3))
    {
      if ((mImsSmsDispatcher.isAvailable()) && (!mIsFallBackRetry))
      {
        Rlog.d("SmsDispatchersController", "old format matched new format processing over IMS");
        mImsSmsDispatcher.sendSms(paramSmsTracker);
        return;
      }
      if (isCdmaFormat((String)localObject3))
      {
        Rlog.d("SmsDispatchersController", "old format matched new format (cdma)");
        mCdmaDispatcher.sendSms(paramSmsTracker);
        return;
      }
      Rlog.d("SmsDispatchersController", "old format matched new format (gsm)");
      mGsmDispatcher.sendSms(paramSmsTracker);
      return;
    }
    localObject1 = paramSmsTracker.getData();
    boolean bool1 = ((HashMap)localObject1).containsKey("scAddr");
    boolean bool2 = false;
    boolean bool3 = false;
    boolean bool4 = false;
    boolean bool5 = false;
    if ((bool1) && (((HashMap)localObject1).containsKey("destAddr")) && ((((HashMap)localObject1).containsKey("text")) || ((((HashMap)localObject1).containsKey("data")) && (((HashMap)localObject1).containsKey("destPort")))))
    {
      String str1 = (String)((HashMap)localObject1).get("scAddr");
      String str2 = (String)((HashMap)localObject1).get("destAddr");
      localObject2 = null;
      if (((HashMap)localObject1).containsKey("text"))
      {
        Rlog.d("SmsDispatchersController", "sms failed was text");
        localObject2 = (String)((HashMap)localObject1).get("text");
        if (isCdmaFormat((String)localObject3))
        {
          Rlog.d("SmsDispatchersController", "old format (gsm) ==> new format (cdma)");
          if (mDeliveryIntent != null) {
            bool5 = true;
          }
        }
        for (localObject2 = com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(str1, str2, (String)localObject2, bool5, null);; localObject2 = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(str1, str2, (String)localObject2, bool5, null))
        {
          break;
          Rlog.d("SmsDispatchersController", "old format (cdma) ==> new format (gsm)");
          bool5 = bool2;
          if (mDeliveryIntent != null) {
            bool5 = true;
          }
        }
      }
      else if (((HashMap)localObject1).containsKey("data"))
      {
        Rlog.d("SmsDispatchersController", "sms failed was data");
        byte[] arrayOfByte = (byte[])((HashMap)localObject1).get("data");
        localObject2 = (Integer)((HashMap)localObject1).get("destPort");
        int i;
        if (isCdmaFormat((String)localObject3))
        {
          Rlog.d("SmsDispatchersController", "old format (gsm) ==> new format (cdma)");
          i = ((Integer)localObject2).intValue();
          if (mDeliveryIntent != null) {
            bool5 = true;
          } else {
            bool5 = bool3;
          }
          localObject2 = com.android.internal.telephony.cdma.SmsMessage.getSubmitPdu(str1, str2, i, arrayOfByte, bool5);
        }
        else
        {
          Rlog.d("SmsDispatchersController", "old format (cdma) ==> new format (gsm)");
          i = ((Integer)localObject2).intValue();
          if (mDeliveryIntent != null) {
            bool5 = true;
          } else {
            bool5 = bool4;
          }
          localObject2 = com.android.internal.telephony.gsm.SmsMessage.getSubmitPdu(str1, str2, i, arrayOfByte, bool5);
        }
      }
      ((HashMap)localObject1).put("smsc", encodedScAddress);
      ((HashMap)localObject1).put("pdu", encodedMessage);
      if ((mImsSmsDispatcher.isAvailable()) && (!mIsFallBackRetry)) {
        localObject2 = mImsSmsDispatcher;
      } else if (isCdmaFormat((String)localObject3)) {
        localObject2 = mCdmaDispatcher;
      } else {
        localObject2 = mGsmDispatcher;
      }
      mFormat = ((SMSDispatcher)localObject2).getFormat();
      ((SMSDispatcher)localObject2).sendSms(paramSmsTracker);
      return;
    }
    Rlog.e("SmsDispatchersController", "sendRetrySms failed to re-encode per missing fields!");
    paramSmsTracker.onFailed(mContext, 1, 0);
  }
  
  public void sendText(String paramString1, String paramString2, String paramString3, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, Uri paramUri, String paramString4, boolean paramBoolean1, int paramInt1, boolean paramBoolean2, int paramInt2)
  {
    if (!mImsSmsDispatcher.isAvailable())
    {
      ImsSmsDispatcher localImsSmsDispatcher = mImsSmsDispatcher;
      String str = paramString1;
      if (!localImsSmsDispatcher.isEmergencySmsSupport(str))
      {
        if (isCdmaMo())
        {
          mCdmaDispatcher.sendText(str, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, paramUri, paramString4, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
          return;
        }
        mGsmDispatcher.sendText(str, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, paramUri, paramString4, paramBoolean1, paramInt1, paramBoolean2, paramInt2);
        return;
      }
    }
    mImsSmsDispatcher.sendText(paramString1, paramString2, paramString3, paramPendingIntent1, paramPendingIntent2, paramUri, paramString4, paramBoolean1, -1, false, -1);
  }
  
  public void setPremiumSmsPermission(String paramString, int paramInt)
  {
    mUsageMonitor.setPremiumSmsPermission(paramString, paramInt);
  }
  
  protected void updatePhoneObject(Phone paramPhone)
  {
    Rlog.d("SmsDispatchersController", "In IMS updatePhoneObject ");
    mCdmaDispatcher.updatePhoneObject(paramPhone);
    mGsmDispatcher.updatePhoneObject(paramPhone);
    mGsmInboundSmsHandler.updatePhoneObject(paramPhone);
    mCdmaInboundSmsHandler.updatePhoneObject(paramPhone);
  }
  
  public static abstract interface SmsInjectionCallback
  {
    public abstract void onSmsInjectedResult(int paramInt);
  }
}
