package android.view;

import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.TimeUtils;
import android.view.animation.AnimationUtils;
import java.io.PrintWriter;

public final class Choreographer
{
  public static final int CALLBACK_ANIMATION = 1;
  public static final int CALLBACK_COMMIT = 3;
  public static final int CALLBACK_INPUT = 0;
  private static final int CALLBACK_LAST = 3;
  private static final String[] CALLBACK_TRACE_TITLES = { "input", "animation", "traversal", "commit" };
  public static final int CALLBACK_TRAVERSAL = 2;
  private static final boolean DEBUG_FRAMES = false;
  private static final boolean DEBUG_JANK = false;
  private static final long DEFAULT_FRAME_DELAY = 10L;
  private static final Object FRAME_CALLBACK_TOKEN;
  private static final int MOTION_EVENT_ACTION_CANCEL = 3;
  private static final int MOTION_EVENT_ACTION_DOWN = 0;
  private static final int MOTION_EVENT_ACTION_MOVE = 2;
  private static final int MOTION_EVENT_ACTION_UP = 1;
  private static final int MSG_DO_FRAME = 0;
  private static final int MSG_DO_SCHEDULE_CALLBACK = 2;
  private static final int MSG_DO_SCHEDULE_VSYNC = 1;
  private static final boolean OPTS_INPUT = true;
  private static final int SKIPPED_FRAME_WARNING_LIMIT;
  private static final String TAG = "Choreographer";
  private static final boolean USE_FRAME_TIME;
  private static final boolean USE_VSYNC;
  private static volatile Choreographer mMainInstance;
  private static volatile long sFrameDelay = 10L;
  private static final ThreadLocal<Choreographer> sSfThreadInstance;
  private static final ThreadLocal<Choreographer> sThreadInstance = new ThreadLocal()
  {
    protected Choreographer initialValue()
    {
      Looper localLooper = Looper.myLooper();
      if (localLooper != null)
      {
        Choreographer localChoreographer = new Choreographer(localLooper, 0, null);
        if (localLooper == Looper.getMainLooper()) {
          Choreographer.access$102(localChoreographer);
        }
        return localChoreographer;
      }
      throw new IllegalStateException("The current thread must have a looper!");
    }
  };
  private CallbackRecord mCallbackPool;
  private final CallbackQueue[] mCallbackQueues;
  private boolean mCallbacksRunning;
  private boolean mConsumedDown;
  private boolean mConsumedMove;
  private boolean mDebugPrintNextFrameTimeDelta;
  private final FrameDisplayEventReceiver mDisplayEventReceiver;
  private int mFPSDivisor = 1;
  FrameInfo mFrameInfo;
  private long mFrameIntervalNanos;
  private boolean mFrameScheduled;
  private final FrameHandler mHandler;
  private long mLastFrameTimeNanos;
  private final Object mLock = new Object();
  private final Looper mLooper;
  private int mMotionEventType = -1;
  private int mTouchMoveNum = -1;
  
  static
  {
    sSfThreadInstance = new ThreadLocal()
    {
      protected Choreographer initialValue()
      {
        Looper localLooper = Looper.myLooper();
        if (localLooper != null) {
          return new Choreographer(localLooper, 1, null);
        }
        throw new IllegalStateException("The current thread must have a looper!");
      }
    };
    USE_VSYNC = SystemProperties.getBoolean("debug.choreographer.vsync", true);
    USE_FRAME_TIME = SystemProperties.getBoolean("debug.choreographer.frametime", true);
    SKIPPED_FRAME_WARNING_LIMIT = SystemProperties.getInt("debug.choreographer.skipwarning", 30);
    FRAME_CALLBACK_TOKEN = new Object()
    {
      public String toString()
      {
        return "FRAME_CALLBACK_TOKEN";
      }
    };
  }
  
