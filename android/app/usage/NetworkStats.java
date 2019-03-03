package android.app.usage;

import android.content.Context;
import android.net.INetworkStatsService;
import android.net.INetworkStatsSession;
import android.net.NetworkStats.Entry;
import android.net.NetworkStatsHistory;
import android.net.NetworkStatsHistory.Entry;
import android.net.NetworkTemplate;
import android.os.RemoteException;
import android.util.IntArray;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class NetworkStats
  implements AutoCloseable
{
  private static final String TAG = "NetworkStats";
  private final CloseGuard mCloseGuard = CloseGuard.get();
  private final long mEndTimeStamp;
  private int mEnumerationIndex = 0;
  private NetworkStatsHistory mHistory = null;
  private NetworkStatsHistory.Entry mRecycledHistoryEntry = null;
  private NetworkStats.Entry mRecycledSummaryEntry = null;
  private INetworkStatsSession mSession;
  private final long mStartTimeStamp;
  private int mState = -1;
  private android.net.NetworkStats mSummary = null;
  private int mTag = 0;
  private NetworkTemplate mTemplate;
  private int mUidOrUidIndex;
  private int[] mUids;
  
  NetworkStats(Context paramContext, NetworkTemplate paramNetworkTemplate, int paramInt, long paramLong1, long paramLong2, INetworkStatsService paramINetworkStatsService)
    throws RemoteException, SecurityException
  {
    mSession = paramINetworkStatsService.openSessionForUsageStats(paramInt, paramContext.getOpPackageName());
    mCloseGuard.open("close");
    mTemplate = paramNetworkTemplate;
    mStartTimeStamp = paramLong1;
    mEndTimeStamp = paramLong2;
  }
  
  private void fillBucketFromSummaryEntry(Bucket paramBucket)
  {
    Bucket.access$102(paramBucket, Bucket.convertUid(mRecycledSummaryEntry.uid));
    Bucket.access$302(paramBucket, Bucket.convertTag(mRecycledSummaryEntry.tag));
    Bucket.access$502(paramBucket, Bucket.convertState(mRecycledSummaryEntry.set));
    Bucket.access$702(paramBucket, Bucket.convertDefaultNetworkStatus(mRecycledSummaryEntry.defaultNetwork));
    Bucket.access$902(paramBucket, Bucket.convertMetered(mRecycledSummaryEntry.metered));
    Bucket.access$1102(paramBucket, Bucket.convertRoaming(mRecycledSummaryEntry.roaming));
    Bucket.access$1302(paramBucket, mStartTimeStamp);
    Bucket.access$1402(paramBucket, mEndTimeStamp);
    Bucket.access$1502(paramBucket, mRecycledSummaryEntry.rxBytes);
    Bucket.access$1602(paramBucket, mRecycledSummaryEntry.rxPackets);
    Bucket.access$1702(paramBucket, mRecycledSummaryEntry.txBytes);
    Bucket.access$1802(paramBucket, mRecycledSummaryEntry.txPackets);
  }
  
  private boolean getNextHistoryBucket(Bucket paramBucket)
  {
    if ((paramBucket != null) && (mHistory != null))
    {
      if (mEnumerationIndex < mHistory.size())
      {
        NetworkStatsHistory localNetworkStatsHistory = mHistory;
        int i = mEnumerationIndex;
        mEnumerationIndex = (i + 1);
        mRecycledHistoryEntry = localNetworkStatsHistory.getValues(i, mRecycledHistoryEntry);
        Bucket.access$102(paramBucket, Bucket.convertUid(getUid()));
        Bucket.access$302(paramBucket, Bucket.convertTag(mTag));
        Bucket.access$502(paramBucket, mState);
        Bucket.access$702(paramBucket, -1);
        Bucket.access$902(paramBucket, -1);
        Bucket.access$1102(paramBucket, -1);
        Bucket.access$1302(paramBucket, mRecycledHistoryEntry.bucketStart);
        Bucket.access$1402(paramBucket, mRecycledHistoryEntry.bucketStart + mRecycledHistoryEntry.bucketDuration);
        Bucket.access$1502(paramBucket, mRecycledHistoryEntry.rxBytes);
        Bucket.access$1602(paramBucket, mRecycledHistoryEntry.rxPackets);
        Bucket.access$1702(paramBucket, mRecycledHistoryEntry.txBytes);
        Bucket.access$1802(paramBucket, mRecycledHistoryEntry.txPackets);
        return true;
      }
      if (hasNextUid())
      {
        stepHistory();
        return getNextHistoryBucket(paramBucket);
      }
    }
    return false;
  }
  
  private boolean getNextSummaryBucket(Bucket paramBucket)
  {
    if ((paramBucket != null) && (mEnumerationIndex < mSummary.size()))
    {
      android.net.NetworkStats localNetworkStats = mSummary;
      int i = mEnumerationIndex;
      mEnumerationIndex = (i + 1);
      mRecycledSummaryEntry = localNetworkStats.getValues(i, mRecycledSummaryEntry);
      fillBucketFromSummaryEntry(paramBucket);
      return true;
    }
    return false;
  }
  
  private int getUid()
  {
    if (isUidEnumeration())
    {
      if ((mUidOrUidIndex >= 0) && (mUidOrUidIndex < mUids.length)) {
        return mUids[mUidOrUidIndex];
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Index=");
      localStringBuilder.append(mUidOrUidIndex);
      localStringBuilder.append(" mUids.length=");
      localStringBuilder.append(mUids.length);
      throw new IndexOutOfBoundsException(localStringBuilder.toString());
    }
    return mUidOrUidIndex;
  }
  
  private boolean hasNextUid()
  {
    boolean bool1 = isUidEnumeration();
    boolean bool2 = true;
    if ((!bool1) || (mUidOrUidIndex + 1 >= mUids.length)) {
      bool2 = false;
    }
    return bool2;
  }
  
  private boolean isUidEnumeration()
  {
    boolean bool;
    if (mUids != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void setSingleUidTagState(int paramInt1, int paramInt2, int paramInt3)
  {
    mUidOrUidIndex = paramInt1;
    mTag = paramInt2;
    mState = paramInt3;
  }
  
  private void stepHistory()
  {
    if (hasNextUid())
    {
      stepUid();
      mHistory = null;
      try
      {
        mHistory = mSession.getHistoryIntervalForUid(mTemplate, getUid(), -1, 0, -1, mStartTimeStamp, mEndTimeStamp);
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("NetworkStats", localRemoteException);
      }
      mEnumerationIndex = 0;
    }
  }
  
  private void stepUid()
  {
    if (mUids != null) {
      mUidOrUidIndex += 1;
    }
  }
  
  public void close()
  {
    if (mSession != null) {
      try
      {
        mSession.close();
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("NetworkStats", localRemoteException);
      }
    }
    mSession = null;
    if (mCloseGuard != null) {
      mCloseGuard.close();
    }
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if (mCloseGuard != null) {
        mCloseGuard.warnIfOpen();
      }
      close();
      return;
    }
    finally
    {
      super.finalize();
    }
  }
  
  Bucket getDeviceSummaryForNetwork()
    throws RemoteException
  {
    mSummary = mSession.getDeviceSummaryForNetwork(mTemplate, mStartTimeStamp, mEndTimeStamp);
    mEnumerationIndex = mSummary.size();
    return getSummaryAggregate();
  }
  
  public boolean getNextBucket(Bucket paramBucket)
  {
    if (mSummary != null) {
      return getNextSummaryBucket(paramBucket);
    }
    return getNextHistoryBucket(paramBucket);
  }
  
  Bucket getSummaryAggregate()
  {
    if (mSummary == null) {
      return null;
    }
    Bucket localBucket = new Bucket();
    if (mRecycledSummaryEntry == null) {
      mRecycledSummaryEntry = new NetworkStats.Entry();
    }
    mSummary.getTotal(mRecycledSummaryEntry);
    fillBucketFromSummaryEntry(localBucket);
    return localBucket;
  }
  
  public boolean hasNextBucket()
  {
    android.net.NetworkStats localNetworkStats = mSummary;
    boolean bool1 = true;
    boolean bool2 = true;
    if (localNetworkStats != null)
    {
      if (mEnumerationIndex >= mSummary.size()) {
        bool2 = false;
      }
      return bool2;
    }
    if (mHistory != null)
    {
      if ((mEnumerationIndex >= mHistory.size()) && (!hasNextUid())) {
        bool2 = false;
      } else {
        bool2 = bool1;
      }
      return bool2;
    }
    return false;
  }
  
  void startHistoryEnumeration(int paramInt1, int paramInt2, int paramInt3)
  {
    mHistory = null;
    try
    {
      mHistory = mSession.getHistoryIntervalForUid(mTemplate, paramInt1, Bucket.convertSet(paramInt3), paramInt2, -1, mStartTimeStamp, mEndTimeStamp);
      setSingleUidTagState(paramInt1, paramInt2, paramInt3);
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("NetworkStats", localRemoteException);
    }
    mEnumerationIndex = 0;
  }
  
  void startSummaryEnumeration()
    throws RemoteException
  {
    mSummary = mSession.getSummaryForAllUid(mTemplate, mStartTimeStamp, mEndTimeStamp, false);
    mEnumerationIndex = 0;
  }
  
  void startUserUidEnumeration()
    throws RemoteException
  {
    int[] arrayOfInt = mSession.getRelevantUids();
    IntArray localIntArray = new IntArray(arrayOfInt.length);
    int i = arrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      int k = arrayOfInt[j];
      try
      {
        localObject = mSession.getHistoryIntervalForUid(mTemplate, k, -1, 0, -1, mStartTimeStamp, mEndTimeStamp);
        if ((localObject != null) && (((NetworkStatsHistory)localObject).size() > 0)) {
          localIntArray.add(k);
        }
      }
      catch (RemoteException localRemoteException)
      {
        Object localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Error while getting history of uid ");
        ((StringBuilder)localObject).append(k);
        Log.w("NetworkStats", ((StringBuilder)localObject).toString(), localRemoteException);
      }
    }
    mUids = localIntArray.toArray();
    mUidOrUidIndex = -1;
    stepHistory();
  }
  
  public static class Bucket
  {
    public static final int DEFAULT_NETWORK_ALL = -1;
    public static final int DEFAULT_NETWORK_NO = 1;
    public static final int DEFAULT_NETWORK_YES = 2;
    public static final int METERED_ALL = -1;
    public static final int METERED_NO = 1;
    public static final int METERED_YES = 2;
    public static final int ROAMING_ALL = -1;
    public static final int ROAMING_NO = 1;
    public static final int ROAMING_YES = 2;
    public static final int STATE_ALL = -1;
    public static final int STATE_DEFAULT = 1;
    public static final int STATE_FOREGROUND = 2;
    public static final int TAG_NONE = 0;
    public static final int UID_ALL = -1;
    public static final int UID_REMOVED = -4;
    public static final int UID_TETHERING = -5;
    private long mBeginTimeStamp;
    private int mDefaultNetworkStatus;
    private long mEndTimeStamp;
    private int mMetered;
    private int mRoaming;
    private long mRxBytes;
    private long mRxPackets;
    private int mState;
    private int mTag;
    private long mTxBytes;
    private long mTxPackets;
    private int mUid;
    
    public Bucket() {}
    
    private static int convertDefaultNetworkStatus(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return 0;
      case 1: 
        return 2;
      case 0: 
        return 1;
      }
      return -1;
    }
    
    private static int convertMetered(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return 0;
      case 1: 
        return 2;
      case 0: 
        return 1;
      }
      return -1;
    }
    
    private static int convertRoaming(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return 0;
      case 1: 
        return 2;
      case 0: 
        return 1;
      }
      return -1;
    }
    
    private static int convertSet(int paramInt)
    {
      if (paramInt != -1)
      {
        switch (paramInt)
        {
        default: 
          return 0;
        case 2: 
          return 1;
        }
        return 0;
      }
      return -1;
    }
    
    private static int convertState(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return 0;
      case 1: 
        return 2;
      case 0: 
        return 1;
      }
      return -1;
    }
    
    private static int convertTag(int paramInt)
    {
      if (paramInt != 0) {
        return paramInt;
      }
      return 0;
    }
    
    private static int convertUid(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return paramInt;
      case -4: 
        return -4;
      }
      return -5;
    }
    
    public int getDefaultNetworkStatus()
    {
      return mDefaultNetworkStatus;
    }
    
    public long getEndTimeStamp()
    {
      return mEndTimeStamp;
    }
    
    public int getMetered()
    {
      return mMetered;
    }
    
    public int getRoaming()
    {
      return mRoaming;
    }
    
    public long getRxBytes()
    {
      return mRxBytes;
    }
    
    public long getRxPackets()
    {
      return mRxPackets;
    }
    
    public long getStartTimeStamp()
    {
      return mBeginTimeStamp;
    }
    
    public int getState()
    {
      return mState;
    }
    
    public int getTag()
    {
      return mTag;
    }
    
    public long getTxBytes()
    {
      return mTxBytes;
    }
    
    public long getTxPackets()
    {
      return mTxPackets;
    }
    
    public int getUid()
    {
      return mUid;
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface DefaultNetworkStatus {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Metered {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Roaming {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface State {}
  }
}
