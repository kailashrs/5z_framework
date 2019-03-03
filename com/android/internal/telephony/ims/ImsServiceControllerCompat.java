package com.android.internal.telephony.ims;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsConfig;
import android.telephony.ims.aidl.IImsMmTelFeature;
import android.telephony.ims.aidl.IImsRcsFeature;
import android.telephony.ims.aidl.IImsRegistration;
import android.util.Log;
import android.util.SparseArray;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.ims.internal.IImsMMTelFeature;
import com.android.ims.internal.IImsServiceController;
import com.android.ims.internal.IImsServiceController.Stub;

public class ImsServiceControllerCompat
  extends ImsServiceController
{
  private static final String TAG = "ImsSCCompat";
  private final SparseArray<ImsConfigCompatAdapter> mConfigCompatAdapters = new SparseArray();
  private final SparseArray<MmTelFeatureCompatAdapter> mMmTelCompatAdapters = new SparseArray();
  private final SparseArray<ImsRegistrationCompatAdapter> mRegCompatAdapters = new SparseArray();
  private IImsServiceController mServiceController;
  
  public ImsServiceControllerCompat(Context paramContext, ComponentName paramComponentName, ImsServiceController.ImsServiceControllerCallbacks paramImsServiceControllerCallbacks)
  {
    super(paramContext, paramComponentName, paramImsServiceControllerCallbacks);
  }
  
  private IImsMmTelFeature createMMTelCompat(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException
  {
    paramIImsFeatureStatusCallback = getInterface(paramInt, paramIImsFeatureStatusCallback);
    MmTelFeatureCompatAdapter localMmTelFeatureCompatAdapter = new MmTelFeatureCompatAdapter(mContext, paramInt, paramIImsFeatureStatusCallback);
    mMmTelCompatAdapters.put(paramInt, localMmTelFeatureCompatAdapter);
    paramIImsFeatureStatusCallback = new ImsRegistrationCompatAdapter();
    localMmTelFeatureCompatAdapter.addRegistrationAdapter(paramIImsFeatureStatusCallback);
    mRegCompatAdapters.put(paramInt, paramIImsFeatureStatusCallback);
    mConfigCompatAdapters.put(paramInt, new ImsConfigCompatAdapter(localMmTelFeatureCompatAdapter.getOldConfigInterface()));
    return localMmTelFeatureCompatAdapter.getBinder();
  }
  
  private IImsRcsFeature createRcsFeature(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
  {
    return null;
  }
  
  protected IInterface createImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException
  {
    switch (paramInt2)
    {
    default: 
      return null;
    case 2: 
      return createRcsFeature(paramInt1, paramIImsFeatureStatusCallback);
    }
    return createMMTelCompat(paramInt1, paramIImsFeatureStatusCallback);
  }
  
  public void disableIms(int paramInt)
  {
    Object localObject = (MmTelFeatureCompatAdapter)mMmTelCompatAdapters.get(paramInt);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("enableIms: adapter null for slot :");
      ((StringBuilder)localObject).append(paramInt);
      Log.w("ImsSCCompat", ((StringBuilder)localObject).toString());
      return;
    }
    try
    {
      ((MmTelFeatureCompatAdapter)localObject).disableIms();
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Couldn't enable IMS: ");
      localStringBuilder.append(localRemoteException.getMessage());
      Log.w("ImsSCCompat", localStringBuilder.toString());
    }
  }
  
  public void enableIms(int paramInt)
  {
    Object localObject = (MmTelFeatureCompatAdapter)mMmTelCompatAdapters.get(paramInt);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("enableIms: adapter null for slot :");
      ((StringBuilder)localObject).append(paramInt);
      Log.w("ImsSCCompat", ((StringBuilder)localObject).toString());
      return;
    }
    try
    {
      ((MmTelFeatureCompatAdapter)localObject).enableIms();
    }
    catch (RemoteException localRemoteException)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Couldn't enable IMS: ");
      ((StringBuilder)localObject).append(localRemoteException.getMessage());
      Log.w("ImsSCCompat", ((StringBuilder)localObject).toString());
    }
  }
  
  public IImsConfig getConfig(int paramInt)
    throws RemoteException
  {
    Object localObject = (ImsConfigCompatAdapter)mConfigCompatAdapters.get(paramInt);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getConfig: Config does not exist for slot ");
      ((StringBuilder)localObject).append(paramInt);
      Log.w("ImsSCCompat", ((StringBuilder)localObject).toString());
      return null;
    }
    return ((ImsConfigCompatAdapter)localObject).getIImsConfig();
  }
  
  protected MmTelInterfaceAdapter getInterface(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException
  {
    paramIImsFeatureStatusCallback = mServiceController.createMMTelFeature(paramInt, paramIImsFeatureStatusCallback);
    if (paramIImsFeatureStatusCallback == null)
    {
      Log.w("ImsSCCompat", "createMMTelCompat: createMMTelFeature returned null.");
      return null;
    }
    return new MmTelInterfaceAdapter(paramInt, paramIImsFeatureStatusCallback.asBinder());
  }
  
  public IImsRegistration getRegistration(int paramInt)
    throws RemoteException
  {
    Object localObject = (ImsRegistrationCompatAdapter)mRegCompatAdapters.get(paramInt);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("getRegistration: Registration does not exist for slot ");
      ((StringBuilder)localObject).append(paramInt);
      Log.w("ImsSCCompat", ((StringBuilder)localObject).toString());
      return null;
    }
    return ((ImsRegistrationCompatAdapter)localObject).getBinder();
  }
  
  protected String getServiceInterface()
  {
    return "android.telephony.ims.compat.ImsService";
  }
  
  protected boolean isServiceControllerAvailable()
  {
    boolean bool;
    if (mServiceController != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected void notifyImsServiceReady()
    throws RemoteException
  {
    Log.d("ImsSCCompat", "notifyImsServiceReady");
  }
  
  protected void removeImsFeature(int paramInt1, int paramInt2, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException
  {
    if (paramInt2 == 1)
    {
      mMmTelCompatAdapters.remove(paramInt1);
      mRegCompatAdapters.remove(paramInt1);
      mConfigCompatAdapters.remove(paramInt1);
    }
    mServiceController.removeImsFeature(paramInt1, paramInt2, paramIImsFeatureStatusCallback);
  }
  
  protected void setServiceController(IBinder paramIBinder)
  {
    mServiceController = IImsServiceController.Stub.asInterface(paramIBinder);
  }
}
