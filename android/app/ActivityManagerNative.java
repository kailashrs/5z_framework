package android.app;

import android.content.Intent;
import android.os.IBinder;

@Deprecated
public abstract class ActivityManagerNative
{
  public ActivityManagerNative() {}
  
  public static IActivityManager asInterface(IBinder paramIBinder)
  {
    return IActivityManager.Stub.asInterface(paramIBinder);
  }
  
  public static void broadcastStickyIntent(Intent paramIntent, String paramString, int paramInt)
  {
    broadcastStickyIntent(paramIntent, paramString, -1, paramInt);
  }
  
  public static void broadcastStickyIntent(Intent paramIntent, String paramString, int paramInt1, int paramInt2)
  {
    ActivityManager.broadcastStickyIntent(paramIntent, paramInt1, paramInt2);
  }
  
  public static IActivityManager getDefault()
  {
    return ActivityManager.getService();
  }
  
  public static boolean isSystemReady()
  {
    return ActivityManager.isSystemReady();
  }
  
  public static void noteAlarmFinish(PendingIntent paramPendingIntent, int paramInt, String paramString)
  {
    ActivityManager.noteAlarmFinish(paramPendingIntent, null, paramInt, paramString);
  }
  
  public static void noteAlarmStart(PendingIntent paramPendingIntent, int paramInt, String paramString)
  {
    ActivityManager.noteAlarmStart(paramPendingIntent, null, paramInt, paramString);
  }
  
  public static void noteWakeupAlarm(PendingIntent paramPendingIntent, int paramInt, String paramString1, String paramString2)
  {
    ActivityManager.noteWakeupAlarm(paramPendingIntent, null, paramInt, paramString1, paramString2);
  }
}
