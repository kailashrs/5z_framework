package android.app;

import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Service
  extends ContextWrapper
  implements ComponentCallbacks2
{
  public static final int START_CONTINUATION_MASK = 15;
  public static final int START_FLAG_REDELIVERY = 1;
  public static final int START_FLAG_RETRY = 2;
  public static final int START_NOT_STICKY = 2;
  public static final int START_REDELIVER_INTENT = 3;
  public static final int START_STICKY = 1;
  public static final int START_STICKY_COMPATIBILITY = 0;
  public static final int START_TASK_REMOVED_COMPLETE = 1000;
  public static final int STOP_FOREGROUND_DETACH = 2;
  public static final int STOP_FOREGROUND_REMOVE = 1;
  private static final String TAG = "Service";
  private IActivityManager mActivityManager = null;
  private Application mApplication = null;
  private String mClassName = null;
  private boolean mStartCompatibility = false;
  private ActivityThread mThread = null;
  private IBinder mToken = null;
  
  public Service()
  {
    super(null);
  }
  
  public final void attach(Context paramContext, ActivityThread paramActivityThread, String paramString, IBinder paramIBinder, Application paramApplication, Object paramObject)
  {
    attachBaseContext(paramContext);
    mThread = paramActivityThread;
    mClassName = paramString;
    mToken = paramIBinder;
    mApplication = paramApplication;
    mActivityManager = ((IActivityManager)paramObject);
    boolean bool;
    if (getApplicationInfotargetSdkVersion < 5) {
      bool = true;
    } else {
      bool = false;
    }
    mStartCompatibility = bool;
  }
  
  public final void detachAndCleanUp()
  {
    mToken = null;
  }
  
  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("nothing to dump");
  }
  
  public final Application getApplication()
  {
    return mApplication;
  }
  
  final String getClassName()
  {
    return mClassName;
  }
  
  public abstract IBinder onBind(Intent paramIntent);
  
  public void onConfigurationChanged(Configuration paramConfiguration) {}
  
  public void onCreate() {}
  
  public void onDestroy() {}
  
  public void onLowMemory() {}
  
  public void onRebind(Intent paramIntent) {}
  
  @Deprecated
  public void onStart(Intent paramIntent, int paramInt) {}
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    onStart(paramIntent, paramInt2);
    return mStartCompatibility ^ true;
  }
  
  public void onTaskRemoved(Intent paramIntent) {}
  
  public void onTrimMemory(int paramInt) {}
  
  public boolean onUnbind(Intent paramIntent)
  {
    return false;
  }
  
  @Deprecated
  public final void setForeground(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("setForeground: ignoring old API call on ");
    localStringBuilder.append(getClass().getName());
    Log.w("Service", localStringBuilder.toString());
  }
  
  public final void startForeground(int paramInt, Notification paramNotification)
  {
    try
    {
      IActivityManager localIActivityManager = mActivityManager;
      ComponentName localComponentName = new android/content/ComponentName;
      localComponentName.<init>(this, mClassName);
      localIActivityManager.setServiceForeground(localComponentName, mToken, paramInt, paramNotification, 0);
    }
    catch (RemoteException paramNotification) {}
  }
  
  public final void stopForeground(int paramInt)
  {
    try
    {
      IActivityManager localIActivityManager = mActivityManager;
      ComponentName localComponentName = new android/content/ComponentName;
      localComponentName.<init>(this, mClassName);
      localIActivityManager.setServiceForeground(localComponentName, mToken, 0, null, paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public final void stopForeground(boolean paramBoolean)
  {
    stopForeground(paramBoolean);
  }
  
  public final void stopSelf()
  {
    stopSelf(-1);
  }
  
  public final void stopSelf(int paramInt)
  {
    if (mActivityManager == null) {
      return;
    }
    try
    {
      IActivityManager localIActivityManager = mActivityManager;
      ComponentName localComponentName = new android/content/ComponentName;
      localComponentName.<init>(this, mClassName);
      localIActivityManager.stopServiceToken(localComponentName, mToken, paramInt);
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public final boolean stopSelfResult(int paramInt)
  {
    if (mActivityManager == null) {
      return false;
    }
    try
    {
      IActivityManager localIActivityManager = mActivityManager;
      ComponentName localComponentName = new android/content/ComponentName;
      localComponentName.<init>(this, mClassName);
      boolean bool = localIActivityManager.stopServiceToken(localComponentName, mToken, paramInt);
      return bool;
    }
    catch (RemoteException localRemoteException) {}
    return false;
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StartArgFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StartResult {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface StopForegroundFlags {}
}
