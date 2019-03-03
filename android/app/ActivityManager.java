package android.app;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.GraphicBuffer;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Build;
import android.os.Build.FEATURES;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Singleton;
import android.util.Size;
import com.android.internal.os.RoSystemProperties;
import com.android.internal.os.TransferPipe;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.MemInfoReader;
import com.android.server.LocalServices;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlSerializer;

public class ActivityManager
{
  public static final String ACTION_REPORT_HEAP_LIMIT = "android.app.action.REPORT_HEAP_LIMIT";
  public static final int APP_START_MODE_DELAYED = 1;
  public static final int APP_START_MODE_DELAYED_RIGID = 2;
  public static final int APP_START_MODE_DISABLED = 3;
  public static final int APP_START_MODE_NORMAL = 0;
  public static final int ASSIST_CONTEXT_AUTOFILL = 2;
  public static final int ASSIST_CONTEXT_BASIC = 0;
  public static final int ASSIST_CONTEXT_FULL = 1;
  public static final int BROADCAST_FAILED_USER_STOPPED = -2;
  public static final int BROADCAST_STICKY_CANT_HAVE_PERMISSION = -1;
  public static final int BROADCAST_SUCCESS = 0;
  public static final int BUGREPORT_OPTION_FULL = 0;
  public static final int BUGREPORT_OPTION_INTERACTIVE = 1;
  public static final int BUGREPORT_OPTION_REMOTE = 2;
  public static final int BUGREPORT_OPTION_TELEPHONY = 4;
  public static final int BUGREPORT_OPTION_WEAR = 3;
  public static final int BUGREPORT_OPTION_WIFI = 5;
  public static final int COMPAT_MODE_ALWAYS = -1;
  public static final int COMPAT_MODE_DISABLED = 0;
  public static final int COMPAT_MODE_ENABLED = 1;
  public static final int COMPAT_MODE_NEVER = -2;
  public static final int COMPAT_MODE_TOGGLE = 2;
  public static final int COMPAT_MODE_UNKNOWN = -3;
  private static final boolean DEVELOPMENT_FORCE_LOW_RAM = SystemProperties.getBoolean("debug.force_low_ram", false);
  private static final int FIRST_START_FATAL_ERROR_CODE = -100;
  private static final int FIRST_START_NON_FATAL_ERROR_CODE = 100;
  private static final int FIRST_START_SUCCESS_CODE = 0;
  public static final int FLAG_AND_LOCKED = 2;
  public static final int FLAG_AND_UNLOCKED = 4;
  public static final int FLAG_AND_UNLOCKING_OR_UNLOCKED = 8;
  public static final int FLAG_OR_STOPPED = 1;
  private static final Singleton<IActivityManager> IActivityManagerSingleton = new Singleton()
  {
    protected IActivityManager create()
    {
      return IActivityManager.Stub.asInterface(ServiceManager.getService("activity"));
    }
  };
  public static final int INTENT_SENDER_ACTIVITY = 2;
  public static final int INTENT_SENDER_ACTIVITY_RESULT = 3;
  public static final int INTENT_SENDER_BROADCAST = 1;
  public static final int INTENT_SENDER_FOREGROUND_SERVICE = 5;
  public static final int INTENT_SENDER_SERVICE = 4;
  private static final int LAST_START_FATAL_ERROR_CODE = -1;
  private static final int LAST_START_NON_FATAL_ERROR_CODE = 199;
  private static final int LAST_START_SUCCESS_CODE = 99;
  public static final int LOCK_TASK_MODE_LOCKED = 1;
  public static final int LOCK_TASK_MODE_NONE = 0;
  public static final int LOCK_TASK_MODE_PINNED = 2;
  public static final int MAX_PROCESS_STATE = 19;
  public static final String META_HOME_ALTERNATE = "android.app.home.alternate";
  public static final int MIN_PROCESS_STATE = 0;
  public static final int MOVE_TASK_NO_USER_ACTION = 2;
  public static final int MOVE_TASK_WITH_HOME = 1;
  public static final int PROCESS_STATE_BACKUP = 8;
  public static final int PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 4;
  public static final int PROCESS_STATE_CACHED_ACTIVITY = 15;
  public static final int PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 16;
  public static final int PROCESS_STATE_CACHED_EMPTY = 18;
  public static final int PROCESS_STATE_CACHED_RECENT = 17;
  public static final int PROCESS_STATE_FOREGROUND_SERVICE = 3;
  public static final int PROCESS_STATE_HEAVY_WEIGHT = 12;
  public static final int PROCESS_STATE_HOME = 13;
  public static final int PROCESS_STATE_IMPORTANT_BACKGROUND = 6;
  public static final int PROCESS_STATE_IMPORTANT_FOREGROUND = 5;
  public static final int PROCESS_STATE_LAST_ACTIVITY = 14;
  public static final int PROCESS_STATE_NONEXISTENT = 19;
  public static final int PROCESS_STATE_PERSISTENT = 0;
  public static final int PROCESS_STATE_PERSISTENT_UI = 1;
  public static final int PROCESS_STATE_RECEIVER = 10;
  public static final int PROCESS_STATE_SERVICE = 9;
  public static final int PROCESS_STATE_TOP = 2;
  public static final int PROCESS_STATE_TOP_SLEEPING = 11;
  public static final int PROCESS_STATE_TRANSIENT_BACKGROUND = 7;
  public static final int PROCESS_STATE_UNKNOWN = -1;
  public static final int RECENT_IGNORE_UNAVAILABLE = 2;
  public static final int RECENT_WITH_EXCLUDED = 1;
  public static final int RESIZE_MODE_FORCED = 2;
  public static final int RESIZE_MODE_PRESERVE_WINDOW = 1;
  public static final int RESIZE_MODE_SYSTEM = 0;
  public static final int RESIZE_MODE_SYSTEM_SCREEN_ROTATION = 1;
  public static final int RESIZE_MODE_USER = 1;
  public static final int RESIZE_MODE_USER_FORCED = 3;
  public static final int SPLIT_SCREEN_CREATE_MODE_BOTTOM_OR_RIGHT = 1;
  public static final int SPLIT_SCREEN_CREATE_MODE_TOP_OR_LEFT = 0;
  public static final int START_ABORTED = 102;
  public static final int START_ASSISTANT_HIDDEN_SESSION = -90;
  public static final int START_ASSISTANT_NOT_ACTIVE_SESSION = -89;
  public static final int START_CANCELED = -96;
  public static final int START_CLASS_NOT_FOUND = -92;
  public static final int START_DELIVERED_TO_TOP = 3;
  public static final int START_FLAG_DEBUG = 2;
  public static final int START_FLAG_NATIVE_DEBUGGING = 8;
  public static final int START_FLAG_ONLY_IF_NEEDED = 1;
  public static final int START_FLAG_TRACK_ALLOCATION = 4;
  public static final int START_FORWARD_AND_REQUEST_CONFLICT = -93;
  public static final int START_INTENT_NOT_RESOLVED = -91;
  public static final int START_NOT_ACTIVITY = -95;
  public static final int START_NOT_CURRENT_USER_ACTIVITY = -98;
  public static final int START_NOT_VOICE_COMPATIBLE = -97;
  public static final int START_PERMISSION_DENIED = -94;
  public static final int START_RETURN_INTENT_TO_CALLER = 1;
  public static final int START_RETURN_LOCK_TASK_MODE_VIOLATION = 101;
  public static final int START_SUCCESS = 0;
  public static final int START_SWITCHES_CANCELED = 100;
  public static final int START_TASK_TO_FRONT = 2;
  public static final int START_VOICE_HIDDEN_SESSION = -100;
  public static final int START_VOICE_NOT_ACTIVE_SESSION = -99;
  private static String TAG = "ActivityManager";
  public static final int UID_OBSERVER_ACTIVE = 8;
  public static final int UID_OBSERVER_CACHED = 16;
  public static final int UID_OBSERVER_GONE = 2;
  public static final int UID_OBSERVER_IDLE = 4;
  public static final int UID_OBSERVER_PROCSTATE = 1;
  public static final int USER_OP_ERROR_IS_SYSTEM = -3;
  public static final int USER_OP_ERROR_RELATED_USERS_CANNOT_STOP = -4;
  public static final int USER_OP_IS_CURRENT = -2;
  public static final int USER_OP_SUCCESS = 0;
  public static final int USER_OP_UNKNOWN_USER = -1;
  private static int gMaxRecentTasks = -1;
  private static volatile boolean sSystemReady = false;
  Point mAppTaskThumbnailSize;
  private final Context mContext;
  final ArrayMap<OnUidImportanceListener, UidObserver> mImportanceListeners = new ArrayMap();
  
  ActivityManager(Context paramContext, Handler paramHandler)
  {
    mContext = paramContext;
  }
  
  public static void broadcastStickyIntent(Intent paramIntent, int paramInt)
  {
    broadcastStickyIntent(paramIntent, -1, paramInt);
  }
  
  public static void broadcastStickyIntent(Intent paramIntent, int paramInt1, int paramInt2)
  {
    try
    {
      getService().broadcastIntent(null, paramIntent, null, null, -1, null, null, null, paramInt1, null, false, true, paramInt2);
    }
    catch (RemoteException paramIntent) {}
  }
  
