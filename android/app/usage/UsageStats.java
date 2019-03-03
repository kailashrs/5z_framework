package android.app.usage;

import android.annotation.SystemApi;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import java.util.Iterator;
import java.util.Set;

public final class UsageStats
  implements Parcelable
{
  public static final Parcelable.Creator<UsageStats> CREATOR = new Parcelable.Creator()
  {
    public UsageStats createFromParcel(Parcel paramAnonymousParcel)
    {
      UsageStats localUsageStats = new UsageStats();
      mPackageName = paramAnonymousParcel.readString();
      mBeginTimeStamp = paramAnonymousParcel.readLong();
      mEndTimeStamp = paramAnonymousParcel.readLong();
      mLastTimeUsed = paramAnonymousParcel.readLong();
      mTotalTimeInForeground = paramAnonymousParcel.readLong();
      mLaunchCount = paramAnonymousParcel.readInt();
      mAppLaunchCount = paramAnonymousParcel.readInt();
      mLastEvent = paramAnonymousParcel.readInt();
      Bundle localBundle1 = paramAnonymousParcel.readBundle();
      if (localBundle1 != null)
      {
        mChooserCounts = new ArrayMap();
        paramAnonymousParcel = localBundle1.keySet().iterator();
        while (paramAnonymousParcel.hasNext())
        {
          String str = (String)paramAnonymousParcel.next();
          Object localObject;
          if (!mChooserCounts.containsKey(str))
          {
            localObject = new ArrayMap();
            mChooserCounts.put(str, localObject);
          }
          Bundle localBundle2 = localBundle1.getBundle(str);
          if (localBundle2 != null)
          {
            Iterator localIterator = localBundle2.keySet().iterator();
            while (localIterator.hasNext())
            {
              localObject = (String)localIterator.next();
              int i = localBundle2.getInt((String)localObject);
              if (i > 0) {
                ((ArrayMap)mChooserCounts.get(str)).put(localObject, Integer.valueOf(i));
              }
            }
          }
        }
      }
      return localUsageStats;
    }
    
    public UsageStats[] newArray(int paramAnonymousInt)
    {
      return new UsageStats[paramAnonymousInt];
    }
  };
  public int mAppLaunchCount;
  public long mBeginTimeStamp;
  public ArrayMap<String, ArrayMap<String, Integer>> mChooserCounts;
  public long mEndTimeStamp;
  public int mLastEvent;
  public long mLastTimeUsed;
  public int mLaunchCount;
  public String mPackageName;
  public long mTotalTimeInForeground;
  
  public UsageStats() {}
  
  public UsageStats(UsageStats paramUsageStats)
  {
    mPackageName = mPackageName;
    mBeginTimeStamp = mBeginTimeStamp;
    mEndTimeStamp = mEndTimeStamp;
    mLastTimeUsed = mLastTimeUsed;
    mTotalTimeInForeground = mTotalTimeInForeground;
    mLaunchCount = mLaunchCount;
    mAppLaunchCount = mAppLaunchCount;
    mLastEvent = mLastEvent;
    mChooserCounts = mChooserCounts;
  }
  
  public void add(UsageStats paramUsageStats)
  {
    if (mPackageName.equals(mPackageName))
    {
      if (mBeginTimeStamp > mBeginTimeStamp)
      {
        mLastEvent = Math.max(mLastEvent, mLastEvent);
        mLastTimeUsed = Math.max(mLastTimeUsed, mLastTimeUsed);
      }
      mBeginTimeStamp = Math.min(mBeginTimeStamp, mBeginTimeStamp);
      mEndTimeStamp = Math.max(mEndTimeStamp, mEndTimeStamp);
      mTotalTimeInForeground += mTotalTimeInForeground;
      mLaunchCount += mLaunchCount;
      mAppLaunchCount += mAppLaunchCount;
      if (mChooserCounts == null)
      {
        mChooserCounts = mChooserCounts;
      }
      else if (mChooserCounts != null)
      {
        int i = mChooserCounts.size();
        for (int j = 0; j < i; j++)
        {
          String str = (String)mChooserCounts.keyAt(j);
          ArrayMap localArrayMap = (ArrayMap)mChooserCounts.valueAt(j);
          int k;
          int m;
          if ((mChooserCounts.containsKey(str)) && (mChooserCounts.get(str) != null))
          {
            k = localArrayMap.size();
            m = 0;
          }
          while (m < k)
          {
            localObject = (String)localArrayMap.keyAt(m);
            int n = ((Integer)localArrayMap.valueAt(m)).intValue();
            int i1 = ((Integer)((ArrayMap)mChooserCounts.get(str)).getOrDefault(localObject, Integer.valueOf(0))).intValue();
            ((ArrayMap)mChooserCounts.get(str)).put(localObject, Integer.valueOf(i1 + n));
            m++;
            continue;
            mChooserCounts.put(str, localArrayMap);
          }
        }
      }
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Can't merge UsageStats for package '");
    ((StringBuilder)localObject).append(mPackageName);
    ((StringBuilder)localObject).append("' with UsageStats for package '");
    ((StringBuilder)localObject).append(mPackageName);
    ((StringBuilder)localObject).append("'.");
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @SystemApi
  public int getAppLaunchCount()
  {
    return mAppLaunchCount;
  }
  
  public long getFirstTimeStamp()
  {
    return mBeginTimeStamp;
  }
  
  public long getLastTimeStamp()
  {
    return mEndTimeStamp;
  }
  
  public long getLastTimeUsed()
  {
    return mLastTimeUsed;
  }
  
  public UsageStats getObfuscatedForInstantApp()
  {
    UsageStats localUsageStats = new UsageStats(this);
    mPackageName = "android.instant_app";
    return localUsageStats;
  }
  
  public String getPackageName()
  {
    return mPackageName;
  }
  
  public long getTotalTimeInForeground()
  {
    return mTotalTimeInForeground;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mPackageName);
    paramParcel.writeLong(mBeginTimeStamp);
    paramParcel.writeLong(mEndTimeStamp);
    paramParcel.writeLong(mLastTimeUsed);
    paramParcel.writeLong(mTotalTimeInForeground);
    paramParcel.writeInt(mLaunchCount);
    paramParcel.writeInt(mAppLaunchCount);
    paramParcel.writeInt(mLastEvent);
    Bundle localBundle1 = new Bundle();
    if (mChooserCounts != null)
    {
      int i = mChooserCounts.size();
      for (paramInt = 0; paramInt < i; paramInt++)
      {
        String str = (String)mChooserCounts.keyAt(paramInt);
        ArrayMap localArrayMap = (ArrayMap)mChooserCounts.valueAt(paramInt);
        Bundle localBundle2 = new Bundle();
        int j = localArrayMap.size();
        for (int k = 0; k < j; k++) {
          localBundle2.putInt((String)localArrayMap.keyAt(k), ((Integer)localArrayMap.valueAt(k)).intValue());
        }
        localBundle1.putBundle(str, localBundle2);
      }
    }
    paramParcel.writeBundle(localBundle1);
  }
}
