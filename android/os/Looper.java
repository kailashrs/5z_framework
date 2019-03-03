package android.os;

import android.util.Printer;
import android.util.Slog;
import android.util.proto.ProtoOutputStream;

public final class Looper
{
  private static final String TAG = "Looper";
  private static Looper sMainLooper;
  static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal();
  private Printer mLogging;
  final MessageQueue mQueue;
  private long mSlowDeliveryThresholdMs;
  private long mSlowDispatchThresholdMs;
  final Thread mThread;
  private long mTraceTag;
  
  private Looper(boolean paramBoolean)
  {
    mQueue = new MessageQueue(paramBoolean);
    mThread = Thread.currentThread();
  }
  
  public static Looper getMainLooper()
  {
    try
    {
      Looper localLooper = sMainLooper;
      return localLooper;
    }
    finally {}
  }
  
  /* Error */
  public static void loop()
  {
    // Byte code:
    //   0: invokestatic 57	android/os/Looper:myLooper	()Landroid/os/Looper;
    //   3: astore_0
    //   4: aload_0
    //   5: ifnull +725 -> 730
    //   8: aload_0
    //   9: getfield 41	android/os/Looper:mQueue	Landroid/os/MessageQueue;
    //   12: astore_1
    //   13: invokestatic 63	android/os/Binder:clearCallingIdentity	()J
    //   16: pop2
    //   17: invokestatic 63	android/os/Binder:clearCallingIdentity	()J
    //   20: lstore_2
    //   21: new 65	java/lang/StringBuilder
    //   24: dup
    //   25: invokespecial 66	java/lang/StringBuilder:<init>	()V
    //   28: astore 4
    //   30: aload 4
    //   32: ldc 68
    //   34: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   37: pop
    //   38: aload 4
    //   40: invokestatic 78	android/os/Process:myUid	()I
    //   43: invokevirtual 81	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   46: pop
    //   47: aload 4
    //   49: ldc 83
    //   51: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   54: pop
    //   55: aload 4
    //   57: invokestatic 47	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   60: invokevirtual 87	java/lang/Thread:getName	()Ljava/lang/String;
    //   63: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: pop
    //   67: aload 4
    //   69: ldc 89
    //   71: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: pop
    //   75: aload 4
    //   77: invokevirtual 92	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   80: iconst_0
    //   81: invokestatic 98	android/os/SystemProperties:getInt	(Ljava/lang/String;I)I
    //   84: istore 5
    //   86: iconst_0
    //   87: istore 6
    //   89: lconst_0
    //   90: lstore 7
    //   92: aload_1
    //   93: invokevirtual 102	android/os/MessageQueue:next	()Landroid/os/Message;
    //   96: astore 4
    //   98: aload 4
    //   100: ifnonnull +4 -> 104
    //   103: return
    //   104: aload_0
    //   105: getfield 104	android/os/Looper:mLogging	Landroid/util/Printer;
    //   108: astore 9
    //   110: aload 9
    //   112: ifnull +81 -> 193
    //   115: new 65	java/lang/StringBuilder
    //   118: dup
    //   119: invokespecial 66	java/lang/StringBuilder:<init>	()V
    //   122: astore 10
    //   124: aload 10
    //   126: ldc 106
    //   128: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: pop
    //   132: aload 10
    //   134: aload 4
    //   136: getfield 112	android/os/Message:target	Landroid/os/Handler;
    //   139: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   142: pop
    //   143: aload 10
    //   145: ldc 117
    //   147: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   150: pop
    //   151: aload 10
    //   153: aload 4
    //   155: getfield 121	android/os/Message:callback	Ljava/lang/Runnable;
    //   158: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   161: pop
    //   162: aload 10
    //   164: ldc 123
    //   166: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: pop
    //   170: aload 10
    //   172: aload 4
    //   174: getfield 127	android/os/Message:what	I
    //   177: invokevirtual 81	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   180: pop
    //   181: aload 9
    //   183: aload 10
    //   185: invokevirtual 92	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   188: invokeinterface 133 2 0
    //   193: invokestatic 138	android/os/SystemClock:elapsedRealtime	()J
    //   196: lstore 11
    //   198: aload_0
    //   199: getfield 140	android/os/Looper:mTraceTag	J
    //   202: lstore 13
    //   204: aload_0
    //   205: getfield 142	android/os/Looper:mSlowDispatchThresholdMs	J
    //   208: lstore 7
    //   210: aload_0
    //   211: getfield 144	android/os/Looper:mSlowDeliveryThresholdMs	J
    //   214: lstore 15
    //   216: iload 5
    //   218: ifle +16 -> 234
    //   221: iload 5
    //   223: i2l
    //   224: lstore 7
    //   226: iload 5
    //   228: i2l
    //   229: lstore 15
    //   231: goto +3 -> 234
    //   234: iconst_1
    //   235: istore 17
    //   237: lload 15
    //   239: lconst_0
    //   240: lcmp
    //   241: ifle +19 -> 260
    //   244: aload 4
    //   246: getfield 147	android/os/Message:when	J
    //   249: lconst_0
    //   250: lcmp
    //   251: ifle +9 -> 260
    //   254: iconst_1
    //   255: istore 18
    //   257: goto +6 -> 263
    //   260: iconst_0
    //   261: istore 18
    //   263: lload 7
    //   265: lconst_0
    //   266: lcmp
    //   267: ifle +9 -> 276
    //   270: iconst_1
    //   271: istore 19
    //   273: goto +6 -> 279
    //   276: iconst_0
    //   277: istore 19
    //   279: iload 17
    //   281: istore 20
    //   283: iload 18
    //   285: ifne +18 -> 303
    //   288: iload 19
    //   290: ifeq +10 -> 300
    //   293: iload 17
    //   295: istore 20
    //   297: goto +6 -> 303
    //   300: iconst_0
    //   301: istore 20
    //   303: lload 13
    //   305: lconst_0
    //   306: lcmp
    //   307: ifeq +26 -> 333
    //   310: lload 13
    //   312: invokestatic 153	android/os/Trace:isTagEnabled	(J)Z
    //   315: ifeq +18 -> 333
    //   318: lload 13
    //   320: aload 4
    //   322: getfield 112	android/os/Message:target	Landroid/os/Handler;
    //   325: aload 4
    //   327: invokevirtual 159	android/os/Handler:getTraceName	(Landroid/os/Message;)Ljava/lang/String;
    //   330: invokestatic 163	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   333: iload 20
    //   335: ifeq +11 -> 346
    //   338: invokestatic 166	android/os/SystemClock:uptimeMillis	()J
    //   341: lstore 21
    //   343: goto +6 -> 349
    //   346: lconst_0
    //   347: lstore 21
    //   349: aload 4
    //   351: getfield 112	android/os/Message:target	Landroid/os/Handler;
    //   354: aload 4
    //   356: invokevirtual 170	android/os/Handler:dispatchMessage	(Landroid/os/Message;)V
    //   359: iload 19
    //   361: ifeq +16 -> 377
    //   364: invokestatic 166	android/os/SystemClock:uptimeMillis	()J
    //   367: lstore 23
    //   369: goto +11 -> 380
    //   372: astore 4
    //   374: goto +341 -> 715
    //   377: lconst_0
    //   378: lstore 23
    //   380: lload 13
    //   382: lconst_0
    //   383: lcmp
    //   384: ifeq +8 -> 392
    //   387: lload 13
    //   389: invokestatic 174	android/os/Trace:traceEnd	(J)V
    //   392: iload 18
    //   394: ifeq +62 -> 456
    //   397: iload 6
    //   399: ifeq +32 -> 431
    //   402: lload 21
    //   404: aload 4
    //   406: getfield 147	android/os/Message:when	J
    //   409: lsub
    //   410: ldc2_w 175
    //   413: lcmp
    //   414: ifgt +14 -> 428
    //   417: ldc 8
    //   419: ldc -78
    //   421: invokestatic 184	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   424: pop
    //   425: iconst_0
    //   426: istore 6
    //   428: goto +28 -> 456
    //   431: lload 15
    //   433: aload 4
    //   435: getfield 147	android/os/Message:when	J
    //   438: lload 21
    //   440: ldc -70
    //   442: aload 4
    //   444: invokestatic 190	android/os/Looper:showSlowLog	(JJJLjava/lang/String;Landroid/os/Message;)Z
    //   447: ifeq +9 -> 456
    //   450: iconst_1
    //   451: istore 6
    //   453: goto +3 -> 456
    //   456: iload 19
    //   458: ifeq +17 -> 475
    //   461: lload 7
    //   463: lload 21
    //   465: lload 23
    //   467: ldc -64
    //   469: aload 4
    //   471: invokestatic 190	android/os/Looper:showSlowLog	(JJJLjava/lang/String;Landroid/os/Message;)Z
    //   474: pop
    //   475: invokestatic 138	android/os/SystemClock:elapsedRealtime	()J
    //   478: lstore 7
    //   480: aload 9
    //   482: ifnull +88 -> 570
    //   485: new 65	java/lang/StringBuilder
    //   488: dup
    //   489: invokespecial 66	java/lang/StringBuilder:<init>	()V
    //   492: astore 25
    //   494: aload 25
    //   496: ldc -62
    //   498: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   501: pop
    //   502: aload 4
    //   504: astore 10
    //   506: aload 25
    //   508: aload 10
    //   510: getfield 112	android/os/Message:target	Landroid/os/Handler;
    //   513: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   516: pop
    //   517: aload 25
    //   519: ldc 117
    //   521: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   524: pop
    //   525: aload 25
    //   527: aload 10
    //   529: getfield 121	android/os/Message:callback	Ljava/lang/Runnable;
    //   532: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   535: pop
    //   536: aload 25
    //   538: ldc -60
    //   540: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   543: pop
    //   544: aload 25
    //   546: lload 7
    //   548: lload 11
    //   550: lsub
    //   551: invokevirtual 199	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   554: pop
    //   555: aload 9
    //   557: aload 25
    //   559: invokevirtual 92	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   562: invokeinterface 133 2 0
    //   567: goto +3 -> 570
    //   570: invokestatic 63	android/os/Binder:clearCallingIdentity	()J
    //   573: lstore 15
    //   575: lload_2
    //   576: lload 15
    //   578: lcmp
    //   579: ifeq +126 -> 705
    //   582: new 65	java/lang/StringBuilder
    //   585: dup
    //   586: invokespecial 66	java/lang/StringBuilder:<init>	()V
    //   589: astore 9
    //   591: aload 9
    //   593: ldc -55
    //   595: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   598: pop
    //   599: aload 9
    //   601: lload_2
    //   602: invokestatic 207	java/lang/Long:toHexString	(J)Ljava/lang/String;
    //   605: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   608: pop
    //   609: aload 9
    //   611: ldc -47
    //   613: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   616: pop
    //   617: aload 9
    //   619: lload 15
    //   621: invokestatic 207	java/lang/Long:toHexString	(J)Ljava/lang/String;
    //   624: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   627: pop
    //   628: aload 9
    //   630: ldc -45
    //   632: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   635: pop
    //   636: aload 9
    //   638: aload 4
    //   640: getfield 112	android/os/Message:target	Landroid/os/Handler;
    //   643: invokevirtual 215	java/lang/Object:getClass	()Ljava/lang/Class;
    //   646: invokevirtual 218	java/lang/Class:getName	()Ljava/lang/String;
    //   649: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   652: pop
    //   653: aload 9
    //   655: ldc 117
    //   657: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   660: pop
    //   661: aload 9
    //   663: aload 4
    //   665: getfield 121	android/os/Message:callback	Ljava/lang/Runnable;
    //   668: invokevirtual 115	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   671: pop
    //   672: aload 9
    //   674: ldc -36
    //   676: invokevirtual 72	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   679: pop
    //   680: aload 9
    //   682: aload 4
    //   684: getfield 127	android/os/Message:what	I
    //   687: invokevirtual 81	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   690: pop
    //   691: ldc 8
    //   693: aload 9
    //   695: invokevirtual 92	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   698: invokestatic 225	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;)I
    //   701: pop
    //   702: goto +3 -> 705
    //   705: aload 4
    //   707: invokevirtual 228	android/os/Message:recycleUnchecked	()V
    //   710: goto -618 -> 92
    //   713: astore 4
    //   715: lload 13
    //   717: lconst_0
    //   718: lcmp
    //   719: ifeq +8 -> 727
    //   722: lload 13
    //   724: invokestatic 174	android/os/Trace:traceEnd	(J)V
    //   727: aload 4
    //   729: athrow
    //   730: new 230	java/lang/RuntimeException
    //   733: dup
    //   734: ldc -24
    //   736: invokespecial 234	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   739: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   3	208	0	localLooper	Looper
    //   12	81	1	localMessageQueue	MessageQueue
    //   20	582	2	l1	long
    //   28	327	4	localObject1	Object
    //   372	334	4	localMessage	Message
    //   713	15	4	localObject2	Object
    //   84	143	5	i	int
    //   87	365	6	j	int
    //   90	457	7	l2	long
    //   108	586	9	localObject3	Object
    //   122	406	10	localObject4	Object
    //   196	353	11	l3	long
    //   202	521	13	l4	long
    //   214	406	15	l5	long
    //   235	59	17	k	int
    //   255	138	18	m	int
    //   271	186	19	n	int
    //   281	53	20	i1	int
    //   341	123	21	l6	long
    //   367	99	23	l7	long
    //   492	66	25	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   364	369	372	finally
    //   349	359	713	finally
  }
  
