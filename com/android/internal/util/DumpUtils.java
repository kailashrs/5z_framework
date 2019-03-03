package com.android.internal.util;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.ComponentName.WithComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.text.TextUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Predicate;

public final class DumpUtils
{
  private static final boolean DEBUG = false;
  private static final String TAG = "DumpUtils";
  
  private DumpUtils() {}
  
  public static boolean checkDumpAndUsageStatsPermission(Context paramContext, String paramString, PrintWriter paramPrintWriter)
  {
    boolean bool;
    if ((checkDumpPermission(paramContext, paramString, paramPrintWriter)) && (checkUsageStatsPermission(paramContext, paramString, paramPrintWriter))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean checkDumpPermission(Context paramContext, String paramString, PrintWriter paramPrintWriter)
  {
    if (paramContext.checkCallingOrSelfPermission("android.permission.DUMP") != 0)
    {
      paramContext = new StringBuilder();
      paramContext.append("Permission Denial: can't dump ");
      paramContext.append(paramString);
      paramContext.append(" from from pid=");
      paramContext.append(Binder.getCallingPid());
      paramContext.append(", uid=");
      paramContext.append(Binder.getCallingUid());
      paramContext.append(" due to missing android.permission.DUMP permission");
      logMessage(paramPrintWriter, paramContext.toString());
      return false;
    }
    return true;
  }
  
  public static boolean checkUsageStatsPermission(Context paramContext, String paramString, PrintWriter paramPrintWriter)
  {
    int i = Binder.getCallingUid();
    if ((i != 0) && (i != 1000) && (i != 1067) && (i != 2000))
    {
      if (paramContext.checkCallingOrSelfPermission("android.permission.PACKAGE_USAGE_STATS") != 0)
      {
        paramContext = new StringBuilder();
        paramContext.append("Permission Denial: can't dump ");
        paramContext.append(paramString);
        paramContext.append(" from from pid=");
        paramContext.append(Binder.getCallingPid());
        paramContext.append(", uid=");
        paramContext.append(Binder.getCallingUid());
        paramContext.append(" due to missing android.permission.PACKAGE_USAGE_STATS permission");
        logMessage(paramPrintWriter, paramContext.toString());
        return false;
      }
      AppOpsManager localAppOpsManager = (AppOpsManager)paramContext.getSystemService(AppOpsManager.class);
      paramContext = paramContext.getPackageManager().getPackagesForUid(i);
      if (paramContext != null)
      {
        int j = paramContext.length;
        int k = 0;
        while (k < j)
        {
          int m = localAppOpsManager.noteOpNoThrow(43, i, paramContext[k]);
          if (m != 0)
          {
            if (m != 3) {
              k++;
            } else {
              return true;
            }
          }
          else {
            return true;
          }
        }
      }
      paramContext = new StringBuilder();
      paramContext.append("Permission Denial: can't dump ");
      paramContext.append(paramString);
      paramContext.append(" from from pid=");
      paramContext.append(Binder.getCallingPid());
      paramContext.append(", uid=");
      paramContext.append(Binder.getCallingUid());
      paramContext.append(" due to android:get_usage_stats app-op not allowed");
      logMessage(paramPrintWriter, paramContext.toString());
      return false;
    }
    return true;
  }
  
  public static void dumpAsync(Handler paramHandler, final Dump paramDump, PrintWriter paramPrintWriter, final String paramString, long paramLong)
  {
    StringWriter localStringWriter = new StringWriter();
    if (paramHandler.runWithScissors(new Runnable()
    {
      public void run()
      {
        FastPrintWriter localFastPrintWriter = new FastPrintWriter(DumpUtils.this);
        paramDump.dump(localFastPrintWriter, paramString);
        localFastPrintWriter.close();
      }
    }, paramLong)) {
      paramPrintWriter.print(localStringWriter.toString());
    } else {
      paramPrintWriter.println("... timed out");
    }
  }
  
  public static <TRec extends ComponentName.WithComponentName> Predicate<TRec> filterRecord(String paramString)
  {
    if (TextUtils.isEmpty(paramString)) {
      return _..Lambda.DumpUtils.D1OlZP6xIpu72ypnJd0fzx0wd6I.INSTANCE;
    }
    if ("all".equals(paramString)) {
      return _..Lambda.DumpUtils.eRa1rlfDk6Og2yFeXGHqUGPzRF0.INSTANCE;
    }
    if ("all-platform".equals(paramString)) {
      return _..Lambda.kVylv1rl9MOSbHFZoVyK5dl1kfY.INSTANCE;
    }
    if ("all-non-platform".equals(paramString)) {
      return _..Lambda.JwOUSWW2_Jzu15y4Kn4JuPh8tWM.INSTANCE;
    }
    ComponentName localComponentName = ComponentName.unflattenFromString(paramString);
    if (localComponentName != null) {
      return new _..Lambda.DumpUtils.X8irOs5hfloCKy89_l1HRA1QeG0(localComponentName);
    }
    return new _..Lambda.DumpUtils.vCLO_0ezRxkpSERUWCFrJ0ph5jg(ParseUtils.parseIntWithBase(paramString, 16, -1), paramString);
  }
  
  public static boolean isNonPlatformPackage(ComponentName.WithComponentName paramWithComponentName)
  {
    boolean bool;
    if ((paramWithComponentName != null) && (!isPlatformPackage(paramWithComponentName.getComponentName()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isNonPlatformPackage(ComponentName paramComponentName)
  {
    boolean bool;
    if ((paramComponentName != null) && (isNonPlatformPackage(paramComponentName.getPackageName()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isNonPlatformPackage(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (!isPlatformPackage(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isPlatformPackage(ComponentName.WithComponentName paramWithComponentName)
  {
    boolean bool;
    if ((paramWithComponentName != null) && (isPlatformPackage(paramWithComponentName.getComponentName()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isPlatformPackage(ComponentName paramComponentName)
  {
    boolean bool;
    if ((paramComponentName != null) && (isPlatformPackage(paramComponentName.getPackageName()))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isPlatformPackage(String paramString)
  {
    boolean bool;
    if ((paramString != null) && ((paramString.equals("android")) || (paramString.startsWith("android.")) || (paramString.startsWith("com.android.")))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static void logMessage(PrintWriter paramPrintWriter, String paramString)
  {
    paramPrintWriter.println(paramString);
  }
  
  public static abstract interface Dump
  {
    public abstract void dump(PrintWriter paramPrintWriter, String paramString);
  }
}
