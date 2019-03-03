package android.telephony.mbms;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class StreamingServiceInfo
  extends ServiceInfo
  implements Parcelable
{
  public static final Parcelable.Creator<StreamingServiceInfo> CREATOR = new Parcelable.Creator()
  {
    public StreamingServiceInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new StreamingServiceInfo(paramAnonymousParcel, null);
    }
    
    public StreamingServiceInfo[] newArray(int paramAnonymousInt)
    {
      return new StreamingServiceInfo[paramAnonymousInt];
    }
  };
  
  private StreamingServiceInfo(Parcel paramParcel)
  {
    super(paramParcel);
  }
  
  @SystemApi
  public StreamingServiceInfo(Map<Locale, String> paramMap, String paramString1, List<Locale> paramList, String paramString2, Date paramDate1, Date paramDate2)
  {
    super(paramMap, paramString1, paramList, paramString2, paramDate1, paramDate2);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
  }
}
