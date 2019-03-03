package android.app.timezone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class DistroRulesVersion
  implements Parcelable
{
  public static final Parcelable.Creator<DistroRulesVersion> CREATOR = new Parcelable.Creator()
  {
    public DistroRulesVersion createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DistroRulesVersion(paramAnonymousParcel.readString(), paramAnonymousParcel.readInt());
    }
    
    public DistroRulesVersion[] newArray(int paramAnonymousInt)
    {
      return new DistroRulesVersion[paramAnonymousInt];
    }
  };
  private final int mRevision;
  private final String mRulesVersion;
  
  public DistroRulesVersion(String paramString, int paramInt)
  {
    mRulesVersion = Utils.validateRulesVersion("rulesVersion", paramString);
    mRevision = Utils.validateVersion("revision", paramInt);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (DistroRulesVersion)paramObject;
      if (mRevision != mRevision) {
        return false;
      }
      return mRulesVersion.equals(mRulesVersion);
    }
    return false;
  }
  
  public int getRevision()
  {
    return mRevision;
  }
  
  public String getRulesVersion()
  {
    return mRulesVersion;
  }
  
  public int hashCode()
  {
    return 31 * mRulesVersion.hashCode() + mRevision;
  }
  
  public boolean isOlderThan(DistroRulesVersion paramDistroRulesVersion)
  {
    int i = mRulesVersion.compareTo(mRulesVersion);
    boolean bool = true;
    if (i < 0) {
      return true;
    }
    if (i > 0) {
      return false;
    }
    if (mRevision >= mRevision) {
      bool = false;
    }
    return bool;
  }
  
  public String toDumpString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(mRulesVersion);
    localStringBuilder.append(",");
    localStringBuilder.append(mRevision);
    return localStringBuilder.toString();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DistroRulesVersion{mRulesVersion='");
    localStringBuilder.append(mRulesVersion);
    localStringBuilder.append('\'');
    localStringBuilder.append(", mRevision='");
    localStringBuilder.append(mRevision);
    localStringBuilder.append('\'');
    localStringBuilder.append('}');
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mRulesVersion);
    paramParcel.writeInt(mRevision);
  }
}
