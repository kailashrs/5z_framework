package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.proto.ProtoOutputStream;

public class FeatureInfo
  implements Parcelable
{
  public static final Parcelable.Creator<FeatureInfo> CREATOR = new Parcelable.Creator()
  {
    public FeatureInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FeatureInfo(paramAnonymousParcel, null);
    }
    
    public FeatureInfo[] newArray(int paramAnonymousInt)
    {
      return new FeatureInfo[paramAnonymousInt];
    }
  };
  public static final int FLAG_REQUIRED = 1;
  public static final int GL_ES_VERSION_UNDEFINED = 0;
  public int flags;
  public String name;
  public int reqGlEsVersion;
  public int version;
  
  public FeatureInfo() {}
  
  public FeatureInfo(FeatureInfo paramFeatureInfo)
  {
    name = name;
    version = version;
    reqGlEsVersion = reqGlEsVersion;
    flags = flags;
  }
  
  private FeatureInfo(Parcel paramParcel)
  {
    name = paramParcel.readString();
    version = paramParcel.readInt();
    reqGlEsVersion = paramParcel.readInt();
    flags = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getGlEsVersion()
  {
    int i = reqGlEsVersion;
    int j = reqGlEsVersion;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(String.valueOf((i & 0xFFFF0000) >> 16));
    localStringBuilder.append(".");
    localStringBuilder.append(String.valueOf(j & 0xFFFF));
    return localStringBuilder.toString();
  }
  
  public String toString()
  {
    if (name != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("FeatureInfo{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" ");
      localStringBuilder.append(name);
      localStringBuilder.append(" v=");
      localStringBuilder.append(version);
      localStringBuilder.append(" fl=0x");
      localStringBuilder.append(Integer.toHexString(flags));
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("FeatureInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" glEsVers=");
    localStringBuilder.append(getGlEsVersion());
    localStringBuilder.append(" fl=0x");
    localStringBuilder.append(Integer.toHexString(flags));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(name);
    paramParcel.writeInt(version);
    paramParcel.writeInt(reqGlEsVersion);
    paramParcel.writeInt(flags);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    if (name != null) {
      paramProtoOutputStream.write(1138166333441L, name);
    }
    paramProtoOutputStream.write(1120986464258L, version);
    paramProtoOutputStream.write(1138166333443L, getGlEsVersion());
    paramProtoOutputStream.write(1120986464260L, flags);
    paramProtoOutputStream.end(paramLong);
  }
}
