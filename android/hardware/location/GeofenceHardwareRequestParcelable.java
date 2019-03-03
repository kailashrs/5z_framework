package android.hardware.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public final class GeofenceHardwareRequestParcelable
  implements Parcelable
{
  public static final Parcelable.Creator<GeofenceHardwareRequestParcelable> CREATOR = new Parcelable.Creator()
  {
    public GeofenceHardwareRequestParcelable createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = paramAnonymousParcel.readInt();
      if (i != 0)
      {
        Log.e("GeofenceHardwareRequest", String.format("Invalid Geofence type: %d", new Object[] { Integer.valueOf(i) }));
        return null;
      }
      GeofenceHardwareRequest localGeofenceHardwareRequest = GeofenceHardwareRequest.createCircularGeofence(paramAnonymousParcel.readDouble(), paramAnonymousParcel.readDouble(), paramAnonymousParcel.readDouble());
      localGeofenceHardwareRequest.setLastTransition(paramAnonymousParcel.readInt());
      localGeofenceHardwareRequest.setMonitorTransitions(paramAnonymousParcel.readInt());
      localGeofenceHardwareRequest.setUnknownTimer(paramAnonymousParcel.readInt());
      localGeofenceHardwareRequest.setNotificationResponsiveness(paramAnonymousParcel.readInt());
      localGeofenceHardwareRequest.setSourceTechnologies(paramAnonymousParcel.readInt());
      return new GeofenceHardwareRequestParcelable(paramAnonymousParcel.readInt(), localGeofenceHardwareRequest);
    }
    
    public GeofenceHardwareRequestParcelable[] newArray(int paramAnonymousInt)
    {
      return new GeofenceHardwareRequestParcelable[paramAnonymousInt];
    }
  };
  private int mId;
  private GeofenceHardwareRequest mRequest;
  
  public GeofenceHardwareRequestParcelable(int paramInt, GeofenceHardwareRequest paramGeofenceHardwareRequest)
  {
    mId = paramInt;
    mRequest = paramGeofenceHardwareRequest;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getId()
  {
    return mId;
  }
  
  public int getLastTransition()
  {
    return mRequest.getLastTransition();
  }
  
  public double getLatitude()
  {
    return mRequest.getLatitude();
  }
  
  public double getLongitude()
  {
    return mRequest.getLongitude();
  }
  
  public int getMonitorTransitions()
  {
    return mRequest.getMonitorTransitions();
  }
  
  public int getNotificationResponsiveness()
  {
    return mRequest.getNotificationResponsiveness();
  }
  
  public double getRadius()
  {
    return mRequest.getRadius();
  }
  
  int getSourceTechnologies()
  {
    return mRequest.getSourceTechnologies();
  }
  
  int getType()
  {
    return mRequest.getType();
  }
  
  public int getUnknownTimer()
  {
    return mRequest.getUnknownTimer();
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("id=");
    localStringBuilder.append(mId);
    localStringBuilder.append(", type=");
    localStringBuilder.append(mRequest.getType());
    localStringBuilder.append(", latitude=");
    localStringBuilder.append(mRequest.getLatitude());
    localStringBuilder.append(", longitude=");
    localStringBuilder.append(mRequest.getLongitude());
    localStringBuilder.append(", radius=");
    localStringBuilder.append(mRequest.getRadius());
    localStringBuilder.append(", lastTransition=");
    localStringBuilder.append(mRequest.getLastTransition());
    localStringBuilder.append(", unknownTimer=");
    localStringBuilder.append(mRequest.getUnknownTimer());
    localStringBuilder.append(", monitorTransitions=");
    localStringBuilder.append(mRequest.getMonitorTransitions());
    localStringBuilder.append(", notificationResponsiveness=");
    localStringBuilder.append(mRequest.getNotificationResponsiveness());
    localStringBuilder.append(", sourceTechnologies=");
    localStringBuilder.append(mRequest.getSourceTechnologies());
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(getType());
    paramParcel.writeDouble(getLatitude());
    paramParcel.writeDouble(getLongitude());
    paramParcel.writeDouble(getRadius());
    paramParcel.writeInt(getLastTransition());
    paramParcel.writeInt(getMonitorTransitions());
    paramParcel.writeInt(getUnknownTimer());
    paramParcel.writeInt(getNotificationResponsiveness());
    paramParcel.writeInt(getSourceTechnologies());
    paramParcel.writeInt(getId());
  }
}
