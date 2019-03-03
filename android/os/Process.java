package android.os;

import android.system.Os;
import android.webkit.WebViewZygote;
import dalvik.system.VMRuntime;

public class Process
{
  public static final int AUDIOSERVER_UID = 1041;
  public static final int BLUETOOTH_UID = 1002;
  public static final int CAMERASERVER_UID = 1047;
  public static final int DRM_UID = 1019;
  public static final int FIRST_APPLICATION_CACHE_GID = 20000;
  public static final int FIRST_APPLICATION_UID = 10000;
  public static final int FIRST_ISOLATED_UID = 99000;
  public static final int FIRST_OPTIFLEX_UID = 95000;
  public static final int FIRST_SHARED_APPLICATION_GID = 50000;
  public static final int INCIDENTD_UID = 1067;
  public static final int KEYSTORE_UID = 1017;
  public static final int LAST_APPLICATION_CACHE_GID = 29999;
  public static final int LAST_APPLICATION_UID = 19999;
  public static final int LAST_ISOLATED_UID = 99999;
  public static final int LAST_OPTIFLEX_UID = 95999;
  public static final int LAST_SHARED_APPLICATION_GID = 59999;
  private static final String LOG_TAG = "Process";
  public static final int LOG_UID = 1007;
  public static final int MEDIA_RW_GID = 1023;
  public static final int MEDIA_UID = 1013;
  public static final int NFC_UID = 1027;
  public static final int NOBODY_UID = 9999;
  public static final int OTA_UPDATE_UID = 1061;
  public static final int PACKAGE_INFO_GID = 1032;
  public static final int PHONE_UID = 1001;
  public static final int PROC_CHAR = 2048;
  public static final int PROC_COMBINE = 256;
  public static final int PROC_OUT_FLOAT = 16384;
  public static final int PROC_OUT_LONG = 8192;
  public static final int PROC_OUT_STRING = 4096;
  public static final int PROC_PARENS = 512;
  public static final int PROC_QUOTES = 1024;
  public static final int PROC_SPACE_TERM = 32;
  public static final int PROC_TAB_TERM = 9;
  public static final int PROC_TERM_MASK = 255;
  public static final int PROC_ZERO_TERM = 0;
  public static final int ROOT_UID = 0;
  public static final int SCHED_BATCH = 3;
  public static final int SCHED_FIFO = 1;
  public static final int SCHED_IDLE = 5;
  public static final int SCHED_OTHER = 0;
  public static final int SCHED_RESET_ON_FORK = 1073741824;
  public static final int SCHED_RR = 2;
  public static final String SECONDARY_ZYGOTE_SOCKET = "zygote_secondary";
  public static final int SE_UID = 1068;
  public static final int SHARED_RELRO_UID = 1037;
  public static final int SHARED_USER_GID = 9997;
  public static final int SHELL_UID = 2000;
  public static final int SIGNAL_KILL = 9;
  public static final int SIGNAL_QUIT = 3;
  public static final int SIGNAL_USR1 = 10;
  public static final int SYSTEM_UID = 1000;
  public static final int THREAD_GROUP_ASUS_BOOST_APP = 8;
  public static final int THREAD_GROUP_AUDIO_APP = 3;
  public static final int THREAD_GROUP_AUDIO_SYS = 4;
  public static final int THREAD_GROUP_BG_NONINTERACTIVE = 0;
  public static final int THREAD_GROUP_DEFAULT = -1;
  private static final int THREAD_GROUP_FOREGROUND = 1;
  public static final int THREAD_GROUP_RESTRICTED = 7;
  public static final int THREAD_GROUP_RT_APP = 6;
  public static final int THREAD_GROUP_SYSTEM = 2;
  public static final int THREAD_GROUP_TOP_APP = 5;
  public static final int THREAD_PRIORITY_AUDIO = -16;
  public static final int THREAD_PRIORITY_BACKGROUND = 10;
  public static final int THREAD_PRIORITY_DEFAULT = 0;
  public static final int THREAD_PRIORITY_DISPLAY = -4;
  public static final int THREAD_PRIORITY_FOREGROUND = -2;
  public static final int THREAD_PRIORITY_LESS_FAVORABLE = 1;
  public static final int THREAD_PRIORITY_LOWEST = 19;
  public static final int THREAD_PRIORITY_MORE_FAVORABLE = -1;
  public static final int THREAD_PRIORITY_URGENT_AUDIO = -19;
  public static final int THREAD_PRIORITY_URGENT_DISPLAY = -8;
  public static final int THREAD_PRIORITY_VIDEO = -10;
  public static final int VPN_UID = 1016;
  public static final int WEBVIEW_ZYGOTE_UID = 1053;
  public static final int WIFI_UID = 1010;
  public static final String ZYGOTE_SOCKET = "zygote";
  private static long sStartElapsedRealtime;
  private static long sStartUptimeMillis;
  public static final ZygoteProcess zygoteProcess = new ZygoteProcess("zygote", "zygote_secondary");
  
