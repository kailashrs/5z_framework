package android.content.pm;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

public class PermissionGroupInfo
  extends PackageItemInfo
  implements Parcelable
{
  public static final Parcelable.Creator<PermissionGroupInfo> CREATOR = new Parcelable.Creator()
  {
    public PermissionGroupInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PermissionGroupInfo(paramAnonymousParcel, null);
    }
    
    public PermissionGroupInfo[] newArray(int paramAnonymousInt)
    {
      return new PermissionGroupInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_PERSONAL_INFO = 1;
  public int descriptionRes;
  public int flags;
  public CharSequence nonLocalizedDescription;
  public int priority;
  @SystemApi
  public int requestRes;
  
  public PermissionGroupInfo() {}
  
  public PermissionGroupInfo(PermissionGroupInfo paramPermissionGroupInfo)
  {
    super(paramPermissionGroupInfo);
    descriptionRes = descriptionRes;
    requestRes = requestRes;
    nonLocalizedDescription = nonLocalizedDescription;
    flags = flags;
    priority = priority;
  }
  
  private PermissionGroupInfo(Parcel paramParcel)
  {
    super(paramParcel);
    descriptionRes = paramParcel.readInt();
    requestRes = paramParcel.readInt();
    nonLocalizedDescription = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    flags = paramParcel.readInt();
    priority = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
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
    localStringBuilder.append("PermissionGroupInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(name);
    localStringBuilder.append(" flgs=0x");
    localStringBuilder.append(Integer.toHexString(flags));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(descriptionRes);
    paramParcel.writeInt(requestRes);
    TextUtils.writeToParcel(nonLocalizedDescription, paramParcel, paramInt);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(priority);
  }
}
