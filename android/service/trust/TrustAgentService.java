package android.service.trust;

import android.annotation.SystemApi;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.util.Log;
import android.util.Slog;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@SystemApi
public class TrustAgentService
  extends Service
{
  private static final boolean DEBUG = false;
  private static final String EXTRA_TOKEN = "token";
  private static final String EXTRA_TOKEN_HANDLE = "token_handle";
  private static final String EXTRA_TOKEN_REMOVED_RESULT = "token_removed_result";
  private static final String EXTRA_TOKEN_STATE = "token_state";
  private static final String EXTRA_USER_HANDLE = "user_handle";
  public static final int FLAG_GRANT_TRUST_DISMISS_KEYGUARD = 2;
  public static final int FLAG_GRANT_TRUST_INITIATED_BY_USER = 1;
  private static final int MSG_CONFIGURE = 2;
  private static final int MSG_DEVICE_LOCKED = 4;
  private static final int MSG_DEVICE_UNLOCKED = 5;
  private static final int MSG_ESCROW_TOKEN_ADDED = 7;
  private static final int MSG_ESCROW_TOKEN_REMOVED = 9;
  private static final int MSG_ESCROW_TOKEN_STATE_RECEIVED = 8;
  private static final int MSG_TRUST_TIMEOUT = 3;
  private static final int MSG_UNLOCK_ATTEMPT = 1;
  private static final int MSG_UNLOCK_LOCKOUT = 6;
  public static final String SERVICE_INTERFACE = "android.service.trust.TrustAgentService";
  public static final int TOKEN_STATE_ACTIVE = 1;
  public static final int TOKEN_STATE_INACTIVE = 0;
  public static final String TRUST_AGENT_META_DATA = "android.service.trust.trustagent";
  private final String TAG;
  private ITrustAgentServiceCallback mCallback;
  private Handler mHandler;
  private final Object mLock;
  private boolean mManagingTrust;
  private Runnable mPendingGrantTrustTask;
  
  public TrustAgentService()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(TrustAgentService.class.getSimpleName());
    localStringBuilder.append("[");
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append("]");
    TAG = localStringBuilder.toString();
    mLock = new Object();
    mHandler = new Handler()
    {
      public void handleMessage(Message arg1)
      {
        int i = what;
        boolean bool = false;
        long l;
        Object localObject1;
        switch (i)
        {
        default: 
          break;
        case 9: 
          ??? = ???.getData();
          l = ???.getLong("token_handle");
          bool = ???.getBoolean("token_removed_result");
          onEscrowTokenRemoved(l, bool);
          break;
        case 8: 
          ??? = ???.getData();
          l = ???.getLong("token_handle");
          i = ???.getInt("token_state", 0);
          onEscrowTokenStateReceived(l, i);
          break;
        case 7: 
          localObject1 = ???.getData();
          ??? = ((Bundle)localObject1).getByteArray("token");
          l = ((Bundle)localObject1).getLong("token_handle");
          localObject1 = (UserHandle)((Bundle)localObject1).getParcelable("user_handle");
          onEscrowTokenAdded(???, l, (UserHandle)localObject1);
          break;
        case 6: 
          onDeviceUnlockLockout(arg1);
          break;
        case 5: 
          onDeviceUnlocked();
          break;
        case 4: 
          onDeviceLocked();
          break;
        case 3: 
          onTrustTimeout();
          break;
        case 2: 
          localObject1 = (TrustAgentService.ConfigurationData)obj;
          bool = onConfigure(options);
          if (token != null) {
            try
            {
              synchronized (mLock)
              {
                mCallback.onConfigureCompleted(bool, token);
              }
            }
            catch (RemoteException ???)
            {
              TrustAgentService.this.onError("calling onSetTrustAgentFeaturesEnabledCompleted()");
            }
          }
          break;
        case 1: 
          TrustAgentService localTrustAgentService = TrustAgentService.this;
          if (arg1 != 0) {
            bool = true;
          }
          localTrustAgentService.onUnlockAttempt(bool);
        }
      }
    };
  }
  
  private void onError(String paramString)
  {
    String str = TAG;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Remote exception while ");
    localStringBuilder.append(paramString);
    Slog.v(str, localStringBuilder.toString());
  }
  
  public final void addEscrowToken(byte[] paramArrayOfByte, UserHandle paramUserHandle)
  {
    synchronized (mLock)
    {
      ITrustAgentServiceCallback localITrustAgentServiceCallback = mCallback;
      if (localITrustAgentServiceCallback != null)
      {
        try
        {
          mCallback.addEscrowToken(paramArrayOfByte, paramUserHandle.getIdentifier());
        }
        catch (RemoteException paramArrayOfByte)
        {
          onError("calling addEscrowToken");
        }
        return;
      }
      Slog.w(TAG, "Cannot add escrow token if the agent is not connecting to framework");
      paramArrayOfByte = new java/lang/IllegalStateException;
      paramArrayOfByte.<init>("Trust agent is not connected");
      throw paramArrayOfByte;
    }
  }
  
  public final void grantTrust(CharSequence paramCharSequence, long paramLong, int paramInt)
  {
    synchronized (mLock)
    {
      if (mManagingTrust)
      {
        Object localObject2 = mCallback;
        if (localObject2 != null)
        {
          try
          {
            mCallback.grantTrust(paramCharSequence.toString(), paramLong, paramInt);
          }
          catch (RemoteException paramCharSequence)
          {
            for (;;)
            {
              onError("calling enableTrust()");
            }
          }
        }
        else
        {
          localObject2 = new android/service/trust/TrustAgentService$2;
          ((2)localObject2).<init>(this, paramCharSequence, paramLong, paramInt);
          mPendingGrantTrustTask = ((Runnable)localObject2);
        }
        return;
      }
      paramCharSequence = new java/lang/IllegalStateException;
      paramCharSequence.<init>("Cannot grant trust if agent is not managing trust. Call setManagingTrust(true) first.");
      throw paramCharSequence;
    }
  }
  
  @Deprecated
  public final void grantTrust(CharSequence paramCharSequence, long paramLong, boolean paramBoolean)
  {
    grantTrust(paramCharSequence, paramLong, paramBoolean);
  }
  
  public final void isEscrowTokenActive(long paramLong, UserHandle paramUserHandle)
  {
    synchronized (mLock)
    {
      ITrustAgentServiceCallback localITrustAgentServiceCallback = mCallback;
      if (localITrustAgentServiceCallback != null)
      {
        try
        {
          mCallback.isEscrowTokenActive(paramLong, paramUserHandle.getIdentifier());
        }
        catch (RemoteException paramUserHandle)
        {
          onError("calling isEscrowTokenActive");
        }
        return;
      }
      Slog.w(TAG, "Cannot add escrow token if the agent is not connecting to framework");
      paramUserHandle = new java/lang/IllegalStateException;
      paramUserHandle.<init>("Trust agent is not connected");
      throw paramUserHandle;
    }
  }
  
  public final IBinder onBind(Intent paramIntent)
  {
    return new TrustAgentServiceWrapper(null);
  }
  
  public boolean onConfigure(List<PersistableBundle> paramList)
  {
    return false;
  }
  
  public void onCreate()
  {
    super.onCreate();
    ComponentName localComponentName = new ComponentName(this, getClass());
    try
    {
      if (!"android.permission.BIND_TRUST_AGENT".equals(getPackageManagergetServiceInfo0permission))
      {
        IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append(localComponentName.flattenToShortString());
        localStringBuilder.append(" is not declared with the permission \"");
        localStringBuilder.append("android.permission.BIND_TRUST_AGENT");
        localStringBuilder.append("\"");
        localIllegalStateException.<init>(localStringBuilder.toString());
        throw localIllegalStateException;
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      String str = TAG;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Can't get ServiceInfo for ");
      localStringBuilder.append(localComponentName.toShortString());
      Log.e(str, localStringBuilder.toString());
    }
  }
  
  public void onDeviceLocked() {}
  
  public void onDeviceUnlockLockout(long paramLong) {}
  
  public void onDeviceUnlocked() {}
  
  public void onEscrowTokenAdded(byte[] paramArrayOfByte, long paramLong, UserHandle paramUserHandle) {}
  
  public void onEscrowTokenRemoved(long paramLong, boolean paramBoolean) {}
  
  public void onEscrowTokenStateReceived(long paramLong, int paramInt) {}
  
  public void onTrustTimeout() {}
  
  public void onUnlockAttempt(boolean paramBoolean) {}
  
  public final void removeEscrowToken(long paramLong, UserHandle paramUserHandle)
  {
    synchronized (mLock)
    {
      ITrustAgentServiceCallback localITrustAgentServiceCallback = mCallback;
      if (localITrustAgentServiceCallback != null)
      {
        try
        {
          mCallback.removeEscrowToken(paramLong, paramUserHandle.getIdentifier());
        }
        catch (RemoteException paramUserHandle)
        {
          onError("callling removeEscrowToken");
        }
        return;
      }
      Slog.w(TAG, "Cannot add escrow token if the agent is not connecting to framework");
      paramUserHandle = new java/lang/IllegalStateException;
      paramUserHandle.<init>("Trust agent is not connected");
      throw paramUserHandle;
    }
  }
  
  public final void revokeTrust()
  {
    synchronized (mLock)
    {
      if (mPendingGrantTrustTask != null) {
        mPendingGrantTrustTask = null;
      }
      ITrustAgentServiceCallback localITrustAgentServiceCallback = mCallback;
      if (localITrustAgentServiceCallback != null) {
        try
        {
          mCallback.revokeTrust();
        }
        catch (RemoteException localRemoteException)
        {
          onError("calling revokeTrust()");
        }
      }
      return;
    }
  }
  
  public final void setManagingTrust(boolean paramBoolean)
  {
    synchronized (mLock)
    {
      if (mManagingTrust != paramBoolean)
      {
        mManagingTrust = paramBoolean;
        ITrustAgentServiceCallback localITrustAgentServiceCallback = mCallback;
        if (localITrustAgentServiceCallback != null) {
          try
          {
            mCallback.setManagingTrust(paramBoolean);
          }
          catch (RemoteException localRemoteException)
          {
            onError("calling setManagingTrust()");
          }
        }
      }
      return;
    }
  }
  
  public final void showKeyguardErrorMessage(CharSequence paramCharSequence)
  {
    if (paramCharSequence != null) {
      synchronized (mLock)
      {
        ITrustAgentServiceCallback localITrustAgentServiceCallback = mCallback;
        if (localITrustAgentServiceCallback != null)
        {
          try
          {
            mCallback.showKeyguardErrorMessage(paramCharSequence);
          }
          catch (RemoteException paramCharSequence)
          {
            onError("calling showKeyguardErrorMessage");
          }
          return;
        }
        Slog.w(TAG, "Cannot show message because service is not connected to framework.");
        paramCharSequence = new java/lang/IllegalStateException;
        paramCharSequence.<init>("Trust agent is not connected");
        throw paramCharSequence;
      }
    }
    throw new IllegalArgumentException("message cannot be null");
  }
  
  public final void unlockUserWithToken(long paramLong, byte[] paramArrayOfByte, UserHandle paramUserHandle)
  {
    if (((UserManager)getSystemService("user")).isUserUnlocked(paramUserHandle))
    {
      Slog.i(TAG, "User already unlocked");
      return;
    }
    synchronized (mLock)
    {
      ITrustAgentServiceCallback localITrustAgentServiceCallback = mCallback;
      if (localITrustAgentServiceCallback != null)
      {
        try
        {
          mCallback.unlockUserWithToken(paramLong, paramArrayOfByte, paramUserHandle.getIdentifier());
        }
        catch (RemoteException paramArrayOfByte)
        {
          onError("calling unlockUserWithToken");
        }
        return;
      }
      Slog.w(TAG, "Cannot add escrow token if the agent is not connecting to framework");
      paramArrayOfByte = new java/lang/IllegalStateException;
      paramArrayOfByte.<init>("Trust agent is not connected");
      throw paramArrayOfByte;
    }
  }
  
  private static final class ConfigurationData
  {
    final List<PersistableBundle> options;
    final IBinder token;
    
    ConfigurationData(List<PersistableBundle> paramList, IBinder paramIBinder)
    {
      options = paramList;
      token = paramIBinder;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface GrantTrustFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TokenState {}
  
  private final class TrustAgentServiceWrapper
    extends ITrustAgentService.Stub
  {
    private TrustAgentServiceWrapper() {}
    
    public void onConfigure(List<PersistableBundle> paramList, IBinder paramIBinder)
    {
      mHandler.obtainMessage(2, new TrustAgentService.ConfigurationData(paramList, paramIBinder)).sendToTarget();
    }
    
    public void onDeviceLocked()
      throws RemoteException
    {
      mHandler.obtainMessage(4).sendToTarget();
    }
    
    public void onDeviceUnlocked()
      throws RemoteException
    {
      mHandler.obtainMessage(5).sendToTarget();
    }
    
    public void onEscrowTokenAdded(byte[] paramArrayOfByte, long paramLong, UserHandle paramUserHandle)
    {
      Message localMessage = mHandler.obtainMessage(7);
      localMessage.getData().putByteArray("token", paramArrayOfByte);
      localMessage.getData().putLong("token_handle", paramLong);
      localMessage.getData().putParcelable("user_handle", paramUserHandle);
      localMessage.sendToTarget();
    }
    
    public void onEscrowTokenRemoved(long paramLong, boolean paramBoolean)
    {
      Message localMessage = mHandler.obtainMessage(9);
      localMessage.getData().putLong("token_handle", paramLong);
      localMessage.getData().putBoolean("token_removed_result", paramBoolean);
      localMessage.sendToTarget();
    }
    
    public void onTokenStateReceived(long paramLong, int paramInt)
    {
      Message localMessage = mHandler.obtainMessage(8);
      localMessage.getData().putLong("token_handle", paramLong);
      localMessage.getData().putInt("token_state", paramInt);
      localMessage.sendToTarget();
    }
    
    public void onTrustTimeout()
    {
      mHandler.sendEmptyMessage(3);
    }
    
    public void onUnlockAttempt(boolean paramBoolean)
    {
      mHandler.obtainMessage(1, paramBoolean, 0).sendToTarget();
    }
    
    public void onUnlockLockout(int paramInt)
    {
      mHandler.obtainMessage(6, paramInt, 0).sendToTarget();
    }
    
    public void setCallback(ITrustAgentServiceCallback paramITrustAgentServiceCallback)
    {
      synchronized (mLock)
      {
        TrustAgentService.access$102(TrustAgentService.this, paramITrustAgentServiceCallback);
        boolean bool = mManagingTrust;
        if (bool) {
          try
          {
            mCallback.setManagingTrust(mManagingTrust);
          }
          catch (RemoteException paramITrustAgentServiceCallback)
          {
            TrustAgentService.this.onError("calling setManagingTrust()");
          }
        }
        if (mPendingGrantTrustTask != null)
        {
          mPendingGrantTrustTask.run();
          TrustAgentService.access$602(TrustAgentService.this, null);
        }
        return;
      }
    }
  }
}
