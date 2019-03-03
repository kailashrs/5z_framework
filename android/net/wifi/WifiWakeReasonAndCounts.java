package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WifiWakeReasonAndCounts
  implements Parcelable
{
  public static final Parcelable.Creator<WifiWakeReasonAndCounts> CREATOR = new Parcelable.Creator()
  {
    public WifiWakeReasonAndCounts createFromParcel(Parcel paramAnonymousParcel)
    {
      WifiWakeReasonAndCounts localWifiWakeReasonAndCounts = new WifiWakeReasonAndCounts();
      totalCmdEventWake = paramAnonymousParcel.readInt();
      totalDriverFwLocalWake = paramAnonymousParcel.readInt();
      totalRxDataWake = paramAnonymousParcel.readInt();
      rxUnicast = paramAnonymousParcel.readInt();
      rxMulticast = paramAnonymousParcel.readInt();
      rxBroadcast = paramAnonymousParcel.readInt();
      icmp = paramAnonymousParcel.readInt();
      icmp6 = paramAnonymousParcel.readInt();
      icmp6Ra = paramAnonymousParcel.readInt();
      icmp6Na = paramAnonymousParcel.readInt();
      icmp6Ns = paramAnonymousParcel.readInt();
      ipv4RxMulticast = paramAnonymousParcel.readInt();
      ipv6Multicast = paramAnonymousParcel.readInt();
      otherRxMulticast = paramAnonymousParcel.readInt();
      paramAnonymousParcel.readIntArray(cmdEventWakeCntArray);
      paramAnonymousParcel.readIntArray(driverFWLocalWakeCntArray);
      return localWifiWakeReasonAndCounts;
    }
    
    public WifiWakeReasonAndCounts[] newArray(int paramAnonymousInt)
    {
      return new WifiWakeReasonAndCounts[paramAnonymousInt];
    }
  };
  private static final String TAG = "WifiWakeReasonAndCounts";
  public int[] cmdEventWakeCntArray;
  public int[] driverFWLocalWakeCntArray;
  public int icmp;
  public int icmp6;
  public int icmp6Na;
  public int icmp6Ns;
  public int icmp6Ra;
  public int ipv4RxMulticast;
  public int ipv6Multicast;
  public int otherRxMulticast;
  public int rxBroadcast;
  public int rxMulticast;
  public int rxUnicast;
  public int totalCmdEventWake;
  public int totalDriverFwLocalWake;
  public int totalRxDataWake;
  
  public WifiWakeReasonAndCounts() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(" totalCmdEventWake ");
    localStringBuffer.append(totalCmdEventWake);
    localStringBuffer.append(" totalDriverFwLocalWake ");
    localStringBuffer.append(totalDriverFwLocalWake);
    localStringBuffer.append(" totalRxDataWake ");
    localStringBuffer.append(totalRxDataWake);
    localStringBuffer.append(" rxUnicast ");
    localStringBuffer.append(rxUnicast);
    localStringBuffer.append(" rxMulticast ");
    localStringBuffer.append(rxMulticast);
    localStringBuffer.append(" rxBroadcast ");
    localStringBuffer.append(rxBroadcast);
    localStringBuffer.append(" icmp ");
    localStringBuffer.append(icmp);
    localStringBuffer.append(" icmp6 ");
    localStringBuffer.append(icmp6);
    localStringBuffer.append(" icmp6Ra ");
    localStringBuffer.append(icmp6Ra);
    localStringBuffer.append(" icmp6Na ");
    localStringBuffer.append(icmp6Na);
    localStringBuffer.append(" icmp6Ns ");
    localStringBuffer.append(icmp6Ns);
    localStringBuffer.append(" ipv4RxMulticast ");
    localStringBuffer.append(ipv4RxMulticast);
    localStringBuffer.append(" ipv6Multicast ");
    localStringBuffer.append(ipv6Multicast);
    localStringBuffer.append(" otherRxMulticast ");
    localStringBuffer.append(otherRxMulticast);
    int i = 0;
    StringBuilder localStringBuilder;
    for (int j = 0; j < cmdEventWakeCntArray.length; j++)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(" cmdEventWakeCntArray[");
      localStringBuilder.append(j);
      localStringBuilder.append("] ");
      localStringBuilder.append(cmdEventWakeCntArray[j]);
      localStringBuffer.append(localStringBuilder.toString());
    }
    for (j = i; j < driverFWLocalWakeCntArray.length; j++)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(" driverFWLocalWakeCntArray[");
      localStringBuilder.append(j);
      localStringBuilder.append("] ");
      localStringBuilder.append(driverFWLocalWakeCntArray[j]);
      localStringBuffer.append(localStringBuilder.toString());
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(totalCmdEventWake);
    paramParcel.writeInt(totalDriverFwLocalWake);
    paramParcel.writeInt(totalRxDataWake);
    paramParcel.writeInt(rxUnicast);
    paramParcel.writeInt(rxMulticast);
    paramParcel.writeInt(rxBroadcast);
    paramParcel.writeInt(icmp);
    paramParcel.writeInt(icmp6);
    paramParcel.writeInt(icmp6Ra);
    paramParcel.writeInt(icmp6Na);
    paramParcel.writeInt(icmp6Ns);
    paramParcel.writeInt(ipv4RxMulticast);
    paramParcel.writeInt(ipv6Multicast);
    paramParcel.writeInt(otherRxMulticast);
    paramParcel.writeIntArray(cmdEventWakeCntArray);
    paramParcel.writeIntArray(driverFWLocalWakeCntArray);
  }
}
