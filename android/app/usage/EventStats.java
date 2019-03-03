package android.app.usage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class EventStats
  implements Parcelable
{
  public static final Parcelable.Creator<EventStats> CREATOR = new Parcelable.Creator()
  {
    public EventStats createFromParcel(Parcel paramAnonymousParcel)
    {
      EventStats localEventStats = new EventStats();
      mEventType = paramAnonymousParcel.readInt();
      mBeginTimeStamp = paramAnonymousParcel.readLong();
      mEndTimeStamp = paramAnonymousParcel.readLong();
      mLastEventTime = paramAnonymousParcel.readLong();
      mTotalTime = paramAnonymousParcel.readLong();
      mCount = paramAnonymousParcel.readInt();
      return localEventStats;
    }
    
    public EventStats[] newArray(int paramAnonymousInt)
    {
      return new EventStats[paramAnonymousInt];
    }
  };
  public long mBeginTimeStamp;
  public int mCount;
  public long mEndTimeStamp;
  public int mEventType;
  public long mLastEventTime;
  public long mTotalTime;
  
  public EventStats() {}
  
  public EventStats(EventStats paramEventStats)
  {
    mEventType = mEventType;
    mBeginTimeStamp = mBeginTimeStamp;
    mEndTimeStamp = mEndTimeStamp;
    mLastEventTime = mLastEventTime;
    mTotalTime = mTotalTime;
    mCount = mCount;
  }
  
  public void add(EventStats paramEventStats)
  {
    if (mEventType == mEventType)
    {
      if (mBeginTimeStamp > mBeginTimeStamp) {
        mLastEventTime = Math.max(mLastEventTime, mLastEventTime);
      }
      mBeginTimeStamp = Math.min(mBeginTimeStamp, mBeginTimeStamp);
      mEndTimeStamp = Math.max(mEndTimeStamp, mEndTimeStamp);
      mTotalTime += mTotalTime;
      mCount += mCount;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Can't merge EventStats for event #");
    localStringBuilder.append(mEventType);
    localStringBuilder.append(" with EventStats for event #");
    localStringBuilder.append(mEventType);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCount()
  {
    return mCount;
  }
  
  public int getEventType()
  {
    return mEventType;
  }
  
  public long getFirstTimeStamp()
  {
    return mBeginTimeStamp;
  }
  
  public long getLastEventTime()
  {
    return mLastEventTime;
  }
  
  public long getLastTimeStamp()
  {
    return mEndTimeStamp;
  }
  
  public long getTotalTime()
  {
    return mTotalTime;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mEventType);
    paramParcel.writeLong(mBeginTimeStamp);
    paramParcel.writeLong(mEndTimeStamp);
    paramParcel.writeLong(mLastEventTime);
    paramParcel.writeLong(mTotalTime);
    paramParcel.writeInt(mCount);
  }
}
