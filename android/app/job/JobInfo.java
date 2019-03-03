package android.app.job;

import android.content.ClipData;
import android.content.ComponentName;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkRequest.Builder;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TimeUtils;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class JobInfo
  implements Parcelable
{
  public static final int BACKOFF_POLICY_EXPONENTIAL = 1;
  public static final int BACKOFF_POLICY_LINEAR = 0;
  public static final int CONSTRAINT_FLAG_BATTERY_NOT_LOW = 2;
  public static final int CONSTRAINT_FLAG_CHARGING = 1;
  public static final int CONSTRAINT_FLAG_DEVICE_IDLE = 4;
  public static final int CONSTRAINT_FLAG_STORAGE_NOT_LOW = 8;
  public static final Parcelable.Creator<JobInfo> CREATOR = new Parcelable.Creator()
  {
    public JobInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new JobInfo(paramAnonymousParcel, null);
    }
    
    public JobInfo[] newArray(int paramAnonymousInt)
    {
      return new JobInfo[paramAnonymousInt];
    }
  };
  public static final int DEFAULT_BACKOFF_POLICY = 1;
  public static final long DEFAULT_INITIAL_BACKOFF_MILLIS = 30000L;
  public static final int FLAG_EXEMPT_FROM_APP_STANDBY = 8;
  public static final int FLAG_IMPORTANT_WHILE_FOREGROUND = 2;
  public static final int FLAG_PREFETCH = 4;
  public static final int FLAG_WILL_BE_FOREGROUND = 1;
  public static final long MAX_BACKOFF_DELAY_MILLIS = 18000000L;
  public static final long MIN_BACKOFF_MILLIS = 10000L;
  private static final long MIN_FLEX_MILLIS = 300000L;
  private static final long MIN_PERIOD_MILLIS = 900000L;
  public static final int NETWORK_BYTES_UNKNOWN = -1;
  public static final int NETWORK_TYPE_ANY = 1;
  public static final int NETWORK_TYPE_CELLULAR = 4;
  @Deprecated
  public static final int NETWORK_TYPE_METERED = 4;
  public static final int NETWORK_TYPE_NONE = 0;
  public static final int NETWORK_TYPE_NOT_ROAMING = 3;
  public static final int NETWORK_TYPE_UNMETERED = 2;
  public static final int PRIORITY_ADJ_ALWAYS_RUNNING = -80;
  public static final int PRIORITY_ADJ_OFTEN_RUNNING = -40;
  public static final int PRIORITY_DEFAULT = 0;
  public static final int PRIORITY_FOREGROUND_APP = 30;
  public static final int PRIORITY_SYNC_EXPEDITED = 10;
  public static final int PRIORITY_SYNC_INITIALIZATION = 20;
  public static final int PRIORITY_TOP_APP = 40;
  private static String TAG = "JobInfo";
  private final int backoffPolicy;
  private final ClipData clipData;
  private final int clipGrantFlags;
  private final int constraintFlags;
  private final PersistableBundle extras;
  private final int flags;
  private final long flexMillis;
  private final boolean hasEarlyConstraint;
  private final boolean hasLateConstraint;
  private final long initialBackoffMillis;
  private final long intervalMillis;
  private final boolean isPeriodic;
  private final boolean isPersisted;
  private final int jobId;
  private final long maxExecutionDelayMillis;
  private final long minLatencyMillis;
  private final long networkDownloadBytes;
  private final NetworkRequest networkRequest;
  private final long networkUploadBytes;
  private final int priority;
  private final ComponentName service;
  private final Bundle transientExtras;
  private final long triggerContentMaxDelay;
  private final long triggerContentUpdateDelay;
  private final TriggerContentUri[] triggerContentUris;
  
  private JobInfo(Builder paramBuilder)
  {
    jobId = mJobId;
    extras = mExtras.deepCopy();
    transientExtras = mTransientExtras.deepCopy();
    clipData = mClipData;
    clipGrantFlags = mClipGrantFlags;
    service = mJobService;
    constraintFlags = mConstraintFlags;
    TriggerContentUri[] arrayOfTriggerContentUri;
    if (mTriggerContentUris != null) {
      arrayOfTriggerContentUri = (TriggerContentUri[])mTriggerContentUris.toArray(new TriggerContentUri[mTriggerContentUris.size()]);
    } else {
      arrayOfTriggerContentUri = null;
    }
    triggerContentUris = arrayOfTriggerContentUri;
    triggerContentUpdateDelay = mTriggerContentUpdateDelay;
    triggerContentMaxDelay = mTriggerContentMaxDelay;
    networkRequest = mNetworkRequest;
    networkDownloadBytes = mNetworkDownloadBytes;
    networkUploadBytes = mNetworkUploadBytes;
    minLatencyMillis = mMinLatencyMillis;
    maxExecutionDelayMillis = mMaxExecutionDelayMillis;
    isPeriodic = mIsPeriodic;
    isPersisted = mIsPersisted;
    intervalMillis = mIntervalMillis;
    flexMillis = mFlexMillis;
    initialBackoffMillis = mInitialBackoffMillis;
    backoffPolicy = mBackoffPolicy;
    hasEarlyConstraint = mHasEarlyConstraint;
    hasLateConstraint = mHasLateConstraint;
    priority = mPriority;
    flags = mFlags;
  }
  
  private JobInfo(Parcel paramParcel)
  {
    jobId = paramParcel.readInt();
    extras = paramParcel.readPersistableBundle();
    transientExtras = paramParcel.readBundle();
    if (paramParcel.readInt() != 0)
    {
      clipData = ((ClipData)ClipData.CREATOR.createFromParcel(paramParcel));
      clipGrantFlags = paramParcel.readInt();
    }
    else
    {
      clipData = null;
      clipGrantFlags = 0;
    }
    service = ((ComponentName)paramParcel.readParcelable(null));
    constraintFlags = paramParcel.readInt();
    triggerContentUris = ((TriggerContentUri[])paramParcel.createTypedArray(TriggerContentUri.CREATOR));
    triggerContentUpdateDelay = paramParcel.readLong();
    triggerContentMaxDelay = paramParcel.readLong();
    if (paramParcel.readInt() != 0) {
      networkRequest = ((NetworkRequest)NetworkRequest.CREATOR.createFromParcel(paramParcel));
    } else {
      networkRequest = null;
    }
    networkDownloadBytes = paramParcel.readLong();
    networkUploadBytes = paramParcel.readLong();
    minLatencyMillis = paramParcel.readLong();
    maxExecutionDelayMillis = paramParcel.readLong();
    int i = paramParcel.readInt();
    boolean bool1 = true;
    boolean bool2;
    if (i == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    isPeriodic = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    isPersisted = bool2;
    intervalMillis = paramParcel.readLong();
    flexMillis = paramParcel.readLong();
    initialBackoffMillis = paramParcel.readLong();
    backoffPolicy = paramParcel.readInt();
    if (paramParcel.readInt() == 1) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    hasEarlyConstraint = bool2;
    if (paramParcel.readInt() == 1) {
      bool2 = bool1;
    } else {
      bool2 = false;
    }
    hasLateConstraint = bool2;
    priority = paramParcel.readInt();
    flags = paramParcel.readInt();
  }
  
  public static final long getMinBackoffMillis()
  {
    return 10000L;
  }
  
  public static final long getMinFlexMillis()
  {
    return 300000L;
  }
  
  public static final long getMinPeriodMillis()
  {
    return 900000L;
  }
  
  private static boolean kindofEqualsBundle(BaseBundle paramBaseBundle1, BaseBundle paramBaseBundle2)
  {
    boolean bool;
    if ((paramBaseBundle1 != paramBaseBundle2) && ((paramBaseBundle1 == null) || (!paramBaseBundle1.kindofEquals(paramBaseBundle2)))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof JobInfo)) {
      return false;
    }
    paramObject = (JobInfo)paramObject;
    if (jobId != jobId) {
      return false;
    }
    if (!kindofEqualsBundle(extras, extras)) {
      return false;
    }
    if (!kindofEqualsBundle(transientExtras, transientExtras)) {
      return false;
    }
    if (clipData != clipData) {
      return false;
    }
    if (clipGrantFlags != clipGrantFlags) {
      return false;
    }
    if (!Objects.equals(service, service)) {
      return false;
    }
    if (constraintFlags != constraintFlags) {
      return false;
    }
    if (!Arrays.equals(triggerContentUris, triggerContentUris)) {
      return false;
    }
    if (triggerContentUpdateDelay != triggerContentUpdateDelay) {
      return false;
    }
    if (triggerContentMaxDelay != triggerContentMaxDelay) {
      return false;
    }
    if (hasEarlyConstraint != hasEarlyConstraint) {
      return false;
    }
    if (hasLateConstraint != hasLateConstraint) {
      return false;
    }
    if (!Objects.equals(networkRequest, networkRequest)) {
      return false;
    }
    if (networkDownloadBytes != networkDownloadBytes) {
      return false;
    }
    if (networkUploadBytes != networkUploadBytes) {
      return false;
    }
    if (minLatencyMillis != minLatencyMillis) {
      return false;
    }
    if (maxExecutionDelayMillis != maxExecutionDelayMillis) {
      return false;
    }
    if (isPeriodic != isPeriodic) {
      return false;
    }
    if (isPersisted != isPersisted) {
      return false;
    }
    if (intervalMillis != intervalMillis) {
      return false;
    }
    if (flexMillis != flexMillis) {
      return false;
    }
    if (initialBackoffMillis != initialBackoffMillis) {
      return false;
    }
    if (backoffPolicy != backoffPolicy) {
      return false;
    }
    if (priority != priority) {
      return false;
    }
    return flags == flags;
  }
  
  public int getBackoffPolicy()
  {
    return backoffPolicy;
  }
  
  public ClipData getClipData()
  {
    return clipData;
  }
  
  public int getClipGrantFlags()
  {
    return clipGrantFlags;
  }
  
  public int getConstraintFlags()
  {
    return constraintFlags;
  }
  
  @Deprecated
  public long getEstimatedNetworkBytes()
  {
    if ((networkDownloadBytes == -1L) && (networkUploadBytes == -1L)) {
      return -1L;
    }
    if (networkDownloadBytes == -1L) {
      return networkUploadBytes;
    }
    if (networkUploadBytes == -1L) {
      return networkDownloadBytes;
    }
    return networkDownloadBytes + networkUploadBytes;
  }
  
  public long getEstimatedNetworkDownloadBytes()
  {
    return networkDownloadBytes;
  }
  
  public long getEstimatedNetworkUploadBytes()
  {
    return networkUploadBytes;
  }
  
  public PersistableBundle getExtras()
  {
    return extras;
  }
  
  public int getFlags()
  {
    return flags;
  }
  
  public long getFlexMillis()
  {
    return flexMillis;
  }
  
  public int getId()
  {
    return jobId;
  }
  
  public long getInitialBackoffMillis()
  {
    return initialBackoffMillis;
  }
  
  public long getIntervalMillis()
  {
    return intervalMillis;
  }
  
  public long getMaxExecutionDelayMillis()
  {
    return maxExecutionDelayMillis;
  }
  
  public long getMinLatencyMillis()
  {
    return minLatencyMillis;
  }
  
  @Deprecated
  public int getNetworkType()
  {
    if (networkRequest == null) {
      return 0;
    }
    if (networkRequest.networkCapabilities.hasCapability(11)) {
      return 2;
    }
    if (networkRequest.networkCapabilities.hasCapability(18)) {
      return 3;
    }
    if (networkRequest.networkCapabilities.hasTransport(0)) {
      return 4;
    }
    return 1;
  }
  
  public int getPriority()
  {
    return priority;
  }
  
  public NetworkRequest getRequiredNetwork()
  {
    return networkRequest;
  }
  
  public ComponentName getService()
  {
    return service;
  }
  
  public Bundle getTransientExtras()
  {
    return transientExtras;
  }
  
  public long getTriggerContentMaxDelay()
  {
    return triggerContentMaxDelay;
  }
  
  public long getTriggerContentUpdateDelay()
  {
    return triggerContentUpdateDelay;
  }
  
  public TriggerContentUri[] getTriggerContentUris()
  {
    return triggerContentUris;
  }
  
  public boolean hasEarlyConstraint()
  {
    return hasEarlyConstraint;
  }
  
  public boolean hasLateConstraint()
  {
    return hasLateConstraint;
  }
  
  public int hashCode()
  {
    int i = jobId;
    int j = i;
    if (extras != null) {
      j = 31 * i + extras.hashCode();
    }
    i = j;
    if (transientExtras != null) {
      i = 31 * j + transientExtras.hashCode();
    }
    j = i;
    if (clipData != null) {
      j = 31 * i + clipData.hashCode();
    }
    j = 31 * j + clipGrantFlags;
    i = j;
    if (service != null) {
      i = 31 * j + service.hashCode();
    }
    j = 31 * i + constraintFlags;
    i = j;
    if (triggerContentUris != null) {
      i = 31 * j + Arrays.hashCode(triggerContentUris);
    }
    j = 31 * (31 * (31 * (31 * i + Long.hashCode(triggerContentUpdateDelay)) + Long.hashCode(triggerContentMaxDelay)) + Boolean.hashCode(hasEarlyConstraint)) + Boolean.hashCode(hasLateConstraint);
    i = j;
    if (networkRequest != null) {
      i = 31 * j + networkRequest.hashCode();
    }
    return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * i + Long.hashCode(networkDownloadBytes)) + Long.hashCode(networkUploadBytes)) + Long.hashCode(minLatencyMillis)) + Long.hashCode(maxExecutionDelayMillis)) + Boolean.hashCode(isPeriodic)) + Boolean.hashCode(isPersisted)) + Long.hashCode(intervalMillis)) + Long.hashCode(flexMillis)) + Long.hashCode(initialBackoffMillis)) + backoffPolicy) + priority) + flags;
  }
  
  public boolean isExemptedFromAppStandby()
  {
    boolean bool;
    if (((flags & 0x8) != 0) && (!isPeriodic())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isImportantWhileForeground()
  {
    boolean bool;
    if ((flags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPeriodic()
  {
    return isPeriodic;
  }
  
  public boolean isPersisted()
  {
    return isPersisted;
  }
  
  public boolean isPrefetch()
  {
    boolean bool;
    if ((flags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRequireBatteryNotLow()
  {
    boolean bool;
    if ((constraintFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRequireCharging()
  {
    int i = constraintFlags;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRequireDeviceIdle()
  {
    boolean bool;
    if ((constraintFlags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRequireStorageNotLow()
  {
    boolean bool;
    if ((constraintFlags & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("(job:");
    localStringBuilder.append(jobId);
    localStringBuilder.append("/");
    localStringBuilder.append(service.flattenToShortString());
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(jobId);
    paramParcel.writePersistableBundle(extras);
    paramParcel.writeBundle(transientExtras);
    if (clipData != null)
    {
      paramParcel.writeInt(1);
      clipData.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(clipGrantFlags);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeParcelable(service, paramInt);
    paramParcel.writeInt(constraintFlags);
    paramParcel.writeTypedArray(triggerContentUris, paramInt);
    paramParcel.writeLong(triggerContentUpdateDelay);
    paramParcel.writeLong(triggerContentMaxDelay);
    if (networkRequest != null)
    {
      paramParcel.writeInt(1);
      networkRequest.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeLong(networkDownloadBytes);
    paramParcel.writeLong(networkUploadBytes);
    paramParcel.writeLong(minLatencyMillis);
    paramParcel.writeLong(maxExecutionDelayMillis);
    paramParcel.writeInt(isPeriodic);
    paramParcel.writeInt(isPersisted);
    paramParcel.writeLong(intervalMillis);
    paramParcel.writeLong(flexMillis);
    paramParcel.writeLong(initialBackoffMillis);
    paramParcel.writeInt(backoffPolicy);
    paramParcel.writeInt(hasEarlyConstraint);
    paramParcel.writeInt(hasLateConstraint);
    paramParcel.writeInt(priority);
    paramParcel.writeInt(flags);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BackoffPolicy {}
  
  public static final class Builder
  {
    private int mBackoffPolicy = 1;
    private boolean mBackoffPolicySet = false;
    private ClipData mClipData;
    private int mClipGrantFlags;
    private int mConstraintFlags;
    private PersistableBundle mExtras = PersistableBundle.EMPTY;
    private int mFlags;
    private long mFlexMillis;
    private boolean mHasEarlyConstraint;
    private boolean mHasLateConstraint;
    private long mInitialBackoffMillis = 30000L;
    private long mIntervalMillis;
    private boolean mIsPeriodic;
    private boolean mIsPersisted;
    private final int mJobId;
    private final ComponentName mJobService;
    private long mMaxExecutionDelayMillis;
    private long mMinLatencyMillis;
    private long mNetworkDownloadBytes = -1L;
    private NetworkRequest mNetworkRequest;
    private long mNetworkUploadBytes = -1L;
    private int mPriority = 0;
    private Bundle mTransientExtras = Bundle.EMPTY;
    private long mTriggerContentMaxDelay = -1L;
    private long mTriggerContentUpdateDelay = -1L;
    private ArrayList<JobInfo.TriggerContentUri> mTriggerContentUris;
    
    public Builder(int paramInt, ComponentName paramComponentName)
    {
      mJobService = paramComponentName;
      mJobId = paramInt;
    }
    
    public Builder addTriggerContentUri(JobInfo.TriggerContentUri paramTriggerContentUri)
    {
      if (mTriggerContentUris == null) {
        mTriggerContentUris = new ArrayList();
      }
      mTriggerContentUris.add(paramTriggerContentUri);
      return this;
    }
    
    public JobInfo build()
    {
      if ((!mHasEarlyConstraint) && (!mHasLateConstraint) && (mConstraintFlags == 0) && (mNetworkRequest == null) && (mTriggerContentUris == null)) {
        throw new IllegalArgumentException("You're trying to build a job with no constraints, this is not allowed.");
      }
      if (((mNetworkDownloadBytes <= 0L) && (mNetworkUploadBytes <= 0L)) || (mNetworkRequest != null))
      {
        if ((mIsPersisted) && (mNetworkRequest != null) && (mNetworkRequest.networkCapabilities.getNetworkSpecifier() != null)) {
          throw new IllegalArgumentException("Network specifiers aren't supported for persistent jobs");
        }
        if (mIsPeriodic) {
          if (mMaxExecutionDelayMillis == 0L)
          {
            if (mMinLatencyMillis == 0L)
            {
              if (mTriggerContentUris != null) {
                throw new IllegalArgumentException("Can't call addTriggerContentUri() on a periodic job");
              }
            }
            else {
              throw new IllegalArgumentException("Can't call setMinimumLatency() on a periodic job");
            }
          }
          else {
            throw new IllegalArgumentException("Can't call setOverrideDeadline() on a periodic job.");
          }
        }
        if (mIsPersisted) {
          if (mTriggerContentUris == null)
          {
            if (mTransientExtras.isEmpty())
            {
              if (mClipData != null) {
                throw new IllegalArgumentException("Can't call setClipData() on a persisted job");
              }
            }
            else {
              throw new IllegalArgumentException("Can't call setTransientExtras() on a persisted job");
            }
          }
          else {
            throw new IllegalArgumentException("Can't call addTriggerContentUri() on a persisted job");
          }
        }
        if (((mFlags & 0x2) != 0) && (mHasEarlyConstraint)) {
          throw new IllegalArgumentException("An important while foreground job cannot have a time delay");
        }
        if ((mBackoffPolicySet) && ((mConstraintFlags & 0x4) != 0)) {
          throw new IllegalArgumentException("An idle mode job will not respect any back-off policy, so calling setBackoffCriteria with setRequiresDeviceIdle is an error.");
        }
        return new JobInfo(this, null);
      }
      throw new IllegalArgumentException("Can't provide estimated network usage without requiring a network");
    }
    
    public Builder setBackoffCriteria(long paramLong, int paramInt)
    {
      long l1 = JobInfo.getMinBackoffMillis();
      long l2 = paramLong;
      if (paramLong < l1)
      {
        String str = JobInfo.TAG;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Requested backoff ");
        localStringBuilder.append(TimeUtils.formatDuration(paramLong));
        localStringBuilder.append(" for job ");
        localStringBuilder.append(mJobId);
        localStringBuilder.append(" is too small; raising to ");
        localStringBuilder.append(TimeUtils.formatDuration(l1));
        Log.w(str, localStringBuilder.toString());
        l2 = l1;
      }
      mBackoffPolicySet = true;
      mInitialBackoffMillis = l2;
      mBackoffPolicy = paramInt;
      return this;
    }
    
    public Builder setClipData(ClipData paramClipData, int paramInt)
    {
      mClipData = paramClipData;
      mClipGrantFlags = paramInt;
      return this;
    }
    
    @Deprecated
    public Builder setEstimatedNetworkBytes(long paramLong)
    {
      return setEstimatedNetworkBytes(paramLong, -1L);
    }
    
    public Builder setEstimatedNetworkBytes(long paramLong1, long paramLong2)
    {
      mNetworkDownloadBytes = paramLong1;
      mNetworkUploadBytes = paramLong2;
      return this;
    }
    
    public Builder setExtras(PersistableBundle paramPersistableBundle)
    {
      mExtras = paramPersistableBundle;
      return this;
    }
    
    public Builder setFlags(int paramInt)
    {
      mFlags = paramInt;
      return this;
    }
    
    public Builder setImportantWhileForeground(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x2;
      } else {
        mFlags &= 0xFFFFFFFD;
      }
      return this;
    }
    
    @Deprecated
    public Builder setIsPrefetch(boolean paramBoolean)
    {
      return setPrefetch(paramBoolean);
    }
    
    public Builder setMinimumLatency(long paramLong)
    {
      mMinLatencyMillis = paramLong;
      mHasEarlyConstraint = true;
      return this;
    }
    
    public Builder setOverrideDeadline(long paramLong)
    {
      mMaxExecutionDelayMillis = paramLong;
      mHasLateConstraint = true;
      return this;
    }
    
    public Builder setPeriodic(long paramLong)
    {
      return setPeriodic(paramLong, paramLong);
    }
    
    public Builder setPeriodic(long paramLong1, long paramLong2)
    {
      long l1 = JobInfo.getMinPeriodMillis();
      long l2 = paramLong1;
      Object localObject1;
      Object localObject2;
      if (paramLong1 < l1)
      {
        localObject1 = JobInfo.TAG;
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Requested interval ");
        ((StringBuilder)localObject2).append(TimeUtils.formatDuration(paramLong1));
        ((StringBuilder)localObject2).append(" for job ");
        ((StringBuilder)localObject2).append(mJobId);
        ((StringBuilder)localObject2).append(" is too small; raising to ");
        ((StringBuilder)localObject2).append(TimeUtils.formatDuration(l1));
        Log.w((String)localObject1, ((StringBuilder)localObject2).toString());
        l2 = l1;
      }
      l1 = Math.max(5L * l2 / 100L, JobInfo.getMinFlexMillis());
      paramLong1 = paramLong2;
      if (paramLong2 < l1)
      {
        localObject2 = JobInfo.TAG;
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Requested flex ");
        ((StringBuilder)localObject1).append(TimeUtils.formatDuration(paramLong2));
        ((StringBuilder)localObject1).append(" for job ");
        ((StringBuilder)localObject1).append(mJobId);
        ((StringBuilder)localObject1).append(" is too small; raising to ");
        ((StringBuilder)localObject1).append(TimeUtils.formatDuration(l1));
        Log.w((String)localObject2, ((StringBuilder)localObject1).toString());
        paramLong1 = l1;
      }
      mIsPeriodic = true;
      mIntervalMillis = l2;
      mFlexMillis = paramLong1;
      mHasLateConstraint = true;
      mHasEarlyConstraint = true;
      return this;
    }
    
    public Builder setPersisted(boolean paramBoolean)
    {
      mIsPersisted = paramBoolean;
      return this;
    }
    
    public Builder setPrefetch(boolean paramBoolean)
    {
      if (paramBoolean) {
        mFlags |= 0x4;
      } else {
        mFlags &= 0xFFFFFFFB;
      }
      return this;
    }
    
    public Builder setPriority(int paramInt)
    {
      mPriority = paramInt;
      return this;
    }
    
    public Builder setRequiredNetwork(NetworkRequest paramNetworkRequest)
    {
      mNetworkRequest = paramNetworkRequest;
      return this;
    }
    
    public Builder setRequiredNetworkType(int paramInt)
    {
      if (paramInt == 0) {
        return setRequiredNetwork(null);
      }
      NetworkRequest.Builder localBuilder = new NetworkRequest.Builder();
      localBuilder.addCapability(12);
      localBuilder.addCapability(16);
      localBuilder.removeCapability(15);
      if (paramInt != 1) {
        if (paramInt == 2) {
          localBuilder.addCapability(11);
        } else if (paramInt == 3) {
          localBuilder.addCapability(18);
        } else if (paramInt == 4) {
          localBuilder.addTransportType(0);
        }
      }
      return setRequiredNetwork(localBuilder.build());
    }
    
    public Builder setRequiresBatteryNotLow(boolean paramBoolean)
    {
      int i = mConstraintFlags;
      int j;
      if (paramBoolean) {
        j = 2;
      } else {
        j = 0;
      }
      mConstraintFlags = (i & 0xFFFFFFFD | j);
      return this;
    }
    
    public Builder setRequiresCharging(boolean paramBoolean)
    {
      mConstraintFlags = (mConstraintFlags & 0xFFFFFFFE | paramBoolean);
      return this;
    }
    
    public Builder setRequiresDeviceIdle(boolean paramBoolean)
    {
      int i = mConstraintFlags;
      int j;
      if (paramBoolean) {
        j = 4;
      } else {
        j = 0;
      }
      mConstraintFlags = (i & 0xFFFFFFFB | j);
      return this;
    }
    
    public Builder setRequiresStorageNotLow(boolean paramBoolean)
    {
      int i = mConstraintFlags;
      int j;
      if (paramBoolean) {
        j = 8;
      } else {
        j = 0;
      }
      mConstraintFlags = (i & 0xFFFFFFF7 | j);
      return this;
    }
    
    public Builder setTransientExtras(Bundle paramBundle)
    {
      mTransientExtras = paramBundle;
      return this;
    }
    
    public Builder setTriggerContentMaxDelay(long paramLong)
    {
      mTriggerContentMaxDelay = paramLong;
      return this;
    }
    
    public Builder setTriggerContentUpdateDelay(long paramLong)
    {
      mTriggerContentUpdateDelay = paramLong;
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface NetworkType {}
  
  public static final class TriggerContentUri
    implements Parcelable
  {
    public static final Parcelable.Creator<TriggerContentUri> CREATOR = new Parcelable.Creator()
    {
      public JobInfo.TriggerContentUri createFromParcel(Parcel paramAnonymousParcel)
      {
        return new JobInfo.TriggerContentUri(paramAnonymousParcel, null);
      }
      
      public JobInfo.TriggerContentUri[] newArray(int paramAnonymousInt)
      {
        return new JobInfo.TriggerContentUri[paramAnonymousInt];
      }
    };
    public static final int FLAG_NOTIFY_FOR_DESCENDANTS = 1;
    private final int mFlags;
    private final Uri mUri;
    
    public TriggerContentUri(Uri paramUri, int paramInt)
    {
      mUri = paramUri;
      mFlags = paramInt;
    }
    
    private TriggerContentUri(Parcel paramParcel)
    {
      mUri = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
      mFlags = paramParcel.readInt();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof TriggerContentUri;
      boolean bool2 = false;
      if (!bool1) {
        return false;
      }
      paramObject = (TriggerContentUri)paramObject;
      bool1 = bool2;
      if (Objects.equals(mUri, mUri))
      {
        bool1 = bool2;
        if (mFlags == mFlags) {
          bool1 = true;
        }
      }
      return bool1;
    }
    
    public int getFlags()
    {
      return mFlags;
    }
    
    public Uri getUri()
    {
      return mUri;
    }
    
    public int hashCode()
    {
      int i;
      if (mUri == null) {
        i = 0;
      } else {
        i = mUri.hashCode();
      }
      return i ^ mFlags;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      mUri.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(mFlags);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Flags {}
  }
}
