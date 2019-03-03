package com.android.internal.telephony.uicc;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.Notification.Action;
import android.app.Notification.Action.Builder;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.Settings.Global;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@VisibleForTesting
public class InstallCarrierAppUtils
{
  private static final int ACTIVATE_CELL_SERVICE_NOTIFICATION_ID = 12;
  private static CarrierAppInstallReceiver sCarrierAppInstallReceiver = null;
  
  public InstallCarrierAppUtils() {}
  
  static String getAppNameFromPackageName(Context paramContext, String paramString)
  {
    return getAppNameFromPackageName(paramString, Settings.Global.getString(paramContext.getContentResolver(), "carrier_app_names"));
  }
  
  @VisibleForTesting
  public static String getAppNameFromPackageName(String paramString1, String paramString2)
  {
    paramString1 = paramString1.toLowerCase();
    if (TextUtils.isEmpty(paramString2)) {
      return null;
    }
    paramString2 = Arrays.asList(paramString2.split("\\s*;\\s*"));
    if (paramString2.isEmpty()) {
      return null;
    }
    Iterator localIterator = paramString2.iterator();
    while (localIterator.hasNext())
    {
      paramString2 = ((String)localIterator.next()).split("\\s*:\\s*");
      if ((paramString2.length == 2) && (paramString2[0].equals(paramString1))) {
        return paramString2[1];
      }
    }
    return null;
  }
  
  private static NotificationManager getNotificationManager(Context paramContext)
  {
    return (NotificationManager)paramContext.getSystemService("notification");
  }
  
  static Intent getPlayStoreIntent(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("market://details?id=");
    localStringBuilder.append(paramString);
    localIntent.setData(Uri.parse(localStringBuilder.toString()));
    localIntent.addFlags(268435456);
    return localIntent;
  }
  
  static void hideAllNotifications(Context paramContext)
  {
    paramContext = getNotificationManager(paramContext);
    StatusBarNotification[] arrayOfStatusBarNotification = paramContext.getActiveNotifications();
    if (arrayOfStatusBarNotification == null) {
      return;
    }
    int i = arrayOfStatusBarNotification.length;
    for (int j = 0; j < i; j++)
    {
      StatusBarNotification localStatusBarNotification = arrayOfStatusBarNotification[j];
      if (localStatusBarNotification.getId() == 12) {
        paramContext.cancel(localStatusBarNotification.getTag(), localStatusBarNotification.getId());
      }
    }
  }
  
  static void hideNotification(Context paramContext, String paramString)
  {
    getNotificationManager(paramContext).cancel(paramString, 12);
  }
  
  static boolean isPackageInstallNotificationActive(Context paramContext)
  {
    paramContext = getNotificationManager(paramContext).getActiveNotifications();
    int i = paramContext.length;
    for (int j = 0; j < i; j++) {
      if (paramContext[j].getId() == 12) {
        return true;
      }
    }
    return false;
  }
  
  static void registerPackageInstallReceiver(Context paramContext)
  {
    if (sCarrierAppInstallReceiver == null)
    {
      sCarrierAppInstallReceiver = new CarrierAppInstallReceiver();
      paramContext = paramContext.getApplicationContext();
      IntentFilter localIntentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
      localIntentFilter.addDataScheme("package");
      paramContext.registerReceiver(sCarrierAppInstallReceiver, localIntentFilter);
    }
  }
  
  static void showNotification(Context paramContext, String paramString)
  {
    Object localObject1 = Resources.getSystem();
    String str = ((Resources)localObject1).getString(17040140);
    Object localObject2 = getAppNameFromPackageName(paramContext, paramString);
    boolean bool1 = TextUtils.isEmpty((CharSequence)localObject2);
    boolean bool2 = true;
    if (bool1) {
      localObject2 = ((Resources)localObject1).getString(17040138);
    } else {
      localObject2 = ((Resources)localObject1).getString(17040139, new Object[] { localObject2 });
    }
    localObject1 = ((Resources)localObject1).getString(17040137);
    if (Settings.Global.getInt(paramContext.getContentResolver(), "install_carrier_app_notification_persistent", 1) != 1) {
      bool2 = false;
    }
    localObject1 = new Notification.Action.Builder(null, (CharSequence)localObject1, PendingIntent.getActivity(paramContext, 0, getPlayStoreIntent(paramString), 134217728)).build();
    localObject2 = new Notification.Builder(paramContext, "sim").setContentTitle(str).setContentText((CharSequence)localObject2).setSmallIcon(17303014).addAction((Notification.Action)localObject1).setOngoing(bool2).setVisibility(-1).build();
    getNotificationManager(paramContext).notify(paramString, 12, (Notification)localObject2);
  }
  
  static void showNotificationIfNotInstalledDelayed(Context paramContext, String paramString, long paramLong)
  {
    AlarmManager localAlarmManager = (AlarmManager)paramContext.getSystemService("alarm");
    paramContext = PendingIntent.getBroadcast(paramContext, 0, ShowInstallAppNotificationReceiver.get(paramContext, paramString), 0);
    localAlarmManager.set(3, SystemClock.elapsedRealtime() + paramLong, paramContext);
  }
  
  static void unregisterPackageInstallReceiver(Context paramContext)
  {
    if (sCarrierAppInstallReceiver == null) {
      return;
    }
    paramContext.getApplicationContext().unregisterReceiver(sCarrierAppInstallReceiver);
    sCarrierAppInstallReceiver = null;
  }
}
