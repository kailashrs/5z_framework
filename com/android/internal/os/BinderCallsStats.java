package com.android.internal.os;

import android.os.Binder;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BinderCallsStats
{
  private static final int CALL_SESSIONS_POOL_SIZE = 100;
  private static final BinderCallsStats sInstance = new BinderCallsStats();
  private final Queue<CallSession> mCallSessionsPool = new ConcurrentLinkedQueue();
  private volatile boolean mDetailedTracking = false;
  private final Object mLock = new Object();
  private long mStartTime = System.currentTimeMillis();
  @GuardedBy("mLock")
  private final SparseArray<UidEntry> mUidEntries = new SparseArray();
  
  private BinderCallsStats() {}
  
  @VisibleForTesting
  public BinderCallsStats(boolean paramBoolean)
  {
    mDetailedTracking = paramBoolean;
  }
  
  private CallSession callStarted(String paramString, int paramInt)
  {
    CallSession localCallSession1 = (CallSession)mCallSessionsPool.poll();
    CallSession localCallSession2 = localCallSession1;
    if (localCallSession1 == null) {
      localCallSession2 = new CallSession();
    }
    mCallStat.className = paramString;
    mCallStat.msg = paramInt;
    mStarted = getThreadTimeMicro();
    return localCallSession2;
  }
  
  public static BinderCallsStats getInstance()
  {
    return sInstance;
  }
  
  private long getThreadTimeMicro()
  {
    long l;
    if (mDetailedTracking) {
      l = SystemClock.currentThreadTimeMicro();
    } else {
      l = 0L;
    }
    return l;
  }
  
  public void callEnded(CallSession paramCallSession)
  {
    Preconditions.checkNotNull(paramCallSession);
    long l;
    if (mDetailedTracking) {
      l = getThreadTimeMicro() - mStarted;
    } else {
      l = 1L;
    }
    mCallingUId = Binder.getCallingUid();
    synchronized (mLock)
    {
      Object localObject2 = (UidEntry)mUidEntries.get(mCallingUId);
      Object localObject3 = localObject2;
      if (localObject2 == null)
      {
        localObject3 = new com/android/internal/os/BinderCallsStats$UidEntry;
        ((UidEntry)localObject3).<init>(mCallingUId);
        mUidEntries.put(mCallingUId, localObject3);
      }
      if (mDetailedTracking)
      {
        CallStat localCallStat = (CallStat)mCallStats.get(mCallStat);
        localObject2 = localCallStat;
        if (localCallStat == null)
        {
          localObject2 = new com/android/internal/os/BinderCallsStats$CallStat;
          ((CallStat)localObject2).<init>(mCallStat.className, mCallStat.msg);
          mCallStats.put(localObject2, localObject2);
        }
        callCount += 1L;
        time += l;
      }
      time += l;
      callCount += 1L;
      if (mCallSessionsPool.size() < 100) {
        mCallSessionsPool.add(paramCallSession);
      }
      return;
    }
  }
  
  public CallSession callStarted(Binder paramBinder, int paramInt)
  {
    return callStarted(paramBinder.getClass().getName(), paramInt);
  }
  
  /* Error */
  public void dump(java.io.PrintWriter paramPrintWriter)
  {
    // Byte code:
    //   0: aload_0
    //   1: astore_2
    //   2: new 191	java/util/HashMap
    //   5: dup
    //   6: invokespecial 192	java/util/HashMap:<init>	()V
    //   9: astore_3
    //   10: new 191	java/util/HashMap
    //   13: dup
    //   14: invokespecial 192	java/util/HashMap:<init>	()V
    //   17: astore 4
    //   19: lconst_0
    //   20: lstore 5
    //   22: aload_1
    //   23: ldc -62
    //   25: invokevirtual 200	java/io/PrintWriter:print	(Ljava/lang/String;)V
    //   28: aload_1
    //   29: ldc -54
    //   31: aload_2
    //   32: getfield 63	com/android/internal/os/BinderCallsStats:mStartTime	J
    //   35: invokestatic 208	android/text/format/DateFormat:format	(Ljava/lang/CharSequence;J)Ljava/lang/CharSequence;
    //   38: invokevirtual 212	java/io/PrintWriter:println	(Ljava/lang/Object;)V
    //   41: aload_2
    //   42: getfield 48	com/android/internal/os/BinderCallsStats:mUidEntries	Landroid/util/SparseArray;
    //   45: invokevirtual 213	android/util/SparseArray:size	()I
    //   48: istore 7
    //   50: new 215	java/util/ArrayList
    //   53: dup
    //   54: invokespecial 216	java/util/ArrayList:<init>	()V
    //   57: astore 8
    //   59: aload_2
    //   60: getfield 55	com/android/internal/os/BinderCallsStats:mLock	Ljava/lang/Object;
    //   63: astore_2
    //   64: aload_2
    //   65: monitorenter
    //   66: lconst_0
    //   67: lstore 9
    //   69: iconst_0
    //   70: istore 11
    //   72: iload 11
    //   74: iload 7
    //   76: if_icmpge +225 -> 301
    //   79: aload_0
    //   80: getfield 48	com/android/internal/os/BinderCallsStats:mUidEntries	Landroid/util/SparseArray;
    //   83: iload 11
    //   85: invokevirtual 219	android/util/SparseArray:valueAt	(I)Ljava/lang/Object;
    //   88: checkcast 12	com/android/internal/os/BinderCallsStats$UidEntry
    //   91: astore 12
    //   93: aload 8
    //   95: aload 12
    //   97: invokeinterface 222 2 0
    //   102: pop
    //   103: aload 12
    //   105: getfield 104	com/android/internal/os/BinderCallsStats$UidEntry:time	J
    //   108: lstore 13
    //   110: lload 5
    //   112: lload 13
    //   114: ladd
    //   115: lstore 13
    //   117: aload_3
    //   118: aload 12
    //   120: getfield 225	com/android/internal/os/BinderCallsStats$UidEntry:uid	I
    //   123: invokestatic 231	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   126: invokeinterface 157 2 0
    //   131: checkcast 116	java/lang/Long
    //   134: astore 15
    //   136: aload 12
    //   138: getfield 225	com/android/internal/os/BinderCallsStats$UidEntry:uid	I
    //   141: istore 16
    //   143: aload 15
    //   145: ifnonnull +13 -> 158
    //   148: aload 12
    //   150: getfield 104	com/android/internal/os/BinderCallsStats$UidEntry:time	J
    //   153: lstore 5
    //   155: goto +20 -> 175
    //   158: aload 15
    //   160: invokevirtual 234	java/lang/Long:longValue	()J
    //   163: lstore 5
    //   165: lload 5
    //   167: aload 12
    //   169: getfield 104	com/android/internal/os/BinderCallsStats$UidEntry:time	J
    //   172: ladd
    //   173: lstore 5
    //   175: aload_3
    //   176: iload 16
    //   178: invokestatic 231	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   181: lload 5
    //   183: invokestatic 237	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   186: invokeinterface 163 3 0
    //   191: pop
    //   192: aload 4
    //   194: aload 12
    //   196: getfield 225	com/android/internal/os/BinderCallsStats$UidEntry:uid	I
    //   199: invokestatic 231	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   202: invokeinterface 157 2 0
    //   207: checkcast 116	java/lang/Long
    //   210: astore 15
    //   212: aload 12
    //   214: getfield 225	com/android/internal/os/BinderCallsStats$UidEntry:uid	I
    //   217: istore 16
    //   219: aload 15
    //   221: ifnonnull +13 -> 234
    //   224: aload 12
    //   226: getfield 167	com/android/internal/os/BinderCallsStats$UidEntry:callCount	J
    //   229: lstore 5
    //   231: goto +16 -> 247
    //   234: aload 15
    //   236: invokevirtual 234	java/lang/Long:longValue	()J
    //   239: aload 12
    //   241: getfield 167	com/android/internal/os/BinderCallsStats$UidEntry:callCount	J
    //   244: ladd
    //   245: lstore 5
    //   247: aload 4
    //   249: iload 16
    //   251: invokestatic 231	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   254: lload 5
    //   256: invokestatic 237	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   259: invokeinterface 163 3 0
    //   264: pop
    //   265: aload 12
    //   267: getfield 167	com/android/internal/os/BinderCallsStats$UidEntry:callCount	J
    //   270: lstore 5
    //   272: lload 9
    //   274: lload 5
    //   276: ladd
    //   277: lstore 9
    //   279: iinc 11 1
    //   282: lload 13
    //   284: lstore 5
    //   286: goto -214 -> 72
    //   289: astore_1
    //   290: goto +567 -> 857
    //   293: astore_1
    //   294: goto +563 -> 857
    //   297: astore_1
    //   298: goto +559 -> 857
    //   301: aload_2
    //   302: monitorexit
    //   303: aload_0
    //   304: getfield 43	com/android/internal/os/BinderCallsStats:mDetailedTracking	Z
    //   307: ifeq +396 -> 703
    //   310: aload_1
    //   311: ldc -17
    //   313: invokevirtual 241	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   316: aload 8
    //   318: getstatic 247	com/android/internal/os/_$$Lambda$BinderCallsStats$JdIS98lVGLAIfkEkC976rVyBc_U:INSTANCE	Lcom/android/internal/os/-$$Lambda$BinderCallsStats$JdIS98lVGLAIfkEkC976rVyBc_U;
    //   321: invokeinterface 251 2 0
    //   326: new 253	java/lang/StringBuilder
    //   329: dup
    //   330: invokespecial 254	java/lang/StringBuilder:<init>	()V
    //   333: astore 12
    //   335: aload 8
    //   337: invokeinterface 258 1 0
    //   342: astore 15
    //   344: aload 15
    //   346: invokeinterface 264 1 0
    //   351: ifeq +146 -> 497
    //   354: aload 15
    //   356: invokeinterface 267 1 0
    //   361: checkcast 12	com/android/internal/os/BinderCallsStats$UidEntry
    //   364: astore 17
    //   366: new 215	java/util/ArrayList
    //   369: dup
    //   370: aload 17
    //   372: getfield 153	com/android/internal/os/BinderCallsStats$UidEntry:mCallStats	Ljava/util/Map;
    //   375: invokeinterface 271 1 0
    //   380: invokespecial 274	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
    //   383: astore 8
    //   385: aload 8
    //   387: getstatic 279	com/android/internal/os/_$$Lambda$BinderCallsStats$8JB19VSNkNr7RqU7ZTJ6NGkFXVU:INSTANCE	Lcom/android/internal/os/-$$Lambda$BinderCallsStats$8JB19VSNkNr7RqU7ZTJ6NGkFXVU;
    //   390: invokeinterface 251 2 0
    //   395: aload 8
    //   397: invokeinterface 258 1 0
    //   402: astore_2
    //   403: aload_2
    //   404: invokeinterface 264 1 0
    //   409: ifeq +85 -> 494
    //   412: aload_2
    //   413: invokeinterface 267 1 0
    //   418: checkcast 9	com/android/internal/os/BinderCallsStats$CallStat
    //   421: astore 18
    //   423: aload 12
    //   425: iconst_0
    //   426: invokevirtual 282	java/lang/StringBuilder:setLength	(I)V
    //   429: aload 12
    //   431: ldc_w 284
    //   434: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   437: pop
    //   438: aload 12
    //   440: aload 17
    //   442: getfield 225	com/android/internal/os/BinderCallsStats$UidEntry:uid	I
    //   445: invokevirtual 291	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   448: pop
    //   449: aload 12
    //   451: ldc_w 293
    //   454: invokevirtual 288	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   457: pop
    //   458: aload 12
    //   460: aload 18
    //   462: invokevirtual 296	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   465: pop
    //   466: aload 12
    //   468: bipush 44
    //   470: invokevirtual 299	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
    //   473: pop
    //   474: aload 12
    //   476: aload 18
    //   478: getfield 107	com/android/internal/os/BinderCallsStats$CallStat:time	J
    //   481: invokevirtual 302	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   484: pop
    //   485: aload_1
    //   486: aload 12
    //   488: invokevirtual 212	java/io/PrintWriter:println	(Ljava/lang/Object;)V
    //   491: goto -88 -> 403
    //   494: goto -150 -> 344
    //   497: aload_1
    //   498: invokevirtual 304	java/io/PrintWriter:println	()V
    //   501: aload_1
    //   502: ldc_w 306
    //   505: invokevirtual 241	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   508: new 215	java/util/ArrayList
    //   511: dup
    //   512: aload_3
    //   513: invokeinterface 309 1 0
    //   518: invokespecial 274	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
    //   521: astore 8
    //   523: aload 8
    //   525: getstatic 314	com/android/internal/os/_$$Lambda$BinderCallsStats$BeSOWJ8AoyB7S9CtX_6IPAXHyNQ:INSTANCE	Lcom/android/internal/os/-$$Lambda$BinderCallsStats$BeSOWJ8AoyB7S9CtX-6IPAXHyNQ;
    //   528: invokeinterface 251 2 0
    //   533: aload 8
    //   535: invokeinterface 258 1 0
    //   540: astore_2
    //   541: aload 12
    //   543: astore_3
    //   544: aload_2
    //   545: invokeinterface 264 1 0
    //   550: ifeq +103 -> 653
    //   553: aload_2
    //   554: invokeinterface 267 1 0
    //   559: checkcast 111	java/util/Map$Entry
    //   562: astore 12
    //   564: aload 4
    //   566: aload 12
    //   568: invokeinterface 317 1 0
    //   573: invokeinterface 157 2 0
    //   578: checkcast 116	java/lang/Long
    //   581: astore 15
    //   583: aload_1
    //   584: ldc_w 319
    //   587: iconst_4
    //   588: anewarray 4	java/lang/Object
    //   591: dup
    //   592: iconst_0
    //   593: aload 12
    //   595: invokeinterface 317 1 0
    //   600: aastore
    //   601: dup
    //   602: iconst_1
    //   603: aload 12
    //   605: invokeinterface 114 1 0
    //   610: aastore
    //   611: dup
    //   612: iconst_2
    //   613: aload 12
    //   615: invokeinterface 114 1 0
    //   620: checkcast 116	java/lang/Long
    //   623: invokevirtual 234	java/lang/Long:longValue	()J
    //   626: l2d
    //   627: ldc2_w 320
    //   630: dmul
    //   631: lload 5
    //   633: l2d
    //   634: ddiv
    //   635: invokestatic 326	java/lang/Double:valueOf	(D)Ljava/lang/Double;
    //   638: aastore
    //   639: dup
    //   640: iconst_3
    //   641: aload 15
    //   643: aastore
    //   644: invokestatic 331	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   647: invokevirtual 241	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   650: goto -106 -> 544
    //   653: aload_1
    //   654: invokevirtual 304	java/io/PrintWriter:println	()V
    //   657: aload_1
    //   658: ldc_w 333
    //   661: iconst_3
    //   662: anewarray 4	java/lang/Object
    //   665: dup
    //   666: iconst_0
    //   667: lload 5
    //   669: invokestatic 237	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   672: aastore
    //   673: dup
    //   674: iconst_1
    //   675: lload 9
    //   677: invokestatic 237	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   680: aastore
    //   681: dup
    //   682: iconst_2
    //   683: lload 5
    //   685: l2d
    //   686: lload 9
    //   688: l2d
    //   689: ddiv
    //   690: invokestatic 326	java/lang/Double:valueOf	(D)Ljava/lang/Double;
    //   693: aastore
    //   694: invokestatic 331	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   697: invokevirtual 241	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   700: goto +155 -> 855
    //   703: aload_1
    //   704: ldc_w 335
    //   707: invokevirtual 241	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   710: new 215	java/util/ArrayList
    //   713: dup
    //   714: aload_3
    //   715: invokeinterface 309 1 0
    //   720: invokespecial 274	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
    //   723: astore 12
    //   725: aload 12
    //   727: getstatic 340	com/android/internal/os/_$$Lambda$BinderCallsStats$jhdszMKzG9FSuIQ4Vz9B0exXKPk:INSTANCE	Lcom/android/internal/os/-$$Lambda$BinderCallsStats$jhdszMKzG9FSuIQ4Vz9B0exXKPk;
    //   730: invokeinterface 251 2 0
    //   735: aload 12
    //   737: invokeinterface 258 1 0
    //   742: astore 15
    //   744: aload 4
    //   746: astore_2
    //   747: aload_3
    //   748: astore 8
    //   750: aload 15
    //   752: astore_3
    //   753: aload 12
    //   755: astore 4
    //   757: aload_3
    //   758: invokeinterface 264 1 0
    //   763: ifeq +92 -> 855
    //   766: aload_3
    //   767: invokeinterface 267 1 0
    //   772: checkcast 111	java/util/Map$Entry
    //   775: astore 15
    //   777: aload_2
    //   778: aload 15
    //   780: invokeinterface 317 1 0
    //   785: invokeinterface 157 2 0
    //   790: checkcast 116	java/lang/Long
    //   793: astore 12
    //   795: aload_1
    //   796: ldc_w 342
    //   799: iconst_3
    //   800: anewarray 4	java/lang/Object
    //   803: dup
    //   804: iconst_0
    //   805: aload 15
    //   807: invokeinterface 317 1 0
    //   812: aastore
    //   813: dup
    //   814: iconst_1
    //   815: aload 12
    //   817: aastore
    //   818: dup
    //   819: iconst_2
    //   820: aload 15
    //   822: invokeinterface 114 1 0
    //   827: checkcast 116	java/lang/Long
    //   830: invokevirtual 234	java/lang/Long:longValue	()J
    //   833: l2d
    //   834: ldc2_w 320
    //   837: dmul
    //   838: lload 5
    //   840: l2d
    //   841: ddiv
    //   842: invokestatic 326	java/lang/Double:valueOf	(D)Ljava/lang/Double;
    //   845: aastore
    //   846: invokestatic 331	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   849: invokevirtual 241	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   852: goto -95 -> 757
    //   855: return
    //   856: astore_1
    //   857: aload_2
    //   858: monitorexit
    //   859: aload_1
    //   860: athrow
    //   861: astore_1
    //   862: goto -5 -> 857
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	865	0	this	BinderCallsStats
    //   0	865	1	paramPrintWriter	java.io.PrintWriter
    //   1	857	2	localObject1	Object
    //   9	758	3	localObject2	Object
    //   17	739	4	localObject3	Object
    //   20	819	5	l1	long
    //   48	29	7	i	int
    //   57	692	8	localObject4	Object
    //   67	620	9	l2	long
    //   70	210	11	j	int
    //   91	725	12	localObject5	Object
    //   108	175	13	l3	long
    //   134	687	15	localObject6	Object
    //   141	109	16	k	int
    //   364	77	17	localUidEntry	UidEntry
    //   421	56	18	localCallStat	CallStat
    // Exception table:
    //   from	to	target	type
    //   165	175	289	finally
    //   175	219	289	finally
    //   224	231	289	finally
    //   234	247	289	finally
    //   247	272	289	finally
    //   117	143	293	finally
    //   158	165	293	finally
    //   79	110	297	finally
    //   148	155	297	finally
    //   301	303	856	finally
    //   857	859	861	finally
  }
  
  public void reset()
  {
    synchronized (mLock)
    {
      mUidEntries.clear();
      mStartTime = System.currentTimeMillis();
      return;
    }
  }
  
  public void setDetailedTracking(boolean paramBoolean)
  {
    if (paramBoolean != mDetailedTracking)
    {
      reset();
      mDetailedTracking = paramBoolean;
    }
  }
  
  public static class CallSession
  {
    BinderCallsStats.CallStat mCallStat = new BinderCallsStats.CallStat();
    int mCallingUId;
    long mStarted;
    
    public CallSession() {}
  }
  
  private static class CallStat
  {
    long callCount;
    String className;
    int msg;
    long time;
    
    CallStat() {}
    
    CallStat(String paramString, int paramInt)
    {
      className = paramString;
      msg = paramInt;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      paramObject = (CallStat)paramObject;
      if ((msg != msg) || (!className.equals(className))) {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return 31 * className.hashCode() + msg;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(className);
      localStringBuilder.append("/");
      localStringBuilder.append(msg);
      return localStringBuilder.toString();
    }
  }
  
  private static class UidEntry
  {
    long callCount;
    Map<BinderCallsStats.CallStat, BinderCallsStats.CallStat> mCallStats = new ArrayMap();
    long time;
    int uid;
    
    UidEntry(int paramInt)
    {
      uid = paramInt;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject) {
        return true;
      }
      paramObject = (UidEntry)paramObject;
      if (uid != uid) {
        bool = false;
      }
      return bool;
    }
    
    public int hashCode()
    {
      return uid;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UidEntry{time=");
      localStringBuilder.append(time);
      localStringBuilder.append(", callCount=");
      localStringBuilder.append(callCount);
      localStringBuilder.append(", mCallStats=");
      localStringBuilder.append(mCallStats);
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
  }
}
