package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.android.internal.util.MessageUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class NetworkEvent
  implements Parcelable
{
  public static final Parcelable.Creator<NetworkEvent> CREATOR = new Parcelable.Creator()
  {
    public NetworkEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new NetworkEvent(paramAnonymousParcel, null);
    }
    
    public NetworkEvent[] newArray(int paramAnonymousInt)
    {
      return new NetworkEvent[paramAnonymousInt];
    }
  };
  public static final int NETWORK_CAPTIVE_PORTAL_FOUND = 4;
  public static final int NETWORK_CONNECTED = 1;
  public static final int NETWORK_DISCONNECTED = 7;
  public static final int NETWORK_FIRST_VALIDATION_PORTAL_FOUND = 10;
  public static final int NETWORK_FIRST_VALIDATION_SUCCESS = 8;
  public static final int NETWORK_LINGER = 5;
  public static final int NETWORK_REVALIDATION_PORTAL_FOUND = 11;
  public static final int NETWORK_REVALIDATION_SUCCESS = 9;
  public static final int NETWORK_UNLINGER = 6;
  public static final int NETWORK_VALIDATED = 2;
  public static final int NETWORK_VALIDATION_FAILED = 3;
  public final long durationMs;
  public final int eventType;
  
  public NetworkEvent(int paramInt)
  {
    this(paramInt, 0L);
  }
  
  public NetworkEvent(int paramInt, long paramLong)
  {
    eventType = paramInt;
    durationMs = paramLong;
  }
  
  private NetworkEvent(Parcel paramParcel)
  {
    eventType = paramParcel.readInt();
    durationMs = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    return String.format("NetworkEvent(%s, %dms)", new Object[] { Decoder.constants.get(eventType), Long.valueOf(durationMs) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(eventType);
    paramParcel.writeLong(durationMs);
  }
  
  static final class Decoder
  {
    static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[] { NetworkEvent.class }, new String[] { "NETWORK_" });
    
    Decoder() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EventType {}
}
