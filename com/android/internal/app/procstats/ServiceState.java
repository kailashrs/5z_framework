package com.android.internal.app.procstats;

import android.os.Parcel;
import android.os.SystemClock;
import android.util.Slog;
import android.util.TimeUtils;
import java.io.PrintWriter;

public final class ServiceState
{
  private static final boolean DEBUG = false;
  public static final int SERVICE_BOUND = 2;
  public static final int SERVICE_COUNT = 4;
  public static final int SERVICE_EXEC = 3;
  public static final int SERVICE_RUN = 0;
  public static final int SERVICE_STARTED = 1;
  private static final String TAG = "ProcessStats";
  private int mBoundCount;
  private long mBoundStartTime;
  private int mBoundState = -1;
  private final DurationsTable mDurations;
  private int mExecCount;
  private long mExecStartTime;
  private int mExecState = -1;
  private final String mName;
  private Object mOwner;
  private final String mPackage;
  private ProcessState mProc;
  private final String mProcessName;
  private boolean mRestarting;
  private int mRunCount;
  private long mRunStartTime;
  private int mRunState = -1;
  private boolean mStarted;
  private int mStartedCount;
  private long mStartedStartTime;
  private int mStartedState = -1;
  
  public ServiceState(ProcessStats paramProcessStats, String paramString1, String paramString2, String paramString3, ProcessState paramProcessState)
  {
    mPackage = paramString1;
    mName = paramString2;
    mProcessName = paramString3;
    mProc = paramProcessState;
    mDurations = new DurationsTable(mTableData);
  }
  
  private void dumpStats(PrintWriter paramPrintWriter, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt1, int paramInt2, int paramInt3, long paramLong1, long paramLong2, long paramLong3, boolean paramBoolean)
  {
    if (paramInt1 != 0) {
      if (paramBoolean)
      {
        paramPrintWriter.print(paramString1);
        paramPrintWriter.print(paramString4);
        paramPrintWriter.print(" op count ");
        paramPrintWriter.print(paramInt1);
        paramPrintWriter.println(":");
        dumpTime(paramPrintWriter, paramString2, paramInt2, paramInt3, paramLong1, paramLong2);
      }
      else
      {
        paramLong1 = dumpTime(null, null, paramInt2, paramInt3, paramLong1, paramLong2);
        paramPrintWriter.print(paramString1);
        paramPrintWriter.print(paramString3);
        paramPrintWriter.print(paramString4);
        paramPrintWriter.print(" count ");
        paramPrintWriter.print(paramInt1);
        paramPrintWriter.print(" / time ");
        DumpUtils.printPercent(paramPrintWriter, paramLong1 / paramLong3);
        paramPrintWriter.println();
      }
    }
  }
  
