package android.provider;

import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IContentProvider;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.media.AudioFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.AndroidException;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Log;
import android.util.MemoryIntArray;
import android.util.SeempLog;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.widget.ILockSettings;
import com.android.internal.widget.ILockSettings.Stub;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class Settings
{
  public static final String ACTION_ACCESSIBILITY_SETTINGS = "android.settings.ACCESSIBILITY_SETTINGS";
  public static final String ACTION_ADD_ACCOUNT = "android.settings.ADD_ACCOUNT_SETTINGS";
  public static final String ACTION_AIRPLANE_MODE_SETTINGS = "android.settings.AIRPLANE_MODE_SETTINGS";
  public static final String ACTION_ALL_APPS_NOTIFICATION_SETTINGS = "android.settings.ALL_APPS_NOTIFICATION_SETTINGS";
  public static final String ACTION_APN_SETTINGS = "android.settings.APN_SETTINGS";
  public static final String ACTION_APPLICATION_DETAILS_SETTINGS = "android.settings.APPLICATION_DETAILS_SETTINGS";
  public static final String ACTION_APPLICATION_DETAILS_SETTINGS_OPEN_BY_DEFAULT_PAGE = "android.settings.APPLICATION_DETAILS_SETTINGS_OPEN_BY_DEFAULT_PAGE";
  public static final String ACTION_APPLICATION_DEVELOPMENT_SETTINGS = "android.settings.APPLICATION_DEVELOPMENT_SETTINGS";
  public static final String ACTION_APPLICATION_SETTINGS = "android.settings.APPLICATION_SETTINGS";
  public static final String ACTION_APP_NOTIFICATION_REDACTION = "android.settings.ACTION_APP_NOTIFICATION_REDACTION";
  public static final String ACTION_APP_NOTIFICATION_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS";
  public static final String ACTION_APP_OPS_SETTINGS = "android.settings.APP_OPS_SETTINGS";
  public static final String ACTION_ASSIST_GESTURE_SETTINGS = "android.settings.ASSIST_GESTURE_SETTINGS";
  public static final String ACTION_BATTERY_SAVER_SETTINGS = "android.settings.BATTERY_SAVER_SETTINGS";
  public static final String ACTION_BLUETOOTH_SETTINGS = "android.settings.BLUETOOTH_SETTINGS";
  public static final String ACTION_CAPTIONING_SETTINGS = "android.settings.CAPTIONING_SETTINGS";
  public static final String ACTION_CAST_SETTINGS = "android.settings.CAST_SETTINGS";
  public static final String ACTION_CHANNEL_NOTIFICATION_SETTINGS = "android.settings.CHANNEL_NOTIFICATION_SETTINGS";
  public static final String ACTION_CONDITION_PROVIDER_SETTINGS = "android.settings.ACTION_CONDITION_PROVIDER_SETTINGS";
  public static final String ACTION_DATA_ROAMING_SETTINGS = "android.settings.DATA_ROAMING_SETTINGS";
  public static final String ACTION_DATA_USAGE_SETTINGS = "android.settings.DATA_USAGE_SETTINGS";
  public static final String ACTION_DATE_SETTINGS = "android.settings.DATE_SETTINGS";
  public static final String ACTION_DEVICE_INFO_SETTINGS = "android.settings.DEVICE_INFO_SETTINGS";
  public static final String ACTION_DISPLAY_SETTINGS = "android.settings.DISPLAY_SETTINGS";
  public static final String ACTION_DREAM_SETTINGS = "android.settings.DREAM_SETTINGS";
  @SystemApi
  public static final String ACTION_ENTERPRISE_PRIVACY_SETTINGS = "android.settings.ENTERPRISE_PRIVACY_SETTINGS";
  public static final String ACTION_FINGERPRINT_ENROLL = "android.settings.FINGERPRINT_ENROLL";
  public static final String ACTION_FOREGROUND_SERVICES_SETTINGS = "android.settings.FOREGROUND_SERVICES_SETTINGS";
  public static final String ACTION_HARD_KEYBOARD_SETTINGS = "android.settings.HARD_KEYBOARD_SETTINGS";
  public static final String ACTION_HOME_SETTINGS = "android.settings.HOME_SETTINGS";
  public static final String ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS = "android.settings.IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS";
  public static final String ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS = "android.settings.IGNORE_BATTERY_OPTIMIZATION_SETTINGS";
  public static final String ACTION_INPUT_METHOD_SETTINGS = "android.settings.INPUT_METHOD_SETTINGS";
  public static final String ACTION_INPUT_METHOD_SUBTYPE_SETTINGS = "android.settings.INPUT_METHOD_SUBTYPE_SETTINGS";
  public static final String ACTION_INTERNAL_STORAGE_SETTINGS = "android.settings.INTERNAL_STORAGE_SETTINGS";
  public static final String ACTION_LOCALE_SETTINGS = "android.settings.LOCALE_SETTINGS";
  public static final String ACTION_LOCATION_SCANNING_SETTINGS = "android.settings.LOCATION_SCANNING_SETTINGS";
  public static final String ACTION_LOCATION_SOURCE_SETTINGS = "android.settings.LOCATION_SOURCE_SETTINGS";
  public static final String ACTION_MANAGED_PROFILE_SETTINGS = "android.settings.MANAGED_PROFILE_SETTINGS";
  public static final String ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS = "android.settings.MANAGE_ALL_APPLICATIONS_SETTINGS";
  public static final String ACTION_MANAGE_APPLICATIONS_SETTINGS = "android.settings.MANAGE_APPLICATIONS_SETTINGS";
  public static final String ACTION_MANAGE_DEFAULT_APPS_SETTINGS = "android.settings.MANAGE_DEFAULT_APPS_SETTINGS";
  public static final String ACTION_MANAGE_OVERLAY_PERMISSION = "android.settings.action.MANAGE_OVERLAY_PERMISSION";
  public static final String ACTION_MANAGE_UNKNOWN_APP_SOURCES = "android.settings.MANAGE_UNKNOWN_APP_SOURCES";
  public static final String ACTION_MANAGE_WRITE_SETTINGS = "android.settings.action.MANAGE_WRITE_SETTINGS";
  public static final String ACTION_MEMORY_CARD_SETTINGS = "android.settings.MEMORY_CARD_SETTINGS";
  public static final String ACTION_MOBILE_DATA_USAGE = "android.settings.MOBILE_DATA_USAGE";
  public static final String ACTION_MONITORING_CERT_INFO = "com.android.settings.MONITORING_CERT_INFO";
  public static final String ACTION_NETWORK_OPERATOR_SETTINGS = "android.settings.NETWORK_OPERATOR_SETTINGS";
  public static final String ACTION_NFCSHARING_SETTINGS = "android.settings.NFCSHARING_SETTINGS";
  public static final String ACTION_NFC_PAYMENT_SETTINGS = "android.settings.NFC_PAYMENT_SETTINGS";
  public static final String ACTION_NFC_SETTINGS = "android.settings.NFC_SETTINGS";
  public static final String ACTION_NIGHT_DISPLAY_SETTINGS = "android.settings.NIGHT_DISPLAY_SETTINGS";
  public static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
  public static final String ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS = "android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS";
  public static final String ACTION_NOTIFICATION_SETTINGS = "android.settings.NOTIFICATION_SETTINGS";
  public static final String ACTION_PAIRING_SETTINGS = "android.settings.PAIRING_SETTINGS";
  public static final String ACTION_PICTURE_IN_PICTURE_SETTINGS = "android.settings.PICTURE_IN_PICTURE_SETTINGS";
  public static final String ACTION_PRINT_SETTINGS = "android.settings.ACTION_PRINT_SETTINGS";
  public static final String ACTION_PRIVACY_SETTINGS = "android.settings.PRIVACY_SETTINGS";
  public static final String ACTION_QUICK_LAUNCH_SETTINGS = "android.settings.QUICK_LAUNCH_SETTINGS";
  public static final String ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS = "android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS";
  public static final String ACTION_REQUEST_SET_AUTOFILL_SERVICE = "android.settings.REQUEST_SET_AUTOFILL_SERVICE";
  public static final String ACTION_SEARCH_SETTINGS = "android.search.action.SEARCH_SETTINGS";
  public static final String ACTION_SECURITY_SETTINGS = "android.settings.SECURITY_SETTINGS";
  public static final String ACTION_SETTINGS = "android.settings.SETTINGS";
  public static final String ACTION_SHOW_ADMIN_SUPPORT_DETAILS = "android.settings.SHOW_ADMIN_SUPPORT_DETAILS";
  public static final String ACTION_SHOW_REGULATORY_INFO = "android.settings.SHOW_REGULATORY_INFO";
  public static final String ACTION_SHOW_REMOTE_BUGREPORT_DIALOG = "android.settings.SHOW_REMOTE_BUGREPORT_DIALOG";
  public static final String ACTION_SOUND_SETTINGS = "android.settings.SOUND_SETTINGS";
  public static final String ACTION_STORAGE_MANAGER_SETTINGS = "android.settings.STORAGE_MANAGER_SETTINGS";
  public static final String ACTION_STORAGE_VOLUME_ACCESS_SETTINGS = "android.settings.STORAGE_VOLUME_ACCESS_SETTINGS";
  public static final String ACTION_SYNC_SETTINGS = "android.settings.SYNC_SETTINGS";
  public static final String ACTION_SYSTEM_UPDATE_SETTINGS = "android.settings.SYSTEM_UPDATE_SETTINGS";
  public static final String ACTION_TETHER_PROVISIONING = "android.settings.TETHER_PROVISIONING_UI";
  public static final String ACTION_TRUSTED_CREDENTIALS_USER = "com.android.settings.TRUSTED_CREDENTIALS_USER";
  public static final String ACTION_USAGE_ACCESS_SETTINGS = "android.settings.USAGE_ACCESS_SETTINGS";
  public static final String ACTION_USER_DICTIONARY_INSERT = "com.android.settings.USER_DICTIONARY_INSERT";
  public static final String ACTION_USER_DICTIONARY_SETTINGS = "android.settings.USER_DICTIONARY_SETTINGS";
  public static final String ACTION_USER_SETTINGS = "android.settings.USER_SETTINGS";
  public static final String ACTION_VOICE_CONTROL_AIRPLANE_MODE = "android.settings.VOICE_CONTROL_AIRPLANE_MODE";
  public static final String ACTION_VOICE_CONTROL_BATTERY_SAVER_MODE = "android.settings.VOICE_CONTROL_BATTERY_SAVER_MODE";
  public static final String ACTION_VOICE_CONTROL_DO_NOT_DISTURB_MODE = "android.settings.VOICE_CONTROL_DO_NOT_DISTURB_MODE";
  public static final String ACTION_VOICE_INPUT_SETTINGS = "android.settings.VOICE_INPUT_SETTINGS";
  public static final String ACTION_VPN_SETTINGS = "android.settings.VPN_SETTINGS";
  public static final String ACTION_VR_LISTENER_SETTINGS = "android.settings.VR_LISTENER_SETTINGS";
  public static final String ACTION_WEBVIEW_SETTINGS = "android.settings.WEBVIEW_SETTINGS";
  public static final String ACTION_WIFI_IP_SETTINGS = "android.settings.WIFI_IP_SETTINGS";
  public static final String ACTION_WIFI_SETTINGS = "android.settings.WIFI_SETTINGS";
  public static final String ACTION_WIRELESS_SETTINGS = "android.settings.WIRELESS_SETTINGS";
  public static final String ACTION_ZEN_MODE_AUTOMATION_SETTINGS = "android.settings.ZEN_MODE_AUTOMATION_SETTINGS";
  public static final String ACTION_ZEN_MODE_EVENT_RULE_SETTINGS = "android.settings.ZEN_MODE_EVENT_RULE_SETTINGS";
  public static final String ACTION_ZEN_MODE_EXTERNAL_RULE_SETTINGS = "android.settings.ZEN_MODE_EXTERNAL_RULE_SETTINGS";
  public static final String ACTION_ZEN_MODE_PRIORITY_SETTINGS = "android.settings.ZEN_MODE_PRIORITY_SETTINGS";
  public static final String ACTION_ZEN_MODE_SCHEDULE_RULE_SETTINGS = "android.settings.ZEN_MODE_SCHEDULE_RULE_SETTINGS";
  public static final String ACTION_ZEN_MODE_SETTINGS = "android.settings.ZEN_MODE_SETTINGS";
  public static final String AUTHORITY = "settings";
  public static final String CALL_METHOD_GENERATION_INDEX_KEY = "_generation_index";
  public static final String CALL_METHOD_GENERATION_KEY = "_generation";
  public static final String CALL_METHOD_GET_GLOBAL = "GET_global";
  public static final String CALL_METHOD_GET_SECURE = "GET_secure";
  public static final String CALL_METHOD_GET_SYSTEM = "GET_system";
  public static final String CALL_METHOD_MAKE_DEFAULT_KEY = "_make_default";
  public static final String CALL_METHOD_PUT_GLOBAL = "PUT_global";
  public static final String CALL_METHOD_PUT_SECURE = "PUT_secure";
  public static final String CALL_METHOD_PUT_SYSTEM = "PUT_system";
  public static final String CALL_METHOD_RESET_GLOBAL = "RESET_global";
  public static final String CALL_METHOD_RESET_MODE_KEY = "_reset_mode";
  public static final String CALL_METHOD_RESET_SECURE = "RESET_secure";
  public static final String CALL_METHOD_TAG_KEY = "_tag";
  public static final String CALL_METHOD_TRACK_GENERATION_KEY = "_track_generation";
  public static final String CALL_METHOD_USER_KEY = "_user";
  public static final String DEVICE_NAME_SETTINGS = "android.settings.DEVICE_NAME";
  public static final String EXTRA_ACCOUNT_TYPES = "account_types";
  public static final String EXTRA_AIRPLANE_MODE_ENABLED = "airplane_mode_enabled";
  public static final String EXTRA_APP_PACKAGE = "android.provider.extra.APP_PACKAGE";
  public static final String EXTRA_APP_UID = "app_uid";
  public static final String EXTRA_AUTHORITIES = "authorities";
  public static final String EXTRA_BATTERY_SAVER_MODE_ENABLED = "android.settings.extra.battery_saver_mode_enabled";
  public static final String EXTRA_CHANNEL_ID = "android.provider.extra.CHANNEL_ID";
  public static final String EXTRA_DO_NOT_DISTURB_MODE_ENABLED = "android.settings.extra.do_not_disturb_mode_enabled";
  public static final String EXTRA_DO_NOT_DISTURB_MODE_MINUTES = "android.settings.extra.do_not_disturb_mode_minutes";
  public static final String EXTRA_INPUT_DEVICE_IDENTIFIER = "input_device_identifier";
  public static final String EXTRA_INPUT_METHOD_ID = "input_method_id";
  public static final String EXTRA_NETWORK_TEMPLATE = "network_template";
  public static final String EXTRA_NUMBER_OF_CERTIFICATES = "android.settings.extra.number_of_certificates";
  public static final String EXTRA_SUB_ID = "android.provider.extra.SUB_ID";
  public static final String INTENT_CATEGORY_USAGE_ACCESS_CONFIG = "android.intent.category.USAGE_ACCESS_CONFIG";
  private static final String JID_RESOURCE_PREFIX = "android";
  private static final boolean LOCAL_LOGV = false;
  public static final String METADATA_USAGE_ACCESS_REASON = "android.settings.metadata.USAGE_ACCESS_REASON";
  private static final String[] PM_CHANGE_NETWORK_STATE = { "android.permission.CHANGE_NETWORK_STATE", "android.permission.WRITE_SETTINGS" };
  private static final String[] PM_SYSTEM_ALERT_WINDOW = { "android.permission.SYSTEM_ALERT_WINDOW" };
  private static final String[] PM_WRITE_SETTINGS;
  public static final int RESET_MODE_PACKAGE_DEFAULTS = 1;
  public static final int RESET_MODE_TRUSTED_DEFAULTS = 4;
  public static final int RESET_MODE_UNTRUSTED_CHANGES = 3;
  public static final int RESET_MODE_UNTRUSTED_DEFAULTS = 2;
  private static final String TAG = "Settings";
  public static final int USER_SETUP_PERSONALIZATION_COMPLETE = 10;
  public static final int USER_SETUP_PERSONALIZATION_NOT_STARTED = 0;
  public static final int USER_SETUP_PERSONALIZATION_PAUSED = 2;
  public static final int USER_SETUP_PERSONALIZATION_STARTED = 1;
  public static final String ZEN_MODE_BLOCKED_EFFECTS_SETTINGS = "android.settings.ZEN_MODE_BLOCKED_EFFECTS_SETTINGS";
  public static final String ZEN_MODE_ONBOARDING = "android.settings.ZEN_MODE_ONBOARDING";
  private static final Object mLocationSettingsLock = new Object();
  private static boolean sInSystemServer = false;
  private static final Object sInSystemServerLock = new Object();
  
  static
  {
    PM_WRITE_SETTINGS = new String[] { "android.permission.WRITE_SETTINGS" };
  }
  
  public Settings() {}
  
  public static boolean canDrawOverlays(Context paramContext)
  {
    return isCallingPackageAllowedToDrawOverlays(paramContext, Process.myUid(), paramContext.getOpPackageName(), false);
  }
  
  public static boolean checkAndNoteChangeNetworkStateOperation(Context paramContext, int paramInt, String paramString, boolean paramBoolean)
  {
    if (paramContext.checkCallingOrSelfPermission("android.permission.CHANGE_NETWORK_STATE") == 0) {
      return true;
    }
    return isCallingPackageAllowedToPerformAppOpsProtectedOperation(paramContext, paramInt, paramString, paramBoolean, 23, PM_CHANGE_NETWORK_STATE, true);
  }
  
  public static boolean checkAndNoteDrawOverlaysOperation(Context paramContext, int paramInt, String paramString, boolean paramBoolean)
  {
    return isCallingPackageAllowedToPerformAppOpsProtectedOperation(paramContext, paramInt, paramString, paramBoolean, 24, PM_SYSTEM_ALERT_WINDOW, true);
  }
  
  public static boolean checkAndNoteWriteSettingsOperation(Context paramContext, int paramInt, String paramString, boolean paramBoolean)
  {
    return isCallingPackageAllowedToPerformAppOpsProtectedOperation(paramContext, paramInt, paramString, paramBoolean, 23, PM_WRITE_SETTINGS, true);
  }
  
  public static String getGTalkDeviceId(long paramLong)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("android-");
    localStringBuilder.append(Long.toHexString(paramLong));
    return localStringBuilder.toString();
  }
  
  public static String getPackageNameForUid(Context paramContext, int paramInt)
  {
    paramContext = paramContext.getPackageManager().getPackagesForUid(paramInt);
    if (paramContext == null) {
      return null;
    }
    return paramContext[0];
  }
  
  public static boolean isCallingPackageAllowedToDrawOverlays(Context paramContext, int paramInt, String paramString, boolean paramBoolean)
  {
    return isCallingPackageAllowedToPerformAppOpsProtectedOperation(paramContext, paramInt, paramString, paramBoolean, 24, PM_SYSTEM_ALERT_WINDOW, false);
  }
  
  public static boolean isCallingPackageAllowedToPerformAppOpsProtectedOperation(Context paramContext, int paramInt1, String paramString, boolean paramBoolean1, int paramInt2, String[] paramArrayOfString, boolean paramBoolean2)
  {
    int i = 0;
    if (paramString == null) {
      return false;
    }
    Object localObject = (AppOpsManager)paramContext.getSystemService("appops");
    if (paramBoolean2) {
      paramInt1 = ((AppOpsManager)localObject).noteOpNoThrow(paramInt2, paramInt1, paramString);
    } else {
      paramInt1 = ((AppOpsManager)localObject).checkOpNoThrow(paramInt2, paramInt1, paramString);
    }
    if (paramInt1 != 0)
    {
      if (paramInt1 == 3)
      {
        paramInt2 = paramArrayOfString.length;
        for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
          if (paramContext.checkCallingOrSelfPermission(paramArrayOfString[paramInt1]) == 0) {
            return true;
          }
        }
      }
      if (!paramBoolean1) {
        return false;
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(paramString);
      ((StringBuilder)localObject).append(" was not granted ");
      if (paramArrayOfString.length > 1) {
        ((StringBuilder)localObject).append(" either of these permissions: ");
      } else {
        ((StringBuilder)localObject).append(" this permission: ");
      }
      for (paramInt1 = i; paramInt1 < paramArrayOfString.length; paramInt1++)
      {
        ((StringBuilder)localObject).append(paramArrayOfString[paramInt1]);
        if (paramInt1 == paramArrayOfString.length - 1) {
          paramContext = ".";
        } else {
          paramContext = ", ";
        }
        ((StringBuilder)localObject).append(paramContext);
      }
      throw new SecurityException(((StringBuilder)localObject).toString());
    }
    return true;
  }
  
  public static boolean isCallingPackageAllowedToWriteSettings(Context paramContext, int paramInt, String paramString, boolean paramBoolean)
  {
    return isCallingPackageAllowedToPerformAppOpsProtectedOperation(paramContext, paramInt, paramString, paramBoolean, 23, PM_WRITE_SETTINGS, false);
  }
  
  public static boolean isInSystemServer()
  {
    synchronized (sInSystemServerLock)
    {
      boolean bool = sInSystemServer;
      return bool;
    }
  }
  
  public static void setInSystemServer()
  {
    synchronized (sInSystemServerLock)
    {
      sInSystemServer = true;
      return;
    }
  }
  
  public static final class Bookmarks
    implements BaseColumns
  {
    public static final Uri CONTENT_URI = Uri.parse("content://settings/bookmarks");
    public static final String FOLDER = "folder";
    public static final String ID = "_id";
    public static final String INTENT = "intent";
    public static final String ORDERING = "ordering";
    public static final String SHORTCUT = "shortcut";
    private static final String TAG = "Bookmarks";
    public static final String TITLE = "title";
    private static final String[] sIntentProjection = { "intent" };
    private static final String[] sShortcutProjection = { "_id", "shortcut" };
    private static final String sShortcutSelection = "shortcut=?";
    
    public Bookmarks() {}
    
    public static Uri add(ContentResolver paramContentResolver, Intent paramIntent, String paramString1, String paramString2, char paramChar, int paramInt)
    {
      if (paramChar != 0) {
        paramContentResolver.delete(CONTENT_URI, "shortcut=?", new String[] { String.valueOf(paramChar) });
      }
      ContentValues localContentValues = new ContentValues();
      if (paramString1 != null) {
        localContentValues.put("title", paramString1);
      }
      if (paramString2 != null) {
        localContentValues.put("folder", paramString2);
      }
      localContentValues.put("intent", paramIntent.toUri(0));
      if (paramChar != 0) {
        localContentValues.put("shortcut", Integer.valueOf(paramChar));
      }
      localContentValues.put("ordering", Integer.valueOf(paramInt));
      return paramContentResolver.insert(CONTENT_URI, localContentValues);
    }
    
    public static Intent getIntentForShortcut(ContentResolver paramContentResolver, char paramChar)
    {
      Intent localIntent = null;
      Cursor localCursor = paramContentResolver.query(CONTENT_URI, sIntentProjection, "shortcut=?", new String[] { String.valueOf(paramChar) }, "ordering");
      paramContentResolver = localIntent;
      while (paramContentResolver == null) {
        try
        {
          boolean bool = localCursor.moveToNext();
          if (bool) {
            try
            {
              localIntent = Intent.parseUri(localCursor.getString(localCursor.getColumnIndexOrThrow("intent")), 0);
              paramContentResolver = localIntent;
            }
            catch (IllegalArgumentException localIllegalArgumentException)
            {
              for (;;)
              {
                Log.w("Bookmarks", "Intent column not found", localIllegalArgumentException);
              }
            }
            catch (URISyntaxException localURISyntaxException)
            {
              for (;;) {}
            }
          } else if (localCursor == null) {
            return paramContentResolver;
          }
        }
        finally
        {
          if (localCursor != null) {
            localCursor.close();
          }
        }
      }
      localCursor.close();
      return paramContentResolver;
    }
    
    public static CharSequence getLabelForFolder(Resources paramResources, String paramString)
    {
      return paramString;
    }
    
    public static CharSequence getTitle(Context paramContext, Cursor paramCursor)
    {
      int i = paramCursor.getColumnIndex("title");
      int j = paramCursor.getColumnIndex("intent");
      if ((i != -1) && (j != -1))
      {
        String str = paramCursor.getString(i);
        if (!TextUtils.isEmpty(str)) {
          return str;
        }
        paramCursor = paramCursor.getString(j);
        if (TextUtils.isEmpty(paramCursor)) {
          return "";
        }
        try
        {
          paramCursor = Intent.parseUri(paramCursor, 0);
          paramContext = paramContext.getPackageManager();
          paramCursor = paramContext.resolveActivity(paramCursor, 0);
          if (paramCursor != null) {
            paramContext = paramCursor.loadLabel(paramContext);
          } else {
            paramContext = "";
          }
          return paramContext;
        }
        catch (URISyntaxException paramContext)
        {
          return "";
        }
      }
      throw new IllegalArgumentException("The cursor must contain the TITLE and INTENT columns.");
    }
  }
  
  private static final class ContentProviderHolder
  {
    @GuardedBy("mLock")
    private IContentProvider mContentProvider;
    private final Object mLock = new Object();
    @GuardedBy("mLock")
    private final Uri mUri;
    
    public ContentProviderHolder(Uri paramUri)
    {
      mUri = paramUri;
    }
    
    public void clearProviderForTest()
    {
      synchronized (mLock)
      {
        mContentProvider = null;
        return;
      }
    }
    
    public IContentProvider getProvider(ContentResolver paramContentResolver)
    {
      synchronized (mLock)
      {
        if (mContentProvider == null) {
          mContentProvider = paramContentResolver.acquireProvider(mUri.getAuthority());
        }
        paramContentResolver = mContentProvider;
        return paramContentResolver;
      }
    }
  }
  
  private static final class GenerationTracker
  {
    private final MemoryIntArray mArray;
    private int mCurrentGeneration;
    private final Runnable mErrorHandler;
    private final int mIndex;
    
    public GenerationTracker(MemoryIntArray paramMemoryIntArray, int paramInt1, int paramInt2, Runnable paramRunnable)
    {
      mArray = paramMemoryIntArray;
      mIndex = paramInt1;
      mErrorHandler = paramRunnable;
      mCurrentGeneration = paramInt2;
    }
    
    private int readCurrentGeneration()
    {
      try
      {
        int i = mArray.get(mIndex);
        return i;
      }
      catch (IOException localIOException)
      {
        Log.e("Settings", "Error getting current generation", localIOException);
        if (mErrorHandler != null) {
          mErrorHandler.run();
        }
      }
      return -1;
    }
    
    public void destroy()
    {
      try
      {
        mArray.close();
      }
      catch (IOException localIOException)
      {
        Log.e("Settings", "Error closing backing array", localIOException);
        if (mErrorHandler != null) {
          mErrorHandler.run();
        }
      }
    }
    
    public int getCurrentGeneration()
    {
      return mCurrentGeneration;
    }
    
    public boolean isGenerationChanged()
    {
      int i = readCurrentGeneration();
      if (i >= 0)
      {
        if (i == mCurrentGeneration) {
          return false;
        }
        mCurrentGeneration = i;
      }
      return true;
    }
  }
  
  public static final class Global
    extends Settings.NameValueTable
  {
    public static final String ACTIVITY_MANAGER_CONSTANTS = "activity_manager_constants";
    public static final String ADAPTIVE_BATTERY_MANAGEMENT_ENABLED = "adaptive_battery_management_enabled";
    public static final String ADB_ENABLED = "adb_enabled";
    public static final String ADD_USERS_WHEN_LOCKED = "add_users_when_locked";
    public static final String AIRPLANE_MODE_ON = "airplane_mode_on";
    public static final String AIRPLANE_MODE_RADIOS = "airplane_mode_radios";
    public static final String AIRPLANE_MODE_TOGGLEABLE_RADIOS = "airplane_mode_toggleable_radios";
    public static final String ALARM_MANAGER_CONSTANTS = "alarm_manager_constants";
    public static final String ALLOW_USER_SWITCHING_WHEN_SYSTEM_USER_LOCKED = "allow_user_switching_when_system_user_locked";
    public static final String ALWAYS_FINISH_ACTIVITIES = "always_finish_activities";
    public static final String ALWAYS_ON_DISPLAY_CONSTANTS = "always_on_display_constants";
    public static final String ANIMATOR_DURATION_SCALE = "animator_duration_scale";
    public static final String ANOMALY_CONFIG = "anomaly_config";
    public static final String ANOMALY_CONFIG_VERSION = "anomaly_config_version";
    public static final String ANOMALY_DETECTION_CONSTANTS = "anomaly_detection_constants";
    public static final String APN_DB_UPDATE_CONTENT_URL = "apn_db_content_url";
    public static final String APN_DB_UPDATE_METADATA_URL = "apn_db_metadata_url";
    public static final String APP_AUTO_RESTRICTION_ENABLED = "app_auto_restriction_enabled";
    private static final SettingsValidators.Validator APP_AUTO_RESTRICTION_ENABLED_VALIDATOR;
    public static final String APP_IDLE_CONSTANTS = "app_idle_constants";
    public static final String APP_OPS_CONSTANTS = "app_ops_constants";
    public static final String APP_STANDBY_ENABLED = "app_standby_enabled";
    public static final String ASSISTED_GPS_ENABLED = "assisted_gps_enabled";
    public static final String ASUS_FINGERPRINT_ANSWER_CALL = "asus_fingerprint_id_answer_call";
    public static final String ASUS_FINGERPRINT_LONG_PRESS = "asus_fingerprint_long_press";
    public static final String ASUS_FINGERPRINT_SELFIE_CAMERA = "asus_fingerprint_selfie_camera";
    public static final String ASUS_FINGERPRINT_SNAP_CALL = "asus_fingerprint_id_snap_call";
    public static final String ASUS_FINGERPRINT_TAKE_PHOTO = "asus_fingerprint_take_photo";
    public static final String ASUS_GAMEMODE = "asus_gamemode";
    public static final String AUDIO_SAFE_VOLUME_STATE = "audio_safe_volume_state";
    @SystemApi
    public static final String AUTOFILL_COMPAT_MODE_ALLOWED_PACKAGES = "autofill_compat_mode_allowed_packages";
    public static final String AUTO_CLEAN_MEMORY = "auto_clean_memory";
    public static final String AUTO_KILL_DRAIN_APPS = "auto_kill_drain_apps";
    public static final String AUTO_TIME = "auto_time";
    private static final SettingsValidators.Validator AUTO_TIME_VALIDATOR;
    public static final String AUTO_TIME_ZONE = "auto_time_zone";
    private static final SettingsValidators.Validator AUTO_TIME_ZONE_VALIDATOR;
    public static final String BACKUP_AGENT_TIMEOUT_PARAMETERS = "backup_agent_timeout_parameters";
    public static final String BATTERY_DISCHARGE_DURATION_THRESHOLD = "battery_discharge_duration_threshold";
    public static final String BATTERY_DISCHARGE_THRESHOLD = "battery_discharge_threshold";
    public static final String BATTERY_SAVER_CONSTANTS = "battery_saver_constants";
    public static final String BATTERY_SAVER_DEVICE_SPECIFIC_CONSTANTS = "battery_saver_device_specific_constants";
    public static final String BATTERY_STATS_CONSTANTS = "battery_stats_constants";
    public static final String BATTERY_TIP_CONSTANTS = "battery_tip_constants";
    public static final String BLE_SCAN_ALWAYS_AVAILABLE = "ble_scan_always_enabled";
    public static final String BLE_SCAN_BACKGROUND_MODE = "ble_scan_background_mode";
    public static final String BLE_SCAN_BALANCED_INTERVAL_MS = "ble_scan_balanced_interval_ms";
    public static final String BLE_SCAN_BALANCED_WINDOW_MS = "ble_scan_balanced_window_ms";
    public static final String BLE_SCAN_LOW_LATENCY_INTERVAL_MS = "ble_scan_low_latency_interval_ms";
    public static final String BLE_SCAN_LOW_LATENCY_WINDOW_MS = "ble_scan_low_latency_window_ms";
    public static final String BLE_SCAN_LOW_POWER_INTERVAL_MS = "ble_scan_low_power_interval_ms";
    public static final String BLE_SCAN_LOW_POWER_WINDOW_MS = "ble_scan_low_power_window_ms";
    public static final String BLOCKED_SLICES = "blocked_slices";
    public static final String BLOCKING_HELPER_DISMISS_TO_VIEW_RATIO_LIMIT = "blocking_helper_dismiss_to_view_ratio";
    public static final String BLOCKING_HELPER_STREAK_LIMIT = "blocking_helper_streak_limit";
    public static final String BLUETOOTH_A2DP_OPTIONAL_CODECS_ENABLED_PREFIX = "bluetooth_a2dp_optional_codecs_enabled_";
    public static final String BLUETOOTH_A2DP_SINK_PRIORITY_PREFIX = "bluetooth_a2dp_sink_priority_";
    public static final String BLUETOOTH_A2DP_SRC_PRIORITY_PREFIX = "bluetooth_a2dp_src_priority_";
    public static final String BLUETOOTH_A2DP_SUPPORTS_OPTIONAL_CODECS_PREFIX = "bluetooth_a2dp_supports_optional_codecs_";
    public static final String BLUETOOTH_CLASS_OF_DEVICE = "bluetooth_class_of_device";
    public static final String BLUETOOTH_DISABLED_PROFILES = "bluetooth_disabled_profiles";
    public static final String BLUETOOTH_HEADSET_PRIORITY_PREFIX = "bluetooth_headset_priority_";
    public static final String BLUETOOTH_HEARING_AID_PRIORITY_PREFIX = "bluetooth_hearing_aid_priority_";
    public static final String BLUETOOTH_INPUT_DEVICE_PRIORITY_PREFIX = "bluetooth_input_device_priority_";
    public static final String BLUETOOTH_INTEROPERABILITY_LIST = "bluetooth_interoperability_list";
    public static final String BLUETOOTH_MAP_CLIENT_PRIORITY_PREFIX = "bluetooth_map_client_priority_";
    public static final String BLUETOOTH_MAP_PRIORITY_PREFIX = "bluetooth_map_priority_";
    public static final String BLUETOOTH_ON = "bluetooth_on";
    private static final SettingsValidators.Validator BLUETOOTH_ON_VALIDATOR;
    public static final String BLUETOOTH_PAN_PRIORITY_PREFIX = "bluetooth_pan_priority_";
    public static final String BLUETOOTH_PBAP_CLIENT_PRIORITY_PREFIX = "bluetooth_pbap_client_priority_";
    public static final String BLUETOOTH_SAP_PRIORITY_PREFIX = "bluetooth_sap_priority_";
    public static final String BOOT_COUNT = "boot_count";
    public static final String BUGREPORT_IN_POWER_MENU = "bugreport_in_power_menu";
    private static final SettingsValidators.Validator BUGREPORT_IN_POWER_MENU_VALIDATOR;
    public static final String CALL_AUTO_RETRY = "call_auto_retry";
    private static final SettingsValidators.Validator CALL_AUTO_RETRY_VALIDATOR;
    @Deprecated
    public static final String CAPTIVE_PORTAL_DETECTION_ENABLED = "captive_portal_detection_enabled";
    public static final String CAPTIVE_PORTAL_FALLBACK_PROBE_SPECS = "captive_portal_fallback_probe_specs";
    public static final String CAPTIVE_PORTAL_FALLBACK_URL = "captive_portal_fallback_url";
    public static final String CAPTIVE_PORTAL_HTTPS_URL = "captive_portal_https_url";
    public static final String CAPTIVE_PORTAL_HTTP_URL = "captive_portal_http_url";
    public static final String CAPTIVE_PORTAL_MODE = "captive_portal_mode";
    public static final int CAPTIVE_PORTAL_MODE_AVOID = 2;
    public static final int CAPTIVE_PORTAL_MODE_IGNORE = 0;
    public static final int CAPTIVE_PORTAL_MODE_PROMPT = 1;
    public static final String CAPTIVE_PORTAL_OTHER_FALLBACK_URLS = "captive_portal_other_fallback_urls";
    public static final String CAPTIVE_PORTAL_SERVER = "captive_portal_server";
    public static final String CAPTIVE_PORTAL_USER_AGENT = "captive_portal_user_agent";
    public static final String CAPTIVE_PORTAL_USE_HTTPS = "captive_portal_use_https";
    @SystemApi
    public static final String CARRIER_APP_NAMES = "carrier_app_names";
    @SystemApi
    public static final String CARRIER_APP_WHITELIST = "carrier_app_whitelist";
    public static final String CAR_DOCK_SOUND = "car_dock_sound";
    public static final String CAR_UNDOCK_SOUND = "car_undock_sound";
    public static final String CDMA_CELL_BROADCAST_SMS = "cdma_cell_broadcast_sms";
    public static final String CDMA_ROAMING_MODE = "roaming_settings";
    public static final String CDMA_SUBSCRIPTION_MODE = "subscription_mode";
    public static final String CELL_ON = "cell_on";
    public static final String CERT_PIN_UPDATE_CONTENT_URL = "cert_pin_content_url";
    public static final String CERT_PIN_UPDATE_METADATA_URL = "cert_pin_metadata_url";
    public static final String CHAINED_BATTERY_ATTRIBUTION_ENABLED = "chained_battery_attribution_enabled";
    public static final String CHARGING_SOUNDS_ENABLED = "charging_sounds_enabled";
    private static final SettingsValidators.Validator CHARGING_SOUNDS_ENABLED_VALIDATOR;
    public static final String CHARGING_STARTED_SOUND = "wireless_charging_started_sound";
    public static final String CHARGING_VIBRATION_ENABLED = "charging_vibration_enabled";
    private static final SettingsValidators.Validator CHARGING_VIBRATION_ENABLED_VALIDATOR;
    public static final String COMPATIBILITY_MODE = "compatibility_mode";
    public static final String CONNECTIVITY_CHANGE_DELAY = "connectivity_change_delay";
    public static final String CONNECTIVITY_METRICS_BUFFER_SIZE = "connectivity_metrics_buffer_size";
    public static final String CONNECTIVITY_SAMPLING_INTERVAL_IN_SECONDS = "connectivity_sampling_interval_in_seconds";
    public static final String CONTACTS_DATABASE_WAL_ENABLED = "contacts_database_wal_enabled";
    @Deprecated
    public static final String CONTACT_METADATA_SYNC = "contact_metadata_sync";
    public static final String CONTACT_METADATA_SYNC_ENABLED = "contact_metadata_sync_enabled";
    public static final Uri CONTENT_URI = Uri.parse("content://settings/global");
    public static final String CPU_SCALING_ENABLED = "cpu_frequency_scaling_enabled";
    public static final String DATABASE_CREATION_BUILDID = "database_creation_buildid";
    public static final String DATABASE_DOWNGRADE_REASON = "database_downgrade_reason";
    public static final String DATA_ACTIVITY_TIMEOUT_MOBILE = "data_activity_timeout_mobile";
    public static final String DATA_ACTIVITY_TIMEOUT_WIFI = "data_activity_timeout_wifi";
    public static final String DATA_ROAMING = "data_roaming";
    public static final String DATA_STALL_ALARM_AGGRESSIVE_DELAY_IN_MS = "data_stall_alarm_aggressive_delay_in_ms";
    public static final String DATA_STALL_ALARM_NON_AGGRESSIVE_DELAY_IN_MS = "data_stall_alarm_non_aggressive_delay_in_ms";
    public static final String DEBUG_APP = "debug_app";
    public static final String DEBUG_VIEW_ATTRIBUTES = "debug_view_attributes";
    public static final String DEFAULT_DNS_SERVER = "default_dns_server";
    public static final String DEFAULT_INSTALL_LOCATION = "default_install_location";
    public static final String DEFAULT_RESTRICT_BACKGROUND_DATA = "default_restrict_background_data";
    @SystemApi
    public static final String DEFAULT_SM_DP_PLUS = "default_sm_dp_plus";
    public static final String DESK_DOCK_SOUND = "desk_dock_sound";
    public static final String DESK_UNDOCK_SOUND = "desk_undock_sound";
    public static final String DEVELOPMENT_ENABLE_FREEFORM_WINDOWS_SUPPORT = "enable_freeform_support";
    public static final String DEVELOPMENT_FORCE_RESIZABLE_ACTIVITIES = "force_resizable_activities";
    public static final String DEVELOPMENT_FORCE_RTL = "debug.force_rtl";
    public static final String DEVELOPMENT_SETTINGS_ENABLED = "development_settings_enabled";
    public static final String DEVICE_DEMO_MODE = "device_demo_mode";
    public static final String DEVICE_IDLE_CONSTANTS = "device_idle_constants";
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_POLICY_CONSTANTS = "device_policy_constants";
    public static final String DEVICE_PROVISIONED = "device_provisioned";
    public static final String DEVICE_PROVISIONING_MOBILE_DATA_ENABLED = "device_provisioning_mobile_data";
    public static final String DISK_FREE_CHANGE_REPORTING_THRESHOLD = "disk_free_change_reporting_threshold";
    public static final String DISPLAY_PANEL_LPM = "display_panel_lpm";
    public static final String DISPLAY_SCALING_FORCE = "display_scaling_force";
    public static final String DISPLAY_SIZE_FORCED = "display_size_forced";
    public static final String DNS_RESOLVER_MAX_SAMPLES = "dns_resolver_max_samples";
    public static final String DNS_RESOLVER_MIN_SAMPLES = "dns_resolver_min_samples";
    public static final String DNS_RESOLVER_SAMPLE_VALIDITY_SECONDS = "dns_resolver_sample_validity_seconds";
    public static final String DNS_RESOLVER_SUCCESS_THRESHOLD_PERCENT = "dns_resolver_success_threshold_percent";
    public static final String DOCK_AUDIO_MEDIA_ENABLED = "dock_audio_media_enabled";
    private static final SettingsValidators.Validator DOCK_AUDIO_MEDIA_ENABLED_VALIDATOR;
    public static final String DOCK_SOUNDS_ENABLED = "dock_sounds_enabled";
    private static final SettingsValidators.Validator DOCK_SOUNDS_ENABLED_VALIDATOR;
    public static final String DOCK_SOUNDS_ENABLED_WHEN_ACCESSIBILITY = "dock_sounds_enabled_when_accessbility";
    public static final String DOWNLOAD_MAX_BYTES_OVER_MOBILE = "download_manager_max_bytes_over_mobile";
    public static final String DOWNLOAD_RECOMMENDED_MAX_BYTES_OVER_MOBILE = "download_manager_recommended_max_bytes_over_mobile";
    public static final String DROPBOX_AGE_SECONDS = "dropbox_age_seconds";
    public static final String DROPBOX_MAX_FILES = "dropbox_max_files";
    public static final String DROPBOX_QUOTA_KB = "dropbox_quota_kb";
    public static final String DROPBOX_QUOTA_PERCENT = "dropbox_quota_percent";
    public static final String DROPBOX_RESERVE_PERCENT = "dropbox_reserve_percent";
    public static final String DROPBOX_TAG_PREFIX = "dropbox:";
    public static final String EMERGENCY_AFFORDANCE_NEEDED = "emergency_affordance_needed";
    public static final String EMERGENCY_TONE = "emergency_tone";
    private static final SettingsValidators.Validator EMERGENCY_TONE_VALIDATOR;
    public static final String EMULATE_DISPLAY_CUTOUT = "emulate_display_cutout";
    public static final int EMULATE_DISPLAY_CUTOUT_OFF = 0;
    public static final int EMULATE_DISPLAY_CUTOUT_ON = 1;
    public static final String ENABLE_ACCESSIBILITY_GLOBAL_GESTURE_ENABLED = "enable_accessibility_global_gesture_enabled";
    public static final String ENABLE_CACHE_QUOTA_CALCULATION = "enable_cache_quota_calculation";
    public static final String ENABLE_CELLULAR_ON_BOOT = "enable_cellular_on_boot";
    public static final String ENABLE_DELETION_HELPER_NO_THRESHOLD_TOGGLE = "enable_deletion_helper_no_threshold_toggle";
    public static final String ENABLE_DISKSTATS_LOGGING = "enable_diskstats_logging";
    public static final String ENABLE_EPHEMERAL_FEATURE = "enable_ephemeral_feature";
    public static final String ENABLE_GNSS_RAW_MEAS_FULL_TRACKING = "enable_gnss_raw_meas_full_tracking";
    public static final String ENABLE_GPU_DEBUG_LAYERS = "enable_gpu_debug_layers";
    public static final String ENCODED_SURROUND_OUTPUT = "encoded_surround_output";
    public static final int ENCODED_SURROUND_OUTPUT_ALWAYS = 2;
    public static final int ENCODED_SURROUND_OUTPUT_AUTO = 0;
    public static final String ENCODED_SURROUND_OUTPUT_ENABLED_FORMATS = "encoded_surround_output_enabled_formats";
    private static final SettingsValidators.Validator ENCODED_SURROUND_OUTPUT_ENABLED_FORMATS_VALIDATOR;
    public static final int ENCODED_SURROUND_OUTPUT_MANUAL = 3;
    public static final int ENCODED_SURROUND_OUTPUT_NEVER = 1;
    private static final SettingsValidators.Validator ENCODED_SURROUND_OUTPUT_VALIDATOR;
    @Deprecated
    public static final String ENHANCED_4G_MODE_ENABLED = "volte_vt_enabled";
    public static final String EPHEMERAL_COOKIE_MAX_SIZE_BYTES = "ephemeral_cookie_max_size_bytes";
    public static final String ERROR_LOGCAT_PREFIX = "logcat_for_";
    public static final String EUICC_FACTORY_RESET_TIMEOUT_MILLIS = "euicc_factory_reset_timeout_millis";
    @SystemApi
    public static final String EUICC_PROVISIONED = "euicc_provisioned";
    public static final String EUICC_SUPPORTED_COUNTRIES = "euicc_supported_countries";
    public static final String FANCY_IME_ANIMATIONS = "fancy_ime_animations";
    public static final String FINGERPRINT_LOCKOUT_RESET_TIME = "fingerprint_lockout_reset_time";
    public static final String FLIPFONT_SETTINGS_NAME = "flipfont_settings_name";
    public static final String FLIPFONT_SETTINGS_PATH = "flipfont_settings_path";
    public static final String FORCED_APP_STANDBY_ENABLED = "forced_app_standby_enabled";
    public static final String FORCED_APP_STANDBY_FOR_SMALL_BATTERY_ENABLED = "forced_app_standby_for_small_battery_enabled";
    public static final String FORCE_ALLOW_ON_EXTERNAL = "force_allow_on_external";
    public static final String FPS_DEVISOR = "fps_divisor";
    public static final String FSTRIM_MANDATORY_INTERVAL = "fstrim_mandatory_interval";
    public static final String GAME_DND_LOCK = "game_dnd_lock";
    public static final String GLOBAL_HTTP_PROXY_EXCLUSION_LIST = "global_http_proxy_exclusion_list";
    public static final String GLOBAL_HTTP_PROXY_HOST = "global_http_proxy_host";
    public static final String GLOBAL_HTTP_PROXY_PAC = "global_proxy_pac_url";
    public static final String GLOBAL_HTTP_PROXY_PORT = "global_http_proxy_port";
    public static final String GNSS_HAL_LOCATION_REQUEST_DURATION_MILLIS = "gnss_hal_location_request_duration_millis";
    public static final String GNSS_SATELLITE_BLACKLIST = "gnss_satellite_blacklist";
    public static final String GPRS_REGISTER_CHECK_PERIOD_MS = "gprs_register_check_period_ms";
    public static final String GPU_DEBUG_APP = "gpu_debug_app";
    public static final String GPU_DEBUG_LAYERS = "gpu_debug_layers";
    public static final String HDMI_CONTROL_AUTO_DEVICE_OFF_ENABLED = "hdmi_control_auto_device_off_enabled";
    public static final String HDMI_CONTROL_AUTO_WAKEUP_ENABLED = "hdmi_control_auto_wakeup_enabled";
    public static final String HDMI_CONTROL_ENABLED = "hdmi_control_enabled";
    public static final String HDMI_SYSTEM_AUDIO_CONTROL_ENABLED = "hdmi_system_audio_control_enabled";
    public static final String HEADS_UP_NOTIFICATIONS_ENABLED = "heads_up_notifications_enabled";
    public static final int HEADS_UP_OFF = 0;
    public static final int HEADS_UP_ON = 1;
    public static final String HIDDEN_API_ACCESS_LOG_SAMPLING_RATE = "hidden_api_access_log_sampling_rate";
    public static final String HIDDEN_API_BLACKLIST_EXEMPTIONS = "hidden_api_blacklist_exemptions";
    public static final String HIDDEN_API_POLICY_PRE_P_APPS = "hidden_api_policy_pre_p_apps";
    public static final String HIDDEN_API_POLICY_P_APPS = "hidden_api_policy_p_apps";
    public static final String HIDE_ERROR_DIALOGS = "hide_error_dialogs";
    public static final String HOTSPOT_DUAL_BAND_MODE_ENABLE = "hotspot_dual_band_mode_enable";
    public static final String HTTP_PROXY = "http_proxy";
    public static final String INET_CONDITION_DEBOUNCE_DOWN_DELAY = "inet_condition_debounce_down_delay";
    public static final String INET_CONDITION_DEBOUNCE_UP_DELAY = "inet_condition_debounce_up_delay";
    public static final String INSTALLED_INSTANT_APP_MAX_CACHE_PERIOD = "installed_instant_app_max_cache_period";
    public static final String INSTALLED_INSTANT_APP_MIN_CACHE_PERIOD = "installed_instant_app_min_cache_period";
    @SystemApi
    public static final String INSTALL_CARRIER_APP_NOTIFICATION_PERSISTENT = "install_carrier_app_notification_persistent";
    @SystemApi
    public static final String INSTALL_CARRIER_APP_NOTIFICATION_SLEEP_MILLIS = "install_carrier_app_notification_sleep_millis";
    @Deprecated
    public static final String INSTALL_NON_MARKET_APPS = "install_non_market_apps";
    public static final String INSTANT_APP_DEXOPT_ENABLED = "instant_app_dexopt_enabled";
    public static final Set<String> INSTANT_APP_SETTINGS;
    public static final String INTENT_FIREWALL_UPDATE_CONTENT_URL = "intent_firewall_content_url";
    public static final String INTENT_FIREWALL_UPDATE_METADATA_URL = "intent_firewall_metadata_url";
    public static final String IS_LIGHT_THEME = "is_light_theme";
    public static final String IS_THEME_CHANGED = "is_theme_changed";
    public static final String JOB_SCHEDULER_CONSTANTS = "job_scheduler_constants";
    public static final String KEEP_PROFILE_IN_BACKGROUND = "keep_profile_in_background";
    public static final String LANG_ID_UPDATE_CONTENT_URL = "lang_id_content_url";
    public static final String LANG_ID_UPDATE_METADATA_URL = "lang_id_metadata_url";
    public static final String[] LEGACY_RESTORE_SETTINGS;
    public static final String LOCATION_BACKGROUND_THROTTLE_INTERVAL_MS = "location_background_throttle_interval_ms";
    public static final String LOCATION_BACKGROUND_THROTTLE_PACKAGE_WHITELIST = "location_background_throttle_package_whitelist";
    public static final String LOCATION_BACKGROUND_THROTTLE_PROXIMITY_ALERT_INTERVAL_MS = "location_background_throttle_proximity_alert_interval_ms";
    public static final String LOCATION_GLOBAL_KILL_SWITCH = "location_global_kill_switch";
    public static final String LOCATION_SETTINGS_LINK_TO_PERMISSIONS_ENABLED = "location_settings_link_to_permissions_enabled";
    public static final String LOCK_SOUND = "lock_sound";
    public static final String LOW_BATTERY_SOUND = "low_battery_sound";
    public static final String LOW_BATTERY_SOUND_TIMEOUT = "low_battery_sound_timeout";
    public static final String LOW_POWER_MODE = "low_power";
    public static final String LOW_POWER_MODE_STICKY = "low_power_sticky";
    public static final String LOW_POWER_MODE_SUGGESTION_PARAMS = "low_power_mode_suggestion_params";
    public static final String LOW_POWER_MODE_TRIGGER_LEVEL = "low_power_trigger_level";
    public static final String LOW_POWER_MODE_TRIGGER_LEVEL_MAX = "low_power_trigger_level_max";
    private static final SettingsValidators.Validator LOW_POWER_MODE_TRIGGER_LEVEL_VALIDATOR;
    public static final String LTE_SERVICE_FORCED = "lte_service_forced";
    public static final String MAX_CPU = "max_cpu";
    public static final String MAX_NOTIFICATION_ENQUEUE_RATE = "max_notification_enqueue_rate";
    public static final String MAX_SOUND_TRIGGER_DETECTION_SERVICE_OPS_PER_DAY = "max_sound_trigger_detection_service_ops_per_day";
    public static final String MDC_INITIAL_MAX_RETRY = "mdc_initial_max_retry";
    public static final String MHL_INPUT_SWITCHING_ENABLED = "mhl_input_switching_enabled";
    public static final String MHL_POWER_CHARGE_ENABLED = "mhl_power_charge_enabled";
    public static final String MOBILE_DATA = "mobile_data";
    public static final String MOBILE_DATA_ALWAYS_ON = "mobile_data_always_on";
    public static final String MOBILE_SLEEP_POLICY = "mobile_sleep_policy";
    public static final int MOBILE_SLEEP_POLICY_DEFAULT = 0;
    public static final int MOBILE_SLEEP_POLICY_NEVER = 2;
    public static final int MOBILE_SLEEP_POLICY_NEVER_WHILE_PLUGGED = 1;
    public static final String MODE_RINGER = "mode_ringer";
    private static final HashSet<String> MOVED_TO_SECURE;
    public static final String MULTI_SIM_DATA_CALL_SUBSCRIPTION = "multi_sim_data_call";
    public static final String MULTI_SIM_SMS_PROMPT = "multi_sim_sms_prompt";
    public static final String MULTI_SIM_SMS_SUBSCRIPTION = "multi_sim_sms";
    public static final String[] MULTI_SIM_USER_PREFERRED_SUBS;
    public static final String MULTI_SIM_VOICE_CALL_SUBSCRIPTION = "multi_sim_voice_call";
    public static final String MULTI_SIM_VOICE_PROMPT = "multi_sim_voice_prompt";
    public static final String NETPOLICY_OVERRIDE_ENABLED = "netpolicy_override_enabled";
    public static final String NETPOLICY_QUOTA_ENABLED = "netpolicy_quota_enabled";
    public static final String NETPOLICY_QUOTA_FRAC_JOBS = "netpolicy_quota_frac_jobs";
    public static final String NETPOLICY_QUOTA_FRAC_MULTIPATH = "netpolicy_quota_frac_multipath";
    public static final String NETPOLICY_QUOTA_LIMITED = "netpolicy_quota_limited";
    public static final String NETPOLICY_QUOTA_UNLIMITED = "netpolicy_quota_unlimited";
    public static final String NETSTATS_AUGMENT_ENABLED = "netstats_augment_enabled";
    public static final String NETSTATS_DEV_BUCKET_DURATION = "netstats_dev_bucket_duration";
    public static final String NETSTATS_DEV_DELETE_AGE = "netstats_dev_delete_age";
    public static final String NETSTATS_DEV_PERSIST_BYTES = "netstats_dev_persist_bytes";
    public static final String NETSTATS_DEV_ROTATE_AGE = "netstats_dev_rotate_age";
    public static final String NETSTATS_ENABLED = "netstats_enabled";
    public static final String NETSTATS_GLOBAL_ALERT_BYTES = "netstats_global_alert_bytes";
    public static final String NETSTATS_POLL_INTERVAL = "netstats_poll_interval";
    public static final String NETSTATS_SAMPLE_ENABLED = "netstats_sample_enabled";
    @Deprecated
    public static final String NETSTATS_TIME_CACHE_MAX_AGE = "netstats_time_cache_max_age";
    public static final String NETSTATS_UID_BUCKET_DURATION = "netstats_uid_bucket_duration";
    public static final String NETSTATS_UID_DELETE_AGE = "netstats_uid_delete_age";
    public static final String NETSTATS_UID_PERSIST_BYTES = "netstats_uid_persist_bytes";
    public static final String NETSTATS_UID_ROTATE_AGE = "netstats_uid_rotate_age";
    public static final String NETSTATS_UID_TAG_BUCKET_DURATION = "netstats_uid_tag_bucket_duration";
    public static final String NETSTATS_UID_TAG_DELETE_AGE = "netstats_uid_tag_delete_age";
    public static final String NETSTATS_UID_TAG_PERSIST_BYTES = "netstats_uid_tag_persist_bytes";
    public static final String NETSTATS_UID_TAG_ROTATE_AGE = "netstats_uid_tag_rotate_age";
    public static final String NETWORK_ACCESS_TIMEOUT_MS = "network_access_timeout_ms";
    public static final String NETWORK_AVOID_BAD_WIFI = "network_avoid_bad_wifi";
    public static final String NETWORK_DEFAULT_DAILY_MULTIPATH_QUOTA_BYTES = "network_default_daily_multipath_quota_bytes";
    public static final String NETWORK_METERED_MULTIPATH_PREFERENCE = "network_metered_multipath_preference";
    public static final String NETWORK_PREFERENCE = "network_preference";
    public static final String NETWORK_RECOMMENDATIONS_ENABLED = "network_recommendations_enabled";
    private static final SettingsValidators.Validator NETWORK_RECOMMENDATIONS_ENABLED_VALIDATOR;
    public static final String NETWORK_RECOMMENDATIONS_PACKAGE = "network_recommendations_package";
    public static final String NETWORK_RECOMMENDATION_REQUEST_TIMEOUT_MS = "network_recommendation_request_timeout_ms";
    public static final String NETWORK_SCORER_APP = "network_scorer_app";
    public static final String NETWORK_SCORING_PROVISIONED = "network_scoring_provisioned";
    public static final String NETWORK_SCORING_UI_ENABLED = "network_scoring_ui_enabled";
    public static final String NETWORK_SWITCH_NOTIFICATION_DAILY_LIMIT = "network_switch_notification_daily_limit";
    public static final String NETWORK_SWITCH_NOTIFICATION_RATE_LIMIT_MILLIS = "network_switch_notification_rate_limit_millis";
    public static final String NETWORK_WATCHLIST_ENABLED = "network_watchlist_enabled";
    public static final String NETWORK_WATCHLIST_LAST_REPORT_TIME = "network_watchlist_last_report_time";
    public static final String NEW_CONTACT_AGGREGATOR = "new_contact_aggregator";
    public static final String NIGHT_DISPLAY_FORCED_AUTO_MODE_AVAILABLE = "night_display_forced_auto_mode_available";
    public static final String NITZ_UPDATE_DIFF = "nitz_update_diff";
    public static final String NITZ_UPDATE_SPACING = "nitz_update_spacing";
    public static final String NOTIFICATION_SNOOZE_OPTIONS = "notification_snooze_options";
    public static final String NSD_ON = "nsd_on";
    public static final String NTP_SERVER = "ntp_server";
    public static final String NTP_SERVER_2 = "ntp_server_2";
    public static final String NTP_TIMEOUT = "ntp_timeout";
    @SystemApi
    public static final String OTA_DISABLE_AUTOMATIC_UPDATE = "ota_disable_automatic_update";
    public static final String OVERLAY_DISPLAY_DEVICES = "overlay_display_devices";
    public static final String OVERRIDE_SETTINGS_PROVIDER_RESTORE_ANY_VERSION = "override_settings_provider_restore_any_version";
    public static final String PACKAGE_VERIFIER_DEFAULT_RESPONSE = "verifier_default_response";
    public static final String PACKAGE_VERIFIER_ENABLE = "package_verifier_enable";
    public static final String PACKAGE_VERIFIER_INCLUDE_ADB = "verifier_verify_adb_installs";
    public static final String PACKAGE_VERIFIER_SETTING_VISIBLE = "verifier_setting_visible";
    public static final String PACKAGE_VERIFIER_TIMEOUT = "verifier_timeout";
    public static final String PAC_CHANGE_DELAY = "pac_change_delay";
    public static final String PDP_WATCHDOG_ERROR_POLL_COUNT = "pdp_watchdog_error_poll_count";
    public static final String PDP_WATCHDOG_ERROR_POLL_INTERVAL_MS = "pdp_watchdog_error_poll_interval_ms";
    public static final String PDP_WATCHDOG_LONG_POLL_INTERVAL_MS = "pdp_watchdog_long_poll_interval_ms";
    public static final String PDP_WATCHDOG_MAX_PDP_RESET_FAIL_COUNT = "pdp_watchdog_max_pdp_reset_fail_count";
    public static final String PDP_WATCHDOG_POLL_INTERVAL_MS = "pdp_watchdog_poll_interval_ms";
    public static final String PDP_WATCHDOG_TRIGGER_PACKET_COUNT = "pdp_watchdog_trigger_packet_count";
    public static final String POLICY_CONTROL = "policy_control";
    public static final String POWER_HINT_MODE = "power_hint";
    public static final String POWER_MANAGER_CONSTANTS = "power_manager_constants";
    public static final String POWER_SAVER_ENABLED = "power_saver_enabled";
    public static final String POWER_SAVING_WARNING_ACTIONBAR_ENABLED = "warning_action_bar";
    public static final String POWER_SOUNDS_ENABLED = "power_sounds_enabled";
    private static final SettingsValidators.Validator POWER_SOUNDS_ENABLED_VALIDATOR;
    public static final String PREFERRED_NETWORK_MODE = "preferred_network_mode";
    public static final String PREF_SMART_RINGTONE_TYPE = "pref_smart_ringtone_type";
    public static final String PRIVATE_DNS_DEFAULT_MODE = "private_dns_default_mode";
    public static final String PRIVATE_DNS_MODE = "private_dns_mode";
    private static final SettingsValidators.Validator PRIVATE_DNS_MODE_VALIDATOR;
    public static final String PRIVATE_DNS_SPECIFIER = "private_dns_specifier";
    private static final SettingsValidators.Validator PRIVATE_DNS_SPECIFIER_VALIDATOR;
    public static final String PRIV_APP_OOB_ENABLED = "priv_app_oob_enabled";
    public static final String PRIV_APP_OOB_LIST = "priv_app_oob_list";
    public static final String PROVISIONING_APN_ALARM_DELAY_IN_MS = "provisioning_apn_alarm_delay_in_ms";
    public static final String RADIO_BLUETOOTH = "bluetooth";
    public static final String RADIO_CELL = "cell";
    public static final String RADIO_NFC = "nfc";
    public static final String RADIO_WIFI = "wifi";
    public static final String RADIO_WIMAX = "wimax";
    public static final String READ_EXTERNAL_STORAGE_ENFORCED_DEFAULT = "read_external_storage_enforced_default";
    public static final String RECOMMENDED_NETWORK_EVALUATOR_CACHE_EXPIRY_MS = "recommended_network_evaluator_cache_expiry_ms";
    public static final String REQUIRE_PASSWORD_TO_DECRYPT = "require_password_to_decrypt";
    public static final String SAFE_BOOT_DISALLOWED = "safe_boot_disallowed";
    public static final String SELINUX_STATUS = "selinux_status";
    public static final String SELINUX_UPDATE_CONTENT_URL = "selinux_content_url";
    public static final String SELINUX_UPDATE_METADATA_URL = "selinux_metadata_url";
    public static final String SEND_ACTION_APP_ERROR = "send_action_app_error";
    public static final String[] SETTINGS_TO_BACKUP;
    public static final String SETUP_PREPAID_DATA_SERVICE_URL = "setup_prepaid_data_service_url";
    public static final String SETUP_PREPAID_DETECTION_REDIR_HOST = "setup_prepaid_detection_redir_host";
    public static final String SETUP_PREPAID_DETECTION_TARGET_URL = "setup_prepaid_detection_target_url";
    public static final String SET_GLOBAL_HTTP_PROXY = "set_global_http_proxy";
    public static final String SET_INSTALL_LOCATION = "set_install_location";
    public static final String SHIELD_KEY_CODE = "shield_key_code";
    public static final String SHORTCUT_MANAGER_CONSTANTS = "shortcut_manager_constants";
    public static final String SHOW_FIRST_CRASH_DIALOG = "show_first_crash_dialog";
    public static final String SHOW_MUTE_IN_CRASH_DIALOG = "show_mute_in_crash_dialog";
    public static final String SHOW_NOTIFICATION_CHANNEL_WARNINGS = "show_notification_channel_warnings";
    @Deprecated
    public static final String SHOW_PROCESSES = "show_processes";
    public static final String SHOW_RESTART_IN_CRASH_DIALOG = "show_restart_in_crash_dialog";
    public static final String SHOW_TEMPERATURE_WARNING = "show_temperature_warning";
    public static final String SHOW_ZEN_SETTINGS_SUGGESTION = "show_zen_settings_suggestion";
    public static final String SHOW_ZEN_UPGRADE_NOTIFICATION = "show_zen_upgrade_notification";
    public static final String SMART_REPLIES_IN_NOTIFICATIONS_FLAGS = "smart_replies_in_notifications_flags";
    public static final String SMART_SELECTION_UPDATE_CONTENT_URL = "smart_selection_content_url";
    public static final String SMART_SELECTION_UPDATE_METADATA_URL = "smart_selection_metadata_url";
    public static final String SMS_OUTGOING_CHECK_INTERVAL_MS = "sms_outgoing_check_interval_ms";
    public static final String SMS_OUTGOING_CHECK_MAX_COUNT = "sms_outgoing_check_max_count";
    public static final String SMS_SHORT_CODES_UPDATE_CONTENT_URL = "sms_short_codes_content_url";
    public static final String SMS_SHORT_CODES_UPDATE_METADATA_URL = "sms_short_codes_metadata_url";
    public static final String SMS_SHORT_CODE_CONFIRMATION = "sms_short_code_confirmation";
    public static final String SMS_SHORT_CODE_RULE = "sms_short_code_rule";
    public static final String SOFT_AP_TIMEOUT_ENABLED = "soft_ap_timeout_enabled";
    public static final String SOFT_AP_TIMEOUT_ENABLED_TIME = "soft_ap_timeout_enabled_time";
    private static final SettingsValidators.Validator SOFT_AP_TIMEOUT_ENABLED_VALIDATOR;
    public static final String SOUND_TRIGGER_DETECTION_SERVICE_OP_TIMEOUT = "sound_trigger_detection_service_op_timeout";
    public static final String SPEED_LABEL_CACHE_EVICTION_AGE_MILLIS = "speed_label_cache_eviction_age_millis";
    public static final String SQLITE_COMPATIBILITY_WAL_FLAGS = "sqlite_compatibility_wal_flags";
    public static final String STAY_ON_WHILE_PLUGGED_IN = "stay_on_while_plugged_in";
    private static final SettingsValidators.Validator STAY_ON_WHILE_PLUGGED_IN_VALIDATOR;
    public static final String STORAGE_BENCHMARK_INTERVAL = "storage_benchmark_interval";
    public static final String STORAGE_SETTINGS_CLOBBER_THRESHOLD = "storage_settings_clobber_threshold";
    public static final String SWAP_ENABLED = "swap_enabled";
    public static final String SYNC_MANAGER_CONSTANTS = "sync_manager_constants";
    public static final String SYNC_MAX_RETRY_DELAY_IN_SECONDS = "sync_max_retry_delay_in_seconds";
    public static final String SYS_FREE_STORAGE_LOG_INTERVAL = "sys_free_storage_log_interval";
    public static final String SYS_STORAGE_CACHE_MAX_BYTES = "sys_storage_cache_max_bytes";
    public static final String SYS_STORAGE_CACHE_PERCENTAGE = "sys_storage_cache_percentage";
    public static final String SYS_STORAGE_FULL_THRESHOLD_BYTES = "sys_storage_full_threshold_bytes";
    public static final String SYS_STORAGE_THRESHOLD_MAX_BYTES = "sys_storage_threshold_max_bytes";
    public static final String SYS_STORAGE_THRESHOLD_PERCENTAGE = "sys_storage_threshold_percentage";
    public static final String SYS_TRACED = "sys_traced";
    public static final String SYS_UIDCPUPOWER = "sys_uidcpupower";
    public static final String SYS_VDSO = "sys_vdso";
    public static final String TCP_DEFAULT_INIT_RWND = "tcp_default_init_rwnd";
    public static final String TETHER_DUN_APN = "tether_dun_apn";
    public static final String TETHER_DUN_REQUIRED = "tether_dun_required";
    public static final String TETHER_OFFLOAD_DISABLED = "tether_offload_disabled";
    public static final String TETHER_SUPPORTED = "tether_supported";
    public static final String TEXT_CLASSIFIER_CONSTANTS = "text_classifier_constants";
    @SystemApi
    public static final String THEATER_MODE_ON = "theater_mode_on";
    public static final String THEME_BACKGROUND_COLOR = "theme_background_color";
    public static final String THEME_HIGHLIGHT_COLOR = "theme_highlight_color";
    public static final String THEME_MAIN_COLOR = "theme_main_color";
    public static final String THEME_TEXT_COLOR = "theme_text_color";
    public static final String TIME_ONLY_MODE_CONSTANTS = "time_only_mode_constants";
    public static final String TOUCH_KEY_LIGHT = "touch_key_light";
    public static final String[] TRANSIENT_SETTINGS;
    public static final String TRANSITION_ANIMATION_SCALE = "transition_animation_scale";
    public static final String TRUSTED_SOUND = "trusted_sound";
    public static final String TZINFO_UPDATE_CONTENT_URL = "tzinfo_content_url";
    public static final String TZINFO_UPDATE_METADATA_URL = "tzinfo_metadata_url";
    public static final String UNGAZE_SLEEP_ENABLED = "ungaze_sleep_enabled";
    public static final String UNINSTALLED_INSTANT_APP_MAX_CACHE_PERIOD = "uninstalled_instant_app_max_cache_period";
    public static final String UNINSTALLED_INSTANT_APP_MIN_CACHE_PERIOD = "uninstalled_instant_app_min_cache_period";
    public static final String UNLOCK_SOUND = "unlock_sound";
    public static final String UNUSED_STATIC_SHARED_LIB_MIN_CACHE_PERIOD = "unused_static_shared_lib_min_cache_period";
    public static final String USB_MASS_STORAGE_ENABLED = "usb_mass_storage_enabled";
    private static final SettingsValidators.Validator USB_MASS_STORAGE_ENABLED_VALIDATOR;
    public static final String USER_ABSENT_RADIOS_OFF_FOR_SMALL_BATTERY_ENABLED = "user_absent_radios_off_for_small_battery_enabled";
    public static final String USE_GOOGLE_MAIL = "use_google_mail";
    public static final String USE_OPEN_WIFI_PACKAGE = "use_open_wifi_package";
    private static final SettingsValidators.Validator USE_OPEN_WIFI_PACKAGE_VALIDATOR;
    public static final Map<String, SettingsValidators.Validator> VALIDATORS;
    @Deprecated
    public static final String VT_IMS_ENABLED = "vt_ims_enabled";
    public static final String WAIT_FOR_DEBUGGER = "wait_for_debugger";
    public static final String WARNING_TEMPERATURE = "warning_temperature";
    public static final String WEBVIEW_DATA_REDUCTION_PROXY_KEY = "webview_data_reduction_proxy_key";
    public static final String WEBVIEW_FALLBACK_LOGIC_ENABLED = "webview_fallback_logic_enabled";
    @SystemApi
    public static final String WEBVIEW_MULTIPROCESS = "webview_multiprocess";
    public static final String WEBVIEW_PROVIDER = "webview_provider";
    @Deprecated
    public static final String WFC_IMS_ENABLED = "wfc_ims_enabled";
    @Deprecated
    public static final String WFC_IMS_MODE = "wfc_ims_mode";
    @Deprecated
    public static final String WFC_IMS_ROAMING_ENABLED = "wfc_ims_roaming_enabled";
    @Deprecated
    public static final String WFC_IMS_ROAMING_MODE = "wfc_ims_roaming_mode";
    @SystemApi
    public static final String WIFI_BADGING_THRESHOLDS = "wifi_badging_thresholds";
    public static final String WIFI_BOUNCE_DELAY_OVERRIDE_MS = "wifi_bounce_delay_override_ms";
    public static final String WIFI_CARRIER_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wifi_carrier_networks_available_notification_on";
    private static final SettingsValidators.Validator WIFI_CARRIER_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR;
    public static final String WIFI_CONNECTED_MAC_RANDOMIZATION_ENABLED = "wifi_connected_mac_randomization_enabled";
    public static final String WIFI_COUNTRY_CODE = "wifi_country_code";
    public static final String WIFI_COVERAGE_EXTEND_FEATURE_ENABLED = "wifi_coverage_extend_feature_enabled";
    public static final String WIFI_DEVICE_OWNER_CONFIGS_LOCKDOWN = "wifi_device_owner_configs_lockdown";
    public static final String WIFI_DISPLAY_CERTIFICATION_ON = "wifi_display_certification_on";
    public static final String WIFI_DISPLAY_ON = "wifi_display_on";
    public static final String WIFI_DISPLAY_WPS_CONFIG = "wifi_display_wps_config";
    public static final String WIFI_ENHANCED_AUTO_JOIN = "wifi_enhanced_auto_join";
    public static final String WIFI_EPHEMERAL_OUT_OF_RANGE_TIMEOUT_MS = "wifi_ephemeral_out_of_range_timeout_ms";
    public static final String WIFI_FRAMEWORK_SCAN_INTERVAL_MS = "wifi_framework_scan_interval_ms";
    public static final String WIFI_FREQUENCY_BAND = "wifi_frequency_band";
    public static final String WIFI_IDLE_MS = "wifi_idle_ms";
    public static final String WIFI_MAX_DHCP_RETRY_COUNT = "wifi_max_dhcp_retry_count";
    public static final String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = "wifi_mobile_data_transition_wakelock_timeout_ms";
    @Deprecated
    public static final String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wifi_networks_available_notification_on";
    private static final SettingsValidators.Validator WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR;
    public static final String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = "wifi_networks_available_repeat_delay";
    private static final SettingsValidators.Validator WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY_VALIDATOR;
    public static final String WIFI_NETWORK_SHOW_RSSI = "wifi_network_show_rssi";
    public static final String WIFI_NUM_OPEN_NETWORKS_KEPT = "wifi_num_open_networks_kept";
    private static final SettingsValidators.Validator WIFI_NUM_OPEN_NETWORKS_KEPT_VALIDATOR;
    public static final String WIFI_ON = "wifi_on";
    public static final String WIFI_ON_WHEN_PROXY_DISCONNECTED = "wifi_on_when_proxy_disconnected";
    public static final String WIFI_P2P_DEVICE_NAME = "wifi_p2p_device_name";
    public static final String WIFI_REENABLE_DELAY_MS = "wifi_reenable_delay";
    public static final String WIFI_RTT_BACKGROUND_EXEC_GAP_MS = "wifi_rtt_background_exec_gap_ms";
    public static final String WIFI_SAVED_STATE = "wifi_saved_state";
    public static final String WIFI_SCAN_ALWAYS_AVAILABLE = "wifi_scan_always_enabled";
    public static final String WIFI_SCAN_INTERVAL_WHEN_P2P_CONNECTED_MS = "wifi_scan_interval_p2p_connected_ms";
    public static final String WIFI_SCORE_PARAMS = "wifi_score_params";
    public static final String WIFI_SLEEP_POLICY = "wifi_sleep_policy";
    public static final int WIFI_SLEEP_POLICY_DEFAULT = 0;
    public static final int WIFI_SLEEP_POLICY_NEVER = 2;
    public static final int WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED = 1;
    public static final String WIFI_SUPPLICANT_SCAN_INTERVAL_MS = "wifi_supplicant_scan_interval_ms";
    public static final String WIFI_SUSPEND_OPTIMIZATIONS_ENABLED = "wifi_suspend_optimizations_enabled";
    public static final String WIFI_VERBOSE_LOGGING_ENABLED = "wifi_verbose_logging_enabled";
    @SystemApi
    public static final String WIFI_WAKEUP_ENABLED = "wifi_wakeup_enabled";
    private static final SettingsValidators.Validator WIFI_WAKEUP_ENABLED_VALIDATOR;
    public static final String WIFI_WATCHDOG_ON = "wifi_watchdog_on";
    public static final String WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED = "wifi_watchdog_poor_network_test_enabled";
    private static final SettingsValidators.Validator WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED_VALIDATOR;
    public static final String WIMAX_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wimax_networks_available_notification_on";
    public static final String WINDOW_ANIMATION_SCALE = "window_animation_scale";
    public static final String WTF_IS_FATAL = "wtf_is_fatal";
    public static final String ZEN_DURATION = "zen_duration";
    public static final int ZEN_DURATION_FOREVER = 0;
    public static final int ZEN_DURATION_PROMPT = -1;
    private static final SettingsValidators.Validator ZEN_DURATION_VALIDATOR;
    public static final String ZEN_MODE = "zen_mode";
    public static final int ZEN_MODE_ALARMS = 3;
    public static final String ZEN_MODE_CONFIG_ETAG = "zen_mode_config_etag";
    public static final int ZEN_MODE_IMPORTANT_INTERRUPTIONS = 1;
    public static final int ZEN_MODE_NO_INTERRUPTIONS = 2;
    public static final int ZEN_MODE_OFF = 0;
    public static final String ZEN_MODE_RINGER_LEVEL = "zen_mode_ringer_level";
    public static final String ZEN_SETTINGS_SUGGESTION_VIEWED = "zen_settings_suggestion_viewed";
    public static final String ZEN_SETTINGS_UPDATED = "zen_settings_updated";
    public static final String ZRAM_ENABLED = "zram_enabled";
    private static final Settings.NameValueCache sNameValueCache;
    private static final Settings.ContentProviderHolder sProviderHolder;
    
    static
    {
      AUTO_TIME_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      AUTO_TIME_ZONE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      DOCK_SOUNDS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      POWER_SOUNDS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      CHARGING_SOUNDS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      CHARGING_VIBRATION_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      STAY_ON_WHILE_PLUGGED_IN_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          boolean bool = false;
          try
          {
            int i = Integer.parseInt(paramAnonymousString);
            if ((i != 0) && (i != 1) && (i != 2) && (i != 4) && (i != 3) && (i != 5) && (i != 6) && (i != 7)) {
              break label56;
            }
            bool = true;
            label56:
            return bool;
          }
          catch (NumberFormatException paramAnonymousString) {}
          return false;
        }
      };
      BUGREPORT_IN_POWER_MENU_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      BLUETOOTH_ON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      USB_MASS_STORAGE_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WIFI_CARRIER_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      WIFI_NUM_OPEN_NETWORKS_KEPT_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      SOFT_AP_TIMEOUT_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WIFI_WAKEUP_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      NETWORK_RECOMMENDATIONS_ENABLED_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "-1", "0", "1" });
      USE_OPEN_WIFI_PACKAGE_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          boolean bool;
          if ((paramAnonymousString != null) && (!SettingsValidators.PACKAGE_NAME_VALIDATOR.validate(paramAnonymousString))) {
            bool = false;
          } else {
            bool = true;
          }
          return bool;
        }
      };
      WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED_VALIDATOR = SettingsValidators.ANY_STRING_VALIDATOR;
      PRIVATE_DNS_MODE_VALIDATOR = SettingsValidators.ANY_STRING_VALIDATOR;
      PRIVATE_DNS_SPECIFIER_VALIDATOR = SettingsValidators.ANY_STRING_VALIDATOR;
      APP_AUTO_RESTRICTION_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      EMERGENCY_TONE_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "0", "1", "2" });
      CALL_AUTO_RETRY_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      LOW_POWER_MODE_TRIGGER_LEVEL_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 100);
      DOCK_AUDIO_MEDIA_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ENCODED_SURROUND_OUTPUT_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "0", "1", "2", "3" });
      ENCODED_SURROUND_OUTPUT_ENABLED_FORMATS_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          try
          {
            paramAnonymousString = TextUtils.split(paramAnonymousString, ",");
            int i = paramAnonymousString.length;
            for (int j = 0; j < i; j++)
            {
              int k = Integer.valueOf(paramAnonymousString[j]).intValue();
              int m = 0;
              int[] arrayOfInt = AudioFormat.SURROUND_SOUND_ENCODING;
              int n = arrayOfInt.length;
              int i2;
              for (int i1 = 0;; i1++)
              {
                i2 = m;
                if (i1 >= n) {
                  break;
                }
                i2 = arrayOfInt[i1];
                if (i2 == k)
                {
                  i2 = 1;
                  break;
                }
              }
              if (i2 == 0) {
                return false;
              }
            }
            return true;
          }
          catch (NumberFormatException paramAnonymousString) {}
          return false;
        }
      };
      ZEN_DURATION_VALIDATOR = SettingsValidators.ANY_INTEGER_VALIDATOR;
      SETTINGS_TO_BACKUP = new String[] { "bugreport_in_power_menu", "stay_on_while_plugged_in", "app_auto_restriction_enabled", "auto_time", "auto_time_zone", "power_sounds_enabled", "dock_sounds_enabled", "charging_sounds_enabled", "usb_mass_storage_enabled", "network_recommendations_enabled", "wifi_wakeup_enabled", "wifi_networks_available_notification_on", "wifi_carrier_networks_available_notification_on", "use_open_wifi_package", "wifi_watchdog_poor_network_test_enabled", "emergency_tone", "call_auto_retry", "dock_audio_media_enabled", "encoded_surround_output", "encoded_surround_output_enabled_formats", "low_power_trigger_level", "bluetooth_on", "private_dns_mode", "private_dns_specifier", "soft_ap_timeout_enabled", "zen_duration", "charging_vibration_enabled" };
      VALIDATORS = new ArrayMap();
      VALIDATORS.put("bugreport_in_power_menu", BUGREPORT_IN_POWER_MENU_VALIDATOR);
      VALIDATORS.put("stay_on_while_plugged_in", STAY_ON_WHILE_PLUGGED_IN_VALIDATOR);
      VALIDATORS.put("auto_time", AUTO_TIME_VALIDATOR);
      VALIDATORS.put("auto_time_zone", AUTO_TIME_ZONE_VALIDATOR);
      VALIDATORS.put("power_sounds_enabled", POWER_SOUNDS_ENABLED_VALIDATOR);
      VALIDATORS.put("dock_sounds_enabled", DOCK_SOUNDS_ENABLED_VALIDATOR);
      VALIDATORS.put("charging_sounds_enabled", CHARGING_SOUNDS_ENABLED_VALIDATOR);
      VALIDATORS.put("usb_mass_storage_enabled", USB_MASS_STORAGE_ENABLED_VALIDATOR);
      VALIDATORS.put("network_recommendations_enabled", NETWORK_RECOMMENDATIONS_ENABLED_VALIDATOR);
      VALIDATORS.put("wifi_wakeup_enabled", WIFI_WAKEUP_ENABLED_VALIDATOR);
      VALIDATORS.put("wifi_networks_available_notification_on", WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR);
      VALIDATORS.put("use_open_wifi_package", USE_OPEN_WIFI_PACKAGE_VALIDATOR);
      VALIDATORS.put("wifi_watchdog_poor_network_test_enabled", WIFI_WATCHDOG_POOR_NETWORK_TEST_ENABLED_VALIDATOR);
      VALIDATORS.put("emergency_tone", EMERGENCY_TONE_VALIDATOR);
      VALIDATORS.put("call_auto_retry", CALL_AUTO_RETRY_VALIDATOR);
      VALIDATORS.put("dock_audio_media_enabled", DOCK_AUDIO_MEDIA_ENABLED_VALIDATOR);
      VALIDATORS.put("encoded_surround_output", ENCODED_SURROUND_OUTPUT_VALIDATOR);
      VALIDATORS.put("encoded_surround_output_enabled_formats", ENCODED_SURROUND_OUTPUT_ENABLED_FORMATS_VALIDATOR);
      VALIDATORS.put("low_power_trigger_level", LOW_POWER_MODE_TRIGGER_LEVEL_VALIDATOR);
      VALIDATORS.put("low_power_trigger_level_max", LOW_POWER_MODE_TRIGGER_LEVEL_VALIDATOR);
      VALIDATORS.put("bluetooth_on", BLUETOOTH_ON_VALIDATOR);
      VALIDATORS.put("private_dns_mode", PRIVATE_DNS_MODE_VALIDATOR);
      VALIDATORS.put("private_dns_specifier", PRIVATE_DNS_SPECIFIER_VALIDATOR);
      VALIDATORS.put("soft_ap_timeout_enabled", SOFT_AP_TIMEOUT_ENABLED_VALIDATOR);
      VALIDATORS.put("wifi_carrier_networks_available_notification_on", WIFI_CARRIER_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR);
      VALIDATORS.put("app_auto_restriction_enabled", APP_AUTO_RESTRICTION_ENABLED_VALIDATOR);
      VALIDATORS.put("zen_duration", ZEN_DURATION_VALIDATOR);
      VALIDATORS.put("charging_vibration_enabled", CHARGING_VIBRATION_ENABLED_VALIDATOR);
      TRANSIENT_SETTINGS = new String[] { "location_global_kill_switch" };
      LEGACY_RESTORE_SETTINGS = new String[0];
      sProviderHolder = new Settings.ContentProviderHolder(CONTENT_URI);
      sNameValueCache = new Settings.NameValueCache(CONTENT_URI, "GET_global", "PUT_global", sProviderHolder);
      MOVED_TO_SECURE = new HashSet(1);
      MOVED_TO_SECURE.add("install_non_market_apps");
      MULTI_SIM_USER_PREFERRED_SUBS = new String[] { "user_preferred_sub1", "user_preferred_sub2", "user_preferred_sub3" };
      INSTANT_APP_SETTINGS = new ArraySet();
      INSTANT_APP_SETTINGS.add("wait_for_debugger");
      INSTANT_APP_SETTINGS.add("device_provisioned");
      INSTANT_APP_SETTINGS.add("force_resizable_activities");
      INSTANT_APP_SETTINGS.add("debug.force_rtl");
      INSTANT_APP_SETTINGS.add("ephemeral_cookie_max_size_bytes");
      INSTANT_APP_SETTINGS.add("airplane_mode_on");
      INSTANT_APP_SETTINGS.add("window_animation_scale");
      INSTANT_APP_SETTINGS.add("transition_animation_scale");
      INSTANT_APP_SETTINGS.add("animator_duration_scale");
      INSTANT_APP_SETTINGS.add("debug_view_attributes");
      INSTANT_APP_SETTINGS.add("wtf_is_fatal");
      INSTANT_APP_SETTINGS.add("send_action_app_error");
      INSTANT_APP_SETTINGS.add("zen_mode");
    }
    
    public Global() {}
    
    public static void clearProviderForTest()
    {
      sProviderHolder.clearProviderForTest();
      sNameValueCache.clearGenerationTrackerForTest();
    }
    
    public static final String getBluetoothA2dpOptionalCodecsEnabledKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_a2dp_optional_codecs_enabled_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothA2dpSinkPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_a2dp_sink_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothA2dpSrcPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_a2dp_src_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothA2dpSupportsOptionalCodecsKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_a2dp_supports_optional_codecs_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothHeadsetPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_headset_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothHearingAidPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_hearing_aid_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothHidHostPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_input_device_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothMapClientPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_map_client_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothMapPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_map_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothPanPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_pan_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothPbapClientPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_pbap_client_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static final String getBluetoothSapPriorityKey(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("bluetooth_sap_priority_");
      localStringBuilder.append(paramString.toUpperCase(Locale.ROOT));
      return localStringBuilder.toString();
    }
    
    public static float getFloat(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      paramContentResolver = getString(paramContentResolver, paramString);
      if (paramContentResolver != null) {
        try
        {
          float f = Float.parseFloat(paramContentResolver);
          return f;
        }
        catch (NumberFormatException paramContentResolver)
        {
          throw new Settings.SettingNotFoundException(paramString);
        }
      }
      throw new Settings.SettingNotFoundException(paramString);
    }
    
    public static float getFloat(ContentResolver paramContentResolver, String paramString, float paramFloat)
    {
      paramContentResolver = getString(paramContentResolver, paramString);
      if (paramContentResolver != null) {
        try
        {
          float f = Float.parseFloat(paramContentResolver);
          paramFloat = f;
        }
        catch (NumberFormatException paramContentResolver)
        {
          return paramFloat;
        }
      }
      return paramFloat;
    }
    
    public static int getInt(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      paramContentResolver = getString(paramContentResolver, paramString);
      try
      {
        int i = Integer.parseInt(paramContentResolver);
        return i;
      }
      catch (NumberFormatException paramContentResolver)
      {
        throw new Settings.SettingNotFoundException(paramString);
      }
    }
    
    public static int getInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      paramContentResolver = getString(paramContentResolver, paramString);
      if (paramContentResolver != null) {
        try
        {
          int i = Integer.parseInt(paramContentResolver);
          paramInt = i;
        }
        catch (NumberFormatException paramContentResolver)
        {
          return paramInt;
        }
      }
      return paramInt;
    }
    
    public static long getLong(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      paramContentResolver = getString(paramContentResolver, paramString);
      try
      {
        long l = Long.parseLong(paramContentResolver);
        return l;
      }
      catch (NumberFormatException paramContentResolver)
      {
        throw new Settings.SettingNotFoundException(paramString);
      }
    }
    
    public static long getLong(ContentResolver paramContentResolver, String paramString, long paramLong)
    {
      paramContentResolver = getString(paramContentResolver, paramString);
      if (paramContentResolver != null) {
        try
        {
          long l = Long.parseLong(paramContentResolver);
          paramLong = l;
        }
        catch (NumberFormatException paramContentResolver) {}
      }
      return paramLong;
    }
    
    public static void getMovedToSecureSettings(Set<String> paramSet)
    {
      paramSet.addAll(MOVED_TO_SECURE);
    }
    
    public static String getString(ContentResolver paramContentResolver, String paramString)
    {
      return getStringForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static String getStringForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      if (MOVED_TO_SECURE.contains(paramString))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Setting ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" has moved from android.provider.Settings.Global to android.provider.Settings.Secure, returning read-only value.");
        Log.w("Settings", localStringBuilder.toString());
        return Settings.Secure.getStringForUser(paramContentResolver, paramString, paramInt);
      }
      return sNameValueCache.getStringForUser(paramContentResolver, paramString, paramInt);
    }
    
    public static Uri getUriFor(String paramString)
    {
      return getUriFor(CONTENT_URI, paramString);
    }
    
    public static boolean isValidZenMode(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return false;
      }
      return true;
    }
    
    public static boolean putFloat(ContentResolver paramContentResolver, String paramString, float paramFloat)
    {
      return putString(paramContentResolver, paramString, Float.toString(paramFloat));
    }
    
    public static boolean putInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      return putString(paramContentResolver, paramString, Integer.toString(paramInt));
    }
    
    public static boolean putLong(ContentResolver paramContentResolver, String paramString, long paramLong)
    {
      return putString(paramContentResolver, paramString, Long.toString(paramLong));
    }
    
    public static boolean putString(ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      return putStringForUser(paramContentResolver, paramString1, paramString2, null, false, paramContentResolver.getUserId());
    }
    
    @SystemApi
    public static boolean putString(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    {
      return putStringForUser(paramContentResolver, paramString1, paramString2, paramString3, paramBoolean, paramContentResolver.getUserId());
    }
    
    public static boolean putStringForUser(ContentResolver paramContentResolver, String paramString1, String paramString2, int paramInt)
    {
      return putStringForUser(paramContentResolver, paramString1, paramString2, null, false, paramInt);
    }
    
    public static boolean putStringForUser(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, boolean paramBoolean, int paramInt)
    {
      if (MOVED_TO_SECURE.contains(paramString1))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Setting ");
        localStringBuilder.append(paramString1);
        localStringBuilder.append(" has moved from android.provider.Settings.Global to android.provider.Settings.Secure, value is unchanged.");
        Log.w("Settings", localStringBuilder.toString());
        return Settings.Secure.putStringForUser(paramContentResolver, paramString1, paramString2, paramString3, paramBoolean, paramInt);
      }
      return sNameValueCache.putStringForUser(paramContentResolver, paramString1, paramString2, paramString3, paramBoolean, paramInt);
    }
    
    @SystemApi
    public static void resetToDefaults(ContentResolver paramContentResolver, String paramString)
    {
      resetToDefaultsAsUser(paramContentResolver, paramString, 1, paramContentResolver.getUserId());
    }
    
    public static void resetToDefaultsAsUser(ContentResolver paramContentResolver, String paramString, int paramInt1, int paramInt2)
    {
      try
      {
        Bundle localBundle = new android/os/Bundle;
        localBundle.<init>();
        localBundle.putInt("_user", paramInt2);
        if (paramString != null) {
          localBundle.putString("_tag", paramString);
        }
        localBundle.putInt("_reset_mode", paramInt1);
        sProviderHolder.getProvider(paramContentResolver).call(paramContentResolver.getPackageName(), "RESET_global", null, localBundle);
      }
      catch (RemoteException paramContentResolver)
      {
        paramString = new StringBuilder();
        paramString.append("Can't reset do defaults for ");
        paramString.append(CONTENT_URI);
        Log.w("Settings", paramString.toString(), paramContentResolver);
      }
    }
    
    public static String zenModeToString(int paramInt)
    {
      if (paramInt == 1) {
        return "ZEN_MODE_IMPORTANT_INTERRUPTIONS";
      }
      if (paramInt == 3) {
        return "ZEN_MODE_ALARMS";
      }
      if (paramInt == 2) {
        return "ZEN_MODE_NO_INTERRUPTIONS";
      }
      return "ZEN_MODE_OFF";
    }
  }
  
  private static class NameValueCache
  {
    private static final boolean DEBUG = false;
    private static final String NAME_EQ_PLACEHOLDER = "name=?";
    private static final String[] SELECT_VALUE_PROJECTION = { "value" };
    private final String mCallGetCommand;
    private final String mCallSetCommand;
    @GuardedBy("this")
    private Settings.GenerationTracker mGenerationTracker;
    private final Settings.ContentProviderHolder mProviderHolder;
    private final Uri mUri;
    private final HashMap<String, String> mValues = new HashMap();
    
    public NameValueCache(Uri paramUri, String paramString1, String paramString2, Settings.ContentProviderHolder paramContentProviderHolder)
    {
      mUri = paramUri;
      mCallGetCommand = paramString1;
      mCallSetCommand = paramString2;
      mProviderHolder = paramContentProviderHolder;
    }
    
    public void clearGenerationTrackerForTest()
    {
      try
      {
        if (mGenerationTracker != null) {
          mGenerationTracker.destroy();
        }
        mValues.clear();
        mGenerationTracker = null;
        return;
      }
      finally {}
    }
    
    /* Error */
    public String getStringForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      // Byte code:
      //   0: iload_3
      //   1: invokestatic 88	android/os/UserHandle:myUserId	()I
      //   4: if_icmpne +9 -> 13
      //   7: iconst_1
      //   8: istore 4
      //   10: goto +6 -> 16
      //   13: iconst_0
      //   14: istore 4
      //   16: iconst_m1
      //   17: istore 5
      //   19: iload 5
      //   21: istore 6
      //   23: iload 4
      //   25: ifeq +93 -> 118
      //   28: aload_0
      //   29: monitorenter
      //   30: iload 5
      //   32: istore 6
      //   34: aload_0
      //   35: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   38: ifnull +70 -> 108
      //   41: aload_0
      //   42: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   45: invokevirtual 92	android/provider/Settings$GenerationTracker:isGenerationChanged	()Z
      //   48: ifeq +13 -> 61
      //   51: aload_0
      //   52: getfield 47	android/provider/Settings$NameValueCache:mValues	Ljava/util/HashMap;
      //   55: invokevirtual 77	java/util/HashMap:clear	()V
      //   58: goto +30 -> 88
      //   61: aload_0
      //   62: getfield 47	android/provider/Settings$NameValueCache:mValues	Ljava/util/HashMap;
      //   65: aload_2
      //   66: invokevirtual 96	java/util/HashMap:containsKey	(Ljava/lang/Object;)Z
      //   69: ifeq +19 -> 88
      //   72: aload_0
      //   73: getfield 47	android/provider/Settings$NameValueCache:mValues	Ljava/util/HashMap;
      //   76: aload_2
      //   77: invokevirtual 100	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
      //   80: checkcast 34	java/lang/String
      //   83: astore_1
      //   84: aload_0
      //   85: monitorexit
      //   86: aload_1
      //   87: areturn
      //   88: iload 5
      //   90: istore 6
      //   92: aload_0
      //   93: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   96: ifnull +12 -> 108
      //   99: aload_0
      //   100: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   103: invokevirtual 103	android/provider/Settings$GenerationTracker:getCurrentGeneration	()I
      //   106: istore 6
      //   108: aload_0
      //   109: monitorexit
      //   110: goto +8 -> 118
      //   113: astore_1
      //   114: aload_0
      //   115: monitorexit
      //   116: aload_1
      //   117: athrow
      //   118: aload_0
      //   119: getfield 55	android/provider/Settings$NameValueCache:mProviderHolder	Landroid/provider/Settings$ContentProviderHolder;
      //   122: aload_1
      //   123: invokevirtual 109	android/provider/Settings$ContentProviderHolder:getProvider	(Landroid/content/ContentResolver;)Landroid/content/IContentProvider;
      //   126: astore 7
      //   128: aload_0
      //   129: getfield 51	android/provider/Settings$NameValueCache:mCallGetCommand	Ljava/lang/String;
      //   132: ifnull +357 -> 489
      //   135: aconst_null
      //   136: astore 8
      //   138: iload 4
      //   140: ifne +29 -> 169
      //   143: new 111	android/os/Bundle
      //   146: astore 8
      //   148: aload 8
      //   150: invokespecial 112	android/os/Bundle:<init>	()V
      //   153: aload 8
      //   155: ldc 114
      //   157: iload_3
      //   158: invokevirtual 118	android/os/Bundle:putInt	(Ljava/lang/String;I)V
      //   161: goto +8 -> 169
      //   164: astore 8
      //   166: goto +323 -> 489
      //   169: iconst_0
      //   170: istore 5
      //   172: aload_0
      //   173: monitorenter
      //   174: aload 8
      //   176: astore 9
      //   178: iload 5
      //   180: istore_3
      //   181: iload 4
      //   183: ifeq +54 -> 237
      //   186: aload 8
      //   188: astore 9
      //   190: iload 5
      //   192: istore_3
      //   193: aload_0
      //   194: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   197: ifnonnull +40 -> 237
      //   200: iconst_1
      //   201: istore_3
      //   202: aload 8
      //   204: astore 9
      //   206: aload 8
      //   208: ifnonnull +13 -> 221
      //   211: new 111	android/os/Bundle
      //   214: astore 9
      //   216: aload 9
      //   218: invokespecial 112	android/os/Bundle:<init>	()V
      //   221: aload 9
      //   223: ldc 120
      //   225: aconst_null
      //   226: invokevirtual 124	android/os/Bundle:putString	(Ljava/lang/String;Ljava/lang/String;)V
      //   229: goto +8 -> 237
      //   232: astore 8
      //   234: goto +250 -> 484
      //   237: aload_0
      //   238: monitorexit
      //   239: invokestatic 127	android/provider/Settings:isInSystemServer	()Z
      //   242: ifeq +55 -> 297
      //   245: invokestatic 132	android/os/Binder:getCallingUid	()I
      //   248: invokestatic 137	android/os/Process:myUid	()I
      //   251: if_icmpeq +46 -> 297
      //   254: invokestatic 141	android/os/Binder:clearCallingIdentity	()J
      //   257: lstore 10
      //   259: aload 7
      //   261: aload_1
      //   262: invokevirtual 147	android/content/ContentResolver:getPackageName	()Ljava/lang/String;
      //   265: aload_0
      //   266: getfield 51	android/provider/Settings$NameValueCache:mCallGetCommand	Ljava/lang/String;
      //   269: aload_2
      //   270: aload 9
      //   272: invokeinterface 153 5 0
      //   277: astore 8
      //   279: lload 10
      //   281: invokestatic 157	android/os/Binder:restoreCallingIdentity	(J)V
      //   284: goto +33 -> 317
      //   287: astore 8
      //   289: lload 10
      //   291: invokestatic 157	android/os/Binder:restoreCallingIdentity	(J)V
      //   294: aload 8
      //   296: athrow
      //   297: aload 7
      //   299: aload_1
      //   300: invokevirtual 147	android/content/ContentResolver:getPackageName	()Ljava/lang/String;
      //   303: aload_0
      //   304: getfield 51	android/provider/Settings$NameValueCache:mCallGetCommand	Ljava/lang/String;
      //   307: aload_2
      //   308: aload 9
      //   310: invokeinterface 153 5 0
      //   315: astore 8
      //   317: aload 8
      //   319: ifnull +162 -> 481
      //   322: aload 8
      //   324: ldc 35
      //   326: invokevirtual 161	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   329: astore 12
      //   331: iload 4
      //   333: ifeq +145 -> 478
      //   336: aload_0
      //   337: monitorenter
      //   338: iload_3
      //   339: ifeq +99 -> 438
      //   342: aload 8
      //   344: ldc 120
      //   346: invokevirtual 165	android/os/Bundle:getParcelable	(Ljava/lang/String;)Landroid/os/Parcelable;
      //   349: checkcast 167	android/util/MemoryIntArray
      //   352: astore 9
      //   354: aload 8
      //   356: ldc -87
      //   358: iconst_m1
      //   359: invokevirtual 173	android/os/Bundle:getInt	(Ljava/lang/String;I)I
      //   362: istore_3
      //   363: aload 9
      //   365: ifnull +73 -> 438
      //   368: iload_3
      //   369: iflt +69 -> 438
      //   372: aload 8
      //   374: ldc -81
      //   376: iconst_0
      //   377: invokevirtual 173	android/os/Bundle:getInt	(Ljava/lang/String;I)I
      //   380: istore 4
      //   382: aload_0
      //   383: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   386: ifnull +10 -> 396
      //   389: aload_0
      //   390: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   393: invokevirtual 74	android/provider/Settings$GenerationTracker:destroy	()V
      //   396: new 71	android/provider/Settings$GenerationTracker
      //   399: astore 13
      //   401: new 177	android/provider/_$$Lambda$Settings$NameValueCache$qSyMM6rUAHCa_5rsP_atfAqR3sA
      //   404: astore 8
      //   406: aload 8
      //   408: aload_0
      //   409: invokespecial 179	android/provider/_$$Lambda$Settings$NameValueCache$qSyMM6rUAHCa_5rsP_atfAqR3sA:<init>	(Landroid/provider/Settings$NameValueCache;)V
      //   412: aload 13
      //   414: aload 9
      //   416: iload_3
      //   417: iload 4
      //   419: aload 8
      //   421: invokespecial 182	android/provider/Settings$GenerationTracker:<init>	(Landroid/util/MemoryIntArray;IILjava/lang/Runnable;)V
      //   424: aload_0
      //   425: aload 13
      //   427: putfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   430: goto +8 -> 438
      //   433: astore 8
      //   435: goto +38 -> 473
      //   438: aload_0
      //   439: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   442: ifnull +26 -> 468
      //   445: iload 6
      //   447: aload_0
      //   448: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   451: invokevirtual 103	android/provider/Settings$GenerationTracker:getCurrentGeneration	()I
      //   454: if_icmpne +14 -> 468
      //   457: aload_0
      //   458: getfield 47	android/provider/Settings$NameValueCache:mValues	Ljava/util/HashMap;
      //   461: aload_2
      //   462: aload 12
      //   464: invokevirtual 186	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   467: pop
      //   468: aload_0
      //   469: monitorexit
      //   470: goto +8 -> 478
      //   473: aload_0
      //   474: monitorexit
      //   475: aload 8
      //   477: athrow
      //   478: aload 12
      //   480: areturn
      //   481: goto +8 -> 489
      //   484: aload_0
      //   485: monitorexit
      //   486: aload 8
      //   488: athrow
      //   489: ldc 14
      //   491: iconst_1
      //   492: anewarray 34	java/lang/String
      //   495: dup
      //   496: iconst_0
      //   497: aload_2
      //   498: aastore
      //   499: aconst_null
      //   500: invokestatic 190	android/content/ContentResolver:createSqlQueryBundle	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/os/Bundle;
      //   503: astore 9
      //   505: invokestatic 127	android/provider/Settings:isInSystemServer	()Z
      //   508: istore 14
      //   510: iload 14
      //   512: ifeq +110 -> 622
      //   515: invokestatic 132	android/os/Binder:getCallingUid	()I
      //   518: invokestatic 137	android/os/Process:myUid	()I
      //   521: if_icmpeq +101 -> 622
      //   524: invokestatic 141	android/os/Binder:clearCallingIdentity	()J
      //   527: lstore 10
      //   529: aload_1
      //   530: invokevirtual 147	android/content/ContentResolver:getPackageName	()Ljava/lang/String;
      //   533: astore_1
      //   534: aload_0
      //   535: getfield 49	android/provider/Settings$NameValueCache:mUri	Landroid/net/Uri;
      //   538: astore 8
      //   540: getstatic 37	android/provider/Settings$NameValueCache:SELECT_VALUE_PROJECTION	[Ljava/lang/String;
      //   543: astore 12
      //   545: aload 7
      //   547: aload_1
      //   548: aload 8
      //   550: aload 12
      //   552: aload 9
      //   554: aconst_null
      //   555: invokeinterface 194 6 0
      //   560: astore 9
      //   562: lload 10
      //   564: invokestatic 157	android/os/Binder:restoreCallingIdentity	(J)V
      //   567: aload 9
      //   569: astore_1
      //   570: goto +87 -> 657
      //   573: astore_1
      //   574: aload 9
      //   576: astore_2
      //   577: goto +401 -> 978
      //   580: astore 8
      //   582: aload 9
      //   584: astore_1
      //   585: goto +300 -> 885
      //   588: astore_1
      //   589: goto +4 -> 593
      //   592: astore_1
      //   593: lload 10
      //   595: invokestatic 157	android/os/Binder:restoreCallingIdentity	(J)V
      //   598: aload_1
      //   599: athrow
      //   600: astore_1
      //   601: goto +243 -> 844
      //   604: astore 8
      //   606: goto +245 -> 851
      //   609: astore_1
      //   610: aconst_null
      //   611: astore_2
      //   612: goto +366 -> 978
      //   615: astore 8
      //   617: aconst_null
      //   618: astore_1
      //   619: goto +266 -> 885
      //   622: aconst_null
      //   623: astore 8
      //   625: aload_1
      //   626: invokevirtual 147	android/content/ContentResolver:getPackageName	()Ljava/lang/String;
      //   629: astore_1
      //   630: aload_0
      //   631: getfield 49	android/provider/Settings$NameValueCache:mUri	Landroid/net/Uri;
      //   634: astore 12
      //   636: getstatic 37	android/provider/Settings$NameValueCache:SELECT_VALUE_PROJECTION	[Ljava/lang/String;
      //   639: astore 13
      //   641: aload 7
      //   643: aload_1
      //   644: aload 12
      //   646: aload 13
      //   648: aload 9
      //   650: aconst_null
      //   651: invokeinterface 194 6 0
      //   656: astore_1
      //   657: aload_1
      //   658: ifnonnull +95 -> 753
      //   661: aload_1
      //   662: astore 9
      //   664: new 196	java/lang/StringBuilder
      //   667: astore 8
      //   669: aload_1
      //   670: astore 9
      //   672: aload 8
      //   674: invokespecial 197	java/lang/StringBuilder:<init>	()V
      //   677: aload_1
      //   678: astore 9
      //   680: aload 8
      //   682: ldc -57
      //   684: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   687: pop
      //   688: aload_1
      //   689: astore 9
      //   691: aload 8
      //   693: aload_2
      //   694: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   697: pop
      //   698: aload_1
      //   699: astore 9
      //   701: aload 8
      //   703: ldc -51
      //   705: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   708: pop
      //   709: aload_1
      //   710: astore 9
      //   712: aload 8
      //   714: aload_0
      //   715: getfield 49	android/provider/Settings$NameValueCache:mUri	Landroid/net/Uri;
      //   718: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   721: pop
      //   722: aload_1
      //   723: astore 9
      //   725: ldc 59
      //   727: aload 8
      //   729: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   732: invokestatic 214	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
      //   735: pop
      //   736: aload_1
      //   737: ifnull +9 -> 746
      //   740: aload_1
      //   741: invokeinterface 219 1 0
      //   746: aconst_null
      //   747: areturn
      //   748: astore 8
      //   750: goto +135 -> 885
      //   753: aload_1
      //   754: astore 9
      //   756: aload_1
      //   757: invokeinterface 222 1 0
      //   762: ifeq +18 -> 780
      //   765: aload_1
      //   766: astore 9
      //   768: aload_1
      //   769: iconst_0
      //   770: invokeinterface 225 2 0
      //   775: astore 8
      //   777: goto +6 -> 783
      //   780: aconst_null
      //   781: astore 8
      //   783: aload_1
      //   784: astore 9
      //   786: aload_0
      //   787: monitorenter
      //   788: aload_0
      //   789: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   792: ifnull +26 -> 818
      //   795: iload 6
      //   797: aload_0
      //   798: getfield 69	android/provider/Settings$NameValueCache:mGenerationTracker	Landroid/provider/Settings$GenerationTracker;
      //   801: invokevirtual 103	android/provider/Settings$GenerationTracker:getCurrentGeneration	()I
      //   804: if_icmpne +14 -> 818
      //   807: aload_0
      //   808: getfield 47	android/provider/Settings$NameValueCache:mValues	Ljava/util/HashMap;
      //   811: aload_2
      //   812: aload 8
      //   814: invokevirtual 186	java/util/HashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
      //   817: pop
      //   818: aload_0
      //   819: monitorexit
      //   820: aload_1
      //   821: ifnull +9 -> 830
      //   824: aload_1
      //   825: invokeinterface 219 1 0
      //   830: aload 8
      //   832: areturn
      //   833: astore 8
      //   835: aload_0
      //   836: monitorexit
      //   837: aload_1
      //   838: astore 9
      //   840: aload 8
      //   842: athrow
      //   843: astore_1
      //   844: aconst_null
      //   845: astore_2
      //   846: goto +132 -> 978
      //   849: astore 8
      //   851: aconst_null
      //   852: astore_1
      //   853: goto +32 -> 885
      //   856: astore_1
      //   857: aload 8
      //   859: astore_2
      //   860: goto +118 -> 978
      //   863: astore 9
      //   865: aload 8
      //   867: astore_1
      //   868: aload 9
      //   870: astore 8
      //   872: goto +13 -> 885
      //   875: astore_1
      //   876: aconst_null
      //   877: astore_2
      //   878: goto +100 -> 978
      //   881: astore 8
      //   883: aconst_null
      //   884: astore_1
      //   885: aload_1
      //   886: astore 9
      //   888: new 196	java/lang/StringBuilder
      //   891: astore 7
      //   893: aload_1
      //   894: astore 9
      //   896: aload 7
      //   898: invokespecial 197	java/lang/StringBuilder:<init>	()V
      //   901: aload_1
      //   902: astore 9
      //   904: aload 7
      //   906: ldc -57
      //   908: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   911: pop
      //   912: aload_1
      //   913: astore 9
      //   915: aload 7
      //   917: aload_2
      //   918: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   921: pop
      //   922: aload_1
      //   923: astore 9
      //   925: aload 7
      //   927: ldc -51
      //   929: invokevirtual 203	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   932: pop
      //   933: aload_1
      //   934: astore 9
      //   936: aload 7
      //   938: aload_0
      //   939: getfield 49	android/provider/Settings$NameValueCache:mUri	Landroid/net/Uri;
      //   942: invokevirtual 208	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   945: pop
      //   946: aload_1
      //   947: astore 9
      //   949: ldc 59
      //   951: aload 7
      //   953: invokevirtual 211	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   956: aload 8
      //   958: invokestatic 228	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   961: pop
      //   962: aload_1
      //   963: ifnull +9 -> 972
      //   966: aload_1
      //   967: invokeinterface 219 1 0
      //   972: aconst_null
      //   973: areturn
      //   974: astore_1
      //   975: aload 9
      //   977: astore_2
      //   978: aload_2
      //   979: ifnull +9 -> 988
      //   982: aload_2
      //   983: invokeinterface 219 1 0
      //   988: aload_1
      //   989: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	990	0	this	NameValueCache
      //   0	990	1	paramContentResolver	ContentResolver
      //   0	990	2	paramString	String
      //   0	990	3	paramInt	int
      //   8	410	4	i	int
      //   17	174	5	j	int
      //   21	784	6	k	int
      //   126	826	7	localObject1	Object
      //   136	18	8	localBundle1	Bundle
      //   164	43	8	localRemoteException1	RemoteException
      //   232	1	8	localObject2	Object
      //   277	1	8	localBundle2	Bundle
      //   287	8	8	localObject3	Object
      //   315	105	8	localObject4	Object
      //   433	54	8	localObject5	Object
      //   538	11	8	localUri	Uri
      //   580	1	8	localRemoteException2	RemoteException
      //   604	1	8	localRemoteException3	RemoteException
      //   615	1	8	localRemoteException4	RemoteException
      //   623	105	8	localStringBuilder	StringBuilder
      //   748	1	8	localRemoteException5	RemoteException
      //   775	56	8	str	String
      //   833	8	8	localObject6	Object
      //   849	17	8	localRemoteException6	RemoteException
      //   870	1	8	localObject7	Object
      //   881	76	8	localRemoteException7	RemoteException
      //   176	663	9	localObject8	Object
      //   863	6	9	localRemoteException8	RemoteException
      //   886	90	9	localContentResolver	ContentResolver
      //   257	337	10	l	long
      //   329	316	12	localObject9	Object
      //   399	248	13	localObject10	Object
      //   508	3	14	bool	boolean
      // Exception table:
      //   from	to	target	type
      //   34	58	113	finally
      //   61	86	113	finally
      //   92	108	113	finally
      //   108	110	113	finally
      //   114	116	113	finally
      //   143	153	164	android/os/RemoteException
      //   153	161	164	android/os/RemoteException
      //   172	174	164	android/os/RemoteException
      //   239	259	164	android/os/RemoteException
      //   279	284	164	android/os/RemoteException
      //   289	297	164	android/os/RemoteException
      //   297	317	164	android/os/RemoteException
      //   322	331	164	android/os/RemoteException
      //   336	338	164	android/os/RemoteException
      //   475	478	164	android/os/RemoteException
      //   486	489	164	android/os/RemoteException
      //   193	200	232	finally
      //   211	221	232	finally
      //   221	229	232	finally
      //   237	239	232	finally
      //   484	486	232	finally
      //   259	279	287	finally
      //   342	363	433	finally
      //   372	396	433	finally
      //   396	430	433	finally
      //   438	468	433	finally
      //   468	470	433	finally
      //   473	475	433	finally
      //   562	567	573	finally
      //   562	567	580	android/os/RemoteException
      //   545	562	588	finally
      //   529	545	592	finally
      //   593	600	600	finally
      //   593	600	604	android/os/RemoteException
      //   515	529	609	finally
      //   515	529	615	android/os/RemoteException
      //   664	669	748	android/os/RemoteException
      //   672	677	748	android/os/RemoteException
      //   680	688	748	android/os/RemoteException
      //   691	698	748	android/os/RemoteException
      //   701	709	748	android/os/RemoteException
      //   712	722	748	android/os/RemoteException
      //   725	736	748	android/os/RemoteException
      //   756	765	748	android/os/RemoteException
      //   768	777	748	android/os/RemoteException
      //   786	788	748	android/os/RemoteException
      //   840	843	748	android/os/RemoteException
      //   788	818	833	finally
      //   818	820	833	finally
      //   835	837	833	finally
      //   641	657	843	finally
      //   641	657	849	android/os/RemoteException
      //   625	641	856	finally
      //   625	641	863	android/os/RemoteException
      //   489	510	875	finally
      //   489	510	881	android/os/RemoteException
      //   664	669	974	finally
      //   672	677	974	finally
      //   680	688	974	finally
      //   691	698	974	finally
      //   701	709	974	finally
      //   712	722	974	finally
      //   725	736	974	finally
      //   756	765	974	finally
      //   768	777	974	finally
      //   786	788	974	finally
      //   840	843	974	finally
      //   888	893	974	finally
      //   896	901	974	finally
      //   904	912	974	finally
      //   915	922	974	finally
      //   925	933	974	finally
      //   936	946	974	finally
      //   949	962	974	finally
    }
    
    public boolean putStringForUser(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, boolean paramBoolean, int paramInt)
    {
      try
      {
        Bundle localBundle = new android/os/Bundle;
        localBundle.<init>();
        localBundle.putString("value", paramString2);
        localBundle.putInt("_user", paramInt);
        if (paramString3 != null) {
          localBundle.putString("_tag", paramString3);
        }
        if (paramBoolean) {
          localBundle.putBoolean("_make_default", true);
        }
        mProviderHolder.getProvider(paramContentResolver).call(paramContentResolver.getPackageName(), mCallSetCommand, paramString1, localBundle);
        return true;
      }
      catch (RemoteException paramContentResolver)
      {
        paramString2 = new StringBuilder();
        paramString2.append("Can't set key ");
        paramString2.append(paramString1);
        paramString2.append(" in ");
        paramString2.append(mUri);
        Log.w("Settings", paramString2.toString(), paramContentResolver);
      }
      return false;
    }
  }
  
  public static class NameValueTable
    implements BaseColumns
  {
    public static final String NAME = "name";
    public static final String VALUE = "value";
    
    public NameValueTable() {}
    
    public static Uri getUriFor(Uri paramUri, String paramString)
    {
      return Uri.withAppendedPath(paramUri, paramString);
    }
    
    protected static boolean putString(ContentResolver paramContentResolver, Uri paramUri, String paramString1, String paramString2)
    {
      try
      {
        ContentValues localContentValues = new android/content/ContentValues;
        localContentValues.<init>();
        localContentValues.put("name", paramString1);
        localContentValues.put("value", paramString2);
        paramContentResolver.insert(paramUri, localContentValues);
        return true;
      }
      catch (SQLException paramString2)
      {
        paramContentResolver = new StringBuilder();
        paramContentResolver.append("Can't set key ");
        paramContentResolver.append(paramString1);
        paramContentResolver.append(" in ");
        paramContentResolver.append(paramUri);
        Log.w("Settings", paramContentResolver.toString(), paramString2);
      }
      return false;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ResetMode {}
  
  public static final class Secure
    extends Settings.NameValueTable
  {
    public static final String ACCESSIBILITY_AUTOCLICK_DELAY = "accessibility_autoclick_delay";
    private static final SettingsValidators.Validator ACCESSIBILITY_AUTOCLICK_DELAY_VALIDATOR;
    public static final String ACCESSIBILITY_AUTOCLICK_ENABLED = "accessibility_autoclick_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_AUTOCLICK_ENABLED_VALIDATOR;
    public static final String ACCESSIBILITY_BUTTON_TARGET_COMPONENT = "accessibility_button_target_component";
    private static final SettingsValidators.Validator ACCESSIBILITY_BUTTON_TARGET_COMPONENT_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR = "accessibility_captioning_background_color";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_EDGE_COLOR = "accessibility_captioning_edge_color";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_EDGE_COLOR_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_EDGE_TYPE = "accessibility_captioning_edge_type";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_EDGE_TYPE_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_ENABLED = "accessibility_captioning_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_ENABLED_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_FONT_SCALE = "accessibility_captioning_font_scale";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_FONT_SCALE_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR = "accessibility_captioning_foreground_color";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_LOCALE = "accessibility_captioning_locale";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_LOCALE_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_PRESET = "accessibility_captioning_preset";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_PRESET_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_TYPEFACE = "accessibility_captioning_typeface";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_TYPEFACE_VALIDATOR;
    public static final String ACCESSIBILITY_CAPTIONING_WINDOW_COLOR = "accessibility_captioning_window_color";
    private static final SettingsValidators.Validator ACCESSIBILITY_CAPTIONING_WINDOW_COLOR_VALIDATOR;
    public static final String ACCESSIBILITY_DISPLAY_DALTONIZER = "accessibility_display_daltonizer";
    public static final String ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED_VALIDATOR;
    private static final SettingsValidators.Validator ACCESSIBILITY_DISPLAY_DALTONIZER_VALIDATOR;
    public static final String ACCESSIBILITY_DISPLAY_INVERSION_ENABLED = "accessibility_display_inversion_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_DISPLAY_INVERSION_ENABLED_VALIDATOR;
    @Deprecated
    public static final String ACCESSIBILITY_DISPLAY_MAGNIFICATION_AUTO_UPDATE = "accessibility_display_magnification_auto_update";
    public static final String ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED = "accessibility_display_magnification_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED_VALIDATOR;
    public static final String ACCESSIBILITY_DISPLAY_MAGNIFICATION_NAVBAR_ENABLED = "accessibility_display_magnification_navbar_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_DISPLAY_MAGNIFICATION_NAVBAR_ENABLED_VALIDATOR;
    public static final String ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE = "accessibility_display_magnification_scale";
    private static final SettingsValidators.Validator ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE_VALIDATOR;
    public static final String ACCESSIBILITY_ENABLED = "accessibility_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_ENABLED_VALIDATOR;
    public static final String ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED = "high_text_contrast_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED_VALIDATOR;
    public static final String ACCESSIBILITY_LARGE_POINTER_ICON = "accessibility_large_pointer_icon";
    private static final SettingsValidators.Validator ACCESSIBILITY_LARGE_POINTER_ICON_VALIDATOR;
    public static final String ACCESSIBILITY_ONEHAND_CTRL_CLING_DISMISSED = "accessibility_onehand_ctrl_cling_dismissed";
    public static final String ACCESSIBILITY_ONEHAND_CTRL_ENABLED = "accessibility_onehand_ctrl_enabled";
    public static final String ACCESSIBILITY_ONEHAND_CTRL_POSITION = "accessibility_onehand_ctrl_position";
    public static final String ACCESSIBILITY_ONEHAND_CTRL_QUICK_TRIGGER_ENABLED = "accessibility_onehand_ctrl_quick_trigger_enabled";
    public static final String ACCESSIBILITY_ONEHAND_CTRL_SCALE = "accessibility_onehand_ctrl_scale";
    public static final String ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN = "accessibility_shortcut_dialog_shown";
    private static final SettingsValidators.Validator ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN_VALIDATOR;
    public static final String ACCESSIBILITY_SHORTCUT_ENABLED = "accessibility_shortcut_enabled";
    private static final SettingsValidators.Validator ACCESSIBILITY_SHORTCUT_ENABLED_VALIDATOR;
    public static final String ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN = "accessibility_shortcut_on_lock_screen";
    private static final SettingsValidators.Validator ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN_VALIDATOR;
    public static final String ACCESSIBILITY_SHORTCUT_TARGET_SERVICE = "accessibility_shortcut_target_service";
    private static final SettingsValidators.Validator ACCESSIBILITY_SHORTCUT_TARGET_SERVICE_VALIDATOR;
    public static final String ACCESSIBILITY_SOFT_KEYBOARD_MODE = "accessibility_soft_keyboard_mode";
    @Deprecated
    public static final String ACCESSIBILITY_SPEAK_PASSWORD = "speak_password";
    private static final SettingsValidators.Validator ACCESSIBILITY_SPEAK_PASSWORD_VALIDATOR;
    @Deprecated
    public static final String ADB_ENABLED = "adb_enabled";
    public static final String ALLOWED_GEOLOCATION_ORIGINS = "allowed_geolocation_origins";
    @Deprecated
    public static final String ALLOW_MOCK_LOCATION = "mock_location";
    private static final SettingsValidators.Validator ALLOW_MOCK_LOCATION_VALIDATOR;
    public static final String ALWAYS_ON_VPN_APP = "always_on_vpn_app";
    public static final String ALWAYS_ON_VPN_LOCKDOWN = "always_on_vpn_lockdown";
    public static final String ANDROID_ID = "android_id";
    public static final String ANR_SHOW_BACKGROUND = "anr_show_background";
    public static final String ASSISTANT = "assistant";
    public static final String ASSIST_DISCLOSURE_ENABLED = "assist_disclosure_enabled";
    public static final String ASSIST_GESTURE_ENABLED = "assist_gesture_enabled";
    private static final SettingsValidators.Validator ASSIST_GESTURE_ENABLED_VALIDATOR;
    public static final String ASSIST_GESTURE_SENSITIVITY = "assist_gesture_sensitivity";
    private static final SettingsValidators.Validator ASSIST_GESTURE_SENSITIVITY_VALIDATOR;
    public static final String ASSIST_GESTURE_SETUP_COMPLETE = "assist_gesture_setup_complete";
    private static final SettingsValidators.Validator ASSIST_GESTURE_SETUP_COMPLETE_VALIDATOR;
    public static final String ASSIST_GESTURE_SILENCE_ALERTS_ENABLED = "assist_gesture_silence_alerts_enabled";
    private static final SettingsValidators.Validator ASSIST_GESTURE_SILENCE_ALERTS_ENABLED_VALIDATOR;
    public static final String ASSIST_GESTURE_WAKE_ENABLED = "assist_gesture_wake_enabled";
    private static final SettingsValidators.Validator ASSIST_GESTURE_WAKE_ENABLED_VALIDATOR;
    public static final String ASSIST_SCREENSHOT_ENABLED = "assist_screenshot_enabled";
    public static final String ASSIST_STRUCTURE_ENABLED = "assist_structure_enabled";
    public static final String ASUS_LOCKSCREEN_DISPLAY_APP = "asus.lockscreen.display.app";
    public static final String ASUS_LOCKSCREEN_INSTANT_CAMERA = "asus_lockscreen_instant_camera";
    public static final String ASUS_LOCKSCREEN_INSTANT_CAMERA_DEFAULT = "asus_lockscreen_instant_camera_defualt";
    @SystemApi
    public static final String AUTOFILL_FEATURE_FIELD_CLASSIFICATION = "autofill_field_classification";
    public static final String AUTOFILL_SERVICE = "autofill_service";
    public static final String AUTOFILL_SERVICE_SEARCH_URI = "autofill_service_search_uri";
    private static final SettingsValidators.Validator AUTOFILL_SERVICE_VALIDATOR;
    @SystemApi
    public static final String AUTOFILL_USER_DATA_MAX_CATEGORY_COUNT = "autofill_user_data_max_category_count";
    @SystemApi
    public static final String AUTOFILL_USER_DATA_MAX_FIELD_CLASSIFICATION_IDS_SIZE = "autofill_user_data_max_field_classification_size";
    @SystemApi
    public static final String AUTOFILL_USER_DATA_MAX_USER_DATA_SIZE = "autofill_user_data_max_user_data_size";
    @SystemApi
    public static final String AUTOFILL_USER_DATA_MAX_VALUE_LENGTH = "autofill_user_data_max_value_length";
    @SystemApi
    public static final String AUTOFILL_USER_DATA_MIN_VALUE_LENGTH = "autofill_user_data_min_value_length";
    public static final String AUTOMATIC_STORAGE_MANAGER_BYTES_CLEARED = "automatic_storage_manager_bytes_cleared";
    public static final String AUTOMATIC_STORAGE_MANAGER_DAYS_TO_RETAIN = "automatic_storage_manager_days_to_retain";
    public static final int AUTOMATIC_STORAGE_MANAGER_DAYS_TO_RETAIN_DEFAULT = 90;
    private static final SettingsValidators.Validator AUTOMATIC_STORAGE_MANAGER_DAYS_TO_RETAIN_VALIDATOR;
    public static final String AUTOMATIC_STORAGE_MANAGER_ENABLED = "automatic_storage_manager_enabled";
    public static final String AUTOMATIC_STORAGE_MANAGER_LAST_RUN = "automatic_storage_manager_last_run";
    public static final String AUTOMATIC_STORAGE_MANAGER_TURNED_OFF_BY_POLICY = "automatic_storage_manager_turned_off_by_policy";
    @Deprecated
    public static final String BACKGROUND_DATA = "background_data";
    public static final String BACKUP_AUTO_RESTORE = "backup_auto_restore";
    public static final String BACKUP_ENABLED = "backup_enabled";
    public static final String BACKUP_LOCAL_TRANSPORT_PARAMETERS = "backup_local_transport_parameters";
    public static final String BACKUP_MANAGER_CONSTANTS = "backup_manager_constants";
    public static final String BACKUP_PROVISIONED = "backup_provisioned";
    public static final String BACKUP_TRANSPORT = "backup_transport";
    @Deprecated
    public static final String BLUETOOTH_ON = "bluetooth_on";
    private static final SettingsValidators.Validator BLUETOOTH_ON_VALIDATOR;
    public static final String BLUETOOTH_ON_WHILE_DRIVING = "bluetooth_on_while_driving";
    @Deprecated
    public static final String BUGREPORT_IN_POWER_MENU = "bugreport_in_power_menu";
    private static final SettingsValidators.Validator BUGREPORT_IN_POWER_MENU_VALIDATOR;
    public static final String CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED = "camera_double_tap_power_gesture_disabled";
    private static final SettingsValidators.Validator CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED_VALIDATOR;
    public static final String CAMERA_DOUBLE_TWIST_TO_FLIP_ENABLED = "camera_double_twist_to_flip_enabled";
    private static final SettingsValidators.Validator CAMERA_DOUBLE_TWIST_TO_FLIP_ENABLED_VALIDATOR;
    public static final String CAMERA_GESTURE_DISABLED = "camera_gesture_disabled";
    private static final SettingsValidators.Validator CAMERA_GESTURE_DISABLED_VALIDATOR;
    public static final String CAMERA_LIFT_TRIGGER_ENABLED = "camera_lift_trigger_enabled";
    public static final int CAMERA_LIFT_TRIGGER_ENABLED_DEFAULT = 1;
    public static final String CARRIER_APPS_HANDLED = "carrier_apps_handled";
    private static final Set<String> CLONE_TO_MANAGED_PROFILE;
    public static final String CMAS_ADDITIONAL_BROADCAST_PKG = "cmas_additional_broadcast_pkg";
    public static final String COMPLETED_CATEGORY_PREFIX = "suggested.completed_category.";
    public static final String CONNECTIVITY_RELEASE_PENDING_INTENT_DELAY_MS = "connectivity_release_pending_intent_delay_ms";
    public static final Uri CONTENT_URI = Uri.parse("content://settings/secure");
    @Deprecated
    public static final String DATA_ROAMING = "data_roaming";
    public static final String DEFAULT_INPUT_METHOD = "default_input_method";
    @Deprecated
    public static final String DEVELOPMENT_SETTINGS_ENABLED = "development_settings_enabled";
    public static final String DEVICE_PAIRED = "device_paired";
    @Deprecated
    public static final String DEVICE_PROVISIONED = "device_provisioned";
    public static final String DIALER_DEFAULT_APPLICATION = "dialer_default_application";
    public static final String DISABLED_PRINT_SERVICES = "disabled_print_services";
    public static final String DISABLED_SYSTEM_INPUT_METHODS = "disabled_system_input_methods";
    public static final String DISPLAY_DENSITY_FORCED = "display_density_forced";
    public static final String DOUBLE_TAP_TO_WAKE = "double_tap_to_wake";
    private static final SettingsValidators.Validator DOUBLE_TAP_TO_WAKE_VALIDATOR;
    public static final String DOZE_ALWAYS_ON = "doze_always_on";
    public static final String DOZE_ENABLED = "doze_enabled";
    private static final SettingsValidators.Validator DOZE_ENABLED_VALIDATOR;
    public static final String DOZE_PULSE_ON_DOUBLE_TAP = "doze_pulse_on_double_tap";
    private static final SettingsValidators.Validator DOZE_PULSE_ON_DOUBLE_TAP_VALIDATOR;
    public static final String DOZE_PULSE_ON_LONG_PRESS = "doze_pulse_on_long_press";
    public static final String DOZE_PULSE_ON_PICK_UP = "doze_pulse_on_pick_up";
    private static final SettingsValidators.Validator DOZE_PULSE_ON_PICK_UP_VALIDATOR;
    public static final String EMERGENCY_ASSISTANCE_APPLICATION = "emergency_assistance_application";
    public static final String ENABLED_ACCESSIBILITY_SERVICES = "enabled_accessibility_services";
    private static final SettingsValidators.Validator ENABLED_ACCESSIBILITY_SERVICES_VALIDATOR;
    public static final String ENABLED_INPUT_METHODS = "enabled_input_methods";
    @Deprecated
    public static final String ENABLED_NOTIFICATION_ASSISTANT = "enabled_notification_assistant";
    private static final SettingsValidators.Validator ENABLED_NOTIFICATION_ASSISTANT_VALIDATOR;
    @Deprecated
    public static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final SettingsValidators.Validator ENABLED_NOTIFICATION_LISTENERS_VALIDATOR;
    @Deprecated
    public static final String ENABLED_NOTIFICATION_POLICY_ACCESS_PACKAGES = "enabled_notification_policy_access_packages";
    private static final SettingsValidators.Validator ENABLED_NOTIFICATION_POLICY_ACCESS_PACKAGES_VALIDATOR;
    public static final String ENABLED_PRINT_SERVICES = "enabled_print_services";
    public static final String ENABLED_VR_LISTENERS = "enabled_vr_listeners";
    private static final SettingsValidators.Validator ENABLED_VR_LISTENERS_VALIDATOR;
    public static final String ENHANCED_VOICE_PRIVACY_ENABLED = "enhanced_voice_privacy_enabled";
    private static final SettingsValidators.Validator ENHANCED_VOICE_PRIVACY_ENABLED_VALIDATOR;
    public static final String GAME_GENIE_SCALING_CONTROL = "gg_scaling_ctrl";
    public static final String HIDE_NAVI_BAR_PREF_PROVIDER_KEY = "hide_navi_bar_provider_key";
    @Deprecated
    public static final String HTTP_PROXY = "http_proxy";
    public static final String HUSH_GESTURE_USED = "hush_gesture_used";
    private static final SettingsValidators.Validator HUSH_GESTURE_USED_VALIDATOR;
    public static final String IMMERSIVE_MODE_CONFIRMATIONS = "immersive_mode_confirmations";
    public static final String INCALL_BACK_BUTTON_BEHAVIOR = "incall_back_button_behavior";
    public static final int INCALL_BACK_BUTTON_BEHAVIOR_DEFAULT = 0;
    public static final int INCALL_BACK_BUTTON_BEHAVIOR_HANGUP = 1;
    public static final int INCALL_BACK_BUTTON_BEHAVIOR_NONE = 0;
    public static final String INCALL_POWER_BUTTON_BEHAVIOR = "incall_power_button_behavior";
    public static final int INCALL_POWER_BUTTON_BEHAVIOR_DEFAULT = 1;
    public static final int INCALL_POWER_BUTTON_BEHAVIOR_HANGUP = 2;
    public static final int INCALL_POWER_BUTTON_BEHAVIOR_SCREEN_OFF = 1;
    private static final SettingsValidators.Validator INCALL_POWER_BUTTON_BEHAVIOR_VALIDATOR;
    public static final String INPUT_METHODS_SUBTYPE_HISTORY = "input_methods_subtype_history";
    public static final String INPUT_METHOD_SELECTOR_VISIBILITY = "input_method_selector_visibility";
    public static final String INSTALL_NON_MARKET_APPS = "install_non_market_apps";
    @SystemApi
    public static final String INSTANT_APPS_ENABLED = "instant_apps_enabled";
    public static final Set<String> INSTANT_APP_SETTINGS;
    public static final String KEYGUARD_SLICE_URI = "keyguard_slice_uri";
    public static final String LAST_SETUP_SHOWN = "last_setup_shown";
    public static final String[] LEGACY_RESTORE_SETTINGS;
    public static final String LOCATION_CHANGER = "location_changer";
    public static final int LOCATION_CHANGER_QUICK_SETTINGS = 2;
    public static final int LOCATION_CHANGER_SYSTEM_SETTINGS = 1;
    public static final int LOCATION_CHANGER_UNKNOWN = 0;
    @Deprecated
    public static final String LOCATION_MODE = "location_mode";
    @Deprecated
    public static final int LOCATION_MODE_BATTERY_SAVING = 2;
    @Deprecated
    public static final int LOCATION_MODE_HIGH_ACCURACY = 3;
    @Deprecated
    public static final int LOCATION_MODE_OFF = 0;
    @Deprecated
    public static final int LOCATION_MODE_SENSORS_ONLY = 1;
    @Deprecated
    public static final String LOCATION_PROVIDERS_ALLOWED = "location_providers_allowed";
    public static final String LOCKDOWN_IN_POWER_MENU = "lockdown_in_power_menu";
    private static final SettingsValidators.Validator LOCKDOWN_IN_POWER_MENU_VALIDATOR;
    @Deprecated
    public static final String LOCK_BIOMETRIC_WEAK_FLAGS = "lock_biometric_weak_flags";
    @Deprecated
    public static final String LOCK_PATTERN_ENABLED = "lock_pattern_autolock";
    @Deprecated
    public static final String LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED = "lock_pattern_tactile_feedback_enabled";
    @Deprecated
    public static final String LOCK_PATTERN_VISIBLE = "lock_pattern_visible_pattern";
    public static final String LOCK_SCREEN_ALLOW_PRIVATE_NOTIFICATIONS = "lock_screen_allow_private_notifications";
    public static final String LOCK_SCREEN_ALLOW_REMOTE_INPUT = "lock_screen_allow_remote_input";
    @Deprecated
    public static final String LOCK_SCREEN_APPWIDGET_IDS = "lock_screen_appwidget_ids";
    @Deprecated
    public static final String LOCK_SCREEN_FALLBACK_APPWIDGET_ID = "lock_screen_fallback_appwidget_id";
    public static final String LOCK_SCREEN_LOCK_AFTER_TIMEOUT = "lock_screen_lock_after_timeout";
    @Deprecated
    public static final String LOCK_SCREEN_OWNER_INFO = "lock_screen_owner_info";
    @Deprecated
    public static final String LOCK_SCREEN_OWNER_INFO_ENABLED = "lock_screen_owner_info_enabled";
    public static final String LOCK_SCREEN_SHOW_NOTIFICATIONS = "lock_screen_show_notifications";
    @Deprecated
    public static final String LOCK_SCREEN_STICKY_APPWIDGET = "lock_screen_sticky_appwidget";
    public static final String LOCK_TO_APP_EXIT_LOCKED = "lock_to_app_exit_locked";
    @Deprecated
    public static final String LOGGING_ID = "logging_id";
    public static final String LONG_PRESS_TIMEOUT = "long_press_timeout";
    private static final SettingsValidators.Validator LONG_PRESS_TIMEOUT_VALIDATOR;
    public static final String LOW_POWER_MANUAL_ACTIVATION_COUNT = "low_power_manual_activation_count";
    public static final String LOW_POWER_WARNING_ACKNOWLEDGED = "low_power_warning_acknowledged";
    public static final String MANAGED_PROFILE_CONTACT_REMOTE_SEARCH = "managed_profile_contact_remote_search";
    public static final String MANUAL_RINGER_TOGGLE_COUNT = "manual_ringer_toggle_count";
    private static final SettingsValidators.Validator MANUAL_RINGER_TOGGLE_COUNT_VALIDATOR;
    public static final String MOUNT_PLAY_NOTIFICATION_SND = "mount_play_not_snd";
    private static final SettingsValidators.Validator MOUNT_PLAY_NOTIFICATION_SND_VALIDATOR;
    public static final String MOUNT_UMS_AUTOSTART = "mount_ums_autostart";
    private static final SettingsValidators.Validator MOUNT_UMS_AUTOSTART_VALIDATOR;
    public static final String MOUNT_UMS_NOTIFY_ENABLED = "mount_ums_notify_enabled";
    private static final SettingsValidators.Validator MOUNT_UMS_NOTIFY_ENABLED_VALIDATOR;
    public static final String MOUNT_UMS_PROMPT = "mount_ums_prompt";
    private static final SettingsValidators.Validator MOUNT_UMS_PROMPT_VALIDATOR;
    private static final HashSet<String> MOVED_TO_GLOBAL;
    private static final HashSet<String> MOVED_TO_LOCK_SETTINGS;
    public static final String MULTI_PRESS_TIMEOUT = "multi_press_timeout";
    public static final String NAVIGATION_GESTURE_CONTROL = "navi_gesture_provider_key";
    public static final String NAVIGATION_VISIBILITY_CONTROL = "nav_vis_ctrl";
    public static final String NAVI_GESTURE_PROVIDER_KEY = "navi_gesture_provider_key";
    @Deprecated
    public static final String NETWORK_PREFERENCE = "network_preference";
    public static final String NFC_PAYMENT_DEFAULT_COMPONENT = "nfc_payment_default_component";
    private static final SettingsValidators.Validator NFC_PAYMENT_DEFAULT_COMPONENT_VALIDATOR;
    public static final String NFC_PAYMENT_FOREGROUND = "nfc_payment_foreground";
    public static final String NIGHT_DISPLAY_ACTIVATED = "night_display_activated";
    public static final String NIGHT_DISPLAY_AUTO_MODE = "night_display_auto_mode";
    private static final SettingsValidators.Validator NIGHT_DISPLAY_AUTO_MODE_VALIDATOR;
    public static final String NIGHT_DISPLAY_COLOR_TEMPERATURE = "night_display_color_temperature";
    private static final SettingsValidators.Validator NIGHT_DISPLAY_COLOR_TEMPERATURE_VALIDATOR;
    public static final String NIGHT_DISPLAY_CUSTOM_END_TIME = "night_display_custom_end_time";
    private static final SettingsValidators.Validator NIGHT_DISPLAY_CUSTOM_END_TIME_VALIDATOR;
    public static final String NIGHT_DISPLAY_CUSTOM_START_TIME = "night_display_custom_start_time";
    private static final SettingsValidators.Validator NIGHT_DISPLAY_CUSTOM_START_TIME_VALIDATOR;
    public static final String NIGHT_DISPLAY_LAST_ACTIVATED_TIME = "night_display_last_activated_time";
    public static final String NOTCH_BLACK_STATUSBAR = "notch_black_statusbar";
    public static final String NOTIFICATION_BADGING = "notification_badging";
    private static final SettingsValidators.Validator NOTIFICATION_BADGING_VALIDATOR;
    public static final String NUM_ROTATION_SUGGESTIONS_ACCEPTED = "num_rotation_suggestions_accepted";
    public static final String PACKAGES_TO_CLEAR_DATA_BEFORE_FULL_RESTORE = "packages_to_clear_data_before_full_restore";
    public static final String PACKAGE_VERIFIER_STATE = "package_verifier_state";
    public static final String PACKAGE_VERIFIER_USER_CONSENT = "package_verifier_user_consent";
    public static final String PARENTAL_CONTROL_ENABLED = "parental_control_enabled";
    public static final String PARENTAL_CONTROL_LAST_UPDATE = "parental_control_last_update";
    public static final String PARENTAL_CONTROL_REDIRECT_URL = "parental_control_redirect_url";
    public static final String PAYMENT_SERVICE_SEARCH_URI = "payment_service_search_uri";
    public static final String PREFERRED_TTY_MODE = "preferred_tty_mode";
    private static final SettingsValidators.Validator PREFERRED_TTY_MODE_VALIDATOR;
    public static final String PRINT_SERVICE_SEARCH_URI = "print_service_search_uri";
    public static final String QS_AUTO_ADDED_TILES = "qs_auto_tiles";
    private static final SettingsValidators.Validator QS_AUTO_ADDED_TILES_VALIDATOR;
    public static final String QS_TILES = "sysui_qs_tiles";
    private static final SettingsValidators.Validator QS_TILES_VALIDATOR;
    public static final String RTT_CALLING_MODE = "rtt_calling_mode";
    private static final SettingsValidators.Validator RTT_CALLING_MODE_VALIDATOR;
    public static final String SCREENSAVER_ACTIVATE_ON_DOCK = "screensaver_activate_on_dock";
    private static final SettingsValidators.Validator SCREENSAVER_ACTIVATE_ON_DOCK_VALIDATOR;
    public static final String SCREENSAVER_ACTIVATE_ON_SLEEP = "screensaver_activate_on_sleep";
    private static final SettingsValidators.Validator SCREENSAVER_ACTIVATE_ON_SLEEP_VALIDATOR;
    public static final String SCREENSAVER_COMPONENTS = "screensaver_components";
    private static final SettingsValidators.Validator SCREENSAVER_COMPONENTS_VALIDATOR;
    public static final String SCREENSAVER_DEFAULT_COMPONENT = "screensaver_default_component";
    public static final String SCREENSAVER_ENABLED = "screensaver_enabled";
    private static final SettingsValidators.Validator SCREENSAVER_ENABLED_VALIDATOR;
    public static final String SCREEN_SCALING_PREF_PROVIDER_KEY = "screen_scaling_provider_key";
    public static final String SEARCH_GLOBAL_SEARCH_ACTIVITY = "search_global_search_activity";
    public static final String SEARCH_MAX_RESULTS_PER_SOURCE = "search_max_results_per_source";
    public static final String SEARCH_MAX_RESULTS_TO_DISPLAY = "search_max_results_to_display";
    public static final String SEARCH_MAX_SHORTCUTS_RETURNED = "search_max_shortcuts_returned";
    public static final String SEARCH_MAX_SOURCE_EVENT_AGE_MILLIS = "search_max_source_event_age_millis";
    public static final String SEARCH_MAX_STAT_AGE_MILLIS = "search_max_stat_age_millis";
    public static final String SEARCH_MIN_CLICKS_FOR_SOURCE_RANKING = "search_min_clicks_for_source_ranking";
    public static final String SEARCH_MIN_IMPRESSIONS_FOR_SOURCE_RANKING = "search_min_impressions_for_source_ranking";
    public static final String SEARCH_NUM_PROMOTED_SOURCES = "search_num_promoted_sources";
    public static final String SEARCH_PER_SOURCE_CONCURRENT_QUERY_LIMIT = "search_per_source_concurrent_query_limit";
    public static final String SEARCH_PREFILL_MILLIS = "search_prefill_millis";
    public static final String SEARCH_PROMOTED_SOURCE_DEADLINE_MILLIS = "search_promoted_source_deadline_millis";
    public static final String SEARCH_QUERY_THREAD_CORE_POOL_SIZE = "search_query_thread_core_pool_size";
    public static final String SEARCH_QUERY_THREAD_MAX_POOL_SIZE = "search_query_thread_max_pool_size";
    public static final String SEARCH_SHORTCUT_REFRESH_CORE_POOL_SIZE = "search_shortcut_refresh_core_pool_size";
    public static final String SEARCH_SHORTCUT_REFRESH_MAX_POOL_SIZE = "search_shortcut_refresh_max_pool_size";
    public static final String SEARCH_SOURCE_TIMEOUT_MILLIS = "search_source_timeout_millis";
    public static final String SEARCH_THREAD_KEEPALIVE_SECONDS = "search_thread_keepalive_seconds";
    public static final String SEARCH_WEB_RESULTS_OVERRIDE_LIMIT = "search_web_results_override_limit";
    public static final String SELECTED_INPUT_METHOD_SUBTYPE = "selected_input_method_subtype";
    public static final String SELECTED_SPELL_CHECKER = "selected_spell_checker";
    public static final String SELECTED_SPELL_CHECKER_SUBTYPE = "selected_spell_checker_subtype";
    private static final SettingsValidators.Validator SELECTED_SPELL_CHECKER_SUBTYPE_VALIDATOR;
    private static final SettingsValidators.Validator SELECTED_SPELL_CHECKER_VALIDATOR;
    public static final String SETTINGS_CLASSNAME = "settings_classname";
    public static final String[] SETTINGS_TO_BACKUP;
    public static final String SHOW_FIRST_CRASH_DIALOG_DEV_OPTION = "show_first_crash_dialog_dev_option";
    private static final SettingsValidators.Validator SHOW_FIRST_CRASH_DIALOG_DEV_OPTION_VALIDATOR;
    public static final String SHOW_IME_WITH_HARD_KEYBOARD = "show_ime_with_hard_keyboard";
    private static final SettingsValidators.Validator SHOW_IME_WITH_HARD_KEYBOARD_VALIDATOR;
    public static final int SHOW_MODE_AUTO = 0;
    public static final int SHOW_MODE_HIDDEN = 1;
    public static final String SHOW_NOTE_ABOUT_NOTIFICATION_HIDING = "show_note_about_notification_hiding";
    public static final String SHOW_ROTATION_SUGGESTIONS = "show_rotation_suggestions";
    public static final int SHOW_ROTATION_SUGGESTIONS_DEFAULT = 1;
    public static final int SHOW_ROTATION_SUGGESTIONS_DISABLED = 0;
    public static final int SHOW_ROTATION_SUGGESTIONS_ENABLED = 1;
    public static final String SKIP_FIRST_USE_HINTS = "skip_first_use_hints";
    public static final String SLEEP_TIMEOUT = "sleep_timeout";
    private static final SettingsValidators.Validator SLEEP_TIMEOUT_VALIDATOR;
    public static final String SMART_READING = "smart_reading";
    public static final String SMS_DEFAULT_APPLICATION = "sms_default_application";
    public static final String SPELL_CHECKER_ENABLED = "spell_checker_enabled";
    private static final SettingsValidators.Validator SPELL_CHECKER_ENABLED_VALIDATOR;
    public static final String SUPPRESS_AUTO_BATTERY_SAVER_SUGGESTION = "suppress_auto_battery_saver_suggestion";
    public static final String SWIPE_UP_TO_SWITCH_APPS_ENABLED = "swipe_up_to_switch_apps_enabled";
    private static final SettingsValidators.Validator SWIPE_UP_TO_SWITCH_APPS_ENABLED_VALIDATOR;
    public static final String SYNC_PARENT_SOUNDS = "sync_parent_sounds";
    private static final SettingsValidators.Validator SYNC_PARENT_SOUNDS_VALIDATOR;
    public static final String SYSTEM_NAVIGATION_KEYS_ENABLED = "system_navigation_keys_enabled";
    private static final SettingsValidators.Validator SYSTEM_NAVIGATION_KEYS_ENABLED_VALIDATOR;
    public static final String SYSTEM_SCALING_CONTROL = "sys_scaling_ctrl";
    public static final String THEME_MODE = "theme_mode";
    public static final int THEME_MODE_DARK = 2;
    public static final int THEME_MODE_LIGHT = 1;
    public static final int THEME_MODE_WALLPAPER = 0;
    public static final String TOUCH_EXPLORATION_ENABLED = "touch_exploration_enabled";
    private static final SettingsValidators.Validator TOUCH_EXPLORATION_ENABLED_VALIDATOR;
    public static final String TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES = "touch_exploration_granted_accessibility_services";
    private static final SettingsValidators.Validator TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES_VALIDATOR;
    public static final String TRUST_AGENTS_INITIALIZED = "trust_agents_initialized";
    @Deprecated
    public static final String TTS_DEFAULT_COUNTRY = "tts_default_country";
    @Deprecated
    public static final String TTS_DEFAULT_LANG = "tts_default_lang";
    public static final String TTS_DEFAULT_LOCALE = "tts_default_locale";
    private static final SettingsValidators.Validator TTS_DEFAULT_LOCALE_VALIDATOR;
    public static final String TTS_DEFAULT_PITCH = "tts_default_pitch";
    private static final SettingsValidators.Validator TTS_DEFAULT_PITCH_VALIDATOR;
    public static final String TTS_DEFAULT_RATE = "tts_default_rate";
    private static final SettingsValidators.Validator TTS_DEFAULT_RATE_VALIDATOR;
    public static final String TTS_DEFAULT_SYNTH = "tts_default_synth";
    private static final SettingsValidators.Validator TTS_DEFAULT_SYNTH_VALIDATOR;
    @Deprecated
    public static final String TTS_DEFAULT_VARIANT = "tts_default_variant";
    public static final String TTS_ENABLED_PLUGINS = "tts_enabled_plugins";
    private static final SettingsValidators.Validator TTS_ENABLED_PLUGINS_VALIDATOR;
    @Deprecated
    public static final String TTS_USE_DEFAULTS = "tts_use_defaults";
    public static final String TTY_MODE_ENABLED = "tty_mode_enabled";
    private static final SettingsValidators.Validator TTY_MODE_ENABLED_VALIDATOR;
    public static final String TV_INPUT_CUSTOM_LABELS = "tv_input_custom_labels";
    public static final String TV_INPUT_HIDDEN_INPUTS = "tv_input_hidden_inputs";
    public static final String TV_USER_SETUP_COMPLETE = "tv_user_setup_complete";
    public static final String UI_NIGHT_MODE = "ui_night_mode";
    public static final String UNKNOWN_SOURCES_DEFAULT_REVERSED = "unknown_sources_default_reversed";
    public static final String UNSAFE_VOLUME_MUSIC_ACTIVE_MS = "unsafe_volume_music_active_ms";
    public static final String USB_AUDIO_AUTOMATIC_ROUTING_DISABLED = "usb_audio_automatic_routing_disabled";
    @Deprecated
    public static final String USB_MASS_STORAGE_ENABLED = "usb_mass_storage_enabled";
    private static final SettingsValidators.Validator USB_MASS_STORAGE_ENABLED_VALIDATOR;
    public static final String USER_SETUP_COMPLETE = "user_setup_complete";
    public static final String USER_SETUP_PERSONALIZATION_STATE = "user_setup_personalization_state";
    @Deprecated
    public static final String USE_GOOGLE_MAIL = "use_google_mail";
    public static final Map<String, SettingsValidators.Validator> VALIDATORS;
    public static final String VOICE_INTERACTION_SERVICE = "voice_interaction_service";
    public static final String VOICE_RECOGNITION_SERVICE = "voice_recognition_service";
    public static final String VOLUME_HUSH_GESTURE = "volume_hush_gesture";
    private static final SettingsValidators.Validator VOLUME_HUSH_GESTURE_VALIDATOR;
    public static final int VOLUME_HUSH_MUTE = 2;
    public static final int VOLUME_HUSH_OFF = 0;
    public static final int VOLUME_HUSH_VIBRATE = 1;
    public static final String VR_DISPLAY_MODE = "vr_display_mode";
    public static final int VR_DISPLAY_MODE_LOW_PERSISTENCE = 0;
    public static final int VR_DISPLAY_MODE_OFF = 1;
    private static final SettingsValidators.Validator VR_DISPLAY_MODE_VALIDATOR;
    public static final String WAKE_GESTURE_ASUS_ENABLED = "wake_gesture_asus_enabled";
    public static final String WAKE_GESTURE_ENABLED = "wake_gesture_enabled";
    private static final SettingsValidators.Validator WAKE_GESTURE_ENABLED_VALIDATOR;
    public static final String WIFI_DISCONNECT_DELAY_DURATION = "wifi_disconnect_delay_duration";
    private static final SettingsValidators.Validator WIFI_DISCONNECT_DELAY_DURATION_VALIDATOR;
    @Deprecated
    public static final String WIFI_IDLE_MS = "wifi_idle_ms";
    @Deprecated
    public static final String WIFI_MAX_DHCP_RETRY_COUNT = "wifi_max_dhcp_retry_count";
    @Deprecated
    public static final String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = "wifi_mobile_data_transition_wakelock_timeout_ms";
    @Deprecated
    public static final String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wifi_networks_available_notification_on";
    private static final SettingsValidators.Validator WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR;
    @Deprecated
    public static final String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = "wifi_networks_available_repeat_delay";
    private static final SettingsValidators.Validator WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY_VALIDATOR;
    @Deprecated
    public static final String WIFI_NUM_OPEN_NETWORKS_KEPT = "wifi_num_open_networks_kept";
    private static final SettingsValidators.Validator WIFI_NUM_OPEN_NETWORKS_KEPT_VALIDATOR;
    @Deprecated
    public static final String WIFI_ON = "wifi_on";
    @Deprecated
    public static final String WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE = "wifi_watchdog_acceptable_packet_loss_percentage";
    @Deprecated
    public static final String WIFI_WATCHDOG_AP_COUNT = "wifi_watchdog_ap_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS = "wifi_watchdog_background_check_delay_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED = "wifi_watchdog_background_check_enabled";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS = "wifi_watchdog_background_check_timeout_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT = "wifi_watchdog_initial_ignored_ping_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_MAX_AP_CHECKS = "wifi_watchdog_max_ap_checks";
    @Deprecated
    public static final String WIFI_WATCHDOG_ON = "wifi_watchdog_on";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_COUNT = "wifi_watchdog_ping_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_DELAY_MS = "wifi_watchdog_ping_delay_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_TIMEOUT_MS = "wifi_watchdog_ping_timeout_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_WATCH_LIST = "wifi_watchdog_watch_list";
    private static boolean sIsSystemProcess;
    private static ILockSettings sLockSettings;
    private static final Settings.NameValueCache sNameValueCache;
    private static final Settings.ContentProviderHolder sProviderHolder = new Settings.ContentProviderHolder(CONTENT_URI);
    
    static
    {
      sNameValueCache = new Settings.NameValueCache(CONTENT_URI, "GET_secure", "PUT_secure", sProviderHolder);
      sLockSettings = null;
      MOVED_TO_LOCK_SETTINGS = new HashSet(3);
      MOVED_TO_LOCK_SETTINGS.add("lock_pattern_autolock");
      MOVED_TO_LOCK_SETTINGS.add("lock_pattern_visible_pattern");
      MOVED_TO_LOCK_SETTINGS.add("lock_pattern_tactile_feedback_enabled");
      MOVED_TO_GLOBAL = new HashSet();
      MOVED_TO_GLOBAL.add("adb_enabled");
      MOVED_TO_GLOBAL.add("assisted_gps_enabled");
      MOVED_TO_GLOBAL.add("bluetooth_on");
      MOVED_TO_GLOBAL.add("bugreport_in_power_menu");
      MOVED_TO_GLOBAL.add("cdma_cell_broadcast_sms");
      MOVED_TO_GLOBAL.add("roaming_settings");
      MOVED_TO_GLOBAL.add("subscription_mode");
      MOVED_TO_GLOBAL.add("data_activity_timeout_mobile");
      MOVED_TO_GLOBAL.add("data_activity_timeout_wifi");
      MOVED_TO_GLOBAL.add("data_roaming");
      MOVED_TO_GLOBAL.add("development_settings_enabled");
      MOVED_TO_GLOBAL.add("device_provisioned");
      MOVED_TO_GLOBAL.add("display_size_forced");
      MOVED_TO_GLOBAL.add("download_manager_max_bytes_over_mobile");
      MOVED_TO_GLOBAL.add("download_manager_recommended_max_bytes_over_mobile");
      MOVED_TO_GLOBAL.add("mobile_data");
      MOVED_TO_GLOBAL.add("netstats_dev_bucket_duration");
      MOVED_TO_GLOBAL.add("netstats_dev_delete_age");
      MOVED_TO_GLOBAL.add("netstats_dev_persist_bytes");
      MOVED_TO_GLOBAL.add("netstats_dev_rotate_age");
      MOVED_TO_GLOBAL.add("netstats_enabled");
      MOVED_TO_GLOBAL.add("netstats_global_alert_bytes");
      MOVED_TO_GLOBAL.add("netstats_poll_interval");
      MOVED_TO_GLOBAL.add("netstats_sample_enabled");
      MOVED_TO_GLOBAL.add("netstats_time_cache_max_age");
      MOVED_TO_GLOBAL.add("netstats_uid_bucket_duration");
      MOVED_TO_GLOBAL.add("netstats_uid_delete_age");
      MOVED_TO_GLOBAL.add("netstats_uid_persist_bytes");
      MOVED_TO_GLOBAL.add("netstats_uid_rotate_age");
      MOVED_TO_GLOBAL.add("netstats_uid_tag_bucket_duration");
      MOVED_TO_GLOBAL.add("netstats_uid_tag_delete_age");
      MOVED_TO_GLOBAL.add("netstats_uid_tag_persist_bytes");
      MOVED_TO_GLOBAL.add("netstats_uid_tag_rotate_age");
      MOVED_TO_GLOBAL.add("network_preference");
      MOVED_TO_GLOBAL.add("nitz_update_diff");
      MOVED_TO_GLOBAL.add("nitz_update_spacing");
      MOVED_TO_GLOBAL.add("ntp_server");
      MOVED_TO_GLOBAL.add("ntp_timeout");
      MOVED_TO_GLOBAL.add("ntp_server_2");
      MOVED_TO_GLOBAL.add("pdp_watchdog_error_poll_count");
      MOVED_TO_GLOBAL.add("pdp_watchdog_long_poll_interval_ms");
      MOVED_TO_GLOBAL.add("pdp_watchdog_max_pdp_reset_fail_count");
      MOVED_TO_GLOBAL.add("pdp_watchdog_poll_interval_ms");
      MOVED_TO_GLOBAL.add("pdp_watchdog_trigger_packet_count");
      MOVED_TO_GLOBAL.add("setup_prepaid_data_service_url");
      MOVED_TO_GLOBAL.add("setup_prepaid_detection_redir_host");
      MOVED_TO_GLOBAL.add("setup_prepaid_detection_target_url");
      MOVED_TO_GLOBAL.add("tether_dun_apn");
      MOVED_TO_GLOBAL.add("tether_dun_required");
      MOVED_TO_GLOBAL.add("tether_supported");
      MOVED_TO_GLOBAL.add("usb_mass_storage_enabled");
      MOVED_TO_GLOBAL.add("use_google_mail");
      MOVED_TO_GLOBAL.add("wifi_country_code");
      MOVED_TO_GLOBAL.add("wifi_framework_scan_interval_ms");
      MOVED_TO_GLOBAL.add("wifi_frequency_band");
      MOVED_TO_GLOBAL.add("wifi_idle_ms");
      MOVED_TO_GLOBAL.add("wifi_max_dhcp_retry_count");
      MOVED_TO_GLOBAL.add("wifi_mobile_data_transition_wakelock_timeout_ms");
      MOVED_TO_GLOBAL.add("wifi_networks_available_notification_on");
      MOVED_TO_GLOBAL.add("wifi_networks_available_repeat_delay");
      MOVED_TO_GLOBAL.add("wifi_num_open_networks_kept");
      MOVED_TO_GLOBAL.add("wifi_on");
      MOVED_TO_GLOBAL.add("wifi_p2p_device_name");
      MOVED_TO_GLOBAL.add("wifi_saved_state");
      MOVED_TO_GLOBAL.add("wifi_supplicant_scan_interval_ms");
      MOVED_TO_GLOBAL.add("wifi_suspend_optimizations_enabled");
      MOVED_TO_GLOBAL.add("wifi_coverage_extend_feature_enabled");
      MOVED_TO_GLOBAL.add("wifi_verbose_logging_enabled");
      MOVED_TO_GLOBAL.add("wifi_enhanced_auto_join");
      MOVED_TO_GLOBAL.add("wifi_network_show_rssi");
      MOVED_TO_GLOBAL.add("wifi_watchdog_on");
      MOVED_TO_GLOBAL.add("wifi_watchdog_poor_network_test_enabled");
      MOVED_TO_GLOBAL.add("wimax_networks_available_notification_on");
      MOVED_TO_GLOBAL.add("package_verifier_enable");
      MOVED_TO_GLOBAL.add("verifier_timeout");
      MOVED_TO_GLOBAL.add("verifier_default_response");
      MOVED_TO_GLOBAL.add("data_stall_alarm_non_aggressive_delay_in_ms");
      MOVED_TO_GLOBAL.add("data_stall_alarm_aggressive_delay_in_ms");
      MOVED_TO_GLOBAL.add("gprs_register_check_period_ms");
      MOVED_TO_GLOBAL.add("wtf_is_fatal");
      MOVED_TO_GLOBAL.add("battery_discharge_duration_threshold");
      MOVED_TO_GLOBAL.add("battery_discharge_threshold");
      MOVED_TO_GLOBAL.add("send_action_app_error");
      MOVED_TO_GLOBAL.add("dropbox_age_seconds");
      MOVED_TO_GLOBAL.add("dropbox_max_files");
      MOVED_TO_GLOBAL.add("dropbox_quota_kb");
      MOVED_TO_GLOBAL.add("dropbox_quota_percent");
      MOVED_TO_GLOBAL.add("dropbox_reserve_percent");
      MOVED_TO_GLOBAL.add("dropbox:");
      MOVED_TO_GLOBAL.add("logcat_for_");
      MOVED_TO_GLOBAL.add("sys_free_storage_log_interval");
      MOVED_TO_GLOBAL.add("disk_free_change_reporting_threshold");
      MOVED_TO_GLOBAL.add("sys_storage_threshold_percentage");
      MOVED_TO_GLOBAL.add("sys_storage_threshold_max_bytes");
      MOVED_TO_GLOBAL.add("sys_storage_full_threshold_bytes");
      MOVED_TO_GLOBAL.add("sync_max_retry_delay_in_seconds");
      MOVED_TO_GLOBAL.add("connectivity_change_delay");
      MOVED_TO_GLOBAL.add("captive_portal_detection_enabled");
      MOVED_TO_GLOBAL.add("captive_portal_server");
      MOVED_TO_GLOBAL.add("nsd_on");
      MOVED_TO_GLOBAL.add("set_install_location");
      MOVED_TO_GLOBAL.add("default_install_location");
      MOVED_TO_GLOBAL.add("inet_condition_debounce_up_delay");
      MOVED_TO_GLOBAL.add("inet_condition_debounce_down_delay");
      MOVED_TO_GLOBAL.add("read_external_storage_enforced_default");
      MOVED_TO_GLOBAL.add("http_proxy");
      MOVED_TO_GLOBAL.add("global_http_proxy_host");
      MOVED_TO_GLOBAL.add("global_http_proxy_port");
      MOVED_TO_GLOBAL.add("global_http_proxy_exclusion_list");
      MOVED_TO_GLOBAL.add("set_global_http_proxy");
      MOVED_TO_GLOBAL.add("default_dns_server");
      MOVED_TO_GLOBAL.add("preferred_network_mode");
      MOVED_TO_GLOBAL.add("webview_data_reduction_proxy_key");
      BUGREPORT_IN_POWER_MENU_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ALLOW_MOCK_LOCATION_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      BLUETOOTH_ON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      AUTOFILL_SERVICE_VALIDATOR = SettingsValidators.NULLABLE_COMPONENT_NAME_VALIDATOR;
      SHOW_IME_WITH_HARD_KEYBOARD_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WIFI_DISCONNECT_DELAY_DURATION_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      USB_MASS_STORAGE_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_SHORTCUT_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_SHORTCUT_TARGET_SERVICE_VALIDATOR = SettingsValidators.NULLABLE_COMPONENT_NAME_VALIDATOR;
      ACCESSIBILITY_BUTTON_TARGET_COMPONENT_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          boolean bool;
          if (paramAnonymousString != null) {
            bool = true;
          } else {
            bool = false;
          }
          return bool;
        }
      };
      TOUCH_EXPLORATION_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ENABLED_ACCESSIBILITY_SERVICES_VALIDATOR = new SettingsValidators.ComponentNameListValidator(":");
      TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES_VALIDATOR = new SettingsValidators.ComponentNameListValidator(":");
      HUSH_GESTURE_USED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      MANUAL_RINGER_TOGGLE_COUNT_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      ACCESSIBILITY_SPEAK_PASSWORD_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_DISPLAY_MAGNIFICATION_NAVBAR_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE_VALIDATOR = new SettingsValidators.InclusiveFloatRangeValidator(1.0F, Float.MAX_VALUE);
      ACCESSIBILITY_CAPTIONING_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_CAPTIONING_LOCALE_VALIDATOR = SettingsValidators.LOCALE_VALIDATOR;
      ACCESSIBILITY_CAPTIONING_PRESET_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "-1", "0", "1", "2", "3", "4" });
      ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR_VALIDATOR = SettingsValidators.ANY_INTEGER_VALIDATOR;
      ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR_VALIDATOR = SettingsValidators.ANY_INTEGER_VALIDATOR;
      ACCESSIBILITY_CAPTIONING_EDGE_TYPE_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "0", "1", "2" });
      ACCESSIBILITY_CAPTIONING_EDGE_COLOR_VALIDATOR = SettingsValidators.ANY_INTEGER_VALIDATOR;
      ACCESSIBILITY_CAPTIONING_WINDOW_COLOR_VALIDATOR = SettingsValidators.ANY_INTEGER_VALIDATOR;
      ACCESSIBILITY_CAPTIONING_TYPEFACE_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "DEFAULT", "MONOSPACE", "SANS_SERIF", "SERIF" });
      ACCESSIBILITY_CAPTIONING_FONT_SCALE_VALIDATOR = new SettingsValidators.InclusiveFloatRangeValidator(0.5F, 2.0F);
      ACCESSIBILITY_DISPLAY_INVERSION_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_DISPLAY_DALTONIZER_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "-1", "0", "11", "12", "13" });
      ACCESSIBILITY_AUTOCLICK_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCESSIBILITY_AUTOCLICK_DELAY_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      ACCESSIBILITY_LARGE_POINTER_ICON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      LONG_PRESS_TIMEOUT_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      TTS_DEFAULT_RATE_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      TTS_DEFAULT_PITCH_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      TTS_DEFAULT_SYNTH_VALIDATOR = SettingsValidators.PACKAGE_NAME_VALIDATOR;
      TTS_DEFAULT_LOCALE_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          if ((paramAnonymousString != null) && (paramAnonymousString.length() != 0))
          {
            paramAnonymousString = paramAnonymousString.split(",");
            int i = paramAnonymousString.length;
            boolean bool1 = true;
            for (int j = 0; j < i; j++)
            {
              String[] arrayOfString = paramAnonymousString[j].split(":");
              int k = arrayOfString.length;
              boolean bool2 = true;
              if ((k != 2) || (arrayOfString[0].length() <= 0) || (!SettingsValidators.ANY_STRING_VALIDATOR.validate(arrayOfString[0])) || (!SettingsValidators.LOCALE_VALIDATOR.validate(arrayOfString[1]))) {
                bool2 = false;
              }
              bool1 |= bool2;
            }
            return bool1;
          }
          return false;
        }
      };
      TTS_ENABLED_PLUGINS_VALIDATOR = new SettingsValidators.PackageNameListValidator(" ");
      WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      WIFI_NUM_OPEN_NETWORKS_KEPT_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      PREFERRED_TTY_MODE_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "0", "1", "2", "3" });
      ENHANCED_VOICE_PRIVACY_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      TTY_MODE_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      RTT_CALLING_MODE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      MOUNT_PLAY_NOTIFICATION_SND_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      MOUNT_UMS_AUTOSTART_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      MOUNT_UMS_PROMPT_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      MOUNT_UMS_NOTIFY_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SHOW_FIRST_CRASH_DIALOG_DEV_OPTION_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SELECTED_SPELL_CHECKER_VALIDATOR = SettingsValidators.COMPONENT_NAME_VALIDATOR;
      SELECTED_SPELL_CHECKER_SUBTYPE_VALIDATOR = SettingsValidators.COMPONENT_NAME_VALIDATOR;
      SPELL_CHECKER_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      INCALL_POWER_BUTTON_BEHAVIOR_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "1", "2" });
      WAKE_GESTURE_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      DOZE_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      DOZE_PULSE_ON_PICK_UP_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      DOZE_PULSE_ON_DOUBLE_TAP_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SCREENSAVER_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SCREENSAVER_COMPONENTS_VALIDATOR = new SettingsValidators.ComponentNameListValidator(",");
      SCREENSAVER_ACTIVATE_ON_DOCK_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SCREENSAVER_ACTIVATE_ON_SLEEP_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      NFC_PAYMENT_DEFAULT_COMPONENT_VALIDATOR = SettingsValidators.COMPONENT_NAME_VALIDATOR;
      ENABLED_NOTIFICATION_ASSISTANT_VALIDATOR = new SettingsValidators.ComponentNameListValidator(":");
      ENABLED_NOTIFICATION_LISTENERS_VALIDATOR = new SettingsValidators.ComponentNameListValidator(":");
      ENABLED_NOTIFICATION_POLICY_ACCESS_PACKAGES_VALIDATOR = new SettingsValidators.PackageNameListValidator(":");
      SYNC_PARENT_SOUNDS_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SLEEP_TIMEOUT_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(-1, Integer.MAX_VALUE);
      DOUBLE_TAP_TO_WAKE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      CAMERA_GESTURE_DISABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      CAMERA_DOUBLE_TWIST_TO_FLIP_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SWIPE_UP_TO_SWITCH_APPS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ASSIST_GESTURE_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ASSIST_GESTURE_SENSITIVITY_VALIDATOR = new SettingsValidators.InclusiveFloatRangeValidator(0.0F, 1.0F);
      ASSIST_GESTURE_SILENCE_ALERTS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ASSIST_GESTURE_WAKE_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ASSIST_GESTURE_SETUP_COMPLETE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      NIGHT_DISPLAY_AUTO_MODE_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 2);
      NIGHT_DISPLAY_COLOR_TEMPERATURE_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      NIGHT_DISPLAY_CUSTOM_START_TIME_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      NIGHT_DISPLAY_CUSTOM_END_TIME_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      ENABLED_VR_LISTENERS_VALIDATOR = new SettingsValidators.ComponentNameListValidator(":");
      VR_DISPLAY_MODE_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "0", "1" });
      AUTOMATIC_STORAGE_MANAGER_DAYS_TO_RETAIN_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      SYSTEM_NAVIGATION_KEYS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      QS_TILES_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          if (paramAnonymousString == null) {
            return false;
          }
          paramAnonymousString = paramAnonymousString.split(",");
          int i = paramAnonymousString.length;
          boolean bool1 = true;
          for (int j = 0; j < i; j++)
          {
            String str = paramAnonymousString[j];
            boolean bool2;
            if ((str.length() > 0) && (SettingsValidators.ANY_STRING_VALIDATOR.validate(str))) {
              bool2 = true;
            } else {
              bool2 = false;
            }
            bool1 |= bool2;
          }
          return bool1;
        }
      };
      NOTIFICATION_BADGING_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      QS_AUTO_ADDED_TILES_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          if (paramAnonymousString == null) {
            return false;
          }
          paramAnonymousString = paramAnonymousString.split(",");
          int i = paramAnonymousString.length;
          boolean bool1 = true;
          for (int j = 0; j < i; j++)
          {
            String str = paramAnonymousString[j];
            boolean bool2;
            if ((str.length() > 0) && (SettingsValidators.ANY_STRING_VALIDATOR.validate(str))) {
              bool2 = true;
            } else {
              bool2 = false;
            }
            bool1 |= bool2;
          }
          return bool1;
        }
      };
      LOCKDOWN_IN_POWER_MENU_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      VOLUME_HUSH_GESTURE_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      SETTINGS_TO_BACKUP = new String[] { "bugreport_in_power_menu", "mock_location", "usb_mass_storage_enabled", "accessibility_display_inversion_enabled", "accessibility_display_daltonizer", "accessibility_display_daltonizer_enabled", "accessibility_display_magnification_enabled", "accessibility_display_magnification_navbar_enabled", "autofill_service", "accessibility_display_magnification_scale", "enabled_accessibility_services", "enabled_vr_listeners", "touch_exploration_granted_accessibility_services", "touch_exploration_enabled", "accessibility_enabled", "accessibility_shortcut_target_service", "accessibility_button_target_component", "accessibility_shortcut_dialog_shown", "accessibility_shortcut_enabled", "accessibility_shortcut_on_lock_screen", "speak_password", "high_text_contrast_enabled", "accessibility_captioning_preset", "accessibility_captioning_enabled", "accessibility_captioning_locale", "accessibility_captioning_background_color", "accessibility_captioning_foreground_color", "accessibility_captioning_edge_type", "accessibility_captioning_edge_color", "accessibility_captioning_typeface", "accessibility_captioning_font_scale", "accessibility_captioning_window_color", "tts_default_rate", "tts_default_pitch", "tts_default_synth", "tts_enabled_plugins", "tts_default_locale", "show_ime_with_hard_keyboard", "wifi_networks_available_notification_on", "wifi_networks_available_repeat_delay", "wifi_num_open_networks_kept", "selected_spell_checker", "selected_spell_checker_subtype", "spell_checker_enabled", "mount_play_not_snd", "mount_ums_autostart", "mount_ums_prompt", "mount_ums_notify_enabled", "sleep_timeout", "double_tap_to_wake", "wake_gesture_enabled", "long_press_timeout", "camera_gesture_disabled", "accessibility_autoclick_enabled", "accessibility_autoclick_delay", "accessibility_large_pointer_icon", "preferred_tty_mode", "enhanced_voice_privacy_enabled", "tty_mode_enabled", "rtt_calling_mode", "incall_power_button_behavior", "night_display_custom_start_time", "night_display_custom_end_time", "night_display_color_temperature", "night_display_auto_mode", "sync_parent_sounds", "camera_double_twist_to_flip_enabled", "swipe_up_to_switch_apps_enabled", "camera_double_tap_power_gesture_disabled", "system_navigation_keys_enabled", "sysui_qs_tiles", "doze_enabled", "doze_pulse_on_pick_up", "doze_pulse_on_double_tap", "nfc_payment_default_component", "automatic_storage_manager_days_to_retain", "assist_gesture_enabled", "assist_gesture_sensitivity", "assist_gesture_setup_complete", "assist_gesture_silence_alerts_enabled", "assist_gesture_wake_enabled", "vr_display_mode", "notification_badging", "qs_auto_tiles", "screensaver_enabled", "screensaver_components", "screensaver_activate_on_dock", "screensaver_activate_on_sleep", "lockdown_in_power_menu", "show_first_crash_dialog_dev_option", "volume_hush_gesture", "manual_ringer_toggle_count", "hush_gesture_used", "wifi_disconnect_delay_duration" };
      VALIDATORS = new ArrayMap();
      VALIDATORS.put("bugreport_in_power_menu", BUGREPORT_IN_POWER_MENU_VALIDATOR);
      VALIDATORS.put("mock_location", ALLOW_MOCK_LOCATION_VALIDATOR);
      VALIDATORS.put("usb_mass_storage_enabled", USB_MASS_STORAGE_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_display_inversion_enabled", ACCESSIBILITY_DISPLAY_INVERSION_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_display_daltonizer", ACCESSIBILITY_DISPLAY_DALTONIZER_VALIDATOR);
      VALIDATORS.put("accessibility_display_daltonizer_enabled", ACCESSIBILITY_DISPLAY_DALTONIZER_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_display_magnification_enabled", ACCESSIBILITY_DISPLAY_MAGNIFICATION_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_display_magnification_navbar_enabled", ACCESSIBILITY_DISPLAY_MAGNIFICATION_NAVBAR_ENABLED_VALIDATOR);
      VALIDATORS.put("autofill_service", AUTOFILL_SERVICE_VALIDATOR);
      VALIDATORS.put("accessibility_display_magnification_scale", ACCESSIBILITY_DISPLAY_MAGNIFICATION_SCALE_VALIDATOR);
      VALIDATORS.put("enabled_accessibility_services", ENABLED_ACCESSIBILITY_SERVICES_VALIDATOR);
      VALIDATORS.put("enabled_vr_listeners", ENABLED_VR_LISTENERS_VALIDATOR);
      VALIDATORS.put("touch_exploration_granted_accessibility_services", TOUCH_EXPLORATION_GRANTED_ACCESSIBILITY_SERVICES_VALIDATOR);
      VALIDATORS.put("touch_exploration_enabled", TOUCH_EXPLORATION_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_enabled", ACCESSIBILITY_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_shortcut_target_service", ACCESSIBILITY_SHORTCUT_TARGET_SERVICE_VALIDATOR);
      VALIDATORS.put("accessibility_button_target_component", ACCESSIBILITY_BUTTON_TARGET_COMPONENT_VALIDATOR);
      VALIDATORS.put("accessibility_shortcut_dialog_shown", ACCESSIBILITY_SHORTCUT_DIALOG_SHOWN_VALIDATOR);
      VALIDATORS.put("accessibility_shortcut_enabled", ACCESSIBILITY_SHORTCUT_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_shortcut_on_lock_screen", ACCESSIBILITY_SHORTCUT_ON_LOCK_SCREEN_VALIDATOR);
      VALIDATORS.put("speak_password", ACCESSIBILITY_SPEAK_PASSWORD_VALIDATOR);
      VALIDATORS.put("high_text_contrast_enabled", ACCESSIBILITY_HIGH_TEXT_CONTRAST_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_preset", ACCESSIBILITY_CAPTIONING_PRESET_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_enabled", ACCESSIBILITY_CAPTIONING_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_locale", ACCESSIBILITY_CAPTIONING_LOCALE_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_background_color", ACCESSIBILITY_CAPTIONING_BACKGROUND_COLOR_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_foreground_color", ACCESSIBILITY_CAPTIONING_FOREGROUND_COLOR_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_edge_type", ACCESSIBILITY_CAPTIONING_EDGE_TYPE_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_edge_color", ACCESSIBILITY_CAPTIONING_EDGE_COLOR_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_typeface", ACCESSIBILITY_CAPTIONING_TYPEFACE_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_font_scale", ACCESSIBILITY_CAPTIONING_FONT_SCALE_VALIDATOR);
      VALIDATORS.put("accessibility_captioning_window_color", ACCESSIBILITY_CAPTIONING_WINDOW_COLOR_VALIDATOR);
      VALIDATORS.put("tts_default_rate", TTS_DEFAULT_RATE_VALIDATOR);
      VALIDATORS.put("tts_default_pitch", TTS_DEFAULT_PITCH_VALIDATOR);
      VALIDATORS.put("tts_default_synth", TTS_DEFAULT_SYNTH_VALIDATOR);
      VALIDATORS.put("tts_enabled_plugins", TTS_ENABLED_PLUGINS_VALIDATOR);
      VALIDATORS.put("tts_default_locale", TTS_DEFAULT_LOCALE_VALIDATOR);
      VALIDATORS.put("show_ime_with_hard_keyboard", SHOW_IME_WITH_HARD_KEYBOARD_VALIDATOR);
      VALIDATORS.put("wifi_networks_available_notification_on", WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR);
      VALIDATORS.put("wifi_networks_available_repeat_delay", WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY_VALIDATOR);
      VALIDATORS.put("wifi_num_open_networks_kept", WIFI_NUM_OPEN_NETWORKS_KEPT_VALIDATOR);
      VALIDATORS.put("selected_spell_checker", SELECTED_SPELL_CHECKER_VALIDATOR);
      VALIDATORS.put("selected_spell_checker_subtype", SELECTED_SPELL_CHECKER_SUBTYPE_VALIDATOR);
      VALIDATORS.put("spell_checker_enabled", SPELL_CHECKER_ENABLED_VALIDATOR);
      VALIDATORS.put("mount_play_not_snd", MOUNT_PLAY_NOTIFICATION_SND_VALIDATOR);
      VALIDATORS.put("mount_ums_autostart", MOUNT_UMS_AUTOSTART_VALIDATOR);
      VALIDATORS.put("mount_ums_prompt", MOUNT_UMS_PROMPT_VALIDATOR);
      VALIDATORS.put("mount_ums_notify_enabled", MOUNT_UMS_NOTIFY_ENABLED_VALIDATOR);
      VALIDATORS.put("sleep_timeout", SLEEP_TIMEOUT_VALIDATOR);
      VALIDATORS.put("double_tap_to_wake", DOUBLE_TAP_TO_WAKE_VALIDATOR);
      VALIDATORS.put("wake_gesture_enabled", WAKE_GESTURE_ENABLED_VALIDATOR);
      VALIDATORS.put("long_press_timeout", LONG_PRESS_TIMEOUT_VALIDATOR);
      VALIDATORS.put("camera_gesture_disabled", CAMERA_GESTURE_DISABLED_VALIDATOR);
      VALIDATORS.put("accessibility_autoclick_enabled", ACCESSIBILITY_AUTOCLICK_ENABLED_VALIDATOR);
      VALIDATORS.put("accessibility_autoclick_delay", ACCESSIBILITY_AUTOCLICK_DELAY_VALIDATOR);
      VALIDATORS.put("accessibility_large_pointer_icon", ACCESSIBILITY_LARGE_POINTER_ICON_VALIDATOR);
      VALIDATORS.put("preferred_tty_mode", PREFERRED_TTY_MODE_VALIDATOR);
      VALIDATORS.put("enhanced_voice_privacy_enabled", ENHANCED_VOICE_PRIVACY_ENABLED_VALIDATOR);
      VALIDATORS.put("tty_mode_enabled", TTY_MODE_ENABLED_VALIDATOR);
      VALIDATORS.put("rtt_calling_mode", RTT_CALLING_MODE_VALIDATOR);
      VALIDATORS.put("incall_power_button_behavior", INCALL_POWER_BUTTON_BEHAVIOR_VALIDATOR);
      VALIDATORS.put("night_display_custom_start_time", NIGHT_DISPLAY_CUSTOM_START_TIME_VALIDATOR);
      VALIDATORS.put("night_display_custom_end_time", NIGHT_DISPLAY_CUSTOM_END_TIME_VALIDATOR);
      VALIDATORS.put("night_display_color_temperature", NIGHT_DISPLAY_COLOR_TEMPERATURE_VALIDATOR);
      VALIDATORS.put("night_display_auto_mode", NIGHT_DISPLAY_AUTO_MODE_VALIDATOR);
      VALIDATORS.put("sync_parent_sounds", SYNC_PARENT_SOUNDS_VALIDATOR);
      VALIDATORS.put("camera_double_twist_to_flip_enabled", CAMERA_DOUBLE_TWIST_TO_FLIP_ENABLED_VALIDATOR);
      VALIDATORS.put("swipe_up_to_switch_apps_enabled", SWIPE_UP_TO_SWITCH_APPS_ENABLED_VALIDATOR);
      VALIDATORS.put("camera_double_tap_power_gesture_disabled", CAMERA_DOUBLE_TAP_POWER_GESTURE_DISABLED_VALIDATOR);
      VALIDATORS.put("system_navigation_keys_enabled", SYSTEM_NAVIGATION_KEYS_ENABLED_VALIDATOR);
      VALIDATORS.put("sysui_qs_tiles", QS_TILES_VALIDATOR);
      VALIDATORS.put("doze_enabled", DOZE_ENABLED_VALIDATOR);
      VALIDATORS.put("doze_pulse_on_pick_up", DOZE_PULSE_ON_PICK_UP_VALIDATOR);
      VALIDATORS.put("doze_pulse_on_double_tap", DOZE_PULSE_ON_DOUBLE_TAP_VALIDATOR);
      VALIDATORS.put("nfc_payment_default_component", NFC_PAYMENT_DEFAULT_COMPONENT_VALIDATOR);
      VALIDATORS.put("automatic_storage_manager_days_to_retain", AUTOMATIC_STORAGE_MANAGER_DAYS_TO_RETAIN_VALIDATOR);
      VALIDATORS.put("assist_gesture_enabled", ASSIST_GESTURE_ENABLED_VALIDATOR);
      VALIDATORS.put("assist_gesture_sensitivity", ASSIST_GESTURE_SENSITIVITY_VALIDATOR);
      VALIDATORS.put("assist_gesture_setup_complete", ASSIST_GESTURE_SETUP_COMPLETE_VALIDATOR);
      VALIDATORS.put("assist_gesture_silence_alerts_enabled", ASSIST_GESTURE_SILENCE_ALERTS_ENABLED_VALIDATOR);
      VALIDATORS.put("assist_gesture_wake_enabled", ASSIST_GESTURE_WAKE_ENABLED_VALIDATOR);
      VALIDATORS.put("vr_display_mode", VR_DISPLAY_MODE_VALIDATOR);
      VALIDATORS.put("notification_badging", NOTIFICATION_BADGING_VALIDATOR);
      VALIDATORS.put("qs_auto_tiles", QS_AUTO_ADDED_TILES_VALIDATOR);
      VALIDATORS.put("screensaver_enabled", SCREENSAVER_ENABLED_VALIDATOR);
      VALIDATORS.put("screensaver_components", SCREENSAVER_COMPONENTS_VALIDATOR);
      VALIDATORS.put("screensaver_activate_on_dock", SCREENSAVER_ACTIVATE_ON_DOCK_VALIDATOR);
      VALIDATORS.put("screensaver_activate_on_sleep", SCREENSAVER_ACTIVATE_ON_SLEEP_VALIDATOR);
      VALIDATORS.put("lockdown_in_power_menu", LOCKDOWN_IN_POWER_MENU_VALIDATOR);
      VALIDATORS.put("show_first_crash_dialog_dev_option", SHOW_FIRST_CRASH_DIALOG_DEV_OPTION_VALIDATOR);
      VALIDATORS.put("volume_hush_gesture", VOLUME_HUSH_GESTURE_VALIDATOR);
      VALIDATORS.put("enabled_notification_listeners", ENABLED_NOTIFICATION_LISTENERS_VALIDATOR);
      VALIDATORS.put("enabled_notification_assistant", ENABLED_NOTIFICATION_ASSISTANT_VALIDATOR);
      VALIDATORS.put("enabled_notification_policy_access_packages", ENABLED_NOTIFICATION_POLICY_ACCESS_PACKAGES_VALIDATOR);
      VALIDATORS.put("hush_gesture_used", HUSH_GESTURE_USED_VALIDATOR);
      VALIDATORS.put("manual_ringer_toggle_count", MANUAL_RINGER_TOGGLE_COUNT_VALIDATOR);
      VALIDATORS.put("wifi_disconnect_delay_duration", WIFI_DISCONNECT_DELAY_DURATION_VALIDATOR);
      LEGACY_RESTORE_SETTINGS = new String[] { "enabled_notification_listeners", "enabled_notification_assistant", "enabled_notification_policy_access_packages" };
      CLONE_TO_MANAGED_PROFILE = new ArraySet();
      CLONE_TO_MANAGED_PROFILE.add("accessibility_enabled");
      CLONE_TO_MANAGED_PROFILE.add("mock_location");
      CLONE_TO_MANAGED_PROFILE.add("allowed_geolocation_origins");
      CLONE_TO_MANAGED_PROFILE.add("autofill_service");
      CLONE_TO_MANAGED_PROFILE.add("default_input_method");
      CLONE_TO_MANAGED_PROFILE.add("enabled_accessibility_services");
      CLONE_TO_MANAGED_PROFILE.add("enabled_input_methods");
      CLONE_TO_MANAGED_PROFILE.add("location_changer");
      CLONE_TO_MANAGED_PROFILE.add("location_mode");
      CLONE_TO_MANAGED_PROFILE.add("location_providers_allowed");
      CLONE_TO_MANAGED_PROFILE.add("selected_input_method_subtype");
      CLONE_TO_MANAGED_PROFILE.add("selected_spell_checker");
      CLONE_TO_MANAGED_PROFILE.add("selected_spell_checker_subtype");
      INSTANT_APP_SETTINGS = new ArraySet();
      INSTANT_APP_SETTINGS.add("enabled_accessibility_services");
      INSTANT_APP_SETTINGS.add("speak_password");
      INSTANT_APP_SETTINGS.add("accessibility_display_inversion_enabled");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_enabled");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_preset");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_edge_type");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_edge_color");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_locale");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_background_color");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_foreground_color");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_typeface");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_font_scale");
      INSTANT_APP_SETTINGS.add("accessibility_captioning_window_color");
      INSTANT_APP_SETTINGS.add("accessibility_display_daltonizer_enabled");
      INSTANT_APP_SETTINGS.add("accessibility_display_daltonizer");
      INSTANT_APP_SETTINGS.add("accessibility_autoclick_delay");
      INSTANT_APP_SETTINGS.add("accessibility_autoclick_enabled");
      INSTANT_APP_SETTINGS.add("accessibility_large_pointer_icon");
      INSTANT_APP_SETTINGS.add("default_input_method");
      INSTANT_APP_SETTINGS.add("enabled_input_methods");
      INSTANT_APP_SETTINGS.add("android_id");
      INSTANT_APP_SETTINGS.add("package_verifier_user_consent");
      INSTANT_APP_SETTINGS.add("mock_location");
    }
    
    public Secure() {}
    
    public static void clearProviderForTest()
    {
      sProviderHolder.clearProviderForTest();
      sNameValueCache.clearGenerationTrackerForTest();
    }
    
    public static void getCloneToManagedProfileSettings(Set<String> paramSet)
    {
      paramSet.addAll(CLONE_TO_MANAGED_PROFILE);
    }
    
    public static float getFloat(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      return getFloatForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static float getFloat(ContentResolver paramContentResolver, String paramString, float paramFloat)
    {
      return getFloatForUser(paramContentResolver, paramString, paramFloat, paramContentResolver.getUserId());
    }
    
    public static float getFloatForUser(ContentResolver paramContentResolver, String paramString, float paramFloat, int paramInt)
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      if (paramContentResolver != null) {
        try
        {
          float f = Float.parseFloat(paramContentResolver);
          paramFloat = f;
        }
        catch (NumberFormatException paramContentResolver)
        {
          return paramFloat;
        }
      }
      return paramFloat;
    }
    
    public static float getFloatForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
      throws Settings.SettingNotFoundException
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      if (paramContentResolver != null) {
        try
        {
          float f = Float.parseFloat(paramContentResolver);
          return f;
        }
        catch (NumberFormatException paramContentResolver)
        {
          throw new Settings.SettingNotFoundException(paramString);
        }
      }
      throw new Settings.SettingNotFoundException(paramString);
    }
    
    public static int getInt(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      return getIntForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static int getInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      return getIntForUser(paramContentResolver, paramString, paramInt, paramContentResolver.getUserId());
    }
    
    public static int getIntForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
      throws Settings.SettingNotFoundException
    {
      if ("location_mode".equals(paramString)) {
        return getLocationModeForUser(paramContentResolver, paramInt);
      }
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      try
      {
        paramInt = Integer.parseInt(paramContentResolver);
        return paramInt;
      }
      catch (NumberFormatException paramContentResolver)
      {
        throw new Settings.SettingNotFoundException(paramString);
      }
    }
    
    public static int getIntForUser(ContentResolver paramContentResolver, String paramString, int paramInt1, int paramInt2)
    {
      if ("location_mode".equals(paramString)) {
        return getLocationModeForUser(paramContentResolver, paramInt2);
      }
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt2);
      if (paramContentResolver != null) {
        try
        {
          paramInt2 = Integer.parseInt(paramContentResolver);
          paramInt1 = paramInt2;
        }
        catch (NumberFormatException paramContentResolver)
        {
          return paramInt1;
        }
      }
      return paramInt1;
    }
    
    private static final int getLocationModeForUser(ContentResolver paramContentResolver, int paramInt)
    {
      synchronized (Settings.mLocationSettingsLock)
      {
        boolean bool1 = isLocationProviderEnabledForUser(paramContentResolver, "gps", paramInt);
        boolean bool2 = isLocationProviderEnabledForUser(paramContentResolver, "network", paramInt);
        if ((bool1) && (bool2)) {
          return 3;
        }
        if (bool1) {
          return 1;
        }
        if (bool2) {
          return 2;
        }
        return 0;
      }
    }
    
    public static long getLong(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      return getLongForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static long getLong(ContentResolver paramContentResolver, String paramString, long paramLong)
    {
      return getLongForUser(paramContentResolver, paramString, paramLong, paramContentResolver.getUserId());
    }
    
    public static long getLongForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
      throws Settings.SettingNotFoundException
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      try
      {
        long l = Long.parseLong(paramContentResolver);
        return l;
      }
      catch (NumberFormatException paramContentResolver)
      {
        throw new Settings.SettingNotFoundException(paramString);
      }
    }
    
    public static long getLongForUser(ContentResolver paramContentResolver, String paramString, long paramLong, int paramInt)
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      if (paramContentResolver != null) {
        try
        {
          long l = Long.parseLong(paramContentResolver);
          paramLong = l;
        }
        catch (NumberFormatException paramContentResolver) {}
      }
      return paramLong;
    }
    
    public static void getMovedToGlobalSettings(Set<String> paramSet)
    {
      paramSet.addAll(MOVED_TO_GLOBAL);
    }
    
    public static String getString(ContentResolver paramContentResolver, String paramString)
    {
      return getStringForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static String getStringForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      Object localObject;
      if (MOVED_TO_GLOBAL.contains(paramString))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Setting ");
        ((StringBuilder)localObject).append(paramString);
        ((StringBuilder)localObject).append(" has moved from android.provider.Settings.Secure to android.provider.Settings.Global.");
        Log.w("Settings", ((StringBuilder)localObject).toString());
        return Settings.Global.getStringForUser(paramContentResolver, paramString, paramInt);
      }
      if (MOVED_TO_LOCK_SETTINGS.contains(paramString)) {
        try
        {
          localObject = sLockSettings;
          int i = 0;
          if (localObject == null)
          {
            sLockSettings = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
            boolean bool;
            if (Process.myUid() == 1000) {
              bool = true;
            } else {
              bool = false;
            }
            sIsSystemProcess = bool;
          }
          if ((sLockSettings != null) && (!sIsSystemProcess))
          {
            localObject = ActivityThread.currentApplication();
            int j = i;
            if (localObject != null)
            {
              j = i;
              if (((Application)localObject).getApplicationInfo() != null)
              {
                j = i;
                if (getApplicationInfotargetSdkVersion <= 22) {
                  j = 1;
                }
              }
            }
            if (j != 0)
            {
              try
              {
                localObject = sLockSettings.getString(paramString, "0", paramInt);
                return localObject;
              }
              catch (RemoteException localRemoteException) {}
            }
            else
            {
              paramContentResolver = new StringBuilder();
              paramContentResolver.append("Settings.Secure.");
              paramContentResolver.append(paramString);
              paramContentResolver.append(" is deprecated and no longer accessible. See API documentation for potential replacements.");
              throw new SecurityException(paramContentResolver.toString());
            }
          }
        }
        finally {}
      }
      return sNameValueCache.getStringForUser(paramContentResolver, paramString, paramInt);
    }
    
    public static Uri getUriFor(String paramString)
    {
      if (MOVED_TO_GLOBAL.contains(paramString))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Setting ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" has moved from android.provider.Settings.Secure to android.provider.Settings.Global, returning global URI.");
        Log.w("Settings", localStringBuilder.toString());
        return Settings.Global.getUriFor(Settings.Global.CONTENT_URI, paramString);
      }
      return getUriFor(CONTENT_URI, paramString);
    }
    
    @Deprecated
    public static final boolean isLocationProviderEnabled(ContentResolver paramContentResolver, String paramString)
    {
      return isLocationProviderEnabledForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    @Deprecated
    public static final boolean isLocationProviderEnabledForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      return TextUtils.delimitedStringContains(getStringForUser(paramContentResolver, "location_providers_allowed", paramInt), ',', paramString);
    }
    
    public static boolean putFloat(ContentResolver paramContentResolver, String paramString, float paramFloat)
    {
      return putFloatForUser(paramContentResolver, paramString, paramFloat, paramContentResolver.getUserId());
    }
    
    public static boolean putFloatForUser(ContentResolver paramContentResolver, String paramString, float paramFloat, int paramInt)
    {
      return putStringForUser(paramContentResolver, paramString, Float.toString(paramFloat), paramInt);
    }
    
    public static boolean putInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      return putIntForUser(paramContentResolver, paramString, paramInt, paramContentResolver.getUserId());
    }
    
    public static boolean putIntForUser(ContentResolver paramContentResolver, String paramString, int paramInt1, int paramInt2)
    {
      return putStringForUser(paramContentResolver, paramString, Integer.toString(paramInt1), paramInt2);
    }
    
    public static boolean putLong(ContentResolver paramContentResolver, String paramString, long paramLong)
    {
      return putLongForUser(paramContentResolver, paramString, paramLong, paramContentResolver.getUserId());
    }
    
    public static boolean putLongForUser(ContentResolver paramContentResolver, String paramString, long paramLong, int paramInt)
    {
      return putStringForUser(paramContentResolver, paramString, Long.toString(paramLong), paramInt);
    }
    
    public static boolean putString(ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      return putStringForUser(paramContentResolver, paramString1, paramString2, paramContentResolver.getUserId());
    }
    
    @SystemApi
    public static boolean putString(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
    {
      return putStringForUser(paramContentResolver, paramString1, paramString2, paramString3, paramBoolean, paramContentResolver.getUserId());
    }
    
    public static boolean putStringForUser(ContentResolver paramContentResolver, String paramString1, String paramString2, int paramInt)
    {
      return putStringForUser(paramContentResolver, paramString1, paramString2, null, false, paramInt);
    }
    
    public static boolean putStringForUser(ContentResolver paramContentResolver, String paramString1, String paramString2, String paramString3, boolean paramBoolean, int paramInt)
    {
      if ("location_mode".equals(paramString1)) {
        return setLocationModeForUser(paramContentResolver, Integer.parseInt(paramString2), paramInt);
      }
      if (MOVED_TO_GLOBAL.contains(paramString1))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Setting ");
        localStringBuilder.append(paramString1);
        localStringBuilder.append(" has moved from android.provider.Settings.Secure to android.provider.Settings.Global");
        Log.w("Settings", localStringBuilder.toString());
        return Settings.Global.putStringForUser(paramContentResolver, paramString1, paramString2, paramString3, paramBoolean, paramInt);
      }
      return sNameValueCache.putStringForUser(paramContentResolver, paramString1, paramString2, paramString3, paramBoolean, paramInt);
    }
    
    @SystemApi
    public static void resetToDefaults(ContentResolver paramContentResolver, String paramString)
    {
      resetToDefaultsAsUser(paramContentResolver, paramString, 1, paramContentResolver.getUserId());
    }
    
    public static void resetToDefaultsAsUser(ContentResolver paramContentResolver, String paramString, int paramInt1, int paramInt2)
    {
      try
      {
        Bundle localBundle = new android/os/Bundle;
        localBundle.<init>();
        localBundle.putInt("_user", paramInt2);
        if (paramString != null) {
          localBundle.putString("_tag", paramString);
        }
        localBundle.putInt("_reset_mode", paramInt1);
        sProviderHolder.getProvider(paramContentResolver).call(paramContentResolver.getPackageName(), "RESET_secure", null, localBundle);
      }
      catch (RemoteException paramString)
      {
        paramContentResolver = new StringBuilder();
        paramContentResolver.append("Can't reset do defaults for ");
        paramContentResolver.append(CONTENT_URI);
        Log.w("Settings", paramContentResolver.toString(), paramString);
      }
    }
    
    @Deprecated
    private static boolean setLocationModeForUser(ContentResolver paramContentResolver, int paramInt1, int paramInt2)
    {
      Object localObject = Settings.mLocationSettingsLock;
      boolean bool1 = false;
      boolean bool2 = false;
      switch (paramInt1)
      {
      }
      try
      {
        IllegalArgumentException localIllegalArgumentException = new java/lang/IllegalArgumentException;
        break label117;
        bool1 = true;
        bool2 = true;
        break label73;
        bool2 = true;
        break label73;
        bool1 = true;
        label73:
        bool2 = setLocationProviderEnabledForUser(paramContentResolver, "network", bool2, paramInt2);
        if ((setLocationProviderEnabledForUser(paramContentResolver, "gps", bool1, paramInt2)) && (bool2)) {
          bool1 = true;
        } else {
          bool1 = false;
        }
        return bool1;
        label117:
        paramContentResolver = new java/lang/StringBuilder;
        paramContentResolver.<init>();
        paramContentResolver.append("Invalid location mode: ");
        paramContentResolver.append(paramInt1);
        localIllegalArgumentException.<init>(paramContentResolver.toString());
        throw localIllegalArgumentException;
      }
      finally {}
    }
    
    @Deprecated
    public static final void setLocationProviderEnabled(ContentResolver paramContentResolver, String paramString, boolean paramBoolean)
    {
      setLocationProviderEnabledForUser(paramContentResolver, paramString, paramBoolean, paramContentResolver.getUserId());
    }
    
    @Deprecated
    public static final boolean setLocationProviderEnabledForUser(ContentResolver paramContentResolver, String paramString, boolean paramBoolean, int paramInt)
    {
      Object localObject = Settings.mLocationSettingsLock;
      StringBuilder localStringBuilder;
      if (paramBoolean)
      {
        try
        {
          localStringBuilder = new java/lang/StringBuilder;
          localStringBuilder.<init>();
          localStringBuilder.append("+");
          localStringBuilder.append(paramString);
          paramString = localStringBuilder.toString();
        }
        finally
        {
          break label98;
        }
      }
      else
      {
        localStringBuilder = new java/lang/StringBuilder;
        localStringBuilder.<init>();
        localStringBuilder.append("-");
        localStringBuilder.append(paramString);
        paramString = localStringBuilder.toString();
      }
      paramBoolean = putStringForUser(paramContentResolver, "location_providers_allowed", paramString, paramInt);
      return paramBoolean;
      label98:
      throw paramContentResolver;
    }
  }
  
  public static class SettingNotFoundException
    extends AndroidException
  {
    public SettingNotFoundException(String paramString)
    {
      super();
    }
  }
  
  public static final class System
    extends Settings.NameValueTable
  {
    public static final String ACCELEROMETER_ROTATION = "accelerometer_rotation";
    public static final SettingsValidators.Validator ACCELEROMETER_ROTATION_VALIDATOR;
    @Deprecated
    public static final String ADB_ENABLED = "adb_enabled";
    public static final String ADVANCED_SETTINGS = "advanced_settings";
    public static final int ADVANCED_SETTINGS_DEFAULT = 0;
    private static final SettingsValidators.Validator ADVANCED_SETTINGS_VALIDATOR;
    @Deprecated
    public static final String AIRPLANE_MODE_ON = "airplane_mode_on";
    @Deprecated
    public static final String AIRPLANE_MODE_RADIOS = "airplane_mode_radios";
    @Deprecated
    public static final String AIRPLANE_MODE_TOGGLEABLE_RADIOS = "airplane_mode_toggleable_radios";
    public static final String ALARM_ALERT = "alarm_alert";
    public static final String ALARM_ALERT_CACHE = "alarm_alert_cache";
    public static final Uri ALARM_ALERT_CACHE_URI;
    private static final SettingsValidators.Validator ALARM_ALERT_VALIDATOR;
    @Deprecated
    public static final String ALWAYS_FINISH_ACTIVITIES = "always_finish_activities";
    @Deprecated
    public static final String ANDROID_ID = "android_id";
    @Deprecated
    public static final String ANIMATOR_DURATION_SCALE = "animator_duration_scale";
    public static final String APPEND_FOR_LAST_AUDIBLE = "_last_audible";
    public static final String ASUS_COVER_TAPTAP_FOR_CALL = "asus_cover_taptap_for_call";
    public static final String ASUS_DOUBLE_TAP = "asus_double_tap";
    public static final String ASUS_EASY_LAUNCHER = "asus_easy_launcher";
    public static final int ASUS_EASY_LAUNCHER_DISABLED = 0;
    public static final int ASUS_EASY_LAUNCHER_ENABLED = 1;
    public static final String ASUS_FIRST_SET = "asus_first_set";
    public static final String ASUS_LOCKSCREEN_DISPLAY_STATUS_BAR = "asus.lockscreen.display.statusbar";
    public static final String ASUS_LOCKSCREEN_LONGPRESS_INSTANT_CAMERA = "asus_lockscreen_longpress_instant_camera";
    public static final String ASUS_LOCKSCREEN_SHORTCUT_NAME = "asus_lockscreen_shortcut_name";
    public static final String ASUS_LOCKSCREEN_SKIP_SLID_DISABLED = "asus_lockscreen_skipslid_disabled";
    public static final String ASUS_LOCKSCREEN_WHATSNEXT = "asus_lockscreen_whatsnext";
    public static final String ASUS_MOTION_DOUBLE_CLICK = "asus_motion_double_click";
    public static final String ASUS_MOTION_FLIP = "asus_motion_flip";
    public static final String ASUS_MOTION_GESTURE_SETTINGS = "asus_motion_gesture_settings";
    public static final String ASUS_MOTION_HAND_UP = "asus_motion_hand_up";
    public static final String ASUS_MOTION_SHAKE = "asus_motion_shake";
    public static final String ASUS_ONE_HAND_OPERATION = "ASUS_ONE_HAND_OPERATION";
    public static final String ASUS_SHAKE_SENSITIVITY = "asus_shake_sensitivity";
    public static final String ASUS_TRANSCOVER = "asus_transcover";
    public static final String ASUS_TRANSCOVER_AUTOMATIC_UNLOCK = "asus_transcover_automatic_unlock";
    public static final int ASUS_TRANSCOVER_DEFAULT_MODE = 1;
    public static final int ASUS_TRANSCOVER_MODE_DISABLED = 0;
    public static final int ASUS_TRANSCOVER_MODE_ENABLED = 1;
    @Deprecated
    public static final String AUTO_TIME = "auto_time";
    private static final SettingsValidators.Validator AUTO_TIME_VALIDATOR;
    @Deprecated
    public static final String AUTO_TIME_ZONE = "auto_time_zone";
    private static final SettingsValidators.Validator AUTO_TIME_ZONE_VALIDATOR;
    public static final String BLUETOOTH_DISCOVERABILITY = "bluetooth_discoverability";
    public static final String BLUETOOTH_DISCOVERABILITY_TIMEOUT = "bluetooth_discoverability_timeout";
    private static final SettingsValidators.Validator BLUETOOTH_DISCOVERABILITY_TIMEOUT_VALIDATOR;
    private static final SettingsValidators.Validator BLUETOOTH_DISCOVERABILITY_VALIDATOR;
    @Deprecated
    public static final String BLUETOOTH_ON = "bluetooth_on";
    private static final SettingsValidators.Validator BLUETOOTH_ON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
    public static final String CALENDARALERT_SOUND = "calendaralert_sound";
    public static final String CALENDARALERT_SOUND_CACHE = "calendaralert_sound_cache";
    public static final Uri CALENDARALERT_SOUND_CACHE_URI;
    @Deprecated
    public static final String CAR_DOCK_SOUND = "car_dock_sound";
    @Deprecated
    public static final String CAR_UNDOCK_SOUND = "car_undock_sound";
    public static final Map<String, String> CLONE_FROM_PARENT_ON_VALUE;
    private static final Set<String> CLONE_TO_MANAGED_PROFILE;
    public static final Uri CONTENT_URI = Uri.parse("content://settings/system");
    @Deprecated
    public static final String DATA_ROAMING = "data_roaming";
    public static final String DATE_FORMAT = "date_format";
    public static final SettingsValidators.Validator DATE_FORMAT_VALIDATOR;
    @Deprecated
    public static final String DEBUG_APP = "debug_app";
    public static final Uri DEFAULT_ALARM_ALERT_URI;
    public static final Uri DEFAULT_CALENDARALERT_URI;
    private static final float DEFAULT_FONT_SCALE = 1.0F;
    public static final Uri DEFAULT_NEWMAIL_URI;
    public static final Uri DEFAULT_NOTIFICATION_URI;
    public static final Uri DEFAULT_NOTIFICATION_URI_2;
    public static final Uri DEFAULT_RINGTONES_TITLE_URI;
    public static final Uri DEFAULT_RINGTONE_URI;
    public static final Uri DEFAULT_RINGTONE_URI_2;
    public static final Uri DEFAULT_SENTMAIL_URI;
    @Deprecated
    public static final String DESK_DOCK_SOUND = "desk_dock_sound";
    @Deprecated
    public static final String DESK_UNDOCK_SOUND = "desk_undock_sound";
    public static final String DETECT_DRAIN_APPS = "detect_drain_apps";
    @Deprecated
    public static final String DEVICE_PROVISIONED = "device_provisioned";
    @Deprecated
    public static final String DIM_SCREEN = "dim_screen";
    private static final SettingsValidators.Validator DIM_SCREEN_VALIDATOR;
    public static final String DISPLAY_COLOR_MODE = "display_color_mode";
    private static final SettingsValidators.Validator DISPLAY_COLOR_MODE_VALIDATOR;
    @Deprecated
    public static final String DOCK_SOUNDS_ENABLED = "dock_sounds_enabled";
    private static final SettingsValidators.Validator DOCK_SOUNDS_ENABLED_VALIDATOR;
    public static final String DTMF_TONE_TYPE_WHEN_DIALING = "dtmf_tone_type";
    public static final SettingsValidators.Validator DTMF_TONE_TYPE_WHEN_DIALING_VALIDATOR;
    public static final String DTMF_TONE_WHEN_DIALING = "dtmf_tone";
    public static final SettingsValidators.Validator DTMF_TONE_WHEN_DIALING_VALIDATOR;
    public static final String EAP_SSID_PRELOADEDE = "eap_ssid_preloaded";
    public static final String EGG_MODE = "egg_mode";
    public static final SettingsValidators.Validator EGG_MODE_VALIDATOR;
    public static final String END_BUTTON_BEHAVIOR = "end_button_behavior";
    public static final int END_BUTTON_BEHAVIOR_DEFAULT = 2;
    public static final int END_BUTTON_BEHAVIOR_HOME = 1;
    public static final int END_BUTTON_BEHAVIOR_SLEEP = 2;
    private static final SettingsValidators.Validator END_BUTTON_BEHAVIOR_VALIDATOR;
    public static final String ETHERNET_ENABLE = "ethernet_enable";
    public static final String FLIPFONT = "flipfont";
    public static final String FONT_SCALE = "font_scale";
    private static final SettingsValidators.Validator FONT_SCALE_VALIDATOR;
    public static final String GESTURE_TYPE1_APP = "gesture_type1_app";
    public static final String GESTURE_TYPE2_APP = "gesture_type2_app";
    public static final String GESTURE_TYPE3_APP = "gesture_type3_app";
    public static final String GESTURE_TYPE4_APP = "gesture_type4_app";
    public static final String GESTURE_TYPE5_APP = "gesture_type5_app";
    public static final String GESTURE_TYPE6_APP = "gesture_type6_app";
    public static final String GLOVE_MODE = "glove_mode";
    public static final String GLOVE_MODE_CONFIRM_DIALOG = "glove_mode_confirm_dialog";
    public static final String HAPTIC_FEEDBACK_ENABLED = "haptic_feedback_enabled";
    public static final SettingsValidators.Validator HAPTIC_FEEDBACK_ENABLED_VALIDATOR;
    public static final String HAPTIC_FEEDBACK_INTENSITY = "haptic_feedback_intensity";
    public static final String HEARING_AID = "hearing_aid";
    public static final SettingsValidators.Validator HEARING_AID_VALIDATOR;
    public static final String HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY = "hide_rotation_lock_toggle_for_accessibility";
    public static final SettingsValidators.Validator HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY_VALIDATOR;
    public static final String HOTSPOT_DISABLE_POLICY = "hotspot_disable_policy";
    public static final String HOTSPOT_WHITELIST_ON = "hotspot_whitelist_on";
    @Deprecated
    public static final String HTTP_PROXY = "http_proxy";
    @Deprecated
    public static final String INSTALL_NON_MARKET_APPS = "install_non_market_apps";
    public static final Set<String> INSTANT_APP_SETTINGS;
    public static final String KEY_AICHARGING_ENABLED = "asus_aicharging_enable";
    public static final String KEY_INADVERTENT_TOUCH = "asus_inadvertent_touch_enable";
    public static final String[] LEGACY_RESTORE_SETTINGS;
    @Deprecated
    public static final String LOCATION_PROVIDERS_ALLOWED = "location_providers_allowed";
    public static final String LOCKSCREEN_DISABLED = "lockscreen.disabled";
    public static final SettingsValidators.Validator LOCKSCREEN_DISABLED_VALIDATOR;
    public static final String LOCKSCREEN_SOUNDS_ENABLED = "lockscreen_sounds_enabled";
    public static final SettingsValidators.Validator LOCKSCREEN_SOUNDS_ENABLED_VALIDATOR;
    @Deprecated
    public static final String LOCK_PATTERN_ENABLED = "lock_pattern_autolock";
    @Deprecated
    public static final String LOCK_PATTERN_TACTILE_FEEDBACK_ENABLED = "lock_pattern_tactile_feedback_enabled";
    @Deprecated
    public static final String LOCK_PATTERN_VISIBLE = "lock_pattern_visible_pattern";
    @Deprecated
    public static final String LOCK_SOUND = "lock_sound";
    public static final String LOCK_TO_APP_ENABLED = "lock_to_app_enabled";
    public static final SettingsValidators.Validator LOCK_TO_APP_ENABLED_VALIDATOR;
    @Deprecated
    public static final String LOGGING_ID = "logging_id";
    public static final String LONG_PRESSED_FUNC = "long_pressed_func";
    public static final int LONG_PRESSED_FUNC_DEFAULT = 0;
    public static final int LONG_PRESSED_FUNC_MENU = 3;
    public static final int LONG_PRESSED_FUNC_MULTIWINDOW = 1;
    public static final int LONG_PRESSED_FUNC_RECENTLIST = 2;
    public static final int LONG_PRESSED_FUNC_SCREENSHOT = 0;
    public static final int LONG_PRESSED_FUNC_STITCHIMAGE = 4;
    @Deprecated
    public static final String LOW_BATTERY_SOUND = "low_battery_sound";
    public static final String MASTER_MONO = "master_mono";
    private static final SettingsValidators.Validator MASTER_MONO_VALIDATOR;
    public static final String MEDIA_BUTTON_RECEIVER = "media_button_receiver";
    private static final SettingsValidators.Validator MEDIA_BUTTON_RECEIVER_VALIDATOR;
    public static final String MIDDLE_BUTTON_MAPPING = "middle_button_mapping";
    @Deprecated
    public static final String MOBILE_SLEEP_POLICY = "mobile_sleep_policy";
    @Deprecated
    public static final int MOBILE_SLEEP_POLICY_DEFAULT = 0;
    @Deprecated
    public static final int MOBILE_SLEEP_POLICY_NEVER = 2;
    @Deprecated
    public static final int MOBILE_SLEEP_POLICY_NEVER_WHILE_PLUGGED = 1;
    @Deprecated
    public static final String MODE_RINGER = "mode_ringer";
    public static final String MODE_RINGER_STREAMS_AFFECTED = "mode_ringer_streams_affected";
    private static final SettingsValidators.Validator MODE_RINGER_STREAMS_AFFECTED_VALIDATOR;
    private static final HashSet<String> MOVED_TO_GLOBAL;
    private static final HashSet<String> MOVED_TO_SECURE;
    private static final HashSet<String> MOVED_TO_SECURE_THEN_GLOBAL;
    public static final String MUTE_STREAMS_AFFECTED = "mute_streams_affected";
    private static final SettingsValidators.Validator MUTE_STREAMS_AFFECTED_VALIDATOR;
    @Deprecated
    public static final String NETWORK_PREFERENCE = "network_preference";
    public static final String NEWMAIL_SOUND = "newmail_sound";
    public static final String NEWMAIL_SOUND_CACHE = "newmail_sound_cache";
    public static final Uri NEWMAIL_SOUND_CACHE_URI;
    @Deprecated
    public static final String NEXT_ALARM_FORMATTED = "next_alarm_formatted";
    private static final SettingsValidators.Validator NEXT_ALARM_FORMATTED_VALIDATOR;
    @Deprecated
    public static final String NOTIFICATIONS_USE_RING_VOLUME = "notifications_use_ring_volume";
    private static final SettingsValidators.Validator NOTIFICATIONS_USE_RING_VOLUME_VALIDATOR;
    public static final String NOTIFICATION_LIGHT_PULSE = "notification_light_pulse";
    public static final SettingsValidators.Validator NOTIFICATION_LIGHT_PULSE_VALIDATOR;
    public static final String NOTIFICATION_SOUND = "notification_sound";
    public static final String NOTIFICATION_SOUND_2 = "notification_sound_2";
    public static final String NOTIFICATION_SOUND_2_CACHE = "notification_sound_2_cache";
    public static final Uri NOTIFICATION_SOUND_2_CACHE_URI;
    public static final String NOTIFICATION_SOUND_CACHE = "notification_sound_cache";
    public static final Uri NOTIFICATION_SOUND_CACHE_URI;
    private static final SettingsValidators.Validator NOTIFICATION_SOUND_VALIDATOR;
    public static final String NOTIFICATION_VIBRATION_INTENSITY = "notification_vibration_intensity";
    public static final String OPTIFLEX = "optiflex";
    public static final String OTG_STATUS = "otg_status";
    @Deprecated
    public static final String PARENTAL_CONTROL_ENABLED = "parental_control_enabled";
    @Deprecated
    public static final String PARENTAL_CONTROL_LAST_UPDATE = "parental_control_last_update";
    @Deprecated
    public static final String PARENTAL_CONTROL_REDIRECT_URL = "parental_control_redirect_url";
    public static final String POINTER_LOCATION = "pointer_location";
    public static final SettingsValidators.Validator POINTER_LOCATION_VALIDATOR;
    public static final String POINTER_SPEED = "pointer_speed";
    public static final SettingsValidators.Validator POINTER_SPEED_VALIDATOR;
    public static final String POWER_SAVER_ENABLED = "power_saver_enabled";
    @Deprecated
    public static final String POWER_SOUNDS_ENABLED = "power_sounds_enabled";
    private static final SettingsValidators.Validator POWER_SOUNDS_ENABLED_VALIDATOR;
    public static final Set<String> PRIVATE_SETTINGS;
    public static final Set<String> PUBLIC_SETTINGS;
    @Deprecated
    public static final String RADIO_BLUETOOTH = "bluetooth";
    @Deprecated
    public static final String RADIO_CELL = "cell";
    @Deprecated
    public static final String RADIO_NFC = "nfc";
    @Deprecated
    public static final String RADIO_WIFI = "wifi";
    @Deprecated
    public static final String RADIO_WIMAX = "wimax";
    public static final String RIGHT_BUTTON_MAPPING = "right_button_mapping";
    public static final String RINGTONE = "ringtone";
    public static final String RINGTONES_TITLE = "ringtones_title";
    public static final String RINGTONES_TITLE_CACHE = "ringtones_title_cache";
    public static final Uri RINGTONES_TITLE_CACHE_URI;
    public static final String RINGTONE_2 = "ringtone_2";
    public static final String RINGTONE_2_CACHE = "ringtone_2_cache";
    public static final Uri RINGTONE_2_CACHE_URI;
    public static final String RINGTONE_CACHE = "ringtone_cache";
    public static final Uri RINGTONE_CACHE_URI;
    private static final SettingsValidators.Validator RINGTONE_VALIDATOR;
    public static final String RINGTONE_VIBRATION_INTENSITY = "ringtone_vibration_intensity";
    public static final String SCREENSHOT = "screenshot";
    public static final String SCREENSHOT_FORMAT = "screenshot_format";
    public static final int SCREENSHOT_FORMAT_DEFAULT = 0;
    public static final int SCREENSHOT_FORMAT_JPG = 0;
    public static final int SCREENSHOT_FORMAT_PNG = 1;
    public static final String SCREENSHOT_NOTIFY = "screenshot_notify";
    public static final String SCREENSHOT_SOUND = "screenshot_sound";
    public static final String SCREEN_AUTO_BRIGHTNESS_ADJ = "screen_auto_brightness_adj";
    private static final SettingsValidators.Validator SCREEN_AUTO_BRIGHTNESS_ADJ_VALIDATOR;
    public static final String SCREEN_BRIGHTNESS = "screen_brightness";
    public static final String SCREEN_BRIGHTNESS_FOR_VR = "screen_brightness_for_vr";
    private static final SettingsValidators.Validator SCREEN_BRIGHTNESS_FOR_VR_VALIDATOR;
    public static final String SCREEN_BRIGHTNESS_MODE = "screen_brightness_mode";
    public static final int SCREEN_BRIGHTNESS_MODE_AUTOMATIC = 1;
    public static final int SCREEN_BRIGHTNESS_MODE_MANUAL = 0;
    private static final SettingsValidators.Validator SCREEN_BRIGHTNESS_MODE_VALIDATOR;
    public static final String SCREEN_OFF_TIMEOUT = "screen_off_timeout";
    private static final SettingsValidators.Validator SCREEN_OFF_TIMEOUT_VALIDATOR;
    public static final String SENTMAIL_SOUND = "sentmail_sound";
    public static final String SENTMAIL_SOUND_CACHE = "sentmail_sound_cache";
    public static final Uri SENTMAIL_SOUND_CACHE_URI;
    @Deprecated
    public static final String SETTINGS_CLASSNAME = "settings_classname";
    public static final String[] SETTINGS_TO_BACKUP;
    public static final String SETUP_WIZARD_HAS_RUN = "setup_wizard_has_run";
    public static final SettingsValidators.Validator SETUP_WIZARD_HAS_RUN_VALIDATOR;
    public static final String SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";
    private static final SettingsValidators.Validator SHOW_BATTERY_PERCENT_VALIDATOR;
    public static final String SHOW_GTALK_SERVICE_STATUS = "SHOW_GTALK_SERVICE_STATUS";
    private static final SettingsValidators.Validator SHOW_GTALK_SERVICE_STATUS_VALIDATOR;
    @Deprecated
    public static final String SHOW_PROCESSES = "show_processes";
    public static final String SHOW_TOUCHES = "show_touches";
    public static final SettingsValidators.Validator SHOW_TOUCHES_VALIDATOR;
    @Deprecated
    public static final String SHOW_WEB_SUGGESTIONS = "show_web_suggestions";
    public static final SettingsValidators.Validator SHOW_WEB_SUGGESTIONS_VALIDATOR;
    public static final String SIP_ADDRESS_ONLY = "SIP_ADDRESS_ONLY";
    public static final SettingsValidators.Validator SIP_ADDRESS_ONLY_VALIDATOR;
    public static final String SIP_ALWAYS = "SIP_ALWAYS";
    public static final SettingsValidators.Validator SIP_ALWAYS_VALIDATOR;
    @Deprecated
    public static final String SIP_ASK_ME_EACH_TIME = "SIP_ASK_ME_EACH_TIME";
    public static final SettingsValidators.Validator SIP_ASK_ME_EACH_TIME_VALIDATOR;
    public static final String SIP_CALL_OPTIONS = "sip_call_options";
    public static final SettingsValidators.Validator SIP_CALL_OPTIONS_VALIDATOR;
    public static final String SIP_RECEIVE_CALLS = "sip_receive_calls";
    public static final SettingsValidators.Validator SIP_RECEIVE_CALLS_VALIDATOR;
    public static final String SOUND_EFFECTS_ENABLED = "sound_effects_enabled";
    public static final SettingsValidators.Validator SOUND_EFFECTS_ENABLED_VALIDATOR;
    @Deprecated
    public static final String STAY_ON_WHILE_PLUGGED_IN = "stay_on_while_plugged_in";
    private static final SettingsValidators.Validator STAY_ON_WHILE_PLUGGED_IN_VALIDATOR;
    public static final String SUGGESTION_APP = "suggesiton_app";
    public static final String SWITCH_ALL_APPS_ENABLED = "all_apps_enabled";
    public static final SettingsValidators.Validator SWITCH_ALL_APPS_ENABLED_VALIDATOR;
    public static final String SYSTEM_LOCALES = "system_locales";
    public static final String TEXT_AUTO_CAPS = "auto_caps";
    private static final SettingsValidators.Validator TEXT_AUTO_CAPS_VALIDATOR;
    public static final String TEXT_AUTO_PUNCTUATE = "auto_punctuate";
    private static final SettingsValidators.Validator TEXT_AUTO_PUNCTUATE_VALIDATOR;
    public static final String TEXT_AUTO_REPLACE = "auto_replace";
    private static final SettingsValidators.Validator TEXT_AUTO_REPLACE_VALIDATOR;
    public static final String TEXT_SHOW_PASSWORD = "show_password";
    private static final SettingsValidators.Validator TEXT_SHOW_PASSWORD_VALIDATOR;
    public static final String TIME_12_24 = "time_12_24";
    public static final SettingsValidators.Validator TIME_12_24_VALIDATOR;
    public static final String TOUCHPAD_MODE = "touchpad_mode";
    public static final int TOUCHPAD_MODE_ANDROID = 0;
    public static final int TOUCHPAD_MODE_CLASSIC = 1;
    @Deprecated
    public static final String TRANSITION_ANIMATION_SCALE = "transition_animation_scale";
    public static final String TTY_MODE = "tty_mode";
    public static final SettingsValidators.Validator TTY_MODE_VALIDATOR;
    @Deprecated
    public static final String UNLOCK_SOUND = "unlock_sound";
    @Deprecated
    public static final String USB_MASS_STORAGE_ENABLED = "usb_mass_storage_enabled";
    private static final SettingsValidators.Validator USB_MASS_STORAGE_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
    public static final String USER_ROTATION = "user_rotation";
    public static final SettingsValidators.Validator USER_ROTATION_VALIDATOR;
    @Deprecated
    public static final String USE_GOOGLE_MAIL = "use_google_mail";
    public static final Map<String, SettingsValidators.Validator> VALIDATORS;
    public static final String VERIZON_TEMPSHUTDOWN = "verizon_tempshutdown";
    public static final String VIBRATE_INPUT_DEVICES = "vibrate_input_devices";
    private static final SettingsValidators.Validator VIBRATE_INPUT_DEVICES_VALIDATOR;
    public static final String VIBRATE_IN_SILENT = "vibrate_in_silent";
    private static final SettingsValidators.Validator VIBRATE_IN_SILENT_VALIDATOR;
    public static final String VIBRATE_ON = "vibrate_on";
    private static final SettingsValidators.Validator VIBRATE_ON_VALIDATOR;
    public static final String VIBRATE_WHEN_RINGING = "vibrate_when_ringing";
    public static final SettingsValidators.Validator VIBRATE_WHEN_RINGING_VALIDATOR;
    private static final SettingsValidators.Validator VIBRATION_INTENSITY_VALIDATOR;
    public static final String VOLUME_ACCESSIBILITY = "volume_a11y";
    public static final String VOLUME_ALARM = "volume_alarm";
    public static final String VOLUME_BLUETOOTH_SCO = "volume_bluetooth_sco";
    public static final String VOLUME_MASTER = "volume_master";
    public static final String VOLUME_MUSIC = "volume_music";
    public static final String VOLUME_NOTIFICATION = "volume_notification";
    public static final String VOLUME_RING = "volume_ring";
    public static final String[] VOLUME_SETTINGS;
    public static final String[] VOLUME_SETTINGS_INT;
    public static final String VOLUME_SYSTEM = "volume_system";
    public static final String VOLUME_VOICE = "volume_voice";
    @Deprecated
    public static final String WAIT_FOR_DEBUGGER = "wait_for_debugger";
    @Deprecated
    public static final String WALLPAPER_ACTIVITY = "wallpaper_activity";
    private static final SettingsValidators.Validator WALLPAPER_ACTIVITY_VALIDATOR;
    public static final String WHEN_TO_MAKE_WIFI_CALLS = "when_to_make_wifi_calls";
    @Deprecated
    public static final String WIFI_MAX_DHCP_RETRY_COUNT = "wifi_max_dhcp_retry_count";
    @Deprecated
    public static final String WIFI_MOBILE_DATA_TRANSITION_WAKELOCK_TIMEOUT_MS = "wifi_mobile_data_transition_wakelock_timeout_ms";
    @Deprecated
    public static final String WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON = "wifi_networks_available_notification_on";
    private static final SettingsValidators.Validator WIFI_NETWORKS_AVAILABLE_NOTIFICATION_ON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
    @Deprecated
    public static final String WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY = "wifi_networks_available_repeat_delay";
    private static final SettingsValidators.Validator WIFI_NETWORKS_AVAILABLE_REPEAT_DELAY_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
    @Deprecated
    public static final String WIFI_NUM_OPEN_NETWORKS_KEPT = "wifi_num_open_networks_kept";
    private static final SettingsValidators.Validator WIFI_NUM_OPEN_NETWORKS_KEPT_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
    @Deprecated
    public static final String WIFI_ON = "wifi_on";
    @Deprecated
    public static final String WIFI_SLEEP_POLICY = "wifi_sleep_policy";
    @Deprecated
    public static final int WIFI_SLEEP_POLICY_DEFAULT = 0;
    @Deprecated
    public static final int WIFI_SLEEP_POLICY_NEVER = 2;
    @Deprecated
    public static final int WIFI_SLEEP_POLICY_NEVER_WHILE_PLUGGED = 1;
    @Deprecated
    public static final String WIFI_STATIC_DNS1 = "wifi_static_dns1";
    private static final SettingsValidators.Validator WIFI_STATIC_DNS1_VALIDATOR;
    @Deprecated
    public static final String WIFI_STATIC_DNS2 = "wifi_static_dns2";
    private static final SettingsValidators.Validator WIFI_STATIC_DNS2_VALIDATOR;
    @Deprecated
    public static final String WIFI_STATIC_GATEWAY = "wifi_static_gateway";
    private static final SettingsValidators.Validator WIFI_STATIC_GATEWAY_VALIDATOR;
    @Deprecated
    public static final String WIFI_STATIC_IP = "wifi_static_ip";
    private static final SettingsValidators.Validator WIFI_STATIC_IP_VALIDATOR;
    @Deprecated
    public static final String WIFI_STATIC_NETMASK = "wifi_static_netmask";
    private static final SettingsValidators.Validator WIFI_STATIC_NETMASK_VALIDATOR;
    @Deprecated
    public static final String WIFI_USE_STATIC_IP = "wifi_use_static_ip";
    private static final SettingsValidators.Validator WIFI_USE_STATIC_IP_VALIDATOR;
    public static final String WIFI_VALID_ENABLE = "wifi_valid_enable";
    @Deprecated
    public static final String WIFI_WATCHDOG_ACCEPTABLE_PACKET_LOSS_PERCENTAGE = "wifi_watchdog_acceptable_packet_loss_percentage";
    @Deprecated
    public static final String WIFI_WATCHDOG_AP_COUNT = "wifi_watchdog_ap_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_DELAY_MS = "wifi_watchdog_background_check_delay_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_ENABLED = "wifi_watchdog_background_check_enabled";
    @Deprecated
    public static final String WIFI_WATCHDOG_BACKGROUND_CHECK_TIMEOUT_MS = "wifi_watchdog_background_check_timeout_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_INITIAL_IGNORED_PING_COUNT = "wifi_watchdog_initial_ignored_ping_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_MAX_AP_CHECKS = "wifi_watchdog_max_ap_checks";
    @Deprecated
    public static final String WIFI_WATCHDOG_ON = "wifi_watchdog_on";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_COUNT = "wifi_watchdog_ping_count";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_DELAY_MS = "wifi_watchdog_ping_delay_ms";
    @Deprecated
    public static final String WIFI_WATCHDOG_PING_TIMEOUT_MS = "wifi_watchdog_ping_timeout_ms";
    @Deprecated
    public static final String WINDOW_ANIMATION_SCALE = "window_animation_scale";
    public static final String WINDOW_ORIENTATION_LISTENER_LOG = "window_orientation_listener_log";
    public static final SettingsValidators.Validator WINDOW_ORIENTATION_LISTENER_LOG_VALIDATOR;
    private static final Settings.NameValueCache sNameValueCache;
    private static final Settings.ContentProviderHolder sProviderHolder = new Settings.ContentProviderHolder(CONTENT_URI);
    
    static
    {
      sNameValueCache = new Settings.NameValueCache(CONTENT_URI, "GET_system", "PUT_system", sProviderHolder);
      MOVED_TO_SECURE = new HashSet(30);
      MOVED_TO_SECURE.add("android_id");
      MOVED_TO_SECURE.add("http_proxy");
      MOVED_TO_SECURE.add("location_providers_allowed");
      MOVED_TO_SECURE.add("lock_biometric_weak_flags");
      MOVED_TO_SECURE.add("lock_pattern_autolock");
      MOVED_TO_SECURE.add("lock_pattern_visible_pattern");
      MOVED_TO_SECURE.add("lock_pattern_tactile_feedback_enabled");
      MOVED_TO_SECURE.add("logging_id");
      MOVED_TO_SECURE.add("parental_control_enabled");
      MOVED_TO_SECURE.add("parental_control_last_update");
      MOVED_TO_SECURE.add("parental_control_redirect_url");
      MOVED_TO_SECURE.add("settings_classname");
      MOVED_TO_SECURE.add("use_google_mail");
      MOVED_TO_SECURE.add("wifi_networks_available_notification_on");
      MOVED_TO_SECURE.add("wifi_networks_available_repeat_delay");
      MOVED_TO_SECURE.add("wifi_num_open_networks_kept");
      MOVED_TO_SECURE.add("wifi_on");
      MOVED_TO_SECURE.add("wifi_watchdog_acceptable_packet_loss_percentage");
      MOVED_TO_SECURE.add("wifi_watchdog_ap_count");
      MOVED_TO_SECURE.add("wifi_watchdog_background_check_delay_ms");
      MOVED_TO_SECURE.add("wifi_watchdog_background_check_enabled");
      MOVED_TO_SECURE.add("wifi_watchdog_background_check_timeout_ms");
      MOVED_TO_SECURE.add("wifi_watchdog_initial_ignored_ping_count");
      MOVED_TO_SECURE.add("wifi_watchdog_max_ap_checks");
      MOVED_TO_SECURE.add("wifi_watchdog_on");
      MOVED_TO_SECURE.add("wifi_watchdog_ping_count");
      MOVED_TO_SECURE.add("wifi_watchdog_ping_delay_ms");
      MOVED_TO_SECURE.add("wifi_watchdog_ping_timeout_ms");
      MOVED_TO_SECURE.add("install_non_market_apps");
      MOVED_TO_SECURE.add("asus_lockscreen_instant_camera");
      MOVED_TO_GLOBAL = new HashSet();
      MOVED_TO_SECURE_THEN_GLOBAL = new HashSet();
      MOVED_TO_SECURE_THEN_GLOBAL.add("adb_enabled");
      MOVED_TO_SECURE_THEN_GLOBAL.add("bluetooth_on");
      MOVED_TO_SECURE_THEN_GLOBAL.add("data_roaming");
      MOVED_TO_SECURE_THEN_GLOBAL.add("device_provisioned");
      MOVED_TO_SECURE_THEN_GLOBAL.add("usb_mass_storage_enabled");
      MOVED_TO_SECURE_THEN_GLOBAL.add("http_proxy");
      MOVED_TO_GLOBAL.add("airplane_mode_on");
      MOVED_TO_GLOBAL.add("airplane_mode_radios");
      MOVED_TO_GLOBAL.add("airplane_mode_toggleable_radios");
      MOVED_TO_GLOBAL.add("auto_time");
      MOVED_TO_GLOBAL.add("auto_time_zone");
      MOVED_TO_GLOBAL.add("car_dock_sound");
      MOVED_TO_GLOBAL.add("car_undock_sound");
      MOVED_TO_GLOBAL.add("desk_dock_sound");
      MOVED_TO_GLOBAL.add("desk_undock_sound");
      MOVED_TO_GLOBAL.add("dock_sounds_enabled");
      MOVED_TO_GLOBAL.add("lock_sound");
      MOVED_TO_GLOBAL.add("unlock_sound");
      MOVED_TO_GLOBAL.add("low_battery_sound");
      MOVED_TO_GLOBAL.add("power_sounds_enabled");
      MOVED_TO_GLOBAL.add("stay_on_while_plugged_in");
      MOVED_TO_GLOBAL.add("wifi_sleep_policy");
      MOVED_TO_GLOBAL.add("mobile_sleep_policy");
      MOVED_TO_GLOBAL.add("mode_ringer");
      MOVED_TO_GLOBAL.add("window_animation_scale");
      MOVED_TO_GLOBAL.add("transition_animation_scale");
      MOVED_TO_GLOBAL.add("animator_duration_scale");
      MOVED_TO_GLOBAL.add("fancy_ime_animations");
      MOVED_TO_GLOBAL.add("compatibility_mode");
      MOVED_TO_GLOBAL.add("emergency_tone");
      MOVED_TO_GLOBAL.add("call_auto_retry");
      MOVED_TO_GLOBAL.add("debug_app");
      MOVED_TO_GLOBAL.add("wait_for_debugger");
      MOVED_TO_GLOBAL.add("always_finish_activities");
      MOVED_TO_GLOBAL.add("tzinfo_content_url");
      MOVED_TO_GLOBAL.add("tzinfo_metadata_url");
      MOVED_TO_GLOBAL.add("selinux_content_url");
      MOVED_TO_GLOBAL.add("selinux_metadata_url");
      MOVED_TO_GLOBAL.add("sms_short_codes_content_url");
      MOVED_TO_GLOBAL.add("sms_short_codes_metadata_url");
      MOVED_TO_GLOBAL.add("cert_pin_content_url");
      MOVED_TO_GLOBAL.add("cert_pin_metadata_url");
      MOVED_TO_GLOBAL.add("power_saver_enabled");
      STAY_ON_WHILE_PLUGGED_IN_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          boolean bool = false;
          try
          {
            int i = Integer.parseInt(paramAnonymousString);
            if ((i != 0) && (i != 1) && (i != 2) && (i != 4) && (i != 3) && (i != 5) && (i != 6) && (i != 7)) {
              break label56;
            }
            bool = true;
            label56:
            return bool;
          }
          catch (NumberFormatException paramAnonymousString) {}
          return false;
        }
      };
      END_BUTTON_BEHAVIOR_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 3);
      ADVANCED_SETTINGS_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WIFI_USE_STATIC_IP_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WIFI_STATIC_IP_VALIDATOR = SettingsValidators.LENIENT_IP_ADDRESS_VALIDATOR;
      WIFI_STATIC_GATEWAY_VALIDATOR = SettingsValidators.LENIENT_IP_ADDRESS_VALIDATOR;
      WIFI_STATIC_NETMASK_VALIDATOR = SettingsValidators.LENIENT_IP_ADDRESS_VALIDATOR;
      WIFI_STATIC_DNS1_VALIDATOR = SettingsValidators.LENIENT_IP_ADDRESS_VALIDATOR;
      WIFI_STATIC_DNS2_VALIDATOR = SettingsValidators.LENIENT_IP_ADDRESS_VALIDATOR;
      BLUETOOTH_DISCOVERABILITY_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 2);
      BLUETOOTH_DISCOVERABILITY_TIMEOUT_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      NEXT_ALARM_FORMATTED_VALIDATOR = new SettingsValidators.Validator()
      {
        private static final int MAX_LENGTH = 1000;
        
        public boolean validate(String paramAnonymousString)
        {
          boolean bool;
          if ((paramAnonymousString != null) && (paramAnonymousString.length() >= 1000)) {
            bool = false;
          } else {
            bool = true;
          }
          return bool;
        }
      };
      FONT_SCALE_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          boolean bool = false;
          try
          {
            float f = Float.parseFloat(paramAnonymousString);
            if (f >= 0.0F) {
              bool = true;
            }
            return bool;
          }
          catch (NumberFormatException|NullPointerException paramAnonymousString) {}
          return false;
        }
      };
      DIM_SCREEN_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      DISPLAY_COLOR_MODE_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 3);
      SCREEN_OFF_TIMEOUT_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      SCREEN_BRIGHTNESS_FOR_VR_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 255);
      SCREEN_BRIGHTNESS_MODE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SCREEN_AUTO_BRIGHTNESS_ADJ_VALIDATOR = new SettingsValidators.InclusiveFloatRangeValidator(-1.0F, 1.0F);
      MODE_RINGER_STREAMS_AFFECTED_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      MUTE_STREAMS_AFFECTED_VALIDATOR = SettingsValidators.NON_NEGATIVE_INTEGER_VALIDATOR;
      VIBRATE_ON_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      VIBRATE_INPUT_DEVICES_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      VIBRATION_INTENSITY_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 3);
      MASTER_MONO_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      NOTIFICATIONS_USE_RING_VOLUME_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      VIBRATE_IN_SILENT_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      VOLUME_SETTINGS = new String[] { "volume_voice", "volume_system", "volume_ring", "volume_music", "volume_alarm", "volume_notification", "volume_bluetooth_sco" };
      VOLUME_SETTINGS_INT = new String[] { "volume_voice", "volume_system", "volume_ring", "volume_music", "volume_alarm", "volume_notification", "volume_bluetooth_sco", "", "", "", "volume_a11y" };
      RINGTONE_VALIDATOR = SettingsValidators.URI_VALIDATOR;
      DEFAULT_RINGTONE_URI = getUriFor("ringtone");
      RINGTONE_CACHE_URI = getUriFor("ringtone_cache");
      NOTIFICATION_SOUND_VALIDATOR = SettingsValidators.URI_VALIDATOR;
      DEFAULT_NOTIFICATION_URI = getUriFor("notification_sound");
      NOTIFICATION_SOUND_CACHE_URI = getUriFor("notification_sound_cache");
      ALARM_ALERT_VALIDATOR = SettingsValidators.URI_VALIDATOR;
      DEFAULT_ALARM_ALERT_URI = getUriFor("alarm_alert");
      ALARM_ALERT_CACHE_URI = getUriFor("alarm_alert_cache");
      MEDIA_BUTTON_RECEIVER_VALIDATOR = SettingsValidators.COMPONENT_NAME_VALIDATOR;
      TEXT_AUTO_REPLACE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      TEXT_AUTO_CAPS_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      TEXT_AUTO_PUNCTUATE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      TEXT_SHOW_PASSWORD_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SHOW_GTALK_SERVICE_STATUS_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WALLPAPER_ACTIVITY_VALIDATOR = new SettingsValidators.Validator()
      {
        private static final int MAX_LENGTH = 1000;
        
        public boolean validate(String paramAnonymousString)
        {
          boolean bool = false;
          if ((paramAnonymousString != null) && (paramAnonymousString.length() > 1000)) {
            return false;
          }
          if (ComponentName.unflattenFromString(paramAnonymousString) != null) {
            bool = true;
          }
          return bool;
        }
      };
      AUTO_TIME_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      AUTO_TIME_ZONE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      TIME_12_24_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "12", "24", null });
      DEFAULT_NEWMAIL_URI = getUriFor("newmail_sound");
      DEFAULT_SENTMAIL_URI = getUriFor("sentmail_sound");
      DEFAULT_CALENDARALERT_URI = getUriFor("calendaralert_sound");
      NEWMAIL_SOUND_CACHE_URI = getUriFor("newmail_sound_cache");
      SENTMAIL_SOUND_CACHE_URI = getUriFor("sentmail_sound_cache");
      CALENDARALERT_SOUND_CACHE_URI = getUriFor("calendaralert_sound_cache");
      DEFAULT_RINGTONES_TITLE_URI = getUriFor("ringtones_title");
      RINGTONES_TITLE_CACHE_URI = getUriFor("ringtones_title_cache");
      DATE_FORMAT_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          try
          {
            new SimpleDateFormat(paramAnonymousString);
            return true;
          }
          catch (IllegalArgumentException|NullPointerException paramAnonymousString) {}
          return false;
        }
      };
      DEFAULT_RINGTONE_URI_2 = getUriFor("ringtone_2");
      DEFAULT_NOTIFICATION_URI_2 = getUriFor("notification_sound_2");
      RINGTONE_2_CACHE_URI = getUriFor("ringtone_2_cache");
      NOTIFICATION_SOUND_2_CACHE_URI = getUriFor("notification_sound_2_cache");
      SETUP_WIZARD_HAS_RUN_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      ACCELEROMETER_ROTATION_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      USER_ROTATION_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 3);
      HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      VIBRATE_WHEN_RINGING_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SWITCH_ALL_APPS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      DTMF_TONE_WHEN_DIALING_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      DTMF_TONE_TYPE_WHEN_DIALING_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      HEARING_AID_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      TTY_MODE_VALIDATOR = new SettingsValidators.InclusiveIntegerRangeValidator(0, 3);
      SOUND_EFFECTS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      HAPTIC_FEEDBACK_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SHOW_WEB_SUGGESTIONS_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      NOTIFICATION_LIGHT_PULSE_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      POINTER_LOCATION_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SHOW_TOUCHES_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      WINDOW_ORIENTATION_LISTENER_LOG_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      POWER_SOUNDS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      DOCK_SOUNDS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      LOCKSCREEN_SOUNDS_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      LOCKSCREEN_DISABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SIP_RECEIVE_CALLS_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SIP_CALL_OPTIONS_VALIDATOR = new SettingsValidators.DiscreteValueValidator(new String[] { "SIP_ALWAYS", "SIP_ADDRESS_ONLY" });
      SIP_ALWAYS_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SIP_ADDRESS_ONLY_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SIP_ASK_ME_EACH_TIME_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      POINTER_SPEED_VALIDATOR = new SettingsValidators.InclusiveFloatRangeValidator(-7.0F, 7.0F);
      LOCK_TO_APP_ENABLED_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      EGG_MODE_VALIDATOR = new SettingsValidators.Validator()
      {
        public boolean validate(String paramAnonymousString)
        {
          boolean bool = false;
          try
          {
            long l = Long.parseLong(paramAnonymousString);
            if (l >= 0L) {
              bool = true;
            }
            return bool;
          }
          catch (NumberFormatException paramAnonymousString) {}
          return false;
        }
      };
      SHOW_BATTERY_PERCENT_VALIDATOR = SettingsValidators.BOOLEAN_VALIDATOR;
      SETTINGS_TO_BACKUP = new String[] { "stay_on_while_plugged_in", "wifi_use_static_ip", "wifi_static_ip", "wifi_static_gateway", "wifi_static_netmask", "wifi_static_dns1", "wifi_static_dns2", "bluetooth_discoverability", "bluetooth_discoverability_timeout", "font_scale", "dim_screen", "screen_off_timeout", "screen_brightness_mode", "screen_auto_brightness_adj", "screen_brightness_for_vr", "vibrate_input_devices", "mode_ringer_streams_affected", "auto_replace", "auto_caps", "auto_punctuate", "show_password", "auto_time", "auto_time_zone", "time_12_24", "date_format", "dtmf_tone", "all_apps_enabled", "dtmf_tone_type", "hearing_aid", "tty_mode", "master_mono", "sound_effects_enabled", "haptic_feedback_enabled", "power_sounds_enabled", "dock_sounds_enabled", "lockscreen_sounds_enabled", "show_web_suggestions", "sip_call_options", "sip_receive_calls", "pointer_speed", "vibrate_when_ringing", "ringtone", "lock_to_app_enabled", "notification_sound", "accelerometer_rotation", "status_bar_show_battery_percent", "notification_vibration_intensity", "ringtone_vibration_intensity", "haptic_feedback_intensity", "display_color_mode" };
      LEGACY_RESTORE_SETTINGS = new String[0];
      PUBLIC_SETTINGS = new ArraySet();
      PUBLIC_SETTINGS.add("end_button_behavior");
      PUBLIC_SETTINGS.add("wifi_use_static_ip");
      PUBLIC_SETTINGS.add("wifi_static_ip");
      PUBLIC_SETTINGS.add("wifi_static_gateway");
      PUBLIC_SETTINGS.add("wifi_static_netmask");
      PUBLIC_SETTINGS.add("wifi_static_dns1");
      PUBLIC_SETTINGS.add("wifi_static_dns2");
      PUBLIC_SETTINGS.add("bluetooth_discoverability");
      PUBLIC_SETTINGS.add("bluetooth_discoverability_timeout");
      PUBLIC_SETTINGS.add("next_alarm_formatted");
      PUBLIC_SETTINGS.add("font_scale");
      PUBLIC_SETTINGS.add("dim_screen");
      PUBLIC_SETTINGS.add("screen_off_timeout");
      PUBLIC_SETTINGS.add("screen_brightness");
      PUBLIC_SETTINGS.add("screen_brightness_for_vr");
      PUBLIC_SETTINGS.add("screen_brightness_mode");
      PUBLIC_SETTINGS.add("mode_ringer_streams_affected");
      PUBLIC_SETTINGS.add("mute_streams_affected");
      PUBLIC_SETTINGS.add("vibrate_on");
      PUBLIC_SETTINGS.add("volume_ring");
      PUBLIC_SETTINGS.add("volume_system");
      PUBLIC_SETTINGS.add("volume_voice");
      PUBLIC_SETTINGS.add("volume_music");
      PUBLIC_SETTINGS.add("volume_alarm");
      PUBLIC_SETTINGS.add("volume_notification");
      PUBLIC_SETTINGS.add("volume_bluetooth_sco");
      PUBLIC_SETTINGS.add("ringtone");
      PUBLIC_SETTINGS.add("notification_sound");
      PUBLIC_SETTINGS.add("alarm_alert");
      PUBLIC_SETTINGS.add("auto_replace");
      PUBLIC_SETTINGS.add("auto_caps");
      PUBLIC_SETTINGS.add("auto_punctuate");
      PUBLIC_SETTINGS.add("show_password");
      PUBLIC_SETTINGS.add("SHOW_GTALK_SERVICE_STATUS");
      PUBLIC_SETTINGS.add("wallpaper_activity");
      PUBLIC_SETTINGS.add("time_12_24");
      PUBLIC_SETTINGS.add("date_format");
      PUBLIC_SETTINGS.add("setup_wizard_has_run");
      PUBLIC_SETTINGS.add("accelerometer_rotation");
      PUBLIC_SETTINGS.add("user_rotation");
      PUBLIC_SETTINGS.add("dtmf_tone");
      PUBLIC_SETTINGS.add("all_apps_enabled");
      PUBLIC_SETTINGS.add("sound_effects_enabled");
      PUBLIC_SETTINGS.add("haptic_feedback_enabled");
      PUBLIC_SETTINGS.add("show_web_suggestions");
      PUBLIC_SETTINGS.add("vibrate_when_ringing");
      PRIVATE_SETTINGS = new ArraySet();
      PRIVATE_SETTINGS.add("wifi_use_static_ip");
      PRIVATE_SETTINGS.add("end_button_behavior");
      PRIVATE_SETTINGS.add("advanced_settings");
      PRIVATE_SETTINGS.add("screen_auto_brightness_adj");
      PRIVATE_SETTINGS.add("vibrate_input_devices");
      PRIVATE_SETTINGS.add("volume_master");
      PRIVATE_SETTINGS.add("master_mono");
      PRIVATE_SETTINGS.add("notifications_use_ring_volume");
      PRIVATE_SETTINGS.add("vibrate_in_silent");
      PRIVATE_SETTINGS.add("media_button_receiver");
      PRIVATE_SETTINGS.add("hide_rotation_lock_toggle_for_accessibility");
      PRIVATE_SETTINGS.add("dtmf_tone_type");
      PRIVATE_SETTINGS.add("hearing_aid");
      PRIVATE_SETTINGS.add("tty_mode");
      PRIVATE_SETTINGS.add("notification_light_pulse");
      PRIVATE_SETTINGS.add("pointer_location");
      PRIVATE_SETTINGS.add("show_touches");
      PRIVATE_SETTINGS.add("window_orientation_listener_log");
      PRIVATE_SETTINGS.add("power_sounds_enabled");
      PRIVATE_SETTINGS.add("dock_sounds_enabled");
      PRIVATE_SETTINGS.add("lockscreen_sounds_enabled");
      PRIVATE_SETTINGS.add("lockscreen.disabled");
      PRIVATE_SETTINGS.add("low_battery_sound");
      PRIVATE_SETTINGS.add("desk_dock_sound");
      PRIVATE_SETTINGS.add("desk_undock_sound");
      PRIVATE_SETTINGS.add("car_dock_sound");
      PRIVATE_SETTINGS.add("car_undock_sound");
      PRIVATE_SETTINGS.add("lock_sound");
      PRIVATE_SETTINGS.add("unlock_sound");
      PRIVATE_SETTINGS.add("sip_receive_calls");
      PRIVATE_SETTINGS.add("sip_call_options");
      PRIVATE_SETTINGS.add("SIP_ALWAYS");
      PRIVATE_SETTINGS.add("SIP_ADDRESS_ONLY");
      PRIVATE_SETTINGS.add("SIP_ASK_ME_EACH_TIME");
      PRIVATE_SETTINGS.add("pointer_speed");
      PRIVATE_SETTINGS.add("lock_to_app_enabled");
      PRIVATE_SETTINGS.add("egg_mode");
      PRIVATE_SETTINGS.add("status_bar_show_battery_percent");
      PRIVATE_SETTINGS.add("display_color_mode");
      VALIDATORS = new ArrayMap();
      VALIDATORS.put("stay_on_while_plugged_in", STAY_ON_WHILE_PLUGGED_IN_VALIDATOR);
      VALIDATORS.put("end_button_behavior", END_BUTTON_BEHAVIOR_VALIDATOR);
      VALIDATORS.put("wifi_use_static_ip", WIFI_USE_STATIC_IP_VALIDATOR);
      VALIDATORS.put("bluetooth_discoverability", BLUETOOTH_DISCOVERABILITY_VALIDATOR);
      VALIDATORS.put("bluetooth_discoverability_timeout", BLUETOOTH_DISCOVERABILITY_TIMEOUT_VALIDATOR);
      VALIDATORS.put("next_alarm_formatted", NEXT_ALARM_FORMATTED_VALIDATOR);
      VALIDATORS.put("font_scale", FONT_SCALE_VALIDATOR);
      VALIDATORS.put("dim_screen", DIM_SCREEN_VALIDATOR);
      VALIDATORS.put("display_color_mode", DISPLAY_COLOR_MODE_VALIDATOR);
      VALIDATORS.put("screen_off_timeout", SCREEN_OFF_TIMEOUT_VALIDATOR);
      VALIDATORS.put("screen_brightness_for_vr", SCREEN_BRIGHTNESS_FOR_VR_VALIDATOR);
      VALIDATORS.put("screen_brightness_mode", SCREEN_BRIGHTNESS_MODE_VALIDATOR);
      VALIDATORS.put("mode_ringer_streams_affected", MODE_RINGER_STREAMS_AFFECTED_VALIDATOR);
      VALIDATORS.put("mute_streams_affected", MUTE_STREAMS_AFFECTED_VALIDATOR);
      VALIDATORS.put("vibrate_on", VIBRATE_ON_VALIDATOR);
      VALIDATORS.put("notification_vibration_intensity", VIBRATION_INTENSITY_VALIDATOR);
      VALIDATORS.put("ringtone_vibration_intensity", VIBRATION_INTENSITY_VALIDATOR);
      VALIDATORS.put("haptic_feedback_intensity", VIBRATION_INTENSITY_VALIDATOR);
      VALIDATORS.put("ringtone", RINGTONE_VALIDATOR);
      VALIDATORS.put("notification_sound", NOTIFICATION_SOUND_VALIDATOR);
      VALIDATORS.put("alarm_alert", ALARM_ALERT_VALIDATOR);
      VALIDATORS.put("auto_replace", TEXT_AUTO_REPLACE_VALIDATOR);
      VALIDATORS.put("auto_caps", TEXT_AUTO_CAPS_VALIDATOR);
      VALIDATORS.put("auto_punctuate", TEXT_AUTO_PUNCTUATE_VALIDATOR);
      VALIDATORS.put("show_password", TEXT_SHOW_PASSWORD_VALIDATOR);
      VALIDATORS.put("auto_time", AUTO_TIME_VALIDATOR);
      VALIDATORS.put("auto_time_zone", AUTO_TIME_ZONE_VALIDATOR);
      VALIDATORS.put("SHOW_GTALK_SERVICE_STATUS", SHOW_GTALK_SERVICE_STATUS_VALIDATOR);
      VALIDATORS.put("wallpaper_activity", WALLPAPER_ACTIVITY_VALIDATOR);
      VALIDATORS.put("time_12_24", TIME_12_24_VALIDATOR);
      VALIDATORS.put("date_format", DATE_FORMAT_VALIDATOR);
      VALIDATORS.put("setup_wizard_has_run", SETUP_WIZARD_HAS_RUN_VALIDATOR);
      VALIDATORS.put("accelerometer_rotation", ACCELEROMETER_ROTATION_VALIDATOR);
      VALIDATORS.put("user_rotation", USER_ROTATION_VALIDATOR);
      VALIDATORS.put("dtmf_tone", DTMF_TONE_WHEN_DIALING_VALIDATOR);
      VALIDATORS.put("all_apps_enabled", SWITCH_ALL_APPS_ENABLED_VALIDATOR);
      VALIDATORS.put("sound_effects_enabled", SOUND_EFFECTS_ENABLED_VALIDATOR);
      VALIDATORS.put("haptic_feedback_enabled", HAPTIC_FEEDBACK_ENABLED_VALIDATOR);
      VALIDATORS.put("power_sounds_enabled", POWER_SOUNDS_ENABLED_VALIDATOR);
      VALIDATORS.put("dock_sounds_enabled", DOCK_SOUNDS_ENABLED_VALIDATOR);
      VALIDATORS.put("show_web_suggestions", SHOW_WEB_SUGGESTIONS_VALIDATOR);
      VALIDATORS.put("wifi_use_static_ip", WIFI_USE_STATIC_IP_VALIDATOR);
      VALIDATORS.put("end_button_behavior", END_BUTTON_BEHAVIOR_VALIDATOR);
      VALIDATORS.put("advanced_settings", ADVANCED_SETTINGS_VALIDATOR);
      VALIDATORS.put("screen_auto_brightness_adj", SCREEN_AUTO_BRIGHTNESS_ADJ_VALIDATOR);
      VALIDATORS.put("vibrate_input_devices", VIBRATE_INPUT_DEVICES_VALIDATOR);
      VALIDATORS.put("master_mono", MASTER_MONO_VALIDATOR);
      VALIDATORS.put("notifications_use_ring_volume", NOTIFICATIONS_USE_RING_VOLUME_VALIDATOR);
      VALIDATORS.put("vibrate_in_silent", VIBRATE_IN_SILENT_VALIDATOR);
      VALIDATORS.put("media_button_receiver", MEDIA_BUTTON_RECEIVER_VALIDATOR);
      VALIDATORS.put("hide_rotation_lock_toggle_for_accessibility", HIDE_ROTATION_LOCK_TOGGLE_FOR_ACCESSIBILITY_VALIDATOR);
      VALIDATORS.put("vibrate_when_ringing", VIBRATE_WHEN_RINGING_VALIDATOR);
      VALIDATORS.put("dtmf_tone_type", DTMF_TONE_TYPE_WHEN_DIALING_VALIDATOR);
      VALIDATORS.put("hearing_aid", HEARING_AID_VALIDATOR);
      VALIDATORS.put("tty_mode", TTY_MODE_VALIDATOR);
      VALIDATORS.put("notification_light_pulse", NOTIFICATION_LIGHT_PULSE_VALIDATOR);
      VALIDATORS.put("pointer_location", POINTER_LOCATION_VALIDATOR);
      VALIDATORS.put("show_touches", SHOW_TOUCHES_VALIDATOR);
      VALIDATORS.put("window_orientation_listener_log", WINDOW_ORIENTATION_LISTENER_LOG_VALIDATOR);
      VALIDATORS.put("lockscreen_sounds_enabled", LOCKSCREEN_SOUNDS_ENABLED_VALIDATOR);
      VALIDATORS.put("lockscreen.disabled", LOCKSCREEN_DISABLED_VALIDATOR);
      VALIDATORS.put("sip_receive_calls", SIP_RECEIVE_CALLS_VALIDATOR);
      VALIDATORS.put("sip_call_options", SIP_CALL_OPTIONS_VALIDATOR);
      VALIDATORS.put("SIP_ALWAYS", SIP_ALWAYS_VALIDATOR);
      VALIDATORS.put("SIP_ADDRESS_ONLY", SIP_ADDRESS_ONLY_VALIDATOR);
      VALIDATORS.put("SIP_ASK_ME_EACH_TIME", SIP_ASK_ME_EACH_TIME_VALIDATOR);
      VALIDATORS.put("pointer_speed", POINTER_SPEED_VALIDATOR);
      VALIDATORS.put("lock_to_app_enabled", LOCK_TO_APP_ENABLED_VALIDATOR);
      VALIDATORS.put("egg_mode", EGG_MODE_VALIDATOR);
      VALIDATORS.put("wifi_static_ip", WIFI_STATIC_IP_VALIDATOR);
      VALIDATORS.put("wifi_static_gateway", WIFI_STATIC_GATEWAY_VALIDATOR);
      VALIDATORS.put("wifi_static_netmask", WIFI_STATIC_NETMASK_VALIDATOR);
      VALIDATORS.put("wifi_static_dns1", WIFI_STATIC_DNS1_VALIDATOR);
      VALIDATORS.put("wifi_static_dns2", WIFI_STATIC_DNS2_VALIDATOR);
      VALIDATORS.put("status_bar_show_battery_percent", SHOW_BATTERY_PERCENT_VALIDATOR);
      CLONE_TO_MANAGED_PROFILE = new ArraySet();
      CLONE_TO_MANAGED_PROFILE.add("date_format");
      CLONE_TO_MANAGED_PROFILE.add("haptic_feedback_enabled");
      CLONE_TO_MANAGED_PROFILE.add("sound_effects_enabled");
      CLONE_TO_MANAGED_PROFILE.add("show_password");
      CLONE_TO_MANAGED_PROFILE.add("time_12_24");
      CLONE_FROM_PARENT_ON_VALUE = new ArrayMap();
      CLONE_FROM_PARENT_ON_VALUE.put("ringtone", "sync_parent_sounds");
      CLONE_FROM_PARENT_ON_VALUE.put("ringtone_2", "sync_parent_sounds");
      CLONE_FROM_PARENT_ON_VALUE.put("notification_sound", "sync_parent_sounds");
      CLONE_FROM_PARENT_ON_VALUE.put("alarm_alert", "sync_parent_sounds");
      INSTANT_APP_SETTINGS = new ArraySet();
      INSTANT_APP_SETTINGS.add("auto_replace");
      INSTANT_APP_SETTINGS.add("auto_caps");
      INSTANT_APP_SETTINGS.add("auto_punctuate");
      INSTANT_APP_SETTINGS.add("show_password");
      INSTANT_APP_SETTINGS.add("date_format");
      INSTANT_APP_SETTINGS.add("font_scale");
      INSTANT_APP_SETTINGS.add("haptic_feedback_enabled");
      INSTANT_APP_SETTINGS.add("time_12_24");
      INSTANT_APP_SETTINGS.add("sound_effects_enabled");
      INSTANT_APP_SETTINGS.add("accelerometer_rotation");
    }
    
    public System() {}
    
    public static void adjustConfigurationForUser(ContentResolver paramContentResolver, Configuration paramConfiguration, int paramInt, boolean paramBoolean)
    {
      fontScale = getFloatForUser(paramContentResolver, "font_scale", 1.0F, paramInt);
      if (fontScale < 0.0F) {
        fontScale = 1.0F;
      }
      String str = getStringForUser(paramContentResolver, "system_locales", paramInt);
      if (str != null) {
        paramConfiguration.setLocales(LocaleList.forLanguageTags(str));
      } else if (paramBoolean) {
        putStringForUser(paramContentResolver, "system_locales", paramConfiguration.getLocales().toLanguageTags(), paramInt);
      }
    }
    
    public static boolean canWrite(Context paramContext)
    {
      return Settings.isCallingPackageAllowedToWriteSettings(paramContext, Process.myUid(), paramContext.getOpPackageName(), false);
    }
    
    public static void clearConfiguration(Configuration paramConfiguration)
    {
      fontScale = 0.0F;
      if ((!userSetLocale) && (!paramConfiguration.getLocales().isEmpty())) {
        paramConfiguration.clearLocales();
      }
    }
    
    public static void clearProviderForTest()
    {
      sProviderHolder.clearProviderForTest();
      sNameValueCache.clearGenerationTrackerForTest();
    }
    
    public static void getCloneFromParentOnValueSettings(Map<String, String> paramMap)
    {
      paramMap.putAll(CLONE_FROM_PARENT_ON_VALUE);
    }
    
    public static void getCloneToManagedProfileSettings(Set<String> paramSet)
    {
      paramSet.addAll(CLONE_TO_MANAGED_PROFILE);
    }
    
    public static void getConfiguration(ContentResolver paramContentResolver, Configuration paramConfiguration)
    {
      adjustConfigurationForUser(paramContentResolver, paramConfiguration, paramContentResolver.getUserId(), false);
    }
    
    public static float getFloat(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      return getFloatForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static float getFloat(ContentResolver paramContentResolver, String paramString, float paramFloat)
    {
      return getFloatForUser(paramContentResolver, paramString, paramFloat, paramContentResolver.getUserId());
    }
    
    public static float getFloatForUser(ContentResolver paramContentResolver, String paramString, float paramFloat, int paramInt)
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      if (paramContentResolver != null) {
        try
        {
          float f = Float.parseFloat(paramContentResolver);
          paramFloat = f;
        }
        catch (NumberFormatException paramContentResolver)
        {
          return paramFloat;
        }
      }
      return paramFloat;
    }
    
    public static float getFloatForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
      throws Settings.SettingNotFoundException
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      if (paramContentResolver != null) {
        try
        {
          float f = Float.parseFloat(paramContentResolver);
          return f;
        }
        catch (NumberFormatException paramContentResolver)
        {
          throw new Settings.SettingNotFoundException(paramString);
        }
      }
      throw new Settings.SettingNotFoundException(paramString);
    }
    
    public static int getInt(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      return getIntForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static int getInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      return getIntForUser(paramContentResolver, paramString, paramInt, paramContentResolver.getUserId());
    }
    
    public static int getIntForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
      throws Settings.SettingNotFoundException
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      try
      {
        paramInt = Integer.parseInt(paramContentResolver);
        return paramInt;
      }
      catch (NumberFormatException paramContentResolver)
      {
        throw new Settings.SettingNotFoundException(paramString);
      }
    }
    
    public static int getIntForUser(ContentResolver paramContentResolver, String paramString, int paramInt1, int paramInt2)
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt2);
      if (paramContentResolver != null) {
        try
        {
          paramInt2 = Integer.parseInt(paramContentResolver);
          paramInt1 = paramInt2;
        }
        catch (NumberFormatException paramContentResolver)
        {
          return paramInt1;
        }
      }
      return paramInt1;
    }
    
    public static long getLong(ContentResolver paramContentResolver, String paramString)
      throws Settings.SettingNotFoundException
    {
      return getLongForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static long getLong(ContentResolver paramContentResolver, String paramString, long paramLong)
    {
      return getLongForUser(paramContentResolver, paramString, paramLong, paramContentResolver.getUserId());
    }
    
    public static long getLongForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
      throws Settings.SettingNotFoundException
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      try
      {
        long l = Long.parseLong(paramContentResolver);
        return l;
      }
      catch (NumberFormatException paramContentResolver)
      {
        throw new Settings.SettingNotFoundException(paramString);
      }
    }
    
    public static long getLongForUser(ContentResolver paramContentResolver, String paramString, long paramLong, int paramInt)
    {
      paramContentResolver = getStringForUser(paramContentResolver, paramString, paramInt);
      if (paramContentResolver != null) {
        try
        {
          long l = Long.parseLong(paramContentResolver);
          paramLong = l;
        }
        catch (NumberFormatException paramContentResolver) {}
      }
      return paramLong;
    }
    
    public static void getMovedToGlobalSettings(Set<String> paramSet)
    {
      paramSet.addAll(MOVED_TO_GLOBAL);
      paramSet.addAll(MOVED_TO_SECURE_THEN_GLOBAL);
    }
    
    public static void getMovedToSecureSettings(Set<String> paramSet)
    {
      paramSet.addAll(MOVED_TO_SECURE);
    }
    
    public static void getNonLegacyMovedKeys(HashSet<String> paramHashSet)
    {
      paramHashSet.addAll(MOVED_TO_GLOBAL);
    }
    
    @Deprecated
    public static boolean getShowGTalkServiceStatus(ContentResolver paramContentResolver)
    {
      return getShowGTalkServiceStatusForUser(paramContentResolver, paramContentResolver.getUserId());
    }
    
    @Deprecated
    public static boolean getShowGTalkServiceStatusForUser(ContentResolver paramContentResolver, int paramInt)
    {
      boolean bool = false;
      if (getIntForUser(paramContentResolver, "SHOW_GTALK_SERVICE_STATUS", 0, paramInt) != 0) {
        bool = true;
      }
      return bool;
    }
    
    public static String getString(ContentResolver paramContentResolver, String paramString)
    {
      return getStringForUser(paramContentResolver, paramString, paramContentResolver.getUserId());
    }
    
    public static String getStringForUser(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      SeempLog.record(SeempLog.getSeempGetApiIdFromValue(paramString));
      if (MOVED_TO_SECURE.contains(paramString))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Setting ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" has moved from android.provider.Settings.System to android.provider.Settings.Secure, returning read-only value.");
        Log.w("Settings", localStringBuilder.toString());
        return Settings.Secure.getStringForUser(paramContentResolver, paramString, paramInt);
      }
      if ((!MOVED_TO_GLOBAL.contains(paramString)) && (!MOVED_TO_SECURE_THEN_GLOBAL.contains(paramString))) {
        return sNameValueCache.getStringForUser(paramContentResolver, paramString, paramInt);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Setting ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" has moved from android.provider.Settings.System to android.provider.Settings.Global, returning read-only value.");
      Log.w("Settings", localStringBuilder.toString());
      return Settings.Global.getStringForUser(paramContentResolver, paramString, paramInt);
    }
    
    public static Uri getUriFor(String paramString)
    {
      if (MOVED_TO_SECURE.contains(paramString))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Setting ");
        localStringBuilder.append(paramString);
        localStringBuilder.append(" has moved from android.provider.Settings.System to android.provider.Settings.Secure, returning Secure URI.");
        Log.w("Settings", localStringBuilder.toString());
        return Settings.Secure.getUriFor(Settings.Secure.CONTENT_URI, paramString);
      }
      if ((!MOVED_TO_GLOBAL.contains(paramString)) && (!MOVED_TO_SECURE_THEN_GLOBAL.contains(paramString))) {
        return getUriFor(CONTENT_URI, paramString);
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Setting ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" has moved from android.provider.Settings.System to android.provider.Settings.Global, returning read-only global URI.");
      Log.w("Settings", localStringBuilder.toString());
      return Settings.Global.getUriFor(Settings.Global.CONTENT_URI, paramString);
    }
    
    public static boolean hasInterestingConfigurationChanges(int paramInt)
    {
      boolean bool;
      if (((0x40000000 & paramInt) == 0) && ((paramInt & 0x4) == 0)) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    
    public static boolean putConfiguration(ContentResolver paramContentResolver, Configuration paramConfiguration)
    {
      return putConfigurationForUser(paramContentResolver, paramConfiguration, paramContentResolver.getUserId());
    }
    
    public static boolean putConfigurationForUser(ContentResolver paramContentResolver, Configuration paramConfiguration, int paramInt)
    {
      boolean bool;
      if ((putFloatForUser(paramContentResolver, "font_scale", fontScale, paramInt)) && (putStringForUser(paramContentResolver, "system_locales", paramConfiguration.getLocales().toLanguageTags(), paramInt))) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public static boolean putFloat(ContentResolver paramContentResolver, String paramString, float paramFloat)
    {
      return putFloatForUser(paramContentResolver, paramString, paramFloat, paramContentResolver.getUserId());
    }
    
    public static boolean putFloatForUser(ContentResolver paramContentResolver, String paramString, float paramFloat, int paramInt)
    {
      return putStringForUser(paramContentResolver, paramString, Float.toString(paramFloat), paramInt);
    }
    
    public static boolean putInt(ContentResolver paramContentResolver, String paramString, int paramInt)
    {
      return putIntForUser(paramContentResolver, paramString, paramInt, paramContentResolver.getUserId());
    }
    
    public static boolean putIntForUser(ContentResolver paramContentResolver, String paramString, int paramInt1, int paramInt2)
    {
      return putStringForUser(paramContentResolver, paramString, Integer.toString(paramInt1), paramInt2);
    }
    
    public static boolean putLong(ContentResolver paramContentResolver, String paramString, long paramLong)
    {
      return putLongForUser(paramContentResolver, paramString, paramLong, paramContentResolver.getUserId());
    }
    
    public static boolean putLongForUser(ContentResolver paramContentResolver, String paramString, long paramLong, int paramInt)
    {
      return putStringForUser(paramContentResolver, paramString, Long.toString(paramLong), paramInt);
    }
    
    public static boolean putString(ContentResolver paramContentResolver, String paramString1, String paramString2)
    {
      return putStringForUser(paramContentResolver, paramString1, paramString2, paramContentResolver.getUserId());
    }
    
    public static boolean putStringForUser(ContentResolver paramContentResolver, String paramString1, String paramString2, int paramInt)
    {
      SeempLog.record(SeempLog.getSeempPutApiIdFromValue(paramString1));
      if (MOVED_TO_SECURE.contains(paramString1))
      {
        paramContentResolver = new StringBuilder();
        paramContentResolver.append("Setting ");
        paramContentResolver.append(paramString1);
        paramContentResolver.append(" has moved from android.provider.Settings.System to android.provider.Settings.Secure, value is unchanged.");
        Log.w("Settings", paramContentResolver.toString());
        return false;
      }
      if ((!MOVED_TO_GLOBAL.contains(paramString1)) && (!MOVED_TO_SECURE_THEN_GLOBAL.contains(paramString1))) {
        return sNameValueCache.putStringForUser(paramContentResolver, paramString1, paramString2, null, false, paramInt);
      }
      paramContentResolver = new StringBuilder();
      paramContentResolver.append("Setting ");
      paramContentResolver.append(paramString1);
      paramContentResolver.append(" has moved from android.provider.Settings.System to android.provider.Settings.Global, value is unchanged.");
      Log.w("Settings", paramContentResolver.toString());
      return false;
    }
    
    @Deprecated
    public static void setShowGTalkServiceStatus(ContentResolver paramContentResolver, boolean paramBoolean)
    {
      setShowGTalkServiceStatusForUser(paramContentResolver, paramBoolean, paramContentResolver.getUserId());
    }
    
    @Deprecated
    public static void setShowGTalkServiceStatusForUser(ContentResolver paramContentResolver, boolean paramBoolean, int paramInt)
    {
      putIntForUser(paramContentResolver, "SHOW_GTALK_SERVICE_STATUS", paramBoolean, paramInt);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UserSetupPersonalization {}
}
