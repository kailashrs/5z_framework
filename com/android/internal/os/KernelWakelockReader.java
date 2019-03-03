package com.android.internal.os;

public class KernelWakelockReader
{
  private static final int[] PROC_WAKELOCKS_FORMAT = { 5129, 8201, 9, 9, 9, 8201 };
  private static final String TAG = "KernelWakelockReader";
  private static final int[] WAKEUP_SOURCES_FORMAT = { 4105, 8457, 265, 265, 265, 265, 8457 };
  private static int sKernelWakelockUpdateVersion = 0;
  private static final String sWakelockFile = "/proc/wakelocks";
  private static final String sWakeupSourceFile = "/d/wakeup_sources";
  private final long[] mProcWakelocksData = new long[3];
  private final String[] mProcWakelocksName = new String[3];
  
  public KernelWakelockReader() {}
  
  /* Error */
  @com.android.internal.annotations.VisibleForTesting
  public KernelWakelockStats parseProcWakelocks(byte[] paramArrayOfByte, int paramInt, boolean paramBoolean, KernelWakelockStats paramKernelWakelockStats)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 5
    //   3: iload 5
    //   5: iload_2
    //   6: if_icmpge +25 -> 31
    //   9: aload_1
    //   10: iload 5
    //   12: baload
    //   13: bipush 10
    //   15: if_icmpeq +16 -> 31
    //   18: aload_1
    //   19: iload 5
    //   21: baload
    //   22: ifeq +9 -> 31
    //   25: iinc 5 1
    //   28: goto -25 -> 3
    //   31: iinc 5 1
    //   34: iload 5
    //   36: istore 6
    //   38: aload_0
    //   39: monitorenter
    //   40: getstatic 28	com/android/internal/os/KernelWakelockReader:sKernelWakelockUpdateVersion	I
    //   43: iconst_1
    //   44: iadd
    //   45: putstatic 28	com/android/internal/os/KernelWakelockReader:sKernelWakelockUpdateVersion	I
    //   48: iload 6
    //   50: iload_2
    //   51: if_icmpge +399 -> 450
    //   54: iload 5
    //   56: istore 6
    //   58: iload 6
    //   60: iload_2
    //   61: if_icmpge +33 -> 94
    //   64: aload_1
    //   65: iload 6
    //   67: baload
    //   68: bipush 10
    //   70: if_icmpeq +24 -> 94
    //   73: aload_1
    //   74: iload 6
    //   76: baload
    //   77: istore 7
    //   79: iload 7
    //   81: ifeq +13 -> 94
    //   84: iinc 6 1
    //   87: goto -29 -> 58
    //   90: astore_1
    //   91: goto +424 -> 515
    //   94: iload 6
    //   96: iload_2
    //   97: iconst_1
    //   98: isub
    //   99: if_icmple +6 -> 105
    //   102: goto +348 -> 450
    //   105: aload_0
    //   106: getfield 40	com/android/internal/os/KernelWakelockReader:mProcWakelocksName	[Ljava/lang/String;
    //   109: astore 8
    //   111: aload_0
    //   112: getfield 42	com/android/internal/os/KernelWakelockReader:mProcWakelocksData	[J
    //   115: astore 9
    //   117: iload 5
    //   119: istore 7
    //   121: iload 7
    //   123: iload 6
    //   125: if_icmpge +27 -> 152
    //   128: aload_1
    //   129: iload 7
    //   131: baload
    //   132: sipush 128
    //   135: iand
    //   136: ifeq +10 -> 146
    //   139: aload_1
    //   140: iload 7
    //   142: bipush 63
    //   144: i2b
    //   145: bastore
    //   146: iinc 7 1
    //   149: goto -28 -> 121
    //   152: iload_3
    //   153: ifeq +11 -> 164
    //   156: getstatic 32	com/android/internal/os/KernelWakelockReader:WAKEUP_SOURCES_FORMAT	[I
    //   159: astore 10
    //   161: goto +8 -> 169
    //   164: getstatic 30	com/android/internal/os/KernelWakelockReader:PROC_WAKELOCKS_FORMAT	[I
    //   167: astore 10
    //   169: aload_1
    //   170: iload 5
    //   172: iload 6
    //   174: aload 10
    //   176: aload 8
    //   178: aload 9
    //   180: aconst_null
    //   181: invokestatic 53	android/os/Process:parseProcLine	([BII[I[Ljava/lang/String;[J[F)Z
    //   184: istore 11
    //   186: aload 8
    //   188: iconst_0
    //   189: aaload
    //   190: astore 10
    //   192: aload 9
    //   194: iconst_1
    //   195: laload
    //   196: l2i
    //   197: istore 7
    //   199: iload_3
    //   200: ifeq +16 -> 216
    //   203: aload 9
    //   205: iconst_2
    //   206: laload
    //   207: ldc2_w 54
    //   210: lmul
    //   211: lstore 12
    //   213: goto +17 -> 230
    //   216: aload 9
    //   218: iconst_2
    //   219: laload
    //   220: ldc2_w 56
    //   223: ladd
    //   224: ldc2_w 54
    //   227: ldiv
    //   228: lstore 12
    //   230: iload 11
    //   232: ifeq +128 -> 360
    //   235: aload 10
    //   237: invokevirtual 61	java/lang/String:length	()I
    //   240: ifle +120 -> 360
    //   243: aload 4
    //   245: aload 10
    //   247: invokevirtual 67	com/android/internal/os/KernelWakelockStats:containsKey	(Ljava/lang/Object;)Z
    //   250: ifne +33 -> 283
    //   253: new 69	com/android/internal/os/KernelWakelockStats$Entry
    //   256: astore 9
    //   258: aload 9
    //   260: iload 7
    //   262: lload 12
    //   264: getstatic 28	com/android/internal/os/KernelWakelockReader:sKernelWakelockUpdateVersion	I
    //   267: invokespecial 72	com/android/internal/os/KernelWakelockStats$Entry:<init>	(IJI)V
    //   270: aload 4
    //   272: aload 10
    //   274: aload 9
    //   276: invokevirtual 76	com/android/internal/os/KernelWakelockStats:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   279: pop
    //   280: goto +153 -> 433
    //   283: aload 4
    //   285: aload 10
    //   287: invokevirtual 80	com/android/internal/os/KernelWakelockStats:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   290: checkcast 69	com/android/internal/os/KernelWakelockStats$Entry
    //   293: astore 10
    //   295: aload 10
    //   297: getfield 83	com/android/internal/os/KernelWakelockStats$Entry:mVersion	I
    //   300: getstatic 28	com/android/internal/os/KernelWakelockReader:sKernelWakelockUpdateVersion	I
    //   303: if_icmpne +32 -> 335
    //   306: aload 10
    //   308: aload 10
    //   310: getfield 86	com/android/internal/os/KernelWakelockStats$Entry:mCount	I
    //   313: iload 7
    //   315: iadd
    //   316: putfield 86	com/android/internal/os/KernelWakelockStats$Entry:mCount	I
    //   319: aload 10
    //   321: aload 10
    //   323: getfield 90	com/android/internal/os/KernelWakelockStats$Entry:mTotalTime	J
    //   326: lload 12
    //   328: ladd
    //   329: putfield 90	com/android/internal/os/KernelWakelockStats$Entry:mTotalTime	J
    //   332: goto +25 -> 357
    //   335: aload 10
    //   337: iload 7
    //   339: putfield 86	com/android/internal/os/KernelWakelockStats$Entry:mCount	I
    //   342: aload 10
    //   344: lload 12
    //   346: putfield 90	com/android/internal/os/KernelWakelockStats$Entry:mTotalTime	J
    //   349: aload 10
    //   351: getstatic 28	com/android/internal/os/KernelWakelockReader:sKernelWakelockUpdateVersion	I
    //   354: putfield 83	com/android/internal/os/KernelWakelockStats$Entry:mVersion	I
    //   357: goto +76 -> 433
    //   360: iload 11
    //   362: ifne +71 -> 433
    //   365: new 92	java/lang/StringBuilder
    //   368: astore 10
    //   370: aload 10
    //   372: invokespecial 93	java/lang/StringBuilder:<init>	()V
    //   375: aload 10
    //   377: ldc 95
    //   379: invokevirtual 99	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   382: pop
    //   383: new 38	java/lang/String
    //   386: astore 9
    //   388: aload 9
    //   390: aload_1
    //   391: iload 5
    //   393: iload 6
    //   395: iload 5
    //   397: isub
    //   398: invokespecial 102	java/lang/String:<init>	([BII)V
    //   401: aload 10
    //   403: aload 9
    //   405: invokevirtual 99	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   408: pop
    //   409: ldc 10
    //   411: aload 10
    //   413: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   416: invokestatic 112	android/util/Slog:wtf	(Ljava/lang/String;Ljava/lang/String;)I
    //   419: pop
    //   420: goto +13 -> 433
    //   423: astore 10
    //   425: ldc 10
    //   427: ldc 114
    //   429: invokestatic 112	android/util/Slog:wtf	(Ljava/lang/String;Ljava/lang/String;)I
    //   432: pop
    //   433: iload 6
    //   435: iconst_1
    //   436: iadd
    //   437: istore 5
    //   439: goto -391 -> 48
    //   442: astore_1
    //   443: goto +72 -> 515
    //   446: astore_1
    //   447: goto +68 -> 515
    //   450: aload 4
    //   452: invokevirtual 118	com/android/internal/os/KernelWakelockStats:values	()Ljava/util/Collection;
    //   455: invokeinterface 124 1 0
    //   460: astore_1
    //   461: aload_1
    //   462: invokeinterface 130 1 0
    //   467: ifeq +30 -> 497
    //   470: aload_1
    //   471: invokeinterface 134 1 0
    //   476: checkcast 69	com/android/internal/os/KernelWakelockStats$Entry
    //   479: getfield 83	com/android/internal/os/KernelWakelockStats$Entry:mVersion	I
    //   482: getstatic 28	com/android/internal/os/KernelWakelockReader:sKernelWakelockUpdateVersion	I
    //   485: if_icmpeq -24 -> 461
    //   488: aload_1
    //   489: invokeinterface 137 1 0
    //   494: goto -33 -> 461
    //   497: aload 4
    //   499: getstatic 28	com/android/internal/os/KernelWakelockReader:sKernelWakelockUpdateVersion	I
    //   502: putfield 140	com/android/internal/os/KernelWakelockStats:kernelWakelockVersion	I
    //   505: aload_0
    //   506: monitorexit
    //   507: aload 4
    //   509: areturn
    //   510: astore_1
    //   511: goto +4 -> 515
    //   514: astore_1
    //   515: aload_0
    //   516: monitorexit
    //   517: aload_1
    //   518: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	519	0	this	KernelWakelockReader
    //   0	519	1	paramArrayOfByte	byte[]
    //   0	519	2	paramInt	int
    //   0	519	3	paramBoolean	boolean
    //   0	519	4	paramKernelWakelockStats	KernelWakelockStats
    //   1	437	5	i	int
    //   36	401	6	j	int
    //   77	261	7	k	int
    //   109	78	8	arrayOfString	String[]
    //   115	289	9	localObject1	Object
    //   159	253	10	localObject2	Object
    //   423	1	10	localException	Exception
    //   184	177	11	bool	boolean
    //   211	134	12	l	long
    // Exception table:
    //   from	to	target	type
    //   156	161	90	finally
    //   365	420	423	java/lang/Exception
    //   169	186	442	finally
    //   216	230	442	finally
    //   235	280	442	finally
    //   283	332	442	finally
    //   335	357	442	finally
    //   365	420	442	finally
    //   425	433	442	finally
    //   105	117	446	finally
    //   164	169	446	finally
    //   450	461	510	finally
    //   461	494	510	finally
    //   497	507	510	finally
    //   40	48	514	finally
    //   515	517	514	finally
  }
  
  /* Error */
  public final KernelWakelockStats readKernelWakelockStats(KernelWakelockStats paramKernelWakelockStats)
  {
    // Byte code:
    //   0: ldc -108
    //   2: newarray byte
    //   4: astore_2
    //   5: invokestatic 154	android/os/SystemClock:uptimeMillis	()J
    //   8: lstore_3
    //   9: invokestatic 159	android/os/StrictMode:allowThreadDiskReadsMask	()I
    //   12: istore 5
    //   14: new 161	java/io/FileInputStream
    //   17: astore 6
    //   19: aload 6
    //   21: ldc 17
    //   23: invokespecial 164	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   26: iconst_0
    //   27: istore 7
    //   29: goto +27 -> 56
    //   32: astore_1
    //   33: goto +227 -> 260
    //   36: astore_1
    //   37: goto +207 -> 244
    //   40: astore 6
    //   42: new 161	java/io/FileInputStream
    //   45: dup
    //   46: ldc 20
    //   48: invokespecial 164	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   51: astore 6
    //   53: iconst_1
    //   54: istore 7
    //   56: aload 6
    //   58: aload_2
    //   59: invokevirtual 168	java/io/FileInputStream:read	([B)I
    //   62: istore 8
    //   64: aload 6
    //   66: invokevirtual 171	java/io/FileInputStream:close	()V
    //   69: iload 5
    //   71: invokestatic 175	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   74: invokestatic 154	android/os/SystemClock:uptimeMillis	()J
    //   77: lload_3
    //   78: lsub
    //   79: lstore_3
    //   80: lload_3
    //   81: ldc2_w 176
    //   84: lcmp
    //   85: ifle +46 -> 131
    //   88: new 92	java/lang/StringBuilder
    //   91: dup
    //   92: invokespecial 93	java/lang/StringBuilder:<init>	()V
    //   95: astore 6
    //   97: aload 6
    //   99: ldc -77
    //   101: invokevirtual 99	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: pop
    //   105: aload 6
    //   107: lload_3
    //   108: invokevirtual 182	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   111: pop
    //   112: aload 6
    //   114: ldc -72
    //   116: invokevirtual 99	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   119: pop
    //   120: ldc 10
    //   122: aload 6
    //   124: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   127: invokestatic 187	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   130: pop
    //   131: iload 8
    //   133: istore 9
    //   135: iload 8
    //   137: ifle +80 -> 217
    //   140: iload 8
    //   142: aload_2
    //   143: arraylength
    //   144: if_icmplt +39 -> 183
    //   147: new 92	java/lang/StringBuilder
    //   150: dup
    //   151: invokespecial 93	java/lang/StringBuilder:<init>	()V
    //   154: astore 6
    //   156: aload 6
    //   158: ldc -67
    //   160: invokevirtual 99	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: aload 6
    //   166: aload_2
    //   167: arraylength
    //   168: invokevirtual 192	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: ldc 10
    //   174: aload 6
    //   176: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   179: invokestatic 112	android/util/Slog:wtf	(Ljava/lang/String;Ljava/lang/String;)I
    //   182: pop
    //   183: iconst_0
    //   184: istore 5
    //   186: iload 8
    //   188: istore 9
    //   190: iload 5
    //   192: iload 8
    //   194: if_icmpge +23 -> 217
    //   197: aload_2
    //   198: iload 5
    //   200: baload
    //   201: ifne +10 -> 211
    //   204: iload 5
    //   206: istore 9
    //   208: goto +9 -> 217
    //   211: iinc 5 1
    //   214: goto -28 -> 186
    //   217: aload_0
    //   218: aload_2
    //   219: iload 9
    //   221: iload 7
    //   223: aload_1
    //   224: invokevirtual 194	com/android/internal/os/KernelWakelockReader:parseProcWakelocks	([BIZLcom/android/internal/os/KernelWakelockStats;)Lcom/android/internal/os/KernelWakelockStats;
    //   227: areturn
    //   228: astore_1
    //   229: ldc 10
    //   231: ldc -60
    //   233: invokestatic 112	android/util/Slog:wtf	(Ljava/lang/String;Ljava/lang/String;)I
    //   236: pop
    //   237: iload 5
    //   239: invokestatic 175	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   242: aconst_null
    //   243: areturn
    //   244: ldc 10
    //   246: ldc -58
    //   248: aload_1
    //   249: invokestatic 201	android/util/Slog:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   252: pop
    //   253: iload 5
    //   255: invokestatic 175	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   258: aconst_null
    //   259: areturn
    //   260: iload 5
    //   262: invokestatic 175	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   265: aload_1
    //   266: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	267	0	this	KernelWakelockReader
    //   0	267	1	paramKernelWakelockStats	KernelWakelockStats
    //   4	215	2	arrayOfByte	byte[]
    //   8	100	3	l	long
    //   12	249	5	i	int
    //   17	3	6	localFileInputStream	java.io.FileInputStream
    //   40	1	6	localFileNotFoundException	java.io.FileNotFoundException
    //   51	124	6	localObject	Object
    //   27	195	7	bool	boolean
    //   62	133	8	j	int
    //   133	87	9	k	int
    // Exception table:
    //   from	to	target	type
    //   14	26	32	finally
    //   42	53	32	finally
    //   56	69	32	finally
    //   229	237	32	finally
    //   244	253	32	finally
    //   14	26	36	java/io/IOException
    //   42	53	36	java/io/IOException
    //   56	69	36	java/io/IOException
    //   229	237	36	java/io/IOException
    //   14	26	40	java/io/FileNotFoundException
    //   42	53	228	java/io/FileNotFoundException
  }
}
