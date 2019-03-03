package android.content.pm;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Printer;
import android.util.Slog;
import java.text.Collator;
import java.util.Comparator;

public class ResolveInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ResolveInfo> CREATOR = new Parcelable.Creator()
  {
    public ResolveInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ResolveInfo(paramAnonymousParcel, null);
    }
    
    public ResolveInfo[] newArray(int paramAnonymousInt)
    {
      return new ResolveInfo[paramAnonymousInt];
    }
  };
  private static final String TAG = "ResolveInfo";
  public ActivityInfo activityInfo;
  public AuxiliaryResolveInfo auxiliaryInfo;
  public IntentFilter filter;
  public boolean handleAllWebDataURI;
  public int icon;
  public int iconResourceId;
  @Deprecated
  public boolean instantAppAvailable;
  public boolean isDefault;
  public boolean isInstantAppAvailable;
  public int labelRes;
  public int match;
  public boolean noResourceId;
  public CharSequence nonLocalizedLabel;
  public int preferredOrder;
  public int priority;
  public ProviderInfo providerInfo;
  public String resolvePackageName;
  public ServiceInfo serviceInfo;
  public int specificIndex = -1;
  public boolean system;
  public int targetUserId;
  
  public ResolveInfo()
  {
    targetUserId = -2;
  }
  
  public ResolveInfo(ResolveInfo paramResolveInfo)
  {
    activityInfo = activityInfo;
    serviceInfo = serviceInfo;
    providerInfo = providerInfo;
    filter = filter;
    priority = priority;
    preferredOrder = preferredOrder;
    match = match;
    specificIndex = specificIndex;
    labelRes = labelRes;
    nonLocalizedLabel = nonLocalizedLabel;
    icon = icon;
    resolvePackageName = resolvePackageName;
    noResourceId = noResourceId;
    iconResourceId = iconResourceId;
    system = system;
    targetUserId = targetUserId;
    handleAllWebDataURI = handleAllWebDataURI;
    isInstantAppAvailable = isInstantAppAvailable;
    instantAppAvailable = isInstantAppAvailable;
  }
  
  private ResolveInfo(Parcel paramParcel)
  {
    activityInfo = null;
    serviceInfo = null;
    providerInfo = null;
    switch (paramParcel.readInt())
    {
    default: 
      Slog.w("ResolveInfo", "Missing ComponentInfo!");
      break;
    case 3: 
      providerInfo = ((ProviderInfo)ProviderInfo.CREATOR.createFromParcel(paramParcel));
      break;
    case 2: 
      serviceInfo = ((ServiceInfo)ServiceInfo.CREATOR.createFromParcel(paramParcel));
      break;
    case 1: 
      activityInfo = ((ActivityInfo)ActivityInfo.CREATOR.createFromParcel(paramParcel));
    }
    if (paramParcel.readInt() != 0) {
      filter = ((IntentFilter)IntentFilter.CREATOR.createFromParcel(paramParcel));
    }
    priority = paramParcel.readInt();
    preferredOrder = paramParcel.readInt();
    match = paramParcel.readInt();
    specificIndex = paramParcel.readInt();
    labelRes = paramParcel.readInt();
    nonLocalizedLabel = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    icon = paramParcel.readInt();
    resolvePackageName = paramParcel.readString();
    targetUserId = paramParcel.readInt();
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    system = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    noResourceId = bool2;
    iconResourceId = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    handleAllWebDataURI = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    isInstantAppAvailable = bool2;
    instantAppAvailable = bool2;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    dump(paramPrinter, paramString, 3);
  }
  
  public void dump(Printer paramPrinter, String paramString, int paramInt)
  {
    Object localObject2;
    if (filter != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("Filter:");
      paramPrinter.println(((StringBuilder)localObject1).toString());
      localObject1 = filter;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("  ");
      ((IntentFilter)localObject1).dump(paramPrinter, ((StringBuilder)localObject2).toString());
    }
    Object localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append(paramString);
    ((StringBuilder)localObject1).append("priority=");
    ((StringBuilder)localObject1).append(priority);
    ((StringBuilder)localObject1).append(" preferredOrder=");
    ((StringBuilder)localObject1).append(preferredOrder);
    ((StringBuilder)localObject1).append(" match=0x");
    ((StringBuilder)localObject1).append(Integer.toHexString(match));
    ((StringBuilder)localObject1).append(" specificIndex=");
    ((StringBuilder)localObject1).append(specificIndex);
    ((StringBuilder)localObject1).append(" isDefault=");
    ((StringBuilder)localObject1).append(isDefault);
    paramPrinter.println(((StringBuilder)localObject1).toString());
    if (resolvePackageName != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("resolvePackageName=");
      ((StringBuilder)localObject1).append(resolvePackageName);
      paramPrinter.println(((StringBuilder)localObject1).toString());
    }
    if ((labelRes != 0) || (nonLocalizedLabel != null) || (icon != 0))
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("labelRes=0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(labelRes));
      ((StringBuilder)localObject1).append(" nonLocalizedLabel=");
      ((StringBuilder)localObject1).append(nonLocalizedLabel);
      ((StringBuilder)localObject1).append(" icon=0x");
      ((StringBuilder)localObject1).append(Integer.toHexString(icon));
      paramPrinter.println(((StringBuilder)localObject1).toString());
    }
    if (activityInfo != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("ActivityInfo:");
      paramPrinter.println(((StringBuilder)localObject1).toString());
      localObject2 = activityInfo;
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("  ");
      ((ActivityInfo)localObject2).dump(paramPrinter, ((StringBuilder)localObject1).toString(), paramInt);
    }
    else if (serviceInfo != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("ServiceInfo:");
      paramPrinter.println(((StringBuilder)localObject1).toString());
      localObject1 = serviceInfo;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("  ");
      ((ServiceInfo)localObject1).dump(paramPrinter, ((StringBuilder)localObject2).toString(), paramInt);
    }
    else if (providerInfo != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append(paramString);
      ((StringBuilder)localObject1).append("ProviderInfo:");
      paramPrinter.println(((StringBuilder)localObject1).toString());
      localObject1 = providerInfo;
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(paramString);
      ((StringBuilder)localObject2).append("  ");
      ((ProviderInfo)localObject1).dump(paramPrinter, ((StringBuilder)localObject2).toString(), paramInt);
    }
  }
  
  public ComponentInfo getComponentInfo()
  {
    if (activityInfo != null) {
      return activityInfo;
    }
    if (serviceInfo != null) {
      return serviceInfo;
    }
    if (providerInfo != null) {
      return providerInfo;
    }
    throw new IllegalStateException("Missing ComponentInfo!");
  }
  
  public final int getIconResource()
  {
    if (noResourceId) {
      return 0;
    }
    return getIconResourceInternal();
  }
  
  final int getIconResourceInternal()
  {
    if (iconResourceId != 0) {
      return iconResourceId;
    }
    ComponentInfo localComponentInfo = getComponentInfo();
    if (localComponentInfo != null) {
      return localComponentInfo.getIconResource();
    }
    return 0;
  }
  
  public Drawable loadIcon(PackageManager paramPackageManager)
  {
    Object localObject1 = null;
    Object localObject2 = localObject1;
    if (resolvePackageName != null)
    {
      localObject2 = localObject1;
      if (iconResourceId != 0) {
        localObject2 = paramPackageManager.getDrawable(resolvePackageName, iconResourceId, null);
      }
    }
    ComponentInfo localComponentInfo = getComponentInfo();
    localObject1 = localObject2;
    if (localObject2 == null)
    {
      localObject1 = localObject2;
      if (iconResourceId != 0)
      {
        localObject2 = applicationInfo;
        localObject1 = paramPackageManager.getDrawable(packageName, iconResourceId, (ApplicationInfo)localObject2);
      }
    }
    if (localObject1 != null)
    {
      if ((paramPackageManager.hasSystemFeature("asus.software.twinapps")) && (applicationInfo != null)) {
        return paramPackageManager.getUserBadgedIcon((Drawable)localObject1, new UserHandle(UserHandle.getUserId(applicationInfo.uid)));
      }
      return paramPackageManager.getUserBadgedIcon((Drawable)localObject1, new UserHandle(paramPackageManager.getUserId()));
    }
    return localComponentInfo.loadIcon(paramPackageManager);
  }
  
  public CharSequence loadLabel(PackageManager paramPackageManager)
  {
    if (nonLocalizedLabel != null) {
      return nonLocalizedLabel;
    }
    if ((resolvePackageName != null) && (labelRes != 0))
    {
      localObject1 = paramPackageManager.getText(resolvePackageName, labelRes, null);
      if (localObject1 != null) {
        return ((CharSequence)localObject1).toString().trim();
      }
    }
    Object localObject1 = getComponentInfo();
    Object localObject2 = applicationInfo;
    if (labelRes != 0)
    {
      localObject2 = paramPackageManager.getText(packageName, labelRes, (ApplicationInfo)localObject2);
      if (localObject2 != null) {
        return ((CharSequence)localObject2).toString().trim();
      }
    }
    localObject1 = ((ComponentInfo)localObject1).loadLabel(paramPackageManager);
    paramPackageManager = (PackageManager)localObject1;
    if (localObject1 != null) {
      paramPackageManager = ((CharSequence)localObject1).toString().trim();
    }
    return paramPackageManager;
  }
  
  public int resolveIconResId()
  {
    if (icon != 0) {
      return icon;
    }
    ComponentInfo localComponentInfo = getComponentInfo();
    if (icon != 0) {
      return icon;
    }
    return applicationInfo.icon;
  }
  
  public int resolveLabelResId()
  {
    if (labelRes != 0) {
      return labelRes;
    }
    ComponentInfo localComponentInfo = getComponentInfo();
    if (labelRes != 0) {
      return labelRes;
    }
    return applicationInfo.labelRes;
  }
  
  public String toString()
  {
    ComponentInfo localComponentInfo = getComponentInfo();
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("ResolveInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(' ');
    ComponentName.appendShortString(localStringBuilder, packageName, name);
    if (priority != 0)
    {
      localStringBuilder.append(" p=");
      localStringBuilder.append(priority);
    }
    if (preferredOrder != 0)
    {
      localStringBuilder.append(" o=");
      localStringBuilder.append(preferredOrder);
    }
    localStringBuilder.append(" m=0x");
    localStringBuilder.append(Integer.toHexString(match));
    if (targetUserId != -2)
    {
      localStringBuilder.append(" targetUserId=");
      localStringBuilder.append(targetUserId);
    }
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (activityInfo != null)
    {
      paramParcel.writeInt(1);
      activityInfo.writeToParcel(paramParcel, paramInt);
    }
    else if (serviceInfo != null)
    {
      paramParcel.writeInt(2);
      serviceInfo.writeToParcel(paramParcel, paramInt);
    }
    else if (providerInfo != null)
    {
      paramParcel.writeInt(3);
      providerInfo.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (filter != null)
    {
      paramParcel.writeInt(1);
      filter.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(priority);
    paramParcel.writeInt(preferredOrder);
    paramParcel.writeInt(match);
    paramParcel.writeInt(specificIndex);
    paramParcel.writeInt(labelRes);
    TextUtils.writeToParcel(nonLocalizedLabel, paramParcel, paramInt);
    paramParcel.writeInt(icon);
    paramParcel.writeString(resolvePackageName);
    paramParcel.writeInt(targetUserId);
    paramParcel.writeInt(system);
    paramParcel.writeInt(noResourceId);
    paramParcel.writeInt(iconResourceId);
    paramParcel.writeInt(handleAllWebDataURI);
    paramParcel.writeInt(isInstantAppAvailable);
  }
  
  public static class DisplayNameComparator
    implements Comparator<ResolveInfo>
  {
    private final Collator mCollator = Collator.getInstance();
    private PackageManager mPM;
    
    public DisplayNameComparator(PackageManager paramPackageManager)
    {
      mPM = paramPackageManager;
      mCollator.setStrength(0);
    }
    
    public final int compare(ResolveInfo paramResolveInfo1, ResolveInfo paramResolveInfo2)
    {
      if (targetUserId != -2) {
        return 1;
      }
      if (targetUserId != -2) {
        return -1;
      }
      CharSequence localCharSequence = paramResolveInfo1.loadLabel(mPM);
      Object localObject = localCharSequence;
      if (localCharSequence == null) {
        localObject = activityInfo.name;
      }
      localCharSequence = paramResolveInfo2.loadLabel(mPM);
      paramResolveInfo1 = localCharSequence;
      if (localCharSequence == null) {
        paramResolveInfo1 = activityInfo.name;
      }
      return mCollator.compare(((CharSequence)localObject).toString(), paramResolveInfo1.toString());
    }
  }
}
