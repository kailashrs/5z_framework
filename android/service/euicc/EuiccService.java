package android.service.euicc;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;
import android.util.ArraySet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@SystemApi
public abstract class EuiccService
  extends Service
{
  public static final String ACTION_MANAGE_EMBEDDED_SUBSCRIPTIONS = "android.service.euicc.action.MANAGE_EMBEDDED_SUBSCRIPTIONS";
  public static final String ACTION_PROVISION_EMBEDDED_SUBSCRIPTION = "android.service.euicc.action.PROVISION_EMBEDDED_SUBSCRIPTION";
  public static final String ACTION_RESOLVE_CONFIRMATION_CODE = "android.service.euicc.action.RESOLVE_CONFIRMATION_CODE";
  public static final String ACTION_RESOLVE_DEACTIVATE_SIM = "android.service.euicc.action.RESOLVE_DEACTIVATE_SIM";
  public static final String ACTION_RESOLVE_NO_PRIVILEGES = "android.service.euicc.action.RESOLVE_NO_PRIVILEGES";
  public static final String CATEGORY_EUICC_UI = "android.service.euicc.category.EUICC_UI";
  public static final String EUICC_SERVICE_INTERFACE = "android.service.euicc.EuiccService";
  public static final String EXTRA_RESOLUTION_CALLING_PACKAGE = "android.service.euicc.extra.RESOLUTION_CALLING_PACKAGE";
  public static final String EXTRA_RESOLUTION_CONFIRMATION_CODE = "android.service.euicc.extra.RESOLUTION_CONFIRMATION_CODE";
  public static final String EXTRA_RESOLUTION_CONFIRMATION_CODE_RETRIED = "android.service.euicc.extra.RESOLUTION_CONFIRMATION_CODE_RETRIED";
  public static final String EXTRA_RESOLUTION_CONSENT = "android.service.euicc.extra.RESOLUTION_CONSENT";
  public static final ArraySet<String> RESOLUTION_ACTIONS = new ArraySet();
  public static final int RESULT_FIRST_USER = 1;
  public static final int RESULT_MUST_DEACTIVATE_SIM = -1;
  public static final int RESULT_NEED_CONFIRMATION_CODE = -2;
  public static final int RESULT_OK = 0;
  private ThreadPoolExecutor mExecutor;
  private final IEuiccService.Stub mStubWrapper = new IEuiccServiceWrapper(null);
  
  static
  {
    RESOLUTION_ACTIONS.add("android.service.euicc.action.RESOLVE_DEACTIVATE_SIM");
    RESOLUTION_ACTIONS.add("android.service.euicc.action.RESOLVE_NO_PRIVILEGES");
    RESOLUTION_ACTIONS.add("android.service.euicc.action.RESOLVE_CONFIRMATION_CODE");
  }
  
  public EuiccService() {}
  
  public IBinder onBind(Intent paramIntent)
  {
    return mStubWrapper;
  }
  
  public void onCreate()
  {
    super.onCreate();
    mExecutor = new ThreadPoolExecutor(4, 4, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory()
    {
      private final AtomicInteger mCount = new AtomicInteger(1);
      
      public Thread newThread(Runnable paramAnonymousRunnable)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("EuiccService #");
        localStringBuilder.append(mCount.getAndIncrement());
        return new Thread(paramAnonymousRunnable, localStringBuilder.toString());
      }
    });
    mExecutor.allowCoreThreadTimeOut(true);
  }
  
  public abstract int onDeleteSubscription(int paramInt, String paramString);
  
  public void onDestroy()
  {
    mExecutor.shutdownNow();
    super.onDestroy();
  }
  
  public abstract int onDownloadSubscription(int paramInt, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract int onEraseSubscriptions(int paramInt);
  
  public abstract GetDefaultDownloadableSubscriptionListResult onGetDefaultDownloadableSubscriptionList(int paramInt, boolean paramBoolean);
  
  public abstract GetDownloadableSubscriptionMetadataResult onGetDownloadableSubscriptionMetadata(int paramInt, DownloadableSubscription paramDownloadableSubscription, boolean paramBoolean);
  
  public abstract String onGetEid(int paramInt);
  
  public abstract EuiccInfo onGetEuiccInfo(int paramInt);
  
  public abstract GetEuiccProfileInfoListResult onGetEuiccProfileInfoList(int paramInt);
  
  public abstract int onGetOtaStatus(int paramInt);
  
  public abstract int onRetainSubscriptionsForFactoryReset(int paramInt);
  
  public abstract void onStartOtaIfNecessary(int paramInt, OtaStatusChangedCallback paramOtaStatusChangedCallback);
  
  public abstract int onSwitchToSubscription(int paramInt, String paramString, boolean paramBoolean);
  
  public abstract int onUpdateSubscriptionNickname(int paramInt, String paramString1, String paramString2);
  
  private class IEuiccServiceWrapper
    extends IEuiccService.Stub
  {
    private IEuiccServiceWrapper() {}
    
    public void deleteSubscription(final int paramInt, final String paramString, final IDeleteSubscriptionCallback paramIDeleteSubscriptionCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          int i = onDeleteSubscription(paramInt, paramString);
          try
          {
            paramIDeleteSubscriptionCallback.onComplete(i);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void downloadSubscription(final int paramInt, final DownloadableSubscription paramDownloadableSubscription, final boolean paramBoolean1, final boolean paramBoolean2, final IDownloadSubscriptionCallback paramIDownloadSubscriptionCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          int i = onDownloadSubscription(paramInt, paramDownloadableSubscription, paramBoolean1, paramBoolean2);
          try
          {
            paramIDownloadSubscriptionCallback.onComplete(i);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void eraseSubscriptions(final int paramInt, final IEraseSubscriptionsCallback paramIEraseSubscriptionsCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          int i = onEraseSubscriptions(paramInt);
          try
          {
            paramIEraseSubscriptionsCallback.onComplete(i);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void getDefaultDownloadableSubscriptionList(final int paramInt, final boolean paramBoolean, final IGetDefaultDownloadableSubscriptionListCallback paramIGetDefaultDownloadableSubscriptionListCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          GetDefaultDownloadableSubscriptionListResult localGetDefaultDownloadableSubscriptionListResult = onGetDefaultDownloadableSubscriptionList(paramInt, paramBoolean);
          try
          {
            paramIGetDefaultDownloadableSubscriptionListCallback.onComplete(localGetDefaultDownloadableSubscriptionListResult);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void getDownloadableSubscriptionMetadata(final int paramInt, final DownloadableSubscription paramDownloadableSubscription, final boolean paramBoolean, final IGetDownloadableSubscriptionMetadataCallback paramIGetDownloadableSubscriptionMetadataCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          GetDownloadableSubscriptionMetadataResult localGetDownloadableSubscriptionMetadataResult = onGetDownloadableSubscriptionMetadata(paramInt, paramDownloadableSubscription, paramBoolean);
          try
          {
            paramIGetDownloadableSubscriptionMetadataCallback.onComplete(localGetDownloadableSubscriptionMetadataResult);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void getEid(final int paramInt, final IGetEidCallback paramIGetEidCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          String str = onGetEid(paramInt);
          try
          {
            paramIGetEidCallback.onSuccess(str);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void getEuiccInfo(final int paramInt, final IGetEuiccInfoCallback paramIGetEuiccInfoCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          EuiccInfo localEuiccInfo = onGetEuiccInfo(paramInt);
          try
          {
            paramIGetEuiccInfoCallback.onSuccess(localEuiccInfo);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void getEuiccProfileInfoList(final int paramInt, final IGetEuiccProfileInfoListCallback paramIGetEuiccProfileInfoListCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          GetEuiccProfileInfoListResult localGetEuiccProfileInfoListResult = onGetEuiccProfileInfoList(paramInt);
          try
          {
            paramIGetEuiccProfileInfoListCallback.onComplete(localGetEuiccProfileInfoListResult);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void getOtaStatus(final int paramInt, final IGetOtaStatusCallback paramIGetOtaStatusCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          int i = onGetOtaStatus(paramInt);
          try
          {
            paramIGetOtaStatusCallback.onSuccess(i);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void retainSubscriptionsForFactoryReset(final int paramInt, final IRetainSubscriptionsForFactoryResetCallback paramIRetainSubscriptionsForFactoryResetCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          int i = onRetainSubscriptionsForFactoryReset(paramInt);
          try
          {
            paramIRetainSubscriptionsForFactoryResetCallback.onComplete(i);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void startOtaIfNecessary(final int paramInt, final IOtaStatusChangedCallback paramIOtaStatusChangedCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          onStartOtaIfNecessary(paramInt, new EuiccService.OtaStatusChangedCallback()
          {
            public void onOtaStatusChanged(int paramAnonymous2Int)
            {
              try
              {
                val$statusChangedCallback.onOtaStatusChanged(paramAnonymous2Int);
              }
              catch (RemoteException localRemoteException) {}
            }
          });
        }
      });
    }
    
    public void switchToSubscription(final int paramInt, final String paramString, final boolean paramBoolean, final ISwitchToSubscriptionCallback paramISwitchToSubscriptionCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          int i = onSwitchToSubscription(paramInt, paramString, paramBoolean);
          try
          {
            paramISwitchToSubscriptionCallback.onComplete(i);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
    
    public void updateSubscriptionNickname(final int paramInt, final String paramString1, final String paramString2, final IUpdateSubscriptionNicknameCallback paramIUpdateSubscriptionNicknameCallback)
    {
      mExecutor.execute(new Runnable()
      {
        public void run()
        {
          int i = onUpdateSubscriptionNickname(paramInt, paramString1, paramString2);
          try
          {
            paramIUpdateSubscriptionNicknameCallback.onComplete(i);
          }
          catch (RemoteException localRemoteException) {}
        }
      });
    }
  }
  
  public static abstract class OtaStatusChangedCallback
  {
    public OtaStatusChangedCallback() {}
    
    public abstract void onOtaStatusChanged(int paramInt);
  }
}
