package com.android.internal.telephony.cdma;

import android.os.Message;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.util.Pair;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.GsmAlphabet.TextEncodingDetails;
import com.android.internal.telephony.GsmCdmaCallTracker;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants.State;
import com.android.internal.telephony.SMSDispatcher;
import com.android.internal.telephony.SMSDispatcher.SmsTracker;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsHeader;
import com.android.internal.telephony.SmsMessageBase.SubmitPduBase;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import java.util.ArrayList;
import java.util.HashMap;

public class CdmaSMSDispatcher
  extends SMSDispatcher
{
  private static final String TAG = "CdmaSMSDispatcher";
  private static final boolean VDBG = false;
  
  public CdmaSMSDispatcher(Phone paramPhone, SmsDispatchersController paramSmsDispatchersController)
  {
    super(paramPhone, paramSmsDispatchersController);
    Rlog.d("CdmaSMSDispatcher", "CdmaSMSDispatcher created");
  }
  
  private void handleCdmaStatusReport(SmsMessage paramSmsMessage)
  {
    int i = 0;
    int j = deliveryPendingList.size();
    while (i < j)
    {
      SMSDispatcher.SmsTracker localSmsTracker = (SMSDispatcher.SmsTracker)deliveryPendingList.get(i);
      if (mMessageRef == mMessageRef)
      {
        if (!((Boolean)mSmsDispatchersController.handleSmsStatusReport(localSmsTracker, getFormat(), paramSmsMessage.getPdu()).second).booleanValue()) {
          break;
        }
        deliveryPendingList.remove(i);
        break;
      }
      i++;
    }
  }
  
  protected GsmAlphabet.TextEncodingDetails calculateLength(CharSequence paramCharSequence, boolean paramBoolean)
  {
    return SMSDispatcherUtil.calculateLengthCdma(paramCharSequence, paramBoolean);
  }
  
  public String getFormat()
  {
    return "3gpp2";
  }
  
  protected SmsMessageBase.SubmitPduBase getSubmitPdu(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return SMSDispatcherUtil.getSubmitPduCdma(paramString1, paramString2, paramInt, paramArrayOfByte, paramBoolean);
  }
  
  protected SmsMessageBase.SubmitPduBase getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, SmsHeader paramSmsHeader, int paramInt1, int paramInt2)
  {
    return SMSDispatcherUtil.getSubmitPduCdma(paramString1, paramString2, paramString3, paramBoolean, paramSmsHeader, paramInt1);
  }
  
  protected void handleStatusReport(Object paramObject)
  {
    if ((paramObject instanceof SmsMessage))
    {
      handleCdmaStatusReport((SmsMessage)paramObject);
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("handleStatusReport() called for object type ");
      localStringBuilder.append(paramObject.getClass().getName());
      Rlog.e("CdmaSMSDispatcher", localStringBuilder.toString());
    }
  }
  
  public void sendSms(SMSDispatcher.SmsTracker paramSmsTracker)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("sendSms:  isIms()=");
    ((StringBuilder)localObject).append(isIms());
    ((StringBuilder)localObject).append(" mRetryCount=");
    ((StringBuilder)localObject).append(mRetryCount);
    ((StringBuilder)localObject).append(" mImsRetry=");
    ((StringBuilder)localObject).append(mImsRetry);
    ((StringBuilder)localObject).append(" mMessageRef=");
    ((StringBuilder)localObject).append(mMessageRef);
    ((StringBuilder)localObject).append(" mUsesImsServiceForIms=");
    ((StringBuilder)localObject).append(mUsesImsServiceForIms);
    ((StringBuilder)localObject).append(" SS=");
    ((StringBuilder)localObject).append(mPhone.getServiceState().getState());
    Rlog.d("CdmaSMSDispatcher", ((StringBuilder)localObject).toString());
    int i = mPhone.getServiceState().getState();
    boolean bool1 = isIms();
    boolean bool2 = false;
    if ((!bool1) && (i != 0))
    {
      paramSmsTracker.onFailed(mContext, getNotInServiceError(i), 0);
      return;
    }
    Message localMessage = obtainMessage(2, paramSmsTracker);
    localObject = (byte[])paramSmsTracker.getData().get("pdu");
    i = mPhone.getServiceState().getDataNetworkType();
    if (((i == 14) || ((ServiceState.isLte(i)) && (!mPhone.getServiceStateTracker().isConcurrentVoiceAndDataAllowed()))) && (mPhone.getServiceState().getVoiceNetworkType() == 7) && (mPhone).mCT.mState != PhoneConstants.State.IDLE)) {
      i = 1;
    } else {
      i = 0;
    }
    if (((mImsRetry != 0) || (isIms())) && (i == 0) && (!mUsesImsServiceForIms))
    {
      mCi.sendImsCdmaSms((byte[])localObject, mImsRetry, mMessageRef, localMessage);
      mImsRetry += 1;
    }
    else
    {
      CommandsInterface localCommandsInterface = mCi;
      bool1 = bool2;
      if (mRetryCount == 0)
      {
        bool1 = bool2;
        if (mExpectMore) {
          bool1 = true;
        }
      }
      localCommandsInterface.sendCdmaSms((byte[])localObject, localMessage, bool1);
    }
  }
  
  public void sendStatusReportMessage(SmsMessage paramSmsMessage)
  {
    sendMessage(obtainMessage(10, paramSmsMessage));
  }
  
  protected boolean shouldBlockSmsForEcbm()
  {
    boolean bool;
    if ((mPhone.isInEcm()) && (isCdmaMo()) && (!isIms())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
}
