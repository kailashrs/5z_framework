package com.android.internal.app.procstats;

import android.os.Parcel;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.util.proto.ProtoUtils;
import com.android.internal.app.ProcessMap;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class ProcessState
{
  public static final Comparator<ProcessState> COMPARATOR = new Comparator()
  {
    public int compare(ProcessState paramAnonymousProcessState1, ProcessState paramAnonymousProcessState2)
    {
      if (mTmpTotalTime < mTmpTotalTime) {
        return -1;
      }
      if (mTmpTotalTime > mTmpTotalTime) {
        return 1;
      }
      return 0;
    }
  };
  private static final boolean DEBUG = false;
  private static final boolean DEBUG_PARCEL = false;
  private static final int[] PROCESS_STATE_TO_STATE = { 0, 0, 1, 2, 2, 2, 3, 3, 4, 5, 7, 1, 8, 9, 10, 11, 12, 11, 13 };
  private static final String TAG = "ProcessStats";
  private boolean mActive;
  private long mAvgCachedKillPss;
  private ProcessState mCommonProcess;
  private int mCurState = -1;
  private boolean mDead;
  private final DurationsTable mDurations;
  private int mLastPssState = -1;
  private long mLastPssTime;
  private long mMaxCachedKillPss;
  private long mMinCachedKillPss;
  private boolean mMultiPackage;
  private final String mName;
  private int mNumActiveServices;
  private int mNumCachedKill;
  private int mNumExcessiveCpu;
  private int mNumStartedServices;
  private final String mPackage;
  private final PssTable mPssTable;
  private long mStartTime;
  private final ProcessStats mStats;
  private long mTmpTotalTime;
  private final int mUid;
  private final long mVersion;
  public ProcessState tmpFoundSubProc;
  public int tmpNumInUse;
  
  public ProcessState(ProcessState paramProcessState, String paramString1, int paramInt, long paramLong1, String paramString2, long paramLong2)
  {
    mStats = mStats;
    mName = paramString2;
    mCommonProcess = paramProcessState;
    mPackage = paramString1;
    mUid = paramInt;
    mVersion = paramLong1;
    mCurState = mCurState;
    mStartTime = paramLong2;
    mDurations = new DurationsTable(mStats.mTableData);
    mPssTable = new PssTable(mStats.mTableData);
  }
  
  public ProcessState(ProcessStats paramProcessStats, String paramString1, int paramInt, long paramLong, String paramString2)
  {
    mStats = paramProcessStats;
    mName = paramString2;
    mCommonProcess = this;
    mPackage = paramString1;
    mUid = paramInt;
    mVersion = paramLong;
    mDurations = new DurationsTable(mTableData);
    mPssTable = new PssTable(mTableData);
  }
  
  private void addCachedKill(int paramInt, long paramLong1, long paramLong2, long paramLong3)
  {
    if (mNumCachedKill <= 0)
    {
      mNumCachedKill = paramInt;
      mMinCachedKillPss = paramLong1;
      mAvgCachedKillPss = paramLong2;
      mMaxCachedKillPss = paramLong3;
    }
    else
    {
      if (paramLong1 < mMinCachedKillPss) {
        mMinCachedKillPss = paramLong1;
      }
      if (paramLong3 > mMaxCachedKillPss) {
        mMaxCachedKillPss = paramLong3;
      }
      mAvgCachedKillPss = (((mAvgCachedKillPss * mNumCachedKill + paramLong2) / (mNumCachedKill + paramInt)));
      mNumCachedKill += paramInt;
    }
  }
  
  private void dumpProcessSummaryDetails(PrintWriter paramPrintWriter, String paramString1, String paramString2, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, long paramLong1, long paramLong2, boolean paramBoolean)
  {
    paramArrayOfInt1 = new ProcessStats.ProcessDataCollection(paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3);
    computeProcessData(paramArrayOfInt1, paramLong1);
    if ((totalTime / paramLong2 * 100.0D < 0.005D) && (numPss == 0L)) {
      return;
    }
    if (paramString1 != null) {
      paramPrintWriter.print(paramString1);
    }
    if (paramString2 != null) {
      paramPrintWriter.print(paramString2);
    }
    paramArrayOfInt1.print(paramPrintWriter, paramLong2, paramBoolean);
    if (paramString1 != null) {
      paramPrintWriter.println();
    }
  }
  
  private void ensureNotDead()
  {
    if (!mDead) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ProcessState dead: name=");
    localStringBuilder.append(mName);
    localStringBuilder.append(" pkg=");
    localStringBuilder.append(mPackage);
    localStringBuilder.append(" uid=");
    localStringBuilder.append(mUid);
    localStringBuilder.append(" common.name=");
    localStringBuilder.append(mCommonProcess.mName);
    Slog.w("ProcessStats", localStringBuilder.toString());
  }
  
  private ProcessState pullFixedProc(ArrayMap<String, ProcessStats.ProcessStateHolder> paramArrayMap, int paramInt)
  {
    ProcessStats.ProcessStateHolder localProcessStateHolder = (ProcessStats.ProcessStateHolder)paramArrayMap.valueAt(paramInt);
    Object localObject1 = state;
    Object localObject2 = localObject1;
    if (mDead)
    {
      localObject2 = localObject1;
      if (mCommonProcess != localObject1)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Pulling dead proc: name=");
        ((StringBuilder)localObject2).append(mName);
        ((StringBuilder)localObject2).append(" pkg=");
        ((StringBuilder)localObject2).append(mPackage);
        ((StringBuilder)localObject2).append(" uid=");
        ((StringBuilder)localObject2).append(mUid);
        ((StringBuilder)localObject2).append(" common.name=");
        ((StringBuilder)localObject2).append(mCommonProcess.mName);
        Log.wtf("ProcessStats", ((StringBuilder)localObject2).toString());
        localObject2 = mStats.getProcessStateLocked(mPackage, mUid, mVersion, mName);
      }
    }
    localObject1 = localObject2;
    if (mMultiPackage)
    {
      localObject1 = (LongSparseArray)mStats.mPackages.get((String)paramArrayMap.keyAt(paramInt), mUid);
      if (localObject1 != null)
      {
        ProcessStats.PackageState localPackageState = (ProcessStats.PackageState)((LongSparseArray)localObject1).get(mVersion);
        if (localPackageState != null)
        {
          paramArrayMap = mName;
          localObject1 = (ProcessState)mProcesses.get(mName);
          if (localObject1 != null)
          {
            state = ((ProcessState)localObject1);
          }
          else
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("Didn't create per-package process ");
            ((StringBuilder)localObject2).append(paramArrayMap);
            ((StringBuilder)localObject2).append(" in pkg ");
            ((StringBuilder)localObject2).append(mPackageName);
            ((StringBuilder)localObject2).append("/");
            ((StringBuilder)localObject2).append(mUid);
            throw new IllegalStateException(((StringBuilder)localObject2).toString());
          }
        }
        else
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("No existing package ");
          ((StringBuilder)localObject1).append((String)paramArrayMap.keyAt(paramInt));
          ((StringBuilder)localObject1).append("/");
          ((StringBuilder)localObject1).append(mUid);
          ((StringBuilder)localObject1).append(" for multi-proc ");
          ((StringBuilder)localObject1).append(mName);
          ((StringBuilder)localObject1).append(" version ");
          ((StringBuilder)localObject1).append(mVersion);
          throw new IllegalStateException(((StringBuilder)localObject1).toString());
        }
      }
      else
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("No existing package ");
        ((StringBuilder)localObject1).append((String)paramArrayMap.keyAt(paramInt));
        ((StringBuilder)localObject1).append("/");
        ((StringBuilder)localObject1).append(mUid);
        ((StringBuilder)localObject1).append(" for multi-proc ");
        ((StringBuilder)localObject1).append(mName);
        throw new IllegalStateException(((StringBuilder)localObject1).toString());
      }
    }
    return localObject1;
  }
  
  public void add(ProcessState paramProcessState)
  {
    mDurations.addDurations(mDurations);
    mPssTable.mergeStats(mPssTable);
    mNumExcessiveCpu += mNumExcessiveCpu;
    if (mNumCachedKill > 0) {
      addCachedKill(mNumCachedKill, mMinCachedKillPss, mAvgCachedKillPss, mMaxCachedKillPss);
    }
  }
  
  public void addPss(long paramLong1, long paramLong2, long paramLong3, boolean paramBoolean, int paramInt, long paramLong4, ArrayMap<String, ProcessStats.ProcessStateHolder> paramArrayMap)
  {
    ensureNotDead();
    ProcessStats localProcessStats;
    switch (paramInt)
    {
    default: 
      break;
    case 4: 
      localProcessStats = mStats;
      mExternalSlowPssCount += 1L;
      localProcessStats = mStats;
      mExternalSlowPssTime += paramLong4;
      break;
    case 3: 
      localProcessStats = mStats;
      mExternalPssCount += 1L;
      localProcessStats = mStats;
      mExternalPssTime += paramLong4;
      break;
    case 2: 
      localProcessStats = mStats;
      mInternalAllPollPssCount += 1L;
      localProcessStats = mStats;
      mInternalAllPollPssTime += paramLong4;
      break;
    case 1: 
      localProcessStats = mStats;
      mInternalAllMemPssCount += 1L;
      localProcessStats = mStats;
      mInternalAllMemPssTime += paramLong4;
      break;
    case 0: 
      localProcessStats = mStats;
      mInternalSinglePssCount += 1L;
      localProcessStats = mStats;
      mInternalSinglePssTime += paramLong4;
    }
    if ((!paramBoolean) && (mLastPssState == mCurState) && (SystemClock.uptimeMillis() < mLastPssTime + 30000L)) {
      return;
    }
    mLastPssState = mCurState;
    mLastPssTime = SystemClock.uptimeMillis();
    if (mCurState != -1)
    {
      mCommonProcess.mPssTable.mergeStats(mCurState, 1, paramLong1, paramLong1, paramLong1, paramLong2, paramLong2, paramLong2, paramLong3, paramLong3, paramLong3);
      if (!mCommonProcess.mMultiPackage) {
        return;
      }
      if (paramArrayMap != null) {
        for (paramInt = paramArrayMap.size() - 1; paramInt >= 0; paramInt--) {
          pullFixedProcmPssTable.mergeStats(mCurState, 1, paramLong1, paramLong1, paramLong1, paramLong2, paramLong2, paramLong2, paramLong3, paramLong3, paramLong3);
        }
      }
    }
  }
  
  public void aggregatePss(ProcessStats.TotalMemoryUseCollection paramTotalMemoryUseCollection, long paramLong)
  {
    Object localObject1 = this;
    PssAggr localPssAggr1 = new PssAggr();
    PssAggr localPssAggr2 = new PssAggr();
    Object localObject2 = new PssAggr();
    int i = 0;
    long l1;
    long l2;
    for (int j = 0; j < mDurations.getKeyCount(); j++)
    {
      k = SparseMappingTable.getIdFromKey(mDurations.getKeyAt(j));
      m = k % 14;
      l1 = ((ProcessState)localObject1).getPssSampleCount(k);
      if (l1 > 0L)
      {
        l2 = ((ProcessState)localObject1).getPssAverage(k);
        i = 1;
        if (m <= 2) {
          localPssAggr1.add(l2, l1);
        } else if (m <= 7) {
          localPssAggr2.add(l2, l1);
        } else {
          ((PssAggr)localObject2).add(l2, l1);
        }
      }
    }
    if (i == 0) {
      return;
    }
    j = 0;
    int m = 0;
    int k = 0;
    i = j;
    if (samples < 3L)
    {
      i = j;
      if (samples > 0L)
      {
        i = 1;
        localPssAggr1.add(pss, samples);
      }
    }
    j = m;
    if (samples < 3L)
    {
      j = m;
      if (samples > 0L)
      {
        j = 1;
        localPssAggr1.add(pss, samples);
      }
    }
    m = k;
    if (samples < 3L)
    {
      m = k;
      if (samples > 0L)
      {
        m = 1;
        localPssAggr2.add(pss, samples);
      }
    }
    if ((samples < 3L) && (i == 0) && (samples > 0L)) {
      localPssAggr2.add(pss, samples);
    }
    if ((samples < 3L) && (m == 0) && (samples > 0L)) {
      ((PssAggr)localObject2).add(pss, samples);
    }
    if ((samples < 3L) && (j == 0) && (samples > 0L)) {
      ((PssAggr)localObject2).add(pss, samples);
    }
    k = 0;
    localObject1 = localObject2;
    for (;;)
    {
      localObject2 = paramTotalMemoryUseCollection;
      Object localObject3 = this;
      if (k >= mDurations.getKeyCount()) {
        break;
      }
      int n = mDurations.getKeyAt(k);
      int i1 = SparseMappingTable.getIdFromKey(n);
      l1 = mDurations.getValue(n);
      long l3 = l1;
      if (mCurState == i1) {
        l3 = l1 + (paramLong - mStartTime);
      }
      n = i1 % 14;
      long[] arrayOfLong = processStateTime;
      arrayOfLong[n] += l3;
      l1 = ((ProcessState)localObject3).getPssSampleCount(i1);
      if (l1 > 0L)
      {
        l2 = ((ProcessState)localObject3).getPssAverage(i1);
      }
      else if (n <= 2)
      {
        l1 = samples;
        l2 = pss;
      }
      else if (n <= 7)
      {
        l1 = samples;
        l2 = pss;
      }
      else
      {
        l1 = samples;
        l2 = pss;
      }
      double d = (processStatePss[n] * processStateSamples[n] + l2 * l1) / (processStateSamples[n] + l1);
      processStatePss[n] = (d);
      localObject3 = processStateSamples;
      localObject3[n] = ((int)(localObject3[n] + l1));
      localObject2 = processStateWeight;
      localObject2[n] += l2 * l3;
      k++;
    }
  }
  
  public ProcessState clone(long paramLong)
  {
    ProcessState localProcessState = new ProcessState(this, mPackage, mUid, mVersion, mName, paramLong);
    mDurations.addDurations(mDurations);
    mPssTable.copyFrom(mPssTable, 10);
    mNumExcessiveCpu = mNumExcessiveCpu;
    mNumCachedKill = mNumCachedKill;
    mMinCachedKillPss = mMinCachedKillPss;
    mAvgCachedKillPss = mAvgCachedKillPss;
    mMaxCachedKillPss = mMaxCachedKillPss;
    mActive = mActive;
    mNumActiveServices = mNumActiveServices;
    mNumStartedServices = mNumStartedServices;
    return localProcessState;
  }
  
  public void commitStateTime(long paramLong)
  {
    if (mCurState != -1)
    {
      long l = paramLong - mStartTime;
      if (l > 0L) {
        mDurations.addDuration(mCurState, l);
      }
    }
    mStartTime = paramLong;
  }
  
  public void computeProcessData(ProcessStats.ProcessDataCollection paramProcessDataCollection, long paramLong)
  {
    long l1 = 0L;
    totalTime = 0L;
    maxRss = 0L;
    avgRss = 0L;
    minRss = 0L;
    maxUss = 0L;
    avgUss = 0L;
    minUss = 0L;
    maxPss = 0L;
    avgPss = 0L;
    minPss = 0L;
    numPss = 0L;
    for (int i = 0; i < screenStates.length; i++) {
      for (int j = 0; j < memStates.length; j++) {
        for (int k = 0; k < procStates.length; k++)
        {
          int m = (screenStates[i] + memStates[j]) * 14 + procStates[k];
          totalTime += getDuration(m, paramLong);
          long l2 = getPssSampleCount(m);
          if (l2 > l1)
          {
            long l3 = getPssMinimum(m);
            long l4 = getPssAverage(m);
            long l5 = getPssMaximum(m);
            long l6 = getPssUssMinimum(m);
            long l7 = getPssUssAverage(m);
            long l8 = getPssUssMaximum(m);
            long l9 = getPssRssMinimum(m);
            long l10 = getPssRssAverage(m);
            long l11 = getPssRssMaximum(m);
            long l12 = numPss;
            l1 = 0L;
            if (l12 == 0L)
            {
              minPss = l3;
              avgPss = l4;
              maxPss = l5;
              minUss = l6;
              avgUss = l7;
              maxUss = l8;
              minRss = l9;
              avgRss = l10;
              maxRss = l11;
            }
            else
            {
              if (l3 < minPss) {
                minPss = l3;
              }
              double d1 = avgPss;
              double d2 = numPss;
              double d3 = l4;
              l12 = l2;
              avgPss = (((d1 * d2 + d3 * l12) / (numPss + l12)));
              if (l5 > maxPss) {
                maxPss = l5;
              }
              if (l6 < minUss) {
                minUss = l6;
              }
              avgUss = (((avgUss * numPss + l7 * l12) / (numPss + l12)));
              if (l8 > maxUss) {
                maxUss = l8;
              }
              if (l9 < minRss) {
                minRss = l9;
              }
              avgRss = (((avgRss * numPss + l10 * l12) / (numPss + l12)));
              if (l11 > maxRss) {
                maxRss = l11;
              }
            }
            numPss += l2;
          }
        }
      }
    }
  }
  
  public long computeProcessTimeLocked(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, long paramLong)
  {
    long l = 0L;
    for (int i = 0; i < paramArrayOfInt1.length; i++) {
      for (int j = 0; j < paramArrayOfInt2.length; j++) {
        for (int k = 0; k < paramArrayOfInt3.length; k++) {
          l += getDuration((paramArrayOfInt1[i] + paramArrayOfInt2[j]) * 14 + paramArrayOfInt3[k], paramLong);
        }
      }
    }
    mTmpTotalTime = l;
    return l;
  }
  
  public void decActiveServices(String paramString)
  {
    if (mCommonProcess != this) {
      mCommonProcess.decActiveServices(paramString);
    }
    mNumActiveServices -= 1;
    if (mNumActiveServices < 0)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Proc active services underrun: pkg=");
      localStringBuilder.append(mPackage);
      localStringBuilder.append(" uid=");
      localStringBuilder.append(mUid);
      localStringBuilder.append(" proc=");
      localStringBuilder.append(mName);
      localStringBuilder.append(" service=");
      localStringBuilder.append(paramString);
      Slog.wtfStack("ProcessStats", localStringBuilder.toString());
      mNumActiveServices = 0;
    }
  }
  
  public void decStartedServices(int paramInt, long paramLong, String paramString)
  {
    if (mCommonProcess != this) {
      mCommonProcess.decStartedServices(paramInt, paramLong, paramString);
    }
    mNumStartedServices -= 1;
    if ((mNumStartedServices == 0) && (mCurState % 14 == 6))
    {
      setState(-1, paramLong);
    }
    else if (mNumStartedServices < 0)
    {
      paramString = new StringBuilder();
      paramString.append("Proc started services underrun: pkg=");
      paramString.append(mPackage);
      paramString.append(" uid=");
      paramString.append(mUid);
      paramString.append(" name=");
      paramString.append(mName);
      Slog.wtfStack("ProcessStats", paramString.toString());
      mNumStartedServices = 0;
    }
  }
  
  public void dumpAllPssCheckin(PrintWriter paramPrintWriter)
  {
    int i = mPssTable.getKeyCount();
    for (int j = 0; j < i; j++)
    {
      int k = mPssTable.getKeyAt(j);
      int m = SparseMappingTable.getIdFromKey(k);
      paramPrintWriter.print(',');
      DumpUtils.printProcStateTag(paramPrintWriter, m);
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 0));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 1));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 2));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 3));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 4));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 5));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 6));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 7));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 8));
      paramPrintWriter.print(':');
      paramPrintWriter.print(mPssTable.getValue(k, 9));
    }
  }
  
  public void dumpAllStateCheckin(PrintWriter paramPrintWriter, long paramLong)
  {
    int i = 0;
    for (int j = 0; j < mDurations.getKeyCount(); j++)
    {
      int k = mDurations.getKeyAt(j);
      int m = SparseMappingTable.getIdFromKey(k);
      long l1 = mDurations.getValue(k);
      long l2 = l1;
      if (mCurState == m)
      {
        i = 1;
        l2 = l1 + (paramLong - mStartTime);
      }
      DumpUtils.printProcStateTagAndValue(paramPrintWriter, m, l2);
    }
    if ((i == 0) && (mCurState != -1)) {
      DumpUtils.printProcStateTagAndValue(paramPrintWriter, mCurState, paramLong - mStartTime);
    }
  }
  
  public void dumpCsv(PrintWriter paramPrintWriter, boolean paramBoolean1, int[] paramArrayOfInt1, boolean paramBoolean2, int[] paramArrayOfInt2, boolean paramBoolean3, int[] paramArrayOfInt3, long paramLong)
  {
    int i;
    if (paramBoolean1) {
      i = paramArrayOfInt1.length;
    } else {
      i = 1;
    }
    int j;
    if (paramBoolean2) {
      j = paramArrayOfInt2.length;
    } else {
      j = 1;
    }
    if (paramBoolean3) {
      k = paramArrayOfInt3.length;
    } else {
      k = 1;
    }
    int m = 0;
    int n = k;
    int k = j;
    int i1 = i;
    while (m < i1)
    {
      j = 0;
      i = m;
      m = n;
      while (j < k)
      {
        for (n = 0;; n++)
        {
          int[] arrayOfInt1 = paramArrayOfInt2;
          int[] arrayOfInt2 = paramArrayOfInt1;
          if (n >= m) {
            break;
          }
          int i2;
          if (paramBoolean1) {
            i2 = arrayOfInt2[i];
          } else {
            i2 = 0;
          }
          int i3;
          if (paramBoolean2) {
            i3 = arrayOfInt1[j];
          } else {
            i3 = 0;
          }
          int i4;
          if (paramBoolean3) {
            i4 = paramArrayOfInt3[n];
          } else {
            i4 = 0;
          }
          int i5;
          if (paramBoolean1) {
            i5 = 1;
          } else {
            i5 = arrayOfInt2.length;
          }
          int i6;
          if (paramBoolean2) {
            i6 = 1;
          } else {
            i6 = arrayOfInt1.length;
          }
          int i7;
          if (paramBoolean3) {
            i7 = 1;
          } else {
            i7 = paramArrayOfInt3.length;
          }
          long l = 0L;
          for (int i8 = 0; i8 < i5; i8++) {
            for (int i9 = 0; i9 < i6; i9++) {
              for (int i10 = 0; i10 < i7; i10++)
              {
                int i11;
                if (paramBoolean1) {
                  i11 = 0;
                } else {
                  i11 = paramArrayOfInt1[i8];
                }
                int i12;
                if (paramBoolean2) {
                  i12 = 0;
                } else {
                  i12 = paramArrayOfInt2[i9];
                }
                int i13;
                if (paramBoolean3) {
                  i13 = 0;
                } else {
                  i13 = paramArrayOfInt3[i10];
                }
                l += getDuration((i2 + i11 + i3 + i12) * 14 + i4 + i13, paramLong);
              }
            }
          }
          paramPrintWriter.print("\t");
          paramPrintWriter.print(l);
        }
        j++;
      }
      i++;
      n = m;
      m = i;
    }
  }
  
  public void dumpInternalLocked(PrintWriter paramPrintWriter, String paramString, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("myID=");
      paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
      paramPrintWriter.print(" mCommonProcess=");
      paramPrintWriter.print(Integer.toHexString(System.identityHashCode(mCommonProcess)));
      paramPrintWriter.print(" mPackage=");
      paramPrintWriter.println(mPackage);
      if (mMultiPackage)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mMultiPackage=");
        paramPrintWriter.println(mMultiPackage);
      }
      if (this != mCommonProcess)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("Common Proc: ");
        paramPrintWriter.print(mCommonProcess.mName);
        paramPrintWriter.print("/");
        paramPrintWriter.print(mCommonProcess.mUid);
        paramPrintWriter.print(" pkg=");
        paramPrintWriter.println(mCommonProcess.mPackage);
      }
    }
    if (mActive)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mActive=");
      paramPrintWriter.println(mActive);
    }
    if (mDead)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mDead=");
      paramPrintWriter.println(mDead);
    }
    if ((mNumActiveServices != 0) || (mNumStartedServices != 0))
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mNumActiveServices=");
      paramPrintWriter.print(mNumActiveServices);
      paramPrintWriter.print(" mNumStartedServices=");
      paramPrintWriter.println(mNumStartedServices);
    }
  }
  
  public void dumpPackageProcCheckin(PrintWriter paramPrintWriter, String paramString1, int paramInt, long paramLong1, String paramString2, long paramLong2)
  {
    paramPrintWriter.print("pkgproc,");
    paramPrintWriter.print(paramString1);
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramInt);
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramLong1);
    paramPrintWriter.print(",");
    paramPrintWriter.print(DumpUtils.collapseString(paramString1, paramString2));
    dumpAllStateCheckin(paramPrintWriter, paramLong2);
    paramPrintWriter.println();
    if (mPssTable.getKeyCount() > 0)
    {
      paramPrintWriter.print("pkgpss,");
      paramPrintWriter.print(paramString1);
      paramPrintWriter.print(",");
      paramPrintWriter.print(paramInt);
      paramPrintWriter.print(",");
      paramPrintWriter.print(paramLong1);
      paramPrintWriter.print(",");
      paramPrintWriter.print(DumpUtils.collapseString(paramString1, paramString2));
      dumpAllPssCheckin(paramPrintWriter);
      paramPrintWriter.println();
    }
    if ((mNumExcessiveCpu > 0) || (mNumCachedKill > 0))
    {
      paramPrintWriter.print("pkgkills,");
      paramPrintWriter.print(paramString1);
      paramPrintWriter.print(",");
      paramPrintWriter.print(paramInt);
      paramPrintWriter.print(",");
      paramPrintWriter.print(paramLong1);
      paramPrintWriter.print(",");
      paramPrintWriter.print(DumpUtils.collapseString(paramString1, paramString2));
      paramPrintWriter.print(",");
      paramPrintWriter.print("0");
      paramPrintWriter.print(",");
      paramPrintWriter.print(mNumExcessiveCpu);
      paramPrintWriter.print(",");
      paramPrintWriter.print(mNumCachedKill);
      paramPrintWriter.print(",");
      paramPrintWriter.print(mMinCachedKillPss);
      paramPrintWriter.print(":");
      paramPrintWriter.print(mAvgCachedKillPss);
      paramPrintWriter.print(":");
      paramPrintWriter.print(mMaxCachedKillPss);
      paramPrintWriter.println();
    }
  }
  
  public void dumpProcCheckin(PrintWriter paramPrintWriter, String paramString, int paramInt, long paramLong)
  {
    if (mDurations.getKeyCount() > 0)
    {
      paramPrintWriter.print("proc,");
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(",");
      paramPrintWriter.print(paramInt);
      dumpAllStateCheckin(paramPrintWriter, paramLong);
      paramPrintWriter.println();
    }
    if (mPssTable.getKeyCount() > 0)
    {
      paramPrintWriter.print("pss,");
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(",");
      paramPrintWriter.print(paramInt);
      dumpAllPssCheckin(paramPrintWriter);
      paramPrintWriter.println();
    }
    if ((mNumExcessiveCpu > 0) || (mNumCachedKill > 0))
    {
      paramPrintWriter.print("kills,");
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(",");
      paramPrintWriter.print(paramInt);
      paramPrintWriter.print(",");
      paramPrintWriter.print("0");
      paramPrintWriter.print(",");
      paramPrintWriter.print(mNumExcessiveCpu);
      paramPrintWriter.print(",");
      paramPrintWriter.print(mNumCachedKill);
      paramPrintWriter.print(",");
      paramPrintWriter.print(mMinCachedKillPss);
      paramPrintWriter.print(":");
      paramPrintWriter.print(mAvgCachedKillPss);
      paramPrintWriter.print(":");
      paramPrintWriter.print(mMaxCachedKillPss);
      paramPrintWriter.println();
    }
  }
  
  public void dumpProcessState(PrintWriter paramPrintWriter, String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, long paramLong)
  {
    int i = -1;
    paramLong = 0L;
    for (int j = 0; j < paramArrayOfInt1.length; j++)
    {
      int k = -1;
      for (int m = 0; m < paramArrayOfInt2.length; m++)
      {
        int n = 0;
        for (;;)
        {
          Object localObject = this;
          if (n >= paramArrayOfInt3.length) {
            break;
          }
          int i1 = paramArrayOfInt1[j];
          int i2 = paramArrayOfInt2[m];
          int i3 = (i1 + i2) * 14 + paramArrayOfInt3[n];
          long l1 = mDurations.getValueForId((byte)i3);
          if (mCurState == i3) {
            localObject = " (running)";
          } else {
            localObject = "";
          }
          i3 = i;
          int i4 = k;
          long l2 = paramLong;
          if (l1 != 0L)
          {
            paramPrintWriter.print(paramString);
            i3 = i;
            if (paramArrayOfInt1.length > 1)
            {
              if (i != i1) {
                i3 = i1;
              } else {
                i3 = -1;
              }
              DumpUtils.printScreenLabel(paramPrintWriter, i3);
              i3 = i1;
            }
            i = k;
            if (paramArrayOfInt2.length > 1)
            {
              if (k != i2) {
                k = i2;
              } else {
                k = -1;
              }
              DumpUtils.printMemLabel(paramPrintWriter, k, '/');
              i = i2;
            }
            paramPrintWriter.print(DumpUtils.STATE_NAMES[paramArrayOfInt3[n]]);
            paramPrintWriter.print(": ");
            TimeUtils.formatDuration(l1, paramPrintWriter);
            paramPrintWriter.println((String)localObject);
            l2 = paramLong + l1;
            i4 = i;
          }
          n++;
          i = i3;
          k = i4;
          paramLong = l2;
        }
      }
    }
    if (paramLong != 0L)
    {
      paramPrintWriter.print(paramString);
      if (paramArrayOfInt1.length > 1) {
        DumpUtils.printScreenLabel(paramPrintWriter, -1);
      }
      if (paramArrayOfInt2.length > 1) {
        DumpUtils.printMemLabel(paramPrintWriter, -1, '/');
      }
      paramPrintWriter.print("TOTAL  : ");
      TimeUtils.formatDuration(paramLong, paramPrintWriter);
      paramPrintWriter.println();
    }
  }
  
  public void dumpPss(PrintWriter paramPrintWriter, String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3)
  {
    int i = -1;
    int j = 0;
    int k = 0;
    while (k < paramArrayOfInt1.length)
    {
      int m = -1;
      int n = 0;
      int i1 = i;
      for (i = m; n < paramArrayOfInt2.length; i = m)
      {
        m = i;
        i = j;
        int i2 = 0;
        j = m;
        for (;;)
        {
          int[] arrayOfInt1 = paramArrayOfInt2;
          int[] arrayOfInt2 = paramArrayOfInt1;
          if (i2 >= paramArrayOfInt3.length) {
            break;
          }
          int i3 = arrayOfInt2[k];
          int i4 = arrayOfInt1[n];
          int i5 = (i3 + i4) * 14 + paramArrayOfInt3[i2];
          long l = getPssSampleCount(i5);
          if (l > 0L)
          {
            if (i == 0)
            {
              paramPrintWriter.print(paramString);
              paramPrintWriter.print("PSS/USS (");
              paramPrintWriter.print(mPssTable.getKeyCount());
              paramPrintWriter.println(" entries):");
              i = 1;
            }
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("  ");
            m = i1;
            if (arrayOfInt2.length > 1)
            {
              if (i1 != i3) {
                i1 = i3;
              } else {
                i1 = -1;
              }
              DumpUtils.printScreenLabel(paramPrintWriter, i1);
              m = i3;
            }
            i1 = j;
            if (arrayOfInt1.length > 1)
            {
              if (j != i4) {
                j = i4;
              } else {
                j = -1;
              }
              DumpUtils.printMemLabel(paramPrintWriter, j, '/');
              i1 = i4;
            }
            paramPrintWriter.print(DumpUtils.STATE_NAMES[paramArrayOfInt3[i2]]);
            paramPrintWriter.print(": ");
            paramPrintWriter.print(l);
            paramPrintWriter.print(" samples ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssMinimum(i5) * 1024L);
            paramPrintWriter.print(" ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssAverage(i5) * 1024L);
            paramPrintWriter.print(" ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssMaximum(i5) * 1024L);
            paramPrintWriter.print(" / ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssUssMinimum(i5) * 1024L);
            paramPrintWriter.print(" ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssUssAverage(i5) * 1024L);
            paramPrintWriter.print(" ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssUssMaximum(i5) * 1024L);
            paramPrintWriter.print(" / ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssRssMinimum(i5) * 1024L);
            paramPrintWriter.print(" ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssRssAverage(i5) * 1024L);
            paramPrintWriter.print(" ");
            DebugUtils.printSizeValue(paramPrintWriter, getPssRssMaximum(i5) * 1024L);
            paramPrintWriter.println();
            j = i1;
          }
          else
          {
            m = i1;
          }
          i2++;
          i1 = m;
        }
        n++;
        m = j;
        j = i;
      }
      k++;
      i = i1;
    }
    if (mNumExcessiveCpu != 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Killed for excessive CPU use: ");
      paramPrintWriter.print(mNumExcessiveCpu);
      paramPrintWriter.println(" times");
    }
    if (mNumCachedKill != 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Killed from cached state: ");
      paramPrintWriter.print(mNumCachedKill);
      paramPrintWriter.print(" times from pss ");
      DebugUtils.printSizeValue(paramPrintWriter, mMinCachedKillPss * 1024L);
      paramPrintWriter.print("-");
      DebugUtils.printSizeValue(paramPrintWriter, mAvgCachedKillPss * 1024L);
      paramPrintWriter.print("-");
      DebugUtils.printSizeValue(paramPrintWriter, mMaxCachedKillPss * 1024L);
      paramPrintWriter.println();
    }
  }
  
  public void dumpSummary(PrintWriter paramPrintWriter, String paramString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int[] paramArrayOfInt3, long paramLong1, long paramLong2)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("* ");
    paramPrintWriter.print(mName);
    paramPrintWriter.print(" / ");
    UserHandle.formatUid(paramPrintWriter, mUid);
    paramPrintWriter.print(" / v");
    paramPrintWriter.print(mVersion);
    paramPrintWriter.println(":");
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "         TOTAL: ", paramArrayOfInt1, paramArrayOfInt2, paramArrayOfInt3, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "    Persistent: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 0 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "           Top: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 1 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "        Imp Fg: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 2 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "        Imp Bg: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 3 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "        Backup: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 4 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "     Heavy Wgt: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 8 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "       Service: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 5 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "    Service Rs: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 6 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "      Receiver: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 7 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "         Heavy: ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 9 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "        (Home): ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 9 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "    (Last Act): ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 10 }, paramLong1, paramLong2, true);
    dumpProcessSummaryDetails(paramPrintWriter, paramString, "      (Cached): ", paramArrayOfInt1, paramArrayOfInt2, new int[] { 11, 12, 13 }, paramLong1, paramLong2, true);
  }
  
  public ProcessState getCommonProcess()
  {
    return mCommonProcess;
  }
  
  public long getDuration(int paramInt, long paramLong)
  {
    long l1 = mDurations.getValueForId((byte)paramInt);
    long l2 = l1;
    if (mCurState == paramInt) {
      l2 = l1 + (paramLong - mStartTime);
    }
    return l2;
  }
  
  public int getDurationsBucketCount()
  {
    return mDurations.getKeyCount();
  }
  
  public String getName()
  {
    return mName;
  }
  
  public String getPackage()
  {
    return mPackage;
  }
  
  public long getPssAverage(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 2);
  }
  
  public long getPssMaximum(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 3);
  }
  
  public long getPssMinimum(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 1);
  }
  
  public long getPssRssAverage(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 8);
  }
  
  public long getPssRssMaximum(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 9);
  }
  
  public long getPssRssMinimum(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 7);
  }
  
  public long getPssSampleCount(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 0);
  }
  
  public long getPssUssAverage(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 5);
  }
  
  public long getPssUssMaximum(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 6);
  }
  
  public long getPssUssMinimum(int paramInt)
  {
    return mPssTable.getValueForId((byte)paramInt, 4);
  }
  
  public int getUid()
  {
    return mUid;
  }
  
  public long getVersion()
  {
    return mVersion;
  }
  
  public boolean hasAnyData()
  {
    boolean bool;
    if ((mDurations.getKeyCount() == 0) && (mCurState == -1) && (mPssTable.getKeyCount() == 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void incActiveServices(String paramString)
  {
    if (mCommonProcess != this) {
      mCommonProcess.incActiveServices(paramString);
    }
    mNumActiveServices += 1;
  }
  
  public void incStartedServices(int paramInt, long paramLong, String paramString)
  {
    if (mCommonProcess != this) {
      mCommonProcess.incStartedServices(paramInt, paramLong, paramString);
    }
    mNumStartedServices += 1;
    if ((mNumStartedServices == 1) && (mCurState == -1)) {
      setState(6 + paramInt * 14, paramLong);
    }
  }
  
  public boolean isActive()
  {
    return mActive;
  }
  
  public boolean isInUse()
  {
    boolean bool;
    if ((!mActive) && (mNumActiveServices <= 0) && (mNumStartedServices <= 0) && (mCurState == -1)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isMultiPackage()
  {
    return mMultiPackage;
  }
  
  public void makeActive()
  {
    ensureNotDead();
    mActive = true;
  }
  
  public void makeDead()
  {
    mDead = true;
  }
  
  public void makeInactive()
  {
    mActive = false;
  }
  
  public void makeStandalone()
  {
    mCommonProcess = this;
  }
  
  public ProcessState pullFixedProc(String paramString)
  {
    if (mMultiPackage)
    {
      Object localObject = (LongSparseArray)mStats.mPackages.get(paramString, mUid);
      if (localObject != null)
      {
        localObject = (ProcessStats.PackageState)((LongSparseArray)localObject).get(mVersion);
        if (localObject != null)
        {
          localObject = (ProcessState)mProcesses.get(mName);
          if (localObject != null) {
            return localObject;
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Didn't create per-package process ");
          ((StringBuilder)localObject).append(mName);
          ((StringBuilder)localObject).append(" in pkg ");
          ((StringBuilder)localObject).append(paramString);
          ((StringBuilder)localObject).append(" / ");
          ((StringBuilder)localObject).append(mUid);
          ((StringBuilder)localObject).append(" vers ");
          ((StringBuilder)localObject).append(mVersion);
          throw new IllegalStateException(((StringBuilder)localObject).toString());
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Didn't find package ");
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append(" / ");
        ((StringBuilder)localObject).append(mUid);
        ((StringBuilder)localObject).append(" vers ");
        ((StringBuilder)localObject).append(mVersion);
        throw new IllegalStateException(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Didn't find package ");
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(" / ");
      ((StringBuilder)localObject).append(mUid);
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    return this;
  }
  
  public boolean readFromParcel(Parcel paramParcel, boolean paramBoolean)
  {
    boolean bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (paramBoolean) {
      mMultiPackage = bool;
    }
    if (!mDurations.readFromParcel(paramParcel)) {
      return false;
    }
    if (!mPssTable.readFromParcel(paramParcel)) {
      return false;
    }
    paramParcel.readInt();
    mNumExcessiveCpu = paramParcel.readInt();
    mNumCachedKill = paramParcel.readInt();
    if (mNumCachedKill > 0)
    {
      mMinCachedKillPss = paramParcel.readLong();
      mAvgCachedKillPss = paramParcel.readLong();
      mMaxCachedKillPss = paramParcel.readLong();
    }
    else
    {
      mMaxCachedKillPss = 0L;
      mAvgCachedKillPss = 0L;
      mMinCachedKillPss = 0L;
    }
    return true;
  }
  
  public void reportCachedKill(ArrayMap<String, ProcessStats.ProcessStateHolder> paramArrayMap, long paramLong)
  {
    ensureNotDead();
    mCommonProcess.addCachedKill(1, paramLong, paramLong, paramLong);
    if (!mCommonProcess.mMultiPackage) {
      return;
    }
    for (int i = paramArrayMap.size() - 1; i >= 0; i--) {
      pullFixedProc(paramArrayMap, i).addCachedKill(1, paramLong, paramLong, paramLong);
    }
  }
  
  public void reportExcessiveCpu(ArrayMap<String, ProcessStats.ProcessStateHolder> paramArrayMap)
  {
    ensureNotDead();
    ProcessState localProcessState = mCommonProcess;
    mNumExcessiveCpu += 1;
    if (!mCommonProcess.mMultiPackage) {
      return;
    }
    for (int i = paramArrayMap.size() - 1; i >= 0; i--)
    {
      localProcessState = pullFixedProc(paramArrayMap, i);
      mNumExcessiveCpu += 1;
    }
  }
  
  public void resetSafely(long paramLong)
  {
    mDurations.resetTable();
    mPssTable.resetTable();
    mStartTime = paramLong;
    mLastPssState = -1;
    mLastPssTime = 0L;
    mNumExcessiveCpu = 0;
    mNumCachedKill = 0;
    mMaxCachedKillPss = 0L;
    mAvgCachedKillPss = 0L;
    mMinCachedKillPss = 0L;
  }
  
  public void setMultiPackage(boolean paramBoolean)
  {
    mMultiPackage = paramBoolean;
  }
  
  public void setState(int paramInt1, int paramInt2, long paramLong, ArrayMap<String, ProcessStats.ProcessStateHolder> paramArrayMap)
  {
    if (paramInt1 < 0)
    {
      if (mNumStartedServices > 0) {
        paramInt1 = 6 + paramInt2 * 14;
      } else {
        paramInt1 = -1;
      }
    }
    else {
      paramInt1 = PROCESS_STATE_TO_STATE[paramInt1] + paramInt2 * 14;
    }
    mCommonProcess.setState(paramInt1, paramLong);
    if (!mCommonProcess.mMultiPackage) {
      return;
    }
    if (paramArrayMap != null) {
      for (paramInt2 = paramArrayMap.size() - 1; paramInt2 >= 0; paramInt2--) {
        pullFixedProc(paramArrayMap, paramInt2).setState(paramInt1, paramLong);
      }
    }
  }
  
  public void setState(int paramInt, long paramLong)
  {
    ensureNotDead();
    if ((!mDead) && (mCurState != paramInt))
    {
      commitStateTime(paramLong);
      mCurState = paramInt;
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("ProcessState{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(mName);
    localStringBuilder.append("/");
    localStringBuilder.append(mUid);
    localStringBuilder.append(" pkg=");
    localStringBuilder.append(mPackage);
    if (mMultiPackage) {
      localStringBuilder.append(" (multi)");
    }
    if (mCommonProcess != this) {
      localStringBuilder.append(" (sub)");
    }
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, long paramLong)
  {
    paramParcel.writeInt(mMultiPackage);
    mDurations.writeToParcel(paramParcel);
    mPssTable.writeToParcel(paramParcel);
    paramParcel.writeInt(0);
    paramParcel.writeInt(mNumExcessiveCpu);
    paramParcel.writeInt(mNumCachedKill);
    if (mNumCachedKill > 0)
    {
      paramParcel.writeLong(mMinCachedKillPss);
      paramParcel.writeLong(mAvgCachedKillPss);
      paramParcel.writeLong(mMaxCachedKillPss);
    }
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong1, String paramString, int paramInt, long paramLong2)
  {
    long l = paramProtoOutputStream.start(paramLong1);
    paramProtoOutputStream.write(1138166333441L, paramString);
    paramProtoOutputStream.write(1120986464258L, paramInt);
    if ((mNumExcessiveCpu > 0) || (mNumCachedKill > 0))
    {
      paramLong1 = paramProtoOutputStream.start(1146756268035L);
      paramProtoOutputStream.write(1120986464257L, mNumExcessiveCpu);
      paramProtoOutputStream.write(1120986464258L, mNumCachedKill);
      ProtoUtils.toAggStatsProto(paramProtoOutputStream, 1146756268035L, mMinCachedKillPss, mAvgCachedKillPss, mMaxCachedKillPss);
      paramProtoOutputStream.end(paramLong1);
    }
    paramString = new HashMap();
    int i = 0;
    int k;
    for (paramInt = 0; paramInt < mDurations.getKeyCount(); paramInt++)
    {
      int j = mDurations.getKeyAt(paramInt);
      k = SparseMappingTable.getIdFromKey(j);
      paramLong1 = mDurations.getValue(j);
      if (mCurState == k)
      {
        paramLong1 += paramLong2 - mStartTime;
        i = 1;
      }
      paramString.put(Integer.valueOf(k), Long.valueOf(paramLong1));
    }
    if ((i == 0) && (mCurState != -1)) {
      paramString.put(Integer.valueOf(mCurState), Long.valueOf(paramLong2 - mStartTime));
    }
    for (paramInt = 0;; paramInt++)
    {
      i = mPssTable.getKeyCount();
      paramLong1 = 2246267895813L;
      if (paramInt >= i) {
        break;
      }
      i = mPssTable.getKeyAt(paramInt);
      k = SparseMappingTable.getIdFromKey(i);
      if (paramString.containsKey(Integer.valueOf(k)))
      {
        paramLong2 = paramProtoOutputStream.start(2246267895813L);
        DumpUtils.printProcStateTagProto(paramProtoOutputStream, 1159641169921L, 1159641169922L, 1159641169923L, k);
        paramLong1 = ((Long)paramString.get(Integer.valueOf(k))).longValue();
        paramString.remove(Integer.valueOf(k));
        paramProtoOutputStream.write(1112396529668L, paramLong1);
        paramProtoOutputStream.write(1120986464261L, mPssTable.getValue(i, 0));
        ProtoUtils.toAggStatsProto(paramProtoOutputStream, 1146756268038L, mPssTable.getValue(i, 1), mPssTable.getValue(i, 2), mPssTable.getValue(i, 3));
        ProtoUtils.toAggStatsProto(paramProtoOutputStream, 1146756268039L, mPssTable.getValue(i, 4), mPssTable.getValue(i, 5), mPssTable.getValue(i, 6));
        ProtoUtils.toAggStatsProto(paramProtoOutputStream, 1146756268040L, mPssTable.getValue(i, 7), mPssTable.getValue(i, 8), mPssTable.getValue(i, 9));
        paramProtoOutputStream.end(paramLong2);
      }
    }
    paramString = paramString.entrySet().iterator();
    while (paramString.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)paramString.next();
      paramLong2 = paramProtoOutputStream.start(paramLong1);
      DumpUtils.printProcStateTagProto(paramProtoOutputStream, 1159641169921L, 1159641169922L, 1159641169923L, ((Integer)localEntry.getKey()).intValue());
      paramProtoOutputStream.write(1112396529668L, ((Long)localEntry.getValue()).longValue());
      paramProtoOutputStream.end(paramLong2);
    }
    paramProtoOutputStream.end(l);
  }
  
  static class PssAggr
  {
    long pss = 0L;
    long samples = 0L;
    
    PssAggr() {}
    
    void add(long paramLong1, long paramLong2)
    {
      pss = ((pss * samples + paramLong1 * paramLong2) / (samples + paramLong2));
      samples += paramLong2;
    }
  }
}
