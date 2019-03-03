package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.dex.IArtManager;
import android.content.pm.dex.IArtManager.Stub;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

public abstract interface IPackageManager
  extends IInterface
{
  public abstract boolean activitySupportsIntent(ComponentName paramComponentName, Intent paramIntent, String paramString)
    throws RemoteException;
  
  public abstract void addCrossProfileIntentFilter(IntentFilter paramIntentFilter, String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void addOnPermissionsChangeListener(IOnPermissionsChangeListener paramIOnPermissionsChangeListener)
    throws RemoteException;
  
  public abstract boolean addPermission(PermissionInfo paramPermissionInfo)
    throws RemoteException;
  
  public abstract boolean addPermissionAsync(PermissionInfo paramPermissionInfo)
    throws RemoteException;
  
  public abstract void addPersistentPreferredActivity(IntentFilter paramIntentFilter, ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract void addPreferredActivity(IntentFilter paramIntentFilter, int paramInt1, ComponentName[] paramArrayOfComponentName, ComponentName paramComponentName, int paramInt2)
    throws RemoteException;
  
  public abstract boolean canForwardTo(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract boolean canRequestPackageInstalls(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract String[] canonicalToCurrentPackageNames(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void checkPackageStartable(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int checkPermission(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract int checkSignatures(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract int checkUidPermission(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int checkUidSignatures(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void clearApplicationProfileData(String paramString)
    throws RemoteException;
  
  public abstract void clearApplicationUserData(String paramString, IPackageDataObserver paramIPackageDataObserver, int paramInt)
    throws RemoteException;
  
  public abstract void clearCrossProfileIntentFilters(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void clearPackagePersistentPreferredActivities(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void clearPackagePreferredActivities(String paramString)
    throws RemoteException;
  
  public abstract String[] currentToCanonicalPackageNames(String[] paramArrayOfString)
    throws RemoteException;
  
  public abstract void deleteApplicationCacheFiles(String paramString, IPackageDataObserver paramIPackageDataObserver)
    throws RemoteException;
  
  public abstract void deleteApplicationCacheFilesAsUser(String paramString, int paramInt, IPackageDataObserver paramIPackageDataObserver)
    throws RemoteException;
  
  public abstract void deleteBundledPackageVersioned(VersionedPackage paramVersionedPackage, IPackageDeleteObserver2 paramIPackageDeleteObserver2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void deletePackageAsUser(String paramString, int paramInt1, IPackageDeleteObserver paramIPackageDeleteObserver, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void deletePackageVersioned(VersionedPackage paramVersionedPackage, IPackageDeleteObserver2 paramIPackageDeleteObserver2, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void deletePreloadsFileCache()
    throws RemoteException;
  
  public abstract void dumpProfiles(String paramString)
    throws RemoteException;
  
  public abstract void enterSafeMode()
    throws RemoteException;
  
  public abstract void executeForceUninstall()
    throws RemoteException;
  
  public abstract void extendVerificationTimeout(int paramInt1, int paramInt2, long paramLong)
    throws RemoteException;
  
  public abstract ResolveInfo findPersistentPreferredActivity(Intent paramIntent, int paramInt)
    throws RemoteException;
  
  public abstract void finishPackageInstall(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void flushPackageRestrictionsAsUser(int paramInt)
    throws RemoteException;
  
  public abstract void forceDexOpt(String paramString)
    throws RemoteException;
  
  public abstract void freeStorage(String paramString, long paramLong, int paramInt, IntentSender paramIntentSender)
    throws RemoteException;
  
  public abstract void freeStorageAndNotify(String paramString, long paramLong, int paramInt, IPackageDataObserver paramIPackageDataObserver)
    throws RemoteException;
  
  public abstract ActivityInfo getActivityInfo(ComponentName paramComponentName, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ParceledListSlice getAllIntentFilters(String paramString)
    throws RemoteException;
  
  public abstract List<String> getAllPackages()
    throws RemoteException;
  
  public abstract ParceledListSlice getAllPermissionGroups(int paramInt)
    throws RemoteException;
  
  public abstract String[] getAppOpPermissionPackages(String paramString)
    throws RemoteException;
  
  public abstract int getApplicationEnabledSetting(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean getApplicationHiddenSettingAsUser(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ApplicationInfo getApplicationInfo(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract IArtManager getArtManager()
    throws RemoteException;
  
  public abstract boolean getBlockUninstallForUser(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ChangedPackages getChangedPackages(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract int getComponentEnabledSetting(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract byte[] getDefaultAppsBackup(int paramInt)
    throws RemoteException;
  
  public abstract String getDefaultBrowserPackageName(int paramInt)
    throws RemoteException;
  
  public abstract int getFlagsForUid(int paramInt)
    throws RemoteException;
  
  public abstract CharSequence getHarmfulAppWarning(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ComponentName getHomeActivities(List<ResolveInfo> paramList)
    throws RemoteException;
  
  public abstract int getInstallLocation()
    throws RemoteException;
  
  public abstract int getInstallReason(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getInstalledApplications(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ParceledListSlice getInstalledPackages(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract String getInstallerPackageName(String paramString)
    throws RemoteException;
  
  public abstract String getInstantAppAndroidId(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract byte[] getInstantAppCookie(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract Bitmap getInstantAppIcon(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ComponentName getInstantAppInstallerComponent()
    throws RemoteException;
  
  public abstract ComponentName getInstantAppResolverComponent()
    throws RemoteException;
  
  public abstract ComponentName getInstantAppResolverSettingsComponent()
    throws RemoteException;
  
  public abstract ParceledListSlice getInstantApps(int paramInt)
    throws RemoteException;
  
  public abstract InstrumentationInfo getInstrumentationInfo(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract byte[] getIntentFilterVerificationBackup(int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getIntentFilterVerifications(String paramString)
    throws RemoteException;
  
  public abstract int getIntentVerificationStatus(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract KeySet getKeySetByAlias(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract ResolveInfo getLastChosenActivity(Intent paramIntent, String paramString, int paramInt)
    throws RemoteException;
  
  public abstract int getMoveStatus(int paramInt)
    throws RemoteException;
  
  public abstract String getNameForUid(int paramInt)
    throws RemoteException;
  
  public abstract String[] getNamesForUids(int[] paramArrayOfInt)
    throws RemoteException;
  
  public abstract int[] getPackageGids(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract PackageInfo getPackageInfo(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract PackageInfo getPackageInfoVersioned(VersionedPackage paramVersionedPackage, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract IPackageInstaller getPackageInstaller()
    throws RemoteException;
  
  public abstract void getPackageSizeInfo(String paramString, int paramInt, IPackageStatsObserver paramIPackageStatsObserver)
    throws RemoteException;
  
  public abstract int getPackageUid(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract String[] getPackagesForUid(int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getPackagesHoldingPermissions(String[] paramArrayOfString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract String getPermissionControllerPackageName()
    throws RemoteException;
  
  public abstract int getPermissionFlags(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract byte[] getPermissionGrantBackup(int paramInt)
    throws RemoteException;
  
  public abstract PermissionGroupInfo getPermissionGroupInfo(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract PermissionInfo getPermissionInfo(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getPersistentApplications(int paramInt)
    throws RemoteException;
  
  public abstract int getPreferredActivities(List<IntentFilter> paramList, List<ComponentName> paramList1, String paramString)
    throws RemoteException;
  
  public abstract byte[] getPreferredActivityBackup(int paramInt)
    throws RemoteException;
  
  public abstract int getPrivateFlagsForUid(int paramInt)
    throws RemoteException;
  
  public abstract ProviderInfo getProviderInfo(ComponentName paramComponentName, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ActivityInfo getReceiverInfo(ComponentName paramComponentName, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ServiceInfo getServiceInfo(ComponentName paramComponentName, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract String getServicesSystemSharedLibraryPackageName()
    throws RemoteException;
  
  public abstract ParceledListSlice getSharedLibraries(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract String getSharedSystemSharedLibraryPackageName()
    throws RemoteException;
  
  public abstract KeySet getSigningKeySet(String paramString)
    throws RemoteException;
  
  public abstract PersistableBundle getSuspendedPackageAppExtras(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice getSystemAvailableFeatures()
    throws RemoteException;
  
  public abstract String[] getSystemSharedLibraryNames()
    throws RemoteException;
  
  public abstract String getSystemTextClassifierPackageName()
    throws RemoteException;
  
  public abstract int getUidForSharedUser(String paramString)
    throws RemoteException;
  
  public abstract VerifierDeviceIdentity getVerifierDeviceIdentity()
    throws RemoteException;
  
  public abstract void grantDefaultPermissionsToActiveLuiApp(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void grantDefaultPermissionsToEnabledCarrierApps(String[] paramArrayOfString, int paramInt)
    throws RemoteException;
  
  public abstract void grantDefaultPermissionsToEnabledImsServices(String[] paramArrayOfString, int paramInt)
    throws RemoteException;
  
  public abstract void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] paramArrayOfString, int paramInt)
    throws RemoteException;
  
  public abstract void grantRuntimePermission(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasSigningCertificate(String paramString, byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasSystemFeature(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean hasSystemUidErrors()
    throws RemoteException;
  
  public abstract boolean hasUidSigningCertificate(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
    throws RemoteException;
  
  public abstract int installExistingPackageAsUser(String paramString, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract boolean isFirstBoot()
    throws RemoteException;
  
  public abstract boolean isInApp2sdBlacklist(String paramString)
    throws RemoteException;
  
  public abstract boolean isInstantApp(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isNeedProtectSecPreloadedApps()
    throws RemoteException;
  
  public abstract boolean isOnlyCoreApps()
    throws RemoteException;
  
  public abstract boolean isPackageAvailable(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isPackageDeviceAdminOnAnyUser(String paramString)
    throws RemoteException;
  
  public abstract boolean isPackageSignedByKeySet(String paramString, KeySet paramKeySet)
    throws RemoteException;
  
  public abstract boolean isPackageSignedByKeySetExactly(String paramString, KeySet paramKeySet)
    throws RemoteException;
  
  public abstract boolean isPackageStateProtected(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isPackageSuspendedForUser(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean isPermissionEnforced(String paramString)
    throws RemoteException;
  
  public abstract boolean isPermissionRevokedByPolicy(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract boolean isProtectedBroadcast(String paramString)
    throws RemoteException;
  
  public abstract boolean isSafeMode()
    throws RemoteException;
  
  public abstract boolean isStorageLow()
    throws RemoteException;
  
  public abstract boolean isSupportUninstallablePreloadedApps()
    throws RemoteException;
  
  public abstract boolean isUidPrivileged(int paramInt)
    throws RemoteException;
  
  public abstract boolean isUpgrade()
    throws RemoteException;
  
  public abstract void logAppProcessStartIfNeeded(String paramString1, int paramInt1, String paramString2, String paramString3, int paramInt2)
    throws RemoteException;
  
  public abstract int movePackage(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract int movePrimaryStorage(String paramString)
    throws RemoteException;
  
  public abstract PackageCleanItem nextPackageToClean(PackageCleanItem paramPackageCleanItem)
    throws RemoteException;
  
  public abstract void notifyDexLoad(String paramString1, List<String> paramList1, List<String> paramList2, String paramString2)
    throws RemoteException;
  
  public abstract void notifyPackageUse(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract boolean performDexOptMode(String paramString1, boolean paramBoolean1, String paramString2, boolean paramBoolean2, boolean paramBoolean3, String paramString3)
    throws RemoteException;
  
  public abstract boolean performDexOptSecondary(String paramString1, String paramString2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void performFstrimIfNeeded()
    throws RemoteException;
  
  public abstract ParceledListSlice queryContentProviders(String paramString1, int paramInt1, int paramInt2, String paramString2)
    throws RemoteException;
  
  public abstract ParceledListSlice queryInstrumentation(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract ParceledListSlice queryIntentActivities(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ParceledListSlice queryIntentActivityOptions(ComponentName paramComponentName, Intent[] paramArrayOfIntent, String[] paramArrayOfString, Intent paramIntent, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ParceledListSlice queryIntentContentProviders(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ParceledListSlice queryIntentReceivers(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ParceledListSlice queryIntentServices(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ParceledListSlice queryPermissionsByGroup(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void querySyncProviders(List<String> paramList, List<ProviderInfo> paramList1)
    throws RemoteException;
  
  public abstract void reconcileSecondaryDexFiles(String paramString)
    throws RemoteException;
  
  public abstract void refreshApp2sdBlacklist()
    throws RemoteException;
  
  public abstract void refreshAppAspectRatio()
    throws RemoteException;
  
  public abstract void refreshAppSupportNotchList()
    throws RemoteException;
  
  public abstract void registerDexModule(String paramString1, String paramString2, boolean paramBoolean, IDexModuleRegisterCallback paramIDexModuleRegisterCallback)
    throws RemoteException;
  
  public abstract void registerMoveCallback(IPackageMoveObserver paramIPackageMoveObserver)
    throws RemoteException;
  
  public abstract void removeOnPermissionsChangeListener(IOnPermissionsChangeListener paramIOnPermissionsChangeListener)
    throws RemoteException;
  
  public abstract void removePermission(String paramString)
    throws RemoteException;
  
  public abstract void replacePreferredActivity(IntentFilter paramIntentFilter, int paramInt1, ComponentName[] paramArrayOfComponentName, ComponentName paramComponentName, int paramInt2)
    throws RemoteException;
  
  public abstract void resetApplicationPreferences(int paramInt)
    throws RemoteException;
  
  public abstract void resetRuntimePermissions()
    throws RemoteException;
  
  public abstract ProviderInfo resolveContentProvider(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ResolveInfo resolveIntent(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract ResolveInfo resolveService(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void restoreDefaultApps(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract void restoreIntentFilterVerification(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract void restorePermissionGrants(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract void restorePreferredActivities(byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] paramArrayOfString, int paramInt)
    throws RemoteException;
  
  public abstract void revokeDefaultPermissionsFromLuiApps(String[] paramArrayOfString, int paramInt)
    throws RemoteException;
  
  public abstract void revokeRuntimePermission(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract boolean runBackgroundDexoptJob(List<String> paramList)
    throws RemoteException;
  
  public abstract void setApplicationCategoryHint(String paramString1, int paramInt, String paramString2)
    throws RemoteException;
  
  public abstract void setApplicationEnabledSetting(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2)
    throws RemoteException;
  
  public abstract boolean setApplicationHiddenSettingAsUser(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract boolean setBlockUninstallForUser(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setComponentEnabledSetting(ComponentName paramComponentName, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract boolean setDefaultBrowserPackageName(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void setFillNotchRegion(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract void setHarmfulAppWarning(String paramString, CharSequence paramCharSequence, int paramInt)
    throws RemoteException;
  
  public abstract void setHomeActivity(ComponentName paramComponentName, int paramInt)
    throws RemoteException;
  
  public abstract boolean setInstallLocation(int paramInt)
    throws RemoteException;
  
  public abstract void setInstallerPackageName(String paramString1, String paramString2)
    throws RemoteException;
  
  public abstract boolean setInstantAppCookie(String paramString, byte[] paramArrayOfByte, int paramInt)
    throws RemoteException;
  
  public abstract void setLastChosenActivity(Intent paramIntent, String paramString, int paramInt1, IntentFilter paramIntentFilter, int paramInt2, ComponentName paramComponentName)
    throws RemoteException;
  
  public abstract void setOverrideMaxAspect(String paramString, float paramFloat, int paramInt)
    throws RemoteException;
  
  public abstract void setPackageStoppedState(String paramString, boolean paramBoolean, int paramInt)
    throws RemoteException;
  
  public abstract String[] setPackagesSuspendedAsUser(String[] paramArrayOfString, boolean paramBoolean, PersistableBundle paramPersistableBundle1, PersistableBundle paramPersistableBundle2, String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void setPermissionEnforced(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean setRequiredForSystemUser(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setUpdateAvailable(String paramString, boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean shouldShowRequestPermissionRationale(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void systemReady()
    throws RemoteException;
  
  public abstract void unregisterMoveCallback(IPackageMoveObserver paramIPackageMoveObserver)
    throws RemoteException;
  
  public abstract boolean updateIntentVerificationStatus(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void updatePackagesIfNeeded()
    throws RemoteException;
  
  public abstract void updatePermissionFlags(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void updatePermissionFlagsForAllApps(int paramInt1, int paramInt2, int paramInt3)
    throws RemoteException;
  
  public abstract void verifyIntentFilter(int paramInt1, int paramInt2, List<String> paramList)
    throws RemoteException;
  
  public abstract void verifyPendingInstall(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IPackageManager
  {
    private static final String DESCRIPTOR = "android.content.pm.IPackageManager";
    static final int TRANSACTION_activitySupportsIntent = 15;
    static final int TRANSACTION_addCrossProfileIntentFilter = 75;
    static final int TRANSACTION_addOnPermissionsChangeListener = 157;
    static final int TRANSACTION_addPermission = 21;
    static final int TRANSACTION_addPermissionAsync = 128;
    static final int TRANSACTION_addPersistentPreferredActivity = 73;
    static final int TRANSACTION_addPreferredActivity = 69;
    static final int TRANSACTION_canForwardTo = 44;
    static final int TRANSACTION_canRequestPackageInstalls = 180;
    static final int TRANSACTION_canonicalToCurrentPackageNames = 8;
    static final int TRANSACTION_checkPackageStartable = 1;
    static final int TRANSACTION_checkPermission = 19;
    static final int TRANSACTION_checkSignatures = 31;
    static final int TRANSACTION_checkUidPermission = 20;
    static final int TRANSACTION_checkUidSignatures = 32;
    static final int TRANSACTION_clearApplicationProfileData = 102;
    static final int TRANSACTION_clearApplicationUserData = 101;
    static final int TRANSACTION_clearCrossProfileIntentFilters = 76;
    static final int TRANSACTION_clearPackagePersistentPreferredActivities = 74;
    static final int TRANSACTION_clearPackagePreferredActivities = 71;
    static final int TRANSACTION_currentToCanonicalPackageNames = 7;
    static final int TRANSACTION_deleteApplicationCacheFiles = 99;
    static final int TRANSACTION_deleteApplicationCacheFilesAsUser = 100;
    static final int TRANSACTION_deleteBundledPackageVersioned = 195;
    static final int TRANSACTION_deletePackageAsUser = 63;
    static final int TRANSACTION_deletePackageVersioned = 64;
    static final int TRANSACTION_deletePreloadsFileCache = 181;
    static final int TRANSACTION_dumpProfiles = 118;
    static final int TRANSACTION_enterSafeMode = 107;
    static final int TRANSACTION_executeForceUninstall = 198;
    static final int TRANSACTION_extendVerificationTimeout = 133;
    static final int TRANSACTION_findPersistentPreferredActivity = 43;
    static final int TRANSACTION_finishPackageInstall = 60;
    static final int TRANSACTION_flushPackageRestrictionsAsUser = 95;
    static final int TRANSACTION_forceDexOpt = 119;
    static final int TRANSACTION_freeStorage = 98;
    static final int TRANSACTION_freeStorageAndNotify = 97;
    static final int TRANSACTION_getActivityInfo = 14;
    static final int TRANSACTION_getAllIntentFilters = 138;
    static final int TRANSACTION_getAllPackages = 33;
    static final int TRANSACTION_getAllPermissionGroups = 12;
    static final int TRANSACTION_getAppOpPermissionPackages = 41;
    static final int TRANSACTION_getApplicationEnabledSetting = 93;
    static final int TRANSACTION_getApplicationHiddenSettingAsUser = 149;
    static final int TRANSACTION_getApplicationInfo = 13;
    static final int TRANSACTION_getArtManager = 186;
    static final int TRANSACTION_getBlockUninstallForUser = 152;
    static final int TRANSACTION_getChangedPackages = 176;
    static final int TRANSACTION_getComponentEnabledSetting = 91;
    static final int TRANSACTION_getDefaultAppsBackup = 82;
    static final int TRANSACTION_getDefaultBrowserPackageName = 140;
    static final int TRANSACTION_getFlagsForUid = 38;
    static final int TRANSACTION_getHarmfulAppWarning = 188;
    static final int TRANSACTION_getHomeActivities = 88;
    static final int TRANSACTION_getInstallLocation = 130;
    static final int TRANSACTION_getInstallReason = 178;
    static final int TRANSACTION_getInstalledApplications = 53;
    static final int TRANSACTION_getInstalledPackages = 51;
    static final int TRANSACTION_getInstallerPackageName = 65;
    static final int TRANSACTION_getInstantAppAndroidId = 185;
    static final int TRANSACTION_getInstantAppCookie = 168;
    static final int TRANSACTION_getInstantAppIcon = 170;
    static final int TRANSACTION_getInstantAppInstallerComponent = 184;
    static final int TRANSACTION_getInstantAppResolverComponent = 182;
    static final int TRANSACTION_getInstantAppResolverSettingsComponent = 183;
    static final int TRANSACTION_getInstantApps = 167;
    static final int TRANSACTION_getInstrumentationInfo = 58;
    static final int TRANSACTION_getIntentFilterVerificationBackup = 84;
    static final int TRANSACTION_getIntentFilterVerifications = 137;
    static final int TRANSACTION_getIntentVerificationStatus = 135;
    static final int TRANSACTION_getKeySetByAlias = 153;
    static final int TRANSACTION_getLastChosenActivity = 67;
    static final int TRANSACTION_getMoveStatus = 123;
    static final int TRANSACTION_getNameForUid = 35;
    static final int TRANSACTION_getNamesForUids = 36;
    static final int TRANSACTION_getPackageGids = 6;
    static final int TRANSACTION_getPackageInfo = 3;
    static final int TRANSACTION_getPackageInfoVersioned = 4;
    static final int TRANSACTION_getPackageInstaller = 150;
    static final int TRANSACTION_getPackageSizeInfo = 103;
    static final int TRANSACTION_getPackageUid = 5;
    static final int TRANSACTION_getPackagesForUid = 34;
    static final int TRANSACTION_getPackagesHoldingPermissions = 52;
    static final int TRANSACTION_getPermissionControllerPackageName = 166;
    static final int TRANSACTION_getPermissionFlags = 26;
    static final int TRANSACTION_getPermissionGrantBackup = 86;
    static final int TRANSACTION_getPermissionGroupInfo = 11;
    static final int TRANSACTION_getPermissionInfo = 9;
    static final int TRANSACTION_getPersistentApplications = 54;
    static final int TRANSACTION_getPreferredActivities = 72;
    static final int TRANSACTION_getPreferredActivityBackup = 80;
    static final int TRANSACTION_getPrivateFlagsForUid = 39;
    static final int TRANSACTION_getProviderInfo = 18;
    static final int TRANSACTION_getReceiverInfo = 16;
    static final int TRANSACTION_getServiceInfo = 17;
    static final int TRANSACTION_getServicesSystemSharedLibraryPackageName = 174;
    static final int TRANSACTION_getSharedLibraries = 179;
    static final int TRANSACTION_getSharedSystemSharedLibraryPackageName = 175;
    static final int TRANSACTION_getSigningKeySet = 154;
    static final int TRANSACTION_getSuspendedPackageAppExtras = 79;
    static final int TRANSACTION_getSystemAvailableFeatures = 105;
    static final int TRANSACTION_getSystemSharedLibraryNames = 104;
    static final int TRANSACTION_getSystemTextClassifierPackageName = 191;
    static final int TRANSACTION_getUidForSharedUser = 37;
    static final int TRANSACTION_getVerifierDeviceIdentity = 141;
    static final int TRANSACTION_grantDefaultPermissionsToActiveLuiApp = 163;
    static final int TRANSACTION_grantDefaultPermissionsToEnabledCarrierApps = 159;
    static final int TRANSACTION_grantDefaultPermissionsToEnabledImsServices = 160;
    static final int TRANSACTION_grantDefaultPermissionsToEnabledTelephonyDataServices = 161;
    static final int TRANSACTION_grantRuntimePermission = 23;
    static final int TRANSACTION_hasSigningCertificate = 189;
    static final int TRANSACTION_hasSystemFeature = 106;
    static final int TRANSACTION_hasSystemUidErrors = 110;
    static final int TRANSACTION_hasUidSigningCertificate = 190;
    static final int TRANSACTION_installExistingPackageAsUser = 131;
    static final int TRANSACTION_isFirstBoot = 142;
    static final int TRANSACTION_isInApp2sdBlacklist = 196;
    static final int TRANSACTION_isInstantApp = 171;
    static final int TRANSACTION_isNeedProtectSecPreloadedApps = 194;
    static final int TRANSACTION_isOnlyCoreApps = 143;
    static final int TRANSACTION_isPackageAvailable = 2;
    static final int TRANSACTION_isPackageDeviceAdminOnAnyUser = 177;
    static final int TRANSACTION_isPackageSignedByKeySet = 155;
    static final int TRANSACTION_isPackageSignedByKeySetExactly = 156;
    static final int TRANSACTION_isPackageStateProtected = 192;
    static final int TRANSACTION_isPackageSuspendedForUser = 78;
    static final int TRANSACTION_isPermissionEnforced = 146;
    static final int TRANSACTION_isPermissionRevokedByPolicy = 165;
    static final int TRANSACTION_isProtectedBroadcast = 30;
    static final int TRANSACTION_isSafeMode = 108;
    static final int TRANSACTION_isStorageLow = 147;
    static final int TRANSACTION_isSupportUninstallablePreloadedApps = 193;
    static final int TRANSACTION_isUidPrivileged = 40;
    static final int TRANSACTION_isUpgrade = 144;
    static final int TRANSACTION_logAppProcessStartIfNeeded = 94;
    static final int TRANSACTION_movePackage = 126;
    static final int TRANSACTION_movePrimaryStorage = 127;
    static final int TRANSACTION_nextPackageToClean = 122;
    static final int TRANSACTION_notifyDexLoad = 114;
    static final int TRANSACTION_notifyPackageUse = 113;
    static final int TRANSACTION_performDexOptMode = 116;
    static final int TRANSACTION_performDexOptSecondary = 117;
    static final int TRANSACTION_performFstrimIfNeeded = 111;
    static final int TRANSACTION_queryContentProviders = 57;
    static final int TRANSACTION_queryInstrumentation = 59;
    static final int TRANSACTION_queryIntentActivities = 45;
    static final int TRANSACTION_queryIntentActivityOptions = 46;
    static final int TRANSACTION_queryIntentContentProviders = 50;
    static final int TRANSACTION_queryIntentReceivers = 47;
    static final int TRANSACTION_queryIntentServices = 49;
    static final int TRANSACTION_queryPermissionsByGroup = 10;
    static final int TRANSACTION_querySyncProviders = 56;
    static final int TRANSACTION_reconcileSecondaryDexFiles = 121;
    static final int TRANSACTION_refreshApp2sdBlacklist = 197;
    static final int TRANSACTION_refreshAppAspectRatio = 200;
    static final int TRANSACTION_refreshAppSupportNotchList = 202;
    static final int TRANSACTION_registerDexModule = 115;
    static final int TRANSACTION_registerMoveCallback = 124;
    static final int TRANSACTION_removeOnPermissionsChangeListener = 158;
    static final int TRANSACTION_removePermission = 22;
    static final int TRANSACTION_replacePreferredActivity = 70;
    static final int TRANSACTION_resetApplicationPreferences = 66;
    static final int TRANSACTION_resetRuntimePermissions = 25;
    static final int TRANSACTION_resolveContentProvider = 55;
    static final int TRANSACTION_resolveIntent = 42;
    static final int TRANSACTION_resolveService = 48;
    static final int TRANSACTION_restoreDefaultApps = 83;
    static final int TRANSACTION_restoreIntentFilterVerification = 85;
    static final int TRANSACTION_restorePermissionGrants = 87;
    static final int TRANSACTION_restorePreferredActivities = 81;
    static final int TRANSACTION_revokeDefaultPermissionsFromDisabledTelephonyDataServices = 162;
    static final int TRANSACTION_revokeDefaultPermissionsFromLuiApps = 164;
    static final int TRANSACTION_revokeRuntimePermission = 24;
    static final int TRANSACTION_runBackgroundDexoptJob = 120;
    static final int TRANSACTION_setApplicationCategoryHint = 62;
    static final int TRANSACTION_setApplicationEnabledSetting = 92;
    static final int TRANSACTION_setApplicationHiddenSettingAsUser = 148;
    static final int TRANSACTION_setBlockUninstallForUser = 151;
    static final int TRANSACTION_setComponentEnabledSetting = 90;
    static final int TRANSACTION_setDefaultBrowserPackageName = 139;
    static final int TRANSACTION_setFillNotchRegion = 201;
    static final int TRANSACTION_setHarmfulAppWarning = 187;
    static final int TRANSACTION_setHomeActivity = 89;
    static final int TRANSACTION_setInstallLocation = 129;
    static final int TRANSACTION_setInstallerPackageName = 61;
    static final int TRANSACTION_setInstantAppCookie = 169;
    static final int TRANSACTION_setLastChosenActivity = 68;
    static final int TRANSACTION_setOverrideMaxAspect = 199;
    static final int TRANSACTION_setPackageStoppedState = 96;
    static final int TRANSACTION_setPackagesSuspendedAsUser = 77;
    static final int TRANSACTION_setPermissionEnforced = 145;
    static final int TRANSACTION_setRequiredForSystemUser = 172;
    static final int TRANSACTION_setUpdateAvailable = 173;
    static final int TRANSACTION_shouldShowRequestPermissionRationale = 29;
    static final int TRANSACTION_systemReady = 109;
    static final int TRANSACTION_unregisterMoveCallback = 125;
    static final int TRANSACTION_updateIntentVerificationStatus = 136;
    static final int TRANSACTION_updatePackagesIfNeeded = 112;
    static final int TRANSACTION_updatePermissionFlags = 27;
    static final int TRANSACTION_updatePermissionFlagsForAllApps = 28;
    static final int TRANSACTION_verifyIntentFilter = 134;
    static final int TRANSACTION_verifyPendingInstall = 132;
    
    public Stub()
    {
      attachInterface(this, "android.content.pm.IPackageManager");
    }
    
    public static IPackageManager asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("android.content.pm.IPackageManager");
      if ((localIInterface != null) && ((localIInterface instanceof IPackageManager))) {
        return (IPackageManager)localIInterface;
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
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 202: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          refreshAppSupportNotchList();
          paramParcel2.writeNoException();
          return true;
        case 201: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setFillNotchRegion((String)localObject1, bool10, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 200: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          refreshAppAspectRatio();
          paramParcel2.writeNoException();
          return true;
        case 199: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          setOverrideMaxAspect(paramParcel1.readString(), paramParcel1.readFloat(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 198: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          executeForceUninstall();
          paramParcel2.writeNoException();
          return true;
        case 197: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          refreshApp2sdBlacklist();
          paramParcel2.writeNoException();
          return true;
        case 196: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isInApp2sdBlacklist(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 195: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (VersionedPackage)VersionedPackage.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject37;
          }
          deleteBundledPackageVersioned((VersionedPackage)localObject1, IPackageDeleteObserver2.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 194: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isNeedProtectSecPreloadedApps();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 193: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isSupportUninstallablePreloadedApps();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 192: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isPackageStateProtected(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 191: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getSystemTextClassifierPackageName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 190: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = hasUidSigningCertificate(paramParcel1.readInt(), paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 189: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = hasSigningCertificate(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 188: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getHarmfulAppWarning(paramParcel1.readString(), paramParcel1.readInt());
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
        case 187: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject25 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            localObject1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel1);
          }
          setHarmfulAppWarning((String)localObject25, (CharSequence)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 186: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = getArtManager();
          paramParcel2.writeNoException();
          paramParcel1 = localObject2;
          if (localObject1 != null) {
            paramParcel1 = ((IArtManager)localObject1).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 185: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstantAppAndroidId(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 184: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstantAppInstallerComponent();
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
        case 183: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstantAppResolverSettingsComponent();
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
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstantAppResolverComponent();
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
        case 181: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          deletePreloadsFileCache();
          paramParcel2.writeNoException();
          return true;
        case 180: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = canRequestPackageInstalls(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 179: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getSharedLibraries(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 178: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getInstallReason(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 177: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isPackageDeviceAdminOnAnyUser(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 176: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getChangedPackages(paramParcel1.readInt(), paramParcel1.readInt());
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
        case 175: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getSharedSystemSharedLibraryPackageName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 174: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getServicesSystemSharedLibraryPackageName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 173: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          bool10 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setUpdateAvailable((String)localObject1, bool10);
          paramParcel2.writeNoException();
          return true;
        case 172: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          bool10 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          paramInt1 = setRequiredForSystemUser((String)localObject1, bool10);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 171: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isInstantApp(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 170: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstantAppIcon(paramParcel1.readString(), paramParcel1.readInt());
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
        case 169: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = setInstantAppCookie(paramParcel1.readString(), paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 168: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstantAppCookie(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 167: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstantApps(paramParcel1.readInt());
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
        case 166: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPermissionControllerPackageName();
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 165: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isPermissionRevokedByPolicy(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 164: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          revokeDefaultPermissionsFromLuiApps(paramParcel1.createStringArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 163: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          grantDefaultPermissionsToActiveLuiApp(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 162: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          revokeDefaultPermissionsFromDisabledTelephonyDataServices(paramParcel1.createStringArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 161: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          grantDefaultPermissionsToEnabledTelephonyDataServices(paramParcel1.createStringArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 160: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          grantDefaultPermissionsToEnabledImsServices(paramParcel1.createStringArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 159: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          grantDefaultPermissionsToEnabledCarrierApps(paramParcel1.createStringArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 158: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          removeOnPermissionsChangeListener(IOnPermissionsChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 157: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          addOnPermissionsChangeListener(IOnPermissionsChangeListener.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 156: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (KeySet)KeySet.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject3;
          }
          paramInt1 = isPackageSignedByKeySetExactly((String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 155: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (KeySet)KeySet.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject4;
          }
          paramInt1 = isPackageSignedByKeySet((String)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 154: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getSigningKeySet(paramParcel1.readString());
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
        case 153: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getKeySetByAlias(paramParcel1.readString(), paramParcel1.readString());
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
        case 152: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getBlockUninstallForUser(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 151: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          bool10 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          paramInt1 = setBlockUninstallForUser((String)localObject1, bool10, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 150: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = getPackageInstaller();
          paramParcel2.writeNoException();
          paramParcel1 = localObject5;
          if (localObject1 != null) {
            paramParcel1 = ((IPackageInstaller)localObject1).asBinder();
          }
          paramParcel2.writeStrongBinder(paramParcel1);
          return true;
        case 149: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getApplicationHiddenSettingAsUser(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 148: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          bool10 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          paramInt1 = setApplicationHiddenSettingAsUser((String)localObject1, bool10, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 147: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isStorageLow();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 146: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isPermissionEnforced(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 145: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          bool10 = bool5;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setPermissionEnforced((String)localObject1, bool10);
          paramParcel2.writeNoException();
          return true;
        case 144: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isUpgrade();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 143: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isOnlyCoreApps();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 142: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isFirstBoot();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 141: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getVerifierDeviceIdentity();
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
        case 140: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getDefaultBrowserPackageName(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 139: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = setDefaultBrowserPackageName(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 138: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getAllIntentFilters(paramParcel1.readString());
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
        case 137: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getIntentFilterVerifications(paramParcel1.readString());
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
        case 136: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = updateIntentVerificationStatus(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 135: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getIntentVerificationStatus(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 134: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          verifyIntentFilter(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          return true;
        case 133: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          extendVerificationTimeout(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 132: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          verifyPendingInstall(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 131: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = installExistingPackageAsUser(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 130: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getInstallLocation();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 129: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = setInstallLocation(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 128: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PermissionInfo)PermissionInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          paramInt1 = addPermissionAsync(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 127: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = movePrimaryStorage(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 126: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = movePackage(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 125: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          unregisterMoveCallback(IPackageMoveObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 124: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          registerMoveCallback(IPackageMoveObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 123: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getMoveStatus(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 122: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PackageCleanItem)PackageCleanItem.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject7;
          }
          paramParcel1 = nextPackageToClean(paramParcel1);
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
        case 121: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          reconcileSecondaryDexFiles(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 120: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = runBackgroundDexoptJob(paramParcel1.createStringArrayList());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 119: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          forceDexOpt(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 118: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          dumpProfiles(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 117: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          localObject25 = paramParcel1.readString();
          bool10 = bool6;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          paramInt1 = performDexOptSecondary((String)localObject1, (String)localObject25, bool10);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 116: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject25 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          } else {
            bool10 = false;
          }
          localObject1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          if (paramParcel1.readInt() != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          paramInt1 = performDexOptMode((String)localObject25, bool10, (String)localObject1, bool1, bool2, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 115: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel2 = paramParcel1.readString();
          localObject1 = paramParcel1.readString();
          bool10 = bool7;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          registerDexModule(paramParcel2, (String)localObject1, bool10, IDexModuleRegisterCallback.Stub.asInterface(paramParcel1.readStrongBinder()));
          return true;
        case 114: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          notifyDexLoad(paramParcel1.readString(), paramParcel1.createStringArrayList(), paramParcel1.createStringArrayList(), paramParcel1.readString());
          return true;
        case 113: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          notifyPackageUse(paramParcel1.readString(), paramParcel1.readInt());
          return true;
        case 112: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          updatePackagesIfNeeded();
          paramParcel2.writeNoException();
          return true;
        case 111: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          performFstrimIfNeeded();
          paramParcel2.writeNoException();
          return true;
        case 110: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = hasSystemUidErrors();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 109: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          systemReady();
          paramParcel2.writeNoException();
          return true;
        case 108: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isSafeMode();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 107: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          enterSafeMode();
          paramParcel2.writeNoException();
          return true;
        case 106: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = hasSystemFeature(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 105: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getSystemAvailableFeatures();
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
        case 104: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getSystemSharedLibraryNames();
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 103: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          getPackageSizeInfo(paramParcel1.readString(), paramParcel1.readInt(), IPackageStatsObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 102: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          clearApplicationProfileData(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 101: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          clearApplicationUserData(paramParcel1.readString(), IPackageDataObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 100: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          deleteApplicationCacheFilesAsUser(paramParcel1.readString(), paramParcel1.readInt(), IPackageDataObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 99: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          deleteApplicationCacheFiles(paramParcel1.readString(), IPackageDataObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 98: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          long l = paramParcel1.readLong();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (IntentSender)IntentSender.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = localObject8) {
            break;
          }
          freeStorage((String)localObject1, l, paramInt1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 97: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          freeStorageAndNotify(paramParcel1.readString(), paramParcel1.readLong(), paramParcel1.readInt(), IPackageDataObserver.Stub.asInterface(paramParcel1.readStrongBinder()));
          paramParcel2.writeNoException();
          return true;
        case 96: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.readString();
          bool10 = bool8;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          setPackageStoppedState((String)localObject1, bool10, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 95: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          flushPackageRestrictionsAsUser(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 94: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          logAppProcessStartIfNeeded(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 93: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getApplicationEnabledSetting(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 92: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          setApplicationEnabledSetting(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 91: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject9;
          }
          paramInt1 = getComponentEnabledSetting((ComponentName)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 90: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject10;
          }
          setComponentEnabledSetting((ComponentName)localObject1, paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 89: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject11;
          }
          setHomeActivity((ComponentName)localObject1, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 88: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = new ArrayList();
          paramParcel1 = getHomeActivities((List)localObject1);
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
          paramParcel2.writeTypedList((List)localObject1);
          return true;
        case 87: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          restorePermissionGrants(paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 86: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPermissionGrantBackup(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 85: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          restoreIntentFilterVerification(paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 84: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getIntentFilterVerificationBackup(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 83: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          restoreDefaultApps(paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 82: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getDefaultAppsBackup(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 81: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          restorePreferredActivities(paramParcel1.createByteArray(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 80: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPreferredActivityBackup(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 79: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getSuspendedPackageAppExtras(paramParcel1.readString(), paramParcel1.readInt());
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
        case 78: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isPackageSuspendedForUser(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 77: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject17 = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          } else {
            bool10 = false;
          }
          if (paramParcel1.readInt() != 0) {
            localObject1 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {}
          for (localObject25 = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(paramParcel1);; localObject25 = localObject12) {
            break;
          }
          paramParcel1 = setPackagesSuspendedAsUser((String[])localObject17, bool10, (PersistableBundle)localObject1, (PersistableBundle)localObject25, paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 76: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          clearCrossProfileIntentFilters(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 75: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {}
          for (localObject1 = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);; localObject1 = localObject13) {
            break;
          }
          addCrossProfileIntentFilter((IntentFilter)localObject1, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 74: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          clearPackagePersistentPreferredActivities(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 73: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject25 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = localObject14;
          }
          addPersistentPreferredActivity((IntentFilter)localObject1, (ComponentName)localObject25, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 72: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject25 = new ArrayList();
          localObject1 = new ArrayList();
          paramInt1 = getPreferredActivities((List)localObject25, (List)localObject1, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          paramParcel2.writeTypedList((List)localObject25);
          paramParcel2.writeTypedList((List)localObject1);
          return true;
        case 71: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          clearPackagePreferredActivities(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 70: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          paramInt1 = paramParcel1.readInt();
          localObject17 = (ComponentName[])paramParcel1.createTypedArray(ComponentName.CREATOR);
          if (paramParcel1.readInt() != 0) {}
          for (localObject25 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);; localObject25 = localObject15) {
            break;
          }
          replacePreferredActivity((IntentFilter)localObject1, paramInt1, (ComponentName[])localObject17, (ComponentName)localObject25, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 69: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          paramInt1 = paramParcel1.readInt();
          localObject17 = (ComponentName[])paramParcel1.createTypedArray(ComponentName.CREATOR);
          if (paramParcel1.readInt() != 0) {}
          for (localObject25 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);; localObject25 = localObject16) {
            break;
          }
          addPreferredActivity((IntentFilter)localObject1, paramInt1, (ComponentName[])localObject17, (ComponentName)localObject25, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 68: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          localObject12 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject25 = (IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = null;
          }
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {}
          for (paramParcel1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);; paramParcel1 = (Parcel)localObject17) {
            break;
          }
          setLastChosenActivity((Intent)localObject1, (String)localObject12, paramInt1, (IntentFilter)localObject25, paramInt2, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 67: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject18;
          }
          paramParcel1 = getLastChosenActivity((Intent)localObject1, paramParcel1.readString(), paramParcel1.readInt());
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
        case 66: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          resetApplicationPreferences(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 65: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstallerPackageName(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 64: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (VersionedPackage)VersionedPackage.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject19;
          }
          deletePackageVersioned((VersionedPackage)localObject1, IPackageDeleteObserver2.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          deletePackageAsUser(paramParcel1.readString(), paramParcel1.readInt(), IPackageDeleteObserver.Stub.asInterface(paramParcel1.readStrongBinder()), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 62: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          setApplicationCategoryHint(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 61: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          setInstallerPackageName(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 60: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = paramParcel1.readInt();
          bool10 = bool9;
          if (paramParcel1.readInt() != 0) {
            bool10 = true;
          }
          finishPackageInstall(paramInt1, bool10);
          paramParcel2.writeNoException();
          return true;
        case 59: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = queryInstrumentation(paramParcel1.readString(), paramParcel1.readInt());
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
        case 58: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject20;
          }
          paramParcel1 = getInstrumentationInfo((ComponentName)localObject1, paramParcel1.readInt());
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
        case 57: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = queryContentProviders(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString());
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
        case 56: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          localObject1 = paramParcel1.createStringArrayList();
          paramParcel1 = paramParcel1.createTypedArrayList(ProviderInfo.CREATOR);
          querySyncProviders((List)localObject1, paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeStringList((List)localObject1);
          paramParcel2.writeTypedList(paramParcel1);
          return true;
        case 55: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = resolveContentProvider(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 54: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPersistentApplications(paramParcel1.readInt());
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
        case 53: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstalledApplications(paramParcel1.readInt(), paramParcel1.readInt());
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
        case 52: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPackagesHoldingPermissions(paramParcel1.createStringArray(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 51: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getInstalledPackages(paramParcel1.readInt(), paramParcel1.readInt());
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
        case 50: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject21;
          }
          paramParcel1 = queryIntentContentProviders((Intent)localObject1, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 49: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject22;
          }
          paramParcel1 = queryIntentServices((Intent)localObject1, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 48: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject23;
          }
          paramParcel1 = resolveService((Intent)localObject1, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 47: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject24;
          }
          paramParcel1 = queryIntentReceivers((Intent)localObject1, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 46: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          localObject12 = (Intent[])paramParcel1.createTypedArray(Intent.CREATOR);
          localObject17 = paramParcel1.createStringArray();
          if (paramParcel1.readInt() != 0) {
            localObject25 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramParcel1 = queryIntentActivityOptions((ComponentName)localObject1, (Intent[])localObject12, (String[])localObject17, (Intent)localObject25, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 45: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject26;
          }
          paramParcel1 = queryIntentActivities((Intent)localObject1, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 44: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject27;
          }
          paramInt1 = canForwardTo((Intent)localObject1, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 43: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject28;
          }
          paramParcel1 = findPersistentPreferredActivity((Intent)localObject1, paramParcel1.readInt());
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
        case 42: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject29;
          }
          paramParcel1 = resolveIntent((Intent)localObject1, paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 41: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getAppOpPermissionPackages(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 40: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isUidPrivileged(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 39: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getPrivateFlagsForUid(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 38: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getFlagsForUid(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 37: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getUidForSharedUser(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 36: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getNamesForUids(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 35: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getNameForUid(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeString(paramParcel1);
          return true;
        case 34: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPackagesForUid(paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 33: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getAllPackages();
          paramParcel2.writeNoException();
          paramParcel2.writeStringList(paramParcel1);
          return true;
        case 32: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = checkUidSignatures(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 31: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = checkSignatures(paramParcel1.readString(), paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 30: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isProtectedBroadcast(paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 29: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = shouldShowRequestPermissionRationale(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 28: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          updatePermissionFlagsForAllApps(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          updatePermissionFlags(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getPermissionFlags(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 25: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          resetRuntimePermissions();
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          revokeRuntimePermission(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          grantRuntimePermission(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          removePermission(paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (PermissionInfo)PermissionInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject30;
          }
          paramInt1 = addPermission(paramParcel1);
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 20: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = checkUidPermission(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 19: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = checkPermission(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 18: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject31;
          }
          paramParcel1 = getProviderInfo((ComponentName)localObject1, paramParcel1.readInt(), paramParcel1.readInt());
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
        case 17: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject32;
          }
          paramParcel1 = getServiceInfo((ComponentName)localObject1, paramParcel1.readInt(), paramParcel1.readInt());
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
        case 16: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject33;
          }
          paramParcel1 = getReceiverInfo((ComponentName)localObject1, paramParcel1.readInt(), paramParcel1.readInt());
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
        case 15: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = null;
          }
          if (paramParcel1.readInt() != 0) {
            localObject25 = (Intent)Intent.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject25 = localObject34;
          }
          paramInt1 = activitySupportsIntent((ComponentName)localObject1, (Intent)localObject25, paramParcel1.readString());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (ComponentName)ComponentName.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject35;
          }
          paramParcel1 = getActivityInfo((ComponentName)localObject1, paramParcel1.readInt(), paramParcel1.readInt());
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
        case 13: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getApplicationInfo(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 12: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getAllPermissionGroups(paramParcel1.readInt());
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
        case 11: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPermissionGroupInfo(paramParcel1.readString(), paramParcel1.readInt());
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
        case 10: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = queryPermissionsByGroup(paramParcel1.readString(), paramParcel1.readInt());
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
        case 9: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPermissionInfo(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
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
        case 8: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = canonicalToCurrentPackageNames(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 7: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = currentToCanonicalPackageNames(paramParcel1.createStringArray());
          paramParcel2.writeNoException();
          paramParcel2.writeStringArray(paramParcel1);
          return true;
        case 6: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPackageGids(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeIntArray(paramParcel1);
          return true;
        case 5: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = getPackageUid(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 4: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          if (paramParcel1.readInt() != 0) {
            localObject1 = (VersionedPackage)VersionedPackage.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject1 = localObject36;
          }
          paramParcel1 = getPackageInfoVersioned((VersionedPackage)localObject1, paramParcel1.readInt(), paramParcel1.readInt());
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
        case 3: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramParcel1 = getPackageInfo(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
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
        case 2: 
          paramParcel1.enforceInterface("android.content.pm.IPackageManager");
          paramInt1 = isPackageAvailable(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        }
        paramParcel1.enforceInterface("android.content.pm.IPackageManager");
        checkPackageStartable(paramParcel1.readString(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("android.content.pm.IPackageManager");
      return true;
    }
    
    private static class Proxy
      implements IPackageManager
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public boolean activitySupportsIntent(ComponentName paramComponentName, Intent paramIntent, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          mRemote.transact(15, localParcel1, localParcel2, 0);
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
      
      public void addCrossProfileIntentFilter(IntentFilter paramIntentFilter, String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntentFilter != null)
          {
            localParcel1.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void addOnPermissionsChangeListener(IOnPermissionsChangeListener paramIOnPermissionsChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIOnPermissionsChangeListener != null) {
            paramIOnPermissionsChangeListener = paramIOnPermissionsChangeListener.asBinder();
          } else {
            paramIOnPermissionsChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIOnPermissionsChangeListener);
          mRemote.transact(157, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean addPermission(PermissionInfo paramPermissionInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          boolean bool = true;
          if (paramPermissionInfo != null)
          {
            localParcel1.writeInt(1);
            paramPermissionInfo.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(21, localParcel1, localParcel2, 0);
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
      
      public boolean addPermissionAsync(PermissionInfo paramPermissionInfo)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          boolean bool = true;
          if (paramPermissionInfo != null)
          {
            localParcel1.writeInt(1);
            paramPermissionInfo.writeToParcel(localParcel1, 0);
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
      
      public void addPersistentPreferredActivity(IntentFilter paramIntentFilter, ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntentFilter != null)
          {
            localParcel1.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
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
          mRemote.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void addPreferredActivity(IntentFilter paramIntentFilter, int paramInt1, ComponentName[] paramArrayOfComponentName, ComponentName paramComponentName, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntentFilter != null)
          {
            localParcel1.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeTypedArray(paramArrayOfComponentName, 0);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
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
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public boolean canForwardTo(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          boolean bool = true;
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(44, localParcel1, localParcel2, 0);
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
      
      public boolean canRequestPackageInstalls(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(180, localParcel1, localParcel2, 0);
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
      
      public String[] canonicalToCurrentPackageNames(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfString = localParcel2.createStringArray();
          return paramArrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void checkPackageStartable(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public int checkPermission(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(19, localParcel1, localParcel2, 0);
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
      
      public int checkSignatures(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(31, localParcel1, localParcel2, 0);
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
      
      public int checkUidPermission(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(20, localParcel1, localParcel2, 0);
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
      
      public int checkUidSignatures(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void clearApplicationProfileData(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
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
      
      public void clearApplicationUserData(String paramString, IPackageDataObserver paramIPackageDataObserver, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          if (paramIPackageDataObserver != null) {
            paramString = paramIPackageDataObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void clearCrossProfileIntentFilters(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
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
      
      public void clearPackagePersistentPreferredActivities(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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
      
      public void clearPackagePreferredActivities(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] currentToCanonicalPackageNames(String[] paramArrayOfString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfString = localParcel2.createStringArray();
          return paramArrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void deleteApplicationCacheFiles(String paramString, IPackageDataObserver paramIPackageDataObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          if (paramIPackageDataObserver != null) {
            paramString = paramIPackageDataObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void deleteApplicationCacheFilesAsUser(String paramString, int paramInt, IPackageDataObserver paramIPackageDataObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramIPackageDataObserver != null) {
            paramString = paramIPackageDataObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
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
      
      public void deleteBundledPackageVersioned(VersionedPackage paramVersionedPackage, IPackageDeleteObserver2 paramIPackageDeleteObserver2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramVersionedPackage != null)
          {
            localParcel1.writeInt(1);
            paramVersionedPackage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIPackageDeleteObserver2 != null) {
            paramVersionedPackage = paramIPackageDeleteObserver2.asBinder();
          } else {
            paramVersionedPackage = null;
          }
          localParcel1.writeStrongBinder(paramVersionedPackage);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void deletePackageAsUser(String paramString, int paramInt1, IPackageDeleteObserver paramIPackageDeleteObserver, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          if (paramIPackageDeleteObserver != null) {
            paramString = paramIPackageDeleteObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void deletePackageVersioned(VersionedPackage paramVersionedPackage, IPackageDeleteObserver2 paramIPackageDeleteObserver2, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramVersionedPackage != null)
          {
            localParcel1.writeInt(1);
            paramVersionedPackage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramIPackageDeleteObserver2 != null) {
            paramVersionedPackage = paramIPackageDeleteObserver2.asBinder();
          } else {
            paramVersionedPackage = null;
          }
          localParcel1.writeStrongBinder(paramVersionedPackage);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
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
      
      public void deletePreloadsFileCache()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(181, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void dumpProfiles(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(118, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void enterSafeMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
      
      public void executeForceUninstall()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
      
      public void extendVerificationTimeout(int paramInt1, int paramInt2, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeLong(paramLong);
          mRemote.transact(133, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ResolveInfo findPersistentPreferredActivity(Intent paramIntent, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIntent = (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIntent = null;
          }
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void finishPackageInstall(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
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
      
      public void flushPackageRestrictionsAsUser(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
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
      
      public void forceDexOpt(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(119, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void freeStorage(String paramString, long paramLong, int paramInt, IntentSender paramIntentSender)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
          if (paramIntentSender != null)
          {
            localParcel1.writeInt(1);
            paramIntentSender.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(98, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void freeStorageAndNotify(String paramString, long paramLong, int paramInt, IPackageDataObserver paramIPackageDataObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt);
          if (paramIPackageDataObserver != null) {
            paramString = paramIPackageDataObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(97, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ActivityInfo getActivityInfo(ComponentName paramComponentName, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public ParceledListSlice getAllIntentFilters(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(138, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public List<String> getAllPackages()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ArrayList localArrayList = localParcel2.createStringArrayList();
          return localArrayList;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getAllPermissionGroups(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getAppOpPermissionPackages(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createStringArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getApplicationEnabledSetting(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(93, localParcel1, localParcel2, 0);
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
      
      public boolean getApplicationHiddenSettingAsUser(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(149, localParcel1, localParcel2, 0);
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
      
      public ApplicationInfo getApplicationInfo(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public IArtManager getArtManager()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(186, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IArtManager localIArtManager = IArtManager.Stub.asInterface(localParcel2.readStrongBinder());
          return localIArtManager;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean getBlockUninstallForUser(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(152, localParcel1, localParcel2, 0);
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
      
      public ChangedPackages getChangedPackages(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(176, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ChangedPackages localChangedPackages;
          if (localParcel2.readInt() != 0) {
            localChangedPackages = (ChangedPackages)ChangedPackages.CREATOR.createFromParcel(localParcel2);
          } else {
            localChangedPackages = null;
          }
          return localChangedPackages;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getComponentEnabledSetting(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          mRemote.transact(91, localParcel1, localParcel2, 0);
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
      
      public byte[] getDefaultAppsBackup(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(82, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getDefaultBrowserPackageName(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(140, localParcel1, localParcel2, 0);
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
      
      public int getFlagsForUid(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(38, localParcel1, localParcel2, 0);
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
      
      public CharSequence getHarmfulAppWarning(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(188, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(localParcel2);
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
      
      public ComponentName getHomeActivities(List<ResolveInfo> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(88, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ComponentName localComponentName;
          if (localParcel2.readInt() != 0) {
            localComponentName = (ComponentName)ComponentName.CREATOR.createFromParcel(localParcel2);
          } else {
            localComponentName = null;
          }
          localParcel2.readTypedList(paramList, ResolveInfo.CREATOR);
          return localComponentName;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getInstallLocation()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
      
      public int getInstallReason(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(178, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getInstalledApplications(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getInstalledPackages(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInstallerPackageName(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInstantAppAndroidId(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(185, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.readString();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] getInstantAppCookie(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(168, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createByteArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Bitmap getInstantAppIcon(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(170, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (Bitmap)Bitmap.CREATOR.createFromParcel(localParcel2);
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
      
      public ComponentName getInstantAppInstallerComponent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(184, localParcel1, localParcel2, 0);
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
      
      public ComponentName getInstantAppResolverComponent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(182, localParcel1, localParcel2, 0);
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
      
      public ComponentName getInstantAppResolverSettingsComponent()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(183, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getInstantApps(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(167, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public InstrumentationInfo getInstrumentationInfo(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (InstrumentationInfo)InstrumentationInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public byte[] getIntentFilterVerificationBackup(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getIntentFilterVerifications(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(137, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public int getIntentVerificationStatus(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(135, localParcel1, localParcel2, 0);
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
      
      public String getInterfaceDescriptor()
      {
        return "android.content.pm.IPackageManager";
      }
      
      public KeySet getKeySetByAlias(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(153, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (KeySet)KeySet.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ResolveInfo getLastChosenActivity(Intent paramIntent, String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIntent = (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIntent = null;
          }
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getMoveStatus(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(123, localParcel1, localParcel2, 0);
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
      
      public String getNameForUid(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(35, localParcel1, localParcel2, 0);
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
      
      public String[] getNamesForUids(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = localParcel2.createStringArray();
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int[] getPackageGids(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramString = localParcel2.createIntArray();
          return paramString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public PackageInfo getPackageInfo(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (PackageInfo)PackageInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public PackageInfo getPackageInfoVersioned(VersionedPackage paramVersionedPackage, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramVersionedPackage != null)
          {
            localParcel1.writeInt(1);
            paramVersionedPackage.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramVersionedPackage = (PackageInfo)PackageInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramVersionedPackage = null;
          }
          return paramVersionedPackage;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public IPackageInstaller getPackageInstaller()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(150, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IPackageInstaller localIPackageInstaller = IPackageInstaller.Stub.asInterface(localParcel2.readStrongBinder());
          return localIPackageInstaller;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void getPackageSizeInfo(String paramString, int paramInt, IPackageStatsObserver paramIPackageStatsObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          if (paramIPackageStatsObserver != null) {
            paramString = paramIPackageStatsObserver.asBinder();
          } else {
            paramString = null;
          }
          localParcel1.writeStrongBinder(paramString);
          mRemote.transact(103, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPackageUid(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getPackagesForUid(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(34, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getPackagesHoldingPermissions(String[] paramArrayOfString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramArrayOfString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramArrayOfString = null;
          }
          return paramArrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getPermissionControllerPackageName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(166, localParcel1, localParcel2, 0);
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
      
      public int getPermissionFlags(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
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
      
      public byte[] getPermissionGrantBackup(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(86, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public PermissionGroupInfo getPermissionGroupInfo(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (PermissionGroupInfo)PermissionGroupInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public PermissionInfo getPermissionInfo(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (PermissionInfo)PermissionInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice getPersistentApplications(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPreferredActivities(List<IntentFilter> paramList, List<ComponentName> paramList1, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          localParcel2.readTypedList(paramList, IntentFilter.CREATOR);
          localParcel2.readTypedList(paramList1, ComponentName.CREATOR);
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public byte[] getPreferredActivityBackup(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int getPrivateFlagsForUid(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(39, localParcel1, localParcel2, 0);
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
      
      public ProviderInfo getProviderInfo(ComponentName paramComponentName, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public ActivityInfo getReceiverInfo(ComponentName paramComponentName, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (ActivityInfo)ActivityInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public ServiceInfo getServiceInfo(ComponentName paramComponentName, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          mRemote.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramComponentName = (ServiceInfo)ServiceInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public String getServicesSystemSharedLibraryPackageName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(174, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice getSharedLibraries(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(179, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public String getSharedSystemSharedLibraryPackageName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(175, localParcel1, localParcel2, 0);
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
      
      public KeySet getSigningKeySet(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(154, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (KeySet)KeySet.CREATOR.createFromParcel(localParcel2);
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
      
      public PersistableBundle getSuspendedPackageAppExtras(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(79, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (PersistableBundle)PersistableBundle.CREATOR.createFromParcel(localParcel2);
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
      
      public ParceledListSlice getSystemAvailableFeatures()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(105, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParceledListSlice localParceledListSlice;
          if (localParcel2.readInt() != 0) {
            localParceledListSlice = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            localParceledListSlice = null;
          }
          return localParceledListSlice;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] getSystemSharedLibraryNames()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(104, localParcel1, localParcel2, 0);
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
      
      public String getSystemTextClassifierPackageName()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(191, localParcel1, localParcel2, 0);
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
      
      public int getUidForSharedUser(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(37, localParcel1, localParcel2, 0);
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
      
      public VerifierDeviceIdentity getVerifierDeviceIdentity()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(141, localParcel1, localParcel2, 0);
          localParcel2.readException();
          VerifierDeviceIdentity localVerifierDeviceIdentity;
          if (localParcel2.readInt() != 0) {
            localVerifierDeviceIdentity = (VerifierDeviceIdentity)VerifierDeviceIdentity.CREATOR.createFromParcel(localParcel2);
          } else {
            localVerifierDeviceIdentity = null;
          }
          return localVerifierDeviceIdentity;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantDefaultPermissionsToActiveLuiApp(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(163, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantDefaultPermissionsToEnabledCarrierApps(String[] paramArrayOfString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(159, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantDefaultPermissionsToEnabledImsServices(String[] paramArrayOfString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(160, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void grantDefaultPermissionsToEnabledTelephonyDataServices(String[] paramArrayOfString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt);
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
      
      public void grantRuntimePermission(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean hasSigningCertificate(String paramString, byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(189, localParcel1, localParcel2, 0);
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
      
      public boolean hasSystemFeature(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(106, localParcel1, localParcel2, 0);
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
      
      public boolean hasSystemUidErrors()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(110, localParcel1, localParcel2, 0);
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
      
      public boolean hasUidSigningCertificate(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt2);
          paramArrayOfByte = mRemote;
          boolean bool = false;
          paramArrayOfByte.transact(190, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
      
      public int installExistingPackageAsUser(String paramString, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(131, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          return paramInt1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isFirstBoot()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(142, localParcel1, localParcel2, 0);
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
      
      public boolean isInApp2sdBlacklist(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(196, localParcel1, localParcel2, 0);
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
      
      public boolean isInstantApp(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(171, localParcel1, localParcel2, 0);
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
      
      public boolean isNeedProtectSecPreloadedApps()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(194, localParcel1, localParcel2, 0);
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
      
      public boolean isOnlyCoreApps()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(143, localParcel1, localParcel2, 0);
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
      
      public boolean isPackageAvailable(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(2, localParcel1, localParcel2, 0);
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
      
      public boolean isPackageDeviceAdminOnAnyUser(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(177, localParcel1, localParcel2, 0);
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
      
      public boolean isPackageSignedByKeySet(String paramString, KeySet paramKeySet)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramKeySet != null)
          {
            localParcel1.writeInt(1);
            paramKeySet.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(155, localParcel1, localParcel2, 0);
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
      
      public boolean isPackageSignedByKeySetExactly(String paramString, KeySet paramKeySet)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          boolean bool = true;
          if (paramKeySet != null)
          {
            localParcel1.writeInt(1);
            paramKeySet.writeToParcel(localParcel1, 0);
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
      
      public boolean isPackageStateProtected(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(192, localParcel1, localParcel2, 0);
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
      
      public boolean isPackageSuspendedForUser(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(78, localParcel1, localParcel2, 0);
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
      
      public boolean isPermissionEnforced(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(146, localParcel1, localParcel2, 0);
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
      
      public boolean isPermissionRevokedByPolicy(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(165, localParcel1, localParcel2, 0);
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
      
      public boolean isProtectedBroadcast(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(30, localParcel1, localParcel2, 0);
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
      
      public boolean isSafeMode()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(108, localParcel1, localParcel2, 0);
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
      
      public boolean isStorageLow()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(147, localParcel1, localParcel2, 0);
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
      
      public boolean isSupportUninstallablePreloadedApps()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(193, localParcel1, localParcel2, 0);
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
      
      public boolean isUidPrivileged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(40, localParcel1, localParcel2, 0);
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
      
      public boolean isUpgrade()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(144, localParcel1, localParcel2, 0);
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
      
      public void logAppProcessStartIfNeeded(String paramString1, int paramInt1, String paramString2, String paramString3, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString2);
          localParcel1.writeString(paramString3);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(94, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public int movePackage(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          mRemote.transact(126, localParcel1, localParcel2, 0);
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
      
      public int movePrimaryStorage(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(127, localParcel1, localParcel2, 0);
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
      
      public PackageCleanItem nextPackageToClean(PackageCleanItem paramPackageCleanItem)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramPackageCleanItem != null)
          {
            localParcel1.writeInt(1);
            paramPackageCleanItem.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(122, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramPackageCleanItem = (PackageCleanItem)PackageCleanItem.CREATOR.createFromParcel(localParcel2);
          } else {
            paramPackageCleanItem = null;
          }
          return paramPackageCleanItem;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notifyDexLoad(String paramString1, List<String> paramList1, List<String> paramList2, String paramString2)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel.writeString(paramString1);
          localParcel.writeStringList(paramList1);
          localParcel.writeStringList(paramList2);
          localParcel.writeString(paramString2);
          mRemote.transact(114, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void notifyPackageUse(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel.writeString(paramString);
          localParcel.writeInt(paramInt);
          mRemote.transact(113, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public boolean performDexOptMode(String paramString1, boolean paramBoolean1, String paramString2, boolean paramBoolean2, boolean paramBoolean3, String paramString3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramBoolean1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramBoolean2);
          localParcel1.writeInt(paramBoolean3);
          localParcel1.writeString(paramString3);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(116, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean1 = localParcel2.readInt();
          if (paramBoolean1) {
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
      
      public boolean performDexOptSecondary(String paramString1, String paramString2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramBoolean);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(117, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public void performFstrimIfNeeded()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
      
      public ParceledListSlice queryContentProviders(String paramString1, int paramInt1, int paramInt2, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString2);
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString1 = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramString1 = null;
          }
          return paramString1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice queryInstrumentation(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public ParceledListSlice queryIntentActivities(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIntent = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIntent = null;
          }
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice queryIntentActivityOptions(ComponentName paramComponentName, Intent[] paramArrayOfIntent, String[] paramArrayOfString, Intent paramIntent, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeTypedArray(paramArrayOfIntent, 0);
          localParcel1.writeStringArray(paramArrayOfString);
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(46, localParcel1, localParcel2, 0);
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
      
      public ParceledListSlice queryIntentContentProviders(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIntent = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIntent = null;
          }
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice queryIntentReceivers(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIntent = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIntent = null;
          }
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice queryIntentServices(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIntent = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIntent = null;
          }
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParceledListSlice queryPermissionsByGroup(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ParceledListSlice)ParceledListSlice.CREATOR.createFromParcel(localParcel2);
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
      
      public void querySyncProviders(List<String> paramList, List<ProviderInfo> paramList1)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringList(paramList);
          localParcel1.writeTypedList(paramList1);
          mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          localParcel2.readStringList(paramList);
          localParcel2.readTypedList(paramList1, ProviderInfo.CREATOR);
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void reconcileSecondaryDexFiles(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(121, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void refreshApp2sdBlacklist()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(197, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void refreshAppAspectRatio()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(200, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void refreshAppSupportNotchList()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(202, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void registerDexModule(String paramString1, String paramString2, boolean paramBoolean, IDexModuleRegisterCallback paramIDexModuleRegisterCallback)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel.writeString(paramString1);
          localParcel.writeString(paramString2);
          localParcel.writeInt(paramBoolean);
          if (paramIDexModuleRegisterCallback != null) {
            paramString1 = paramIDexModuleRegisterCallback.asBinder();
          } else {
            paramString1 = null;
          }
          localParcel.writeStrongBinder(paramString1);
          mRemote.transact(115, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void registerMoveCallback(IPackageMoveObserver paramIPackageMoveObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIPackageMoveObserver != null) {
            paramIPackageMoveObserver = paramIPackageMoveObserver.asBinder();
          } else {
            paramIPackageMoveObserver = null;
          }
          localParcel1.writeStrongBinder(paramIPackageMoveObserver);
          mRemote.transact(124, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void removeOnPermissionsChangeListener(IOnPermissionsChangeListener paramIOnPermissionsChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIOnPermissionsChangeListener != null) {
            paramIOnPermissionsChangeListener = paramIOnPermissionsChangeListener.asBinder();
          } else {
            paramIOnPermissionsChangeListener = null;
          }
          localParcel1.writeStrongBinder(paramIOnPermissionsChangeListener);
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
      
      public void removePermission(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void replacePreferredActivity(IntentFilter paramIntentFilter, int paramInt1, ComponentName[] paramArrayOfComponentName, ComponentName paramComponentName, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntentFilter != null)
          {
            localParcel1.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeTypedArray(paramArrayOfComponentName, 0);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          mRemote.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resetApplicationPreferences(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void resetRuntimePermissions()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ProviderInfo resolveContentProvider(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramString = (ProviderInfo)ProviderInfo.CREATOR.createFromParcel(localParcel2);
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
      
      public ResolveInfo resolveIntent(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIntent = (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIntent = null;
          }
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ResolveInfo resolveService(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0) {
            paramIntent = (ResolveInfo)ResolveInfo.CREATOR.createFromParcel(localParcel2);
          } else {
            paramIntent = null;
          }
          return paramIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void restoreDefaultApps(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
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
      
      public void restoreIntentFilterVerification(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
          mRemote.transact(85, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void restorePermissionGrants(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
          mRemote.transact(87, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void restorePreferredActivities(byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void revokeDefaultPermissionsFromDisabledTelephonyDataServices(String[] paramArrayOfString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt);
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
      
      public void revokeDefaultPermissionsFromLuiApps(String[] paramArrayOfString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(164, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void revokeRuntimePermission(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean runBackgroundDexoptJob(List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringList(paramList);
          paramList = mRemote;
          boolean bool = false;
          paramList.transact(120, localParcel1, localParcel2, 0);
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
      
      public void setApplicationCategoryHint(String paramString1, int paramInt, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString2);
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
      
      public void setApplicationEnabledSetting(String paramString1, int paramInt1, int paramInt2, int paramInt3, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeString(paramString2);
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
      
      public boolean setApplicationHiddenSettingAsUser(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(148, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public boolean setBlockUninstallForUser(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(151, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public void setComponentEnabledSetting(ComponentName paramComponentName, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          localParcel1.writeInt(paramInt3);
          mRemote.transact(90, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setDefaultBrowserPackageName(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(139, localParcel1, localParcel2, 0);
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
      
      public void setFillNotchRegion(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(201, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setHarmfulAppWarning(String paramString, CharSequence paramCharSequence, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          if (paramCharSequence != null)
          {
            localParcel1.writeInt(1);
            TextUtils.writeToParcel(paramCharSequence, localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(187, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setHomeActivity(ComponentName paramComponentName, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
          mRemote.transact(89, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean setInstallLocation(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt);
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(129, localParcel1, localParcel2, 0);
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
      
      public void setInstallerPackageName(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
      
      public boolean setInstantAppCookie(String paramString, byte[] paramArrayOfByte, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeByteArray(paramArrayOfByte);
          localParcel1.writeInt(paramInt);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(169, localParcel1, localParcel2, 0);
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
      
      public void setLastChosenActivity(Intent paramIntent, String paramString, int paramInt1, IntentFilter paramIntentFilter, int paramInt2, ComponentName paramComponentName)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIntent != null)
          {
            localParcel1.writeInt(1);
            paramIntent.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          if (paramIntentFilter != null)
          {
            localParcel1.writeInt(1);
            paramIntentFilter.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt2);
          if (paramComponentName != null)
          {
            localParcel1.writeInt(1);
            paramComponentName.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setOverrideMaxAspect(String paramString, float paramFloat, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeFloat(paramFloat);
          localParcel1.writeInt(paramInt);
          mRemote.transact(199, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPackageStoppedState(String paramString, boolean paramBoolean, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          localParcel1.writeInt(paramInt);
          mRemote.transact(96, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String[] setPackagesSuspendedAsUser(String[] paramArrayOfString, boolean paramBoolean, PersistableBundle paramPersistableBundle1, PersistableBundle paramPersistableBundle2, String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeStringArray(paramArrayOfString);
          localParcel1.writeInt(paramBoolean);
          if (paramPersistableBundle1 != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramPersistableBundle2 != null)
          {
            localParcel1.writeInt(1);
            paramPersistableBundle2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfString = localParcel2.createStringArray();
          return paramArrayOfString;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setPermissionEnforced(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
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
      
      public boolean setRequiredForSystemUser(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(172, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramBoolean = localParcel2.readInt();
          if (paramBoolean) {
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
      
      public void setUpdateAvailable(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(173, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean shouldShowRequestPermissionRationale(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          paramString1 = mRemote;
          boolean bool = false;
          paramString1.transact(29, localParcel1, localParcel2, 0);
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
      
      public void systemReady()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
      
      public void unregisterMoveCallback(IPackageMoveObserver paramIPackageMoveObserver)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          if (paramIPackageMoveObserver != null) {
            paramIPackageMoveObserver = paramIPackageMoveObserver.asBinder();
          } else {
            paramIPackageMoveObserver = null;
          }
          localParcel1.writeStrongBinder(paramIPackageMoveObserver);
          mRemote.transact(125, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean updateIntentVerificationStatus(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          paramString = mRemote;
          boolean bool = false;
          paramString.transact(136, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramInt1 = localParcel2.readInt();
          if (paramInt1 != 0) {
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
      
      public void updatePackagesIfNeeded()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
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
      
      public void updatePermissionFlags(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
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
      
      public void updatePermissionFlagsForAllApps(int paramInt1, int paramInt2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(28, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void verifyIntentFilter(int paramInt1, int paramInt2, List<String> paramList)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeStringList(paramList);
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
      
      public void verifyPendingInstall(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("android.content.pm.IPackageManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(132, localParcel1, localParcel2, 0);
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
