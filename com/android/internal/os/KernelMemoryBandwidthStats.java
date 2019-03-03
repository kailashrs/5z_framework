package com.android.internal.os;

import android.text.TextUtils.SimpleStringSplitter;
import android.util.LongSparseLongArray;
import com.android.internal.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.IOException;

public class KernelMemoryBandwidthStats
{
  private static final boolean DEBUG = false;
  private static final String TAG = "KernelMemoryBandwidthStats";
  private static final String mSysfsFile = "/sys/kernel/memory_state_time/show_stat";
  protected final LongSparseLongArray mBandwidthEntries = new LongSparseLongArray();
  private boolean mStatsDoNotExist = false;
  
  public KernelMemoryBandwidthStats() {}
  
  public LongSparseLongArray getBandwidthEntries()
  {
    return mBandwidthEntries;
  }
  
  @VisibleForTesting
  public void parseStats(BufferedReader paramBufferedReader)
    throws IOException
  {
    TextUtils.SimpleStringSplitter localSimpleStringSplitter = new TextUtils.SimpleStringSplitter(' ');
    mBandwidthEntries.clear();
    for (;;)
    {
      String str = paramBufferedReader.readLine();
      if (str == null) {
        break;
      }
      localSimpleStringSplitter.setString(str);
      localSimpleStringSplitter.next();
      int i = 0;
      do
      {
        int j = mBandwidthEntries.indexOfKey(i);
        if (j >= 0) {
          mBandwidthEntries.put(i, mBandwidthEntries.valueAt(j) + Long.parseLong(localSimpleStringSplitter.next()) / 1000000L);
        } else {
          mBandwidthEntries.put(i, Long.parseLong(localSimpleStringSplitter.next()) / 1000000L);
        }
        i++;
      } while (localSimpleStringSplitter.hasNext());
    }
  }
  
