package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@Deprecated
public class WpsInfo
  implements Parcelable
{
  @Deprecated
  public static final Parcelable.Creator<WpsInfo> CREATOR = new Parcelable.Creator()
  {
    @Deprecated
    public WpsInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      WpsInfo localWpsInfo = new WpsInfo();
      setup = paramAnonymousParcel.readInt();
      BSSID = paramAnonymousParcel.readString();
      pin = paramAnonymousParcel.readString();
      return localWpsInfo;
    }
    
    @Deprecated
    public WpsInfo[] newArray(int paramAnonymousInt)
    {
      return new WpsInfo[paramAnonymousInt];
    }
  };
  @Deprecated
  public static final int DISPLAY = 1;
  @Deprecated
  public static final int INVALID = 4;
  @Deprecated
  public static final int KEYPAD = 2;
  @Deprecated
  public static final int LABEL = 3;
  @Deprecated
  public static final int PBC = 0;
  @Deprecated
  public String BSSID;
  @Deprecated
  public String pin;
  @Deprecated
  public int setup;
  
  @Deprecated
  public WpsInfo()
  {
    setup = 4;
    BSSID = null;
    pin = null;
  }
  
  @Deprecated
  public WpsInfo(WpsInfo paramWpsInfo)
  {
    if (paramWpsInfo != null)
    {
      setup = setup;
      BSSID = BSSID;
      pin = pin;
    }
  }
  
  @Deprecated
  public int describeContents()
  {
    return 0;
  }
  
  @Deprecated
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(" setup: ");
    localStringBuffer.append(setup);
    localStringBuffer.append('\n');
    localStringBuffer.append(" BSSID: ");
    localStringBuffer.append(BSSID);
    localStringBuffer.append('\n');
    localStringBuffer.append(" pin: ");
    localStringBuffer.append(pin);
    localStringBuffer.append('\n');
    return localStringBuffer.toString();
  }
  
  @Deprecated
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(setup);
    paramParcel.writeString(BSSID);
    paramParcel.writeString(pin);
  }
}