  public Process() {}
  
  public static final native long getCachedMemory();
  
  public static final native long getElapsedCpuTime();
  
  public static final native int[] getExclusiveCores();
  
  public static final native long getFreeMemory();
  
  public static final native int getGidForName(String paramString);
  
  public static final int getParentPid(int paramInt)
  {
    long[] arrayOfLong = new long[1];
    arrayOfLong[0] = -1L;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("/proc/");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("/status");
    readProcLines(localStringBuilder.toString(), new String[] { "PPid:" }, arrayOfLong);
    return (int)arrayOfLong[0];
  }
  
  public static final native int[] getPids(String paramString, int[] paramArrayOfInt);
  
  public static final native int[] getPidsForCommands(String[] paramArrayOfString);
  
  public static final native int getProcessGroup(int paramInt)
    throws IllegalArgumentException, SecurityException;
  
  public static final native long getPss(int paramInt);
  
  public static final long getStartElapsedRealtime()
  {
    return sStartElapsedRealtime;
  }
  
  public static final long getStartUptimeMillis()
  {
    return sStartUptimeMillis;
  }
  
  public static final int getThreadGroupLeader(int paramInt)
  {
    long[] arrayOfLong = new long[1];
    arrayOfLong[0] = -1L;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("/proc/");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("/status");
    readProcLines(localStringBuilder.toString(), new String[] { "Tgid:" }, arrayOfLong);
    return (int)arrayOfLong[0];
  }
  
  public static final native int getThreadPriority(int paramInt)
    throws IllegalArgumentException;
  
  public static final native int getThreadScheduler(int paramInt)
    throws IllegalArgumentException;
  
  public static final native long getTotalMemory();
  
  public static final native int getUidForName(String paramString);
  
  public static final int getUidForPid(int paramInt)
  {
    long[] arrayOfLong = new long[1];
    arrayOfLong[0] = -1L;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("/proc/");
    localStringBuilder.append(paramInt);
    localStringBuilder.append("/status");
    readProcLines(localStringBuilder.toString(), new String[] { "Uid:" }, arrayOfLong);
    return (int)arrayOfLong[0];
  }
  
  public static final boolean is64Bit()
  {
    return VMRuntime.getRuntime().is64Bit();
  }
  
  public static boolean isApplicationUid(int paramInt)
  {
    return UserHandle.isApp(paramInt);
  }
  
  public static boolean isCoreUid(int paramInt)
  {
    return UserHandle.isCore(paramInt);
  }
  
  public static final boolean isIsolated()
  {
    return isIsolated(myUid());
  }
  
