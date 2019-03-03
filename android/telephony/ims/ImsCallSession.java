package android.telephony.ims;

import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsCallSessionListener.Stub;
import android.util.Log;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsVideoCallProvider;
import java.util.Objects;

public class ImsCallSession
{
  private static final String TAG = "ImsCallSession";
  private boolean mClosed = false;
  private Listener mListener;
  private final IImsCallSession miSession;
  
  public ImsCallSession(IImsCallSession paramIImsCallSession)
  {
    miSession = paramIImsCallSession;
    if (paramIImsCallSession != null) {
      try
      {
        IImsCallSessionListenerProxy localIImsCallSessionListenerProxy = new android/telephony/ims/ImsCallSession$IImsCallSessionListenerProxy;
        localIImsCallSessionListenerProxy.<init>(this, null);
        paramIImsCallSession.setListener(localIImsCallSessionListenerProxy);
      }
      catch (RemoteException paramIImsCallSession) {}
    } else {
      mClosed = true;
    }
  }
  
  public ImsCallSession(IImsCallSession paramIImsCallSession, Listener paramListener)
  {
    this(paramIImsCallSession);
    setListener(paramListener);
  }
  
  public void accept(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.accept(paramInt, paramImsStreamMediaProfile);
    }
    catch (RemoteException paramImsStreamMediaProfile) {}
  }
  
  public void close()
  {
    try
    {
      if (mClosed) {
        return;
      }
      try
      {
        miSession.close();
        mClosed = true;
      }
      catch (RemoteException localRemoteException) {}
      return;
    }
    finally {}
  }
  
  public void deflect(String paramString)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.deflect(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void extendToConference(String[] paramArrayOfString)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.extendToConference(paramArrayOfString);
    }
    catch (RemoteException paramArrayOfString) {}
  }
  
  public String getCallId()
  {
    if (mClosed) {
      return null;
    }
    try
    {
      String str = miSession.getCallId();
      return str;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public ImsCallProfile getCallProfile()
  {
    if (mClosed) {
      return null;
    }
    try
    {
      ImsCallProfile localImsCallProfile = miSession.getCallProfile();
      return localImsCallProfile;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public ImsCallProfile getLocalCallProfile()
  {
    if (mClosed) {
      return null;
    }
    try
    {
      ImsCallProfile localImsCallProfile = miSession.getLocalCallProfile();
      return localImsCallProfile;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public String getProperty(String paramString)
  {
    if (mClosed) {
      return null;
    }
    try
    {
      paramString = miSession.getProperty(paramString);
      return paramString;
    }
    catch (RemoteException paramString) {}
    return null;
  }
  
  public ImsCallProfile getRemoteCallProfile()
  {
    if (mClosed) {
      return null;
    }
    try
    {
      ImsCallProfile localImsCallProfile = miSession.getRemoteCallProfile();
      return localImsCallProfile;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public IImsCallSession getSession()
  {
    return miSession;
  }
  
  public int getState()
  {
    if (mClosed) {
      return -1;
    }
    try
    {
      int i = miSession.getState();
      return i;
    }
    catch (RemoteException localRemoteException) {}
    return -1;
  }
  
  public IImsVideoCallProvider getVideoCallProvider()
  {
    if (mClosed) {
      return null;
    }
    try
    {
      IImsVideoCallProvider localIImsVideoCallProvider = miSession.getVideoCallProvider();
      return localIImsVideoCallProvider;
    }
    catch (RemoteException localRemoteException) {}
    return null;
  }
  
  public void hold(ImsStreamMediaProfile paramImsStreamMediaProfile)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.hold(paramImsStreamMediaProfile);
    }
    catch (RemoteException paramImsStreamMediaProfile) {}
  }
  
  public void inviteParticipants(String[] paramArrayOfString)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.inviteParticipants(paramArrayOfString);
    }
    catch (RemoteException paramArrayOfString) {}
  }
  
  public boolean isAlive()
  {
    if (mClosed) {
      return false;
    }
    switch (getState())
    {
    default: 
      return false;
    }
    return true;
  }
  
  public boolean isInCall()
  {
    if (mClosed) {
      return false;
    }
    try
    {
      boolean bool = miSession.isInCall();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isMultiparty()
  {
    if (mClosed) {
      return false;
    }
    try
    {
      boolean bool = miSession.isMultiparty();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public void merge()
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.merge();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void reject(int paramInt)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.reject(paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void removeParticipants(String[] paramArrayOfString)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.removeParticipants(paramArrayOfString);
    }
    catch (RemoteException paramArrayOfString) {}
  }
  
  public void resume(ImsStreamMediaProfile paramImsStreamMediaProfile)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.resume(paramImsStreamMediaProfile);
    }
    catch (RemoteException paramImsStreamMediaProfile) {}
  }
  
  public void sendDtmf(char paramChar, Message paramMessage)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.sendDtmf(paramChar, paramMessage);
    }
    catch (RemoteException paramMessage) {}
  }
  
  public void sendRttMessage(String paramString)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.sendRttMessage(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void sendRttModifyRequest(ImsCallProfile paramImsCallProfile)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.sendRttModifyRequest(paramImsCallProfile);
    }
    catch (RemoteException paramImsCallProfile) {}
  }
  
  public void sendRttModifyResponse(boolean paramBoolean)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.sendRttModifyResponse(paramBoolean);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void sendUssd(String paramString)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.sendUssd(paramString);
    }
    catch (RemoteException paramString) {}
  }
  
  public void setListener(Listener paramListener)
  {
    mListener = paramListener;
  }
  
  public void setMute(boolean paramBoolean)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.setMute(paramBoolean);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void start(String paramString, ImsCallProfile paramImsCallProfile)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.start(paramString, paramImsCallProfile);
    }
    catch (RemoteException paramString) {}
  }
  
  public void start(String[] paramArrayOfString, ImsCallProfile paramImsCallProfile)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.startConference(paramArrayOfString, paramImsCallProfile);
    }
    catch (RemoteException paramArrayOfString) {}
  }
  
  public void startDtmf(char paramChar)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.startDtmf(paramChar);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void stopDtmf()
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.stopDtmf();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void terminate(int paramInt)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.terminate(paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[ImsCallSession objId:");
    localStringBuilder.append(System.identityHashCode(this));
    localStringBuilder.append(" state:");
    localStringBuilder.append(State.toString(getState()));
    localStringBuilder.append(" callId:");
    localStringBuilder.append(getCallId());
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void update(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile)
  {
    if (mClosed) {
      return;
    }
    try
    {
      miSession.update(paramInt, paramImsStreamMediaProfile);
    }
    catch (RemoteException paramImsStreamMediaProfile) {}
  }
  
  private class IImsCallSessionListenerProxy
    extends IImsCallSessionListener.Stub
  {
    private IImsCallSessionListenerProxy() {}
    
    public void callSessionConferenceExtendFailed(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionConferenceExtendFailed(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionConferenceExtendReceived(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionConferenceExtendReceived(ImsCallSession.this, new ImsCallSession(paramIImsCallSession), paramImsCallProfile);
      }
    }
    
    public void callSessionConferenceExtended(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionConferenceExtended(ImsCallSession.this, new ImsCallSession(paramIImsCallSession), paramImsCallProfile);
      }
    }
    
    public void callSessionConferenceMaxnumUserCountUpdated(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("callSessionConferenceMaxnumUserCountUpdated: num: ");
      localStringBuilder.append(paramInt);
      Log.d("ImsCallSession", localStringBuilder.toString());
      if (mListener != null) {
        mListener.callSessionConferenceMaxnumUserCountUpdated(ImsCallSession.this, paramInt);
      }
    }
    
    public void callSessionConferenceStateUpdated(ImsConferenceState paramImsConferenceState)
    {
      if (mListener != null) {
        mListener.callSessionConferenceStateUpdated(ImsCallSession.this, paramImsConferenceState);
      }
    }
    
    public void callSessionHandover(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionHandover(ImsCallSession.this, paramInt1, paramInt2, paramImsReasonInfo);
      }
    }
    
    public void callSessionHandoverFailed(int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionHandoverFailed(ImsCallSession.this, paramInt1, paramInt2, paramImsReasonInfo);
      }
    }
    
    public void callSessionHeld(ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionHeld(ImsCallSession.this, paramImsCallProfile);
      }
    }
    
    public void callSessionHoldFailed(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionHoldFailed(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionHoldReceived(ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionHoldReceived(ImsCallSession.this, paramImsCallProfile);
      }
    }
    
    public void callSessionInitiated(ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionStarted(ImsCallSession.this, paramImsCallProfile);
      }
    }
    
    public void callSessionInitiatedFailed(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionStartFailed(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionInviteParticipantsRequestDelivered()
    {
      if (mListener != null) {
        mListener.callSessionInviteParticipantsRequestDelivered(ImsCallSession.this);
      }
    }
    
    public void callSessionInviteParticipantsRequestFailed(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionInviteParticipantsRequestFailed(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionMayHandover(int paramInt1, int paramInt2)
    {
      if (mListener != null) {
        mListener.callSessionMayHandover(ImsCallSession.this, paramInt1, paramInt2);
      }
    }
    
    public void callSessionMergeComplete(IImsCallSession paramIImsCallSession)
    {
      if (mListener != null) {
        if (paramIImsCallSession != null)
        {
          ImsCallSession localImsCallSession1 = ImsCallSession.this;
          ImsCallSession localImsCallSession2 = localImsCallSession1;
          try
          {
            if (!Objects.equals(miSession.getCallId(), paramIImsCallSession.getCallId()))
            {
              localImsCallSession2 = new android/telephony/ims/ImsCallSession;
              localImsCallSession2.<init>(paramIImsCallSession);
            }
          }
          catch (RemoteException paramIImsCallSession)
          {
            Log.e("ImsCallSession", "callSessionMergeComplete: exception for getCallId!");
            localImsCallSession2 = localImsCallSession1;
          }
          mListener.callSessionMergeComplete(localImsCallSession2);
        }
        else
        {
          mListener.callSessionMergeComplete(null);
        }
      }
    }
    
    public void callSessionMergeFailed(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionMergeFailed(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionMergeStarted(IImsCallSession paramIImsCallSession, ImsCallProfile paramImsCallProfile)
    {
      Log.d("ImsCallSession", "callSessionMergeStarted");
    }
    
    public void callSessionMultipartyStateChanged(boolean paramBoolean)
    {
      if (mListener != null) {
        mListener.callSessionMultipartyStateChanged(ImsCallSession.this, paramBoolean);
      }
    }
    
    public void callSessionProgressing(ImsStreamMediaProfile paramImsStreamMediaProfile)
    {
      if (mListener != null) {
        mListener.callSessionProgressing(ImsCallSession.this, paramImsStreamMediaProfile);
      }
    }
    
    public void callSessionPropertyChanged(int paramInt)
    {
      if (mListener != null) {
        mListener.callSessionPropertyChanged(paramInt);
      }
    }
    
    public void callSessionRemoveParticipantsRequestDelivered()
    {
      if (mListener != null) {
        mListener.callSessionRemoveParticipantsRequestDelivered(ImsCallSession.this);
      }
    }
    
    public void callSessionRemoveParticipantsRequestFailed(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionRemoveParticipantsRequestFailed(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionResumeFailed(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionResumeFailed(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionResumeReceived(ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionResumeReceived(ImsCallSession.this, paramImsCallProfile);
      }
    }
    
    public void callSessionResumed(ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionResumed(ImsCallSession.this, paramImsCallProfile);
      }
    }
    
    public void callSessionRttMessageReceived(String paramString)
    {
      if (mListener != null) {
        mListener.callSessionRttMessageReceived(paramString);
      }
    }
    
    public void callSessionRttModifyRequestReceived(ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionRttModifyRequestReceived(ImsCallSession.this, paramImsCallProfile);
      }
    }
    
    public void callSessionRttModifyResponseReceived(int paramInt)
    {
      if (mListener != null) {
        mListener.callSessionRttModifyResponseReceived(paramInt);
      }
    }
    
    public void callSessionSuppServiceReceived(ImsSuppServiceNotification paramImsSuppServiceNotification)
    {
      if (mListener != null) {
        mListener.callSessionSuppServiceReceived(ImsCallSession.this, paramImsSuppServiceNotification);
      }
    }
    
    public void callSessionTerminated(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionTerminated(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionTtyModeReceived(int paramInt)
    {
      if (mListener != null) {
        mListener.callSessionTtyModeReceived(ImsCallSession.this, paramInt);
      }
    }
    
    public void callSessionUpdateFailed(ImsReasonInfo paramImsReasonInfo)
    {
      if (mListener != null) {
        mListener.callSessionUpdateFailed(ImsCallSession.this, paramImsReasonInfo);
      }
    }
    
    public void callSessionUpdateReceived(ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionUpdateReceived(ImsCallSession.this, paramImsCallProfile);
      }
    }
    
    public void callSessionUpdated(ImsCallProfile paramImsCallProfile)
    {
      if (mListener != null) {
        mListener.callSessionUpdated(ImsCallSession.this, paramImsCallProfile);
      }
    }
    
    public void callSessionUssdMessageReceived(int paramInt, String paramString)
    {
      if (mListener != null) {
        mListener.callSessionUssdMessageReceived(ImsCallSession.this, paramInt, paramString);
      }
    }
  }
  
  public static class Listener
  {
    public Listener() {}
    
    public void callSessionConferenceExtendFailed(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionConferenceExtendReceived(ImsCallSession paramImsCallSession1, ImsCallSession paramImsCallSession2, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionConferenceExtended(ImsCallSession paramImsCallSession1, ImsCallSession paramImsCallSession2, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionConferenceMaxnumUserCountUpdated(ImsCallSession paramImsCallSession, int paramInt) {}
    
    public void callSessionConferenceStateUpdated(ImsCallSession paramImsCallSession, ImsConferenceState paramImsConferenceState) {}
    
    public void callSessionHandover(ImsCallSession paramImsCallSession, int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionHandoverFailed(ImsCallSession paramImsCallSession, int paramInt1, int paramInt2, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionHeld(ImsCallSession paramImsCallSession, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionHoldFailed(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionHoldReceived(ImsCallSession paramImsCallSession, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionInviteParticipantsRequestDelivered(ImsCallSession paramImsCallSession) {}
    
    public void callSessionInviteParticipantsRequestFailed(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionMayHandover(ImsCallSession paramImsCallSession, int paramInt1, int paramInt2) {}
    
    public void callSessionMergeComplete(ImsCallSession paramImsCallSession) {}
    
    public void callSessionMergeFailed(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionMergeStarted(ImsCallSession paramImsCallSession1, ImsCallSession paramImsCallSession2, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionMultipartyStateChanged(ImsCallSession paramImsCallSession, boolean paramBoolean) {}
    
    public void callSessionProgressing(ImsCallSession paramImsCallSession, ImsStreamMediaProfile paramImsStreamMediaProfile) {}
    
    public void callSessionPropertyChanged(int paramInt) {}
    
    public void callSessionRemoveParticipantsRequestDelivered(ImsCallSession paramImsCallSession) {}
    
    public void callSessionRemoveParticipantsRequestFailed(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionResumeFailed(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionResumeReceived(ImsCallSession paramImsCallSession, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionResumed(ImsCallSession paramImsCallSession, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionRttMessageReceived(String paramString) {}
    
    public void callSessionRttModifyRequestReceived(ImsCallSession paramImsCallSession, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionRttModifyResponseReceived(int paramInt) {}
    
    public void callSessionStartFailed(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionStarted(ImsCallSession paramImsCallSession, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionSuppServiceReceived(ImsCallSession paramImsCallSession, ImsSuppServiceNotification paramImsSuppServiceNotification) {}
    
    public void callSessionTerminated(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionTtyModeReceived(ImsCallSession paramImsCallSession, int paramInt) {}
    
    public void callSessionUpdateFailed(ImsCallSession paramImsCallSession, ImsReasonInfo paramImsReasonInfo) {}
    
    public void callSessionUpdateReceived(ImsCallSession paramImsCallSession, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionUpdated(ImsCallSession paramImsCallSession, ImsCallProfile paramImsCallProfile) {}
    
    public void callSessionUssdMessageReceived(ImsCallSession paramImsCallSession, int paramInt, String paramString) {}
  }
  
  public static class State
  {
    public static final int ESTABLISHED = 4;
    public static final int ESTABLISHING = 3;
    public static final int IDLE = 0;
    public static final int INITIATED = 1;
    public static final int INVALID = -1;
    public static final int NEGOTIATING = 2;
    public static final int REESTABLISHING = 6;
    public static final int RENEGOTIATING = 5;
    public static final int TERMINATED = 8;
    public static final int TERMINATING = 7;
    
    private State() {}
    
    public static String toString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return "UNKNOWN";
      case 8: 
        return "TERMINATED";
      case 7: 
        return "TERMINATING";
      case 6: 
        return "REESTABLISHING";
      case 5: 
        return "RENEGOTIATING";
      case 4: 
        return "ESTABLISHED";
      case 3: 
        return "ESTABLISHING";
      case 2: 
        return "NEGOTIATING";
      case 1: 
        return "INITIATED";
      }
      return "IDLE";
    }
  }
}
