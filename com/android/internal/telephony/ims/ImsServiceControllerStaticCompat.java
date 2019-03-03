package com.android.internal.telephony.ims;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.ims.internal.IImsFeatureStatusCallback;
import com.android.ims.internal.IImsService;
import com.android.ims.internal.IImsService.Stub;

public class ImsServiceControllerStaticCompat
  extends ImsServiceControllerCompat
{
  private static final String IMS_SERVICE_NAME = "ims";
  private static final String TAG = "ImsSCStaticCompat";
  private IImsService mImsServiceCompat = null;
  
  public ImsServiceControllerStaticCompat(Context paramContext, ComponentName paramComponentName, ImsServiceController.ImsServiceControllerCallbacks paramImsServiceControllerCallbacks)
  {
    super(paramContext, paramComponentName, paramImsServiceControllerCallbacks);
  }
  
  protected MmTelInterfaceAdapter getInterface(int paramInt, IImsFeatureStatusCallback paramIImsFeatureStatusCallback)
    throws RemoteException
  {
    if (mImsServiceCompat == null)
    {
      Log.w("ImsSCStaticCompat", "getInterface: IImsService returned null.");
      return null;
    }
    return new ImsServiceInterfaceAdapter(paramInt, mImsServiceCompat.asBinder());
  }
  
  protected void setServiceController(IBinder paramIBinder)
  {
    mImsServiceCompat = IImsService.Stub.asInterface(paramIBinder);
  }
  
  public boolean startBindToService(Intent paramIntent, ImsServiceController.ImsServiceConnection paramImsServiceConnection, int paramInt)
  {
    paramIntent = ServiceManager.checkService("ims");
    if (paramIntent == null) {
      return false;
    }
    paramImsServiceConnection.onServiceConnected(new ComponentName(mContext, ImsServiceControllerStaticCompat.class), paramIntent);
    return true;
  }
}
