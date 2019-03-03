package android.app.admin;

import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public abstract class NetworkEvent
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkEvent> CREATOR = new Parcelable.Creator()
  {
    public NetworkEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.dataPosition();
      int j = paramAnonymousParcel.readInt();
      paramAnonymousParcel.setDataPosition(i);
      switch (j)
      {
      default: 
        paramAnonymousParcel = new StringBuilder();
        paramAnonymousParcel.append("Unexpected NetworkEvent token in parcel: ");
        paramAnonymousParcel.append(j);
        throw new ParcelFormatException(paramAnonymousParcel.toString());
      case 2: 
        return (NetworkEvent)ConnectEvent.CREATOR.createFromParcel(paramAnonymousParcel);
      }
      return (NetworkEvent)DnsEvent.CREATOR.createFromParcel(paramAnonymousParcel);
    }
    
    public NetworkEvent[] newArray(int paramAnonymousInt)
    {
      return new NetworkEvent[paramAnonymousInt];
    }
  };
  static final int PARCEL_TOKEN_CONNECT_EVENT = 2;
  static final int PARCEL_TOKEN_DNS_EVENT = 1;
  long mId;
  String mPackageName;
  long mTimestamp;
  
  NetworkEvent() {}
  
  NetworkEvent(String paramString, long paramLong)
  {
    mPackageName = paramString;
    mTimestamp = paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getId()
  {
    return mId;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public long getTimestamp()
  {
    return mTimestamp;
  }
  
  public void setId(long paramLong)
  {
    mId = paramLong;
  }
  
  public abstract void writeToParcel(Parcel paramParcel, int paramInt);
}
