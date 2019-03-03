package android.content.pm;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.FEATURES;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Printer;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import com.android.internal.util.ArrayUtils;
import com.android.server.SystemConfig;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.Collator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

public class ApplicationInfo
  extends PackageItemInfo
  implements Parcelable
{
  public static final int CATEGORY_AUDIO = 1;
  public static final int CATEGORY_GAME = 0;
  public static final int CATEGORY_IMAGE = 3;
  public static final int CATEGORY_MAPS = 6;
  public static final int CATEGORY_NEWS = 5;
  public static final int CATEGORY_PRODUCTIVITY = 7;
  public static final int CATEGORY_SOCIAL = 4;
  public static final int CATEGORY_UNDEFINED = -1;
  public static final int CATEGORY_VIDEO = 2;
  public static final Parcelable.Creator<ApplicationInfo> CREATOR = new Parcelable.Creator()
  {
    public ApplicationInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ApplicationInfo(paramAnonymousParcel, null);
    }
    
    public ApplicationInfo[] newArray(int paramAnonymousInt)
    {
      return new ApplicationInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_ALLOW_BACKUP = 32768;
  public static final int FLAG_ALLOW_CLEAR_USER_DATA = 64;
  public static final int FLAG_ALLOW_TASK_REPARENTING = 32;
  public static final int FLAG_ASUS_APPS = 8;
  public static final int FLAG_ASUS_DIL = 4;
  public static final int FLAG_ASUS_FORECE_UNINSTALLED = 32;
  public static final int FLAG_ASUS_TWINAPPS_EXCLUSION = 16;
  public static final int FLAG_DEBUGGABLE = 2;
  public static final int FLAG_EXTERNAL_STORAGE = 262144;
  public static final int FLAG_EXTRACT_NATIVE_LIBS = 268435456;
  public static final int FLAG_FACTORY_TEST = 16;
  public static final int FLAG_FULL_BACKUP_ONLY = 67108864;
  public static final int FLAG_HARDWARE_ACCELERATED = 536870912;
  public static final int FLAG_HAS_CODE = 4;
  public static final int FLAG_INSTALLED = 8388608;
  public static final int FLAG_IS_DATA_ONLY = 16777216;
  @Deprecated
  public static final int FLAG_IS_GAME = 33554432;
  public static final int FLAG_KILL_AFTER_RESTORE = 65536;
  public static final int FLAG_LARGE_HEAP = 1048576;
  public static final int FLAG_MULTIARCH = Integer.MIN_VALUE;
  public static final int FLAG_PERSISTENT = 8;
  public static final int FLAG_RESIZEABLE_FOR_SCREENS = 4096;
  public static final int FLAG_RESTORE_ANY_VERSION = 131072;
  public static final int FLAG_SEC_PART_APP = 2;
  public static final int FLAG_STICKY_PRELOADED_APP = 1;
  public static final int FLAG_STOPPED = 2097152;
  public static final int FLAG_SUPPORTS_LARGE_SCREENS = 2048;
  public static final int FLAG_SUPPORTS_NORMAL_SCREENS = 1024;
  public static final int FLAG_SUPPORTS_RTL = 4194304;
  public static final int FLAG_SUPPORTS_SCREEN_DENSITIES = 8192;
  public static final int FLAG_SUPPORTS_SMALL_SCREENS = 512;
  public static final int FLAG_SUPPORTS_XLARGE_SCREENS = 524288;
  public static final int FLAG_SUSPENDED = 1073741824;
  public static final int FLAG_SYSTEM = 1;
  public static final int FLAG_TEST_ONLY = 256;
  public static final int FLAG_UPDATED_SYSTEM_APP = 128;
  public static final int FLAG_USES_CLEARTEXT_TRAFFIC = 134217728;
  public static final int FLAG_VM_SAFE_MODE = 16384;
  public static final int HIDDEN_API_ENFORCEMENT_BLACK = 3;
  public static final int HIDDEN_API_ENFORCEMENT_DARK_GREY_AND_BLACK = 2;
  public static final int HIDDEN_API_ENFORCEMENT_DEFAULT = -1;
  public static final int HIDDEN_API_ENFORCEMENT_JUST_WARN = 1;
  private static final int HIDDEN_API_ENFORCEMENT_MAX = 3;
  public static final int HIDDEN_API_ENFORCEMENT_NONE = 0;
  public static final float MAX_ASPECT_DEFAULT = 1.86F;
  public static final float MAX_ASPECT_FULLSCREEN = 0.0F;
  public static final float MAX_ASPECT_NONE = -1.0F;
  public static final String METADATA_PRELOADED_FONTS = "preloaded_fonts";
  public static final float OVERRIDE_MAX_ASPECT_DEFAULT;
  public static final int PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE = 1024;
  public static final int PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION = 4096;
  public static final int PRIVATE_FLAG_ACTIVITIES_RESIZE_MODE_UNRESIZEABLE = 2048;
  public static final int PRIVATE_FLAG_BACKUP_IN_FOREGROUND = 8192;
  public static final int PRIVATE_FLAG_CANT_SAVE_STATE = 2;
  public static final int PRIVATE_FLAG_DEFAULT_TO_DEVICE_PROTECTED_STORAGE = 32;
  public static final int PRIVATE_FLAG_DIRECT_BOOT_AWARE = 64;
  public static final int PRIVATE_FLAG_FORWARD_LOCK = 4;
  public static final int PRIVATE_FLAG_HAS_DOMAIN_URLS = 16;
  public static final int PRIVATE_FLAG_HIDDEN = 1;
  public static final int PRIVATE_FLAG_INSTANT = 128;
  public static final int PRIVATE_FLAG_ISOLATED_SPLIT_LOADING = 32768;
  public static final int PRIVATE_FLAG_OEM = 131072;
  public static final int PRIVATE_FLAG_PARTIALLY_DIRECT_BOOT_AWARE = 256;
  public static final int PRIVATE_FLAG_PRIVILEGED = 8;
  public static final int PRIVATE_FLAG_PRODUCT = 524288;
  public static final int PRIVATE_FLAG_REQUIRED_FOR_SYSTEM_USER = 512;
  public static final int PRIVATE_FLAG_SIGNED_WITH_PLATFORM_KEY = 1048576;
  public static final int PRIVATE_FLAG_STATIC_SHARED_LIBRARY = 16384;
  public static final int PRIVATE_FLAG_VENDOR = 262144;
  public static final int PRIVATE_FLAG_VIRTUAL_PRELOAD = 65536;
  public float actualMaxAspect;
  public String appComponentFactory;
  public String backupAgentName;
  public int category;
  public String classLoaderName;
  public String className;
  public int compatibleWidthLimitDp;
  public int compileSdkVersion;
  public String compileSdkVersionCodename;
  @SystemApi
  public String credentialProtectedDataDir;
  public String dataDir;
  public int descriptionRes;
  public String deviceProtectedDataDir;
  public boolean enabled;
  public int enabledSetting;
  public boolean fillNotchRegion;
  public int flags;
  public int flagsAsus;
  public int fullBackupContent;
  public int installLocation;
  public int largestWidthLimitDp;
  public long longVersionCode;
  private int mHiddenApiPolicy;
  public String manageSpaceActivityName;
  public float maxAspectRatio;
  public int minSdkVersion;
  public String nativeLibraryDir;
  public String nativeLibraryRootDir;
  public boolean nativeLibraryRootRequiresIsa;
  public int networkSecurityConfigRes;
  public int overrideDensity;
  public float overrideMaxAspect;
  public int overrideRes;
  public String permission;
  public String primaryCpuAbi;
  public int privateFlags;
  public String processName;
  public String publicSourceDir;
  public int requiresSmallestWidthDp;
  public String[] resourceDirs;
  public String scanPublicSourceDir;
  public String scanSourceDir;
  public String seInfo;
  public String seInfoUser;
  public String secondaryCpuAbi;
  public String secondaryNativeLibraryDir;
  public String[] sharedLibraryFiles;
  public String sourceDir;
  public String[] splitClassLoaderNames;
  public SparseArray<int[]> splitDependencies;
  public String[] splitNames;
  public String[] splitPublicSourceDirs;
  public String[] splitSourceDirs;
  public UUID storageUuid;
  @SystemApi
  public int targetSandboxVersion;
  public int targetSdkVersion;
  public String taskAffinity;
  public int theme;
  public int uiOptions;
  public int uid;
  @Deprecated
  public int versionCode;
  @Deprecated
  public String volumeUuid;
  public int whiteListed;
  
  static
  {
    float f;
    if ((!"CN".equals(Build.ASUSSKU)) && (!"CUCC".equals(Build.ASUSSKU)) && (!"CMCC".equals(Build.ASUSSKU))) {
      f = -1.0F;
    } else {
      f = 0.0F;
    }
    OVERRIDE_MAX_ASPECT_DEFAULT = f;
  }
  
  public ApplicationInfo()
  {
    fullBackupContent = 0;
    uiOptions = 0;
    flags = 0;
    overrideRes = 0;
    overrideDensity = 0;
    whiteListed = 0;
    requiresSmallestWidthDp = 0;
    compatibleWidthLimitDp = 0;
    largestWidthLimitDp = 0;
    overrideMaxAspect = OVERRIDE_MAX_ASPECT_DEFAULT;
    actualMaxAspect = -1.0F;
    fillNotchRegion = false;
    enabled = true;
    enabledSetting = 0;
    installLocation = -1;
    category = -1;
    mHiddenApiPolicy = -1;
    flagsAsus = 0;
  }
  
  public ApplicationInfo(ApplicationInfo paramApplicationInfo)
  {
    super(paramApplicationInfo);
    fullBackupContent = 0;
    uiOptions = 0;
    flags = 0;
    overrideRes = 0;
    overrideDensity = 0;
    whiteListed = 0;
    requiresSmallestWidthDp = 0;
    compatibleWidthLimitDp = 0;
    largestWidthLimitDp = 0;
    overrideMaxAspect = OVERRIDE_MAX_ASPECT_DEFAULT;
    actualMaxAspect = -1.0F;
    fillNotchRegion = false;
    enabled = true;
    enabledSetting = 0;
    installLocation = -1;
    category = -1;
    mHiddenApiPolicy = -1;
    flagsAsus = 0;
    taskAffinity = taskAffinity;
    permission = permission;
    processName = processName;
    className = className;
    theme = theme;
    flags = flags;
    privateFlags = privateFlags;
    overrideRes = overrideRes;
    overrideDensity = overrideDensity;
    whiteListed = whiteListed;
    requiresSmallestWidthDp = requiresSmallestWidthDp;
    compatibleWidthLimitDp = compatibleWidthLimitDp;
    largestWidthLimitDp = largestWidthLimitDp;
    volumeUuid = volumeUuid;
    storageUuid = storageUuid;
    scanSourceDir = scanSourceDir;
    scanPublicSourceDir = scanPublicSourceDir;
    sourceDir = sourceDir;
    publicSourceDir = publicSourceDir;
    splitNames = splitNames;
    splitSourceDirs = splitSourceDirs;
    splitPublicSourceDirs = splitPublicSourceDirs;
    splitDependencies = splitDependencies;
    nativeLibraryDir = nativeLibraryDir;
    secondaryNativeLibraryDir = secondaryNativeLibraryDir;
    nativeLibraryRootDir = nativeLibraryRootDir;
    nativeLibraryRootRequiresIsa = nativeLibraryRootRequiresIsa;
    primaryCpuAbi = primaryCpuAbi;
    secondaryCpuAbi = secondaryCpuAbi;
    resourceDirs = resourceDirs;
    seInfo = seInfo;
    seInfoUser = seInfoUser;
    sharedLibraryFiles = sharedLibraryFiles;
    dataDir = dataDir;
    deviceProtectedDataDir = deviceProtectedDataDir;
    credentialProtectedDataDir = credentialProtectedDataDir;
    uid = uid;
    minSdkVersion = minSdkVersion;
    targetSdkVersion = targetSdkVersion;
    setVersionCode(longVersionCode);
    enabled = enabled;
    enabledSetting = enabledSetting;
    installLocation = installLocation;
    manageSpaceActivityName = manageSpaceActivityName;
    descriptionRes = descriptionRes;
    uiOptions = uiOptions;
    backupAgentName = backupAgentName;
    fullBackupContent = fullBackupContent;
    networkSecurityConfigRes = networkSecurityConfigRes;
    category = category;
    targetSandboxVersion = targetSandboxVersion;
    classLoaderName = classLoaderName;
    splitClassLoaderNames = splitClassLoaderNames;
    appComponentFactory = appComponentFactory;
    compileSdkVersion = compileSdkVersion;
    compileSdkVersionCodename = compileSdkVersionCodename;
    mHiddenApiPolicy = mHiddenApiPolicy;
    flagsAsus = flagsAsus;
    if (Build.FEATURES.ENABLE_APP_SCALING)
    {
      overrideMaxAspect = overrideMaxAspect;
      actualMaxAspect = actualMaxAspect;
    }
    if (Build.FEATURES.ENABLE_NOTCH_UI) {
      fillNotchRegion = fillNotchRegion;
    }
  }
  
  private ApplicationInfo(Parcel paramParcel)
  {
    super(paramParcel);
    boolean bool1 = false;
    fullBackupContent = 0;
    uiOptions = 0;
    flags = 0;
    overrideRes = 0;
    overrideDensity = 0;
    whiteListed = 0;
    requiresSmallestWidthDp = 0;
    compatibleWidthLimitDp = 0;
    largestWidthLimitDp = 0;
    overrideMaxAspect = OVERRIDE_MAX_ASPECT_DEFAULT;
    actualMaxAspect = -1.0F;
    fillNotchRegion = false;
    enabled = true;
    enabledSetting = 0;
    installLocation = -1;
    category = -1;
    mHiddenApiPolicy = -1;
    flagsAsus = 0;
    taskAffinity = paramParcel.readString();
    permission = paramParcel.readString();
    processName = paramParcel.readString();
    className = paramParcel.readString();
    theme = paramParcel.readInt();
    flags = paramParcel.readInt();
    privateFlags = paramParcel.readInt();
    overrideRes = paramParcel.readInt();
    overrideDensity = paramParcel.readInt();
    whiteListed = paramParcel.readInt();
    requiresSmallestWidthDp = paramParcel.readInt();
    compatibleWidthLimitDp = paramParcel.readInt();
    largestWidthLimitDp = paramParcel.readInt();
    if (paramParcel.readInt() != 0)
    {
      storageUuid = new UUID(paramParcel.readLong(), paramParcel.readLong());
      volumeUuid = StorageManager.convert(storageUuid);
    }
    scanSourceDir = paramParcel.readString();
    scanPublicSourceDir = paramParcel.readString();
    sourceDir = paramParcel.readString();
    publicSourceDir = paramParcel.readString();
    splitNames = paramParcel.readStringArray();
    splitSourceDirs = paramParcel.readStringArray();
    splitPublicSourceDirs = paramParcel.readStringArray();
    splitDependencies = paramParcel.readSparseArray(null);
    nativeLibraryDir = paramParcel.readString();
    secondaryNativeLibraryDir = paramParcel.readString();
    nativeLibraryRootDir = paramParcel.readString();
    boolean bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    nativeLibraryRootRequiresIsa = bool2;
    primaryCpuAbi = paramParcel.readString();
    secondaryCpuAbi = paramParcel.readString();
    resourceDirs = paramParcel.readStringArray();
    seInfo = paramParcel.readString();
    seInfoUser = paramParcel.readString();
    sharedLibraryFiles = paramParcel.readStringArray();
    dataDir = paramParcel.readString();
    deviceProtectedDataDir = paramParcel.readString();
    credentialProtectedDataDir = paramParcel.readString();
    uid = paramParcel.readInt();
    minSdkVersion = paramParcel.readInt();
    targetSdkVersion = paramParcel.readInt();
    setVersionCode(paramParcel.readLong());
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    enabled = bool2;
    enabledSetting = paramParcel.readInt();
    installLocation = paramParcel.readInt();
    manageSpaceActivityName = paramParcel.readString();
    backupAgentName = paramParcel.readString();
    descriptionRes = paramParcel.readInt();
    uiOptions = paramParcel.readInt();
    fullBackupContent = paramParcel.readInt();
    networkSecurityConfigRes = paramParcel.readInt();
    category = paramParcel.readInt();
    targetSandboxVersion = paramParcel.readInt();
    classLoaderName = paramParcel.readString();
    splitClassLoaderNames = paramParcel.readStringArray();
    compileSdkVersion = paramParcel.readInt();
    compileSdkVersionCodename = paramParcel.readString();
    appComponentFactory = paramParcel.readString();
    mHiddenApiPolicy = paramParcel.readInt();
    flagsAsus = paramParcel.readInt();
    if (Build.FEATURES.ENABLE_APP_SCALING)
    {
      overrideMaxAspect = paramParcel.readFloat();
      actualMaxAspect = paramParcel.readFloat();
    }
    if (Build.FEATURES.ENABLE_NOTCH_UI)
    {
      bool2 = bool1;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      }
      fillNotchRegion = bool2;
    }
  }
  
  public static CharSequence getCategoryTitle(Context paramContext, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 7: 
      return paramContext.getText(17039490);
    case 6: 
      return paramContext.getText(17039488);
    case 5: 
      return paramContext.getText(17039489);
    case 4: 
      return paramContext.getText(17039491);
    case 3: 
      return paramContext.getText(17039487);
    case 2: 
      return paramContext.getText(17039492);
    case 1: 
      return paramContext.getText(17039485);
    }
    return paramContext.getText(17039486);
  }
  
  private boolean isAllowedToUseHiddenApis()
  {
    boolean bool;
    if ((!isSignedWithPlatformKey()) && ((!isPackageWhitelistedForHiddenApis()) || ((!isSystemApp()) && (!isUpdatedSystemApp())))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean isPackageUnavailable(PackageManager paramPackageManager)
  {
    boolean bool = true;
    try
    {
      paramPackageManager = paramPackageManager.getPackageInfo(packageName, 0);
      if (paramPackageManager != null) {
        bool = false;
      }
      return bool;
    }
    catch (PackageManager.NameNotFoundException paramPackageManager) {}
    return true;
  }
  
  private boolean isPackageWhitelistedForHiddenApis()
  {
    return SystemConfig.getInstance().getHiddenApiWhitelistedApps().contains(packageName);
  }
  
  public static boolean isValidHiddenApiEnforcementPolicy(int paramInt)
  {
    boolean bool;
    if ((paramInt >= -1) && (paramInt <= 3)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int canOverrideRes()
  {
    return overrideRes;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void disableCompatibilityMode()
  {
    flags |= 0x83E00;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    dump(paramPrinter, paramString, 3);
  }
  
  public void dump(Printer paramPrinter, String paramString, int paramInt)
  {
    super.dumpFront(paramPrinter, paramString);
    if (((paramInt & 0x1) != 0) && (className != null))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("className=");
      ((StringBuilder)localObject).append(className);
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    if (permission != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("permission=");
      ((StringBuilder)localObject).append(permission);
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("processName=");
    ((StringBuilder)localObject).append(processName);
    paramPrinter.println(((StringBuilder)localObject).toString());
    if ((paramInt & 0x1) != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("taskAffinity=");
      ((StringBuilder)localObject).append(taskAffinity);
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("uid=");
    ((StringBuilder)localObject).append(uid);
    ((StringBuilder)localObject).append(" flags=0x");
    ((StringBuilder)localObject).append(Integer.toHexString(flags));
    ((StringBuilder)localObject).append(" privateFlags=0x");
    ((StringBuilder)localObject).append(Integer.toHexString(privateFlags));
    ((StringBuilder)localObject).append(" theme=0x");
    ((StringBuilder)localObject).append(Integer.toHexString(theme));
    paramPrinter.println(((StringBuilder)localObject).toString());
    if ((paramInt & 0x1) != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("requiresSmallestWidthDp=");
      ((StringBuilder)localObject).append(requiresSmallestWidthDp);
      ((StringBuilder)localObject).append(" compatibleWidthLimitDp=");
      ((StringBuilder)localObject).append(compatibleWidthLimitDp);
      ((StringBuilder)localObject).append(" largestWidthLimitDp=");
      ((StringBuilder)localObject).append(largestWidthLimitDp);
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("sourceDir=");
    ((StringBuilder)localObject).append(sourceDir);
    paramPrinter.println(((StringBuilder)localObject).toString());
    if (!Objects.equals(sourceDir, publicSourceDir))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("publicSourceDir=");
      ((StringBuilder)localObject).append(publicSourceDir);
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    if (!ArrayUtils.isEmpty(splitSourceDirs))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("splitSourceDirs=");
      ((StringBuilder)localObject).append(Arrays.toString(splitSourceDirs));
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    if ((!ArrayUtils.isEmpty(splitPublicSourceDirs)) && (!Arrays.equals(splitSourceDirs, splitPublicSourceDirs)))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("splitPublicSourceDirs=");
      ((StringBuilder)localObject).append(Arrays.toString(splitPublicSourceDirs));
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    if (resourceDirs != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("resourceDirs=");
      ((StringBuilder)localObject).append(Arrays.toString(resourceDirs));
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    if (((paramInt & 0x1) != 0) && (seInfo != null))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("seinfo=");
      ((StringBuilder)localObject).append(seInfo);
      paramPrinter.println(((StringBuilder)localObject).toString());
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("seinfoUser=");
      ((StringBuilder)localObject).append(seInfoUser);
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("dataDir=");
    ((StringBuilder)localObject).append(dataDir);
    paramPrinter.println(((StringBuilder)localObject).toString());
    if ((paramInt & 0x1) != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("deviceProtectedDataDir=");
      ((StringBuilder)localObject).append(deviceProtectedDataDir);
      paramPrinter.println(((StringBuilder)localObject).toString());
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("credentialProtectedDataDir=");
      ((StringBuilder)localObject).append(credentialProtectedDataDir);
      paramPrinter.println(((StringBuilder)localObject).toString());
      if (sharedLibraryFiles != null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append("sharedLibraryFiles=");
        ((StringBuilder)localObject).append(Arrays.toString(sharedLibraryFiles));
        paramPrinter.println(((StringBuilder)localObject).toString());
      }
    }
    if (classLoaderName != null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("classLoaderName=");
      ((StringBuilder)localObject).append(classLoaderName);
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    if (!ArrayUtils.isEmpty(splitClassLoaderNames))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("splitClassLoaderNames=");
      ((StringBuilder)localObject).append(Arrays.toString(splitClassLoaderNames));
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("enabled=");
    ((StringBuilder)localObject).append(enabled);
    ((StringBuilder)localObject).append(" minSdkVersion=");
    ((StringBuilder)localObject).append(minSdkVersion);
    ((StringBuilder)localObject).append(" targetSdkVersion=");
    ((StringBuilder)localObject).append(targetSdkVersion);
    ((StringBuilder)localObject).append(" versionCode=");
    ((StringBuilder)localObject).append(longVersionCode);
    ((StringBuilder)localObject).append(" targetSandboxVersion=");
    ((StringBuilder)localObject).append(targetSandboxVersion);
    paramPrinter.println(((StringBuilder)localObject).toString());
    if ((paramInt & 0x1) != 0)
    {
      if (manageSpaceActivityName != null)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append("manageSpaceActivityName=");
        ((StringBuilder)localObject).append(manageSpaceActivityName);
        paramPrinter.println(((StringBuilder)localObject).toString());
      }
      if (descriptionRes != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append("description=0x");
        ((StringBuilder)localObject).append(Integer.toHexString(descriptionRes));
        paramPrinter.println(((StringBuilder)localObject).toString());
      }
      if (uiOptions != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append("uiOptions=0x");
        ((StringBuilder)localObject).append(Integer.toHexString(uiOptions));
        paramPrinter.println(((StringBuilder)localObject).toString());
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("supportsRtl=");
      if (hasRtlSupport()) {
        localObject = "true";
      } else {
        localObject = "false";
      }
      localStringBuilder.append((String)localObject);
      paramPrinter.println(localStringBuilder.toString());
      if (fullBackupContent > 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append("fullBackupContent=@xml/");
        ((StringBuilder)localObject).append(fullBackupContent);
        paramPrinter.println(((StringBuilder)localObject).toString());
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("fullBackupContent=");
        if (fullBackupContent < 0) {
          localObject = "false";
        } else {
          localObject = "true";
        }
        localStringBuilder.append((String)localObject);
        paramPrinter.println(localStringBuilder.toString());
      }
      if (networkSecurityConfigRes != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append("networkSecurityConfigRes=0x");
        ((StringBuilder)localObject).append(Integer.toHexString(networkSecurityConfigRes));
        paramPrinter.println(((StringBuilder)localObject).toString());
      }
      if (category != -1)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append("category=");
        ((StringBuilder)localObject).append(category);
        paramPrinter.println(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("HiddenApiEnforcementPolicy=");
      ((StringBuilder)localObject).append(getHiddenApiEnforcementPolicy());
      paramPrinter.println(((StringBuilder)localObject).toString());
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("uid=");
    ((StringBuilder)localObject).append(uid);
    ((StringBuilder)localObject).append(" flagsAsus=0x");
    ((StringBuilder)localObject).append(Integer.toHexString(flagsAsus));
    paramPrinter.println(((StringBuilder)localObject).toString());
    super.dumpBack(paramPrinter, paramString);
  }
  
  protected ApplicationInfo getApplicationInfo()
  {
    return this;
  }
  
  public String getBaseCodePath()
  {
    return sourceDir;
  }
  
  public String getBaseResourcePath()
  {
    return publicSourceDir;
  }
  
  public String getCodePath()
  {
    return scanSourceDir;
  }
  
  public int getHiddenApiEnforcementPolicy()
  {
    if (isAllowedToUseHiddenApis()) {
      return 0;
    }
    if (mHiddenApiPolicy != -1) {
      return mHiddenApiPolicy;
    }
    if (targetSdkVersion < 28) {
      return 3;
    }
    return 2;
  }
  
  public int getOverrideDensity()
  {
    return overrideDensity;
  }
  
  public String getResourcePath()
  {
    return scanPublicSourceDir;
  }
  
  public String[] getSplitCodePaths()
  {
    return splitSourceDirs;
  }
  
  public String[] getSplitResourcePaths()
  {
    return splitPublicSourceDirs;
  }
  
  public boolean hasCode()
  {
    boolean bool;
    if ((flags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasRtlSupport()
  {
    boolean bool;
    if ((flags & 0x400000) == 4194304) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void initForUser(int paramInt)
  {
    uid = UserHandle.getUid(paramInt, UserHandle.getAppId(uid));
    if ("android".equals(packageName))
    {
      dataDir = Environment.getDataSystemDirectory().getAbsolutePath();
      return;
    }
    deviceProtectedDataDir = Environment.getDataUserDePackageDirectory(volumeUuid, paramInt, packageName).getAbsolutePath();
    credentialProtectedDataDir = Environment.getDataUserCePackageDirectory(volumeUuid, paramInt, packageName).getAbsolutePath();
    if ((privateFlags & 0x20) != 0) {
      dataDir = deviceProtectedDataDir;
    } else {
      dataDir = credentialProtectedDataDir;
    }
  }
  
  public boolean isAppWhiteListed()
  {
    int i = whiteListed;
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDefaultToDeviceProtectedStorage()
  {
    boolean bool;
    if ((privateFlags & 0x20) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDirectBootAware()
  {
    boolean bool;
    if ((privateFlags & 0x40) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isEncryptionAware()
  {
    boolean bool;
    if ((!isDirectBootAware()) && (!isPartiallyDirectBootAware())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isExternal()
  {
    boolean bool;
    if ((flags & 0x40000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isExternalAsec()
  {
    boolean bool;
    if ((TextUtils.isEmpty(volumeUuid)) && (isExternal())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isForwardLocked()
  {
    boolean bool;
    if ((privateFlags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @SystemApi
  public boolean isInstantApp()
  {
    boolean bool;
    if ((privateFlags & 0x80) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isInternal()
  {
    boolean bool;
    if ((flags & 0x40000) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isOem()
  {
    boolean bool;
    if ((privateFlags & 0x20000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPartiallyDirectBootAware()
  {
    boolean bool;
    if ((privateFlags & 0x100) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPrivilegedApp()
  {
    boolean bool;
    if ((privateFlags & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isProduct()
  {
    boolean bool;
    if ((privateFlags & 0x80000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRequiredForSystemUser()
  {
    boolean bool;
    if ((privateFlags & 0x200) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSignedWithPlatformKey()
  {
    boolean bool;
    if ((privateFlags & 0x100000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStaticSharedLibrary()
  {
    boolean bool;
    if ((privateFlags & 0x4000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSystemApp()
  {
    int i = flags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isUpdatedSystemApp()
  {
    boolean bool;
    if ((flags & 0x80) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVendor()
  {
    boolean bool;
    if ((privateFlags & 0x40000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVirtualPreload()
  {
    boolean bool;
    if ((privateFlags & 0x10000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Drawable loadDefaultIcon(PackageManager paramPackageManager)
  {
    if (((flags & 0x40000) != 0) && (isPackageUnavailable(paramPackageManager))) {
      return Resources.getSystem().getDrawable(17303833);
    }
    return paramPackageManager.getDefaultActivityIcon();
  }
  
  public CharSequence loadDescription(PackageManager paramPackageManager)
  {
    if (descriptionRes != 0)
    {
      paramPackageManager = paramPackageManager.getText(packageName, descriptionRes, this);
      if (paramPackageManager != null) {
        return paramPackageManager;
      }
    }
    return null;
  }
  
  public void maybeUpdateHiddenApiEnforcementPolicy(int paramInt1, int paramInt2)
  {
    if (isPackageWhitelistedForHiddenApis()) {
      return;
    }
    if (targetSdkVersion < 28) {
      setHiddenApiEnforcementPolicy(paramInt1);
    } else if (targetSdkVersion >= 28) {
      setHiddenApiEnforcementPolicy(paramInt2);
    }
  }
  
  public boolean requestsIsolatedSplitLoading()
  {
    boolean bool;
    if ((privateFlags & 0x8000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setAppOverrideDensity()
  {
    int i = 0;
    String str = SystemProperties.get("persist.vendor.qti.debug.appdensity");
    int j = i;
    if (str != null)
    {
      j = i;
      if (!str.isEmpty())
      {
        i = Integer.parseInt(str);
        if (i >= 120)
        {
          j = i;
          if (i <= 480) {}
        }
        else
        {
          j = 0;
        }
      }
    }
    setOverrideDensity(j);
  }
  
  public void setAppWhiteListed(int paramInt)
  {
    whiteListed = paramInt;
  }
  
  public void setBaseCodePath(String paramString)
  {
    sourceDir = paramString;
  }
  
  public void setBaseResourcePath(String paramString)
  {
    publicSourceDir = paramString;
  }
  
  public void setCodePath(String paramString)
  {
    scanSourceDir = paramString;
  }
  
  public void setHiddenApiEnforcementPolicy(int paramInt)
  {
    if (isValidHiddenApiEnforcementPolicy(paramInt))
    {
      mHiddenApiPolicy = paramInt;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Invalid API enforcement policy: ");
    localStringBuilder.append(paramInt);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void setOverrideDensity(int paramInt)
  {
    overrideDensity = paramInt;
  }
  
  public void setOverrideRes(int paramInt)
  {
    overrideRes = paramInt;
  }
  
  public void setResourcePath(String paramString)
  {
    scanPublicSourceDir = paramString;
  }
  
  public void setSplitCodePaths(String[] paramArrayOfString)
  {
    splitSourceDirs = paramArrayOfString;
  }
  
  public void setSplitResourcePaths(String[] paramArrayOfString)
  {
    splitPublicSourceDirs = paramArrayOfString;
  }
  
  public void setVersionCode(long paramLong)
  {
    longVersionCode = paramLong;
    versionCode = ((int)paramLong);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ApplicationInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(packageName);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public boolean usesCompatibilityMode()
  {
    boolean bool;
    if ((targetSdkVersion >= 4) && ((flags & 0x83E00) != 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(taskAffinity);
    paramParcel.writeString(permission);
    paramParcel.writeString(processName);
    paramParcel.writeString(className);
    paramParcel.writeInt(theme);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(privateFlags);
    paramParcel.writeInt(overrideRes);
    paramParcel.writeInt(overrideDensity);
    paramParcel.writeInt(whiteListed);
    paramParcel.writeInt(requiresSmallestWidthDp);
    paramParcel.writeInt(compatibleWidthLimitDp);
    paramParcel.writeInt(largestWidthLimitDp);
    if (storageUuid != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeLong(storageUuid.getMostSignificantBits());
      paramParcel.writeLong(storageUuid.getLeastSignificantBits());
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeString(scanSourceDir);
    paramParcel.writeString(scanPublicSourceDir);
    paramParcel.writeString(sourceDir);
    paramParcel.writeString(publicSourceDir);
    paramParcel.writeStringArray(splitNames);
    paramParcel.writeStringArray(splitSourceDirs);
    paramParcel.writeStringArray(splitPublicSourceDirs);
    paramParcel.writeSparseArray(splitDependencies);
    paramParcel.writeString(nativeLibraryDir);
    paramParcel.writeString(secondaryNativeLibraryDir);
    paramParcel.writeString(nativeLibraryRootDir);
    paramParcel.writeInt(nativeLibraryRootRequiresIsa);
    paramParcel.writeString(primaryCpuAbi);
    paramParcel.writeString(secondaryCpuAbi);
    paramParcel.writeStringArray(resourceDirs);
    paramParcel.writeString(seInfo);
    paramParcel.writeString(seInfoUser);
    paramParcel.writeStringArray(sharedLibraryFiles);
    paramParcel.writeString(dataDir);
    paramParcel.writeString(deviceProtectedDataDir);
    paramParcel.writeString(credentialProtectedDataDir);
    paramParcel.writeInt(uid);
    paramParcel.writeInt(minSdkVersion);
    paramParcel.writeInt(targetSdkVersion);
    paramParcel.writeLong(longVersionCode);
    paramParcel.writeInt(enabled);
    paramParcel.writeInt(enabledSetting);
    paramParcel.writeInt(installLocation);
    paramParcel.writeString(manageSpaceActivityName);
    paramParcel.writeString(backupAgentName);
    paramParcel.writeInt(descriptionRes);
    paramParcel.writeInt(uiOptions);
    paramParcel.writeInt(fullBackupContent);
    paramParcel.writeInt(networkSecurityConfigRes);
    paramParcel.writeInt(category);
    paramParcel.writeInt(targetSandboxVersion);
    paramParcel.writeString(classLoaderName);
    paramParcel.writeStringArray(splitClassLoaderNames);
    paramParcel.writeInt(compileSdkVersion);
    paramParcel.writeString(compileSdkVersionCodename);
    paramParcel.writeString(appComponentFactory);
    paramParcel.writeInt(mHiddenApiPolicy);
    paramParcel.writeInt(flagsAsus);
    if (Build.FEATURES.ENABLE_APP_SCALING)
    {
      paramParcel.writeFloat(overrideMaxAspect);
      paramParcel.writeFloat(actualMaxAspect);
    }
    if (Build.FEATURES.ENABLE_NOTCH_UI) {
      paramParcel.writeInt(fillNotchRegion);
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong, int paramInt)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    super.writeToProto(paramProtoOutputStream, 1146756268033L);
    paramProtoOutputStream.write(1138166333442L, permission);
    paramProtoOutputStream.write(1138166333443L, processName);
    paramProtoOutputStream.write(1120986464260L, uid);
    paramProtoOutputStream.write(1120986464261L, flags);
    paramProtoOutputStream.write(1120986464262L, privateFlags);
    paramProtoOutputStream.write(1120986464263L, theme);
    paramProtoOutputStream.write(1138166333448L, sourceDir);
    if (!Objects.equals(sourceDir, publicSourceDir)) {
      paramProtoOutputStream.write(1138166333449L, publicSourceDir);
    }
    boolean bool1 = ArrayUtils.isEmpty(splitSourceDirs);
    boolean bool2 = false;
    Object localObject;
    int i;
    int j;
    if (!bool1)
    {
      localObject = splitSourceDirs;
      i = localObject.length;
      for (j = 0; j < i; j++) {
        paramProtoOutputStream.write(2237677961226L, localObject[j]);
      }
    }
    if ((!ArrayUtils.isEmpty(splitPublicSourceDirs)) && (!Arrays.equals(splitSourceDirs, splitPublicSourceDirs)))
    {
      localObject = splitPublicSourceDirs;
      i = localObject.length;
      for (j = 0; j < i; j++) {
        paramProtoOutputStream.write(2237677961227L, localObject[j]);
      }
    }
    if (resourceDirs != null)
    {
      localObject = resourceDirs;
      i = localObject.length;
      for (j = 0; j < i; j++) {
        paramProtoOutputStream.write(2237677961228L, localObject[j]);
      }
    }
    paramProtoOutputStream.write(1138166333453L, dataDir);
    paramProtoOutputStream.write(1138166333454L, classLoaderName);
    if (!ArrayUtils.isEmpty(splitClassLoaderNames))
    {
      localObject = splitClassLoaderNames;
      i = localObject.length;
      for (j = 0; j < i; j++) {
        paramProtoOutputStream.write(2237677961231L, localObject[j]);
      }
    }
    long l = paramProtoOutputStream.start(1146756268048L);
    paramProtoOutputStream.write(1133871366145L, enabled);
    paramProtoOutputStream.write(1120986464258L, minSdkVersion);
    paramProtoOutputStream.write(1120986464259L, targetSdkVersion);
    paramProtoOutputStream.write(1120986464260L, longVersionCode);
    paramProtoOutputStream.write(1120986464261L, targetSandboxVersion);
    paramProtoOutputStream.end(l);
    if ((paramInt & 0x1) != 0)
    {
      l = paramProtoOutputStream.start(1146756268049L);
      if (className != null) {
        paramProtoOutputStream.write(1138166333441L, className);
      }
      paramProtoOutputStream.write(1138166333442L, taskAffinity);
      paramProtoOutputStream.write(1120986464259L, requiresSmallestWidthDp);
      paramProtoOutputStream.write(1120986464260L, compatibleWidthLimitDp);
      paramProtoOutputStream.write(1120986464261L, largestWidthLimitDp);
      if (seInfo != null)
      {
        paramProtoOutputStream.write(1138166333446L, seInfo);
        paramProtoOutputStream.write(1138166333447L, seInfoUser);
      }
      paramProtoOutputStream.write(1138166333448L, deviceProtectedDataDir);
      paramProtoOutputStream.write(1138166333449L, credentialProtectedDataDir);
      if (sharedLibraryFiles != null)
      {
        localObject = sharedLibraryFiles;
        j = localObject.length;
        for (paramInt = 0; paramInt < j; paramInt++) {
          paramProtoOutputStream.write(2237677961226L, localObject[paramInt]);
        }
      }
      if (manageSpaceActivityName != null) {
        paramProtoOutputStream.write(1138166333451L, manageSpaceActivityName);
      }
      if (descriptionRes != 0) {
        paramProtoOutputStream.write(1120986464268L, descriptionRes);
      }
      if (uiOptions != 0) {
        paramProtoOutputStream.write(1120986464269L, uiOptions);
      }
      paramProtoOutputStream.write(1133871366158L, hasRtlSupport());
      if (fullBackupContent > 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("@xml/");
        ((StringBuilder)localObject).append(fullBackupContent);
        paramProtoOutputStream.write(1138166333455L, ((StringBuilder)localObject).toString());
      }
      else
      {
        if (fullBackupContent == 0) {
          bool2 = true;
        }
        for (;;)
        {
          break;
        }
        paramProtoOutputStream.write(1133871366160L, bool2);
      }
      if (networkSecurityConfigRes != 0) {
        paramProtoOutputStream.write(1120986464273L, networkSecurityConfigRes);
      }
      if (category != -1) {
        paramProtoOutputStream.write(1120986464274L, category);
      }
      paramProtoOutputStream.end(l);
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ApplicationInfoPrivateFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Category {}
  
  public static class DisplayNameComparator
    implements Comparator<ApplicationInfo>
  {
    private PackageManager mPM;
    private final Collator sCollator = Collator.getInstance();
    
    public DisplayNameComparator(PackageManager paramPackageManager)
    {
      mPM = paramPackageManager;
    }
    
    public final int compare(ApplicationInfo paramApplicationInfo1, ApplicationInfo paramApplicationInfo2)
    {
      CharSequence localCharSequence = mPM.getApplicationLabel(paramApplicationInfo1);
      Object localObject = localCharSequence;
      if (localCharSequence == null) {
        localObject = packageName;
      }
      localCharSequence = mPM.getApplicationLabel(paramApplicationInfo2);
      paramApplicationInfo1 = localCharSequence;
      if (localCharSequence == null) {
        paramApplicationInfo1 = packageName;
      }
      return sCollator.compare(((CharSequence)localObject).toString(), paramApplicationInfo1.toString());
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface HiddenApiEnforcementPolicy {}
}
