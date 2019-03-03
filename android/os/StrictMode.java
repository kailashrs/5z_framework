package android.os;

import android.animation.ValueAnimator;
import android.app.ActivityThread;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.strictmode.CleartextNetworkViolation;
import android.os.strictmode.ContentUriWithoutPermissionViolation;
import android.os.strictmode.CustomViolation;
import android.os.strictmode.DiskReadViolation;
import android.os.strictmode.DiskWriteViolation;
import android.os.strictmode.FileUriExposedViolation;
import android.os.strictmode.InstanceCountViolation;
import android.os.strictmode.IntentReceiverLeakedViolation;
import android.os.strictmode.LeakedClosableViolation;
import android.os.strictmode.NetworkViolation;
import android.os.strictmode.NonSdkApiUsedViolation;
import android.os.strictmode.ResourceMismatchViolation;
import android.os.strictmode.ServiceConnectionLeakedViolation;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.os.strictmode.UnbufferedIoViolation;
import android.os.strictmode.UntaggedSocketViolation;
import android.os.strictmode.Violation;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Printer;
import android.util.Singleton;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.HexDump;
import dalvik.system.BlockGuard;
import dalvik.system.BlockGuard.Policy;
import dalvik.system.CloseGuard;
import dalvik.system.CloseGuard.Reporter;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public final class StrictMode
{
  private static final int ALL_THREAD_DETECT_BITS = 63;
  private static final int ALL_VM_DETECT_BITS = -1073676544;
  private static final String AMAX_STRICT_MODE = "debug.asus.strictmode";
  private static final int AMAX_STRICT_MODE_DISABLE = 0;
  private static final int AMAX_STRICT_MODE_THREAD = 1;
  private static final int AMAX_STRICT_MODE_THREAD_VM = 3;
  private static final int AMAX_STRICT_MODE_VM = 2;
  public static final String CLEARTEXT_DETECTED_MSG = "Detected cleartext network traffic from UID ";
  private static final String CLEARTEXT_PROPERTY = "persist.sys.strictmode.clear";
  public static final int DETECT_CUSTOM = 8;
  public static final int DETECT_DISK_READ = 2;
  public static final int DETECT_DISK_WRITE = 1;
  public static final int DETECT_NETWORK = 4;
  public static final int DETECT_RESOURCE_MISMATCH = 16;
  public static final int DETECT_UNBUFFERED_IO = 32;
  public static final int DETECT_VM_ACTIVITY_LEAKS = 1024;
  public static final int DETECT_VM_CLEARTEXT_NETWORK = 16384;
  public static final int DETECT_VM_CLOSABLE_LEAKS = 512;
  public static final int DETECT_VM_CONTENT_URI_WITHOUT_PERMISSION = 32768;
  public static final int DETECT_VM_CURSOR_LEAKS = 256;
  public static final int DETECT_VM_FILE_URI_EXPOSURE = 8192;
  public static final int DETECT_VM_INSTANCE_LEAKS = 2048;
  private static final int DETECT_VM_MEMORY = 536870912;
  public static final int DETECT_VM_NON_SDK_API_USAGE = 1073741824;
  public static final int DETECT_VM_REGISTRATION_LEAKS = 4096;
  public static final int DETECT_VM_UNTAGGED_SOCKET = Integer.MIN_VALUE;
  private static final boolean DISABLE = false;
  public static final String DISABLE_PROPERTY = "persist.sys.strictmode.disable";
  private static final HashMap<Class, Integer> EMPTY_CLASS_LIMIT_MAP;
  private static final ViolationLogger LOGCAT_LOGGER;
  private static final boolean LOG_V = Log.isLoggable("StrictMode", 2);
  private static final int MAX_OFFENSES_PER_LOOP = 10;
  private static final int MAX_SPAN_TAGS = 20;
  private static final long MIN_DIALOG_INTERVAL_MS = 30000L;
  private static final long MIN_LOG_INTERVAL_MS = 1000L;
  private static final long MIN_VM_INTERVAL_MS = 1000L;
  public static final int NETWORK_POLICY_ACCEPT = 0;
  public static final int NETWORK_POLICY_LOG = 1;
  public static final int NETWORK_POLICY_REJECT = 2;
  private static final Span NO_OP_SPAN;
  public static final int PENALTY_DEATH = 262144;
  public static final int PENALTY_DEATH_ON_CLEARTEXT_NETWORK = 33554432;
  public static final int PENALTY_DEATH_ON_FILE_URI_EXPOSURE = 67108864;
  public static final int PENALTY_DEATH_ON_NETWORK = 16777216;
  public static final int PENALTY_DIALOG = 131072;
  public static final int PENALTY_DROPBOX = 2097152;
  public static final int PENALTY_FLASH = 1048576;
  public static final int PENALTY_GATHER = 4194304;
  public static final int PENALTY_LOG = 65536;
  private static final String TAG = "StrictMode";
  private static final ThreadLocal<AndroidBlockGuardPolicy> THREAD_ANDROID_POLICY;
  private static final ThreadLocal<Handler> THREAD_HANDLER;
  private static final int THREAD_PENALTY_MASK = 24576000;
  public static final String VISUAL_PROPERTY = "persist.sys.strictmode.visual";
  private static final int VM_PENALTY_MASK = 103088128;
  private static final ThreadLocal<ArrayList<ViolationInfo>> gatheredViolations;
  private static boolean sDisableMonitorMemory;
  private static final AtomicInteger sDropboxCallsInFlight;
  @GuardedBy("StrictMode.class")
  private static final HashMap<Class, Integer> sExpectedActivityInstanceCount = new HashMap();
  private static boolean sIsIdlerMemoryRegistered;
  private static boolean sIsIdlerRegistered;
  private static long sLastInstanceCountCheckMillis;
  private static long sLastMemoryBudgetCheckMillis;
  private static final HashMap<Integer, Long> sLastVmViolationTime;
  private static volatile ViolationLogger sLogger;
  private static Budget sMemoryBudget;
  private static final Consumer<String> sNonSdkApiUsageConsumer;
  private static final MessageQueue.IdleHandler sProcessIdleHandler;
  private static final MessageQueue.IdleHandler sProcessIdleMemoryHandler;
  private static final ThreadLocal<ThreadSpanState> sThisThreadSpanState;
  private static final ThreadLocal<Executor> sThreadViolationExecutor;
  private static final ThreadLocal<OnThreadViolationListener> sThreadViolationListener;
  private static volatile VmPolicy sVmPolicy;
  private static Singleton<IWindowManager> sWindowManager;
  private static final ThreadLocal<ArrayList<ViolationInfo>> violationsBeingTimed;
  
  static
  {
    EMPTY_CLASS_LIMIT_MAP = new HashMap();
    sVmPolicy = VmPolicy.LAX;
    LOGCAT_LOGGER = _..Lambda.StrictMode.1yH8AK0bTwVwZOb9x8HoiSBdzr0.INSTANCE;
    sLogger = LOGCAT_LOGGER;
    sThreadViolationListener = new ThreadLocal();
    sThreadViolationExecutor = new ThreadLocal();
    sDropboxCallsInFlight = new AtomicInteger(0);
    sNonSdkApiUsageConsumer = _..Lambda.StrictMode.lu9ekkHJ2HMz0jd3F8K8MnhenxQ.INSTANCE;
    gatheredViolations = new ThreadLocal()
    {
      protected ArrayList<StrictMode.ViolationInfo> initialValue()
      {
        return null;
      }
    };
    violationsBeingTimed = new ThreadLocal()
    {
      protected ArrayList<StrictMode.ViolationInfo> initialValue()
      {
        return new ArrayList();
      }
    };
    THREAD_HANDLER = new ThreadLocal()
    {
      protected Handler initialValue()
      {
        return new Handler();
      }
    };
    THREAD_ANDROID_POLICY = new ThreadLocal()
    {
      protected StrictMode.AndroidBlockGuardPolicy initialValue()
      {
        return new StrictMode.AndroidBlockGuardPolicy(0);
      }
    };
    sLastInstanceCountCheckMillis = 0L;
    sIsIdlerRegistered = false;
    sProcessIdleHandler = new MessageQueue.IdleHandler()
    {
      public boolean queueIdle()
      {
        long l = SystemClock.uptimeMillis();
        if (l - StrictMode.sLastInstanceCountCheckMillis > 30000L)
        {
          StrictMode.access$1802(l);
          StrictMode.conditionallyCheckInstanceCounts();
        }
        return true;
      }
    };
    sMemoryBudget = null;
    sLastMemoryBudgetCheckMillis = 0L;
    sIsIdlerMemoryRegistered = false;
    sDisableMonitorMemory = Build.IS_USER;
    sProcessIdleMemoryHandler = new MessageQueue.IdleHandler()
    {
      public boolean queueIdle()
      {
        long l = SystemClock.uptimeMillis();
        if (l - StrictMode.sLastMemoryBudgetCheckMillis > 3600000L)
        {
          StrictMode.access$2000();
          if (StrictMode.sLastMemoryBudgetCheckMillis != 0L) {
            StrictMode.access$2100();
          }
          StrictMode.access$1902(l);
        }
        return true;
      }
    };
    sLastVmViolationTime = new HashMap();
    NO_OP_SPAN = new Span()
    {
      public void finish() {}
    };
    sThisThreadSpanState = new ThreadLocal()
    {
      protected StrictMode.ThreadSpanState initialValue()
      {
        return new StrictMode.ThreadSpanState(null);
      }
    };
    sWindowManager = new Singleton()
    {
      protected IWindowManager create()
      {
        return IWindowManager.Stub.asInterface(ServiceManager.getService("window"));
      }
    };
  }
  
  private StrictMode() {}
  
  public static ThreadPolicy allowThreadDiskReads()
  {
    return new ThreadPolicy(allowThreadDiskReadsMask(), (OnThreadViolationListener)sThreadViolationListener.get(), (Executor)sThreadViolationExecutor.get(), null);
  }
  
  public static int allowThreadDiskReadsMask()
  {
    int i = getThreadPolicyMask();
    int j = i & 0xFFFFFFFD;
    if (j != i) {
      setThreadPolicyMask(j);
    }
    return i;
  }
  
  public static ThreadPolicy allowThreadDiskWrites()
  {
    return new ThreadPolicy(allowThreadDiskWritesMask(), (OnThreadViolationListener)sThreadViolationListener.get(), (Executor)sThreadViolationExecutor.get(), null);
  }
  
  public static int allowThreadDiskWritesMask()
  {
    int i = getThreadPolicyMask();
    int j = i & 0xFFFFFFFC;
    if (j != i) {
      setThreadPolicyMask(j);
    }
    return i;
  }
  
  private static ThreadPolicy allowThreadViolations()
  {
    ThreadPolicy localThreadPolicy = getThreadPolicy();
    setThreadPolicyMask(0);
    return localThreadPolicy;
  }
  
  private static VmPolicy allowVmViolations()
  {
    VmPolicy localVmPolicy = getVmPolicy();
    sVmPolicy = VmPolicy.LAX;
    return localVmPolicy;
  }
  
  static void clearGatheredViolations()
  {
    gatheredViolations.set(null);
  }
  
  public static void conditionallyCheckInstanceCounts()
  {
    VmPolicy localVmPolicy = getVmPolicy();
    int i = classInstanceLimit.size();
    if (i == 0) {
      return;
    }
    System.gc();
    System.runFinalization();
    System.gc();
    Class[] arrayOfClass = (Class[])classInstanceLimit.keySet().toArray(new Class[i]);
    i = 0;
    long[] arrayOfLong = VMDebug.countInstancesOfClasses(arrayOfClass, false);
    while (i < arrayOfClass.length)
    {
      Class localClass = arrayOfClass[i];
      int j = ((Integer)classInstanceLimit.get(localClass)).intValue();
      long l = arrayOfLong[i];
      if (l > j) {
        onVmPolicyViolation(new InstanceCountViolation(localClass, l, j));
      }
      i++;
    }
  }
  
  private static void conditionallyCheckMemoryBudget()
  {
    if ((sMemoryBudget != null) && (!sMemoryBudget.invalid()))
    {
      System.gc();
      System.runFinalization();
      System.gc();
      float f = (float)Debug.getPss(Process.myPid(), new long[2], null);
      if (f > sMemoryBudgethardKB) {
        getVmPolicy().dumpHeapForOverBudget(sMemoryBudgetpackageName, f, sMemoryBudgethardKB);
      }
      return;
    }
    Looper localLooper = Looper.getMainLooper();
    if (localLooper != null)
    {
      mQueue.removeIdleHandler(sProcessIdleMemoryHandler);
      sIsIdlerMemoryRegistered = false;
      sDisableMonitorMemory = true;
      if (LOG_V) {
        Log.d("StrictMode", "disable detect memory idle handler.");
      }
    }
  }
  
  public static void decrementExpectedActivityCount(Class paramClass)
  {
    if (paramClass == null) {
      return;
    }
    try
    {
      if ((sVmPolicymask & 0x400) == 0) {
        return;
      }
      Integer localInteger = (Integer)sExpectedActivityInstanceCount.get(paramClass);
      int i;
      if ((localInteger != null) && (localInteger.intValue() != 0)) {
        i = localInteger.intValue() - 1;
      } else {
        i = 0;
      }
      if (i == 0) {
        sExpectedActivityInstanceCount.remove(paramClass);
      } else {
        sExpectedActivityInstanceCount.put(paramClass, Integer.valueOf(i));
      }
      i++;
      if (InstanceTracker.getInstanceCount(paramClass) <= i) {
        return;
      }
      System.gc();
      System.runFinalization();
      System.gc();
      long l = VMDebug.countInstancesOfClass(paramClass, false);
      if (l > i) {
        onVmPolicyViolation(new InstanceCountViolation(paramClass, l, i));
      }
      return;
    }
    finally {}
  }
  
  public static void disableDeathOnFileUriExposure()
  {
    sVmPolicy = new VmPolicy(0xFBFFDFFF & sVmPolicymask, sVmPolicyclassInstanceLimit, sVmPolicymListener, sVmPolicymCallbackExecutor, null);
  }
  
  private static void dropboxViolationAsync(int paramInt, ViolationInfo paramViolationInfo)
  {
    int i = sDropboxCallsInFlight.incrementAndGet();
    if (i > 20)
    {
      sDropboxCallsInFlight.decrementAndGet();
      return;
    }
    if (LOG_V)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Dropboxing async; in-flight=");
      localStringBuilder.append(i);
      Log.d("StrictMode", localStringBuilder.toString());
    }
    BackgroundThread.getHandler().post(new _..Lambda.StrictMode.yZJXPvy2veRNA_xL_SWdXzX_OLg(paramInt, paramViolationInfo));
  }
  
  public static void enableDeathOnFileUriExposure()
  {
    sVmPolicy = new VmPolicy(0x4000000 | sVmPolicymask | 0x2000, sVmPolicyclassInstanceLimit, sVmPolicymListener, sVmPolicymCallbackExecutor, null);
  }
  
  public static void enableDefaults()
  {
    setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
    setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
  }
  
  public static Span enterCriticalSpan(String paramString)
  {
    if (Build.IS_USER) {
      return NO_OP_SPAN;
    }
    if ((paramString != null) && (!paramString.isEmpty())) {
      synchronized ((ThreadSpanState)sThisThreadSpanState.get())
      {
        Span localSpan;
        if (mFreeListHead != null)
        {
          localSpan = mFreeListHead;
          mFreeListHead = mNext;
          mFreeListSize -= 1;
        }
        else
        {
          localSpan = new Span(???);
        }
        Span.access$2402(localSpan, paramString);
        Span.access$2502(localSpan, SystemClock.uptimeMillis());
        Span.access$2302(localSpan, mActiveHead);
        Span.access$2602(localSpan, null);
        mActiveHead = localSpan;
        mActiveSize += 1;
        if (mNext != null) {
          Span.access$2602(mNext, localSpan);
        }
        if (LOG_V)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Span enter=");
          localStringBuilder.append(paramString);
          localStringBuilder.append("; size=");
          localStringBuilder.append(mActiveSize);
          Log.d("StrictMode", localStringBuilder.toString());
        }
        return localSpan;
      }
    }
    throw new IllegalArgumentException("name must be non-null and non-empty");
  }
  
  public static ThreadPolicy getThreadPolicy()
  {
    return new ThreadPolicy(getThreadPolicyMask(), (OnThreadViolationListener)sThreadViolationListener.get(), (Executor)sThreadViolationExecutor.get(), null);
  }
  
  public static int getThreadPolicyMask()
  {
    return BlockGuard.getThreadPolicy().getPolicyMask();
  }
  
  public static VmPolicy getVmPolicy()
  {
    try
    {
      VmPolicy localVmPolicy = sVmPolicy;
      return localVmPolicy;
    }
    finally {}
  }
  
  /* Error */
  private static void handleApplicationStrictModeViolation(int paramInt, ViolationInfo paramViolationInfo)
  {
    // Byte code:
    //   0: invokestatic 384	android/os/StrictMode:getThreadPolicyMask	()I
    //   3: istore_2
    //   4: iconst_0
    //   5: invokestatic 387	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   8: invokestatic 682	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   11: astore_3
    //   12: aload_3
    //   13: ifnonnull +15 -> 28
    //   16: ldc -87
    //   18: ldc_w 684
    //   21: invokestatic 687	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   24: pop
    //   25: goto +42 -> 67
    //   28: aload_3
    //   29: invokestatic 693	com/android/internal/os/RuntimeInit:getApplicationObject	()Landroid/os/IBinder;
    //   32: iload_0
    //   33: aload_1
    //   34: invokeinterface 698 4 0
    //   39: goto +28 -> 67
    //   42: astore_1
    //   43: goto +29 -> 72
    //   46: astore_1
    //   47: aload_1
    //   48: instanceof 700
    //   51: ifeq +6 -> 57
    //   54: goto +13 -> 67
    //   57: ldc -87
    //   59: ldc_w 702
    //   62: aload_1
    //   63: invokestatic 706	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   66: pop
    //   67: iload_2
    //   68: invokestatic 387	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   71: return
    //   72: iload_2
    //   73: invokestatic 387	android/os/StrictMode:setThreadPolicyMask	(I)V
    //   76: aload_1
    //   77: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	78	0	paramInt	int
    //   0	78	1	paramViolationInfo	ViolationInfo
    //   3	70	2	i	int
    //   11	18	3	localIActivityManager	android.app.IActivityManager
    // Exception table:
    //   from	to	target	type
    //   4	12	42	finally
    //   16	25	42	finally
    //   28	39	42	finally
    //   47	54	42	finally
    //   57	67	42	finally
    //   4	12	46	android/os/RemoteException
    //   16	25	46	android/os/RemoteException
    //   28	39	46	android/os/RemoteException
  }
  
  static boolean hasGatheredViolations()
  {
    boolean bool;
    if (gatheredViolations.get() != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static void incrementExpectedActivityCount(Class paramClass)
  {
    if (paramClass == null) {
      return;
    }
    try
    {
      if ((sVmPolicymask & 0x400) == 0) {
        return;
      }
      Integer localInteger = (Integer)sExpectedActivityInstanceCount.get(paramClass);
      int i = 1;
      if (localInteger != null) {
        i = 1 + localInteger.intValue();
      }
      sExpectedActivityInstanceCount.put(paramClass, Integer.valueOf(i));
      return;
    }
    finally {}
  }
  
  public static void initThreadDefaults(ApplicationInfo paramApplicationInfo)
  {
    int i = readAmaxDetect();
    StrictMode.ThreadPolicy.Builder localBuilder = new StrictMode.ThreadPolicy.Builder();
    int j;
    if (paramApplicationInfo != null) {
      j = targetSdkVersion;
    } else {
      j = 10000;
    }
    if (j >= 11)
    {
      localBuilder.detectNetwork();
      localBuilder.penaltyDeathOnNetwork();
    }
    if ((!Build.IS_USER) && (!SystemProperties.getBoolean("persist.sys.strictmode.disable", false))) {
      if (Build.IS_USERDEBUG)
      {
        if (isBundledSystemApp(paramApplicationInfo))
        {
          localBuilder.detectAll();
          localBuilder.penaltyDropBox();
          if (SystemProperties.getBoolean("persist.sys.strictmode.visual", false)) {
            localBuilder.penaltyFlashScreen();
          }
          if (isAmaxDetectingThread(i)) {
            localBuilder.penaltyLog();
          }
        }
      }
      else if ((Build.IS_ENG) && (isBundledSystemApp(paramApplicationInfo)))
      {
        localBuilder.detectAll();
        localBuilder.penaltyDropBox();
        localBuilder.penaltyLog();
        localBuilder.penaltyFlashScreen();
      }
    }
    setThreadPolicy(localBuilder.build());
  }
  
  public static void initVmDefaults(ApplicationInfo paramApplicationInfo)
  {
    int i = readAmaxDetect();
    StrictMode.VmPolicy.Builder localBuilder = new StrictMode.VmPolicy.Builder();
    int j;
    if (paramApplicationInfo != null) {
      j = targetSdkVersion;
    } else {
      j = 10000;
    }
    if (j >= 24)
    {
      localBuilder.detectFileUriExposure();
      localBuilder.penaltyDeathOnFileUriExposure();
    }
    if ((!Build.IS_USER) && (!SystemProperties.getBoolean("persist.sys.strictmode.disable", false))) {
      if (Build.IS_USERDEBUG)
      {
        if (isBundledSystemApp(paramApplicationInfo))
        {
          localBuilder.detectAll();
          localBuilder.permitActivityLeaks();
          localBuilder.penaltyDropBox();
          if (isAmaxDetectingVM(i))
          {
            localBuilder.detectActivityLeaks();
            localBuilder.detectVmMemory();
            localBuilder.penaltyLog();
          }
        }
      }
      else if ((Build.IS_ENG) && (isBundledSystemApp(paramApplicationInfo)))
      {
        localBuilder.detectAll();
        localBuilder.penaltyDropBox();
        localBuilder.penaltyLog();
        if (isAmaxDetectingVM(i)) {
          localBuilder.detectVmMemory();
        }
      }
    }
    setVmPolicy(localBuilder.build());
  }
  
  private static boolean isAmaxDetectingThread(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1) {
      if (paramInt == 3) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
    }
    return bool2;
  }
  
  private static boolean isAmaxDetectingVM(int paramInt)
  {
    boolean bool;
    if ((paramInt != 2) && (paramInt != 3)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isBundledSystemApp(ApplicationInfo paramApplicationInfo)
  {
    if ((paramApplicationInfo != null) && (packageName != null))
    {
      if (paramApplicationInfo.isSystemApp()) {
        if ((!packageName.equals("com.android.vending")) && (!packageName.equals("com.android.chrome")))
        {
          if (packageName.equals("com.android.phone")) {
            return false;
          }
          if ((packageName.equals("android")) || (packageName.startsWith("android.")) || (packageName.startsWith("com.android."))) {
            return true;
          }
        }
        else
        {
          return false;
        }
      }
      return false;
    }
    return true;
  }
  
  private static boolean isStrictModeEnable()
  {
    return SystemProperties.getBoolean("persist.sys.strictmode.visual", false);
  }
  
  /* Error */
  private static void loadBudget()
  {
    // Byte code:
    //   0: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   3: ifnull +4 -> 7
    //   6: return
    //   7: invokestatic 834	android/app/ActivityThread:currentActivityThread	()Landroid/app/ActivityThread;
    //   10: invokevirtual 838	android/app/ActivityThread:getApplication	()Landroid/app/Application;
    //   13: astore_0
    //   14: aload_0
    //   15: invokevirtual 843	android/app/Application:getPackageName	()Ljava/lang/String;
    //   18: astore_1
    //   19: ldc_w 845
    //   22: astore_2
    //   23: ldc_w 847
    //   26: aload_1
    //   27: invokevirtual 781	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   30: ifne +15 -> 45
    //   33: aload_2
    //   34: astore_3
    //   35: ldc_w 849
    //   38: aload_1
    //   39: invokevirtual 781	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   42: ifeq +35 -> 77
    //   45: ldc_w 851
    //   48: invokestatic 854	android/os/SystemProperties:get	(Ljava/lang/String;)Ljava/lang/String;
    //   51: astore 4
    //   53: aload_2
    //   54: astore_3
    //   55: aload 4
    //   57: ifnull +20 -> 77
    //   60: aload_2
    //   61: astore_3
    //   62: ldc_w 856
    //   65: aload 4
    //   67: invokevirtual 781	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   70: ifeq +7 -> 77
    //   73: ldc_w 858
    //   76: astore_3
    //   77: aload_0
    //   78: invokevirtual 862	android/app/Application:getContentResolver	()Landroid/content/ContentResolver;
    //   81: astore_0
    //   82: ldc_w 864
    //   85: invokestatic 870	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   88: astore 4
    //   90: new 548	java/lang/StringBuilder
    //   93: astore_2
    //   94: aload_2
    //   95: invokespecial 549	java/lang/StringBuilder:<init>	()V
    //   98: aload_2
    //   99: aload_1
    //   100: invokevirtual 555	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   103: pop
    //   104: aload_2
    //   105: aload_3
    //   106: invokevirtual 555	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: pop
    //   110: aload_0
    //   111: aload 4
    //   113: ldc_w 872
    //   116: aload_2
    //   117: invokevirtual 562	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   120: aconst_null
    //   121: invokevirtual 878	android/content/ContentResolver:call	(Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;Landroid/os/Bundle;)Landroid/os/Bundle;
    //   124: astore_2
    //   125: aload_2
    //   126: ifnull +33 -> 159
    //   129: new 30	android/os/StrictMode$Budget
    //   132: astore_3
    //   133: aload_3
    //   134: aload_1
    //   135: aload_2
    //   136: ldc_w 880
    //   139: invokevirtual 886	android/os/Bundle:getFloat	(Ljava/lang/String;)F
    //   142: aload_2
    //   143: ldc_w 888
    //   146: invokevirtual 886	android/os/Bundle:getFloat	(Ljava/lang/String;)F
    //   149: invokespecial 890	android/os/StrictMode$Budget:<init>	(Ljava/lang/String;FF)V
    //   152: aload_3
    //   153: putstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   156: goto +33 -> 189
    //   159: getstatic 227	android/os/StrictMode:LOG_V	Z
    //   162: ifeq +27 -> 189
    //   165: ldc -87
    //   167: ldc_w 892
    //   170: iconst_2
    //   171: anewarray 4	java/lang/Object
    //   174: dup
    //   175: iconst_0
    //   176: aload_1
    //   177: aastore
    //   178: dup
    //   179: iconst_1
    //   180: aload_3
    //   181: aastore
    //   182: invokestatic 896	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   185: invokestatic 503	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   188: pop
    //   189: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   192: ifnonnull +13 -> 205
    //   195: new 30	android/os/StrictMode$Budget
    //   198: dup
    //   199: invokespecial 897	android/os/StrictMode$Budget:<init>	()V
    //   202: putstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   205: getstatic 227	android/os/StrictMode:LOG_V	Z
    //   208: ifeq +108 -> 316
    //   211: iconst_2
    //   212: anewarray 4	java/lang/Object
    //   215: astore_3
    //   216: aload_3
    //   217: iconst_0
    //   218: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   221: getfield 900	android/os/StrictMode$Budget:avgKB	F
    //   224: invokestatic 905	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   227: aastore
    //   228: aload_3
    //   229: iconst_1
    //   230: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   233: getfield 474	android/os/StrictMode$Budget:hardKB	F
    //   236: invokestatic 905	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   239: aastore
    //   240: goto +63 -> 303
    //   243: astore_3
    //   244: goto +73 -> 317
    //   247: astore_3
    //   248: aload_3
    //   249: invokevirtual 908	java/lang/Exception:printStackTrace	()V
    //   252: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   255: ifnonnull +13 -> 268
    //   258: new 30	android/os/StrictMode$Budget
    //   261: dup
    //   262: invokespecial 897	android/os/StrictMode$Budget:<init>	()V
    //   265: putstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   268: getstatic 227	android/os/StrictMode:LOG_V	Z
    //   271: ifeq +45 -> 316
    //   274: iconst_2
    //   275: anewarray 4	java/lang/Object
    //   278: astore_3
    //   279: aload_3
    //   280: iconst_0
    //   281: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   284: getfield 900	android/os/StrictMode$Budget:avgKB	F
    //   287: invokestatic 905	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   290: aastore
    //   291: aload_3
    //   292: iconst_1
    //   293: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   296: getfield 474	android/os/StrictMode$Budget:hardKB	F
    //   299: invokestatic 905	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   302: aastore
    //   303: ldc -87
    //   305: ldc_w 910
    //   308: aload_3
    //   309: invokestatic 896	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   312: invokestatic 503	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   315: pop
    //   316: return
    //   317: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   320: ifnonnull +13 -> 333
    //   323: new 30	android/os/StrictMode$Budget
    //   326: dup
    //   327: invokespecial 897	android/os/StrictMode$Budget:<init>	()V
    //   330: putstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   333: getstatic 227	android/os/StrictMode:LOG_V	Z
    //   336: ifeq +43 -> 379
    //   339: ldc -87
    //   341: ldc_w 910
    //   344: iconst_2
    //   345: anewarray 4	java/lang/Object
    //   348: dup
    //   349: iconst_0
    //   350: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   353: getfield 900	android/os/StrictMode$Budget:avgKB	F
    //   356: invokestatic 905	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   359: aastore
    //   360: dup
    //   361: iconst_1
    //   362: getstatic 291	android/os/StrictMode:sMemoryBudget	Landroid/os/StrictMode$Budget;
    //   365: getfield 474	android/os/StrictMode$Budget:hardKB	F
    //   368: invokestatic 905	java/lang/Float:valueOf	(F)Ljava/lang/Float;
    //   371: aastore
    //   372: invokestatic 896	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   375: invokestatic 503	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   378: pop
    //   379: aload_3
    //   380: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   13	98	0	localObject1	Object
    //   18	159	1	str	String
    //   22	121	2	localObject2	Object
    //   34	195	3	localObject3	Object
    //   243	1	3	localObject4	Object
    //   247	2	3	localException	Exception
    //   278	102	3	arrayOfObject	Object[]
    //   51	61	4	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   7	19	243	finally
    //   23	33	243	finally
    //   35	45	243	finally
    //   45	53	243	finally
    //   62	73	243	finally
    //   77	125	243	finally
    //   129	156	243	finally
    //   159	189	243	finally
    //   248	252	243	finally
    //   7	19	247	java/lang/Exception
    //   23	33	247	java/lang/Exception
    //   35	45	247	java/lang/Exception
    //   45	53	247	java/lang/Exception
    //   62	73	247	java/lang/Exception
    //   77	125	247	java/lang/Exception
    //   129	156	247	java/lang/Exception
    //   159	189	247	java/lang/Exception
  }
  
  public static void noteDiskRead()
  {
    BlockGuard.Policy localPolicy = BlockGuard.getThreadPolicy();
    if (!(localPolicy instanceof AndroidBlockGuardPolicy)) {
      return;
    }
    localPolicy.onReadFromDisk();
  }
  
  public static void noteDiskWrite()
  {
    BlockGuard.Policy localPolicy = BlockGuard.getThreadPolicy();
    if (!(localPolicy instanceof AndroidBlockGuardPolicy)) {
      return;
    }
    localPolicy.onWriteToDisk();
  }
  
  public static void noteResourceMismatch(Object paramObject)
  {
    BlockGuard.Policy localPolicy = BlockGuard.getThreadPolicy();
    if (!(localPolicy instanceof AndroidBlockGuardPolicy)) {
      return;
    }
    ((AndroidBlockGuardPolicy)localPolicy).onResourceMismatch(paramObject);
  }
  
  public static void noteSlowCall(String paramString)
  {
    BlockGuard.Policy localPolicy = BlockGuard.getThreadPolicy();
    if (!(localPolicy instanceof AndroidBlockGuardPolicy)) {
      return;
    }
    ((AndroidBlockGuardPolicy)localPolicy).onCustomSlowCall(paramString);
  }
  
  public static void noteUnbufferedIO()
  {
    BlockGuard.Policy localPolicy = BlockGuard.getThreadPolicy();
    if (!(localPolicy instanceof AndroidBlockGuardPolicy)) {
      return;
    }
    localPolicy.onUnbufferedIO();
  }
  
  private static void onBinderStrictModePolicyChange(int paramInt)
  {
    setBlockGuardPolicy(paramInt);
  }
  
  public static void onCleartextNetworkDetected(byte[] paramArrayOfByte)
  {
    Object localObject1 = null;
    boolean bool = false;
    Object localObject2 = localObject1;
    if (paramArrayOfByte != null) {
      if ((paramArrayOfByte.length >= 20) && ((paramArrayOfByte[0] & 0xF0) == 64))
      {
        localObject2 = new byte[4];
        System.arraycopy(paramArrayOfByte, 16, (byte[])localObject2, 0, 4);
      }
      else
      {
        localObject2 = localObject1;
        if (paramArrayOfByte.length >= 40)
        {
          localObject2 = localObject1;
          if ((paramArrayOfByte[0] & 0xF0) == 96)
          {
            localObject2 = new byte[16];
            System.arraycopy(paramArrayOfByte, 24, (byte[])localObject2, 0, 16);
          }
        }
      }
    }
    int i = Process.myUid();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append("Detected cleartext network traffic from UID ");
    ((StringBuilder)localObject1).append(i);
    String str = ((StringBuilder)localObject1).toString();
    localObject1 = str;
    if (localObject2 != null) {
      try
      {
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append(str);
        ((StringBuilder)localObject1).append(" to ");
        ((StringBuilder)localObject1).append(InetAddress.getByAddress((byte[])localObject2));
        localObject1 = ((StringBuilder)localObject1).toString();
      }
      catch (UnknownHostException localUnknownHostException)
      {
        localObject1 = str;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append((String)localObject1);
    localStringBuilder.append(HexDump.dumpHexString(paramArrayOfByte).trim());
    localStringBuilder.append(" ");
    paramArrayOfByte = localStringBuilder.toString();
    if ((sVmPolicymask & 0x2000000) != 0) {
      bool = true;
    }
    onVmPolicyViolation(new CleartextNetworkViolation(paramArrayOfByte), bool);
  }
  
  public static void onContentUriWithoutPermission(Uri paramUri, String paramString)
  {
    onVmPolicyViolation(new ContentUriWithoutPermissionViolation(paramUri, paramString));
  }
  
  public static void onFileUriExposed(Uri paramUri, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramUri);
    localStringBuilder.append(" exposed beyond app through ");
    localStringBuilder.append(paramString);
    paramUri = localStringBuilder.toString();
    if ((sVmPolicymask & 0x4000000) == 0)
    {
      onVmPolicyViolation(new FileUriExposedViolation(paramUri));
      return;
    }
    throw new FileUriExposedException(paramUri);
  }
  
  public static void onIntentReceiverLeaked(Throwable paramThrowable)
  {
    onVmPolicyViolation(new IntentReceiverLeakedViolation(paramThrowable));
  }
  
  public static void onServiceConnectionLeaked(Throwable paramThrowable)
  {
    onVmPolicyViolation(new ServiceConnectionLeakedViolation(paramThrowable));
  }
  
  public static void onSqliteObjectLeaked(String paramString, Throwable paramThrowable)
  {
    onVmPolicyViolation(new SqliteObjectLeakedViolation(paramString, paramThrowable));
  }
  
  public static void onUntaggedSocket()
  {
    onVmPolicyViolation(new UntaggedSocketViolation());
  }
  
  public static void onVmPolicyViolation(Violation paramViolation)
  {
    onVmPolicyViolation(paramViolation, false);
  }
  
  public static void onVmPolicyViolation(Violation paramViolation, boolean paramBoolean)
  {
    if ((!Build.IS_USER) && ((paramViolation instanceof InstanceCountViolation))) {
      ((InstanceCountViolation)paramViolation).dumpHeap(getVmPolicy());
    }
    int i = sVmPolicymask;
    int j = 1;
    if ((i & 0x200000) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    int k;
    if (((sVmPolicymask & 0x40000) == 0) && (!paramBoolean)) {
      k = 0;
    } else {
      k = 1;
    }
    if ((sVmPolicymask & 0x10000) == 0) {
      j = 0;
    }
    Object localObject1 = new ViolationInfo(paramViolation, sVmPolicymask);
    numAnimationsRunning = 0;
    tags = null;
    broadcastIntentAction = null;
    Object localObject2 = Integer.valueOf(((ViolationInfo)localObject1).hashCode());
    long l1 = SystemClock.uptimeMillis();
    long l2 = Long.MAX_VALUE;
    synchronized (sLastVmViolationTime)
    {
      if (sLastVmViolationTime.containsKey(localObject2)) {
        l2 = l1 - ((Long)sLastVmViolationTime.get(localObject2)).longValue();
      }
      if (l2 > 1000L) {
        sLastVmViolationTime.put(localObject2, Long.valueOf(l1));
      }
      if (l2 <= 1000L) {
        return;
      }
      if ((j != 0) && (sLogger != null) && (l2 > 1000L)) {
        sLogger.log((ViolationInfo)localObject1);
      }
      j = 0x200000 | 0xC000FF00 & sVmPolicymask;
      if (i != 0) {
        if (k != 0) {
          handleApplicationStrictModeViolation(j, (ViolationInfo)localObject1);
        } else {
          dropboxViolationAsync(j, (ViolationInfo)localObject1);
        }
      }
      if (k != 0)
      {
        System.err.println("StrictMode VmPolicy violation with POLICY_DEATH; shutting down.");
        Process.killProcess(Process.myPid());
        System.exit(10);
      }
      if ((sVmPolicymListener != null) && (sVmPolicymCallbackExecutor != null))
      {
        localObject2 = sVmPolicymListener;
        try
        {
          ??? = sVmPolicymCallbackExecutor;
          localObject1 = new android/os/_$$Lambda$StrictMode$UFC_nI1x6u8ZwMQmA7bmj9NHZz4;
          ((_..Lambda.StrictMode.UFC_nI1x6u8ZwMQmA7bmj9NHZz4)localObject1).<init>((OnVmViolationListener)localObject2, paramViolation);
          ((Executor)???).execute((Runnable)localObject1);
        }
        catch (RejectedExecutionException paramViolation)
        {
          Log.e("StrictMode", "VmPolicy penaltyCallback failed", paramViolation);
        }
      }
      return;
    }
  }
  
  public static void onWebViewMethodCalledOnWrongThread(Throwable paramThrowable)
  {
    onVmPolicyViolation(new WebViewMethodCalledOnWrongThreadViolation(paramThrowable));
  }
  
  private static int parsePolicyFromMessage(String paramString)
  {
    if ((paramString != null) && (paramString.startsWith("policy=")))
    {
      int i = paramString.indexOf(' ');
      if (i == -1) {
        return 0;
      }
      paramString = paramString.substring(7, i);
      try
      {
        i = Integer.parseInt(paramString);
        return i;
      }
      catch (NumberFormatException paramString)
      {
        return 0;
      }
    }
    return 0;
  }
  
  private static int readAmaxDetect()
  {
    boolean bool = isStrictModeEnable();
    int i = 0;
    if (bool) {
      i = SystemProperties.getInt("debug.asus.strictmode", 0);
    }
    return i;
  }
  
  static void readAndHandleBinderCallViolations(Parcel paramParcel)
  {
    Throwable localThrowable = new Throwable();
    int i;
    if ((0x400000 & getThreadPolicyMask()) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    int j = paramParcel.readInt();
    for (int k = 0; k < j; k++)
    {
      boolean bool;
      if (i == 0) {
        bool = true;
      } else {
        bool = false;
      }
      ViolationInfo localViolationInfo = new ViolationInfo(paramParcel, bool);
      localViolationInfo.addLocalStack(localThrowable);
      BlockGuard.Policy localPolicy = BlockGuard.getThreadPolicy();
      if ((localPolicy instanceof AndroidBlockGuardPolicy)) {
        ((AndroidBlockGuardPolicy)localPolicy).handleViolationWithTimingAttempt(localViolationInfo);
      }
    }
  }
  
  private static void setBlockGuardPolicy(int paramInt)
  {
    if (paramInt == 0)
    {
      BlockGuard.setThreadPolicy(BlockGuard.LAX_POLICY);
      return;
    }
    Object localObject = BlockGuard.getThreadPolicy();
    if ((localObject instanceof AndroidBlockGuardPolicy))
    {
      localObject = (AndroidBlockGuardPolicy)localObject;
    }
    else
    {
      localObject = (AndroidBlockGuardPolicy)THREAD_ANDROID_POLICY.get();
      BlockGuard.setThreadPolicy((BlockGuard.Policy)localObject);
    }
    ((AndroidBlockGuardPolicy)localObject).setPolicyMask(paramInt);
  }
  
  private static void setCloseGuardEnabled(boolean paramBoolean)
  {
    if (!(CloseGuard.getReporter() instanceof AndroidCloseGuardReporter)) {
      CloseGuard.setReporter(new AndroidCloseGuardReporter(null));
    }
    CloseGuard.setEnabled(paramBoolean);
  }
  
  public static void setThreadPolicy(ThreadPolicy paramThreadPolicy)
  {
    setThreadPolicyMask(mask);
    sThreadViolationListener.set(mListener);
    sThreadViolationExecutor.set(mCallbackExecutor);
  }
  
  public static void setThreadPolicyMask(int paramInt)
  {
    setBlockGuardPolicy(paramInt);
    Binder.setThreadStrictModePolicy(paramInt);
  }
  
  public static void setViolationLogger(ViolationLogger paramViolationLogger)
  {
    ViolationLogger localViolationLogger = paramViolationLogger;
    if (paramViolationLogger == null) {
      localViolationLogger = LOGCAT_LOGGER;
    }
    sLogger = localViolationLogger;
  }
  
  public static void setVmPolicy(VmPolicy paramVmPolicy)
  {
    try
    {
      sVmPolicy = paramVmPolicy;
      setCloseGuardEnabled(vmClosableObjectLeaksEnabled());
      Object localObject = Looper.getMainLooper();
      if (localObject != null)
      {
        localObject = mQueue;
        if ((classInstanceLimit.size() != 0) && ((sVmPolicymask & 0x6250000) != 0))
        {
          if (!sIsIdlerRegistered)
          {
            ((MessageQueue)localObject).addIdleHandler(sProcessIdleHandler);
            sIsIdlerRegistered = true;
          }
        }
        else
        {
          ((MessageQueue)localObject).removeIdleHandler(sProcessIdleHandler);
          sIsIdlerRegistered = false;
        }
        if (!sDisableMonitorMemory) {
          if ((sVmPolicymask & 0x20000000) == 536870912)
          {
            if (!sIsIdlerMemoryRegistered)
            {
              ((MessageQueue)localObject).addIdleHandler(sProcessIdleMemoryHandler);
              sIsIdlerMemoryRegistered = true;
              if (LOG_V) {
                Log.d("StrictMode", "add detect memory idle handler.");
              }
            }
          }
          else if (sIsIdlerMemoryRegistered)
          {
            ((MessageQueue)localObject).removeIdleHandler(sProcessIdleMemoryHandler);
            sIsIdlerMemoryRegistered = false;
            if (LOG_V) {
              Log.d("StrictMode", "remove detect memory idle handler.");
            }
          }
        }
      }
      int i = 0;
      if ((sVmPolicymask & 0x4000) != 0) {
        if (((sVmPolicymask & 0x40000) == 0) && ((sVmPolicymask & 0x2000000) == 0)) {
          i = 1;
        } else {
          i = 2;
        }
      }
      paramVmPolicy = INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"));
      if (paramVmPolicy != null) {
        try
        {
          paramVmPolicy.setUidCleartextNetworkPolicy(Process.myUid(), i);
        }
        catch (RemoteException paramVmPolicy) {}
      } else if (i != 0) {
        Log.w("StrictMode", "Dropping requested network policy due to missing service!");
      }
      if ((sVmPolicymask & 0x40000000) != 0)
      {
        VMRuntime.setNonSdkApiUsageConsumer(sNonSdkApiUsageConsumer);
        VMRuntime.setDedupeHiddenApiWarnings(false);
      }
      else
      {
        VMRuntime.setNonSdkApiUsageConsumer(null);
        VMRuntime.setDedupeHiddenApiWarnings(true);
      }
      return;
    }
    finally {}
  }
  
  private static boolean tooManyViolationsThisLoop()
  {
    boolean bool;
    if (((ArrayList)violationsBeingTimed.get()).size() >= 10) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static Object trackActivity(Object paramObject)
  {
    return new InstanceTracker(paramObject);
  }
  
  public static boolean vmCleartextNetworkEnabled()
  {
    boolean bool;
    if ((sVmPolicymask & 0x4000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean vmClosableObjectLeaksEnabled()
  {
    boolean bool;
    if ((sVmPolicymask & 0x200) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean vmContentUriWithoutPermissionEnabled()
  {
    boolean bool;
    if ((sVmPolicymask & 0x8000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean vmFileUriExposureEnabled()
  {
    boolean bool;
    if ((sVmPolicymask & 0x2000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean vmRegistrationLeaksEnabled()
  {
    boolean bool;
    if ((sVmPolicymask & 0x1000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean vmSqliteObjectLeaksEnabled()
  {
    boolean bool;
    if ((sVmPolicymask & 0x100) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean vmUntaggedSocketEnabled()
  {
    boolean bool;
    if ((sVmPolicymask & 0x80000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  static void writeGatheredViolationsToParcel(Parcel paramParcel)
  {
    ArrayList localArrayList = (ArrayList)gatheredViolations.get();
    if (localArrayList == null)
    {
      paramParcel.writeInt(0);
    }
    else
    {
      int i = Math.min(localArrayList.size(), 3);
      paramParcel.writeInt(i);
      for (int j = 0; j < i; j++) {
        ((ViolationInfo)localArrayList.get(j)).writeToParcel(paramParcel, 0);
      }
    }
    gatheredViolations.set(null);
  }
  
  private static class AndroidBlockGuardPolicy
    implements BlockGuard.Policy
  {
    private ArrayMap<Integer, Long> mLastViolationTime;
    private int mPolicyMask;
    
    public AndroidBlockGuardPolicy(int paramInt)
    {
      mPolicyMask = paramInt;
    }
    
    public int getPolicyMask()
    {
      return mPolicyMask;
    }
    
    void handleViolationWithTimingAttempt(StrictMode.ViolationInfo paramViolationInfo)
    {
      if ((Looper.myLooper() != null) && ((StrictMode.ViolationInfo.access$600(paramViolationInfo) & 0x1770000) != 262144))
      {
        ArrayList localArrayList = (ArrayList)StrictMode.violationsBeingTimed.get();
        if (localArrayList.size() >= 10) {
          return;
        }
        localArrayList.add(paramViolationInfo);
        if (localArrayList.size() > 1) {
          return;
        }
        if (paramViolationInfo.penaltyEnabled(1048576)) {
          paramViolationInfo = (IWindowManager)StrictMode.sWindowManager.get();
        } else {
          paramViolationInfo = null;
        }
        if (paramViolationInfo != null) {
          try
          {
            paramViolationInfo.showStrictModeViolation(true);
          }
          catch (RemoteException localRemoteException) {}
        }
        ((Handler)StrictMode.THREAD_HANDLER.get()).postAtFrontOfQueue(new _..Lambda.StrictMode.AndroidBlockGuardPolicy.9nBulCQKaMajrWr41SB7f7YRT1I(this, paramViolationInfo, localArrayList));
        return;
      }
      durationMillis = -1;
      onThreadPolicyViolation(paramViolationInfo);
    }
    
    void onCustomSlowCall(String paramString)
    {
      if ((mPolicyMask & 0x8) == 0) {
        return;
      }
      if (StrictMode.access$500()) {
        return;
      }
      startHandlingViolationException(new CustomViolation(paramString));
    }
    
    public void onNetwork()
    {
      if ((mPolicyMask & 0x4) == 0) {
        return;
      }
      if ((mPolicyMask & 0x1000000) == 0)
      {
        if (StrictMode.access$500()) {
          return;
        }
        startHandlingViolationException(new NetworkViolation());
        return;
      }
      throw new NetworkOnMainThreadException();
    }
    
    public void onReadFromDisk()
    {
      if ((mPolicyMask & 0x2) == 0) {
        return;
      }
      if (StrictMode.access$500()) {
        return;
      }
      startHandlingViolationException(new DiskReadViolation());
    }
    
    void onResourceMismatch(Object paramObject)
    {
      if ((mPolicyMask & 0x10) == 0) {
        return;
      }
      if (StrictMode.access$500()) {
        return;
      }
      startHandlingViolationException(new ResourceMismatchViolation(paramObject));
    }
    
    void onThreadPolicyViolation(StrictMode.ViolationInfo paramViolationInfo)
    {
      if (StrictMode.LOG_V)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("onThreadPolicyViolation; policy=");
        ((StringBuilder)localObject1).append(StrictMode.ViolationInfo.access$600(paramViolationInfo));
        Log.d("StrictMode", ((StringBuilder)localObject1).toString());
      }
      boolean bool = paramViolationInfo.penaltyEnabled(4194304);
      int i = 1;
      Object localObject2;
      Object localObject3;
      if (bool)
      {
        localObject2 = (ArrayList)StrictMode.gatheredViolations.get();
        localObject1 = localObject2;
        if (localObject2 == null)
        {
          localObject1 = new ArrayList(1);
          StrictMode.gatheredViolations.set(localObject1);
        }
        localObject3 = ((ArrayList)localObject1).iterator();
        while (((Iterator)localObject3).hasNext())
        {
          localObject2 = (StrictMode.ViolationInfo)((Iterator)localObject3).next();
          if (paramViolationInfo.getStackTrace().equals(((StrictMode.ViolationInfo)localObject2).getStackTrace())) {
            return;
          }
        }
        ((ArrayList)localObject1).add(paramViolationInfo);
        return;
      }
      Object localObject1 = Integer.valueOf(paramViolationInfo.hashCode());
      long l1 = 0L;
      if (mLastViolationTime != null)
      {
        localObject2 = (Long)mLastViolationTime.get(localObject1);
        if (localObject2 != null) {
          l1 = ((Long)localObject2).longValue();
        }
      }
      else
      {
        mLastViolationTime = new ArrayMap(1);
      }
      long l2 = SystemClock.uptimeMillis();
      mLastViolationTime.put(localObject1, Long.valueOf(l2));
      if (l1 == 0L) {
        l2 = Long.MAX_VALUE;
      } else {
        l2 -= l1;
      }
      if ((paramViolationInfo.penaltyEnabled(65536)) && (l2 > 1000L)) {
        StrictMode.sLogger.log(paramViolationInfo);
      }
      localObject1 = StrictMode.ViolationInfo.access$1200(paramViolationInfo);
      int j = 0;
      int k = j;
      if (paramViolationInfo.penaltyEnabled(131072))
      {
        k = j;
        if (l2 > 30000L) {
          k = 0x0 | 0x20000;
        }
      }
      j = k;
      if (paramViolationInfo.penaltyEnabled(2097152))
      {
        j = k;
        if (l1 == 0L) {
          j = k | 0x200000;
        }
      }
      k = j;
      if (j != 0)
      {
        j |= paramViolationInfo.getViolationBit();
        if ((StrictMode.ViolationInfo.access$600(paramViolationInfo) & 0x1770000) == 2097152) {
          k = i;
        } else {
          k = 0;
        }
        if (k != 0)
        {
          StrictMode.dropboxViolationAsync(j, paramViolationInfo);
          k = j;
        }
        else
        {
          StrictMode.handleApplicationStrictModeViolation(j, paramViolationInfo);
          k = j;
        }
      }
      if ((paramViolationInfo.getPolicyMask() & 0x40000) == 0)
      {
        paramViolationInfo = (StrictMode.OnThreadViolationListener)StrictMode.sThreadViolationListener.get();
        localObject3 = (Executor)StrictMode.sThreadViolationExecutor.get();
        if ((paramViolationInfo != null) && (localObject3 != null)) {
          try
          {
            localObject2 = new android/os/_$$Lambda$StrictMode$AndroidBlockGuardPolicy$FxZGA9KtfTewqdcxlUwvIe5Nx9I;
            ((_..Lambda.StrictMode.AndroidBlockGuardPolicy.FxZGA9KtfTewqdcxlUwvIe5Nx9I)localObject2).<init>(paramViolationInfo, (Violation)localObject1);
            ((Executor)localObject3).execute((Runnable)localObject2);
          }
          catch (RejectedExecutionException paramViolationInfo)
          {
            Log.e("StrictMode", "ThreadPolicy penaltyCallback failed", paramViolationInfo);
          }
        }
        return;
      }
      throw new RuntimeException("StrictMode ThreadPolicy violation", (Throwable)localObject1);
    }
    
    public void onUnbufferedIO()
    {
      if ((mPolicyMask & 0x20) == 0) {
        return;
      }
      if (StrictMode.access$500()) {
        return;
      }
      startHandlingViolationException(new UnbufferedIoViolation());
    }
    
    public void onWriteToDisk()
    {
      if ((mPolicyMask & 0x1) == 0) {
        return;
      }
      if (StrictMode.access$500()) {
        return;
      }
      startHandlingViolationException(new DiskWriteViolation());
    }
    
    public void setPolicyMask(int paramInt)
    {
      mPolicyMask = paramInt;
    }
    
    void startHandlingViolationException(Violation paramViolation)
    {
      paramViolation = new StrictMode.ViolationInfo(paramViolation, mPolicyMask);
      violationUptimeMillis = SystemClock.uptimeMillis();
      handleViolationWithTimingAttempt(paramViolation);
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AndroidBlockGuardPolicy; mPolicyMask=");
      localStringBuilder.append(mPolicyMask);
      return localStringBuilder.toString();
    }
  }
  
  private static class AndroidCloseGuardReporter
    implements CloseGuard.Reporter
  {
    private AndroidCloseGuardReporter() {}
    
    public void report(String paramString, Throwable paramThrowable)
    {
      StrictMode.onVmPolicyViolation(new LeakedClosableViolation(paramString, paramThrowable));
    }
  }
  
  private static class Budget
  {
    final float avgKB;
    final float hardKB;
    final String packageName;
    
    public Budget()
    {
      packageName = "INVALID";
      avgKB = 0.0F;
      hardKB = 0.0F;
    }
    
    public Budget(String paramString, float paramFloat1, float paramFloat2)
    {
      packageName = paramString;
      avgKB = paramFloat1;
      hardKB = paramFloat2;
    }
    
    public boolean invalid()
    {
      return "INVALID".equals(packageName);
    }
  }
  
  private static final class InstanceTracker
  {
    private static final HashMap<Class<?>, Integer> sInstanceCounts = new HashMap();
    private final Class<?> mKlass;
    
    public InstanceTracker(Object arg1)
    {
      mKlass = ???.getClass();
      synchronized (sInstanceCounts)
      {
        Integer localInteger = (Integer)sInstanceCounts.get(mKlass);
        int i = 1;
        if (localInteger != null) {
          i = 1 + localInteger.intValue();
        }
        sInstanceCounts.put(mKlass, Integer.valueOf(i));
        return;
      }
    }
    
    public static int getInstanceCount(Class<?> paramClass)
    {
      synchronized (sInstanceCounts)
      {
        paramClass = (Integer)sInstanceCounts.get(paramClass);
        int i;
        if (paramClass != null) {
          i = paramClass.intValue();
        } else {
          i = 0;
        }
        return i;
      }
    }
    
    /* Error */
    protected void finalize()
      throws Throwable
    {
      // Byte code:
      //   0: getstatic 22	android/os/StrictMode$InstanceTracker:sInstanceCounts	Ljava/util/HashMap;
      //   3: astore_1
      //   4: aload_1
      //   5: monitorenter
      //   6: getstatic 22	android/os/StrictMode$InstanceTracker:sInstanceCounts	Ljava/util/HashMap;
      //   9: aload_0
      //   10: getfield 31	android/os/StrictMode$InstanceTracker:mKlass	Ljava/lang/Class;
      //   13: invokevirtual 35	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
      //   16: checkcast 37	java/lang/Integer
      //   19: astore_2
      //   20: aload_2
      //   21: ifnull +43 -> 64
      //   24: aload_2
      //   25: invokevirtual 41	java/lang/Integer:intValue	()I
      //   28: iconst_1
      //   29: isub
      //   30: istore_3
      //   31: iload_3
      //   32: ifle +21 -> 53
      //   35: getstatic 22	android/os/StrictMode$InstanceTracker:sInstanceCounts	Ljava/util/HashMap;
      //   38: aload_0
      //   39: getfield 31	android/os/StrictMode$InstanceTracker:mKlass	Ljava/lang/Class;
      //   42: iload_3
      //   43: invokestatic 45	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   46: invokevirtual 49	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   49: pop
      //   50: goto +14 -> 64
      //   53: getstatic 22	android/os/StrictMode$InstanceTracker:sInstanceCounts	Ljava/util/HashMap;
      //   56: aload_0
      //   57: getfield 31	android/os/StrictMode$InstanceTracker:mKlass	Ljava/lang/Class;
      //   60: invokevirtual 59	java/util/HashMap:remove	(Ljava/lang/Object;)Ljava/lang/Object;
      //   63: pop
      //   64: aload_1
      //   65: monitorexit
      //   66: aload_0
      //   67: invokespecial 61	java/lang/Object:finalize	()V
      //   70: return
      //   71: astore_2
      //   72: aload_1
      //   73: monitorexit
      //   74: aload_2
      //   75: athrow
      //   76: astore_1
      //   77: aload_0
      //   78: invokespecial 61	java/lang/Object:finalize	()V
      //   81: aload_1
      //   82: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	83	0	this	InstanceTracker
      //   76	6	1	localObject1	Object
      //   19	6	2	localInteger	Integer
      //   71	4	2	localObject2	Object
      //   30	13	3	i	int
      // Exception table:
      //   from	to	target	type
      //   6	20	71	finally
      //   24	31	71	finally
      //   35	50	71	finally
      //   53	64	71	finally
      //   64	66	71	finally
      //   72	74	71	finally
      //   0	6	76	finally
      //   74	76	76	finally
    }
  }
  
  public static abstract interface OnThreadViolationListener
  {
    public abstract void onThreadViolation(Violation paramViolation);
  }
  
  public static abstract interface OnVmViolationListener
  {
    public abstract void onVmViolation(Violation paramViolation);
  }
  
  public static class Span
  {
    private final StrictMode.ThreadSpanState mContainerState;
    private long mCreateMillis;
    private String mName;
    private Span mNext;
    private Span mPrev;
    
    protected Span()
    {
      mContainerState = null;
    }
    
    Span(StrictMode.ThreadSpanState paramThreadSpanState)
    {
      mContainerState = paramThreadSpanState;
    }
    
    public void finish()
    {
      synchronized (mContainerState)
      {
        if (mName == null) {
          return;
        }
        if (mPrev != null) {
          mPrev.mNext = mNext;
        }
        if (mNext != null) {
          mNext.mPrev = mPrev;
        }
        if (mActiveHead == this) {
          mActiveHead = mNext;
        }
        mActiveSize -= 1;
        if (StrictMode.LOG_V)
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Span finished=");
          localStringBuilder.append(mName);
          localStringBuilder.append("; size=");
          localStringBuilder.append(mActiveSize);
          Log.d("StrictMode", localStringBuilder.toString());
        }
        mCreateMillis = -1L;
        mName = null;
        mPrev = null;
        mNext = null;
        if (mFreeListSize < 5)
        {
          mNext = mFreeListHead;
          mFreeListHead = this;
          mFreeListSize += 1;
        }
        return;
      }
    }
  }
  
  public static final class ThreadPolicy
  {
    public static final ThreadPolicy LAX = new ThreadPolicy(0, null, null);
    final Executor mCallbackExecutor;
    final StrictMode.OnThreadViolationListener mListener;
    final int mask;
    
    private ThreadPolicy(int paramInt, StrictMode.OnThreadViolationListener paramOnThreadViolationListener, Executor paramExecutor)
    {
      mask = paramInt;
      mListener = paramOnThreadViolationListener;
      mCallbackExecutor = paramExecutor;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[StrictMode.ThreadPolicy; mask=");
      localStringBuilder.append(mask);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public static final class Builder
    {
      private Executor mExecutor;
      private StrictMode.OnThreadViolationListener mListener;
      private int mMask = 0;
      
      public Builder()
      {
        mMask = 0;
      }
      
      public Builder(StrictMode.ThreadPolicy paramThreadPolicy)
      {
        mMask = mask;
        mListener = mListener;
        mExecutor = mCallbackExecutor;
      }
      
      private Builder disable(int paramInt)
      {
        mMask &= paramInt;
        return this;
      }
      
      private Builder enable(int paramInt)
      {
        mMask |= paramInt;
        return this;
      }
      
      public StrictMode.ThreadPolicy build()
      {
        if ((mListener == null) && (mMask != 0) && ((mMask & 0x270000) == 0)) {
          penaltyLog();
        }
        return new StrictMode.ThreadPolicy(mMask, mListener, mExecutor, null);
      }
      
      public Builder detectAll()
      {
        detectDiskReads();
        detectDiskWrites();
        detectNetwork();
        int i = VMRuntime.getRuntime().getTargetSdkVersion();
        if (i >= 11) {
          detectCustomSlowCalls();
        }
        if (i >= 23) {
          detectResourceMismatches();
        }
        if (i >= 26) {
          detectUnbufferedIo();
        }
        return this;
      }
      
      public Builder detectCustomSlowCalls()
      {
        return enable(8);
      }
      
      public Builder detectDiskReads()
      {
        return enable(2);
      }
      
      public Builder detectDiskWrites()
      {
        return enable(1);
      }
      
      public Builder detectNetwork()
      {
        return enable(4);
      }
      
      public Builder detectResourceMismatches()
      {
        return enable(16);
      }
      
      public Builder detectUnbufferedIo()
      {
        return enable(32);
      }
      
      public Builder penaltyDeath()
      {
        return enable(262144);
      }
      
      public Builder penaltyDeathOnNetwork()
      {
        return enable(16777216);
      }
      
      public Builder penaltyDialog()
      {
        return enable(131072);
      }
      
      public Builder penaltyDropBox()
      {
        return enable(2097152);
      }
      
      public Builder penaltyFlashScreen()
      {
        return enable(1048576);
      }
      
      public Builder penaltyListener(StrictMode.OnThreadViolationListener paramOnThreadViolationListener, Executor paramExecutor)
      {
        return penaltyListener(paramExecutor, paramOnThreadViolationListener);
      }
      
      public Builder penaltyListener(Executor paramExecutor, StrictMode.OnThreadViolationListener paramOnThreadViolationListener)
      {
        if (paramExecutor != null)
        {
          mListener = paramOnThreadViolationListener;
          mExecutor = paramExecutor;
          return this;
        }
        throw new NullPointerException("executor must not be null");
      }
      
      public Builder penaltyLog()
      {
        return enable(65536);
      }
      
      public Builder permitAll()
      {
        return disable(63);
      }
      
      public Builder permitCustomSlowCalls()
      {
        return disable(8);
      }
      
      public Builder permitDiskReads()
      {
        return disable(2);
      }
      
      public Builder permitDiskWrites()
      {
        return disable(1);
      }
      
      public Builder permitNetwork()
      {
        return disable(4);
      }
      
      public Builder permitResourceMismatches()
      {
        return disable(16);
      }
      
      public Builder permitUnbufferedIo()
      {
        return disable(32);
      }
    }
  }
  
  private static class ThreadSpanState
  {
    public StrictMode.Span mActiveHead;
    public int mActiveSize;
    public StrictMode.Span mFreeListHead;
    public int mFreeListSize;
    
    private ThreadSpanState() {}
  }
  
  public static final class ViolationInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<ViolationInfo> CREATOR = new Parcelable.Creator()
    {
      public StrictMode.ViolationInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new StrictMode.ViolationInfo(paramAnonymousParcel);
      }
      
      public StrictMode.ViolationInfo[] newArray(int paramAnonymousInt)
      {
        return new StrictMode.ViolationInfo[paramAnonymousInt];
      }
    };
    public String broadcastIntentAction;
    public int durationMillis = -1;
    private final Deque<StackTraceElement[]> mBinderStack = new ArrayDeque();
    private final int mPolicy;
    private String mStackTrace;
    private final Violation mViolation;
    public int numAnimationsRunning;
    public long numInstances;
    public String[] tags;
    public int violationNumThisLoop;
    public long violationUptimeMillis;
    
    public ViolationInfo(Parcel paramParcel)
    {
      this(paramParcel, false);
    }
    
    public ViolationInfo(Parcel paramParcel, boolean paramBoolean)
    {
      numAnimationsRunning = 0;
      numInstances = -1L;
      mViolation = ((Violation)paramParcel.readSerializable());
      int i = paramParcel.readInt();
      for (int j = 0; j < i; j++)
      {
        StackTraceElement[] arrayOfStackTraceElement = new StackTraceElement[paramParcel.readInt()];
        for (int k = 0; k < arrayOfStackTraceElement.length; k++) {
          arrayOfStackTraceElement[k] = new StackTraceElement(paramParcel.readString(), paramParcel.readString(), paramParcel.readString(), paramParcel.readInt());
        }
        mBinderStack.add(arrayOfStackTraceElement);
      }
      j = paramParcel.readInt();
      if (paramBoolean) {
        mPolicy = (0xFFBFFFFF & j);
      } else {
        mPolicy = j;
      }
      durationMillis = paramParcel.readInt();
      violationNumThisLoop = paramParcel.readInt();
      numAnimationsRunning = paramParcel.readInt();
      violationUptimeMillis = paramParcel.readLong();
      numInstances = paramParcel.readLong();
      broadcastIntentAction = paramParcel.readString();
      tags = paramParcel.readStringArray();
    }
    
    ViolationInfo(Violation paramViolation, int paramInt)
    {
      int i = 0;
      numAnimationsRunning = 0;
      numInstances = -1L;
      mViolation = paramViolation;
      mPolicy = paramInt;
      violationUptimeMillis = SystemClock.uptimeMillis();
      numAnimationsRunning = ValueAnimator.getCurrentAnimationsCount();
      Object localObject = ActivityThread.getIntentBeingBroadcast();
      if (localObject != null) {
        broadcastIntentAction = ((Intent)localObject).getAction();
      }
      localObject = (StrictMode.ThreadSpanState)StrictMode.sThisThreadSpanState.get();
      if ((paramViolation instanceof InstanceCountViolation)) {
        numInstances = ((InstanceCountViolation)paramViolation).getNumberOfInstances();
      }
      try
      {
        int j = mActiveSize;
        paramInt = j;
        if (j > 20) {
          paramInt = 20;
        }
        if (paramInt != 0)
        {
          tags = new String[paramInt];
          for (paramViolation = mActiveHead; (paramViolation != null) && (i < paramInt); paramViolation = mNext)
          {
            tags[i] = mName;
            i++;
          }
        }
        return;
      }
      finally {}
    }
    
    void addLocalStack(Throwable paramThrowable)
    {
      mBinderStack.addFirst(paramThrowable.getStackTrace());
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void dump(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append(paramString);
      localStringBuilder1.append("stackTrace: ");
      localStringBuilder1.append(getStackTrace());
      paramPrinter.println(localStringBuilder1.toString());
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append(paramString);
      localStringBuilder1.append("policy: ");
      localStringBuilder1.append(mPolicy);
      paramPrinter.println(localStringBuilder1.toString());
      if (durationMillis != -1)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append(paramString);
        localStringBuilder1.append("durationMillis: ");
        localStringBuilder1.append(durationMillis);
        paramPrinter.println(localStringBuilder1.toString());
      }
      if (numInstances != -1L)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append(paramString);
        localStringBuilder1.append("numInstances: ");
        localStringBuilder1.append(numInstances);
        paramPrinter.println(localStringBuilder1.toString());
      }
      if (violationNumThisLoop != 0)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append(paramString);
        localStringBuilder1.append("violationNumThisLoop: ");
        localStringBuilder1.append(violationNumThisLoop);
        paramPrinter.println(localStringBuilder1.toString());
      }
      if (numAnimationsRunning != 0)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append(paramString);
        localStringBuilder1.append("numAnimationsRunning: ");
        localStringBuilder1.append(numAnimationsRunning);
        paramPrinter.println(localStringBuilder1.toString());
      }
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append(paramString);
      localStringBuilder1.append("violationUptimeMillis: ");
      localStringBuilder1.append(violationUptimeMillis);
      paramPrinter.println(localStringBuilder1.toString());
      if (broadcastIntentAction != null)
      {
        localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append(paramString);
        localStringBuilder1.append("broadcastIntentAction: ");
        localStringBuilder1.append(broadcastIntentAction);
        paramPrinter.println(localStringBuilder1.toString());
      }
      if (tags != null)
      {
        int i = 0;
        String[] arrayOfString = tags;
        int j = arrayOfString.length;
        int k = 0;
        while (k < j)
        {
          localStringBuilder1 = arrayOfString[k];
          StringBuilder localStringBuilder2 = new StringBuilder();
          localStringBuilder2.append(paramString);
          localStringBuilder2.append("tag[");
          localStringBuilder2.append(i);
          localStringBuilder2.append("]: ");
          localStringBuilder2.append(localStringBuilder1);
          paramPrinter.println(localStringBuilder2.toString());
          k++;
          i++;
        }
      }
    }
    
    public int getPolicyMask()
    {
      return mPolicy;
    }
    
    public String getStackTrace()
    {
      if (mStackTrace == null)
      {
        StringWriter localStringWriter = new StringWriter();
        FastPrintWriter localFastPrintWriter = new FastPrintWriter(localStringWriter, false, 256);
        mViolation.printStackTrace(localFastPrintWriter);
        Iterator localIterator = mBinderStack.iterator();
        while (localIterator.hasNext())
        {
          StackTraceElement[] arrayOfStackTraceElement = (StackTraceElement[])localIterator.next();
          localFastPrintWriter.append("# via Binder call with stack:\n");
          int i = arrayOfStackTraceElement.length;
          for (int j = 0; j < i; j++)
          {
            StackTraceElement localStackTraceElement = arrayOfStackTraceElement[j];
            localFastPrintWriter.append("\tat ");
            localFastPrintWriter.append(localStackTraceElement.toString());
            localFastPrintWriter.append('\n');
          }
        }
        localFastPrintWriter.flush();
        localFastPrintWriter.close();
        mStackTrace = localStringWriter.toString();
      }
      return mStackTrace;
    }
    
    public int getViolationBit()
    {
      if ((mViolation instanceof DiskWriteViolation)) {
        return 1;
      }
      if ((mViolation instanceof DiskReadViolation)) {
        return 2;
      }
      if ((mViolation instanceof NetworkViolation)) {
        return 4;
      }
      if ((mViolation instanceof CustomViolation)) {
        return 8;
      }
      if ((mViolation instanceof ResourceMismatchViolation)) {
        return 16;
      }
      if ((mViolation instanceof UnbufferedIoViolation)) {
        return 32;
      }
      if ((mViolation instanceof SqliteObjectLeakedViolation)) {
        return 256;
      }
      if ((mViolation instanceof LeakedClosableViolation)) {
        return 512;
      }
      if ((mViolation instanceof InstanceCountViolation)) {
        return 2048;
      }
      if ((mViolation instanceof IntentReceiverLeakedViolation)) {
        return 4096;
      }
      if ((mViolation instanceof ServiceConnectionLeakedViolation)) {
        return 4096;
      }
      if ((mViolation instanceof FileUriExposedViolation)) {
        return 8192;
      }
      if ((mViolation instanceof CleartextNetworkViolation)) {
        return 16384;
      }
      if ((mViolation instanceof ContentUriWithoutPermissionViolation)) {
        return 32768;
      }
      if ((mViolation instanceof UntaggedSocketViolation)) {
        return Integer.MIN_VALUE;
      }
      if ((mViolation instanceof NonSdkApiUsedViolation)) {
        return 1073741824;
      }
      throw new IllegalStateException("missing violation bit");
    }
    
    public String getViolationDetails()
    {
      return mViolation.getMessage();
    }
    
    public int hashCode()
    {
      int i = 17;
      if (mViolation != null) {
        i = 37 * 17 + mViolation.hashCode();
      }
      int j = i;
      if (numAnimationsRunning != 0) {
        j = i * 37;
      }
      i = j;
      if (broadcastIntentAction != null) {
        i = 37 * j + broadcastIntentAction.hashCode();
      }
      int k = i;
      if (tags != null)
      {
        String[] arrayOfString = tags;
        int m = arrayOfString.length;
        for (j = 0;; j++)
        {
          k = i;
          if (j >= m) {
            break;
          }
          i = 37 * i + arrayOfString[j].hashCode();
        }
      }
      return k;
    }
    
    boolean penaltyEnabled(int paramInt)
    {
      boolean bool;
      if ((mPolicy & paramInt) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeSerializable(mViolation);
      paramParcel.writeInt(mBinderStack.size());
      Iterator localIterator = mBinderStack.iterator();
      while (localIterator.hasNext())
      {
        StackTraceElement[] arrayOfStackTraceElement = (StackTraceElement[])localIterator.next();
        paramParcel.writeInt(arrayOfStackTraceElement.length);
        int i = arrayOfStackTraceElement.length;
        for (paramInt = 0; paramInt < i; paramInt++)
        {
          StackTraceElement localStackTraceElement = arrayOfStackTraceElement[paramInt];
          paramParcel.writeString(localStackTraceElement.getClassName());
          paramParcel.writeString(localStackTraceElement.getMethodName());
          paramParcel.writeString(localStackTraceElement.getFileName());
          paramParcel.writeInt(localStackTraceElement.getLineNumber());
        }
      }
      paramParcel.dataPosition();
      paramParcel.writeInt(mPolicy);
      paramParcel.writeInt(durationMillis);
      paramParcel.writeInt(violationNumThisLoop);
      paramParcel.writeInt(numAnimationsRunning);
      paramParcel.writeLong(violationUptimeMillis);
      paramParcel.writeLong(numInstances);
      paramParcel.writeString(broadcastIntentAction);
      paramParcel.writeStringArray(tags);
      paramParcel.dataPosition();
    }
  }
  
  public static abstract interface ViolationLogger
  {
    public abstract void log(StrictMode.ViolationInfo paramViolationInfo);
  }
  
  public static final class VmPolicy
  {
    public static final VmPolicy LAX = new VmPolicy(0, StrictMode.EMPTY_CLASS_LIMIT_MAP, null, null);
    final HashMap<Class, Integer> classInstanceLimit;
    ArrayList<Class> dumpedClasses;
    boolean enableAsusDump;
    boolean initAsusDump;
    final Executor mCallbackExecutor;
    final StrictMode.OnVmViolationListener mListener;
    final int mask;
    
    private VmPolicy(int paramInt, HashMap<Class, Integer> paramHashMap, StrictMode.OnVmViolationListener paramOnVmViolationListener, Executor paramExecutor)
    {
      if (paramHashMap != null)
      {
        mask = paramInt;
        classInstanceLimit = paramHashMap;
        mListener = paramOnVmViolationListener;
        mCallbackExecutor = paramExecutor;
        return;
      }
      throw new NullPointerException("classInstanceLimit == null");
    }
    
    private void initAsusDump()
    {
      if (initAsusDump) {
        return;
      }
      boolean bool = false;
      if (SystemProperties.getInt("debug.asus.dumpheap", 0) == 1) {
        bool = true;
      }
      enableAsusDump = bool;
      if (enableAsusDump) {
        dumpedClasses = new ArrayList();
      }
      initAsusDump = true;
    }
    
    public void dumpHeapForInstanceCount(Class paramClass, long paramLong, int paramInt)
    {
      initAsusDump();
      if (!enableAsusDump) {
        return;
      }
      if (dumpedClasses.contains(paramClass)) {
        return;
      }
      try
      {
        StringBuilder localStringBuilder1 = new java/lang/StringBuilder;
        localStringBuilder1.<init>();
        localStringBuilder1.append("/sdcard/");
        localStringBuilder1.append(Process.myPid());
        localStringBuilder1.append("_InstanceCountViolation_");
        localStringBuilder1.append(paramClass.getName());
        localStringBuilder1.append("_");
        localStringBuilder1.append(paramLong);
        localStringBuilder1.append("_");
        localStringBuilder1.append(paramInt);
        localStringBuilder1.append(".hprof");
        Debug.dumpHprofData(localStringBuilder1.toString());
        if (StrictMode.LOG_V)
        {
          StringBuilder localStringBuilder2 = new java/lang/StringBuilder;
          localStringBuilder2.<init>();
          localStringBuilder2.append("dumped heap for InstanceCountViolation '");
          localStringBuilder2.append(localStringBuilder1.toString());
          localStringBuilder2.append("'");
          Log.d("StrictMode", localStringBuilder2.toString());
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        localRuntimeException.printStackTrace();
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
      dumpedClasses.add(paramClass);
    }
    
    public void dumpHeapForOverBudget(String paramString, float paramFloat1, float paramFloat2)
    {
      initAsusDump();
      if (!enableAsusDump) {
        return;
      }
      try
      {
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("/sdcard/");
        localStringBuilder.append(Process.myPid());
        localStringBuilder.append("_OverBudgetViolation_");
        localStringBuilder.append(paramString);
        localStringBuilder.append("_");
        localStringBuilder.append(paramFloat1);
        localStringBuilder.append("_");
        localStringBuilder.append(paramFloat2);
        localStringBuilder.append(".hprof");
        Debug.dumpHprofData(localStringBuilder.toString());
        if (StrictMode.LOG_V)
        {
          paramString = new java/lang/StringBuilder;
          paramString.<init>();
          paramString.append("dumped heap for OverBudgetViolation '");
          paramString.append(localStringBuilder.toString());
          paramString.append("'");
          Log.d("StrictMode", paramString.toString());
        }
      }
      catch (RuntimeException paramString)
      {
        paramString.printStackTrace();
      }
      catch (IOException paramString)
      {
        paramString.printStackTrace();
      }
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("[StrictMode.VmPolicy; mask=");
      localStringBuilder.append(mask);
      localStringBuilder.append("]");
      return localStringBuilder.toString();
    }
    
    public static final class Builder
    {
      private HashMap<Class, Integer> mClassInstanceLimit;
      private boolean mClassInstanceLimitNeedCow = false;
      private Executor mExecutor;
      private StrictMode.OnVmViolationListener mListener;
      private int mMask;
      
      public Builder()
      {
        mMask = 0;
      }
      
      public Builder(StrictMode.VmPolicy paramVmPolicy)
      {
        mMask = mask;
        mClassInstanceLimitNeedCow = true;
        mClassInstanceLimit = classInstanceLimit;
        mListener = mListener;
        mExecutor = mCallbackExecutor;
      }
      
      private Builder enable(int paramInt)
      {
        mMask |= paramInt;
        return this;
      }
      
      public StrictMode.VmPolicy build()
      {
        if ((mListener == null) && (mMask != 0) && ((mMask & 0x270000) == 0)) {
          penaltyLog();
        }
        int i = mMask;
        if (mClassInstanceLimit != null) {}
        for (HashMap localHashMap = mClassInstanceLimit;; localHashMap = StrictMode.EMPTY_CLASS_LIMIT_MAP) {
          break;
        }
        return new StrictMode.VmPolicy(i, localHashMap, mListener, mExecutor, null);
      }
      
      public Builder detectActivityLeaks()
      {
        return enable(1024);
      }
      
      public Builder detectAll()
      {
        detectLeakedSqlLiteObjects();
        int i = VMRuntime.getRuntime().getTargetSdkVersion();
        if (i >= 11)
        {
          detectActivityLeaks();
          detectLeakedClosableObjects();
        }
        if (i >= 16) {
          detectLeakedRegistrationObjects();
        }
        if (i >= 18) {
          detectFileUriExposure();
        }
        if ((i >= 23) && (SystemProperties.getBoolean("persist.sys.strictmode.clear", false))) {
          detectCleartextNetwork();
        }
        if (i >= 26)
        {
          detectContentUriWithoutPermission();
          detectUntaggedSockets();
        }
        return this;
      }
      
      public Builder detectCleartextNetwork()
      {
        return enable(16384);
      }
      
      public Builder detectContentUriWithoutPermission()
      {
        return enable(32768);
      }
      
      public Builder detectFileUriExposure()
      {
        return enable(8192);
      }
      
      public Builder detectLeakedClosableObjects()
      {
        return enable(512);
      }
      
      public Builder detectLeakedRegistrationObjects()
      {
        return enable(4096);
      }
      
      public Builder detectLeakedSqlLiteObjects()
      {
        return enable(256);
      }
      
      public Builder detectNonSdkApiUsage()
      {
        return enable(1073741824);
      }
      
      public Builder detectUntaggedSockets()
      {
        return enable(Integer.MIN_VALUE);
      }
      
      public Builder detectVmMemory()
      {
        return enable(536870912);
      }
      
      Builder disable(int paramInt)
      {
        mMask &= paramInt;
        return this;
      }
      
      public Builder penaltyDeath()
      {
        return enable(262144);
      }
      
      public Builder penaltyDeathOnCleartextNetwork()
      {
        return enable(33554432);
      }
      
      public Builder penaltyDeathOnFileUriExposure()
      {
        return enable(67108864);
      }
      
      public Builder penaltyDropBox()
      {
        return enable(2097152);
      }
      
      public Builder penaltyListener(StrictMode.OnVmViolationListener paramOnVmViolationListener, Executor paramExecutor)
      {
        return penaltyListener(paramExecutor, paramOnVmViolationListener);
      }
      
      public Builder penaltyListener(Executor paramExecutor, StrictMode.OnVmViolationListener paramOnVmViolationListener)
      {
        if (paramExecutor != null)
        {
          mListener = paramOnVmViolationListener;
          mExecutor = paramExecutor;
          return this;
        }
        throw new NullPointerException("executor must not be null");
      }
      
      public Builder penaltyLog()
      {
        return enable(65536);
      }
      
      public Builder permitActivityLeaks()
      {
        return disable(1024);
      }
      
      public Builder permitNonSdkApiUsage()
      {
        return disable(1073741824);
      }
      
      public Builder permitUntaggedSockets()
      {
        return disable(Integer.MIN_VALUE);
      }
      
      public Builder permitVmMemory()
      {
        return disable(536870912);
      }
      
      public Builder setClassInstanceLimit(Class paramClass, int paramInt)
      {
        if (paramClass != null)
        {
          if (mClassInstanceLimitNeedCow)
          {
            if ((mClassInstanceLimit.containsKey(paramClass)) && (((Integer)mClassInstanceLimit.get(paramClass)).intValue() == paramInt)) {
              return this;
            }
            mClassInstanceLimitNeedCow = false;
            mClassInstanceLimit = ((HashMap)mClassInstanceLimit.clone());
          }
          else if (mClassInstanceLimit == null)
          {
            mClassInstanceLimit = new HashMap();
          }
          mMask |= 0x800;
          mClassInstanceLimit.put(paramClass, Integer.valueOf(paramInt));
          return this;
        }
        throw new NullPointerException("klass == null");
      }
    }
  }
}
