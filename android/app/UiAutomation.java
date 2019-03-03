package android.app;

import android.accessibilityservice.AccessibilityService.Callbacks;
import android.accessibilityservice.AccessibilityService.IAccessibilityServiceClientWrapper;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.accessibilityservice.IAccessibilityServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.WindowAnimationFrameStats;
import android.view.WindowContentFrameStats;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityInteractionClient;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.internal.util.function.pooled.PooledRunnable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public final class UiAutomation
{
  private static final int CONNECTION_ID_UNDEFINED = -1;
  private static final long CONNECT_TIMEOUT_MILLIS = 5000L;
  private static final boolean DEBUG = false;
  public static final int FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES = 1;
  private static final String LOG_TAG = UiAutomation.class.getSimpleName();
  public static final int ROTATION_FREEZE_0 = 0;
  public static final int ROTATION_FREEZE_180 = 2;
  public static final int ROTATION_FREEZE_270 = 3;
  public static final int ROTATION_FREEZE_90 = 1;
  public static final int ROTATION_FREEZE_CURRENT = -1;
  public static final int ROTATION_UNFREEZE = -2;
  private IAccessibilityServiceClient mClient;
  private int mConnectionId = -1;
  private final ArrayList<AccessibilityEvent> mEventQueue = new ArrayList();
  private int mFlags;
  private boolean mIsConnecting;
  private boolean mIsDestroyed;
  private long mLastEventTimeMillis;
  private final Handler mLocalCallbackHandler;
  private final Object mLock = new Object();
  private OnAccessibilityEventListener mOnAccessibilityEventListener;
  private HandlerThread mRemoteCallbackThread;
  private final IUiAutomationConnection mUiAutomationConnection;
  private boolean mWaitingForEventDelivery;
  
  public UiAutomation(Looper paramLooper, IUiAutomationConnection paramIUiAutomationConnection)
  {
    if (paramLooper != null)
    {
      if (paramIUiAutomationConnection != null)
      {
        mLocalCallbackHandler = new Handler(paramLooper);
        mUiAutomationConnection = paramIUiAutomationConnection;
        return;
      }
      throw new IllegalArgumentException("Connection cannot be null!");
    }
    throw new IllegalArgumentException("Looper cannot be null!");
  }
  
  private static float getDegreesForRotation(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0.0F;
    case 3: 
      return 90.0F;
    case 2: 
      return 180.0F;
    }
    return 270.0F;
  }
  
  private boolean isConnectedLocked()
  {
    boolean bool;
    if (mConnectionId != -1) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void throwIfConnectedLocked()
  {
    if (mConnectionId == -1) {
      return;
    }
    throw new IllegalStateException("UiAutomation not connected!");
  }
  
  private void throwIfNotConnectedLocked()
  {
    if (isConnectedLocked()) {
      return;
    }
    throw new IllegalStateException("UiAutomation not connected!");
  }
  
  private void warnIfBetterCommand(String paramString)
  {
    if (paramString.startsWith("pm grant ")) {
      Log.w(LOG_TAG, "UiAutomation.grantRuntimePermission() is more robust and should be used instead of 'pm grant'");
    } else if (paramString.startsWith("pm revoke ")) {
      Log.w(LOG_TAG, "UiAutomation.revokeRuntimePermission() is more robust and should be used instead of 'pm revoke'");
    }
  }
  
  public void clearWindowAnimationFrameStats()
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      try
      {
        mUiAutomationConnection.clearWindowAnimationFrameStats();
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(LOG_TAG, "Error clearing window animation frame stats!", localRemoteException);
      }
      return;
    }
  }
  
  public boolean clearWindowContentFrameStats(int paramInt)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      try
      {
        boolean bool = mUiAutomationConnection.clearWindowContentFrameStats(paramInt);
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(LOG_TAG, "Error clearing window content frame stats!", localRemoteException);
        return false;
      }
    }
  }
  
  public void connect()
  {
    connect(0);
  }
  
  public void connect(int paramInt)
  {
    synchronized (mLock)
    {
      throwIfConnectedLocked();
      if (mIsConnecting) {
        return;
      }
      mIsConnecting = true;
      Object localObject2 = new android/os/HandlerThread;
      ((HandlerThread)localObject2).<init>("UiAutomation");
      mRemoteCallbackThread = ((HandlerThread)localObject2);
      mRemoteCallbackThread.start();
      localObject2 = new android/app/UiAutomation$IAccessibilityServiceClientImpl;
      ((IAccessibilityServiceClientImpl)localObject2).<init>(this, mRemoteCallbackThread.getLooper());
      mClient = ((IAccessibilityServiceClient)localObject2);
      try
      {
        mUiAutomationConnection.connect(mClient, paramInt);
        mFlags = paramInt;
        synchronized (mLock)
        {
          long l1 = SystemClock.uptimeMillis();
          try
          {
            for (;;)
            {
              boolean bool = isConnectedLocked();
              if (bool)
              {
                mIsConnecting = false;
                return;
              }
              long l2 = SystemClock.uptimeMillis();
              l2 = 5000L - (l2 - l1);
              if (l2 <= 0L) {
                break;
              }
              try
              {
                mLock.wait(l2);
              }
              catch (InterruptedException localInterruptedException) {}
            }
            RuntimeException localRuntimeException = new java/lang/RuntimeException;
            localRuntimeException.<init>("Error while connecting UiAutomation");
            throw localRuntimeException;
          }
          finally
          {
            mIsConnecting = false;
          }
        }
        localObject5 = finally;
      }
      catch (RemoteException localRemoteException)
      {
        throw new RuntimeException("Error while connecting UiAutomation", localRemoteException);
      }
    }
  }
  
  public void destroy()
  {
    disconnect();
    mIsDestroyed = true;
  }
  
  /* Error */
  public void disconnect()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   4: astore_1
    //   5: aload_1
    //   6: monitorenter
    //   7: aload_0
    //   8: getfield 189	android/app/UiAutomation:mIsConnecting	Z
    //   11: ifne +70 -> 81
    //   14: aload_0
    //   15: invokespecial 163	android/app/UiAutomation:throwIfNotConnectedLocked	()V
    //   18: aload_0
    //   19: iconst_m1
    //   20: putfield 83	android/app/UiAutomation:mConnectionId	I
    //   23: aload_1
    //   24: monitorexit
    //   25: aload_0
    //   26: getfield 92	android/app/UiAutomation:mUiAutomationConnection	Landroid/app/IUiAutomationConnection;
    //   29: invokeinterface 238 1 0
    //   34: aload_0
    //   35: getfield 196	android/app/UiAutomation:mRemoteCallbackThread	Landroid/os/HandlerThread;
    //   38: invokevirtual 241	android/os/HandlerThread:quit	()Z
    //   41: pop
    //   42: aload_0
    //   43: aconst_null
    //   44: putfield 196	android/app/UiAutomation:mRemoteCallbackThread	Landroid/os/HandlerThread;
    //   47: return
    //   48: astore_1
    //   49: goto +17 -> 66
    //   52: astore_1
    //   53: new 225	java/lang/RuntimeException
    //   56: astore_2
    //   57: aload_2
    //   58: ldc -13
    //   60: aload_1
    //   61: invokespecial 231	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   64: aload_2
    //   65: athrow
    //   66: aload_0
    //   67: getfield 196	android/app/UiAutomation:mRemoteCallbackThread	Landroid/os/HandlerThread;
    //   70: invokevirtual 241	android/os/HandlerThread:quit	()Z
    //   73: pop
    //   74: aload_0
    //   75: aconst_null
    //   76: putfield 196	android/app/UiAutomation:mRemoteCallbackThread	Landroid/os/HandlerThread;
    //   79: aload_1
    //   80: athrow
    //   81: new 131	java/lang/IllegalStateException
    //   84: astore_2
    //   85: aload_2
    //   86: ldc -11
    //   88: invokespecial 134	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   91: aload_2
    //   92: athrow
    //   93: astore_2
    //   94: aload_1
    //   95: monitorexit
    //   96: aload_2
    //   97: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	98	0	this	UiAutomation
    //   4	20	1	localObject1	Object
    //   48	1	1	localObject2	Object
    //   52	43	1	localRemoteException	RemoteException
    //   56	36	2	localObject3	Object
    //   93	4	2	localObject4	Object
    // Exception table:
    //   from	to	target	type
    //   25	34	48	finally
    //   53	66	48	finally
    //   25	34	52	android/os/RemoteException
    //   7	25	93	finally
    //   81	93	93	finally
    //   94	96	93	finally
  }
  
  /* Error */
  public AccessibilityEvent executeAndWaitForEvent(Runnable paramRunnable, AccessibilityEventFilter paramAccessibilityEventFilter, long paramLong)
    throws TimeoutException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   4: astore 5
    //   6: aload 5
    //   8: monitorenter
    //   9: aload_0
    //   10: invokespecial 163	android/app/UiAutomation:throwIfNotConnectedLocked	()V
    //   13: aload_0
    //   14: getfield 81	android/app/UiAutomation:mEventQueue	Ljava/util/ArrayList;
    //   17: invokevirtual 252	java/util/ArrayList:clear	()V
    //   20: aload_0
    //   21: iconst_1
    //   22: putfield 113	android/app/UiAutomation:mWaitingForEventDelivery	Z
    //   25: aload 5
    //   27: monitorexit
    //   28: invokestatic 219	android/os/SystemClock:uptimeMillis	()J
    //   31: lstore 6
    //   33: aload_1
    //   34: invokeinterface 257 1 0
    //   39: new 78	java/util/ArrayList
    //   42: dup
    //   43: invokespecial 79	java/util/ArrayList:<init>	()V
    //   46: astore 5
    //   48: lload 6
    //   50: lstore 8
    //   52: invokestatic 219	android/os/SystemClock:uptimeMillis	()J
    //   55: lstore 10
    //   57: lload 6
    //   59: lstore 8
    //   61: new 78	java/util/ArrayList
    //   64: astore_1
    //   65: lload 6
    //   67: lstore 8
    //   69: aload_1
    //   70: invokespecial 79	java/util/ArrayList:<init>	()V
    //   73: lload 6
    //   75: lstore 8
    //   77: aload_0
    //   78: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   81: astore 12
    //   83: lload 6
    //   85: lstore 8
    //   87: aload 12
    //   89: monitorenter
    //   90: aload_1
    //   91: aload_0
    //   92: getfield 81	android/app/UiAutomation:mEventQueue	Ljava/util/ArrayList;
    //   95: invokeinterface 263 2 0
    //   100: pop
    //   101: aload_0
    //   102: getfield 81	android/app/UiAutomation:mEventQueue	Ljava/util/ArrayList;
    //   105: invokevirtual 252	java/util/ArrayList:clear	()V
    //   108: aload 12
    //   110: monitorexit
    //   111: lload 6
    //   113: lstore 8
    //   115: aload_1
    //   116: invokeinterface 266 1 0
    //   121: istore 13
    //   123: iload 13
    //   125: ifne +145 -> 270
    //   128: aload_1
    //   129: iconst_0
    //   130: invokeinterface 270 2 0
    //   135: checkcast 272	android/view/accessibility/AccessibilityEvent
    //   138: astore 12
    //   140: aload 12
    //   142: invokevirtual 275	android/view/accessibility/AccessibilityEvent:getEventTime	()J
    //   145: lstore 8
    //   147: lload 8
    //   149: lload 6
    //   151: lcmp
    //   152: ifge +6 -> 158
    //   155: goto -44 -> 111
    //   158: aload_2
    //   159: aload 12
    //   161: invokeinterface 279 2 0
    //   166: istore 13
    //   168: iload 13
    //   170: ifeq +79 -> 249
    //   173: aload 5
    //   175: invokeinterface 283 1 0
    //   180: istore 14
    //   182: iconst_0
    //   183: istore 15
    //   185: iload 15
    //   187: iload 14
    //   189: if_icmpge +24 -> 213
    //   192: aload 5
    //   194: iload 15
    //   196: invokeinterface 286 2 0
    //   201: checkcast 272	android/view/accessibility/AccessibilityEvent
    //   204: invokevirtual 289	android/view/accessibility/AccessibilityEvent:recycle	()V
    //   207: iinc 15 1
    //   210: goto -25 -> 185
    //   213: aload_0
    //   214: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   217: astore_1
    //   218: aload_1
    //   219: monitorenter
    //   220: aload_0
    //   221: iconst_0
    //   222: putfield 113	android/app/UiAutomation:mWaitingForEventDelivery	Z
    //   225: aload_0
    //   226: getfield 81	android/app/UiAutomation:mEventQueue	Ljava/util/ArrayList;
    //   229: invokevirtual 252	java/util/ArrayList:clear	()V
    //   232: aload_0
    //   233: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   236: invokevirtual 292	java/lang/Object:notifyAll	()V
    //   239: aload_1
    //   240: monitorexit
    //   241: aload 12
    //   243: areturn
    //   244: astore_2
    //   245: aload_1
    //   246: monitorexit
    //   247: aload_2
    //   248: athrow
    //   249: aload 5
    //   251: aload 12
    //   253: invokeinterface 296 2 0
    //   258: pop
    //   259: goto -148 -> 111
    //   262: astore_1
    //   263: goto +4 -> 267
    //   266: astore_1
    //   267: goto +143 -> 410
    //   270: invokestatic 219	android/os/SystemClock:uptimeMillis	()J
    //   273: lstore 8
    //   275: lload_3
    //   276: lload 8
    //   278: lload 10
    //   280: lsub
    //   281: lsub
    //   282: lstore 8
    //   284: lload 8
    //   286: lconst_0
    //   287: lcmp
    //   288: ifle +48 -> 336
    //   291: aload_0
    //   292: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   295: astore_1
    //   296: aload_1
    //   297: monitorenter
    //   298: aload_0
    //   299: getfield 81	android/app/UiAutomation:mEventQueue	Ljava/util/ArrayList;
    //   302: invokevirtual 297	java/util/ArrayList:isEmpty	()Z
    //   305: istore 13
    //   307: iload 13
    //   309: ifeq +17 -> 326
    //   312: aload_0
    //   313: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   316: lload 8
    //   318: invokevirtual 223	java/lang/Object:wait	(J)V
    //   321: goto +5 -> 326
    //   324: astore 12
    //   326: aload_1
    //   327: monitorexit
    //   328: goto -271 -> 57
    //   331: astore_2
    //   332: aload_1
    //   333: monitorexit
    //   334: aload_2
    //   335: athrow
    //   336: new 249	java/util/concurrent/TimeoutException
    //   339: astore_1
    //   340: new 299	java/lang/StringBuilder
    //   343: astore_2
    //   344: aload_2
    //   345: invokespecial 300	java/lang/StringBuilder:<init>	()V
    //   348: aload_2
    //   349: ldc_w 302
    //   352: invokevirtual 306	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   355: pop
    //   356: aload_2
    //   357: lload_3
    //   358: invokevirtual 309	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   361: pop
    //   362: aload_2
    //   363: ldc_w 311
    //   366: invokevirtual 306	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   369: pop
    //   370: aload_2
    //   371: aload 5
    //   373: invokevirtual 314	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   376: pop
    //   377: aload_1
    //   378: aload_2
    //   379: invokevirtual 317	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   382: invokespecial 318	java/util/concurrent/TimeoutException:<init>	(Ljava/lang/String;)V
    //   385: aload_1
    //   386: athrow
    //   387: astore_1
    //   388: goto +22 -> 410
    //   391: astore_1
    //   392: aload 12
    //   394: monitorexit
    //   395: aload_1
    //   396: athrow
    //   397: astore_1
    //   398: goto +12 -> 410
    //   401: astore_1
    //   402: goto -10 -> 392
    //   405: astore_1
    //   406: lload 8
    //   408: lstore 6
    //   410: aload 5
    //   412: invokeinterface 283 1 0
    //   417: istore 14
    //   419: iconst_0
    //   420: istore 15
    //   422: iload 15
    //   424: iload 14
    //   426: if_icmpge +24 -> 450
    //   429: aload 5
    //   431: iload 15
    //   433: invokeinterface 286 2 0
    //   438: checkcast 272	android/view/accessibility/AccessibilityEvent
    //   441: invokevirtual 289	android/view/accessibility/AccessibilityEvent:recycle	()V
    //   444: iinc 15 1
    //   447: goto -25 -> 422
    //   450: aload_0
    //   451: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   454: astore_2
    //   455: aload_2
    //   456: monitorenter
    //   457: aload_0
    //   458: iconst_0
    //   459: putfield 113	android/app/UiAutomation:mWaitingForEventDelivery	Z
    //   462: aload_0
    //   463: getfield 81	android/app/UiAutomation:mEventQueue	Ljava/util/ArrayList;
    //   466: invokevirtual 252	java/util/ArrayList:clear	()V
    //   469: aload_0
    //   470: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   473: invokevirtual 292	java/lang/Object:notifyAll	()V
    //   476: aload_2
    //   477: monitorexit
    //   478: aload_1
    //   479: athrow
    //   480: astore_1
    //   481: aload_2
    //   482: monitorexit
    //   483: aload_1
    //   484: athrow
    //   485: astore_1
    //   486: aload 5
    //   488: monitorexit
    //   489: aload_1
    //   490: athrow
    //   491: astore_1
    //   492: goto -6 -> 486
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	495	0	this	UiAutomation
    //   0	495	1	paramRunnable	Runnable
    //   0	495	2	paramAccessibilityEventFilter	AccessibilityEventFilter
    //   0	495	3	paramLong	long
    //   4	483	5	localObject1	Object
    //   31	378	6	l1	long
    //   50	357	8	l2	long
    //   55	224	10	l3	long
    //   81	171	12	localObject2	Object
    //   324	69	12	localInterruptedException	InterruptedException
    //   121	187	13	bool	boolean
    //   180	247	14	i	int
    //   183	262	15	j	int
    // Exception table:
    //   from	to	target	type
    //   220	241	244	finally
    //   245	247	244	finally
    //   158	168	262	finally
    //   249	259	262	finally
    //   128	147	266	finally
    //   312	321	324	java/lang/InterruptedException
    //   298	307	331	finally
    //   312	321	331	finally
    //   326	328	331	finally
    //   332	334	331	finally
    //   270	275	387	finally
    //   90	111	391	finally
    //   291	298	397	finally
    //   334	336	397	finally
    //   336	387	397	finally
    //   395	397	397	finally
    //   392	395	401	finally
    //   52	57	405	finally
    //   61	65	405	finally
    //   69	73	405	finally
    //   77	83	405	finally
    //   87	90	405	finally
    //   115	123	405	finally
    //   457	478	480	finally
    //   481	483	480	finally
    //   9	28	485	finally
    //   486	489	491	finally
  }
  
  /* Error */
  public android.os.ParcelFileDescriptor executeShellCommand(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: invokespecial 163	android/app/UiAutomation:throwIfNotConnectedLocked	()V
    //   11: aload_2
    //   12: monitorexit
    //   13: aload_0
    //   14: aload_1
    //   15: invokespecial 325	android/app/UiAutomation:warnIfBetterCommand	(Ljava/lang/String;)V
    //   18: aconst_null
    //   19: astore_3
    //   20: aconst_null
    //   21: astore 4
    //   23: aconst_null
    //   24: astore 5
    //   26: aconst_null
    //   27: astore 6
    //   29: aconst_null
    //   30: astore_2
    //   31: invokestatic 331	android/os/ParcelFileDescriptor:createPipe	()[Landroid/os/ParcelFileDescriptor;
    //   34: astore 7
    //   36: aload 7
    //   38: iconst_0
    //   39: aaload
    //   40: astore 8
    //   42: aload 7
    //   44: iconst_1
    //   45: aaload
    //   46: astore 7
    //   48: aload 7
    //   50: astore_2
    //   51: aload 8
    //   53: astore 4
    //   55: aload 7
    //   57: astore 5
    //   59: aload 8
    //   61: astore_3
    //   62: aload 7
    //   64: astore 6
    //   66: aload_0
    //   67: getfield 92	android/app/UiAutomation:mUiAutomationConnection	Landroid/app/IUiAutomationConnection;
    //   70: aload_1
    //   71: aload 7
    //   73: aconst_null
    //   74: invokeinterface 334 4 0
    //   79: aload 8
    //   81: astore 4
    //   83: aload 7
    //   85: astore 5
    //   87: goto +47 -> 134
    //   90: astore_1
    //   91: goto +51 -> 142
    //   94: astore_1
    //   95: aload 5
    //   97: astore_2
    //   98: getstatic 69	android/app/UiAutomation:LOG_TAG	Ljava/lang/String;
    //   101: ldc_w 336
    //   104: aload_1
    //   105: invokestatic 173	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   108: pop
    //   109: goto +25 -> 134
    //   112: astore_1
    //   113: aload 6
    //   115: astore_2
    //   116: getstatic 69	android/app/UiAutomation:LOG_TAG	Ljava/lang/String;
    //   119: ldc_w 336
    //   122: aload_1
    //   123: invokestatic 173	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   126: pop
    //   127: aload 6
    //   129: astore 5
    //   131: aload_3
    //   132: astore 4
    //   134: aload 5
    //   136: invokestatic 342	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   139: aload 4
    //   141: areturn
    //   142: aload_2
    //   143: invokestatic 342	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   146: aload_1
    //   147: athrow
    //   148: astore_1
    //   149: aload_2
    //   150: monitorexit
    //   151: aload_1
    //   152: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	153	0	this	UiAutomation
    //   0	153	1	paramString	String
    //   4	146	2	localObject1	Object
    //   19	113	3	localObject2	Object
    //   21	119	4	localObject3	Object
    //   24	111	5	localObject4	Object
    //   27	101	6	localObject5	Object
    //   34	50	7	localObject6	Object
    //   40	40	8	localObject7	Object
    // Exception table:
    //   from	to	target	type
    //   31	36	90	finally
    //   66	79	90	finally
    //   98	109	90	finally
    //   116	127	90	finally
    //   31	36	94	android/os/RemoteException
    //   66	79	94	android/os/RemoteException
    //   31	36	112	java/io/IOException
    //   66	79	112	java/io/IOException
    //   7	13	148	finally
    //   149	151	148	finally
  }
  
  /* Error */
  public android.os.ParcelFileDescriptor[] executeShellCommandRw(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 76	android/app/UiAutomation:mLock	Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_0
    //   8: invokespecial 163	android/app/UiAutomation:throwIfNotConnectedLocked	()V
    //   11: aload_2
    //   12: monitorexit
    //   13: aload_0
    //   14: aload_1
    //   15: invokespecial 325	android/app/UiAutomation:warnIfBetterCommand	(Ljava/lang/String;)V
    //   18: aconst_null
    //   19: astore_3
    //   20: aconst_null
    //   21: astore 4
    //   23: aconst_null
    //   24: astore 5
    //   26: aconst_null
    //   27: astore 6
    //   29: aconst_null
    //   30: astore 7
    //   32: aconst_null
    //   33: astore 8
    //   35: aconst_null
    //   36: astore 9
    //   38: aconst_null
    //   39: astore 10
    //   41: aconst_null
    //   42: astore 11
    //   44: aconst_null
    //   45: astore 12
    //   47: aload 10
    //   49: astore_2
    //   50: aload 8
    //   52: astore 13
    //   54: aload 12
    //   56: astore 14
    //   58: aload 9
    //   60: astore 15
    //   62: aload 11
    //   64: astore 16
    //   66: invokestatic 331	android/os/ParcelFileDescriptor:createPipe	()[Landroid/os/ParcelFileDescriptor;
    //   69: astore 17
    //   71: aload 17
    //   73: iconst_0
    //   74: aaload
    //   75: astore 18
    //   77: aload 17
    //   79: iconst_1
    //   80: aaload
    //   81: astore 17
    //   83: aload 17
    //   85: astore 7
    //   87: aload 10
    //   89: astore_2
    //   90: aload 18
    //   92: astore 4
    //   94: aload 17
    //   96: astore 5
    //   98: aload 8
    //   100: astore 13
    //   102: aload 12
    //   104: astore 14
    //   106: aload 18
    //   108: astore_3
    //   109: aload 17
    //   111: astore 6
    //   113: aload 9
    //   115: astore 15
    //   117: aload 11
    //   119: astore 16
    //   121: invokestatic 331	android/os/ParcelFileDescriptor:createPipe	()[Landroid/os/ParcelFileDescriptor;
    //   124: astore 12
    //   126: aload 12
    //   128: iconst_0
    //   129: aaload
    //   130: astore 10
    //   132: aload 12
    //   134: iconst_1
    //   135: aaload
    //   136: astore 12
    //   138: aload 17
    //   140: astore 7
    //   142: aload 10
    //   144: astore_2
    //   145: aload 18
    //   147: astore 4
    //   149: aload 17
    //   151: astore 5
    //   153: aload 10
    //   155: astore 13
    //   157: aload 12
    //   159: astore 14
    //   161: aload 18
    //   163: astore_3
    //   164: aload 17
    //   166: astore 6
    //   168: aload 10
    //   170: astore 15
    //   172: aload 12
    //   174: astore 16
    //   176: aload_0
    //   177: getfield 92	android/app/UiAutomation:mUiAutomationConnection	Landroid/app/IUiAutomationConnection;
    //   180: aload_1
    //   181: aload 17
    //   183: aload 10
    //   185: invokeinterface 334 4 0
    //   190: aload 18
    //   192: astore_3
    //   193: aload 17
    //   195: astore 5
    //   197: aload 10
    //   199: astore 13
    //   201: aload 12
    //   203: astore 14
    //   205: goto +63 -> 268
    //   208: astore_1
    //   209: goto +83 -> 292
    //   212: astore_1
    //   213: aload 5
    //   215: astore 7
    //   217: aload 13
    //   219: astore_2
    //   220: getstatic 69	android/app/UiAutomation:LOG_TAG	Ljava/lang/String;
    //   223: ldc_w 336
    //   226: aload_1
    //   227: invokestatic 173	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   230: pop
    //   231: aload 4
    //   233: astore_3
    //   234: goto +34 -> 268
    //   237: astore_1
    //   238: aload 6
    //   240: astore 7
    //   242: aload 15
    //   244: astore_2
    //   245: getstatic 69	android/app/UiAutomation:LOG_TAG	Ljava/lang/String;
    //   248: ldc_w 336
    //   251: aload_1
    //   252: invokestatic 173	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   255: pop
    //   256: aload 16
    //   258: astore 14
    //   260: aload 15
    //   262: astore 13
    //   264: aload 6
    //   266: astore 5
    //   268: aload 5
    //   270: invokestatic 342	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   273: aload 13
    //   275: invokestatic 342	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   278: iconst_2
    //   279: anewarray 327	android/os/ParcelFileDescriptor
    //   282: dup
    //   283: iconst_0
    //   284: aload_3
    //   285: aastore
    //   286: dup
    //   287: iconst_1
    //   288: aload 14
    //   290: aastore
    //   291: areturn
    //   292: aload 7
    //   294: invokestatic 342	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   297: aload_2
    //   298: invokestatic 342	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   301: aload_1
    //   302: athrow
    //   303: astore_1
    //   304: aload_2
    //   305: monitorexit
    //   306: aload_1
    //   307: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	308	0	this	UiAutomation
    //   0	308	1	paramString	String
    //   4	301	2	localObject1	Object
    //   19	266	3	localObject2	Object
    //   21	211	4	localObject3	Object
    //   24	245	5	localObject4	Object
    //   27	238	6	localObject5	Object
    //   30	263	7	localObject6	Object
    //   33	66	8	localObject7	Object
    //   36	78	9	localObject8	Object
    //   39	159	10	localParcelFileDescriptor	android.os.ParcelFileDescriptor
    //   42	76	11	localObject9	Object
    //   45	157	12	localObject10	Object
    //   52	222	13	localObject11	Object
    //   56	233	14	localObject12	Object
    //   60	201	15	localObject13	Object
    //   64	193	16	localObject14	Object
    //   69	125	17	localObject15	Object
    //   75	116	18	localObject16	Object
    // Exception table:
    //   from	to	target	type
    //   66	71	208	finally
    //   121	126	208	finally
    //   176	190	208	finally
    //   220	231	208	finally
    //   245	256	208	finally
    //   66	71	212	android/os/RemoteException
    //   121	126	212	android/os/RemoteException
    //   176	190	212	android/os/RemoteException
    //   66	71	237	java/io/IOException
    //   121	126	237	java/io/IOException
    //   176	190	237	java/io/IOException
    //   7	13	303	finally
    //   304	306	303	finally
  }
  
  public AccessibilityNodeInfo findFocus(int paramInt)
  {
    return AccessibilityInteractionClient.getInstance().findFocus(mConnectionId, -2, AccessibilityNodeInfo.ROOT_NODE_ID, paramInt);
  }
  
  public int getConnectionId()
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      int i = mConnectionId;
      return i;
    }
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public AccessibilityNodeInfo getRootInActiveWindow()
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      int i = mConnectionId;
      return AccessibilityInteractionClient.getInstance().getRootInActiveWindow(i);
    }
  }
  
  public final AccessibilityServiceInfo getServiceInfo()
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          ??? = localIAccessibilityServiceConnection.getServiceInfo();
          return ???;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w(LOG_TAG, "Error while getting AccessibilityServiceInfo", localRemoteException);
        }
      }
      return null;
    }
  }
  
  public WindowAnimationFrameStats getWindowAnimationFrameStats()
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      try
      {
        WindowAnimationFrameStats localWindowAnimationFrameStats = mUiAutomationConnection.getWindowAnimationFrameStats();
        return localWindowAnimationFrameStats;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(LOG_TAG, "Error getting window animation frame stats!", localRemoteException);
        return null;
      }
    }
  }
  
  public WindowContentFrameStats getWindowContentFrameStats(int paramInt)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      try
      {
        ??? = mUiAutomationConnection.getWindowContentFrameStats(paramInt);
        return ???;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(LOG_TAG, "Error getting window content frame stats!", localRemoteException);
        return null;
      }
    }
  }
  
  public List<AccessibilityWindowInfo> getWindows()
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      int i = mConnectionId;
      return AccessibilityInteractionClient.getInstance().getWindows(i);
    }
  }
  
  public void grantRuntimePermission(String paramString1, String paramString2)
  {
    grantRuntimePermissionAsUser(paramString1, paramString2, Process.myUserHandle());
  }
  
  @Deprecated
  public boolean grantRuntimePermission(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    grantRuntimePermissionAsUser(paramString1, paramString2, paramUserHandle);
    return true;
  }
  
  public void grantRuntimePermissionAsUser(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      try
      {
        mUiAutomationConnection.grantRuntimePermission(paramString1, paramString2, paramUserHandle.getIdentifier());
        return;
      }
      catch (Exception paramString1)
      {
        throw new SecurityException("Error granting runtime permission", paramString1);
      }
    }
  }
  
  public boolean injectInputEvent(InputEvent paramInputEvent, boolean paramBoolean)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      try
      {
        paramBoolean = mUiAutomationConnection.injectInputEvent(paramInputEvent, paramBoolean);
        return paramBoolean;
      }
      catch (RemoteException paramInputEvent)
      {
        Log.e(LOG_TAG, "Error while injecting input event!", paramInputEvent);
        return false;
      }
    }
  }
  
  public boolean isDestroyed()
  {
    return mIsDestroyed;
  }
  
  public final boolean performGlobalAction(int paramInt)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          boolean bool = localIAccessibilityServiceConnection.performGlobalAction(paramInt);
          return bool;
        }
        catch (RemoteException localRemoteException)
        {
          Log.w(LOG_TAG, "Error while calling performGlobalAction", localRemoteException);
        }
      }
      return false;
    }
  }
  
  public void revokeRuntimePermission(String paramString1, String paramString2)
  {
    revokeRuntimePermissionAsUser(paramString1, paramString2, Process.myUserHandle());
  }
  
  @Deprecated
  public boolean revokeRuntimePermission(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    revokeRuntimePermissionAsUser(paramString1, paramString2, paramUserHandle);
    return true;
  }
  
  public void revokeRuntimePermissionAsUser(String paramString1, String paramString2, UserHandle paramUserHandle)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      try
      {
        mUiAutomationConnection.revokeRuntimePermission(paramString1, paramString2, paramUserHandle.getIdentifier());
        return;
      }
      catch (Exception paramString1)
      {
        throw new SecurityException("Error granting runtime permission", paramString1);
      }
    }
  }
  
  public void setOnAccessibilityEventListener(OnAccessibilityEventListener paramOnAccessibilityEventListener)
  {
    synchronized (mLock)
    {
      mOnAccessibilityEventListener = paramOnAccessibilityEventListener;
      return;
    }
  }
  
  public boolean setRotation(int paramInt)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      switch (paramInt)
      {
      default: 
        throw new IllegalArgumentException("Invalid rotation.");
      }
      try
      {
        mUiAutomationConnection.setRotation(paramInt);
        return true;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(LOG_TAG, "Error while setting rotation!", localRemoteException);
        return false;
      }
    }
  }
  
  public void setRunAsMonkey(boolean paramBoolean)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      try
      {
        ActivityManager.getService().setUserIsMonkey(paramBoolean);
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(LOG_TAG, "Error while setting run as monkey!", localRemoteException);
      }
      return;
    }
  }
  
  public final void setServiceInfo(AccessibilityServiceInfo paramAccessibilityServiceInfo)
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      AccessibilityInteractionClient.getInstance().clearCache();
      AccessibilityInteractionClient.getInstance();
      IAccessibilityServiceConnection localIAccessibilityServiceConnection = AccessibilityInteractionClient.getConnection(mConnectionId);
      if (localIAccessibilityServiceConnection != null) {
        try
        {
          localIAccessibilityServiceConnection.setServiceInfo(paramAccessibilityServiceInfo);
        }
        catch (RemoteException paramAccessibilityServiceInfo)
        {
          Log.w(LOG_TAG, "Error while setting AccessibilityServiceInfo", paramAccessibilityServiceInfo);
        }
      }
      return;
    }
  }
  
  public Bitmap takeScreenshot()
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      ??? = DisplayManagerGlobal.getInstance().getRealDisplay(0);
      Object localObject2 = new Point();
      ((Display)???).getRealSize((Point)localObject2);
      int i = ((Display)???).getRotation();
      try
      {
        ??? = mUiAutomationConnection;
        Rect localRect = new android/graphics/Rect;
        localRect.<init>(0, 0, x, y);
        localObject2 = ((IUiAutomationConnection)???).takeScreenshot(localRect, i);
        if (localObject2 == null) {
          return null;
        }
        ((Bitmap)localObject2).setHasAlpha(false);
        return localObject2;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e(LOG_TAG, "Error while taking screnshot!", localRemoteException);
        return null;
      }
    }
  }
  
  public void waitForIdle(long paramLong1, long paramLong2)
    throws TimeoutException
  {
    synchronized (mLock)
    {
      throwIfNotConnectedLocked();
      long l1 = SystemClock.uptimeMillis();
      long l2 = l1;
      if (mLastEventTimeMillis <= 0L)
      {
        mLastEventTimeMillis = l1;
        l2 = l1;
      }
      for (;;)
      {
        l1 = SystemClock.uptimeMillis();
        if (paramLong2 - (l1 - l2) <= 0L) {
          break;
        }
        l1 = paramLong1 - (l1 - mLastEventTimeMillis);
        if (l1 <= 0L) {
          return;
        }
        try
        {
          mLock.wait(l1);
        }
        catch (InterruptedException localInterruptedException) {}
      }
      TimeoutException localTimeoutException = new java/util/concurrent/TimeoutException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("No idle state with idle timeout: ");
      localStringBuilder.append(paramLong1);
      localStringBuilder.append(" within global timeout: ");
      localStringBuilder.append(paramLong2);
      localTimeoutException.<init>(localStringBuilder.toString());
      throw localTimeoutException;
    }
  }
  
  public static abstract interface AccessibilityEventFilter
  {
    public abstract boolean accept(AccessibilityEvent paramAccessibilityEvent);
  }
  
  private class IAccessibilityServiceClientImpl
    extends AccessibilityService.IAccessibilityServiceClientWrapper
  {
    public IAccessibilityServiceClientImpl(Looper paramLooper)
    {
      super(paramLooper, new AccessibilityService.Callbacks()
      {
        public void init(int paramAnonymousInt, IBinder arg2)
        {
          synchronized (mLock)
          {
            UiAutomation.access$102(UiAutomation.IAccessibilityServiceClientImpl.this, paramAnonymousInt);
            mLock.notifyAll();
            return;
          }
        }
        
        public void onAccessibilityButtonAvailabilityChanged(boolean paramAnonymousBoolean) {}
        
        public void onAccessibilityButtonClicked() {}
        
        public void onAccessibilityEvent(AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          synchronized (mLock)
          {
            UiAutomation.access$202(UiAutomation.IAccessibilityServiceClientImpl.this, paramAnonymousAccessibilityEvent.getEventTime());
            if (mWaitingForEventDelivery) {
              mEventQueue.add(AccessibilityEvent.obtain(paramAnonymousAccessibilityEvent));
            }
            mLock.notifyAll();
            UiAutomation.OnAccessibilityEventListener localOnAccessibilityEventListener = mOnAccessibilityEventListener;
            if (localOnAccessibilityEventListener != null) {
              mLocalCallbackHandler.post(PooledLambda.obtainRunnable(_..Lambda.GnVtsLTLDH5bZdtLeTd6cfwpgcs.INSTANCE, localOnAccessibilityEventListener, AccessibilityEvent.obtain(paramAnonymousAccessibilityEvent)).recycleOnUse());
            }
            return;
          }
        }
        
        public void onFingerprintCapturingGesturesChanged(boolean paramAnonymousBoolean) {}
        
        public void onFingerprintGesture(int paramAnonymousInt) {}
        
        public boolean onGesture(int paramAnonymousInt)
        {
          return false;
        }
        
        public void onInterrupt() {}
        
        public boolean onKeyEvent(KeyEvent paramAnonymousKeyEvent)
        {
          return false;
        }
        
        public void onMagnificationChanged(Region paramAnonymousRegion, float paramAnonymousFloat1, float paramAnonymousFloat2, float paramAnonymousFloat3) {}
        
        public void onPerformGestureResult(int paramAnonymousInt, boolean paramAnonymousBoolean) {}
        
        public void onServiceConnected() {}
        
        public void onSoftKeyboardShowModeChanged(int paramAnonymousInt) {}
      });
    }
  }
  
  public static abstract interface OnAccessibilityEventListener
  {
    public abstract void onAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent);
  }
}
