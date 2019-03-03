package android.content.pm;

import android.os.BaseBundle;
import android.os.Build.FEATURES;
import android.os.PersistableBundle;
import android.util.ArraySet;
import com.android.internal.util.ArrayUtils;
import java.util.Arrays;
import java.util.Objects;

public class PackageUserState
{
  public int appLinkGeneration;
  public int categoryHint = -1;
  public long ceDataInode;
  public String dialogMessage;
  public ArraySet<String> disabledComponents;
  public int domainVerificationStatus;
  public int enabled;
  public ArraySet<String> enabledComponents;
  public boolean fillNotchRegion = false;
  public String harmfulAppWarning;
  public boolean hidden;
  public int installReason;
  public boolean installed;
  public boolean instantApp;
  public String lastDisableAppCaller;
  public boolean notLaunched;
  public String[] overlayPaths;
  public float overrideMaxAspect;
  public boolean stopped;
  public boolean suspended;
  public PersistableBundle suspendedAppExtras;
  public PersistableBundle suspendedLauncherExtras;
  public String suspendingPackage;
  public boolean virtualPreload;
  
  public PackageUserState()
  {
    installed = true;
    hidden = false;
    suspended = false;
    enabled = 0;
    domainVerificationStatus = 0;
    installReason = 0;
    if (Build.FEATURES.ENABLE_APP_SCALING) {
      overrideMaxAspect = ApplicationInfo.OVERRIDE_MAX_ASPECT_DEFAULT;
    }
  }
  
  public PackageUserState(PackageUserState paramPackageUserState)
  {
    ceDataInode = ceDataInode;
    installed = installed;
    stopped = stopped;
    notLaunched = notLaunched;
    hidden = hidden;
    suspended = suspended;
    suspendingPackage = suspendingPackage;
    dialogMessage = dialogMessage;
    suspendedAppExtras = suspendedAppExtras;
    suspendedLauncherExtras = suspendedLauncherExtras;
    instantApp = instantApp;
    virtualPreload = virtualPreload;
    enabled = enabled;
    lastDisableAppCaller = lastDisableAppCaller;
    domainVerificationStatus = domainVerificationStatus;
    appLinkGeneration = appLinkGeneration;
    categoryHint = categoryHint;
    installReason = installReason;
    disabledComponents = ArrayUtils.cloneOrNull(disabledComponents);
    enabledComponents = ArrayUtils.cloneOrNull(enabledComponents);
    String[] arrayOfString;
    if (overlayPaths == null) {
      arrayOfString = null;
    } else {
      arrayOfString = (String[])Arrays.copyOf(overlayPaths, overlayPaths.length);
    }
    overlayPaths = arrayOfString;
    harmfulAppWarning = harmfulAppWarning;
    if (Build.FEATURES.ENABLE_APP_SCALING) {
      overrideMaxAspect = overrideMaxAspect;
    }
    if (Build.FEATURES.ENABLE_NOTCH_UI) {
      fillNotchRegion = fillNotchRegion;
    }
  }
  
  public final boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof PackageUserState)) {
      return false;
    }
    paramObject = (PackageUserState)paramObject;
    if (ceDataInode != ceDataInode) {
      return false;
    }
    if (installed != installed) {
      return false;
    }
    if (stopped != stopped) {
      return false;
    }
    if (notLaunched != notLaunched) {
      return false;
    }
    if (hidden != hidden) {
      return false;
    }
    if (suspended != suspended) {
      return false;
    }
    if (suspended) {
      if ((suspendingPackage != null) && (suspendingPackage.equals(suspendingPackage)))
      {
        if (!Objects.equals(dialogMessage, dialogMessage)) {
          return false;
        }
        if (!BaseBundle.kindofEquals(suspendedAppExtras, suspendedAppExtras)) {
          return false;
        }
        if (!BaseBundle.kindofEquals(suspendedLauncherExtras, suspendedLauncherExtras)) {
          return false;
        }
      }
      else
      {
        return false;
      }
    }
    if (instantApp != instantApp) {
      return false;
    }
    if (virtualPreload != virtualPreload) {
      return false;
    }
    if (enabled != enabled) {
      return false;
    }
    if (((lastDisableAppCaller == null) && (lastDisableAppCaller != null)) || ((lastDisableAppCaller != null) && (!lastDisableAppCaller.equals(lastDisableAppCaller)))) {
      return false;
    }
    if (domainVerificationStatus != domainVerificationStatus) {
      return false;
    }
    if (appLinkGeneration != appLinkGeneration) {
      return false;
    }
    if (categoryHint != categoryHint) {
      return false;
    }
    if (installReason != installReason) {
      return false;
    }
    if (((disabledComponents == null) && (disabledComponents != null)) || ((disabledComponents != null) && (disabledComponents == null))) {
      return false;
    }
    int i;
    if (disabledComponents != null)
    {
      if (disabledComponents.size() != disabledComponents.size()) {
        return false;
      }
      for (i = disabledComponents.size() - 1; i >= 0; i--) {
        if (!disabledComponents.contains(disabledComponents.valueAt(i))) {
          return false;
        }
      }
    }
    if (((enabledComponents == null) && (enabledComponents != null)) || ((enabledComponents != null) && (enabledComponents == null))) {
      return false;
    }
    if (enabledComponents != null)
    {
      if (enabledComponents.size() != enabledComponents.size()) {
        return false;
      }
      for (i = enabledComponents.size() - 1; i >= 0; i--) {
        if (!enabledComponents.contains(enabledComponents.valueAt(i))) {
          return false;
        }
      }
    }
    return ((harmfulAppWarning != null) || (harmfulAppWarning == null)) && ((harmfulAppWarning == null) || (harmfulAppWarning.equals(harmfulAppWarning)));
  }
  
  public boolean isAvailable(int paramInt)
  {
    boolean bool = false;
    int i;
    if ((0x400000 & paramInt) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if ((paramInt & 0x2000) != 0) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    if ((i == 0) && ((!installed) || ((hidden) && (paramInt == 0)))) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public boolean isEnabled(ComponentInfo paramComponentInfo, int paramInt)
  {
    if ((paramInt & 0x200) != 0) {
      return true;
    }
    int i = enabled;
    if (i != 0) {
      switch (i)
      {
      default: 
        break;
      case 4: 
        if ((0x8000 & paramInt) == 0) {
          return false;
        }
        break;
      case 2: 
      case 3: 
        return false;
      }
    }
    if (!applicationInfo.enabled) {
      return false;
    }
    if (ArrayUtils.contains(enabledComponents, name)) {
      return true;
    }
    if (ArrayUtils.contains(disabledComponents, name)) {
      return false;
    }
    return enabled;
  }
  
  public boolean isMatch(ComponentInfo paramComponentInfo, int paramInt)
  {
    boolean bool1 = applicationInfo.isSystemApp();
    boolean bool2 = true;
    int i;
    if ((0x402000 & paramInt) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if ((!isAvailable(paramInt)) && ((!bool1) || (i == 0))) {
      return false;
    }
    if (!isEnabled(paramComponentInfo, paramInt)) {
      return false;
    }
    if (((0x100000 & paramInt) != 0) && (!bool1)) {
      return false;
    }
    if (((0x40000 & paramInt) != 0) && (!directBootAware)) {
      i = 1;
    } else {
      i = 0;
    }
    if (((0x80000 & paramInt) != 0) && (directBootAware)) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    bool1 = bool2;
    if (i == 0) {
      if (paramInt != 0) {
        bool1 = bool2;
      } else {
        bool1 = false;
      }
    }
    return bool1;
  }
}
