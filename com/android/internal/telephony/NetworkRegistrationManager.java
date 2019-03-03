package com.android.internal.telephony;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Registrant;
import android.os.RegistrantList;
import android.os.RemoteException;
import android.telephony.CarrierConfigManager;
import android.telephony.INetworkService.Stub;
import android.telephony.INetworkServiceCallback.Stub;
import android.telephony.NetworkRegistrationState;
import android.telephony.Rlog;
import java.util.Hashtable;
import java.util.Map;

public class NetworkRegistrationManager
{
  private static final String TAG = NetworkRegistrationManager.class.getSimpleName();
  private final Map<NetworkRegStateCallback, Message> mCallbackTable = new Hashtable();
  private final CarrierConfigManager mCarrierConfigManager;
  private RegManagerDeathRecipient mDeathRecipient;
  private final Phone mPhone;
  private final RegistrantList mRegStateChangeRegistrants = new RegistrantList();
  private INetworkService.Stub mServiceBinder;
  private final int mTransportType;
  
  public NetworkRegistrationManager(int paramInt, Phone paramPhone)
  {
    mTransportType = paramInt;
    mPhone = paramPhone;
    mCarrierConfigManager = ((CarrierConfigManager)paramPhone.getContext().getSystemService("carrier_config"));
    bindService();
  }
  
  private boolean bindService()
  {
    Intent localIntent = new Intent("android.telephony.NetworkService");
    localIntent.setPackage(getPackageName());
    try
    {
      localObject = mPhone.getContext();
      NetworkServiceConnection localNetworkServiceConnection = new com/android/internal/telephony/NetworkRegistrationManager$NetworkServiceConnection;
      localNetworkServiceConnection.<init>(this, null);
      boolean bool = ((Context)localObject).bindService(localIntent, localNetworkServiceConnection, 1);
      return bool;
    }
    catch (SecurityException localSecurityException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("bindService failed ");
      ((StringBuilder)localObject).append(localSecurityException);
      loge(((StringBuilder)localObject).toString());
    }
    return false;
  }
  
  private String getPackageName()
  {
    int i;
    switch (mTransportType)
    {
    default: 
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Transport type not WWAN or WLAN. type=");
      ((StringBuilder)localObject1).append(mTransportType);
      throw new IllegalStateException(((StringBuilder)localObject1).toString());
    case 2: 
      i = 17039765;
      localObject2 = "carrier_network_service_wlan_package_override_string";
      break;
    case 1: 
      i = 17039767;
      localObject2 = "carrier_network_service_wwan_package_override_string";
    }
    String str = mPhone.getContext().getResources().getString(i);
    PersistableBundle localPersistableBundle = mCarrierConfigManager.getConfigForSubId(mPhone.getSubId());
    Object localObject1 = str;
    if (localPersistableBundle != null) {
      localObject1 = localPersistableBundle.getString((String)localObject2, str);
    }
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append("Binding to packageName ");
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(" for transport type");
    ((StringBuilder)localObject2).append(mTransportType);
    logd(((StringBuilder)localObject2).toString());
    return localObject1;
  }
  
  private static int logd(String paramString)
  {
    return Rlog.d(TAG, paramString);
  }
  
  private static int loge(String paramString)
  {
    return Rlog.e(TAG, paramString);
  }
  
  public void getNetworkRegistrationState(int paramInt, Message paramMessage)
  {
    if (paramMessage == null) {
      return;
    }
    StringBuilder localStringBuilder1 = new StringBuilder();
    localStringBuilder1.append("getNetworkRegistrationState domain ");
    localStringBuilder1.append(paramInt);
    logd(localStringBuilder1.toString());
    if (!isServiceConnected())
    {
      logd("service not connected.");
      obj = new AsyncResult(obj, null, new IllegalStateException("Service not connected."));
      paramMessage.sendToTarget();
      return;
    }
    NetworkRegStateCallback localNetworkRegStateCallback = new NetworkRegStateCallback(null);
    try
    {
      mCallbackTable.put(localNetworkRegStateCallback, paramMessage);
      mServiceBinder.getNetworkRegistrationState(mPhone.getPhoneId(), paramInt, localNetworkRegStateCallback);
    }
    catch (RemoteException localRemoteException)
    {
      String str = TAG;
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("getNetworkRegistrationState RemoteException ");
      localStringBuilder2.append(localRemoteException);
      Rlog.e(str, localStringBuilder2.toString());
      mCallbackTable.remove(localNetworkRegStateCallback);
      obj = new AsyncResult(obj, null, localRemoteException);
      paramMessage.sendToTarget();
    }
  }
  
