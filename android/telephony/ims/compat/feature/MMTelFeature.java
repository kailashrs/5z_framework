package android.telephony.ims.compat.feature;

import android.app.PendingIntent;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.stub.ImsEcbmImplBase;
import android.telephony.ims.stub.ImsMultiEndpointImplBase;
import android.telephony.ims.stub.ImsUtImplBase;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsCallSessionListener;
import com.android.ims.internal.IImsConfig;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMMTelFeature;
import com.android.ims.internal.IImsMMTelFeature.Stub;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsRegistrationListener;
import com.android.ims.internal.IImsUt;

public class MMTelFeature
  extends ImsFeature
{
  private final IImsMMTelFeature mImsMMTelBinder = new IImsMMTelFeature.Stub()
  {
    public void addRegistrationListener(IImsRegistrationListener paramAnonymousIImsRegistrationListener)
      throws RemoteException
    {
      synchronized (mLock)
      {
        MMTelFeature.this.addRegistrationListener(paramAnonymousIImsRegistrationListener);
        return;
      }
    }
    
    public ImsCallProfile createCallProfile(int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      throws RemoteException
    {
      synchronized (mLock)
      {
        ImsCallProfile localImsCallProfile = MMTelFeature.this.createCallProfile(paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
        return localImsCallProfile;
      }
    }
    
    public IImsCallSession createCallSession(int paramAnonymousInt, ImsCallProfile paramAnonymousImsCallProfile)
      throws RemoteException
    {
      synchronized (mLock)
      {
        paramAnonymousImsCallProfile = createCallSession(paramAnonymousInt, paramAnonymousImsCallProfile, null);
        return paramAnonymousImsCallProfile;
      }
    }
    
    public void endSession(int paramAnonymousInt)
      throws RemoteException
    {
      synchronized (mLock)
      {
        MMTelFeature.this.endSession(paramAnonymousInt);
        return;
      }
    }
    
    public IImsConfig getConfigInterface()
      throws RemoteException
    {
      synchronized (mLock)
      {
        IImsConfig localIImsConfig = MMTelFeature.this.getConfigInterface();
        return localIImsConfig;
      }
    }
    
    public IImsEcbm getEcbmInterface()
      throws RemoteException
    {
      synchronized (mLock)
      {
        Object localObject2 = getEcbmInterface();
        if (localObject2 != null) {
          localObject2 = ((ImsEcbmImplBase)localObject2).getImsEcbm();
        } else {
          localObject2 = null;
        }
        return localObject2;
      }
    }
    
    public int getFeatureStatus()
      throws RemoteException
    {
      synchronized (mLock)
      {
        int i = getFeatureState();
        return i;
      }
    }
    
    public IImsMultiEndpoint getMultiEndpointInterface()
      throws RemoteException
    {
      synchronized (mLock)
      {
        Object localObject2 = getMultiEndpointInterface();
        if (localObject2 != null) {
          localObject2 = ((ImsMultiEndpointImplBase)localObject2).getIImsMultiEndpoint();
        } else {
          localObject2 = null;
        }
        return localObject2;
      }
    }
    
    public IImsCallSession getPendingCallSession(int paramAnonymousInt, String paramAnonymousString)
      throws RemoteException
    {
      synchronized (mLock)
      {
        paramAnonymousString = MMTelFeature.this.getPendingCallSession(paramAnonymousInt, paramAnonymousString);
        return paramAnonymousString;
      }
    }
    
    public IImsUt getUtInterface()
      throws RemoteException
    {
      synchronized (mLock)
      {
        Object localObject2 = getUtInterface();
        if (localObject2 != null) {
          localObject2 = ((ImsUtImplBase)localObject2).getInterface();
        } else {
          localObject2 = null;
        }
        return localObject2;
      }
    }
    
    public boolean isConnected(int paramAnonymousInt1, int paramAnonymousInt2)
      throws RemoteException
    {
      synchronized (mLock)
      {
        boolean bool = MMTelFeature.this.isConnected(paramAnonymousInt1, paramAnonymousInt2);
        return bool;
      }
    }
    
    public boolean isOpened()
      throws RemoteException
    {
      synchronized (mLock)
      {
        boolean bool = MMTelFeature.this.isOpened();
        return bool;
      }
    }
    
    public void removeRegistrationListener(IImsRegistrationListener paramAnonymousIImsRegistrationListener)
      throws RemoteException
    {
      synchronized (mLock)
      {
        MMTelFeature.this.removeRegistrationListener(paramAnonymousIImsRegistrationListener);
        return;
      }
    }
    
    public void setUiTTYMode(int paramAnonymousInt, Message paramAnonymousMessage)
      throws RemoteException
    {
      synchronized (mLock)
      {
        MMTelFeature.this.setUiTTYMode(paramAnonymousInt, paramAnonymousMessage);
        return;
      }
    }
    
    public int startSession(PendingIntent paramAnonymousPendingIntent, IImsRegistrationListener paramAnonymousIImsRegistrationListener)
      throws RemoteException
    {
      synchronized (mLock)
      {
        int i = MMTelFeature.this.startSession(paramAnonymousPendingIntent, paramAnonymousIImsRegistrationListener);
        return i;
      }
    }
    
    public void turnOffIms()
      throws RemoteException
    {
      synchronized (mLock)
      {
        MMTelFeature.this.turnOffIms();
        return;
      }
    }
    
    public void turnOnIms()
      throws RemoteException
    {
      synchronized (mLock)
      {
        MMTelFeature.this.turnOnIms();
        return;
      }
    }
  };
  private final Object mLock = new Object();
  
  public MMTelFeature() {}
  
  public void addRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener) {}
  
  public ImsCallProfile createCallProfile(int paramInt1, int paramInt2, int paramInt3)
  {
    return null;
  }
  
  public IImsCallSession createCallSession(int paramInt, ImsCallProfile paramImsCallProfile, IImsCallSessionListener paramIImsCallSessionListener)
  {
    return null;
  }
  
  public void endSession(int paramInt) {}
  
  public final IImsMMTelFeature getBinder()
  {
    return mImsMMTelBinder;
  }
  
  public IImsConfig getConfigInterface()
  {
    return null;
  }
  
  public ImsEcbmImplBase getEcbmInterface()
  {
    return null;
  }
  
  public ImsMultiEndpointImplBase getMultiEndpointInterface()
  {
    return null;
  }
  
  public IImsCallSession getPendingCallSession(int paramInt, String paramString)
  {
    return null;
  }
  
  public ImsUtImplBase getUtInterface()
  {
    return null;
  }
  
  public boolean isConnected(int paramInt1, int paramInt2)
  {
    return false;
  }
  
  public boolean isOpened()
  {
    return false;
  }
  
  public void onFeatureReady() {}
  
  public void onFeatureRemoved() {}
  
  public void removeRegistrationListener(IImsRegistrationListener paramIImsRegistrationListener) {}
  
  public void setUiTTYMode(int paramInt, Message paramMessage) {}
  
  public int startSession(PendingIntent paramPendingIntent, IImsRegistrationListener paramIImsRegistrationListener)
  {
    return 0;
  }
  
  public void turnOffIms() {}
  
  public void turnOnIms() {}
}
