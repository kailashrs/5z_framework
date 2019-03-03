package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class StaticIpConfiguration
  implements Parcelable
{
  public static Parcelable.Creator<StaticIpConfiguration> CREATOR = new Parcelable.Creator()
  {
    public StaticIpConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      StaticIpConfiguration localStaticIpConfiguration = new StaticIpConfiguration();
      StaticIpConfiguration.readFromParcel(localStaticIpConfiguration, paramAnonymousParcel);
      return localStaticIpConfiguration;
    }
    
    public StaticIpConfiguration[] newArray(int paramAnonymousInt)
    {
      return new StaticIpConfiguration[paramAnonymousInt];
    }
  };
  public final ArrayList<InetAddress> dnsServers = new ArrayList();
  public String domains;
  public InetAddress gateway;
  public LinkAddress ipAddress;
  
  public StaticIpConfiguration() {}
  
  public StaticIpConfiguration(StaticIpConfiguration paramStaticIpConfiguration)
  {
    this();
    if (paramStaticIpConfiguration != null)
    {
      ipAddress = ipAddress;
      gateway = gateway;
      dnsServers.addAll(dnsServers);
      domains = domains;
    }
  }
  
  protected static void readFromParcel(StaticIpConfiguration paramStaticIpConfiguration, Parcel paramParcel)
  {
    ipAddress = ((LinkAddress)paramParcel.readParcelable(null));
    gateway = NetworkUtils.unparcelInetAddress(paramParcel);
    dnsServers.clear();
    int i = paramParcel.readInt();
    for (int j = 0; j < i; j++) {
      dnsServers.add(NetworkUtils.unparcelInetAddress(paramParcel));
    }
    domains = paramParcel.readString();
  }
  
  public void clear()
  {
    ipAddress = null;
    gateway = null;
    dnsServers.clear();
    domains = null;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof StaticIpConfiguration)) {
      return false;
    }
    paramObject = (StaticIpConfiguration)paramObject;
    if ((paramObject == null) || (!Objects.equals(ipAddress, ipAddress)) || (!Objects.equals(gateway, gateway)) || (!dnsServers.equals(dnsServers)) || (!Objects.equals(domains, domains))) {
      bool = false;
    }
    return bool;
  }
  
  public List<RouteInfo> getRoutes(String paramString)
  {
    ArrayList localArrayList = new ArrayList(3);
    if (ipAddress != null)
    {
      RouteInfo localRouteInfo = new RouteInfo(ipAddress, null, paramString);
      localArrayList.add(localRouteInfo);
      if ((gateway != null) && (!localRouteInfo.matches(gateway))) {
        localArrayList.add(RouteInfo.makeHostRoute(gateway, paramString));
      }
    }
    if (gateway != null) {
      localArrayList.add(new RouteInfo((IpPrefix)null, gateway, paramString));
    }
    return localArrayList;
  }
  
  public int hashCode()
  {
    LinkAddress localLinkAddress = ipAddress;
    int i = 0;
    int j;
    if (localLinkAddress == null) {
      j = 0;
    } else {
      j = ipAddress.hashCode();
    }
    int k;
    if (gateway == null) {
      k = 0;
    } else {
      k = gateway.hashCode();
    }
    if (domains != null) {
      i = domains.hashCode();
    }
    return 47 * (47 * (47 * (47 * 13 + j) + k) + i) + dnsServers.hashCode();
  }
  
  public LinkProperties toLinkProperties(String paramString)
  {
    LinkProperties localLinkProperties = new LinkProperties();
    localLinkProperties.setInterfaceName(paramString);
    if (ipAddress != null) {
      localLinkProperties.addLinkAddress(ipAddress);
    }
    paramString = getRoutes(paramString).iterator();
    while (paramString.hasNext()) {
      localLinkProperties.addRoute((RouteInfo)paramString.next());
    }
    paramString = dnsServers.iterator();
    while (paramString.hasNext()) {
      localLinkProperties.addDnsServer((InetAddress)paramString.next());
    }
    localLinkProperties.setDomains(domains);
    return localLinkProperties;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("IP address ");
    if (ipAddress != null)
    {
      localStringBuffer.append(ipAddress);
      localStringBuffer.append(" ");
    }
    localStringBuffer.append("Gateway ");
    if (gateway != null)
    {
      localStringBuffer.append(gateway.getHostAddress());
      localStringBuffer.append(" ");
    }
    localStringBuffer.append(" DNS servers: [");
    Iterator localIterator = dnsServers.iterator();
    while (localIterator.hasNext())
    {
      InetAddress localInetAddress = (InetAddress)localIterator.next();
      localStringBuffer.append(" ");
      localStringBuffer.append(localInetAddress.getHostAddress());
    }
    localStringBuffer.append(" ] Domains ");
    if (domains != null) {
      localStringBuffer.append(domains);
    }
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(ipAddress, paramInt);
    NetworkUtils.parcelInetAddress(paramParcel, gateway, paramInt);
    paramParcel.writeInt(dnsServers.size());
    Iterator localIterator = dnsServers.iterator();
    while (localIterator.hasNext()) {
      NetworkUtils.parcelInetAddress(paramParcel, (InetAddress)localIterator.next(), paramInt);
    }
    paramParcel.writeString(domains);
  }
}
