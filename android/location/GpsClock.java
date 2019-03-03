package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public class GpsClock
  implements Parcelable
{
  public static final Parcelable.Creator<GpsClock> CREATOR = new Parcelable.Creator()
  {
    public GpsClock createFromParcel(Parcel paramAnonymousParcel)
    {
      GpsClock localGpsClock = new GpsClock();
      GpsClock.access$002(localGpsClock, (short)paramAnonymousParcel.readInt());
      GpsClock.access$102(localGpsClock, (short)paramAnonymousParcel.readInt());
      GpsClock.access$202(localGpsClock, paramAnonymousParcel.readByte());
      GpsClock.access$302(localGpsClock, paramAnonymousParcel.readLong());
      GpsClock.access$402(localGpsClock, paramAnonymousParcel.readDouble());
      GpsClock.access$502(localGpsClock, paramAnonymousParcel.readLong());
      GpsClock.access$602(localGpsClock, paramAnonymousParcel.readDouble());
      GpsClock.access$702(localGpsClock, paramAnonymousParcel.readDouble());
      GpsClock.access$802(localGpsClock, paramAnonymousParcel.readDouble());
      GpsClock.access$902(localGpsClock, paramAnonymousParcel.readDouble());
      return localGpsClock;
    }
    
    public GpsClock[] newArray(int paramAnonymousInt)
    {
      return new GpsClock[paramAnonymousInt];
    }
  };
  private static final short HAS_BIAS = 8;
  private static final short HAS_BIAS_UNCERTAINTY = 16;
  private static final short HAS_DRIFT = 32;
  private static final short HAS_DRIFT_UNCERTAINTY = 64;
  private static final short HAS_FULL_BIAS = 4;
  private static final short HAS_LEAP_SECOND = 1;
  private static final short HAS_NO_FLAGS = 0;
  private static final short HAS_TIME_UNCERTAINTY = 2;
  public static final byte TYPE_GPS_TIME = 2;
  public static final byte TYPE_LOCAL_HW_TIME = 1;
  public static final byte TYPE_UNKNOWN = 0;
  private double mBiasInNs;
  private double mBiasUncertaintyInNs;
  private double mDriftInNsPerSec;
  private double mDriftUncertaintyInNsPerSec;
  private short mFlags;
  private long mFullBiasInNs;
  private short mLeapSecond;
  private long mTimeInNs;
  private double mTimeUncertaintyInNs;
  private byte mType;
  
  GpsClock()
  {
    initialize();
  }
  
  private String getTypeString()
  {
    switch (mType)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("<Invalid:");
      localStringBuilder.append(mType);
      localStringBuilder.append(">");
      return localStringBuilder.toString();
    case 2: 
      return "GpsTime";
    case 1: 
      return "LocalHwClock";
    }
    return "Unknown";
  }
  
  private void initialize()
  {
    mFlags = ((short)0);
    resetLeapSecond();
    setType((byte)0);
    setTimeInNs(Long.MIN_VALUE);
    resetTimeUncertaintyInNs();
    resetFullBiasInNs();
    resetBiasInNs();
    resetBiasUncertaintyInNs();
    resetDriftInNsPerSec();
    resetDriftUncertaintyInNsPerSec();
  }
  
  private boolean isFlagSet(short paramShort)
  {
    boolean bool;
    if ((mFlags & paramShort) == paramShort) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void resetFlag(short paramShort)
  {
    mFlags = ((short)(short)(mFlags & paramShort));
  }
  
  private void setFlag(short paramShort)
  {
    mFlags = ((short)(short)(mFlags | paramShort));
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public double getBiasInNs()
  {
    return mBiasInNs;
  }
  
  public double getBiasUncertaintyInNs()
  {
    return mBiasUncertaintyInNs;
  }
  
  public double getDriftInNsPerSec()
  {
    return mDriftInNsPerSec;
  }
  
  public double getDriftUncertaintyInNsPerSec()
  {
    return mDriftUncertaintyInNsPerSec;
  }
  
  public long getFullBiasInNs()
  {
    return mFullBiasInNs;
  }
  
  public short getLeapSecond()
  {
    return mLeapSecond;
  }
  
  public long getTimeInNs()
  {
    return mTimeInNs;
  }
  
  public double getTimeUncertaintyInNs()
  {
    return mTimeUncertaintyInNs;
  }
  
  public byte getType()
  {
    return mType;
  }
  
  public boolean hasBiasInNs()
  {
    return isFlagSet((short)8);
  }
  
  public boolean hasBiasUncertaintyInNs()
  {
    return isFlagSet((short)16);
  }
  
  public boolean hasDriftInNsPerSec()
  {
    return isFlagSet((short)32);
  }
  
  public boolean hasDriftUncertaintyInNsPerSec()
  {
    return isFlagSet((short)64);
  }
  
  public boolean hasFullBiasInNs()
  {
    return isFlagSet((short)4);
  }
  
  public boolean hasLeapSecond()
  {
    return isFlagSet((short)1);
  }
  
  public boolean hasTimeUncertaintyInNs()
  {
    return isFlagSet((short)2);
  }
  
  public void reset()
  {
    initialize();
  }
  
  public void resetBiasInNs()
  {
    resetFlag((short)8);
    mBiasInNs = NaN.0D;
  }
  
  public void resetBiasUncertaintyInNs()
  {
    resetFlag((short)16);
    mBiasUncertaintyInNs = NaN.0D;
  }
  
  public void resetDriftInNsPerSec()
  {
    resetFlag((short)32);
    mDriftInNsPerSec = NaN.0D;
  }
  
  public void resetDriftUncertaintyInNsPerSec()
  {
    resetFlag((short)64);
    mDriftUncertaintyInNsPerSec = NaN.0D;
  }
  
  public void resetFullBiasInNs()
  {
    resetFlag((short)4);
    mFullBiasInNs = Long.MIN_VALUE;
  }
  
  public void resetLeapSecond()
  {
    resetFlag((short)1);
    mLeapSecond = ((short)'耀');
  }
  
  public void resetTimeUncertaintyInNs()
  {
    resetFlag((short)2);
    mTimeUncertaintyInNs = NaN.0D;
  }
  
  public void set(GpsClock paramGpsClock)
  {
    mFlags = ((short)mFlags);
    mLeapSecond = ((short)mLeapSecond);
    mType = ((byte)mType);
    mTimeInNs = mTimeInNs;
    mTimeUncertaintyInNs = mTimeUncertaintyInNs;
    mFullBiasInNs = mFullBiasInNs;
    mBiasInNs = mBiasInNs;
    mBiasUncertaintyInNs = mBiasUncertaintyInNs;
    mDriftInNsPerSec = mDriftInNsPerSec;
    mDriftUncertaintyInNsPerSec = mDriftUncertaintyInNsPerSec;
  }
  
  public void setBiasInNs(double paramDouble)
  {
    setFlag((short)8);
    mBiasInNs = paramDouble;
  }
  
  public void setBiasUncertaintyInNs(double paramDouble)
  {
    setFlag((short)16);
    mBiasUncertaintyInNs = paramDouble;
  }
  
  public void setDriftInNsPerSec(double paramDouble)
  {
    setFlag((short)32);
    mDriftInNsPerSec = paramDouble;
  }
  
  public void setDriftUncertaintyInNsPerSec(double paramDouble)
  {
    setFlag((short)64);
    mDriftUncertaintyInNsPerSec = paramDouble;
  }
  
  public void setFullBiasInNs(long paramLong)
  {
    setFlag((short)4);
    mFullBiasInNs = paramLong;
  }
  
  public void setLeapSecond(short paramShort)
  {
    setFlag((short)1);
    mLeapSecond = ((short)paramShort);
  }
  
  public void setTimeInNs(long paramLong)
  {
    mTimeInNs = paramLong;
  }
  
  public void setTimeUncertaintyInNs(double paramDouble)
  {
    setFlag((short)2);
    mTimeUncertaintyInNs = paramDouble;
  }
  
  public void setType(byte paramByte)
  {
    mType = ((byte)paramByte);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder("GpsClock:\n");
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "Type", getTypeString() }));
    boolean bool = hasLeapSecond();
    Object localObject1 = null;
    Object localObject2;
    if (bool) {
      localObject2 = Short.valueOf(mLeapSecond);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "LeapSecond", localObject2 }));
    long l = mTimeInNs;
    if (hasTimeUncertaintyInNs()) {
      localObject2 = Double.valueOf(mTimeUncertaintyInNs);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-15s = %-25s   %-26s = %s\n", new Object[] { "TimeInNs", Long.valueOf(l), "TimeUncertaintyInNs", localObject2 }));
    if (hasFullBiasInNs()) {
      localObject2 = Long.valueOf(mFullBiasInNs);
    } else {
      localObject2 = null;
    }
    localStringBuilder.append(String.format("   %-15s = %s\n", new Object[] { "FullBiasInNs", localObject2 }));
    if (hasBiasInNs()) {
      localObject2 = Double.valueOf(mBiasInNs);
    } else {
      localObject2 = null;
    }
    if (hasBiasUncertaintyInNs()) {
      localObject3 = Double.valueOf(mBiasUncertaintyInNs);
    } else {
      localObject3 = null;
    }
    localStringBuilder.append(String.format("   %-15s = %-25s   %-26s = %s\n", new Object[] { "BiasInNs", localObject2, "BiasUncertaintyInNs", localObject3 }));
    if (hasDriftInNsPerSec()) {
      localObject2 = Double.valueOf(mDriftInNsPerSec);
    } else {
      localObject2 = null;
    }
    Object localObject3 = localObject1;
    if (hasDriftUncertaintyInNsPerSec()) {
      localObject3 = Double.valueOf(mDriftUncertaintyInNsPerSec);
    }
    localStringBuilder.append(String.format("   %-15s = %-25s   %-26s = %s\n", new Object[] { "DriftInNsPerSec", localObject2, "DriftUncertaintyInNsPerSec", localObject3 }));
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mFlags);
    paramParcel.writeInt(mLeapSecond);
    paramParcel.writeByte(mType);
    paramParcel.writeLong(mTimeInNs);
    paramParcel.writeDouble(mTimeUncertaintyInNs);
    paramParcel.writeLong(mFullBiasInNs);
    paramParcel.writeDouble(mBiasInNs);
    paramParcel.writeDouble(mBiasUncertaintyInNs);
    paramParcel.writeDouble(mDriftInNsPerSec);
    paramParcel.writeDouble(mDriftUncertaintyInNsPerSec);
  }
}
