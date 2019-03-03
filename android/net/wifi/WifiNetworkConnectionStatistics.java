package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class WifiNetworkConnectionStatistics
  implements Parcelable
{
  public static final Parcelable.Creator<WifiNetworkConnectionStatistics> CREATOR = new Parcelable.Creator()
  {
    public WifiNetworkConnectionStatistics createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WifiNetworkConnectionStatistics(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public WifiNetworkConnectionStatistics[] newArray(int paramAnonymousInt)
    {
      return new WifiNetworkConnectionStatistics[paramAnonymousInt];
    }
  };
  private static final String TAG = "WifiNetworkConnnectionStatistics";
  public int numConnection;
  public int numUsage;
  
  public WifiNetworkConnectionStatistics() {}
  
  public WifiNetworkConnectionStatistics(int paramInt1, int paramInt2)
  {
    numConnection = paramInt1;
    numUsage = paramInt2;
  }
  
  public WifiNetworkConnectionStatistics(WifiNetworkConnectionStatistics paramWifiNetworkConnectionStatistics)
  {
    numConnection = numConnection;
    numUsage = numUsage;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("c=");
    localStringBuilder.append(numConnection);
    localStringBuilder.append(" u=");
    localStringBuilder.append(numUsage);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(numConnection);
    paramParcel.writeInt(numUsage);
  }
}
