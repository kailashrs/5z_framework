package android.content.pm;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PermissionInfo
  extends PackageItemInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PermissionInfo> CREATOR = new Parcelable.Creator()
  {
    public PermissionInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PermissionInfo(paramAnonymousParcel, null);
    }
    
    public PermissionInfo[] newArray(int paramAnonymousInt)
    {
      return new PermissionInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_COSTS_MONEY = 1;
  public static final int FLAG_INSTALLED = 1073741824;
  @SystemApi
  public static final int FLAG_REMOVED = 2;
  public static final int PROTECTION_DANGEROUS = 1;
  public static final int PROTECTION_FLAG_APPOP = 64;
  public static final int PROTECTION_FLAG_DEVELOPMENT = 32;
  public static final int PROTECTION_FLAG_INSTALLER = 256;
  public static final int PROTECTION_FLAG_INSTANT = 4096;
  @SystemApi
  public static final int PROTECTION_FLAG_OEM = 16384;
  public static final int PROTECTION_FLAG_PRE23 = 128;
  public static final int PROTECTION_FLAG_PREINSTALLED = 1024;
  public static final int PROTECTION_FLAG_PRIVILEGED = 16;
  public static final int PROTECTION_FLAG_RUNTIME_ONLY = 8192;
  public static final int PROTECTION_FLAG_SETUP = 2048;
  @Deprecated
  public static final int PROTECTION_FLAG_SYSTEM = 16;
  @SystemApi
  public static final int PROTECTION_FLAG_SYSTEM_TEXT_CLASSIFIER = 65536;
  public static final int PROTECTION_FLAG_VENDOR_PRIVILEGED = 32768;
  public static final int PROTECTION_FLAG_VERIFIER = 512;
  @Deprecated
  public static final int PROTECTION_MASK_BASE = 15;
  @Deprecated
  public static final int PROTECTION_MASK_FLAGS = 65520;
  public static final int PROTECTION_NORMAL = 0;
  public static final int PROTECTION_SIGNATURE = 2;
  @Deprecated
  public static final int PROTECTION_SIGNATURE_OR_SYSTEM = 3;
  public int descriptionRes;
  public int flags;
  public String group;
  public CharSequence nonLocalizedDescription;
  @Deprecated
  public int protectionLevel;
  @SystemApi
  public int requestRes;
  
  public PermissionInfo() {}
  
  public PermissionInfo(PermissionInfo paramPermissionInfo)
  {
    super(paramPermissionInfo);
    protectionLevel = protectionLevel;
    flags = flags;
    group = group;
    descriptionRes = descriptionRes;
    requestRes = requestRes;
    nonLocalizedDescription = nonLocalizedDescription;
  }
  
  private PermissionInfo(Parcel paramParcel)
  {
    super(paramParcel);
    protectionLevel = paramParcel.readInt();
    flags = paramParcel.readInt();
    group = paramParcel.readString();
    descriptionRes = paramParcel.readInt();
    requestRes = paramParcel.readInt();
    nonLocalizedDescription = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
  }
  
  public static int fixProtectionLevel(int paramInt)
  {
    int i = paramInt;
    if (paramInt == 3) {
      i = 18;
    }
    paramInt = i;
    if ((0x8000 & i) != 0)
    {
      paramInt = i;
      if ((i & 0x10) == 0) {
        paramInt = i & 0xFFFF7FFF;
      }
    }
    return paramInt;
  }
  
  public static String protectionToString(int paramInt)
  {
    Object localObject1 = "????";
    switch (paramInt & 0xF)
    {
    default: 
      break;
    case 3: 
      localObject1 = "signatureOrSystem";
      break;
    case 2: 
      localObject1 = "signature";
      break;
    case 1: 
      localObject1 = "dangerous";
      break;
    case 0: 
      localObject1 = "normal";
    }
    Object localObject2 = localObject1;
    if ((paramInt & 0x10) != 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("|privileged");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    Object localObject3 = localObject2;
    if ((paramInt & 0x20) != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("|development");
      localObject3 = ((StringBuilder)localObject1).toString();
    }
    localObject1 = localObject3;
    if ((paramInt & 0x40) != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject3);
      ((StringBuilder)localObject1).append("|appop");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject3 = localObject1;
    if ((paramInt & 0x80) != 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("|pre23");
      localObject3 = ((StringBuilder)localObject2).toString();
    }
    localObject2 = localObject3;
    if ((paramInt & 0x100) != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject3);
      ((StringBuilder)localObject1).append("|installer");
      localObject2 = ((StringBuilder)localObject1).toString();
    }
    localObject1 = localObject2;
    if ((paramInt & 0x200) != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("|verifier");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = localObject1;
    if ((paramInt & 0x400) != 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("|preinstalled");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = localObject2;
    if ((paramInt & 0x800) != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("|setup");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = localObject1;
    if ((paramInt & 0x1000) != 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("|instant");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = localObject2;
    if ((paramInt & 0x2000) != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("|runtime");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = localObject1;
    if ((paramInt & 0x4000) != 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("|oem");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    localObject1 = localObject2;
    if ((0x8000 & paramInt) != 0)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("|vendorPrivileged");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    localObject2 = localObject1;
    if ((0x10000 & paramInt) != 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("|textClassifier");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    return localObject2;
  }
  
  public int calculateFootprint()
  {
    int i = name.length();
    int j = i;
    if (nonLocalizedLabel != null) {
      j = i + nonLocalizedLabel.length();
    }
    i = j;
    if (nonLocalizedDescription != null) {
      i = j + nonLocalizedDescription.length();
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getProtection()
  {
    return protectionLevel & 0xF;
  }
  
  public int getProtectionFlags()
  {
    return protectionLevel & 0xFFFFFFF0;
  }
  
  public boolean isAppOp()
  {
    boolean bool;
    if ((protectionLevel & 0x40) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public CharSequence loadDescription(PackageManager paramPackageManager)
  {
    if (nonLocalizedDescription != null) {
      return nonLocalizedDescription;
    }
    if (descriptionRes != 0)
    {
      paramPackageManager = paramPackageManager.getText(packageName, descriptionRes, null);
      if (paramPackageManager != null) {
        return paramPackageManager;
      }
    }
    return null;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PermissionInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(name);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(protectionLevel);
    paramParcel.writeInt(flags);
    paramParcel.writeString(group);
    paramParcel.writeInt(descriptionRes);
    paramParcel.writeInt(requestRes);
    TextUtils.writeToParcel(nonLocalizedDescription, paramParcel, paramInt);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Protection {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ProtectionFlags {}
}
