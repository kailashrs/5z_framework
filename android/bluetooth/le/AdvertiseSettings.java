package android.bluetooth.le;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class AdvertiseSettings
  implements Parcelable
{
  public static final int ADVERTISE_MODE_BALANCED = 1;
  public static final int ADVERTISE_MODE_LOW_LATENCY = 2;
  public static final int ADVERTISE_MODE_LOW_POWER = 0;
  public static final int ADVERTISE_TX_POWER_HIGH = 3;
  public static final int ADVERTISE_TX_POWER_LOW = 1;
  public static final int ADVERTISE_TX_POWER_MEDIUM = 2;
  public static final int ADVERTISE_TX_POWER_ULTRA_LOW = 0;
  public static final Parcelable.Creator<AdvertiseSettings> CREATOR = new Parcelable.Creator()
  {
    public AdvertiseSettings createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AdvertiseSettings(paramAnonymousParcel, null);
    }
    
    public AdvertiseSettings[] newArray(int paramAnonymousInt)
    {
      return new AdvertiseSettings[paramAnonymousInt];
    }
  };
  private static final int LIMITED_ADVERTISING_MAX_MILLIS = 180000;
  private final boolean mAdvertiseConnectable;
  private final int mAdvertiseMode;
  private final int mAdvertiseTimeoutMillis;
  private final int mAdvertiseTxPowerLevel;
  
  private AdvertiseSettings(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    mAdvertiseMode = paramInt1;
    mAdvertiseTxPowerLevel = paramInt2;
    mAdvertiseConnectable = paramBoolean;
    mAdvertiseTimeoutMillis = paramInt3;
  }
  
  private AdvertiseSettings(Parcel paramParcel)
  {
    mAdvertiseMode = paramParcel.readInt();
    mAdvertiseTxPowerLevel = paramParcel.readInt();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mAdvertiseConnectable = bool;
    mAdvertiseTimeoutMillis = paramParcel.readInt();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getMode()
  {
    return mAdvertiseMode;
  }
  
  public int getTimeout()
  {
    return mAdvertiseTimeoutMillis;
  }
  
  public int getTxPowerLevel()
  {
    return mAdvertiseTxPowerLevel;
  }
  
  public boolean isConnectable()
  {
    return mAdvertiseConnectable;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Settings [mAdvertiseMode=");
    localStringBuilder.append(mAdvertiseMode);
    localStringBuilder.append(", mAdvertiseTxPowerLevel=");
    localStringBuilder.append(mAdvertiseTxPowerLevel);
    localStringBuilder.append(", mAdvertiseConnectable=");
    localStringBuilder.append(mAdvertiseConnectable);
    localStringBuilder.append(", mAdvertiseTimeoutMillis=");
    localStringBuilder.append(mAdvertiseTimeoutMillis);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mAdvertiseMode);
    paramParcel.writeInt(mAdvertiseTxPowerLevel);
    paramParcel.writeInt(mAdvertiseConnectable);
    paramParcel.writeInt(mAdvertiseTimeoutMillis);
  }
  
  public static final class Builder
  {
    private boolean mConnectable = true;
    private int mMode = 0;
    private int mTimeoutMillis = 0;
    private int mTxPowerLevel = 2;
    
    public Builder() {}
    
    public AdvertiseSettings build()
    {
      return new AdvertiseSettings(mMode, mTxPowerLevel, mConnectable, mTimeoutMillis, null);
    }
    
    public Builder setAdvertiseMode(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 2))
      {
        mMode = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown mode ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    
    public Builder setConnectable(boolean paramBoolean)
    {
      mConnectable = paramBoolean;
      return this;
    }
    
    public Builder setTimeout(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 180000))
      {
        mTimeoutMillis = paramInt;
        return this;
      }
      throw new IllegalArgumentException("timeoutMillis invalid (must be 0-180000 milliseconds)");
    }
    
    public Builder setTxPowerLevel(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt <= 3))
      {
        mTxPowerLevel = paramInt;
        return this;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown tx power level ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
  }
}
