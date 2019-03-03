package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class DataUsageRequest
  implements Parcelable
{
  public static final Parcelable.Creator<DataUsageRequest> CREATOR = new Parcelable.Creator()
  {
    public DataUsageRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DataUsageRequest(paramAnonymousParcel.readInt(), (NetworkTemplate)paramAnonymousParcel.readParcelable(null), paramAnonymousParcel.readLong());
    }
    
    public DataUsageRequest[] newArray(int paramAnonymousInt)
    {
      return new DataUsageRequest[paramAnonymousInt];
    }
  };
  public static final String PARCELABLE_KEY = "DataUsageRequest";
  public static final int REQUEST_ID_UNSET = 0;
  public final int requestId;
  public final NetworkTemplate template;
  public final long thresholdInBytes;
  
  public DataUsageRequest(int paramInt, NetworkTemplate paramNetworkTemplate, long paramLong)
  {
    requestId = paramInt;
    template = paramNetworkTemplate;
    thresholdInBytes = paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof DataUsageRequest;
    boolean bool2 = false;
    if (!bool1) {
      return false;
    }
    paramObject = (DataUsageRequest)paramObject;
    bool1 = bool2;
    if (requestId == requestId)
    {
      bool1 = bool2;
      if (Objects.equals(template, template))
      {
        bool1 = bool2;
        if (thresholdInBytes == thresholdInBytes) {
          bool1 = true;
        }
      }
    }
    return bool1;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(requestId), template, Long.valueOf(thresholdInBytes) });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DataUsageRequest [ requestId=");
    localStringBuilder.append(requestId);
    localStringBuilder.append(", networkTemplate=");
    localStringBuilder.append(template);
    localStringBuilder.append(", thresholdInBytes=");
    localStringBuilder.append(thresholdInBytes);
    localStringBuilder.append(" ]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(requestId);
    paramParcel.writeParcelable(template, paramInt);
    paramParcel.writeLong(thresholdInBytes);
  }
}
