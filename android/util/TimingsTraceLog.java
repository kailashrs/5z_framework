package android.util;

import android.os.Build;
import android.os.SystemClock;
import android.os.Trace;
import java.util.ArrayDeque;
import java.util.Deque;

public class TimingsTraceLog
{
  private static final boolean DEBUG_BOOT_TIME = Build.IS_USER ^ true;
  private final Deque<Pair<String, Long>> mStartTimes;
  private final String mTag;
  private long mThreadId;
  private long mTraceTag;
  
  public TimingsTraceLog(String paramString, long paramLong)
  {
    ArrayDeque localArrayDeque;
    if (DEBUG_BOOT_TIME) {
      localArrayDeque = new ArrayDeque();
    } else {
      localArrayDeque = null;
    }
    mStartTimes = localArrayDeque;
    mTag = paramString;
    mTraceTag = paramLong;
    mThreadId = Thread.currentThread().getId();
  }
  
  private void assertSameThread()
  {
    Thread localThread = Thread.currentThread();
    if (localThread.getId() == mThreadId) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Instance of TimingsTraceLog can only be called from the thread it was created on (tid: ");
    localStringBuilder.append(mThreadId);
    localStringBuilder.append("), but was from ");
    localStringBuilder.append(localThread.getName());
    localStringBuilder.append(" (tid: ");
    localStringBuilder.append(localThread.getId());
    localStringBuilder.append(")");
    throw new IllegalStateException(localStringBuilder.toString());
  }
  
  public void logDuration(String paramString, long paramLong)
  {
    String str = mTag;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" took to complete: ");
    localStringBuilder.append(paramLong);
    localStringBuilder.append("ms");
    Slog.d(str, localStringBuilder.toString());
  }
  
  public void traceBegin(String paramString)
  {
    assertSameThread();
    Trace.traceBegin(mTraceTag, paramString);
    if (DEBUG_BOOT_TIME) {
      mStartTimes.push(Pair.create(paramString, Long.valueOf(SystemClock.elapsedRealtime())));
    }
  }
  
  public void traceEnd()
  {
    assertSameThread();
    Trace.traceEnd(mTraceTag);
    if (!DEBUG_BOOT_TIME) {
      return;
    }
    if (mStartTimes.peek() == null)
    {
      Slog.w(mTag, "traceEnd called more times than traceBegin");
      return;
    }
    Pair localPair = (Pair)mStartTimes.pop();
    logDuration((String)first, SystemClock.elapsedRealtime() - ((Long)second).longValue());
  }
}
