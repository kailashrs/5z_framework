package com.android.internal.telephony.dataconnection;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RegistrantList;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.CarrierConfigManager;
import android.telephony.Rlog;
import android.telephony.data.DataCallResponse;
import android.telephony.data.DataProfile;
import android.telephony.data.IDataService;
import android.telephony.data.IDataService.Stub;
import android.telephony.data.IDataServiceCallback.Stub;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataServiceManager
{
  static final String DATA_CALL_RESPONSE = "data_call_response";
  private static final boolean DBG = false;
  private static final String TAG = DataServiceManager.class.getSimpleName();
  private final AppOpsManager mAppOps;
  private boolean mBound;
  private final CarrierConfigManager mCarrierConfigManager;
  private ComponentName mComponentName;
  private final RegistrantList mDataCallListChangedRegistrants = new RegistrantList();
  private DataServiceManagerDeathRecipient mDeathRecipient;
  private IDataService mIDataService;
  private final Map<IBinder, Message> mMessageMap = new ConcurrentHashMap();
  private final IPackageManager mPackageManager;
  private final Phone mPhone;
  private final RegistrantList mServiceBindingChangedRegistrants = new RegistrantList();
  private final int mTransportType;
  
  public DataServiceManager(Phone paramPhone, int paramInt)
  {
    mPhone = paramPhone;
    mTransportType = paramInt;
    mBound = false;
    mCarrierConfigManager = ((CarrierConfigManager)paramPhone.getContext().getSystemService("carrier_config"));
    mPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
    mAppOps = ((AppOpsManager)paramPhone.getContext().getSystemService("appops"));
    bindDataService();
  }
  
  private void bindDataService()
  {
    revokePermissionsFromUnusedDataServices();
    Object localObject1 = getDataServicePackageName();
    if (TextUtils.isEmpty((CharSequence)localObject1))
    {
      loge("Can't find the binding package");
      return;
    }
    grantPermissionsToService((String)localObject1);
    try
    {
      localObject2 = mPhone.getContext();
      Object localObject3 = new android/content/Intent;
      ((Intent)localObject3).<init>("android.telephony.data.DataService");
      localObject1 = ((Intent)localObject3).setPackage((String)localObject1);
      localObject3 = new com/android/internal/telephony/dataconnection/DataServiceManager$CellularDataServiceConnection;
      ((CellularDataServiceConnection)localObject3).<init>(this, null);
      if (!((Context)localObject2).bindService((Intent)localObject1, (ServiceConnection)localObject3, 1)) {
        loge("Cannot bind to the data service.");
      }
    }
    catch (Exception localException)
    {
      Object localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Cannot bind to the data service. Exception: ");
      ((StringBuilder)localObject2).append(localException);
      loge(((StringBuilder)localObject2).toString());
    }
  }
  
  private Set<String> getAllDataServicePackageNames()
  {
    Object localObject = mPhone.getContext().getPackageManager().queryIntentServices(new Intent("android.telephony.data.DataService"), 1048576);
    HashSet localHashSet = new HashSet();
    Iterator localIterator = ((List)localObject).iterator();
    while (localIterator.hasNext())
    {
      localObject = (ResolveInfo)localIterator.next();
      if (serviceInfo != null) {
        localHashSet.add(serviceInfo.packageName);
      }
    }
    return localHashSet;
  }
  
  private String getDataServicePackageName()
  {
    return getDataServicePackageName(mTransportType);
  }
  
  private String getDataServicePackageName(int paramInt)
  {
    String str1;
    switch (paramInt)
    {
    default: 
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Transport type not WWAN or WLAN. type=");
      ((StringBuilder)localObject).append(mTransportType);
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    case 2: 
      paramInt = 17039764;
      str1 = "carrier_data_service_wlan_package_override_string";
      break;
    case 1: 
      paramInt = 17039766;
      str1 = "carrier_data_service_wwan_package_override_string";
    }
    String str2 = mPhone.getContext().getResources().getString(paramInt);
    PersistableBundle localPersistableBundle = mCarrierConfigManager.getConfigForSubId(mPhone.getSubId());
    Object localObject = str2;
    if (localPersistableBundle != null) {
      localObject = localPersistableBundle.getString(str1, str2);
    }
    return localObject;
  }
  
  private void grantPermissionsToService(String paramString)
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = paramString;
    try
    {
      mPackageManager.grantDefaultPermissionsToEnabledTelephonyDataServices(arrayOfString, mPhone.getContext().getUserId());
      mAppOps.setMode(75, mPhone.getContext().getUserId(), arrayOfString[0], 0);
      return;
    }
    catch (RemoteException paramString)
    {
      loge("Binder to package manager died, permission grant for DataService failed.");
      throw paramString.rethrowAsRuntimeException();
    }
  }
  
  private void log(String paramString)
  {
    Rlog.d(TAG, paramString);
  }
  
  private void loge(String paramString)
  {
    Rlog.e(TAG, paramString);
  }
  
  private void revokePermissionsFromUnusedDataServices()
  {
    Object localObject1 = getAllDataServicePackageNames();
    Object localObject2 = new int[2];
    Object tmp10_9 = localObject2;
    tmp10_9[0] = 1;
    Object tmp14_10 = tmp10_9;
    tmp14_10[1] = 2;
    tmp14_10;
    int i = localObject2.length;
    for (int j = 0; j < i; j++) {
      ((Set)localObject1).remove(getDataServicePackageName(localObject2[j]));
    }
    try
    {
      localObject2 = new String[((Set)localObject1).size()];
      ((Set)localObject1).toArray((Object[])localObject2);
      mPackageManager.revokeDefaultPermissionsFromDisabledTelephonyDataServices((String[])localObject2, mPhone.getContext().getUserId());
      localObject2 = ((Set)localObject1).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (String)((Iterator)localObject2).next();
        mAppOps.setMode(75, mPhone.getContext().getUserId(), (String)localObject1, 2);
      }
      return;
    }
    catch (RemoteException localRemoteException)
    {
      loge("Binder to package manager died; failed to revoke DataService permissions.");
      throw localRemoteException.rethrowAsRuntimeException();
    }
  }
  
  private void sendCompleteMessage(Message paramMessage, int paramInt)
  {
    if (paramMessage != null)
    {
      arg1 = paramInt;
      paramMessage.sendToTarget();
    }
  }
  
  public void deactivateDataCall(int paramInt1, int paramInt2, Message paramMessage)
  {
    if (!mBound)
    {
      loge("Data service not bound.");
      sendCompleteMessage(paramMessage, 4);
      return;
    }
    CellularDataServiceCallback localCellularDataServiceCallback = null;
    if (paramMessage != null)
    {
      localCellularDataServiceCallback = new CellularDataServiceCallback(null);
      mMessageMap.put(localCellularDataServiceCallback.asBinder(), paramMessage);
    }
    try
    {
      mIDataService.deactivateDataCall(mPhone.getPhoneId(), paramInt1, paramInt2, localCellularDataServiceCallback);
    }
    catch (RemoteException localRemoteException)
    {
      loge("Cannot invoke deactivateDataCall on data service.");
      if (localCellularDataServiceCallback != null) {
        mMessageMap.remove(localCellularDataServiceCallback.asBinder());
      }
      sendCompleteMessage(paramMessage, 4);
    }
  }
  
  public void getDataCallList(Message paramMessage)
  {
    if (!mBound)
    {
      loge("Data service not bound.");
      sendCompleteMessage(paramMessage, 4);
      return;
    }
    CellularDataServiceCallback localCellularDataServiceCallback = null;
    if (paramMessage != null)
    {
      localCellularDataServiceCallback = new CellularDataServiceCallback(null);
      mMessageMap.put(localCellularDataServiceCallback.asBinder(), paramMessage);
    }
    try
    {
      mIDataService.getDataCallList(mPhone.getPhoneId(), localCellularDataServiceCallback);
    }
    catch (RemoteException localRemoteException)
    {
      loge("Cannot invoke getDataCallList on data service.");
      if (localCellularDataServiceCallback != null) {
        mMessageMap.remove(localCellularDataServiceCallback.asBinder());
      }
      sendCompleteMessage(paramMessage, 4);
    }
  }
  
  public int getTransportType()
  {
    return mTransportType;
  }
  
  public void registerForDataCallListChanged(Handler paramHandler, int paramInt)
  {
    if (paramHandler != null) {
      mDataCallListChangedRegistrants.addUnique(paramHandler, paramInt, null);
    }
  }
  
  public void registerForServiceBindingChanged(Handler paramHandler, int paramInt, Object paramObject)
  {
    if (paramHandler != null) {
      mServiceBindingChangedRegistrants.addUnique(paramHandler, paramInt, paramObject);
    }
  }
  
  public void setDataProfile(List<DataProfile> paramList, boolean paramBoolean, Message paramMessage)
  {
    if (!mBound)
    {
      loge("Data service not bound.");
      sendCompleteMessage(paramMessage, 4);
      return;
    }
    CellularDataServiceCallback localCellularDataServiceCallback = null;
    if (paramMessage != null)
    {
      localCellularDataServiceCallback = new CellularDataServiceCallback(null);
      mMessageMap.put(localCellularDataServiceCallback.asBinder(), paramMessage);
    }
    try
    {
      mIDataService.setDataProfile(mPhone.getPhoneId(), paramList, paramBoolean, localCellularDataServiceCallback);
    }
    catch (RemoteException paramList)
    {
      loge("Cannot invoke setDataProfile on data service.");
      if (localCellularDataServiceCallback != null) {
        mMessageMap.remove(localCellularDataServiceCallback.asBinder());
      }
      sendCompleteMessage(paramMessage, 4);
    }
  }
  
  public void setInitialAttachApn(DataProfile paramDataProfile, boolean paramBoolean, Message paramMessage)
  {
    if (!mBound)
    {
      loge("Data service not bound.");
      sendCompleteMessage(paramMessage, 4);
      return;
    }
    CellularDataServiceCallback localCellularDataServiceCallback = null;
    if (paramMessage != null)
    {
      localCellularDataServiceCallback = new CellularDataServiceCallback(null);
      mMessageMap.put(localCellularDataServiceCallback.asBinder(), paramMessage);
    }
    try
    {
      mIDataService.setInitialAttachApn(mPhone.getPhoneId(), paramDataProfile, paramBoolean, localCellularDataServiceCallback);
    }
    catch (RemoteException paramDataProfile)
    {
      loge("Cannot invoke setInitialAttachApn on data service.");
      if (localCellularDataServiceCallback != null) {
        mMessageMap.remove(localCellularDataServiceCallback.asBinder());
      }
      sendCompleteMessage(paramMessage, 4);
    }
  }
  
  public void setupDataCall(int paramInt1, DataProfile paramDataProfile, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, LinkProperties paramLinkProperties, Message paramMessage)
  {
    if (!mBound)
    {
      loge("Data service not bound.");
      sendCompleteMessage(paramMessage, 4);
      return;
    }
    CellularDataServiceCallback localCellularDataServiceCallback = null;
    if (paramMessage != null)
    {
      localCellularDataServiceCallback = new CellularDataServiceCallback(null);
      mMessageMap.put(localCellularDataServiceCallback.asBinder(), paramMessage);
    }
    try
    {
      mIDataService.setupDataCall(mPhone.getPhoneId(), paramInt1, paramDataProfile, paramBoolean1, paramBoolean2, paramInt2, paramLinkProperties, localCellularDataServiceCallback);
    }
    catch (RemoteException paramDataProfile)
    {
      loge("Cannot invoke setupDataCall on data service.");
      if (localCellularDataServiceCallback != null) {
        mMessageMap.remove(localCellularDataServiceCallback.asBinder());
      }
      sendCompleteMessage(paramMessage, 4);
    }
  }
  
  public void unregisterForDataCallListChanged(Handler paramHandler)
  {
    if (paramHandler != null) {
      mDataCallListChangedRegistrants.remove(paramHandler);
    }
  }
  
  public void unregisterForServiceBindingChanged(Handler paramHandler)
  {
    if (paramHandler != null) {
      mServiceBindingChangedRegistrants.remove(paramHandler);
    }
  }
  
  private final class CellularDataServiceCallback
    extends IDataServiceCallback.Stub
  {
    private CellularDataServiceCallback() {}
    
    public void onDataCallListChanged(List<DataCallResponse> paramList)
    {
      mDataCallListChangedRegistrants.notifyRegistrants(new AsyncResult(null, paramList, null));
    }
    
    public void onDeactivateDataCallComplete(int paramInt)
    {
      Message localMessage = (Message)mMessageMap.remove(asBinder());
      DataServiceManager.this.sendCompleteMessage(localMessage, paramInt);
    }
    
    public void onGetDataCallListComplete(int paramInt, List<DataCallResponse> paramList)
    {
      paramList = (Message)mMessageMap.remove(asBinder());
      DataServiceManager.this.sendCompleteMessage(paramList, paramInt);
    }
    
    public void onSetDataProfileComplete(int paramInt)
    {
      Message localMessage = (Message)mMessageMap.remove(asBinder());
      DataServiceManager.this.sendCompleteMessage(localMessage, paramInt);
    }
    
    public void onSetInitialAttachApnComplete(int paramInt)
    {
      Message localMessage = (Message)mMessageMap.remove(asBinder());
      DataServiceManager.this.sendCompleteMessage(localMessage, paramInt);
    }
    
    public void onSetupDataCallComplete(int paramInt, DataCallResponse paramDataCallResponse)
    {
      Message localMessage = (Message)mMessageMap.remove(asBinder());
      if (localMessage != null)
      {
        localMessage.getData().putParcelable("data_call_response", paramDataCallResponse);
        DataServiceManager.this.sendCompleteMessage(localMessage, paramInt);
      }
      else
      {
        DataServiceManager.this.loge("Unable to find the message for setup call response.");
      }
    }
  }
  
  private final class CellularDataServiceConnection
    implements ServiceConnection
  {
    private CellularDataServiceConnection() {}
    
    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      DataServiceManager.access$002(DataServiceManager.this, paramComponentName);
      DataServiceManager.access$302(DataServiceManager.this, IDataService.Stub.asInterface(paramIBinder));
      DataServiceManager.access$402(DataServiceManager.this, new DataServiceManager.DataServiceManagerDeathRecipient(DataServiceManager.this, null));
      DataServiceManager.access$602(DataServiceManager.this, true);
      try
      {
        paramIBinder.linkToDeath(mDeathRecipient, 0);
        mIDataService.createDataServiceProvider(mPhone.getPhoneId());
        paramComponentName = mIDataService;
        int i = mPhone.getPhoneId();
        paramIBinder = new com/android/internal/telephony/dataconnection/DataServiceManager$CellularDataServiceCallback;
        paramIBinder.<init>(DataServiceManager.this, null);
        paramComponentName.registerForDataCallListChanged(i, paramIBinder);
        mServiceBindingChangedRegistrants.notifyResult(Boolean.valueOf(true));
        return;
      }
      catch (RemoteException localRemoteException)
      {
        mDeathRecipient.binderDied();
        paramComponentName = DataServiceManager.this;
        paramIBinder = new StringBuilder();
        paramIBinder.append("Remote exception. ");
        paramIBinder.append(localRemoteException);
        paramComponentName.loge(paramIBinder.toString());
      }
    }
    
    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      mIDataService.asBinder().unlinkToDeath(mDeathRecipient, 0);
      DataServiceManager.access$302(DataServiceManager.this, null);
      DataServiceManager.access$602(DataServiceManager.this, false);
      mServiceBindingChangedRegistrants.notifyResult(Boolean.valueOf(false));
    }
  }
  
  private class DataServiceManagerDeathRecipient
    implements IBinder.DeathRecipient
  {
    private DataServiceManagerDeathRecipient() {}
    
    public void binderDied()
    {
      DataServiceManager localDataServiceManager = DataServiceManager.this;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("DataService(");
      localStringBuilder.append(mComponentName);
      localStringBuilder.append(" transport type ");
      localStringBuilder.append(mTransportType);
      localStringBuilder.append(") died.");
      localDataServiceManager.loge(localStringBuilder.toString());
    }
  }
}
