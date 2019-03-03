package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class ConfigRequest
  implements Parcelable
{
  public static final int CLUSTER_ID_MAX = 65535;
  public static final int CLUSTER_ID_MIN = 0;
  public static final Parcelable.Creator<ConfigRequest> CREATOR = new Parcelable.Creator()
  {
    public ConfigRequest createFromParcel(Parcel paramAnonymousParcel)
    {
      if (paramAnonymousParcel.readInt() != 0) {}
      for (boolean bool = true;; bool = false) {
        break;
      }
      return new ConfigRequest(bool, paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.readInt(), paramAnonymousParcel.createIntArray(), null);
    }
    
    public ConfigRequest[] newArray(int paramAnonymousInt)
    {
      return new ConfigRequest[paramAnonymousInt];
    }
  };
  public static final int DW_DISABLE = 0;
  public static final int DW_INTERVAL_NOT_INIT = -1;
  public static final int NAN_BAND_24GHZ = 0;
  public static final int NAN_BAND_5GHZ = 1;
  public final int mClusterHigh;
  public final int mClusterLow;
  public final int[] mDiscoveryWindowInterval;
  public final int mMasterPreference;
  public final boolean mSupport5gBand;
  
  private ConfigRequest(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfInt)
  {
    mSupport5gBand = paramBoolean;
    mMasterPreference = paramInt1;
    mClusterLow = paramInt2;
    mClusterHigh = paramInt3;
    mDiscoveryWindowInterval = paramArrayOfInt;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof ConfigRequest)) {
      return false;
    }
    paramObject = (ConfigRequest)paramObject;
    if ((mSupport5gBand != mSupport5gBand) || (mMasterPreference != mMasterPreference) || (mClusterLow != mClusterLow) || (mClusterHigh != mClusterHigh) || (!Arrays.equals(mDiscoveryWindowInterval, mDiscoveryWindowInterval))) {
      bool = false;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return 31 * (31 * (31 * (31 * (31 * 17 + mSupport5gBand) + mMasterPreference) + mClusterLow) + mClusterHigh) + Arrays.hashCode(mDiscoveryWindowInterval);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ConfigRequest [mSupport5gBand=");
    localStringBuilder.append(mSupport5gBand);
    localStringBuilder.append(", mMasterPreference=");
    localStringBuilder.append(mMasterPreference);
    localStringBuilder.append(", mClusterLow=");
    localStringBuilder.append(mClusterLow);
    localStringBuilder.append(", mClusterHigh=");
    localStringBuilder.append(mClusterHigh);
    localStringBuilder.append(", mDiscoveryWindowInterval=");
    localStringBuilder.append(Arrays.toString(mDiscoveryWindowInterval));
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
  
  public void validate()
    throws IllegalArgumentException
  {
    if (mMasterPreference >= 0)
    {
      if ((mMasterPreference != 1) && (mMasterPreference != 255) && (mMasterPreference <= 255))
      {
        if (mClusterLow >= 0)
        {
          if (mClusterLow <= 65535)
          {
            if (mClusterHigh >= 0)
            {
              if (mClusterHigh <= 65535)
              {
                if (mClusterLow <= mClusterHigh)
                {
                  if (mDiscoveryWindowInterval.length == 2)
                  {
                    if ((mDiscoveryWindowInterval[0] != -1) && ((mDiscoveryWindowInterval[0] < 1) || (mDiscoveryWindowInterval[0] > 5))) {
                      throw new IllegalArgumentException("Invalid discovery window interval for 2.4GHz: valid is UNSET or [1,5]");
                    }
                    if ((mDiscoveryWindowInterval[1] != -1) && ((mDiscoveryWindowInterval[1] < 0) || (mDiscoveryWindowInterval[1] > 5))) {
                      throw new IllegalArgumentException("Invalid discovery window interval for 5GHz: valid is UNSET or [0,5]");
                    }
                    return;
                  }
                  throw new IllegalArgumentException("Invalid discovery window interval: must have 2 elements (2.4 & 5");
                }
                throw new IllegalArgumentException("Invalid argument combination - must have Cluster Low <= Cluster High");
              }
              throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
            }
            throw new IllegalArgumentException("Cluster specification must be non-negative");
          }
          throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
        }
        throw new IllegalArgumentException("Cluster specification must be non-negative");
      }
      throw new IllegalArgumentException("Master Preference specification must not exceed 255 or use 1 or 255 (reserved values)");
    }
    throw new IllegalArgumentException("Master Preference specification must be non-negative");
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(mSupport5gBand);
    paramParcel.writeInt(mMasterPreference);
    paramParcel.writeInt(mClusterLow);
    paramParcel.writeInt(mClusterHigh);
    paramParcel.writeIntArray(mDiscoveryWindowInterval);
  }
  
  public static final class Builder
  {
    private int mClusterHigh = 65535;
    private int mClusterLow = 0;
    private int[] mDiscoveryWindowInterval = { -1, -1 };
    private int mMasterPreference = 0;
    private boolean mSupport5gBand = false;
    
    public Builder() {}
    
    public ConfigRequest build()
    {
      if (mClusterLow <= mClusterHigh) {
        return new ConfigRequest(mSupport5gBand, mMasterPreference, mClusterLow, mClusterHigh, mDiscoveryWindowInterval, null);
      }
      throw new IllegalArgumentException("Invalid argument combination - must have Cluster Low <= Cluster High");
    }
    
    public Builder setClusterHigh(int paramInt)
    {
      if (paramInt >= 0)
      {
        if (paramInt <= 65535)
        {
          mClusterHigh = paramInt;
          return this;
        }
        throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
      }
      throw new IllegalArgumentException("Cluster specification must be non-negative");
    }
    
    public Builder setClusterLow(int paramInt)
    {
      if (paramInt >= 0)
      {
        if (paramInt <= 65535)
        {
          mClusterLow = paramInt;
          return this;
        }
        throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
      }
      throw new IllegalArgumentException("Cluster specification must be non-negative");
    }
    
    public Builder setDiscoveryWindowInterval(int paramInt1, int paramInt2)
    {
      if ((paramInt1 != 0) && (paramInt1 != 1)) {
        throw new IllegalArgumentException("Invalid band value");
      }
      if (((paramInt1 == 0) && ((paramInt2 < 1) || (paramInt2 > 5))) || ((paramInt1 == 1) && ((paramInt2 < 0) || (paramInt2 > 5)))) {
        throw new IllegalArgumentException("Invalid interval value: 2.4 GHz [1,5] or 5GHz [0,5]");
      }
      mDiscoveryWindowInterval[paramInt1] = paramInt2;
      return this;
    }
    
    public Builder setMasterPreference(int paramInt)
    {
      if (paramInt >= 0)
      {
        if ((paramInt != 1) && (paramInt != 255) && (paramInt <= 255))
        {
          mMasterPreference = paramInt;
          return this;
        }
        throw new IllegalArgumentException("Master Preference specification must not exceed 255 or use 1 or 255 (reserved values)");
      }
      throw new IllegalArgumentException("Master Preference specification must be non-negative");
    }
    
    public Builder setSupport5gBand(boolean paramBoolean)
    {
      mSupport5gBand = paramBoolean;
      return this;
    }
  }
}
