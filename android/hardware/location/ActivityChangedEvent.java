package android.hardware.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ActivityChangedEvent
  implements Parcelable
{
  public static final Parcelable.Creator<ActivityChangedEvent> CREATOR = new Parcelable.Creator()
  {
    public ActivityChangedEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      ActivityRecognitionEvent[] arrayOfActivityRecognitionEvent = new ActivityRecognitionEvent[paramAnonymousParcel.readInt()];
      paramAnonymousParcel.readTypedArray(arrayOfActivityRecognitionEvent, ActivityRecognitionEvent.CREATOR);
      return new ActivityChangedEvent(arrayOfActivityRecognitionEvent);
    }
    
    public ActivityChangedEvent[] newArray(int paramAnonymousInt)
    {
      return new ActivityChangedEvent[paramAnonymousInt];
    }
  };
  private final List<ActivityRecognitionEvent> mActivityRecognitionEvents;
  
  public ActivityChangedEvent(ActivityRecognitionEvent[] paramArrayOfActivityRecognitionEvent)
  {
    if (paramArrayOfActivityRecognitionEvent != null)
    {
      mActivityRecognitionEvents = Arrays.asList(paramArrayOfActivityRecognitionEvent);
      return;
    }
    throw new InvalidParameterException("Parameter 'activityRecognitionEvents' must not be null.");
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public Iterable<ActivityRecognitionEvent> getActivityRecognitionEvents()
  {
    return mActivityRecognitionEvents;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("[ ActivityChangedEvent:");
    Iterator localIterator = mActivityRecognitionEvents.iterator();
    while (localIterator.hasNext())
    {
      ActivityRecognitionEvent localActivityRecognitionEvent = (ActivityRecognitionEvent)localIterator.next();
      localStringBuilder.append("\n    ");
      localStringBuilder.append(localActivityRecognitionEvent.toString());
    }
    localStringBuilder.append("\n]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    ActivityRecognitionEvent[] arrayOfActivityRecognitionEvent = (ActivityRecognitionEvent[])mActivityRecognitionEvents.toArray(new ActivityRecognitionEvent[0]);
    paramParcel.writeInt(arrayOfActivityRecognitionEvent.length);
    paramParcel.writeTypedArray(arrayOfActivityRecognitionEvent, paramInt);
  }
}
