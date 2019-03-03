package com.android.internal.telephony.ims;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsService;
import com.android.ims.internal.IImsService.Stub;
import com.android.ims.internal.IImsUt;

public class ImsServiceInterfaceAdapter
  extends MmTelInterfaceAdapter
{
  private static final int SERVICE_ID = 1;
  
  public ImsServiceInterfaceAdapter(int paramInt, IBinder paramIBinder)
  {
    super(paramInt, paramIBinder);
  }
  
  private IImsService getInterface()
    throws RemoteException
  {
    IImsService localIImsService = IImsService.Stub.asInterface(mBinder);
    if (localIImsService != null) {
      return localIImsService;
    }
    throw new RemoteException("Binder not Available");
  }
  
  public void addRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException
  {
    getInterface().addRegistrationListener(mSlotId, 1, paramIImsRegistrationListener);
  }
  
  public ImsCallProfile createCallProfile(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException
  {
    return getInterface().createCallProfile(paramInt1, paramInt2, paramInt3);
  }
  
  public IImsCallSession createCallSession(int paramInt, ImsCallProfile paramImsCallProfile)
    throws RemoteException
  {
    return getInterface().createCallSession(paramInt, paramImsCallProfile, null);
  }
  
  public void endSession(int paramInt)
    throws RemoteException
  {
    getInterface().close(paramInt);
  }
  
  public IImsConfig getConfigInterface()
    throws RemoteException
  {
    return getInterface().getConfigInterface(mSlotId);
  }
  
  public IImsEcbm getEcbmInterface()
    throws RemoteException
  {
    return getInterface().getEcbmInterface(1);
  }
  
  public int getFeatureState()
    throws RemoteException
  {
    return 2;
  }
  
  public IImsMultiEndpoint getMultiEndpointInterface()
    throws RemoteException
  {
    return getInterface().getMultiEndpointInterface(1);
  }
  
  public IImsCallSession getPendingCallSession(int paramInt, String paramString)
    throws RemoteException
  {
    return getInterface().getPendingCallSession(paramInt, paramString);
  }
  
  public IImsUt getUtInterface()
    throws RemoteException
  {
    return getInterface().getUtInterface(1);
  }
  
  public boolean isConnected(int paramInt1, int paramInt2)
    throws RemoteException
  {
    return getInterface().isConnected(1, paramInt1, paramInt2);
  }
  
  public boolean isOpened()
    throws RemoteException
  {
    return getInterface().isOpened(1);
  }
  
  public void removeRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException
  {}
  
  public void setUiTTYMode(int paramInt, Message paramMessage)
    throws RemoteException
  {
    getInterface().setUiTTYMode(1, paramInt, paramMessage);
  }
  
  public int startSession(PendingIntent paramPendingIntent, IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException
  {
    return getInterface().open(mSlotId, 1, paramPendingIntent, paramIImsRegistrationListener);
  }
  
  public void turnOffIms()
    throws RemoteException
  {
    getInterface().turnOffIms(mSlotId);
  }
  
  public void turnOnIms()
    throws RemoteException
  {
    getInterface().turnOnIms(mSlotId);
  }
}