  private Choreographer(Looper paramLooper, int paramInt)
  {
    int i = 0;
    mConsumedMove = false;
    mConsumedDown = false;
    mFrameInfo = new FrameInfo();
    mLooper = paramLooper;
    mHandler = new FrameHandler(paramLooper);
    if (USE_VSYNC) {
      paramLooper = new FrameDisplayEventReceiver(paramLooper, paramInt);
    } else {
      paramLooper = null;
    }
    mDisplayEventReceiver = paramLooper;
    mLastFrameTimeNanos = Long.MIN_VALUE;
    mFrameIntervalNanos = ((1.0E9F / getRefreshRate()));
    mCallbackQueues = new CallbackQueue[4];
    for (paramInt = i; paramInt <= 3; paramInt++) {
      mCallbackQueues[paramInt] = new CallbackQueue(null);
    }
    setFPSDivisor(SystemProperties.getInt("debug.hwui.fps_divisor", 1));
  }
  
  private void dispose()
  {
    mDisplayEventReceiver.dispose();
  }
  
  public static long getFrameDelay()
  {
    return sFrameDelay;
  }
  
  public static Choreographer getInstance()
  {
    return (Choreographer)sThreadInstance.get();
  }
  
  public static Choreographer getMainThreadInstance()
  {
    return mMainInstance;
  }
  
  private static float getRefreshRate()
  {
    return DisplayManagerGlobal.getInstance().getDisplayInfo(0).getMode().getRefreshRate();
  }
  
  public static Choreographer getSfInstance()
  {
    return (Choreographer)sSfThreadInstance.get();
  }
  
