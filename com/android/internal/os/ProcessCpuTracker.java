package com.android.internal.os;

import android.os.FileUtils;
import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import android.system.Os;
import android.system.OsConstants;
import android.util.Slog;
import com.android.internal.util.FastPrintWriter;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ProcessCpuTracker
{
  private static final boolean DEBUG = false;
  private static final int[] LOAD_AVERAGE_FORMAT = { 16416, 16416, 16416 };
  private static final int[] PROCESS_FULL_STATS_FORMAT;
  static final int PROCESS_FULL_STAT_MAJOR_FAULTS = 2;
  static final int PROCESS_FULL_STAT_MINOR_FAULTS = 1;
  static final int PROCESS_FULL_STAT_STIME = 4;
  static final int PROCESS_FULL_STAT_UTIME = 3;
  static final int PROCESS_FULL_STAT_VSIZE = 5;
  private static final int[] PROCESS_STATS_FORMAT = { 32, 544, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224 };
  static final int PROCESS_STAT_MAJOR_FAULTS = 1;
  static final int PROCESS_STAT_MINOR_FAULTS = 0;
  static final int PROCESS_STAT_STIME = 3;
  static final int PROCESS_STAT_UTIME = 2;
  private static final int[] SYSTEM_CPU_FORMAT;
  private static final String TAG = "ProcessCpuTracker";
  private static final boolean localLOGV = false;
  private static final Comparator<Stats> sLoadComparator = new Comparator()
  {
    public final int compare(ProcessCpuTracker.Stats paramAnonymousStats1, ProcessCpuTracker.Stats paramAnonymousStats2)
    {
      int i = rel_utime + rel_stime;
      int j = rel_utime + rel_stime;
      int k = 1;
      int m = 1;
      int n = 1;
      if (i != j)
      {
        if (i > j) {
          n = -1;
        }
        return n;
      }
      if (added != added)
      {
        n = k;
        if (added) {
          n = -1;
        }
        return n;
      }
      if (removed != removed)
      {
        n = m;
        if (added) {
          n = -1;
        }
        return n;
      }
      return 0;
    }
  };
  private long mBaseIdleTime;
  private long mBaseIoWaitTime;
  private long mBaseIrqTime;
  private long mBaseSoftIrqTime;
  private long mBaseSystemTime;
  private long mBaseUserTime;
  private byte[] mBuffer = new byte['က'];
  private int[] mCurPids;
  private int[] mCurThreadPids;
  private long mCurrentSampleRealTime;
  private long mCurrentSampleTime;
  private long mCurrentSampleWallTime;
  private boolean mFirst = true;
  private final boolean mIncludeThreads;
  private final long mJiffyMillis;
  private long mLastSampleRealTime;
  private long mLastSampleTime;
  private long mLastSampleWallTime;
  private float mLoad1 = 0.0F;
  private float mLoad15 = 0.0F;
  private float mLoad5 = 0.0F;
  private final float[] mLoadAverageData = new float[3];
  private final ArrayList<Stats> mProcStats = new ArrayList();
  private final long[] mProcessFullStatsData = new long[6];
  private final String[] mProcessFullStatsStringData = new String[6];
  private final long[] mProcessStatsData = new long[4];
  private int mRelIdleTime;
  private int mRelIoWaitTime;
  private int mRelIrqTime;
  private int mRelSoftIrqTime;
  private boolean mRelStatsAreGood;
  private int mRelSystemTime;
  private int mRelUserTime;
  private final long[] mSinglePidStatsData = new long[4];
  private final long[] mSystemCpuData = new long[7];
  private final ArrayList<Stats> mWorkingProcs = new ArrayList();
  private boolean mWorkingProcsSorted;
  
  static
  {
    PROCESS_FULL_STATS_FORMAT = new int[] { 32, 4640, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224, 32, 32, 32, 32, 32, 32, 32, 8224 };
    SYSTEM_CPU_FORMAT = new int[] { 288, 8224, 8224, 8224, 8224, 8224, 8224, 8224 };
  }
  
  public ProcessCpuTracker(boolean paramBoolean)
  {
    mIncludeThreads = paramBoolean;
    mJiffyMillis = (1000L / Os.sysconf(OsConstants._SC_CLK_TCK));
  }
  
  private int[] collectStats(String paramString, int paramInt, boolean paramBoolean, int[] paramArrayOfInt, ArrayList<Stats> paramArrayList)
  {
    int i = paramInt;
    paramArrayOfInt = Process.getPids(paramString, paramArrayOfInt);
    if (paramArrayOfInt == null) {
      j = 0;
    } else {
      j = paramArrayOfInt.length;
    }
    int k = j;
    int j = paramArrayList.size();
    int m = 0;
    int n = 0;
    int i1 = i;
    i = n;
    for (;;)
    {
      paramString = paramArrayList;
      if (i >= k) {
        break;
      }
      n = paramArrayOfInt[i];
      if (n < 0) {
        break;
      }
      if (m < j) {
        paramString = (Stats)paramString.get(m);
      } else {
        paramString = null;
      }
      Object localObject1;
      Object localObject2;
      boolean bool;
      if ((paramString != null) && (pid == n))
      {
        added = false;
        working = false;
        if (interesting)
        {
          long l1 = SystemClock.uptimeMillis();
          localObject1 = mProcessStatsData;
          if (Process.readProcFile(statFile.toString(), PROCESS_STATS_FORMAT, null, (long[])localObject1, null))
          {
            long l2;
            long l3;
            long l4;
            long l5;
            for (;;)
            {
              l2 = localObject1[0];
              l3 = localObject1[1];
              l4 = localObject1[2] * mJiffyMillis;
              l5 = localObject1[3];
              l5 = mJiffyMillis * l5;
              if ((l4 != base_utime) || (l5 != base_stime)) {
                break;
              }
              rel_utime = 0;
              rel_stime = 0;
              rel_minfaults = 0;
              rel_majfaults = 0;
              if (active) {
                active = false;
              }
            }
            if (!active) {
              active = true;
            }
            if (i1 < 0)
            {
              getName(paramString, cmdlineFile);
              if (threadStats != null)
              {
                localObject2 = threadsDir;
                int[] arrayOfInt = mCurThreadPids;
                localObject1 = threadStats;
                bool = true;
                mCurThreadPids = collectStats((String)localObject2, n, false, arrayOfInt, (ArrayList)localObject1);
                break label336;
              }
            }
            bool = true;
            label336:
            rel_uptime = (l1 - base_uptime);
            base_uptime = l1;
            rel_utime = ((int)(l4 - base_utime));
            rel_stime = ((int)(l5 - base_stime));
            base_utime = l4;
            base_stime = l5;
            rel_minfaults = ((int)(l2 - base_minfaults));
            rel_majfaults = ((int)(l3 - base_majfaults));
            base_minfaults = l2;
            base_majfaults = l3;
            working = bool;
          }
        }
        m++;
      }
      for (;;)
      {
        break;
        if ((paramString != null) && (pid <= n))
        {
          rel_utime = 0;
          rel_stime = 0;
          rel_minfaults = 0;
          rel_majfaults = 0;
          removed = true;
          working = true;
          paramArrayList.remove(m);
          j--;
          i--;
        }
        else
        {
          bool = mIncludeThreads;
          i1 = paramInt;
          paramString = new Stats(n, i1, bool);
          paramArrayList.add(m, paramString);
          j++;
          localObject2 = mProcessFullStatsStringData;
          localObject1 = mProcessFullStatsData;
          base_uptime = SystemClock.uptimeMillis();
          if (Process.readProcFile(statFile.toString(), PROCESS_FULL_STATS_FORMAT, (String[])localObject2, (long[])localObject1, null))
          {
            vsize = localObject1[5];
            interesting = true;
            baseName = localObject2[0];
            base_minfaults = localObject1[1];
            base_majfaults = localObject1[2];
            base_utime = (localObject1[3] * mJiffyMillis);
            base_stime = (localObject1[4] * mJiffyMillis);
          }
          else
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Skipping unknown process pid ");
            ((StringBuilder)localObject1).append(n);
            Slog.w("ProcessCpuTracker", ((StringBuilder)localObject1).toString());
            baseName = "<unknown>";
            base_stime = 0L;
            base_utime = 0L;
            base_majfaults = 0L;
            base_minfaults = 0L;
          }
          if (i1 < 0)
          {
            getName(paramString, cmdlineFile);
            if (threadStats != null) {
              mCurThreadPids = collectStats(threadsDir, n, true, mCurThreadPids, threadStats);
            }
          }
          else if (interesting)
          {
            name = baseName;
            nameWidth = onMeasureProcessName(name);
          }
          rel_utime = 0;
          rel_stime = 0;
          rel_minfaults = 0;
          rel_majfaults = 0;
          added = true;
          if ((!paramBoolean) && (interesting)) {
            working = true;
          }
          m++;
        }
      }
      i++;
      i1 = paramInt;
    }
    while (m < j)
    {
      paramArrayList = (Stats)paramString.get(m);
      rel_utime = 0;
      rel_stime = 0;
      rel_minfaults = 0;
      rel_majfaults = 0;
      removed = true;
      working = true;
      paramString.remove(m);
      j--;
    }
    return paramArrayOfInt;
  }
  
  private void getName(Stats paramStats, String paramString)
  {
    String str1 = name;
    String str2;
    if ((name != null) && (!name.equals("app_process")))
    {
      str2 = str1;
      if (!name.equals("<pre-initialized>")) {}
    }
    else
    {
      str2 = readFile(paramString, '\000');
      paramString = str1;
      if (str2 != null)
      {
        paramString = str1;
        if (str2.length() > 1)
        {
          int i = str2.lastIndexOf("/");
          paramString = str2;
          if (i > 0)
          {
            paramString = str2;
            if (i < str2.length() - 1) {
              paramString = str2.substring(i + 1);
            }
          }
        }
      }
      str2 = paramString;
      if (paramString == null) {
        str2 = baseName;
      }
    }
    if ((name == null) || (!str2.equals(name)))
    {
      name = str2;
      nameWidth = onMeasureProcessName(name);
    }
  }
  
  private void printProcessCPU(PrintWriter paramPrintWriter, String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9)
  {
    paramPrintWriter.print(paramString1);
    if (paramInt2 == 0) {
      paramInt2 = 1;
    }
    printRatio(paramPrintWriter, paramInt3 + paramInt4 + paramInt5 + paramInt6 + paramInt7, paramInt2);
    paramPrintWriter.print("% ");
    if (paramInt1 >= 0)
    {
      paramPrintWriter.print(paramInt1);
      paramPrintWriter.print("/");
    }
    paramPrintWriter.print(paramString2);
    paramPrintWriter.print(": ");
    printRatio(paramPrintWriter, paramInt3, paramInt2);
    paramPrintWriter.print("% user + ");
    printRatio(paramPrintWriter, paramInt4, paramInt2);
    paramPrintWriter.print("% kernel");
    if (paramInt5 > 0)
    {
      paramPrintWriter.print(" + ");
      printRatio(paramPrintWriter, paramInt5, paramInt2);
      paramPrintWriter.print("% iowait");
    }
    if (paramInt6 > 0)
    {
      paramPrintWriter.print(" + ");
      printRatio(paramPrintWriter, paramInt6, paramInt2);
      paramPrintWriter.print("% irq");
    }
    if (paramInt7 > 0)
    {
      paramPrintWriter.print(" + ");
      printRatio(paramPrintWriter, paramInt7, paramInt2);
      paramPrintWriter.print("% softirq");
    }
    if ((paramInt8 > 0) || (paramInt9 > 0))
    {
      paramPrintWriter.print(" / faults:");
      if (paramInt8 > 0)
      {
        paramPrintWriter.print(" ");
        paramPrintWriter.print(paramInt8);
        paramPrintWriter.print(" minor");
      }
      if (paramInt9 > 0)
      {
        paramPrintWriter.print(" ");
        paramPrintWriter.print(paramInt9);
        paramPrintWriter.print(" major");
      }
    }
    paramPrintWriter.println();
  }
  
  private void printRatio(PrintWriter paramPrintWriter, long paramLong1, long paramLong2)
  {
    paramLong1 = 1000L * paramLong1 / paramLong2;
    paramLong2 = paramLong1 / 10L;
    paramPrintWriter.print(paramLong2);
    if (paramLong2 < 10L)
    {
      paramLong1 -= 10L * paramLong2;
      if (paramLong1 != 0L)
      {
        paramPrintWriter.print('.');
        paramPrintWriter.print(paramLong1);
      }
    }
  }
  
  /* Error */
  private String readFile(String paramString, char paramChar)
  {
    // Byte code:
    //   0: invokestatic 385	android/os/StrictMode:allowThreadDiskReads	()Landroid/os/StrictMode$ThreadPolicy;
    //   3: astore_3
    //   4: aconst_null
    //   5: astore 4
    //   7: aconst_null
    //   8: astore 5
    //   10: aconst_null
    //   11: astore 6
    //   13: aload 6
    //   15: astore 7
    //   17: aload 4
    //   19: astore 8
    //   21: aload 5
    //   23: astore 9
    //   25: new 387	java/io/FileInputStream
    //   28: astore 10
    //   30: aload 6
    //   32: astore 7
    //   34: aload 4
    //   36: astore 8
    //   38: aload 5
    //   40: astore 9
    //   42: aload 10
    //   44: aload_1
    //   45: invokespecial 389	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   48: aload 10
    //   50: astore_1
    //   51: aload_1
    //   52: astore 7
    //   54: aload_1
    //   55: astore 8
    //   57: aload_1
    //   58: astore 9
    //   60: aload_1
    //   61: aload_0
    //   62: getfield 137	com/android/internal/os/ProcessCpuTracker:mBuffer	[B
    //   65: invokevirtual 393	java/io/FileInputStream:read	([B)I
    //   68: istore 11
    //   70: aload_1
    //   71: astore 7
    //   73: aload_1
    //   74: astore 8
    //   76: aload_1
    //   77: astore 9
    //   79: aload_1
    //   80: invokevirtual 396	java/io/FileInputStream:close	()V
    //   83: aload_1
    //   84: astore 9
    //   86: iload 11
    //   88: ifle +99 -> 187
    //   91: iconst_0
    //   92: istore 12
    //   94: iload 12
    //   96: iload 11
    //   98: if_icmpge +32 -> 130
    //   101: aload_1
    //   102: astore 7
    //   104: aload_1
    //   105: astore 8
    //   107: aload_1
    //   108: astore 9
    //   110: aload_0
    //   111: getfield 137	com/android/internal/os/ProcessCpuTracker:mBuffer	[B
    //   114: iload 12
    //   116: baload
    //   117: iload_2
    //   118: if_icmpne +6 -> 124
    //   121: goto +9 -> 130
    //   124: iinc 12 1
    //   127: goto -33 -> 94
    //   130: aload_1
    //   131: astore 7
    //   133: aload_1
    //   134: astore 8
    //   136: aload_1
    //   137: astore 9
    //   139: new 112	java/lang/String
    //   142: dup
    //   143: aload_0
    //   144: getfield 137	com/android/internal/os/ProcessCpuTracker:mBuffer	[B
    //   147: iconst_0
    //   148: iload 12
    //   150: invokespecial 399	java/lang/String:<init>	([BII)V
    //   153: astore 6
    //   155: aload_1
    //   156: invokestatic 405	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   159: aload_3
    //   160: invokestatic 409	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   163: aload 6
    //   165: areturn
    //   166: astore_1
    //   167: aload 7
    //   169: invokestatic 405	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   172: aload_3
    //   173: invokestatic 409	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   176: aload_1
    //   177: athrow
    //   178: astore_1
    //   179: aload 8
    //   181: astore 9
    //   183: goto +4 -> 187
    //   186: astore_1
    //   187: aload 9
    //   189: invokestatic 405	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   192: aload_3
    //   193: invokestatic 409	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   196: aconst_null
    //   197: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	198	0	this	ProcessCpuTracker
    //   0	198	1	paramString	String
    //   0	198	2	paramChar	char
    //   3	190	3	localThreadPolicy	StrictMode.ThreadPolicy
    //   5	30	4	localObject1	Object
    //   8	31	5	localObject2	Object
    //   11	153	6	str1	String
    //   15	153	7	str2	String
    //   19	161	8	localObject3	Object
    //   23	165	9	localObject4	Object
    //   28	21	10	localFileInputStream	java.io.FileInputStream
    //   68	31	11	i	int
    //   92	57	12	j	int
    // Exception table:
    //   from	to	target	type
    //   25	30	166	finally
    //   42	48	166	finally
    //   60	70	166	finally
    //   79	83	166	finally
    //   110	121	166	finally
    //   139	155	166	finally
    //   25	30	178	java/io/IOException
    //   42	48	178	java/io/IOException
    //   60	70	178	java/io/IOException
    //   79	83	178	java/io/IOException
    //   110	121	178	java/io/IOException
    //   139	155	178	java/io/IOException
    //   25	30	186	java/io/FileNotFoundException
    //   42	48	186	java/io/FileNotFoundException
    //   60	70	186	java/io/FileNotFoundException
    //   79	83	186	java/io/FileNotFoundException
    //   110	121	186	java/io/FileNotFoundException
    //   139	155	186	java/io/FileNotFoundException
  }
  
  final void buildWorkingProcs()
  {
    if (!mWorkingProcsSorted)
    {
      mWorkingProcs.clear();
      int i = mProcStats.size();
      for (int j = 0; j < i; j++)
      {
        Stats localStats1 = (Stats)mProcStats.get(j);
        if (working)
        {
          mWorkingProcs.add(localStats1);
          if ((threadStats != null) && (threadStats.size() > 1))
          {
            workingThreads.clear();
            int k = threadStats.size();
            for (int m = 0; m < k; m++)
            {
              Stats localStats2 = (Stats)threadStats.get(m);
              if (working) {
                workingThreads.add(localStats2);
              }
            }
            Collections.sort(workingThreads, sLoadComparator);
          }
        }
      }
      Collections.sort(mWorkingProcs, sLoadComparator);
      mWorkingProcsSorted = true;
    }
  }
  
  public final int countStats()
  {
    return mProcStats.size();
  }
  
  public final int countWorkingStats()
  {
    buildWorkingProcs();
    return mWorkingProcs.size();
  }
  
  public long getCpuTimeForPid(int paramInt)
  {
    synchronized (mSinglePidStatsData)
    {
      Object localObject1 = new java/lang/StringBuilder;
      ((StringBuilder)localObject1).<init>();
      ((StringBuilder)localObject1).append("/proc/");
      ((StringBuilder)localObject1).append(paramInt);
      ((StringBuilder)localObject1).append("/stat");
      localObject1 = ((StringBuilder)localObject1).toString();
      long[] arrayOfLong2 = mSinglePidStatsData;
      if (Process.readProcFile((String)localObject1, PROCESS_STATS_FORMAT, null, arrayOfLong2, null))
      {
        long l1 = arrayOfLong2[2];
        long l2 = arrayOfLong2[3];
        long l3 = mJiffyMillis;
        return l3 * (l1 + l2);
      }
      return 0L;
    }
  }
  
  public final int getLastIdleTime()
  {
    return mRelIdleTime;
  }
  
  public final int getLastIoWaitTime()
  {
    return mRelIoWaitTime;
  }
  
  public final int getLastIrqTime()
  {
    return mRelIrqTime;
  }
  
  public final int getLastSoftIrqTime()
  {
    return mRelSoftIrqTime;
  }
  
  public final int getLastSystemTime()
  {
    return mRelSystemTime;
  }
  
  public final int getLastUserTime()
  {
    return mRelUserTime;
  }
  
  public final Stats getStats(int paramInt)
  {
    return (Stats)mProcStats.get(paramInt);
  }
  
  public final List<Stats> getStats(FilterStats paramFilterStats)
  {
    ArrayList localArrayList = new ArrayList(mProcStats.size());
    int i = mProcStats.size();
    for (int j = 0; j < i; j++)
    {
      Stats localStats = (Stats)mProcStats.get(j);
      if (paramFilterStats.needed(localStats)) {
        localArrayList.add(localStats);
      }
    }
    return localArrayList;
  }
  
  public final float getTotalCpuPercent()
  {
    int i = mRelUserTime + mRelSystemTime + mRelIrqTime + mRelIdleTime;
    if (i <= 0) {
      return 0.0F;
    }
    return (mRelUserTime + mRelSystemTime + mRelIrqTime) * 100.0F / i;
  }
  
  public final Stats getWorkingStats(int paramInt)
  {
    return (Stats)mWorkingProcs.get(paramInt);
  }
  
  public final boolean hasGoodLastStats()
  {
    return mRelStatsAreGood;
  }
  
  public void init()
  {
    mFirst = true;
    update();
  }
  
  public void onLoadChanged(float paramFloat1, float paramFloat2, float paramFloat3) {}
  
  public int onMeasureProcessName(String paramString)
  {
    return 0;
  }
  
  public final String printCurrentLoad()
  {
    StringWriter localStringWriter = new StringWriter();
    FastPrintWriter localFastPrintWriter = new FastPrintWriter(localStringWriter, false, 128);
    localFastPrintWriter.print("Load: ");
    localFastPrintWriter.print(mLoad1);
    localFastPrintWriter.print(" / ");
    localFastPrintWriter.print(mLoad5);
    localFastPrintWriter.print(" / ");
    localFastPrintWriter.println(mLoad15);
    localFastPrintWriter.flush();
    return localStringWriter.toString();
  }
  
  public final String printCurrentState(long paramLong)
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    buildWorkingProcs();
    StringWriter localStringWriter = new StringWriter();
    FastPrintWriter localFastPrintWriter = new FastPrintWriter(localStringWriter, false, 1024);
    localFastPrintWriter.print("CPU usage from ");
    if (paramLong > mLastSampleTime)
    {
      localFastPrintWriter.print(paramLong - mLastSampleTime);
      localFastPrintWriter.print("ms to ");
      localFastPrintWriter.print(paramLong - mCurrentSampleTime);
      localFastPrintWriter.print("ms ago");
    }
    else
    {
      localFastPrintWriter.print(mLastSampleTime - paramLong);
      localFastPrintWriter.print("ms to ");
      localFastPrintWriter.print(mCurrentSampleTime - paramLong);
      localFastPrintWriter.print("ms later");
    }
    localFastPrintWriter.print(" (");
    localFastPrintWriter.print(localSimpleDateFormat.format(new Date(mLastSampleWallTime)));
    localFastPrintWriter.print(" to ");
    localFastPrintWriter.print(localSimpleDateFormat.format(new Date(mCurrentSampleWallTime)));
    localFastPrintWriter.print(")");
    long l1 = mCurrentSampleTime;
    long l2 = mLastSampleTime;
    long l3 = mCurrentSampleRealTime - mLastSampleRealTime;
    paramLong = 0L;
    if (l3 > 0L) {
      paramLong = (l1 - l2) * 100L / l3;
    }
    if (paramLong != 100L)
    {
      localFastPrintWriter.print(" with ");
      localFastPrintWriter.print(paramLong);
      localFastPrintWriter.print("% awake");
    }
    localFastPrintWriter.println(":");
    int i = mRelUserTime;
    int j = mRelSystemTime;
    int k = mRelIoWaitTime;
    int m = mRelIrqTime;
    int n = mRelSoftIrqTime;
    int i1 = mRelIdleTime;
    int i2 = mWorkingProcs.size();
    for (int i3 = 0; i3 < i2; i3++)
    {
      Stats localStats1 = (Stats)mWorkingProcs.get(i3);
      String str;
      if (added) {
        str = " +";
      }
      for (;;)
      {
        break;
        if (removed) {
          str = " -";
        } else {
          str = "  ";
        }
      }
      printProcessCPU(localFastPrintWriter, str, pid, name, (int)rel_uptime, rel_utime, rel_stime, 0, 0, 0, rel_minfaults, rel_majfaults);
      if ((!removed) && (workingThreads != null))
      {
        int i4 = workingThreads.size();
        for (int i5 = 0; i5 < i4; i5++)
        {
          Stats localStats2 = (Stats)workingThreads.get(i5);
          if (added) {
            str = "   +";
          }
          for (;;)
          {
            break;
            if (removed) {
              str = "   -";
            } else {
              str = "    ";
            }
          }
          printProcessCPU(localFastPrintWriter, str, pid, name, (int)rel_uptime, rel_utime, rel_stime, 0, 0, 0, 0, 0);
        }
      }
    }
    printProcessCPU(localFastPrintWriter, "", -1, "TOTAL", i + j + k + m + n + i1, mRelUserTime, mRelSystemTime, mRelIoWaitTime, mRelIrqTime, mRelSoftIrqTime, 0, 0);
    localFastPrintWriter.flush();
    return localStringWriter.toString();
  }
  
  public void update()
  {
    long l1 = SystemClock.uptimeMillis();
    long l2 = SystemClock.elapsedRealtime();
    long l3 = System.currentTimeMillis();
    Object localObject1 = mSystemCpuData;
    if (Process.readProcFile("/proc/stat", SYSTEM_CPU_FORMAT, null, (long[])localObject1, null))
    {
      long l4 = (localObject1[0] + localObject1[1]) * mJiffyMillis;
      long l5 = localObject1[2] * mJiffyMillis;
      long l6 = localObject1[3] * mJiffyMillis;
      long l7 = localObject1[4] * mJiffyMillis;
      long l8 = localObject1[5] * mJiffyMillis;
      long l9 = localObject1[6] * mJiffyMillis;
      mRelUserTime = ((int)(l4 - mBaseUserTime));
      mRelSystemTime = ((int)(l5 - mBaseSystemTime));
      mRelIoWaitTime = ((int)(l7 - mBaseIoWaitTime));
      mRelIrqTime = ((int)(l8 - mBaseIrqTime));
      mRelSoftIrqTime = ((int)(l9 - mBaseSoftIrqTime));
      mRelIdleTime = ((int)(l6 - mBaseIdleTime));
      mRelStatsAreGood = true;
      mBaseUserTime = l4;
      mBaseSystemTime = l5;
      mBaseIoWaitTime = l7;
      mBaseIrqTime = l8;
      mBaseSoftIrqTime = l9;
      mBaseIdleTime = l6;
    }
    mLastSampleTime = mCurrentSampleTime;
    mCurrentSampleTime = l1;
    mLastSampleRealTime = mCurrentSampleRealTime;
    mCurrentSampleRealTime = l2;
    mLastSampleWallTime = mCurrentSampleWallTime;
    mCurrentSampleWallTime = l3;
    StrictMode.ThreadPolicy localThreadPolicy = StrictMode.allowThreadDiskReads();
    try
    {
      mCurPids = collectStats("/proc", -1, mFirst, mCurPids, mProcStats);
      StrictMode.setThreadPolicy(localThreadPolicy);
      localObject1 = mLoadAverageData;
      if (Process.readProcFile("/proc/loadavg", LOAD_AVERAGE_FORMAT, null, null, (float[])localObject1))
      {
        float f1 = localObject1[0];
        float f2 = localObject1[1];
        float f3 = localObject1[2];
        if ((f1 != mLoad1) || (f2 != mLoad5) || (f3 != mLoad15))
        {
          mLoad1 = f1;
          mLoad5 = f2;
          mLoad15 = f3;
          onLoadChanged(f1, f2, f3);
        }
      }
      mWorkingProcsSorted = false;
      mFirst = false;
      return;
    }
    finally
    {
      StrictMode.setThreadPolicy(localThreadPolicy);
    }
  }
  
  public static abstract interface FilterStats
  {
    public abstract boolean needed(ProcessCpuTracker.Stats paramStats);
  }
  
  public static class Stats
  {
    public boolean active;
    public boolean added;
    public String baseName;
    public long base_majfaults;
    public long base_minfaults;
    public long base_stime;
    public long base_uptime;
    public long base_utime;
    public BatteryStatsImpl.Uid.Proc batteryStats;
    final String cmdlineFile;
    public boolean interesting;
    public String name;
    public int nameWidth;
    public final int pid;
    public int rel_majfaults;
    public int rel_minfaults;
    public int rel_stime;
    public long rel_uptime;
    public int rel_utime;
    public boolean removed;
    final String statFile;
    final ArrayList<Stats> threadStats;
    final String threadsDir;
    public final int uid;
    public long vsize;
    public boolean working;
    final ArrayList<Stats> workingThreads;
    
    Stats(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      pid = paramInt1;
      if (paramInt2 < 0)
      {
        File localFile = new File("/proc", Integer.toString(pid));
        statFile = new File(localFile, "stat").toString();
        cmdlineFile = new File(localFile, "cmdline").toString();
        threadsDir = new File(localFile, "task").toString();
        if (paramBoolean)
        {
          threadStats = new ArrayList();
          workingThreads = new ArrayList();
        }
        else
        {
          threadStats = null;
          workingThreads = null;
        }
      }
      else
      {
        statFile = new File(new File(new File(new File("/proc", Integer.toString(paramInt2)), "task"), Integer.toString(pid)), "stat").toString();
        cmdlineFile = null;
        threadsDir = null;
        threadStats = null;
        workingThreads = null;
      }
      uid = FileUtils.getUid(statFile.toString());
    }
  }
}
