package com.android.internal.os;

import android.system.Os;
import android.system.OsConstants;

public class KernelCpuSpeedReader
{
  private static final String TAG = "KernelCpuSpeedReader";
  private final long[] mDeltaSpeedTimesMs;
  private final long mJiffyMillis;
  private final long[] mLastSpeedTimesMs;
  private final int mNumSpeedSteps;
  private final String mProcFile;
  
  public KernelCpuSpeedReader(int paramInt1, int paramInt2)
  {
    mProcFile = String.format("/sys/devices/system/cpu/cpu%d/cpufreq/stats/time_in_state", new Object[] { Integer.valueOf(paramInt1) });
    mNumSpeedSteps = paramInt2;
    mLastSpeedTimesMs = new long[paramInt2];
    mDeltaSpeedTimesMs = new long[paramInt2];
    mJiffyMillis = (1000L / Os.sysconf(OsConstants._SC_CLK_TCK));
  }
  
  /* Error */
  public long[] readAbsolute()
  {
    // Byte code:
    //   0: invokestatic 82	android/os/StrictMode:allowThreadDiskReads	()Landroid/os/StrictMode$ThreadPolicy;
    //   3: astore_1
    //   4: aload_0
    //   5: getfield 53	com/android/internal/os/KernelCpuSpeedReader:mNumSpeedSteps	I
    //   8: newarray long
    //   10: astore_2
    //   11: new 84	java/io/BufferedReader
    //   14: astore_3
    //   15: new 86	java/io/FileReader
    //   18: astore 4
    //   20: aload 4
    //   22: aload_0
    //   23: getfield 51	com/android/internal/os/KernelCpuSpeedReader:mProcFile	Ljava/lang/String;
    //   26: invokespecial 89	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   29: aload_3
    //   30: aload 4
    //   32: invokespecial 92	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   35: aconst_null
    //   36: astore 5
    //   38: aload 5
    //   40: astore 4
    //   42: new 94	android/text/TextUtils$SimpleStringSplitter
    //   45: astore 6
    //   47: aload 5
    //   49: astore 4
    //   51: aload 6
    //   53: bipush 32
    //   55: invokespecial 97	android/text/TextUtils$SimpleStringSplitter:<init>	(C)V
    //   58: iconst_0
    //   59: istore 7
    //   61: aload 5
    //   63: astore 4
    //   65: iload 7
    //   67: aload_0
    //   68: getfield 53	com/android/internal/os/KernelCpuSpeedReader:mNumSpeedSteps	I
    //   71: if_icmpge +66 -> 137
    //   74: aload 5
    //   76: astore 4
    //   78: aload_3
    //   79: invokevirtual 101	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   82: astore 8
    //   84: aload 8
    //   86: ifnull +51 -> 137
    //   89: aload 5
    //   91: astore 4
    //   93: aload 6
    //   95: aload 8
    //   97: invokevirtual 104	android/text/TextUtils$SimpleStringSplitter:setString	(Ljava/lang/String;)V
    //   100: aload 5
    //   102: astore 4
    //   104: aload 6
    //   106: invokevirtual 107	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   109: pop
    //   110: aload 5
    //   112: astore 4
    //   114: aload_2
    //   115: iload 7
    //   117: aload 6
    //   119: invokevirtual 107	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   122: invokestatic 113	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   125: aload_0
    //   126: getfield 72	com/android/internal/os/KernelCpuSpeedReader:mJiffyMillis	J
    //   129: lmul
    //   130: lastore
    //   131: iinc 7 1
    //   134: goto -73 -> 61
    //   137: aconst_null
    //   138: aload_3
    //   139: invokestatic 115	com/android/internal/os/KernelCpuSpeedReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   142: goto +78 -> 220
    //   145: astore 5
    //   147: goto +12 -> 159
    //   150: astore 5
    //   152: aload 5
    //   154: astore 4
    //   156: aload 5
    //   158: athrow
    //   159: aload 4
    //   161: aload_3
    //   162: invokestatic 115	com/android/internal/os/KernelCpuSpeedReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   165: aload 5
    //   167: athrow
    //   168: astore 4
    //   170: goto +56 -> 226
    //   173: astore 4
    //   175: new 117	java/lang/StringBuilder
    //   178: astore 5
    //   180: aload 5
    //   182: invokespecial 118	java/lang/StringBuilder:<init>	()V
    //   185: aload 5
    //   187: ldc 120
    //   189: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   192: pop
    //   193: aload 5
    //   195: aload 4
    //   197: invokevirtual 127	java/io/IOException:getMessage	()Ljava/lang/String;
    //   200: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: ldc 8
    //   206: aload 5
    //   208: invokevirtual 130	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   211: invokestatic 136	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   214: pop
    //   215: aload_2
    //   216: lconst_0
    //   217: invokestatic 142	java/util/Arrays:fill	([JJ)V
    //   220: aload_1
    //   221: invokestatic 146	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   224: aload_2
    //   225: areturn
    //   226: aload_1
    //   227: invokestatic 146	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   230: aload 4
    //   232: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	233	0	this	KernelCpuSpeedReader
    //   3	224	1	localThreadPolicy	android.os.StrictMode.ThreadPolicy
    //   10	215	2	arrayOfLong	long[]
    //   14	148	3	localBufferedReader	java.io.BufferedReader
    //   18	142	4	localObject1	Object
    //   168	1	4	localObject2	Object
    //   173	58	4	localIOException	java.io.IOException
    //   36	75	5	localObject3	Object
    //   145	1	5	localObject4	Object
    //   150	16	5	localThrowable	Throwable
    //   178	29	5	localStringBuilder	StringBuilder
    //   45	73	6	localSimpleStringSplitter	android.text.TextUtils.SimpleStringSplitter
    //   59	73	7	i	int
    //   82	14	8	str	String
    // Exception table:
    //   from	to	target	type
    //   42	47	145	finally
    //   51	58	145	finally
    //   65	74	145	finally
    //   78	84	145	finally
    //   93	100	145	finally
    //   104	110	145	finally
    //   114	131	145	finally
    //   156	159	145	finally
    //   42	47	150	java/lang/Throwable
    //   51	58	150	java/lang/Throwable
    //   65	74	150	java/lang/Throwable
    //   78	84	150	java/lang/Throwable
    //   93	100	150	java/lang/Throwable
    //   104	110	150	java/lang/Throwable
    //   114	131	150	java/lang/Throwable
    //   11	35	168	finally
    //   137	142	168	finally
    //   159	168	168	finally
    //   175	220	168	finally
    //   11	35	173	java/io/IOException
    //   137	142	173	java/io/IOException
    //   159	168	173	java/io/IOException
  }
  