  private boolean isRunningOnLooperThreadLocked()
  {
    boolean bool;
    if (Looper.myLooper() == mLooper) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private CallbackRecord obtainCallbackLocked(long paramLong, Object paramObject1, Object paramObject2)
  {
    CallbackRecord localCallbackRecord = mCallbackPool;
    if (localCallbackRecord == null)
    {
      localCallbackRecord = new CallbackRecord(null);
    }
    else
    {
      mCallbackPool = next;
      next = null;
    }
    dueTime = paramLong;
    action = paramObject1;
    token = paramObject2;
    return localCallbackRecord;
  }
  
  private void postCallbackDelayedInternal(int paramInt, Object paramObject1, Object paramObject2, long paramLong)
  {
    synchronized (mLock)
    {
      long l = SystemClock.uptimeMillis();
      paramLong = l + paramLong;
      mCallbackQueues[paramInt].addCallbackLocked(paramLong, paramObject1, paramObject2);
      if (paramLong <= l)
      {
        scheduleFrameLocked(l);
      }
      else
      {
        paramObject1 = mHandler.obtainMessage(2, paramObject1);
        arg1 = paramInt;
        paramObject1.setAsynchronous(true);
        mHandler.sendMessageAtTime(paramObject1, paramLong);
      }
      return;
    }
  }
  
  private void recycleCallbackLocked(CallbackRecord paramCallbackRecord)
  {
    action = null;
    token = null;
    next = mCallbackPool;
    mCallbackPool = paramCallbackRecord;
  }
  
  public static void releaseInstance()
  {
    Choreographer localChoreographer = (Choreographer)sThreadInstance.get();
    sThreadInstance.remove();
    localChoreographer.dispose();
  }
  
  private void removeCallbacksInternal(int paramInt, Object paramObject1, Object paramObject2)
  {
    synchronized (mLock)
    {
      mCallbackQueues[paramInt].removeCallbacksLocked(paramObject1, paramObject2);
      if ((paramObject1 != null) && (paramObject2 == null)) {
        mHandler.removeMessages(2, paramObject1);
      }
      return;
    }
  }
  
  private void scheduleFrameLocked(long paramLong)
  {
    if (!mFrameScheduled)
    {
      mFrameScheduled = true;
      Object localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("scheduleFrameLocked-mMotionEventType:");
      ((StringBuilder)localObject1).append(mMotionEventType);
      ((StringBuilder)localObject1).append(" mTouchMoveNum:");
      ((StringBuilder)localObject1).append(mTouchMoveNum);
      ((StringBuilder)localObject1).append(" mConsumedDown:");
      ((StringBuilder)localObject1).append(mConsumedDown);
      ((StringBuilder)localObject1).append(" mConsumedMove:");
      ((StringBuilder)localObject1).append(mConsumedMove);
      Trace.traceBegin(8L, ((StringBuilder)localObject1).toString());
      Trace.traceEnd(8L);
      try
      {
        switch (mMotionEventType)
        {
        default: 
          break;
        case 2: 
          mConsumedDown = false;
          if ((mTouchMoveNum == 1) && (!mConsumedMove))
          {
            localObject1 = mHandler.obtainMessage(0);
            ((Message)localObject1).setAsynchronous(true);
            mHandler.sendMessageAtFrontOfQueue((Message)localObject1);
            mConsumedMove = true;
            return;
          }
          break;
        case 1: 
        case 3: 
          mConsumedMove = false;
          mConsumedDown = false;
          break;
        case 0: 
          mConsumedMove = false;
          if (!mConsumedDown)
          {
            localObject1 = mHandler.obtainMessage(0);
            ((Message)localObject1).setAsynchronous(true);
            mHandler.sendMessageAtFrontOfQueue((Message)localObject1);
            mConsumedDown = true;
            return;
          }
          break;
        }
        if (USE_VSYNC)
        {
          if (isRunningOnLooperThreadLocked())
          {
            scheduleVsyncLocked();
          }
          else
          {
            localObject1 = mHandler.obtainMessage(1);
            ((Message)localObject1).setAsynchronous(true);
            mHandler.sendMessageAtFrontOfQueue((Message)localObject1);
          }
        }
        else
        {
          paramLong = Math.max(mLastFrameTimeNanos / 1000000L + sFrameDelay, paramLong);
          localObject1 = mHandler.obtainMessage(0);
          ((Message)localObject1).setAsynchronous(true);
          mHandler.sendMessageAtTime((Message)localObject1, paramLong);
        }
      }
      finally {}
    }
  }
  
  private void scheduleVsyncLocked()
  {
    mDisplayEventReceiver.scheduleVsync();
  }
  
  public static void setFrameDelay(long paramLong)
  {
    sFrameDelay = paramLong;
  }
  
  public static long subtractFrameDelay(long paramLong)
  {
    long l = sFrameDelay;
    if (paramLong <= l) {
      paramLong = 0L;
    } else {
      paramLong -= l;
    }
    return paramLong;
  }
  
  /* Error */
  void doCallbacks(int paramInt, long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 145	android/view/Choreographer:mLock	Ljava/lang/Object;
    //   4: astore 4
    //   6: aload 4
    //   8: monitorenter
    //   9: invokestatic 392	java/lang/System:nanoTime	()J
    //   12: lstore 5
    //   14: aload_0
    //   15: getfield 185	android/view/Choreographer:mCallbackQueues	[Landroid/view/Choreographer$CallbackQueue;
    //   18: iload_1
    //   19: aaload
    //   20: lload 5
    //   22: ldc2_w 372
    //   25: ldiv
    //   26: invokevirtual 396	android/view/Choreographer$CallbackQueue:extractDueCallbacksLocked	(J)Landroid/view/Choreographer$CallbackRecord;
    //   29: astore 7
    //   31: aload 7
    //   33: ifnonnull +7 -> 40
    //   36: aload 4
    //   38: monitorexit
    //   39: return
    //   40: aload_0
    //   41: iconst_1
    //   42: putfield 398	android/view/Choreographer:mCallbacksRunning	Z
    //   45: iload_1
    //   46: iconst_3
    //   47: if_icmpne +68 -> 115
    //   50: lload 5
    //   52: lload_2
    //   53: lsub
    //   54: lstore 8
    //   56: ldc2_w 345
    //   59: ldc_w 400
    //   62: lload 8
    //   64: l2i
    //   65: invokestatic 404	android/os/Trace:traceCounter	(JLjava/lang/String;I)V
    //   68: lload 8
    //   70: ldc2_w 405
    //   73: aload_0
    //   74: getfield 183	android/view/Choreographer:mFrameIntervalNanos	J
    //   77: lmul
    //   78: lcmp
    //   79: iflt +36 -> 115
    //   82: aload_0
    //   83: getfield 183	android/view/Choreographer:mFrameIntervalNanos	J
    //   86: lstore 10
    //   88: aload_0
    //   89: getfield 183	android/view/Choreographer:mFrameIntervalNanos	J
    //   92: lstore_2
    //   93: lload 5
    //   95: lload 8
    //   97: lload 10
    //   99: lrem
    //   100: lload_2
    //   101: ladd
    //   102: lsub
    //   103: lstore_2
    //   104: lload_2
    //   105: lstore 8
    //   107: aload_0
    //   108: lload_2
    //   109: putfield 176	android/view/Choreographer:mLastFrameTimeNanos	J
    //   112: goto +3 -> 115
    //   115: lload_2
    //   116: lstore 8
    //   118: aload 4
    //   120: monitorexit
    //   121: ldc2_w 345
    //   124: getstatic 140	android/view/Choreographer:CALLBACK_TRACE_TITLES	[Ljava/lang/String;
    //   127: iload_1
    //   128: aaload
    //   129: invokestatic 356	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   132: aload 7
    //   134: astore 4
    //   136: aload 4
    //   138: ifnull +19 -> 157
    //   141: aload 4
    //   143: lload_2
    //   144: invokevirtual 409	android/view/Choreographer$CallbackRecord:run	(J)V
    //   147: aload 4
    //   149: getfield 265	android/view/Choreographer$CallbackRecord:next	Landroid/view/Choreographer$CallbackRecord;
    //   152: astore 4
    //   154: goto -18 -> 136
    //   157: aload_0
    //   158: getfield 145	android/view/Choreographer:mLock	Ljava/lang/Object;
    //   161: astore 12
    //   163: aload 12
    //   165: monitorenter
    //   166: aload_0
    //   167: iconst_0
    //   168: putfield 398	android/view/Choreographer:mCallbacksRunning	Z
    //   171: aload 7
    //   173: getfield 265	android/view/Choreographer$CallbackRecord:next	Landroid/view/Choreographer$CallbackRecord;
    //   176: astore 4
    //   178: aload_0
    //   179: aload 7
    //   181: invokespecial 217	android/view/Choreographer:recycleCallbackLocked	(Landroid/view/Choreographer$CallbackRecord;)V
    //   184: aload 4
    //   186: astore 7
    //   188: aload 4
    //   190: ifnonnull -19 -> 171
    //   193: aload 12
    //   195: monitorexit
    //   196: ldc2_w 345
    //   199: invokestatic 359	android/os/Trace:traceEnd	(J)V
    //   202: return
    //   203: astore 7
    //   205: aload 12
    //   207: monitorexit
    //   208: aload 7
    //   210: athrow
    //   211: astore 13
    //   213: aload_0
    //   214: getfield 145	android/view/Choreographer:mLock	Ljava/lang/Object;
    //   217: astore 12
    //   219: aload 12
    //   221: monitorenter
    //   222: aload_0
    //   223: iconst_0
    //   224: putfield 398	android/view/Choreographer:mCallbacksRunning	Z
    //   227: aload 7
    //   229: getfield 265	android/view/Choreographer$CallbackRecord:next	Landroid/view/Choreographer$CallbackRecord;
    //   232: astore 4
    //   234: aload_0
    //   235: aload 7
    //   237: invokespecial 217	android/view/Choreographer:recycleCallbackLocked	(Landroid/view/Choreographer$CallbackRecord;)V
    //   240: aload 4
    //   242: astore 7
    //   244: aload 7
    //   246: ifnull +6 -> 252
    //   249: goto -22 -> 227
    //   252: aload 12
    //   254: monitorexit
    //   255: ldc2_w 345
    //   258: invokestatic 359	android/os/Trace:traceEnd	(J)V
    //   261: aload 13
    //   263: athrow
    //   264: astore 7
    //   266: aload 12
    //   268: monitorexit
    //   269: aload 7
    //   271: athrow
    //   272: astore 7
    //   274: goto +5 -> 279
    //   277: astore 7
    //   279: aload 4
    //   281: monitorexit
    //   282: aload 7
    //   284: athrow
    //   285: astore 7
    //   287: goto -8 -> 279
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	290	0	this	Choreographer
    //   0	290	1	paramInt	int
    //   0	290	2	paramLong	long
    //   4	276	4	localObject1	Object
    //   12	82	5	l1	long
    //   29	158	7	localObject2	Object
    //   203	33	7	localCallbackRecord	CallbackRecord
    //   242	3	7	localObject3	Object
    //   264	6	7	localObject4	Object
    //   272	1	7	localObject5	Object
    //   277	6	7	localObject6	Object
    //   285	1	7	localObject7	Object
    //   54	63	8	l2	long
    //   86	12	10	l3	long
    //   161	106	12	localObject8	Object
    //   211	51	13	localObject9	Object
    // Exception table:
    //   from	to	target	type
    //   166	171	203	finally
    //   171	184	203	finally
    //   193	196	203	finally
    //   205	208	203	finally
    //   121	132	211	finally
    //   141	154	211	finally
    //   222	227	264	finally
    //   227	240	264	finally
    //   252	255	264	finally
    //   266	269	264	finally
    //   107	112	272	finally
    //   118	121	272	finally
    //   9	31	277	finally
    //   36	39	277	finally
    //   40	45	277	finally
    //   56	93	277	finally
    //   279	282	285	finally
  }
  
  void doFrame(long paramLong, int paramInt)
  {
    long l1;
    long l2;
    synchronized (mLock)
    {
      if (!mFrameScheduled) {
        return;
      }
      l1 = System.nanoTime();
      l2 = l1 - paramLong;
      if (l2 >= mFrameIntervalNanos)
      {
        long l3 = l2 / mFrameIntervalNanos;
        if (l3 >= SKIPPED_FRAME_WARNING_LIMIT)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Skipped ");
          localStringBuilder.append(l3);
          localStringBuilder.append(" frames!  The application may be doing too much work on its main thread.");
          Log.i("Choreographer", localStringBuilder.toString());
        }
        l3 = mFrameIntervalNanos;
        l1 -= l2 % l3;
      }
      else
      {
        l1 = paramLong;
      }
    }
    try
    {
      if (l1 < mLastFrameTimeNanos)
      {
        scheduleVsyncLocked();
        return;
      }
      if (mFPSDivisor > 1)
      {
        l2 = l1 - mLastFrameTimeNanos;
        if ((l2 < mFrameIntervalNanos * mFPSDivisor) && (l2 > 0L))
        {
          scheduleVsyncLocked();
          return;
        }
      }
      mFrameInfo.setVsync(paramLong, l1);
      mFrameScheduled = false;
      mLastFrameTimeNanos = l1;
      try
      {
        Trace.traceBegin(8L, "Choreographer#doFrame");
        AnimationUtils.lockAnimationClock(l1 / 1000000L);
        mFrameInfo.markInputHandlingStart();
        doCallbacks(0, l1);
        mFrameInfo.markAnimationsStart();
        doCallbacks(1, l1);
        mFrameInfo.markPerformTraversalsStart();
        doCallbacks(2, l1);
        doCallbacks(3, l1);
        return;
      }
      finally
      {
        AnimationUtils.unlockAnimationClock();
        Trace.traceEnd(8L);
      }
      localObject3 = finally;
    }
    finally
    {
      for (;;) {}
    }
    throw localObject3;
  }
  
