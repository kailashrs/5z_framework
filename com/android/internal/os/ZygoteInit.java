package com.android.internal.os;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.icu.impl.CacheValue;
import android.icu.impl.CacheValue.Strength;
import android.icu.text.DecimalFormatSymbols;
import android.icu.util.ULocale;
import android.opengl.EGL14;
import android.os.Build;
import android.os.Environment;
import android.os.IInstalld;
import android.os.IInstalld.Stub;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.ZygoteProcess;
import android.os.storage.StorageManager;
import android.security.keystore.AndroidKeyStoreProvider;
import android.system.Os;
import android.system.OsConstants;
import android.text.Hyphenator;
import android.util.Log;
import android.util.TimingsTraceLog;
import android.webkit.WebViewFactory;
import android.widget.TextView;
import com.android.internal.util.Preconditions;
import dalvik.system.DexFile;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;

public class ZygoteInit
{
  private static final String ABI_LIST_ARG = "--abi-list=";
  private static final int LOG_BOOT_PROGRESS_PRELOAD_END = 3030;
  private static final int LOG_BOOT_PROGRESS_PRELOAD_START = 3020;
  private static final String PRELOADED_CLASSES = "/system/etc/preloaded-classes";
  private static final int PRELOAD_GC_THRESHOLD = 50000;
  public static final boolean PRELOAD_RESOURCES = true;
  private static final String PROPERTY_DISABLE_OPENGL_PRELOADING = "ro.zygote.disable_gl_preload";
  private static final String PROPERTY_GFX_DRIVER = "ro.gfx.driver.0";
  private static final int ROOT_GID = 0;
  private static final int ROOT_UID = 0;
  private static final String SOCKET_NAME_ARG = "--socket-name=";
  private static final String TAG = "Zygote";
  private static final int UNPRIVILEGED_GID = 9999;
  private static final int UNPRIVILEGED_UID = 9999;
  private static Resources mResources;
  private static boolean sPreloadComplete;
  
  private ZygoteInit() {}
  
  private static void beginIcuCachePinning()
  {
    Log.i("Zygote", "Installing ICU cache reference pinning...");
    CacheValue.setStrength(CacheValue.Strength.STRONG);
    Log.i("Zygote", "Preloading ICU data...");
    ULocale[] arrayOfULocale = new ULocale[3];
    ULocale localULocale = ULocale.ROOT;
    int i = 0;
    arrayOfULocale[0] = localULocale;
    arrayOfULocale[1] = ULocale.US;
    arrayOfULocale[2] = ULocale.getDefault();
    int j = arrayOfULocale.length;
    while (i < j)
    {
      new DecimalFormatSymbols(arrayOfULocale[i]);
      i++;
    }
  }
  
  static final Runnable childZygoteInit(int paramInt, String[] paramArrayOfString, ClassLoader paramClassLoader)
  {
    paramArrayOfString = new RuntimeInit.Arguments(paramArrayOfString);
    return RuntimeInit.findStaticMain(startClass, startArgs, paramClassLoader);
  }
  
  static ClassLoader createPathClassLoader(String paramString, int paramInt)
  {
    String str = System.getProperty("java.library.path");
    return ClassLoaderFactory.createClassLoader(paramString, str, str, ClassLoader.getSystemClassLoader(), paramInt, true, null);
  }
  
