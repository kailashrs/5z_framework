package com.android.internal.telephony.ims;

import android.net.Uri;
import android.os.RemoteException;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.stub.ImsRegistrationImplBase;
import android.util.ArrayMap;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsRegistrationListener.Stub;
import java.util.Map;

public class ImsRegistrationCompatAdapter
  extends ImsRegistrationImplBase
{
  private static final Map<Integer, Integer> RADIO_TECH_MAPPER = new ArrayMap(2);
  private final IImsRegistrationListener mListener = new IImsRegistrationListener.Stub()
  {
    public void registrationAssociatedUriChanged(Uri[] paramAnonymousArrayOfUri)
      throws RemoteException
    {
      onSubscriberAssociatedUriChanged(paramAnonymousArrayOfUri);
    }
    
    public void registrationChangeFailed(int paramAnonymousInt, ImsReasonInfo paramAnonymousImsReasonInfo)
      throws RemoteException
    {
      onTechnologyChangeFailed(((Integer)ImsRegistrationCompatAdapter.RADIO_TECH_MAPPER.getOrDefault(Integer.valueOf(paramAnonymousInt), Integer.valueOf(-1))).intValue(), paramAnonymousImsReasonInfo);
    }
    
    public void registrationConnected()
      throws RemoteException
    {
      onRegistered(-1);
    }
    
    public void registrationConnectedWithRadioTech(int paramAnonymousInt)
      throws RemoteException
    {
      onRegistered(((Integer)ImsRegistrationCompatAdapter.RADIO_TECH_MAPPER.getOrDefault(Integer.valueOf(paramAnonymousInt), Integer.valueOf(-1))).intValue());
    }
    
    public void registrationDisconnected(ImsReasonInfo paramAnonymousImsReasonInfo)
      throws RemoteException
    {
      onDeregistered(paramAnonymousImsReasonInfo);
    }
    
    public void registrationFeatureCapabilityChanged(int paramAnonymousInt, int[] paramAnonymousArrayOfInt1, int[] paramAnonymousArrayOfInt2)
      throws RemoteException
    {}
    
    public void registrationProgressing()
      throws RemoteException
    {
      onRegistering(-1);
    }
    
    public void registrationProgressingWithRadioTech(int paramAnonymousInt)
      throws RemoteException
    {
      onRegistering(((Integer)ImsRegistrationCompatAdapter.RADIO_TECH_MAPPER.getOrDefault(Integer.valueOf(paramAnonymousInt), Integer.valueOf(-1))).intValue());
    }
    
    public void registrationResumed()
      throws RemoteException
    {}
    
    public void registrationServiceCapabilityChanged(int paramAnonymousInt1, int paramAnonymousInt2)
      throws RemoteException
    {}
    
    public void registrationSuspended()
      throws RemoteException
    {}
    
    public void voiceMessageCountUpdate(int paramAnonymousInt)
      throws RemoteException
    {}
  };
  
  static
  {
    RADIO_TECH_MAPPER.put(Integer.valueOf(14), Integer.valueOf(0));
    RADIO_TECH_MAPPER.put(Integer.valueOf(18), Integer.valueOf(1));
  }
  
  public ImsRegistrationCompatAdapter() {}
  
  public IImsRegistrationListener getRegistrationListener()
  {
    return mListener;
  }
}
