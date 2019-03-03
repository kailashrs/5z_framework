package android.media.tv;

import android.annotation.SystemApi;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class TvContentRatingSystemInfo
  implements Parcelable
{
  public static final Parcelable.Creator<TvContentRatingSystemInfo> CREATOR = new Parcelable.Creator()
  {
    public TvContentRatingSystemInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TvContentRatingSystemInfo(paramAnonymousParcel, null);
    }
    
    public TvContentRatingSystemInfo[] newArray(int paramAnonymousInt)
    {
      return new TvContentRatingSystemInfo[paramAnonymousInt];
    }
  };
  private final ApplicationInfo mApplicationInfo;
  private final Uri mXmlUri;
  
  private TvContentRatingSystemInfo(Uri paramUri, ApplicationInfo paramApplicationInfo)
  {
    mXmlUri = paramUri;
    mApplicationInfo = paramApplicationInfo;
  }
  
  private TvContentRatingSystemInfo(Parcel paramParcel)
  {
    mXmlUri = ((Uri)paramParcel.readParcelable(null));
    mApplicationInfo = ((ApplicationInfo)paramParcel.readParcelable(null));
  }
  
  public static final TvContentRatingSystemInfo createTvContentRatingSystemInfo(int paramInt, ApplicationInfo paramApplicationInfo)
  {
    return new TvContentRatingSystemInfo(new Uri.Builder().scheme("android.resource").authority(packageName).appendPath(String.valueOf(paramInt)).build(), paramApplicationInfo);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public final Uri getXmlUri()
  {
    return mXmlUri;
  }
  
  public final boolean isSystemDefined()
  {
    int i = mApplicationInfo.flags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mXmlUri, paramInt);
    paramParcel.writeParcelable(mApplicationInfo, paramInt);
  }
}
