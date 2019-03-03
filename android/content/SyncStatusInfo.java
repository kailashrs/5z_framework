package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

public class SyncStatusInfo
  implements Parcelable
{
  public static final Parcelable.Creator<SyncStatusInfo> CREATOR = new Parcelable.Creator()
  {
    public SyncStatusInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new SyncStatusInfo(paramAnonymousParcel);
    }
    
    public SyncStatusInfo[] newArray(int paramAnonymousInt)
    {
      return new SyncStatusInfo[paramAnonymousInt];
    }
  };
  private static final int MAX_EVENT_COUNT = 10;
  private static final int SOURCE_COUNT = 6;
  private static final String TAG = "Sync";
  static final int VERSION = 6;
  public final int authorityId;
  public long initialFailureTime;
  public boolean initialize;
  public String lastFailureMesg;
  public int lastFailureSource;
  public long lastFailureTime;
  public int lastSuccessSource;
  public long lastSuccessTime;
  public long lastTodayResetTime;
  private final ArrayList<Long> mLastEventTimes = new ArrayList();
  private final ArrayList<String> mLastEvents = new ArrayList();
  public boolean pending;
  public final long[] perSourceLastFailureTimes = new long[6];
  public final long[] perSourceLastSuccessTimes = new long[6];
  private ArrayList<Long> periodicSyncTimes;
  public final Stats todayStats = new Stats();
  public final Stats totalStats = new Stats();
  public final Stats yesterdayStats = new Stats();
  
  public SyncStatusInfo(int paramInt)
  {
    authorityId = paramInt;
  }
  
  public SyncStatusInfo(SyncStatusInfo paramSyncStatusInfo)
  {
    authorityId = authorityId;
    totalStats.copyTo(totalStats);
    todayStats.copyTo(todayStats);
    yesterdayStats.copyTo(yesterdayStats);
    lastTodayResetTime = lastTodayResetTime;
    lastSuccessTime = lastSuccessTime;
    lastSuccessSource = lastSuccessSource;
    lastFailureTime = lastFailureTime;
    lastFailureSource = lastFailureSource;
    lastFailureMesg = lastFailureMesg;
    initialFailureTime = initialFailureTime;
    pending = pending;
    initialize = initialize;
    if (periodicSyncTimes != null) {
      periodicSyncTimes = new ArrayList(periodicSyncTimes);
    }
    mLastEventTimes.addAll(mLastEventTimes);
    mLastEvents.addAll(mLastEvents);
    copy(perSourceLastSuccessTimes, perSourceLastSuccessTimes);
    copy(perSourceLastFailureTimes, perSourceLastFailureTimes);
  }
  
  public SyncStatusInfo(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    if ((i != 6) && (i != 1))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown version: ");
      localStringBuilder.append(i);
      Log.w("SyncStatusInfo", localStringBuilder.toString());
    }
    authorityId = paramParcel.readInt();
    totalStats.totalElapsedTime = paramParcel.readLong();
    totalStats.numSyncs = paramParcel.readInt();
    totalStats.numSourcePoll = paramParcel.readInt();
    totalStats.numSourceOther = paramParcel.readInt();
    totalStats.numSourceLocal = paramParcel.readInt();
    totalStats.numSourceUser = paramParcel.readInt();
    lastSuccessTime = paramParcel.readLong();
    lastSuccessSource = paramParcel.readInt();
    lastFailureTime = paramParcel.readLong();
    lastFailureSource = paramParcel.readInt();
    lastFailureMesg = paramParcel.readString();
    initialFailureTime = paramParcel.readLong();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    pending = bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    initialize = bool;
    if (i == 1)
    {
      periodicSyncTimes = null;
    }
    else
    {
      int j = paramParcel.readInt();
      int k;
      if (j < 0)
      {
        periodicSyncTimes = null;
      }
      else
      {
        periodicSyncTimes = new ArrayList();
        for (k = 0; k < j; k++) {
          periodicSyncTimes.add(Long.valueOf(paramParcel.readLong()));
        }
      }
      if (i >= 3)
      {
        mLastEventTimes.clear();
        mLastEvents.clear();
        j = paramParcel.readInt();
        for (k = 0; k < j; k++)
        {
          mLastEventTimes.add(Long.valueOf(paramParcel.readLong()));
          mLastEvents.add(paramParcel.readString());
        }
      }
    }
    if (i < 4)
    {
      totalStats.numSourcePeriodic = (totalStats.numSyncs - totalStats.numSourceLocal - totalStats.numSourcePoll - totalStats.numSourceOther - totalStats.numSourceUser);
      if (totalStats.numSourcePeriodic < 0) {
        totalStats.numSourcePeriodic = 0;
      }
    }
    else
    {
      totalStats.numSourcePeriodic = paramParcel.readInt();
    }
    if (i >= 5)
    {
      totalStats.numSourceFeed = paramParcel.readInt();
      totalStats.numFailures = paramParcel.readInt();
      totalStats.numCancels = paramParcel.readInt();
      lastTodayResetTime = paramParcel.readLong();
      todayStats.readFromParcel(paramParcel);
      yesterdayStats.readFromParcel(paramParcel);
    }
    if (i >= 6)
    {
      paramParcel.readLongArray(perSourceLastSuccessTimes);
      paramParcel.readLongArray(perSourceLastFailureTimes);
    }
  }
  
  private static boolean areSameDates(long paramLong1, long paramLong2)
  {
    GregorianCalendar localGregorianCalendar1 = new GregorianCalendar();
    GregorianCalendar localGregorianCalendar2 = new GregorianCalendar();
    localGregorianCalendar1.setTimeInMillis(paramLong1);
    localGregorianCalendar2.setTimeInMillis(paramLong2);
    boolean bool = true;
    if ((localGregorianCalendar1.get(1) != localGregorianCalendar2.get(1)) || (localGregorianCalendar1.get(6) != localGregorianCalendar2.get(6))) {
      bool = false;
    }
    return bool;
  }
  
  private static void copy(long[] paramArrayOfLong1, long[] paramArrayOfLong2)
  {
    System.arraycopy(paramArrayOfLong2, 0, paramArrayOfLong1, 0, paramArrayOfLong1.length);
  }
  
  private void ensurePeriodicSyncTimeSize(int paramInt)
  {
    if (periodicSyncTimes == null) {
      periodicSyncTimes = new ArrayList(0);
    }
    int i = paramInt + 1;
    if (periodicSyncTimes.size() < i) {
      for (paramInt = periodicSyncTimes.size(); paramInt < i; paramInt++) {
        periodicSyncTimes.add(Long.valueOf(0L));
      }
    }
  }
  
  public void addEvent(String paramString)
  {
    if (mLastEventTimes.size() >= 10)
    {
      mLastEventTimes.remove(9);
      mLastEvents.remove(9);
    }
    mLastEventTimes.add(0, Long.valueOf(System.currentTimeMillis()));
    mLastEvents.add(0, paramString);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public String getEvent(int paramInt)
  {
    return (String)mLastEvents.get(paramInt);
  }
  
  public int getEventCount()
  {
    return mLastEventTimes.size();
  }
  
  public long getEventTime(int paramInt)
  {
    return ((Long)mLastEventTimes.get(paramInt)).longValue();
  }
  
  public int getLastFailureMesgAsInt(int paramInt)
  {
    int i = ContentResolver.syncErrorStringToInt(lastFailureMesg);
    if (i > 0) {
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown lastFailureMesg:");
    localStringBuilder.append(lastFailureMesg);
    Log.d("Sync", localStringBuilder.toString());
    return paramInt;
  }
  
  public long getPeriodicSyncTime(int paramInt)
  {
    if ((periodicSyncTimes != null) && (paramInt < periodicSyncTimes.size())) {
      return ((Long)periodicSyncTimes.get(paramInt)).longValue();
    }
    return 0L;
  }
  
  public void maybeResetTodayStats(boolean paramBoolean1, boolean paramBoolean2)
  {
    long l = System.currentTimeMillis();
    if (!paramBoolean2)
    {
      if (areSameDates(l, lastTodayResetTime)) {
        return;
      }
      if ((l < lastTodayResetTime) && (!paramBoolean1)) {
        return;
      }
    }
    lastTodayResetTime = l;
    todayStats.copyTo(yesterdayStats);
    todayStats.clear();
  }
  
  public void removePeriodicSyncTime(int paramInt)
  {
    if ((periodicSyncTimes != null) && (paramInt < periodicSyncTimes.size())) {
      periodicSyncTimes.remove(paramInt);
    }
  }
  
  public void setLastFailure(int paramInt, long paramLong, String paramString)
  {
    lastFailureTime = paramLong;
    lastFailureSource = paramInt;
    lastFailureMesg = paramString;
    if (initialFailureTime == 0L) {
      initialFailureTime = paramLong;
    }
    if ((paramInt >= 0) && (paramInt < perSourceLastFailureTimes.length)) {
      perSourceLastFailureTimes[paramInt] = paramLong;
    }
  }
  
  public void setLastSuccess(int paramInt, long paramLong)
  {
    lastSuccessTime = paramLong;
    lastSuccessSource = paramInt;
    lastFailureTime = 0L;
    lastFailureSource = -1;
    lastFailureMesg = null;
    initialFailureTime = 0L;
    if ((paramInt >= 0) && (paramInt < perSourceLastSuccessTimes.length)) {
      perSourceLastSuccessTimes[paramInt] = paramLong;
    }
  }
  
  public void setPeriodicSyncTime(int paramInt, long paramLong)
  {
    ensurePeriodicSyncTimeSize(paramInt);
    periodicSyncTimes.set(paramInt, Long.valueOf(paramLong));
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(6);
    paramParcel.writeInt(authorityId);
    paramParcel.writeLong(totalStats.totalElapsedTime);
    paramParcel.writeInt(totalStats.numSyncs);
    paramParcel.writeInt(totalStats.numSourcePoll);
    paramParcel.writeInt(totalStats.numSourceOther);
    paramParcel.writeInt(totalStats.numSourceLocal);
    paramParcel.writeInt(totalStats.numSourceUser);
    paramParcel.writeLong(lastSuccessTime);
    paramParcel.writeInt(lastSuccessSource);
    paramParcel.writeLong(lastFailureTime);
    paramParcel.writeInt(lastFailureSource);
    paramParcel.writeString(lastFailureMesg);
    paramParcel.writeLong(initialFailureTime);
    paramParcel.writeInt(pending);
    paramParcel.writeInt(initialize);
    if (periodicSyncTimes != null)
    {
      paramParcel.writeInt(periodicSyncTimes.size());
      Iterator localIterator = periodicSyncTimes.iterator();
      while (localIterator.hasNext()) {
        paramParcel.writeLong(((Long)localIterator.next()).longValue());
      }
    }
    paramParcel.writeInt(-1);
    paramParcel.writeInt(mLastEventTimes.size());
    for (paramInt = 0; paramInt < mLastEventTimes.size(); paramInt++)
    {
      paramParcel.writeLong(((Long)mLastEventTimes.get(paramInt)).longValue());
      paramParcel.writeString((String)mLastEvents.get(paramInt));
    }
    paramParcel.writeInt(totalStats.numSourcePeriodic);
    paramParcel.writeInt(totalStats.numSourceFeed);
    paramParcel.writeInt(totalStats.numFailures);
    paramParcel.writeInt(totalStats.numCancels);
    paramParcel.writeLong(lastTodayResetTime);
    todayStats.writeToParcel(paramParcel);
    yesterdayStats.writeToParcel(paramParcel);
    paramParcel.writeLongArray(perSourceLastSuccessTimes);
    paramParcel.writeLongArray(perSourceLastFailureTimes);
  }
  
  public static class Stats
  {
    public int numCancels;
    public int numFailures;
    public int numSourceFeed;
    public int numSourceLocal;
    public int numSourceOther;
    public int numSourcePeriodic;
    public int numSourcePoll;
    public int numSourceUser;
    public int numSyncs;
    public long totalElapsedTime;
    
    public Stats() {}
    
    public void clear()
    {
      totalElapsedTime = 0L;
      numSyncs = 0;
      numSourcePoll = 0;
      numSourceOther = 0;
      numSourceLocal = 0;
      numSourceUser = 0;
      numSourcePeriodic = 0;
      numSourceFeed = 0;
      numFailures = 0;
      numCancels = 0;
    }
    
    public void copyTo(Stats paramStats)
    {
      totalElapsedTime = totalElapsedTime;
      numSyncs = numSyncs;
      numSourcePoll = numSourcePoll;
      numSourceOther = numSourceOther;
      numSourceLocal = numSourceLocal;
      numSourceUser = numSourceUser;
      numSourcePeriodic = numSourcePeriodic;
      numSourceFeed = numSourceFeed;
      numFailures = numFailures;
      numCancels = numCancels;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      totalElapsedTime = paramParcel.readLong();
      numSyncs = paramParcel.readInt();
      numSourcePoll = paramParcel.readInt();
      numSourceOther = paramParcel.readInt();
      numSourceLocal = paramParcel.readInt();
      numSourceUser = paramParcel.readInt();
      numSourcePeriodic = paramParcel.readInt();
      numSourceFeed = paramParcel.readInt();
      numFailures = paramParcel.readInt();
      numCancels = paramParcel.readInt();
    }
    
    public void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeLong(totalElapsedTime);
      paramParcel.writeInt(numSyncs);
      paramParcel.writeInt(numSourcePoll);
      paramParcel.writeInt(numSourceOther);
      paramParcel.writeInt(numSourceLocal);
      paramParcel.writeInt(numSourceUser);
      paramParcel.writeInt(numSourcePeriodic);
      paramParcel.writeInt(numSourceFeed);
      paramParcel.writeInt(numFailures);
      paramParcel.writeInt(numCancels);
    }
  }
}
