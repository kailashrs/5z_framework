package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@Deprecated
public class NetworkQuotaInfo
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkQuotaInfo> CREATOR = new Parcelable.Creator()
  {
    public NetworkQuotaInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkQuotaInfo(paramAnonymousParcel);
    }
    
    public NetworkQuotaInfo[] newArray(int paramAnonymousInt)
    {
      return new NetworkQuotaInfo[paramAnonymousInt];
    }
  };
  public static final long NO_LIMIT = -1L;
  
  public NetworkQuotaInfo() {}
  
  public NetworkQuotaInfo(Parcel paramParcel) {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getEstimatedBytes()
  {
    return 0L;
  }
  
  public long getHardLimitBytes()
  {
    return -1L;
  }
  
  public long getSoftLimitBytes()
  {
    return -1L;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {}
}
