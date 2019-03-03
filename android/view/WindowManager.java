package android.view;

import android.annotation.SystemApi;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Objects;

public abstract interface WindowManager
  extends ViewManager
{
  public static final int DOCKED_BOTTOM = 4;
  public static final int DOCKED_INVALID = -1;
  public static final int DOCKED_LEFT = 1;
  public static final int DOCKED_RIGHT = 3;
  public static final int DOCKED_TOP = 2;
  public static final String INPUT_CONSUMER_GESTURE = "gesture_input_consumer";
  public static final String INPUT_CONSUMER_NAVIGATION = "nav_input_consumer";
  public static final String INPUT_CONSUMER_PIP = "pip_input_consumer";
  public static final String INPUT_CONSUMER_RECENTS_ANIMATION = "recents_animation_input_consumer";
  public static final String INPUT_CONSUMER_WALLPAPER = "wallpaper_input_consumer";
  public static final String PARCEL_KEY_SHORTCUTS_ARRAY = "shortcuts_array";
  public static final int TAKE_SCREENSHOT_FULLSCREEN = 1;
  public static final int TAKE_SCREENSHOT_SELECTED_REGION = 2;
  public static final int TRANSIT_ACTIVITY_CLOSE = 7;
  public static final int TRANSIT_ACTIVITY_OPEN = 6;
  public static final int TRANSIT_ACTIVITY_RELAUNCH = 18;
  public static final int TRANSIT_CRASHING_ACTIVITY_CLOSE = 26;
  public static final int TRANSIT_DOCK_TASK_FROM_RECENTS = 19;
  public static final int TRANSIT_FLAG_KEYGUARD_GOING_AWAY_NO_ANIMATION = 2;
  public static final int TRANSIT_FLAG_KEYGUARD_GOING_AWAY_TO_SHADE = 1;
  public static final int TRANSIT_FLAG_KEYGUARD_GOING_AWAY_WITH_WALLPAPER = 4;
  public static final int TRANSIT_KEYGUARD_GOING_AWAY = 20;
  public static final int TRANSIT_KEYGUARD_GOING_AWAY_ON_WALLPAPER = 21;
  public static final int TRANSIT_KEYGUARD_OCCLUDE = 22;
  public static final int TRANSIT_KEYGUARD_UNOCCLUDE = 23;
  public static final int TRANSIT_NONE = 0;
  public static final int TRANSIT_TASK_CLOSE = 9;
  public static final int TRANSIT_TASK_IN_PLACE = 17;
  public static final int TRANSIT_TASK_OPEN = 8;
  public static final int TRANSIT_TASK_OPEN_BEHIND = 16;
  public static final int TRANSIT_TASK_TO_BACK = 11;
  public static final int TRANSIT_TASK_TO_FRONT = 10;
  public static final int TRANSIT_TRANSLUCENT_ACTIVITY_CLOSE = 25;
  public static final int TRANSIT_TRANSLUCENT_ACTIVITY_OPEN = 24;
  public static final int TRANSIT_UNSET = -1;
  public static final int TRANSIT_WALLPAPER_CLOSE = 12;
  public static final int TRANSIT_WALLPAPER_INTRA_CLOSE = 15;
  public static final int TRANSIT_WALLPAPER_INTRA_OPEN = 14;
  public static final int TRANSIT_WALLPAPER_OPEN = 13;
  
  @SystemApi
  public abstract Region getCurrentImeTouchRegion();
  
  public abstract Display getDefaultDisplay();
  
  public abstract void removeViewImmediate(View paramView);
  
  public abstract void requestAppKeyboardShortcuts(KeyboardShortcutsReceiver paramKeyboardShortcutsReceiver, int paramInt);
  
  public static class BadTokenException
    extends RuntimeException
  {
    public BadTokenException() {}
    
    public BadTokenException(String paramString)
    {
      super();
    }
  }
  
  public static class InvalidDisplayException
    extends RuntimeException
  {
    public InvalidDisplayException() {}
    
    public InvalidDisplayException(String paramString)
    {
      super();
    }
  }
  
  public static abstract interface KeyboardShortcutsReceiver
  {
    public abstract void onKeyboardShortcutsReceived(List<KeyboardShortcutGroup> paramList);
  }
  
  public static class LayoutParams
    extends ViewGroup.LayoutParams
    implements Parcelable
  {
    public static final int ACCESSIBILITY_ANCHOR_CHANGED = 16777216;
    public static final int ACCESSIBILITY_TITLE_CHANGED = 33554432;
    public static final int ALPHA_CHANGED = 128;
    public static final int ANIMATION_CHANGED = 16;
    public static final float BRIGHTNESS_OVERRIDE_FULL = 1.0F;
    public static final float BRIGHTNESS_OVERRIDE_NONE = -1.0F;
    public static final float BRIGHTNESS_OVERRIDE_OFF = 0.0F;
    public static final int BUTTON_BRIGHTNESS_CHANGED = 8192;
    public static final int COLOR_MODE_CHANGED = 67108864;
    public static final Parcelable.Creator<LayoutParams> CREATOR = new Parcelable.Creator()
    {
      public WindowManager.LayoutParams createFromParcel(Parcel paramAnonymousParcel)
      {
        return new WindowManager.LayoutParams(paramAnonymousParcel);
      }
      
      public WindowManager.LayoutParams[] newArray(int paramAnonymousInt)
      {
        return new WindowManager.LayoutParams[paramAnonymousInt];
      }
    };
    public static final int DIM_AMOUNT_CHANGED = 32;
    public static final int EVERYTHING_CHANGED = -1;
    public static final int FIRST_APPLICATION_WINDOW = 1;
    public static final int FIRST_SUB_WINDOW = 1000;
    public static final int FIRST_SYSTEM_WINDOW = 2000;
    public static final int FLAGS_CHANGED = 4;
    public static final int FLAG_ALLOW_LOCK_WHILE_SCREEN_ON = 1;
    public static final int FLAG_ALT_FOCUSABLE_IM = 131072;
    @Deprecated
    public static final int FLAG_BLUR_BEHIND = 4;
    public static final int FLAG_DIM_BEHIND = 2;
    @Deprecated
    public static final int FLAG_DISMISS_KEYGUARD = 4194304;
    @Deprecated
    public static final int FLAG_DITHER = 4096;
    public static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = Integer.MIN_VALUE;
    public static final int FLAG_FORCE_NOT_FULLSCREEN = 2048;
    public static final int FLAG_FULLSCREEN = 1024;
    public static final int FLAG_HARDWARE_ACCELERATED = 16777216;
    public static final int FLAG_IGNORE_CHEEK_PRESSES = 32768;
    public static final int FLAG_KEEP_SCREEN_ON = 128;
    public static final int FLAG_LAYOUT_ATTACHED_IN_DECOR = 1073741824;
    public static final int FLAG_LAYOUT_INSET_DECOR = 65536;
    public static final int FLAG_LAYOUT_IN_OVERSCAN = 33554432;
    public static final int FLAG_LAYOUT_IN_SCREEN = 256;
    public static final int FLAG_LAYOUT_NO_LIMITS = 512;
    public static final int FLAG_LOCAL_FOCUS_MODE = 268435456;
    public static final int FLAG_NOT_FOCUSABLE = 8;
    public static final int FLAG_NOT_TOUCHABLE = 16;
    public static final int FLAG_NOT_TOUCH_MODAL = 32;
    public static final int FLAG_SCALED = 16384;
    public static final int FLAG_SECURE = 8192;
    public static final int FLAG_SHOW_WALLPAPER = 1048576;
    @Deprecated
    public static final int FLAG_SHOW_WHEN_LOCKED = 524288;
    public static final int FLAG_SLIPPERY = 536870912;
    public static final int FLAG_SPLIT_TOUCH = 8388608;
    @Deprecated
    public static final int FLAG_TOUCHABLE_WHEN_WAKING = 64;
    public static final int FLAG_TRANSLUCENT_NAVIGATION = 134217728;
    public static final int FLAG_TRANSLUCENT_STATUS = 67108864;
    @Deprecated
    public static final int FLAG_TURN_SCREEN_ON = 2097152;
    public static final int FLAG_WATCH_OUTSIDE_TOUCH = 262144;
    public static final int FORMAT_CHANGED = 8;
    public static final int INPUT_FEATURES_CHANGED = 65536;
    public static final int INPUT_FEATURE_DISABLE_POINTER_GESTURES = 1;
    public static final int INPUT_FEATURE_DISABLE_USER_ACTIVITY = 4;
    public static final int INPUT_FEATURE_NO_INPUT_CHANNEL = 2;
    public static final int INVALID_WINDOW_TYPE = -1;
    public static final int LAST_APPLICATION_WINDOW = 99;
    public static final int LAST_SUB_WINDOW = 1999;
    public static final int LAST_SYSTEM_WINDOW = 2999;
    public static final int LAYOUT_CHANGED = 1;
    @Deprecated
    public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS = 1;
    public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT = 0;
    public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER = 2;
    public static final int LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES = 1;
    public static final int MEMORY_TYPE_CHANGED = 256;
    @Deprecated
    public static final int MEMORY_TYPE_GPU = 2;
    @Deprecated
    public static final int MEMORY_TYPE_HARDWARE = 1;
    @Deprecated
    public static final int MEMORY_TYPE_NORMAL = 0;
    @Deprecated
    public static final int MEMORY_TYPE_PUSH_BUFFERS = 3;
    public static final int NEEDS_MENU_KEY_CHANGED = 4194304;
    public static final int NEEDS_MENU_SET_FALSE = 2;
    public static final int NEEDS_MENU_SET_TRUE = 1;
    public static final int NEEDS_MENU_UNSET = 0;
    public static final int PREFERRED_DISPLAY_MODE_ID = 8388608;
    public static final int PREFERRED_REFRESH_RATE_CHANGED = 2097152;
    public static final int PRIVATE_FLAGS_CHANGED = 131072;
    public static final int PRIVATE_FLAG_ACQUIRES_SLEEP_TOKEN = 2097152;
    public static final int PRIVATE_FLAG_COMPATIBLE_WINDOW = 128;
    public static final int PRIVATE_FLAG_DISABLE_WALLPAPER_TOUCH_EVENTS = 2048;
    public static final int PRIVATE_FLAG_FAKE_HARDWARE_ACCELERATED = 1;
    public static final int PRIVATE_FLAG_FORCE_DECOR_VIEW_VISIBILITY = 16384;
    public static final int PRIVATE_FLAG_FORCE_DRAW_STATUS_BAR_BACKGROUND = 131072;
    public static final int PRIVATE_FLAG_FORCE_HARDWARE_ACCELERATED = 2;
    public static final int PRIVATE_FLAG_FORCE_STATUS_BAR_VISIBLE_TRANSPARENT = 4096;
    public static final int PRIVATE_FLAG_HIDE_NON_SYSTEM_OVERLAY_WINDOWS = 524288;
    public static final int PRIVATE_FLAG_INHERIT_TRANSLUCENT_DECOR = 512;
    public static final int PRIVATE_FLAG_IS_ROUNDED_CORNERS_OVERLAY = 1048576;
    public static final int PRIVATE_FLAG_IS_SCREEN_DECOR = 4194304;
    public static final int PRIVATE_FLAG_KEYGUARD = 1024;
    public static final int PRIVATE_FLAG_LAYOUT_CHILD_WINDOW_IN_PARENT_FRAME = 65536;
    public static final int PRIVATE_FLAG_NO_MOVE_ANIMATION = 64;
    public static final int PRIVATE_FLAG_PRESERVE_GEOMETRY = 8192;
    public static final int PRIVATE_FLAG_SHOW_FOR_ALL_USERS = 16;
    public static final int PRIVATE_FLAG_SUSTAINED_PERFORMANCE_MODE = 262144;
    public static final int PRIVATE_FLAG_SYSTEM_ERROR = 256;
    public static final int PRIVATE_FLAG_WANTS_OFFSET_NOTIFICATIONS = 4;
    public static final int PRIVATE_FLAG_WILL_NOT_REPLACE_ON_RELAUNCH = 32768;
    public static final int ROTATION_ANIMATION_CHANGED = 4096;
    public static final int ROTATION_ANIMATION_CROSSFADE = 1;
    public static final int ROTATION_ANIMATION_JUMPCUT = 2;
    public static final int ROTATION_ANIMATION_ROTATE = 0;
    public static final int ROTATION_ANIMATION_SEAMLESS = 3;
    public static final int ROTATION_ANIMATION_UNSPECIFIED = -1;
    public static final int SCREEN_BRIGHTNESS_CHANGED = 2048;
    public static final int SCREEN_ORIENTATION_CHANGED = 1024;
    public static final int SOFT_INPUT_ADJUST_NOTHING = 48;
    public static final int SOFT_INPUT_ADJUST_PAN = 32;
    public static final int SOFT_INPUT_ADJUST_RESIZE = 16;
    public static final int SOFT_INPUT_ADJUST_UNSPECIFIED = 0;
    public static final int SOFT_INPUT_IS_FORWARD_NAVIGATION = 256;
    public static final int SOFT_INPUT_MASK_ADJUST = 240;
    public static final int SOFT_INPUT_MASK_STATE = 15;
    public static final int SOFT_INPUT_MODE_CHANGED = 512;
    public static final int SOFT_INPUT_STATE_ALWAYS_HIDDEN = 3;
    public static final int SOFT_INPUT_STATE_ALWAYS_VISIBLE = 5;
    public static final int SOFT_INPUT_STATE_HIDDEN = 2;
    public static final int SOFT_INPUT_STATE_UNCHANGED = 1;
    public static final int SOFT_INPUT_STATE_UNSPECIFIED = 0;
    public static final int SOFT_INPUT_STATE_VISIBLE = 4;
    public static final int SURFACE_INSETS_CHANGED = 1048576;
    public static final int SYSTEM_UI_LISTENER_CHANGED = 32768;
    public static final int SYSTEM_UI_VISIBILITY_CHANGED = 16384;
    public static final int TITLE_CHANGED = 64;
    public static final int TRANSLUCENT_FLAGS_CHANGED = 524288;
    public static final int TYPE_ACCESSIBILITY_OVERLAY = 2032;
    public static final int TYPE_APPLICATION = 2;
    public static final int TYPE_APPLICATION_ABOVE_SUB_PANEL = 1005;
    public static final int TYPE_APPLICATION_ATTACHED_DIALOG = 1003;
    public static final int TYPE_APPLICATION_MEDIA = 1001;
    public static final int TYPE_APPLICATION_MEDIA_OVERLAY = 1004;
    public static final int TYPE_APPLICATION_OVERLAY = 2038;
    public static final int TYPE_APPLICATION_PANEL = 1000;
    public static final int TYPE_APPLICATION_STARTING = 3;
    public static final int TYPE_APPLICATION_SUB_PANEL = 1002;
    public static final int TYPE_BASE_APPLICATION = 1;
    public static final int TYPE_BOOT_PROGRESS = 2021;
    public static final int TYPE_CHANGED = 2;
    public static final int TYPE_DISPLAY_OVERLAY = 2026;
    public static final int TYPE_DOCK_DIVIDER = 2034;
    public static final int TYPE_DRAG = 2016;
    public static final int TYPE_DRAWN_APPLICATION = 4;
    public static final int TYPE_DREAM = 2023;
    public static final int TYPE_INPUT_CONSUMER = 2022;
    public static final int TYPE_INPUT_METHOD = 2011;
    public static final int TYPE_INPUT_METHOD_DIALOG = 2012;
    public static final int TYPE_KEYGUARD = 2004;
    public static final int TYPE_KEYGUARD_DIALOG = 2009;
    public static final int TYPE_MAGNIFICATION_OVERLAY = 2027;
    public static final int TYPE_NAVIGATION_BAR = 2019;
    public static final int TYPE_NAVIGATION_BAR_PANEL = 2024;
    @Deprecated
    public static final int TYPE_PHONE = 2002;
    public static final int TYPE_POINTER = 2018;
    public static final int TYPE_PRESENTATION = 2037;
    @Deprecated
    public static final int TYPE_PRIORITY_PHONE = 2007;
    public static final int TYPE_PRIVATE_PRESENTATION = 2030;
    public static final int TYPE_QS_DIALOG = 2035;
    public static final int TYPE_SCREENSHOT = 2036;
    public static final int TYPE_SEARCH_BAR = 2001;
    public static final int TYPE_SECURE_SYSTEM_OVERLAY = 2015;
    public static final int TYPE_STATUS_BAR = 2000;
    public static final int TYPE_STATUS_BAR_PANEL = 2014;
    public static final int TYPE_STATUS_BAR_SUB_PANEL = 2017;
    @Deprecated
    public static final int TYPE_SYSTEM_ALERT = 2003;
    public static final int TYPE_SYSTEM_DIALOG = 2008;
    @Deprecated
    public static final int TYPE_SYSTEM_ERROR = 2010;
    @Deprecated
    public static final int TYPE_SYSTEM_OVERLAY = 2006;
    @Deprecated
    public static final int TYPE_TOAST = 2005;
    public static final int TYPE_VOICE_INTERACTION = 2031;
    public static final int TYPE_VOICE_INTERACTION_STARTING = 2033;
    public static final int TYPE_VOLUME_OVERLAY = 2020;
    public static final int TYPE_WALLPAPER = 2013;
    public static final int USER_ACTIVITY_TIMEOUT_CHANGED = 262144;
    public long accessibilityIdOfAnchor;
    public CharSequence accessibilityTitle;
    public float alpha;
    public float buttonBrightness;
    public float dimAmount;
    @ViewDebug.ExportedProperty(flagMapping={@ViewDebug.FlagToString(equals=1, mask=1, name="ALLOW_LOCK_WHILE_SCREEN_ON"), @ViewDebug.FlagToString(equals=2, mask=2, name="DIM_BEHIND"), @ViewDebug.FlagToString(equals=4, mask=4, name="BLUR_BEHIND"), @ViewDebug.FlagToString(equals=8, mask=8, name="NOT_FOCUSABLE"), @ViewDebug.FlagToString(equals=16, mask=16, name="NOT_TOUCHABLE"), @ViewDebug.FlagToString(equals=32, mask=32, name="NOT_TOUCH_MODAL"), @ViewDebug.FlagToString(equals=64, mask=64, name="TOUCHABLE_WHEN_WAKING"), @ViewDebug.FlagToString(equals=128, mask=128, name="KEEP_SCREEN_ON"), @ViewDebug.FlagToString(equals=256, mask=256, name="LAYOUT_IN_SCREEN"), @ViewDebug.FlagToString(equals=512, mask=512, name="LAYOUT_NO_LIMITS"), @ViewDebug.FlagToString(equals=1024, mask=1024, name="FULLSCREEN"), @ViewDebug.FlagToString(equals=2048, mask=2048, name="FORCE_NOT_FULLSCREEN"), @ViewDebug.FlagToString(equals=4096, mask=4096, name="DITHER"), @ViewDebug.FlagToString(equals=8192, mask=8192, name="SECURE"), @ViewDebug.FlagToString(equals=16384, mask=16384, name="SCALED"), @ViewDebug.FlagToString(equals=32768, mask=32768, name="IGNORE_CHEEK_PRESSES"), @ViewDebug.FlagToString(equals=65536, mask=65536, name="LAYOUT_INSET_DECOR"), @ViewDebug.FlagToString(equals=131072, mask=131072, name="ALT_FOCUSABLE_IM"), @ViewDebug.FlagToString(equals=262144, mask=262144, name="WATCH_OUTSIDE_TOUCH"), @ViewDebug.FlagToString(equals=524288, mask=524288, name="SHOW_WHEN_LOCKED"), @ViewDebug.FlagToString(equals=1048576, mask=1048576, name="SHOW_WALLPAPER"), @ViewDebug.FlagToString(equals=2097152, mask=2097152, name="TURN_SCREEN_ON"), @ViewDebug.FlagToString(equals=4194304, mask=4194304, name="DISMISS_KEYGUARD"), @ViewDebug.FlagToString(equals=8388608, mask=8388608, name="SPLIT_TOUCH"), @ViewDebug.FlagToString(equals=16777216, mask=16777216, name="HARDWARE_ACCELERATED"), @ViewDebug.FlagToString(equals=33554432, mask=33554432, name="LOCAL_FOCUS_MODE"), @ViewDebug.FlagToString(equals=67108864, mask=67108864, name="TRANSLUCENT_STATUS"), @ViewDebug.FlagToString(equals=134217728, mask=134217728, name="TRANSLUCENT_NAVIGATION"), @ViewDebug.FlagToString(equals=268435456, mask=268435456, name="LOCAL_FOCUS_MODE"), @ViewDebug.FlagToString(equals=536870912, mask=536870912, name="FLAG_SLIPPERY"), @ViewDebug.FlagToString(equals=1073741824, mask=1073741824, name="FLAG_LAYOUT_ATTACHED_IN_DECOR"), @ViewDebug.FlagToString(equals=Integer.MIN_VALUE, mask=Integer.MIN_VALUE, name="DRAWS_SYSTEM_BAR_BACKGROUNDS")}, formatToHexString=true)
    public int flags;
    public int format;
    public int gravity;
    public boolean hasManualSurfaceInsets;
    public boolean hasSystemUiListeners;
    public long hideTimeoutMilliseconds;
    public float horizontalMargin;
    @ViewDebug.ExportedProperty
    public float horizontalWeight;
    public int inputFeatures;
    public int layoutInDisplayCutoutMode;
    private int mColorMode;
    private int[] mCompatibilityParamsBackup;
    private CharSequence mTitle;
    @Deprecated
    public int memoryType;
    public int needsMenuKey;
    public int overrideDisplayCutoutMode;
    public String packageName;
    public int preferredDisplayModeId;
    @Deprecated
    public float preferredRefreshRate;
    public boolean preservePreviousSurfaceInsets;
    @ViewDebug.ExportedProperty(flagMapping={@ViewDebug.FlagToString(equals=1, mask=1, name="FAKE_HARDWARE_ACCELERATED"), @ViewDebug.FlagToString(equals=2, mask=2, name="FORCE_HARDWARE_ACCELERATED"), @ViewDebug.FlagToString(equals=4, mask=4, name="WANTS_OFFSET_NOTIFICATIONS"), @ViewDebug.FlagToString(equals=16, mask=16, name="SHOW_FOR_ALL_USERS"), @ViewDebug.FlagToString(equals=64, mask=64, name="NO_MOVE_ANIMATION"), @ViewDebug.FlagToString(equals=128, mask=128, name="COMPATIBLE_WINDOW"), @ViewDebug.FlagToString(equals=256, mask=256, name="SYSTEM_ERROR"), @ViewDebug.FlagToString(equals=512, mask=512, name="INHERIT_TRANSLUCENT_DECOR"), @ViewDebug.FlagToString(equals=1024, mask=1024, name="KEYGUARD"), @ViewDebug.FlagToString(equals=2048, mask=2048, name="DISABLE_WALLPAPER_TOUCH_EVENTS"), @ViewDebug.FlagToString(equals=4096, mask=4096, name="FORCE_STATUS_BAR_VISIBLE_TRANSPARENT"), @ViewDebug.FlagToString(equals=8192, mask=8192, name="PRESERVE_GEOMETRY"), @ViewDebug.FlagToString(equals=16384, mask=16384, name="FORCE_DECOR_VIEW_VISIBILITY"), @ViewDebug.FlagToString(equals=32768, mask=32768, name="WILL_NOT_REPLACE_ON_RELAUNCH"), @ViewDebug.FlagToString(equals=65536, mask=65536, name="LAYOUT_CHILD_WINDOW_IN_PARENT_FRAME"), @ViewDebug.FlagToString(equals=131072, mask=131072, name="FORCE_DRAW_STATUS_BAR_BACKGROUND"), @ViewDebug.FlagToString(equals=262144, mask=262144, name="SUSTAINED_PERFORMANCE_MODE"), @ViewDebug.FlagToString(equals=524288, mask=524288, name="HIDE_NON_SYSTEM_OVERLAY_WINDOWS"), @ViewDebug.FlagToString(equals=1048576, mask=1048576, name="IS_ROUNDED_CORNERS_OVERLAY"), @ViewDebug.FlagToString(equals=2097152, mask=2097152, name="ACQUIRES_SLEEP_TOKEN"), @ViewDebug.FlagToString(equals=4194304, mask=4194304, name="IS_SCREEN_DECOR")})
    public int privateFlags;
    public int rotationAnimation;
    public float screenBrightness;
    public int screenOrientation;
    public int softInputMode;
    public int subtreeSystemUiVisibility;
    public final Rect surfaceInsets;
    public int systemUiVisibility;
    public IBinder token;
    @ViewDebug.ExportedProperty(mapping={@ViewDebug.IntToString(from=1, to="BASE_APPLICATION"), @ViewDebug.IntToString(from=2, to="APPLICATION"), @ViewDebug.IntToString(from=3, to="APPLICATION_STARTING"), @ViewDebug.IntToString(from=4, to="DRAWN_APPLICATION"), @ViewDebug.IntToString(from=1000, to="APPLICATION_PANEL"), @ViewDebug.IntToString(from=1001, to="APPLICATION_MEDIA"), @ViewDebug.IntToString(from=1002, to="APPLICATION_SUB_PANEL"), @ViewDebug.IntToString(from=1005, to="APPLICATION_ABOVE_SUB_PANEL"), @ViewDebug.IntToString(from=1003, to="APPLICATION_ATTACHED_DIALOG"), @ViewDebug.IntToString(from=1004, to="APPLICATION_MEDIA_OVERLAY"), @ViewDebug.IntToString(from=2000, to="STATUS_BAR"), @ViewDebug.IntToString(from=2001, to="SEARCH_BAR"), @ViewDebug.IntToString(from=2002, to="PHONE"), @ViewDebug.IntToString(from=2003, to="SYSTEM_ALERT"), @ViewDebug.IntToString(from=2005, to="TOAST"), @ViewDebug.IntToString(from=2006, to="SYSTEM_OVERLAY"), @ViewDebug.IntToString(from=2007, to="PRIORITY_PHONE"), @ViewDebug.IntToString(from=2008, to="SYSTEM_DIALOG"), @ViewDebug.IntToString(from=2009, to="KEYGUARD_DIALOG"), @ViewDebug.IntToString(from=2010, to="SYSTEM_ERROR"), @ViewDebug.IntToString(from=2011, to="INPUT_METHOD"), @ViewDebug.IntToString(from=2012, to="INPUT_METHOD_DIALOG"), @ViewDebug.IntToString(from=2013, to="WALLPAPER"), @ViewDebug.IntToString(from=2014, to="STATUS_BAR_PANEL"), @ViewDebug.IntToString(from=2015, to="SECURE_SYSTEM_OVERLAY"), @ViewDebug.IntToString(from=2016, to="DRAG"), @ViewDebug.IntToString(from=2017, to="STATUS_BAR_SUB_PANEL"), @ViewDebug.IntToString(from=2018, to="POINTER"), @ViewDebug.IntToString(from=2019, to="NAVIGATION_BAR"), @ViewDebug.IntToString(from=2020, to="VOLUME_OVERLAY"), @ViewDebug.IntToString(from=2021, to="BOOT_PROGRESS"), @ViewDebug.IntToString(from=2022, to="INPUT_CONSUMER"), @ViewDebug.IntToString(from=2023, to="DREAM"), @ViewDebug.IntToString(from=2024, to="NAVIGATION_BAR_PANEL"), @ViewDebug.IntToString(from=2026, to="DISPLAY_OVERLAY"), @ViewDebug.IntToString(from=2027, to="MAGNIFICATION_OVERLAY"), @ViewDebug.IntToString(from=2037, to="PRESENTATION"), @ViewDebug.IntToString(from=2030, to="PRIVATE_PRESENTATION"), @ViewDebug.IntToString(from=2031, to="VOICE_INTERACTION"), @ViewDebug.IntToString(from=2033, to="VOICE_INTERACTION_STARTING"), @ViewDebug.IntToString(from=2034, to="DOCK_DIVIDER"), @ViewDebug.IntToString(from=2035, to="QS_DIALOG"), @ViewDebug.IntToString(from=2036, to="SCREENSHOT"), @ViewDebug.IntToString(from=2038, to="APPLICATION_OVERLAY")})
    public int type;
    public long userActivityTimeout;
    public float verticalMargin;
    @ViewDebug.ExportedProperty
    public float verticalWeight;
    public int windowAnimations;
    @ViewDebug.ExportedProperty
    public int x;
    @ViewDebug.ExportedProperty
    public int y;
    
    public LayoutParams()
    {
      super(-1);
      needsMenuKey = 0;
      surfaceInsets = new Rect();
      preservePreviousSurfaceInsets = true;
      alpha = 1.0F;
      dimAmount = 1.0F;
      screenBrightness = -1.0F;
      buttonBrightness = -1.0F;
      rotationAnimation = 0;
      token = null;
      packageName = null;
      screenOrientation = -1;
      layoutInDisplayCutoutMode = 0;
      overrideDisplayCutoutMode = 0;
      userActivityTimeout = -1L;
      accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
      hideTimeoutMilliseconds = -1L;
      mColorMode = 0;
      mCompatibilityParamsBackup = null;
      mTitle = null;
      type = 2;
      format = -1;
    }
    
    public LayoutParams(int paramInt)
    {
      super(-1);
      needsMenuKey = 0;
      surfaceInsets = new Rect();
      preservePreviousSurfaceInsets = true;
      alpha = 1.0F;
      dimAmount = 1.0F;
      screenBrightness = -1.0F;
      buttonBrightness = -1.0F;
      rotationAnimation = 0;
      token = null;
      packageName = null;
      screenOrientation = -1;
      layoutInDisplayCutoutMode = 0;
      overrideDisplayCutoutMode = 0;
      userActivityTimeout = -1L;
      accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
      hideTimeoutMilliseconds = -1L;
      mColorMode = 0;
      mCompatibilityParamsBackup = null;
      mTitle = null;
      type = paramInt;
      format = -1;
    }
    
    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(-1);
      needsMenuKey = 0;
      surfaceInsets = new Rect();
      preservePreviousSurfaceInsets = true;
      alpha = 1.0F;
      dimAmount = 1.0F;
      screenBrightness = -1.0F;
      buttonBrightness = -1.0F;
      rotationAnimation = 0;
      token = null;
      packageName = null;
      screenOrientation = -1;
      layoutInDisplayCutoutMode = 0;
      overrideDisplayCutoutMode = 0;
      userActivityTimeout = -1L;
      accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
      hideTimeoutMilliseconds = -1L;
      mColorMode = 0;
      mCompatibilityParamsBackup = null;
      mTitle = null;
      type = paramInt1;
      flags = paramInt2;
      format = -1;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3)
    {
      super(-1);
      needsMenuKey = 0;
      surfaceInsets = new Rect();
      preservePreviousSurfaceInsets = true;
      alpha = 1.0F;
      dimAmount = 1.0F;
      screenBrightness = -1.0F;
      buttonBrightness = -1.0F;
      rotationAnimation = 0;
      token = null;
      packageName = null;
      screenOrientation = -1;
      layoutInDisplayCutoutMode = 0;
      overrideDisplayCutoutMode = 0;
      userActivityTimeout = -1L;
      accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
      hideTimeoutMilliseconds = -1L;
      mColorMode = 0;
      mCompatibilityParamsBackup = null;
      mTitle = null;
      type = paramInt1;
      flags = paramInt2;
      format = paramInt3;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      super(paramInt2);
      needsMenuKey = 0;
      surfaceInsets = new Rect();
      preservePreviousSurfaceInsets = true;
      alpha = 1.0F;
      dimAmount = 1.0F;
      screenBrightness = -1.0F;
      buttonBrightness = -1.0F;
      rotationAnimation = 0;
      token = null;
      packageName = null;
      screenOrientation = -1;
      layoutInDisplayCutoutMode = 0;
      overrideDisplayCutoutMode = 0;
      userActivityTimeout = -1L;
      accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
      hideTimeoutMilliseconds = -1L;
      mColorMode = 0;
      mCompatibilityParamsBackup = null;
      mTitle = null;
      type = paramInt3;
      flags = paramInt4;
      format = paramInt5;
    }
    
    public LayoutParams(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7)
    {
      super(paramInt2);
      needsMenuKey = 0;
      surfaceInsets = new Rect();
      preservePreviousSurfaceInsets = true;
      alpha = 1.0F;
      dimAmount = 1.0F;
      screenBrightness = -1.0F;
      buttonBrightness = -1.0F;
      rotationAnimation = 0;
      token = null;
      packageName = null;
      screenOrientation = -1;
      layoutInDisplayCutoutMode = 0;
      overrideDisplayCutoutMode = 0;
      userActivityTimeout = -1L;
      accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
      hideTimeoutMilliseconds = -1L;
      mColorMode = 0;
      mCompatibilityParamsBackup = null;
      mTitle = null;
      x = paramInt3;
      y = paramInt4;
      type = paramInt5;
      flags = paramInt6;
      format = paramInt7;
    }
    
    public LayoutParams(Parcel paramParcel)
    {
      boolean bool1 = false;
      needsMenuKey = 0;
      surfaceInsets = new Rect();
      preservePreviousSurfaceInsets = true;
      alpha = 1.0F;
      dimAmount = 1.0F;
      screenBrightness = -1.0F;
      buttonBrightness = -1.0F;
      rotationAnimation = 0;
      token = null;
      packageName = null;
      screenOrientation = -1;
      layoutInDisplayCutoutMode = 0;
      overrideDisplayCutoutMode = 0;
      userActivityTimeout = -1L;
      accessibilityIdOfAnchor = AccessibilityNodeInfo.UNDEFINED_NODE_ID;
      hideTimeoutMilliseconds = -1L;
      mColorMode = 0;
      mCompatibilityParamsBackup = null;
      mTitle = null;
      width = paramParcel.readInt();
      height = paramParcel.readInt();
      x = paramParcel.readInt();
      y = paramParcel.readInt();
      type = paramParcel.readInt();
      flags = paramParcel.readInt();
      privateFlags = paramParcel.readInt();
      softInputMode = paramParcel.readInt();
      layoutInDisplayCutoutMode = paramParcel.readInt();
      overrideDisplayCutoutMode = paramParcel.readInt();
      gravity = paramParcel.readInt();
      horizontalMargin = paramParcel.readFloat();
      verticalMargin = paramParcel.readFloat();
      format = paramParcel.readInt();
      windowAnimations = paramParcel.readInt();
      alpha = paramParcel.readFloat();
      dimAmount = paramParcel.readFloat();
      screenBrightness = paramParcel.readFloat();
      buttonBrightness = paramParcel.readFloat();
      rotationAnimation = paramParcel.readInt();
      token = paramParcel.readStrongBinder();
      packageName = paramParcel.readString();
      mTitle = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      screenOrientation = paramParcel.readInt();
      preferredRefreshRate = paramParcel.readFloat();
      preferredDisplayModeId = paramParcel.readInt();
      systemUiVisibility = paramParcel.readInt();
      subtreeSystemUiVisibility = paramParcel.readInt();
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      hasSystemUiListeners = bool2;
      inputFeatures = paramParcel.readInt();
      userActivityTimeout = paramParcel.readLong();
      surfaceInsets.left = paramParcel.readInt();
      surfaceInsets.top = paramParcel.readInt();
      surfaceInsets.right = paramParcel.readInt();
      surfaceInsets.bottom = paramParcel.readInt();
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      hasManualSurfaceInsets = bool2;
      boolean bool2 = bool1;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      }
      preservePreviousSurfaceInsets = bool2;
      needsMenuKey = paramParcel.readInt();
      accessibilityIdOfAnchor = paramParcel.readLong();
      accessibilityTitle = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
      mColorMode = paramParcel.readInt();
      hideTimeoutMilliseconds = paramParcel.readLong();
    }
    
    private static String inputFeatureToString(int paramInt)
    {
      if (paramInt != 4)
      {
        switch (paramInt)
        {
        default: 
          return Integer.toString(paramInt);
        case 2: 
          return "NO_INPUT_CHANNEL";
        }
        return "DISABLE_POINTER_GESTURES";
      }
      return "DISABLE_USER_ACTIVITY";
    }
    
    public static boolean isSystemAlertWindowType(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return false;
      }
      return true;
    }
    
    private static String layoutInDisplayCutoutModeToString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("unknown(");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      case 2: 
        return "never";
      case 1: 
        return "always";
      }
      return "default";
    }
    
    public static boolean mayUseInputMethod(int paramInt)
    {
      paramInt &= 0x20008;
      return (paramInt == 0) || (paramInt == 131080);
    }
    
    private static String rotationAnimationToString(int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return Integer.toString(paramInt);
      case 3: 
        return "SEAMLESS";
      case 2: 
        return "JUMPCUT";
      case 1: 
        return "CROSSFADE";
      case 0: 
        return "ROTATE";
      }
      return "UNSPECIFIED";
    }
    
    private static String softInputModeToString(int paramInt)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      int i = paramInt & 0xF;
      if (i != 0)
      {
        localStringBuilder.append("state=");
        switch (i)
        {
        default: 
          localStringBuilder.append(i);
          break;
        case 5: 
          localStringBuilder.append("always_visible");
          break;
        case 4: 
          localStringBuilder.append("visible");
          break;
        case 3: 
          localStringBuilder.append("always_hidden");
          break;
        case 2: 
          localStringBuilder.append("hidden");
          break;
        case 1: 
          localStringBuilder.append("unchanged");
        }
        localStringBuilder.append(' ');
      }
      i = paramInt & 0xF0;
      if (i != 0)
      {
        localStringBuilder.append("adjust=");
        if (i != 16)
        {
          if (i != 32)
          {
            if (i != 48) {
              localStringBuilder.append(i);
            } else {
              localStringBuilder.append("nothing");
            }
          }
          else {
            localStringBuilder.append("pan");
          }
        }
        else {
          localStringBuilder.append("resize");
        }
        localStringBuilder.append(' ');
      }
      if ((paramInt & 0x100) != 0)
      {
        localStringBuilder.append("forwardNavigation");
        localStringBuilder.append(' ');
      }
      localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
      return localStringBuilder.toString();
    }
    
    void backup()
    {
      int[] arrayOfInt1 = mCompatibilityParamsBackup;
      int[] arrayOfInt2 = arrayOfInt1;
      if (arrayOfInt1 == null)
      {
        arrayOfInt2 = new int[4];
        mCompatibilityParamsBackup = arrayOfInt2;
      }
      arrayOfInt2[0] = x;
      arrayOfInt2[1] = y;
      arrayOfInt2[2] = width;
      arrayOfInt2[3] = height;
    }
    
    public final int copyFrom(LayoutParams paramLayoutParams)
    {
      int i = 0;
      if (width != width)
      {
        width = width;
        i = 0x0 | 0x1;
      }
      int j = i;
      if (height != height)
      {
        height = height;
        j = i | 0x1;
      }
      i = j;
      if (x != x)
      {
        x = x;
        i = j | 0x1;
      }
      j = i;
      if (y != y)
      {
        y = y;
        j = i | 0x1;
      }
      i = j;
      if (horizontalWeight != horizontalWeight)
      {
        horizontalWeight = horizontalWeight;
        i = j | 0x1;
      }
      j = i;
      if (verticalWeight != verticalWeight)
      {
        verticalWeight = verticalWeight;
        j = i | 0x1;
      }
      i = j;
      if (horizontalMargin != horizontalMargin)
      {
        horizontalMargin = horizontalMargin;
        i = j | 0x1;
      }
      j = i;
      if (verticalMargin != verticalMargin)
      {
        verticalMargin = verticalMargin;
        j = i | 0x1;
      }
      i = j;
      if (type != type)
      {
        type = type;
        i = j | 0x2;
      }
      j = i;
      if (flags != flags)
      {
        j = i;
        if ((0xC000000 & (flags ^ flags)) != 0) {
          j = i | 0x80000;
        }
        flags = flags;
        j |= 0x4;
      }
      int k = j;
      if (privateFlags != privateFlags)
      {
        privateFlags = privateFlags;
        k = j | 0x20000;
      }
      i = k;
      if (softInputMode != softInputMode)
      {
        softInputMode = softInputMode;
        i = k | 0x200;
      }
      j = i;
      if (layoutInDisplayCutoutMode != layoutInDisplayCutoutMode)
      {
        layoutInDisplayCutoutMode = layoutInDisplayCutoutMode;
        j = i | 0x1;
      }
      i = j;
      if (overrideDisplayCutoutMode != overrideDisplayCutoutMode)
      {
        overrideDisplayCutoutMode = overrideDisplayCutoutMode;
        i = j | 0x1;
      }
      j = i;
      if (gravity != gravity)
      {
        gravity = gravity;
        j = i | 0x1;
      }
      i = j;
      if (format != format)
      {
        format = format;
        i = j | 0x8;
      }
      j = i;
      if (windowAnimations != windowAnimations)
      {
        windowAnimations = windowAnimations;
        j = i | 0x10;
      }
      if (token == null) {
        token = token;
      }
      if (packageName == null) {
        packageName = packageName;
      }
      k = j;
      if (!Objects.equals(mTitle, mTitle))
      {
        k = j;
        if (mTitle != null)
        {
          mTitle = mTitle;
          k = j | 0x40;
        }
      }
      i = k;
      if (alpha != alpha)
      {
        alpha = alpha;
        i = k | 0x80;
      }
      j = i;
      if (dimAmount != dimAmount)
      {
        dimAmount = dimAmount;
        j = i | 0x20;
      }
      k = j;
      if (screenBrightness != screenBrightness)
      {
        screenBrightness = screenBrightness;
        k = j | 0x800;
      }
      i = k;
      if (buttonBrightness != buttonBrightness)
      {
        buttonBrightness = buttonBrightness;
        i = k | 0x2000;
      }
      j = i;
      if (rotationAnimation != rotationAnimation)
      {
        rotationAnimation = rotationAnimation;
        j = i | 0x1000;
      }
      i = j;
      if (screenOrientation != screenOrientation)
      {
        screenOrientation = screenOrientation;
        i = j | 0x400;
      }
      j = i;
      if (preferredRefreshRate != preferredRefreshRate)
      {
        preferredRefreshRate = preferredRefreshRate;
        j = i | 0x200000;
      }
      i = j;
      if (preferredDisplayModeId != preferredDisplayModeId)
      {
        preferredDisplayModeId = preferredDisplayModeId;
        i = j | 0x800000;
      }
      if (systemUiVisibility == systemUiVisibility)
      {
        j = i;
        if (subtreeSystemUiVisibility == subtreeSystemUiVisibility) {}
      }
      else
      {
        systemUiVisibility = systemUiVisibility;
        subtreeSystemUiVisibility = subtreeSystemUiVisibility;
        j = i | 0x4000;
      }
      i = j;
      if (hasSystemUiListeners != hasSystemUiListeners)
      {
        hasSystemUiListeners = hasSystemUiListeners;
        i = j | 0x8000;
      }
      k = i;
      if (inputFeatures != inputFeatures)
      {
        inputFeatures = inputFeatures;
        k = i | 0x10000;
      }
      j = k;
      if (userActivityTimeout != userActivityTimeout)
      {
        userActivityTimeout = userActivityTimeout;
        j = k | 0x40000;
      }
      i = j;
      if (!surfaceInsets.equals(surfaceInsets))
      {
        surfaceInsets.set(surfaceInsets);
        i = j | 0x100000;
      }
      j = i;
      if (hasManualSurfaceInsets != hasManualSurfaceInsets)
      {
        hasManualSurfaceInsets = hasManualSurfaceInsets;
        j = i | 0x100000;
      }
      i = j;
      if (preservePreviousSurfaceInsets != preservePreviousSurfaceInsets)
      {
        preservePreviousSurfaceInsets = preservePreviousSurfaceInsets;
        i = j | 0x100000;
      }
      k = i;
      if (needsMenuKey != needsMenuKey)
      {
        needsMenuKey = needsMenuKey;
        k = i | 0x400000;
      }
      j = k;
      if (accessibilityIdOfAnchor != accessibilityIdOfAnchor)
      {
        accessibilityIdOfAnchor = accessibilityIdOfAnchor;
        j = k | 0x1000000;
      }
      i = j;
      if (!Objects.equals(accessibilityTitle, accessibilityTitle))
      {
        i = j;
        if (accessibilityTitle != null)
        {
          accessibilityTitle = accessibilityTitle;
          i = j | 0x2000000;
        }
      }
      j = i;
      if (mColorMode != mColorMode)
      {
        mColorMode = mColorMode;
        j = i | 0x4000000;
      }
      hideTimeoutMilliseconds = hideTimeoutMilliseconds;
      return j;
    }
    
    public String debug(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("Contents of ");
      localStringBuilder.append(this);
      localStringBuilder.append(":");
      Log.d("Debug", localStringBuilder.toString());
      Log.d("Debug", super.debug(""));
      Log.d("Debug", "");
      paramString = new StringBuilder();
      paramString.append("WindowManager.LayoutParams={title=");
      paramString.append(mTitle);
      paramString.append("}");
      Log.d("Debug", paramString.toString());
      return "";
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void dumpDimensions(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append('(');
      paramStringBuilder.append(x);
      paramStringBuilder.append(',');
      paramStringBuilder.append(y);
      paramStringBuilder.append(")(");
      String str;
      if (width == -1) {
        str = "fill";
      } else if (width == -2) {
        str = "wrap";
      } else {
        str = String.valueOf(width);
      }
      paramStringBuilder.append(str);
      paramStringBuilder.append('x');
      if (height == -1) {
        str = "fill";
      } else if (height == -2) {
        str = "wrap";
      } else {
        str = String.valueOf(height);
      }
      paramStringBuilder.append(str);
      paramStringBuilder.append(")");
    }
    
    protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
    {
      super.encodeProperties(paramViewHierarchyEncoder);
      paramViewHierarchyEncoder.addProperty("x", x);
      paramViewHierarchyEncoder.addProperty("y", y);
      paramViewHierarchyEncoder.addProperty("horizontalWeight", horizontalWeight);
      paramViewHierarchyEncoder.addProperty("verticalWeight", verticalWeight);
      paramViewHierarchyEncoder.addProperty("type", type);
      paramViewHierarchyEncoder.addProperty("flags", flags);
    }
    
    public int getColorMode()
    {
      return mColorMode;
    }
    
    public final CharSequence getTitle()
    {
      Object localObject;
      if (mTitle != null) {
        localObject = mTitle;
      } else {
        localObject = "";
      }
      return localObject;
    }
    
    @SystemApi
    public final long getUserActivityTimeout()
    {
      return userActivityTimeout;
    }
    
    public boolean isFullscreen()
    {
      boolean bool;
      if ((x == 0) && (y == 0) && (width == -1) && (height == -1)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void restore()
    {
      int[] arrayOfInt = mCompatibilityParamsBackup;
      if (arrayOfInt != null)
      {
        x = arrayOfInt[0];
        y = arrayOfInt[1];
        width = arrayOfInt[2];
        height = arrayOfInt[3];
      }
    }
    
    public void scale(float paramFloat)
    {
      x = ((int)(x * paramFloat + 0.5F));
      y = ((int)(y * paramFloat + 0.5F));
      if (width > 0) {
        width = ((int)(width * paramFloat + 0.5F));
      }
      if (height > 0) {
        height = ((int)(height * paramFloat + 0.5F));
      }
    }
    
    public void setColorMode(int paramInt)
    {
      mColorMode = paramInt;
    }
    
    public final void setSurfaceInsets(View paramView, boolean paramBoolean1, boolean paramBoolean2)
    {
      int i = (int)Math.ceil(paramView.getZ() * 2.0F);
      if (i == 0) {
        surfaceInsets.set(0, 0, 0, 0);
      } else {
        surfaceInsets.set(Math.max(i, surfaceInsets.left), Math.max(i, surfaceInsets.top), Math.max(i, surfaceInsets.right), Math.max(i, surfaceInsets.bottom));
      }
      hasManualSurfaceInsets = paramBoolean1;
      preservePreviousSurfaceInsets = paramBoolean2;
    }
    
    public final void setTitle(CharSequence paramCharSequence)
    {
      Object localObject = paramCharSequence;
      if (paramCharSequence == null) {
        localObject = "";
      }
      mTitle = TextUtils.stringOrSpannedString((CharSequence)localObject);
    }
    
    @SystemApi
    public final void setUserActivityTimeout(long paramLong)
    {
      userActivityTimeout = paramLong;
    }
    
    public String toString()
    {
      return toString("");
    }
    
    public String toString(String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder(256);
      localStringBuilder.append('{');
      dumpDimensions(localStringBuilder);
      if (horizontalMargin != 0.0F)
      {
        localStringBuilder.append(" hm=");
        localStringBuilder.append(horizontalMargin);
      }
      if (verticalMargin != 0.0F)
      {
        localStringBuilder.append(" vm=");
        localStringBuilder.append(verticalMargin);
      }
      if (gravity != 0)
      {
        localStringBuilder.append(" gr=");
        localStringBuilder.append(Gravity.toString(gravity));
      }
      if (softInputMode != 0)
      {
        localStringBuilder.append(" sim={");
        localStringBuilder.append(softInputModeToString(softInputMode));
        localStringBuilder.append('}');
      }
      if (layoutInDisplayCutoutMode != 0)
      {
        localStringBuilder.append(" layoutInDisplayCutoutMode=");
        localStringBuilder.append(layoutInDisplayCutoutModeToString(layoutInDisplayCutoutMode));
      }
      if (overrideDisplayCutoutMode != 0)
      {
        localStringBuilder.append(" overrideDisplayCutoutMode=");
        localStringBuilder.append(layoutInDisplayCutoutModeToString(overrideDisplayCutoutMode));
      }
      localStringBuilder.append(" ty=");
      localStringBuilder.append(ViewDebug.intToString(LayoutParams.class, "type", type));
      if (format != -1)
      {
        localStringBuilder.append(" fmt=");
        localStringBuilder.append(PixelFormat.formatToString(format));
      }
      if (windowAnimations != 0)
      {
        localStringBuilder.append(" wanim=0x");
        localStringBuilder.append(Integer.toHexString(windowAnimations));
      }
      if (screenOrientation != -1)
      {
        localStringBuilder.append(" or=");
        localStringBuilder.append(ActivityInfo.screenOrientationToString(screenOrientation));
      }
      if (alpha != 1.0F)
      {
        localStringBuilder.append(" alpha=");
        localStringBuilder.append(alpha);
      }
      if (screenBrightness != -1.0F)
      {
        localStringBuilder.append(" sbrt=");
        localStringBuilder.append(screenBrightness);
      }
      if (buttonBrightness != -1.0F)
      {
        localStringBuilder.append(" bbrt=");
        localStringBuilder.append(buttonBrightness);
      }
      if (rotationAnimation != 0)
      {
        localStringBuilder.append(" rotAnim=");
        localStringBuilder.append(rotationAnimationToString(rotationAnimation));
      }
      if (preferredRefreshRate != 0.0F)
      {
        localStringBuilder.append(" preferredRefreshRate=");
        localStringBuilder.append(preferredRefreshRate);
      }
      if (preferredDisplayModeId != 0)
      {
        localStringBuilder.append(" preferredDisplayMode=");
        localStringBuilder.append(preferredDisplayModeId);
      }
      if (hasSystemUiListeners)
      {
        localStringBuilder.append(" sysuil=");
        localStringBuilder.append(hasSystemUiListeners);
      }
      if (inputFeatures != 0)
      {
        localStringBuilder.append(" if=");
        localStringBuilder.append(inputFeatureToString(inputFeatures));
      }
      if (userActivityTimeout >= 0L)
      {
        localStringBuilder.append(" userActivityTimeout=");
        localStringBuilder.append(userActivityTimeout);
      }
      if ((surfaceInsets.left != 0) || (surfaceInsets.top != 0) || (surfaceInsets.right != 0) || (surfaceInsets.bottom != 0) || (hasManualSurfaceInsets) || (!preservePreviousSurfaceInsets))
      {
        localStringBuilder.append(" surfaceInsets=");
        localStringBuilder.append(surfaceInsets);
        if (hasManualSurfaceInsets) {
          localStringBuilder.append(" (manual)");
        }
        if (!preservePreviousSurfaceInsets) {
          localStringBuilder.append(" (!preservePreviousSurfaceInsets)");
        }
      }
      if (needsMenuKey == 1) {
        localStringBuilder.append(" needsMenuKey");
      }
      if (mColorMode != 0)
      {
        localStringBuilder.append(" colorMode=");
        localStringBuilder.append(ActivityInfo.colorModeToString(mColorMode));
      }
      localStringBuilder.append(System.lineSeparator());
      localStringBuilder.append(paramString);
      localStringBuilder.append("  fl=");
      localStringBuilder.append(ViewDebug.flagsToString(LayoutParams.class, "flags", flags));
      if (privateFlags != 0)
      {
        localStringBuilder.append(System.lineSeparator());
        localStringBuilder.append(paramString);
        localStringBuilder.append("  pfl=");
        localStringBuilder.append(ViewDebug.flagsToString(LayoutParams.class, "privateFlags", privateFlags));
      }
      if (systemUiVisibility != 0)
      {
        localStringBuilder.append(System.lineSeparator());
        localStringBuilder.append(paramString);
        localStringBuilder.append("  sysui=");
        localStringBuilder.append(ViewDebug.flagsToString(View.class, "mSystemUiVisibility", systemUiVisibility));
      }
      if (subtreeSystemUiVisibility != 0)
      {
        localStringBuilder.append(System.lineSeparator());
        localStringBuilder.append(paramString);
        localStringBuilder.append("  vsysui=");
        localStringBuilder.append(ViewDebug.flagsToString(View.class, "mSystemUiVisibility", subtreeSystemUiVisibility));
      }
      localStringBuilder.append('}');
      return localStringBuilder.toString();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeInt(width);
      paramParcel.writeInt(height);
      paramParcel.writeInt(x);
      paramParcel.writeInt(y);
      paramParcel.writeInt(type);
      paramParcel.writeInt(flags);
      paramParcel.writeInt(privateFlags);
      paramParcel.writeInt(softInputMode);
      paramParcel.writeInt(layoutInDisplayCutoutMode);
      paramParcel.writeInt(overrideDisplayCutoutMode);
      paramParcel.writeInt(gravity);
      paramParcel.writeFloat(horizontalMargin);
      paramParcel.writeFloat(verticalMargin);
      paramParcel.writeInt(format);
      paramParcel.writeInt(windowAnimations);
      paramParcel.writeFloat(alpha);
      paramParcel.writeFloat(dimAmount);
      paramParcel.writeFloat(screenBrightness);
      paramParcel.writeFloat(buttonBrightness);
      paramParcel.writeInt(rotationAnimation);
      paramParcel.writeStrongBinder(token);
      paramParcel.writeString(packageName);
      TextUtils.writeToParcel(mTitle, paramParcel, paramInt);
      paramParcel.writeInt(screenOrientation);
      paramParcel.writeFloat(preferredRefreshRate);
      paramParcel.writeInt(preferredDisplayModeId);
      paramParcel.writeInt(systemUiVisibility);
      paramParcel.writeInt(subtreeSystemUiVisibility);
      paramParcel.writeInt(hasSystemUiListeners);
      paramParcel.writeInt(inputFeatures);
      paramParcel.writeLong(userActivityTimeout);
      paramParcel.writeInt(surfaceInsets.left);
      paramParcel.writeInt(surfaceInsets.top);
      paramParcel.writeInt(surfaceInsets.right);
      paramParcel.writeInt(surfaceInsets.bottom);
      paramParcel.writeInt(hasManualSurfaceInsets);
      paramParcel.writeInt(preservePreviousSurfaceInsets);
      paramParcel.writeInt(needsMenuKey);
      paramParcel.writeLong(accessibilityIdOfAnchor);
      TextUtils.writeToParcel(accessibilityTitle, paramParcel, paramInt);
      paramParcel.writeInt(mColorMode);
      paramParcel.writeLong(hideTimeoutMilliseconds);
    }
    
    public void writeToProto(ProtoOutputStream paramProtoOutputStream, long paramLong)
    {
      paramLong = paramProtoOutputStream.start(paramLong);
      paramProtoOutputStream.write(1120986464257L, type);
      paramProtoOutputStream.write(1120986464258L, x);
      paramProtoOutputStream.write(1120986464259L, y);
      paramProtoOutputStream.write(1120986464260L, width);
      paramProtoOutputStream.write(1120986464261L, height);
      paramProtoOutputStream.write(1108101562374L, horizontalMargin);
      paramProtoOutputStream.write(1108101562375L, verticalMargin);
      paramProtoOutputStream.write(1120986464264L, gravity);
      paramProtoOutputStream.write(1120986464265L, softInputMode);
      paramProtoOutputStream.write(1159641169930L, format);
      paramProtoOutputStream.write(1120986464267L, windowAnimations);
      paramProtoOutputStream.write(1108101562380L, alpha);
      paramProtoOutputStream.write(1108101562381L, screenBrightness);
      paramProtoOutputStream.write(1108101562382L, buttonBrightness);
      paramProtoOutputStream.write(1159641169935L, rotationAnimation);
      paramProtoOutputStream.write(1108101562384L, preferredRefreshRate);
      paramProtoOutputStream.write(1120986464273L, preferredDisplayModeId);
      paramProtoOutputStream.write(1133871366162L, hasSystemUiListeners);
      paramProtoOutputStream.write(1155346202643L, inputFeatures);
      paramProtoOutputStream.write(1112396529684L, userActivityTimeout);
      paramProtoOutputStream.write(1159641169942L, needsMenuKey);
      paramProtoOutputStream.write(1159641169943L, mColorMode);
      paramProtoOutputStream.write(1155346202648L, flags);
      paramProtoOutputStream.write(1155346202650L, privateFlags);
      paramProtoOutputStream.write(1155346202651L, systemUiVisibility);
      paramProtoOutputStream.write(1155346202652L, subtreeSystemUiVisibility);
      paramProtoOutputStream.end(paramLong);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    static @interface LayoutInDisplayCutoutMode {}
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface SoftInputModeFlags {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TransitionFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TransitionType {}
}
