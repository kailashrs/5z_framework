package android.os.connectivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class GpsBatteryStats
  implements Parcelable
{
  public static final Parcelable.Creator<GpsBatteryStats> CREATOR = new Parcelable.Creator()
  {
    public GpsBatteryStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new GpsBatteryStats(paramAnonymousParcel, null);
    }
    
    public GpsBatteryStats[] newArray(int paramAnonymousInt)
    {
      return new GpsBatteryStats[paramAnonymousInt];
    }
  };
  private long mEnergyConsumedMaMs;
  private long mLoggingDurationMs;
  private long[] mTimeInGpsSignalQualityLevel;
  
  public GpsBatteryStats()
  {
    initialize();
  }
  
  private GpsBatteryStats(Parcel paramParcel)
  {
    initialize();
    readFromParcel(paramParcel);
  }
  
  private void initialize()
  {
    mLoggingDurationMs = 0L;
    mEnergyConsumedMaMs = 0L;
    mTimeInGpsSignalQualityLevel = new long[2];
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getEnergyConsumedMaMs()
  {
    return mEnergyConsumedMaMs;
  }
  
  public long getLoggingDurationMs()
  {
    return mLoggingDurationMs;
  }
  
  public long[] getTimeInGpsSignalQualityLevel()
  {
    return mTimeInGpsSignalQualityLevel;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mLoggingDurationMs = paramParcel.readLong();
    mEnergyConsumedMaMs = paramParcel.readLong();
    paramParcel.readLongArray(mTimeInGpsSignalQualityLevel);
  }
  
  public void setEnergyConsumedMaMs(long paramLong)
  {
    mEnergyConsumedMaMs = paramLong;
  }
  
  public void setLoggingDurationMs(long paramLong)
  {
    mLoggingDurationMs = paramLong;
  }
  
  public void setTimeInGpsSignalQualityLevel(long[] paramArrayOfLong)
  {
    mTimeInGpsSignalQualityLevel = Arrays.copyOfRange(paramArrayOfLong, 0, Math.min(paramArrayOfLong.length, 2));
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mLoggingDurationMs);
    paramParcel.writeLong(mEnergyConsumedMaMs);
    paramParcel.writeLongArray(mTimeInGpsSignalQualityLevel);
  }
}
