package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.android.internal.util.MessageUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class IpManagerEvent
  implements Parcelable
{
  public static final int COMPLETE_LIFECYCLE = 3;
  public static final Parcelable.Creator<IpManagerEvent> CREATOR = new Parcelable.Creator()
  {
    public IpManagerEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new IpManagerEvent(paramAnonymousParcel, null);
    }
    
    public IpManagerEvent[] newArray(int paramAnonymousInt)
    {
      return new IpManagerEvent[paramAnonymousInt];
    }
  };
  public static final int ERROR_INTERFACE_NOT_FOUND = 8;
  public static final int ERROR_INVALID_PROVISIONING = 7;
  public static final int ERROR_STARTING_IPREACHABILITYMONITOR = 6;
  public static final int ERROR_STARTING_IPV4 = 4;
  public static final int ERROR_STARTING_IPV6 = 5;
  public static final int PROVISIONING_FAIL = 2;
  public static final int PROVISIONING_OK = 1;
  public final long durationMs;
  public final int eventType;
  
  public IpManagerEvent(int paramInt, long paramLong)
  {
    eventType = paramInt;
    durationMs = paramLong;
  }
  
  private IpManagerEvent(Parcel paramParcel)
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
    return String.format("IpManagerEvent(%s, %dms)", new Object[] { Decoder.constants.get(eventType), Long.valueOf(durationMs) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(eventType);
    paramParcel.writeLong(durationMs);
  }
  
  static final class Decoder
  {
    static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[] { IpManagerEvent.class }, new String[] { "PROVISIONING_", "COMPLETE_", "ERROR_" });
    
    Decoder() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface EventType {}
}
