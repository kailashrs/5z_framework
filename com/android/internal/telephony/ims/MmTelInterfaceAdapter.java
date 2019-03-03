package com.android.internal.telephony.ims;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMMTelFeature;
import com.android.ims.internal.IImsMMTelFeature.Stub;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsUt;

public class MmTelInterfaceAdapter
{
  protected IBinder mBinder;
  protected int mSlotId;
  
  public MmTelInterfaceAdapter(int paramInt, IBinder paramIBinder)
  {
    mBinder = paramIBinder;
    mSlotId = paramInt;
  }
  
  private IImsMMTelFeature getInterface()
    throws RemoteException
  {
    IImsMMTelFeature localIImsMMTelFeature = IImsMMTelFeature.Stub.asInterface(mBinder);
    if (localIImsMMTelFeature != null) {
      return localIImsMMTelFeature;
    }
    throw new RemoteException("Binder not Available");
  }
  
  public void addRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException
  {
    getInterface().addRegistrationListener(paramIImsRegistrationListener);
  }
  
  public ImsCallProfile createCallProfile(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException
  {
    return getInterface().createCallProfile(paramInt1, paramInt2, paramInt3);
  }
  
  public IImsCallSession createCallSession(int paramInt, ImsCallProfile paramImsCallProfile)
    throws RemoteException
  {
    return getInterface().createCallSession(paramInt, paramImsCallProfile);
  }
  
  public void endSession(int paramInt)
    throws RemoteException
  {
    getInterface().endSession(paramInt);
  }
  
  public IImsConfig getConfigInterface()
    throws RemoteException
  {
    return getInterface().getConfigInterface();
  }
  
  public IImsEcbm getEcbmInterface()
    throws RemoteException
  {
    return getInterface().getEcbmInterface();
  }
  
  public int getFeatureState()
    throws RemoteException
  {
    return getInterface().getFeatureStatus();
  }
  
  public IImsMultiEndpoint getMultiEndpointInterface()
    throws RemoteException
  {
    return getInterface().getMultiEndpointInterface();
  }
  
  public IImsCallSession getPendingCallSession(int paramInt, String paramString)
    throws RemoteException
  {
    return getInterface().getPendingCallSession(paramInt, paramString);
  }
  
  public IImsUt getUtInterface()
    throws RemoteException
  {
    return getInterface().getUtInterface();
  }
  
  public boolean isConnected(int paramInt1, int paramInt2)
    throws RemoteException
  {
    return getInterface().isConnected(paramInt1, paramInt2);
  }
  
  public boolean isOpened()
    throws RemoteException
  {
    return getInterface().isOpened();
  }
  
  public void removeRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException
  {
    getInterface().removeRegistrationListener(paramIImsRegistrationListener);
  }
  
  public void setUiTTYMode(int paramInt, Message paramMessage)
    throws RemoteException
  {
    getInterface().setUiTTYMode(paramInt, paramMessage);
  }
  
  public int startSession(PendingIntent paramPendingIntent, IImsRegistrationListener paramIImsRegistrationListener)
    throws RemoteException
  {
    return getInterface().startSession(paramPendingIntent, paramIImsRegistrationListener);
  }
  
  public void turnOffIms()
    throws RemoteException
  {
    getInterface().turnOffIms();
  }
  
  public void turnOnIms()
    throws RemoteException
  {
    getInterface().turnOnIms();
  }
}
