package android.app;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.app.backup.BackupAgent;
import android.app.servertransaction.ActivityRelaunchItem;
import android.app.servertransaction.ActivityResultItem;
import android.app.servertransaction.ClientTransaction;
import android.app.servertransaction.PendingTransactionActions;
import android.app.servertransaction.PendingTransactionActions.StopInfo;
import android.app.servertransaction.TransactionExecutor;
import android.app.servertransaction.TransactionExecutorHelper;
import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ParceledListSlice;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDebug;
import android.database.sqlite.SQLiteDebug.DbStats;
import android.database.sqlite.SQLiteDebug.PagerStats;
import android.ddm.DdmHandleAppName;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.hardware.display.DisplayManagerGlobal;
import android.net.ConnectivityManager;
import android.net.IConnectivityManager;
import android.net.IConnectivityManager.Stub;
import android.net.Proxy;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Build.FEATURES;
import android.os.Bundle;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.GraphicsEnvironment;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.TransactionTracker;
import android.os.UserHandle;
import android.provider.FontsContract;
import android.renderscript.RenderScriptCacheDir;
import android.security.NetworkSecurityPolicy;
import android.security.net.config.NetworkSecurityConfigProvider;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.BoostFramework;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.LogPrinter;
import android.util.MergedConfiguration;
import android.util.Pair;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SparseIntArray;
import android.util.SuperNotCalledException;
import android.util.proto.ProtoOutputStream;
import android.view.Choreographer;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewManager;
import android.view.ViewRootImpl;
import android.view.ViewRootImpl.ActivityConfigCallback;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import android.webkit.WebView;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.content.ReferrerIntent;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.RuntimeInit;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.function.pooled.PooledLambda;
import com.android.internal.util.function.pooled.PooledRunnable;
import com.android.org.conscrypt.OpenSSLSocketImpl;
import com.android.org.conscrypt.TrustedCertificateStore;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.CloseGuard;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import libcore.io.DropBox;
import libcore.io.DropBox.Reporter;
import libcore.io.EventLogger;
import libcore.io.EventLogger.Reporter;
import libcore.io.IoUtils;
import libcore.net.event.NetworkEventDispatcher;
import org.apache.harmony.dalvik.ddmc.DdmVmInternal;

