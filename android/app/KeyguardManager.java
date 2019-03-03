package android.app;

import android.annotation.SystemApi;
import android.app.trust.ITrustManager;
import android.app.trust.ITrustManager.Stub;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.provider.Settings.Global;
import android.service.persistentdata.IPersistentDataBlockService;
import android.service.persistentdata.IPersistentDataBlockService.Stub;
import android.util.Log;
import android.view.IOnKeyguardExitResult.Stub;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import com.android.internal.policy.IKeyguardDismissCallback.Stub;
import com.android.internal.widget.LockPatternUtils;
import java.util.List;
import java.util.Objects;

public class KeyguardManager
{
  public static final String ACTION_CONFIRM_DEVICE_CREDENTIAL = "android.app.action.CONFIRM_DEVICE_CREDENTIAL";
  public static final String ACTION_CONFIRM_DEVICE_CREDENTIAL_WITH_USER = "android.app.action.CONFIRM_DEVICE_CREDENTIAL_WITH_USER";
  public static final String ACTION_CONFIRM_FRP_CREDENTIAL = "android.app.action.CONFIRM_FRP_CREDENTIAL";
  public static final String EXTRA_ALTERNATE_BUTTON_LABEL = "android.app.extra.ALTERNATE_BUTTON_LABEL";
  public static final String EXTRA_DESCRIPTION = "android.app.extra.DESCRIPTION";
  public static final String EXTRA_TITLE = "android.app.extra.TITLE";
  public static final int RESULT_ALTERNATE = 1;
  private static final String TAG = "KeyguardManager";
  private final IActivityManager mAm;
  private final Context mContext;
  private final ITrustManager mTrustManager;
  private final IWindowManager mWM;
  
  KeyguardManager(Context paramContext)
    throws ServiceManager.ServiceNotFoundException
  {
    mContext = paramContext;
    mWM = WindowManagerGlobal.getWindowManagerService();
    mAm = ActivityManager.getService();
    mTrustManager = ITrustManager.Stub.asInterface(ServiceManager.getServiceOrThrow("trust"));
  }
  
  private String getSettingsPackageForIntent(Intent paramIntent)
  {
    paramIntent = mContext.getPackageManager().queryIntentActivities(paramIntent, 1048576);
    if (paramIntent.size() < 0) {
      return get0activityInfo.packageName;
    }
    return "com.android.settings";
  }
  
  public Intent createConfirmDeviceCredentialIntent(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    if (!isDeviceSecure()) {
      return null;
    }
    Intent localIntent = new Intent("android.app.action.CONFIRM_DEVICE_CREDENTIAL");
    localIntent.putExtra("android.app.extra.TITLE", paramCharSequence1);
    localIntent.putExtra("android.app.extra.DESCRIPTION", paramCharSequence2);
    localIntent.setPackage(getSettingsPackageForIntent(localIntent));
    return localIntent;
  }
  
  public Intent createConfirmDeviceCredentialIntent(CharSequence paramCharSequence1, CharSequence paramCharSequence2, int paramInt)
  {
    if (!isDeviceSecure(paramInt)) {
      return null;
    }
    Intent localIntent = new Intent("android.app.action.CONFIRM_DEVICE_CREDENTIAL_WITH_USER");
    localIntent.putExtra("android.app.extra.TITLE", paramCharSequence1);
    localIntent.putExtra("android.app.extra.DESCRIPTION", paramCharSequence2);
    localIntent.putExtra("android.intent.extra.USER_ID", paramInt);
    localIntent.setPackage(getSettingsPackageForIntent(localIntent));
    return localIntent;
  }
  
  @SystemApi
  public Intent createConfirmFactoryResetCredentialIntent(CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3)
  {
    if (LockPatternUtils.frpCredentialEnabled(mContext))
    {
      if (Settings.Global.getInt(mContext.getContentResolver(), "device_provisioned", 0) == 0) {
        try
        {
          Object localObject = IPersistentDataBlockService.Stub.asInterface(ServiceManager.getService("persistent_data_block"));
          if (localObject != null)
          {
            if (!((IPersistentDataBlockService)localObject).hasFrpCredentialHandle())
            {
              Log.i("KeyguardManager", "The persistent data block does not have a factory reset credential.");
              return null;
            }
            localObject = new Intent("android.app.action.CONFIRM_FRP_CREDENTIAL");
            ((Intent)localObject).putExtra("android.app.extra.TITLE", paramCharSequence1);
            ((Intent)localObject).putExtra("android.app.extra.DESCRIPTION", paramCharSequence2);
            ((Intent)localObject).putExtra("android.app.extra.ALTERNATE_BUTTON_LABEL", paramCharSequence3);
            ((Intent)localObject).setPackage(getSettingsPackageForIntent((Intent)localObject));
            return localObject;
          }
          Log.e("KeyguardManager", "No persistent data block service");
          paramCharSequence1 = new java/lang/UnsupportedOperationException;
          paramCharSequence1.<init>("not supported on this device");
          throw paramCharSequence1;
        }
        catch (RemoteException paramCharSequence1)
        {
          throw paramCharSequence1.rethrowFromSystemServer();
        }
      }
      Log.e("KeyguardManager", "Factory reset credential cannot be verified after provisioning.");
      throw new IllegalStateException("must not be provisioned yet");
    }
    Log.w("KeyguardManager", "Factory reset credentials not supported.");
    throw new UnsupportedOperationException("not supported on this device");
  }
  
