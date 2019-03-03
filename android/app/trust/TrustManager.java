package android.app.trust;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.ArrayMap;

public class TrustManager
{
  private static final String DATA_FLAGS = "initiatedByUser";
  private static final String DATA_MESSAGE = "message";
  private static final int MSG_TRUST_CHANGED = 1;
  private static final int MSG_TRUST_ERROR = 3;
  private static final int MSG_TRUST_MANAGED_CHANGED = 2;
  private static final String TAG = "TrustManager";
  private final Handler mHandler = new Handler(Looper.getMainLooper())
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      int i = what;
      boolean bool1 = true;
      boolean bool2 = true;
      Object localObject;
      switch (i)
      {
      default: 
        break;
      case 3: 
        localObject = paramAnonymousMessage.peekData().getCharSequence("message");
        ((TrustManager.TrustListener)obj).onTrustError((CharSequence)localObject);
        break;
      case 2: 
        localObject = (TrustManager.TrustListener)obj;
        if (arg1 == 0) {
          bool2 = false;
        }
        ((TrustManager.TrustListener)localObject).onTrustManagedChanged(bool2, arg2);
        break;
      case 1: 
        if (paramAnonymousMessage.peekData() != null) {
          i = paramAnonymousMessage.peekData().getInt("initiatedByUser");
        } else {
          i = 0;
        }
        localObject = (TrustManager.TrustListener)obj;
        if (arg1 != 0) {
          bool2 = bool1;
        } else {
          bool2 = false;
        }
        ((TrustManager.TrustListener)localObject).onTrustChanged(bool2, arg2, i);
      }
    }
  };
  private final ITrustManager mService;
  private final ArrayMap<TrustListener, ITrustListener> mTrustListeners;
  
  public TrustManager(IBinder paramIBinder)
  {
    mService = ITrustManager.Stub.asInterface(paramIBinder);
    mTrustListeners = new ArrayMap();
  }
  
  public void clearAllFingerprints()
  {
    try
    {
      mService.clearAllFingerprints();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isTrustUsuallyManaged(int paramInt)
  {
    try
    {
      boolean bool = mService.isTrustUsuallyManaged(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void registerTrustListener(TrustListener paramTrustListener)
  {
    try
    {
      ITrustListener.Stub local1 = new android/app/trust/TrustManager$1;
      local1.<init>(this, paramTrustListener);
      mService.registerTrustListener(local1);
      mTrustListeners.put(paramTrustListener, local1);
      return;
    }
    catch (RemoteException paramTrustListener)
    {
      throw paramTrustListener.rethrowFromSystemServer();
    }
  }
  
  public void reportEnabledTrustAgentsChanged(int paramInt)
  {
    try
    {
      mService.reportEnabledTrustAgentsChanged(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void reportKeyguardShowingChanged()
  {
    try
    {
      mService.reportKeyguardShowingChanged();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void reportUnlockAttempt(boolean paramBoolean, int paramInt)
  {
    try
    {
      mService.reportUnlockAttempt(paramBoolean, paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void reportUnlockLockout(int paramInt1, int paramInt2)
  {
    try
    {
      mService.reportUnlockLockout(paramInt1, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setDeviceLockedForUser(int paramInt, boolean paramBoolean)
  {
    try
    {
      mService.setDeviceLockedForUser(paramInt, paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void unlockedByFingerprintForUser(int paramInt)
  {
    try
    {
      mService.unlockedByFingerprintForUser(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void unregisterTrustListener(TrustListener paramTrustListener)
  {
    paramTrustListener = (ITrustListener)mTrustListeners.remove(paramTrustListener);
    if (paramTrustListener != null) {
      try
      {
        mService.unregisterTrustListener(paramTrustListener);
      }
      catch (RemoteException paramTrustListener)
      {
        throw paramTrustListener.rethrowFromSystemServer();
      }
    }
  }
  
  public static abstract interface TrustListener
  {
    public abstract void onTrustChanged(boolean paramBoolean, int paramInt1, int paramInt2);
    
    public abstract void onTrustError(CharSequence paramCharSequence);
    
    public abstract void onTrustManagedChanged(boolean paramBoolean, int paramInt);
  }
}
