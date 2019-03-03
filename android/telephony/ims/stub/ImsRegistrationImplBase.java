package android.telephony.ims.stub;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.aidl.IImsRegistration;
import android.telephony.ims.aidl.IImsRegistration.Stub;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public class ImsRegistrationImplBase
{
  private static final String LOG_TAG = "ImsRegistrationImplBase";
  private static final int REGISTRATION_STATE_NOT_REGISTERED = 0;
  private static final int REGISTRATION_STATE_REGISTERED = 2;
  private static final int REGISTRATION_STATE_REGISTERING = 1;
  private static final int REGISTRATION_STATE_UNKNOWN = -1;
  public static final int REGISTRATION_TECH_IWLAN = 1;
  public static final int REGISTRATION_TECH_LTE = 0;
  public static final int REGISTRATION_TECH_NONE = -1;
  private final IImsRegistration mBinder = new IImsRegistration.Stub()
  {
    public void addRegistrationCallback(IImsRegistrationCallback paramAnonymousIImsRegistrationCallback)
      throws RemoteException
    {
      ImsRegistrationImplBase.this.addRegistrationCallback(paramAnonymousIImsRegistrationCallback);
    }
    
    public int getRegistrationTechnology()
      throws RemoteException
    {
      return getConnectionType();
    }
    
    public void removeRegistrationCallback(IImsRegistrationCallback paramAnonymousIImsRegistrationCallback)
      throws RemoteException
    {
      ImsRegistrationImplBase.this.removeRegistrationCallback(paramAnonymousIImsRegistrationCallback);
    }
  };
  private final RemoteCallbackList<IImsRegistrationCallback> mCallbacks = new RemoteCallbackList();
  private int mConnectionType = -1;
  private ImsReasonInfo mLastDisconnectCause = new ImsReasonInfo();
  private final Object mLock = new Object();
  private int mRegistrationState = -1;
  
  public ImsRegistrationImplBase() {}
  
  private void addRegistrationCallback(IImsRegistrationCallback paramIImsRegistrationCallback)
    throws RemoteException
  {
    mCallbacks.register(paramIImsRegistrationCallback);
    updateNewCallbackWithState(paramIImsRegistrationCallback);
  }
  
  private void removeRegistrationCallback(IImsRegistrationCallback paramIImsRegistrationCallback)
  {
    mCallbacks.unregister(paramIImsRegistrationCallback);
  }
  
  private void updateNewCallbackWithState(IImsRegistrationCallback paramIImsRegistrationCallback)
    throws RemoteException
  {
    synchronized (mLock)
    {
      int i = mRegistrationState;
      ImsReasonInfo localImsReasonInfo = mLastDisconnectCause;
      switch (i)
      {
      default: 
        break;
      case 2: 
        paramIImsRegistrationCallback.onRegistered(getConnectionType());
        break;
      case 1: 
        paramIImsRegistrationCallback.onRegistering(getConnectionType());
        break;
      case 0: 
        paramIImsRegistrationCallback.onDeregistered(localImsReasonInfo);
      }
      return;
    }
  }
  
  private void updateToDisconnectedState(ImsReasonInfo paramImsReasonInfo)
  {
    synchronized (mLock)
    {
      updateToState(-1, 0);
      if (paramImsReasonInfo != null)
      {
        mLastDisconnectCause = paramImsReasonInfo;
      }
      else
      {
        Log.w("ImsRegistrationImplBase", "updateToDisconnectedState: no ImsReasonInfo provided.");
        paramImsReasonInfo = new android/telephony/ims/ImsReasonInfo;
        paramImsReasonInfo.<init>();
        mLastDisconnectCause = paramImsReasonInfo;
      }
      return;
    }
  }
  
  private void updateToState(int paramInt1, int paramInt2)
  {
    synchronized (mLock)
    {
      mConnectionType = paramInt1;
      mRegistrationState = paramInt2;
      mLastDisconnectCause = null;
      return;
    }
  }
  
  public final IImsRegistration getBinder()
  {
    return mBinder;
  }
  
  @VisibleForTesting
  public final int getConnectionType()
  {
    synchronized (mLock)
    {
      int i = mConnectionType;
      return i;
    }
  }
  
  public final void onDeregistered(ImsReasonInfo paramImsReasonInfo)
  {
    updateToDisconnectedState(paramImsReasonInfo);
    mCallbacks.broadcast(new _..Lambda.ImsRegistrationImplBase.s7PspXVbCf1Q_WSzodP2glP9TjI(paramImsReasonInfo));
  }
  
  public final void onRegistered(int paramInt)
  {
    updateToState(paramInt, 2);
    mCallbacks.broadcast(new _..Lambda.ImsRegistrationImplBase.cWwTXSDsk_bWPbsDJYI__DUBMnE(paramInt));
  }
  
  public final void onRegistering(int paramInt)
  {
    updateToState(paramInt, 1);
    mCallbacks.broadcast(new _..Lambda.ImsRegistrationImplBase.sbjuTvW_brOSWMR74UInSZEIQB0(paramInt));
  }
  
  public final void onSubscriberAssociatedUriChanged(Uri[] paramArrayOfUri)
  {
    mCallbacks.broadcast(new _..Lambda.ImsRegistrationImplBase.wwtkoeOtGwMjG5I0_ZTfjNpGU_s(paramArrayOfUri));
  }
  
  public final void onTechnologyChangeFailed(int paramInt, ImsReasonInfo paramImsReasonInfo)
  {
    mCallbacks.broadcast(new _..Lambda.ImsRegistrationImplBase.wDtW65cPmn_jF6dfimhBTfdg1kI(paramInt, paramImsReasonInfo));
  }
  
  public static class Callback
  {
    public Callback() {}
    
    public void onDeregistered(ImsReasonInfo paramImsReasonInfo) {}
    
    public void onRegistered(int paramInt) {}
    
    public void onRegistering(int paramInt) {}
    
    public void onSubscriberAssociatedUriChanged(Uri[] paramArrayOfUri) {}
    
    public void onTechnologyChangeFailed(int paramInt, ImsReasonInfo paramImsReasonInfo) {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ImsRegistrationTech {}
}
