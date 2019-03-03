package android.net.wifi.p2p;

import android.net.wifi.WpsInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WifiP2pConfig
  implements Parcelable
{
  public static final Parcelable.Creator<WifiP2pConfig> CREATOR = new Parcelable.Creator()
  {
    public WifiP2pConfig createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiP2pConfig localWifiP2pConfig = new WifiP2pConfig();
      deviceAddress = paramAnonymousParcel.readString();
      wps = ((WpsInfo)paramAnonymousParcel.readParcelable(null));
      groupOwnerIntent = paramAnonymousParcel.readInt();
      netId = paramAnonymousParcel.readInt();
      return localWifiP2pConfig;
    }
    
    public WifiP2pConfig[] newArray(int paramAnonymousInt)
    {
      return new WifiP2pConfig[paramAnonymousInt];
    }
  };
  public static final int MAX_GROUP_OWNER_INTENT = 15;
  public static final int MIN_GROUP_OWNER_INTENT = 0;
  public String deviceAddress = "";
  public int groupOwnerIntent = -1;
  public int netId = -2;
  public WpsInfo wps;
  
  public WifiP2pConfig()
  {
    wps = new WpsInfo();
    wps.setup = 0;
  }
  
  public WifiP2pConfig(WifiP2pConfig paramWifiP2pConfig)
  {
    if (paramWifiP2pConfig != null)
    {
      deviceAddress = deviceAddress;
      wps = new WpsInfo(wps);
      groupOwnerIntent = groupOwnerIntent;
      netId = netId;
    }
  }
  
  public WifiP2pConfig(String paramString)
    throws IllegalArgumentException
  {
    paramString = paramString.split(" ");
    if ((paramString.length >= 2) && (paramString[0].equals("P2P-GO-NEG-REQUEST")))
    {
      deviceAddress = paramString[1];
      wps = new WpsInfo();
      if (paramString.length > 2)
      {
        paramString = paramString[2].split("=");
        int i;
        try
        {
          i = Integer.parseInt(paramString[1]);
        }
        catch (NumberFormatException paramString)
        {
          i = 0;
        }
        if (i != 1) {
          switch (i)
          {
          default: 
            wps.setup = 0;
            break;
          case 5: 
            wps.setup = 2;
            break;
          case 4: 
            wps.setup = 0;
            break;
          }
        } else {
          wps.setup = 1;
        }
      }
      return;
    }
    throw new IllegalArgumentException("Malformed supplicant event");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void invalidate()
  {
    deviceAddress = "";
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("\n address: ");
    localStringBuffer.append(deviceAddress);
    localStringBuffer.append("\n wps: ");
    localStringBuffer.append(wps);
    localStringBuffer.append("\n groupOwnerIntent: ");
    localStringBuffer.append(groupOwnerIntent);
    localStringBuffer.append("\n persist: ");
    localStringBuffer.append(netId);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(deviceAddress);
    paramParcel.writeParcelable(wps, paramInt);
    paramParcel.writeInt(groupOwnerIntent);
    paramParcel.writeInt(netId);
  }
}