  public static final boolean isIsolated(int paramInt)
  {
    paramInt = UserHandle.getAppId(paramInt);
    boolean bool;
    if ((paramInt >= 99000) && (paramInt <= 99999)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  /* Error */
  public static final boolean isThreadInProcess(int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: invokestatic 277	android/os/StrictMode:allowThreadDiskReads	()Landroid/os/StrictMode$ThreadPolicy;
    //   3: astore_2
    //   4: new 185	java/lang/StringBuilder
    //   7: astore_3
    //   8: aload_3
    //   9: invokespecial 186	java/lang/StringBuilder:<init>	()V
    //   12: aload_3
    //   13: ldc -68
    //   15: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   18: pop
    //   19: aload_3
    //   20: iload_0
    //   21: invokevirtual 195	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: aload_3
    //   26: ldc_w 279
    //   29: invokevirtual 192	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   32: pop
    //   33: aload_3
    //   34: iload_1
    //   35: invokevirtual 195	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   38: pop
    //   39: aload_3
    //   40: invokevirtual 201	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   43: getstatic 284	android/system/OsConstants:F_OK	I
    //   46: invokestatic 290	android/system/Os:access	(Ljava/lang/String;I)Z
    //   49: istore 4
    //   51: iload 4
    //   53: ifeq +9 -> 62
    //   56: aload_2
    //   57: invokestatic 294	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   60: iconst_1
    //   61: ireturn
    //   62: aload_2
    //   63: invokestatic 294	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   66: iconst_0
    //   67: ireturn
    //   68: astore_3
    //   69: aload_2
    //   70: invokestatic 294	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   73: aload_3
    //   74: athrow
    //   75: astore_3
    //   76: aload_2
    //   77: invokestatic 294	android/os/StrictMode:setThreadPolicy	(Landroid/os/StrictMode$ThreadPolicy;)V
    //   80: iconst_0
    //   81: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	82	0	paramInt1	int
    //   0	82	1	paramInt2	int
    //   3	74	2	localThreadPolicy	StrictMode.ThreadPolicy
    //   7	33	3	localStringBuilder	StringBuilder
    //   68	6	3	localObject	Object
    //   75	1	3	localException	Exception
    //   49	3	4	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   4	51	68	finally
    //   4	51	75	java/lang/Exception
  }
  
  public static final void killProcess(int paramInt)
  {
    sendSignal(paramInt, 9);
  }
  
  public static final native int killProcessGroup(int paramInt1, int paramInt2);
  
  public static final void killProcessQuiet(int paramInt)
  {
    sendSignalQuiet(paramInt, 9);
  }
  
  public static final int myPid()
  {
    return Os.getpid();
  }
  
  public static final int myPpid()
  {
    return Os.getppid();
  }
  
  public static final int myTid()
  {
    return Os.gettid();
  }
  
  public static final int myUid()
  {
    return Os.getuid();
  }
  
  public static UserHandle myUserHandle()
  {
    return UserHandle.of(UserHandle.getUserId(myUid()));
  }
  
  public static final native boolean parseProcLine(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int[] paramArrayOfInt, String[] paramArrayOfString, long[] paramArrayOfLong, float[] paramArrayOfFloat);
  
  public static final native boolean readProcFile(String paramString, int[] paramArrayOfInt, String[] paramArrayOfString, long[] paramArrayOfLong, float[] paramArrayOfFloat);
  
  public static final native void readProcLines(String paramString, String[] paramArrayOfString, long[] paramArrayOfLong);
  
  public static final native void removeAllProcessGroups();
  
  public static final native void sendSignal(int paramInt1, int paramInt2);
  
  public static final native void sendSignalQuiet(int paramInt1, int paramInt2);
  
  public static final native void setArgV0(String paramString);
  
  public static final native void setCanSelfBackground(boolean paramBoolean);
  
  public static final native void setCgroupProcsProcessGroup(int paramInt1, int paramInt2, int paramInt3)
    throws IllegalArgumentException, SecurityException;
  
  public static final native int setGid(int paramInt);
  
  public static final native void setProcessGroup(int paramInt1, int paramInt2)
    throws IllegalArgumentException, SecurityException;
  
  public static final void setStartTimes(long paramLong1, long paramLong2)
  {
    sStartElapsedRealtime = paramLong1;
    sStartUptimeMillis = paramLong2;
  }
  
  public static final native boolean setSwappiness(int paramInt, boolean paramBoolean);
  
  public static final native void setThreadGroup(int paramInt1, int paramInt2)
    throws IllegalArgumentException, SecurityException;
  
  public static final native void setThreadGroupAndCpuset(int paramInt1, int paramInt2)
    throws IllegalArgumentException, SecurityException;
  
  public static final native void setThreadPriority(int paramInt)
    throws IllegalArgumentException, SecurityException;
  
  public static final native void setThreadPriority(int paramInt1, int paramInt2)
    throws IllegalArgumentException, SecurityException;
  
  public static final native void setThreadScheduler(int paramInt1, int paramInt2, int paramInt3)
    throws IllegalArgumentException;
  
  public static final native int setUid(int paramInt);
  
  public static final ProcessStartResult start(String paramString1, String paramString2, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String[] paramArrayOfString)
  {
    return zygoteProcess.start(paramString1, paramString2, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramString3, paramString4, paramString5, paramString6, paramString7, paramArrayOfString);
  }
  
  public static final ProcessStartResult startWebView(String paramString1, String paramString2, int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3, int paramInt4, int paramInt5, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String[] paramArrayOfString)
  {
    return WebViewZygote.getProcess().start(paramString1, paramString2, paramInt1, paramInt2, paramArrayOfInt, paramInt3, paramInt4, paramInt5, paramString3, paramString4, paramString5, paramString6, paramString7, paramArrayOfString);
  }
  
  @Deprecated
  public static final boolean supportsProcesses()
  {
    return true;
  }
  
  public static final class ProcessStartResult
  {
    public int pid;
    public boolean usingWrapper;
    
    public ProcessStartResult() {}
  }
}