  public static Looper myLooper()
  {
    return (Looper)sThreadLocal.get();
  }
  
  public static MessageQueue myQueue()
  {
    return myLoopermQueue;
  }
  
  public static void prepare()
  {
    prepare(true);
  }
  
  private static void prepare(boolean paramBoolean)
  {
    if (sThreadLocal.get() == null)
    {
      sThreadLocal.set(new Looper(paramBoolean));
      return;
    }
    throw new RuntimeException("Only one Looper may be created per thread");
  }
  
  public static void prepareMainLooper()
  {
    prepare(false);
    try
    {
      if (sMainLooper == null)
      {
        sMainLooper = myLooper();
        return;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("The main Looper has already been prepared.");
      throw localIllegalStateException;
    }
    finally {}
  }
  
  private static boolean showSlowLog(long paramLong1, long paramLong2, long paramLong3, String paramString, Message paramMessage)
  {
    paramLong2 = paramLong3 - paramLong2;
    if (paramLong2 < paramLong1) {
      return false;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Slow ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(" took ");
    localStringBuilder.append(paramLong2);
    localStringBuilder.append("ms ");
    localStringBuilder.append(Thread.currentThread().getName());
    localStringBuilder.append(" h=");
    localStringBuilder.append(target.getClass().getName());
    localStringBuilder.append(" c=");
    localStringBuilder.append(callback);
    localStringBuilder.append(" m=");
    localStringBuilder.append(what);
    Slog.w("Looper", localStringBuilder.toString());
    return true;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(toString());
    paramPrinter.println(((StringBuilder)localObject).toString());
    localObject = mQueue;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  ");
    ((MessageQueue)localObject).dump(paramPrinter, localStringBuilder.toString(), null);
  }
  
  public void dump(Printer paramPrinter, String paramString, Handler paramHandler)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(toString());
    paramPrinter.println(((StringBuilder)localObject).toString());
    localObject = mQueue;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("  ");
    ((MessageQueue)localObject).dump(paramPrinter, localStringBuilder.toString(), paramHandler);
  }
  
  public MessageQueue getQueue()
  {
    return mQueue;
  }
  
  public Thread getThread()
  {
    return mThread;
  }
  
  public boolean isCurrentThread()
  {
    boolean bool;
    if (Thread.currentThread() == mThread) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void quit()
  {
    mQueue.quit(false);
  }
  
  public void quitSafely()
  {
    mQueue.quit(true);
  }
  
  public void setMessageLogging(Printer paramPrinter)
  {
    mLogging = paramPrinter;
  }
  
  public void setSlowLogThresholdMs(long paramLong1, long paramLong2)
  {
    mSlowDispatchThresholdMs = paramLong1;
    mSlowDeliveryThresholdMs = paramLong2;
  }
  
  public void setTraceTag(long paramLong)
  {
    mTraceTag = paramLong;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Looper (");
    localStringBuilder.append(mThread.getName());
    localStringBuilder.append(", tid ");
    localStringBuilder.append(mThread.getId());
    localStringBuilder.append(") {");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, mThread.getName());
    paramProtoOutputStream.write(1112396529666L, mThread.getId());
    mQueue.writeToProto(paramProtoOutputStream, 1146756268035L);
    paramProtoOutputStream.end(paramLong);
  }
}
