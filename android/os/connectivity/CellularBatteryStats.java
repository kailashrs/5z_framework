package android.os.connectivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class CellularBatteryStats
  implements Parcelable
{
  public static final Parcelable.Creator<CellularBatteryStats> CREATOR = new Parcelable.Creator()
  {
    public CellularBatteryStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CellularBatteryStats(paramAnonymousParcel, null);
    }
    
    public CellularBatteryStats[] newArray(int paramAnonymousInt)
    {
      return new CellularBatteryStats[paramAnonymousInt];
    }
  };
  private long mEnergyConsumedMaMs;
  private long mIdleTimeMs;
  private long mKernelActiveTimeMs;
  private long mLoggingDurationMs;
  private long mNumBytesRx;
  private long mNumBytesTx;
  private long mNumPacketsRx;
  private long mNumPacketsTx;
  private long mRxTimeMs;
  private long mSleepTimeMs;
  private long[] mTimeInRatMs;
  private long[] mTimeInRxSignalStrengthLevelMs;
  private long[] mTxTimeMs;
  
  public CellularBatteryStats()
  {
    initialize();
  }
  
  private CellularBatteryStats(Parcel paramParcel)
  {
    initialize();
    readFromParcel(paramParcel);
  }
  
  private void initialize()
  {
    mLoggingDurationMs = 0L;
    mKernelActiveTimeMs = 0L;
    mNumPacketsTx = 0L;
    mNumBytesTx = 0L;
    mNumPacketsRx = 0L;
    mNumBytesRx = 0L;
    mSleepTimeMs = 0L;
    mIdleTimeMs = 0L;
    mRxTimeMs = 0L;
    mEnergyConsumedMaMs = 0L;
    mTimeInRatMs = new long[21];
    Arrays.fill(mTimeInRatMs, 0L);
    mTimeInRxSignalStrengthLevelMs = new long[6];
    Arrays.fill(mTimeInRxSignalStrengthLevelMs, 0L);
    mTxTimeMs = new long[5];
    Arrays.fill(mTxTimeMs, 0L);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public long getEnergyConsumedMaMs()
  {
    return mEnergyConsumedMaMs;
  }
  
  public long getIdleTimeMs()
  {
    return mIdleTimeMs;
  }
  
  public long getKernelActiveTimeMs()
  {
    return mKernelActiveTimeMs;
  }
  
  public long getLoggingDurationMs()
  {
    return mLoggingDurationMs;
  }
  
  public long getNumBytesRx()
  {
    return mNumBytesRx;
  }
  
  public long getNumBytesTx()
  {
    return mNumBytesTx;
  }
  
  public long getNumPacketsRx()
  {
    return mNumPacketsRx;
  }
  
  public long getNumPacketsTx()
  {
    return mNumPacketsTx;
  }
  
  public long getRxTimeMs()
  {
    return mRxTimeMs;
  }
  
  public long getSleepTimeMs()
  {
    return mSleepTimeMs;
  }
  
  public long[] getTimeInRatMs()
  {
    return mTimeInRatMs;
  }
  
  public long[] getTimeInRxSignalStrengthLevelMs()
  {
    return mTimeInRxSignalStrengthLevelMs;
  }
  
  public long[] getTxTimeMs()
  {
    return mTxTimeMs;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    mLoggingDurationMs = paramParcel.readLong();
    mKernelActiveTimeMs = paramParcel.readLong();
    mNumPacketsTx = paramParcel.readLong();
    mNumBytesTx = paramParcel.readLong();
    mNumPacketsRx = paramParcel.readLong();
    mNumBytesRx = paramParcel.readLong();
    mSleepTimeMs = paramParcel.readLong();
    mIdleTimeMs = paramParcel.readLong();
    mRxTimeMs = paramParcel.readLong();
    mEnergyConsumedMaMs = paramParcel.readLong();
    paramParcel.readLongArray(mTimeInRatMs);
    paramParcel.readLongArray(mTimeInRxSignalStrengthLevelMs);
    paramParcel.readLongArray(mTxTimeMs);
  }
  
  public void setEnergyConsumedMaMs(long paramLong)
  {
    mEnergyConsumedMaMs = paramLong;
  }
  
  public void setIdleTimeMs(long paramLong)
  {
    mIdleTimeMs = paramLong;
  }
  
  public void setKernelActiveTimeMs(long paramLong)
  {
    mKernelActiveTimeMs = paramLong;
  }
  
  public void setLoggingDurationMs(long paramLong)
  {
    mLoggingDurationMs = paramLong;
  }
  
  public void setNumBytesRx(long paramLong)
  {
    mNumBytesRx = paramLong;
  }
  
  public void setNumBytesTx(long paramLong)
  {
    mNumBytesTx = paramLong;
  }
  
  public void setNumPacketsRx(long paramLong)
  {
    mNumPacketsRx = paramLong;
  }
  
  public void setNumPacketsTx(long paramLong)
  {
    mNumPacketsTx = paramLong;
  }
  
  public void setRxTimeMs(long paramLong)
  {
    mRxTimeMs = paramLong;
  }
  
  public void setSleepTimeMs(long paramLong)
  {
    mSleepTimeMs = paramLong;
  }
  
  public void setTimeInRatMs(long[] paramArrayOfLong)
  {
    mTimeInRatMs = Arrays.copyOfRange(paramArrayOfLong, 0, Math.min(paramArrayOfLong.length, 21));
  }
  
  public void setTimeInRxSignalStrengthLevelMs(long[] paramArrayOfLong)
  {
    mTimeInRxSignalStrengthLevelMs = Arrays.copyOfRange(paramArrayOfLong, 0, Math.min(paramArrayOfLong.length, 6));
  }
  
  public void setTxTimeMs(long[] paramArrayOfLong)
  {
    mTxTimeMs = Arrays.copyOfRange(paramArrayOfLong, 0, Math.min(paramArrayOfLong.length, 5));
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeLong(mLoggingDurationMs);
    paramParcel.writeLong(mKernelActiveTimeMs);
    paramParcel.writeLong(mNumPacketsTx);
    paramParcel.writeLong(mNumBytesTx);
    paramParcel.writeLong(mNumPacketsRx);
    paramParcel.writeLong(mNumBytesRx);
    paramParcel.writeLong(mSleepTimeMs);
    paramParcel.writeLong(mIdleTimeMs);
    paramParcel.writeLong(mRxTimeMs);
    paramParcel.writeLong(mEnergyConsumedMaMs);
    paramParcel.writeLongArray(mTimeInRatMs);
    paramParcel.writeLongArray(mTimeInRxSignalStrengthLevelMs);
    paramParcel.writeLongArray(mTxTimeMs);
  }
}
