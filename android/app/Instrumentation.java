package android.app;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.os.PerformanceCollector;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.TestLooperManager;
import android.os.UserHandle;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.util.SeempLog;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.Window;
import com.android.internal.content.ReferrerIntent;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class Instrumentation
{
  public static final String REPORT_KEY_IDENTIFIER = "id";
  public static final String REPORT_KEY_STREAMRESULT = "stream";
  private static final String TAG = "Instrumentation";
  private List<ActivityMonitor> mActivityMonitors;
  private Context mAppContext;
  private boolean mAutomaticPerformanceSnapshots = false;
  private ComponentName mComponent;
  private Context mInstrContext;
  private MessageQueue mMessageQueue = null;
  private Bundle mPerfMetrics = new Bundle();
  private PerformanceCollector mPerformanceCollector;
  private Thread mRunner;
  private final Object mSync = new Object();
  private ActivityThread mThread = null;
  private UiAutomation mUiAutomation;
  private IUiAutomationConnection mUiAutomationConnection;
  private List<ActivityWaiter> mWaitingActivities;
  private IInstrumentationWatcher mWatcher;
  
  public Instrumentation() {}
  
  private void addValue(String paramString, int paramInt, Bundle paramBundle)
  {
    if (paramBundle.containsKey(paramString))
    {
      paramString = paramBundle.getIntegerArrayList(paramString);
      if (paramString != null) {
        paramString.add(Integer.valueOf(paramInt));
      }
    }
    else
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add(Integer.valueOf(paramInt));
      paramBundle.putIntegerArrayList(paramString, localArrayList);
    }
  }
  
  private void checkInstrumenting(String paramString)
  {
    if (mInstrContext != null) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" cannot be called outside of instrumented processes");
    throw new RuntimeException(localStringBuilder.toString());
  }
  
  public static void checkStartActivityResult(int paramInt, Object paramObject)
  {
    if (!ActivityManager.isStartResultFatalError(paramInt)) {
      return;
    }
    StringBuilder localStringBuilder;
    switch (paramInt)
    {
    case -98: 
    default: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Unknown error code ");
      localStringBuilder.append(paramInt);
      localStringBuilder.append(" when starting ");
      localStringBuilder.append(paramObject);
      throw new AndroidRuntimeException(localStringBuilder.toString());
    case -89: 
      throw new IllegalStateException("Session calling startAssistantActivity does not match active session");
    case -90: 
      throw new IllegalStateException("Cannot start assistant activity on a hidden session");
    case -92: 
    case -91: 
      if (((paramObject instanceof Intent)) && (((Intent)paramObject).getComponent() != null))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unable to find explicit activity class ");
        localStringBuilder.append(((Intent)paramObject).getComponent().toShortString());
        localStringBuilder.append("; have you declared this activity in your AndroidManifest.xml?");
        throw new ActivityNotFoundException(localStringBuilder.toString());
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("No Activity found to handle ");
      localStringBuilder.append(paramObject);
      throw new ActivityNotFoundException(localStringBuilder.toString());
    case -93: 
      throw new AndroidRuntimeException("FORWARD_RESULT_FLAG used while also requesting a result");
    case -94: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Not allowed to start activity ");
      localStringBuilder.append(paramObject);
      throw new SecurityException(localStringBuilder.toString());
    case -95: 
      throw new IllegalArgumentException("PendingIntent is not an activity");
    case -96: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Activity could not be started for ");
      localStringBuilder.append(paramObject);
      throw new AndroidRuntimeException(localStringBuilder.toString());
    case -97: 
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Starting under voice control not allowed for: ");
      localStringBuilder.append(paramObject);
      throw new SecurityException(localStringBuilder.toString());
    case -99: 
      throw new IllegalStateException("Session calling startVoiceActivity does not match active session");
    }
    throw new IllegalStateException("Cannot start voice activity on a hidden session");
  }
  
  private AppComponentFactory getFactory(String paramString)
  {
    if (paramString == null)
    {
      Log.e("Instrumentation", "No pkg specified, disabling AppComponentFactory");
      return AppComponentFactory.DEFAULT;
    }
    if (mThread == null)
    {
      Log.e("Instrumentation", "Uninitialized ActivityThread, likely app-created Instrumentation, disabling AppComponentFactory", new Throwable());
      return AppComponentFactory.DEFAULT;
    }
    LoadedApk localLoadedApk = mThread.peekPackageInfo(paramString, true);
    paramString = localLoadedApk;
    if (localLoadedApk == null) {
      paramString = mThread.getSystemContext().mPackageInfo;
    }
    return paramString.getAppFactory();
  }
  
  public static Application newApplication(Class<?> paramClass, Context paramContext)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException
  {
    paramClass = (Application)paramClass.newInstance();
    paramClass.attach(paramContext);
    return paramClass;
  }
  
  private void postPerformCreate(Activity paramActivity)
  {
    if (mActivityMonitors != null) {
      synchronized (mSync)
      {
        int i = mActivityMonitors.size();
        for (int j = 0; j < i; j++) {
          ((ActivityMonitor)mActivityMonitors.get(j)).match(paramActivity, paramActivity, paramActivity.getIntent());
        }
      }
    }
  }
  
  private void prePerformCreate(Activity paramActivity)
  {
    if (mWaitingActivities != null) {
      synchronized (mSync)
      {
        int i = mWaitingActivities.size();
        for (int j = 0; j < i; j++)
        {
          ActivityWaiter localActivityWaiter = (ActivityWaiter)mWaitingActivities.get(j);
          if (intent.filterEquals(paramActivity.getIntent()))
          {
            activity = paramActivity;
            MessageQueue localMessageQueue = mMessageQueue;
            ActivityGoing localActivityGoing = new android/app/Instrumentation$ActivityGoing;
            localActivityGoing.<init>(this, localActivityWaiter);
            localMessageQueue.addIdleHandler(localActivityGoing);
          }
        }
      }
    }
  }
  
  private final void validateNotAppThread()
  {
    if (Looper.myLooper() != Looper.getMainLooper()) {
      return;
    }
    throw new RuntimeException("This method can not be called from the main application thread");
  }
  
  public TestLooperManager acquireLooperManager(Looper paramLooper)
  {
    checkInstrumenting("acquireLooperManager");
    return new TestLooperManager(paramLooper);
  }
  
  public ActivityMonitor addMonitor(IntentFilter paramIntentFilter, ActivityResult paramActivityResult, boolean paramBoolean)
  {
    paramIntentFilter = new ActivityMonitor(paramIntentFilter, paramActivityResult, paramBoolean);
    addMonitor(paramIntentFilter);
    return paramIntentFilter;
  }
  
  public ActivityMonitor addMonitor(String paramString, ActivityResult paramActivityResult, boolean paramBoolean)
  {
    paramString = new ActivityMonitor(paramString, paramActivityResult, paramBoolean);
    addMonitor(paramString);
    return paramString;
  }
  
  public void addMonitor(ActivityMonitor paramActivityMonitor)
  {
    synchronized (mSync)
    {
      if (mActivityMonitors == null)
      {
        ArrayList localArrayList = new java/util/ArrayList;
        localArrayList.<init>();
        mActivityMonitors = localArrayList;
      }
      mActivityMonitors.add(paramActivityMonitor);
      return;
    }
  }
  
  public void addResults(Bundle paramBundle)
  {
    IActivityManager localIActivityManager = ActivityManager.getService();
    try
    {
      localIActivityManager.addInstrumentationResults(mThread.getApplicationThread(), paramBundle);
      return;
    }
    catch (RemoteException paramBundle)
    {
      throw paramBundle.rethrowFromSystemServer();
    }
  }
  
  final void basicInit(ActivityThread paramActivityThread)
  {
    mThread = paramActivityThread;
  }
  
  public void callActivityOnCreate(Activity paramActivity, Bundle paramBundle)
  {
    prePerformCreate(paramActivity);
    paramActivity.performCreate(paramBundle);
    postPerformCreate(paramActivity);
  }
  
  public void callActivityOnCreate(Activity paramActivity, Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    prePerformCreate(paramActivity);
    paramActivity.performCreate(paramBundle, paramPersistableBundle);
    postPerformCreate(paramActivity);
  }
  
  public void callActivityOnDestroy(Activity paramActivity)
  {
    paramActivity.performDestroy();
  }
  
  public void callActivityOnNewIntent(Activity paramActivity, Intent paramIntent)
  {
    paramActivity.performNewIntent(paramIntent);
  }
  
  public void callActivityOnNewIntent(Activity paramActivity, ReferrerIntent paramReferrerIntent)
  {
    String str = mReferrer;
    if (paramReferrerIntent != null) {
      try
      {
        mReferrer = mReferrer;
      }
      finally
      {
        break label59;
      }
    }
    if (paramReferrerIntent != null)
    {
      Intent localIntent = new android/content/Intent;
      localIntent.<init>(paramReferrerIntent);
      paramReferrerIntent = localIntent;
    }
    else
    {
      paramReferrerIntent = null;
    }
    callActivityOnNewIntent(paramActivity, paramReferrerIntent);
    mReferrer = str;
    return;
    label59:
    mReferrer = str;
    throw paramReferrerIntent;
  }
  
  public void callActivityOnPause(Activity paramActivity)
  {
    paramActivity.performPause();
  }
  
  public void callActivityOnPostCreate(Activity paramActivity, Bundle paramBundle)
  {
    paramActivity.onPostCreate(paramBundle);
  }
  
  public void callActivityOnPostCreate(Activity paramActivity, Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    paramActivity.onPostCreate(paramBundle, paramPersistableBundle);
  }
  
  public void callActivityOnRestart(Activity paramActivity)
  {
    paramActivity.onRestart();
  }
  
  public void callActivityOnRestoreInstanceState(Activity paramActivity, Bundle paramBundle)
  {
    paramActivity.performRestoreInstanceState(paramBundle);
  }
  
  public void callActivityOnRestoreInstanceState(Activity paramActivity, Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    paramActivity.performRestoreInstanceState(paramBundle, paramPersistableBundle);
  }
  
  public void callActivityOnResume(Activity paramActivity)
  {
    mResumed = true;
    paramActivity.onResume();
    if (mActivityMonitors != null) {
      synchronized (mSync)
      {
        int i = mActivityMonitors.size();
        for (int j = 0; j < i; j++) {
          ((ActivityMonitor)mActivityMonitors.get(j)).match(paramActivity, paramActivity, paramActivity.getIntent());
        }
      }
    }
  }
  
  public void callActivityOnSaveInstanceState(Activity paramActivity, Bundle paramBundle)
  {
    paramActivity.performSaveInstanceState(paramBundle);
  }
  
  public void callActivityOnSaveInstanceState(Activity paramActivity, Bundle paramBundle, PersistableBundle paramPersistableBundle)
  {
    paramActivity.performSaveInstanceState(paramBundle, paramPersistableBundle);
  }
  
  public void callActivityOnStart(Activity paramActivity)
  {
    paramActivity.onStart();
  }
  
  public void callActivityOnStop(Activity paramActivity)
  {
    paramActivity.onStop();
  }
  
  public void callActivityOnUserLeaving(Activity paramActivity)
  {
    paramActivity.performUserLeaving();
  }
  
  public void callApplicationOnCreate(Application paramApplication)
  {
    paramApplication.onCreate();
  }
  
  public boolean checkMonitorHit(ActivityMonitor paramActivityMonitor, int paramInt)
  {
    waitForIdleSync();
    synchronized (mSync)
    {
      if (paramActivityMonitor.getHits() < paramInt) {
        return false;
      }
      mActivityMonitors.remove(paramActivityMonitor);
      return true;
    }
  }
  
  public void endPerformanceSnapshot()
  {
    if (!isProfiling()) {
      mPerfMetrics = mPerformanceCollector.endSnapshot();
    }
  }
  
  public void execStartActivities(Context paramContext, IBinder paramIBinder1, IBinder paramIBinder2, Activity paramActivity, Intent[] paramArrayOfIntent, Bundle paramBundle)
  {
    execStartActivitiesAsUser(paramContext, paramIBinder1, paramIBinder2, paramActivity, paramArrayOfIntent, paramBundle, paramContext.getUserId());
  }
  
  public int execStartActivitiesAsUser(Context paramContext, IBinder paramIBinder1, IBinder paramIBinder2, Activity paramActivity, Intent[] paramArrayOfIntent, Bundle paramBundle, int paramInt)
  {
    SeempLog.record_str(378, paramArrayOfIntent.toString());
    paramActivity = (IApplicationThread)paramIBinder1;
    int j;
    if (mActivityMonitors != null) {
      synchronized (mSync)
      {
        int i = mActivityMonitors.size();
        for (j = 0; j < i; j++)
        {
          ActivityMonitor localActivityMonitor = (ActivityMonitor)mActivityMonitors.get(j);
          paramIBinder1 = null;
          if (localActivityMonitor.ignoreMatchingSpecificIntents()) {
            paramIBinder1 = localActivityMonitor.onStartActivity(paramArrayOfIntent[0]);
          }
          if (paramIBinder1 != null)
          {
            mHits += 1;
            return -96;
          }
          if (localActivityMonitor.match(paramContext, null, paramArrayOfIntent[0]))
          {
            mHits += 1;
            if (!localActivityMonitor.isBlocking()) {
              break;
            }
            return -96;
          }
        }
      }
    }
    try
    {
      paramIBinder1 = new String[paramArrayOfIntent.length];
      for (j = 0; j < paramArrayOfIntent.length; j++)
      {
        paramArrayOfIntent[j].migrateExtraStreamToClipData();
        paramArrayOfIntent[j].prepareToLeaveProcess(paramContext);
        paramIBinder1[j] = paramArrayOfIntent[j].resolveTypeIfNeeded(paramContext.getContentResolver());
      }
      paramInt = ActivityManager.getService().startActivities(paramActivity, paramContext.getBasePackageName(), paramArrayOfIntent, paramIBinder1, paramIBinder2, paramBundle, paramInt);
      checkStartActivityResult(paramInt, paramArrayOfIntent[0]);
      return paramInt;
    }
    catch (RemoteException paramContext)
    {
      throw new RuntimeException("Failure from system", paramContext);
    }
  }
  
  public ActivityResult execStartActivity(Context paramContext, IBinder paramIBinder1, IBinder paramIBinder2, Activity paramActivity, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    SeempLog.record_str(377, paramIntent.toString());
    IApplicationThread localIApplicationThread = (IApplicationThread)paramIBinder1;
    String str = null;
    if (paramActivity != null) {
      paramIBinder1 = paramActivity.onProvideReferrer();
    } else {
      paramIBinder1 = null;
    }
    if (paramIBinder1 != null) {
      paramIntent.putExtra("android.intent.extra.REFERRER", paramIBinder1);
    }
    if (mActivityMonitors != null) {
      synchronized (mSync)
      {
        int i = mActivityMonitors.size();
        for (int j = 0; j < i; j++)
        {
          ActivityMonitor localActivityMonitor = (ActivityMonitor)mActivityMonitors.get(j);
          paramIBinder1 = null;
          if (localActivityMonitor.ignoreMatchingSpecificIntents()) {
            paramIBinder1 = localActivityMonitor.onStartActivity(paramIntent);
          }
          if (paramIBinder1 != null)
          {
            mHits += 1;
            return paramIBinder1;
          }
          if (localActivityMonitor.match(paramContext, null, paramIntent))
          {
            mHits += 1;
            if (!localActivityMonitor.isBlocking()) {
              break;
            }
            paramContext = str;
            if (paramInt >= 0) {
              paramContext = localActivityMonitor.getResult();
            }
            return paramContext;
          }
        }
      }
    }
    try
    {
      paramIntent.migrateExtraStreamToClipData();
      paramIntent.prepareToLeaveProcess(paramContext);
      paramIBinder1 = ActivityManager.getService();
      str = paramContext.getBasePackageName();
      ??? = paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver());
      if (paramActivity != null) {
        try
        {
          paramContext = mEmbeddedID;
        }
        catch (RemoteException paramContext)
        {
          break label299;
        }
      } else {
        paramContext = null;
      }
      try
      {
        checkStartActivityResult(paramIBinder1.startActivity(localIApplicationThread, str, paramIntent, (String)???, paramIBinder2, paramContext, paramInt, 0, null, paramBundle), paramIntent);
        return null;
      }
      catch (RemoteException paramContext) {}
      throw new RuntimeException("Failure from system", paramContext);
    }
    catch (RemoteException paramContext) {}
  }
  
  public ActivityResult execStartActivity(Context paramContext, IBinder paramIBinder1, IBinder paramIBinder2, String paramString, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    SeempLog.record_str(377, paramIntent.toString());
    IApplicationThread localIApplicationThread = (IApplicationThread)paramIBinder1;
    paramIBinder1 = mActivityMonitors;
    Object localObject1 = null;
    if (paramIBinder1 != null) {
      synchronized (mSync)
      {
        int i = mActivityMonitors.size();
        for (int j = 0; j < i; j++)
        {
          ActivityMonitor localActivityMonitor = (ActivityMonitor)mActivityMonitors.get(j);
          paramIBinder1 = null;
          if (localActivityMonitor.ignoreMatchingSpecificIntents()) {
            paramIBinder1 = localActivityMonitor.onStartActivity(paramIntent);
          }
          if (paramIBinder1 != null)
          {
            mHits += 1;
            return paramIBinder1;
          }
          if (localActivityMonitor.match(paramContext, null, paramIntent))
          {
            mHits += 1;
            if (!localActivityMonitor.isBlocking()) {
              break;
            }
            paramContext = localObject1;
            if (paramInt >= 0) {
              paramContext = localActivityMonitor.getResult();
            }
            return paramContext;
          }
        }
      }
    }
    try
    {
      paramIntent.migrateExtraStreamToClipData();
      paramIntent.prepareToLeaveProcess(paramContext);
      checkStartActivityResult(ActivityManager.getService().startActivity(localIApplicationThread, paramContext.getBasePackageName(), paramIntent, paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver()), paramIBinder2, paramString, paramInt, 0, null, paramBundle), paramIntent);
      return null;
    }
    catch (RemoteException paramContext)
    {
      throw new RuntimeException("Failure from system", paramContext);
    }
  }
  
  public ActivityResult execStartActivity(Context paramContext, IBinder paramIBinder1, IBinder paramIBinder2, String paramString, Intent paramIntent, int paramInt, Bundle paramBundle, UserHandle paramUserHandle)
  {
    SeempLog.record_str(377, paramIntent.toString());
    IApplicationThread localIApplicationThread = (IApplicationThread)paramIBinder1;
    paramIBinder1 = mActivityMonitors;
    Object localObject1 = null;
    if (paramIBinder1 != null) {
      synchronized (mSync)
      {
        int i = mActivityMonitors.size();
        for (int j = 0; j < i; j++)
        {
          ActivityMonitor localActivityMonitor = (ActivityMonitor)mActivityMonitors.get(j);
          paramIBinder1 = null;
          if (localActivityMonitor.ignoreMatchingSpecificIntents()) {
            paramIBinder1 = localActivityMonitor.onStartActivity(paramIntent);
          }
          if (paramIBinder1 != null)
          {
            mHits += 1;
            return paramIBinder1;
          }
          if (localActivityMonitor.match(paramContext, null, paramIntent))
          {
            mHits += 1;
            if (!localActivityMonitor.isBlocking()) {
              break;
            }
            paramContext = localObject1;
            if (paramInt >= 0) {
              paramContext = localActivityMonitor.getResult();
            }
            return paramContext;
          }
        }
      }
    }
    try
    {
      paramIntent.migrateExtraStreamToClipData();
      paramIntent.prepareToLeaveProcess(paramContext);
      checkStartActivityResult(ActivityManager.getService().startActivityAsUser(localIApplicationThread, paramContext.getBasePackageName(), paramIntent, paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver()), paramIBinder2, paramString, paramInt, 0, null, paramBundle, paramUserHandle.getIdentifier()), paramIntent);
      return null;
    }
    catch (RemoteException paramContext)
    {
      throw new RuntimeException("Failure from system", paramContext);
    }
  }
  
  /* Error */
  public ActivityResult execStartActivityAsCaller(Context paramContext, IBinder paramIBinder1, IBinder paramIBinder2, Activity paramActivity, Intent paramIntent, int paramInt1, Bundle paramBundle, boolean paramBoolean, int paramInt2)
  {
    // Byte code:
    //   0: sipush 379
    //   3: aload 5
    //   5: invokevirtual 570	android/content/Intent:toString	()Ljava/lang/String;
    //   8: invokestatic 521	android/util/SeempLog:record_str	(ILjava/lang/String;)I
    //   11: pop
    //   12: aload_2
    //   13: checkcast 523	android/app/IApplicationThread
    //   16: astore 10
    //   18: aload_0
    //   19: getfield 297	android/app/Instrumentation:mActivityMonitors	Ljava/util/List;
    //   22: astore_2
    //   23: aconst_null
    //   24: astore 11
    //   26: aload_2
    //   27: ifnull +157 -> 184
    //   30: aload_0
    //   31: getfield 85	android/app/Instrumentation:mSync	Ljava/lang/Object;
    //   34: astore 12
    //   36: aload 12
    //   38: monitorenter
    //   39: aload_0
    //   40: getfield 297	android/app/Instrumentation:mActivityMonitors	Ljava/util/List;
    //   43: invokeinterface 301 1 0
    //   48: istore 13
    //   50: iconst_0
    //   51: istore 14
    //   53: iload 14
    //   55: iload 13
    //   57: if_icmpge +115 -> 172
    //   60: aload_0
    //   61: getfield 297	android/app/Instrumentation:mActivityMonitors	Ljava/util/List;
    //   64: iload 14
    //   66: invokeinterface 305 2 0
    //   71: checkcast 17	android/app/Instrumentation$ActivityMonitor
    //   74: astore 15
    //   76: aconst_null
    //   77: astore_2
    //   78: aload 15
    //   80: invokevirtual 526	android/app/Instrumentation$ActivityMonitor:ignoreMatchingSpecificIntents	()Z
    //   83: ifeq +11 -> 94
    //   86: aload 15
    //   88: aload 5
    //   90: invokevirtual 530	android/app/Instrumentation$ActivityMonitor:onStartActivity	(Landroid/content/Intent;)Landroid/app/Instrumentation$ActivityResult;
    //   93: astore_2
    //   94: aload_2
    //   95: ifnull +20 -> 115
    //   98: aload 15
    //   100: aload 15
    //   102: getfield 534	android/app/Instrumentation$ActivityMonitor:mHits	I
    //   105: iconst_1
    //   106: iadd
    //   107: putfield 534	android/app/Instrumentation$ActivityMonitor:mHits	I
    //   110: aload 12
    //   112: monitorexit
    //   113: aload_2
    //   114: areturn
    //   115: aload 15
    //   117: aload_1
    //   118: aconst_null
    //   119: aload 5
    //   121: invokevirtual 315	android/app/Instrumentation$ActivityMonitor:match	(Landroid/content/Context;Landroid/app/Activity;Landroid/content/Intent;)Z
    //   124: ifeq +42 -> 166
    //   127: aload 15
    //   129: aload 15
    //   131: getfield 534	android/app/Instrumentation$ActivityMonitor:mHits	I
    //   134: iconst_1
    //   135: iadd
    //   136: putfield 534	android/app/Instrumentation$ActivityMonitor:mHits	I
    //   139: aload 15
    //   141: invokevirtual 537	android/app/Instrumentation$ActivityMonitor:isBlocking	()Z
    //   144: ifeq +28 -> 172
    //   147: aload 11
    //   149: astore_1
    //   150: iload 6
    //   152: iflt +9 -> 161
    //   155: aload 15
    //   157: invokevirtual 584	android/app/Instrumentation$ActivityMonitor:getResult	()Landroid/app/Instrumentation$ActivityResult;
    //   160: astore_1
    //   161: aload 12
    //   163: monitorexit
    //   164: aload_1
    //   165: areturn
    //   166: iinc 14 1
    //   169: goto -116 -> 53
    //   172: aload 12
    //   174: monitorexit
    //   175: goto +9 -> 184
    //   178: astore_1
    //   179: aload 12
    //   181: monitorexit
    //   182: aload_1
    //   183: athrow
    //   184: aload 5
    //   186: invokevirtual 542	android/content/Intent:migrateExtraStreamToClipData	()Z
    //   189: pop
    //   190: aload 5
    //   192: aload_1
    //   193: invokevirtual 545	android/content/Intent:prepareToLeaveProcess	(Landroid/content/Context;)V
    //   196: invokestatic 379	android/app/ActivityManager:getService	()Landroid/app/IActivityManager;
    //   199: astore_2
    //   200: aload_1
    //   201: invokevirtual 556	android/content/Context:getBasePackageName	()Ljava/lang/String;
    //   204: astore 11
    //   206: aload 5
    //   208: aload_1
    //   209: invokevirtual 549	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   212: invokevirtual 553	android/content/Intent:resolveTypeIfNeeded	(Landroid/content/ContentResolver;)Ljava/lang/String;
    //   215: astore 12
    //   217: aload 4
    //   219: ifnull +12 -> 231
    //   222: aload 4
    //   224: getfield 587	android/app/Activity:mEmbeddedID	Ljava/lang/String;
    //   227: astore_1
    //   228: goto +5 -> 233
    //   231: aconst_null
    //   232: astore_1
    //   233: aload_2
    //   234: aload 10
    //   236: aload 11
    //   238: aload 5
    //   240: aload 12
    //   242: aload_3
    //   243: aload_1
    //   244: iload 6
    //   246: iconst_0
    //   247: aconst_null
    //   248: aload 7
    //   250: iload 8
    //   252: iload 9
    //   254: invokeinterface 608 13 0
    //   259: istore 6
    //   261: iload 6
    //   263: aload 5
    //   265: invokestatic 562	android/app/Instrumentation:checkStartActivityResult	(ILjava/lang/Object;)V
    //   268: aconst_null
    //   269: areturn
    //   270: astore_1
    //   271: goto +8 -> 279
    //   274: astore_1
    //   275: goto +4 -> 279
    //   278: astore_1
    //   279: new 150	java/lang/RuntimeException
    //   282: dup
    //   283: ldc_w 564
    //   286: aload_1
    //   287: invokespecial 567	java/lang/RuntimeException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   290: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	291	0	this	Instrumentation
    //   0	291	1	paramContext	Context
    //   0	291	2	paramIBinder1	IBinder
    //   0	291	3	paramIBinder2	IBinder
    //   0	291	4	paramActivity	Activity
    //   0	291	5	paramIntent	Intent
    //   0	291	6	paramInt1	int
    //   0	291	7	paramBundle	Bundle
    //   0	291	8	paramBoolean	boolean
    //   0	291	9	paramInt2	int
    //   16	219	10	localIApplicationThread	IApplicationThread
    //   24	213	11	str	String
    //   34	207	12	localObject	Object
    //   48	10	13	i	int
    //   51	116	14	j	int
    //   74	82	15	localActivityMonitor	ActivityMonitor
    // Exception table:
    //   from	to	target	type
    //   39	50	178	finally
    //   60	76	178	finally
    //   78	94	178	finally
    //   98	113	178	finally
    //   115	147	178	finally
    //   155	161	178	finally
    //   161	164	178	finally
    //   172	175	178	finally
    //   179	182	178	finally
    //   261	268	270	android/os/RemoteException
    //   233	261	274	android/os/RemoteException
    //   184	217	278	android/os/RemoteException
    //   222	228	278	android/os/RemoteException
  }
  
  public void execStartActivityFromAppTask(Context paramContext, IBinder paramIBinder, IAppTask paramIAppTask, Intent paramIntent, Bundle paramBundle)
  {
    SeempLog.record_str(380, paramIntent.toString());
    IApplicationThread localIApplicationThread = (IApplicationThread)paramIBinder;
    if (mActivityMonitors != null) {
      synchronized (mSync)
      {
        int i = mActivityMonitors.size();
        for (int j = 0; j < i; j++)
        {
          ActivityMonitor localActivityMonitor = (ActivityMonitor)mActivityMonitors.get(j);
          paramIBinder = null;
          if (localActivityMonitor.ignoreMatchingSpecificIntents()) {
            paramIBinder = localActivityMonitor.onStartActivity(paramIntent);
          }
          if (paramIBinder != null)
          {
            mHits += 1;
            return;
          }
          if (localActivityMonitor.match(paramContext, null, paramIntent))
          {
            mHits += 1;
            if (!localActivityMonitor.isBlocking()) {
              break;
            }
            return;
          }
        }
      }
    }
    try
    {
      paramIntent.migrateExtraStreamToClipData();
      paramIntent.prepareToLeaveProcess(paramContext);
      checkStartActivityResult(paramIAppTask.startActivity(localIApplicationThread.asBinder(), paramContext.getBasePackageName(), paramIntent, paramIntent.resolveTypeIfNeeded(paramContext.getContentResolver()), paramBundle), paramIntent);
      return;
    }
    catch (RemoteException paramContext)
    {
      throw new RuntimeException("Failure from system", paramContext);
    }
  }
  
  public void finish(int paramInt, Bundle paramBundle)
  {
    if (mAutomaticPerformanceSnapshots) {
      endPerformanceSnapshot();
    }
    Bundle localBundle = paramBundle;
    if (mPerfMetrics != null)
    {
      localBundle = paramBundle;
      if (paramBundle == null) {
        localBundle = new Bundle();
      }
      localBundle.putAll(mPerfMetrics);
    }
    if ((mUiAutomation != null) && (!mUiAutomation.isDestroyed()))
    {
      mUiAutomation.disconnect();
      mUiAutomation = null;
    }
    mThread.finishInstrumentation(paramInt, localBundle);
  }
  
  public Bundle getAllocCounts()
  {
    Bundle localBundle = new Bundle();
    localBundle.putLong("global_alloc_count", Debug.getGlobalAllocCount());
    localBundle.putLong("global_alloc_size", Debug.getGlobalAllocSize());
    localBundle.putLong("global_freed_count", Debug.getGlobalFreedCount());
    localBundle.putLong("global_freed_size", Debug.getGlobalFreedSize());
    localBundle.putLong("gc_invocation_count", Debug.getGlobalGcInvocationCount());
    return localBundle;
  }
  
  public Bundle getBinderCounts()
  {
    Bundle localBundle = new Bundle();
    localBundle.putLong("sent_transactions", Debug.getBinderSentTransactions());
    localBundle.putLong("received_transactions", Debug.getBinderReceivedTransactions());
    return localBundle;
  }
  
  public ComponentName getComponentName()
  {
    return mComponent;
  }
  
  public Context getContext()
  {
    return mInstrContext;
  }
  
  public String getProcessName()
  {
    return mThread.getProcessName();
  }
  
  public Context getTargetContext()
  {
    return mAppContext;
  }
  
  public UiAutomation getUiAutomation()
  {
    return getUiAutomation(0);
  }
  
  public UiAutomation getUiAutomation(int paramInt)
  {
    int i;
    if ((mUiAutomation != null) && (!mUiAutomation.isDestroyed())) {
      i = 0;
    } else {
      i = 1;
    }
    if (mUiAutomationConnection != null)
    {
      if ((i == 0) && (mUiAutomation.getFlags() == paramInt)) {
        return mUiAutomation;
      }
      if (i != 0) {
        mUiAutomation = new UiAutomation(getTargetContext().getMainLooper(), mUiAutomationConnection);
      } else {
        mUiAutomation.disconnect();
      }
      mUiAutomation.connect(paramInt);
      return mUiAutomation;
    }
    return null;
  }
  
  final void init(ActivityThread paramActivityThread, Context paramContext1, Context paramContext2, ComponentName paramComponentName, IInstrumentationWatcher paramIInstrumentationWatcher, IUiAutomationConnection paramIUiAutomationConnection)
  {
    mThread = paramActivityThread;
    mThread.getLooper();
    mMessageQueue = Looper.myQueue();
    mInstrContext = paramContext1;
    mAppContext = paramContext2;
    mComponent = paramComponentName;
    mWatcher = paramIInstrumentationWatcher;
    mUiAutomationConnection = paramIUiAutomationConnection;
  }
  
  public boolean invokeContextMenuAction(Activity paramActivity, int paramInt1, int paramInt2)
  {
    validateNotAppThread();
    sendKeySync(new KeyEvent(0, 23));
    waitForIdleSync();
    try
    {
      Thread.sleep(ViewConfiguration.getLongPressTimeout());
      sendKeySync(new KeyEvent(1, 23));
      waitForIdleSync();
      paramActivity = new Runnable()
      {
        private final Activity activity;
        private final int flags;
        private final int identifier;
        boolean returnValue;
        
        public void run()
        {
          returnValue = activity.getWindow().performContextMenuIdentifierAction(identifier, flags);
        }
      };
      runOnMainSync(paramActivity);
      return returnValue;
    }
    catch (InterruptedException paramActivity)
    {
      Log.e("Instrumentation", "Could not sleep for long press timeout", paramActivity);
    }
    return false;
  }
  
  public boolean invokeMenuActionSync(Activity paramActivity, int paramInt1, int paramInt2)
  {
    paramActivity = new Runnable()
    {
      private final Activity activity;
      private final int flags;
      private final int identifier;
      boolean returnValue;
      
      public void run()
      {
        returnValue = activity.getWindow().performPanelIdentifierAction(0, identifier, flags);
      }
    };
    runOnMainSync(paramActivity);
    return returnValue;
  }
  
  public boolean isProfiling()
  {
    return mThread.isProfiling();
  }
  
  public Activity newActivity(Class<?> paramClass, Context paramContext, IBinder paramIBinder, Application paramApplication, Intent paramIntent, ActivityInfo paramActivityInfo, CharSequence paramCharSequence, Activity paramActivity, String paramString, Object paramObject)
    throws InstantiationException, IllegalAccessException
  {
    paramClass = (Activity)paramClass.newInstance();
    if (paramApplication == null) {
      paramApplication = new Application();
    }
    paramClass.attach(paramContext, null, this, paramIBinder, 0, paramApplication, paramIntent, paramActivityInfo, paramCharSequence, paramActivity, paramString, (Activity.NonConfigurationInstances)paramObject, new Configuration(), null, null, null, null);
    return paramClass;
  }
  
  public Activity newActivity(ClassLoader paramClassLoader, String paramString, Intent paramIntent)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException
  {
    String str;
    if ((paramIntent != null) && (paramIntent.getComponent() != null)) {
      str = paramIntent.getComponent().getPackageName();
    } else {
      str = null;
    }
    return getFactory(str).instantiateActivity(paramClassLoader, paramString, paramIntent);
  }
  
  public Application newApplication(ClassLoader paramClassLoader, String paramString, Context paramContext)
    throws InstantiationException, IllegalAccessException, ClassNotFoundException
  {
    paramClassLoader = getFactory(paramContext.getPackageName()).instantiateApplication(paramClassLoader, paramString);
    paramClassLoader.attach(paramContext);
    return paramClassLoader;
  }
  
  public void onCreate(Bundle paramBundle) {}
  
  public void onDestroy() {}
  
  public boolean onException(Object paramObject, Throwable paramThrowable)
  {
    return false;
  }
  
  public void onStart() {}
  
  public void removeMonitor(ActivityMonitor paramActivityMonitor)
  {
    synchronized (mSync)
    {
      mActivityMonitors.remove(paramActivityMonitor);
      return;
    }
  }
  
  public void runOnMainSync(Runnable paramRunnable)
  {
    validateNotAppThread();
    paramRunnable = new SyncRunnable(paramRunnable);
    mThread.getHandler().post(paramRunnable);
    paramRunnable.waitForComplete();
  }
  
  public void sendCharacterSync(int paramInt)
  {
    sendKeySync(new KeyEvent(0, paramInt));
    sendKeySync(new KeyEvent(1, paramInt));
  }
  
  public void sendKeyDownUpSync(int paramInt)
  {
    sendKeySync(new KeyEvent(0, paramInt));
    sendKeySync(new KeyEvent(1, paramInt));
  }
  
  public void sendKeySync(KeyEvent paramKeyEvent)
  {
    validateNotAppThread();
    long l1 = paramKeyEvent.getDownTime();
    long l2 = paramKeyEvent.getEventTime();
    int i = paramKeyEvent.getAction();
    int j = paramKeyEvent.getKeyCode();
    int k = paramKeyEvent.getRepeatCount();
    int m = paramKeyEvent.getMetaState();
    int n = paramKeyEvent.getDeviceId();
    int i1 = paramKeyEvent.getScanCode();
    int i2 = paramKeyEvent.getSource();
    int i3 = paramKeyEvent.getFlags();
    int i4 = i2;
    if (i2 == 0) {
      i4 = 257;
    }
    long l3 = l2;
    if (l2 == 0L) {
      l3 = SystemClock.uptimeMillis();
    }
    l2 = l1;
    if (l1 == 0L) {
      l2 = l3;
    }
    paramKeyEvent = new KeyEvent(l2, l3, i, j, k, m, n, i1, i3 | 0x8, i4);
    InputManager.getInstance().injectInputEvent(paramKeyEvent, 2);
  }
  
  public void sendPointerSync(MotionEvent paramMotionEvent)
  {
    validateNotAppThread();
    if ((paramMotionEvent.getSource() & 0x2) == 0) {
      paramMotionEvent.setSource(4098);
    }
    InputManager.getInstance().injectInputEvent(paramMotionEvent, 2);
  }
  
  public void sendStatus(int paramInt, Bundle paramBundle)
  {
    if (mWatcher != null) {
      try
      {
        mWatcher.instrumentationStatus(mComponent, paramInt, paramBundle);
      }
      catch (RemoteException paramBundle)
      {
        mWatcher = null;
      }
    }
  }
  
  public void sendStringSync(String paramString)
  {
    if (paramString == null) {
      return;
    }
    paramString = KeyCharacterMap.load(-1).getEvents(paramString.toCharArray());
    if (paramString != null) {
      for (int i = 0; i < paramString.length; i++) {
        sendKeySync(KeyEvent.changeTimeRepeat(paramString[i], SystemClock.uptimeMillis(), 0));
      }
    }
  }
  
  public void sendTrackballEventSync(MotionEvent paramMotionEvent)
  {
    validateNotAppThread();
    if ((paramMotionEvent.getSource() & 0x4) == 0) {
      paramMotionEvent.setSource(65540);
    }
    InputManager.getInstance().injectInputEvent(paramMotionEvent, 2);
  }
  
  public void setAutomaticPerformanceSnapshots()
  {
    mAutomaticPerformanceSnapshots = true;
    mPerformanceCollector = new PerformanceCollector();
  }
  
  public void setInTouchMode(boolean paramBoolean)
  {
    try
    {
      IWindowManager.Stub.asInterface(ServiceManager.getService("window")).setInTouchMode(paramBoolean);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public void start()
  {
    if (mRunner == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Instr: ");
      localStringBuilder.append(getClass().getName());
      mRunner = new InstrumentationThread(localStringBuilder.toString());
      mRunner.start();
      return;
    }
    throw new RuntimeException("Instrumentation already started");
  }
  
  public Activity startActivitySync(Intent paramIntent)
  {
    return startActivitySync(paramIntent, null);
  }
  
  public Activity startActivitySync(Intent paramIntent, Bundle paramBundle)
  {
    SeempLog.record_str(376, paramIntent.toString());
    validateNotAppThread();
    synchronized (mSync)
    {
      Intent localIntent = new android/content/Intent;
      localIntent.<init>(paramIntent);
      paramIntent = localIntent.resolveActivityInfo(getTargetContext().getPackageManager(), 0);
      if (paramIntent != null)
      {
        Object localObject2 = mThread.getProcessName();
        if (processName.equals(localObject2))
        {
          localObject2 = new android/content/ComponentName;
          ((ComponentName)localObject2).<init>(applicationInfo.packageName, name);
          localIntent.setComponent((ComponentName)localObject2);
          paramIntent = new android/app/Instrumentation$ActivityWaiter;
          paramIntent.<init>(localIntent);
          if (mWaitingActivities == null)
          {
            localObject2 = new java/util/ArrayList;
            ((ArrayList)localObject2).<init>();
            mWaitingActivities = ((List)localObject2);
          }
          mWaitingActivities.add(paramIntent);
          getTargetContext().startActivity(localIntent, paramBundle);
          do
          {
            try
            {
              mSync.wait();
            }
            catch (InterruptedException paramBundle) {}
          } while (mWaitingActivities.contains(paramIntent));
          paramIntent = activity;
          return paramIntent;
        }
        paramBundle = new java/lang/RuntimeException;
        StringBuilder localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("Intent in process ");
        localStringBuilder.append((String)localObject2);
        localStringBuilder.append(" resolved to different process ");
        localStringBuilder.append(processName);
        localStringBuilder.append(": ");
        localStringBuilder.append(localIntent);
        paramBundle.<init>(localStringBuilder.toString());
        throw paramBundle;
      }
      paramIntent = new java/lang/RuntimeException;
      paramBundle = new java/lang/StringBuilder;
      paramBundle.<init>();
      paramBundle.append("Unable to resolve activity for: ");
      paramBundle.append(localIntent);
      paramIntent.<init>(paramBundle.toString());
      throw paramIntent;
    }
  }
  
  @Deprecated
  public void startAllocCounting()
  {
    Runtime.getRuntime().gc();
    Runtime.getRuntime().runFinalization();
    Runtime.getRuntime().gc();
    Debug.resetAllCounts();
    Debug.startAllocCounting();
  }
  
  public void startPerformanceSnapshot()
  {
    if (!isProfiling()) {
      mPerformanceCollector.beginSnapshot(null);
    }
  }
  
  public void startProfiling()
  {
    if (mThread.isProfiling())
    {
      File localFile = new File(mThread.getProfileFilePath());
      localFile.getParentFile().mkdirs();
      Debug.startMethodTracing(localFile.toString(), 8388608);
    }
  }
  
  @Deprecated
  public void stopAllocCounting()
  {
    Runtime.getRuntime().gc();
    Runtime.getRuntime().runFinalization();
    Runtime.getRuntime().gc();
    Debug.stopAllocCounting();
  }
  
  public void stopProfiling()
  {
    if (mThread.isProfiling()) {
      Debug.stopMethodTracing();
    }
  }
  
  public void waitForIdle(Runnable paramRunnable)
  {
    mMessageQueue.addIdleHandler(new Idler(paramRunnable));
    mThread.getHandler().post(new EmptyRunnable(null));
  }
  
  public void waitForIdleSync()
  {
    validateNotAppThread();
    Idler localIdler = new Idler(null);
    mMessageQueue.addIdleHandler(localIdler);
    mThread.getHandler().post(new EmptyRunnable(null));
    localIdler.waitForIdle();
  }
  
  public Activity waitForMonitor(ActivityMonitor paramActivityMonitor)
  {
    Activity localActivity = paramActivityMonitor.waitForActivity();
    synchronized (mSync)
    {
      mActivityMonitors.remove(paramActivityMonitor);
      return localActivity;
    }
  }
  
  public Activity waitForMonitorWithTimeout(ActivityMonitor paramActivityMonitor, long paramLong)
  {
    Activity localActivity = paramActivityMonitor.waitForActivityWithTimeout(paramLong);
    synchronized (mSync)
    {
      mActivityMonitors.remove(paramActivityMonitor);
      return localActivity;
    }
  }
  
  private final class ActivityGoing
    implements MessageQueue.IdleHandler
  {
    private final Instrumentation.ActivityWaiter mWaiter;
    
    public ActivityGoing(Instrumentation.ActivityWaiter paramActivityWaiter)
    {
      mWaiter = paramActivityWaiter;
    }
    
    public final boolean queueIdle()
    {
      synchronized (mSync)
      {
        mWaitingActivities.remove(mWaiter);
        mSync.notifyAll();
        return false;
      }
    }
  }
  
  public static class ActivityMonitor
  {
    private final boolean mBlock;
    private final String mClass;
    int mHits = 0;
    private final boolean mIgnoreMatchingSpecificIntents;
    Activity mLastActivity = null;
    private final Instrumentation.ActivityResult mResult;
    private final IntentFilter mWhich;
    
    public ActivityMonitor()
    {
      mWhich = null;
      mClass = null;
      mResult = null;
      mBlock = false;
      mIgnoreMatchingSpecificIntents = true;
    }
    
    public ActivityMonitor(IntentFilter paramIntentFilter, Instrumentation.ActivityResult paramActivityResult, boolean paramBoolean)
    {
      mWhich = paramIntentFilter;
      mClass = null;
      mResult = paramActivityResult;
      mBlock = paramBoolean;
      mIgnoreMatchingSpecificIntents = false;
    }
    
    public ActivityMonitor(String paramString, Instrumentation.ActivityResult paramActivityResult, boolean paramBoolean)
    {
      mWhich = null;
      mClass = paramString;
      mResult = paramActivityResult;
      mBlock = paramBoolean;
      mIgnoreMatchingSpecificIntents = false;
    }
    
    public final IntentFilter getFilter()
    {
      return mWhich;
    }
    
    public final int getHits()
    {
      return mHits;
    }
    
    public final Activity getLastActivity()
    {
      return mLastActivity;
    }
    
    public final Instrumentation.ActivityResult getResult()
    {
      return mResult;
    }
    
    final boolean ignoreMatchingSpecificIntents()
    {
      return mIgnoreMatchingSpecificIntents;
    }
    
    public final boolean isBlocking()
    {
      return mBlock;
    }
    
    final boolean match(Context paramContext, Activity paramActivity, Intent paramIntent)
    {
      if (mIgnoreMatchingSpecificIntents) {
        return false;
      }
      try
      {
        if ((mWhich != null) && (mWhich.match(paramContext.getContentResolver(), paramIntent, true, "Instrumentation") < 0)) {
          return false;
        }
        if (mClass != null)
        {
          paramContext = null;
          if (paramActivity != null) {
            paramContext = paramActivity.getClass().getName();
          } else if (paramIntent.getComponent() != null) {
            paramContext = paramIntent.getComponent().getClassName();
          }
          if ((paramContext == null) || (!mClass.equals(paramContext))) {
            return false;
          }
        }
        if (paramActivity != null)
        {
          mLastActivity = paramActivity;
          notifyAll();
        }
        return true;
      }
      finally {}
    }
    
    public Instrumentation.ActivityResult onStartActivity(Intent paramIntent)
    {
      return null;
    }
    
    public final Activity waitForActivity()
    {
      try
      {
        for (;;)
        {
          Activity localActivity1 = mLastActivity;
          if (localActivity1 == null) {
            try
            {
              wait();
            }
            catch (InterruptedException localInterruptedException)
            {
              for (;;) {}
            }
          }
        }
        Activity localActivity2 = mLastActivity;
        mLastActivity = null;
        return localActivity2;
      }
      finally {}
    }
    
    public final Activity waitForActivityWithTimeout(long paramLong)
    {
      try
      {
        Activity localActivity1 = mLastActivity;
        if (localActivity1 == null) {
          try
          {
            wait(paramLong);
          }
          catch (InterruptedException localInterruptedException) {}
        }
        if (mLastActivity == null) {
          return null;
        }
        Activity localActivity2 = mLastActivity;
        mLastActivity = null;
        return localActivity2;
      }
      finally {}
    }
  }
  
  public static final class ActivityResult
  {
    private final int mResultCode;
    private final Intent mResultData;
    
    public ActivityResult(int paramInt, Intent paramIntent)
    {
      mResultCode = paramInt;
      mResultData = paramIntent;
    }
    
    public int getResultCode()
    {
      return mResultCode;
    }
    
    public Intent getResultData()
    {
      return mResultData;
    }
  }
  
  private static final class ActivityWaiter
  {
    public Activity activity;
    public final Intent intent;
    
    public ActivityWaiter(Intent paramIntent)
    {
      intent = paramIntent;
    }
  }
  
  private static final class EmptyRunnable
    implements Runnable
  {
    private EmptyRunnable() {}
    
    public void run() {}
  }
  
  private static final class Idler
    implements MessageQueue.IdleHandler
  {
    private final Runnable mCallback;
    private boolean mIdle;
    
    public Idler(Runnable paramRunnable)
    {
      mCallback = paramRunnable;
      mIdle = false;
    }
    
    public final boolean queueIdle()
    {
      if (mCallback != null) {
        mCallback.run();
      }
      try
      {
        mIdle = true;
        notifyAll();
        return false;
      }
      finally {}
    }
    
    public void waitForIdle()
    {
      try
      {
        for (;;)
        {
          boolean bool = mIdle;
          if (!bool) {
            try
            {
              wait();
            }
            catch (InterruptedException localInterruptedException)
            {
              for (;;) {}
            }
          }
        }
        return;
      }
      finally {}
    }
  }
  
  private final class InstrumentationThread
    extends Thread
  {
    public InstrumentationThread(String paramString)
    {
      super();
    }
    
    public void run()
    {
      try
      {
        Process.setThreadPriority(-8);
      }
      catch (RuntimeException localRuntimeException)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Exception setting priority of instrumentation thread ");
        localStringBuilder.append(Process.myTid());
        Log.w("Instrumentation", localStringBuilder.toString(), localRuntimeException);
      }
      if (mAutomaticPerformanceSnapshots) {
        startPerformanceSnapshot();
      }
      onStart();
    }
  }
  
  private static final class SyncRunnable
    implements Runnable
  {
    private boolean mComplete;
    private final Runnable mTarget;
    
    public SyncRunnable(Runnable paramRunnable)
    {
      mTarget = paramRunnable;
    }
    
    public void run()
    {
      mTarget.run();
      try
      {
        mComplete = true;
        notifyAll();
        return;
      }
      finally {}
    }
    
    public void waitForComplete()
    {
      try
      {
        for (;;)
        {
          boolean bool = mComplete;
          if (!bool) {
            try
            {
              wait();
            }
            catch (InterruptedException localInterruptedException)
            {
              for (;;) {}
            }
          }
        }
        return;
      }
      finally {}
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UiAutomationFlags {}
}
