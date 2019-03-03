package android.app.admin;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DnsEvent
  extends NetworkEvent
  implements Parcelable
{
  public static final Parcelable.Creator<DnsEvent> CREATOR = new Parcelable.Creator()
  {
    public DnsEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      if (paramAnonymousParcel.readInt() != 1) {
        return null;
      }
      return new DnsEvent(paramAnonymousParcel, null);
    }
    
    public DnsEvent[] newArray(int paramAnonymousInt)
    {
      return new DnsEvent[paramAnonymousInt];
    }
  };
  private final String mHostname;
  private final String[] mIpAddresses;
  private final int mIpAddressesCount;
  
  private DnsEvent(Parcel paramParcel)
  {
    mHostname = paramParcel.readString();
    mIpAddresses = paramParcel.createStringArray();
    mIpAddressesCount = paramParcel.readInt();
    mPackageName = paramParcel.readString();
    mTimestamp = paramParcel.readLong();
    mId = paramParcel.readLong();
  }
  
  public DnsEvent(String paramString1, String[] paramArrayOfString, int paramInt, String paramString2, long paramLong)
  {
    super(paramString2, paramLong);
    mHostname = paramString1;
    mIpAddresses = paramArrayOfString;
    mIpAddressesCount = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getHostname()
  {
    return mHostname;
  }
  
  public List<InetAddress> getInetAddresses()
  {
    if ((mIpAddresses != null) && (mIpAddresses.length != 0))
    {
      ArrayList localArrayList = new ArrayList(mIpAddresses.length);
      for (String str : mIpAddresses) {
        try
        {
          localArrayList.add(InetAddress.getByName(str));
        }
        catch (UnknownHostException localUnknownHostException) {}
      }
      return localArrayList;
    }
    return Collections.emptyList();
  }
  
  public int getTotalResolvedAddressCount()
  {
    return mIpAddressesCount;
  }
  
  public String toString()
  {
    long l = mId;
    String str1 = mHostname;
    String str2;
    if (mIpAddresses == null) {
      str2 = "NONE";
    } else {
      str2 = String.join(" ", mIpAddresses);
    }
    return String.format("DnsEvent(%d, %s, %s, %d, %d, %s)", new Object[] { Long.valueOf(l), str1, str2, Integer.valueOf(mIpAddressesCount), Long.valueOf(mTimestamp), mPackageName });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(1);
    paramParcel.writeString(mHostname);
    paramParcel.writeStringArray(mIpAddresses);
    paramParcel.writeInt(mIpAddressesCount);
    paramParcel.writeString(mPackageName);
    paramParcel.writeLong(mTimestamp);
    paramParcel.writeLong(mId);
  }
}
