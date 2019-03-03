package android.telephony.ims;

import android.annotation.SystemApi;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsCallSessionListener;
import android.telephony.ims.stub.ImsCallSessionImplBase;
import com.android.ims.internal.IImsCallSession;

@SystemApi
public class ImsCallSessionListener
{
  private final IImsCallSessionListener mListener;
  
  public ImsCallSessionListener(IImsCallSessionListener paramIImsCallSessionListener)
  {
    mListener = paramIImsCallSessionListener;
  }
  
  public void callSessionConferenceExtendFailed(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionConferenceExtendFailed(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionConferenceExtendReceived(ImsCallSessionImplBase paramImsCallSessionImplBase, ImsCallProfile paramImsCallProfile)
  {
    try
    {
      IImsCallSessionListener localIImsCallSessionListener = mListener;
      if (paramImsCallSessionImplBase != null) {
        paramImsCallSessionImplBase = paramImsCallSessionImplBase.getServiceImpl();
      } else {
        paramImsCallSessionImplBase = null;
      }
      localIImsCallSessionListener.callSessionConferenceExtendReceived(paramImsCallSessionImplBase, paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallSessionImplBase)
    {
      throw new RuntimeException(paramImsCallSessionImplBase);
    }
  }
  
  public void callSessionConferenceExtendReceived(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionConferenceExtendReceived(paramIImsCallSession, paramImsCallProfile);
      return;
    }
    catch (RemoteException paramIImsCallSession)
    {
      throw new RuntimeException(paramIImsCallSession);
    }
  }
  
  public void callSessionConferenceExtended(ImsCallSessionImplBase paramImsCallSessionImplBase, ImsCallProfile paramImsCallProfile)
  {
    try
    {
      IImsCallSessionListener localIImsCallSessionListener = mListener;
      if (paramImsCallSessionImplBase != null) {
        paramImsCallSessionImplBase = paramImsCallSessionImplBase.getServiceImpl();
      } else {
        paramImsCallSessionImplBase = null;
      }
      localIImsCallSessionListener.callSessionConferenceExtended(paramImsCallSessionImplBase, paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallSessionImplBase)
    {
      throw new RuntimeException(paramImsCallSessionImplBase);
    }
  }
  
  public void callSessionConferenceExtended(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionConferenceExtended(paramIImsCallSession, paramImsCallProfile);
      return;
    }
    catch (RemoteException paramIImsCallSession)
    {
      throw new RuntimeException(paramIImsCallSession);
    }
  }
  
  public void callSessionConferenceMaxnumUserCountUpdated(int paramInt)
  {
    try
    {
      mListener.callSessionConferenceMaxnumUserCountUpdated(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public void callSessionConferenceStateUpdated(ImsConferenceState paramImsConferenceState)
  {
    try
    {
      mListener.callSessionConferenceStateUpdated(paramImsConferenceState);
      return;
    }
    catch (RemoteException paramImsConferenceState)
    {
      throw new RuntimeException(paramImsConferenceState);
    }
  }
  
  public void callSessionHandover(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionHandover(paramInt1, paramInt2, paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionHandoverFailed(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionHandoverFailed(paramInt1, paramInt2, paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionHeld(ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionHeld(paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallProfile)
    {
      throw new RuntimeException(paramImsCallProfile);
    }
  }
  
  public void callSessionHoldFailed(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionHoldFailed(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionHoldReceived(ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionHoldReceived(paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallProfile)
    {
      throw new RuntimeException(paramImsCallProfile);
    }
  }
  
  public void callSessionInitiated(ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionInitiated(paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallProfile)
    {
      throw new RuntimeException(paramImsCallProfile);
    }
  }
  
  public void callSessionInitiatedFailed(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionInitiatedFailed(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionInviteParticipantsRequestDelivered()
  {
    try
    {
      mListener.callSessionInviteParticipantsRequestDelivered();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public void callSessionInviteParticipantsRequestFailed(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionInviteParticipantsRequestFailed(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionMayHandover(int paramInt1, int paramInt2)
  {
    try
    {
      mListener.callSessionMayHandover(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public void callSessionMergeComplete(ImsCallSessionImplBase paramImsCallSessionImplBase)
  {
    try
    {
      IImsCallSessionListener localIImsCallSessionListener = mListener;
      if (paramImsCallSessionImplBase != null) {
        paramImsCallSessionImplBase = paramImsCallSessionImplBase.getServiceImpl();
      } else {
        paramImsCallSessionImplBase = null;
      }
      localIImsCallSessionListener.callSessionMergeComplete(paramImsCallSessionImplBase);
      return;
    }
    catch (RemoteException paramImsCallSessionImplBase)
    {
      throw new RuntimeException(paramImsCallSessionImplBase);
    }
  }
  
  public void callSessionMergeComplete(IImsCallSession paramIImsCallSession)
  {
    try
    {
      mListener.callSessionMergeComplete(paramIImsCallSession);
      return;
    }
    catch (RemoteException paramIImsCallSession)
    {
      throw new RuntimeException(paramIImsCallSession);
    }
  }
  
  public void callSessionMergeFailed(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionMergeFailed(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionMergeStarted(ImsCallSessionImplBase paramImsCallSessionImplBase, ImsCallProfile paramImsCallProfile)
  {
    try
    {
      IImsCallSessionListener localIImsCallSessionListener = mListener;
      if (paramImsCallSessionImplBase != null) {
        paramImsCallSessionImplBase = paramImsCallSessionImplBase.getServiceImpl();
      } else {
        paramImsCallSessionImplBase = null;
      }
      localIImsCallSessionListener.callSessionMergeStarted(paramImsCallSessionImplBase, paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallSessionImplBase)
    {
      throw new RuntimeException(paramImsCallSessionImplBase);
    }
  }
  
  public void callSessionMergeStarted(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionMergeStarted(paramIImsCallSession, paramImsCallProfile);
      return;
    }
    catch (RemoteException paramIImsCallSession)
    {
      throw new RuntimeException(paramIImsCallSession);
    }
  }
  
  public void callSessionMultipartyStateChanged(boolean paramBoolean)
  {
    try
    {
      mListener.callSessionMultipartyStateChanged(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public void callSessionProgressing(ImsStreamMediaProfile paramImsStreamMediaProfile)
  {
    try
    {
      mListener.callSessionProgressing(paramImsStreamMediaProfile);
      return;
    }
    catch (RemoteException paramImsStreamMediaProfile)
    {
      throw new RuntimeException(paramImsStreamMediaProfile);
    }
  }
  
  public void callSessionPropertyChanged(int paramInt)
  {
    try
    {
      mListener.callSessionPropertyChanged(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public void callSessionRemoveParticipantsRequestDelivered()
  {
    try
    {
      mListener.callSessionRemoveParticipantsRequestDelivered();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public void callSessionRemoveParticipantsRequestFailed(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionInviteParticipantsRequestFailed(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionResumeFailed(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionResumeFailed(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionResumeReceived(ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionResumeReceived(paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallProfile)
    {
      throw new RuntimeException(paramImsCallProfile);
    }
  }
  
  public void callSessionResumed(ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionResumed(paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallProfile)
    {
      throw new RuntimeException(paramImsCallProfile);
    }
  }
  
  public void callSessionRttMessageReceived(String paramString)
  {
    try
    {
      mListener.callSessionRttMessageReceived(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw new RuntimeException(paramString);
    }
  }
  
  public void callSessionRttModifyRequestReceived(ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionRttModifyRequestReceived(paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallProfile)
    {
      throw new RuntimeException(paramImsCallProfile);
    }
  }
  
  public void callSessionRttModifyResponseReceived(int paramInt)
  {
    try
    {
      mListener.callSessionRttModifyResponseReceived(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public void callSessionSuppServiceReceived(ImsSuppServiceNotification paramImsSuppServiceNotification)
  {
    try
    {
      mListener.callSessionSuppServiceReceived(paramImsSuppServiceNotification);
      return;
    }
    catch (RemoteException paramImsSuppServiceNotification)
    {
      throw new RuntimeException(paramImsSuppServiceNotification);
    }
  }
  
  public void callSessionTerminated(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionTerminated(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionTtyModeReceived(int paramInt)
  {
    try
    {
      mListener.callSessionTtyModeReceived(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeException(localRemoteException);
    }
  }
  
  public void callSessionUpdateFailed(ImsReasonInfo paramImsReasonInfo)
  {
    try
    {
      mListener.callSessionUpdateFailed(paramImsReasonInfo);
      return;
    }
    catch (RemoteException paramImsReasonInfo)
    {
      throw new RuntimeException(paramImsReasonInfo);
    }
  }
  
  public void callSessionUpdateReceived(ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionUpdateReceived(paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallProfile)
    {
      throw new RuntimeException(paramImsCallProfile);
    }
  }
  
  public void callSessionUpdated(ImsCallProfile paramImsCallProfile)
  {
    try
    {
      mListener.callSessionUpdated(paramImsCallProfile);
      return;
    }
    catch (RemoteException paramImsCallProfile)
    {
      throw new RuntimeException(paramImsCallProfile);
    }
  }
  
  public void callSessionUssdMessageReceived(int paramInt, String paramString)
  {
    try
    {
      mListener.callSessionUssdMessageReceived(paramInt, paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw new RuntimeException(paramString);
    }
  }
}
