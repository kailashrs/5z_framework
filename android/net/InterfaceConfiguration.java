package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.collect.Sets;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Iterator;

public class InterfaceConfiguration
  implements Parcelable
{
  public static final Parcelable.Creator<InterfaceConfiguration> CREATOR = new Parcelable.Creator()
  {
    public InterfaceConfiguration createFromParcel(Parcel paramAnonymousParcel)
    {
      InterfaceConfiguration localInterfaceConfiguration = new InterfaceConfiguration();
      InterfaceConfiguration.access$002(localInterfaceConfiguration, paramAnonymousParcel.readString());
      if (paramAnonymousParcel.readByte() == 1) {
        InterfaceConfiguration.access$102(localInterfaceConfiguration, (LinkAddress)paramAnonymousParcel.readParcelable(null));
      }
      int i = paramAnonymousParcel.readInt();
      for (int j = 0; j < i; j++) {
        mFlags.add(paramAnonymousParcel.readString());
      }
      return localInterfaceConfiguration;
    }
    
    public InterfaceConfiguration[] newArray(int paramAnonymousInt)
    {
      return new InterfaceConfiguration[paramAnonymousInt];
    }
  };
  private static final String FLAG_DOWN = "down";
  private static final String FLAG_UP = "up";
  private LinkAddress mAddr;
  private HashSet<String> mFlags = Sets.newHashSet();
  private String mHwAddr;
  
  public InterfaceConfiguration() {}
  
  private static void validateFlag(String paramString)
  {
    if (paramString.indexOf(' ') < 0) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("flag contains space: ");
    localStringBuilder.append(paramString);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public void clearFlag(String paramString)
  {
    validateFlag(paramString);
    mFlags.remove(paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Iterable<String> getFlags()
  {
    return mFlags;
  }
  
  public String getHardwareAddress()
  {
    return mHwAddr;
  }
  
  public LinkAddress getLinkAddress()
  {
    return mAddr;
  }
  
  public boolean hasFlag(String paramString)
  {
    validateFlag(paramString);
    return mFlags.contains(paramString);
  }
  
  public void ignoreInterfaceUpDownStatus()
  {
    mFlags.remove("up");
    mFlags.remove("down");
  }
  
  public boolean isActive()
  {
    try
    {
      if (isUp()) {
        for (int k : mAddr.getAddress().getAddress()) {
          if (k != 0) {
            return true;
          }
        }
      }
      return false;
    }
    catch (NullPointerException localNullPointerException) {}
    return false;
  }
  
  public boolean isUp()
  {
    return hasFlag("up");
  }
  
  public void setFlag(String paramString)
  {
    validateFlag(paramString);
    mFlags.add(paramString);
  }
  
  public void setHardwareAddress(String paramString)
  {
    mHwAddr = paramString;
  }
  
  public void setInterfaceDown()
  {
    mFlags.remove("up");
    mFlags.add("down");
  }
  
  public void setInterfaceUp()
  {
    mFlags.remove("down");
    mFlags.add("up");
  }
  
  public void setLinkAddress(LinkAddress paramLinkAddress)
  {
    mAddr = paramLinkAddress;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("mHwAddr=");
    localStringBuilder.append(mHwAddr);
    localStringBuilder.append(" mAddr=");
    localStringBuilder.append(String.valueOf(mAddr));
    localStringBuilder.append(" mFlags=");
    localStringBuilder.append(getFlags());
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mHwAddr);
    if (mAddr != null)
    {
      paramParcel.writeByte((byte)1);
      paramParcel.writeParcelable(mAddr, paramInt);
    }
    else
    {
      paramParcel.writeByte((byte)0);
    }
    paramParcel.writeInt(mFlags.size());
    Iterator localIterator = mFlags.iterator();
    while (localIterator.hasNext()) {
      paramParcel.writeString((String)localIterator.next());
    }
  }
}