  void doScheduleCallback(int paramInt)
  {
    synchronized (mLock)
    {
      if (!mFrameScheduled)
      {
        long l = SystemClock.uptimeMillis();
        if (mCallbackQueues[paramInt].hasDueCallbacksLocked(l)) {
          scheduleFrameLocked(l);
        }
      }
      return;
    }
  }
  
  void doScheduleVsync()
  {
    synchronized (mLock)
    {
      if (mFrameScheduled) {
        scheduleVsyncLocked();
      }
      return;
    }
  }
  
  void dump(String paramString, PrintWriter paramPrintWriter)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append("  ");
    localObject = ((StringBuilder)localObject).toString();
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("Choreographer:");
    paramPrintWriter.print((String)localObject);
    paramPrintWriter.print("mFrameScheduled=");
    paramPrintWriter.println(mFrameScheduled);
    paramPrintWriter.print((String)localObject);
    paramPrintWriter.print("mLastFrameTime=");
    paramPrintWriter.println(TimeUtils.formatUptime(mLastFrameTimeNanos / 1000000L));
  }
  
  public long getFrameIntervalNanos()
  {
    return mFrameIntervalNanos;
  }
  
  public long getFrameTime()
  {
    return getFrameTimeNanos() / 1000000L;
  }
  
  public long getFrameTimeNanos()
  {
    synchronized (mLock)
    {
      if (mCallbacksRunning)
      {
        long l;
        if (USE_FRAME_TIME) {
          l = mLastFrameTimeNanos;
        } else {
          l = System.nanoTime();
        }
        return l;
      }
      IllegalStateException localIllegalStateException = new java/lang/IllegalStateException;
      localIllegalStateException.<init>("This method must only be called as part of a callback while a frame is in progress.");
      throw localIllegalStateException;
    }
  }
  
  public long getLastFrameTimeNanos()
  {
    synchronized (mLock)
    {
      long l;
      if (USE_FRAME_TIME) {
        l = mLastFrameTimeNanos;
      } else {
        l = System.nanoTime();
      }
      return l;
    }
  }
  
  public void postCallback(int paramInt, Runnable paramRunnable, Object paramObject)
  {
    postCallbackDelayed(paramInt, paramRunnable, paramObject, 0L);
  }
  
  public void postCallbackDelayed(int paramInt, Runnable paramRunnable, Object paramObject, long paramLong)
  {
    if (paramRunnable != null)
    {
      if ((paramInt >= 0) && (paramInt <= 3))
      {
        postCallbackDelayedInternal(paramInt, paramRunnable, paramObject, paramLong);
        return;
      }
      throw new IllegalArgumentException("callbackType is invalid");
    }
    throw new IllegalArgumentException("action must not be null");
  }
  
  public void postFrameCallback(FrameCallback paramFrameCallback)
  {
    postFrameCallbackDelayed(paramFrameCallback, 0L);
  }
  
  public void postFrameCallbackDelayed(FrameCallback paramFrameCallback, long paramLong)
  {
    if (paramFrameCallback != null)
    {
      postCallbackDelayedInternal(1, paramFrameCallback, FRAME_CALLBACK_TOKEN, paramLong);
      return;
    }
    throw new IllegalArgumentException("callback must not be null");
  }
  
  public void removeCallbacks(int paramInt, Runnable paramRunnable, Object paramObject)
  {
    if ((paramInt >= 0) && (paramInt <= 3))
    {
      removeCallbacksInternal(paramInt, paramRunnable, paramObject);
      return;
    }
    throw new IllegalArgumentException("callbackType is invalid");
  }
  
  public void removeFrameCallback(FrameCallback paramFrameCallback)
  {
    if (paramFrameCallback != null)
    {
      removeCallbacksInternal(1, paramFrameCallback, FRAME_CALLBACK_TOKEN);
      return;
    }
    throw new IllegalArgumentException("callback must not be null");
  }
  
  void setFPSDivisor(int paramInt)
  {
    int i = paramInt;
    if (paramInt <= 0) {
      i = 1;
    }
    mFPSDivisor = i;
    ThreadedRenderer.setFPSDivisor(i);
  }
  
  public void setMotionEventInfo(int paramInt1, int paramInt2)
  {
    try
    {
      mTouchMoveNum = paramInt2;
      mMotionEventType = paramInt1;
      return;
    }
    finally {}
  }
  
  private final class CallbackQueue
  {
    private Choreographer.CallbackRecord mHead;
    
    private CallbackQueue() {}
    
    public void addCallbackLocked(long paramLong, Object paramObject1, Object paramObject2)
    {
      Choreographer.CallbackRecord localCallbackRecord = Choreographer.this.obtainCallbackLocked(paramLong, paramObject1, paramObject2);
      paramObject2 = mHead;
      if (paramObject2 == null)
      {
        mHead = localCallbackRecord;
        return;
      }
      paramObject1 = paramObject2;
      if (paramLong < dueTime)
      {
        next = paramObject2;
        mHead = localCallbackRecord;
        return;
      }
      while (next != null)
      {
        if (paramLong < next.dueTime)
        {
          next = next;
          break;
        }
        paramObject1 = next;
      }
      next = localCallbackRecord;
    }
    
    public Choreographer.CallbackRecord extractDueCallbacksLocked(long paramLong)
    {
      Choreographer.CallbackRecord localCallbackRecord1 = mHead;
      if ((localCallbackRecord1 != null) && (dueTime <= paramLong))
      {
        Object localObject = localCallbackRecord1;
        for (Choreographer.CallbackRecord localCallbackRecord2 = next; localCallbackRecord2 != null; localCallbackRecord2 = next)
        {
          if (dueTime > paramLong)
          {
            next = null;
            break;
          }
          localObject = localCallbackRecord2;
        }
        mHead = localCallbackRecord2;
        return localCallbackRecord1;
      }
      return null;
    }
    
    public boolean hasDueCallbacksLocked(long paramLong)
    {
      boolean bool;
      if ((mHead != null) && (mHead.dueTime <= paramLong)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void removeCallbacksLocked(Object paramObject1, Object paramObject2)
    {
      Object localObject1 = null;
      Choreographer.CallbackRecord localCallbackRecord;
      for (Object localObject2 = mHead; localObject2 != null; localObject2 = localCallbackRecord)
      {
        localCallbackRecord = next;
        if (((paramObject1 != null) && (action != paramObject1)) || ((paramObject2 != null) && (token != paramObject2)))
        {
          localObject1 = localObject2;
        }
        else
        {
          if (localObject1 != null) {
            next = localCallbackRecord;
          } else {
            mHead = localCallbackRecord;
          }
          Choreographer.this.recycleCallbackLocked((Choreographer.CallbackRecord)localObject2);
        }
      }
    }
  }
  
  private static final class CallbackRecord
  {
    public Object action;
    public long dueTime;
    public CallbackRecord next;
    public Object token;
    
    private CallbackRecord() {}
    
    public void run(long paramLong)
    {
      if (token == Choreographer.FRAME_CALLBACK_TOKEN) {
        ((Choreographer.FrameCallback)action).doFrame(paramLong);
      } else {
        ((Runnable)action).run();
      }
    }
  }
  
  public static abstract interface FrameCallback
  {
    public abstract void doFrame(long paramLong);
  }
  
  private final class FrameDisplayEventReceiver
    extends DisplayEventReceiver
    implements Runnable
  {
    private int mFrame;
    private boolean mHavePendingVsync;
    private long mTimestampNanos;
    
    public FrameDisplayEventReceiver(Looper paramLooper, int paramInt)
    {
      super(paramInt);
    }
    
    public void onVsync(long paramLong, int paramInt1, int paramInt2)
    {
      if (paramInt1 != 0)
      {
        Log.d("Choreographer", "Received vsync from secondary display, but we don't support this case yet.  Choreographer needs a way to explicitly request vsync for a specific display to ensure it doesn't lose track of its scheduled vsync.");
        scheduleVsync();
        return;
      }
      long l1 = System.nanoTime();
      long l2 = paramLong;
      if (paramLong > l1)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Frame time is ");
        ((StringBuilder)localObject).append((float)(paramLong - l1) * 1.0E-6F);
        ((StringBuilder)localObject).append(" ms in the future!  Check that graphics HAL is generating vsync timestamps using the correct timebase.");
        Log.w("Choreographer", ((StringBuilder)localObject).toString());
        l2 = l1;
      }
      if (mHavePendingVsync) {
        Log.w("Choreographer", "Already have a pending vsync event.  There should only be one at a time.");
      } else {
        mHavePendingVsync = true;
      }
      mTimestampNanos = l2;
      mFrame = paramInt2;
      Object localObject = Message.obtain(mHandler, this);
      ((Message)localObject).setAsynchronous(true);
      mHandler.sendMessageAtTime((Message)localObject, l2 / 1000000L);
    }
    
    public void run()
    {
      mHavePendingVsync = false;
      doFrame(mTimestampNanos, mFrame);
    }
  }
  
  private final class FrameHandler
    extends Handler
  {
    public FrameHandler(Looper paramLooper)
    {
      super();
    }
    
    public void handleMessage(Message paramMessage)
    {
      switch (what)
      {
      default: 
        break;
      case 2: 
        doScheduleCallback(arg1);
        break;
      case 1: 
        doScheduleVsync();
        break;
      case 0: 
        doFrame(System.nanoTime(), 0);
      }
    }
  }
}
