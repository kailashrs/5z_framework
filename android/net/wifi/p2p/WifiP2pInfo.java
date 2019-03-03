package android.net.wifi.p2p;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class WifiP2pInfo
  implements Parcelable
{
  public static final Parcelable.Creator<WifiP2pInfo> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiP2pInfo localWifiP2pInfo = new WifiP2pInfo();
      int i = paramAnonymousParcel.readByte();
      boolean bool1 = false;
      if (i == 1) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      groupFormed = bool2;
      boolean bool2 = bool1;
      if (paramAnonymousParcel.readByte() == 1) {
        bool2 = true;
      }
      isGroupOwner = bool2;
      if (paramAnonymousParcel.readByte() == 1) {
        try
        {
          groupOwnerAddress = InetAddress.getByAddress(paramAnonymousParcel.createByteArray());
        }
        catch (UnknownHostException paramAnonymousParcel) {}
      }
      return localWifiP2pInfo;
    }
    
    public WifiP2pInfo[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pInfo[paramAnonymousInt];
    }
  };
  public boolean groupFormed;
  public InetAddress groupOwnerAddress;
  public boolean isGroupOwner;
  
  public WifiP2pInfo() {}
  
  public WifiP2pInfo(WifiP2pInfo paramWifiP2pInfo)
  {
    if (paramWifiP2pInfo != null)
    {
      groupFormed = groupFormed;
      isGroupOwner = isGroupOwner;
      groupOwnerAddress = groupOwnerAddress;
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("groupFormed: ");
    localStringBuffer.append(groupFormed);
    localStringBuffer.append(" isGroupOwner: ");
    localStringBuffer.append(isGroupOwner);
    localStringBuffer.append(" groupOwnerAddress: ");
    localStringBuffer.append(groupOwnerAddress);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByte(groupFormed);
    paramParcel.writeByte(isGroupOwner);
    if (groupOwnerAddress != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeByteArray(groupOwnerAddress.getAddress());
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
  }
}
