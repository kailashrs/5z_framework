package com.android.internal.telephony.util;

import android.app.NotificationChannel;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings.System;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class VoicemailNotificationSettingsUtil
{
  private static final String OLD_VOICEMAIL_NOTIFICATION_RINGTONE_SHARED_PREFS_KEY = "button_voicemail_notification_ringtone_key";
  private static final String OLD_VOICEMAIL_NOTIFICATION_VIBRATION_SHARED_PREFS_KEY = "button_voicemail_notification_vibrate_key";
  private static final String OLD_VOICEMAIL_RINGTONE_SHARED_PREFS_KEY = "button_voicemail_notification_ringtone_key";
  private static final String OLD_VOICEMAIL_VIBRATE_WHEN_SHARED_PREFS_KEY = "button_voicemail_notification_vibrate_when_key";
  private static final String OLD_VOICEMAIL_VIBRATION_ALWAYS = "always";
  private static final String OLD_VOICEMAIL_VIBRATION_NEVER = "never";
  private static final String VOICEMAIL_NOTIFICATION_RINGTONE_SHARED_PREFS_KEY_PREFIX = "voicemail_notification_ringtone_";
  private static final String VOICEMAIL_NOTIFICATION_VIBRATION_SHARED_PREFS_KEY_PREFIX = "voicemail_notification_vibrate_";
  
  public VoicemailNotificationSettingsUtil() {}
  
  public static Uri getRingTonePreference(Context paramContext)
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
    migrateVoicemailRingtoneSettingsIfNeeded(paramContext, localSharedPreferences);
    paramContext = localSharedPreferences.getString(getVoicemailRingtoneSharedPrefsKey(), Settings.System.DEFAULT_NOTIFICATION_URI.toString());
    if (!TextUtils.isEmpty(paramContext)) {
      paramContext = Uri.parse(paramContext);
    } else {
      paramContext = null;
    }
    return paramContext;
  }
  
  public static Uri getRingtoneUri(Context paramContext)
  {
    NotificationChannel localNotificationChannel = NotificationChannelController.getChannel("voiceMail", paramContext);
    if (localNotificationChannel != null) {
      paramContext = localNotificationChannel.getSound();
    } else {
      paramContext = getRingTonePreference(paramContext);
    }
    return paramContext;
  }
  
  public static boolean getVibrationPreference(Context paramContext)
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
    migrateVoicemailVibrationSettingsIfNeeded(paramContext, localSharedPreferences);
    return localSharedPreferences.getBoolean(getVoicemailVibrationSharedPrefsKey(), false);
  }
  
  private static String getVoicemailRingtoneSharedPrefsKey()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("voicemail_notification_ringtone_");
    localStringBuilder.append(SubscriptionManager.getDefaultSubscriptionId());
    return localStringBuilder.toString();
  }
  
  private static String getVoicemailVibrationSharedPrefsKey()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("voicemail_notification_vibrate_");
    localStringBuilder.append(SubscriptionManager.getDefaultSubscriptionId());
    return localStringBuilder.toString();
  }
  
  public static boolean isVibrationEnabled(Context paramContext)
  {
    NotificationChannel localNotificationChannel = NotificationChannelController.getChannel("voiceMail", paramContext);
    boolean bool;
    if (localNotificationChannel != null) {
      bool = localNotificationChannel.shouldVibrate();
    } else {
      bool = getVibrationPreference(paramContext);
    }
    return bool;
  }
  
  private static void migrateVoicemailRingtoneSettingsIfNeeded(Context paramContext, SharedPreferences paramSharedPreferences)
  {
    String str = getVoicemailRingtoneSharedPrefsKey();
    paramContext = TelephonyManager.from(paramContext);
    if ((!paramSharedPreferences.contains(str)) && (paramContext.getPhoneCount() == 1))
    {
      if (paramSharedPreferences.contains("button_voicemail_notification_ringtone_key"))
      {
        paramContext = paramSharedPreferences.getString("button_voicemail_notification_ringtone_key", null);
        paramSharedPreferences.edit().putString(str, paramContext).remove("button_voicemail_notification_ringtone_key").commit();
      }
      return;
    }
  }
  
  private static void migrateVoicemailVibrationSettingsIfNeeded(Context paramContext, SharedPreferences paramSharedPreferences)
  {
    String str = getVoicemailVibrationSharedPrefsKey();
    paramContext = TelephonyManager.from(paramContext);
    if ((!paramSharedPreferences.contains(str)) && (paramContext.getPhoneCount() == 1))
    {
      boolean bool;
      if (paramSharedPreferences.contains("button_voicemail_notification_vibrate_key"))
      {
        bool = paramSharedPreferences.getBoolean("button_voicemail_notification_vibrate_key", false);
        paramSharedPreferences.edit().putBoolean(str, bool).remove("button_voicemail_notification_vibrate_when_key").commit();
      }
      if (paramSharedPreferences.contains("button_voicemail_notification_vibrate_when_key"))
      {
        bool = paramSharedPreferences.getString("button_voicemail_notification_vibrate_when_key", "never").equals("always");
        paramSharedPreferences.edit().putBoolean(str, bool).remove("button_voicemail_notification_vibrate_key").commit();
      }
      return;
    }
  }
  
  public static void setRingtoneUri(Context paramContext, Uri paramUri)
  {
    SharedPreferences localSharedPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
    if (paramUri != null) {
      paramContext = paramUri.toString();
    } else {
      paramContext = "";
    }
    paramUri = localSharedPreferences.edit();
    paramUri.putString(getVoicemailRingtoneSharedPrefsKey(), paramContext);
    paramUri.commit();
  }
  
  public static void setVibrationEnabled(Context paramContext, boolean paramBoolean)
  {
    paramContext = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    paramContext.putBoolean(getVoicemailVibrationSharedPrefsKey(), paramBoolean);
    paramContext.commit();
  }
}
