package android.telephony.euicc;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class EuiccInfo
  implements Parcelable
{
  public static final Parcelable.Creator<EuiccInfo> CREATOR = new Parcelable.Creator()
  {
    public EuiccInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new EuiccInfo(paramAnonymousParcel, null);
    }
    
    public EuiccInfo[] newArray(int paramAnonymousInt)
    {
      return new EuiccInfo[paramAnonymousInt];
    }
  };
  private final String osVersion;
  
  private EuiccInfo(Parcel paramParcel)
  {
    osVersion = paramParcel.readString();
  }
  
  public EuiccInfo(String paramString)
  {
    osVersion = paramString;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getOsVersion()
  {
    return osVersion;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(osVersion);
  }
}