  @Deprecated
  public void dismissKeyguard(Activity paramActivity, KeyguardDismissCallback paramKeyguardDismissCallback, Handler paramHandler)
  {
    requestDismissKeyguard(paramActivity, paramKeyguardDismissCallback);
  }
  
  @Deprecated
  public void exitKeyguardSecurely(OnKeyguardExitResult paramOnKeyguardExitResult)
  {
    try
    {
      IWindowManager localIWindowManager = mWM;
      IOnKeyguardExitResult.Stub local2 = new android/app/KeyguardManager$2;
      local2.<init>(this, paramOnKeyguardExitResult);
      localIWindowManager.exitKeyguardSecurely(local2);
    }
    catch (RemoteException paramOnKeyguardExitResult) {}
  }
  
  public boolean inKeyguardRestrictedInputMode()
  {
    return isKeyguardLocked();
  }
  
  public boolean isDeviceLocked()
  {
    return isDeviceLocked(mContext.getUserId());
  }
  
  public boolean isDeviceLocked(int paramInt)
  {
    try
    {
      boolean bool = mTrustManager.isDeviceLocked(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isDeviceSecure()
  {
    return isDeviceSecure(mContext.getUserId());
  }
  
  public boolean isDeviceSecure(int paramInt)
  {
    try
    {
      boolean bool = mTrustManager.isDeviceSecure(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isKeyguardLocked()
  {
    try
    {
      boolean bool = mWM.isKeyguardLocked();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  public boolean isKeyguardSecure()
  {
    try
    {
      boolean bool = mWM.isKeyguardSecure();
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  @Deprecated
  public KeyguardLock newKeyguardLock(String paramString)
  {
    return new KeyguardLock(paramString);
  }
  
  public void requestDismissKeyguard(Activity paramActivity, KeyguardDismissCallback paramKeyguardDismissCallback)
  {
    requestDismissKeyguard(paramActivity, null, paramKeyguardDismissCallback);
  }
  
  @SystemApi
  public void requestDismissKeyguard(Activity paramActivity, CharSequence paramCharSequence, KeyguardDismissCallback paramKeyguardDismissCallback)
  {
    try
    {
      IActivityManager localIActivityManager = mAm;
      IBinder localIBinder = paramActivity.getActivityToken();
      IKeyguardDismissCallback.Stub local1 = new android/app/KeyguardManager$1;
      local1.<init>(this, paramKeyguardDismissCallback, paramActivity);
      localIActivityManager.dismissKeyguard(localIBinder, local1, paramCharSequence);
      return;
    }
    catch (RemoteException paramActivity)
    {
      throw paramActivity.rethrowFromSystemServer();
    }
  }
  
  public static abstract class KeyguardDismissCallback
  {
    public KeyguardDismissCallback() {}
    
    public void onDismissCancelled() {}
    
    public void onDismissError() {}
    
    public void onDismissSucceeded() {}
  }
  
  @Deprecated
  public class KeyguardLock
  {
    private final String mTag;
    private final IBinder mToken = new Binder();
    
    KeyguardLock(String paramString)
    {
      mTag = paramString;
    }
    
    public void disableKeyguard()
    {
      try
      {
        mWM.disableKeyguard(mToken, mTag);
      }
      catch (RemoteException localRemoteException) {}
    }
    
    public void reenableKeyguard()
    {
      try
      {
        mWM.reenableKeyguard(mToken);
      }
      catch (RemoteException localRemoteException) {}
    }
  }
  
  @Deprecated
  public static abstract interface OnKeyguardExitResult
  {
    public abstract void onKeyguardExitResult(boolean paramBoolean);
  }
}
