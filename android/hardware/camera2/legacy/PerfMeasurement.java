package android.hardware.camera2.legacy;

import android.os.SystemClock;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class PerfMeasurement
{
  public static final int DEFAULT_MAX_QUERIES = 3;
  private static final long FAILED_TIMING = -2L;
  private static final long NO_DURATION_YET = -1L;
  private static final String TAG = "PerfMeasurement";
  private ArrayList<Long> mCollectedCpuDurations = new ArrayList();
  private ArrayList<Long> mCollectedGpuDurations = new ArrayList();
  private ArrayList<Long> mCollectedTimestamps = new ArrayList();
  private int mCompletedQueryCount = 0;
  private Queue<Long> mCpuDurationsQueue = new LinkedList();
  private final long mNativeContext;
  private long mStartTimeNs;
  private Queue<Long> mTimestampQueue = new LinkedList();
  
  public PerfMeasurement()
  {
    mNativeContext = nativeCreateContext(3);
  }
  
  public PerfMeasurement(int paramInt)
  {
    if (paramInt >= 1)
    {
      mNativeContext = nativeCreateContext(paramInt);
      return;
    }
    throw new IllegalArgumentException("maxQueries is less than 1");
  }
  
  private long getNextGlDuration()
  {
    long l = nativeGetNextGlDuration(mNativeContext);
    if (l > 0L) {
      mCompletedQueryCount += 1;
    }
    return l;
  }
  
  public static boolean isGlTimingSupported()
  {
    return nativeQuerySupport();
  }
  
  private static native long nativeCreateContext(int paramInt);
  
  private static native void nativeDeleteContext(long paramLong);
  
  protected static native long nativeGetNextGlDuration(long paramLong);
  
  private static native boolean nativeQuerySupport();
  
  protected static native void nativeStartGlTimer(long paramLong);
  
  protected static native void nativeStopGlTimer(long paramLong);
  
  public void addTimestamp(long paramLong)
  {
    mTimestampQueue.add(Long.valueOf(paramLong));
  }
  
  /* Error */
  public void dumpPerformanceData(String paramString)
  {
    // Byte code:
    //   0: new 102	java/io/BufferedWriter
    //   3: astore_2
    //   4: new 104	java/io/FileWriter
    //   7: astore_3
    //   8: aload_3
    //   9: aload_1
    //   10: invokespecial 105	java/io/FileWriter:<init>	(Ljava/lang/String;)V
    //   13: aload_2
    //   14: aload_3
    //   15: invokespecial 108	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
    //   18: aconst_null
    //   19: astore 4
    //   21: aload 4
    //   23: astore_3
    //   24: aload_2
    //   25: ldc 110
    //   27: invokevirtual 113	java/io/BufferedWriter:write	(Ljava/lang/String;)V
    //   30: iconst_0
    //   31: istore 5
    //   33: aload 4
    //   35: astore_3
    //   36: iload 5
    //   38: aload_0
    //   39: getfield 41	android/hardware/camera2/legacy/PerfMeasurement:mCollectedGpuDurations	Ljava/util/ArrayList;
    //   42: invokevirtual 117	java/util/ArrayList:size	()I
    //   45: if_icmpge +61 -> 106
    //   48: aload 4
    //   50: astore_3
    //   51: aload_2
    //   52: ldc 119
    //   54: iconst_3
    //   55: anewarray 4	java/lang/Object
    //   58: dup
    //   59: iconst_0
    //   60: aload_0
    //   61: getfield 45	android/hardware/camera2/legacy/PerfMeasurement:mCollectedTimestamps	Ljava/util/ArrayList;
    //   64: iload 5
    //   66: invokevirtual 123	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   69: aastore
    //   70: dup
    //   71: iconst_1
    //   72: aload_0
    //   73: getfield 41	android/hardware/camera2/legacy/PerfMeasurement:mCollectedGpuDurations	Ljava/util/ArrayList;
    //   76: iload 5
    //   78: invokevirtual 123	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   81: aastore
    //   82: dup
    //   83: iconst_2
    //   84: aload_0
    //   85: getfield 43	android/hardware/camera2/legacy/PerfMeasurement:mCollectedCpuDurations	Ljava/util/ArrayList;
    //   88: iload 5
    //   90: invokevirtual 123	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   93: aastore
    //   94: invokestatic 129	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   97: invokevirtual 113	java/io/BufferedWriter:write	(Ljava/lang/String;)V
    //   100: iinc 5 1
    //   103: goto -70 -> 33
    //   106: aload 4
    //   108: astore_3
    //   109: aload_0
    //   110: getfield 45	android/hardware/camera2/legacy/PerfMeasurement:mCollectedTimestamps	Ljava/util/ArrayList;
    //   113: invokevirtual 132	java/util/ArrayList:clear	()V
    //   116: aload 4
    //   118: astore_3
    //   119: aload_0
    //   120: getfield 41	android/hardware/camera2/legacy/PerfMeasurement:mCollectedGpuDurations	Ljava/util/ArrayList;
    //   123: invokevirtual 132	java/util/ArrayList:clear	()V
    //   126: aload 4
    //   128: astore_3
    //   129: aload_0
    //   130: getfield 43	android/hardware/camera2/legacy/PerfMeasurement:mCollectedCpuDurations	Ljava/util/ArrayList;
    //   133: invokevirtual 132	java/util/ArrayList:clear	()V
    //   136: aload_2
    //   137: invokevirtual 135	java/io/BufferedWriter:close	()V
    //   140: goto +94 -> 234
    //   143: astore 4
    //   145: goto +11 -> 156
    //   148: astore 4
    //   150: aload 4
    //   152: astore_3
    //   153: aload 4
    //   155: athrow
    //   156: aload_3
    //   157: ifnull +19 -> 176
    //   160: aload_2
    //   161: invokevirtual 135	java/io/BufferedWriter:close	()V
    //   164: goto +16 -> 180
    //   167: astore_2
    //   168: aload_3
    //   169: aload_2
    //   170: invokevirtual 139	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   173: goto +7 -> 180
    //   176: aload_2
    //   177: invokevirtual 135	java/io/BufferedWriter:close	()V
    //   180: aload 4
    //   182: athrow
    //   183: astore_3
    //   184: new 141	java/lang/StringBuilder
    //   187: dup
    //   188: invokespecial 142	java/lang/StringBuilder:<init>	()V
    //   191: astore 4
    //   193: aload 4
    //   195: ldc -112
    //   197: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: pop
    //   201: aload 4
    //   203: aload_1
    //   204: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: pop
    //   208: aload 4
    //   210: ldc -106
    //   212: invokevirtual 148	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: pop
    //   216: aload 4
    //   218: aload_3
    //   219: invokevirtual 153	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   222: pop
    //   223: ldc 18
    //   225: aload 4
    //   227: invokevirtual 157	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   230: invokestatic 163	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   233: pop
    //   234: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	235	0	this	PerfMeasurement
    //   0	235	1	paramString	String
    //   3	158	2	localBufferedWriter	java.io.BufferedWriter
    //   167	10	2	localThrowable1	Throwable
    //   7	162	3	localObject1	Object
    //   183	36	3	localIOException	java.io.IOException
    //   19	108	4	localObject2	Object
    //   143	1	4	localObject3	Object
    //   148	33	4	localThrowable2	Throwable
    //   191	35	4	localStringBuilder	StringBuilder
    //   31	70	5	i	int
    // Exception table:
    //   from	to	target	type
    //   24	30	143	finally
    //   36	48	143	finally
    //   51	100	143	finally
    //   109	116	143	finally
    //   119	126	143	finally
    //   129	136	143	finally
    //   153	156	143	finally
    //   24	30	148	java/lang/Throwable
    //   36	48	148	java/lang/Throwable
    //   51	100	148	java/lang/Throwable
    //   109	116	148	java/lang/Throwable
    //   119	126	148	java/lang/Throwable
    //   129	136	148	java/lang/Throwable
    //   160	164	167	java/lang/Throwable
    //   0	18	183	java/io/IOException
    //   136	140	183	java/io/IOException
    //   160	164	183	java/io/IOException
    //   168	173	183	java/io/IOException
    //   176	180	183	java/io/IOException
    //   180	183	183	java/io/IOException
  }
  
  protected void finalize()
  {
    nativeDeleteContext(mNativeContext);
  }
  
  public int getCompletedQueryCount()
  {
    return mCompletedQueryCount;
  }
  
  public void startTimer()
  {
    nativeStartGlTimer(mNativeContext);
    mStartTimeNs = SystemClock.elapsedRealtimeNanos();
  }
  
  public void stopTimer()
  {
    long l1 = SystemClock.elapsedRealtimeNanos();
    mCpuDurationsQueue.add(Long.valueOf(l1 - mStartTimeNs));
    nativeStopGlTimer(mNativeContext);
    long l2 = getNextGlDuration();
    if (l2 > 0L)
    {
      mCollectedGpuDurations.add(Long.valueOf(l2));
      ArrayList localArrayList = mCollectedTimestamps;
      boolean bool = mTimestampQueue.isEmpty();
      long l3 = -1L;
      if (bool) {
        l1 = -1L;
      } else {
        l1 = ((Long)mTimestampQueue.poll()).longValue();
      }
      localArrayList.add(Long.valueOf(l1));
      localArrayList = mCollectedCpuDurations;
      if (mCpuDurationsQueue.isEmpty()) {
        l1 = l3;
      } else {
        l1 = ((Long)mCpuDurationsQueue.poll()).longValue();
      }
      localArrayList.add(Long.valueOf(l1));
    }
    if (l2 == -2L)
    {
      if (!mTimestampQueue.isEmpty()) {
        mTimestampQueue.poll();
      }
      if (!mCpuDurationsQueue.isEmpty()) {
        mCpuDurationsQueue.poll();
      }
    }
  }
}
