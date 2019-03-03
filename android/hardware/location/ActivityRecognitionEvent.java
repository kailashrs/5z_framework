package android.hardware.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ActivityRecognitionEvent
  implements Parcelable
{
  public static final Parcelable.Creator<ActivityRecognitionEvent> CREATOR = new Parcelable.Creator()
  {
    public ActivityRecognitionEvent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ActivityRecognitionEvent(paramAnonymousParcel.readString(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readLong());
    }
    
    public ActivityRecognitionEvent[] newArray(int paramAnonymousInt)
    {
      return new ActivityRecognitionEvent[paramAnonymousInt];
    }
  };
  private final String mActivity;
  private final int mEventType;
  private final long mTimestampNs;
  
  public ActivityRecognitionEvent(String paramString, int paramInt, long paramLong)
  {
    mActivity = paramString;
    mEventType = paramInt;
    mTimestampNs = paramLong;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getActivity()
  {
    return mActivity;
  }
  
  public int getEventType()
  {
    return mEventType;
  }
  
  public long getTimestampNs()
  {
    return mTimestampNs;
  }
  
  public String toString()
  {
    return String.format("Activity='%s', EventType=%s, TimestampNs=%s", new Object[] { mActivity, Integer.valueOf(mEventType), Long.valueOf(mTimestampNs) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mActivity);
    paramParcel.writeInt(mEventType);
    paramParcel.writeLong(mTimestampNs);
  }
}
