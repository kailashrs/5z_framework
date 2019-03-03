package android.app.usage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class AppStandbyInfo
  implements Parcelable
{
  public static final Parcelable.Creator<AppStandbyInfo> CREATOR = new Parcelable.Creator()
  {
    public AppStandbyInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AppStandbyInfo(paramAnonymousParcel, null);
    }
    
    public AppStandbyInfo[] newArray(int paramAnonymousInt)
    {
      return new AppStandbyInfo[paramAnonymousInt];
    }
  };
  public String mPackageName;
  public int mStandbyBucket;
  
  private AppStandbyInfo(Parcel paramParcel)
  {
    mPackageName = paramParcel.readString();
    mStandbyBucket = paramParcel.readInt();
  }
  
  public AppStandbyInfo(String paramString, int paramInt)
  {
    mPackageName = paramString;
    mStandbyBucket = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    paramParcel.writeInt(mStandbyBucket);
  }
}
