package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Pair;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Comparator;

public final class IpPrefix
  implements Parcelable
{
  public static final Parcelable.Creator<IpPrefix> CREATOR = new Parcelable.Creator()
  {
    public IpPrefix createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpPrefix(paramAnonymousParcel.createByteArray(), paramAnonymousParcel.readInt());
    }
    
    public IpPrefix[] newArray(int paramAnonymousInt)
    {
      return new IpPrefix[paramAnonymousInt];
    }
  };
  private final byte[] address;
  private final int prefixLength;
  
  public IpPrefix(String paramString)
  {
    paramString = NetworkUtils.parseIpAndMask(paramString);
    address = ((InetAddress)first).getAddress();
    prefixLength = ((Integer)second).intValue();
    checkAndMaskAddressAndPrefixLength();
  }
  
  public IpPrefix(InetAddress paramInetAddress, int paramInt)
  {
    address = paramInetAddress.getAddress();
    prefixLength = paramInt;
    checkAndMaskAddressAndPrefixLength();
  }
  
  public IpPrefix(byte[] paramArrayOfByte, int paramInt)
  {
    address = ((byte[])paramArrayOfByte.clone());
    prefixLength = paramInt;
    checkAndMaskAddressAndPrefixLength();
  }
  
  private void checkAndMaskAddressAndPrefixLength()
  {
    if ((address.length != 4) && (address.length != 16))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("IpPrefix has ");
      localStringBuilder.append(address.length);
      localStringBuilder.append(" bytes which is neither 4 nor 16");
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    NetworkUtils.maskRawAddress(address, prefixLength);
  }
  
  public static Comparator<IpPrefix> lengthComparator()
  {
    new Comparator()
    {
      public int compare(IpPrefix paramAnonymousIpPrefix1, IpPrefix paramAnonymousIpPrefix2)
      {
        if (paramAnonymousIpPrefix1.isIPv4())
        {
          if (paramAnonymousIpPrefix2.isIPv6()) {
            return -1;
          }
        }
        else if (paramAnonymousIpPrefix2.isIPv4()) {
          return 1;
        }
        int i = paramAnonymousIpPrefix1.getPrefixLength();
        int j = paramAnonymousIpPrefix2.getPrefixLength();
        if (i < j) {
          return -1;
        }
        if (j < i) {
          return 1;
        }
        paramAnonymousIpPrefix1 = address;
        paramAnonymousIpPrefix2 = address;
        if (paramAnonymousIpPrefix1.length < paramAnonymousIpPrefix2.length) {
          i = paramAnonymousIpPrefix1.length;
        } else {
          i = paramAnonymousIpPrefix2.length;
        }
        for (j = 0; j < i; j++)
        {
          if (paramAnonymousIpPrefix1[j] < paramAnonymousIpPrefix2[j]) {
            return -1;
          }
          if (paramAnonymousIpPrefix1[j] > paramAnonymousIpPrefix2[j]) {
            return 1;
          }
        }
        if (paramAnonymousIpPrefix2.length < i) {
          return 1;
        }
        if (paramAnonymousIpPrefix1.length < i) {
          return -1;
        }
        return 0;
      }
    };
  }
  
  public boolean contains(InetAddress paramInetAddress)
  {
    if (paramInetAddress == null) {
      paramInetAddress = null;
    } else {
      paramInetAddress = paramInetAddress.getAddress();
    }
    if ((paramInetAddress != null) && (paramInetAddress.length == address.length))
    {
      NetworkUtils.maskRawAddress(paramInetAddress, prefixLength);
      return Arrays.equals(address, paramInetAddress);
    }
    return false;
  }
  
  public boolean containsPrefix(IpPrefix paramIpPrefix)
  {
    if (paramIpPrefix.getPrefixLength() < prefixLength) {
      return false;
    }
    paramIpPrefix = paramIpPrefix.getRawAddress();
    NetworkUtils.maskRawAddress(paramIpPrefix, prefixLength);
    return Arrays.equals(paramIpPrefix, address);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof IpPrefix;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (IpPrefix)paramObject;
    bool1 = bool2;
    if (Arrays.equals(address, address))
    {
      bool1 = bool2;
      if (prefixLength == prefixLength) {
        bool1 = true;
      }
    }
    return bool1;
  }
  
  public InetAddress getAddress()
  {
    try
    {
      InetAddress localInetAddress = InetAddress.getByAddress(address);
      return localInetAddress;
    }
    catch (UnknownHostException localUnknownHostException) {}
    return null;
  }
  
  public int getPrefixLength()
  {
    return prefixLength;
  }
  
  public byte[] getRawAddress()
  {
    return (byte[])address.clone();
  }
  
  public int hashCode()
  {
    return Arrays.hashCode(address) + 11 * prefixLength;
  }
  
  public boolean isIPv4()
  {
    return getAddress() instanceof Inet4Address;
  }
  
  public boolean isIPv6()
  {
    return getAddress() instanceof Inet6Address;
  }
  
  public String toString()
  {
    try
    {
      Object localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append(InetAddress.getByAddress(address).getHostAddress());
      ((StringBuilder)localObject).append("/");
      ((StringBuilder)localObject).append(prefixLength);
      localObject = ((StringBuilder)localObject).toString();
      return localObject;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw new IllegalStateException("IpPrefix with invalid address! Shouldn't happen.", localUnknownHostException);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeByteArray(address);
    paramParcel.writeInt(prefixLength);
  }
}
