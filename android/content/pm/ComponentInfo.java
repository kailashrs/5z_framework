package android.content.pm;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Printer;

public class ComponentInfo
  extends PackageItemInfo
{
  public ApplicationInfo applicationInfo;
  public int descriptionRes;
  public boolean directBootAware;
  public boolean enabled;
  @Deprecated
  public boolean encryptionAware;
  public boolean exported;
  public String processName;
  public String splitName;
  
  public ComponentInfo()
  {
    enabled = true;
    exported = false;
    directBootAware = false;
    encryptionAware = false;
  }
  
  public ComponentInfo(ComponentInfo paramComponentInfo)
  {
    super(paramComponentInfo);
    enabled = true;
    exported = false;
    directBootAware = false;
    encryptionAware = false;
    applicationInfo = applicationInfo;
    processName = processName;
    splitName = splitName;
    descriptionRes = descriptionRes;
    enabled = enabled;
    exported = exported;
    boolean bool = directBootAware;
    directBootAware = bool;
    encryptionAware = bool;
  }
  
  protected ComponentInfo(Parcel paramParcel)
  {
    super(paramParcel);
    boolean bool1 = true;
    enabled = true;
    exported = false;
    directBootAware = false;
    encryptionAware = false;
    int i;
    if (paramParcel.readInt() != 0) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      applicationInfo = ((ApplicationInfo)ApplicationInfo.CREATOR.createFromParcel(paramParcel));
    }
    processName = paramParcel.readString();
    splitName = paramParcel.readString();
    descriptionRes = paramParcel.readInt();
    boolean bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    enabled = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    exported = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    directBootAware = bool2;
    encryptionAware = bool2;
  }
  
  protected void dumpBack(Printer paramPrinter, String paramString)
  {
    dumpBack(paramPrinter, paramString, 3);
  }
  
  void dumpBack(Printer paramPrinter, String paramString, int paramInt)
  {
    if ((paramInt & 0x2) != 0)
    {
      StringBuilder localStringBuilder;
      if (applicationInfo != null)
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("ApplicationInfo:");
        paramPrinter.println(localStringBuilder.toString());
        ApplicationInfo localApplicationInfo = applicationInfo;
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("  ");
        localApplicationInfo.dump(paramPrinter, localStringBuilder.toString(), paramInt);
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("ApplicationInfo: null");
        paramPrinter.println(localStringBuilder.toString());
      }
    }
    super.dumpBack(paramPrinter, paramString);
  }
  
  protected void dumpFront(Printer paramPrinter, String paramString)
  {
    super.dumpFront(paramPrinter, paramString);
    if ((processName != null) && (!packageName.equals(processName)))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("processName=");
      localStringBuilder.append(processName);
      paramPrinter.println(localStringBuilder.toString());
    }
    if (splitName != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("splitName=");
      localStringBuilder.append(splitName);
      paramPrinter.println(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("enabled=");
    localStringBuilder.append(enabled);
    localStringBuilder.append(" exported=");
    localStringBuilder.append(exported);
    localStringBuilder.append(" directBootAware=");
    localStringBuilder.append(directBootAware);
    paramPrinter.println(localStringBuilder.toString());
    if (descriptionRes != 0)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("description=");
      localStringBuilder.append(descriptionRes);
      paramPrinter.println(localStringBuilder.toString());
    }
  }
  
  protected ApplicationInfo getApplicationInfo()
  {
    return applicationInfo;
  }
  
  public final int getBannerResource()
  {
    int i;
    if (banner != 0) {
      i = banner;
    } else {
      i = applicationInfo.banner;
    }
    return i;
  }
  
  public ComponentName getComponentName()
  {
    return new ComponentName(packageName, name);
  }
  
  public final int getIconResource()
  {
    int i;
    if (icon != 0) {
      i = icon;
    } else {
      i = applicationInfo.icon;
    }
    return i;
  }
  
  public final int getLogoResource()
  {
    int i;
    if (logo != 0) {
      i = logo;
    } else {
      i = applicationInfo.logo;
    }
    return i;
  }
  
  public boolean isEnabled()
  {
    boolean bool;
    if ((enabled) && (applicationInfo.enabled)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected Drawable loadDefaultBanner(PackageManager paramPackageManager)
  {
    return applicationInfo.loadBanner(paramPackageManager);
  }
  
  public Drawable loadDefaultIcon(PackageManager paramPackageManager)
  {
    return applicationInfo.loadIcon(paramPackageManager);
  }
  
  protected Drawable loadDefaultLogo(PackageManager paramPackageManager)
  {
    return applicationInfo.loadLogo(paramPackageManager);
  }
  
  public CharSequence loadUnsafeLabel(PackageManager paramPackageManager)
  {
    if (nonLocalizedLabel != null) {
      return nonLocalizedLabel;
    }
    ApplicationInfo localApplicationInfo = applicationInfo;
    if (labelRes != 0)
    {
      CharSequence localCharSequence = paramPackageManager.getText(packageName, labelRes, localApplicationInfo);
      if (localCharSequence != null) {
        return localCharSequence;
      }
    }
    if (nonLocalizedLabel != null) {
      return nonLocalizedLabel;
    }
    if (labelRes != 0)
    {
      paramPackageManager = paramPackageManager.getText(packageName, labelRes, localApplicationInfo);
      if (paramPackageManager != null) {
        return paramPackageManager;
      }
    }
    return name;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    if ((paramInt & 0x2) != 0)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      paramParcel.writeInt(1);
      applicationInfo.writeToParcel(paramParcel, paramInt);
    }
    paramParcel.writeString(processName);
    paramParcel.writeString(splitName);
    paramParcel.writeInt(descriptionRes);
    paramParcel.writeInt(enabled);
    paramParcel.writeInt(exported);
    paramParcel.writeInt(directBootAware);
  }
}
