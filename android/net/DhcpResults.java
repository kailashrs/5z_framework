package android.net;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Objects;

public class DhcpResults
  extends StaticIpConfiguration
{
  public static final Parcelable.Creator<DhcpResults> CREATOR = new Parcelable.Creator()
  {
    public DhcpResults createFromParcel(Parcel paramAnonymousParcel)
    {
      DhcpResults localDhcpResults = new DhcpResults();
      DhcpResults.readFromParcel(localDhcpResults, paramAnonymousParcel);
      return localDhcpResults;
    }
    
    public DhcpResults[] newArray(int paramAnonymousInt)
    {
      return new DhcpResults[paramAnonymousInt];
    }
  };
  private static final String TAG = "DhcpResults";
  public int leaseDuration;
  public int mtu;
  public Inet4Address serverAddress;
  public String vendorInfo;
  
  public DhcpResults() {}
  
  public DhcpResults(DhcpResults paramDhcpResults)
  {
    super(paramDhcpResults);
    if (paramDhcpResults != null)
    {
      serverAddress = serverAddress;
      vendorInfo = vendorInfo;
      leaseDuration = leaseDuration;
      mtu = mtu;
    }
  }
  
  public DhcpResults(StaticIpConfiguration paramStaticIpConfiguration)
  {
    super(paramStaticIpConfiguration);
  }
  
  private static void readFromParcel(DhcpResults paramDhcpResults, Parcel paramParcel)
  {
    StaticIpConfiguration.readFromParcel(paramDhcpResults, paramParcel);
    leaseDuration = paramParcel.readInt();
    mtu = paramParcel.readInt();
    serverAddress = ((Inet4Address)NetworkUtils.unparcelInetAddress(paramParcel));
    vendorInfo = paramParcel.readString();
  }
  
  public boolean addDns(String paramString)
  {
    if (!TextUtils.isEmpty(paramString)) {
      try
      {
        dnsServers.add(NetworkUtils.numericToInetAddress(paramString));
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("addDns failed with addrString ");
        localStringBuilder.append(paramString);
        Log.e("DhcpResults", localStringBuilder.toString());
        return true;
      }
    }
    return false;
  }
  
  public void clear()
  {
    super.clear();
    vendorInfo = null;
    leaseDuration = 0;
    mtu = 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof DhcpResults)) {
      return false;
    }
    DhcpResults localDhcpResults = (DhcpResults)paramObject;
    if ((!super.equals((StaticIpConfiguration)paramObject)) || (!Objects.equals(serverAddress, serverAddress)) || (!Objects.equals(vendorInfo, vendorInfo)) || (leaseDuration != leaseDuration) || (mtu != mtu)) {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasMeteredHint()
  {
    if (vendorInfo != null) {
      return vendorInfo.contains("ANDROID_METERED");
    }
    return false;
  }
  
  public void setDomains(String paramString)
  {
    domains = paramString;
  }
  
  public boolean setGateway(String paramString)
  {
    try
    {
      gateway = NetworkUtils.numericToInetAddress(paramString);
      return false;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setGateway failed with addrString ");
      localStringBuilder.append(paramString);
      Log.e("DhcpResults", localStringBuilder.toString());
    }
    return true;
  }
  
  public boolean setIpAddress(String paramString, int paramInt)
  {
    try
    {
      Inet4Address localInet4Address = (Inet4Address)NetworkUtils.numericToInetAddress(paramString);
      LinkAddress localLinkAddress = new android/net/LinkAddress;
      localLinkAddress.<init>(localInet4Address, paramInt);
      ipAddress = localLinkAddress;
      return false;
    }
    catch (IllegalArgumentException|ClassCastException localIllegalArgumentException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setIpAddress failed with addrString ");
      localStringBuilder.append(paramString);
      localStringBuilder.append("/");
      localStringBuilder.append(paramInt);
      Log.e("DhcpResults", localStringBuilder.toString());
    }
    return true;
  }
  
  public void setLeaseDuration(int paramInt)
  {
    leaseDuration = paramInt;
  }
  
  public boolean setServerAddress(String paramString)
  {
    try
    {
      serverAddress = ((Inet4Address)NetworkUtils.numericToInetAddress(paramString));
      return false;
    }
    catch (IllegalArgumentException|ClassCastException localIllegalArgumentException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setServerAddress failed with addrString ");
      localStringBuilder.append(paramString);
      Log.e("DhcpResults", localStringBuilder.toString());
    }
    return true;
  }
  
  public void setVendorInfo(String paramString)
  {
    vendorInfo = paramString;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(super.toString());
    localStringBuffer.append(" DHCP server ");
    localStringBuffer.append(serverAddress);
    localStringBuffer.append(" Vendor info ");
    localStringBuffer.append(vendorInfo);
    localStringBuffer.append(" lease ");
    localStringBuffer.append(leaseDuration);
    localStringBuffer.append(" seconds");
    if (mtu != 0)
    {
      localStringBuffer.append(" MTU ");
      localStringBuffer.append(mtu);
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(leaseDuration);
    paramParcel.writeInt(mtu);
    NetworkUtils.parcelInetAddress(paramParcel, serverAddress, paramInt);
    paramParcel.writeString(vendorInfo);
  }
}
