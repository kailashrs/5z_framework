package com.android.internal.telephony.gsm;

import android.os.AsyncResult;
import android.os.Message;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.util.Pair;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SMSDispatcher.SmsTracker;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase.SubmitPduBase;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public final class GsmSMSDispatcher
  extends SMSDispatcher
{
  private static final int EVENT_NEW_SMS_STATUS_REPORT = 100;
  private static final String TAG = "GsmSMSDispatcher";
  private GsmInboundSmsHandler mGsmInboundSmsHandler;
  private AtomicReference<IccRecords> mIccRecords = new AtomicReference();
  private AtomicReference<UiccCardApplication> mUiccApplication = new AtomicReference();
  protected UiccController mUiccController = null;
  
  public GsmSMSDispatcher(Phone paramPhone, SmsDispatchersController paramSmsDispatchersController, GsmInboundSmsHandler paramGsmInboundSmsHandler)
  {
    super(paramPhone, paramSmsDispatchersController);
    mCi.setOnSmsStatus(this, 100, null);
    mGsmInboundSmsHandler = paramGsmInboundSmsHandler;
    mUiccController = UiccController.getInstance();
    mUiccController.registerForIccChanged(this, 15, null);
    Rlog.d("GsmSMSDispatcher", "GsmSMSDispatcher created");
  }
  
  private void handleStatusReport(AsyncResult paramAsyncResult)
  {
    paramAsyncResult = (byte[])result;
    Object localObject = SmsMessage.newFromCDS(paramAsyncResult);
    if (localObject != null)
    {
      int i = mMessageRef;
      int j = 0;
      int k = deliveryPendingList.size();
      while (j < k)
      {
        localObject = (SMSDispatcher.SmsTracker)deliveryPendingList.get(j);
        if (mMessageRef == i)
        {
          if (!((Boolean)mSmsDispatchersController.handleSmsStatusReport((SMSDispatcher.SmsTracker)localObject, getFormat(), paramAsyncResult).second).booleanValue()) {
            break;
          }
          deliveryPendingList.remove(j);
          break;
        }
        j++;
      }
    }
    mCi.acknowledgeLastIncomingGsmSms(true, 1, null);
  }
  
  private void onUpdateIccAvailability()
  {
    if (mUiccController == null) {
      return;
    }
    UiccCardApplication localUiccCardApplication1 = getUiccCardApplication();
    UiccCardApplication localUiccCardApplication2 = (UiccCardApplication)mUiccApplication.get();
    if (localUiccCardApplication2 != localUiccCardApplication1)
    {
      if (localUiccCardApplication2 != null)
      {
        Rlog.d("GsmSMSDispatcher", "Removing stale icc objects.");
        if (mIccRecords.get() != null) {
          ((IccRecords)mIccRecords.get()).unregisterForNewSms(this);
        }
        mIccRecords.set(null);
        mUiccApplication.set(null);
      }
      if (localUiccCardApplication1 != null)
      {
        Rlog.d("GsmSMSDispatcher", "New Uicc application found");
        mUiccApplication.set(localUiccCardApplication1);
        mIccRecords.set(localUiccCardApplication1.getIccRecords());
        if (mIccRecords.get() != null) {
          ((IccRecords)mIccRecords.get()).registerForNewSms(this, 14, null);
        }
      }
    }
  }
  
  protected GsmAlphabet.TextEncodingDetails calculateLength(CharSequence paramCharSequence, boolean paramBoolean)
  {
    return SMSDispatcherUtil.calculateLengthGsm(paramCharSequence, paramBoolean);
  }
  
  public void dispose()
  {
    super.dispose();
    mCi.unSetOnSmsStatus(this);
    mUiccController.unregisterForIccChanged(this);
  }
  
  protected String getFormat()
  {
    return "3gpp";
  }
  
  protected SmsMessageBase.SubmitPduBase getSubmitPdu(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return SMSDispatcherUtil.getSubmitPduGsm(paramString1, paramString2, paramInt, paramArrayOfByte, paramBoolean);
  }
  
  protected SmsMessageBase.SubmitPduBase getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, SmsHeader paramSmsHeader, int paramInt1, int paramInt2)
  {
    return SMSDispatcherUtil.getSubmitPduGsm(paramString1, paramString2, paramString3, paramBoolean, paramInt2);
  }
  
  protected UiccCardApplication getUiccCardApplication()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("GsmSMSDispatcher: subId = ");
    localStringBuilder.append(mPhone.getSubId());
    localStringBuilder.append(" slotId = ");
    localStringBuilder.append(mPhone.getPhoneId());
    Rlog.d("GsmSMSDispatcher", localStringBuilder.toString());
    return mUiccController.getUiccCardApplication(mPhone.getPhoneId(), 1);
  }
  
  public void handleMessage(Message paramMessage)
  {
    int i = what;
    if (i != 100) {
      switch (i)
      {
      default: 
        super.handleMessage(paramMessage);
        break;
      case 15: 
        onUpdateIccAvailability();
        break;
      case 14: 
        mGsmInboundSmsHandler.sendMessage(1, obj);
        break;
      }
    } else {
      handleStatusReport((AsyncResult)obj);
    }
  }
  
  protected void sendSms(SMSDispatcher.SmsTracker paramSmsTracker)
  {
    Object localObject1 = paramSmsTracker.getData();
    byte[] arrayOfByte = (byte[])((HashMap)localObject1).get("pdu");
    if (mRetryCount > 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("sendSms:  mRetryCount=");
      ((StringBuilder)localObject2).append(mRetryCount);
      ((StringBuilder)localObject2).append(" mMessageRef=");
      ((StringBuilder)localObject2).append(mMessageRef);
      ((StringBuilder)localObject2).append(" SS=");
      ((StringBuilder)localObject2).append(mPhone.getServiceState().getState());
      Rlog.d("GsmSMSDispatcher", ((StringBuilder)localObject2).toString());
      if ((arrayOfByte[0] & 0x1) == 1)
      {
        arrayOfByte[0] = ((byte)(byte)(arrayOfByte[0] | 0x4));
        arrayOfByte[1] = ((byte)(byte)mMessageRef);
      }
    }
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("sendSms:  isIms()=");
    ((StringBuilder)localObject2).append(isIms());
    ((StringBuilder)localObject2).append(" mRetryCount=");
    ((StringBuilder)localObject2).append(mRetryCount);
    ((StringBuilder)localObject2).append(" mImsRetry=");
    ((StringBuilder)localObject2).append(mImsRetry);
    ((StringBuilder)localObject2).append(" mMessageRef=");
    ((StringBuilder)localObject2).append(mMessageRef);
    ((StringBuilder)localObject2).append(" mUsesImsServiceForIms=");
    ((StringBuilder)localObject2).append(mUsesImsServiceForIms);
    ((StringBuilder)localObject2).append(" SS=");
    ((StringBuilder)localObject2).append(mPhone.getServiceState().getState());
    Rlog.d("GsmSMSDispatcher", ((StringBuilder)localObject2).toString());
    int i = mPhone.getServiceState().getState();
    if ((!isIms()) && (i != 0))
    {
      paramSmsTracker.onFailed(mContext, getNotInServiceError(i), 0);
      return;
    }
    localObject2 = (byte[])((HashMap)localObject1).get("smsc");
    localObject1 = obtainMessage(2, paramSmsTracker);
    if (((mImsRetry == 0) && (!isIms())) || (mUsesImsServiceForIms))
    {
      if ((mRetryCount == 0) && (mExpectMore)) {
        mCi.sendSMSExpectMore(IccUtils.bytesToHexString((byte[])localObject2), IccUtils.bytesToHexString(arrayOfByte), (Message)localObject1);
      } else {
        mCi.sendSMS(IccUtils.bytesToHexString((byte[])localObject2), IccUtils.bytesToHexString(arrayOfByte), (Message)localObject1);
      }
    }
    else
    {
      mCi.sendImsGsmSms(IccUtils.bytesToHexString((byte[])localObject2), IccUtils.bytesToHexString(arrayOfByte), mImsRetry, mMessageRef, (Message)localObject1);
      mImsRetry += 1;
    }
  }
  
  protected boolean shouldBlockSmsForEcbm()
  {
    return false;
  }
}
