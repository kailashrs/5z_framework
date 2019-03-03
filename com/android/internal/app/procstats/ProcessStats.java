package com.android.internal.app.procstats;

import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.LongSparseArray;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.ProcessMap;
import dalvik.system.VMRuntime;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ProcessStats
  implements Parcelable
{
  public static final int ADD_PSS_EXTERNAL = 3;
  public static final int ADD_PSS_EXTERNAL_SLOW = 4;
  public static final int ADD_PSS_INTERNAL_ALL_MEM = 1;
  public static final int ADD_PSS_INTERNAL_ALL_POLL = 2;
  public static final int ADD_PSS_INTERNAL_SINGLE = 0;
  public static final int ADJ_COUNT = 8;
  public static final int ADJ_MEM_FACTOR_COUNT = 4;
  public static final int ADJ_MEM_FACTOR_CRITICAL = 3;
  public static final int ADJ_MEM_FACTOR_LOW = 2;
  public static final int ADJ_MEM_FACTOR_MODERATE = 1;
  public static final int ADJ_MEM_FACTOR_NORMAL = 0;
  public static final int ADJ_NOTHING = -1;
  public static final int ADJ_SCREEN_MOD = 4;
  public static final int ADJ_SCREEN_OFF = 0;
  public static final int ADJ_SCREEN_ON = 4;
  public static final int[] ALL_MEM_ADJ;
  public static final int[] ALL_PROC_STATES;
  public static final int[] ALL_SCREEN_ADJ;
  public static final int[] BACKGROUND_PROC_STATES;
  static final int[] BAD_TABLE = new int[0];
  public static long COMMIT_PERIOD = 10800000L;
  public static long COMMIT_UPTIME_PERIOD = 3600000L;
  public static final Parcelable.Creator<ProcessStats> CREATOR;
  static final boolean DEBUG = false;
  static final boolean DEBUG_PARCEL = false;
  public static final int FLAG_COMPLETE = 1;
  public static final int FLAG_SHUTDOWN = 2;
  public static final int FLAG_SYSPROPS = 4;
  private static final int MAGIC = 1347638356;
  public static final int[] NON_CACHED_PROC_STATES;
  private static final int PARCEL_VERSION = 27;
  public static final int PSS_AVERAGE = 2;
  public static final int PSS_COUNT = 10;
  public static final int PSS_MAXIMUM = 3;
  public static final int PSS_MINIMUM = 1;
  public static final int PSS_RSS_AVERAGE = 8;
  public static final int PSS_RSS_MAXIMUM = 9;
  public static final int PSS_RSS_MINIMUM = 7;
  public static final int PSS_SAMPLE_COUNT = 0;
  public static final int PSS_USS_AVERAGE = 5;
  public static final int PSS_USS_MAXIMUM = 6;
  public static final int PSS_USS_MINIMUM = 4;
  public static final String SERVICE_NAME = "procstats";
  public static final int STATE_BACKUP = 4;
  public static final int STATE_CACHED_ACTIVITY = 11;
  public static final int STATE_CACHED_ACTIVITY_CLIENT = 12;
  public static final int STATE_CACHED_EMPTY = 13;
  public static final int STATE_COUNT = 14;
  public static final int STATE_HEAVY_WEIGHT = 8;
  public static final int STATE_HOME = 9;
  public static final int STATE_IMPORTANT_BACKGROUND = 3;
  public static final int STATE_IMPORTANT_FOREGROUND = 2;
  public static final int STATE_LAST_ACTIVITY = 10;
  public static final int STATE_NOTHING = -1;
  public static final int STATE_PERSISTENT = 0;
  public static final int STATE_RECEIVER = 7;
  public static final int STATE_SERVICE = 5;
  public static final int STATE_SERVICE_RESTARTING = 6;
  public static final int STATE_TOP = 1;
  public static final int SYS_MEM_USAGE_CACHED_AVERAGE = 2;
  public static final int SYS_MEM_USAGE_CACHED_MAXIMUM = 3;
  public static final int SYS_MEM_USAGE_CACHED_MINIMUM = 1;
  public static final int SYS_MEM_USAGE_COUNT = 16;
  public static final int SYS_MEM_USAGE_FREE_AVERAGE = 5;
  public static final int SYS_MEM_USAGE_FREE_MAXIMUM = 6;
  public static final int SYS_MEM_USAGE_FREE_MINIMUM = 4;
  public static final int SYS_MEM_USAGE_KERNEL_AVERAGE = 11;
  public static final int SYS_MEM_USAGE_KERNEL_MAXIMUM = 12;
  public static final int SYS_MEM_USAGE_KERNEL_MINIMUM = 10;
  public static final int SYS_MEM_USAGE_NATIVE_AVERAGE = 14;
  public static final int SYS_MEM_USAGE_NATIVE_MAXIMUM = 15;
  public static final int SYS_MEM_USAGE_NATIVE_MINIMUM = 13;
  public static final int SYS_MEM_USAGE_SAMPLE_COUNT = 0;
  public static final int SYS_MEM_USAGE_ZRAM_AVERAGE = 8;
  public static final int SYS_MEM_USAGE_ZRAM_MAXIMUM = 9;
  public static final int SYS_MEM_USAGE_ZRAM_MINIMUM = 7;
  public static final String TAG = "ProcessStats";
  private static final Pattern sPageTypeRegex;
  ArrayMap<String, Integer> mCommonStringToIndex;
  public long mExternalPssCount;
  public long mExternalPssTime;
  public long mExternalSlowPssCount;
  public long mExternalSlowPssTime;
  public int mFlags;
  boolean mHasSwappedOutPss;
  ArrayList<String> mIndexToCommonString;
  public long mInternalAllMemPssCount;
  public long mInternalAllMemPssTime;
  public long mInternalAllPollPssCount;
  public long mInternalAllPollPssTime;
  public long mInternalSinglePssCount;
  public long mInternalSinglePssTime;
  public int mMemFactor = -1;
  public final long[] mMemFactorDurations = new long[8];
  public final ProcessMap<LongSparseArray<PackageState>> mPackages = new ProcessMap();
  private final ArrayList<String> mPageTypeLabels = new ArrayList();
  private final ArrayList<int[]> mPageTypeSizes = new ArrayList();
  private final ArrayList<Integer> mPageTypeZones = new ArrayList();
  public final ProcessMap<ProcessState> mProcesses = new ProcessMap();
  public String mReadError;
  boolean mRunning;
  String mRuntime;
  public long mStartTime;
  public final SysMemUsageTable mSysMemUsage = new SysMemUsageTable(mTableData);
  public final long[] mSysMemUsageArgs = new long[16];
  public final SparseMappingTable mTableData = new SparseMappingTable();
  public long mTimePeriodEndRealtime;
  public long mTimePeriodEndUptime;
  public long mTimePeriodStartClock;
  public String mTimePeriodStartClockStr;
  public long mTimePeriodStartRealtime;
  public long mTimePeriodStartUptime;
  
  static
  {
    ALL_MEM_ADJ = new int[] { 0, 1, 2, 3 };
    ALL_SCREEN_ADJ = new int[] { 0, 4 };
    NON_CACHED_PROC_STATES = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
    BACKGROUND_PROC_STATES = new int[] { 2, 3, 4, 8, 5, 6, 7 };
    ALL_PROC_STATES = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
    sPageTypeRegex = Pattern.compile("^Node\\s+(\\d+),.*. type\\s+(\\w+)\\s+([\\s\\d]+?)\\s*$");
    CREATOR = new Parcelable.Creator()
    {
      public ProcessStats createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ProcessStats(paramAnonymousParcel);
      }
      
      public ProcessStats[] newArray(int paramAnonymousInt)
      {
        return new ProcessStats[paramAnonymousInt];
      }
    };
  }
  
  public ProcessStats(Parcel paramParcel)
  {
    reset();
    readFromParcel(paramParcel);
  }
  
  public ProcessStats(boolean paramBoolean)
  {
    mRunning = paramBoolean;
    reset();
    if (paramBoolean)
    {
      Debug.MemoryInfo localMemoryInfo = new Debug.MemoryInfo();
      Debug.getMemoryInfo(Process.myPid(), localMemoryInfo);
      mHasSwappedOutPss = localMemoryInfo.hasSwappedOutPss();
    }
  }
  
  private void buildTimePeriodStartClockStr()
  {
    mTimePeriodStartClockStr = DateFormat.format("yyyy-MM-dd-HH-mm-ss", mTimePeriodStartClock).toString();
  }
  
  private void dumpFragmentationLocked(PrintWriter paramPrintWriter)
  {
    paramPrintWriter.println();
    paramPrintWriter.println("Available pages by page size:");
    int i = mPageTypeLabels.size();
    for (int j = 0; j < i; j++)
    {
      paramPrintWriter.format("Zone %3d  %14s ", new Object[] { mPageTypeZones.get(j), mPageTypeLabels.get(j) });
      int[] arrayOfInt = (int[])mPageTypeSizes.get(j);
      int k;
      if (arrayOfInt == null) {
        k = 0;
      } else {
        k = arrayOfInt.length;
      }
      for (int m = 0; m < k; m++) {
        paramPrintWriter.format("%6d", new Object[] { Integer.valueOf(arrayOfInt[m]) });
      }
      paramPrintWriter.println();
    }
  }
  
  private boolean readCheckedInt(Parcel paramParcel, int paramInt, String paramString)
  {
    int i = paramParcel.readInt();
    if (i != paramInt)
    {
      paramParcel = new StringBuilder();
      paramParcel.append("bad ");
      paramParcel.append(paramString);
      paramParcel.append(": ");
      paramParcel.append(i);
      mReadError = paramParcel.toString();
      return false;
    }
    return true;
  }
  
  private String readCommonString(Parcel paramParcel, int paramInt)
  {
    if (paramInt <= 9) {
      return paramParcel.readString();
    }
    paramInt = paramParcel.readInt();
    if (paramInt >= 0) {
      return (String)mIndexToCommonString.get(paramInt);
    }
    paramInt = paramInt;
    paramParcel = paramParcel.readString();
    while (mIndexToCommonString.size() <= paramInt) {
      mIndexToCommonString.add(null);
    }
    mIndexToCommonString.set(paramInt, paramParcel);
    return paramParcel;
  }
  
  private void readCompactedLongArray(Parcel paramParcel, int paramInt1, long[] paramArrayOfLong, int paramInt2)
  {
    if (paramInt1 <= 10)
    {
      paramParcel.readLongArray(paramArrayOfLong);
      return;
    }
    int i = paramArrayOfLong.length;
    if (paramInt2 <= i)
    {
      int j;
      for (paramInt1 = 0;; paramInt1++)
      {
        j = paramInt1;
        if (paramInt1 >= paramInt2) {
          break;
        }
        j = paramParcel.readInt();
        if (j >= 0)
        {
          paramArrayOfLong[paramInt1] = j;
        }
        else
        {
          int k = paramParcel.readInt();
          paramArrayOfLong[paramInt1] = (j << 32 | k);
        }
      }
      while (j < i)
      {
        paramArrayOfLong[j] = 0L;
        j++;
      }
      return;
    }
    paramParcel = new StringBuilder();
    paramParcel.append("bad array lengths: got ");
    paramParcel.append(paramInt2);
    paramParcel.append(" array is ");
    paramParcel.append(i);
    throw new RuntimeException(paramParcel.toString());
  }
  
  static byte[] readFully(InputStream paramInputStream, int[] paramArrayOfInt)
    throws IOException
  {
    int i = 0;
    int j = paramInputStream.available();
    if (j > 0) {
      j++;
    } else {
      j = 16384;
    }
    Object localObject1 = new byte[j];
    j = i;
    for (;;)
    {
      i = paramInputStream.read((byte[])localObject1, j, localObject1.length - j);
      if (i < 0)
      {
        paramArrayOfInt[0] = j;
        return localObject1;
      }
      j += i;
      Object localObject2 = localObject1;
      if (j >= localObject1.length)
      {
        localObject2 = new byte[j + 16384];
        System.arraycopy((byte[])localObject1, 0, (byte[])localObject2, 0, j);
      }
      localObject1 = localObject2;
    }
  }
  
  private void resetCommon()
  {
    mTimePeriodStartClock = System.currentTimeMillis();
    buildTimePeriodStartClockStr();
    long l = SystemClock.elapsedRealtime();
    mTimePeriodEndRealtime = l;
    mTimePeriodStartRealtime = l;
    l = SystemClock.uptimeMillis();
    mTimePeriodEndUptime = l;
    mTimePeriodStartUptime = l;
    mInternalSinglePssCount = 0L;
    mInternalSinglePssTime = 0L;
    mInternalAllMemPssCount = 0L;
    mInternalAllMemPssTime = 0L;
    mInternalAllPollPssCount = 0L;
    mInternalAllPollPssTime = 0L;
    mExternalPssCount = 0L;
    mExternalPssTime = 0L;
    mExternalSlowPssCount = 0L;
    mExternalSlowPssTime = 0L;
    mTableData.reset();
    Arrays.fill(mMemFactorDurations, 0L);
    mSysMemUsage.resetTable();
    mStartTime = 0L;
    mReadError = null;
    mFlags = 0;
    evaluateSystemProperties(true);
    updateFragmentation();
  }
  
  private static int[] splitAndParseNumbers(String paramString)
  {
    int i = 0;
    int j = paramString.length();
    int k = 0;
    int m = 0;
    int n = 0;
    int i1;
    while (n < j)
    {
      i1 = paramString.charAt(n);
      if ((i1 >= 48) && (i1 <= 57))
      {
        i2 = i;
        i1 = m;
        if (m == 0)
        {
          i1 = 1;
          i2 = i + 1;
        }
      }
      else
      {
        i1 = 0;
        i2 = i;
      }
      n++;
      i = i2;
      m = i1;
    }
    int[] arrayOfInt = new int[i];
    n = 0;
    int i2 = 0;
    int i3 = m;
    while (k < j)
    {
      m = paramString.charAt(k);
      int i4;
      if ((m >= 48) && (m <= 57))
      {
        if (i3 == 0)
        {
          i1 = 1;
          m -= 48;
          i4 = n;
        }
        else
        {
          m = i2 * 10 + (m - 48);
          i1 = i3;
          i4 = n;
        }
      }
      else
      {
        i1 = i3;
        i4 = n;
        m = i2;
        if (i3 != 0)
        {
          i1 = 0;
          arrayOfInt[n] = i2;
          i4 = n + 1;
          m = i2;
        }
      }
      k++;
      i3 = i1;
      n = i4;
      i2 = m;
    }
    if (i > 0) {
      arrayOfInt[(i - 1)] = i2;
    }
    return arrayOfInt;
  }
  
  private void writeCommonString(Parcel paramParcel, String paramString)
  {
    Integer localInteger = (Integer)mCommonStringToIndex.get(paramString);
    if (localInteger != null)
    {
      paramParcel.writeInt(localInteger.intValue());
      return;
    }
    localInteger = Integer.valueOf(mCommonStringToIndex.size());
    mCommonStringToIndex.put(paramString, localInteger);
    paramParcel.writeInt(localInteger.intValue());
    paramParcel.writeString(paramString);
  }
  
  private void writeCompactedLongArray(Parcel paramParcel, long[] paramArrayOfLong, int paramInt)
  {
    for (int i = 0; i < paramInt; i++)
    {
      long l1 = paramArrayOfLong[i];
      long l2 = l1;
      if (l1 < 0L)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Time val negative: ");
        localStringBuilder.append(l1);
        Slog.w("ProcessStats", localStringBuilder.toString());
        l2 = 0L;
      }
      if (l2 <= 2147483647L)
      {
        paramParcel.writeInt((int)l2);
      }
      else
      {
        int j = (int)(0x7FFFFFFF & l2 >> 32);
        int k = (int)(0xFFFFFFFF & l2);
        paramParcel.writeInt(j);
        paramParcel.writeInt(k);
      }
    }
  }
  
  public void add(ProcessStats paramProcessStats)
  {
    Object localObject1 = mPackages.getMap();
    String str;
    int j;
    Object localObject3;
    long l1;
    Object localObject4;
    Object localObject5;
    Object localObject6;
    for (int i = 0; i < ((ArrayMap)localObject1).size(); i++)
    {
      str = (String)((ArrayMap)localObject1).keyAt(i);
      localObject2 = (SparseArray)((ArrayMap)localObject1).valueAt(i);
      for (j = 0; j < ((SparseArray)localObject2).size(); j++)
      {
        int k = ((SparseArray)localObject2).keyAt(j);
        localObject3 = (LongSparseArray)((SparseArray)localObject2).valueAt(j);
        int i3;
        for (m = 0; m < ((LongSparseArray)localObject3).size(); m = i3 + 1)
        {
          l1 = ((LongSparseArray)localObject3).keyAt(m);
          localObject4 = (PackageState)((LongSparseArray)localObject3).valueAt(m);
          int n = mProcesses.size();
          int i1 = mServices.size();
          int i2 = 0;
          i3 = m;
          m = i1;
          while (i2 < n)
          {
            localObject5 = (ProcessState)mProcesses.valueAt(i2);
            if (((ProcessState)localObject5).getCommonProcess() != localObject5)
            {
              localObject6 = getProcessStateLocked(str, k, l1, ((ProcessState)localObject5).getName());
              if (((ProcessState)localObject6).getCommonProcess() == localObject6)
              {
                ((ProcessState)localObject6).setMultiPackage(true);
                long l2 = SystemClock.uptimeMillis();
                PackageState localPackageState = getPackageStateLocked(str, k, l1);
                localObject6 = ((ProcessState)localObject6).clone(l2);
                mProcesses.put(((ProcessState)localObject6).getName(), localObject6);
              }
              ((ProcessState)localObject6).add((ProcessState)localObject5);
            }
            i2++;
          }
          for (n = 0; n < m; n++)
          {
            localObject6 = (ServiceState)mServices.valueAt(n);
            getServiceStateLocked(str, k, l1, ((ServiceState)localObject6).getProcessName(), ((ServiceState)localObject6).getName()).add((ServiceState)localObject6);
          }
        }
      }
    }
    Object localObject2 = mProcesses.getMap();
    for (int m = 0; m < ((ArrayMap)localObject2).size(); m++)
    {
      localObject4 = (SparseArray)((ArrayMap)localObject2).valueAt(m);
      for (i = 0; i < ((SparseArray)localObject4).size(); i++)
      {
        j = ((SparseArray)localObject4).keyAt(i);
        localObject6 = (ProcessState)((SparseArray)localObject4).valueAt(i);
        str = ((ProcessState)localObject6).getName();
        localObject5 = ((ProcessState)localObject6).getPackage();
        l1 = ((ProcessState)localObject6).getVersion();
        localObject1 = (ProcessState)mProcesses.get(str, j);
        if (localObject1 == null)
        {
          localObject3 = new ProcessState(this, (String)localObject5, j, l1, str);
          mProcesses.put(str, j, localObject3);
          localObject5 = getPackageStateLocked((String)localObject5, j, l1);
          localObject1 = localObject3;
          if (!mProcesses.containsKey(str))
          {
            mProcesses.put(str, localObject3);
            localObject1 = localObject3;
          }
        }
        ((ProcessState)localObject1).add((ProcessState)localObject6);
      }
    }
    for (m = 0; m < 8; m++)
    {
      localObject2 = mMemFactorDurations;
      localObject2[m] += mMemFactorDurations[m];
    }
    mSysMemUsage.mergeStats(mSysMemUsage);
    if (mTimePeriodStartClock < mTimePeriodStartClock)
    {
      mTimePeriodStartClock = mTimePeriodStartClock;
      mTimePeriodStartClockStr = mTimePeriodStartClockStr;
    }
    mTimePeriodEndRealtime += mTimePeriodEndRealtime - mTimePeriodStartRealtime;
    mTimePeriodEndUptime += mTimePeriodEndUptime - mTimePeriodStartUptime;
    mInternalSinglePssCount += mInternalSinglePssCount;
    mInternalSinglePssTime += mInternalSinglePssTime;
    mInternalAllMemPssCount += mInternalAllMemPssCount;
    mInternalAllMemPssTime += mInternalAllMemPssTime;
    mInternalAllPollPssCount += mInternalAllPollPssCount;
    mInternalAllPollPssTime += mInternalAllPollPssTime;
    mExternalPssCount += mExternalPssCount;
    mExternalPssTime += mExternalPssTime;
    mExternalSlowPssCount += mExternalSlowPssCount;
    mExternalSlowPssTime += mExternalSlowPssTime;
    mHasSwappedOutPss |= mHasSwappedOutPss;
  }
  
  public void addSysMemUsage(long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
  {
    if (mMemFactor != -1)
    {
      int i = mMemFactor;
      mSysMemUsageArgs[0] = 1L;
      for (int j = 0; j < 3; j++)
      {
        mSysMemUsageArgs[(1 + j)] = paramLong1;
        mSysMemUsageArgs[(4 + j)] = paramLong2;
        mSysMemUsageArgs[(7 + j)] = paramLong3;
        mSysMemUsageArgs[(10 + j)] = paramLong4;
        mSysMemUsageArgs[(13 + j)] = paramLong5;
      }
      mSysMemUsage.mergeStats(i * 14, mSysMemUsageArgs, 0);
    }
  }
  
  public ArrayList<ProcessState> collectProcessesLocked(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, long paramLong, String paramString, boolean paramBoolean)
  {
    ArraySet localArraySet = new ArraySet();
    ArrayMap localArrayMap = mPackages.getMap();
    Object localObject;
    for (int i = 0; i < localArrayMap.size(); i++)
    {
      String str = (String)localArrayMap.keyAt(i);
      SparseArray localSparseArray = (SparseArray)localArrayMap.valueAt(i);
      for (int j = 0; j < localSparseArray.size(); j++)
      {
        LongSparseArray localLongSparseArray = (LongSparseArray)localSparseArray.valueAt(j);
        int k = localLongSparseArray.size();
        for (int m = 0;; m++)
        {
          localObject = paramString;
          if (m >= k) {
            break;
          }
          PackageState localPackageState = (PackageState)localLongSparseArray.valueAt(m);
          int n = mProcesses.size();
          int i1;
          if ((localObject != null) && (!((String)localObject).equals(str))) {
            i1 = 0;
          } else {
            i1 = 1;
          }
          for (int i2 = 0; i2 < n; i2++)
          {
            localObject = (ProcessState)mProcesses.valueAt(i2);
            if (((i1 != 0) || (paramString.equals(((ProcessState)localObject).getName()))) && ((!paramBoolean) || (((ProcessState)localObject).isInUse()))) {
              localArraySet.add(((ProcessState)localObject).getCommonProcess());
            }
          }
        }
      }
    }
    paramString = new ArrayList(localArraySet.size());
    for (i = 0; i < localArraySet.size(); i++)
    {
      localObject = (ProcessState)localArraySet.valueAt(i);
      if (((ProcessState)localObject).computeProcessTimeLocked(paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramLong) > 0L)
      {
        paramString.add(localObject);
        if (paramArrayOfInt3 != paramArrayOfInt4) {
          ((ProcessState)localObject).computeProcessTimeLocked(paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt4, paramLong);
        }
      }
    }
    Collections.sort(paramString, ProcessState.COMPARATOR);
    return paramString;
  }
  
  public void computeTotalMemoryUse(TotalMemoryUseCollection paramTotalMemoryUseCollection, long paramLong)
  {
    totalTime = 0L;
    for (int i = 0; i < 14; i++)
    {
      processStateWeight[i] = 0.0D;
      processStatePss[i] = 0L;
      processStateTime[i] = 0L;
      processStateSamples[i] = 0;
    }
    for (i = 0; i < 16; i++) {
      sysMemUsage[i] = 0L;
    }
    sysMemCachedWeight = 0.0D;
    sysMemFreeWeight = 0.0D;
    sysMemZRamWeight = 0.0D;
    sysMemKernelWeight = 0.0D;
    sysMemNativeWeight = 0.0D;
    sysMemSamples = 0;
    Object localObject1 = mSysMemUsage.getTotalMemUsage();
    int j;
    for (i = 0; i < screenStates.length; i++) {
      for (j = 0; j < memStates.length; j++)
      {
        int k = screenStates[i] + memStates[j];
        long l1 = mMemFactorDurations[k];
        long l2 = l1;
        if (mMemFactor == k) {
          l2 = l1 + (paramLong - mStartTime);
        }
        totalTime += l2;
        int m = mSysMemUsage.getKey((byte)(k * 14));
        localObject2 = localObject1;
        k = 0;
        if (m != -1)
        {
          long[] arrayOfLong = mSysMemUsage.getArrayForKey(m);
          m = SparseMappingTable.getIndexFromKey(m);
          if (arrayOfLong[(m + 0)] >= 3L)
          {
            SysMemUsageTable.mergeSysMemUsage(sysMemUsage, 0, (long[])localObject2, 0);
            localObject2 = arrayOfLong;
            k = m;
          }
        }
        sysMemCachedWeight += localObject2[(k + 2)] * l2;
        sysMemFreeWeight += localObject2[(k + 5)] * l2;
        sysMemZRamWeight += localObject2[(k + 8)] * l2;
        sysMemKernelWeight += localObject2[(k + 11)] * l2;
        sysMemNativeWeight += localObject2[(k + 14)] * l2;
        sysMemSamples = ((int)(sysMemSamples + localObject2[(k + 0)]));
      }
    }
    hasSwappedOutPss = mHasSwappedOutPss;
    Object localObject2 = mProcesses.getMap();
    for (i = 0; i < ((ArrayMap)localObject2).size(); i++)
    {
      localObject1 = (SparseArray)((ArrayMap)localObject2).valueAt(i);
      for (j = 0; j < ((SparseArray)localObject1).size(); j++) {
        ((ProcessState)((SparseArray)localObject1).valueAt(j)).aggregatePss(paramTotalMemoryUseCollection, paramLong);
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dumpCheckinLocked(PrintWriter paramPrintWriter, String paramString)
  {
    long l1 = SystemClock.uptimeMillis();
    Object localObject1 = mPackages.getMap();
    paramPrintWriter.println("vers,5");
    paramPrintWriter.print("period,");
    paramPrintWriter.print(mTimePeriodStartClockStr);
    paramPrintWriter.print(",");
    paramPrintWriter.print(mTimePeriodStartRealtime);
    paramPrintWriter.print(",");
    long l2;
    if (mRunning) {
      l2 = SystemClock.elapsedRealtime();
    } else {
      l2 = mTimePeriodEndRealtime;
    }
    paramPrintWriter.print(l2);
    int i = 1;
    if ((mFlags & 0x2) != 0)
    {
      paramPrintWriter.print(",shutdown");
      i = 0;
    }
    if ((mFlags & 0x4) != 0)
    {
      paramPrintWriter.print(",sysprops");
      i = 0;
    }
    if ((mFlags & 0x1) != 0)
    {
      paramPrintWriter.print(",complete");
      i = 0;
    }
    if (i != 0) {
      paramPrintWriter.print(",partial");
    }
    if (mHasSwappedOutPss) {
      paramPrintWriter.print(",swapped-out-pss");
    }
    paramPrintWriter.println();
    paramPrintWriter.print("config,");
    paramPrintWriter.println(mRuntime);
    Object localObject2;
    for (int j = 0;; j++)
    {
      localObject2 = paramString;
      if (j >= ((ArrayMap)localObject1).size()) {
        break;
      }
      String str1 = (String)((ArrayMap)localObject1).keyAt(j);
      if ((localObject2 == null) || (((String)localObject2).equals(str1)))
      {
        localObject2 = (SparseArray)((ArrayMap)localObject1).valueAt(j);
        i = 0;
        while (i < ((SparseArray)localObject2).size())
        {
          int k = ((SparseArray)localObject2).keyAt(i);
          Object localObject3 = (LongSparseArray)((SparseArray)localObject2).valueAt(i);
          m = 0;
          Object localObject4 = localObject1;
          localObject1 = localObject2;
          localObject2 = localObject3;
          while (m < ((LongSparseArray)localObject2).size())
          {
            l2 = ((LongSparseArray)localObject2).keyAt(m);
            localObject3 = (PackageState)((LongSparseArray)localObject2).valueAt(m);
            int n = mProcesses.size();
            i1 = mServices.size();
            for (int i2 = 0; i2 < n; i2++) {
              ((ProcessState)mProcesses.valueAt(i2)).dumpPackageProcCheckin(paramPrintWriter, str1, k, l2, (String)mProcesses.keyAt(i2), l1);
            }
            for (n = 0; n < i1; n++)
            {
              String str2 = DumpUtils.collapseString(str1, (String)mServices.keyAt(n));
              ((ServiceState)mServices.valueAt(n)).dumpTimesCheckin(paramPrintWriter, str1, k, l2, str2, l1);
            }
            m++;
          }
          i++;
          localObject2 = localObject1;
          localObject1 = localObject4;
        }
      }
    }
    paramString = mProcesses.getMap();
    for (i = 0; i < paramString.size(); i++)
    {
      localObject1 = (String)paramString.keyAt(i);
      localObject2 = (SparseArray)paramString.valueAt(i);
      for (j = 0; j < ((SparseArray)localObject2).size(); j++)
      {
        m = ((SparseArray)localObject2).keyAt(j);
        ((ProcessState)((SparseArray)localObject2).valueAt(j)).dumpProcCheckin(paramPrintWriter, (String)localObject1, m, l1);
      }
    }
    paramPrintWriter.print("total");
    DumpUtils.dumpAdjTimesCheckin(paramPrintWriter, ",", mMemFactorDurations, mMemFactor, mStartTime, l1);
    paramPrintWriter.println();
    int m = mSysMemUsage.getKeyCount();
    if (m > 0)
    {
      paramPrintWriter.print("sysmemusage");
      for (i = 0; i < m; i++)
      {
        i1 = mSysMemUsage.getKeyAt(i);
        j = SparseMappingTable.getIdFromKey(i1);
        paramPrintWriter.print(",");
        DumpUtils.printProcStateTag(paramPrintWriter, j);
        for (j = 0; j < 16; j++)
        {
          if (j > 1) {
            paramPrintWriter.print(":");
          }
          paramPrintWriter.print(mSysMemUsage.getValue(i1, j));
        }
      }
    }
    paramPrintWriter.println();
    paramString = new TotalMemoryUseCollection(ALL_SCREEN_ADJ, ALL_MEM_ADJ);
    computeTotalMemoryUse(paramString, l1);
    paramPrintWriter.print("weights,");
    paramPrintWriter.print(totalTime);
    paramPrintWriter.print(",");
    paramPrintWriter.print(sysMemCachedWeight);
    paramPrintWriter.print(":");
    paramPrintWriter.print(sysMemSamples);
    paramPrintWriter.print(",");
    paramPrintWriter.print(sysMemFreeWeight);
    paramPrintWriter.print(":");
    paramPrintWriter.print(sysMemSamples);
    paramPrintWriter.print(",");
    paramPrintWriter.print(sysMemZRamWeight);
    paramPrintWriter.print(":");
    paramPrintWriter.print(sysMemSamples);
    paramPrintWriter.print(",");
    paramPrintWriter.print(sysMemKernelWeight);
    paramPrintWriter.print(":");
    paramPrintWriter.print(sysMemSamples);
    paramPrintWriter.print(",");
    paramPrintWriter.print(sysMemNativeWeight);
    paramPrintWriter.print(":");
    paramPrintWriter.print(sysMemSamples);
    for (i = 0; i < 14; i++)
    {
      paramPrintWriter.print(",");
      paramPrintWriter.print(processStateWeight[i]);
      paramPrintWriter.print(":");
      paramPrintWriter.print(processStateSamples[i]);
    }
    paramPrintWriter.println();
    int i1 = mPageTypeLabels.size();
    for (i = 0; i < i1; i++)
    {
      paramPrintWriter.print("availablepages,");
      paramPrintWriter.print((String)mPageTypeLabels.get(i));
      paramPrintWriter.print(",");
      paramPrintWriter.print(mPageTypeZones.get(i));
      paramPrintWriter.print(",");
      paramString = (int[])mPageTypeSizes.get(i);
      if (paramString == null) {
        j = 0;
      } else {
        j = paramString.length;
      }
      for (m = 0; m < j; m++)
      {
        if (m != 0) {
          paramPrintWriter.print(",");
        }
        paramPrintWriter.print(paramString[m]);
      }
      paramPrintWriter.println();
    }
  }
  
  void dumpFilteredSummaryLocked(PrintWriter paramPrintWriter, String paramString1, String paramString2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, int[] paramArrayOfInt4, long paramLong1, long paramLong2, String paramString3, boolean paramBoolean)
  {
    paramArrayOfInt3 = collectProcessesLocked(paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramArrayOfInt4, paramLong1, paramString3, paramBoolean);
    if (paramArrayOfInt3.size() > 0)
    {
      if (paramString1 != null)
      {
        paramPrintWriter.println();
        paramPrintWriter.println(paramString1);
      }
      DumpUtils.dumpProcessSummaryLocked(paramPrintWriter, paramString2, paramArrayOfInt3, paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt4, paramLong1, paramLong2);
    }
  }
  
  public void dumpLocked(PrintWriter paramPrintWriter, String paramString, long paramLong, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    Object localObject1 = paramPrintWriter;
    Object localObject2 = paramString;
    boolean bool = paramBoolean2;
    long l1 = DumpUtils.dumpSingleTime(null, null, mMemFactorDurations, mMemFactor, mStartTime, paramLong);
    int i = 0;
    if (mSysMemUsage.getKeyCount() > 0)
    {
      ((PrintWriter)localObject1).println("System memory usage:");
      mSysMemUsage.dump((PrintWriter)localObject1, "  ", ALL_SCREEN_ADJ, ALL_MEM_ADJ);
      i = 1;
    }
    ArrayMap localArrayMap = mPackages.getMap();
    int j = 0;
    int k = 0;
    Object localObject3;
    Object localObject4;
    int n;
    Object localObject5;
    while (k < localArrayMap.size())
    {
      String str = (String)localArrayMap.keyAt(k);
      localObject3 = (SparseArray)localArrayMap.valueAt(k);
      m = 0;
      localObject4 = localObject1;
      localObject1 = localObject2;
      n = k;
      while (m < ((SparseArray)localObject3).size())
      {
        i1 = ((SparseArray)localObject3).keyAt(m);
        localObject5 = (LongSparseArray)((SparseArray)localObject3).valueAt(m);
        k = 0;
        localObject2 = localObject4;
        int i2 = m;
        for (;;)
        {
          m = k;
          if (m >= ((LongSparseArray)localObject5).size()) {
            break;
          }
          long l2 = ((LongSparseArray)localObject5).keyAt(m);
          PackageState localPackageState = (PackageState)((LongSparseArray)localObject5).valueAt(m);
          int i3 = mProcesses.size();
          int i4 = mServices.size();
          int i5;
          if ((localObject1 != null) && (!((String)localObject1).equals(str))) {
            i5 = 0;
          } else {
            i5 = 1;
          }
          if (i5 == 0)
          {
            i6 = 0;
            for (k = 0; k < i3; k++) {
              if (((String)localObject1).equals(((ProcessState)mProcesses.valueAt(k)).getName()))
              {
                i6 = 1;
                break;
              }
            }
            localObject4 = localObject3;
            localObject6 = localObject5;
            k = m;
            localObject3 = localObject6;
            localObject5 = localObject4;
            if (i6 == 0)
            {
              localObject3 = localObject6;
              localObject5 = localObject4;
              break label1101;
            }
          }
          else
          {
            localObject4 = localObject5;
            localObject5 = localObject3;
            localObject3 = localObject4;
            k = m;
          }
          if (i3 <= 0)
          {
            m = j;
            i6 = i;
            if (i4 > 0) {}
          }
          for (;;)
          {
            break;
            m = j;
            i6 = i;
            if (j == 0)
            {
              if (i != 0) {
                paramPrintWriter.println();
              }
              ((PrintWriter)localObject2).println("Per-Package Stats:");
              m = 1;
              i6 = 1;
            }
            ((PrintWriter)localObject2).print("  * ");
            ((PrintWriter)localObject2).print(str);
            ((PrintWriter)localObject2).print(" / ");
            UserHandle.formatUid((PrintWriter)localObject2, i1);
            ((PrintWriter)localObject2).print(" / v");
            ((PrintWriter)localObject2).print(l2);
            ((PrintWriter)localObject2).println(":");
          }
          if ((paramBoolean1) && (!bool))
          {
            localObject6 = new ArrayList();
            for (i = 0; i < i3; i++)
            {
              localObject4 = (ProcessState)mProcesses.valueAt(i);
              if (((i5 != 0) || (((String)localObject1).equals(((ProcessState)localObject4).getName()))) && ((!paramBoolean3) || (((ProcessState)localObject4).isInUse()))) {
                ((ArrayList)localObject6).add(localObject4);
              }
            }
            DumpUtils.dumpProcessSummaryLocked((PrintWriter)localObject2, "      ", (ArrayList)localObject6, ALL_SCREEN_ADJ, ALL_MEM_ADJ, NON_CACHED_PROC_STATES, paramLong, l1);
            i = i4;
            localObject4 = localPackageState;
            localObject6 = localObject1;
            localObject1 = localObject2;
            localObject2 = localObject6;
          }
          else
          {
            localObject4 = localPackageState;
            j = 0;
            i = i4;
            while (j < i3)
            {
              localObject6 = (ProcessState)mProcesses.valueAt(j);
              if ((i5 != 0) || (((String)localObject1).equals(((ProcessState)localObject6).getName())))
              {
                while ((paramBoolean3) && (!((ProcessState)localObject6).isInUse()))
                {
                  ((PrintWriter)localObject2).print("      (Not active: ");
                  ((PrintWriter)localObject2).print((String)mProcesses.keyAt(j));
                  ((PrintWriter)localObject2).println(")");
                }
                ((PrintWriter)localObject2).print("      Process ");
                ((PrintWriter)localObject2).print((String)mProcesses.keyAt(j));
                if (((ProcessState)localObject6).getCommonProcess().isMultiPackage()) {
                  ((PrintWriter)localObject2).print(" (multi, ");
                } else {
                  ((PrintWriter)localObject2).print(" (unique, ");
                }
                ((PrintWriter)localObject2).print(((ProcessState)localObject6).getDurationsBucketCount());
                ((PrintWriter)localObject2).print(" entries)");
                ((PrintWriter)localObject2).println(":");
                ((ProcessState)localObject6).dumpProcessState((PrintWriter)localObject2, "        ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, paramLong);
                ((ProcessState)localObject6).dumpPss((PrintWriter)localObject2, "        ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES);
                ((ProcessState)localObject6).dumpInternalLocked((PrintWriter)localObject2, "        ", paramBoolean2);
              }
              j++;
            }
            localObject6 = localObject2;
            localObject2 = localObject1;
            localObject1 = localObject6;
          }
          bool = paramBoolean2;
          for (j = 0; j < i; j++)
          {
            localObject6 = (ServiceState)mServices.valueAt(j);
            if ((i5 != 0) || (((String)localObject2).equals(((ServiceState)localObject6).getProcessName())))
            {
              while ((paramBoolean3) && (!((ServiceState)localObject6).isInUse()))
              {
                ((PrintWriter)localObject1).print("      (Not active: ");
                ((PrintWriter)localObject1).print((String)mServices.keyAt(j));
                ((PrintWriter)localObject1).println(")");
              }
              if (bool) {
                ((PrintWriter)localObject1).print("      Service ");
              } else {
                ((PrintWriter)localObject1).print("      * ");
              }
              ((PrintWriter)localObject1).print((String)mServices.keyAt(j));
              ((PrintWriter)localObject1).println(":");
              ((PrintWriter)localObject1).print("        Process: ");
              ((PrintWriter)localObject1).println(((ServiceState)localObject6).getProcessName());
              ((ServiceState)localObject6).dumpStats((PrintWriter)localObject1, "        ", "          ", "    ", paramLong, l1, paramBoolean1, bool);
            }
          }
          localObject4 = localObject1;
          localObject1 = localObject2;
          i = i6;
          j = m;
          m = k;
          localObject2 = localObject4;
          label1101:
          k = m + 1;
          localObject4 = localObject3;
          localObject3 = localObject5;
          localObject5 = localObject4;
        }
        m = i2 + 1;
        localObject4 = localObject2;
      }
      k = n + 1;
      localObject2 = localObject1;
      localObject1 = localObject4;
    }
    Object localObject6 = mProcesses.getMap();
    j = 0;
    k = 0;
    int m = 0;
    int i6 = 0;
    int i1 = i;
    i = i6;
    paramBoolean2 = bool;
    for (;;)
    {
      i6 = m;
      if (i6 >= ((ArrayMap)localObject6).size()) {
        break;
      }
      localObject4 = (String)((ArrayMap)localObject6).keyAt(i6);
      localObject3 = (SparseArray)((ArrayMap)localObject6).valueAt(i6);
      m = j;
      j = i;
      n = 0;
      i = m;
      m = n;
      localObject5 = localObject2;
      localObject2 = localObject4;
      while (m < ((SparseArray)localObject3).size())
      {
        n = ((SparseArray)localObject3).keyAt(m);
        localObject4 = (ProcessState)((SparseArray)localObject3).valueAt(m);
        if ((((ProcessState)localObject4).hasAnyData()) || (!((ProcessState)localObject4).isMultiPackage()) || ((localObject5 == null) || (((String)localObject5).equals(localObject2)) || (((String)localObject5).equals(((ProcessState)localObject4).getPackage()))))
        {
          if (i1 != 0) {
            paramPrintWriter.println();
          }
          if (i == 0)
          {
            ((PrintWriter)localObject1).println("Multi-Package Common Processes:");
            i = 1;
          }
          if ((paramBoolean3) && (!((ProcessState)localObject4).isInUse()))
          {
            ((PrintWriter)localObject1).print("      (Not active: ");
            ((PrintWriter)localObject1).print((String)localObject2);
            ((PrintWriter)localObject1).println(")");
          }
          else
          {
            ((PrintWriter)localObject1).print("  * ");
            ((PrintWriter)localObject1).print((String)localObject2);
            ((PrintWriter)localObject1).print(" / ");
            UserHandle.formatUid((PrintWriter)localObject1, n);
            ((PrintWriter)localObject1).print(" (");
            ((PrintWriter)localObject1).print(((ProcessState)localObject4).getDurationsBucketCount());
            ((PrintWriter)localObject1).print(" entries)");
            ((PrintWriter)localObject1).println(":");
            ((ProcessState)localObject4).dumpProcessState((PrintWriter)localObject1, "        ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, paramLong);
            ((ProcessState)localObject4).dumpPss((PrintWriter)localObject1, "        ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES);
            ((ProcessState)localObject4).dumpInternalLocked((PrintWriter)localObject1, "        ", paramBoolean2);
          }
          j++;
          i1 = 1;
        }
        localObject5 = paramString;
        k++;
        m++;
      }
      m = i6 + 1;
      localObject2 = paramString;
      i6 = j;
      j = i;
      i = i6;
    }
    if (paramBoolean2)
    {
      paramPrintWriter.println();
      ((PrintWriter)localObject1).print("  Total procs: ");
      ((PrintWriter)localObject1).print(i);
      ((PrintWriter)localObject1).print(" shown of ");
      ((PrintWriter)localObject1).print(k);
      ((PrintWriter)localObject1).println(" total");
    }
    if (i1 != 0) {
      paramPrintWriter.println();
    }
    if (paramBoolean1)
    {
      ((PrintWriter)localObject1).println("Summary:");
      dumpSummaryLocked((PrintWriter)localObject1, paramString, paramLong, paramBoolean3);
    }
    else
    {
      dumpTotalsLocked((PrintWriter)localObject1, paramLong);
    }
    if (paramBoolean2)
    {
      paramPrintWriter.println();
      ((PrintWriter)localObject1).println("Internal state:");
      ((PrintWriter)localObject1).print("  mRunning=");
      ((PrintWriter)localObject1).println(mRunning);
    }
    dumpFragmentationLocked(paramPrintWriter);
  }
  
  public void dumpSummaryLocked(PrintWriter paramPrintWriter, String paramString, long paramLong, boolean paramBoolean)
  {
    long l = DumpUtils.dumpSingleTime(null, null, mMemFactorDurations, mMemFactor, mStartTime, paramLong);
    dumpFilteredSummaryLocked(paramPrintWriter, null, "  ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, NON_CACHED_PROC_STATES, paramLong, l, paramString, paramBoolean);
    paramPrintWriter.println();
    dumpTotalsLocked(paramPrintWriter, paramLong);
  }
  
  void dumpTotalsLocked(PrintWriter paramPrintWriter, long paramLong)
  {
    paramPrintWriter.println("Run time Stats:");
    DumpUtils.dumpSingleTime(paramPrintWriter, "  ", mMemFactorDurations, mMemFactor, mStartTime, paramLong);
    paramPrintWriter.println();
    paramPrintWriter.println("Memory usage:");
    TotalMemoryUseCollection localTotalMemoryUseCollection = new TotalMemoryUseCollection(ALL_SCREEN_ADJ, ALL_MEM_ADJ);
    computeTotalMemoryUse(localTotalMemoryUseCollection, paramLong);
    paramLong = printMemoryCategory(paramPrintWriter, "  ", "Kernel ", sysMemKernelWeight, totalTime, 0L, sysMemSamples);
    paramLong = printMemoryCategory(paramPrintWriter, "  ", "Native ", sysMemNativeWeight, totalTime, paramLong, sysMemSamples);
    for (int i = 0; i < 14; i++) {
      if (i != 6) {
        paramLong = printMemoryCategory(paramPrintWriter, "  ", DumpUtils.STATE_NAMES[i], processStateWeight[i], totalTime, paramLong, processStateSamples[i]);
      }
    }
    paramLong = printMemoryCategory(paramPrintWriter, "  ", "Cached ", sysMemCachedWeight, totalTime, paramLong, sysMemSamples);
    paramLong = printMemoryCategory(paramPrintWriter, "  ", "Free   ", sysMemFreeWeight, totalTime, paramLong, sysMemSamples);
    paramLong = printMemoryCategory(paramPrintWriter, "  ", "Z-Ram  ", sysMemZRamWeight, totalTime, paramLong, sysMemSamples);
    paramPrintWriter.print("  TOTAL  : ");
    DebugUtils.printSizeValue(paramPrintWriter, paramLong);
    paramPrintWriter.println();
    printMemoryCategory(paramPrintWriter, "  ", DumpUtils.STATE_NAMES[6], processStateWeight[6], totalTime, paramLong, processStateSamples[6]);
    paramPrintWriter.println();
    paramPrintWriter.println("PSS collection stats:");
    paramPrintWriter.print("  Internal Single: ");
    paramPrintWriter.print(mInternalSinglePssCount);
    paramPrintWriter.print("x over ");
    TimeUtils.formatDuration(mInternalSinglePssTime, paramPrintWriter);
    paramPrintWriter.println();
    paramPrintWriter.print("  Internal All Procs (Memory Change): ");
    paramPrintWriter.print(mInternalAllMemPssCount);
    paramPrintWriter.print("x over ");
    TimeUtils.formatDuration(mInternalAllMemPssTime, paramPrintWriter);
    paramPrintWriter.println();
    paramPrintWriter.print("  Internal All Procs (Polling): ");
    paramPrintWriter.print(mInternalAllPollPssCount);
    paramPrintWriter.print("x over ");
    TimeUtils.formatDuration(mInternalAllPollPssTime, paramPrintWriter);
    paramPrintWriter.println();
    paramPrintWriter.print("  External: ");
    paramPrintWriter.print(mExternalPssCount);
    paramPrintWriter.print("x over ");
    TimeUtils.formatDuration(mExternalPssTime, paramPrintWriter);
    paramPrintWriter.println();
    paramPrintWriter.print("  External Slow: ");
    paramPrintWriter.print(mExternalSlowPssCount);
    paramPrintWriter.print("x over ");
    TimeUtils.formatDuration(mExternalSlowPssTime, paramPrintWriter);
    paramPrintWriter.println();
    paramPrintWriter.println();
    paramPrintWriter.print("          Start time: ");
    paramPrintWriter.print(DateFormat.format("yyyy-MM-dd HH:mm:ss", mTimePeriodStartClock));
    paramPrintWriter.println();
    paramPrintWriter.print("        Total uptime: ");
    if (mRunning) {
      paramLong = SystemClock.uptimeMillis();
    } else {
      paramLong = mTimePeriodEndUptime;
    }
    TimeUtils.formatDuration(paramLong - mTimePeriodStartUptime, paramPrintWriter);
    paramPrintWriter.println();
    paramPrintWriter.print("  Total elapsed time: ");
    if (mRunning) {
      paramLong = SystemClock.elapsedRealtime();
    } else {
      paramLong = mTimePeriodEndRealtime;
    }
    TimeUtils.formatDuration(paramLong - mTimePeriodStartRealtime, paramPrintWriter);
    i = 1;
    if ((mFlags & 0x2) != 0)
    {
      paramPrintWriter.print(" (shutdown)");
      i = 0;
    }
    if ((mFlags & 0x4) != 0)
    {
      paramPrintWriter.print(" (sysprops)");
      i = 0;
    }
    if ((mFlags & 0x1) != 0)
    {
      paramPrintWriter.print(" (complete)");
      i = 0;
    }
    if (i != 0) {
      paramPrintWriter.print(" (partial)");
    }
    if (mHasSwappedOutPss) {
      paramPrintWriter.print(" (swapped-out-pss)");
    }
    paramPrintWriter.print(' ');
    paramPrintWriter.print(mRuntime);
    paramPrintWriter.println();
  }
  
  public boolean evaluateSystemProperties(boolean paramBoolean)
  {
    boolean bool1 = false;
    String str = SystemProperties.get("persist.sys.dalvik.vm.lib.2", VMRuntime.getRuntime().vmLibrary());
    if (!Objects.equals(str, mRuntime))
    {
      boolean bool2 = true;
      bool1 = bool2;
      if (paramBoolean)
      {
        mRuntime = str;
        bool1 = bool2;
      }
    }
    return bool1;
  }
  
  public PackageState getPackageStateLocked(String paramString, int paramInt, long paramLong)
  {
    Object localObject1 = (LongSparseArray)mPackages.get(paramString, paramInt);
    Object localObject2 = localObject1;
    if (localObject1 == null)
    {
      localObject2 = new LongSparseArray();
      mPackages.put(paramString, paramInt, localObject2);
    }
    localObject1 = (PackageState)((LongSparseArray)localObject2).get(paramLong);
    if (localObject1 != null) {
      return localObject1;
    }
    paramString = new PackageState(paramString, paramInt);
    ((LongSparseArray)localObject2).put(paramLong, paramString);
    return paramString;
  }
  
  public ProcessState getProcessStateLocked(String paramString1, int paramInt, long paramLong, String paramString2)
  {
    PackageState localPackageState1 = getPackageStateLocked(paramString1, paramInt, paramLong);
    ProcessState localProcessState = (ProcessState)mProcesses.get(paramString2);
    if (localProcessState != null) {
      return localProcessState;
    }
    localProcessState = (ProcessState)mProcesses.get(paramString2, paramInt);
    if (localProcessState == null)
    {
      localProcessState = new ProcessState(this, paramString1, paramInt, paramLong, paramString2);
      mProcesses.put(paramString2, paramInt, localProcessState);
    }
    if (!localProcessState.isMultiPackage())
    {
      if ((paramString1.equals(localProcessState.getPackage())) && (paramLong == localProcessState.getVersion()))
      {
        paramString1 = localProcessState;
      }
      else
      {
        localProcessState.setMultiPackage(true);
        long l = SystemClock.uptimeMillis();
        PackageState localPackageState2 = getPackageStateLocked(localProcessState.getPackage(), paramInt, localProcessState.getVersion());
        Object localObject;
        if (localPackageState2 != null)
        {
          localObject = localProcessState.clone(l);
          mProcesses.put(localProcessState.getName(), localObject);
          for (int i = mServices.size() - 1; i >= 0; i--)
          {
            ServiceState localServiceState = (ServiceState)mServices.valueAt(i);
            if (localServiceState.getProcess() == localProcessState) {
              localServiceState.setProcess((ProcessState)localObject);
            }
          }
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Cloning proc state: no package state ");
          ((StringBuilder)localObject).append(localProcessState.getPackage());
          ((StringBuilder)localObject).append("/");
          ((StringBuilder)localObject).append(paramInt);
          ((StringBuilder)localObject).append(" for proc ");
          ((StringBuilder)localObject).append(localProcessState.getName());
          Slog.w("ProcessStats", ((StringBuilder)localObject).toString());
        }
        paramString1 = new ProcessState(localProcessState, paramString1, paramInt, paramLong, paramString2, l);
      }
    }
    else {
      paramString1 = new ProcessState(localProcessState, paramString1, paramInt, paramLong, paramString2, SystemClock.uptimeMillis());
    }
    mProcesses.put(paramString2, paramString1);
    return paramString1;
  }
  
  public ServiceState getServiceStateLocked(String paramString1, int paramInt, long paramLong, String paramString2, String paramString3)
  {
    PackageState localPackageState = getPackageStateLocked(paramString1, paramInt, paramLong);
    Object localObject = (ServiceState)mServices.get(paramString3);
    if (localObject != null) {
      return localObject;
    }
    if (paramString2 != null) {}
    for (localObject = getProcessStateLocked(paramString1, paramInt, paramLong, paramString2);; localObject = null) {
      break;
    }
    paramString1 = new ServiceState(this, paramString1, paramString3, paramString2, (ProcessState)localObject);
    mServices.put(paramString3, paramString1);
    return paramString1;
  }
  
  long printMemoryCategory(PrintWriter paramPrintWriter, String paramString1, String paramString2, double paramDouble, long paramLong1, long paramLong2, int paramInt)
  {
    if (paramDouble != 0.0D)
    {
      paramLong1 = (1024.0D * paramDouble / paramLong1);
      paramPrintWriter.print(paramString1);
      paramPrintWriter.print(paramString2);
      paramPrintWriter.print(": ");
      DebugUtils.printSizeValue(paramPrintWriter, paramLong1);
      paramPrintWriter.print(" (");
      paramPrintWriter.print(paramInt);
      paramPrintWriter.print(" samples)");
      paramPrintWriter.println();
      return paramLong2 + paramLong1;
    }
    return paramLong2;
  }
  
  public void read(InputStream paramInputStream)
  {
    try
    {
      localObject = new int[1];
      byte[] arrayOfByte = readFully(paramInputStream, (int[])localObject);
      Parcel localParcel = Parcel.obtain();
      localParcel.unmarshall(arrayOfByte, 0, localObject[0]);
      localParcel.setDataPosition(0);
      paramInputStream.close();
      readFromParcel(localParcel);
    }
    catch (IOException paramInputStream)
    {
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("caught exception: ");
      ((StringBuilder)localObject).append(paramInputStream);
      mReadError = ((StringBuilder)localObject).toString();
    }
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    if ((mPackages.getMap().size() <= 0) && (mProcesses.getMap().size() <= 0)) {
      i = 0;
    } else {
      i = 1;
    }
    if (i != 0) {
      resetSafely();
    }
    if (!readCheckedInt(paramParcel, 1347638356, "magic number")) {
      return;
    }
    int j = paramParcel.readInt();
    if (j != 27)
    {
      paramParcel = new StringBuilder();
      paramParcel.append("bad version: ");
      paramParcel.append(j);
      mReadError = paramParcel.toString();
      return;
    }
    if (!readCheckedInt(paramParcel, 14, "state count")) {
      return;
    }
    if (!readCheckedInt(paramParcel, 8, "adj count")) {
      return;
    }
    if (!readCheckedInt(paramParcel, 10, "pss count")) {
      return;
    }
    if (!readCheckedInt(paramParcel, 16, "sys mem usage count")) {
      return;
    }
    if (!readCheckedInt(paramParcel, 4096, "longs size")) {
      return;
    }
    mIndexToCommonString = new ArrayList();
    mTimePeriodStartClock = paramParcel.readLong();
    buildTimePeriodStartClockStr();
    mTimePeriodStartRealtime = paramParcel.readLong();
    mTimePeriodEndRealtime = paramParcel.readLong();
    mTimePeriodStartUptime = paramParcel.readLong();
    mTimePeriodEndUptime = paramParcel.readLong();
    mInternalSinglePssCount = paramParcel.readLong();
    mInternalSinglePssTime = paramParcel.readLong();
    mInternalAllMemPssCount = paramParcel.readLong();
    mInternalAllMemPssTime = paramParcel.readLong();
    mInternalAllPollPssCount = paramParcel.readLong();
    mInternalAllPollPssTime = paramParcel.readLong();
    mExternalPssCount = paramParcel.readLong();
    mExternalPssTime = paramParcel.readLong();
    mExternalSlowPssCount = paramParcel.readLong();
    mExternalSlowPssTime = paramParcel.readLong();
    mRuntime = paramParcel.readString();
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mHasSwappedOutPss = bool;
    mFlags = paramParcel.readInt();
    mTableData.readFromParcel(paramParcel);
    readCompactedLongArray(paramParcel, j, mMemFactorDurations, mMemFactorDurations.length);
    if (!mSysMemUsage.readFromParcel(paramParcel)) {
      return;
    }
    int k = paramParcel.readInt();
    if (k < 0)
    {
      paramParcel = new StringBuilder();
      paramParcel.append("bad process count: ");
      paramParcel.append(k);
      mReadError = paramParcel.toString();
      return;
    }
    Object localObject1;
    int m;
    Object localObject2;
    long l;
    Object localObject3;
    while (k > 0)
    {
      localObject1 = readCommonString(paramParcel, j);
      if (localObject1 == null)
      {
        mReadError = "bad process name";
        return;
      }
      m = paramParcel.readInt();
      n = m;
      if (m < 0)
      {
        paramParcel = new StringBuilder();
        paramParcel.append("bad uid count: ");
        paramParcel.append(m);
        mReadError = paramParcel.toString();
        return;
      }
      while (n > 0)
      {
        m = paramParcel.readInt();
        if (m < 0)
        {
          paramParcel = new StringBuilder();
          paramParcel.append("bad uid: ");
          paramParcel.append(m);
          mReadError = paramParcel.toString();
          return;
        }
        localObject2 = readCommonString(paramParcel, j);
        if (localObject2 == null)
        {
          mReadError = "bad process package name";
          return;
        }
        l = paramParcel.readLong();
        if (i != 0) {
          localObject3 = (ProcessState)mProcesses.get((String)localObject1, m);
        } else {
          localObject3 = null;
        }
        if (localObject3 != null)
        {
          if (!((ProcessState)localObject3).readFromParcel(paramParcel, false)) {
            return;
          }
        }
        else
        {
          localObject2 = new ProcessState(this, (String)localObject2, m, l, (String)localObject1);
          localObject3 = localObject2;
          if (!((ProcessState)localObject2).readFromParcel(paramParcel, true)) {
            return;
          }
        }
        mProcesses.put((String)localObject1, m, localObject3);
        n--;
      }
      k--;
    }
    int n = paramParcel.readInt();
    k = n;
    if (n < 0)
    {
      paramParcel = new StringBuilder();
      paramParcel.append("bad package count: ");
      paramParcel.append(n);
      mReadError = paramParcel.toString();
      return;
    }
    while (k > 0)
    {
      String str1 = readCommonString(paramParcel, j);
      if (str1 == null)
      {
        mReadError = "bad package name";
        return;
      }
      m = paramParcel.readInt();
      n = m;
      if (m < 0)
      {
        paramParcel = new StringBuilder();
        paramParcel.append("bad uid count: ");
        paramParcel.append(m);
        mReadError = paramParcel.toString();
        return;
      }
      while (n > 0)
      {
        int i1 = paramParcel.readInt();
        if (i1 < 0)
        {
          paramParcel = new StringBuilder();
          paramParcel.append("bad uid: ");
          paramParcel.append(i1);
          mReadError = paramParcel.toString();
          return;
        }
        int i2 = paramParcel.readInt();
        m = i2;
        if (i2 < 0)
        {
          paramParcel = new StringBuilder();
          paramParcel.append("bad versions count: ");
          paramParcel.append(i2);
          mReadError = paramParcel.toString();
          return;
        }
        while (m > 0)
        {
          l = paramParcel.readLong();
          localObject1 = new PackageState(str1, i1);
          localObject2 = (LongSparseArray)mPackages.get(str1, i1);
          localObject3 = localObject2;
          if (localObject2 == null)
          {
            localObject3 = new LongSparseArray();
            mPackages.put(str1, i1, localObject3);
          }
          ((LongSparseArray)localObject3).put(l, localObject1);
          i2 = paramParcel.readInt();
          if (i2 < 0)
          {
            paramParcel = new StringBuilder();
            paramParcel.append("bad package process count: ");
            paramParcel.append(i2);
            mReadError = paramParcel.toString();
            return;
          }
          String str2;
          Object localObject4;
          while (i2 > 0)
          {
            i2--;
            str2 = readCommonString(paramParcel, j);
            if (str2 == null)
            {
              mReadError = "bad package process name";
              return;
            }
            i3 = paramParcel.readInt();
            localObject4 = (ProcessState)mProcesses.get(str2, i1);
            if (localObject4 == null)
            {
              paramParcel = new StringBuilder();
              paramParcel.append("no common proc: ");
              paramParcel.append(str2);
              mReadError = paramParcel.toString();
              return;
            }
            if (i3 != 0)
            {
              if (i != 0) {
                localObject2 = (ProcessState)mProcesses.get(str2);
              } else {
                localObject2 = null;
              }
              if (localObject2 != null)
              {
                if (!((ProcessState)localObject2).readFromParcel(paramParcel, false)) {
                  return;
                }
              }
              else
              {
                localObject4 = new ProcessState((ProcessState)localObject4, str1, i1, l, str2, 0L);
                localObject2 = localObject4;
                if (!((ProcessState)localObject4).readFromParcel(paramParcel, true)) {
                  return;
                }
              }
              mProcesses.put(str2, localObject2);
            }
            else
            {
              mProcesses.put(str2, localObject4);
            }
          }
          int i3 = paramParcel.readInt();
          i2 = i3;
          localObject2 = localObject1;
          localObject1 = localObject3;
          if (i3 < 0)
          {
            paramParcel = new StringBuilder();
            paramParcel.append("bad package service count: ");
            paramParcel.append(i3);
            mReadError = paramParcel.toString();
            return;
          }
          while (i2 > 0)
          {
            str2 = paramParcel.readString();
            if (str2 == null)
            {
              mReadError = "bad package service name";
              return;
            }
            if (j > 9) {
              localObject4 = readCommonString(paramParcel, j);
            } else {
              localObject4 = null;
            }
            if (i != 0) {
              localObject3 = (ServiceState)mServices.get(str2);
            } else {
              localObject3 = null;
            }
            if (localObject3 == null) {
              localObject3 = new ServiceState(this, str1, str2, (String)localObject4, null);
            }
            if (!((ServiceState)localObject3).readFromParcel(paramParcel)) {
              return;
            }
            mServices.put(str2, localObject3);
            i2--;
          }
          m--;
        }
        n--;
      }
      k--;
    }
    k = paramParcel.readInt();
    mPageTypeZones.clear();
    mPageTypeZones.ensureCapacity(k);
    mPageTypeLabels.clear();
    mPageTypeLabels.ensureCapacity(k);
    mPageTypeSizes.clear();
    mPageTypeSizes.ensureCapacity(k);
    for (int i = 0; i < k; i++)
    {
      mPageTypeZones.add(Integer.valueOf(paramParcel.readInt()));
      mPageTypeLabels.add(paramParcel.readString());
      mPageTypeSizes.add(paramParcel.createIntArray());
    }
    mIndexToCommonString = null;
  }
  
  public void reset()
  {
    resetCommon();
    mPackages.getMap().clear();
    mProcesses.getMap().clear();
    mMemFactor = -1;
    mStartTime = 0L;
  }
  
  public void resetSafely()
  {
    resetCommon();
    long l = SystemClock.uptimeMillis();
    ArrayMap localArrayMap = mProcesses.getMap();
    int j;
    for (int i = localArrayMap.size() - 1; i >= 0; i--)
    {
      localObject1 = (SparseArray)localArrayMap.valueAt(i);
      for (j = ((SparseArray)localObject1).size() - 1; j >= 0; j--) {
        valueAttmpNumInUse = 0;
      }
    }
    Object localObject1 = mPackages.getMap();
    Object localObject2;
    for (i = ((ArrayMap)localObject1).size() - 1; i >= 0; i--)
    {
      SparseArray localSparseArray = (SparseArray)((ArrayMap)localObject1).valueAt(i);
      for (j = localSparseArray.size() - 1; j >= 0; j--)
      {
        LongSparseArray localLongSparseArray = (LongSparseArray)localSparseArray.valueAt(j);
        for (int k = localLongSparseArray.size() - 1; k >= 0; k--)
        {
          localObject2 = (PackageState)localLongSparseArray.valueAt(k);
          Object localObject3;
          for (int m = mProcesses.size() - 1; m >= 0; m--)
          {
            ProcessState localProcessState = (ProcessState)mProcesses.valueAt(m);
            if (localProcessState.isInUse())
            {
              localProcessState.resetSafely(l);
              localObject3 = localProcessState.getCommonProcess();
              tmpNumInUse += 1;
              getCommonProcesstmpFoundSubProc = localProcessState;
            }
            else
            {
              ((ProcessState)mProcesses.valueAt(m)).makeDead();
              mProcesses.removeAt(m);
            }
          }
          for (m = mServices.size() - 1; m >= 0; m--)
          {
            localObject3 = (ServiceState)mServices.valueAt(m);
            if (((ServiceState)localObject3).isInUse()) {
              ((ServiceState)localObject3).resetSafely(l);
            } else {
              mServices.removeAt(m);
            }
          }
          if ((mProcesses.size() <= 0) && (mServices.size() <= 0)) {
            localLongSparseArray.removeAt(k);
          }
        }
        if (localLongSparseArray.size() <= 0) {
          localSparseArray.removeAt(j);
        }
      }
      if (localSparseArray.size() <= 0) {
        ((ArrayMap)localObject1).removeAt(i);
      }
    }
    for (i = localArrayMap.size() - 1; i >= 0; i--)
    {
      localObject1 = (SparseArray)localArrayMap.valueAt(i);
      for (j = ((SparseArray)localObject1).size() - 1; j >= 0; j--)
      {
        localObject2 = (ProcessState)((SparseArray)localObject1).valueAt(j);
        if ((!((ProcessState)localObject2).isInUse()) && (tmpNumInUse <= 0))
        {
          ((ProcessState)localObject2).makeDead();
          ((SparseArray)localObject1).removeAt(j);
        }
        else if ((!((ProcessState)localObject2).isActive()) && (((ProcessState)localObject2).isMultiPackage()) && (tmpNumInUse == 1))
        {
          localObject2 = tmpFoundSubProc;
          ((ProcessState)localObject2).makeStandalone();
          ((SparseArray)localObject1).setValueAt(j, localObject2);
        }
        else
        {
          ((ProcessState)localObject2).resetSafely(l);
        }
      }
      if (((SparseArray)localObject1).size() <= 0) {
        localArrayMap.removeAt(i);
      }
    }
    mStartTime = l;
  }
  
  /* Error */
  public void updateFragmentation()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aload_2
    //   5: astore_3
    //   6: aload_1
    //   7: astore 4
    //   9: new 1180	java/io/BufferedReader
    //   12: astore 5
    //   14: aload_2
    //   15: astore_3
    //   16: aload_1
    //   17: astore 4
    //   19: new 1182	java/io/FileReader
    //   22: astore 6
    //   24: aload_2
    //   25: astore_3
    //   26: aload_1
    //   27: astore 4
    //   29: aload 6
    //   31: ldc_w 1184
    //   34: invokespecial 1185	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   37: aload_2
    //   38: astore_3
    //   39: aload_1
    //   40: astore 4
    //   42: aload 5
    //   44: aload 6
    //   46: invokespecial 1188	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   49: aload 5
    //   51: astore_3
    //   52: aload 5
    //   54: astore 4
    //   56: getstatic 208	com/android/internal/app/procstats/ProcessStats:sPageTypeRegex	Ljava/util/regex/Pattern;
    //   59: ldc_w 1190
    //   62: invokevirtual 1194	java/util/regex/Pattern:matcher	(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   65: astore_2
    //   66: aload 5
    //   68: astore_3
    //   69: aload 5
    //   71: astore 4
    //   73: aload_0
    //   74: getfield 248	com/android/internal/app/procstats/ProcessStats:mPageTypeZones	Ljava/util/ArrayList;
    //   77: invokevirtual 1139	java/util/ArrayList:clear	()V
    //   80: aload 5
    //   82: astore_3
    //   83: aload 5
    //   85: astore 4
    //   87: aload_0
    //   88: getfield 250	com/android/internal/app/procstats/ProcessStats:mPageTypeLabels	Ljava/util/ArrayList;
    //   91: invokevirtual 1139	java/util/ArrayList:clear	()V
    //   94: aload 5
    //   96: astore_3
    //   97: aload 5
    //   99: astore 4
    //   101: aload_0
    //   102: getfield 252	com/android/internal/app/procstats/ProcessStats:mPageTypeSizes	Ljava/util/ArrayList;
    //   105: invokevirtual 1139	java/util/ArrayList:clear	()V
    //   108: aload 5
    //   110: astore_3
    //   111: aload 5
    //   113: astore 4
    //   115: aload 5
    //   117: invokevirtual 1197	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   120: astore_1
    //   121: aload_1
    //   122: ifnonnull +17 -> 139
    //   125: aload 5
    //   127: invokevirtual 1198	java/io/BufferedReader:close	()V
    //   130: goto +8 -> 138
    //   133: astore 4
    //   135: goto -5 -> 130
    //   138: return
    //   139: aload 5
    //   141: astore_3
    //   142: aload 5
    //   144: astore 4
    //   146: aload_2
    //   147: aload_1
    //   148: invokevirtual 1202	java/util/regex/Matcher:reset	(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
    //   151: pop
    //   152: aload 5
    //   154: astore_3
    //   155: aload 5
    //   157: astore 4
    //   159: aload_2
    //   160: invokevirtual 1205	java/util/regex/Matcher:matches	()Z
    //   163: ifeq +87 -> 250
    //   166: aload 5
    //   168: astore_3
    //   169: aload 5
    //   171: astore 4
    //   173: aload_2
    //   174: iconst_1
    //   175: invokevirtual 1209	java/util/regex/Matcher:group	(I)Ljava/lang/String;
    //   178: bipush 10
    //   180: invokestatic 1212	java/lang/Integer:valueOf	(Ljava/lang/String;I)Ljava/lang/Integer;
    //   183: astore_1
    //   184: aload_1
    //   185: ifnonnull +6 -> 191
    //   188: goto -80 -> 108
    //   191: aload 5
    //   193: astore_3
    //   194: aload 5
    //   196: astore 4
    //   198: aload_0
    //   199: getfield 248	com/android/internal/app/procstats/ProcessStats:mPageTypeZones	Ljava/util/ArrayList;
    //   202: aload_1
    //   203: invokevirtual 371	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   206: pop
    //   207: aload 5
    //   209: astore_3
    //   210: aload 5
    //   212: astore 4
    //   214: aload_0
    //   215: getfield 250	com/android/internal/app/procstats/ProcessStats:mPageTypeLabels	Ljava/util/ArrayList;
    //   218: aload_2
    //   219: iconst_2
    //   220: invokevirtual 1209	java/util/regex/Matcher:group	(I)Ljava/lang/String;
    //   223: invokevirtual 371	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   226: pop
    //   227: aload 5
    //   229: astore_3
    //   230: aload 5
    //   232: astore 4
    //   234: aload_0
    //   235: getfield 252	com/android/internal/app/procstats/ProcessStats:mPageTypeSizes	Ljava/util/ArrayList;
    //   238: aload_2
    //   239: iconst_3
    //   240: invokevirtual 1209	java/util/regex/Matcher:group	(I)Ljava/lang/String;
    //   243: invokestatic 1214	com/android/internal/app/procstats/ProcessStats:splitAndParseNumbers	(Ljava/lang/String;)[I
    //   246: invokevirtual 371	java/util/ArrayList:add	(Ljava/lang/Object;)Z
    //   249: pop
    //   250: goto -142 -> 108
    //   253: astore 4
    //   255: goto +50 -> 305
    //   258: astore_3
    //   259: aload 4
    //   261: astore_3
    //   262: aload_0
    //   263: getfield 248	com/android/internal/app/procstats/ProcessStats:mPageTypeZones	Ljava/util/ArrayList;
    //   266: invokevirtual 1139	java/util/ArrayList:clear	()V
    //   269: aload 4
    //   271: astore_3
    //   272: aload_0
    //   273: getfield 250	com/android/internal/app/procstats/ProcessStats:mPageTypeLabels	Ljava/util/ArrayList;
    //   276: invokevirtual 1139	java/util/ArrayList:clear	()V
    //   279: aload 4
    //   281: astore_3
    //   282: aload_0
    //   283: getfield 252	com/android/internal/app/procstats/ProcessStats:mPageTypeSizes	Ljava/util/ArrayList;
    //   286: invokevirtual 1139	java/util/ArrayList:clear	()V
    //   289: aload 4
    //   291: ifnull +13 -> 304
    //   294: aload 4
    //   296: invokevirtual 1198	java/io/BufferedReader:close	()V
    //   299: goto +5 -> 304
    //   302: astore 4
    //   304: return
    //   305: aload_3
    //   306: ifnull +11 -> 317
    //   309: aload_3
    //   310: invokevirtual 1198	java/io/BufferedReader:close	()V
    //   313: goto +4 -> 317
    //   316: astore_3
    //   317: aload 4
    //   319: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	320	0	this	ProcessStats
    //   1	202	1	localObject1	Object
    //   3	236	2	localMatcher	java.util.regex.Matcher
    //   5	225	3	localObject2	Object
    //   258	1	3	localIOException1	IOException
    //   261	49	3	localObject3	Object
    //   316	1	3	localIOException2	IOException
    //   7	107	4	localObject4	Object
    //   133	1	4	localIOException3	IOException
    //   144	89	4	localObject5	Object
    //   253	42	4	localObject6	Object
    //   302	16	4	localIOException4	IOException
    //   12	219	5	localBufferedReader	java.io.BufferedReader
    //   22	23	6	localFileReader	java.io.FileReader
    // Exception table:
    //   from	to	target	type
    //   125	130	133	java/io/IOException
    //   9	14	253	finally
    //   19	24	253	finally
    //   29	37	253	finally
    //   42	49	253	finally
    //   56	66	253	finally
    //   73	80	253	finally
    //   87	94	253	finally
    //   101	108	253	finally
    //   115	121	253	finally
    //   146	152	253	finally
    //   159	166	253	finally
    //   173	184	253	finally
    //   198	207	253	finally
    //   214	227	253	finally
    //   234	250	253	finally
    //   262	269	253	finally
    //   272	279	253	finally
    //   282	289	253	finally
    //   9	14	258	java/io/IOException
    //   19	24	258	java/io/IOException
    //   29	37	258	java/io/IOException
    //   42	49	258	java/io/IOException
    //   56	66	258	java/io/IOException
    //   73	80	258	java/io/IOException
    //   87	94	258	java/io/IOException
    //   101	108	258	java/io/IOException
    //   115	121	258	java/io/IOException
    //   146	152	258	java/io/IOException
    //   159	166	258	java/io/IOException
    //   173	184	258	java/io/IOException
    //   198	207	258	java/io/IOException
    //   214	227	258	java/io/IOException
    //   234	250	258	java/io/IOException
    //   294	299	302	java/io/IOException
    //   309	313	316	java/io/IOException
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcel(paramParcel, SystemClock.uptimeMillis(), paramInt);
  }
  
  public void writeToParcel(Parcel paramParcel, long paramLong, int paramInt)
  {
    paramParcel.writeInt(1347638356);
    paramParcel.writeInt(27);
    paramParcel.writeInt(14);
    paramParcel.writeInt(8);
    paramParcel.writeInt(10);
    paramParcel.writeInt(16);
    paramParcel.writeInt(4096);
    mCommonStringToIndex = new ArrayMap(mProcesses.size());
    Object localObject1 = mProcesses.getMap();
    int i = ((ArrayMap)localObject1).size();
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      localObject2 = (SparseArray)((ArrayMap)localObject1).valueAt(paramInt);
      j = ((SparseArray)localObject2).size();
      for (k = 0; k < j; k++) {
        ((ProcessState)((SparseArray)localObject2).valueAt(k)).commitStateTime(paramLong);
      }
    }
    Object localObject3 = mPackages.getMap();
    int m = ((ArrayMap)localObject3).size();
    int n;
    int i1;
    Object localObject5;
    int i2;
    int i3;
    Object localObject6;
    for (int k = 0; k < m; k++)
    {
      localObject4 = (SparseArray)((ArrayMap)localObject3).valueAt(k);
      paramInt = ((SparseArray)localObject4).size();
      for (j = 0; j < paramInt; j++)
      {
        localObject2 = (LongSparseArray)((SparseArray)localObject4).valueAt(j);
        n = ((LongSparseArray)localObject2).size();
        for (i1 = 0; i1 < n; i1++)
        {
          localObject5 = (PackageState)((LongSparseArray)localObject2).valueAt(i1);
          i2 = mProcesses.size();
          for (i3 = 0; i3 < i2; i3++)
          {
            localObject6 = (ProcessState)mProcesses.valueAt(i3);
            if (((ProcessState)localObject6).getCommonProcess() != localObject6) {
              ((ProcessState)localObject6).commitStateTime(paramLong);
            }
          }
          i2 = mServices.size();
          for (i3 = 0; i3 < i2; i3++) {
            ((ServiceState)mServices.valueAt(i3)).commitStateTime(paramLong);
          }
        }
      }
    }
    paramParcel.writeLong(mTimePeriodStartClock);
    paramParcel.writeLong(mTimePeriodStartRealtime);
    paramParcel.writeLong(mTimePeriodEndRealtime);
    paramParcel.writeLong(mTimePeriodStartUptime);
    paramParcel.writeLong(mTimePeriodEndUptime);
    paramParcel.writeLong(mInternalSinglePssCount);
    paramParcel.writeLong(mInternalSinglePssTime);
    paramParcel.writeLong(mInternalAllMemPssCount);
    paramParcel.writeLong(mInternalAllMemPssTime);
    paramParcel.writeLong(mInternalAllPollPssCount);
    paramParcel.writeLong(mInternalAllPollPssTime);
    paramParcel.writeLong(mExternalPssCount);
    paramParcel.writeLong(mExternalPssTime);
    paramParcel.writeLong(mExternalSlowPssCount);
    paramParcel.writeLong(mExternalSlowPssTime);
    paramParcel.writeString(mRuntime);
    paramParcel.writeInt(mHasSwappedOutPss);
    paramParcel.writeInt(mFlags);
    mTableData.writeToParcel(paramParcel);
    if (mMemFactor != -1)
    {
      localObject2 = mMemFactorDurations;
      paramInt = mMemFactor;
      localObject2[paramInt] += paramLong - mStartTime;
      mStartTime = paramLong;
    }
    writeCompactedLongArray(paramParcel, mMemFactorDurations, mMemFactorDurations.length);
    mSysMemUsage.writeToParcel(paramParcel);
    paramParcel.writeInt(i);
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      writeCommonString(paramParcel, (String)((ArrayMap)localObject1).keyAt(paramInt));
      localObject2 = (SparseArray)((ArrayMap)localObject1).valueAt(paramInt);
      j = ((SparseArray)localObject2).size();
      paramParcel.writeInt(j);
      for (k = 0; k < j; k++)
      {
        paramParcel.writeInt(((SparseArray)localObject2).keyAt(k));
        localObject4 = (ProcessState)((SparseArray)localObject2).valueAt(k);
        writeCommonString(paramParcel, ((ProcessState)localObject4).getPackage());
        paramParcel.writeLong(((ProcessState)localObject4).getVersion());
        ((ProcessState)localObject4).writeToParcel(paramParcel, paramLong);
      }
    }
    paramParcel.writeInt(m);
    paramInt = 0;
    Object localObject2 = localObject3;
    int j = i;
    Object localObject4 = localObject1;
    while (paramInt < m)
    {
      writeCommonString(paramParcel, (String)((ArrayMap)localObject2).keyAt(paramInt));
      localObject3 = (SparseArray)((ArrayMap)localObject2).valueAt(paramInt);
      i = ((SparseArray)localObject3).size();
      paramParcel.writeInt(i);
      for (k = 0; k < i; k++)
      {
        paramParcel.writeInt(((SparseArray)localObject3).keyAt(k));
        localObject5 = (LongSparseArray)((SparseArray)localObject3).valueAt(k);
        n = ((LongSparseArray)localObject5).size();
        paramParcel.writeInt(n);
        for (i1 = 0; i1 < n; i1++)
        {
          paramParcel.writeLong(((LongSparseArray)localObject5).keyAt(i1));
          localObject1 = (PackageState)((LongSparseArray)localObject5).valueAt(i1);
          i2 = mProcesses.size();
          paramParcel.writeInt(i2);
          for (i3 = 0; i3 < i2; i3++)
          {
            writeCommonString(paramParcel, (String)mProcesses.keyAt(i3));
            localObject6 = (ProcessState)mProcesses.valueAt(i3);
            if (((ProcessState)localObject6).getCommonProcess() == localObject6)
            {
              paramParcel.writeInt(0);
            }
            else
            {
              paramParcel.writeInt(1);
              ((ProcessState)localObject6).writeToParcel(paramParcel, paramLong);
            }
          }
          i2 = mServices.size();
          paramParcel.writeInt(i2);
          for (i3 = 0; i3 < i2; i3++)
          {
            paramParcel.writeString((String)mServices.keyAt(i3));
            localObject6 = (ServiceState)mServices.valueAt(i3);
            writeCommonString(paramParcel, ((ServiceState)localObject6).getProcessName());
            ((ServiceState)localObject6).writeToParcel(paramParcel, paramLong);
          }
        }
      }
      paramInt++;
    }
    k = mPageTypeLabels.size();
    paramParcel.writeInt(k);
    for (paramInt = 0; paramInt < k; paramInt++)
    {
      paramParcel.writeInt(((Integer)mPageTypeZones.get(paramInt)).intValue());
      paramParcel.writeString((String)mPageTypeLabels.get(paramInt));
      paramParcel.writeIntArray((int[])mPageTypeSizes.get(paramInt));
    }
    mCommonStringToIndex = null;
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong1, long paramLong2)
  {
    mPackages.getMap();
    long l = paramProtoOutputStream.start(paramLong1);
    paramProtoOutputStream.write(1112396529665L, mTimePeriodStartRealtime);
    if (mRunning) {
      paramLong1 = SystemClock.elapsedRealtime();
    } else {
      paramLong1 = mTimePeriodEndRealtime;
    }
    paramProtoOutputStream.write(1112396529666L, paramLong1);
    paramProtoOutputStream.write(1112396529667L, mTimePeriodStartUptime);
    paramProtoOutputStream.write(1112396529668L, mTimePeriodEndUptime);
    paramProtoOutputStream.write(1138166333445L, mRuntime);
    paramProtoOutputStream.write(1133871366150L, mHasSwappedOutPss);
    int i = 1;
    if ((mFlags & 0x2) != 0)
    {
      paramProtoOutputStream.write(2259152797703L, 3);
      i = 0;
    }
    if ((mFlags & 0x4) != 0)
    {
      paramProtoOutputStream.write(2259152797703L, 4);
      i = 0;
    }
    if ((mFlags & 0x1) != 0)
    {
      paramProtoOutputStream.write(2259152797703L, 1);
      i = 0;
    }
    if (i != 0) {
      paramProtoOutputStream.write(2259152797703L, 2);
    }
    ArrayMap localArrayMap = mProcesses.getMap();
    for (i = 0; i < localArrayMap.size(); i++)
    {
      String str = (String)localArrayMap.keyAt(i);
      SparseArray localSparseArray = (SparseArray)localArrayMap.valueAt(i);
      for (int j = 0; j < localSparseArray.size(); j++)
      {
        int k = localSparseArray.keyAt(j);
        ((ProcessState)localSparseArray.valueAt(j)).writeToProto(paramProtoOutputStream, 2246267895816L, str, k, paramLong2);
      }
    }
    paramProtoOutputStream.end(l);
  }
  
  public static final class PackageState
  {
    public final String mPackageName;
    public final ArrayMap<String, ProcessState> mProcesses = new ArrayMap();
    public final ArrayMap<String, ServiceState> mServices = new ArrayMap();
    public final int mUid;
    
    public PackageState(String paramString, int paramInt)
    {
      mUid = paramInt;
      mPackageName = paramString;
    }
  }
  
  public static final class ProcessDataCollection
  {
    public long avgPss;
    public long avgRss;
    public long avgUss;
    public long maxPss;
    public long maxRss;
    public long maxUss;
    final int[] memStates;
    public long minPss;
    public long minRss;
    public long minUss;
    public long numPss;
    final int[] procStates;
    final int[] screenStates;
    public long totalTime;
    
    public ProcessDataCollection(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
    {
      screenStates = paramArrayOfInt1;
      memStates = paramArrayOfInt2;
      procStates = paramArrayOfInt3;
    }
    
    void print(PrintWriter paramPrintWriter, long paramLong, boolean paramBoolean)
    {
      if (totalTime > paramLong) {
        paramPrintWriter.print("*");
      }
      DumpUtils.printPercent(paramPrintWriter, totalTime / paramLong);
      if (numPss > 0L)
      {
        paramPrintWriter.print(" (");
        DebugUtils.printSizeValue(paramPrintWriter, minPss * 1024L);
        paramPrintWriter.print("-");
        DebugUtils.printSizeValue(paramPrintWriter, avgPss * 1024L);
        paramPrintWriter.print("-");
        DebugUtils.printSizeValue(paramPrintWriter, maxPss * 1024L);
        paramPrintWriter.print("/");
        DebugUtils.printSizeValue(paramPrintWriter, minUss * 1024L);
        paramPrintWriter.print("-");
        DebugUtils.printSizeValue(paramPrintWriter, avgUss * 1024L);
        paramPrintWriter.print("-");
        DebugUtils.printSizeValue(paramPrintWriter, maxUss * 1024L);
        paramPrintWriter.print("/");
        DebugUtils.printSizeValue(paramPrintWriter, minRss * 1024L);
        paramPrintWriter.print("-");
        DebugUtils.printSizeValue(paramPrintWriter, avgRss * 1024L);
        paramPrintWriter.print("-");
        DebugUtils.printSizeValue(paramPrintWriter, maxRss * 1024L);
        if (paramBoolean)
        {
          paramPrintWriter.print(" over ");
          paramPrintWriter.print(numPss);
        }
        paramPrintWriter.print(")");
      }
    }
  }
  
  public static final class ProcessStateHolder
  {
    public final long appVersion;
    public ProcessState state;
    
    public ProcessStateHolder(long paramLong)
    {
      appVersion = paramLong;
    }
  }
  
  public static class TotalMemoryUseCollection
  {
    public boolean hasSwappedOutPss;
    final int[] memStates;
    public long[] processStatePss = new long[14];
    public int[] processStateSamples = new int[14];
    public long[] processStateTime = new long[14];
    public double[] processStateWeight = new double[14];
    final int[] screenStates;
    public double sysMemCachedWeight;
    public double sysMemFreeWeight;
    public double sysMemKernelWeight;
    public double sysMemNativeWeight;
    public int sysMemSamples;
    public long[] sysMemUsage = new long[16];
    public double sysMemZRamWeight;
    public long totalTime;
    
    public TotalMemoryUseCollection(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
    {
      screenStates = paramArrayOfInt1;
      memStates = paramArrayOfInt2;
    }
  }
}
