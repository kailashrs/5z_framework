package android.hardware.location;

import android.annotation.SystemApi;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class GeofenceHardwareMonitorEvent
  implements Parcelable
{
  public static final Parcelable.Creator<GeofenceHardwareMonitorEvent> CREATOR = new Parcelable.Creator()
  {
    public GeofenceHardwareMonitorEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      ClassLoader localClassLoader = GeofenceHardwareMonitorEvent.class.getClassLoader();
      return new GeofenceHardwareMonitorEvent(paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), (Location)paramAnonymousParcel.readParcelable(localClassLoader));
    }
    
    public GeofenceHardwareMonitorEvent[] newArray(int paramAnonymousInt)
    {
      return new GeofenceHardwareMonitorEvent[paramAnonymousInt];
    }
  };
  private final Location mLocation;
  private final int mMonitoringStatus;
  private final int mMonitoringType;
  private final int mSourceTechnologies;
  
  public GeofenceHardwareMonitorEvent(int paramInt1, int paramInt2, int paramInt3, Location paramLocation)
  {
    mMonitoringType = paramInt1;
    mMonitoringStatus = paramInt2;
    mSourceTechnologies = paramInt3;
    mLocation = paramLocation;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Location getLocation()
  {
    return mLocation;
  }
  
  public int getMonitoringStatus()
  {
    return mMonitoringStatus;
  }
  
  public int getMonitoringType()
  {
    return mMonitoringType;
  }
  
  public int getSourceTechnologies()
  {
    return mSourceTechnologies;
  }
  
  public String toString()
  {
    return String.format("GeofenceHardwareMonitorEvent: type=%d, status=%d, sources=%d, location=%s", new Object[] { Integer.valueOf(mMonitoringType), Integer.valueOf(mMonitoringStatus), Integer.valueOf(mSourceTechnologies), mLocation });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mMonitoringType);
    paramParcel.writeInt(mMonitoringStatus);
    paramParcel.writeInt(mSourceTechnologies);
    paramParcel.writeParcelable(mLocation, paramInt);
  }
}
