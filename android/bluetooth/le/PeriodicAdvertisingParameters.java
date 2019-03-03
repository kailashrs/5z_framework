package android.bluetooth.le;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class PeriodicAdvertisingParameters
  implements Parcelable
{
  public static final Parcelable.Creator<PeriodicAdvertisingParameters> CREATOR = new Parcelable.Creator()
  {
    public PeriodicAdvertisingParameters createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PeriodicAdvertisingParameters(paramAnonymousParcel, null);
    }
    
    public PeriodicAdvertisingParameters[] newArray(int paramAnonymousInt)
    {
      return new PeriodicAdvertisingParameters[paramAnonymousInt];
    }
  };
  private static final int INTERVAL_MAX = 65519;
  private static final int INTERVAL_MIN = 80;
  private final boolean mIncludeTxPower;
  private final int mInterval;
  
  private PeriodicAdvertisingParameters(Parcel paramParcel)
  {
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mIncludeTxPower = bool;
    mInterval = paramParcel.readInt();
  }
  
  private PeriodicAdvertisingParameters(boolean paramBoolean, int paramInt)
  {
    mIncludeTxPower = paramBoolean;
    mInterval = paramInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean getIncludeTxPower()
  {
    return mIncludeTxPower;
  }
  
  public int getInterval()
  {
    return mInterval;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mIncludeTxPower);
    paramParcel.writeInt(mInterval);
  }
  
  public static final class Builder
  {
    private boolean mIncludeTxPower = false;
    private int mInterval = 65519;
    
    public Builder() {}
    
    public PeriodicAdvertisingParameters build()
    {
      return new PeriodicAdvertisingParameters(mIncludeTxPower, mInterval, null);
    }
    
    public Builder setIncludeTxPower(boolean paramBoolean)
    {
      mIncludeTxPower = paramBoolean;
      return this;
    }
    
    public Builder setInterval(int paramInt)
    {
      if ((paramInt >= 80) && (paramInt <= 65519))
      {
        mInterval = paramInt;
        return this;
      }
      throw new IllegalArgumentException("Invalid interval (must be 80-65519)");
    }
  }
}
