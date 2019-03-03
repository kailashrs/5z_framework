package android.content.pm;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public abstract class PackageManagerInternal
{
  public static final int PACKAGE_BROWSER = 4;
  public static final int PACKAGE_INSTALLER = 2;
  public static final int PACKAGE_SETUP_WIZARD = 1;
  public static final int PACKAGE_SYSTEM = 0;
  public static final int PACKAGE_SYSTEM_TEXT_CLASSIFIER = 5;
  public static final int PACKAGE_VERIFIER = 3;
  
  public PackageManagerInternal() {}
  
  public abstract void addIsolatedUid(int paramInt1, int paramInt2);
  
  public abstract boolean canAccessComponent(int paramInt1, ComponentName paramComponentName, int paramInt2);
  
  public abstract boolean canAccessInstantApps(int paramInt1, int paramInt2);
  
  public abstract boolean filterAppAccess(PackageParser.Package paramPackage, int paramInt1, int paramInt2);
  
  public abstract ActivityInfo getActivityInfo(ComponentName paramComponentName, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract ApplicationInfo getApplicationInfo(String paramString, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract ComponentName getDefaultHomeActivity(int paramInt);
  
  public abstract PackageParser.Package getDisabledPackage(String paramString);
  
  public abstract ComponentName getHomeActivitiesAsUser(List<ResolveInfo> paramList, int paramInt);
  
  public abstract String getInstantAppPackageName(int paramInt);
  
  public abstract String getKnownPackageName(int paramInt1, int paramInt2);
  
  public abstract String getNameForUid(int paramInt);
  
  public abstract List<PackageInfo> getOverlayPackages(int paramInt);
  
  public abstract PackageParser.Package getPackage(String paramString);
  
  public abstract PackageInfo getPackageInfo(String paramString, int paramInt1, int paramInt2, int paramInt3);
  
  public PackageList getPackageList()
  {
    return getPackageList(null);
  }
  
  public abstract PackageList getPackageList(PackageListObserver paramPackageListObserver);
  
  public abstract int getPackageTargetSdkVersion(String paramString);
  
  public abstract int getPackageUid(String paramString, int paramInt1, int paramInt2);
  
  public abstract int getPermissionFlagsTEMP(String paramString1, String paramString2, int paramInt);
  
  public abstract String getSetupWizardPackageName();
  
  public abstract String getSuspendedDialogMessage(String paramString, int paramInt);
  
  public abstract Bundle getSuspendedPackageLauncherExtras(String paramString, int paramInt);
  
  public abstract String getSuspendingPackage(String paramString, int paramInt);
  
  public abstract List<String> getTargetPackageNames(int paramInt);
  
  public abstract int getUidTargetSdkVersion(int paramInt);
  
  public abstract void grantDefaultPermissionsToDefaultDialerApp(String paramString, int paramInt);
  
  public abstract void grantDefaultPermissionsToDefaultSimCallManager(String paramString, int paramInt);
  
  public abstract void grantDefaultPermissionsToDefaultSmsApp(String paramString, int paramInt);
  
  public abstract void grantDefaultPermissionsToDefaultUseOpenWifiApp(String paramString, int paramInt);
  
  public abstract void grantEphemeralAccess(int paramInt1, Intent paramIntent, int paramInt2, int paramInt3);
  
  public abstract void grantRuntimePermission(String paramString1, String paramString2, int paramInt, boolean paramBoolean);
  
  public abstract boolean hasInstantApplicationMetadata(String paramString, int paramInt);
  
  public abstract boolean hasSignatureCapability(int paramInt1, int paramInt2, @PackageParser.SigningDetails.CertCapabilities int paramInt3);
  
  public abstract boolean isDataRestoreSafe(Signature paramSignature, String paramString);
  
  public abstract boolean isDataRestoreSafe(byte[] paramArrayOfByte, String paramString);
  
  public abstract boolean isInstantApp(String paramString, int paramInt);
  
  public abstract boolean isInstantAppInstallerComponent(ComponentName paramComponentName);
  
  public abstract boolean isLegacySystemApp(PackageParser.Package paramPackage);
  
  public abstract boolean isPackageDataProtected(int paramInt, String paramString);
  
  public abstract boolean isPackageEphemeral(int paramInt, String paramString);
  
  public abstract boolean isPackagePersistent(String paramString);
  
  public abstract boolean isPackageStateProtected(String paramString, int paramInt);
  
  public abstract boolean isPackageSuspended(String paramString, int paramInt);
  
  public abstract boolean isPermissionsReviewRequired(String paramString, int paramInt);
  
  public abstract boolean isResolveActivityComponent(ComponentInfo paramComponentInfo);
  
  public abstract void notifyPackageUse(String paramString, int paramInt);
  
  public abstract void pruneInstantApps();
  
  public abstract List<ResolveInfo> queryIntentActivities(Intent paramIntent, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract List<ResolveInfo> queryIntentServices(Intent paramIntent, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void removeIsolatedUid(int paramInt);
  
  public abstract void removePackageListObserver(PackageListObserver paramPackageListObserver);
  
  public abstract void requestInstantAppResolutionPhaseTwo(AuxiliaryResolveInfo paramAuxiliaryResolveInfo, Intent paramIntent, String paramString1, String paramString2, Bundle paramBundle, int paramInt);
  
  public abstract ProviderInfo resolveContentProvider(String paramString, int paramInt1, int paramInt2);
  
  public abstract ResolveInfo resolveIntent(Intent paramIntent, String paramString, int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3);
  
  public abstract ResolveInfo resolveService(Intent paramIntent, String paramString, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract void revokeRuntimePermission(String paramString1, String paramString2, int paramInt, boolean paramBoolean);
  
  public abstract void setDeviceAndProfileOwnerPackages(int paramInt, String paramString, SparseArray<String> paramSparseArray);
  
  public abstract void setDialerAppPackagesProvider(PackagesProvider paramPackagesProvider);
  
  public abstract boolean setEnabledOverlayPackages(int paramInt, String paramString, List<String> paramList);
  
  public abstract void setExternalSourcesPolicy(ExternalSourcesPolicy paramExternalSourcesPolicy);
  
  public abstract void setKeepUninstalledPackages(List<String> paramList);
  
  public abstract void setLocationPackagesProvider(PackagesProvider paramPackagesProvider);
  
  public abstract void setSimCallManagerPackagesProvider(PackagesProvider paramPackagesProvider);
  
  public abstract void setSmsAppPackagesProvider(PackagesProvider paramPackagesProvider);
  
  public abstract void setSyncAdapterPackagesprovider(SyncAdapterPackagesProvider paramSyncAdapterPackagesProvider);
  
  public abstract void setUseOpenWifiAppPackagesProvider(PackagesProvider paramPackagesProvider);
  
  public abstract void setVoiceInteractionPackagesProvider(PackagesProvider paramPackagesProvider);
  
  public abstract void updatePermissionFlagsTEMP(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3);
  
  public abstract boolean wasPackageEverLaunched(String paramString, int paramInt);
  
  public static abstract interface ExternalSourcesPolicy
  {
    public static final int USER_BLOCKED = 1;
    public static final int USER_DEFAULT = 2;
    public static final int USER_TRUSTED = 0;
    
    public abstract int getPackageTrustedToInstallApps(String paramString, int paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface KnownPackage {}
  
  public static abstract interface PackageListObserver
  {
    public abstract void onPackageAdded(String paramString);
    
    public abstract void onPackageRemoved(String paramString);
  }
  
  public static abstract interface PackagesProvider
  {
    public abstract String[] getPackages(int paramInt);
  }
  
  public static abstract interface SyncAdapterPackagesProvider
  {
    public abstract String[] getPackages(String paramString, int paramInt);
  }
}
