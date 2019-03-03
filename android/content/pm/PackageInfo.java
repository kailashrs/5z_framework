package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PackageInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PackageInfo> CREATOR = new Parcelable.Creator()
  {
    public PackageInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PackageInfo(paramAnonymousParcel, null);
    }
    
    public PackageInfo[] newArray(int paramAnonymousInt)
    {
      return new PackageInfo[paramAnonymousInt];
    }
  };
  public static final int INSTALL_LOCATION_AUTO = 0;
  public static final int INSTALL_LOCATION_INTERNAL_ONLY = 1;
  public static final int INSTALL_LOCATION_PREFER_EXTERNAL = 2;
  public static final int INSTALL_LOCATION_UNSPECIFIED = -1;
  public static final int REQUESTED_PERMISSION_GRANTED = 2;
  public static final int REQUESTED_PERMISSION_REQUIRED = 1;
  public ActivityInfo[] activities;
  public ApplicationInfo applicationInfo;
  public int baseRevisionCode;
  public int compileSdkVersion;
  public String compileSdkVersionCodename;
  public ConfigurationInfo[] configPreferences;
  public boolean coreApp;
  public FeatureGroupInfo[] featureGroups;
  public long firstInstallTime;
  public int[] gids;
  public int installLocation;
  public InstrumentationInfo[] instrumentation;
  public boolean isStub;
  public long lastUpdateTime;
  boolean mOverlayIsStatic;
  public String overlayCategory;
  public int overlayPriority;
  public String overlayTarget;
  public String packageName;
  public PermissionInfo[] permissions;
  public ProviderInfo[] providers;
  public ActivityInfo[] receivers;
  public FeatureInfo[] reqFeatures;
  public String[] requestedPermissions;
  public int[] requestedPermissionsFlags;
  public String requiredAccountType;
  public boolean requiredForAllUsers;
  public String restrictedAccountType;
  public ServiceInfo[] services;
  public String sharedUserId;
  public int sharedUserLabel;
  @Deprecated
  public Signature[] signatures;
  public SigningInfo signingInfo;
  public String[] splitNames;
  public int[] splitRevisionCodes;
  @Deprecated
  public int versionCode;
  public int versionCodeMajor;
  public String versionName;
  
  public PackageInfo()
  {
    installLocation = 1;
  }
  
  private PackageInfo(Parcel paramParcel)
  {
    boolean bool1 = true;
    installLocation = 1;
    packageName = paramParcel.readString();
    splitNames = paramParcel.createStringArray();
    versionCode = paramParcel.readInt();
    versionCodeMajor = paramParcel.readInt();
    versionName = paramParcel.readString();
    baseRevisionCode = paramParcel.readInt();
    splitRevisionCodes = paramParcel.createIntArray();
    sharedUserId = paramParcel.readString();
    sharedUserLabel = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      applicationInfo = ((ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(paramParcel));
    }
    firstInstallTime = paramParcel.readLong();
    lastUpdateTime = paramParcel.readLong();
    gids = paramParcel.createIntArray();
    activities = ((ActivityInfo[])paramParcel.createTypedArray(ActivityInfo.CREATOR));
    receivers = ((ActivityInfo[])paramParcel.createTypedArray(ActivityInfo.CREATOR));
    services = ((ServiceInfo[])paramParcel.createTypedArray(ServiceInfo.CREATOR));
    providers = ((ProviderInfo[])paramParcel.createTypedArray(ProviderInfo.CREATOR));
    instrumentation = ((InstrumentationInfo[])paramParcel.createTypedArray(InstrumentationInfo.CREATOR));
    permissions = ((PermissionInfo[])paramParcel.createTypedArray(PermissionInfo.CREATOR));
    requestedPermissions = paramParcel.createStringArray();
    requestedPermissionsFlags = paramParcel.createIntArray();
    signatures = ((Signature[])paramParcel.createTypedArray(Signature.CREATOR));
    configPreferences = ((ConfigurationInfo[])paramParcel.createTypedArray(ConfigurationInfo.CREATOR));
    reqFeatures = ((FeatureInfo[])paramParcel.createTypedArray(FeatureInfo.CREATOR));
    featureGroups = ((FeatureGroupInfo[])paramParcel.createTypedArray(FeatureGroupInfo.CREATOR));
    installLocation = paramParcel.readInt();
    boolean bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    isStub = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    coreApp = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    requiredForAllUsers = bool2;
    restrictedAccountType = paramParcel.readString();
    requiredAccountType = paramParcel.readString();
    overlayTarget = paramParcel.readString();
    overlayCategory = paramParcel.readString();
    overlayPriority = paramParcel.readInt();
    mOverlayIsStatic = paramParcel.readBoolean();
    compileSdkVersion = paramParcel.readInt();
    compileSdkVersionCodename = paramParcel.readString();
    if (paramParcel.readInt() != 0) {
      signingInfo = ((SigningInfo)SigningInfo.CREATOR.createFromParcel(paramParcel));
    }
    if (applicationInfo != null)
    {
      propagateApplicationInfo(applicationInfo, activities);
      propagateApplicationInfo(applicationInfo, receivers);
      propagateApplicationInfo(applicationInfo, services);
      propagateApplicationInfo(applicationInfo, providers);
    }
  }
  
  public static long composeLongVersionCode(int paramInt1, int paramInt2)
  {
    return paramInt1 << 32 | paramInt2 & 0xFFFFFFFF;
  }
  
  private void propagateApplicationInfo(ApplicationInfo paramApplicationInfo, ComponentInfo[] paramArrayOfComponentInfo)
  {
    if (paramArrayOfComponentInfo != null)
    {
      int i = paramArrayOfComponentInfo.length;
      for (int j = 0; j < i; j++) {
        applicationInfo = paramApplicationInfo;
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getLongVersionCode()
  {
    return composeLongVersionCode(versionCodeMajor, versionCode);
  }
  
  public boolean isOverlayPackage()
  {
    boolean bool;
    if (overlayTarget != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isStaticOverlayPackage()
  {
    boolean bool;
    if ((overlayTarget != null) && (mOverlayIsStatic)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void setLongVersionCode(long paramLong)
  {
    versionCodeMajor = ((int)(paramLong >> 32));
    versionCode = ((int)paramLong);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PackageInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(packageName);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(packageName);
    paramParcel.writeStringArray(splitNames);
    paramParcel.writeInt(versionCode);
    paramParcel.writeInt(versionCodeMajor);
    paramParcel.writeString(versionName);
    paramParcel.writeInt(baseRevisionCode);
    paramParcel.writeIntArray(splitRevisionCodes);
    paramParcel.writeString(sharedUserId);
    paramParcel.writeInt(sharedUserLabel);
    if (applicationInfo != null)
    {
      paramParcel.writeInt(1);
      applicationInfo.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeLong(firstInstallTime);
    paramParcel.writeLong(lastUpdateTime);
    paramParcel.writeIntArray(gids);
    paramParcel.writeTypedArray(activities, paramInt | 0x2);
    paramParcel.writeTypedArray(receivers, paramInt | 0x2);
    paramParcel.writeTypedArray(services, paramInt | 0x2);
    paramParcel.writeTypedArray(providers, paramInt | 0x2);
    paramParcel.writeTypedArray(instrumentation, paramInt);
    paramParcel.writeTypedArray(permissions, paramInt);
    paramParcel.writeStringArray(requestedPermissions);
    paramParcel.writeIntArray(requestedPermissionsFlags);
    paramParcel.writeTypedArray(signatures, paramInt);
    paramParcel.writeTypedArray(configPreferences, paramInt);
    paramParcel.writeTypedArray(reqFeatures, paramInt);
    paramParcel.writeTypedArray(featureGroups, paramInt);
    paramParcel.writeInt(installLocation);
    paramParcel.writeInt(isStub);
    paramParcel.writeInt(coreApp);
    paramParcel.writeInt(requiredForAllUsers);
    paramParcel.writeString(restrictedAccountType);
    paramParcel.writeString(requiredAccountType);
    paramParcel.writeString(overlayTarget);
    paramParcel.writeString(overlayCategory);
    paramParcel.writeInt(overlayPriority);
    paramParcel.writeBoolean(mOverlayIsStatic);
    paramParcel.writeInt(compileSdkVersion);
    paramParcel.writeString(compileSdkVersionCodename);
    if (signingInfo != null)
    {
      paramParcel.writeInt(1);
      signingInfo.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
