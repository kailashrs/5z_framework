package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.InvalidParameterException;

@SystemApi
public class GpsNavigationMessageEvent
  implements Parcelable
{
  public static final Parcelable.Creator<GpsNavigationMessageEvent> CREATOR = new Parcelable.Creator()
  {
    public GpsNavigationMessageEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new GpsNavigationMessageEvent((GpsNavigationMessage)paramAnonymousParcel.readParcelable(getClass().getClassLoader()));
    }
    
    public GpsNavigationMessageEvent[] newArray(int paramAnonymousInt)
    {
      return new GpsNavigationMessageEvent[paramAnonymousInt];
    }
  };
  public static int STATUS_GPS_LOCATION_DISABLED;
  public static int STATUS_NOT_SUPPORTED = 0;
  public static int STATUS_READY = 1;
  private final GpsNavigationMessage mNavigationMessage;
  
  static
  {
    STATUS_GPS_LOCATION_DISABLED = 2;
  }
  
  public GpsNavigationMessageEvent(GpsNavigationMessage paramGpsNavigationMessage)
  {
    if (paramGpsNavigationMessage != null)
    {
      mNavigationMessage = paramGpsNavigationMessage;
      return;
    }
    throw new InvalidParameterException("Parameter 'message' must not be null.");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public GpsNavigationMessage getNavigationMessage()
  {
    return mNavigationMessage;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("[ GpsNavigationMessageEvent:\n\n");
    localStringBuilder.append(mNavigationMessage.toString());
    localStringBuilder.append("\n]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mNavigationMessage, paramInt);
  }
  
  @SystemApi
  public static abstract interface Listener
  {
    public abstract void onGpsNavigationMessageReceived(GpsNavigationMessageEvent paramGpsNavigationMessageEvent);
    
    public abstract void onStatusChanged(int paramInt);
  }
}
