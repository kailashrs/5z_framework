package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.InetAddress;

public class DhcpInfo
  implements Parcelable
{
  public static final Parcelable.Creator<DhcpInfo> CREATOR = new Parcelable.Creator()
  {
    public DhcpInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      DhcpInfo localDhcpInfo = new DhcpInfo();
      ipAddress = paramAnonymousParcel.readInt();
      gateway = paramAnonymousParcel.readInt();
      netmask = paramAnonymousParcel.readInt();
      dns1 = paramAnonymousParcel.readInt();
      dns2 = paramAnonymousParcel.readInt();
      serverAddress = paramAnonymousParcel.readInt();
      leaseDuration = paramAnonymousParcel.readInt();
      return localDhcpInfo;
    }
    
    public DhcpInfo[] newArray(int paramAnonymousInt)
    {
      return new DhcpInfo[paramAnonymousInt];
    }
  };
  public int dns1;
  public int dns2;
  public int gateway;
  public int ipAddress;
  public int leaseDuration;
  public int netmask;
  public int serverAddress;
  
  public DhcpInfo() {}
  
  public DhcpInfo(DhcpInfo paramDhcpInfo)
  {
    if (paramDhcpInfo != null)
    {
      ipAddress = ipAddress;
      gateway = gateway;
      netmask = netmask;
      dns1 = dns1;
      dns2 = dns2;
      serverAddress = serverAddress;
      leaseDuration = leaseDuration;
    }
  }
  
  private static void putAddress(StringBuffer paramStringBuffer, int paramInt)
  {
    paramStringBuffer.append(NetworkUtils.intToInetAddress(paramInt).getHostAddress());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("ipaddr ");
    putAddress(localStringBuffer, ipAddress);
    localStringBuffer.append(" gateway ");
    putAddress(localStringBuffer, gateway);
    localStringBuffer.append(" netmask ");
    putAddress(localStringBuffer, netmask);
    localStringBuffer.append(" dns1 ");
    putAddress(localStringBuffer, dns1);
    localStringBuffer.append(" dns2 ");
    putAddress(localStringBuffer, dns2);
    localStringBuffer.append(" DHCP server ");
    putAddress(localStringBuffer, serverAddress);
    localStringBuffer.append(" lease ");
    localStringBuffer.append(leaseDuration);
    localStringBuffer.append(" seconds");
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(ipAddress);
    paramParcel.writeInt(gateway);
    paramParcel.writeInt(netmask);
    paramParcel.writeInt(dns1);
    paramParcel.writeInt(dns2);
    paramParcel.writeInt(serverAddress);
    paramParcel.writeInt(leaseDuration);
  }
}
