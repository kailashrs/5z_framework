package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.BitUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

public final class MacAddress
  implements Parcelable
{
  public static final MacAddress ALL_ZEROS_ADDRESS;
  private static final MacAddress BASE_GOOGLE_MAC = fromString("da:a1:19:0:0:0");
  public static final MacAddress BROADCAST_ADDRESS;
  public static final Parcelable.Creator<MacAddress> CREATOR = new Parcelable.Creator()
  {
    public MacAddress createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MacAddress(paramAnonymousParcel.readLong(), null);
    }
    
    public MacAddress[] newArray(int paramAnonymousInt)
    {
      return new MacAddress[paramAnonymousInt];
    }
  };
  private static final byte[] ETHER_ADDR_BROADCAST = addr(new int[] { 255, 255, 255, 255, 255, 255 });
  private static final int ETHER_ADDR_LEN = 6;
  private static final long LOCALLY_ASSIGNED_MASK;
  private static final long MULTICAST_MASK;
  private static final long NIC_MASK;
  private static final long OUI_MASK;
  public static final int TYPE_BROADCAST = 3;
  public static final int TYPE_MULTICAST = 2;
  public static final int TYPE_UNICAST = 1;
  public static final int TYPE_UNKNOWN = 0;
  private static final long VALID_LONG_MASK = 281474976710655L;
  private final long mAddr;
  
  static
  {
    BROADCAST_ADDRESS = fromBytes(ETHER_ADDR_BROADCAST);
    ALL_ZEROS_ADDRESS = new MacAddress(0L);
    LOCALLY_ASSIGNED_MASK = fromString"2:0:0:0:0:0"mAddr;
    MULTICAST_MASK = fromString"1:0:0:0:0:0"mAddr;
    OUI_MASK = fromString"ff:ff:ff:0:0:0"mAddr;
    NIC_MASK = fromString"0:0:0:ff:ff:ff"mAddr;
  }
  
  private MacAddress(long paramLong)
  {
    mAddr = (0xFFFFFFFFFFFF & paramLong);
  }
  
  private static byte[] addr(int... paramVarArgs)
  {
    if (paramVarArgs.length == 6)
    {
      localObject = new byte[6];
      for (int i = 0; i < 6; i++) {
        localObject[i] = ((byte)(byte)paramVarArgs[i]);
      }
      return localObject;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(Arrays.toString(paramVarArgs));
    ((StringBuilder)localObject).append(" was not an array with length equal to ");
    ((StringBuilder)localObject).append(6);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  private static byte[] byteAddrFromLongAddr(long paramLong)
  {
    int i = 6;
    byte[] arrayOfByte = new byte[6];
    for (;;)
    {
      int j = i - 1;
      if (i <= 0) {
        break;
      }
      arrayOfByte[j] = ((byte)(byte)(int)paramLong);
      paramLong >>= 8;
      i = j;
    }
    return arrayOfByte;
  }
  
  public static byte[] byteAddrFromStringAddr(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    Object localObject = paramString.split(":");
    if (localObject.length == 6)
    {
      byte[] arrayOfByte = new byte[6];
      int i = 0;
      while (i < 6)
      {
        int j = Integer.valueOf(localObject[i], 16).intValue();
        if ((j >= 0) && (255 >= j))
        {
          arrayOfByte[i] = ((byte)(byte)j);
          i++;
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(paramString);
          ((StringBuilder)localObject).append("was not a valid MAC address");
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        }
      }
      return arrayOfByte;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(" was not a valid MAC address");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public static MacAddress createRandomUnicastAddress()
  {
    return new MacAddress((new SecureRandom().nextLong() & 0xFFFFFFFFFFFF | LOCALLY_ASSIGNED_MASK) & MULTICAST_MASK);
  }
  
  public static MacAddress createRandomUnicastAddress(MacAddress paramMacAddress, Random paramRandom)
  {
    return new MacAddress((mAddr & OUI_MASK | NIC_MASK & paramRandom.nextLong() | LOCALLY_ASSIGNED_MASK) & MULTICAST_MASK);
  }
  
  public static MacAddress createRandomUnicastAddressWithGoogleBase()
  {
    return createRandomUnicastAddress(BASE_GOOGLE_MAC, new SecureRandom());
  }
  
  public static MacAddress fromBytes(byte[] paramArrayOfByte)
  {
    return new MacAddress(longAddrFromByteAddr(paramArrayOfByte));
  }
  
  public static MacAddress fromString(String paramString)
  {
    return new MacAddress(longAddrFromStringAddr(paramString));
  }
  
  public static boolean isMacAddress(byte[] paramArrayOfByte)
  {
    boolean bool;
    if ((paramArrayOfByte != null) && (paramArrayOfByte.length == 6)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static long longAddrFromByteAddr(byte[] paramArrayOfByte)
  {
    Preconditions.checkNotNull(paramArrayOfByte);
    if (isMacAddress(paramArrayOfByte))
    {
      long l = 0L;
      int i = paramArrayOfByte.length;
      for (int j = 0; j < i; j++) {
        l = (l << 8) + BitUtils.uint8(paramArrayOfByte[j]);
      }
      return l;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(Arrays.toString(paramArrayOfByte));
    localStringBuilder.append(" was not a valid MAC address");
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private static long longAddrFromStringAddr(String paramString)
  {
    Preconditions.checkNotNull(paramString);
    Object localObject = paramString.split(":");
    if (localObject.length == 6)
    {
      long l = 0L;
      int i = 0;
      while (i < localObject.length)
      {
        int j = Integer.valueOf(localObject[i], 16).intValue();
        if ((j >= 0) && (255 >= j))
        {
          l = j + (l << 8);
          i++;
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(paramString);
          ((StringBuilder)localObject).append("was not a valid MAC address");
          throw new IllegalArgumentException(((StringBuilder)localObject).toString());
        }
      }
      return l;
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(" was not a valid MAC address");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public static int macAddressType(byte[] paramArrayOfByte)
  {
    if (!isMacAddress(paramArrayOfByte)) {
      return 0;
    }
    return fromBytes(paramArrayOfByte).getAddressType();
  }
  
  public static String stringAddrFromByteAddr(byte[] paramArrayOfByte)
  {
    if (!isMacAddress(paramArrayOfByte)) {
      return null;
    }
    return String.format("%02x:%02x:%02x:%02x:%02x:%02x", new Object[] { Byte.valueOf(paramArrayOfByte[0]), Byte.valueOf(paramArrayOfByte[1]), Byte.valueOf(paramArrayOfByte[2]), Byte.valueOf(paramArrayOfByte[3]), Byte.valueOf(paramArrayOfByte[4]), Byte.valueOf(paramArrayOfByte[5]) });
  }
  
  private static String stringAddrFromLongAddr(long paramLong)
  {
    return String.format("%02x:%02x:%02x:%02x:%02x:%02x", new Object[] { Long.valueOf(paramLong >> 40 & 0xFF), Long.valueOf(paramLong >> 32 & 0xFF), Long.valueOf(paramLong >> 24 & 0xFF), Long.valueOf(paramLong >> 16 & 0xFF), Long.valueOf(paramLong >> 8 & 0xFF), Long.valueOf(paramLong & 0xFF) });
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof MacAddress)) && (mAddr == mAddr)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public int getAddressType()
  {
    if (equals(BROADCAST_ADDRESS)) {
      return 3;
    }
    if (isMulticastAddress()) {
      return 2;
    }
    return 1;
  }
  
  public int hashCode()
  {
    return (int)(mAddr >> 32 ^ mAddr);
  }
  
  public boolean isLocallyAssigned()
  {
    boolean bool;
    if ((mAddr & LOCALLY_ASSIGNED_MASK) != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isMulticastAddress()
  {
    boolean bool;
    if ((mAddr & MULTICAST_MASK) != 0L) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public byte[] toByteArray()
  {
    return byteAddrFromLongAddr(mAddr);
  }
  
  public String toOuiString()
  {
    return String.format("%02x:%02x:%02x", new Object[] { Long.valueOf(mAddr >> 40 & 0xFF), Long.valueOf(mAddr >> 32 & 0xFF), Long.valueOf(mAddr >> 24 & 0xFF) });
  }
  
  public String toString()
  {
    return stringAddrFromLongAddr(mAddr);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mAddr);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MacAddressType {}
}
