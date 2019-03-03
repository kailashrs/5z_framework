package android.bluetooth.le;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class AdvertisingSetParameters
  implements Parcelable
{
  public static final Parcelable.Creator<AdvertisingSetParameters> CREATOR = new Parcelable.Creator()
  {
    public AdvertisingSetParameters createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AdvertisingSetParameters(paramAnonymousParcel, null);
    }
    
    public AdvertisingSetParameters[] newArray(int paramAnonymousInt)
    {
      return new AdvertisingSetParameters[paramAnonymousInt];
    }
  };
  public static final int INTERVAL_HIGH = 1600;
  public static final int INTERVAL_LOW = 160;
  public static final int INTERVAL_MAX = 16777215;
  public static final int INTERVAL_MEDIUM = 400;
  public static final int INTERVAL_MIN = 160;
  private static final int LIMITED_ADVERTISING_MAX_MILLIS = 180000;
  public static final int TX_POWER_HIGH = 1;
  public static final int TX_POWER_LOW = -15;
  public static final int TX_POWER_MAX = 1;
  public static final int TX_POWER_MEDIUM = -7;
  public static final int TX_POWER_MIN = -127;
  public static final int TX_POWER_ULTRA_LOW = -21;
  private final boolean mConnectable;
  private final boolean mIncludeTxPower;
  private final int mInterval;
  private final boolean mIsAnonymous;
  private final boolean mIsLegacy;
  private final int mPrimaryPhy;
  private final boolean mScannable;
  private final int mSecondaryPhy;
  private final int mTxPowerLevel;
  
  private AdvertisingSetParameters(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    boolean bool1 = false;
    if (i != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mConnectable = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mScannable = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsLegacy = bool2;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    mIsAnonymous = bool2;
    boolean bool2 = bool1;
    if (paramParcel.readInt() != 0) {
      bool2 = true;
    }
    mIncludeTxPower = bool2;
    mPrimaryPhy = paramParcel.readInt();
    mSecondaryPhy = paramParcel.readInt();
    mInterval = paramParcel.readInt();
    mTxPowerLevel = paramParcel.readInt();
  }
  
  private AdvertisingSetParameters(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mConnectable = paramBoolean1;
    mScannable = paramBoolean2;
    mIsLegacy = paramBoolean3;
    mIsAnonymous = paramBoolean4;
    mIncludeTxPower = paramBoolean5;
    mPrimaryPhy = paramInt1;
    mSecondaryPhy = paramInt2;
    mInterval = paramInt3;
    mTxPowerLevel = paramInt4;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getInterval()
  {
    return mInterval;
  }
  
  public int getPrimaryPhy()
  {
    return mPrimaryPhy;
  }
  
  public int getSecondaryPhy()
  {
    return mSecondaryPhy;
  }
  
  public int getTxPowerLevel()
  {
    return mTxPowerLevel;
  }
  
  public boolean includeTxPower()
  {
    return mIncludeTxPower;
  }
  
  public boolean isAnonymous()
  {
    return mIsAnonymous;
  }
  
  public boolean isConnectable()
  {
    return mConnectable;
  }
  
  public boolean isLegacy()
  {
    return mIsLegacy;
  }
  
  public boolean isScannable()
  {
    return mScannable;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("AdvertisingSetParameters [connectable=");
    localStringBuilder.append(mConnectable);
    localStringBuilder.append(", isLegacy=");
    localStringBuilder.append(mIsLegacy);
    localStringBuilder.append(", isAnonymous=");
    localStringBuilder.append(mIsAnonymous);
    localStringBuilder.append(", includeTxPower=");
    localStringBuilder.append(mIncludeTxPower);
    localStringBuilder.append(", primaryPhy=");
    localStringBuilder.append(mPrimaryPhy);
    localStringBuilder.append(", secondaryPhy=");
    localStringBuilder.append(mSecondaryPhy);
    localStringBuilder.append(", interval=");
    localStringBuilder.append(mInterval);
    localStringBuilder.append(", txPowerLevel=");
    localStringBuilder.append(mTxPowerLevel);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mConnectable);
    paramParcel.writeInt(mScannable);
    paramParcel.writeInt(mIsLegacy);
    paramParcel.writeInt(mIsAnonymous);
    paramParcel.writeInt(mIncludeTxPower);
    paramParcel.writeInt(mPrimaryPhy);
    paramParcel.writeInt(mSecondaryPhy);
    paramParcel.writeInt(mInterval);
    paramParcel.writeInt(mTxPowerLevel);
  }
  
  public static final class Builder
  {
    private boolean mConnectable = false;
    private boolean mIncludeTxPower = false;
    private int mInterval = 160;
    private boolean mIsAnonymous = false;
    private boolean mIsLegacy = false;
    private int mPrimaryPhy = 1;
    private boolean mScannable = false;
    private int mSecondaryPhy = 1;
    private int mTxPowerLevel = -7;
    
    public Builder() {}
    
    public AdvertisingSetParameters build()
    {
      if (mIsLegacy)
      {
        if (!mIsAnonymous)
        {
          if ((mConnectable) && (!mScannable)) {
            throw new IllegalStateException("Legacy advertisement can't be connectable and non-scannable");
          }
          if (mIncludeTxPower) {
            throw new IllegalStateException("Legacy advertising can't include TX power level in header");
          }
        }
        else
        {
          throw new IllegalArgumentException("Legacy advertising can't be anonymous");
        }
      }
      else
      {
        if ((mConnectable) && (mScannable)) {
          throw new IllegalStateException("Advertising can't be both connectable and scannable");
        }
        if ((mIsAnonymous) && (mConnectable)) {
          throw new IllegalStateException("Advertising can't be both connectable and anonymous");
        }
      }
      return new AdvertisingSetParameters(mConnectable, mScannable, mIsLegacy, mIsAnonymous, mIncludeTxPower, mPrimaryPhy, mSecondaryPhy, mInterval, mTxPowerLevel, null);
    }
    
    public Builder setAnonymous(boolean paramBoolean)
    {
      mIsAnonymous = paramBoolean;
      return this;
    }
    
    public Builder setConnectable(boolean paramBoolean)
    {
      mConnectable = paramBoolean;
      return this;
    }
    
    public Builder setIncludeTxPower(boolean paramBoolean)
    {
      mIncludeTxPower = paramBoolean;
      return this;
    }
    
    public Builder setInterval(int paramInt)
    {
      if ((paramInt >= 160) && (paramInt <= 16777215))
      {
        mInterval = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown interval ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setLegacyMode(boolean paramBoolean)
    {
      mIsLegacy = paramBoolean;
      return this;
    }
    
    public Builder setPrimaryPhy(int paramInt)
    {
      if ((paramInt != 1) && (paramInt != 3))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("bad primaryPhy ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mPrimaryPhy = paramInt;
      return this;
    }
    
    public Builder setScannable(boolean paramBoolean)
    {
      mScannable = paramBoolean;
      return this;
    }
    
    public Builder setSecondaryPhy(int paramInt)
    {
      if ((paramInt != 1) && (paramInt != 2) && (paramInt != 3))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("bad secondaryPhy ");
        localStringBuilder.append(paramInt);
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
      mSecondaryPhy = paramInt;
      return this;
    }
    
    public Builder setTxPowerLevel(int paramInt)
    {
      if ((paramInt >= -127) && (paramInt <= 1))
      {
        mTxPowerLevel = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown txPowerLevel ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
}
