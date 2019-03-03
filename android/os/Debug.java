package android.os;

import android.app.AppGlobals;
import android.content.Context;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.android.internal.util.TypedProperties;
import dalvik.system.VMDebug;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;

public final class Debug
{
  private static final String DEFAULT_TRACE_BODY = "dmtrace";
  private static final String DEFAULT_TRACE_EXTENSION = ".trace";
  public static final int MEMINFO_BUFFERS = 2;
  public static final int MEMINFO_CACHED = 3;
  public static final int MEMINFO_COUNT = 15;
  public static final int MEMINFO_FREE = 1;
  public static final int MEMINFO_KERNEL_STACK = 14;
  public static final int MEMINFO_MAPPED = 11;
  public static final int MEMINFO_PAGE_TABLES = 13;
  public static final int MEMINFO_SHMEM = 4;
  public static final int MEMINFO_SLAB = 5;
  public static final int MEMINFO_SLAB_RECLAIMABLE = 6;
  public static final int MEMINFO_SLAB_UNRECLAIMABLE = 7;
  public static final int MEMINFO_SWAP_FREE = 9;
  public static final int MEMINFO_SWAP_TOTAL = 8;
  public static final int MEMINFO_TOTAL = 0;
  public static final int MEMINFO_VM_ALLOC_USED = 12;
  public static final int MEMINFO_ZRAM_TOTAL = 10;
  private static final int MIN_DEBUGGER_IDLE = 1300;
  public static final int SHOW_CLASSLOADER = 2;
  public static final int SHOW_FULL_DETAIL = 1;
  public static final int SHOW_INITIALIZED = 4;
  private static final int SPIN_DELAY = 200;
  private static final String SYSFS_QEMU_TRACE_STATE = "/sys/qemu_trace/state";
  private static final String TAG = "Debug";
  @Deprecated
  public static final int TRACE_COUNT_ALLOCS = 1;
  private static final TypedProperties debugProperties = null;
  private static volatile boolean mWaiting = false;
  
  private Debug() {}
  
  public static void attachJvmtiAgent(String paramString1, String paramString2, ClassLoader paramClassLoader)
    throws IOException
  {
    Preconditions.checkNotNull(paramString1);
    Preconditions.checkArgument(paramString1.contains("=") ^ true);
    if (paramString2 == null)
    {
      VMDebug.attachAgent(paramString1, paramClassLoader);
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append("=");
      localStringBuilder.append(paramString2);
      VMDebug.attachAgent(localStringBuilder.toString(), paramClassLoader);
    }
  }
  
  public static final boolean cacheRegisterMap(String paramString)
  {
    return VMDebug.cacheRegisterMap(paramString);
  }
  
  @Deprecated
  public static void changeDebugPort(int paramInt) {}
  
  public static long countInstancesOfClass(Class paramClass)
  {
    return VMDebug.countInstancesOfClass(paramClass, true);
  }
  
  public static void dumpHprofData(String paramString)
    throws IOException
  {
    VMDebug.dumpHprofData(paramString);
  }
  
  public static void dumpHprofData(String paramString, FileDescriptor paramFileDescriptor)
    throws IOException
  {
    VMDebug.dumpHprofData(paramString, paramFileDescriptor);
  }
  
  public static void dumpHprofDataDdms() {}
  
  public static native boolean dumpJavaBacktraceToFileTimeout(int paramInt1, String paramString, int paramInt2);
  
  public static native boolean dumpNativeBacktraceToFileTimeout(int paramInt1, String paramString, int paramInt2);
  
  public static native void dumpNativeHeap(FileDescriptor paramFileDescriptor);
  
  public static native void dumpNativeMallocInfo(FileDescriptor paramFileDescriptor);
  
  public static final void dumpReferenceTables() {}
  
  public static boolean dumpService(String paramString, FileDescriptor paramFileDescriptor, String[] paramArrayOfString)
  {
    IBinder localIBinder = ServiceManager.getService(paramString);
    if (localIBinder == null)
    {
      paramFileDescriptor = new StringBuilder();
      paramFileDescriptor.append("Can't find service to dump: ");
      paramFileDescriptor.append(paramString);
      Log.e("Debug", paramFileDescriptor.toString());
      return false;
    }
    try
    {
      localIBinder.dump(paramFileDescriptor, paramArrayOfString);
      return true;
    }
    catch (RemoteException paramFileDescriptor)
    {
      paramArrayOfString = new StringBuilder();
      paramArrayOfString.append("Can't dump service: ");
      paramArrayOfString.append(paramString);
      Log.e("Debug", paramArrayOfString.toString(), paramFileDescriptor);
    }
    return false;
  }
  
  public static void enableEmulatorTraceOutput() {}
  
  private static boolean fieldTypeMatches(Field paramField, Class<?> paramClass)
  {
    paramField = paramField.getType();
    boolean bool = true;
    if (paramField == paramClass) {
      return true;
    }
    try
    {
      paramClass = paramClass.getField("TYPE");
      try
      {
        paramClass = (Class)paramClass.get(null);
        if (paramField != paramClass) {
          bool = false;
        }
        return bool;
      }
      catch (IllegalAccessException paramField)
      {
        return false;
      }
      return false;
    }
    catch (NoSuchFieldException paramField) {}
  }
  
