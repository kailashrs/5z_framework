package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.android.internal.util.MessageUtils;

public final class IpReachabilityEvent
  implements Parcelable
{
  public static final Parcelable.Creator<IpReachabilityEvent> CREATOR = new Parcelable.Creator()
  {
    public IpReachabilityEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpReachabilityEvent(paramAnonymousParcel, null);
    }
    
    public IpReachabilityEvent[] newArray(int paramAnonymousInt)
    {
      return new IpReachabilityEvent[paramAnonymousInt];
    }
  };
  public static final int NUD_FAILED = 512;
  public static final int NUD_FAILED_ORGANIC = 1024;
  public static final int PROBE = 256;
  public static final int PROVISIONING_LOST = 768;
  public static final int PROVISIONING_LOST_ORGANIC = 1280;
  public final int eventType;
  
  public IpReachabilityEvent(int paramInt)
  {
    eventType = paramInt;
  }
  
  private IpReachabilityEvent(Parcel paramParcel)
  {
    eventType = paramParcel.readInt();
  }
  
  public static int nudFailureEventType(boolean paramBoolean1, boolean paramBoolean2)
  {
    int i;
    if (paramBoolean1)
    {
      if (paramBoolean2) {
        i = 768;
      } else {
        i = 512;
      }
      return i;
    }
    if (paramBoolean2) {
      i = 1280;
    } else {
      i = 1024;
    }
    return i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    int i = eventType;
    int j = eventType;
    return String.format("IpReachabilityEvent(%s:%02x)", new Object[] { (String)Decoder.constants.get(i & 0xFF00), Integer.valueOf(j & 0xFF) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(eventType);
  }
  
  static final class Decoder
  {
    static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[] { IpReachabilityEvent.class }, new String[] { "PROBE", "PROVISIONING_", "NUD_" });
    
    Decoder() {}
  }
}
