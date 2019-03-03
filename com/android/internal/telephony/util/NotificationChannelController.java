package com.android.internal.telephony.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes.Builder;
import android.net.Uri;
import android.provider.Settings.System;
import android.telephony.SubscriptionManager;
import java.util.Arrays;

public class NotificationChannelController
{
  public static final String CHANNEL_ID_ALERT = "alert";
  public static final String CHANNEL_ID_CALL_FORWARD = "callForward";
  private static final String CHANNEL_ID_MOBILE_DATA_ALERT_DEPRECATED = "mobileDataAlert";
  public static final String CHANNEL_ID_MOBILE_DATA_STATUS = "mobileDataAlertNew";
  public static final String CHANNEL_ID_SIM = "sim";
  public static final String CHANNEL_ID_SMS = "sms";
  public static final String CHANNEL_ID_VOICE_MAIL = "voiceMail";
  public static final String CHANNEL_ID_WFC = "wfc";
  private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ("android.intent.action.LOCALE_CHANGED".equals(paramAnonymousIntent.getAction())) {
        NotificationChannelController.createAll(paramAnonymousContext);
      } else if (("android.intent.action.SIM_STATE_CHANGED".equals(paramAnonymousIntent.getAction())) && (-1 != SubscriptionManager.getDefaultSubscriptionId())) {
        NotificationChannelController.migrateVoicemailNotificationSettings(paramAnonymousContext);
      }
    }
  };
  
  public NotificationChannelController(Context paramContext)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.LOCALE_CHANGED");
    localIntentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
    paramContext.registerReceiver(mBroadcastReceiver, localIntentFilter);
    createAll(paramContext);
  }
  
  private static void createAll(Context paramContext)
  {
    NotificationChannel localNotificationChannel1 = new NotificationChannel("alert", paramContext.getText(17040495), 3);
    localNotificationChannel1.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, new AudioAttributes.Builder().setUsage(5).build());
    localNotificationChannel1.setBlockableSystem(true);
    NotificationChannel localNotificationChannel2 = new NotificationChannel("mobileDataAlertNew", paramContext.getText(17040494), 2);
    localNotificationChannel2.setBlockableSystem(true);
    NotificationChannel localNotificationChannel3 = new NotificationChannel("sim", paramContext.getText(17040502), 2);
    localNotificationChannel3.setSound(null, null);
    ((NotificationManager)paramContext.getSystemService(NotificationManager.class)).createNotificationChannels(Arrays.asList(new NotificationChannel[] { new NotificationChannel("callForward", paramContext.getText(17040486), 2), new NotificationChannel("sms", paramContext.getText(17040503), 4), new NotificationChannel("wfc", paramContext.getText(17040510), 2), localNotificationChannel1, localNotificationChannel2, localNotificationChannel3 }));
    if (getChannel("voiceMail", paramContext) != null) {
      migrateVoicemailNotificationSettings(paramContext);
    }
    if (getChannel("mobileDataAlert", paramContext) != null) {
      ((NotificationManager)paramContext.getSystemService(NotificationManager.class)).deleteNotificationChannel("mobileDataAlert");
    }
  }
  
  public static NotificationChannel getChannel(String paramString, Context paramContext)
  {
    return ((NotificationManager)paramContext.getSystemService(NotificationManager.class)).getNotificationChannel(paramString);
  }
  
  private static void migrateVoicemailNotificationSettings(Context paramContext)
  {
    NotificationChannel localNotificationChannel = new NotificationChannel("voiceMail", paramContext.getText(17040508), 3);
    localNotificationChannel.enableVibration(VoicemailNotificationSettingsUtil.getVibrationPreference(paramContext));
    Uri localUri = VoicemailNotificationSettingsUtil.getRingTonePreference(paramContext);
    if (localUri == null) {
      localUri = Settings.System.DEFAULT_NOTIFICATION_URI;
    }
    localNotificationChannel.setSound(localUri, new AudioAttributes.Builder().setUsage(5).build());
    ((NotificationManager)paramContext.getSystemService(NotificationManager.class)).createNotificationChannel(localNotificationChannel);
  }
}