  /* Error */
  public long[] readDelta()
  {
    // Byte code:
    //   0: invokestatic 82	android/os/StrictMode:allowThreadDiskReads	()Landroid/os/StrictMode$ThreadPolicy;
    //   3: astore_1
    //   4: new 84	java/io/BufferedReader
    //   7: astore_2
    //   8: new 86	java/io/FileReader
    //   11: astore_3
    //   12: aload_3
    //   13: aload_0
    //   14: getfield 51	com/android/internal/os/KernelCpuSpeedReader:mProcFile	Ljava/lang/String;
    //   17: invokespecial 89	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   20: aload_2
    //   21: aload_3
    //   22: invokespecial 92	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   25: aconst_null
    //   26: astore 4
    //   28: aload 4
    //   30: astore_3
    //   31: new 94	android/text/TextUtils$SimpleStringSplitter
    //   34: astore 5
    //   36: aload 4
    //   38: astore_3
    //   39: aload 5
    //   41: bipush 32
    //   43: invokespecial 97	android/text/TextUtils$SimpleStringSplitter:<init>	(C)V
    //   46: iconst_0
    //   47: istore 6
    //   49: aload 4
    //   51: astore_3
    //   52: iload 6
    //   54: aload_0
    //   55: getfield 55	com/android/internal/os/KernelCpuSpeedReader:mLastSpeedTimesMs	[J
    //   58: arraylength
    //   59: if_icmpge +123 -> 182
    //   62: aload 4
    //   64: astore_3
    //   65: aload_2
    //   66: invokevirtual 101	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   69: astore 7
    //   71: aload 7
    //   73: ifnull +109 -> 182
    //   76: aload 4
    //   78: astore_3
    //   79: aload 5
    //   81: aload 7
    //   83: invokevirtual 104	android/text/TextUtils$SimpleStringSplitter:setString	(Ljava/lang/String;)V
    //   86: aload 4
    //   88: astore_3
    //   89: aload 5
    //   91: invokevirtual 107	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   94: pop
    //   95: aload 4
    //   97: astore_3
    //   98: aload 5
    //   100: invokevirtual 107	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   103: invokestatic 113	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   106: aload_0
    //   107: getfield 72	com/android/internal/os/KernelCpuSpeedReader:mJiffyMillis	J
    //   110: lmul
    //   111: lstore 8
    //   113: aload 4
    //   115: astore_3
    //   116: lload 8
    //   118: aload_0
    //   119: getfield 55	com/android/internal/os/KernelCpuSpeedReader:mLastSpeedTimesMs	[J
    //   122: iload 6
    //   124: laload
    //   125: lcmp
    //   126: ifge +18 -> 144
    //   129: aload 4
    //   131: astore_3
    //   132: aload_0
    //   133: getfield 57	com/android/internal/os/KernelCpuSpeedReader:mDeltaSpeedTimesMs	[J
    //   136: iload 6
    //   138: lload 8
    //   140: lastore
    //   141: goto +23 -> 164
    //   144: aload 4
    //   146: astore_3
    //   147: aload_0
    //   148: getfield 57	com/android/internal/os/KernelCpuSpeedReader:mDeltaSpeedTimesMs	[J
    //   151: iload 6
    //   153: lload 8
    //   155: aload_0
    //   156: getfield 55	com/android/internal/os/KernelCpuSpeedReader:mLastSpeedTimesMs	[J
    //   159: iload 6
    //   161: laload
    //   162: lsub
    //   163: lastore
    //   164: aload 4
    //   166: astore_3
    //   167: aload_0
    //   168: getfield 55	com/android/internal/os/KernelCpuSpeedReader:mLastSpeedTimesMs	[J
    //   171: iload 6
    //   173: lload 8
    //   175: lastore
    //   176: iinc 6 1
    //   179: goto -130 -> 49
    //   182: aconst_null
    //   183: aload_2
    //   184: invokestatic 115	com/android/internal/os/KernelCpuSpeedReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   187: goto +73 -> 260
    //   190: astore 4
    //   192: goto +11 -> 203
    //   195: astore 4
    //   197: aload 4
    //   199: astore_3
    //   200: aload 4
    //   202: athrow
    //   203: aload_3
    //   204: aload_2
    //   205: invokestatic 115	com/android/internal/os/KernelCpuSpeedReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   208: aload 4
    //   210: athrow
    //   211: astore_3
    //   212: goto +57 -> 269
    //   215: astore 4
    //   217: new 117	java/lang/StringBuilder
    //   220: astore_3
    //   221: aload_3
    //   222: invokespecial 118	java/lang/StringBuilder:<init>	()V
    //   225: aload_3
    //   226: ldc 120
    //   228: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: pop
    //   232: aload_3
    //   233: aload 4
    //   235: invokevirtual 127	java/io/IOException:getMessage	()Ljava/lang/String;
    //   238: invokevirtual 124	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   241: pop
    //   242: ldc 8
    //   244: aload_3
    //   245: invokevirtual 130	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   248: invokestatic 136	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   251: pop
    //   252: aload_0
    //   253: getfield 57	com/android/internal/os/KernelCpuSpeedReader:mDeltaSpeedTimesMs	[J
    //   256: lconst_0
    //   257: invokestatic 142	java/util/Arrays:fill	([JJ)V
    //   260: aload_1
    //   261: invokestatic 146	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   264: aload_0
    //   265: getfield 57	com/android/internal/os/KernelCpuSpeedReader:mDeltaSpeedTimesMs	[J
    //   268: areturn
    //   269: aload_1
    //   270: invokestatic 146	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   273: aload_3
    //   274: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	275	0	this	KernelCpuSpeedReader
    //   3	267	1	localThreadPolicy	android.os.StrictMode.ThreadPolicy
    //   7	198	2	localBufferedReader	java.io.BufferedReader
    //   11	193	3	localObject1	Object
    //   211	1	3	localObject2	Object
    //   220	54	3	localStringBuilder	StringBuilder
    //   26	139	4	localObject3	Object
    //   190	1	4	localObject4	Object
    //   195	14	4	localThrowable	Throwable
    //   215	19	4	localIOException	java.io.IOException
    //   34	65	5	localSimpleStringSplitter	android.text.TextUtils.SimpleStringSplitter
    //   47	130	6	i	int
    //   69	13	7	str	String
    //   111	63	8	l	long
    // Exception table:
    //   from	to	target	type
    //   31	36	190	finally
    //   39	46	190	finally
    //   52	62	190	finally
    //   65	71	190	finally
    //   79	86	190	finally
    //   89	95	190	finally
    //   98	113	190	finally
    //   116	129	190	finally
    //   132	141	190	finally
    //   147	164	190	finally
    //   167	176	190	finally
    //   200	203	190	finally
    //   31	36	195	java/lang/Throwable
    //   39	46	195	java/lang/Throwable
    //   52	62	195	java/lang/Throwable
    //   65	71	195	java/lang/Throwable
    //   79	86	195	java/lang/Throwable
    //   89	95	195	java/lang/Throwable
    //   98	113	195	java/lang/Throwable
    //   116	129	195	java/lang/Throwable
    //   132	141	195	java/lang/Throwable
    //   147	164	195	java/lang/Throwable
    //   167	176	195	java/lang/Throwable
    //   4	25	211	finally
    //   182	187	211	finally
    //   203	211	211	finally
    //   217	260	211	finally
    //   4	25	215	java/io/IOException
    //   182	187	215	java/io/IOException
    //   203	211	215	java/io/IOException
  }
}
