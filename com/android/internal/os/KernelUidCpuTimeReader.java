package com.android.internal.os;

import android.util.SparseLongArray;

public class KernelUidCpuTimeReader
  extends KernelUidCpuTimeReaderBase<Callback>
{
  private static final String TAG = KernelUidCpuTimeReader.class.getSimpleName();
  private static final String sProcFile = "/proc/uid_cputime/show_uid_stat";
  private static final String sRemoveUidProcFile = "/proc/uid_cputime/remove_uid_range";
  private SparseLongArray mLastSystemTimeUs = new SparseLongArray();
  private long mLastTimeReadUs = 0L;
  private SparseLongArray mLastUserTimeUs = new SparseLongArray();
  
  public KernelUidCpuTimeReader() {}
  
  /* Error */
  private void removeUidsFromKernelModule(int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: getstatic 45	com/android/internal/os/KernelUidCpuTimeReader:TAG	Ljava/lang/String;
    //   3: astore_3
    //   4: new 63	java/lang/StringBuilder
    //   7: dup
    //   8: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   11: astore 4
    //   13: aload 4
    //   15: ldc 66
    //   17: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   20: pop
    //   21: aload 4
    //   23: iload_1
    //   24: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload 4
    //   30: ldc 75
    //   32: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: pop
    //   36: aload 4
    //   38: iload_2
    //   39: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   42: pop
    //   43: aload_3
    //   44: aload 4
    //   46: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   49: invokestatic 84	android/util/Slog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   52: pop
    //   53: invokestatic 90	android/os/StrictMode:allowThreadDiskWritesMask	()I
    //   56: istore 5
    //   58: new 92	java/io/FileWriter
    //   61: astore 6
    //   63: aload 6
    //   65: ldc 16
    //   67: invokespecial 95	java/io/FileWriter:<init>	(Ljava/lang/String;)V
    //   70: aconst_null
    //   71: astore_3
    //   72: aload_3
    //   73: astore 4
    //   75: new 63	java/lang/StringBuilder
    //   78: astore 7
    //   80: aload_3
    //   81: astore 4
    //   83: aload 7
    //   85: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   88: aload_3
    //   89: astore 4
    //   91: aload 7
    //   93: iload_1
    //   94: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   97: pop
    //   98: aload_3
    //   99: astore 4
    //   101: aload 7
    //   103: ldc 75
    //   105: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: pop
    //   109: aload_3
    //   110: astore 4
    //   112: aload 7
    //   114: iload_2
    //   115: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   118: pop
    //   119: aload_3
    //   120: astore 4
    //   122: aload 6
    //   124: aload 7
    //   126: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   129: invokevirtual 98	java/io/FileWriter:write	(Ljava/lang/String;)V
    //   132: aload_3
    //   133: astore 4
    //   135: aload 6
    //   137: invokevirtual 101	java/io/FileWriter:flush	()V
    //   140: aconst_null
    //   141: aload 6
    //   143: invokestatic 103	com/android/internal/os/KernelUidCpuTimeReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   146: goto +93 -> 239
    //   149: astore_3
    //   150: goto +9 -> 159
    //   153: astore_3
    //   154: aload_3
    //   155: astore 4
    //   157: aload_3
    //   158: athrow
    //   159: aload 4
    //   161: aload 6
    //   163: invokestatic 103	com/android/internal/os/KernelUidCpuTimeReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   166: aload_3
    //   167: athrow
    //   168: astore 4
    //   170: goto +75 -> 245
    //   173: astore_3
    //   174: getstatic 45	com/android/internal/os/KernelUidCpuTimeReader:TAG	Ljava/lang/String;
    //   177: astore 4
    //   179: new 63	java/lang/StringBuilder
    //   182: astore 6
    //   184: aload 6
    //   186: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   189: aload 6
    //   191: ldc 105
    //   193: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   196: pop
    //   197: aload 6
    //   199: iload_1
    //   200: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   203: pop
    //   204: aload 6
    //   206: ldc 107
    //   208: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: pop
    //   212: aload 6
    //   214: iload_2
    //   215: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   218: pop
    //   219: aload 6
    //   221: ldc 109
    //   223: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: pop
    //   227: aload 4
    //   229: aload 6
    //   231: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   234: aload_3
    //   235: invokestatic 113	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   238: pop
    //   239: iload 5
    //   241: invokestatic 117	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   244: return
    //   245: iload 5
    //   247: invokestatic 117	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   250: aload 4
    //   252: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	253	0	this	KernelUidCpuTimeReader
    //   0	253	1	paramInt1	int
    //   0	253	2	paramInt2	int
    //   3	130	3	str1	String
    //   149	1	3	localObject1	Object
    //   153	14	3	localThrowable	Throwable
    //   173	62	3	localIOException	java.io.IOException
    //   11	149	4	localObject2	Object
    //   168	1	4	localObject3	Object
    //   177	74	4	str2	String
    //   56	190	5	i	int
    //   61	169	6	localObject4	Object
    //   78	47	7	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   75	80	149	finally
    //   83	88	149	finally
    //   91	98	149	finally
    //   101	109	149	finally
    //   112	119	149	finally
    //   122	132	149	finally
    //   135	140	149	finally
    //   157	159	149	finally
    //   75	80	153	java/lang/Throwable
    //   83	88	153	java/lang/Throwable
    //   91	98	153	java/lang/Throwable
    //   101	109	153	java/lang/Throwable
    //   112	119	153	java/lang/Throwable
    //   122	132	153	java/lang/Throwable
    //   135	140	153	java/lang/Throwable
    //   58	70	168	finally
    //   140	146	168	finally
    //   159	168	168	finally
    //   174	239	168	finally
    //   58	70	173	java/io/IOException
    //   140	146	173	java/io/IOException
    //   159	168	173	java/io/IOException
  }
  
  /* Error */
  public void readAbsolute(Callback paramCallback)
  {
    // Byte code:
    //   0: invokestatic 122	android/os/StrictMode:allowThreadDiskReadsMask	()I
    //   3: istore_2
    //   4: new 124	java/io/BufferedReader
    //   7: astore_3
    //   8: new 126	java/io/FileReader
    //   11: astore 4
    //   13: aload 4
    //   15: ldc 13
    //   17: invokespecial 127	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   20: aload_3
    //   21: aload 4
    //   23: invokespecial 130	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   26: aconst_null
    //   27: astore 5
    //   29: aload 5
    //   31: astore 4
    //   33: new 132	android/text/TextUtils$SimpleStringSplitter
    //   36: astore 6
    //   38: aload 5
    //   40: astore 4
    //   42: aload 6
    //   44: bipush 32
    //   46: invokespecial 135	android/text/TextUtils$SimpleStringSplitter:<init>	(C)V
    //   49: aload 5
    //   51: astore 4
    //   53: aload_3
    //   54: invokevirtual 138	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   57: astore 7
    //   59: aload 7
    //   61: ifnull +76 -> 137
    //   64: aload 5
    //   66: astore 4
    //   68: aload 6
    //   70: aload 7
    //   72: invokevirtual 141	android/text/TextUtils$SimpleStringSplitter:setString	(Ljava/lang/String;)V
    //   75: aload 5
    //   77: astore 4
    //   79: aload 6
    //   81: invokevirtual 144	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   84: astore 7
    //   86: aload 5
    //   88: astore 4
    //   90: aload_1
    //   91: aload 7
    //   93: iconst_0
    //   94: aload 7
    //   96: invokevirtual 149	java/lang/String:length	()I
    //   99: iconst_1
    //   100: isub
    //   101: invokevirtual 153	java/lang/String:substring	(II)Ljava/lang/String;
    //   104: bipush 10
    //   106: invokestatic 159	java/lang/Integer:parseInt	(Ljava/lang/String;I)I
    //   109: aload 6
    //   111: invokevirtual 144	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   114: bipush 10
    //   116: invokestatic 165	java/lang/Long:parseLong	(Ljava/lang/String;I)J
    //   119: aload 6
    //   121: invokevirtual 144	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   124: bipush 10
    //   126: invokestatic 165	java/lang/Long:parseLong	(Ljava/lang/String;I)J
    //   129: invokeinterface 169 6 0
    //   134: goto -85 -> 49
    //   137: aconst_null
    //   138: aload_3
    //   139: invokestatic 103	com/android/internal/os/KernelUidCpuTimeReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   142: goto +70 -> 212
    //   145: astore_1
    //   146: goto +9 -> 155
    //   149: astore_1
    //   150: aload_1
    //   151: astore 4
    //   153: aload_1
    //   154: athrow
    //   155: aload 4
    //   157: aload_3
    //   158: invokestatic 103	com/android/internal/os/KernelUidCpuTimeReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   161: aload_1
    //   162: athrow
    //   163: astore_1
    //   164: goto +53 -> 217
    //   167: astore 4
    //   169: getstatic 45	com/android/internal/os/KernelUidCpuTimeReader:TAG	Ljava/lang/String;
    //   172: astore_1
    //   173: new 63	java/lang/StringBuilder
    //   176: astore 5
    //   178: aload 5
    //   180: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   183: aload 5
    //   185: ldc -85
    //   187: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   190: pop
    //   191: aload 5
    //   193: aload 4
    //   195: invokevirtual 174	java/io/IOException:getMessage	()Ljava/lang/String;
    //   198: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: pop
    //   202: aload_1
    //   203: aload 5
    //   205: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   208: invokestatic 176	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   211: pop
    //   212: iload_2
    //   213: invokestatic 117	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   216: return
    //   217: iload_2
    //   218: invokestatic 117	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   221: aload_1
    //   222: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	223	0	this	KernelUidCpuTimeReader
    //   0	223	1	paramCallback	Callback
    //   3	215	2	i	int
    //   7	151	3	localBufferedReader	java.io.BufferedReader
    //   11	145	4	localObject	Object
    //   167	27	4	localIOException	java.io.IOException
    //   27	177	5	localStringBuilder	StringBuilder
    //   36	84	6	localSimpleStringSplitter	android.text.TextUtils.SimpleStringSplitter
    //   57	38	7	str	String
    // Exception table:
    //   from	to	target	type
    //   33	38	145	finally
    //   42	49	145	finally
    //   53	59	145	finally
    //   68	75	145	finally
    //   79	86	145	finally
    //   90	134	145	finally
    //   153	155	145	finally
    //   33	38	149	java/lang/Throwable
    //   42	49	149	java/lang/Throwable
    //   53	59	149	java/lang/Throwable
    //   68	75	149	java/lang/Throwable
    //   79	86	149	java/lang/Throwable
    //   90	134	149	java/lang/Throwable
    //   4	26	163	finally
    //   137	142	163	finally
    //   155	163	163	finally
    //   169	212	163	finally
    //   4	26	167	java/io/IOException
    //   137	142	167	java/io/IOException
    //   155	163	167	java/io/IOException
  }
  
  /* Error */
  protected void readDeltaImpl(Callback paramCallback)
  {
    // Byte code:
    //   0: invokestatic 122	android/os/StrictMode:allowThreadDiskReadsMask	()I
    //   3: istore_2
    //   4: invokestatic 183	android/os/SystemClock:elapsedRealtime	()J
    //   7: lstore_3
    //   8: ldc2_w 184
    //   11: lstore 5
    //   13: lload_3
    //   14: ldc2_w 184
    //   17: lmul
    //   18: lstore_3
    //   19: new 124	java/io/BufferedReader
    //   22: astore 7
    //   24: new 126	java/io/FileReader
    //   27: astore 8
    //   29: aload 8
    //   31: ldc 13
    //   33: invokespecial 127	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   36: aload 7
    //   38: aload 8
    //   40: invokespecial 130	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   43: lload_3
    //   44: lstore 9
    //   46: lload_3
    //   47: lstore 11
    //   49: new 132	android/text/TextUtils$SimpleStringSplitter
    //   52: astore 8
    //   54: lload_3
    //   55: lstore 9
    //   57: lload_3
    //   58: lstore 11
    //   60: aload 8
    //   62: bipush 32
    //   64: invokespecial 135	android/text/TextUtils$SimpleStringSplitter:<init>	(C)V
    //   67: lload_3
    //   68: lstore 9
    //   70: lload_3
    //   71: lstore 11
    //   73: aload 7
    //   75: invokevirtual 138	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   78: astore 13
    //   80: aload 13
    //   82: ifnull +578 -> 660
    //   85: lload_3
    //   86: lstore 9
    //   88: lload_3
    //   89: lstore 11
    //   91: aload 8
    //   93: aload 13
    //   95: invokevirtual 141	android/text/TextUtils$SimpleStringSplitter:setString	(Ljava/lang/String;)V
    //   98: lload_3
    //   99: lstore 9
    //   101: lload_3
    //   102: lstore 11
    //   104: aload 8
    //   106: invokevirtual 144	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   109: astore 13
    //   111: lload_3
    //   112: lstore 9
    //   114: lload_3
    //   115: lstore 11
    //   117: aload 13
    //   119: iconst_0
    //   120: aload 13
    //   122: invokevirtual 149	java/lang/String:length	()I
    //   125: iconst_1
    //   126: isub
    //   127: invokevirtual 153	java/lang/String:substring	(II)Ljava/lang/String;
    //   130: bipush 10
    //   132: invokestatic 159	java/lang/Integer:parseInt	(Ljava/lang/String;I)I
    //   135: istore 14
    //   137: lload_3
    //   138: lstore 9
    //   140: lload_3
    //   141: lstore 11
    //   143: aload 8
    //   145: invokevirtual 144	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   148: bipush 10
    //   150: invokestatic 165	java/lang/Long:parseLong	(Ljava/lang/String;I)J
    //   153: lstore 15
    //   155: lload_3
    //   156: lstore 9
    //   158: lload_3
    //   159: lstore 11
    //   161: aload 8
    //   163: invokevirtual 144	android/text/TextUtils$SimpleStringSplitter:next	()Ljava/lang/String;
    //   166: bipush 10
    //   168: invokestatic 165	java/lang/Long:parseLong	(Ljava/lang/String;I)J
    //   171: lstore 17
    //   173: iconst_0
    //   174: istore 19
    //   176: lload 15
    //   178: lstore 20
    //   180: lload 17
    //   182: lstore 22
    //   184: aload_1
    //   185: ifnull +410 -> 595
    //   188: lload_3
    //   189: lstore 9
    //   191: lload_3
    //   192: lstore 11
    //   194: aload_0
    //   195: getfield 57	com/android/internal/os/KernelUidCpuTimeReader:mLastTimeReadUs	J
    //   198: lconst_0
    //   199: lcmp
    //   200: ifeq +395 -> 595
    //   203: lload_3
    //   204: lstore 9
    //   206: lload_3
    //   207: lstore 11
    //   209: aload_0
    //   210: getfield 53	com/android/internal/os/KernelUidCpuTimeReader:mLastUserTimeUs	Landroid/util/SparseLongArray;
    //   213: iload 14
    //   215: invokevirtual 189	android/util/SparseLongArray:indexOfKey	(I)I
    //   218: istore 19
    //   220: iload 19
    //   222: iflt +328 -> 550
    //   225: lload_3
    //   226: lstore 9
    //   228: lload_3
    //   229: lstore 11
    //   231: lload 20
    //   233: aload_0
    //   234: getfield 53	com/android/internal/os/KernelUidCpuTimeReader:mLastUserTimeUs	Landroid/util/SparseLongArray;
    //   237: iload 19
    //   239: invokevirtual 193	android/util/SparseLongArray:valueAt	(I)J
    //   242: lsub
    //   243: lstore 24
    //   245: lload_3
    //   246: lstore 9
    //   248: lload_3
    //   249: lstore 11
    //   251: lload 22
    //   253: aload_0
    //   254: getfield 55	com/android/internal/os/KernelUidCpuTimeReader:mLastSystemTimeUs	Landroid/util/SparseLongArray;
    //   257: iload 19
    //   259: invokevirtual 193	android/util/SparseLongArray:valueAt	(I)J
    //   262: lsub
    //   263: lstore 20
    //   265: lload_3
    //   266: lstore 9
    //   268: lload_3
    //   269: lstore 11
    //   271: aload_0
    //   272: getfield 57	com/android/internal/os/KernelUidCpuTimeReader:mLastTimeReadUs	J
    //   275: lstore 22
    //   277: lload 24
    //   279: lconst_0
    //   280: lcmp
    //   281: iflt +28 -> 309
    //   284: lload 20
    //   286: lconst_0
    //   287: lcmp
    //   288: ifge +6 -> 294
    //   291: goto +18 -> 309
    //   294: lload 5
    //   296: lstore 9
    //   298: lload 24
    //   300: lstore 5
    //   302: lload 20
    //   304: lstore 11
    //   306: goto +256 -> 562
    //   309: lload_3
    //   310: lstore 9
    //   312: lload_3
    //   313: lstore 11
    //   315: new 63	java/lang/StringBuilder
    //   318: astore 13
    //   320: lload_3
    //   321: lstore 9
    //   323: lload_3
    //   324: lstore 11
    //   326: aload 13
    //   328: ldc -61
    //   330: invokespecial 196	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
    //   333: lload_3
    //   334: lstore 9
    //   336: lload_3
    //   337: lstore 11
    //   339: aload 13
    //   341: iload 14
    //   343: invokevirtual 73	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   346: pop
    //   347: lload_3
    //   348: lstore 9
    //   350: lload_3
    //   351: lstore 11
    //   353: aload 13
    //   355: ldc -58
    //   357: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   360: pop
    //   361: lload_3
    //   362: lstore 9
    //   364: lload_3
    //   365: lstore 11
    //   367: aload 13
    //   369: ldc -56
    //   371: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   374: pop
    //   375: lload_3
    //   376: lload 22
    //   378: lsub
    //   379: ldc2_w 184
    //   382: ldiv
    //   383: aload 13
    //   385: invokestatic 206	android/util/TimeUtils:formatDuration	(JLjava/lang/StringBuilder;)V
    //   388: aload 13
    //   390: ldc -48
    //   392: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   395: pop
    //   396: aload 13
    //   398: ldc -46
    //   400: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   403: pop
    //   404: aload_0
    //   405: getfield 53	com/android/internal/os/KernelUidCpuTimeReader:mLastUserTimeUs	Landroid/util/SparseLongArray;
    //   408: iload 19
    //   410: invokevirtual 193	android/util/SparseLongArray:valueAt	(I)J
    //   413: ldc2_w 184
    //   416: ldiv
    //   417: aload 13
    //   419: invokestatic 206	android/util/TimeUtils:formatDuration	(JLjava/lang/StringBuilder;)V
    //   422: aload 13
    //   424: ldc -44
    //   426: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   429: pop
    //   430: aload_0
    //   431: getfield 55	com/android/internal/os/KernelUidCpuTimeReader:mLastSystemTimeUs	Landroid/util/SparseLongArray;
    //   434: iload 19
    //   436: invokevirtual 193	android/util/SparseLongArray:valueAt	(I)J
    //   439: ldc2_w 184
    //   442: ldiv
    //   443: aload 13
    //   445: invokestatic 206	android/util/TimeUtils:formatDuration	(JLjava/lang/StringBuilder;)V
    //   448: aload 13
    //   450: ldc -42
    //   452: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   455: pop
    //   456: lload 15
    //   458: ldc2_w 184
    //   461: ldiv
    //   462: aload 13
    //   464: invokestatic 206	android/util/TimeUtils:formatDuration	(JLjava/lang/StringBuilder;)V
    //   467: aload 13
    //   469: ldc -44
    //   471: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   474: pop
    //   475: lload 17
    //   477: ldc2_w 184
    //   480: ldiv
    //   481: aload 13
    //   483: invokestatic 206	android/util/TimeUtils:formatDuration	(JLjava/lang/StringBuilder;)V
    //   486: aload 13
    //   488: ldc -40
    //   490: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   493: pop
    //   494: lload 24
    //   496: ldc2_w 184
    //   499: ldiv
    //   500: aload 13
    //   502: invokestatic 206	android/util/TimeUtils:formatDuration	(JLjava/lang/StringBuilder;)V
    //   505: aload 13
    //   507: ldc -44
    //   509: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   512: pop
    //   513: ldc2_w 184
    //   516: lstore 9
    //   518: lload 20
    //   520: ldc2_w 184
    //   523: ldiv
    //   524: aload 13
    //   526: invokestatic 206	android/util/TimeUtils:formatDuration	(JLjava/lang/StringBuilder;)V
    //   529: getstatic 45	com/android/internal/os/KernelUidCpuTimeReader:TAG	Ljava/lang/String;
    //   532: aload 13
    //   534: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   537: invokestatic 176	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   540: pop
    //   541: lconst_0
    //   542: lstore 5
    //   544: lconst_0
    //   545: lstore 11
    //   547: goto +15 -> 562
    //   550: lload 5
    //   552: lstore 9
    //   554: lload 22
    //   556: lstore 11
    //   558: lload 20
    //   560: lstore 5
    //   562: lload 5
    //   564: lconst_0
    //   565: lcmp
    //   566: ifne +19 -> 585
    //   569: lload 11
    //   571: lconst_0
    //   572: lcmp
    //   573: ifeq +6 -> 579
    //   576: goto +9 -> 585
    //   579: iconst_0
    //   580: istore 19
    //   582: goto +6 -> 588
    //   585: iconst_1
    //   586: istore 19
    //   588: lload 5
    //   590: lstore 20
    //   592: goto +11 -> 603
    //   595: lload 22
    //   597: lstore 11
    //   599: lload 5
    //   601: lstore 9
    //   603: aload_0
    //   604: getfield 53	com/android/internal/os/KernelUidCpuTimeReader:mLastUserTimeUs	Landroid/util/SparseLongArray;
    //   607: iload 14
    //   609: lload 15
    //   611: invokevirtual 220	android/util/SparseLongArray:put	(IJ)V
    //   614: aload_0
    //   615: getfield 55	com/android/internal/os/KernelUidCpuTimeReader:mLastSystemTimeUs	Landroid/util/SparseLongArray;
    //   618: iload 14
    //   620: lload 17
    //   622: invokevirtual 220	android/util/SparseLongArray:put	(IJ)V
    //   625: iload 19
    //   627: ifeq +15 -> 642
    //   630: aload_1
    //   631: iload 14
    //   633: lload 20
    //   635: lload 11
    //   637: invokeinterface 169 6 0
    //   642: lload 9
    //   644: lstore 5
    //   646: goto -579 -> 67
    //   649: astore_1
    //   650: aconst_null
    //   651: astore 8
    //   653: goto +46 -> 699
    //   656: astore_1
    //   657: goto +32 -> 689
    //   660: lload_3
    //   661: lstore 5
    //   663: lload_3
    //   664: lstore 5
    //   666: aconst_null
    //   667: aload 7
    //   669: invokestatic 103	com/android/internal/os/KernelUidCpuTimeReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   672: goto +111 -> 783
    //   675: astore_1
    //   676: lload 9
    //   678: lstore_3
    //   679: aconst_null
    //   680: astore 8
    //   682: goto +17 -> 699
    //   685: astore_1
    //   686: lload 11
    //   688: lstore_3
    //   689: aload_1
    //   690: athrow
    //   691: astore 13
    //   693: aload_1
    //   694: astore 8
    //   696: aload 13
    //   698: astore_1
    //   699: lload_3
    //   700: lstore 5
    //   702: lload_3
    //   703: lstore 5
    //   705: aload 8
    //   707: aload 7
    //   709: invokestatic 103	com/android/internal/os/KernelUidCpuTimeReader:$closeResource	(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V
    //   712: lload_3
    //   713: lstore 5
    //   715: lload_3
    //   716: lstore 5
    //   718: aload_1
    //   719: athrow
    //   720: astore_1
    //   721: goto +73 -> 794
    //   724: astore_1
    //   725: goto +11 -> 736
    //   728: astore_1
    //   729: goto +65 -> 794
    //   732: astore_1
    //   733: lload_3
    //   734: lstore 5
    //   736: getstatic 45	com/android/internal/os/KernelUidCpuTimeReader:TAG	Ljava/lang/String;
    //   739: astore 13
    //   741: new 63	java/lang/StringBuilder
    //   744: astore 8
    //   746: aload 8
    //   748: invokespecial 64	java/lang/StringBuilder:<init>	()V
    //   751: aload 8
    //   753: ldc -85
    //   755: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   758: pop
    //   759: aload 8
    //   761: aload_1
    //   762: invokevirtual 174	java/io/IOException:getMessage	()Ljava/lang/String;
    //   765: invokevirtual 70	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   768: pop
    //   769: aload 13
    //   771: aload 8
    //   773: invokevirtual 78	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   776: invokestatic 176	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   779: pop
    //   780: lload 5
    //   782: lstore_3
    //   783: iload_2
    //   784: invokestatic 117	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   787: aload_0
    //   788: lload_3
    //   789: putfield 57	com/android/internal/os/KernelUidCpuTimeReader:mLastTimeReadUs	J
    //   792: return
    //   793: astore_1
    //   794: iload_2
    //   795: invokestatic 117	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   798: aload_1
    //   799: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	800	0	this	KernelUidCpuTimeReader
    //   0	800	1	paramCallback	Callback
    //   3	792	2	i	int
    //   7	782	3	l1	long
    //   11	770	5	l2	long
    //   22	686	7	localBufferedReader	java.io.BufferedReader
    //   27	745	8	localObject1	Object
    //   44	633	9	l3	long
    //   47	640	11	l4	long
    //   78	455	13	localObject2	Object
    //   691	6	13	localObject3	Object
    //   739	31	13	str	String
    //   135	497	14	j	int
    //   153	457	15	l5	long
    //   171	450	17	l6	long
    //   174	452	19	k	int
    //   178	456	20	l7	long
    //   182	414	22	l8	long
    //   243	252	24	l9	long
    // Exception table:
    //   from	to	target	type
    //   375	513	649	finally
    //   518	541	649	finally
    //   603	625	649	finally
    //   630	642	649	finally
    //   375	513	656	java/lang/Throwable
    //   518	541	656	java/lang/Throwable
    //   603	625	656	java/lang/Throwable
    //   630	642	656	java/lang/Throwable
    //   49	54	675	finally
    //   60	67	675	finally
    //   73	80	675	finally
    //   91	98	675	finally
    //   104	111	675	finally
    //   117	137	675	finally
    //   143	155	675	finally
    //   161	173	675	finally
    //   194	203	675	finally
    //   209	220	675	finally
    //   231	245	675	finally
    //   251	265	675	finally
    //   271	277	675	finally
    //   315	320	675	finally
    //   326	333	675	finally
    //   339	347	675	finally
    //   353	361	675	finally
    //   367	375	675	finally
    //   49	54	685	java/lang/Throwable
    //   60	67	685	java/lang/Throwable
    //   73	80	685	java/lang/Throwable
    //   91	98	685	java/lang/Throwable
    //   104	111	685	java/lang/Throwable
    //   117	137	685	java/lang/Throwable
    //   143	155	685	java/lang/Throwable
    //   161	173	685	java/lang/Throwable
    //   194	203	685	java/lang/Throwable
    //   209	220	685	java/lang/Throwable
    //   231	245	685	java/lang/Throwable
    //   251	265	685	java/lang/Throwable
    //   271	277	685	java/lang/Throwable
    //   315	320	685	java/lang/Throwable
    //   326	333	685	java/lang/Throwable
    //   339	347	685	java/lang/Throwable
    //   353	361	685	java/lang/Throwable
    //   367	375	685	java/lang/Throwable
    //   689	691	691	finally
    //   666	672	720	finally
    //   705	712	720	finally
    //   718	720	720	finally
    //   666	672	724	java/io/IOException
    //   705	712	724	java/io/IOException
    //   718	720	724	java/io/IOException
    //   19	43	728	finally
    //   19	43	732	java/io/IOException
    //   736	780	793	finally
  }
  
  public void removeUid(int paramInt)
  {
    int i = mLastSystemTimeUs.indexOfKey(paramInt);
    if (i >= 0)
    {
      mLastSystemTimeUs.removeAt(i);
      mLastUserTimeUs.removeAt(i);
    }
    removeUidsFromKernelModule(paramInt, paramInt);
  }
  
  public void removeUidsInRange(int paramInt1, int paramInt2)
  {
    if (paramInt2 < paramInt1) {
      return;
    }
    mLastSystemTimeUs.put(paramInt1, 0L);
    mLastUserTimeUs.put(paramInt1, 0L);
    mLastSystemTimeUs.put(paramInt2, 0L);
    mLastUserTimeUs.put(paramInt2, 0L);
    int i = mLastSystemTimeUs.indexOfKey(paramInt1);
    int j = mLastSystemTimeUs.indexOfKey(paramInt2);
    mLastSystemTimeUs.removeAtRange(i, j - i + 1);
    mLastUserTimeUs.removeAtRange(i, j - i + 1);
    removeUidsFromKernelModule(paramInt1, paramInt2);
  }
  
  public static abstract interface Callback
    extends KernelUidCpuTimeReaderBase.Callback
  {
    public abstract void onUidCpuTime(int paramInt, long paramLong1, long paramLong2);
  }
}
