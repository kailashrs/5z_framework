package android.app.admin;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class ConnectEvent
  extends NetworkEvent
  implements Parcelable
{
  public static final Parcelable.Creator<ConnectEvent> CREATOR = new Parcelable.Creator()
  {
    public ConnectEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      if (paramAnonymousParcel.readInt() != 2) {
        return null;
      }
      return new ConnectEvent(paramAnonymousParcel, null);
    }
    
    public ConnectEvent[] newArray(int paramAnonymousInt)
    {
      return new ConnectEvent[paramAnonymousInt];
    }
  };
  private final String mIpAddress;
  private final int mPort;
  
  private ConnectEvent(Parcel paramParcel)
  {
    mIpAddress = paramParcel.readString();
    mPort = paramParcel.readInt();
    mPackageName = paramParcel.readString();
    mTimestamp = paramParcel.readLong();
    mId = paramParcel.readLong();
  }
  
  public ConnectEvent(String paramString1, int paramInt, String paramString2, long paramLong)
  {
    super(paramString2, paramLong);
    mIpAddress = paramString1;
    mPort = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public InetAddress getInetAddress()
  {
    try
    {
      InetAddress localInetAddress = InetAddress.getByName(mIpAddress);
      return localInetAddress;
    }
    catch (UnknownHostException localUnknownHostException) {}
    return InetAddress.getLoopbackAddress();
  }
  
  public int getPort()
  {
    return mPort;
  }
  
  public String toString()
  {
    return String.format("ConnectEvent(%d, %s, %d, %d, %s)", new Object[] { Long.valueOf(mId), mIpAddress, Integer.valueOf(mPort), Long.valueOf(mTimestamp), mPackageName });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(2);
    paramParcel.writeString(mIpAddress);
    paramParcel.writeInt(mPort);
    paramParcel.writeString(mPackageName);
    paramParcel.writeLong(mTimestamp);
    paramParcel.writeLong(mId);
  }
}
