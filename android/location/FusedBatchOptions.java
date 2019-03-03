package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class FusedBatchOptions
  implements Parcelable
{
  public static final Parcelable.Creator<FusedBatchOptions> CREATOR = new Parcelable.Creator()
  {
    public FusedBatchOptions createFromParcel(Parcel paramAnonymousParcel)
    {
      FusedBatchOptions localFusedBatchOptions = new FusedBatchOptions();
      localFusedBatchOptions.setMaxPowerAllocationInMW(paramAnonymousParcel.readDouble());
      localFusedBatchOptions.setPeriodInNS(paramAnonymousParcel.readLong());
      localFusedBatchOptions.setSourceToUse(paramAnonymousParcel.readInt());
      localFusedBatchOptions.setFlag(paramAnonymousParcel.readInt());
      localFusedBatchOptions.setSmallestDisplacementMeters(paramAnonymousParcel.readFloat());
      return localFusedBatchOptions;
    }
    
    public FusedBatchOptions[] newArray(int paramAnonymousInt)
    {
      return new FusedBatchOptions[paramAnonymousInt];
    }
  };
  private volatile int mFlags = 0;
  private volatile double mMaxPowerAllocationInMW = 0.0D;
  private volatile long mPeriodInNS = 0L;
  private volatile float mSmallestDisplacementMeters = 0.0F;
  private volatile int mSourcesToUse = 0;
  
  public FusedBatchOptions() {}
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public double getMaxPowerAllocationInMW()
  {
    return mMaxPowerAllocationInMW;
  }
  
  public long getPeriodInNS()
  {
    return mPeriodInNS;
  }
  
  public float getSmallestDisplacementMeters()
  {
    return mSmallestDisplacementMeters;
  }
  
  public int getSourcesToUse()
  {
    return mSourcesToUse;
  }
  
  public boolean isFlagSet(int paramInt)
  {
    boolean bool;
    if ((mFlags & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSourceToUseSet(int paramInt)
  {
    boolean bool;
    if ((mSourcesToUse & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void resetFlag(int paramInt)
  {
    mFlags &= paramInt;
  }
  
  public void resetSourceToUse(int paramInt)
  {
    mSourcesToUse &= paramInt;
  }
  
  public void setFlag(int paramInt)
  {
    mFlags |= paramInt;
  }
  
  public void setMaxPowerAllocationInMW(double paramDouble)
  {
    mMaxPowerAllocationInMW = paramDouble;
  }
  
  public void setPeriodInNS(long paramLong)
  {
    mPeriodInNS = paramLong;
  }
  
  public void setSmallestDisplacementMeters(float paramFloat)
  {
    mSmallestDisplacementMeters = paramFloat;
  }
  
  public void setSourceToUse(int paramInt)
  {
    mSourcesToUse |= paramInt;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeDouble(mMaxPowerAllocationInMW);
    paramParcel.writeLong(mPeriodInNS);
    paramParcel.writeInt(mSourcesToUse);
    paramParcel.writeInt(mFlags);
    paramParcel.writeFloat(mSmallestDisplacementMeters);
  }
  
  public static final class BatchFlags
  {
    public static int CALLBACK_ON_LOCATION_FIX = 2;
    public static int WAKEUP_ON_FIFO_FULL = 1;
    
    public BatchFlags() {}
  }
  
  public static final class SourceTechnologies
  {
    public static int BLUETOOTH = 16;
    public static int CELL;
    public static int GNSS = 1;
    public static int SENSORS;
    public static int WIFI = 2;
    
    static
    {
      SENSORS = 4;
      CELL = 8;
    }
    
    public SourceTechnologies() {}
  }
}
