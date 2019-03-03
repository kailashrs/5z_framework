package com.android.internal.telephony;

import android.content.Context;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.CarrierConfigManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SmsMessage;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.aidl.IImsSmsListener;
import android.telephony.ims.aidl.IImsSmsListener.Stub;
import android.telephony.ims.feature.ImsFeature.Capabilities;
import android.telephony.ims.feature.ImsFeature.CapabilityCallback;
import android.telephony.ims.stub.ImsRegistrationImplBase.Callback;
import android.util.Pair;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.ims.ImsManager.Connector;
import com.android.ims.ImsManager.Connector.Listener;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.telephony.util.SMSDispatcherUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ImsSmsDispatcher
  extends SMSDispatcher
{
  private static final String TAG = "ImsSmsDispacher";
  private ImsFeature.CapabilityCallback mCapabilityCallback = new ImsFeature.CapabilityCallback()
  {
    public void onCapabilitiesStatusChanged(ImsFeature.Capabilities paramAnonymousCapabilities)
    {
      synchronized (mLock)
      {
        ImsSmsDispatcher.access$202(ImsSmsDispatcher.this, paramAnonymousCapabilities.isCapable(8));
        return;
      }
    }
  };
  private final ImsManager.Connector mImsManagerConnector = new ImsManager.Connector(mContext, mPhone.getPhoneId(), new ImsManager.Connector.Listener()
  {
    public void connectionReady(ImsManager arg1)
      throws ImsException
    {
      Rlog.d("ImsSmsDispacher", "ImsManager: connection ready.");
      synchronized (mLock)
      {
        ImsSmsDispatcher.this.setListeners();
        ImsSmsDispatcher.access$502(ImsSmsDispatcher.this, true);
        return;
      }
    }
    
    public void connectionUnavailable()
    {
      Rlog.d("ImsSmsDispacher", "ImsManager: connection unavailable.");
      synchronized (mLock)
      {
        ImsSmsDispatcher.access$502(ImsSmsDispatcher.this, false);
        return;
      }
    }
  });
  private final IImsSmsListener mImsSmsListener = new IImsSmsListener.Stub()
  {
    public void onSendSmsResult(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4)
      throws RemoteException
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onSendSmsResult token=");
      ((StringBuilder)localObject).append(paramAnonymousInt1);
      ((StringBuilder)localObject).append(" messageRef=");
      ((StringBuilder)localObject).append(paramAnonymousInt2);
      ((StringBuilder)localObject).append(" status=");
      ((StringBuilder)localObject).append(paramAnonymousInt3);
      ((StringBuilder)localObject).append(" reason=");
      ((StringBuilder)localObject).append(paramAnonymousInt4);
      Rlog.d("ImsSmsDispacher", ((StringBuilder)localObject).toString());
      SMSDispatcher.SmsTracker localSmsTracker = (SMSDispatcher.SmsTracker)mTrackers.get(Integer.valueOf(paramAnonymousInt1));
      if (localSmsTracker != null)
      {
        mMessageRef = paramAnonymousInt2;
        switch (paramAnonymousInt3)
        {
        default: 
          break;
        case 3: 
        case 4: 
          if (mRetryCount < 3)
          {
            mRetryCount += 1;
            if (paramAnonymousInt3 == 4) {
              mIsFallBackRetry = true;
            }
            localObject = obtainMessage(3, localSmsTracker);
            sendMessageDelayed((Message)localObject, 2000L);
          }
          else
          {
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("onSendSmsResult Max retrys reaached: ");
            ((StringBuilder)localObject).append(mRetryCount);
            Rlog.e("ImsSmsDispacher", ((StringBuilder)localObject).toString());
            localSmsTracker.onFailed(mContext, 1, 0);
            mTrackers.remove(Integer.valueOf(paramAnonymousInt1));
          }
          break;
        case 2: 
          localSmsTracker.onFailed(mContext, paramAnonymousInt4, 0);
          mTrackers.remove(Integer.valueOf(paramAnonymousInt1));
          break;
        case 1: 
          localSmsTracker.onSent(mContext);
        }
        return;
      }
      throw new IllegalArgumentException("Invalid token.");
    }
    
    public void onSmsReceived(int paramAnonymousInt, String paramAnonymousString, byte[] paramAnonymousArrayOfByte)
      throws RemoteException
    {
      Rlog.d("ImsSmsDispacher", "SMS received.");
      paramAnonymousArrayOfByte = SmsMessage.createFromPdu(paramAnonymousArrayOfByte, paramAnonymousString);
      mSmsDispatchersController.injectSmsPdu(paramAnonymousArrayOfByte, paramAnonymousString, new _..Lambda.ImsSmsDispatcher.3.q7JFSZBuWsj_jBm5R51WxdJYNxc(this, paramAnonymousArrayOfByte, paramAnonymousInt), true);
    }
    
    public void onSmsStatusReportReceived(int paramAnonymousInt1, int paramAnonymousInt2, String paramAnonymousString, byte[] paramAnonymousArrayOfByte)
      throws RemoteException
    {
      Rlog.d("ImsSmsDispacher", "Status report received.");
      Object localObject = (SMSDispatcher.SmsTracker)mTrackers.get(Integer.valueOf(paramAnonymousInt1));
      if (localObject != null)
      {
        paramAnonymousString = mSmsDispatchersController.handleSmsStatusReport((SMSDispatcher.SmsTracker)localObject, paramAnonymousString, paramAnonymousArrayOfByte);
        paramAnonymousArrayOfByte = new StringBuilder();
        paramAnonymousArrayOfByte.append("Status report handle result, success: ");
        paramAnonymousArrayOfByte.append(first);
        paramAnonymousArrayOfByte.append("complete: ");
        paramAnonymousArrayOfByte.append(second);
        Rlog.d("ImsSmsDispacher", paramAnonymousArrayOfByte.toString());
        try
        {
          paramAnonymousArrayOfByte = ImsSmsDispatcher.this.getImsManager();
          int i;
          if (((Boolean)first).booleanValue()) {
            i = 1;
          } else {
            i = 2;
          }
          paramAnonymousArrayOfByte.acknowledgeSmsReport(paramAnonymousInt1, paramAnonymousInt2, i);
        }
        catch (ImsException paramAnonymousArrayOfByte)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Failed to acknowledgeSmsReport(). Error: ");
          ((StringBuilder)localObject).append(paramAnonymousArrayOfByte.getMessage());
          Rlog.e("ImsSmsDispacher", ((StringBuilder)localObject).toString());
        }
        if (((Boolean)second).booleanValue()) {
          mTrackers.remove(Integer.valueOf(paramAnonymousInt1));
        }
        return;
      }
      throw new RemoteException("Invalid token.");
    }
  };
  private volatile boolean mIsImsServiceUp;
  private volatile boolean mIsRegistered;
  private volatile boolean mIsSmsCapable;
  private final Object mLock = new Object();
  @VisibleForTesting
  public AtomicInteger mNextToken = new AtomicInteger();
  private ImsRegistrationImplBase.Callback mRegistrationCallback = new ImsRegistrationImplBase.Callback()
  {
    public void onDeregistered(ImsReasonInfo paramAnonymousImsReasonInfo)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onImsDisconnected imsReasonInfo=");
      ((StringBuilder)???).append(paramAnonymousImsReasonInfo);
      Rlog.d("ImsSmsDispacher", ((StringBuilder)???).toString());
      synchronized (mLock)
      {
        ImsSmsDispatcher.access$102(ImsSmsDispatcher.this, false);
        return;
      }
    }
    
    public void onRegistered(int paramAnonymousInt)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onImsConnected imsRadioTech=");
      ((StringBuilder)???).append(paramAnonymousInt);
      Rlog.d("ImsSmsDispacher", ((StringBuilder)???).toString());
      synchronized (mLock)
      {
        ImsSmsDispatcher.access$102(ImsSmsDispatcher.this, true);
        return;
      }
    }
    
    public void onRegistering(int paramAnonymousInt)
    {
      ??? = new StringBuilder();
      ((StringBuilder)???).append("onImsProgressing imsRadioTech=");
      ((StringBuilder)???).append(paramAnonymousInt);
      Rlog.d("ImsSmsDispacher", ((StringBuilder)???).toString());
      synchronized (mLock)
      {
        ImsSmsDispatcher.access$102(ImsSmsDispatcher.this, false);
        return;
      }
    }
  };
  @VisibleForTesting
  public Map<Integer, SMSDispatcher.SmsTracker> mTrackers = new ConcurrentHashMap();
  
  public ImsSmsDispatcher(Phone paramPhone, SmsDispatchersController paramSmsDispatchersController)
  {
    super(paramPhone, paramSmsDispatchersController);
    mImsManagerConnector.connect();
  }
  
  private ImsManager getImsManager()
  {
    return ImsManager.getInstance(mContext, mPhone.getPhoneId());
  }
  
  private boolean isEmergencySmsPossible()
  {
    boolean bool;
    if ((!isLteService()) && (!isLimitedLteService())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isLimitedLteService()
  {
    boolean bool;
    if ((mPhone.getServiceState().getRilVoiceRadioTechnology() == 14) && (mPhone.getServiceState().isEmergencyOnly())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isLteService()
  {
    boolean bool;
    if ((mPhone.getServiceState().getRilDataRadioTechnology() == 14) && (mPhone.getServiceState().getDataRegState() == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void setListeners()
    throws ImsException
  {
    getImsManager().addRegistrationCallback(mRegistrationCallback);
    getImsManager().addCapabilitiesCallback(mCapabilityCallback);
    getImsManager().setSmsListener(mImsSmsListener);
    getImsManager().onSmsReady();
  }
  
  protected GsmAlphabet.TextEncodingDetails calculateLength(CharSequence paramCharSequence, boolean paramBoolean)
  {
    return SMSDispatcherUtil.calculateLength(isCdmaMo(), paramCharSequence, paramBoolean);
  }
  
  @VisibleForTesting
  public void fallbackToPstn(int paramInt, SMSDispatcher.SmsTracker paramSmsTracker)
  {
    mSmsDispatchersController.sendRetrySms(paramSmsTracker);
    mTrackers.remove(Integer.valueOf(paramInt));
  }
  
  protected String getFormat()
  {
    try
    {
      localObject = getImsManager().getSmsFormat();
      return localObject;
    }
    catch (ImsException localImsException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Failed to get sms format. Error: ");
      ((StringBuilder)localObject).append(localImsException.getMessage());
      Rlog.e("ImsSmsDispacher", ((StringBuilder)localObject).toString());
    }
    return "unknown";
  }
  
  protected SmsMessageBase.SubmitPduBase getSubmitPdu(String paramString1, String paramString2, int paramInt, byte[] paramArrayOfByte, boolean paramBoolean)
  {
    return SMSDispatcherUtil.getSubmitPdu(isCdmaMo(), paramString1, paramString2, paramInt, paramArrayOfByte, paramBoolean);
  }
  
  protected SmsMessageBase.SubmitPduBase getSubmitPdu(String paramString1, String paramString2, String paramString3, boolean paramBoolean, SmsHeader paramSmsHeader, int paramInt1, int paramInt2)
  {
    return SMSDispatcherUtil.getSubmitPdu(isCdmaMo(), paramString1, paramString2, paramString3, paramBoolean, paramSmsHeader, paramInt1, paramInt2);
  }
  
  public boolean isAvailable()
  {
    synchronized (mLock)
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("isAvailable: up=");
      localStringBuilder.append(mIsImsServiceUp);
      localStringBuilder.append(", reg= ");
      localStringBuilder.append(mIsRegistered);
      localStringBuilder.append(", cap= ");
      localStringBuilder.append(mIsSmsCapable);
      Rlog.d("ImsSmsDispacher", localStringBuilder.toString());
      boolean bool;
      if ((mIsImsServiceUp) && (mIsRegistered) && (mIsSmsCapable)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  protected boolean isCdmaMo()
  {
    return mSmsDispatchersController.isCdmaFormat(getFormat());
  }
  
  public boolean isEmergencySmsSupport(String paramString)
  {
    boolean bool1 = PhoneNumberUtils.isLocalEmergencyNumber(mContext, paramString);
    boolean bool2 = false;
    if (!bool1)
    {
      Rlog.e("ImsSmsDispacher", "Emergency Sms is not supported for this phone number");
      return false;
    }
    Object localObject = (CarrierConfigManager)mPhone.getContext().getSystemService("carrier_config");
    if (localObject == null)
    {
      Rlog.e("ImsSmsDispacher", "configManager is null");
      return false;
    }
    localObject = ((CarrierConfigManager)localObject).getConfigForSubId(getSubId());
    if (localObject == null)
    {
      Rlog.e("ImsSmsDispacher", "PersistableBundle is null");
      return false;
    }
    boolean bool3 = ((PersistableBundle)localObject).getBoolean("emergency_sms_support_bool");
    boolean bool4 = isEmergencySmsPossible();
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("isEmergencySmsSupport emergencySmsCarrierSupport: ");
    ((StringBuilder)localObject).append(bool3);
    ((StringBuilder)localObject).append(" destAddr: ");
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(" mIsImsServiceUp: ");
    ((StringBuilder)localObject).append(mIsImsServiceUp);
    ((StringBuilder)localObject).append(" lteOrLimitedLte: ");
    ((StringBuilder)localObject).append(bool4);
    Rlog.i("ImsSmsDispacher", ((StringBuilder)localObject).toString());
    bool1 = bool2;
    if (bool3)
    {
      bool1 = bool2;
      if (mIsImsServiceUp)
      {
        bool1 = bool2;
        if (bool4) {
          bool1 = true;
        }
      }
    }
    return bool1;
  }
  
  public void sendSms(SMSDispatcher.SmsTracker paramSmsTracker)
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("sendSms:  mRetryCount=");
    ((StringBuilder)localObject1).append(mRetryCount);
    ((StringBuilder)localObject1).append(" mMessageRef=");
    ((StringBuilder)localObject1).append(mMessageRef);
    ((StringBuilder)localObject1).append(" SS=");
    ((StringBuilder)localObject1).append(mPhone.getServiceState().getState());
    Rlog.d("ImsSmsDispacher", ((StringBuilder)localObject1).toString());
    mUsesImsServiceForIms = true;
    localObject1 = paramSmsTracker.getData();
    Object localObject2 = (byte[])((HashMap)localObject1).get("pdu");
    byte[] arrayOfByte = (byte[])((HashMap)localObject1).get("smsc");
    boolean bool;
    if (mRetryCount > 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (("3gpp".equals(getFormat())) && (mRetryCount > 0) && ((localObject2[0] & 0x1) == 1))
    {
      localObject2[0] = ((byte)(byte)(localObject2[0] | 0x4));
      localObject2[1] = ((byte)(byte)mMessageRef);
    }
    int i = mNextToken.incrementAndGet();
    mTrackers.put(Integer.valueOf(i), paramSmsTracker);
    try
    {
      ImsManager localImsManager = getImsManager();
      int j = mMessageRef;
      String str = getFormat();
      if (arrayOfByte != null)
      {
        localObject1 = new java/lang/String;
        ((String)localObject1).<init>(arrayOfByte);
      }
      else
      {
        localObject1 = null;
      }
      localImsManager.sendSms(i, j, str, (String)localObject1, bool, (byte[])localObject2);
    }
    catch (ImsException localImsException)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("sendSms failed. Falling back to PSTN. Error: ");
      ((StringBuilder)localObject2).append(localImsException.getMessage());
      Rlog.e("ImsSmsDispacher", ((StringBuilder)localObject2).toString());
      fallbackToPstn(i, paramSmsTracker);
    }
  }
  
  protected boolean shouldBlockSmsForEcbm()
  {
    return false;
  }
}
