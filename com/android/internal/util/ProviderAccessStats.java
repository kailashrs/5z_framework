package com.android.internal.util;

import android.os.SystemClock;
import android.util.SparseBooleanArray;
import android.util.SparseLongArray;
import java.io.PrintWriter;

public class ProviderAccessStats
{
  private final SparseBooleanArray mAllCallingUids = new SparseBooleanArray();
  private final SparseLongArray mBatchStats = new SparseLongArray(0);
  private final SparseLongArray mDeleteInBatchStats = new SparseLongArray(0);
  private final SparseLongArray mDeleteStats = new SparseLongArray(0);
  private final SparseLongArray mInsertInBatchStats = new SparseLongArray(0);
  private final SparseLongArray mInsertStats = new SparseLongArray(0);
  private final Object mLock = new Object();
  private final SparseLongArray mOperationDurationMillis = new SparseLongArray(16);
  private final SparseLongArray mQueryStats = new SparseLongArray(16);
  private final long mStartUptime = SystemClock.uptimeMillis();
  private final ThreadLocal<PerThreadData> mThreadLocal = ThreadLocal.withInitial(_..Lambda.ProviderAccessStats.9AhC6lKURctNKuYjVd_wu7jn6_c.INSTANCE);
  private final SparseLongArray mUpdateInBatchStats = new SparseLongArray(0);
  private final SparseLongArray mUpdateStats = new SparseLongArray(0);
  
  public ProviderAccessStats() {}
  
  private void incrementStats(int paramInt, SparseLongArray paramSparseLongArray)
  {
    synchronized (mLock)
    {
      paramSparseLongArray.put(paramInt, paramSparseLongArray.get(paramInt) + 1L);
      mAllCallingUids.put(paramInt, true);
      paramSparseLongArray = (PerThreadData)mThreadLocal.get();
      nestCount += 1;
      if (nestCount == 1) {
        startUptimeMillis = SystemClock.uptimeMillis();
      }
      return;
    }
  }
  
  private void incrementStats(int paramInt, boolean paramBoolean, SparseLongArray paramSparseLongArray1, SparseLongArray paramSparseLongArray2)
  {
    if (paramBoolean) {
      paramSparseLongArray1 = paramSparseLongArray2;
    }
    incrementStats(paramInt, paramSparseLongArray1);
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    synchronized (mLock)
    {
      paramPrintWriter.print("  Process uptime: ");
      paramPrintWriter.print((SystemClock.uptimeMillis() - mStartUptime) / 60000L);
      paramPrintWriter.println(" minutes");
      paramPrintWriter.println();
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Client activities:");
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("  UID        Query  Insert Update Delete   Batch Insert Update Delete          Sec");
      for (int i = 0; i < mAllCallingUids.size(); i++)
      {
        int j = mAllCallingUids.keyAt(i);
        paramPrintWriter.print(paramString);
        paramPrintWriter.println(String.format("  %-9d %6d  %6d %6d %6d  %6d %6d %6d %6d %12.3f", new Object[] { Integer.valueOf(j), Long.valueOf(mQueryStats.get(j)), Long.valueOf(mInsertStats.get(j)), Long.valueOf(mUpdateStats.get(j)), Long.valueOf(mDeleteStats.get(j)), Long.valueOf(mBatchStats.get(j)), Long.valueOf(mInsertInBatchStats.get(j)), Long.valueOf(mUpdateInBatchStats.get(j)), Long.valueOf(mDeleteInBatchStats.get(j)), Double.valueOf(mOperationDurationMillis.get(j) / 1000.0D) }));
      }
      paramPrintWriter.println();
      return;
    }
  }
  
  public void finishOperation(int paramInt)
  {
    ??? = (PerThreadData)mThreadLocal.get();
    nestCount -= 1;
    if (nestCount == 0)
    {
      long l = Math.max(1L, SystemClock.uptimeMillis() - startUptimeMillis);
      synchronized (mLock)
      {
        mOperationDurationMillis.put(paramInt, mOperationDurationMillis.get(paramInt) + l);
      }
    }
  }
  
  public final void incrementBatchStats(int paramInt)
  {
    incrementStats(paramInt, mBatchStats);
  }
  
  public final void incrementDeleteStats(int paramInt, boolean paramBoolean)
  {
    incrementStats(paramInt, paramBoolean, mDeleteStats, mDeleteInBatchStats);
  }
  
  public final void incrementInsertStats(int paramInt, boolean paramBoolean)
  {
    incrementStats(paramInt, paramBoolean, mInsertStats, mInsertInBatchStats);
  }
  
  public final void incrementQueryStats(int paramInt)
  {
    incrementStats(paramInt, mQueryStats);
  }
  
  public final void incrementUpdateStats(int paramInt, boolean paramBoolean)
  {
    incrementStats(paramInt, paramBoolean, mUpdateStats, mUpdateInBatchStats);
  }
  
  private static class PerThreadData
  {
    public int nestCount;
    public long startUptimeMillis;
    
    private PerThreadData() {}
  }
}