public final class ActivityThread
  extends ClientTransactionHandler
{
  private static final int ACTIVITY_THREAD_CHECKIN_VERSION = 4;
  private static final boolean DEBUG_BACKUP = false;
  public static final boolean DEBUG_BROADCAST = false;
  public static final boolean DEBUG_CONFIGURATION = false;
  public static final boolean DEBUG_MEMORY_TRIM = false;
  static final boolean DEBUG_MESSAGES = false;
  public static final boolean DEBUG_ORDER = false;
  private static final boolean DEBUG_PROVIDER = false;
  private static final boolean DEBUG_RESULTS = false;
  private static final boolean DEBUG_SERVICE = false;
  private static final String HEAP_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s";
  private static final String HEAP_FULL_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s";
  public static final long INVALID_PROC_STATE_SEQ = -1L;
  private static final long MIN_TIME_BETWEEN_GCS = 5000L;
  private static final String ONE_COUNT_COLUMN = "%21s %8d";
  private static final String ONE_COUNT_COLUMN_HEADER = "%21s %8s";
  public static final String PROC_START_SEQ_IDENT = "seq=";
  private static final boolean REPORT_TO_ACTIVITY = true;
  public static final int SERVICE_DONE_EXECUTING_ANON = 0;
  public static final int SERVICE_DONE_EXECUTING_START = 1;
  public static final int SERVICE_DONE_EXECUTING_STOP = 2;
  private static final int SQLITE_MEM_RELEASED_EVENT_LOG_TAG = 75003;
  public static final String TAG = "ActivityThread";
  private static final Bitmap.Config THUMBNAIL_FORMAT = Bitmap.Config.RGB_565;
  private static final String TWO_COUNT_COLUMNS = "%21s %8d %21s %8d";
  static final boolean localLOGV = false;
  private static volatile ActivityThread sCurrentActivityThread;
  private static final ThreadLocal<Intent> sCurrentBroadcastIntent = new ThreadLocal();
  static volatile Handler sMainThreadHandler;
  static volatile IPackageManager sPackageManager;
  final ArrayMap<IBinder, ActivityClientRecord> mActivities = new ArrayMap();
  final ArrayList<Application> mAllApplications = new ArrayList();
  final ApplicationThread mAppThread = new ApplicationThread(null);
  final ArrayMap<String, BackupAgent> mBackupAgents = new ArrayMap();
  AppBindData mBoundApplication;
  Configuration mCompatConfiguration;
  Configuration mConfiguration;
  Bundle mCoreSettings = null;
  int mCurDefaultDisplayDpi;
  boolean mDensityCompatMode;
  final Executor mExecutor = new HandlerExecutor(mH);
  final GcIdler mGcIdler = new GcIdler();
  boolean mGcIdlerScheduled = false;
  @GuardedBy("mGetProviderLocks")
  final ArrayMap<ProviderKey, Object> mGetProviderLocks = new ArrayMap();
  final H mH = new H();
  boolean mHiddenApiWarningShown = false;
  Application mInitialApplication;
  Instrumentation mInstrumentation;
  String mInstrumentationAppDir = null;
  String mInstrumentationLibDir = null;
  String mInstrumentationPackageName = null;
  String[] mInstrumentationSplitAppDirs = null;
  String mInstrumentedAppDir = null;
  String mInstrumentedLibDir = null;
  String[] mInstrumentedSplitAppDirs = null;
  boolean mJitEnabled = false;
  ArrayList<WeakReference<AssistStructure>> mLastAssistStructures = new ArrayList();
  private ActivityClientRecord mLastResumedActivity = null;
  private int mLastSessionId;
  final ArrayMap<IBinder, ProviderClientRecord> mLocalProviders = new ArrayMap();
  final ArrayMap<ComponentName, ProviderClientRecord> mLocalProvidersByName = new ArrayMap();
  final Looper mLooper = Looper.myLooper();
  private Configuration mMainThreadConfig = new Configuration();
  @GuardedBy("mNetworkPolicyLock")
  private long mNetworkBlockSeq = -1L;
  private final Object mNetworkPolicyLock = new Object();
  ActivityClientRecord mNewActivities = null;
  int mNumVisibleActivities = 0;
  final ArrayMap<Activity, ArrayList<OnActivityPausedListener>> mOnPauseListeners = new ArrayMap();
  @GuardedBy("mResourcesManager")
  final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap();
  @GuardedBy("mResourcesManager")
  Configuration mPendingConfiguration = null;
  Profiler mProfiler;
  final ArrayMap<ProviderKey, ProviderClientRecord> mProviderMap = new ArrayMap();
  final ArrayMap<IBinder, ProviderRefCount> mProviderRefCountMap = new ArrayMap();
  @GuardedBy("mResourcesManager")
  final ArrayList<ActivityClientRecord> mRelaunchingActivities = new ArrayList();
  @GuardedBy("mResourcesManager")
  final ArrayMap<String, WeakReference<LoadedApk>> mResourcePackages = new ArrayMap();
  private final ResourcesManager mResourcesManager = ResourcesManager.getInstance();
  final ArrayMap<IBinder, Service> mServices = new ArrayMap();
  boolean mSomeActivitiesChanged = false;
  private ContextImpl mSystemContext;
  boolean mSystemThread = false;
  private ContextImpl mSystemUiContext;
  private final TransactionExecutor mTransactionExecutor = new TransactionExecutor(this);
  boolean mUpdatingSystemConfig = false;
  
  ActivityThread() {}
  
  private void attach(boolean paramBoolean, long paramLong)
  {
    sCurrentActivityThread = this;
    mSystemThread = paramBoolean;
    if (!paramBoolean)
    {
      ViewRootImpl.addFirstDrawHandler(new Runnable()
      {
        public void run()
        {
          ensureJitEnabled();
        }
      });
      DdmHandleAppName.setAppName("<pre-initialized>", UserHandle.myUserId());
      RuntimeInit.setApplicationObject(mAppThread.asBinder());
      final IActivityManager localIActivityManager = ActivityManager.getService();
      try
      {
        localIActivityManager.attachApplication(mAppThread, paramLong);
        BinderInternal.addGcWatcher(new Runnable()
        {
          public void run()
          {
            if (!mSomeActivitiesChanged) {
              return;
            }
            Runtime localRuntime = Runtime.getRuntime();
            long l = localRuntime.maxMemory();
            if (localRuntime.totalMemory() - localRuntime.freeMemory() > 3L * l / 4L)
            {
              mSomeActivitiesChanged = false;
              try
              {
                localIActivityManager.releaseSomeActivities(mAppThread);
              }
              catch (RemoteException localRemoteException)
              {
                throw localRemoteException.rethrowFromSystemServer();
              }
            }
          }
        });
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
    }
    else
    {
      DdmHandleAppName.setAppName("system_process", UserHandle.myUserId());
    }
    try
    {
      Instrumentation localInstrumentation = new android/app/Instrumentation;
      localInstrumentation.<init>();
      mInstrumentation = localInstrumentation;
      mInstrumentation.basicInit(this);
      mInitialApplication = createAppContextgetSystemContextmPackageInfo).mPackageInfo.makeApplication(true, null);
      mInitialApplication.onCreate();
      DropBox.setReporter(new DropBoxReporter());
      ViewRootImpl.addConfigCallback(new _..Lambda.ActivityThread.ZXDWm3IBeFmLnFVblhB_IOZCr9o(this));
      return;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to instantiate Application():");
      localStringBuilder.append(localException.toString());
      throw new RuntimeException(localStringBuilder.toString(), localException);
    }
  }
  
  private static boolean attemptAttachAgent(String paramString, ClassLoader paramClassLoader)
  {
    try
    {
      VMDebug.attachAgent(paramString, paramClassLoader);
      return true;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Attaching agent with ");
      localStringBuilder.append(paramClassLoader);
      localStringBuilder.append(" failed: ");
      localStringBuilder.append(paramString);
      Slog.e("ActivityThread", localStringBuilder.toString());
    }
    return false;
  }
  
  private void callActivityOnSaveInstanceState(ActivityClientRecord paramActivityClientRecord)
  {
    state = new Bundle();
    state.setAllowFds(false);
    if (paramActivityClientRecord.isPersistable())
    {
      persistentState = new PersistableBundle();
      mInstrumentation.callActivityOnSaveInstanceState(activity, state, persistentState);
    }
    else
    {
      mInstrumentation.callActivityOnSaveInstanceState(activity, state);
    }
  }
  
  private void callActivityOnStop(ActivityClientRecord paramActivityClientRecord, boolean paramBoolean, String paramString)
  {
    int i;
    if ((paramBoolean) && (!activity.mFinished) && (state == null) && (!paramActivityClientRecord.isPreHoneycomb())) {
      i = 1;
    } else {
      i = 0;
    }
    paramBoolean = paramActivityClientRecord.isPreP();
    if ((i != 0) && (paramBoolean)) {
      callActivityOnSaveInstanceState(paramActivityClientRecord);
    }
    try
    {
      try
      {
        activity.performStop(false, paramString);
      }
      catch (Exception paramString)
      {
        if (!mInstrumentation.onException(activity, paramString)) {
          break label104;
        }
      }
      paramActivityClientRecord.setState(5);
      if ((i != 0) && (!paramBoolean)) {
        callActivityOnSaveInstanceState(paramActivityClientRecord);
      }
      return;
    }
    catch (SuperNotCalledException paramActivityClientRecord)
    {
      label104:
      StringBuilder localStringBuilder;
      throw paramActivityClientRecord;
    }
    localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unable to stop activity ");
    localStringBuilder.append(intent.getComponent().toShortString());
    localStringBuilder.append(": ");
    localStringBuilder.append(paramString.toString());
    throw new RuntimeException(localStringBuilder.toString(), paramString);
  }
  
  private void checkAndBlockForNetworkAccess()
  {
    synchronized (mNetworkPolicyLock)
    {
      long l = mNetworkBlockSeq;
      if (l != -1L) {
        try
        {
          ActivityManager.getService().waitForNetworkStateUpdate(mNetworkBlockSeq);
          mNetworkBlockSeq = -1L;
        }
        catch (RemoteException localRemoteException) {}
      }
      return;
    }
  }
  
  static final void cleanUpPendingRemoveWindows(ActivityClientRecord paramActivityClientRecord, boolean paramBoolean)
  {
    if ((mPreserveWindow) && (!paramBoolean)) {
      return;
    }
    if (mPendingRemoveWindow != null)
    {
      mPendingRemoveWindowManager.removeViewImmediate(mPendingRemoveWindow.getDecorView());
      IBinder localIBinder = mPendingRemoveWindow.getDecorView().getWindowToken();
      if (localIBinder != null) {
        WindowManagerGlobal.getInstance().closeAll(localIBinder, activity.getClass().getName(), "Activity");
      }
    }
    mPendingRemoveWindow = null;
    mPendingRemoveWindowManager = null;
  }
  
  private ContextImpl createBaseContextForActivity(ActivityClientRecord paramActivityClientRecord)
  {
    try
    {
      int i = ActivityManager.getService().getActivityDisplayId(token);
      ContextImpl localContextImpl1 = ContextImpl.createActivityContext(this, packageInfo, activityInfo, token, i, overrideConfig);
      DisplayManagerGlobal localDisplayManagerGlobal = DisplayManagerGlobal.getInstance();
      String str = SystemProperties.get("debug.second-display.pkg");
      ContextImpl localContextImpl2 = localContextImpl1;
      if (str != null)
      {
        localContextImpl2 = localContextImpl1;
        if (!str.isEmpty())
        {
          localContextImpl2 = localContextImpl1;
          if (packageInfo.mPackageName.contains(str))
          {
            paramActivityClientRecord = localDisplayManagerGlobal.getDisplayIds();
            int j = paramActivityClientRecord.length;
            for (i = 0;; i++)
            {
              localContextImpl2 = localContextImpl1;
              if (i >= j) {
                break;
              }
              int k = paramActivityClientRecord[i];
              if (k != 0)
              {
                localContextImpl2 = (ContextImpl)localContextImpl1.createDisplayContext(localDisplayManagerGlobal.getCompatibleDisplay(k, localContextImpl1.getResources()));
                break;
              }
            }
          }
        }
      }
      return localContextImpl2;
    }
    catch (RemoteException paramActivityClientRecord)
    {
      throw paramActivityClientRecord.rethrowFromSystemServer();
    }
  }
  
  private static Configuration createNewConfigAndUpdateIfNotNull(Configuration paramConfiguration1, Configuration paramConfiguration2)
  {
    if (paramConfiguration2 == null) {
      return paramConfiguration1;
    }
    paramConfiguration1 = new Configuration(paramConfiguration1);
    paramConfiguration1.updateFrom(paramConfiguration2);
    return paramConfiguration1;
  }
  
  public static ActivityThread currentActivityThread()
  {
    return sCurrentActivityThread;
  }
  
  public static Application currentApplication()
  {
    Object localObject = currentActivityThread();
    if (localObject != null) {
      localObject = mInitialApplication;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public static String currentOpPackageName()
  {
    Object localObject = currentActivityThread();
    if ((localObject != null) && (((ActivityThread)localObject).getApplication() != null)) {
      localObject = ((ActivityThread)localObject).getApplication().getOpPackageName();
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public static String currentPackageName()
  {
    Object localObject = currentActivityThread();
    if ((localObject != null) && (mBoundApplication != null)) {
      localObject = mBoundApplication.appInfo.packageName;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public static String currentProcessName()
  {
    Object localObject = currentActivityThread();
    if ((localObject != null) && (mBoundApplication != null)) {
      localObject = mBoundApplication.processName;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  private void deliverNewIntents(ActivityClientRecord paramActivityClientRecord, List<ReferrerIntent> paramList)
  {
    int i = paramList.size();
    for (int j = 0; j < i; j++)
    {
      ReferrerIntent localReferrerIntent = (ReferrerIntent)paramList.get(j);
      localReferrerIntent.setExtrasClassLoader(activity.getClassLoader());
      localReferrerIntent.prepareToEnterProcess();
      activity.mFragments.noteStateNotSaved();
      mInstrumentation.callActivityOnNewIntent(activity, localReferrerIntent);
    }
  }
  
  private void deliverResults(ActivityClientRecord paramActivityClientRecord, List<ResultInfo> paramList, String paramString)
  {
    int i = paramList.size();
    int j = 0;
    while (j < i)
    {
      ResultInfo localResultInfo = (ResultInfo)paramList.get(j);
      try
      {
        if (mData != null)
        {
          mData.setExtrasClassLoader(activity.getClassLoader());
          mData.prepareToEnterProcess();
        }
        activity.dispatchActivityResult(mResultWho, mRequestCode, mResultCode, mData, paramString);
      }
      catch (Exception localException)
      {
        if (!mInstrumentation.onException(activity, localException)) {
          break label117;
        }
      }
      j++;
      continue;
      label117:
      paramList = new StringBuilder();
      paramList.append("Failure delivering result ");
      paramList.append(localResultInfo);
      paramList.append(" to activity ");
      paramList.append(intent.getComponent().toShortString());
      paramList.append(": ");
      paramList.append(localException.toString());
      throw new RuntimeException(paramList.toString(), localException);
    }
  }
  
  public static void dumpMemInfoTable(ProtoOutputStream paramProtoOutputStream, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6)
  {
    Debug.MemoryInfo localMemoryInfo = paramMemoryInfo;
    if (!paramBoolean2)
    {
      long l = paramProtoOutputStream.start(1146756268035L);
      dumpMemoryInfo(paramProtoOutputStream, 1146756268033L, "Native Heap", nativePss, nativeSwappablePss, nativeSharedDirty, nativePrivateDirty, nativeSharedClean, nativePrivateClean, hasSwappedOutPss, nativeSwappedOut, nativeSwappedOutPss);
      paramProtoOutputStream.write(1120986464258L, paramLong1);
      paramProtoOutputStream.write(1120986464259L, paramLong2);
      paramProtoOutputStream.write(1120986464260L, paramLong3);
      paramProtoOutputStream.end(l);
      l = paramProtoOutputStream.start(1146756268036L);
      int i = dalvikPss;
      int j = dalvikSwappablePss;
      int k = dalvikSharedDirty;
      int m = dalvikPrivateDirty;
      int n = dalvikSharedClean;
      int i1 = dalvikPrivateClean;
      paramBoolean2 = hasSwappedOutPss;
      int i2 = dalvikSwappedOut;
      int i3 = dalvikSwappedOutPss;
      localMemoryInfo = paramMemoryInfo;
      dumpMemoryInfo(paramProtoOutputStream, 1146756268033L, "Dalvik Heap", i, j, k, m, n, i1, paramBoolean2, i2, i3);
      paramProtoOutputStream.write(1120986464258L, paramLong4);
      paramProtoOutputStream.write(1120986464259L, paramLong5);
      paramProtoOutputStream.write(1120986464260L, paramLong6);
      paramProtoOutputStream.end(l);
      i2 = otherPss;
      int i4 = otherSwappablePss;
      m = otherSharedDirty;
      i = otherPrivateDirty;
      j = otherSharedClean;
      n = otherPrivateClean;
      i3 = otherSwappedOut;
      i1 = otherSwappedOutPss;
      int i13;
      for (k = 0; k < 17; k++)
      {
        int i5 = localMemoryInfo.getOtherPss(k);
        int i6 = localMemoryInfo.getOtherSwappablePss(k);
        int i7 = localMemoryInfo.getOtherSharedDirty(k);
        int i8 = localMemoryInfo.getOtherPrivateDirty(k);
        int i9 = localMemoryInfo.getOtherSharedClean(k);
        int i10 = localMemoryInfo.getOtherPrivateClean(k);
        int i11 = localMemoryInfo.getOtherSwappedOut(k);
        int i12 = localMemoryInfo.getOtherSwappedOutPss(k);
        if ((i5 == 0) && (i7 == 0) && (i8 == 0) && (i9 == 0) && (i10 == 0))
        {
          if (hasSwappedOutPss) {
            i13 = i12;
          } else {
            i13 = i11;
          }
          if (i13 == 0) {
            continue;
          }
        }
        dumpMemoryInfo(paramProtoOutputStream, 2246267895813L, Debug.MemoryInfo.getOtherLabel(k), i5, i6, i7, i8, i9, i10, hasSwappedOutPss, i11, i12);
        i2 -= i5;
        i4 -= i6;
        m -= i7;
        i -= i8;
        j -= i9;
        n -= i10;
        i3 -= i11;
        i1 -= i12;
      }
      paramBoolean2 = hasSwappedOutPss;
      k = 17;
      dumpMemoryInfo(paramProtoOutputStream, 1146756268038L, "Unknown", i2, i4, m, i, j, n, paramBoolean2, i3, i1);
      l = paramProtoOutputStream.start(1146756268039L);
      dumpMemoryInfo(paramProtoOutputStream, 1146756268033L, "TOTAL", paramMemoryInfo.getTotalPss(), paramMemoryInfo.getTotalSwappablePss(), paramMemoryInfo.getTotalSharedDirty(), paramMemoryInfo.getTotalPrivateDirty(), paramMemoryInfo.getTotalSharedClean(), paramMemoryInfo.getTotalPrivateClean(), hasSwappedOutPss, paramMemoryInfo.getTotalSwappedOut(), paramMemoryInfo.getTotalSwappedOutPss());
      paramProtoOutputStream.write(1120986464258L, paramLong1 + paramLong4);
      paramProtoOutputStream.write(1120986464259L, paramLong2 + paramLong5);
      paramProtoOutputStream.write(1120986464260L, paramLong3 + paramLong6);
      paramLong1 = l;
      paramProtoOutputStream.end(paramLong1);
      if (paramBoolean1) {
        for (i3 = k; i3 < 31; i3++)
        {
          m = localMemoryInfo.getOtherPss(i3);
          i4 = localMemoryInfo.getOtherSwappablePss(i3);
          i = localMemoryInfo.getOtherSharedDirty(i3);
          i2 = localMemoryInfo.getOtherPrivateDirty(i3);
          j = localMemoryInfo.getOtherSharedClean(i3);
          i13 = localMemoryInfo.getOtherPrivateClean(i3);
          k = localMemoryInfo.getOtherSwappedOut(i3);
          n = localMemoryInfo.getOtherSwappedOutPss(i3);
          if ((m == 0) && (i == 0) && (i2 == 0) && (j == 0) && (i13 == 0))
          {
            if (hasSwappedOutPss) {
              i1 = n;
            } else {
              i1 = k;
            }
            if (i1 == 0) {
              continue;
            }
          }
          dumpMemoryInfo(paramProtoOutputStream, 2246267895816L, Debug.MemoryInfo.getOtherLabel(i3), m, i4, i, i2, j, i13, hasSwappedOutPss, k, n);
        }
      }
    }
    paramLong1 = paramProtoOutputStream.start(1146756268041L);
    paramProtoOutputStream.write(1120986464257L, paramMemoryInfo.getSummaryJavaHeap());
    paramProtoOutputStream.write(1120986464258L, paramMemoryInfo.getSummaryNativeHeap());
    paramProtoOutputStream.write(1120986464259L, paramMemoryInfo.getSummaryCode());
    paramProtoOutputStream.write(1120986464260L, paramMemoryInfo.getSummaryStack());
    paramProtoOutputStream.write(1120986464261L, paramMemoryInfo.getSummaryGraphics());
    paramProtoOutputStream.write(1120986464262L, paramMemoryInfo.getSummaryPrivateOther());
    paramProtoOutputStream.write(1120986464263L, paramMemoryInfo.getSummarySystem());
    if (hasSwappedOutPss) {
      paramProtoOutputStream.write(1120986464264L, paramMemoryInfo.getSummaryTotalSwapPss());
    } else {
      paramProtoOutputStream.write(1120986464264L, paramMemoryInfo.getSummaryTotalSwap());
    }
    paramProtoOutputStream.end(paramLong1);
  }
  
  public static void dumpMemInfoTable(PrintWriter paramPrintWriter, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, int paramInt, String paramString, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5, long paramLong6)
  {
    int i = 0;
    if (paramBoolean1)
    {
      paramPrintWriter.print(4);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramInt);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramString);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramLong1);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramLong4);
      paramPrintWriter.print(',');
      paramPrintWriter.print("N/A,");
      paramPrintWriter.print(paramLong1 + paramLong4);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramLong2);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramLong5);
      paramPrintWriter.print(',');
      paramPrintWriter.print("N/A,");
      paramPrintWriter.print(paramLong2 + paramLong5);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramLong3);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramLong6);
      paramPrintWriter.print(',');
      paramPrintWriter.print("N/A,");
      paramPrintWriter.print(paramLong3 + paramLong6);
      paramPrintWriter.print(',');
      paramPrintWriter.print(nativePss);
      paramPrintWriter.print(',');
      paramPrintWriter.print(dalvikPss);
      paramPrintWriter.print(',');
      paramPrintWriter.print(otherPss);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramMemoryInfo.getTotalPss());
      paramPrintWriter.print(',');
      paramPrintWriter.print(nativeSwappablePss);
      paramPrintWriter.print(',');
      paramPrintWriter.print(dalvikSwappablePss);
      paramPrintWriter.print(',');
      paramPrintWriter.print(otherSwappablePss);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramMemoryInfo.getTotalSwappablePss());
      paramPrintWriter.print(',');
      paramPrintWriter.print(nativeSharedDirty);
      paramPrintWriter.print(',');
      paramPrintWriter.print(dalvikSharedDirty);
      paramPrintWriter.print(',');
      paramPrintWriter.print(otherSharedDirty);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramMemoryInfo.getTotalSharedDirty());
      paramPrintWriter.print(',');
      paramPrintWriter.print(nativeSharedClean);
      paramPrintWriter.print(',');
      paramPrintWriter.print(dalvikSharedClean);
      paramPrintWriter.print(',');
      paramPrintWriter.print(otherSharedClean);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramMemoryInfo.getTotalSharedClean());
      paramPrintWriter.print(',');
      paramPrintWriter.print(nativePrivateDirty);
      paramPrintWriter.print(',');
      paramPrintWriter.print(dalvikPrivateDirty);
      paramPrintWriter.print(',');
      paramPrintWriter.print(otherPrivateDirty);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramMemoryInfo.getTotalPrivateDirty());
      paramPrintWriter.print(',');
      paramPrintWriter.print(nativePrivateClean);
      paramPrintWriter.print(',');
      paramPrintWriter.print(dalvikPrivateClean);
      paramPrintWriter.print(',');
      paramPrintWriter.print(otherPrivateClean);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramMemoryInfo.getTotalPrivateClean());
      paramPrintWriter.print(',');
      paramPrintWriter.print(nativeSwappedOut);
      paramPrintWriter.print(',');
      paramPrintWriter.print(dalvikSwappedOut);
      paramPrintWriter.print(',');
      paramPrintWriter.print(otherSwappedOut);
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramMemoryInfo.getTotalSwappedOut());
      paramPrintWriter.print(',');
      if (hasSwappedOutPss)
      {
        paramPrintWriter.print(nativeSwappedOutPss);
        paramPrintWriter.print(',');
        paramPrintWriter.print(dalvikSwappedOutPss);
        paramPrintWriter.print(',');
        paramPrintWriter.print(otherSwappedOutPss);
        paramPrintWriter.print(',');
        paramPrintWriter.print(paramMemoryInfo.getTotalSwappedOutPss());
        paramPrintWriter.print(',');
      }
      else
      {
        paramPrintWriter.print("N/A,");
        paramPrintWriter.print("N/A,");
        paramPrintWriter.print("N/A,");
        paramPrintWriter.print("N/A,");
      }
      for (paramInt = i; paramInt < 17; paramInt++)
      {
        paramPrintWriter.print(Debug.MemoryInfo.getOtherLabel(paramInt));
        paramPrintWriter.print(',');
        paramPrintWriter.print(paramMemoryInfo.getOtherPss(paramInt));
        paramPrintWriter.print(',');
        paramPrintWriter.print(paramMemoryInfo.getOtherSwappablePss(paramInt));
        paramPrintWriter.print(',');
        paramPrintWriter.print(paramMemoryInfo.getOtherSharedDirty(paramInt));
        paramPrintWriter.print(',');
        paramPrintWriter.print(paramMemoryInfo.getOtherSharedClean(paramInt));
        paramPrintWriter.print(',');
        paramPrintWriter.print(paramMemoryInfo.getOtherPrivateDirty(paramInt));
        paramPrintWriter.print(',');
        paramPrintWriter.print(paramMemoryInfo.getOtherPrivateClean(paramInt));
        paramPrintWriter.print(',');
        paramPrintWriter.print(paramMemoryInfo.getOtherSwappedOut(paramInt));
        paramPrintWriter.print(',');
        if (hasSwappedOutPss)
        {
          paramPrintWriter.print(paramMemoryInfo.getOtherSwappedOutPss(paramInt));
          paramPrintWriter.print(',');
        }
        else
        {
          paramPrintWriter.print("N/A,");
        }
      }
      return;
    }
    if (!paramBoolean4)
    {
      int k;
      int m;
      if (paramBoolean2)
      {
        if (hasSwappedOutPss) {
          paramString = "SwapPss";
        } else {
          paramString = "Swap";
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "", "Pss", "Pss", "Shared", "Private", "Shared", "Private", paramString, "Heap", "Heap", "Heap" });
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "", "Total", "Clean", "Dirty", "Dirty", "Clean", "Clean", "Dirty", "Size", "Alloc", "Free" });
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "", "------", "------", "------", "------", "------", "------", "------", "------", "------", "------" });
        j = nativePss;
        k = nativeSwappablePss;
        m = nativeSharedDirty;
        i = nativePrivateDirty;
        n = nativeSharedClean;
        i1 = nativePrivateClean;
        if (hasSwappedOutPss) {
          paramInt = nativeSwappedOutPss;
        } else {
          paramInt = nativeSwappedOut;
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "Native Heap", Integer.valueOf(j), Integer.valueOf(k), Integer.valueOf(m), Integer.valueOf(i), Integer.valueOf(n), Integer.valueOf(i1), Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) });
        i1 = dalvikPss;
        m = dalvikSwappablePss;
        i = dalvikSharedDirty;
        n = dalvikPrivateDirty;
        k = dalvikSharedClean;
        j = dalvikPrivateClean;
        if (hasSwappedOutPss) {
          paramInt = dalvikSwappedOutPss;
        } else {
          paramInt = dalvikSwappedOut;
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "Dalvik Heap", Integer.valueOf(i1), Integer.valueOf(m), Integer.valueOf(i), Integer.valueOf(n), Integer.valueOf(k), Integer.valueOf(j), Integer.valueOf(paramInt), Long.valueOf(paramLong4), Long.valueOf(paramLong5), Long.valueOf(paramLong6) });
      }
      else
      {
        if (hasSwappedOutPss) {
          paramString = "SwapPss";
        } else {
          paramString = "Swap";
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "", "Pss", "Private", "Private", paramString, "Heap", "Heap", "Heap" });
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "", "Total", "Dirty", "Clean", "Dirty", "Size", "Alloc", "Free" });
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "", "------", "------", "------", "------", "------", "------", "------", "------" });
        i1 = nativePss;
        m = nativePrivateDirty;
        i = nativePrivateClean;
        if (hasSwappedOutPss) {
          paramInt = nativeSwappedOutPss;
        } else {
          paramInt = nativeSwappedOut;
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "Native Heap", Integer.valueOf(i1), Integer.valueOf(m), Integer.valueOf(i), Integer.valueOf(paramInt), Long.valueOf(paramLong1), Long.valueOf(paramLong2), Long.valueOf(paramLong3) });
        i1 = dalvikPss;
        i = dalvikPrivateDirty;
        m = dalvikPrivateClean;
        if (hasSwappedOutPss) {
          paramInt = dalvikSwappedOutPss;
        } else {
          paramInt = dalvikSwappedOut;
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "Dalvik Heap", Integer.valueOf(i1), Integer.valueOf(i), Integer.valueOf(m), Integer.valueOf(paramInt), Long.valueOf(paramLong4), Long.valueOf(paramLong5), Long.valueOf(paramLong6) });
      }
      int i1 = otherPss;
      int i2 = otherSwappablePss;
      int j = otherSharedDirty;
      int i3 = otherPrivateDirty;
      int i4 = otherSharedClean;
      int n = otherPrivateClean;
      i = otherSwappedOut;
      paramInt = otherSwappedOutPss;
      for (int i5 = 0; i5 < 17; i5++)
      {
        int i6 = paramMemoryInfo.getOtherPss(i5);
        int i7 = paramMemoryInfo.getOtherSwappablePss(i5);
        int i8 = paramMemoryInfo.getOtherSharedDirty(i5);
        int i9 = paramMemoryInfo.getOtherPrivateDirty(i5);
        int i10 = paramMemoryInfo.getOtherSharedClean(i5);
        int i11 = paramMemoryInfo.getOtherPrivateClean(i5);
        k = paramMemoryInfo.getOtherSwappedOut(i5);
        m = paramMemoryInfo.getOtherSwappedOutPss(i5);
        int i12;
        if ((i6 == 0) && (i8 == 0) && (i9 == 0) && (i10 == 0) && (i11 == 0))
        {
          if (hasSwappedOutPss) {
            i12 = m;
          } else {
            i12 = k;
          }
          if (i12 == 0) {
            continue;
          }
        }
        if (paramBoolean2)
        {
          paramString = Debug.MemoryInfo.getOtherLabel(i5);
          if (hasSwappedOutPss) {
            i12 = m;
          } else {
            i12 = k;
          }
          printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { paramString, Integer.valueOf(i6), Integer.valueOf(i7), Integer.valueOf(i8), Integer.valueOf(i9), Integer.valueOf(i10), Integer.valueOf(i11), Integer.valueOf(i12), "", "", "" });
        }
        else
        {
          paramString = Debug.MemoryInfo.getOtherLabel(i5);
          if (hasSwappedOutPss) {
            i12 = m;
          } else {
            i12 = k;
          }
          printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { paramString, Integer.valueOf(i6), Integer.valueOf(i9), Integer.valueOf(i11), Integer.valueOf(i12), "", "", "" });
        }
        i1 -= i6;
        i2 -= i7;
        j -= i8;
        i3 -= i9;
        i4 -= i10;
        n -= i11;
        i -= k;
        paramInt -= m;
      }
      if (paramBoolean2)
      {
        if (!hasSwappedOutPss) {
          paramInt = i;
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "Unknown", Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(j), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(n), Integer.valueOf(paramInt), "", "", "" });
        i = paramMemoryInfo.getTotalPss();
        k = paramMemoryInfo.getTotalSwappablePss();
        j = paramMemoryInfo.getTotalSharedDirty();
        n = paramMemoryInfo.getTotalPrivateDirty();
        m = paramMemoryInfo.getTotalSharedClean();
        i3 = paramMemoryInfo.getTotalPrivateClean();
        if (hasSwappedOutPss) {
          paramInt = paramMemoryInfo.getTotalSwappedOutPss();
        } else {
          paramInt = paramMemoryInfo.getTotalSwappedOut();
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "TOTAL", Integer.valueOf(i), Integer.valueOf(k), Integer.valueOf(j), Integer.valueOf(n), Integer.valueOf(m), Integer.valueOf(i3), Integer.valueOf(paramInt), Long.valueOf(paramLong1 + paramLong4), Long.valueOf(paramLong2 + paramLong5), Long.valueOf(paramLong3 + paramLong6) });
      }
      else
      {
        if (!hasSwappedOutPss) {
          paramInt = i;
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "Unknown", Integer.valueOf(i1), Integer.valueOf(i3), Integer.valueOf(n), Integer.valueOf(paramInt), "", "", "" });
        k = paramMemoryInfo.getTotalPss();
        i = paramMemoryInfo.getTotalPrivateDirty();
        m = paramMemoryInfo.getTotalPrivateClean();
        if (hasSwappedOutPss) {
          paramInt = paramMemoryInfo.getTotalSwappedOutPss();
        } else {
          paramInt = paramMemoryInfo.getTotalSwappedOut();
        }
        printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { "TOTAL", Integer.valueOf(k), Integer.valueOf(i), Integer.valueOf(m), Integer.valueOf(paramInt), Long.valueOf(paramLong1 + paramLong4), Long.valueOf(paramLong2 + paramLong5), Long.valueOf(paramLong3 + paramLong6) });
      }
      if (paramBoolean3)
      {
        paramPrintWriter.println(" ");
        paramPrintWriter.println(" Dalvik Details");
        for (m = 17; m < 31; m++)
        {
          n = paramMemoryInfo.getOtherPss(m);
          i5 = paramMemoryInfo.getOtherSwappablePss(m);
          i2 = paramMemoryInfo.getOtherSharedDirty(m);
          j = paramMemoryInfo.getOtherPrivateDirty(m);
          i4 = paramMemoryInfo.getOtherSharedClean(m);
          i3 = paramMemoryInfo.getOtherPrivateClean(m);
          paramInt = paramMemoryInfo.getOtherSwappedOut(m);
          i = paramMemoryInfo.getOtherSwappedOutPss(m);
          if ((n == 0) && (i2 == 0) && (j == 0) && (i4 == 0) && (i3 == 0))
          {
            if (hasSwappedOutPss) {
              k = i;
            } else {
              k = paramInt;
            }
            if (k == 0) {
              continue;
            }
          }
          if (paramBoolean2)
          {
            paramString = Debug.MemoryInfo.getOtherLabel(m);
            if (hasSwappedOutPss) {
              paramInt = i;
            }
            printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s", new Object[] { paramString, Integer.valueOf(n), Integer.valueOf(i5), Integer.valueOf(i2), Integer.valueOf(j), Integer.valueOf(i4), Integer.valueOf(i3), Integer.valueOf(paramInt), "", "", "" });
          }
          else
          {
            paramString = Debug.MemoryInfo.getOtherLabel(m);
            if (hasSwappedOutPss) {
              paramInt = i;
            }
            printRow(paramPrintWriter, "%13s %8s %8s %8s %8s %8s %8s %8s", new Object[] { paramString, Integer.valueOf(n), Integer.valueOf(j), Integer.valueOf(i3), Integer.valueOf(paramInt), "", "", "" });
          }
        }
      }
    }
    paramPrintWriter.println(" ");
    paramPrintWriter.println(" App Summary");
    printRow(paramPrintWriter, "%21s %8s", new Object[] { "", "Pss(KB)" });
    printRow(paramPrintWriter, "%21s %8s", new Object[] { "", "------" });
    printRow(paramPrintWriter, "%21s %8d", new Object[] { "Java Heap:", Integer.valueOf(paramMemoryInfo.getSummaryJavaHeap()) });
    printRow(paramPrintWriter, "%21s %8d", new Object[] { "Native Heap:", Integer.valueOf(paramMemoryInfo.getSummaryNativeHeap()) });
    printRow(paramPrintWriter, "%21s %8d", new Object[] { "Code:", Integer.valueOf(paramMemoryInfo.getSummaryCode()) });
    printRow(paramPrintWriter, "%21s %8d", new Object[] { "Stack:", Integer.valueOf(paramMemoryInfo.getSummaryStack()) });
    printRow(paramPrintWriter, "%21s %8d", new Object[] { "Graphics:", Integer.valueOf(paramMemoryInfo.getSummaryGraphics()) });
    printRow(paramPrintWriter, "%21s %8d", new Object[] { "Private Other:", Integer.valueOf(paramMemoryInfo.getSummaryPrivateOther()) });
    printRow(paramPrintWriter, "%21s %8d", new Object[] { "System:", Integer.valueOf(paramMemoryInfo.getSummarySystem()) });
    paramPrintWriter.println(" ");
    if (hasSwappedOutPss) {
      printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "TOTAL:", Integer.valueOf(paramMemoryInfo.getSummaryTotalPss()), "TOTAL SWAP PSS:", Integer.valueOf(paramMemoryInfo.getSummaryTotalSwapPss()) });
    } else {
      printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "TOTAL:", Integer.valueOf(paramMemoryInfo.getSummaryTotalPss()), "TOTAL SWAP (KB):", Integer.valueOf(paramMemoryInfo.getSummaryTotalSwap()) });
    }
  }
  
  private static void dumpMemoryInfo(ProtoOutputStream paramProtoOutputStream, long paramLong, String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, int paramInt7, int paramInt8)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1138166333441L, paramString);
    paramProtoOutputStream.write(1120986464258L, paramInt1);
    paramProtoOutputStream.write(1120986464259L, paramInt2);
    paramProtoOutputStream.write(1120986464260L, paramInt3);
    paramProtoOutputStream.write(1120986464261L, paramInt4);
    paramProtoOutputStream.write(1120986464262L, paramInt5);
    paramProtoOutputStream.write(1120986464263L, paramInt6);
    if (paramBoolean) {
      paramProtoOutputStream.write(1120986464265L, paramInt8);
    } else {
      paramProtoOutputStream.write(1120986464264L, paramInt7);
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  static void freeTextLayoutCachesIfNeeded(int paramInt)
  {
    if (paramInt != 0)
    {
      if ((paramInt & 0x4) != 0) {
        paramInt = 1;
      } else {
        paramInt = 0;
      }
      if (paramInt != 0) {
        Canvas.freeTextLayoutCaches();
      }
    }
  }
  
  private Object getGetProviderLock(String paramString, int paramInt)
  {
    ProviderKey localProviderKey = new ProviderKey(paramString, paramInt);
    synchronized (mGetProviderLocks)
    {
      Object localObject = mGetProviderLocks.get(localProviderKey);
      paramString = localObject;
      if (localObject == null)
      {
        paramString = localProviderKey;
        mGetProviderLocks.put(localProviderKey, paramString);
      }
      return paramString;
    }
  }
  
  private String getInstrumentationLibrary(ApplicationInfo paramApplicationInfo, InstrumentationInfo paramInstrumentationInfo)
  {
    if ((primaryCpuAbi != null) && (secondaryCpuAbi != null) && (secondaryCpuAbi.equals(secondaryCpuAbi)))
    {
      paramApplicationInfo = VMRuntime.getInstructionSet(secondaryCpuAbi);
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("ro.dalvik.vm.isa.");
      ((StringBuilder)localObject).append(paramApplicationInfo);
      localObject = SystemProperties.get(((StringBuilder)localObject).toString());
      if (!((String)localObject).isEmpty()) {
        paramApplicationInfo = (ApplicationInfo)localObject;
      }
      if (VMRuntime.getRuntime().vmInstructionSet().equals(paramApplicationInfo)) {
        return secondaryNativeLibraryDir;
      }
    }
    return nativeLibraryDir;
  }
  
  public static Intent getIntentBeingBroadcast()
  {
    return (Intent)sCurrentBroadcastIntent.get();
  }
  
  private LoadedApk getPackageInfo(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo, ClassLoader paramClassLoader, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    int i;
    if (UserHandle.myUserId() != UserHandle.getUserId(uid)) {
      i = 1;
    } else {
      i = 0;
    }
    ResourcesManager localResourcesManager = mResourcesManager;
    if (i != 0) {
      localObject1 = null;
    }
    for (;;)
    {
      break label86;
      if (paramBoolean2) {
        try
        {
          localObject1 = (WeakReference)mPackages.get(packageName);
        }
        finally
        {
          break label315;
        }
      }
    }
    Object localObject1 = (WeakReference)mResourcePackages.get(packageName);
    label86:
    if (localObject1 != null) {
      localObject1 = (LoadedApk)((WeakReference)localObject1).get();
    } else {
      localObject1 = null;
    }
    Object localObject2 = localObject1;
    if (localObject2 != null)
    {
      localObject1 = localObject2;
      if (mResources != null)
      {
        localObject1 = localObject2;
        if (mResources.getAssets().isUpToDate()) {}
      }
    }
    else
    {
      localObject1 = new android/app/LoadedApk;
      boolean bool;
      if ((paramBoolean2) && ((flags & 0x4) != 0)) {
        bool = true;
      } else {
        bool = false;
      }
      ((LoadedApk)localObject1).<init>(this, paramApplicationInfo, paramCompatibilityInfo, paramClassLoader, paramBoolean1, bool, paramBoolean3);
      paramCompatibilityInfo = (CompatibilityInfo)localObject1;
      if ((mSystemThread) && ("android".equals(packageName))) {
        paramCompatibilityInfo.installSystemApplicationInfo(paramApplicationInfo, getSystemContextmPackageInfo.getClassLoader());
      }
      if (i != 0)
      {
        localObject1 = paramCompatibilityInfo;
      }
      else if (paramBoolean2)
      {
        paramClassLoader = mPackages;
        paramApplicationInfo = packageName;
        localObject1 = new java/lang/ref/WeakReference;
        ((WeakReference)localObject1).<init>(paramCompatibilityInfo);
        paramClassLoader.put(paramApplicationInfo, localObject1);
        localObject1 = paramCompatibilityInfo;
      }
      else
      {
        paramClassLoader = mResourcePackages;
        localObject1 = packageName;
        paramApplicationInfo = new java/lang/ref/WeakReference;
        paramApplicationInfo.<init>(paramCompatibilityInfo);
        paramClassLoader.put(localObject1, paramApplicationInfo);
        localObject1 = paramCompatibilityInfo;
      }
    }
    return localObject1;
    label315:
    throw paramApplicationInfo;
  }
  
  public static IPackageManager getPackageManager()
  {
    if (sPackageManager != null) {
      return sPackageManager;
    }
    sPackageManager = IPackageManager.Stub.asInterface(ServiceManager.getService("package"));
    return sPackageManager;
  }
  
  static void handleAttachAgent(String paramString, LoadedApk paramLoadedApk)
  {
    if (paramLoadedApk != null) {
      paramLoadedApk = paramLoadedApk.getClassLoader();
    } else {
      paramLoadedApk = null;
    }
    if (attemptAttachAgent(paramString, paramLoadedApk)) {
      return;
    }
    if (paramLoadedApk != null) {
      attemptAttachAgent(paramString, null);
    }
  }
  
  private void handleBindApplication(AppBindData paramAppBindData)
  {
    long l = SystemClock.uptimeMillis();
    ApplicationInfo localApplicationInfo = null;
    VMRuntime.registerSensitiveThread();
    if (trackAllocation) {
      DdmVmInternal.enableRecentAllocations(true);
    }
    Process.setStartTimes(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis());
    mBoundApplication = paramAppBindData;
    mConfiguration = new Configuration(config);
    mCompatConfiguration = new Configuration(config);
    mProfiler = new Profiler();
    ??? = null;
    Object localObject3 = ???;
    if (initProfilerInfo != null)
    {
      mProfiler.profileFile = initProfilerInfo.profileFile;
      mProfiler.profileFd = initProfilerInfo.profileFd;
      mProfiler.samplingInterval = initProfilerInfo.samplingInterval;
      mProfiler.autoStopProfiler = initProfilerInfo.autoStopProfiler;
      mProfiler.streamingOutput = initProfilerInfo.streamingOutput;
      localObject3 = ???;
      if (initProfilerInfo.attachAgentDuringBind) {
        localObject3 = initProfilerInfo.agent;
      }
    }
    Process.setArgV0(processName);
    DdmHandleAppName.setAppName(processName, UserHandle.myUserId());
    VMRuntime.setProcessPackageName(appInfo.packageName);
    if (mProfiler.profileFd != null) {
      mProfiler.startProfiling();
    }
    if (appInfo.targetSdkVersion <= 12) {
      AsyncTask.setDefaultExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    Message.updateCheckRecycle(appInfo.targetSdkVersion);
    android.graphics.ImageDecoder.sApiLevel = appInfo.targetSdkVersion;
    TimeZone.setDefault(null);
    LocaleList.setDefault(config.getLocales());
    synchronized (mResourcesManager)
    {
      mResourcesManager.applyConfigurationToResourcesLocked(config, compatInfo);
      mCurDefaultDisplayDpi = config.densityDpi;
      applyCompatConfiguration(mCurDefaultDisplayDpi);
      info = getPackageInfoNoCheck(appInfo, compatInfo);
      if (localObject3 != null) {
        handleAttachAgent((String)localObject3, info);
      }
      int i;
      if ((appInfo.flags & 0x2000) == 0)
      {
        mDensityCompatMode = true;
        Bitmap.setDefaultDensity(160);
      }
      else
      {
        i = appInfo.getOverrideDensity();
        if (i != 0)
        {
          localObject3 = new StringBuilder();
          ((StringBuilder)localObject3).append("override app density from ");
          ((StringBuilder)localObject3).append(DisplayMetrics.DENSITY_DEVICE);
          ((StringBuilder)localObject3).append(" to ");
          ((StringBuilder)localObject3).append(i);
          Log.d("ActivityThread", ((StringBuilder)localObject3).toString());
          mDensityCompatMode = true;
          Bitmap.setDefaultDensity(i);
        }
      }
      updateDefaultDensity();
      ??? = mCoreSettings.getString("time_12_24");
      localObject3 = null;
      if (??? != null) {
        if ("24".equals(???)) {
          localObject3 = Boolean.TRUE;
        } else {
          localObject3 = Boolean.FALSE;
        }
      }
      DateFormat.set24HourTimePref((Boolean)localObject3);
      boolean bool1;
      if (mCoreSettings.getInt("debug_view_attributes", 0) != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      View.mDebugViewAttributes = bool1;
      StrictMode.initThreadDefaults(appInfo);
      StrictMode.initVmDefaults(appInfo);
      try
      {
        localObject3 = Build.class.getDeclaredField("SERIAL");
        ((Field)localObject3).setAccessible(true);
        ((Field)localObject3).set(Build.class, buildSerial);
      }
      catch (NoSuchFieldException|IllegalAccessException localNoSuchFieldException) {}
      if (debugMode != 0)
      {
        Debug.changeDebugPort(8100);
        if (debugMode == 2)
        {
          localObject4 = new StringBuilder();
          ((StringBuilder)localObject4).append("Application ");
          ((StringBuilder)localObject4).append(info.getPackageName());
          ((StringBuilder)localObject4).append(" is waiting for the debugger on port 8100...");
          Slog.w("ActivityThread", ((StringBuilder)localObject4).toString());
          localObject4 = ActivityManager.getService();
          try
          {
            ((IActivityManager)localObject4).showWaitingForDebugger(mAppThread, true);
            Debug.waitForDebugger();
            try
            {
              ((IActivityManager)localObject4).showWaitingForDebugger(mAppThread, false);
            }
            catch (RemoteException paramAppBindData)
            {
              throw paramAppBindData.rethrowFromSystemServer();
            }
            localObject4 = new StringBuilder();
          }
          catch (RemoteException paramAppBindData)
          {
            throw paramAppBindData.rethrowFromSystemServer();
          }
        }
        else
        {
          ((StringBuilder)localObject4).append("Application ");
          ((StringBuilder)localObject4).append(info.getPackageName());
          ((StringBuilder)localObject4).append(" can be debugged on port 8100...");
          Slog.w("ActivityThread", ((StringBuilder)localObject4).toString());
        }
      }
      if ((appInfo.flags & 0x2) != 0) {
        bool1 = true;
      } else {
        bool1 = false;
      }
      Trace.setAppTracingAllowed(bool1);
      boolean bool2;
      if ((!bool1) && (!Build.IS_DEBUGGABLE)) {
        bool2 = false;
      } else {
        bool2 = true;
      }
      ThreadedRenderer.setDebuggingEnabled(bool2);
      if ((bool1) && (enableBinderTracking)) {
        Binder.enableTracing();
      }
      Trace.traceBegin(64L, "Setup proxies");
      Object localObject4 = ServiceManager.getService("connectivity");
      if (localObject4 != null)
      {
        localObject4 = IConnectivityManager.Stub.asInterface((IBinder)localObject4);
        try
        {
          Proxy.setHttpProxySystemProperty(((IConnectivityManager)localObject4).getProxyForNetwork(null));
        }
        catch (RemoteException paramAppBindData)
        {
          Trace.traceEnd(64L);
          throw paramAppBindData.rethrowFromSystemServer();
        }
      }
      Trace.traceEnd(64L);
      if (instrumentationName != null) {
        try
        {
          localObject4 = new android/app/ApplicationPackageManager;
          ((ApplicationPackageManager)localObject4).<init>(null, getPackageManager());
          ??? = ((ApplicationPackageManager)localObject4).getInstrumentationInfo(instrumentationName, 0);
          if ((!Objects.equals(appInfo.primaryCpuAbi, primaryCpuAbi)) || (!Objects.equals(appInfo.secondaryCpuAbi, secondaryCpuAbi)))
          {
            localObject4 = new StringBuilder();
            ((StringBuilder)localObject4).append("Package uses different ABI(s) than its instrumentation: package[");
            ((StringBuilder)localObject4).append(appInfo.packageName);
            ((StringBuilder)localObject4).append("]: ");
            ((StringBuilder)localObject4).append(appInfo.primaryCpuAbi);
            ((StringBuilder)localObject4).append(", ");
            ((StringBuilder)localObject4).append(appInfo.secondaryCpuAbi);
            ((StringBuilder)localObject4).append(" instrumentation[");
            ((StringBuilder)localObject4).append(packageName);
            ((StringBuilder)localObject4).append("]: ");
            ((StringBuilder)localObject4).append(primaryCpuAbi);
            ((StringBuilder)localObject4).append(", ");
            ((StringBuilder)localObject4).append(secondaryCpuAbi);
            Slog.w("ActivityThread", ((StringBuilder)localObject4).toString());
          }
          mInstrumentationPackageName = packageName;
          mInstrumentationAppDir = sourceDir;
          mInstrumentationSplitAppDirs = splitSourceDirs;
          mInstrumentationLibDir = getInstrumentationLibrary(appInfo, (InstrumentationInfo)???);
          mInstrumentedAppDir = info.getAppDir();
          mInstrumentedSplitAppDirs = info.getSplitAppDirs();
          mInstrumentedLibDir = info.getLibDir();
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          localObject5 = new StringBuilder();
          ((StringBuilder)localObject5).append("Unable to find instrumentation info for: ");
          ((StringBuilder)localObject5).append(instrumentationName);
          throw new RuntimeException(((StringBuilder)localObject5).toString());
        }
      }
      ??? = null;
      Object localObject9 = ContextImpl.createAppContext(this, info);
      updateLocaleListFromAppContext((Context)localObject9, mResourcesManager.getConfiguration().getLocales());
      Object localObject5 = localApplicationInfo;
      if (!Process.isIsolated()) {
        localObject5 = new BoostFramework((Context)localObject9);
      }
      if (!Process.isIsolated()) {
        i = StrictMode.allowThreadDiskWritesMask();
      }
      try
      {
        setupGraphicsSupport((Context)localObject9);
        StrictMode.setThreadPolicyMask(i);
      }
      finally
      {
        StrictMode.setThreadPolicyMask(i);
      }
      if (SystemProperties.getBoolean("dalvik.vm.usejitprofiles", false)) {
        BaseDexClassLoader.setReporter(DexLoadReporter.getInstance());
      }
      Trace.traceBegin(64L, "NetworkSecurityConfigProvider.install");
      NetworkSecurityConfigProvider.install((Context)localObject9);
      Trace.traceEnd(64L);
      Object localObject10;
      if (??? != null)
      {
        try
        {
          localApplicationInfo = getPackageManager().getApplicationInfo(packageName, 0, UserHandle.myUserId());
        }
        catch (RemoteException localRemoteException)
        {
          localObject1 = null;
        }
        localObject10 = localObject1;
        if (localObject1 == null) {
          localObject10 = new ApplicationInfo();
        }
        ((InstrumentationInfo)???).copyTo((ApplicationInfo)localObject10);
        ((ApplicationInfo)localObject10).initForUser(UserHandle.myUserId());
        localObject1 = ContextImpl.createAppContext(this, getPackageInfo((ApplicationInfo)localObject10, compatInfo, ((ContextImpl)localObject9).getClassLoader(), false, true, false));
        try
        {
          mInstrumentation = ((Instrumentation)((ContextImpl)localObject1).getClassLoader().loadClass(instrumentationName.getClassName()).newInstance());
          localObject10 = new ComponentName(packageName, name);
          mInstrumentation.init(this, (Context)localObject1, (Context)localObject9, (ComponentName)localObject10, instrumentationWatcher, instrumentationUiAutomationConnection);
          if ((mProfiler.profileFile != null) && (!handleProfiling) && (mProfiler.profileFd == null))
          {
            mProfiler.handlingProfiling = true;
            localObject1 = new File(mProfiler.profileFile);
            ((File)localObject1).getParentFile().mkdirs();
            Debug.startMethodTracing(((File)localObject1).toString(), 8388608);
          }
        }
        catch (Exception localException1)
        {
          localObject1 = new StringBuilder();
          ((StringBuilder)localObject1).append("Unable to instantiate instrumentation ");
          ((StringBuilder)localObject1).append(instrumentationName);
          ((StringBuilder)localObject1).append(": ");
          ((StringBuilder)localObject1).append(localException1.toString());
          throw new RuntimeException(((StringBuilder)localObject1).toString(), localException1);
        }
      }
      mInstrumentation = new Instrumentation();
      mInstrumentation.basicInit(this);
      if ((appInfo.flags & 0x100000) != 0) {
        VMRuntime.getRuntime().clearGrowthLimit();
      } else {
        VMRuntime.getRuntime().clampGrowthLimit();
      }
      ??? = StrictMode.allowThreadDiskWrites();
      Object localObject1 = StrictMode.getThreadPolicy();
      try
      {
        localObject10 = info.makeApplication(restrictedBackupMode, null);
        ((Application)localObject10).setAutofillCompatibilityEnabled(autofillCompatibilityEnabled);
        mInitialApplication = ((Application)localObject10);
        bool1 = restrictedBackupMode;
        if (!bool1) {
          try
          {
            if (!ArrayUtils.isEmpty(providers))
            {
              installContentProviders((Context)localObject10, providers);
              mH.sendEmptyMessageDelayed(132, 10000L);
            }
          }
          finally
          {
            break label2141;
          }
        }
        try
        {
          mInstrumentation.onCreate(instrumentationArgs);
          try
          {
            mInstrumentation.callApplicationOnCreate((Application)localObject10);
          }
          catch (Exception localException3)
          {
            bool1 = mInstrumentation.onException(localObject10, localException3);
            if (!bool1) {
              break label1990;
            }
          }
          if ((appInfo.targetSdkVersion < 27) || (StrictMode.getThreadPolicy().equals(localObject1))) {
            StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)???);
          }
          FontsContract.setApplicationContextForResources((Context)localObject9);
          if (!Process.isIsolated()) {
            try
            {
              localObject1 = getPackageManager().getApplicationInfo(appInfo.packageName, 128, UserHandle.myUserId());
              if (metaData != null)
              {
                i = metaData.getInt("preloaded_fonts", 0);
                if (i != 0) {
                  info.getResources().preloadFonts(i);
                }
              }
            }
            catch (RemoteException paramAppBindData)
            {
              throw paramAppBindData.rethrowFromSystemServer();
            }
          }
          i = (int)(SystemClock.uptimeMillis() - l);
          paramAppBindData = null;
          if (localObject9 != null) {
            paramAppBindData = ((ContextImpl)localObject9).getPackageName();
          }
          if ((localObject6 != null) && (!Process.isIsolated()) && (paramAppBindData != null)) {
            localObject6.perfUXEngine_events(2, 0, paramAppBindData, i);
          }
          return;
        }
        catch (Exception localException2)
        {
          try
          {
            label1990:
            localRuntimeException = new java/lang/RuntimeException;
            localObject9 = new java/lang/StringBuilder;
            ((StringBuilder)localObject9).<init>();
            ((StringBuilder)localObject9).append("Unable to create application ");
            ((StringBuilder)localObject9).append(localObject10.getClass().getName());
            ((StringBuilder)localObject9).append(": ");
            ((StringBuilder)localObject9).append(localException3.toString());
            localRuntimeException.<init>(((StringBuilder)localObject9).toString(), localException3);
            throw localRuntimeException;
          }
          finally
          {
            RuntimeException localRuntimeException;
            break label2141;
          }
          localException2 = localException2;
          localRuntimeException = new java/lang/RuntimeException;
          localObject9 = new java/lang/StringBuilder;
          ((StringBuilder)localObject9).<init>();
          ((StringBuilder)localObject9).append("Exception thrown in onCreate() of ");
          ((StringBuilder)localObject9).append(instrumentationName);
          ((StringBuilder)localObject9).append(": ");
          ((StringBuilder)localObject9).append(localException2.toString());
          localRuntimeException.<init>(((StringBuilder)localObject9).toString(), localException2);
          throw localRuntimeException;
        }
        if (appInfo.targetSdkVersion < 27) {
          break label2164;
        }
      }
      finally {}
      label2141:
      if (StrictMode.getThreadPolicy().equals(localObject1)) {
        label2164:
        StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)???);
      }
      throw localObject8;
    }
  }
  
  private void handleBindService(BindServiceData paramBindServiceData)
  {
    Service localService = (Service)mServices.get(token);
    if (localService != null) {
      try
      {
        intent.setExtrasClassLoader(localService.getClassLoader());
        intent.prepareToEnterProcess();
        try
        {
          if (!rebind)
          {
            IBinder localIBinder = localService.onBind(intent);
            ActivityManager.getService().publishService(token, intent, localIBinder);
          }
          else
          {
            localService.onRebind(intent);
            ActivityManager.getService().serviceDoneExecuting(token, 0, 0, 0);
          }
          ensureJitEnabled();
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
        StringBuilder localStringBuilder;
        return;
      }
      catch (Exception localException)
      {
        if (!mInstrumentation.onException(localService, localException))
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unable to bind to service ");
          localStringBuilder.append(localService);
          localStringBuilder.append(" with ");
          localStringBuilder.append(intent);
          localStringBuilder.append(": ");
          localStringBuilder.append(localException.toString());
          throw new RuntimeException(localStringBuilder.toString(), localException);
        }
      }
    }
  }
  
  private void handleConfigurationChanged(Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo)
  {
    int i = 0;
    int j;
    if ((paramConfiguration != null) && (mConfiguration != null) && (mConfiguration.diffPublicOnly(paramConfiguration) == 0)) {
      j = 1;
    } else {
      j = 0;
    }
    Resources.Theme localTheme1 = getSystemContext().getTheme();
    Resources.Theme localTheme2 = getSystemUiContext().getTheme();
    ResourcesManager localResourcesManager = mResourcesManager;
    Object localObject = paramConfiguration;
    try
    {
      if (mPendingConfiguration != null)
      {
        localObject = paramConfiguration;
        if (!mPendingConfiguration.isOtherSeqNewer(paramConfiguration))
        {
          localObject = mPendingConfiguration;
          mCurDefaultDisplayDpi = densityDpi;
          updateDefaultDensity();
        }
        mPendingConfiguration = null;
      }
      if (localObject == null) {
        return;
      }
      mResourcesManager.applyConfigurationToResourcesLocked((Configuration)localObject, paramCompatibilityInfo);
      updateLocaleListFromAppContext(mInitialApplication.getApplicationContext(), mResourcesManager.getConfiguration().getLocales());
      if (mConfiguration == null)
      {
        paramConfiguration = new android/content/res/Configuration;
        paramConfiguration.<init>();
        mConfiguration = paramConfiguration;
      }
      if ((!mConfiguration.isOtherSeqNewer((Configuration)localObject)) && (paramCompatibilityInfo == null)) {
        return;
      }
      int k = mConfiguration.updateFrom((Configuration)localObject);
      paramConfiguration = applyCompatConfiguration(mCurDefaultDisplayDpi);
      if ((localTheme1.getChangingConfigurations() & k) != 0) {
        localTheme1.rebase();
      }
      if ((localTheme2.getChangingConfigurations() & k) != 0) {
        localTheme2.rebase();
      }
      paramCompatibilityInfo = collectComponentCallbacks(false, paramConfiguration);
      freeTextLayoutCachesIfNeeded(k);
      if (paramCompatibilityInfo != null)
      {
        k = paramCompatibilityInfo.size();
        while (i < k)
        {
          localObject = (ComponentCallbacks2)paramCompatibilityInfo.get(i);
          if ((localObject instanceof Activity))
          {
            localObject = (Activity)localObject;
            performConfigurationChangedForActivity((ActivityClientRecord)mActivities.get(((Activity)localObject).getActivityToken()), paramConfiguration);
          }
          else if (j == 0)
          {
            performConfigurationChanged((ComponentCallbacks2)localObject, paramConfiguration);
          }
          i++;
        }
      }
      return;
    }
    finally {}
  }
  
  /* Error */
  private void handleCreateBackupAgent(CreateBackupAgentData paramCreateBackupAgentData)
  {
    // Byte code:
    //   0: invokestatic 1737	android/app/ActivityThread:getPackageManager	()Landroid/content/pm/IPackageManager;
    //   3: aload_1
    //   4: getfield 2061	android/app/ActivityThread$CreateBackupAgentData:appInfo	Landroid/content/pm/ApplicationInfo;
    //   7: getfield 907	android/content/pm/ApplicationInfo:packageName	Ljava/lang/String;
    //   10: iconst_0
    //   11: invokestatic 540	android/os/UserHandle:myUserId	()I
    //   14: invokeinterface 2064 4 0
    //   19: getfield 2069	android/content/pm/PackageInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   22: getfield 1357	android/content/pm/ApplicationInfo:uid	I
    //   25: invokestatic 2072	android/os/Process:myUid	()I
    //   28: if_icmpeq +42 -> 70
    //   31: new 632	java/lang/StringBuilder
    //   34: astore_2
    //   35: aload_2
    //   36: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   39: aload_2
    //   40: ldc_w 2074
    //   43: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: pop
    //   47: aload_2
    //   48: aload_1
    //   49: getfield 2061	android/app/ActivityThread$CreateBackupAgentData:appInfo	Landroid/content/pm/ApplicationInfo;
    //   52: getfield 907	android/content/pm/ApplicationInfo:packageName	Ljava/lang/String;
    //   55: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   58: pop
    //   59: ldc 124
    //   61: aload_2
    //   62: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   65: invokestatic 1670	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   68: pop
    //   69: return
    //   70: aload_0
    //   71: invokevirtual 2077	android/app/ActivityThread:unscheduleGcIdler	()V
    //   74: aload_0
    //   75: aload_1
    //   76: getfield 2061	android/app/ActivityThread$CreateBackupAgentData:appInfo	Landroid/content/pm/ApplicationInfo;
    //   79: aload_1
    //   80: getfield 2078	android/app/ActivityThread$CreateBackupAgentData:compatInfo	Landroid/content/res/CompatibilityInfo;
    //   83: invokevirtual 1557	android/app/ActivityThread:getPackageInfoNoCheck	(Landroid/content/pm/ApplicationInfo;Landroid/content/res/CompatibilityInfo;)Landroid/app/LoadedApk;
    //   86: astore_3
    //   87: aload_3
    //   88: getfield 853	android/app/LoadedApk:mPackageName	Ljava/lang/String;
    //   91: astore 4
    //   93: aload 4
    //   95: ifnonnull +13 -> 108
    //   98: ldc 124
    //   100: ldc_w 2080
    //   103: invokestatic 2081	android/util/Slog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   106: pop
    //   107: return
    //   108: aload_1
    //   109: getfield 2061	android/app/ActivityThread$CreateBackupAgentData:appInfo	Landroid/content/pm/ApplicationInfo;
    //   112: getfield 2084	android/content/pm/ApplicationInfo:backupAgentName	Ljava/lang/String;
    //   115: astore_2
    //   116: aload_2
    //   117: astore 5
    //   119: aload_2
    //   120: ifnonnull +27 -> 147
    //   123: aload_1
    //   124: getfield 2087	android/app/ActivityThread$CreateBackupAgentData:backupMode	I
    //   127: iconst_1
    //   128: if_icmpeq +14 -> 142
    //   131: aload_2
    //   132: astore 5
    //   134: aload_1
    //   135: getfield 2087	android/app/ActivityThread$CreateBackupAgentData:backupMode	I
    //   138: iconst_3
    //   139: if_icmpne +8 -> 147
    //   142: ldc_w 2089
    //   145: astore 5
    //   147: aconst_null
    //   148: astore 6
    //   150: aload_0
    //   151: getfield 296	android/app/ActivityThread:mBackupAgents	Landroid/util/ArrayMap;
    //   154: aload 4
    //   156: invokevirtual 1308	android/util/ArrayMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   159: checkcast 2091	android/app/backup/BackupAgent
    //   162: astore_2
    //   163: aload_2
    //   164: ifnull +12 -> 176
    //   167: aload_2
    //   168: invokevirtual 2093	android/app/backup/BackupAgent:onBind	()Landroid/os/IBinder;
    //   171: astore 6
    //   173: goto +151 -> 324
    //   176: aload 6
    //   178: astore_2
    //   179: aload_3
    //   180: invokevirtual 1387	android/app/LoadedApk:getClassLoader	()Ljava/lang/ClassLoader;
    //   183: aload 5
    //   185: invokevirtual 1857	java/lang/ClassLoader:loadClass	(Ljava/lang/String;)Ljava/lang/Class;
    //   188: invokevirtual 1860	java/lang/Class:newInstance	()Ljava/lang/Object;
    //   191: checkcast 2091	android/app/backup/BackupAgent
    //   194: astore 7
    //   196: aload 6
    //   198: astore_2
    //   199: aload_0
    //   200: aload_3
    //   201: invokestatic 603	android/app/ContextImpl:createAppContext	(Landroid/app/ActivityThread;Landroid/app/LoadedApk;)Landroid/app/ContextImpl;
    //   204: astore_3
    //   205: aload 6
    //   207: astore_2
    //   208: aload_3
    //   209: aload 7
    //   211: invokevirtual 2096	android/app/ContextImpl:setOuterContext	(Landroid/content/Context;)V
    //   214: aload 6
    //   216: astore_2
    //   217: aload 7
    //   219: aload_3
    //   220: invokevirtual 2098	android/app/backup/BackupAgent:attach	(Landroid/content/Context;)V
    //   223: aload 6
    //   225: astore_2
    //   226: aload 7
    //   228: invokevirtual 2099	android/app/backup/BackupAgent:onCreate	()V
    //   231: aload 6
    //   233: astore_2
    //   234: aload 7
    //   236: invokevirtual 2093	android/app/backup/BackupAgent:onBind	()Landroid/os/IBinder;
    //   239: astore 6
    //   241: aload 6
    //   243: astore_2
    //   244: aload_0
    //   245: getfield 296	android/app/ActivityThread:mBackupAgents	Landroid/util/ArrayMap;
    //   248: aload 4
    //   250: aload 7
    //   252: invokevirtual 1312	android/util/ArrayMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   255: pop
    //   256: goto +68 -> 324
    //   259: astore_3
    //   260: new 632	java/lang/StringBuilder
    //   263: astore 6
    //   265: aload 6
    //   267: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   270: aload 6
    //   272: ldc_w 2101
    //   275: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: pop
    //   279: aload 6
    //   281: aload_3
    //   282: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   285: pop
    //   286: ldc 124
    //   288: aload 6
    //   290: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   293: invokestatic 672	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   296: pop
    //   297: aload_2
    //   298: astore 6
    //   300: aload_1
    //   301: getfield 2087	android/app/ActivityThread$CreateBackupAgentData:backupMode	I
    //   304: iconst_2
    //   305: if_icmpeq +19 -> 324
    //   308: aload_1
    //   309: getfield 2087	android/app/ActivityThread$CreateBackupAgentData:backupMode	I
    //   312: iconst_3
    //   313: if_icmpne +9 -> 322
    //   316: aload_2
    //   317: astore 6
    //   319: goto +5 -> 324
    //   322: aload_3
    //   323: athrow
    //   324: invokestatic 561	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   327: aload 4
    //   329: aload 6
    //   331: invokeinterface 2105 3 0
    //   336: return
    //   337: astore_1
    //   338: aload_1
    //   339: invokevirtual 579	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   342: athrow
    //   343: astore_1
    //   344: new 632	java/lang/StringBuilder
    //   347: dup
    //   348: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   351: astore_2
    //   352: aload_2
    //   353: ldc_w 2107
    //   356: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   359: pop
    //   360: aload_2
    //   361: aload 5
    //   363: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   366: pop
    //   367: aload_2
    //   368: ldc_w 753
    //   371: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   374: pop
    //   375: aload_2
    //   376: aload_1
    //   377: invokevirtual 643	java/lang/Exception:toString	()Ljava/lang/String;
    //   380: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   383: pop
    //   384: new 645	java/lang/RuntimeException
    //   387: dup
    //   388: aload_2
    //   389: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   392: aload_1
    //   393: invokespecial 649	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   396: athrow
    //   397: astore_1
    //   398: aload_1
    //   399: invokevirtual 579	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   402: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	403	0	this	ActivityThread
    //   0	403	1	paramCreateBackupAgentData	CreateBackupAgentData
    //   34	355	2	localObject1	Object
    //   86	134	3	localObject2	Object
    //   259	64	3	localException	Exception
    //   91	237	4	str	String
    //   117	245	5	localObject3	Object
    //   148	182	6	localObject4	Object
    //   194	57	7	localBackupAgent	BackupAgent
    // Exception table:
    //   from	to	target	type
    //   179	196	259	java/lang/Exception
    //   199	205	259	java/lang/Exception
    //   208	214	259	java/lang/Exception
    //   217	223	259	java/lang/Exception
    //   226	231	259	java/lang/Exception
    //   234	241	259	java/lang/Exception
    //   244	256	259	java/lang/Exception
    //   324	336	337	android/os/RemoteException
    //   150	163	343	java/lang/Exception
    //   167	173	343	java/lang/Exception
    //   260	297	343	java/lang/Exception
    //   300	316	343	java/lang/Exception
    //   322	324	343	java/lang/Exception
    //   324	336	343	java/lang/Exception
    //   338	343	343	java/lang/Exception
    //   0	69	397	android/os/RemoteException
  }
  
  private void handleCreateService(CreateServiceData paramCreateServiceData)
  {
    unscheduleGcIdler();
    Object localObject1 = getPackageInfoNoCheck(info.applicationInfo, compatInfo);
    Object localObject2 = null;
    try
    {
      Object localObject3 = ((LoadedApk)localObject1).getClassLoader();
      localObject3 = ((LoadedApk)localObject1).getAppFactory().instantiateService((ClassLoader)localObject3, info.name, intent);
      localObject2 = localObject3;
    }
    catch (Exception localException1)
    {
      if (!mInstrumentation.onException(null, localException1)) {
        break label235;
      }
    }
    try
    {
      ContextImpl localContextImpl = ContextImpl.createAppContext(this, (LoadedApk)localObject1);
      localContextImpl.setOuterContext((Context)localObject2);
      localObject1 = ((LoadedApk)localObject1).makeApplication(false, mInstrumentation);
      ((Service)localObject2).attach(localContextImpl, this, info.name, token, (Application)localObject1, ActivityManager.getService());
      ((Service)localObject2).onCreate();
      mServices.put(token, localObject2);
      try
      {
        ActivityManager.getService().serviceDoneExecuting(token, 0, 0, 0);
      }
      catch (RemoteException localRemoteException)
      {
        throw localRemoteException.rethrowFromSystemServer();
      }
      localObject2 = new StringBuilder();
    }
    catch (Exception localException2)
    {
      if (mInstrumentation.onException(localObject2, localException2)) {
        return;
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Unable to create service ");
      ((StringBuilder)localObject2).append(info.name);
      ((StringBuilder)localObject2).append(": ");
      ((StringBuilder)localObject2).append(localException2.toString());
      throw new RuntimeException(((StringBuilder)localObject2).toString(), localException2);
    }
    label235:
    ((StringBuilder)localObject2).append("Unable to instantiate service ");
    ((StringBuilder)localObject2).append(info.name);
    ((StringBuilder)localObject2).append(": ");
    ((StringBuilder)localObject2).append(localException2.toString());
    throw new RuntimeException(((StringBuilder)localObject2).toString(), localException2);
  }
  
  private void handleDestroyBackupAgent(CreateBackupAgentData paramCreateBackupAgentData)
  {
    Object localObject1 = getPackageInfoNoCheckappInfo, compatInfo).mPackageName;
    Object localObject2 = (BackupAgent)mBackupAgents.get(localObject1);
    if (localObject2 != null)
    {
      try
      {
        ((BackupAgent)localObject2).onDestroy();
      }
      catch (Exception localException)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append("Exception thrown in onDestroy by backup agent of ");
        ((StringBuilder)localObject2).append(appInfo);
        Slog.w("ActivityThread", ((StringBuilder)localObject2).toString());
        localException.printStackTrace();
      }
      mBackupAgents.remove(localObject1);
    }
    else
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Attempt to destroy unknown backup agent ");
      ((StringBuilder)localObject1).append(paramCreateBackupAgentData);
      Slog.w("ActivityThread", ((StringBuilder)localObject1).toString());
    }
  }
  
  private void handleDumpActivity(DumpComponentInfo paramDumpComponentInfo)
  {
    StrictMode.ThreadPolicy localThreadPolicy = StrictMode.allowThreadDiskWrites();
    try
    {
      ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mActivities.get(token);
      if ((localActivityClientRecord != null) && (activity != null))
      {
        FastPrintWriter localFastPrintWriter = new com/android/internal/util/FastPrintWriter;
        FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
        localFileOutputStream.<init>(fd.getFileDescriptor());
        localFastPrintWriter.<init>(localFileOutputStream);
        activity.dump(prefix, fd.getFileDescriptor(), localFastPrintWriter, args);
        localFastPrintWriter.flush();
      }
      return;
    }
    finally
    {
      IoUtils.closeQuietly(fd);
      StrictMode.setThreadPolicy(localThreadPolicy);
    }
  }
  
  /* Error */
  static void handleDumpHeap(DumpHeapData paramDumpHeapData)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 2191	android/app/ActivityThread$DumpHeapData:runGc	Z
    //   4: ifeq +12 -> 16
    //   7: invokestatic 2196	java/lang/System:gc	()V
    //   10: invokestatic 2199	java/lang/System:runFinalization	()V
    //   13: invokestatic 2196	java/lang/System:gc	()V
    //   16: aload_0
    //   17: getfield 2202	android/app/ActivityThread$DumpHeapData:managed	Z
    //   20: ifeq +122 -> 142
    //   23: aload_0
    //   24: getfield 2205	android/app/ActivityThread$DumpHeapData:path	Ljava/lang/String;
    //   27: aload_0
    //   28: getfield 2206	android/app/ActivityThread$DumpHeapData:fd	Landroid/os/ParcelFileDescriptor;
    //   31: invokevirtual 2162	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   34: invokestatic 2210	android/os/Debug:dumpHprofData	(Ljava/lang/String;Ljava/io/FileDescriptor;)V
    //   37: aload_0
    //   38: getfield 2206	android/app/ActivityThread$DumpHeapData:fd	Landroid/os/ParcelFileDescriptor;
    //   41: invokevirtual 2213	android/os/ParcelFileDescriptor:close	()V
    //   44: goto +128 -> 172
    //   47: astore_1
    //   48: ldc 124
    //   50: ldc_w 2215
    //   53: aload_1
    //   54: invokestatic 2218	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   57: pop
    //   58: goto +114 -> 172
    //   61: astore_1
    //   62: goto +57 -> 119
    //   65: astore_1
    //   66: new 632	java/lang/StringBuilder
    //   69: astore_1
    //   70: aload_1
    //   71: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   74: aload_1
    //   75: ldc_w 2220
    //   78: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: pop
    //   82: aload_1
    //   83: aload_0
    //   84: getfield 2205	android/app/ActivityThread$DumpHeapData:path	Ljava/lang/String;
    //   87: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: pop
    //   91: aload_1
    //   92: ldc_w 2222
    //   95: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: pop
    //   99: ldc 124
    //   101: aload_1
    //   102: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   105: invokestatic 1670	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   108: pop
    //   109: aload_0
    //   110: getfield 2206	android/app/ActivityThread$DumpHeapData:fd	Landroid/os/ParcelFileDescriptor;
    //   113: invokevirtual 2213	android/os/ParcelFileDescriptor:close	()V
    //   116: goto -72 -> 44
    //   119: aload_0
    //   120: getfield 2206	android/app/ActivityThread$DumpHeapData:fd	Landroid/os/ParcelFileDescriptor;
    //   123: invokevirtual 2213	android/os/ParcelFileDescriptor:close	()V
    //   126: goto +14 -> 140
    //   129: astore_0
    //   130: ldc 124
    //   132: ldc_w 2215
    //   135: aload_0
    //   136: invokestatic 2218	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   139: pop
    //   140: aload_1
    //   141: athrow
    //   142: aload_0
    //   143: getfield 2225	android/app/ActivityThread$DumpHeapData:mallocInfo	Z
    //   146: ifeq +16 -> 162
    //   149: aload_0
    //   150: getfield 2206	android/app/ActivityThread$DumpHeapData:fd	Landroid/os/ParcelFileDescriptor;
    //   153: invokevirtual 2162	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   156: invokestatic 2228	android/os/Debug:dumpNativeMallocInfo	(Ljava/io/FileDescriptor;)V
    //   159: goto +13 -> 172
    //   162: aload_0
    //   163: getfield 2206	android/app/ActivityThread$DumpHeapData:fd	Landroid/os/ParcelFileDescriptor;
    //   166: invokevirtual 2162	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   169: invokestatic 2231	android/os/Debug:dumpNativeHeap	(Ljava/io/FileDescriptor;)V
    //   172: invokestatic 561	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   175: aload_0
    //   176: getfield 2205	android/app/ActivityThread$DumpHeapData:path	Ljava/lang/String;
    //   179: invokeinterface 2234 2 0
    //   184: return
    //   185: astore_0
    //   186: aload_0
    //   187: invokevirtual 579	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   190: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	191	0	paramDumpHeapData	DumpHeapData
    //   47	7	1	localIOException1	IOException
    //   61	1	1	localObject	Object
    //   65	1	1	localIOException2	IOException
    //   69	72	1	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   37	44	47	java/io/IOException
    //   109	116	47	java/io/IOException
    //   23	37	61	finally
    //   66	109	61	finally
    //   23	37	65	java/io/IOException
    //   119	126	129	java/io/IOException
    //   172	184	185	android/os/RemoteException
  }
  
  private void handleDumpProvider(DumpComponentInfo paramDumpComponentInfo)
  {
    StrictMode.ThreadPolicy localThreadPolicy = StrictMode.allowThreadDiskWrites();
    try
    {
      ProviderClientRecord localProviderClientRecord = (ProviderClientRecord)mLocalProviders.get(token);
      if ((localProviderClientRecord != null) && (mLocalProvider != null))
      {
        FastPrintWriter localFastPrintWriter = new com/android/internal/util/FastPrintWriter;
        FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
        localFileOutputStream.<init>(fd.getFileDescriptor());
        localFastPrintWriter.<init>(localFileOutputStream);
        mLocalProvider.dump(fd.getFileDescriptor(), localFastPrintWriter, args);
        localFastPrintWriter.flush();
      }
      return;
    }
    finally
    {
      IoUtils.closeQuietly(fd);
      StrictMode.setThreadPolicy(localThreadPolicy);
    }
  }
  
  private void handleDumpService(DumpComponentInfo paramDumpComponentInfo)
  {
    StrictMode.ThreadPolicy localThreadPolicy = StrictMode.allowThreadDiskWrites();
    try
    {
      Service localService = (Service)mServices.get(token);
      if (localService != null)
      {
        FastPrintWriter localFastPrintWriter = new com/android/internal/util/FastPrintWriter;
        FileOutputStream localFileOutputStream = new java/io/FileOutputStream;
        localFileOutputStream.<init>(fd.getFileDescriptor());
        localFastPrintWriter.<init>(localFileOutputStream);
        localService.dump(fd.getFileDescriptor(), localFastPrintWriter, args);
        localFastPrintWriter.flush();
      }
      return;
    }
    finally
    {
      IoUtils.closeQuietly(fd);
      StrictMode.setThreadPolicy(localThreadPolicy);
    }
  }
  
  private void handleEnterAnimationComplete(IBinder paramIBinder)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (paramIBinder != null) {
      activity.dispatchEnterAnimationComplete();
    }
  }
  
  private void handleLocalVoiceInteractionStarted(IBinder paramIBinder, IVoiceInteractor paramIVoiceInteractor)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (paramIBinder != null)
    {
      voiceInteractor = paramIVoiceInteractor;
      activity.setVoiceInteractor(paramIVoiceInteractor);
      if (paramIVoiceInteractor == null) {
        activity.onLocalVoiceInteractionStopped();
      } else {
        activity.onLocalVoiceInteractionStarted();
      }
    }
  }
  
  /* Error */
  private void handleReceiver(ReceiverData paramReceiverData)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 2077	android/app/ActivityThread:unscheduleGcIdler	()V
    //   4: aload_1
    //   5: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   8: invokevirtual 746	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   11: invokevirtual 1851	android/content/ComponentName:getClassName	()Ljava/lang/String;
    //   14: astore_2
    //   15: aload_0
    //   16: aload_1
    //   17: getfield 2264	android/app/ActivityThread$ReceiverData:info	Landroid/content/pm/ActivityInfo;
    //   20: getfield 2267	android/content/pm/ActivityInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   23: aload_1
    //   24: getfield 2268	android/app/ActivityThread$ReceiverData:compatInfo	Landroid/content/res/CompatibilityInfo;
    //   27: invokevirtual 1557	android/app/ActivityThread:getPackageInfoNoCheck	(Landroid/content/pm/ApplicationInfo;Landroid/content/res/CompatibilityInfo;)Landroid/app/LoadedApk;
    //   30: astore_3
    //   31: invokestatic 561	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   34: astore 4
    //   36: aload_3
    //   37: iconst_0
    //   38: aload_0
    //   39: getfield 586	android/app/ActivityThread:mInstrumentation	Landroid/app/Instrumentation;
    //   42: invokevirtual 609	android/app/LoadedApk:makeApplication	(ZLandroid/app/Instrumentation;)Landroid/app/Application;
    //   45: astore 5
    //   47: aload 5
    //   49: invokevirtual 2271	android/app/Application:getBaseContext	()Landroid/content/Context;
    //   52: checkcast 595	android/app/ContextImpl
    //   55: astore 6
    //   57: aload 6
    //   59: astore 7
    //   61: aload_1
    //   62: getfield 2264	android/app/ActivityThread$ReceiverData:info	Landroid/content/pm/ActivityInfo;
    //   65: getfield 2274	android/content/pm/ActivityInfo:splitName	Ljava/lang/String;
    //   68: ifnull +20 -> 88
    //   71: aload 6
    //   73: aload_1
    //   74: getfield 2264	android/app/ActivityThread$ReceiverData:info	Landroid/content/pm/ActivityInfo;
    //   77: getfield 2274	android/content/pm/ActivityInfo:splitName	Ljava/lang/String;
    //   80: invokevirtual 2278	android/app/ContextImpl:createContextForSplit	(Ljava/lang/String;)Landroid/content/Context;
    //   83: checkcast 595	android/app/ContextImpl
    //   86: astore 7
    //   88: aload 7
    //   90: invokevirtual 1846	android/app/ContextImpl:getClassLoader	()Ljava/lang/ClassLoader;
    //   93: astore 6
    //   95: aload_1
    //   96: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   99: aload 6
    //   101: invokevirtual 957	android/content/Intent:setExtrasClassLoader	(Ljava/lang/ClassLoader;)V
    //   104: aload_1
    //   105: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   108: invokevirtual 958	android/content/Intent:prepareToEnterProcess	()V
    //   111: aload_1
    //   112: aload 6
    //   114: invokevirtual 2279	android/app/ActivityThread$ReceiverData:setExtrasClassLoader	(Ljava/lang/ClassLoader;)V
    //   117: aload_3
    //   118: invokevirtual 2118	android/app/LoadedApk:getAppFactory	()Landroid/app/AppComponentFactory;
    //   121: aload 6
    //   123: aload_1
    //   124: getfield 2264	android/app/ActivityThread$ReceiverData:info	Landroid/content/pm/ActivityInfo;
    //   127: getfield 2280	android/content/pm/ActivityInfo:name	Ljava/lang/String;
    //   130: aload_1
    //   131: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   134: invokevirtual 2284	android/app/AppComponentFactory:instantiateReceiver	(Ljava/lang/ClassLoader;Ljava/lang/String;Landroid/content/Intent;)Landroid/content/BroadcastReceiver;
    //   137: astore 8
    //   139: ldc2_w 100
    //   142: lstore 9
    //   144: getstatic 242	android/app/ActivityThread:sCurrentBroadcastIntent	Ljava/lang/ThreadLocal;
    //   147: aload_1
    //   148: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   151: invokevirtual 2286	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
    //   154: aload 8
    //   156: aload_1
    //   157: invokevirtual 2292	android/content/BroadcastReceiver:setPendingResult	(Landroid/content/BroadcastReceiver$PendingResult;)V
    //   160: invokestatic 1442	android/os/SystemClock:elapsedRealtime	()J
    //   163: lstore 11
    //   165: aload 8
    //   167: aload 7
    //   169: invokevirtual 2295	android/app/ContextImpl:getReceiverRestrictedContext	()Landroid/content/Context;
    //   172: aload_1
    //   173: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   176: invokevirtual 2299	android/content/BroadcastReceiver:onReceive	(Landroid/content/Context;Landroid/content/Intent;)V
    //   179: invokestatic 1442	android/os/SystemClock:elapsedRealtime	()J
    //   182: lstore 9
    //   184: lload 9
    //   186: lload 11
    //   188: lsub
    //   189: lstore 9
    //   191: getstatic 242	android/app/ActivityThread:sCurrentBroadcastIntent	Ljava/lang/ThreadLocal;
    //   194: aconst_null
    //   195: invokevirtual 2286	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
    //   198: lload 9
    //   200: ldc2_w 100
    //   203: lcmp
    //   204: ifeq +222 -> 426
    //   207: aload 5
    //   209: ifnonnull +6 -> 215
    //   212: goto +214 -> 426
    //   215: lload 9
    //   217: invokestatic 2302	android/os/Build:getOnReceiveTimeLimit	()I
    //   220: i2l
    //   221: lcmp
    //   222: iflt +388 -> 610
    //   225: new 632	java/lang/StringBuilder
    //   228: dup
    //   229: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   232: astore 7
    //   234: aload 7
    //   236: ldc_w 2304
    //   239: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: pop
    //   243: aload 7
    //   245: aload_1
    //   246: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   249: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   252: pop
    //   253: aload 7
    //   255: ldc_w 2306
    //   258: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   261: pop
    //   262: aload 7
    //   264: aload 5
    //   266: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   269: pop
    //   270: aload 7
    //   272: ldc_w 2308
    //   275: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   278: pop
    //   279: aload 7
    //   281: aload 5
    //   283: invokevirtual 2309	android/app/Application:getPackageName	()Ljava/lang/String;
    //   286: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: pop
    //   290: aload 7
    //   292: ldc_w 2311
    //   295: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   298: pop
    //   299: aload 7
    //   301: aload_3
    //   302: invokevirtual 1665	android/app/LoadedApk:getPackageName	()Ljava/lang/String;
    //   305: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   308: pop
    //   309: aload_1
    //   310: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   313: invokevirtual 746	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   316: ifnonnull +11 -> 327
    //   319: ldc_w 2313
    //   322: astore 6
    //   324: goto +44 -> 368
    //   327: new 632	java/lang/StringBuilder
    //   330: dup
    //   331: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   334: astore 6
    //   336: aload 6
    //   338: ldc_w 2315
    //   341: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   344: pop
    //   345: aload 6
    //   347: aload_1
    //   348: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   351: invokevirtual 746	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   354: invokevirtual 751	android/content/ComponentName:toShortString	()Ljava/lang/String;
    //   357: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   360: pop
    //   361: aload 6
    //   363: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   366: astore 6
    //   368: aload 7
    //   370: aload 6
    //   372: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   375: pop
    //   376: aload 7
    //   378: ldc_w 2317
    //   381: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   384: pop
    //   385: aload 7
    //   387: aload_3
    //   388: invokevirtual 1770	android/app/LoadedApk:getAppDir	()Ljava/lang/String;
    //   391: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   394: pop
    //   395: aload 7
    //   397: ldc_w 2319
    //   400: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   403: pop
    //   404: aload 7
    //   406: lload 9
    //   408: invokevirtual 2322	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   411: pop
    //   412: ldc 124
    //   414: aload 7
    //   416: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   419: invokestatic 2325	android/util/Slog:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   422: pop
    //   423: goto +187 -> 610
    //   426: new 632	java/lang/StringBuilder
    //   429: dup
    //   430: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   433: astore 7
    //   435: aload 7
    //   437: aload_1
    //   438: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   441: invokevirtual 746	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   444: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   447: pop
    //   448: aload 7
    //   450: ldc_w 2327
    //   453: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   456: pop
    //   457: ldc 124
    //   459: aload 7
    //   461: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   464: invokestatic 2325	android/util/Slog:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   467: pop
    //   468: goto +142 -> 610
    //   471: astore 6
    //   473: goto +216 -> 689
    //   476: astore 7
    //   478: aload_1
    //   479: aload 4
    //   481: invokevirtual 2331	android/app/ActivityThread$ReceiverData:sendFinished	(Landroid/app/IActivityManager;)V
    //   484: aload_0
    //   485: getfield 586	android/app/ActivityThread:mInstrumentation	Landroid/app/Instrumentation;
    //   488: aload 8
    //   490: aload 7
    //   492: invokevirtual 731	android/app/Instrumentation:onException	(Ljava/lang/Object;Ljava/lang/Throwable;)Z
    //   495: istore 13
    //   497: iload 13
    //   499: ifeq +124 -> 623
    //   502: getstatic 242	android/app/ActivityThread:sCurrentBroadcastIntent	Ljava/lang/ThreadLocal;
    //   505: aconst_null
    //   506: invokevirtual 2286	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
    //   509: ldc2_w 100
    //   512: ldc2_w 100
    //   515: lcmp
    //   516: ifeq +82 -> 598
    //   519: iconst_0
    //   520: ifne +6 -> 526
    //   523: goto +75 -> 598
    //   526: ldc2_w 100
    //   529: invokestatic 2302	android/os/Build:getOnReceiveTimeLimit	()I
    //   532: i2l
    //   533: lcmp
    //   534: iflt +76 -> 610
    //   537: new 632	java/lang/StringBuilder
    //   540: dup
    //   541: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   544: astore 7
    //   546: aload 7
    //   548: ldc_w 2304
    //   551: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   554: pop
    //   555: aload 7
    //   557: aload_1
    //   558: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   561: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   564: pop
    //   565: aload 7
    //   567: ldc_w 2306
    //   570: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   573: pop
    //   574: aload 7
    //   576: aconst_null
    //   577: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   580: pop
    //   581: aload 7
    //   583: ldc_w 2308
    //   586: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   589: pop
    //   590: new 2333	java/lang/NullPointerException
    //   593: dup
    //   594: invokespecial 2334	java/lang/NullPointerException:<init>	()V
    //   597: athrow
    //   598: new 632	java/lang/StringBuilder
    //   601: dup
    //   602: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   605: astore 7
    //   607: goto -172 -> 435
    //   610: aload 8
    //   612: invokevirtual 2338	android/content/BroadcastReceiver:getPendingResult	()Landroid/content/BroadcastReceiver$PendingResult;
    //   615: ifnull +7 -> 622
    //   618: aload_1
    //   619: invokevirtual 2341	android/app/ActivityThread$ReceiverData:finish	()V
    //   622: return
    //   623: new 645	java/lang/RuntimeException
    //   626: astore 8
    //   628: new 632	java/lang/StringBuilder
    //   631: astore 6
    //   633: aload 6
    //   635: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   638: aload 6
    //   640: ldc_w 2343
    //   643: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   646: pop
    //   647: aload 6
    //   649: aload_2
    //   650: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   653: pop
    //   654: aload 6
    //   656: ldc_w 753
    //   659: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   662: pop
    //   663: aload 6
    //   665: aload 7
    //   667: invokevirtual 643	java/lang/Exception:toString	()Ljava/lang/String;
    //   670: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   673: pop
    //   674: aload 8
    //   676: aload 6
    //   678: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   681: aload 7
    //   683: invokespecial 649	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   686: aload 8
    //   688: athrow
    //   689: getstatic 242	android/app/ActivityThread:sCurrentBroadcastIntent	Ljava/lang/ThreadLocal;
    //   692: aconst_null
    //   693: invokevirtual 2286	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
    //   696: ldc2_w 100
    //   699: ldc2_w 100
    //   702: lcmp
    //   703: ifeq +79 -> 782
    //   706: iconst_0
    //   707: ifeq +75 -> 782
    //   710: ldc2_w 100
    //   713: invokestatic 2302	android/os/Build:getOnReceiveTimeLimit	()I
    //   716: i2l
    //   717: lcmp
    //   718: iflt +106 -> 824
    //   721: new 632	java/lang/StringBuilder
    //   724: dup
    //   725: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   728: astore 7
    //   730: aload 7
    //   732: ldc_w 2304
    //   735: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   738: pop
    //   739: aload 7
    //   741: aload_1
    //   742: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   745: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   748: pop
    //   749: aload 7
    //   751: ldc_w 2306
    //   754: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   757: pop
    //   758: aload 7
    //   760: aconst_null
    //   761: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   764: pop
    //   765: aload 7
    //   767: ldc_w 2308
    //   770: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   773: pop
    //   774: new 2333	java/lang/NullPointerException
    //   777: dup
    //   778: invokespecial 2334	java/lang/NullPointerException:<init>	()V
    //   781: athrow
    //   782: new 632	java/lang/StringBuilder
    //   785: dup
    //   786: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   789: astore 7
    //   791: aload 7
    //   793: aload_1
    //   794: getfield 2262	android/app/ActivityThread$ReceiverData:intent	Landroid/content/Intent;
    //   797: invokevirtual 746	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   800: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   803: pop
    //   804: aload 7
    //   806: ldc_w 2327
    //   809: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   812: pop
    //   813: ldc 124
    //   815: aload 7
    //   817: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   820: invokestatic 2325	android/util/Slog:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   823: pop
    //   824: aload 6
    //   826: athrow
    //   827: astore 7
    //   829: aload_1
    //   830: aload 4
    //   832: invokevirtual 2331	android/app/ActivityThread$ReceiverData:sendFinished	(Landroid/app/IActivityManager;)V
    //   835: new 632	java/lang/StringBuilder
    //   838: dup
    //   839: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   842: astore_1
    //   843: aload_1
    //   844: ldc_w 2345
    //   847: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   850: pop
    //   851: aload_1
    //   852: aload_2
    //   853: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   856: pop
    //   857: aload_1
    //   858: ldc_w 753
    //   861: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   864: pop
    //   865: aload_1
    //   866: aload 7
    //   868: invokevirtual 643	java/lang/Exception:toString	()Ljava/lang/String;
    //   871: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   874: pop
    //   875: new 645	java/lang/RuntimeException
    //   878: dup
    //   879: aload_1
    //   880: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   883: aload 7
    //   885: invokespecial 649	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   888: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	889	0	this	ActivityThread
    //   0	889	1	paramReceiverData	ReceiverData
    //   14	839	2	str	String
    //   30	358	3	localLoadedApk	LoadedApk
    //   34	797	4	localIActivityManager	IActivityManager
    //   45	237	5	localApplication	Application
    //   55	316	6	localObject1	Object
    //   471	1	6	localObject2	Object
    //   631	194	6	localStringBuilder1	StringBuilder
    //   59	401	7	localObject3	Object
    //   476	15	7	localException1	Exception
    //   544	272	7	localStringBuilder2	StringBuilder
    //   827	57	7	localException2	Exception
    //   137	550	8	localObject4	Object
    //   142	265	9	l1	long
    //   163	24	11	l2	long
    //   495	3	13	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   144	184	471	finally
    //   478	497	471	finally
    //   623	689	471	finally
    //   144	184	476	java/lang/Exception
    //   36	57	827	java/lang/Exception
    //   61	88	827	java/lang/Exception
    //   88	139	827	java/lang/Exception
  }
  
  private void handleRelaunchActivityInner(ActivityClientRecord paramActivityClientRecord, int paramInt, List<ResultInfo> paramList, List<ReferrerIntent> paramList1, PendingTransactionActions paramPendingTransactionActions, boolean paramBoolean, Configuration paramConfiguration, String paramString)
  {
    Intent localIntent = activity.mIntent;
    if (!paused) {
      performPauseActivity(paramActivityClientRecord, false, paramString, null);
    }
    if (!stopped) {
      callActivityOnStop(paramActivityClientRecord, true, paramString);
    }
    handleDestroyActivity(token, false, paramInt, true, paramString);
    activity = null;
    window = null;
    hideForNow = false;
    nextIdle = null;
    if (paramList != null) {
      if (pendingResults == null) {
        pendingResults = paramList;
      } else {
        pendingResults.addAll(paramList);
      }
    }
    if (paramList1 != null) {
      if (pendingIntents == null) {
        pendingIntents = paramList1;
      } else {
        pendingIntents.addAll(paramList1);
      }
    }
    startsNotResumed = paramBoolean;
    overrideConfig = paramConfiguration;
    handleLaunchActivity(paramActivityClientRecord, paramPendingTransactionActions, localIntent);
  }
  
  private void handleRelaunchActivityLocally(IBinder paramIBinder)
  {
    Object localObject = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (localObject == null)
    {
      Log.w("ActivityThread", "Activity to relaunch no longer exists");
      return;
    }
    int i = ((ActivityClientRecord)localObject).getLifecycleState();
    if ((i >= 3) && (i <= 5))
    {
      if (createdConfig != null) {
        paramIBinder = createdConfig;
      } else {
        paramIBinder = mConfiguration;
      }
      ActivityRelaunchItem localActivityRelaunchItem = ActivityRelaunchItem.obtain(null, null, 0, new MergedConfiguration(paramIBinder, overrideConfig), mPreserveWindow);
      paramIBinder = TransactionExecutorHelper.getLifecycleRequestForCurrentState((ActivityClientRecord)localObject);
      localObject = ClientTransaction.obtain(mAppThread, token);
      ((ClientTransaction)localObject).addCallback(localActivityRelaunchItem);
      ((ClientTransaction)localObject).setLifecycleStateRequest(paramIBinder);
      executeTransaction((ClientTransaction)localObject);
      return;
    }
    paramIBinder = new StringBuilder();
    paramIBinder.append("Activity state must be in [ON_RESUME..ON_STOP] in order to be relaunched,current state is ");
    paramIBinder.append(i);
    Log.w("ActivityThread", paramIBinder.toString());
  }
  
  private void handleRunIsolatedEntryPoint(String paramString, String[] paramArrayOfString)
  {
    try
    {
      Class.forName(paramString).getMethod("main", new Class[] { [Ljava.lang.String.class }).invoke(null, new Object[] { paramArrayOfString });
      System.exit(0);
      return;
    }
    catch (ReflectiveOperationException paramString)
    {
      throw new AndroidRuntimeException("runIsolatedEntryPoint failed", paramString);
    }
  }
  
  private void handleServiceArgs(ServiceArgsData paramServiceArgsData)
  {
    Service localService = (Service)mServices.get(token);
    if (localService != null) {
      try
      {
        if (args != null)
        {
          args.setExtrasClassLoader(localService.getClassLoader());
          args.prepareToEnterProcess();
        }
        int i;
        if (!taskRemoved)
        {
          i = localService.onStartCommand(args, flags, startId);
        }
        else
        {
          localService.onTaskRemoved(args);
          i = 1000;
        }
        QueuedWork.waitToFinish();
        try
        {
          ActivityManager.getService().serviceDoneExecuting(token, 1, startId, i);
          ensureJitEnabled();
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
        StringBuilder localStringBuilder;
        return;
      }
      catch (Exception localException)
      {
        if (!mInstrumentation.onException(localService, localException))
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Unable to start service ");
          localStringBuilder.append(localService);
          localStringBuilder.append(" with ");
          localStringBuilder.append(args);
          localStringBuilder.append(": ");
          localStringBuilder.append(localException.toString());
          throw new RuntimeException(localStringBuilder.toString(), localException);
        }
      }
    }
  }
  
  private void handleSetCoreSettings(Bundle paramBundle)
  {
    synchronized (mResourcesManager)
    {
      mCoreSettings = paramBundle;
      onCoreSettingsChange();
      return;
    }
  }
  
  private void handleSleeping(IBinder paramIBinder, boolean paramBoolean)
  {
    Object localObject = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("handleSleeping: no activity for token ");
      ((StringBuilder)localObject).append(paramIBinder);
      Log.w("ActivityThread", ((StringBuilder)localObject).toString());
      return;
    }
    if (paramBoolean)
    {
      if ((!stopped) && (!((ActivityClientRecord)localObject).isPreHoneycomb())) {
        callActivityOnStop((ActivityClientRecord)localObject, true, "sleeping");
      }
      if (!((ActivityClientRecord)localObject).isPreHoneycomb()) {
        QueuedWork.waitToFinish();
      }
      try
      {
        ActivityManager.getService().activitySlept(token);
      }
      catch (RemoteException paramIBinder)
      {
        throw paramIBinder.rethrowFromSystemServer();
      }
    }
    else if ((stopped) && (activity.mVisibleFromServer))
    {
      activity.performRestart(true, "handleSleeping");
      ((ActivityClientRecord)localObject).setState(2);
    }
  }
  
  private void handleStartBinderTracking() {}
  
  private void handleStopBinderTrackingAndDump(ParcelFileDescriptor paramParcelFileDescriptor)
  {
    try
    {
      Binder.disableTracing();
      Binder.getTransactionTracker().writeTracesToFile(paramParcelFileDescriptor);
      return;
    }
    finally
    {
      IoUtils.closeQuietly(paramParcelFileDescriptor);
      Binder.getTransactionTracker().clearTraces();
    }
  }
  
  private void handleStopService(IBinder paramIBinder)
  {
    Object localObject = (Service)mServices.remove(paramIBinder);
    if (localObject != null) {
      try
      {
        ((Service)localObject).onDestroy();
        ((Service)localObject).detachAndCleanUp();
        Context localContext = ((Service)localObject).getBaseContext();
        if ((localContext instanceof ContextImpl))
        {
          String str = ((Service)localObject).getClassName();
          ((ContextImpl)localContext).scheduleFinalCleanup(str, "Service");
        }
        QueuedWork.waitToFinish();
        try
        {
          ActivityManager.getService().serviceDoneExecuting(paramIBinder, 2, 0, 0);
        }
        catch (RemoteException localRemoteException)
        {
          throw localRemoteException.rethrowFromSystemServer();
        }
        localObject = new StringBuilder();
      }
      catch (Exception localException)
      {
        if (mInstrumentation.onException(localObject, localException))
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("handleStopService: exception for ");
          ((StringBuilder)localObject).append(paramIBinder);
          Slog.i("ActivityThread", ((StringBuilder)localObject).toString(), localException);
          return;
        }
        paramIBinder = new StringBuilder();
        paramIBinder.append("Unable to stop service ");
        paramIBinder.append(localObject);
        paramIBinder.append(": ");
        paramIBinder.append(localException.toString());
        throw new RuntimeException(paramIBinder.toString(), localException);
      }
    }
    ((StringBuilder)localObject).append("handleStopService: token=");
    ((StringBuilder)localObject).append(paramIBinder);
    ((StringBuilder)localObject).append(" not found.");
    Slog.i("ActivityThread", ((StringBuilder)localObject).toString());
  }
  
  private void handleTrimMemory(int paramInt)
  {
    Trace.traceBegin(64L, "trimMemory");
    ArrayList localArrayList = collectComponentCallbacks(true, null);
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      ((ComponentCallbacks2)localArrayList.get(j)).onTrimMemory(paramInt);
    }
    WindowManagerGlobal.getInstance().trimMemory(paramInt);
    Trace.traceEnd(64L);
  }
  
  private void handleUnbindService(BindServiceData paramBindServiceData)
  {
    Service localService = (Service)mServices.get(token);
    if (localService != null)
    {
      try
      {
        intent.setExtrasClassLoader(localService.getClassLoader());
        intent.prepareToEnterProcess();
        boolean bool = localService.onUnbind(intent);
        if (bool) {
          try
          {
            ActivityManager.getService().unbindFinished(token, intent, bool);
          }
          catch (RemoteException localRemoteException)
          {
            break label93;
          }
        } else {
          ActivityManager.getService().serviceDoneExecuting(token, 0, 0, 0);
        }
      }
      catch (Exception localException)
      {
        label93:
        if (mInstrumentation.onException(localService, localException)) {
          return;
        }
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unable to unbind to service ");
        localStringBuilder.append(localService);
        localStringBuilder.append(" with ");
        localStringBuilder.append(intent);
        localStringBuilder.append(": ");
        localStringBuilder.append(localException.toString());
        throw new RuntimeException(localStringBuilder.toString(), localException);
      }
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  private void handleUpdatePackageCompatibilityInfo(UpdateCompatibilityData paramUpdateCompatibilityData)
  {
    LoadedApk localLoadedApk = peekPackageInfo(pkg, false);
    if (localLoadedApk != null) {
      localLoadedApk.setCompatibilityInfo(info);
    }
    localLoadedApk = peekPackageInfo(pkg, true);
    if (localLoadedApk != null) {
      localLoadedApk.setCompatibilityInfo(info);
    }
    handleConfigurationChanged(mConfiguration, info);
    WindowManagerGlobal.getInstance().reportNewConfiguration(mConfiguration);
  }
  
  private final void incProviderRefLocked(ProviderRefCount paramProviderRefCount, boolean paramBoolean)
  {
    int i = 0;
    if (paramBoolean)
    {
      stableCount += 1;
      if (stableCount == 1)
      {
        if (removePending)
        {
          removePending = false;
          mH.removeMessages(131, paramProviderRefCount);
          i = -1;
        }
        try
        {
          ActivityManager.getService().refContentProvider(holder.connection, 1, i);
        }
        catch (RemoteException paramProviderRefCount) {}
      }
    }
    else
    {
      unstableCount += 1;
      if (unstableCount == 1) {
        if (removePending)
        {
          removePending = false;
          mH.removeMessages(131, paramProviderRefCount);
        }
        else
        {
          try
          {
            ActivityManager.getService().refContentProvider(holder.connection, 0, 1);
          }
          catch (RemoteException paramProviderRefCount) {}
        }
      }
    }
  }
  
  private void installContentProviders(Context paramContext, List<ProviderInfo> paramList)
  {
    ArrayList localArrayList = new ArrayList();
    paramList = paramList.iterator();
    while (paramList.hasNext())
    {
      ContentProviderHolder localContentProviderHolder = installProvider(paramContext, null, (ProviderInfo)paramList.next(), false, true, true);
      if (localContentProviderHolder != null)
      {
        noReleaseNeeded = true;
        localArrayList.add(localContentProviderHolder);
      }
    }
    try
    {
      ActivityManager.getService().publishContentProviders(getApplicationThread(), localArrayList);
      return;
    }
    catch (RemoteException paramContext)
    {
      throw paramContext.rethrowFromSystemServer();
    }
  }
  
  private ContentProviderHolder installProvider(Context paramContext, ContentProviderHolder paramContentProviderHolder, ProviderInfo paramProviderInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    Object localObject1 = null;
    Object localObject3;
    if ((paramContentProviderHolder != null) && (provider != null))
    {
      paramContext = provider;
    }
    else
    {
      if (paramBoolean1)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("Loading provider ");
        ((StringBuilder)localObject1).append(authority);
        ((StringBuilder)localObject1).append(": ");
        ((StringBuilder)localObject1).append(name);
        Slog.d("ActivityThread", ((StringBuilder)localObject1).toString());
      }
      localObject1 = null;
      ??? = applicationInfo;
      if (!paramContext.getPackageName().equals(packageName))
      {
        while ((mInitialApplication != null) && (mInitialApplication.getPackageName().equals(packageName))) {
          paramContext = mInitialApplication;
        }
        try
        {
          localObject3 = packageName;
          try
          {
            paramContext = paramContext.createPackageContext((String)localObject3, 1);
          }
          catch (PackageManager.NameNotFoundException paramContext)
          {
            paramContext = (Context)localObject1;
          }
          if (paramContext != null) {
            break label236;
          }
        }
        catch (PackageManager.NameNotFoundException paramContext)
        {
          paramContext = (Context)localObject1;
        }
      }
      paramContext = new StringBuilder();
      paramContext.append("Unable to get context for package ");
      paramContext.append(packageName);
      paramContext.append(" while loading content provider ");
      paramContext.append(name);
      Slog.w("ActivityThread", paramContext.toString());
      return null;
      label236:
      localObject1 = paramContext;
      if (splitName != null) {
        try
        {
          localObject1 = paramContext.createContextForSplit(splitName);
        }
        catch (PackageManager.NameNotFoundException paramContext)
        {
          throw new RuntimeException(paramContext);
        }
      }
    }
    for (;;)
    {
      try
      {
        localObject3 = ((Context)localObject1).getClassLoader();
        ??? = peekPackageInfo(packageName, true);
        paramContext = (Context)???;
        if (??? == null) {
          paramContext = getSystemContextmPackageInfo;
        }
        ??? = paramContext.getAppFactory().instantiateProvider((ClassLoader)localObject3, name);
        paramContext = ((ContentProvider)???).getIContentProvider();
        if (paramContext == null)
        {
          paramContext = new java/lang/StringBuilder;
          paramContext.<init>();
          paramContext.append("Failed to instantiate class ");
          paramContext.append(name);
          paramContext.append(" from sourceDir ");
          paramContext.append(applicationInfo.sourceDir);
          Slog.e("ActivityThread", paramContext.toString());
          return null;
        }
        ((ContentProvider)???).attachInfo((Context)localObject1, paramProviderInfo);
        localObject1 = ???;
      }
      catch (Exception paramContext)
      {
        if (!mInstrumentation.onException(null, paramContext)) {
          continue;
        }
        return null;
        paramContentProviderHolder = new StringBuilder();
        paramContentProviderHolder.append("Unable to get provider ");
        paramContentProviderHolder.append(name);
        paramContentProviderHolder.append(": ");
        paramContentProviderHolder.append(paramContext.toString());
        throw new RuntimeException(paramContentProviderHolder.toString(), paramContext);
      }
      synchronized (mProviderMap)
      {
        localObject3 = paramContext.asBinder();
        if (localObject1 != null)
        {
          ComponentName localComponentName = new android/content/ComponentName;
          localComponentName.<init>(packageName, name);
          paramContentProviderHolder = (ProviderClientRecord)mLocalProvidersByName.get(localComponentName);
          if (paramContentProviderHolder != null)
          {
            paramContext = mProvider;
            paramContext = paramContentProviderHolder;
          }
          else
          {
            paramContentProviderHolder = new android/app/ContentProviderHolder;
            paramContentProviderHolder.<init>(paramProviderInfo);
            provider = paramContext;
            noReleaseNeeded = true;
            paramContext = installProviderAuthoritiesLocked(paramContext, (ContentProvider)localObject1, paramContentProviderHolder);
            mLocalProviders.put(localObject3, paramContext);
            mLocalProvidersByName.put(localComponentName, paramContext);
          }
          paramContext = mHolder;
        }
        else
        {
          paramProviderInfo = (ProviderRefCount)mProviderRefCountMap.get(localObject3);
          if (paramProviderInfo != null)
          {
            paramContext = paramProviderInfo;
            if (!paramBoolean2)
            {
              incProviderRefLocked(paramProviderInfo, paramBoolean3);
              try
              {
                ActivityManager.getService().removeContentProvider(connection, paramBoolean3);
              }
              catch (RemoteException paramContext) {}
              paramContext = paramProviderInfo;
            }
          }
          else
          {
            paramProviderInfo = installProviderAuthoritiesLocked(paramContext, (ContentProvider)localObject1, paramContentProviderHolder);
            if (paramBoolean2)
            {
              paramContext = new android/app/ActivityThread$ProviderRefCount;
              paramContext.<init>(paramContentProviderHolder, paramProviderInfo, 1000, 1000);
            }
            else
            {
              if (paramBoolean3) {
                paramContext = new ProviderRefCount(paramContentProviderHolder, paramProviderInfo, 1, 0);
              } else {
                paramContext = new ProviderRefCount(paramContentProviderHolder, paramProviderInfo, 0, 1);
              }
              continue;
            }
            mProviderRefCountMap.put(localObject3, paramContext);
          }
          paramContext = holder;
        }
        return paramContext;
      }
    }
  }
  
  private ProviderClientRecord installProviderAuthoritiesLocked(IContentProvider paramIContentProvider, ContentProvider paramContentProvider, ContentProviderHolder paramContentProviderHolder)
  {
    String[] arrayOfString = info.authority.split(";");
    int i = UserHandle.getUserId(info.applicationInfo.uid);
    int j = 0;
    if (paramIContentProvider != null)
    {
      int k = arrayOfString.length;
      for (m = 0; m < k; m++)
      {
        String str = arrayOfString[m];
        n = -1;
        switch (str.hashCode())
        {
        default: 
          break;
        case 1995645513: 
          if (str.equals("com.android.blockednumber")) {
            n = 3;
          }
          break;
        case 1312704747: 
          if (str.equals("downloads")) {
            n = 5;
          }
          break;
        case 783201304: 
          if (str.equals("telephony")) {
            n = 6;
          }
          break;
        case 63943420: 
          if (str.equals("call_log_shadow")) {
            n = 2;
          }
          break;
        case -172298781: 
          if (str.equals("call_log")) {
            n = 1;
          }
          break;
        case -456066902: 
          if (str.equals("com.android.calendar")) {
            n = 4;
          }
          break;
        case -845193793: 
          if (str.equals("com.android.contacts")) {
            n = 0;
          }
          break;
        }
        switch (n)
        {
        default: 
          break;
        case 0: 
        case 1: 
        case 2: 
        case 3: 
        case 4: 
        case 5: 
        case 6: 
          Binder.allowBlocking(paramIContentProvider.asBinder());
        }
      }
    }
    paramContentProvider = new ProviderClientRecord(arrayOfString, paramIContentProvider, paramContentProvider, paramContentProviderHolder);
    int m = arrayOfString.length;
    for (int n = j; n < m; n++)
    {
      paramIContentProvider = arrayOfString[n];
      paramContentProviderHolder = new ProviderKey(paramIContentProvider, i);
      if ((ProviderClientRecord)mProviderMap.get(paramContentProviderHolder) != null)
      {
        paramContentProviderHolder = new StringBuilder();
        paramContentProviderHolder.append("Content provider ");
        paramContentProviderHolder.append(mHolder.info.name);
        paramContentProviderHolder.append(" already published as ");
        paramContentProviderHolder.append(paramIContentProvider);
        Slog.w("ActivityThread", paramContentProviderHolder.toString());
      }
      else
      {
        mProviderMap.put(paramContentProviderHolder, paramContentProvider);
      }
    }
    return paramContentProvider;
  }
  
  public static boolean isSystem()
  {
    boolean bool;
    if (sCurrentActivityThread != null) {
      bool = sCurrentActivityThreadmSystemThread;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static void main(String[] paramArrayOfString)
  {
    Trace.traceBegin(64L, "ActivityThreadMain");
    CloseGuard.setEnabled(false);
    Environment.initForCurrentUser();
    EventLogger.setReporter(new EventLoggingReporter(null));
    TrustedCertificateStore.setDefaultUserDirectory(Environment.getUserConfigDirectory(UserHandle.myUserId()));
    Process.setArgV0("<pre-initialized>");
    Looper.prepareMainLooper();
    long l1 = 0L;
    long l2 = l1;
    if (paramArrayOfString != null)
    {
      int i = paramArrayOfString.length - 1;
      for (;;)
      {
        l2 = l1;
        if (i < 0) {
          break;
        }
        l2 = l1;
        if (paramArrayOfString[i] != null)
        {
          l2 = l1;
          if (paramArrayOfString[i].startsWith("seq=")) {
            l2 = Long.parseLong(paramArrayOfString[i].substring("seq=".length()));
          }
        }
        i--;
        l1 = l2;
      }
    }
    paramArrayOfString = new ActivityThread();
    paramArrayOfString.attach(false, l2);
    if (sMainThreadHandler == null) {
      sMainThreadHandler = paramArrayOfString.getHandler();
    }
    if (Build.isPerformanceDebugging()) {
      Looper.myLooper().setMessageLogging(new LogPrinter(3, "ActivityThread"));
    }
    Trace.traceEnd(64L);
    Looper.loop();
    throw new RuntimeException("Main thread loop unexpectedly exited");
  }
  
  private native void nDumpGraphicsInfo(FileDescriptor paramFileDescriptor);
  
  private void onCoreSettingsChange()
  {
    Bundle localBundle = mCoreSettings;
    boolean bool = false;
    if (localBundle.getInt("debug_view_attributes", 0) != 0) {
      bool = true;
    }
    if (bool != View.mDebugViewAttributes)
    {
      View.mDebugViewAttributes = bool;
      relaunchAllActivities();
    }
  }
  
  private Configuration performActivityConfigurationChanged(Activity paramActivity, Configuration paramConfiguration1, Configuration paramConfiguration2, int paramInt, boolean paramBoolean)
  {
    if (paramActivity != null)
    {
      IBinder localIBinder = paramActivity.getActivityToken();
      if (localIBinder != null)
      {
        int i = 0;
        int j;
        if (mCurrentConfig == null)
        {
          j = 1;
        }
        else
        {
          int k = mCurrentConfig.diffPublicOnly(paramConfiguration1);
          if (k == 0)
          {
            j = i;
            if (mResourcesManager.isSameResourcesOverrideConfig(localIBinder, paramConfiguration2)) {}
          }
          else if ((mUpdatingSystemConfig) && ((mActivityInfo.getRealConfigChanged() & k) != 0))
          {
            j = i;
          }
          else
          {
            j = 1;
          }
        }
        if ((j == 0) && (!paramBoolean)) {
          return null;
        }
        Configuration localConfiguration = paramActivity.getOverrideConfiguration();
        paramConfiguration2 = createNewConfigAndUpdateIfNotNull(paramConfiguration2, localConfiguration);
        mResourcesManager.updateResourcesForActivity(localIBinder, paramConfiguration2, paramInt, paramBoolean);
        mConfigChangeFlags = 0;
        mCurrentConfig = new Configuration(paramConfiguration1);
        paramConfiguration1 = createNewConfigAndUpdateIfNotNull(paramConfiguration1, localConfiguration);
        if (paramBoolean) {
          paramActivity.dispatchMovedToDisplay(paramInt, paramConfiguration1);
        }
        if (j != 0)
        {
          mCalled = false;
          paramActivity.onConfigurationChanged(paramConfiguration1);
          if (!mCalled)
          {
            paramConfiguration1 = new StringBuilder();
            paramConfiguration1.append("Activity ");
            paramConfiguration1.append(paramActivity.getLocalClassName());
            paramConfiguration1.append(" did not call through to super.onConfigurationChanged()");
            throw new SuperNotCalledException(paramConfiguration1.toString());
          }
        }
        return paramConfiguration1;
      }
      throw new IllegalArgumentException("Activity token not set. Is the activity attached?");
    }
    throw new IllegalArgumentException("No activity provided.");
  }
  
  private void performConfigurationChanged(ComponentCallbacks2 paramComponentCallbacks2, Configuration paramConfiguration)
  {
    Configuration localConfiguration = null;
    if ((paramComponentCallbacks2 instanceof ContextThemeWrapper)) {
      localConfiguration = ((ContextThemeWrapper)paramComponentCallbacks2).getOverrideConfiguration();
    }
    paramComponentCallbacks2.onConfigurationChanged(createNewConfigAndUpdateIfNotNull(paramConfiguration, localConfiguration));
  }
  
  private Configuration performConfigurationChangedForActivity(ActivityClientRecord paramActivityClientRecord, Configuration paramConfiguration, int paramInt, boolean paramBoolean)
  {
    tmpConfig.setTo(paramConfiguration);
    if (overrideConfig != null) {
      tmpConfig.updateFrom(overrideConfig);
    }
    paramConfiguration = performActivityConfigurationChanged(activity, tmpConfig, overrideConfig, paramInt, paramBoolean);
    freeTextLayoutCachesIfNeeded(activity.mCurrentConfig.diff(tmpConfig));
    return paramConfiguration;
  }
  
  private void performConfigurationChangedForActivity(ActivityClientRecord paramActivityClientRecord, Configuration paramConfiguration)
  {
    performConfigurationChangedForActivity(paramActivityClientRecord, paramConfiguration, activity.getDisplay().getDisplayId(), false);
  }
  
  /* Error */
  private Activity performLaunchActivity(ActivityClientRecord paramActivityClientRecord, Intent paramIntent)
  {
    // Byte code:
    //   0: aload_1
    //   1: getfield 825	android/app/ActivityThread$ActivityClientRecord:activityInfo	Landroid/content/pm/ActivityInfo;
    //   4: astore_3
    //   5: aload_1
    //   6: getfield 821	android/app/ActivityThread$ActivityClientRecord:packageInfo	Landroid/app/LoadedApk;
    //   9: ifnonnull +20 -> 29
    //   12: aload_1
    //   13: aload_0
    //   14: aload_3
    //   15: getfield 2267	android/content/pm/ActivityInfo:applicationInfo	Landroid/content/pm/ApplicationInfo;
    //   18: aload_1
    //   19: getfield 2910	android/app/ActivityThread$ActivityClientRecord:compatInfo	Landroid/content/res/CompatibilityInfo;
    //   22: iconst_1
    //   23: invokevirtual 2913	android/app/ActivityThread:getPackageInfo	(Landroid/content/pm/ApplicationInfo;Landroid/content/res/CompatibilityInfo;I)Landroid/app/LoadedApk;
    //   26: putfield 821	android/app/ActivityThread$ActivityClientRecord:packageInfo	Landroid/app/LoadedApk;
    //   29: aload_1
    //   30: getfield 740	android/app/ActivityThread$ActivityClientRecord:intent	Landroid/content/Intent;
    //   33: invokevirtual 746	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   36: astore 4
    //   38: aload 4
    //   40: astore_3
    //   41: aload 4
    //   43: ifnonnull +27 -> 70
    //   46: aload_1
    //   47: getfield 740	android/app/ActivityThread$ActivityClientRecord:intent	Landroid/content/Intent;
    //   50: aload_0
    //   51: getfield 611	android/app/ActivityThread:mInitialApplication	Landroid/app/Application;
    //   54: invokevirtual 2916	android/app/Application:getPackageManager	()Landroid/content/pm/PackageManager;
    //   57: invokevirtual 2920	android/content/Intent:resolveActivity	(Landroid/content/pm/PackageManager;)Landroid/content/ComponentName;
    //   60: astore_3
    //   61: aload_1
    //   62: getfield 740	android/app/ActivityThread$ActivityClientRecord:intent	Landroid/content/Intent;
    //   65: aload_3
    //   66: invokevirtual 2924	android/content/Intent:setComponent	(Landroid/content/ComponentName;)Landroid/content/Intent;
    //   69: pop
    //   70: aload_3
    //   71: astore 4
    //   73: aload_1
    //   74: getfield 825	android/app/ActivityThread$ActivityClientRecord:activityInfo	Landroid/content/pm/ActivityInfo;
    //   77: getfield 2927	android/content/pm/ActivityInfo:targetActivity	Ljava/lang/String;
    //   80: ifnull +26 -> 106
    //   83: new 748	android/content/ComponentName
    //   86: dup
    //   87: aload_1
    //   88: getfield 825	android/app/ActivityThread$ActivityClientRecord:activityInfo	Landroid/content/pm/ActivityInfo;
    //   91: getfield 2928	android/content/pm/ActivityInfo:packageName	Ljava/lang/String;
    //   94: aload_1
    //   95: getfield 825	android/app/ActivityThread$ActivityClientRecord:activityInfo	Landroid/content/pm/ActivityInfo;
    //   98: getfield 2927	android/content/pm/ActivityInfo:targetActivity	Ljava/lang/String;
    //   101: invokespecial 1866	android/content/ComponentName:<init>	(Ljava/lang/String;Ljava/lang/String;)V
    //   104: astore 4
    //   106: aload_0
    //   107: aload_1
    //   108: invokespecial 2930	android/app/ActivityThread:createBaseContextForActivity	(Landroid/app/ActivityThread$ActivityClientRecord;)Landroid/app/ContextImpl;
    //   111: astore 5
    //   113: aconst_null
    //   114: astore 6
    //   116: aload 6
    //   118: astore_3
    //   119: aload 5
    //   121: invokevirtual 1846	android/app/ContextImpl:getClassLoader	()Ljava/lang/ClassLoader;
    //   124: astore 7
    //   126: aload 6
    //   128: astore_3
    //   129: aload_0
    //   130: getfield 586	android/app/ActivityThread:mInstrumentation	Landroid/app/Instrumentation;
    //   133: aload 7
    //   135: aload 4
    //   137: invokevirtual 1851	android/content/ComponentName:getClassName	()Ljava/lang/String;
    //   140: aload_1
    //   141: getfield 740	android/app/ActivityThread$ActivityClientRecord:intent	Landroid/content/Intent;
    //   144: invokevirtual 2934	android/app/Instrumentation:newActivity	(Ljava/lang/ClassLoader;Ljava/lang/String;Landroid/content/Intent;)Landroid/app/Activity;
    //   147: astore 6
    //   149: aload 6
    //   151: astore_3
    //   152: aload 6
    //   154: invokevirtual 797	java/lang/Object:getClass	()Ljava/lang/Class;
    //   157: invokestatic 2938	android/os/StrictMode:incrementExpectedActivityCount	(Ljava/lang/Class;)V
    //   160: aload 6
    //   162: astore_3
    //   163: aload_1
    //   164: getfield 740	android/app/ActivityThread$ActivityClientRecord:intent	Landroid/content/Intent;
    //   167: aload 7
    //   169: invokevirtual 957	android/content/Intent:setExtrasClassLoader	(Ljava/lang/ClassLoader;)V
    //   172: aload 6
    //   174: astore_3
    //   175: aload_1
    //   176: getfield 740	android/app/ActivityThread$ActivityClientRecord:intent	Landroid/content/Intent;
    //   179: invokevirtual 958	android/content/Intent:prepareToEnterProcess	()V
    //   182: aload 6
    //   184: astore_3
    //   185: aload_1
    //   186: getfield 680	android/app/ActivityThread$ActivityClientRecord:state	Landroid/os/Bundle;
    //   189: ifnull +15 -> 204
    //   192: aload 6
    //   194: astore_3
    //   195: aload_1
    //   196: getfield 680	android/app/ActivityThread$ActivityClientRecord:state	Landroid/os/Bundle;
    //   199: aload 7
    //   201: invokevirtual 2941	android/os/Bundle:setClassLoader	(Ljava/lang/ClassLoader;)V
    //   204: aload 6
    //   206: astore_3
    //   207: goto +18 -> 225
    //   210: astore 6
    //   212: aload_0
    //   213: getfield 586	android/app/ActivityThread:mInstrumentation	Landroid/app/Instrumentation;
    //   216: aload_3
    //   217: aload 6
    //   219: invokevirtual 731	android/app/Instrumentation:onException	(Ljava/lang/Object;Ljava/lang/Throwable;)Z
    //   222: ifeq +559 -> 781
    //   225: aload_1
    //   226: getfield 821	android/app/ActivityThread$ActivityClientRecord:packageInfo	Landroid/app/LoadedApk;
    //   229: astore 6
    //   231: aload 6
    //   233: iconst_0
    //   234: aload_0
    //   235: getfield 586	android/app/ActivityThread:mInstrumentation	Landroid/app/Instrumentation;
    //   238: invokevirtual 609	android/app/LoadedApk:makeApplication	(ZLandroid/app/Instrumentation;)Landroid/app/Application;
    //   241: astore 8
    //   243: aload_3
    //   244: ifnull +433 -> 677
    //   247: aload_1
    //   248: getfield 825	android/app/ActivityThread$ActivityClientRecord:activityInfo	Landroid/content/pm/ActivityInfo;
    //   251: aload 5
    //   253: invokevirtual 2942	android/app/ContextImpl:getPackageManager	()Landroid/content/pm/PackageManager;
    //   256: invokevirtual 2946	android/content/pm/ActivityInfo:loadLabel	(Landroid/content/pm/PackageManager;)Ljava/lang/CharSequence;
    //   259: astore 9
    //   261: new 358	android/content/res/Configuration
    //   264: astore 10
    //   266: aload 10
    //   268: aload_0
    //   269: getfield 1454	android/app/ActivityThread:mCompatConfiguration	Landroid/content/res/Configuration;
    //   272: invokespecial 878	android/content/res/Configuration:<init>	(Landroid/content/res/Configuration;)V
    //   275: aload_1
    //   276: getfield 828	android/app/ActivityThread$ActivityClientRecord:overrideConfig	Landroid/content/res/Configuration;
    //   279: astore 6
    //   281: aload 6
    //   283: ifnull +24 -> 307
    //   286: aload 10
    //   288: aload_1
    //   289: getfield 828	android/app/ActivityThread$ActivityClientRecord:overrideConfig	Landroid/content/res/Configuration;
    //   292: invokevirtual 882	android/content/res/Configuration:updateFrom	(Landroid/content/res/Configuration;)I
    //   295: pop
    //   296: goto +11 -> 307
    //   299: astore_1
    //   300: goto +411 -> 711
    //   303: astore_1
    //   304: goto +475 -> 779
    //   307: aconst_null
    //   308: astore 7
    //   310: aload_1
    //   311: getfield 767	android/app/ActivityThread$ActivityClientRecord:mPendingRemoveWindow	Landroid/view/Window;
    //   314: astore 11
    //   316: aload 7
    //   318: astore 6
    //   320: aload 11
    //   322: ifnull +30 -> 352
    //   325: aload 7
    //   327: astore 6
    //   329: aload_1
    //   330: getfield 763	android/app/ActivityThread$ActivityClientRecord:mPreserveWindow	Z
    //   333: ifeq +19 -> 352
    //   336: aload_1
    //   337: getfield 767	android/app/ActivityThread$ActivityClientRecord:mPendingRemoveWindow	Landroid/view/Window;
    //   340: astore 6
    //   342: aload_1
    //   343: aconst_null
    //   344: putfield 767	android/app/ActivityThread$ActivityClientRecord:mPendingRemoveWindow	Landroid/view/Window;
    //   347: aload_1
    //   348: aconst_null
    //   349: putfield 771	android/app/ActivityThread$ActivityClientRecord:mPendingRemoveWindowManager	Landroid/view/WindowManager;
    //   352: aload 5
    //   354: aload_3
    //   355: invokevirtual 2096	android/app/ContextImpl:setOuterContext	(Landroid/content/Context;)V
    //   358: aload_0
    //   359: invokevirtual 2950	android/app/ActivityThread:getInstrumentation	()Landroid/app/Instrumentation;
    //   362: astore 12
    //   364: aload_1
    //   365: getfield 814	android/app/ActivityThread$ActivityClientRecord:token	Landroid/os/IBinder;
    //   368: astore 11
    //   370: aload_1
    //   371: getfield 2953	android/app/ActivityThread$ActivityClientRecord:ident	I
    //   374: istore 13
    //   376: aload_1
    //   377: getfield 740	android/app/ActivityThread$ActivityClientRecord:intent	Landroid/content/Intent;
    //   380: astore 14
    //   382: aload_1
    //   383: getfield 825	android/app/ActivityThread$ActivityClientRecord:activityInfo	Landroid/content/pm/ActivityInfo;
    //   386: astore 15
    //   388: aload_1
    //   389: getfield 2956	android/app/ActivityThread$ActivityClientRecord:parent	Landroid/app/Activity;
    //   392: astore 16
    //   394: aload_1
    //   395: getfield 2959	android/app/ActivityThread$ActivityClientRecord:embeddedID	Ljava/lang/String;
    //   398: astore 7
    //   400: aload_1
    //   401: getfield 2963	android/app/ActivityThread$ActivityClientRecord:lastNonConfigurationInstances	Landroid/app/Activity$NonConfigurationInstances;
    //   404: astore 17
    //   406: aload_1
    //   407: getfield 2966	android/app/ActivityThread$ActivityClientRecord:referrer	Ljava/lang/String;
    //   410: astore 18
    //   412: aload_1
    //   413: getfield 2251	android/app/ActivityThread$ActivityClientRecord:voiceInteractor	Lcom/android/internal/app/IVoiceInteractor;
    //   416: astore 19
    //   418: aload_1
    //   419: getfield 2970	android/app/ActivityThread$ActivityClientRecord:configCallback	Landroid/view/ViewRootImpl$ActivityConfigCallback;
    //   422: astore 20
    //   424: aload_3
    //   425: aload 5
    //   427: aload_0
    //   428: aload 12
    //   430: aload 11
    //   432: iload 13
    //   434: aload 8
    //   436: aload 14
    //   438: aload 15
    //   440: aload 9
    //   442: aload 16
    //   444: aload 7
    //   446: aload 17
    //   448: aload 10
    //   450: aload 18
    //   452: aload 19
    //   454: aload 6
    //   456: aload 20
    //   458: invokevirtual 2973	android/app/Activity:attach	(Landroid/content/Context;Landroid/app/ActivityThread;Landroid/app/Instrumentation;Landroid/os/IBinder;ILandroid/app/Application;Landroid/content/Intent;Landroid/content/pm/ActivityInfo;Ljava/lang/CharSequence;Landroid/app/Activity;Ljava/lang/String;Landroid/app/Activity$NonConfigurationInstances;Landroid/content/res/Configuration;Ljava/lang/String;Lcom/android/internal/app/IVoiceInteractor;Landroid/view/Window;Landroid/view/ViewRootImpl$ActivityConfigCallback;)V
    //   461: aload_2
    //   462: ifnull +19 -> 481
    //   465: aload_3
    //   466: aload_2
    //   467: putfield 2350	android/app/Activity:mIntent	Landroid/content/Intent;
    //   470: goto +11 -> 481
    //   473: astore_1
    //   474: goto +237 -> 711
    //   477: astore_1
    //   478: goto +301 -> 779
    //   481: aload_3
    //   482: astore 6
    //   484: aload_1
    //   485: astore_2
    //   486: aload_2
    //   487: aconst_null
    //   488: putfield 2963	android/app/ActivityThread$ActivityClientRecord:lastNonConfigurationInstances	Landroid/app/Activity$NonConfigurationInstances;
    //   491: aload_0
    //   492: invokespecial 2975	android/app/ActivityThread:checkAndBlockForNetworkAccess	()V
    //   495: aload 6
    //   497: iconst_0
    //   498: putfield 2978	android/app/Activity:mStartedActivity	Z
    //   501: aload_2
    //   502: getfield 825	android/app/ActivityThread$ActivityClientRecord:activityInfo	Landroid/content/pm/ActivityInfo;
    //   505: invokevirtual 2981	android/content/pm/ActivityInfo:getThemeResource	()I
    //   508: istore 13
    //   510: iload 13
    //   512: ifeq +10 -> 522
    //   515: aload 6
    //   517: iload 13
    //   519: invokevirtual 2984	android/app/Activity:setTheme	(I)V
    //   522: aload 6
    //   524: iconst_0
    //   525: putfield 2861	android/app/Activity:mCalled	Z
    //   528: aload_1
    //   529: invokevirtual 688	android/app/ActivityThread$ActivityClientRecord:isPersistable	()Z
    //   532: istore 21
    //   534: iload 21
    //   536: ifeq +23 -> 559
    //   539: aload_0
    //   540: getfield 586	android/app/ActivityThread:mInstrumentation	Landroid/app/Instrumentation;
    //   543: aload 6
    //   545: aload_2
    //   546: getfield 680	android/app/ActivityThread$ActivityClientRecord:state	Landroid/os/Bundle;
    //   549: aload_2
    //   550: getfield 695	android/app/ActivityThread$ActivityClientRecord:persistentState	Landroid/os/PersistableBundle;
    //   553: invokevirtual 2987	android/app/Instrumentation:callActivityOnCreate	(Landroid/app/Activity;Landroid/os/Bundle;Landroid/os/PersistableBundle;)V
    //   556: goto +16 -> 572
    //   559: aload_0
    //   560: getfield 586	android/app/ActivityThread:mInstrumentation	Landroid/app/Instrumentation;
    //   563: aload 6
    //   565: aload_2
    //   566: getfield 680	android/app/ActivityThread$ActivityClientRecord:state	Landroid/os/Bundle;
    //   569: invokevirtual 2989	android/app/Instrumentation:callActivityOnCreate	(Landroid/app/Activity;Landroid/os/Bundle;)V
    //   572: aload 6
    //   574: getfield 2861	android/app/Activity:mCalled	Z
    //   577: ifeq +12 -> 589
    //   580: aload_2
    //   581: aload 6
    //   583: putfield 699	android/app/ActivityThread$ActivityClientRecord:activity	Landroid/app/Activity;
    //   586: goto +91 -> 677
    //   589: new 709	android/util/SuperNotCalledException
    //   592: astore 6
    //   594: new 632	java/lang/StringBuilder
    //   597: astore_1
    //   598: aload_1
    //   599: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   602: aload_1
    //   603: ldc_w 2866
    //   606: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   609: pop
    //   610: aload_1
    //   611: aload_2
    //   612: getfield 740	android/app/ActivityThread$ActivityClientRecord:intent	Landroid/content/Intent;
    //   615: invokevirtual 746	android/content/Intent:getComponent	()Landroid/content/ComponentName;
    //   618: invokevirtual 751	android/content/ComponentName:toShortString	()Ljava/lang/String;
    //   621: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   624: pop
    //   625: aload_1
    //   626: ldc_w 2991
    //   629: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   632: pop
    //   633: aload 6
    //   635: aload_1
    //   636: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   639: invokespecial 2872	android/util/SuperNotCalledException:<init>	(Ljava/lang/String;)V
    //   642: aload 6
    //   644: athrow
    //   645: astore_1
    //   646: goto +65 -> 711
    //   649: astore_1
    //   650: goto +53 -> 703
    //   653: astore_1
    //   654: goto +57 -> 711
    //   657: astore_1
    //   658: goto +121 -> 779
    //   661: astore_1
    //   662: goto +8 -> 670
    //   665: astore_1
    //   666: goto +8 -> 674
    //   669: astore_1
    //   670: goto +41 -> 711
    //   673: astore_1
    //   674: goto +105 -> 779
    //   677: aload_1
    //   678: iconst_1
    //   679: invokevirtual 734	android/app/ActivityThread$ActivityClientRecord:setState	(I)V
    //   682: aload_0
    //   683: getfield 281	android/app/ActivityThread:mActivities	Landroid/util/ArrayMap;
    //   686: aload_1
    //   687: getfield 814	android/app/ActivityThread$ActivityClientRecord:token	Landroid/os/IBinder;
    //   690: aload_1
    //   691: invokevirtual 1312	android/util/ArrayMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   694: pop
    //   695: goto +28 -> 723
    //   698: astore_1
    //   699: goto +12 -> 711
    //   702: astore_1
    //   703: goto +76 -> 779
    //   706: astore_1
    //   707: goto +72 -> 779
    //   710: astore_1
    //   711: aload_0
    //   712: getfield 586	android/app/ActivityThread:mInstrumentation	Landroid/app/Instrumentation;
    //   715: aload_3
    //   716: aload_1
    //   717: invokevirtual 731	android/app/Instrumentation:onException	(Ljava/lang/Object;Ljava/lang/Throwable;)Z
    //   720: ifeq +5 -> 725
    //   723: aload_3
    //   724: areturn
    //   725: new 632	java/lang/StringBuilder
    //   728: dup
    //   729: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   732: astore_2
    //   733: aload_2
    //   734: ldc_w 2993
    //   737: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   740: pop
    //   741: aload_2
    //   742: aload 4
    //   744: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   747: pop
    //   748: aload_2
    //   749: ldc_w 753
    //   752: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   755: pop
    //   756: aload_2
    //   757: aload_1
    //   758: invokevirtual 643	java/lang/Exception:toString	()Ljava/lang/String;
    //   761: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   764: pop
    //   765: new 645	java/lang/RuntimeException
    //   768: dup
    //   769: aload_2
    //   770: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   773: aload_1
    //   774: invokespecial 649	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   777: athrow
    //   778: astore_1
    //   779: aload_1
    //   780: athrow
    //   781: new 632	java/lang/StringBuilder
    //   784: dup
    //   785: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   788: astore_1
    //   789: aload_1
    //   790: ldc_w 2995
    //   793: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   796: pop
    //   797: aload_1
    //   798: aload 4
    //   800: invokevirtual 664	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   803: pop
    //   804: aload_1
    //   805: ldc_w 753
    //   808: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   811: pop
    //   812: aload_1
    //   813: aload 6
    //   815: invokevirtual 643	java/lang/Exception:toString	()Ljava/lang/String;
    //   818: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   821: pop
    //   822: new 645	java/lang/RuntimeException
    //   825: dup
    //   826: aload_1
    //   827: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   830: aload 6
    //   832: invokespecial 649	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   835: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	836	0	this	ActivityThread
    //   0	836	1	paramActivityClientRecord	ActivityClientRecord
    //   0	836	2	paramIntent	Intent
    //   4	720	3	localObject1	Object
    //   36	763	4	localObject2	Object
    //   111	315	5	localContextImpl	ContextImpl
    //   114	91	6	localActivity1	Activity
    //   210	8	6	localException	Exception
    //   229	602	6	localObject3	Object
    //   124	321	7	localObject4	Object
    //   241	194	8	localApplication	Application
    //   259	182	9	localCharSequence	CharSequence
    //   264	185	10	localConfiguration	Configuration
    //   314	117	11	localObject5	Object
    //   362	67	12	localInstrumentation	Instrumentation
    //   374	144	13	i	int
    //   380	57	14	localIntent	Intent
    //   386	53	15	localActivityInfo	ActivityInfo
    //   392	51	16	localActivity2	Activity
    //   404	43	17	localNonConfigurationInstances	Activity.NonConfigurationInstances
    //   410	41	18	str	String
    //   416	37	19	localIVoiceInteractor	IVoiceInteractor
    //   422	35	20	localActivityConfigCallback	ViewRootImpl.ActivityConfigCallback
    //   532	3	21	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   119	126	210	java/lang/Exception
    //   129	149	210	java/lang/Exception
    //   152	160	210	java/lang/Exception
    //   163	172	210	java/lang/Exception
    //   175	182	210	java/lang/Exception
    //   185	192	210	java/lang/Exception
    //   195	204	210	java/lang/Exception
    //   286	296	299	java/lang/Exception
    //   329	352	299	java/lang/Exception
    //   286	296	303	android/util/SuperNotCalledException
    //   329	352	303	android/util/SuperNotCalledException
    //   465	470	473	java/lang/Exception
    //   465	470	477	android/util/SuperNotCalledException
    //   486	510	645	java/lang/Exception
    //   515	522	645	java/lang/Exception
    //   522	534	645	java/lang/Exception
    //   486	510	649	android/util/SuperNotCalledException
    //   515	522	649	android/util/SuperNotCalledException
    //   522	534	649	android/util/SuperNotCalledException
    //   424	461	653	java/lang/Exception
    //   424	461	657	android/util/SuperNotCalledException
    //   394	424	661	java/lang/Exception
    //   394	424	665	android/util/SuperNotCalledException
    //   388	394	669	java/lang/Exception
    //   388	394	673	android/util/SuperNotCalledException
    //   539	556	698	java/lang/Exception
    //   559	572	698	java/lang/Exception
    //   572	586	698	java/lang/Exception
    //   589	645	698	java/lang/Exception
    //   677	695	698	java/lang/Exception
    //   539	556	702	android/util/SuperNotCalledException
    //   559	572	702	android/util/SuperNotCalledException
    //   572	586	702	android/util/SuperNotCalledException
    //   589	645	702	android/util/SuperNotCalledException
    //   677	695	702	android/util/SuperNotCalledException
    //   231	243	706	android/util/SuperNotCalledException
    //   247	281	706	android/util/SuperNotCalledException
    //   310	316	706	android/util/SuperNotCalledException
    //   352	388	706	android/util/SuperNotCalledException
    //   225	231	710	java/lang/Exception
    //   231	243	710	java/lang/Exception
    //   247	281	710	java/lang/Exception
    //   310	316	710	java/lang/Exception
    //   352	388	710	java/lang/Exception
    //   225	231	778	android/util/SuperNotCalledException
  }
  
  private Bundle performPauseActivity(ActivityClientRecord paramActivityClientRecord, boolean paramBoolean, String arg3, PendingTransactionActions paramPendingTransactionActions)
  {
    boolean bool = paused;
    Object localObject1 = null;
    Object localObject2;
    if (bool)
    {
      if (activity.mFinished) {
        return null;
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append("Performing pause of activity that is not resumed: ");
      ((StringBuilder)localObject2).append(intent.getComponent().toShortString());
      localObject2 = new RuntimeException(((StringBuilder)localObject2).toString());
      Slog.e("ActivityThread", ((RuntimeException)localObject2).getMessage(), (Throwable)localObject2);
    }
    int i = 1;
    if (paramBoolean) {
      activity.mFinished = true;
    }
    paramBoolean = activity.mFinished;
    int j = 0;
    if ((paramBoolean) || (!paramActivityClientRecord.isPreHoneycomb())) {
      i = 0;
    }
    if (i != 0) {
      callActivityOnSaveInstanceState(paramActivityClientRecord);
    }
    performPauseActivityIfNeeded(paramActivityClientRecord, ???);
    synchronized (mOnPauseListeners)
    {
      localObject2 = (ArrayList)mOnPauseListeners.remove(activity);
      int k;
      if (localObject2 != null) {
        k = ((ArrayList)localObject2).size();
      } else {
        k = 0;
      }
      while (j < k)
      {
        ((OnActivityPausedListener)((ArrayList)localObject2).get(j)).onPaused(activity);
        j++;
      }
      if (paramPendingTransactionActions != null) {
        ??? = paramPendingTransactionActions.getOldState();
      } else {
        ??? = null;
      }
      if ((??? != null) && (paramActivityClientRecord.isPreHoneycomb())) {
        state = ???;
      }
      ??? = localObject1;
      if (i != 0) {
        ??? = state;
      }
      return ???;
    }
  }
  
  private void performPauseActivityIfNeeded(ActivityClientRecord paramActivityClientRecord, String paramString)
  {
    if (paused) {
      return;
    }
    try
    {
      activity.mCalled = false;
      mInstrumentation.callActivityOnPause(activity);
      if (!activity.mCalled)
      {
        localObject = new android/util/SuperNotCalledException;
        paramString = new java/lang/StringBuilder;
        paramString.<init>();
        paramString.append("Activity ");
        paramString.append(safeToComponentShortString(intent));
        paramString.append(" did not call through to super.onPause()");
        ((SuperNotCalledException)localObject).<init>(paramString.toString());
        throw ((Throwable)localObject);
      }
    }
    catch (Exception paramString)
    {
      if (mInstrumentation.onException(activity, paramString))
      {
        paramActivityClientRecord.setState(4);
        if ((Build.FEATURES.ENABLE_NOTCH_UI) && (paramActivityClientRecord == mLastResumedActivity)) {
          mLastResumedActivity = null;
        }
        return;
      }
      Object localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unable to pause activity ");
      ((StringBuilder)localObject).append(safeToComponentShortString(intent));
      ((StringBuilder)localObject).append(": ");
      ((StringBuilder)localObject).append(paramString.toString());
      throw new RuntimeException(((StringBuilder)localObject).toString(), paramString);
    }
    catch (SuperNotCalledException paramActivityClientRecord)
    {
      throw paramActivityClientRecord;
    }
  }
  
  private void performStopActivityInner(ActivityClientRecord paramActivityClientRecord, PendingTransactionActions.StopInfo paramStopInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString)
  {
    if (paramActivityClientRecord != null)
    {
      if ((!paramBoolean1) && (stopped))
      {
        if (activity.mFinished) {
          return;
        }
        if (!paramBoolean3)
        {
          Object localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Performing stop of activity that is already stopped: ");
          ((StringBuilder)localObject).append(intent.getComponent().toShortString());
          localObject = new RuntimeException(((StringBuilder)localObject).toString());
          Slog.e("ActivityThread", ((RuntimeException)localObject).getMessage(), (Throwable)localObject);
          Slog.e("ActivityThread", paramActivityClientRecord.getStateString());
        }
      }
      performPauseActivityIfNeeded(paramActivityClientRecord, paramString);
      if (paramStopInfo != null) {
        try
        {
          paramStopInfo.setDescription(activity.onCreateDescription());
        }
        catch (Exception paramStopInfo)
        {
          if (!mInstrumentation.onException(activity, paramStopInfo))
          {
            paramString = new StringBuilder();
            paramString.append("Unable to save state of activity ");
            paramString.append(intent.getComponent().toShortString());
            paramString.append(": ");
            paramString.append(paramStopInfo.toString());
            throw new RuntimeException(paramString.toString(), paramStopInfo);
          }
        }
      }
      if (!paramBoolean1) {
        callActivityOnStop(paramActivityClientRecord, paramBoolean2, paramString);
      }
    }
  }
  
  static void printRow(PrintWriter paramPrintWriter, String paramString, Object... paramVarArgs)
  {
    paramPrintWriter.println(String.format(paramString, paramVarArgs));
  }
  
  private void relaunchAllActivities()
  {
    Iterator localIterator = mActivities.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      if (!getValueactivity.mFinished) {
        scheduleRelaunchActivity((IBinder)localEntry.getKey());
      }
    }
  }
  
  private void reportSizeConfigurations(ActivityClientRecord paramActivityClientRecord)
  {
    Configuration[] arrayOfConfiguration = activity.getResources().getSizeConfigurations();
    if (arrayOfConfiguration == null) {
      return;
    }
    SparseIntArray localSparseIntArray1 = new SparseIntArray();
    SparseIntArray localSparseIntArray2 = new SparseIntArray();
    SparseIntArray localSparseIntArray3 = new SparseIntArray();
    for (int i = arrayOfConfiguration.length - 1; i >= 0; i--)
    {
      Configuration localConfiguration = arrayOfConfiguration[i];
      if (screenHeightDp != 0) {
        localSparseIntArray2.put(screenHeightDp, 0);
      }
      if (screenWidthDp != 0) {
        localSparseIntArray1.put(screenWidthDp, 0);
      }
      if (smallestScreenWidthDp != 0) {
        localSparseIntArray3.put(smallestScreenWidthDp, 0);
      }
    }
    try
    {
      ActivityManager.getService().reportSizeConfigurations(token, localSparseIntArray1.copyKeys(), localSparseIntArray2.copyKeys(), localSparseIntArray3.copyKeys());
      return;
    }
    catch (RemoteException paramActivityClientRecord)
    {
      throw paramActivityClientRecord.rethrowFromSystemServer();
    }
  }
  
  private static String safeToComponentShortString(Intent paramIntent)
  {
    paramIntent = paramIntent.getComponent();
    if (paramIntent == null) {
      paramIntent = "[Unknown]";
    } else {
      paramIntent = paramIntent.toShortString();
    }
    return paramIntent;
  }
  
  private void sendMessage(int paramInt1, Object paramObject, int paramInt2)
  {
    sendMessage(paramInt1, paramObject, paramInt2, 0, false);
  }
  
  private void sendMessage(int paramInt1, Object paramObject, int paramInt2, int paramInt3)
  {
    sendMessage(paramInt1, paramObject, paramInt2, paramInt3, false);
  }
  
  private void sendMessage(int paramInt1, Object paramObject, int paramInt2, int paramInt3, int paramInt4)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    SomeArgs localSomeArgs = SomeArgs.obtain();
    arg1 = paramObject;
    argi1 = paramInt2;
    argi2 = paramInt3;
    argi3 = paramInt4;
    obj = localSomeArgs;
    mH.sendMessage(localMessage);
  }
  
  private void sendMessage(int paramInt1, Object paramObject, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    Message localMessage = Message.obtain();
    what = paramInt1;
    obj = paramObject;
    arg1 = paramInt2;
    arg2 = paramInt3;
    if (paramBoolean) {
      localMessage.setAsynchronous(true);
    }
    mH.sendMessage(localMessage);
  }
  
  private void setupGraphicsSupport(Context paramContext)
  {
    Trace.traceBegin(64L, "setupGraphicsSupport");
    if (!"android".equals(paramContext.getPackageName()))
    {
      File localFile = paramContext.getCacheDir();
      if (localFile != null) {
        System.setProperty("java.io.tmpdir", localFile.getAbsolutePath());
      } else {
        Log.v("ActivityThread", "Unable to initialize \"java.io.tmpdir\" property due to missing cache directory");
      }
      localFile = paramContext.createDeviceProtectedStorageContext().getCodeCacheDir();
      if (localFile != null) {
        try
        {
          int i = Process.myUid();
          if (getPackageManager().getPackagesForUid(i) != null)
          {
            ThreadedRenderer.setupDiskCache(localFile);
            RenderScriptCacheDir.setupDiskCache(localFile);
          }
        }
        catch (RemoteException paramContext)
        {
          Trace.traceEnd(64L);
          throw paramContext.rethrowFromSystemServer();
        }
      } else {
        Log.w("ActivityThread", "Unable to use shader/script cache: missing code-cache directory");
      }
    }
    GraphicsEnvironment.getInstance().setup(paramContext);
    Trace.traceEnd(64L);
  }
  
  public static ActivityThread systemMain()
  {
    if (!ActivityManager.isHighEndGfx()) {
      ThreadedRenderer.disable(true);
    } else {
      ThreadedRenderer.enableForegroundTrimming();
    }
    ActivityThread localActivityThread = new ActivityThread();
    localActivityThread.attach(true, 0L);
    return localActivityThread;
  }
  
  private void updateDefaultDensity()
  {
    int i = mCurDefaultDisplayDpi;
    if ((!mDensityCompatMode) && (i != 0) && (i != DisplayMetrics.DENSITY_DEVICE))
    {
      DisplayMetrics.DENSITY_DEVICE = i;
      Bitmap.setDefaultDensity(i);
    }
  }
  
  private void updateLocaleListFromAppContext(Context paramContext, LocaleList paramLocaleList)
  {
    paramContext = paramContext.getResources().getConfiguration().getLocales();
    int i = 0;
    paramContext = paramContext.get(0);
    int j = paramLocaleList.size();
    while (i < j)
    {
      if (paramContext.equals(paramLocaleList.get(i)))
      {
        LocaleList.setDefault(paramLocaleList, i);
        return;
      }
      i++;
    }
    LocaleList.setDefault(new LocaleList(paramContext, paramLocaleList));
  }
  
  private void updateVisibility(ActivityClientRecord paramActivityClientRecord, boolean paramBoolean)
  {
    View localView = activity.mDecor;
    if (localView != null) {
      if (paramBoolean)
      {
        if (!activity.mVisibleFromServer)
        {
          activity.mVisibleFromServer = true;
          mNumVisibleActivities += 1;
          if (activity.mVisibleFromClient) {
            activity.makeVisible();
          }
        }
        if (newConfig != null)
        {
          performConfigurationChangedForActivity(paramActivityClientRecord, newConfig);
          newConfig = null;
        }
      }
      else if (activity.mVisibleFromServer)
      {
        activity.mVisibleFromServer = false;
        mNumVisibleActivities -= 1;
        localView.setVisibility(4);
      }
    }
  }
  
  public final IContentProvider acquireExistingProvider(Context arg1, String paramString, int paramInt, boolean paramBoolean)
  {
    synchronized (mProviderMap)
    {
      Object localObject1 = new android/app/ActivityThread$ProviderKey;
      ((ProviderKey)localObject1).<init>(paramString, paramInt);
      localObject1 = (ProviderClientRecord)mProviderMap.get(localObject1);
      if (localObject1 == null) {
        return null;
      }
      Object localObject2 = mProvider;
      localObject1 = ((IContentProvider)localObject2).asBinder();
      if (!((IBinder)localObject1).isBinderAlive())
      {
        localObject2 = new java/lang/StringBuilder;
        ((StringBuilder)localObject2).<init>();
        ((StringBuilder)localObject2).append("Acquiring provider ");
        ((StringBuilder)localObject2).append(paramString);
        ((StringBuilder)localObject2).append(" for user ");
        ((StringBuilder)localObject2).append(paramInt);
        ((StringBuilder)localObject2).append(": existing object's process dead");
        Log.i("ActivityThread", ((StringBuilder)localObject2).toString());
        handleUnstableProviderDiedLocked((IBinder)localObject1, true);
        return null;
      }
      paramString = (ProviderRefCount)mProviderRefCountMap.get(localObject1);
      if (paramString != null) {
        incProviderRefLocked(paramString, paramBoolean);
      }
      return localObject2;
    }
  }
  
  /* Error */
  public final IContentProvider acquireProvider(Context paramContext, String paramString, int paramInt, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: aload_2
    //   3: iload_3
    //   4: iload 4
    //   6: invokevirtual 3248	android/app/ActivityThread:acquireExistingProvider	(Landroid/content/Context;Ljava/lang/String;IZ)Landroid/content/IContentProvider;
    //   9: astore 5
    //   11: aload 5
    //   13: ifnull +6 -> 19
    //   16: aload 5
    //   18: areturn
    //   19: aload_0
    //   20: aload_2
    //   21: iload_3
    //   22: invokespecial 3250	android/app/ActivityThread:getGetProviderLock	(Ljava/lang/String;I)Ljava/lang/Object;
    //   25: astore 5
    //   27: aload 5
    //   29: monitorenter
    //   30: invokestatic 561	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   33: astore 6
    //   35: aload_0
    //   36: invokevirtual 2631	android/app/ActivityThread:getApplicationThread	()Landroid/app/ActivityThread$ApplicationThread;
    //   39: astore 7
    //   41: aload 6
    //   43: aload 7
    //   45: aload_2
    //   46: iload_3
    //   47: iload 4
    //   49: invokeinterface 3254 5 0
    //   54: astore 7
    //   56: aload 5
    //   58: monitorexit
    //   59: aload 7
    //   61: ifnonnull +37 -> 98
    //   64: new 632	java/lang/StringBuilder
    //   67: dup
    //   68: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   71: astore_1
    //   72: aload_1
    //   73: ldc_w 3256
    //   76: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: pop
    //   80: aload_1
    //   81: aload_2
    //   82: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: ldc 124
    //   88: aload_1
    //   89: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   92: invokestatic 672	android/util/Slog:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   95: pop
    //   96: aconst_null
    //   97: areturn
    //   98: aload_0
    //   99: aload_1
    //   100: aload 7
    //   102: aload 7
    //   104: getfield 2711	android/app/ContentProviderHolder:info	Landroid/content/pm/ProviderInfo;
    //   107: iconst_1
    //   108: aload 7
    //   110: getfield 2624	android/app/ContentProviderHolder:noReleaseNeeded	Z
    //   113: iload 4
    //   115: invokespecial 2621	android/app/ActivityThread:installProvider	(Landroid/content/Context;Landroid/app/ContentProviderHolder;Landroid/content/pm/ProviderInfo;ZZZ)Landroid/app/ContentProviderHolder;
    //   118: getfield 2640	android/app/ContentProviderHolder:provider	Landroid/content/IContentProvider;
    //   121: areturn
    //   122: astore_1
    //   123: aload 5
    //   125: monitorexit
    //   126: aload_1
    //   127: athrow
    //   128: astore_1
    //   129: goto +8 -> 137
    //   132: astore_1
    //   133: goto -10 -> 123
    //   136: astore_1
    //   137: aload_1
    //   138: invokevirtual 579	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   141: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	142	0	this	ActivityThread
    //   0	142	1	paramContext	Context
    //   0	142	2	paramString	String
    //   0	142	3	paramInt	int
    //   0	142	4	paramBoolean	boolean
    //   33	9	6	localIActivityManager	IActivityManager
    //   39	70	7	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   30	41	122	finally
    //   126	128	128	android/os/RemoteException
    //   41	59	132	finally
    //   123	126	132	finally
    //   19	30	136	android/os/RemoteException
  }
  
  final void appNotRespondingViaProvider(IBinder paramIBinder)
  {
    synchronized (mProviderMap)
    {
      paramIBinder = (ProviderRefCount)mProviderRefCountMap.get(paramIBinder);
      if (paramIBinder != null) {
        try
        {
          ActivityManager.getService().appNotRespondingViaProvider(holder.connection);
        }
        catch (RemoteException paramIBinder)
        {
          throw paramIBinder.rethrowFromSystemServer();
        }
      }
      return;
    }
  }
  
  final Configuration applyCompatConfiguration(int paramInt)
  {
    Configuration localConfiguration = mConfiguration;
    if (mCompatConfiguration == null) {
      mCompatConfiguration = new Configuration();
    }
    mCompatConfiguration.setTo(mConfiguration);
    if (mResourcesManager.applyCompatConfigurationLocked(paramInt, mCompatConfiguration)) {
      localConfiguration = mCompatConfiguration;
    }
    return localConfiguration;
  }
  
  Configuration applyConfigCompatMainThread(int paramInt, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo)
  {
    if (paramConfiguration == null) {
      return null;
    }
    Configuration localConfiguration = paramConfiguration;
    if (!paramCompatibilityInfo.supportsScreen())
    {
      mMainThreadConfig.setTo(paramConfiguration);
      localConfiguration = mMainThreadConfig;
      paramCompatibilityInfo.applyToConfiguration(paramInt, localConfiguration);
    }
    return localConfiguration;
  }
  
  public final void applyConfigurationToResources(Configuration paramConfiguration)
  {
    synchronized (mResourcesManager)
    {
      mResourcesManager.applyConfigurationToResourcesLocked(paramConfiguration, null);
      return;
    }
  }
  
  ArrayList<ComponentCallbacks2> collectComponentCallbacks(boolean paramBoolean, Configuration arg2)
  {
    ArrayList localArrayList = new ArrayList();
    synchronized (mResourcesManager)
    {
      int i = mAllApplications.size();
      int j = 0;
      for (int k = 0; k < i; k++) {
        localArrayList.add((ComponentCallbacks2)mAllApplications.get(k));
      }
      i = mActivities.size();
      for (k = 0; k < i; k++)
      {
        ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mActivities.valueAt(k);
        Activity localActivity = activity;
        if (localActivity != null)
        {
          Configuration localConfiguration = applyConfigCompatMainThread(mCurDefaultDisplayDpi, ???, packageInfo.getCompatibilityInfo());
          if ((!activity.mFinished) && ((paramBoolean) || (!paused))) {
            localArrayList.add(localActivity);
          } else if (localConfiguration != null) {
            newConfig = localConfiguration;
          }
        }
      }
      i = mServices.size();
      for (k = 0; k < i; k++) {
        localArrayList.add((ComponentCallbacks2)mServices.valueAt(k));
      }
      synchronized (mProviderMap)
      {
        i = mLocalProviders.size();
        for (k = j; k < i; k++) {
          localArrayList.add(mLocalProviders.valueAt(k)).mLocalProvider);
        }
        return localArrayList;
      }
    }
  }
  
  final void completeRemoveProvider(ProviderRefCount paramProviderRefCount)
  {
    synchronized (mProviderMap)
    {
      if (!removePending) {
        return;
      }
      removePending = false;
      IBinder localIBinder = holder.provider.asBinder();
      if ((ProviderRefCount)mProviderRefCountMap.get(localIBinder) == paramProviderRefCount) {
        mProviderRefCountMap.remove(localIBinder);
      }
      for (int i = mProviderMap.size() - 1; i >= 0; i--) {
        if (mProviderMap.valueAt(i)).mProvider.asBinder() == localIBinder) {
          mProviderMap.removeAt(i);
        }
      }
      try
      {
        ActivityManager.getService().removeContentProvider(holder.connection, false);
      }
      catch (RemoteException paramProviderRefCount) {}
      return;
    }
  }
  
  void doGcIfNeeded()
  {
    mGcIdlerScheduled = false;
    long l = SystemClock.uptimeMillis();
    if (BinderInternal.getLastGcTime() + 5000L < l) {
      BinderInternal.forceGc("bg");
    }
  }
  
  void ensureJitEnabled()
  {
    if (!mJitEnabled)
    {
      mJitEnabled = true;
      VMRuntime.getRuntime().startJitCompilation();
    }
  }
  
  final void finishInstrumentation(int paramInt, Bundle paramBundle)
  {
    IActivityManager localIActivityManager = ActivityManager.getService();
    if ((mProfiler.profileFile != null) && (mProfiler.handlingProfiling) && (mProfiler.profileFd == null)) {
      Debug.stopMethodTracing();
    }
    try
    {
      localIActivityManager.finishInstrumentation(mAppThread, paramInt, paramBundle);
      return;
    }
    catch (RemoteException paramBundle)
    {
      throw paramBundle.rethrowFromSystemServer();
    }
  }
  
  public final Activity getActivity(IBinder paramIBinder)
  {
    return mActivities.get(paramIBinder)).activity;
  }
  
  public ActivityClientRecord getActivityClient(IBinder paramIBinder)
  {
    return (ActivityClientRecord)mActivities.get(paramIBinder);
  }
  
  public Application getApplication()
  {
    return mInitialApplication;
  }
  
  public ApplicationThread getApplicationThread()
  {
    return mAppThread;
  }
  
  public Executor getExecutor()
  {
    return mExecutor;
  }
  
  final Handler getHandler()
  {
    return mH;
  }
  
  public Instrumentation getInstrumentation()
  {
    return mInstrumentation;
  }
  
  public int getIntCoreSetting(String paramString, int paramInt)
  {
    synchronized (mResourcesManager)
    {
      if (mCoreSettings != null)
      {
        paramInt = mCoreSettings.getInt(paramString, paramInt);
        return paramInt;
      }
      return paramInt;
    }
  }
  
  public Looper getLooper()
  {
    return mLooper;
  }
  
  public final LoadedApk getPackageInfo(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt)
  {
    boolean bool1;
    if ((paramInt & 0x1) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2;
    if ((bool1) && (uid != 0) && (uid != 1000) && ((mBoundApplication == null) || (!UserHandle.isSameApp(uid, mBoundApplication.appInfo.uid)))) {
      bool2 = true;
    } else {
      bool2 = false;
    }
    boolean bool3;
    if ((bool1) && ((0x40000000 & paramInt) != 0)) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    if (((paramInt & 0x3) == 1) && (bool2))
    {
      paramCompatibilityInfo = new StringBuilder();
      paramCompatibilityInfo.append("Requesting code from ");
      paramCompatibilityInfo.append(packageName);
      paramCompatibilityInfo.append(" (with uid ");
      paramCompatibilityInfo.append(uid);
      paramCompatibilityInfo.append(")");
      paramCompatibilityInfo = paramCompatibilityInfo.toString();
      paramApplicationInfo = paramCompatibilityInfo;
      if (mBoundApplication != null)
      {
        paramApplicationInfo = new StringBuilder();
        paramApplicationInfo.append(paramCompatibilityInfo);
        paramApplicationInfo.append(" to be run in process ");
        paramApplicationInfo.append(mBoundApplication.processName);
        paramApplicationInfo.append(" (with uid ");
        paramApplicationInfo.append(mBoundApplication.appInfo.uid);
        paramApplicationInfo.append(")");
        paramApplicationInfo = paramApplicationInfo.toString();
      }
      throw new SecurityException(paramApplicationInfo);
    }
    return getPackageInfo(paramApplicationInfo, paramCompatibilityInfo, null, bool2, bool1, bool3);
  }
  
  public final LoadedApk getPackageInfo(String paramString, CompatibilityInfo paramCompatibilityInfo, int paramInt)
  {
    return getPackageInfo(paramString, paramCompatibilityInfo, paramInt, UserHandle.myUserId());
  }
  
  public final LoadedApk getPackageInfo(String paramString, CompatibilityInfo paramCompatibilityInfo, int paramInt1, int paramInt2)
  {
    int i;
    if (UserHandle.myUserId() != paramInt2) {
      i = 1;
    } else {
      i = 0;
    }
    ResourcesManager localResourcesManager = mResourcesManager;
    if (i != 0) {
      localObject = null;
    }
    for (;;)
    {
      break label76;
      if ((paramInt1 & 0x1) != 0) {
        try
        {
          localObject = (WeakReference)mPackages.get(paramString);
        }
        finally
        {
          break label273;
        }
      }
    }
    Object localObject = (WeakReference)mResourcePackages.get(paramString);
    label76:
    if (localObject != null) {
      localObject = (LoadedApk)((WeakReference)localObject).get();
    } else {
      localObject = null;
    }
    if ((localObject != null) && ((mResources == null) || (mResources.getAssets().isUpToDate())))
    {
      if ((((LoadedApk)localObject).isSecurityViolation()) && ((paramInt1 & 0x2) == 0))
      {
        paramCompatibilityInfo = new java/lang/SecurityException;
        localObject = new java/lang/StringBuilder;
        ((StringBuilder)localObject).<init>();
        ((StringBuilder)localObject).append("Requesting code from ");
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append(" to be run in process ");
        ((StringBuilder)localObject).append(mBoundApplication.processName);
        ((StringBuilder)localObject).append("/");
        ((StringBuilder)localObject).append(mBoundApplication.appInfo.uid);
        paramCompatibilityInfo.<init>(((StringBuilder)localObject).toString());
        throw paramCompatibilityInfo;
      }
      return localObject;
    }
    try
    {
      paramString = getPackageManager().getApplicationInfo(paramString, 268436480, paramInt2);
      if (paramString != null) {
        return getPackageInfo(paramString, paramCompatibilityInfo, paramInt1);
      }
      return null;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
    label273:
    throw paramString;
  }
  
  public final LoadedApk getPackageInfoNoCheck(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo)
  {
    return getPackageInfo(paramApplicationInfo, paramCompatibilityInfo, null, false, true, false);
  }
  
  public String getProcessName()
  {
    return mBoundApplication.processName;
  }
  
  public String getProfileFilePath()
  {
    return mProfiler.profileFile;
  }
  
  public int getResumedActivityCutoutMode()
  {
    if ((mLastResumedActivity != null) && (mLastResumedActivity.window != null))
    {
      if (mLastResumedActivity.window.getAttributes().overrideDisplayCutoutMode != 0) {
        return mLastResumedActivity.window.getAttributes().overrideDisplayCutoutMode;
      }
      return mLastResumedActivity.window.getAttributes().layoutInDisplayCutoutMode;
    }
    return -1;
  }
  
  public ContextImpl getSystemContext()
  {
    try
    {
      if (mSystemContext == null) {
        mSystemContext = ContextImpl.createSystemContext(this);
      }
      ContextImpl localContextImpl = mSystemContext;
      return localContextImpl;
    }
    finally {}
  }
  
  public ContextImpl getSystemUiContext()
  {
    try
    {
      if (mSystemUiContext == null) {
        mSystemUiContext = ContextImpl.createSystemUiContext(getSystemContext());
      }
      ContextImpl localContextImpl = mSystemUiContext;
      return localContextImpl;
    }
    finally {}
  }
  
  Resources getTopLevelResources(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, int paramInt, LoadedApk paramLoadedApk)
  {
    return mResourcesManager.getResources(null, paramString, paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramInt, null, paramLoadedApk.getCompatibilityInfo(), paramLoadedApk.getClassLoader());
  }
  
  TransactionExecutor getTransactionExecutor()
  {
    return mTransactionExecutor;
  }
  
  public void handleActivityConfigurationChanged(IBinder paramIBinder, Configuration paramConfiguration, int paramInt)
  {
    ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mActivities.get(paramIBinder);
    if ((localActivityClientRecord != null) && (activity != null))
    {
      int i;
      if ((paramInt != -1) && (paramInt != activity.getDisplay().getDisplayId())) {
        i = 1;
      } else {
        i = 0;
      }
      overrideConfig = paramConfiguration;
      if (activity.mDecor != null) {
        paramIBinder = activity.mDecor.getViewRootImpl();
      } else {
        paramIBinder = null;
      }
      if (i != 0)
      {
        paramConfiguration = performConfigurationChangedForActivity(localActivityClientRecord, mCompatConfiguration, paramInt, true);
        if (paramIBinder != null) {
          paramIBinder.onMovedToDisplay(paramInt, paramConfiguration);
        }
      }
      else
      {
        performConfigurationChangedForActivity(localActivityClientRecord, mCompatConfiguration);
      }
      if (paramIBinder != null) {
        paramIBinder.updateConfiguration(paramInt);
      }
      mSomeActivitiesChanged = true;
      return;
    }
  }
  
  void handleApplicationInfoChanged(ApplicationInfo paramApplicationInfo)
  {
    synchronized (mResourcesManager)
    {
      ??? = (WeakReference)mPackages.get(packageName);
      if (??? != null) {
        ??? = (LoadedApk)((WeakReference)???).get();
      } else {
        ??? = null;
      }
      Object localObject3 = (WeakReference)mResourcePackages.get(packageName);
      if (localObject3 != null) {
        localObject3 = (LoadedApk)((WeakReference)localObject3).get();
      } else {
        localObject3 = null;
      }
      if (??? != null)
      {
        ??? = new ArrayList();
        LoadedApk.makePaths(this, ((LoadedApk)???).getApplicationInfo(), (List)???);
        ((LoadedApk)???).updateApplicationInfo(paramApplicationInfo, (List)???);
      }
      if (localObject3 != null)
      {
        ??? = new ArrayList();
        LoadedApk.makePaths(this, ((LoadedApk)localObject3).getApplicationInfo(), (List)???);
        ((LoadedApk)localObject3).updateApplicationInfo(paramApplicationInfo, (List)???);
      }
      synchronized (mResourcesManager)
      {
        mResourcesManager.applyNewResourceDirsLocked(sourceDir, resourceDirs);
        ApplicationPackageManager.configurationChanged();
        paramApplicationInfo = new Configuration();
        int i;
        if (mConfiguration != null) {
          i = mConfiguration.assetsSeq;
        } else {
          i = 0;
        }
        assetsSeq = (i + 1);
        handleConfigurationChanged(paramApplicationInfo, null);
        relaunchAllActivities();
        return;
      }
    }
  }
  
  public void handleConfigurationChanged(Configuration paramConfiguration)
  {
    Trace.traceBegin(64L, "configChanged");
    mCurDefaultDisplayDpi = densityDpi;
    mUpdatingSystemConfig = true;
    try
    {
      handleConfigurationChanged(paramConfiguration, null);
      mUpdatingSystemConfig = false;
      Trace.traceEnd(64L);
      return;
    }
    finally
    {
      mUpdatingSystemConfig = false;
    }
  }
  
  public void handleDestroyActivity(IBinder paramIBinder, boolean paramBoolean1, int paramInt, boolean paramBoolean2, String paramString)
  {
    paramString = performDestroyActivity(paramIBinder, paramBoolean1, paramInt, paramBoolean2, paramString);
    if (paramString != null)
    {
      cleanUpPendingRemoveWindows(paramString, paramBoolean1);
      WindowManager localWindowManager = activity.getWindowManager();
      View localView = activity.mDecor;
      if (localView != null)
      {
        if (activity.mVisibleFromServer) {
          mNumVisibleActivities -= 1;
        }
        localObject = localView.getWindowToken();
        if (activity.mWindowAdded) {
          if (mPreserveWindow)
          {
            mPendingRemoveWindow = window;
            mPendingRemoveWindowManager = localWindowManager;
            window.clearContentView();
          }
          else
          {
            localWindowManager.removeViewImmediate(localView);
          }
        }
        if ((localObject != null) && (mPendingRemoveWindow == null)) {
          WindowManagerGlobal.getInstance().closeAll((IBinder)localObject, activity.getClass().getName(), "Activity");
        } else if (mPendingRemoveWindow != null) {
          WindowManagerGlobal.getInstance().closeAllExceptView(paramIBinder, localView, activity.getClass().getName(), "Activity");
        }
        activity.mDecor = null;
      }
      if (mPendingRemoveWindow == null) {
        WindowManagerGlobal.getInstance().closeAll(paramIBinder, activity.getClass().getName(), "Activity");
      }
      Object localObject = activity.getBaseContext();
      if ((localObject instanceof ContextImpl)) {
        ((ContextImpl)localObject).scheduleFinalCleanup(activity.getClass().getName(), "Activity");
      }
    }
    if (paramBoolean1) {
      try
      {
        ActivityManager.getService().activityDestroyed(paramIBinder);
      }
      catch (RemoteException paramIBinder)
      {
        throw paramIBinder.rethrowFromSystemServer();
      }
    }
    mSomeActivitiesChanged = true;
  }
  
  final void handleDispatchPackageBroadcast(int paramInt, String[] paramArrayOfString)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    int i;
    Object localObject2;
    if (paramInt != 0)
    {
      switch (paramInt)
      {
      default: 
        bool1 = bool2;
        break;
      case 3: 
        if (paramArrayOfString == null)
        {
          bool1 = bool2;
          break;
        }
        synchronized (mResourcesManager)
        {
          for (i = paramArrayOfString.length - 1; i >= 0; i--)
          {
            Object localObject1 = (WeakReference)mPackages.get(paramArrayOfString[i]);
            localObject2 = null;
            if (localObject1 != null) {
              localObject1 = (LoadedApk)((WeakReference)localObject1).get();
            } else {
              localObject1 = null;
            }
            Object localObject3;
            if (localObject1 != null)
            {
              bool3 = true;
            }
            else
            {
              localObject3 = (WeakReference)mResourcePackages.get(paramArrayOfString[i]);
              localObject1 = localObject2;
              if (localObject3 != null) {
                localObject1 = (LoadedApk)((WeakReference)localObject3).get();
              }
              localObject2 = localObject1;
              localObject1 = localObject2;
              if (localObject2 != null)
              {
                bool3 = true;
                localObject1 = localObject2;
              }
            }
            if (localObject1 != null)
            {
              String str = paramArrayOfString[i];
              try
              {
                localObject2 = sPackageManager.getApplicationInfo(str, 1024, UserHandle.myUserId());
                if (mActivities.size() > 0)
                {
                  Iterator localIterator = mActivities.values().iterator();
                  while (localIterator.hasNext())
                  {
                    localObject3 = (ActivityClientRecord)localIterator.next();
                    if (activityInfo.applicationInfo.packageName.equals(str))
                    {
                      activityInfo.applicationInfo = ((ApplicationInfo)localObject2);
                      packageInfo = ((LoadedApk)localObject1);
                    }
                  }
                }
                localObject3 = new java/util/ArrayList;
                ((ArrayList)localObject3).<init>();
                LoadedApk.makePaths(this, ((LoadedApk)localObject1).getApplicationInfo(), (List)localObject3);
                ((LoadedApk)localObject1).updateApplicationInfo((ApplicationInfo)localObject2, (List)localObject3);
              }
              catch (RemoteException localRemoteException) {}
            }
          }
          bool1 = bool3;
        }
      }
    }
    else
    {
      if (paramInt == 0) {
        i = 1;
      } else {
        i = 0;
      }
      if (paramArrayOfString == null) {
        bool1 = bool2;
      }
    }
    synchronized (mResourcesManager)
    {
      int j = paramArrayOfString.length - 1;
      while (j >= 0)
      {
        bool3 = bool1;
        if (!bool1)
        {
          localObject2 = (WeakReference)mPackages.get(paramArrayOfString[j]);
          if ((localObject2 != null) && (((WeakReference)localObject2).get() != null))
          {
            bool3 = true;
          }
          else
          {
            localObject2 = (WeakReference)mResourcePackages.get(paramArrayOfString[j]);
            bool3 = bool1;
            if (localObject2 != null)
            {
              bool3 = bool1;
              if (((WeakReference)localObject2).get() != null) {
                bool3 = true;
              }
            }
          }
        }
        if (i != 0)
        {
          mPackages.remove(paramArrayOfString[j]);
          mResourcePackages.remove(paramArrayOfString[j]);
        }
        j--;
        bool1 = bool3;
      }
      ApplicationPackageManager.handlePackageBroadcast(paramInt, paramArrayOfString, bool1);
      return;
    }
  }
  
  public void handleInstallProvider(ProviderInfo paramProviderInfo)
  {
    StrictMode.ThreadPolicy localThreadPolicy = StrictMode.allowThreadDiskWrites();
    try
    {
      installContentProviders(mInitialApplication, Arrays.asList(new ProviderInfo[] { paramProviderInfo }));
      return;
    }
    finally
    {
      StrictMode.setThreadPolicy(localThreadPolicy);
    }
  }
  
  public Activity handleLaunchActivity(ActivityClientRecord paramActivityClientRecord, PendingTransactionActions paramPendingTransactionActions, Intent paramIntent)
  {
    unscheduleGcIdler();
    mSomeActivitiesChanged = true;
    if (profilerInfo != null)
    {
      mProfiler.setProfiler(profilerInfo);
      mProfiler.startProfiling();
    }
    handleConfigurationChanged(null, null);
    if (!ThreadedRenderer.sRendererDisabled) {
      GraphicsEnvironment.earlyInitEGL();
    }
    WindowManagerGlobal.initialize();
    paramIntent = performLaunchActivity(paramActivityClientRecord, paramIntent);
    if (paramIntent != null)
    {
      createdConfig = new Configuration(mConfiguration);
      reportSizeConfigurations(paramActivityClientRecord);
      if ((activity.mFinished) || (paramPendingTransactionActions == null)) {
        break label134;
      }
      paramPendingTransactionActions.setOldState(state);
      paramPendingTransactionActions.setRestoreInstanceState(true);
      paramPendingTransactionActions.setCallOnPostCreate(true);
    }
    try
    {
      ActivityManager.getService().finishActivity(token, 0, null, 0);
      label134:
      return paramIntent;
    }
    catch (RemoteException paramActivityClientRecord)
    {
      throw paramActivityClientRecord.rethrowFromSystemServer();
    }
  }
  
  final void handleLowMemory()
  {
    ArrayList localArrayList = collectComponentCallbacks(true, null);
    int i = localArrayList.size();
    for (int j = 0; j < i; j++) {
      ((ComponentCallbacks2)localArrayList.get(j)).onLowMemory();
    }
    if (Process.myUid() != 1000) {
      EventLog.writeEvent(75003, SQLiteDatabase.releaseMemory());
    }
    Canvas.freeCaches();
    Canvas.freeTextLayoutCaches();
    BinderInternal.forceGc("mem");
  }
  
  public void handleMultiWindowModeChanged(IBinder paramIBinder, boolean paramBoolean, Configuration paramConfiguration)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (paramIBinder != null)
    {
      Configuration localConfiguration = new Configuration(mConfiguration);
      if (paramConfiguration != null) {
        localConfiguration.updateFrom(paramConfiguration);
      }
      activity.dispatchMultiWindowModeChanged(paramBoolean, localConfiguration);
    }
  }
  
  public void handleNewIntent(IBinder paramIBinder, List<ReferrerIntent> paramList, boolean paramBoolean)
  {
    performNewIntents(paramIBinder, paramList, paramBoolean);
  }
  
  public void handlePauseActivity(IBinder paramIBinder, boolean paramBoolean1, boolean paramBoolean2, int paramInt, PendingTransactionActions paramPendingTransactionActions, String paramString)
  {
    ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (localActivityClientRecord != null)
    {
      if (paramBoolean2) {
        performUserLeavingActivity(localActivityClientRecord);
      }
      paramIBinder = activity;
      mConfigChangeFlags |= paramInt;
      performPauseActivity(localActivityClientRecord, paramBoolean1, paramString, paramPendingTransactionActions);
      if (localActivityClientRecord.isPreHoneycomb()) {
        QueuedWork.waitToFinish();
      }
      mSomeActivitiesChanged = true;
    }
  }
  
  public void handlePictureInPictureModeChanged(IBinder paramIBinder, boolean paramBoolean, Configuration paramConfiguration)
  {
    ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (localActivityClientRecord != null)
    {
      paramIBinder = new Configuration(mConfiguration);
      if (paramConfiguration != null) {
        paramIBinder.updateFrom(paramConfiguration);
      }
      activity.dispatchPictureInPictureModeChanged(paramBoolean, paramIBinder);
    }
  }
  
  /* Error */
  final void handleProfilerControl(boolean paramBoolean, ProfilerInfo paramProfilerInfo, int paramInt)
  {
    // Byte code:
    //   0: iload_1
    //   1: ifeq +91 -> 92
    //   4: aload_0
    //   5: getfield 1457	android/app/ActivityThread:mProfiler	Landroid/app/ActivityThread$Profiler;
    //   8: aload_2
    //   9: invokevirtual 3467	android/app/ActivityThread$Profiler:setProfiler	(Landroid/app/ProfilerInfo;)V
    //   12: aload_0
    //   13: getfield 1457	android/app/ActivityThread:mProfiler	Landroid/app/ActivityThread$Profiler;
    //   16: invokevirtual 1499	android/app/ActivityThread$Profiler:startProfiling	()V
    //   19: aload_2
    //   20: invokevirtual 3539	android/app/ProfilerInfo:closeFd	()V
    //   23: goto +76 -> 99
    //   26: astore 4
    //   28: goto +57 -> 85
    //   31: astore 4
    //   33: new 632	java/lang/StringBuilder
    //   36: astore 4
    //   38: aload 4
    //   40: invokespecial 633	java/lang/StringBuilder:<init>	()V
    //   43: aload 4
    //   45: ldc_w 3541
    //   48: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: pop
    //   52: aload 4
    //   54: aload_2
    //   55: getfield 1466	android/app/ProfilerInfo:profileFile	Ljava/lang/String;
    //   58: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: pop
    //   62: aload 4
    //   64: ldc_w 2222
    //   67: invokevirtual 639	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   70: pop
    //   71: ldc 124
    //   73: aload 4
    //   75: invokevirtual 646	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   78: invokestatic 1670	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   81: pop
    //   82: goto -63 -> 19
    //   85: aload_2
    //   86: invokevirtual 3539	android/app/ProfilerInfo:closeFd	()V
    //   89: aload 4
    //   91: athrow
    //   92: aload_0
    //   93: getfield 1457	android/app/ActivityThread:mProfiler	Landroid/app/ActivityThread$Profiler;
    //   96: invokevirtual 3544	android/app/ActivityThread$Profiler:stopProfiling	()V
    //   99: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	100	0	this	ActivityThread
    //   0	100	1	paramBoolean	boolean
    //   0	100	2	paramProfilerInfo	ProfilerInfo
    //   0	100	3	paramInt	int
    //   26	1	4	localObject	Object
    //   31	1	4	localRuntimeException	RuntimeException
    //   36	54	4	localStringBuilder	StringBuilder
    // Exception table:
    //   from	to	target	type
    //   4	19	26	finally
    //   33	82	26	finally
    //   4	19	31	java/lang/RuntimeException
  }
  
  /* Error */
  public void handleRelaunchActivity(ActivityClientRecord paramActivityClientRecord, PendingTransactionActions paramPendingTransactionActions)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 2077	android/app/ActivityThread:unscheduleGcIdler	()V
    //   4: aload_0
    //   5: iconst_1
    //   6: putfield 316	android/app/ActivityThread:mSomeActivitiesChanged	Z
    //   9: aconst_null
    //   10: astore_3
    //   11: aload_0
    //   12: getfield 369	android/app/ActivityThread:mResourcesManager	Landroid/app/ResourcesManager;
    //   15: astore 4
    //   17: aload 4
    //   19: monitorenter
    //   20: aload_0
    //   21: getfield 326	android/app/ActivityThread:mRelaunchingActivities	Ljava/util/ArrayList;
    //   24: invokevirtual 2046	java/util/ArrayList:size	()I
    //   27: istore 5
    //   29: aload_1
    //   30: getfield 814	android/app/ActivityThread$ActivityClientRecord:token	Landroid/os/IBinder;
    //   33: astore 6
    //   35: iconst_0
    //   36: istore 7
    //   38: iconst_0
    //   39: istore 8
    //   41: aconst_null
    //   42: astore 9
    //   44: iload 7
    //   46: istore 10
    //   48: iload 10
    //   50: iload 5
    //   52: if_icmpge +108 -> 160
    //   55: aload_0
    //   56: getfield 326	android/app/ActivityThread:mRelaunchingActivities	Ljava/util/ArrayList;
    //   59: iload 10
    //   61: invokevirtual 2047	java/util/ArrayList:get	(I)Ljava/lang/Object;
    //   64: checkcast 10	android/app/ActivityThread$ActivityClientRecord
    //   67: astore_1
    //   68: aload_1
    //   69: getfield 814	android/app/ActivityThread$ActivityClientRecord:token	Landroid/os/IBinder;
    //   72: astore 11
    //   74: iload 10
    //   76: istore 7
    //   78: iload 5
    //   80: istore 12
    //   82: iload 8
    //   84: istore 13
    //   86: aload 11
    //   88: aload 6
    //   90: if_acmpne +52 -> 142
    //   93: aload_1
    //   94: getfield 3549	android/app/ActivityThread$ActivityClientRecord:pendingConfigChanges	I
    //   97: istore 13
    //   99: aload_0
    //   100: getfield 326	android/app/ActivityThread:mRelaunchingActivities	Ljava/util/ArrayList;
    //   103: iload 10
    //   105: invokevirtual 3551	java/util/ArrayList:remove	(I)Ljava/lang/Object;
    //   108: pop
    //   109: iload 10
    //   111: iconst_1
    //   112: isub
    //   113: istore 7
    //   115: iload 5
    //   117: iconst_1
    //   118: isub
    //   119: istore 12
    //   121: aload_1
    //   122: astore 9
    //   124: iload 13
    //   126: iload 8
    //   128: ior
    //   129: istore 13
    //   131: goto +11 -> 142
    //   134: astore_1
    //   135: goto +279 -> 414
    //   138: astore_1
    //   139: goto +18 -> 157
    //   142: iinc 7 1
    //   145: iload 12
    //   147: istore 5
    //   149: iload 13
    //   151: istore 8
    //   153: goto -109 -> 44
    //   156: astore_1
    //   157: goto +257 -> 414
    //   160: aload 9
    //   162: ifnonnull +7 -> 169
    //   165: aload 4
    //   167: monitorexit
    //   168: return
    //   169: aload_3
    //   170: astore_1
    //   171: aload_0
    //   172: getfield 328	android/app/ActivityThread:mPendingConfiguration	Landroid/content/res/Configuration;
    //   175: ifnull +13 -> 188
    //   178: aload_0
    //   179: getfield 328	android/app/ActivityThread:mPendingConfiguration	Landroid/content/res/Configuration;
    //   182: astore_1
    //   183: aload_0
    //   184: aconst_null
    //   185: putfield 328	android/app/ActivityThread:mPendingConfiguration	Landroid/content/res/Configuration;
    //   188: aload 4
    //   190: monitorexit
    //   191: aload_1
    //   192: astore_3
    //   193: aload 9
    //   195: getfield 2401	android/app/ActivityThread$ActivityClientRecord:createdConfig	Landroid/content/res/Configuration;
    //   198: ifnull +68 -> 266
    //   201: aload_0
    //   202: getfield 1452	android/app/ActivityThread:mConfiguration	Landroid/content/res/Configuration;
    //   205: ifnull +37 -> 242
    //   208: aload_1
    //   209: astore_3
    //   210: aload 9
    //   212: getfield 2401	android/app/ActivityThread$ActivityClientRecord:createdConfig	Landroid/content/res/Configuration;
    //   215: aload_0
    //   216: getfield 1452	android/app/ActivityThread:mConfiguration	Landroid/content/res/Configuration;
    //   219: invokevirtual 2027	android/content/res/Configuration:isOtherSeqNewer	(Landroid/content/res/Configuration;)Z
    //   222: ifeq +44 -> 266
    //   225: aload_1
    //   226: astore_3
    //   227: aload_0
    //   228: getfield 1452	android/app/ActivityThread:mConfiguration	Landroid/content/res/Configuration;
    //   231: aload 9
    //   233: getfield 2401	android/app/ActivityThread$ActivityClientRecord:createdConfig	Landroid/content/res/Configuration;
    //   236: invokevirtual 2896	android/content/res/Configuration:diff	(Landroid/content/res/Configuration;)I
    //   239: ifeq +27 -> 266
    //   242: aload_1
    //   243: ifnull +17 -> 260
    //   246: aload_1
    //   247: astore_3
    //   248: aload 9
    //   250: getfield 2401	android/app/ActivityThread$ActivityClientRecord:createdConfig	Landroid/content/res/Configuration;
    //   253: aload_1
    //   254: invokevirtual 2027	android/content/res/Configuration:isOtherSeqNewer	(Landroid/content/res/Configuration;)Z
    //   257: ifeq +9 -> 266
    //   260: aload 9
    //   262: getfield 2401	android/app/ActivityThread$ActivityClientRecord:createdConfig	Landroid/content/res/Configuration;
    //   265: astore_3
    //   266: aload_3
    //   267: ifnull +21 -> 288
    //   270: aload_0
    //   271: aload_3
    //   272: getfield 1547	android/content/res/Configuration:densityDpi	I
    //   275: putfield 1549	android/app/ActivityThread:mCurDefaultDisplayDpi	I
    //   278: aload_0
    //   279: invokespecial 1592	android/app/ActivityThread:updateDefaultDensity	()V
    //   282: aload_0
    //   283: aload_3
    //   284: aconst_null
    //   285: invokespecial 2572	android/app/ActivityThread:handleConfigurationChanged	(Landroid/content/res/Configuration;Landroid/content/res/CompatibilityInfo;)V
    //   288: aload_0
    //   289: getfield 281	android/app/ActivityThread:mActivities	Landroid/util/ArrayMap;
    //   292: aload 9
    //   294: getfield 814	android/app/ActivityThread$ActivityClientRecord:token	Landroid/os/IBinder;
    //   297: invokevirtual 1308	android/util/ArrayMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   300: checkcast 10	android/app/ActivityThread$ActivityClientRecord
    //   303: astore_1
    //   304: aload_1
    //   305: ifnonnull +4 -> 309
    //   308: return
    //   309: aload_1
    //   310: getfield 699	android/app/ActivityThread$ActivityClientRecord:activity	Landroid/app/Activity;
    //   313: astore_3
    //   314: aload_3
    //   315: aload_3
    //   316: getfield 2854	android/app/Activity:mConfigChangeFlags	I
    //   319: iload 8
    //   321: ior
    //   322: putfield 2854	android/app/Activity:mConfigChangeFlags	I
    //   325: aload_1
    //   326: aload 9
    //   328: getfield 763	android/app/ActivityThread$ActivityClientRecord:mPreserveWindow	Z
    //   331: putfield 763	android/app/ActivityThread$ActivityClientRecord:mPreserveWindow	Z
    //   334: aload_1
    //   335: getfield 699	android/app/ActivityThread$ActivityClientRecord:activity	Landroid/app/Activity;
    //   338: iconst_1
    //   339: putfield 3554	android/app/Activity:mChangingConfigurations	Z
    //   342: aload_1
    //   343: getfield 763	android/app/ActivityThread$ActivityClientRecord:mPreserveWindow	Z
    //   346: ifeq +16 -> 362
    //   349: invokestatic 3558	android/view/WindowManagerGlobal:getWindowSession	()Landroid/view/IWindowSession;
    //   352: aload_1
    //   353: getfield 814	android/app/ActivityThread$ActivityClientRecord:token	Landroid/os/IBinder;
    //   356: iconst_1
    //   357: invokeinterface 3563 3 0
    //   362: aload_0
    //   363: aload_1
    //   364: iload 8
    //   366: aload 9
    //   368: getfield 2378	android/app/ActivityThread$ActivityClientRecord:pendingResults	Ljava/util/List;
    //   371: aload 9
    //   373: getfield 2384	android/app/ActivityThread$ActivityClientRecord:pendingIntents	Ljava/util/List;
    //   376: aload_2
    //   377: aload 9
    //   379: getfield 2387	android/app/ActivityThread$ActivityClientRecord:startsNotResumed	Z
    //   382: aload 9
    //   384: getfield 828	android/app/ActivityThread$ActivityClientRecord:overrideConfig	Landroid/content/res/Configuration;
    //   387: ldc_w 3564
    //   390: invokespecial 3566	android/app/ActivityThread:handleRelaunchActivityInner	(Landroid/app/ActivityThread$ActivityClientRecord;ILjava/util/List;Ljava/util/List;Landroid/app/servertransaction/PendingTransactionActions;ZLandroid/content/res/Configuration;Ljava/lang/String;)V
    //   393: aload_2
    //   394: ifnull +8 -> 402
    //   397: aload_2
    //   398: iconst_1
    //   399: invokevirtual 3569	android/app/servertransaction/PendingTransactionActions:setReportRelaunchToWindowManager	(Z)V
    //   402: return
    //   403: astore_1
    //   404: aload_1
    //   405: invokevirtual 579	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   408: athrow
    //   409: astore_1
    //   410: goto +4 -> 414
    //   413: astore_1
    //   414: aload 4
    //   416: monitorexit
    //   417: aload_1
    //   418: athrow
    //   419: astore_1
    //   420: goto -6 -> 414
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	423	0	this	ActivityThread
    //   0	423	1	paramActivityClientRecord	ActivityClientRecord
    //   0	423	2	paramPendingTransactionActions	PendingTransactionActions
    //   10	306	3	localObject	Object
    //   15	400	4	localResourcesManager	ResourcesManager
    //   27	121	5	i	int
    //   33	56	6	localIBinder1	IBinder
    //   36	107	7	j	int
    //   39	326	8	k	int
    //   42	341	9	localActivityClientRecord	ActivityClientRecord
    //   46	67	10	m	int
    //   72	15	11	localIBinder2	IBinder
    //   80	66	12	n	int
    //   84	66	13	i1	int
    // Exception table:
    //   from	to	target	type
    //   99	109	134	finally
    //   93	99	138	finally
    //   55	74	156	finally
    //   165	168	156	finally
    //   171	188	156	finally
    //   188	191	156	finally
    //   342	362	403	android/os/RemoteException
    //   29	35	409	finally
    //   20	29	413	finally
    //   414	417	419	finally
  }
  
  public void handleRequestAssistContextExtras(RequestAssistContextExtras paramRequestAssistContextExtras)
  {
    int i = requestType;
    int j = 0;
    boolean bool;
    if (i == 2) {
      bool = true;
    } else {
      bool = false;
    }
    if (mLastSessionId != sessionId)
    {
      mLastSessionId = sessionId;
      for (i = mLastAssistStructures.size() - 1; i >= 0; i--)
      {
        localObject1 = (AssistStructure)((WeakReference)mLastAssistStructures.get(i)).get();
        if (localObject1 != null) {
          ((AssistStructure)localObject1).clearSendChannel();
        }
        mLastAssistStructures.remove(i);
      }
    }
    Bundle localBundle = new Bundle();
    AssistStructure localAssistStructure = null;
    AssistContent localAssistContent;
    if (bool) {
      localAssistContent = null;
    } else {
      localAssistContent = new AssistContent();
    }
    long l = SystemClock.uptimeMillis();
    ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mActivities.get(activityToken);
    Object localObject2 = null;
    Object localObject3 = null;
    Object localObject1 = localAssistStructure;
    if (localActivityClientRecord != null)
    {
      if (!bool)
      {
        activity.getApplication().dispatchOnProvideAssistData(activity, localBundle);
        activity.onProvideAssistData(localBundle);
        localObject3 = activity.onProvideReferrer();
      }
      if (requestType != 1)
      {
        localObject1 = localAssistStructure;
        localObject2 = localObject3;
        if (!bool) {}
      }
      else
      {
        localAssistStructure = new AssistStructure(activity, bool, flags);
        localObject1 = activity.getIntent();
        if ((window != null) && ((window.getAttributes().flags & 0x2000) != 0)) {
          i = j;
        } else {
          i = 1;
        }
        if ((localObject1 != null) && (i != 0))
        {
          if (!bool)
          {
            localObject1 = new Intent((Intent)localObject1);
            ((Intent)localObject1).setFlags(((Intent)localObject1).getFlags() & 0xFFFFFFBD);
            ((Intent)localObject1).removeUnsafeExtras();
            localAssistContent.setDefaultIntent((Intent)localObject1);
          }
        }
        else if (!bool) {
          localAssistContent.setDefaultIntent(new Intent());
        }
        localObject1 = localAssistStructure;
        localObject2 = localObject3;
        if (!bool)
        {
          activity.onProvideAssistContent(localAssistContent);
          localObject2 = localObject3;
          localObject1 = localAssistStructure;
        }
      }
    }
    if (localObject1 == null) {
      localObject1 = new AssistStructure();
    }
    ((AssistStructure)localObject1).setAcquisitionStartTime(l);
    ((AssistStructure)localObject1).setAcquisitionEndTime(SystemClock.uptimeMillis());
    mLastAssistStructures.add(new WeakReference(localObject1));
    localObject3 = ActivityManager.getService();
    try
    {
      ((IActivityManager)localObject3).reportAssistContextExtras(requestToken, localBundle, (AssistStructure)localObject1, localAssistContent, localObject2);
      return;
    }
    catch (RemoteException paramRequestAssistContextExtras)
    {
      throw paramRequestAssistContextExtras.rethrowFromSystemServer();
    }
  }
  
  public void handleResumeActivity(IBinder paramIBinder, boolean paramBoolean1, boolean paramBoolean2, String paramString)
  {
    unscheduleGcIdler();
    mSomeActivitiesChanged = true;
    paramIBinder = performResumeActivity(paramIBinder, paramBoolean1, paramString);
    if (paramIBinder == null) {
      return;
    }
    paramString = activity;
    int i;
    if (paramBoolean2) {
      i = 256;
    } else {
      i = 0;
    }
    paramBoolean2 = mStartedActivity ^ true;
    paramBoolean1 = paramBoolean2;
    if (!paramBoolean2) {
      try
      {
        paramBoolean1 = ActivityManager.getService().willActivityBeVisible(paramString.getActivityToken());
      }
      catch (RemoteException paramIBinder)
      {
        throw paramIBinder.rethrowFromSystemServer();
      }
    }
    WindowManager.LayoutParams localLayoutParams;
    if ((window == null) && (!mFinished) && (paramBoolean1))
    {
      window = activity.getWindow();
      View localView = window.getDecorView();
      localView.setVisibility(4);
      WindowManager localWindowManager = paramString.getWindowManager();
      localLayoutParams = window.getAttributes();
      mDecor = localView;
      type = 1;
      softInputMode |= i;
      if (mPreserveWindow)
      {
        mWindowAdded = true;
        mPreserveWindow = false;
        ViewRootImpl localViewRootImpl = localView.getViewRootImpl();
        if (localViewRootImpl != null) {
          localViewRootImpl.notifyChildRebuilt();
        }
      }
      if (mVisibleFromClient) {
        if (!mWindowAdded)
        {
          mWindowAdded = true;
          localWindowManager.addView(localView, localLayoutParams);
        }
        else
        {
          paramString.onWindowAttributesChanged(localLayoutParams);
        }
      }
    }
    else if (!paramBoolean1)
    {
      hideForNow = true;
    }
    cleanUpPendingRemoveWindows(paramIBinder, false);
    if ((!activity.mFinished) && (paramBoolean1) && (activity.mDecor != null) && (!hideForNow))
    {
      if (newConfig != null)
      {
        performConfigurationChangedForActivity(paramIBinder, newConfig);
        newConfig = null;
      }
      localLayoutParams = window.getAttributes();
      if ((0x100 & softInputMode) != i)
      {
        softInputMode = (softInputMode & 0xFEFF | i);
        if (activity.mVisibleFromClient) {
          paramString.getWindowManager().updateViewLayout(window.getDecorView(), localLayoutParams);
        }
      }
      activity.mVisibleFromServer = true;
      mNumVisibleActivities += 1;
      if (activity.mVisibleFromClient) {
        activity.makeVisible();
      }
    }
    nextIdle = mNewActivities;
    mNewActivities = paramIBinder;
    Looper.myQueue().addIdleHandler(new Idler(null));
  }
  
  public void handleSendResult(IBinder paramIBinder, List<ResultInfo> paramList, String paramString)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (paramIBinder != null)
    {
      boolean bool = paused ^ true;
      if ((!activity.mFinished) && (activity.mDecor != null) && (hideForNow) && (bool)) {
        updateVisibility(paramIBinder, true);
      }
      if (bool) {
        try
        {
          activity.mCalled = false;
          activity.mTemporaryPause = true;
          mInstrumentation.callActivityOnPause(activity);
          if (!activity.mCalled)
          {
            SuperNotCalledException localSuperNotCalledException = new android/util/SuperNotCalledException;
            StringBuilder localStringBuilder = new java/lang/StringBuilder;
            localStringBuilder.<init>();
            localStringBuilder.append("Activity ");
            localStringBuilder.append(intent.getComponent().toShortString());
            localStringBuilder.append(" did not call through to super.onPause()");
            localSuperNotCalledException.<init>(localStringBuilder.toString());
            throw localSuperNotCalledException;
          }
        }
        catch (Exception localException)
        {
          if (!mInstrumentation.onException(activity, localException))
          {
            paramList = new StringBuilder();
            paramList.append("Unable to pause activity ");
            paramList.append(intent.getComponent().toShortString());
            paramList.append(": ");
            paramList.append(localException.toString());
            throw new RuntimeException(paramList.toString(), localException);
          }
        }
        catch (SuperNotCalledException paramIBinder)
        {
          throw paramIBinder;
        }
      }
      checkAndBlockForNetworkAccess();
      deliverResults(paramIBinder, paramList, paramString);
      if (bool)
      {
        activity.performResume(false, paramString);
        activity.mTemporaryPause = false;
      }
    }
  }
  
  public void handleStartActivity(ActivityClientRecord paramActivityClientRecord, PendingTransactionActions paramPendingTransactionActions)
  {
    Activity localActivity = activity;
    if (activity == null) {
      return;
    }
    if (stopped)
    {
      if (activity.mFinished) {
        return;
      }
      localActivity.performStart("handleStartActivity");
      paramActivityClientRecord.setState(2);
      if (paramPendingTransactionActions == null) {
        return;
      }
      if (paramPendingTransactionActions.shouldRestoreInstanceState()) {
        if (paramActivityClientRecord.isPersistable())
        {
          if ((state != null) || (persistentState != null)) {
            mInstrumentation.callActivityOnRestoreInstanceState(localActivity, state, persistentState);
          }
        }
        else if (state != null) {
          mInstrumentation.callActivityOnRestoreInstanceState(localActivity, state);
        }
      }
      if (paramPendingTransactionActions.shouldCallOnPostCreate())
      {
        mCalled = false;
        if (paramActivityClientRecord.isPersistable()) {
          mInstrumentation.callActivityOnPostCreate(localActivity, state, persistentState);
        } else {
          mInstrumentation.callActivityOnPostCreate(localActivity, state);
        }
        if (!mCalled)
        {
          paramPendingTransactionActions = new StringBuilder();
          paramPendingTransactionActions.append("Activity ");
          paramPendingTransactionActions.append(intent.getComponent().toShortString());
          paramPendingTransactionActions.append(" did not call through to super.onPostCreate()");
          throw new SuperNotCalledException(paramPendingTransactionActions.toString());
        }
      }
      return;
    }
    throw new IllegalStateException("Can't start activity that is not stopped.");
  }
  
  public void handleStopActivity(IBinder paramIBinder, boolean paramBoolean1, int paramInt, PendingTransactionActions paramPendingTransactionActions, boolean paramBoolean2, String paramString)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    Object localObject = activity;
    mConfigChangeFlags |= paramInt;
    localObject = new PendingTransactionActions.StopInfo();
    performStopActivityInner(paramIBinder, (PendingTransactionActions.StopInfo)localObject, paramBoolean1, true, paramBoolean2, paramString);
    updateVisibility(paramIBinder, paramBoolean1);
    if (!paramIBinder.isPreHoneycomb()) {
      QueuedWork.waitToFinish();
    }
    ((PendingTransactionActions.StopInfo)localObject).setActivity(paramIBinder);
    ((PendingTransactionActions.StopInfo)localObject).setState(state);
    ((PendingTransactionActions.StopInfo)localObject).setPersistentState(persistentState);
    paramPendingTransactionActions.setStopInfo((PendingTransactionActions.StopInfo)localObject);
    mSomeActivitiesChanged = true;
  }
  
  public void handleTranslucentConversionComplete(IBinder paramIBinder, boolean paramBoolean)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (paramIBinder != null) {
      activity.onTranslucentConversionComplete(paramBoolean);
    }
  }
  
  final void handleUnstableProviderDied(IBinder paramIBinder, boolean paramBoolean)
  {
    synchronized (mProviderMap)
    {
      handleUnstableProviderDiedLocked(paramIBinder, paramBoolean);
      return;
    }
  }
  
  final void handleUnstableProviderDiedLocked(IBinder paramIBinder, boolean paramBoolean)
  {
    ProviderRefCount localProviderRefCount = (ProviderRefCount)mProviderRefCountMap.get(paramIBinder);
    if (localProviderRefCount != null)
    {
      mProviderRefCountMap.remove(paramIBinder);
      for (int i = mProviderMap.size() - 1; i >= 0; i--)
      {
        ProviderClientRecord localProviderClientRecord = (ProviderClientRecord)mProviderMap.valueAt(i);
        if ((localProviderClientRecord != null) && (mProvider.asBinder() == paramIBinder))
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("Removing dead content provider:");
          localStringBuilder.append(mProvider.toString());
          Slog.i("ActivityThread", localStringBuilder.toString());
          mProviderMap.removeAt(i);
        }
      }
      if (paramBoolean) {
        try
        {
          ActivityManager.getService().unstableProviderDied(holder.connection);
        }
        catch (RemoteException paramIBinder) {}
      }
    }
  }
  
  public void handleWindowVisibility(IBinder paramIBinder, boolean paramBoolean)
  {
    Object localObject = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (localObject == null)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("handleWindowVisibility: no activity for token ");
      ((StringBuilder)localObject).append(paramIBinder);
      Log.w("ActivityThread", ((StringBuilder)localObject).toString());
      return;
    }
    if ((!paramBoolean) && (!stopped))
    {
      performStopActivityInner((ActivityClientRecord)localObject, null, paramBoolean, false, false, "handleWindowVisibility");
    }
    else if ((paramBoolean) && (stopped))
    {
      unscheduleGcIdler();
      activity.performRestart(true, "handleWindowVisibility");
      ((ActivityClientRecord)localObject).setState(2);
    }
    if (activity.mDecor != null) {
      updateVisibility((ActivityClientRecord)localObject, paramBoolean);
    }
    mSomeActivitiesChanged = true;
  }
  
  public void installSystemApplicationInfo(ApplicationInfo paramApplicationInfo, ClassLoader paramClassLoader)
  {
    try
    {
      getSystemContext().installSystemApplicationInfo(paramApplicationInfo, paramClassLoader);
      getSystemUiContext().installSystemApplicationInfo(paramApplicationInfo, paramClassLoader);
      paramApplicationInfo = new android/app/ActivityThread$Profiler;
      paramApplicationInfo.<init>();
      mProfiler = paramApplicationInfo;
      return;
    }
    finally {}
  }
  
  public final void installSystemProviders(List<ProviderInfo> paramList)
  {
    if (paramList != null) {
      installContentProviders(mInitialApplication, paramList);
    }
  }
  
  public boolean isProfiling()
  {
    boolean bool;
    if ((mProfiler != null) && (mProfiler.profileFile != null) && (mProfiler.profileFd == null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isResumedActivityFullscreen()
  {
    ActivityClientRecord localActivityClientRecord = mLastResumedActivity;
    boolean bool = false;
    if ((localActivityClientRecord != null) && (mLastResumedActivity.window != null))
    {
      int i = mLastResumedActivity.window.getAttributes().systemUiVisibility;
      int j = mLastResumedActivity.window.getAttributes().flags;
      if (((i & 0x4) == 0) && ((j & 0x400) == 0)) {
        break label76;
      }
      bool = true;
      label76:
      return bool;
    }
    return false;
  }
  
  public void onNewActivityOptions(IBinder paramIBinder, ActivityOptions paramActivityOptions)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (paramIBinder != null) {
      activity.onNewActivityOptions(paramActivityOptions);
    }
  }
  
  public final LoadedApk peekPackageInfo(String paramString, boolean paramBoolean)
  {
    ResourcesManager localResourcesManager = mResourcesManager;
    if (paramBoolean) {
      try
      {
        paramString = (WeakReference)mPackages.get(paramString);
      }
      finally
      {
        break label63;
      }
    } else {
      paramString = (WeakReference)mResourcePackages.get(paramString);
    }
    if (paramString != null) {
      paramString = (LoadedApk)paramString.get();
    } else {
      paramString = null;
    }
    return paramString;
    label63:
    throw paramString;
  }
  
  ActivityClientRecord performDestroyActivity(IBinder paramIBinder, boolean paramBoolean1, int paramInt, boolean paramBoolean2, String paramString)
  {
    ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mActivities.get(paramIBinder);
    paramString = null;
    if (localActivityClientRecord != null)
    {
      paramString = activity.getClass();
      Activity localActivity = activity;
      mConfigChangeFlags |= paramInt;
      if (paramBoolean1) {
        activity.mFinished = true;
      }
      performPauseActivityIfNeeded(localActivityClientRecord, "destroy");
      if (!stopped) {
        callActivityOnStop(localActivityClientRecord, false, "destroy");
      }
      if (paramBoolean2) {
        try
        {
          lastNonConfigurationInstances = activity.retainNonConfigurationInstances();
        }
        catch (Exception localException1)
        {
          if (!mInstrumentation.onException(activity, localException1))
          {
            paramIBinder = new StringBuilder();
            paramIBinder.append("Unable to retain activity ");
            paramIBinder.append(intent.getComponent().toShortString());
            paramIBinder.append(": ");
            paramIBinder.append(localException1.toString());
            throw new RuntimeException(paramIBinder.toString(), localException1);
          }
        }
      }
      try
      {
        activity.mCalled = false;
        mInstrumentation.callActivityOnDestroy(activity);
        if (activity.mCalled)
        {
          if (window != null) {
            window.closeAllPanels();
          }
        }
        else
        {
          SuperNotCalledException localSuperNotCalledException = new android/util/SuperNotCalledException;
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Activity ");
          localStringBuilder.append(safeToComponentShortString(intent));
          localStringBuilder.append(" did not call through to super.onDestroy()");
          localSuperNotCalledException.<init>(localStringBuilder.toString());
          throw localSuperNotCalledException;
        }
      }
      catch (Exception localException2)
      {
        if (mInstrumentation.onException(activity, localException2))
        {
          localActivityClientRecord.setState(6);
        }
        else
        {
          paramIBinder = new StringBuilder();
          paramIBinder.append("Unable to destroy activity ");
          paramIBinder.append(safeToComponentShortString(intent));
          paramIBinder.append(": ");
          paramIBinder.append(localException2.toString());
          throw new RuntimeException(paramIBinder.toString(), localException2);
        }
      }
      catch (SuperNotCalledException paramIBinder)
      {
        throw paramIBinder;
      }
    }
    mActivities.remove(paramIBinder);
    StrictMode.decrementExpectedActivityCount(paramString);
    return localActivityClientRecord;
  }
  
  void performNewIntents(IBinder paramIBinder, List<ReferrerIntent> paramList, boolean paramBoolean)
  {
    ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (localActivityClientRecord == null) {
      return;
    }
    boolean bool = paused ^ true;
    if (bool)
    {
      activity.mTemporaryPause = true;
      mInstrumentation.callActivityOnPause(activity);
    }
    checkAndBlockForNetworkAccess();
    deliverNewIntents(localActivityClientRecord, paramList);
    if (bool)
    {
      activity.performResume(false, "performNewIntents");
      activity.mTemporaryPause = false;
    }
    if ((paused) && (paramBoolean))
    {
      performResumeActivity(paramIBinder, false, "performNewIntents");
      performPauseActivityIfNeeded(localActivityClientRecord, "performNewIntents");
    }
  }
  
  final Bundle performPauseActivity(IBinder paramIBinder, boolean paramBoolean, String paramString, PendingTransactionActions paramPendingTransactionActions)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (paramIBinder != null) {
      paramIBinder = performPauseActivity(paramIBinder, paramBoolean, paramString, paramPendingTransactionActions);
    } else {
      paramIBinder = null;
    }
    return paramIBinder;
  }
  
  public void performRestartActivity(IBinder paramIBinder, boolean paramBoolean)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if (stopped)
    {
      activity.performRestart(paramBoolean, "performRestartActivity");
      if (paramBoolean) {
        paramIBinder.setState(2);
      }
    }
  }
  
  @VisibleForTesting
  public ActivityClientRecord performResumeActivity(IBinder paramIBinder, boolean paramBoolean, String paramString)
  {
    paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
    if ((paramIBinder != null) && (!activity.mFinished))
    {
      if (paramIBinder.getLifecycleState() == 3)
      {
        if (!paramBoolean)
        {
          paramString = new IllegalStateException("Trying to resume activity which is already resumed");
          Slog.e("ActivityThread", paramString.getMessage(), paramString);
          Slog.e("ActivityThread", paramIBinder.getStateString());
        }
        return null;
      }
      if (paramBoolean)
      {
        hideForNow = false;
        activity.mStartedActivity = false;
      }
      try
      {
        activity.onStateNotSaved();
        activity.mFragments.noteStateNotSaved();
        checkAndBlockForNetworkAccess();
        if (pendingIntents != null)
        {
          deliverNewIntents(paramIBinder, pendingIntents);
          pendingIntents = null;
        }
        if (pendingResults != null)
        {
          deliverResults(paramIBinder, pendingResults, paramString);
          pendingResults = null;
        }
        activity.performResume(startsNotResumed, paramString);
        state = null;
        persistentState = null;
        paramIBinder.setState(3);
        if (Build.FEATURES.ENABLE_NOTCH_UI) {
          mLastResumedActivity = paramIBinder;
        }
      }
      catch (Exception paramString)
      {
        if (!mInstrumentation.onException(activity, paramString)) {
          break label215;
        }
      }
      return paramIBinder;
      label215:
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unable to resume activity ");
      localStringBuilder.append(intent.getComponent().toShortString());
      localStringBuilder.append(": ");
      localStringBuilder.append(paramString.toString());
      throw new RuntimeException(localStringBuilder.toString(), paramString);
    }
    return null;
  }
  
  final void performStopActivity(IBinder paramIBinder, boolean paramBoolean, String paramString)
  {
    performStopActivityInner((ActivityClientRecord)mActivities.get(paramIBinder), null, false, paramBoolean, false, paramString);
  }
  
  final void performUserLeavingActivity(ActivityClientRecord paramActivityClientRecord)
  {
    mInstrumentation.callActivityOnUserLeaving(activity);
  }
  
  public ActivityClientRecord prepareRelaunchActivity(IBinder paramIBinder, List<ResultInfo> paramList, List<ReferrerIntent> paramList1, int paramInt, MergedConfiguration paramMergedConfiguration, boolean paramBoolean)
  {
    Object localObject1 = null;
    int i = 0;
    ResourcesManager localResourcesManager = mResourcesManager;
    int j = 0;
    for (;;)
    {
      Object localObject2 = localObject1;
      try
      {
        if (j < mRelaunchingActivities.size())
        {
          ActivityClientRecord localActivityClientRecord = (ActivityClientRecord)mRelaunchingActivities.get(j);
          if (token == paramIBinder)
          {
            localObject1 = localActivityClientRecord;
            if (paramList != null) {
              if (pendingResults != null) {
                pendingResults.addAll(paramList);
              } else {
                pendingResults = paramList;
              }
            }
            localObject2 = localObject1;
            if (paramList1 != null) {
              if (pendingIntents != null)
              {
                pendingIntents.addAll(paramList1);
                localObject2 = localObject1;
              }
              else
              {
                pendingIntents = paramList1;
                localObject2 = localObject1;
              }
            }
          }
          else
          {
            j++;
            continue;
          }
        }
        localObject1 = localObject2;
        j = i;
        if (localObject2 == null)
        {
          localObject1 = new android/app/ActivityThread$ActivityClientRecord;
          ((ActivityClientRecord)localObject1).<init>();
          token = paramIBinder;
          pendingResults = paramList;
          pendingIntents = paramList1;
          mPreserveWindow = paramBoolean;
          mRelaunchingActivities.add(localObject1);
          j = 1;
        }
        createdConfig = paramMergedConfiguration.getGlobalConfiguration();
        overrideConfig = paramMergedConfiguration.getOverrideConfiguration();
        pendingConfigChanges |= paramInt;
        if (j != 0) {
          paramIBinder = (IBinder)localObject1;
        } else {
          paramIBinder = null;
        }
        return paramIBinder;
      }
      finally {}
    }
  }
  
  public void registerOnActivityPausedListener(Activity paramActivity, OnActivityPausedListener paramOnActivityPausedListener)
  {
    synchronized (mOnPauseListeners)
    {
      ArrayList localArrayList1 = (ArrayList)mOnPauseListeners.get(paramActivity);
      ArrayList localArrayList2 = localArrayList1;
      if (localArrayList1 == null)
      {
        localArrayList2 = new java/util/ArrayList;
        localArrayList2.<init>();
        mOnPauseListeners.put(paramActivity, localArrayList2);
      }
      localArrayList2.add(paramOnActivityPausedListener);
      return;
    }
  }
  
  public final boolean releaseProvider(IContentProvider arg1, boolean paramBoolean)
  {
    int i = 0;
    if (??? == null) {
      return false;
    }
    Object localObject1 = ???.asBinder();
    synchronized (mProviderMap)
    {
      localObject1 = (ProviderRefCount)mProviderRefCountMap.get(localObject1);
      if (localObject1 == null) {
        return false;
      }
      int j = 0;
      if (paramBoolean)
      {
        if (stableCount == 0) {
          return false;
        }
        stableCount -= 1;
        if (stableCount == 0)
        {
          j = unstableCount;
          if (j == 0) {
            j = 1;
          } else {
            j = 0;
          }
          try
          {
            IActivityManager localIActivityManager = ActivityManager.getService();
            IBinder localIBinder = holder.connection;
            if (j != 0) {
              i = 1;
            }
            localIActivityManager.refContentProvider(localIBinder, -1, i);
          }
          catch (RemoteException localRemoteException1) {}
        }
      }
      else
      {
        if (unstableCount == 0) {
          return false;
        }
        unstableCount -= 1;
        if (unstableCount == 0)
        {
          j = stableCount;
          if (j == 0) {
            j = 1;
          } else {
            j = 0;
          }
          i = j;
          j = i;
          if (i == 0) {
            try
            {
              ActivityManager.getService().refContentProvider(holder.connection, 0, -1);
              j = i;
            }
            catch (RemoteException localRemoteException2)
            {
              j = i;
            }
          }
        }
      }
      if (j != 0) {
        if (!removePending)
        {
          removePending = true;
          localObject1 = mH.obtainMessage(131, localObject1);
          mH.sendMessage((Message)localObject1);
        }
        else
        {
          StringBuilder localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("Duplicate remove pending of provider ");
          localStringBuilder.append(holder.info.name);
          Slog.w("ActivityThread", localStringBuilder.toString());
        }
      }
      return true;
    }
  }
  
  public void reportRelaunch(IBinder paramIBinder, PendingTransactionActions paramPendingTransactionActions)
  {
    try
    {
      ActivityManager.getService().activityRelaunched(paramIBinder);
      paramIBinder = (ActivityClientRecord)mActivities.get(paramIBinder);
      if ((paramPendingTransactionActions.shouldReportRelaunchToWindowManager()) && (paramIBinder != null) && (window != null)) {
        window.reportActivityRelaunched();
      }
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public void reportStop(PendingTransactionActions paramPendingTransactionActions)
  {
    mH.post(paramPendingTransactionActions.getStopInfo());
  }
  
  public final ActivityInfo resolveActivityInfo(Intent paramIntent)
  {
    ActivityInfo localActivityInfo = paramIntent.resolveActivityInfo(mInitialApplication.getPackageManager(), 1024);
    if (localActivityInfo == null) {
      Instrumentation.checkStartActivityResult(-92, paramIntent);
    }
    return localActivityInfo;
  }
  
  final void scheduleContextCleanup(ContextImpl paramContextImpl, String paramString1, String paramString2)
  {
    ContextCleanupInfo localContextCleanupInfo = new ContextCleanupInfo();
    context = paramContextImpl;
    who = paramString1;
    what = paramString2;
    sendMessage(119, localContextCleanupInfo);
  }
  
  void scheduleGcIdler()
  {
    if (!mGcIdlerScheduled)
    {
      mGcIdlerScheduled = true;
      Looper.myQueue().addIdleHandler(mGcIdler);
    }
    mH.removeMessages(120);
  }
  
  void scheduleRelaunchActivity(IBinder paramIBinder)
  {
    sendMessage(160, paramIBinder);
  }
  
  public final void sendActivityResult(IBinder paramIBinder, String paramString, int paramInt1, int paramInt2, Intent paramIntent)
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new ResultInfo(paramString, paramInt1, paramInt2, paramIntent));
    paramIBinder = ClientTransaction.obtain(mAppThread, paramIBinder);
    paramIBinder.addCallback(ActivityResultItem.obtain(localArrayList));
    try
    {
      mAppThread.scheduleTransaction(paramIBinder);
    }
    catch (RemoteException paramIBinder) {}
  }
  
  void sendMessage(int paramInt, Object paramObject)
  {
    sendMessage(paramInt, paramObject, 0, 0, false);
  }
  
  public final Activity startActivityNow(Activity paramActivity, String paramString, Intent paramIntent, ActivityInfo paramActivityInfo, IBinder paramIBinder, Bundle paramBundle, Activity.NonConfigurationInstances paramNonConfigurationInstances)
  {
    ActivityClientRecord localActivityClientRecord = new ActivityClientRecord();
    token = paramIBinder;
    ident = 0;
    intent = paramIntent;
    state = paramBundle;
    parent = paramActivity;
    embeddedID = paramString;
    activityInfo = paramActivityInfo;
    lastNonConfigurationInstances = paramNonConfigurationInstances;
    return performLaunchActivity(localActivityClientRecord, null);
  }
  
  public void stopProfiling()
  {
    if (mProfiler != null) {
      mProfiler.stopProfiling();
    }
  }
  
  public void unregisterOnActivityPausedListener(Activity paramActivity, OnActivityPausedListener paramOnActivityPausedListener)
  {
    synchronized (mOnPauseListeners)
    {
      paramActivity = (ArrayList)mOnPauseListeners.get(paramActivity);
      if (paramActivity != null) {
        paramActivity.remove(paramOnActivityPausedListener);
      }
      return;
    }
  }
  
  void unscheduleGcIdler()
  {
    if (mGcIdlerScheduled)
    {
      mGcIdlerScheduled = false;
      Looper.myQueue().removeIdleHandler(mGcIdler);
    }
    mH.removeMessages(120);
  }
  
  public void updatePendingConfiguration(Configuration paramConfiguration)
  {
    mAppThread.updatePendingConfiguration(paramConfiguration);
  }
  
  public void updateProcessState(int paramInt, boolean paramBoolean)
  {
    mAppThread.updateProcessState(paramInt, paramBoolean);
  }
  
  public static final class ActivityClientRecord
  {
    Activity activity;
    ActivityInfo activityInfo;
    CompatibilityInfo compatInfo;
    ViewRootImpl.ActivityConfigCallback configCallback;
    Configuration createdConfig;
    String embeddedID;
    boolean hideForNow;
    int ident;
    Intent intent;
    public final boolean isForward;
    Activity.NonConfigurationInstances lastNonConfigurationInstances;
    private int mLifecycleState = 0;
    Window mPendingRemoveWindow;
    WindowManager mPendingRemoveWindowManager;
    boolean mPreserveWindow;
    Configuration newConfig;
    ActivityClientRecord nextIdle;
    Configuration overrideConfig;
    public LoadedApk packageInfo;
    Activity parent;
    boolean paused;
    int pendingConfigChanges;
    List<ReferrerIntent> pendingIntents;
    List<ResultInfo> pendingResults;
    PersistableBundle persistentState;
    ProfilerInfo profilerInfo;
    String referrer;
    boolean startsNotResumed;
    Bundle state;
    boolean stopped;
    private Configuration tmpConfig = new Configuration();
    public IBinder token;
    IVoiceInteractor voiceInteractor;
    Window window;
    
    @VisibleForTesting
    public ActivityClientRecord()
    {
      isForward = false;
      init();
    }
    
    public ActivityClientRecord(IBinder paramIBinder, Intent paramIntent, int paramInt, ActivityInfo paramActivityInfo, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo, String paramString, IVoiceInteractor paramIVoiceInteractor, Bundle paramBundle, PersistableBundle paramPersistableBundle, List<ResultInfo> paramList, List<ReferrerIntent> paramList1, boolean paramBoolean, ProfilerInfo paramProfilerInfo, ClientTransactionHandler paramClientTransactionHandler)
    {
      token = paramIBinder;
      ident = paramInt;
      intent = paramIntent;
      referrer = paramString;
      voiceInteractor = paramIVoiceInteractor;
      activityInfo = paramActivityInfo;
      compatInfo = paramCompatibilityInfo;
      state = paramBundle;
      persistentState = paramPersistableBundle;
      pendingResults = paramList;
      pendingIntents = paramList1;
      isForward = paramBoolean;
      profilerInfo = paramProfilerInfo;
      overrideConfig = paramConfiguration;
      packageInfo = paramClientTransactionHandler.getPackageInfoNoCheck(activityInfo.applicationInfo, paramCompatibilityInfo);
      init();
    }
    
    private void init()
    {
      parent = null;
      embeddedID = null;
      paused = false;
      stopped = false;
      hideForNow = false;
      nextIdle = null;
      configCallback = new _..Lambda.ActivityThread.ActivityClientRecord.HOrG1qglSjSUHSjKBn2rXtX0gGg(this);
    }
    
    private boolean isPreHoneycomb()
    {
      boolean bool;
      if ((activity != null) && (activity.getApplicationInfo().targetSdkVersion < 11)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    private boolean isPreP()
    {
      boolean bool;
      if ((activity != null) && (activity.getApplicationInfo().targetSdkVersion < 28)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public int getLifecycleState()
    {
      return mLifecycleState;
    }
    
    public String getStateString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ActivityClientRecord{");
      localStringBuilder.append("paused=");
      localStringBuilder.append(paused);
      localStringBuilder.append(", stopped=");
      localStringBuilder.append(stopped);
      localStringBuilder.append(", hideForNow=");
      localStringBuilder.append(hideForNow);
      localStringBuilder.append(", startsNotResumed=");
      localStringBuilder.append(startsNotResumed);
      localStringBuilder.append(", isForward=");
      localStringBuilder.append(isForward);
      localStringBuilder.append(", pendingConfigChanges=");
      localStringBuilder.append(pendingConfigChanges);
      localStringBuilder.append(", preserveWindow=");
      localStringBuilder.append(mPreserveWindow);
      if (activity != null)
      {
        localStringBuilder.append(", Activity{");
        localStringBuilder.append("resumed=");
        localStringBuilder.append(activity.mResumed);
        localStringBuilder.append(", stopped=");
        localStringBuilder.append(activity.mStopped);
        localStringBuilder.append(", finished=");
        localStringBuilder.append(activity.isFinishing());
        localStringBuilder.append(", destroyed=");
        localStringBuilder.append(activity.isDestroyed());
        localStringBuilder.append(", startedActivity=");
        localStringBuilder.append(activity.mStartedActivity);
        localStringBuilder.append(", temporaryPause=");
        localStringBuilder.append(activity.mTemporaryPause);
        localStringBuilder.append(", changingConfigurations=");
        localStringBuilder.append(activity.mChangingConfigurations);
        localStringBuilder.append("}");
      }
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
    
    public boolean isPersistable()
    {
      boolean bool;
      if (activityInfo.persistableMode == 2) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isVisibleFromServer()
    {
      boolean bool;
      if ((activity != null) && (activity.mVisibleFromServer)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void setState(int paramInt)
    {
      mLifecycleState = paramInt;
      switch (mLifecycleState)
      {
      default: 
        break;
      case 5: 
        paused = true;
        stopped = true;
        break;
      case 4: 
        paused = true;
        stopped = false;
        break;
      case 3: 
        paused = false;
        stopped = false;
        break;
      case 2: 
        paused = true;
        stopped = false;
        break;
      case 1: 
        paused = true;
        stopped = true;
      }
    }
    
    public String toString()
    {
      Object localObject;
      if (intent != null) {
        localObject = intent.getComponent();
      } else {
        localObject = null;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ActivityRecord{");
      localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
      localStringBuilder.append(" token=");
      localStringBuilder.append(token);
      localStringBuilder.append(" ");
      if (localObject == null) {
        localObject = "no component name";
      } else {
        localObject = ((ComponentName)localObject).toShortString();
      }
      localStringBuilder.append((String)localObject);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  static final class AppBindData
  {
    ApplicationInfo appInfo;
    boolean autofillCompatibilityEnabled;
    String buildSerial;
    CompatibilityInfo compatInfo;
    Configuration config;
    int debugMode;
    boolean enableBinderTracking;
    LoadedApk info;
    ProfilerInfo initProfilerInfo;
    Bundle instrumentationArgs;
    ComponentName instrumentationName;
    IUiAutomationConnection instrumentationUiAutomationConnection;
    IInstrumentationWatcher instrumentationWatcher;
    boolean persistent;
    String processName;
    List<ProviderInfo> providers;
    boolean restrictedBackupMode;
    boolean trackAllocation;
    
    AppBindData() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("AppBindData{appInfo=");
      localStringBuilder.append(appInfo);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  private class ApplicationThread
    extends IApplicationThread.Stub
  {
    private static final String DB_INFO_FORMAT = "  %8s %8s %14s %14s  %s";
    private int mLastProcessState = -1;
    
    private ApplicationThread() {}
    
    private void dumpDatabaseInfo(ParcelFileDescriptor paramParcelFileDescriptor, String[] paramArrayOfString)
    {
      paramParcelFileDescriptor = new FastPrintWriter(new FileOutputStream(paramParcelFileDescriptor.getFileDescriptor()));
      SQLiteDebug.dump(new PrintWriterPrinter(paramParcelFileDescriptor), paramArrayOfString);
      paramParcelFileDescriptor.flush();
    }
    
    private void dumpMemInfo(ProtoOutputStream paramProtoOutputStream, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
    {
      long l1 = Debug.getNativeHeapSize() / 1024L;
      long l2 = Debug.getNativeHeapAllocatedSize() / 1024L;
      long l3 = Debug.getNativeHeapFreeSize() / 1024L;
      Object localObject = Runtime.getRuntime();
      ((Runtime)localObject).gc();
      long l4 = ((Runtime)localObject).totalMemory() / 1024L;
      long l5 = ((Runtime)localObject).freeMemory() / 1024L;
      paramBoolean1 = false;
      localObject = VMDebug.countInstancesOfClasses(new Class[] { ContextImpl.class, Activity.class, WebView.class, OpenSSLSocketImpl.class }, true);
      long l6 = localObject[0];
      long l7 = localObject[1];
      long l8 = localObject[2];
      long l9 = localObject[3];
      long l10 = ViewDebug.getViewInstanceCount();
      long l11 = ViewDebug.getViewRootImplCount();
      int i = AssetManager.getGlobalAssetCount();
      int j = AssetManager.getGlobalAssetManagerCount();
      int k = Debug.getBinderLocalObjectCount();
      int m = Debug.getBinderProxyObjectCount();
      int n = Debug.getBinderDeathObjectCount();
      long l12 = Parcel.getGlobalAllocSize();
      long l13 = Parcel.getGlobalAllocCount();
      SQLiteDebug.PagerStats localPagerStats = SQLiteDebug.getDatabaseInfo();
      long l14 = paramProtoOutputStream.start(1146756268033L);
      paramProtoOutputStream.write(1120986464257L, Process.myPid());
      if (mBoundApplication != null) {
        localObject = mBoundApplication.processName;
      } else {
        localObject = "unknown";
      }
      paramProtoOutputStream.write(1138166333442L, (String)localObject);
      ActivityThread.dumpMemInfoTable(paramProtoOutputStream, paramMemoryInfo, paramBoolean2, paramBoolean3, l1, l2, l3, l4, l4 - l5, l5);
      paramProtoOutputStream.end(l14);
      l14 = paramProtoOutputStream.start(1146756268034L);
      paramProtoOutputStream.write(1120986464257L, l10);
      paramProtoOutputStream.write(1120986464258L, l11);
      paramProtoOutputStream.write(1120986464259L, l6);
      paramProtoOutputStream.write(1120986464260L, l7);
      paramProtoOutputStream.write(1120986464261L, i);
      paramProtoOutputStream.write(1120986464262L, j);
      paramProtoOutputStream.write(1120986464263L, k);
      paramProtoOutputStream.write(1120986464264L, m);
      paramProtoOutputStream.write(1112396529673L, l12 / 1024L);
      paramProtoOutputStream.write(1120986464266L, l13);
      paramProtoOutputStream.write(1120986464267L, n);
      paramProtoOutputStream.write(1120986464268L, l9);
      paramProtoOutputStream.write(1120986464269L, l8);
      paramProtoOutputStream.end(l14);
      l12 = paramProtoOutputStream.start(1146756268035L);
      paramMemoryInfo = localPagerStats;
      paramProtoOutputStream.write(1120986464257L, memoryUsed / 1024);
      paramProtoOutputStream.write(1120986464258L, pageCacheOverflow / 1024);
      paramProtoOutputStream.write(1120986464259L, largestMemAlloc / 1024);
      k = dbStats.size();
      for (i = 0; i < k; i++)
      {
        localObject = (SQLiteDebug.DbStats)dbStats.get(i);
        l8 = paramProtoOutputStream.start(2246267895812L);
        paramProtoOutputStream.write(1138166333441L, dbName);
        paramProtoOutputStream.write(1120986464258L, pageSize);
        paramProtoOutputStream.write(1120986464259L, dbSize);
        paramProtoOutputStream.write(1120986464260L, lookaside);
        paramProtoOutputStream.write(1138166333445L, cache);
        paramProtoOutputStream.end(l8);
      }
      paramProtoOutputStream.end(l12);
      paramMemoryInfo = AssetManager.getAssetAllocations();
      if (paramMemoryInfo != null) {
        paramProtoOutputStream.write(1138166333444L, paramMemoryInfo);
      }
      if (paramBoolean4)
      {
        if (mBoundApplication == null) {
          k = 0;
        } else {
          k = mBoundApplication.appInfo.flags;
        }
        if (((k & 0x2) == 0) && (!Build.IS_DEBUGGABLE)) {
          break label638;
        }
        paramBoolean1 = true;
        label638:
        paramProtoOutputStream.write(1138166333445L, Debug.getUnreachableMemory(100, paramBoolean1));
      }
    }
    
    private void dumpMemInfo(PrintWriter paramPrintWriter, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
    {
      long l1 = Debug.getNativeHeapSize() / 1024L;
      long l2 = Debug.getNativeHeapAllocatedSize() / 1024L;
      long l3 = Debug.getNativeHeapFreeSize() / 1024L;
      Object localObject1 = Runtime.getRuntime();
      ((Runtime)localObject1).gc();
      long l4 = ((Runtime)localObject1).totalMemory() / 1024L;
      long l5 = ((Runtime)localObject1).freeMemory() / 1024L;
      boolean bool = false;
      int i = 0;
      localObject1 = VMDebug.countInstancesOfClasses(new Class[] { ContextImpl.class, Activity.class, WebView.class, OpenSSLSocketImpl.class }, true);
      long l6 = localObject1[0];
      long l7 = localObject1[1];
      long l8 = localObject1[2];
      long l9 = localObject1[3];
      long l10 = ViewDebug.getViewInstanceCount();
      long l11 = ViewDebug.getViewRootImplCount();
      int j = AssetManager.getGlobalAssetCount();
      int k = AssetManager.getGlobalAssetManagerCount();
      int m = Debug.getBinderLocalObjectCount();
      int n = Debug.getBinderProxyObjectCount();
      int i1 = Debug.getBinderDeathObjectCount();
      long l12 = Parcel.getGlobalAllocSize();
      long l13 = Parcel.getGlobalAllocCount();
      Object localObject2 = SQLiteDebug.getDatabaseInfo();
      int i2 = Process.myPid();
      if (mBoundApplication != null) {
        localObject1 = mBoundApplication.processName;
      } else {
        localObject1 = "unknown";
      }
      ActivityThread.dumpMemInfoTable(paramPrintWriter, paramMemoryInfo, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, i2, (String)localObject1, l1, l2, l3, l4, l4 - l5, l5);
      if (paramBoolean1)
      {
        paramPrintWriter.print(l10);
        paramPrintWriter.print(',');
        paramPrintWriter.print(l11);
        paramPrintWriter.print(',');
        paramPrintWriter.print(l6);
        paramPrintWriter.print(',');
        paramPrintWriter.print(l7);
        paramPrintWriter.print(',');
        paramPrintWriter.print(j);
        paramPrintWriter.print(',');
        paramPrintWriter.print(k);
        paramPrintWriter.print(',');
        paramPrintWriter.print(m);
        paramPrintWriter.print(',');
        paramPrintWriter.print(n);
        paramPrintWriter.print(',');
        paramPrintWriter.print(i1);
        paramPrintWriter.print(',');
        paramPrintWriter.print(l9);
        paramPrintWriter.print(',');
        paramPrintWriter.print(memoryUsed / 1024);
        paramPrintWriter.print(',');
        paramPrintWriter.print(memoryUsed / 1024);
        paramPrintWriter.print(',');
        paramPrintWriter.print(pageCacheOverflow / 1024);
        paramPrintWriter.print(',');
        paramPrintWriter.print(largestMemAlloc / 1024);
        while (i < dbStats.size())
        {
          paramMemoryInfo = (SQLiteDebug.DbStats)dbStats.get(i);
          paramPrintWriter.print(',');
          paramPrintWriter.print(dbName);
          paramPrintWriter.print(',');
          paramPrintWriter.print(pageSize);
          paramPrintWriter.print(',');
          paramPrintWriter.print(dbSize);
          paramPrintWriter.print(',');
          paramPrintWriter.print(lookaside);
          paramPrintWriter.print(',');
          paramPrintWriter.print(cache);
          paramPrintWriter.print(',');
          paramPrintWriter.print(cache);
          i++;
        }
        paramPrintWriter.println();
        return;
      }
      paramMemoryInfo = (Debug.MemoryInfo)localObject2;
      paramPrintWriter.println(" ");
      paramPrintWriter.println(" Objects");
      ActivityThread.printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "Views:", Long.valueOf(l10), "ViewRootImpl:", Long.valueOf(l11) });
      ActivityThread.printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "AppContexts:", Long.valueOf(l6), "Activities:", Long.valueOf(l7) });
      ActivityThread.printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "Assets:", Integer.valueOf(j), "AssetManagers:", Integer.valueOf(k) });
      ActivityThread.printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "Local Binders:", Integer.valueOf(m), "Proxy Binders:", Integer.valueOf(n) });
      ActivityThread.printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "Parcel memory:", Long.valueOf(l12 / 1024L), "Parcel count:", Long.valueOf(l13) });
      ActivityThread.printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "Death Recipients:", Integer.valueOf(i1), "OpenSSL Sockets:", Long.valueOf(l9) });
      ActivityThread.printRow(paramPrintWriter, "%21s %8d", new Object[] { "WebViews:", Long.valueOf(l8) });
      paramPrintWriter.println(" ");
      paramPrintWriter.println(" SQL");
      ActivityThread.printRow(paramPrintWriter, "%21s %8d", new Object[] { "MEMORY_USED:", Integer.valueOf(memoryUsed / 1024) });
      ActivityThread.printRow(paramPrintWriter, "%21s %8d %21s %8d", new Object[] { "PAGECACHE_OVERFLOW:", Integer.valueOf(pageCacheOverflow / 1024), "MALLOC_SIZE:", Integer.valueOf(largestMemAlloc / 1024) });
      paramPrintWriter.println(" ");
      k = dbStats.size();
      i = k;
      localObject1 = paramMemoryInfo;
      if (k > 0)
      {
        paramPrintWriter.println(" DATABASES");
        ActivityThread.printRow(paramPrintWriter, "  %8s %8s %14s %14s  %s", new Object[] { "pgsz", "dbsz", "Lookaside(b)", "cache", "Dbname" });
        for (m = 0;; m++)
        {
          i = k;
          localObject1 = paramMemoryInfo;
          if (m >= k) {
            break;
          }
          SQLiteDebug.DbStats localDbStats = (SQLiteDebug.DbStats)dbStats.get(m);
          if (pageSize > 0L) {
            localObject1 = String.valueOf(pageSize);
          } else {
            localObject1 = " ";
          }
          if (dbSize > 0L) {
            localObject2 = String.valueOf(dbSize);
          } else {
            localObject2 = " ";
          }
          String str;
          if (lookaside > 0) {
            str = String.valueOf(lookaside);
          } else {
            str = " ";
          }
          ActivityThread.printRow(paramPrintWriter, "  %8s %8s %14s %14s  %s", new Object[] { localObject1, localObject2, str, cache, dbName });
        }
      }
      paramMemoryInfo = AssetManager.getAssetAllocations();
      if (paramMemoryInfo != null)
      {
        paramPrintWriter.println(" ");
        paramPrintWriter.println(" Asset Allocations");
        paramPrintWriter.print(paramMemoryInfo);
      }
      if (paramBoolean5)
      {
        if ((mBoundApplication == null) || ((0x2 & mBoundApplication.appInfo.flags) == 0))
        {
          paramBoolean1 = bool;
          if (!Build.IS_DEBUGGABLE) {}
        }
        else
        {
          paramBoolean1 = true;
        }
        paramPrintWriter.println(" ");
        paramPrintWriter.println(" Unreachable memory");
        paramPrintWriter.print(Debug.getUnreachableMemory(100, paramBoolean1));
      }
    }
    
    private void updatePendingConfiguration(Configuration paramConfiguration)
    {
      synchronized (mResourcesManager)
      {
        if ((mPendingConfiguration == null) || (mPendingConfiguration.isOtherSeqNewer(paramConfiguration))) {
          mPendingConfiguration = paramConfiguration;
        }
        return;
      }
    }
    
    public void attachAgent(String paramString)
    {
      sendMessage(155, paramString);
    }
    
    public final void bindApplication(String paramString1, ApplicationInfo paramApplicationInfo, List<ProviderInfo> paramList, ComponentName paramComponentName, ProfilerInfo paramProfilerInfo, Bundle paramBundle1, IInstrumentationWatcher paramIInstrumentationWatcher, IUiAutomationConnection paramIUiAutomationConnection, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, Configuration paramConfiguration, CompatibilityInfo paramCompatibilityInfo, Map paramMap, Bundle paramBundle2, String paramString2, boolean paramBoolean5)
    {
      if (paramMap != null) {
        ServiceManager.initServiceCache(paramMap);
      }
      setCoreSettings(paramBundle2);
      paramMap = new ActivityThread.AppBindData();
      processName = paramString1;
      appInfo = paramApplicationInfo;
      providers = paramList;
      instrumentationName = paramComponentName;
      instrumentationArgs = paramBundle1;
      instrumentationWatcher = paramIInstrumentationWatcher;
      instrumentationUiAutomationConnection = paramIUiAutomationConnection;
      debugMode = paramInt;
      enableBinderTracking = paramBoolean1;
      trackAllocation = paramBoolean2;
      restrictedBackupMode = paramBoolean3;
      persistent = paramBoolean4;
      config = paramConfiguration;
      compatInfo = paramCompatibilityInfo;
      initProfilerInfo = paramProfilerInfo;
      buildSerial = paramString2;
      autofillCompatibilityEnabled = paramBoolean5;
      sendMessage(110, paramMap);
    }
    
    public void clearDnsCache()
    {
      InetAddress.clearDnsCache();
      NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
    }
    
    public void dispatchPackageBroadcast(int paramInt, String[] paramArrayOfString)
    {
      ActivityThread.this.sendMessage(133, paramArrayOfString, paramInt);
    }
    
    /* Error */
    public void dumpActivity(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String paramString, String[] paramArrayOfString)
    {
      // Byte code:
      //   0: new 527	android/app/ActivityThread$DumpComponentInfo
      //   3: dup
      //   4: invokespecial 528	android/app/ActivityThread$DumpComponentInfo:<init>	()V
      //   7: astore 5
      //   9: aload 5
      //   11: aload_1
      //   12: invokevirtual 532	android/os/ParcelFileDescriptor:dup	()Landroid/os/ParcelFileDescriptor;
      //   15: putfield 536	android/app/ActivityThread$DumpComponentInfo:fd	Landroid/os/ParcelFileDescriptor;
      //   18: aload 5
      //   20: aload_2
      //   21: putfield 540	android/app/ActivityThread$DumpComponentInfo:token	Landroid/os/IBinder;
      //   24: aload 5
      //   26: aload_3
      //   27: putfield 543	android/app/ActivityThread$DumpComponentInfo:prefix	Ljava/lang/String;
      //   30: aload 5
      //   32: aload 4
      //   34: putfield 547	android/app/ActivityThread$DumpComponentInfo:args	[Ljava/lang/String;
      //   37: aload_0
      //   38: getfield 21	android/app/ActivityThread$ApplicationThread:this$0	Landroid/app/ActivityThread;
      //   41: sipush 136
      //   44: aload 5
      //   46: iconst_0
      //   47: iconst_0
      //   48: iconst_1
      //   49: invokestatic 551	android/app/ActivityThread:access$300	(Landroid/app/ActivityThread;ILjava/lang/Object;IIZ)V
      //   52: goto +19 -> 71
      //   55: astore_2
      //   56: goto +20 -> 76
      //   59: astore_2
      //   60: ldc_w 553
      //   63: ldc_w 555
      //   66: aload_2
      //   67: invokestatic 561	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   70: pop
      //   71: aload_1
      //   72: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   75: return
      //   76: aload_1
      //   77: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   80: aload_2
      //   81: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	82	0	this	ApplicationThread
      //   0	82	1	paramParcelFileDescriptor	ParcelFileDescriptor
      //   0	82	2	paramIBinder	IBinder
      //   0	82	3	paramString	String
      //   0	82	4	paramArrayOfString	String[]
      //   7	38	5	localDumpComponentInfo	ActivityThread.DumpComponentInfo
      // Exception table:
      //   from	to	target	type
      //   9	52	55	finally
      //   60	71	55	finally
      //   9	52	59	java/io/IOException
    }
    
    /* Error */
    public void dumpDbInfo(ParcelFileDescriptor paramParcelFileDescriptor, final String[] paramArrayOfString)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 21	android/app/ActivityThread$ApplicationThread:this$0	Landroid/app/ActivityThread;
      //   4: getfield 571	android/app/ActivityThread:mSystemThread	Z
      //   7: ifeq +88 -> 95
      //   10: aload_1
      //   11: invokevirtual 532	android/os/ParcelFileDescriptor:dup	()Landroid/os/ParcelFileDescriptor;
      //   14: astore_3
      //   15: aload_1
      //   16: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   19: getstatic 577	android/os/AsyncTask:THREAD_POOL_EXECUTOR	Ljava/util/concurrent/Executor;
      //   22: new 9	android/app/ActivityThread$ApplicationThread$1
      //   25: dup
      //   26: aload_0
      //   27: aload_3
      //   28: aload_2
      //   29: invokespecial 579	android/app/ActivityThread$ApplicationThread$1:<init>	(Landroid/app/ActivityThread$ApplicationThread;Landroid/os/ParcelFileDescriptor;[Ljava/lang/String;)V
      //   32: invokeinterface 585 2 0
      //   37: goto +68 -> 105
      //   40: astore_2
      //   41: goto +48 -> 89
      //   44: astore_2
      //   45: new 587	java/lang/StringBuilder
      //   48: astore_2
      //   49: aload_2
      //   50: invokespecial 588	java/lang/StringBuilder:<init>	()V
      //   53: aload_2
      //   54: ldc_w 590
      //   57: invokevirtual 594	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   60: pop
      //   61: aload_2
      //   62: aload_1
      //   63: invokevirtual 52	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
      //   66: invokevirtual 599	java/io/FileDescriptor:getInt$	()I
      //   69: invokevirtual 602	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   72: pop
      //   73: ldc_w 553
      //   76: aload_2
      //   77: invokevirtual 605	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   80: invokestatic 610	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   83: pop
      //   84: aload_1
      //   85: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   88: return
      //   89: aload_1
      //   90: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   93: aload_2
      //   94: athrow
      //   95: aload_0
      //   96: aload_1
      //   97: aload_2
      //   98: invokespecial 42	android/app/ActivityThread$ApplicationThread:dumpDatabaseInfo	(Landroid/os/ParcelFileDescriptor;[Ljava/lang/String;)V
      //   101: aload_1
      //   102: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   105: return
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	106	0	this	ApplicationThread
      //   0	106	1	paramParcelFileDescriptor	ParcelFileDescriptor
      //   0	106	2	paramArrayOfString	String[]
      //   14	14	3	localParcelFileDescriptor	ParcelFileDescriptor
      // Exception table:
      //   from	to	target	type
      //   10	15	40	finally
      //   45	84	40	finally
      //   10	15	44	java/io/IOException
    }
    
    public void dumpGfxInfo(ParcelFileDescriptor paramParcelFileDescriptor, String[] paramArrayOfString)
    {
      ActivityThread.this.nDumpGraphicsInfo(paramParcelFileDescriptor.getFileDescriptor());
      WindowManagerGlobal.getInstance().dumpGfxInfo(paramParcelFileDescriptor.getFileDescriptor(), paramArrayOfString);
      IoUtils.closeQuietly(paramParcelFileDescriptor);
    }
    
    public void dumpHeap(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString, ParcelFileDescriptor paramParcelFileDescriptor)
    {
      ActivityThread.DumpHeapData localDumpHeapData = new ActivityThread.DumpHeapData();
      managed = paramBoolean1;
      mallocInfo = paramBoolean2;
      runGc = paramBoolean3;
      path = paramString;
      fd = paramParcelFileDescriptor;
      ActivityThread.this.sendMessage(135, localDumpHeapData, 0, 0, true);
    }
    
    public void dumpMemInfo(ParcelFileDescriptor paramParcelFileDescriptor, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, String[] paramArrayOfString)
    {
      paramArrayOfString = new FastPrintWriter(new FileOutputStream(paramParcelFileDescriptor.getFileDescriptor()));
      try
      {
        dumpMemInfo(paramArrayOfString, paramMemoryInfo, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, paramBoolean5);
        return;
      }
      finally
      {
        paramArrayOfString.flush();
        IoUtils.closeQuietly(paramParcelFileDescriptor);
      }
    }
    
    public void dumpMemInfoProto(ParcelFileDescriptor paramParcelFileDescriptor, Debug.MemoryInfo paramMemoryInfo, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, String[] paramArrayOfString)
    {
      paramArrayOfString = new ProtoOutputStream(paramParcelFileDescriptor.getFileDescriptor());
      try
      {
        dumpMemInfo(paramArrayOfString, paramMemoryInfo, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
        return;
      }
      finally
      {
        paramArrayOfString.flush();
        IoUtils.closeQuietly(paramParcelFileDescriptor);
      }
    }
    
    /* Error */
    public void dumpProvider(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String[] paramArrayOfString)
    {
      // Byte code:
      //   0: new 527	android/app/ActivityThread$DumpComponentInfo
      //   3: dup
      //   4: invokespecial 528	android/app/ActivityThread$DumpComponentInfo:<init>	()V
      //   7: astore 4
      //   9: aload 4
      //   11: aload_1
      //   12: invokevirtual 532	android/os/ParcelFileDescriptor:dup	()Landroid/os/ParcelFileDescriptor;
      //   15: putfield 536	android/app/ActivityThread$DumpComponentInfo:fd	Landroid/os/ParcelFileDescriptor;
      //   18: aload 4
      //   20: aload_2
      //   21: putfield 540	android/app/ActivityThread$DumpComponentInfo:token	Landroid/os/IBinder;
      //   24: aload 4
      //   26: aload_3
      //   27: putfield 547	android/app/ActivityThread$DumpComponentInfo:args	[Ljava/lang/String;
      //   30: aload_0
      //   31: getfield 21	android/app/ActivityThread$ApplicationThread:this$0	Landroid/app/ActivityThread;
      //   34: sipush 141
      //   37: aload 4
      //   39: iconst_0
      //   40: iconst_0
      //   41: iconst_1
      //   42: invokestatic 551	android/app/ActivityThread:access$300	(Landroid/app/ActivityThread;ILjava/lang/Object;IIZ)V
      //   45: goto +19 -> 64
      //   48: astore_2
      //   49: goto +20 -> 69
      //   52: astore_2
      //   53: ldc_w 553
      //   56: ldc_w 654
      //   59: aload_2
      //   60: invokestatic 561	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   63: pop
      //   64: aload_1
      //   65: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   68: return
      //   69: aload_1
      //   70: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   73: aload_2
      //   74: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	75	0	this	ApplicationThread
      //   0	75	1	paramParcelFileDescriptor	ParcelFileDescriptor
      //   0	75	2	paramIBinder	IBinder
      //   0	75	3	paramArrayOfString	String[]
      //   7	31	4	localDumpComponentInfo	ActivityThread.DumpComponentInfo
      // Exception table:
      //   from	to	target	type
      //   9	45	48	finally
      //   53	64	48	finally
      //   9	45	52	java/io/IOException
    }
    
    /* Error */
    public void dumpService(ParcelFileDescriptor paramParcelFileDescriptor, IBinder paramIBinder, String[] paramArrayOfString)
    {
      // Byte code:
      //   0: new 527	android/app/ActivityThread$DumpComponentInfo
      //   3: dup
      //   4: invokespecial 528	android/app/ActivityThread$DumpComponentInfo:<init>	()V
      //   7: astore 4
      //   9: aload 4
      //   11: aload_1
      //   12: invokevirtual 532	android/os/ParcelFileDescriptor:dup	()Landroid/os/ParcelFileDescriptor;
      //   15: putfield 536	android/app/ActivityThread$DumpComponentInfo:fd	Landroid/os/ParcelFileDescriptor;
      //   18: aload 4
      //   20: aload_2
      //   21: putfield 540	android/app/ActivityThread$DumpComponentInfo:token	Landroid/os/IBinder;
      //   24: aload 4
      //   26: aload_3
      //   27: putfield 547	android/app/ActivityThread$DumpComponentInfo:args	[Ljava/lang/String;
      //   30: aload_0
      //   31: getfield 21	android/app/ActivityThread$ApplicationThread:this$0	Landroid/app/ActivityThread;
      //   34: bipush 123
      //   36: aload 4
      //   38: iconst_0
      //   39: iconst_0
      //   40: iconst_1
      //   41: invokestatic 551	android/app/ActivityThread:access$300	(Landroid/app/ActivityThread;ILjava/lang/Object;IIZ)V
      //   44: goto +19 -> 63
      //   47: astore_2
      //   48: goto +20 -> 68
      //   51: astore_2
      //   52: ldc_w 553
      //   55: ldc_w 657
      //   58: aload_2
      //   59: invokestatic 561	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   62: pop
      //   63: aload_1
      //   64: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   67: return
      //   68: aload_1
      //   69: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   72: aload_2
      //   73: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	74	0	this	ApplicationThread
      //   0	74	1	paramParcelFileDescriptor	ParcelFileDescriptor
      //   0	74	2	paramIBinder	IBinder
      //   0	74	3	paramArrayOfString	String[]
      //   7	30	4	localDumpComponentInfo	ActivityThread.DumpComponentInfo
      // Exception table:
      //   from	to	target	type
      //   9	44	47	finally
      //   52	63	47	finally
      //   9	44	51	java/io/IOException
    }
    
    public void handleTrustStorageUpdate()
    {
      NetworkSecurityPolicy.getInstance().handleTrustStorageUpdate();
    }
    
    public void notifyCleartextNetwork(byte[] paramArrayOfByte)
    {
      if (StrictMode.vmCleartextNetworkEnabled()) {
        StrictMode.onCleartextNetworkDetected(paramArrayOfByte);
      }
    }
    
    public void processInBackground()
    {
      mH.removeMessages(120);
      mH.sendMessage(mH.obtainMessage(120));
    }
    
    public void profilerControl(boolean paramBoolean, ProfilerInfo paramProfilerInfo, int paramInt)
    {
      ActivityThread.this.sendMessage(127, paramProfilerInfo, paramBoolean, paramInt);
    }
    
    public void requestAssistContextExtras(IBinder paramIBinder1, IBinder paramIBinder2, int paramInt1, int paramInt2, int paramInt3)
    {
      ActivityThread.RequestAssistContextExtras localRequestAssistContextExtras = new ActivityThread.RequestAssistContextExtras();
      activityToken = paramIBinder1;
      requestToken = paramIBinder2;
      requestType = paramInt1;
      sessionId = paramInt2;
      flags = paramInt3;
      sendMessage(143, localRequestAssistContextExtras);
    }
    
    public final void runIsolatedEntryPoint(String paramString, String[] paramArrayOfString)
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramString;
      arg2 = paramArrayOfString;
      sendMessage(158, localSomeArgs);
    }
    
    public void scheduleApplicationInfoChanged(ApplicationInfo paramApplicationInfo)
    {
      sendMessage(156, paramApplicationInfo);
    }
    
    public final void scheduleBindService(IBinder paramIBinder, Intent paramIntent, boolean paramBoolean, int paramInt)
    {
      updateProcessState(paramInt, false);
      ActivityThread.BindServiceData localBindServiceData = new ActivityThread.BindServiceData();
      token = paramIBinder;
      intent = paramIntent;
      rebind = paramBoolean;
      sendMessage(121, localBindServiceData);
    }
    
    public void scheduleCrash(String paramString)
    {
      sendMessage(134, paramString);
    }
    
    public final void scheduleCreateBackupAgent(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt)
    {
      ActivityThread.CreateBackupAgentData localCreateBackupAgentData = new ActivityThread.CreateBackupAgentData();
      appInfo = paramApplicationInfo;
      compatInfo = paramCompatibilityInfo;
      backupMode = paramInt;
      sendMessage(128, localCreateBackupAgentData);
    }
    
    public final void scheduleCreateService(IBinder paramIBinder, ServiceInfo paramServiceInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt)
    {
      updateProcessState(paramInt, false);
      ActivityThread.CreateServiceData localCreateServiceData = new ActivityThread.CreateServiceData();
      token = paramIBinder;
      info = paramServiceInfo;
      compatInfo = paramCompatibilityInfo;
      sendMessage(114, localCreateServiceData);
    }
    
    public final void scheduleDestroyBackupAgent(ApplicationInfo paramApplicationInfo, CompatibilityInfo paramCompatibilityInfo)
    {
      ActivityThread.CreateBackupAgentData localCreateBackupAgentData = new ActivityThread.CreateBackupAgentData();
      appInfo = paramApplicationInfo;
      compatInfo = paramCompatibilityInfo;
      sendMessage(129, localCreateBackupAgentData);
    }
    
    public void scheduleEnterAnimationComplete(IBinder paramIBinder)
    {
      sendMessage(149, paramIBinder);
    }
    
    public final void scheduleExit()
    {
      sendMessage(111, null);
    }
    
    public void scheduleInstallProvider(ProviderInfo paramProviderInfo)
    {
      sendMessage(145, paramProviderInfo);
    }
    
    public void scheduleLocalVoiceInteractionStarted(IBinder paramIBinder, IVoiceInteractor paramIVoiceInteractor)
      throws RemoteException
    {
      SomeArgs localSomeArgs = SomeArgs.obtain();
      arg1 = paramIBinder;
      arg2 = paramIVoiceInteractor;
      sendMessage(154, localSomeArgs);
    }
    
    public void scheduleLowMemory()
    {
      sendMessage(124, null);
    }
    
    public void scheduleOnNewActivityOptions(IBinder paramIBinder, Bundle paramBundle)
    {
      sendMessage(146, new Pair(paramIBinder, ActivityOptions.fromBundle(paramBundle)));
    }
    
    public final void scheduleReceiver(Intent paramIntent, ActivityInfo paramActivityInfo, CompatibilityInfo paramCompatibilityInfo, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean, int paramInt2, int paramInt3)
    {
      updateProcessState(paramInt3, false);
      paramIntent = new ActivityThread.ReceiverData(paramIntent, paramInt1, paramString, paramBundle, paramBoolean, false, mAppThread.asBinder(), paramInt2);
      info = paramActivityInfo;
      compatInfo = paramCompatibilityInfo;
      sendMessage(113, paramIntent);
    }
    
    public void scheduleRegisteredReceiver(IIntentReceiver paramIIntentReceiver, Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3)
      throws RemoteException
    {
      updateProcessState(paramInt3, false);
      paramIIntentReceiver.performReceive(paramIntent, paramInt1, paramString, paramBundle, paramBoolean1, paramBoolean2, paramInt2);
    }
    
    public final void scheduleServiceArgs(IBinder paramIBinder, ParceledListSlice paramParceledListSlice)
    {
      paramParceledListSlice = paramParceledListSlice.getList();
      for (int i = 0; i < paramParceledListSlice.size(); i++)
      {
        ServiceStartArgs localServiceStartArgs = (ServiceStartArgs)paramParceledListSlice.get(i);
        ActivityThread.ServiceArgsData localServiceArgsData = new ActivityThread.ServiceArgsData();
        token = paramIBinder;
        taskRemoved = taskRemoved;
        startId = startId;
        flags = flags;
        args = args;
        sendMessage(115, localServiceArgsData);
      }
    }
    
    public final void scheduleSleeping(IBinder paramIBinder, boolean paramBoolean)
    {
      ActivityThread.this.sendMessage(137, paramIBinder, paramBoolean);
    }
    
    public final void scheduleStopService(IBinder paramIBinder)
    {
      sendMessage(116, paramIBinder);
    }
    
    public final void scheduleSuicide()
    {
      sendMessage(130, null);
    }
    
    public void scheduleTransaction(ClientTransaction paramClientTransaction)
      throws RemoteException
    {
      ActivityThread.this.scheduleTransaction(paramClientTransaction);
    }
    
    public void scheduleTranslucentConversionComplete(IBinder paramIBinder, boolean paramBoolean)
    {
      ActivityThread.this.sendMessage(144, paramIBinder, paramBoolean);
    }
    
    public void scheduleTrimMemory(int paramInt)
    {
      PooledRunnable localPooledRunnable = PooledLambda.obtainRunnable(_..Lambda.ActivityThread.ApplicationThread.tUGFX7CUhzB4Pg5wFd5yeqOnu38.INSTANCE, ActivityThread.this, Integer.valueOf(paramInt));
      Choreographer localChoreographer = Choreographer.getMainThreadInstance();
      if (localChoreographer != null) {
        localChoreographer.postCallback(3, localPooledRunnable, null);
      } else {
        mH.post(localPooledRunnable);
      }
    }
    
    public final void scheduleUnbindService(IBinder paramIBinder, Intent paramIntent)
    {
      ActivityThread.BindServiceData localBindServiceData = new ActivityThread.BindServiceData();
      token = paramIBinder;
      intent = paramIntent;
      sendMessage(122, localBindServiceData);
    }
    
    public void setCoreSettings(Bundle paramBundle)
    {
      sendMessage(138, paramBundle);
    }
    
    public void setHttpProxy(String paramString1, String paramString2, String paramString3, Uri paramUri)
    {
      if (getApplication() != null) {
        localObject = getApplication();
      } else {
        localObject = getSystemContext();
      }
      Object localObject = ConnectivityManager.from((Context)localObject);
      if (((ConnectivityManager)localObject).getBoundNetworkForProcess() != null) {
        Proxy.setHttpProxySystemProperty(((ConnectivityManager)localObject).getDefaultProxy());
      } else {
        Proxy.setHttpProxySystemProperty(paramString1, paramString2, paramString3, paramUri);
      }
    }
    
    public void setNetworkBlockSeq(long paramLong)
    {
      synchronized (mNetworkPolicyLock)
      {
        ActivityThread.access$802(ActivityThread.this, paramLong);
        return;
      }
    }
    
    public void setProcessState(int paramInt)
    {
      updateProcessState(paramInt, true);
    }
    
    public void setSchedulingGroup(int paramInt)
    {
      try
      {
        Process.setProcessGroup(Process.myPid(), paramInt);
      }
      catch (Exception localException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Failed setting process group to ");
        localStringBuilder.append(paramInt);
        Slog.w("ActivityThread", localStringBuilder.toString(), localException);
      }
    }
    
    public void startBinderTracking()
    {
      sendMessage(150, null);
    }
    
    /* Error */
    public void stopBinderTrackingAndDump(ParcelFileDescriptor paramParcelFileDescriptor)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 21	android/app/ActivityThread$ApplicationThread:this$0	Landroid/app/ActivityThread;
      //   4: sipush 151
      //   7: aload_1
      //   8: invokevirtual 532	android/os/ParcelFileDescriptor:dup	()Landroid/os/ParcelFileDescriptor;
      //   11: invokevirtual 434	android/app/ActivityThread:sendMessage	(ILjava/lang/Object;)V
      //   14: goto +11 -> 25
      //   17: astore_2
      //   18: aload_1
      //   19: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   22: aload_2
      //   23: athrow
      //   24: astore_2
      //   25: aload_1
      //   26: invokestatic 567	libcore/io/IoUtils:closeQuietly	(Ljava/lang/AutoCloseable;)V
      //   29: return
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	30	0	this	ApplicationThread
      //   0	30	1	paramParcelFileDescriptor	ParcelFileDescriptor
      //   17	6	2	localObject	Object
      //   24	1	2	localIOException	IOException
      // Exception table:
      //   from	to	target	type
      //   0	14	17	finally
      //   0	14	24	java/io/IOException
    }
    
    public void unstableProviderDied(IBinder paramIBinder)
    {
      sendMessage(142, paramIBinder);
    }
    
    public void updatePackageCompatibilityInfo(String paramString, CompatibilityInfo paramCompatibilityInfo)
    {
      ActivityThread.UpdateCompatibilityData localUpdateCompatibilityData = new ActivityThread.UpdateCompatibilityData();
      pkg = paramString;
      info = paramCompatibilityInfo;
      sendMessage(139, localUpdateCompatibilityData);
    }
    
    public void updateProcessState(int paramInt, boolean paramBoolean)
    {
      try
      {
        if (mLastProcessState != paramInt)
        {
          mLastProcessState = paramInt;
          int i = 1;
          if (paramInt <= 5) {
            i = 0;
          }
          VMRuntime.getRuntime().updateProcessState(i);
        }
        return;
      }
      finally {}
    }
    
    public final void updateTimePrefs(int paramInt)
    {
      Boolean localBoolean;
      if (paramInt == 0) {
        localBoolean = Boolean.FALSE;
      }
      for (;;)
      {
        break;
        if (paramInt == 1) {
          localBoolean = Boolean.TRUE;
        } else {
          localBoolean = null;
        }
      }
      DateFormat.set24HourTimePref(localBoolean);
    }
    
    public void updateTimeZone()
    {
      TimeZone.setDefault(null);
    }
  }
  
  static final class BindServiceData
  {
    Intent intent;
    boolean rebind;
    IBinder token;
    
    BindServiceData() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("BindServiceData{token=");
      localStringBuilder.append(token);
      localStringBuilder.append(" intent=");
      localStringBuilder.append(intent);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  static final class ContextCleanupInfo
  {
    ContextImpl context;
    String what;
    String who;
    
    ContextCleanupInfo() {}
  }
  
  static final class CreateBackupAgentData
  {
    ApplicationInfo appInfo;
    int backupMode;
    CompatibilityInfo compatInfo;
    
    CreateBackupAgentData() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CreateBackupAgentData{appInfo=");
      localStringBuilder.append(appInfo);
      localStringBuilder.append(" backupAgent=");
      localStringBuilder.append(appInfo.backupAgentName);
      localStringBuilder.append(" mode=");
      localStringBuilder.append(backupMode);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  static final class CreateServiceData
  {
    CompatibilityInfo compatInfo;
    ServiceInfo info;
    Intent intent;
    IBinder token;
    
    CreateServiceData() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("CreateServiceData{token=");
      localStringBuilder.append(token);
      localStringBuilder.append(" className=");
      localStringBuilder.append(info.name);
      localStringBuilder.append(" packageName=");
      localStringBuilder.append(info.packageName);
      localStringBuilder.append(" intent=");
      localStringBuilder.append(intent);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  private class DropBoxReporter
    implements DropBox.Reporter
  {
    private DropBoxManager dropBox;
    
    public DropBoxReporter() {}
    
    private void ensureInitialized()
    {
      try
      {
        if (dropBox == null) {
          dropBox = ((DropBoxManager)getSystemContext().getSystemService("dropbox"));
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
    
    public void addData(String paramString, byte[] paramArrayOfByte, int paramInt)
    {
      ensureInitialized();
      dropBox.addData(paramString, paramArrayOfByte, paramInt);
    }
    
    public void addText(String paramString1, String paramString2)
    {
      ensureInitialized();
      dropBox.addText(paramString1, paramString2);
    }
  }
  
  static final class DumpComponentInfo
  {
    String[] args;
    ParcelFileDescriptor fd;
    String prefix;
    IBinder token;
    
    DumpComponentInfo() {}
  }
  
  static final class DumpHeapData
  {
    ParcelFileDescriptor fd;
    public boolean mallocInfo;
    public boolean managed;
    String path;
    public boolean runGc;
    
    DumpHeapData() {}
  }
  
  private static class EventLoggingReporter
    implements EventLogger.Reporter
  {
    private EventLoggingReporter() {}
    
    public void report(int paramInt, Object... paramVarArgs)
    {
      EventLog.writeEvent(paramInt, paramVarArgs);
    }
  }
  
  final class GcIdler
    implements MessageQueue.IdleHandler
  {
    GcIdler() {}
    
    public final boolean queueIdle()
    {
      doGcIfNeeded();
      return false;
    }
  }
  
  class H
    extends Handler
  {
    public static final int APPLICATION_INFO_CHANGED = 156;
    public static final int ATTACH_AGENT = 155;
    public static final int BIND_APPLICATION = 110;
    public static final int BIND_SERVICE = 121;
    public static final int CLEAN_UP_CONTEXT = 119;
    public static final int CONFIGURATION_CHANGED = 118;
    public static final int CREATE_BACKUP_AGENT = 128;
    public static final int CREATE_SERVICE = 114;
    public static final int DESTROY_BACKUP_AGENT = 129;
    public static final int DISPATCH_PACKAGE_BROADCAST = 133;
    public static final int DUMP_ACTIVITY = 136;
    public static final int DUMP_HEAP = 135;
    public static final int DUMP_PROVIDER = 141;
    public static final int DUMP_SERVICE = 123;
    public static final int ENABLE_JIT = 132;
    public static final int ENTER_ANIMATION_COMPLETE = 149;
    public static final int EXECUTE_TRANSACTION = 159;
    public static final int EXIT_APPLICATION = 111;
    public static final int GC_WHEN_IDLE = 120;
    public static final int INSTALL_PROVIDER = 145;
    public static final int LOCAL_VOICE_INTERACTION_STARTED = 154;
    public static final int LOW_MEMORY = 124;
    public static final int ON_NEW_ACTIVITY_OPTIONS = 146;
    public static final int PROFILER_CONTROL = 127;
    public static final int RECEIVER = 113;
    public static final int RELAUNCH_ACTIVITY = 160;
    public static final int REMOVE_PROVIDER = 131;
    public static final int REQUEST_ASSIST_CONTEXT_EXTRAS = 143;
    public static final int RUN_ISOLATED_ENTRY_POINT = 158;
    public static final int SCHEDULE_CRASH = 134;
    public static final int SERVICE_ARGS = 115;
    public static final int SET_CORE_SETTINGS = 138;
    public static final int SLEEPING = 137;
    public static final int START_BINDER_TRACKING = 150;
    public static final int STOP_BINDER_TRACKING_AND_DUMP = 151;
    public static final int STOP_SERVICE = 116;
    public static final int SUICIDE = 130;
    public static final int TRANSLUCENT_CONVERSION_COMPLETE = 144;
    public static final int UNBIND_SERVICE = 122;
    public static final int UNSTABLE_PROVIDER_DIED = 142;
    public static final int UPDATE_PACKAGE_COMPATIBILITY_INFO = 139;
    
    H() {}
    
    String codeToString(int paramInt)
    {
      return Integer.toString(paramInt);
    }
    
    public void handleMessage(Message paramMessage)
    {
      int i = what;
      boolean bool1 = true;
      boolean bool2 = true;
      boolean bool3 = true;
      Object localObject1;
      Object localObject2;
      switch (i)
      {
      case 112: 
      case 117: 
      case 125: 
      case 126: 
      case 140: 
      case 147: 
      case 148: 
      case 152: 
      case 153: 
      case 157: 
      default: 
        break;
      case 160: 
        ActivityThread.this.handleRelaunchActivityLocally((IBinder)obj);
        break;
      case 159: 
        localObject1 = (ClientTransaction)obj;
        mTransactionExecutor.execute((ClientTransaction)localObject1);
        if (ActivityThread.isSystem()) {
          ((ClientTransaction)localObject1).recycle();
        }
        break;
      case 158: 
        ActivityThread.this.handleRunIsolatedEntryPoint((String)obj).arg1, (String[])obj).arg2);
        break;
      case 156: 
        mUpdatingSystemConfig = true;
      case 155: 
        try
        {
          handleApplicationInfoChanged((ApplicationInfo)obj);
          mUpdatingSystemConfig = false;
        }
        finally
        {
          mUpdatingSystemConfig = false;
        }
        localObject2 = (String)obj;
        if (localObject1 != null) {
          localObject1 = mLoadedApk;
        } else {
          localObject1 = null;
        }
        ActivityThread.handleAttachAgent((String)localObject2, (LoadedApk)localObject1);
        break;
      case 154: 
        ActivityThread.this.handleLocalVoiceInteractionStarted((IBinder)obj).arg1, (IVoiceInteractor)obj).arg2);
        break;
      case 151: 
        ActivityThread.this.handleStopBinderTrackingAndDump((ParcelFileDescriptor)obj);
        break;
      case 150: 
        ActivityThread.this.handleStartBinderTracking();
        break;
      case 149: 
        ActivityThread.this.handleEnterAnimationComplete((IBinder)obj);
        break;
      case 146: 
        localObject1 = (Pair)obj;
        onNewActivityOptions((IBinder)first, (ActivityOptions)second);
        break;
      case 145: 
        handleInstallProvider((ProviderInfo)obj);
        break;
      case 144: 
        localObject2 = ActivityThread.this;
        localObject1 = (IBinder)obj;
        if (arg1 != 1) {
          bool3 = false;
        }
        ((ActivityThread)localObject2).handleTranslucentConversionComplete((IBinder)localObject1, bool3);
        break;
      case 143: 
        handleRequestAssistContextExtras((ActivityThread.RequestAssistContextExtras)obj);
        break;
      case 142: 
        handleUnstableProviderDied((IBinder)obj, false);
        break;
      case 141: 
        ActivityThread.this.handleDumpProvider((ActivityThread.DumpComponentInfo)obj);
        break;
      case 139: 
        ActivityThread.this.handleUpdatePackageCompatibilityInfo((ActivityThread.UpdateCompatibilityData)obj);
        break;
      case 138: 
        Trace.traceBegin(64L, "setCoreSettings");
        ActivityThread.this.handleSetCoreSettings((Bundle)obj);
        Trace.traceEnd(64L);
        break;
      case 137: 
        Trace.traceBegin(64L, "sleeping");
        localObject2 = ActivityThread.this;
        localObject1 = (IBinder)obj;
        if (arg1 != 0) {
          bool3 = bool1;
        } else {
          bool3 = false;
        }
        ((ActivityThread)localObject2).handleSleeping((IBinder)localObject1, bool3);
        Trace.traceEnd(64L);
        break;
      case 136: 
        ActivityThread.this.handleDumpActivity((ActivityThread.DumpComponentInfo)obj);
        break;
      case 135: 
        ActivityThread.handleDumpHeap((ActivityThread.DumpHeapData)obj);
        break;
      case 134: 
        throw new RemoteServiceException((String)obj);
      case 133: 
        Trace.traceBegin(64L, "broadcastPackage");
        handleDispatchPackageBroadcast(arg1, (String[])obj);
        Trace.traceEnd(64L);
        break;
      case 132: 
        ensureJitEnabled();
        break;
      case 131: 
        Trace.traceBegin(64L, "providerRemove");
        completeRemoveProvider((ActivityThread.ProviderRefCount)obj);
        Trace.traceEnd(64L);
        break;
      case 130: 
        Process.killProcess(Process.myPid());
        break;
      case 129: 
        Trace.traceBegin(64L, "backupDestroyAgent");
        ActivityThread.this.handleDestroyBackupAgent((ActivityThread.CreateBackupAgentData)obj);
        Trace.traceEnd(64L);
        break;
      case 128: 
        Trace.traceBegin(64L, "backupCreateAgent");
        ActivityThread.this.handleCreateBackupAgent((ActivityThread.CreateBackupAgentData)obj);
        Trace.traceEnd(64L);
        break;
      case 127: 
        localObject1 = ActivityThread.this;
        if (arg1 != 0) {
          bool3 = bool2;
        } else {
          bool3 = false;
        }
        ((ActivityThread)localObject1).handleProfilerControl(bool3, (ProfilerInfo)obj, arg2);
        break;
      case 124: 
        Trace.traceBegin(64L, "lowMemory");
        handleLowMemory();
        Trace.traceEnd(64L);
        break;
      case 123: 
        ActivityThread.this.handleDumpService((ActivityThread.DumpComponentInfo)obj);
        break;
      case 122: 
        Trace.traceBegin(64L, "serviceUnbind");
        ActivityThread.this.handleUnbindService((ActivityThread.BindServiceData)obj);
        Trace.traceEnd(64L);
        break;
      case 121: 
        Trace.traceBegin(64L, "serviceBind");
        ActivityThread.this.handleBindService((ActivityThread.BindServiceData)obj);
        Trace.traceEnd(64L);
        break;
      case 120: 
        scheduleGcIdler();
        break;
      case 119: 
        localObject1 = (ActivityThread.ContextCleanupInfo)obj;
        context.performFinalCleanup(who, what);
        break;
      case 118: 
        handleConfigurationChanged((Configuration)obj);
        break;
      case 116: 
        Trace.traceBegin(64L, "serviceStop");
        ActivityThread.this.handleStopService((IBinder)obj);
        Trace.traceEnd(64L);
        break;
      case 115: 
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("serviceStart: ");
        ((StringBuilder)localObject1).append(String.valueOf(obj));
        Trace.traceBegin(64L, ((StringBuilder)localObject1).toString());
        ActivityThread.this.handleServiceArgs((ActivityThread.ServiceArgsData)obj);
        Trace.traceEnd(64L);
        break;
      case 114: 
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append("serviceCreate: ");
        ((StringBuilder)localObject1).append(String.valueOf(obj));
        Trace.traceBegin(64L, ((StringBuilder)localObject1).toString());
        ActivityThread.this.handleCreateService((ActivityThread.CreateServiceData)obj);
        Trace.traceEnd(64L);
        break;
      case 113: 
        Trace.traceBegin(64L, "broadcastReceiveComp");
        ActivityThread.this.handleReceiver((ActivityThread.ReceiverData)obj);
        Trace.traceEnd(64L);
        break;
      case 111: 
        if (mInitialApplication != null) {
          mInitialApplication.onTerminate();
        }
        Looper.myLooper().quit();
        break;
      case 110: 
        Trace.traceBegin(64L, "bindApplication");
        localObject1 = (ActivityThread.AppBindData)obj;
        ActivityThread.this.handleBindApplication((ActivityThread.AppBindData)localObject1);
        Trace.traceEnd(64L);
      }
      paramMessage = obj;
      if ((paramMessage instanceof SomeArgs)) {
        ((SomeArgs)paramMessage).recycle();
      }
    }
  }
  
  private class Idler
    implements MessageQueue.IdleHandler
  {
    private Idler() {}
    
    public final boolean queueIdle()
    {
      ActivityThread.ActivityClientRecord localActivityClientRecord1 = mNewActivities;
      boolean bool1 = false;
      boolean bool2 = bool1;
      if (mBoundApplication != null)
      {
        bool2 = bool1;
        if (mProfiler.profileFd != null)
        {
          bool2 = bool1;
          if (mProfiler.autoStopProfiler) {
            bool2 = true;
          }
        }
      }
      if (localActivityClientRecord1 != null)
      {
        mNewActivities = null;
        IActivityManager localIActivityManager = ActivityManager.getService();
        ActivityThread.ActivityClientRecord localActivityClientRecord2;
        do
        {
          if ((activity != null) && (!activity.mFinished)) {
            try
            {
              localIActivityManager.activityIdle(token, createdConfig, bool2);
              createdConfig = null;
            }
            catch (RemoteException localRemoteException)
            {
              throw localRemoteException.rethrowFromSystemServer();
            }
          }
          localActivityClientRecord2 = nextIdle;
          nextIdle = null;
          Object localObject = localActivityClientRecord2;
        } while (localActivityClientRecord2 != null);
      }
      if (bool2) {
        mProfiler.stopProfiling();
      }
      ensureJitEnabled();
      return false;
    }
  }
  
  static final class Profiler
  {
    boolean autoStopProfiler;
    boolean handlingProfiling;
    ParcelFileDescriptor profileFd;
    String profileFile;
    boolean profiling;
    int samplingInterval;
    boolean streamingOutput;
    
    Profiler() {}
    
    public void setProfiler(ProfilerInfo paramProfilerInfo)
    {
      ParcelFileDescriptor localParcelFileDescriptor = profileFd;
      if (profiling)
      {
        if (localParcelFileDescriptor != null) {
          try
          {
            localParcelFileDescriptor.close();
          }
          catch (IOException paramProfilerInfo) {}
        }
        return;
      }
      if (profileFd != null) {
        try
        {
          profileFd.close();
        }
        catch (IOException localIOException) {}
      }
      profileFile = profileFile;
      profileFd = localParcelFileDescriptor;
      samplingInterval = samplingInterval;
      autoStopProfiler = autoStopProfiler;
      streamingOutput = streamingOutput;
    }
    
    public void startProfiling()
    {
      if ((profileFd != null) && (!profiling))
      {
        try
        {
          int i = SystemProperties.getInt("debug.traceview-buffer-size-mb", 8);
          localObject = profileFile;
          FileDescriptor localFileDescriptor = profileFd.getFileDescriptor();
          boolean bool;
          if (samplingInterval != 0) {
            bool = true;
          } else {
            bool = false;
          }
          VMDebug.startMethodTracing((String)localObject, localFileDescriptor, i * 1024 * 1024, 0, bool, samplingInterval, streamingOutput);
          profiling = true;
        }
        catch (RuntimeException localRuntimeException)
        {
          Object localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Profiling failed on path ");
          ((StringBuilder)localObject).append(profileFile);
          Slog.w("ActivityThread", ((StringBuilder)localObject).toString(), localRuntimeException);
          try
          {
            profileFd.close();
            profileFd = null;
          }
          catch (IOException localIOException)
          {
            Slog.w("ActivityThread", "Failure closing profile fd", localIOException);
          }
        }
        return;
      }
    }
    
    public void stopProfiling()
    {
      if (profiling)
      {
        profiling = false;
        Debug.stopMethodTracing();
        if (profileFd != null) {
          try
          {
            profileFd.close();
          }
          catch (IOException localIOException) {}
        }
        profileFd = null;
        profileFile = null;
      }
    }
  }
  
  final class ProviderClientRecord
  {
    final ContentProviderHolder mHolder;
    final ContentProvider mLocalProvider;
    final String[] mNames;
    final IContentProvider mProvider;
    
    ProviderClientRecord(String[] paramArrayOfString, IContentProvider paramIContentProvider, ContentProvider paramContentProvider, ContentProviderHolder paramContentProviderHolder)
    {
      mNames = paramArrayOfString;
      mProvider = paramIContentProvider;
      mLocalProvider = paramContentProvider;
      mHolder = paramContentProviderHolder;
    }
  }
  
  private static final class ProviderKey
  {
    final String authority;
    final int userId;
    
    public ProviderKey(String paramString, int paramInt)
    {
      authority = paramString;
      userId = paramInt;
    }
    
    public boolean equals(Object paramObject)
    {
      boolean bool1 = paramObject instanceof ProviderKey;
      boolean bool2 = false;
      if (bool1)
      {
        paramObject = (ProviderKey)paramObject;
        bool1 = bool2;
        if (Objects.equals(authority, authority))
        {
          bool1 = bool2;
          if (userId == userId) {
            bool1 = true;
          }
        }
        return bool1;
      }
      return false;
    }
    
    public int hashCode()
    {
      int i;
      if (authority != null) {
        i = authority.hashCode();
      } else {
        i = 0;
      }
      return i ^ userId;
    }
  }
  
  private static final class ProviderRefCount
  {
    public final ActivityThread.ProviderClientRecord client;
    public final ContentProviderHolder holder;
    public boolean removePending;
    public int stableCount;
    public int unstableCount;
    
    ProviderRefCount(ContentProviderHolder paramContentProviderHolder, ActivityThread.ProviderClientRecord paramProviderClientRecord, int paramInt1, int paramInt2)
    {
      holder = paramContentProviderHolder;
      client = paramProviderClientRecord;
      stableCount = paramInt1;
      unstableCount = paramInt2;
    }
  }
  
  static final class ReceiverData
    extends BroadcastReceiver.PendingResult
  {
    CompatibilityInfo compatInfo;
    ActivityInfo info;
    Intent intent;
    
    public ReceiverData(Intent paramIntent, int paramInt1, String paramString, Bundle paramBundle, boolean paramBoolean1, boolean paramBoolean2, IBinder paramIBinder, int paramInt2)
    {
      super(paramString, paramBundle, 0, paramBoolean1, paramBoolean2, paramIBinder, paramInt2, paramIntent.getFlags());
      intent = paramIntent;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ReceiverData{intent=");
      localStringBuilder.append(intent);
      localStringBuilder.append(" packageName=");
      localStringBuilder.append(info.packageName);
      localStringBuilder.append(" resultCode=");
      localStringBuilder.append(getResultCode());
      localStringBuilder.append(" resultData=");
      localStringBuilder.append(getResultData());
      localStringBuilder.append(" resultExtras=");
      localStringBuilder.append(getResultExtras(false));
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  static final class RequestAssistContextExtras
  {
    IBinder activityToken;
    int flags;
    IBinder requestToken;
    int requestType;
    int sessionId;
    
    RequestAssistContextExtras() {}
  }
  
  static final class ServiceArgsData
  {
    Intent args;
    int flags;
    int startId;
    boolean taskRemoved;
    IBinder token;
    
    ServiceArgsData() {}
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("ServiceArgsData{token=");
      localStringBuilder.append(token);
      localStringBuilder.append(" startId=");
      localStringBuilder.append(startId);
      localStringBuilder.append(" args=");
      localStringBuilder.append(args);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
  
  static final class UpdateCompatibilityData
  {
    CompatibilityInfo info;
    String pkg;
    
    UpdateCompatibilityData() {}
  }
}
