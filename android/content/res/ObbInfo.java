package android.content.res;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ObbInfo
  implements Parcelable
{
  public static final Parcelable.Creator<ObbInfo> CREATOR = new Parcelable.Creator()
  {
    public ObbInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ObbInfo(paramAnonymousParcel, null);
    }
    
    public ObbInfo[] newArray(int paramAnonymousInt)
    {
      return new ObbInfo[paramAnonymousInt];
    }
  };
  public static final int OBB_OVERLAY = 1;
  public String filename;
  public int flags;
  public String packageName;
  public byte[] salt;
  public int version;
  
  ObbInfo() {}
  
  private ObbInfo(Parcel paramParcel)
  {
    filename = paramParcel.readString();
    packageName = paramParcel.readString();
    version = paramParcel.readInt();
    flags = paramParcel.readInt();
    salt = paramParcel.createByteArray();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ObbInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" packageName=");
    localStringBuilder.append(packageName);
    localStringBuilder.append(",version=");
    localStringBuilder.append(version);
    localStringBuilder.append(",flags=");
    localStringBuilder.append(flags);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(filename);
    paramParcel.writeString(packageName);
    paramParcel.writeInt(version);
    paramParcel.writeInt(flags);
    paramParcel.writeByteArray(salt);
  }
}
