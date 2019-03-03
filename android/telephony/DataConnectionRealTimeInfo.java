package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class DataConnectionRealTimeInfo
  implements Parcelable
{
  public static final Parcelable.Creator<DataConnectionRealTimeInfo> CREATOR = new Parcelable.Creator()
  {
    public DataConnectionRealTimeInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DataConnectionRealTimeInfo(paramAnonymousParcel, null);
    }
    
    public DataConnectionRealTimeInfo[] newArray(int paramAnonymousInt)
    {
      return new DataConnectionRealTimeInfo[paramAnonymousInt];
    }
  };
  public static final int DC_POWER_STATE_HIGH = 3;
  public static final int DC_POWER_STATE_LOW = 1;
  public static final int DC_POWER_STATE_MEDIUM = 2;
  public static final int DC_POWER_STATE_UNKNOWN = Integer.MAX_VALUE;
  private int mDcPowerState;
  private long mTime;
  
  public DataConnectionRealTimeInfo()
  {
    mTime = Long.MAX_VALUE;
    mDcPowerState = Integer.MAX_VALUE;
  }
  
  public DataConnectionRealTimeInfo(long paramLong, int paramInt)
  {
    mTime = paramLong;
    mDcPowerState = paramInt;
  }
  
  private DataConnectionRealTimeInfo(Parcel paramParcel)
  {
    mTime = paramParcel.readLong();
    mDcPowerState = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    paramObject = (DataConnectionRealTimeInfo)paramObject;
    if ((mTime != mTime) || (mDcPowerState != mDcPowerState)) {
      bool = false;
    }
    return bool;
  }
  
  public int getDcPowerState()
  {
    return mDcPowerState;
  }
  
  public long getTime()
  {
    return mTime;
  }
  
  public int hashCode()
  {
    long l = 17L * 1L + mTime;
    return (int)(l + (17L * l + mDcPowerState));
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("mTime=");
    localStringBuffer.append(mTime);
    localStringBuffer.append(" mDcPowerState=");
    localStringBuffer.append(mDcPowerState);
    return localStringBuffer.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mTime);
    paramParcel.writeInt(mDcPowerState);
  }
}
