package android.app.admin;

import android.app.IApplicationThread;
import android.app.IApplicationThread.Stub;
import android.app.IServiceConnection;
import android.app.IServiceConnection.Stub;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageDataObserver.Stub;
import android.content.pm.ParceledListSlice;
import android.content.pm.StringParceledListSlice;
import android.graphics.Bitmap;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserHandle;
import android.security.keymaster.KeymasterCertificateChain;
import android.security.keystore.ParcelableKeyGenParameterSpec;
import android.telephony.data.ApnSetting;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public abstract interface IDevicePolicyManager
  extends IInterface
{
  public abstract void addCrossProfileIntentFilter(ComponentName paramComponentName, IntentFilter paramIntentFilter, int paramInt)
    throws RemoteException;
  
  public abstract boolean addCrossProfileWidgetProvider(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract int addOverrideApn(ComponentName paramComponentName, ApnSetting paramApnSetting)
    throws RemoteException;
  
  public abstract void addPersistentPreferredActivity(ComponentName paramComponentName1, IntentFilter paramIntentFilter, ComponentName paramComponentName2)
    throws RemoteException;
  
  public abstract boolean approveCaCert(String paramString, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean bindDeviceAdminServiceAsUser(ComponentName paramComponentName, IApplicationThread paramIApplicationThread, IBinder paramIBinder, Intent paramIntent, IServiceConnection paramIServiceConnection, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int checkProvisioningPreCondition(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void choosePrivateKeyAlias(int paramInt, Uri paramUri, String paramString, IBinder paramIBinder)
    throws RemoteException;
  
  public abstract void clearApplicationUserData(ComponentName paramComponentName, String paramString, IPackageDataObserver paramIPackageDataObserver)
    throws RemoteException;
  
  public abstract void clearCrossProfileIntentFilters(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void clearDeviceOwner(String paramString)
    throws RemoteException;
  
  public abstract void clearPackagePersistentPreferredActivities(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract void clearProfileOwner(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean clearResetPasswordToken(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void clearSystemUpdatePolicyFreezePeriodRecord()
    throws RemoteException;
  
  public abstract Intent createAdminSupportIntent(String paramString)
    throws RemoteException;
  
  public abstract UserHandle createAndManageUser(ComponentName paramComponentName1, String paramString, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle, int paramInt)
    throws RemoteException;
  
  public abstract void enableSystemApp(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract int enableSystemAppWithIntent(ComponentName paramComponentName, String paramString, Intent paramIntent)
    throws RemoteException;
  
  public abstract void enforceCanManageCaCerts(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract void forceRemoveActiveAdmin(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract long forceSecurityLogs()
    throws RemoteException;
  
  public abstract void forceUpdateUserSetupComplete()
    throws RemoteException;
  
  public abstract boolean generateKeyPair(ComponentName paramComponentName, String paramString1, String paramString2, ParcelableKeyGenParameterSpec paramParcelableKeyGenParameterSpec, int paramInt, KeymasterCertificateChain paramKeymasterCertificateChain)
    throws RemoteException;
  
  public abstract String[] getAccountTypesWithManagementDisabled()
    throws RemoteException;
  
  public abstract String[] getAccountTypesWithManagementDisabledAsUser(int paramInt)
    throws RemoteException;
  
  public abstract List<ComponentName> getActiveAdmins(int paramInt)
    throws RemoteException;
  
  public abstract List<String> getAffiliationIds(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract String getAlwaysOnVpnPackage(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract Bundle getApplicationRestrictions(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract String getApplicationRestrictionsManagingPackage(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean getAutoTimeRequired()
    throws RemoteException;
  
  public abstract List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean getBluetoothContactSharingDisabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean getBluetoothContactSharingDisabledForUser(int paramInt)
    throws RemoteException;
  
  public abstract boolean getCameraDisabled(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract String getCertInstallerPackage(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean getCrossProfileCallerIdDisabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean getCrossProfileCallerIdDisabledForUser(int paramInt)
    throws RemoteException;
  
  public abstract boolean getCrossProfileContactsSearchDisabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean getCrossProfileContactsSearchDisabledForUser(int paramInt)
    throws RemoteException;
  
  public abstract List<String> getCrossProfileWidgetProviders(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract int getCurrentFailedPasswordAttempts(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getCurrentFailedPasswordAttemptsForAsus(int paramInt)
    throws RemoteException;
  
  public abstract List<String> getDelegatePackages(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract List<String> getDelegatedScopes(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract ComponentName getDeviceOwnerComponent(boolean paramBoolean)
    throws RemoteException;
  
  public abstract CharSequence getDeviceOwnerLockScreenInfo()
    throws RemoteException;
  
  public abstract String getDeviceOwnerName()
    throws RemoteException;
  
  public abstract CharSequence getDeviceOwnerOrganizationName()
    throws RemoteException;
  
  public abstract int getDeviceOwnerUserId()
    throws RemoteException;
  
  public abstract List<String> getDisallowedSystemApps(ComponentName paramComponentName, int paramInt, String paramString)
    throws RemoteException;
  
  public abstract boolean getDoNotAskCredentialsOnBoot()
    throws RemoteException;
  
  public abstract CharSequence getEndUserSessionMessage(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean getForceEphemeralUsers(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract ComponentName getGlobalProxyAdmin(int paramInt)
    throws RemoteException;
  
  public abstract List<String> getKeepUninstalledPackages(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract int getKeyguardDisabledFeatures(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract long getLastBugReportRequestTime()
    throws RemoteException;
  
  public abstract long getLastNetworkLogRetrievalTime()
    throws RemoteException;
  
  public abstract long getLastSecurityLogRetrievalTime()
    throws RemoteException;
  
  public abstract int getLockTaskFeatures(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract String[] getLockTaskPackages(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract CharSequence getLongSupportMessage(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract CharSequence getLongSupportMessageForUser(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract ComponentName getMandatoryBackupTransport()
    throws RemoteException;
  
  public abstract int getMaximumFailedPasswordsForWipe(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract long getMaximumTimeToLock(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract List<String> getMeteredDataDisabledPackages(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract int getOrganizationColor(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract int getOrganizationColorForUser(int paramInt)
    throws RemoteException;
  
  public abstract CharSequence getOrganizationName(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract CharSequence getOrganizationNameForUser(int paramInt)
    throws RemoteException;
  
  public abstract List<ApnSetting> getOverrideApns(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract StringParceledListSlice getOwnerInstalledCaCerts(UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract long getPasswordExpiration(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract long getPasswordExpirationTimeout(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordHistoryLength(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordLengthDirectly(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract int getPasswordMinimumLength(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordMinimumLetters(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordMinimumLowerCase(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordMinimumNonLetter(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordMinimumNumeric(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordMinimumSymbols(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordMinimumUpperCase(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getPasswordQuality(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract SystemUpdateInfo getPendingSystemUpdate(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract int getPermissionGrantState(ComponentName paramComponentName, String paramString1, String paramString2, String paramString3)
    throws RemoteException;
  
  public abstract int getPermissionPolicy(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract List getPermittedAccessibilityServices(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract List getPermittedAccessibilityServicesForUser(int paramInt)
    throws RemoteException;
  
  public abstract List<String> getPermittedCrossProfileNotificationListeners(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract List getPermittedInputMethods(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract List getPermittedInputMethodsForCurrentUser()
    throws RemoteException;
  
  public abstract ComponentName getProfileOwner(int paramInt)
    throws RemoteException;
  
  public abstract String getProfileOwnerName(int paramInt)
    throws RemoteException;
  
  public abstract int getProfileWithMinimumFailedPasswordsForWipe(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void getRemoveWarning(ComponentName paramComponentName, RemoteCallback paramRemoteCallback, int paramInt)
    throws RemoteException;
  
  public abstract long getRequiredStrongAuthTimeout(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ComponentName getRestrictionsProvider(int paramInt)
    throws RemoteException;
  
  public abstract boolean getScreenCaptureDisabled(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract List<UserHandle> getSecondaryUsers(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract CharSequence getShortSupportMessage(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract CharSequence getShortSupportMessageForUser(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract CharSequence getStartUserSessionMessage(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean getStorageEncryption(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract int getStorageEncryptionStatus(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract SystemUpdatePolicy getSystemUpdatePolicy()
    throws RemoteException;
  
  public abstract PersistableBundle getTransferOwnershipBundle()
    throws RemoteException;
  
  public abstract List<PersistableBundle> getTrustAgentConfiguration(ComponentName paramComponentName1, ComponentName paramComponentName2, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int getUserProvisioningState()
    throws RemoteException;
  
  public abstract Bundle getUserRestrictions(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract String getWifiMacAddress(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean hasDeviceOwner()
    throws RemoteException;
  
  public abstract boolean hasGrantedPolicy(ComponentName paramComponentName, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean hasUserSetupCompleted()
    throws RemoteException;
  
  public abstract boolean installCaCert(ComponentName paramComponentName, String paramString, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract boolean installExistingPackage(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean installKeyPair(ComponentName paramComponentName, String paramString1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;
  
  public abstract boolean isAccessibilityServicePermittedByAdmin(ComponentName paramComponentName, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isActivePasswordSufficient(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean isAdminActive(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean isAffiliatedUser()
    throws RemoteException;
  
  public abstract boolean isApplicationHidden(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean isBackupServiceEnabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isCaCertApproved(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isCallerApplicationRestrictionsManagingPackage(String paramString)
    throws RemoteException;
  
  public abstract boolean isCurrentInputMethodSetByOwner()
    throws RemoteException;
  
  public abstract boolean isDeviceProvisioned()
    throws RemoteException;
  
  public abstract boolean isDeviceProvisioningConfigApplied()
    throws RemoteException;
  
  public abstract boolean isEphemeralUser(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isInputMethodPermittedByAdmin(ComponentName paramComponentName, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isLockTaskPermitted(String paramString)
    throws RemoteException;
  
  public abstract boolean isLogoutEnabled()
    throws RemoteException;
  
  public abstract boolean isManagedProfile(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isMasterVolumeMuted(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isMeteredDataDisabledPackageForUser(ComponentName paramComponentName, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isNetworkLoggingEnabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isNotificationListenerServicePermitted(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isOverrideApnEnabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isPackageSuspended(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean isProfileActivePasswordSufficientForParent(int paramInt)
    throws RemoteException;
  
  public abstract boolean isProvisioningAllowed(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean isRemovingAdmin(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean isResetPasswordTokenActive(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isSecurityLoggingEnabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isSeparateProfileChallengeAllowed(int paramInt)
    throws RemoteException;
  
  public abstract boolean isSystemOnlyUser(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean isUninstallBlocked(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract boolean isUninstallInQueue(String paramString)
    throws RemoteException;
  
  public abstract boolean isUsingUnifiedPassword(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void lockNow(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int logoutUser(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void notifyLockTaskModeChanged(boolean paramBoolean, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void notifyPendingSystemUpdate(SystemUpdateInfo paramSystemUpdateInfo)
    throws RemoteException;
  
  public abstract boolean packageHasActiveAdmins(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void reboot(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void removeActiveAdmin(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean removeCrossProfileWidgetProvider(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract boolean removeKeyPair(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean removeOverrideApn(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean removeUser(ComponentName paramComponentName, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void reportFailedFingerprintAttempt(int paramInt)
    throws RemoteException;
  
  public abstract void reportFailedPasswordAttempt(int paramInt)
    throws RemoteException;
  
  public abstract void reportKeyguardDismissed(int paramInt)
    throws RemoteException;
  
  public abstract void reportKeyguardSecured(int paramInt)
    throws RemoteException;
  
  public abstract void reportPasswordChanged(int paramInt)
    throws RemoteException;
  
  public abstract void reportSuccessfulFingerprintAttempt(int paramInt)
    throws RemoteException;
  
  public abstract void reportSuccessfulPasswordAttempt(int paramInt)
    throws RemoteException;
  
  public abstract boolean requestBugreport(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract boolean resetPassword(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean resetPasswordWithToken(ComponentName paramComponentName, String paramString, byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract List<NetworkEvent> retrieveNetworkLogs(ComponentName paramComponentName, long paramLong)
    throws RemoteException;
  
  public abstract ParceledListSlice retrievePreRebootSecurityLogs(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract ParceledListSlice retrieveSecurityLogs(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void setAccountManagementDisabled(ComponentName paramComponentName, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setActiveAdmin(ComponentName paramComponentName, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setActivePasswordState(PasswordMetrics paramPasswordMetrics, int paramInt)
    throws RemoteException;
  
  public abstract void setAffiliationIds(ComponentName paramComponentName, List<String> paramList)
    throws RemoteException;
  
  public abstract boolean setAlwaysOnVpnPackage(ComponentName paramComponentName, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setApplicationHidden(ComponentName paramComponentName, String paramString1, String paramString2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setApplicationRestrictions(ComponentName paramComponentName, String paramString1, String paramString2, Bundle paramBundle)
    throws RemoteException;
  
  public abstract boolean setApplicationRestrictionsManagingPackage(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract void setAutoTimeRequired(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setBackupServiceEnabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setBluetoothContactSharingDisabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setCameraDisabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setCertInstallerPackage(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract void setCrossProfileCallerIdDisabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setCrossProfileContactsSearchDisabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setDefaultSmsApplication(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract void setDelegatedScopes(ComponentName paramComponentName, String paramString, List<String> paramList)
    throws RemoteException;
  
  public abstract boolean setDeviceOwner(ComponentName paramComponentName, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setDeviceOwnerLockScreenInfo(ComponentName paramComponentName, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void setDeviceProvisioningConfigApplied()
    throws RemoteException;
  
  public abstract void setEndUserSessionMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void setForceEphemeralUsers(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract ComponentName setGlobalProxy(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setGlobalSetting(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setKeepUninstalledPackages(ComponentName paramComponentName, String paramString, List<String> paramList)
    throws RemoteException;
  
  public abstract boolean setKeyPairCertificate(ComponentName paramComponentName, String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setKeyguardDisabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setKeyguardDisabledFeatures(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setLockTaskFeatures(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract void setLockTaskPackages(ComponentName paramComponentName, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void setLogoutEnabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setLongSupportMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract boolean setMandatoryBackupTransport(ComponentName paramComponentName1, ComponentName paramComponentName2)
    throws RemoteException;
  
  public abstract void setMasterVolumeMuted(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setMaximumFailedPasswordsForWipe(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setMaximumTimeToLock(ComponentName paramComponentName, long paramLong, boolean paramBoolean)
    throws RemoteException;
  
  public abstract List<String> setMeteredDataDisabledPackages(ComponentName paramComponentName, List<String> paramList)
    throws RemoteException;
  
  public abstract void setNetworkLoggingEnabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setOrganizationColor(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract void setOrganizationColorForUser(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setOrganizationName(ComponentName paramComponentName, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void setOverrideApnsEnabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract String[] setPackagesSuspended(ComponentName paramComponentName, String paramString, String[] paramArrayOfString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordExpirationTimeout(ComponentName paramComponentName, long paramLong, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordHistoryLength(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordMinimumLength(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordMinimumLetters(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordMinimumLowerCase(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordMinimumNonLetter(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordMinimumNumeric(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordMinimumSymbols(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordMinimumUpperCase(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setPasswordQuality(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setPermissionGrantState(ComponentName paramComponentName, String paramString1, String paramString2, String paramString3, int paramInt)
    throws RemoteException;
  
  public abstract void setPermissionPolicy(ComponentName paramComponentName, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean setPermittedAccessibilityServices(ComponentName paramComponentName, List paramList)
    throws RemoteException;
  
  public abstract boolean setPermittedCrossProfileNotificationListeners(ComponentName paramComponentName, List<String> paramList)
    throws RemoteException;
  
  public abstract boolean setPermittedInputMethods(ComponentName paramComponentName, List paramList)
    throws RemoteException;
  
  public abstract void setProfileEnabled(ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void setProfileName(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract boolean setProfileOwner(ComponentName paramComponentName, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setRecommendedGlobalProxy(ComponentName paramComponentName, ProxyInfo paramProxyInfo)
    throws RemoteException;
  
  public abstract void setRequiredStrongAuthTimeout(ComponentName paramComponentName, long paramLong, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setResetPasswordToken(ComponentName paramComponentName, byte[] paramArrayOfByte)
    throws RemoteException;
  
  public abstract void setRestrictionsProvider(ComponentName paramComponentName1, ComponentName paramComponentName2)
    throws RemoteException;
  
  public abstract void setScreenCaptureDisabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSecureSetting(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setSecurityLoggingEnabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setShortSupportMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract void setStartUserSessionMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
    throws RemoteException;
  
  public abstract boolean setStatusBarDisabled(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract int setStorageEncryption(ComponentName paramComponentName, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setSystemSetting(ComponentName paramComponentName, String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract void setSystemUpdatePolicy(ComponentName paramComponentName, SystemUpdatePolicy paramSystemUpdatePolicy)
    throws RemoteException;
  
  public abstract boolean setTime(ComponentName paramComponentName, long paramLong)
    throws RemoteException;
  
  public abstract boolean setTimeZone(ComponentName paramComponentName, String paramString)
    throws RemoteException;
  
  public abstract void setTrustAgentConfiguration(ComponentName paramComponentName1, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUninstallBlocked(ComponentName paramComponentName, String paramString1, String paramString2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUserIcon(ComponentName paramComponentName, Bitmap paramBitmap)
    throws RemoteException;
  
  public abstract void setUserProvisioningState(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void setUserRestriction(ComponentName paramComponentName, String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void startManagedQuickContact(String paramString, long paramLong1, boolean paramBoolean, long paramLong2, Intent paramIntent)
    throws RemoteException;
  
  public abstract int startUserInBackground(ComponentName paramComponentName, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract int stopUser(ComponentName paramComponentName, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract boolean switchUser(ComponentName paramComponentName, UserHandle paramUserHandle)
    throws RemoteException;
  
  public abstract void transferOwnership(ComponentName paramComponentName1, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle)
    throws RemoteException;
  
  public abstract void uninstallCaCerts(ComponentName paramComponentName, String paramString, String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void uninstallPackageWithActiveAdmins(String paramString)
    throws RemoteException;
  
  public abstract boolean updateOverrideApn(ComponentName paramComponentName, int paramInt, ApnSetting paramApnSetting)
    throws RemoteException;
  
  public abstract void wipeDataWithReason(int paramInt, String paramString)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDevicePolicyManager
  {
    private static final String DESCRIPTOR = "android.app.admin.IDevicePolicyManager";
    static final int TRANSACTION_addCrossProfileIntentFilter = 111;
    static final int TRANSACTION_addCrossProfileWidgetProvider = 167;
    static final int TRANSACTION_addOverrideApn = 258;
    static final int TRANSACTION_addPersistentPreferredActivity = 99;
    static final int TRANSACTION_approveCaCert = 85;
    static final int TRANSACTION_bindDeviceAdminServiceAsUser = 234;
    static final int TRANSACTION_checkProvisioningPreCondition = 189;
    static final int TRANSACTION_choosePrivateKeyAlias = 91;
    static final int TRANSACTION_clearApplicationUserData = 246;
    static final int TRANSACTION_clearCrossProfileIntentFilters = 112;
    static final int TRANSACTION_clearDeviceOwner = 69;
    static final int TRANSACTION_clearPackagePersistentPreferredActivities = 100;
    static final int TRANSACTION_clearProfileOwner = 76;
    static final int TRANSACTION_clearResetPasswordToken = 241;
    static final int TRANSACTION_clearSystemUpdatePolicyFreezePeriodRecord = 178;
    static final int TRANSACTION_createAdminSupportIntent = 124;
    static final int TRANSACTION_createAndManageUser = 127;
    static final int TRANSACTION_enableSystemApp = 134;
    static final int TRANSACTION_enableSystemAppWithIntent = 135;
    static final int TRANSACTION_enforceCanManageCaCerts = 84;
    static final int TRANSACTION_forceRemoveActiveAdmin = 55;
    static final int TRANSACTION_forceSecurityLogs = 220;
    static final int TRANSACTION_forceUpdateUserSetupComplete = 226;
    static final int TRANSACTION_generateKeyPair = 89;
    static final int TRANSACTION_getAccountTypesWithManagementDisabled = 138;
    static final int TRANSACTION_getAccountTypesWithManagementDisabledAsUser = 139;
    static final int TRANSACTION_getActiveAdmins = 51;
    static final int TRANSACTION_getAffiliationIds = 214;
    static final int TRANSACTION_getAlwaysOnVpnPackage = 98;
    static final int TRANSACTION_getApplicationRestrictions = 103;
    static final int TRANSACTION_getApplicationRestrictionsManagingPackage = 105;
    static final int TRANSACTION_getAutoTimeRequired = 171;
    static final int TRANSACTION_getBindDeviceAdminTargetUsers = 235;
    static final int TRANSACTION_getBluetoothContactSharingDisabled = 163;
    static final int TRANSACTION_getBluetoothContactSharingDisabledForUser = 164;
    static final int TRANSACTION_getCameraDisabled = 44;
    static final int TRANSACTION_getCertInstallerPackage = 96;
    static final int TRANSACTION_getCrossProfileCallerIdDisabled = 156;
    static final int TRANSACTION_getCrossProfileCallerIdDisabledForUser = 157;
    static final int TRANSACTION_getCrossProfileContactsSearchDisabled = 159;
    static final int TRANSACTION_getCrossProfileContactsSearchDisabledForUser = 160;
    static final int TRANSACTION_getCrossProfileWidgetProviders = 169;
    static final int TRANSACTION_getCurrentFailedPasswordAttempts = 25;
    static final int TRANSACTION_getCurrentFailedPasswordAttemptsForAsus = 266;
    static final int TRANSACTION_getDelegatePackages = 94;
    static final int TRANSACTION_getDelegatedScopes = 93;
    static final int TRANSACTION_getDeviceOwnerComponent = 66;
    static final int TRANSACTION_getDeviceOwnerLockScreenInfo = 79;
    static final int TRANSACTION_getDeviceOwnerName = 68;
    static final int TRANSACTION_getDeviceOwnerOrganizationName = 209;
    static final int TRANSACTION_getDeviceOwnerUserId = 70;
    static final int TRANSACTION_getDisallowedSystemApps = 249;
    static final int TRANSACTION_getDoNotAskCredentialsOnBoot = 181;
    static final int TRANSACTION_getEndUserSessionMessage = 255;
    static final int TRANSACTION_getForceEphemeralUsers = 173;
    static final int TRANSACTION_getGlobalProxyAdmin = 37;
    static final int TRANSACTION_getKeepUninstalledPackages = 191;
    static final int TRANSACTION_getKeyguardDisabledFeatures = 48;
    static final int TRANSACTION_getLastBugReportRequestTime = 238;
    static final int TRANSACTION_getLastNetworkLogRetrievalTime = 239;
    static final int TRANSACTION_getLastSecurityLogRetrievalTime = 237;
    static final int TRANSACTION_getLockTaskFeatures = 144;
    static final int TRANSACTION_getLockTaskPackages = 141;
    static final int TRANSACTION_getLongSupportMessage = 199;
    static final int TRANSACTION_getLongSupportMessageForUser = 201;
    static final int TRANSACTION_getMandatoryBackupTransport = 230;
    static final int TRANSACTION_getMaximumFailedPasswordsForWipe = 28;
    static final int TRANSACTION_getMaximumTimeToLock = 31;
    static final int TRANSACTION_getMeteredDataDisabledPackages = 257;
    static final int TRANSACTION_getOrganizationColor = 205;
    static final int TRANSACTION_getOrganizationColorForUser = 206;
    static final int TRANSACTION_getOrganizationName = 208;
    static final int TRANSACTION_getOrganizationNameForUser = 210;
    static final int TRANSACTION_getOverrideApns = 261;
    static final int TRANSACTION_getOwnerInstalledCaCerts = 245;
    static final int TRANSACTION_getPasswordExpiration = 21;
    static final int TRANSACTION_getPasswordExpirationTimeout = 20;
    static final int TRANSACTION_getPasswordHistoryLength = 18;
    static final int TRANSACTION_getPasswordLengthDirectly = 265;
    static final int TRANSACTION_getPasswordMinimumLength = 4;
    static final int TRANSACTION_getPasswordMinimumLetters = 10;
    static final int TRANSACTION_getPasswordMinimumLowerCase = 8;
    static final int TRANSACTION_getPasswordMinimumNonLetter = 16;
    static final int TRANSACTION_getPasswordMinimumNumeric = 12;
    static final int TRANSACTION_getPasswordMinimumSymbols = 14;
    static final int TRANSACTION_getPasswordMinimumUpperCase = 6;
    static final int TRANSACTION_getPasswordQuality = 2;
    static final int TRANSACTION_getPendingSystemUpdate = 183;
    static final int TRANSACTION_getPermissionGrantState = 187;
    static final int TRANSACTION_getPermissionPolicy = 185;
    static final int TRANSACTION_getPermittedAccessibilityServices = 114;
    static final int TRANSACTION_getPermittedAccessibilityServicesForUser = 115;
    static final int TRANSACTION_getPermittedCrossProfileNotificationListeners = 122;
    static final int TRANSACTION_getPermittedInputMethods = 118;
    static final int TRANSACTION_getPermittedInputMethodsForCurrentUser = 119;
    static final int TRANSACTION_getProfileOwner = 72;
    static final int TRANSACTION_getProfileOwnerName = 73;
    static final int TRANSACTION_getProfileWithMinimumFailedPasswordsForWipe = 26;
    static final int TRANSACTION_getRemoveWarning = 53;
    static final int TRANSACTION_getRequiredStrongAuthTimeout = 33;
    static final int TRANSACTION_getRestrictionsProvider = 108;
    static final int TRANSACTION_getScreenCaptureDisabled = 46;
    static final int TRANSACTION_getSecondaryUsers = 133;
    static final int TRANSACTION_getShortSupportMessage = 197;
    static final int TRANSACTION_getShortSupportMessageForUser = 200;
    static final int TRANSACTION_getStartUserSessionMessage = 254;
    static final int TRANSACTION_getStorageEncryption = 40;
    static final int TRANSACTION_getStorageEncryptionStatus = 41;
    static final int TRANSACTION_getSystemUpdatePolicy = 177;
    static final int TRANSACTION_getTransferOwnershipBundle = 251;
    static final int TRANSACTION_getTrustAgentConfiguration = 166;
    static final int TRANSACTION_getUserProvisioningState = 211;
    static final int TRANSACTION_getUserRestrictions = 110;
    static final int TRANSACTION_getWifiMacAddress = 194;
    static final int TRANSACTION_hasDeviceOwner = 67;
    static final int TRANSACTION_hasGrantedPolicy = 56;
    static final int TRANSACTION_hasUserSetupCompleted = 77;
    static final int TRANSACTION_installCaCert = 82;
    static final int TRANSACTION_installExistingPackage = 136;
    static final int TRANSACTION_installKeyPair = 87;
    static final int TRANSACTION_isAccessibilityServicePermittedByAdmin = 116;
    static final int TRANSACTION_isActivePasswordSufficient = 22;
    static final int TRANSACTION_isAdminActive = 50;
    static final int TRANSACTION_isAffiliatedUser = 215;
    static final int TRANSACTION_isApplicationHidden = 126;
    static final int TRANSACTION_isBackupServiceEnabled = 228;
    static final int TRANSACTION_isCaCertApproved = 86;
    static final int TRANSACTION_isCallerApplicationRestrictionsManagingPackage = 106;
    static final int TRANSACTION_isCurrentInputMethodSetByOwner = 244;
    static final int TRANSACTION_isDeviceProvisioned = 223;
    static final int TRANSACTION_isDeviceProvisioningConfigApplied = 224;
    static final int TRANSACTION_isEphemeralUser = 236;
    static final int TRANSACTION_isInputMethodPermittedByAdmin = 120;
    static final int TRANSACTION_isLockTaskPermitted = 142;
    static final int TRANSACTION_isLogoutEnabled = 248;
    static final int TRANSACTION_isManagedProfile = 192;
    static final int TRANSACTION_isMasterVolumeMuted = 151;
    static final int TRANSACTION_isMeteredDataDisabledPackageForUser = 264;
    static final int TRANSACTION_isNetworkLoggingEnabled = 232;
    static final int TRANSACTION_isNotificationListenerServicePermitted = 123;
    static final int TRANSACTION_isOverrideApnEnabled = 263;
    static final int TRANSACTION_isPackageSuspended = 81;
    static final int TRANSACTION_isProfileActivePasswordSufficientForParent = 23;
    static final int TRANSACTION_isProvisioningAllowed = 188;
    static final int TRANSACTION_isRemovingAdmin = 174;
    static final int TRANSACTION_isResetPasswordTokenActive = 242;
    static final int TRANSACTION_isSecurityLoggingEnabled = 217;
    static final int TRANSACTION_isSeparateProfileChallengeAllowed = 202;
    static final int TRANSACTION_isSystemOnlyUser = 193;
    static final int TRANSACTION_isUninstallBlocked = 154;
    static final int TRANSACTION_isUninstallInQueue = 221;
    static final int TRANSACTION_isUsingUnifiedPassword = 24;
    static final int TRANSACTION_lockNow = 34;
    static final int TRANSACTION_logoutUser = 132;
    static final int TRANSACTION_notifyLockTaskModeChanged = 152;
    static final int TRANSACTION_notifyPendingSystemUpdate = 182;
    static final int TRANSACTION_packageHasActiveAdmins = 52;
    static final int TRANSACTION_reboot = 195;
    static final int TRANSACTION_removeActiveAdmin = 54;
    static final int TRANSACTION_removeCrossProfileWidgetProvider = 168;
    static final int TRANSACTION_removeKeyPair = 88;
    static final int TRANSACTION_removeOverrideApn = 260;
    static final int TRANSACTION_removeUser = 128;
    static final int TRANSACTION_reportFailedFingerprintAttempt = 61;
    static final int TRANSACTION_reportFailedPasswordAttempt = 59;
    static final int TRANSACTION_reportKeyguardDismissed = 63;
    static final int TRANSACTION_reportKeyguardSecured = 64;
    static final int TRANSACTION_reportPasswordChanged = 58;
    static final int TRANSACTION_reportSuccessfulFingerprintAttempt = 62;
    static final int TRANSACTION_reportSuccessfulPasswordAttempt = 60;
    static final int TRANSACTION_requestBugreport = 42;
    static final int TRANSACTION_resetPassword = 29;
    static final int TRANSACTION_resetPasswordWithToken = 243;
    static final int TRANSACTION_retrieveNetworkLogs = 233;
    static final int TRANSACTION_retrievePreRebootSecurityLogs = 219;
    static final int TRANSACTION_retrieveSecurityLogs = 218;
    static final int TRANSACTION_setAccountManagementDisabled = 137;
    static final int TRANSACTION_setActiveAdmin = 49;
    static final int TRANSACTION_setActivePasswordState = 57;
    static final int TRANSACTION_setAffiliationIds = 213;
    static final int TRANSACTION_setAlwaysOnVpnPackage = 97;
    static final int TRANSACTION_setApplicationHidden = 125;
    static final int TRANSACTION_setApplicationRestrictions = 102;
    static final int TRANSACTION_setApplicationRestrictionsManagingPackage = 104;
    static final int TRANSACTION_setAutoTimeRequired = 170;
    static final int TRANSACTION_setBackupServiceEnabled = 227;
    static final int TRANSACTION_setBluetoothContactSharingDisabled = 162;
    static final int TRANSACTION_setCameraDisabled = 43;
    static final int TRANSACTION_setCertInstallerPackage = 95;
    static final int TRANSACTION_setCrossProfileCallerIdDisabled = 155;
    static final int TRANSACTION_setCrossProfileContactsSearchDisabled = 158;
    static final int TRANSACTION_setDefaultSmsApplication = 101;
    static final int TRANSACTION_setDelegatedScopes = 92;
    static final int TRANSACTION_setDeviceOwner = 65;
    static final int TRANSACTION_setDeviceOwnerLockScreenInfo = 78;
    static final int TRANSACTION_setDeviceProvisioningConfigApplied = 225;
    static final int TRANSACTION_setEndUserSessionMessage = 253;
    static final int TRANSACTION_setForceEphemeralUsers = 172;
    static final int TRANSACTION_setGlobalProxy = 36;
    static final int TRANSACTION_setGlobalSetting = 145;
    static final int TRANSACTION_setKeepUninstalledPackages = 190;
    static final int TRANSACTION_setKeyPairCertificate = 90;
    static final int TRANSACTION_setKeyguardDisabled = 179;
    static final int TRANSACTION_setKeyguardDisabledFeatures = 47;
    static final int TRANSACTION_setLockTaskFeatures = 143;
    static final int TRANSACTION_setLockTaskPackages = 140;
    static final int TRANSACTION_setLogoutEnabled = 247;
    static final int TRANSACTION_setLongSupportMessage = 198;
    static final int TRANSACTION_setMandatoryBackupTransport = 229;
    static final int TRANSACTION_setMasterVolumeMuted = 150;
    static final int TRANSACTION_setMaximumFailedPasswordsForWipe = 27;
    static final int TRANSACTION_setMaximumTimeToLock = 30;
    static final int TRANSACTION_setMeteredDataDisabledPackages = 256;
    static final int TRANSACTION_setNetworkLoggingEnabled = 231;
    static final int TRANSACTION_setOrganizationColor = 203;
    static final int TRANSACTION_setOrganizationColorForUser = 204;
    static final int TRANSACTION_setOrganizationName = 207;
    static final int TRANSACTION_setOverrideApnsEnabled = 262;
    static final int TRANSACTION_setPackagesSuspended = 80;
    static final int TRANSACTION_setPasswordExpirationTimeout = 19;
    static final int TRANSACTION_setPasswordHistoryLength = 17;
    static final int TRANSACTION_setPasswordMinimumLength = 3;
    static final int TRANSACTION_setPasswordMinimumLetters = 9;
    static final int TRANSACTION_setPasswordMinimumLowerCase = 7;
    static final int TRANSACTION_setPasswordMinimumNonLetter = 15;
    static final int TRANSACTION_setPasswordMinimumNumeric = 11;
    static final int TRANSACTION_setPasswordMinimumSymbols = 13;
    static final int TRANSACTION_setPasswordMinimumUpperCase = 5;
    static final int TRANSACTION_setPasswordQuality = 1;
    static final int TRANSACTION_setPermissionGrantState = 186;
    static final int TRANSACTION_setPermissionPolicy = 184;
    static final int TRANSACTION_setPermittedAccessibilityServices = 113;
    static final int TRANSACTION_setPermittedCrossProfileNotificationListeners = 121;
    static final int TRANSACTION_setPermittedInputMethods = 117;
    static final int TRANSACTION_setProfileEnabled = 74;
    static final int TRANSACTION_setProfileName = 75;
    static final int TRANSACTION_setProfileOwner = 71;
    static final int TRANSACTION_setRecommendedGlobalProxy = 38;
    static final int TRANSACTION_setRequiredStrongAuthTimeout = 32;
    static final int TRANSACTION_setResetPasswordToken = 240;
    static final int TRANSACTION_setRestrictionsProvider = 107;
    static final int TRANSACTION_setScreenCaptureDisabled = 45;
    static final int TRANSACTION_setSecureSetting = 147;
    static final int TRANSACTION_setSecurityLoggingEnabled = 216;
    static final int TRANSACTION_setShortSupportMessage = 196;
    static final int TRANSACTION_setStartUserSessionMessage = 252;
    static final int TRANSACTION_setStatusBarDisabled = 180;
    static final int TRANSACTION_setStorageEncryption = 39;
    static final int TRANSACTION_setSystemSetting = 146;
    static final int TRANSACTION_setSystemUpdatePolicy = 176;
    static final int TRANSACTION_setTime = 148;
    static final int TRANSACTION_setTimeZone = 149;
    static final int TRANSACTION_setTrustAgentConfiguration = 165;
    static final int TRANSACTION_setUninstallBlocked = 153;
    static final int TRANSACTION_setUserIcon = 175;
    static final int TRANSACTION_setUserProvisioningState = 212;
    static final int TRANSACTION_setUserRestriction = 109;
    static final int TRANSACTION_startManagedQuickContact = 161;
    static final int TRANSACTION_startUserInBackground = 130;
    static final int TRANSACTION_stopUser = 131;
    static final int TRANSACTION_switchUser = 129;
    static final int TRANSACTION_transferOwnership = 250;
    static final int TRANSACTION_uninstallCaCerts = 83;
    static final int TRANSACTION_uninstallPackageWithActiveAdmins = 222;
    static final int TRANSACTION_updateOverrideApn = 259;
    static final int TRANSACTION_wipeDataWithReason = 35;
    
    public Stub()
    {
      attachInterface(this, "android.app.admin.IDevicePolicyManager");
    }
    
    public static IDevicePolicyManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.app.admin.IDevicePolicyManager");
      if ((localIInterface != null) && ((localIInterface instanceof IDevicePolicyManager))) {
        return (IDevicePolicyManager)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        int i = 0;
        int j = 0;
        int k = 0;
        int m = 0;
        int n = 0;
        int i1 = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        boolean bool6 = false;
        boolean bool7 = false;
        boolean bool8 = false;
        boolean bool9 = false;
        boolean bool10 = false;
        boolean bool11 = false;
        boolean bool12 = false;
        boolean bool13 = false;
        boolean bool14 = false;
        boolean bool15 = false;
        boolean bool16 = false;
        boolean bool17 = false;
        boolean bool18 = false;
        boolean bool19 = false;
        boolean bool20 = false;
        boolean bool21 = false;
        boolean bool22 = false;
        boolean bool23 = false;
        boolean bool24 = false;
        boolean bool25 = false;
        boolean bool26 = false;
        boolean bool27 = false;
        boolean bool28 = false;
        boolean bool29 = false;
        boolean bool30 = false;
        boolean bool31 = false;
        boolean bool32 = false;
        boolean bool33 = false;
        boolean bool34 = false;
        boolean bool35 = false;
        boolean bool36 = false;
        boolean bool37 = false;
        boolean bool38 = false;
        boolean bool39 = false;
        boolean bool40 = false;
        boolean bool41 = false;
        boolean bool42 = false;
        boolean bool43 = false;
        boolean bool44 = false;
        boolean bool45 = false;
        boolean bool46 = false;
        boolean bool47 = false;
        boolean bool48 = false;
        int i8 = 0;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        Object localObject15 = null;
        Object localObject16 = null;
        Object localObject17 = null;
        Object localObject18 = null;
        Object localObject19 = null;
        Object localObject20 = null;
        Object localObject21 = null;
        Object localObject22 = null;
        Object localObject23 = null;
        Object localObject24 = null;
        Object localObject25 = null;
        Object localObject26 = null;
        Object localObject27 = null;
        Object localObject28 = null;
        Object localObject29 = null;
        Object localObject30 = null;
        Object localObject31 = null;
        Object localObject32 = null;
        Object localObject33 = null;
        Object localObject34 = null;
        Object localObject35 = null;
        Object localObject36 = null;
        Object localObject37 = null;
        Object localObject38 = null;
        Object localObject39 = null;
        Object localObject40 = null;
        Object localObject41 = null;
        Object localObject42 = null;
        Object localObject43 = null;
        Object localObject44 = null;
        Object localObject45 = null;
        Object localObject46 = null;
        Object localObject47 = null;
        Object localObject48 = null;
        Object localObject49 = null;
        Object localObject50 = null;
        Object localObject51 = null;
        Object localObject52 = null;
        Object localObject53 = null;
        Object localObject54 = null;
        Object localObject55 = null;
        Object localObject56 = null;
        Object localObject57 = null;
        Object localObject58 = null;
        Object localObject59 = null;
        Object localObject60 = null;
        Object localObject61 = null;
        Object localObject62 = null;
        Object localObject63 = null;
        Object localObject64 = null;
        Object localObject65 = null;
        Object localObject66 = null;
        Object localObject67 = null;
        Object localObject68 = null;
        Object localObject69 = null;
        Object localObject70 = null;
        Object localObject71 = null;
        byte[] arrayOfByte = null;
        Object localObject72 = null;
        Object localObject73 = null;
        Object localObject74 = null;
        Object localObject75 = null;
        Object localObject76 = null;
        Object localObject77 = null;
        Object localObject78 = null;
        Object localObject79 = null;
        Object localObject80 = null;
        Object localObject81 = null;
        Object localObject82 = null;
        Object localObject83 = null;
        Object localObject84 = null;
        Object localObject85 = null;
        Object localObject86 = null;
        Object localObject87 = null;
        Object localObject88 = null;
        Object localObject89 = null;
        Object localObject90 = null;
        Object localObject91 = null;
        Object localObject92 = null;
        Object localObject93 = null;
        Object localObject94 = null;
        Object localObject95 = null;
        Object localObject96 = null;
        Object localObject97 = null;
        Object localObject98 = null;
        Object localObject99 = null;
        Object localObject100 = null;
        Object localObject101 = null;
        Object localObject102 = null;
        Object localObject103 = null;
        Object localObject104 = null;
        Object localObject105 = null;
        Object localObject106 = null;
        Object localObject107 = null;
        Object localObject108 = null;
        Object localObject109 = null;
        Object localObject110 = null;
        Object localObject111 = null;
        Object localObject112 = null;
        Object localObject113 = null;
        Object localObject114 = null;
        Object localObject115 = null;
        Object localObject116 = null;
        Object localObject117 = null;
        Object localObject118 = null;
        Object localObject119 = null;
        Object localObject120 = null;
        Object localObject121 = null;
        Object localObject122 = null;
        Object localObject123 = null;
        String str = null;
        Object localObject124 = null;
        Object localObject125 = null;
        Object localObject126 = null;
        Object localObject127 = null;
        Object localObject128 = null;
        Object localObject129 = null;
        Object localObject130 = null;
        Object localObject131 = null;
        Object localObject132 = null;
        Object localObject133 = null;
        Object localObject134 = null;
        Object localObject135 = null;
        Object localObject136 = null;
        Object localObject137 = null;
        Object localObject138 = null;
        Object localObject139 = null;
        Object localObject140 = null;
        Object localObject141 = null;
        Object localObject142 = null;
        Object localObject143 = null;
        Object localObject144 = null;
        Object localObject145 = null;
        Object localObject146 = null;
        Object localObject147 = null;
        Object localObject148 = null;
        Object localObject149 = null;
        Object localObject150 = null;
        Object localObject151 = null;
        Object localObject152 = null;
        Object localObject153 = null;
        Object localObject154 = null;
        Object localObject155 = null;
        Object localObject156 = null;
        Object localObject157 = null;
        Object localObject158 = null;
        Object localObject159 = null;
        Object localObject160 = null;
        Object localObject161 = null;
        Object localObject162 = null;
        Object localObject163 = null;
        Object localObject164 = null;
        Object localObject165 = null;
        Object localObject166 = null;
        Object localObject167 = null;
        Object localObject168 = null;
        Object localObject169 = null;
        Object localObject170 = null;
        Object localObject171 = null;
        Object localObject172 = null;
        Object localObject173 = null;
        Object localObject174 = null;
        Object localObject175 = null;
        Object localObject176 = null;
        Object localObject177 = null;
        Object localObject178 = null;
        Object localObject179 = null;
        Object localObject180 = null;
        Object localObject181 = null;
        Object localObject182 = null;
        Object localObject183 = null;
        Object localObject184 = null;
        Object localObject185 = null;
        Object localObject186 = null;
        Object localObject187 = null;
        Object localObject188 = null;
        Object localObject189 = null;
        Object localObject190 = null;
        Object localObject191 = null;
        Object localObject192 = null;
        Object localObject193 = null;
        long l1;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 266: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getCurrentFailedPasswordAttemptsForAsus(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 265: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = 1;
          paramInt1 = getPasswordLengthDirectly((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 264: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? paramParcel1.readInt() != 0 ? 1 : localObject193 : localObject1;
          paramInt1 = isMeteredDataDisabledPackageForUser((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 263: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          paramParcel1 = paramParcel1.readInt() != 0 ? 0 : localObject2;
          paramInt1 = isOverrideApnEnabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 262: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? -1 : localObject3;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setOverrideApnsEnabled((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          return true;
        case 261: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          paramParcel1 = paramParcel1.readInt() != 0 ? null : localObject4;
          paramParcel1 = getOverrideApns(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 260: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject5;
          }
          paramInt1 = removeOverrideApn((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 259: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ApnSetting)ApnSetting.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramInt1 = updateOverrideApn((ComponentName)localObject74, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 258: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ApnSetting)ApnSetting.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramInt1 = addOverrideApn((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 257: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          paramParcel1 = getMeteredDataDisabledPackages(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 256: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject9;
          }
          paramParcel1 = setMeteredDataDisabledPackages((ComponentName)localObject74, paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 255: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          paramParcel1 = getEndUserSessionMessage(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 254: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          paramParcel1 = getStartUserSessionMessage(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 253: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          setEndUserSessionMessage((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 252: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          setStartUserSessionMessage((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 251: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getTransferOwnershipBundle();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 250: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject25 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject14;
          }
          transferOwnership((ComponentName)localObject74, (ComponentName)localObject25, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 249: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject15;
          }
          paramParcel1 = getDisallowedSystemApps((ComponentName)localObject74, paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 248: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isLogoutEnabled();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 247: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject16;
          }
          i8 = i;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setLogoutEnabled((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          return true;
        case 246: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject17;
          }
          clearApplicationUserData((ComponentName)localObject74, paramParcel1.readString(), IPackageDataObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 245: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject18;
          }
          paramParcel1 = getOwnerInstalledCaCerts(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 244: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isCurrentInputMethodSetByOwner();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 243: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject19;
          }
          paramInt1 = resetPasswordWithToken((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 242: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject20;
          }
          paramInt1 = isResetPasswordTokenActive(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 241: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject21;
          }
          paramInt1 = clearResetPasswordToken(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 240: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject22;
          }
          paramInt1 = setResetPasswordToken((ComponentName)localObject74, paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 239: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          l1 = getLastNetworkLogRetrievalTime();
          paramParcel2.writeNoException();
          l1.writeLong(2);
          return true;
        case 238: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          l1 = getLastBugReportRequestTime();
          paramParcel2.writeNoException();
          l1.writeLong(2);
          return true;
        case 237: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          l1 = getLastSecurityLogRetrievalTime();
          paramParcel2.writeNoException();
          l1.writeLong(2);
          return true;
        case 236: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          paramParcel1 = paramParcel1.readInt() != 0 ? paramParcel2 : localObject23;
          paramInt1 = isEphemeralUser(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 235: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          paramParcel1 = paramParcel1.readInt() != 0 ? 2 : localObject24;
          paramParcel1 = getBindDeviceAdminTargetUsers(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 234: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? paramParcel2 : null;
          localObject14 = IApplicationThread.Stub.asInterface(paramParcel1.readStrongBinder());
          localObject102 = paramParcel1.readStrongBinder();
          if (paramParcel1.readInt() != 0)
          {
            localObject25 = (Intent);;
            break label3681;
            tmpTernaryOp = paramParcel2;
          }
          else
          {
            for (;;) {}
          }
          paramInt1 = bindDeviceAdminServiceAsUser((ComponentName)localObject74, (IApplicationThread)localObject14, (IBinder)localObject102, (Intent)localObject25, IServiceConnection.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 233: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? 2 : localObject26;
          paramParcel1 = retrieveNetworkLogs((ComponentName)localObject74, paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 232: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject27;
          }
          paramInt1 = isNetworkLoggingEnabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 231: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject28;
          }
          i8 = j;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setNetworkLoggingEnabled((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          return true;
        case 230: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getMandatoryBackupTransport();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 229: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject29;
          }
          paramInt1 = setMandatoryBackupTransport((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 228: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject30;
          }
          paramInt1 = isBackupServiceEnabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 227: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject31;
          }
          i8 = k;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setBackupServiceEnabled((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          return true;
        case 226: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          forceUpdateUserSetupComplete();
          paramParcel2.writeNoException();
          return true;
        case 225: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          setDeviceProvisioningConfigApplied();
          paramParcel2.writeNoException();
          return true;
        case 224: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isDeviceProvisioningConfigApplied();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 223: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isDeviceProvisioned();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 222: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          uninstallPackageWithActiveAdmins(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 221: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isUninstallInQueue(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 220: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          l1 = forceSecurityLogs();
          paramParcel2.writeNoException();
          l1.writeLong(2);
          return true;
        case 219: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          paramParcel1 = paramParcel1.readInt() != 0 ? paramParcel2 : localObject32;
          paramParcel1 = retrievePreRebootSecurityLogs(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          paramParcel2.writeInt(0);
          return true;
        case 218: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject33;
          }
          paramParcel1 = retrieveSecurityLogs(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 217: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject34;
          }
          paramInt1 = isSecurityLoggingEnabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 216: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject35;
          }
          i8 = m;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setSecurityLoggingEnabled((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          return true;
        case 215: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isAffiliatedUser();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 214: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject36;
          }
          paramParcel1 = getAffiliationIds(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 213: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject37;
          }
          setAffiliationIds((ComponentName)localObject74, paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          return true;
        case 212: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          setUserProvisioningState(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 211: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getUserProvisioningState();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 210: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getOrganizationNameForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 209: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getDeviceOwnerOrganizationName();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 208: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject38;
          }
          paramParcel1 = getOrganizationName(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 207: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject39;
          }
          setOrganizationName((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 206: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getOrganizationColorForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 205: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject40;
          }
          paramInt1 = getOrganizationColor(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 204: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          setOrganizationColorForUser(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 203: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject41;
          }
          setOrganizationColor((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 202: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isSeparateProfileChallengeAllowed(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 201: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject42;
          }
          paramParcel1 = getLongSupportMessageForUser((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 200: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject43;
          }
          paramParcel1 = getShortSupportMessageForUser((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 199: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject44;
          }
          paramParcel1 = getLongSupportMessage(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 198: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject45;
          }
          setLongSupportMessage((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 197: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject46;
          }
          paramParcel1 = getShortSupportMessage(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 196: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject47;
          }
          setShortSupportMessage((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 195: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject48;
          }
          reboot(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 194: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject49;
          }
          paramParcel1 = getWifiMacAddress(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 193: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject50;
          }
          paramInt1 = isSystemOnlyUser(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 192: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject51;
          }
          paramInt1 = isManagedProfile(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 191: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject52;
          }
          paramParcel1 = getKeepUninstalledPackages((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 190: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject53;
          }
          setKeepUninstalledPackages((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          return true;
        case 189: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = checkProvisioningPreCondition(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 188: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isProvisioningAllowed(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 187: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject54;
          }
          paramInt1 = getPermissionGrantState((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 186: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {}
          for (localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);; localObject74 = localObject55) {
            break;
          }
          paramInt1 = setPermissionGrantState((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 185: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject56;
          }
          paramInt1 = getPermissionPolicy(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 184: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject57;
          }
          setPermissionPolicy((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 183: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject58;
          }
          paramParcel1 = getPendingSystemUpdate(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 182: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SystemUpdateInfo)SystemUpdateInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject59;
          }
          notifyPendingSystemUpdate(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 181: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getDoNotAskCredentialsOnBoot();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 180: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject60;
          }
          i8 = n;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          paramInt1 = setStatusBarDisabled((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 179: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject61;
          }
          i8 = i1;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          paramInt1 = setKeyguardDisabled((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 178: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          clearSystemUpdatePolicyFreezePeriodRecord();
          paramParcel2.writeNoException();
          return true;
        case 177: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getSystemUpdatePolicy();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 176: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SystemUpdatePolicy)SystemUpdatePolicy.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject62;
          }
          setSystemUpdatePolicy((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 175: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject63;
          }
          setUserIcon((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 174: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject64;
          }
          paramInt1 = isRemovingAdmin((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 173: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject65;
          }
          paramInt1 = getForceEphemeralUsers(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 172: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject66;
          }
          i8 = i2;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setForceEphemeralUsers((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          return true;
        case 171: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getAutoTimeRequired();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 170: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject67;
          }
          i8 = i3;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setAutoTimeRequired((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          return true;
        case 169: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject68;
          }
          paramParcel1 = getCrossProfileWidgetProviders(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 168: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject69;
          }
          paramInt1 = removeCrossProfileWidgetProvider((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 167: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject70;
          }
          paramInt1 = addCrossProfileWidgetProvider((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 166: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject25 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = localObject71;
          }
          paramInt1 = paramParcel1.readInt();
          i8 = i4;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          paramParcel1 = getTrustAgentConfiguration((ComponentName)localObject74, (ComponentName)localObject25, paramInt1, i8);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 165: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject25 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject102 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject102 = arrayOfByte;
          }
          i8 = i5;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setTrustAgentConfiguration((ComponentName)localObject74, (ComponentName)localObject25, (PersistableBundle)localObject102, i8);
          paramParcel2.writeNoException();
          return true;
        case 164: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getBluetoothContactSharingDisabledForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 163: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject72;
          }
          paramInt1 = getBluetoothContactSharingDisabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 162: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject73;
          }
          i8 = i6;
          if (paramParcel1.readInt() != 0) {
            i8 = 1;
          }
          setBluetoothContactSharingDisabled((ComponentName)localObject74, i8);
          paramParcel2.writeNoException();
          return true;
        case 161: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject25 = paramParcel1.readString();
          l1 = paramParcel1.readLong();
          i8 = 1;
          i8 = paramParcel1.readInt() != 0 ? 2 : 0;
          long l2 = paramParcel1.readLong();
          for (paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = paramParcel1.readInt() != 0 ? 4 : (Parcel)localObject74) {}
          l1.startManagedQuickContact(2, i8, l2, 4, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 160: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getCrossProfileContactsSearchDisabledForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 159: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          paramParcel1 = paramParcel1.readInt() != 0 ? localObject25 : localObject75;
          paramInt1 = getCrossProfileContactsSearchDisabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 158: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? this : localObject76;
          i8 = i7;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setCrossProfileContactsSearchDisabled((ComponentName)localObject74, bool49);
          paramParcel2.writeNoException();
          return true;
        case 157: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getCrossProfileCallerIdDisabledForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 156: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject77;
          }
          paramInt1 = getCrossProfileCallerIdDisabled(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 155: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject78;
          }
          bool49 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setCrossProfileCallerIdDisabled((ComponentName)localObject74, bool49);
          paramParcel2.writeNoException();
          return true;
        case 154: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject79;
          }
          paramInt1 = isUninstallBlocked((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 153: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject80;
          }
          localObject25 = paramParcel1.readString();
          localObject102 = paramParcel1.readString();
          bool49 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setUninstallBlocked((ComponentName)localObject74, (String)localObject25, (String)localObject102, bool49);
          paramParcel2.writeNoException();
          return true;
        case 152: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          bool49 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          notifyLockTaskModeChanged(bool49, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 151: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject81;
          }
          paramInt1 = isMasterVolumeMuted(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 150: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject82;
          }
          bool49 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setMasterVolumeMuted((ComponentName)localObject74, bool49);
          paramParcel2.writeNoException();
          return true;
        case 149: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject83;
          }
          paramInt1 = setTimeZone((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 148: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject84;
          }
          paramInt1 = setTime((ComponentName)localObject74, paramParcel1.readLong());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 147: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject85;
          }
          setSecureSetting((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 146: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject86;
          }
          setSystemSetting((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 145: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject87;
          }
          setGlobalSetting((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 144: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject88;
          }
          paramInt1 = getLockTaskFeatures(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 143: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject89;
          }
          setLockTaskFeatures((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 142: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isLockTaskPermitted(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 141: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject90;
          }
          paramParcel1 = getLockTaskPackages(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 140: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject91;
          }
          setLockTaskPackages((ComponentName)localObject74, paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 139: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getAccountTypesWithManagementDisabledAsUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 138: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getAccountTypesWithManagementDisabled();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 137: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject92;
          }
          localObject25 = paramParcel1.readString();
          bool49 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setAccountManagementDisabled((ComponentName)localObject74, (String)localObject25, bool49);
          paramParcel2.writeNoException();
          return true;
        case 136: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject93;
          }
          paramInt1 = installExistingPackage((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 135: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          localObject25 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject94;
          }
          paramInt1 = enableSystemAppWithIntent((ComponentName)localObject74, (String)localObject25, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 134: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject95;
          }
          enableSystemApp((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 133: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject96;
          }
          paramParcel1 = getSecondaryUsers(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 132: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject97;
          }
          paramInt1 = logoutUser(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 131: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject98;
          }
          paramInt1 = stopUser((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 130: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject99;
          }
          paramInt1 = startUserInBackground((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 129: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject100;
          }
          paramInt1 = switchUser((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 128: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (UserHandle)UserHandle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject101;
          }
          paramInt1 = removeUser((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 127: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          localObject14 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject25 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject102 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = createAndManageUser((ComponentName)localObject74, (String)localObject14, (ComponentName)localObject25, (PersistableBundle)localObject102, paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 126: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject103;
          }
          paramInt1 = isApplicationHidden((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 125: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject104;
          }
          localObject102 = paramParcel1.readString();
          localObject25 = paramParcel1.readString();
          bool49 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = setApplicationHidden((ComponentName)localObject74, (String)localObject102, (String)localObject25, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 124: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = createAdminSupportIntent(paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 123: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isNotificationListenerServicePermitted(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 122: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject105;
          }
          paramParcel1 = getPermittedCrossProfileNotificationListeners(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 121: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject106;
          }
          paramInt1 = setPermittedCrossProfileNotificationListeners((ComponentName)localObject74, paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 120: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject107;
          }
          paramInt1 = isInputMethodPermittedByAdmin((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 119: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getPermittedInputMethodsForCurrentUser();
          paramParcel2.writeNoException();
          paramParcel2.writeList(paramParcel1);
          return true;
        case 118: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject108;
          }
          paramParcel1 = getPermittedInputMethods(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeList(paramParcel1);
          return true;
        case 117: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject109;
          }
          paramInt1 = setPermittedInputMethods((ComponentName)localObject74, paramParcel1.readArrayList(getClass().getClassLoader()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 116: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject110;
          }
          paramInt1 = isAccessibilityServicePermittedByAdmin((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 115: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getPermittedAccessibilityServicesForUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeList(paramParcel1);
          return true;
        case 114: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject111;
          }
          paramParcel1 = getPermittedAccessibilityServices(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeList(paramParcel1);
          return true;
        case 113: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject112;
          }
          paramInt1 = setPermittedAccessibilityServices((ComponentName)localObject74, paramParcel1.readArrayList(getClass().getClassLoader()));
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 112: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject113;
          }
          clearCrossProfileIntentFilters(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 111: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject25 = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = localObject114;
          }
          addCrossProfileIntentFilter((ComponentName)localObject74, (IntentFilter)localObject25, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 110: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject115;
          }
          paramParcel1 = getUserRestrictions(paramParcel1);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 109: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject116;
          }
          localObject25 = paramParcel1.readString();
          bool49 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setUserRestriction((ComponentName)localObject74, (String)localObject25, bool49);
          paramParcel2.writeNoException();
          return true;
        case 108: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getRestrictionsProvider(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 107: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject117;
          }
          setRestrictionsProvider((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 106: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isCallerApplicationRestrictionsManagingPackage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 105: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject118;
          }
          paramParcel1 = getApplicationRestrictionsManagingPackage(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 104: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject119;
          }
          paramInt1 = setApplicationRestrictionsManagingPackage((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 103: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject120;
          }
          paramParcel1 = getApplicationRestrictions((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 102: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          localObject102 = paramParcel1.readString();
          localObject25 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject121;
          }
          setApplicationRestrictions((ComponentName)localObject74, (String)localObject102, (String)localObject25, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 101: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject122;
          }
          setDefaultSmsApplication((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 100: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject123;
          }
          clearPackagePersistentPreferredActivities((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 99: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject25 = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str;
          }
          addPersistentPreferredActivity((ComponentName)localObject74, (IntentFilter)localObject25, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 98: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject124;
          }
          paramParcel1 = getAlwaysOnVpnPackage(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 97: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject125;
          }
          localObject25 = paramParcel1.readString();
          bool49 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = setAlwaysOnVpnPackage((ComponentName)localObject74, (String)localObject25, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 96: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject126;
          }
          paramParcel1 = getCertInstallerPackage(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 95: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject127;
          }
          setCertInstallerPackage((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 94: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject128;
          }
          paramParcel1 = getDelegatePackages((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 93: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject129;
          }
          paramParcel1 = getDelegatedScopes((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 92: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject130;
          }
          setDelegatedScopes((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          return true;
        case 91: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject74 = (Uri)Uri.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject131;
          }
          choosePrivateKeyAlias(paramInt1, (Uri)localObject74, paramParcel1.readString(), paramParcel1.readStrongBinder());
          paramParcel2.writeNoException();
          return true;
        case 90: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {}
          for (localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);; localObject74 = localObject132) {
            break;
          }
          localObject102 = paramParcel1.readString();
          localObject14 = paramParcel1.readString();
          arrayOfByte = paramParcel1.createByteArray();
          localObject25 = paramParcel1.createByteArray();
          bool49 = bool9;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = setKeyPairCertificate((ComponentName)localObject74, (String)localObject102, (String)localObject14, arrayOfByte, (byte[])localObject25, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 89: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          localObject14 = paramParcel1.readString();
          localObject102 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {}
          for (localObject25 = (ParcelableKeyGenParameterSpec)ParcelableKeyGenParameterSpec.CREATOR.createFromParcel(paramParcel1);; localObject25 = localObject133) {
            break;
          }
          paramInt1 = paramParcel1.readInt();
          paramParcel1 = new KeymasterCertificateChain();
          paramInt1 = generateKeyPair((ComponentName)localObject74, (String)localObject14, (String)localObject102, (ParcelableKeyGenParameterSpec)localObject25, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeInt(1);
          paramParcel1.writeToParcel(paramParcel2, 1);
          return true;
        case 88: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject134;
          }
          paramInt1 = removeKeyPair((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 87: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {}
          for (localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);; localObject74 = localObject135) {
            break;
          }
          localObject14 = paramParcel1.readString();
          localObject25 = paramParcel1.createByteArray();
          arrayOfByte = paramParcel1.createByteArray();
          localObject102 = paramParcel1.createByteArray();
          str = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          } else {
            bool49 = false;
          }
          if (paramParcel1.readInt() != 0) {
            i = 1;
          } else {
            i = 0;
          }
          paramInt1 = installKeyPair((ComponentName)localObject74, (String)localObject14, (byte[])localObject25, arrayOfByte, (byte[])localObject102, str, bool49, i);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 86: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isCaCertApproved(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 85: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          bool49 = bool10;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = approveCaCert((String)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 84: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject136;
          }
          enforceCanManageCaCerts((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 83: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject137;
          }
          uninstallCaCerts((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          return true;
        case 82: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject138;
          }
          paramInt1 = installCaCert((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.createByteArray());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 81: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject139;
          }
          paramInt1 = isPackageSuspended((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 80: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject140;
          }
          localObject25 = paramParcel1.readString();
          localObject102 = paramParcel1.createStringArray();
          bool49 = bool11;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramParcel1 = setPackagesSuspended((ComponentName)localObject74, (String)localObject25, (String[])localObject102, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 79: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getDeviceOwnerLockScreenInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            TextUtils.writeToParcel(paramParcel1, paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 78: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject141;
          }
          setDeviceOwnerLockScreenInfo((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 77: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = hasUserSetupCompleted();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 76: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject142;
          }
          clearProfileOwner(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject143;
          }
          setProfileName((ComponentName)localObject74, paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject144;
          }
          setProfileEnabled(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getProfileOwnerName(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 72: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getProfileOwner(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 71: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject145;
          }
          paramInt1 = setProfileOwner((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getDeviceOwnerUserId();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          clearDeviceOwner(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getDeviceOwnerName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = hasDeviceOwner();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 66: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          } else {
            bool49 = false;
          }
          paramParcel1 = getDeviceOwnerComponent(bool49);
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject146;
          }
          paramInt1 = setDeviceOwner((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          reportKeyguardSecured(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          reportKeyguardDismissed(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          reportSuccessfulFingerprintAttempt(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          reportFailedFingerprintAttempt(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          reportSuccessfulPasswordAttempt(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          reportFailedPasswordAttempt(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 58: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          reportPasswordChanged(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 57: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (PasswordMetrics)PasswordMetrics.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject147;
          }
          setActivePasswordState((PasswordMetrics)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 56: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject148;
          }
          paramInt1 = hasGrantedPolicy((ComponentName)localObject74, paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject149;
          }
          forceRemoveActiveAdmin((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 54: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject150;
          }
          removeActiveAdmin((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 53: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject25 = (RemoteCallback)RemoteCallback.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = localObject151;
          }
          getRemoveWarning((ComponentName)localObject74, (RemoteCallback)localObject25, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = packageHasActiveAdmins(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 51: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getActiveAdmins(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 50: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject152;
          }
          paramInt1 = isAdminActive((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 49: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject153;
          }
          bool49 = bool12;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setActiveAdmin((ComponentName)localObject74, bool49, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 48: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject154;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool13;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getKeyguardDisabledFeatures((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 47: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject155;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool14;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setKeyguardDisabledFeatures((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject156;
          }
          paramInt1 = getScreenCaptureDisabled((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 45: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject157;
          }
          bool49 = bool15;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setScreenCaptureDisabled((ComponentName)localObject74, bool49);
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject158;
          }
          paramInt1 = getCameraDisabled((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject159;
          }
          bool49 = bool16;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setCameraDisabled((ComponentName)localObject74, bool49);
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject160;
          }
          paramInt1 = requestBugreport(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 41: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = getStorageEncryptionStatus(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject161;
          }
          paramInt1 = getStorageEncryption((ComponentName)localObject74, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject162;
          }
          bool49 = bool17;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = setStorageEncryption((ComponentName)localObject74, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ProxyInfo)ProxyInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject163;
          }
          setRecommendedGlobalProxy((ComponentName)localObject74, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = getGlobalProxyAdmin(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject164;
          }
          paramParcel1 = setGlobalProxy((ComponentName)localObject74, paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          wipeDataWithReason(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = paramParcel1.readInt();
          bool49 = bool18;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          lockNow(paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject165;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool19;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          l1 = getRequiredStrongAuthTimeout((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          l1.writeLong(2);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? paramParcel2 : localObject166;
          l1 = paramParcel1.readLong();
          bool49 = bool20;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          ((Stub)localObject74).setRequiredStrongAuthTimeout(l1, 2, bool49);
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? this : localObject167;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool21;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          l1 = getMaximumTimeToLock((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          l1.writeLong(2);
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? paramParcel2 : localObject168;
          l1 = paramParcel1.readLong();
          bool49 = bool22;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          ((Stub)localObject74).setMaximumTimeToLock(l1, 2, bool49);
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = resetPassword(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? this : localObject169;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool23;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getMaximumFailedPasswordsForWipe((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? 2 : localObject170;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool24;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setMaximumFailedPasswordsForWipe((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = paramParcel1.readInt();
          bool49 = bool25;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getProfileWithMinimumFailedPasswordsForWipe(paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = paramParcel1.readInt();
          bool49 = bool26;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getCurrentFailedPasswordAttempts(paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          paramParcel1 = paramParcel1.readInt() != 0 ? 2 : localObject171;
          paramInt1 = isUsingUnifiedPassword(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = isProfileActivePasswordSufficientForParent(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          paramInt1 = paramParcel1.readInt();
          bool49 = bool27;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = isActivePasswordSufficient(paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? 2 : localObject172;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool28;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          l1 = getPasswordExpiration((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          l1.writeLong(2);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? paramParcel2 : localObject173;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool29;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          l1 = getPasswordExpirationTimeout((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          l1.writeLong(2);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? paramParcel2 : localObject174;
          l1 = paramParcel1.readLong();
          bool49 = bool30;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          ((Stub)localObject74).setPasswordExpirationTimeout(l1, 2, bool49);
          paramParcel2.writeNoException();
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? this : localObject175;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool31;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordHistoryLength((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 17: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? 2 : localObject176;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool32;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setPasswordHistoryLength((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 16: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? 2 : localObject177;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool33;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordMinimumNonLetter((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 15: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? 2 : localObject178;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool34;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setPasswordMinimumNonLetter((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = paramParcel1.readInt() != 0 ? 2 : localObject179;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool35;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordMinimumSymbols((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 13: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject180;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool36;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setPasswordMinimumSymbols((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject181;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool37;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordMinimumNumeric((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 11: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject182;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool38;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setPasswordMinimumNumeric((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject183;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool39;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordMinimumLetters((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 9: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject184;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool40;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setPasswordMinimumLetters((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject185;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool41;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordMinimumLowerCase((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject186;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool42;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setPasswordMinimumLowerCase((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject187;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool43;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordMinimumUpperCase((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject188;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool44;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setPasswordMinimumUpperCase((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = localObject189;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool45;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordMinimumLength((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 3: 
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          if (paramParcel1.readInt() != 0) {
            localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject74 = null;
          }
          paramInt1 = paramParcel1.readInt();
          bool49 = bool46;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          setPasswordMinimumLength((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          return true;
        case 2: 
          label3681:
          paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
          localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          localObject74 = -1;
          paramInt1 = paramParcel1.readInt();
          bool49 = bool47;
          if (paramParcel1.readInt() != 0) {
            bool49 = true;
          }
          paramInt1 = getPasswordQuality((ComponentName)localObject74, paramInt1, bool49);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.app.admin.IDevicePolicyManager");
        localObject74 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
        localObject74 = 0;
        paramInt1 = paramParcel1.readInt();
        boolean bool49 = bool48;
        if (paramParcel1.readInt() != 0) {
          bool49 = true;
        }
        setPasswordQuality((ComponentName)localObject74, paramInt1, bool49);
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.app.admin.IDevicePolicyManager");
      return true;
    }
    
    private static class Proxy
      implements IDevicePolicyManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public void addCrossProfileIntentFilter(ComponentName paramComponentName, IntentFilter paramIntentFilter, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIntentFilter != null)
          {
            localParcel1.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(111, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean addCrossProfileWidgetProvider(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(167, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int addOverrideApn(ComponentName paramComponentName, ApnSetting paramApnSetting)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramApnSetting != null)
          {
            localParcel1.writeInt(1);
            paramApnSetting.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(258, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addPersistentPreferredActivity(ComponentName paramComponentName1, IntentFilter paramIntentFilter, ComponentName paramComponentName2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName1 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIntentFilter != null)
          {
            localParcel1.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramComponentName2 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(99, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean approveCaCert(String paramString, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(85, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean bindDeviceAdminServiceAsUser(ComponentName paramComponentName, IApplicationThread paramIApplicationThread, IBinder paramIBinder, Intent paramIntent, IServiceConnection paramIServiceConnection, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          Object localObject = null;
          if (paramIApplicationThread != null) {
            paramComponentName = paramIApplicationThread.asBinder();
          } else {
            paramComponentName = null;
          }
          localParcel1.writeStrongBinder(paramComponentName);
          localParcel1.writeStrongBinder(paramIBinder);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          paramComponentName = localObject;
          if (paramIServiceConnection != null) {
            paramComponentName = paramIServiceConnection.asBinder();
          }
          localParcel1.writeStrongBinder(paramComponentName);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(234, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int checkProvisioningPreCondition(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(189, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void choosePrivateKeyAlias(int paramInt, Uri paramUri, String paramString, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          if (paramUri != null)
          {
            localParcel1.writeInt(1);
            paramUri.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeStrongBinder(paramIBinder);
          mRemote.transact(91, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearApplicationUserData(ComponentName paramComponentName, String paramString, IPackageDataObserver paramIPackageDataObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramIPackageDataObserver != null) {
            paramComponentName = paramIPackageDataObserver.asBinder();
          } else {
            paramComponentName = null;
          }
          localParcel1.writeStrongBinder(paramComponentName);
          mRemote.transact(246, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearCrossProfileIntentFilters(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(112, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearDeviceOwner(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          mRemote.transact(69, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearPackagePersistentPreferredActivities(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(100, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearProfileOwner(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean clearResetPasswordToken(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(241, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearSystemUpdatePolicyFreezePeriodRecord()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(178, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Intent createAdminSupportIntent(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          mRemote.transact(124, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString = null;
          }
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public UserHandle createAndManageUser(ComponentName paramComponentName1, String paramString, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName1 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramComponentName2 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPersistableBundle != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(127, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName1 = (UserHandle)UserHandle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName1 = null;
          }
          return paramComponentName1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enableSystemApp(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(134, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int enableSystemAppWithIntent(ComponentName paramComponentName, String paramString, Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(135, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enforceCanManageCaCerts(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void forceRemoveActiveAdmin(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long forceSecurityLogs()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(220, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void forceUpdateUserSetupComplete()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(226, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean generateKeyPair(ComponentName paramComponentName, String paramString1, String paramString2, ParcelableKeyGenParameterSpec paramParcelableKeyGenParameterSpec, int paramInt, KeymasterCertificateChain paramKeymasterCertificateChain)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramParcelableKeyGenParameterSpec != null)
          {
            localParcel1.writeInt(1);
            paramParcelableKeyGenParameterSpec.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(89, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() == 0) {
            bool = false;
          }
          if (localParcel2.readInt() != 0) {
            paramKeymasterCertificateChain.readFromParcel(localParcel2);
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getAccountTypesWithManagementDisabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(138, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getAccountTypesWithManagementDisabledAsUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(139, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String[] arrayOfString = localParcel2.createStringArray();
          return arrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ComponentName> getActiveAdmins(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createTypedArrayList(ComponentName.CREATOR);
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getAffiliationIds(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(214, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getAlwaysOnVpnPackage(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(98, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.readString();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getApplicationRestrictions(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(103, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getApplicationRestrictionsManagingPackage(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(105, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.readString();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getAutoTimeRequired()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(171, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<UserHandle> getBindDeviceAdminTargetUsers(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(235, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createTypedArrayList(UserHandle.CREATOR);
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getBluetoothContactSharingDisabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(163, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getBluetoothContactSharingDisabledForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(164, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getCameraDisabled(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getCertInstallerPackage(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(96, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.readString();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getCrossProfileCallerIdDisabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(156, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getCrossProfileCallerIdDisabledForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(157, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getCrossProfileContactsSearchDisabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(159, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getCrossProfileContactsSearchDisabledForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(160, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getCrossProfileWidgetProviders(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(169, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCurrentFailedPasswordAttempts(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getCurrentFailedPasswordAttemptsForAsus(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(266, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getDelegatePackages(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(94, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getDelegatedScopes(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(93, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getDeviceOwnerComponent(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getDeviceOwnerLockScreenInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(79, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CharSequence localCharSequence;
          if (localParcel2.readInt() != 0) {
            localCharSequence = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            localCharSequence = null;
          }
          return localCharSequence;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDeviceOwnerName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getDeviceOwnerOrganizationName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(209, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CharSequence localCharSequence;
          if (localParcel2.readInt() != 0) {
            localCharSequence = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            localCharSequence = null;
          }
          return localCharSequence;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getDeviceOwnerUserId()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getDisallowedSystemApps(ComponentName paramComponentName, int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(249, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getDoNotAskCredentialsOnBoot()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(181, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getEndUserSessionMessage(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(255, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getForceEphemeralUsers(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(173, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getGlobalProxyAdmin(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "android.app.admin.IDevicePolicyManager";
      }
      
      public List<String> getKeepUninstalledPackages(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(191, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getKeyguardDisabledFeatures(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getLastBugReportRequestTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(238, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getLastNetworkLogRetrievalTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(239, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getLastSecurityLogRetrievalTime()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(237, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getLockTaskFeatures(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(144, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getLockTaskPackages(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(141, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArray();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getLongSupportMessage(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(199, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getLongSupportMessageForUser(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(201, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getMandatoryBackupTransport()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(230, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getMaximumFailedPasswordsForWipe(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getMaximumTimeToLock(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getMeteredDataDisabledPackages(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(257, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getOrganizationColor(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(205, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getOrganizationColorForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(206, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getOrganizationName(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(208, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getOrganizationNameForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(210, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CharSequence localCharSequence;
          if (localParcel2.readInt() != 0) {
            localCharSequence = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            localCharSequence = null;
          }
          return localCharSequence;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<ApnSetting> getOverrideApns(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(261, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createTypedArrayList(ApnSetting.CREATOR);
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public StringParceledListSlice getOwnerInstalledCaCerts(UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(245, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramUserHandle = (StringParceledListSlice)StringParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramUserHandle = null;
          }
          return paramUserHandle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getPasswordExpiration(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getPasswordExpirationTimeout(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordHistoryLength(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordLengthDirectly(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(265, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordMinimumLength(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordMinimumLetters(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordMinimumLowerCase(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordMinimumNonLetter(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordMinimumNumeric(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordMinimumSymbols(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordMinimumUpperCase(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPasswordQuality(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SystemUpdateInfo getPendingSystemUpdate(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(183, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (SystemUpdateInfo)SystemUpdateInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPermissionGrantState(ComponentName paramComponentName, String paramString1, String paramString2, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          mRemote.transact(187, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPermissionPolicy(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(185, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List getPermittedAccessibilityServices(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(114, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.readArrayList(getClass().getClassLoader());
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List getPermittedAccessibilityServicesForUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(115, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.readArrayList(getClass().getClassLoader());
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> getPermittedCrossProfileNotificationListeners(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(122, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List getPermittedInputMethods(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(118, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.readArrayList(getClass().getClassLoader());
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List getPermittedInputMethodsForCurrentUser()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(119, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.readArrayList(getClass().getClassLoader());
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getProfileOwner(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getProfileOwnerName(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          String str = localParcel2.readString();
          return str;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getProfileWithMinimumFailedPasswordsForWipe(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getRemoveWarning(ComponentName paramComponentName, RemoteCallback paramRemoteCallback, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramRemoteCallback != null)
          {
            localParcel1.writeInt(1);
            paramRemoteCallback.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getRequiredStrongAuthTimeout(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName getRestrictionsProvider(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(108, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getScreenCaptureDisabled(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<UserHandle> getSecondaryUsers(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(133, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createTypedArrayList(UserHandle.CREATOR);
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getShortSupportMessage(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(197, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getShortSupportMessageForUser(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(200, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CharSequence getStartUserSessionMessage(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(254, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getStorageEncryption(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getStorageEncryptionStatus(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          return paramInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public SystemUpdatePolicy getSystemUpdatePolicy()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(177, localParcel1, localParcel2, 0);
          localParcel2.readException();
          SystemUpdatePolicy localSystemUpdatePolicy;
          if (localParcel2.readInt() != 0) {
            localSystemUpdatePolicy = (SystemUpdatePolicy)SystemUpdatePolicy.CREATOR.createFromParcel(localParcel2);
          } else {
            localSystemUpdatePolicy = null;
          }
          return localSystemUpdatePolicy;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public PersistableBundle getTransferOwnershipBundle()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(251, localParcel1, localParcel2, 0);
          localParcel2.readException();
          PersistableBundle localPersistableBundle;
          if (localParcel2.readInt() != 0) {
            localPersistableBundle = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(localParcel2);
          } else {
            localPersistableBundle = null;
          }
          return localPersistableBundle;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<PersistableBundle> getTrustAgentConfiguration(ComponentName paramComponentName1, ComponentName paramComponentName2, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName1 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramComponentName2 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(166, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName1 = localParcel2.createTypedArrayList(PersistableBundle.CREATOR);
          return paramComponentName1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getUserProvisioningState()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(211, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bundle getUserRestrictions(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(110, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getWifiMacAddress(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(194, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.readString();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasDeviceOwner()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasGrantedPolicy(ComponentName paramComponentName, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasUserSetupCompleted()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean installCaCert(ComponentName paramComponentName, String paramString, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(82, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean installExistingPackage(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(136, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean installKeyPair(ComponentName paramComponentName, String paramString1, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, String paramString2, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          localParcel1.writeByteArray(paramArrayOfByte3);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeInt(paramBoolean2);
          mRemote.transact(87, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean1 = localParcel2.readInt();
          if (!paramBoolean1) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAccessibilityServicePermittedByAdmin(ComponentName paramComponentName, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(116, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isActivePasswordSufficient(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAdminActive(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isAffiliatedUser()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(215, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isApplicationHidden(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(126, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isBackupServiceEnabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(228, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isCaCertApproved(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(86, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isCallerApplicationRestrictionsManagingPackage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(106, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isCurrentInputMethodSetByOwner()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(244, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isDeviceProvisioned()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(223, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isDeviceProvisioningConfigApplied()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(224, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isEphemeralUser(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(236, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isInputMethodPermittedByAdmin(ComponentName paramComponentName, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(120, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isLockTaskPermitted(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(142, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isLogoutEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(248, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isManagedProfile(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(192, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isMasterVolumeMuted(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(151, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isMeteredDataDisabledPackageForUser(ComponentName paramComponentName, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(264, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isNetworkLoggingEnabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(232, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isNotificationListenerServicePermitted(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(123, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isOverrideApnEnabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(263, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isPackageSuspended(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isProfileActivePasswordSufficientForParent(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isProvisioningAllowed(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(188, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isRemovingAdmin(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(174, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isResetPasswordTokenActive(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(242, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSecurityLoggingEnabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(217, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSeparateProfileChallengeAllowed(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(202, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isSystemOnlyUser(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(193, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isUninstallBlocked(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(154, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isUninstallInQueue(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(221, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isUsingUnifiedPassword(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void lockNow(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int logoutUser(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(132, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyLockTaskModeChanged(boolean paramBoolean, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(152, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyPendingSystemUpdate(SystemUpdateInfo paramSystemUpdateInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramSystemUpdateInfo != null)
          {
            localParcel1.writeInt(1);
            paramSystemUpdateInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(182, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean packageHasActiveAdmins(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reboot(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(195, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeActiveAdmin(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean removeCrossProfileWidgetProvider(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(168, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean removeKeyPair(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(88, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean removeOverrideApn(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(260, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean removeUser(ComponentName paramComponentName, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(128, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportFailedFingerprintAttempt(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportFailedPasswordAttempt(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportKeyguardDismissed(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportKeyguardSecured(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportPasswordChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportSuccessfulFingerprintAttempt(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reportSuccessfulPasswordAttempt(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean requestBugreport(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean resetPassword(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean resetPasswordWithToken(ComponentName paramComponentName, String paramString, byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
          mRemote.transact(243, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<NetworkEvent> retrieveNetworkLogs(ComponentName paramComponentName, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong);
          mRemote.transact(233, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createTypedArrayList(NetworkEvent.CREATOR);
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice retrievePreRebootSecurityLogs(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(219, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice retrieveSecurityLogs(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(218, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAccountManagementDisabled(ComponentName paramComponentName, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(137, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setActiveAdmin(ComponentName paramComponentName, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setActivePasswordState(PasswordMetrics paramPasswordMetrics, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramPasswordMetrics != null)
          {
            localParcel1.writeInt(1);
            paramPasswordMetrics.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAffiliationIds(ComponentName paramComponentName, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStringList(paramList);
          mRemote.transact(213, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setAlwaysOnVpnPackage(ComponentName paramComponentName, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(97, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setApplicationHidden(ComponentName paramComponentName, String paramString1, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(125, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setApplicationRestrictions(ComponentName paramComponentName, String paramString1, String paramString2, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramBundle != null)
          {
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(102, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setApplicationRestrictionsManagingPackage(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(104, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setAutoTimeRequired(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(170, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setBackupServiceEnabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(227, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setBluetoothContactSharingDisabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(162, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setCameraDisabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setCertInstallerPackage(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(95, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setCrossProfileCallerIdDisabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(155, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setCrossProfileContactsSearchDisabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(158, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDefaultSmsApplication(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(101, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDelegatedScopes(ComponentName paramComponentName, String paramString, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeStringList(paramList);
          mRemote.transact(92, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setDeviceOwner(ComponentName paramComponentName, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDeviceOwnerLockScreenInfo(ComponentName paramComponentName, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(78, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setDeviceProvisioningConfigApplied()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          mRemote.transact(225, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setEndUserSessionMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(253, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setForceEphemeralUsers(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(172, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ComponentName setGlobalProxy(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            paramComponentName = null;
          }
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setGlobalSetting(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(145, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setKeepUninstalledPackages(ComponentName paramComponentName, String paramString, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeStringList(paramList);
          mRemote.transact(190, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setKeyPairCertificate(ComponentName paramComponentName, String paramString1, String paramString2, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeByteArray(paramArrayOfByte1);
          localParcel1.writeByteArray(paramArrayOfByte2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(90, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setKeyguardDisabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(179, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setKeyguardDisabledFeatures(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setLockTaskFeatures(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(143, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setLockTaskPackages(ComponentName paramComponentName, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(140, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setLogoutEnabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(247, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setLongSupportMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(198, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setMandatoryBackupTransport(ComponentName paramComponentName1, ComponentName paramComponentName2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName1 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramComponentName2 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(229, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMasterVolumeMuted(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(150, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMaximumFailedPasswordsForWipe(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setMaximumTimeToLock(ComponentName paramComponentName, long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public List<String> setMeteredDataDisabledPackages(ComponentName paramComponentName, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStringList(paramList);
          mRemote.transact(256, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArrayList();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setNetworkLoggingEnabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(231, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOrganizationColor(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(203, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOrganizationColorForUser(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(204, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOrganizationName(ComponentName paramComponentName, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(207, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOverrideApnsEnabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(262, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] setPackagesSuspended(ComponentName paramComponentName, String paramString, String[] paramArrayOfString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramComponentName = localParcel2.createStringArray();
          return paramComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordExpirationTimeout(ComponentName paramComponentName, long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordHistoryLength(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordMinimumLength(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordMinimumLetters(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordMinimumLowerCase(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordMinimumNonLetter(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordMinimumNumeric(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordMinimumSymbols(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordMinimumUpperCase(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPasswordQuality(ComponentName paramComponentName, int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPermissionGrantState(ComponentName paramComponentName, String paramString1, String paramString2, String paramString3, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt);
          mRemote.transact(186, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPermissionPolicy(ComponentName paramComponentName, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(184, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPermittedAccessibilityServices(ComponentName paramComponentName, List paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeList(paramList);
          mRemote.transact(113, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPermittedCrossProfileNotificationListeners(ComponentName paramComponentName, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeStringList(paramList);
          mRemote.transact(121, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setPermittedInputMethods(ComponentName paramComponentName, List paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeList(paramList);
          mRemote.transact(117, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setProfileEnabled(ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setProfileName(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setProfileOwner(ComponentName paramComponentName, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRecommendedGlobalProxy(ComponentName paramComponentName, ProxyInfo paramProxyInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramProxyInfo != null)
          {
            localParcel1.writeInt(1);
            paramProxyInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRequiredStrongAuthTimeout(ComponentName paramComponentName, long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setResetPasswordToken(ComponentName paramComponentName, byte[] paramArrayOfByte)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeByteArray(paramArrayOfByte);
          mRemote.transact(240, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setRestrictionsProvider(ComponentName paramComponentName1, ComponentName paramComponentName2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName1 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramComponentName2 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(107, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setScreenCaptureDisabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSecureSetting(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(147, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSecurityLoggingEnabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(216, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setShortSupportMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(196, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setStartUserSessionMessage(ComponentName paramComponentName, CharSequence paramCharSequence)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(252, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setStatusBarDisabled(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(180, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (!paramBoolean) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int setStorageEncryption(ComponentName paramComponentName, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          return paramBoolean;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSystemSetting(ComponentName paramComponentName, String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(146, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setSystemUpdatePolicy(ComponentName paramComponentName, SystemUpdatePolicy paramSystemUpdatePolicy)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramSystemUpdatePolicy != null)
          {
            localParcel1.writeInt(1);
            paramSystemUpdatePolicy.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(176, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setTime(ComponentName paramComponentName, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeLong(paramLong);
          mRemote.transact(148, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setTimeZone(ComponentName paramComponentName, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(149, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setTrustAgentConfiguration(ComponentName paramComponentName1, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName1 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramComponentName2 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPersistableBundle != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(165, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUninstallBlocked(ComponentName paramComponentName, String paramString1, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(153, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUserIcon(ComponentName paramComponentName, Bitmap paramBitmap)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramBitmap != null)
          {
            localParcel1.writeInt(1);
            paramBitmap.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(175, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUserProvisioningState(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(212, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setUserRestriction(ComponentName paramComponentName, String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(109, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void startManagedQuickContact(String paramString, long paramLong1, boolean paramBoolean, long paramLong2, Intent paramIntent)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong1);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeLong(paramLong2);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(161, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int startUserInBackground(ComponentName paramComponentName, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(130, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int stopUser(ComponentName paramComponentName, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(131, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean switchUser(ComponentName paramComponentName, UserHandle paramUserHandle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramUserHandle != null)
          {
            localParcel1.writeInt(1);
            paramUserHandle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(129, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void transferOwnership(ComponentName paramComponentName1, ComponentName paramComponentName2, PersistableBundle paramPersistableBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName1 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramComponentName2 != null)
          {
            localParcel1.writeInt(1);
            paramComponentName2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPersistableBundle != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(250, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void uninstallCaCerts(ComponentName paramComponentName, String paramString, String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(83, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void uninstallPackageWithActiveAdmins(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeString(paramString);
          mRemote.transact(222, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean updateOverrideApn(ComponentName paramComponentName, int paramInt, ApnSetting paramApnSetting)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          boolean bool = true;
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          if (paramApnSetting != null)
          {
            localParcel1.writeInt(1);
            paramApnSetting.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(259, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt = localParcel2.readInt();
          if (paramInt == 0) {
            bool = false;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void wipeDataWithReason(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.app.admin.IDevicePolicyManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
