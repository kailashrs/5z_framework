package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class WifiActivityEnergyInfo
  implements Parcelable
{
  public static final Parcelable.Creator<WifiActivityEnergyInfo> CREATOR = new Parcelable.Creator()
  {
    public WifiActivityEnergyInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WifiActivityEnergyInfo(paramAnonymousParcel.readLong(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readLong(), paramAnonymousParcel.createLongArray(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong(), paramAnonymousParcel.readLong());
    }
    
    public WifiActivityEnergyInfo[] newArray(int paramAnonymousInt)
    {
      return new WifiActivityEnergyInfo[paramAnonymousInt];
    }
  };
  public static final int STACK_STATE_INVALID = 0;
  public static final int STACK_STATE_STATE_ACTIVE = 1;
  public static final int STACK_STATE_STATE_IDLE = 3;
  public static final int STACK_STATE_STATE_SCANNING = 2;
  public long mControllerEnergyUsed;
  public long mControllerIdleTimeMs;
  public long mControllerRxTimeMs;
  public long mControllerScanTimeMs;
  public long mControllerTxTimeMs;
  public long[] mControllerTxTimePerLevelMs;
  public int mStackState;
  public long mTimestamp;
  
  public WifiActivityEnergyInfo(long paramLong1, int paramInt, long paramLong2, long[] paramArrayOfLong, long paramLong3, long paramLong4, long paramLong5, long paramLong6)
  {
    mTimestamp = paramLong1;
    mStackState = paramInt;
    mControllerTxTimeMs = paramLong2;
    mControllerTxTimePerLevelMs = paramArrayOfLong;
    mControllerRxTimeMs = paramLong3;
    mControllerScanTimeMs = paramLong4;
    mControllerIdleTimeMs = paramLong5;
    mControllerEnergyUsed = paramLong6;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getControllerEnergyUsed()
  {
    return mControllerEnergyUsed;
  }
  
  public long getControllerIdleTimeMillis()
  {
    return mControllerIdleTimeMs;
  }
  
  public long getControllerRxTimeMillis()
  {
    return mControllerRxTimeMs;
  }
  
  public long getControllerScanTimeMillis()
  {
    return mControllerScanTimeMs;
  }
  
  public long getControllerTxTimeMillis()
  {
    return mControllerTxTimeMs;
  }
  
  public long getControllerTxTimeMillisAtLevel(int paramInt)
  {
    if (paramInt < mControllerTxTimePerLevelMs.length) {
      return mControllerTxTimePerLevelMs[paramInt];
    }
    return 0L;
  }
  
  public int getStackState()
  {
    return mStackState;
  }
  
  public long getTimeStamp()
  {
    return mTimestamp;
  }
  
  public boolean isValid()
  {
    boolean bool;
    if ((mControllerTxTimeMs >= 0L) && (mControllerRxTimeMs >= 0L) && (mControllerScanTimeMs >= 0L) && (mControllerIdleTimeMs >= 0L)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("WifiActivityEnergyInfo{ timestamp=");
    localStringBuilder.append(mTimestamp);
    localStringBuilder.append(" mStackState=");
    localStringBuilder.append(mStackState);
    localStringBuilder.append(" mControllerTxTimeMs=");
    localStringBuilder.append(mControllerTxTimeMs);
    localStringBuilder.append(" mControllerTxTimePerLevelMs=");
    localStringBuilder.append(Arrays.toString(mControllerTxTimePerLevelMs));
    localStringBuilder.append(" mControllerRxTimeMs=");
    localStringBuilder.append(mControllerRxTimeMs);
    localStringBuilder.append(" mControllerScanTimeMs=");
    localStringBuilder.append(mControllerScanTimeMs);
    localStringBuilder.append(" mControllerIdleTimeMs=");
    localStringBuilder.append(mControllerIdleTimeMs);
    localStringBuilder.append(" mControllerEnergyUsed=");
    localStringBuilder.append(mControllerEnergyUsed);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mTimestamp);
    paramParcel.writeInt(mStackState);
    paramParcel.writeLong(mControllerTxTimeMs);
    paramParcel.writeLongArray(mControllerTxTimePerLevelMs);
    paramParcel.writeLong(mControllerRxTimeMs);
    paramParcel.writeLong(mControllerScanTimeMs);
    paramParcel.writeLong(mControllerIdleTimeMs);
    paramParcel.writeLong(mControllerEnergyUsed);
  }
}