  private void dumpTimeCheckin(PrintWriter paramPrintWriter, String paramString1, String paramString2, int paramInt1, long paramLong1, String paramString3, int paramInt2, int paramInt3, int paramInt4, long paramLong2, long paramLong3)
  {
    if (paramInt3 <= 0) {
      return;
    }
    paramPrintWriter.print(paramString1);
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramString2);
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramInt1);
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramLong1);
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramString3);
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramInt3);
    paramInt1 = 0;
    int i = mDurations.getKeyCount();
    for (paramInt3 = 0;; paramInt3++)
    {
      paramString1 = this;
      if (paramInt3 >= i) {
        break;
      }
      int j = mDurations.getKeyAt(paramInt3);
      long l = mDurations.getValue(j);
      int k = SparseMappingTable.getIdFromKey(j);
      j = k / 4;
      if (k % 4 == paramInt2)
      {
        paramLong1 = l;
        if (paramInt4 == j)
        {
          paramInt1 = 1;
          paramLong1 = l + (paramLong3 - paramLong2);
        }
        DumpUtils.printAdjTagAndValue(paramPrintWriter, j, paramLong1);
      }
    }
    if ((paramInt1 == 0) && (paramInt4 != -1)) {
      DumpUtils.printAdjTagAndValue(paramPrintWriter, paramInt4, paramLong3 - paramLong2);
    }
    paramPrintWriter.println();
  }
  
  private void updateRunning(int paramInt, long paramLong)
  {
    if ((mStartedState == -1) && (mBoundState == -1) && (mExecState == -1)) {
      paramInt = -1;
    }
    if (mRunState != paramInt)
    {
      if (mRunState != -1) {
        mDurations.addDuration(0 + mRunState * 4, paramLong - mRunStartTime);
      } else if (paramInt != -1) {
        mRunCount += 1;
      }
      mRunState = paramInt;
      mRunStartTime = paramLong;
    }
  }
  
  public void add(ServiceState paramServiceState)
  {
    mDurations.addDurations(mDurations);
    mRunCount += mRunCount;
    mStartedCount += mStartedCount;
    mBoundCount += mBoundCount;
    mExecCount += mExecCount;
  }
  
  public void applyNewOwner(Object paramObject)
  {
    if (mOwner != paramObject) {
      if (mOwner == null)
      {
        mOwner = paramObject;
        mProc.incActiveServices(mName);
      }
      else
      {
        mOwner = paramObject;
        if ((mStarted) || (mBoundState != -1) || (mExecState != -1))
        {
          long l = SystemClock.uptimeMillis();
          if (mStarted) {
            setStarted(false, 0, l);
          }
          if (mBoundState != -1) {
            setBound(false, 0, l);
          }
          if (mExecState != -1) {
            setExecuting(false, 0, l);
          }
        }
      }
    }
  }
  
  public void clearCurrentOwner(Object paramObject, boolean paramBoolean)
  {
    if (mOwner == paramObject)
    {
      mProc.decActiveServices(mName);
      if ((mStarted) || (mBoundState != -1) || (mExecState != -1))
      {
        long l = SystemClock.uptimeMillis();
        StringBuilder localStringBuilder;
        if (mStarted)
        {
          if (!paramBoolean)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Service owner ");
            localStringBuilder.append(paramObject);
            localStringBuilder.append(" cleared while started: pkg=");
            localStringBuilder.append(mPackage);
            localStringBuilder.append(" service=");
            localStringBuilder.append(mName);
            localStringBuilder.append(" proc=");
            localStringBuilder.append(mProc);
            Slog.wtfStack("ProcessStats", localStringBuilder.toString());
          }
          setStarted(false, 0, l);
        }
        if (mBoundState != -1)
        {
          if (!paramBoolean)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Service owner ");
            localStringBuilder.append(paramObject);
            localStringBuilder.append(" cleared while bound: pkg=");
            localStringBuilder.append(mPackage);
            localStringBuilder.append(" service=");
            localStringBuilder.append(mName);
            localStringBuilder.append(" proc=");
            localStringBuilder.append(mProc);
            Slog.wtfStack("ProcessStats", localStringBuilder.toString());
          }
          setBound(false, 0, l);
        }
        if (mExecState != -1)
        {
          if (!paramBoolean)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append("Service owner ");
            localStringBuilder.append(paramObject);
            localStringBuilder.append(" cleared while exec: pkg=");
            localStringBuilder.append(mPackage);
            localStringBuilder.append(" service=");
            localStringBuilder.append(mName);
            localStringBuilder.append(" proc=");
            localStringBuilder.append(mProc);
            Slog.wtfStack("ProcessStats", localStringBuilder.toString());
          }
          setExecuting(false, 0, l);
        }
      }
      mOwner = null;
    }
  }
  
  public void commitStateTime(long paramLong)
  {
    if (mRunState != -1)
    {
      mDurations.addDuration(0 + mRunState * 4, paramLong - mRunStartTime);
      mRunStartTime = paramLong;
    }
    if (mStartedState != -1)
    {
      mDurations.addDuration(1 + mStartedState * 4, paramLong - mStartedStartTime);
      mStartedStartTime = paramLong;
    }
    if (mBoundState != -1)
    {
      mDurations.addDuration(2 + mBoundState * 4, paramLong - mBoundStartTime);
      mBoundStartTime = paramLong;
    }
    if (mExecState != -1)
    {
      mDurations.addDuration(3 + mExecState * 4, paramLong - mExecStartTime);
      mExecStartTime = paramLong;
    }
  }
  
  public void dumpStats(PrintWriter paramPrintWriter, String paramString1, String paramString2, String paramString3, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = mRunCount;
    int j = mRunState;
    long l = mRunStartTime;
    boolean bool1 = false;
    boolean bool2;
    if ((paramBoolean1) && (!paramBoolean2)) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    dumpStats(paramPrintWriter, paramString1, paramString2, paramString3, "Running", i, 0, j, l, paramLong1, paramLong2, bool2);
    i = mStartedCount;
    j = mStartedState;
    l = mStartedStartTime;
    if ((paramBoolean1) && (!paramBoolean2)) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    dumpStats(paramPrintWriter, paramString1, paramString2, paramString3, "Started", i, 1, j, l, paramLong1, paramLong2, bool2);
    j = mBoundCount;
    i = mBoundState;
    l = mBoundStartTime;
    if ((paramBoolean1) && (!paramBoolean2)) {
      bool2 = false;
    } else {
      bool2 = true;
    }
    dumpStats(paramPrintWriter, paramString1, paramString2, paramString3, "Bound", j, 2, i, l, paramLong1, paramLong2, bool2);
    j = mExecCount;
    i = mExecState;
    l = mExecStartTime;
    if ((paramBoolean1) && (!paramBoolean2)) {
      paramBoolean1 = bool1;
    } else {
      paramBoolean1 = true;
    }
    dumpStats(paramPrintWriter, paramString1, paramString2, paramString3, "Executing", j, 3, i, l, paramLong1, paramLong2, paramBoolean1);
    if (paramBoolean2)
    {
      if (mOwner != null)
      {
        paramString1 = paramPrintWriter;
        paramString1.print("        mOwner=");
        paramString1.println(mOwner);
      }
      if ((mStarted) || (mRestarting))
      {
        paramPrintWriter.print("        mStarted=");
        paramPrintWriter.print(mStarted);
        paramPrintWriter.print(" mRestarting=");
        paramPrintWriter.println(mRestarting);
      }
    }
  }
  
  public long dumpTime(PrintWriter paramPrintWriter, String paramString, int paramInt1, int paramInt2, long paramLong1, long paramLong2)
  {
    int i = -1;
    long l1 = 0L;
    for (int j = 0; j < 8; j += 4)
    {
      int k = -1;
      int m = 0;
      while (m < 4)
      {
        int n = m + j;
        long l2 = getDuration(paramInt1, paramInt2, paramLong1, n, paramLong2);
        String str1 = "";
        String str2 = str1;
        if (paramInt2 == n)
        {
          str2 = str1;
          if (paramPrintWriter != null) {
            str2 = " (running)";
          }
        }
        int i1 = i;
        n = k;
        long l3 = l1;
        if (l2 != 0L)
        {
          i1 = i;
          n = k;
          if (paramPrintWriter != null)
          {
            paramPrintWriter.print(paramString);
            if (i != j) {
              i = j;
            } else {
              i = -1;
            }
            DumpUtils.printScreenLabel(paramPrintWriter, i);
            i1 = j;
            if (k != m) {
              k = m;
            } else {
              k = -1;
            }
            DumpUtils.printMemLabel(paramPrintWriter, k, '\000');
            n = m;
            paramPrintWriter.print(": ");
            TimeUtils.formatDuration(l2, paramPrintWriter);
            paramPrintWriter.println(str2);
          }
          l3 = l1 + l2;
        }
        m++;
        i = i1;
        k = n;
        l1 = l3;
      }
    }
    if ((l1 != 0L) && (paramPrintWriter != null))
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("    TOTAL: ");
      TimeUtils.formatDuration(l1, paramPrintWriter);
      paramPrintWriter.println();
    }
    return l1;
  }
  
  public void dumpTimesCheckin(PrintWriter paramPrintWriter, String paramString1, int paramInt, long paramLong1, String paramString2, long paramLong2)
  {
    dumpTimeCheckin(paramPrintWriter, "pkgsvc-run", paramString1, paramInt, paramLong1, paramString2, 0, mRunCount, mRunState, mRunStartTime, paramLong2);
    dumpTimeCheckin(paramPrintWriter, "pkgsvc-start", paramString1, paramInt, paramLong1, paramString2, 1, mStartedCount, mStartedState, mStartedStartTime, paramLong2);
    dumpTimeCheckin(paramPrintWriter, "pkgsvc-bound", paramString1, paramInt, paramLong1, paramString2, 2, mBoundCount, mBoundState, mBoundStartTime, paramLong2);
    dumpTimeCheckin(paramPrintWriter, "pkgsvc-exec", paramString1, paramInt, paramLong1, paramString2, 3, mExecCount, mExecState, mExecStartTime, paramLong2);
  }
  
  public long getDuration(int paramInt1, int paramInt2, long paramLong1, int paramInt3, long paramLong2)
  {
    long l1 = mDurations.getValueForId((byte)(paramInt3 * 4 + paramInt1));
    long l2 = l1;
    if (paramInt2 == paramInt3) {
      l2 = l1 + (paramLong2 - paramLong1);
    }
    return l2;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public String getPackage()
  {
    return mPackage;
  }
  
  public ProcessState getProcess()
  {
    return mProc;
  }
  
  public String getProcessName()
  {
    return mProcessName;
  }
  
  public boolean isInUse()
  {
    boolean bool;
    if ((mOwner == null) && (!mRestarting)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isRestarting()
  {
    return mRestarting;
  }
  
  public boolean readFromParcel(Parcel paramParcel)
  {
    if (!mDurations.readFromParcel(paramParcel)) {
      return false;
    }
    mRunCount = paramParcel.readInt();
    mStartedCount = paramParcel.readInt();
    mBoundCount = paramParcel.readInt();
    mExecCount = paramParcel.readInt();
    return true;
  }
  
  public void resetSafely(long paramLong)
  {
    mDurations.resetTable();
    int i = mRunState;
    int j = 0;
    if (i != -1) {
      i = 1;
    } else {
      i = 0;
    }
    mRunCount = i;
    if (mStartedState != -1) {
      i = 1;
    } else {
      i = 0;
    }
    mStartedCount = i;
    if (mBoundState != -1) {
      i = 1;
    } else {
      i = 0;
    }
    mBoundCount = i;
    i = j;
    if (mExecState != -1) {
      i = 1;
    }
    mExecCount = i;
    mExecStartTime = paramLong;
    mBoundStartTime = paramLong;
    mStartedStartTime = paramLong;
    mRunStartTime = paramLong;
  }
  
  public void setBound(boolean paramBoolean, int paramInt, long paramLong)
  {
    if (mOwner == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Binding service ");
      localStringBuilder.append(this);
      localStringBuilder.append(" without owner");
      Slog.wtf("ProcessStats", localStringBuilder.toString());
    }
    int i;
    if (paramBoolean) {
      i = paramInt;
    } else {
      i = -1;
    }
    if (mBoundState != i)
    {
      if (mBoundState != -1) {
        mDurations.addDuration(2 + mBoundState * 4, paramLong - mBoundStartTime);
      } else if (paramBoolean) {
        mBoundCount += 1;
      }
      mBoundState = i;
      mBoundStartTime = paramLong;
      updateRunning(paramInt, paramLong);
    }
  }
  
  public void setExecuting(boolean paramBoolean, int paramInt, long paramLong)
  {
    if (mOwner == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Executing service ");
      localStringBuilder.append(this);
      localStringBuilder.append(" without owner");
      Slog.wtf("ProcessStats", localStringBuilder.toString());
    }
    int i;
    if (paramBoolean) {
      i = paramInt;
    } else {
      i = -1;
    }
    if (mExecState != i)
    {
      if (mExecState != -1) {
        mDurations.addDuration(3 + mExecState * 4, paramLong - mExecStartTime);
      } else if (paramBoolean) {
        mExecCount += 1;
      }
      mExecState = i;
      mExecStartTime = paramLong;
      updateRunning(paramInt, paramLong);
    }
  }
  
  public void setMemFactor(int paramInt, long paramLong)
  {
    if (isRestarting())
    {
      setRestarting(true, paramInt, paramLong);
    }
    else if (isInUse())
    {
      if (mStartedState != -1) {
        setStarted(true, paramInt, paramLong);
      }
      if (mBoundState != -1) {
        setBound(true, paramInt, paramLong);
      }
      if (mExecState != -1) {
        setExecuting(true, paramInt, paramLong);
      }
    }
  }
  
  public void setProcess(ProcessState paramProcessState)
  {
    mProc = paramProcessState;
  }
  
  public void setRestarting(boolean paramBoolean, int paramInt, long paramLong)
  {
    mRestarting = paramBoolean;
    updateStartedState(paramInt, paramLong);
  }
  
  public void setStarted(boolean paramBoolean, int paramInt, long paramLong)
  {
    if (mOwner == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Starting service ");
      localStringBuilder.append(this);
      localStringBuilder.append(" without owner");
      Slog.wtf("ProcessStats", localStringBuilder.toString());
    }
    mStarted = paramBoolean;
    updateStartedState(paramInt, paramLong);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ServiceState{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(mName);
    localStringBuilder.append(" pkg=");
    localStringBuilder.append(mPackage);
    localStringBuilder.append(" proc=");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void updateStartedState(int paramInt, long paramLong)
  {
    int i = mStartedState;
    int j = 0;
    if (i != -1) {
      i = 1;
    } else {
      i = 0;
    }
    if ((!mStarted) && (!mRestarting)) {
      break label47;
    }
    j = 1;
    label47:
    int k;
    if (j != 0) {
      k = paramInt;
    } else {
      k = -1;
    }
    if (mStartedState != k)
    {
      if (mStartedState != -1) {
        mDurations.addDuration(1 + mStartedState * 4, paramLong - mStartedStartTime);
      } else if (j != 0) {
        mStartedCount += 1;
      }
      mStartedState = k;
      mStartedStartTime = paramLong;
      mProc = mProc.pullFixedProc(mPackage);
      if (i != j) {
        if (j != 0) {
          mProc.incStartedServices(paramInt, paramLong, mName);
        } else {
          mProc.decStartedServices(paramInt, paramLong, mName);
        }
      }
      updateRunning(paramInt, paramLong);
    }
  }
  
  public void writeToParcel(Parcel paramParcel, long paramLong)
  {
    mDurations.writeToParcel(paramParcel);
    paramParcel.writeInt(mRunCount);
    paramParcel.writeInt(mStartedCount);
    paramParcel.writeInt(mBoundCount);
    paramParcel.writeInt(mExecCount);
  }
}
