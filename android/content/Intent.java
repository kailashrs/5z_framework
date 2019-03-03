package android.content;

import android.annotation.SystemApi;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.ShellCommand;
import android.os.StrictMode;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import com.android.internal.R.styleable;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class Intent
  implements Parcelable, Cloneable
{
  public static final String ACTION_ADVANCED_SETTINGS_CHANGED = "android.intent.action.ADVANCED_SETTINGS";
  public static final String ACTION_AIRPLANE_MODE_CHANGED = "android.intent.action.AIRPLANE_MODE";
  public static final String ACTION_ALARM_CHANGED = "android.intent.action.ALARM_CHANGED";
  public static final String ACTION_ALL_APPS = "android.intent.action.ALL_APPS";
  public static final String ACTION_ANSWER = "android.intent.action.ANSWER";
  public static final String ACTION_APPLICATION_PREFERENCES = "android.intent.action.APPLICATION_PREFERENCES";
  public static final String ACTION_APPLICATION_RESTRICTIONS_CHANGED = "android.intent.action.APPLICATION_RESTRICTIONS_CHANGED";
  public static final String ACTION_APP_ERROR = "android.intent.action.APP_ERROR";
  public static final String ACTION_ASSIST = "android.intent.action.ASSIST";
  public static final String ACTION_ATTACH_DATA = "android.intent.action.ATTACH_DATA";
  public static final String ACTION_BATTERY_CHANGED = "android.intent.action.BATTERY_CHANGED";
  @SystemApi
  public static final String ACTION_BATTERY_LEVEL_CHANGED = "android.intent.action.BATTERY_LEVEL_CHANGED";
  public static final String ACTION_BATTERY_LOW = "android.intent.action.BATTERY_LOW";
  public static final String ACTION_BATTERY_OKAY = "android.intent.action.BATTERY_OKAY";
  public static final String ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
  public static final String ACTION_BUG_REPORT = "android.intent.action.BUG_REPORT";
  public static final String ACTION_CALL = "android.intent.action.CALL";
  public static final String ACTION_CALL_BUTTON = "android.intent.action.CALL_BUTTON";
  @SystemApi
  public static final String ACTION_CALL_EMERGENCY = "android.intent.action.CALL_EMERGENCY";
  @SystemApi
  public static final String ACTION_CALL_PRIVILEGED = "android.intent.action.CALL_PRIVILEGED";
  public static final String ACTION_CAMERA_BUTTON = "android.intent.action.CAMERA_BUTTON";
  public static final String ACTION_CARRIER_SETUP = "android.intent.action.CARRIER_SETUP";
  public static final String ACTION_CHOOSER = "android.intent.action.CHOOSER";
  public static final String ACTION_CLEAR_DNS_CACHE = "android.intent.action.CLEAR_DNS_CACHE";
  public static final String ACTION_CLOSE_SYSTEM_DIALOGS = "android.intent.action.CLOSE_SYSTEM_DIALOGS";
  public static final String ACTION_CONFIGURATION_CHANGED = "android.intent.action.CONFIGURATION_CHANGED";
  public static final String ACTION_CREATE_DOCUMENT = "android.intent.action.CREATE_DOCUMENT";
  public static final String ACTION_CREATE_SHORTCUT = "android.intent.action.CREATE_SHORTCUT";
  public static final String ACTION_DATE_CHANGED = "android.intent.action.DATE_CHANGED";
  public static final String ACTION_DEFAULT = "android.intent.action.VIEW";
  public static final String ACTION_DELETE = "android.intent.action.DELETE";
  @SystemApi
  @Deprecated
  public static final String ACTION_DEVICE_INITIALIZATION_WIZARD = "android.intent.action.DEVICE_INITIALIZATION_WIZARD";
  public static final String ACTION_DEVICE_LOCKED_CHANGED = "android.intent.action.DEVICE_LOCKED_CHANGED";
  @Deprecated
  public static final String ACTION_DEVICE_STORAGE_FULL = "android.intent.action.DEVICE_STORAGE_FULL";
  @Deprecated
  public static final String ACTION_DEVICE_STORAGE_LOW = "android.intent.action.DEVICE_STORAGE_LOW";
  @Deprecated
  public static final String ACTION_DEVICE_STORAGE_NOT_FULL = "android.intent.action.DEVICE_STORAGE_NOT_FULL";
  @Deprecated
  public static final String ACTION_DEVICE_STORAGE_OK = "android.intent.action.DEVICE_STORAGE_OK";
  public static final String ACTION_DIAL = "android.intent.action.DIAL";
  public static final String ACTION_DISMISS_KEYBOARD_SHORTCUTS = "com.android.intent.action.DISMISS_KEYBOARD_SHORTCUTS";
  public static final String ACTION_DOCK_ACTIVE = "android.intent.action.DOCK_ACTIVE";
  public static final String ACTION_DOCK_EVENT = "android.intent.action.DOCK_EVENT";
  public static final String ACTION_DOCK_IDLE = "android.intent.action.DOCK_IDLE";
  public static final String ACTION_DREAMING_STARTED = "android.intent.action.DREAMING_STARTED";
  public static final String ACTION_DREAMING_STOPPED = "android.intent.action.DREAMING_STOPPED";
  public static final String ACTION_DYNAMIC_SENSOR_CHANGED = "android.intent.action.DYNAMIC_SENSOR_CHANGED";
  public static final String ACTION_EDIT = "android.intent.action.EDIT";
  public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
  public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
  @SystemApi
  public static final String ACTION_FACTORY_RESET = "android.intent.action.FACTORY_RESET";
  public static final String ACTION_FACTORY_TEST = "android.intent.action.FACTORY_TEST";
  public static final String ACTION_GET_CONTENT = "android.intent.action.GET_CONTENT";
  public static final String ACTION_GET_RESTRICTION_ENTRIES = "android.intent.action.GET_RESTRICTION_ENTRIES";
  @SystemApi
  public static final String ACTION_GLOBAL_BUTTON = "android.intent.action.GLOBAL_BUTTON";
  public static final String ACTION_GTALK_SERVICE_CONNECTED = "android.intent.action.GTALK_CONNECTED";
  public static final String ACTION_GTALK_SERVICE_DISCONNECTED = "android.intent.action.GTALK_DISCONNECTED";
  public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
  public static final String ACTION_IDLE_MAINTENANCE_END = "android.intent.action.ACTION_IDLE_MAINTENANCE_END";
  public static final String ACTION_IDLE_MAINTENANCE_START = "android.intent.action.ACTION_IDLE_MAINTENANCE_START";
  public static final String ACTION_INPUT_METHOD_CHANGED = "android.intent.action.INPUT_METHOD_CHANGED";
  public static final String ACTION_INSERT = "android.intent.action.INSERT";
  public static final String ACTION_INSERT_OR_EDIT = "android.intent.action.INSERT_OR_EDIT";
  public static final String ACTION_INSTALL_FAILURE = "android.intent.action.INSTALL_FAILURE";
  @SystemApi
  public static final String ACTION_INSTALL_INSTANT_APP_PACKAGE = "android.intent.action.INSTALL_INSTANT_APP_PACKAGE";
  public static final String ACTION_INSTALL_PACKAGE = "android.intent.action.INSTALL_PACKAGE";
  @SystemApi
  public static final String ACTION_INSTANT_APP_RESOLVER_SETTINGS = "android.intent.action.INSTANT_APP_RESOLVER_SETTINGS";
  @SystemApi
  public static final String ACTION_INTENT_FILTER_NEEDS_VERIFICATION = "android.intent.action.INTENT_FILTER_NEEDS_VERIFICATION";
  public static final String ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";
  public static final String ACTION_LOCKED_BOOT_COMPLETED = "android.intent.action.LOCKED_BOOT_COMPLETED";
  public static final String ACTION_MAIN = "android.intent.action.MAIN";
  public static final String ACTION_MANAGED_PROFILE_ADDED = "android.intent.action.MANAGED_PROFILE_ADDED";
  public static final String ACTION_MANAGED_PROFILE_AVAILABLE = "android.intent.action.MANAGED_PROFILE_AVAILABLE";
  public static final String ACTION_MANAGED_PROFILE_REMOVED = "android.intent.action.MANAGED_PROFILE_REMOVED";
  public static final String ACTION_MANAGED_PROFILE_UNAVAILABLE = "android.intent.action.MANAGED_PROFILE_UNAVAILABLE";
  public static final String ACTION_MANAGED_PROFILE_UNLOCKED = "android.intent.action.MANAGED_PROFILE_UNLOCKED";
  @SystemApi
  public static final String ACTION_MANAGE_APP_PERMISSIONS = "android.intent.action.MANAGE_APP_PERMISSIONS";
  public static final String ACTION_MANAGE_NETWORK_USAGE = "android.intent.action.MANAGE_NETWORK_USAGE";
  public static final String ACTION_MANAGE_PACKAGE_STORAGE = "android.intent.action.MANAGE_PACKAGE_STORAGE";
  @SystemApi
  public static final String ACTION_MANAGE_PERMISSIONS = "android.intent.action.MANAGE_PERMISSIONS";
  @SystemApi
  public static final String ACTION_MANAGE_PERMISSION_APPS = "android.intent.action.MANAGE_PERMISSION_APPS";
  @SystemApi
  @Deprecated
  public static final String ACTION_MASTER_CLEAR = "android.intent.action.MASTER_CLEAR";
  @SystemApi
  public static final String ACTION_MASTER_CLEAR_NOTIFICATION = "android.intent.action.MASTER_CLEAR_NOTIFICATION";
  public static final String ACTION_MEDIA_BAD_REMOVAL = "android.intent.action.MEDIA_BAD_REMOVAL";
  public static final String ACTION_MEDIA_BUTTON = "android.intent.action.MEDIA_BUTTON";
  public static final String ACTION_MEDIA_CHECKING = "android.intent.action.MEDIA_CHECKING";
  public static final String ACTION_MEDIA_EJECT = "android.intent.action.MEDIA_EJECT";
  public static final String ACTION_MEDIA_MOUNTED = "android.intent.action.MEDIA_MOUNTED";
  public static final String ACTION_MEDIA_NOFS = "android.intent.action.MEDIA_NOFS";
  public static final String ACTION_MEDIA_REMOVED = "android.intent.action.MEDIA_REMOVED";
  public static final String ACTION_MEDIA_RESOURCE_GRANTED = "android.intent.action.MEDIA_RESOURCE_GRANTED";
  public static final String ACTION_MEDIA_SCANNER_FINISHED = "android.intent.action.MEDIA_SCANNER_FINISHED";
  public static final String ACTION_MEDIA_SCANNER_SCAN_FILE = "android.intent.action.MEDIA_SCANNER_SCAN_FILE";
  public static final String ACTION_MEDIA_SCANNER_STARTED = "android.intent.action.MEDIA_SCANNER_STARTED";
  public static final String ACTION_MEDIA_SHARED = "android.intent.action.MEDIA_SHARED";
  public static final String ACTION_MEDIA_UNMOUNTABLE = "android.intent.action.MEDIA_UNMOUNTABLE";
  public static final String ACTION_MEDIA_UNMOUNTED = "android.intent.action.MEDIA_UNMOUNTED";
  public static final String ACTION_MEDIA_UNSHARED = "android.intent.action.MEDIA_UNSHARED";
  public static final String ACTION_MY_PACKAGE_REPLACED = "android.intent.action.MY_PACKAGE_REPLACED";
  public static final String ACTION_MY_PACKAGE_SUSPENDED = "android.intent.action.MY_PACKAGE_SUSPENDED";
  public static final String ACTION_MY_PACKAGE_UNSUSPENDED = "android.intent.action.MY_PACKAGE_UNSUSPENDED";
  public static final String ACTION_NEW_OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
  public static final String ACTION_OPEN_DOCUMENT = "android.intent.action.OPEN_DOCUMENT";
  public static final String ACTION_OPEN_DOCUMENT_TREE = "android.intent.action.OPEN_DOCUMENT_TREE";
  public static final String ACTION_OVERLAY_CHANGED = "android.intent.action.OVERLAY_CHANGED";
  public static final String ACTION_PACKAGES_SUSPENDED = "android.intent.action.PACKAGES_SUSPENDED";
  public static final String ACTION_PACKAGES_UNSUSPENDED = "android.intent.action.PACKAGES_UNSUSPENDED";
  public static final String ACTION_PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED";
  public static final String ACTION_PACKAGE_CHANGED = "android.intent.action.PACKAGE_CHANGED";
  public static final String ACTION_PACKAGE_DATA_CLEARED = "android.intent.action.PACKAGE_DATA_CLEARED";
  public static final String ACTION_PACKAGE_FIRST_LAUNCH = "android.intent.action.PACKAGE_FIRST_LAUNCH";
  public static final String ACTION_PACKAGE_FULLY_REMOVED = "android.intent.action.PACKAGE_FULLY_REMOVED";
  @Deprecated
  public static final String ACTION_PACKAGE_INSTALL = "android.intent.action.PACKAGE_INSTALL";
  public static final String ACTION_PACKAGE_NEEDS_OPTIONAL_VERIFICATION = "com.qualcomm.qti.intent.action.PACKAGE_NEEDS_OPTIONAL_VERIFICATION";
  public static final String ACTION_PACKAGE_NEEDS_VERIFICATION = "android.intent.action.PACKAGE_NEEDS_VERIFICATION";
  public static final String ACTION_PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED";
  public static final String ACTION_PACKAGE_REPLACED = "android.intent.action.PACKAGE_REPLACED";
  public static final String ACTION_PACKAGE_RESTARTED = "android.intent.action.PACKAGE_RESTARTED";
  public static final String ACTION_PACKAGE_VERIFIED = "android.intent.action.PACKAGE_VERIFIED";
  public static final String ACTION_PASTE = "android.intent.action.PASTE";
  public static final String ACTION_PICK = "android.intent.action.PICK";
  public static final String ACTION_PICK_ACTIVITY = "android.intent.action.PICK_ACTIVITY";
  public static final String ACTION_POWER_CONNECTED = "android.intent.action.ACTION_POWER_CONNECTED";
  public static final String ACTION_POWER_DISCONNECTED = "android.intent.action.ACTION_POWER_DISCONNECTED";
  public static final String ACTION_POWER_USAGE_SUMMARY = "android.intent.action.POWER_USAGE_SUMMARY";
  public static final String ACTION_PREFERRED_ACTIVITY_CHANGED = "android.intent.action.ACTION_PREFERRED_ACTIVITY_CHANGED";
  @SystemApi
  public static final String ACTION_PRE_BOOT_COMPLETED = "android.intent.action.PRE_BOOT_COMPLETED";
  public static final String ACTION_PROCESS_TEXT = "android.intent.action.PROCESS_TEXT";
  public static final String ACTION_PROVIDER_CHANGED = "android.intent.action.PROVIDER_CHANGED";
  @SystemApi
  public static final String ACTION_QUERY_PACKAGE_RESTART = "android.intent.action.QUERY_PACKAGE_RESTART";
  public static final String ACTION_QUICK_CLOCK = "android.intent.action.QUICK_CLOCK";
  public static final String ACTION_QUICK_VIEW = "android.intent.action.QUICK_VIEW";
  public static final String ACTION_REBOOT = "android.intent.action.REBOOT";
  public static final String ACTION_REMOTE_INTENT = "com.google.android.c2dm.intent.RECEIVE";
  public static final String ACTION_REQUEST_SHUTDOWN = "com.android.internal.intent.action.REQUEST_SHUTDOWN";
  @SystemApi
  public static final String ACTION_RESOLVE_INSTANT_APP_PACKAGE = "android.intent.action.RESOLVE_INSTANT_APP_PACKAGE";
  @SystemApi
  public static final String ACTION_REVIEW_PERMISSIONS = "android.intent.action.REVIEW_PERMISSIONS";
  public static final String ACTION_RUN = "android.intent.action.RUN";
  public static final String ACTION_SCREEN_OFF = "android.intent.action.SCREEN_OFF";
  public static final String ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
  public static final String ACTION_SEARCH = "android.intent.action.SEARCH";
  public static final String ACTION_SEARCH_LONG_PRESS = "android.intent.action.SEARCH_LONG_PRESS";
  public static final String ACTION_SEND = "android.intent.action.SEND";
  public static final String ACTION_SENDTO = "android.intent.action.SENDTO";
  public static final String ACTION_SEND_MULTIPLE = "android.intent.action.SEND_MULTIPLE";
  @SystemApi
  @Deprecated
  public static final String ACTION_SERVICE_STATE = "android.intent.action.SERVICE_STATE";
  public static final String ACTION_SETTING_RESTORED = "android.os.action.SETTING_RESTORED";
  public static final String ACTION_SET_WALLPAPER = "android.intent.action.SET_WALLPAPER";
  public static final String ACTION_SHOW_APP_INFO = "android.intent.action.SHOW_APP_INFO";
  public static final String ACTION_SHOW_BRIGHTNESS_DIALOG = "com.android.intent.action.SHOW_BRIGHTNESS_DIALOG";
  public static final String ACTION_SHOW_KEYBOARD_SHORTCUTS = "com.android.intent.action.SHOW_KEYBOARD_SHORTCUTS";
  @SystemApi
  public static final String ACTION_SHOW_SUSPENDED_APP_DETAILS = "android.intent.action.SHOW_SUSPENDED_APP_DETAILS";
  public static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";
  @SystemApi
  @Deprecated
  public static final String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
  @SystemApi
  public static final String ACTION_SPLIT_CONFIGURATION_CHANGED = "android.intent.action.SPLIT_CONFIGURATION_CHANGED";
  public static final String ACTION_SYNC = "android.intent.action.SYNC";
  public static final String ACTION_SYSTEM_TUTORIAL = "android.intent.action.SYSTEM_TUTORIAL";
  public static final String ACTION_THERMAL_EVENT = "android.intent.action.THERMAL_EVENT";
  public static final String ACTION_TIMEZONE_CHANGED = "android.intent.action.TIMEZONE_CHANGED";
  public static final String ACTION_TIME_CHANGED = "android.intent.action.TIME_SET";
  public static final String ACTION_TIME_TICK = "android.intent.action.TIME_TICK";
  public static final String ACTION_UID_REMOVED = "android.intent.action.UID_REMOVED";
  @Deprecated
  public static final String ACTION_UMS_CONNECTED = "android.intent.action.UMS_CONNECTED";
  @Deprecated
  public static final String ACTION_UMS_DISCONNECTED = "android.intent.action.UMS_DISCONNECTED";
  public static final String ACTION_UNINSTALL_PACKAGE = "android.intent.action.UNINSTALL_PACKAGE";
  @SystemApi
  public static final String ACTION_UPGRADE_SETUP = "android.intent.action.UPGRADE_SETUP";
  public static final String ACTION_USER_ADDED = "android.intent.action.USER_ADDED";
  public static final String ACTION_USER_BACKGROUND = "android.intent.action.USER_BACKGROUND";
  public static final String ACTION_USER_FOREGROUND = "android.intent.action.USER_FOREGROUND";
  public static final String ACTION_USER_INFO_CHANGED = "android.intent.action.USER_INFO_CHANGED";
  public static final String ACTION_USER_INITIALIZE = "android.intent.action.USER_INITIALIZE";
  public static final String ACTION_USER_PRESENT = "android.intent.action.USER_PRESENT";
  @SystemApi
  public static final String ACTION_USER_REMOVED = "android.intent.action.USER_REMOVED";
  public static final String ACTION_USER_STARTED = "android.intent.action.USER_STARTED";
  public static final String ACTION_USER_STARTING = "android.intent.action.USER_STARTING";
  public static final String ACTION_USER_STOPPED = "android.intent.action.USER_STOPPED";
  public static final String ACTION_USER_STOPPING = "android.intent.action.USER_STOPPING";
  public static final String ACTION_USER_SWITCHED = "android.intent.action.USER_SWITCHED";
  public static final String ACTION_USER_UNLOCKED = "android.intent.action.USER_UNLOCKED";
  public static final String ACTION_VIEW = "android.intent.action.VIEW";
  @SystemApi
  public static final String ACTION_VOICE_ASSIST = "android.intent.action.VOICE_ASSIST";
  public static final String ACTION_VOICE_COMMAND = "android.intent.action.VOICE_COMMAND";
  @Deprecated
  public static final String ACTION_WALLPAPER_CHANGED = "android.intent.action.WALLPAPER_CHANGED";
  public static final String ACTION_WEB_SEARCH = "android.intent.action.WEB_SEARCH";
  private static final String ATTR_ACTION = "action";
  private static final String ATTR_CATEGORY = "category";
  private static final String ATTR_COMPONENT = "component";
  private static final String ATTR_DATA = "data";
  private static final String ATTR_FLAGS = "flags";
  private static final String ATTR_TYPE = "type";
  public static final String CATEGORY_ALTERNATIVE = "android.intent.category.ALTERNATIVE";
  public static final String CATEGORY_APP_BROWSER = "android.intent.category.APP_BROWSER";
  public static final String CATEGORY_APP_CALCULATOR = "android.intent.category.APP_CALCULATOR";
  public static final String CATEGORY_APP_CALENDAR = "android.intent.category.APP_CALENDAR";
  public static final String CATEGORY_APP_CONTACTS = "android.intent.category.APP_CONTACTS";
  public static final String CATEGORY_APP_EMAIL = "android.intent.category.APP_EMAIL";
  public static final String CATEGORY_APP_GALLERY = "android.intent.category.APP_GALLERY";
  public static final String CATEGORY_APP_MAPS = "android.intent.category.APP_MAPS";
  public static final String CATEGORY_APP_MARKET = "android.intent.category.APP_MARKET";
  public static final String CATEGORY_APP_MESSAGING = "android.intent.category.APP_MESSAGING";
  public static final String CATEGORY_APP_MUSIC = "android.intent.category.APP_MUSIC";
  public static final String CATEGORY_BROWSABLE = "android.intent.category.BROWSABLE";
  public static final String CATEGORY_CAR_DOCK = "android.intent.category.CAR_DOCK";
  public static final String CATEGORY_CAR_LAUNCHER = "android.intent.category.CAR_LAUNCHER";
  public static final String CATEGORY_CAR_MODE = "android.intent.category.CAR_MODE";
  public static final String CATEGORY_DEFAULT = "android.intent.category.DEFAULT";
  public static final String CATEGORY_DESK_DOCK = "android.intent.category.DESK_DOCK";
  public static final String CATEGORY_DEVELOPMENT_PREFERENCE = "android.intent.category.DEVELOPMENT_PREFERENCE";
  public static final String CATEGORY_EMBED = "android.intent.category.EMBED";
  public static final String CATEGORY_FRAMEWORK_INSTRUMENTATION_TEST = "android.intent.category.FRAMEWORK_INSTRUMENTATION_TEST";
  public static final String CATEGORY_HE_DESK_DOCK = "android.intent.category.HE_DESK_DOCK";
  public static final String CATEGORY_HOME = "android.intent.category.HOME";
  public static final String CATEGORY_HOME_MAIN = "android.intent.category.HOME_MAIN";
  public static final String CATEGORY_INFO = "android.intent.category.INFO";
  public static final String CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER";
  public static final String CATEGORY_LAUNCHER_APP = "android.intent.category.LAUNCHER_APP";
  public static final String CATEGORY_LEANBACK_LAUNCHER = "android.intent.category.LEANBACK_LAUNCHER";
  @SystemApi
  public static final String CATEGORY_LEANBACK_SETTINGS = "android.intent.category.LEANBACK_SETTINGS";
  public static final String CATEGORY_LE_DESK_DOCK = "android.intent.category.LE_DESK_DOCK";
  public static final String CATEGORY_MONKEY = "android.intent.category.MONKEY";
  public static final String CATEGORY_OPENABLE = "android.intent.category.OPENABLE";
  public static final String CATEGORY_PREFERENCE = "android.intent.category.PREFERENCE";
  public static final String CATEGORY_SAMPLE_CODE = "android.intent.category.SAMPLE_CODE";
  public static final String CATEGORY_SELECTED_ALTERNATIVE = "android.intent.category.SELECTED_ALTERNATIVE";
  public static final String CATEGORY_SETUP_WIZARD = "android.intent.category.SETUP_WIZARD";
  public static final String CATEGORY_TAB = "android.intent.category.TAB";
  public static final String CATEGORY_TEST = "android.intent.category.TEST";
  public static final String CATEGORY_TYPED_OPENABLE = "android.intent.category.TYPED_OPENABLE";
  public static final String CATEGORY_UNIT_TEST = "android.intent.category.UNIT_TEST";
  public static final String CATEGORY_VOICE = "android.intent.category.VOICE";
  public static final String CATEGORY_VR_HOME = "android.intent.category.VR_HOME";
  private static final int COPY_MODE_ALL = 0;
  private static final int COPY_MODE_FILTER = 1;
  private static final int COPY_MODE_HISTORY = 2;
  public static final Parcelable.Creator<Intent> CREATOR = new Parcelable.Creator()
  {
    public Intent createFromParcel(Parcel paramAnonymousParcel)
    {
      return new Intent(paramAnonymousParcel);
    }
    
    public Intent[] newArray(int paramAnonymousInt)
    {
      return new Intent[paramAnonymousInt];
    }
  };
  public static final String EXTRA_ALARM_COUNT = "android.intent.extra.ALARM_COUNT";
  public static final String EXTRA_ALLOW_MULTIPLE = "android.intent.extra.ALLOW_MULTIPLE";
  @Deprecated
  public static final String EXTRA_ALLOW_REPLACE = "android.intent.extra.ALLOW_REPLACE";
  public static final String EXTRA_ALTERNATE_INTENTS = "android.intent.extra.ALTERNATE_INTENTS";
  public static final String EXTRA_ASSIST_CONTEXT = "android.intent.extra.ASSIST_CONTEXT";
  public static final String EXTRA_ASSIST_INPUT_DEVICE_ID = "android.intent.extra.ASSIST_INPUT_DEVICE_ID";
  public static final String EXTRA_ASSIST_INPUT_HINT_KEYBOARD = "android.intent.extra.ASSIST_INPUT_HINT_KEYBOARD";
  public static final String EXTRA_ASSIST_PACKAGE = "android.intent.extra.ASSIST_PACKAGE";
  public static final String EXTRA_ASSIST_UID = "android.intent.extra.ASSIST_UID";
  public static final String EXTRA_AUTO_LAUNCH_SINGLE_CHOICE = "android.intent.extra.AUTO_LAUNCH_SINGLE_CHOICE";
  public static final String EXTRA_BCC = "android.intent.extra.BCC";
  public static final String EXTRA_BUG_REPORT = "android.intent.extra.BUG_REPORT";
  @SystemApi
  public static final String EXTRA_CALLING_PACKAGE = "android.intent.extra.CALLING_PACKAGE";
  public static final String EXTRA_CC = "android.intent.extra.CC";
  @SystemApi
  @Deprecated
  public static final String EXTRA_CDMA_DEFAULT_ROAMING_INDICATOR = "cdmaDefaultRoamingIndicator";
  @SystemApi
  @Deprecated
  public static final String EXTRA_CDMA_ROAMING_INDICATOR = "cdmaRoamingIndicator";
  @Deprecated
  public static final String EXTRA_CHANGED_COMPONENT_NAME = "android.intent.extra.changed_component_name";
  public static final String EXTRA_CHANGED_COMPONENT_NAME_LIST = "android.intent.extra.changed_component_name_list";
  public static final String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
  public static final String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";
  public static final String EXTRA_CHOOSER_REFINEMENT_INTENT_SENDER = "android.intent.extra.CHOOSER_REFINEMENT_INTENT_SENDER";
  public static final String EXTRA_CHOOSER_TARGETS = "android.intent.extra.CHOOSER_TARGETS";
  public static final String EXTRA_CHOSEN_COMPONENT = "android.intent.extra.CHOSEN_COMPONENT";
  public static final String EXTRA_CHOSEN_COMPONENT_INTENT_SENDER = "android.intent.extra.CHOSEN_COMPONENT_INTENT_SENDER";
  public static final String EXTRA_CLIENT_INTENT = "android.intent.extra.client_intent";
  public static final String EXTRA_CLIENT_LABEL = "android.intent.extra.client_label";
  public static final String EXTRA_COMPONENT_NAME = "android.intent.extra.COMPONENT_NAME";
  public static final String EXTRA_CONTENT_ANNOTATIONS = "android.intent.extra.CONTENT_ANNOTATIONS";
  @SystemApi
  @Deprecated
  public static final String EXTRA_CSS_INDICATOR = "cssIndicator";
  @SystemApi
  @Deprecated
  public static final String EXTRA_DATA_OPERATOR_ALPHA_LONG = "data-operator-alpha-long";
  @SystemApi
  @Deprecated
  public static final String EXTRA_DATA_OPERATOR_ALPHA_SHORT = "data-operator-alpha-short";
  @SystemApi
  @Deprecated
  public static final String EXTRA_DATA_OPERATOR_NUMERIC = "data-operator-numeric";
  @SystemApi
  @Deprecated
  public static final String EXTRA_DATA_RADIO_TECH = "dataRadioTechnology";
  @SystemApi
  @Deprecated
  public static final String EXTRA_DATA_REG_STATE = "dataRegState";
  public static final String EXTRA_DATA_REMOVED = "android.intent.extra.DATA_REMOVED";
  @SystemApi
  @Deprecated
  public static final String EXTRA_DATA_ROAMING_TYPE = "dataRoamingType";
  public static final String EXTRA_DOCK_STATE = "android.intent.extra.DOCK_STATE";
  public static final int EXTRA_DOCK_STATE_CAR = 2;
  public static final int EXTRA_DOCK_STATE_DESK = 1;
  public static final int EXTRA_DOCK_STATE_HE_DESK = 4;
  public static final int EXTRA_DOCK_STATE_KEYBOARD = 10;
  public static final int EXTRA_DOCK_STATE_LE_DESK = 3;
  public static final int EXTRA_DOCK_STATE_UNDOCKED = 0;
  public static final String EXTRA_DONT_KILL_APP = "android.intent.extra.DONT_KILL_APP";
  public static final String EXTRA_EMAIL = "android.intent.extra.EMAIL";
  @SystemApi
  @Deprecated
  public static final String EXTRA_EMERGENCY_ONLY = "emergencyOnly";
  @Deprecated
  public static final String EXTRA_EPHEMERAL_FAILURE = "android.intent.extra.EPHEMERAL_FAILURE";
  @Deprecated
  public static final String EXTRA_EPHEMERAL_HOSTNAME = "android.intent.extra.EPHEMERAL_HOSTNAME";
  @Deprecated
  public static final String EXTRA_EPHEMERAL_SUCCESS = "android.intent.extra.EPHEMERAL_SUCCESS";
  @Deprecated
  public static final String EXTRA_EPHEMERAL_TOKEN = "android.intent.extra.EPHEMERAL_TOKEN";
  public static final String EXTRA_EXCLUDE_COMPONENTS = "android.intent.extra.EXCLUDE_COMPONENTS";
  @SystemApi
  public static final String EXTRA_FORCE_FACTORY_RESET = "android.intent.extra.FORCE_FACTORY_RESET";
  @Deprecated
  public static final String EXTRA_FORCE_MASTER_CLEAR = "android.intent.extra.FORCE_MASTER_CLEAR";
  public static final String EXTRA_FROM_STORAGE = "android.intent.extra.FROM_STORAGE";
  public static final String EXTRA_HTML_TEXT = "android.intent.extra.HTML_TEXT";
  public static final String EXTRA_INDEX = "android.intent.extra.INDEX";
  public static final String EXTRA_INITIAL_INTENTS = "android.intent.extra.INITIAL_INTENTS";
  public static final String EXTRA_INSTALLER_PACKAGE_NAME = "android.intent.extra.INSTALLER_PACKAGE_NAME";
  public static final String EXTRA_INSTALL_RESULT = "android.intent.extra.INSTALL_RESULT";
  @SystemApi
  public static final String EXTRA_INSTANT_APP_ACTION = "android.intent.extra.INSTANT_APP_ACTION";
  @SystemApi
  public static final String EXTRA_INSTANT_APP_BUNDLES = "android.intent.extra.INSTANT_APP_BUNDLES";
  @SystemApi
  public static final String EXTRA_INSTANT_APP_EXTRAS = "android.intent.extra.INSTANT_APP_EXTRAS";
  @SystemApi
  public static final String EXTRA_INSTANT_APP_FAILURE = "android.intent.extra.INSTANT_APP_FAILURE";
  @SystemApi
  public static final String EXTRA_INSTANT_APP_HOSTNAME = "android.intent.extra.INSTANT_APP_HOSTNAME";
  @SystemApi
  public static final String EXTRA_INSTANT_APP_SUCCESS = "android.intent.extra.INSTANT_APP_SUCCESS";
  @SystemApi
  public static final String EXTRA_INSTANT_APP_TOKEN = "android.intent.extra.INSTANT_APP_TOKEN";
  public static final String EXTRA_INTENT = "android.intent.extra.INTENT";
  @SystemApi
  @Deprecated
  public static final String EXTRA_IS_DATA_ROAMING_FROM_REGISTRATION = "isDataRoamingFromRegistration";
  @SystemApi
  @Deprecated
  public static final String EXTRA_IS_USING_CARRIER_AGGREGATION = "isUsingCarrierAggregation";
  public static final String EXTRA_KEY_CONFIRM = "android.intent.extra.KEY_CONFIRM";
  public static final String EXTRA_KEY_EVENT = "android.intent.extra.KEY_EVENT";
  public static final String EXTRA_LAUNCHER_EXTRAS = "android.intent.extra.LAUNCHER_EXTRAS";
  public static final String EXTRA_LOCAL_ONLY = "android.intent.extra.LOCAL_ONLY";
  @SystemApi
  public static final String EXTRA_LONG_VERSION_CODE = "android.intent.extra.LONG_VERSION_CODE";
  @SystemApi
  @Deprecated
  public static final String EXTRA_LTE_EARFCN_RSRP_BOOST = "LteEarfcnRsrpBoost";
  @SystemApi
  @Deprecated
  public static final String EXTRA_MANUAL = "manual";
  public static final String EXTRA_MEDIA_RESOURCE_TYPE = "android.intent.extra.MEDIA_RESOURCE_TYPE";
  public static final int EXTRA_MEDIA_RESOURCE_TYPE_AUDIO_CODEC = 1;
  public static final int EXTRA_MEDIA_RESOURCE_TYPE_VIDEO_CODEC = 0;
  public static final String EXTRA_MIME_TYPES = "android.intent.extra.MIME_TYPES";
  @SystemApi
  @Deprecated
  public static final String EXTRA_NETWORK_ID = "networkId";
  public static final String EXTRA_NOT_UNKNOWN_SOURCE = "android.intent.extra.NOT_UNKNOWN_SOURCE";
  @SystemApi
  @Deprecated
  public static final String EXTRA_OPERATOR_ALPHA_LONG = "operator-alpha-long";
  @SystemApi
  @Deprecated
  public static final String EXTRA_OPERATOR_ALPHA_SHORT = "operator-alpha-short";
  @SystemApi
  @Deprecated
  public static final String EXTRA_OPERATOR_NUMERIC = "operator-numeric";
  @SystemApi
  public static final String EXTRA_ORIGINATING_UID = "android.intent.extra.ORIGINATING_UID";
  public static final String EXTRA_ORIGINATING_URI = "android.intent.extra.ORIGINATING_URI";
  @SystemApi
  public static final String EXTRA_PACKAGES = "android.intent.extra.PACKAGES";
  public static final String EXTRA_PACKAGE_NAME = "android.intent.extra.PACKAGE_NAME";
  @SystemApi
  public static final String EXTRA_PERMISSION_NAME = "android.intent.extra.PERMISSION_NAME";
  public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";
  public static final String EXTRA_PROCESS_TEXT = "android.intent.extra.PROCESS_TEXT";
  public static final String EXTRA_PROCESS_TEXT_READONLY = "android.intent.extra.PROCESS_TEXT_READONLY";
  @Deprecated
  public static final String EXTRA_QUICK_VIEW_ADVANCED = "android.intent.extra.QUICK_VIEW_ADVANCED";
  public static final String EXTRA_QUICK_VIEW_FEATURES = "android.intent.extra.QUICK_VIEW_FEATURES";
  public static final String EXTRA_QUIET_MODE = "android.intent.extra.QUIET_MODE";
  @SystemApi
  public static final String EXTRA_REASON = "android.intent.extra.REASON";
  public static final String EXTRA_REFERRER = "android.intent.extra.REFERRER";
  public static final String EXTRA_REFERRER_NAME = "android.intent.extra.REFERRER_NAME";
  @SystemApi
  public static final String EXTRA_REMOTE_CALLBACK = "android.intent.extra.REMOTE_CALLBACK";
  public static final String EXTRA_REMOTE_INTENT_TOKEN = "android.intent.extra.remote_intent_token";
  public static final String EXTRA_REMOVED_FOR_ALL_USERS = "android.intent.extra.REMOVED_FOR_ALL_USERS";
  public static final String EXTRA_REPLACEMENT_EXTRAS = "android.intent.extra.REPLACEMENT_EXTRAS";
  public static final String EXTRA_REPLACING = "android.intent.extra.REPLACING";
  public static final String EXTRA_RESTRICTIONS_BUNDLE = "android.intent.extra.restrictions_bundle";
  public static final String EXTRA_RESTRICTIONS_INTENT = "android.intent.extra.restrictions_intent";
  public static final String EXTRA_RESTRICTIONS_LIST = "android.intent.extra.restrictions_list";
  @SystemApi
  public static final String EXTRA_RESULT_NEEDED = "android.intent.extra.RESULT_NEEDED";
  public static final String EXTRA_RESULT_RECEIVER = "android.intent.extra.RESULT_RECEIVER";
  public static final String EXTRA_RETURN_RESULT = "android.intent.extra.RETURN_RESULT";
  public static final String EXTRA_SETTING_NAME = "setting_name";
  public static final String EXTRA_SETTING_NEW_VALUE = "new_value";
  public static final String EXTRA_SETTING_PREVIOUS_VALUE = "previous_value";
  public static final String EXTRA_SETTING_RESTORED_FROM_SDK_INT = "restored_from_sdk_int";
  @Deprecated
  public static final String EXTRA_SHORTCUT_ICON = "android.intent.extra.shortcut.ICON";
  @Deprecated
  public static final String EXTRA_SHORTCUT_ICON_RESOURCE = "android.intent.extra.shortcut.ICON_RESOURCE";
  @Deprecated
  public static final String EXTRA_SHORTCUT_INTENT = "android.intent.extra.shortcut.INTENT";
  @Deprecated
  public static final String EXTRA_SHORTCUT_NAME = "android.intent.extra.shortcut.NAME";
  public static final String EXTRA_SHUTDOWN_USERSPACE_ONLY = "android.intent.extra.SHUTDOWN_USERSPACE_ONLY";
  public static final String EXTRA_SIM_ACTIVATION_RESPONSE = "android.intent.extra.SIM_ACTIVATION_RESPONSE";
  public static final String EXTRA_SPLIT_NAME = "android.intent.extra.SPLIT_NAME";
  public static final String EXTRA_STREAM = "android.intent.extra.STREAM";
  public static final String EXTRA_SUBJECT = "android.intent.extra.SUBJECT";
  public static final String EXTRA_SUSPENDED_PACKAGE_EXTRAS = "android.intent.extra.SUSPENDED_PACKAGE_EXTRAS";
  @SystemApi
  @Deprecated
  public static final String EXTRA_SYSTEM_ID = "systemId";
  public static final String EXTRA_TASK_ID = "android.intent.extra.TASK_ID";
  public static final String EXTRA_TEMPLATE = "android.intent.extra.TEMPLATE";
  public static final String EXTRA_TEXT = "android.intent.extra.TEXT";
  public static final String EXTRA_THERMAL_STATE = "android.intent.extra.THERMAL_STATE";
  public static final int EXTRA_THERMAL_STATE_EXCEEDED = 2;
  public static final int EXTRA_THERMAL_STATE_NORMAL = 0;
  public static final int EXTRA_THERMAL_STATE_WARNING = 1;
  public static final String EXTRA_TIME_PREF_24_HOUR_FORMAT = "android.intent.extra.TIME_PREF_24_HOUR_FORMAT";
  public static final int EXTRA_TIME_PREF_VALUE_USE_12_HOUR = 0;
  public static final int EXTRA_TIME_PREF_VALUE_USE_24_HOUR = 1;
  public static final int EXTRA_TIME_PREF_VALUE_USE_LOCALE_DEFAULT = 2;
  public static final String EXTRA_TITLE = "android.intent.extra.TITLE";
  public static final String EXTRA_UID = "android.intent.extra.UID";
  public static final String EXTRA_UNINSTALL_ALL_USERS = "android.intent.extra.UNINSTALL_ALL_USERS";
  @SystemApi
  public static final String EXTRA_UNKNOWN_INSTANT_APP = "android.intent.extra.UNKNOWN_INSTANT_APP";
  public static final String EXTRA_USER = "android.intent.extra.USER";
  public static final String EXTRA_USER_HANDLE = "android.intent.extra.user_handle";
  public static final String EXTRA_USER_ID = "android.intent.extra.USER_ID";
  public static final String EXTRA_USER_REQUESTED_SHUTDOWN = "android.intent.extra.USER_REQUESTED_SHUTDOWN";
  @SystemApi
  public static final String EXTRA_VERIFICATION_BUNDLE = "android.intent.extra.VERIFICATION_BUNDLE";
  @Deprecated
  public static final String EXTRA_VERSION_CODE = "android.intent.extra.VERSION_CODE";
  @SystemApi
  @Deprecated
  public static final String EXTRA_VOICE_RADIO_TECH = "radioTechnology";
  @SystemApi
  @Deprecated
  public static final String EXTRA_VOICE_REG_STATE = "voiceRegState";
  @SystemApi
  @Deprecated
  public static final String EXTRA_VOICE_ROAMING_TYPE = "voiceRoamingType";
  public static final String EXTRA_WIPE_ESIMS = "com.android.internal.intent.extra.WIPE_ESIMS";
  public static final String EXTRA_WIPE_EXTERNAL_STORAGE = "android.intent.extra.WIPE_EXTERNAL_STORAGE";
  public static final int FILL_IN_ACTION = 1;
  public static final int FILL_IN_CATEGORIES = 4;
  public static final int FILL_IN_CLIP_DATA = 128;
  public static final int FILL_IN_COMPONENT = 8;
  public static final int FILL_IN_DATA = 2;
  public static final int FILL_IN_PACKAGE = 16;
  public static final int FILL_IN_SELECTOR = 64;
  public static final int FILL_IN_SOURCE_BOUNDS = 32;
  public static final int FLAG_ACTIVITY_BROUGHT_TO_FRONT = 4194304;
  public static final int FLAG_ACTIVITY_CLEAR_TASK = 32768;
  public static final int FLAG_ACTIVITY_CLEAR_TOP = 67108864;
  @Deprecated
  public static final int FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET = 524288;
  public static final int FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS = 8388608;
  public static final int FLAG_ACTIVITY_FORWARD_RESULT = 33554432;
  public static final int FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY = 1048576;
  public static final int FLAG_ACTIVITY_LAUNCH_ADJACENT = 4096;
  public static final int FLAG_ACTIVITY_MATCH_EXTERNAL = 2048;
  public static final int FLAG_ACTIVITY_MULTIPLE_TASK = 134217728;
  public static final int FLAG_ACTIVITY_NEW_DOCUMENT = 524288;
  public static final int FLAG_ACTIVITY_NEW_TASK = 268435456;
  public static final int FLAG_ACTIVITY_NO_ANIMATION = 65536;
  public static final int FLAG_ACTIVITY_NO_HISTORY = 1073741824;
  public static final int FLAG_ACTIVITY_NO_USER_ACTION = 262144;
  public static final int FLAG_ACTIVITY_PREVIOUS_IS_TOP = 16777216;
  public static final int FLAG_ACTIVITY_REORDER_TO_FRONT = 131072;
  public static final int FLAG_ACTIVITY_RESET_TASK_IF_NEEDED = 2097152;
  public static final int FLAG_ACTIVITY_RETAIN_IN_RECENTS = 8192;
  public static final int FLAG_ACTIVITY_SINGLE_TOP = 536870912;
  public static final int FLAG_ACTIVITY_TASK_ON_HOME = 16384;
  public static final int FLAG_DEBUG_LOG_RESOLUTION = 8;
  public static final int FLAG_DEBUG_TRIAGED_MISSING = 256;
  public static final int FLAG_EXCLUDE_STOPPED_PACKAGES = 16;
  public static final int FLAG_FROM_BACKGROUND = 4;
  public static final int FLAG_GRANT_PERSISTABLE_URI_PERMISSION = 64;
  public static final int FLAG_GRANT_PREFIX_URI_PERMISSION = 128;
  public static final int FLAG_GRANT_READ_URI_PERMISSION = 1;
  public static final int FLAG_GRANT_WRITE_URI_PERMISSION = 2;
  public static final int FLAG_IGNORE_EPHEMERAL = 512;
  public static final int FLAG_INCLUDE_STOPPED_PACKAGES = 32;
  public static final int FLAG_RECEIVER_BOOT_UPGRADE = 33554432;
  public static final int FLAG_RECEIVER_EXCLUDE_BACKGROUND = 8388608;
  public static final int FLAG_RECEIVER_FOREGROUND = 268435456;
  public static final int FLAG_RECEIVER_FROM_SHELL = 4194304;
  public static final int FLAG_RECEIVER_INCLUDE_BACKGROUND = 16777216;
  public static final int FLAG_RECEIVER_NO_ABORT = 134217728;
  public static final int FLAG_RECEIVER_REGISTERED_ONLY = 1073741824;
  public static final int FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT = 67108864;
  public static final int FLAG_RECEIVER_REPLACE_PENDING = 536870912;
  public static final int FLAG_RECEIVER_VISIBLE_TO_INSTANT_APPS = 2097152;
  public static final int IMMUTABLE_FLAGS = 195;
  public static final String METADATA_DOCK_HOME = "android.dock_home";
  public static final String METADATA_SETUP_VERSION = "android.SETUP_VERSION";
  private static final String TAG_CATEGORIES = "categories";
  private static final String TAG_EXTRA = "extra";
  public static final int URI_ALLOW_UNSAFE = 4;
  public static final int URI_ANDROID_APP_SCHEME = 2;
  public static final int URI_INTENT_SCHEME = 1;
  private String mAction;
  private ArraySet<String> mCategories;
  private ClipData mClipData;
  private ComponentName mComponent;
  private int mContentUserHint = -2;
  private Uri mData;
  private Bundle mExtras;
  private int mFlags;
  private String mLaunchToken;
  private String mPackage;
  private Intent mSelector;
  private Rect mSourceBounds;
  private String mType;
  
  public Intent() {}
  
  public Intent(Context paramContext, Class<?> paramClass)
  {
    mComponent = new ComponentName(paramContext, paramClass);
  }
  
  public Intent(Intent paramIntent)
  {
    this(paramIntent, 0);
  }
  
  private Intent(Intent paramIntent, int paramInt)
  {
    mAction = mAction;
    mData = mData;
    mType = mType;
    mPackage = mPackage;
    mComponent = mComponent;
    if (mCategories != null) {
      mCategories = new ArraySet(mCategories);
    }
    if (paramInt != 1)
    {
      mFlags = mFlags;
      mContentUserHint = mContentUserHint;
      mLaunchToken = mLaunchToken;
      if (mSourceBounds != null) {
        mSourceBounds = new Rect(mSourceBounds);
      }
      if (mSelector != null) {
        mSelector = new Intent(mSelector);
      }
      if (paramInt != 2)
      {
        if (mExtras != null) {
          mExtras = new Bundle(mExtras);
        }
        if (mClipData != null) {
          mClipData = new ClipData(mClipData);
        }
      }
      else if ((mExtras != null) && (!mExtras.maybeIsEmpty()))
      {
        mExtras = Bundle.STRIPPED;
      }
    }
  }
  
  protected Intent(Parcel paramParcel)
  {
    readFromParcel(paramParcel);
  }
  
  public Intent(String paramString)
  {
    setAction(paramString);
  }
  
  public Intent(String paramString, Uri paramUri)
  {
    setAction(paramString);
    mData = paramUri;
  }
  
  public Intent(String paramString, Uri paramUri, Context paramContext, Class<?> paramClass)
  {
    setAction(paramString);
    mData = paramUri;
    mComponent = new ComponentName(paramContext, paramClass);
  }
  
  public static Intent createChooser(Intent paramIntent, CharSequence paramCharSequence)
  {
    return createChooser(paramIntent, paramCharSequence, null);
  }
  
  public static Intent createChooser(Intent paramIntent, CharSequence paramCharSequence, IntentSender paramIntentSender)
  {
    Intent localIntent = new Intent("android.intent.action.CHOOSER");
    localIntent.putExtra("android.intent.extra.INTENT", paramIntent);
    if (paramCharSequence != null) {
      localIntent.putExtra("android.intent.extra.TITLE", paramCharSequence);
    }
    if (paramIntentSender != null) {
      localIntent.putExtra("android.intent.extra.CHOSEN_COMPONENT_INTENT_SENDER", paramIntentSender);
    }
    int i = paramIntent.getFlags() & 0xC3;
    if (i != 0)
    {
      paramIntentSender = paramIntent.getClipData();
      paramCharSequence = paramIntentSender;
      if (paramIntentSender == null)
      {
        paramCharSequence = paramIntentSender;
        if (paramIntent.getData() != null)
        {
          paramIntentSender = new ClipData.Item(paramIntent.getData());
          if (paramIntent.getType() != null)
          {
            paramCharSequence = new String[1];
            paramCharSequence[0] = paramIntent.getType();
            paramIntent = paramCharSequence;
          }
          else
          {
            paramIntent = new String[0];
          }
          paramCharSequence = new ClipData(null, paramIntent, paramIntentSender);
        }
      }
      if (paramCharSequence != null)
      {
        localIntent.setClipData(paramCharSequence);
        localIntent.addFlags(i);
      }
    }
    return localIntent;
  }
  
  public static String dockStateToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 4: 
      return "EXTRA_DOCK_STATE_HE_DESK";
    case 3: 
      return "EXTRA_DOCK_STATE_LE_DESK";
    case 2: 
      return "EXTRA_DOCK_STATE_CAR";
    case 1: 
      return "EXTRA_DOCK_STATE_DESK";
    }
    return "EXTRA_DOCK_STATE_UNDOCKED";
  }
  
  @Deprecated
  public static Intent getIntent(String paramString)
    throws URISyntaxException
  {
    return parseUri(paramString, 0);
  }
  
  public static Intent getIntentOld(String paramString)
    throws URISyntaxException
  {
    return getIntentOld(paramString, 0);
  }
  
  private static Intent getIntentOld(String paramString, int paramInt)
    throws URISyntaxException
  {
    int i = paramString.lastIndexOf('#');
    if (i >= 0)
    {
      Object localObject = null;
      int j = 0;
      int k = i + 1;
      int m = k;
      if (paramString.regionMatches(k, "action(", 0, 7))
      {
        j = 1;
        m = k + 7;
        k = paramString.indexOf(')', m);
        localObject = paramString.substring(m, k);
        m = k + 1;
      }
      localObject = new Intent((String)localObject);
      k = m;
      int n;
      int i1;
      if (paramString.regionMatches(m, "categories(", 0, 11))
      {
        j = 1;
        k = m + 11;
        n = paramString.indexOf(')', k);
        while (k < n)
        {
          i1 = paramString.indexOf('!', k);
          if (i1 >= 0)
          {
            m = i1;
            if (i1 <= n) {}
          }
          else
          {
            m = n;
          }
          if (k < m) {
            ((Intent)localObject).addCategory(paramString.substring(k, m));
          }
          k = m + 1;
        }
        k = n + 1;
      }
      m = k;
      if (paramString.regionMatches(k, "type(", 0, 5))
      {
        j = 1;
        k += 5;
        m = paramString.indexOf(')', k);
        mType = paramString.substring(k, m);
        m++;
      }
      k = m;
      if (paramString.regionMatches(m, "launchFlags(", 0, 12))
      {
        j = 1;
        m += 12;
        k = paramString.indexOf(')', m);
        mFlags = Integer.decode(paramString.substring(m, k)).intValue();
        if ((paramInt & 0x4) == 0) {
          mFlags &= 0xFF3C;
        }
        k++;
      }
      paramInt = k;
      if (paramString.regionMatches(k, "component(", 0, 10))
      {
        j = 1;
        k += 10;
        paramInt = paramString.indexOf(')', k);
        m = paramString.indexOf('!', k);
        if ((m >= 0) && (m < paramInt)) {
          mComponent = new ComponentName(paramString.substring(k, m), paramString.substring(m + 1, paramInt));
        }
        paramInt++;
      }
      if (paramString.regionMatches(paramInt, "extras(", 0, 7))
      {
        k = 1;
        paramInt += 7;
        m = paramString.indexOf(')', paramInt);
        if (m != -1)
        {
          String str1;
          String str2;
          for (;;)
          {
            j = k;
            if (paramInt >= m) {
              break label906;
            }
            j = paramString.indexOf('=', paramInt);
            if ((j <= paramInt + 1) || (paramInt >= m)) {
              break label880;
            }
            n = paramString.charAt(paramInt);
            str1 = paramString.substring(paramInt + 1, j);
            i1 = j + 1;
            j = paramString.indexOf('!', i1);
            if (j != -1)
            {
              paramInt = j;
              if (j < m) {}
            }
            else
            {
              paramInt = m;
            }
            if (i1 >= paramInt) {
              break label866;
            }
            str2 = paramString.substring(i1, paramInt);
            if (mExtras == null) {
              mExtras = new Bundle();
            }
            if (n != 66) {
              if (n != 83) {
                if (n != 102) {
                  if (n != 105) {
                    if (n != 108) {
                      if (n != 115) {
                        switch (n)
                        {
                        default: 
                          try
                          {
                            localObject = new java/net/URISyntaxException;
                            try
                            {
                              ((URISyntaxException)localObject).<init>(paramString, "EXTRA has unknown type", paramInt);
                              throw ((Throwable)localObject);
                            }
                            catch (NumberFormatException localNumberFormatException1) {}
                            try
                            {
                              mExtras.putDouble(str1, Double.parseDouble(str2));
                              break label805;
                              mExtras.putChar(str1, Uri.decode(str2).charAt(0));
                              break label805;
                              mExtras.putByte(str1, Byte.parseByte(str2));
                              break label805;
                              mExtras.putShort(str1, Short.parseShort(str2));
                              break label805;
                              mExtras.putLong(str1, Long.parseLong(str2));
                              break label805;
                              mExtras.putInt(str1, Integer.parseInt(str2));
                              break label805;
                              mExtras.putFloat(str1, Float.parseFloat(str2));
                              break label805;
                              mExtras.putString(str1, Uri.decode(str2));
                              break label805;
                              mExtras.putBoolean(str1, Boolean.parseBoolean(str2));
                              j = paramString.charAt(paramInt);
                              if (j == 41)
                              {
                                j = k;
                                break label906;
                              }
                              if (j == 33) {
                                paramInt++;
                              } else {
                                throw new URISyntaxException(paramString, "EXTRA missing '!'", paramInt);
                              }
                            }
                            catch (NumberFormatException localNumberFormatException3) {}
                          }
                          catch (NumberFormatException localNumberFormatException2) {}
                        }
                      }
                    }
                  }
                }
              }
            }
          }
          label805:
          throw new URISyntaxException(paramString, "EXTRA value can't be parsed", paramInt);
          label866:
          throw new URISyntaxException(paramString, "EXTRA missing '!'", i1);
          label880:
          throw new URISyntaxException(paramString, "EXTRA missing '='", paramInt);
        }
        throw new URISyntaxException(paramString, "EXTRA missing trailing ')'", paramInt);
      }
      label906:
      if (j != 0) {
        mData = Uri.parse(paramString.substring(0, i));
      } else {
        mData = Uri.parse(paramString);
      }
      if (mAction == null) {
        mAction = "android.intent.action.VIEW";
      }
      paramString = localNumberFormatException3;
    }
    else
    {
      paramString = new Intent("android.intent.action.VIEW", Uri.parse(paramString));
    }
    return paramString;
  }
  
  public static boolean isAccessUriMode(int paramInt)
  {
    boolean bool;
    if ((paramInt & 0x3) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private static ClipData.Item makeClipItem(ArrayList<Uri> paramArrayList, ArrayList<CharSequence> paramArrayList1, ArrayList<String> paramArrayList2, int paramInt)
  {
    if (paramArrayList != null) {
      paramArrayList = (Uri)paramArrayList.get(paramInt);
    } else {
      paramArrayList = null;
    }
    if (paramArrayList1 != null) {
      paramArrayList1 = (CharSequence)paramArrayList1.get(paramInt);
    } else {
      paramArrayList1 = null;
    }
    if (paramArrayList2 != null) {
      paramArrayList2 = (String)paramArrayList2.get(paramInt);
    } else {
      paramArrayList2 = null;
    }
    return new ClipData.Item(paramArrayList1, paramArrayList2, null, paramArrayList);
  }
  
  public static Intent makeMainActivity(ComponentName paramComponentName)
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.setComponent(paramComponentName);
    localIntent.addCategory("android.intent.category.LAUNCHER");
    return localIntent;
  }
  
  public static Intent makeMainSelectorActivity(String paramString1, String paramString2)
  {
    Intent localIntent1 = new Intent("android.intent.action.MAIN");
    localIntent1.addCategory("android.intent.category.LAUNCHER");
    Intent localIntent2 = new Intent();
    localIntent2.setAction(paramString1);
    localIntent2.addCategory(paramString2);
    localIntent1.setSelector(localIntent2);
    return localIntent1;
  }
  
  public static Intent makeRestartActivityTask(ComponentName paramComponentName)
  {
    paramComponentName = makeMainActivity(paramComponentName);
    paramComponentName.addFlags(268468224);
    return paramComponentName;
  }
  
  public static String normalizeMimeType(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    String str = paramString.trim().toLowerCase(Locale.ROOT);
    int i = str.indexOf(';');
    paramString = str;
    if (i != -1) {
      paramString = str.substring(0, i);
    }
    return paramString;
  }
  
  public static Intent parseCommandArgs(ShellCommand paramShellCommand, CommandOptionHandler paramCommandOptionHandler)
    throws URISyntaxException
  {
    Intent localIntent = new Intent();
    Object localObject1 = null;
    int i = 0;
    Object localObject2 = localIntent;
    Object localObject3 = null;
    Object localObject4;
    for (;;)
    {
      localObject4 = paramShellCommand.getNextOption();
      j = 7;
      if (localObject4 == null) {
        break label3123;
      }
      int k = ((String)localObject4).hashCode();
      switch (k)
      {
      default: 
        switch (k)
        {
        default: 
          break;
        case 1816558127: 
          if (((String)localObject4).equals("--grant-write-uri-permission")) {
            j = 25;
          }
          break;
        case 1765369476: 
          if (((String)localObject4).equals("--activity-multiple-task")) {
            j = 36;
          }
          break;
        case 1742380566: 
          if (((String)localObject4).equals("--grant-read-uri-permission")) {
            j = 24;
          }
          break;
        case 1652786753: 
          if (((String)localObject4).equals("--receiver-foreground")) {
            j = 49;
          }
          break;
        case 1453225122: 
          if (((String)localObject4).equals("--receiver-no-abort")) {
            j = 50;
          }
          break;
        case 1398403374: 
          if (((String)localObject4).equals("--activity-launched-from-history")) {
            j = 35;
          }
          break;
        case 1353919836: 
          if (((String)localObject4).equals("--activity-clear-when-task-reset")) {
            j = 33;
          }
          break;
        case 1332992761: 
          if (((String)localObject4).equals("--esal")) {
            j = 19;
          }
          break;
        case 1332986034: 
          if (((String)localObject4).equals("--elal")) {
            j = 14;
          }
          break;
        case 1332983151: 
          if (((String)localObject4).equals("--eial")) {
            j = 11;
          }
          break;
        case 1332980268: 
          if (((String)localObject4).equals("--efal")) {
            j = 17;
          }
          break;
        case 1207327103: 
          if (((String)localObject4).equals("--selector")) {
            j = 52;
          }
          break;
        case 1110195121: 
          if (((String)localObject4).equals("--activity-match-external")) {
            j = 46;
          }
          break;
        case 775126336: 
          if (((String)localObject4).equals("--receiver-replace-pending")) {
            j = 48;
          }
          break;
        case 749648146: 
          if (((String)localObject4).equals("--include-stopped-packages")) {
            j = 29;
          }
          break;
        case 580418080: 
          if (((String)localObject4).equals("--exclude-stopped-packages")) {
            j = 28;
          }
          break;
        case 527014976: 
          if (((String)localObject4).equals("--grant-persistable-uri-permission")) {
            j = 26;
          }
          break;
        case 438531630: 
          if (((String)localObject4).equals("--activity-single-top")) {
            j = 43;
          }
          break;
        case 436286937: 
          if (((String)localObject4).equals("--receiver-registered-only")) {
            j = 47;
          }
          break;
        case 429439306: 
          if (((String)localObject4).equals("--activity-no-user-action")) {
            j = 39;
          }
          break;
        case 236677687: 
          if (((String)localObject4).equals("--activity-clear-top")) {
            j = 32;
          }
          break;
        case 190913209: 
          if (((String)localObject4).equals("--activity-reset-task-if-needed")) {
            j = 42;
          }
          break;
        case 88747734: 
          if (((String)localObject4).equals("--activity-no-animation")) {
            j = 37;
          }
          break;
        case 69120454: 
          if (((String)localObject4).equals("--activity-exclude-from-recents")) {
            j = 34;
          }
          break;
        case 42999776: 
          if (((String)localObject4).equals("--esn")) {
            j = 6;
          }
          break;
        case 42999763: 
          if (((String)localObject4).equals("--esa")) {
            j = 18;
          }
          break;
        case 42999546: 
          if (((String)localObject4).equals("--ela")) {
            j = 13;
          }
          break;
        case 42999453: 
          if (((String)localObject4).equals("--eia")) {
            j = 10;
          }
          break;
        case 42999360: 
          if (((String)localObject4).equals("--efa")) {
            j = 16;
          }
          break;
        case 42999280: 
          if (((String)localObject4).equals("--ecn")) {
            j = 9;
          }
          break;
        case 1387093: 
          if (((String)localObject4).equals("--ez")) {
            j = 20;
          }
          break;
        case 1387088: 
          if (((String)localObject4).equals("--eu")) {
            j = 8;
          }
          break;
        case 1387086: 
          if (((String)localObject4).equals("--es")) {
            j = 5;
          }
          break;
        case 1387079: 
          if (((String)localObject4).equals("--el")) {
            j = 12;
          }
          break;
        case 1387076: 
          if (!((String)localObject4).equals("--ei")) {
            break;
          }
          break;
        case 1387073: 
          if (((String)localObject4).equals("--ef")) {
            j = 15;
          }
          break;
        case 1511: 
          if (((String)localObject4).equals("-t")) {
            j = 2;
          }
          break;
        case 1507: 
          if (((String)localObject4).equals("-p")) {
            j = 22;
          }
          break;
        case 1505: 
          if (((String)localObject4).equals("-n")) {
            j = 21;
          }
          break;
        case 1492: 
          if (((String)localObject4).equals("-a")) {
            j = 0;
          }
          break;
        case -780160399: 
          if (((String)localObject4).equals("--receiver-include-background")) {
            j = 51;
          }
          break;
        case -792169302: 
          if (((String)localObject4).equals("--activity-previous-is-top")) {
            j = 40;
          }
          break;
        case -833172539: 
          if (((String)localObject4).equals("--activity-brought-to-front")) {
            j = 31;
          }
          break;
        case -848214457: 
          if (((String)localObject4).equals("--activity-reorder-to-front")) {
            j = 41;
          }
          break;
        case -1069446353: 
          if (((String)localObject4).equals("--debug-log-resolution")) {
            j = 30;
          }
          break;
        case -1252939549: 
          if (((String)localObject4).equals("--activity-clear-task")) {
            j = 44;
          }
          break;
        case -1630559130: 
          if (((String)localObject4).equals("--activity-no-history")) {
            j = 38;
          }
          break;
        case -2118172637: 
          if (((String)localObject4).equals("--activity-task-on-home")) {
            j = 45;
          }
          break;
        case -2147394086: 
          if (((String)localObject4).equals("--grant-prefix-uri-permission")) {
            j = 27;
          }
          break;
        }
        break;
      case 1497: 
        if (((String)localObject4).equals("-f")) {
          j = 23;
        }
        break;
      case 1496: 
        if (((String)localObject4).equals("-e")) {
          j = 4;
        }
        break;
      case 1495: 
        if (((String)localObject4).equals("-d")) {
          j = 1;
        }
        break;
      case 1494: 
        if (((String)localObject4).equals("-c")) {
          j = 3;
        }
        break;
      }
      j = -1;
      switch (j)
      {
      default: 
        if ((paramCommandOptionHandler == null) || (!paramCommandOptionHandler.handleOption((String)localObject4, paramShellCommand))) {
          break label3088;
        }
        break;
      case 52: 
        ((Intent)localObject2).setDataAndType((Uri)localObject1, (String)localObject3);
        localObject2 = new Intent();
        break;
      case 51: 
        ((Intent)localObject2).addFlags(16777216);
        break;
      case 50: 
        ((Intent)localObject2).addFlags(134217728);
        break;
      case 49: 
        ((Intent)localObject2).addFlags(268435456);
        break;
      case 48: 
        ((Intent)localObject2).addFlags(536870912);
        break;
      case 47: 
        ((Intent)localObject2).addFlags(1073741824);
        break;
      case 46: 
        ((Intent)localObject2).addFlags(2048);
        break;
      case 45: 
        ((Intent)localObject2).addFlags(16384);
        break;
      case 44: 
        ((Intent)localObject2).addFlags(32768);
        break;
      case 43: 
        ((Intent)localObject2).addFlags(536870912);
        break;
      case 42: 
        ((Intent)localObject2).addFlags(2097152);
        break;
      case 41: 
        ((Intent)localObject2).addFlags(131072);
        break;
      case 40: 
        ((Intent)localObject2).addFlags(16777216);
        break;
      case 39: 
        ((Intent)localObject2).addFlags(262144);
        break;
      case 38: 
        ((Intent)localObject2).addFlags(1073741824);
        break;
      case 37: 
        ((Intent)localObject2).addFlags(65536);
        break;
      case 36: 
        ((Intent)localObject2).addFlags(134217728);
        break;
      case 35: 
        ((Intent)localObject2).addFlags(1048576);
        break;
      case 34: 
        ((Intent)localObject2).addFlags(8388608);
        break;
      case 33: 
        ((Intent)localObject2).addFlags(524288);
        break;
      case 32: 
        ((Intent)localObject2).addFlags(67108864);
        break;
      case 31: 
        ((Intent)localObject2).addFlags(4194304);
        break;
      case 30: 
        ((Intent)localObject2).addFlags(8);
        break;
      case 29: 
        ((Intent)localObject2).addFlags(32);
        break;
      case 28: 
        ((Intent)localObject2).addFlags(16);
        break;
      case 27: 
        ((Intent)localObject2).addFlags(128);
        break;
      case 26: 
        ((Intent)localObject2).addFlags(64);
        break;
      case 25: 
        ((Intent)localObject2).addFlags(2);
        break;
      case 24: 
        ((Intent)localObject2).addFlags(1);
        break;
      case 23: 
        ((Intent)localObject2).setFlags(Integer.decode(paramShellCommand.getNextArgRequired()).intValue());
        break;
      case 22: 
        ((Intent)localObject2).setPackage(paramShellCommand.getNextArgRequired());
        if (localObject2 == localIntent) {
          i = 1;
        }
        break;
      }
      for (;;)
      {
        break;
        localObject4 = paramShellCommand.getNextArgRequired();
        Object localObject5 = ComponentName.unflattenFromString((String)localObject4);
        if (localObject5 != null)
        {
          ((Intent)localObject2).setComponent((ComponentName)localObject5);
          if (localObject2 == localIntent) {
            i = 1;
          }
        }
        else
        {
          paramShellCommand = new StringBuilder();
          paramShellCommand.append("Bad component name: ");
          paramShellCommand.append((String)localObject4);
          throw new IllegalArgumentException(paramShellCommand.toString());
          boolean bool = true;
          localObject5 = paramShellCommand.getNextArgRequired();
          localObject4 = paramShellCommand.getNextArgRequired().toLowerCase();
          if ((!"true".equals(localObject4)) && (!"t".equals(localObject4)))
          {
            if ((!"false".equals(localObject4)) && (!"f".equals(localObject4))) {
              try
              {
                j = Integer.decode((String)localObject4).intValue();
                if (j == 0) {
                  bool = false;
                }
              }
              catch (NumberFormatException paramShellCommand)
              {
                paramShellCommand = new StringBuilder();
                paramShellCommand.append("Invalid boolean value: ");
                paramShellCommand.append((String)localObject4);
                throw new IllegalArgumentException(paramShellCommand.toString());
              }
            }
            bool = false;
          }
          else
          {
            bool = true;
          }
          ((Intent)localObject2).putExtra((String)localObject5, bool);
          continue;
          Object localObject6 = paramShellCommand.getNextArgRequired();
          localObject4 = paramShellCommand.getNextArgRequired().split("(?<!\\\\),");
          localObject5 = new ArrayList(localObject4.length);
          for (j = 0; j < localObject4.length; j++) {
            ((ArrayList)localObject5).add(localObject4[j]);
          }
          ((Intent)localObject2).putExtra((String)localObject6, (Serializable)localObject5);
          break label2544;
          ((Intent)localObject2).putExtra(paramShellCommand.getNextArgRequired(), paramShellCommand.getNextArgRequired().split("(?<!\\\\),"));
          break label2544;
          localObject5 = paramShellCommand.getNextArgRequired();
          localObject6 = paramShellCommand.getNextArgRequired().split(",");
          localObject4 = new ArrayList(localObject6.length);
          for (j = 0; j < localObject6.length; j++) {
            ((ArrayList)localObject4).add(Float.valueOf(localObject6[j]));
          }
          ((Intent)localObject2).putExtra((String)localObject5, (Serializable)localObject4);
          break label2544;
          localObject4 = paramShellCommand.getNextArgRequired();
          localObject6 = paramShellCommand.getNextArgRequired().split(",");
          localObject5 = new float[localObject6.length];
          for (j = 0; j < localObject6.length; j++) {
            localObject5[j] = Float.valueOf(localObject6[j]).floatValue();
          }
          ((Intent)localObject2).putExtra((String)localObject4, (float[])localObject5);
          break label2544;
          ((Intent)localObject2).putExtra(paramShellCommand.getNextArgRequired(), Float.valueOf(paramShellCommand.getNextArgRequired()));
          label2544:
          label3082:
          for (;;)
          {
            i = 1;
            break;
            localObject5 = paramShellCommand.getNextArgRequired();
            localObject6 = paramShellCommand.getNextArgRequired().split(",");
            localObject4 = new ArrayList(localObject6.length);
            for (j = 0; j < localObject6.length; j++) {
              ((ArrayList)localObject4).add(Long.valueOf(localObject6[j]));
            }
            ((Intent)localObject2).putExtra((String)localObject5, (Serializable)localObject4);
            continue;
            localObject5 = paramShellCommand.getNextArgRequired();
            localObject6 = paramShellCommand.getNextArgRequired().split(",");
            localObject4 = new long[localObject6.length];
            for (j = 0; j < localObject6.length; j++) {
              localObject4[j] = Long.valueOf(localObject6[j]).longValue();
            }
            ((Intent)localObject2).putExtra((String)localObject5, (long[])localObject4);
            continue;
            ((Intent)localObject2).putExtra(paramShellCommand.getNextArgRequired(), Long.valueOf(paramShellCommand.getNextArgRequired()));
            break;
            localObject4 = paramShellCommand.getNextArgRequired();
            localObject6 = paramShellCommand.getNextArgRequired().split(",");
            localObject5 = new ArrayList(localObject6.length);
            for (j = 0; j < localObject6.length; j++) {
              ((ArrayList)localObject5).add(Integer.decode(localObject6[j]));
            }
            ((Intent)localObject2).putExtra((String)localObject4, (Serializable)localObject5);
            break;
            localObject6 = paramShellCommand.getNextArgRequired();
            localObject5 = paramShellCommand.getNextArgRequired().split(",");
            localObject4 = new int[localObject5.length];
            for (j = 0; j < localObject5.length; j++) {
              localObject4[j] = Integer.decode(localObject5[j]).intValue();
            }
            ((Intent)localObject2).putExtra((String)localObject6, (int[])localObject4);
            break;
            localObject6 = paramShellCommand.getNextArgRequired();
            localObject4 = paramShellCommand.getNextArgRequired();
            localObject5 = ComponentName.unflattenFromString((String)localObject4);
            if (localObject5 != null)
            {
              ((Intent)localObject2).putExtra((String)localObject6, (Parcelable)localObject5);
              break;
            }
            paramShellCommand = new StringBuilder();
            paramShellCommand.append("Bad component name: ");
            paramShellCommand.append((String)localObject4);
            throw new IllegalArgumentException(paramShellCommand.toString());
            ((Intent)localObject2).putExtra(paramShellCommand.getNextArgRequired(), Uri.parse(paramShellCommand.getNextArgRequired()));
            break;
            ((Intent)localObject2).putExtra(paramShellCommand.getNextArgRequired(), Integer.decode(paramShellCommand.getNextArgRequired()));
            break;
            ((Intent)localObject2).putExtra(paramShellCommand.getNextArgRequired(), (String)null);
            break;
            ((Intent)localObject2).putExtra(paramShellCommand.getNextArgRequired(), paramShellCommand.getNextArgRequired());
            break;
            ((Intent)localObject2).addCategory(paramShellCommand.getNextArgRequired());
            if (localObject2 != localIntent) {
              break;
            }
            break label3082;
            localObject3 = paramShellCommand.getNextArgRequired();
            if (localObject2 == localIntent) {
              i = 1;
            }
            break;
            localObject1 = Uri.parse(paramShellCommand.getNextArgRequired());
            if (localObject2 == localIntent) {
              i = 1;
            }
            break;
            ((Intent)localObject2).setAction(paramShellCommand.getNextArgRequired());
            if (localObject2 != localIntent) {
              break;
            }
          }
        }
      }
    }
    label3088:
    paramShellCommand = new StringBuilder();
    paramShellCommand.append("Unknown option: ");
    paramShellCommand.append((String)localObject4);
    throw new IllegalArgumentException(paramShellCommand.toString());
    label3123:
    int j = 1;
    ((Intent)localObject2).setDataAndType((Uri)localObject1, (String)localObject3);
    if (localObject2 == localIntent) {
      j = 0;
    }
    paramCommandOptionHandler = (CommandOptionHandler)localObject2;
    if (j != 0)
    {
      localIntent.setSelector((Intent)localObject2);
      paramCommandOptionHandler = localIntent;
    }
    localObject2 = paramShellCommand.getNextArg();
    paramShellCommand = null;
    if (localObject2 == null)
    {
      if (j != 0)
      {
        paramShellCommand = new Intent("android.intent.action.MAIN");
        paramShellCommand.addCategory("android.intent.category.LAUNCHER");
      }
    }
    else if (((String)localObject2).indexOf(':') >= 0)
    {
      paramShellCommand = parseUri((String)localObject2, 7);
    }
    else if (((String)localObject2).indexOf('/') >= 0)
    {
      paramShellCommand = new Intent("android.intent.action.MAIN");
      paramShellCommand.addCategory("android.intent.category.LAUNCHER");
      paramShellCommand.setComponent(ComponentName.unflattenFromString((String)localObject2));
    }
    else
    {
      paramShellCommand = new Intent("android.intent.action.MAIN");
      paramShellCommand.addCategory("android.intent.category.LAUNCHER");
      paramShellCommand.setPackage((String)localObject2);
    }
    if (paramShellCommand != null)
    {
      localObject1 = paramCommandOptionHandler.getExtras();
      localObject3 = (Bundle)null;
      paramCommandOptionHandler.replaceExtras((Bundle)localObject3);
      localObject2 = paramShellCommand.getExtras();
      paramShellCommand.replaceExtras((Bundle)localObject3);
      if ((paramCommandOptionHandler.getAction() != null) && (paramShellCommand.getCategories() != null))
      {
        localObject3 = new HashSet(paramShellCommand.getCategories()).iterator();
        while (((Iterator)localObject3).hasNext()) {
          paramShellCommand.removeCategory((String)((Iterator)localObject3).next());
        }
      }
      paramCommandOptionHandler.fillIn(paramShellCommand, 72);
      if (localObject1 == null)
      {
        paramShellCommand = (ShellCommand)localObject2;
      }
      else
      {
        paramShellCommand = (ShellCommand)localObject1;
        if (localObject2 != null)
        {
          ((Bundle)localObject2).putAll((Bundle)localObject1);
          paramShellCommand = (ShellCommand)localObject2;
        }
      }
      paramCommandOptionHandler.replaceExtras(paramShellCommand);
      i = 1;
    }
    if (i != 0) {
      return paramCommandOptionHandler;
    }
    throw new IllegalArgumentException("No intent supplied");
  }
  
  public static Intent parseIntent(Resources paramResources, XmlPullParser paramXmlPullParser, AttributeSet paramAttributeSet)
    throws XmlPullParserException, IOException
  {
    Intent localIntent = new Intent();
    Object localObject1 = paramResources.obtainAttributes(paramAttributeSet, R.styleable.Intent);
    localIntent.setAction(((TypedArray)localObject1).getString(2));
    Object localObject2 = ((TypedArray)localObject1).getString(3);
    String str = ((TypedArray)localObject1).getString(1);
    if (localObject2 != null) {
      localObject2 = Uri.parse((String)localObject2);
    } else {
      localObject2 = null;
    }
    localIntent.setDataAndType((Uri)localObject2, str);
    localObject2 = ((TypedArray)localObject1).getString(0);
    str = ((TypedArray)localObject1).getString(4);
    if ((localObject2 != null) && (str != null)) {
      localIntent.setComponent(new ComponentName((String)localObject2, str));
    }
    ((TypedArray)localObject1).recycle();
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
        break;
      }
      if ((j != 3) && (j != 4))
      {
        localObject2 = paramXmlPullParser.getName();
        if (((String)localObject2).equals("categories"))
        {
          localObject2 = paramResources.obtainAttributes(paramAttributeSet, R.styleable.IntentCategory);
          localObject1 = ((TypedArray)localObject2).getString(0);
          ((TypedArray)localObject2).recycle();
          if (localObject1 != null) {
            localIntent.addCategory((String)localObject1);
          }
          XmlUtils.skipCurrentTag(paramXmlPullParser);
        }
        else if (((String)localObject2).equals("extra"))
        {
          if (mExtras == null) {
            mExtras = new Bundle();
          }
          paramResources.parseBundleExtra("extra", paramAttributeSet, mExtras);
          XmlUtils.skipCurrentTag(paramXmlPullParser);
        }
        else
        {
          XmlUtils.skipCurrentTag(paramXmlPullParser);
        }
      }
    }
    return localIntent;
  }
  
  public static Intent parseUri(String paramString, int paramInt)
    throws URISyntaxException
  {
    Object localObject1 = paramString;
    int i = 0;
    Object localObject2 = localObject1;
    int j = i;
    try
    {
      boolean bool = ((String)localObject1).startsWith("android-app:");
      if ((paramInt & 0x3) != 0)
      {
        localObject2 = localObject1;
        j = i;
        if ((!((String)localObject1).startsWith("intent:")) && (!bool))
        {
          localObject2 = localObject1;
          j = i;
          localObject3 = new android/content/Intent;
          localObject2 = localObject1;
          j = i;
          ((Intent)localObject3).<init>("android.intent.action.VIEW");
          localObject2 = localObject1;
          j = i;
          try
          {
            ((Intent)localObject3).setData(Uri.parse(paramString));
            return localObject3;
          }
          catch (IllegalArgumentException paramString)
          {
            localObject2 = localObject1;
            j = i;
            localObject3 = new java/net/URISyntaxException;
            localObject2 = localObject1;
            j = i;
            ((URISyntaxException)localObject3).<init>((String)localObject1, paramString.getMessage());
            localObject2 = localObject1;
            j = i;
            throw ((Throwable)localObject3);
          }
        }
      }
      localObject2 = localObject1;
      j = i;
      int k = ((String)localObject1).lastIndexOf("#");
      if (k == -1)
      {
        i = k;
        if (!bool)
        {
          localObject2 = localObject1;
          j = k;
          return new Intent("android.intent.action.VIEW", Uri.parse(paramString));
        }
      }
      else
      {
        i = k;
        localObject2 = localObject1;
        j = k;
        if (!((String)localObject1).startsWith("#Intent;", k))
        {
          if (!bool)
          {
            localObject2 = localObject1;
            j = k;
            return getIntentOld(paramString, paramInt);
          }
          i = -1;
        }
      }
      localObject2 = localObject1;
      j = i;
      Object localObject5 = new android/content/Intent;
      localObject2 = localObject1;
      j = i;
      ((Intent)localObject5).<init>("android.intent.action.VIEW");
      k = 0;
      int m = 0;
      Object localObject3 = null;
      Object localObject6;
      Object localObject7;
      if (i >= 0)
      {
        localObject2 = localObject1;
        j = i;
        localObject6 = ((String)localObject1).substring(0, i);
        i += 8;
        localObject7 = localObject5;
      }
      else
      {
        localObject6 = localObject1;
        localObject7 = localObject5;
      }
      int i1;
      while (i >= 0)
      {
        localObject2 = localObject1;
        j = i;
        if (!((String)localObject1).startsWith("end", i))
        {
          localObject2 = localObject1;
          j = i;
          int n = ((String)localObject1).indexOf('=', i);
          i1 = n;
          if (n < 0) {
            i1 = i - 1;
          }
          localObject2 = localObject1;
          j = i;
          n = ((String)localObject1).indexOf(';', i);
          if (i1 < n)
          {
            localObject2 = localObject1;
            j = i;
            localObject8 = Uri.decode(((String)localObject1).substring(i1 + 1, n));
          }
          else
          {
            localObject8 = "";
          }
          localObject2 = localObject1;
          j = i;
          if (((String)localObject1).startsWith("action=", i))
          {
            localObject2 = localObject1;
            j = i;
            ((Intent)localObject7).setAction((String)localObject8);
            localObject2 = localObject1;
            if (m == 0)
            {
              k = 1;
              break label1294;
            }
          }
          Object localObject9;
          Bundle localBundle;
          for (;;)
          {
            localObject1 = localObject2;
            break;
            localObject2 = localObject1;
            j = i;
            if (((String)localObject1).startsWith("category=", i))
            {
              localObject2 = localObject1;
              j = i;
              ((Intent)localObject7).addCategory((String)localObject8);
              localObject2 = localObject1;
            }
            else
            {
              localObject2 = localObject1;
              j = i;
              if (((String)localObject1).startsWith("type=", i))
              {
                localObject2 = localObject1;
                j = i;
                mType = ((String)localObject8);
                localObject2 = localObject1;
              }
              else
              {
                localObject2 = localObject1;
                j = i;
                if (((String)localObject1).startsWith("launchFlags=", i))
                {
                  localObject2 = localObject1;
                  j = i;
                  mFlags = Integer.decode((String)localObject8).intValue();
                  localObject2 = localObject1;
                  if ((paramInt & 0x4) == 0)
                  {
                    localObject2 = localObject1;
                    j = i;
                    mFlags &= 0xFF3C;
                    localObject2 = localObject1;
                  }
                }
                else
                {
                  localObject2 = localObject1;
                  j = i;
                  if (((String)localObject1).startsWith("package=", i))
                  {
                    localObject2 = localObject1;
                    j = i;
                    mPackage = ((String)localObject8);
                    localObject2 = localObject1;
                  }
                  else
                  {
                    localObject2 = localObject1;
                    j = i;
                    if (((String)localObject1).startsWith("component=", i))
                    {
                      localObject2 = localObject1;
                      j = i;
                      mComponent = ComponentName.unflattenFromString((String)localObject8);
                      localObject2 = localObject1;
                    }
                    else
                    {
                      localObject2 = localObject1;
                      j = i;
                      if (((String)localObject1).startsWith("scheme=", i))
                      {
                        if (m != 0)
                        {
                          localObject2 = localObject1;
                          j = i;
                          localObject9 = new java/lang/StringBuilder;
                          localObject2 = localObject1;
                          j = i;
                          ((StringBuilder)localObject9).<init>();
                          localObject2 = localObject1;
                          j = i;
                          ((StringBuilder)localObject9).append((String)localObject8);
                          localObject2 = localObject1;
                          j = i;
                          ((StringBuilder)localObject9).append(":");
                          localObject2 = localObject1;
                          j = i;
                          mData = Uri.parse(((StringBuilder)localObject9).toString());
                          localObject2 = localObject1;
                          continue;
                        }
                        localObject3 = localObject8;
                        break;
                      }
                      localObject2 = localObject1;
                      j = i;
                      if (((String)localObject1).startsWith("sourceBounds=", i))
                      {
                        localObject2 = localObject1;
                        j = i;
                        mSourceBounds = Rect.unflattenFromString((String)localObject8);
                        localObject2 = localObject1;
                      }
                      else
                      {
                        if (n == i + 3)
                        {
                          localObject2 = localObject1;
                          j = i;
                          if (((String)localObject1).startsWith("SEL", i))
                          {
                            localObject2 = localObject1;
                            j = i;
                            localObject7 = new Intent();
                            m = 1;
                            break;
                          }
                        }
                        localObject2 = localObject1;
                        j = i;
                        localObject9 = Uri.decode(((String)localObject1).substring(i + 2, i1));
                        localObject2 = localObject1;
                        j = i;
                        if (mExtras == null)
                        {
                          localObject2 = localObject1;
                          j = i;
                          localBundle = new android/os/Bundle;
                          localObject2 = localObject1;
                          j = i;
                          localBundle.<init>();
                          localObject2 = localObject1;
                          j = i;
                          mExtras = localBundle;
                        }
                        localObject2 = localObject1;
                        j = i;
                        localBundle = mExtras;
                        localObject2 = localObject1;
                        j = i;
                        if (((String)localObject1).startsWith("S.", i))
                        {
                          localObject2 = localObject1;
                          j = i;
                          localBundle.putString((String)localObject9, (String)localObject8);
                          localObject2 = localObject1;
                        }
                        else
                        {
                          localObject2 = localObject1;
                          j = i;
                          if (((String)localObject1).startsWith("B.", i))
                          {
                            localObject2 = localObject1;
                            j = i;
                            localBundle.putBoolean((String)localObject9, Boolean.parseBoolean((String)localObject8));
                            localObject2 = localObject1;
                          }
                          else
                          {
                            localObject2 = localObject1;
                            j = i;
                            if (((String)localObject1).startsWith("b.", i))
                            {
                              localObject2 = localObject1;
                              j = i;
                              localBundle.putByte((String)localObject9, Byte.parseByte((String)localObject8));
                              localObject2 = localObject1;
                            }
                            else
                            {
                              localObject2 = localObject1;
                              j = i;
                              if (((String)localObject1).startsWith("c.", i))
                              {
                                localObject2 = localObject1;
                                j = i;
                                localBundle.putChar((String)localObject9, ((String)localObject8).charAt(0));
                                localObject2 = localObject1;
                              }
                              else
                              {
                                localObject2 = localObject1;
                                j = i;
                                bool = ((String)localObject1).startsWith("d.", i);
                                if (bool) {
                                  try
                                  {
                                    localBundle.putDouble((String)localObject9, Double.parseDouble((String)localObject8));
                                    localObject1 = paramString;
                                  }
                                  catch (IndexOutOfBoundsException localIndexOutOfBoundsException)
                                  {
                                    localObject2 = paramString;
                                    break label2108;
                                  }
                                }
                                localObject1 = paramString;
                                localObject2 = localObject1;
                                j = i;
                                if (((String)localObject1).startsWith("f.", i))
                                {
                                  localObject2 = localObject1;
                                  j = i;
                                  localBundle.putFloat((String)localObject9, Float.parseFloat((String)localObject8));
                                  localObject2 = localObject1;
                                }
                                else
                                {
                                  localObject2 = localObject1;
                                  j = i;
                                  if (!((String)localObject1).startsWith("i.", i)) {
                                    break label1221;
                                  }
                                  localObject2 = localObject1;
                                  j = i;
                                  localBundle.putInt((String)localObject9, Integer.parseInt((String)localObject8));
                                  localObject2 = localObject1;
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
          label1221:
          localObject2 = localObject1;
          j = i;
          if (((String)localObject1).startsWith("l.", i))
          {
            localObject2 = localObject1;
            j = i;
            localBundle.putLong((String)localObject9, Long.parseLong((String)localObject8));
          }
          else
          {
            localObject2 = localObject1;
            j = i;
            if (!((String)localObject1).startsWith("s.", i)) {
              break label1302;
            }
            localObject2 = localObject1;
            j = i;
            localBundle.putShort((String)localObject9, Short.parseShort((String)localObject8));
          }
          label1294:
          i = n + 1;
          continue;
          label1302:
          localObject2 = localObject1;
          j = i;
          paramString = new java/net/URISyntaxException;
          localObject2 = localObject1;
          j = i;
          paramString.<init>((String)localObject1, "unknown EXTRA type", i);
          localObject2 = localObject1;
          j = i;
          throw paramString;
        }
      }
      Object localObject8 = localObject7;
      if (m != 0)
      {
        localObject2 = localObject1;
        j = i;
        if (mPackage == null)
        {
          localObject2 = localObject1;
          j = i;
          ((Intent)localObject5).setSelector((Intent)localObject7);
        }
        localObject8 = localObject5;
      }
      if (localObject6 != null)
      {
        localObject2 = localObject1;
        j = i;
        Object localObject4;
        if (((String)localObject6).startsWith("intent:"))
        {
          localObject2 = localObject1;
          j = i;
          paramString = ((String)localObject6).substring(7);
          if (localIndexOutOfBoundsException != null)
          {
            localObject2 = localObject1;
            j = i;
            localObject6 = new java/lang/StringBuilder;
            localObject2 = localObject1;
            j = i;
            ((StringBuilder)localObject6).<init>();
            localObject2 = localObject1;
            j = i;
            ((StringBuilder)localObject6).append(localIndexOutOfBoundsException);
            localObject2 = localObject1;
            j = i;
            ((StringBuilder)localObject6).append(':');
            localObject2 = localObject1;
            j = i;
            ((StringBuilder)localObject6).append(paramString);
            localObject2 = localObject1;
            j = i;
            paramString = ((StringBuilder)localObject6).toString();
          }
          else
          {
            break label2028;
          }
        }
        else
        {
          paramString = localIndexOutOfBoundsException;
          localObject2 = localObject1;
          j = i;
          if (!((String)localObject6).startsWith("android-app:")) {
            break label2025;
          }
          localObject2 = localObject1;
          j = i;
          if (((String)localObject6).charAt(12) == '/')
          {
            localObject2 = localObject1;
            j = i;
            if (((String)localObject6).charAt(13) == '/')
            {
              localObject2 = localObject1;
              j = i;
              m = ((String)localObject6).indexOf('/', 14);
              if (m < 0)
              {
                localObject2 = localObject1;
                j = i;
                mPackage = ((String)localObject6).substring(14);
                if (k == 0)
                {
                  localObject2 = localObject1;
                  j = i;
                  ((Intent)localObject8).setAction("android.intent.action.MAIN");
                }
                paramString = "";
              }
              for (;;)
              {
                break;
                localObject7 = null;
                localObject2 = localObject1;
                j = i;
                mPackage = ((String)localObject6).substring(14, m);
                paramInt = m;
                localObject4 = localObject7;
                localObject2 = localObject1;
                j = i;
                if (m + 1 < ((String)localObject6).length())
                {
                  localObject2 = localObject1;
                  j = i;
                  paramInt = ((String)localObject6).indexOf('/', m + 1);
                  if (paramInt >= 0)
                  {
                    localObject2 = localObject1;
                    j = i;
                    localObject5 = ((String)localObject6).substring(m + 1, paramInt);
                    m = paramInt;
                    paramString = (String)localObject5;
                    paramInt = m;
                    localObject4 = localObject7;
                    localObject2 = localObject1;
                    j = i;
                    if (m < ((String)localObject6).length())
                    {
                      localObject2 = localObject1;
                      j = i;
                      i1 = ((String)localObject6).indexOf('/', m + 1);
                      paramString = (String)localObject5;
                      paramInt = m;
                      localObject4 = localObject7;
                      if (i1 >= 0)
                      {
                        localObject2 = localObject1;
                        j = i;
                        localObject4 = ((String)localObject6).substring(m + 1, i1);
                        paramInt = i1;
                        paramString = (String)localObject5;
                      }
                    }
                  }
                  else
                  {
                    localObject2 = localObject1;
                    j = i;
                    paramString = ((String)localObject6).substring(m + 1);
                    localObject4 = localObject7;
                    paramInt = m;
                  }
                }
                if (paramString == null)
                {
                  if (k == 0)
                  {
                    localObject2 = localObject1;
                    j = i;
                    ((Intent)localObject8).setAction("android.intent.action.MAIN");
                  }
                  paramString = "";
                }
                else if (localObject4 == null)
                {
                  localObject2 = localObject1;
                  j = i;
                  localObject4 = new java/lang/StringBuilder;
                  localObject2 = localObject1;
                  j = i;
                  ((StringBuilder)localObject4).<init>();
                  localObject2 = localObject1;
                  j = i;
                  ((StringBuilder)localObject4).append(paramString);
                  localObject2 = localObject1;
                  j = i;
                  ((StringBuilder)localObject4).append(":");
                  localObject2 = localObject1;
                  j = i;
                  paramString = ((StringBuilder)localObject4).toString();
                }
                else
                {
                  localObject2 = localObject1;
                  j = i;
                  localObject7 = new java/lang/StringBuilder;
                  localObject2 = localObject1;
                  j = i;
                  ((StringBuilder)localObject7).<init>();
                  localObject2 = localObject1;
                  j = i;
                  ((StringBuilder)localObject7).append(paramString);
                  localObject2 = localObject1;
                  j = i;
                  ((StringBuilder)localObject7).append("://");
                  localObject2 = localObject1;
                  j = i;
                  ((StringBuilder)localObject7).append((String)localObject4);
                  localObject2 = localObject1;
                  j = i;
                  ((StringBuilder)localObject7).append(((String)localObject6).substring(paramInt));
                  localObject2 = localObject1;
                  j = i;
                  paramString = ((StringBuilder)localObject7).toString();
                }
              }
              break label2022;
            }
          }
          paramString = "";
        }
        label2022:
        break label2028;
        label2025:
        paramString = (String)localObject6;
        label2028:
        localObject2 = localObject1;
        j = i;
        paramInt = paramString.length();
        if (paramInt > 0)
        {
          localObject2 = localObject1;
          j = i;
          try
          {
            mData = Uri.parse(paramString);
          }
          catch (IllegalArgumentException paramString)
          {
            localObject2 = localObject1;
            j = i;
            localObject4 = new java/net/URISyntaxException;
            localObject2 = localObject1;
            j = i;
            ((URISyntaxException)localObject4).<init>((String)localObject1, paramString.getMessage());
            localObject2 = localObject1;
            j = i;
            throw ((Throwable)localObject4);
          }
        }
      }
      return localObject8;
    }
    catch (IndexOutOfBoundsException paramString)
    {
      i = j;
      label2108:
      throw new URISyntaxException((String)localObject2, "illegal Intent URI format", i);
    }
  }
  
  public static void printIntentArgsHelp(PrintWriter paramPrintWriter, String paramString)
  {
    String[] arrayOfString = new String[48];
    arrayOfString[0] = "<INTENT> specifications include these flags and arguments:";
    arrayOfString[1] = "    [-a <ACTION>] [-d <DATA_URI>] [-t <MIME_TYPE>]";
    arrayOfString[2] = "    [-c <CATEGORY> [-c <CATEGORY>] ...]";
    arrayOfString[3] = "    [-n <COMPONENT_NAME>]";
    arrayOfString[4] = "    [-e|--es <EXTRA_KEY> <EXTRA_STRING_VALUE> ...]";
    arrayOfString[5] = "    [--esn <EXTRA_KEY> ...]";
    arrayOfString[6] = "    [--ez <EXTRA_KEY> <EXTRA_BOOLEAN_VALUE> ...]";
    arrayOfString[7] = "    [--ei <EXTRA_KEY> <EXTRA_INT_VALUE> ...]";
    arrayOfString[8] = "    [--el <EXTRA_KEY> <EXTRA_LONG_VALUE> ...]";
    arrayOfString[9] = "    [--ef <EXTRA_KEY> <EXTRA_FLOAT_VALUE> ...]";
    arrayOfString[10] = "    [--eu <EXTRA_KEY> <EXTRA_URI_VALUE> ...]";
    arrayOfString[11] = "    [--ecn <EXTRA_KEY> <EXTRA_COMPONENT_NAME_VALUE>]";
    arrayOfString[12] = "    [--eia <EXTRA_KEY> <EXTRA_INT_VALUE>[,<EXTRA_INT_VALUE...]]";
    arrayOfString[13] = "        (mutiple extras passed as Integer[])";
    arrayOfString[14] = "    [--eial <EXTRA_KEY> <EXTRA_INT_VALUE>[,<EXTRA_INT_VALUE...]]";
    arrayOfString[15] = "        (mutiple extras passed as List<Integer>)";
    arrayOfString[16] = "    [--ela <EXTRA_KEY> <EXTRA_LONG_VALUE>[,<EXTRA_LONG_VALUE...]]";
    arrayOfString[17] = "        (mutiple extras passed as Long[])";
    arrayOfString[18] = "    [--elal <EXTRA_KEY> <EXTRA_LONG_VALUE>[,<EXTRA_LONG_VALUE...]]";
    arrayOfString[19] = "        (mutiple extras passed as List<Long>)";
    arrayOfString[20] = "    [--efa <EXTRA_KEY> <EXTRA_FLOAT_VALUE>[,<EXTRA_FLOAT_VALUE...]]";
    arrayOfString[21] = "        (mutiple extras passed as Float[])";
    arrayOfString[22] = "    [--efal <EXTRA_KEY> <EXTRA_FLOAT_VALUE>[,<EXTRA_FLOAT_VALUE...]]";
    arrayOfString[23] = "        (mutiple extras passed as List<Float>)";
    arrayOfString[24] = "    [--esa <EXTRA_KEY> <EXTRA_STRING_VALUE>[,<EXTRA_STRING_VALUE...]]";
    arrayOfString[25] = "        (mutiple extras passed as String[]; to embed a comma into a string,";
    arrayOfString[26] = "         escape it using \"\\,\")";
    arrayOfString[27] = "    [--esal <EXTRA_KEY> <EXTRA_STRING_VALUE>[,<EXTRA_STRING_VALUE...]]";
    arrayOfString[28] = "        (mutiple extras passed as List<String>; to embed a comma into a string,";
    arrayOfString[29] = "         escape it using \"\\,\")";
    arrayOfString[30] = "    [-f <FLAG>]";
    arrayOfString[31] = "    [--grant-read-uri-permission] [--grant-write-uri-permission]";
    arrayOfString[32] = "    [--grant-persistable-uri-permission] [--grant-prefix-uri-permission]";
    arrayOfString[33] = "    [--debug-log-resolution] [--exclude-stopped-packages]";
    arrayOfString[34] = "    [--include-stopped-packages]";
    arrayOfString[35] = "    [--activity-brought-to-front] [--activity-clear-top]";
    arrayOfString[36] = "    [--activity-clear-when-task-reset] [--activity-exclude-from-recents]";
    arrayOfString[37] = "    [--activity-launched-from-history] [--activity-multiple-task]";
    arrayOfString[38] = "    [--activity-no-animation] [--activity-no-history]";
    arrayOfString[39] = "    [--activity-no-user-action] [--activity-previous-is-top]";
    arrayOfString[40] = "    [--activity-reorder-to-front] [--activity-reset-task-if-needed]";
    arrayOfString[41] = "    [--activity-single-top] [--activity-clear-task]";
    arrayOfString[42] = "    [--activity-task-on-home] [--activity-match-external]";
    arrayOfString[43] = "    [--receiver-registered-only] [--receiver-replace-pending]";
    arrayOfString[44] = "    [--receiver-foreground] [--receiver-no-abort]";
    arrayOfString[45] = "    [--receiver-include-background]";
    arrayOfString[46] = "    [--selector]";
    arrayOfString[47] = "    [<URI> | <PACKAGE> | <COMPONENT>]";
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = arrayOfString[j];
      paramPrintWriter.print(paramString);
      paramPrintWriter.println(str);
    }
  }
  
  public static Intent restoreFromXml(XmlPullParser paramXmlPullParser)
    throws IOException, XmlPullParserException
  {
    Intent localIntent = new Intent();
    int i = paramXmlPullParser.getDepth();
    String str;
    Object localObject;
    for (int j = paramXmlPullParser.getAttributeCount() - 1; j >= 0; j--)
    {
      str = paramXmlPullParser.getAttributeName(j);
      localObject = paramXmlPullParser.getAttributeValue(j);
      if ("action".equals(str))
      {
        localIntent.setAction((String)localObject);
      }
      else if ("data".equals(str))
      {
        localIntent.setData(Uri.parse((String)localObject));
      }
      else if ("type".equals(str))
      {
        localIntent.setType((String)localObject);
      }
      else if ("component".equals(str))
      {
        localIntent.setComponent(ComponentName.unflattenFromString((String)localObject));
      }
      else if ("flags".equals(str))
      {
        localIntent.setFlags(Integer.parseInt((String)localObject, 16));
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("restoreFromXml: unknown attribute=");
        ((StringBuilder)localObject).append(str);
        Log.e("Intent", ((StringBuilder)localObject).toString());
      }
    }
    for (;;)
    {
      j = paramXmlPullParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() >= i))) {
        break;
      }
      if (j == 2)
      {
        str = paramXmlPullParser.getName();
        if ("categories".equals(str))
        {
          for (j = paramXmlPullParser.getAttributeCount() - 1; j >= 0; j--) {
            localIntent.addCategory(paramXmlPullParser.getAttributeValue(j));
          }
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("restoreFromXml: unknown name=");
          ((StringBuilder)localObject).append(str);
          Log.w("Intent", ((StringBuilder)localObject).toString());
          XmlUtils.skipCurrentTag(paramXmlPullParser);
        }
      }
    }
    return localIntent;
  }
  
  private void toUriFragment(StringBuilder paramStringBuilder, String paramString1, String paramString2, String paramString3, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    toUriInner(localStringBuilder, paramString1, paramString2, paramString3, paramInt);
    if (mSelector != null)
    {
      localStringBuilder.append("SEL;");
      paramString2 = mSelector;
      if (mSelector.mData != null) {}
      for (paramString1 = mSelector.mData.getScheme();; paramString1 = null) {
        break;
      }
      paramString2.toUriInner(localStringBuilder, paramString1, null, null, paramInt);
    }
    if (localStringBuilder.length() > 0)
    {
      paramStringBuilder.append("#Intent;");
      paramStringBuilder.append(localStringBuilder);
      paramStringBuilder.append("end");
    }
  }
  
  private void toUriInner(StringBuilder paramStringBuilder, String paramString1, String paramString2, String paramString3, int paramInt)
  {
    if (paramString1 != null)
    {
      paramStringBuilder.append("scheme=");
      paramStringBuilder.append(paramString1);
      paramStringBuilder.append(';');
    }
    if ((mAction != null) && (!mAction.equals(paramString2)))
    {
      paramStringBuilder.append("action=");
      paramStringBuilder.append(Uri.encode(mAction));
      paramStringBuilder.append(';');
    }
    if (mCategories != null) {
      for (paramInt = 0; paramInt < mCategories.size(); paramInt++)
      {
        paramStringBuilder.append("category=");
        paramStringBuilder.append(Uri.encode((String)mCategories.valueAt(paramInt)));
        paramStringBuilder.append(';');
      }
    }
    if (mType != null)
    {
      paramStringBuilder.append("type=");
      paramStringBuilder.append(Uri.encode(mType, "/"));
      paramStringBuilder.append(';');
    }
    if (mFlags != 0)
    {
      paramStringBuilder.append("launchFlags=0x");
      paramStringBuilder.append(Integer.toHexString(mFlags));
      paramStringBuilder.append(';');
    }
    if ((mPackage != null) && (!mPackage.equals(paramString3)))
    {
      paramStringBuilder.append("package=");
      paramStringBuilder.append(Uri.encode(mPackage));
      paramStringBuilder.append(';');
    }
    if (mComponent != null)
    {
      paramStringBuilder.append("component=");
      paramStringBuilder.append(Uri.encode(mComponent.flattenToShortString(), "/"));
      paramStringBuilder.append(';');
    }
    if (mSourceBounds != null)
    {
      paramStringBuilder.append("sourceBounds=");
      paramStringBuilder.append(Uri.encode(mSourceBounds.flattenToString()));
      paramStringBuilder.append(';');
    }
    if (mExtras != null)
    {
      paramString3 = mExtras.keySet().iterator();
      while (paramString3.hasNext())
      {
        paramString2 = (String)paramString3.next();
        paramString1 = mExtras.get(paramString2);
        int i;
        if ((paramString1 instanceof String))
        {
          paramInt = 83;
          i = paramInt;
        }
        else if ((paramString1 instanceof Boolean))
        {
          paramInt = 66;
          i = paramInt;
        }
        else if ((paramString1 instanceof Byte))
        {
          paramInt = 98;
          i = paramInt;
        }
        else if ((paramString1 instanceof Character))
        {
          paramInt = 99;
          i = paramInt;
        }
        else if ((paramString1 instanceof Double))
        {
          paramInt = 100;
          i = paramInt;
        }
        else if ((paramString1 instanceof Float))
        {
          paramInt = 102;
          i = paramInt;
        }
        else if ((paramString1 instanceof Integer))
        {
          paramInt = 105;
          i = paramInt;
        }
        else if ((paramString1 instanceof Long))
        {
          paramInt = 108;
          i = paramInt;
        }
        else if ((paramString1 instanceof Short))
        {
          paramInt = 115;
          i = paramInt;
        }
        else
        {
          paramInt = 0;
          i = paramInt;
        }
        if (i != 0)
        {
          paramStringBuilder.append(i);
          paramStringBuilder.append('.');
          paramStringBuilder.append(Uri.encode(paramString2));
          paramStringBuilder.append('=');
          paramStringBuilder.append(Uri.encode(paramString1.toString()));
          paramStringBuilder.append(';');
        }
      }
    }
  }
  
  public Intent addCategory(String paramString)
  {
    if (mCategories == null) {
      mCategories = new ArraySet();
    }
    mCategories.add(paramString.intern());
    return this;
  }
  
  public Intent addFlags(int paramInt)
  {
    mFlags |= paramInt;
    return this;
  }
  
  public boolean canStripForHistory()
  {
    boolean bool;
    if (((mExtras != null) && (mExtras.isParcelled())) || (mClipData != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Object clone()
  {
    return new Intent(this);
  }
  
  public Intent cloneFilter()
  {
    return new Intent(this, 1);
  }
  
  public int describeContents()
  {
    int i;
    if (mExtras != null) {
      i = mExtras.describeContents();
    } else {
      i = 0;
    }
    return i;
  }
  
  public int fillIn(Intent paramIntent, int paramInt)
  {
    int i = 0;
    int j = 0;
    int k = i;
    if (mAction != null) {
      if (mAction != null)
      {
        k = i;
        if ((paramInt & 0x1) == 0) {}
      }
      else
      {
        mAction = mAction;
        k = 0x0 | 0x1;
      }
    }
    if (mData == null)
    {
      m = k;
      i = j;
      if (mType == null) {}
    }
    else if ((mData != null) || (mType != null))
    {
      m = k;
      i = j;
      if ((paramInt & 0x2) == 0) {}
    }
    else
    {
      mData = mData;
      mType = mType;
      m = k | 0x2;
      i = 1;
    }
    j = m;
    if (mCategories != null) {
      if (mCategories != null)
      {
        j = m;
        if ((paramInt & 0x4) == 0) {}
      }
      else
      {
        if (mCategories != null) {
          mCategories = new ArraySet(mCategories);
        }
        j = m | 0x4;
      }
    }
    k = j;
    if (mPackage != null) {
      if (mPackage != null)
      {
        k = j;
        if ((paramInt & 0x10) == 0) {}
      }
      else
      {
        k = j;
        if (mSelector == null)
        {
          mPackage = mPackage;
          k = j | 0x10;
        }
      }
    }
    int m = k;
    if (mSelector != null)
    {
      m = k;
      if ((paramInt & 0x40) != 0)
      {
        m = k;
        if (mPackage == null)
        {
          mSelector = new Intent(mSelector);
          mPackage = null;
          m = k | 0x40;
        }
      }
    }
    j = m;
    k = i;
    if (mClipData != null) {
      if (mClipData != null)
      {
        j = m;
        k = i;
        if ((paramInt & 0x80) == 0) {}
      }
      else
      {
        mClipData = mClipData;
        j = m | 0x80;
        k = 1;
      }
    }
    i = j;
    if (mComponent != null)
    {
      i = j;
      if ((paramInt & 0x8) != 0)
      {
        mComponent = mComponent;
        i = j | 0x8;
      }
    }
    mFlags |= mFlags;
    m = i;
    if (mSourceBounds != null) {
      if (mSourceBounds != null)
      {
        m = i;
        if ((paramInt & 0x20) == 0) {}
      }
      else
      {
        mSourceBounds = new Rect(mSourceBounds);
        m = i | 0x20;
      }
    }
    if (mExtras == null)
    {
      paramInt = k;
      if (mExtras != null)
      {
        mExtras = new Bundle(mExtras);
        paramInt = 1;
      }
    }
    else
    {
      paramInt = k;
      if (mExtras != null) {
        try
        {
          Bundle localBundle = new android/os/Bundle;
          localBundle.<init>(mExtras);
          localBundle.putAll(mExtras);
          mExtras = localBundle;
          paramInt = 1;
        }
        catch (RuntimeException localRuntimeException)
        {
          Log.w("Intent", "Failure filling in extras", localRuntimeException);
          paramInt = k;
        }
      }
    }
    if ((paramInt != 0) && (mContentUserHint == -2) && (mContentUserHint != -2)) {
      mContentUserHint = mContentUserHint;
    }
    return m;
  }
  
  public boolean filterEquals(Intent paramIntent)
  {
    if (paramIntent == null) {
      return false;
    }
    if (!Objects.equals(mAction, mAction)) {
      return false;
    }
    if (!Objects.equals(mData, mData)) {
      return false;
    }
    if (!Objects.equals(mType, mType)) {
      return false;
    }
    if (!Objects.equals(mPackage, mPackage)) {
      return false;
    }
    if (!Objects.equals(mComponent, mComponent)) {
      return false;
    }
    return Objects.equals(mCategories, mCategories);
  }
  
  public int filterHashCode()
  {
    int i = 0;
    if (mAction != null) {
      i = 0 + mAction.hashCode();
    }
    int j = i;
    if (mData != null) {
      j = i + mData.hashCode();
    }
    i = j;
    if (mType != null) {
      i = j + mType.hashCode();
    }
    j = i;
    if (mPackage != null) {
      j = i + mPackage.hashCode();
    }
    i = j;
    if (mComponent != null) {
      i = j + mComponent.hashCode();
    }
    j = i;
    if (mCategories != null) {
      j = i + mCategories.hashCode();
    }
    return j;
  }
  
  public void fixUris(int paramInt)
  {
    Object localObject = getData();
    if (localObject != null) {
      mData = ContentProvider.maybeAddUserId((Uri)localObject, paramInt);
    }
    if (mClipData != null) {
      mClipData.fixUris(paramInt);
    }
    localObject = getAction();
    if ("android.intent.action.SEND".equals(localObject))
    {
      localObject = (Uri)getParcelableExtra("android.intent.extra.STREAM");
      if (localObject != null) {
        putExtra("android.intent.extra.STREAM", ContentProvider.maybeAddUserId((Uri)localObject, paramInt));
      }
    }
    else if ("android.intent.action.SEND_MULTIPLE".equals(localObject))
    {
      ArrayList localArrayList = getParcelableArrayListExtra("android.intent.extra.STREAM");
      if (localArrayList != null)
      {
        localObject = new ArrayList();
        for (int i = 0; i < localArrayList.size(); i++) {
          ((ArrayList)localObject).add(ContentProvider.maybeAddUserId((Uri)localArrayList.get(i), paramInt));
        }
        putParcelableArrayListExtra("android.intent.extra.STREAM", (ArrayList)localObject);
      }
    }
    else if (("android.media.action.IMAGE_CAPTURE".equals(localObject)) || ("android.media.action.IMAGE_CAPTURE_SECURE".equals(localObject)) || ("android.media.action.VIDEO_CAPTURE".equals(localObject)))
    {
      localObject = (Uri)getParcelableExtra("output");
      if (localObject != null) {
        putExtra("output", ContentProvider.maybeAddUserId((Uri)localObject, paramInt));
      }
    }
  }
  
  public String getAction()
  {
    return mAction;
  }
  
  public boolean[] getBooleanArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getBooleanArray(paramString);
    }
    return paramString;
  }
  
  public boolean getBooleanExtra(String paramString, boolean paramBoolean)
  {
    if (mExtras != null) {
      paramBoolean = mExtras.getBoolean(paramString, paramBoolean);
    }
    return paramBoolean;
  }
  
  public Bundle getBundleExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getBundle(paramString);
    }
    return paramString;
  }
  
  public byte[] getByteArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getByteArray(paramString);
    }
    return paramString;
  }
  
  public byte getByteExtra(String paramString, byte paramByte)
  {
    byte b;
    if (mExtras == null)
    {
      b = paramByte;
      paramByte = b;
    }
    else
    {
      b = mExtras.getByte(paramString, paramByte).byteValue();
      paramByte = b;
    }
    return paramByte;
  }
  
  public Set<String> getCategories()
  {
    return mCategories;
  }
  
  public char[] getCharArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getCharArray(paramString);
    }
    return paramString;
  }
  
  public char getCharExtra(String paramString, char paramChar)
  {
    char c;
    if (mExtras == null)
    {
      c = paramChar;
      paramChar = c;
    }
    else
    {
      c = mExtras.getChar(paramString, paramChar);
      paramChar = c;
    }
    return paramChar;
  }
  
  public CharSequence[] getCharSequenceArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getCharSequenceArray(paramString);
    }
    return paramString;
  }
  
  public ArrayList<CharSequence> getCharSequenceArrayListExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getCharSequenceArrayList(paramString);
    }
    return paramString;
  }
  
  public CharSequence getCharSequenceExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getCharSequence(paramString);
    }
    return paramString;
  }
  
  public ClipData getClipData()
  {
    return mClipData;
  }
  
  public ComponentName getComponent()
  {
    return mComponent;
  }
  
  public int getContentUserHint()
  {
    return mContentUserHint;
  }
  
  public Uri getData()
  {
    return mData;
  }
  
  public String getDataString()
  {
    String str;
    if (mData != null) {
      str = mData.toString();
    } else {
      str = null;
    }
    return str;
  }
  
  public double[] getDoubleArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getDoubleArray(paramString);
    }
    return paramString;
  }
  
  public double getDoubleExtra(String paramString, double paramDouble)
  {
    if (mExtras != null) {
      paramDouble = mExtras.getDouble(paramString, paramDouble);
    }
    return paramDouble;
  }
  
  @Deprecated
  public Object getExtra(String paramString)
  {
    return getExtra(paramString, null);
  }
  
  @Deprecated
  public Object getExtra(String paramString, Object paramObject)
  {
    Object localObject = paramObject;
    paramObject = localObject;
    if (mExtras != null)
    {
      paramString = mExtras.get(paramString);
      paramObject = localObject;
      if (paramString != null) {
        paramObject = paramString;
      }
    }
    return paramObject;
  }
  
  public Bundle getExtras()
  {
    Bundle localBundle;
    if (mExtras != null) {
      localBundle = new Bundle(mExtras);
    } else {
      localBundle = null;
    }
    return localBundle;
  }
  
  public int getFlags()
  {
    return mFlags;
  }
  
  public float[] getFloatArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getFloatArray(paramString);
    }
    return paramString;
  }
  
  public float getFloatExtra(String paramString, float paramFloat)
  {
    if (mExtras != null) {
      paramFloat = mExtras.getFloat(paramString, paramFloat);
    }
    return paramFloat;
  }
  
  @Deprecated
  public IBinder getIBinderExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getIBinder(paramString);
    }
    return paramString;
  }
  
  public int[] getIntArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getIntArray(paramString);
    }
    return paramString;
  }
  
  public int getIntExtra(String paramString, int paramInt)
  {
    if (mExtras != null) {
      paramInt = mExtras.getInt(paramString, paramInt);
    }
    return paramInt;
  }
  
  public ArrayList<Integer> getIntegerArrayListExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getIntegerArrayList(paramString);
    }
    return paramString;
  }
  
  public String getLaunchToken()
  {
    return mLaunchToken;
  }
  
  public long[] getLongArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getLongArray(paramString);
    }
    return paramString;
  }
  
  public long getLongExtra(String paramString, long paramLong)
  {
    if (mExtras != null) {
      paramLong = mExtras.getLong(paramString, paramLong);
    }
    return paramLong;
  }
  
  public String getPackage()
  {
    return mPackage;
  }
  
  public Parcelable[] getParcelableArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getParcelableArray(paramString);
    }
    return paramString;
  }
  
  public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getParcelableArrayList(paramString);
    }
    return paramString;
  }
  
  public <T extends Parcelable> T getParcelableExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getParcelable(paramString);
    }
    return paramString;
  }
  
  public String getScheme()
  {
    String str;
    if (mData != null) {
      str = mData.getScheme();
    } else {
      str = null;
    }
    return str;
  }
  
  public Intent getSelector()
  {
    return mSelector;
  }
  
  public Serializable getSerializableExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getSerializable(paramString);
    }
    return paramString;
  }
  
  public short[] getShortArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getShortArray(paramString);
    }
    return paramString;
  }
  
  public short getShortExtra(String paramString, short paramShort)
  {
    short s;
    if (mExtras == null)
    {
      s = paramShort;
      paramShort = s;
    }
    else
    {
      s = mExtras.getShort(paramString, paramShort);
      paramShort = s;
    }
    return paramShort;
  }
  
  public Rect getSourceBounds()
  {
    return mSourceBounds;
  }
  
  public String[] getStringArrayExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getStringArray(paramString);
    }
    return paramString;
  }
  
  public ArrayList<String> getStringArrayListExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getStringArrayList(paramString);
    }
    return paramString;
  }
  
  public String getStringExtra(String paramString)
  {
    if (mExtras == null) {
      paramString = null;
    } else {
      paramString = mExtras.getString(paramString);
    }
    return paramString;
  }
  
  public String getType()
  {
    return mType;
  }
  
  public boolean hasCategory(String paramString)
  {
    boolean bool;
    if ((mCategories != null) && (mCategories.contains(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasExtra(String paramString)
  {
    boolean bool;
    if ((mExtras != null) && (mExtras.containsKey(paramString))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasFileDescriptors()
  {
    boolean bool;
    if ((mExtras != null) && (mExtras.hasFileDescriptors())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasWebURI()
  {
    Object localObject = getData();
    boolean bool = false;
    if (localObject == null) {
      return false;
    }
    localObject = getScheme();
    if (TextUtils.isEmpty((CharSequence)localObject)) {
      return false;
    }
    if ((!((String)localObject).equals("http")) && (!((String)localObject).equals("https"))) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public boolean isDocument()
  {
    boolean bool;
    if ((mFlags & 0x80000) == 524288) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isExcludingStopped()
  {
    boolean bool;
    if ((mFlags & 0x30) == 16) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isWebIntent()
  {
    boolean bool;
    if (("android.intent.action.VIEW".equals(mAction)) && (hasWebURI())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public Intent maybeStripForHistory()
  {
    if (!canStripForHistory()) {
      return this;
    }
    return new Intent(this, 2);
  }
  
  public boolean migrateExtraStreamToClipData()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 1338	android/content/Intent:mExtras	Landroid/os/Bundle;
    //   4: astore_1
    //   5: iconst_0
    //   6: istore_2
    //   7: aload_1
    //   8: ifnull +15 -> 23
    //   11: aload_0
    //   12: getfield 1338	android/content/Intent:mExtras	Landroid/os/Bundle;
    //   15: invokevirtual 2230	android/os/Bundle:isParcelled	()Z
    //   18: ifeq +5 -> 23
    //   21: iconst_0
    //   22: ireturn
    //   23: aload_0
    //   24: invokevirtual 1391	android/content/Intent:getClipData	()Landroid/content/ClipData;
    //   27: ifnull +5 -> 32
    //   30: iconst_0
    //   31: ireturn
    //   32: aload_0
    //   33: invokevirtual 1866	android/content/Intent:getAction	()Ljava/lang/String;
    //   36: astore_1
    //   37: ldc 113
    //   39: aload_1
    //   40: invokevirtual 1650	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   43: ifeq +137 -> 180
    //   46: iconst_0
    //   47: istore_3
    //   48: iconst_0
    //   49: istore 4
    //   51: aload_0
    //   52: ldc_w 932
    //   55: invokevirtual 2267	android/content/Intent:getParcelableExtra	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   58: checkcast 2	android/content/Intent
    //   61: astore_1
    //   62: iload 4
    //   64: istore 5
    //   66: aload_1
    //   67: ifnull +16 -> 83
    //   70: aload_1
    //   71: invokevirtual 2480	android/content/Intent:migrateExtraStreamToClipData	()Z
    //   74: istore 5
    //   76: iload 4
    //   78: iload 5
    //   80: ior
    //   81: istore 5
    //   83: goto +7 -> 90
    //   86: astore_1
    //   87: iload_3
    //   88: istore 5
    //   90: iload 5
    //   92: istore 4
    //   94: aload_0
    //   95: ldc_w 902
    //   98: invokevirtual 2482	android/content/Intent:getParcelableArrayExtra	(Ljava/lang/String;)[Landroid/os/Parcelable;
    //   101: astore 6
    //   103: iload 5
    //   105: istore_3
    //   106: aload 6
    //   108: ifnull +62 -> 170
    //   111: iload 5
    //   113: istore_3
    //   114: iload 5
    //   116: istore 4
    //   118: iload_2
    //   119: aload 6
    //   121: arraylength
    //   122: if_icmpge +48 -> 170
    //   125: iload 5
    //   127: istore 4
    //   129: aload 6
    //   131: iload_2
    //   132: aaload
    //   133: checkcast 2	android/content/Intent
    //   136: astore_1
    //   137: iload 5
    //   139: istore 4
    //   141: aload_1
    //   142: ifnull +18 -> 160
    //   145: iload 5
    //   147: istore 4
    //   149: aload_1
    //   150: invokevirtual 2480	android/content/Intent:migrateExtraStreamToClipData	()Z
    //   153: istore_3
    //   154: iload 5
    //   156: iload_3
    //   157: ior
    //   158: istore 4
    //   160: iinc 2 1
    //   163: iload 4
    //   165: istore 5
    //   167: goto -56 -> 111
    //   170: iload_3
    //   171: istore 4
    //   173: goto +4 -> 177
    //   176: astore_1
    //   177: iload 4
    //   179: ireturn
    //   180: ldc_w 468
    //   183: aload_1
    //   184: invokevirtual 1650	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   187: ifeq +108 -> 295
    //   190: aload_0
    //   191: ldc_w 1093
    //   194: invokevirtual 2267	android/content/Intent:getParcelableExtra	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   197: checkcast 1505	android/net/Uri
    //   200: astore 7
    //   202: aload_0
    //   203: ldc_w 1111
    //   206: invokevirtual 2484	android/content/Intent:getCharSequenceExtra	(Ljava/lang/String;)Ljava/lang/CharSequence;
    //   209: astore 6
    //   211: aload_0
    //   212: ldc_w 896
    //   215: invokevirtual 2486	android/content/Intent:getStringExtra	(Ljava/lang/String;)Ljava/lang/String;
    //   218: astore_1
    //   219: aload 7
    //   221: ifnonnull +12 -> 233
    //   224: aload 6
    //   226: ifnonnull +7 -> 233
    //   229: aload_1
    //   230: ifnull +62 -> 292
    //   233: new 1347	android/content/ClipData
    //   236: astore 8
    //   238: aload_0
    //   239: invokevirtual 1404	android/content/Intent:getType	()Ljava/lang/String;
    //   242: astore 9
    //   244: new 1397	android/content/ClipData$Item
    //   247: astore 10
    //   249: aload 10
    //   251: aload 6
    //   253: aload_1
    //   254: aconst_null
    //   255: aload 7
    //   257: invokespecial 1602	android/content/ClipData$Item:<init>	(Ljava/lang/CharSequence;Ljava/lang/String;Landroid/content/Intent;Landroid/net/Uri;)V
    //   260: aload 8
    //   262: aconst_null
    //   263: iconst_1
    //   264: anewarray 1406	java/lang/String
    //   267: dup
    //   268: iconst_0
    //   269: aload 9
    //   271: aastore
    //   272: aload 10
    //   274: invokespecial 1409	android/content/ClipData:<init>	(Ljava/lang/CharSequence;[Ljava/lang/String;Landroid/content/ClipData$Item;)V
    //   277: aload_0
    //   278: aload 8
    //   280: invokevirtual 1412	android/content/Intent:setClipData	(Landroid/content/ClipData;)V
    //   283: aload_0
    //   284: iconst_1
    //   285: invokevirtual 1416	android/content/Intent:addFlags	(I)Landroid/content/Intent;
    //   288: pop
    //   289: iconst_1
    //   290: ireturn
    //   291: astore_1
    //   292: goto +270 -> 562
    //   295: ldc_w 474
    //   298: aload_1
    //   299: invokevirtual 1650	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   302: ifeq +196 -> 498
    //   305: aload_0
    //   306: ldc_w 1093
    //   309: invokevirtual 2271	android/content/Intent:getParcelableArrayListExtra	(Ljava/lang/String;)Ljava/util/ArrayList;
    //   312: astore 9
    //   314: aload_0
    //   315: ldc_w 1111
    //   318: invokevirtual 2488	android/content/Intent:getCharSequenceArrayListExtra	(Ljava/lang/String;)Ljava/util/ArrayList;
    //   321: astore 7
    //   323: aload_0
    //   324: ldc_w 896
    //   327: invokevirtual 2490	android/content/Intent:getStringArrayListExtra	(Ljava/lang/String;)Ljava/util/ArrayList;
    //   330: astore 10
    //   332: iconst_m1
    //   333: istore 11
    //   335: aload 9
    //   337: ifnull +10 -> 347
    //   340: aload 9
    //   342: invokevirtual 2273	java/util/ArrayList:size	()I
    //   345: istore 11
    //   347: iload 11
    //   349: istore_2
    //   350: aload 7
    //   352: ifnull +26 -> 378
    //   355: iload 11
    //   357: iflt +15 -> 372
    //   360: iload 11
    //   362: aload 7
    //   364: invokevirtual 2273	java/util/ArrayList:size	()I
    //   367: if_icmpeq +5 -> 372
    //   370: iconst_0
    //   371: ireturn
    //   372: aload 7
    //   374: invokevirtual 2273	java/util/ArrayList:size	()I
    //   377: istore_2
    //   378: iload_2
    //   379: istore 11
    //   381: aload 10
    //   383: ifnull +25 -> 408
    //   386: iload_2
    //   387: iflt +14 -> 401
    //   390: iload_2
    //   391: aload 10
    //   393: invokevirtual 2273	java/util/ArrayList:size	()I
    //   396: if_icmpeq +5 -> 401
    //   399: iconst_0
    //   400: ireturn
    //   401: aload 10
    //   403: invokevirtual 2273	java/util/ArrayList:size	()I
    //   406: istore 11
    //   408: iload 11
    //   410: ifle +85 -> 495
    //   413: new 1347	android/content/ClipData
    //   416: astore 8
    //   418: aload_0
    //   419: invokevirtual 1404	android/content/Intent:getType	()Ljava/lang/String;
    //   422: astore_1
    //   423: aload 9
    //   425: aload 7
    //   427: aload 10
    //   429: iconst_0
    //   430: invokestatic 2492	android/content/Intent:makeClipItem	(Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;I)Landroid/content/ClipData$Item;
    //   433: astore 6
    //   435: aload 8
    //   437: aconst_null
    //   438: iconst_1
    //   439: anewarray 1406	java/lang/String
    //   442: dup
    //   443: iconst_0
    //   444: aload_1
    //   445: aastore
    //   446: aload 6
    //   448: invokespecial 1409	android/content/ClipData:<init>	(Ljava/lang/CharSequence;[Ljava/lang/String;Landroid/content/ClipData$Item;)V
    //   451: iconst_1
    //   452: istore_2
    //   453: iload_2
    //   454: iload 11
    //   456: if_icmpge +24 -> 480
    //   459: aload 8
    //   461: aload 9
    //   463: aload 7
    //   465: aload 10
    //   467: iload_2
    //   468: invokestatic 2492	android/content/Intent:makeClipItem	(Ljava/util/ArrayList;Ljava/util/ArrayList;Ljava/util/ArrayList;I)Landroid/content/ClipData$Item;
    //   471: invokevirtual 2496	android/content/ClipData:addItem	(Landroid/content/ClipData$Item;)V
    //   474: iinc 2 1
    //   477: goto -24 -> 453
    //   480: aload_0
    //   481: aload 8
    //   483: invokevirtual 1412	android/content/Intent:setClipData	(Landroid/content/ClipData;)V
    //   486: aload_0
    //   487: iconst_1
    //   488: invokevirtual 1416	android/content/Intent:addFlags	(I)Landroid/content/Intent;
    //   491: pop
    //   492: iconst_1
    //   493: ireturn
    //   494: astore_1
    //   495: goto +67 -> 562
    //   498: ldc_w 2279
    //   501: aload_1
    //   502: invokevirtual 1650	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   505: ifne +23 -> 528
    //   508: ldc_w 2281
    //   511: aload_1
    //   512: invokevirtual 1650	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   515: ifne +13 -> 528
    //   518: ldc_w 2283
    //   521: aload_1
    //   522: invokevirtual 1650	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   525: ifeq +37 -> 562
    //   528: aload_0
    //   529: ldc_w 2285
    //   532: invokevirtual 2267	android/content/Intent:getParcelableExtra	(Ljava/lang/String;)Landroid/os/Parcelable;
    //   535: checkcast 1505	android/net/Uri
    //   538: astore_1
    //   539: aload_1
    //   540: ifnull +22 -> 562
    //   543: aload_0
    //   544: ldc_w 1979
    //   547: aload_1
    //   548: invokestatic 2500	android/content/ClipData:newRawUri	(Ljava/lang/CharSequence;Landroid/net/Uri;)Landroid/content/ClipData;
    //   551: invokevirtual 1412	android/content/Intent:setClipData	(Landroid/content/ClipData;)V
    //   554: aload_0
    //   555: iconst_3
    //   556: invokevirtual 1416	android/content/Intent:addFlags	(I)Landroid/content/Intent;
    //   559: pop
    //   560: iconst_1
    //   561: ireturn
    //   562: iconst_0
    //   563: ireturn
    //   564: astore_1
    //   565: iconst_0
    //   566: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	567	0	this	Intent
    //   4	67	1	localObject1	Object
    //   86	1	1	localClassCastException1	ClassCastException
    //   136	14	1	localIntent	Intent
    //   176	8	1	localClassCastException2	ClassCastException
    //   218	36	1	str1	String
    //   291	8	1	localClassCastException3	ClassCastException
    //   422	23	1	str2	String
    //   494	28	1	localClassCastException4	ClassCastException
    //   538	10	1	localUri	Uri
    //   564	1	1	localClassCastException5	ClassCastException
    //   6	469	2	i	int
    //   47	124	3	bool1	boolean
    //   49	129	4	bool2	boolean
    //   64	102	5	bool3	boolean
    //   101	346	6	localObject2	Object
    //   200	264	7	localObject3	Object
    //   236	246	8	localClipData	ClipData
    //   242	220	9	localObject4	Object
    //   247	219	10	localObject5	Object
    //   333	124	11	j	int
    // Exception table:
    //   from	to	target	type
    //   51	62	86	java/lang/ClassCastException
    //   70	76	86	java/lang/ClassCastException
    //   94	103	176	java/lang/ClassCastException
    //   118	125	176	java/lang/ClassCastException
    //   129	137	176	java/lang/ClassCastException
    //   149	154	176	java/lang/ClassCastException
    //   190	219	291	java/lang/ClassCastException
    //   233	289	291	java/lang/ClassCastException
    //   305	332	494	java/lang/ClassCastException
    //   340	347	494	java/lang/ClassCastException
    //   360	370	494	java/lang/ClassCastException
    //   372	378	494	java/lang/ClassCastException
    //   390	399	494	java/lang/ClassCastException
    //   401	408	494	java/lang/ClassCastException
    //   413	451	494	java/lang/ClassCastException
    //   459	474	494	java/lang/ClassCastException
    //   480	492	494	java/lang/ClassCastException
    //   528	539	564	java/lang/ClassCastException
  }
  
  public void prepareToEnterProcess()
  {
    setDefusable(true);
    if (mSelector != null) {
      mSelector.prepareToEnterProcess();
    }
    if (mClipData != null) {
      mClipData.prepareToEnterProcess();
    }
    if ((mContentUserHint != -2) && (UserHandle.getAppId(Process.myUid()) != 1000))
    {
      fixUris(mContentUserHint);
      mContentUserHint = -2;
    }
  }
  
  public void prepareToLeaveProcess(Context paramContext)
  {
    boolean bool;
    if ((mComponent != null) && (Objects.equals(mComponent.getPackageName(), paramContext.getPackageName()))) {
      bool = false;
    } else {
      bool = true;
    }
    prepareToLeaveProcess(bool);
  }
  
  public void prepareToLeaveProcess(boolean paramBoolean)
  {
    int i = 0;
    setAllowFds(false);
    if (mSelector != null) {
      mSelector.prepareToLeaveProcess(paramBoolean);
    }
    if (mClipData != null) {
      mClipData.prepareToLeaveProcess(paramBoolean, getFlags());
    }
    Object localObject;
    if ((mExtras != null) && (!mExtras.isParcelled()))
    {
      localObject = mExtras.get("android.intent.extra.INTENT");
      if ((localObject instanceof Intent)) {
        ((Intent)localObject).prepareToLeaveProcess(paramBoolean);
      }
    }
    int j;
    if ((mAction != null) && (mData != null) && (StrictMode.vmFileUriExposureEnabled()) && (paramBoolean))
    {
      localObject = mAction;
      switch (((String)localObject).hashCode())
      {
      default: 
        break;
      case 2045140818: 
        if (((String)localObject).equals("android.intent.action.MEDIA_BAD_REMOVAL")) {
          j = 7;
        }
        break;
      case 1964681210: 
        if (((String)localObject).equals("android.intent.action.MEDIA_CHECKING")) {
          j = 2;
        }
        break;
      case 1920444806: 
        if (((String)localObject).equals("android.intent.action.PACKAGE_VERIFIED")) {
          j = 15;
        }
        break;
      case 1431947322: 
        if (((String)localObject).equals("android.intent.action.MEDIA_UNMOUNTABLE")) {
          j = 8;
        }
        break;
      case 1412829408: 
        if (((String)localObject).equals("android.intent.action.MEDIA_SCANNER_STARTED")) {
          j = 10;
        }
        break;
      case 852070077: 
        if (((String)localObject).equals("android.intent.action.MEDIA_SCANNER_SCAN_FILE")) {
          j = 12;
        }
        break;
      case 582421979: 
        if (((String)localObject).equals("android.intent.action.PACKAGE_NEEDS_VERIFICATION")) {
          j = 13;
        }
        break;
      case 410719838: 
        if (((String)localObject).equals("android.intent.action.MEDIA_UNSHARED")) {
          j = 6;
        }
        break;
      case 257177710: 
        if (((String)localObject).equals("android.intent.action.MEDIA_NOFS")) {
          j = 3;
        }
        break;
      case -625887599: 
        if (((String)localObject).equals("android.intent.action.MEDIA_EJECT")) {
          j = 9;
        }
        break;
      case -808646005: 
        if (((String)localObject).equals("com.qualcomm.qti.intent.action.PACKAGE_NEEDS_OPTIONAL_VERIFICATION")) {
          j = 14;
        }
        break;
      case -963871873: 
        if (((String)localObject).equals("android.intent.action.MEDIA_UNMOUNTED")) {
          j = 1;
        }
        break;
      case -1142424621: 
        if (((String)localObject).equals("android.intent.action.MEDIA_SCANNER_FINISHED")) {
          j = 11;
        }
        break;
      case -1514214344: 
        if (((String)localObject).equals("android.intent.action.MEDIA_MOUNTED")) {
          j = 4;
        }
        break;
      case -1665311200: 
        if (((String)localObject).equals("android.intent.action.MEDIA_REMOVED")) {
          j = 0;
        }
        break;
      case -1823790459: 
        if (((String)localObject).equals("android.intent.action.MEDIA_SHARED")) {
          j = 5;
        }
        break;
      }
      j = -1;
      switch (j)
      {
      default: 
        mData.checkFileUriExposed("Intent.getData()");
        break;
      }
    }
    if ((mAction != null) && (mData != null) && (StrictMode.vmContentUriWithoutPermissionEnabled()) && (paramBoolean))
    {
      localObject = mAction;
      j = ((String)localObject).hashCode();
      if (j != -577088908)
      {
        if ((j == 1662413067) && (((String)localObject).equals("android.intent.action.PROVIDER_CHANGED")))
        {
          j = i;
          break label710;
        }
      }
      else if (((String)localObject).equals("android.provider.action.QUICK_CONTACT"))
      {
        j = 1;
        break label710;
      }
      j = -1;
      switch (j)
      {
      default: 
        label710:
        mData.checkContentUriWithoutPermission("Intent.getData()", getFlags());
        break;
      }
    }
  }
  
  public void prepareToLeaveUser(int paramInt)
  {
    if (mContentUserHint == -2) {
      mContentUserHint = paramInt;
    }
  }
  
  public Intent putCharSequenceArrayListExtra(String paramString, ArrayList<CharSequence> paramArrayList)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putCharSequenceArrayList(paramString, paramArrayList);
    return this;
  }
  
  public Intent putExtra(String paramString, byte paramByte)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putByte(paramString, paramByte);
    return this;
  }
  
  public Intent putExtra(String paramString, char paramChar)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putChar(paramString, paramChar);
    return this;
  }
  
  public Intent putExtra(String paramString, double paramDouble)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putDouble(paramString, paramDouble);
    return this;
  }
  
  public Intent putExtra(String paramString, float paramFloat)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putFloat(paramString, paramFloat);
    return this;
  }
  
  public Intent putExtra(String paramString, int paramInt)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putInt(paramString, paramInt);
    return this;
  }
  
  public Intent putExtra(String paramString, long paramLong)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putLong(paramString, paramLong);
    return this;
  }
  
  public Intent putExtra(String paramString, Bundle paramBundle)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putBundle(paramString, paramBundle);
    return this;
  }
  
  @Deprecated
  public Intent putExtra(String paramString, IBinder paramIBinder)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putIBinder(paramString, paramIBinder);
    return this;
  }
  
  public Intent putExtra(String paramString, Parcelable paramParcelable)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putParcelable(paramString, paramParcelable);
    return this;
  }
  
  public Intent putExtra(String paramString, Serializable paramSerializable)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putSerializable(paramString, paramSerializable);
    return this;
  }
  
  public Intent putExtra(String paramString, CharSequence paramCharSequence)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putCharSequence(paramString, paramCharSequence);
    return this;
  }
  
  public Intent putExtra(String paramString1, String paramString2)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putString(paramString1, paramString2);
    return this;
  }
  
  public Intent putExtra(String paramString, short paramShort)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putShort(paramString, paramShort);
    return this;
  }
  
  public Intent putExtra(String paramString, boolean paramBoolean)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putBoolean(paramString, paramBoolean);
    return this;
  }
  
  public Intent putExtra(String paramString, byte[] paramArrayOfByte)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putByteArray(paramString, paramArrayOfByte);
    return this;
  }
  
  public Intent putExtra(String paramString, char[] paramArrayOfChar)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putCharArray(paramString, paramArrayOfChar);
    return this;
  }
  
  public Intent putExtra(String paramString, double[] paramArrayOfDouble)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putDoubleArray(paramString, paramArrayOfDouble);
    return this;
  }
  
  public Intent putExtra(String paramString, float[] paramArrayOfFloat)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putFloatArray(paramString, paramArrayOfFloat);
    return this;
  }
  
  public Intent putExtra(String paramString, int[] paramArrayOfInt)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putIntArray(paramString, paramArrayOfInt);
    return this;
  }
  
  public Intent putExtra(String paramString, long[] paramArrayOfLong)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putLongArray(paramString, paramArrayOfLong);
    return this;
  }
  
  public Intent putExtra(String paramString, Parcelable[] paramArrayOfParcelable)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putParcelableArray(paramString, paramArrayOfParcelable);
    return this;
  }
  
  public Intent putExtra(String paramString, CharSequence[] paramArrayOfCharSequence)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putCharSequenceArray(paramString, paramArrayOfCharSequence);
    return this;
  }
  
  public Intent putExtra(String paramString, String[] paramArrayOfString)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putStringArray(paramString, paramArrayOfString);
    return this;
  }
  
  public Intent putExtra(String paramString, short[] paramArrayOfShort)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putShortArray(paramString, paramArrayOfShort);
    return this;
  }
  
  public Intent putExtra(String paramString, boolean[] paramArrayOfBoolean)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putBooleanArray(paramString, paramArrayOfBoolean);
    return this;
  }
  
  public Intent putExtras(Intent paramIntent)
  {
    if (mExtras != null) {
      if (mExtras == null) {
        mExtras = new Bundle(mExtras);
      } else {
        mExtras.putAll(mExtras);
      }
    }
    return this;
  }
  
  public Intent putExtras(Bundle paramBundle)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putAll(paramBundle);
    return this;
  }
  
  public Intent putIntegerArrayListExtra(String paramString, ArrayList<Integer> paramArrayList)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putIntegerArrayList(paramString, paramArrayList);
    return this;
  }
  
  public Intent putParcelableArrayListExtra(String paramString, ArrayList<? extends Parcelable> paramArrayList)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putParcelableArrayList(paramString, paramArrayList);
    return this;
  }
  
  public Intent putStringArrayListExtra(String paramString, ArrayList<String> paramArrayList)
  {
    if (mExtras == null) {
      mExtras = new Bundle();
    }
    mExtras.putStringArrayList(paramString, paramArrayList);
    return this;
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    setAction(paramParcel.readString());
    mData = ((Uri)Uri.CREATOR.createFromParcel(paramParcel));
    mType = paramParcel.readString();
    mFlags = paramParcel.readInt();
    mPackage = paramParcel.readString();
    mComponent = ComponentName.readFromParcel(paramParcel);
    if (paramParcel.readInt() != 0) {
      mSourceBounds = ((Rect)Rect.CREATOR.createFromParcel(paramParcel));
    }
    int i = paramParcel.readInt();
    if (i > 0)
    {
      mCategories = new ArraySet();
      for (int j = 0; j < i; j++) {
        mCategories.add(paramParcel.readString().intern());
      }
    }
    else
    {
      mCategories = null;
    }
    if (paramParcel.readInt() != 0) {
      mSelector = new Intent(paramParcel);
    }
    if (paramParcel.readInt() != 0) {
      mClipData = new ClipData(paramParcel);
    }
    mContentUserHint = paramParcel.readInt();
    mExtras = paramParcel.readBundle();
  }
  
  public void removeCategory(String paramString)
  {
    if (mCategories != null)
    {
      mCategories.remove(paramString);
      if (mCategories.size() == 0) {
        mCategories = null;
      }
    }
  }
  
  public void removeExtra(String paramString)
  {
    if (mExtras != null)
    {
      mExtras.remove(paramString);
      if (mExtras.size() == 0) {
        mExtras = null;
      }
    }
  }
  
  public void removeFlags(int paramInt)
  {
    mFlags &= paramInt;
  }
  
  public void removeUnsafeExtras()
  {
    if (mExtras != null) {
      mExtras = mExtras.filterValues();
    }
  }
  
  public Intent replaceExtras(Intent paramIntent)
  {
    if (mExtras != null) {
      paramIntent = new Bundle(mExtras);
    } else {
      paramIntent = null;
    }
    mExtras = paramIntent;
    return this;
  }
  
  public Intent replaceExtras(Bundle paramBundle)
  {
    if (paramBundle != null) {
      paramBundle = new Bundle(paramBundle);
    } else {
      paramBundle = null;
    }
    mExtras = paramBundle;
    return this;
  }
  
  public ComponentName resolveActivity(PackageManager paramPackageManager)
  {
    if (mComponent != null) {
      return mComponent;
    }
    paramPackageManager = paramPackageManager.resolveActivity(this, 65536);
    if (paramPackageManager != null) {
      return new ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name);
    }
    return null;
  }
  
  public ActivityInfo resolveActivityInfo(PackageManager paramPackageManager, int paramInt)
  {
    Object localObject = null;
    ResolveInfo localResolveInfo = null;
    if (mComponent != null)
    {
      try
      {
        paramPackageManager = paramPackageManager.getActivityInfo(mComponent, paramInt);
      }
      catch (PackageManager.NameNotFoundException paramPackageManager)
      {
        for (;;)
        {
          paramPackageManager = localResolveInfo;
        }
      }
    }
    else
    {
      localResolveInfo = paramPackageManager.resolveActivity(this, 0x10000 | paramInt);
      paramPackageManager = localObject;
      if (localResolveInfo != null) {
        paramPackageManager = activityInfo;
      }
    }
    return paramPackageManager;
  }
  
  public ComponentName resolveSystemService(PackageManager paramPackageManager, int paramInt)
  {
    if (mComponent != null) {
      return mComponent;
    }
    Object localObject1 = paramPackageManager.queryIntentServices(this, paramInt);
    if (localObject1 == null) {
      return null;
    }
    paramPackageManager = null;
    paramInt = 0;
    while (paramInt < ((List)localObject1).size())
    {
      Object localObject2 = (ResolveInfo)((List)localObject1).get(paramInt);
      if ((serviceInfo.applicationInfo.flags & 0x1) != 0)
      {
        localObject2 = new ComponentName(serviceInfo.applicationInfo.packageName, serviceInfo.name);
        if (paramPackageManager == null) {
          paramPackageManager = (PackageManager)localObject2;
        }
      }
      else
      {
        paramInt++;
        continue;
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Multiple system services handle ");
      ((StringBuilder)localObject1).append(this);
      ((StringBuilder)localObject1).append(": ");
      ((StringBuilder)localObject1).append(paramPackageManager);
      ((StringBuilder)localObject1).append(", ");
      ((StringBuilder)localObject1).append(localObject2);
      throw new IllegalStateException(((StringBuilder)localObject1).toString());
    }
    return paramPackageManager;
  }
  
  public String resolveType(ContentResolver paramContentResolver)
  {
    if (mType != null) {
      return mType;
    }
    if ((mData != null) && ("content".equals(mData.getScheme()))) {
      return paramContentResolver.getType(mData);
    }
    return null;
  }
  
  public String resolveType(Context paramContext)
  {
    return resolveType(paramContext.getContentResolver());
  }
  
  public String resolveTypeIfNeeded(ContentResolver paramContentResolver)
  {
    if (mComponent != null) {
      return mType;
    }
    return resolveType(paramContentResolver);
  }
  
  public void saveToXml(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    if (mAction != null) {
      paramXmlSerializer.attribute(null, "action", mAction);
    }
    if (mData != null) {
      paramXmlSerializer.attribute(null, "data", mData.toString());
    }
    if (mType != null) {
      paramXmlSerializer.attribute(null, "type", mType);
    }
    if (mComponent != null) {
      paramXmlSerializer.attribute(null, "component", mComponent.flattenToShortString());
    }
    paramXmlSerializer.attribute(null, "flags", Integer.toHexString(getFlags()));
    if (mCategories != null)
    {
      paramXmlSerializer.startTag(null, "categories");
      for (int i = mCategories.size() - 1; i >= 0; i--) {
        paramXmlSerializer.attribute(null, "category", (String)mCategories.valueAt(i));
      }
      paramXmlSerializer.endTag(null, "categories");
    }
  }
  
  public Intent setAction(String paramString)
  {
    if (paramString != null) {
      paramString = paramString.intern();
    } else {
      paramString = null;
    }
    mAction = paramString;
    return this;
  }
  
  public void setAllowFds(boolean paramBoolean)
  {
    if (mExtras != null) {
      mExtras.setAllowFds(paramBoolean);
    }
  }
  
  public Intent setClass(Context paramContext, Class<?> paramClass)
  {
    mComponent = new ComponentName(paramContext, paramClass);
    return this;
  }
  
  public Intent setClassName(Context paramContext, String paramString)
  {
    mComponent = new ComponentName(paramContext, paramString);
    return this;
  }
  
  public Intent setClassName(String paramString1, String paramString2)
  {
    mComponent = new ComponentName(paramString1, paramString2);
    return this;
  }
  
  public void setClipData(ClipData paramClipData)
  {
    mClipData = paramClipData;
  }
  
  public Intent setComponent(ComponentName paramComponentName)
  {
    mComponent = paramComponentName;
    return this;
  }
  
  public Intent setData(Uri paramUri)
  {
    mData = paramUri;
    mType = null;
    return this;
  }
  
  public Intent setDataAndNormalize(Uri paramUri)
  {
    return setData(paramUri.normalizeScheme());
  }
  
  public Intent setDataAndType(Uri paramUri, String paramString)
  {
    mData = paramUri;
    mType = paramString;
    return this;
  }
  
  public Intent setDataAndTypeAndNormalize(Uri paramUri, String paramString)
  {
    return setDataAndType(paramUri.normalizeScheme(), normalizeMimeType(paramString));
  }
  
  public void setDefusable(boolean paramBoolean)
  {
    if (mExtras != null) {
      mExtras.setDefusable(paramBoolean);
    }
  }
  
  public void setExtrasClassLoader(ClassLoader paramClassLoader)
  {
    if (mExtras != null) {
      mExtras.setClassLoader(paramClassLoader);
    }
  }
  
  public Intent setFlags(int paramInt)
  {
    mFlags = paramInt;
    return this;
  }
  
  public void setLaunchToken(String paramString)
  {
    mLaunchToken = paramString;
  }
  
  public Intent setPackage(String paramString)
  {
    if ((paramString != null) && (mSelector != null)) {
      throw new IllegalArgumentException("Can't set package name when selector is already set");
    }
    mPackage = paramString;
    return this;
  }
  
  public void setSelector(Intent paramIntent)
  {
    if (paramIntent != this)
    {
      if ((paramIntent != null) && (mPackage != null)) {
        throw new IllegalArgumentException("Can't set selector when package name is already set");
      }
      mSelector = paramIntent;
      return;
    }
    throw new IllegalArgumentException("Intent being set as a selector of itself");
  }
  
  public void setSourceBounds(Rect paramRect)
  {
    if (paramRect != null) {
      mSourceBounds = new Rect(paramRect);
    } else {
      mSourceBounds = null;
    }
  }
  
  public Intent setType(String paramString)
  {
    mData = null;
    mType = paramString;
    return this;
  }
  
  public Intent setTypeAndNormalize(String paramString)
  {
    return setType(normalizeMimeType(paramString));
  }
  
  public String toInsecureString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("Intent { ");
    toShortString(localStringBuilder, false, true, true, false);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public String toInsecureStringWithClip()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("Intent { ");
    toShortString(localStringBuilder, false, true, true, true);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  public String toShortString(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    toShortString(localStringBuilder, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
    return localStringBuilder.toString();
  }
  
  public void toShortString(StringBuilder paramStringBuilder, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    int i = 1;
    if (mAction != null)
    {
      paramStringBuilder.append("act=");
      paramStringBuilder.append(mAction);
      i = 0;
    }
    int j = i;
    if (mCategories != null)
    {
      if (i == 0) {
        paramStringBuilder.append(' ');
      }
      j = 0;
      paramStringBuilder.append("cat=[");
      for (i = 0; i < mCategories.size(); i++)
      {
        if (i > 0) {
          paramStringBuilder.append(',');
        }
        paramStringBuilder.append((String)mCategories.valueAt(i));
      }
      paramStringBuilder.append("]");
    }
    i = j;
    if (mData != null)
    {
      if (j == 0) {
        paramStringBuilder.append(' ');
      }
      i = 0;
      paramStringBuilder.append("dat=");
      if (paramBoolean1) {
        paramStringBuilder.append(mData.toSafeString());
      } else {
        paramStringBuilder.append(mData);
      }
    }
    j = i;
    if (mType != null)
    {
      if (i == 0) {
        paramStringBuilder.append(' ');
      }
      j = 0;
      paramStringBuilder.append("typ=");
      paramStringBuilder.append(mType);
    }
    i = j;
    if (mFlags != 0)
    {
      if (j == 0) {
        paramStringBuilder.append(' ');
      }
      i = 0;
      paramStringBuilder.append("flg=0x");
      paramStringBuilder.append(Integer.toHexString(mFlags));
    }
    j = i;
    if (mPackage != null)
    {
      if (i == 0) {
        paramStringBuilder.append(' ');
      }
      j = 0;
      paramStringBuilder.append("pkg=");
      paramStringBuilder.append(mPackage);
    }
    i = j;
    if (paramBoolean2)
    {
      i = j;
      if (mComponent != null)
      {
        if (j == 0) {
          paramStringBuilder.append(' ');
        }
        i = 0;
        paramStringBuilder.append("cmp=");
        paramStringBuilder.append(mComponent.flattenToShortString());
      }
    }
    j = i;
    if (mSourceBounds != null)
    {
      if (i == 0) {
        paramStringBuilder.append(' ');
      }
      j = 0;
      paramStringBuilder.append("bnds=");
      paramStringBuilder.append(mSourceBounds.toShortString());
    }
    i = j;
    if (mClipData != null)
    {
      if (j == 0) {
        paramStringBuilder.append(' ');
      }
      paramStringBuilder.append("clip={");
      if (paramBoolean4)
      {
        mClipData.toShortString(paramStringBuilder);
      }
      else
      {
        boolean bool;
        if (mClipData.getDescription() != null) {
          bool = mClipData.getDescription().toShortStringTypesOnly(paramStringBuilder) ^ true;
        } else {
          bool = true;
        }
        mClipData.toShortStringShortItems(paramStringBuilder, bool);
      }
      i = 0;
      paramStringBuilder.append('}');
    }
    j = i;
    if (paramBoolean3)
    {
      j = i;
      if (mExtras != null)
      {
        if (i == 0) {
          paramStringBuilder.append(' ');
        }
        j = 0;
        paramStringBuilder.append("(has extras)");
      }
    }
    if (mContentUserHint != -2)
    {
      if (j == 0) {
        paramStringBuilder.append(' ');
      }
      paramStringBuilder.append("u=");
      paramStringBuilder.append(mContentUserHint);
    }
    if (mSelector != null)
    {
      paramStringBuilder.append(" sel=");
      mSelector.toShortString(paramStringBuilder, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4);
      paramStringBuilder.append("}");
    }
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append("Intent { ");
    toShortString(localStringBuilder, true, true, true, false);
    localStringBuilder.append(" }");
    return localStringBuilder.toString();
  }
  
  @Deprecated
  public String toURI()
  {
    return toUri(0);
  }
  
  public String toUri(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    Object localObject1;
    String str;
    if ((paramInt & 0x2) != 0)
    {
      if (mPackage != null)
      {
        localStringBuilder.append("android-app://");
        localStringBuilder.append(mPackage);
        localObject1 = null;
        if (mData != null)
        {
          localObject2 = mData.getScheme();
          localObject1 = localObject2;
          if (localObject2 != null)
          {
            localStringBuilder.append('/');
            localStringBuilder.append((String)localObject2);
            str = mData.getEncodedAuthority();
            localObject1 = localObject2;
            if (str != null)
            {
              localStringBuilder.append('/');
              localStringBuilder.append(str);
              localObject1 = mData.getEncodedPath();
              if (localObject1 != null) {
                localStringBuilder.append((String)localObject1);
              }
              localObject1 = mData.getEncodedQuery();
              if (localObject1 != null)
              {
                localStringBuilder.append('?');
                localStringBuilder.append((String)localObject1);
              }
              str = mData.getEncodedFragment();
              localObject1 = localObject2;
              if (str != null)
              {
                localStringBuilder.append('#');
                localStringBuilder.append(str);
                localObject1 = localObject2;
              }
            }
          }
        }
        if (localObject1 == null) {}
        for (localObject1 = "android.intent.action.MAIN";; localObject1 = "android.intent.action.VIEW") {
          break;
        }
        toUriFragment(localStringBuilder, null, (String)localObject1, mPackage, paramInt);
        return localStringBuilder.toString();
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("Intent must include an explicit package name to build an android-app: ");
      ((StringBuilder)localObject1).append(this);
      throw new IllegalArgumentException(((StringBuilder)localObject1).toString());
    }
    Object localObject2 = null;
    Object localObject3 = null;
    if (mData != null)
    {
      str = mData.toString();
      localObject1 = localObject3;
      localObject2 = str;
      if ((paramInt & 0x1) != 0)
      {
        int i = str.length();
        for (int j = 0;; j++)
        {
          localObject1 = localObject3;
          localObject2 = str;
          if (j >= i) {
            break;
          }
          int k = str.charAt(j);
          if (((k < 97) || (k > 122)) && ((k < 65) || (k > 90)) && ((k < 48) || (k > 57)) && (k != 46) && (k != 45) && (k != 43))
          {
            localObject1 = localObject3;
            localObject2 = str;
            if (k != 58) {
              break;
            }
            localObject1 = localObject3;
            localObject2 = str;
            if (j <= 0) {
              break;
            }
            localObject1 = str.substring(0, j);
            localStringBuilder.append("intent:");
            localObject2 = str.substring(j + 1);
            break;
          }
        }
      }
      localStringBuilder.append((String)localObject2);
    }
    else
    {
      localObject1 = localObject2;
      if ((paramInt & 0x1) != 0)
      {
        localStringBuilder.append("intent:");
        localObject1 = localObject2;
      }
    }
    toUriFragment(localStringBuilder, (String)localObject1, "android.intent.action.VIEW", null, paramInt);
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(mAction);
    Uri.writeToParcel(paramParcel, mData);
    paramParcel.writeString(mType);
    paramParcel.writeInt(mFlags);
    paramParcel.writeString(mPackage);
    ComponentName.writeToParcel(mComponent, paramParcel);
    if (mSourceBounds != null)
    {
      paramParcel.writeInt(1);
      mSourceBounds.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mCategories != null)
    {
      int i = mCategories.size();
      paramParcel.writeInt(i);
      for (int j = 0; j < i; j++) {
        paramParcel.writeString((String)mCategories.valueAt(j));
      }
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mSelector != null)
    {
      paramParcel.writeInt(1);
      mSelector.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    if (mClipData != null)
    {
      paramParcel.writeInt(1);
      mClipData.writeToParcel(paramParcel, paramInt);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(mContentUserHint);
    paramParcel.writeBundle(mExtras);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
  {
    writeToProto(paramProtoOutputStream, paramLong, true, true, true, false);
  }
  
  public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    paramLong = paramProtoOutputStream.start(paramLong);
    if (mAction != null) {
      paramProtoOutputStream.write(1138166333441L, mAction);
    }
    Object localObject;
    if (mCategories != null)
    {
      localObject = mCategories.iterator();
      while (((Iterator)localObject).hasNext()) {
        paramProtoOutputStream.write(2237677961218L, (String)((Iterator)localObject).next());
      }
    }
    if (mData != null)
    {
      if (paramBoolean1) {
        localObject = mData.toSafeString();
      } else {
        localObject = mData.toString();
      }
      paramProtoOutputStream.write(1138166333443L, (String)localObject);
    }
    if (mType != null) {
      paramProtoOutputStream.write(1138166333444L, mType);
    }
    if (mFlags != 0)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("0x");
      ((StringBuilder)localObject).append(Integer.toHexString(mFlags));
      paramProtoOutputStream.write(1138166333445L, ((StringBuilder)localObject).toString());
    }
    if (mPackage != null) {
      paramProtoOutputStream.write(1138166333446L, mPackage);
    }
    if ((paramBoolean2) && (mComponent != null)) {
      mComponent.writeToProto(paramProtoOutputStream, 1146756268039L);
    }
    if (mSourceBounds != null) {
      paramProtoOutputStream.write(1138166333448L, mSourceBounds.toShortString());
    }
    if (mClipData != null)
    {
      localObject = new StringBuilder();
      if (paramBoolean4) {
        mClipData.toShortString((StringBuilder)localObject);
      } else {
        mClipData.toShortStringShortItems((StringBuilder)localObject, false);
      }
      paramProtoOutputStream.write(1138166333449L, ((StringBuilder)localObject).toString());
    }
    if ((paramBoolean3) && (mExtras != null)) {
      paramProtoOutputStream.write(1138166333450L, mExtras.toShortString());
    }
    if (mContentUserHint != 0) {
      paramProtoOutputStream.write(1120986464267L, mContentUserHint);
    }
    if (mSelector != null) {
      paramProtoOutputStream.write(1138166333452L, mSelector.toShortString(paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4));
    }
    paramProtoOutputStream.end(paramLong);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AccessUriMode {}
  
  public static abstract interface CommandOptionHandler
  {
    public abstract boolean handleOption(String paramString, ShellCommand paramShellCommand);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface CopyMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FillInFlags {}
  
  public static final class FilterComparison
  {
    private final int mHashCode;
    private final Intent mIntent;
    
    public FilterComparison(Intent paramIntent)
    {
      mIntent = paramIntent;
      mHashCode = paramIntent.filterHashCode();
    }
    
    public boolean equals(Object paramObject)
    {
      if ((paramObject instanceof FilterComparison))
      {
        paramObject = mIntent;
        return mIntent.filterEquals(paramObject);
      }
      return false;
    }
    
    public Intent getIntent()
    {
      return mIntent;
    }
    
    public int hashCode()
    {
      return mHashCode;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Flags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface GrantUriMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface MutableFlags {}
  
  public static class ShortcutIconResource
    implements Parcelable
  {
    public static final Parcelable.Creator<ShortcutIconResource> CREATOR = new Parcelable.Creator()
    {
      public Intent.ShortcutIconResource createFromParcel(Parcel paramAnonymousParcel)
      {
        Intent.ShortcutIconResource localShortcutIconResource = new Intent.ShortcutIconResource();
        packageName = paramAnonymousParcel.readString();
        resourceName = paramAnonymousParcel.readString();
        return localShortcutIconResource;
      }
      
      public Intent.ShortcutIconResource[] newArray(int paramAnonymousInt)
      {
        return new Intent.ShortcutIconResource[paramAnonymousInt];
      }
    };
    public String packageName;
    public String resourceName;
    
    public ShortcutIconResource() {}
    
    public static ShortcutIconResource fromContext(Context paramContext, int paramInt)
    {
      ShortcutIconResource localShortcutIconResource = new ShortcutIconResource();
      packageName = paramContext.getPackageName();
      resourceName = paramContext.getResources().getResourceName(paramInt);
      return localShortcutIconResource;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public String toString()
    {
      return resourceName;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(packageName);
      paramParcel.writeString(resourceName);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface UriFlags {}
}