  private static String encodeSystemServerClassPath(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (!paramString1.isEmpty()))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString1);
      localStringBuilder.append(":");
      localStringBuilder.append(paramString2);
      paramString1 = localStringBuilder.toString();
    }
    else
    {
      paramString1 = paramString2;
    }
    return paramString1;
  }
  
  private static void endIcuCachePinning()
  {
    CacheValue.setStrength(CacheValue.Strength.SOFT);
    Log.i("Zygote", "Uninstalled ICU cache reference pinning...");
  }
  
  /* Error */
  private static Runnable forkSystemServer(String paramString1, String paramString2, ZygoteServer paramZygoteServer)
  {
    // Byte code:
    //   0: bipush 13
    //   2: newarray int
    //   4: dup
    //   5: iconst_0
    //   6: getstatic 168	android/system/OsConstants:CAP_IPC_LOCK	I
    //   9: iastore
    //   10: dup
    //   11: iconst_1
    //   12: getstatic 171	android/system/OsConstants:CAP_KILL	I
    //   15: iastore
    //   16: dup
    //   17: iconst_2
    //   18: getstatic 174	android/system/OsConstants:CAP_NET_ADMIN	I
    //   21: iastore
    //   22: dup
    //   23: iconst_3
    //   24: getstatic 177	android/system/OsConstants:CAP_NET_BIND_SERVICE	I
    //   27: iastore
    //   28: dup
    //   29: iconst_4
    //   30: getstatic 180	android/system/OsConstants:CAP_NET_BROADCAST	I
    //   33: iastore
    //   34: dup
    //   35: iconst_5
    //   36: getstatic 183	android/system/OsConstants:CAP_NET_RAW	I
    //   39: iastore
    //   40: dup
    //   41: bipush 6
    //   43: getstatic 186	android/system/OsConstants:CAP_SYS_MODULE	I
    //   46: iastore
    //   47: dup
    //   48: bipush 7
    //   50: getstatic 189	android/system/OsConstants:CAP_SYS_NICE	I
    //   53: iastore
    //   54: dup
    //   55: bipush 8
    //   57: getstatic 192	android/system/OsConstants:CAP_SYS_PTRACE	I
    //   60: iastore
    //   61: dup
    //   62: bipush 9
    //   64: getstatic 195	android/system/OsConstants:CAP_SYS_TIME	I
    //   67: iastore
    //   68: dup
    //   69: bipush 10
    //   71: getstatic 198	android/system/OsConstants:CAP_SYS_TTY_CONFIG	I
    //   74: iastore
    //   75: dup
    //   76: bipush 11
    //   78: getstatic 201	android/system/OsConstants:CAP_WAKE_ALARM	I
    //   81: iastore
    //   82: dup
    //   83: bipush 12
    //   85: getstatic 204	android/system/OsConstants:CAP_BLOCK_SUSPEND	I
    //   88: iastore
    //   89: invokestatic 208	com/android/internal/os/ZygoteInit:posixCapabilitiesAsBits	([I)J
    //   92: lstore_3
    //   93: new 210	android/system/StructCapUserHeader
    //   96: dup
    //   97: getstatic 213	android/system/OsConstants:_LINUX_CAPABILITY_VERSION_3	I
    //   100: iconst_0
    //   101: invokespecial 216	android/system/StructCapUserHeader:<init>	(II)V
    //   104: astore 5
    //   106: aload 5
    //   108: invokestatic 222	android/system/Os:capget	(Landroid/system/StructCapUserHeader;)[Landroid/system/StructCapUserData;
    //   111: astore 5
    //   113: aload 5
    //   115: iconst_0
    //   116: aaload
    //   117: getfield 227	android/system/StructCapUserData:effective	I
    //   120: i2l
    //   121: lstore 6
    //   123: aload 5
    //   125: iconst_1
    //   126: aaload
    //   127: getfield 227	android/system/StructCapUserData:effective	I
    //   130: i2l
    //   131: bipush 32
    //   133: lshl
    //   134: lload 6
    //   136: lor
    //   137: lload_3
    //   138: land
    //   139: lstore_3
    //   140: new 140	java/lang/StringBuilder
    //   143: dup
    //   144: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   147: astore 5
    //   149: aload 5
    //   151: ldc -27
    //   153: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: pop
    //   157: aload 5
    //   159: lload_3
    //   160: invokevirtual 232	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   163: pop
    //   164: aload 5
    //   166: ldc -22
    //   168: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   171: pop
    //   172: aload 5
    //   174: lload_3
    //   175: invokevirtual 232	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   178: pop
    //   179: aload 5
    //   181: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   184: astore 8
    //   186: new 236	com/android/internal/os/ZygoteConnection$Arguments
    //   189: astore 5
    //   191: aload 5
    //   193: bipush 8
    //   195: anewarray 134	java/lang/String
    //   198: dup
    //   199: iconst_0
    //   200: ldc -18
    //   202: aastore
    //   203: dup
    //   204: iconst_1
    //   205: ldc -16
    //   207: aastore
    //   208: dup
    //   209: iconst_2
    //   210: ldc -14
    //   212: aastore
    //   213: dup
    //   214: iconst_3
    //   215: aload 8
    //   217: aastore
    //   218: dup
    //   219: iconst_4
    //   220: ldc -12
    //   222: aastore
    //   223: dup
    //   224: iconst_5
    //   225: ldc -10
    //   227: aastore
    //   228: dup
    //   229: bipush 6
    //   231: ldc -8
    //   233: aastore
    //   234: dup
    //   235: bipush 7
    //   237: ldc -6
    //   239: aastore
    //   240: invokespecial 251	com/android/internal/os/ZygoteConnection$Arguments:<init>	([Ljava/lang/String;)V
    //   243: aload 5
    //   245: invokestatic 257	com/android/internal/os/ZygoteConnection:applyDebuggerSystemProperty	(Lcom/android/internal/os/ZygoteConnection$Arguments;)V
    //   248: aload 5
    //   250: invokestatic 260	com/android/internal/os/ZygoteConnection:applyInvokeWithSystemProperty	(Lcom/android/internal/os/ZygoteConnection$Arguments;)V
    //   253: ldc_w 262
    //   256: iconst_0
    //   257: invokestatic 268	android/os/SystemProperties:getBoolean	(Ljava/lang/String;Z)Z
    //   260: istore 9
    //   262: iload 9
    //   264: ifeq +24 -> 288
    //   267: aload 5
    //   269: aload 5
    //   271: getfield 271	com/android/internal/os/ZygoteConnection$Arguments:runtimeFlags	I
    //   274: sipush 16384
    //   277: ior
    //   278: putfield 271	com/android/internal/os/ZygoteConnection$Arguments:runtimeFlags	I
    //   281: goto +7 -> 288
    //   284: astore_0
    //   285: goto +90 -> 375
    //   288: aload 5
    //   290: getfield 274	com/android/internal/os/ZygoteConnection$Arguments:uid	I
    //   293: istore 10
    //   295: aload 5
    //   297: getfield 277	com/android/internal/os/ZygoteConnection$Arguments:gid	I
    //   300: istore 11
    //   302: aload 5
    //   304: getfield 281	com/android/internal/os/ZygoteConnection$Arguments:gids	[I
    //   307: astore 8
    //   309: aload 5
    //   311: getfield 271	com/android/internal/os/ZygoteConnection$Arguments:runtimeFlags	I
    //   314: istore 12
    //   316: aload 5
    //   318: getfield 285	com/android/internal/os/ZygoteConnection$Arguments:permittedCapabilities	J
    //   321: lstore_3
    //   322: iload 10
    //   324: iload 11
    //   326: aload 8
    //   328: iload 12
    //   330: aconst_null
    //   331: lload_3
    //   332: aload 5
    //   334: getfield 288	com/android/internal/os/ZygoteConnection$Arguments:effectiveCapabilities	J
    //   337: invokestatic 293	com/android/internal/os/Zygote:forkSystemServer	(II[II[[IJJ)I
    //   340: istore 10
    //   342: iload 10
    //   344: ifne +24 -> 368
    //   347: aload_0
    //   348: invokestatic 297	com/android/internal/os/ZygoteInit:hasSecondZygote	(Ljava/lang/String;)Z
    //   351: ifeq +7 -> 358
    //   354: aload_1
    //   355: invokestatic 301	com/android/internal/os/ZygoteInit:waitForSecondaryZygote	(Ljava/lang/String;)V
    //   358: aload_2
    //   359: invokevirtual 306	com/android/internal/os/ZygoteServer:closeServerSocket	()V
    //   362: aload 5
    //   364: invokestatic 310	com/android/internal/os/ZygoteInit:handleSystemServerProcess	(Lcom/android/internal/os/ZygoteConnection$Arguments;)Ljava/lang/Runnable;
    //   367: areturn
    //   368: aconst_null
    //   369: areturn
    //   370: astore_0
    //   371: goto +4 -> 375
    //   374: astore_0
    //   375: new 312	java/lang/RuntimeException
    //   378: dup
    //   379: aload_0
    //   380: invokespecial 315	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   383: athrow
    //   384: astore_0
    //   385: new 312	java/lang/RuntimeException
    //   388: dup
    //   389: ldc_w 317
    //   392: aload_0
    //   393: invokespecial 320	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   396: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	397	0	paramString1	String
    //   0	397	1	paramString2	String
    //   0	397	2	paramZygoteServer	ZygoteServer
    //   92	240	3	l1	long
    //   104	259	5	localObject1	Object
    //   121	14	6	l2	long
    //   184	143	8	localObject2	Object
    //   260	3	9	bool	boolean
    //   293	50	10	i	int
    //   300	25	11	j	int
    //   314	15	12	k	int
    // Exception table:
    //   from	to	target	type
    //   267	281	284	java/lang/IllegalArgumentException
    //   322	342	370	java/lang/IllegalArgumentException
    //   186	262	374	java/lang/IllegalArgumentException
    //   288	322	374	java/lang/IllegalArgumentException
    //   106	113	384	android/system/ErrnoException
  }
  
  static void gcAndFinalize()
  {
    VMRuntime localVMRuntime = VMRuntime.getRuntime();
    System.gc();
    localVMRuntime.runFinalizationSync();
    System.gc();
  }
  
  private static String getSystemServerClassLoaderContext(String paramString)
  {
    if (paramString == null)
    {
      paramString = "PCL[]";
    }
    else
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("PCL[");
      localStringBuilder.append(paramString);
      localStringBuilder.append("]");
      paramString = localStringBuilder.toString();
    }
    return paramString;
  }
  
  private static Runnable handleSystemServerProcess(ZygoteConnection.Arguments paramArguments)
  {
    Os.umask(OsConstants.S_IRWXG | OsConstants.S_IRWXO);
    if (niceName != null) {
      Process.setArgV0(niceName);
    }
    String str = Os.getenv("SYSTEMSERVERCLASSPATH");
    if (str != null)
    {
      performSystemServerDexOpt(str);
      if ((SystemProperties.getBoolean("dalvik.vm.profilesystemserver", false)) && ((Build.IS_USERDEBUG) || (Build.IS_ENG))) {
        try
        {
          prepareSystemServerProfile(str);
        }
        catch (Exception localException)
        {
          Log.wtf("Zygote", "Failed to set up system server profile", localException);
        }
      }
    }
    if (invokeWith != null)
    {
      String[] arrayOfString = remainingArgs;
      localObject = arrayOfString;
      if (str != null)
      {
        localObject = new String[arrayOfString.length + 2];
        localObject[0] = "-cp";
        localObject[1] = str;
        System.arraycopy(arrayOfString, 0, localObject, 2, arrayOfString.length);
      }
      WrapperInit.execApplication(invokeWith, niceName, targetSdkVersion, VMRuntime.getCurrentInstructionSet(), null, (String[])localObject);
      throw new IllegalStateException("Unexpected return from WrapperInit.execApplication");
    }
    Object localObject = null;
    if (str != null)
    {
      localObject = createPathClassLoader(str, targetSdkVersion);
      Thread.currentThread().setContextClassLoader((ClassLoader)localObject);
    }
    return zygoteInit(targetSdkVersion, remainingArgs, (ClassLoader)localObject);
  }
  
  private static boolean hasSecondZygote(String paramString)
  {
    return SystemProperties.get("ro.product.cpu.abilist").equals(paramString) ^ true;
  }
  
  static boolean isPreloadComplete()
  {
    return sPreloadComplete;
  }
  
  public static void lazyPreload()
  {
    Preconditions.checkState(sPreloadComplete ^ true);
    Log.i("Zygote", "Lazily preloading resources.");
    preload(new TimingsTraceLog("ZygoteInitTiming_lazy", 16384L));
  }
  
  /* Error */
  public static void main(String[] paramArrayOfString)
  {
    // Byte code:
    //   0: new 303	com/android/internal/os/ZygoteServer
    //   3: dup
    //   4: invokespecial 468	com/android/internal/os/ZygoteServer:<init>	()V
    //   7: astore_1
    //   8: invokestatic 473	dalvik/system/ZygoteHooks:startZygoteNoThreadCreation	()V
    //   11: iconst_0
    //   12: iconst_0
    //   13: invokestatic 476	android/system/Os:setpgid	(II)V
    //   16: ldc_w 478
    //   19: ldc_w 480
    //   22: invokestatic 435	android/os/SystemProperties:get	(Ljava/lang/String;)Ljava/lang/String;
    //   25: invokevirtual 439	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   28: ifne +14 -> 42
    //   31: aconst_null
    //   32: ldc_w 482
    //   35: invokestatic 488	android/os/SystemClock:elapsedRealtime	()J
    //   38: l2i
    //   39: invokestatic 494	com/android/internal/logging/MetricsLogger:histogram	(Landroid/content/Context;Ljava/lang/String;I)V
    //   42: invokestatic 497	android/os/Process:is64Bit	()Z
    //   45: ifeq +10 -> 55
    //   48: ldc_w 499
    //   51: astore_2
    //   52: goto +7 -> 59
    //   55: ldc_w 501
    //   58: astore_2
    //   59: new 453	android/util/TimingsTraceLog
    //   62: astore_3
    //   63: aload_3
    //   64: aload_2
    //   65: ldc2_w 456
    //   68: invokespecial 460	android/util/TimingsTraceLog:<init>	(Ljava/lang/String;J)V
    //   71: aload_3
    //   72: ldc_w 503
    //   75: invokevirtual 506	android/util/TimingsTraceLog:traceBegin	(Ljava/lang/String;)V
    //   78: invokestatic 509	com/android/internal/os/RuntimeInit:enableDdms	()V
    //   81: iconst_0
    //   82: istore 4
    //   84: ldc_w 511
    //   87: astore 5
    //   89: aconst_null
    //   90: astore_2
    //   91: iconst_0
    //   92: istore 6
    //   94: iconst_1
    //   95: istore 7
    //   97: iload 7
    //   99: aload_0
    //   100: arraylength
    //   101: if_icmpge +143 -> 244
    //   104: ldc_w 513
    //   107: aload_0
    //   108: iload 7
    //   110: aaload
    //   111: invokevirtual 439	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   114: ifeq +9 -> 123
    //   117: iconst_1
    //   118: istore 4
    //   120: goto +76 -> 196
    //   123: ldc_w 515
    //   126: aload_0
    //   127: iload 7
    //   129: aaload
    //   130: invokevirtual 439	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   133: ifeq +9 -> 142
    //   136: iconst_1
    //   137: istore 6
    //   139: goto +57 -> 196
    //   142: aload_0
    //   143: iload 7
    //   145: aaload
    //   146: ldc 8
    //   148: invokevirtual 518	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   151: ifeq +19 -> 170
    //   154: aload_0
    //   155: iload 7
    //   157: aaload
    //   158: ldc 8
    //   160: invokevirtual 522	java/lang/String:length	()I
    //   163: invokevirtual 526	java/lang/String:substring	(I)Ljava/lang/String;
    //   166: astore_2
    //   167: goto +29 -> 196
    //   170: aload_0
    //   171: iload 7
    //   173: aaload
    //   174: ldc 33
    //   176: invokevirtual 518	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   179: ifeq +23 -> 202
    //   182: aload_0
    //   183: iload 7
    //   185: aaload
    //   186: ldc 33
    //   188: invokevirtual 522	java/lang/String:length	()I
    //   191: invokevirtual 526	java/lang/String:substring	(I)Ljava/lang/String;
    //   194: astore 5
    //   196: iinc 7 1
    //   199: goto -102 -> 97
    //   202: new 312	java/lang/RuntimeException
    //   205: astore 5
    //   207: new 140	java/lang/StringBuilder
    //   210: astore_2
    //   211: aload_2
    //   212: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   215: aload_2
    //   216: ldc_w 528
    //   219: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   222: pop
    //   223: aload_2
    //   224: aload_0
    //   225: iload 7
    //   227: aaload
    //   228: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   231: pop
    //   232: aload 5
    //   234: aload_2
    //   235: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   238: invokespecial 529	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   241: aload 5
    //   243: athrow
    //   244: aload_2
    //   245: ifnull +145 -> 390
    //   248: aload_1
    //   249: aload 5
    //   251: invokevirtual 532	com/android/internal/os/ZygoteServer:registerServerSocketFromEnv	(Ljava/lang/String;)V
    //   254: iload 6
    //   256: ifne +41 -> 297
    //   259: aload_3
    //   260: ldc_w 534
    //   263: invokevirtual 506	android/util/TimingsTraceLog:traceBegin	(Ljava/lang/String;)V
    //   266: sipush 3020
    //   269: invokestatic 537	android/os/SystemClock:uptimeMillis	()J
    //   272: invokestatic 543	android/util/EventLog:writeEvent	(IJ)I
    //   275: pop
    //   276: aload_3
    //   277: invokestatic 464	com/android/internal/os/ZygoteInit:preload	(Landroid/util/TimingsTraceLog;)V
    //   280: sipush 3030
    //   283: invokestatic 537	android/os/SystemClock:uptimeMillis	()J
    //   286: invokestatic 543	android/util/EventLog:writeEvent	(IJ)I
    //   289: pop
    //   290: aload_3
    //   291: invokevirtual 546	android/util/TimingsTraceLog:traceEnd	()V
    //   294: goto +6 -> 300
    //   297: invokestatic 549	com/android/internal/os/Zygote:resetNicePriority	()V
    //   300: aload_3
    //   301: ldc_w 551
    //   304: invokevirtual 506	android/util/TimingsTraceLog:traceBegin	(Ljava/lang/String;)V
    //   307: invokestatic 553	com/android/internal/os/ZygoteInit:gcAndFinalize	()V
    //   310: aload_3
    //   311: invokevirtual 546	android/util/TimingsTraceLog:traceEnd	()V
    //   314: aload_3
    //   315: invokevirtual 546	android/util/TimingsTraceLog:traceEnd	()V
    //   318: iconst_0
    //   319: iconst_0
    //   320: invokestatic 559	android/os/Trace:setTracingEnabled	(ZI)V
    //   323: invokestatic 562	com/android/internal/os/Zygote:nativeSecurityInit	()V
    //   326: invokestatic 565	com/android/internal/os/Zygote:nativeUnmountStorageOnInit	()V
    //   329: invokestatic 568	dalvik/system/ZygoteHooks:stopZygoteNoThreadCreation	()V
    //   332: iload 4
    //   334: ifeq +26 -> 360
    //   337: aload_2
    //   338: aload 5
    //   340: aload_1
    //   341: invokestatic 570	com/android/internal/os/ZygoteInit:forkSystemServer	(Ljava/lang/String;Ljava/lang/String;Lcom/android/internal/os/ZygoteServer;)Ljava/lang/Runnable;
    //   344: astore_0
    //   345: aload_0
    //   346: ifnull +14 -> 360
    //   349: aload_0
    //   350: invokeinterface 575 1 0
    //   355: aload_1
    //   356: invokevirtual 306	com/android/internal/os/ZygoteServer:closeServerSocket	()V
    //   359: return
    //   360: ldc 36
    //   362: ldc_w 577
    //   365: invokestatic 56	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   368: pop
    //   369: aload_1
    //   370: aload_2
    //   371: invokevirtual 581	com/android/internal/os/ZygoteServer:runSelectLoop	(Ljava/lang/String;)Ljava/lang/Runnable;
    //   374: astore_0
    //   375: aload_1
    //   376: invokevirtual 306	com/android/internal/os/ZygoteServer:closeServerSocket	()V
    //   379: aload_0
    //   380: ifnull +9 -> 389
    //   383: aload_0
    //   384: invokeinterface 575 1 0
    //   389: return
    //   390: new 312	java/lang/RuntimeException
    //   393: astore_0
    //   394: aload_0
    //   395: ldc_w 583
    //   398: invokespecial 529	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   401: aload_0
    //   402: athrow
    //   403: astore_0
    //   404: goto +16 -> 420
    //   407: astore_0
    //   408: ldc 36
    //   410: ldc_w 585
    //   413: aload_0
    //   414: invokestatic 588	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   417: pop
    //   418: aload_0
    //   419: athrow
    //   420: aload_1
    //   421: invokevirtual 306	com/android/internal/os/ZygoteServer:closeServerSocket	()V
    //   424: aload_0
    //   425: athrow
    //   426: astore_0
    //   427: new 312	java/lang/RuntimeException
    //   430: dup
    //   431: ldc_w 590
    //   434: aload_0
    //   435: invokespecial 320	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   438: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	439	0	paramArrayOfString	String[]
    //   7	414	1	localZygoteServer	ZygoteServer
    //   51	320	2	localObject1	Object
    //   62	253	3	localTimingsTraceLog	TimingsTraceLog
    //   82	251	4	i	int
    //   87	252	5	localObject2	Object
    //   92	163	6	j	int
    //   95	131	7	k	int
    // Exception table:
    //   from	to	target	type
    //   16	42	403	finally
    //   42	48	403	finally
    //   59	81	403	finally
    //   97	117	403	finally
    //   123	136	403	finally
    //   142	167	403	finally
    //   170	196	403	finally
    //   202	244	403	finally
    //   248	254	403	finally
    //   259	294	403	finally
    //   297	300	403	finally
    //   300	332	403	finally
    //   337	345	403	finally
    //   349	355	403	finally
    //   360	375	403	finally
    //   390	403	403	finally
    //   408	420	403	finally
    //   16	42	407	java/lang/Throwable
    //   42	48	407	java/lang/Throwable
    //   59	81	407	java/lang/Throwable
    //   97	117	407	java/lang/Throwable
    //   123	136	407	java/lang/Throwable
    //   142	167	407	java/lang/Throwable
    //   170	196	407	java/lang/Throwable
    //   202	244	407	java/lang/Throwable
    //   248	254	407	java/lang/Throwable
    //   259	294	407	java/lang/Throwable
    //   297	300	407	java/lang/Throwable
    //   300	332	407	java/lang/Throwable
    //   337	345	407	java/lang/Throwable
    //   349	355	407	java/lang/Throwable
    //   360	375	407	java/lang/Throwable
    //   390	403	407	java/lang/Throwable
    //   11	16	426	android/system/ErrnoException
  }
  
  private static native void nativePreloadAppProcessHALs();
  
  private static final native void nativeZygoteInit();
  
  private static void performSystemServerDexOpt(String paramString)
  {
    String[] arrayOfString = paramString.split(":");
    IInstalld localIInstalld = IInstalld.Stub.asInterface(ServiceManager.getService("installd"));
    String str1 = VMRuntime.getRuntime().vmInstructionSet();
    int i = arrayOfString.length;
    paramString = "";
    for (int j = 0; j < i; j++)
    {
      String str2 = arrayOfString[j];
      Object localObject1 = SystemProperties.get("dalvik.vm.systemservercompilerfilter", "speed");
      try
      {
        int k;
        Object localObject2;
        try
        {
          k = DexFile.getDexOptNeeded(str2, str1, (String)localObject1, null, false, false);
        }
        catch (IOException localIOException)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("Error checking classpath element for system server: ");
          ((StringBuilder)localObject2).append(str2);
          Log.w("Zygote", ((StringBuilder)localObject2).toString(), localIOException);
          k = 0;
        }
        if (k != 0)
        {
          localObject2 = StorageManager.UUID_PRIVATE_INTERNAL;
          String str3 = getSystemServerClassLoaderContext(paramString);
          try
          {
            localIInstalld.dexopt(str2, 1000, "*", str1, k, null, 0, (String)localObject1, (String)localObject2, str3, null, false, 0, null, null, "server-dexopt");
          }
          catch (RemoteException|ServiceSpecificException localRemoteException)
          {
            localObject1 = new StringBuilder();
            ((StringBuilder)localObject1).append("Failed compiling classpath element for system server: ");
            ((StringBuilder)localObject1).append(str2);
            Log.w("Zygote", ((StringBuilder)localObject1).toString(), localRemoteException);
          }
        }
        paramString = encodeSystemServerClassPath(paramString, str2);
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Missing classpath element for system server: ");
        localStringBuilder.append(str2);
        Log.w("Zygote", localStringBuilder.toString());
      }
    }
  }
  
  private static long posixCapabilitiesAsBits(int... paramVarArgs)
  {
    long l = 0L;
    int i = paramVarArgs.length;
    int j = 0;
    while (j < i)
    {
      int k = paramVarArgs[j];
      if ((k >= 0) && (k <= OsConstants.CAP_LAST_CAP))
      {
        l |= 1L << k;
        j++;
      }
      else
      {
        throw new IllegalArgumentException(String.valueOf(k));
      }
    }
    return l;
  }
  
  static void preload(TimingsTraceLog paramTimingsTraceLog)
  {
    Log.d("Zygote", "begin preload");
    paramTimingsTraceLog.traceBegin("BeginIcuCachePinning");
    beginIcuCachePinning();
    paramTimingsTraceLog.traceEnd();
    paramTimingsTraceLog.traceBegin("PreloadClasses");
    preloadClasses();
    paramTimingsTraceLog.traceEnd();
    paramTimingsTraceLog.traceBegin("PreloadResources");
    preloadResources();
    paramTimingsTraceLog.traceEnd();
    Trace.traceBegin(16384L, "PreloadAppProcessHALs");
    nativePreloadAppProcessHALs();
    Trace.traceEnd(16384L);
    Trace.traceBegin(16384L, "PreloadOpenGL");
    preloadOpenGL();
    Trace.traceEnd(16384L);
    preloadSharedLibraries();
    preloadTextResources();
    WebViewFactory.prepareWebViewInZygote();
    endIcuCachePinning();
    warmUpJcaProviders();
    Log.d("Zygote", "end preload");
    sPreloadComplete = true;
  }
  
  /* Error */
  private static void preloadClasses()
  {
    // Byte code:
    //   0: invokestatic 327	dalvik/system/VMRuntime:getRuntime	()Ldalvik/system/VMRuntime;
    //   3: astore_0
    //   4: new 730	java/io/FileInputStream
    //   7: dup
    //   8: ldc 16
    //   10: invokespecial 731	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   13: astore_1
    //   14: ldc 36
    //   16: ldc_w 733
    //   19: invokestatic 56	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   22: pop
    //   23: invokestatic 537	android/os/SystemClock:uptimeMillis	()J
    //   26: lstore_2
    //   27: invokestatic 736	android/system/Os:getuid	()I
    //   30: istore 4
    //   32: invokestatic 739	android/system/Os:getgid	()I
    //   35: istore 5
    //   37: iconst_0
    //   38: istore 6
    //   40: iload 6
    //   42: istore 7
    //   44: iload 4
    //   46: ifne +45 -> 91
    //   49: iload 6
    //   51: istore 7
    //   53: iload 5
    //   55: ifne +36 -> 91
    //   58: iconst_0
    //   59: sipush 9999
    //   62: invokestatic 742	android/system/Os:setregid	(II)V
    //   65: iconst_0
    //   66: sipush 9999
    //   69: invokestatic 745	android/system/Os:setreuid	(II)V
    //   72: iconst_1
    //   73: istore 7
    //   75: goto +16 -> 91
    //   78: astore_1
    //   79: new 312	java/lang/RuntimeException
    //   82: dup
    //   83: ldc_w 747
    //   86: aload_1
    //   87: invokespecial 320	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   90: athrow
    //   91: aload_0
    //   92: invokevirtual 751	dalvik/system/VMRuntime:getTargetHeapUtilization	()F
    //   95: fstore 8
    //   97: aload_0
    //   98: ldc_w 752
    //   101: invokevirtual 756	dalvik/system/VMRuntime:setTargetHeapUtilization	(F)F
    //   104: pop
    //   105: new 758	java/io/BufferedReader
    //   108: astore 9
    //   110: new 760	java/io/InputStreamReader
    //   113: astore 10
    //   115: aload 10
    //   117: aload_1
    //   118: invokespecial 763	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   121: aload 9
    //   123: aload 10
    //   125: sipush 256
    //   128: invokespecial 766	java/io/BufferedReader:<init>	(Ljava/io/Reader;I)V
    //   131: iconst_0
    //   132: istore 6
    //   134: aload 9
    //   136: invokevirtual 769	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   139: astore 10
    //   141: aload 10
    //   143: ifnull +266 -> 409
    //   146: aload 10
    //   148: invokevirtual 772	java/lang/String:trim	()Ljava/lang/String;
    //   151: astore 10
    //   153: aload 10
    //   155: ldc_w 774
    //   158: invokevirtual 518	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   161: ifne +245 -> 406
    //   164: aload 10
    //   166: ldc_w 623
    //   169: invokevirtual 439	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   172: ifeq +6 -> 178
    //   175: goto +231 -> 406
    //   178: ldc2_w 456
    //   181: aload 10
    //   183: invokestatic 696	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   186: aload 10
    //   188: iconst_1
    //   189: aconst_null
    //   190: invokestatic 780	java/lang/Class:forName	(Ljava/lang/String;ZLjava/lang/ClassLoader;)Ljava/lang/Class;
    //   193: pop
    //   194: iinc 6 1
    //   197: goto +200 -> 397
    //   200: astore 9
    //   202: new 140	java/lang/StringBuilder
    //   205: astore 11
    //   207: aload 11
    //   209: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   212: aload 11
    //   214: ldc_w 782
    //   217: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: pop
    //   221: aload 11
    //   223: aload 10
    //   225: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   228: pop
    //   229: aload 11
    //   231: ldc_w 784
    //   234: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: pop
    //   238: ldc 36
    //   240: aload 11
    //   242: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   245: aload 9
    //   247: invokestatic 588	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   250: pop
    //   251: aload 9
    //   253: instanceof 786
    //   256: ifne +32 -> 288
    //   259: aload 9
    //   261: instanceof 312
    //   264: ifeq +9 -> 273
    //   267: aload 9
    //   269: checkcast 312	java/lang/RuntimeException
    //   272: athrow
    //   273: new 312	java/lang/RuntimeException
    //   276: astore 10
    //   278: aload 10
    //   280: aload 9
    //   282: invokespecial 315	java/lang/RuntimeException:<init>	(Ljava/lang/Throwable;)V
    //   285: aload 10
    //   287: athrow
    //   288: aload 9
    //   290: checkcast 786	java/lang/Error
    //   293: athrow
    //   294: astore 11
    //   296: new 140	java/lang/StringBuilder
    //   299: astore 12
    //   301: aload 12
    //   303: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   306: aload 12
    //   308: ldc_w 788
    //   311: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   314: pop
    //   315: aload 12
    //   317: aload 10
    //   319: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   322: pop
    //   323: aload 12
    //   325: ldc_w 790
    //   328: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   331: pop
    //   332: aload 12
    //   334: aload 11
    //   336: invokevirtual 793	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   339: pop
    //   340: ldc 36
    //   342: aload 12
    //   344: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   347: invokestatic 665	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   350: pop
    //   351: goto -154 -> 197
    //   354: astore 11
    //   356: new 140	java/lang/StringBuilder
    //   359: astore 11
    //   361: aload 11
    //   363: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   366: aload 11
    //   368: ldc_w 795
    //   371: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   374: pop
    //   375: aload 11
    //   377: aload 10
    //   379: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   382: pop
    //   383: ldc 36
    //   385: aload 11
    //   387: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   390: invokestatic 665	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   393: pop
    //   394: goto -197 -> 197
    //   397: ldc2_w 456
    //   400: invokestatic 701	android/os/Trace:traceEnd	(J)V
    //   403: goto +3 -> 406
    //   406: goto -272 -> 134
    //   409: new 140	java/lang/StringBuilder
    //   412: astore 9
    //   414: aload 9
    //   416: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   419: aload 9
    //   421: ldc_w 797
    //   424: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   427: pop
    //   428: aload 9
    //   430: iload 6
    //   432: invokevirtual 800	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   435: pop
    //   436: aload 9
    //   438: ldc_w 802
    //   441: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   444: pop
    //   445: aload 9
    //   447: invokestatic 537	android/os/SystemClock:uptimeMillis	()J
    //   450: lload_2
    //   451: lsub
    //   452: invokevirtual 232	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   455: pop
    //   456: aload 9
    //   458: ldc_w 804
    //   461: invokevirtual 145	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   464: pop
    //   465: ldc 36
    //   467: aload 9
    //   469: invokevirtual 151	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   472: invokestatic 56	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   475: pop
    //   476: aload_1
    //   477: invokestatic 810	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   480: aload_0
    //   481: fload 8
    //   483: invokevirtual 756	dalvik/system/VMRuntime:setTargetHeapUtilization	(F)F
    //   486: pop
    //   487: ldc2_w 456
    //   490: ldc_w 812
    //   493: invokestatic 696	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   496: aload_0
    //   497: invokevirtual 815	dalvik/system/VMRuntime:preloadDexCaches	()V
    //   500: ldc2_w 456
    //   503: invokestatic 701	android/os/Trace:traceEnd	(J)V
    //   506: iload 7
    //   508: ifeq +108 -> 616
    //   511: iconst_0
    //   512: iconst_0
    //   513: invokestatic 745	android/system/Os:setreuid	(II)V
    //   516: iconst_0
    //   517: iconst_0
    //   518: invokestatic 742	android/system/Os:setregid	(II)V
    //   521: goto +79 -> 600
    //   524: astore_1
    //   525: new 312	java/lang/RuntimeException
    //   528: dup
    //   529: ldc_w 817
    //   532: aload_1
    //   533: invokespecial 320	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   536: athrow
    //   537: astore 9
    //   539: goto +78 -> 617
    //   542: astore 9
    //   544: ldc 36
    //   546: ldc_w 819
    //   549: aload 9
    //   551: invokestatic 588	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   554: pop
    //   555: aload_1
    //   556: invokestatic 810	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   559: aload_0
    //   560: fload 8
    //   562: invokevirtual 756	dalvik/system/VMRuntime:setTargetHeapUtilization	(F)F
    //   565: pop
    //   566: ldc2_w 456
    //   569: ldc_w 812
    //   572: invokestatic 696	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   575: aload_0
    //   576: invokevirtual 815	dalvik/system/VMRuntime:preloadDexCaches	()V
    //   579: ldc2_w 456
    //   582: invokestatic 701	android/os/Trace:traceEnd	(J)V
    //   585: iload 7
    //   587: ifeq +29 -> 616
    //   590: iconst_0
    //   591: iconst_0
    //   592: invokestatic 745	android/system/Os:setreuid	(II)V
    //   595: iconst_0
    //   596: iconst_0
    //   597: invokestatic 742	android/system/Os:setregid	(II)V
    //   600: goto +16 -> 616
    //   603: astore_1
    //   604: new 312	java/lang/RuntimeException
    //   607: dup
    //   608: ldc_w 817
    //   611: aload_1
    //   612: invokespecial 320	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   615: athrow
    //   616: return
    //   617: aload_1
    //   618: invokestatic 810	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
    //   621: aload_0
    //   622: fload 8
    //   624: invokevirtual 756	dalvik/system/VMRuntime:setTargetHeapUtilization	(F)F
    //   627: pop
    //   628: ldc2_w 456
    //   631: ldc_w 812
    //   634: invokestatic 696	android/os/Trace:traceBegin	(JLjava/lang/String;)V
    //   637: aload_0
    //   638: invokevirtual 815	dalvik/system/VMRuntime:preloadDexCaches	()V
    //   641: ldc2_w 456
    //   644: invokestatic 701	android/os/Trace:traceEnd	(J)V
    //   647: iload 7
    //   649: ifeq +29 -> 678
    //   652: iconst_0
    //   653: iconst_0
    //   654: invokestatic 745	android/system/Os:setreuid	(II)V
    //   657: iconst_0
    //   658: iconst_0
    //   659: invokestatic 742	android/system/Os:setregid	(II)V
    //   662: goto +16 -> 678
    //   665: astore_1
    //   666: new 312	java/lang/RuntimeException
    //   669: dup
    //   670: ldc_w 817
    //   673: aload_1
    //   674: invokespecial 320	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   677: athrow
    //   678: aload 9
    //   680: athrow
    //   681: astore_1
    //   682: ldc 36
    //   684: ldc_w 821
    //   687: invokestatic 823	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   690: pop
    //   691: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   3	635	0	localVMRuntime	VMRuntime
    //   13	1	1	localFileInputStream	java.io.FileInputStream
    //   78	399	1	localErrnoException1	android.system.ErrnoException
    //   524	32	1	localErrnoException2	android.system.ErrnoException
    //   603	15	1	localErrnoException3	android.system.ErrnoException
    //   665	9	1	localErrnoException4	android.system.ErrnoException
    //   681	1	1	localFileNotFoundException	FileNotFoundException
    //   26	425	2	l	long
    //   30	15	4	i	int
    //   35	19	5	j	int
    //   38	393	6	k	int
    //   42	606	7	m	int
    //   95	528	8	f	float
    //   108	27	9	localBufferedReader	java.io.BufferedReader
    //   200	89	9	localThrowable	Throwable
    //   412	56	9	localStringBuilder1	StringBuilder
    //   537	1	9	localObject1	Object
    //   542	137	9	localIOException	IOException
    //   113	265	10	localObject2	Object
    //   205	36	11	localStringBuilder2	StringBuilder
    //   294	41	11	localUnsatisfiedLinkError	UnsatisfiedLinkError
    //   354	1	11	localClassNotFoundException	ClassNotFoundException
    //   359	27	11	localStringBuilder3	StringBuilder
    //   299	44	12	localStringBuilder4	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   58	72	78	android/system/ErrnoException
    //   186	194	200	java/lang/Throwable
    //   186	194	294	java/lang/UnsatisfiedLinkError
    //   186	194	354	java/lang/ClassNotFoundException
    //   511	521	524	android/system/ErrnoException
    //   105	131	537	finally
    //   134	141	537	finally
    //   146	175	537	finally
    //   178	186	537	finally
    //   186	194	537	finally
    //   202	273	537	finally
    //   273	288	537	finally
    //   288	294	537	finally
    //   296	351	537	finally
    //   356	394	537	finally
    //   397	403	537	finally
    //   409	476	537	finally
    //   544	555	537	finally
    //   105	131	542	java/io/IOException
    //   134	141	542	java/io/IOException
    //   146	175	542	java/io/IOException
    //   178	186	542	java/io/IOException
    //   186	194	542	java/io/IOException
    //   202	273	542	java/io/IOException
    //   273	288	542	java/io/IOException
    //   288	294	542	java/io/IOException
    //   296	351	542	java/io/IOException
    //   356	394	542	java/io/IOException
    //   397	403	542	java/io/IOException
    //   409	476	542	java/io/IOException
    //   590	600	603	android/system/ErrnoException
    //   652	662	665	android/system/ErrnoException
    //   4	14	681	java/io/FileNotFoundException
  }
  
  private static int preloadColorStateLists(TypedArray paramTypedArray)
  {
    int i = paramTypedArray.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramTypedArray.getResourceId(j, 0);
      if ((k != 0) && (mResources.getColorStateList(k, null) == null))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unable to find preloaded color resource #0x");
        localStringBuilder.append(Integer.toHexString(k));
        localStringBuilder.append(" (");
        localStringBuilder.append(paramTypedArray.getString(j));
        localStringBuilder.append(")");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
    return i;
  }
  
  private static int preloadDrawables(TypedArray paramTypedArray)
  {
    int i = paramTypedArray.length();
    for (int j = 0; j < i; j++)
    {
      int k = paramTypedArray.getResourceId(j, 0);
      if ((k != 0) && (mResources.getDrawable(k, null) == null))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unable to find preloaded drawable resource #0x");
        localStringBuilder.append(Integer.toHexString(k));
        localStringBuilder.append(" (");
        localStringBuilder.append(paramTypedArray.getString(j));
        localStringBuilder.append(")");
        throw new IllegalArgumentException(localStringBuilder.toString());
      }
    }
    return i;
  }
  
  private static void preloadOpenGL()
  {
    String str = SystemProperties.get("ro.gfx.driver.0");
    if ((!SystemProperties.getBoolean("ro.zygote.disable_gl_preload", false)) && ((str == null) || (str.isEmpty()))) {
      EGL14.eglGetDisplay(0);
    }
  }
  
  private static void preloadResources()
  {
    VMRuntime.getRuntime();
    try
    {
      mResources = Resources.getSystem();
      mResources.startPreloading();
      Log.i("Zygote", "Preloading resources...");
      long l = SystemClock.uptimeMillis();
      Object localObject = mResources.obtainTypedArray(17236073);
      int i = preloadDrawables((TypedArray)localObject);
      ((TypedArray)localObject).recycle();
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("...preloaded ");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(" resources in ");
      ((StringBuilder)localObject).append(SystemClock.uptimeMillis() - l);
      ((StringBuilder)localObject).append("ms.");
      Log.i("Zygote", ((StringBuilder)localObject).toString());
      l = SystemClock.uptimeMillis();
      localObject = mResources.obtainTypedArray(17236072);
      i = preloadColorStateLists((TypedArray)localObject);
      ((TypedArray)localObject).recycle();
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("...preloaded ");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(" resources in ");
      ((StringBuilder)localObject).append(SystemClock.uptimeMillis() - l);
      ((StringBuilder)localObject).append("ms.");
      Log.i("Zygote", ((StringBuilder)localObject).toString());
      if (mResources.getBoolean(17956981))
      {
        l = SystemClock.uptimeMillis();
        localObject = mResources.obtainTypedArray(17236074);
        i = preloadDrawables((TypedArray)localObject);
        ((TypedArray)localObject).recycle();
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("...preloaded ");
        ((StringBuilder)localObject).append(i);
        ((StringBuilder)localObject).append(" resource in ");
        ((StringBuilder)localObject).append(SystemClock.uptimeMillis() - l);
        ((StringBuilder)localObject).append("ms.");
        Log.i("Zygote", ((StringBuilder)localObject).toString());
      }
      mResources.finishPreloading();
    }
    catch (RuntimeException localRuntimeException)
    {
      Log.w("Zygote", "Failure preloading resources", localRuntimeException);
    }
  }
  
  private static void preloadSharedLibraries()
  {
    Log.i("Zygote", "Preloading shared libraries...");
    System.loadLibrary("android");
    System.loadLibrary("compiler_rt");
    System.loadLibrary("jnigraphics");
  }
  
  private static void preloadTextResources()
  {
    Hyphenator.init();
    TextView.preloadFontCache();
  }
  
  private static void prepareSystemServerProfile(String paramString)
    throws RemoteException
  {
    if (paramString.isEmpty()) {
      return;
    }
    paramString = paramString.split(":");
    IInstalld.Stub.asInterface(ServiceManager.getService("installd")).prepareAppProfile("android", 0, UserHandle.getAppId(1000), "primary.prof", paramString[0], null);
    VMRuntime.registerAppInfo(new File(Environment.getDataProfilesDePackageDirectory(0, "android"), "primary.prof").getAbsolutePath(), paramString);
  }
  
  public static void setApiBlacklistExemptions(String[] paramArrayOfString)
  {
    VMRuntime.getRuntime().setHiddenApiExemptions(paramArrayOfString);
  }
  
  public static void setHiddenApiAccessLogSampleRate(int paramInt)
  {
    VMRuntime.getRuntime().setHiddenApiAccessLogSamplingRate(paramInt);
  }
  
  private static void waitForSecondaryZygote(String paramString)
  {
    if ("zygote".equals(paramString)) {
      paramString = "zygote_secondary";
    } else {
      paramString = "zygote";
    }
    ZygoteProcess.waitForConnectionToZygote(paramString);
  }
  
  private static void warmUpJcaProviders()
  {
    long l = SystemClock.uptimeMillis();
    Trace.traceBegin(16384L, "Starting installation of AndroidKeyStoreProvider");
    AndroidKeyStoreProvider.install();
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Installed AndroidKeyStoreProvider in ");
    ((StringBuilder)localObject).append(SystemClock.uptimeMillis() - l);
    ((StringBuilder)localObject).append("ms.");
    Log.i("Zygote", ((StringBuilder)localObject).toString());
    Trace.traceEnd(16384L);
    l = SystemClock.uptimeMillis();
    Trace.traceBegin(16384L, "Starting warm up of JCA providers");
    localObject = Security.getProviders();
    int i = localObject.length;
    for (int j = 0; j < i; j++) {
      localObject[j].warmUpServiceProvision();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Warmed up JCA providers in ");
    ((StringBuilder)localObject).append(SystemClock.uptimeMillis() - l);
    ((StringBuilder)localObject).append("ms.");
    Log.i("Zygote", ((StringBuilder)localObject).toString());
    Trace.traceEnd(16384L);
  }
  
  public static final Runnable zygoteInit(int paramInt, String[] paramArrayOfString, ClassLoader paramClassLoader)
  {
    Trace.traceBegin(64L, "ZygoteInit");
    RuntimeInit.redirectLogStreams();
    RuntimeInit.commonInit();
    nativeZygoteInit();
    return RuntimeInit.applicationInit(paramInt, paramArrayOfString, paramClassLoader);
  }
}
