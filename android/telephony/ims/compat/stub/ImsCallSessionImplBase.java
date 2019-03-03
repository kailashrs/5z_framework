package android.telephony.ims.compat.stub;

import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSession.Stub;
import com.android.ims.internal.IImsCallSessionListener.Stub;
import com.android.ims.internal.IImsVideoCallProvider;

public class ImsCallSessionImplBase
  extends IImsCallSession.Stub
{
  public ImsCallSessionImplBase() {}
  
  public void accept(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile) {}
  
  public void close() {}
  
  public void deflect(String paramString) {}
  
  public void extendToConference(String[] paramArrayOfString) {}
  
  public String getCallId()
  {
    return null;
  }
  
  public ImsCallProfile getCallProfile()
  {
    return null;
  }
  
  public ImsCallProfile getLocalCallProfile()
  {
    return null;
  }
  
  public String getProperty(String paramString)
  {
    return null;
  }
  
  public ImsCallProfile getRemoteCallProfile()
  {
    return null;
  }
  
  public int getState()
  {
    return -1;
  }
  
  public IImsVideoCallProvider getVideoCallProvider()
  {
    return null;
  }
  
  public void hold(ImsStreamMediaProfile paramImsStreamMediaProfile) {}
  
  public void inviteParticipants(String[] paramArrayOfString) {}
  
  public boolean isInCall()
  {
    return false;
  }
  
  public boolean isMultiparty()
  {
    return false;
  }
  
  public void merge() {}
  
  public void reject(int paramInt) {}
  
  public void removeParticipants(String[] paramArrayOfString) {}
  
  public void resume(ImsStreamMediaProfile paramImsStreamMediaProfile) {}
  
  public void sendDtmf(char paramChar, Message paramMessage) {}
  
  public void sendRttMessage(String paramString) {}
  
  public void sendRttModifyRequest(ImsCallProfile paramImsCallProfile) {}
  
  public void sendRttModifyResponse(boolean paramBoolean) {}
  
  public void sendUssd(String paramString) {}
  
  public final void setListener(android.telephony.ims.aidl.IImsCallSessionListener paramIImsCallSessionListener)
    throws RemoteException
  {
    setListener(new ImsCallSessionListenerConverter(paramIImsCallSessionListener));
  }
  
  public void setListener(com.android.ims.internal.IImsCallSessionListener paramIImsCallSessionListener) {}
  
  public void setMute(boolean paramBoolean) {}
  
  public void start(String paramString, ImsCallProfile paramImsCallProfile) {}
  
  public void startConference(String[] paramArrayOfString, ImsCallProfile paramImsCallProfile) {}
  
  public void startDtmf(char paramChar) {}
  
  public void stopDtmf() {}
  
  public void terminate(int paramInt) {}
  
  public void update(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile) {}
  
  private class ImsCallSessionListenerConverter
    extends IImsCallSessionListener.Stub
  {
    private final android.telephony.ims.aidl.IImsCallSessionListener mNewListener;
    
    public ImsCallSessionListenerConverter(android.telephony.ims.aidl.IImsCallSessionListener paramIImsCallSessionListener)
    {
      mNewListener = paramIImsCallSessionListener;
    }
    
    public void callSessionConferenceExtendFailed(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionConferenceExtendFailed(paramImsReasonInfo);
    }
    
    public void callSessionConferenceExtendReceived(IImsCallSession paramIImsCallSession1, IImsCallSession paramIImsCallSession2, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionConferenceExtendReceived(paramIImsCallSession2, paramImsCallProfile);
    }
    
    public void callSessionConferenceExtended(IImsCallSession paramIImsCallSession1, IImsCallSession paramIImsCallSession2, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionConferenceExtended(paramIImsCallSession2, paramImsCallProfile);
    }
    
    public void callSessionConferenceMaxnumUserCountUpdated(IImsCallSession paramIImsCallSession, int paramInt)
      throws RemoteException
    {
      mNewListener.callSessionConferenceMaxnumUserCountUpdated(paramInt);
    }
    
    public void callSessionConferenceStateUpdated(IImsCallSession paramIImsCallSession, ImsConferenceState paramImsConferenceState)
      throws RemoteException
    {
      mNewListener.callSessionConferenceStateUpdated(paramImsConferenceState);
    }
    
    public void callSessionHandover(IImsCallSession paramIImsCallSession, int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionHandover(paramInt1, paramInt2, paramImsReasonInfo);
    }
    
    public void callSessionHandoverFailed(IImsCallSession paramIImsCallSession, int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionHandoverFailed(paramInt1, paramInt2, paramImsReasonInfo);
    }
    
    public void callSessionHeld(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionHeld(paramImsCallProfile);
    }
    
    public void callSessionHoldFailed(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionHoldFailed(paramImsReasonInfo);
    }
    
    public void callSessionHoldReceived(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionHoldReceived(paramImsCallProfile);
    }
    
    public void callSessionInviteParticipantsRequestDelivered(IImsCallSession paramIImsCallSession)
      throws RemoteException
    {
      mNewListener.callSessionInviteParticipantsRequestDelivered();
    }
    
    public void callSessionInviteParticipantsRequestFailed(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionInviteParticipantsRequestFailed(paramImsReasonInfo);
    }
    
    public void callSessionMayHandover(IImsCallSession paramIImsCallSession, int paramInt1, int paramInt2)
      throws RemoteException
    {
      mNewListener.callSessionMayHandover(paramInt1, paramInt2);
    }
    
    public void callSessionMergeComplete(IImsCallSession paramIImsCallSession)
      throws RemoteException
    {
      mNewListener.callSessionMergeComplete(paramIImsCallSession);
    }
    
    public void callSessionMergeFailed(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionMergeFailed(paramImsReasonInfo);
    }
    
    public void callSessionMergeStarted(IImsCallSession paramIImsCallSession1, IImsCallSession paramIImsCallSession2, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionMergeStarted(paramIImsCallSession2, paramImsCallProfile);
    }
    
    public void callSessionMultipartyStateChanged(IImsCallSession paramIImsCallSession, boolean paramBoolean)
      throws RemoteException
    {
      mNewListener.callSessionMultipartyStateChanged(paramBoolean);
    }
    
    public void callSessionProgressing(IImsCallSession paramIImsCallSession, ImsStreamMediaProfile paramImsStreamMediaProfile)
      throws RemoteException
    {
      mNewListener.callSessionProgressing(paramImsStreamMediaProfile);
    }
    
    public void callSessionPropertyChanged(int paramInt)
      throws RemoteException
    {
      mNewListener.callSessionPropertyChanged(paramInt);
    }
    
    public void callSessionRemoveParticipantsRequestDelivered(IImsCallSession paramIImsCallSession)
      throws RemoteException
    {
      mNewListener.callSessionRemoveParticipantsRequestDelivered();
    }
    
    public void callSessionRemoveParticipantsRequestFailed(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionRemoveParticipantsRequestFailed(paramImsReasonInfo);
    }
    
    public void callSessionResumeFailed(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionResumeFailed(paramImsReasonInfo);
    }
    
    public void callSessionResumeReceived(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionResumeReceived(paramImsCallProfile);
    }
    
    public void callSessionResumed(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionResumed(paramImsCallProfile);
    }
    
    public void callSessionRttMessageReceived(String paramString)
      throws RemoteException
    {
      mNewListener.callSessionRttMessageReceived(paramString);
    }
    
    public void callSessionRttModifyRequestReceived(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionRttModifyRequestReceived(paramImsCallProfile);
    }
    
    public void callSessionRttModifyResponseReceived(int paramInt)
      throws RemoteException
    {
      mNewListener.callSessionRttModifyResponseReceived(paramInt);
    }
    
    public void callSessionStartFailed(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionInitiatedFailed(paramImsReasonInfo);
    }
    
    public void callSessionStarted(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionInitiated(paramImsCallProfile);
    }
    
    public void callSessionSuppServiceReceived(IImsCallSession paramIImsCallSession, ImsSuppServiceNotification paramImsSuppServiceNotification)
      throws RemoteException
    {
      mNewListener.callSessionSuppServiceReceived(paramImsSuppServiceNotification);
    }
    
    public void callSessionTerminated(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionTerminated(paramImsReasonInfo);
    }
    
    public void callSessionTtyModeReceived(IImsCallSession paramIImsCallSession, int paramInt)
      throws RemoteException
    {
      mNewListener.callSessionTtyModeReceived(paramInt);
    }
    
    public void callSessionUpdateFailed(IImsCallSession paramIImsCallSession, ImsReasonInfo paramImsReasonInfo)
      throws RemoteException
    {
      mNewListener.callSessionUpdateFailed(paramImsReasonInfo);
    }
    
    public void callSessionUpdateReceived(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionUpdateReceived(paramImsCallProfile);
    }
    
    public void callSessionUpdated(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
      throws RemoteException
    {
      mNewListener.callSessionUpdated(paramImsCallProfile);
    }
    
    public void callSessionUssdMessageReceived(IImsCallSession paramIImsCallSession, int paramInt, String paramString)
      throws RemoteException
    {
      mNewListener.callSessionUssdMessageReceived(paramInt, paramString);
    }
  }
}
