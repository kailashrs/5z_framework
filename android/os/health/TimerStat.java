package android.os.health;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class TimerStat
  implements Parcelable
{
  public static final Parcelable.Creator<TimerStat> CREATOR = new Parcelable.Creator()
  {
    public TimerStat createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TimerStat(paramAnonymousParcel);
    }
    
    public TimerStat[] newArray(int paramAnonymousInt)
    {
      return new TimerStat[paramAnonymousInt];
    }
  };
  private int mCount;
  private long mTime;
  
  public TimerStat() {}
  
  public TimerStat(int paramInt, long paramLong)
  {
    mCount = paramInt;
    mTime = paramLong;
  }
  
  public TimerStat(Parcel paramParcel)
  {
    mCount = paramParcel.readInt();
    mTime = paramParcel.readLong();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCount()
  {
    return mCount;
  }
  
  public long getTime()
  {
    return mTime;
  }
  
  public void setCount(int paramInt)
  {
    mCount = paramInt;
  }
  
  public void setTime(long paramLong)
  {
    mTime = paramLong;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mCount);
    paramParcel.writeLong(mTime);
  }
}
