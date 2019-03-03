package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

@SystemApi
public class GpsMeasurementsEvent
  implements Parcelable
{
  public static final Parcelable.Creator<GpsMeasurementsEvent> CREATOR = new Parcelable.Creator()
  {
    public GpsMeasurementsEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      GpsClock localGpsClock = (GpsClock)paramAnonymousParcel.readParcelable(getClass().getClassLoader());
      GpsMeasurement[] arrayOfGpsMeasurement = new GpsMeasurement[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readTypedArray(arrayOfGpsMeasurement, GpsMeasurement.CREATOR);
      return new GpsMeasurementsEvent(localGpsClock, arrayOfGpsMeasurement);
    }
    
    public GpsMeasurementsEvent[] newArray(int paramAnonymousInt)
    {
      return new GpsMeasurementsEvent[paramAnonymousInt];
    }
  };
  public static final int STATUS_GPS_LOCATION_DISABLED = 2;
  public static final int STATUS_NOT_SUPPORTED = 0;
  public static final int STATUS_READY = 1;
  private final GpsClock mClock;
  private final Collection<GpsMeasurement> mReadOnlyMeasurements;
  
  public GpsMeasurementsEvent(GpsClock paramGpsClock, GpsMeasurement[] paramArrayOfGpsMeasurement)
  {
    if (paramGpsClock != null)
    {
      if ((paramArrayOfGpsMeasurement != null) && (paramArrayOfGpsMeasurement.length != 0))
      {
        mClock = paramGpsClock;
        mReadOnlyMeasurements = Collections.unmodifiableCollection(Arrays.asList(paramArrayOfGpsMeasurement));
        return;
      }
      throw new InvalidParameterException("Parameter 'measurements' must not be null or empty.");
    }
    throw new InvalidParameterException("Parameter 'clock' must not be null.");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public GpsClock getClock()
  {
    return mClock;
  }
  
  public Collection<GpsMeasurement> getMeasurements()
  {
    return mReadOnlyMeasurements;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("[ GpsMeasurementsEvent:\n\n");
    localStringBuilder.append(mClock.toString());
    localStringBuilder.append("\n");
    Iterator localIterator = mReadOnlyMeasurements.iterator();
    while (localIterator.hasNext())
    {
      localStringBuilder.append(((GpsMeasurement)localIterator.next()).toString());
      localStringBuilder.append("\n");
    }
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(mClock, paramInt);
    int i = mReadOnlyMeasurements.size();
    GpsMeasurement[] arrayOfGpsMeasurement = (GpsMeasurement[])mReadOnlyMeasurements.toArray(new GpsMeasurement[i]);
    paramParcel.writeInt(arrayOfGpsMeasurement.length);
    paramParcel.writeTypedArray(arrayOfGpsMeasurement, paramInt);
  }
  
  @SystemApi
  public static abstract interface Listener
  {
    public abstract void onGpsMeasurementsReceived(GpsMeasurementsEvent paramGpsMeasurementsEvent);
    
    public abstract void onStatusChanged(int paramInt);
  }
}
