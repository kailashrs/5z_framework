package com.android.internal.notification;

import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.media.AudioAttributes.Builder;
import android.os.RemoteException;
import android.provider.Settings.System;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemNotificationChannels
{
  public static String ACCOUNT;
  public static String ALERTS;
  public static String CAR_MODE;
  public static String DEVELOPER;
  public static String DEVICE_ADMIN;
  public static String DO_NOT_DISTURB = "DO_NOT_DISTURB";
  public static String FOREGROUND_SERVICE;
  public static String HEAVY_WEIGHT_APP;
  public static String HOTSPOT;
  public static String NETWORK_ALERTS;
  public static String NETWORK_AVAILABLE;
  public static String NETWORK_STATUS;
  public static String PHYSICAL_KEYBOARD;
  public static String RETAIL_MODE;
  public static String SECURITY;
  public static String SYSTEM_CHANGES;
  public static String UPDATES;
  public static String USB;
  public static String VIRTUAL_KEYBOARD = "VIRTUAL_KEYBOARD";
  public static String VPN;
  
  static
  {
    PHYSICAL_KEYBOARD = "PHYSICAL_KEYBOARD";
    SECURITY = "SECURITY";
    CAR_MODE = "CAR_MODE";
    ACCOUNT = "ACCOUNT";
    DEVELOPER = "DEVELOPER";
    UPDATES = "UPDATES";
    NETWORK_STATUS = "NETWORK_STATUS";
    NETWORK_ALERTS = "NETWORK_ALERTS";
    NETWORK_AVAILABLE = "NETWORK_AVAILABLE";
    VPN = "VPN";
    DEVICE_ADMIN = "DEVICE_ADMIN";
    ALERTS = "ALERTS";
    RETAIL_MODE = "RETAIL_MODE";
    USB = "USB";
    HOTSPOT = "HOTSPOT";
    FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    HEAVY_WEIGHT_APP = "HEAVY_WEIGHT_APP";
    SYSTEM_CHANGES = "SYSTEM_CHANGES";
  }
  
  private SystemNotificationChannels() {}
  
  public static void createAccountChannelForPackage(String paramString, int paramInt, Context paramContext)
  {
    INotificationManager localINotificationManager = NotificationManager.getService();
    try
    {
      ParceledListSlice localParceledListSlice = new android/content/pm/ParceledListSlice;
      localParceledListSlice.<init>(Arrays.asList(new NotificationChannel[] { newAccountChannel(paramContext) }));
      localINotificationManager.createNotificationChannelsForPackage(paramString, paramInt, localParceledListSlice);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public static void createAll(Context paramContext)
  {
    NotificationManager localNotificationManager = (NotificationManager)paramContext.getSystemService(NotificationManager.class);
    ArrayList localArrayList = new ArrayList();
    NotificationChannel localNotificationChannel = new NotificationChannel(VIRTUAL_KEYBOARD, paramContext.getString(17040507), 2);
    localNotificationChannel.setBlockableSystem(true);
    localArrayList.add(localNotificationChannel);
    localNotificationChannel = new NotificationChannel(PHYSICAL_KEYBOARD, paramContext.getString(17040499), 3);
    localNotificationChannel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, Notification.AUDIO_ATTRIBUTES_DEFAULT);
    localNotificationChannel.setBlockableSystem(true);
    localArrayList.add(localNotificationChannel);
    localArrayList.add(new NotificationChannel(SECURITY, paramContext.getString(17040501), 2));
    localNotificationChannel = new NotificationChannel(CAR_MODE, paramContext.getString(17040487), 2);
    localNotificationChannel.setBlockableSystem(true);
    localArrayList.add(localNotificationChannel);
    localArrayList.add(newAccountChannel(paramContext));
    localNotificationChannel = new NotificationChannel(DEVELOPER, paramContext.getString(17040488), 2);
    localNotificationChannel.setBlockableSystem(true);
    localArrayList.add(localNotificationChannel);
    localArrayList.add(new NotificationChannel(UPDATES, paramContext.getString(17040505), 2));
    localArrayList.add(new NotificationChannel(NETWORK_STATUS, paramContext.getString(17040498), 2));
    localNotificationChannel = new NotificationChannel(NETWORK_ALERTS, paramContext.getString(17040496), 4);
    localNotificationChannel.setBlockableSystem(true);
    localArrayList.add(localNotificationChannel);
    localNotificationChannel = new NotificationChannel(NETWORK_AVAILABLE, paramContext.getString(17040497), 2);
    localNotificationChannel.setBlockableSystem(true);
    localArrayList.add(localNotificationChannel);
    localArrayList.add(new NotificationChannel(VPN, paramContext.getString(17040509), 2));
    localArrayList.add(new NotificationChannel(DEVICE_ADMIN, paramContext.getString(17040489), 2));
    localArrayList.add(new NotificationChannel(ALERTS, paramContext.getString(17040485), 3));
    localArrayList.add(new NotificationChannel(RETAIL_MODE, paramContext.getString(17040500), 2));
    localArrayList.add(new NotificationChannel(USB, paramContext.getString(17040506), 1));
    localNotificationChannel = new NotificationChannel(FOREGROUND_SERVICE, paramContext.getString(17040492), 2);
    localNotificationChannel.setBlockableSystem(true);
    localArrayList.add(localNotificationChannel);
    localNotificationChannel = new NotificationChannel(HEAVY_WEIGHT_APP, paramContext.getString(17040493), 3);
    localNotificationChannel.setShowBadge(false);
    localNotificationChannel.setSound(null, new AudioAttributes.Builder().setContentType(4).setUsage(10).build());
    localArrayList.add(localNotificationChannel);
    localArrayList.add(new NotificationChannel(SYSTEM_CHANGES, paramContext.getString(17040504), 2));
    localArrayList.add(new NotificationChannel(DO_NOT_DISTURB, paramContext.getString(17040490), 2));
    localArrayList.add(new NotificationChannel(HOTSPOT, paramContext.getString(17040498), 1));
    localNotificationManager.createNotificationChannels(localArrayList);
  }
  
  private static NotificationChannel newAccountChannel(Context paramContext)
  {
    return new NotificationChannel(ACCOUNT, paramContext.getString(17040484), 2);
  }
}
