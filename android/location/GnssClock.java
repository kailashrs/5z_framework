package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class GnssClock
  implements Parcelable
{
  public static final Parcelable.Creator<GnssClock> CREATOR = new Parcelable.Creator()
  {
    public GnssClock createFromParcel(Parcel paramAnonymousParcel)
    {
      GnssClock localGnssClock = new GnssClock();
      GnssClock.access$002(localGnssClock, paramAnonymousParcel.readInt());
      GnssClock.access$102(localGnssClock, paramAnonymousParcel.readInt());
      GnssClock.access$202(localGnssClock, paramAnonymousParcel.readLong());
      GnssClock.access$302(localGnssClock, paramAnonymousParcel.readDouble());
      GnssClock.access$402(localGnssClock, paramAnonymousParcel.readLong());
      GnssClock.access$502(localGnssClock, paramAnonymousParcel.readDouble());
      GnssClock.access$602(localGnssClock, paramAnonymousParcel.readDouble());
      GnssClock.access$702(localGnssClock, paramAnonymousParcel.readDouble());
      GnssClock.access$802(localGnssClock, paramAnonymousParcel.readDouble());
      GnssClock.access$902(localGnssClock, paramAnonymousParcel.readInt());
      return localGnssClock;
    }
    
    public GnssClock[] newArray(int paramAnonymousInt)
    {
      return new GnssClock[paramAnonymousInt];
    }
  };
  private static final int HAS_BIAS = 8;
  private static final int HAS_BIAS_UNCERTAINTY = 16;
  private static final int HAS_DRIFT = 32;
  private static final int HAS_DRIFT_UNCERTAINTY = 64;
  private static final int HAS_FULL_BIAS = 4;
  private static final int HAS_LEAP_SECOND = 1;
  private static final int HAS_NO_FLAGS = 0;
  private static final int HAS_TIME_UNCERTAINTY = 2;
  private double mBiasNanos;
  private double mBiasUncertaintyNanos;
  private double mDriftNanosPerSecond;
  private double mDriftUncertaintyNanosPerSecond;
  private int mFlags;
  private long mFullBiasNanos;
  private int mHardwareClockDiscontinuityCount;
  private int mLeapSecond;
  private long mTimeNanos;
  private double mTimeUncertaintyNanos;
  
  public GnssClock()
  {
    initialize();
  }
  
  private void initialize()
  {
    mFlags = 0;
    resetLeapSecond();
    setTimeNanos(Long.MIN_VALUE);
    resetTimeUncertaintyNanos();
    resetFullBiasNanos();
    resetBiasNanos();
    resetBiasUncertaintyNanos();
    resetDriftNanosPerSecond();
    resetDriftUncertaintyNanosPerSecond();
    setHardwareClockDiscontinuityCount(Integer.MIN_VALUE);
  }
  
  private boolean isFlagSet(int paramInt)
  {
    boolean bool;
    if ((mFlags & paramInt) == paramInt) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void resetFlag(int paramInt)
  {
    mFlags &= paramInt;
  }
  
  private void setFlag(int paramInt)
  {
    mFlags |= paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public double getBiasNanos()
  {
    return mBiasNanos;
  }
  
  public double getBiasUncertaintyNanos()
  {
    return mBiasUncertaintyNanos;
  }
  
  public double getDriftNanosPerSecond()
  {
    return mDriftNanosPerSecond;
  }
  
  public double getDriftUncertaintyNanosPerSecond()
  {
    return mDriftUncertaintyNanosPerSecond;
  }
  
  public long getFullBiasNanos()
  {
    return mFullBiasNanos;
  }
  
  public int getHardwareClockDiscontinuityCount()
  {
    return mHardwareClockDiscontinuityCount;
  }
  
  public int getLeapSecond()
  {
    return mLeapSecond;
  }
  
  public long getTimeNanos()
  {
    return mTimeNanos;
  }
  
  public double getTimeUncertaintyNanos()
  {
    return mTimeUncertaintyNanos;
  }
  
  public boolean hasBiasNanos()
  {
    return isFlagSet(8);
  }
  
  public boolean hasBiasUncertaintyNanos()
  {
    return isFlagSet(16);
  }
  
  public boolean hasDriftNanosPerSecond()
  {
    return isFlagSet(32);
  }
  
  public boolean hasDriftUncertaintyNanosPerSecond()
  {
    return isFlagSet(64);
  }
  
  public boolean hasFullBiasNanos()
  {
    return isFlagSet(4);
  }
  
  public boolean hasLeapSecond()
  {
    return isFlagSet(1);
  }
  
  public boolean hasTimeUncertaintyNanos()
  {
    return isFlagSet(2);
  }
  
  public void reset()
  {
    initialize();
  }
  
  public void resetBiasNanos()
  {
    resetFlag(8);
    mBiasNanos = NaN.0D;
  }
  
  public void resetBiasUncertaintyNanos()
  {
    resetFlag(16);
    mBiasUncertaintyNanos = NaN.0D;
  }
  
  public void resetDriftNanosPerSecond()
  {
    resetFlag(32);
    mDriftNanosPerSecond = NaN.0D;
  }
  
  public void resetDriftUncertaintyNanosPerSecond()
  {
    resetFlag(64);
    mDriftUncertaintyNanosPerSecond = NaN.0D;
  }
  
  public void resetFullBiasNanos()
  {
    resetFlag(4);
    mFullBiasNanos = Long.MIN_VALUE;
  }
  
  public void resetLeapSecond()
  {
    resetFlag(1);
    mLeapSecond = Integer.MIN_VALUE;
  }
  
  public void resetTimeUncertaintyNanos()
  {
    resetFlag(2);
    mTimeUncertaintyNanos = NaN.0D;
  }
  
  public void set(GnssClock paramGnssClock)
  {
    mFlags = mFlags;
    mLeapSecond = mLeapSecond;
    mTimeNanos = mTimeNanos;
    mTimeUncertaintyNanos = mTimeUncertaintyNanos;
    mFullBiasNanos = mFullBiasNanos;
    mBiasNanos = mBiasNanos;
    mBiasUncertaintyNanos = mBiasUncertaintyNanos;
    mDriftNanosPerSecond = mDriftNanosPerSecond;
    mDriftUncertaintyNanosPerSecond = mDriftUncertaintyNanosPerSecond;
    mHardwareClockDiscontinuityCount = mHardwareClockDiscontinuityCount;
  }
  
  public void setBiasNanos(double paramDouble)
  {
    setFlag(8);
    mBiasNanos = paramDouble;
  }
  
  public void setBiasUncertaintyNanos(double paramDouble)
  {
    setFlag(16);
    mBiasUncertaintyNanos = paramDouble;
  }
  
  public void setDriftNanosPerSecond(double paramDouble)
  {
    setFlag(32);
    mDriftNanosPerSecond = paramDouble;
  }
  
  public void setDriftUncertaintyNanosPerSecond(double paramDouble)
  {
    setFlag(64);
    mDriftUncertaintyNanosPerSecond = paramDouble;
  }
  
  public void setFullBiasNanos(long paramLong)
  {
    setFlag(4);
    mFullBiasNanos = paramLong;
  }
  
  public void setHardwareClockDiscontinuityCount(int paramInt)
  {
    mHardwareClockDiscontinuityCount = paramInt;
  }
  
  public void setLeapSecond(int paramInt)
  {
    setFlag(1);
    mLeapSecond = paramInt;
  }
  
  public void setTimeNanos(long paramLong)
  {
    mTimeNanos = paramLong;
  }
  
  public void setTimeUncertaintyNanos(double paramDouble)
  {
    setFlag(2);
    mTimeUncertaintyNanos = paramDouble;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("GnssClock:\n");
    boolean bool = hasLeapSecond();
    Object localObject1 = null;
    Object localObject2;
    if (bool) {
      localObject2 = Integer.valueOf(mLeapSecond);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "LeapSecond", localObject2 }));
    long l = mTimeNanos;
    if (hasTimeUncertaintyNanos()) {
      localObject2 = Double.valueOf(mTimeUncertaintyNanos);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-15s = %-25s   %-26s = %s\n", new Object[] { "TimeNanos", Long.valueOf(l), "TimeUncertaintyNanos", localObject2 }));
    if (hasFullBiasNanos()) {
      localObject2 = Long.valueOf(mFullBiasNanos);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "FullBiasNanos", localObject2 }));
    if (hasBiasNanos()) {
      localObject2 = Double.valueOf(mBiasNanos);
    } else {
      localObject2 = null;
    }
    if (hasBiasUncertaintyNanos()) {
      localObject3 = Double.valueOf(mBiasUncertaintyNanos);
    } else {
      localObject3 = null;
    }
    localStringBuilder.append(String.format("   %-15s = %-25s   %-26s = %s\n", new Object[] { "BiasNanos", localObject2, "BiasUncertaintyNanos", localObject3 }));
    if (hasDriftNanosPerSecond()) {
      localObject2 = Double.valueOf(mDriftNanosPerSecond);
    } else {
      localObject2 = null;
    }
    Object localObject3 = localObject1;
    if (hasDriftUncertaintyNanosPerSecond()) {
      localObject3 = Double.valueOf(mDriftUncertaintyNanosPerSecond);
    }
    localStringBuilder.append(String.format("   %-15s = %-25s   %-26s = %s\n", new Object[] { "DriftNanosPerSecond", localObject2, "DriftUncertaintyNanosPerSecond", localObject3 }));
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "HardwareClockDiscontinuityCount", Integer.valueOf(mHardwareClockDiscontinuityCount) }));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mFlags);
    paramParcel.writeInt(mLeapSecond);
    paramParcel.writeLong(mTimeNanos);
    paramParcel.writeDouble(mTimeUncertaintyNanos);
    paramParcel.writeLong(mFullBiasNanos);
    paramParcel.writeDouble(mBiasNanos);
    paramParcel.writeDouble(mBiasUncertaintyNanos);
    paramParcel.writeDouble(mDriftNanosPerSecond);
    paramParcel.writeDouble(mDriftUncertaintyNanosPerSecond);
    paramParcel.writeInt(mHardwareClockDiscontinuityCount);
  }
}
