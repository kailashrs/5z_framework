package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class DhcpClientEvent
  implements Parcelable
{
  public static final Parcelable.Creator<DhcpClientEvent> CREATOR = new Parcelable.Creator()
  {
    public DhcpClientEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DhcpClientEvent(paramAnonymousParcel, null);
    }
    
    public DhcpClientEvent[] newArray(int paramAnonymousInt)
    {
      return new DhcpClientEvent[paramAnonymousInt];
    }
  };
  public static final String INITIAL_BOUND = "InitialBoundState";
  public static final String RENEWING_BOUND = "RenewingBoundState";
  public final int durationMs;
  public final String msg;
  
  private DhcpClientEvent(Parcel paramParcel)
  {
    msg = paramParcel.readString();
    durationMs = paramParcel.readInt();
  }
  
  public DhcpClientEvent(String paramString, int paramInt)
  {
    msg = paramString;
    durationMs = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    return String.format("DhcpClientEvent(%s, %dms)", new Object[] { msg, Integer.valueOf(durationMs) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(msg);
    paramParcel.writeInt(durationMs);
  }
}
