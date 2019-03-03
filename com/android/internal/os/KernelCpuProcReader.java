package com.android.internal.os;

import com.android.internal.annotations.VisibleForTesting;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class KernelCpuProcReader
{
  private static final long DEFAULT_THROTTLE_INTERVAL = 3000L;
  private static final int ERROR_THRESHOLD = 5;
  private static final int INITIAL_BUFFER_SIZE = 8192;
  private static final int MAX_BUFFER_SIZE = 1048576;
  private static final String PROC_UID_ACTIVE_TIME = "/proc/uid_cpupower/concurrent_active_time";
  private static final String PROC_UID_CLUSTER_TIME = "/proc/uid_cpupower/concurrent_policy_time";
  private static final String PROC_UID_FREQ_TIME = "/proc/uid_cpupower/time_in_state";
  private static final String TAG = "KernelCpuProcReader";
  private static final KernelCpuProcReader mActiveTimeReader = new KernelCpuProcReader("/proc/uid_cpupower/concurrent_active_time");
  private static final KernelCpuProcReader mClusterTimeReader = new KernelCpuProcReader("/proc/uid_cpupower/concurrent_policy_time");
  private static final KernelCpuProcReader mFreqTimeReader = new KernelCpuProcReader("/proc/uid_cpupower/time_in_state");
  private ByteBuffer mBuffer;
  private int mErrors;
  private long mLastReadTime = Long.MIN_VALUE;
  private final Path mProc;
  private long mThrottleInterval = 3000L;
  
  @VisibleForTesting
  public KernelCpuProcReader(String paramString)
  {
    mProc = Paths.get(paramString, new String[0]);
    mBuffer = ByteBuffer.allocateDirect(8192);
    mBuffer.clear();
  }
  
  public static KernelCpuProcReader getActiveTimeReaderInstance()
  {
    return mActiveTimeReader;
  }
  
  public static KernelCpuProcReader getClusterTimeReaderInstance()
  {
    return mClusterTimeReader;
  }
  
  public static KernelCpuProcReader getFreqTimeReaderInstance()
  {
    return mFreqTimeReader;
  }
  
  private boolean resize()
  {
    if (mBuffer.capacity() >= 1048576) {
      return false;
    }
    mBuffer = ByteBuffer.allocateDirect(Math.min(mBuffer.capacity() << 1, 1048576));
    return true;
  }
  
  /* Error */
  public ByteBuffer readBytes()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 112	com/android/internal/os/KernelCpuProcReader:mErrors	I
    //   4: iconst_5
    //   5: if_icmplt +5 -> 10
    //   8: aconst_null
    //   9: areturn
    //   10: invokestatic 118	android/os/SystemClock:elapsedRealtime	()J
    //   13: aload_0
    //   14: getfield 61	com/android/internal/os/KernelCpuProcReader:mLastReadTime	J
    //   17: aload_0
    //   18: getfield 57	com/android/internal/os/KernelCpuProcReader:mThrottleInterval	J
    //   21: ladd
    //   22: lcmp
    //   23: ifge +46 -> 69
    //   26: aload_0
    //   27: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   30: invokevirtual 121	java/nio/ByteBuffer:limit	()I
    //   33: ifle +34 -> 67
    //   36: aload_0
    //   37: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   40: invokevirtual 121	java/nio/ByteBuffer:limit	()I
    //   43: aload_0
    //   44: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   47: invokevirtual 94	java/nio/ByteBuffer:capacity	()I
    //   50: if_icmpge +17 -> 67
    //   53: aload_0
    //   54: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   57: invokevirtual 124	java/nio/ByteBuffer:asReadOnlyBuffer	()Ljava/nio/ByteBuffer;
    //   60: invokestatic 130	java/nio/ByteOrder:nativeOrder	()Ljava/nio/ByteOrder;
    //   63: invokevirtual 134	java/nio/ByteBuffer:order	(Ljava/nio/ByteOrder;)Ljava/nio/ByteBuffer;
    //   66: areturn
    //   67: aconst_null
    //   68: areturn
    //   69: aload_0
    //   70: invokestatic 118	android/os/SystemClock:elapsedRealtime	()J
    //   73: putfield 61	com/android/internal/os/KernelCpuProcReader:mLastReadTime	J
    //   76: aload_0
    //   77: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   80: invokevirtual 83	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   83: pop
    //   84: invokestatic 139	android/os/StrictMode:allowThreadDiskReadsMask	()I
    //   87: istore_1
    //   88: aload_0
    //   89: getfield 71	com/android/internal/os/KernelCpuProcReader:mProc	Ljava/nio/file/Path;
    //   92: iconst_1
    //   93: anewarray 141	java/nio/file/OpenOption
    //   96: dup
    //   97: iconst_0
    //   98: getstatic 147	java/nio/file/StandardOpenOption:READ	Ljava/nio/file/StandardOpenOption;
    //   101: aastore
    //   102: invokestatic 153	java/nio/channels/FileChannel:open	(Ljava/nio/file/Path;[Ljava/nio/file/OpenOption;)Ljava/nio/channels/FileChannel;
    //   105: astore_2
    //   106: aload_2
    //   107: aload_0
    //   108: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   111: invokevirtual 157	java/nio/channels/FileChannel:read	(Ljava/nio/ByteBuffer;)I
    //   114: aload_0
    //   115: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   118: invokevirtual 94	java/nio/ByteBuffer:capacity	()I
    //   121: if_icmpne +77 -> 198
    //   124: aload_0
    //   125: invokespecial 159	com/android/internal/os/KernelCpuProcReader:resize	()Z
    //   128: ifne +61 -> 189
    //   131: aload_0
    //   132: aload_0
    //   133: getfield 112	com/android/internal/os/KernelCpuProcReader:mErrors	I
    //   136: iconst_1
    //   137: iadd
    //   138: putfield 112	com/android/internal/os/KernelCpuProcReader:mErrors	I
    //   141: new 161	java/lang/StringBuilder
    //   144: astore_3
    //   145: aload_3
    //   146: invokespecial 162	java/lang/StringBuilder:<init>	()V
    //   149: aload_3
    //   150: ldc -92
    //   152: invokevirtual 168	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: pop
    //   156: aload_3
    //   157: aload_0
    //   158: getfield 71	com/android/internal/os/KernelCpuProcReader:mProc	Ljava/nio/file/Path;
    //   161: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   164: pop
    //   165: ldc 28
    //   167: aload_3
    //   168: invokevirtual 175	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   171: invokestatic 181	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   174: pop
    //   175: aload_2
    //   176: ifnull +7 -> 183
    //   179: aload_2
    //   180: invokevirtual 184	java/nio/channels/FileChannel:close	()V
    //   183: iload_1
    //   184: invokestatic 188	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   187: aconst_null
    //   188: areturn
    //   189: aload_2
    //   190: lconst_0
    //   191: invokevirtual 192	java/nio/channels/FileChannel:position	(J)Ljava/nio/channels/FileChannel;
    //   194: pop
    //   195: goto -89 -> 106
    //   198: aload_2
    //   199: ifnull +7 -> 206
    //   202: aload_2
    //   203: invokevirtual 184	java/nio/channels/FileChannel:close	()V
    //   206: iload_1
    //   207: invokestatic 188	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   210: aload_0
    //   211: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   214: invokevirtual 195	java/nio/ByteBuffer:flip	()Ljava/nio/Buffer;
    //   217: pop
    //   218: aload_0
    //   219: getfield 79	com/android/internal/os/KernelCpuProcReader:mBuffer	Ljava/nio/ByteBuffer;
    //   222: invokevirtual 124	java/nio/ByteBuffer:asReadOnlyBuffer	()Ljava/nio/ByteBuffer;
    //   225: invokestatic 130	java/nio/ByteOrder:nativeOrder	()Ljava/nio/ByteOrder;
    //   228: invokevirtual 134	java/nio/ByteBuffer:order	(Ljava/nio/ByteOrder;)Ljava/nio/ByteBuffer;
    //   231: areturn
    //   232: astore 4
    //   234: aconst_null
    //   235: astore_3
    //   236: goto +8 -> 244
    //   239: astore_3
    //   240: aload_3
    //   241: athrow
    //   242: astore 4
    //   244: aload_2
    //   245: ifnull +27 -> 272
    //   248: aload_3
    //   249: ifnull +19 -> 268
    //   252: aload_2
    //   253: invokevirtual 184	java/nio/channels/FileChannel:close	()V
    //   256: goto +16 -> 272
    //   259: astore_2
    //   260: aload_3
    //   261: aload_2
    //   262: invokevirtual 199	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   265: goto +7 -> 272
    //   268: aload_2
    //   269: invokevirtual 184	java/nio/channels/FileChannel:close	()V
    //   272: aload 4
    //   274: athrow
    //   275: astore_3
    //   276: goto +111 -> 387
    //   279: astore_3
    //   280: aload_0
    //   281: aload_0
    //   282: getfield 112	com/android/internal/os/KernelCpuProcReader:mErrors	I
    //   285: iconst_1
    //   286: iadd
    //   287: putfield 112	com/android/internal/os/KernelCpuProcReader:mErrors	I
    //   290: new 161	java/lang/StringBuilder
    //   293: astore 4
    //   295: aload 4
    //   297: invokespecial 162	java/lang/StringBuilder:<init>	()V
    //   300: aload 4
    //   302: ldc -55
    //   304: invokevirtual 168	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   307: pop
    //   308: aload 4
    //   310: aload_0
    //   311: getfield 71	com/android/internal/os/KernelCpuProcReader:mProc	Ljava/nio/file/Path;
    //   314: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   317: pop
    //   318: ldc 28
    //   320: aload 4
    //   322: invokevirtual 175	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   325: aload_3
    //   326: invokestatic 204	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   329: pop
    //   330: iload_1
    //   331: invokestatic 188	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   334: aconst_null
    //   335: areturn
    //   336: astore_3
    //   337: aload_0
    //   338: aload_0
    //   339: getfield 112	com/android/internal/os/KernelCpuProcReader:mErrors	I
    //   342: iconst_1
    //   343: iadd
    //   344: putfield 112	com/android/internal/os/KernelCpuProcReader:mErrors	I
    //   347: new 161	java/lang/StringBuilder
    //   350: astore_3
    //   351: aload_3
    //   352: invokespecial 162	java/lang/StringBuilder:<init>	()V
    //   355: aload_3
    //   356: ldc -50
    //   358: invokevirtual 168	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: pop
    //   362: aload_3
    //   363: aload_0
    //   364: getfield 71	com/android/internal/os/KernelCpuProcReader:mProc	Ljava/nio/file/Path;
    //   367: invokevirtual 171	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   370: pop
    //   371: ldc 28
    //   373: aload_3
    //   374: invokevirtual 175	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   377: invokestatic 209	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   380: pop
    //   381: iload_1
    //   382: invokestatic 188	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   385: aconst_null
    //   386: areturn
    //   387: iload_1
    //   388: invokestatic 188	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   391: aload_3
    //   392: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	393	0	this	KernelCpuProcReader
    //   87	301	1	i	int
    //   105	148	2	localFileChannel	java.nio.channels.FileChannel
    //   259	10	2	localThrowable1	Throwable
    //   144	92	3	localStringBuilder1	StringBuilder
    //   239	22	3	localThrowable2	Throwable
    //   275	1	3	localObject1	Object
    //   279	47	3	localIOException	java.io.IOException
    //   336	1	3	localNoSuchFileException	java.nio.file.NoSuchFileException
    //   350	42	3	localStringBuilder2	StringBuilder
    //   232	1	4	localObject2	Object
    //   242	31	4	localObject3	Object
    //   293	28	4	localStringBuilder3	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   106	175	232	finally
    //   189	195	232	finally
    //   106	175	239	java/lang/Throwable
    //   189	195	239	java/lang/Throwable
    //   240	242	242	finally
    //   252	256	259	java/lang/Throwable
    //   88	106	275	finally
    //   179	183	275	finally
    //   202	206	275	finally
    //   252	256	275	finally
    //   260	265	275	finally
    //   268	272	275	finally
    //   272	275	275	finally
    //   280	330	275	finally
    //   337	381	275	finally
    //   88	106	279	java/io/IOException
    //   179	183	279	java/io/IOException
    //   202	206	279	java/io/IOException
    //   252	256	279	java/io/IOException
    //   260	265	279	java/io/IOException
    //   268	272	279	java/io/IOException
    //   272	275	279	java/io/IOException
    //   88	106	336	java/nio/file/NoSuchFileException
    //   88	106	336	java/io/FileNotFoundException
    //   179	183	336	java/nio/file/NoSuchFileException
    //   179	183	336	java/io/FileNotFoundException
    //   202	206	336	java/nio/file/NoSuchFileException
    //   202	206	336	java/io/FileNotFoundException
    //   252	256	336	java/nio/file/NoSuchFileException
    //   252	256	336	java/io/FileNotFoundException
    //   260	265	336	java/nio/file/NoSuchFileException
    //   260	265	336	java/io/FileNotFoundException
    //   268	272	336	java/nio/file/NoSuchFileException
    //   268	272	336	java/io/FileNotFoundException
    //   272	275	336	java/nio/file/NoSuchFileException
    //   272	275	336	java/io/FileNotFoundException
  }
  
  public void setThrottleInterval(long paramLong)
  {
    if (paramLong >= 0L) {
      mThrottleInterval = paramLong;
    }
  }
}
