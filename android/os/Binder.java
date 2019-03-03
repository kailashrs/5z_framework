package android.os;

import android.util.Log;
import com.android.internal.os.BinderInternal;
import com.android.internal.util.FastPrintWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import libcore.util.NativeAllocationRegistry;

public class Binder
  implements IBinder
{
  public static final boolean CHECK_PARCEL_SIZE = false;
  private static final boolean FIND_POTENTIAL_LEAKS = false;
  public static boolean LOG_RUNTIME_EXCEPTION = false;
  private static final int NATIVE_ALLOCATION_SIZE = 500;
  static final String TAG = "Binder";
  private static volatile String sDumpDisabled = null;
  private static volatile boolean sTracingEnabled = false;
  private static volatile TransactionTracker sTransactionTracker = null;
  static volatile boolean sWarnOnBlocking = false;
  private String mDescriptor;
  private final long mObject = getNativeBBinderHolder();
  private IInterface mOwner;
  
  public Binder()
  {
    NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, mObject);
  }
  
  public static IBinder allowBlocking(IBinder paramIBinder)
  {
    try
    {
      if ((paramIBinder instanceof BinderProxy))
      {
        mWarnOnBlocking = false;
      }
      else if ((paramIBinder != null) && (paramIBinder.getInterfaceDescriptor() != null) && (paramIBinder.queryLocalInterface(paramIBinder.getInterfaceDescriptor()) == null))
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Unable to allow blocking on interface ");
        localStringBuilder.append(paramIBinder);
        Log.w("Binder", localStringBuilder.toString());
      }
    }
    catch (RemoteException localRemoteException) {}
    return paramIBinder;
  }
  
  public static final native void blockUntilThreadAvailable();
  
  static void checkParcel(IBinder paramIBinder, int paramInt, Parcel paramParcel, String paramString) {}
  
  public static final native long clearCallingIdentity();
  
  public static void copyAllowBlocking(IBinder paramIBinder1, IBinder paramIBinder2)
  {
    if (((paramIBinder1 instanceof BinderProxy)) && ((paramIBinder2 instanceof BinderProxy))) {
      mWarnOnBlocking = mWarnOnBlocking;
    }
  }
  
  public static IBinder defaultBlocking(IBinder paramIBinder)
  {
    if ((paramIBinder instanceof BinderProxy)) {
      mWarnOnBlocking = sWarnOnBlocking;
    }
    return paramIBinder;
  }
  
  public static void disableTracing()
  {
    sTracingEnabled = false;
  }
  
  public static void enableTracing()
  {
    sTracingEnabled = true;
  }
  
  /* Error */
  private boolean execTransact(int paramInt1, long paramLong1, long paramLong2, int paramInt2)
  {
    // Byte code:
    //   0: invokestatic 126	com/android/internal/os/BinderCallsStats:getInstance	()Lcom/android/internal/os/BinderCallsStats;
    //   3: astore 7
    //   5: aload 7
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual 130	com/android/internal/os/BinderCallsStats:callStarted	(Landroid/os/Binder;I)Lcom/android/internal/os/BinderCallsStats$CallSession;
    //   12: astore 8
    //   14: lload_2
    //   15: invokestatic 136	android/os/Parcel:obtain	(J)Landroid/os/Parcel;
    //   18: astore 9
    //   20: lload 4
    //   22: invokestatic 136	android/os/Parcel:obtain	(J)Landroid/os/Parcel;
    //   25: astore 10
    //   27: invokestatic 140	android/os/Binder:isTracingEnabled	()Z
    //   30: istore 11
    //   32: iload 11
    //   34: ifeq +63 -> 97
    //   37: new 88	java/lang/StringBuilder
    //   40: astore 12
    //   42: aload 12
    //   44: invokespecial 89	java/lang/StringBuilder:<init>	()V
    //   47: aload 12
    //   49: aload_0
    //   50: invokevirtual 144	java/lang/Object:getClass	()Ljava/lang/Class;
    //   53: invokevirtual 149	java/lang/Class:getName	()Ljava/lang/String;
    //   56: invokevirtual 95	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: aload 12
    //   62: ldc -105
    //   64: invokevirtual 95	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: pop
    //   68: aload 12
    //   70: iload_1
    //   71: invokevirtual 154	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   74: pop
    //   75: lconst_1
    //   76: aload 12
    //   78: invokevirtual 101	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   81: invokestatic 160	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   84: goto +13 -> 97
    //   87: astore 8
    //   89: goto +161 -> 250
    //   92: astore 12
    //   94: goto +36 -> 130
    //   97: aload_0
    //   98: iload_1
    //   99: aload 9
    //   101: aload 10
    //   103: iload 6
    //   105: invokevirtual 164	android/os/Binder:onTransact	(ILandroid/os/Parcel;Landroid/os/Parcel;I)Z
    //   108: istore 13
    //   110: iload 13
    //   112: istore 14
    //   114: iload 11
    //   116: ifeq +102 -> 218
    //   119: iload 13
    //   121: istore 14
    //   123: lconst_1
    //   124: invokestatic 168	android/os/Trace:traceEnd	(J)V
    //   127: goto +91 -> 218
    //   130: getstatic 37	android/os/Binder:LOG_RUNTIME_EXCEPTION	Z
    //   133: ifeq +13 -> 146
    //   136: ldc 23
    //   138: ldc -86
    //   140: aload 12
    //   142: invokestatic 173	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   145: pop
    //   146: iload 6
    //   148: iconst_1
    //   149: iand
    //   150: ifeq +37 -> 187
    //   153: aload 12
    //   155: instanceof 73
    //   158: ifeq +16 -> 174
    //   161: ldc 23
    //   163: ldc -81
    //   165: aload 12
    //   167: invokestatic 173	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   170: pop
    //   171: goto +29 -> 200
    //   174: ldc 23
    //   176: ldc -86
    //   178: aload 12
    //   180: invokestatic 173	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   183: pop
    //   184: goto +16 -> 200
    //   187: aload 10
    //   189: iconst_0
    //   190: invokevirtual 179	android/os/Parcel:setDataPosition	(I)V
    //   193: aload 10
    //   195: aload 12
    //   197: invokevirtual 183	android/os/Parcel:writeException	(Ljava/lang/Exception;)V
    //   200: iconst_1
    //   201: istore 14
    //   203: iconst_1
    //   204: istore 13
    //   206: iload 11
    //   208: ifeq +10 -> 218
    //   211: iload 13
    //   213: istore 14
    //   215: goto -92 -> 123
    //   218: aload_0
    //   219: iload_1
    //   220: aload 10
    //   222: ldc -71
    //   224: invokestatic 187	android/os/Binder:checkParcel	(Landroid/os/IBinder;ILandroid/os/Parcel;Ljava/lang/String;)V
    //   227: aload 10
    //   229: invokevirtual 190	android/os/Parcel:recycle	()V
    //   232: aload 9
    //   234: invokevirtual 190	android/os/Parcel:recycle	()V
    //   237: invokestatic 195	android/os/StrictMode:clearGatheredViolations	()V
    //   240: aload 7
    //   242: aload 8
    //   244: invokevirtual 199	com/android/internal/os/BinderCallsStats:callEnded	(Lcom/android/internal/os/BinderCallsStats$CallSession;)V
    //   247: iload 14
    //   249: ireturn
    //   250: iload 11
    //   252: ifeq +7 -> 259
    //   255: lconst_1
    //   256: invokestatic 168	android/os/Trace:traceEnd	(J)V
    //   259: aload 8
    //   261: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	262	0	this	Binder
    //   0	262	1	paramInt1	int
    //   0	262	2	paramLong1	long
    //   0	262	4	paramLong2	long
    //   0	262	6	paramInt2	int
    //   3	238	7	localBinderCallsStats	com.android.internal.os.BinderCallsStats
    //   12	1	8	localCallSession1	com.android.internal.os.BinderCallsStats.CallSession
    //   87	173	8	localCallSession2	com.android.internal.os.BinderCallsStats.CallSession
    //   18	215	9	localParcel1	Parcel
    //   25	203	10	localParcel2	Parcel
    //   30	221	11	bool1	boolean
    //   40	37	12	localStringBuilder	StringBuilder
    //   92	104	12	localRemoteException	RemoteException
    //   108	104	13	bool2	boolean
    //   112	136	14	bool3	boolean
    // Exception table:
    //   from	to	target	type
    //   37	84	87	finally
    //   97	110	87	finally
    //   130	146	87	finally
    //   153	171	87	finally
    //   174	184	87	finally
    //   187	200	87	finally
    //   37	84	92	android/os/RemoteException
    //   37	84	92	java/lang/RuntimeException
    //   97	110	92	android/os/RemoteException
    //   97	110	92	java/lang/RuntimeException
  }
  
  public static final native void flushPendingCommands();
  
  public static final native int getCallingPid();
  
  public static final native int getCallingUid();
  
  public static final UserHandle getCallingUserHandle()
  {
    return UserHandle.of(UserHandle.getUserId(getCallingUid()));
  }
  
  private static native long getFinalizer();
  
  private static native long getNativeBBinderHolder();
  
  private static native long getNativeFinalizer();
  
  public static final native int getThreadStrictModePolicy();
  
  public static TransactionTracker getTransactionTracker()
  {
    try
    {
      if (sTransactionTracker == null)
      {
        localTransactionTracker = new android/os/TransactionTracker;
        localTransactionTracker.<init>();
        sTransactionTracker = localTransactionTracker;
      }
      TransactionTracker localTransactionTracker = sTransactionTracker;
      return localTransactionTracker;
    }
    finally {}
  }
  
  public static final boolean isProxy(IInterface paramIInterface)
  {
    boolean bool;
    if (paramIInterface.asBinder() != paramIInterface) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isTracingEnabled()
  {
    return sTracingEnabled;
  }
  
  public static final void joinThreadPool() {}
  
  public static final native void restoreCallingIdentity(long paramLong);
  
  public static void setDumpDisabled(String paramString)
  {
    sDumpDisabled = paramString;
  }
  
  public static final native void setThreadStrictModePolicy(int paramInt);
  
  public static void setWarnOnBlocking(boolean paramBoolean)
  {
    sWarnOnBlocking = paramBoolean;
  }
  
  /* Error */
  public static final <T> T withCleanCallingIdentity(com.android.internal.util.FunctionalUtils.ThrowingSupplier<T> paramThrowingSupplier)
  {
    // Byte code:
    //   0: invokestatic 249	android/os/Binder:clearCallingIdentity	()J
    //   3: lstore_1
    //   4: aload_0
    //   5: invokeinterface 255 1 0
    //   10: astore_0
    //   11: lload_1
    //   12: invokestatic 257	android/os/Binder:restoreCallingIdentity	(J)V
    //   15: iconst_0
    //   16: ifne +5 -> 21
    //   19: aload_0
    //   20: areturn
    //   21: aconst_null
    //   22: invokestatic 263	android/util/ExceptionUtils:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   25: athrow
    //   26: astore_0
    //   27: lload_1
    //   28: invokestatic 257	android/os/Binder:restoreCallingIdentity	(J)V
    //   31: iconst_0
    //   32: ifeq +8 -> 40
    //   35: aconst_null
    //   36: invokestatic 263	android/util/ExceptionUtils:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   39: athrow
    //   40: aload_0
    //   41: athrow
    //   42: astore_0
    //   43: lload_1
    //   44: invokestatic 257	android/os/Binder:restoreCallingIdentity	(J)V
    //   47: aload_0
    //   48: invokestatic 263	android/util/ExceptionUtils:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   51: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	52	0	paramThrowingSupplier	com.android.internal.util.FunctionalUtils.ThrowingSupplier<T>
    //   3	41	1	l	long
    // Exception table:
    //   from	to	target	type
    //   4	11	26	finally
    //   4	11	42	java/lang/Throwable
  }
  
  /* Error */
  public static final void withCleanCallingIdentity(com.android.internal.util.FunctionalUtils.ThrowingRunnable paramThrowingRunnable)
  {
    // Byte code:
    //   0: invokestatic 249	android/os/Binder:clearCallingIdentity	()J
    //   3: lstore_1
    //   4: aload_0
    //   5: invokeinterface 271 1 0
    //   10: lload_1
    //   11: invokestatic 257	android/os/Binder:restoreCallingIdentity	(J)V
    //   14: iconst_0
    //   15: ifne +4 -> 19
    //   18: return
    //   19: aconst_null
    //   20: invokestatic 263	android/util/ExceptionUtils:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   23: athrow
    //   24: astore_0
    //   25: lload_1
    //   26: invokestatic 257	android/os/Binder:restoreCallingIdentity	(J)V
    //   29: iconst_0
    //   30: ifeq +8 -> 38
    //   33: aconst_null
    //   34: invokestatic 263	android/util/ExceptionUtils:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   37: athrow
    //   38: aload_0
    //   39: athrow
    //   40: astore_0
    //   41: lload_1
    //   42: invokestatic 257	android/os/Binder:restoreCallingIdentity	(J)V
    //   45: aload_0
    //   46: invokestatic 263	android/util/ExceptionUtils:propagate	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
    //   49: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	50	0	paramThrowingRunnable	com.android.internal.util.FunctionalUtils.ThrowingRunnable
    //   3	39	1	l	long
    // Exception table:
    //   from	to	target	type
    //   4	10	24	finally
    //   4	10	40	java/lang/Throwable
  }
  
  public void attachInterface(IInterface paramIInterface, String paramString)
  {
    mOwner = paramIInterface;
    mDescriptor = paramString;
  }
  
  void doDump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if (sDumpDisabled == null) {
      try
      {
        dump(paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      catch (Throwable paramFileDescriptor)
      {
        for (;;)
        {
          paramPrintWriter.println();
          paramPrintWriter.println("Exception occurred while dumping:");
          paramFileDescriptor.printStackTrace(paramPrintWriter);
        }
      }
      catch (SecurityException paramFileDescriptor)
      {
        paramArrayOfString = new StringBuilder();
        paramArrayOfString.append("Security exception: ");
        paramArrayOfString.append(paramFileDescriptor.getMessage());
        paramPrintWriter.println(paramArrayOfString.toString());
        throw paramFileDescriptor;
      }
    } else {
      paramPrintWriter.println(sDumpDisabled);
    }
  }
  
  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {}
  
  public void dump(FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
  {
    FastPrintWriter localFastPrintWriter = new FastPrintWriter(new FileOutputStream(paramFileDescriptor));
    try
    {
      doDump(paramFileDescriptor, localFastPrintWriter, paramArrayOfString);
      return;
    }
    finally
    {
      localFastPrintWriter.flush();
    }
  }
  
  public void dumpAsync(final FileDescriptor paramFileDescriptor, final String[] paramArrayOfString)
  {
    new Thread("Binder.dumpAsync")
    {
      public void run()
      {
        try
        {
          dump(paramFileDescriptor, val$pw, paramArrayOfString);
          return;
        }
        finally
        {
          val$pw.flush();
        }
      }
    }.start();
  }
  
  public String getInterfaceDescriptor()
  {
    return mDescriptor;
  }
  
  public boolean isBinderAlive()
  {
    return true;
  }
  
  public void linkToDeath(IBinder.DeathRecipient paramDeathRecipient, int paramInt) {}
  
  public void onShellCommand(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, FileDescriptor paramFileDescriptor3, String[] paramArrayOfString, ShellCallback paramShellCallback, ResultReceiver paramResultReceiver)
    throws RemoteException
  {
    if (paramFileDescriptor3 != null) {
      paramFileDescriptor2 = paramFileDescriptor3;
    }
    paramFileDescriptor1 = new FastPrintWriter(new FileOutputStream(paramFileDescriptor2));
    paramFileDescriptor1.println("No shell command implementation.");
    paramFileDescriptor1.flush();
    paramResultReceiver.send(0, null);
  }
  
  /* Error */
  protected boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
    throws RemoteException
  {
    // Byte code:
    //   0: iload_1
    //   1: ldc_w 344
    //   4: if_icmpne +13 -> 17
    //   7: aload_3
    //   8: aload_0
    //   9: invokevirtual 345	android/os/Binder:getInterfaceDescriptor	()Ljava/lang/String;
    //   12: invokevirtual 348	android/os/Parcel:writeString	(Ljava/lang/String;)V
    //   15: iconst_1
    //   16: ireturn
    //   17: iload_1
    //   18: ldc_w 349
    //   21: if_icmpne +69 -> 90
    //   24: aload_2
    //   25: invokevirtual 353	android/os/Parcel:readFileDescriptor	()Landroid/os/ParcelFileDescriptor;
    //   28: astore 5
    //   30: aload_2
    //   31: invokevirtual 357	android/os/Parcel:readStringArray	()[Ljava/lang/String;
    //   34: astore_2
    //   35: aload 5
    //   37: ifnull +37 -> 74
    //   40: aload 5
    //   42: invokevirtual 363	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   45: astore 6
    //   47: aload_0
    //   48: aload 6
    //   50: aload_2
    //   51: invokevirtual 365	android/os/Binder:dump	(Ljava/io/FileDescriptor;[Ljava/lang/String;)V
    //   54: aload 5
    //   56: invokestatic 371	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   59: goto +15 -> 74
    //   62: astore_2
    //   63: goto +4 -> 67
    //   66: astore_2
    //   67: aload 5
    //   69: invokestatic 371	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   72: aload_2
    //   73: athrow
    //   74: aload_3
    //   75: ifnull +10 -> 85
    //   78: aload_3
    //   79: invokevirtual 374	android/os/Parcel:writeNoException	()V
    //   82: goto +6 -> 88
    //   85: invokestatic 195	android/os/StrictMode:clearGatheredViolations	()V
    //   88: iconst_1
    //   89: ireturn
    //   90: iload_1
    //   91: ldc_w 375
    //   94: if_icmpne +189 -> 283
    //   97: aload_2
    //   98: invokevirtual 353	android/os/Parcel:readFileDescriptor	()Landroid/os/ParcelFileDescriptor;
    //   101: astore 6
    //   103: aload_2
    //   104: invokevirtual 353	android/os/Parcel:readFileDescriptor	()Landroid/os/ParcelFileDescriptor;
    //   107: astore 7
    //   109: aload_2
    //   110: invokevirtual 353	android/os/Parcel:readFileDescriptor	()Landroid/os/ParcelFileDescriptor;
    //   113: astore 8
    //   115: aload_2
    //   116: invokevirtual 357	android/os/Parcel:readStringArray	()[Ljava/lang/String;
    //   119: astore 9
    //   121: getstatic 381	android/os/ShellCallback:CREATOR	Landroid/os/Parcelable$Creator;
    //   124: aload_2
    //   125: invokeinterface 387 2 0
    //   130: checkcast 377	android/os/ShellCallback
    //   133: astore 10
    //   135: getstatic 388	android/os/ResultReceiver:CREATOR	Landroid/os/Parcelable$Creator;
    //   138: aload_2
    //   139: invokeinterface 387 2 0
    //   144: checkcast 338	android/os/ResultReceiver
    //   147: astore 11
    //   149: aload 7
    //   151: ifnull +101 -> 252
    //   154: aload 6
    //   156: ifnull +16 -> 172
    //   159: aload 6
    //   161: invokevirtual 363	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   164: astore_2
    //   165: goto +9 -> 174
    //   168: astore_2
    //   169: goto +52 -> 221
    //   172: aconst_null
    //   173: astore_2
    //   174: aload 7
    //   176: invokevirtual 363	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   179: astore 12
    //   181: aload 8
    //   183: ifnull +13 -> 196
    //   186: aload 8
    //   188: invokevirtual 363	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   191: astore 5
    //   193: goto +10 -> 203
    //   196: aload 7
    //   198: invokevirtual 363	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   201: astore 5
    //   203: aload_0
    //   204: aload_2
    //   205: aload 12
    //   207: aload 5
    //   209: aload 9
    //   211: aload 10
    //   213: aload 11
    //   215: invokevirtual 391	android/os/Binder:shellCommand	(Ljava/io/FileDescriptor;Ljava/io/FileDescriptor;Ljava/io/FileDescriptor;[Ljava/lang/String;Landroid/os/ShellCallback;Landroid/os/ResultReceiver;)V
    //   218: goto +34 -> 252
    //   221: aload 6
    //   223: invokestatic 371	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   226: aload 7
    //   228: invokestatic 371	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   231: aload 8
    //   233: invokestatic 371	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   236: aload_3
    //   237: ifnull +10 -> 247
    //   240: aload_3
    //   241: invokevirtual 374	android/os/Parcel:writeNoException	()V
    //   244: goto +6 -> 250
    //   247: invokestatic 195	android/os/StrictMode:clearGatheredViolations	()V
    //   250: aload_2
    //   251: athrow
    //   252: aload 6
    //   254: invokestatic 371	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   257: aload 7
    //   259: invokestatic 371	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   262: aload 8
    //   264: invokestatic 371	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   267: aload_3
    //   268: ifnull +10 -> 278
    //   271: aload_3
    //   272: invokevirtual 374	android/os/Parcel:writeNoException	()V
    //   275: goto +6 -> 281
    //   278: invokestatic 195	android/os/StrictMode:clearGatheredViolations	()V
    //   281: iconst_1
    //   282: ireturn
    //   283: iconst_0
    //   284: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	285	0	this	Binder
    //   0	285	1	paramInt1	int
    //   0	285	2	paramParcel1	Parcel
    //   0	285	3	paramParcel2	Parcel
    //   0	285	4	paramInt2	int
    //   28	180	5	localObject1	Object
    //   45	208	6	localObject2	Object
    //   107	151	7	localParcelFileDescriptor1	ParcelFileDescriptor
    //   113	150	8	localParcelFileDescriptor2	ParcelFileDescriptor
    //   119	91	9	arrayOfString	String[]
    //   133	79	10	localShellCallback	ShellCallback
    //   147	67	11	localResultReceiver	ResultReceiver
    //   179	27	12	localFileDescriptor	FileDescriptor
    // Exception table:
    //   from	to	target	type
    //   47	54	62	finally
    //   40	47	66	finally
    //   159	165	168	finally
    //   174	181	168	finally
    //   186	193	168	finally
    //   196	203	168	finally
    //   203	218	168	finally
  }
  
  public boolean pingBinder()
  {
    return true;
  }
  
  public IInterface queryLocalInterface(String paramString)
  {
    if ((mDescriptor != null) && (mDescriptor.equals(paramString))) {
      return mOwner;
    }
    return null;
  }
  
  public void shellCommand(FileDescriptor paramFileDescriptor1, FileDescriptor paramFileDescriptor2, FileDescriptor paramFileDescriptor3, String[] paramArrayOfString, ShellCallback paramShellCallback, ResultReceiver paramResultReceiver)
    throws RemoteException
  {
    onShellCommand(paramFileDescriptor1, paramFileDescriptor2, paramFileDescriptor3, paramArrayOfString, paramShellCallback, paramResultReceiver);
  }
  
  public final boolean transact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
    throws RemoteException
  {
    if (paramParcel1 != null) {
      paramParcel1.setDataPosition(0);
    }
    boolean bool = onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
    if (paramParcel2 != null) {
      paramParcel2.setDataPosition(0);
    }
    return bool;
  }
  
  public boolean unlinkToDeath(IBinder.DeathRecipient paramDeathRecipient, int paramInt)
  {
    return true;
  }
  
  private static class NoImagePreloadHolder
  {
    public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Binder.class.getClassLoader(), Binder.access$000(), 500L);
    
    private NoImagePreloadHolder() {}
  }
}
