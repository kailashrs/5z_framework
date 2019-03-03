package com.android.internal.os;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ProxyFileDescriptorCallback;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

public class FuseAppLoop
  implements Handler.Callback
{
  private static final int ARGS_POOL_SIZE = 50;
  private static final boolean DEBUG = Log.isLoggable("FuseAppLoop", 3);
  private static final int FUSE_FSYNC = 20;
  private static final int FUSE_GETATTR = 3;
  private static final int FUSE_LOOKUP = 1;
  private static final int FUSE_MAX_WRITE = 131072;
  private static final int FUSE_OK = 0;
  private static final int FUSE_OPEN = 14;
  private static final int FUSE_READ = 15;
  private static final int FUSE_RELEASE = 18;
  private static final int FUSE_WRITE = 16;
  private static final int MIN_INODE = 2;
  public static final int ROOT_INODE = 1;
  private static final String TAG = "FuseAppLoop";
  private static final ThreadFactory sDefaultThreadFactory = new ThreadFactory()
  {
    public Thread newThread(Runnable paramAnonymousRunnable)
    {
      return new Thread(paramAnonymousRunnable, "FuseAppLoop");
    }
  };
  @GuardedBy("mLock")
  private final LinkedList<Args> mArgsPool = new LinkedList();
  @GuardedBy("mLock")
  private final BytesMap mBytesMap = new BytesMap(null);
  @GuardedBy("mLock")
  private final SparseArray<CallbackEntry> mCallbackMap = new SparseArray();
  @GuardedBy("mLock")
  private long mInstance;
  private final Object mLock = new Object();
  private final int mMountPointId;
  @GuardedBy("mLock")
  private int mNextInode = 2;
  private final Thread mThread;
  
  public FuseAppLoop(int paramInt, ParcelFileDescriptor paramParcelFileDescriptor, ThreadFactory paramThreadFactory)
  {
    mMountPointId = paramInt;
    ThreadFactory localThreadFactory = paramThreadFactory;
    if (paramThreadFactory == null) {
      localThreadFactory = sDefaultThreadFactory;
    }
    mInstance = native_new(paramParcelFileDescriptor.detachFd());
    mThread = localThreadFactory.newThread(new _..Lambda.FuseAppLoop.e9Yru2f_btesWlxIgerkPnHibpg(this));
    mThread.start();
  }
  
  private static int checkInode(long paramLong)
  {
    Preconditions.checkArgumentInRange(paramLong, 2L, 2147483647L, "checkInode");
    return (int)paramLong;
  }
  
  @GuardedBy("mLock")
  private CallbackEntry getCallbackEntryOrThrowLocked(long paramLong)
    throws ErrnoException
  {
    CallbackEntry localCallbackEntry = (CallbackEntry)mCallbackMap.get(checkInode(paramLong));
    if (localCallbackEntry != null) {
      return localCallbackEntry;
    }
    throw new ErrnoException("getCallbackEntryOrThrowLocked", OsConstants.ENOENT);
  }
  
  private static int getError(Exception paramException)
  {
    if ((paramException instanceof ErrnoException))
    {
      int i = errno;
      if (i != OsConstants.ENOSYS) {
        return -i;
      }
    }
    return -OsConstants.EBADF;
  }
  
  private void onCommand(int paramInt1, long paramLong1, long paramLong2, long paramLong3, int paramInt2, byte[] paramArrayOfByte)
  {
    try
    {
      synchronized (mLock)
      {
        Args localArgs;
        if (mArgsPool.size() == 0)
        {
          localArgs = new com/android/internal/os/FuseAppLoop$Args;
          localArgs.<init>(null);
        }
        else
        {
          localArgs = (Args)mArgsPool.pop();
        }
        unique = paramLong1;
        inode = paramLong2;
        offset = paramLong3;
        size = paramInt2;
        data = paramArrayOfByte;
        entry = getCallbackEntryOrThrowLocked(paramLong2);
        if (!entry.handler.sendMessage(Message.obtain(entry.handler, paramInt1, 0, 0, localArgs)))
        {
          paramArrayOfByte = new android/system/ErrnoException;
          paramArrayOfByte.<init>("onCommand", OsConstants.EBADF);
          throw paramArrayOfByte;
        }
      }
    }
    catch (Exception paramArrayOfByte)
    {
      replySimpleLocked(paramLong1, getError(paramArrayOfByte));
      return;
    }
  }
  
  private byte[] onOpen(long paramLong1, long paramLong2)
  {
    try
    {
      synchronized (mLock)
      {
        Object localObject2 = getCallbackEntryOrThrowLocked(paramLong2);
        if (!opened)
        {
          if (mInstance != 0L)
          {
            native_replyOpen(mInstance, paramLong1, paramLong2);
            opened = true;
            localObject2 = mBytesMap.startUsing(((CallbackEntry)localObject2).getThreadId());
            return localObject2;
          }
        }
        else
        {
          localObject2 = new android/system/ErrnoException;
          ((ErrnoException)localObject2).<init>("onOpen", OsConstants.EMFILE);
          throw ((Throwable)localObject2);
        }
      }
    }
    catch (ErrnoException localErrnoException)
    {
      replySimpleLocked(paramLong1, getError(localErrnoException));
      return null;
    }
  }
  
  @GuardedBy("mLock")
  private void recycleLocked(Args paramArgs)
  {
    if (mArgsPool.size() < 50) {
      mArgsPool.add(paramArgs);
    }
  }
  
  @GuardedBy("mLock")
  private void replySimpleLocked(long paramLong, int paramInt)
  {
    if (mInstance != 0L) {
      native_replySimple(mInstance, paramLong, paramInt);
    }
  }
  
  public int getMountPointId()
  {
    return mMountPointId;
  }
  
  /* Error */
  public boolean handleMessage(Message paramMessage)
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 290	android/os/Message:obj	Ljava/lang/Object;
    //   4: checkcast 10	com/android/internal/os/FuseAppLoop$Args
    //   7: astore_2
    //   8: aload_2
    //   9: getfield 231	com/android/internal/os/FuseAppLoop$Args:entry	Lcom/android/internal/os/FuseAppLoop$CallbackEntry;
    //   12: astore_3
    //   13: aload_2
    //   14: getfield 216	com/android/internal/os/FuseAppLoop$Args:inode	J
    //   17: lstore 4
    //   19: aload_2
    //   20: getfield 213	com/android/internal/os/FuseAppLoop$Args:unique	J
    //   23: lstore 6
    //   25: aload_2
    //   26: getfield 221	com/android/internal/os/FuseAppLoop$Args:size	I
    //   29: istore 8
    //   31: aload_2
    //   32: getfield 219	com/android/internal/os/FuseAppLoop$Args:offset	J
    //   35: lstore 9
    //   37: aload_2
    //   38: getfield 225	com/android/internal/os/FuseAppLoop$Args:data	[B
    //   41: astore 11
    //   43: aload_1
    //   44: getfield 293	android/os/Message:what	I
    //   47: istore 12
    //   49: iload 12
    //   51: iconst_1
    //   52: if_icmpeq +543 -> 595
    //   55: iload 12
    //   57: iconst_3
    //   58: if_icmpeq +426 -> 484
    //   61: iload 12
    //   63: bipush 18
    //   65: if_icmpeq +352 -> 417
    //   68: iload 12
    //   70: bipush 20
    //   72: if_icmpeq +296 -> 368
    //   75: iload 12
    //   77: tableswitch	default:+23->100, 15:+198->275, 16:+74->151
    //   100: new 295	java/lang/IllegalArgumentException
    //   103: astore_3
    //   104: new 297	java/lang/StringBuilder
    //   107: astore 11
    //   109: aload 11
    //   111: invokespecial 298	java/lang/StringBuilder:<init>	()V
    //   114: aload 11
    //   116: ldc_w 300
    //   119: invokevirtual 304	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: pop
    //   123: aload 11
    //   125: aload_1
    //   126: getfield 293	android/os/Message:what	I
    //   129: invokevirtual 307	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   132: pop
    //   133: aload_3
    //   134: aload 11
    //   136: invokevirtual 311	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   139: invokespecial 314	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   142: aload_3
    //   143: athrow
    //   144: astore_1
    //   145: iconst_1
    //   146: istore 13
    //   148: goto +561 -> 709
    //   151: aload_3
    //   152: getfield 318	com/android/internal/os/FuseAppLoop$CallbackEntry:callback	Landroid/os/ProxyFileDescriptorCallback;
    //   155: lload 9
    //   157: iload 8
    //   159: aload 11
    //   161: invokevirtual 324	android/os/ProxyFileDescriptorCallback:onWrite	(JI[B)I
    //   164: istore 12
    //   166: aload_0
    //   167: getfield 93	com/android/internal/os/FuseAppLoop:mLock	Ljava/lang/Object;
    //   170: astore_3
    //   171: aload_3
    //   172: monitorenter
    //   173: aload_0
    //   174: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   177: lconst_0
    //   178: lcmp
    //   179: ifeq +27 -> 206
    //   182: aload_0
    //   183: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   186: lstore 9
    //   188: aload_3
    //   189: astore_1
    //   190: iconst_1
    //   191: istore 13
    //   193: aload_0
    //   194: lload 9
    //   196: lload 6
    //   198: iload 12
    //   200: invokevirtual 327	com/android/internal/os/FuseAppLoop:native_replyWrite	(JJI)V
    //   203: goto +3 -> 206
    //   206: iconst_1
    //   207: istore 14
    //   209: aload_3
    //   210: astore_1
    //   211: iload 14
    //   213: istore 13
    //   215: aload_0
    //   216: aload_2
    //   217: invokespecial 329	com/android/internal/os/FuseAppLoop:recycleLocked	(Lcom/android/internal/os/FuseAppLoop$Args;)V
    //   220: aload_3
    //   221: astore_1
    //   222: iload 14
    //   224: istore 13
    //   226: aload_3
    //   227: monitorexit
    //   228: goto +115 -> 343
    //   231: astore_1
    //   232: iconst_1
    //   233: istore 14
    //   235: aload_1
    //   236: astore 11
    //   238: aload_3
    //   239: astore_1
    //   240: iload 14
    //   242: istore 13
    //   244: aload_3
    //   245: monitorexit
    //   246: aload 11
    //   248: athrow
    //   249: astore_1
    //   250: iload 14
    //   252: istore 13
    //   254: goto +227 -> 481
    //   257: astore 11
    //   259: aload_1
    //   260: astore_3
    //   261: iload 13
    //   263: istore 14
    //   265: goto -27 -> 238
    //   268: astore_1
    //   269: iconst_1
    //   270: istore 13
    //   272: goto +437 -> 709
    //   275: iconst_1
    //   276: istore 13
    //   278: aload_3
    //   279: getfield 318	com/android/internal/os/FuseAppLoop$CallbackEntry:callback	Landroid/os/ProxyFileDescriptorCallback;
    //   282: lload 9
    //   284: iload 8
    //   286: aload 11
    //   288: invokevirtual 332	android/os/ProxyFileDescriptorCallback:onRead	(JI[B)I
    //   291: istore 12
    //   293: aload_0
    //   294: getfield 93	com/android/internal/os/FuseAppLoop:mLock	Ljava/lang/Object;
    //   297: astore_3
    //   298: aload_3
    //   299: monitorenter
    //   300: aload_0
    //   301: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   304: lconst_0
    //   305: lcmp
    //   306: ifeq +26 -> 332
    //   309: aload_0
    //   310: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   313: lstore 9
    //   315: aload_3
    //   316: astore_1
    //   317: aload_0
    //   318: lload 9
    //   320: lload 6
    //   322: iload 12
    //   324: aload 11
    //   326: invokevirtual 336	com/android/internal/os/FuseAppLoop:native_replyRead	(JJI[B)V
    //   329: goto +3 -> 332
    //   332: aload_3
    //   333: astore_1
    //   334: aload_0
    //   335: aload_2
    //   336: invokespecial 329	com/android/internal/os/FuseAppLoop:recycleLocked	(Lcom/android/internal/os/FuseAppLoop$Args;)V
    //   339: aload_3
    //   340: astore_1
    //   341: aload_3
    //   342: monitorexit
    //   343: goto +327 -> 670
    //   346: astore_1
    //   347: aload_1
    //   348: astore 11
    //   350: aload_3
    //   351: astore_1
    //   352: aload_3
    //   353: monitorexit
    //   354: aload 11
    //   356: athrow
    //   357: astore 11
    //   359: aload_1
    //   360: astore_3
    //   361: goto -11 -> 350
    //   364: astore_1
    //   365: goto +344 -> 709
    //   368: aload_3
    //   369: getfield 318	com/android/internal/os/FuseAppLoop$CallbackEntry:callback	Landroid/os/ProxyFileDescriptorCallback;
    //   372: invokevirtual 339	android/os/ProxyFileDescriptorCallback:onFsync	()V
    //   375: aload_0
    //   376: getfield 93	com/android/internal/os/FuseAppLoop:mLock	Ljava/lang/Object;
    //   379: astore_3
    //   380: aload_3
    //   381: monitorenter
    //   382: aload_0
    //   383: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   386: lconst_0
    //   387: lcmp
    //   388: ifeq +14 -> 402
    //   391: aload_0
    //   392: aload_0
    //   393: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   396: lload 6
    //   398: iconst_0
    //   399: invokevirtual 285	com/android/internal/os/FuseAppLoop:native_replySimple	(JJI)V
    //   402: aload_0
    //   403: aload_2
    //   404: invokespecial 329	com/android/internal/os/FuseAppLoop:recycleLocked	(Lcom/android/internal/os/FuseAppLoop$Args;)V
    //   407: aload_3
    //   408: monitorexit
    //   409: goto -66 -> 343
    //   412: astore_1
    //   413: aload_3
    //   414: monitorexit
    //   415: aload_1
    //   416: athrow
    //   417: aload_3
    //   418: getfield 318	com/android/internal/os/FuseAppLoop$CallbackEntry:callback	Landroid/os/ProxyFileDescriptorCallback;
    //   421: invokevirtual 342	android/os/ProxyFileDescriptorCallback:onRelease	()V
    //   424: aload_0
    //   425: getfield 93	com/android/internal/os/FuseAppLoop:mLock	Ljava/lang/Object;
    //   428: astore_1
    //   429: aload_1
    //   430: monitorenter
    //   431: aload_0
    //   432: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   435: lconst_0
    //   436: lcmp
    //   437: ifeq +14 -> 451
    //   440: aload_0
    //   441: aload_0
    //   442: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   445: lload 6
    //   447: iconst_0
    //   448: invokevirtual 285	com/android/internal/os/FuseAppLoop:native_replySimple	(JJI)V
    //   451: aload_0
    //   452: getfield 103	com/android/internal/os/FuseAppLoop:mBytesMap	Lcom/android/internal/os/FuseAppLoop$BytesMap;
    //   455: aload_3
    //   456: invokevirtual 267	com/android/internal/os/FuseAppLoop$CallbackEntry:getThreadId	()J
    //   459: invokevirtual 345	com/android/internal/os/FuseAppLoop$BytesMap:stopUsing	(J)V
    //   462: aload_0
    //   463: aload_2
    //   464: invokespecial 329	com/android/internal/os/FuseAppLoop:recycleLocked	(Lcom/android/internal/os/FuseAppLoop$Args;)V
    //   467: aload_1
    //   468: monitorexit
    //   469: goto -126 -> 343
    //   472: astore_3
    //   473: aload_1
    //   474: monitorexit
    //   475: aload_3
    //   476: athrow
    //   477: iconst_1
    //   478: istore 13
    //   480: astore_1
    //   481: goto +228 -> 709
    //   484: iconst_1
    //   485: istore 13
    //   487: aload_3
    //   488: getfield 318	com/android/internal/os/FuseAppLoop$CallbackEntry:callback	Landroid/os/ProxyFileDescriptorCallback;
    //   491: invokevirtual 348	android/os/ProxyFileDescriptorCallback:onGetSize	()J
    //   494: lstore 15
    //   496: aload_0
    //   497: getfield 93	com/android/internal/os/FuseAppLoop:mLock	Ljava/lang/Object;
    //   500: astore_3
    //   501: aload_3
    //   502: monitorenter
    //   503: aload_0
    //   504: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   507: lconst_0
    //   508: lcmp
    //   509: ifeq +30 -> 539
    //   512: aload_0
    //   513: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   516: lstore 17
    //   518: aload_3
    //   519: astore_1
    //   520: lload 6
    //   522: lstore 9
    //   524: aload_0
    //   525: lload 17
    //   527: lload 6
    //   529: lload 4
    //   531: lload 15
    //   533: invokevirtual 352	com/android/internal/os/FuseAppLoop:native_replyGetAttr	(JJJJ)V
    //   536: goto +3 -> 539
    //   539: aload_3
    //   540: astore_1
    //   541: lload 6
    //   543: lstore 9
    //   545: aload_0
    //   546: aload_2
    //   547: invokespecial 329	com/android/internal/os/FuseAppLoop:recycleLocked	(Lcom/android/internal/os/FuseAppLoop$Args;)V
    //   550: aload_3
    //   551: astore_1
    //   552: lload 6
    //   554: lstore 9
    //   556: aload_3
    //   557: monitorexit
    //   558: goto +112 -> 670
    //   561: astore_1
    //   562: aload_1
    //   563: astore 11
    //   565: aload_3
    //   566: astore_1
    //   567: lload 6
    //   569: lstore 9
    //   571: aload_3
    //   572: monitorexit
    //   573: aload 11
    //   575: athrow
    //   576: astore 11
    //   578: aload_1
    //   579: astore_3
    //   580: lload 9
    //   582: lstore 6
    //   584: goto -19 -> 565
    //   587: astore_1
    //   588: goto +121 -> 709
    //   591: astore_1
    //   592: goto +117 -> 709
    //   595: lload 6
    //   597: lstore 9
    //   599: lload 9
    //   601: lstore 6
    //   603: aload_3
    //   604: getfield 318	com/android/internal/os/FuseAppLoop$CallbackEntry:callback	Landroid/os/ProxyFileDescriptorCallback;
    //   607: invokevirtual 348	android/os/ProxyFileDescriptorCallback:onGetSize	()J
    //   610: lstore 17
    //   612: lload 9
    //   614: lstore 6
    //   616: aload_0
    //   617: getfield 93	com/android/internal/os/FuseAppLoop:mLock	Ljava/lang/Object;
    //   620: astore_3
    //   621: lload 9
    //   623: lstore 6
    //   625: aload_3
    //   626: monitorenter
    //   627: aload_0
    //   628: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   631: lconst_0
    //   632: lcmp
    //   633: ifeq +26 -> 659
    //   636: aload_0
    //   637: getfield 124	com/android/internal/os/FuseAppLoop:mInstance	J
    //   640: lstore 6
    //   642: aload_3
    //   643: astore_1
    //   644: aload_0
    //   645: lload 6
    //   647: lload 9
    //   649: lload 4
    //   651: lload 17
    //   653: invokevirtual 355	com/android/internal/os/FuseAppLoop:native_replyLookup	(JJJJ)V
    //   656: goto +3 -> 659
    //   659: aload_3
    //   660: astore_1
    //   661: aload_0
    //   662: aload_2
    //   663: invokespecial 329	com/android/internal/os/FuseAppLoop:recycleLocked	(Lcom/android/internal/os/FuseAppLoop$Args;)V
    //   666: aload_3
    //   667: astore_1
    //   668: aload_3
    //   669: monitorexit
    //   670: iconst_1
    //   671: istore 13
    //   673: goto +74 -> 747
    //   676: astore_1
    //   677: aload_1
    //   678: astore 11
    //   680: aload_3
    //   681: astore_1
    //   682: aload_3
    //   683: monitorexit
    //   684: lload 9
    //   686: lstore 6
    //   688: aload 11
    //   690: athrow
    //   691: astore 11
    //   693: aload_1
    //   694: astore_3
    //   695: goto -15 -> 680
    //   698: iconst_1
    //   699: istore 13
    //   701: astore_1
    //   702: goto +7 -> 709
    //   705: astore_1
    //   706: iconst_1
    //   707: istore 13
    //   709: aload_0
    //   710: getfield 93	com/android/internal/os/FuseAppLoop:mLock	Ljava/lang/Object;
    //   713: astore_3
    //   714: aload_3
    //   715: monitorenter
    //   716: ldc 53
    //   718: ldc_w 357
    //   721: aload_1
    //   722: invokestatic 361	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   725: pop
    //   726: aload_1
    //   727: invokestatic 250	com/android/internal/os/FuseAppLoop:getError	(Ljava/lang/Exception;)I
    //   730: istore 12
    //   732: aload_0
    //   733: lload 6
    //   735: iload 12
    //   737: invokespecial 254	com/android/internal/os/FuseAppLoop:replySimpleLocked	(JI)V
    //   740: aload_0
    //   741: aload_2
    //   742: invokespecial 329	com/android/internal/os/FuseAppLoop:recycleLocked	(Lcom/android/internal/os/FuseAppLoop$Args;)V
    //   745: aload_3
    //   746: monitorexit
    //   747: iload 13
    //   749: ireturn
    //   750: astore_1
    //   751: aload_3
    //   752: monitorexit
    //   753: aload_1
    //   754: athrow
    //   755: astore_1
    //   756: goto -5 -> 751
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	759	0	this	FuseAppLoop
    //   0	759	1	paramMessage	Message
    //   7	735	2	localArgs	Args
    //   12	444	3	localObject1	Object
    //   472	16	3	localObject2	Object
    //   17	633	4	l1	long
    //   23	711	6	l2	long
    //   29	256	8	i	int
    //   35	650	9	l3	long
    //   41	206	11	localObject4	Object
    //   257	68	11	arrayOfByte	byte[]
    //   348	7	11	localMessage1	Message
    //   357	1	11	localObject5	Object
    //   563	11	11	localMessage2	Message
    //   576	1	11	localObject6	Object
    //   678	11	11	localMessage3	Message
    //   691	1	11	localObject7	Object
    //   47	689	12	j	int
    //   146	602	13	bool1	boolean
    //   207	57	14	bool2	boolean
    //   494	38	15	l4	long
    //   516	136	17	l5	long
    // Exception table:
    //   from	to	target	type
    //   100	144	144	java/lang/Exception
    //   173	188	231	finally
    //   246	249	249	java/lang/Exception
    //   193	203	257	finally
    //   215	220	257	finally
    //   226	228	257	finally
    //   244	246	257	finally
    //   151	173	268	java/lang/Exception
    //   300	315	346	finally
    //   317	329	357	finally
    //   334	339	357	finally
    //   341	343	357	finally
    //   352	354	357	finally
    //   278	300	364	java/lang/Exception
    //   382	402	412	finally
    //   402	409	412	finally
    //   413	415	412	finally
    //   431	451	472	finally
    //   451	469	472	finally
    //   473	475	472	finally
    //   354	357	477	java/lang/Exception
    //   368	382	477	java/lang/Exception
    //   415	417	477	java/lang/Exception
    //   417	431	477	java/lang/Exception
    //   475	477	477	java/lang/Exception
    //   503	518	561	finally
    //   524	536	576	finally
    //   545	550	576	finally
    //   556	558	576	finally
    //   571	573	576	finally
    //   496	503	587	java/lang/Exception
    //   487	496	591	java/lang/Exception
    //   627	642	676	finally
    //   644	656	691	finally
    //   661	666	691	finally
    //   668	670	691	finally
    //   682	684	691	finally
    //   573	576	698	java/lang/Exception
    //   603	612	698	java/lang/Exception
    //   616	621	698	java/lang/Exception
    //   625	627	698	java/lang/Exception
    //   688	691	698	java/lang/Exception
    //   43	49	705	java/lang/Exception
    //   716	732	750	finally
    //   732	747	755	finally
    //   751	753	755	finally
  }
  
  native void native_delete(long paramLong);
  
  native long native_new(int paramInt);
  
  native void native_replyGetAttr(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  native void native_replyLookup(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  native void native_replyOpen(long paramLong1, long paramLong2, long paramLong3);
  
  native void native_replyRead(long paramLong1, long paramLong2, int paramInt, byte[] paramArrayOfByte);
  
  native void native_replySimple(long paramLong1, long paramLong2, int paramInt);
  
  native void native_replyWrite(long paramLong1, long paramLong2, int paramInt);
  
  native void native_start(long paramLong);
  
  public int registerCallback(ProxyFileDescriptorCallback paramProxyFileDescriptorCallback, Handler paramHandler)
    throws FuseUnavailableMountException
  {
    synchronized (mLock)
    {
      Preconditions.checkNotNull(paramProxyFileDescriptorCallback);
      Preconditions.checkNotNull(paramHandler);
      int i = mCallbackMap.size();
      boolean bool1 = false;
      if (i < 2147483645) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Preconditions.checkState(bool2, "Too many opened files.");
      boolean bool2 = bool1;
      if (Thread.currentThread().getId() != paramHandler.getLooper().getThread().getId()) {
        bool2 = true;
      }
      Preconditions.checkArgument(bool2, "Handler must be different from the current thread");
      if (mInstance != 0L)
      {
        do
        {
          i = mNextInode;
          mNextInode += 1;
          if (mNextInode < 0) {
            mNextInode = 2;
          }
        } while (mCallbackMap.get(i) != null);
        SparseArray localSparseArray = mCallbackMap;
        CallbackEntry localCallbackEntry = new com/android/internal/os/FuseAppLoop$CallbackEntry;
        Handler localHandler = new android/os/Handler;
        localHandler.<init>(paramHandler.getLooper(), this);
        localCallbackEntry.<init>(paramProxyFileDescriptorCallback, localHandler);
        localSparseArray.put(i, localCallbackEntry);
        return i;
      }
      paramProxyFileDescriptorCallback = new com/android/internal/os/FuseUnavailableMountException;
      paramProxyFileDescriptorCallback.<init>(mMountPointId);
      throw paramProxyFileDescriptorCallback;
    }
  }
  
  public void unregisterCallback(int paramInt)
  {
    synchronized (mLock)
    {
      mCallbackMap.remove(paramInt);
      return;
    }
  }
  
  private static class Args
  {
    byte[] data;
    FuseAppLoop.CallbackEntry entry;
    long inode;
    long offset;
    int size;
    long unique;
    
    private Args() {}
  }
  
  private static class BytesMap
  {
    final Map<Long, FuseAppLoop.BytesMapEntry> mEntries = new HashMap();
    
    private BytesMap() {}
    
    void clear()
    {
      mEntries.clear();
    }
    
    byte[] startUsing(long paramLong)
    {
      FuseAppLoop.BytesMapEntry localBytesMapEntry1 = (FuseAppLoop.BytesMapEntry)mEntries.get(Long.valueOf(paramLong));
      FuseAppLoop.BytesMapEntry localBytesMapEntry2 = localBytesMapEntry1;
      if (localBytesMapEntry1 == null)
      {
        localBytesMapEntry2 = new FuseAppLoop.BytesMapEntry(null);
        mEntries.put(Long.valueOf(paramLong), localBytesMapEntry2);
      }
      counter += 1;
      return bytes;
    }
    
    void stopUsing(long paramLong)
    {
      FuseAppLoop.BytesMapEntry localBytesMapEntry = (FuseAppLoop.BytesMapEntry)mEntries.get(Long.valueOf(paramLong));
      Preconditions.checkNotNull(localBytesMapEntry);
      counter -= 1;
      if (counter <= 0) {
        mEntries.remove(Long.valueOf(paramLong));
      }
    }
  }
  
  private static class BytesMapEntry
  {
    byte[] bytes = new byte[131072];
    int counter = 0;
    
    private BytesMapEntry() {}
  }
  
  private static class CallbackEntry
  {
    final ProxyFileDescriptorCallback callback;
    final Handler handler;
    boolean opened;
    
    CallbackEntry(ProxyFileDescriptorCallback paramProxyFileDescriptorCallback, Handler paramHandler)
    {
      callback = ((ProxyFileDescriptorCallback)Preconditions.checkNotNull(paramProxyFileDescriptorCallback));
      handler = ((Handler)Preconditions.checkNotNull(paramHandler));
    }
    
    long getThreadId()
    {
      return handler.getLooper().getThread().getId();
    }
  }
  
  public static class UnmountedException
    extends Exception
  {
    public UnmountedException() {}
  }
}
