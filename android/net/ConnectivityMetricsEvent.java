package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.BitUtils;

public final class ConnectivityMetricsEvent
  implements Parcelable
{
  public static final Parcelable.Creator<ConnectivityMetricsEvent> CREATOR = new Parcelable.Creator()
  {
    public ConnectivityMetricsEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ConnectivityMetricsEvent(paramAnonymousParcel, null);
    }
    
    public ConnectivityMetricsEvent[] newArray(int paramAnonymousInt)
    {
      return new ConnectivityMetricsEvent[paramAnonymousInt];
    }
  };
  public Parcelable data;
  public String ifname;
  public int netId;
  public long timestamp;
  public long transports;
  
  public ConnectivityMetricsEvent() {}
  
  private ConnectivityMetricsEvent(Parcel paramParcel)
  {
    timestamp = paramParcel.readLong();
    transports = paramParcel.readLong();
    netId = paramParcel.readInt();
    ifname = paramParcel.readString();
    data = paramParcel.readParcelable(null);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("ConnectivityMetricsEvent(");
    long l = timestamp;
    int i = 0;
    localStringBuilder.append(String.format("%tT.%tL", new Object[] { Long.valueOf(l), Long.valueOf(timestamp) }));
    if (netId != 0)
    {
      localStringBuilder.append(", ");
      localStringBuilder.append("netId=");
      localStringBuilder.append(netId);
    }
    if (ifname != null)
    {
      localStringBuilder.append(", ");
      localStringBuilder.append(ifname);
    }
    int[] arrayOfInt = BitUtils.unpackBits(transports);
    int j = arrayOfInt.length;
    while (i < j)
    {
      int k = arrayOfInt[i];
      localStringBuilder.append(", ");
      localStringBuilder.append(NetworkCapabilities.transportNameOf(k));
      i++;
    }
    localStringBuilder.append("): ");
    localStringBuilder.append(data.toString());
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(timestamp);
    paramParcel.writeLong(transports);
    paramParcel.writeInt(netId);
    paramParcel.writeString(ifname);
    paramParcel.writeParcelable(data, 0);
  }
}
