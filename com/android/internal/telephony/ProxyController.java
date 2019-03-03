package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.RadioAccessFamily;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.dataconnection.DcTracker;
import com.android.internal.telephony.uicc.UiccController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ProxyController
{
  private static final int EVENT_APPLY_RC_RESPONSE = 3;
  private static final int EVENT_FINISH_RC_RESPONSE = 4;
  private static final int EVENT_NOTIFICATION_RC_CHANGED = 1;
  private static final int EVENT_START_RC_RESPONSE = 2;
  private static final int EVENT_TIMEOUT = 5;
  static final String LOG_TAG = "ProxyController";
  private static final int SET_RC_STATUS_APPLYING = 3;
  private static final int SET_RC_STATUS_FAIL = 5;
  private static final int SET_RC_STATUS_IDLE = 0;
  private static final int SET_RC_STATUS_STARTED = 2;
  private static final int SET_RC_STATUS_STARTING = 1;
  private static final int SET_RC_STATUS_SUCCESS = 4;
  private static final int SET_RC_TIMEOUT_WAITING_MSEC = 45000;
  private static ProxyController sProxyController;
  private CommandsInterface[] mCi;
  private Context mContext;
  private String[] mCurrentLogicalModemIds;
  private Handler mHandler;
  private String[] mNewLogicalModemIds;
  private int[] mNewRadioAccessFamily;
  private int[] mOldRadioAccessFamily;
  private PhoneSubInfoController mPhoneSubInfoController;
  private PhoneSwitcher mPhoneSwitcher;
  private Phone[] mPhones;
  private int mRadioAccessFamilyStatusCounter;
  private int mRadioCapabilitySessionId;
  private int[] mSetRadioAccessFamilyStatus;
  private boolean mTransactionFailed;
  private UiccController mUiccController;
  private UiccPhoneBookController mUiccPhoneBookController;
  private UiccSmsController mUiccSmsController;
  private AtomicInteger mUniqueIdGenerator;
  PowerManager.WakeLock mWakeLock;
  
  private ProxyController(Context paramContext, Phone[] paramArrayOfPhone, UiccController paramUiccController, CommandsInterface[] paramArrayOfCommandsInterface, PhoneSwitcher paramPhoneSwitcher)
  {
    int i = 0;
    mTransactionFailed = false;
    mUniqueIdGenerator = new AtomicInteger(new Random().nextInt());
    mHandler = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        ProxyController localProxyController = ProxyController.this;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("handleMessage msg.what=");
        localStringBuilder.append(what);
        localProxyController.logd(localStringBuilder.toString());
        switch (what)
        {
        default: 
          break;
        case 5: 
          ProxyController.this.onTimeoutRadioCapability(paramAnonymousMessage);
          break;
        case 4: 
          onFinishRadioCapabilityResponse(paramAnonymousMessage);
          break;
        case 3: 
          ProxyController.this.onApplyRadioCapabilityResponse(paramAnonymousMessage);
          break;
        case 2: 
          ProxyController.this.onStartRadioCapabilityResponse(paramAnonymousMessage);
          break;
        case 1: 
          ProxyController.this.onNotificationRadioCapabilityChanged(paramAnonymousMessage);
        }
      }
    };
    logd("Constructor - Enter");
    mContext = paramContext;
    mPhones = paramArrayOfPhone;
    mUiccController = paramUiccController;
    mCi = paramArrayOfCommandsInterface;
    mPhoneSwitcher = paramPhoneSwitcher;
    mUiccPhoneBookController = new UiccPhoneBookController(mPhones);
    mPhoneSubInfoController = new PhoneSubInfoController(mContext, mPhones);
    mUiccSmsController = new UiccSmsController();
    mSetRadioAccessFamilyStatus = new int[mPhones.length];
    mNewRadioAccessFamily = new int[mPhones.length];
    mOldRadioAccessFamily = new int[mPhones.length];
    mCurrentLogicalModemIds = new String[mPhones.length];
    mNewLogicalModemIds = new String[mPhones.length];
    mWakeLock = ((PowerManager)paramContext.getSystemService("power")).newWakeLock(1, "ProxyController");
    mWakeLock.setReferenceCounted(false);
    clearTransaction();
    while (i < mPhones.length)
    {
      mPhones[i].registerForRadioCapabilityChanged(mHandler, 1, null);
      i++;
    }
    logd("Constructor - Exit");
  }
  
  private void clearTransaction()
  {
    logd("clearTransaction");
    int[] arrayOfInt = mSetRadioAccessFamilyStatus;
    int i = 0;
    try
    {
      while (i < mPhones.length)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("clearTransaction: phoneId=");
        localStringBuilder.append(i);
        localStringBuilder.append(" status=IDLE");
        logd(localStringBuilder.toString());
        mSetRadioAccessFamilyStatus[i] = 0;
        mOldRadioAccessFamily[i] = 0;
        mNewRadioAccessFamily[i] = 0;
        mTransactionFailed = false;
        i++;
      }
      if (mWakeLock.isHeld()) {
        mWakeLock.release();
      }
      return;
    }
    finally {}
  }
  
  private void completeRadioCapabilityTransaction()
  {
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("onFinishRadioCapabilityResponse: success=");
    ((StringBuilder)localObject1).append(mTransactionFailed ^ true);
    logd(((StringBuilder)localObject1).toString());
    boolean bool = mTransactionFailed;
    int i = 0;
    int j = 0;
    Object localObject2;
    if (!bool)
    {
      localObject2 = new ArrayList();
      while (j < mPhones.length)
      {
        i = mPhones[j].getRadioAccessFamily();
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("radioAccessFamily[");
        ((StringBuilder)localObject1).append(j);
        ((StringBuilder)localObject1).append("]=");
        ((StringBuilder)localObject1).append(i);
        logd(((StringBuilder)localObject1).toString());
        ((ArrayList)localObject2).add(new RadioAccessFamily(j, i));
        j++;
      }
      localObject1 = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
      ((Intent)localObject1).putParcelableArrayListExtra("rafs", (ArrayList)localObject2);
      mRadioCapabilitySessionId = mUniqueIdGenerator.getAndIncrement();
      clearTransaction();
    }
    else
    {
      localObject1 = new Intent("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
      mTransactionFailed = false;
      localObject2 = new RadioAccessFamily[mPhones.length];
      for (j = i; j < mPhones.length; j++) {
        localObject2[j] = new RadioAccessFamily(j, mOldRadioAccessFamily[j]);
      }
      doSetRadioCapabilities((RadioAccessFamily[])localObject2);
    }
    mContext.sendBroadcast((Intent)localObject1, "android.permission.READ_PHONE_STATE");
  }
  
  private boolean doSetRadioCapabilities(RadioAccessFamily[] paramArrayOfRadioAccessFamily)
  {
    mRadioCapabilitySessionId = mUniqueIdGenerator.getAndIncrement();
    ??? = mHandler;
    int i = mRadioCapabilitySessionId;
    int j = 0;
    ??? = ((Handler)???).obtainMessage(5, i, 0);
    mHandler.sendMessageDelayed((Message)???, 45000L);
    synchronized (mSetRadioAccessFamilyStatus)
    {
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("setRadioCapability: new request session id=");
      localStringBuilder.append(mRadioCapabilitySessionId);
      logd(localStringBuilder.toString());
      resetRadioAccessFamilyStatusCounter();
      while (j < paramArrayOfRadioAccessFamily.length)
      {
        int k = paramArrayOfRadioAccessFamily[j].getPhoneId();
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("setRadioCapability: phoneId=");
        localStringBuilder.append(k);
        localStringBuilder.append(" status=STARTING");
        logd(localStringBuilder.toString());
        mSetRadioAccessFamilyStatus[k] = 1;
        mOldRadioAccessFamily[k] = mPhones[k].getRadioAccessFamily();
        i = paramArrayOfRadioAccessFamily[j].getRadioAccessFamily();
        mNewRadioAccessFamily[k] = i;
        mCurrentLogicalModemIds[k] = mPhones[k].getModemUuId();
        mNewLogicalModemIds[k] = getLogicalModemIdFromRaf(i);
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("setRadioCapability: mOldRadioAccessFamily[");
        localStringBuilder.append(k);
        localStringBuilder.append("]=");
        localStringBuilder.append(mOldRadioAccessFamily[k]);
        logd(localStringBuilder.toString());
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("setRadioCapability: mNewRadioAccessFamily[");
        localStringBuilder.append(k);
        localStringBuilder.append("]=");
        localStringBuilder.append(mNewRadioAccessFamily[k]);
        logd(localStringBuilder.toString());
        sendRadioCapabilityRequest(k, mRadioCapabilitySessionId, 1, mOldRadioAccessFamily[k], mCurrentLogicalModemIds[k], 0, 2);
        j++;
      }
      return true;
    }
  }
  
  public static ProxyController getInstance()
  {
    return sProxyController;
  }
  
  public static ProxyController getInstance(Context paramContext, Phone[] paramArrayOfPhone, UiccController paramUiccController, CommandsInterface[] paramArrayOfCommandsInterface, PhoneSwitcher paramPhoneSwitcher)
  {
    if (sProxyController == null) {
      sProxyController = new ProxyController(paramContext, paramArrayOfPhone, paramUiccController, paramArrayOfCommandsInterface, paramPhoneSwitcher);
    }
    return sProxyController;
  }
  
  private String getLogicalModemIdFromRaf(int paramInt)
  {
    Object localObject1 = null;
    Object localObject2;
    for (int i = 0;; i++)
    {
      localObject2 = localObject1;
      if (i >= mPhones.length) {
        break;
      }
      if (mPhones[i].getRadioAccessFamily() == paramInt)
      {
        localObject2 = mPhones[i].getModemUuId();
        break;
      }
    }
    return localObject2;
  }
  
  private void issueFinish(int paramInt)
  {
    int[] arrayOfInt = mSetRadioAccessFamilyStatus;
    int i = 0;
    try
    {
      while (i < mPhones.length)
      {
        Object localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("issueFinish: phoneId=");
        ((StringBuilder)localObject1).append(i);
        ((StringBuilder)localObject1).append(" sessionId=");
        ((StringBuilder)localObject1).append(paramInt);
        ((StringBuilder)localObject1).append(" mTransactionFailed=");
        ((StringBuilder)localObject1).append(mTransactionFailed);
        logd(((StringBuilder)localObject1).toString());
        mRadioAccessFamilyStatusCounter += 1;
        int j;
        if (mTransactionFailed) {
          j = mOldRadioAccessFamily[i];
        } else {
          j = mNewRadioAccessFamily[i];
        }
        if (mTransactionFailed) {
          localObject1 = mCurrentLogicalModemIds[i];
        } else {
          localObject1 = mNewLogicalModemIds[i];
        }
        int k;
        if (mTransactionFailed) {
          k = 2;
        } else {
          k = 1;
        }
        sendRadioCapabilityRequest(i, paramInt, 4, j, (String)localObject1, k, 4);
        if (mTransactionFailed)
        {
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("issueFinish: phoneId: ");
          ((StringBuilder)localObject1).append(i);
          ((StringBuilder)localObject1).append(" status: FAIL");
          logd(((StringBuilder)localObject1).toString());
          mSetRadioAccessFamilyStatus[i] = 5;
        }
        i++;
      }
      return;
    }
    finally {}
  }
  
  private void logd(String paramString)
  {
    Rlog.d("ProxyController", paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e("ProxyController", paramString);
  }
  
  private void onApplyRadioCapabilityResponse(Message arg1)
  {
    Object localObject1 = (RadioCapability)obj).result;
    if ((localObject1 != null) && (((RadioCapability)localObject1).getSession() == mRadioCapabilitySessionId))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onApplyRadioCapabilityResponse: rc=");
      localStringBuilder.append(localObject1);
      logd(localStringBuilder.toString());
      if (obj).exception != null) {
        synchronized (mSetRadioAccessFamilyStatus)
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("onApplyRadioCapabilityResponse: Error response session=");
          localStringBuilder.append(((RadioCapability)localObject1).getSession());
          logd(localStringBuilder.toString());
          int i = ((RadioCapability)localObject1).getPhoneId();
          localObject1 = new java/lang/StringBuilder;
          ((StringBuilder)localObject1).<init>();
          ((StringBuilder)localObject1).append("onApplyRadioCapabilityResponse: phoneId=");
          ((StringBuilder)localObject1).append(i);
          ((StringBuilder)localObject1).append(" status=FAIL");
          logd(((StringBuilder)localObject1).toString());
          mSetRadioAccessFamilyStatus[i] = 5;
          mTransactionFailed = true;
        }
      }
      ??? = new StringBuilder();
      ???.append("onApplyRadioCapabilityResponse: Valid start expecting notification rc=");
      ???.append(localObject2);
      logd(???.toString());
      return;
    }
    ??? = new StringBuilder();
    ???.append("onApplyRadioCapabilityResponse: Ignore session=");
    ???.append(mRadioCapabilitySessionId);
    ???.append(" rc=");
    ???.append(localObject2);
    logd(???.toString());
  }
  
  private void onNotificationRadioCapabilityChanged(Message paramMessage)
  {
    RadioCapability localRadioCapability = (RadioCapability)obj).result;
    if ((localRadioCapability != null) && (localRadioCapability.getSession() == mRadioCapabilitySessionId)) {
      synchronized (mSetRadioAccessFamilyStatus)
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("onNotificationRadioCapabilityChanged: rc=");
        localStringBuilder.append(localRadioCapability);
        logd(localStringBuilder.toString());
        if (localRadioCapability.getSession() != mRadioCapabilitySessionId)
        {
          paramMessage = new java/lang/StringBuilder;
          paramMessage.<init>();
          paramMessage.append("onNotificationRadioCapabilityChanged: Ignore session=");
          paramMessage.append(mRadioCapabilitySessionId);
          paramMessage.append(" rc=");
          paramMessage.append(localRadioCapability);
          logd(paramMessage.toString());
          return;
        }
        int i = localRadioCapability.getPhoneId();
        if ((obj).exception == null) && (localRadioCapability.getStatus() != 2))
        {
          paramMessage = new java/lang/StringBuilder;
          paramMessage.<init>();
          paramMessage.append("onNotificationRadioCapabilityChanged: phoneId=");
          paramMessage.append(i);
          paramMessage.append(" status=SUCCESS");
          logd(paramMessage.toString());
          mSetRadioAccessFamilyStatus[i] = 4;
          mPhoneSwitcher.resendDataAllowed(i);
          mPhones[i].radioCapabilityUpdated(localRadioCapability);
        }
        else
        {
          paramMessage = new java/lang/StringBuilder;
          paramMessage.<init>();
          paramMessage.append("onNotificationRadioCapabilityChanged: phoneId=");
          paramMessage.append(i);
          paramMessage.append(" status=FAIL");
          logd(paramMessage.toString());
          mSetRadioAccessFamilyStatus[i] = 5;
          mTransactionFailed = true;
        }
        mRadioAccessFamilyStatusCounter -= 1;
        if (mRadioAccessFamilyStatusCounter == 0)
        {
          paramMessage = new java/lang/StringBuilder;
          paramMessage.<init>();
          paramMessage.append("onNotificationRadioCapabilityChanged: APPLY URC success=");
          paramMessage.append(mTransactionFailed);
          logd(paramMessage.toString());
          issueFinish(mRadioCapabilitySessionId);
        }
        return;
      }
    }
    paramMessage = new StringBuilder();
    paramMessage.append("onNotificationRadioCapabilityChanged: Ignore session=");
    paramMessage.append(mRadioCapabilitySessionId);
    paramMessage.append(" rc=");
    paramMessage.append(localRadioCapability);
    logd(paramMessage.toString());
  }
  
  private void onStartRadioCapabilityResponse(Message paramMessage)
  {
    synchronized (mSetRadioAccessFamilyStatus)
    {
      Object localObject = (AsyncResult)obj;
      if ((TelephonyManager.getDefault().getPhoneCount() == 1) && (exception != null))
      {
        paramMessage = new java/lang/StringBuilder;
        paramMessage.<init>();
        paramMessage.append("onStartRadioCapabilityResponse got exception=");
        paramMessage.append(exception);
        logd(paramMessage.toString());
        mRadioCapabilitySessionId = mUniqueIdGenerator.getAndIncrement();
        paramMessage = new android/content/Intent;
        paramMessage.<init>("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
        mContext.sendBroadcast(paramMessage);
        clearTransaction();
        return;
      }
      localObject = (RadioCapability)obj).result;
      if ((localObject != null) && (((RadioCapability)localObject).getSession() == mRadioCapabilitySessionId))
      {
        mRadioAccessFamilyStatusCounter -= 1;
        int i = ((RadioCapability)localObject).getPhoneId();
        if (obj).exception != null)
        {
          paramMessage = new java/lang/StringBuilder;
          paramMessage.<init>();
          paramMessage.append("onStartRadioCapabilityResponse: Error response session=");
          paramMessage.append(((RadioCapability)localObject).getSession());
          logd(paramMessage.toString());
          paramMessage = new java/lang/StringBuilder;
          paramMessage.<init>();
          paramMessage.append("onStartRadioCapabilityResponse: phoneId=");
          paramMessage.append(i);
          paramMessage.append(" status=FAIL");
          logd(paramMessage.toString());
          mSetRadioAccessFamilyStatus[i] = 5;
          mTransactionFailed = true;
        }
        else
        {
          paramMessage = new java/lang/StringBuilder;
          paramMessage.<init>();
          paramMessage.append("onStartRadioCapabilityResponse: phoneId=");
          paramMessage.append(i);
          paramMessage.append(" status=STARTED");
          logd(paramMessage.toString());
          mSetRadioAccessFamilyStatus[i] = 2;
        }
        if (mRadioAccessFamilyStatusCounter == 0)
        {
          localObject = new java/util/HashSet;
          ((HashSet)localObject).<init>(mNewLogicalModemIds.length);
          paramMessage = mNewLogicalModemIds;
          int j = paramMessage.length;
          int k = 0;
          for (i = 0; i < j; i++) {
            if (!((HashSet)localObject).add(paramMessage[i]))
            {
              mTransactionFailed = true;
              Log.wtf("ProxyController", "ERROR: sending down the same id for different phones");
            }
          }
          paramMessage = new java/lang/StringBuilder;
          paramMessage.<init>();
          paramMessage.append("onStartRadioCapabilityResponse: success=");
          paramMessage.append(true ^ mTransactionFailed);
          logd(paramMessage.toString());
          if (mTransactionFailed)
          {
            issueFinish(mRadioCapabilitySessionId);
          }
          else
          {
            resetRadioAccessFamilyStatusCounter();
            for (i = k; i < mPhones.length; i++)
            {
              sendRadioCapabilityRequest(i, mRadioCapabilitySessionId, 2, mNewRadioAccessFamily[i], mNewLogicalModemIds[i], 0, 3);
              paramMessage = new java/lang/StringBuilder;
              paramMessage.<init>();
              paramMessage.append("onStartRadioCapabilityResponse: phoneId=");
              paramMessage.append(i);
              paramMessage.append(" status=APPLYING");
              logd(paramMessage.toString());
              mSetRadioAccessFamilyStatus[i] = 3;
            }
          }
        }
        return;
      }
      paramMessage = new java/lang/StringBuilder;
      paramMessage.<init>();
      paramMessage.append("onStartRadioCapabilityResponse: Ignore session=");
      paramMessage.append(mRadioCapabilitySessionId);
      paramMessage.append(" rc=");
      paramMessage.append(localObject);
      logd(paramMessage.toString());
      return;
    }
  }
  
  private void onTimeoutRadioCapability(Message paramMessage)
  {
    StringBuilder localStringBuilder;
    if (arg1 != mRadioCapabilitySessionId)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("RadioCapability timeout: Ignore msg.arg1=");
      localStringBuilder.append(arg1);
      localStringBuilder.append("!= mRadioCapabilitySessionId=");
      localStringBuilder.append(mRadioCapabilitySessionId);
      logd(localStringBuilder.toString());
      return;
    }
    paramMessage = mSetRadioAccessFamilyStatus;
    int i = 0;
    try
    {
      while (i < mPhones.length)
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("RadioCapability timeout: mSetRadioAccessFamilyStatus[");
        localStringBuilder.append(i);
        localStringBuilder.append("]=");
        localStringBuilder.append(mSetRadioAccessFamilyStatus[i]);
        logd(localStringBuilder.toString());
        i++;
      }
      mRadioCapabilitySessionId = mUniqueIdGenerator.getAndIncrement();
      mRadioAccessFamilyStatusCounter = 0;
      mTransactionFailed = true;
      issueFinish(mRadioCapabilitySessionId);
      return;
    }
    finally {}
  }
  
  private void resetRadioAccessFamilyStatusCounter()
  {
    mRadioAccessFamilyStatusCounter = mPhones.length;
  }
  
  private void sendRadioCapabilityRequest(int paramInt1, int paramInt2, int paramInt3, int paramInt4, String paramString, int paramInt5, int paramInt6)
  {
    paramString = new RadioCapability(paramInt1, paramInt2, paramInt3, paramInt4, paramString, paramInt5);
    mPhones[paramInt1].setRadioCapability(paramString, mHandler.obtainMessage(paramInt6));
  }
  
  public void disableDataConnectivity(int paramInt, Message paramMessage)
  {
    mPhones[paramInt].setInternalDataEnabled(false, paramMessage);
  }
  
  public void enableDataConnectivity(int paramInt)
  {
    mPhones[paramInt].setInternalDataEnabled(true, null);
  }
  
  public int getMaxRafSupported()
  {
    int[] arrayOfInt = new int[mPhones.length];
    int i = 0;
    int j = 1;
    int k = 0;
    while (k < mPhones.length)
    {
      arrayOfInt[k] = Integer.bitCount(mPhones[k].getRadioAccessFamily());
      int m = i;
      if (i < arrayOfInt[k])
      {
        m = arrayOfInt[k];
        j = mPhones[k].getRadioAccessFamily();
      }
      k++;
      i = m;
    }
    return j;
  }
  
  public int getMinRafSupported()
  {
    int[] arrayOfInt = new int[mPhones.length];
    int i = 0;
    int j = 1;
    int k = 0;
    while (k < mPhones.length)
    {
      arrayOfInt[k] = Integer.bitCount(mPhones[k].getRadioAccessFamily());
      int m;
      if (i != 0)
      {
        m = i;
        if (i <= arrayOfInt[k]) {}
      }
      else
      {
        m = arrayOfInt[k];
        j = mPhones[k].getRadioAccessFamily();
      }
      k++;
      i = m;
    }
    return j;
  }
  
  public int getRadioAccessFamily(int paramInt)
  {
    if (paramInt >= mPhones.length) {
      return 1;
    }
    return mPhones[paramInt].getRadioAccessFamily();
  }
  
  public boolean isDataDisconnected(int paramInt)
  {
    paramInt = SubscriptionController.getInstance().getPhoneId(paramInt);
    if ((paramInt >= 0) && (paramInt < TelephonyManager.getDefault().getPhoneCount())) {
      return mPhones[paramInt].mDcTracker.isDisconnected();
    }
    return true;
  }
  
  void onFinishRadioCapabilityResponse(Message arg1)
  {
    Object localObject1 = (RadioCapability)obj).result;
    if ((localObject1 != null) && (((RadioCapability)localObject1).getSession() != mRadioCapabilitySessionId))
    {
      ??? = new StringBuilder();
      ???.append("onFinishRadioCapabilityResponse: Ignore session=");
      ???.append(mRadioCapabilitySessionId);
      ???.append(" rc=");
      ???.append(localObject1);
      logd(???.toString());
      return;
    }
    synchronized (mSetRadioAccessFamilyStatus)
    {
      localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append(" onFinishRadioCapabilityResponse mRadioAccessFamilyStatusCounter=");
      ((StringBuilder)localObject1).append(mRadioAccessFamilyStatusCounter);
      logd(((StringBuilder)localObject1).toString());
      mRadioAccessFamilyStatusCounter -= 1;
      if (mRadioAccessFamilyStatusCounter == 0) {
        completeRadioCapabilityTransaction();
      }
      return;
    }
  }
  
  public void registerForAllDataDisconnected(int paramInt1, Handler paramHandler, int paramInt2, Object paramObject)
  {
    paramInt1 = SubscriptionController.getInstance().getPhoneId(paramInt1);
    if ((paramInt1 >= 0) && (paramInt1 < TelephonyManager.getDefault().getPhoneCount())) {
      mPhones[paramInt1].registerForAllDataDisconnected(paramHandler, paramInt2, paramObject);
    }
  }
  
  public boolean setRadioCapability(RadioAccessFamily[] paramArrayOfRadioAccessFamily)
  {
    if (paramArrayOfRadioAccessFamily.length == mPhones.length)
    {
      int[] arrayOfInt = mSetRadioAccessFamilyStatus;
      int i = 0;
      int j = 0;
      try
      {
        while (j < mPhones.length)
        {
          if (mSetRadioAccessFamilyStatus[j] != 0)
          {
            paramArrayOfRadioAccessFamily = new java/lang/StringBuilder;
            paramArrayOfRadioAccessFamily.<init>();
            paramArrayOfRadioAccessFamily.append("setRadioCapability: Phone[");
            paramArrayOfRadioAccessFamily.append(j);
            paramArrayOfRadioAccessFamily.append("] is not idle. Rejecting request.");
            loge(paramArrayOfRadioAccessFamily.toString());
            return false;
          }
          j++;
        }
        int k = 1;
        j = i;
        i = k;
        while (j < mPhones.length)
        {
          if (mPhones[j].getRadioAccessFamily() != paramArrayOfRadioAccessFamily[j].getRadioAccessFamily()) {
            i = 0;
          }
          j++;
        }
        if (i != 0)
        {
          logd("setRadioCapability: Already in requested configuration, nothing to do.");
          return true;
        }
        clearTransaction();
        mWakeLock.acquire();
        return doSetRadioCapabilities(paramArrayOfRadioAccessFamily);
      }
      finally {}
    }
    throw new RuntimeException("Length of input rafs must equal to total phone count");
  }
  
  public void unregisterForAllDataDisconnected(int paramInt, Handler paramHandler)
  {
    paramInt = SubscriptionController.getInstance().getPhoneId(paramInt);
    if ((paramInt >= 0) && (paramInt < TelephonyManager.getDefault().getPhoneCount())) {
      mPhones[paramInt].unregisterForAllDataDisconnected(paramHandler);
    }
  }
  
  public void updateCurrentCarrierInProvider(int paramInt)
  {
    mPhones[paramInt].updateCurrentCarrierInProvider();
  }
  
  public void updateDataConnectionTracker(int paramInt)
  {
    mPhones[paramInt].updateDataConnectionTracker();
  }
}
