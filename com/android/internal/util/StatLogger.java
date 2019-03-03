package com.android.internal.util;

import android.os.SystemClock;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;

public class StatLogger
{
  private static final String TAG = "StatLogger";
  private final int SIZE;
  @GuardedBy("mLock")
  private final int[] mCallsPerSecond;
  @GuardedBy("mLock")
  private final int[] mCountStats;
  @GuardedBy("mLock")
  private final long[] mDurationPerSecond;
  @GuardedBy("mLock")
  private final long[] mDurationStats;
  private final String[] mLabels;
  private final Object mLock = new Object();
  @GuardedBy("mLock")
  private final int[] mMaxCallsPerSecond;
  @GuardedBy("mLock")
  private final long[] mMaxDurationPerSecond;
  @GuardedBy("mLock")
  private final long[] mMaxDurationStats;
  @GuardedBy("mLock")
  private long mNextTickTime = SystemClock.elapsedRealtime() + 1000L;
  
  public StatLogger(String[] paramArrayOfString)
  {
    SIZE = paramArrayOfString.length;
    mCountStats = new int[SIZE];
    mDurationStats = new long[SIZE];
    mCallsPerSecond = new int[SIZE];
    mMaxCallsPerSecond = new int[SIZE];
    mDurationPerSecond = new long[SIZE];
    mMaxDurationPerSecond = new long[SIZE];
    mMaxDurationStats = new long[SIZE];
    mLabels = paramArrayOfString;
  }
  
  public void dump(IndentingPrintWriter paramIndentingPrintWriter)
  {
    synchronized (mLock)
    {
      paramIndentingPrintWriter.println("Stats:");
      paramIndentingPrintWriter.increaseIndent();
      for (int i = 0; i < SIZE; i++)
      {
        int j = mCountStats[i];
        double d1 = mDurationStats[i] / 1000.0D;
        String str = mLabels[i];
        double d2;
        if (j == 0) {
          d2 = 0.0D;
        } else {
          d2 = d1 / j;
        }
        paramIndentingPrintWriter.println(String.format("%s: count=%d, total=%.1fms, avg=%.3fms, max calls/s=%d max dur/s=%.1fms max time=%.1fms", new Object[] { str, Integer.valueOf(j), Double.valueOf(d1), Double.valueOf(d2), Integer.valueOf(mMaxCallsPerSecond[i]), Double.valueOf(mMaxDurationPerSecond[i] / 1000.0D), Double.valueOf(mMaxDurationStats[i] / 1000.0D) }));
      }
      paramIndentingPrintWriter.decreaseIndent();
      return;
    }
  }
  
  public void dump(PrintWriter paramPrintWriter, String paramString)
  {
    dump(new IndentingPrintWriter(paramPrintWriter, "  ").setIndent(paramString));
  }
  
  public void dumpProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    synchronized (mLock)
    {
      long l = paramProtoOutputStream.start(paramLong);
      for (int i = 0; i < mLabels.length; i++)
      {
        paramLong = paramProtoOutputStream.start(2246267895809L);
        paramProtoOutputStream.write(1120986464257L, i);
        paramProtoOutputStream.write(1138166333442L, mLabels[i]);
        paramProtoOutputStream.write(1120986464259L, mCountStats[i]);
        paramProtoOutputStream.write(1112396529668L, mDurationStats[i]);
        paramProtoOutputStream.end(paramLong);
      }
      paramProtoOutputStream.end(l);
      return;
    }
  }
  
  public long getTime()
  {
    return SystemClock.elapsedRealtimeNanos() / 1000L;
  }
  
  public long logDurationStat(int paramInt, long paramLong)
  {
    synchronized (mLock)
    {
      paramLong = getTime() - paramLong;
      if ((paramInt >= 0) && (paramInt < SIZE))
      {
        localObject2 = mCountStats;
        localObject2[paramInt] += 1;
        localObject2 = mDurationStats;
        localObject2[paramInt] += paramLong;
        if (mMaxDurationStats[paramInt] < paramLong) {
          mMaxDurationStats[paramInt] = paramLong;
        }
        long l = SystemClock.elapsedRealtime();
        if (l > mNextTickTime)
        {
          if (mMaxCallsPerSecond[paramInt] < mCallsPerSecond[paramInt]) {
            mMaxCallsPerSecond[paramInt] = mCallsPerSecond[paramInt];
          }
          if (mMaxDurationPerSecond[paramInt] < mDurationPerSecond[paramInt]) {
            mMaxDurationPerSecond[paramInt] = mDurationPerSecond[paramInt];
          }
          mCallsPerSecond[paramInt] = 0;
          mDurationPerSecond[paramInt] = 0L;
          mNextTickTime = (1000L + l);
        }
        localObject2 = mCallsPerSecond;
        localObject2[paramInt] += 1;
        localObject2 = mDurationPerSecond;
        localObject2[paramInt] += paramLong;
        return paramLong;
      }
      Object localObject2 = new java/lang/StringBuilder;
      ((StringBuilder)localObject2).<init>();
      ((StringBuilder)localObject2).append("Invalid event ID: ");
      ((StringBuilder)localObject2).append(paramInt);
      Slog.wtf("StatLogger", ((StringBuilder)localObject2).toString());
      return paramLong;
    }
  }
}
