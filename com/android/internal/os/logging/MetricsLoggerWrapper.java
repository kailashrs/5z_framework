package com.android.internal.os.logging;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Pair;
import android.util.StatsLog;
import com.android.internal.logging.MetricsLogger;

public class MetricsLoggerWrapper
{
  private static final int METRIC_VALUE_DISMISSED_BY_DRAG = 1;
  private static final int METRIC_VALUE_DISMISSED_BY_TAP = 0;
  
  public MetricsLoggerWrapper() {}
  
  private static int getUid(Context paramContext, ComponentName paramComponentName, int paramInt)
  {
    int i = -1;
    try
    {
      paramInt = getPackageManagergetApplicationInfoAsUsergetPackageName0uid;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
      paramInt = i;
    }
    return paramInt;
  }
  
  public static void logAppOverlayEnter(int paramInt1, String paramString, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    if (paramBoolean1) {
      if (paramInt2 != 2038) {
        StatsLog.write(59, paramInt1, paramString, true, 1);
      } else if (!paramBoolean2) {
        StatsLog.write(59, paramInt1, paramString, false, 1);
      }
    }
  }
  
  public static void logAppOverlayExit(int paramInt1, String paramString, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    if (paramBoolean1) {
      if (paramInt2 != 2038) {
        StatsLog.write(59, paramInt1, paramString, true, 2);
      } else if (!paramBoolean2) {
        StatsLog.write(59, paramInt1, paramString, false, 2);
      }
    }
  }
  
  public static void logPictureInPictureDismissByDrag(Context paramContext, Pair<ComponentName, Integer> paramPair)
  {
    MetricsLogger.action(paramContext, 822, 1);
    StatsLog.write(52, getUid(paramContext, (ComponentName)first, ((Integer)second).intValue()), ((ComponentName)first).flattenToString(), 4);
  }
  
  public static void logPictureInPictureDismissByTap(Context paramContext, Pair<ComponentName, Integer> paramPair)
  {
    MetricsLogger.action(paramContext, 822, 0);
    StatsLog.write(52, getUid(paramContext, (ComponentName)first, ((Integer)second).intValue()), ((ComponentName)first).flattenToString(), 4);
  }
  
  public static void logPictureInPictureEnter(Context paramContext, int paramInt, String paramString, boolean paramBoolean)
  {
    MetricsLogger.action(paramContext, 819, paramBoolean);
    StatsLog.write(52, paramInt, paramString, 1);
  }
  
  public static void logPictureInPictureFullScreen(Context paramContext, int paramInt, String paramString)
  {
    MetricsLogger.action(paramContext, 820);
    StatsLog.write(52, paramInt, paramString, 2);
  }
  
  public static void logPictureInPictureMenuVisible(Context paramContext, boolean paramBoolean)
  {
    MetricsLogger.visibility(paramContext, 823, paramBoolean);
  }
  
  public static void logPictureInPictureMinimize(Context paramContext, boolean paramBoolean, Pair<ComponentName, Integer> paramPair)
  {
    MetricsLogger.action(paramContext, 821, paramBoolean);
    StatsLog.write(52, getUid(paramContext, (ComponentName)first, ((Integer)second).intValue()), ((ComponentName)first).flattenToString(), 3);
  }
}
