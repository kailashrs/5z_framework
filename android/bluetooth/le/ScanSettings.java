package android.bluetooth.le;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ScanSettings
  implements Parcelable
{
  public static final int CALLBACK_TYPE_ALL_MATCHES = 1;
  public static final int CALLBACK_TYPE_FIRST_MATCH = 2;
  public static final int CALLBACK_TYPE_MATCH_LOST = 4;
  public static final int CALLBACK_TYPE_SENSOR_ROUTING = 8;
  public static final Parcelable.Creator<ScanSettings> CREATOR = new Parcelable.Creator()
  {
    public ScanSettings createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ScanSettings(paramAnonymousParcel, null);
    }
    
    public ScanSettings[] newArray(int paramAnonymousInt)
    {
      return new ScanSettings[paramAnonymousInt];
    }
  };
  public static final int MATCH_MODE_AGGRESSIVE = 1;
  public static final int MATCH_MODE_STICKY = 2;
  public static final int MATCH_NUM_FEW_ADVERTISEMENT = 2;
  public static final int MATCH_NUM_MAX_ADVERTISEMENT = 3;
  public static final int MATCH_NUM_ONE_ADVERTISEMENT = 1;
  public static final int PHY_LE_ALL_SUPPORTED = 255;
  public static final int SCAN_MODE_BALANCED = 1;
  public static final int SCAN_MODE_LOW_LATENCY = 2;
  public static final int SCAN_MODE_LOW_POWER = 0;
  public static final int SCAN_MODE_OPPORTUNISTIC = -1;
  @SystemApi
  public static final int SCAN_RESULT_TYPE_ABBREVIATED = 1;
  @SystemApi
  public static final int SCAN_RESULT_TYPE_FULL = 0;
  private int mCallbackType;
  private boolean mLegacy;
  private int mMatchMode;
  private int mNumOfMatchesPerFilter;
  private int mPhy;
  private long mReportDelayMillis;
  private int mScanMode;
  private int mScanResultType;
  
  private ScanSettings(int paramInt1, int paramInt2, int paramInt3, long paramLong, int paramInt4, int paramInt5, boolean paramBoolean, int paramInt6)
  {
    mScanMode = paramInt1;
    mCallbackType = paramInt2;
    mScanResultType = paramInt3;
    mReportDelayMillis = paramLong;
    mNumOfMatchesPerFilter = paramInt5;
    mMatchMode = paramInt4;
    mLegacy = paramBoolean;
    mPhy = paramInt6;
  }
  
  private ScanSettings(Parcel paramParcel)
  {
    mScanMode = paramParcel.readInt();
    mCallbackType = paramParcel.readInt();
    mScanResultType = paramParcel.readInt();
    mReportDelayMillis = paramParcel.readLong();
    mMatchMode = paramParcel.readInt();
    mNumOfMatchesPerFilter = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mLegacy = bool;
    mPhy = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getCallbackType()
  {
    return mCallbackType;
  }
  
  public boolean getLegacy()
  {
    return mLegacy;
  }
  
  public int getMatchMode()
  {
    return mMatchMode;
  }
  
  public int getNumOfMatches()
  {
    return mNumOfMatchesPerFilter;
  }
  
  public int getPhy()
  {
    return mPhy;
  }
  
  public long getReportDelayMillis()
  {
    return mReportDelayMillis;
  }
  
  public int getScanMode()
  {
    return mScanMode;
  }
  
  public int getScanResultType()
  {
    return mScanResultType;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mScanMode);
    paramParcel.writeInt(mCallbackType);
    paramParcel.writeInt(mScanResultType);
    paramParcel.writeLong(mReportDelayMillis);
    paramParcel.writeInt(mMatchMode);
    paramParcel.writeInt(mNumOfMatchesPerFilter);
    paramParcel.writeInt(mLegacy);
    paramParcel.writeInt(mPhy);
  }
  
  public static final class Builder
  {
    private int mCallbackType = 1;
    private boolean mLegacy = true;
    private int mMatchMode = 1;
    private int mNumOfMatchesPerFilter = 3;
    private int mPhy = 255;
    private long mReportDelayMillis = 0L;
    private int mScanMode = 0;
    private int mScanResultType = 0;
    
    public Builder() {}
    
    private boolean isValidCallbackType(int paramInt)
    {
      boolean bool = true;
      if ((paramInt != 1) && (paramInt != 2) && (paramInt != 4) && (paramInt != 8))
      {
        if (paramInt != 6) {
          bool = false;
        }
        return bool;
      }
      return true;
    }
    
    public ScanSettings build()
    {
      return new ScanSettings(mScanMode, mCallbackType, mScanResultType, mReportDelayMillis, mMatchMode, mNumOfMatchesPerFilter, mLegacy, mPhy, null);
    }
    
    public Builder setCallbackType(int paramInt)
    {
      if (isValidCallbackType(paramInt))
      {
        mCallbackType = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid callback type - ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setLegacy(boolean paramBoolean)
    {
      mLegacy = paramBoolean;
      return this;
    }
    
    public Builder setMatchMode(int paramInt)
    {
      if ((paramInt >= 1) && (paramInt <= 2))
      {
        mMatchMode = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid matchMode ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setNumOfMatches(int paramInt)
    {
      if ((paramInt >= 1) && (paramInt <= 3))
      {
        mNumOfMatchesPerFilter = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid numOfMatches ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setPhy(int paramInt)
    {
      mPhy = paramInt;
      return this;
    }
    
    public Builder setReportDelay(long paramLong)
    {
      if (paramLong >= 0L)
      {
        mReportDelayMillis = paramLong;
        return this;
      }
      throw new IllegalArgumentException("reportDelay must be > 0");
    }
    
    public Builder setScanMode(int paramInt)
    {
      if ((paramInt >= -1) && (paramInt <= 2))
      {
        mScanMode = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid scan mode ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    @SystemApi
    public Builder setScanResultType(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 1))
      {
        mScanResultType = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("invalid scanResultType - ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
}
