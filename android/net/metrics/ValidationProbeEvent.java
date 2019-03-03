package android.net.metrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.SparseArray;
import com.android.internal.util.MessageUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ValidationProbeEvent
  implements Parcelable
{
  public static final Parcelable.Creator<ValidationProbeEvent> CREATOR = new Parcelable.Creator()
  {
    public ValidationProbeEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ValidationProbeEvent(paramAnonymousParcel, null);
    }
    
    public ValidationProbeEvent[] newArray(int paramAnonymousInt)
    {
      return new ValidationProbeEvent[paramAnonymousInt];
    }
  };
  public static final int DNS_FAILURE = 0;
  public static final int DNS_SUCCESS = 1;
  private static final int FIRST_VALIDATION = 256;
  public static final int PROBE_DNS = 0;
  public static final int PROBE_FALLBACK = 4;
  public static final int PROBE_HTTP = 1;
  public static final int PROBE_HTTPS = 2;
  public static final int PROBE_PAC = 3;
  private static final int REVALIDATION = 512;
  public long durationMs;
  public int probeType;
  public int returnCode;
  
  public ValidationProbeEvent() {}
  
  private ValidationProbeEvent(Parcel paramParcel)
  {
    durationMs = paramParcel.readLong();
    probeType = paramParcel.readInt();
    returnCode = paramParcel.readInt();
  }
  
  public static String getProbeName(int paramInt)
  {
    return (String)Decoder.constants.get(paramInt & 0xFF, "PROBE_???");
  }
  
  public static String getValidationStage(int paramInt)
  {
    return (String)Decoder.constants.get(0xFF00 & paramInt, "UNKNOWN");
  }
  
  public static int makeProbeType(int paramInt, boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 256;
    } else {
      i = 512;
    }
    return paramInt & 0xFF | i;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String toString()
  {
    return String.format("ValidationProbeEvent(%s:%d %s, %dms)", new Object[] { getProbeName(probeType), Integer.valueOf(returnCode), getValidationStage(probeType), Long.valueOf(durationMs) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(durationMs);
    paramParcel.writeInt(probeType);
    paramParcel.writeInt(returnCode);
  }
  
  static final class Decoder
  {
    static final SparseArray<String> constants = MessageUtils.findMessageNames(new Class[] { ValidationProbeEvent.class }, new String[] { "PROBE_", "FIRST_", "REVALIDATION" });
    
    Decoder() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ReturnCode {}
}
