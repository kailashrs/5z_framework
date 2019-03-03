package android.app.usage;

import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class ConfigurationStats
  implements Parcelable
{
  public static final Parcelable.Creator<ConfigurationStats> CREATOR = new Parcelable.Creator()
  {
    public ConfigurationStats createFromParcel(Parcel paramAnonymousParcel)
    {
      ConfigurationStats localConfigurationStats = new ConfigurationStats();
      if (paramAnonymousParcel.readInt() != 0) {
        mConfiguration = ((Configuration)Configuration.CREATOR.createFromParcel(paramAnonymousParcel));
      }
      mBeginTimeStamp = paramAnonymousParcel.readLong();
      mEndTimeStamp = paramAnonymousParcel.readLong();
      mLastTimeActive = paramAnonymousParcel.readLong();
      mTotalTimeActive = paramAnonymousParcel.readLong();
      mActivationCount = paramAnonymousParcel.readInt();
      return localConfigurationStats;
    }
    
    public ConfigurationStats[] newArray(int paramAnonymousInt)
    {
      return new ConfigurationStats[paramAnonymousInt];
    }
  };
  public int mActivationCount;
  public long mBeginTimeStamp;
  public Configuration mConfiguration;
  public long mEndTimeStamp;
  public long mLastTimeActive;
  public long mTotalTimeActive;
  
  public ConfigurationStats() {}
  
  public ConfigurationStats(ConfigurationStats paramConfigurationStats)
  {
    mConfiguration = mConfiguration;
    mBeginTimeStamp = mBeginTimeStamp;
    mEndTimeStamp = mEndTimeStamp;
    mLastTimeActive = mLastTimeActive;
    mTotalTimeActive = mTotalTimeActive;
    mActivationCount = mActivationCount;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int getActivationCount()
  {
    return mActivationCount;
  }
  
  public Configuration getConfiguration()
  {
    return mConfiguration;
  }
  
  public long getFirstTimeStamp()
  {
    return mBeginTimeStamp;
  }
  
  public long getLastTimeActive()
  {
    return mLastTimeActive;
  }
  
  public long getLastTimeStamp()
  {
    return mEndTimeStamp;
  }
  
  public long getTotalTimeActive()
  {
    return mTotalTimeActive;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    if (mConfiguration != null)
    {
      paramParcel.writeInt(1);
      mConfiguration.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeLong(mBeginTimeStamp);
    paramParcel.writeLong(mEndTimeStamp);
    paramParcel.writeLong(mLastTimeActive);
    paramParcel.writeLong(mTotalTimeActive);
    paramParcel.writeInt(mActivationCount);
  }
}
