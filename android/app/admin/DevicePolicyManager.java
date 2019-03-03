package android.app.admin;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.IPackageDataObserver.Stub;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ParceledListSlice;
import android.content.pm.StringParceledListSlice;
import android.content.pm.UserInfo;
import android.graphics.Bitmap;
import android.net.ProxyInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.os.UserHandle;
import android.os.UserManager.UserOperationException;
import android.security.Credentials;
import android.telephony.data.ApnSetting;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import com.android.org.conscrypt.TrustedCertificateStore;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

public class DevicePolicyManager
{
  @SystemApi
  public static final String ACCOUNT_FEATURE_DEVICE_OR_PROFILE_OWNER_ALLOWED = "android.account.DEVICE_OR_PROFILE_OWNER_ALLOWED";
  @SystemApi
  public static final String ACCOUNT_FEATURE_DEVICE_OR_PROFILE_OWNER_DISALLOWED = "android.account.DEVICE_OR_PROFILE_OWNER_DISALLOWED";
  public static final String ACTION_ADD_DEVICE_ADMIN = "android.app.action.ADD_DEVICE_ADMIN";
  public static final String ACTION_APPLICATION_DELEGATION_SCOPES_CHANGED = "android.app.action.APPLICATION_DELEGATION_SCOPES_CHANGED";
  public static final String ACTION_BUGREPORT_SHARING_ACCEPTED = "com.android.server.action.REMOTE_BUGREPORT_SHARING_ACCEPTED";
  public static final String ACTION_BUGREPORT_SHARING_DECLINED = "com.android.server.action.REMOTE_BUGREPORT_SHARING_DECLINED";
  public static final String ACTION_DATA_SHARING_RESTRICTION_APPLIED = "android.app.action.DATA_SHARING_RESTRICTION_APPLIED";
  public static final String ACTION_DATA_SHARING_RESTRICTION_CHANGED = "android.app.action.DATA_SHARING_RESTRICTION_CHANGED";
  public static final String ACTION_DEVICE_ADMIN_SERVICE = "android.app.action.DEVICE_ADMIN_SERVICE";
  public static final String ACTION_DEVICE_OWNER_CHANGED = "android.app.action.DEVICE_OWNER_CHANGED";
  public static final String ACTION_DEVICE_POLICY_MANAGER_STATE_CHANGED = "android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED";
  public static final String ACTION_MANAGED_PROFILE_PROVISIONED = "android.app.action.MANAGED_PROFILE_PROVISIONED";
  public static final String ACTION_MANAGED_USER_CREATED = "android.app.action.MANAGED_USER_CREATED";
  public static final String ACTION_PROFILE_OWNER_CHANGED = "android.app.action.PROFILE_OWNER_CHANGED";
  public static final String ACTION_PROVISIONING_SUCCESSFUL = "android.app.action.PROVISIONING_SUCCESSFUL";
  @SystemApi
  public static final String ACTION_PROVISION_FINALIZATION = "android.app.action.PROVISION_FINALIZATION";
  public static final String ACTION_PROVISION_MANAGED_DEVICE = "android.app.action.PROVISION_MANAGED_DEVICE";
  @SystemApi
  public static final String ACTION_PROVISION_MANAGED_DEVICE_FROM_TRUSTED_SOURCE = "android.app.action.PROVISION_MANAGED_DEVICE_FROM_TRUSTED_SOURCE";
  public static final String ACTION_PROVISION_MANAGED_PROFILE = "android.app.action.PROVISION_MANAGED_PROFILE";
  public static final String ACTION_PROVISION_MANAGED_SHAREABLE_DEVICE = "android.app.action.PROVISION_MANAGED_SHAREABLE_DEVICE";
  public static final String ACTION_PROVISION_MANAGED_USER = "android.app.action.PROVISION_MANAGED_USER";
  public static final String ACTION_REMOTE_BUGREPORT_DISPATCH = "android.intent.action.REMOTE_BUGREPORT_DISPATCH";
  public static final String ACTION_SET_NEW_PARENT_PROFILE_PASSWORD = "android.app.action.SET_NEW_PARENT_PROFILE_PASSWORD";
  public static final String ACTION_SET_NEW_PASSWORD = "android.app.action.SET_NEW_PASSWORD";
  @SystemApi
  public static final String ACTION_SET_PROFILE_OWNER = "android.app.action.SET_PROFILE_OWNER";
  public static final String ACTION_SHOW_DEVICE_MONITORING_DIALOG = "android.app.action.SHOW_DEVICE_MONITORING_DIALOG";
  public static final String ACTION_START_ENCRYPTION = "android.app.action.START_ENCRYPTION";
  @SystemApi
  public static final String ACTION_STATE_USER_SETUP_COMPLETE = "android.app.action.STATE_USER_SETUP_COMPLETE";
  public static final String ACTION_SYSTEM_UPDATE_POLICY_CHANGED = "android.app.action.SYSTEM_UPDATE_POLICY_CHANGED";
  public static final int CODE_ACCOUNTS_NOT_EMPTY = 6;
  public static final int CODE_ADD_MANAGED_PROFILE_DISALLOWED = 15;
  public static final int CODE_CANNOT_ADD_MANAGED_PROFILE = 11;
  public static final int CODE_DEVICE_ADMIN_NOT_SUPPORTED = 13;
  public static final int CODE_HAS_DEVICE_OWNER = 1;
  public static final int CODE_HAS_PAIRED = 8;
  public static final int CODE_MANAGED_USERS_NOT_SUPPORTED = 9;
  public static final int CODE_NONSYSTEM_USER_EXISTS = 5;
  public static final int CODE_NOT_SYSTEM_USER = 7;
  public static final int CODE_NOT_SYSTEM_USER_SPLIT = 12;
  public static final int CODE_OK = 0;
  public static final int CODE_SPLIT_SYSTEM_USER_DEVICE_SYSTEM_USER = 14;
  public static final int CODE_SYSTEM_USER = 10;
  public static final int CODE_USER_HAS_PROFILE_OWNER = 2;
  public static final int CODE_USER_NOT_RUNNING = 3;
  public static final int CODE_USER_SETUP_COMPLETED = 4;
  public static final long DEFAULT_STRONG_AUTH_TIMEOUT_MS = 259200000L;
  public static final String DELEGATION_APP_RESTRICTIONS = "delegation-app-restrictions";
  public static final String DELEGATION_BLOCK_UNINSTALL = "delegation-block-uninstall";
  public static final String DELEGATION_CERT_INSTALL = "delegation-cert-install";
  public static final String DELEGATION_ENABLE_SYSTEM_APP = "delegation-enable-system-app";
  public static final String DELEGATION_INSTALL_EXISTING_PACKAGE = "delegation-install-existing-package";
  public static final String DELEGATION_KEEP_UNINSTALLED_PACKAGES = "delegation-keep-uninstalled-packages";
  public static final String DELEGATION_PACKAGE_ACCESS = "delegation-package-access";
  public static final String DELEGATION_PERMISSION_GRANT = "delegation-permission-grant";
  public static final int ENCRYPTION_STATUS_ACTIVATING = 2;
  public static final int ENCRYPTION_STATUS_ACTIVE = 3;
  public static final int ENCRYPTION_STATUS_ACTIVE_DEFAULT_KEY = 4;
  public static final int ENCRYPTION_STATUS_ACTIVE_PER_USER = 5;
  public static final int ENCRYPTION_STATUS_INACTIVE = 1;
  public static final int ENCRYPTION_STATUS_UNSUPPORTED = 0;
  public static final String EXTRA_ADD_EXPLANATION = "android.app.extra.ADD_EXPLANATION";
  public static final String EXTRA_BUGREPORT_NOTIFICATION_TYPE = "android.app.extra.bugreport_notification_type";
  public static final String EXTRA_DELEGATION_SCOPES = "android.app.extra.DELEGATION_SCOPES";
  public static final String EXTRA_DEVICE_ADMIN = "android.app.extra.DEVICE_ADMIN";
  @SystemApi
  public static final String EXTRA_PROFILE_OWNER_NAME = "android.app.extra.PROFILE_OWNER_NAME";
  public static final String EXTRA_PROVISIONING_ACCOUNT_TO_MIGRATE = "android.app.extra.PROVISIONING_ACCOUNT_TO_MIGRATE";
  public static final String EXTRA_PROVISIONING_ADMIN_EXTRAS_BUNDLE = "android.app.extra.PROVISIONING_ADMIN_EXTRAS_BUNDLE";
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME = "android.app.extra.PROVISIONING_DEVICE_ADMIN_COMPONENT_NAME";
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_MINIMUM_VERSION_CODE = "android.app.extra.PROVISIONING_DEVICE_ADMIN_MINIMUM_VERSION_CODE";
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_CHECKSUM = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_CHECKSUM";
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_COOKIE_HEADER = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_COOKIE_HEADER";
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_DOWNLOAD_LOCATION";
  @SystemApi
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_ICON_URI = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_ICON_URI";
  @SystemApi
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_LABEL = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_LABEL";
  @Deprecated
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME = "android.app.extra.PROVISIONING_DEVICE_ADMIN_PACKAGE_NAME";
  public static final String EXTRA_PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM = "android.app.extra.PROVISIONING_DEVICE_ADMIN_SIGNATURE_CHECKSUM";
  public static final String EXTRA_PROVISIONING_DISCLAIMERS = "android.app.extra.PROVISIONING_DISCLAIMERS";
  public static final String EXTRA_PROVISIONING_DISCLAIMER_CONTENT = "android.app.extra.PROVISIONING_DISCLAIMER_CONTENT";
  public static final String EXTRA_PROVISIONING_DISCLAIMER_HEADER = "android.app.extra.PROVISIONING_DISCLAIMER_HEADER";
  @Deprecated
  public static final String EXTRA_PROVISIONING_EMAIL_ADDRESS = "android.app.extra.PROVISIONING_EMAIL_ADDRESS";
  public static final String EXTRA_PROVISIONING_KEEP_ACCOUNT_ON_MIGRATION = "android.app.extra.PROVISIONING_KEEP_ACCOUNT_ON_MIGRATION";
  public static final String EXTRA_PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED = "android.app.extra.PROVISIONING_LEAVE_ALL_SYSTEM_APPS_ENABLED";
  public static final String EXTRA_PROVISIONING_LOCALE = "android.app.extra.PROVISIONING_LOCALE";
  public static final String EXTRA_PROVISIONING_LOCAL_TIME = "android.app.extra.PROVISIONING_LOCAL_TIME";
  public static final String EXTRA_PROVISIONING_LOGO_URI = "android.app.extra.PROVISIONING_LOGO_URI";
  public static final String EXTRA_PROVISIONING_MAIN_COLOR = "android.app.extra.PROVISIONING_MAIN_COLOR";
  @SystemApi
  public static final String EXTRA_PROVISIONING_ORGANIZATION_NAME = "android.app.extra.PROVISIONING_ORGANIZATION_NAME";
  public static final String EXTRA_PROVISIONING_SKIP_ENCRYPTION = "android.app.extra.PROVISIONING_SKIP_ENCRYPTION";
  public static final String EXTRA_PROVISIONING_SKIP_USER_CONSENT = "android.app.extra.PROVISIONING_SKIP_USER_CONSENT";
  public static final String EXTRA_PROVISIONING_SKIP_USER_SETUP = "android.app.extra.PROVISIONING_SKIP_USER_SETUP";
  @SystemApi
  public static final String EXTRA_PROVISIONING_SUPPORT_URL = "android.app.extra.PROVISIONING_SUPPORT_URL";
  public static final String EXTRA_PROVISIONING_TIME_ZONE = "android.app.extra.PROVISIONING_TIME_ZONE";
  public static final String EXTRA_PROVISIONING_USE_MOBILE_DATA = "android.app.extra.PROVISIONING_USE_MOBILE_DATA";
  public static final String EXTRA_PROVISIONING_WIFI_HIDDEN = "android.app.extra.PROVISIONING_WIFI_HIDDEN";
  public static final String EXTRA_PROVISIONING_WIFI_PAC_URL = "android.app.extra.PROVISIONING_WIFI_PAC_URL";
  public static final String EXTRA_PROVISIONING_WIFI_PASSWORD = "android.app.extra.PROVISIONING_WIFI_PASSWORD";
  public static final String EXTRA_PROVISIONING_WIFI_PROXY_BYPASS = "android.app.extra.PROVISIONING_WIFI_PROXY_BYPASS";
  public static final String EXTRA_PROVISIONING_WIFI_PROXY_HOST = "android.app.extra.PROVISIONING_WIFI_PROXY_HOST";
  public static final String EXTRA_PROVISIONING_WIFI_PROXY_PORT = "android.app.extra.PROVISIONING_WIFI_PROXY_PORT";
  public static final String EXTRA_PROVISIONING_WIFI_SECURITY_TYPE = "android.app.extra.PROVISIONING_WIFI_SECURITY_TYPE";
  public static final String EXTRA_PROVISIONING_WIFI_SSID = "android.app.extra.PROVISIONING_WIFI_SSID";
  public static final String EXTRA_REMOTE_BUGREPORT_HASH = "android.intent.extra.REMOTE_BUGREPORT_HASH";
  public static final String EXTRA_RESTRICTION = "android.app.extra.RESTRICTION";
  public static final int FLAG_EVICT_CREDENTIAL_ENCRYPTION_KEY = 1;
  public static final int FLAG_MANAGED_CAN_ACCESS_PARENT = 2;
  public static final int FLAG_PARENT_CAN_ACCESS_MANAGED = 1;
  public static final int ID_TYPE_BASE_INFO = 1;
  public static final int ID_TYPE_IMEI = 4;
  public static final int ID_TYPE_MEID = 8;
  public static final int ID_TYPE_SERIAL = 2;
  public static final int INSTALLKEY_REQUEST_CREDENTIALS_ACCESS = 1;
  public static final int INSTALLKEY_SET_USER_SELECTABLE = 2;
  public static final int KEYGUARD_DISABLE_BIOMETRICS = 416;
  public static final int KEYGUARD_DISABLE_FACE = 128;
  public static final int KEYGUARD_DISABLE_FEATURES_ALL = Integer.MAX_VALUE;
  public static final int KEYGUARD_DISABLE_FEATURES_NONE = 0;
  public static final int KEYGUARD_DISABLE_FINGERPRINT = 32;
  public static final int KEYGUARD_DISABLE_IRIS = 256;
  public static final int KEYGUARD_DISABLE_REMOTE_INPUT = 64;
  public static final int KEYGUARD_DISABLE_SECURE_CAMERA = 2;
  public static final int KEYGUARD_DISABLE_SECURE_NOTIFICATIONS = 4;
  public static final int KEYGUARD_DISABLE_TRUST_AGENTS = 16;
  public static final int KEYGUARD_DISABLE_UNREDACTED_NOTIFICATIONS = 8;
  public static final int KEYGUARD_DISABLE_WIDGETS_ALL = 1;
  public static final int LEAVE_ALL_SYSTEM_APPS_ENABLED = 16;
  public static final int LOCK_TASK_FEATURE_GLOBAL_ACTIONS = 16;
  public static final int LOCK_TASK_FEATURE_HOME = 4;
  public static final int LOCK_TASK_FEATURE_KEYGUARD = 32;
  public static final int LOCK_TASK_FEATURE_NONE = 0;
  public static final int LOCK_TASK_FEATURE_NOTIFICATIONS = 2;
  public static final int LOCK_TASK_FEATURE_OVERVIEW = 8;
  public static final int LOCK_TASK_FEATURE_SYSTEM_INFO = 1;
  public static final int MAKE_USER_DEMO = 4;
  public static final int MAKE_USER_EPHEMERAL = 2;
  public static final String MIME_TYPE_PROVISIONING_NFC = "application/com.android.managedprovisioning";
  public static final int NOTIFICATION_BUGREPORT_ACCEPTED_NOT_FINISHED = 2;
  public static final int NOTIFICATION_BUGREPORT_FINISHED_NOT_ACCEPTED = 3;
  public static final int NOTIFICATION_BUGREPORT_STARTED = 1;
  public static final int PASSWORD_QUALITY_ALPHABETIC = 262144;
  public static final int PASSWORD_QUALITY_ALPHANUMERIC = 327680;
  public static final int PASSWORD_QUALITY_BIOMETRIC_WEAK = 32768;
  public static final int PASSWORD_QUALITY_COMPLEX = 393216;
  public static final int PASSWORD_QUALITY_MANAGED = 524288;
  public static final int PASSWORD_QUALITY_NUMERIC = 131072;
  public static final int PASSWORD_QUALITY_NUMERIC_COMPLEX = 196608;
  public static final int PASSWORD_QUALITY_SOMETHING = 65536;
  public static final int PASSWORD_QUALITY_UNSPECIFIED = 0;
  public static final int PERMISSION_GRANT_STATE_DEFAULT = 0;
  public static final int PERMISSION_GRANT_STATE_DENIED = 2;
  public static final int PERMISSION_GRANT_STATE_GRANTED = 1;
  public static final int PERMISSION_POLICY_AUTO_DENY = 2;
  public static final int PERMISSION_POLICY_AUTO_GRANT = 1;
  public static final int PERMISSION_POLICY_PROMPT = 0;
  public static final String POLICY_DISABLE_CAMERA = "policy_disable_camera";
  public static final String POLICY_DISABLE_SCREEN_CAPTURE = "policy_disable_screen_capture";
  public static final String POLICY_MANDATORY_BACKUPS = "policy_mandatory_backups";
  public static final String POLICY_SUSPEND_PACKAGES = "policy_suspend_packages";
  public static final int PROFILE_KEYGUARD_FEATURES_AFFECT_OWNER = 432;
  public static final int RESET_PASSWORD_DO_NOT_ASK_CREDENTIALS_ON_BOOT = 2;
  public static final int RESET_PASSWORD_REQUIRE_ENTRY = 1;
  public static final int SKIP_SETUP_WIZARD = 1;
  @SystemApi
  public static final int STATE_USER_PROFILE_COMPLETE = 4;
  @SystemApi
  public static final int STATE_USER_SETUP_COMPLETE = 2;
  @SystemApi
  public static final int STATE_USER_SETUP_FINALIZED = 3;
  @SystemApi
  public static final int STATE_USER_SETUP_INCOMPLETE = 1;
  @SystemApi
  public static final int STATE_USER_UNMANAGED = 0;
  private static String TAG = "DevicePolicyManager";
  public static final int WIPE_EUICC = 4;
  public static final int WIPE_EXTERNAL_STORAGE = 1;
  public static final int WIPE_RESET_PROTECTION_DATA = 2;
  private final Context mContext;
  private final boolean mParentInstance;
  private final IDevicePolicyManager mService;
  
