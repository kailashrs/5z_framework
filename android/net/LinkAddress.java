package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.system.OsConstants;
import android.util.Pair;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.UnknownHostException;

public class LinkAddress
  implements Parcelable
{
  public static final Parcelable.Creator<LinkAddress> CREATOR = new Parcelable.Creator()
  {
    public LinkAddress createFromParcel(Parcel paramAnonymousParcel)
    {
      Object localObject = null;
      try
      {
        InetAddress localInetAddress = InetAddress.getByAddress(paramAnonymousParcel.createByteArray());
        localObject = localInetAddress;
      }
      catch (UnknownHostException localUnknownHostException) {}
      return new LinkAddress(localObject, paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt());
    }
    
    public LinkAddress[] newArray(int paramAnonymousInt)
    {
      return new LinkAddress[paramAnonymousInt];
    }
  };
  private InetAddress address;
  private int flags;
  private int prefixLength;
  private int scope;
  
  public LinkAddress(String paramString)
  {
    this(paramString, 0, 0);
    scope = scopeForUnicastAddress(address);
  }
  
  public LinkAddress(String paramString, int paramInt1, int paramInt2)
  {
    paramString = NetworkUtils.parseIpAndMask(paramString);
    init((InetAddress)first, ((Integer)second).intValue(), paramInt1, paramInt2);
  }
  
  public LinkAddress(InetAddress paramInetAddress, int paramInt)
  {
    this(paramInetAddress, paramInt, 0, 0);
    scope = scopeForUnicastAddress(paramInetAddress);
  }
  
  public LinkAddress(InetAddress paramInetAddress, int paramInt1, int paramInt2, int paramInt3)
  {
    init(paramInetAddress, paramInt1, paramInt2, paramInt3);
  }
  
  public LinkAddress(InterfaceAddress paramInterfaceAddress)
  {
    this(paramInterfaceAddress.getAddress(), paramInterfaceAddress.getNetworkPrefixLength());
  }
  
  private void init(InetAddress paramInetAddress, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInetAddress != null) && (!paramInetAddress.isMulticastAddress()) && (paramInt1 >= 0) && ((!(paramInetAddress instanceof Inet4Address)) || (paramInt1 <= 32)) && (paramInt1 <= 128))
    {
      address = paramInetAddress;
      prefixLength = paramInt1;
      flags = paramInt2;
      scope = paramInt3;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Bad LinkAddress params ");
    localStringBuilder.append(paramInetAddress);
    localStringBuilder.append("/");
    localStringBuilder.append(paramInt1);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private boolean isIPv6ULA()
  {
    boolean bool1 = isIPv6();
    boolean bool2 = false;
    if (bool1)
    {
      if ((address.getAddress()[0] & 0xFFFFFFFE) == -4) {
        bool2 = true;
      }
      return bool2;
    }
    return false;
  }
  
  private static int scopeForUnicastAddress(InetAddress paramInetAddress)
  {
    if (paramInetAddress.isAnyLocalAddress()) {
      return OsConstants.RT_SCOPE_HOST;
    }
    if ((!paramInetAddress.isLoopbackAddress()) && (!paramInetAddress.isLinkLocalAddress()))
    {
      if ((!(paramInetAddress instanceof Inet4Address)) && (paramInetAddress.isSiteLocalAddress())) {
        return OsConstants.RT_SCOPE_SITE;
      }
      return OsConstants.RT_SCOPE_UNIVERSE;
    }
    return OsConstants.RT_SCOPE_LINK;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof LinkAddress;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (LinkAddress)paramObject;
    bool1 = bool2;
    if (address.equals(address))
    {
      bool1 = bool2;
      if (prefixLength == prefixLength)
      {
        bool1 = bool2;
        if (flags == flags)
        {
          bool1 = bool2;
          if (scope == scope) {
            bool1 = true;
          }
        }
      }
    }
    return bool1;
  }
  
  public InetAddress getAddress()
  {
    return address;
  }
  
  public int getFlags()
  {
    return flags;
  }
  
  public int getNetworkPrefixLength()
  {
    return getPrefixLength();
  }
  
  public int getPrefixLength()
  {
    return prefixLength;
  }
  
  public int getScope()
  {
    return scope;
  }
  
  public int hashCode()
  {
    return address.hashCode() + 11 * prefixLength + 19 * flags + 43 * scope;
  }
  
  public boolean isGlobalPreferred()
  {
    boolean bool;
    if ((scope == OsConstants.RT_SCOPE_UNIVERSE) && (!isIPv6ULA()) && ((flags & (OsConstants.IFA_F_DADFAILED | OsConstants.IFA_F_DEPRECATED)) == 0L) && (((flags & OsConstants.IFA_F_TENTATIVE) == 0L) || ((flags & OsConstants.IFA_F_OPTIMISTIC) != 0L))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isIPv4()
  {
    return address instanceof Inet4Address;
  }
  
  public boolean isIPv6()
  {
    return address instanceof Inet6Address;
  }
  
  public boolean isSameAddressAs(LinkAddress paramLinkAddress)
  {
    boolean bool;
    if ((address.equals(address)) && (prefixLength == prefixLength)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(address.getHostAddress());
    localStringBuilder.append("/");
    localStringBuilder.append(prefixLength);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(address.getAddress());
    paramParcel.writeInt(prefixLength);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(scope);
  }
}
