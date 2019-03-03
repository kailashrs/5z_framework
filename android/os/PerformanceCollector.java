package android.os;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class PerformanceCollector
{
  public static final String METRIC_KEY_CPU_TIME = "cpu_time";
  public static final String METRIC_KEY_EXECUTION_TIME = "execution_time";
  public static final String METRIC_KEY_GC_INVOCATION_COUNT = "gc_invocation_count";
  public static final String METRIC_KEY_GLOBAL_ALLOC_COUNT = "global_alloc_count";
  public static final String METRIC_KEY_GLOBAL_ALLOC_SIZE = "global_alloc_size";
  public static final String METRIC_KEY_GLOBAL_FREED_COUNT = "global_freed_count";
  public static final String METRIC_KEY_GLOBAL_FREED_SIZE = "global_freed_size";
  public static final String METRIC_KEY_ITERATIONS = "iterations";
  public static final String METRIC_KEY_JAVA_ALLOCATED = "java_allocated";
  public static final String METRIC_KEY_JAVA_FREE = "java_free";
  public static final String METRIC_KEY_JAVA_PRIVATE_DIRTY = "java_private_dirty";
  public static final String METRIC_KEY_JAVA_PSS = "java_pss";
  public static final String METRIC_KEY_JAVA_SHARED_DIRTY = "java_shared_dirty";
  public static final String METRIC_KEY_JAVA_SIZE = "java_size";
  public static final String METRIC_KEY_LABEL = "label";
  public static final String METRIC_KEY_NATIVE_ALLOCATED = "native_allocated";
  public static final String METRIC_KEY_NATIVE_FREE = "native_free";
  public static final String METRIC_KEY_NATIVE_PRIVATE_DIRTY = "native_private_dirty";
  public static final String METRIC_KEY_NATIVE_PSS = "native_pss";
  public static final String METRIC_KEY_NATIVE_SHARED_DIRTY = "native_shared_dirty";
  public static final String METRIC_KEY_NATIVE_SIZE = "native_size";
  public static final String METRIC_KEY_OTHER_PRIVATE_DIRTY = "other_private_dirty";
  public static final String METRIC_KEY_OTHER_PSS = "other_pss";
  public static final String METRIC_KEY_OTHER_SHARED_DIRTY = "other_shared_dirty";
  public static final String METRIC_KEY_PRE_RECEIVED_TRANSACTIONS = "pre_received_transactions";
  public static final String METRIC_KEY_PRE_SENT_TRANSACTIONS = "pre_sent_transactions";
  public static final String METRIC_KEY_RECEIVED_TRANSACTIONS = "received_transactions";
  public static final String METRIC_KEY_SENT_TRANSACTIONS = "sent_transactions";
  private long mCpuTime;
  private long mExecTime;
  private Bundle mPerfMeasurement;
  private Bundle mPerfSnapshot;
  private PerformanceResultsWriter mPerfWriter;
  private long mSnapshotCpuTime;
  private long mSnapshotExecTime;
  
  public PerformanceCollector() {}
  
  public PerformanceCollector(PerformanceResultsWriter paramPerformanceResultsWriter)
  {
    setPerformanceResultsWriter(paramPerformanceResultsWriter);
  }
  
  private void endPerformanceSnapshot()
  {
    mSnapshotCpuTime = (Process.getElapsedCpuTime() - mSnapshotCpuTime);
    mSnapshotExecTime = (SystemClock.uptimeMillis() - mSnapshotExecTime);
    stopAllocCounting();
    long l1 = Debug.getNativeHeapSize() / 1024L;
    long l2 = Debug.getNativeHeapAllocatedSize() / 1024L;
    long l3 = Debug.getNativeHeapFreeSize() / 1024L;
    Debug.MemoryInfo localMemoryInfo = new Debug.MemoryInfo();
    Debug.getMemoryInfo(localMemoryInfo);
    Object localObject1 = Runtime.getRuntime();
    long l4 = ((Runtime)localObject1).totalMemory() / 1024L;
    long l5 = ((Runtime)localObject1).freeMemory() / 1024L;
    long l6 = l4 - l5;
    Object localObject2 = getBinderCounts();
    localObject1 = ((Bundle)localObject2).keySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject3 = (String)((Iterator)localObject1).next();
      mPerfSnapshot.putLong((String)localObject3, ((Bundle)localObject2).getLong((String)localObject3));
    }
    Bundle localBundle = getAllocCounts();
    Object localObject3 = localBundle.keySet().iterator();
    localObject1 = localObject2;
    while (((Iterator)localObject3).hasNext())
    {
      localObject2 = (String)((Iterator)localObject3).next();
      mPerfSnapshot.putLong((String)localObject2, localBundle.getLong((String)localObject2));
    }
    mPerfSnapshot.putLong("execution_time", mSnapshotExecTime);
    mPerfSnapshot.putLong("cpu_time", mSnapshotCpuTime);
    mPerfSnapshot.putLong("native_size", l1);
    mPerfSnapshot.putLong("native_allocated", l2);
    mPerfSnapshot.putLong("native_free", l3);
    mPerfSnapshot.putLong("native_pss", nativePss);
    mPerfSnapshot.putLong("native_private_dirty", nativePrivateDirty);
    mPerfSnapshot.putLong("native_shared_dirty", nativeSharedDirty);
    mPerfSnapshot.putLong("java_size", l4);
    mPerfSnapshot.putLong("java_allocated", l6);
    mPerfSnapshot.putLong("java_free", l5);
    mPerfSnapshot.putLong("java_pss", dalvikPss);
    mPerfSnapshot.putLong("java_private_dirty", dalvikPrivateDirty);
    mPerfSnapshot.putLong("java_shared_dirty", dalvikSharedDirty);
    mPerfSnapshot.putLong("other_pss", otherPss);
    mPerfSnapshot.putLong("other_private_dirty", otherPrivateDirty);
    mPerfSnapshot.putLong("other_shared_dirty", otherSharedDirty);
  }
  
  private static Bundle getAllocCounts()
  {
    Bundle localBundle = new Bundle();
    localBundle.putLong("global_alloc_count", Debug.getGlobalAllocCount());
    localBundle.putLong("global_alloc_size", Debug.getGlobalAllocSize());
    localBundle.putLong("global_freed_count", Debug.getGlobalFreedCount());
    localBundle.putLong("global_freed_size", Debug.getGlobalFreedSize());
    localBundle.putLong("gc_invocation_count", Debug.getGlobalGcInvocationCount());
    return localBundle;
  }
  
  private static Bundle getBinderCounts()
  {
    Bundle localBundle = new Bundle();
    localBundle.putLong("sent_transactions", Debug.getBinderSentTransactions());
    localBundle.putLong("received_transactions", Debug.getBinderReceivedTransactions());
    return localBundle;
  }
  
  private static void startAllocCounting()
  {
    Runtime.getRuntime().gc();
    Runtime.getRuntime().runFinalization();
    Runtime.getRuntime().gc();
    Debug.resetAllCounts();
    Debug.startAllocCounting();
  }
  
  private void startPerformanceSnapshot()
  {
    mPerfSnapshot = new Bundle();
    Bundle localBundle1 = getBinderCounts();
    Iterator localIterator = localBundle1.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      Bundle localBundle2 = mPerfSnapshot;
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("pre_");
      localStringBuilder.append(str);
      localBundle2.putLong(localStringBuilder.toString(), localBundle1.getLong(str));
    }
    startAllocCounting();
    mSnapshotExecTime = SystemClock.uptimeMillis();
    mSnapshotCpuTime = Process.getElapsedCpuTime();
  }
  
  private static void stopAllocCounting()
  {
    Runtime.getRuntime().gc();
    Runtime.getRuntime().runFinalization();
    Runtime.getRuntime().gc();
    Debug.stopAllocCounting();
  }
  
  public Bundle addIteration(String paramString)
  {
    mCpuTime = (Process.getElapsedCpuTime() - mCpuTime);
    mExecTime = (SystemClock.uptimeMillis() - mExecTime);
    Bundle localBundle = new Bundle();
    localBundle.putString("label", paramString);
    localBundle.putLong("execution_time", mExecTime);
    localBundle.putLong("cpu_time", mCpuTime);
    mPerfMeasurement.getParcelableArrayList("iterations").add(localBundle);
    mExecTime = SystemClock.uptimeMillis();
    mCpuTime = Process.getElapsedCpuTime();
    return localBundle;
  }
  
  public void addMeasurement(String paramString, float paramFloat)
  {
    if (mPerfWriter != null) {
      mPerfWriter.writeMeasurement(paramString, paramFloat);
    }
  }
  
  public void addMeasurement(String paramString, long paramLong)
  {
    if (mPerfWriter != null) {
      mPerfWriter.writeMeasurement(paramString, paramLong);
    }
  }
  
  public void addMeasurement(String paramString1, String paramString2)
  {
    if (mPerfWriter != null) {
      mPerfWriter.writeMeasurement(paramString1, paramString2);
    }
  }
  
  public void beginSnapshot(String paramString)
  {
    if (mPerfWriter != null) {
      mPerfWriter.writeBeginSnapshot(paramString);
    }
    startPerformanceSnapshot();
  }
  
  public Bundle endSnapshot()
  {
    endPerformanceSnapshot();
    if (mPerfWriter != null) {
      mPerfWriter.writeEndSnapshot(mPerfSnapshot);
    }
    return mPerfSnapshot;
  }
  
  public void setPerformanceResultsWriter(PerformanceResultsWriter paramPerformanceResultsWriter)
  {
    mPerfWriter = paramPerformanceResultsWriter;
  }
  
  public void startTiming(String paramString)
  {
    if (mPerfWriter != null) {
      mPerfWriter.writeStartTiming(paramString);
    }
    mPerfMeasurement = new Bundle();
    mPerfMeasurement.putParcelableArrayList("iterations", new ArrayList());
    mExecTime = SystemClock.uptimeMillis();
    mCpuTime = Process.getElapsedCpuTime();
  }
  
  public Bundle stopTiming(String paramString)
  {
    addIteration(paramString);
    if (mPerfWriter != null) {
      mPerfWriter.writeStopTiming(mPerfMeasurement);
    }
    return mPerfMeasurement;
  }
  
  public static abstract interface PerformanceResultsWriter
  {
    public abstract void writeBeginSnapshot(String paramString);
    
    public abstract void writeEndSnapshot(Bundle paramBundle);
    
    public abstract void writeMeasurement(String paramString, float paramFloat);
    
    public abstract void writeMeasurement(String paramString, long paramLong);
    
    public abstract void writeMeasurement(String paramString1, String paramString2);
    
    public abstract void writeStartTiming(String paramString);
    
    public abstract void writeStopTiming(Bundle paramBundle);
  }
}
