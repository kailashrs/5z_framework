package android.app.admin;

import android.annotation.SystemApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.UserHandle;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DeviceAdminReceiver
  extends BroadcastReceiver
{
  public static final String ACTION_AFFILIATED_PROFILE_TRANSFER_OWNERSHIP_COMPLETE = "android.app.action.AFFILIATED_PROFILE_TRANSFER_OWNERSHIP_COMPLETE";
  public static final String ACTION_BUGREPORT_FAILED = "android.app.action.BUGREPORT_FAILED";
  public static final String ACTION_BUGREPORT_SHARE = "android.app.action.BUGREPORT_SHARE";
  public static final String ACTION_BUGREPORT_SHARING_DECLINED = "android.app.action.BUGREPORT_SHARING_DECLINED";
  public static final String ACTION_CHOOSE_PRIVATE_KEY_ALIAS = "android.app.action.CHOOSE_PRIVATE_KEY_ALIAS";
  public static final String ACTION_DEVICE_ADMIN_DISABLED = "android.app.action.DEVICE_ADMIN_DISABLED";
  public static final String ACTION_DEVICE_ADMIN_DISABLE_REQUESTED = "android.app.action.DEVICE_ADMIN_DISABLE_REQUESTED";
  public static final String ACTION_DEVICE_ADMIN_ENABLED = "android.app.action.DEVICE_ADMIN_ENABLED";
  public static final String ACTION_LOCK_TASK_ENTERING = "android.app.action.LOCK_TASK_ENTERING";
  public static final String ACTION_LOCK_TASK_EXITING = "android.app.action.LOCK_TASK_EXITING";
  public static final String ACTION_NETWORK_LOGS_AVAILABLE = "android.app.action.NETWORK_LOGS_AVAILABLE";
  public static final String ACTION_NOTIFY_PENDING_SYSTEM_UPDATE = "android.app.action.NOTIFY_PENDING_SYSTEM_UPDATE";
  public static final String ACTION_PASSWORD_CHANGED = "android.app.action.ACTION_PASSWORD_CHANGED";
  public static final String ACTION_PASSWORD_EXPIRING = "android.app.action.ACTION_PASSWORD_EXPIRING";
  public static final String ACTION_PASSWORD_FAILED = "android.app.action.ACTION_PASSWORD_FAILED";
  public static final String ACTION_PASSWORD_SUCCEEDED = "android.app.action.ACTION_PASSWORD_SUCCEEDED";
  public static final String ACTION_PROFILE_PROVISIONING_COMPLETE = "android.app.action.PROFILE_PROVISIONING_COMPLETE";
  public static final String ACTION_SECURITY_LOGS_AVAILABLE = "android.app.action.SECURITY_LOGS_AVAILABLE";
  public static final String ACTION_TRANSFER_OWNERSHIP_COMPLETE = "android.app.action.TRANSFER_OWNERSHIP_COMPLETE";
  public static final String ACTION_USER_ADDED = "android.app.action.USER_ADDED";
  public static final String ACTION_USER_REMOVED = "android.app.action.USER_REMOVED";
  public static final String ACTION_USER_STARTED = "android.app.action.USER_STARTED";
  public static final String ACTION_USER_STOPPED = "android.app.action.USER_STOPPED";
  public static final String ACTION_USER_SWITCHED = "android.app.action.USER_SWITCHED";
  public static final int BUGREPORT_FAILURE_FAILED_COMPLETING = 0;
  public static final int BUGREPORT_FAILURE_FILE_NO_LONGER_AVAILABLE = 1;
  public static final String DEVICE_ADMIN_META_DATA = "android.app.device_admin";
  public static final String EXTRA_BUGREPORT_FAILURE_REASON = "android.app.extra.BUGREPORT_FAILURE_REASON";
  public static final String EXTRA_BUGREPORT_HASH = "android.app.extra.BUGREPORT_HASH";
  public static final String EXTRA_CHOOSE_PRIVATE_KEY_ALIAS = "android.app.extra.CHOOSE_PRIVATE_KEY_ALIAS";
  public static final String EXTRA_CHOOSE_PRIVATE_KEY_RESPONSE = "android.app.extra.CHOOSE_PRIVATE_KEY_RESPONSE";
  public static final String EXTRA_CHOOSE_PRIVATE_KEY_SENDER_UID = "android.app.extra.CHOOSE_PRIVATE_KEY_SENDER_UID";
  public static final String EXTRA_CHOOSE_PRIVATE_KEY_URI = "android.app.extra.CHOOSE_PRIVATE_KEY_URI";
  public static final String EXTRA_DISABLE_WARNING = "android.app.extra.DISABLE_WARNING";
  public static final String EXTRA_LOCK_TASK_PACKAGE = "android.app.extra.LOCK_TASK_PACKAGE";
  public static final String EXTRA_NETWORK_LOGS_COUNT = "android.app.extra.EXTRA_NETWORK_LOGS_COUNT";
  public static final String EXTRA_NETWORK_LOGS_TOKEN = "android.app.extra.EXTRA_NETWORK_LOGS_TOKEN";
  public static final String EXTRA_SYSTEM_UPDATE_RECEIVED_TIME = "android.app.extra.SYSTEM_UPDATE_RECEIVED_TIME";
  public static final String EXTRA_TRANSFER_OWNERSHIP_ADMIN_EXTRAS_BUNDLE = "android.app.extra.TRANSFER_OWNERSHIP_ADMIN_EXTRAS_BUNDLE";
  private static String TAG = "DevicePolicy";
  private static boolean localLOGV = false;
  private DevicePolicyManager mManager;
  private ComponentName mWho;
  
  public DeviceAdminReceiver() {}
  
  public DevicePolicyManager getManager(Context paramContext)
  {
    if (mManager != null) {
      return mManager;
    }
    mManager = ((DevicePolicyManager)paramContext.getSystemService("device_policy"));
    return mManager;
  }
  
  public ComponentName getWho(Context paramContext)
  {
    if (mWho != null) {
      return mWho;
    }
    mWho = new ComponentName(paramContext, getClass());
    return mWho;
  }
  
  public void onBugreportFailed(Context paramContext, Intent paramIntent, int paramInt) {}
  
  public void onBugreportShared(Context paramContext, Intent paramIntent, String paramString) {}
  
  public void onBugreportSharingDeclined(Context paramContext, Intent paramIntent) {}
  
  public String onChoosePrivateKeyAlias(Context paramContext, Intent paramIntent, int paramInt, Uri paramUri, String paramString)
  {
    return null;
  }
  
  public CharSequence onDisableRequested(Context paramContext, Intent paramIntent)
  {
    return null;
  }
  
  public void onDisabled(Context paramContext, Intent paramIntent) {}
  
  public void onEnabled(Context paramContext, Intent paramIntent) {}
  
  public void onLockTaskModeEntering(Context paramContext, Intent paramIntent, String paramString) {}
  
  public void onLockTaskModeExiting(Context paramContext, Intent paramIntent) {}
  
  public void onNetworkLogsAvailable(Context paramContext, Intent paramIntent, long paramLong, int paramInt) {}
  
  @Deprecated
  public void onPasswordChanged(Context paramContext, Intent paramIntent) {}
  
  public void onPasswordChanged(Context paramContext, Intent paramIntent, UserHandle paramUserHandle)
  {
    onPasswordChanged(paramContext, paramIntent);
  }
  
  @Deprecated
  public void onPasswordExpiring(Context paramContext, Intent paramIntent) {}
  
  public void onPasswordExpiring(Context paramContext, Intent paramIntent, UserHandle paramUserHandle)
  {
    onPasswordExpiring(paramContext, paramIntent);
  }
  
  @Deprecated
  public void onPasswordFailed(Context paramContext, Intent paramIntent) {}
  
  public void onPasswordFailed(Context paramContext, Intent paramIntent, UserHandle paramUserHandle)
  {
    onPasswordFailed(paramContext, paramIntent);
  }
  
  @Deprecated
  public void onPasswordSucceeded(Context paramContext, Intent paramIntent) {}
  
  public void onPasswordSucceeded(Context paramContext, Intent paramIntent, UserHandle paramUserHandle)
  {
    onPasswordSucceeded(paramContext, paramIntent);
  }
  
  public void onProfileProvisioningComplete(Context paramContext, Intent paramIntent) {}
  
  @SystemApi
  @Deprecated
  public void onReadyForUserInitialization(Context paramContext, Intent paramIntent) {}
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramIntent.getAction();
    if ("android.app.action.ACTION_PASSWORD_CHANGED".equals(str))
    {
      onPasswordChanged(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.ACTION_PASSWORD_FAILED".equals(str))
    {
      onPasswordFailed(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.ACTION_PASSWORD_SUCCEEDED".equals(str))
    {
      onPasswordSucceeded(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.DEVICE_ADMIN_ENABLED".equals(str))
    {
      onEnabled(paramContext, paramIntent);
    }
    else if ("android.app.action.DEVICE_ADMIN_DISABLE_REQUESTED".equals(str))
    {
      paramContext = onDisableRequested(paramContext, paramIntent);
      if (paramContext != null) {
        getResultExtras(true).putCharSequence("android.app.extra.DISABLE_WARNING", paramContext);
      }
    }
    else if ("android.app.action.DEVICE_ADMIN_DISABLED".equals(str))
    {
      onDisabled(paramContext, paramIntent);
    }
    else if ("android.app.action.ACTION_PASSWORD_EXPIRING".equals(str))
    {
      onPasswordExpiring(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.PROFILE_PROVISIONING_COMPLETE".equals(str))
    {
      onProfileProvisioningComplete(paramContext, paramIntent);
    }
    else if ("android.app.action.CHOOSE_PRIVATE_KEY_ALIAS".equals(str))
    {
      setResultData(onChoosePrivateKeyAlias(paramContext, paramIntent, paramIntent.getIntExtra("android.app.extra.CHOOSE_PRIVATE_KEY_SENDER_UID", -1), (Uri)paramIntent.getParcelableExtra("android.app.extra.CHOOSE_PRIVATE_KEY_URI"), paramIntent.getStringExtra("android.app.extra.CHOOSE_PRIVATE_KEY_ALIAS")));
    }
    else if ("android.app.action.LOCK_TASK_ENTERING".equals(str))
    {
      onLockTaskModeEntering(paramContext, paramIntent, paramIntent.getStringExtra("android.app.extra.LOCK_TASK_PACKAGE"));
    }
    else if ("android.app.action.LOCK_TASK_EXITING".equals(str))
    {
      onLockTaskModeExiting(paramContext, paramIntent);
    }
    else if ("android.app.action.NOTIFY_PENDING_SYSTEM_UPDATE".equals(str))
    {
      onSystemUpdatePending(paramContext, paramIntent, paramIntent.getLongExtra("android.app.extra.SYSTEM_UPDATE_RECEIVED_TIME", -1L));
    }
    else if ("android.app.action.BUGREPORT_SHARING_DECLINED".equals(str))
    {
      onBugreportSharingDeclined(paramContext, paramIntent);
    }
    else if ("android.app.action.BUGREPORT_SHARE".equals(str))
    {
      onBugreportShared(paramContext, paramIntent, paramIntent.getStringExtra("android.app.extra.BUGREPORT_HASH"));
    }
    else if ("android.app.action.BUGREPORT_FAILED".equals(str))
    {
      onBugreportFailed(paramContext, paramIntent, paramIntent.getIntExtra("android.app.extra.BUGREPORT_FAILURE_REASON", 0));
    }
    else if ("android.app.action.SECURITY_LOGS_AVAILABLE".equals(str))
    {
      onSecurityLogsAvailable(paramContext, paramIntent);
    }
    else if ("android.app.action.NETWORK_LOGS_AVAILABLE".equals(str))
    {
      onNetworkLogsAvailable(paramContext, paramIntent, paramIntent.getLongExtra("android.app.extra.EXTRA_NETWORK_LOGS_TOKEN", -1L), paramIntent.getIntExtra("android.app.extra.EXTRA_NETWORK_LOGS_COUNT", 0));
    }
    else if ("android.app.action.USER_ADDED".equals(str))
    {
      onUserAdded(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.USER_REMOVED".equals(str))
    {
      onUserRemoved(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.USER_STARTED".equals(str))
    {
      onUserStarted(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.USER_STOPPED".equals(str))
    {
      onUserStopped(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.USER_SWITCHED".equals(str))
    {
      onUserSwitched(paramContext, paramIntent, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
    else if ("android.app.action.TRANSFER_OWNERSHIP_COMPLETE".equals(str))
    {
      onTransferOwnershipComplete(paramContext, (PersistableBundle)paramIntent.getParcelableExtra("android.app.extra.TRANSFER_OWNERSHIP_ADMIN_EXTRAS_BUNDLE"));
    }
    else if ("android.app.action.AFFILIATED_PROFILE_TRANSFER_OWNERSHIP_COMPLETE".equals(str))
    {
      onTransferAffiliatedProfileOwnershipComplete(paramContext, (UserHandle)paramIntent.getParcelableExtra("android.intent.extra.USER"));
    }
  }
  
  public void onSecurityLogsAvailable(Context paramContext, Intent paramIntent) {}
  
  public void onSystemUpdatePending(Context paramContext, Intent paramIntent, long paramLong) {}
  
  public void onTransferAffiliatedProfileOwnershipComplete(Context paramContext, UserHandle paramUserHandle) {}
  
  public void onTransferOwnershipComplete(Context paramContext, PersistableBundle paramPersistableBundle) {}
  
  public void onUserAdded(Context paramContext, Intent paramIntent, UserHandle paramUserHandle) {}
  
  public void onUserRemoved(Context paramContext, Intent paramIntent, UserHandle paramUserHandle) {}
  
  public void onUserStarted(Context paramContext, Intent paramIntent, UserHandle paramUserHandle) {}
  
  public void onUserStopped(Context paramContext, Intent paramIntent, UserHandle paramUserHandle) {}
  
  public void onUserSwitched(Context paramContext, Intent paramIntent, UserHandle paramUserHandle) {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BugreportFailureCode {}
}
