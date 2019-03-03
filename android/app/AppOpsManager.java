package android.app;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.RemoteException;
import android.util.ArrayMap;
import com.android.internal.app.IAppOpsActiveCallback;
import com.android.internal.app.IAppOpsActiveCallback.Stub;
import com.android.internal.app.IAppOpsCallback;
import com.android.internal.app.IAppOpsCallback.Stub;
import com.android.internal.app.IAppOpsService;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AppOpsManager
{
  public static final int MODE_ALLOWED = 0;
  public static final int MODE_DEFAULT = 3;
  public static final int MODE_ERRORED = 2;
  public static final int MODE_FOREGROUND = 4;
  public static final int MODE_IGNORED = 1;
  public static final String[] MODE_NAMES = { "allow", "ignore", "deny", "default", "foreground" };
  @SystemApi
  public static final String OPSTR_ACCEPT_HANDOVER = "android:accept_handover";
  @SystemApi
  public static final String OPSTR_ACCESS_NOTIFICATIONS = "android:access_notifications";
  @SystemApi
  public static final String OPSTR_ACTIVATE_VPN = "android:activate_vpn";
  public static final String OPSTR_ADD_VOICEMAIL = "android:add_voicemail";
  public static final String OPSTR_ANSWER_PHONE_CALLS = "android:answer_phone_calls";
  @SystemApi
  public static final String OPSTR_ASSIST_SCREENSHOT = "android:assist_screenshot";
  @SystemApi
  public static final String OPSTR_ASSIST_STRUCTURE = "android:assist_structure";
  @SystemApi
  public static final String OPSTR_AUDIO_ACCESSIBILITY_VOLUME = "android:audio_accessibility_volume";
  @SystemApi
  public static final String OPSTR_AUDIO_ALARM_VOLUME = "android:audio_alarm_volume";
  @SystemApi
  public static final String OPSTR_AUDIO_BLUETOOTH_VOLUME = "android:audio_bluetooth_volume";
  @SystemApi
  public static final String OPSTR_AUDIO_MASTER_VOLUME = "android:audio_master_volume";
  @SystemApi
  public static final String OPSTR_AUDIO_MEDIA_VOLUME = "android:audio_media_volume";
  @SystemApi
  public static final String OPSTR_AUDIO_NOTIFICATION_VOLUME = "android:audio_notification_volume";
  @SystemApi
  public static final String OPSTR_AUDIO_RING_VOLUME = "android:audio_ring_volume";
  @SystemApi
  public static final String OPSTR_AUDIO_VOICE_VOLUME = "android:audio_voice_volume";
  public static final String OPSTR_AUTO_RUN = "asus:auto_run";
  @SystemApi
  public static final String OPSTR_BIND_ACCESSIBILITY_SERVICE = "android:bind_accessibility_service";
  public static final String OPSTR_BLUETOOTH_SCAN = "android:bluetooth_scan";
  public static final String OPSTR_BODY_SENSORS = "android:body_sensors";
  public static final String OPSTR_CALL_PHONE = "android:call_phone";
  public static final String OPSTR_CAMERA = "android:camera";
  @SystemApi
  public static final String OPSTR_CHANGE_WIFI_STATE = "android:change_wifi_state";
  public static final String OPSTR_COARSE_LOCATION = "android:coarse_location";
  public static final String OPSTR_FINE_LOCATION = "android:fine_location";
  @SystemApi
  public static final String OPSTR_GET_ACCOUNTS = "android:get_accounts";
  public static final String OPSTR_GET_USAGE_STATS = "android:get_usage_stats";
  @SystemApi
  public static final String OPSTR_GPS = "android:gps";
  @SystemApi
  public static final String OPSTR_INSTANT_APP_START_FOREGROUND = "android:instant_app_start_foreground";
  @SystemApi
  public static final String OPSTR_MANAGE_IPSEC_TUNNELS = "android:manage_ipsec_tunnels";
  public static final String OPSTR_MOCK_LOCATION = "android:mock_location";
  public static final String OPSTR_MONITOR_HIGH_POWER_LOCATION = "android:monitor_location_high_power";
  public static final String OPSTR_MONITOR_LOCATION = "android:monitor_location";
  @SystemApi
  public static final String OPSTR_MUTE_MICROPHONE = "android:mute_microphone";
  @SystemApi
  public static final String OPSTR_NEIGHBORING_CELLS = "android:neighboring_cells";
  public static final String OPSTR_PICTURE_IN_PICTURE = "android:picture_in_picture";
  @SystemApi
  public static final String OPSTR_PLAY_AUDIO = "android:play_audio";
  @SystemApi
  public static final String OPSTR_POST_NOTIFICATION = "android:post_notification";
  public static final String OPSTR_PROCESS_OUTGOING_CALLS = "android:process_outgoing_calls";
  @SystemApi
  public static final String OPSTR_PROJECT_MEDIA = "android:project_media";
  public static final String OPSTR_READ_CALENDAR = "android:read_calendar";
  public static final String OPSTR_READ_CALL_LOG = "android:read_call_log";
  public static final String OPSTR_READ_CELL_BROADCASTS = "android:read_cell_broadcasts";
  @SystemApi
  public static final String OPSTR_READ_CLIPBOARD = "android:read_clipboard";
  public static final String OPSTR_READ_CONTACTS = "android:read_contacts";
  public static final String OPSTR_READ_EXTERNAL_STORAGE = "android:read_external_storage";
  @SystemApi
  public static final String OPSTR_READ_ICC_SMS = "android:read_icc_sms";
  public static final String OPSTR_READ_PHONE_NUMBERS = "android:read_phone_numbers";
  public static final String OPSTR_READ_PHONE_STATE = "android:read_phone_state";
  public static final String OPSTR_READ_SMS = "android:read_sms";
  @SystemApi
  public static final String OPSTR_RECEIVE_EMERGENCY_BROADCAST = "android:receive_emergency_broadcast";
  public static final String OPSTR_RECEIVE_MMS = "android:receive_mms";
  public static final String OPSTR_RECEIVE_SMS = "android:receive_sms";
  public static final String OPSTR_RECEIVE_WAP_PUSH = "android:receive_wap_push";
  public static final String OPSTR_RECORD_AUDIO = "android:record_audio";
  public static final String OPSTR_RELATIONAL_START = "asus:relational_start";
  @SystemApi
  public static final String OPSTR_REQUEST_DELETE_PACKAGES = "android:request_delete_packages";
  @SystemApi
  public static final String OPSTR_REQUEST_INSTALL_PACKAGES = "android:request_install_packages";
  @SystemApi
  public static final String OPSTR_RUN_ANY_IN_BACKGROUND = "android:run_any_in_background";
  @SystemApi
  public static final String OPSTR_RUN_IN_BACKGROUND = "android:run_in_background";
  public static final String OPSTR_SEND_SMS = "android:send_sms";
  public static final String OPSTR_START_ACTIVITY = "android:start_activity";
  @SystemApi
  public static final String OPSTR_START_FOREGROUND = "android:start_foreground";
  public static final String OPSTR_SYSTEM_ALERT_WINDOW = "android:system_alert_window";
  @SystemApi
  public static final String OPSTR_TAKE_AUDIO_FOCUS = "android:take_audio_focus";
  @SystemApi
  public static final String OPSTR_TAKE_MEDIA_BUTTONS = "android:take_media_buttons";
  @SystemApi
  public static final String OPSTR_TOAST_WINDOW = "android:toast_window";
  @SystemApi
  public static final String OPSTR_TURN_SCREEN_ON = "android:turn_screen_on";
  public static final String OPSTR_USE_FINGERPRINT = "android:use_fingerprint";
  public static final String OPSTR_USE_SIP = "android:use_sip";
  @SystemApi
  public static final String OPSTR_VIBRATE = "android:vibrate";
  @SystemApi
  public static final String OPSTR_WAKE_LOCK = "android:wake_lock";
  @SystemApi
  public static final String OPSTR_WIFI_SCAN = "android:wifi_scan";
  public static final String OPSTR_WRITE_CALENDAR = "android:write_calendar";
  public static final String OPSTR_WRITE_CALL_LOG = "android:write_call_log";
  @SystemApi
  public static final String OPSTR_WRITE_CLIPBOARD = "android:write_clipboard";
  public static final String OPSTR_WRITE_CONTACTS = "android:write_contacts";
  public static final String OPSTR_WRITE_EXTERNAL_STORAGE = "android:write_external_storage";
  @SystemApi
  public static final String OPSTR_WRITE_ICC_SMS = "android:write_icc_sms";
  public static final String OPSTR_WRITE_SETTINGS = "android:write_settings";
  @SystemApi
  public static final String OPSTR_WRITE_SMS = "android:write_sms";
  @SystemApi
  public static final String OPSTR_WRITE_WALLPAPER = "android:write_wallpaper";
  public static final int OP_ACCEPT_HANDOVER = 74;
  public static final int OP_ACCESS_NOTIFICATIONS = 25;
  public static final int OP_ACTIVATE_VPN = 47;
  public static final int OP_ADD_VOICEMAIL = 52;
  public static final int OP_ANSWER_PHONE_CALLS = 69;
  public static final int OP_ASSIST_SCREENSHOT = 50;
  public static final int OP_ASSIST_STRUCTURE = 49;
  public static final int OP_AUDIO_ACCESSIBILITY_VOLUME = 64;
  public static final int OP_AUDIO_ALARM_VOLUME = 37;
  public static final int OP_AUDIO_BLUETOOTH_VOLUME = 39;
  public static final int OP_AUDIO_MASTER_VOLUME = 33;
  public static final int OP_AUDIO_MEDIA_VOLUME = 36;
  public static final int OP_AUDIO_NOTIFICATION_VOLUME = 38;
  public static final int OP_AUDIO_RING_VOLUME = 35;
  public static final int OP_AUDIO_VOICE_VOLUME = 34;
  public static final int OP_AUTO_RUN = 78;
  public static final int OP_BIND_ACCESSIBILITY_SERVICE = 73;
  public static final int OP_BLUETOOTH_SCAN = 77;
  public static final int OP_BODY_SENSORS = 56;
  public static final int OP_CALL_PHONE = 13;
  public static final int OP_CAMERA = 26;
  public static final int OP_CHANGE_WIFI_STATE = 71;
  public static final int OP_COARSE_LOCATION = 0;
  public static final int OP_FINE_LOCATION = 1;
  public static final int OP_GET_ACCOUNTS = 62;
  public static final int OP_GET_USAGE_STATS = 43;
  public static final int OP_GPS = 2;
  public static final int OP_INSTANT_APP_START_FOREGROUND = 68;
  public static final int OP_MANAGE_IPSEC_TUNNELS = 75;
  public static final int OP_MOCK_LOCATION = 58;
  public static final int OP_MONITOR_HIGH_POWER_LOCATION = 42;
  public static final int OP_MONITOR_LOCATION = 41;
  public static final int OP_MUTE_MICROPHONE = 44;
  public static final int OP_NEIGHBORING_CELLS = 12;
  public static final int OP_NONE = -1;
  public static final int OP_PICTURE_IN_PICTURE = 67;
  public static final int OP_PLAY_AUDIO = 28;
  public static final int OP_POST_NOTIFICATION = 11;
  public static final int OP_PROCESS_OUTGOING_CALLS = 54;
  public static final int OP_PROJECT_MEDIA = 46;
  public static final int OP_READ_CALENDAR = 8;
  public static final int OP_READ_CALL_LOG = 6;
  public static final int OP_READ_CELL_BROADCASTS = 57;
  public static final int OP_READ_CLIPBOARD = 29;
  public static final int OP_READ_CONTACTS = 4;
  public static final int OP_READ_EXTERNAL_STORAGE = 59;
  public static final int OP_READ_ICC_SMS = 21;
  public static final int OP_READ_PHONE_NUMBERS = 65;
  public static final int OP_READ_PHONE_STATE = 51;
  public static final int OP_READ_SMS = 14;
  public static final int OP_RECEIVE_EMERGECY_SMS = 17;
  public static final int OP_RECEIVE_MMS = 18;
  public static final int OP_RECEIVE_SMS = 16;
  public static final int OP_RECEIVE_WAP_PUSH = 19;
  public static final int OP_RECORD_AUDIO = 27;
  public static final int OP_RELATIONAL_START = 79;
  public static final int OP_REQUEST_DELETE_PACKAGES = 72;
  public static final int OP_REQUEST_INSTALL_PACKAGES = 66;
  public static final int OP_RUN_ANY_IN_BACKGROUND = 70;
  public static final int OP_RUN_IN_BACKGROUND = 63;
  public static final int OP_SEND_SMS = 20;
  public static final int OP_START_ACTIVITY = 80;
  public static final int OP_START_FOREGROUND = 76;
  public static final int OP_SYSTEM_ALERT_WINDOW = 24;
  public static final int OP_TAKE_AUDIO_FOCUS = 32;
  public static final int OP_TAKE_MEDIA_BUTTONS = 31;
  public static final int OP_TOAST_WINDOW = 45;
  public static final int OP_TURN_SCREEN_ON = 61;
  public static final int OP_USE_FINGERPRINT = 55;
  public static final int OP_USE_SIP = 53;
  public static final int OP_VIBRATE = 3;
  public static final int OP_WAKE_LOCK = 40;
  public static final int OP_WIFI_SCAN = 10;
  public static final int OP_WRITE_CALENDAR = 9;
  public static final int OP_WRITE_CALL_LOG = 7;
  public static final int OP_WRITE_CLIPBOARD = 30;
  public static final int OP_WRITE_CONTACTS = 5;
  public static final int OP_WRITE_EXTERNAL_STORAGE = 60;
  public static final int OP_WRITE_ICC_SMS = 22;
  public static final int OP_WRITE_SETTINGS = 23;
  public static final int OP_WRITE_SMS = 15;
  public static final int OP_WRITE_WALLPAPER = 48;
  private static final int[] RUNTIME_AND_APPOP_PERMISSIONS_OPS = { 4, 5, 62, 8, 9, 20, 16, 14, 19, 18, 57, 59, 60, 0, 1, 51, 65, 13, 6, 7, 52, 53, 54, 69, 74, 27, 26, 56, 25, 24, 23, 66, 76 };
  public static final int UID_STATE_BACKGROUND = 4;
  public static final int UID_STATE_CACHED = 5;
  public static final int UID_STATE_FOREGROUND = 3;
  public static final int UID_STATE_FOREGROUND_SERVICE = 2;
  public static final int UID_STATE_LAST_NON_RESTRICTED = 2;
  public static final int UID_STATE_PERSISTENT = 0;
  public static final int UID_STATE_TOP = 1;
  public static final int WATCH_FOREGROUND_CHANGES = 1;
  public static final int _NUM_OP = 81;
  public static final int _NUM_UID_STATE = 6;
  private static boolean[] sOpAllowSystemRestrictionBypass;
  private static int[] sOpDefaultMode;
  private static boolean[] sOpDisableReset;
  private static String[] sOpNames;
  private static String[] sOpPerms;
  private static String[] sOpRestrictions;
  private static HashMap<String, Integer> sOpStrToOp;
  private static String[] sOpToString;
  private static int[] sOpToSwitch = { 0, 0, 0, 3, 4, 5, 6, 7, 8, 9, 0, 11, 0, 13, 14, 15, 16, 16, 18, 19, 20, 14, 15, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 0, 0, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 0, 78, 79, 80 };
  private static HashMap<String, Integer> sPermToOp;
  static IBinder sToken;
  final ArrayMap<OnOpActiveChangedListener, IAppOpsActiveCallback> mActiveWatchers = new ArrayMap();
  final Context mContext;
  final ArrayMap<OnOpChangedListener, IAppOpsCallback> mModeWatchers = new ArrayMap();
  final IAppOpsService mService;
  
  static
  {
    sOpToString = new String[] { "android:coarse_location", "android:fine_location", "android:gps", "android:vibrate", "android:read_contacts", "android:write_contacts", "android:read_call_log", "android:write_call_log", "android:read_calendar", "android:write_calendar", "android:wifi_scan", "android:post_notification", "android:neighboring_cells", "android:call_phone", "android:read_sms", "android:write_sms", "android:receive_sms", "android:receive_emergency_broadcast", "android:receive_mms", "android:receive_wap_push", "android:send_sms", "android:read_icc_sms", "android:write_icc_sms", "android:write_settings", "android:system_alert_window", "android:access_notifications", "android:camera", "android:record_audio", "android:play_audio", "android:read_clipboard", "android:write_clipboard", "android:take_media_buttons", "android:take_audio_focus", "android:audio_master_volume", "android:audio_voice_volume", "android:audio_ring_volume", "android:audio_media_volume", "android:audio_alarm_volume", "android:audio_notification_volume", "android:audio_bluetooth_volume", "android:wake_lock", "android:monitor_location", "android:monitor_location_high_power", "android:get_usage_stats", "android:mute_microphone", "android:toast_window", "android:project_media", "android:activate_vpn", "android:write_wallpaper", "android:assist_structure", "android:assist_screenshot", "android:read_phone_state", "android:add_voicemail", "android:use_sip", "android:process_outgoing_calls", "android:use_fingerprint", "android:body_sensors", "android:read_cell_broadcasts", "android:mock_location", "android:read_external_storage", "android:write_external_storage", "android:turn_screen_on", "android:get_accounts", "android:run_in_background", "android:audio_accessibility_volume", "android:read_phone_numbers", "android:request_install_packages", "android:picture_in_picture", "android:instant_app_start_foreground", "android:answer_phone_calls", "android:run_any_in_background", "android:change_wifi_state", "android:request_delete_packages", "android:bind_accessibility_service", "android:accept_handover", "android:manage_ipsec_tunnels", "android:start_foreground", "android:bluetooth_scan", "asus:auto_run", "asus:relational_start", "android:start_activity" };
    sOpNames = new String[] { "COARSE_LOCATION", "FINE_LOCATION", "GPS", "VIBRATE", "READ_CONTACTS", "WRITE_CONTACTS", "READ_CALL_LOG", "WRITE_CALL_LOG", "READ_CALENDAR", "WRITE_CALENDAR", "WIFI_SCAN", "POST_NOTIFICATION", "NEIGHBORING_CELLS", "CALL_PHONE", "READ_SMS", "WRITE_SMS", "RECEIVE_SMS", "RECEIVE_EMERGECY_SMS", "RECEIVE_MMS", "RECEIVE_WAP_PUSH", "SEND_SMS", "READ_ICC_SMS", "WRITE_ICC_SMS", "WRITE_SETTINGS", "SYSTEM_ALERT_WINDOW", "ACCESS_NOTIFICATIONS", "CAMERA", "RECORD_AUDIO", "PLAY_AUDIO", "READ_CLIPBOARD", "WRITE_CLIPBOARD", "TAKE_MEDIA_BUTTONS", "TAKE_AUDIO_FOCUS", "AUDIO_MASTER_VOLUME", "AUDIO_VOICE_VOLUME", "AUDIO_RING_VOLUME", "AUDIO_MEDIA_VOLUME", "AUDIO_ALARM_VOLUME", "AUDIO_NOTIFICATION_VOLUME", "AUDIO_BLUETOOTH_VOLUME", "WAKE_LOCK", "MONITOR_LOCATION", "MONITOR_HIGH_POWER_LOCATION", "GET_USAGE_STATS", "MUTE_MICROPHONE", "TOAST_WINDOW", "PROJECT_MEDIA", "ACTIVATE_VPN", "WRITE_WALLPAPER", "ASSIST_STRUCTURE", "ASSIST_SCREENSHOT", "OP_READ_PHONE_STATE", "ADD_VOICEMAIL", "USE_SIP", "PROCESS_OUTGOING_CALLS", "USE_FINGERPRINT", "BODY_SENSORS", "READ_CELL_BROADCASTS", "MOCK_LOCATION", "READ_EXTERNAL_STORAGE", "WRITE_EXTERNAL_STORAGE", "TURN_ON_SCREEN", "GET_ACCOUNTS", "RUN_IN_BACKGROUND", "AUDIO_ACCESSIBILITY_VOLUME", "READ_PHONE_NUMBERS", "REQUEST_INSTALL_PACKAGES", "PICTURE_IN_PICTURE", "INSTANT_APP_START_FOREGROUND", "ANSWER_PHONE_CALLS", "RUN_ANY_IN_BACKGROUND", "CHANGE_WIFI_STATE", "REQUEST_DELETE_PACKAGES", "BIND_ACCESSIBILITY_SERVICE", "ACCEPT_HANDOVER", "MANAGE_IPSEC_TUNNELS", "START_FOREGROUND", "BLUETOOTH_SCAN", "AUTO_RUN", "RELATIONAL_START", "START_ACTIVITY" };
    sOpPerms = new String[] { "android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION", null, "android.permission.VIBRATE", "android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "android.permission.READ_CALENDAR", "android.permission.WRITE_CALENDAR", "android.permission.ACCESS_WIFI_STATE", null, null, "android.permission.CALL_PHONE", "android.permission.READ_SMS", null, "android.permission.RECEIVE_SMS", "android.permission.RECEIVE_EMERGENCY_BROADCAST", "android.permission.RECEIVE_MMS", "android.permission.RECEIVE_WAP_PUSH", "android.permission.SEND_SMS", "android.permission.READ_SMS", null, "android.permission.WRITE_SETTINGS", "android.permission.SYSTEM_ALERT_WINDOW", "android.permission.ACCESS_NOTIFICATIONS", "android.permission.CAMERA", "android.permission.RECORD_AUDIO", null, null, null, null, null, null, null, null, null, null, null, null, "android.permission.WAKE_LOCK", null, null, "android.permission.PACKAGE_USAGE_STATS", null, null, null, null, null, null, null, "android.permission.READ_PHONE_STATE", "com.android.voicemail.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS", "android.permission.USE_FINGERPRINT", "android.permission.BODY_SENSORS", "android.permission.READ_CELL_BROADCASTS", null, "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", null, "android.permission.GET_ACCOUNTS", null, null, "android.permission.READ_PHONE_NUMBERS", "android.permission.REQUEST_INSTALL_PACKAGES", null, "android.permission.INSTANT_APP_FOREGROUND_SERVICE", "android.permission.ANSWER_PHONE_CALLS", null, "android.permission.CHANGE_WIFI_STATE", "android.permission.REQUEST_DELETE_PACKAGES", "android.permission.BIND_ACCESSIBILITY_SERVICE", "android.permission.ACCEPT_HANDOVER", null, "android.permission.FOREGROUND_SERVICE", null, null, null, null };
    sOpRestrictions = new String[] { "no_share_location", "no_share_location", "no_share_location", null, null, null, "no_outgoing_calls", "no_outgoing_calls", null, null, "no_share_location", null, null, null, "no_sms", "no_sms", "no_sms", null, "no_sms", null, "no_sms", "no_sms", "no_sms", null, "no_create_windows", null, "no_camera", "no_record_audio", null, null, null, null, null, "no_adjust_volume", "no_adjust_volume", "no_adjust_volume", "no_adjust_volume", "no_adjust_volume", "no_adjust_volume", "no_adjust_volume", null, "no_share_location", "no_share_location", null, "no_unmute_microphone", "no_create_windows", null, null, "no_wallpaper", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "no_adjust_volume", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null };
    sOpAllowSystemRestrictionBypass = new boolean[] { 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0 };
    sOpDefaultMode = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 0, 3, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 1 };
    sOpDisableReset = new boolean[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    sOpStrToOp = new HashMap();
    sPermToOp = new HashMap();
    if (sOpToSwitch.length == 81)
    {
      if (sOpToString.length == 81)
      {
        if (sOpNames.length == 81)
        {
          if (sOpPerms.length == 81)
          {
            if (sOpDefaultMode.length == 81)
            {
              if (sOpDisableReset.length == 81)
              {
                if (sOpRestrictions.length == 81)
                {
                  if (sOpAllowSystemRestrictionBypass.length == 81)
                  {
                    int i = 0;
                    for (int j = 0; j < 81; j++) {
                      if (sOpToString[j] != null) {
                        sOpStrToOp.put(sOpToString[j], Integer.valueOf(j));
                      }
                    }
                    localObject = RUNTIME_AND_APPOP_PERMISSIONS_OPS;
                    int k = localObject.length;
                    for (j = i; j < k; j++)
                    {
                      i = localObject[j];
                      if (sOpPerms[i] != null) {
                        sPermToOp.put(sOpPerms[i], Integer.valueOf(i));
                      }
                    }
                    return;
                  }
                  localObject = new StringBuilder();
                  ((StringBuilder)localObject).append("sOpAllowSYstemRestrictionsBypass length ");
                  ((StringBuilder)localObject).append(sOpRestrictions.length);
                  ((StringBuilder)localObject).append(" should be ");
                  ((StringBuilder)localObject).append(81);
                  throw new IllegalStateException(((StringBuilder)localObject).toString());
                }
                localObject = new StringBuilder();
                ((StringBuilder)localObject).append("sOpRestrictions length ");
                ((StringBuilder)localObject).append(sOpRestrictions.length);
                ((StringBuilder)localObject).append(" should be ");
                ((StringBuilder)localObject).append(81);
                throw new IllegalStateException(((StringBuilder)localObject).toString());
              }
              localObject = new StringBuilder();
              ((StringBuilder)localObject).append("sOpDisableReset length ");
              ((StringBuilder)localObject).append(sOpDisableReset.length);
              ((StringBuilder)localObject).append(" should be ");
              ((StringBuilder)localObject).append(81);
              throw new IllegalStateException(((StringBuilder)localObject).toString());
            }
            localObject = new StringBuilder();
            ((StringBuilder)localObject).append("sOpDefaultMode length ");
            ((StringBuilder)localObject).append(sOpDefaultMode.length);
            ((StringBuilder)localObject).append(" should be ");
            ((StringBuilder)localObject).append(81);
            throw new IllegalStateException(((StringBuilder)localObject).toString());
          }
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("sOpPerms length ");
          ((StringBuilder)localObject).append(sOpPerms.length);
          ((StringBuilder)localObject).append(" should be ");
          ((StringBuilder)localObject).append(81);
          throw new IllegalStateException(((StringBuilder)localObject).toString());
        }
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("sOpNames length ");
        ((StringBuilder)localObject).append(sOpNames.length);
        ((StringBuilder)localObject).append(" should be ");
        ((StringBuilder)localObject).append(81);
        throw new IllegalStateException(((StringBuilder)localObject).toString());
      }
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("sOpToString length ");
      ((StringBuilder)localObject).append(sOpToString.length);
      ((StringBuilder)localObject).append(" should be ");
      ((StringBuilder)localObject).append(81);
      throw new IllegalStateException(((StringBuilder)localObject).toString());
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("sOpToSwitch length ");
    ((StringBuilder)localObject).append(sOpToSwitch.length);
    ((StringBuilder)localObject).append(" should be ");
    ((StringBuilder)localObject).append(81);
    throw new IllegalStateException(((StringBuilder)localObject).toString());
  }
  
  AppOpsManager(Context paramContext, IAppOpsService paramIAppOpsService)
  {
    mContext = paramContext;
    mService = paramIAppOpsService;
  }
  
  private String buildSecurityExceptionMsg(int paramInt1, int paramInt2, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append(" from uid ");
    localStringBuilder.append(paramInt2);
    localStringBuilder.append(" not allowed to perform ");
    localStringBuilder.append(sOpNames[paramInt1]);
    return localStringBuilder.toString();
  }
  
  @SystemApi
  public static String[] getOpStrs()
  {
    return (String[])Arrays.copyOf(sOpToString, 78);
  }
  
  /* Error */
  public static IBinder getToken(IAppOpsService paramIAppOpsService)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 873	android/app/AppOpsManager:sToken	Landroid/os/IBinder;
    //   6: ifnull +12 -> 18
    //   9: getstatic 873	android/app/AppOpsManager:sToken	Landroid/os/IBinder;
    //   12: astore_0
    //   13: ldc 2
    //   15: monitorexit
    //   16: aload_0
    //   17: areturn
    //   18: new 875	android/os/Binder
    //   21: astore_1
    //   22: aload_1
    //   23: invokespecial 876	android/os/Binder:<init>	()V
    //   26: aload_0
    //   27: aload_1
    //   28: invokeinterface 881 2 0
    //   33: putstatic 873	android/app/AppOpsManager:sToken	Landroid/os/IBinder;
    //   36: getstatic 873	android/app/AppOpsManager:sToken	Landroid/os/IBinder;
    //   39: astore_0
    //   40: ldc 2
    //   42: monitorexit
    //   43: aload_0
    //   44: areturn
    //   45: astore_0
    //   46: aload_0
    //   47: invokevirtual 885	android/os/RemoteException:rethrowFromSystemServer	()Ljava/lang/RuntimeException;
    //   50: athrow
    //   51: astore_0
    //   52: ldc 2
    //   54: monitorexit
    //   55: aload_0
    //   56: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	57	0	paramIAppOpsService	IAppOpsService
    //   21	7	1	localBinder	Binder
    // Exception table:
    //   from	to	target	type
    //   18	36	45	android/os/RemoteException
    //   3	16	51	finally
    //   18	36	51	finally
    //   36	43	51	finally
    //   46	51	51	finally
    //   52	55	51	finally
  }
  
  public static long maxTime(long[] paramArrayOfLong, int paramInt1, int paramInt2)
  {
    long l2;
    for (long l1 = 0L; paramInt1 < paramInt2; l1 = l2)
    {
      l2 = l1;
      if (paramArrayOfLong[paramInt1] > l1) {
        l2 = paramArrayOfLong[paramInt1];
      }
      paramInt1++;
    }
    return l1;
  }
  
  public static String modeToName(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt < MODE_NAMES.length)) {
      return MODE_NAMES[paramInt];
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("mode=");
    localStringBuilder.append(paramInt);
    return localStringBuilder.toString();
  }
  
  public static boolean opAllowSystemBypassRestriction(int paramInt)
  {
    return sOpAllowSystemRestrictionBypass[paramInt];
  }
  
  public static boolean opAllowsReset(int paramInt)
  {
    return sOpDisableReset[paramInt] ^ 0x1;
  }
  
  public static int opToDefaultMode(int paramInt)
  {
    return sOpDefaultMode[paramInt];
  }
  
  public static String opToName(int paramInt)
  {
    if (paramInt == -1) {
      return "NONE";
    }
    Object localObject;
    if (paramInt < sOpNames.length)
    {
      localObject = sOpNames[paramInt];
    }
    else
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Unknown(");
      ((StringBuilder)localObject).append(paramInt);
      ((StringBuilder)localObject).append(")");
      localObject = ((StringBuilder)localObject).toString();
    }
    return localObject;
  }
  
  public static String opToPermission(int paramInt)
  {
    return sOpPerms[paramInt];
  }
  
  public static String opToRestriction(int paramInt)
  {
    return sOpRestrictions[paramInt];
  }
  
  public static int opToSwitch(int paramInt)
  {
    return sOpToSwitch[paramInt];
  }
  
  public static String permissionToOp(String paramString)
  {
    paramString = (Integer)sPermToOp.get(paramString);
    if (paramString == null) {
      return null;
    }
    return sOpToString[paramString.intValue()];
  }
  
  public static int permissionToOpCode(String paramString)
  {
    paramString = (Integer)sPermToOp.get(paramString);
    int i;
    if (paramString != null) {
      i = paramString.intValue();
    } else {
      i = -1;
    }
    return i;
  }
  
  public static int strDebugOpToOp(String paramString)
  {
    for (int i = 0; i < sOpNames.length; i++) {
      if (sOpNames[i].equals(paramString)) {
        return i;
      }
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Unknown operation string: ");
    localStringBuilder.append(paramString);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  public static int strOpToOp(String paramString)
  {
    Object localObject = (Integer)sOpStrToOp.get(paramString);
    if (localObject != null) {
      return ((Integer)localObject).intValue();
    }
    localObject = new StringBuilder();
    ((StringBuilder)localObject).append("Unknown operation string: ");
    ((StringBuilder)localObject).append(paramString);
    throw new IllegalArgumentException(((StringBuilder)localObject).toString());
  }
  
  public int checkAudioOp(int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    try
    {
      paramInt2 = mService.checkAudioOperation(paramInt1, paramInt2, paramInt3, paramString);
      if (paramInt2 != 2) {
        return paramInt2;
      }
      SecurityException localSecurityException = new java/lang/SecurityException;
      localSecurityException.<init>(buildSecurityExceptionMsg(paramInt1, paramInt3, paramString));
      throw localSecurityException;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int checkAudioOpNoThrow(int paramInt1, int paramInt2, int paramInt3, String paramString)
  {
    try
    {
      paramInt1 = mService.checkAudioOperation(paramInt1, paramInt2, paramInt3, paramString);
      return paramInt1;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int checkOp(int paramInt1, int paramInt2, String paramString)
  {
    try
    {
      int i = mService.checkOperation(paramInt1, paramInt2, paramString);
      if (i != 2) {
        return i;
      }
      SecurityException localSecurityException = new java/lang/SecurityException;
      localSecurityException.<init>(buildSecurityExceptionMsg(paramInt1, paramInt2, paramString));
      throw localSecurityException;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int checkOp(String paramString1, int paramInt, String paramString2)
  {
    return checkOp(strOpToOp(paramString1), paramInt, paramString2);
  }
  
  public int checkOpNoThrow(int paramInt1, int paramInt2, String paramString)
  {
    try
    {
      paramInt1 = mService.checkOperation(paramInt1, paramInt2, paramString);
      if (paramInt1 == 4) {
        paramInt1 = 0;
      }
      return paramInt1;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int checkOpNoThrow(String paramString1, int paramInt, String paramString2)
  {
    return checkOpNoThrow(strOpToOp(paramString1), paramInt, paramString2);
  }
  
  public void checkPackage(int paramInt, String paramString)
  {
    try
    {
      if (mService.checkPackage(paramInt, paramString) == 0) {
        return;
      }
      SecurityException localSecurityException = new java/lang/SecurityException;
      StringBuilder localStringBuilder = new java/lang/StringBuilder;
      localStringBuilder.<init>();
      localStringBuilder.append("Package ");
      localStringBuilder.append(paramString);
      localStringBuilder.append(" does not belong to ");
      localStringBuilder.append(paramInt);
      localSecurityException.<init>(localStringBuilder.toString());
      throw localSecurityException;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void finishOp(int paramInt)
  {
    finishOp(paramInt, Process.myUid(), mContext.getOpPackageName());
  }
  
  public void finishOp(int paramInt1, int paramInt2, String paramString)
  {
    try
    {
      mService.finishOperation(getToken(mService), paramInt1, paramInt2, paramString);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void finishOp(String paramString1, int paramInt, String paramString2)
  {
    finishOp(strOpToOp(paramString1), paramInt, paramString2);
  }
  
  public List<PackageOps> getOpsForPackage(int paramInt, String paramString, int[] paramArrayOfInt)
  {
    try
    {
      paramString = mService.getOpsForPackage(paramInt, paramString, paramArrayOfInt);
      return paramString;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public List<PackageOps> getPackagesForOps(int[] paramArrayOfInt)
  {
    try
    {
      paramArrayOfInt = mService.getPackagesForOps(paramArrayOfInt);
      return paramArrayOfInt;
    }
    catch (RemoteException paramArrayOfInt)
    {
      throw paramArrayOfInt.rethrowFromSystemServer();
    }
  }
  
  public boolean isOperationActive(int paramInt1, int paramInt2, String paramString)
  {
    try
    {
      boolean bool = mService.isOperationActive(paramInt1, paramInt2, paramString);
      return bool;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int noteOp(int paramInt)
  {
    return noteOp(paramInt, Process.myUid(), mContext.getOpPackageName());
  }
  
  public int noteOp(int paramInt1, int paramInt2, String paramString)
  {
    int i = noteOpNoThrow(paramInt1, paramInt2, paramString);
    if (i != 2) {
      return i;
    }
    throw new SecurityException(buildSecurityExceptionMsg(paramInt1, paramInt2, paramString));
  }
  
  public int noteOp(String paramString1, int paramInt, String paramString2)
  {
    return noteOp(strOpToOp(paramString1), paramInt, paramString2);
  }
  
  public int noteOpNoThrow(int paramInt1, int paramInt2, String paramString)
  {
    try
    {
      paramInt1 = mService.noteOperation(paramInt1, paramInt2, paramString);
      return paramInt1;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int noteOpNoThrow(String paramString1, int paramInt, String paramString2)
  {
    return noteOpNoThrow(strOpToOp(paramString1), paramInt, paramString2);
  }
  
  public int noteProxyOp(int paramInt, String paramString)
  {
    int i = noteProxyOpNoThrow(paramInt, paramString);
    if (i != 2) {
      return i;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Proxy package ");
    localStringBuilder.append(mContext.getOpPackageName());
    localStringBuilder.append(" from uid ");
    localStringBuilder.append(Process.myUid());
    localStringBuilder.append(" or calling package ");
    localStringBuilder.append(paramString);
    localStringBuilder.append(" from uid ");
    localStringBuilder.append(Binder.getCallingUid());
    localStringBuilder.append(" not allowed to perform ");
    localStringBuilder.append(sOpNames[paramInt]);
    throw new SecurityException(localStringBuilder.toString());
  }
  
  public int noteProxyOp(String paramString1, String paramString2)
  {
    return noteProxyOp(strOpToOp(paramString1), paramString2);
  }
  
  public int noteProxyOpNoThrow(int paramInt, String paramString)
  {
    try
    {
      paramInt = mService.noteProxyOperation(paramInt, mContext.getOpPackageName(), Binder.getCallingUid(), paramString);
      return paramInt;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int noteProxyOpNoThrow(String paramString1, String paramString2)
  {
    return noteProxyOpNoThrow(strOpToOp(paramString1), paramString2);
  }
  
  public void resetAllModes()
  {
    try
    {
      mService.resetAllModes(mContext.getUserId(), null);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  public void setMode(int paramInt1, int paramInt2, String paramString, int paramInt3)
  {
    try
    {
      mService.setMode(paramInt1, paramInt2, paramString, paramInt3);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setMode(String paramString1, int paramInt1, String paramString2, int paramInt2)
  {
    try
    {
      mService.setMode(strOpToOp(paramString1), paramInt1, paramString2, paramInt2);
      return;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public void setRestriction(int paramInt1, int paramInt2, int paramInt3, String[] paramArrayOfString)
  {
    try
    {
      int i = Binder.getCallingUid();
      mService.setAudioRestriction(paramInt1, paramInt2, i, paramInt3, paramArrayOfString);
      return;
    }
    catch (RemoteException paramArrayOfString)
    {
      throw paramArrayOfString.rethrowFromSystemServer();
    }
  }
  
  public void setUidMode(int paramInt1, int paramInt2, int paramInt3)
  {
    try
    {
      mService.setUidMode(paramInt1, paramInt2, paramInt3);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      throw localRemoteException.rethrowFromSystemServer();
    }
  }
  
  @SystemApi
  public void setUidMode(String paramString, int paramInt1, int paramInt2)
  {
    try
    {
      mService.setUidMode(strOpToOp(paramString), paramInt1, paramInt2);
      return;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public void setUserRestriction(int paramInt, boolean paramBoolean, IBinder paramIBinder)
  {
    setUserRestriction(paramInt, paramBoolean, paramIBinder, null);
  }
  
  public void setUserRestriction(int paramInt, boolean paramBoolean, IBinder paramIBinder, String[] paramArrayOfString)
  {
    setUserRestrictionForUser(paramInt, paramBoolean, paramIBinder, paramArrayOfString, mContext.getUserId());
  }
  
  public void setUserRestrictionForUser(int paramInt1, boolean paramBoolean, IBinder paramIBinder, String[] paramArrayOfString, int paramInt2)
  {
    try
    {
      mService.setUserRestriction(paramInt1, paramBoolean, paramIBinder, paramInt2, paramArrayOfString);
      return;
    }
    catch (RemoteException paramIBinder)
    {
      throw paramIBinder.rethrowFromSystemServer();
    }
  }
  
  public int startOp(int paramInt)
  {
    return startOp(paramInt, Process.myUid(), mContext.getOpPackageName());
  }
  
  public int startOp(int paramInt1, int paramInt2, String paramString)
  {
    return startOp(paramInt1, paramInt2, paramString, false);
  }
  
  public int startOp(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
  {
    int i = startOpNoThrow(paramInt1, paramInt2, paramString, paramBoolean);
    if (i != 2) {
      return i;
    }
    throw new SecurityException(buildSecurityExceptionMsg(paramInt1, paramInt2, paramString));
  }
  
  public int startOp(String paramString1, int paramInt, String paramString2)
  {
    return startOp(strOpToOp(paramString1), paramInt, paramString2);
  }
  
  public int startOpNoThrow(int paramInt1, int paramInt2, String paramString)
  {
    return startOpNoThrow(paramInt1, paramInt2, paramString, false);
  }
  
  public int startOpNoThrow(int paramInt1, int paramInt2, String paramString, boolean paramBoolean)
  {
    try
    {
      paramInt1 = mService.startOperation(getToken(mService), paramInt1, paramInt2, paramString, paramBoolean);
      return paramInt1;
    }
    catch (RemoteException paramString)
    {
      throw paramString.rethrowFromSystemServer();
    }
  }
  
  public int startOpNoThrow(String paramString1, int paramInt, String paramString2)
  {
    return startOpNoThrow(strOpToOp(paramString1), paramInt, paramString2);
  }
  
  public void startWatchingActive(int[] paramArrayOfInt, OnOpActiveChangedListener paramOnOpActiveChangedListener)
  {
    Preconditions.checkNotNull(paramArrayOfInt, "ops cannot be null");
    Preconditions.checkNotNull(paramOnOpActiveChangedListener, "callback cannot be null");
    synchronized (mActiveWatchers)
    {
      if ((IAppOpsActiveCallback)mActiveWatchers.get(paramOnOpActiveChangedListener) != null) {
        return;
      }
      IAppOpsActiveCallback.Stub local2 = new android/app/AppOpsManager$2;
      local2.<init>(this, paramOnOpActiveChangedListener);
      mActiveWatchers.put(paramOnOpActiveChangedListener, local2);
      try
      {
        mService.startWatchingActive(paramArrayOfInt, local2);
        return;
      }
      catch (RemoteException paramArrayOfInt)
      {
        throw paramArrayOfInt.rethrowFromSystemServer();
      }
    }
  }
  
  public void startWatchingMode(int paramInt1, String paramString, int paramInt2, OnOpChangedListener paramOnOpChangedListener)
  {
    synchronized (mModeWatchers)
    {
      IAppOpsCallback localIAppOpsCallback = (IAppOpsCallback)mModeWatchers.get(paramOnOpChangedListener);
      Object localObject = localIAppOpsCallback;
      if (localIAppOpsCallback == null)
      {
        localObject = new android/app/AppOpsManager$1;
        ((1)localObject).<init>(this, paramOnOpChangedListener);
        mModeWatchers.put(paramOnOpChangedListener, localObject);
      }
      try
      {
        mService.startWatchingModeWithFlags(paramInt1, paramString, paramInt2, (IAppOpsCallback)localObject);
        return;
      }
      catch (RemoteException paramString)
      {
        throw paramString.rethrowFromSystemServer();
      }
    }
  }
  
  public void startWatchingMode(int paramInt, String paramString, OnOpChangedListener paramOnOpChangedListener)
  {
    startWatchingMode(paramInt, paramString, 0, paramOnOpChangedListener);
  }
  
  public void startWatchingMode(String paramString1, String paramString2, int paramInt, OnOpChangedListener paramOnOpChangedListener)
  {
    startWatchingMode(strOpToOp(paramString1), paramString2, paramInt, paramOnOpChangedListener);
  }
  
  public void startWatchingMode(String paramString1, String paramString2, OnOpChangedListener paramOnOpChangedListener)
  {
    startWatchingMode(strOpToOp(paramString1), paramString2, paramOnOpChangedListener);
  }
  
  public void stopWatchingActive(OnOpActiveChangedListener paramOnOpActiveChangedListener)
  {
    synchronized (mActiveWatchers)
    {
      paramOnOpActiveChangedListener = (IAppOpsActiveCallback)mActiveWatchers.get(paramOnOpActiveChangedListener);
      if (paramOnOpActiveChangedListener != null) {
        try
        {
          mService.stopWatchingActive(paramOnOpActiveChangedListener);
        }
        catch (RemoteException paramOnOpActiveChangedListener)
        {
          throw paramOnOpActiveChangedListener.rethrowFromSystemServer();
        }
      }
      return;
    }
  }
  
  public void stopWatchingMode(OnOpChangedListener paramOnOpChangedListener)
  {
    synchronized (mModeWatchers)
    {
      paramOnOpChangedListener = (IAppOpsCallback)mModeWatchers.get(paramOnOpChangedListener);
      if (paramOnOpChangedListener != null) {
        try
        {
          mService.stopWatchingMode(paramOnOpChangedListener);
        }
        catch (RemoteException paramOnOpChangedListener)
        {
          throw paramOnOpChangedListener.rethrowFromSystemServer();
        }
      }
      return;
    }
  }
  
  public int unsafeCheckOpRaw(String paramString1, int paramInt, String paramString2)
  {
    try
    {
      paramInt = mService.checkOperation(strOpToOp(paramString1), paramInt, paramString2);
      return paramInt;
    }
    catch (RemoteException paramString1)
    {
      throw paramString1.rethrowFromSystemServer();
    }
  }
  
  public static abstract interface OnOpActiveChangedListener
  {
    public abstract void onOpActiveChanged(int paramInt1, int paramInt2, String paramString, boolean paramBoolean);
  }
  
  public static class OnOpChangedInternalListener
    implements AppOpsManager.OnOpChangedListener
  {
    public OnOpChangedInternalListener() {}
    
    public void onOpChanged(int paramInt, String paramString) {}
    
    public void onOpChanged(String paramString1, String paramString2) {}
  }
  
  public static abstract interface OnOpChangedListener
  {
    public abstract void onOpChanged(String paramString1, String paramString2);
  }
  
  public static class OpEntry
    implements Parcelable
  {
    public static final Parcelable.Creator<OpEntry> CREATOR = new Parcelable.Creator()
    {
      public AppOpsManager.OpEntry createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AppOpsManager.OpEntry(paramAnonymousParcel);
      }
      
      public AppOpsManager.OpEntry[] newArray(int paramAnonymousInt)
      {
        return new AppOpsManager.OpEntry[paramAnonymousInt];
      }
    };
    private final int mDuration;
    private final int mMode;
    private final int mOp;
    private final String mProxyPackageName;
    private final int mProxyUid;
    private final long[] mRejectTimes;
    private final boolean mRunning;
    private final long[] mTimes;
    
    public OpEntry(int paramInt1, int paramInt2, long paramLong1, long paramLong2, int paramInt3, int paramInt4, String paramString)
    {
      mOp = paramInt1;
      mMode = paramInt2;
      mTimes = new long[6];
      mRejectTimes = new long[6];
      long[] arrayOfLong = mTimes;
      boolean bool = false;
      arrayOfLong[0] = paramLong1;
      mRejectTimes[0] = paramLong2;
      mDuration = paramInt3;
      if (paramInt3 == -1) {
        bool = true;
      }
      mRunning = bool;
      mProxyUid = paramInt4;
      mProxyPackageName = paramString;
    }
    
    public OpEntry(int paramInt1, int paramInt2, long[] paramArrayOfLong1, long[] paramArrayOfLong2, int paramInt3, int paramInt4, String paramString)
    {
      this(paramInt1, paramInt2, paramArrayOfLong1, paramArrayOfLong2, paramInt3, bool, paramInt4, paramString);
    }
    
    public OpEntry(int paramInt1, int paramInt2, long[] paramArrayOfLong1, long[] paramArrayOfLong2, int paramInt3, boolean paramBoolean, int paramInt4, String paramString)
    {
      mOp = paramInt1;
      mMode = paramInt2;
      mTimes = new long[6];
      mRejectTimes = new long[6];
      System.arraycopy(paramArrayOfLong1, 0, mTimes, 0, 6);
      System.arraycopy(paramArrayOfLong2, 0, mRejectTimes, 0, 6);
      mDuration = paramInt3;
      mRunning = paramBoolean;
      mProxyUid = paramInt4;
      mProxyPackageName = paramString;
    }
    
    OpEntry(Parcel paramParcel)
    {
      mOp = paramParcel.readInt();
      mMode = paramParcel.readInt();
      mTimes = paramParcel.createLongArray();
      mRejectTimes = paramParcel.createLongArray();
      mDuration = paramParcel.readInt();
      mRunning = paramParcel.readBoolean();
      mProxyUid = paramParcel.readInt();
      mProxyPackageName = paramParcel.readString();
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public int getDuration()
    {
      return mDuration;
    }
    
    public long getLastAccessBackgroundTime()
    {
      return AppOpsManager.maxTime(mTimes, 3, 6);
    }
    
    public long getLastAccessForegroundTime()
    {
      return AppOpsManager.maxTime(mTimes, 0, 3);
    }
    
    public long getLastAccessTime()
    {
      return AppOpsManager.maxTime(mTimes, 0, 6);
    }
    
    public long getLastRejectBackgroundTime()
    {
      return AppOpsManager.maxTime(mRejectTimes, 3, 6);
    }
    
    public long getLastRejectForegroundTime()
    {
      return AppOpsManager.maxTime(mRejectTimes, 0, 3);
    }
    
    public long getLastRejectTime()
    {
      return AppOpsManager.maxTime(mRejectTimes, 0, 6);
    }
    
    public long getLastRejectTimeFor(int paramInt)
    {
      return mRejectTimes[paramInt];
    }
    
    public long getLastTimeFor(int paramInt)
    {
      return mTimes[paramInt];
    }
    
    public int getMode()
    {
      return mMode;
    }
    
    public int getOp()
    {
      return mOp;
    }
    
    public String getProxyPackageName()
    {
      return mProxyPackageName;
    }
    
    public int getProxyUid()
    {
      return mProxyUid;
    }
    
    public long getRejectTime()
    {
      return AppOpsManager.maxTime(mRejectTimes, 0, 6);
    }
    
    public long getTime()
    {
      return AppOpsManager.maxTime(mTimes, 0, 6);
    }
    
    public boolean isRunning()
    {
      return mRunning;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(mOp);
      paramParcel.writeInt(mMode);
      paramParcel.writeLongArray(mTimes);
      paramParcel.writeLongArray(mRejectTimes);
      paramParcel.writeInt(mDuration);
      paramParcel.writeBoolean(mRunning);
      paramParcel.writeInt(mProxyUid);
      paramParcel.writeString(mProxyPackageName);
    }
  }
  
  public static class PackageOps
    implements Parcelable
  {
    public static final Parcelable.Creator<PackageOps> CREATOR = new Parcelable.Creator()
    {
      public AppOpsManager.PackageOps createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AppOpsManager.PackageOps(paramAnonymousParcel);
      }
      
      public AppOpsManager.PackageOps[] newArray(int paramAnonymousInt)
      {
        return new AppOpsManager.PackageOps[paramAnonymousInt];
      }
    };
    private final List<AppOpsManager.OpEntry> mEntries;
    private final String mPackageName;
    private final int mUid;
    
    PackageOps(Parcel paramParcel)
    {
      mPackageName = paramParcel.readString();
      mUid = paramParcel.readInt();
      mEntries = new ArrayList();
      int i = paramParcel.readInt();
      for (int j = 0; j < i; j++) {
        mEntries.add((AppOpsManager.OpEntry)AppOpsManager.OpEntry.CREATOR.createFromParcel(paramParcel));
      }
    }
    
    public PackageOps(String paramString, int paramInt, List<AppOpsManager.OpEntry> paramList)
    {
      mPackageName = paramString;
      mUid = paramInt;
      mEntries = paramList;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public List<AppOpsManager.OpEntry> getOps()
    {
      return mEntries;
    }
    
    public String getPackageName()
    {
      return mPackageName;
    }
    
    public int getUid()
    {
      return mUid;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(mPackageName);
      paramParcel.writeInt(mUid);
      paramParcel.writeInt(mEntries.size());
      for (int i = 0; i < mEntries.size(); i++) {
        ((AppOpsManager.OpEntry)mEntries.get(i)).writeToParcel(paramParcel, paramInt);
      }
    }
  }
}
