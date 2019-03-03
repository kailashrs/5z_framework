package android.net;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SystemApi
public class WifiKey
  implements Parcelable
{
  private static final Pattern BSSID_PATTERN = Pattern.compile("([\\p{XDigit}]{2}:){5}[\\p{XDigit}]{2}");
  public static final Parcelable.Creator<WifiKey> CREATOR = new Parcelable.Creator()
  {
    public WifiKey createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WifiKey(paramAnonymousParcel, null);
    }
    
    public WifiKey[] newArray(int paramAnonymousInt)
    {
      return new WifiKey[paramAnonymousInt];
    }
  };
  private static final Pattern SSID_PATTERN = Pattern.compile("(\".*\")|(0x[\\p{XDigit}]+)", 32);
  public final String bssid;
  public final String ssid;
  
  private WifiKey(Parcel paramParcel)
  {
    ssid = paramParcel.readString();
    bssid = paramParcel.readString();
  }
  
  public WifiKey(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (SSID_PATTERN.matcher(paramString1).matches()))
    {
      if ((paramString2 != null) && (BSSID_PATTERN.matcher(paramString2).matches()))
      {
        ssid = paramString1;
        bssid = paramString2;
        return;
      }
      paramString1 = new StringBuilder();
      paramString1.append("Invalid bssid: ");
      paramString1.append(paramString2);
      throw new IllegalArgumentException(paramString1.toString());
    }
    paramString2 = new StringBuilder();
    paramString2.append("Invalid ssid: ");
    paramString2.append(paramString1);
    throw new IllegalArgumentException(paramString2.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if ((paramObject != null) && (getClass() == paramObject.getClass()))
    {
      paramObject = (WifiKey)paramObject;
      if ((!Objects.equals(ssid, ssid)) || (!Objects.equals(bssid, bssid))) {
        bool = false;
      }
      return bool;
    }
    return false;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { ssid, bssid });
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WifiKey[SSID=");
    localStringBuilder.append(ssid);
    localStringBuilder.append(",BSSID=");
    localStringBuilder.append(bssid);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(ssid);
    paramParcel.writeString(bssid);
  }
}