  /* Error */
  public void updateStats()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	com/android/internal/os/KernelMemoryBandwidthStats:mStatsDoNotExist	Z
    //   4: ifeq +4 -> 8
    //   7: return
    //   8: invokestatic 94	android/os/SystemClock:uptimeMillis	()J
    //   11: lstore_1
    //   12: invokestatic 100	android/os/StrictMode:allowThreadDiskReads	()Landroid/os/StrictMode$ThreadPolicy;
    //   15: astore_3
    //   16: new 46	java/io/BufferedReader
    //   19: astore 4
    //   21: new 102	java/io/FileReader
    //   24: astore 5
    //   26: aload 5
    //   28: ldc 14
    //   30: invokespecial 104	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   33: aload 4
    //   35: aload 5
    //   37: invokespecial 107	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   40: aconst_null
    //   41: astore 5
    //   43: aload_0
    //   44: aload 4
    //   46: invokevirtual 109	com/android/internal/os/KernelMemoryBandwidthStats:parseStats	(Ljava/io/BufferedReader;)V
    //   49: aload 4
    //   51: invokevirtual 112	java/io/BufferedReader:close	()V
    //   54: goto +129 -> 183
    //   57: astore 6
    //   59: goto +12 -> 71
    //   62: astore 6
    //   64: aload 6
    //   66: astore 5
    //   68: aload 6
    //   70: athrow
    //   71: aload 5
    //   73: ifnull +23 -> 96
    //   76: aload 4
    //   78: invokevirtual 112	java/io/BufferedReader:close	()V
    //   81: goto +20 -> 101
    //   84: astore 4
    //   86: aload 5
    //   88: aload 4
    //   90: invokevirtual 116	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   93: goto +8 -> 101
    //   96: aload 4
    //   98: invokevirtual 112	java/io/BufferedReader:close	()V
    //   101: aload 6
    //   103: athrow
    //   104: astore 5
    //   106: goto +139 -> 245
    //   109: astore 5
    //   111: new 118	java/lang/StringBuilder
    //   114: astore 6
    //   116: aload 6
    //   118: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   121: aload 6
    //   123: ldc 121
    //   125: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   128: pop
    //   129: aload 6
    //   131: aload 5
    //   133: invokevirtual 128	java/io/IOException:getMessage	()Ljava/lang/String;
    //   136: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   139: pop
    //   140: ldc 11
    //   142: aload 6
    //   144: invokevirtual 131	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   147: invokestatic 137	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   150: pop
    //   151: aload_0
    //   152: getfield 26	com/android/internal/os/KernelMemoryBandwidthStats:mBandwidthEntries	Landroid/util/LongSparseLongArray;
    //   155: invokevirtual 44	android/util/LongSparseLongArray:clear	()V
    //   158: goto +25 -> 183
    //   161: astore 5
    //   163: ldc 11
    //   165: ldc -117
    //   167: invokestatic 142	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   170: pop
    //   171: aload_0
    //   172: getfield 26	com/android/internal/os/KernelMemoryBandwidthStats:mBandwidthEntries	Landroid/util/LongSparseLongArray;
    //   175: invokevirtual 44	android/util/LongSparseLongArray:clear	()V
    //   178: aload_0
    //   179: iconst_1
    //   180: putfield 28	com/android/internal/os/KernelMemoryBandwidthStats:mStatsDoNotExist	Z
    //   183: aload_3
    //   184: invokestatic 146	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   187: invokestatic 94	android/os/SystemClock:uptimeMillis	()J
    //   190: lload_1
    //   191: lsub
    //   192: lstore_1
    //   193: lload_1
    //   194: ldc2_w 147
    //   197: lcmp
    //   198: ifle +46 -> 244
    //   201: new 118	java/lang/StringBuilder
    //   204: dup
    //   205: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   208: astore 5
    //   210: aload 5
    //   212: ldc -106
    //   214: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: pop
    //   218: aload 5
    //   220: lload_1
    //   221: invokevirtual 153	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   224: pop
    //   225: aload 5
    //   227: ldc -101
    //   229: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   232: pop
    //   233: ldc 11
    //   235: aload 5
    //   237: invokevirtual 131	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   240: invokestatic 142	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   243: pop
    //   244: return
    //   245: aload_3
    //   246: invokestatic 146	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   249: aload 5
    //   251: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	252	0	this	KernelMemoryBandwidthStats
    //   11	210	1	l	long
    //   15	231	3	localThreadPolicy	android.os.StrictMode.ThreadPolicy
    //   19	58	4	localBufferedReader	BufferedReader
    //   84	13	4	localThrowable1	Throwable
    //   24	63	5	localObject1	Object
    //   104	1	5	localObject2	Object
    //   109	23	5	localIOException	IOException
    //   161	1	5	localFileNotFoundException	java.io.FileNotFoundException
    //   208	42	5	localStringBuilder1	StringBuilder
    //   57	1	6	localObject3	Object
    //   62	40	6	localThrowable2	Throwable
    //   114	29	6	localStringBuilder2	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   43	49	57	finally
    //   68	71	57	finally
    //   43	49	62	java/lang/Throwable
    //   76	81	84	java/lang/Throwable
    //   16	40	104	finally
    //   49	54	104	finally
    //   76	81	104	finally
    //   86	93	104	finally
    //   96	101	104	finally
    //   101	104	104	finally
    //   111	158	104	finally
    //   163	183	104	finally
    //   16	40	109	java/io/IOException
    //   49	54	109	java/io/IOException
    //   76	81	109	java/io/IOException
    //   86	93	109	java/io/IOException
    //   96	101	109	java/io/IOException
    //   101	104	109	java/io/IOException
    //   16	40	161	java/io/FileNotFoundException
    //   49	54	161	java/io/FileNotFoundException
    //   76	81	161	java/io/FileNotFoundException
    //   86	93	161	java/io/FileNotFoundException
    //   96	101	161	java/io/FileNotFoundException
    //   101	104	161	java/io/FileNotFoundException
  }
}
