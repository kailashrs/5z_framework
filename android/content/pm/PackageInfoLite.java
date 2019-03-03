package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PackageInfoLite
  implements Parcelable
{
  public static final Parcelable.Creator<PackageInfoLite> CREATOR = new Parcelable.Creator()
  {
    public PackageInfoLite createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PackageInfoLite(paramAnonymousParcel, null);
    }
    
    public PackageInfoLite[] newArray(int paramAnonymousInt)
    {
      return new PackageInfoLite[paramAnonymousInt];
    }
  };
  public int baseRevisionCode;
  public int installLocation;
  public boolean multiArch;
  public String packageName;
  public int recommendedInstallLocation;
  public String[] splitNames;
  public int[] splitRevisionCodes;
  public VerifierInfo[] verifiers;
  @Deprecated
  public int versionCode;
  public int versionCodeMajor;
  
  public PackageInfoLite() {}
  
  private PackageInfoLite(Parcel paramParcel)
  {
    packageName = paramParcel.readString();
    splitNames = paramParcel.createStringArray();
    versionCode = paramParcel.readInt();
    versionCodeMajor = paramParcel.readInt();
    baseRevisionCode = paramParcel.readInt();
    splitRevisionCodes = paramParcel.createIntArray();
    recommendedInstallLocation = paramParcel.readInt();
    installLocation = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    multiArch = bool;
    int i = paramParcel.readInt();
    if (i == 0)
    {
      verifiers = new VerifierInfo[0];
    }
    else
    {
      verifiers = new VerifierInfo[i];
      paramParcel.readTypedArray(verifiers, VerifierInfo.CREATOR);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getLongVersionCode()
  {
    return PackageInfo.composeLongVersionCode(versionCodeMajor, versionCode);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("PackageInfoLite{");
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
    paramParcel.writeInt(baseRevisionCode);
    paramParcel.writeIntArray(splitRevisionCodes);
    paramParcel.writeInt(recommendedInstallLocation);
    paramParcel.writeInt(installLocation);
    paramParcel.writeInt(multiArch);
    if ((verifiers != null) && (verifiers.length != 0))
    {
      paramParcel.writeInt(verifiers.length);
      paramParcel.writeTypedArray(verifiers, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
  }
}
