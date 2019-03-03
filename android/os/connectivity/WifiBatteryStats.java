package android.os.connectivity;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class WifiBatteryStats
  implements Parcelable
{
  public static final Parcelable.Creator<WifiBatteryStats> CREATOR = new Parcelable.Creator()
  {
    public WifiBatteryStats createFromParcel(Parcel paramAnonymousParcel)
    {
      return new WifiBatteryStats(paramAnonymousParcel, null);
    }
    
    public WifiBatteryStats[] newArray(int paramAnonymousInt)
    {
      return new WifiBatteryStats[paramAnonymousInt];
    }
  };
  private long mEnergyConsumedMaMs;
  private long mIdleTimeMs;
  private long mKernelActiveTimeMs;
  private long mLoggingDurationMs;
  private long mNumAppScanRequest;
  private long mNumBytesRx;
  private long mNumBytesTx;
  private long mNumPacketsRx;
  private long mNumPacketsTx;
  private long mRxTimeMs;
  private long mScanTimeMs;
  private long mSleepTimeMs;
  private long[] mTimeInRxSignalStrengthLevelMs;
  private long[] mTimeInStateMs;
  private long[] mTimeInSupplicantStateMs;
  private long mTxTimeMs;
  
  public WifiBatteryStats()
  {
    initialize();
  }
  
  private WifiBatteryStats(Parcel paramParcel)
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
    mScanTimeMs = 0L;
    mIdleTimeMs = 0L;
    mRxTimeMs = 0L;
    mTxTimeMs = 0L;
    mEnergyConsumedMaMs = 0L;
    mNumAppScanRequest = 0L;
    mTimeInStateMs = new long[8];
    Arrays.fill(mTimeInStateMs, 0L);
    mTimeInRxSignalStrengthLevelMs = new long[5];
    Arrays.fill(mTimeInRxSignalStrengthLevelMs, 0L);
    mTimeInSupplicantStateMs = new long[13];
    Arrays.fill(mTimeInSupplicantStateMs, 0L);
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
  
  public long getNumAppScanRequest()
  {
    return mNumAppScanRequest;
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
  
  public long getScanTimeMs()
  {
    return mScanTimeMs;
  }
  
  public long getSleepTimeMs()
  {
    return mSleepTimeMs;
  }
  
  public long[] getTimeInRxSignalStrengthLevelMs()
  {
    return mTimeInRxSignalStrengthLevelMs;
  }
  
  public long[] getTimeInStateMs()
  {
    return mTimeInStateMs;
  }
  
  public long[] getTimeInSupplicantStateMs()
  {
    return mTimeInSupplicantStateMs;
  }
  
  public long getTxTimeMs()
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
    mScanTimeMs = paramParcel.readLong();
    mIdleTimeMs = paramParcel.readLong();
    mRxTimeMs = paramParcel.readLong();
    mTxTimeMs = paramParcel.readLong();
    mEnergyConsumedMaMs = paramParcel.readLong();
    mNumAppScanRequest = paramParcel.readLong();
    paramParcel.readLongArray(mTimeInStateMs);
    paramParcel.readLongArray(mTimeInRxSignalStrengthLevelMs);
    paramParcel.readLongArray(mTimeInSupplicantStateMs);
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
  
  public void setNumAppScanRequest(long paramLong)
  {
    mNumAppScanRequest = paramLong;
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
  
  public void setScanTimeMs(long paramLong)
  {
    mScanTimeMs = paramLong;
  }
  
  public void setSleepTimeMs(long paramLong)
  {
    mSleepTimeMs = paramLong;
  }
  
  public void setTimeInRxSignalStrengthLevelMs(long[] paramArrayOfLong)
  {
    mTimeInRxSignalStrengthLevelMs = Arrays.copyOfRange(paramArrayOfLong, 0, Math.min(paramArrayOfLong.length, 5));
  }
  
  public void setTimeInStateMs(long[] paramArrayOfLong)
  {
    mTimeInStateMs = Arrays.copyOfRange(paramArrayOfLong, 0, Math.min(paramArrayOfLong.length, 8));
  }
  
  public void setTimeInSupplicantStateMs(long[] paramArrayOfLong)
  {
    mTimeInSupplicantStateMs = Arrays.copyOfRange(paramArrayOfLong, 0, Math.min(paramArrayOfLong.length, 13));
  }
  
  public void setTxTimeMs(long paramLong)
  {
    mTxTimeMs = paramLong;
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
    paramParcel.writeLong(mScanTimeMs);
    paramParcel.writeLong(mIdleTimeMs);
    paramParcel.writeLong(mRxTimeMs);
    paramParcel.writeLong(mTxTimeMs);
    paramParcel.writeLong(mEnergyConsumedMaMs);
    paramParcel.writeLong(mNumAppScanRequest);
    paramParcel.writeLongArray(mTimeInStateMs);
    paramParcel.writeLongArray(mTimeInRxSignalStrengthLevelMs);
    paramParcel.writeLongArray(mTimeInSupplicantStateMs);
  }
}
