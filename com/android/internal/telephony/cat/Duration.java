package com.android.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Duration
  implements Parcelable
{
  public static final Parcelable.Creator<Duration> CREATOR = new Parcelable.Creator()
  {
    public Duration createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Duration(paramAnonymousParcel, null);
    }
    
    public Duration[] newArray(int paramAnonymousInt)
    {
      return new Duration[paramAnonymousInt];
    }
  };
  public int timeInterval;
  public TimeUnit timeUnit;
  
  public Duration(int paramInt, TimeUnit paramTimeUnit)
  {
    timeInterval = paramInt;
    timeUnit = paramTimeUnit;
  }
  
  private Duration(Parcel paramParcel)
  {
    timeInterval = paramParcel.readInt();
    timeUnit = TimeUnit.values()[paramParcel.readInt()];
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(timeInterval);
    paramParcel.writeInt(timeUnit.ordinal());
  }
  
  public static enum TimeUnit
  {
    MINUTE(0),  SECOND(1),  TENTH_SECOND(2);
    
    private int mValue;
    
    private TimeUnit(int paramInt)
    {
      mValue = paramInt;
    }
    
    public int value()
    {
      return mValue;
    }
  }
}
