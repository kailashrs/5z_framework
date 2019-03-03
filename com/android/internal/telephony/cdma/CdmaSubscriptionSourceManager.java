package com.android.internal.telephony.cdma;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.Registrant;
import android.os.RegistrantList;
import android.provider.Settings.Global;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import java.util.concurrent.atomic.AtomicInteger;

public class CdmaSubscriptionSourceManager
  extends Handler
{
  private static final int EVENT_CDMA_SUBSCRIPTION_SOURCE_CHANGED = 1;
  private static final int EVENT_GET_CDMA_SUBSCRIPTION_SOURCE = 2;
  private static final int EVENT_RADIO_ON = 3;
  private static final int EVENT_SUBSCRIPTION_STATUS_CHANGED = 4;
  static final String LOG_TAG = "CdmaSSM";
  private static final int SUBSCRIPTION_ACTIVATED = 1;
  public static final int SUBSCRIPTION_FROM_NV = 1;
  public static final int SUBSCRIPTION_FROM_RUIM = 0;
  public static final int SUBSCRIPTION_SOURCE_UNKNOWN = -1;
  private static CdmaSubscriptionSourceManager sInstance;
  private static int sReferenceCount = 0;
  private static final Object sReferenceCountMonitor = new Object();
  private AtomicInteger mCdmaSubscriptionSource = new AtomicInteger(0);
  private RegistrantList mCdmaSubscriptionSourceChangedRegistrants = new RegistrantList();
  private CommandsInterface mCi;
  
  private CdmaSubscriptionSourceManager(Context paramContext, CommandsInterface paramCommandsInterface)
  {
    mCi = paramCommandsInterface;
    mCi.registerForCdmaSubscriptionChanged(this, 1, null);
    mCi.registerForOn(this, 3, null);
    int i = getDefault(paramContext);
    paramContext = new StringBuilder();
    paramContext.append("cdmaSSM constructor: ");
    paramContext.append(i);
    log(paramContext.toString());
    mCdmaSubscriptionSource.set(i);
    mCi.registerForSubscriptionStatusChanged(this, 4, null);
  }
  
  public static int getDefault(Context paramContext)
  {
    int i = Settings.Global.getInt(paramContext.getContentResolver(), "subscription_mode", 0);
    paramContext = new StringBuilder();
    paramContext.append("subscriptionSource from settings: ");
    paramContext.append(i);
    Rlog.d("CdmaSSM", paramContext.toString());
    return i;
  }
  
  public static CdmaSubscriptionSourceManager getInstance(Context paramContext, CommandsInterface paramCommandsInterface, Handler paramHandler, int paramInt, Object paramObject)
  {
    synchronized (sReferenceCountMonitor)
    {
      if (sInstance == null)
      {
        CdmaSubscriptionSourceManager localCdmaSubscriptionSourceManager = new com/android/internal/telephony/cdma/CdmaSubscriptionSourceManager;
        localCdmaSubscriptionSourceManager.<init>(paramContext, paramCommandsInterface);
        sInstance = localCdmaSubscriptionSourceManager;
      }
      sReferenceCount += 1;
      sInstance.registerForCdmaSubscriptionSourceChanged(paramHandler, paramInt, paramObject);
      return sInstance;
    }
  }
  
  private void handleGetCdmaSubscriptionSource(AsyncResult paramAsyncResult)
  {
    if ((exception == null) && (result != null))
    {
      int i = ((int[])result)[0];
      if (i != mCdmaSubscriptionSource.get())
      {
        paramAsyncResult = new StringBuilder();
        paramAsyncResult.append("Subscription Source Changed : ");
        paramAsyncResult.append(mCdmaSubscriptionSource);
        paramAsyncResult.append(" >> ");
        paramAsyncResult.append(i);
        log(paramAsyncResult.toString());
        mCdmaSubscriptionSource.set(i);
        mCdmaSubscriptionSourceChangedRegistrants.notifyRegistrants(new AsyncResult(null, null, null));
      }
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to get CDMA Subscription Source, Exception: ");
      localStringBuilder.append(exception);
      localStringBuilder.append(", result: ");
      localStringBuilder.append(result);
      logw(localStringBuilder.toString());
    }
  }
  
  private void log(String paramString)
  {
    Rlog.d("CdmaSSM", paramString);
  }
  
  private void logw(String paramString)
  {
    Rlog.w("CdmaSSM", paramString);
  }
  
  private void registerForCdmaSubscriptionSourceChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    paramHandler = new Registrant(paramHandler, paramInt, paramObject);
    mCdmaSubscriptionSourceChangedRegistrants.add(paramHandler);
  }
  
  public void dispose(Handler arg1)
  {
    mCdmaSubscriptionSourceChangedRegistrants.remove(???);
    synchronized (sReferenceCountMonitor)
    {
      sReferenceCount -= 1;
      if (sReferenceCount <= 0)
      {
        mCi.unregisterForCdmaSubscriptionChanged(this);
        mCi.unregisterForOn(this);
        mCi.unregisterForSubscriptionStatusChanged(this);
        sInstance = null;
      }
      return;
    }
  }
  
  public int getCdmaSubscriptionSource()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("getcdmasubscriptionSource: ");
    localStringBuilder.append(mCdmaSubscriptionSource.get());
    log(localStringBuilder.toString());
    return mCdmaSubscriptionSource.get();
  }
  
  public void handleMessage(Message paramMessage)
  {
    Object localObject;
    switch (what)
    {
    default: 
      super.handleMessage(paramMessage);
      break;
    case 4: 
      log("EVENT_SUBSCRIPTION_STATUS_CHANGED");
      localObject = (AsyncResult)obj;
      if (exception == null)
      {
        int i = ((int[])result)[0];
        paramMessage = new StringBuilder();
        paramMessage.append("actStatus = ");
        paramMessage.append(i);
        log(paramMessage.toString());
        if (i == 1)
        {
          Rlog.v("CdmaSSM", "get Cdma Subscription Source");
          mCi.getCdmaSubscriptionSource(obtainMessage(2));
        }
      }
      else
      {
        paramMessage = new StringBuilder();
        paramMessage.append("EVENT_SUBSCRIPTION_STATUS_CHANGED, Exception:");
        paramMessage.append(exception);
        logw(paramMessage.toString());
      }
      break;
    case 3: 
      mCi.getCdmaSubscriptionSource(obtainMessage(2));
      break;
    case 1: 
    case 2: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("CDMA_SUBSCRIPTION_SOURCE event = ");
      ((StringBuilder)localObject).append(what);
      log(((StringBuilder)localObject).toString());
      handleGetCdmaSubscriptionSource((AsyncResult)obj);
    }
  }
}
