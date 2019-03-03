package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class NetworkState
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkState> CREATOR = new Parcelable.Creator()
  {
    public NetworkState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkState(paramAnonymousParcel);
    }
    
    public NetworkState[] newArray(int paramAnonymousInt)
    {
      return new NetworkState[paramAnonymousInt];
    }
  };
  public static final NetworkState EMPTY = new NetworkState(null, null, null, null, null, null);
  private static final boolean SANITY_CHECK_ROAMING = false;
  public final LinkProperties linkProperties;
  public final Network network;
  public final NetworkCapabilities networkCapabilities;
  public final String networkId;
  public final NetworkInfo networkInfo;
  public final String subscriberId;
  
  public NetworkState(NetworkInfo paramNetworkInfo, LinkProperties paramLinkProperties, NetworkCapabilities paramNetworkCapabilities, Network paramNetwork, String paramString1, String paramString2)
  {
    networkInfo = paramNetworkInfo;
    linkProperties = paramLinkProperties;
    networkCapabilities = paramNetworkCapabilities;
    network = paramNetwork;
    subscriberId = paramString1;
    networkId = paramString2;
  }
  
  public NetworkState(Parcel paramParcel)
  {
    networkInfo = ((NetworkInfo)paramParcel.readParcelable(null));
    linkProperties = ((LinkProperties)paramParcel.readParcelable(null));
    networkCapabilities = ((NetworkCapabilities)paramParcel.readParcelable(null));
    network = ((Network)paramParcel.readParcelable(null));
    subscriberId = paramParcel.readString();
    networkId = paramParcel.readString();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(networkInfo, paramInt);
    paramParcel.writeParcelable(linkProperties, paramInt);
    paramParcel.writeParcelable(networkCapabilities, paramInt);
    paramParcel.writeParcelable(network, paramInt);
    paramParcel.writeString(subscriberId);
    paramParcel.writeString(networkId);
  }
}