  public static int checkComponentPermission(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = UserHandle.getAppId(paramInt1);
    if ((i != 0) && (i != 1000))
    {
      if (UserHandle.isIsolated(paramInt1)) {
        return -1;
      }
      if ((paramInt2 >= 0) && (UserHandle.isSameApp(paramInt1, paramInt2))) {
        return 0;
      }
      if (!paramBoolean) {
        return -1;
      }
      if (paramString == null) {
        return 0;
      }
      try
      {
        paramInt1 = AppGlobals.getPackageManager().checkUidPermission(paramString, paramInt1);
        return paramInt1;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
    return 0;
  }
  
  public static int checkUidPermission(String paramString, int paramInt)
  {
    try
    {
      paramInt = AppGlobals.getPackageManager().checkUidPermission(paramString, paramInt);
      return paramInt;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public static void dumpPackageStateStatic(FileDescriptor paramFileDescriptor, String paramString)
  {
    FastPrintWriter localFastPrintWriter = new FastPrintWriter(new FileOutputStream(paramFileDescriptor));
    dumpService(localFastPrintWriter, paramFileDescriptor, "package", new String[] { paramString });
    localFastPrintWriter.println();
    dumpService(localFastPrintWriter, paramFileDescriptor, "activity", new String[] { "-a", "package", paramString });
    localFastPrintWriter.println();
    dumpService(localFastPrintWriter, paramFileDescriptor, "meminfo", new String[] { "--local", "--package", paramString });
    localFastPrintWriter.println();
    dumpService(localFastPrintWriter, paramFileDescriptor, "procstats", new String[] { paramString });
    localFastPrintWriter.println();
    dumpService(localFastPrintWriter, paramFileDescriptor, "usagestats", new String[] { paramString });
    localFastPrintWriter.println();
    dumpService(localFastPrintWriter, paramFileDescriptor, "batterystats", new String[] { paramString });
    localFastPrintWriter.flush();
  }
  
  private static void dumpService(PrintWriter paramPrintWriter, FileDescriptor paramFileDescriptor, String paramString, String[] paramArrayOfString)
  {
    paramPrintWriter.print("DUMP OF SERVICE ");
    paramPrintWriter.print(paramString);
    paramPrintWriter.println(":");
    IBinder localIBinder = ServiceManager.checkService(paramString);
    if (localIBinder == null)
    {
      paramPrintWriter.println("  (Service not found)");
      paramPrintWriter.flush();
      return;
    }
    paramPrintWriter.flush();
    if ((localIBinder instanceof Binder))
    {
      try
      {
        localIBinder.dump(paramFileDescriptor, paramArrayOfString);
      }
      catch (Throwable paramFileDescriptor)
      {
        for (;;)
        {
          paramPrintWriter.println("Failure dumping service:");
          paramFileDescriptor.printStackTrace(paramPrintWriter);
          paramPrintWriter.flush();
        }
      }
    }
    else
    {
      Object localObject = null;
      paramString = localObject;
      try
      {
        paramPrintWriter.flush();
        paramString = localObject;
        TransferPipe localTransferPipe = new com/android/internal/os/TransferPipe;
        paramString = localObject;
        localTransferPipe.<init>();
        localObject = localTransferPipe;
        paramString = localObject;
        localObject.setBufferPrefix("  ");
        paramString = localObject;
        localIBinder.dumpAsync(localObject.getWriteFd().getFileDescriptor(), paramArrayOfString);
        paramString = localObject;
        localObject.go(paramFileDescriptor, 10000L);
      }
      catch (Throwable paramFileDescriptor)
      {
        if (paramString != null) {
          paramString.kill();
        }
        paramPrintWriter.println("Failure dumping service:");
        paramFileDescriptor.printStackTrace(paramPrintWriter);
      }
    }
  }
  
  private void ensureAppTaskThumbnailSizeLocked()
  {
    if (mAppTaskThumbnailSize == null) {
      try
      {
        mAppTaskThumbnailSize = getService().getAppTaskThumbnailSize();
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  @SystemApi
  public static int getCurrentUser()
  {
    try
    {
      UserInfo localUserInfo = getService().getCurrentUser();
      int i;
      if (localUserInfo != null) {
        i = id;
      } else {
        i = 0;
      }
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static int getDefaultAppRecentsLimitStatic()
  {
    return getMaxRecentTasksStatic() / 6;
  }
  
  static int getLauncherLargeIconSizeInner(Context paramContext)
  {
    paramContext = paramContext.getResources();
    int i = paramContext.getDimensionPixelSize(17104896);
    if (getConfigurationsmallestScreenWidthDp < 600) {
      return i;
    }
    int j = getDisplayMetricsdensityDpi;
    if (j != 120)
    {
      if (j != 160)
      {
        if (j != 213)
        {
          if (j != 240)
          {
            if (j != 320)
            {
              if (j != 480) {
                return (int)(i * 1.5F + 0.5F);
              }
              return i * 320 * 2 / 480;
            }
            return i * 480 / 320;
          }
          return i * 320 / 240;
        }
        return i * 320 / 240;
      }
      return i * 240 / 160;
    }
    return i * 160 / 120;
  }
  
  public static int getMaxAppRecentsLimitStatic()
  {
    return getMaxRecentTasksStatic() / 2;
  }
  
  @Deprecated
  public static int getMaxNumPictureInPictureActions()
  {
    return 3;
  }
  
  public static int getMaxRecentTasksStatic()
  {
    if (gMaxRecentTasks < 0)
    {
      int i;
      if ((!isLowRamDeviceStatic()) && (!isAsusLowRamDeviceStatic())) {
        i = 48;
      } else {
        i = 36;
      }
      gMaxRecentTasks = i;
      return i;
    }
    return gMaxRecentTasks;
  }
  
  public static void getMyMemoryState(RunningAppProcessInfo paramRunningAppProcessInfo)
  {
    try
    {
      getService().getMyMemoryState(paramRunningAppProcessInfo);
      return;
    }
    catch (RemoteException paramRunningAppProcessInfo)
    {
      throw paramRunningAppProcessInfo.rethrowFromSystemServer();
    }
  }
  
  public static IActivityManager getService()
  {
    return (IActivityManager)IActivityManagerSingleton.get();
  }
  
  public static int handleIncomingUser(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, String paramString1, String paramString2)
  {
    if (UserHandle.getUserId(paramInt2) == paramInt3) {
      return paramInt3;
    }
    try
    {
      paramInt1 = getService().handleIncomingUser(paramInt1, paramInt2, paramInt3, paramBoolean1, paramBoolean2, paramString1, paramString2);
      return paramInt1;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public static boolean isAsusLowRamDeviceStatic()
  {
    return "true".equals(SystemProperties.get("persist.asus.config.low_ram", "false"));
  }
  
  public static boolean isHighEndGfx()
  {
    boolean bool;
    if ((!isLowRamDeviceStatic()) && (!RoSystemProperties.CONFIG_AVOID_GFX_ACCEL) && (!Resources.getSystem().getBoolean(17956897))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isLowRamDeviceStatic()
  {
    boolean bool;
    if ((!RoSystemProperties.CONFIG_LOW_RAM) && ((!Build.IS_DEBUGGABLE) || (!DEVELOPMENT_FORCE_LOW_RAM))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static final boolean isProcStateBackground(int paramInt)
  {
    boolean bool;
    if (paramInt >= 7) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isRunningInTestHarness()
  {
    return SystemProperties.getBoolean("ro.test_harness", false);
  }
  
  public static boolean isSmallBatteryDevice()
  {
    return RoSystemProperties.CONFIG_SMALL_BATTERY;
  }
  
  public static final boolean isStartResultFatalError(int paramInt)
  {
    boolean bool;
    if ((-100 <= paramInt) && (paramInt <= -1)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static final boolean isStartResultSuccessful(int paramInt)
  {
    boolean bool;
    if ((paramInt >= 0) && (paramInt <= 99)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isSystemReady()
  {
    if (!sSystemReady) {
      if (ActivityThread.isSystem()) {
        sSystemReady = ((ActivityManagerInternal)LocalServices.getService(ActivityManagerInternal.class)).isSystemReady();
      } else {
        sSystemReady = true;
      }
    }
    return sSystemReady;
  }
  
  public static boolean isUserAMonkey()
  {
    try
    {
      boolean bool = getService().isUserAMonkey();
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static void logoutCurrentUser()
  {
    int i = getCurrentUser();
    if (i != 0) {
      try
      {
        getService().switchUser(0);
        getService().stopUser(i, false, null);
      }
      catch (RemoteException localRemoteException)
      {
        localRemoteException.rethrowFromSystemServer();
      }
    }
  }
  
  public static void noteAlarmFinish(PendingIntent paramPendingIntent, WorkSource paramWorkSource, int paramInt, String paramString)
  {
    try
    {
      IActivityManager localIActivityManager = getService();
      if (paramPendingIntent != null) {
        paramPendingIntent = paramPendingIntent.getTarget();
      } else {
        paramPendingIntent = null;
      }
      localIActivityManager.noteAlarmFinish(paramPendingIntent, paramWorkSource, paramInt, paramString);
    }
    catch (RemoteException paramPendingIntent) {}
  }
  
  public static void noteAlarmStart(PendingIntent paramPendingIntent, WorkSource paramWorkSource, int paramInt, String paramString)
  {
    try
    {
      IActivityManager localIActivityManager = getService();
      if (paramPendingIntent != null) {
        paramPendingIntent = paramPendingIntent.getTarget();
      } else {
        paramPendingIntent = null;
      }
      localIActivityManager.noteAlarmStart(paramPendingIntent, paramWorkSource, paramInt, paramString);
    }
    catch (RemoteException paramPendingIntent) {}
  }
  
  public static void noteWakeupAlarm(PendingIntent paramPendingIntent, WorkSource paramWorkSource, int paramInt, String paramString1, String paramString2)
  {
    try
    {
      IActivityManager localIActivityManager = getService();
      if (paramPendingIntent != null) {
        paramPendingIntent = paramPendingIntent.getTarget();
      } else {
        paramPendingIntent = null;
      }
      localIActivityManager.noteWakeupAlarm(paramPendingIntent, paramWorkSource, paramInt, paramString1, paramString2);
    }
    catch (RemoteException paramPendingIntent) {}
  }
  
  public static final int processStateAmToProto(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 998;
    case 19: 
      return 1019;
    case 18: 
      return 1018;
    case 17: 
      return 1017;
    case 16: 
      return 1016;
    case 15: 
      return 1015;
    case 14: 
      return 1014;
    case 13: 
      return 1013;
    case 12: 
      return 1012;
    case 11: 
      return 1011;
    case 10: 
      return 1010;
    case 9: 
      return 1009;
    case 8: 
      return 1008;
    case 7: 
      return 1007;
    case 6: 
      return 1006;
    case 5: 
      return 1005;
    case 4: 
      return 1004;
    case 3: 
      return 1003;
    case 2: 
      return 1002;
    case 1: 
      return 1001;
    case 0: 
      return 1000;
    }
    return 999;
  }
  
  public static void setPersistentVrThread(int paramInt)
  {
    try
    {
      getService().setPersistentVrThread(paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public static void setVrThread(int paramInt)
  {
    try
    {
      getService().setVrThread(paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public static int staticGetLargeMemoryClass()
  {
    String str = SystemProperties.get("dalvik.vm.heapsize", "16m");
    return Integer.parseInt(str.substring(0, str.length() - 1));
  }
  
  public static int staticGetMemoryClass()
  {
    String str = SystemProperties.get("dalvik.vm.heapgrowthlimit", "");
    if ((str != null) && (!"".equals(str))) {
      return Integer.parseInt(str.substring(0, str.length() - 1));
    }
    return staticGetLargeMemoryClass();
  }
  
  public static boolean supportsMultiWindow(Context paramContext)
  {
    boolean bool = paramContext.getPackageManager().hasSystemFeature("android.hardware.type.watch");
    if (((!isLowRamDeviceStatic()) || (bool)) && (Resources.getSystem().getBoolean(17957050))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean supportsSplitScreenMultiWindow(Context paramContext)
  {
    boolean bool;
    if ((supportsMultiWindow(paramContext)) && (Resources.getSystem().getBoolean(17957051))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  /* Error */
  public int addAppTask(Activity paramActivity, Intent paramIntent, TaskDescription paramTaskDescription, Bitmap paramBitmap)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial 665	android/app/ActivityManager:ensureAppTaskThumbnailSizeLocked	()V
    //   6: aload_0
    //   7: getfield 439	android/app/ActivityManager:mAppTaskThumbnailSize	Landroid/graphics/Point;
    //   10: astore 5
    //   12: aload_0
    //   13: monitorexit
    //   14: aload 4
    //   16: invokevirtual 670	android/graphics/Bitmap:getWidth	()I
    //   19: istore 6
    //   21: aload 4
    //   23: invokevirtual 673	android/graphics/Bitmap:getHeight	()I
    //   26: istore 7
    //   28: iload 6
    //   30: aload 5
    //   32: getfield 678	android/graphics/Point:x	I
    //   35: if_icmpne +17 -> 52
    //   38: aload 4
    //   40: astore 8
    //   42: iload 7
    //   44: aload 5
    //   46: getfield 681	android/graphics/Point:y	I
    //   49: if_icmpeq +164 -> 213
    //   52: aload 5
    //   54: getfield 678	android/graphics/Point:x	I
    //   57: aload 5
    //   59: getfield 681	android/graphics/Point:y	I
    //   62: aload 4
    //   64: invokevirtual 685	android/graphics/Bitmap:getConfig	()Landroid/graphics/Bitmap$Config;
    //   67: invokestatic 689	android/graphics/Bitmap:createBitmap	(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
    //   70: astore 8
    //   72: fconst_0
    //   73: fstore 9
    //   75: aload 5
    //   77: getfield 678	android/graphics/Point:x	I
    //   80: iload 6
    //   82: imul
    //   83: aload 5
    //   85: getfield 681	android/graphics/Point:y	I
    //   88: iload 7
    //   90: imul
    //   91: if_icmple +37 -> 128
    //   94: aload 5
    //   96: getfield 678	android/graphics/Point:x	I
    //   99: i2f
    //   100: iload 7
    //   102: i2f
    //   103: fdiv
    //   104: fstore 10
    //   106: aload 5
    //   108: getfield 681	android/graphics/Point:y	I
    //   111: i2f
    //   112: iload 6
    //   114: i2f
    //   115: fload 10
    //   117: fmul
    //   118: fsub
    //   119: ldc_w 493
    //   122: fmul
    //   123: fstore 9
    //   125: goto +28 -> 153
    //   128: aload 5
    //   130: getfield 681	android/graphics/Point:y	I
    //   133: i2f
    //   134: iload 6
    //   136: i2f
    //   137: fdiv
    //   138: fstore 10
    //   140: aload 5
    //   142: getfield 678	android/graphics/Point:x	I
    //   145: i2f
    //   146: fstore 11
    //   148: iload 7
    //   150: i2f
    //   151: fstore 11
    //   153: new 691	android/graphics/Matrix
    //   156: dup
    //   157: invokespecial 692	android/graphics/Matrix:<init>	()V
    //   160: astore 5
    //   162: aload 5
    //   164: fload 10
    //   166: fload 10
    //   168: invokevirtual 696	android/graphics/Matrix:setScale	(FF)V
    //   171: aload 5
    //   173: ldc_w 493
    //   176: fload 9
    //   178: fadd
    //   179: f2i
    //   180: i2f
    //   181: fconst_0
    //   182: invokevirtual 700	android/graphics/Matrix:postTranslate	(FF)Z
    //   185: pop
    //   186: new 702	android/graphics/Canvas
    //   189: dup
    //   190: aload 8
    //   192: invokespecial 705	android/graphics/Canvas:<init>	(Landroid/graphics/Bitmap;)V
    //   195: astore 12
    //   197: aload 12
    //   199: aload 4
    //   201: aload 5
    //   203: aconst_null
    //   204: invokevirtual 709	android/graphics/Canvas:drawBitmap	(Landroid/graphics/Bitmap;Landroid/graphics/Matrix;Landroid/graphics/Paint;)V
    //   207: aload 12
    //   209: aconst_null
    //   210: invokevirtual 712	android/graphics/Canvas:setBitmap	(Landroid/graphics/Bitmap;)V
    //   213: aload_3
    //   214: astore 4
    //   216: aload_3
    //   217: ifnonnull +12 -> 229
    //   220: new 61	android/app/ActivityManager$TaskDescription
    //   223: dup
    //   224: invokespecial 713	android/app/ActivityManager$TaskDescription:<init>	()V
    //   227: astore 4
    //   229: invokestatic 290	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   232: aload_1
    //   233: invokevirtual 719	android/app/Activity:getActivityToken	()Landroid/os/IBinder;
    //   236: aload_2
    //   237: aload 4
    //   239: aload 8
    //   241: invokeinterface 722 5 0
    //   246: istore 6
    //   248: iload 6
    //   250: ireturn
    //   251: astore_1
    //   252: aload_1
    //   253: invokevirtual 328	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   256: athrow
    //   257: astore_1
    //   258: aload_0
    //   259: monitorexit
    //   260: aload_1
    //   261: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	262	0	this	ActivityManager
    //   0	262	1	paramActivity	Activity
    //   0	262	2	paramIntent	Intent
    //   0	262	3	paramTaskDescription	TaskDescription
    //   0	262	4	paramBitmap	Bitmap
    //   10	192	5	localObject	Object
    //   19	230	6	i	int
    //   26	123	7	j	int
    //   40	200	8	localBitmap	Bitmap
    //   73	104	9	f1	float
    //   104	63	10	f2	float
    //   146	6	11	f3	float
    //   195	13	12	localCanvas	android.graphics.Canvas
    // Exception table:
    //   from	to	target	type
    //   229	248	251	android/os/RemoteException
    //   2	14	257	finally
    //   258	260	257	finally
  }
  
  @SystemApi
  public void addOnUidImportanceListener(OnUidImportanceListener paramOnUidImportanceListener, int paramInt)
  {
    try
    {
      if (!mImportanceListeners.containsKey(paramOnUidImportanceListener))
      {
        localObject = new android/app/ActivityManager$UidObserver;
        ((UidObserver)localObject).<init>(paramOnUidImportanceListener, mContext);
        try
        {
          getService().registerUidObserver((IUidObserver)localObject, 3, RunningAppProcessInfo.importanceToProcState(paramInt), mContext.getOpPackageName());
          mImportanceListeners.put(paramOnUidImportanceListener, localObject);
          return;
        }
        catch (RemoteException paramOnUidImportanceListener)
        {
          throw paramOnUidImportanceListener.rethrowFromSystemServer();
        }
      }
      Object localObject = new java/lang/IllegalArgumentException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Listener already registered: ");
      localStringBuilder.append(paramOnUidImportanceListener);
      ((IllegalArgumentException)localObject).<init>(localStringBuilder.toString());
      throw ((Throwable)localObject);
    }
    finally {}
  }
  
  public void alwaysShowUnsupportedCompileSdkWarning(ComponentName paramComponentName)
  {
    try
    {
      getService().alwaysShowUnsupportedCompileSdkWarning(paramComponentName);
      return;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public boolean clearApplicationUserData()
  {
    return clearApplicationUserData(mContext.getPackageName(), null);
  }
  
  public boolean clearApplicationUserData(String paramString, IPackageDataObserver paramIPackageDataObserver)
  {
    try
    {
      boolean bool = getService().clearApplicationUserData(paramString, false, paramIPackageDataObserver, mContext.getUserId());
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void clearGrantedUriPermissions(String paramString)
  {
    try
    {
      getService().clearGrantedUriPermissions(paramString, mContext.getUserId());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void clearWatchHeapLimit()
  {
    try
    {
      getService().setDumpHeapDebugLimit(null, 0, 0L, null);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void dumpPackageState(FileDescriptor paramFileDescriptor, String paramString)
  {
    dumpPackageStateStatic(paramFileDescriptor, paramString);
  }
  
  @SystemApi
  public void forceStopPackage(String paramString)
  {
    forceStopPackageAsUser(paramString, mContext.getUserId());
  }
  
  public void forceStopPackageAsUser(String paramString, int paramInt)
  {
    try
    {
      getService().forceStopPackage(paramString, paramInt);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Size getAppTaskThumbnailSize()
  {
    try
    {
      ensureAppTaskThumbnailSizeLocked();
      Size localSize = new android/util/Size;
      localSize.<init>(mAppTaskThumbnailSize.x, mAppTaskThumbnailSize.y);
      return localSize;
    }
    finally {}
  }
  
  public List<AppTask> getAppTasks()
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      List localList = getService().getAppTasks(mContext.getPackageName());
      int i = localList.size();
      for (int j = 0; j < i; j++) {
        localArrayList.add(new AppTask(IAppTask.Stub.asInterface((IBinder)localList.get(j))));
      }
      return localArrayList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ConfigurationInfo getDeviceConfigurationInfo()
  {
    try
    {
      ConfigurationInfo localConfigurationInfo = getService().getDeviceConfigurationInfo();
      return localConfigurationInfo;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getFocusedAppNotchUiMode()
  {
    try
    {
      int i = getService().getFocusedAppNotchUiMode();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getFocusedAppScaleMode()
  {
    try
    {
      int i = getService().getFocusedAppScaleMode();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getFrontActivityScreenCompatMode()
  {
    try
    {
      int i = getService().getFrontActivityScreenCompatMode();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public ParceledListSlice<GrantedUriPermission> getGrantedUriPermissions(String paramString)
  {
    try
    {
      paramString = getService().getGrantedUriPermissions(paramString, mContext.getUserId());
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int getLargeMemoryClass()
  {
    return staticGetLargeMemoryClass();
  }
  
  public int getLauncherLargeIconDensity()
  {
    Resources localResources = mContext.getResources();
    int i = getDisplayMetricsdensityDpi;
    if (getConfigurationsmallestScreenWidthDp < 600) {
      return i;
    }
    if (i != 120)
    {
      if (i != 160)
      {
        if (i != 213)
        {
          if (i != 240)
          {
            if (i != 320)
            {
              if (i != 480) {
                return (int)(i * 1.5F + 0.5F);
              }
              return 640;
            }
            return 480;
          }
          return 320;
        }
        return 320;
      }
      return 240;
    }
    return 160;
  }
  
  public int getLauncherLargeIconSize()
  {
    return getLauncherLargeIconSizeInner(mContext);
  }
  
  public int getLockTaskModeState()
  {
    try
    {
      int i = getService().getLockTaskModeState();
      return i;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getMemoryClass()
  {
    return staticGetMemoryClass();
  }
  
  public void getMemoryInfo(MemoryInfo paramMemoryInfo)
  {
    try
    {
      getService().getMemoryInfo(paramMemoryInfo);
      return;
    }
    catch (RemoteException paramMemoryInfo)
    {
      throw paramMemoryInfo.rethrowFromSystemServer();
    }
  }
  
  public boolean getPackageAskScreenCompat(String paramString)
  {
    try
    {
      boolean bool = getService().getPackageAskScreenCompat(paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public int getPackageImportance(String paramString)
  {
    try
    {
      int i = RunningAppProcessInfo.procStateToImportanceForClient(getService().getPackageProcessState(paramString, mContext.getOpPackageName()), mContext);
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int getPackageScreenCompatMode(String paramString)
  {
    try
    {
      int i = getService().getPackageScreenCompatMode(paramString);
      return i;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public Debug.MemoryInfo[] getProcessMemoryInfo(int[] paramArrayOfInt)
  {
    try
    {
      paramArrayOfInt = getService().getProcessMemoryInfo(paramArrayOfInt);
      return paramArrayOfInt;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public List<ProcessErrorStateInfo> getProcessesInErrorState()
  {
    try
    {
      List localList = getService().getProcessesInErrorState();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public List<RecentTaskInfo> getRecentTasks(int paramInt1, int paramInt2)
    throws SecurityException
  {
    if (paramInt1 >= 0) {}
    try
    {
      return getService().getRecentTasks(paramInt1, paramInt2, mContext.getUserId()).getList();
    }
    catch (RemoteException localRemoteException)
    {
      IllegalArgumentException localIllegalArgumentException;
      throw localRemoteException.rethrowFromSystemServer();
    }
    localIllegalArgumentException = new java/lang/IllegalArgumentException;
    localIllegalArgumentException.<init>("The requested number of tasks should be >= 0");
    throw localIllegalArgumentException;
  }
  
  public List<RunningAppProcessInfo> getRunningAppProcesses()
  {
    try
    {
      List localList = getService().getRunningAppProcesses();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public List<ApplicationInfo> getRunningExternalApplications()
  {
    try
    {
      List localList = getService().getRunningExternalApplications();
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public PendingIntent getRunningServiceControlPanel(ComponentName paramComponentName)
    throws SecurityException
  {
    try
    {
      paramComponentName = getService().getRunningServiceControlPanel(paramComponentName);
      return paramComponentName;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public List<RunningServiceInfo> getRunningServices(int paramInt)
    throws SecurityException
  {
    try
    {
      List localList = getService().getServices(paramInt, 0);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public List<RunningTaskInfo> getRunningTasks(int paramInt)
    throws SecurityException
  {
    try
    {
      List localList = getService().getTasks(paramInt);
      return localList;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public long getTotalRam()
  {
    MemInfoReader localMemInfoReader = new MemInfoReader();
    localMemInfoReader.readMemInfo();
    return localMemInfoReader.getTotalSize();
  }
  
  @SystemApi
  public int getUidImportance(int paramInt)
  {
    try
    {
      paramInt = RunningAppProcessInfo.procStateToImportanceForClient(getService().getUidProcessState(paramInt, mContext.getOpPackageName()), mContext);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public int getVisibleAppScaleMode(int paramInt)
  {
    try
    {
      paramInt = getService().getVisibleAppScaleMode(paramInt);
      return paramInt;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isBackgroundRestricted()
  {
    try
    {
      boolean bool = getService().isBackgroundRestricted(mContext.getOpPackageName());
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public boolean isInLockTaskMode()
  {
    boolean bool;
    if (getLockTaskModeState() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLowRamDevice()
  {
    return isLowRamDeviceStatic();
  }
  
  public boolean isUserRunning(int paramInt)
  {
    try
    {
      boolean bool = getService().isUserRunning(paramInt, 0);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean isVrModePackageEnabled(ComponentName paramComponentName)
  {
    try
    {
      boolean bool = getService().isVrModePackageEnabled(paramComponentName);
      return bool;
    }
    catch (RemoteException paramComponentName)
    {
      throw paramComponentName.rethrowFromSystemServer();
    }
  }
  
  public void killBackgroundProcesses(String paramString)
  {
    try
    {
      getService().killBackgroundProcesses(paramString, mContext.getUserId());
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void killUid(int paramInt, String paramString)
  {
    try
    {
      getService().killUid(UserHandle.getAppId(paramInt), UserHandle.getUserId(paramInt), paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void moveTaskToFront(int paramInt1, int paramInt2)
  {
    moveTaskToFront(paramInt1, paramInt2, null);
  }
  
  public void moveTaskToFront(int paramInt1, int paramInt2, Bundle paramBundle)
  {
    try
    {
      getService().moveTaskToFront(paramInt1, paramInt2, paramBundle);
      return;
    }
    catch (RemoteException paramBundle)
    {
      throw paramBundle.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void removeOnUidImportanceListener(OnUidImportanceListener paramOnUidImportanceListener)
  {
    try
    {
      Object localObject = (UidObserver)mImportanceListeners.remove(paramOnUidImportanceListener);
      if (localObject != null) {
        try
        {
          getService().unregisterUidObserver((IUidObserver)localObject);
          return;
        }
        catch (RemoteException paramOnUidImportanceListener)
        {
          throw paramOnUidImportanceListener.rethrowFromSystemServer();
        }
      }
      IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
      localObject = new java/lang/StringBuilder;
      ((StringBuilder)localObject).<init>();
      ((StringBuilder)localObject).append("Listener not registered: ");
      ((StringBuilder)localObject).append(paramOnUidImportanceListener);
      localIllegalArgumentException.<init>(((StringBuilder)localObject).toString());
      throw localIllegalArgumentException;
    }
    finally {}
  }
  
  public void removeStacksInWindowingModes(int[] paramArrayOfInt)
    throws SecurityException
  {
    try
    {
      getService().removeStacksInWindowingModes(paramArrayOfInt);
      return;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public void removeStacksWithActivityTypes(int[] paramArrayOfInt)
    throws SecurityException
  {
    try
    {
      getService().removeStacksWithActivityTypes(paramArrayOfInt);
      return;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public void requestFocusedAppFillNotchRegion(boolean paramBoolean)
  {
    try
    {
      getService().requestFocusedAppFillNotchRegion(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void requestFocusedAppFitScreen(boolean paramBoolean)
  {
    try
    {
      getService().requestFocusedAppFitScreen(paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void requestVisibleAppFitScreen(boolean paramBoolean, int paramInt)
  {
    try
    {
      getService().requestVisibleAppFitScreen(paramBoolean, paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void resizeStack(int paramInt, Rect paramRect)
    throws SecurityException
  {
    try
    {
      getService().resizeStack(paramInt, paramRect, false, false, false, -1);
      return;
    }
    catch (RemoteException paramRect)
    {
      throw paramRect.rethrowFromSystemServer();
    }
  }
  
  @Deprecated
  public void restartPackage(String paramString)
  {
    killBackgroundProcesses(paramString);
  }
  
  public void setFrontActivityScreenCompatMode(int paramInt)
  {
    try
    {
      getService().setFrontActivityScreenCompatMode(paramInt);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setPackageAskScreenCompat(String paramString, boolean paramBoolean)
  {
    try
    {
      getService().setPackageAskScreenCompat(paramString, paramBoolean);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setPackageScreenCompatMode(String paramString, int paramInt)
  {
    try
    {
      getService().setPackageScreenCompatMode(paramString, paramInt);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public boolean setProcessMemoryTrimLevel(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      boolean bool = getService().setProcessMemoryTrimLevel(paramString, paramInt1, paramInt2);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setTaskWindowingMode(int paramInt1, int paramInt2, boolean paramBoolean)
    throws SecurityException
  {
    try
    {
      getService().setTaskWindowingMode(paramInt1, paramInt2, paramBoolean);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setTaskWindowingModeSplitScreenPrimary(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, Rect paramRect, boolean paramBoolean3)
    throws SecurityException
  {
    try
    {
      getService().setTaskWindowingModeSplitScreenPrimary(paramInt1, paramInt2, paramBoolean1, paramBoolean2, paramRect, paramBoolean3);
      return;
    }
    catch (RemoteException paramRect)
    {
      throw paramRect.rethrowFromSystemServer();
    }
  }
  
  public void setWatchHeapLimit(long paramLong)
  {
    try
    {
      getService().setDumpHeapDebugLimit(null, 0, paramLong, mContext.getPackageName());
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public boolean switchUser(int paramInt)
  {
    try
    {
      boolean bool = getService().switchUser(paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public static class AppTask
  {
    private IAppTask mAppTaskImpl;
    
    public AppTask(IAppTask paramIAppTask)
    {
      mAppTaskImpl = paramIAppTask;
    }
    
    public void finishAndRemoveTask()
    {
      try
      {
        mAppTaskImpl.finishAndRemoveTask();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public ActivityManager.RecentTaskInfo getTaskInfo()
    {
      try
      {
        ActivityManager.RecentTaskInfo localRecentTaskInfo = mAppTaskImpl.getTaskInfo();
        return localRecentTaskInfo;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void moveToFront()
    {
      try
      {
        mAppTaskImpl.moveToFront();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void setExcludeFromRecents(boolean paramBoolean)
    {
      try
      {
        mAppTaskImpl.setExcludeFromRecents(paramBoolean);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    
    public void startActivity(Context paramContext, Intent paramIntent, Bundle paramBundle)
    {
      ActivityThread localActivityThread = ActivityThread.currentActivityThread();
      localActivityThread.getInstrumentation().execStartActivityFromAppTask(paramContext, localActivityThread.getApplicationThread(), mAppTaskImpl, paramIntent, paramBundle);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BugreportMode {}
  
  public static class MemoryInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<MemoryInfo> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.MemoryInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.MemoryInfo(paramAnonymousParcel, null);
      }
      
      public ActivityManager.MemoryInfo[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.MemoryInfo[paramAnonymousInt];
      }
    };
    public long availMem;
    public long foregroundAppThreshold;
    public long hiddenAppThreshold;
    public boolean lowMemory;
    public long secondaryServerThreshold;
    public long threshold;
    public long totalMem;
    public long visibleAppThreshold;
    
    public MemoryInfo() {}
    
    private MemoryInfo(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      availMem = paramParcel.readLong();
      totalMem = paramParcel.readLong();
      threshold = paramParcel.readLong();
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      lowMemory = bool;
      hiddenAppThreshold = paramParcel.readLong();
      secondaryServerThreshold = paramParcel.readLong();
      visibleAppThreshold = paramParcel.readLong();
      foregroundAppThreshold = paramParcel.readLong();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(availMem);
      paramParcel.writeLong(totalMem);
      paramParcel.writeLong(threshold);
      paramParcel.writeInt(lowMemory);
      paramParcel.writeLong(hiddenAppThreshold);
      paramParcel.writeLong(secondaryServerThreshold);
      paramParcel.writeLong(visibleAppThreshold);
      paramParcel.writeLong(foregroundAppThreshold);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MoveTaskFlags {}
  
  @SystemApi
  public static abstract interface OnUidImportanceListener
  {
    public abstract void onUidImportance(int paramInt1, int paramInt2);
  }
  
  public static class ProcessErrorStateInfo
    implements Parcelable
  {
    public static final int CRASHED = 1;
    public static final Parcelable.Creator<ProcessErrorStateInfo> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.ProcessErrorStateInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.ProcessErrorStateInfo(paramAnonymousParcel, null);
      }
      
      public ActivityManager.ProcessErrorStateInfo[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.ProcessErrorStateInfo[paramAnonymousInt];
      }
    };
    public static final int NOT_RESPONDING = 2;
    public static final int NO_ERROR = 0;
    public int condition;
    public byte[] crashData = null;
    public String longMsg;
    public int pid;
    public String processName;
    public String shortMsg;
    public String stackTrace;
    public String tag;
    public int uid;
    
    public ProcessErrorStateInfo() {}
    
    private ProcessErrorStateInfo(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      condition = paramParcel.readInt();
      processName = paramParcel.readString();
      pid = paramParcel.readInt();
      uid = paramParcel.readInt();
      tag = paramParcel.readString();
      shortMsg = paramParcel.readString();
      longMsg = paramParcel.readString();
      stackTrace = paramParcel.readString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(condition);
      paramParcel.writeString(processName);
      paramParcel.writeInt(pid);
      paramParcel.writeInt(uid);
      paramParcel.writeString(tag);
      paramParcel.writeString(shortMsg);
      paramParcel.writeString(longMsg);
      paramParcel.writeString(stackTrace);
    }
  }
  
  public static class RecentTaskInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<RecentTaskInfo> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.RecentTaskInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.RecentTaskInfo(paramAnonymousParcel, null);
      }
      
      public ActivityManager.RecentTaskInfo[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.RecentTaskInfo[paramAnonymousInt];
      }
    };
    public int affiliatedTaskColor;
    public int affiliatedTaskId;
    public ComponentName baseActivity;
    public Intent baseIntent;
    public Rect bounds;
    public final Configuration configuration = new Configuration();
    public CharSequence description;
    public long firstActiveTime;
    public int id;
    public long lastActiveTime;
    public int notchUiMode;
    public int numActivities;
    public ComponentName origActivity;
    public int persistentId;
    public ComponentName realActivity;
    public int resizeMode;
    public int scaleMode;
    public int stackId;
    public boolean supportsSplitScreenMultiWindow;
    public ActivityManager.TaskDescription taskDescription;
    public ComponentName topActivity;
    public int userId;
    
    public RecentTaskInfo() {}
    
    private RecentTaskInfo(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      id = paramParcel.readInt();
      persistentId = paramParcel.readInt();
      int i = paramParcel.readInt();
      Object localObject1 = null;
      if (i > 0) {
        localObject2 = (Intent)Intent.CREATOR.createFromParcel(paramParcel);
      } else {
        localObject2 = null;
      }
      baseIntent = ((Intent)localObject2);
      origActivity = ComponentName.readFromParcel(paramParcel);
      realActivity = ComponentName.readFromParcel(paramParcel);
      description = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      if (paramParcel.readInt() > 0) {
        localObject2 = (ActivityManager.TaskDescription)ActivityManager.TaskDescription.CREATOR.createFromParcel(paramParcel);
      } else {
        localObject2 = null;
      }
      taskDescription = ((ActivityManager.TaskDescription)localObject2);
      stackId = paramParcel.readInt();
      userId = paramParcel.readInt();
      lastActiveTime = paramParcel.readLong();
      affiliatedTaskId = paramParcel.readInt();
      affiliatedTaskColor = paramParcel.readInt();
      baseActivity = ComponentName.readFromParcel(paramParcel);
      topActivity = ComponentName.readFromParcel(paramParcel);
      numActivities = paramParcel.readInt();
      Object localObject2 = localObject1;
      if (paramParcel.readInt() > 0) {
        localObject2 = (Rect)Rect.CREATOR.createFromParcel(paramParcel);
      }
      bounds = ((Rect)localObject2);
      i = paramParcel.readInt();
      boolean bool = true;
      if (i != 1) {
        bool = false;
      }
      supportsSplitScreenMultiWindow = bool;
      resizeMode = paramParcel.readInt();
      if (Build.FEATURES.ENABLE_APP_SCALING) {
        scaleMode = paramParcel.readInt();
      }
      if (Build.FEATURES.ENABLE_NOTCH_UI) {
        notchUiMode = paramParcel.readInt();
      }
      configuration.readFromParcel(paramParcel);
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(id);
      paramParcel.writeInt(persistentId);
      if (baseIntent != null)
      {
        paramParcel.writeInt(1);
        baseIntent.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      ComponentName.writeToParcel(origActivity, paramParcel);
      ComponentName.writeToParcel(realActivity, paramParcel);
      TextUtils.writeToParcel(description, paramParcel, 1);
      if (taskDescription != null)
      {
        paramParcel.writeInt(1);
        taskDescription.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      paramParcel.writeInt(stackId);
      paramParcel.writeInt(userId);
      paramParcel.writeLong(lastActiveTime);
      paramParcel.writeInt(affiliatedTaskId);
      paramParcel.writeInt(affiliatedTaskColor);
      ComponentName.writeToParcel(baseActivity, paramParcel);
      ComponentName.writeToParcel(topActivity, paramParcel);
      paramParcel.writeInt(numActivities);
      if (bounds != null)
      {
        paramParcel.writeInt(1);
        bounds.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      paramParcel.writeInt(supportsSplitScreenMultiWindow);
      paramParcel.writeInt(resizeMode);
      if (Build.FEATURES.ENABLE_APP_SCALING) {
        paramParcel.writeInt(scaleMode);
      }
      if (Build.FEATURES.ENABLE_NOTCH_UI) {
        paramParcel.writeInt(notchUiMode);
      }
      configuration.writeToParcel(paramParcel, paramInt);
    }
  }
  
  public static class RunningAppProcessInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<RunningAppProcessInfo> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.RunningAppProcessInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.RunningAppProcessInfo(paramAnonymousParcel, null);
      }
      
      public ActivityManager.RunningAppProcessInfo[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.RunningAppProcessInfo[paramAnonymousInt];
      }
    };
    public static final int FLAG_CANT_SAVE_STATE = 1;
    public static final int FLAG_HAS_ACTIVITIES = 4;
    public static final int FLAG_PERSISTENT = 2;
    public static final int IMPORTANCE_BACKGROUND = 400;
    public static final int IMPORTANCE_CACHED = 400;
    public static final int IMPORTANCE_CANT_SAVE_STATE = 350;
    public static final int IMPORTANCE_CANT_SAVE_STATE_PRE_26 = 170;
    @Deprecated
    public static final int IMPORTANCE_EMPTY = 500;
    public static final int IMPORTANCE_FOREGROUND = 100;
    public static final int IMPORTANCE_FOREGROUND_SERVICE = 125;
    public static final int IMPORTANCE_GONE = 1000;
    public static final int IMPORTANCE_PERCEPTIBLE = 230;
    public static final int IMPORTANCE_PERCEPTIBLE_PRE_26 = 130;
    public static final int IMPORTANCE_SERVICE = 300;
    public static final int IMPORTANCE_TOP_SLEEPING = 325;
    @Deprecated
    public static final int IMPORTANCE_TOP_SLEEPING_PRE_28 = 150;
    public static final int IMPORTANCE_VISIBLE = 200;
    public static final int REASON_PROVIDER_IN_USE = 1;
    public static final int REASON_SERVICE_IN_USE = 2;
    public static final int REASON_UNKNOWN = 0;
    public int flags;
    public int importance;
    public int importanceReasonCode;
    public ComponentName importanceReasonComponent;
    public int importanceReasonImportance;
    public int importanceReasonPid;
    public int lastTrimLevel;
    public int lru;
    public int pid;
    public String[] pkgList;
    public String processName;
    public int processState;
    public int uid;
    
    public RunningAppProcessInfo()
    {
      importance = 100;
      importanceReasonCode = 0;
      processState = 5;
    }
    
    private RunningAppProcessInfo(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public RunningAppProcessInfo(String paramString, int paramInt, String[] paramArrayOfString)
    {
      processName = paramString;
      pid = paramInt;
      pkgList = paramArrayOfString;
    }
    
    public static int importanceToProcState(int paramInt)
    {
      if (paramInt == 1000) {
        return 19;
      }
      if (paramInt >= 400) {
        return 13;
      }
      if (paramInt >= 350) {
        return 12;
      }
      if (paramInt >= 325) {
        return 11;
      }
      if (paramInt >= 300) {
        return 9;
      }
      if (paramInt >= 230) {
        return 7;
      }
      if (paramInt >= 200) {
        return 5;
      }
      if (paramInt >= 150) {
        return 5;
      }
      if (paramInt >= 125) {
        return 3;
      }
      return 2;
    }
    
    public static int procStateToImportance(int paramInt)
    {
      if (paramInt == 19) {
        return 1000;
      }
      if (paramInt >= 13) {
        return 400;
      }
      if (paramInt == 12) {
        return 350;
      }
      if (paramInt >= 11) {
        return 325;
      }
      if (paramInt >= 9) {
        return 300;
      }
      if (paramInt >= 7) {
        return 230;
      }
      if (paramInt >= 5) {
        return 200;
      }
      if (paramInt >= 3) {
        return 125;
      }
      return 100;
    }
    
    public static int procStateToImportanceForClient(int paramInt, Context paramContext)
    {
      return procStateToImportanceForTargetSdk(paramInt, getApplicationInfotargetSdkVersion);
    }
    
    public static int procStateToImportanceForTargetSdk(int paramInt1, int paramInt2)
    {
      paramInt1 = procStateToImportance(paramInt1);
      if (paramInt2 < 26) {
        if (paramInt1 != 230)
        {
          if (paramInt1 != 325)
          {
            if (paramInt1 == 350) {
              return 170;
            }
          }
          else {
            return 150;
          }
        }
        else {
          return 130;
        }
      }
      return paramInt1;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      processName = paramParcel.readString();
      pid = paramParcel.readInt();
      uid = paramParcel.readInt();
      pkgList = paramParcel.readStringArray();
      flags = paramParcel.readInt();
      lastTrimLevel = paramParcel.readInt();
      importance = paramParcel.readInt();
      lru = paramParcel.readInt();
      importanceReasonCode = paramParcel.readInt();
      importanceReasonPid = paramParcel.readInt();
      importanceReasonComponent = ComponentName.readFromParcel(paramParcel);
      importanceReasonImportance = paramParcel.readInt();
      processState = paramParcel.readInt();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(processName);
      paramParcel.writeInt(pid);
      paramParcel.writeInt(uid);
      paramParcel.writeStringArray(pkgList);
      paramParcel.writeInt(flags);
      paramParcel.writeInt(lastTrimLevel);
      paramParcel.writeInt(importance);
      paramParcel.writeInt(lru);
      paramParcel.writeInt(importanceReasonCode);
      paramParcel.writeInt(importanceReasonPid);
      ComponentName.writeToParcel(importanceReasonComponent, paramParcel);
      paramParcel.writeInt(importanceReasonImportance);
      paramParcel.writeInt(processState);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Importance {}
  }
  
  public static class RunningServiceInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<RunningServiceInfo> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.RunningServiceInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.RunningServiceInfo(paramAnonymousParcel, null);
      }
      
      public ActivityManager.RunningServiceInfo[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.RunningServiceInfo[paramAnonymousInt];
      }
    };
    public static final int FLAG_FOREGROUND = 2;
    public static final int FLAG_PERSISTENT_PROCESS = 8;
    public static final int FLAG_STARTED = 1;
    public static final int FLAG_SYSTEM_PROCESS = 4;
    public long activeSince;
    public int clientCount;
    public int clientLabel;
    public String clientPackage;
    public int crashCount;
    public int flags;
    public boolean foreground;
    public long lastActivityTime;
    public int pid;
    public String process;
    public long restarting;
    public ComponentName service;
    public boolean started;
    public int uid;
    
    public RunningServiceInfo() {}
    
    private RunningServiceInfo(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      service = ComponentName.readFromParcel(paramParcel);
      pid = paramParcel.readInt();
      uid = paramParcel.readInt();
      process = paramParcel.readString();
      int i = paramParcel.readInt();
      boolean bool1 = false;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      foreground = bool2;
      activeSince = paramParcel.readLong();
      boolean bool2 = bool1;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      }
      started = bool2;
      clientCount = paramParcel.readInt();
      crashCount = paramParcel.readInt();
      lastActivityTime = paramParcel.readLong();
      restarting = paramParcel.readLong();
      flags = paramParcel.readInt();
      clientPackage = paramParcel.readString();
      clientLabel = paramParcel.readInt();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      ComponentName.writeToParcel(service, paramParcel);
      paramParcel.writeInt(pid);
      paramParcel.writeInt(uid);
      paramParcel.writeString(process);
      paramParcel.writeInt(foreground);
      paramParcel.writeLong(activeSince);
      paramParcel.writeInt(started);
      paramParcel.writeInt(clientCount);
      paramParcel.writeInt(crashCount);
      paramParcel.writeLong(lastActivityTime);
      paramParcel.writeLong(restarting);
      paramParcel.writeInt(flags);
      paramParcel.writeString(clientPackage);
      paramParcel.writeInt(clientLabel);
    }
  }
  
  public static class RunningTaskInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<RunningTaskInfo> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.RunningTaskInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.RunningTaskInfo(paramAnonymousParcel, null);
      }
      
      public ActivityManager.RunningTaskInfo[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.RunningTaskInfo[paramAnonymousInt];
      }
    };
    public ComponentName baseActivity;
    public final Configuration configuration = new Configuration();
    public CharSequence description;
    public int id;
    public long lastActiveTime;
    public int numActivities;
    public int numRunning;
    public int resizeMode;
    public int stackId;
    public boolean supportsSplitScreenMultiWindow;
    public Bitmap thumbnail;
    public ComponentName topActivity;
    
    public RunningTaskInfo() {}
    
    private RunningTaskInfo(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      id = paramParcel.readInt();
      stackId = paramParcel.readInt();
      baseActivity = ComponentName.readFromParcel(paramParcel);
      topActivity = ComponentName.readFromParcel(paramParcel);
      if (paramParcel.readInt() != 0) {
        thumbnail = ((Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel));
      } else {
        thumbnail = null;
      }
      description = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      numActivities = paramParcel.readInt();
      numRunning = paramParcel.readInt();
      boolean bool;
      if (paramParcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      }
      supportsSplitScreenMultiWindow = bool;
      resizeMode = paramParcel.readInt();
      configuration.readFromParcel(paramParcel);
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(id);
      paramParcel.writeInt(stackId);
      ComponentName.writeToParcel(baseActivity, paramParcel);
      ComponentName.writeToParcel(topActivity, paramParcel);
      if (thumbnail != null)
      {
        paramParcel.writeInt(1);
        thumbnail.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      TextUtils.writeToParcel(description, paramParcel, 1);
      paramParcel.writeInt(numActivities);
      paramParcel.writeInt(numRunning);
      paramParcel.writeInt(supportsSplitScreenMultiWindow);
      paramParcel.writeInt(resizeMode);
      configuration.writeToParcel(paramParcel, paramInt);
    }
  }
  
  public static class StackId
  {
    public static final int INVALID_STACK_ID = -1;
    
    private StackId() {}
  }
  
  public static class StackInfo
    implements Parcelable
  {
    public static final Parcelable.Creator<StackInfo> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.StackInfo createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.StackInfo(paramAnonymousParcel, null);
      }
      
      public ActivityManager.StackInfo[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.StackInfo[paramAnonymousInt];
      }
    };
    public Rect bounds = new Rect();
    public final Configuration configuration = new Configuration();
    public int displayId;
    public int position;
    public int stackId;
    public Rect[] taskBounds;
    public int[] taskIds;
    public String[] taskNames;
    public int[] taskUserIds;
    public ComponentName topActivity;
    public int userId;
    public boolean visible;
    
    public StackInfo() {}
    
    private StackInfo(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      stackId = paramParcel.readInt();
      bounds = new Rect(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
      taskIds = paramParcel.createIntArray();
      taskNames = paramParcel.createStringArray();
      int i = paramParcel.readInt();
      boolean bool = false;
      if (i > 0)
      {
        taskBounds = new Rect[i];
        for (int j = 0; j < i; j++)
        {
          taskBounds[j] = new Rect();
          taskBounds[j].set(paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt(), paramParcel.readInt());
        }
      }
      taskBounds = null;
      taskUserIds = paramParcel.createIntArray();
      displayId = paramParcel.readInt();
      userId = paramParcel.readInt();
      if (paramParcel.readInt() > 0) {
        bool = true;
      }
      visible = bool;
      position = paramParcel.readInt();
      if (paramParcel.readInt() > 0) {
        topActivity = ComponentName.readFromParcel(paramParcel);
      }
      configuration.readFromParcel(paramParcel);
    }
    
    public String toString()
    {
      return toString("");
    }
    
    public String toString(String paramString)
    {
      StringBuilder localStringBuilder1 = new StringBuilder(256);
      localStringBuilder1.append(paramString);
      localStringBuilder1.append("Stack id=");
      localStringBuilder1.append(stackId);
      localStringBuilder1.append(" bounds=");
      localStringBuilder1.append(bounds.toShortString());
      localStringBuilder1.append(" displayId=");
      localStringBuilder1.append(displayId);
      localStringBuilder1.append(" userId=");
      localStringBuilder1.append(userId);
      localStringBuilder1.append("\n");
      localStringBuilder1.append(" configuration=");
      localStringBuilder1.append(configuration);
      localStringBuilder1.append("\n");
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append(paramString);
      localStringBuilder2.append("  ");
      paramString = localStringBuilder2.toString();
      for (int i = 0; i < taskIds.length; i++)
      {
        localStringBuilder1.append(paramString);
        localStringBuilder1.append("taskId=");
        localStringBuilder1.append(taskIds[i]);
        localStringBuilder1.append(": ");
        localStringBuilder1.append(taskNames[i]);
        if (taskBounds != null)
        {
          localStringBuilder1.append(" bounds=");
          localStringBuilder1.append(taskBounds[i].toShortString());
        }
        localStringBuilder1.append(" userId=");
        localStringBuilder1.append(taskUserIds[i]);
        localStringBuilder1.append(" visible=");
        localStringBuilder1.append(visible);
        if (topActivity != null)
        {
          localStringBuilder1.append(" topActivity=");
          localStringBuilder1.append(topActivity);
        }
        localStringBuilder1.append("\n");
      }
      return localStringBuilder1.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(stackId);
      paramParcel.writeInt(bounds.left);
      paramParcel.writeInt(bounds.top);
      paramParcel.writeInt(bounds.right);
      paramParcel.writeInt(bounds.bottom);
      paramParcel.writeIntArray(taskIds);
      paramParcel.writeStringArray(taskNames);
      int i;
      if (taskBounds == null) {
        i = 0;
      } else {
        i = taskBounds.length;
      }
      paramParcel.writeInt(i);
      for (int j = 0; j < i; j++)
      {
        paramParcel.writeInt(taskBounds[j].left);
        paramParcel.writeInt(taskBounds[j].top);
        paramParcel.writeInt(taskBounds[j].right);
        paramParcel.writeInt(taskBounds[j].bottom);
      }
      paramParcel.writeIntArray(taskUserIds);
      paramParcel.writeInt(displayId);
      paramParcel.writeInt(userId);
      paramParcel.writeInt(visible);
      paramParcel.writeInt(position);
      if (topActivity != null)
      {
        paramParcel.writeInt(1);
        topActivity.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      configuration.writeToParcel(paramParcel, paramInt);
    }
  }
  
  public static class TaskDescription
    implements Parcelable
  {
    private static final String ATTR_TASKDESCRIPTIONCOLOR_BACKGROUND = "task_description_colorBackground";
    private static final String ATTR_TASKDESCRIPTIONCOLOR_PRIMARY = "task_description_color";
    private static final String ATTR_TASKDESCRIPTIONICON_FILENAME = "task_description_icon_filename";
    private static final String ATTR_TASKDESCRIPTIONICON_RESOURCE = "task_description_icon_resource";
    private static final String ATTR_TASKDESCRIPTIONLABEL = "task_description_label";
    public static final String ATTR_TASKDESCRIPTION_PREFIX = "task_description_";
    public static final Parcelable.Creator<TaskDescription> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.TaskDescription createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.TaskDescription(paramAnonymousParcel, null);
      }
      
      public ActivityManager.TaskDescription[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.TaskDescription[paramAnonymousInt];
      }
    };
    private int mColorBackground;
    private int mColorPrimary;
    private Bitmap mIcon;
    private String mIconFilename;
    private int mIconRes;
    private String mLabel;
    private int mNavigationBarColor;
    private int mStatusBarColor;
    
    public TaskDescription()
    {
      this(null, null, 0, null, 0, 0, 0, 0);
    }
    
    public TaskDescription(TaskDescription paramTaskDescription)
    {
      copyFrom(paramTaskDescription);
    }
    
    private TaskDescription(Parcel paramParcel)
    {
      readFromParcel(paramParcel);
    }
    
    public TaskDescription(String paramString)
    {
      this(paramString, null, 0, null, 0, 0, 0, 0);
    }
    
    public TaskDescription(String paramString, int paramInt)
    {
      this(paramString, null, paramInt, null, 0, 0, 0, 0);
    }
    
    public TaskDescription(String paramString, int paramInt1, int paramInt2)
    {
      this(paramString, null, paramInt1, null, paramInt2, 0, 0, 0);
      if ((paramInt2 != 0) && (Color.alpha(paramInt2) != 255)) {
        throw new RuntimeException("A TaskDescription's primary color should be opaque");
      }
    }
    
    @Deprecated
    public TaskDescription(String paramString, Bitmap paramBitmap)
    {
      this(paramString, paramBitmap, 0, null, 0, 0, 0, 0);
    }
    
    @Deprecated
    public TaskDescription(String paramString, Bitmap paramBitmap, int paramInt)
    {
      this(paramString, paramBitmap, 0, null, paramInt, 0, 0, 0);
      if ((paramInt != 0) && (Color.alpha(paramInt) != 255)) {
        throw new RuntimeException("A TaskDescription's primary color should be opaque");
      }
    }
    
    public TaskDescription(String paramString1, Bitmap paramBitmap, int paramInt1, String paramString2, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      mLabel = paramString1;
      mIcon = paramBitmap;
      mIconRes = paramInt1;
      mIconFilename = paramString2;
      mColorPrimary = paramInt2;
      mColorBackground = paramInt3;
      mStatusBarColor = paramInt4;
      mNavigationBarColor = paramInt5;
    }
    
    public static Bitmap loadTaskDescriptionIcon(String paramString, int paramInt)
    {
      if (paramString != null) {
        try
        {
          paramString = ActivityManager.getService().getTaskDescriptionIcon(paramString, paramInt);
          return paramString;
        }
        catch (RemoteException paramString)
        {
          throw paramString.rethrowFromSystemServer();
        }
      }
      return null;
    }
    
    public void copyFrom(TaskDescription paramTaskDescription)
    {
      mLabel = mLabel;
      mIcon = mIcon;
      mIconRes = mIconRes;
      mIconFilename = mIconFilename;
      mColorPrimary = mColorPrimary;
      mColorBackground = mColorBackground;
      mStatusBarColor = mStatusBarColor;
      mNavigationBarColor = mNavigationBarColor;
    }
    
    public void copyFromPreserveHiddenFields(TaskDescription paramTaskDescription)
    {
      mLabel = mLabel;
      mIcon = mIcon;
      mIconRes = mIconRes;
      mIconFilename = mIconFilename;
      mColorPrimary = mColorPrimary;
      if (mColorBackground != 0) {
        mColorBackground = mColorBackground;
      }
      if (mStatusBarColor != 0) {
        mStatusBarColor = mStatusBarColor;
      }
      if (mNavigationBarColor != 0) {
        mNavigationBarColor = mNavigationBarColor;
      }
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getBackgroundColor()
    {
      return mColorBackground;
    }
    
    public Bitmap getIcon()
    {
      if (mIcon != null) {
        return mIcon;
      }
      return loadTaskDescriptionIcon(mIconFilename, UserHandle.myUserId());
    }
    
    public String getIconFilename()
    {
      return mIconFilename;
    }
    
    public int getIconResource()
    {
      return mIconRes;
    }
    
    public Bitmap getInMemoryIcon()
    {
      return mIcon;
    }
    
    public String getLabel()
    {
      return mLabel;
    }
    
    public int getNavigationBarColor()
    {
      return mNavigationBarColor;
    }
    
    public int getPrimaryColor()
    {
      return mColorPrimary;
    }
    
    public int getStatusBarColor()
    {
      return mStatusBarColor;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      Object localObject1 = null;
      if (i > 0) {
        localObject2 = paramParcel.readString();
      } else {
        localObject2 = null;
      }
      mLabel = ((String)localObject2);
      if (paramParcel.readInt() > 0) {
        localObject2 = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel);
      } else {
        localObject2 = null;
      }
      mIcon = ((Bitmap)localObject2);
      mIconRes = paramParcel.readInt();
      mColorPrimary = paramParcel.readInt();
      mColorBackground = paramParcel.readInt();
      mStatusBarColor = paramParcel.readInt();
      mNavigationBarColor = paramParcel.readInt();
      Object localObject2 = localObject1;
      if (paramParcel.readInt() > 0) {
        localObject2 = paramParcel.readString();
      }
      mIconFilename = ((String)localObject2);
    }
    
    public void restoreFromXml(String paramString1, String paramString2)
    {
      if ("task_description_label".equals(paramString1)) {
        setLabel(paramString2);
      } else if ("task_description_color".equals(paramString1)) {
        setPrimaryColor((int)Long.parseLong(paramString2, 16));
      } else if ("task_description_colorBackground".equals(paramString1)) {
        setBackgroundColor((int)Long.parseLong(paramString2, 16));
      } else if ("task_description_icon_filename".equals(paramString1)) {
        setIconFilename(paramString2);
      } else if ("task_description_icon_resource".equals(paramString1)) {
        setIcon(Integer.parseInt(paramString2, 10));
      }
    }
    
    public void saveToXml(XmlSerializer paramXmlSerializer)
      throws IOException
    {
      if (mLabel != null) {
        paramXmlSerializer.attribute(null, "task_description_label", mLabel);
      }
      if (mColorPrimary != 0) {
        paramXmlSerializer.attribute(null, "task_description_color", Integer.toHexString(mColorPrimary));
      }
      if (mColorBackground != 0) {
        paramXmlSerializer.attribute(null, "task_description_colorBackground", Integer.toHexString(mColorBackground));
      }
      if (mIconFilename != null) {
        paramXmlSerializer.attribute(null, "task_description_icon_filename", mIconFilename);
      }
      if (mIconRes != 0) {
        paramXmlSerializer.attribute(null, "task_description_icon_resource", Integer.toString(mIconRes));
      }
    }
    
    public void setBackgroundColor(int paramInt)
    {
      if ((paramInt != 0) && (Color.alpha(paramInt) != 255)) {
        throw new RuntimeException("A TaskDescription's background color should be opaque");
      }
      mColorBackground = paramInt;
    }
    
    public void setIcon(int paramInt)
    {
      mIconRes = paramInt;
    }
    
    public void setIcon(Bitmap paramBitmap)
    {
      mIcon = paramBitmap;
    }
    
    public void setIconFilename(String paramString)
    {
      mIconFilename = paramString;
      mIcon = null;
    }
    
    public void setLabel(String paramString)
    {
      mLabel = paramString;
    }
    
    public void setNavigationBarColor(int paramInt)
    {
      mNavigationBarColor = paramInt;
    }
    
    public void setPrimaryColor(int paramInt)
    {
      if ((paramInt != 0) && (Color.alpha(paramInt) != 255)) {
        throw new RuntimeException("A TaskDescription's primary color should be opaque");
      }
      mColorPrimary = paramInt;
    }
    
    public void setStatusBarColor(int paramInt)
    {
      mStatusBarColor = paramInt;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("TaskDescription Label: ");
      localStringBuilder.append(mLabel);
      localStringBuilder.append(" Icon: ");
      localStringBuilder.append(mIcon);
      localStringBuilder.append(" IconRes: ");
      localStringBuilder.append(mIconRes);
      localStringBuilder.append(" IconFilename: ");
      localStringBuilder.append(mIconFilename);
      localStringBuilder.append(" colorPrimary: ");
      localStringBuilder.append(mColorPrimary);
      localStringBuilder.append(" colorBackground: ");
      localStringBuilder.append(mColorBackground);
      localStringBuilder.append(" statusBarColor: ");
      localStringBuilder.append(mColorBackground);
      localStringBuilder.append(" navigationBarColor: ");
      localStringBuilder.append(mNavigationBarColor);
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      if (mLabel == null)
      {
        paramParcel.writeInt(0);
      }
      else
      {
        paramParcel.writeInt(1);
        paramParcel.writeString(mLabel);
      }
      if (mIcon == null)
      {
        paramParcel.writeInt(0);
      }
      else
      {
        paramParcel.writeInt(1);
        mIcon.writeToParcel(paramParcel, 0);
      }
      paramParcel.writeInt(mIconRes);
      paramParcel.writeInt(mColorPrimary);
      paramParcel.writeInt(mColorBackground);
      paramParcel.writeInt(mStatusBarColor);
      paramParcel.writeInt(mNavigationBarColor);
      if (mIconFilename == null)
      {
        paramParcel.writeInt(0);
      }
      else
      {
        paramParcel.writeInt(1);
        paramParcel.writeString(mIconFilename);
      }
    }
  }
  
  public static class TaskSnapshot
    implements Parcelable
  {
    public static final Parcelable.Creator<TaskSnapshot> CREATOR = new Parcelable.Creator()
    {
      public ActivityManager.TaskSnapshot createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActivityManager.TaskSnapshot(paramAnonymousParcel, null);
      }
      
      public ActivityManager.TaskSnapshot[] newArray(int paramAnonymousInt)
      {
        return new ActivityManager.TaskSnapshot[paramAnonymousInt];
      }
    };
    private final Rect mContentInsets;
    private final boolean mIsRealSnapshot;
    private final boolean mIsTranslucent;
    private final int mOrientation;
    private final boolean mReducedResolution;
    private final float mScale;
    private final GraphicBuffer mSnapshot;
    private final int mSystemUiVisibility;
    private final int mWindowingMode;
    
    public TaskSnapshot(GraphicBuffer paramGraphicBuffer, int paramInt1, Rect paramRect, boolean paramBoolean1, float paramFloat, boolean paramBoolean2, int paramInt2, int paramInt3, boolean paramBoolean3)
    {
      mSnapshot = paramGraphicBuffer;
      mOrientation = paramInt1;
      mContentInsets = new Rect(paramRect);
      mReducedResolution = paramBoolean1;
      mScale = paramFloat;
      mIsRealSnapshot = paramBoolean2;
      mWindowingMode = paramInt2;
      mSystemUiVisibility = paramInt3;
      mIsTranslucent = paramBoolean3;
    }
    
    private TaskSnapshot(Parcel paramParcel)
    {
      mSnapshot = ((GraphicBuffer)paramParcel.readParcelable(null));
      mOrientation = paramParcel.readInt();
      mContentInsets = ((Rect)paramParcel.readParcelable(null));
      mReducedResolution = paramParcel.readBoolean();
      mScale = paramParcel.readFloat();
      mIsRealSnapshot = paramParcel.readBoolean();
      mWindowingMode = paramParcel.readInt();
      mSystemUiVisibility = paramParcel.readInt();
      mIsTranslucent = paramParcel.readBoolean();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public Rect getContentInsets()
    {
      return mContentInsets;
    }
    
    public int getOrientation()
    {
      return mOrientation;
    }
    
    public float getScale()
    {
      return mScale;
    }
    
    public GraphicBuffer getSnapshot()
    {
      return mSnapshot;
    }
    
    public int getSystemUiVisibility()
    {
      return mSystemUiVisibility;
    }
    
    public int getWindowingMode()
    {
      return mWindowingMode;
    }
    
    public boolean isRealSnapshot()
    {
      return mIsRealSnapshot;
    }
    
    public boolean isReducedResolution()
    {
      return mReducedResolution;
    }
    
    public boolean isTranslucent()
    {
      return mIsTranslucent;
    }
    
    public String toString()
    {
      Object localObject = mSnapshot;
      int i = 0;
      int j;
      if (localObject != null) {
        j = mSnapshot.getWidth();
      } else {
        j = 0;
      }
      if (mSnapshot != null) {
        i = mSnapshot.getHeight();
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("TaskSnapshot{mSnapshot=");
      ((StringBuilder)localObject).append(mSnapshot);
      ((StringBuilder)localObject).append(" (");
      ((StringBuilder)localObject).append(j);
      ((StringBuilder)localObject).append("x");
      ((StringBuilder)localObject).append(i);
      ((StringBuilder)localObject).append(") mOrientation=");
      ((StringBuilder)localObject).append(mOrientation);
      ((StringBuilder)localObject).append(" mContentInsets=");
      ((StringBuilder)localObject).append(mContentInsets.toShortString());
      ((StringBuilder)localObject).append(" mReducedResolution=");
      ((StringBuilder)localObject).append(mReducedResolution);
      ((StringBuilder)localObject).append(" mScale=");
      ((StringBuilder)localObject).append(mScale);
      ((StringBuilder)localObject).append(" mIsRealSnapshot=");
      ((StringBuilder)localObject).append(mIsRealSnapshot);
      ((StringBuilder)localObject).append(" mWindowingMode=");
      ((StringBuilder)localObject).append(mWindowingMode);
      ((StringBuilder)localObject).append(" mSystemUiVisibility=");
      ((StringBuilder)localObject).append(mSystemUiVisibility);
      ((StringBuilder)localObject).append(" mIsTranslucent=");
      ((StringBuilder)localObject).append(mIsTranslucent);
      return ((StringBuilder)localObject).toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeParcelable(mSnapshot, 0);
      paramParcel.writeInt(mOrientation);
      paramParcel.writeParcelable(mContentInsets, 0);
      paramParcel.writeBoolean(mReducedResolution);
      paramParcel.writeFloat(mScale);
      paramParcel.writeBoolean(mIsRealSnapshot);
      paramParcel.writeInt(mWindowingMode);
      paramParcel.writeInt(mSystemUiVisibility);
      paramParcel.writeBoolean(mIsTranslucent);
    }
  }
  
  static final class UidObserver
    extends IUidObserver.Stub
  {
    final Context mContext;
    final ActivityManager.OnUidImportanceListener mListener;
    
    UidObserver(ActivityManager.OnUidImportanceListener paramOnUidImportanceListener, Context paramContext)
    {
      mListener = paramOnUidImportanceListener;
      mContext = paramContext;
    }
    
    public void onUidActive(int paramInt) {}
    
    public void onUidCachedChanged(int paramInt, boolean paramBoolean) {}
    
    public void onUidGone(int paramInt, boolean paramBoolean)
    {
      mListener.onUidImportance(paramInt, 1000);
    }
    
    public void onUidIdle(int paramInt, boolean paramBoolean) {}
    
    public void onUidStateChanged(int paramInt1, int paramInt2, long paramLong)
    {
      mListener.onUidImportance(paramInt1, ActivityManager.RunningAppProcessInfo.procStateToImportanceForClient(paramInt2, mContext));
    }
  }
}