  private static String fixTracePath(String paramString)
  {
    Object localObject;
    if (paramString != null)
    {
      localObject = paramString;
      if (paramString.charAt(0) == '/') {}
    }
    else
    {
      localObject = AppGlobals.getInitialApplication();
      if (localObject != null) {
        localObject = ((Context)localObject).getExternalFilesDir(null);
      } else {
        localObject = Environment.getExternalStorageDirectory();
      }
      if (paramString == null) {
        localObject = new File((File)localObject, "dmtrace").getAbsolutePath();
      } else {
        localObject = new File((File)localObject, paramString).getAbsolutePath();
      }
    }
    paramString = (String)localObject;
    if (!((String)localObject).endsWith(".trace"))
    {
      paramString = new StringBuilder();
      paramString.append((String)localObject);
      paramString.append(".trace");
      paramString = paramString.toString();
    }
    return paramString;
  }
  
  public static final native int getBinderDeathObjectCount();
  
  public static final native int getBinderLocalObjectCount();
  
  public static final native int getBinderProxyObjectCount();
  
  public static native int getBinderReceivedTransactions();
  
  public static native int getBinderSentTransactions();
  
  public static String getCaller()
  {
    return getCaller(Thread.currentThread().getStackTrace(), 0);
  }
  
  private static String getCaller(StackTraceElement[] paramArrayOfStackTraceElement, int paramInt)
  {
    if (4 + paramInt >= paramArrayOfStackTraceElement.length) {
      return "<bottom of call stack>";
    }
    paramArrayOfStackTraceElement = paramArrayOfStackTraceElement[(4 + paramInt)];
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramArrayOfStackTraceElement.getClassName());
    localStringBuilder.append(".");
    localStringBuilder.append(paramArrayOfStackTraceElement.getMethodName());
    localStringBuilder.append(":");
    localStringBuilder.append(paramArrayOfStackTraceElement.getLineNumber());
    return localStringBuilder.toString();
  }
  
  public static String getCallers(int paramInt)
  {
    StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramInt; i++)
    {
      localStringBuffer.append(getCaller(arrayOfStackTraceElement, i));
      localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }
  
  public static String getCallers(int paramInt1, int paramInt2)
  {
    StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = paramInt1; i < paramInt2 + paramInt1; i++)
    {
      localStringBuffer.append(getCaller(arrayOfStackTraceElement, i));
      localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }
  
  public static String getCallers(int paramInt, String paramString)
  {
    StackTraceElement[] arrayOfStackTraceElement = Thread.currentThread().getStackTrace();
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < paramInt; i++)
    {
      localStringBuffer.append(paramString);
      localStringBuffer.append(getCaller(arrayOfStackTraceElement, i));
      localStringBuffer.append("\n");
    }
    return localStringBuffer.toString();
  }
  
  @Deprecated
  public static int getGlobalAllocCount()
  {
    return VMDebug.getAllocCount(1);
  }
  
  @Deprecated
  public static int getGlobalAllocSize()
  {
    return VMDebug.getAllocCount(2);
  }
  
  @Deprecated
  public static int getGlobalClassInitCount()
  {
    return VMDebug.getAllocCount(32);
  }
  
  @Deprecated
  public static int getGlobalClassInitTime()
  {
    return VMDebug.getAllocCount(64);
  }
  
  @Deprecated
  public static int getGlobalExternalAllocCount()
  {
    return 0;
  }
  
  @Deprecated
  public static int getGlobalExternalAllocSize()
  {
    return 0;
  }
  
  @Deprecated
  public static int getGlobalExternalFreedCount()
  {
    return 0;
  }
  
  @Deprecated
  public static int getGlobalExternalFreedSize()
  {
    return 0;
  }
  
  @Deprecated
  public static int getGlobalFreedCount()
  {
    return VMDebug.getAllocCount(4);
  }
  
  @Deprecated
  public static int getGlobalFreedSize()
  {
    return VMDebug.getAllocCount(8);
  }
  
  @Deprecated
  public static int getGlobalGcInvocationCount()
  {
    return VMDebug.getAllocCount(16);
  }
  
  public static int getLoadedClassCount()
  {
    return VMDebug.getLoadedClassCount();
  }
  
  public static native void getMemInfo(long[] paramArrayOfLong);
  
  public static native void getMemoryInfo(int paramInt, MemoryInfo paramMemoryInfo);
  
  public static native void getMemoryInfo(MemoryInfo paramMemoryInfo);
  
  public static int getMethodTracingMode()
  {
    return VMDebug.getMethodTracingMode();
  }
  
  public static native long getNativeHeapAllocatedSize();
  
  public static native long getNativeHeapFreeSize();
  
  public static native long getNativeHeapSize();
  
  public static native long getPss();
  
  public static native long getPss(int paramInt, long[] paramArrayOfLong1, long[] paramArrayOfLong2);
  
  public static String getRuntimeStat(String paramString)
  {
    return VMDebug.getRuntimeStat(paramString);
  }
  
  public static Map<String, String> getRuntimeStats()
  {
    return VMDebug.getRuntimeStats();
  }
  
  @Deprecated
  public static int getThreadAllocCount()
  {
    return VMDebug.getAllocCount(65536);
  }
  
  @Deprecated
  public static int getThreadAllocSize()
  {
    return VMDebug.getAllocCount(131072);
  }
  
  @Deprecated
  public static int getThreadExternalAllocCount()
  {
    return 0;
  }
  
  @Deprecated
  public static int getThreadExternalAllocSize()
  {
    return 0;
  }
  
  @Deprecated
  public static int getThreadGcInvocationCount()
  {
    return VMDebug.getAllocCount(1048576);
  }
  
  public static native String getUnreachableMemory(int paramInt, boolean paramBoolean);
  
  public static String[] getVmFeatureList()
  {
    return VMDebug.getVmFeatureList();
  }
  
  public static boolean isDebuggerConnected()
  {
    return VMDebug.isDebuggerConnected();
  }
  
  private static void modifyFieldIfSet(Field paramField, TypedProperties paramTypedProperties, String paramString)
  {
    if (paramField.getType() == String.class)
    {
      int i = paramTypedProperties.getStringInfo(paramString);
      switch (i)
      {
      default: 
        paramField = new StringBuilder();
        paramField.append("Unexpected getStringInfo(");
        paramField.append(paramString);
        paramField.append(") return value ");
        paramField.append(i);
        throw new IllegalStateException(paramField.toString());
      case 1: 
        break;
      case 0: 
        try
        {
          paramField.set(null, null);
          return;
        }
        catch (IllegalAccessException paramField)
        {
          paramTypedProperties = new StringBuilder();
          paramTypedProperties.append("Cannot set field for ");
          paramTypedProperties.append(paramString);
          throw new IllegalArgumentException(paramTypedProperties.toString(), paramField);
        }
      case -1: 
        return;
      }
      paramTypedProperties = new StringBuilder();
      paramTypedProperties.append("Type of ");
      paramTypedProperties.append(paramString);
      paramTypedProperties.append("  does not match field type (");
      paramTypedProperties.append(paramField.getType());
      paramTypedProperties.append(")");
      throw new IllegalArgumentException(paramTypedProperties.toString());
    }
    Object localObject = paramTypedProperties.get(paramString);
    if (localObject != null) {
      if (fieldTypeMatches(paramField, localObject.getClass()))
      {
        try
        {
          paramField.set(null, localObject);
        }
        catch (IllegalAccessException paramField)
        {
          paramTypedProperties = new StringBuilder();
          paramTypedProperties.append("Cannot set field for ");
          paramTypedProperties.append(paramString);
          throw new IllegalArgumentException(paramTypedProperties.toString(), paramField);
        }
      }
      else
      {
        paramTypedProperties = new StringBuilder();
        paramTypedProperties.append("Type of ");
        paramTypedProperties.append(paramString);
        paramTypedProperties.append(" (");
        paramTypedProperties.append(localObject.getClass());
        paramTypedProperties.append(")  does not match field type (");
        paramTypedProperties.append(paramField.getType());
        paramTypedProperties.append(")");
        throw new IllegalArgumentException(paramTypedProperties.toString());
      }
    }
  }
  
  public static void printLoadedClasses(int paramInt)
  {
    VMDebug.printLoadedClasses(paramInt);
  }
  
  @Deprecated
  public static void resetAllCounts()
  {
    VMDebug.resetAllocCount(-1);
  }
  
  @Deprecated
  public static void resetGlobalAllocCount()
  {
    VMDebug.resetAllocCount(1);
  }
  
  @Deprecated
  public static void resetGlobalAllocSize()
  {
    VMDebug.resetAllocCount(2);
  }
  
  @Deprecated
  public static void resetGlobalClassInitCount()
  {
    VMDebug.resetAllocCount(32);
  }
  
  @Deprecated
  public static void resetGlobalClassInitTime()
  {
    VMDebug.resetAllocCount(64);
  }
  
  @Deprecated
  public static void resetGlobalExternalAllocCount() {}
  
  @Deprecated
  public static void resetGlobalExternalAllocSize() {}
  
  @Deprecated
  public static void resetGlobalExternalFreedCount() {}
  
  @Deprecated
  public static void resetGlobalExternalFreedSize() {}
  
  @Deprecated
  public static void resetGlobalFreedCount()
  {
    VMDebug.resetAllocCount(4);
  }
  
  @Deprecated
  public static void resetGlobalFreedSize()
  {
    VMDebug.resetAllocCount(8);
  }
  
  @Deprecated
  public static void resetGlobalGcInvocationCount()
  {
    VMDebug.resetAllocCount(16);
  }
  
  @Deprecated
  public static void resetThreadAllocCount()
  {
    VMDebug.resetAllocCount(65536);
  }
  
  @Deprecated
  public static void resetThreadAllocSize()
  {
    VMDebug.resetAllocCount(131072);
  }
  
  @Deprecated
  public static void resetThreadExternalAllocCount() {}
  
  @Deprecated
  public static void resetThreadExternalAllocSize() {}
  
  @Deprecated
  public static void resetThreadGcInvocationCount()
  {
    VMDebug.resetAllocCount(1048576);
  }
  
  @Deprecated
  public static int setAllocationLimit(int paramInt)
  {
    return -1;
  }
  
  public static void setFieldsOn(Class<?> paramClass)
  {
    setFieldsOn(paramClass, false);
  }
  
  public static void setFieldsOn(Class<?> paramClass, boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setFieldsOn(");
    if (paramClass == null) {
      paramClass = "null";
    } else {
      paramClass = paramClass.getName();
    }
    localStringBuilder.append(paramClass);
    localStringBuilder.append(") called in non-DEBUG build");
    Log.wtf("Debug", localStringBuilder.toString());
  }
  
  @Deprecated
  public static int setGlobalAllocationLimit(int paramInt)
  {
    return -1;
  }
  
  @Deprecated
  public static void startAllocCounting() {}
  
  public static void startMethodTracing()
  {
    VMDebug.startMethodTracing(fixTracePath(null), 0, 0, false, 0);
  }
  
  public static void startMethodTracing(String paramString)
  {
    startMethodTracing(paramString, 0, 0);
  }
  
  public static void startMethodTracing(String paramString, int paramInt)
  {
    startMethodTracing(paramString, paramInt, 0);
  }
  
  public static void startMethodTracing(String paramString, int paramInt1, int paramInt2)
  {
    VMDebug.startMethodTracing(fixTracePath(paramString), paramInt1, paramInt2, false, 0);
  }
  
  public static void startMethodTracing(String paramString, FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    VMDebug.startMethodTracing(paramString, paramFileDescriptor, paramInt1, paramInt2, false, 0, paramBoolean);
  }
  
  public static void startMethodTracingDdms(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    VMDebug.startMethodTracingDdms(paramInt1, paramInt2, paramBoolean, paramInt3);
  }
  
  public static void startMethodTracingSampling(String paramString, int paramInt1, int paramInt2)
  {
    VMDebug.startMethodTracing(fixTracePath(paramString), paramInt1, 0, true, paramInt2);
  }
  
  /* Error */
  public static void startNativeTracing()
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: aconst_null
    //   3: astore_1
    //   4: aload_1
    //   5: astore_2
    //   6: aload_0
    //   7: astore_3
    //   8: new 481	java/io/FileOutputStream
    //   11: astore 4
    //   13: aload_1
    //   14: astore_2
    //   15: aload_0
    //   16: astore_3
    //   17: aload 4
    //   19: ldc 67
    //   21: invokespecial 482	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   24: aload_1
    //   25: astore_2
    //   26: aload_0
    //   27: astore_3
    //   28: new 484	com/android/internal/util/FastPrintWriter
    //   31: astore 5
    //   33: aload_1
    //   34: astore_2
    //   35: aload_0
    //   36: astore_3
    //   37: aload 5
    //   39: aload 4
    //   41: invokespecial 487	com/android/internal/util/FastPrintWriter:<init>	(Ljava/io/OutputStream;)V
    //   44: aload 5
    //   46: astore_1
    //   47: aload_1
    //   48: astore_2
    //   49: aload_1
    //   50: astore_3
    //   51: aload_1
    //   52: ldc_w 489
    //   55: invokevirtual 494	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   58: aload_1
    //   59: invokevirtual 497	java/io/PrintWriter:close	()V
    //   62: goto +24 -> 86
    //   65: astore_1
    //   66: aload_2
    //   67: ifnull +7 -> 74
    //   70: aload_2
    //   71: invokevirtual 497	java/io/PrintWriter:close	()V
    //   74: aload_1
    //   75: athrow
    //   76: astore_1
    //   77: aload_3
    //   78: ifnull +8 -> 86
    //   81: aload_3
    //   82: astore_1
    //   83: goto -25 -> 58
    //   86: invokestatic 190	dalvik/system/VMDebug:startEmulatorTracing	()V
    //   89: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   1	35	0	localObject1	Object
    //   3	56	1	localObject2	Object
    //   65	10	1	localObject3	Object
    //   76	1	1	localException	Exception
    //   82	1	1	localObject4	Object
    //   5	66	2	localObject5	Object
    //   7	75	3	localObject6	Object
    //   11	29	4	localFileOutputStream	java.io.FileOutputStream
    //   31	14	5	localFastPrintWriter	com.android.internal.util.FastPrintWriter
    // Exception table:
    //   from	to	target	type
    //   8	13	65	finally
    //   17	24	65	finally
    //   28	33	65	finally
    //   37	44	65	finally
    //   51	58	65	finally
    //   8	13	76	java/lang/Exception
    //   17	24	76	java/lang/Exception
    //   28	33	76	java/lang/Exception
    //   37	44	76	java/lang/Exception
    //   51	58	76	java/lang/Exception
  }
  
  @Deprecated
  public static void stopAllocCounting() {}
  
  public static void stopMethodTracing() {}
  
  /* Error */
  public static void stopNativeTracing()
  {
    // Byte code:
    //   0: invokestatic 507	dalvik/system/VMDebug:stopEmulatorTracing	()V
    //   3: aconst_null
    //   4: astore_0
    //   5: aconst_null
    //   6: astore_1
    //   7: aload_1
    //   8: astore_2
    //   9: aload_0
    //   10: astore_3
    //   11: new 481	java/io/FileOutputStream
    //   14: astore 4
    //   16: aload_1
    //   17: astore_2
    //   18: aload_0
    //   19: astore_3
    //   20: aload 4
    //   22: ldc 67
    //   24: invokespecial 482	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   27: aload_1
    //   28: astore_2
    //   29: aload_0
    //   30: astore_3
    //   31: new 484	com/android/internal/util/FastPrintWriter
    //   34: astore 5
    //   36: aload_1
    //   37: astore_2
    //   38: aload_0
    //   39: astore_3
    //   40: aload 5
    //   42: aload 4
    //   44: invokespecial 487	com/android/internal/util/FastPrintWriter:<init>	(Ljava/io/OutputStream;)V
    //   47: aload 5
    //   49: astore_1
    //   50: aload_1
    //   51: astore_2
    //   52: aload_1
    //   53: astore_3
    //   54: aload_1
    //   55: ldc_w 509
    //   58: invokevirtual 494	java/io/PrintWriter:println	(Ljava/lang/String;)V
    //   61: aload_1
    //   62: invokevirtual 497	java/io/PrintWriter:close	()V
    //   65: goto +24 -> 89
    //   68: astore_1
    //   69: aload_2
    //   70: ifnull +7 -> 77
    //   73: aload_2
    //   74: invokevirtual 497	java/io/PrintWriter:close	()V
    //   77: aload_1
    //   78: athrow
    //   79: astore_1
    //   80: aload_3
    //   81: ifnull +8 -> 89
    //   84: aload_3
    //   85: astore_1
    //   86: goto -25 -> 61
    //   89: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   4	35	0	localObject1	Object
    //   6	56	1	localObject2	Object
    //   68	10	1	localObject3	Object
    //   79	1	1	localException	Exception
    //   85	1	1	localObject4	Object
    //   8	66	2	localObject5	Object
    //   10	75	3	localObject6	Object
    //   14	29	4	localFileOutputStream	java.io.FileOutputStream
    //   34	14	5	localFastPrintWriter	com.android.internal.util.FastPrintWriter
    // Exception table:
    //   from	to	target	type
    //   11	16	68	finally
    //   20	27	68	finally
    //   31	36	68	finally
    //   40	47	68	finally
    //   54	61	68	finally
    //   11	16	79	java/lang/Exception
    //   20	27	79	java/lang/Exception
    //   31	36	79	java/lang/Exception
    //   40	47	79	java/lang/Exception
    //   54	61	79	java/lang/Exception
  }
  
  public static long threadCpuTimeNanos()
  {
    return VMDebug.threadCpuTimeNanos();
  }
  
  public static void waitForDebugger()
  {
    if (!VMDebug.isDebuggingEnabled()) {
      return;
    }
    if (isDebuggerConnected()) {
      return;
    }
    System.out.println("Sending WAIT chunk");
    DdmServer.sendChunk(new Chunk(ChunkHandler.type("WAIT"), new byte[] { 0 }, 0, 1));
    mWaiting = true;
    while (!isDebuggerConnected()) {
      try
      {
        Thread.sleep(200L);
      }
      catch (InterruptedException localInterruptedException1) {}
    }
    mWaiting = false;
    System.out.println("Debugger has connected");
    long l;
    for (;;)
    {
      l = VMDebug.lastDebuggerActivity();
      if (l < 0L)
      {
        System.out.println("debugger detached?");
        return;
      }
      if (l >= 1300L) {
        break;
      }
      System.out.println("waiting for debugger to settle...");
      try
      {
        Thread.sleep(200L);
      }
      catch (InterruptedException localInterruptedException2) {}
    }
    PrintStream localPrintStream = System.out;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("debugger has settled (");
    localStringBuilder.append(l);
    localStringBuilder.append(")");
    localPrintStream.println(localStringBuilder.toString());
  }
  
  public static boolean waitingForDebugger()
  {
    return mWaiting;
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  @Target({java.lang.annotation.ElementType.FIELD})
  public static @interface DebugProperty {}
  
  @Deprecated
  public static class InstructionCount
  {
    public InstructionCount() {}
    
    public boolean collect()
    {
      return false;
    }
    
    public int globalMethodInvocations()
    {
      return 0;
    }
    
    public int globalTotal()
    {
      return 0;
    }
    
    public boolean resetAndStart()
    {
      return false;
    }
  }
  
  public static class MemoryInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<MemoryInfo> CREATOR = new Parcelable.Creator()
    {
      public Debug.MemoryInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Debug.MemoryInfo(paramAnonymousParcel, null);
      }
      
      public Debug.MemoryInfo[] newArray(int paramAnonymousInt)
      {
        return new Debug.MemoryInfo[paramAnonymousInt];
      }
    };
    public static final int HEAP_DALVIK = 1;
    public static final int HEAP_NATIVE = 2;
    public static final int HEAP_UNKNOWN = 0;
    public static final int NUM_CATEGORIES = 9;
    public static final int NUM_DVK_STATS = 14;
    public static final int NUM_OTHER_STATS = 17;
    public static final int OFFSET_PRIVATE_CLEAN = 5;
    public static final int OFFSET_PRIVATE_DIRTY = 3;
    public static final int OFFSET_PSS = 0;
    public static final int OFFSET_RSS = 2;
    public static final int OFFSET_SHARED_CLEAN = 6;
    public static final int OFFSET_SHARED_DIRTY = 4;
    public static final int OFFSET_SWAPPABLE_PSS = 1;
    public static final int OFFSET_SWAPPED_OUT = 7;
    public static final int OFFSET_SWAPPED_OUT_PSS = 8;
    public static final int OTHER_APK = 8;
    public static final int OTHER_ART = 12;
    public static final int OTHER_ART_APP = 29;
    public static final int OTHER_ART_BOOT = 30;
    public static final int OTHER_ASHMEM = 3;
    public static final int OTHER_CURSOR = 2;
    public static final int OTHER_DALVIK_LARGE = 18;
    public static final int OTHER_DALVIK_NON_MOVING = 20;
    public static final int OTHER_DALVIK_NORMAL = 17;
    public static final int OTHER_DALVIK_OTHER = 0;
    public static final int OTHER_DALVIK_OTHER_ACCOUNTING = 22;
    public static final int OTHER_DALVIK_OTHER_CODE_CACHE = 23;
    public static final int OTHER_DALVIK_OTHER_COMPILER_METADATA = 24;
    public static final int OTHER_DALVIK_OTHER_INDIRECT_REFERENCE_TABLE = 25;
    public static final int OTHER_DALVIK_OTHER_LINEARALLOC = 21;
    public static final int OTHER_DALVIK_ZYGOTE = 19;
    public static final int OTHER_DEX = 10;
    public static final int OTHER_DEX_APP_DEX = 27;
    public static final int OTHER_DEX_APP_VDEX = 28;
    public static final int OTHER_DEX_BOOT_VDEX = 26;
    public static final int OTHER_DVK_STAT_ART_END = 13;
    public static final int OTHER_DVK_STAT_ART_START = 12;
    public static final int OTHER_DVK_STAT_DALVIK_END = 3;
    public static final int OTHER_DVK_STAT_DALVIK_OTHER_END = 8;
    public static final int OTHER_DVK_STAT_DALVIK_OTHER_START = 4;
    public static final int OTHER_DVK_STAT_DALVIK_START = 0;
    public static final int OTHER_DVK_STAT_DEX_END = 11;
    public static final int OTHER_DVK_STAT_DEX_START = 9;
    public static final int OTHER_GL = 15;
    public static final int OTHER_GL_DEV = 4;
    public static final int OTHER_GRAPHICS = 14;
    public static final int OTHER_JAR = 7;
    public static final int OTHER_OAT = 11;
    public static final int OTHER_OTHER_MEMTRACK = 16;
    public static final int OTHER_SO = 6;
    public static final int OTHER_STACK = 1;
    public static final int OTHER_TTF = 9;
    public static final int OTHER_UNKNOWN_DEV = 5;
    public static final int OTHER_UNKNOWN_MAP = 13;
    public int dalvikPrivateClean;
    public int dalvikPrivateDirty;
    public int dalvikPss;
    public int dalvikRss;
    public int dalvikSharedClean;
    public int dalvikSharedDirty;
    public int dalvikSwappablePss;
    public int dalvikSwappedOut;
    public int dalvikSwappedOutPss;
    public boolean hasSwappedOutPss;
    public int nativePrivateClean;
    public int nativePrivateDirty;
    public int nativePss;
    public int nativeRss;
    public int nativeSharedClean;
    public int nativeSharedDirty;
    public int nativeSwappablePss;
    public int nativeSwappedOut;
    public int nativeSwappedOutPss;
    public int otherPrivateClean;
    public int otherPrivateDirty;
    public int otherPss;
    public int otherRss;
    public int otherSharedClean;
    public int otherSharedDirty;
    private int[] otherStats = new int['ė'];
    public int otherSwappablePss;
    public int otherSwappedOut;
    public int otherSwappedOutPss;
    
    public MemoryInfo() {}
    
    private MemoryInfo(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public static String getOtherLabel(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return "????";
      case 30: 
        return ".Boot art";
      case 29: 
        return ".App art";
      case 28: 
        return ".App vdex";
      case 27: 
        return ".App dex";
      case 26: 
        return ".Boot vdex";
      case 25: 
        return ".IndirectRef";
      case 24: 
        return ".CompilerMetadata";
      case 23: 
        return ".JITCache";
      case 22: 
        return ".GC";
      case 21: 
        return ".LinearAlloc";
      case 20: 
        return ".NonMoving";
      case 19: 
        return ".Zygote";
      case 18: 
        return ".LOS";
      case 17: 
        return ".Heap";
      case 16: 
        return "Other mtrack";
      case 15: 
        return "GL mtrack";
      case 14: 
        return "EGL mtrack";
      case 13: 
        return "Other mmap";
      case 12: 
        return ".art mmap";
      case 11: 
        return ".oat mmap";
      case 10: 
        return ".dex mmap";
      case 9: 
        return ".ttf mmap";
      case 8: 
        return ".apk mmap";
      case 7: 
        return ".jar mmap";
      case 6: 
        return ".so mmap";
      case 5: 
        return "Other dev";
      case 4: 
        return "Gfx dev";
      case 3: 
        return "Ashmem";
      case 2: 
        return "Cursor";
      case 1: 
        return "Stack";
      }
      return "Dalvik Other";
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String getMemoryStat(String paramString)
    {
      switch (paramString.hashCode())
      {
      default: 
        break;
      case 2069370308: 
        if (paramString.equals("summary.total-swap")) {
          i = 8;
        }
        break;
      case 2016489427: 
        if (paramString.equals("summary.graphics")) {
          i = 4;
        }
        break;
      case 1640306485: 
        if (paramString.equals("summary.code")) {
          i = 2;
        }
        break;
      case 549300599: 
        if (paramString.equals("summary.system")) {
          i = 6;
        }
        break;
      case -675184064: 
        if (paramString.equals("summary.stack")) {
          i = 3;
        }
        break;
      case -1040176230: 
        if (paramString.equals("summary.native-heap")) {
          i = 1;
        }
        break;
      case -1086991874: 
        if (paramString.equals("summary.private-other")) {
          i = 5;
        }
        break;
      case -1318722433: 
        if (paramString.equals("summary.total-pss")) {
          i = 7;
        }
        break;
      case -1629983121: 
        if (paramString.equals("summary.java-heap")) {
          i = 0;
        }
        break;
      }
      int i = -1;
      switch (i)
      {
      default: 
        return null;
      case 8: 
        return Integer.toString(getSummaryTotalSwap());
      case 7: 
        return Integer.toString(getSummaryTotalPss());
      case 6: 
        return Integer.toString(getSummarySystem());
      case 5: 
        return Integer.toString(getSummaryPrivateOther());
      case 4: 
        return Integer.toString(getSummaryGraphics());
      case 3: 
        return Integer.toString(getSummaryStack());
      case 2: 
        return Integer.toString(getSummaryCode());
      case 1: 
        return Integer.toString(getSummaryNativeHeap());
      }
      return Integer.toString(getSummaryJavaHeap());
    }
    
    public Map<String, String> getMemoryStats()
    {
      HashMap localHashMap = new HashMap();
      localHashMap.put("summary.java-heap", Integer.toString(getSummaryJavaHeap()));
      localHashMap.put("summary.native-heap", Integer.toString(getSummaryNativeHeap()));
      localHashMap.put("summary.code", Integer.toString(getSummaryCode()));
      localHashMap.put("summary.stack", Integer.toString(getSummaryStack()));
      localHashMap.put("summary.graphics", Integer.toString(getSummaryGraphics()));
      localHashMap.put("summary.private-other", Integer.toString(getSummaryPrivateOther()));
      localHashMap.put("summary.system", Integer.toString(getSummarySystem()));
      localHashMap.put("summary.total-pss", Integer.toString(getSummaryTotalPss()));
      localHashMap.put("summary.total-swap", Integer.toString(getSummaryTotalSwap()));
      return localHashMap;
    }
    
    public int getOtherPrivate(int paramInt)
    {
      return getOtherPrivateClean(paramInt) + getOtherPrivateDirty(paramInt);
    }
    
    public int getOtherPrivateClean(int paramInt)
    {
      return otherStats[(paramInt * 9 + 5)];
    }
    
    public int getOtherPrivateDirty(int paramInt)
    {
      return otherStats[(paramInt * 9 + 3)];
    }
    
    public int getOtherPss(int paramInt)
    {
      return otherStats[(paramInt * 9 + 0)];
    }
    
    public int getOtherRss(int paramInt)
    {
      return otherStats[(paramInt * 9 + 2)];
    }
    
    public int getOtherSharedClean(int paramInt)
    {
      return otherStats[(paramInt * 9 + 6)];
    }
    
    public int getOtherSharedDirty(int paramInt)
    {
      return otherStats[(paramInt * 9 + 4)];
    }
    
    public int getOtherSwappablePss(int paramInt)
    {
      return otherStats[(paramInt * 9 + 1)];
    }
    
    public int getOtherSwappedOut(int paramInt)
    {
      return otherStats[(paramInt * 9 + 7)];
    }
    
    public int getOtherSwappedOutPss(int paramInt)
    {
      return otherStats[(paramInt * 9 + 8)];
    }
    
    public int getSummaryCode()
    {
      return getOtherPrivate(6) + getOtherPrivate(7) + getOtherPrivate(8) + getOtherPrivate(9) + getOtherPrivate(10) + getOtherPrivate(11);
    }
    
    public int getSummaryGraphics()
    {
      return getOtherPrivate(4) + getOtherPrivate(14) + getOtherPrivate(15);
    }
    
    public int getSummaryJavaHeap()
    {
      return dalvikPrivateDirty + getOtherPrivate(12);
    }
    
    public int getSummaryNativeHeap()
    {
      return nativePrivateDirty;
    }
    
    public int getSummaryPrivateOther()
    {
      return getTotalPrivateClean() + getTotalPrivateDirty() - getSummaryJavaHeap() - getSummaryNativeHeap() - getSummaryCode() - getSummaryStack() - getSummaryGraphics();
    }
    
    public int getSummaryStack()
    {
      return getOtherPrivateDirty(1);
    }
    
    public int getSummarySystem()
    {
      return getTotalPss() - getTotalPrivateClean() - getTotalPrivateDirty();
    }
    
    public int getSummaryTotalPss()
    {
      return getTotalPss();
    }
    
    public int getSummaryTotalSwap()
    {
      return getTotalSwappedOut();
    }
    
    public int getSummaryTotalSwapPss()
    {
      return getTotalSwappedOutPss();
    }
    
    public int getTotalPrivateClean()
    {
      return dalvikPrivateClean + nativePrivateClean + otherPrivateClean;
    }
    
    public int getTotalPrivateDirty()
    {
      return dalvikPrivateDirty + nativePrivateDirty + otherPrivateDirty;
    }
    
    public int getTotalPss()
    {
      return dalvikPss + nativePss + otherPss + getTotalSwappedOutPss();
    }
    
    public int getTotalRss()
    {
      return dalvikRss + nativeRss + otherRss;
    }
    
    public int getTotalSharedClean()
    {
      return dalvikSharedClean + nativeSharedClean + otherSharedClean;
    }
    
    public int getTotalSharedDirty()
    {
      return dalvikSharedDirty + nativeSharedDirty + otherSharedDirty;
    }
    
    public int getTotalSwappablePss()
    {
      return dalvikSwappablePss + nativeSwappablePss + otherSwappablePss;
    }
    
    public int getTotalSwappedOut()
    {
      return dalvikSwappedOut + nativeSwappedOut + otherSwappedOut;
    }
    
    public int getTotalSwappedOutPss()
    {
      return dalvikSwappedOutPss + nativeSwappedOutPss + otherSwappedOutPss;
    }
    
    public int getTotalUss()
    {
      return dalvikPrivateClean + dalvikPrivateDirty + nativePrivateClean + nativePrivateDirty + otherPrivateClean + otherPrivateDirty;
    }
    
    public boolean hasSwappedOutPss()
    {
      return hasSwappedOutPss;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      dalvikPss = paramParcel.readInt();
      dalvikSwappablePss = paramParcel.readInt();
      dalvikRss = paramParcel.readInt();
      dalvikPrivateDirty = paramParcel.readInt();
      dalvikSharedDirty = paramParcel.readInt();
      dalvikPrivateClean = paramParcel.readInt();
      dalvikSharedClean = paramParcel.readInt();
      dalvikSwappedOut = paramParcel.readInt();
      dalvikSwappedOutPss = paramParcel.readInt();
      nativePss = paramParcel.readInt();
      nativeSwappablePss = paramParcel.readInt();
      nativeRss = paramParcel.readInt();
      nativePrivateDirty = paramParcel.readInt();
      nativeSharedDirty = paramParcel.readInt();
      nativePrivateClean = paramParcel.readInt();
      nativeSharedClean = paramParcel.readInt();
      nativeSwappedOut = paramParcel.readInt();
      nativeSwappedOutPss = paramParcel.readInt();
      otherPss = paramParcel.readInt();
      otherSwappablePss = paramParcel.readInt();
      otherRss = paramParcel.readInt();
      otherPrivateDirty = paramParcel.readInt();
      otherSharedDirty = paramParcel.readInt();
      otherPrivateClean = paramParcel.readInt();
      otherSharedClean = paramParcel.readInt();
      otherSwappedOut = paramParcel.readInt();
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      hasSwappedOutPss = bool;
      otherSwappedOutPss = paramParcel.readInt();
      otherStats = paramParcel.createIntArray();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(dalvikPss);
      paramParcel.writeInt(dalvikSwappablePss);
      paramParcel.writeInt(dalvikRss);
      paramParcel.writeInt(dalvikPrivateDirty);
      paramParcel.writeInt(dalvikSharedDirty);
      paramParcel.writeInt(dalvikPrivateClean);
      paramParcel.writeInt(dalvikSharedClean);
      paramParcel.writeInt(dalvikSwappedOut);
      paramParcel.writeInt(dalvikSwappedOutPss);
      paramParcel.writeInt(nativePss);
      paramParcel.writeInt(nativeSwappablePss);
      paramParcel.writeInt(nativeRss);
      paramParcel.writeInt(nativePrivateDirty);
      paramParcel.writeInt(nativeSharedDirty);
      paramParcel.writeInt(nativePrivateClean);
      paramParcel.writeInt(nativeSharedClean);
      paramParcel.writeInt(nativeSwappedOut);
      paramParcel.writeInt(nativeSwappedOutPss);
      paramParcel.writeInt(otherPss);
      paramParcel.writeInt(otherSwappablePss);
      paramParcel.writeInt(otherRss);
      paramParcel.writeInt(otherPrivateDirty);
      paramParcel.writeInt(otherSharedDirty);
      paramParcel.writeInt(otherPrivateClean);
      paramParcel.writeInt(otherSharedClean);
      paramParcel.writeInt(otherSwappedOut);
      paramParcel.writeInt(hasSwappedOutPss);
      paramParcel.writeInt(otherSwappedOutPss);
      paramParcel.writeIntArray(otherStats);
    }
  }
}
