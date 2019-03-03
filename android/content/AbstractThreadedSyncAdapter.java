package android.content;

import android.accounts.Account;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractThreadedSyncAdapter
{
  private static final boolean ENABLE_LOG;
  @Deprecated
  public static final int LOG_SYNC_DETAILS = 2743;
  private static final String TAG = "SyncAdapter";
  private boolean mAllowParallelSyncs;
  private final boolean mAutoInitialize;
  private final Context mContext;
  private final ISyncAdapterImpl mISyncAdapterImpl;
  private final AtomicInteger mNumSyncStarts;
  private final Object mSyncThreadLock = new Object();
  private final HashMap<Account, SyncThread> mSyncThreads = new HashMap();
  
  static
  {
    boolean bool;
    if ((Build.IS_DEBUGGABLE) && (Log.isLoggable("SyncAdapter", 3))) {
      bool = true;
    } else {
      bool = false;
    }
    ENABLE_LOG = bool;
  }
  
  public AbstractThreadedSyncAdapter(Context paramContext, boolean paramBoolean)
  {
    this(paramContext, paramBoolean, false);
  }
  
  public AbstractThreadedSyncAdapter(Context paramContext, boolean paramBoolean1, boolean paramBoolean2)
  {
    mContext = paramContext;
    mISyncAdapterImpl = new ISyncAdapterImpl(null);
    mNumSyncStarts = new AtomicInteger(0);
    mAutoInitialize = paramBoolean1;
    mAllowParallelSyncs = paramBoolean2;
  }
  
  private void handleOnUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback paramISyncAdapterUnsyncableAccountCallback)
  {
    boolean bool;
    try
    {
      bool = onUnsyncableAccount();
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.e("SyncAdapter", "Exception while calling onUnsyncableAccount, assuming 'true'", localRuntimeException);
      bool = true;
    }
    try
    {
      paramISyncAdapterUnsyncableAccountCallback.onUnsyncableAccountDone(bool);
    }
    catch (RemoteException paramISyncAdapterUnsyncableAccountCallback)
    {
      Log.e("SyncAdapter", "Could not report result of onUnsyncableAccount", paramISyncAdapterUnsyncableAccountCallback);
    }
  }
  
  private Account toSyncKey(Account paramAccount)
  {
    if (mAllowParallelSyncs) {
      return paramAccount;
    }
    return null;
  }
  
  public Context getContext()
  {
    return mContext;
  }
  
  public final IBinder getSyncAdapterBinder()
  {
    return mISyncAdapterImpl.asBinder();
  }
  
  public abstract void onPerformSync(Account paramAccount, Bundle paramBundle, String paramString, ContentProviderClient paramContentProviderClient, SyncResult paramSyncResult);
  
  public void onSecurityException(Account paramAccount, Bundle paramBundle, String paramString, SyncResult paramSyncResult) {}
  
  public void onSyncCanceled()
  {
    synchronized (mSyncThreadLock)
    {
      SyncThread localSyncThread = (SyncThread)mSyncThreads.get(null);
      if (localSyncThread != null) {
        localSyncThread.interrupt();
      }
      return;
    }
  }
  
  public void onSyncCanceled(Thread paramThread)
  {
    paramThread.interrupt();
  }
  
  public boolean onUnsyncableAccount()
  {
    return true;
  }
  
  private class ISyncAdapterImpl
    extends ISyncAdapter.Stub
  {
    private ISyncAdapterImpl() {}
    
    /* Error */
    public void cancelSync(ISyncContext paramISyncContext)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_2
      //   2: aload_0
      //   3: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   6: invokestatic 36	android/content/AbstractThreadedSyncAdapter:access$300	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/lang/Object;
      //   9: astore_3
      //   10: aload_3
      //   11: monitorenter
      //   12: aload_0
      //   13: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   16: invokestatic 40	android/content/AbstractThreadedSyncAdapter:access$400	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/util/HashMap;
      //   19: invokevirtual 46	java/util/HashMap:values	()Ljava/util/Collection;
      //   22: invokeinterface 52 1 0
      //   27: astore 4
      //   29: aload_2
      //   30: astore 5
      //   32: aload 4
      //   34: invokeinterface 58 1 0
      //   39: ifeq +38 -> 77
      //   42: aload 4
      //   44: invokeinterface 62 1 0
      //   49: checkcast 64	android/content/AbstractThreadedSyncAdapter$SyncThread
      //   52: astore 5
      //   54: aload 5
      //   56: invokestatic 68	android/content/AbstractThreadedSyncAdapter$SyncThread:access$800	(Landroid/content/AbstractThreadedSyncAdapter$SyncThread;)Landroid/content/SyncContext;
      //   59: invokevirtual 74	android/content/SyncContext:getSyncContextBinder	()Landroid/os/IBinder;
      //   62: aload_1
      //   63: invokeinterface 79 1 0
      //   68: if_acmpne +6 -> 74
      //   71: goto +6 -> 77
      //   74: goto -45 -> 29
      //   77: aload_3
      //   78: monitorexit
      //   79: aload 5
      //   81: ifnull +93 -> 174
      //   84: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   87: ifeq +55 -> 142
      //   90: new 84	java/lang/StringBuilder
      //   93: astore_1
      //   94: aload_1
      //   95: invokespecial 85	java/lang/StringBuilder:<init>	()V
      //   98: aload_1
      //   99: ldc 87
      //   101: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   104: pop
      //   105: aload_1
      //   106: aload 5
      //   108: invokestatic 95	android/content/AbstractThreadedSyncAdapter$SyncThread:access$900	(Landroid/content/AbstractThreadedSyncAdapter$SyncThread;)Ljava/lang/String;
      //   111: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   114: pop
      //   115: aload_1
      //   116: ldc 97
      //   118: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   121: pop
      //   122: aload_1
      //   123: aload 5
      //   125: invokestatic 101	android/content/AbstractThreadedSyncAdapter$SyncThread:access$1000	(Landroid/content/AbstractThreadedSyncAdapter$SyncThread;)Landroid/accounts/Account;
      //   128: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   131: pop
      //   132: ldc 106
      //   134: aload_1
      //   135: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   138: invokestatic 116	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   141: pop
      //   142: aload_0
      //   143: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   146: invokestatic 120	android/content/AbstractThreadedSyncAdapter:access$1100	(Landroid/content/AbstractThreadedSyncAdapter;)Z
      //   149: ifeq +15 -> 164
      //   152: aload_0
      //   153: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   156: aload 5
      //   158: invokevirtual 124	android/content/AbstractThreadedSyncAdapter:onSyncCanceled	(Ljava/lang/Thread;)V
      //   161: goto +27 -> 188
      //   164: aload_0
      //   165: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   168: invokevirtual 126	android/content/AbstractThreadedSyncAdapter:onSyncCanceled	()V
      //   171: goto +17 -> 188
      //   174: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   177: ifeq +11 -> 188
      //   180: ldc 106
      //   182: ldc -128
      //   184: invokestatic 131	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   187: pop
      //   188: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   191: ifeq +11 -> 202
      //   194: ldc 106
      //   196: ldc -123
      //   198: invokestatic 116	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   201: pop
      //   202: return
      //   203: astore_1
      //   204: aload_3
      //   205: monitorexit
      //   206: aload_1
      //   207: athrow
      //   208: astore_1
      //   209: goto +21 -> 230
      //   212: astore_1
      //   213: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   216: ifeq +12 -> 228
      //   219: ldc 106
      //   221: ldc -121
      //   223: aload_1
      //   224: invokestatic 138	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   227: pop
      //   228: aload_1
      //   229: athrow
      //   230: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   233: ifeq +11 -> 244
      //   236: ldc 106
      //   238: ldc -123
      //   240: invokestatic 116	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   243: pop
      //   244: aload_1
      //   245: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	246	0	this	ISyncAdapterImpl
      //   0	246	1	paramISyncContext	ISyncContext
      //   1	29	2	localObject1	Object
      //   27	16	4	localIterator	java.util.Iterator
      //   30	127	5	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   12	29	203	finally
      //   32	71	203	finally
      //   77	79	203	finally
      //   204	206	203	finally
      //   2	12	208	finally
      //   84	142	208	finally
      //   142	161	208	finally
      //   164	171	208	finally
      //   174	188	208	finally
      //   206	208	208	finally
      //   213	228	208	finally
      //   228	230	208	finally
      //   2	12	212	java/lang/RuntimeException
      //   2	12	212	java/lang/Error
      //   84	142	212	java/lang/RuntimeException
      //   84	142	212	java/lang/Error
      //   142	161	212	java/lang/RuntimeException
      //   142	161	212	java/lang/Error
      //   164	171	212	java/lang/RuntimeException
      //   164	171	212	java/lang/Error
      //   174	188	212	java/lang/RuntimeException
      //   174	188	212	java/lang/Error
      //   206	208	212	java/lang/RuntimeException
      //   206	208	212	java/lang/Error
    }
    
    public void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback paramISyncAdapterUnsyncableAccountCallback)
    {
      Handler.getMain().sendMessage(PooledLambda.obtainMessage(_..Lambda.AbstractThreadedSyncAdapter.ISyncAdapterImpl.L6ZtOCe8gjKwJj0908ytPlrD8Rc.INSTANCE, AbstractThreadedSyncAdapter.this, paramISyncAdapterUnsyncableAccountCallback));
    }
    
    /* Error */
    public void startSync(ISyncContext paramISyncContext, String paramString, Account paramAccount, Bundle paramBundle)
    {
      // Byte code:
      //   0: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   3: ifeq +80 -> 83
      //   6: aload 4
      //   8: ifnull +9 -> 17
      //   11: aload 4
      //   13: invokevirtual 170	android/os/Bundle:size	()I
      //   16: pop
      //   17: new 84	java/lang/StringBuilder
      //   20: dup
      //   21: invokespecial 85	java/lang/StringBuilder:<init>	()V
      //   24: astore 5
      //   26: aload 5
      //   28: ldc -84
      //   30: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   33: pop
      //   34: aload 5
      //   36: aload_2
      //   37: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   40: pop
      //   41: aload 5
      //   43: ldc 97
      //   45: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   48: pop
      //   49: aload 5
      //   51: aload_3
      //   52: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   55: pop
      //   56: aload 5
      //   58: ldc 97
      //   60: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   63: pop
      //   64: aload 5
      //   66: aload 4
      //   68: invokevirtual 104	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   71: pop
      //   72: ldc 106
      //   74: aload 5
      //   76: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   79: invokestatic 116	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   82: pop
      //   83: new 70	android/content/SyncContext
      //   86: astore 5
      //   88: aload 5
      //   90: aload_1
      //   91: invokespecial 174	android/content/SyncContext:<init>	(Landroid/content/ISyncContext;)V
      //   94: aload_0
      //   95: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   98: aload_3
      //   99: invokestatic 178	android/content/AbstractThreadedSyncAdapter:access$200	(Landroid/content/AbstractThreadedSyncAdapter;Landroid/accounts/Account;)Landroid/accounts/Account;
      //   102: astore 6
      //   104: aload_0
      //   105: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   108: invokestatic 36	android/content/AbstractThreadedSyncAdapter:access$300	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/lang/Object;
      //   111: astore_1
      //   112: aload_1
      //   113: monitorenter
      //   114: aload_0
      //   115: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   118: invokestatic 40	android/content/AbstractThreadedSyncAdapter:access$400	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/util/HashMap;
      //   121: aload 6
      //   123: invokevirtual 182	java/util/HashMap:containsKey	(Ljava/lang/Object;)Z
      //   126: istore 7
      //   128: iconst_1
      //   129: istore 8
      //   131: iload 7
      //   133: ifne +185 -> 318
      //   136: aload_0
      //   137: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   140: invokestatic 185	android/content/AbstractThreadedSyncAdapter:access$500	(Landroid/content/AbstractThreadedSyncAdapter;)Z
      //   143: ifeq +85 -> 228
      //   146: aload 4
      //   148: ifnull +80 -> 228
      //   151: aload 4
      //   153: ldc -69
      //   155: iconst_0
      //   156: invokevirtual 191	android/os/Bundle:getBoolean	(Ljava/lang/String;Z)Z
      //   159: istore 7
      //   161: iload 7
      //   163: ifeq +65 -> 228
      //   166: aload_3
      //   167: aload_2
      //   168: invokestatic 197	android/content/ContentResolver:getIsSyncable	(Landroid/accounts/Account;Ljava/lang/String;)I
      //   171: ifge +9 -> 180
      //   174: aload_3
      //   175: aload_2
      //   176: iconst_1
      //   177: invokestatic 201	android/content/ContentResolver:setIsSyncable	(Landroid/accounts/Account;Ljava/lang/String;I)V
      //   180: new 203	android/content/SyncResult
      //   183: astore_2
      //   184: aload_2
      //   185: invokespecial 204	android/content/SyncResult:<init>	()V
      //   188: aload 5
      //   190: aload_2
      //   191: invokevirtual 208	android/content/SyncContext:onFinished	(Landroid/content/SyncResult;)V
      //   194: aload_1
      //   195: monitorexit
      //   196: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   199: ifeq +11 -> 210
      //   202: ldc 106
      //   204: ldc -46
      //   206: invokestatic 116	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   209: pop
      //   210: return
      //   211: astore_3
      //   212: new 203	android/content/SyncResult
      //   215: astore_2
      //   216: aload_2
      //   217: invokespecial 204	android/content/SyncResult:<init>	()V
      //   220: aload 5
      //   222: aload_2
      //   223: invokevirtual 208	android/content/SyncContext:onFinished	(Landroid/content/SyncResult;)V
      //   226: aload_3
      //   227: athrow
      //   228: new 64	android/content/AbstractThreadedSyncAdapter$SyncThread
      //   231: astore 9
      //   233: aload_0
      //   234: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   237: astore 10
      //   239: new 84	java/lang/StringBuilder
      //   242: astore 11
      //   244: aload 11
      //   246: invokespecial 85	java/lang/StringBuilder:<init>	()V
      //   249: aload 11
      //   251: ldc -44
      //   253: invokevirtual 91	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   256: pop
      //   257: aload 11
      //   259: aload_0
      //   260: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   263: invokestatic 216	android/content/AbstractThreadedSyncAdapter:access$600	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/util/concurrent/atomic/AtomicInteger;
      //   266: invokevirtual 221	java/util/concurrent/atomic/AtomicInteger:incrementAndGet	()I
      //   269: invokevirtual 224	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   272: pop
      //   273: aload 9
      //   275: aload 10
      //   277: aload 11
      //   279: invokevirtual 110	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   282: aload 5
      //   284: aload_2
      //   285: aload_3
      //   286: aload 4
      //   288: aconst_null
      //   289: invokespecial 227	android/content/AbstractThreadedSyncAdapter$SyncThread:<init>	(Landroid/content/AbstractThreadedSyncAdapter;Ljava/lang/String;Landroid/content/SyncContext;Ljava/lang/String;Landroid/accounts/Account;Landroid/os/Bundle;Landroid/content/AbstractThreadedSyncAdapter$1;)V
      //   292: aload_0
      //   293: getfield 13	android/content/AbstractThreadedSyncAdapter$ISyncAdapterImpl:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   296: invokestatic 40	android/content/AbstractThreadedSyncAdapter:access$400	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/util/HashMap;
      //   299: aload 6
      //   301: aload 9
      //   303: invokevirtual 231	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   306: pop
      //   307: aload 9
      //   309: invokevirtual 234	android/content/AbstractThreadedSyncAdapter$SyncThread:start	()V
      //   312: iconst_0
      //   313: istore 8
      //   315: goto +17 -> 332
      //   318: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   321: ifeq +11 -> 332
      //   324: ldc 106
      //   326: ldc -20
      //   328: invokestatic 116	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   331: pop
      //   332: aload_1
      //   333: monitorexit
      //   334: iload 8
      //   336: ifeq +11 -> 347
      //   339: aload 5
      //   341: getstatic 240	android/content/SyncResult:ALREADY_IN_PROGRESS	Landroid/content/SyncResult;
      //   344: invokevirtual 208	android/content/SyncContext:onFinished	(Landroid/content/SyncResult;)V
      //   347: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   350: ifeq +11 -> 361
      //   353: ldc 106
      //   355: ldc -46
      //   357: invokestatic 116	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   360: pop
      //   361: return
      //   362: astore_2
      //   363: aload_1
      //   364: monitorexit
      //   365: aload_2
      //   366: athrow
      //   367: astore_1
      //   368: goto +29 -> 397
      //   371: astore_1
      //   372: goto +8 -> 380
      //   375: astore_1
      //   376: goto +21 -> 397
      //   379: astore_1
      //   380: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   383: ifeq +12 -> 395
      //   386: ldc 106
      //   388: ldc -14
      //   390: aload_1
      //   391: invokestatic 138	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   394: pop
      //   395: aload_1
      //   396: athrow
      //   397: invokestatic 82	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   400: ifeq +11 -> 411
      //   403: ldc 106
      //   405: ldc -46
      //   407: invokestatic 116	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   410: pop
      //   411: aload_1
      //   412: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	413	0	this	ISyncAdapterImpl
      //   0	413	1	paramISyncContext	ISyncContext
      //   0	413	2	paramString	String
      //   0	413	3	paramAccount	Account
      //   0	413	4	paramBundle	Bundle
      //   24	316	5	localObject	Object
      //   102	198	6	localAccount	Account
      //   126	36	7	bool	boolean
      //   129	206	8	i	int
      //   231	77	9	localSyncThread	AbstractThreadedSyncAdapter.SyncThread
      //   237	39	10	localAbstractThreadedSyncAdapter	AbstractThreadedSyncAdapter
      //   242	36	11	localStringBuilder	StringBuilder
      // Exception table:
      //   from	to	target	type
      //   166	180	211	finally
      //   114	128	362	finally
      //   136	146	362	finally
      //   151	161	362	finally
      //   180	194	362	finally
      //   194	196	362	finally
      //   212	228	362	finally
      //   228	312	362	finally
      //   318	332	362	finally
      //   332	334	362	finally
      //   363	365	362	finally
      //   88	114	367	finally
      //   339	347	367	finally
      //   365	367	367	finally
      //   380	395	367	finally
      //   395	397	367	finally
      //   88	114	371	java/lang/RuntimeException
      //   88	114	371	java/lang/Error
      //   339	347	371	java/lang/RuntimeException
      //   339	347	371	java/lang/Error
      //   365	367	371	java/lang/RuntimeException
      //   365	367	371	java/lang/Error
      //   83	88	375	finally
      //   83	88	379	java/lang/RuntimeException
      //   83	88	379	java/lang/Error
    }
  }
  
  private class SyncThread
    extends Thread
  {
    private final Account mAccount;
    private final String mAuthority;
    private final Bundle mExtras;
    private final SyncContext mSyncContext;
    private final Account mThreadsKey;
    
    private SyncThread(String paramString1, SyncContext paramSyncContext, String paramString2, Account paramAccount, Bundle paramBundle)
    {
      super();
      mSyncContext = paramSyncContext;
      mAuthority = paramString2;
      mAccount = paramAccount;
      mExtras = paramBundle;
      mThreadsKey = AbstractThreadedSyncAdapter.this.toSyncKey(paramAccount);
    }
    
    private boolean isCanceled()
    {
      return Thread.currentThread().isInterrupted();
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: bipush 10
      //   2: invokestatic 72	android/os/Process:setThreadPriority	(I)V
      //   5: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   8: ifeq +11 -> 19
      //   11: ldc 77
      //   13: ldc 79
      //   15: invokestatic 85	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   18: pop
      //   19: ldc2_w 86
      //   22: aload_0
      //   23: getfield 29	android/content/AbstractThreadedSyncAdapter$SyncThread:mAuthority	Ljava/lang/String;
      //   26: invokestatic 93	android/os/Trace:traceBegin	(JLjava/lang/String;)V
      //   29: new 95	android/content/SyncResult
      //   32: dup
      //   33: invokespecial 97	android/content/SyncResult:<init>	()V
      //   36: astore_1
      //   37: aconst_null
      //   38: astore_2
      //   39: aconst_null
      //   40: astore_3
      //   41: aconst_null
      //   42: astore 4
      //   44: aload 4
      //   46: astore 5
      //   48: aload_0
      //   49: invokespecial 99	android/content/AbstractThreadedSyncAdapter$SyncThread:isCanceled	()Z
      //   52: ifeq +111 -> 163
      //   55: aload 4
      //   57: astore 5
      //   59: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   62: ifeq +15 -> 77
      //   65: aload 4
      //   67: astore 5
      //   69: ldc 77
      //   71: ldc 101
      //   73: invokestatic 85	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   76: pop
      //   77: ldc2_w 86
      //   80: invokestatic 105	android/os/Trace:traceEnd	(J)V
      //   83: iconst_0
      //   84: ifeq +11 -> 95
      //   87: new 107	java/lang/NullPointerException
      //   90: dup
      //   91: invokespecial 108	java/lang/NullPointerException:<init>	()V
      //   94: athrow
      //   95: aload_0
      //   96: invokespecial 99	android/content/AbstractThreadedSyncAdapter$SyncThread:isCanceled	()Z
      //   99: ifne +11 -> 110
      //   102: aload_0
      //   103: getfield 27	android/content/AbstractThreadedSyncAdapter$SyncThread:mSyncContext	Landroid/content/SyncContext;
      //   106: aload_1
      //   107: invokevirtual 114	android/content/SyncContext:onFinished	(Landroid/content/SyncResult;)V
      //   110: aload_0
      //   111: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   114: invokestatic 118	android/content/AbstractThreadedSyncAdapter:access$300	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/lang/Object;
      //   117: astore 4
      //   119: aload 4
      //   121: monitorenter
      //   122: aload_0
      //   123: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   126: invokestatic 122	android/content/AbstractThreadedSyncAdapter:access$400	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/util/HashMap;
      //   129: aload_0
      //   130: getfield 39	android/content/AbstractThreadedSyncAdapter$SyncThread:mThreadsKey	Landroid/accounts/Account;
      //   133: invokevirtual 128	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
      //   136: pop
      //   137: aload 4
      //   139: monitorexit
      //   140: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   143: ifeq +11 -> 154
      //   146: ldc 77
      //   148: ldc -126
      //   150: invokestatic 85	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   153: pop
      //   154: return
      //   155: astore 5
      //   157: aload 4
      //   159: monitorexit
      //   160: aload 5
      //   162: athrow
      //   163: aload 4
      //   165: astore 5
      //   167: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   170: ifeq +15 -> 185
      //   173: aload 4
      //   175: astore 5
      //   177: ldc 77
      //   179: ldc -124
      //   181: invokestatic 85	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   184: pop
      //   185: aload 4
      //   187: astore 5
      //   189: aload_0
      //   190: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   193: invokestatic 136	android/content/AbstractThreadedSyncAdapter:access$1300	(Landroid/content/AbstractThreadedSyncAdapter;)Landroid/content/Context;
      //   196: invokevirtual 142	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
      //   199: aload_0
      //   200: getfield 29	android/content/AbstractThreadedSyncAdapter$SyncThread:mAuthority	Ljava/lang/String;
      //   203: invokevirtual 148	android/content/ContentResolver:acquireContentProviderClient	(Ljava/lang/String;)Landroid/content/ContentProviderClient;
      //   206: astore 4
      //   208: aload 4
      //   210: ifnull +43 -> 253
      //   213: aload_0
      //   214: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   217: aload_0
      //   218: getfield 31	android/content/AbstractThreadedSyncAdapter$SyncThread:mAccount	Landroid/accounts/Account;
      //   221: aload_0
      //   222: getfield 33	android/content/AbstractThreadedSyncAdapter$SyncThread:mExtras	Landroid/os/Bundle;
      //   225: aload_0
      //   226: getfield 29	android/content/AbstractThreadedSyncAdapter$SyncThread:mAuthority	Ljava/lang/String;
      //   229: aload 4
      //   231: aload_1
      //   232: invokevirtual 152	android/content/AbstractThreadedSyncAdapter:onPerformSync	(Landroid/accounts/Account;Landroid/os/Bundle;Ljava/lang/String;Landroid/content/ContentProviderClient;Landroid/content/SyncResult;)V
      //   235: goto +23 -> 258
      //   238: astore 5
      //   240: goto +315 -> 555
      //   243: astore 6
      //   245: goto +132 -> 377
      //   248: astore 6
      //   250: goto +163 -> 413
      //   253: aload_1
      //   254: iconst_1
      //   255: putfield 156	android/content/SyncResult:databaseError	Z
      //   258: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   261: ifeq +11 -> 272
      //   264: ldc 77
      //   266: ldc -98
      //   268: invokestatic 85	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   271: pop
      //   272: ldc2_w 86
      //   275: invokestatic 105	android/os/Trace:traceEnd	(J)V
      //   278: aload 4
      //   280: ifnull +9 -> 289
      //   283: aload 4
      //   285: invokevirtual 163	android/content/ContentProviderClient:release	()Z
      //   288: pop
      //   289: aload_0
      //   290: invokespecial 99	android/content/AbstractThreadedSyncAdapter$SyncThread:isCanceled	()Z
      //   293: ifne +11 -> 304
      //   296: aload_0
      //   297: getfield 27	android/content/AbstractThreadedSyncAdapter$SyncThread:mSyncContext	Landroid/content/SyncContext;
      //   300: aload_1
      //   301: invokevirtual 114	android/content/SyncContext:onFinished	(Landroid/content/SyncResult;)V
      //   304: aload_0
      //   305: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   308: invokestatic 118	android/content/AbstractThreadedSyncAdapter:access$300	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/lang/Object;
      //   311: astore 5
      //   313: aload 5
      //   315: monitorenter
      //   316: aload_0
      //   317: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   320: invokestatic 122	android/content/AbstractThreadedSyncAdapter:access$400	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/util/HashMap;
      //   323: aload_0
      //   324: getfield 39	android/content/AbstractThreadedSyncAdapter$SyncThread:mThreadsKey	Landroid/accounts/Account;
      //   327: invokevirtual 128	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
      //   330: pop
      //   331: aload 5
      //   333: monitorexit
      //   334: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   337: ifeq +11 -> 348
      //   340: ldc 77
      //   342: ldc -126
      //   344: invokestatic 85	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   347: pop
      //   348: goto +198 -> 546
      //   351: astore 4
      //   353: aload 5
      //   355: monitorexit
      //   356: aload 4
      //   358: athrow
      //   359: astore 6
      //   361: aload 5
      //   363: astore 4
      //   365: aload 6
      //   367: astore 5
      //   369: goto +186 -> 555
      //   372: astore 6
      //   374: aload_2
      //   375: astore 4
      //   377: aload 4
      //   379: astore 5
      //   381: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   384: ifeq +17 -> 401
      //   387: aload 4
      //   389: astore 5
      //   391: ldc 77
      //   393: ldc -91
      //   395: aload 6
      //   397: invokestatic 168	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   400: pop
      //   401: aload 4
      //   403: astore 5
      //   405: aload 6
      //   407: athrow
      //   408: astore 6
      //   410: aload_3
      //   411: astore 4
      //   413: aload 4
      //   415: astore 5
      //   417: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   420: ifeq +17 -> 437
      //   423: aload 4
      //   425: astore 5
      //   427: ldc 77
      //   429: ldc -86
      //   431: aload 6
      //   433: invokestatic 168	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   436: pop
      //   437: aload 4
      //   439: astore 5
      //   441: aload_0
      //   442: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   445: aload_0
      //   446: getfield 31	android/content/AbstractThreadedSyncAdapter$SyncThread:mAccount	Landroid/accounts/Account;
      //   449: aload_0
      //   450: getfield 33	android/content/AbstractThreadedSyncAdapter$SyncThread:mExtras	Landroid/os/Bundle;
      //   453: aload_0
      //   454: getfield 29	android/content/AbstractThreadedSyncAdapter$SyncThread:mAuthority	Ljava/lang/String;
      //   457: aload_1
      //   458: invokevirtual 174	android/content/AbstractThreadedSyncAdapter:onSecurityException	(Landroid/accounts/Account;Landroid/os/Bundle;Ljava/lang/String;Landroid/content/SyncResult;)V
      //   461: aload 4
      //   463: astore 5
      //   465: aload_1
      //   466: iconst_1
      //   467: putfield 156	android/content/SyncResult:databaseError	Z
      //   470: ldc2_w 86
      //   473: invokestatic 105	android/os/Trace:traceEnd	(J)V
      //   476: aload 4
      //   478: ifnull +9 -> 487
      //   481: aload 4
      //   483: invokevirtual 163	android/content/ContentProviderClient:release	()Z
      //   486: pop
      //   487: aload_0
      //   488: invokespecial 99	android/content/AbstractThreadedSyncAdapter$SyncThread:isCanceled	()Z
      //   491: ifne +11 -> 502
      //   494: aload_0
      //   495: getfield 27	android/content/AbstractThreadedSyncAdapter$SyncThread:mSyncContext	Landroid/content/SyncContext;
      //   498: aload_1
      //   499: invokevirtual 114	android/content/SyncContext:onFinished	(Landroid/content/SyncResult;)V
      //   502: aload_0
      //   503: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   506: invokestatic 118	android/content/AbstractThreadedSyncAdapter:access$300	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/lang/Object;
      //   509: astore 4
      //   511: aload 4
      //   513: monitorenter
      //   514: aload_0
      //   515: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   518: invokestatic 122	android/content/AbstractThreadedSyncAdapter:access$400	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/util/HashMap;
      //   521: aload_0
      //   522: getfield 39	android/content/AbstractThreadedSyncAdapter$SyncThread:mThreadsKey	Landroid/accounts/Account;
      //   525: invokevirtual 128	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
      //   528: pop
      //   529: aload 4
      //   531: monitorexit
      //   532: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   535: ifeq +11 -> 546
      //   538: ldc 77
      //   540: ldc -126
      //   542: invokestatic 85	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   545: pop
      //   546: return
      //   547: astore 5
      //   549: aload 4
      //   551: monitorexit
      //   552: aload 5
      //   554: athrow
      //   555: ldc2_w 86
      //   558: invokestatic 105	android/os/Trace:traceEnd	(J)V
      //   561: aload 4
      //   563: ifnull +9 -> 572
      //   566: aload 4
      //   568: invokevirtual 163	android/content/ContentProviderClient:release	()Z
      //   571: pop
      //   572: aload_0
      //   573: invokespecial 99	android/content/AbstractThreadedSyncAdapter$SyncThread:isCanceled	()Z
      //   576: ifne +11 -> 587
      //   579: aload_0
      //   580: getfield 27	android/content/AbstractThreadedSyncAdapter$SyncThread:mSyncContext	Landroid/content/SyncContext;
      //   583: aload_1
      //   584: invokevirtual 114	android/content/SyncContext:onFinished	(Landroid/content/SyncResult;)V
      //   587: aload_0
      //   588: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   591: invokestatic 118	android/content/AbstractThreadedSyncAdapter:access$300	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/lang/Object;
      //   594: astore 4
      //   596: aload 4
      //   598: monitorenter
      //   599: aload_0
      //   600: getfield 22	android/content/AbstractThreadedSyncAdapter$SyncThread:this$0	Landroid/content/AbstractThreadedSyncAdapter;
      //   603: invokestatic 122	android/content/AbstractThreadedSyncAdapter:access$400	(Landroid/content/AbstractThreadedSyncAdapter;)Ljava/util/HashMap;
      //   606: aload_0
      //   607: getfield 39	android/content/AbstractThreadedSyncAdapter$SyncThread:mThreadsKey	Landroid/accounts/Account;
      //   610: invokevirtual 128	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
      //   613: pop
      //   614: aload 4
      //   616: monitorexit
      //   617: invokestatic 75	android/content/AbstractThreadedSyncAdapter:access$100	()Z
      //   620: ifeq +11 -> 631
      //   623: ldc 77
      //   625: ldc -126
      //   627: invokestatic 85	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   630: pop
      //   631: aload 5
      //   633: athrow
      //   634: astore 5
      //   636: aload 4
      //   638: monitorexit
      //   639: aload 5
      //   641: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	642	0	this	SyncThread
      //   36	548	1	localSyncResult	SyncResult
      //   38	337	2	localObject1	Object
      //   40	371	3	localObject2	Object
      //   42	242	4	localObject3	Object
      //   351	6	4	localObject4	Object
      //   363	274	4	localObject5	Object
      //   46	22	5	localObject6	Object
      //   155	6	5	localObject7	Object
      //   165	23	5	localObject8	Object
      //   238	1	5	localObject9	Object
      //   311	153	5	localObject10	Object
      //   547	85	5	localObject11	Object
      //   634	6	5	localObject12	Object
      //   243	1	6	localRuntimeException1	RuntimeException
      //   248	1	6	localSecurityException1	SecurityException
      //   359	7	6	localObject13	Object
      //   372	34	6	localRuntimeException2	RuntimeException
      //   408	24	6	localSecurityException2	SecurityException
      // Exception table:
      //   from	to	target	type
      //   122	140	155	finally
      //   157	160	155	finally
      //   213	235	238	finally
      //   253	258	238	finally
      //   258	272	238	finally
      //   213	235	243	java/lang/RuntimeException
      //   213	235	243	java/lang/Error
      //   253	258	243	java/lang/RuntimeException
      //   253	258	243	java/lang/Error
      //   258	272	243	java/lang/RuntimeException
      //   258	272	243	java/lang/Error
      //   213	235	248	java/lang/SecurityException
      //   253	258	248	java/lang/SecurityException
      //   258	272	248	java/lang/SecurityException
      //   316	334	351	finally
      //   353	356	351	finally
      //   48	55	359	finally
      //   59	65	359	finally
      //   69	77	359	finally
      //   167	173	359	finally
      //   177	185	359	finally
      //   189	208	359	finally
      //   381	387	359	finally
      //   391	401	359	finally
      //   405	408	359	finally
      //   417	423	359	finally
      //   427	437	359	finally
      //   441	461	359	finally
      //   465	470	359	finally
      //   48	55	372	java/lang/RuntimeException
      //   48	55	372	java/lang/Error
      //   59	65	372	java/lang/RuntimeException
      //   59	65	372	java/lang/Error
      //   69	77	372	java/lang/RuntimeException
      //   69	77	372	java/lang/Error
      //   167	173	372	java/lang/RuntimeException
      //   167	173	372	java/lang/Error
      //   177	185	372	java/lang/RuntimeException
      //   177	185	372	java/lang/Error
      //   189	208	372	java/lang/RuntimeException
      //   189	208	372	java/lang/Error
      //   48	55	408	java/lang/SecurityException
      //   59	65	408	java/lang/SecurityException
      //   69	77	408	java/lang/SecurityException
      //   167	173	408	java/lang/SecurityException
      //   177	185	408	java/lang/SecurityException
      //   189	208	408	java/lang/SecurityException
      //   514	532	547	finally
      //   549	552	547	finally
      //   599	617	634	finally
      //   636	639	634	finally
    }
  }
}
