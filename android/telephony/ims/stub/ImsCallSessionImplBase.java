package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsCallSessionListener;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsVideoCallProvider;
import android.telephony.ims.aidl.IImsCallSessionListener;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSession.Stub;
import com.android.ims.internal.IImsVideoCallProvider;

@SystemApi
public class ImsCallSessionImplBase
  implements AutoCloseable
{
  public static final int USSD_MODE_NOTIFY = 0;
  public static final int USSD_MODE_REQUEST = 1;
  private IImsCallSession mServiceImpl = new IImsCallSession.Stub()
  {
    public void accept(int paramAnonymousInt, ImsStreamMediaProfile paramAnonymousImsStreamMediaProfile)
    {
      ImsCallSessionImplBase.this.accept(paramAnonymousInt, paramAnonymousImsStreamMediaProfile);
    }
    
    public void close()
    {
      ImsCallSessionImplBase.this.close();
    }
    
    public void deflect(String paramAnonymousString)
    {
      ImsCallSessionImplBase.this.deflect(paramAnonymousString);
    }
    
    public void extendToConference(String[] paramAnonymousArrayOfString)
    {
      ImsCallSessionImplBase.this.extendToConference(paramAnonymousArrayOfString);
    }
    
    public String getCallId()
    {
      return ImsCallSessionImplBase.this.getCallId();
    }
    
    public ImsCallProfile getCallProfile()
    {
      return ImsCallSessionImplBase.this.getCallProfile();
    }
    
    public ImsCallProfile getLocalCallProfile()
    {
      return ImsCallSessionImplBase.this.getLocalCallProfile();
    }
    
    public String getProperty(String paramAnonymousString)
    {
      return ImsCallSessionImplBase.this.getProperty(paramAnonymousString);
    }
    
    public ImsCallProfile getRemoteCallProfile()
    {
      return ImsCallSessionImplBase.this.getRemoteCallProfile();
    }
    
    public int getState()
    {
      return ImsCallSessionImplBase.this.getState();
    }
    
    public IImsVideoCallProvider getVideoCallProvider()
    {
      return ImsCallSessionImplBase.this.getVideoCallProvider();
    }
    
    public void hold(ImsStreamMediaProfile paramAnonymousImsStreamMediaProfile)
    {
      ImsCallSessionImplBase.this.hold(paramAnonymousImsStreamMediaProfile);
    }
    
    public void inviteParticipants(String[] paramAnonymousArrayOfString)
    {
      ImsCallSessionImplBase.this.inviteParticipants(paramAnonymousArrayOfString);
    }
    
    public boolean isInCall()
    {
      return ImsCallSessionImplBase.this.isInCall();
    }
    
    public boolean isMultiparty()
    {
      return ImsCallSessionImplBase.this.isMultiparty();
    }
    
    public void merge()
    {
      ImsCallSessionImplBase.this.merge();
    }
    
    public void reject(int paramAnonymousInt)
    {
      ImsCallSessionImplBase.this.reject(paramAnonymousInt);
    }
    
    public void removeParticipants(String[] paramAnonymousArrayOfString)
    {
      ImsCallSessionImplBase.this.removeParticipants(paramAnonymousArrayOfString);
    }
    
    public void resume(ImsStreamMediaProfile paramAnonymousImsStreamMediaProfile)
    {
      ImsCallSessionImplBase.this.resume(paramAnonymousImsStreamMediaProfile);
    }
    
    public void sendDtmf(char paramAnonymousChar, Message paramAnonymousMessage)
    {
      ImsCallSessionImplBase.this.sendDtmf(paramAnonymousChar, paramAnonymousMessage);
    }
    
    public void sendRttMessage(String paramAnonymousString)
    {
      ImsCallSessionImplBase.this.sendRttMessage(paramAnonymousString);
    }
    
    public void sendRttModifyRequest(ImsCallProfile paramAnonymousImsCallProfile)
    {
      ImsCallSessionImplBase.this.sendRttModifyRequest(paramAnonymousImsCallProfile);
    }
    
    public void sendRttModifyResponse(boolean paramAnonymousBoolean)
    {
      ImsCallSessionImplBase.this.sendRttModifyResponse(paramAnonymousBoolean);
    }
    
    public void sendUssd(String paramAnonymousString)
    {
      ImsCallSessionImplBase.this.sendUssd(paramAnonymousString);
    }
    
    public void setListener(IImsCallSessionListener paramAnonymousIImsCallSessionListener)
    {
      setListener(new ImsCallSessionListener(paramAnonymousIImsCallSessionListener));
    }
    
    public void setMute(boolean paramAnonymousBoolean)
    {
      ImsCallSessionImplBase.this.setMute(paramAnonymousBoolean);
    }
    
    public void start(String paramAnonymousString, ImsCallProfile paramAnonymousImsCallProfile)
    {
      ImsCallSessionImplBase.this.start(paramAnonymousString, paramAnonymousImsCallProfile);
    }
    
    public void startConference(String[] paramAnonymousArrayOfString, ImsCallProfile paramAnonymousImsCallProfile)
      throws RemoteException
    {
      ImsCallSessionImplBase.this.startConference(paramAnonymousArrayOfString, paramAnonymousImsCallProfile);
    }
    
    public void startDtmf(char paramAnonymousChar)
    {
      ImsCallSessionImplBase.this.startDtmf(paramAnonymousChar);
    }
    
    public void stopDtmf()
    {
      ImsCallSessionImplBase.this.stopDtmf();
    }
    
    public void terminate(int paramAnonymousInt)
    {
      ImsCallSessionImplBase.this.terminate(paramAnonymousInt);
    }
    
    public void update(int paramAnonymousInt, ImsStreamMediaProfile paramAnonymousImsStreamMediaProfile)
    {
      ImsCallSessionImplBase.this.update(paramAnonymousInt, paramAnonymousImsStreamMediaProfile);
    }
  };
  
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
  
  public ImsVideoCallProvider getImsVideoCallProvider()
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
  
  public IImsCallSession getServiceImpl()
  {
    return mServiceImpl;
  }
  
  public int getState()
  {
    return -1;
  }
  
  public IImsVideoCallProvider getVideoCallProvider()
  {
    Object localObject = getImsVideoCallProvider();
    if (localObject != null) {
      localObject = ((ImsVideoCallProvider)localObject).getInterface();
    } else {
      localObject = null;
    }
    return localObject;
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
  
  public void setListener(ImsCallSessionListener paramImsCallSessionListener) {}
  
  public final void setListener(IImsCallSessionListener paramIImsCallSessionListener)
    throws RemoteException
  {
    setListener(new ImsCallSessionListener(paramIImsCallSessionListener));
  }
  
  public void setMute(boolean paramBoolean) {}
  
  public void setServiceImpl(IImsCallSession paramIImsCallSession)
  {
    mServiceImpl = paramIImsCallSession;
  }
  
  public void start(String paramString, ImsCallProfile paramImsCallProfile) {}
  
  public void startConference(String[] paramArrayOfString, ImsCallProfile paramImsCallProfile) {}
  
  public void startDtmf(char paramChar) {}
  
  public void stopDtmf() {}
  
  public void terminate(int paramInt) {}
  
  public void update(int paramInt, ImsStreamMediaProfile paramImsStreamMediaProfile) {}
  
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
