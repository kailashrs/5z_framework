package com.android.internal.telephony.ims;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsServiceController;
import android.telephony.ims.aidl.IImsServiceController.Stub;
import android.telephony.ims.stub.ImsFeatureConfiguration;
import android.telephony.ims.stub.ImsFeatureConfiguration.FeatureSlotPair;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ImsServiceFeatureQueryManager
{
  private final Map<ComponentName, ImsServiceFeatureQuery> mActiveQueries = new HashMap();
  private final Context mContext;
  private final Listener mListener;
  private final Object mLock = new Object();
  
  public ImsServiceFeatureQueryManager(Context paramContext, Listener paramListener)
  {
    mContext = paramContext;
    mListener = paramListener;
  }
  
  public boolean isQueryInProgress()
  {
    synchronized (mLock)
    {
      boolean bool = mActiveQueries.isEmpty();
      return bool ^ true;
    }
  }
  
  public boolean startQuery(ComponentName paramComponentName, String paramString)
  {
    synchronized (mLock)
    {
      if (mActiveQueries.containsKey(paramComponentName)) {
        return true;
      }
      ImsServiceFeatureQuery localImsServiceFeatureQuery = new com/android/internal/telephony/ims/ImsServiceFeatureQueryManager$ImsServiceFeatureQuery;
      localImsServiceFeatureQuery.<init>(this, paramComponentName, paramString);
      mActiveQueries.put(paramComponentName, localImsServiceFeatureQuery);
      boolean bool = localImsServiceFeatureQuery.start();
      return bool;
    }
  }
  
  private final class ImsServiceFeatureQuery
    implements ServiceConnection
  {
    private static final String LOG_TAG = "ImsServiceFeatureQuery";
    private final String mIntentFilter;
    private final ComponentName mName;
    
    ImsServiceFeatureQuery(ComponentName paramComponentName, String paramString)
    {
      mName = paramComponentName;
      mIntentFilter = paramString;
    }
    
    private void cleanup()
    {
      mContext.unbindService(this);
      synchronized (mLock)
      {
        mActiveQueries.remove(mName);
        return;
      }
    }
    
    private void queryImsFeatures(IImsServiceController paramIImsServiceController)
    {
      try
      {
        paramIImsServiceController = paramIImsServiceController.querySupportedImsFeatures();
        paramIImsServiceController = paramIImsServiceController.getServiceFeatures();
        cleanup();
        mListener.onComplete(mName, paramIImsServiceController);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        paramIImsServiceController = new StringBuilder();
        paramIImsServiceController.append("queryImsFeatures - error: ");
        paramIImsServiceController.append(localRemoteException);
        Log.w("ImsServiceFeatureQuery", paramIImsServiceController.toString());
        cleanup();
        mListener.onError(mName);
      }
    }
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onServiceConnected for component: ");
      localStringBuilder.append(paramComponentName);
      Log.i("ImsServiceFeatureQuery", localStringBuilder.toString());
      if (paramIBinder != null)
      {
        queryImsFeatures(IImsServiceController.Stub.asInterface(paramIBinder));
      }
      else
      {
        paramIBinder = new StringBuilder();
        paramIBinder.append("onServiceConnected: ");
        paramIBinder.append(paramComponentName);
        paramIBinder.append(" binder null, cleaning up.");
        Log.w("ImsServiceFeatureQuery", paramIBinder.toString());
        cleanup();
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onServiceDisconnected for component: ");
      localStringBuilder.append(paramComponentName);
      Log.w("ImsServiceFeatureQuery", localStringBuilder.toString());
    }
    
    public boolean start()
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("start: intent filter=");
      ((StringBuilder)localObject).append(mIntentFilter);
      ((StringBuilder)localObject).append(", name=");
      ((StringBuilder)localObject).append(mName);
      Log.d("ImsServiceFeatureQuery", ((StringBuilder)localObject).toString());
      localObject = new Intent(mIntentFilter).setComponent(mName);
      boolean bool = mContext.bindService((Intent)localObject, this, 67108929);
      if (!bool) {
        cleanup();
      }
      return bool;
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void onComplete(ComponentName paramComponentName, Set<ImsFeatureConfiguration.FeatureSlotPair> paramSet);
    
    public abstract void onError(ComponentName paramComponentName);
  }
}
