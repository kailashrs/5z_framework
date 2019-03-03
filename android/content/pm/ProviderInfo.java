package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PatternMatcher;
import android.util.Printer;

public final class ProviderInfo
  extends ComponentInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ProviderInfo> CREATOR = new Parcelable.Creator()
  {
    public ProviderInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ProviderInfo(paramAnonymousParcel, null);
    }
    
    public ProviderInfo[] newArray(int paramAnonymousInt)
    {
      return new ProviderInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_SINGLE_USER = 1073741824;
  public static final int FLAG_VISIBLE_TO_INSTANT_APP = 1048576;
  public String authority = null;
  public int flags;
  public boolean grantUriPermissions;
  public int initOrder;
  @Deprecated
  public boolean isSyncable;
  public boolean multiprocess;
  public PathPermission[] pathPermissions;
  public String readPermission = null;
  public PatternMatcher[] uriPermissionPatterns;
  public String writePermission = null;
  
  public ProviderInfo()
  {
    grantUriPermissions = false;
    uriPermissionPatterns = null;
    pathPermissions = null;
    multiprocess = false;
    initOrder = 0;
    flags = 0;
    isSyncable = false;
  }
  
  public ProviderInfo(ProviderInfo paramProviderInfo)
  {
    super(paramProviderInfo);
    grantUriPermissions = false;
    uriPermissionPatterns = null;
    pathPermissions = null;
    multiprocess = false;
    initOrder = 0;
    flags = 0;
    isSyncable = false;
    authority = authority;
    readPermission = readPermission;
    writePermission = writePermission;
    grantUriPermissions = grantUriPermissions;
    uriPermissionPatterns = uriPermissionPatterns;
    pathPermissions = pathPermissions;
    multiprocess = multiprocess;
    initOrder = initOrder;
    flags = flags;
    isSyncable = isSyncable;
  }
  
  private ProviderInfo(Parcel paramParcel)
  {
    super(paramParcel);
    boolean bool1 = false;
    grantUriPermissions = false;
    uriPermissionPatterns = null;
    pathPermissions = null;
    multiprocess = false;
    initOrder = 0;
    flags = 0;
    isSyncable = false;
    authority = paramParcel.readString();
    readPermission = paramParcel.readString();
    writePermission = paramParcel.readString();
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    grantUriPermissions = bool2;
    uriPermissionPatterns = ((PatternMatcher[])paramParcel.createTypedArray(PatternMatcher.CREATOR));
    pathPermissions = ((PathPermission[])paramParcel.createTypedArray(PathPermission.CREATOR));
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    multiprocess = bool2;
    initOrder = paramParcel.readInt();
    flags = paramParcel.readInt();
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    isSyncable = bool2;
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
    super.dumpFront(paramPrinter, paramString);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("authority=");
    localStringBuilder.append(authority);
    paramPrinter.println(localStringBuilder.toString());
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("flags=0x");
    localStringBuilder.append(Integer.toHexString(flags));
    paramPrinter.println(localStringBuilder.toString());
    super.dumpBack(paramPrinter, paramString, paramInt);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ContentProviderInfo{name=");
    localStringBuilder.append(authority);
    localStringBuilder.append(" className=");
    localStringBuilder.append(name);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeString(authority);
    paramParcel.writeString(readPermission);
    paramParcel.writeString(writePermission);
    paramParcel.writeInt(grantUriPermissions);
    paramParcel.writeTypedArray(uriPermissionPatterns, paramInt);
    paramParcel.writeTypedArray(pathPermissions, paramInt);
    paramParcel.writeInt(multiprocess);
    paramParcel.writeInt(initOrder);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(isSyncable);
  }
}
