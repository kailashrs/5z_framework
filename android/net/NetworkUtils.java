package android.net;

import android.os.Parcel;
import android.util.Log;
import android.util.Pair;
import java.io.FileDescriptor;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;

public class NetworkUtils
{
  private static final String TAG = "NetworkUtils";
  
  public NetworkUtils() {}
  
  public static boolean addressTypeMatches(InetAddress paramInetAddress1, InetAddress paramInetAddress2)
  {
    boolean bool;
    if ((((paramInetAddress1 instanceof Inet4Address)) && ((paramInetAddress2 instanceof Inet4Address))) || (((paramInetAddress1 instanceof Inet6Address)) && ((paramInetAddress2 instanceof Inet6Address)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static native void attachControlPacketFilter(FileDescriptor paramFileDescriptor, int paramInt)
    throws SocketException;
  
  public static native void attachDhcpFilter(FileDescriptor paramFileDescriptor)
    throws SocketException;
  
  public static native void attachRaFilter(FileDescriptor paramFileDescriptor, int paramInt)
    throws SocketException;
  
  public static native boolean bindProcessToNetwork(int paramInt);
  
  @Deprecated
  public static native boolean bindProcessToNetworkForHostResolution(int paramInt);
  
  public static native int bindSocketToNetwork(int paramInt1, int paramInt2);
  
  private static TreeSet<IpPrefix> deduplicatePrefixSet(TreeSet<IpPrefix> paramTreeSet)
  {
    TreeSet localTreeSet = new TreeSet(paramTreeSet.comparator());
    paramTreeSet = paramTreeSet.iterator();
    while (paramTreeSet.hasNext())
    {
      IpPrefix localIpPrefix = (IpPrefix)paramTreeSet.next();
      Iterator localIterator = localTreeSet.iterator();
      for (;;)
      {
        if (!localIterator.hasNext()) {
          break label72;
        }
        if (((IpPrefix)localIterator.next()).containsPrefix(localIpPrefix)) {
          break;
        }
      }
      label72:
      localTreeSet.add(localIpPrefix);
    }
    return localTreeSet;
  }
  
  public static native int getBoundNetworkForProcess();
  
  public static int getImplicitNetmask(Inet4Address paramInet4Address)
  {
    int i = paramInet4Address.getAddress()[0] & 0xFF;
    if (i < 128) {
      return 8;
    }
    if (i < 192) {
      return 16;
    }
    if (i < 224) {
      return 24;
    }
    return 32;
  }
  
  public static InetAddress getNetworkPart(InetAddress paramInetAddress, int paramInt)
  {
    paramInetAddress = paramInetAddress.getAddress();
    maskRawAddress(paramInetAddress, paramInt);
    try
    {
      paramInetAddress = InetAddress.getByAddress(paramInetAddress);
      return paramInetAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      paramInetAddress = new StringBuilder();
      paramInetAddress.append("getNetworkPart error - ");
      paramInetAddress.append(localUnknownHostException.toString());
      throw new RuntimeException(paramInetAddress.toString());
    }
  }
  
  public static InetAddress hexToInet6Address(String paramString)
    throws IllegalArgumentException
  {
    try
    {
      localObject = numericToInetAddress(String.format(Locale.US, "%s:%s:%s:%s:%s:%s:%s:%s", new Object[] { paramString.substring(0, 4), paramString.substring(4, 8), paramString.substring(8, 12), paramString.substring(12, 16), paramString.substring(16, 20), paramString.substring(20, 24), paramString.substring(24, 28), paramString.substring(28, 32) }));
      return localObject;
    }
    catch (Exception localException)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("error in hexToInet6Address(");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append("): ");
      ((StringBuilder)localObject).append(localException);
      Log.e("NetworkUtils", ((StringBuilder)localObject).toString());
      throw new IllegalArgumentException(localException);
    }
  }
  
  public static int inetAddressToInt(Inet4Address paramInet4Address)
    throws IllegalArgumentException
  {
    paramInet4Address = paramInet4Address.getAddress();
    return (paramInet4Address[3] & 0xFF) << 24 | (paramInet4Address[2] & 0xFF) << 16 | (paramInet4Address[1] & 0xFF) << 8 | paramInet4Address[0] & 0xFF;
  }
  
  public static InetAddress intToInetAddress(int paramInt)
  {
    int i = (byte)(0xFF & paramInt);
    int j = (byte)(paramInt >> 8 & 0xFF);
    int k = (byte)(paramInt >> 16 & 0xFF);
    int m = (byte)(0xFF & paramInt >> 24);
    try
    {
      InetAddress localInetAddress = InetAddress.getByAddress(new byte[] { i, j, k, m });
      return localInetAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw new AssertionError();
    }
  }
  
  public static String[] makeStrings(Collection<InetAddress> paramCollection)
  {
    String[] arrayOfString = new String[paramCollection.size()];
    int i = 0;
    paramCollection = paramCollection.iterator();
    while (paramCollection.hasNext())
    {
      arrayOfString[i] = ((InetAddress)paramCollection.next()).getHostAddress();
      i++;
    }
    return arrayOfString;
  }
  
  public static void maskRawAddress(byte[] paramArrayOfByte, int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= paramArrayOfByte.length * 8))
    {
      int i = paramInt / 8;
      paramInt = (byte)(255 << 8 - paramInt % 8);
      if (i < paramArrayOfByte.length) {
        paramArrayOfByte[i] = ((byte)(byte)(paramArrayOfByte[i] & paramInt));
      }
      for (paramInt = i + 1; paramInt < paramArrayOfByte.length; paramInt++) {
        paramArrayOfByte[paramInt] = ((byte)0);
      }
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("IP address with ");
    localStringBuilder.append(paramArrayOfByte.length);
    localStringBuilder.append(" bytes has invalid prefix length ");
    localStringBuilder.append(paramInt);
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public static int netmaskIntToPrefixLength(int paramInt)
  {
    return Integer.bitCount(paramInt);
  }
  
  public static int netmaskToPrefixLength(Inet4Address paramInet4Address)
  {
    int i = Integer.reverseBytes(inetAddressToInt(paramInet4Address));
    int j = Integer.bitCount(i);
    if (Integer.numberOfTrailingZeros(i) == 32 - j) {
      return j;
    }
    paramInet4Address = new StringBuilder();
    paramInet4Address.append("Non-contiguous netmask: ");
    paramInet4Address.append(Integer.toHexString(i));
    throw new IllegalArgumentException(paramInet4Address.toString());
  }
  
  public static InetAddress numericToInetAddress(String paramString)
    throws IllegalArgumentException
  {
    return InetAddress.parseNumericAddress(paramString);
  }
  
  protected static void parcelInetAddress(Parcel paramParcel, InetAddress paramInetAddress, int paramInt)
  {
    if (paramInetAddress != null) {
      paramInetAddress = paramInetAddress.getAddress();
    } else {
      paramInetAddress = null;
    }
    paramParcel.writeByteArray(paramInetAddress);
  }
  
  public static Pair<InetAddress, Integer> parseIpAndMask(String paramString)
  {
    Object localObject1 = null;
    Object localObject2 = null;
    int i = -1;
    int j = i;
    int k = i;
    int m = i;
    int n = i;
    try
    {
      Object localObject4 = paramString.split("/", 2);
      j = i;
      k = i;
      m = i;
      n = i;
      i = Integer.parseInt(localObject4[1]);
      j = i;
      k = i;
      m = i;
      n = i;
      localObject4 = InetAddress.parseNumericAddress(localObject4[0]);
      localObject2 = localObject4;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localObject3 = localObject1;
      i = j;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      i = k;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
      i = m;
    }
    catch (NullPointerException localNullPointerException)
    {
      i = n;
    }
    if ((localObject3 != null) && (i != -1)) {
      return new Pair(localObject3, Integer.valueOf(i));
    }
    Object localObject3 = new StringBuilder();
    ((StringBuilder)localObject3).append("Invalid IP address and mask ");
    ((StringBuilder)localObject3).append(paramString);
    throw new IllegalArgumentException(((StringBuilder)localObject3).toString());
  }
  
  public static int prefixLengthToNetmaskInt(int paramInt)
    throws IllegalArgumentException
  {
    if ((paramInt >= 0) && (paramInt <= 32)) {
      return Integer.reverseBytes(-1 << 32 - paramInt);
    }
    throw new IllegalArgumentException("Invalid prefix length (0 <= prefix <= 32)");
  }
  
  public static native boolean protectFromVpn(int paramInt);
  
  public static boolean protectFromVpn(FileDescriptor paramFileDescriptor)
  {
    return protectFromVpn(paramFileDescriptor.getInt$());
  }
  
  public static native boolean queryUserAccess(int paramInt1, int paramInt2);
  
  public static long routedIPv4AddressCount(TreeSet<IpPrefix> paramTreeSet)
  {
    long l = 0L;
    Iterator localIterator = deduplicatePrefixSet(paramTreeSet).iterator();
    while (localIterator.hasNext())
    {
      paramTreeSet = (IpPrefix)localIterator.next();
      if (!paramTreeSet.isIPv4()) {
        Log.wtf("NetworkUtils", "Non-IPv4 prefix in routedIPv4AddressCount");
      }
      l += (1L << 32 - paramTreeSet.getPrefixLength());
    }
    return l;
  }
  
  public static BigInteger routedIPv6AddressCount(TreeSet<IpPrefix> paramTreeSet)
  {
    Object localObject = BigInteger.ZERO;
    Iterator localIterator = deduplicatePrefixSet(paramTreeSet).iterator();
    int i;
    for (paramTreeSet = (TreeSet<IpPrefix>)localObject; localIterator.hasNext(); paramTreeSet = paramTreeSet.add(BigInteger.ONE.shiftLeft(128 - i)))
    {
      localObject = (IpPrefix)localIterator.next();
      if (!((IpPrefix)localObject).isIPv6()) {
        Log.wtf("NetworkUtils", "Non-IPv6 prefix in routedIPv6AddressCount");
      }
      i = ((IpPrefix)localObject).getPrefixLength();
    }
    return paramTreeSet;
  }
  
  public static native void setupRaSocket(FileDescriptor paramFileDescriptor, int paramInt)
    throws SocketException;
  
  public static String trimV4AddrZeros(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    String[] arrayOfString = paramString.split("\\.");
    if (arrayOfString.length != 4) {
      return paramString;
    }
    StringBuilder localStringBuilder = new StringBuilder(16);
    int i = 0;
    while (i < 4) {
      try
      {
        if (arrayOfString[i].length() > 3) {
          return paramString;
        }
        localStringBuilder.append(Integer.parseInt(arrayOfString[i]));
        if (i < 3) {
          localStringBuilder.append('.');
        }
        i++;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return paramString;
      }
    }
    return localStringBuilder.toString();
  }
  
  protected static InetAddress unparcelInetAddress(Parcel paramParcel)
  {
    paramParcel = paramParcel.createByteArray();
    if (paramParcel == null) {
      return null;
    }
    try
    {
      paramParcel = InetAddress.getByAddress(paramParcel);
      return paramParcel;
    }
    catch (UnknownHostException paramParcel) {}
    return null;
  }
}
