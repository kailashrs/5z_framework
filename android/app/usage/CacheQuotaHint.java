package android.app.usage;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.Objects;

@SystemApi
public final class CacheQuotaHint
  implements Parcelable
{
  public static final Parcelable.Creator<CacheQuotaHint> CREATOR = new Parcelable.Creator()
  {
    public CacheQuotaHint createFromParcel(Parcel paramAnonymousParcel)
    {
      return new CacheQuotaHint.Builder().setVolumeUuid(paramAnonymousParcel.readString()).setUid(paramAnonymousParcel.readInt()).setQuota(paramAnonymousParcel.readLong()).setUsageStats((UsageStats)paramAnonymousParcel.readParcelable(UsageStats.class.getClassLoader())).build();
    }
    
    public CacheQuotaHint[] newArray(int paramAnonymousInt)
    {
      return new CacheQuotaHint[paramAnonymousInt];
    }
  };
  public static final long QUOTA_NOT_SET = -1L;
  private final long mQuota;
  private final int mUid;
  private final UsageStats mUsageStats;
  private final String mUuid;
  
  public CacheQuotaHint(Builder paramBuilder)
  {
    mUuid = mUuid;
    mUid = mUid;
    mUsageStats = mUsageStats;
    mQuota = mQuota;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool1 = paramObject instanceof CacheQuotaHint;
    boolean bool2 = false;
    if (bool1)
    {
      paramObject = (CacheQuotaHint)paramObject;
      bool1 = bool2;
      if (Objects.equals(mUuid, mUuid))
      {
        bool1 = bool2;
        if (Objects.equals(mUsageStats, mUsageStats))
        {
          bool1 = bool2;
          if (mUid == mUid)
          {
            bool1 = bool2;
            if (mQuota == mQuota) {
              bool1 = true;
            }
          }
        }
      }
      return bool1;
    }
    return false;
  }
  
  public long getQuota()
  {
    return mQuota;
  }
  
  public int getUid()
  {
    return mUid;
  }
  
  public UsageStats getUsageStats()
  {
    return mUsageStats;
  }
  
  public String getVolumeUuid()
  {
    return mUuid;
  }
  
  public int hashCode()
  {
    return Objects.hash(new Object[] { mUuid, Integer.valueOf(mUid), mUsageStats, Long.valueOf(mQuota) });
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mUuid);
    paramParcel.writeInt(mUid);
    paramParcel.writeLong(mQuota);
    paramParcel.writeParcelable(mUsageStats, 0);
  }
  
  public static final class Builder
  {
    private long mQuota;
    private int mUid;
    private UsageStats mUsageStats;
    private String mUuid;
    
    public Builder() {}
    
    public Builder(CacheQuotaHint paramCacheQuotaHint)
    {
      setVolumeUuid(paramCacheQuotaHint.getVolumeUuid());
      setUid(paramCacheQuotaHint.getUid());
      setUsageStats(paramCacheQuotaHint.getUsageStats());
      setQuota(paramCacheQuotaHint.getQuota());
    }
    
    public CacheQuotaHint build()
    {
      return new CacheQuotaHint(this);
    }
    
    public Builder setQuota(long paramLong)
    {
      boolean bool;
      if (paramLong >= -1L) {
        bool = true;
      } else {
        bool = false;
      }
      Preconditions.checkArgument(bool);
      mQuota = paramLong;
      return this;
    }
    
    public Builder setUid(int paramInt)
    {
      Preconditions.checkArgumentNonnegative(paramInt, "Proposed uid was negative.");
      mUid = paramInt;
      return this;
    }
    
    public Builder setUsageStats(UsageStats paramUsageStats)
    {
      mUsageStats = paramUsageStats;
      return this;
    }
    
    public Builder setVolumeUuid(String paramString)
    {
      mUuid = paramString;
      return this;
    }
  }
}
