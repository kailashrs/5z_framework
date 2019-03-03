package com.android.ims.internal.uce.uceservice;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import java.util.HashMap;

public class ImsUceManager
{
  public static final String ACTION_UCE_SERVICE_DOWN = "com.android.ims.internal.uce.UCE_SERVICE_DOWN";
  public static final String ACTION_UCE_SERVICE_UP = "com.android.ims.internal.uce.UCE_SERVICE_UP";
  public static final String EXTRA_PHONE_ID = "android:phone_id";
  private static final String LOG_TAG = "ImsUceManager";
  private static final String UCE_SERVICE = "uce";
  public static final int UCE_SERVICE_STATUS_CLOSED = 2;
  public static final int UCE_SERVICE_STATUS_FAILURE = 0;
  public static final int UCE_SERVICE_STATUS_ON = 1;
  public static final int UCE_SERVICE_STATUS_READY = 3;
  private static HashMap<Integer, ImsUceManager> sUceManagerInstances = new HashMap();
  private Context mContext;
  private UceServiceDeathRecipient mDeathReceipient = new UceServiceDeathRecipient(null);
  private int mPhoneId;
  private IUceService mUceService = null;
  
  private ImsUceManager(Context paramContext, int paramInt)
  {
    mContext = paramContext;
    mPhoneId = paramInt;
    createUceService(true);
  }
  
  public static ImsUceManager getInstance(Context paramContext, int paramInt)
  {
    synchronized (sUceManagerInstances)
    {
      if (sUceManagerInstances.containsKey(Integer.valueOf(paramInt)))
      {
        paramContext = (ImsUceManager)sUceManagerInstances.get(Integer.valueOf(paramInt));
        return paramContext;
      }
      ImsUceManager localImsUceManager = new com/android/ims/internal/uce/uceservice/ImsUceManager;
      localImsUceManager.<init>(paramContext, paramInt);
      sUceManagerInstances.put(Integer.valueOf(paramInt), localImsUceManager);
      return localImsUceManager;
    }
  }
  
  private String getUceServiceName(int paramInt)
  {
    return "uce";
  }
  
  public void createUceService(boolean paramBoolean)
  {
    if ((paramBoolean) && (ServiceManager.checkService(getUceServiceName(mPhoneId)) == null)) {
      return;
    }
    IBinder localIBinder = ServiceManager.getService(getUceServiceName(mPhoneId));
    if (localIBinder != null) {
      try
      {
        localIBinder.linkToDeath(mDeathReceipient, 0);
      }
      catch (RemoteException localRemoteException) {}
    }
    mUceService = IUceService.Stub.asInterface(localIBinder);
  }
  
  public IUceService getUceServiceInstance()
  {
    return mUceService;
  }
  
  private class UceServiceDeathRecipient
    implements IBinder.DeathRecipient
  {
    private UceServiceDeathRecipient() {}
    
    public void binderDied()
    {
      ImsUceManager.access$102(ImsUceManager.this, null);
      if (mContext != null)
      {
        Intent localIntent = new Intent("com.android.ims.internal.uce.UCE_SERVICE_DOWN");
        localIntent.putExtra("android:phone_id", mPhoneId);
        mContext.sendBroadcast(new Intent(localIntent));
      }
    }
  }
}
