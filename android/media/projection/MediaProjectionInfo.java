package android.media.projection;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import java.util.Objects;

public final class MediaProjectionInfo
  implements Parcelable
{
  public static final Parcelable.Creator<MediaProjectionInfo> CREATOR = new Parcelable.Creator()
  {
    public MediaProjectionInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MediaProjectionInfo(paramAnonymousParcel);
    }
    
    public MediaProjectionInfo[] newArray(int paramAnonymousInt)
    {
      return new MediaProjectionInfo[paramAnonymousInt];
    }
  };
  private final String mPackageName;
  private final UserHandle mUserHandle;
  
  public MediaProjectionInfo(Parcel paramParcel)
  {
    mPackageName = paramParcel.readString();
    mUserHandle = UserHandle.readFromParcel(paramParcel);
  }
  
  public MediaProjectionInfo(String paramString, UserHandle paramUserHandle)
  {
    mPackageName = paramString;
    mUserHandle = paramUserHandle;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof MediaProjectionInfo;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (MediaProjectionInfo)paramObject;
      bool1 = bool2;
      if (Objects.equals(mPackageName, mPackageName))
      {
        bool1 = bool2;
        if (Objects.equals(mUserHandle, mUserHandle)) {
          bool1 = true;
        }
      }
      return bool1;
    }
    return false;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public UserHandle getUserHandle()
  {
    return mUserHandle;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mPackageName, mUserHandle });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("MediaProjectionInfo{mPackageName=");
    localStringBuilder.append(mPackageName);
    localStringBuilder.append(", mUserHandle=");
    localStringBuilder.append(mUserHandle);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    UserHandle.writeToParcel(mUserHandle, paramParcel);
  }
}
