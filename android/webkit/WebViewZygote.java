package android.webkit;

import android.app.LoadedApk;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.ChildZygoteProcess;
import android.os.Process;
import android.os.ZygoteProcess;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class WebViewZygote
{
  private static final String LOGTAG = "WebViewZygote";
  private static final Object sLock = new Object();
  @GuardedBy("sLock")
  private static boolean sMultiprocessEnabled = false;
  @GuardedBy("sLock")
  private static PackageInfo sPackage;
  @GuardedBy("sLock")
  private static ApplicationInfo sPackageOriginalAppInfo;
  @GuardedBy("sLock")
  private static ChildZygoteProcess sZygote;
  
  public WebViewZygote() {}
  
  @GuardedBy("sLock")
  private static void connectToZygoteIfNeededLocked()
  {
    if (sZygote != null) {
      return;
    }
    if (sPackage == null)
    {
      Log.e("WebViewZygote", "Cannot connect to zygote, no package specified");
      return;
    }
    try
    {
      sZygote = Process.zygoteProcess.startChildZygote("com.android.internal.os.WebViewZygoteInit", "webview_zygote", 1053, 1053, null, 0, "webview_zygote", sPackageapplicationInfo.primaryCpuAbi, null);
      Object localObject1 = new java/util/ArrayList;
      ((ArrayList)localObject1).<init>(10);
      Object localObject2 = new java/util/ArrayList;
      ((ArrayList)localObject2).<init>(10);
      LoadedApk.makePaths(null, false, sPackageapplicationInfo, (List)localObject1, (List)localObject2);
      String str1 = TextUtils.join(File.pathSeparator, (Iterable)localObject2);
      if (((List)localObject1).size() == 1) {
        localObject2 = (String)((List)localObject1).get(0);
      } else {
        localObject2 = TextUtils.join(File.pathSeparator, (Iterable)localObject1);
      }
      String str2 = WebViewFactory.getWebViewLibrary(sPackageapplicationInfo);
      LoadedApk.makePaths(null, false, sPackageOriginalAppInfo, (List)localObject1, null);
      if (((List)localObject1).size() == 1) {
        localObject1 = (String)((List)localObject1).get(0);
      } else {
        localObject1 = TextUtils.join(File.pathSeparator, (Iterable)localObject1);
      }
      ZygoteProcess.waitForConnectionToZygote(sZygote.getPrimarySocketAddress());
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Preloading package ");
      localStringBuilder.append((String)localObject2);
      localStringBuilder.append(" ");
      localStringBuilder.append(str1);
      Log.d("WebViewZygote", localStringBuilder.toString());
      sZygote.preloadPackageForAbi((String)localObject2, str1, str2, (String)localObject1, android.os.Build.SUPPORTED_ABIS[0]);
    }
    catch (Exception localException)
    {
      Log.e("WebViewZygote", "Error connecting to webview zygote", localException);
      stopZygoteLocked();
    }
  }
  
  public static String getPackageName()
  {
    synchronized (sLock)
    {
      String str = sPackagepackageName;
      return str;
    }
  }
  
  public static ZygoteProcess getProcess()
  {
    synchronized (sLock)
    {
      if (sZygote != null)
      {
        localChildZygoteProcess = sZygote;
        return localChildZygoteProcess;
      }
      connectToZygoteIfNeededLocked();
      ChildZygoteProcess localChildZygoteProcess = sZygote;
      return localChildZygoteProcess;
    }
  }
  
  public static boolean isMultiprocessEnabled()
  {
    synchronized (sLock)
    {
      boolean bool;
      if ((sMultiprocessEnabled) && (sPackage != null)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public static void onWebViewProviderChanged(PackageInfo paramPackageInfo, ApplicationInfo paramApplicationInfo)
  {
    synchronized (sLock)
    {
      sPackage = paramPackageInfo;
      sPackageOriginalAppInfo = paramApplicationInfo;
      if (!sMultiprocessEnabled) {
        return;
      }
      stopZygoteLocked();
      return;
    }
  }
  
  public static void setMultiprocessEnabled(boolean paramBoolean)
  {
    synchronized (sLock)
    {
      sMultiprocessEnabled = paramBoolean;
      if (paramBoolean) {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(_..Lambda.xYTrYQCPf1HcdlWzDof3mq93ihs.INSTANCE);
      } else {
        stopZygoteLocked();
      }
      return;
    }
  }
  
  @GuardedBy("sLock")
  private static void stopZygoteLocked()
  {
    if (sZygote != null)
    {
      sZygote.close();
      Process.killProcess(sZygote.getPid());
      sZygote = null;
    }
  }
}
