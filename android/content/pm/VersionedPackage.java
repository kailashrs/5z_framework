package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class VersionedPackage
  implements Parcelable
{
  public static final Parcelable.Creator<VersionedPackage> CREATOR = new Parcelable.Creator()
  {
    public VersionedPackage createFromParcel(Parcel paramAnonymousParcel)
    {
      return new VersionedPackage(paramAnonymousParcel, null);
    }
    
    public VersionedPackage[] newArray(int paramAnonymousInt)
    {
      return new VersionedPackage[paramAnonymousInt];
    }
  };
  private final String mPackageName;
  private final long mVersionCode;
  
  private VersionedPackage(Parcel paramParcel)
  {
    mPackageName = paramParcel.readString();
    mVersionCode = paramParcel.readLong();
  }
  
  public VersionedPackage(String paramString, int paramInt)
  {
    mPackageName = paramString;
    mVersionCode = paramInt;
  }
  
  public VersionedPackage(String paramString, long paramLong)
  {
    mPackageName = paramString;
    mVersionCode = paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getLongVersionCode()
  {
    return mVersionCode;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  @Deprecated
  public int getVersionCode()
  {
    return (int)(mVersionCode & 0x7FFFFFFF);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("VersionedPackage[");
    localStringBuilder.append(mPackageName);
    localStringBuilder.append("/");
    localStringBuilder.append(mVersionCode);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    paramParcel.writeLong(mVersionCode);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface VersionCode {}
}
