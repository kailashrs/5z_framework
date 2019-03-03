package android.app.timezone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class DistroFormatVersion
  implements Parcelable
{
  public static final Parcelable.Creator<DistroFormatVersion> CREATOR = new Parcelable.Creator()
  {
    public DistroFormatVersion createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DistroFormatVersion(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public DistroFormatVersion[] newArray(int paramAnonymousInt)
    {
      return new DistroFormatVersion[paramAnonymousInt];
    }
  };
  private final int mMajorVersion;
  private final int mMinorVersion;
  
  public DistroFormatVersion(int paramInt1, int paramInt2)
  {
    mMajorVersion = Utils.validateVersion("major", paramInt1);
    mMinorVersion = Utils.validateVersion("minor", paramInt2);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (DistroFormatVersion)paramObject;
      if (mMajorVersion != mMajorVersion) {
        return false;
      }
      if (mMinorVersion != mMinorVersion) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int getMajorVersion()
  {
    return mMajorVersion;
  }
  
  public int getMinorVersion()
  {
    return mMinorVersion;
  }
  
  public int hashCode()
  {
    return 31 * mMajorVersion + mMinorVersion;
  }
  
  public boolean supports(DistroFormatVersion paramDistroFormatVersion)
  {
    boolean bool;
    if ((mMajorVersion == mMajorVersion) && (mMinorVersion <= mMinorVersion)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DistroFormatVersion{mMajorVersion=");
    localStringBuilder.append(mMajorVersion);
    localStringBuilder.append(", mMinorVersion=");
    localStringBuilder.append(mMinorVersion);
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mMajorVersion);
    paramParcel.writeInt(mMinorVersion);
  }
}