  public boolean isServiceConnected()
  {
    boolean bool;
    if ((mServiceBinder != null) && (mServiceBinder.isBinderAlive())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void registerForNetworkRegistrationStateChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    logd("registerForNetworkRegistrationStateChanged");
    new Registrant(paramHandler, paramInt, paramObject);
    mRegStateChangeRegistrants.addUnique(paramHandler, paramInt, paramObject);
  }
  
  public void unregisterForNetworkRegistrationStateChanged(Handler paramHandler)
  {
    mRegStateChangeRegistrants.remove(paramHandler);
  }
  
  private class NetworkRegStateCallback
    extends INetworkServiceCallback.Stub
  {
    private NetworkRegStateCallback() {}
    
    public void onGetNetworkRegistrationStateComplete(int paramInt, NetworkRegistrationState paramNetworkRegistrationState)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("onGetNetworkRegistrationStateComplete result ");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(" state ");
      ((StringBuilder)localObject).append(paramNetworkRegistrationState);
      NetworkRegistrationManager.logd(((StringBuilder)localObject).toString());
      localObject = (Message)mCallbackTable.remove(this);
      if (localObject != null)
      {
        arg1 = paramInt;
        obj = new AsyncResult(obj, paramNetworkRegistrationState, null);
        ((Message)localObject).sendToTarget();
      }
      else
      {
        NetworkRegistrationManager.loge("onCompleteMessage is null");
      }
    }
    
    public void onNetworkStateChanged()
    {
      NetworkRegistrationManager.logd("onNetworkStateChanged");
      mRegStateChangeRegistrants.notifyRegistrants();
    }
  }
  
  private class NetworkServiceConnection
    implements ServiceConnection
  {
    private NetworkServiceConnection() {}
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      NetworkRegistrationManager.logd("service connected.");
      NetworkRegistrationManager.access$302(NetworkRegistrationManager.this, (INetworkService.Stub)paramIBinder);
      NetworkRegistrationManager.access$402(NetworkRegistrationManager.this, new NetworkRegistrationManager.RegManagerDeathRecipient(NetworkRegistrationManager.this, paramComponentName));
      try
      {
        mServiceBinder.linkToDeath(mDeathRecipient, 0);
        mServiceBinder.createNetworkServiceProvider(mPhone.getPhoneId());
        paramComponentName = mServiceBinder;
        int i = mPhone.getPhoneId();
        paramIBinder = new com/android/internal/telephony/NetworkRegistrationManager$NetworkRegStateCallback;
        paramIBinder.<init>(NetworkRegistrationManager.this, null);
        paramComponentName.registerForNetworkRegistrationStateChanged(i, paramIBinder);
      }
      catch (RemoteException paramComponentName)
      {
        mDeathRecipient.binderDied();
        paramIBinder = new StringBuilder();
        paramIBinder.append("RemoteException ");
        paramIBinder.append(paramComponentName);
        NetworkRegistrationManager.logd(paramIBinder.toString());
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("onServiceDisconnected ");
      localStringBuilder.append(paramComponentName);
      NetworkRegistrationManager.logd(localStringBuilder.toString());
      if (mServiceBinder != null) {
        mServiceBinder.unlinkToDeath(mDeathRecipient, 0);
      }
    }
  }
  
  private class RegManagerDeathRecipient
    implements IBinder.DeathRecipient
  {
    private final ComponentName mComponentName;
    
    RegManagerDeathRecipient(ComponentName paramComponentName)
    {
      mComponentName = paramComponentName;
    }
    
    public void binderDied()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("NetworkService(");
      localStringBuilder.append(mComponentName);
      localStringBuilder.append(" transport type ");
      localStringBuilder.append(mTransportType);
      localStringBuilder.append(") died.");
      NetworkRegistrationManager.logd(localStringBuilder.toString());
    }
  }
}
