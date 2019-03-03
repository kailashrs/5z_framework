package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PackageCleanItem
  implements Parcelable
{
  public static final Parcelable.Creator<PackageCleanItem> CREATOR = new Parcelable.Creator()
  {
    public PackageCleanItem createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PackageCleanItem(paramAnonymousParcel, null);
    }
    
    public PackageCleanItem[] newArray(int paramAnonymousInt)
    {
      return new PackageCleanItem[paramAnonymousInt];
    }
  };
  public final boolean andCode;
  public final String packageName;
  public final int userId;
  
  public PackageCleanItem(int paramInt, String paramString, boolean paramBoolean)
  {
    userId = paramInt;
    packageName = paramString;
    andCode = paramBoolean;
  }
  
  private PackageCleanItem(Parcel paramParcel)
  {
    userId = paramParcel.readInt();
    packageName = paramParcel.readString();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    andCode = bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = true;
    if (this == paramObject) {
      return true;
    }
    if (paramObject != null) {
      try
      {
        paramObject = (PackageCleanItem)paramObject;
        if ((userId == userId) && (packageName.equals(packageName)))
        {
          boolean bool2 = andCode;
          boolean bool3 = andCode;
          if (bool2 == bool3) {}
        }
        else
        {
          bool1 = false;
        }
        return bool1;
      }
      catch (ClassCastException paramObject) {}
    }
    return false;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * 17 + userId) + packageName.hashCode()) + andCode;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(userId);
    paramParcel.writeString(packageName);
    paramParcel.writeInt(andCode);
  }
}
