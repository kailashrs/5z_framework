package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RssiPacketCountInfo
  implements Parcelable
{
  public static final Parcelable.Creator<RssiPacketCountInfo> CREATOR = new Parcelable.Creator()
  {
    public RssiPacketCountInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RssiPacketCountInfo(paramAnonymousParcel, null);
    }
    
    public RssiPacketCountInfo[] newArray(int paramAnonymousInt)
    {
      return new RssiPacketCountInfo[paramAnonymousInt];
    }
  };
  public int rssi;
  public int rxgood;
  public int txbad;
  public int txgood;
  
  public RssiPacketCountInfo()
  {
    rxgood = 0;
    txbad = 0;
    txgood = 0;
    rssi = 0;
  }
  
  private RssiPacketCountInfo(Parcel paramParcel)
  {
    rssi = paramParcel.readInt();
    txgood = paramParcel.readInt();
    txbad = paramParcel.readInt();
    rxgood = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(rssi);
    paramParcel.writeInt(txgood);
    paramParcel.writeInt(txbad);
    paramParcel.writeInt(rxgood);
  }
}
