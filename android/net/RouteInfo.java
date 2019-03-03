package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public final class RouteInfo
  implements Parcelable
{
  public static final Parcelable.Creator<RouteInfo> CREATOR = new Parcelable.Creator()
  {
    public RouteInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      IpPrefix localIpPrefix = (IpPrefix)paramAnonymousParcel.readParcelable(null);
      Object localObject1 = null;
      Object localObject2 = paramAnonymousParcel.createByteArray();
      try
      {
        localObject2 = InetAddress.getByAddress((byte[])localObject2);
        localObject1 = localObject2;
      }
      catch (UnknownHostException localUnknownHostException) {}
      return new RouteInfo(localIpPrefix, localObject1, paramAnonymousParcel.readString(), paramAnonymousParcel.readInt());
    }
    
    public RouteInfo[] newArray(int paramAnonymousInt)
    {
      return new RouteInfo[paramAnonymousInt];
    }
  };
  public static final int RTN_THROW = 9;
  public static final int RTN_UNICAST = 1;
  public static final int RTN_UNREACHABLE = 7;
  private final IpPrefix mDestination;
  private final InetAddress mGateway;
  private final boolean mHasGateway;
  private final String mInterface;
  private final boolean mIsHost;
  private final int mType;
  
  public RouteInfo(IpPrefix paramIpPrefix)
  {
    this(paramIpPrefix, null, null);
  }
  
  public RouteInfo(IpPrefix paramIpPrefix, int paramInt)
  {
    this(paramIpPrefix, null, null, paramInt);
  }
  
  public RouteInfo(IpPrefix paramIpPrefix, InetAddress paramInetAddress)
  {
    this(paramIpPrefix, paramInetAddress, null);
  }
  
  public RouteInfo(IpPrefix paramIpPrefix, InetAddress paramInetAddress, String paramString)
  {
    this(paramIpPrefix, paramInetAddress, paramString, 1);
  }
  
  public RouteInfo(IpPrefix paramIpPrefix, InetAddress paramInetAddress, String paramString, int paramInt)
  {
    if ((paramInt != 1) && (paramInt != 7) && (paramInt != 9))
    {
      paramIpPrefix = new StringBuilder();
      paramIpPrefix.append("Unknown route type ");
      paramIpPrefix.append(paramInt);
      throw new IllegalArgumentException(paramIpPrefix.toString());
    }
    IpPrefix localIpPrefix = paramIpPrefix;
    if (paramIpPrefix == null) {
      if (paramInetAddress != null)
      {
        if ((paramInetAddress instanceof Inet4Address)) {
          localIpPrefix = new IpPrefix(Inet4Address.ANY, 0);
        } else {
          localIpPrefix = new IpPrefix(Inet6Address.ANY, 0);
        }
      }
      else
      {
        paramString = new StringBuilder();
        paramString.append("Invalid arguments passed in: ");
        paramString.append(paramInetAddress);
        paramString.append(",");
        paramString.append(paramIpPrefix);
        throw new IllegalArgumentException(paramString.toString());
      }
    }
    paramIpPrefix = paramInetAddress;
    if (paramInetAddress == null) {
      if ((localIpPrefix.getAddress() instanceof Inet4Address)) {
        paramIpPrefix = Inet4Address.ANY;
      } else {
        paramIpPrefix = Inet6Address.ANY;
      }
    }
    mHasGateway = (true ^ paramIpPrefix.isAnyLocalAddress());
    if ((((localIpPrefix.getAddress() instanceof Inet4Address)) && (!(paramIpPrefix instanceof Inet4Address))) || (((localIpPrefix.getAddress() instanceof Inet6Address)) && (!(paramIpPrefix instanceof Inet6Address)))) {
      throw new IllegalArgumentException("address family mismatch in RouteInfo constructor");
    }
    mDestination = localIpPrefix;
    mGateway = paramIpPrefix;
    mInterface = paramString;
    mType = paramInt;
    mIsHost = isHost();
  }
  
  public RouteInfo(LinkAddress paramLinkAddress)
  {
    this(paramLinkAddress, null, null);
  }
  
  public RouteInfo(LinkAddress paramLinkAddress, InetAddress paramInetAddress)
  {
    this(paramLinkAddress, paramInetAddress, null);
  }
  
  public RouteInfo(LinkAddress paramLinkAddress, InetAddress paramInetAddress, String paramString)
  {
    this(paramLinkAddress, paramInetAddress, paramString);
  }
  
  public RouteInfo(InetAddress paramInetAddress)
  {
    this((IpPrefix)null, paramInetAddress, null);
  }
  
  private boolean isHost()
  {
    boolean bool;
    if ((((mDestination.getAddress() instanceof Inet4Address)) && (mDestination.getPrefixLength() == 32)) || (((mDestination.getAddress() instanceof Inet6Address)) && (mDestination.getPrefixLength() == 128))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static RouteInfo makeHostRoute(InetAddress paramInetAddress, String paramString)
  {
    return makeHostRoute(paramInetAddress, null, paramString);
  }
  
  public static RouteInfo makeHostRoute(InetAddress paramInetAddress1, InetAddress paramInetAddress2, String paramString)
  {
    if (paramInetAddress1 == null) {
      return null;
    }
    if ((paramInetAddress1 instanceof Inet4Address)) {
      return new RouteInfo(new IpPrefix(paramInetAddress1, 32), paramInetAddress2, paramString);
    }
    return new RouteInfo(new IpPrefix(paramInetAddress1, 128), paramInetAddress2, paramString);
  }
  
  public static RouteInfo selectBestRoute(Collection<RouteInfo> paramCollection, InetAddress paramInetAddress)
  {
    if ((paramCollection != null) && (paramInetAddress != null))
    {
      Object localObject = null;
      Iterator localIterator = paramCollection.iterator();
      for (paramCollection = (Collection<RouteInfo>)localObject; localIterator.hasNext(); paramCollection = (Collection<RouteInfo>)localObject)
      {
        label22:
        RouteInfo localRouteInfo = (RouteInfo)localIterator.next();
        localObject = paramCollection;
        if (NetworkUtils.addressTypeMatches(mDestination.getAddress(), paramInetAddress))
        {
          if ((paramCollection != null) && (mDestination.getPrefixLength() >= mDestination.getPrefixLength())) {
            break label22;
          }
          localObject = paramCollection;
          if (localRouteInfo.matches(paramInetAddress)) {
            localObject = localRouteInfo;
          }
        }
      }
      return paramCollection;
    }
    return null;
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
    if (!(paramObject instanceof RouteInfo)) {
      return false;
    }
    paramObject = (RouteInfo)paramObject;
    if ((!Objects.equals(mDestination, paramObject.getDestination())) || (!Objects.equals(mGateway, paramObject.getGateway())) || (!Objects.equals(mInterface, paramObject.getInterface())) || (mType != paramObject.getType())) {
      bool = false;
    }
    return bool;
  }
  
  public IpPrefix getDestination()
  {
    return mDestination;
  }
  
  public LinkAddress getDestinationLinkAddress()
  {
    return new LinkAddress(mDestination.getAddress(), mDestination.getPrefixLength());
  }
  
  public InetAddress getGateway()
  {
    return mGateway;
  }
  
  public String getInterface()
  {
    return mInterface;
  }
  
  public int getType()
  {
    return mType;
  }
  
  public boolean hasGateway()
  {
    return mHasGateway;
  }
  
  public int hashCode()
  {
    int i = mDestination.hashCode();
    InetAddress localInetAddress = mGateway;
    int j = 0;
    int k;
    if (localInetAddress == null) {
      k = 0;
    } else {
      k = mGateway.hashCode() * 47;
    }
    if (mInterface != null) {
      j = mInterface.hashCode() * 67;
    }
    return i * 41 + k + j + mType * 71;
  }
  
  public boolean isDefaultRoute()
  {
    int i = mType;
    boolean bool = true;
    if ((i != 1) || (mDestination.getPrefixLength() != 0)) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isHostRoute()
  {
    return mIsHost;
  }
  
  public boolean isIPv4Default()
  {
    boolean bool;
    if ((isDefaultRoute()) && ((mDestination.getAddress() instanceof Inet4Address))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isIPv6Default()
  {
    boolean bool;
    if ((isDefaultRoute()) && ((mDestination.getAddress() instanceof Inet6Address))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean matches(InetAddress paramInetAddress)
  {
    return mDestination.contains(paramInetAddress);
  }
  
  public String toString()
  {
    Object localObject1 = "";
    if (mDestination != null) {
      localObject1 = mDestination.toString();
    }
    Object localObject2;
    if (mType == 7)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(" unreachable");
      localObject1 = ((StringBuilder)localObject2).toString();
    }
    else if (mType == 9)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(" throw");
      localObject1 = ((StringBuilder)localObject2).toString();
    }
    else
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(" ->");
      localObject2 = ((StringBuilder)localObject2).toString();
      localObject1 = localObject2;
      if (mGateway != null)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append((String)localObject2);
        ((StringBuilder)localObject1).append(" ");
        ((StringBuilder)localObject1).append(mGateway.getHostAddress());
        localObject1 = ((StringBuilder)localObject1).toString();
      }
      localObject2 = localObject1;
      if (mInterface != null)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" ");
        ((StringBuilder)localObject2).append(mInterface);
        localObject2 = ((StringBuilder)localObject2).toString();
      }
      localObject1 = localObject2;
      if (mType != 1)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append((String)localObject2);
        ((StringBuilder)localObject1).append(" unknown type ");
        ((StringBuilder)localObject1).append(mType);
        localObject1 = ((StringBuilder)localObject1).toString();
      }
    }
    return localObject1;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mDestination, paramInt);
    byte[] arrayOfByte;
    if (mGateway == null) {
      arrayOfByte = null;
    } else {
      arrayOfByte = mGateway.getAddress();
    }
    paramParcel.writeByteArray(arrayOfByte);
    paramParcel.writeString(mInterface);
    paramParcel.writeInt(mType);
  }
}