  public DevicePolicyManager(Context paramContext, IDevicePolicyManager paramIDevicePolicyManager)
  {
    this(paramContext, paramIDevicePolicyManager, false);
  }
  
  @VisibleForTesting
  protected DevicePolicyManager(Context paramContext, IDevicePolicyManager paramIDevicePolicyManager, boolean paramBoolean)
  {
    mContext = paramContext;
    mService = paramIDevicePolicyManager;
    mParentInstance = paramBoolean;
  }
  
  private static String getCaCertAlias(byte[] paramArrayOfByte)
    throws CertificateException
  {
    paramArrayOfByte = (X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(paramArrayOfByte));
    return new TrustedCertificateStore().getCertificateAlias(paramArrayOfByte);
  }
  
  private ComponentName getDeviceOwnerComponentInner(boolean paramBoolean)
  {
    if (mService != null) {
      try
      {
        ComponentName localComponentName = mService.getDeviceOwnerComponent(paramBoolean);
        return localComponentName;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  private boolean isDeviceOwnerAppOnAnyUserInner(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      return false;
    }
    ComponentName localComponentName = getDeviceOwnerComponentInner(paramBoolean);
    if (localComponentName == null) {
      return false;
    }
    return paramString.equals(localComponentName.getPackageName());
  }
  
  private void throwIfParentInstance(String paramString)
  {
    if (!mParentInstance) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" cannot be called on the parent instance");
    throw new SecurityException(localStringBuilder.toString());
  }
  
  private void wipeDataInternal(int paramInt, String paramString)
  {
    if (mService != null) {
      try
      {
        mService.wipeDataWithReason(paramInt, paramString);
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
  }
  
  public void addCrossProfileIntentFilter(ComponentName paramComponentName, IntentFilter paramIntentFilter, int paramInt)
  {
    throwIfParentInstance("addCrossProfileIntentFilter");
    if (mService != null) {
      try
      {
        mService.addCrossProfileIntentFilter(paramComponentName, paramIntentFilter, paramInt);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean addCrossProfileWidgetProvider(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("addCrossProfileWidgetProvider");
    if (mService != null) {
      try
      {
        boolean bool = mService.addCrossProfileWidgetProvider(paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public int addOverrideApn(ComponentName paramComponentName, ApnSetting paramApnSetting)
  {
    throwIfParentInstance("addOverrideApn");
    if (mService != null) {
      try
      {
        int i = mService.addOverrideApn(paramComponentName, paramApnSetting);
        return i;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return -1;
  }
  
  public void addPersistentPreferredActivity(ComponentName paramComponentName1, IntentFilter paramIntentFilter, ComponentName paramComponentName2)
  {
    throwIfParentInstance("addPersistentPreferredActivity");
    if (mService != null) {
      try
      {
        mService.addPersistentPreferredActivity(paramComponentName1, paramIntentFilter, paramComponentName2);
      }
      catch (RemoteException paramComponentName1)
      {
        throw paramComponentName1.rethrowFromSystemServer();
      }
    }
  }
  
  public void addUserRestriction(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("addUserRestriction");
    if (mService != null) {
      try
      {
        mService.setUserRestriction(paramComponentName, paramString, true);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean approveCaCert(String paramString, int paramInt, boolean paramBoolean)
  {
    if (mService != null) {
      try
      {
        paramBoolean = mService.approveCaCert(paramString, paramInt, paramBoolean);
        return paramBoolean;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean bindDeviceAdminServiceAsUser(ComponentName paramComponentName, Intent paramIntent, ServiceConnection paramServiceConnection, int paramInt, UserHandle paramUserHandle)
  {
    throwIfParentInstance("bindDeviceAdminServiceAsUser");
    try
    {
      paramServiceConnection = mContext.getServiceDispatcher(paramServiceConnection, mContext.getMainThreadHandler(), paramInt);
      paramIntent.prepareToLeaveProcess(mContext);
      boolean bool = mService.bindDeviceAdminServiceAsUser(paramComponentName, mContext.getIApplicationThread(), mContext.getActivityToken(), paramIntent, paramServiceConnection, paramInt, paramUserHandle.getIdentifier());
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public int checkProvisioningPreCondition(String paramString1, String paramString2)
  {
    try
    {
      int i = mService.checkProvisioningPreCondition(paramString1, paramString2);
      return i;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void clearApplicationUserData(ComponentName paramComponentName, String paramString, Executor paramExecutor, OnClearApplicationUserDataListener paramOnClearApplicationUserDataListener)
  {
    throwIfParentInstance("clearAppData");
    Preconditions.checkNotNull(paramExecutor);
    Preconditions.checkNotNull(paramOnClearApplicationUserDataListener);
    try
    {
      IDevicePolicyManager localIDevicePolicyManager = mService;
      IPackageDataObserver.Stub local1 = new android/app/admin/DevicePolicyManager$1;
      local1.<init>(this, paramExecutor, paramOnClearApplicationUserDataListener);
      localIDevicePolicyManager.clearApplicationUserData(paramComponentName, paramString, local1);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void clearCrossProfileIntentFilters(ComponentName paramComponentName)
  {
    throwIfParentInstance("clearCrossProfileIntentFilters");
    if (mService != null) {
      try
      {
        mService.clearCrossProfileIntentFilters(paramComponentName);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  @Deprecated
  public void clearDeviceOwnerApp(String paramString)
  {
    throwIfParentInstance("clearDeviceOwnerApp");
    if (mService != null) {
      try
      {
        mService.clearDeviceOwner(paramString);
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
  }
  
  public void clearPackagePersistentPreferredActivities(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("clearPackagePersistentPreferredActivities");
    if (mService != null) {
      try
      {
        mService.clearPackagePersistentPreferredActivities(paramComponentName, paramString);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  @Deprecated
  public void clearProfileOwner(ComponentName paramComponentName)
  {
    throwIfParentInstance("clearProfileOwner");
    if (mService != null) {
      try
      {
        mService.clearProfileOwner(paramComponentName);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean clearResetPasswordToken(ComponentName paramComponentName)
  {
    throwIfParentInstance("clearResetPasswordToken");
    if (mService != null) {
      try
      {
        boolean bool = mService.clearResetPasswordToken(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void clearSystemUpdatePolicyFreezePeriodRecord()
  {
    throwIfParentInstance("clearSystemUpdatePolicyFreezePeriodRecord");
    if (mService == null) {
      return;
    }
    try
    {
      mService.clearSystemUpdatePolicyFreezePeriodRecord();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void clearUserRestriction(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("clearUserRestriction");
    if (mService != null) {
      try
      {
        mService.setUserRestriction(paramComponentName, paramString, false);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public Intent createAdminSupportIntent(String paramString)
  {
    throwIfParentInstance("createAdminSupportIntent");
    if (mService != null) {
      try
      {
        paramString = mService.createAdminSupportIntent(paramString);
        return paramString;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  @Deprecated
  public UserHandle createAndInitializeUser(ComponentName paramComponentName1, String paramString1, String paramString2, ComponentName paramComponentName2, Bundle paramBundle)
  {
    return null;
  }
  
  public UserHandle createAndManageUser(ComponentName paramComponentName1, String paramString, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle, int paramInt)
  {
    throwIfParentInstance("createAndManageUser");
    try
    {
      paramComponentName1 = mService.createAndManageUser(paramComponentName1, paramString, paramComponentName2, paramPersistableBundle, paramInt);
      return paramComponentName1;
    }
    catch (RemoteException paramComponentName1)
    {
      throw paramComponentName1.rethrowFromSystemServer();
    }
    catch (ServiceSpecificException paramComponentName1)
    {
      throw new UserManager.UserOperationException(paramComponentName1.getMessage(), errorCode);
    }
  }
  
  @Deprecated
  public UserHandle createUser(ComponentName paramComponentName, String paramString)
  {
    return null;
  }
  
  public int enableSystemApp(ComponentName paramComponentName, Intent paramIntent)
  {
    throwIfParentInstance("enableSystemApp");
    if (mService != null) {
      try
      {
        int i = mService.enableSystemAppWithIntent(paramComponentName, mContext.getPackageName(), paramIntent);
        return i;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public void enableSystemApp(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("enableSystemApp");
    if (mService != null) {
      try
      {
        mService.enableSystemApp(paramComponentName, mContext.getPackageName(), paramString);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void forceRemoveActiveAdmin(ComponentName paramComponentName, int paramInt)
  {
    try
    {
      mService.forceRemoveActiveAdmin(paramComponentName, paramInt);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public long forceSecurityLogs()
  {
    if (mService == null) {
      return 0L;
    }
    try
    {
      long l = mService.forceSecurityLogs();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void forceUpdateUserSetupComplete()
  {
    try
    {
      mService.forceUpdateUserSetupComplete();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  /* Error */
  public android.security.AttestedKeyPair generateKeyPair(ComponentName paramComponentName, String paramString, android.security.keystore.KeyGenParameterSpec paramKeyGenParameterSpec, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc_w 697
    //   4: invokespecial 525	android/app/admin/DevicePolicyManager:throwIfParentInstance	(Ljava/lang/String;)V
    //   7: new 699	android/security/keystore/ParcelableKeyGenParameterSpec
    //   10: astore 5
    //   12: aload 5
    //   14: aload_3
    //   15: invokespecial 702	android/security/keystore/ParcelableKeyGenParameterSpec:<init>	(Landroid/security/keystore/KeyGenParameterSpec;)V
    //   18: new 704	android/security/keymaster/KeymasterCertificateChain
    //   21: astore 6
    //   23: aload 6
    //   25: invokespecial 705	android/security/keymaster/KeymasterCertificateChain:<init>	()V
    //   28: aload_0
    //   29: getfield 434	android/app/admin/DevicePolicyManager:mService	Landroid/app/admin/IDevicePolicyManager;
    //   32: aload_1
    //   33: aload_0
    //   34: getfield 432	android/app/admin/DevicePolicyManager:mContext	Landroid/content/Context;
    //   37: invokevirtual 670	android/content/Context:getPackageName	()Ljava/lang/String;
    //   40: aload_2
    //   41: aload 5
    //   43: iload 4
    //   45: aload 6
    //   47: invokeinterface 708 7 0
    //   52: ifne +15 -> 67
    //   55: getstatic 421	android/app/admin/DevicePolicyManager:TAG	Ljava/lang/String;
    //   58: ldc_w 710
    //   61: invokestatic 715	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   64: pop
    //   65: aconst_null
    //   66: areturn
    //   67: aload_3
    //   68: invokevirtual 720	android/security/keystore/KeyGenParameterSpec:getKeystoreAlias	()Ljava/lang/String;
    //   71: astore_3
    //   72: aload_0
    //   73: getfield 432	android/app/admin/DevicePolicyManager:mContext	Landroid/content/Context;
    //   76: aload_3
    //   77: invokestatic 726	android/security/KeyChain:getKeyPair	(Landroid/content/Context;Ljava/lang/String;)Ljava/security/KeyPair;
    //   80: astore 5
    //   82: aconst_null
    //   83: astore_2
    //   84: aload 6
    //   86: invokestatic 732	android/security/keystore/AttestationUtils:isChainValid	(Landroid/security/keymaster/KeymasterCertificateChain;)Z
    //   89: ifeq +9 -> 98
    //   92: aload 6
    //   94: invokestatic 736	android/security/keystore/AttestationUtils:parseCertificateChain	(Landroid/security/keymaster/KeymasterCertificateChain;)[Ljava/security/cert/X509Certificate;
    //   97: astore_2
    //   98: new 738	android/security/AttestedKeyPair
    //   101: dup
    //   102: aload 5
    //   104: aload_2
    //   105: invokespecial 741	android/security/AttestedKeyPair:<init>	(Ljava/security/KeyPair;[Ljava/security/cert/Certificate;)V
    //   108: areturn
    //   109: astore 5
    //   111: getstatic 421	android/app/admin/DevicePolicyManager:TAG	Ljava/lang/String;
    //   114: astore 6
    //   116: new 501	java/lang/StringBuilder
    //   119: astore_2
    //   120: aload_2
    //   121: invokespecial 502	java/lang/StringBuilder:<init>	()V
    //   124: aload_2
    //   125: ldc_w 743
    //   128: invokevirtual 506	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload_2
    //   133: aload_3
    //   134: invokevirtual 506	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: pop
    //   138: aload 6
    //   140: aload_2
    //   141: invokevirtual 513	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   144: aload 5
    //   146: invokestatic 746	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   149: pop
    //   150: aload_0
    //   151: getfield 434	android/app/admin/DevicePolicyManager:mService	Landroid/app/admin/IDevicePolicyManager;
    //   154: aload_1
    //   155: aload_0
    //   156: getfield 432	android/app/admin/DevicePolicyManager:mContext	Landroid/content/Context;
    //   159: invokevirtual 670	android/content/Context:getPackageName	()Ljava/lang/String;
    //   162: aload_3
    //   163: invokeinterface 750 4 0
    //   168: pop
    //   169: aconst_null
    //   170: areturn
    //   171: astore_1
    //   172: getstatic 421	android/app/admin/DevicePolicyManager:TAG	Ljava/lang/String;
    //   175: ldc_w 752
    //   178: aload_1
    //   179: invokestatic 755	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   182: pop
    //   183: invokestatic 761	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   186: invokevirtual 764	java/lang/Thread:interrupt	()V
    //   189: goto +15 -> 204
    //   192: astore_1
    //   193: getstatic 421	android/app/admin/DevicePolicyManager:TAG	Ljava/lang/String;
    //   196: ldc_w 766
    //   199: aload_1
    //   200: invokestatic 755	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   203: pop
    //   204: aconst_null
    //   205: areturn
    //   206: astore_1
    //   207: aload_1
    //   208: invokevirtual 481	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   211: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	212	0	this	DevicePolicyManager
    //   0	212	1	paramComponentName	ComponentName
    //   0	212	2	paramString	String
    //   0	212	3	paramKeyGenParameterSpec	android.security.keystore.KeyGenParameterSpec
    //   0	212	4	paramInt	int
    //   10	93	5	localObject1	Object
    //   109	36	5	localKeyAttestationException	android.security.keystore.KeyAttestationException
    //   21	118	6	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   84	98	109	android/security/keystore/KeyAttestationException
    //   7	65	171	java/lang/InterruptedException
    //   67	82	171	java/lang/InterruptedException
    //   84	98	171	java/lang/InterruptedException
    //   98	109	171	java/lang/InterruptedException
    //   111	169	171	java/lang/InterruptedException
    //   7	65	192	android/security/KeyChainException
    //   67	82	192	android/security/KeyChainException
    //   84	98	192	android/security/KeyChainException
    //   98	109	192	android/security/KeyChainException
    //   111	169	192	android/security/KeyChainException
    //   7	65	206	android/os/RemoteException
    //   67	82	206	android/os/RemoteException
    //   84	98	206	android/os/RemoteException
    //   98	109	206	android/os/RemoteException
    //   111	169	206	android/os/RemoteException
  }
  
  public String[] getAccountTypesWithManagementDisabled()
  {
    throwIfParentInstance("getAccountTypesWithManagementDisabled");
    return getAccountTypesWithManagementDisabledAsUser(myUserId());
  }
  
  public String[] getAccountTypesWithManagementDisabledAsUser(int paramInt)
  {
    if (mService != null) {
      try
      {
        String[] arrayOfString = mService.getAccountTypesWithManagementDisabledAsUser(paramInt);
        return arrayOfString;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public List<ComponentName> getActiveAdmins()
  {
    throwIfParentInstance("getActiveAdmins");
    return getActiveAdminsAsUser(myUserId());
  }
  
  public List<ComponentName> getActiveAdminsAsUser(int paramInt)
  {
    if (mService != null) {
      try
      {
        List localList = mService.getActiveAdmins(paramInt);
        return localList;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public Set<String> getAffiliationIds(ComponentName paramComponentName)
  {
    throwIfParentInstance("getAffiliationIds");
    try
    {
      paramComponentName = new ArraySet(mService.getAffiliationIds(paramComponentName));
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public String getAlwaysOnVpnPackage(ComponentName paramComponentName)
  {
    throwIfParentInstance("getAlwaysOnVpnPackage");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getAlwaysOnVpnPackage(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public Bundle getApplicationRestrictions(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("getApplicationRestrictions");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getApplicationRestrictions(paramComponentName, mContext.getPackageName(), paramString);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  @Deprecated
  public String getApplicationRestrictionsManagingPackage(ComponentName paramComponentName)
  {
    throwIfParentInstance("getApplicationRestrictionsManagingPackage");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getApplicationRestrictionsManagingPackage(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public boolean getAutoTimeRequired()
  {
    throwIfParentInstance("getAutoTimeRequired");
    if (mService != null) {
      try
      {
        boolean bool = mService.getAutoTimeRequired();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName paramComponentName)
  {
    throwIfParentInstance("getBindDeviceAdminTargetUsers");
    try
    {
      paramComponentName = mService.getBindDeviceAdminTargetUsers(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean getBluetoothContactSharingDisabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("getBluetoothContactSharingDisabled");
    if (mService != null) {
      try
      {
        boolean bool = mService.getBluetoothContactSharingDisabled(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return true;
  }
  
  public boolean getBluetoothContactSharingDisabled(UserHandle paramUserHandle)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.getBluetoothContactSharingDisabledForUser(paramUserHandle.getIdentifier());
        return bool;
      }
      catch (RemoteException paramUserHandle)
      {
        throw paramUserHandle.rethrowFromSystemServer();
      }
    }
    return true;
  }
  
  public boolean getCameraDisabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("getCameraDisabled");
    return getCameraDisabled(paramComponentName, myUserId());
  }
  
  public boolean getCameraDisabled(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.getCameraDisabled(paramComponentName, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  @Deprecated
  public String getCertInstallerPackage(ComponentName paramComponentName)
    throws SecurityException
  {
    throwIfParentInstance("getCertInstallerPackage");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getCertInstallerPackage(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public boolean getCrossProfileCallerIdDisabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("getCrossProfileCallerIdDisabled");
    if (mService != null) {
      try
      {
        boolean bool = mService.getCrossProfileCallerIdDisabled(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean getCrossProfileCallerIdDisabled(UserHandle paramUserHandle)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.getCrossProfileCallerIdDisabledForUser(paramUserHandle.getIdentifier());
        return bool;
      }
      catch (RemoteException paramUserHandle)
      {
        throw paramUserHandle.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean getCrossProfileContactsSearchDisabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("getCrossProfileContactsSearchDisabled");
    if (mService != null) {
      try
      {
        boolean bool = mService.getCrossProfileContactsSearchDisabled(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean getCrossProfileContactsSearchDisabled(UserHandle paramUserHandle)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.getCrossProfileContactsSearchDisabledForUser(paramUserHandle.getIdentifier());
        return bool;
      }
      catch (RemoteException paramUserHandle)
      {
        throw paramUserHandle.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public List<String> getCrossProfileWidgetProviders(ComponentName paramComponentName)
  {
    throwIfParentInstance("getCrossProfileWidgetProviders");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getCrossProfileWidgetProviders(paramComponentName);
        if (paramComponentName != null) {
          return paramComponentName;
        }
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return Collections.emptyList();
  }
  
  public int getCurrentFailedPasswordAttempts()
  {
    return getCurrentFailedPasswordAttempts(myUserId());
  }
  
  public int getCurrentFailedPasswordAttempts(int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getCurrentFailedPasswordAttempts(paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return -1;
  }
  
  public int getCurrentFailedPasswordAttemptsForAsus()
  {
    if (mService != null) {
      try
      {
        int i = mService.getCurrentFailedPasswordAttemptsForAsus(UserHandle.myUserId());
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        Log.w(TAG, "Failed talking with device policy service", localRemoteException);
      }
    }
    return -1;
  }
  
  public List<String> getDelegatePackages(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("getDelegatePackages");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getDelegatePackages(paramComponentName, paramString);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public List<String> getDelegatedScopes(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("getDelegatedScopes");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getDelegatedScopes(paramComponentName, paramString);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  @SystemApi
  @Deprecated
  @SuppressLint({"Doclava125"})
  public String getDeviceInitializerApp()
  {
    return null;
  }
  
  @SystemApi
  @Deprecated
  @SuppressLint({"Doclava125"})
  public ComponentName getDeviceInitializerComponent()
  {
    return null;
  }
  
  @SystemApi
  public String getDeviceOwner()
  {
    throwIfParentInstance("getDeviceOwner");
    Object localObject = getDeviceOwnerComponentOnCallingUser();
    if (localObject != null) {
      localObject = ((ComponentName)localObject).getPackageName();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  @SystemApi
  public ComponentName getDeviceOwnerComponentOnAnyUser()
  {
    return getDeviceOwnerComponentInner(false);
  }
  
  public ComponentName getDeviceOwnerComponentOnCallingUser()
  {
    return getDeviceOwnerComponentInner(true);
  }
  
  public CharSequence getDeviceOwnerLockScreenInfo()
  {
    throwIfParentInstance("getDeviceOwnerLockScreenInfo");
    if (mService != null) {
      try
      {
        CharSequence localCharSequence = mService.getDeviceOwnerLockScreenInfo();
        return localCharSequence;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  @SystemApi
  public String getDeviceOwnerNameOnAnyUser()
  {
    throwIfParentInstance("getDeviceOwnerNameOnAnyUser");
    if (mService != null) {
      try
      {
        String str = mService.getDeviceOwnerName();
        return str;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public CharSequence getDeviceOwnerOrganizationName()
  {
    try
    {
      CharSequence localCharSequence = mService.getDeviceOwnerOrganizationName();
      return localCharSequence;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getDeviceOwnerUserId()
  {
    if (mService != null) {
      try
      {
        int i = mService.getDeviceOwnerUserId();
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return 55536;
  }
  
  public Set<String> getDisallowedSystemApps(ComponentName paramComponentName, int paramInt, String paramString)
  {
    try
    {
      paramComponentName = new ArraySet(mService.getDisallowedSystemApps(paramComponentName, paramInt, paramString));
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean getDoNotAskCredentialsOnBoot()
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.getDoNotAskCredentialsOnBoot();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public CharSequence getEndUserSessionMessage(ComponentName paramComponentName)
  {
    throwIfParentInstance("getEndUserSessionMessage");
    try
    {
      paramComponentName = mService.getEndUserSessionMessage(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean getForceEphemeralUsers(ComponentName paramComponentName)
  {
    throwIfParentInstance("getForceEphemeralUsers");
    if (mService != null) {
      try
      {
        boolean bool = mService.getForceEphemeralUsers(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public ComponentName getGlobalProxyAdmin()
  {
    if (mService != null) {
      try
      {
        ComponentName localComponentName = mService.getGlobalProxyAdmin(myUserId());
        return localComponentName;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public boolean getGuestUserDisabled(ComponentName paramComponentName)
  {
    return false;
  }
  
  public List<byte[]> getInstalledCaCerts(ComponentName paramComponentName)
  {
    ArrayList localArrayList = new ArrayList();
    throwIfParentInstance("getInstalledCaCerts");
    if (mService != null) {
      try
      {
        mService.enforceCanManageCaCerts(paramComponentName, mContext.getPackageName());
        paramComponentName = new com/android/org/conscrypt/TrustedCertificateStore;
        paramComponentName.<init>();
        Iterator localIterator = paramComponentName.userAliases().iterator();
        while (localIterator.hasNext())
        {
          String str1 = (String)localIterator.next();
          try
          {
            localArrayList.add(paramComponentName.getCertificate(str1).getEncoded());
          }
          catch (CertificateException localCertificateException)
          {
            String str2 = TAG;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Could not encode certificate: ");
            localStringBuilder.append(str1);
            Log.w(str2, localStringBuilder.toString(), localCertificateException);
          }
        }
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return localArrayList;
  }
  
  public List<String> getKeepUninstalledPackages(ComponentName paramComponentName)
  {
    throwIfParentInstance("getKeepUninstalledPackages");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getKeepUninstalledPackages(paramComponentName, mContext.getPackageName());
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public int getKeyguardDisabledFeatures(ComponentName paramComponentName)
  {
    return getKeyguardDisabledFeatures(paramComponentName, myUserId());
  }
  
  public int getKeyguardDisabledFeatures(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getKeyguardDisabledFeatures(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public long getLastBugReportRequestTime()
  {
    try
    {
      long l = mService.getLastBugReportRequestTime();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getLastNetworkLogRetrievalTime()
  {
    try
    {
      long l = mService.getLastNetworkLogRetrievalTime();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getLastSecurityLogRetrievalTime()
  {
    try
    {
      long l = mService.getLastSecurityLogRetrievalTime();
      return l;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getLockTaskFeatures(ComponentName paramComponentName)
  {
    throwIfParentInstance("getLockTaskFeatures");
    if (mService != null) {
      try
      {
        int i = mService.getLockTaskFeatures(paramComponentName);
        return i;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public String[] getLockTaskPackages(ComponentName paramComponentName)
  {
    throwIfParentInstance("getLockTaskPackages");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getLockTaskPackages(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return new String[0];
  }
  
  public CharSequence getLongSupportMessage(ComponentName paramComponentName)
  {
    throwIfParentInstance("getLongSupportMessage");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getLongSupportMessage(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public CharSequence getLongSupportMessageForUser(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramComponentName = mService.getLongSupportMessageForUser(paramComponentName, paramInt);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public ComponentName getMandatoryBackupTransport()
  {
    throwIfParentInstance("getMandatoryBackupTransport");
    try
    {
      ComponentName localComponentName = mService.getMandatoryBackupTransport();
      return localComponentName;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getMaximumFailedPasswordsForWipe(ComponentName paramComponentName)
  {
    return getMaximumFailedPasswordsForWipe(paramComponentName, myUserId());
  }
  
  public int getMaximumFailedPasswordsForWipe(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getMaximumFailedPasswordsForWipe(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public long getMaximumTimeToLock(ComponentName paramComponentName)
  {
    return getMaximumTimeToLock(paramComponentName, myUserId());
  }
  
  public long getMaximumTimeToLock(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        long l = mService.getMaximumTimeToLock(paramComponentName, paramInt, mParentInstance);
        return l;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0L;
  }
  
  public List<String> getMeteredDataDisabledPackages(ComponentName paramComponentName)
  {
    throwIfParentInstance("getMeteredDataDisabled");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getMeteredDataDisabledPackages(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return new ArrayList();
  }
  
  public int getOrganizationColor(ComponentName paramComponentName)
  {
    throwIfParentInstance("getOrganizationColor");
    try
    {
      int i = mService.getOrganizationColor(paramComponentName);
      return i;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public int getOrganizationColorForUser(int paramInt)
  {
    try
    {
      paramInt = mService.getOrganizationColorForUser(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public CharSequence getOrganizationName(ComponentName paramComponentName)
  {
    throwIfParentInstance("getOrganizationName");
    try
    {
      paramComponentName = mService.getOrganizationName(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public CharSequence getOrganizationNameForUser(int paramInt)
  {
    try
    {
      CharSequence localCharSequence = mService.getOrganizationNameForUser(paramInt);
      return localCharSequence;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<ApnSetting> getOverrideApns(ComponentName paramComponentName)
  {
    throwIfParentInstance("getOverrideApns");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getOverrideApns(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return Collections.emptyList();
  }
  
  public List<String> getOwnerInstalledCaCerts(UserHandle paramUserHandle)
  {
    try
    {
      paramUserHandle = mService.getOwnerInstalledCaCerts(paramUserHandle).getList();
      return paramUserHandle;
    }
    catch (RemoteException paramUserHandle)
    {
      throw paramUserHandle.rethrowFromSystemServer();
    }
  }
  
  public DevicePolicyManager getParentProfileInstance(ComponentName paramComponentName)
  {
    throwIfParentInstance("getParentProfileInstance");
    try
    {
      if (mService.isManagedProfile(paramComponentName)) {
        return new DevicePolicyManager(mContext, mService, true);
      }
      paramComponentName = new java/lang/SecurityException;
      paramComponentName.<init>("The current user does not have a parent profile.");
      throw paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public DevicePolicyManager getParentProfileInstance(UserInfo paramUserInfo)
  {
    mContext.checkSelfPermission("android.permission.MANAGE_PROFILE_AND_DEVICE_OWNERS");
    if (paramUserInfo.isManagedProfile()) {
      return new DevicePolicyManager(mContext, mService, true);
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("The user ");
    localStringBuilder.append(id);
    localStringBuilder.append(" does not have a parent profile.");
    throw new SecurityException(localStringBuilder.toString());
  }
  
  public long getPasswordExpiration(ComponentName paramComponentName)
  {
    if (mService != null) {
      try
      {
        long l = mService.getPasswordExpiration(paramComponentName, myUserId(), mParentInstance);
        return l;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0L;
  }
  
  public long getPasswordExpirationTimeout(ComponentName paramComponentName)
  {
    if (mService != null) {
      try
      {
        long l = mService.getPasswordExpirationTimeout(paramComponentName, myUserId(), mParentInstance);
        return l;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0L;
  }
  
  public int getPasswordHistoryLength(ComponentName paramComponentName)
  {
    return getPasswordHistoryLength(paramComponentName, myUserId());
  }
  
  public int getPasswordHistoryLength(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordHistoryLength(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public int getPasswordLengthDirectly(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordLengthDirectly(paramComponentName, paramInt);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        Log.w(TAG, "Failed talking with device policy service", paramComponentName);
      }
    }
    return -1;
  }
  
  public int getPasswordMaximumLength(int paramInt)
  {
    return 16;
  }
  
  public int getPasswordMinimumLength(ComponentName paramComponentName)
  {
    return getPasswordMinimumLength(paramComponentName, myUserId());
  }
  
  public int getPasswordMinimumLength(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordMinimumLength(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public int getPasswordMinimumLetters(ComponentName paramComponentName)
  {
    return getPasswordMinimumLetters(paramComponentName, myUserId());
  }
  
  public int getPasswordMinimumLetters(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordMinimumLetters(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public int getPasswordMinimumLowerCase(ComponentName paramComponentName)
  {
    return getPasswordMinimumLowerCase(paramComponentName, myUserId());
  }
  
  public int getPasswordMinimumLowerCase(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordMinimumLowerCase(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public int getPasswordMinimumNonLetter(ComponentName paramComponentName)
  {
    return getPasswordMinimumNonLetter(paramComponentName, myUserId());
  }
  
  public int getPasswordMinimumNonLetter(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordMinimumNonLetter(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public int getPasswordMinimumNumeric(ComponentName paramComponentName)
  {
    return getPasswordMinimumNumeric(paramComponentName, myUserId());
  }
  
  public int getPasswordMinimumNumeric(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordMinimumNumeric(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public int getPasswordMinimumSymbols(ComponentName paramComponentName)
  {
    return getPasswordMinimumSymbols(paramComponentName, myUserId());
  }
  
  public int getPasswordMinimumSymbols(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordMinimumSymbols(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public int getPasswordMinimumUpperCase(ComponentName paramComponentName)
  {
    return getPasswordMinimumUpperCase(paramComponentName, myUserId());
  }
  
  public int getPasswordMinimumUpperCase(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordMinimumUpperCase(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public int getPasswordQuality(ComponentName paramComponentName)
  {
    return getPasswordQuality(paramComponentName, myUserId());
  }
  
  public int getPasswordQuality(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getPasswordQuality(paramComponentName, paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public SystemUpdateInfo getPendingSystemUpdate(ComponentName paramComponentName)
  {
    throwIfParentInstance("getPendingSystemUpdate");
    try
    {
      paramComponentName = mService.getPendingSystemUpdate(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public int getPermissionGrantState(ComponentName paramComponentName, String paramString1, String paramString2)
  {
    throwIfParentInstance("getPermissionGrantState");
    try
    {
      int i = mService.getPermissionGrantState(paramComponentName, mContext.getPackageName(), paramString1, paramString2);
      return i;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public int getPermissionPolicy(ComponentName paramComponentName)
  {
    throwIfParentInstance("getPermissionPolicy");
    try
    {
      int i = mService.getPermissionPolicy(paramComponentName);
      return i;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public List<String> getPermittedAccessibilityServices(int paramInt)
  {
    throwIfParentInstance("getPermittedAccessibilityServices");
    if (mService != null) {
      try
      {
        List localList = mService.getPermittedAccessibilityServicesForUser(paramInt);
        return localList;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public List<String> getPermittedAccessibilityServices(ComponentName paramComponentName)
  {
    throwIfParentInstance("getPermittedAccessibilityServices");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getPermittedAccessibilityServices(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public List<String> getPermittedCrossProfileNotificationListeners(ComponentName paramComponentName)
  {
    throwIfParentInstance("getPermittedCrossProfileNotificationListeners");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getPermittedCrossProfileNotificationListeners(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public List<String> getPermittedInputMethods(ComponentName paramComponentName)
  {
    throwIfParentInstance("getPermittedInputMethods");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getPermittedInputMethods(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  @SystemApi
  public List<String> getPermittedInputMethodsForCurrentUser()
  {
    throwIfParentInstance("getPermittedInputMethodsForCurrentUser");
    if (mService != null) {
      try
      {
        List localList = mService.getPermittedInputMethodsForCurrentUser();
        return localList;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  @SystemApi
  public ComponentName getProfileOwner()
    throws IllegalArgumentException
  {
    throwIfParentInstance("getProfileOwner");
    return getProfileOwnerAsUser(mContext.getUserId());
  }
  
  public ComponentName getProfileOwnerAsUser(int paramInt)
    throws IllegalArgumentException
  {
    if (mService != null) {
      try
      {
        ComponentName localComponentName = mService.getProfileOwner(paramInt);
        return localComponentName;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public String getProfileOwnerName()
    throws IllegalArgumentException
  {
    if (mService != null) {
      try
      {
        String str = mService.getProfileOwnerName(mContext.getUserId());
        return str;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  @SystemApi
  public String getProfileOwnerNameAsUser(int paramInt)
    throws IllegalArgumentException
  {
    throwIfParentInstance("getProfileOwnerNameAsUser");
    if (mService != null) {
      try
      {
        String str = mService.getProfileOwnerName(paramInt);
        return str;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public int getProfileWithMinimumFailedPasswordsForWipe(int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getProfileWithMinimumFailedPasswordsForWipe(paramInt, mParentInstance);
        return paramInt;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return 55536;
  }
  
  public void getRemoveWarning(ComponentName paramComponentName, RemoteCallback paramRemoteCallback)
  {
    if (mService != null) {
      try
      {
        mService.getRemoveWarning(paramComponentName, paramRemoteCallback, myUserId());
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public long getRequiredStrongAuthTimeout(ComponentName paramComponentName)
  {
    return getRequiredStrongAuthTimeout(paramComponentName, myUserId());
  }
  
  public long getRequiredStrongAuthTimeout(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        long l = mService.getRequiredStrongAuthTimeout(paramComponentName, paramInt, mParentInstance);
        return l;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 259200000L;
  }
  
  public boolean getScreenCaptureDisabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("getScreenCaptureDisabled");
    return getScreenCaptureDisabled(paramComponentName, myUserId());
  }
  
  public boolean getScreenCaptureDisabled(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.getScreenCaptureDisabled(paramComponentName, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public List<UserHandle> getSecondaryUsers(ComponentName paramComponentName)
  {
    throwIfParentInstance("getSecondaryUsers");
    try
    {
      paramComponentName = mService.getSecondaryUsers(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public CharSequence getShortSupportMessage(ComponentName paramComponentName)
  {
    throwIfParentInstance("getShortSupportMessage");
    if (mService != null) {
      try
      {
        paramComponentName = mService.getShortSupportMessage(paramComponentName);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public CharSequence getShortSupportMessageForUser(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramComponentName = mService.getShortSupportMessageForUser(paramComponentName, paramInt);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public CharSequence getStartUserSessionMessage(ComponentName paramComponentName)
  {
    throwIfParentInstance("getStartUserSessionMessage");
    try
    {
      paramComponentName = mService.getStartUserSessionMessage(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean getStorageEncryption(ComponentName paramComponentName)
  {
    throwIfParentInstance("getStorageEncryption");
    if (mService != null) {
      try
      {
        boolean bool = mService.getStorageEncryption(paramComponentName, myUserId());
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public int getStorageEncryptionStatus()
  {
    throwIfParentInstance("getStorageEncryptionStatus");
    return getStorageEncryptionStatus(myUserId());
  }
  
  public int getStorageEncryptionStatus(int paramInt)
  {
    if (mService != null) {
      try
      {
        paramInt = mService.getStorageEncryptionStatus(mContext.getPackageName(), paramInt);
        return paramInt;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public SystemUpdatePolicy getSystemUpdatePolicy()
  {
    throwIfParentInstance("getSystemUpdatePolicy");
    if (mService != null) {
      try
      {
        SystemUpdatePolicy localSystemUpdatePolicy = mService.getSystemUpdatePolicy();
        return localSystemUpdatePolicy;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return null;
  }
  
  public PersistableBundle getTransferOwnershipBundle()
  {
    throwIfParentInstance("getTransferOwnershipBundle");
    try
    {
      PersistableBundle localPersistableBundle = mService.getTransferOwnershipBundle();
      return localPersistableBundle;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<PersistableBundle> getTrustAgentConfiguration(ComponentName paramComponentName1, ComponentName paramComponentName2)
  {
    return getTrustAgentConfiguration(paramComponentName1, paramComponentName2, myUserId());
  }
  
  public List<PersistableBundle> getTrustAgentConfiguration(ComponentName paramComponentName1, ComponentName paramComponentName2, int paramInt)
  {
    if (mService != null) {
      try
      {
        paramComponentName1 = mService.getTrustAgentConfiguration(paramComponentName1, paramComponentName2, paramInt, mParentInstance);
        return paramComponentName1;
      }
      catch (RemoteException paramComponentName1)
      {
        throw paramComponentName1.rethrowFromSystemServer();
      }
    }
    return new ArrayList();
  }
  
  @SystemApi
  public int getUserProvisioningState()
  {
    throwIfParentInstance("getUserProvisioningState");
    if (mService != null) {
      try
      {
        int i = mService.getUserProvisioningState();
        return i;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public Bundle getUserRestrictions(ComponentName paramComponentName)
  {
    throwIfParentInstance("getUserRestrictions");
    Bundle localBundle = null;
    if (mService != null) {
      try
      {
        localBundle = mService.getUserRestrictions(paramComponentName);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    if (localBundle == null) {
      paramComponentName = new Bundle();
    } else {
      paramComponentName = localBundle;
    }
    return paramComponentName;
  }
  
  public String getWifiMacAddress(ComponentName paramComponentName)
  {
    throwIfParentInstance("getWifiMacAddress");
    try
    {
      paramComponentName = mService.getWifiMacAddress(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean hasCaCertInstalled(ComponentName paramComponentName, byte[] paramArrayOfByte)
  {
    throwIfParentInstance("hasCaCertInstalled");
    IDevicePolicyManager localIDevicePolicyManager = mService;
    boolean bool = false;
    if (localIDevicePolicyManager != null) {
      try
      {
        mService.enforceCanManageCaCerts(paramComponentName, mContext.getPackageName());
        paramComponentName = getCaCertAlias(paramArrayOfByte);
        if (paramComponentName != null) {
          bool = true;
        }
        return bool;
      }
      catch (CertificateException paramComponentName)
      {
        Log.w(TAG, "Could not parse certificate", paramComponentName);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean hasGrantedPolicy(ComponentName paramComponentName, int paramInt)
  {
    throwIfParentInstance("hasGrantedPolicy");
    if (mService != null) {
      try
      {
        boolean bool = mService.hasGrantedPolicy(paramComponentName, paramInt, myUserId());
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean hasUserSetupCompleted()
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.hasUserSetupCompleted();
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return true;
  }
  
  public boolean installCaCert(ComponentName paramComponentName, byte[] paramArrayOfByte)
  {
    throwIfParentInstance("installCaCert");
    if (mService != null) {
      try
      {
        boolean bool = mService.installCaCert(paramComponentName, mContext.getPackageName(), paramArrayOfByte);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean installExistingPackage(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("installExistingPackage");
    if (mService != null) {
      try
      {
        boolean bool = mService.installExistingPackage(paramComponentName, mContext.getPackageName(), paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean installKeyPair(ComponentName paramComponentName, PrivateKey paramPrivateKey, Certificate paramCertificate, String paramString)
  {
    return installKeyPair(paramComponentName, paramPrivateKey, new Certificate[] { paramCertificate }, paramString, false);
  }
  
  /* Error */
  public boolean installKeyPair(ComponentName paramComponentName, PrivateKey paramPrivateKey, Certificate[] paramArrayOfCertificate, String paramString, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: ldc_w 1334
    //   4: invokespecial 525	android/app/admin/DevicePolicyManager:throwIfParentInstance	(Ljava/lang/String;)V
    //   7: iload 5
    //   9: iconst_1
    //   10: iand
    //   11: iconst_1
    //   12: if_icmpne +9 -> 21
    //   15: iconst_1
    //   16: istore 6
    //   18: goto +6 -> 24
    //   21: iconst_0
    //   22: istore 6
    //   24: iload 5
    //   26: iconst_2
    //   27: iand
    //   28: iconst_2
    //   29: if_icmpne +9 -> 38
    //   32: iconst_1
    //   33: istore 7
    //   35: goto +6 -> 41
    //   38: iconst_0
    //   39: istore 7
    //   41: iconst_1
    //   42: anewarray 976	java/security/cert/Certificate
    //   45: dup
    //   46: iconst_0
    //   47: aload_3
    //   48: iconst_0
    //   49: aaload
    //   50: aastore
    //   51: invokestatic 1340	android/security/Credentials:convertToPem	([Ljava/security/cert/Certificate;)[B
    //   54: astore 8
    //   56: aconst_null
    //   57: astore 9
    //   59: aload_3
    //   60: arraylength
    //   61: iconst_1
    //   62: if_icmple +18 -> 80
    //   65: aload_3
    //   66: iconst_1
    //   67: aload_3
    //   68: arraylength
    //   69: invokestatic 1346	java/util/Arrays:copyOfRange	([Ljava/lang/Object;II)[Ljava/lang/Object;
    //   72: checkcast 1348	[Ljava/security/cert/Certificate;
    //   75: invokestatic 1340	android/security/Credentials:convertToPem	([Ljava/security/cert/Certificate;)[B
    //   78: astore 9
    //   80: aload_2
    //   81: invokeinterface 1353 1 0
    //   86: invokestatic 1358	java/security/KeyFactory:getInstance	(Ljava/lang/String;)Ljava/security/KeyFactory;
    //   89: astore_3
    //   90: aload_3
    //   91: aload_2
    //   92: ldc_w 1360
    //   95: invokevirtual 1364	java/security/KeyFactory:getKeySpec	(Ljava/security/Key;Ljava/lang/Class;)Ljava/security/spec/KeySpec;
    //   98: checkcast 1360	java/security/spec/PKCS8EncodedKeySpec
    //   101: invokevirtual 1365	java/security/spec/PKCS8EncodedKeySpec:getEncoded	()[B
    //   104: astore_2
    //   105: aload_0
    //   106: getfield 434	android/app/admin/DevicePolicyManager:mService	Landroid/app/admin/IDevicePolicyManager;
    //   109: aload_1
    //   110: aload_0
    //   111: getfield 432	android/app/admin/DevicePolicyManager:mContext	Landroid/content/Context;
    //   114: invokevirtual 670	android/content/Context:getPackageName	()Ljava/lang/String;
    //   117: aload_2
    //   118: aload 8
    //   120: aload 9
    //   122: aload 4
    //   124: iload 6
    //   126: iload 7
    //   128: invokeinterface 1368 9 0
    //   133: istore 6
    //   135: iload 6
    //   137: ireturn
    //   138: astore_1
    //   139: goto +12 -> 151
    //   142: astore_1
    //   143: goto +23 -> 166
    //   146: astore_1
    //   147: goto +33 -> 180
    //   150: astore_1
    //   151: getstatic 421	android/app/admin/DevicePolicyManager:TAG	Ljava/lang/String;
    //   154: ldc_w 1370
    //   157: aload_1
    //   158: invokestatic 755	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   161: pop
    //   162: goto +15 -> 177
    //   165: astore_1
    //   166: getstatic 421	android/app/admin/DevicePolicyManager:TAG	Ljava/lang/String;
    //   169: ldc_w 1372
    //   172: aload_1
    //   173: invokestatic 755	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   176: pop
    //   177: iconst_0
    //   178: ireturn
    //   179: astore_1
    //   180: aload_1
    //   181: invokevirtual 481	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   184: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	185	0	this	DevicePolicyManager
    //   0	185	1	paramComponentName	ComponentName
    //   0	185	2	paramPrivateKey	PrivateKey
    //   0	185	3	paramArrayOfCertificate	Certificate[]
    //   0	185	4	paramString	String
    //   0	185	5	paramInt	int
    //   16	120	6	bool1	boolean
    //   33	94	7	bool2	boolean
    //   54	65	8	arrayOfByte1	byte[]
    //   57	64	9	arrayOfByte2	byte[]
    // Exception table:
    //   from	to	target	type
    //   90	135	138	java/security/cert/CertificateException
    //   90	135	138	java/io/IOException
    //   90	135	142	java/security/NoSuchAlgorithmException
    //   90	135	142	java/security/spec/InvalidKeySpecException
    //   90	135	146	android/os/RemoteException
    //   41	56	150	java/security/cert/CertificateException
    //   41	56	150	java/io/IOException
    //   59	80	150	java/security/cert/CertificateException
    //   59	80	150	java/io/IOException
    //   80	90	150	java/security/cert/CertificateException
    //   80	90	150	java/io/IOException
    //   41	56	165	java/security/NoSuchAlgorithmException
    //   41	56	165	java/security/spec/InvalidKeySpecException
    //   59	80	165	java/security/NoSuchAlgorithmException
    //   59	80	165	java/security/spec/InvalidKeySpecException
    //   80	90	165	java/security/NoSuchAlgorithmException
    //   80	90	165	java/security/spec/InvalidKeySpecException
    //   41	56	179	android/os/RemoteException
    //   59	80	179	android/os/RemoteException
    //   80	90	179	android/os/RemoteException
  }
  
  public boolean installKeyPair(ComponentName paramComponentName, PrivateKey paramPrivateKey, Certificate[] paramArrayOfCertificate, String paramString, boolean paramBoolean)
  {
    int i = 2;
    if (paramBoolean) {
      i = 0x2 | 0x1;
    }
    return installKeyPair(paramComponentName, paramPrivateKey, paramArrayOfCertificate, paramString, i);
  }
  
  public boolean isAccessibilityServicePermittedByAdmin(ComponentName paramComponentName, String paramString, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isAccessibilityServicePermittedByAdmin(paramComponentName, paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isActivePasswordSufficient()
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isActivePasswordSufficient(myUserId(), mParentInstance);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isAdminActive(ComponentName paramComponentName)
  {
    throwIfParentInstance("isAdminActive");
    return isAdminActiveAsUser(paramComponentName, myUserId());
  }
  
  public boolean isAdminActiveAsUser(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isAdminActive(paramComponentName, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isAffiliatedUser()
  {
    throwIfParentInstance("isAffiliatedUser");
    try
    {
      boolean bool = mService.isAffiliatedUser();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isApplicationHidden(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("isApplicationHidden");
    if (mService != null) {
      try
      {
        boolean bool = mService.isApplicationHidden(paramComponentName, mContext.getPackageName(), paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isBackupServiceEnabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("isBackupServiceEnabled");
    try
    {
      boolean bool = mService.isBackupServiceEnabled(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isCaCertApproved(String paramString, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isCaCertApproved(paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  @Deprecated
  public boolean isCallerApplicationRestrictionsManagingPackage()
  {
    throwIfParentInstance("isCallerApplicationRestrictionsManagingPackage");
    if (mService != null) {
      try
      {
        boolean bool = mService.isCallerApplicationRestrictionsManagingPackage(mContext.getPackageName());
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isCurrentInputMethodSetByOwner()
  {
    try
    {
      boolean bool = mService.isCurrentInputMethodSetByOwner();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isDeviceIdAttestationSupported()
  {
    return mContext.getPackageManager().hasSystemFeature("android.software.device_id_attestation");
  }
  
  @SystemApi
  @SuppressLint({"Doclava125"})
  public boolean isDeviceManaged()
  {
    try
    {
      boolean bool = mService.hasDeviceOwner();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isDeviceOwnerApp(String paramString)
  {
    throwIfParentInstance("isDeviceOwnerApp");
    return isDeviceOwnerAppOnCallingUser(paramString);
  }
  
  public boolean isDeviceOwnerAppOnAnyUser(String paramString)
  {
    return isDeviceOwnerAppOnAnyUserInner(paramString, false);
  }
  
  public boolean isDeviceOwnerAppOnCallingUser(String paramString)
  {
    return isDeviceOwnerAppOnAnyUserInner(paramString, true);
  }
  
  @SystemApi
  public boolean isDeviceProvisioned()
  {
    try
    {
      boolean bool = mService.isDeviceProvisioned();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public boolean isDeviceProvisioningConfigApplied()
  {
    try
    {
      boolean bool = mService.isDeviceProvisioningConfigApplied();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isEphemeralUser(ComponentName paramComponentName)
  {
    throwIfParentInstance("isEphemeralUser");
    try
    {
      boolean bool = mService.isEphemeralUser(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isInputMethodPermittedByAdmin(ComponentName paramComponentName, String paramString, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isInputMethodPermittedByAdmin(paramComponentName, paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isLockTaskPermitted(String paramString)
  {
    throwIfParentInstance("isLockTaskPermitted");
    if (mService != null) {
      try
      {
        boolean bool = mService.isLockTaskPermitted(paramString);
        return bool;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isLogoutEnabled()
  {
    throwIfParentInstance("isLogoutEnabled");
    try
    {
      boolean bool = mService.isLogoutEnabled();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isManagedProfile(ComponentName paramComponentName)
  {
    throwIfParentInstance("isManagedProfile");
    try
    {
      boolean bool = mService.isManagedProfile(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isMasterVolumeMuted(ComponentName paramComponentName)
  {
    throwIfParentInstance("isMasterVolumeMuted");
    if (mService != null) {
      try
      {
        boolean bool = mService.isMasterVolumeMuted(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isMeteredDataDisabledPackageForUser(ComponentName paramComponentName, String paramString, int paramInt)
  {
    throwIfParentInstance("getMeteredDataDisabledForUser");
    if (mService != null) {
      try
      {
        boolean bool = mService.isMeteredDataDisabledPackageForUser(paramComponentName, paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isNetworkLoggingEnabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("isNetworkLoggingEnabled");
    try
    {
      boolean bool = mService.isNetworkLoggingEnabled(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isNotificationListenerServicePermitted(String paramString, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isNotificationListenerServicePermitted(paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return true;
  }
  
  public boolean isOverrideApnEnabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("isOverrideApnEnabled");
    if (mService != null) {
      try
      {
        boolean bool = mService.isOverrideApnEnabled(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isPackageSuspended(ComponentName paramComponentName, String paramString)
    throws PackageManager.NameNotFoundException
  {
    throwIfParentInstance("isPackageSuspended");
    if (mService != null) {
      try
      {
        boolean bool = mService.isPackageSuspended(paramComponentName, mContext.getPackageName(), paramString);
        return bool;
      }
      catch (IllegalArgumentException paramComponentName)
      {
        throw new PackageManager.NameNotFoundException(paramString);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isProfileActivePasswordSufficientForParent(int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isProfileActivePasswordSufficientForParent(paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isProfileOwnerApp(String paramString)
  {
    throwIfParentInstance("isProfileOwnerApp");
    Object localObject = mService;
    boolean bool1 = false;
    if (localObject != null) {
      try
      {
        localObject = mService.getProfileOwner(myUserId());
        boolean bool2 = bool1;
        if (localObject != null)
        {
          boolean bool3 = ((ComponentName)localObject).getPackageName().equals(paramString);
          bool2 = bool1;
          if (bool3) {
            bool2 = true;
          }
        }
        return bool2;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isProvisioningAllowed(String paramString)
  {
    throwIfParentInstance("isProvisioningAllowed");
    try
    {
      boolean bool = mService.isProvisioningAllowed(paramString, mContext.getPackageName());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isRemovingAdmin(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isRemovingAdmin(paramComponentName, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isResetPasswordTokenActive(ComponentName paramComponentName)
  {
    throwIfParentInstance("isResetPasswordTokenActive");
    if (mService != null) {
      try
      {
        boolean bool = mService.isResetPasswordTokenActive(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isSecurityLoggingEnabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("isSecurityLoggingEnabled");
    try
    {
      boolean bool = mService.isSecurityLoggingEnabled(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isSeparateProfileChallengeAllowed(int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.isSeparateProfileChallengeAllowed(paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isSystemOnlyUser(ComponentName paramComponentName)
  {
    try
    {
      boolean bool = mService.isSystemOnlyUser(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean isUninstallBlocked(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("isUninstallBlocked");
    if (mService != null) {
      try
      {
        boolean bool = mService.isUninstallBlocked(paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean isUninstallInQueue(String paramString)
  {
    try
    {
      boolean bool = mService.isUninstallInQueue(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean isUsingUnifiedPassword(ComponentName paramComponentName)
  {
    throwIfParentInstance("isUsingUnifiedPassword");
    if (mService != null) {
      try
      {
        boolean bool = mService.isUsingUnifiedPassword(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return true;
  }
  
  public void lockNow()
  {
    lockNow(0);
  }
  
  public void lockNow(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.lockNow(paramInt, mParentInstance);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public int logoutUser(ComponentName paramComponentName)
  {
    throwIfParentInstance("logoutUser");
    try
    {
      int i = mService.logoutUser(paramComponentName);
      return i;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  @VisibleForTesting
  protected int myUserId()
  {
    return mContext.getUserId();
  }
  
  @SystemApi
  public void notifyPendingSystemUpdate(long paramLong)
  {
    throwIfParentInstance("notifyPendingSystemUpdate");
    if (mService != null) {
      try
      {
        mService.notifyPendingSystemUpdate(SystemUpdateInfo.of(paramLong));
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  @SystemApi
  public void notifyPendingSystemUpdate(long paramLong, boolean paramBoolean)
  {
    throwIfParentInstance("notifyPendingSystemUpdate");
    if (mService != null) {
      try
      {
        mService.notifyPendingSystemUpdate(SystemUpdateInfo.of(paramLong, paramBoolean));
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  @SystemApi
  public boolean packageHasActiveAdmins(String paramString)
  {
    return packageHasActiveAdmins(paramString, myUserId());
  }
  
  public boolean packageHasActiveAdmins(String paramString, int paramInt)
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.packageHasActiveAdmins(paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void reboot(ComponentName paramComponentName)
  {
    throwIfParentInstance("reboot");
    try
    {
      mService.reboot(paramComponentName);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void removeActiveAdmin(ComponentName paramComponentName)
  {
    throwIfParentInstance("removeActiveAdmin");
    if (mService != null) {
      try
      {
        mService.removeActiveAdmin(paramComponentName, myUserId());
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean removeCrossProfileWidgetProvider(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("removeCrossProfileWidgetProvider");
    if (mService != null) {
      try
      {
        boolean bool = mService.removeCrossProfileWidgetProvider(paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean removeKeyPair(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("removeKeyPair");
    try
    {
      boolean bool = mService.removeKeyPair(paramComponentName, mContext.getPackageName(), paramString);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean removeOverrideApn(ComponentName paramComponentName, int paramInt)
  {
    throwIfParentInstance("removeOverrideApn");
    if (mService != null) {
      try
      {
        boolean bool = mService.removeOverrideApn(paramComponentName, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean removeUser(ComponentName paramComponentName, UserHandle paramUserHandle)
  {
    throwIfParentInstance("removeUser");
    try
    {
      boolean bool = mService.removeUser(paramComponentName, paramUserHandle);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void reportFailedFingerprintAttempt(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.reportFailedFingerprintAttempt(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void reportFailedPasswordAttempt(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.reportFailedPasswordAttempt(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void reportKeyguardDismissed(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.reportKeyguardDismissed(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void reportKeyguardSecured(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.reportKeyguardSecured(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void reportPasswordChanged(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.reportPasswordChanged(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void reportSuccessfulFingerprintAttempt(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.reportSuccessfulFingerprintAttempt(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void reportSuccessfulPasswordAttempt(int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.reportSuccessfulPasswordAttempt(paramInt);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean requestBugreport(ComponentName paramComponentName)
  {
    throwIfParentInstance("requestBugreport");
    if (mService != null) {
      try
      {
        boolean bool = mService.requestBugreport(paramComponentName);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean resetPassword(String paramString, int paramInt)
  {
    throwIfParentInstance("resetPassword");
    if (mService != null) {
      try
      {
        boolean bool = mService.resetPassword(paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean resetPasswordWithToken(ComponentName paramComponentName, String paramString, byte[] paramArrayOfByte, int paramInt)
  {
    throwIfParentInstance("resetPassword");
    if (mService != null) {
      try
      {
        boolean bool = mService.resetPasswordWithToken(paramComponentName, paramString, paramArrayOfByte, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public List<NetworkEvent> retrieveNetworkLogs(ComponentName paramComponentName, long paramLong)
  {
    throwIfParentInstance("retrieveNetworkLogs");
    try
    {
      paramComponentName = mService.retrieveNetworkLogs(paramComponentName, paramLong);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public List<SecurityLog.SecurityEvent> retrievePreRebootSecurityLogs(ComponentName paramComponentName)
  {
    throwIfParentInstance("retrievePreRebootSecurityLogs");
    try
    {
      paramComponentName = mService.retrievePreRebootSecurityLogs(paramComponentName);
      if (paramComponentName != null)
      {
        paramComponentName = paramComponentName.getList();
        return paramComponentName;
      }
      return null;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public List<SecurityLog.SecurityEvent> retrieveSecurityLogs(ComponentName paramComponentName)
  {
    throwIfParentInstance("retrieveSecurityLogs");
    try
    {
      paramComponentName = mService.retrieveSecurityLogs(paramComponentName);
      if (paramComponentName != null)
      {
        paramComponentName = paramComponentName.getList();
        return paramComponentName;
      }
      return null;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setAccountManagementDisabled(ComponentName paramComponentName, String paramString, boolean paramBoolean)
  {
    throwIfParentInstance("setAccountManagementDisabled");
    if (mService != null) {
      try
      {
        mService.setAccountManagementDisabled(paramComponentName, paramString, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setActiveAdmin(ComponentName paramComponentName, boolean paramBoolean)
  {
    setActiveAdmin(paramComponentName, paramBoolean, myUserId());
  }
  
  public void setActiveAdmin(ComponentName paramComponentName, boolean paramBoolean, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setActiveAdmin(paramComponentName, paramBoolean, paramInt);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setActivePasswordState(PasswordMetrics paramPasswordMetrics, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setActivePasswordState(paramPasswordMetrics, paramInt);
      }
      catch (RemoteException paramPasswordMetrics)
      {
        throw paramPasswordMetrics.rethrowFromSystemServer();
      }
    }
  }
  
  @SystemApi
  @Deprecated
  public boolean setActiveProfileOwner(ComponentName paramComponentName, @Deprecated String paramString)
    throws IllegalArgumentException
  {
    throwIfParentInstance("setActiveProfileOwner");
    if (mService != null) {
      try
      {
        int i = myUserId();
        mService.setActiveAdmin(paramComponentName, false, i);
        boolean bool = mService.setProfileOwner(paramComponentName, paramString, i);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void setAffiliationIds(ComponentName paramComponentName, Set<String> paramSet)
  {
    throwIfParentInstance("setAffiliationIds");
    if (paramSet != null) {
      try
      {
        IDevicePolicyManager localIDevicePolicyManager = mService;
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>(paramSet);
        localIDevicePolicyManager.setAffiliationIds(paramComponentName, localArrayList);
        return;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    throw new IllegalArgumentException("ids must not be null");
  }
  
  public void setAlwaysOnVpnPackage(ComponentName paramComponentName, String paramString, boolean paramBoolean)
    throws PackageManager.NameNotFoundException, UnsupportedOperationException
  {
    throwIfParentInstance("setAlwaysOnVpnPackage");
    if (mService != null) {
      try
      {
        if (!mService.setAlwaysOnVpnPackage(paramComponentName, paramString, paramBoolean))
        {
          paramComponentName = new android/content/pm/PackageManager$NameNotFoundException;
          paramComponentName.<init>(paramString);
          throw paramComponentName;
        }
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean setApplicationHidden(ComponentName paramComponentName, String paramString, boolean paramBoolean)
  {
    throwIfParentInstance("setApplicationHidden");
    if (mService != null) {
      try
      {
        paramBoolean = mService.setApplicationHidden(paramComponentName, mContext.getPackageName(), paramString, paramBoolean);
        return paramBoolean;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void setApplicationRestrictions(ComponentName paramComponentName, String paramString, Bundle paramBundle)
  {
    throwIfParentInstance("setApplicationRestrictions");
    if (mService != null) {
      try
      {
        mService.setApplicationRestrictions(paramComponentName, mContext.getPackageName(), paramString, paramBundle);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  @Deprecated
  public void setApplicationRestrictionsManagingPackage(ComponentName paramComponentName, String paramString)
    throws PackageManager.NameNotFoundException
  {
    throwIfParentInstance("setApplicationRestrictionsManagingPackage");
    if (mService != null) {
      try
      {
        if (!mService.setApplicationRestrictionsManagingPackage(paramComponentName, paramString))
        {
          paramComponentName = new android/content/pm/PackageManager$NameNotFoundException;
          paramComponentName.<init>(paramString);
          throw paramComponentName;
        }
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setAutoTimeRequired(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setAutoTimeRequired");
    if (mService != null) {
      try
      {
        mService.setAutoTimeRequired(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setBackupServiceEnabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setBackupServiceEnabled");
    try
    {
      mService.setBackupServiceEnabled(paramComponentName, paramBoolean);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setBluetoothContactSharingDisabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setBluetoothContactSharingDisabled");
    if (mService != null) {
      try
      {
        mService.setBluetoothContactSharingDisabled(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setCameraDisabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setCameraDisabled");
    if (mService != null) {
      try
      {
        mService.setCameraDisabled(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  @Deprecated
  public void setCertInstallerPackage(ComponentName paramComponentName, String paramString)
    throws SecurityException
  {
    throwIfParentInstance("setCertInstallerPackage");
    if (mService != null) {
      try
      {
        mService.setCertInstallerPackage(paramComponentName, paramString);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setCrossProfileCallerIdDisabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setCrossProfileCallerIdDisabled");
    if (mService != null) {
      try
      {
        mService.setCrossProfileCallerIdDisabled(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setCrossProfileContactsSearchDisabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setCrossProfileContactsSearchDisabled");
    if (mService != null) {
      try
      {
        mService.setCrossProfileContactsSearchDisabled(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setDefaultSmsApplication(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("setDefaultSmsApplication");
    if (mService != null) {
      try
      {
        mService.setDefaultSmsApplication(paramComponentName, paramString);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setDelegatedScopes(ComponentName paramComponentName, String paramString, List<String> paramList)
  {
    throwIfParentInstance("setDelegatedScopes");
    if (mService != null) {
      try
      {
        mService.setDelegatedScopes(paramComponentName, paramString, paramList);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean setDeviceOwner(ComponentName paramComponentName)
  {
    return setDeviceOwner(paramComponentName, null);
  }
  
  public boolean setDeviceOwner(ComponentName paramComponentName, int paramInt)
  {
    return setDeviceOwner(paramComponentName, null, paramInt);
  }
  
  public boolean setDeviceOwner(ComponentName paramComponentName, String paramString)
  {
    return setDeviceOwner(paramComponentName, paramString, 0);
  }
  
  public boolean setDeviceOwner(ComponentName paramComponentName, String paramString, int paramInt)
    throws IllegalArgumentException, IllegalStateException
  {
    if (mService != null) {
      try
      {
        boolean bool = mService.setDeviceOwner(paramComponentName, paramString, paramInt);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void setDeviceOwnerLockScreenInfo(ComponentName paramComponentName, CharSequence paramCharSequence)
  {
    throwIfParentInstance("setDeviceOwnerLockScreenInfo");
    if (mService != null) {
      try
      {
        mService.setDeviceOwnerLockScreenInfo(paramComponentName, paramCharSequence);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  @SystemApi
  public void setDeviceProvisioningConfigApplied()
  {
    try
    {
      mService.setDeviceProvisioningConfigApplied();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setEndUserSessionMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
  {
    throwIfParentInstance("setEndUserSessionMessage");
    try
    {
      mService.setEndUserSessionMessage(paramComponentName, paramCharSequence);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setForceEphemeralUsers(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setForceEphemeralUsers");
    if (mService != null) {
      try
      {
        mService.setForceEphemeralUsers(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public ComponentName setGlobalProxy(ComponentName paramComponentName, java.net.Proxy paramProxy, List<String> paramList)
  {
    throwIfParentInstance("setGlobalProxy");
    if (paramProxy != null)
    {
      if (mService != null) {
        try
        {
          if (paramProxy.equals(java.net.Proxy.NO_PROXY))
          {
            paramList = null;
            paramProxy = null;
          }
          else
          {
            if (!paramProxy.type().equals(Proxy.Type.HTTP)) {
              break label234;
            }
            paramProxy = (InetSocketAddress)paramProxy.address();
            String str1 = paramProxy.getHostName();
            int i = paramProxy.getPort();
            paramProxy = new java/lang/StringBuilder;
            paramProxy.<init>();
            paramProxy.append(str1);
            paramProxy.append(":");
            paramProxy.append(Integer.toString(i));
            String str2 = paramProxy.toString();
            if (paramList == null)
            {
              paramProxy = "";
            }
            else
            {
              paramProxy = new java/lang/StringBuilder;
              paramProxy.<init>();
              int j = 1;
              Iterator localIterator = paramList.iterator();
              while (localIterator.hasNext())
              {
                paramList = (String)localIterator.next();
                if (j == 0) {
                  paramProxy.append(",");
                } else {
                  j = 0;
                }
                paramProxy.append(paramList.trim());
              }
              paramProxy = paramProxy.toString();
            }
            if (android.net.Proxy.validate(str1, Integer.toString(i), paramProxy) != 0) {
              break label224;
            }
            paramList = str2;
          }
          return mService.setGlobalProxy(paramComponentName, paramList, paramProxy);
          label224:
          paramComponentName = new java/lang/IllegalArgumentException;
          paramComponentName.<init>();
          throw paramComponentName;
          label234:
          paramComponentName = new java/lang/IllegalArgumentException;
          paramComponentName.<init>();
          throw paramComponentName;
        }
        catch (RemoteException paramComponentName)
        {
          throw paramComponentName.rethrowFromSystemServer();
        }
      }
      return null;
    }
    throw new NullPointerException();
  }
  
  public void setGlobalSetting(ComponentName paramComponentName, String paramString1, String paramString2)
  {
    throwIfParentInstance("setGlobalSetting");
    if (mService != null) {
      try
      {
        mService.setGlobalSetting(paramComponentName, paramString1, paramString2);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setKeepUninstalledPackages(ComponentName paramComponentName, List<String> paramList)
  {
    throwIfParentInstance("setKeepUninstalledPackages");
    if (mService != null) {
      try
      {
        mService.setKeepUninstalledPackages(paramComponentName, mContext.getPackageName(), paramList);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean setKeyPairCertificate(ComponentName paramComponentName, String paramString, List<Certificate> paramList, boolean paramBoolean)
  {
    throwIfParentInstance("setKeyPairCertificate");
    try
    {
      byte[] arrayOfByte = Credentials.convertToPem(new Certificate[] { (Certificate)paramList.get(0) });
      if (paramList.size() > 1) {
        paramList = Credentials.convertToPem((Certificate[])paramList.subList(1, paramList.size()).toArray(new Certificate[0]));
      } else {
        paramList = null;
      }
      paramBoolean = mService.setKeyPairCertificate(paramComponentName, mContext.getPackageName(), paramString, arrayOfByte, paramList, paramBoolean);
      return paramBoolean;
    }
    catch (CertificateException|IOException paramComponentName)
    {
      Log.w(TAG, "Could not pem-encode certificate", paramComponentName);
      return false;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean setKeyguardDisabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setKeyguardDisabled");
    try
    {
      paramBoolean = mService.setKeyguardDisabled(paramComponentName, paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setKeyguardDisabledFeatures(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setKeyguardDisabledFeatures(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setLockTaskFeatures(ComponentName paramComponentName, int paramInt)
  {
    throwIfParentInstance("setLockTaskFeatures");
    if (mService != null) {
      try
      {
        mService.setLockTaskFeatures(paramComponentName, paramInt);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setLockTaskPackages(ComponentName paramComponentName, String[] paramArrayOfString)
    throws SecurityException
  {
    throwIfParentInstance("setLockTaskPackages");
    if (mService != null) {
      try
      {
        mService.setLockTaskPackages(paramComponentName, paramArrayOfString);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setLogoutEnabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setLogoutEnabled");
    try
    {
      mService.setLogoutEnabled(paramComponentName, paramBoolean);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setLongSupportMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
  {
    throwIfParentInstance("setLongSupportMessage");
    if (mService != null) {
      try
      {
        mService.setLongSupportMessage(paramComponentName, paramCharSequence);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean setMandatoryBackupTransport(ComponentName paramComponentName1, ComponentName paramComponentName2)
  {
    throwIfParentInstance("setMandatoryBackupTransport");
    try
    {
      boolean bool = mService.setMandatoryBackupTransport(paramComponentName1, paramComponentName2);
      return bool;
    }
    catch (RemoteException paramComponentName1)
    {
      throw paramComponentName1.rethrowFromSystemServer();
    }
  }
  
  public void setMasterVolumeMuted(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setMasterVolumeMuted");
    if (mService != null) {
      try
      {
        mService.setMasterVolumeMuted(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setMaximumFailedPasswordsForWipe(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setMaximumFailedPasswordsForWipe(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setMaximumTimeToLock(ComponentName paramComponentName, long paramLong)
  {
    if (mService != null) {
      try
      {
        mService.setMaximumTimeToLock(paramComponentName, paramLong, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public List<String> setMeteredDataDisabledPackages(ComponentName paramComponentName, List<String> paramList)
  {
    throwIfParentInstance("setMeteredDataDisabled");
    if (mService != null) {
      try
      {
        paramComponentName = mService.setMeteredDataDisabledPackages(paramComponentName, paramList);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return paramList;
  }
  
  public void setNetworkLoggingEnabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setNetworkLoggingEnabled");
    try
    {
      mService.setNetworkLoggingEnabled(paramComponentName, paramBoolean);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setOrganizationColor(ComponentName paramComponentName, int paramInt)
  {
    throwIfParentInstance("setOrganizationColor");
    try
    {
      mService.setOrganizationColor(paramComponentName, paramInt | 0xFF000000);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setOrganizationColorForUser(int paramInt1, int paramInt2)
  {
    try
    {
      mService.setOrganizationColorForUser(paramInt1 | 0xFF000000, paramInt2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setOrganizationName(ComponentName paramComponentName, CharSequence paramCharSequence)
  {
    throwIfParentInstance("setOrganizationName");
    try
    {
      mService.setOrganizationName(paramComponentName, paramCharSequence);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setOverrideApnsEnabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setOverrideApnEnabled");
    if (mService != null) {
      try
      {
        mService.setOverrideApnsEnabled(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public String[] setPackagesSuspended(ComponentName paramComponentName, String[] paramArrayOfString, boolean paramBoolean)
  {
    throwIfParentInstance("setPackagesSuspended");
    if (mService != null) {
      try
      {
        paramComponentName = mService.setPackagesSuspended(paramComponentName, mContext.getPackageName(), paramArrayOfString, paramBoolean);
        return paramComponentName;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return paramArrayOfString;
  }
  
  public void setPasswordExpirationTimeout(ComponentName paramComponentName, long paramLong)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordExpirationTimeout(paramComponentName, paramLong, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordHistoryLength(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordHistoryLength(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordMinimumLength(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordMinimumLength(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordMinimumLetters(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordMinimumLetters(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordMinimumLowerCase(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordMinimumLowerCase(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordMinimumNonLetter(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordMinimumNonLetter(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordMinimumNumeric(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordMinimumNumeric(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordMinimumSymbols(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordMinimumSymbols(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordMinimumUpperCase(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordMinimumUpperCase(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setPasswordQuality(ComponentName paramComponentName, int paramInt)
  {
    if (mService != null) {
      try
      {
        mService.setPasswordQuality(paramComponentName, paramInt, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean setPermissionGrantState(ComponentName paramComponentName, String paramString1, String paramString2, int paramInt)
  {
    throwIfParentInstance("setPermissionGrantState");
    try
    {
      boolean bool = mService.setPermissionGrantState(paramComponentName, mContext.getPackageName(), paramString1, paramString2, paramInt);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setPermissionPolicy(ComponentName paramComponentName, int paramInt)
  {
    throwIfParentInstance("setPermissionPolicy");
    try
    {
      mService.setPermissionPolicy(paramComponentName, mContext.getPackageName(), paramInt);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean setPermittedAccessibilityServices(ComponentName paramComponentName, List<String> paramList)
  {
    throwIfParentInstance("setPermittedAccessibilityServices");
    if (mService != null) {
      try
      {
        boolean bool = mService.setPermittedAccessibilityServices(paramComponentName, paramList);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean setPermittedCrossProfileNotificationListeners(ComponentName paramComponentName, List<String> paramList)
  {
    throwIfParentInstance("setPermittedCrossProfileNotificationListeners");
    if (mService != null) {
      try
      {
        boolean bool = mService.setPermittedCrossProfileNotificationListeners(paramComponentName, paramList);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean setPermittedInputMethods(ComponentName paramComponentName, List<String> paramList)
  {
    throwIfParentInstance("setPermittedInputMethods");
    if (mService != null) {
      try
      {
        boolean bool = mService.setPermittedInputMethods(paramComponentName, paramList);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void setProfileEnabled(ComponentName paramComponentName)
  {
    throwIfParentInstance("setProfileEnabled");
    if (mService != null) {
      try
      {
        mService.setProfileEnabled(paramComponentName);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setProfileName(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("setProfileName");
    if (mService != null) {
      try
      {
        mService.setProfileName(paramComponentName, paramString);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  /* Error */
  public boolean setProfileOwner(ComponentName paramComponentName, @Deprecated String paramString, int paramInt)
    throws IllegalArgumentException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 434	android/app/admin/DevicePolicyManager:mService	Landroid/app/admin/IDevicePolicyManager;
    //   4: ifnull +45 -> 49
    //   7: aload_2
    //   8: astore 4
    //   10: aload_2
    //   11: ifnonnull +15 -> 26
    //   14: ldc_w 1785
    //   17: astore 4
    //   19: goto +7 -> 26
    //   22: astore_1
    //   23: goto +21 -> 44
    //   26: aload_0
    //   27: getfield 434	android/app/admin/DevicePolicyManager:mService	Landroid/app/admin/IDevicePolicyManager;
    //   30: aload_1
    //   31: aload 4
    //   33: iload_3
    //   34: invokeinterface 1648 4 0
    //   39: istore 5
    //   41: iload 5
    //   43: ireturn
    //   44: aload_1
    //   45: invokevirtual 481	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   48: athrow
    //   49: iconst_0
    //   50: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	51	0	this	DevicePolicyManager
    //   0	51	1	paramComponentName	ComponentName
    //   0	51	2	paramString	String
    //   0	51	3	paramInt	int
    //   8	24	4	str	String
    //   39	3	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   26	41	22	android/os/RemoteException
  }
  
  public void setRecommendedGlobalProxy(ComponentName paramComponentName, ProxyInfo paramProxyInfo)
  {
    throwIfParentInstance("setRecommendedGlobalProxy");
    if (mService != null) {
      try
      {
        mService.setRecommendedGlobalProxy(paramComponentName, paramProxyInfo);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setRequiredStrongAuthTimeout(ComponentName paramComponentName, long paramLong)
  {
    if (mService != null) {
      try
      {
        mService.setRequiredStrongAuthTimeout(paramComponentName, paramLong, mParentInstance);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean setResetPasswordToken(ComponentName paramComponentName, byte[] paramArrayOfByte)
  {
    throwIfParentInstance("setResetPasswordToken");
    if (mService != null) {
      try
      {
        boolean bool = mService.setResetPasswordToken(paramComponentName, paramArrayOfByte);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void setRestrictionsProvider(ComponentName paramComponentName1, ComponentName paramComponentName2)
  {
    throwIfParentInstance("setRestrictionsProvider");
    if (mService != null) {
      try
      {
        mService.setRestrictionsProvider(paramComponentName1, paramComponentName2);
      }
      catch (RemoteException paramComponentName1)
      {
        throw paramComponentName1.rethrowFromSystemServer();
      }
    }
  }
  
  public void setScreenCaptureDisabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setScreenCaptureDisabled");
    if (mService != null) {
      try
      {
        mService.setScreenCaptureDisabled(paramComponentName, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setSecureSetting(ComponentName paramComponentName, String paramString1, String paramString2)
  {
    throwIfParentInstance("setSecureSetting");
    if (mService != null) {
      try
      {
        mService.setSecureSetting(paramComponentName, paramString1, paramString2);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setSecurityLoggingEnabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setSecurityLoggingEnabled");
    try
    {
      mService.setSecurityLoggingEnabled(paramComponentName, paramBoolean);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setShortSupportMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
  {
    throwIfParentInstance("setShortSupportMessage");
    if (mService != null) {
      try
      {
        mService.setShortSupportMessage(paramComponentName, paramCharSequence);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setStartUserSessionMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
  {
    throwIfParentInstance("setStartUserSessionMessage");
    try
    {
      mService.setStartUserSessionMessage(paramComponentName, paramCharSequence);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean setStatusBarDisabled(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setStatusBarDisabled");
    try
    {
      paramBoolean = mService.setStatusBarDisabled(paramComponentName, paramBoolean);
      return paramBoolean;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public int setStorageEncryption(ComponentName paramComponentName, boolean paramBoolean)
  {
    throwIfParentInstance("setStorageEncryption");
    if (mService != null) {
      try
      {
        int i = mService.setStorageEncryption(paramComponentName, paramBoolean);
        return i;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public void setSystemSetting(ComponentName paramComponentName, String paramString1, String paramString2)
  {
    throwIfParentInstance("setSystemSetting");
    if (mService != null) {
      try
      {
        mService.setSystemSetting(paramComponentName, paramString1, paramString2);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setSystemUpdatePolicy(ComponentName paramComponentName, SystemUpdatePolicy paramSystemUpdatePolicy)
  {
    throwIfParentInstance("setSystemUpdatePolicy");
    if (mService != null) {
      try
      {
        mService.setSystemUpdatePolicy(paramComponentName, paramSystemUpdatePolicy);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public boolean setTime(ComponentName paramComponentName, long paramLong)
  {
    throwIfParentInstance("setTime");
    if (mService != null) {
      try
      {
        boolean bool = mService.setTime(paramComponentName, paramLong);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public boolean setTimeZone(ComponentName paramComponentName, String paramString)
  {
    throwIfParentInstance("setTimeZone");
    if (mService != null) {
      try
      {
        boolean bool = mService.setTimeZone(paramComponentName, paramString);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void setTrustAgentConfiguration(ComponentName paramComponentName1, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle)
  {
    if (mService != null) {
      try
      {
        mService.setTrustAgentConfiguration(paramComponentName1, paramComponentName2, paramPersistableBundle, mParentInstance);
      }
      catch (RemoteException paramComponentName1)
      {
        throw paramComponentName1.rethrowFromSystemServer();
      }
    }
  }
  
  public void setUninstallBlocked(ComponentName paramComponentName, String paramString, boolean paramBoolean)
  {
    throwIfParentInstance("setUninstallBlocked");
    if (mService != null) {
      try
      {
        mService.setUninstallBlocked(paramComponentName, mContext.getPackageName(), paramString, paramBoolean);
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void setUserIcon(ComponentName paramComponentName, Bitmap paramBitmap)
  {
    throwIfParentInstance("setUserIcon");
    try
    {
      mService.setUserIcon(paramComponentName, paramBitmap);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void setUserProvisioningState(int paramInt1, int paramInt2)
  {
    if (mService != null) {
      try
      {
        mService.setUserProvisioningState(paramInt1, paramInt2);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public void startManagedQuickContact(String paramString, long paramLong, Intent paramIntent)
  {
    startManagedQuickContact(paramString, paramLong, false, 0L, paramIntent);
  }
  
  public void startManagedQuickContact(String paramString, long paramLong1, boolean paramBoolean, long paramLong2, Intent paramIntent)
  {
    if (mService != null) {
      try
      {
        mService.startManagedQuickContact(paramString, paramLong1, paramBoolean, paramLong2, paramIntent);
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
  }
  
  public int startUserInBackground(ComponentName paramComponentName, UserHandle paramUserHandle)
  {
    throwIfParentInstance("startUserInBackground");
    try
    {
      int i = mService.startUserInBackground(paramComponentName, paramUserHandle);
      return i;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public int stopUser(ComponentName paramComponentName, UserHandle paramUserHandle)
  {
    throwIfParentInstance("stopUser");
    try
    {
      int i = mService.stopUser(paramComponentName, paramUserHandle);
      return i;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean switchUser(ComponentName paramComponentName, UserHandle paramUserHandle)
  {
    throwIfParentInstance("switchUser");
    try
    {
      boolean bool = mService.switchUser(paramComponentName, paramUserHandle);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void transferOwnership(ComponentName paramComponentName1, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle)
  {
    throwIfParentInstance("transferOwnership");
    try
    {
      mService.transferOwnership(paramComponentName1, paramComponentName2, paramPersistableBundle);
      return;
    }
    catch (RemoteException paramComponentName1)
    {
      throw paramComponentName1.rethrowFromSystemServer();
    }
  }
  
  public void uninstallAllUserCaCerts(ComponentName paramComponentName)
  {
    throwIfParentInstance("uninstallAllUserCaCerts");
    if (mService != null) {
      try
      {
        IDevicePolicyManager localIDevicePolicyManager = mService;
        String str = mContext.getPackageName();
        TrustedCertificateStore localTrustedCertificateStore = new com/android/org/conscrypt/TrustedCertificateStore;
        localTrustedCertificateStore.<init>();
        localIDevicePolicyManager.uninstallCaCerts(paramComponentName, str, (String[])localTrustedCertificateStore.userAliases().toArray(new String[0]));
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
  }
  
  public void uninstallCaCert(ComponentName paramComponentName, byte[] paramArrayOfByte)
  {
    throwIfParentInstance("uninstallCaCert");
    if (mService != null) {
      try
      {
        paramArrayOfByte = getCaCertAlias(paramArrayOfByte);
        mService.uninstallCaCerts(paramComponentName, mContext.getPackageName(), new String[] { paramArrayOfByte });
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
      catch (CertificateException paramComponentName)
      {
        Log.w(TAG, "Unable to parse certificate", paramComponentName);
      }
    }
  }
  
  public void uninstallPackageWithActiveAdmins(String paramString)
  {
    try
    {
      mService.uninstallPackageWithActiveAdmins(paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean updateOverrideApn(ComponentName paramComponentName, int paramInt, ApnSetting paramApnSetting)
  {
    throwIfParentInstance("updateOverrideApn");
    if (mService != null) {
      try
      {
        boolean bool = mService.updateOverrideApn(paramComponentName, paramInt, paramApnSetting);
        return bool;
      }
      catch (RemoteException paramComponentName)
      {
        throw paramComponentName.rethrowFromSystemServer();
      }
    }
    return false;
  }
  
  public void wipeData(int paramInt)
  {
    throwIfParentInstance("wipeData");
    wipeDataInternal(paramInt, mContext.getString(17041306));
  }
  
  public void wipeData(int paramInt, CharSequence paramCharSequence)
  {
    throwIfParentInstance("wipeData");
    Preconditions.checkNotNull(paramCharSequence, "CharSequence is null");
    wipeDataInternal(paramInt, paramCharSequence.toString());
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AttestationIdType {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CreateAndManageUserFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LockNowFlag {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LockTaskFeature {}
  
  public static abstract interface OnClearApplicationUserDataListener
  {
    public abstract void onApplicationUserDataCleared(String paramString, boolean paramBoolean);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ProvisioningPreCondition {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SystemSettingsWhitelist {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UserProvisioningState {}
}
