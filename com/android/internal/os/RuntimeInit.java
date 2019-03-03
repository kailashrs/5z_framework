package com.android.internal.os;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.ApplicationErrorReport.ParcelableCrashInfo;
import android.app.IActivityManager;
import android.ddm.DdmRegister;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.Slog;
import com.android.internal.logging.AndroidConfig;
import com.android.server.NetworkManagementSocketTagger;
import dalvik.system.VMRuntime;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.TimeZone;
import java.util.logging.LogManager;
import org.apache.harmony.luni.internal.util.TimezoneGetter;

public class RuntimeInit
{
  static final boolean DEBUG = false;
  static final String TAG = "AndroidRuntime";
  private static boolean initialized;
  private static IBinder mApplicationObject;
  private static volatile boolean mCrashing = false;
  
  public RuntimeInit() {}
  
  private static int Clog_e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    return Log.printlns(4, 6, paramString1, paramString2, paramThrowable);
  }
  
  protected static Runnable applicationInit(int paramInt, String[] paramArrayOfString, ClassLoader paramClassLoader)
  {
    nativeSetExitWithoutCleanup(true);
    VMRuntime.getRuntime().setTargetHeapUtilization(0.75F);
    VMRuntime.getRuntime().setTargetSdkVersion(paramInt);
    paramArrayOfString = new Arguments(paramArrayOfString);
    Trace.traceEnd(64L);
    return findStaticMain(startClass, startArgs, paramClassLoader);
  }
  
  protected static final void commonInit()
  {
    LoggingHandler localLoggingHandler = new LoggingHandler(null);
    Thread.setUncaughtExceptionPreHandler(localLoggingHandler);
    Thread.setDefaultUncaughtExceptionHandler(new KillApplicationHandler(localLoggingHandler));
    TimezoneGetter.setInstance(new TimezoneGetter()
    {
      public String getId()
      {
        return SystemProperties.get("persist.sys.timezone");
      }
    });
    TimeZone.setDefault(null);
    LogManager.getLogManager().reset();
    new AndroidConfig();
    System.setProperty("http.agent", getDefaultUserAgent());
    NetworkManagementSocketTagger.install();
    if (SystemProperties.get("ro.kernel.android.tracing").equals("1"))
    {
      Slog.i("AndroidRuntime", "NOTE: emulator trace profiling enabled");
      Debug.enableEmulatorTraceOutput();
    }
    initialized = true;
  }
  
  static final void enableDdms() {}
  
  /* Error */
  protected static Runnable findStaticMain(String paramString, String[] paramArrayOfString, ClassLoader paramClassLoader)
  {
    // Byte code:
    //   0: aload_0
    //   1: iconst_1
    //   2: aload_2
    //   3: invokestatic 206	java/lang/Class:forName	(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
    //   6: astore_2
    //   7: aload_2
    //   8: ldc -48
    //   10: iconst_1
    //   11: anewarray 202	java/lang/Class
    //   14: dup
    //   15: iconst_0
    //   16: ldc -47
    //   18: aastore
    //   19: invokevirtual 213	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   22: astore_2
    //   23: aload_2
    //   24: invokevirtual 219	java/lang/reflect/Method:getModifiers	()I
    //   27: istore_3
    //   28: iload_3
    //   29: invokestatic 225	java/lang/reflect/Modifier:isStatic	(I)Z
    //   32: ifeq +20 -> 52
    //   35: iload_3
    //   36: invokestatic 228	java/lang/reflect/Modifier:isPublic	(I)Z
    //   39: ifeq +13 -> 52
    //   42: new 17	com/android/internal/os/RuntimeInit$MethodAndArgsCaller
    //   45: dup
    //   46: aload_2
    //   47: aload_1
    //   48: invokespecial 231	com/android/internal/os/RuntimeInit$MethodAndArgsCaller:<init>	(Ljava/lang/reflect/Method;[Ljava/lang/String;)V
    //   51: areturn
    //   52: new 233	java/lang/StringBuilder
    //   55: dup
    //   56: invokespecial 234	java/lang/StringBuilder:<init>	()V
    //   59: astore_1
    //   60: aload_1
    //   61: ldc -20
    //   63: invokevirtual 240	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: pop
    //   67: aload_1
    //   68: aload_0
    //   69: invokevirtual 240	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   72: pop
    //   73: new 242	java/lang/RuntimeException
    //   76: dup
    //   77: aload_1
    //   78: invokevirtual 245	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   81: invokespecial 248	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   84: athrow
    //   85: astore_1
    //   86: new 233	java/lang/StringBuilder
    //   89: dup
    //   90: invokespecial 234	java/lang/StringBuilder:<init>	()V
    //   93: astore_2
    //   94: aload_2
    //   95: ldc -6
    //   97: invokevirtual 240	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: pop
    //   101: aload_2
    //   102: aload_0
    //   103: invokevirtual 240	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: pop
    //   107: new 242	java/lang/RuntimeException
    //   110: dup
    //   111: aload_2
    //   112: invokevirtual 245	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   115: aload_1
    //   116: invokespecial 253	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   119: athrow
    //   120: astore_1
    //   121: new 233	java/lang/StringBuilder
    //   124: dup
    //   125: invokespecial 234	java/lang/StringBuilder:<init>	()V
    //   128: astore_2
    //   129: aload_2
    //   130: ldc -1
    //   132: invokevirtual 240	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: pop
    //   136: aload_2
    //   137: aload_0
    //   138: invokevirtual 240	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: new 242	java/lang/RuntimeException
    //   145: dup
    //   146: aload_2
    //   147: invokevirtual 245	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   150: aload_1
    //   151: invokespecial 253	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   154: athrow
    //   155: astore_2
    //   156: new 233	java/lang/StringBuilder
    //   159: dup
    //   160: invokespecial 234	java/lang/StringBuilder:<init>	()V
    //   163: astore_1
    //   164: aload_1
    //   165: ldc_w 257
    //   168: invokevirtual 240	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: aload_1
    //   173: aload_0
    //   174: invokevirtual 240	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   177: pop
    //   178: new 242	java/lang/RuntimeException
    //   181: dup
    //   182: aload_1
    //   183: invokevirtual 245	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   186: aload_2
    //   187: invokespecial 253	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   190: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	191	0	paramString	String
    //   0	191	1	paramArrayOfString	String[]
    //   0	191	2	paramClassLoader	ClassLoader
    //   27	9	3	i	int
    // Exception table:
    //   from	to	target	type
    //   7	23	85	java/lang/SecurityException
    //   7	23	120	java/lang/NoSuchMethodException
    //   0	7	155	java/lang/ClassNotFoundException
  }
  
  public static final IBinder getApplicationObject()
  {
    return mApplicationObject;
  }
  
  private static String getDefaultUserAgent()
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    localStringBuilder.append("Dalvik/");
    localStringBuilder.append(System.getProperty("java.vm.version"));
    localStringBuilder.append(" (Linux; U; Android ");
    String str = Build.VERSION.RELEASE;
    if (str.length() <= 0) {
      str = "1.0";
    }
    localStringBuilder.append(str);
    if ("REL".equals(Build.VERSION.CODENAME))
    {
      str = Build.MODEL;
      if (str.length() > 0)
      {
        localStringBuilder.append("; ");
        localStringBuilder.append(str);
      }
    }
    str = Build.ID;
    if (str.length() > 0)
    {
      localStringBuilder.append(" Build/");
      localStringBuilder.append(str);
    }
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
  
  public static final void main(String[] paramArrayOfString)
  {
    
    if ((paramArrayOfString.length == 2) && (paramArrayOfString[1].equals("application"))) {
      redirectLogStreams();
    }
    commonInit();
    nativeFinishInit();
  }
  
  private static final native void nativeFinishInit();
  
  private static final native void nativeSetExitWithoutCleanup(boolean paramBoolean);
  
  public static void redirectLogStreams()
  {
    System.out.close();
    System.setOut(new AndroidPrintStream(4, "System.out"));
    System.err.close();
    System.setErr(new AndroidPrintStream(5, "System.err"));
  }
  
  public static final void setApplicationObject(IBinder paramIBinder)
  {
    mApplicationObject = paramIBinder;
  }
  
  public static void wtf(String paramString, Throwable paramThrowable, boolean paramBoolean)
  {
    try
    {
      IActivityManager localIActivityManager = ActivityManager.getService();
      IBinder localIBinder = mApplicationObject;
      ApplicationErrorReport.ParcelableCrashInfo localParcelableCrashInfo = new android/app/ApplicationErrorReport$ParcelableCrashInfo;
      localParcelableCrashInfo.<init>(paramThrowable);
      if (localIActivityManager.handleApplicationWtf(localIBinder, paramString, paramBoolean, localParcelableCrashInfo))
      {
        Process.killProcess(Process.myPid());
        System.exit(10);
      }
    }
    catch (Throwable paramString)
    {
      if (!(paramString instanceof DeadObjectException))
      {
        Slog.e("AndroidRuntime", "Error reporting WTF", paramString);
        Slog.e("AndroidRuntime", "Original WTF:", paramThrowable);
      }
    }
  }
  
  static class Arguments
  {
    String[] startArgs;
    String startClass;
    
    Arguments(String[] paramArrayOfString)
      throws IllegalArgumentException
    {
      parseArgs(paramArrayOfString);
    }
    
    private void parseArgs(String[] paramArrayOfString)
      throws IllegalArgumentException
    {
      int j;
      for (int i = 0;; i++)
      {
        j = i;
        if (i >= paramArrayOfString.length) {
          break;
        }
        String str = paramArrayOfString[i];
        if (str.equals("--"))
        {
          j = i + 1;
          break;
        }
        if (!str.startsWith("--"))
        {
          j = i;
          break;
        }
      }
      if (j != paramArrayOfString.length)
      {
        i = j + 1;
        startClass = paramArrayOfString[j];
        startArgs = new String[paramArrayOfString.length - i];
        System.arraycopy(paramArrayOfString, i, startArgs, 0, startArgs.length);
        return;
      }
      throw new IllegalArgumentException("Missing classname argument to RuntimeInit!");
    }
  }
  
  private static class KillApplicationHandler
    implements Thread.UncaughtExceptionHandler
  {
    private final RuntimeInit.LoggingHandler mLoggingHandler;
    
    public KillApplicationHandler(RuntimeInit.LoggingHandler paramLoggingHandler)
    {
      mLoggingHandler = ((RuntimeInit.LoggingHandler)Objects.requireNonNull(paramLoggingHandler));
    }
    
    private void ensureLogging(Thread paramThread, Throwable paramThrowable)
    {
      if (!mLoggingHandler.mTriggered) {
        try
        {
          mLoggingHandler.uncaughtException(paramThread, paramThrowable);
        }
        catch (Throwable paramThread) {}
      }
    }
    
    /* Error */
    public void uncaughtException(Thread paramThread, Throwable paramThrowable)
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: aload_2
      //   3: invokespecial 40	com/android/internal/os/RuntimeInit$KillApplicationHandler:ensureLogging	(Ljava/lang/Thread;Ljava/lang/Throwable;)V
      //   6: invokestatic 44	com/android/internal/os/RuntimeInit:access$000	()Z
      //   9: istore_3
      //   10: iload_3
      //   11: ifeq +15 -> 26
      //   14: invokestatic 50	android/os/Process:myPid	()I
      //   17: invokestatic 54	android/os/Process:killProcess	(I)V
      //   20: bipush 10
      //   22: invokestatic 59	java/lang/System:exit	(I)V
      //   25: return
      //   26: iconst_1
      //   27: invokestatic 63	com/android/internal/os/RuntimeInit:access$002	(Z)Z
      //   30: pop
      //   31: invokestatic 69	android/app/ActivityThread:currentActivityThread	()Landroid/app/ActivityThread;
      //   34: ifnull +9 -> 43
      //   37: invokestatic 69	android/app/ActivityThread:currentActivityThread	()Landroid/app/ActivityThread;
      //   40: invokevirtual 72	android/app/ActivityThread:stopProfiling	()V
      //   43: invokestatic 78	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
      //   46: astore_1
      //   47: invokestatic 82	com/android/internal/os/RuntimeInit:access$100	()Landroid/os/IBinder;
      //   50: astore 4
      //   52: new 84	android/app/ApplicationErrorReport$ParcelableCrashInfo
      //   55: astore 5
      //   57: aload 5
      //   59: aload_2
      //   60: invokespecial 87	android/app/ApplicationErrorReport$ParcelableCrashInfo:<init>	(Ljava/lang/Throwable;)V
      //   63: aload_1
      //   64: aload 4
      //   66: aload 5
      //   68: invokeinterface 93 3 0
      //   73: goto +33 -> 106
      //   76: astore_1
      //   77: goto +41 -> 118
      //   80: astore_1
      //   81: aload_1
      //   82: instanceof 95
      //   85: istore_3
      //   86: iload_3
      //   87: ifeq +6 -> 93
      //   90: goto +16 -> 106
      //   93: ldc 97
      //   95: ldc 99
      //   97: aload_1
      //   98: invokestatic 103	com/android/internal/os/RuntimeInit:access$200	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   101: pop
      //   102: goto +4 -> 106
      //   105: astore_1
      //   106: invokestatic 50	android/os/Process:myPid	()I
      //   109: invokestatic 54	android/os/Process:killProcess	(I)V
      //   112: bipush 10
      //   114: invokestatic 59	java/lang/System:exit	(I)V
      //   117: return
      //   118: invokestatic 50	android/os/Process:myPid	()I
      //   121: invokestatic 54	android/os/Process:killProcess	(I)V
      //   124: bipush 10
      //   126: invokestatic 59	java/lang/System:exit	(I)V
      //   129: aload_1
      //   130: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	131	0	this	KillApplicationHandler
      //   0	131	1	paramThread	Thread
      //   0	131	2	paramThrowable	Throwable
      //   9	78	3	bool	boolean
      //   50	15	4	localIBinder	IBinder
      //   55	12	5	localParcelableCrashInfo	ApplicationErrorReport.ParcelableCrashInfo
      // Exception table:
      //   from	to	target	type
      //   0	10	76	finally
      //   26	43	76	finally
      //   43	73	76	finally
      //   81	86	76	finally
      //   93	102	76	finally
      //   0	10	80	java/lang/Throwable
      //   26	43	80	java/lang/Throwable
      //   43	73	80	java/lang/Throwable
      //   93	102	105	java/lang/Throwable
    }
  }
  
  private static class LoggingHandler
    implements Thread.UncaughtExceptionHandler
  {
    public volatile boolean mTriggered = false;
    
    private LoggingHandler() {}
    
    public void uncaughtException(Thread paramThread, Throwable paramThrowable)
    {
      mTriggered = true;
      if (RuntimeInit.mCrashing) {
        return;
      }
      StringBuilder localStringBuilder;
      if ((RuntimeInit.mApplicationObject == null) && (1000 == Process.myUid()))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("*** FATAL EXCEPTION IN SYSTEM PROCESS: ");
        localStringBuilder.append(paramThread.getName());
        RuntimeInit.Clog_e("AndroidRuntime", localStringBuilder.toString(), paramThrowable);
      }
      else
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("FATAL EXCEPTION: ");
        localStringBuilder.append(paramThread.getName());
        localStringBuilder.append("\n");
        paramThread = ActivityThread.currentProcessName();
        if (paramThread != null)
        {
          localStringBuilder.append("Process: ");
          localStringBuilder.append(paramThread);
          localStringBuilder.append(", ");
        }
        localStringBuilder.append("PID: ");
        localStringBuilder.append(Process.myPid());
        RuntimeInit.Clog_e("AndroidRuntime", localStringBuilder.toString(), paramThrowable);
      }
    }
  }
  
  static class MethodAndArgsCaller
    implements Runnable
  {
    private final String[] mArgs;
    private final Method mMethod;
    
    public MethodAndArgsCaller(Method paramMethod, String[] paramArrayOfString)
    {
      mMethod = paramMethod;
      mArgs = paramArrayOfString;
    }
    
    public void run()
    {
      try
      {
        mMethod.invoke(null, new Object[] { mArgs });
        return;
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        Throwable localThrowable = localInvocationTargetException.getCause();
        if (!(localThrowable instanceof RuntimeException))
        {
          if ((localThrowable instanceof Error)) {
            throw ((Error)localThrowable);
          }
          throw new RuntimeException(localInvocationTargetException);
        }
        throw ((RuntimeException)localThrowable);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new RuntimeException(localIllegalAccessException);
      }
    }
  }
}
