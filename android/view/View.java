package android.view;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.content.ClipData;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Canvas.EdgeType;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Interpolator;
import android.graphics.Interpolator.Result;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatProperty;
import android.util.Log;
import android.util.LongArray;
import android.util.LongSparseLongArray;
import android.util.Pools.SynchronizedPool;
import android.util.Property;
import android.util.SeempLog;
import android.util.SparseArray;
import android.util.StateSet;
import android.util.SuperNotCalledException;
import android.util.TypedValue;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityEventSource;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ScrollBarDrawable;
import com.android.internal.R.styleable;
import com.android.internal.view.TooltipPopup;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.widget.ScrollBarUtils;
import com.google.android.collect.Lists;
import com.google.android.collect.Maps;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class View
  implements Drawable.Callback, KeyEvent.Callback, AccessibilityEventSource
{
  public static final int ACCESSIBILITY_CURSOR_POSITION_UNDEFINED = -1;
  public static final int ACCESSIBILITY_LIVE_REGION_ASSERTIVE = 2;
  static final int ACCESSIBILITY_LIVE_REGION_DEFAULT = 0;
  public static final int ACCESSIBILITY_LIVE_REGION_NONE = 0;
  public static final int ACCESSIBILITY_LIVE_REGION_POLITE = 1;
  static final int ALL_RTL_PROPERTIES_RESOLVED = 1610678816;
  public static final Property<View, Float> ALPHA;
  public static final int AUTOFILL_FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 1;
  private static final int[] AUTOFILL_HIGHLIGHT_ATTR;
  public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate";
  public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_DAY = "creditCardExpirationDay";
  public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_MONTH = "creditCardExpirationMonth";
  public static final String AUTOFILL_HINT_CREDIT_CARD_EXPIRATION_YEAR = "creditCardExpirationYear";
  public static final String AUTOFILL_HINT_CREDIT_CARD_NUMBER = "creditCardNumber";
  public static final String AUTOFILL_HINT_CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode";
  public static final String AUTOFILL_HINT_EMAIL_ADDRESS = "emailAddress";
  public static final String AUTOFILL_HINT_NAME = "name";
  public static final String AUTOFILL_HINT_PASSWORD = "password";
  public static final String AUTOFILL_HINT_PHONE = "phone";
  public static final String AUTOFILL_HINT_POSTAL_ADDRESS = "postalAddress";
  public static final String AUTOFILL_HINT_POSTAL_CODE = "postalCode";
  public static final String AUTOFILL_HINT_USERNAME = "username";
  public static final int AUTOFILL_TYPE_DATE = 4;
  public static final int AUTOFILL_TYPE_LIST = 3;
  public static final int AUTOFILL_TYPE_NONE = 0;
  public static final int AUTOFILL_TYPE_TEXT = 1;
  public static final int AUTOFILL_TYPE_TOGGLE = 2;
  static final int CLICKABLE = 16384;
  static final int CONTEXT_CLICKABLE = 8388608;
  private static final boolean DBG = false;
  static final int DEBUG_CORNERS_COLOR;
  static final int DEBUG_CORNERS_SIZE_DIP = 8;
  public static boolean DEBUG_DRAW = false;
  public static final String DEBUG_LAYOUT_PROPERTY = "debug.layout";
  static final int DISABLED = 32;
  public static final int DRAG_FLAG_GLOBAL = 256;
  public static final int DRAG_FLAG_GLOBAL_PERSISTABLE_URI_PERMISSION = 64;
  public static final int DRAG_FLAG_GLOBAL_PREFIX_URI_PERMISSION = 128;
  public static final int DRAG_FLAG_GLOBAL_URI_READ = 1;
  public static final int DRAG_FLAG_GLOBAL_URI_WRITE = 2;
  public static final int DRAG_FLAG_OPAQUE = 512;
  static final int DRAG_MASK = 3;
  static final int DRAWING_CACHE_ENABLED = 32768;
  @Deprecated
  public static final int DRAWING_CACHE_QUALITY_AUTO = 0;
  private static final int[] DRAWING_CACHE_QUALITY_FLAGS;
  @Deprecated
  public static final int DRAWING_CACHE_QUALITY_HIGH = 1048576;
  @Deprecated
  public static final int DRAWING_CACHE_QUALITY_LOW = 524288;
  static final int DRAWING_CACHE_QUALITY_MASK = 1572864;
  static final int DRAW_MASK = 128;
  static final int DUPLICATE_PARENT_STATE = 4194304;
  protected static final int[] EMPTY_STATE_SET;
  static final int ENABLED = 0;
  protected static final int[] ENABLED_FOCUSED_SELECTED_STATE_SET;
  protected static final int[] ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] ENABLED_FOCUSED_STATE_SET;
  protected static final int[] ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET;
  static final int ENABLED_MASK = 32;
  protected static final int[] ENABLED_SELECTED_STATE_SET;
  protected static final int[] ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] ENABLED_STATE_SET;
  protected static final int[] ENABLED_WINDOW_FOCUSED_STATE_SET;
  static final int FADING_EDGE_HORIZONTAL = 4096;
  static final int FADING_EDGE_MASK = 12288;
  static final int FADING_EDGE_NONE = 0;
  static final int FADING_EDGE_VERTICAL = 8192;
  static final int FILTER_TOUCHES_WHEN_OBSCURED = 1024;
  public static final int FIND_VIEWS_WITH_ACCESSIBILITY_NODE_PROVIDERS = 4;
  public static final int FIND_VIEWS_WITH_CONTENT_DESCRIPTION = 2;
  public static final int FIND_VIEWS_WITH_TEXT = 1;
  private static final int FITS_SYSTEM_WINDOWS = 2;
  public static final int FOCUSABLE = 1;
  public static final int FOCUSABLES_ALL = 0;
  public static final int FOCUSABLES_TOUCH_MODE = 1;
  public static final int FOCUSABLE_AUTO = 16;
  static final int FOCUSABLE_IN_TOUCH_MODE = 262144;
  private static final int FOCUSABLE_MASK = 17;
  protected static final int[] FOCUSED_SELECTED_STATE_SET;
  protected static final int[] FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] FOCUSED_STATE_SET;
  protected static final int[] FOCUSED_WINDOW_FOCUSED_STATE_SET;
  public static final int FOCUS_BACKWARD = 1;
  public static final int FOCUS_DOWN = 130;
  public static final int FOCUS_FORWARD = 2;
  public static final int FOCUS_LEFT = 17;
  public static final int FOCUS_RIGHT = 66;
  public static final int FOCUS_UP = 33;
  public static final int GONE = 8;
  public static final int HAPTIC_FEEDBACK_ENABLED = 268435456;
  public static final int IMPORTANT_FOR_ACCESSIBILITY_AUTO = 0;
  static final int IMPORTANT_FOR_ACCESSIBILITY_DEFAULT = 0;
  public static final int IMPORTANT_FOR_ACCESSIBILITY_NO = 2;
  public static final int IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS = 4;
  public static final int IMPORTANT_FOR_ACCESSIBILITY_YES = 1;
  public static final int IMPORTANT_FOR_AUTOFILL_AUTO = 0;
  public static final int IMPORTANT_FOR_AUTOFILL_NO = 2;
  public static final int IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS = 8;
  public static final int IMPORTANT_FOR_AUTOFILL_YES = 1;
  public static final int IMPORTANT_FOR_AUTOFILL_YES_EXCLUDE_DESCENDANTS = 4;
  public static final int INVISIBLE = 4;
  public static final int KEEP_SCREEN_ON = 67108864;
  public static final int LAST_APP_AUTOFILL_ID = 1073741823;
  public static final int LAYER_TYPE_HARDWARE = 2;
  public static final int LAYER_TYPE_NONE = 0;
  public static final int LAYER_TYPE_SOFTWARE = 1;
  private static final int LAYOUT_DIRECTION_DEFAULT = 2;
  private static final int[] LAYOUT_DIRECTION_FLAGS;
  public static final int LAYOUT_DIRECTION_INHERIT = 2;
  public static final int LAYOUT_DIRECTION_LOCALE = 3;
  public static final int LAYOUT_DIRECTION_LTR = 0;
  static final int LAYOUT_DIRECTION_RESOLVED_DEFAULT = 0;
  public static final int LAYOUT_DIRECTION_RTL = 1;
  public static final int LAYOUT_DIRECTION_UNDEFINED = -1;
  static final int LONG_CLICKABLE = 2097152;
  public static final int MEASURED_HEIGHT_STATE_SHIFT = 16;
  public static final int MEASURED_SIZE_MASK = 16777215;
  public static final int MEASURED_STATE_MASK = -16777216;
  public static final int MEASURED_STATE_TOO_SMALL = 16777216;
  public static final int NAVIGATION_BAR_TRANSIENT = 134217728;
  public static final int NAVIGATION_BAR_TRANSLUCENT = Integer.MIN_VALUE;
  public static final int NAVIGATION_BAR_TRANSPARENT = 32768;
  public static final int NAVIGATION_BAR_UNHIDE = 536870912;
  public static final int NOT_FOCUSABLE = 0;
  public static final int NO_ID = -1;
  static final int OPTIONAL_FITS_SYSTEM_WINDOWS = 2048;
  public static final int OVER_SCROLL_ALWAYS = 0;
  public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
  public static final int OVER_SCROLL_NEVER = 2;
  static final int PARENT_SAVE_DISABLED = 536870912;
  static final int PARENT_SAVE_DISABLED_MASK = 536870912;
  static final int PFLAG2_ACCESSIBILITY_FOCUSED = 67108864;
  static final int PFLAG2_ACCESSIBILITY_LIVE_REGION_MASK = 25165824;
  static final int PFLAG2_ACCESSIBILITY_LIVE_REGION_SHIFT = 23;
  static final int PFLAG2_DRAG_CAN_ACCEPT = 1;
  static final int PFLAG2_DRAG_HOVERED = 2;
  static final int PFLAG2_DRAWABLE_RESOLVED = 1073741824;
  static final int PFLAG2_HAS_TRANSIENT_STATE = Integer.MIN_VALUE;
  static final int PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_MASK = 7340032;
  static final int PFLAG2_IMPORTANT_FOR_ACCESSIBILITY_SHIFT = 20;
  static final int PFLAG2_LAYOUT_DIRECTION_MASK = 12;
  static final int PFLAG2_LAYOUT_DIRECTION_MASK_SHIFT = 2;
  static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED = 32;
  static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED_MASK = 48;
  static final int PFLAG2_LAYOUT_DIRECTION_RESOLVED_RTL = 16;
  static final int PFLAG2_PADDING_RESOLVED = 536870912;
  static final int PFLAG2_SUBTREE_ACCESSIBILITY_STATE_CHANGED = 134217728;
  private static final int[] PFLAG2_TEXT_ALIGNMENT_FLAGS;
  static final int PFLAG2_TEXT_ALIGNMENT_MASK = 57344;
  static final int PFLAG2_TEXT_ALIGNMENT_MASK_SHIFT = 13;
  static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED = 65536;
  private static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_DEFAULT = 131072;
  static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK = 917504;
  static final int PFLAG2_TEXT_ALIGNMENT_RESOLVED_MASK_SHIFT = 17;
  private static final int[] PFLAG2_TEXT_DIRECTION_FLAGS;
  static final int PFLAG2_TEXT_DIRECTION_MASK = 448;
  static final int PFLAG2_TEXT_DIRECTION_MASK_SHIFT = 6;
  static final int PFLAG2_TEXT_DIRECTION_RESOLVED = 512;
  static final int PFLAG2_TEXT_DIRECTION_RESOLVED_DEFAULT = 1024;
  static final int PFLAG2_TEXT_DIRECTION_RESOLVED_MASK = 7168;
  static final int PFLAG2_TEXT_DIRECTION_RESOLVED_MASK_SHIFT = 10;
  static final int PFLAG2_VIEW_QUICK_REJECTED = 268435456;
  private static final int PFLAG3_ACCESSIBILITY_HEADING = Integer.MIN_VALUE;
  private static final int PFLAG3_AGGREGATED_VISIBLE = 536870912;
  static final int PFLAG3_APPLYING_INSETS = 32;
  static final int PFLAG3_ASSIST_BLOCKED = 16384;
  private static final int PFLAG3_AUTOFILLID_EXPLICITLY_SET = 1073741824;
  static final int PFLAG3_CALLED_SUPER = 16;
  private static final int PFLAG3_CLUSTER = 32768;
  private static final int PFLAG3_FINGER_DOWN = 131072;
  static final int PFLAG3_FITTING_SYSTEM_WINDOWS = 64;
  private static final int PFLAG3_FOCUSED_BY_DEFAULT = 262144;
  private static final int PFLAG3_HAS_OVERLAPPING_RENDERING_FORCED = 16777216;
  static final int PFLAG3_IMPORTANT_FOR_AUTOFILL_MASK = 7864320;
  static final int PFLAG3_IMPORTANT_FOR_AUTOFILL_SHIFT = 19;
  private static final int PFLAG3_IS_AUTOFILLED = 65536;
  static final int PFLAG3_IS_LAID_OUT = 4;
  static final int PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT = 8;
  static final int PFLAG3_NESTED_SCROLLING_ENABLED = 128;
  static final int PFLAG3_NOTIFY_AUTOFILL_ENTER_ON_LAYOUT = 134217728;
  private static final int PFLAG3_NO_REVEAL_ON_FOCUS = 67108864;
  private static final int PFLAG3_OVERLAPPING_RENDERING_FORCED_VALUE = 8388608;
  private static final int PFLAG3_SCREEN_READER_FOCUSABLE = 268435456;
  static final int PFLAG3_SCROLL_INDICATOR_BOTTOM = 512;
  static final int PFLAG3_SCROLL_INDICATOR_END = 8192;
  static final int PFLAG3_SCROLL_INDICATOR_LEFT = 1024;
  static final int PFLAG3_SCROLL_INDICATOR_RIGHT = 2048;
  static final int PFLAG3_SCROLL_INDICATOR_START = 4096;
  static final int PFLAG3_SCROLL_INDICATOR_TOP = 256;
  static final int PFLAG3_TEMPORARY_DETACH = 33554432;
  static final int PFLAG3_VIEW_IS_ANIMATING_ALPHA = 2;
  static final int PFLAG3_VIEW_IS_ANIMATING_TRANSFORM = 1;
  static final int PFLAG_ACTIVATED = 1073741824;
  static final int PFLAG_ALPHA_SET = 262144;
  static final int PFLAG_ANIMATION_STARTED = 65536;
  private static final int PFLAG_AWAKEN_SCROLL_BARS_ON_ATTACH = 134217728;
  static final int PFLAG_CANCEL_NEXT_UP_EVENT = 67108864;
  static final int PFLAG_DIRTY = 2097152;
  static final int PFLAG_DIRTY_MASK = 6291456;
  static final int PFLAG_DIRTY_OPAQUE = 4194304;
  static final int PFLAG_DRAWABLE_STATE_DIRTY = 1024;
  static final int PFLAG_DRAWING_CACHE_VALID = 32768;
  static final int PFLAG_DRAWN = 32;
  static final int PFLAG_DRAW_ANIMATION = 64;
  static final int PFLAG_FOCUSED = 2;
  static final int PFLAG_FORCE_LAYOUT = 4096;
  static final int PFLAG_HAS_BOUNDS = 16;
  private static final int PFLAG_HOVERED = 268435456;
  static final int PFLAG_INVALIDATED = Integer.MIN_VALUE;
  static final int PFLAG_IS_ROOT_NAMESPACE = 8;
  static final int PFLAG_LAYOUT_REQUIRED = 8192;
  static final int PFLAG_MEASURED_DIMENSION_SET = 2048;
  private static final int PFLAG_NOTIFY_AUTOFILL_MANAGER_ON_CLICK = 536870912;
  static final int PFLAG_OPAQUE_BACKGROUND = 8388608;
  static final int PFLAG_OPAQUE_MASK = 25165824;
  static final int PFLAG_OPAQUE_SCROLLBARS = 16777216;
  private static final int PFLAG_PREPRESSED = 33554432;
  private static final int PFLAG_PRESSED = 16384;
  static final int PFLAG_REQUEST_TRANSPARENT_REGIONS = 512;
  private static final int PFLAG_SAVE_STATE_CALLED = 131072;
  static final int PFLAG_SCROLL_CONTAINER = 524288;
  static final int PFLAG_SCROLL_CONTAINER_ADDED = 1048576;
  static final int PFLAG_SELECTED = 4;
  static final int PFLAG_SKIP_DRAW = 128;
  static final int PFLAG_WANTS_FOCUS = 1;
  private static final int POPULATING_ACCESSIBILITY_EVENT_TYPES = 172479;
  protected static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_STATE_SET;
  protected static final int[] PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_ENABLED_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_ENABLED_SELECTED_STATE_SET;
  protected static final int[] PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_ENABLED_STATE_SET;
  protected static final int[] PRESSED_ENABLED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_FOCUSED_SELECTED_STATE_SET;
  protected static final int[] PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_FOCUSED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_SELECTED_STATE_SET;
  protected static final int[] PRESSED_SELECTED_WINDOW_FOCUSED_STATE_SET;
  protected static final int[] PRESSED_STATE_SET;
  protected static final int[] PRESSED_WINDOW_FOCUSED_STATE_SET;
  private static final int PROVIDER_BACKGROUND = 0;
  private static final int PROVIDER_BOUNDS = 2;
  private static final int PROVIDER_NONE = 1;
  private static final int PROVIDER_PADDED_BOUNDS = 3;
  public static final int PUBLIC_STATUS_BAR_VISIBILITY_MASK = 16375;
  public static final Property<View, Float> ROTATION = new FloatProperty("rotation")
  {
    public Float get(View paramAnonymousView)
    {
      return Float.valueOf(paramAnonymousView.getRotation());
    }
    
    public void setValue(View paramAnonymousView, float paramAnonymousFloat)
    {
      paramAnonymousView.setRotation(paramAnonymousFloat);
    }
  };
  public static final Property<View, Float> ROTATION_X = new FloatProperty("rotationX")
  {
    public Float get(View paramAnonymousView)
    {
      return Float.valueOf(paramAnonymousView.getRotationX());
    }
    
    public void setValue(View paramAnonymousView, float paramAnonymousFloat)
    {
      paramAnonymousView.setRotationX(paramAnonymousFloat);
    }
  };
  public static final Property<View, Float> ROTATION_Y = new FloatProperty("rotationY")
  {
    public Float get(View paramAnonymousView)
    {
      return Float.valueOf(paramAnonymousView.getRotationY());
    }
    
    public void setValue(View paramAnonymousView, float paramAnonymousFloat)
    {
      paramAnonymousView.setRotationY(paramAnonymousFloat);
    }
  };
  static final int SAVE_DISABLED = 65536;
  static final int SAVE_DISABLED_MASK = 65536;
  public static final Property<View, Float> SCALE_X = new FloatProperty("scaleX")
  {
    public Float get(View paramAnonymousView)
    {
      return Float.valueOf(paramAnonymousView.getScaleX());
    }
    
    public void setValue(View paramAnonymousView, float paramAnonymousFloat)
    {
      paramAnonymousView.setScaleX(paramAnonymousFloat);
    }
  };
  public static final Property<View, Float> SCALE_Y = new FloatProperty("scaleY")
  {
    public Float get(View paramAnonymousView)
    {
      return Float.valueOf(paramAnonymousView.getScaleY());
    }
    
    public void setValue(View paramAnonymousView, float paramAnonymousFloat)
    {
      paramAnonymousView.setScaleY(paramAnonymousFloat);
    }
  };
  public static final int SCREEN_STATE_OFF = 0;
  public static final int SCREEN_STATE_ON = 1;
  static final int SCROLLBARS_HORIZONTAL = 256;
  static final int SCROLLBARS_INSET_MASK = 16777216;
  public static final int SCROLLBARS_INSIDE_INSET = 16777216;
  public static final int SCROLLBARS_INSIDE_OVERLAY = 0;
  static final int SCROLLBARS_MASK = 768;
  static final int SCROLLBARS_NONE = 0;
  public static final int SCROLLBARS_OUTSIDE_INSET = 50331648;
  static final int SCROLLBARS_OUTSIDE_MASK = 33554432;
  public static final int SCROLLBARS_OUTSIDE_OVERLAY = 33554432;
  static final int SCROLLBARS_STYLE_MASK = 50331648;
  static final int SCROLLBARS_VERTICAL = 512;
  public static final int SCROLLBAR_POSITION_DEFAULT = 0;
  public static final int SCROLLBAR_POSITION_LEFT = 1;
  public static final int SCROLLBAR_POSITION_RIGHT = 2;
  public static final int SCROLL_AXIS_HORIZONTAL = 1;
  public static final int SCROLL_AXIS_NONE = 0;
  public static final int SCROLL_AXIS_VERTICAL = 2;
  static final int SCROLL_INDICATORS_NONE = 0;
  static final int SCROLL_INDICATORS_PFLAG3_MASK = 16128;
  static final int SCROLL_INDICATORS_TO_PFLAGS3_LSHIFT = 8;
  public static final int SCROLL_INDICATOR_BOTTOM = 2;
  public static final int SCROLL_INDICATOR_END = 32;
  public static final int SCROLL_INDICATOR_LEFT = 4;
  public static final int SCROLL_INDICATOR_RIGHT = 8;
  public static final int SCROLL_INDICATOR_START = 16;
  public static final int SCROLL_INDICATOR_TOP = 1;
  protected static final int[] SELECTED_STATE_SET;
  protected static final int[] SELECTED_WINDOW_FOCUSED_STATE_SET;
  public static final int SOUND_EFFECTS_ENABLED = 134217728;
  public static final int STATUS_BAR_DISABLE_BACK = 4194304;
  public static final int STATUS_BAR_DISABLE_CLOCK = 8388608;
  public static final int STATUS_BAR_DISABLE_EXPAND = 65536;
  public static final int STATUS_BAR_DISABLE_HOME = 2097152;
  public static final int STATUS_BAR_DISABLE_NOTIFICATION_ALERTS = 262144;
  public static final int STATUS_BAR_DISABLE_NOTIFICATION_ICONS = 131072;
  public static final int STATUS_BAR_DISABLE_NOTIFICATION_TICKER = 524288;
  public static final int STATUS_BAR_DISABLE_RECENT = 16777216;
  public static final int STATUS_BAR_DISABLE_SEARCH = 33554432;
  public static final int STATUS_BAR_DISABLE_SYSTEM_INFO = 1048576;
  @Deprecated
  public static final int STATUS_BAR_HIDDEN = 1;
  public static final int STATUS_BAR_TRANSIENT = 67108864;
  public static final int STATUS_BAR_TRANSLUCENT = 1073741824;
  public static final int STATUS_BAR_TRANSPARENT = 8;
  public static final int STATUS_BAR_UNHIDE = 268435456;
  @Deprecated
  public static final int STATUS_BAR_VISIBLE = 0;
  public static final int SYSTEM_UI_CLEARABLE_FLAGS = 7;
  public static final int SYSTEM_UI_FLAG_FULLSCREEN = 4;
  public static final int SYSTEM_UI_FLAG_HIDE_NAVIGATION = 2;
  public static final int SYSTEM_UI_FLAG_HIDE_NAVIGATION_FOREVER = 64;
  public static final int SYSTEM_UI_FLAG_IMMERSIVE = 2048;
  public static final int SYSTEM_UI_FLAG_IMMERSIVE_STICKY = 4096;
  public static final int SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN = 1024;
  public static final int SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION = 512;
  public static final int SYSTEM_UI_FLAG_LAYOUT_STABLE = 256;
  public static final int SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR = 16;
  public static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 8192;
  public static final int SYSTEM_UI_FLAG_LOW_PROFILE = 1;
  public static final int SYSTEM_UI_FLAG_USER_REQUESTED = 32;
  public static final int SYSTEM_UI_FLAG_VISIBLE = 0;
  public static final int SYSTEM_UI_LAYOUT_FLAGS = 1536;
  private static final int SYSTEM_UI_RESERVED_LEGACY1 = 16384;
  private static final int SYSTEM_UI_RESERVED_LEGACY2 = 65536;
  public static final int SYSTEM_UI_TRANSPARENT = 32776;
  public static final int TEXT_ALIGNMENT_CENTER = 4;
  private static final int TEXT_ALIGNMENT_DEFAULT = 1;
  public static final int TEXT_ALIGNMENT_GRAVITY = 1;
  public static final int TEXT_ALIGNMENT_INHERIT = 0;
  static final int TEXT_ALIGNMENT_RESOLVED_DEFAULT = 1;
  public static final int TEXT_ALIGNMENT_TEXT_END = 3;
  public static final int TEXT_ALIGNMENT_TEXT_START = 2;
  public static final int TEXT_ALIGNMENT_VIEW_END = 6;
  public static final int TEXT_ALIGNMENT_VIEW_START = 5;
  public static final int TEXT_DIRECTION_ANY_RTL = 2;
  private static final int TEXT_DIRECTION_DEFAULT = 0;
  public static final int TEXT_DIRECTION_FIRST_STRONG = 1;
  public static final int TEXT_DIRECTION_FIRST_STRONG_LTR = 6;
  public static final int TEXT_DIRECTION_FIRST_STRONG_RTL = 7;
  public static final int TEXT_DIRECTION_INHERIT = 0;
  public static final int TEXT_DIRECTION_LOCALE = 5;
  public static final int TEXT_DIRECTION_LTR = 3;
  static final int TEXT_DIRECTION_RESOLVED_DEFAULT = 1;
  public static final int TEXT_DIRECTION_RTL = 4;
  static final int TOOLTIP = 1073741824;
  public static final Property<View, Float> TRANSLATION_X;
  public static final Property<View, Float> TRANSLATION_Y;
  public static final Property<View, Float> TRANSLATION_Z;
  private static final int UNDEFINED_PADDING = Integer.MIN_VALUE;
  protected static final String VIEW_LOG_TAG = "View";
  private static final int[] VISIBILITY_FLAGS;
  static final int VISIBILITY_MASK = 12;
  public static final int VISIBLE = 0;
  static final int WILL_NOT_CACHE_DRAWING = 131072;
  static final int WILL_NOT_DRAW = 128;
  protected static final int[] WINDOW_FOCUSED_STATE_SET;
  public static final Property<View, Float> X;
  public static final Property<View, Float> Y;
  public static final Property<View, Float> Z;
  private static SparseArray<String> mAttributeMap;
  public static boolean mDebugViewAttributes = false;
  private static boolean sAcceptZeroSizeDragShadow;
  private static boolean sAlwaysAssignFocus;
  private static boolean sAlwaysRemeasureExactly;
  private static boolean sAutoFocusableOffUIThreadWontNotifyParents;
  private static boolean sCanFocusZeroSized;
  static boolean sCascadedDragDrop;
  private static boolean sCompatibilityDone;
  private static Paint sDebugPaint;
  static boolean sHasFocusableExcludeAutoFocusable;
  private static boolean sIgnoreMeasureCache;
  private static boolean sLayoutParamsAlwaysChanged;
  private static int sNextAccessibilityViewId;
  private static final AtomicInteger sNextGeneratedId;
  protected static boolean sPreserveMarginParamsInLayoutParamConversion;
  static boolean sTextureViewIgnoresDrawableSetters;
  static final ThreadLocal<Rect> sThreadLocal;
  private static boolean sThrowOnInvalidFloatProperties;
  private static boolean sUseBrokenMakeMeasureSpec;
  private static boolean sUseDefaultFocusHighlight;
  static boolean sUseZeroUnspecifiedMeasureSpec;
  private int mAccessibilityCursorPosition;
  AccessibilityDelegate mAccessibilityDelegate;
  private CharSequence mAccessibilityPaneTitle;
  private int mAccessibilityTraversalAfterId;
  private int mAccessibilityTraversalBeforeId;
  private int mAccessibilityViewId;
  private ViewPropertyAnimator mAnimator;
  AttachInfo mAttachInfo;
  @ViewDebug.ExportedProperty(category="attributes", hasAdjacentMapping=true)
  public String[] mAttributes;
  private String[] mAutofillHints;
  private AutofillId mAutofillId;
  private int mAutofillViewId;
  @ViewDebug.ExportedProperty(deepExport=true, prefix="bg_")
  private Drawable mBackground;
  private RenderNode mBackgroundRenderNode;
  private int mBackgroundResource;
  private boolean mBackgroundSizeChanged;
  private TintInfo mBackgroundTint;
  @ViewDebug.ExportedProperty(category="layout")
  protected int mBottom;
  public boolean mCachingFailed;
  @ViewDebug.ExportedProperty(category="drawing")
  Rect mClipBounds;
  private CharSequence mContentDescription;
  @ViewDebug.ExportedProperty(deepExport=true)
  protected Context mContext;
  protected Animation mCurrentAnimation;
  private Drawable mDefaultFocusHighlight;
  private Drawable mDefaultFocusHighlightCache;
  boolean mDefaultFocusHighlightEnabled;
  private boolean mDefaultFocusHighlightSizeChanged;
  private int[] mDrawableState;
  private Bitmap mDrawingCache;
  private int mDrawingCacheBackgroundColor;
  private ViewTreeObserver mFloatingTreeObserver;
  @ViewDebug.ExportedProperty(deepExport=true, prefix="fg_")
  private ForegroundInfo mForegroundInfo;
  private ArrayList<FrameMetricsObserver> mFrameMetricsObservers;
  GhostView mGhostView;
  private boolean mHasPerformedLongPress;
  @ViewDebug.ExportedProperty(resolveId=true)
  int mID;
  private boolean mIgnoreNextUpEvent;
  private boolean mInContextButtonPress;
  protected final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
  private SparseArray<Object> mKeyedTags;
  private int mLabelForId;
  private boolean mLastIsOpaque;
  Paint mLayerPaint;
  @ViewDebug.ExportedProperty(category="drawing", mapping={@ViewDebug.IntToString(from=0, to="NONE"), @ViewDebug.IntToString(from=1, to="SOFTWARE"), @ViewDebug.IntToString(from=2, to="HARDWARE")})
  int mLayerType;
  private Insets mLayoutInsets;
  protected ViewGroup.LayoutParams mLayoutParams;
  @ViewDebug.ExportedProperty(category="layout")
  protected int mLeft;
  private boolean mLeftPaddingDefined;
  ListenerInfo mListenerInfo;
  private float mLongClickX;
  private float mLongClickY;
  private MatchIdPredicate mMatchIdPredicate;
  private MatchLabelForPredicate mMatchLabelForPredicate;
  private LongSparseLongArray mMeasureCache;
  @ViewDebug.ExportedProperty(category="measurement")
  int mMeasuredHeight;
  @ViewDebug.ExportedProperty(category="measurement")
  int mMeasuredWidth;
  @ViewDebug.ExportedProperty(category="measurement")
  private int mMinHeight;
  @ViewDebug.ExportedProperty(category="measurement")
  private int mMinWidth;
  private ViewParent mNestedScrollingParent;
  int mNextClusterForwardId;
  private int mNextFocusDownId;
  int mNextFocusForwardId;
  private int mNextFocusLeftId;
  private int mNextFocusRightId;
  private int mNextFocusUpId;
  int mOldHeightMeasureSpec;
  int mOldWidthMeasureSpec;
  ViewOutlineProvider mOutlineProvider;
  private int mOverScrollMode;
  ViewOverlay mOverlay;
  @ViewDebug.ExportedProperty(category="padding")
  protected int mPaddingBottom;
  @ViewDebug.ExportedProperty(category="padding")
  protected int mPaddingLeft;
  @ViewDebug.ExportedProperty(category="padding")
  protected int mPaddingRight;
  @ViewDebug.ExportedProperty(category="padding")
  protected int mPaddingTop;
  protected ViewParent mParent;
  private CheckForLongPress mPendingCheckForLongPress;
  private CheckForTap mPendingCheckForTap;
  private PerformClick mPerformClick;
  private PointerIcon mPointerIcon;
  @ViewDebug.ExportedProperty(flagMapping={@ViewDebug.FlagToString(equals=4096, mask=4096, name="FORCE_LAYOUT"), @ViewDebug.FlagToString(equals=8192, mask=8192, name="LAYOUT_REQUIRED"), @ViewDebug.FlagToString(equals=32768, mask=32768, name="DRAWING_CACHE_INVALID", outputIf=false), @ViewDebug.FlagToString(equals=32, mask=32, name="DRAWN", outputIf=true), @ViewDebug.FlagToString(equals=32, mask=32, name="NOT_DRAWN", outputIf=false), @ViewDebug.FlagToString(equals=4194304, mask=6291456, name="DIRTY_OPAQUE"), @ViewDebug.FlagToString(equals=2097152, mask=6291456, name="DIRTY")}, formatToHexString=true)
  public int mPrivateFlags;
  int mPrivateFlags2;
  int mPrivateFlags3;
  boolean mRecreateDisplayList;
  final RenderNode mRenderNode;
  private final Resources mResources;
  @ViewDebug.ExportedProperty(category="layout")
  protected int mRight;
  private boolean mRightPaddingDefined;
  private RoundScrollbarRenderer mRoundScrollbarRenderer;
  private HandlerActionQueue mRunQueue;
  private ScrollabilityCache mScrollCache;
  private Drawable mScrollIndicatorDrawable;
  @ViewDebug.ExportedProperty(category="scrolling")
  protected int mScrollX;
  @ViewDebug.ExportedProperty(category="scrolling")
  protected int mScrollY;
  private SendViewScrolledAccessibilityEvent mSendViewScrolledAccessibilityEvent;
  private boolean mSendingHoverAccessibilityEvents;
  String mStartActivityRequestWho;
  private StateListAnimator mStateListAnimator;
  @ViewDebug.ExportedProperty(flagMapping={@ViewDebug.FlagToString(equals=1, mask=1, name="LOW_PROFILE"), @ViewDebug.FlagToString(equals=2, mask=2, name="HIDE_NAVIGATION"), @ViewDebug.FlagToString(equals=4, mask=4, name="FULLSCREEN"), @ViewDebug.FlagToString(equals=256, mask=256, name="LAYOUT_STABLE"), @ViewDebug.FlagToString(equals=512, mask=512, name="LAYOUT_HIDE_NAVIGATION"), @ViewDebug.FlagToString(equals=1024, mask=1024, name="LAYOUT_FULLSCREEN"), @ViewDebug.FlagToString(equals=2048, mask=2048, name="IMMERSIVE"), @ViewDebug.FlagToString(equals=4096, mask=4096, name="IMMERSIVE_STICKY"), @ViewDebug.FlagToString(equals=8192, mask=8192, name="LIGHT_STATUS_BAR"), @ViewDebug.FlagToString(equals=16, mask=16, name="LIGHT_NAVIGATION_BAR"), @ViewDebug.FlagToString(equals=65536, mask=65536, name="STATUS_BAR_DISABLE_EXPAND"), @ViewDebug.FlagToString(equals=131072, mask=131072, name="STATUS_BAR_DISABLE_NOTIFICATION_ICONS"), @ViewDebug.FlagToString(equals=262144, mask=262144, name="STATUS_BAR_DISABLE_NOTIFICATION_ALERTS"), @ViewDebug.FlagToString(equals=524288, mask=524288, name="STATUS_BAR_DISABLE_NOTIFICATION_TICKER"), @ViewDebug.FlagToString(equals=1048576, mask=1048576, name="STATUS_BAR_DISABLE_SYSTEM_INFO"), @ViewDebug.FlagToString(equals=2097152, mask=2097152, name="STATUS_BAR_DISABLE_HOME"), @ViewDebug.FlagToString(equals=4194304, mask=4194304, name="STATUS_BAR_DISABLE_BACK"), @ViewDebug.FlagToString(equals=8388608, mask=8388608, name="STATUS_BAR_DISABLE_CLOCK"), @ViewDebug.FlagToString(equals=16777216, mask=16777216, name="STATUS_BAR_DISABLE_RECENT"), @ViewDebug.FlagToString(equals=33554432, mask=33554432, name="STATUS_BAR_DISABLE_SEARCH"), @ViewDebug.FlagToString(equals=67108864, mask=67108864, name="STATUS_BAR_TRANSIENT"), @ViewDebug.FlagToString(equals=134217728, mask=134217728, name="NAVIGATION_BAR_TRANSIENT"), @ViewDebug.FlagToString(equals=268435456, mask=268435456, name="STATUS_BAR_UNHIDE"), @ViewDebug.FlagToString(equals=536870912, mask=536870912, name="NAVIGATION_BAR_UNHIDE"), @ViewDebug.FlagToString(equals=1073741824, mask=1073741824, name="STATUS_BAR_TRANSLUCENT"), @ViewDebug.FlagToString(equals=Integer.MIN_VALUE, mask=Integer.MIN_VALUE, name="NAVIGATION_BAR_TRANSLUCENT"), @ViewDebug.FlagToString(equals=32768, mask=32768, name="NAVIGATION_BAR_TRANSPARENT"), @ViewDebug.FlagToString(equals=8, mask=8, name="STATUS_BAR_TRANSPARENT")}, formatToHexString=true)
  int mSystemUiVisibility;
  protected Object mTag;
  private int[] mTempNestedScrollConsumed;
  TooltipInfo mTooltipInfo;
  @ViewDebug.ExportedProperty(category="layout")
  protected int mTop;
  private TouchDelegate mTouchDelegate;
  private int mTouchSlop;
  public TransformationInfo mTransformationInfo;
  int mTransientStateCount;
  private String mTransitionName;
  private Bitmap mUnscaledDrawingCache;
  private UnsetPressedState mUnsetPressedState;
  @ViewDebug.ExportedProperty(category="padding")
  protected int mUserPaddingBottom;
  @ViewDebug.ExportedProperty(category="padding")
  int mUserPaddingEnd;
  @ViewDebug.ExportedProperty(category="padding")
  protected int mUserPaddingLeft;
  int mUserPaddingLeftInitial;
  @ViewDebug.ExportedProperty(category="padding")
  protected int mUserPaddingRight;
  int mUserPaddingRightInitial;
  @ViewDebug.ExportedProperty(category="padding")
  int mUserPaddingStart;
  private float mVerticalScrollFactor;
  private int mVerticalScrollbarPosition;
  @ViewDebug.ExportedProperty(formatToHexString=true)
  int mViewFlags;
  private Handler mVisibilityChangeForAutofillHandler;
  int mWindowAttachCount;
  
  static
  {
    AUTOFILL_HIGHLIGHT_ATTR = new int[] { 16844136 };
    sCompatibilityDone = false;
    sUseBrokenMakeMeasureSpec = false;
    sUseZeroUnspecifiedMeasureSpec = false;
    sIgnoreMeasureCache = false;
    sAlwaysRemeasureExactly = false;
    sLayoutParamsAlwaysChanged = false;
    sTextureViewIgnoresDrawableSetters = false;
    VISIBILITY_FLAGS = new int[] { 0, 4, 8 };
    DRAWING_CACHE_QUALITY_FLAGS = new int[] { 0, 524288, 1048576 };
    EMPTY_STATE_SET = StateSet.get(0);
    WINDOW_FOCUSED_STATE_SET = StateSet.get(1);
    SELECTED_STATE_SET = StateSet.get(2);
    SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(3);
    FOCUSED_STATE_SET = StateSet.get(4);
    FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(5);
    FOCUSED_SELECTED_STATE_SET = StateSet.get(6);
    FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(7);
    ENABLED_STATE_SET = StateSet.get(8);
    ENABLED_WINDOW_FOCUSED_STATE_SET = StateSet.get(9);
    ENABLED_SELECTED_STATE_SET = StateSet.get(10);
    ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(11);
    ENABLED_FOCUSED_STATE_SET = StateSet.get(12);
    ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(13);
    ENABLED_FOCUSED_SELECTED_STATE_SET = StateSet.get(14);
    ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(15);
    PRESSED_STATE_SET = StateSet.get(16);
    PRESSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(17);
    PRESSED_SELECTED_STATE_SET = StateSet.get(18);
    PRESSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(19);
    PRESSED_FOCUSED_STATE_SET = StateSet.get(20);
    PRESSED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(21);
    PRESSED_FOCUSED_SELECTED_STATE_SET = StateSet.get(22);
    PRESSED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(23);
    PRESSED_ENABLED_STATE_SET = StateSet.get(24);
    PRESSED_ENABLED_WINDOW_FOCUSED_STATE_SET = StateSet.get(25);
    PRESSED_ENABLED_SELECTED_STATE_SET = StateSet.get(26);
    PRESSED_ENABLED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(27);
    PRESSED_ENABLED_FOCUSED_STATE_SET = StateSet.get(28);
    PRESSED_ENABLED_FOCUSED_WINDOW_FOCUSED_STATE_SET = StateSet.get(29);
    PRESSED_ENABLED_FOCUSED_SELECTED_STATE_SET = StateSet.get(30);
    PRESSED_ENABLED_FOCUSED_SELECTED_WINDOW_FOCUSED_STATE_SET = StateSet.get(31);
    DEBUG_CORNERS_COLOR = Color.rgb(63, 127, 255);
    sThreadLocal = new ThreadLocal();
    LAYOUT_DIRECTION_FLAGS = new int[] { 0, 1, 2, 3 };
    PFLAG2_TEXT_DIRECTION_FLAGS = new int[] { 0, 64, 128, 192, 256, 320, 384, 448 };
    PFLAG2_TEXT_ALIGNMENT_FLAGS = new int[] { 0, 8192, 16384, 24576, 32768, 40960, 49152 };
    sNextGeneratedId = new AtomicInteger(1);
    ALPHA = new FloatProperty("alpha")
    {
      public Float get(View paramAnonymousView)
      {
        return Float.valueOf(paramAnonymousView.getAlpha());
      }
      
      public void setValue(View paramAnonymousView, float paramAnonymousFloat)
      {
        paramAnonymousView.setAlpha(paramAnonymousFloat);
      }
    };
    TRANSLATION_X = new FloatProperty("translationX")
    {
      public Float get(View paramAnonymousView)
      {
        return Float.valueOf(paramAnonymousView.getTranslationX());
      }
      
      public void setValue(View paramAnonymousView, float paramAnonymousFloat)
      {
        paramAnonymousView.setTranslationX(paramAnonymousFloat);
      }
    };
    TRANSLATION_Y = new FloatProperty("translationY")
    {
      public Float get(View paramAnonymousView)
      {
        return Float.valueOf(paramAnonymousView.getTranslationY());
      }
      
      public void setValue(View paramAnonymousView, float paramAnonymousFloat)
      {
        paramAnonymousView.setTranslationY(paramAnonymousFloat);
      }
    };
    TRANSLATION_Z = new FloatProperty("translationZ")
    {
      public Float get(View paramAnonymousView)
      {
        return Float.valueOf(paramAnonymousView.getTranslationZ());
      }
      
      public void setValue(View paramAnonymousView, float paramAnonymousFloat)
      {
        paramAnonymousView.setTranslationZ(paramAnonymousFloat);
      }
    };
    X = new FloatProperty("x")
    {
      public Float get(View paramAnonymousView)
      {
        return Float.valueOf(paramAnonymousView.getX());
      }
      
      public void setValue(View paramAnonymousView, float paramAnonymousFloat)
      {
        paramAnonymousView.setX(paramAnonymousFloat);
      }
    };
    Y = new FloatProperty("y")
    {
      public Float get(View paramAnonymousView)
      {
        return Float.valueOf(paramAnonymousView.getY());
      }
      
      public void setValue(View paramAnonymousView, float paramAnonymousFloat)
      {
        paramAnonymousView.setY(paramAnonymousFloat);
      }
    };
    Z = new FloatProperty("z")
    {
      public Float get(View paramAnonymousView)
      {
        return Float.valueOf(paramAnonymousView.getZ());
      }
      
      public void setValue(View paramAnonymousView, float paramAnonymousFloat)
      {
        paramAnonymousView.setZ(paramAnonymousFloat);
      }
    };
  }
  
  View()
  {
    mCurrentAnimation = null;
    mRecreateDisplayList = false;
    mID = -1;
    mAutofillViewId = -1;
    mAccessibilityViewId = -1;
    mAccessibilityCursorPosition = -1;
    mTag = null;
    mTransientStateCount = 0;
    mClipBounds = null;
    mPaddingLeft = 0;
    mPaddingRight = 0;
    mLabelForId = -1;
    mAccessibilityTraversalBeforeId = -1;
    mAccessibilityTraversalAfterId = -1;
    mLeftPaddingDefined = false;
    mRightPaddingDefined = false;
    mOldWidthMeasureSpec = Integer.MIN_VALUE;
    mOldHeightMeasureSpec = Integer.MIN_VALUE;
    mLongClickX = NaN.0F;
    mLongClickY = NaN.0F;
    mDrawableState = null;
    mOutlineProvider = ViewOutlineProvider.BACKGROUND;
    mNextFocusLeftId = -1;
    mNextFocusRightId = -1;
    mNextFocusUpId = -1;
    mNextFocusDownId = -1;
    mNextFocusForwardId = -1;
    mNextClusterForwardId = -1;
    mDefaultFocusHighlightEnabled = true;
    mPendingCheckForTap = null;
    mTouchDelegate = null;
    mDrawingCacheBackgroundColor = 0;
    mAnimator = null;
    mLayerType = 0;
    InputEventConsistencyVerifier localInputEventConsistencyVerifier;
    if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
      localInputEventConsistencyVerifier = new InputEventConsistencyVerifier(this, 0);
    } else {
      localInputEventConsistencyVerifier = null;
    }
    mInputEventConsistencyVerifier = localInputEventConsistencyVerifier;
    mResources = null;
    mRenderNode = RenderNode.create(getClass().getName(), this);
  }
  
  public View(Context paramContext)
  {
    Object localObject1 = null;
    mCurrentAnimation = null;
    boolean bool1 = false;
    mRecreateDisplayList = false;
    mID = -1;
    mAutofillViewId = -1;
    mAccessibilityViewId = -1;
    mAccessibilityCursorPosition = -1;
    mTag = null;
    mTransientStateCount = 0;
    mClipBounds = null;
    mPaddingLeft = 0;
    mPaddingRight = 0;
    mLabelForId = -1;
    mAccessibilityTraversalBeforeId = -1;
    mAccessibilityTraversalAfterId = -1;
    mLeftPaddingDefined = false;
    mRightPaddingDefined = false;
    mOldWidthMeasureSpec = Integer.MIN_VALUE;
    mOldHeightMeasureSpec = Integer.MIN_VALUE;
    mLongClickX = NaN.0F;
    mLongClickY = NaN.0F;
    mDrawableState = null;
    mOutlineProvider = ViewOutlineProvider.BACKGROUND;
    mNextFocusLeftId = -1;
    mNextFocusRightId = -1;
    mNextFocusUpId = -1;
    mNextFocusDownId = -1;
    mNextFocusForwardId = -1;
    mNextClusterForwardId = -1;
    mDefaultFocusHighlightEnabled = true;
    mPendingCheckForTap = null;
    mTouchDelegate = null;
    mDrawingCacheBackgroundColor = 0;
    mAnimator = null;
    mLayerType = 0;
    if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
      localObject2 = new InputEventConsistencyVerifier(this, 0);
    } else {
      localObject2 = null;
    }
    mInputEventConsistencyVerifier = ((InputEventConsistencyVerifier)localObject2);
    mContext = paramContext;
    Object localObject2 = localObject1;
    if (paramContext != null) {
      localObject2 = paramContext.getResources();
    }
    mResources = ((Resources)localObject2);
    mViewFlags = 402653200;
    mPrivateFlags2 = 140296;
    mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    setOverScrollMode(1);
    mUserPaddingStart = Integer.MIN_VALUE;
    mUserPaddingEnd = Integer.MIN_VALUE;
    mRenderNode = RenderNode.create(getClass().getName(), this);
    if ((!sCompatibilityDone) && (paramContext != null))
    {
      int i = getApplicationInfotargetSdkVersion;
      if (i <= 17) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sUseBrokenMakeMeasureSpec = bool2;
      if (i < 19) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sIgnoreMeasureCache = bool2;
      if (i < 23) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Canvas.sCompatibilityRestore = bool2;
      if (i < 26) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      Canvas.sCompatibilitySetBitmap = bool2;
      Canvas.setCompatibilityVersion(i);
      if (i < 23) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sUseZeroUnspecifiedMeasureSpec = bool2;
      if (i <= 23) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sAlwaysRemeasureExactly = bool2;
      if (i <= 23) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sLayoutParamsAlwaysChanged = bool2;
      if (i <= 23) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sTextureViewIgnoresDrawableSetters = bool2;
      if (i >= 24) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sPreserveMarginParamsInLayoutParamConversion = bool2;
      if (i < 24) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sCascadedDragDrop = bool2;
      if (i < 26) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sHasFocusableExcludeAutoFocusable = bool2;
      if (i < 26) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sAutoFocusableOffUIThreadWontNotifyParents = bool2;
      sUseDefaultFocusHighlight = paramContext.getResources().getBoolean(17957066);
      if (i >= 28) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sThrowOnInvalidFloatProperties = bool2;
      if (i < 28) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sCanFocusZeroSized = bool2;
      if (i < 28) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      sAlwaysAssignFocus = bool2;
      boolean bool2 = bool1;
      if (i < 28) {
        bool2 = true;
      }
      sAcceptZeroSizeDragShadow = bool2;
      sCompatibilityDone = true;
    }
  }
  
  public View(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public View(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public View(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    this(paramContext);
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.View, paramInt1, paramInt2);
    if (mDebugViewAttributes) {
      saveAttributeData(paramAttributeSet, localTypedArray);
    }
    int i = Integer.MIN_VALUE;
    int j = Integer.MIN_VALUE;
    paramInt1 = -1;
    int k = 0;
    int m = 0;
    int n = mOverScrollMode;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    int i5 = getApplicationInfotargetSdkVersion;
    int i6 = localTypedArray.getIndexCount();
    int i7 = 0x0 | 0x10;
    int i8 = -1;
    int i9 = -1;
    int i10 = 0x0 | 0x10;
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    float f4 = 0.0F;
    float f5 = 0.0F;
    float f6 = 0.0F;
    float f7 = 0.0F;
    float f8 = 1.0F;
    float f9 = 1.0F;
    paramAttributeSet = null;
    boolean bool1 = false;
    int i11 = 0;
    int i12 = -1;
    int i13 = -1;
    boolean bool2 = false;
    int i14 = -1;
    int i15 = 0;
    int i16 = -1;
    int i17 = 0;
    int i18 = 0;
    while (i11 < i6)
    {
      int i19 = localTypedArray.getIndex(i11);
      Object localObject2;
      int i22;
      int i23;
      switch (i19)
      {
      case 25: 
      case 45: 
      case 46: 
      case 47: 
      case 102: 
      case 103: 
      case 104: 
      default: 
        paramInt2 = i10;
        i10 = i7;
        i7 = paramInt2;
        i20 = i18;
        i21 = m;
        paramInt2 = paramInt1;
        break;
      case 105: 
        if ((i5 < 23) && (!(this instanceof FrameLayout))) {
          break;
        }
        if (mForegroundInfo == null) {
          mForegroundInfo = new ForegroundInfo(null);
        }
        ForegroundInfo.access$102(mForegroundInfo, localTypedArray.getBoolean(i19, mForegroundInfo.mInsidePadding));
        break;
      case 101: 
        setOutlineAmbientShadowColor(localTypedArray.getColor(i19, -16777216));
        break;
      case 100: 
        setOutlineSpotShadowColor(localTypedArray.getColor(i19, -16777216));
        break;
      case 99: 
        setAccessibilityHeading(localTypedArray.getBoolean(i19, false));
        break;
      case 98: 
        if (localTypedArray.peekValue(i19) != null) {
          setAccessibilityPaneTitle(localTypedArray.getString(i19));
        }
        break;
      case 97: 
        if (localTypedArray.peekValue(i19) != null) {
          setScreenReaderFocusable(localTypedArray.getBoolean(i19, false));
        }
        break;
      case 96: 
        if (localTypedArray.peekValue(i19) != null) {
          setDefaultFocusHighlightEnabled(localTypedArray.getBoolean(i19, true));
        }
        break;
      case 95: 
        if (localTypedArray.peekValue(i19) != null) {
          setImportantForAutofill(localTypedArray.getInt(i19, 0));
        }
        paramInt2 = paramInt1;
        break;
      case 94: 
        if (localTypedArray.peekValue(i19) != null)
        {
          Object localObject1 = null;
          if (localTypedArray.getType(i19) == 1)
          {
            paramInt2 = localTypedArray.getResourceId(i19, 0);
            try
            {
              CharSequence[] arrayOfCharSequence = localTypedArray.getTextArray(i19);
            }
            catch (Resources.NotFoundException localNotFoundException)
            {
              localObject1 = getResources().getString(paramInt2);
              localObject2 = null;
            }
          }
          else
          {
            localObject1 = localTypedArray.getString(i19);
            localObject2 = null;
          }
          if (localObject2 == null) {
            if (localObject1 != null) {
              localObject2 = ((String)localObject1).split(",");
            } else {
              throw new IllegalArgumentException("Could not resolve autofillHints");
            }
          }
          localObject1 = new String[localObject2.length];
          i20 = localObject2.length;
          for (paramInt2 = 0; paramInt2 < i20; paramInt2++) {
            localObject1[paramInt2] = localObject2[paramInt2].toString().trim();
          }
          setAutofillHints((String[])localObject1);
          paramInt2 = paramInt1;
        }
        else
        {
          paramInt2 = i10;
          i10 = i7;
          i7 = paramInt2;
          i20 = i18;
          i21 = m;
          paramInt2 = paramInt1;
        }
        break;
      case 93: 
        paramInt2 = paramInt1;
        if (localTypedArray.peekValue(i19) != null)
        {
          setFocusedByDefault(localTypedArray.getBoolean(i19, true));
          paramInt2 = paramInt1;
        }
        break;
      case 92: 
        mNextClusterForwardId = localTypedArray.getResourceId(i19, -1);
        paramInt2 = paramInt1;
        break;
      case 91: 
        paramInt2 = paramInt1;
        if (localTypedArray.peekValue(i19) != null)
        {
          setKeyboardNavigationCluster(localTypedArray.getBoolean(i19, true));
          paramInt2 = paramInt1;
        }
        break;
      case 90: 
        paramInt2 = i15;
        i13 = localTypedArray.getDimensionPixelSize(i19, -1);
        break;
      case 89: 
        paramInt2 = i15;
        i9 = localTypedArray.getDimensionPixelSize(i19, -1);
        mUserPaddingLeftInitial = i9;
        mUserPaddingRightInitial = i9;
        bool1 = true;
        bool2 = true;
        break;
      case 88: 
        setTooltipText(localTypedArray.getText(i19));
        paramInt2 = paramInt1;
        break;
      case 87: 
        paramInt2 = paramInt1;
        if (localTypedArray.peekValue(i19) != null)
        {
          forceHasOverlappingRendering(localTypedArray.getBoolean(i19, true));
          paramInt2 = paramInt1;
        }
      case 86: 
        for (;;)
        {
          break;
          paramInt2 = localTypedArray.getResourceId(i19, 0);
          if (paramInt2 != 0)
          {
            setPointerIcon(PointerIcon.load(paramContext.getResources(), paramInt2));
            paramInt2 = paramInt1;
          }
          else
          {
            paramInt2 = localTypedArray.getInt(i19, 1);
            if (paramInt2 != 1) {
              setPointerIcon(PointerIcon.getSystemIcon(paramContext, paramInt2));
            }
            paramInt2 = paramInt1;
          }
        }
      case 85: 
        paramInt2 = i15;
        if (localTypedArray.getBoolean(i19, false))
        {
          i10 = 0x800000 | i10;
          i7 |= 0x800000;
          break label3525;
        }
        paramInt2 = i7;
        i7 = i10;
        i10 = paramInt2;
        i20 = i18;
        i21 = m;
        paramInt2 = paramInt1;
        break;
      case 84: 
        paramInt2 = i15;
        i20 = localTypedArray.getInt(i19, 0) << 8 & 0x3F00;
        if (i20 != 0)
        {
          mPrivateFlags3 |= i20;
          i2 = 1;
        }
        break;
      case 83: 
        setAccessibilityTraversalAfter(localTypedArray.getResourceId(i19, -1));
        break;
      case 82: 
        setAccessibilityTraversalBefore(localTypedArray.getResourceId(i19, -1));
        break;
      case 81: 
        setOutlineProviderFromAttribute(localTypedArray.getInt(81, 0));
      case 80: 
      case 79: 
        for (;;)
        {
          paramInt2 = paramInt1;
          break;
          if ((i5 >= 23) || ((this instanceof FrameLayout)))
          {
            setForegroundTintMode(Drawable.parseTintMode(localTypedArray.getInt(i19, -1), null));
            continue;
            if ((i5 >= 23) || ((this instanceof FrameLayout))) {
              setForegroundTintList(localTypedArray.getColorStateList(i19));
            }
          }
        }
      case 78: 
        if (mBackgroundTint == null) {
          mBackgroundTint = new TintInfo();
        }
        mBackgroundTint.mTintMode = Drawable.parseTintMode(localTypedArray.getInt(78, -1), null);
        mBackgroundTint.mHasTintMode = true;
        paramInt2 = paramInt1;
        break;
      case 77: 
        if (mBackgroundTint == null) {
          mBackgroundTint = new TintInfo();
        }
        mBackgroundTint.mTintList = localTypedArray.getColorStateList(77);
        mBackgroundTint.mHasTintList = true;
        paramInt2 = paramInt1;
        break;
      case 76: 
        setStateListAnimator(AnimatorInflater.loadStateListAnimator(paramContext, localTypedArray.getResourceId(i19, 0)));
        paramInt2 = paramInt1;
        break;
      case 75: 
        f4 = localTypedArray.getDimension(i19, 0.0F);
        break;
      case 74: 
        setNestedScrollingEnabled(localTypedArray.getBoolean(i19, false));
        paramInt2 = paramInt1;
        break;
      case 73: 
        setTransitionName(localTypedArray.getString(i19));
        paramInt2 = paramInt1;
        break;
      case 72: 
        f3 = localTypedArray.getDimension(i19, 0.0F);
        break;
      case 71: 
        setAccessibilityLiveRegion(localTypedArray.getInt(i19, 0));
        paramInt2 = paramInt1;
        break;
      case 70: 
        setLabelFor(localTypedArray.getResourceId(i19, -1));
        paramInt2 = paramInt1;
        break;
      case 69: 
        j = localTypedArray.getDimensionPixelSize(i19, Integer.MIN_VALUE);
        if (j != Integer.MIN_VALUE) {
          paramInt2 = 1;
        } else {
          paramInt2 = 0;
        }
        i4 = paramInt2;
        break;
      case 68: 
        i = localTypedArray.getDimensionPixelSize(i19, Integer.MIN_VALUE);
        if (i != Integer.MIN_VALUE) {
          paramInt2 = 1;
        } else {
          paramInt2 = 0;
        }
        i3 = paramInt2;
        paramInt2 = i7;
        break;
      case 67: 
        mPrivateFlags2 &= 0xFFFFFFC3;
        paramInt2 = localTypedArray.getInt(i19, -1);
        if (paramInt2 != -1) {
          paramInt2 = LAYOUT_DIRECTION_FLAGS[paramInt2];
        } else {
          paramInt2 = 2;
        }
        mPrivateFlags2 |= paramInt2 << 2;
        paramInt2 = paramInt1;
        break;
      case 66: 
        mPrivateFlags2 &= 0xFFFF1FFF;
        paramInt2 = localTypedArray.getInt(i19, 1);
        mPrivateFlags2 |= PFLAG2_TEXT_ALIGNMENT_FLAGS[paramInt2];
        paramInt2 = paramInt1;
        break;
      case 65: 
        mPrivateFlags2 &= 0xFE3F;
        i20 = localTypedArray.getInt(i19, -1);
        paramInt2 = paramInt1;
        if (i20 != -1)
        {
          mPrivateFlags2 |= PFLAG2_TEXT_DIRECTION_FLAGS[i20];
          paramInt2 = paramInt1;
        }
        break;
      case 64: 
        paramInt2 = i10;
        setImportantForAccessibility(localTypedArray.getInt(i19, 0));
        i10 = i7;
        i7 = paramInt2;
        i20 = i18;
        i21 = m;
        paramInt2 = paramInt1;
        break;
      case 63: 
        break;
      case 62: 
        paramInt2 = i7;
        i7 = i10;
        setLayerType(localTypedArray.getInt(i19, 0), null);
        i10 = paramInt2;
        i20 = i18;
        i21 = m;
        paramInt2 = paramInt1;
        break;
      case 61: 
        paramInt2 = i7;
        i7 = i10;
        mNextFocusForwardId = localTypedArray.getResourceId(i19, -1);
        i10 = paramInt2;
        i20 = i18;
        i21 = m;
        paramInt2 = paramInt1;
        break;
      case 60: 
        mVerticalScrollbarPosition = localTypedArray.getInt(i19, 0);
        paramInt2 = paramInt1;
        break;
      case 59: 
        f7 = localTypedArray.getFloat(i19, 0.0F);
        break;
      case 58: 
        f6 = localTypedArray.getFloat(i19, 0.0F);
        break;
      case 57: 
        f5 = localTypedArray.getFloat(i19, 0.0F);
        break;
      case 56: 
        f9 = localTypedArray.getFloat(i19, 1.0F);
        break;
      case 55: 
        f8 = localTypedArray.getFloat(i19, 1.0F);
        break;
      case 54: 
        f2 = localTypedArray.getDimension(i19, 0.0F);
        break;
      case 53: 
        f1 = localTypedArray.getDimension(i19, 0.0F);
        paramInt2 = i15;
        k = 1;
        break;
      case 52: 
        setPivotY(localTypedArray.getDimension(i19, 0.0F));
        paramInt2 = paramInt1;
        break;
      case 51: 
        setPivotX(localTypedArray.getDimension(i19, 0.0F));
        paramInt2 = paramInt1;
        break;
      case 50: 
        setAlpha(localTypedArray.getFloat(i19, 1.0F));
        paramInt2 = paramInt1;
        break;
      case 49: 
        paramInt2 = paramInt1;
        if (localTypedArray.getBoolean(i19, false))
        {
          paramInt2 = i7 | 0x400;
          i10 |= 0x400;
        }
        break;
      case 48: 
        paramInt2 = i15;
        n = localTypedArray.getInt(i19, 1);
        break;
      case 44: 
        setContentDescription(localTypedArray.getString(i19));
        paramInt2 = paramInt1;
        break;
      case 43: 
        if (!paramContext.isRestricted())
        {
          localObject2 = localTypedArray.getString(i19);
          paramInt2 = paramInt1;
          if (localObject2 != null)
          {
            setOnClickListener(new DeclaredOnClickListener(this, (String)localObject2));
            paramInt2 = paramInt1;
          }
        }
        else
        {
          throw new IllegalStateException("The android:onClick attribute cannot be used within a restricted context");
        }
      case 42: 
        paramInt2 = paramInt1;
        if (!localTypedArray.getBoolean(i19, true))
        {
          paramInt2 = 0xEFFFFFFF & i7;
          i10 = 0x10000000 | i10;
        }
        break;
      case 41: 
        paramInt2 = i7;
        i7 = i10;
        if (localTypedArray.getBoolean(i19, false)) {
          setScrollContainer(true);
        }
        i20 = 1;
        i10 = paramInt2;
        i21 = m;
        paramInt2 = paramInt1;
        break;
      case 40: 
        paramInt2 = paramInt1;
        if (localTypedArray.getBoolean(i19, false))
        {
          i10 = 0x4000000 | i10;
          paramInt2 = i7 | 0x4000000;
        }
        break;
      case 39: 
        paramInt2 = paramInt1;
        if (!localTypedArray.getBoolean(i19, true))
        {
          paramInt2 = 0xF7FFFFFF & i7;
          i10 = 0x8000000 | i10;
        }
        break;
      case 38: 
        i21 = i7;
        i20 = i10;
        if (i5 < 23)
        {
          paramInt2 = paramInt1;
          if (!(this instanceof FrameLayout)) {}
        }
        else
        {
          setForegroundGravity(localTypedArray.getInt(i19, 0));
          i10 = i21;
          i7 = i20;
          i20 = i18;
          i21 = m;
          paramInt2 = paramInt1;
        }
        break;
      case 37: 
        paramInt2 = i10;
        mMinHeight = localTypedArray.getDimensionPixelSize(i19, 0);
        i10 = i7;
        i7 = paramInt2;
        i20 = i18;
        i21 = m;
        paramInt2 = paramInt1;
        break;
      case 36: 
        mMinWidth = localTypedArray.getDimensionPixelSize(i19, 0);
        paramInt2 = paramInt1;
        break;
      case 35: 
        if (i5 < 23)
        {
          paramInt2 = paramInt1;
          if (!(this instanceof FrameLayout)) {}
        }
        else
        {
          setForeground(localTypedArray.getDrawable(i19));
          paramInt2 = paramInt1;
        }
        break;
      case 34: 
        i22 = i7;
        i23 = i10;
        i10 = i22;
        i7 = i23;
        i20 = i18;
        i21 = m;
        paramInt2 = paramInt1;
        if (!localTypedArray.getBoolean(i19, false)) {
          break label3500;
        }
        i10 = 0x400000 | i23;
        i7 = i22 | 0x400000;
        paramInt2 = paramInt1;
        break;
      case 33: 
        paramInt2 = paramInt1;
        i20 = localTypedArray.getInt(i19, 0);
        if (i20 != 0)
        {
          i7 = DRAWING_CACHE_QUALITY_FLAGS[i20] | i7;
          paramInt2 = 0x180000 | i10;
          i10 = i7;
        }
        break;
      case 32: 
        paramInt2 = paramInt1;
        if (!localTypedArray.getBoolean(i19, true))
        {
          paramInt2 = 0x10000 | i7;
          i10 = 0x10000 | i10;
        }
        break;
      case 31: 
        paramInt2 = paramInt1;
        i22 = i7;
        i23 = i10;
        i10 = i22;
        i7 = i23;
        i20 = i18;
        i21 = m;
        if (!localTypedArray.getBoolean(i19, false)) {
          break label3500;
        }
        i10 = 0x200000 | i22;
        paramInt2 = 0x200000 | i23;
        break;
      case 30: 
        paramInt2 = paramInt1;
        if (localTypedArray.getBoolean(i19, false))
        {
          paramInt2 = i7 | 0x4000;
          i10 |= 0x4000;
        }
        break;
      case 29: 
        mNextFocusDownId = localTypedArray.getResourceId(i19, -1);
        paramInt2 = paramInt1;
        break;
      case 28: 
        mNextFocusUpId = localTypedArray.getResourceId(i19, -1);
        paramInt2 = paramInt1;
        break;
      case 27: 
        mNextFocusRightId = localTypedArray.getResourceId(i19, -1);
        paramInt2 = paramInt1;
        break;
      case 26: 
        mNextFocusLeftId = localTypedArray.getResourceId(i19, -1);
        paramInt2 = paramInt1;
        break;
      case 24: 
        paramInt2 = paramInt1;
        if (i5 < 14)
        {
          paramInt2 = paramInt1;
          i20 = localTypedArray.getInt(i19, 0);
          if (i20 != 0)
          {
            paramInt2 = i7 | i20;
            i7 = i10 | 0x3000;
            initializeFadingEdgeInternal(localTypedArray);
            i10 = paramInt2;
            paramInt2 = i7;
          }
        }
        break;
      case 23: 
        paramInt2 = paramInt1;
        i20 = localTypedArray.getInt(i19, 0);
        if (i20 != 0)
        {
          paramInt2 = i7 | i20;
          i10 |= 0x300;
          i1 = 1;
        }
        break;
      case 22: 
        paramInt2 = paramInt1;
        i22 = i7;
        i23 = i10;
        i10 = i22;
        i7 = i23;
        i20 = i18;
        i21 = m;
        if (!localTypedArray.getBoolean(i19, false)) {
          break label3500;
        }
        i10 = i22 | 0x2;
        paramInt2 = i23 | 0x2;
        i7 = i10;
        i10 = paramInt2;
        paramInt2 = paramInt1;
        break;
      case 21: 
        paramInt2 = paramInt1;
        i20 = localTypedArray.getInt(i19, 0);
        if (i20 != 0)
        {
          i7 = VISIBILITY_FLAGS[i20] | i7;
          paramInt2 = i10 | 0xC;
          i10 = i7;
          i7 = paramInt2;
          paramInt2 = i10;
          i10 = i7;
        }
        break;
      case 20: 
        paramInt2 = paramInt1;
        if (localTypedArray.getBoolean(i19, false))
        {
          paramInt2 = i7 & 0xFFFFFFEF | 0x40001;
          i10 = 0x40011 | i10;
        }
        break;
      case 19: 
        paramInt2 = i7 & 0xFFFFFFEE | getFocusableAttribute(localTypedArray);
        if ((paramInt2 & 0x10) == 0) {
          i10 |= 0x11;
        }
        i7 = paramInt2;
        paramInt2 = i15;
        break;
      case 18: 
        paramInt2 = i15;
        i16 = localTypedArray.getDimensionPixelSize(i19, -1);
        break;
      case 17: 
        paramInt2 = i15;
        i14 = localTypedArray.getDimensionPixelSize(i19, -1);
        mUserPaddingRightInitial = i14;
        bool2 = true;
        break;
      case 16: 
        paramInt2 = i15;
        i8 = localTypedArray.getDimensionPixelSize(i19, -1);
        break;
      case 15: 
        paramInt2 = i15;
        i12 = localTypedArray.getDimensionPixelSize(i19, -1);
        mUserPaddingLeftInitial = i12;
        bool1 = true;
        break;
      case 14: 
        paramInt2 = i15;
        paramInt1 = localTypedArray.getDimensionPixelSize(i19, -1);
        mUserPaddingLeftInitial = paramInt1;
        mUserPaddingRightInitial = paramInt1;
        bool2 = true;
        bool1 = true;
        break;
      case 13: 
        paramInt2 = i15;
        paramAttributeSet = localTypedArray.getDrawable(i19);
        break;
      case 12: 
        paramInt2 = i15;
        i17 = localTypedArray.getDimensionPixelOffset(i19, 0);
        break;
      case 11: 
        paramInt2 = localTypedArray.getDimensionPixelOffset(i19, 0);
        break;
      case 10: 
        mTag = localTypedArray.getText(i19);
        paramInt2 = paramInt1;
        break;
      case 9: 
        mID = localTypedArray.getResourceId(i19, -1);
        paramInt2 = paramInt1;
        paramInt1 = i10;
        i10 = i7;
        i7 = paramInt1;
        i20 = i18;
        i21 = m;
        break;
      }
      paramInt2 = i7;
      m = i10;
      int i21 = localTypedArray.getInt(i19, 0);
      i10 = paramInt2;
      i7 = m;
      if (i21 != 0)
      {
        i10 = paramInt2 | 0x3000000 & i21;
        i7 = m | 0x3000000;
      }
      paramInt2 = paramInt1;
      int i20 = i18;
      label3500:
      paramInt1 = i10;
      i10 = i7;
      i7 = paramInt1;
      m = i21;
      i18 = i20;
      paramInt1 = paramInt2;
      paramInt2 = i15;
      label3525:
      i11++;
      i15 = paramInt2;
    }
    setOverScrollMode(n);
    mUserPaddingStart = i;
    mUserPaddingEnd = j;
    if (paramAttributeSet != null) {
      setBackground(paramAttributeSet);
    }
    mLeftPaddingDefined = bool1;
    mRightPaddingDefined = bool2;
    if (paramInt1 >= 0)
    {
      paramInt2 = paramInt1;
      i16 = paramInt1;
      i13 = paramInt1;
      mUserPaddingLeftInitial = paramInt1;
      mUserPaddingRightInitial = paramInt1;
      i8 = paramInt1;
    }
    else
    {
      if (i9 >= 0)
      {
        i12 = i9;
        i14 = i9;
        mUserPaddingLeftInitial = i9;
        mUserPaddingRightInitial = i9;
      }
      paramInt2 = i12;
      if (i13 >= 0)
      {
        i8 = i13;
        i16 = i13;
      }
      i13 = i16;
      i16 = i8;
      i8 = i14;
    }
    if (isRtlCompatibilityMode())
    {
      paramInt1 = paramInt2;
      if (!mLeftPaddingDefined)
      {
        paramInt1 = paramInt2;
        if (i3 != 0) {
          paramInt1 = i;
        }
      }
      if (paramInt1 >= 0) {
        paramInt2 = paramInt1;
      } else {
        paramInt2 = mUserPaddingLeftInitial;
      }
      mUserPaddingLeftInitial = paramInt2;
      paramInt2 = i8;
      if (!mRightPaddingDefined)
      {
        paramInt2 = i8;
        if (i4 != 0) {
          paramInt2 = j;
        }
      }
      if (paramInt2 < 0) {
        paramInt2 = mUserPaddingRightInitial;
      }
      mUserPaddingRightInitial = paramInt2;
      j = paramInt1;
    }
    else
    {
      if ((i3 == 0) && (i4 == 0)) {
        paramInt1 = 0;
      } else {
        paramInt1 = 1;
      }
      if ((mLeftPaddingDefined) && (paramInt1 == 0)) {
        mUserPaddingLeftInitial = paramInt2;
      }
      j = paramInt2;
      if (mRightPaddingDefined)
      {
        j = paramInt2;
        if (paramInt1 == 0)
        {
          mUserPaddingRightInitial = i8;
          j = paramInt2;
        }
      }
    }
    paramInt2 = mUserPaddingLeftInitial;
    if (i16 >= 0) {
      paramInt1 = i16;
    } else {
      paramInt1 = mPaddingTop;
    }
    j = mUserPaddingRightInitial;
    if (i13 < 0) {
      i13 = mPaddingBottom;
    }
    internalSetPadding(paramInt2, paramInt1, j, i13);
    if (i10 != 0) {
      setFlags(i7, i10);
    }
    if (i1 != 0) {
      initializeScrollbarsInternal(localTypedArray);
    }
    if (i2 != 0) {
      initializeScrollIndicatorsInternal();
    }
    localTypedArray.recycle();
    if (m != 0) {
      recomputePadding();
    }
    if ((i15 == 0) && (i17 == 0)) {
      break label3947;
    }
    scrollTo(i15, i17);
    label3947:
    if (k != 0)
    {
      setTranslationX(f1);
      setTranslationY(f2);
      setTranslationZ(f3);
      setElevation(f4);
      setRotation(f5);
      setRotationX(f6);
      setRotationY(f7);
      setScaleX(f8);
      setScaleY(f9);
    }
    if ((i18 == 0) && ((i7 & 0x200) != 0)) {
      setScrollContainer(true);
    }
    computeOpaqueFlags();
  }
  
  private void applyBackgroundTint()
  {
    if ((mBackground != null) && (mBackgroundTint != null))
    {
      TintInfo localTintInfo = mBackgroundTint;
      if ((mHasTintList) || (mHasTintMode))
      {
        mBackground = mBackground.mutate();
        if (mHasTintList) {
          mBackground.setTintList(mTintList);
        }
        if (mHasTintMode) {
          mBackground.setTintMode(mTintMode);
        }
        if (mBackground.isStateful()) {
          mBackground.setState(getDrawableState());
        }
      }
    }
  }
  
  private void applyForegroundTint()
  {
    if ((mForegroundInfo != null) && (mForegroundInfo.mDrawable != null) && (mForegroundInfo.mTintInfo != null))
    {
      TintInfo localTintInfo = mForegroundInfo.mTintInfo;
      if ((mHasTintList) || (mHasTintMode))
      {
        ForegroundInfo.access$1202(mForegroundInfo, mForegroundInfo.mDrawable.mutate());
        if (mHasTintList) {
          mForegroundInfo.mDrawable.setTintList(mTintList);
        }
        if (mHasTintMode) {
          mForegroundInfo.mDrawable.setTintMode(mTintMode);
        }
        if (mForegroundInfo.mDrawable.isStateful()) {
          mForegroundInfo.mDrawable.setState(getDrawableState());
        }
      }
    }
  }
  
  private boolean applyLegacyAnimation(ViewGroup paramViewGroup, long paramLong, Animation paramAnimation, boolean paramBoolean)
  {
    int i = mGroupFlags;
    if (!paramAnimation.isInitialized())
    {
      paramAnimation.initialize(mRight - mLeft, mBottom - mTop, paramViewGroup.getWidth(), paramViewGroup.getHeight());
      paramAnimation.initializeInvalidateRegion(0, 0, mRight - mLeft, mBottom - mTop);
      if (mAttachInfo != null) {
        paramAnimation.setListenerHandler(mAttachInfo.mHandler);
      }
      onAnimationStart();
    }
    Transformation localTransformation = paramViewGroup.getChildTransformation();
    boolean bool = paramAnimation.getTransformation(paramLong, localTransformation, 1.0F);
    if ((paramBoolean) && (mAttachInfo.mApplicationScale != 1.0F))
    {
      if (mInvalidationTransformation == null) {
        mInvalidationTransformation = new Transformation();
      }
      localTransformation = mInvalidationTransformation;
      paramAnimation.getTransformation(paramLong, localTransformation, 1.0F);
    }
    if (bool) {
      if (!paramAnimation.willChangeBounds())
      {
        if ((i & 0x90) == 128)
        {
          mGroupFlags |= 0x4;
        }
        else if ((i & 0x4) == 0)
        {
          mPrivateFlags |= 0x40;
          paramViewGroup.invalidate(mLeft, mTop, mRight, mBottom);
        }
      }
      else
      {
        if (mInvalidateRegion == null) {
          mInvalidateRegion = new RectF();
        }
        RectF localRectF = mInvalidateRegion;
        paramAnimation.getInvalidateRegion(0, 0, mRight - mLeft, mBottom - mTop, localRectF, localTransformation);
        mPrivateFlags |= 0x40;
        i = mLeft + (int)left;
        int j = mTop + (int)top;
        paramViewGroup.invalidate(i, j, (int)(localRectF.width() + 0.5F) + i, (int)(localRectF.height() + 0.5F) + j);
      }
    }
    return bool;
  }
  
  private void buildDrawingCacheImpl(boolean paramBoolean)
  {
    mCachingFailed = false;
    int i = mRight - mLeft;
    int j = mBottom - mTop;
    AttachInfo localAttachInfo = mAttachInfo;
    int k;
    if ((localAttachInfo != null) && (mScalingRequired)) {
      k = 1;
    } else {
      k = 0;
    }
    int m = i;
    int n = j;
    if (paramBoolean)
    {
      m = i;
      n = j;
      if (k != 0)
      {
        m = (int)(i * mApplicationScale + 0.5F);
        n = (int)(j * mApplicationScale + 0.5F);
      }
    }
    int i1 = mDrawingCacheBackgroundColor;
    if ((i1 == 0) && (!isOpaque())) {
      j = 0;
    } else {
      j = 1;
    }
    if ((localAttachInfo != null) && (mUse32BitDrawingCache)) {
      i = 1;
    } else {
      i = 0;
    }
    int i2;
    if ((j != 0) && (i == 0)) {
      i2 = 2;
    } else {
      i2 = 4;
    }
    long l1 = m * n * i2;
    long l2 = ViewConfiguration.get(mContext).getScaledMaximumDrawingCacheSize();
    if ((m > 0) && (n > 0) && (l1 <= l2))
    {
      Bitmap localBitmap;
      if (paramBoolean) {
        localBitmap = mDrawingCache;
      } else {
        localBitmap = mUnscaledDrawingCache;
      }
      Object localObject;
      if ((localBitmap != null) && (localBitmap.getWidth() == m) && (localBitmap.getHeight() == n))
      {
        n = 1;
        localObject = localBitmap;
      }
      else
      {
        if (j == 0)
        {
          i2 = mViewFlags;
          localObject = Bitmap.Config.ARGB_8888;
        }
        else if (i != 0)
        {
          localObject = Bitmap.Config.ARGB_8888;
        }
        else
        {
          localObject = Bitmap.Config.RGB_565;
        }
        if (localBitmap != null) {
          localBitmap.recycle();
        }
      }
      try
      {
        localObject = Bitmap.createBitmap(mResources.getDisplayMetrics(), m, n, (Bitmap.Config)localObject);
        ((Bitmap)localObject).setDensity(getResourcesgetDisplayMetricsdensityDpi);
        if (paramBoolean) {
          try
          {
            mDrawingCache = ((Bitmap)localObject);
          }
          catch (OutOfMemoryError localOutOfMemoryError1)
          {
            break label679;
          }
        } else {
          mUnscaledDrawingCache = ((Bitmap)localObject);
        }
        if ((j != 0) && (i != 0)) {
          ((Bitmap)localObject).setHasAlpha(false);
        }
        if (i1 != 0) {
          n = 1;
        } else {
          n = 0;
        }
        Canvas localCanvas;
        if (localAttachInfo != null)
        {
          localCanvas = mCanvas;
          if (localCanvas == null) {
            localCanvas = new Canvas();
          }
          localCanvas.setBitmap((Bitmap)localObject);
          mCanvas = null;
        }
        else
        {
          localCanvas = new Canvas((Bitmap)localObject);
        }
        if (n != 0) {
          ((Bitmap)localObject).eraseColor(i1);
        }
        computeScroll();
        n = localCanvas.save();
        if ((paramBoolean) && (k != 0))
        {
          float f = mApplicationScale;
          localCanvas.scale(f, f);
        }
        localCanvas.translate(-mScrollX, -mScrollY);
        mPrivateFlags |= 0x20;
        if ((mAttachInfo == null) || (!mAttachInfo.mHardwareAccelerated) || (mLayerType != 0)) {
          mPrivateFlags |= 0x8000;
        }
        if ((mPrivateFlags & 0x80) == 128)
        {
          mPrivateFlags &= 0xFF9FFFFF;
          dispatchDraw(localCanvas);
          drawAutofilledHighlight(localCanvas);
          if ((mOverlay != null) && (!mOverlay.isEmpty())) {
            mOverlay.getOverlayView().draw(localCanvas);
          }
        }
        else
        {
          draw(localCanvas);
        }
        localCanvas.restoreToCount(n);
        localCanvas.setBitmap(null);
        if (localAttachInfo != null) {
          mCanvas = localCanvas;
        }
        return;
      }
      catch (OutOfMemoryError localOutOfMemoryError2)
      {
        label679:
        if (paramBoolean) {
          mDrawingCache = null;
        } else {
          mUnscaledDrawingCache = null;
        }
        mCachingFailed = true;
        return;
      }
    }
    if ((m > 0) && (n > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(getClass().getSimpleName());
      localStringBuilder.append(" not displayed because it is too large to fit into a software layer (or drawing cache), needs ");
      localStringBuilder.append(l1);
      localStringBuilder.append(" bytes, only ");
      localStringBuilder.append(l2);
      localStringBuilder.append(" available");
      Log.w("View", localStringBuilder.toString());
    }
    destroyDrawingCache();
    mCachingFailed = true;
  }
  
  private boolean canTakeFocus()
  {
    int i = mViewFlags;
    boolean bool = true;
    if (((i & 0xC) != 0) || ((mViewFlags & 0x1) != 1) || ((mViewFlags & 0x20) != 0) || ((sCanFocusZeroSized) || (!isLayoutValid()) || (!hasSize()))) {
      bool = false;
    }
    return bool;
  }
  
  private void cancel(SendViewScrolledAccessibilityEvent paramSendViewScrolledAccessibilityEvent)
  {
    if ((paramSendViewScrolledAccessibilityEvent != null) && (mIsPending))
    {
      removeCallbacks(paramSendViewScrolledAccessibilityEvent);
      paramSendViewScrolledAccessibilityEvent.reset();
      return;
    }
  }
  
  private void checkForLongClick(int paramInt, float paramFloat1, float paramFloat2)
  {
    if (((mViewFlags & 0x200000) == 2097152) || ((mViewFlags & 0x40000000) == 1073741824))
    {
      mHasPerformedLongPress = false;
      if (mPendingCheckForLongPress == null) {
        mPendingCheckForLongPress = new CheckForLongPress(null);
      }
      mPendingCheckForLongPress.setAnchor(paramFloat1, paramFloat2);
      mPendingCheckForLongPress.rememberWindowAttachCount();
      mPendingCheckForLongPress.rememberPressedState();
      postDelayed(mPendingCheckForLongPress, ViewConfiguration.getLongPressTimeout() - paramInt);
    }
  }
  
  private void cleanupDraw()
  {
    resetDisplayList();
    if (mAttachInfo != null) {
      mAttachInfo.mViewRootImpl.cancelInvalidate(this);
    }
  }
  
  public static int combineMeasuredStates(int paramInt1, int paramInt2)
  {
    return paramInt1 | paramInt2;
  }
  
  private final void debugDrawFocus(Canvas paramCanvas)
  {
    if (isFocused())
    {
      int i = dipsToPixels(8);
      int j = mScrollX;
      int k = mRight + j - mLeft;
      int m = mScrollY;
      int n = mBottom + m - mTop;
      Paint localPaint = getDebugPaint();
      localPaint.setColor(DEBUG_CORNERS_COLOR);
      localPaint.setStyle(Paint.Style.FILL);
      paramCanvas.drawRect(j, m, j + i, m + i, localPaint);
      paramCanvas.drawRect(k - i, m, k, m + i, localPaint);
      paramCanvas.drawRect(j, n - i, j + i, n, localPaint);
      paramCanvas.drawRect(k - i, n - i, k, n, localPaint);
      localPaint.setStyle(Paint.Style.STROKE);
      paramCanvas.drawLine(j, m, k, n, localPaint);
      paramCanvas.drawLine(j, n, k, m, localPaint);
    }
  }
  
  protected static String debugIndent(int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder((paramInt * 2 + 3) * 2);
    for (int i = 0; i < paramInt * 2 + 3; i++)
    {
      localStringBuilder.append(' ');
      localStringBuilder.append(' ');
    }
    return localStringBuilder.toString();
  }
  
  private boolean dispatchGenericMotionEventInternal(MotionEvent paramMotionEvent)
  {
    ListenerInfo localListenerInfo = mListenerInfo;
    if ((localListenerInfo != null) && (mOnGenericMotionListener != null) && ((mViewFlags & 0x20) == 0) && (mOnGenericMotionListener.onGenericMotion(this, paramMotionEvent))) {
      return true;
    }
    if (onGenericMotionEvent(paramMotionEvent)) {
      return true;
    }
    int i = paramMotionEvent.getActionButton();
    switch (paramMotionEvent.getActionMasked())
    {
    default: 
      break;
    case 12: 
      if ((mInContextButtonPress) && ((i == 32) || (i == 2)))
      {
        mInContextButtonPress = false;
        mIgnoreNextUpEvent = true;
      }
      break;
    case 11: 
      if ((isContextClickable()) && (!mInContextButtonPress) && (!mHasPerformedLongPress) && ((i == 32) || (i == 2)) && (performContextClick(paramMotionEvent.getX(), paramMotionEvent.getY())))
      {
        mInContextButtonPress = true;
        setPressed(true, paramMotionEvent.getX(), paramMotionEvent.getY());
        removeTapCallback();
        removeLongPressCallback();
        return true;
      }
      break;
    }
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onUnhandledEvent(paramMotionEvent, 0);
    }
    return false;
  }
  
  private void dispatchProvideStructureForAssistOrAutofill(ViewStructure paramViewStructure, boolean paramBoolean, int paramInt)
  {
    if (paramBoolean)
    {
      paramViewStructure.setAutofillId(getAutofillId());
      onProvideAutofillStructure(paramViewStructure, paramInt);
      onProvideAutofillVirtualStructure(paramViewStructure, paramInt);
    }
    else if (!isAssistBlocked())
    {
      onProvideStructure(paramViewStructure);
      onProvideVirtualStructure(paramViewStructure);
    }
    else
    {
      paramViewStructure.setClassName(getAccessibilityClassName().toString());
      paramViewStructure.setAssistBlocked(true);
    }
  }
  
  private void drawAutofilledHighlight(Canvas paramCanvas)
  {
    if (isAutofilled())
    {
      Drawable localDrawable = getAutofilledDrawable();
      if (localDrawable != null)
      {
        localDrawable.setBounds(0, 0, getWidth(), getHeight());
        localDrawable.draw(paramCanvas);
      }
    }
  }
  
  private void drawBackground(Canvas paramCanvas)
  {
    Drawable localDrawable = mBackground;
    if (localDrawable == null) {
      return;
    }
    setBackgroundBounds();
    if ((paramCanvas.isHardwareAccelerated()) && (mAttachInfo != null) && (mAttachInfo.mThreadedRenderer != null))
    {
      mBackgroundRenderNode = getDrawableRenderNode(localDrawable, mBackgroundRenderNode);
      RenderNode localRenderNode = mBackgroundRenderNode;
      if ((localRenderNode != null) && (localRenderNode.isValid()))
      {
        setBackgroundRenderNodeProperties(localRenderNode);
        ((DisplayListCanvas)paramCanvas).drawRenderNode(localRenderNode);
        return;
      }
    }
    int i = mScrollX;
    int j = mScrollY;
    if ((i | j) == 0)
    {
      localDrawable.draw(paramCanvas);
    }
    else
    {
      paramCanvas.translate(i, j);
      localDrawable.draw(paramCanvas);
      paramCanvas.translate(-i, -j);
    }
  }
  
  private void drawDefaultFocusHighlight(Canvas paramCanvas)
  {
    if (mDefaultFocusHighlight != null)
    {
      if (mDefaultFocusHighlightSizeChanged)
      {
        mDefaultFocusHighlightSizeChanged = false;
        int i = mScrollX;
        int j = mRight;
        int k = mLeft;
        int m = mScrollY;
        int n = mBottom;
        int i1 = mTop;
        mDefaultFocusHighlight.setBounds(i, m, j + i - k, n + m - i1);
      }
      mDefaultFocusHighlight.draw(paramCanvas);
    }
  }
  
  private static void dumpFlag(HashMap<String, String> paramHashMap, String paramString, int paramInt)
  {
    String str1 = String.format("%32s", new Object[] { Integer.toBinaryString(paramInt) }).replace('0', ' ');
    paramInt = paramString.indexOf('_');
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramInt > 0) {
      str2 = paramString.substring(0, paramInt);
    } else {
      str2 = paramString;
    }
    localStringBuilder.append(str2);
    localStringBuilder.append(str1);
    localStringBuilder.append(paramString);
    String str2 = localStringBuilder.toString();
    localStringBuilder = new StringBuilder();
    localStringBuilder.append(str1);
    localStringBuilder.append(" ");
    localStringBuilder.append(paramString);
    paramHashMap.put(str2, localStringBuilder.toString());
  }
  
  private static void dumpFlags()
  {
    HashMap localHashMap = Maps.newHashMap();
    try
    {
      for (Field localField : View.class.getDeclaredFields())
      {
        int k = localField.getModifiers();
        if ((Modifier.isStatic(k)) && (Modifier.isFinal(k))) {
          if (localField.getType().equals(Integer.TYPE))
          {
            k = localField.getInt(null);
            dumpFlag(localHashMap, localField.getName(), k);
          }
          else if (localField.getType().equals([I.class))
          {
            localObject = (int[])localField.get(null);
            for (k = 0; k < localObject.length; k++)
            {
              StringBuilder localStringBuilder = new java/lang/StringBuilder;
              localStringBuilder.<init>();
              localStringBuilder.append(localField.getName());
              localStringBuilder.append("[");
              localStringBuilder.append(k);
              localStringBuilder.append("]");
              dumpFlag(localHashMap, localStringBuilder.toString(), localObject[k]);
            }
          }
        }
      }
      Object localObject = Lists.newArrayList();
      ((ArrayList)localObject).addAll(localHashMap.keySet());
      Collections.sort((List)localObject);
      localObject = ((ArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        Log.d("View", (String)localHashMap.get((String)((Iterator)localObject).next()));
      }
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new RuntimeException(localIllegalAccessException);
    }
  }
  
  private View findAccessibilityFocusHost(boolean paramBoolean)
  {
    if (isAccessibilityFocusedViewOrHost()) {
      return this;
    }
    if (paramBoolean)
    {
      Object localObject = getViewRootImpl();
      if (localObject != null)
      {
        localObject = ((ViewRootImpl)localObject).getAccessibilityFocusedHost();
        if ((localObject != null) && (ViewRootImpl.isViewDescendantOf((View)localObject, this))) {
          return localObject;
        }
      }
    }
    return null;
  }
  
  private FrameMetricsObserver findFrameMetricsObserver(Window.OnFrameMetricsAvailableListener paramOnFrameMetricsAvailableListener)
  {
    for (int i = 0; i < mFrameMetricsObservers.size(); i++)
    {
      FrameMetricsObserver localFrameMetricsObserver = (FrameMetricsObserver)mFrameMetricsObservers.get(i);
      if (mListener == paramOnFrameMetricsAvailableListener) {
        return localFrameMetricsObserver;
      }
    }
    return null;
  }
  
  private View findLabelForView(View paramView, int paramInt)
  {
    if (mMatchLabelForPredicate == null) {
      mMatchLabelForPredicate = new MatchLabelForPredicate(null);
    }
    MatchLabelForPredicate.access$1002(mMatchLabelForPredicate, paramInt);
    return findViewByPredicateInsideOut(paramView, mMatchLabelForPredicate);
  }
  
  private View findViewInsideOutShouldExist(View paramView, int paramInt)
  {
    if (mMatchIdPredicate == null) {
      mMatchIdPredicate = new MatchIdPredicate(null);
    }
    mMatchIdPredicate.mId = paramInt;
    paramView = paramView.findViewByPredicateInsideOut(this, mMatchIdPredicate);
    if (paramView == null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("couldn't find view with id ");
      localStringBuilder.append(paramInt);
      Log.w("View", localStringBuilder.toString());
    }
    return paramView;
  }
  
  private boolean fitSystemWindowsInt(Rect paramRect)
  {
    if ((mViewFlags & 0x2) == 2)
    {
      mUserPaddingStart = Integer.MIN_VALUE;
      mUserPaddingEnd = Integer.MIN_VALUE;
      Rect localRect1 = (Rect)sThreadLocal.get();
      Rect localRect2 = localRect1;
      if (localRect1 == null)
      {
        localRect2 = new Rect();
        sThreadLocal.set(localRect2);
      }
      boolean bool = computeFitSystemWindows(paramRect, localRect2);
      mUserPaddingLeftInitial = left;
      mUserPaddingRightInitial = right;
      internalSetPadding(left, top, right, bottom);
      return bool;
    }
    return false;
  }
  
  public static int generateViewId()
  {
    for (;;)
    {
      int i = sNextGeneratedId.get();
      int j = i + 1;
      int k = j;
      if (j > 16777215) {
        k = 1;
      }
      if (sNextGeneratedId.compareAndSet(i, k)) {
        return i;
      }
    }
  }
  
  private static SparseArray<String> getAttributeMap()
  {
    if (mAttributeMap == null) {
      mAttributeMap = new SparseArray();
    }
    return mAttributeMap;
  }
  
  private AutofillManager getAutofillManager()
  {
    return (AutofillManager)mContext.getSystemService(AutofillManager.class);
  }
  
  private Drawable getAutofilledDrawable()
  {
    if (mAttachInfo == null) {
      return null;
    }
    if (mAttachInfo.mAutofilledDrawable == null)
    {
      Context localContext = getRootView().getContext();
      TypedArray localTypedArray = localContext.getTheme().obtainStyledAttributes(AUTOFILL_HIGHLIGHT_ATTR);
      int i = localTypedArray.getResourceId(0, 0);
      mAttachInfo.mAutofilledDrawable = localContext.getDrawable(i);
      localTypedArray.recycle();
    }
    return mAttachInfo.mAutofilledDrawable;
  }
  
  static Paint getDebugPaint()
  {
    if (sDebugPaint == null)
    {
      sDebugPaint = new Paint();
      sDebugPaint.setAntiAlias(false);
    }
    return sDebugPaint;
  }
  
  private Drawable getDefaultFocusHighlightDrawable()
  {
    if ((mDefaultFocusHighlightCache == null) && (mContext != null))
    {
      TypedArray localTypedArray = mContext.obtainStyledAttributes(new int[] { 16843534 });
      mDefaultFocusHighlightCache = localTypedArray.getDrawable(0);
      localTypedArray.recycle();
    }
    return mDefaultFocusHighlightCache;
  }
  
  public static int getDefaultSize(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = MeasureSpec.getMode(paramInt2);
    paramInt2 = MeasureSpec.getSize(paramInt2);
    if (j != Integer.MIN_VALUE) {
      if (j != 0)
      {
        if (j != 1073741824) {
          return i;
        }
      }
      else {
        return paramInt1;
      }
    }
    paramInt1 = paramInt2;
    return paramInt1;
  }
  
  private RenderNode getDrawableRenderNode(Drawable paramDrawable, RenderNode paramRenderNode)
  {
    RenderNode localRenderNode = paramRenderNode;
    if (paramRenderNode == null) {
      localRenderNode = RenderNode.create(paramDrawable.getClass().getName(), this);
    }
    Rect localRect = paramDrawable.getBounds();
    paramRenderNode = localRenderNode.start(localRect.width(), localRect.height());
    paramRenderNode.translate(-left, -top);
    try
    {
      paramDrawable.draw(paramRenderNode);
      localRenderNode.end(paramRenderNode);
      localRenderNode.setLeftTopRightBottom(left, top, right, bottom);
      localRenderNode.setProjectBackwards(paramDrawable.isProjected());
      localRenderNode.setProjectionReceiver(true);
      localRenderNode.setClipToBounds(false);
      return localRenderNode;
    }
    finally
    {
      localRenderNode.end(paramRenderNode);
    }
  }
  
  private float getFinalAlpha()
  {
    if (mTransformationInfo != null) {
      return mTransformationInfo.mAlpha * mTransformationInfo.mTransitionAlpha;
    }
    return 1.0F;
  }
  
  private int getFocusableAttribute(TypedArray paramTypedArray)
  {
    TypedValue localTypedValue = new TypedValue();
    if (paramTypedArray.getValue(19, localTypedValue))
    {
      if (type == 18)
      {
        int i;
        if (data == 0) {
          i = 0;
        } else {
          i = 1;
        }
        return i;
      }
      return data;
    }
    return 16;
  }
  
  private void getHorizontalScrollBarBounds(Rect paramRect1, Rect paramRect2)
  {
    if (paramRect1 == null) {
      paramRect1 = paramRect2;
    }
    if (paramRect1 == null) {
      return;
    }
    int i = mViewFlags;
    int j = 0;
    if ((i & 0x2000000) == 0) {
      i = -1;
    } else {
      i = 0;
    }
    if ((isVerticalScrollBarEnabled()) && (!isVerticalScrollBarHidden())) {
      k = 1;
    } else {
      k = 0;
    }
    int m = getHorizontalScrollbarHeight();
    if (k != 0) {
      j = getVerticalScrollbarWidth();
    }
    int n = mRight;
    int i1 = mLeft;
    int k = mBottom - mTop;
    top = (mScrollY + k - m - (mUserPaddingBottom & i));
    left = (mScrollX + (mPaddingLeft & i));
    right = (mScrollX + (n - i1) - (mUserPaddingRight & i) - j);
    bottom = (top + m);
    if (paramRect2 == null) {
      return;
    }
    if (paramRect2 != paramRect1) {
      paramRect2.set(paramRect1);
    }
    i = mScrollCache.scrollBarMinTouchTarget;
    if (paramRect2.height() < i)
    {
      j = (i - paramRect2.height()) / 2;
      bottom = Math.min(bottom + j, mScrollY + k);
      top = (bottom - i);
    }
    if (paramRect2.width() < i)
    {
      j = (i - paramRect2.width()) / 2;
      left -= j;
      right = (left + i);
    }
  }
  
  private View getProjectionReceiver()
  {
    for (ViewParent localViewParent = getParent(); (localViewParent != null) && ((localViewParent instanceof View)); localViewParent = localViewParent.getParent())
    {
      View localView = (View)localViewParent;
      if (localView.isProjectionReceiver()) {
        return localView;
      }
    }
    return null;
  }
  
  private void getRoundVerticalScrollBarBounds(Rect paramRect)
  {
    int i = mRight;
    int j = mLeft;
    int k = mBottom;
    int m = mTop;
    left = mScrollX;
    top = mScrollY;
    right = (left + (i - j));
    bottom = (mScrollY + (k - m));
  }
  
  private HandlerActionQueue getRunQueue()
  {
    if (mRunQueue == null) {
      mRunQueue = new HandlerActionQueue();
    }
    return mRunQueue;
  }
  
  private ScrollabilityCache getScrollCache()
  {
    initScrollCache();
    return mScrollCache;
  }
  
  private void getStraightVerticalScrollBarBounds(Rect paramRect1, Rect paramRect2)
  {
    if (paramRect1 == null) {
      paramRect1 = paramRect2;
    }
    if (paramRect1 == null) {
      return;
    }
    if ((mViewFlags & 0x2000000) == 0) {
      i = -1;
    } else {
      i = 0;
    }
    int j = getVerticalScrollbarWidth();
    int k = mVerticalScrollbarPosition;
    int m = k;
    if (k == 0) {
      if (isLayoutRtl()) {
        m = 1;
      } else {
        m = 2;
      }
    }
    k = mRight - mLeft;
    int n = mBottom;
    int i1 = mTop;
    if (m != 1) {
      left = (mScrollX + k - j - (mUserPaddingRight & i));
    } else {
      left = (mScrollX + (mUserPaddingLeft & i));
    }
    top = (mScrollY + (mPaddingTop & i));
    right = (left + j);
    bottom = (mScrollY + (n - i1) - (mUserPaddingBottom & i));
    if (paramRect2 == null) {
      return;
    }
    if (paramRect2 != paramRect1) {
      paramRect2.set(paramRect1);
    }
    int i = mScrollCache.scrollBarMinTouchTarget;
    if (paramRect2.width() < i)
    {
      j = (i - paramRect2.width()) / 2;
      if (m == 2)
      {
        right = Math.min(right + j, mScrollX + k);
        left = (right - i);
      }
      else
      {
        left = Math.max(left + j, mScrollX);
        right = (left + i);
      }
    }
    if (paramRect2.height() < i)
    {
      m = (i - paramRect2.height()) / 2;
      top -= m;
      bottom = (top + i);
    }
  }
  
  private void getVerticalScrollBarBounds(Rect paramRect1, Rect paramRect2)
  {
    if (mRoundScrollbarRenderer == null)
    {
      getStraightVerticalScrollBarBounds(paramRect1, paramRect2);
    }
    else
    {
      if (paramRect1 == null) {
        paramRect1 = paramRect2;
      }
      getRoundVerticalScrollBarBounds(paramRect1);
    }
  }
  
  private void handleTooltipUp()
  {
    if ((mTooltipInfo != null) && (mTooltipInfo.mTooltipPopup != null))
    {
      removeCallbacks(mTooltipInfo.mHideTooltipRunnable);
      postDelayed(mTooltipInfo.mHideTooltipRunnable, ViewConfiguration.getLongPressTooltipHideTimeout());
      return;
    }
  }
  
  private boolean hasAncestorThatBlocksDescendantFocus()
  {
    boolean bool = isFocusableInTouchMode();
    Object localObject = mParent;
    while ((localObject instanceof ViewGroup))
    {
      localObject = (ViewGroup)localObject;
      if ((((ViewGroup)localObject).getDescendantFocusability() != 393216) && ((bool) || (!((ViewGroup)localObject).shouldBlockFocusForTouchscreen()))) {
        localObject = ((ViewGroup)localObject).getParent();
      } else {
        return true;
      }
    }
    return false;
  }
  
  private boolean hasListenersForAccessibility()
  {
    ListenerInfo localListenerInfo = getListenerInfo();
    boolean bool;
    if ((mTouchDelegate == null) && (mOnKeyListener == null) && (mOnTouchListener == null) && (mOnGenericMotionListener == null) && (mOnHoverListener == null) && (mOnDragListener == null)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private boolean hasParentWantsFocus()
  {
    for (Object localObject = mParent; (localObject instanceof ViewGroup); localObject = mParent)
    {
      localObject = (ViewGroup)localObject;
      if ((mPrivateFlags & 0x1) != 0) {
        return true;
      }
    }
    return false;
  }
  
  private boolean hasRtlSupport()
  {
    return mContext.getApplicationInfo().hasRtlSupport();
  }
  
  private boolean hasSize()
  {
    boolean bool;
    if ((mBottom > mTop) && (mRight > mLeft)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static View inflate(Context paramContext, int paramInt, ViewGroup paramViewGroup)
  {
    return LayoutInflater.from(paramContext).inflate(paramInt, paramViewGroup);
  }
  
  private void initScrollCache()
  {
    if (mScrollCache == null) {
      mScrollCache = new ScrollabilityCache(ViewConfiguration.get(mContext), this);
    }
  }
  
  private boolean initialAwakenScrollBars()
  {
    ScrollabilityCache localScrollabilityCache = mScrollCache;
    boolean bool = true;
    if ((localScrollabilityCache == null) || (!awakenScrollBars(mScrollCache.scrollBarDefaultDelayBeforeFade * 4, true))) {
      bool = false;
    }
    return bool;
  }
  
  private void initializeScrollIndicatorsInternal()
  {
    if (mScrollIndicatorDrawable == null) {
      mScrollIndicatorDrawable = mContext.getDrawable(17303594);
    }
  }
  
  private boolean isAccessibilityPane()
  {
    boolean bool;
    if (mAccessibilityPaneTitle != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isAutofillable()
  {
    boolean bool;
    if ((getAutofillType() != 0) && (isImportantForAutofill()) && (getAutofillViewId() > 1073741823)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean isDefaultFocusHighlightEnabled()
  {
    return sUseDefaultFocusHighlight;
  }
  
  private boolean isHoverable()
  {
    int i = mViewFlags;
    boolean bool = false;
    if ((i & 0x20) == 32) {
      return false;
    }
    if (((i & 0x4000) != 16384) && ((i & 0x200000) != 2097152) && ((i & 0x800000) != 8388608)) {
      return bool;
    }
    bool = true;
    return bool;
  }
  
  public static boolean isLayoutModeOptical(Object paramObject)
  {
    boolean bool;
    if (((paramObject instanceof ViewGroup)) && (((ViewGroup)paramObject).isLayoutModeOptical())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isOnHorizontalScrollbarThumb(float paramFloat1, float paramFloat2)
  {
    if (mScrollCache == null) {
      return false;
    }
    if (isHorizontalScrollBarEnabled())
    {
      paramFloat1 += getScrollX();
      paramFloat2 += getScrollY();
      Rect localRect1 = mScrollCache.mScrollBarBounds;
      Rect localRect2 = mScrollCache.mScrollBarTouchBounds;
      getHorizontalScrollBarBounds(localRect1, localRect2);
      int i = computeHorizontalScrollRange();
      int j = computeHorizontalScrollOffset();
      int k = computeHorizontalScrollExtent();
      int m = ScrollBarUtils.getThumbLength(localRect1.width(), localRect1.height(), k, i);
      k = ScrollBarUtils.getThumbOffset(localRect1.width(), m, k, i, j);
      i = left + k;
      k = Math.max(mScrollCache.scrollBarMinTouchTarget - m, 0) / 2;
      if ((paramFloat1 >= i - k) && (paramFloat1 <= i + m + k) && (paramFloat2 >= top) && (paramFloat2 <= bottom)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isOnVerticalScrollbarThumb(float paramFloat1, float paramFloat2)
  {
    if (mScrollCache == null) {
      return false;
    }
    if ((isVerticalScrollBarEnabled()) && (!isVerticalScrollBarHidden()))
    {
      paramFloat1 += getScrollX();
      paramFloat2 += getScrollY();
      Rect localRect1 = mScrollCache.mScrollBarBounds;
      Rect localRect2 = mScrollCache.mScrollBarTouchBounds;
      getVerticalScrollBarBounds(localRect1, localRect2);
      int i = computeVerticalScrollRange();
      int j = computeVerticalScrollOffset();
      int k = computeVerticalScrollExtent();
      int m = ScrollBarUtils.getThumbLength(localRect1.height(), localRect1.width(), k, i);
      k = ScrollBarUtils.getThumbOffset(localRect1.height(), m, k, i, j);
      i = top + k;
      k = Math.max(mScrollCache.scrollBarMinTouchTarget - m, 0) / 2;
      if ((paramFloat1 >= left) && (paramFloat1 <= right) && (paramFloat2 >= i - k) && (paramFloat2 <= i + m + k)) {
        return true;
      }
    }
    return false;
  }
  
  private boolean isProjectionReceiver()
  {
    boolean bool;
    if (mBackground != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private boolean isRtlCompatibilityMode()
  {
    boolean bool;
    if ((getContextgetApplicationInfotargetSdkVersion >= 17) && (hasRtlSupport())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  private static boolean isViewIdGenerated(int paramInt)
  {
    boolean bool;
    if (((0xFF000000 & paramInt) == 0) && ((0xFFFFFF & paramInt) != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected static int[] mergeDrawableStates(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    for (int i = paramArrayOfInt1.length - 1; (i >= 0) && (paramArrayOfInt1[i] == 0); i--) {}
    System.arraycopy(paramArrayOfInt2, 0, paramArrayOfInt1, i + 1, paramArrayOfInt2.length);
    return paramArrayOfInt1;
  }
  
  private boolean needRtlPropertiesResolution()
  {
    boolean bool;
    if ((mPrivateFlags2 & 0x60010220) != 1610678816) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void notifyAutofillManagerOnClick()
  {
    if ((mPrivateFlags & 0x20000000) != 0) {
      try
      {
        getAutofillManager().notifyViewClicked(this);
        mPrivateFlags = (0xDFFFFFFF & mPrivateFlags);
      }
      finally
      {
        mPrivateFlags = (0xDFFFFFFF & mPrivateFlags);
      }
    }
  }
  
  private static int numViewsForAccessibility(View paramView)
  {
    if (paramView != null)
    {
      if (paramView.includeForAccessibility()) {
        return 1;
      }
      if ((paramView instanceof ViewGroup)) {
        return ((ViewGroup)paramView).getNumChildrenForAccessibility();
      }
    }
    return 0;
  }
  
  private void onDrawScrollIndicators(Canvas paramCanvas)
  {
    if ((mPrivateFlags3 & 0x3F00) == 0) {
      return;
    }
    Drawable localDrawable = mScrollIndicatorDrawable;
    if (localDrawable == null) {
      return;
    }
    int i = localDrawable.getIntrinsicHeight();
    int j = localDrawable.getIntrinsicWidth();
    Rect localRect = mAttachInfo.mTmpInvalRect;
    getScrollIndicatorBounds(localRect);
    if (((mPrivateFlags3 & 0x100) != 0) && (canScrollVertically(-1)))
    {
      localDrawable.setBounds(left, top, right, top + i);
      localDrawable.draw(paramCanvas);
    }
    if (((mPrivateFlags3 & 0x200) != 0) && (canScrollVertically(1)))
    {
      localDrawable.setBounds(left, bottom - i, right, bottom);
      localDrawable.draw(paramCanvas);
    }
    int k;
    if (getLayoutDirection() == 1)
    {
      i = 8192;
      k = 4096;
    }
    else
    {
      i = 4096;
      k = 8192;
    }
    if (((mPrivateFlags3 & (0x400 | i)) != 0) && (canScrollHorizontally(-1)))
    {
      localDrawable.setBounds(left, top, left + j, bottom);
      localDrawable.draw(paramCanvas);
    }
    if (((mPrivateFlags3 & (0x800 | k)) != 0) && (canScrollHorizontally(1)))
    {
      localDrawable.setBounds(right - j, top, right, bottom);
      localDrawable.draw(paramCanvas);
    }
  }
  
  private void onProvideStructureForAssistOrAutofill(ViewStructure paramViewStructure, boolean paramBoolean, int paramInt)
  {
    int i = mID;
    Object localObject1 = null;
    Object localObject4;
    Object localObject3;
    if ((i != -1) && (!isViewIdGenerated(i)))
    {
      try
      {
        Object localObject2 = getResources();
        String str = ((Resources)localObject2).getResourceEntryName(i);
        localObject4 = ((Resources)localObject2).getResourceTypeName(i);
        localObject2 = ((Resources)localObject2).getResourcePackageName(i);
        localObject1 = str;
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        localObject3 = null;
        localObject4 = null;
      }
      paramViewStructure.setId(i, (String)localObject3, (String)localObject4, localObject1);
    }
    else
    {
      paramViewStructure.setId(i, null, null, null);
    }
    if (paramBoolean)
    {
      i = getAutofillType();
      if (i != 0)
      {
        paramViewStructure.setAutofillType(i);
        paramViewStructure.setAutofillHints(getAutofillHints());
        paramViewStructure.setAutofillValue(getAutofillValue());
      }
      paramViewStructure.setImportantForAutofill(getImportantForAutofill());
    }
    int j = 0;
    int k = 0;
    int m = 0;
    int n = 0;
    i = j;
    int i1 = m;
    if (paramBoolean)
    {
      i = j;
      i1 = m;
      if ((paramInt & 0x1) == 0)
      {
        localObject3 = null;
        localObject4 = getParent();
        paramInt = k;
        j = n;
        if ((localObject4 instanceof View))
        {
          localObject3 = (View)localObject4;
          j = n;
          paramInt = k;
        }
        for (;;)
        {
          i = paramInt;
          i1 = j;
          if (localObject3 == null) {
            break;
          }
          i = paramInt;
          i1 = j;
          if (((View)localObject3).isImportantForAutofill()) {
            break;
          }
          paramInt += mLeft;
          j += mTop;
          localObject3 = ((View)localObject3).getParent();
          i = paramInt;
          i1 = j;
          if (!(localObject3 instanceof View)) {
            break;
          }
          localObject3 = (View)localObject3;
        }
      }
    }
    paramViewStructure.setDimens(i + mLeft, i1 + mTop, mScrollX, mScrollY, mRight - mLeft, mBottom - mTop);
    if (!paramBoolean)
    {
      if (!hasIdentityMatrix()) {
        paramViewStructure.setTransformation(getMatrix());
      }
      paramViewStructure.setElevation(getZ());
    }
    paramViewStructure.setVisibility(getVisibility());
    paramViewStructure.setEnabled(isEnabled());
    if (isClickable()) {
      paramViewStructure.setClickable(true);
    }
    if (isFocusable()) {
      paramViewStructure.setFocusable(true);
    }
    if (isFocused()) {
      paramViewStructure.setFocused(true);
    }
    if (isAccessibilityFocused()) {
      paramViewStructure.setAccessibilityFocused(true);
    }
    if (isSelected()) {
      paramViewStructure.setSelected(true);
    }
    if (isActivated()) {
      paramViewStructure.setActivated(true);
    }
    if (isLongClickable()) {
      paramViewStructure.setLongClickable(true);
    }
    if ((this instanceof Checkable))
    {
      paramViewStructure.setCheckable(true);
      if (((Checkable)this).isChecked()) {
        paramViewStructure.setChecked(true);
      }
    }
    if (isOpaque()) {
      paramViewStructure.setOpaque(true);
    }
    if (isContextClickable()) {
      paramViewStructure.setContextClickable(true);
    }
    paramViewStructure.setClassName(getAccessibilityClassName().toString());
    paramViewStructure.setContentDescription(getContentDescription());
  }
  
  private void onProvideVirtualStructureCompat(ViewStructure paramViewStructure, boolean paramBoolean)
  {
    AccessibilityNodeProvider localAccessibilityNodeProvider = getAccessibilityNodeProvider();
    if (localAccessibilityNodeProvider != null)
    {
      if ((Helper.sVerbose) && (paramBoolean))
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("onProvideVirtualStructureCompat() for ");
        ((StringBuilder)localObject).append(this);
        Log.v("View", ((StringBuilder)localObject).toString());
      }
      Object localObject = createAccessibilityNodeInfo();
      paramViewStructure.setChildCount(1);
      populateVirtualStructure(paramViewStructure.newChild(0), localAccessibilityNodeProvider, (AccessibilityNodeInfo)localObject, paramBoolean);
      ((AccessibilityNodeInfo)localObject).recycle();
    }
  }
  
  private boolean performClickInternal()
  {
    notifyAutofillManagerOnClick();
    return performClick();
  }
  
  private boolean performLongClickInternal(float paramFloat1, float paramFloat2)
  {
    sendAccessibilityEvent(2);
    boolean bool1 = false;
    ListenerInfo localListenerInfo = mListenerInfo;
    boolean bool2 = bool1;
    if (localListenerInfo != null)
    {
      bool2 = bool1;
      if (mOnLongClickListener != null) {
        bool2 = mOnLongClickListener.onLongClick(this);
      }
    }
    bool1 = bool2;
    if (!bool2)
    {
      int i;
      if ((!Float.isNaN(paramFloat1)) && (!Float.isNaN(paramFloat2))) {
        i = 1;
      } else {
        i = 0;
      }
      if (i != 0) {
        bool2 = showContextMenu(paramFloat1, paramFloat2);
      } else {
        bool2 = showContextMenu();
      }
      bool1 = bool2;
    }
    bool2 = bool1;
    if ((mViewFlags & 0x40000000) == 1073741824)
    {
      bool2 = bool1;
      if (!bool1) {
        bool2 = showLongClickTooltip((int)paramFloat1, (int)paramFloat2);
      }
    }
    if (bool2) {
      performHapticFeedback(0);
    }
    return bool2;
  }
  
  private void populateAccessibilityNodeInfoDrawingOrderInParent(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    if ((mPrivateFlags & 0x10) == 0)
    {
      paramAccessibilityNodeInfo.setDrawingOrder(0);
      return;
    }
    int i = 1;
    View localView = this;
    ViewParent localViewParent1 = getParentForAccessibility();
    int j;
    for (;;)
    {
      j = i;
      if (localView == localViewParent1) {
        break;
      }
      ViewParent localViewParent2 = localView.getParent();
      if (!(localViewParent2 instanceof ViewGroup))
      {
        j = 0;
        break;
      }
      ViewGroup localViewGroup = (ViewGroup)localViewParent2;
      int k = localViewGroup.getChildCount();
      int m = i;
      if (k > 1)
      {
        ArrayList localArrayList = localViewGroup.buildOrderedChildList();
        if (localArrayList != null)
        {
          m = localArrayList.indexOf(localView);
          for (j = 0; j < m; j++) {
            i += numViewsForAccessibility((View)localArrayList.get(j));
          }
          m = i;
        }
        else
        {
          j = localViewGroup.indexOfChild(localView);
          boolean bool = localViewGroup.isChildrenDrawingOrderEnabled();
          if ((j >= 0) && (bool)) {
            j = localViewGroup.getChildDrawingOrder(k, j);
          }
          int n;
          if (bool) {
            n = k;
          } else {
            n = j;
          }
          m = i;
          if (j != 0)
          {
            int i1 = 0;
            m = i;
            i = i1;
            while (i < n)
            {
              int i2;
              if (bool) {
                i2 = localViewGroup.getChildDrawingOrder(k, i);
              } else {
                i2 = i;
              }
              i1 = m;
              if (i2 < j) {
                i1 = m + numViewsForAccessibility(localViewGroup.getChildAt(i));
              }
              i++;
              m = i1;
            }
          }
        }
      }
      localView = (View)localViewParent2;
      i = m;
    }
    paramAccessibilityNodeInfo.setDrawingOrder(j);
  }
  
  private void populateVirtualStructure(ViewStructure paramViewStructure, AccessibilityNodeProvider paramAccessibilityNodeProvider, AccessibilityNodeInfo paramAccessibilityNodeInfo, boolean paramBoolean)
  {
    int i = AccessibilityNodeInfo.getVirtualDescendantId(paramAccessibilityNodeInfo.getSourceNodeId());
    Object localObject1 = paramAccessibilityNodeInfo.getViewIdResourceName();
    Object localObject2 = null;
    paramViewStructure.setId(i, null, null, (String)localObject1);
    localObject1 = paramViewStructure.getTempRect();
    paramAccessibilityNodeInfo.getBoundsInParent((Rect)localObject1);
    paramViewStructure.setDimens(left, top, 0, 0, ((Rect)localObject1).width(), ((Rect)localObject1).height());
    int j = 0;
    paramViewStructure.setVisibility(0);
    paramViewStructure.setEnabled(paramAccessibilityNodeInfo.isEnabled());
    if (paramAccessibilityNodeInfo.isClickable()) {
      paramViewStructure.setClickable(true);
    }
    if (paramAccessibilityNodeInfo.isFocusable()) {
      paramViewStructure.setFocusable(true);
    }
    if (paramAccessibilityNodeInfo.isFocused()) {
      paramViewStructure.setFocused(true);
    }
    if (paramAccessibilityNodeInfo.isAccessibilityFocused()) {
      paramViewStructure.setAccessibilityFocused(true);
    }
    if (paramAccessibilityNodeInfo.isSelected()) {
      paramViewStructure.setSelected(true);
    }
    if (paramAccessibilityNodeInfo.isLongClickable()) {
      paramViewStructure.setLongClickable(true);
    }
    if (paramAccessibilityNodeInfo.isCheckable())
    {
      paramViewStructure.setCheckable(true);
      if (paramAccessibilityNodeInfo.isChecked()) {
        paramViewStructure.setChecked(true);
      }
    }
    if (paramAccessibilityNodeInfo.isContextClickable()) {
      paramViewStructure.setContextClickable(true);
    }
    if (paramBoolean) {
      paramViewStructure.setAutofillId(new AutofillId(getAutofillId(), AccessibilityNodeInfo.getVirtualDescendantId(paramAccessibilityNodeInfo.getSourceNodeId())));
    }
    localObject1 = paramAccessibilityNodeInfo.getClassName();
    if (localObject1 != null) {
      localObject2 = ((CharSequence)localObject1).toString();
    }
    paramViewStructure.setClassName((String)localObject2);
    paramViewStructure.setContentDescription(paramAccessibilityNodeInfo.getContentDescription());
    if (paramBoolean)
    {
      i = paramAccessibilityNodeInfo.getMaxTextLength();
      if (i != -1) {
        paramViewStructure.setMaxTextLength(i);
      }
      paramViewStructure.setHint(paramAccessibilityNodeInfo.getHintText());
    }
    localObject2 = paramAccessibilityNodeInfo.getText();
    if ((localObject2 == null) && (paramAccessibilityNodeInfo.getError() == null)) {
      i = 0;
    } else {
      i = 1;
    }
    if (i != 0) {
      paramViewStructure.setText((CharSequence)localObject2, paramAccessibilityNodeInfo.getTextSelectionStart(), paramAccessibilityNodeInfo.getTextSelectionEnd());
    }
    if (paramBoolean) {
      if (paramAccessibilityNodeInfo.isEditable())
      {
        paramViewStructure.setDataIsSensitive(true);
        if (i != 0)
        {
          paramViewStructure.setAutofillType(1);
          paramViewStructure.setAutofillValue(AutofillValue.forText((CharSequence)localObject2));
        }
        k = paramAccessibilityNodeInfo.getInputType();
        i = k;
        if (k == 0)
        {
          i = k;
          if (paramAccessibilityNodeInfo.isPassword()) {
            i = 129;
          }
        }
        paramViewStructure.setInputType(i);
      }
      else
      {
        paramViewStructure.setDataIsSensitive(false);
      }
    }
    int k = paramAccessibilityNodeInfo.getChildCount();
    if (k > 0)
    {
      paramViewStructure.setChildCount(k);
      for (i = j; i < k; i++) {
        if (AccessibilityNodeInfo.getVirtualDescendantId(paramAccessibilityNodeInfo.getChildNodeIds().get(i)) == -1)
        {
          Log.e("View", "Virtual view pointing to its host. Ignoring");
        }
        else
        {
          localObject2 = paramAccessibilityNodeProvider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(paramAccessibilityNodeInfo.getChildId(i)));
          populateVirtualStructure(paramViewStructure.newChild(i), paramAccessibilityNodeProvider, (AccessibilityNodeInfo)localObject2, paramBoolean);
          ((AccessibilityNodeInfo)localObject2).recycle();
        }
      }
    }
  }
  
  private void postSendViewScrolledAccessibilityEventCallback(int paramInt1, int paramInt2)
  {
    if (mSendViewScrolledAccessibilityEvent == null) {
      mSendViewScrolledAccessibilityEvent = new SendViewScrolledAccessibilityEvent(null);
    }
    mSendViewScrolledAccessibilityEvent.post(paramInt1, paramInt2);
  }
  
  private static String printFlags(int paramInt)
  {
    Object localObject1 = "";
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("");
      ((StringBuilder)localObject1).append("TAKES_FOCUS");
      localObject1 = ((StringBuilder)localObject1).toString();
      i = 0 + 1;
    }
    paramInt &= 0xC;
    Object localObject2;
    if (paramInt != 4)
    {
      if (paramInt == 8)
      {
        localObject2 = localObject1;
        if (i > 0)
        {
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append((String)localObject1);
          ((StringBuilder)localObject2).append(" ");
          localObject2 = ((StringBuilder)localObject2).toString();
        }
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append((String)localObject2);
        ((StringBuilder)localObject1).append("GONE");
        localObject1 = ((StringBuilder)localObject1).toString();
      }
    }
    else
    {
      localObject2 = localObject1;
      if (i > 0)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" ");
        localObject2 = ((StringBuilder)localObject2).toString();
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("INVISIBLE");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    return localObject1;
  }
  
  private static String printPrivateFlags(int paramInt)
  {
    Object localObject1 = "";
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("");
      ((StringBuilder)localObject1).append("WANTS_FOCUS");
      localObject1 = ((StringBuilder)localObject1).toString();
      i = 0 + 1;
    }
    Object localObject2 = localObject1;
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localObject2 = localObject1;
      if (i > 0)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" ");
        localObject2 = ((StringBuilder)localObject2).toString();
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("FOCUSED");
      localObject2 = ((StringBuilder)localObject1).toString();
      j = i + 1;
    }
    localObject1 = localObject2;
    i = j;
    if ((paramInt & 0x4) == 4)
    {
      localObject1 = localObject2;
      if (j > 0)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append((String)localObject2);
        ((StringBuilder)localObject1).append(" ");
        localObject1 = ((StringBuilder)localObject1).toString();
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("SELECTED");
      localObject1 = ((StringBuilder)localObject2).toString();
      i = j + 1;
    }
    localObject2 = localObject1;
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localObject2 = localObject1;
      if (i > 0)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" ");
        localObject2 = ((StringBuilder)localObject2).toString();
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("IS_ROOT_NAMESPACE");
      localObject2 = ((StringBuilder)localObject1).toString();
      j = i + 1;
    }
    localObject1 = localObject2;
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localObject1 = localObject2;
      if (j > 0)
      {
        localObject1 = new StringBuilder();
        ((StringBuilder)localObject1).append((String)localObject2);
        ((StringBuilder)localObject1).append(" ");
        localObject1 = ((StringBuilder)localObject1).toString();
      }
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("HAS_BOUNDS");
      localObject1 = ((StringBuilder)localObject2).toString();
      i = j + 1;
    }
    localObject2 = localObject1;
    if ((paramInt & 0x20) == 32)
    {
      localObject2 = localObject1;
      if (i > 0)
      {
        localObject2 = new StringBuilder();
        ((StringBuilder)localObject2).append((String)localObject1);
        ((StringBuilder)localObject2).append(" ");
        localObject2 = ((StringBuilder)localObject2).toString();
      }
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append("DRAWN");
      localObject2 = ((StringBuilder)localObject1).toString();
    }
    return localObject2;
  }
  
  private void rebuildOutline()
  {
    if (mAttachInfo == null) {
      return;
    }
    if (mOutlineProvider == null)
    {
      mRenderNode.setOutline(null);
    }
    else
    {
      Outline localOutline = mAttachInfo.mTmpOutline;
      localOutline.setEmpty();
      localOutline.setAlpha(1.0F);
      mOutlineProvider.getOutline(this, localOutline);
      mRenderNode.setOutline(localOutline);
    }
  }
  
  private void registerPendingFrameMetricsObservers()
  {
    if (mFrameMetricsObservers != null)
    {
      ThreadedRenderer localThreadedRenderer = getThreadedRenderer();
      if (localThreadedRenderer != null)
      {
        Iterator localIterator = mFrameMetricsObservers.iterator();
        while (localIterator.hasNext()) {
          localThreadedRenderer.addFrameMetricsObserver((FrameMetricsObserver)localIterator.next());
        }
      }
      Log.w("View", "View not hardware-accelerated. Unable to observe frame stats");
    }
  }
  
  private void removeLongPressCallback()
  {
    if (mPendingCheckForLongPress != null) {
      removeCallbacks(mPendingCheckForLongPress);
    }
  }
  
  private void removePerformClickCallback()
  {
    if (mPerformClick != null) {
      removeCallbacks(mPerformClick);
    }
  }
  
  private void removeTapCallback()
  {
    if (mPendingCheckForTap != null)
    {
      mPrivateFlags &= 0xFDFFFFFF;
      removeCallbacks(mPendingCheckForTap);
    }
  }
  
  private void removeUnsetPressCallback()
  {
    if (((mPrivateFlags & 0x4000) != 0) && (mUnsetPressedState != null))
    {
      setPressed(false);
      removeCallbacks(mUnsetPressedState);
    }
  }
  
  private boolean requestFocusNoSearch(int paramInt, Rect paramRect)
  {
    if (!canTakeFocus()) {
      return false;
    }
    if ((isInTouchMode()) && (262144 != (mViewFlags & 0x40000))) {
      return false;
    }
    if (hasAncestorThatBlocksDescendantFocus()) {
      return false;
    }
    if (!isLayoutValid()) {
      mPrivateFlags |= 0x1;
    } else {
      clearParentsWantFocus();
    }
    handleFocusGainInternal(paramInt, paramRect);
    return true;
  }
  
  private void resetDisplayList()
  {
    mRenderNode.discardDisplayList();
    if (mBackgroundRenderNode != null) {
      mBackgroundRenderNode.discardDisplayList();
    }
  }
  
  private void resetPressedState()
  {
    if ((mViewFlags & 0x20) == 32) {
      return;
    }
    if (isPressed())
    {
      setPressed(false);
      if (!mHasPerformedLongPress) {
        removeLongPressCallback();
      }
    }
  }
  
  public static int resolveSize(int paramInt1, int paramInt2)
  {
    return resolveSizeAndState(paramInt1, paramInt2, 0) & 0xFFFFFF;
  }
  
  public static int resolveSizeAndState(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = MeasureSpec.getMode(paramInt2);
    paramInt2 = MeasureSpec.getSize(paramInt2);
    if (i != Integer.MIN_VALUE)
    {
      if (i == 1073741824) {
        paramInt1 = paramInt2;
      }
    }
    else if (paramInt2 < paramInt1) {
      paramInt1 = 0x1000000 | paramInt2;
    }
    return 0xFF000000 & paramInt3 | paramInt1;
  }
  
  private static float sanitizeFloatPropertyValue(float paramFloat, String paramString)
  {
    return sanitizeFloatPropertyValue(paramFloat, paramString, -3.4028235E38F, Float.MAX_VALUE);
  }
  
  private static float sanitizeFloatPropertyValue(float paramFloat1, String paramString, float paramFloat2, float paramFloat3)
  {
    if ((paramFloat1 >= paramFloat2) && (paramFloat1 <= paramFloat3)) {
      return paramFloat1;
    }
    if ((paramFloat1 >= paramFloat2) && (paramFloat1 != Float.NEGATIVE_INFINITY))
    {
      if ((paramFloat1 <= paramFloat3) && (paramFloat1 != Float.POSITIVE_INFINITY))
      {
        if (Float.isNaN(paramFloat1))
        {
          if (!sThrowOnInvalidFloatProperties) {
            return 0.0F;
          }
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Cannot set '");
          localStringBuilder.append(paramString);
          localStringBuilder.append("' to Float.NaN");
          throw new IllegalArgumentException(localStringBuilder.toString());
        }
        paramString = new StringBuilder();
        paramString.append("How do you get here?? ");
        paramString.append(paramFloat1);
        throw new IllegalStateException(paramString.toString());
      }
      if (!sThrowOnInvalidFloatProperties) {
        return paramFloat3;
      }
      localStringBuilder = new StringBuilder();
      localStringBuilder.append("Cannot set '");
      localStringBuilder.append(paramString);
      localStringBuilder.append("' to ");
      localStringBuilder.append(paramFloat1);
      localStringBuilder.append(", the value must be <= ");
      localStringBuilder.append(paramFloat3);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    if (!sThrowOnInvalidFloatProperties) {
      return paramFloat2;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("Cannot set '");
    localStringBuilder.append(paramString);
    localStringBuilder.append("' to ");
    localStringBuilder.append(paramFloat1);
    localStringBuilder.append(", the value must be >= ");
    localStringBuilder.append(paramFloat2);
    throw new IllegalArgumentException(localStringBuilder.toString());
  }
  
  private void saveAttributeData(AttributeSet paramAttributeSet, TypedArray paramTypedArray)
  {
    if (paramAttributeSet == null) {
      i = 0;
    } else {
      i = paramAttributeSet.getAttributeCount();
    }
    int j = paramTypedArray.getIndexCount();
    String[] arrayOfString = new String[(i + j) * 2];
    int k = 0;
    for (int m = 0; m < i; m++)
    {
      arrayOfString[k] = paramAttributeSet.getAttributeName(m);
      arrayOfString[(k + 1)] = paramAttributeSet.getAttributeValue(m);
      k += 2;
    }
    Resources localResources = paramTypedArray.getResources();
    SparseArray localSparseArray = getAttributeMap();
    m = 0;
    int i = k;
    for (k = m; k < j; k++)
    {
      int n = paramTypedArray.getIndex(k);
      if (paramTypedArray.hasValueOrEmpty(n))
      {
        m = paramTypedArray.getResourceId(n, 0);
        if (m != 0)
        {
          String str = (String)localSparseArray.get(m);
          paramAttributeSet = str;
          if (str == null)
          {
            try
            {
              paramAttributeSet = localResources.getResourceName(m);
            }
            catch (Resources.NotFoundException paramAttributeSet)
            {
              for (;;)
              {
                paramAttributeSet = new StringBuilder();
                paramAttributeSet.append("0x");
                paramAttributeSet.append(Integer.toHexString(m));
                paramAttributeSet = paramAttributeSet.toString();
              }
            }
            localSparseArray.put(m, paramAttributeSet);
          }
          arrayOfString[i] = paramAttributeSet;
          arrayOfString[(i + 1)] = paramTypedArray.getString(n);
          i += 2;
        }
      }
    }
    paramAttributeSet = new String[i];
    System.arraycopy(arrayOfString, 0, paramAttributeSet, 0, i);
    mAttributes = paramAttributeSet;
  }
  
  private void sendAccessibilityHoverEvent(int paramInt)
  {
    for (Object localObject = this;; localObject = (View)localObject)
    {
      if (((View)localObject).includeForAccessibility())
      {
        ((View)localObject).sendAccessibilityEvent(paramInt);
        return;
      }
      localObject = ((View)localObject).getParent();
      if (!(localObject instanceof View)) {
        break;
      }
    }
  }
  
  private void sendViewTextTraversedAtGranularityEvent(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (mParent == null) {
      return;
    }
    AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(131072);
    onInitializeAccessibilityEvent(localAccessibilityEvent);
    onPopulateAccessibilityEvent(localAccessibilityEvent);
    localAccessibilityEvent.setFromIndex(paramInt3);
    localAccessibilityEvent.setToIndex(paramInt4);
    localAccessibilityEvent.setAction(paramInt1);
    localAccessibilityEvent.setMovementGranularity(paramInt2);
    mParent.requestSendAccessibilityEvent(this, localAccessibilityEvent);
  }
  
  private void setAlphaInternal(float paramFloat)
  {
    float f = mTransformationInfo.mAlpha;
    mTransformationInfo.mAlpha = paramFloat;
    int i = 0;
    int j;
    if (paramFloat == 0.0F) {
      j = 1;
    } else {
      j = 0;
    }
    if (f == 0.0F) {
      i = 1;
    }
    if ((j ^ i) != 0) {
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  private void setBackgroundRenderNodeProperties(RenderNode paramRenderNode)
  {
    paramRenderNode.setTranslationX(mScrollX);
    paramRenderNode.setTranslationY(mScrollY);
  }
  
  private void setDefaultFocusHighlight(Drawable paramDrawable)
  {
    mDefaultFocusHighlight = paramDrawable;
    boolean bool = true;
    mDefaultFocusHighlightSizeChanged = true;
    if (paramDrawable != null)
    {
      if ((mPrivateFlags & 0x80) != 0) {
        mPrivateFlags &= 0xFF7F;
      }
      paramDrawable.setLayoutDirection(getLayoutDirection());
      if (paramDrawable.isStateful()) {
        paramDrawable.setState(getDrawableState());
      }
      if (isAttachedToWindow())
      {
        if ((getWindowVisibility() != 0) || (!isShown())) {
          bool = false;
        }
        paramDrawable.setVisible(bool, false);
      }
      paramDrawable.setCallback(this);
    }
    else if (((mViewFlags & 0x80) != 0) && (mBackground == null) && ((mForegroundInfo == null) || (mForegroundInfo.mDrawable == null)))
    {
      mPrivateFlags |= 0x80;
    }
    invalidate();
  }
  
  private void setFocusedInCluster(View paramView)
  {
    if ((this instanceof ViewGroup)) {
      mFocusedInCluster = null;
    }
    if (paramView == this) {
      return;
    }
    ViewParent localViewParent = mParent;
    View localView = this;
    while ((localViewParent instanceof ViewGroup))
    {
      mFocusedInCluster = localView;
      if (localViewParent == paramView) {
        break;
      }
      localView = (View)localViewParent;
      localViewParent = localViewParent.getParent();
    }
  }
  
  private void setKeyedTag(int paramInt, Object paramObject)
  {
    if (mKeyedTags == null) {
      mKeyedTags = new SparseArray(2);
    }
    mKeyedTags.put(paramInt, paramObject);
  }
  
  private void setMeasuredDimensionRaw(int paramInt1, int paramInt2)
  {
    mMeasuredWidth = paramInt1;
    mMeasuredHeight = paramInt2;
    mPrivateFlags |= 0x800;
  }
  
  private boolean setOpticalFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Insets localInsets1;
    if ((mParent instanceof View)) {
      localInsets1 = ((View)mParent).getOpticalInsets();
    } else {
      localInsets1 = Insets.NONE;
    }
    Insets localInsets2 = getOpticalInsets();
    return setFrame(left + paramInt1 - left, top + paramInt2 - top, left + paramInt3 + right, top + paramInt4 + bottom);
  }
  
  private void setOutlineProviderFromAttribute(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      break;
    case 3: 
      setOutlineProvider(ViewOutlineProvider.PADDED_BOUNDS);
      break;
    case 2: 
      setOutlineProvider(ViewOutlineProvider.BOUNDS);
      break;
    case 1: 
      setOutlineProvider(null);
      break;
    case 0: 
      setOutlineProvider(ViewOutlineProvider.BACKGROUND);
    }
  }
  
  private void setPressed(boolean paramBoolean, float paramFloat1, float paramFloat2)
  {
    if (paramBoolean) {
      drawableHotspotChanged(paramFloat1, paramFloat2);
    }
    setPressed(paramBoolean);
  }
  
  private boolean showHoverTooltip()
  {
    return showTooltip(mTooltipInfo.mAnchorX, mTooltipInfo.mAnchorY, false);
  }
  
  private boolean showLongClickTooltip(int paramInt1, int paramInt2)
  {
    removeCallbacks(mTooltipInfo.mShowTooltipRunnable);
    removeCallbacks(mTooltipInfo.mHideTooltipRunnable);
    return showTooltip(paramInt1, paramInt2, true);
  }
  
  private boolean showTooltip(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((mAttachInfo != null) && (mTooltipInfo != null))
    {
      if ((paramBoolean) && ((mViewFlags & 0x20) != 0)) {
        return false;
      }
      if (TextUtils.isEmpty(mTooltipInfo.mTooltipText)) {
        return false;
      }
      hideTooltip();
      mTooltipInfo.mTooltipFromLongClick = paramBoolean;
      mTooltipInfo.mTooltipPopup = new TooltipPopup(getContext());
      if ((mPrivateFlags3 & 0x20000) == 131072) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      mTooltipInfo.mTooltipPopup.show(this, paramInt1, paramInt2, paramBoolean, mTooltipInfo.mTooltipText);
      mAttachInfo.mTooltipHost = this;
      notifyViewAccessibilityStateChangedIfNeeded(0);
      return true;
    }
    return false;
  }
  
  private void sizeChange(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (mOverlay != null)
    {
      mOverlay.getOverlayView().setRight(paramInt1);
      mOverlay.getOverlayView().setBottom(paramInt2);
    }
    if ((!sCanFocusZeroSized) && (isLayoutValid()) && ((!(mParent instanceof ViewGroup)) || (!((ViewGroup)mParent).isLayoutSuppressed()))) {
      if ((paramInt1 > 0) && (paramInt2 > 0))
      {
        if (((paramInt3 <= 0) || (paramInt4 <= 0)) && (mParent != null) && (canTakeFocus())) {
          mParent.focusableViewAvailable(this);
        }
      }
      else
      {
        if (hasFocus())
        {
          clearFocus();
          if ((mParent instanceof ViewGroup)) {
            ((ViewGroup)mParent).clearFocusedInCluster();
          }
        }
        clearAccessibilityFocus();
      }
    }
    rebuildOutline();
  }
  
  private boolean skipInvalidate()
  {
    boolean bool;
    if (((mViewFlags & 0xC) != 0) && (mCurrentAnimation == null) && ((!(mParent instanceof ViewGroup)) || (!((ViewGroup)mParent).isViewTransitioning(this)))) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  private void switchDefaultFocusHighlight()
  {
    if (isFocused())
    {
      Drawable localDrawable1 = mBackground;
      Drawable localDrawable2;
      if (mForegroundInfo == null) {
        localDrawable2 = null;
      } else {
        localDrawable2 = mForegroundInfo.mDrawable;
      }
      boolean bool = isDefaultFocusHighlightNeeded(localDrawable1, localDrawable2);
      int i;
      if (mDefaultFocusHighlight != null) {
        i = 1;
      } else {
        i = 0;
      }
      if ((bool) && (i == 0)) {
        setDefaultFocusHighlight(getDefaultFocusHighlightDrawable());
      } else if ((!bool) && (i != 0)) {
        setDefaultFocusHighlight(null);
      }
    }
  }
  
  private boolean traverseAtGranularity(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    Object localObject = getIterableTextForAccessibility();
    if ((localObject != null) && (((CharSequence)localObject).length() != 0))
    {
      AccessibilityIterators.TextSegmentIterator localTextSegmentIterator = getIteratorForGranularity(paramInt);
      if (localTextSegmentIterator == null) {
        return false;
      }
      int i = getAccessibilitySelectionEnd();
      int j = i;
      if (i == -1) {
        if (paramBoolean1) {
          j = 0;
        } else {
          j = ((CharSequence)localObject).length();
        }
      }
      if (paramBoolean1) {
        localObject = localTextSegmentIterator.following(j);
      } else {
        localObject = localTextSegmentIterator.preceding(j);
      }
      if (localObject == null) {
        return false;
      }
      int k = localObject[0];
      i = localObject[1];
      int m;
      if ((paramBoolean2) && (isAccessibilitySelectionExtendable()))
      {
        m = getAccessibilitySelectionStart();
        j = m;
        if (m == -1) {
          if (paramBoolean1) {
            j = k;
          } else {
            j = i;
          }
        }
        if (paramBoolean1) {
          m = i;
        } else {
          m = k;
        }
      }
      else
      {
        if (paramBoolean1) {
          j = i;
        } else {
          j = k;
        }
        m = j;
      }
      setAccessibilitySelection(j, m);
      if (paramBoolean1) {
        j = 256;
      } else {
        j = 512;
      }
      sendViewTextTraversedAtGranularityEvent(j, paramInt, k, i);
      return true;
    }
    return false;
  }
  
  private void updateFocusedInCluster(View paramView, int paramInt)
  {
    if (paramView != null)
    {
      View localView = paramView.findKeyboardNavigationCluster();
      if (localView != findKeyboardNavigationCluster())
      {
        paramView.setFocusedInCluster(localView);
        if (!(mParent instanceof ViewGroup)) {
          return;
        }
        if ((paramInt != 2) && (paramInt != 1))
        {
          if (((paramView instanceof ViewGroup)) && (((ViewGroup)paramView).getDescendantFocusability() == 262144) && (ViewRootImpl.isViewDescendantOf(this, paramView))) {
            ((ViewGroup)mParent).clearFocusedInCluster(paramView);
          }
        }
        else {
          ((ViewGroup)mParent).clearFocusedInCluster(paramView);
        }
      }
    }
  }
  
  private void updatePflags3AndNotifyA11yIfChanged(int paramInt, boolean paramBoolean)
  {
    int i = mPrivateFlags3;
    if (paramBoolean) {
      paramInt = i | paramInt;
    } else {
      paramInt = i & paramInt;
    }
    if (paramInt != mPrivateFlags3)
    {
      mPrivateFlags3 = paramInt;
      notifyViewAccessibilityStateChangedIfNeeded(0);
    }
  }
  
  public void addChildrenForAccessibility(ArrayList<View> paramArrayList) {}
  
  public void addExtraDataToAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString, Bundle paramBundle) {}
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt)
  {
    addFocusables(paramArrayList, paramInt, isInTouchMode());
  }
  
  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    if (paramArrayList == null) {
      return;
    }
    if (!canTakeFocus()) {
      return;
    }
    if (((paramInt2 & 0x1) == 1) && (!isFocusableInTouchMode())) {
      return;
    }
    paramArrayList.add(this);
  }
  
  public void addFrameMetricsListener(Window paramWindow, Window.OnFrameMetricsAvailableListener paramOnFrameMetricsAvailableListener, Handler paramHandler)
  {
    if (mAttachInfo != null)
    {
      if (mAttachInfo.mThreadedRenderer != null)
      {
        if (mFrameMetricsObservers == null) {
          mFrameMetricsObservers = new ArrayList();
        }
        paramWindow = new FrameMetricsObserver(paramWindow, paramHandler.getLooper(), paramOnFrameMetricsAvailableListener);
        mFrameMetricsObservers.add(paramWindow);
        mAttachInfo.mThreadedRenderer.addFrameMetricsObserver(paramWindow);
      }
      else
      {
        Log.w("View", "View not hardware-accelerated. Unable to observe frame stats");
      }
    }
    else
    {
      if (mFrameMetricsObservers == null) {
        mFrameMetricsObservers = new ArrayList();
      }
      paramWindow = new FrameMetricsObserver(paramWindow, paramHandler.getLooper(), paramOnFrameMetricsAvailableListener);
      mFrameMetricsObservers.add(paramWindow);
    }
  }
  
  public void addKeyboardNavigationClusters(Collection<View> paramCollection, int paramInt)
  {
    if (!isKeyboardNavigationCluster()) {
      return;
    }
    if (!hasFocusable()) {
      return;
    }
    paramCollection.add(this);
  }
  
  public void addOnAttachStateChangeListener(OnAttachStateChangeListener paramOnAttachStateChangeListener)
  {
    ListenerInfo localListenerInfo = getListenerInfo();
    if (mOnAttachStateChangeListeners == null) {
      ListenerInfo.access$302(localListenerInfo, new CopyOnWriteArrayList());
    }
    mOnAttachStateChangeListeners.add(paramOnAttachStateChangeListener);
  }
  
  public void addOnLayoutChangeListener(OnLayoutChangeListener paramOnLayoutChangeListener)
  {
    ListenerInfo localListenerInfo = getListenerInfo();
    if (mOnLayoutChangeListeners == null) {
      ListenerInfo.access$202(localListenerInfo, new ArrayList());
    }
    if (!mOnLayoutChangeListeners.contains(paramOnLayoutChangeListener)) {
      mOnLayoutChangeListeners.add(paramOnLayoutChangeListener);
    }
  }
  
  public void addOnUnhandledKeyEventListener(OnUnhandledKeyEventListener paramOnUnhandledKeyEventListener)
  {
    ArrayList localArrayList1 = getListenerInfomUnhandledKeyListeners;
    ArrayList localArrayList2 = localArrayList1;
    if (localArrayList1 == null)
    {
      localArrayList2 = new ArrayList();
      ListenerInfo.access$3702(getListenerInfo(), localArrayList2);
    }
    localArrayList2.add(paramOnUnhandledKeyEventListener);
    if ((localArrayList2.size() == 1) && ((mParent instanceof ViewGroup))) {
      ((ViewGroup)mParent).incrementChildUnhandledKeyListeners();
    }
  }
  
  public void addTouchables(ArrayList<View> paramArrayList)
  {
    int i = mViewFlags;
    if ((((i & 0x4000) == 16384) || ((i & 0x200000) == 2097152) || ((i & 0x800000) == 8388608)) && ((i & 0x20) == 0)) {
      paramArrayList.add(this);
    }
  }
  
  public ViewPropertyAnimator animate()
  {
    if (mAnimator == null) {
      mAnimator = new ViewPropertyAnimator(this);
    }
    return mAnimator;
  }
  
  public void announceForAccessibility(CharSequence paramCharSequence)
  {
    if ((AccessibilityManager.getInstance(mContext).isEnabled()) && (mParent != null))
    {
      AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(16384);
      onInitializeAccessibilityEvent(localAccessibilityEvent);
      localAccessibilityEvent.getText().add(paramCharSequence);
      localAccessibilityEvent.setContentDescription(null);
      mParent.requestSendAccessibilityEvent(this, localAccessibilityEvent);
    }
  }
  
  public void applyDrawableToTransparentRegion(Drawable paramDrawable, Region paramRegion)
  {
    Region localRegion = paramDrawable.getTransparentRegion();
    Rect localRect = paramDrawable.getBounds();
    paramDrawable = mAttachInfo;
    if ((localRegion != null) && (paramDrawable != null))
    {
      int i = getRight() - getLeft();
      int j = getBottom() - getTop();
      if (left > 0) {
        localRegion.op(0, 0, left, j, Region.Op.UNION);
      }
      if (right < i) {
        localRegion.op(right, 0, i, j, Region.Op.UNION);
      }
      if (top > 0) {
        localRegion.op(0, 0, i, top, Region.Op.UNION);
      }
      if (bottom < j) {
        localRegion.op(0, bottom, i, j, Region.Op.UNION);
      }
      paramDrawable = mTransparentLocation;
      getLocationInWindow(paramDrawable);
      localRegion.translate(paramDrawable[0], paramDrawable[1]);
      paramRegion.op(localRegion, Region.Op.INTERSECT);
    }
    else
    {
      paramRegion.op(localRect, Region.Op.DIFFERENCE);
    }
  }
  
  boolean areDrawablesResolved()
  {
    boolean bool;
    if ((mPrivateFlags2 & 0x40000000) == 1073741824) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void assignParent(ViewParent paramViewParent)
  {
    if (mParent == null)
    {
      mParent = paramViewParent;
    }
    else
    {
      if (paramViewParent != null) {
        break label25;
      }
      mParent = null;
    }
    return;
    label25:
    paramViewParent = new StringBuilder();
    paramViewParent.append("view ");
    paramViewParent.append(this);
    paramViewParent.append(" being added, but it already has a parent");
    throw new RuntimeException(paramViewParent.toString());
  }
  
  public void autofill(SparseArray<AutofillValue> paramSparseArray)
  {
    if (!mContext.isAutofillCompatibilityEnabled()) {
      return;
    }
    AccessibilityNodeProvider localAccessibilityNodeProvider = getAccessibilityNodeProvider();
    if (localAccessibilityNodeProvider == null) {
      return;
    }
    int i = paramSparseArray.size();
    for (int j = 0; j < i; j++)
    {
      Object localObject = (AutofillValue)paramSparseArray.valueAt(j);
      if (((AutofillValue)localObject).isText())
      {
        int k = paramSparseArray.keyAt(j);
        CharSequence localCharSequence = ((AutofillValue)localObject).getTextValue();
        localObject = new Bundle();
        ((Bundle)localObject).putCharSequence("ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE", localCharSequence);
        localAccessibilityNodeProvider.performAction(k, 2097152, (Bundle)localObject);
      }
    }
  }
  
  public void autofill(AutofillValue paramAutofillValue) {}
  
  protected boolean awakenScrollBars()
  {
    ScrollabilityCache localScrollabilityCache = mScrollCache;
    boolean bool = true;
    if ((localScrollabilityCache == null) || (!awakenScrollBars(mScrollCache.scrollBarDefaultDelayBeforeFade, true))) {
      bool = false;
    }
    return bool;
  }
  
  protected boolean awakenScrollBars(int paramInt)
  {
    return awakenScrollBars(paramInt, true);
  }
  
  protected boolean awakenScrollBars(int paramInt, boolean paramBoolean)
  {
    ScrollabilityCache localScrollabilityCache = mScrollCache;
    if ((localScrollabilityCache != null) && (fadeScrollBars))
    {
      if (scrollBar == null)
      {
        scrollBar = new ScrollBarDrawable();
        scrollBar.setState(getDrawableState());
        scrollBar.setCallback(this);
      }
      if ((!isHorizontalScrollBarEnabled()) && (!isVerticalScrollBarEnabled())) {
        return false;
      }
      if (paramBoolean) {
        postInvalidateOnAnimation();
      }
      int i = paramInt;
      if (state == 0) {
        i = Math.max(750, paramInt);
      }
      long l = AnimationUtils.currentAnimationTimeMillis() + i;
      fadeStartTime = l;
      state = 1;
      if (mAttachInfo != null)
      {
        mAttachInfo.mHandler.removeCallbacks(localScrollabilityCache);
        mAttachInfo.mHandler.postAtTime(localScrollabilityCache, l);
      }
      return true;
    }
    return false;
  }
  
  public void bringToFront()
  {
    if (mParent != null) {
      mParent.bringChildToFront(this);
    }
  }
  
  @Deprecated
  public void buildDrawingCache()
  {
    buildDrawingCache(false);
  }
  
  @Deprecated
  public void buildDrawingCache(boolean paramBoolean)
  {
    if (((mPrivateFlags & 0x8000) == 0) || (paramBoolean ? mDrawingCache == null : mUnscaledDrawingCache == null)) {
      if (Trace.isTagEnabled(8L))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("buildDrawingCache/SW Layer for ");
        localStringBuilder.append(getClass().getSimpleName());
        Trace.traceBegin(8L, localStringBuilder.toString());
      }
    }
    try
    {
      buildDrawingCacheImpl(paramBoolean);
      return;
    }
    finally
    {
      Trace.traceEnd(8L);
    }
  }
  
  public void buildLayer()
  {
    if (mLayerType == 0) {
      return;
    }
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null)
    {
      if ((getWidth() != 0) && (getHeight() != 0))
      {
        switch (mLayerType)
        {
        default: 
          break;
        case 2: 
          updateDisplayListIfDirty();
          if ((mThreadedRenderer != null) && (mRenderNode.isValid())) {
            mThreadedRenderer.buildLayer(mRenderNode);
          }
          break;
        case 1: 
          buildDrawingCache(true);
        }
        return;
      }
      return;
    }
    throw new IllegalStateException("This view must be attached to a window first");
  }
  
  final boolean callDragEventHandler(DragEvent paramDragEvent)
  {
    ListenerInfo localListenerInfo = mListenerInfo;
    boolean bool;
    if ((localListenerInfo != null) && (mOnDragListener != null) && ((mViewFlags & 0x20) == 0) && (mOnDragListener.onDrag(this, paramDragEvent))) {
      bool = true;
    } else {
      bool = onDragEvent(paramDragEvent);
    }
    switch (mAction)
    {
    default: 
      break;
    case 6: 
      mPrivateFlags2 &= 0xFFFFFFFD;
      refreshDrawableState();
      break;
    case 5: 
      mPrivateFlags2 |= 0x2;
      refreshDrawableState();
      break;
    case 4: 
      mPrivateFlags2 &= 0xFFFFFFFC;
      refreshDrawableState();
    }
    return bool;
  }
  
  public boolean callOnClick()
  {
    ListenerInfo localListenerInfo = mListenerInfo;
    if ((localListenerInfo != null) && (mOnClickListener != null))
    {
      mOnClickListener.onClick(this);
      return true;
    }
    return false;
  }
  
  boolean canAcceptDrag()
  {
    int i = mPrivateFlags2;
    boolean bool = true;
    if ((i & 0x1) == 0) {
      bool = false;
    }
    return bool;
  }
  
  public boolean canHaveDisplayList()
  {
    boolean bool;
    if ((mAttachInfo != null) && (mAttachInfo.mThreadedRenderer != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canNotifyAutofillEnterExitEvent()
  {
    boolean bool;
    if ((isAutofillable()) && (isAttachedToWindow())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean canResolveLayoutDirection()
  {
    if (getRawLayoutDirection() != 2) {
      return true;
    }
    if (mParent != null) {
      try
      {
        boolean bool = mParent.canResolveLayoutDirection();
        return bool;
      }
      catch (AbstractMethodError localAbstractMethodError)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(mParent.getClass().getSimpleName());
        localStringBuilder.append(" does not fully implement ViewParent");
        Log.e("View", localStringBuilder.toString(), localAbstractMethodError);
      }
    }
    return false;
  }
  
  public boolean canResolveTextAlignment()
  {
    if (getRawTextAlignment() != 0) {
      return true;
    }
    if (mParent != null) {
      try
      {
        boolean bool = mParent.canResolveTextAlignment();
        return bool;
      }
      catch (AbstractMethodError localAbstractMethodError)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(mParent.getClass().getSimpleName());
        localStringBuilder.append(" does not fully implement ViewParent");
        Log.e("View", localStringBuilder.toString(), localAbstractMethodError);
      }
    }
    return false;
  }
  
  public boolean canResolveTextDirection()
  {
    if (getRawTextDirection() != 0) {
      return true;
    }
    if (mParent != null) {
      try
      {
        boolean bool = mParent.canResolveTextDirection();
        return bool;
      }
      catch (AbstractMethodError localAbstractMethodError)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(mParent.getClass().getSimpleName());
        localStringBuilder.append(" does not fully implement ViewParent");
        Log.e("View", localStringBuilder.toString(), localAbstractMethodError);
      }
    }
    return false;
  }
  
  public boolean canScrollHorizontally(int paramInt)
  {
    int i = computeHorizontalScrollOffset();
    int j = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
    boolean bool1 = false;
    boolean bool2 = false;
    if (j == 0) {
      return false;
    }
    if (paramInt < 0)
    {
      if (i > 0) {
        bool2 = true;
      }
      return bool2;
    }
    bool2 = bool1;
    if (i < j - 1) {
      bool2 = true;
    }
    return bool2;
  }
  
  public boolean canScrollVertically(int paramInt)
  {
    int i = computeVerticalScrollOffset();
    int j = computeVerticalScrollRange() - computeVerticalScrollExtent();
    boolean bool1 = false;
    boolean bool2 = false;
    if (j == 0) {
      return false;
    }
    if (paramInt < 0)
    {
      if (i > 0) {
        bool2 = true;
      }
      return bool2;
    }
    bool2 = bool1;
    if (i < j - 1) {
      bool2 = true;
    }
    return bool2;
  }
  
  public final void cancelDragAndDrop()
  {
    if (mAttachInfo == null)
    {
      Log.w("View", "cancelDragAndDrop called on a detached view.");
      return;
    }
    if (mAttachInfo.mDragToken != null)
    {
      try
      {
        mAttachInfo.mSession.cancelDragAndDrop(mAttachInfo.mDragToken);
      }
      catch (Exception localException)
      {
        Log.e("View", "Unable to cancel drag", localException);
      }
      mAttachInfo.mDragToken = null;
    }
    else
    {
      Log.e("View", "No active drag to cancel");
    }
  }
  
  public void cancelLongPress()
  {
    removeLongPressCallback();
    removeTapCallback();
  }
  
  public final void cancelPendingInputEvents()
  {
    dispatchCancelPendingInputEvents();
  }
  
  public void captureTransitioningViews(List<View> paramList)
  {
    if (getVisibility() == 0) {
      paramList.add(this);
    }
  }
  
  public boolean checkInputConnectionProxy(View paramView)
  {
    return false;
  }
  
  public void clearAccessibilityFocus()
  {
    clearAccessibilityFocusNoCallbacks(0);
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    if (localViewRootImpl != null)
    {
      View localView = localViewRootImpl.getAccessibilityFocusedHost();
      if ((localView != null) && (ViewRootImpl.isViewDescendantOf(localView, this))) {
        localViewRootImpl.setAccessibilityFocus(null, null);
      }
    }
  }
  
  void clearAccessibilityFocusNoCallbacks(int paramInt)
  {
    if ((mPrivateFlags2 & 0x4000000) != 0)
    {
      mPrivateFlags2 &= 0xFBFFFFFF;
      invalidate();
      if (AccessibilityManager.getInstance(mContext).isEnabled())
      {
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(65536);
        localAccessibilityEvent.setAction(paramInt);
        if (mAccessibilityDelegate != null) {
          mAccessibilityDelegate.sendAccessibilityEventUnchecked(this, localAccessibilityEvent);
        } else {
          sendAccessibilityEventUnchecked(localAccessibilityEvent);
        }
      }
    }
  }
  
  public void clearAnimation()
  {
    if (mCurrentAnimation != null) {
      mCurrentAnimation.detach();
    }
    mCurrentAnimation = null;
    invalidateParentIfNeeded();
  }
  
  public void clearFocus()
  {
    boolean bool;
    if ((!sAlwaysAssignFocus) && (isInTouchMode())) {
      bool = false;
    } else {
      bool = true;
    }
    clearFocusInternal(null, true, bool);
  }
  
  void clearFocusInternal(View paramView, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((mPrivateFlags & 0x2) != 0)
    {
      mPrivateFlags &= 0xFFFFFFFD;
      clearParentsWantFocus();
      if ((paramBoolean1) && (mParent != null)) {
        mParent.clearChildFocus(this);
      }
      onFocusChanged(false, 0, null);
      refreshDrawableState();
      if ((paramBoolean1) && ((!paramBoolean2) || (!rootViewRequestFocus()))) {
        notifyGlobalFocusCleared(this);
      }
    }
  }
  
  void clearParentsWantFocus()
  {
    if ((mParent instanceof View))
    {
      View localView = (View)mParent;
      mPrivateFlags &= 0xFFFFFFFE;
      ((View)mParent).clearParentsWantFocus();
    }
  }
  
  int combineVisibility(int paramInt1, int paramInt2)
  {
    return Math.max(paramInt1, paramInt2);
  }
  
  @Deprecated
  protected boolean computeFitSystemWindows(Rect paramRect1, Rect paramRect2)
  {
    paramRect2 = computeSystemWindowInsets(new WindowInsets(paramRect1), paramRect2);
    paramRect1.set(paramRect2.getSystemWindowInsets());
    return paramRect2.isSystemWindowInsetsConsumed();
  }
  
  protected int computeHorizontalScrollExtent()
  {
    return getWidth();
  }
  
  protected int computeHorizontalScrollOffset()
  {
    return mScrollX;
  }
  
  protected int computeHorizontalScrollRange()
  {
    return getWidth();
  }
  
  protected void computeOpaqueFlags()
  {
    if ((mBackground != null) && (mBackground.getOpacity() == -1)) {
      mPrivateFlags |= 0x800000;
    } else {
      mPrivateFlags &= 0xFF7FFFFF;
    }
    int i = mViewFlags;
    if ((((i & 0x200) != 0) || ((i & 0x100) != 0)) && ((i & 0x3000000) != 0) && ((0x3000000 & i) != 33554432)) {
      mPrivateFlags &= 0xFEFFFFFF;
    } else {
      mPrivateFlags |= 0x1000000;
    }
  }
  
  Insets computeOpticalInsets()
  {
    Insets localInsets;
    if (mBackground == null) {
      localInsets = Insets.NONE;
    } else {
      localInsets = mBackground.getOpticalInsets();
    }
    return localInsets;
  }
  
  public void computeScroll() {}
  
  public WindowInsets computeSystemWindowInsets(WindowInsets paramWindowInsets, Rect paramRect)
  {
    if (((mViewFlags & 0x800) != 0) && (mAttachInfo != null) && (((mAttachInfo.mSystemUiVisibility & 0x600) != 0) || (mAttachInfo.mOverscanRequested)))
    {
      paramRect.set(mAttachInfo.mOverscanInsets);
      return paramWindowInsets.inset(paramRect);
    }
    paramRect.set(paramWindowInsets.getSystemWindowInsets());
    return paramWindowInsets.consumeSystemWindowInsets().inset(paramRect);
  }
  
  protected int computeVerticalScrollExtent()
  {
    return getHeight();
  }
  
  protected int computeVerticalScrollOffset()
  {
    return mScrollY;
  }
  
  protected int computeVerticalScrollRange()
  {
    return getHeight();
  }
  
  public AccessibilityNodeInfo createAccessibilityNodeInfo()
  {
    if (mAccessibilityDelegate != null) {
      return mAccessibilityDelegate.createAccessibilityNodeInfo(this);
    }
    return createAccessibilityNodeInfoInternal();
  }
  
  public AccessibilityNodeInfo createAccessibilityNodeInfoInternal()
  {
    Object localObject = getAccessibilityNodeProvider();
    if (localObject != null) {
      return ((AccessibilityNodeProvider)localObject).createAccessibilityNodeInfo(-1);
    }
    localObject = AccessibilityNodeInfo.obtain(this);
    onInitializeAccessibilityNodeInfo((AccessibilityNodeInfo)localObject);
    return localObject;
  }
  
  public void createContextMenu(ContextMenu paramContextMenu)
  {
    ContextMenu.ContextMenuInfo localContextMenuInfo = getContextMenuInfo();
    ((MenuBuilder)paramContextMenu).setCurrentMenuInfo(localContextMenuInfo);
    onCreateContextMenu(paramContextMenu);
    ListenerInfo localListenerInfo = mListenerInfo;
    if ((localListenerInfo != null) && (mOnCreateContextMenuListener != null)) {
      mOnCreateContextMenuListener.onCreateContextMenu(paramContextMenu, this, localContextMenuInfo);
    }
    ((MenuBuilder)paramContextMenu).setCurrentMenuInfo(null);
    if (mParent != null) {
      mParent.createContextMenu(paramContextMenu);
    }
  }
  
  public Bitmap createSnapshot(ViewDebug.CanvasProvider paramCanvasProvider, boolean paramBoolean)
  {
    int i = mRight;
    int j = mLeft;
    int k = mBottom;
    int m = mTop;
    AttachInfo localAttachInfo = mAttachInfo;
    float f;
    if (localAttachInfo != null) {
      f = mApplicationScale;
    } else {
      f = 1.0F;
    }
    i = (int)((i - j) * f + 0.5F);
    k = (int)((k - m) * f + 0.5F);
    Object localObject1 = null;
    Canvas localCanvas1 = null;
    m = 1;
    if (i <= 0) {
      i = 1;
    }
    if (k > 0) {
      m = k;
    }
    Object localObject2 = localObject1;
    try
    {
      Canvas localCanvas2 = paramCanvasProvider.getCanvas(this, i, m);
      if (localAttachInfo != null)
      {
        localObject2 = localObject1;
        localCanvas1 = mCanvas;
        localObject2 = localCanvas1;
        mCanvas = null;
      }
      localObject2 = localCanvas1;
      computeScroll();
      localObject2 = localCanvas1;
      m = localCanvas2.save();
      localObject2 = localCanvas1;
      localCanvas2.scale(f, f);
      localObject2 = localCanvas1;
      localCanvas2.translate(-mScrollX, -mScrollY);
      localObject2 = localCanvas1;
      i = mPrivateFlags;
      localObject2 = localCanvas1;
      mPrivateFlags &= 0xFF9FFFFF;
      localObject2 = localCanvas1;
      if ((mPrivateFlags & 0x80) == 128)
      {
        localObject2 = localCanvas1;
        dispatchDraw(localCanvas2);
        localObject2 = localCanvas1;
        drawAutofilledHighlight(localCanvas2);
        localObject2 = localCanvas1;
        if (mOverlay != null)
        {
          localObject2 = localCanvas1;
          if (!mOverlay.isEmpty())
          {
            localObject2 = localCanvas1;
            mOverlay.getOverlayView().draw(localCanvas2);
          }
        }
      }
      else
      {
        localObject2 = localCanvas1;
        draw(localCanvas2);
      }
      localObject2 = localCanvas1;
      mPrivateFlags = i;
      localObject2 = localCanvas1;
      localCanvas2.restoreToCount(m);
      localObject2 = localCanvas1;
      paramCanvasProvider = paramCanvasProvider.createBitmap();
      if (localCanvas1 != null) {
        mCanvas = localCanvas1;
      }
      return paramCanvasProvider;
    }
    finally
    {
      if (localObject2 != null) {
        mCanvas = ((Canvas)localObject2);
      }
    }
  }
  
  protected void damageInParent()
  {
    if ((mParent != null) && (mAttachInfo != null)) {
      mParent.onDescendantInvalidated(this, this);
    }
  }
  
  public void debug()
  {
    debug(0);
  }
  
  protected void debug(int paramInt)
  {
    Object localObject1 = debugIndent(paramInt - 1);
    Object localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("+ ");
    ((StringBuilder)localObject2).append(this);
    localObject1 = ((StringBuilder)localObject2).toString();
    int i = getId();
    localObject2 = localObject1;
    if (i != -1)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append(" (id=");
      ((StringBuilder)localObject2).append(i);
      ((StringBuilder)localObject2).append(")");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    Object localObject3 = getTag();
    localObject1 = localObject2;
    if (localObject3 != null)
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append((String)localObject2);
      ((StringBuilder)localObject1).append(" (tag=");
      ((StringBuilder)localObject1).append(localObject3);
      ((StringBuilder)localObject1).append(")");
      localObject1 = ((StringBuilder)localObject1).toString();
    }
    Log.d("View", (String)localObject1);
    if ((mPrivateFlags & 0x2) != 0)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append(debugIndent(paramInt));
      ((StringBuilder)localObject2).append(" FOCUSED");
      Log.d("View", ((StringBuilder)localObject2).toString());
    }
    localObject2 = debugIndent(paramInt);
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append("frame={");
    ((StringBuilder)localObject1).append(mLeft);
    ((StringBuilder)localObject1).append(", ");
    ((StringBuilder)localObject1).append(mTop);
    ((StringBuilder)localObject1).append(", ");
    ((StringBuilder)localObject1).append(mRight);
    ((StringBuilder)localObject1).append(", ");
    ((StringBuilder)localObject1).append(mBottom);
    ((StringBuilder)localObject1).append("} scroll={");
    ((StringBuilder)localObject1).append(mScrollX);
    ((StringBuilder)localObject1).append(", ");
    ((StringBuilder)localObject1).append(mScrollY);
    ((StringBuilder)localObject1).append("} ");
    Log.d("View", ((StringBuilder)localObject1).toString());
    if ((mPaddingLeft != 0) || (mPaddingTop != 0) || (mPaddingRight != 0) || (mPaddingBottom != 0))
    {
      localObject1 = debugIndent(paramInt);
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("padding={");
      ((StringBuilder)localObject2).append(mPaddingLeft);
      ((StringBuilder)localObject2).append(", ");
      ((StringBuilder)localObject2).append(mPaddingTop);
      ((StringBuilder)localObject2).append(", ");
      ((StringBuilder)localObject2).append(mPaddingRight);
      ((StringBuilder)localObject2).append(", ");
      ((StringBuilder)localObject2).append(mPaddingBottom);
      ((StringBuilder)localObject2).append("}");
      Log.d("View", ((StringBuilder)localObject2).toString());
    }
    localObject1 = debugIndent(paramInt);
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("mMeasureWidth=");
    ((StringBuilder)localObject2).append(mMeasuredWidth);
    ((StringBuilder)localObject2).append(" mMeasureHeight=");
    ((StringBuilder)localObject2).append(mMeasuredHeight);
    Log.d("View", ((StringBuilder)localObject2).toString());
    localObject1 = debugIndent(paramInt);
    if (mLayoutParams == null)
    {
      localObject2 = new StringBuilder();
      ((StringBuilder)localObject2).append((String)localObject1);
      ((StringBuilder)localObject2).append("BAD! no layout params");
      localObject2 = ((StringBuilder)localObject2).toString();
    }
    else
    {
      localObject2 = mLayoutParams.debug((String)localObject1);
    }
    Log.d("View", (String)localObject2);
    localObject1 = debugIndent(paramInt);
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("flags={");
    localObject1 = ((StringBuilder)localObject2).toString();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(printFlags(mViewFlags));
    localObject1 = ((StringBuilder)localObject2).toString();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("}");
    Log.d("View", ((StringBuilder)localObject2).toString());
    localObject1 = debugIndent(paramInt);
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append("privateFlags={");
    localObject1 = ((StringBuilder)localObject2).toString();
    localObject2 = new StringBuilder();
    ((StringBuilder)localObject2).append((String)localObject1);
    ((StringBuilder)localObject2).append(printPrivateFlags(mPrivateFlags));
    localObject2 = ((StringBuilder)localObject2).toString();
    localObject1 = new StringBuilder();
    ((StringBuilder)localObject1).append((String)localObject2);
    ((StringBuilder)localObject1).append("}");
    Log.d("View", ((StringBuilder)localObject1).toString());
  }
  
  final boolean debugDraw()
  {
    boolean bool;
    if ((!DEBUG_DRAW) && ((mAttachInfo == null) || (!mAttachInfo.mDebugLayout))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  @Deprecated
  public void destroyDrawingCache()
  {
    if (mDrawingCache != null)
    {
      mDrawingCache.recycle();
      mDrawingCache = null;
    }
    if (mUnscaledDrawingCache != null)
    {
      mUnscaledDrawingCache.recycle();
      mUnscaledDrawingCache = null;
    }
  }
  
  protected void destroyHardwareResources()
  {
    if (mOverlay != null) {
      mOverlay.getOverlayView().destroyHardwareResources();
    }
    if (mGhostView != null) {
      mGhostView.destroyHardwareResources();
    }
  }
  
  final int dipsToPixels(int paramInt)
  {
    float f = getContextgetResourcesgetDisplayMetricsdensity;
    return (int)(paramInt * f + 0.5F);
  }
  
  public boolean dispatchActivityResult(String paramString, int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((mStartActivityRequestWho != null) && (mStartActivityRequestWho.equals(paramString)))
    {
      onActivityResult(paramInt1, paramInt2, paramIntent);
      mStartActivityRequestWho = null;
      return true;
    }
    return false;
  }
  
  /* Error */
  public WindowInsets dispatchApplyWindowInsets(WindowInsets paramWindowInsets)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_0
    //   2: getfield 1439	android/view/View:mPrivateFlags3	I
    //   5: bipush 32
    //   7: ior
    //   8: putfield 1439	android/view/View:mPrivateFlags3	I
    //   11: aload_0
    //   12: getfield 2046	android/view/View:mListenerInfo	Landroid/view/View$ListenerInfo;
    //   15: ifnull +41 -> 56
    //   18: aload_0
    //   19: getfield 2046	android/view/View:mListenerInfo	Landroid/view/View$ListenerInfo;
    //   22: getfield 4050	android/view/View$ListenerInfo:mOnApplyWindowInsetsListener	Landroid/view/View$OnApplyWindowInsetsListener;
    //   25: ifnull +31 -> 56
    //   28: aload_0
    //   29: getfield 2046	android/view/View:mListenerInfo	Landroid/view/View$ListenerInfo;
    //   32: getfield 4050	android/view/View$ListenerInfo:mOnApplyWindowInsetsListener	Landroid/view/View$OnApplyWindowInsetsListener;
    //   35: aload_0
    //   36: aload_1
    //   37: invokeinterface 4054 3 0
    //   42: astore_1
    //   43: aload_0
    //   44: aload_0
    //   45: getfield 1439	android/view/View:mPrivateFlags3	I
    //   48: bipush -33
    //   50: iand
    //   51: putfield 1439	android/view/View:mPrivateFlags3	I
    //   54: aload_1
    //   55: areturn
    //   56: aload_0
    //   57: aload_1
    //   58: invokevirtual 4056	android/view/View:onApplyWindowInsets	(Landroid/view/WindowInsets;)Landroid/view/WindowInsets;
    //   61: astore_1
    //   62: aload_0
    //   63: aload_0
    //   64: getfield 1439	android/view/View:mPrivateFlags3	I
    //   67: bipush -33
    //   69: iand
    //   70: putfield 1439	android/view/View:mPrivateFlags3	I
    //   73: aload_1
    //   74: areturn
    //   75: astore_1
    //   76: aload_0
    //   77: aload_0
    //   78: getfield 1439	android/view/View:mPrivateFlags3	I
    //   81: bipush -33
    //   83: iand
    //   84: putfield 1439	android/view/View:mPrivateFlags3	I
    //   87: aload_1
    //   88: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	89	0	this	View
    //   0	89	1	paramWindowInsets	WindowInsets
    // Exception table:
    //   from	to	target	type
    //   0	43	75	finally
    //   56	62	75	finally
  }
  
  void dispatchAttachedToWindow(AttachInfo paramAttachInfo, int paramInt)
  {
    mAttachInfo = paramAttachInfo;
    if (mOverlay != null) {
      mOverlay.getOverlayView().dispatchAttachedToWindow(paramAttachInfo, paramInt);
    }
    mWindowAttachCount += 1;
    mPrivateFlags |= 0x400;
    Object localObject1 = mFloatingTreeObserver;
    Object localObject2 = null;
    if (localObject1 != null)
    {
      mTreeObserver.merge(mFloatingTreeObserver);
      mFloatingTreeObserver = null;
    }
    registerPendingFrameMetricsObservers();
    if ((mPrivateFlags & 0x80000) != 0)
    {
      mAttachInfo.mScrollContainers.add(this);
      mPrivateFlags |= 0x100000;
    }
    if (mRunQueue != null)
    {
      mRunQueue.executeActions(mHandler);
      mRunQueue = null;
    }
    performCollectViewAttributes(mAttachInfo, paramInt);
    onAttachedToWindow();
    localObject1 = mListenerInfo;
    if (localObject1 != null) {
      localObject2 = mOnAttachStateChangeListeners;
    }
    if ((localObject2 != null) && (((CopyOnWriteArrayList)localObject2).size() > 0))
    {
      localObject2 = ((CopyOnWriteArrayList)localObject2).iterator();
      while (((Iterator)localObject2).hasNext()) {
        ((OnAttachStateChangeListener)((Iterator)localObject2).next()).onViewAttachedToWindow(this);
      }
    }
    int i = mWindowVisibility;
    if (i != 8)
    {
      onWindowVisibilityChanged(i);
      if (isShown())
      {
        boolean bool;
        if (i == 0) {
          bool = true;
        } else {
          bool = false;
        }
        onVisibilityAggregated(bool);
      }
    }
    onVisibilityChanged(this, paramInt);
    if ((mPrivateFlags & 0x400) != 0) {
      refreshDrawableState();
    }
    needGlobalAttributesUpdate(false);
    notifyEnterOrExitForAutoFillIfNeeded(true);
  }
  
  void dispatchCancelPendingInputEvents()
  {
    mPrivateFlags3 &= 0xFFFFFFEF;
    onCancelPendingInputEvents();
    if ((mPrivateFlags3 & 0x10) == 16) {
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("View ");
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append(" did not call through to super.onCancelPendingInputEvents()");
    throw new SuperNotCalledException(localStringBuilder.toString());
  }
  
  public boolean dispatchCapturedPointerEvent(MotionEvent paramMotionEvent)
  {
    if (!hasPointerCapture()) {
      return false;
    }
    ListenerInfo localListenerInfo = mListenerInfo;
    if ((localListenerInfo != null) && (mOnCapturedPointerListener != null) && (mOnCapturedPointerListener.onCapturedPointer(this, paramMotionEvent))) {
      return true;
    }
    return onCapturedPointerEvent(paramMotionEvent);
  }
  
  void dispatchCollectViewAttributes(AttachInfo paramAttachInfo, int paramInt)
  {
    performCollectViewAttributes(paramAttachInfo, paramInt);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration)
  {
    onConfigurationChanged(paramConfiguration);
  }
  
  void dispatchDetachedFromWindow()
  {
    Object localObject = mAttachInfo;
    if ((localObject != null) && (mWindowVisibility != 8))
    {
      onWindowVisibilityChanged(8);
      if (isShown()) {
        onVisibilityAggregated(false);
      }
    }
    onDetachedFromWindow();
    onDetachedFromWindowInternal();
    localObject = InputMethodManager.peekInstance();
    if (localObject != null) {
      ((InputMethodManager)localObject).onViewDetachedFromWindow(this);
    }
    localObject = mListenerInfo;
    if (localObject != null) {
      localObject = mOnAttachStateChangeListeners;
    } else {
      localObject = null;
    }
    if ((localObject != null) && (((CopyOnWriteArrayList)localObject).size() > 0))
    {
      localObject = ((CopyOnWriteArrayList)localObject).iterator();
      while (((Iterator)localObject).hasNext()) {
        ((OnAttachStateChangeListener)((Iterator)localObject).next()).onViewDetachedFromWindow(this);
      }
    }
    if ((mPrivateFlags & 0x100000) != 0)
    {
      mAttachInfo.mScrollContainers.remove(this);
      mPrivateFlags &= 0xFFEFFFFF;
    }
    mAttachInfo = null;
    if (mOverlay != null) {
      mOverlay.getOverlayView().dispatchDetachedFromWindow();
    }
    notifyEnterOrExitForAutoFillIfNeeded(false);
  }
  
  public void dispatchDisplayHint(int paramInt)
  {
    onDisplayHint(paramInt);
  }
  
  boolean dispatchDragEnterExitInPreN(DragEvent paramDragEvent)
  {
    return callDragEventHandler(paramDragEvent);
  }
  
  public boolean dispatchDragEvent(DragEvent paramDragEvent)
  {
    mEventHandlerWasCalled = true;
    if ((mAction == 2) || (mAction == 3)) {
      getViewRootImpl().setDragFocus(this, paramDragEvent);
    }
    return callDragEventHandler(paramDragEvent);
  }
  
  protected void dispatchDraw(Canvas paramCanvas) {}
  
  public void dispatchDrawableHotspotChanged(float paramFloat1, float paramFloat2) {}
  
  public void dispatchFinishTemporaryDetach()
  {
    mPrivateFlags3 &= 0xFDFFFFFF;
    onFinishTemporaryDetach();
    if ((hasWindowFocus()) && (hasFocus())) {
      InputMethodManager.getInstance().focusIn(this);
    }
    notifyEnterOrExitForAutoFillIfNeeded(true);
  }
  
  protected boolean dispatchGenericFocusedEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onGenericMotionEvent(paramMotionEvent, 0);
    }
    if ((paramMotionEvent.getSource() & 0x2) != 0)
    {
      int i = paramMotionEvent.getAction();
      if ((i != 9) && (i != 7) && (i != 10))
      {
        if (dispatchGenericPointerEvent(paramMotionEvent)) {
          return true;
        }
      }
      else if (dispatchHoverEvent(paramMotionEvent)) {
        return true;
      }
    }
    else if (dispatchGenericFocusedEvent(paramMotionEvent))
    {
      return true;
    }
    if (dispatchGenericMotionEventInternal(paramMotionEvent)) {
      return true;
    }
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onUnhandledEvent(paramMotionEvent, 0);
    }
    return false;
  }
  
  protected boolean dispatchGenericPointerEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  protected void dispatchGetDisplayList() {}
  
  protected boolean dispatchHoverEvent(MotionEvent paramMotionEvent)
  {
    ListenerInfo localListenerInfo = mListenerInfo;
    if ((localListenerInfo != null) && (mOnHoverListener != null) && ((mViewFlags & 0x20) == 0) && (mOnHoverListener.onHover(this, paramMotionEvent))) {
      return true;
    }
    return onHoverEvent(paramMotionEvent);
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onKeyEvent(paramKeyEvent, 0);
    }
    Object localObject = mListenerInfo;
    if ((localObject != null) && (mOnKeyListener != null) && ((mViewFlags & 0x20) == 0) && (mOnKeyListener.onKey(this, paramKeyEvent.getKeyCode(), paramKeyEvent))) {
      return true;
    }
    if (mAttachInfo != null) {
      localObject = mAttachInfo.mKeyDispatchState;
    } else {
      localObject = null;
    }
    if (paramKeyEvent.dispatch(this, (KeyEvent.DispatcherState)localObject, this)) {
      return true;
    }
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onUnhandledEvent(paramKeyEvent, 0);
    }
    return false;
  }
  
  public boolean dispatchKeyEventPreIme(KeyEvent paramKeyEvent)
  {
    return onKeyPreIme(paramKeyEvent.getKeyCode(), paramKeyEvent);
  }
  
  public boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
  {
    return onKeyShortcut(paramKeyEvent.getKeyCode(), paramKeyEvent);
  }
  
  void dispatchMovedToDisplay(Display paramDisplay, Configuration paramConfiguration)
  {
    mAttachInfo.mDisplay = paramDisplay;
    mAttachInfo.mDisplayState = paramDisplay.getState();
    onMovedToDisplay(paramDisplay.getDisplayId(), paramConfiguration);
  }
  
  public boolean dispatchNestedFling(float paramFloat1, float paramFloat2, boolean paramBoolean)
  {
    if ((isNestedScrollingEnabled()) && (mNestedScrollingParent != null)) {
      return mNestedScrollingParent.onNestedFling(this, paramFloat1, paramFloat2, paramBoolean);
    }
    return false;
  }
  
  public boolean dispatchNestedPreFling(float paramFloat1, float paramFloat2)
  {
    if ((isNestedScrollingEnabled()) && (mNestedScrollingParent != null)) {
      return mNestedScrollingParent.onNestedPreFling(this, paramFloat1, paramFloat2);
    }
    return false;
  }
  
  public boolean dispatchNestedPrePerformAccessibilityAction(int paramInt, Bundle paramBundle)
  {
    for (ViewParent localViewParent = getParent(); localViewParent != null; localViewParent = localViewParent.getParent()) {
      if (localViewParent.onNestedPrePerformAccessibilityAction(this, paramInt, paramBundle)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean dispatchNestedPreScroll(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if ((isNestedScrollingEnabled()) && (mNestedScrollingParent != null))
    {
      boolean bool1 = true;
      if ((paramInt1 == 0) && (paramInt2 == 0))
      {
        if (paramArrayOfInt2 != null)
        {
          paramArrayOfInt2[0] = 0;
          paramArrayOfInt2[1] = 0;
        }
      }
      else
      {
        int i = 0;
        int j = 0;
        if (paramArrayOfInt2 != null)
        {
          getLocationInWindow(paramArrayOfInt2);
          i = paramArrayOfInt2[0];
          j = paramArrayOfInt2[1];
        }
        int[] arrayOfInt = paramArrayOfInt1;
        if (paramArrayOfInt1 == null)
        {
          if (mTempNestedScrollConsumed == null) {
            mTempNestedScrollConsumed = new int[2];
          }
          arrayOfInt = mTempNestedScrollConsumed;
        }
        arrayOfInt[0] = 0;
        arrayOfInt[1] = 0;
        mNestedScrollingParent.onNestedPreScroll(this, paramInt1, paramInt2, arrayOfInt);
        if (paramArrayOfInt2 != null)
        {
          getLocationInWindow(paramArrayOfInt2);
          paramArrayOfInt2[0] -= i;
          paramArrayOfInt2[1] -= j;
        }
        boolean bool2 = bool1;
        if (arrayOfInt[0] == 0) {
          if (arrayOfInt[1] != 0) {
            bool2 = bool1;
          } else {
            bool2 = false;
          }
        }
        return bool2;
      }
    }
    return false;
  }
  
  public boolean dispatchNestedScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    if ((isNestedScrollingEnabled()) && (mNestedScrollingParent != null)) {
      if ((paramInt1 == 0) && (paramInt2 == 0) && (paramInt3 == 0) && (paramInt4 == 0))
      {
        if (paramArrayOfInt != null)
        {
          paramArrayOfInt[0] = 0;
          paramArrayOfInt[1] = 0;
        }
      }
      else
      {
        int i = 0;
        int j = 0;
        if (paramArrayOfInt != null)
        {
          getLocationInWindow(paramArrayOfInt);
          i = paramArrayOfInt[0];
          j = paramArrayOfInt[1];
        }
        mNestedScrollingParent.onNestedScroll(this, paramInt1, paramInt2, paramInt3, paramInt4);
        if (paramArrayOfInt != null)
        {
          getLocationInWindow(paramArrayOfInt);
          paramArrayOfInt[0] -= i;
          paramArrayOfInt[1] -= j;
        }
        return true;
      }
    }
    return false;
  }
  
  public void dispatchPointerCaptureChanged(boolean paramBoolean)
  {
    onPointerCaptureChange(paramBoolean);
  }
  
  public final boolean dispatchPointerEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.isTouchEvent()) {
      return dispatchTouchEvent(paramMotionEvent);
    }
    return dispatchGenericMotionEvent(paramMotionEvent);
  }
  
  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (mAccessibilityDelegate != null) {
      return mAccessibilityDelegate.dispatchPopulateAccessibilityEvent(this, paramAccessibilityEvent);
    }
    return dispatchPopulateAccessibilityEventInternal(paramAccessibilityEvent);
  }
  
  public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    onPopulateAccessibilityEvent(paramAccessibilityEvent);
    return false;
  }
  
  public void dispatchProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt)
  {
    dispatchProvideStructureForAssistOrAutofill(paramViewStructure, true, paramInt);
  }
  
  public void dispatchProvideStructure(ViewStructure paramViewStructure)
  {
    dispatchProvideStructureForAssistOrAutofill(paramViewStructure, false, 0);
  }
  
  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    if (mID != -1)
    {
      paramSparseArray = (Parcelable)paramSparseArray.get(mID);
      if (paramSparseArray != null)
      {
        mPrivateFlags &= 0xFFFDFFFF;
        onRestoreInstanceState(paramSparseArray);
        if ((mPrivateFlags & 0x20000) == 0) {
          throw new IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
        }
      }
    }
  }
  
  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    if ((mID != -1) && ((mViewFlags & 0x10000) == 0))
    {
      mPrivateFlags &= 0xFFFDFFFF;
      Parcelable localParcelable = onSaveInstanceState();
      if ((mPrivateFlags & 0x20000) != 0)
      {
        if (localParcelable != null) {
          paramSparseArray.put(mID, localParcelable);
        }
      }
      else {
        throw new IllegalStateException("Derived class did not call super.onSaveInstanceState()");
      }
    }
  }
  
  void dispatchScreenStateChanged(int paramInt)
  {
    onScreenStateChanged(paramInt);
  }
  
  protected void dispatchSetActivated(boolean paramBoolean) {}
  
  protected void dispatchSetPressed(boolean paramBoolean) {}
  
  protected void dispatchSetSelected(boolean paramBoolean) {}
  
  public void dispatchStartTemporaryDetach()
  {
    mPrivateFlags3 |= 0x2000000;
    notifyEnterOrExitForAutoFillIfNeeded(false);
    onStartTemporaryDetach();
  }
  
  public void dispatchSystemUiVisibilityChanged(int paramInt)
  {
    ListenerInfo localListenerInfo = mListenerInfo;
    if ((localListenerInfo != null) && (mOnSystemUiVisibilityChangeListener != null)) {
      mOnSystemUiVisibilityChangeListener.onSystemUiVisibilityChange(paramInt & 0x3FF7);
    }
  }
  
  boolean dispatchTooltipHoverEvent(MotionEvent paramMotionEvent)
  {
    if (mTooltipInfo == null) {
      return false;
    }
    int i = paramMotionEvent.getAction();
    if (i != 7)
    {
      if (i == 10)
      {
        mTooltipInfo.clearAnchorPos();
        if (!mTooltipInfo.mTooltipFromLongClick) {
          hideTooltip();
        }
      }
    }
    else {
      if ((mViewFlags & 0x40000000) == 1073741824) {
        break label69;
      }
    }
    return false;
    label69:
    if ((!mTooltipInfo.mTooltipFromLongClick) && (mTooltipInfo.updateAnchorPos(paramMotionEvent)))
    {
      if (mTooltipInfo.mTooltipPopup == null)
      {
        removeCallbacks(mTooltipInfo.mShowTooltipRunnable);
        postDelayed(mTooltipInfo.mShowTooltipRunnable, ViewConfiguration.getHoverTooltipShowTimeout());
      }
      if ((getWindowSystemUiVisibility() & 0x1) == 1) {
        i = ViewConfiguration.getHoverTooltipHideShortTimeout();
      } else {
        i = ViewConfiguration.getHoverTooltipHideTimeout();
      }
      removeCallbacks(mTooltipInfo.mHideTooltipRunnable);
      postDelayed(mTooltipInfo.mHideTooltipRunnable, i);
    }
    return true;
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.isTargetAccessibilityFocus())
    {
      if (!isAccessibilityFocusedViewOrHost()) {
        return false;
      }
      paramMotionEvent.setTargetAccessibilityFocus(false);
    }
    boolean bool1 = false;
    boolean bool2 = false;
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onTouchEvent(paramMotionEvent, 0);
    }
    int i = paramMotionEvent.getActionMasked();
    if (i == 0)
    {
      SeempLog.record(3);
      stopNestedScroll();
    }
    if (onFilterTouchEventForSecurity(paramMotionEvent))
    {
      bool1 = bool2;
      if ((mViewFlags & 0x20) == 0)
      {
        bool1 = bool2;
        if (handleScrollBarDragging(paramMotionEvent)) {
          bool1 = true;
        }
      }
      ListenerInfo localListenerInfo = mListenerInfo;
      bool2 = bool1;
      if (localListenerInfo != null)
      {
        bool2 = bool1;
        if (mOnTouchListener != null)
        {
          bool2 = bool1;
          if ((mViewFlags & 0x20) == 0)
          {
            bool2 = bool1;
            if (mOnTouchListener.onTouch(this, paramMotionEvent)) {
              bool2 = true;
            }
          }
        }
      }
      bool1 = bool2;
      if (!bool2)
      {
        bool1 = bool2;
        if (onTouchEvent(paramMotionEvent)) {
          bool1 = true;
        }
      }
    }
    if ((!bool1) && (mInputEventConsistencyVerifier != null)) {
      mInputEventConsistencyVerifier.onUnhandledEvent(paramMotionEvent, 0);
    }
    if ((i == 1) || (i == 3) || ((i == 0) && (!bool1))) {
      stopNestedScroll();
    }
    return bool1;
  }
  
  public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
  {
    if (mInputEventConsistencyVerifier != null) {
      mInputEventConsistencyVerifier.onTrackballEvent(paramMotionEvent, 0);
    }
    return onTrackballEvent(paramMotionEvent);
  }
  
  View dispatchUnhandledKeyEvent(KeyEvent paramKeyEvent)
  {
    if (onUnhandledKeyEvent(paramKeyEvent)) {
      return this;
    }
    return null;
  }
  
  public boolean dispatchUnhandledMove(View paramView, int paramInt)
  {
    return false;
  }
  
  boolean dispatchVisibilityAggregated(boolean paramBoolean)
  {
    int i = getVisibility();
    boolean bool1 = false;
    if (i == 0) {
      i = 1;
    } else {
      i = 0;
    }
    if ((i != 0) || (!paramBoolean)) {
      onVisibilityAggregated(paramBoolean);
    }
    boolean bool2 = bool1;
    if (i != 0)
    {
      bool2 = bool1;
      if (paramBoolean) {
        bool2 = true;
      }
    }
    return bool2;
  }
  
  protected void dispatchVisibilityChanged(View paramView, int paramInt)
  {
    onVisibilityChanged(paramView, paramInt);
  }
  
  public void dispatchWindowFocusChanged(boolean paramBoolean)
  {
    onWindowFocusChanged(paramBoolean);
  }
  
  public void dispatchWindowSystemUiVisiblityChanged(int paramInt)
  {
    onWindowSystemUiVisibilityChanged(paramInt);
  }
  
  public void dispatchWindowVisibilityChanged(int paramInt)
  {
    onWindowVisibilityChanged(paramInt);
  }
  
  public void draw(Canvas paramCanvas)
  {
    int i = mPrivateFlags;
    if (((0x600000 & i) == 4194304) && ((mAttachInfo == null) || (!mAttachInfo.mIgnoreDirtyState))) {
      j = 1;
    } else {
      j = 0;
    }
    mPrivateFlags = (0xFF9FFFFF & i | 0x20);
    if (j == 0) {
      drawBackground(paramCanvas);
    }
    i = mViewFlags;
    int k;
    if ((i & 0x1000) != 0) {
      k = 1;
    } else {
      k = 0;
    }
    int m;
    if ((i & 0x2000) != 0) {
      m = 1;
    } else {
      m = 0;
    }
    if ((m == 0) && (k == 0))
    {
      if (j == 0) {
        onDraw(paramCanvas);
      }
      dispatchDraw(paramCanvas);
      drawAutofilledHighlight(paramCanvas);
      if ((mOverlay != null) && (!mOverlay.isEmpty())) {
        mOverlay.getOverlayView().dispatchDraw(paramCanvas);
      }
      onDrawForeground(paramCanvas);
      drawDefaultFocusHighlight(paramCanvas);
      if (debugDraw()) {
        debugDrawFocus(paramCanvas);
      }
      return;
    }
    float f1 = 0.0F;
    float f2 = 0.0F;
    float f3 = 0.0F;
    int n = mPaddingLeft;
    boolean bool = isPaddingOffsetRequired();
    i = n;
    if (bool) {
      i = n + getLeftPaddingOffset();
    }
    int i1 = mScrollX + i;
    n = mRight;
    int i2 = 0;
    int i3 = n + i1 - mLeft - mPaddingRight - i;
    int i4 = mScrollY + getFadeTop(bool);
    n = i4 + getFadeHeight(bool);
    int i5 = i3;
    i = n;
    if (bool)
    {
      i5 = i3 + getRightPaddingOffset();
      i = n + getBottomPaddingOffset();
    }
    Object localObject = mScrollCache;
    float f4 = fadingEdgeLength;
    i3 = (int)f4;
    if (m != 0)
    {
      n = i3;
      if (i4 + i3 > i - i3) {
        n = (i - i4) / 2;
      }
    }
    else
    {
      n = i3;
    }
    float f5 = 0.0F;
    int i6 = 0;
    i3 = n;
    if (k != 0)
    {
      i3 = n;
      if (i1 + n > i5 - n) {
        i3 = (i5 - i1) / 2;
      }
    }
    if (m != 0)
    {
      f5 = Math.max(0.0F, Math.min(1.0F, getTopFadingEdgeStrength()));
      if (f5 * f4 > 1.0F) {
        n = 1;
      } else {
        n = 0;
      }
      i2 = n;
      f1 = Math.max(0.0F, Math.min(1.0F, getBottomFadingEdgeStrength()));
      if (f1 * f4 > 1.0F) {
        n = 1;
      } else {
        n = 0;
      }
      m = n;
    }
    else
    {
      m = 0;
    }
    if (k != 0)
    {
      f2 = Math.max(0.0F, Math.min(1.0F, getLeftFadingEdgeStrength()));
      if (f2 * f4 > 1.0F) {
        n = 1;
      } else {
        n = 0;
      }
      k = n;
      f3 = Math.max(0.0F, Math.min(1.0F, getRightFadingEdgeStrength()));
      if (f3 * f4 > 1.0F) {
        n = 1;
      } else {
        n = 0;
      }
    }
    else
    {
      k = 0;
      n = i6;
    }
    i6 = paramCanvas.getSaveCount();
    int i7 = getSolidColor();
    if (i7 == 0)
    {
      if (i2 != 0) {
        paramCanvas.saveUnclippedLayer(i1, i4, i5, i4 + i3);
      }
      if (m != 0) {
        paramCanvas.saveUnclippedLayer(i1, i - i3, i5, i);
      }
      if (k != 0) {
        paramCanvas.saveUnclippedLayer(i1, i4, i1 + i3, i);
      }
      if (n != 0) {
        paramCanvas.saveUnclippedLayer(i5 - i3, i4, i5, i);
      }
    }
    else
    {
      ((ScrollabilityCache)localObject).setFadeColor(i7);
    }
    if (j == 0) {
      onDraw(paramCanvas);
    }
    dispatchDraw(paramCanvas);
    Paint localPaint = paint;
    Matrix localMatrix = matrix;
    Shader localShader = shader;
    if (i2 != 0)
    {
      localMatrix.setScale(1.0F, f4 * f5);
      localMatrix.postTranslate(i1, i4);
      localShader.setLocalMatrix(localMatrix);
      localPaint.setShader(localShader);
      paramCanvas.drawRect(i1, i4, i5, i4 + i3, localPaint);
    }
    int j = i4;
    if (m != 0)
    {
      localMatrix.setScale(1.0F, f4 * f1);
      localMatrix.postRotate(180.0F);
      localMatrix.postTranslate(i1, i);
      localShader.setLocalMatrix(localMatrix);
      localPaint.setShader(localShader);
      paramCanvas.drawRect(i1, i - i3, i5, i, localPaint);
    }
    f1 = 1.0F;
    localObject = paramCanvas;
    if (k != 0)
    {
      localMatrix.setScale(f1, f4 * f2);
      localMatrix.postRotate(-90.0F);
      localMatrix.postTranslate(i1, j);
      localShader.setLocalMatrix(localMatrix);
      localPaint.setShader(localShader);
      ((Canvas)localObject).drawRect(i1, j, i1 + i3, i, localPaint);
    }
    if (n != 0)
    {
      localMatrix.setScale(f1, f4 * f3);
      localMatrix.postRotate(90.0F);
      localMatrix.postTranslate(i5, j);
      localShader.setLocalMatrix(localMatrix);
      localPaint.setShader(localShader);
      ((Canvas)localObject).drawRect(i5 - i3, j, i5, i, localPaint);
    }
    ((Canvas)localObject).restoreToCount(i6);
    drawAutofilledHighlight(paramCanvas);
    if ((mOverlay != null) && (!mOverlay.isEmpty())) {
      mOverlay.getOverlayView().dispatchDraw((Canvas)localObject);
    }
    onDrawForeground(paramCanvas);
    if (debugDraw()) {
      debugDrawFocus(paramCanvas);
    }
  }
  
  boolean draw(Canvas paramCanvas, ViewGroup paramViewGroup, long paramLong)
  {
    boolean bool1 = paramCanvas.isHardwareAccelerated();
    if ((mAttachInfo != null) && (mAttachInfo.mHardwareAccelerated) && (bool1)) {
      i = 1;
    } else {
      i = 0;
    }
    int j = i;
    boolean bool2 = false;
    boolean bool3 = hasIdentityMatrix();
    int k = mGroupFlags;
    if ((k & 0x100) != 0)
    {
      paramViewGroup.getChildTransformation().clear();
      mGroupFlags &= 0xFEFF;
    }
    RenderNode localRenderNode = null;
    boolean bool4 = false;
    boolean bool5;
    if ((mAttachInfo != null) && (mAttachInfo.mScalingRequired)) {
      bool5 = true;
    } else {
      bool5 = false;
    }
    Animation localAnimation = getAnimation();
    boolean bool6;
    boolean bool7;
    if (localAnimation != null)
    {
      bool6 = applyLegacyAnimation(paramViewGroup, paramLong, localAnimation, bool5);
      bool7 = localAnimation.willChangeTransformationMatrix();
      if (bool7) {
        mPrivateFlags3 |= 0x1;
      }
      localObject1 = paramViewGroup.getChildTransformation();
    }
    for (;;)
    {
      break;
      i = mPrivateFlags3;
      localObject2 = null;
      if ((i & 0x1) != 0)
      {
        mRenderNode.setAnimationMatrix(null);
        mPrivateFlags3 &= 0xFFFFFFFE;
      }
      bool6 = bool2;
      localObject1 = localRenderNode;
      bool7 = bool4;
      if (j == 0)
      {
        bool6 = bool2;
        localObject1 = localRenderNode;
        bool7 = bool4;
        if ((k & 0x800) != 0)
        {
          localObject3 = paramViewGroup.getChildTransformation();
          bool6 = bool2;
          localObject1 = localRenderNode;
          bool7 = bool4;
          if (paramViewGroup.getChildStaticTransformation(this, (Transformation)localObject3))
          {
            i = ((Transformation)localObject3).getTransformationType();
            localObject1 = localObject2;
            if (i != 0) {
              localObject1 = localObject3;
            }
            if ((i & 0x2) != 0) {
              bool6 = true;
            } else {
              bool6 = false;
            }
            bool7 = bool6;
            bool6 = bool2;
          }
        }
      }
    }
    int m = bool7 | bool3 ^ true;
    mPrivateFlags |= 0x20;
    if ((m == 0) && ((k & 0x801) == 1) && (paramCanvas.quickReject(mLeft, mTop, mRight, mBottom, Canvas.EdgeType.BW)) && ((mPrivateFlags & 0x40) == 0))
    {
      mPrivateFlags2 |= 0x10000000;
      return bool6;
    }
    Object localObject2 = localObject1;
    mPrivateFlags2 &= 0xEFFFFFFF;
    if (bool1)
    {
      if ((mPrivateFlags & 0x80000000) != 0) {
        bool7 = true;
      } else {
        bool7 = false;
      }
      mRecreateDisplayList = bool7;
      mPrivateFlags &= 0x7FFFFFFF;
    }
    Object localObject3 = null;
    Object localObject1 = null;
    int n = getLayerType();
    int i1;
    if (n != 1)
    {
      i1 = n;
      if (j == 0) {}
    }
    for (;;)
    {
      break;
      i = n;
      if (n != 0)
      {
        i = 1;
        buildDrawingCache(true);
      }
      localObject1 = getDrawingCache(true);
      i1 = i;
    }
    int i2 = j;
    if (j != 0)
    {
      localRenderNode = updateDisplayListIfDirty();
      localObject3 = localRenderNode;
      i2 = j;
      if (!localRenderNode.isValid())
      {
        localObject3 = null;
        i2 = 0;
      }
    }
    n = 0;
    j = 0;
    if (i2 == 0)
    {
      computeScroll();
      n = mScrollX;
      j = mScrollY;
    }
    int i3;
    if ((localObject1 != null) && (i2 == 0)) {
      i3 = 1;
    } else {
      i3 = 0;
    }
    int i4;
    if ((localObject1 == null) && (i2 == 0)) {
      i4 = 1;
    } else {
      i4 = 0;
    }
    int i = -1;
    if (i2 != 0) {
      if (localObject2 == null) {
        break label656;
      }
    }
    i = paramCanvas.save();
    label656:
    float f1;
    if (i4 != 0)
    {
      paramCanvas.translate(mLeft - n, mTop - j);
    }
    else
    {
      if (i2 == 0) {
        paramCanvas.translate(mLeft, mTop);
      }
      if (bool5)
      {
        if (i2 != 0) {
          i = paramCanvas.save();
        }
        f1 = 1.0F / mAttachInfo.mApplicationScale;
        paramCanvas.scale(f1, f1);
      }
    }
    if (i2 != 0) {
      f1 = 1.0F;
    } else {
      f1 = getAlpha() * getTransitionAlpha();
    }
    if ((localObject2 == null) && (f1 >= 1.0F) && (hasIdentityMatrix()) && ((mPrivateFlags3 & 0x2) == 0))
    {
      if ((mPrivateFlags & 0x40000) == 262144)
      {
        onSetAlpha(255);
        mPrivateFlags &= 0xFFFBFFFF;
      }
    }
    else
    {
      if ((localObject2 == null) && (bool3)) {
        break label1034;
      }
      int i5 = 0;
      int i6 = 0;
      if (i4 != 0)
      {
        i5 = -n;
        i6 = -j;
      }
      float f3;
      if (localObject2 != null)
      {
        if (m != 0)
        {
          if (i2 != 0)
          {
            ((RenderNode)localObject3).setAnimationMatrix(((Transformation)localObject2).getMatrix());
          }
          else
          {
            paramCanvas.translate(-i5, -i6);
            paramCanvas.concat(((Transformation)localObject2).getMatrix());
            paramCanvas.translate(i5, i6);
          }
          mGroupFlags |= 0x100;
        }
        float f2 = ((Transformation)localObject2).getAlpha();
        f3 = f1;
        if (f2 < 1.0F)
        {
          f3 = f1 * f2;
          mGroupFlags |= 0x100;
        }
      }
      else
      {
        f3 = f1;
      }
      if ((!bool3) && (i2 == 0))
      {
        paramCanvas.translate(-i5, -i6);
        paramCanvas.concat(getMatrix());
        paramCanvas.translate(i5, i6);
      }
      f1 = f3;
      label1034:
      if ((f1 >= 1.0F) && ((mPrivateFlags3 & 0x2) == 0)) {
        break label1203;
      }
      if (f1 < 1.0F) {
        mPrivateFlags3 |= 0x2;
      } else {
        mPrivateFlags3 &= 0xFFFFFFFD;
      }
      mGroupFlags |= 0x100;
      if (i3 == 0)
      {
        i6 = (int)(255.0F * f1);
        if (!onSetAlpha(i6))
        {
          if (i2 != 0) {
            ((RenderNode)localObject3).setAlpha(getAlpha() * f1 * getTransitionAlpha());
          } else if (i1 == 0) {
            paramCanvas.saveLayerAlpha(n, j, getWidth() + n, j + getHeight(), i6);
          }
        }
        else {
          mPrivateFlags |= 0x40000;
        }
      }
    }
    label1203:
    if (i2 == 0)
    {
      if (((k & 0x1) != 0) && (localObject1 == null)) {
        if (i4 != 0) {
          paramCanvas.clipRect(n, j, n + getWidth(), j + getHeight());
        } else if ((bool5) && (localObject1 != null)) {
          paramCanvas.clipRect(0, 0, ((Bitmap)localObject1).getWidth(), ((Bitmap)localObject1).getHeight());
        } else {
          paramCanvas.clipRect(0, 0, getWidth(), getHeight());
        }
      }
      if (mClipBounds != null) {
        paramCanvas.clipRect(mClipBounds);
      }
    }
    if (i3 == 0)
    {
      if (i2 != 0)
      {
        mPrivateFlags = (0xFF9FFFFF & mPrivateFlags);
        ((DisplayListCanvas)paramCanvas).drawRenderNode((RenderNode)localObject3);
      }
      else if ((mPrivateFlags & 0x80) == 128)
      {
        mPrivateFlags = (0xFF9FFFFF & mPrivateFlags);
        dispatchDraw(paramCanvas);
      }
      else
      {
        draw(paramCanvas);
      }
    }
    else if (localObject1 != null)
    {
      mPrivateFlags = (0xFF9FFFFF & mPrivateFlags);
      if ((i1 != 0) && (mLayerPaint != null))
      {
        j = mLayerPaint.getAlpha();
        if (f1 < 1.0F) {
          mLayerPaint.setAlpha((int)(j * f1));
        }
        paramCanvas.drawBitmap((Bitmap)localObject1, 0.0F, 0.0F, mLayerPaint);
        if (f1 < 1.0F) {
          mLayerPaint.setAlpha(j);
        }
      }
      else
      {
        localObject2 = mCachePaint;
        localObject3 = localObject2;
        if (localObject2 == null)
        {
          localObject3 = new Paint();
          ((Paint)localObject3).setDither(false);
          mCachePaint = ((Paint)localObject3);
        }
        ((Paint)localObject3).setAlpha((int)(f1 * 255.0F));
        paramCanvas.drawBitmap((Bitmap)localObject1, 0.0F, 0.0F, (Paint)localObject3);
      }
    }
    if (i >= 0) {
      paramCanvas.restoreToCount(i);
    }
    if ((localAnimation != null) && (!bool6))
    {
      if ((!bool1) && (!localAnimation.getFillAfter())) {
        onSetAlpha(255);
      }
      paramViewGroup.finishAnimatingView(this, localAnimation);
    }
    if ((bool6) && (bool1) && (localAnimation.hasAlpha()) && ((mPrivateFlags & 0x40000) == 262144)) {
      invalidate(true);
    }
    mRecreateDisplayList = false;
    return bool6;
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2)
  {
    if (mBackground != null) {
      mBackground.setHotspot(paramFloat1, paramFloat2);
    }
    if (mDefaultFocusHighlight != null) {
      mDefaultFocusHighlight.setHotspot(paramFloat1, paramFloat2);
    }
    if ((mForegroundInfo != null) && (mForegroundInfo.mDrawable != null)) {
      mForegroundInfo.mDrawable.setHotspot(paramFloat1, paramFloat2);
    }
    dispatchDrawableHotspotChanged(paramFloat1, paramFloat2);
  }
  
  protected void drawableStateChanged()
  {
    int[] arrayOfInt = getDrawableState();
    boolean bool1 = false;
    Object localObject = mBackground;
    boolean bool2 = bool1;
    if (localObject != null)
    {
      bool2 = bool1;
      if (((Drawable)localObject).isStateful()) {
        bool2 = false | ((Drawable)localObject).setState(arrayOfInt);
      }
    }
    localObject = mDefaultFocusHighlight;
    bool1 = bool2;
    if (localObject != null)
    {
      bool1 = bool2;
      if (((Drawable)localObject).isStateful()) {
        bool1 = bool2 | ((Drawable)localObject).setState(arrayOfInt);
      }
    }
    if (mForegroundInfo != null) {
      localObject = mForegroundInfo.mDrawable;
    } else {
      localObject = null;
    }
    bool2 = bool1;
    if (localObject != null)
    {
      bool2 = bool1;
      if (((Drawable)localObject).isStateful()) {
        bool2 = bool1 | ((Drawable)localObject).setState(arrayOfInt);
      }
    }
    bool1 = bool2;
    if (mScrollCache != null)
    {
      localObject = mScrollCache.scrollBar;
      bool1 = bool2;
      if (localObject != null)
      {
        bool1 = bool2;
        if (((Drawable)localObject).isStateful())
        {
          if ((((Drawable)localObject).setState(arrayOfInt)) && (mScrollCache.state != 0)) {
            bool1 = true;
          } else {
            bool1 = false;
          }
          bool1 = bool2 | bool1;
        }
      }
    }
    if (mStateListAnimator != null) {
      mStateListAnimator.setState(arrayOfInt);
    }
    if (bool1) {
      invalidate();
    }
  }
  
  public void encode(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    paramViewHierarchyEncoder.beginObject(this);
    encodeProperties(paramViewHierarchyEncoder);
    paramViewHierarchyEncoder.endObject();
  }
  
  protected void encodeProperties(ViewHierarchyEncoder paramViewHierarchyEncoder)
  {
    Object localObject = ViewDebug.resolveId(getContext(), mID);
    if ((localObject instanceof String)) {
      paramViewHierarchyEncoder.addProperty("id", (String)localObject);
    } else {
      paramViewHierarchyEncoder.addProperty("id", mID);
    }
    float f;
    if (mTransformationInfo != null) {
      f = mTransformationInfo.mAlpha;
    } else {
      f = 0.0F;
    }
    paramViewHierarchyEncoder.addProperty("misc:transformation.alpha", f);
    paramViewHierarchyEncoder.addProperty("misc:transitionName", getTransitionName());
    paramViewHierarchyEncoder.addProperty("layout:left", mLeft);
    paramViewHierarchyEncoder.addProperty("layout:right", mRight);
    paramViewHierarchyEncoder.addProperty("layout:top", mTop);
    paramViewHierarchyEncoder.addProperty("layout:bottom", mBottom);
    paramViewHierarchyEncoder.addProperty("layout:width", getWidth());
    paramViewHierarchyEncoder.addProperty("layout:height", getHeight());
    paramViewHierarchyEncoder.addProperty("layout:layoutDirection", getLayoutDirection());
    paramViewHierarchyEncoder.addProperty("layout:layoutRtl", isLayoutRtl());
    paramViewHierarchyEncoder.addProperty("layout:hasTransientState", hasTransientState());
    paramViewHierarchyEncoder.addProperty("layout:baseline", getBaseline());
    localObject = getLayoutParams();
    if (localObject != null)
    {
      paramViewHierarchyEncoder.addPropertyKey("layoutParams");
      ((ViewGroup.LayoutParams)localObject).encode(paramViewHierarchyEncoder);
    }
    paramViewHierarchyEncoder.addProperty("scrolling:scrollX", mScrollX);
    paramViewHierarchyEncoder.addProperty("scrolling:scrollY", mScrollY);
    paramViewHierarchyEncoder.addProperty("padding:paddingLeft", mPaddingLeft);
    paramViewHierarchyEncoder.addProperty("padding:paddingRight", mPaddingRight);
    paramViewHierarchyEncoder.addProperty("padding:paddingTop", mPaddingTop);
    paramViewHierarchyEncoder.addProperty("padding:paddingBottom", mPaddingBottom);
    paramViewHierarchyEncoder.addProperty("padding:userPaddingRight", mUserPaddingRight);
    paramViewHierarchyEncoder.addProperty("padding:userPaddingLeft", mUserPaddingLeft);
    paramViewHierarchyEncoder.addProperty("padding:userPaddingBottom", mUserPaddingBottom);
    paramViewHierarchyEncoder.addProperty("padding:userPaddingStart", mUserPaddingStart);
    paramViewHierarchyEncoder.addProperty("padding:userPaddingEnd", mUserPaddingEnd);
    paramViewHierarchyEncoder.addProperty("measurement:minHeight", mMinHeight);
    paramViewHierarchyEncoder.addProperty("measurement:minWidth", mMinWidth);
    paramViewHierarchyEncoder.addProperty("measurement:measuredWidth", mMeasuredWidth);
    paramViewHierarchyEncoder.addProperty("measurement:measuredHeight", mMeasuredHeight);
    paramViewHierarchyEncoder.addProperty("drawing:elevation", getElevation());
    paramViewHierarchyEncoder.addProperty("drawing:translationX", getTranslationX());
    paramViewHierarchyEncoder.addProperty("drawing:translationY", getTranslationY());
    paramViewHierarchyEncoder.addProperty("drawing:translationZ", getTranslationZ());
    paramViewHierarchyEncoder.addProperty("drawing:rotation", getRotation());
    paramViewHierarchyEncoder.addProperty("drawing:rotationX", getRotationX());
    paramViewHierarchyEncoder.addProperty("drawing:rotationY", getRotationY());
    paramViewHierarchyEncoder.addProperty("drawing:scaleX", getScaleX());
    paramViewHierarchyEncoder.addProperty("drawing:scaleY", getScaleY());
    paramViewHierarchyEncoder.addProperty("drawing:pivotX", getPivotX());
    paramViewHierarchyEncoder.addProperty("drawing:pivotY", getPivotY());
    if (mClipBounds == null) {
      localObject = null;
    } else {
      localObject = mClipBounds.toString();
    }
    paramViewHierarchyEncoder.addProperty("drawing:clipBounds", (String)localObject);
    paramViewHierarchyEncoder.addProperty("drawing:opaque", isOpaque());
    paramViewHierarchyEncoder.addProperty("drawing:alpha", getAlpha());
    paramViewHierarchyEncoder.addProperty("drawing:transitionAlpha", getTransitionAlpha());
    paramViewHierarchyEncoder.addProperty("drawing:shadow", hasShadow());
    paramViewHierarchyEncoder.addProperty("drawing:solidColor", getSolidColor());
    paramViewHierarchyEncoder.addProperty("drawing:layerType", mLayerType);
    paramViewHierarchyEncoder.addProperty("drawing:willNotDraw", willNotDraw());
    paramViewHierarchyEncoder.addProperty("drawing:hardwareAccelerated", isHardwareAccelerated());
    paramViewHierarchyEncoder.addProperty("drawing:willNotCacheDrawing", willNotCacheDrawing());
    paramViewHierarchyEncoder.addProperty("drawing:drawingCacheEnabled", isDrawingCacheEnabled());
    paramViewHierarchyEncoder.addProperty("drawing:overlappingRendering", hasOverlappingRendering());
    paramViewHierarchyEncoder.addProperty("drawing:outlineAmbientShadowColor", getOutlineAmbientShadowColor());
    paramViewHierarchyEncoder.addProperty("drawing:outlineSpotShadowColor", getOutlineSpotShadowColor());
    paramViewHierarchyEncoder.addProperty("focus:hasFocus", hasFocus());
    paramViewHierarchyEncoder.addProperty("focus:isFocused", isFocused());
    paramViewHierarchyEncoder.addProperty("focus:focusable", getFocusable());
    paramViewHierarchyEncoder.addProperty("focus:isFocusable", isFocusable());
    paramViewHierarchyEncoder.addProperty("focus:isFocusableInTouchMode", isFocusableInTouchMode());
    paramViewHierarchyEncoder.addProperty("misc:clickable", isClickable());
    paramViewHierarchyEncoder.addProperty("misc:pressed", isPressed());
    paramViewHierarchyEncoder.addProperty("misc:selected", isSelected());
    paramViewHierarchyEncoder.addProperty("misc:touchMode", isInTouchMode());
    paramViewHierarchyEncoder.addProperty("misc:hovered", isHovered());
    paramViewHierarchyEncoder.addProperty("misc:activated", isActivated());
    paramViewHierarchyEncoder.addProperty("misc:visibility", getVisibility());
    paramViewHierarchyEncoder.addProperty("misc:fitsSystemWindows", getFitsSystemWindows());
    paramViewHierarchyEncoder.addProperty("misc:filterTouchesWhenObscured", getFilterTouchesWhenObscured());
    paramViewHierarchyEncoder.addProperty("misc:enabled", isEnabled());
    paramViewHierarchyEncoder.addProperty("misc:soundEffectsEnabled", isSoundEffectsEnabled());
    paramViewHierarchyEncoder.addProperty("misc:hapticFeedbackEnabled", isHapticFeedbackEnabled());
    localObject = getContext().getTheme();
    if (localObject != null)
    {
      paramViewHierarchyEncoder.addPropertyKey("theme");
      ((Resources.Theme)localObject).encode(paramViewHierarchyEncoder);
    }
    localObject = mAttributes;
    int i = 0;
    int j;
    if (localObject != null) {
      j = mAttributes.length;
    } else {
      j = 0;
    }
    paramViewHierarchyEncoder.addProperty("meta:__attrCount__", j / 2);
    while (i < j)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("meta:__attr__");
      ((StringBuilder)localObject).append(mAttributes[i]);
      paramViewHierarchyEncoder.addProperty(((StringBuilder)localObject).toString(), mAttributes[(i + 1)]);
      i += 2;
    }
    paramViewHierarchyEncoder.addProperty("misc:scrollBarStyle", getScrollBarStyle());
    paramViewHierarchyEncoder.addProperty("text:textDirection", getTextDirection());
    paramViewHierarchyEncoder.addProperty("text:textAlignment", getTextAlignment());
    localObject = getContentDescription();
    if (localObject == null) {
      localObject = "";
    } else {
      localObject = ((CharSequence)localObject).toString();
    }
    paramViewHierarchyEncoder.addProperty("accessibility:contentDescription", (String)localObject);
    paramViewHierarchyEncoder.addProperty("accessibility:labelFor", getLabelFor());
    paramViewHierarchyEncoder.addProperty("accessibility:importantForAccessibility", getImportantForAccessibility());
  }
  
  void ensureTransformationInfo()
  {
    if (mTransformationInfo == null) {
      mTransformationInfo = new TransformationInfo();
    }
  }
  
  public View findFocus()
  {
    View localView;
    if ((mPrivateFlags & 0x2) != 0) {
      localView = this;
    } else {
      localView = null;
    }
    return localView;
  }
  
  View findKeyboardNavigationCluster()
  {
    if ((mParent instanceof View))
    {
      View localView = ((View)mParent).findKeyboardNavigationCluster();
      if (localView != null) {
        return localView;
      }
      if (isKeyboardNavigationCluster()) {
        return this;
      }
    }
    return null;
  }
  
  public void findNamedViews(Map<String, View> paramMap)
  {
    if ((getVisibility() == 0) || (mGhostView != null))
    {
      String str = getTransitionName();
      if (str != null) {
        paramMap.put(str, this);
      }
    }
  }
  
  View findUserSetNextFocus(View paramView, int paramInt)
  {
    if (paramInt != 17)
    {
      if (paramInt != 33)
      {
        if (paramInt != 66)
        {
          if (paramInt != 130)
          {
            switch (paramInt)
            {
            default: 
              return null;
            case 2: 
              if (mNextFocusForwardId == -1) {
                return null;
              }
              return findViewInsideOutShouldExist(paramView, mNextFocusForwardId);
            }
            if (mID == -1) {
              return null;
            }
            paramView.findViewByPredicateInsideOut(this, new Predicate()
            {
              public boolean test(View paramAnonymousView)
              {
                boolean bool;
                if (mNextFocusForwardId == val$id) {
                  bool = true;
                } else {
                  bool = false;
                }
                return bool;
              }
            });
          }
          if (mNextFocusDownId == -1) {
            return null;
          }
          return findViewInsideOutShouldExist(paramView, mNextFocusDownId);
        }
        if (mNextFocusRightId == -1) {
          return null;
        }
        return findViewInsideOutShouldExist(paramView, mNextFocusRightId);
      }
      if (mNextFocusUpId == -1) {
        return null;
      }
      return findViewInsideOutShouldExist(paramView, mNextFocusUpId);
    }
    if (mNextFocusLeftId == -1) {
      return null;
    }
    return findViewInsideOutShouldExist(paramView, mNextFocusLeftId);
  }
  
  View findUserSetNextKeyboardNavigationCluster(View paramView, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return null;
    case 2: 
      if (mNextClusterForwardId == -1) {
        return null;
      }
      return findViewInsideOutShouldExist(paramView, mNextClusterForwardId);
    }
    if (mID == -1) {
      return null;
    }
    return paramView.findViewByPredicateInsideOut(this, new _..Lambda.View.7kZ4TXHKswReUMQB8098MEBcx_U(mID));
  }
  
  final <T extends View> T findViewByAccessibilityId(int paramInt)
  {
    Object localObject = null;
    if (paramInt < 0) {
      return null;
    }
    View localView = findViewByAccessibilityIdTraversal(paramInt);
    if (localView != null)
    {
      if (localView.includeForAccessibility()) {
        localObject = localView;
      }
      return localObject;
    }
    return null;
  }
  
  public <T extends View> T findViewByAccessibilityIdTraversal(int paramInt)
  {
    if (getAccessibilityViewId() == paramInt) {
      return this;
    }
    return null;
  }
  
  public <T extends View> T findViewByAutofillIdTraversal(int paramInt)
  {
    if (getAutofillViewId() == paramInt) {
      return this;
    }
    return null;
  }
  
  public final <T extends View> T findViewById(int paramInt)
  {
    if (paramInt == -1) {
      return null;
    }
    return findViewTraversal(paramInt);
  }
  
  public final <T extends View> T findViewByPredicate(Predicate<View> paramPredicate)
  {
    return findViewByPredicateTraversal(paramPredicate, null);
  }
  
  public final <T extends View> T findViewByPredicateInsideOut(View paramView, Predicate<View> paramPredicate)
  {
    View localView = null;
    for (;;)
    {
      localView = paramView.findViewByPredicateTraversal(paramPredicate, localView);
      if ((localView != null) || (paramView == this)) {
        return localView;
      }
      ViewParent localViewParent = paramView.getParent();
      if ((localViewParent == null) || (!(localViewParent instanceof View))) {
        break;
      }
      localView = paramView;
      paramView = (View)localViewParent;
    }
    return null;
    return localView;
  }
  
  protected <T extends View> T findViewByPredicateTraversal(Predicate<View> paramPredicate, View paramView)
  {
    if (paramPredicate.test(this)) {
      return this;
    }
    return null;
  }
  
  protected <T extends View> T findViewTraversal(int paramInt)
  {
    if (paramInt == mID) {
      return this;
    }
    return null;
  }
  
  public final <T extends View> T findViewWithTag(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    return findViewWithTagTraversal(paramObject);
  }
  
  protected <T extends View> T findViewWithTagTraversal(Object paramObject)
  {
    if ((paramObject != null) && (paramObject.equals(mTag))) {
      return this;
    }
    return null;
  }
  
  public void findViewsWithText(ArrayList<View> paramArrayList, CharSequence paramCharSequence, int paramInt)
  {
    if (getAccessibilityNodeProvider() != null)
    {
      if ((paramInt & 0x4) != 0) {
        paramArrayList.add(this);
      }
    }
    else if (((paramInt & 0x2) != 0) && (paramCharSequence != null) && (paramCharSequence.length() > 0) && (mContentDescription != null) && (mContentDescription.length() > 0))
    {
      paramCharSequence = paramCharSequence.toString().toLowerCase();
      if (mContentDescription.toString().toLowerCase().contains(paramCharSequence)) {
        paramArrayList.add(this);
      }
    }
  }
  
  /* Error */
  @Deprecated
  protected boolean fitSystemWindows(Rect paramRect)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 1439	android/view/View:mPrivateFlags3	I
    //   4: bipush 32
    //   6: iand
    //   7: ifne +65 -> 72
    //   10: aload_1
    //   11: ifnonnull +5 -> 16
    //   14: iconst_0
    //   15: ireturn
    //   16: aload_0
    //   17: aload_0
    //   18: getfield 1439	android/view/View:mPrivateFlags3	I
    //   21: bipush 64
    //   23: ior
    //   24: putfield 1439	android/view/View:mPrivateFlags3	I
    //   27: new 3880	android/view/WindowInsets
    //   30: astore_2
    //   31: aload_2
    //   32: aload_1
    //   33: invokespecial 3882	android/view/WindowInsets:<init>	(Landroid/graphics/Rect;)V
    //   36: aload_0
    //   37: aload_2
    //   38: invokevirtual 5017	android/view/View:dispatchApplyWindowInsets	(Landroid/view/WindowInsets;)Landroid/view/WindowInsets;
    //   41: invokevirtual 5020	android/view/WindowInsets:isConsumed	()Z
    //   44: istore_3
    //   45: aload_0
    //   46: aload_0
    //   47: getfield 1439	android/view/View:mPrivateFlags3	I
    //   50: bipush -65
    //   52: iand
    //   53: putfield 1439	android/view/View:mPrivateFlags3	I
    //   56: iload_3
    //   57: ireturn
    //   58: astore_1
    //   59: aload_0
    //   60: aload_0
    //   61: getfield 1439	android/view/View:mPrivateFlags3	I
    //   64: bipush -65
    //   66: iand
    //   67: putfield 1439	android/view/View:mPrivateFlags3	I
    //   70: aload_1
    //   71: athrow
    //   72: aload_0
    //   73: aload_1
    //   74: invokespecial 5022	android/view/View:fitSystemWindowsInt	(Landroid/graphics/Rect;)Z
    //   77: ireturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	78	0	this	View
    //   0	78	1	paramRect	Rect
    //   30	8	2	localWindowInsets	WindowInsets
    //   44	13	3	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   16	45	58	finally
  }
  
  public boolean fitsSystemWindows()
  {
    return getFitsSystemWindows();
  }
  
  public View focusSearch(int paramInt)
  {
    if (mParent != null) {
      return mParent.focusSearch(this, paramInt);
    }
    return null;
  }
  
  public void forceHasOverlappingRendering(boolean paramBoolean)
  {
    mPrivateFlags3 |= 0x1000000;
    if (paramBoolean) {
      mPrivateFlags3 |= 0x800000;
    } else {
      mPrivateFlags3 &= 0xFF7FFFFF;
    }
  }
  
  public void forceLayout()
  {
    if (mMeasureCache != null) {
      mMeasureCache.clear();
    }
    mPrivateFlags |= 0x1000;
    mPrivateFlags |= 0x80000000;
  }
  
  public boolean gatherTransparentRegion(Region paramRegion)
  {
    Object localObject = mAttachInfo;
    if ((paramRegion != null) && (localObject != null)) {
      if ((mPrivateFlags & 0x80) == 0)
      {
        localObject = mTransparentLocation;
        getLocationInWindow((int[])localObject);
        int i;
        if (getZ() > 0.0F) {
          i = (int)getZ();
        } else {
          i = 0;
        }
        paramRegion.op(localObject[0] - i, localObject[1] - i, localObject[0] + mRight - mLeft + i, localObject[1] + mBottom - mTop + i * 3, Region.Op.DIFFERENCE);
      }
      else
      {
        if ((mBackground != null) && (mBackground.getOpacity() != -2)) {
          applyDrawableToTransparentRegion(mBackground, paramRegion);
        }
        if ((mForegroundInfo != null) && (mForegroundInfo.mDrawable != null) && (mForegroundInfo.mDrawable.getOpacity() != -2)) {
          applyDrawableToTransparentRegion(mForegroundInfo.mDrawable, paramRegion);
        }
        if ((mDefaultFocusHighlight != null) && (mDefaultFocusHighlight.getOpacity() != -2)) {
          applyDrawableToTransparentRegion(mDefaultFocusHighlight, paramRegion);
        }
      }
    }
    return true;
  }
  
  public CharSequence getAccessibilityClassName()
  {
    return View.class.getName();
  }
  
  public AccessibilityDelegate getAccessibilityDelegate()
  {
    return mAccessibilityDelegate;
  }
  
  public int getAccessibilityLiveRegion()
  {
    return (mPrivateFlags2 & 0x1800000) >> 23;
  }
  
  public AccessibilityNodeProvider getAccessibilityNodeProvider()
  {
    if (mAccessibilityDelegate != null) {
      return mAccessibilityDelegate.getAccessibilityNodeProvider(this);
    }
    return null;
  }
  
  public CharSequence getAccessibilityPaneTitle()
  {
    return mAccessibilityPaneTitle;
  }
  
  public int getAccessibilitySelectionEnd()
  {
    return getAccessibilitySelectionStart();
  }
  
  public int getAccessibilitySelectionStart()
  {
    return mAccessibilityCursorPosition;
  }
  
  public int getAccessibilityTraversalAfter()
  {
    return mAccessibilityTraversalAfterId;
  }
  
  public int getAccessibilityTraversalBefore()
  {
    return mAccessibilityTraversalBeforeId;
  }
  
  public int getAccessibilityViewId()
  {
    if (mAccessibilityViewId == -1)
    {
      int i = sNextAccessibilityViewId;
      sNextAccessibilityViewId = i + 1;
      mAccessibilityViewId = i;
    }
    return mAccessibilityViewId;
  }
  
  public int getAccessibilityWindowId()
  {
    int i;
    if (mAttachInfo != null) {
      i = mAttachInfo.mAccessibilityWindowId;
    } else {
      i = -1;
    }
    return i;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getAlpha()
  {
    float f;
    if (mTransformationInfo != null) {
      f = mTransformationInfo.mAlpha;
    } else {
      f = 1.0F;
    }
    return f;
  }
  
  public Animation getAnimation()
  {
    return mCurrentAnimation;
  }
  
  public IBinder getApplicationWindowToken()
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null)
    {
      IBinder localIBinder1 = mPanelParentWindowToken;
      IBinder localIBinder2 = localIBinder1;
      if (localIBinder1 == null) {
        localIBinder2 = mWindowToken;
      }
      return localIBinder2;
    }
    return null;
  }
  
  @ViewDebug.ExportedProperty
  public String[] getAutofillHints()
  {
    return mAutofillHints;
  }
  
  public final AutofillId getAutofillId()
  {
    if (mAutofillId == null) {
      mAutofillId = new AutofillId(getAutofillViewId());
    }
    return mAutofillId;
  }
  
  public int getAutofillType()
  {
    return 0;
  }
  
  public AutofillValue getAutofillValue()
  {
    return null;
  }
  
  public int getAutofillViewId()
  {
    if (mAutofillViewId == -1) {
      mAutofillViewId = mContext.getNextAutofillId();
    }
    return mAutofillViewId;
  }
  
  public Drawable getBackground()
  {
    return mBackground;
  }
  
  public ColorStateList getBackgroundTintList()
  {
    ColorStateList localColorStateList;
    if (mBackgroundTint != null) {
      localColorStateList = mBackgroundTint.mTintList;
    } else {
      localColorStateList = null;
    }
    return localColorStateList;
  }
  
  public PorterDuff.Mode getBackgroundTintMode()
  {
    PorterDuff.Mode localMode;
    if (mBackgroundTint != null) {
      localMode = mBackgroundTint.mTintMode;
    } else {
      localMode = null;
    }
    return localMode;
  }
  
  @ViewDebug.ExportedProperty(category="layout")
  public int getBaseline()
  {
    return -1;
  }
  
  @ViewDebug.CapturedViewProperty
  public final int getBottom()
  {
    return mBottom;
  }
  
  protected float getBottomFadingEdgeStrength()
  {
    float f;
    if (computeVerticalScrollOffset() + computeVerticalScrollExtent() < computeVerticalScrollRange()) {
      f = 1.0F;
    } else {
      f = 0.0F;
    }
    return f;
  }
  
  protected int getBottomPaddingOffset()
  {
    return 0;
  }
  
  public void getBoundsOnScreen(Rect paramRect)
  {
    getBoundsOnScreen(paramRect, false);
  }
  
  public void getBoundsOnScreen(Rect paramRect, boolean paramBoolean)
  {
    if (mAttachInfo == null) {
      return;
    }
    RectF localRectF = mAttachInfo.mTmpTransformRect;
    localRectF.set(0.0F, 0.0F, mRight - mLeft, mBottom - mTop);
    mapRectFromViewToScreenCoords(localRectF, paramBoolean);
    paramRect.set(Math.round(left), Math.round(top), Math.round(right), Math.round(bottom));
  }
  
  public float getCameraDistance()
  {
    float f = mResources.getDisplayMetrics().densityDpi;
    return -(mRenderNode.getCameraDistance() * f);
  }
  
  public Rect getClipBounds()
  {
    Rect localRect;
    if (mClipBounds != null) {
      localRect = new Rect(mClipBounds);
    } else {
      localRect = null;
    }
    return localRect;
  }
  
  public boolean getClipBounds(Rect paramRect)
  {
    if (mClipBounds != null)
    {
      paramRect.set(mClipBounds);
      return true;
    }
    return false;
  }
  
  public final boolean getClipToOutline()
  {
    return mRenderNode.getClipToOutline();
  }
  
  @ViewDebug.ExportedProperty(category="accessibility")
  public CharSequence getContentDescription()
  {
    return mContentDescription;
  }
  
  @ViewDebug.CapturedViewProperty
  public final Context getContext()
  {
    return mContext;
  }
  
  protected ContextMenu.ContextMenuInfo getContextMenuInfo()
  {
    return null;
  }
  
  @ViewDebug.ExportedProperty(category="focus")
  public final boolean getDefaultFocusHighlightEnabled()
  {
    return mDefaultFocusHighlightEnabled;
  }
  
  public Display getDisplay()
  {
    Display localDisplay;
    if (mAttachInfo != null) {
      localDisplay = mAttachInfo.mDisplay;
    } else {
      localDisplay = null;
    }
    return localDisplay;
  }
  
  public final int[] getDrawableState()
  {
    if ((mDrawableState != null) && ((mPrivateFlags & 0x400) == 0)) {
      return mDrawableState;
    }
    mDrawableState = onCreateDrawableState(0);
    mPrivateFlags &= 0xFBFF;
    return mDrawableState;
  }
  
  @Deprecated
  public Bitmap getDrawingCache()
  {
    return getDrawingCache(false);
  }
  
  @Deprecated
  public Bitmap getDrawingCache(boolean paramBoolean)
  {
    if ((mViewFlags & 0x20000) == 131072) {
      return null;
    }
    if ((mViewFlags & 0x8000) == 32768) {
      buildDrawingCache(paramBoolean);
    }
    Bitmap localBitmap;
    if (paramBoolean) {
      localBitmap = mDrawingCache;
    } else {
      localBitmap = mUnscaledDrawingCache;
    }
    return localBitmap;
  }
  
  @Deprecated
  public int getDrawingCacheBackgroundColor()
  {
    return mDrawingCacheBackgroundColor;
  }
  
  @Deprecated
  public int getDrawingCacheQuality()
  {
    return mViewFlags & 0x180000;
  }
  
  public void getDrawingRect(Rect paramRect)
  {
    left = mScrollX;
    top = mScrollY;
    right = (mScrollX + (mRight - mLeft));
    bottom = (mScrollY + (mBottom - mTop));
  }
  
  public long getDrawingTime()
  {
    long l;
    if (mAttachInfo != null) {
      l = mAttachInfo.mDrawingTime;
    } else {
      l = 0L;
    }
    return l;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getElevation()
  {
    return mRenderNode.getElevation();
  }
  
  protected int getFadeHeight(boolean paramBoolean)
  {
    int i = mPaddingTop;
    int j = i;
    if (paramBoolean) {
      j = i + getTopPaddingOffset();
    }
    return mBottom - mTop - mPaddingBottom - j;
  }
  
  protected int getFadeTop(boolean paramBoolean)
  {
    int i = mPaddingTop;
    int j = i;
    if (paramBoolean) {
      j = i + getTopPaddingOffset();
    }
    return j;
  }
  
  @ViewDebug.ExportedProperty
  public boolean getFilterTouchesWhenObscured()
  {
    boolean bool;
    if ((mViewFlags & 0x400) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty
  public boolean getFitsSystemWindows()
  {
    boolean bool;
    if ((mViewFlags & 0x2) == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="focus", mapping={@ViewDebug.IntToString(from=0, to="NOT_FOCUSABLE"), @ViewDebug.IntToString(from=1, to="FOCUSABLE"), @ViewDebug.IntToString(from=16, to="FOCUSABLE_AUTO")})
  public int getFocusable()
  {
    int i = mViewFlags;
    int j = 16;
    if ((i & 0x10) <= 0) {
      j = mViewFlags & 0x1;
    }
    return j;
  }
  
  public ArrayList<View> getFocusables(int paramInt)
  {
    ArrayList localArrayList = new ArrayList(24);
    addFocusables(localArrayList, paramInt);
    return localArrayList;
  }
  
  public void getFocusedRect(Rect paramRect)
  {
    getDrawingRect(paramRect);
  }
  
  public Drawable getForeground()
  {
    Drawable localDrawable;
    if (mForegroundInfo != null) {
      localDrawable = mForegroundInfo.mDrawable;
    } else {
      localDrawable = null;
    }
    return localDrawable;
  }
  
  public int getForegroundGravity()
  {
    int i;
    if (mForegroundInfo != null) {
      i = mForegroundInfo.mGravity;
    } else {
      i = 8388659;
    }
    return i;
  }
  
  public ColorStateList getForegroundTintList()
  {
    ColorStateList localColorStateList;
    if ((mForegroundInfo != null) && (mForegroundInfo.mTintInfo != null)) {
      localColorStateList = mForegroundInfo.mTintInfo.mTintList;
    } else {
      localColorStateList = null;
    }
    return localColorStateList;
  }
  
  public PorterDuff.Mode getForegroundTintMode()
  {
    PorterDuff.Mode localMode;
    if ((mForegroundInfo != null) && (mForegroundInfo.mTintInfo != null)) {
      localMode = mForegroundInfo.mTintInfo.mTintMode;
    } else {
      localMode = null;
    }
    return localMode;
  }
  
  public final boolean getGlobalVisibleRect(Rect paramRect)
  {
    return getGlobalVisibleRect(paramRect, null);
  }
  
  public boolean getGlobalVisibleRect(Rect paramRect, Point paramPoint)
  {
    int i = mRight - mLeft;
    int j = mBottom - mTop;
    boolean bool = false;
    if ((i > 0) && (j > 0))
    {
      paramRect.set(0, 0, i, j);
      if (paramPoint != null) {
        paramPoint.set(-mScrollX, -mScrollY);
      }
      if ((mParent != null) && (!mParent.getChildVisibleRect(this, paramRect, paramPoint))) {
        break label91;
      }
      bool = true;
      label91:
      return bool;
    }
    return false;
  }
  
  public Handler getHandler()
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null) {
      return mHandler;
    }
    return null;
  }
  
  public final boolean getHasOverlappingRendering()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x1000000) != 0)
    {
      if ((mPrivateFlags3 & 0x800000) != 0) {
        bool = true;
      } else {
        bool = false;
      }
    }
    else {
      bool = hasOverlappingRendering();
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="layout")
  public final int getHeight()
  {
    return mBottom - mTop;
  }
  
  public void getHitRect(Rect paramRect)
  {
    if ((!hasIdentityMatrix()) && (mAttachInfo != null))
    {
      RectF localRectF = mAttachInfo.mTmpTransformRect;
      localRectF.set(0.0F, 0.0F, getWidth(), getHeight());
      getMatrix().mapRect(localRectF);
      paramRect.set((int)left + mLeft, (int)top + mTop, (int)right + mLeft, (int)bottom + mTop);
    }
    else
    {
      paramRect.set(mLeft, mTop, mRight, mBottom);
    }
  }
  
  public int getHorizontalFadingEdgeLength()
  {
    if (isHorizontalFadingEdgeEnabled())
    {
      ScrollabilityCache localScrollabilityCache = mScrollCache;
      if (localScrollabilityCache != null) {
        return fadingEdgeLength;
      }
    }
    return 0;
  }
  
  protected float getHorizontalScrollFactor()
  {
    return getVerticalScrollFactor();
  }
  
  protected int getHorizontalScrollbarHeight()
  {
    ScrollabilityCache localScrollabilityCache = mScrollCache;
    if (localScrollabilityCache != null)
    {
      ScrollBarDrawable localScrollBarDrawable = scrollBar;
      if (localScrollBarDrawable != null)
      {
        int i = localScrollBarDrawable.getSize(false);
        int j = i;
        if (i <= 0) {
          j = scrollBarSize;
        }
        return j;
      }
      return 0;
    }
    return 0;
  }
  
  public void getHotspotBounds(Rect paramRect)
  {
    Drawable localDrawable = getBackground();
    if (localDrawable != null) {
      localDrawable.getHotspotBounds(paramRect);
    } else {
      getBoundsOnScreen(paramRect);
    }
  }
  
  @ViewDebug.CapturedViewProperty
  public int getId()
  {
    return mID;
  }
  
  @ViewDebug.ExportedProperty(category="accessibility", mapping={@ViewDebug.IntToString(from=0, to="auto"), @ViewDebug.IntToString(from=1, to="yes"), @ViewDebug.IntToString(from=2, to="no"), @ViewDebug.IntToString(from=4, to="noHideDescendants")})
  public int getImportantForAccessibility()
  {
    return (mPrivateFlags2 & 0x700000) >> 20;
  }
  
  @ViewDebug.ExportedProperty(mapping={@ViewDebug.IntToString(from=0, to="auto"), @ViewDebug.IntToString(from=1, to="yes"), @ViewDebug.IntToString(from=2, to="no"), @ViewDebug.IntToString(from=4, to="yesExcludeDescendants"), @ViewDebug.IntToString(from=8, to="noExcludeDescendants")})
  public int getImportantForAutofill()
  {
    return (mPrivateFlags3 & 0x780000) >> 19;
  }
  
  public final Matrix getInverseMatrix()
  {
    ensureTransformationInfo();
    if (mTransformationInfo.mInverseMatrix == null) {
      TransformationInfo.access$2002(mTransformationInfo, new Matrix());
    }
    Matrix localMatrix = mTransformationInfo.mInverseMatrix;
    mRenderNode.getInverseMatrix(localMatrix);
    return localMatrix;
  }
  
  public CharSequence getIterableTextForAccessibility()
  {
    return getContentDescription();
  }
  
  public AccessibilityIterators.TextSegmentIterator getIteratorForGranularity(int paramInt)
  {
    Object localObject1;
    Object localObject2;
    if (paramInt != 8)
    {
      switch (paramInt)
      {
      default: 
        break;
      case 2: 
        localObject1 = getIterableTextForAccessibility();
        if ((localObject1 != null) && (((CharSequence)localObject1).length() > 0))
        {
          localObject2 = AccessibilityIterators.WordTextSegmentIterator.getInstance(mContext.getResources().getConfiguration().locale);
          ((AccessibilityIterators.WordTextSegmentIterator)localObject2).initialize(((CharSequence)localObject1).toString());
          return localObject2;
        }
        break;
      case 1: 
        localObject2 = getIterableTextForAccessibility();
        if ((localObject2 != null) && (((CharSequence)localObject2).length() > 0))
        {
          localObject1 = AccessibilityIterators.CharacterTextSegmentIterator.getInstance(mContext.getResources().getConfiguration().locale);
          ((AccessibilityIterators.CharacterTextSegmentIterator)localObject1).initialize(((CharSequence)localObject2).toString());
          return localObject1;
        }
        break;
      }
    }
    else
    {
      localObject2 = getIterableTextForAccessibility();
      if ((localObject2 != null) && (((CharSequence)localObject2).length() > 0))
      {
        localObject1 = AccessibilityIterators.ParagraphTextSegmentIterator.getInstance();
        ((AccessibilityIterators.ParagraphTextSegmentIterator)localObject1).initialize(((CharSequence)localObject2).toString());
        return localObject1;
      }
    }
    return null;
  }
  
  public boolean getKeepScreenOn()
  {
    boolean bool;
    if ((mViewFlags & 0x4000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public KeyEvent.DispatcherState getKeyDispatcherState()
  {
    KeyEvent.DispatcherState localDispatcherState;
    if (mAttachInfo != null) {
      localDispatcherState = mAttachInfo.mKeyDispatchState;
    } else {
      localDispatcherState = null;
    }
    return localDispatcherState;
  }
  
  @ViewDebug.ExportedProperty(category="accessibility")
  public int getLabelFor()
  {
    return mLabelForId;
  }
  
  public int getLayerType()
  {
    return mLayerType;
  }
  
  @ViewDebug.ExportedProperty(category="layout", mapping={@ViewDebug.IntToString(from=0, to="RESOLVED_DIRECTION_LTR"), @ViewDebug.IntToString(from=1, to="RESOLVED_DIRECTION_RTL")})
  public int getLayoutDirection()
  {
    int i = getContextgetApplicationInfotargetSdkVersion;
    int j = 0;
    if (i < 17)
    {
      mPrivateFlags2 |= 0x20;
      return 0;
    }
    if ((mPrivateFlags2 & 0x10) == 16) {
      j = 1;
    }
    return j;
  }
  
  @ViewDebug.ExportedProperty(deepExport=true, prefix="layout_")
  public ViewGroup.LayoutParams getLayoutParams()
  {
    return mLayoutParams;
  }
  
  @ViewDebug.CapturedViewProperty
  public final int getLeft()
  {
    return mLeft;
  }
  
  protected float getLeftFadingEdgeStrength()
  {
    float f;
    if (computeHorizontalScrollOffset() > 0) {
      f = 1.0F;
    } else {
      f = 0.0F;
    }
    return f;
  }
  
  protected int getLeftPaddingOffset()
  {
    return 0;
  }
  
  ListenerInfo getListenerInfo()
  {
    if (mListenerInfo != null) {
      return mListenerInfo;
    }
    mListenerInfo = new ListenerInfo();
    return mListenerInfo;
  }
  
  public final boolean getLocalVisibleRect(Rect paramRect)
  {
    Point localPoint;
    if (mAttachInfo != null) {
      localPoint = mAttachInfo.mPoint;
    } else {
      localPoint = new Point();
    }
    if (getGlobalVisibleRect(paramRect, localPoint))
    {
      paramRect.offset(-x, -y);
      return true;
    }
    return false;
  }
  
  public void getLocationInSurface(int[] paramArrayOfInt)
  {
    getLocationInWindow(paramArrayOfInt);
    if ((mAttachInfo != null) && (mAttachInfo.mViewRootImpl != null))
    {
      paramArrayOfInt[0] += mAttachInfo.mViewRootImpl.mWindowAttributes.surfaceInsets.left;
      paramArrayOfInt[1] += mAttachInfo.mViewRootImpl.mWindowAttributes.surfaceInsets.top;
    }
  }
  
  public void getLocationInWindow(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length >= 2))
    {
      paramArrayOfInt[0] = 0;
      paramArrayOfInt[1] = 0;
      transformFromViewToWindowSpace(paramArrayOfInt);
      return;
    }
    throw new IllegalArgumentException("outLocation must be an array of two integers");
  }
  
  public void getLocationOnScreen(int[] paramArrayOfInt)
  {
    getLocationInWindow(paramArrayOfInt);
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null)
    {
      paramArrayOfInt[0] += mWindowLeft;
      paramArrayOfInt[1] += mWindowTop;
    }
  }
  
  @ViewDebug.ExportedProperty(category="layout", indexMapping={@ViewDebug.IntToString(from=0, to="x"), @ViewDebug.IntToString(from=1, to="y")})
  public int[] getLocationOnScreen()
  {
    int[] arrayOfInt = new int[2];
    getLocationOnScreen(arrayOfInt);
    return arrayOfInt;
  }
  
  public Matrix getMatrix()
  {
    ensureTransformationInfo();
    Matrix localMatrix = mTransformationInfo.mMatrix;
    mRenderNode.getMatrix(localMatrix);
    return localMatrix;
  }
  
  public final int getMeasuredHeight()
  {
    return mMeasuredHeight & 0xFFFFFF;
  }
  
  @ViewDebug.ExportedProperty(category="measurement", flagMapping={@ViewDebug.FlagToString(equals=16777216, mask=-16777216, name="MEASURED_STATE_TOO_SMALL")})
  public final int getMeasuredHeightAndState()
  {
    return mMeasuredHeight;
  }
  
  public final int getMeasuredState()
  {
    return mMeasuredWidth & 0xFF000000 | mMeasuredHeight >> 16 & 0xFF00;
  }
  
  public final int getMeasuredWidth()
  {
    return mMeasuredWidth & 0xFFFFFF;
  }
  
  @ViewDebug.ExportedProperty(category="measurement", flagMapping={@ViewDebug.FlagToString(equals=16777216, mask=-16777216, name="MEASURED_STATE_TOO_SMALL")})
  public final int getMeasuredWidthAndState()
  {
    return mMeasuredWidth;
  }
  
  public int getMinimumHeight()
  {
    return mMinHeight;
  }
  
  public int getMinimumWidth()
  {
    return mMinWidth;
  }
  
  public int getNextClusterForwardId()
  {
    return mNextClusterForwardId;
  }
  
  public int getNextFocusDownId()
  {
    return mNextFocusDownId;
  }
  
  public int getNextFocusForwardId()
  {
    return mNextFocusForwardId;
  }
  
  public int getNextFocusLeftId()
  {
    return mNextFocusLeftId;
  }
  
  public int getNextFocusRightId()
  {
    return mNextFocusRightId;
  }
  
  public int getNextFocusUpId()
  {
    return mNextFocusUpId;
  }
  
  public OnFocusChangeListener getOnFocusChangeListener()
  {
    Object localObject = mListenerInfo;
    if (localObject != null) {
      localObject = mOnFocusChangeListener;
    } else {
      localObject = null;
    }
    return localObject;
  }
  
  public Insets getOpticalInsets()
  {
    if (mLayoutInsets == null) {
      mLayoutInsets = computeOpticalInsets();
    }
    return mLayoutInsets;
  }
  
  public int getOutlineAmbientShadowColor()
  {
    return mRenderNode.getAmbientShadowColor();
  }
  
  public ViewOutlineProvider getOutlineProvider()
  {
    return mOutlineProvider;
  }
  
  public int getOutlineSpotShadowColor()
  {
    return mRenderNode.getSpotShadowColor();
  }
  
  public void getOutsets(Rect paramRect)
  {
    if (mAttachInfo != null) {
      paramRect.set(mAttachInfo.mOutsets);
    } else {
      paramRect.setEmpty();
    }
  }
  
  public int getOverScrollMode()
  {
    return mOverScrollMode;
  }
  
  public ViewOverlay getOverlay()
  {
    if (mOverlay == null) {
      mOverlay = new ViewOverlay(mContext, this);
    }
    return mOverlay;
  }
  
  public int getPaddingBottom()
  {
    return mPaddingBottom;
  }
  
  public int getPaddingEnd()
  {
    if (!isPaddingResolved()) {
      resolvePadding();
    }
    int i;
    if (getLayoutDirection() == 1) {
      i = mPaddingLeft;
    } else {
      i = mPaddingRight;
    }
    return i;
  }
  
  public int getPaddingLeft()
  {
    if (!isPaddingResolved()) {
      resolvePadding();
    }
    return mPaddingLeft;
  }
  
  public int getPaddingRight()
  {
    if (!isPaddingResolved()) {
      resolvePadding();
    }
    return mPaddingRight;
  }
  
  public int getPaddingStart()
  {
    if (!isPaddingResolved()) {
      resolvePadding();
    }
    int i;
    if (getLayoutDirection() == 1) {
      i = mPaddingRight;
    } else {
      i = mPaddingLeft;
    }
    return i;
  }
  
  public int getPaddingTop()
  {
    return mPaddingTop;
  }
  
  public final ViewParent getParent()
  {
    return mParent;
  }
  
  public ViewParent getParentForAccessibility()
  {
    if ((mParent instanceof View))
    {
      if (((View)mParent).includeForAccessibility()) {
        return mParent;
      }
      return mParent.getParentForAccessibility();
    }
    return null;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getPivotX()
  {
    return mRenderNode.getPivotX();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getPivotY()
  {
    return mRenderNode.getPivotY();
  }
  
  public PointerIcon getPointerIcon()
  {
    return mPointerIcon;
  }
  
  @ViewDebug.ExportedProperty(category="layout", mapping={@ViewDebug.IntToString(from=0, to="LTR"), @ViewDebug.IntToString(from=1, to="RTL"), @ViewDebug.IntToString(from=2, to="INHERIT"), @ViewDebug.IntToString(from=3, to="LOCALE")})
  public int getRawLayoutDirection()
  {
    return (mPrivateFlags2 & 0xC) >> 2;
  }
  
  @ViewDebug.ExportedProperty(category="text", mapping={@ViewDebug.IntToString(from=0, to="INHERIT"), @ViewDebug.IntToString(from=1, to="GRAVITY"), @ViewDebug.IntToString(from=2, to="TEXT_START"), @ViewDebug.IntToString(from=3, to="TEXT_END"), @ViewDebug.IntToString(from=4, to="CENTER"), @ViewDebug.IntToString(from=5, to="VIEW_START"), @ViewDebug.IntToString(from=6, to="VIEW_END")})
  public int getRawTextAlignment()
  {
    return (mPrivateFlags2 & 0xE000) >> 13;
  }
  
  @ViewDebug.ExportedProperty(category="text", mapping={@ViewDebug.IntToString(from=0, to="INHERIT"), @ViewDebug.IntToString(from=1, to="FIRST_STRONG"), @ViewDebug.IntToString(from=2, to="ANY_RTL"), @ViewDebug.IntToString(from=3, to="LTR"), @ViewDebug.IntToString(from=4, to="RTL"), @ViewDebug.IntToString(from=5, to="LOCALE"), @ViewDebug.IntToString(from=6, to="FIRST_STRONG_LTR"), @ViewDebug.IntToString(from=7, to="FIRST_STRONG_RTL")})
  public int getRawTextDirection()
  {
    return (mPrivateFlags2 & 0x1C0) >> 6;
  }
  
  public Resources getResources()
  {
    return mResources;
  }
  
  public final boolean getRevealOnFocusHint()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x4000000) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.CapturedViewProperty
  public final int getRight()
  {
    return mRight;
  }
  
  protected float getRightFadingEdgeStrength()
  {
    float f;
    if (computeHorizontalScrollOffset() + computeHorizontalScrollExtent() < computeHorizontalScrollRange()) {
      f = 1.0F;
    } else {
      f = 0.0F;
    }
    return f;
  }
  
  protected int getRightPaddingOffset()
  {
    return 0;
  }
  
  public View getRootView()
  {
    if (mAttachInfo != null)
    {
      localView = mAttachInfo.mRootView;
      if (localView != null) {
        return localView;
      }
    }
    for (View localView = this; (mParent != null) && ((mParent instanceof View)); localView = (View)mParent) {}
    return localView;
  }
  
  public WindowInsets getRootWindowInsets()
  {
    if (mAttachInfo != null) {
      return mAttachInfo.mViewRootImpl.getWindowInsets(false);
    }
    return null;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getRotation()
  {
    return mRenderNode.getRotation();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getRotationX()
  {
    return mRenderNode.getRotationX();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getRotationY()
  {
    return mRenderNode.getRotationY();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getScaleX()
  {
    return mRenderNode.getScaleX();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getScaleY()
  {
    return mRenderNode.getScaleY();
  }
  
  public int getScrollBarDefaultDelayBeforeFade()
  {
    int i;
    if (mScrollCache == null) {
      i = ViewConfiguration.getScrollDefaultDelay();
    } else {
      i = mScrollCache.scrollBarDefaultDelayBeforeFade;
    }
    return i;
  }
  
  public int getScrollBarFadeDuration()
  {
    int i;
    if (mScrollCache == null) {
      i = ViewConfiguration.getScrollBarFadeDuration();
    } else {
      i = mScrollCache.scrollBarFadeDuration;
    }
    return i;
  }
  
  public int getScrollBarSize()
  {
    int i;
    if (mScrollCache == null) {
      i = ViewConfiguration.get(mContext).getScaledScrollBarSize();
    } else {
      i = mScrollCache.scrollBarSize;
    }
    return i;
  }
  
  @ViewDebug.ExportedProperty(mapping={@ViewDebug.IntToString(from=0, to="INSIDE_OVERLAY"), @ViewDebug.IntToString(from=16777216, to="INSIDE_INSET"), @ViewDebug.IntToString(from=33554432, to="OUTSIDE_OVERLAY"), @ViewDebug.IntToString(from=50331648, to="OUTSIDE_INSET")})
  public int getScrollBarStyle()
  {
    return mViewFlags & 0x3000000;
  }
  
  void getScrollIndicatorBounds(Rect paramRect)
  {
    left = mScrollX;
    right = (mScrollX + mRight - mLeft);
    top = mScrollY;
    bottom = (mScrollY + mBottom - mTop);
  }
  
  public int getScrollIndicators()
  {
    return (mPrivateFlags3 & 0x3F00) >>> 8;
  }
  
  public final int getScrollX()
  {
    return mScrollX;
  }
  
  public final int getScrollY()
  {
    return mScrollY;
  }
  
  View getSelfOrParentImportantForA11y()
  {
    if (isImportantForAccessibility()) {
      return this;
    }
    ViewParent localViewParent = getParentForAccessibility();
    if ((localViewParent instanceof View)) {
      return (View)localViewParent;
    }
    return null;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public int getSolidColor()
  {
    return 0;
  }
  
  public StateListAnimator getStateListAnimator()
  {
    return mStateListAnimator;
  }
  
  protected int getSuggestedMinimumHeight()
  {
    int i;
    if (mBackground == null) {
      i = mMinHeight;
    } else {
      i = Math.max(mMinHeight, mBackground.getMinimumHeight());
    }
    return i;
  }
  
  protected int getSuggestedMinimumWidth()
  {
    int i;
    if (mBackground == null) {
      i = mMinWidth;
    } else {
      i = Math.max(mMinWidth, mBackground.getMinimumWidth());
    }
    return i;
  }
  
  public int getSystemUiVisibility()
  {
    return mSystemUiVisibility;
  }
  
  @ViewDebug.ExportedProperty
  public Object getTag()
  {
    return mTag;
  }
  
  public Object getTag(int paramInt)
  {
    if (mKeyedTags != null) {
      return mKeyedTags.get(paramInt);
    }
    return null;
  }
  
  @ViewDebug.ExportedProperty(category="text", mapping={@ViewDebug.IntToString(from=0, to="INHERIT"), @ViewDebug.IntToString(from=1, to="GRAVITY"), @ViewDebug.IntToString(from=2, to="TEXT_START"), @ViewDebug.IntToString(from=3, to="TEXT_END"), @ViewDebug.IntToString(from=4, to="CENTER"), @ViewDebug.IntToString(from=5, to="VIEW_START"), @ViewDebug.IntToString(from=6, to="VIEW_END")})
  public int getTextAlignment()
  {
    return (mPrivateFlags2 & 0xE0000) >> 17;
  }
  
  @ViewDebug.ExportedProperty(category="text", mapping={@ViewDebug.IntToString(from=0, to="INHERIT"), @ViewDebug.IntToString(from=1, to="FIRST_STRONG"), @ViewDebug.IntToString(from=2, to="ANY_RTL"), @ViewDebug.IntToString(from=3, to="LTR"), @ViewDebug.IntToString(from=4, to="RTL"), @ViewDebug.IntToString(from=5, to="LOCALE"), @ViewDebug.IntToString(from=6, to="FIRST_STRONG_LTR"), @ViewDebug.IntToString(from=7, to="FIRST_STRONG_RTL")})
  public int getTextDirection()
  {
    return (mPrivateFlags2 & 0x1C00) >> 10;
  }
  
  public ThreadedRenderer getThreadedRenderer()
  {
    ThreadedRenderer localThreadedRenderer;
    if (mAttachInfo != null) {
      localThreadedRenderer = mAttachInfo.mThreadedRenderer;
    } else {
      localThreadedRenderer = null;
    }
    return localThreadedRenderer;
  }
  
  public CharSequence getTooltip()
  {
    return getTooltipText();
  }
  
  public CharSequence getTooltipText()
  {
    CharSequence localCharSequence;
    if (mTooltipInfo != null) {
      localCharSequence = mTooltipInfo.mTooltipText;
    } else {
      localCharSequence = null;
    }
    return localCharSequence;
  }
  
  public View getTooltipView()
  {
    if ((mTooltipInfo != null) && (mTooltipInfo.mTooltipPopup != null)) {
      return mTooltipInfo.mTooltipPopup.getContentView();
    }
    return null;
  }
  
  @ViewDebug.CapturedViewProperty
  public final int getTop()
  {
    return mTop;
  }
  
  protected float getTopFadingEdgeStrength()
  {
    float f;
    if (computeVerticalScrollOffset() > 0) {
      f = 1.0F;
    } else {
      f = 0.0F;
    }
    return f;
  }
  
  protected int getTopPaddingOffset()
  {
    return 0;
  }
  
  public TouchDelegate getTouchDelegate()
  {
    return mTouchDelegate;
  }
  
  public ArrayList<View> getTouchables()
  {
    ArrayList localArrayList = new ArrayList();
    addTouchables(localArrayList);
    return localArrayList;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getTransitionAlpha()
  {
    float f;
    if (mTransformationInfo != null) {
      f = mTransformationInfo.mTransitionAlpha;
    } else {
      f = 1.0F;
    }
    return f;
  }
  
  @ViewDebug.ExportedProperty
  public String getTransitionName()
  {
    return mTransitionName;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getTranslationX()
  {
    return mRenderNode.getTranslationX();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getTranslationY()
  {
    return mRenderNode.getTranslationY();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getTranslationZ()
  {
    return mRenderNode.getTranslationZ();
  }
  
  public int getVerticalFadingEdgeLength()
  {
    if (isVerticalFadingEdgeEnabled())
    {
      ScrollabilityCache localScrollabilityCache = mScrollCache;
      if (localScrollabilityCache != null) {
        return fadingEdgeLength;
      }
    }
    return 0;
  }
  
  protected float getVerticalScrollFactor()
  {
    if (mVerticalScrollFactor == 0.0F)
    {
      TypedValue localTypedValue = new TypedValue();
      if (mContext.getTheme().resolveAttribute(16842829, localTypedValue, true)) {
        mVerticalScrollFactor = localTypedValue.getDimension(mContext.getResources().getDisplayMetrics());
      } else {
        throw new IllegalStateException("Expected theme to define listPreferredItemHeight.");
      }
    }
    return mVerticalScrollFactor;
  }
  
  public int getVerticalScrollbarPosition()
  {
    return mVerticalScrollbarPosition;
  }
  
  public int getVerticalScrollbarWidth()
  {
    ScrollabilityCache localScrollabilityCache = mScrollCache;
    if (localScrollabilityCache != null)
    {
      ScrollBarDrawable localScrollBarDrawable = scrollBar;
      if (localScrollBarDrawable != null)
      {
        int i = localScrollBarDrawable.getSize(true);
        int j = i;
        if (i <= 0) {
          j = scrollBarSize;
        }
        return j;
      }
      return 0;
    }
    return 0;
  }
  
  public ViewRootImpl getViewRootImpl()
  {
    if (mAttachInfo != null) {
      return mAttachInfo.mViewRootImpl;
    }
    return null;
  }
  
  public ViewTreeObserver getViewTreeObserver()
  {
    if (mAttachInfo != null) {
      return mAttachInfo.mTreeObserver;
    }
    if (mFloatingTreeObserver == null) {
      mFloatingTreeObserver = new ViewTreeObserver(mContext);
    }
    return mFloatingTreeObserver;
  }
  
  @ViewDebug.ExportedProperty(mapping={@ViewDebug.IntToString(from=0, to="VISIBLE"), @ViewDebug.IntToString(from=4, to="INVISIBLE"), @ViewDebug.IntToString(from=8, to="GONE")})
  public int getVisibility()
  {
    return mViewFlags & 0xC;
  }
  
  @ViewDebug.ExportedProperty(category="layout")
  public final int getWidth()
  {
    return mRight - mLeft;
  }
  
  protected IWindow getWindow()
  {
    IWindow localIWindow;
    if (mAttachInfo != null) {
      localIWindow = mAttachInfo.mWindow;
    } else {
      localIWindow = null;
    }
    return localIWindow;
  }
  
  protected int getWindowAttachCount()
  {
    return mWindowAttachCount;
  }
  
  public void getWindowDisplayFrame(Rect paramRect)
  {
    if (mAttachInfo != null) {
      try
      {
        mAttachInfo.mSession.getDisplayFrame(mAttachInfo.mWindow, paramRect);
        return;
      }
      catch (RemoteException paramRect)
      {
        return;
      }
    }
    DisplayManagerGlobal.getInstance().getRealDisplay(0).getRectSize(paramRect);
  }
  
  public WindowId getWindowId()
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo == null) {
      return null;
    }
    if (mWindowId == null) {
      try
      {
        mIWindowId = mSession.getWindowId(mWindowToken);
        if (mIWindowId != null)
        {
          WindowId localWindowId = new android/view/WindowId;
          localWindowId.<init>(mIWindowId);
          mWindowId = localWindowId;
        }
      }
      catch (RemoteException localRemoteException) {}
    }
    return mWindowId;
  }
  
  IWindowSession getWindowSession()
  {
    IWindowSession localIWindowSession;
    if (mAttachInfo != null) {
      localIWindowSession = mAttachInfo.mSession;
    } else {
      localIWindowSession = null;
    }
    return localIWindowSession;
  }
  
  public int getWindowSystemUiVisibility()
  {
    int i;
    if (mAttachInfo != null) {
      i = mAttachInfo.mSystemUiVisibility;
    } else {
      i = 0;
    }
    return i;
  }
  
  public IBinder getWindowToken()
  {
    IBinder localIBinder;
    if (mAttachInfo != null) {
      localIBinder = mAttachInfo.mWindowToken;
    } else {
      localIBinder = null;
    }
    return localIBinder;
  }
  
  public int getWindowVisibility()
  {
    int i;
    if (mAttachInfo != null) {
      i = mAttachInfo.mWindowVisibility;
    } else {
      i = 8;
    }
    return i;
  }
  
  public void getWindowVisibleDisplayFrame(Rect paramRect)
  {
    if (mAttachInfo != null) {
      try
      {
        mAttachInfo.mSession.getDisplayFrame(mAttachInfo.mWindow, paramRect);
        Rect localRect = mAttachInfo.mVisibleInsets;
        left += left;
        top += top;
        right -= right;
        bottom -= bottom;
        return;
      }
      catch (RemoteException paramRect)
      {
        return;
      }
    }
    DisplayManagerGlobal.getInstance().getRealDisplay(0).getRectSize(paramRect);
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getX()
  {
    return mLeft + getTranslationX();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getY()
  {
    return mTop + getTranslationY();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public float getZ()
  {
    return getElevation() + getTranslationZ();
  }
  
  void handleFocusGainInternal(int paramInt, Rect paramRect)
  {
    if ((mPrivateFlags & 0x2) == 0)
    {
      mPrivateFlags |= 0x2;
      View localView;
      if (mAttachInfo != null) {
        localView = getRootView().findFocus();
      } else {
        localView = null;
      }
      if (mParent != null)
      {
        mParent.requestChildFocus(this, this);
        updateFocusedInCluster(localView, paramInt);
      }
      if (mAttachInfo != null) {
        mAttachInfo.mTreeObserver.dispatchOnGlobalFocusChange(localView, this);
      }
      onFocusChanged(true, paramInt, paramRect);
      refreshDrawableState();
    }
  }
  
  protected boolean handleScrollBarDragging(MotionEvent paramMotionEvent)
  {
    if (mScrollCache == null) {
      return false;
    }
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    int i = paramMotionEvent.getAction();
    if (((mScrollCache.mScrollBarDraggingState != 0) || (i == 0)) && (paramMotionEvent.isFromSource(8194)) && (paramMotionEvent.isButtonPressed(1)))
    {
      if (i != 0)
      {
        if (i != 2) {
          break label547;
        }
        if (mScrollCache.mScrollBarDraggingState == 0) {
          return false;
        }
        int j;
        int k;
        int m;
        float f3;
        if (mScrollCache.mScrollBarDraggingState == 1)
        {
          paramMotionEvent = mScrollCache.mScrollBarBounds;
          getVerticalScrollBarBounds(paramMotionEvent, null);
          j = computeVerticalScrollRange();
          k = computeVerticalScrollOffset();
          i = computeVerticalScrollExtent();
          m = ScrollBarUtils.getThumbLength(paramMotionEvent.height(), paramMotionEvent.width(), i, j);
          k = ScrollBarUtils.getThumbOffset(paramMotionEvent.height(), m, i, j, k);
          f3 = mScrollCache.mScrollBarDraggingPos;
          f1 = paramMotionEvent.height() - m;
          f3 = Math.min(Math.max(k + (f2 - f3), 0.0F), f1);
          m = getHeight();
          if ((Math.round(f3) != k) && (f1 > 0.0F) && (m > 0) && (i > 0))
          {
            i = Math.round((j - i) / (i / m) * (f3 / f1));
            if (i != getScrollY())
            {
              mScrollCache.mScrollBarDraggingPos = f2;
              setScrollY(i);
            }
          }
          return true;
        }
        if (mScrollCache.mScrollBarDraggingState == 2)
        {
          paramMotionEvent = mScrollCache.mScrollBarBounds;
          getHorizontalScrollBarBounds(paramMotionEvent, null);
          i = computeHorizontalScrollRange();
          k = computeHorizontalScrollOffset();
          j = computeHorizontalScrollExtent();
          m = ScrollBarUtils.getThumbLength(paramMotionEvent.width(), paramMotionEvent.height(), j, i);
          k = ScrollBarUtils.getThumbOffset(paramMotionEvent.width(), m, j, i, k);
          f3 = mScrollCache.mScrollBarDraggingPos;
          f2 = paramMotionEvent.width() - m;
          f3 = Math.min(Math.max(k + (f1 - f3), 0.0F), f2);
          m = getWidth();
          if ((Math.round(f3) != k) && (f2 > 0.0F) && (m > 0) && (j > 0))
          {
            i = Math.round((i - j) / (j / m) * (f3 / f2));
            if (i != getScrollX())
            {
              mScrollCache.mScrollBarDraggingPos = f1;
              setScrollX(i);
            }
          }
          return true;
        }
      }
      if (mScrollCache.state == 0) {
        return false;
      }
      if (isOnVerticalScrollbarThumb(f1, f2))
      {
        mScrollCache.mScrollBarDraggingState = 1;
        mScrollCache.mScrollBarDraggingPos = f2;
        return true;
      }
      if (isOnHorizontalScrollbarThumb(f1, f2))
      {
        mScrollCache.mScrollBarDraggingState = 2;
        mScrollCache.mScrollBarDraggingPos = f1;
        return true;
      }
      label547:
      mScrollCache.mScrollBarDraggingState = 0;
      return false;
    }
    mScrollCache.mScrollBarDraggingState = 0;
    return false;
  }
  
  void handleTooltipKey(KeyEvent paramKeyEvent)
  {
    switch (paramKeyEvent.getAction())
    {
    default: 
      break;
    case 1: 
      handleTooltipUp();
      break;
    case 0: 
      if (paramKeyEvent.getRepeatCount() == 0) {
        hideTooltip();
      }
      break;
    }
  }
  
  boolean hasDefaultFocus()
  {
    return isFocusedByDefault();
  }
  
  public boolean hasExplicitFocusable()
  {
    return hasFocusable(false, true);
  }
  
  @ViewDebug.ExportedProperty(category="focus")
  public boolean hasFocus()
  {
    boolean bool;
    if ((mPrivateFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasFocusable()
  {
    return hasFocusable(sHasFocusableExcludeAutoFocusable ^ true, false);
  }
  
  boolean hasFocusable(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (!isFocusableInTouchMode()) {
      for (ViewParent localViewParent = mParent; (localViewParent instanceof ViewGroup); localViewParent = localViewParent.getParent()) {
        if (((ViewGroup)localViewParent).shouldBlockFocusForTouchscreen()) {
          return false;
        }
      }
    }
    if (((mViewFlags & 0xC) == 0) && ((mViewFlags & 0x20) == 0)) {
      return ((paramBoolean1) || (getFocusable() != 16)) && (isFocusable());
    }
    return false;
  }
  
  protected boolean hasHoveredChild()
  {
    return false;
  }
  
  final boolean hasIdentityMatrix()
  {
    return mRenderNode.hasIdentityMatrix();
  }
  
  public boolean hasNestedScrollingParent()
  {
    boolean bool;
    if (mNestedScrollingParent != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasOnClickListeners()
  {
    ListenerInfo localListenerInfo = mListenerInfo;
    boolean bool;
    if ((localListenerInfo != null) && (mOnClickListener != null)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean hasOpaqueScrollbars()
  {
    boolean bool;
    if ((mPrivateFlags & 0x1000000) == 16777216) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public boolean hasOverlappingRendering()
  {
    return true;
  }
  
  public boolean hasPointerCapture()
  {
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    if (localViewRootImpl == null) {
      return false;
    }
    return localViewRootImpl.hasPointerCapture();
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public boolean hasShadow()
  {
    return mRenderNode.hasShadow();
  }
  
  @ViewDebug.ExportedProperty(category="layout")
  public boolean hasTransientState()
  {
    boolean bool;
    if ((mPrivateFlags2 & 0x80000000) == Integer.MIN_VALUE) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean hasUnhandledKeyListener()
  {
    boolean bool;
    if ((mListenerInfo != null) && (mListenerInfo.mUnhandledKeyListeners != null) && (!mListenerInfo.mUnhandledKeyListeners.isEmpty())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean hasWindowFocus()
  {
    boolean bool;
    if ((mAttachInfo != null) && (mAttachInfo.mHasWindowFocus)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  void hideTooltip()
  {
    if (mTooltipInfo == null) {
      return;
    }
    removeCallbacks(mTooltipInfo.mShowTooltipRunnable);
    if (mTooltipInfo.mTooltipPopup == null) {
      return;
    }
    mTooltipInfo.mTooltipPopup.hide();
    mTooltipInfo.mTooltipPopup = null;
    mTooltipInfo.mTooltipFromLongClick = false;
    mTooltipInfo.clearAnchorPos();
    if (mAttachInfo != null) {
      mAttachInfo.mTooltipHost = null;
    }
    notifyViewAccessibilityStateChangedIfNeeded(0);
  }
  
  public boolean includeForAccessibility()
  {
    AttachInfo localAttachInfo = mAttachInfo;
    boolean bool = false;
    if (localAttachInfo != null)
    {
      if (((mAttachInfo.mAccessibilityFetchFlags & 0x8) == 0) && (!isImportantForAccessibility())) {
        break label39;
      }
      bool = true;
      label39:
      return bool;
    }
    return false;
  }
  
  protected void initializeFadingEdge(TypedArray paramTypedArray)
  {
    paramTypedArray = mContext.obtainStyledAttributes(R.styleable.View);
    initializeFadingEdgeInternal(paramTypedArray);
    paramTypedArray.recycle();
  }
  
  protected void initializeFadingEdgeInternal(TypedArray paramTypedArray)
  {
    initScrollCache();
    mScrollCache.fadingEdgeLength = paramTypedArray.getDimensionPixelSize(25, ViewConfiguration.get(mContext).getScaledFadingEdgeLength());
  }
  
  protected void initializeScrollbars(TypedArray paramTypedArray)
  {
    paramTypedArray = mContext.obtainStyledAttributes(R.styleable.View);
    initializeScrollbarsInternal(paramTypedArray);
    paramTypedArray.recycle();
  }
  
  protected void initializeScrollbarsInternal(TypedArray paramTypedArray)
  {
    initScrollCache();
    ScrollabilityCache localScrollabilityCache = mScrollCache;
    if (scrollBar == null)
    {
      scrollBar = new ScrollBarDrawable();
      scrollBar.setState(getDrawableState());
      scrollBar.setCallback(this);
    }
    boolean bool = paramTypedArray.getBoolean(47, true);
    if (!bool) {
      state = 1;
    }
    fadeScrollBars = bool;
    scrollBarFadeDuration = paramTypedArray.getInt(45, ViewConfiguration.getScrollBarFadeDuration());
    scrollBarDefaultDelayBeforeFade = paramTypedArray.getInt(46, ViewConfiguration.getScrollDefaultDelay());
    scrollBarSize = paramTypedArray.getDimensionPixelSize(1, ViewConfiguration.get(mContext).getScaledScrollBarSize());
    Drawable localDrawable1 = paramTypedArray.getDrawable(4);
    scrollBar.setHorizontalTrackDrawable(localDrawable1);
    localDrawable1 = paramTypedArray.getDrawable(2);
    if (localDrawable1 != null) {
      scrollBar.setHorizontalThumbDrawable(localDrawable1);
    }
    if (paramTypedArray.getBoolean(6, false)) {
      scrollBar.setAlwaysDrawHorizontalTrack(true);
    }
    localDrawable1 = paramTypedArray.getDrawable(5);
    scrollBar.setVerticalTrackDrawable(localDrawable1);
    Drawable localDrawable2 = paramTypedArray.getDrawable(3);
    if (localDrawable2 != null) {
      scrollBar.setVerticalThumbDrawable(localDrawable2);
    }
    if (paramTypedArray.getBoolean(7, false)) {
      scrollBar.setAlwaysDrawVerticalTrack(true);
    }
    int i = getLayoutDirection();
    if (localDrawable1 != null) {
      localDrawable1.setLayoutDirection(i);
    }
    if (localDrawable2 != null) {
      localDrawable2.setLayoutDirection(i);
    }
    resolvePadding();
  }
  
  protected void internalSetPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    mUserPaddingLeft = paramInt1;
    mUserPaddingRight = paramInt3;
    mUserPaddingBottom = paramInt4;
    int i = mViewFlags;
    int j = 0;
    int k = paramInt1;
    int m = paramInt3;
    int n = paramInt4;
    if ((i & 0x300) != 0)
    {
      int i1 = 0;
      int i2 = paramInt1;
      int i3 = paramInt3;
      if ((i & 0x200) != 0)
      {
        if ((i & 0x1000000) == 0) {
          i2 = 0;
        } else {
          i2 = getVerticalScrollbarWidth();
        }
        switch (mVerticalScrollbarPosition)
        {
        default: 
          i2 = paramInt1;
          i3 = paramInt3;
          break;
        case 2: 
          i3 = paramInt3 + i2;
          i2 = paramInt1;
          break;
        case 1: 
          i2 = paramInt1 + i2;
          i3 = paramInt3;
          break;
        case 0: 
          if (isLayoutRtl())
          {
            i2 = paramInt1 + i2;
            i3 = paramInt3;
          }
          else
          {
            i3 = paramInt3 + i2;
            i2 = paramInt1;
          }
          break;
        }
      }
      k = i2;
      m = i3;
      n = paramInt4;
      if ((i & 0x100) != 0)
      {
        if ((i & 0x1000000) == 0) {
          paramInt1 = i1;
        } else {
          paramInt1 = getHorizontalScrollbarHeight();
        }
        n = paramInt4 + paramInt1;
        m = i3;
        k = i2;
      }
    }
    paramInt1 = j;
    if (mPaddingLeft != k)
    {
      paramInt1 = 1;
      mPaddingLeft = k;
    }
    if (mPaddingTop != paramInt2)
    {
      paramInt1 = 1;
      mPaddingTop = paramInt2;
    }
    if (mPaddingRight != m)
    {
      paramInt1 = 1;
      mPaddingRight = m;
    }
    if (mPaddingBottom != n)
    {
      paramInt1 = 1;
      mPaddingBottom = n;
    }
    if (paramInt1 != 0)
    {
      requestLayout();
      invalidateOutline();
    }
  }
  
  public void invalidate()
  {
    invalidate(true);
  }
  
  @Deprecated
  public void invalidate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = mScrollX;
    int j = mScrollY;
    invalidateInternal(paramInt1 - i, paramInt2 - j, paramInt3 - i, paramInt4 - j, true, false);
  }
  
  @Deprecated
  public void invalidate(Rect paramRect)
  {
    int i = mScrollX;
    int j = mScrollY;
    invalidateInternal(left - i, top - j, right - i, bottom - j, true, false);
  }
  
  public void invalidate(boolean paramBoolean)
  {
    invalidateInternal(0, 0, mRight - mLeft, mBottom - mTop, paramBoolean, true);
  }
  
  public void invalidateDrawable(Drawable paramDrawable)
  {
    if (verifyDrawable(paramDrawable))
    {
      paramDrawable = paramDrawable.getDirtyBounds();
      int i = mScrollX;
      int j = mScrollY;
      invalidate(left + i, top + j, right + i, bottom + j);
      rebuildOutline();
    }
  }
  
  void invalidateInheritedLayoutMode(int paramInt) {}
  
  void invalidateInternal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mGhostView != null)
    {
      mGhostView.invalidate(true);
      return;
    }
    if (skipInvalidate()) {
      return;
    }
    if (((mPrivateFlags & 0x30) == 48) || ((paramBoolean1) && ((mPrivateFlags & 0x8000) == 32768)) || ((mPrivateFlags & 0x80000000) != Integer.MIN_VALUE) || ((paramBoolean2) && (isOpaque() != mLastIsOpaque)))
    {
      if (paramBoolean2)
      {
        mLastIsOpaque = isOpaque();
        mPrivateFlags &= 0xFFFFFFDF;
      }
      mPrivateFlags |= 0x200000;
      if (paramBoolean1)
      {
        mPrivateFlags |= 0x80000000;
        mPrivateFlags &= 0xFFFF7FFF;
      }
      Object localObject1 = mAttachInfo;
      Object localObject2 = mParent;
      if ((localObject2 != null) && (localObject1 != null) && (paramInt1 < paramInt3) && (paramInt2 < paramInt4))
      {
        localObject1 = mTmpInvalRect;
        ((Rect)localObject1).set(paramInt1, paramInt2, paramInt3, paramInt4);
        ((ViewParent)localObject2).invalidateChild(this, (Rect)localObject1);
      }
      if ((mBackground != null) && (mBackground.isProjected()))
      {
        localObject2 = getProjectionReceiver();
        if (localObject2 != null) {
          ((View)localObject2).damageInParent();
        }
      }
    }
  }
  
  public void invalidateOutline()
  {
    rebuildOutline();
    notifySubtreeAccessibilityStateChangedIfNeeded();
    invalidateViewProperty(false, false);
  }
  
  protected void invalidateParentCaches()
  {
    if ((mParent instanceof View))
    {
      View localView = (View)mParent;
      mPrivateFlags |= 0x80000000;
    }
  }
  
  protected void invalidateParentIfNeeded()
  {
    if ((isHardwareAccelerated()) && ((mParent instanceof View))) {
      ((View)mParent).invalidate(true);
    }
  }
  
  protected void invalidateParentIfNeededAndWasQuickRejected()
  {
    if ((mPrivateFlags2 & 0x10000000) != 0) {
      invalidateParentIfNeeded();
    }
  }
  
  void invalidateViewProperty(boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((isHardwareAccelerated()) && (mRenderNode.isValid()) && ((mPrivateFlags & 0x40) == 0))
    {
      damageInParent();
    }
    else
    {
      if (paramBoolean1) {
        invalidateParentCaches();
      }
      if (paramBoolean2) {
        mPrivateFlags |= 0x20;
      }
      invalidate(false);
    }
  }
  
  public boolean isAccessibilityFocused()
  {
    boolean bool;
    if ((mPrivateFlags2 & 0x4000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isAccessibilityFocusedViewOrHost()
  {
    boolean bool;
    if ((!isAccessibilityFocused()) && ((getViewRootImpl() == null) || (getViewRootImpl().getAccessibilityFocusedHost() != this))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isAccessibilityHeading()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x80000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAccessibilitySelectionExtendable()
  {
    return false;
  }
  
  public boolean isActionableForAccessibility()
  {
    boolean bool;
    if ((!isClickable()) && (!isLongClickable()) && (!isFocusable())) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isActivated()
  {
    boolean bool;
    if ((mPrivateFlags & 0x40000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAssistBlocked()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x4000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAttachedToWindow()
  {
    boolean bool;
    if (mAttachInfo != null) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isAutofilled()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x10000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isClickable()
  {
    boolean bool;
    if ((mViewFlags & 0x4000) == 16384) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isContextClickable()
  {
    boolean bool;
    if ((mViewFlags & 0x800000) == 8388608) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDefaultFocusHighlightNeeded(Drawable paramDrawable1, Drawable paramDrawable2)
  {
    boolean bool = true;
    int i;
    if (((paramDrawable1 != null) && (paramDrawable1.isStateful()) && (paramDrawable1.hasFocusStateSpecified())) || ((paramDrawable2 != null) && (paramDrawable2.isStateful()) && (paramDrawable2.hasFocusStateSpecified()))) {
      i = 0;
    } else {
      i = 1;
    }
    if ((isInTouchMode()) || (!getDefaultFocusHighlightEnabled()) || (i == 0) || (!isAttachedToWindow()) || (!sUseDefaultFocusHighlight)) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDirty()
  {
    boolean bool;
    if ((mPrivateFlags & 0x600000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isDraggingScrollBar()
  {
    boolean bool;
    if ((mScrollCache != null) && (mScrollCache.mScrollBarDraggingState != 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  @Deprecated
  public boolean isDrawingCacheEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x8000) == 32768) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isDuplicateParentStateEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x400000) == 4194304) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x20) == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="focus")
  public final boolean isFocusable()
  {
    int i = mViewFlags;
    boolean bool = true;
    if (1 != (i & 0x1)) {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="focus")
  public final boolean isFocusableInTouchMode()
  {
    boolean bool;
    if (262144 == (mViewFlags & 0x40000)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="focus")
  public boolean isFocused()
  {
    boolean bool;
    if ((mPrivateFlags & 0x2) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="focus")
  public final boolean isFocusedByDefault()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x40000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isForegroundInsidePadding()
  {
    boolean bool;
    if (mForegroundInfo != null) {
      bool = mForegroundInfo.mInsidePadding;
    } else {
      bool = true;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isHapticFeedbackEnabled()
  {
    boolean bool;
    if (268435456 == (mViewFlags & 0x10000000)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public boolean isHardwareAccelerated()
  {
    boolean bool;
    if ((mAttachInfo != null) && (mAttachInfo.mHardwareAccelerated)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isHorizontalFadingEdgeEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x1000) == 4096) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isHorizontalScrollBarEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x100) == 256) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isHovered()
  {
    boolean bool;
    if ((mPrivateFlags & 0x10000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isImportantForAccessibility()
  {
    int i = (mPrivateFlags2 & 0x700000) >> 20;
    boolean bool = false;
    if ((i != 2) && (i != 4))
    {
      for (ViewParent localViewParent = mParent; (localViewParent instanceof View); localViewParent = localViewParent.getParent()) {
        if (((View)localViewParent).getImportantForAccessibility() == 4) {
          return false;
        }
      }
      if ((i != 1) && (!isActionableForAccessibility()) && (!hasListenersForAccessibility()) && (getAccessibilityNodeProvider() == null) && (getAccessibilityLiveRegion() == 0) && (!isAccessibilityPane())) {
        break label110;
      }
      bool = true;
      label110:
      return bool;
    }
    return false;
  }
  
  public final boolean isImportantForAutofill()
  {
    Object localObject1 = mParent;
    while ((localObject1 instanceof View))
    {
      i = ((View)localObject1).getImportantForAutofill();
      if ((i != 8) && (i != 4)) {
        localObject1 = ((ViewParent)localObject1).getParent();
      } else {
        return false;
      }
    }
    int i = getImportantForAutofill();
    if ((i != 4) && (i != 1))
    {
      if ((i != 8) && (i != 2))
      {
        i = mID;
        if ((i != -1) && (!isViewIdGenerated(i)))
        {
          Object localObject2 = getResources();
          localObject1 = null;
          Object localObject3 = null;
          Object localObject5;
          try
          {
            Object localObject4 = ((Resources)localObject2).getResourceEntryName(i);
            localObject1 = localObject4;
            localObject2 = ((Resources)localObject2).getResourcePackageName(i);
            localObject1 = localObject2;
            localObject3 = localObject4;
            localObject4 = localObject1;
          }
          catch (Resources.NotFoundException localNotFoundException)
          {
            localObject5 = localObject3;
            localObject3 = localObject1;
          }
          if ((localObject3 != null) && (localObject5 != null) && (localObject5.equals(mContext.getPackageName()))) {
            return true;
          }
        }
        return getAutofillHints() != null;
      }
      return false;
    }
    return true;
  }
  
  public boolean isInEditMode()
  {
    return false;
  }
  
  public boolean isInLayout()
  {
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    boolean bool;
    if ((localViewRootImpl != null) && (localViewRootImpl.isInLayout())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isInScrollingContainer()
  {
    for (ViewParent localViewParent = getParent(); (localViewParent != null) && ((localViewParent instanceof ViewGroup)); localViewParent = localViewParent.getParent()) {
      if (((ViewGroup)localViewParent).shouldDelayChildPressedState()) {
        return true;
      }
    }
    return false;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isInTouchMode()
  {
    if (mAttachInfo != null) {
      return mAttachInfo.mInTouchMode;
    }
    return ViewRootImpl.isInTouchMode();
  }
  
  @ViewDebug.ExportedProperty(category="focus")
  public final boolean isKeyboardNavigationCluster()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x8000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLaidOut()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x4) == 4) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLayoutDirectionInherited()
  {
    boolean bool;
    if (getRawLayoutDirection() == 2) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLayoutDirectionResolved()
  {
    boolean bool;
    if ((mPrivateFlags2 & 0x20) == 32) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLayoutRequested()
  {
    boolean bool;
    if ((mPrivateFlags & 0x1000) == 4096) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="layout")
  public boolean isLayoutRtl()
  {
    int i = getLayoutDirection();
    boolean bool = true;
    if (i != 1) {
      bool = false;
    }
    return bool;
  }
  
  boolean isLayoutValid()
  {
    boolean bool;
    if ((isLaidOut()) && ((mPrivateFlags & 0x1000) == 0)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isLongClickable()
  {
    boolean bool;
    if ((mViewFlags & 0x200000) == 2097152) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isNestedScrollingEnabled()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x80) == 128) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  boolean isOnScrollbar(float paramFloat1, float paramFloat2)
  {
    if (mScrollCache == null) {
      return false;
    }
    paramFloat1 += getScrollX();
    paramFloat2 += getScrollY();
    Rect localRect;
    if ((isVerticalScrollBarEnabled()) && (!isVerticalScrollBarHidden()))
    {
      localRect = mScrollCache.mScrollBarTouchBounds;
      getVerticalScrollBarBounds(null, localRect);
      if (localRect.contains((int)paramFloat1, (int)paramFloat2)) {
        return true;
      }
    }
    if (isHorizontalScrollBarEnabled())
    {
      localRect = mScrollCache.mScrollBarTouchBounds;
      getHorizontalScrollBarBounds(null, localRect);
      if (localRect.contains((int)paramFloat1, (int)paramFloat2)) {
        return true;
      }
    }
    return false;
  }
  
  boolean isOnScrollbarThumb(float paramFloat1, float paramFloat2)
  {
    boolean bool;
    if ((!isOnVerticalScrollbarThumb(paramFloat1, paramFloat2)) && (!isOnHorizontalScrollbarThumb(paramFloat1, paramFloat2))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public boolean isOpaque()
  {
    boolean bool;
    if (((mPrivateFlags & 0x1800000) == 25165824) && (getFinalAlpha() >= 1.0F)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean isPaddingOffsetRequired()
  {
    return false;
  }
  
  public boolean isPaddingRelative()
  {
    boolean bool;
    if ((mUserPaddingStart == Integer.MIN_VALUE) && (mUserPaddingEnd == Integer.MIN_VALUE)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  boolean isPaddingResolved()
  {
    boolean bool;
    if ((mPrivateFlags2 & 0x20000000) == 536870912) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isPivotSet()
  {
    return mRenderNode.isPivotExplicitlySet();
  }
  
  @ViewDebug.ExportedProperty
  public boolean isPressed()
  {
    boolean bool;
    if ((mPrivateFlags & 0x4000) == 16384) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isRootNamespace()
  {
    boolean bool;
    if ((mPrivateFlags & 0x8) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSaveEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x10000) != 65536) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isSaveFromParentEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x20000000) != 536870912) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isScreenReaderFocusable()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x10000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isScrollContainer()
  {
    boolean bool;
    if ((mPrivateFlags & 0x100000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isScrollbarFadingEnabled()
  {
    boolean bool;
    if ((mScrollCache != null) && (mScrollCache.fadeScrollBars)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isSelected()
  {
    boolean bool;
    if ((mPrivateFlags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isShown()
  {
    Object localObject = this;
    View localView;
    do
    {
      if ((mViewFlags & 0xC) != 0) {
        return false;
      }
      localObject = mParent;
      if (localObject == null) {
        return false;
      }
      if (!(localObject instanceof View)) {
        return true;
      }
      localView = (View)localObject;
      localObject = localView;
    } while (localView != null);
    return false;
  }
  
  @ViewDebug.ExportedProperty
  public boolean isSoundEffectsEnabled()
  {
    boolean bool;
    if (134217728 == (mViewFlags & 0x8000000)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public final boolean isTemporarilyDetached()
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x2000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTextAlignmentInherited()
  {
    boolean bool;
    if (getRawTextAlignment() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTextAlignmentResolved()
  {
    boolean bool;
    if ((mPrivateFlags2 & 0x10000) == 65536) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTextDirectionInherited()
  {
    boolean bool;
    if (getRawTextDirection() == 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isTextDirectionResolved()
  {
    boolean bool;
    if ((mPrivateFlags2 & 0x200) == 512) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVerticalFadingEdgeEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x2000) == 8192) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean isVerticalScrollBarEnabled()
  {
    boolean bool;
    if ((mViewFlags & 0x200) == 512) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  protected boolean isVerticalScrollBarHidden()
  {
    return false;
  }
  
  public boolean isVisibleToUser()
  {
    return isVisibleToUser(null);
  }
  
  protected boolean isVisibleToUser(Rect paramRect)
  {
    if (mAttachInfo != null)
    {
      if (mAttachInfo.mWindowVisibility != 0) {
        return false;
      }
      Object localObject = this;
      while ((localObject instanceof View))
      {
        localObject = (View)localObject;
        if ((((View)localObject).getAlpha() > 0.0F) && (((View)localObject).getTransitionAlpha() > 0.0F) && (((View)localObject).getVisibility() == 0)) {
          localObject = mParent;
        } else {
          return false;
        }
      }
      Rect localRect = mAttachInfo.mTmpInvalRect;
      localObject = mAttachInfo.mPoint;
      if (!getGlobalVisibleRect(localRect, (Point)localObject)) {
        return false;
      }
      if (paramRect != null)
      {
        localRect.offset(-x, -y);
        return paramRect.intersect(localRect);
      }
      return true;
    }
    return false;
  }
  
  public boolean isVisibleToUserForAutofill(int paramInt)
  {
    if (mContext.isAutofillCompatibilityEnabled())
    {
      Object localObject = getAccessibilityNodeProvider();
      if (localObject != null)
      {
        localObject = ((AccessibilityNodeProvider)localObject).createAccessibilityNodeInfo(paramInt);
        if (localObject != null) {
          return ((AccessibilityNodeInfo)localObject).isVisibleToUser();
        }
      }
      else
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("isVisibleToUserForAutofill(");
        ((StringBuilder)localObject).append(paramInt);
        ((StringBuilder)localObject).append("): no provider");
        Log.w("View", ((StringBuilder)localObject).toString());
      }
      return false;
    }
    return true;
  }
  
  public void jumpDrawablesToCurrentState()
  {
    if (mBackground != null) {
      mBackground.jumpToCurrentState();
    }
    if (mStateListAnimator != null) {
      mStateListAnimator.jumpToCurrentState();
    }
    if (mDefaultFocusHighlight != null) {
      mDefaultFocusHighlight.jumpToCurrentState();
    }
    if ((mForegroundInfo != null) && (mForegroundInfo.mDrawable != null)) {
      mForegroundInfo.mDrawable.jumpToCurrentState();
    }
  }
  
  public View keyboardNavigationClusterSearch(View paramView, int paramInt)
  {
    if (isKeyboardNavigationCluster()) {
      paramView = this;
    }
    if (isRootNamespace()) {
      return FocusFinder.getInstance().findNextKeyboardNavigationCluster(this, paramView, paramInt);
    }
    if (mParent != null) {
      return mParent.keyboardNavigationClusterSearch(paramView, paramInt);
    }
    return null;
  }
  
  public void layout(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((mPrivateFlags3 & 0x8) != 0)
    {
      onMeasure(mOldWidthMeasureSpec, mOldHeightMeasureSpec);
      mPrivateFlags3 &= 0xFFFFFFF7;
    }
    int i = mLeft;
    int j = mTop;
    int k = mBottom;
    int m = mRight;
    boolean bool1;
    if (isLayoutModeOptical(mParent)) {
      bool1 = setOpticalFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    } else {
      bool1 = setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    boolean bool2 = false;
    boolean bool3 = false;
    Object localObject;
    if ((!bool1) && ((mPrivateFlags & 0x2000) != 8192))
    {
      bool1 = false;
    }
    else
    {
      onLayout(bool1, paramInt1, paramInt2, paramInt3, paramInt4);
      if (shouldDrawRoundScrollbar())
      {
        if (mRoundScrollbarRenderer == null) {
          mRoundScrollbarRenderer = new RoundScrollbarRenderer(this);
        }
      }
      else {
        mRoundScrollbarRenderer = null;
      }
      mPrivateFlags &= 0xDFFF;
      localObject = mListenerInfo;
      bool1 = bool2;
      int n = i;
      if (localObject != null)
      {
        bool1 = bool2;
        n = i;
        if (mOnLayoutChangeListeners != null)
        {
          ArrayList localArrayList = (ArrayList)mOnLayoutChangeListeners.clone();
          int i1 = localArrayList.size();
          for (int i2 = 0;; i2++)
          {
            bool1 = bool3;
            n = i;
            if (i2 >= i1) {
              break;
            }
            ((OnLayoutChangeListener)localArrayList.get(i2)).onLayoutChange(this, paramInt1, paramInt2, paramInt3, paramInt4, i, j, m, k);
          }
        }
      }
    }
    bool3 = isLayoutValid();
    mPrivateFlags &= 0xEFFF;
    mPrivateFlags3 |= 0x4;
    if ((!bool3) && (isFocused()))
    {
      mPrivateFlags &= 0xFFFFFFFE;
      if (canTakeFocus())
      {
        clearParentsWantFocus();
      }
      else if ((getViewRootImpl() != null) && (getViewRootImpl().isInLayout()))
      {
        if (!hasParentWantsFocus()) {
          clearFocusInternal(null, true, bool1);
        }
      }
      else
      {
        clearFocusInternal(null, true, bool1);
        clearParentsWantFocus();
      }
    }
    else if ((mPrivateFlags & 0x1) != 0)
    {
      mPrivateFlags &= 0xFFFFFFFE;
      localObject = findFocus();
      if ((localObject != null) && (!restoreDefaultFocus()) && (!hasParentWantsFocus())) {
        ((View)localObject).clearFocusInternal(null, true, bool1);
      }
    }
    if ((mPrivateFlags3 & 0x8000000) != 0)
    {
      mPrivateFlags3 &= 0xF7FFFFFF;
      notifyEnterOrExitForAutoFillIfNeeded(true);
    }
  }
  
  public void makeOptionalFitsSystemWindows()
  {
    setFlags(2048, 2048);
  }
  
  public void mapRectFromViewToScreenCoords(RectF paramRectF, boolean paramBoolean)
  {
    if (!hasIdentityMatrix()) {
      getMatrix().mapRect(paramRectF);
    }
    paramRectF.offset(mLeft, mTop);
    for (Object localObject = mParent; (localObject instanceof View); localObject = mParent)
    {
      localObject = (View)localObject;
      paramRectF.offset(-mScrollX, -mScrollY);
      if (paramBoolean)
      {
        left = Math.max(left, 0.0F);
        top = Math.max(top, 0.0F);
        right = Math.min(right, ((View)localObject).getWidth());
        bottom = Math.min(bottom, ((View)localObject).getHeight());
      }
      if (!((View)localObject).hasIdentityMatrix()) {
        ((View)localObject).getMatrix().mapRect(paramRectF);
      }
      paramRectF.offset(mLeft, mTop);
    }
    if ((localObject instanceof ViewRootImpl)) {
      paramRectF.offset(0.0F, -mCurScrollY);
    }
    paramRectF.offset(mAttachInfo.mWindowLeft, mAttachInfo.mWindowTop);
  }
  
  public final void measure(int paramInt1, int paramInt2)
  {
    boolean bool = isLayoutModeOptical(this);
    int j;
    if (bool != isLayoutModeOptical(mParent))
    {
      localObject = getOpticalInsets();
      i = left + right;
      j = top + bottom;
      if (bool) {
        i = -i;
      }
      i = MeasureSpec.adjust(paramInt1, i);
      if (bool) {
        paramInt1 = -j;
      } else {
        paramInt1 = j;
      }
      paramInt2 = MeasureSpec.adjust(paramInt2, paramInt1);
      paramInt1 = i;
    }
    long l1 = paramInt1 << 32 | paramInt2 & 0xFFFFFFFF;
    if (mMeasureCache == null) {
      mMeasureCache = new LongSparseLongArray(2);
    }
    int i = mPrivateFlags;
    int k = 0;
    if ((i & 0x1000) == 4096) {
      i = 1;
    } else {
      i = 0;
    }
    if ((paramInt1 == mOldWidthMeasureSpec) && (paramInt2 == mOldHeightMeasureSpec)) {
      j = 0;
    } else {
      j = 1;
    }
    int m;
    if ((MeasureSpec.getMode(paramInt1) == 1073741824) && (MeasureSpec.getMode(paramInt2) == 1073741824)) {
      m = 1;
    } else {
      m = 0;
    }
    int n;
    if ((getMeasuredWidth() == MeasureSpec.getSize(paramInt1)) && (getMeasuredHeight() == MeasureSpec.getSize(paramInt2))) {
      n = 1;
    } else {
      n = 0;
    }
    int i1 = k;
    if (j != 0) {
      if ((!sAlwaysRemeasureExactly) && (m != 0))
      {
        i1 = k;
        if (n != 0) {}
      }
      else
      {
        i1 = 1;
      }
    }
    if ((i == 0) && (i1 == 0)) {
      break label427;
    }
    mPrivateFlags &= 0xF7FF;
    resolveRtlPropertiesIfNeeded();
    if (i != 0) {
      i = -1;
    } else {
      i = mMeasureCache.indexOfKey(l1);
    }
    long l2;
    if ((i >= 0) && (!sIgnoreMeasureCache))
    {
      l2 = mMeasureCache.valueAt(i);
      setMeasuredDimensionRaw((int)(l2 >> 32), (int)l2);
      mPrivateFlags3 |= 0x8;
    }
    else
    {
      onMeasure(paramInt1, paramInt2);
      mPrivateFlags3 &= 0xFFFFFFF7;
    }
    if ((mPrivateFlags & 0x800) == 2048)
    {
      mPrivateFlags |= 0x2000;
      label427:
      mOldWidthMeasureSpec = paramInt1;
      mOldHeightMeasureSpec = paramInt2;
      localObject = mMeasureCache;
      l2 = mMeasuredWidth;
      ((LongSparseLongArray)localObject).put(l1, mMeasuredHeight & 0xFFFFFFFF | l2 << 32);
      return;
    }
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append("View with id ");
    ((StringBuilder)localObject).append(getId());
    ((StringBuilder)localObject).append(": ");
    ((StringBuilder)localObject).append(getClass().getName());
    ((StringBuilder)localObject).append("#onMeasure() did not set the measured dimension by calling setMeasuredDimension()");
    throw new IllegalStateException(((StringBuilder)localObject).toString());
  }
  
  void needGlobalAttributesUpdate(boolean paramBoolean)
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if ((localAttachInfo != null) && (!mRecomputeGlobalAttributes) && ((paramBoolean) || (mKeepScreenOn) || (mSystemUiVisibility != 0) || (mHasSystemUiListeners))) {
      mRecomputeGlobalAttributes = true;
    }
  }
  
  public void notifyEnterOrExitForAutoFillIfNeeded(boolean paramBoolean)
  {
    if (canNotifyAutofillEnterExitEvent())
    {
      AutofillManager localAutofillManager = getAutofillManager();
      if (localAutofillManager != null) {
        if ((paramBoolean) && (isFocused()))
        {
          if (!isLaidOut()) {
            mPrivateFlags3 |= 0x8000000;
          } else if (isVisibleToUser()) {
            localAutofillManager.notifyViewEntered(this);
          }
        }
        else if ((!paramBoolean) && (!isFocused())) {
          localAutofillManager.notifyViewExited(this);
        }
      }
    }
  }
  
  void notifyGlobalFocusCleared(View paramView)
  {
    if ((paramView != null) && (mAttachInfo != null)) {
      mAttachInfo.mTreeObserver.dispatchOnGlobalFocusChange(paramView, null);
    }
  }
  
  public void notifySubtreeAccessibilityStateChangedIfNeeded()
  {
    if ((AccessibilityManager.getInstance(mContext).isEnabled()) && (mAttachInfo != null))
    {
      if ((mPrivateFlags2 & 0x8000000) == 0)
      {
        mPrivateFlags2 |= 0x8000000;
        if (mParent != null) {
          try
          {
            mParent.notifySubtreeAccessibilityStateChanged(this, this, 1);
          }
          catch (AbstractMethodError localAbstractMethodError)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(mParent.getClass().getSimpleName());
            localStringBuilder.append(" does not fully implement ViewParent");
            Log.e("View", localStringBuilder.toString(), localAbstractMethodError);
          }
        }
      }
      return;
    }
  }
  
  public void notifyViewAccessibilityStateChangedIfNeeded(int paramInt)
  {
    if ((AccessibilityManager.getInstance(mContext).isEnabled()) && (mAttachInfo != null))
    {
      if ((paramInt != 1) && (isAccessibilityPane()) && ((getVisibility() == 0) || (paramInt == 32)))
      {
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain();
        localAccessibilityEvent.setEventType(32);
        localAccessibilityEvent.setContentChangeTypes(paramInt);
        localAccessibilityEvent.setSource(this);
        onPopulateAccessibilityEvent(localAccessibilityEvent);
        if (mParent != null) {
          try
          {
            mParent.requestSendAccessibilityEvent(this, localAccessibilityEvent);
          }
          catch (AbstractMethodError localAbstractMethodError1)
          {
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append(mParent.getClass().getSimpleName());
            localStringBuilder.append(" does not fully implement ViewParent");
            Log.e("View", localStringBuilder.toString(), localAbstractMethodError1);
          }
        }
        return;
      }
      Object localObject;
      if (getAccessibilityLiveRegion() != 0)
      {
        localObject = AccessibilityEvent.obtain();
        ((AccessibilityEvent)localObject).setEventType(2048);
        ((AccessibilityEvent)localObject).setContentChangeTypes(paramInt);
        sendAccessibilityEventUnchecked((AccessibilityEvent)localObject);
      }
      else if (mParent != null)
      {
        try
        {
          mParent.notifySubtreeAccessibilityStateChanged(this, this, paramInt);
        }
        catch (AbstractMethodError localAbstractMethodError2)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append(mParent.getClass().getSimpleName());
          ((StringBuilder)localObject).append(" does not fully implement ViewParent");
          Log.e("View", ((StringBuilder)localObject).toString(), localAbstractMethodError2);
        }
      }
      return;
    }
  }
  
  public void offsetLeftAndRight(int paramInt)
  {
    if (paramInt != 0)
    {
      boolean bool = hasIdentityMatrix();
      if (bool)
      {
        if (isHardwareAccelerated())
        {
          invalidateViewProperty(false, false);
        }
        else
        {
          ViewParent localViewParent = mParent;
          if ((localViewParent != null) && (mAttachInfo != null))
          {
            Rect localRect = mAttachInfo.mTmpInvalRect;
            int i;
            int j;
            if (paramInt < 0)
            {
              i = mLeft + paramInt;
              j = mRight;
            }
            else
            {
              i = mLeft;
              j = mRight + paramInt;
            }
            localRect.set(0, 0, j - i, mBottom - mTop);
            localViewParent.invalidateChild(this, localRect);
          }
        }
      }
      else {
        invalidateViewProperty(false, false);
      }
      mLeft += paramInt;
      mRight += paramInt;
      mRenderNode.offsetLeftAndRight(paramInt);
      if (isHardwareAccelerated())
      {
        invalidateViewProperty(false, false);
        invalidateParentIfNeededAndWasQuickRejected();
      }
      else
      {
        if (!bool) {
          invalidateViewProperty(false, true);
        }
        invalidateParentIfNeeded();
      }
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void offsetTopAndBottom(int paramInt)
  {
    if (paramInt != 0)
    {
      boolean bool = hasIdentityMatrix();
      if (bool)
      {
        if (isHardwareAccelerated())
        {
          invalidateViewProperty(false, false);
        }
        else
        {
          ViewParent localViewParent = mParent;
          if ((localViewParent != null) && (mAttachInfo != null))
          {
            Rect localRect = mAttachInfo.mTmpInvalRect;
            int i;
            int j;
            int k;
            if (paramInt < 0)
            {
              i = mTop + paramInt;
              j = mBottom;
              k = paramInt;
            }
            else
            {
              i = mTop;
              j = mBottom + paramInt;
              k = 0;
            }
            localRect.set(0, k, mRight - mLeft, j - i);
            localViewParent.invalidateChild(this, localRect);
          }
        }
      }
      else {
        invalidateViewProperty(false, false);
      }
      mTop += paramInt;
      mBottom += paramInt;
      mRenderNode.offsetTopAndBottom(paramInt);
      if (isHardwareAccelerated())
      {
        invalidateViewProperty(false, false);
        invalidateParentIfNeededAndWasQuickRejected();
      }
      else
      {
        if (!bool) {
          invalidateViewProperty(false, true);
        }
        invalidateParentIfNeeded();
      }
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {}
  
  protected void onAnimationEnd()
  {
    mPrivateFlags &= 0xFFFEFFFF;
  }
  
  protected void onAnimationStart()
  {
    mPrivateFlags |= 0x10000;
  }
  
  public WindowInsets onApplyWindowInsets(WindowInsets paramWindowInsets)
  {
    if ((mPrivateFlags3 & 0x40) == 0)
    {
      if (fitSystemWindows(paramWindowInsets.getSystemWindowInsets())) {
        return paramWindowInsets.consumeSystemWindowInsets();
      }
    }
    else if (fitSystemWindowsInt(paramWindowInsets.getSystemWindowInsets())) {
      return paramWindowInsets.consumeSystemWindowInsets();
    }
    return paramWindowInsets;
  }
  
  protected void onAttachedToWindow()
  {
    if ((mPrivateFlags & 0x200) != 0) {
      mParent.requestTransparentRegion(this);
    }
    mPrivateFlags3 &= 0xFFFFFFFB;
    jumpDrawablesToCurrentState();
    resetSubtreeAccessibilityStateChanged();
    rebuildOutline();
    if (isFocused())
    {
      InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
      if (localInputMethodManager != null) {
        localInputMethodManager.focusIn(this);
      }
    }
  }
  
  public void onCancelPendingInputEvents()
  {
    removePerformClickCallback();
    cancelLongPress();
    mPrivateFlags3 |= 0x10;
  }
  
  public boolean onCapturedPointerEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public boolean onCheckIsTextEditor()
  {
    return false;
  }
  
  public void onCloseSystemDialogs(String paramString) {}
  
  protected void onConfigurationChanged(Configuration paramConfiguration) {}
  
  protected void onCreateContextMenu(ContextMenu paramContextMenu) {}
  
  protected int[] onCreateDrawableState(int paramInt)
  {
    if (((mViewFlags & 0x400000) == 4194304) && ((mParent instanceof View))) {
      return ((View)mParent).onCreateDrawableState(paramInt);
    }
    int i = mPrivateFlags;
    int j = 0;
    if ((i & 0x4000) != 0) {
      j = 0x0 | 0x10;
    }
    int k = j;
    if ((mViewFlags & 0x20) == 0) {
      k = j | 0x8;
    }
    j = k;
    if (isFocused()) {
      j = k | 0x4;
    }
    k = j;
    if ((i & 0x4) != 0) {
      k = j | 0x2;
    }
    j = k;
    if (hasWindowFocus()) {
      j = k | 0x1;
    }
    int m = j;
    if ((0x40000000 & i) != 0) {
      m = j | 0x20;
    }
    k = m;
    if (mAttachInfo != null)
    {
      k = m;
      if (mAttachInfo.mHardwareAccelerationRequested)
      {
        k = m;
        if (ThreadedRenderer.isAvailable()) {
          k = m | 0x40;
        }
      }
    }
    j = k;
    if ((0x10000000 & i) != 0) {
      j = k | 0x80;
    }
    m = mPrivateFlags2;
    k = j;
    if ((m & 0x1) != 0) {
      k = j | 0x100;
    }
    j = k;
    if ((m & 0x2) != 0) {
      j = k | 0x200;
    }
    int[] arrayOfInt1 = StateSet.get(j);
    if (paramInt == 0) {
      return arrayOfInt1;
    }
    int[] arrayOfInt2;
    if (arrayOfInt1 != null)
    {
      arrayOfInt2 = new int[arrayOfInt1.length + paramInt];
      System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, arrayOfInt1.length);
    }
    else
    {
      arrayOfInt2 = new int[paramInt];
    }
    return arrayOfInt2;
  }
  
  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo)
  {
    return null;
  }
  
  protected void onDetachedFromWindow() {}
  
  protected void onDetachedFromWindowInternal()
  {
    mPrivateFlags &= 0xFBFFFFFF;
    mPrivateFlags3 &= 0xFFFFFFFB;
    mPrivateFlags3 &= 0xFDFFFFFF;
    removeUnsetPressCallback();
    removeLongPressCallback();
    removePerformClickCallback();
    cancel(mSendViewScrolledAccessibilityEvent);
    stopNestedScroll();
    jumpDrawablesToCurrentState();
    destroyDrawingCache();
    cleanupDraw();
    mCurrentAnimation = null;
    if ((mViewFlags & 0x40000000) == 1073741824) {
      hideTooltip();
    }
  }
  
  protected void onDisplayHint(int paramInt) {}
  
  public boolean onDragEvent(DragEvent paramDragEvent)
  {
    return false;
  }
  
  protected void onDraw(Canvas paramCanvas) {}
  
  public void onDrawForeground(Canvas paramCanvas)
  {
    onDrawScrollIndicators(paramCanvas);
    onDrawScrollBars(paramCanvas);
    Drawable localDrawable;
    if (mForegroundInfo != null) {
      localDrawable = mForegroundInfo.mDrawable;
    } else {
      localDrawable = null;
    }
    if (localDrawable != null)
    {
      if (mForegroundInfo.mBoundsChanged)
      {
        ForegroundInfo.access$1802(mForegroundInfo, false);
        Rect localRect1 = mForegroundInfo.mSelfBounds;
        Rect localRect2 = mForegroundInfo.mOverlayBounds;
        if (mForegroundInfo.mInsidePadding) {
          localRect1.set(0, 0, getWidth(), getHeight());
        } else {
          localRect1.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        }
        int i = getLayoutDirection();
        Gravity.apply(mForegroundInfo.mGravity, localDrawable.getIntrinsicWidth(), localDrawable.getIntrinsicHeight(), localRect1, localRect2, i);
        localDrawable.setBounds(localRect2);
      }
      localDrawable.draw(paramCanvas);
    }
  }
  
  protected void onDrawHorizontalScrollBar(Canvas paramCanvas, Drawable paramDrawable, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramDrawable.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    paramDrawable.draw(paramCanvas);
  }
  
  protected final void onDrawScrollBars(Canvas paramCanvas)
  {
    Object localObject1 = mScrollCache;
    if (localObject1 != null)
    {
      int i = state;
      if (i == 0) {
        return;
      }
      int j = 0;
      Object localObject2;
      if (i == 2)
      {
        if (interpolatorValues == null) {
          interpolatorValues = new float[1];
        }
        localObject2 = interpolatorValues;
        if (scrollBarInterpolator.timeToValues((float[])localObject2) == Interpolator.Result.FREEZE_END) {
          state = 0;
        } else {
          scrollBar.mutate().setAlpha(Math.round(localObject2[0]));
        }
        j = 1;
      }
      else
      {
        scrollBar.mutate().setAlpha(255);
      }
      boolean bool = isHorizontalScrollBarEnabled();
      if ((isVerticalScrollBarEnabled()) && (!isVerticalScrollBarHidden())) {
        i = 1;
      } else {
        i = 0;
      }
      if (mRoundScrollbarRenderer != null)
      {
        if (i != 0)
        {
          localObject2 = mScrollBarBounds;
          getVerticalScrollBarBounds((Rect)localObject2, null);
          mRoundScrollbarRenderer.drawRoundScrollbars(paramCanvas, scrollBar.getAlpha() / 255.0F, (Rect)localObject2);
          if (j != 0) {
            invalidate();
          }
        }
      }
      else if ((i != 0) || (bool))
      {
        localObject2 = scrollBar;
        if (bool)
        {
          ((ScrollBarDrawable)localObject2).setParameters(computeHorizontalScrollRange(), computeHorizontalScrollOffset(), computeHorizontalScrollExtent(), false);
          Rect localRect = mScrollBarBounds;
          getHorizontalScrollBarBounds(localRect, null);
          onDrawHorizontalScrollBar(paramCanvas, (Drawable)localObject2, left, top, right, bottom);
          if (j != 0) {
            invalidate(localRect);
          }
        }
        if (i != 0)
        {
          ((ScrollBarDrawable)localObject2).setParameters(computeVerticalScrollRange(), computeVerticalScrollOffset(), computeVerticalScrollExtent(), true);
          localObject1 = mScrollBarBounds;
          getVerticalScrollBarBounds((Rect)localObject1, null);
          onDrawVerticalScrollBar(paramCanvas, (Drawable)localObject2, left, top, right, bottom);
          if (j != 0) {
            invalidate((Rect)localObject1);
          }
        }
      }
    }
  }
  
  protected void onDrawVerticalScrollBar(Canvas paramCanvas, Drawable paramDrawable, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramDrawable.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
    paramDrawable.draw(paramCanvas);
  }
  
  public boolean onFilterTouchEventForSecurity(MotionEvent paramMotionEvent)
  {
    return ((mViewFlags & 0x400) == 0) || ((paramMotionEvent.getFlags() & 0x1) == 0);
  }
  
  protected void onFinishInflate() {}
  
  public void onFinishTemporaryDetach() {}
  
  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    if (paramBoolean) {
      sendAccessibilityEvent(8);
    } else {
      notifyViewAccessibilityStateChangedIfNeeded(0);
    }
    switchDefaultFocusHighlight();
    paramRect = InputMethodManager.peekInstance();
    if (!paramBoolean)
    {
      if (isPressed()) {
        setPressed(false);
      }
      if ((paramRect != null) && (mAttachInfo != null) && (mAttachInfo.mHasWindowFocus)) {
        paramRect.focusOut(this);
      }
      onFocusLost();
    }
    else if ((paramRect != null) && (mAttachInfo != null) && (mAttachInfo.mHasWindowFocus))
    {
      paramRect.focusIn(this);
    }
    invalidate(true);
    paramRect = mListenerInfo;
    if ((paramRect != null) && (mOnFocusChangeListener != null)) {
      mOnFocusChangeListener.onFocusChange(this, paramBoolean);
    }
    if (mAttachInfo != null) {
      mAttachInfo.mKeyDispatchState.reset(this);
    }
    notifyEnterOrExitForAutoFillIfNeeded(paramBoolean);
  }
  
  protected void onFocusLost()
  {
    resetPressedState();
  }
  
  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  public void onHoverChanged(boolean paramBoolean) {}
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionMasked();
    if (!mSendingHoverAccessibilityEvents)
    {
      if (((i == 9) || (i == 7)) && (!hasHoveredChild()) && (pointInView(paramMotionEvent.getX(), paramMotionEvent.getY())))
      {
        sendAccessibilityHoverEvent(128);
        mSendingHoverAccessibilityEvents = true;
      }
    }
    else if ((i == 10) || ((i == 2) && (!pointInView(paramMotionEvent.getX(), paramMotionEvent.getY()))))
    {
      mSendingHoverAccessibilityEvents = false;
      sendAccessibilityHoverEvent(256);
    }
    if (((i == 9) || (i == 7)) && (paramMotionEvent.isFromSource(8194)) && (isOnScrollbar(paramMotionEvent.getX(), paramMotionEvent.getY()))) {
      awakenScrollBars();
    }
    if ((!isHoverable()) && (!isHovered())) {
      return false;
    }
    switch (i)
    {
    default: 
      break;
    case 10: 
      setHovered(false);
      break;
    case 9: 
      setHovered(true);
    }
    dispatchGenericMotionEventInternal(paramMotionEvent);
    return true;
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (mAccessibilityDelegate != null) {
      mAccessibilityDelegate.onInitializeAccessibilityEvent(this, paramAccessibilityEvent);
    } else {
      onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    }
  }
  
  public void onInitializeAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    paramAccessibilityEvent.setSource(this);
    paramAccessibilityEvent.setClassName(getAccessibilityClassName());
    paramAccessibilityEvent.setPackageName(getContext().getPackageName());
    paramAccessibilityEvent.setEnabled(isEnabled());
    paramAccessibilityEvent.setContentDescription(mContentDescription);
    int i = paramAccessibilityEvent.getEventType();
    Object localObject;
    if (i != 8)
    {
      if (i == 8192)
      {
        localObject = getIterableTextForAccessibility();
        if ((localObject != null) && (((CharSequence)localObject).length() > 0))
        {
          paramAccessibilityEvent.setFromIndex(getAccessibilitySelectionStart());
          paramAccessibilityEvent.setToIndex(getAccessibilitySelectionEnd());
          paramAccessibilityEvent.setItemCount(((CharSequence)localObject).length());
        }
      }
    }
    else
    {
      if (mAttachInfo != null) {
        localObject = mAttachInfo.mTempArrayList;
      } else {
        localObject = new ArrayList();
      }
      getRootView().addFocusables((ArrayList)localObject, 2, 0);
      paramAccessibilityEvent.setItemCount(((ArrayList)localObject).size());
      paramAccessibilityEvent.setCurrentItemIndex(((ArrayList)localObject).indexOf(this));
      if (mAttachInfo != null) {
        ((ArrayList)localObject).clear();
      }
    }
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    if (mAccessibilityDelegate != null) {
      mAccessibilityDelegate.onInitializeAccessibilityNodeInfo(this, paramAccessibilityNodeInfo);
    } else {
      onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    }
  }
  
  public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo paramAccessibilityNodeInfo)
  {
    if (mAttachInfo == null) {
      return;
    }
    Object localObject1 = mAttachInfo.mTmpInvalRect;
    getDrawingRect((Rect)localObject1);
    paramAccessibilityNodeInfo.setBoundsInParent((Rect)localObject1);
    getBoundsOnScreen((Rect)localObject1, true);
    paramAccessibilityNodeInfo.setBoundsInScreen((Rect)localObject1);
    localObject1 = getParentForAccessibility();
    if ((localObject1 instanceof View)) {
      paramAccessibilityNodeInfo.setParent((View)localObject1);
    }
    View localView;
    if (mID != -1)
    {
      localView = getRootView();
      localObject1 = localView;
      if (localView == null) {
        localObject1 = this;
      }
      localObject1 = ((View)localObject1).findLabelForView(this, mID);
      if (localObject1 != null) {
        paramAccessibilityNodeInfo.setLabeledBy((View)localObject1);
      }
      if (((mAttachInfo.mAccessibilityFetchFlags & 0x10) != 0) && (Resources.resourceHasPackage(mID))) {
        try
        {
          paramAccessibilityNodeInfo.setViewIdResourceName(getResources().getResourceName(mID));
        }
        catch (Resources.NotFoundException localNotFoundException) {}
      }
    }
    if (mLabelForId != -1)
    {
      localView = getRootView();
      localObject2 = localView;
      if (localView == null) {
        localObject2 = this;
      }
      localObject2 = ((View)localObject2).findViewInsideOutShouldExist(this, mLabelForId);
      if (localObject2 != null) {
        paramAccessibilityNodeInfo.setLabelFor((View)localObject2);
      }
    }
    if (mAccessibilityTraversalBeforeId != -1)
    {
      localView = getRootView();
      localObject2 = localView;
      if (localView == null) {
        localObject2 = this;
      }
      localObject2 = ((View)localObject2).findViewInsideOutShouldExist(this, mAccessibilityTraversalBeforeId);
      if ((localObject2 != null) && (((View)localObject2).includeForAccessibility())) {
        paramAccessibilityNodeInfo.setTraversalBefore((View)localObject2);
      }
    }
    if (mAccessibilityTraversalAfterId != -1)
    {
      localView = getRootView();
      localObject2 = localView;
      if (localView == null) {
        localObject2 = this;
      }
      localObject2 = ((View)localObject2).findViewInsideOutShouldExist(this, mAccessibilityTraversalAfterId);
      if ((localObject2 != null) && (((View)localObject2).includeForAccessibility())) {
        paramAccessibilityNodeInfo.setTraversalAfter((View)localObject2);
      }
    }
    paramAccessibilityNodeInfo.setVisibleToUser(isVisibleToUser());
    paramAccessibilityNodeInfo.setImportantForAccessibility(isImportantForAccessibility());
    paramAccessibilityNodeInfo.setPackageName(mContext.getPackageName());
    paramAccessibilityNodeInfo.setClassName(getAccessibilityClassName());
    paramAccessibilityNodeInfo.setContentDescription(getContentDescription());
    paramAccessibilityNodeInfo.setEnabled(isEnabled());
    paramAccessibilityNodeInfo.setClickable(isClickable());
    paramAccessibilityNodeInfo.setFocusable(isFocusable());
    paramAccessibilityNodeInfo.setScreenReaderFocusable(isScreenReaderFocusable());
    paramAccessibilityNodeInfo.setFocused(isFocused());
    paramAccessibilityNodeInfo.setAccessibilityFocused(isAccessibilityFocused());
    paramAccessibilityNodeInfo.setSelected(isSelected());
    paramAccessibilityNodeInfo.setLongClickable(isLongClickable());
    paramAccessibilityNodeInfo.setContextClickable(isContextClickable());
    paramAccessibilityNodeInfo.setLiveRegion(getAccessibilityLiveRegion());
    if ((mTooltipInfo != null) && (mTooltipInfo.mTooltipText != null))
    {
      paramAccessibilityNodeInfo.setTooltipText(mTooltipInfo.mTooltipText);
      if (mTooltipInfo.mTooltipPopup == null) {
        localObject2 = AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_TOOLTIP;
      } else {
        localObject2 = AccessibilityNodeInfo.AccessibilityAction.ACTION_HIDE_TOOLTIP;
      }
      paramAccessibilityNodeInfo.addAction((AccessibilityNodeInfo.AccessibilityAction)localObject2);
    }
    paramAccessibilityNodeInfo.addAction(4);
    paramAccessibilityNodeInfo.addAction(8);
    if (isFocusable()) {
      if (isFocused()) {
        paramAccessibilityNodeInfo.addAction(2);
      } else {
        paramAccessibilityNodeInfo.addAction(1);
      }
    }
    if (!isAccessibilityFocused()) {
      paramAccessibilityNodeInfo.addAction(64);
    } else {
      paramAccessibilityNodeInfo.addAction(128);
    }
    if ((isClickable()) && (isEnabled())) {
      paramAccessibilityNodeInfo.addAction(16);
    }
    if ((isLongClickable()) && (isEnabled())) {
      paramAccessibilityNodeInfo.addAction(32);
    }
    if ((isContextClickable()) && (isEnabled())) {
      paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CONTEXT_CLICK);
    }
    Object localObject2 = getIterableTextForAccessibility();
    if ((localObject2 != null) && (((CharSequence)localObject2).length() > 0))
    {
      paramAccessibilityNodeInfo.setTextSelection(getAccessibilitySelectionStart(), getAccessibilitySelectionEnd());
      paramAccessibilityNodeInfo.addAction(131072);
      paramAccessibilityNodeInfo.addAction(256);
      paramAccessibilityNodeInfo.addAction(512);
      paramAccessibilityNodeInfo.setMovementGranularities(11);
    }
    paramAccessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN);
    populateAccessibilityNodeInfoDrawingOrderInParent(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setPaneTitle(mAccessibilityPaneTitle);
    paramAccessibilityNodeInfo.setHeading(isAccessibilityHeading());
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    SeempLog.record(4);
    if (KeyEvent.isConfirmKey(paramInt))
    {
      if ((mViewFlags & 0x20) == 32) {
        return true;
      }
      if (paramKeyEvent.getRepeatCount() == 0)
      {
        if (((mViewFlags & 0x4000) != 16384) && ((mViewFlags & 0x200000) != 2097152)) {
          paramInt = 0;
        } else {
          paramInt = 1;
        }
        if ((paramInt != 0) || ((mViewFlags & 0x40000000) == 1073741824))
        {
          float f1 = getWidth() / 2.0F;
          float f2 = getHeight() / 2.0F;
          if (paramInt != 0) {
            setPressed(true, f1, f2);
          }
          checkForLongClick(0, f1, f2);
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyShortcut(int paramInt, KeyEvent paramKeyEvent)
  {
    return false;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    SeempLog.record(5);
    if (KeyEvent.isConfirmKey(paramInt))
    {
      if ((mViewFlags & 0x20) == 32) {
        return true;
      }
      if (((mViewFlags & 0x4000) == 16384) && (isPressed()))
      {
        setPressed(false);
        if (!mHasPerformedLongPress)
        {
          removeLongPressCallback();
          if (!paramKeyEvent.isCanceled()) {
            return performClickInternal();
          }
        }
      }
    }
    return false;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), paramInt1), getDefaultSize(getSuggestedMinimumHeight(), paramInt2));
  }
  
  public void onMovedToDisplay(int paramInt, Configuration paramConfiguration) {}
  
  protected void onOverScrolled(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2) {}
  
  public void onPointerCaptureChange(boolean paramBoolean) {}
  
  public void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (mAccessibilityDelegate != null) {
      mAccessibilityDelegate.onPopulateAccessibilityEvent(this, paramAccessibilityEvent);
    } else {
      onPopulateAccessibilityEventInternal(paramAccessibilityEvent);
    }
  }
  
  public void onPopulateAccessibilityEventInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    if ((paramAccessibilityEvent.getEventType() == 32) && (!TextUtils.isEmpty(getAccessibilityPaneTitle()))) {
      paramAccessibilityEvent.getText().add(getAccessibilityPaneTitle());
    }
  }
  
  public void onProvideAutofillStructure(ViewStructure paramViewStructure, int paramInt)
  {
    onProvideStructureForAssistOrAutofill(paramViewStructure, true, paramInt);
  }
  
  public void onProvideAutofillVirtualStructure(ViewStructure paramViewStructure, int paramInt)
  {
    if (mContext.isAutofillCompatibilityEnabled()) {
      onProvideVirtualStructureCompat(paramViewStructure, true);
    }
  }
  
  public void onProvideStructure(ViewStructure paramViewStructure)
  {
    onProvideStructureForAssistOrAutofill(paramViewStructure, false, 0);
  }
  
  public void onProvideVirtualStructure(ViewStructure paramViewStructure)
  {
    onProvideVirtualStructureCompat(paramViewStructure, false);
  }
  
  public void onResolveDrawables(int paramInt) {}
  
  public PointerIcon onResolvePointerIcon(MotionEvent paramMotionEvent, int paramInt)
  {
    float f1 = paramMotionEvent.getX(paramInt);
    float f2 = paramMotionEvent.getY(paramInt);
    if ((!isDraggingScrollBar()) && (!isOnScrollbarThumb(f1, f2))) {
      return mPointerIcon;
    }
    return PointerIcon.getSystemIcon(mContext, 1000);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    mPrivateFlags |= 0x20000;
    Object localObject;
    if ((paramParcelable != null) && (!(paramParcelable instanceof AbsSavedState)))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Wrong state class, expecting View State but received ");
      ((StringBuilder)localObject).append(paramParcelable.getClass().toString());
      ((StringBuilder)localObject).append(" instead. This usually happens when two views of different type have the same id in the same hierarchy. This view's id is ");
      ((StringBuilder)localObject).append(ViewDebug.resolveId(mContext, getId()));
      ((StringBuilder)localObject).append(". Make sure other views do not use the same id.");
      throw new IllegalArgumentException(((StringBuilder)localObject).toString());
    }
    if ((paramParcelable != null) && ((paramParcelable instanceof BaseSavedState)))
    {
      localObject = (BaseSavedState)paramParcelable;
      if ((mSavedData & 0x1) != 0) {
        mStartActivityRequestWho = mStartActivityRequestWhoSaved;
      }
      if ((mSavedData & 0x2) != 0) {
        setAutofilled(mIsAutofilled);
      }
      if ((mSavedData & 0x4) != 0)
      {
        paramParcelable = (BaseSavedState)paramParcelable;
        mSavedData &= 0xFFFFFFFB;
        if ((mPrivateFlags3 & 0x40000000) != 0)
        {
          if (Helper.sDebug)
          {
            paramParcelable = new StringBuilder();
            paramParcelable.append("onRestoreInstanceState(): not setting autofillId to ");
            paramParcelable.append(mAutofillViewId);
            paramParcelable.append(" because view explicitly set it to ");
            paramParcelable.append(mAutofillId);
            Log.d("View", paramParcelable.toString());
          }
        }
        else
        {
          mAutofillViewId = mAutofillViewId;
          mAutofillId = null;
        }
      }
    }
  }
  
  public void onRtlPropertiesChanged(int paramInt) {}
  
  protected Parcelable onSaveInstanceState()
  {
    mPrivateFlags |= 0x20000;
    if ((mStartActivityRequestWho == null) && (!isAutofilled()) && (mAutofillViewId <= 1073741823)) {
      return BaseSavedState.EMPTY_STATE;
    }
    BaseSavedState localBaseSavedState = new BaseSavedState(AbsSavedState.EMPTY_STATE);
    if (mStartActivityRequestWho != null) {
      mSavedData |= 0x1;
    }
    if (isAutofilled()) {
      mSavedData |= 0x2;
    }
    if (mAutofillViewId > 1073741823) {
      mSavedData |= 0x4;
    }
    mStartActivityRequestWhoSaved = mStartActivityRequestWho;
    mIsAutofilled = isAutofilled();
    mAutofillViewId = mAutofillViewId;
    return localBaseSavedState;
  }
  
  public void onScreenStateChanged(int paramInt) {}
  
  protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    notifySubtreeAccessibilityStateChangedIfNeeded();
    if (AccessibilityManager.getInstance(mContext).isEnabled()) {
      postSendViewScrolledAccessibilityEventCallback(paramInt1 - paramInt3, paramInt2 - paramInt4);
    }
    mBackgroundSizeChanged = true;
    mDefaultFocusHighlightSizeChanged = true;
    if (mForegroundInfo != null) {
      ForegroundInfo.access$1802(mForegroundInfo, true);
    }
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null) {
      mViewScrollChanged = true;
    }
    if ((mListenerInfo != null) && (mListenerInfo.mOnScrollChangeListener != null)) {
      mListenerInfo.mOnScrollChangeListener.onScrollChange(this, paramInt1, paramInt2, paramInt3, paramInt4);
    }
  }
  
  protected boolean onSetAlpha(int paramInt)
  {
    return false;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {}
  
  public void onStartTemporaryDetach()
  {
    removeUnsetPressCallback();
    mPrivateFlags |= 0x4000000;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    SeempLog.record(3);
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    int i = mViewFlags;
    int j = paramMotionEvent.getAction();
    boolean bool1;
    if (((i & 0x4000) != 16384) && ((i & 0x200000) != 2097152) && ((i & 0x800000) != 8388608)) {
      bool1 = false;
    } else {
      bool1 = true;
    }
    if ((i & 0x20) == 32)
    {
      if ((j == 1) && ((0x4000 & mPrivateFlags) != 0)) {
        setPressed(false);
      }
      mPrivateFlags3 &= 0xFFFDFFFF;
      return bool1;
    }
    if ((mTouchDelegate != null) && (mTouchDelegate.onTouchEvent(paramMotionEvent))) {
      return true;
    }
    if ((!bool1) && ((i & 0x40000000) != 1073741824)) {
      return false;
    }
    switch (j)
    {
    default: 
      break;
    case 3: 
      if (bool1) {
        setPressed(false);
      }
      removeTapCallback();
      removeLongPressCallback();
      mInContextButtonPress = false;
      mHasPerformedLongPress = false;
      mIgnoreNextUpEvent = false;
      mPrivateFlags3 &= 0xFFFDFFFF;
      break;
    case 2: 
      if (bool1) {
        drawableHotspotChanged(f1, f2);
      }
      if (!pointInView(f1, f2, mTouchSlop))
      {
        removeTapCallback();
        removeLongPressCallback();
        if ((0x4000 & mPrivateFlags) != 0) {
          setPressed(false);
        }
        mPrivateFlags3 &= 0xFFFDFFFF;
      }
      break;
    case 1: 
      mPrivateFlags3 = (0xFFFDFFFF & mPrivateFlags3);
      if ((i & 0x40000000) == 1073741824) {
        handleTooltipUp();
      }
      if (!bool1)
      {
        removeTapCallback();
        removeLongPressCallback();
        mInContextButtonPress = false;
        mHasPerformedLongPress = false;
        mIgnoreNextUpEvent = false;
      }
      else
      {
        if ((mPrivateFlags & 0x2000000) != 0) {
          j = 1;
        } else {
          j = 0;
        }
        if (((0x4000 & mPrivateFlags) != 0) || (j != 0))
        {
          boolean bool2 = false;
          bool1 = bool2;
          if (isFocusable())
          {
            bool1 = bool2;
            if (isFocusableInTouchMode())
            {
              bool1 = bool2;
              if (!isFocused()) {
                bool1 = requestFocus();
              }
            }
          }
          if (j != 0) {
            setPressed(true, f1, f2);
          }
          if ((!mHasPerformedLongPress) && (!mIgnoreNextUpEvent))
          {
            removeLongPressCallback();
            if (!bool1)
            {
              if (mPerformClick == null) {
                mPerformClick = new PerformClick(null);
              }
              if (!post(mPerformClick)) {
                performClickInternal();
              }
            }
          }
          if (mUnsetPressedState == null) {
            mUnsetPressedState = new UnsetPressedState(null);
          }
          if (j != 0) {
            postDelayed(mUnsetPressedState, ViewConfiguration.getPressedStateDuration());
          } else if (!post(mUnsetPressedState)) {
            mUnsetPressedState.run();
          }
          removeTapCallback();
        }
        mIgnoreNextUpEvent = false;
      }
      break;
    case 0: 
      if (paramMotionEvent.getSource() == 4098) {
        mPrivateFlags3 |= 0x20000;
      }
      mHasPerformedLongPress = false;
      if (!bool1) {
        checkForLongClick(0, f1, f2);
      } else if (!performButtonActionOnTouchDown(paramMotionEvent)) {
        if (isInScrollingContainer())
        {
          mPrivateFlags |= 0x2000000;
          if (mPendingCheckForTap == null) {
            mPendingCheckForTap = new CheckForTap(null);
          }
          mPendingCheckForTap.x = paramMotionEvent.getX();
          mPendingCheckForTap.y = paramMotionEvent.getY();
          postDelayed(mPendingCheckForTap, ViewConfiguration.getTapTimeout());
        }
        else
        {
          setPressed(true, f1, f2);
          checkForLongClick(0, f1, f2);
        }
      }
      break;
    }
    return true;
  }
  
  public boolean onTrackballEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }
  
  boolean onUnhandledKeyEvent(KeyEvent paramKeyEvent)
  {
    if ((mListenerInfo != null) && (mListenerInfo.mUnhandledKeyListeners != null)) {
      for (int i = mListenerInfo.mUnhandledKeyListeners.size() - 1; i >= 0; i--) {
        if (((OnUnhandledKeyEventListener)mListenerInfo.mUnhandledKeyListeners.get(i)).onUnhandledKeyEvent(this, paramKeyEvent)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void onVisibilityAggregated(boolean paramBoolean)
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x20000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    int i;
    if (paramBoolean) {
      i = 0x20000000 | mPrivateFlags3;
    } else {
      i = mPrivateFlags3 & 0xDFFFFFFF;
    }
    mPrivateFlags3 = i;
    if ((paramBoolean) && (mAttachInfo != null)) {
      initialAwakenScrollBars();
    }
    Object localObject = mBackground;
    if ((localObject != null) && (paramBoolean != ((Drawable)localObject).isVisible())) {
      ((Drawable)localObject).setVisible(paramBoolean, false);
    }
    localObject = mDefaultFocusHighlight;
    if ((localObject != null) && (paramBoolean != ((Drawable)localObject).isVisible())) {
      ((Drawable)localObject).setVisible(paramBoolean, false);
    }
    if (mForegroundInfo != null) {
      localObject = mForegroundInfo.mDrawable;
    } else {
      localObject = null;
    }
    if ((localObject != null) && (paramBoolean != ((Drawable)localObject).isVisible())) {
      ((Drawable)localObject).setVisible(paramBoolean, false);
    }
    if (isAutofillable())
    {
      localObject = getAutofillManager();
      if ((localObject != null) && (getAutofillViewId() > 1073741823))
      {
        if (mVisibilityChangeForAutofillHandler != null) {
          mVisibilityChangeForAutofillHandler.removeMessages(0);
        }
        if (paramBoolean)
        {
          ((AutofillManager)localObject).notifyViewVisibilityChanged(this, true);
        }
        else
        {
          if (mVisibilityChangeForAutofillHandler == null) {
            mVisibilityChangeForAutofillHandler = new VisibilityChangeForAutofillHandler((AutofillManager)localObject, this, null);
          }
          mVisibilityChangeForAutofillHandler.obtainMessage(0, this).sendToTarget();
        }
      }
    }
    if ((!TextUtils.isEmpty(getAccessibilityPaneTitle())) && (paramBoolean != bool))
    {
      if (paramBoolean) {
        i = 16;
      } else {
        i = 32;
      }
      notifyViewAccessibilityStateChangedIfNeeded(i);
    }
  }
  
  protected void onVisibilityChanged(View paramView, int paramInt) {}
  
  public void onWindowFocusChanged(boolean paramBoolean)
  {
    InputMethodManager localInputMethodManager = InputMethodManager.peekInstance();
    if (!paramBoolean)
    {
      if (isPressed()) {
        setPressed(false);
      }
      mPrivateFlags3 &= 0xFFFDFFFF;
      if ((localInputMethodManager != null) && ((mPrivateFlags & 0x2) != 0)) {
        localInputMethodManager.focusOut(this);
      }
      removeLongPressCallback();
      removeTapCallback();
      onFocusLost();
    }
    else if ((localInputMethodManager != null) && ((mPrivateFlags & 0x2) != 0))
    {
      localInputMethodManager.focusIn(this);
    }
    refreshDrawableState();
  }
  
  public void onWindowSystemUiVisibilityChanged(int paramInt) {}
  
  protected void onWindowVisibilityChanged(int paramInt)
  {
    if (paramInt == 0) {
      initialAwakenScrollBars();
    }
  }
  
  public void outputDirtyFlags(String paramString, boolean paramBoolean, int paramInt)
  {
    Object localObject = new StringBuilder();
    ((StringBuilder)localObject).append(paramString);
    ((StringBuilder)localObject).append(this);
    ((StringBuilder)localObject).append("             DIRTY(");
    ((StringBuilder)localObject).append(mPrivateFlags & 0x600000);
    ((StringBuilder)localObject).append(") DRAWN(");
    ((StringBuilder)localObject).append(mPrivateFlags & 0x20);
    ((StringBuilder)localObject).append(") CACHE_VALID(");
    ((StringBuilder)localObject).append(mPrivateFlags & 0x8000);
    ((StringBuilder)localObject).append(") INVALIDATED(");
    ((StringBuilder)localObject).append(mPrivateFlags & 0x80000000);
    ((StringBuilder)localObject).append(")");
    Log.d("View", ((StringBuilder)localObject).toString());
    if (paramBoolean) {
      mPrivateFlags &= paramInt;
    }
    if ((this instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)this;
      int i = localViewGroup.getChildCount();
      for (int j = 0; j < i; j++)
      {
        localObject = localViewGroup.getChildAt(j);
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(paramString);
        localStringBuilder.append("  ");
        ((View)localObject).outputDirtyFlags(localStringBuilder.toString(), paramBoolean, paramInt);
      }
    }
  }
  
  protected boolean overScrollBy(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, boolean paramBoolean)
  {
    int i = mOverScrollMode;
    int j;
    if (computeHorizontalScrollRange() > computeHorizontalScrollExtent()) {
      j = 1;
    } else {
      j = 0;
    }
    int k;
    if (computeVerticalScrollRange() > computeVerticalScrollExtent()) {
      k = 1;
    } else {
      k = 0;
    }
    if ((i != 0) && ((i != 1) || (j == 0))) {
      j = 0;
    } else {
      j = 1;
    }
    if ((i != 0) && ((i != 1) || (k == 0))) {
      k = 0;
    } else {
      k = 1;
    }
    paramInt3 += paramInt1;
    if (j == 0) {
      paramInt1 = 0;
    } else {
      paramInt1 = paramInt7;
    }
    paramInt4 += paramInt2;
    if (k == 0) {
      paramInt2 = 0;
    } else {
      paramInt2 = paramInt8;
    }
    paramInt7 = -paramInt1;
    paramInt1 += paramInt5;
    paramInt5 = -paramInt2;
    paramInt2 += paramInt6;
    paramBoolean = false;
    if (paramInt3 > paramInt1) {
      paramBoolean = true;
    }
    for (;;)
    {
      break;
      paramInt1 = paramInt3;
      if (paramInt3 < paramInt7)
      {
        paramInt1 = paramInt7;
        paramBoolean = true;
      }
    }
    boolean bool = false;
    if (paramInt4 > paramInt2) {
      bool = true;
    }
    for (;;)
    {
      break;
      paramInt2 = paramInt4;
      if (paramInt4 < paramInt5)
      {
        paramInt2 = paramInt5;
        bool = true;
      }
    }
    onOverScrolled(paramInt1, paramInt2, paramBoolean, bool);
    if ((!paramBoolean) && (!bool)) {
      paramBoolean = false;
    } else {
      paramBoolean = true;
    }
    return paramBoolean;
  }
  
  public boolean performAccessibilityAction(int paramInt, Bundle paramBundle)
  {
    if (mAccessibilityDelegate != null) {
      return mAccessibilityDelegate.performAccessibilityAction(this, paramInt, paramBundle);
    }
    return performAccessibilityActionInternal(paramInt, paramBundle);
  }
  
  public boolean performAccessibilityActionInternal(int paramInt, Bundle paramBundle)
  {
    if ((isNestedScrollingEnabled()) && ((paramInt == 8192) || (paramInt == 4096) || (paramInt == 16908344) || (paramInt == 16908345) || (paramInt == 16908346) || (paramInt == 16908347)) && (dispatchNestedPrePerformAccessibilityAction(paramInt, paramBundle))) {
      return true;
    }
    switch (paramInt)
    {
    default: 
      break;
    case 16908357: 
      if ((mTooltipInfo != null) && (mTooltipInfo.mTooltipPopup != null))
      {
        hideTooltip();
        return true;
      }
      return false;
    case 16908356: 
      if ((mTooltipInfo != null) && (mTooltipInfo.mTooltipPopup != null)) {
        return false;
      }
      return showLongClickTooltip(0, 0);
    case 16908348: 
      if (isContextClickable())
      {
        performContextClick();
        return true;
      }
      break;
    case 16908342: 
      if (mAttachInfo != null)
      {
        paramBundle = mAttachInfo.mTmpInvalRect;
        getDrawingRect(paramBundle);
        return requestRectangleOnScreen(paramBundle, true);
      }
      break;
    case 131072: 
      if (getIterableTextForAccessibility() == null) {
        return false;
      }
      int i = -1;
      if (paramBundle != null) {
        paramInt = paramBundle.getInt("ACTION_ARGUMENT_SELECTION_START_INT", -1);
      } else {
        paramInt = -1;
      }
      if (paramBundle != null) {
        i = paramBundle.getInt("ACTION_ARGUMENT_SELECTION_END_INT", -1);
      }
      if (((getAccessibilitySelectionStart() != paramInt) || (getAccessibilitySelectionEnd() != i)) && (paramInt == i))
      {
        setAccessibilitySelection(paramInt, i);
        notifyViewAccessibilityStateChangedIfNeeded(0);
        return true;
      }
      break;
    case 512: 
      if (paramBundle != null) {
        return traverseAtGranularity(paramBundle.getInt("ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT"), false, paramBundle.getBoolean("ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN"));
      }
      break;
    case 256: 
      if (paramBundle != null) {
        return traverseAtGranularity(paramBundle.getInt("ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT"), true, paramBundle.getBoolean("ACTION_ARGUMENT_EXTEND_SELECTION_BOOLEAN"));
      }
      break;
    case 128: 
      if (isAccessibilityFocused())
      {
        clearAccessibilityFocus();
        return true;
      }
      break;
    case 64: 
      if (!isAccessibilityFocused()) {
        return requestAccessibilityFocus();
      }
      break;
    case 32: 
      if (isLongClickable())
      {
        performLongClick();
        return true;
      }
      break;
    case 16: 
      if (isClickable())
      {
        performClickInternal();
        return true;
      }
      break;
    case 8: 
      if (isSelected())
      {
        setSelected(false);
        return isSelected() ^ true;
      }
      break;
    case 4: 
      if (!isSelected())
      {
        setSelected(true);
        return isSelected();
      }
      break;
    case 2: 
      if (hasFocus())
      {
        clearFocus();
        return isFocused() ^ true;
      }
      break;
    case 1: 
      if (!hasFocus())
      {
        getViewRootImpl().ensureTouchMode(false);
        return requestFocus();
      }
      break;
    }
    return false;
  }
  
  protected boolean performButtonActionOnTouchDown(MotionEvent paramMotionEvent)
  {
    if ((paramMotionEvent.isFromSource(8194)) && ((paramMotionEvent.getButtonState() & 0x2) != 0))
    {
      showContextMenu(paramMotionEvent.getX(), paramMotionEvent.getY());
      mPrivateFlags |= 0x4000000;
      return true;
    }
    return false;
  }
  
  public boolean performClick()
  {
    notifyAutofillManagerOnClick();
    ListenerInfo localListenerInfo = mListenerInfo;
    boolean bool = false;
    if ((localListenerInfo != null) && (mOnClickListener != null))
    {
      playSoundEffect(0);
      mOnClickListener.onClick(this);
      bool = true;
    }
    sendAccessibilityEvent(1);
    notifyEnterOrExitForAutoFillIfNeeded(true);
    return bool;
  }
  
  void performCollectViewAttributes(AttachInfo paramAttachInfo, int paramInt)
  {
    if ((paramInt & 0xC) == 0)
    {
      if ((mViewFlags & 0x4000000) == 67108864) {
        mKeepScreenOn = true;
      }
      mSystemUiVisibility |= mSystemUiVisibility;
      ListenerInfo localListenerInfo = mListenerInfo;
      if ((localListenerInfo != null) && (mOnSystemUiVisibilityChangeListener != null)) {
        mHasSystemUiListeners = true;
      }
    }
  }
  
  public boolean performContextClick()
  {
    sendAccessibilityEvent(8388608);
    boolean bool1 = false;
    ListenerInfo localListenerInfo = mListenerInfo;
    boolean bool2 = bool1;
    if (localListenerInfo != null)
    {
      bool2 = bool1;
      if (mOnContextClickListener != null) {
        bool2 = mOnContextClickListener.onContextClick(this);
      }
    }
    if (bool2) {
      performHapticFeedback(6);
    }
    return bool2;
  }
  
  public boolean performContextClick(float paramFloat1, float paramFloat2)
  {
    return performContextClick();
  }
  
  public boolean performHapticFeedback(int paramInt)
  {
    return performHapticFeedback(paramInt, 0);
  }
  
  public boolean performHapticFeedback(int paramInt1, int paramInt2)
  {
    Object localObject = mAttachInfo;
    boolean bool = false;
    if (localObject == null) {
      return false;
    }
    if (((paramInt2 & 0x1) == 0) && (!isHapticFeedbackEnabled())) {
      return false;
    }
    localObject = mAttachInfo.mRootCallbacks;
    if ((paramInt2 & 0x2) != 0) {
      bool = true;
    }
    return ((View.AttachInfo.Callbacks)localObject).performHapticFeedback(paramInt1, bool);
  }
  
  public boolean performLongClick()
  {
    return performLongClickInternal(mLongClickX, mLongClickY);
  }
  
  public boolean performLongClick(float paramFloat1, float paramFloat2)
  {
    mLongClickX = paramFloat1;
    mLongClickY = paramFloat2;
    boolean bool = performLongClick();
    mLongClickX = NaN.0F;
    mLongClickY = NaN.0F;
    return bool;
  }
  
  public void playSoundEffect(int paramInt)
  {
    if ((mAttachInfo != null) && (mAttachInfo.mRootCallbacks != null) && (isSoundEffectsEnabled()))
    {
      mAttachInfo.mRootCallbacks.playSoundEffect(paramInt);
      return;
    }
  }
  
  final boolean pointInView(float paramFloat1, float paramFloat2)
  {
    return pointInView(paramFloat1, paramFloat2, 0.0F);
  }
  
  public boolean pointInView(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    boolean bool;
    if ((paramFloat1 >= -paramFloat3) && (paramFloat2 >= -paramFloat3) && (paramFloat1 < mRight - mLeft + paramFloat3) && (paramFloat2 < mBottom - mTop + paramFloat3)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public boolean post(Runnable paramRunnable)
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null) {
      return mHandler.post(paramRunnable);
    }
    getRunQueue().post(paramRunnable);
    return true;
  }
  
  public boolean postDelayed(Runnable paramRunnable, long paramLong)
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null) {
      return mHandler.postDelayed(paramRunnable, paramLong);
    }
    getRunQueue().postDelayed(paramRunnable, paramLong);
    return true;
  }
  
  public void postInvalidate()
  {
    postInvalidateDelayed(0L);
  }
  
  public void postInvalidate(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    postInvalidateDelayed(0L, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void postInvalidateDelayed(long paramLong)
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null) {
      mViewRootImpl.dispatchInvalidateDelayed(this, paramLong);
    }
  }
  
  public void postInvalidateDelayed(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null)
    {
      View.AttachInfo.InvalidateInfo localInvalidateInfo = View.AttachInfo.InvalidateInfo.obtain();
      target = this;
      left = paramInt1;
      top = paramInt2;
      right = paramInt3;
      bottom = paramInt4;
      mViewRootImpl.dispatchInvalidateRectDelayed(localInvalidateInfo, paramLong);
    }
  }
  
  public void postInvalidateOnAnimation()
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null) {
      mViewRootImpl.dispatchInvalidateOnAnimation(this);
    }
  }
  
  public void postInvalidateOnAnimation(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null)
    {
      View.AttachInfo.InvalidateInfo localInvalidateInfo = View.AttachInfo.InvalidateInfo.obtain();
      target = this;
      left = paramInt1;
      top = paramInt2;
      right = paramInt3;
      bottom = paramInt4;
      mViewRootImpl.dispatchInvalidateRectOnAnimation(localInvalidateInfo);
    }
  }
  
  public void postOnAnimation(Runnable paramRunnable)
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null) {
      mViewRootImpl.mChoreographer.postCallback(1, paramRunnable, null);
    } else {
      getRunQueue().post(paramRunnable);
    }
  }
  
  public void postOnAnimationDelayed(Runnable paramRunnable, long paramLong)
  {
    AttachInfo localAttachInfo = mAttachInfo;
    if (localAttachInfo != null) {
      mViewRootImpl.mChoreographer.postCallbackDelayed(1, paramRunnable, null, paramLong);
    } else {
      getRunQueue().postDelayed(paramRunnable, paramLong);
    }
  }
  
  protected void recomputePadding()
  {
    internalSetPadding(mUserPaddingLeft, mPaddingTop, mUserPaddingRight, mUserPaddingBottom);
  }
  
  public void refreshDrawableState()
  {
    mPrivateFlags |= 0x400;
    drawableStateChanged();
    ViewParent localViewParent = mParent;
    if (localViewParent != null) {
      localViewParent.childDrawableStateChanged(this);
    }
  }
  
  public void releasePointerCapture()
  {
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    if (localViewRootImpl != null) {
      localViewRootImpl.requestPointerCapture(false);
    }
  }
  
  public boolean removeCallbacks(Runnable paramRunnable)
  {
    if (paramRunnable != null)
    {
      AttachInfo localAttachInfo = mAttachInfo;
      if (localAttachInfo != null)
      {
        mHandler.removeCallbacks(paramRunnable);
        mViewRootImpl.mChoreographer.removeCallbacks(1, paramRunnable, null);
      }
      getRunQueue().removeCallbacks(paramRunnable);
    }
    return true;
  }
  
  public void removeFrameMetricsListener(Window.OnFrameMetricsAvailableListener paramOnFrameMetricsAvailableListener)
  {
    ThreadedRenderer localThreadedRenderer = getThreadedRenderer();
    paramOnFrameMetricsAvailableListener = findFrameMetricsObserver(paramOnFrameMetricsAvailableListener);
    if (paramOnFrameMetricsAvailableListener != null)
    {
      if (mFrameMetricsObservers != null)
      {
        mFrameMetricsObservers.remove(paramOnFrameMetricsAvailableListener);
        if (localThreadedRenderer != null) {
          localThreadedRenderer.removeFrameMetricsObserver(paramOnFrameMetricsAvailableListener);
        }
      }
      return;
    }
    throw new IllegalArgumentException("attempt to remove OnFrameMetricsAvailableListener that was never added");
  }
  
  public void removeOnAttachStateChangeListener(OnAttachStateChangeListener paramOnAttachStateChangeListener)
  {
    ListenerInfo localListenerInfo = mListenerInfo;
    if ((localListenerInfo != null) && (mOnAttachStateChangeListeners != null))
    {
      mOnAttachStateChangeListeners.remove(paramOnAttachStateChangeListener);
      return;
    }
  }
  
  public void removeOnLayoutChangeListener(OnLayoutChangeListener paramOnLayoutChangeListener)
  {
    ListenerInfo localListenerInfo = mListenerInfo;
    if ((localListenerInfo != null) && (mOnLayoutChangeListeners != null))
    {
      mOnLayoutChangeListeners.remove(paramOnLayoutChangeListener);
      return;
    }
  }
  
  public void removeOnUnhandledKeyEventListener(OnUnhandledKeyEventListener paramOnUnhandledKeyEventListener)
  {
    if ((mListenerInfo != null) && (mListenerInfo.mUnhandledKeyListeners != null) && (!mListenerInfo.mUnhandledKeyListeners.isEmpty()))
    {
      mListenerInfo.mUnhandledKeyListeners.remove(paramOnUnhandledKeyEventListener);
      if (mListenerInfo.mUnhandledKeyListeners.isEmpty())
      {
        ListenerInfo.access$3702(mListenerInfo, null);
        if ((mParent instanceof ViewGroup)) {
          ((ViewGroup)mParent).decrementChildUnhandledKeyListeners();
        }
      }
    }
  }
  
  public boolean requestAccessibilityFocus()
  {
    Object localObject = AccessibilityManager.getInstance(mContext);
    if ((((AccessibilityManager)localObject).isEnabled()) && (((AccessibilityManager)localObject).isTouchExplorationEnabled()))
    {
      if ((mViewFlags & 0xC) != 0) {
        return false;
      }
      if ((mPrivateFlags2 & 0x4000000) == 0)
      {
        mPrivateFlags2 |= 0x4000000;
        localObject = getViewRootImpl();
        if (localObject != null) {
          ((ViewRootImpl)localObject).setAccessibilityFocus(this, null);
        }
        invalidate();
        sendAccessibilityEvent(32768);
        return true;
      }
      return false;
    }
    return false;
  }
  
  public void requestApplyInsets()
  {
    requestFitSystemWindows();
  }
  
  @Deprecated
  public void requestFitSystemWindows()
  {
    if (mParent != null) {
      mParent.requestFitSystemWindows();
    }
  }
  
  public final boolean requestFocus()
  {
    return requestFocus(130);
  }
  
  public final boolean requestFocus(int paramInt)
  {
    return requestFocus(paramInt, null);
  }
  
  public boolean requestFocus(int paramInt, Rect paramRect)
  {
    return requestFocusNoSearch(paramInt, paramRect);
  }
  
  public final boolean requestFocusFromTouch()
  {
    if (isInTouchMode())
    {
      ViewRootImpl localViewRootImpl = getViewRootImpl();
      if (localViewRootImpl != null) {
        localViewRootImpl.ensureTouchMode(false);
      }
    }
    return requestFocus(130);
  }
  
  public void requestKeyboardShortcuts(List<KeyboardShortcutGroup> paramList, int paramInt) {}
  
  public void requestLayout()
  {
    if (mMeasureCache != null) {
      mMeasureCache.clear();
    }
    if ((mAttachInfo != null) && (mAttachInfo.mViewRequestingLayout == null))
    {
      ViewRootImpl localViewRootImpl = getViewRootImpl();
      if ((localViewRootImpl != null) && (localViewRootImpl.isInLayout()) && (!localViewRootImpl.requestLayoutDuringLayout(this))) {
        return;
      }
      mAttachInfo.mViewRequestingLayout = this;
    }
    mPrivateFlags |= 0x1000;
    mPrivateFlags |= 0x80000000;
    if ((mParent != null) && (!mParent.isLayoutRequested())) {
      mParent.requestLayout();
    }
    if ((mAttachInfo != null) && (mAttachInfo.mViewRequestingLayout == this)) {
      mAttachInfo.mViewRequestingLayout = null;
    }
  }
  
  public void requestPointerCapture()
  {
    ViewRootImpl localViewRootImpl = getViewRootImpl();
    if (localViewRootImpl != null) {
      localViewRootImpl.requestPointerCapture(true);
    }
  }
  
  public boolean requestRectangleOnScreen(Rect paramRect)
  {
    return requestRectangleOnScreen(paramRect, false);
  }
  
  public boolean requestRectangleOnScreen(Rect paramRect, boolean paramBoolean)
  {
    Object localObject = mParent;
    boolean bool1 = false;
    if (localObject == null) {
      return false;
    }
    View localView = this;
    if (mAttachInfo != null) {
      localObject = mAttachInfo.mTmpTransformRect;
    } else {
      localObject = new RectF();
    }
    ((RectF)localObject).set(paramRect);
    boolean bool2;
    for (ViewParent localViewParent = mParent;; localViewParent = localView.getParent())
    {
      bool2 = bool1;
      if (localViewParent == null) {
        break;
      }
      paramRect.set((int)left, (int)top, (int)right, (int)bottom);
      bool1 |= localViewParent.requestChildRectangleOnScreen(localView, paramRect, paramBoolean);
      if (!(localViewParent instanceof View))
      {
        bool2 = bool1;
        break;
      }
      ((RectF)localObject).offset(mLeft - localView.getScrollX(), mTop - localView.getScrollY());
      localView = (View)localViewParent;
    }
    return bool2;
  }
  
  public final void requestUnbufferedDispatch(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction();
    if ((mAttachInfo != null) && ((i == 0) || (i == 2)) && (paramMotionEvent.isTouchEvent()))
    {
      mAttachInfo.mUnbufferedDispatchRequested = true;
      return;
    }
  }
  
  public final <T extends View> T requireViewById(int paramInt)
  {
    View localView = findViewById(paramInt);
    if (localView != null) {
      return localView;
    }
    throw new IllegalArgumentException("ID does not reference a View inside this View");
  }
  
  public void resetPaddingToInitialValues()
  {
    if (isRtlCompatibilityMode())
    {
      mPaddingLeft = mUserPaddingLeftInitial;
      mPaddingRight = mUserPaddingRightInitial;
      return;
    }
    int i;
    if (isLayoutRtl())
    {
      if (mUserPaddingEnd >= 0) {
        i = mUserPaddingEnd;
      } else {
        i = mUserPaddingLeftInitial;
      }
      mPaddingLeft = i;
      if (mUserPaddingStart >= 0) {
        i = mUserPaddingStart;
      } else {
        i = mUserPaddingRightInitial;
      }
      mPaddingRight = i;
    }
    else
    {
      if (mUserPaddingStart >= 0) {
        i = mUserPaddingStart;
      } else {
        i = mUserPaddingLeftInitial;
      }
      mPaddingLeft = i;
      if (mUserPaddingEnd >= 0) {
        i = mUserPaddingEnd;
      } else {
        i = mUserPaddingRightInitial;
      }
      mPaddingRight = i;
    }
  }
  
  public void resetPivot()
  {
    if (mRenderNode.resetPivot()) {
      invalidateViewProperty(false, false);
    }
  }
  
  protected void resetResolvedDrawables()
  {
    resetResolvedDrawablesInternal();
  }
  
  void resetResolvedDrawablesInternal()
  {
    mPrivateFlags2 &= 0xBFFFFFFF;
  }
  
  public void resetResolvedLayoutDirection()
  {
    mPrivateFlags2 &= 0xFFFFFFCF;
  }
  
  public void resetResolvedPadding()
  {
    resetResolvedPaddingInternal();
  }
  
  void resetResolvedPaddingInternal()
  {
    mPrivateFlags2 &= 0xDFFFFFFF;
  }
  
  public void resetResolvedTextAlignment()
  {
    mPrivateFlags2 &= 0xFFF0FFFF;
    mPrivateFlags2 |= 0x20000;
  }
  
  public void resetResolvedTextDirection()
  {
    mPrivateFlags2 &= 0xE1FF;
    mPrivateFlags2 |= 0x400;
  }
  
  public void resetRtlProperties()
  {
    resetResolvedLayoutDirection();
    resetResolvedTextDirection();
    resetResolvedTextAlignment();
    resetResolvedPadding();
    resetResolvedDrawables();
  }
  
  void resetSubtreeAccessibilityStateChanged()
  {
    mPrivateFlags2 &= 0xF7FFFFFF;
  }
  
  protected void resolveDrawables()
  {
    if ((!isLayoutDirectionResolved()) && (getRawLayoutDirection() == 2)) {
      return;
    }
    int i;
    if (isLayoutDirectionResolved()) {
      i = getLayoutDirection();
    } else {
      i = getRawLayoutDirection();
    }
    if (mBackground != null) {
      mBackground.setLayoutDirection(i);
    }
    if ((mForegroundInfo != null) && (mForegroundInfo.mDrawable != null)) {
      mForegroundInfo.mDrawable.setLayoutDirection(i);
    }
    if (mDefaultFocusHighlight != null) {
      mDefaultFocusHighlight.setLayoutDirection(i);
    }
    mPrivateFlags2 |= 0x40000000;
    onResolveDrawables(i);
  }
  
  public boolean resolveLayoutDirection()
  {
    mPrivateFlags2 &= 0xFFFFFFCF;
    if (hasRtlSupport()) {
      switch ((mPrivateFlags2 & 0xC) >> 2)
      {
      default: 
        break;
      case 3: 
        if (1 == TextUtils.getLayoutDirectionFromLocale(Locale.getDefault())) {
          mPrivateFlags2 |= 0x10;
        }
        break;
      case 2: 
        if (!canResolveLayoutDirection()) {
          return false;
        }
        try
        {
          if (!mParent.isLayoutDirectionResolved()) {
            return false;
          }
          if (mParent.getLayoutDirection() == 1) {
            mPrivateFlags2 |= 0x10;
          }
        }
        catch (AbstractMethodError localAbstractMethodError)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(mParent.getClass().getSimpleName());
          localStringBuilder.append(" does not fully implement ViewParent");
          Log.e("View", localStringBuilder.toString(), localAbstractMethodError);
        }
      case 1: 
        mPrivateFlags2 |= 0x10;
      }
    }
    mPrivateFlags2 |= 0x20;
    return true;
  }
  
  public void resolveLayoutParams()
  {
    if (mLayoutParams != null) {
      mLayoutParams.resolveLayoutDirection(getLayoutDirection());
    }
  }
  
  public void resolvePadding()
  {
    int i = getLayoutDirection();
    if (!isRtlCompatibilityMode())
    {
      if ((mBackground != null) && ((!mLeftPaddingDefined) || (!mRightPaddingDefined)))
      {
        Rect localRect1 = (Rect)sThreadLocal.get();
        Rect localRect2 = localRect1;
        if (localRect1 == null)
        {
          localRect2 = new Rect();
          sThreadLocal.set(localRect2);
        }
        mBackground.getPadding(localRect2);
        if (!mLeftPaddingDefined) {
          mUserPaddingLeftInitial = left;
        }
        if (!mRightPaddingDefined) {
          mUserPaddingRightInitial = right;
        }
      }
      if (i != 1)
      {
        if (mUserPaddingStart != Integer.MIN_VALUE) {
          mUserPaddingLeft = mUserPaddingStart;
        } else {
          mUserPaddingLeft = mUserPaddingLeftInitial;
        }
        if (mUserPaddingEnd != Integer.MIN_VALUE) {
          mUserPaddingRight = mUserPaddingEnd;
        } else {
          mUserPaddingRight = mUserPaddingRightInitial;
        }
      }
      else
      {
        if (mUserPaddingStart != Integer.MIN_VALUE) {
          mUserPaddingRight = mUserPaddingStart;
        } else {
          mUserPaddingRight = mUserPaddingRightInitial;
        }
        if (mUserPaddingEnd != Integer.MIN_VALUE) {
          mUserPaddingLeft = mUserPaddingEnd;
        } else {
          mUserPaddingLeft = mUserPaddingLeftInitial;
        }
      }
      int j;
      if (mUserPaddingBottom >= 0) {
        j = mUserPaddingBottom;
      } else {
        j = mPaddingBottom;
      }
      mUserPaddingBottom = j;
    }
    internalSetPadding(mUserPaddingLeft, mPaddingTop, mUserPaddingRight, mUserPaddingBottom);
    onRtlPropertiesChanged(i);
    mPrivateFlags2 |= 0x20000000;
  }
  
  public boolean resolveRtlPropertiesIfNeeded()
  {
    if (!needRtlPropertiesResolution()) {
      return false;
    }
    if (!isLayoutDirectionResolved())
    {
      resolveLayoutDirection();
      resolveLayoutParams();
    }
    if (!isTextDirectionResolved()) {
      resolveTextDirection();
    }
    if (!isTextAlignmentResolved()) {
      resolveTextAlignment();
    }
    if (!areDrawablesResolved()) {
      resolveDrawables();
    }
    if (!isPaddingResolved()) {
      resolvePadding();
    }
    onRtlPropertiesChanged(getLayoutDirection());
    return true;
  }
  
  public boolean resolveTextAlignment()
  {
    mPrivateFlags2 &= 0xFFF0FFFF;
    if (hasRtlSupport())
    {
      int i = getRawTextAlignment();
      switch (i)
      {
      default: 
        mPrivateFlags2 = (0x20000 | mPrivateFlags2);
        break;
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
        mPrivateFlags2 |= i << 17;
        break;
      case 0: 
        if (!canResolveTextAlignment())
        {
          mPrivateFlags2 |= 0x20000;
          return false;
        }
        try
        {
          if (!mParent.isTextAlignmentResolved())
          {
            mPrivateFlags2 = (0x20000 | mPrivateFlags2);
            return false;
          }
          try
          {
            i = mParent.getTextAlignment();
          }
          catch (AbstractMethodError localAbstractMethodError1)
          {
            localStringBuilder = new StringBuilder();
            localStringBuilder.append(mParent.getClass().getSimpleName());
            localStringBuilder.append(" does not fully implement ViewParent");
            Log.e("View", localStringBuilder.toString(), localAbstractMethodError1);
            i = 1;
          }
          switch (i)
          {
          default: 
            mPrivateFlags2 = (0x20000 | mPrivateFlags2);
            break;
          case 1: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
          case 6: 
            mPrivateFlags2 |= i << 17;
          }
        }
        catch (AbstractMethodError localAbstractMethodError2)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(mParent.getClass().getSimpleName());
          localStringBuilder.append(" does not fully implement ViewParent");
          Log.e("View", localStringBuilder.toString(), localAbstractMethodError2);
          mPrivateFlags2 |= 0x30000;
          return true;
        }
      }
    }
    else
    {
      mPrivateFlags2 |= 0x20000;
    }
    mPrivateFlags2 |= 0x10000;
    return true;
  }
  
  public boolean resolveTextDirection()
  {
    mPrivateFlags2 &= 0xE1FF;
    if (hasRtlSupport())
    {
      int i = getRawTextDirection();
      switch (i)
      {
      default: 
        mPrivateFlags2 |= 0x400;
        break;
      case 1: 
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
        mPrivateFlags2 |= i << 10;
        break;
      case 0: 
        if (!canResolveTextDirection())
        {
          mPrivateFlags2 |= 0x400;
          return false;
        }
        try
        {
          if (!mParent.isTextDirectionResolved())
          {
            mPrivateFlags2 |= 0x400;
            return false;
          }
          try
          {
            i = mParent.getTextDirection();
          }
          catch (AbstractMethodError localAbstractMethodError1)
          {
            StringBuilder localStringBuilder2 = new StringBuilder();
            localStringBuilder2.append(mParent.getClass().getSimpleName());
            localStringBuilder2.append(" does not fully implement ViewParent");
            Log.e("View", localStringBuilder2.toString(), localAbstractMethodError1);
            i = 3;
          }
          switch (i)
          {
          default: 
            mPrivateFlags2 |= 0x400;
            break;
          case 1: 
          case 2: 
          case 3: 
          case 4: 
          case 5: 
          case 6: 
          case 7: 
            mPrivateFlags2 |= i << 10;
          }
        }
        catch (AbstractMethodError localAbstractMethodError2)
        {
          StringBuilder localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append(mParent.getClass().getSimpleName());
          localStringBuilder1.append(" does not fully implement ViewParent");
          Log.e("View", localStringBuilder1.toString(), localAbstractMethodError2);
          mPrivateFlags2 |= 0x600;
          return true;
        }
      }
    }
    else
    {
      mPrivateFlags2 |= 0x400;
    }
    mPrivateFlags2 |= 0x200;
    return true;
  }
  
  public boolean restoreDefaultFocus()
  {
    return requestFocus(130);
  }
  
  public boolean restoreFocusInCluster(int paramInt)
  {
    if (restoreDefaultFocus()) {
      return true;
    }
    return requestFocus(paramInt);
  }
  
  public boolean restoreFocusNotInCluster()
  {
    return requestFocus(130);
  }
  
  public void restoreHierarchyState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchRestoreInstanceState(paramSparseArray);
  }
  
  boolean rootViewRequestFocus()
  {
    View localView = getRootView();
    boolean bool;
    if ((localView != null) && (localView.requestFocus())) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void saveHierarchyState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchSaveInstanceState(paramSparseArray);
  }
  
  public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
  {
    if ((verifyDrawable(paramDrawable)) && (paramRunnable != null))
    {
      paramLong -= SystemClock.uptimeMillis();
      if (mAttachInfo != null) {
        mAttachInfo.mViewRootImpl.mChoreographer.postCallbackDelayed(1, paramRunnable, paramDrawable, Choreographer.subtractFrameDelay(paramLong));
      } else {
        getRunQueue().postDelayed(paramRunnable, paramLong);
      }
    }
  }
  
  public void scrollBy(int paramInt1, int paramInt2)
  {
    scrollTo(mScrollX + paramInt1, mScrollY + paramInt2);
  }
  
  public void scrollTo(int paramInt1, int paramInt2)
  {
    if ((mScrollX != paramInt1) || (mScrollY != paramInt2))
    {
      int i = mScrollX;
      int j = mScrollY;
      mScrollX = paramInt1;
      mScrollY = paramInt2;
      invalidateParentCaches();
      onScrollChanged(mScrollX, mScrollY, i, j);
      if (!awakenScrollBars()) {
        postInvalidateOnAnimation();
      }
    }
  }
  
  public void sendAccessibilityEvent(int paramInt)
  {
    if (mAccessibilityDelegate != null) {
      mAccessibilityDelegate.sendAccessibilityEvent(this, paramInt);
    } else {
      sendAccessibilityEventInternal(paramInt);
    }
  }
  
  public void sendAccessibilityEventInternal(int paramInt)
  {
    if (AccessibilityManager.getInstance(mContext).isEnabled()) {
      sendAccessibilityEventUnchecked(AccessibilityEvent.obtain(paramInt));
    }
  }
  
  public void sendAccessibilityEventUnchecked(AccessibilityEvent paramAccessibilityEvent)
  {
    if (mAccessibilityDelegate != null) {
      mAccessibilityDelegate.sendAccessibilityEventUnchecked(this, paramAccessibilityEvent);
    } else {
      sendAccessibilityEventUncheckedInternal(paramAccessibilityEvent);
    }
  }
  
  public void sendAccessibilityEventUncheckedInternal(AccessibilityEvent paramAccessibilityEvent)
  {
    int i = paramAccessibilityEvent.getEventType();
    int j = 0;
    int k;
    if (i == 32) {
      k = 1;
    } else {
      k = 0;
    }
    i = j;
    if (k != 0)
    {
      i = j;
      if ((0x20 & paramAccessibilityEvent.getContentChangeTypes()) != 0) {
        i = 1;
      }
    }
    if ((!isShown()) && (i == 0)) {
      return;
    }
    onInitializeAccessibilityEvent(paramAccessibilityEvent);
    if ((paramAccessibilityEvent.getEventType() & 0x2A1BF) != 0) {
      dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
    }
    if (getParent() != null) {
      getParent().requestSendAccessibilityEvent(this, paramAccessibilityEvent);
    }
  }
  
  public void setAccessibilityDelegate(AccessibilityDelegate paramAccessibilityDelegate)
  {
    mAccessibilityDelegate = paramAccessibilityDelegate;
  }
  
  public void setAccessibilityHeading(boolean paramBoolean)
  {
    updatePflags3AndNotifyA11yIfChanged(Integer.MIN_VALUE, paramBoolean);
  }
  
  public void setAccessibilityLiveRegion(int paramInt)
  {
    if (paramInt != getAccessibilityLiveRegion())
    {
      mPrivateFlags2 &= 0xFE7FFFFF;
      mPrivateFlags2 |= paramInt << 23 & 0x1800000;
      notifyViewAccessibilityStateChangedIfNeeded(0);
    }
  }
  
  public void setAccessibilityPaneTitle(CharSequence paramCharSequence)
  {
    if (!TextUtils.equals(paramCharSequence, mAccessibilityPaneTitle))
    {
      mAccessibilityPaneTitle = paramCharSequence;
      notifyViewAccessibilityStateChangedIfNeeded(8);
    }
  }
  
  public void setAccessibilitySelection(int paramInt1, int paramInt2)
  {
    if ((paramInt1 == paramInt2) && (paramInt2 == mAccessibilityCursorPosition)) {
      return;
    }
    if ((paramInt1 >= 0) && (paramInt1 == paramInt2) && (paramInt2 <= getIterableTextForAccessibility().length())) {
      mAccessibilityCursorPosition = paramInt1;
    } else {
      mAccessibilityCursorPosition = -1;
    }
    sendAccessibilityEvent(8192);
  }
  
  @RemotableViewMethod
  public void setAccessibilityTraversalAfter(int paramInt)
  {
    if (mAccessibilityTraversalAfterId == paramInt) {
      return;
    }
    mAccessibilityTraversalAfterId = paramInt;
    notifyViewAccessibilityStateChangedIfNeeded(0);
  }
  
  @RemotableViewMethod
  public void setAccessibilityTraversalBefore(int paramInt)
  {
    if (mAccessibilityTraversalBeforeId == paramInt) {
      return;
    }
    mAccessibilityTraversalBeforeId = paramInt;
    notifyViewAccessibilityStateChangedIfNeeded(0);
  }
  
  public void setActivated(boolean paramBoolean)
  {
    int i = mPrivateFlags;
    int j = 1073741824;
    boolean bool;
    if ((i & 0x40000000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool != paramBoolean)
    {
      i = mPrivateFlags;
      if (!paramBoolean) {
        j = 0;
      }
      mPrivateFlags = (i & 0xBFFFFFFF | j);
      invalidate(true);
      refreshDrawableState();
      dispatchSetActivated(paramBoolean);
    }
  }
  
  public void setAlpha(float paramFloat)
  {
    ensureTransformationInfo();
    if (mTransformationInfo.mAlpha != paramFloat)
    {
      setAlphaInternal(paramFloat);
      if (onSetAlpha((int)(255.0F * paramFloat)))
      {
        mPrivateFlags |= 0x40000;
        invalidateParentCaches();
        invalidate(true);
      }
      else
      {
        mPrivateFlags &= 0xFFFBFFFF;
        invalidateViewProperty(true, false);
        mRenderNode.setAlpha(getFinalAlpha());
      }
    }
  }
  
  boolean setAlphaNoInvalidation(float paramFloat)
  {
    ensureTransformationInfo();
    if (mTransformationInfo.mAlpha != paramFloat)
    {
      setAlphaInternal(paramFloat);
      if (onSetAlpha((int)(255.0F * paramFloat)))
      {
        mPrivateFlags |= 0x40000;
        return true;
      }
      mPrivateFlags &= 0xFFFBFFFF;
      mRenderNode.setAlpha(getFinalAlpha());
    }
    return false;
  }
  
  public void setAnimation(Animation paramAnimation)
  {
    mCurrentAnimation = paramAnimation;
    if (paramAnimation != null)
    {
      if ((mAttachInfo != null) && (mAttachInfo.mDisplayState == 1) && (paramAnimation.getStartTime() == -1L)) {
        paramAnimation.setStartTime(AnimationUtils.currentAnimationTimeMillis());
      }
      paramAnimation.reset();
    }
  }
  
  public void setAnimationMatrix(Matrix paramMatrix)
  {
    invalidateViewProperty(true, false);
    mRenderNode.setAnimationMatrix(paramMatrix);
    invalidateViewProperty(false, true);
    invalidateParentIfNeededAndWasQuickRejected();
  }
  
  public void setAssistBlocked(boolean paramBoolean)
  {
    if (paramBoolean) {
      mPrivateFlags3 |= 0x4000;
    } else {
      mPrivateFlags3 &= 0xBFFF;
    }
  }
  
  public void setAutofillHints(String... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0)) {
      mAutofillHints = paramVarArgs;
    } else {
      mAutofillHints = null;
    }
  }
  
  public void setAutofillId(AutofillId paramAutofillId)
  {
    if (Helper.sVerbose)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("setAutofill(): from ");
      localStringBuilder.append(mAutofillId);
      localStringBuilder.append(" to ");
      localStringBuilder.append(paramAutofillId);
      Log.v("View", localStringBuilder.toString());
    }
    if (!isAttachedToWindow())
    {
      if ((paramAutofillId != null) && (paramAutofillId.isVirtual())) {
        throw new IllegalStateException("Cannot set autofill id assigned to virtual views");
      }
      if ((paramAutofillId == null) && ((mPrivateFlags3 & 0x40000000) == 0)) {
        return;
      }
      mAutofillId = paramAutofillId;
      if (paramAutofillId != null)
      {
        mAutofillViewId = paramAutofillId.getViewId();
        mPrivateFlags3 = (0x40000000 | mPrivateFlags3);
      }
      else
      {
        mAutofillViewId = -1;
        mPrivateFlags3 &= 0xBFFFFFFF;
      }
      return;
    }
    throw new IllegalStateException("Cannot set autofill id when view is attached");
  }
  
  public void setAutofilled(boolean paramBoolean)
  {
    int i;
    if (paramBoolean != isAutofilled()) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0)
    {
      if (paramBoolean) {
        mPrivateFlags3 |= 0x10000;
      } else {
        mPrivateFlags3 &= 0xFFFEFFFF;
      }
      invalidate();
    }
  }
  
  public void setBackground(Drawable paramDrawable)
  {
    setBackgroundDrawable(paramDrawable);
  }
  
  void setBackgroundBounds()
  {
    if ((mBackgroundSizeChanged) && (mBackground != null))
    {
      mBackground.setBounds(0, 0, mRight - mLeft, mBottom - mTop);
      mBackgroundSizeChanged = false;
      rebuildOutline();
    }
  }
  
  @RemotableViewMethod
  public void setBackgroundColor(int paramInt)
  {
    if ((mBackground instanceof ColorDrawable))
    {
      ((ColorDrawable)mBackground.mutate()).setColor(paramInt);
      computeOpaqueFlags();
      mBackgroundResource = 0;
    }
    else
    {
      setBackground(new ColorDrawable(paramInt));
    }
  }
  
  @Deprecated
  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    computeOpaqueFlags();
    if (paramDrawable == mBackground) {
      return;
    }
    int i = 0;
    mBackgroundResource = 0;
    if (mBackground != null)
    {
      if (isAttachedToWindow()) {
        mBackground.setVisible(false, false);
      }
      mBackground.setCallback(null);
      unscheduleDrawable(mBackground);
    }
    if (paramDrawable != null)
    {
      Rect localRect1 = (Rect)sThreadLocal.get();
      Rect localRect2 = localRect1;
      if (localRect1 == null)
      {
        localRect2 = new Rect();
        sThreadLocal.set(localRect2);
      }
      resetResolvedDrawablesInternal();
      paramDrawable.setLayoutDirection(getLayoutDirection());
      if (paramDrawable.getPadding(localRect2))
      {
        resetResolvedPaddingInternal();
        if (paramDrawable.getLayoutDirection() != 1)
        {
          mUserPaddingLeftInitial = left;
          mUserPaddingRightInitial = right;
          internalSetPadding(left, top, right, bottom);
        }
        else
        {
          mUserPaddingLeftInitial = right;
          mUserPaddingRightInitial = left;
          internalSetPadding(right, top, left, bottom);
        }
        mLeftPaddingDefined = false;
        mRightPaddingDefined = false;
      }
      if ((mBackground == null) || (mBackground.getMinimumHeight() != paramDrawable.getMinimumHeight()) || (mBackground.getMinimumWidth() != paramDrawable.getMinimumWidth())) {
        i = 1;
      }
      mBackground = paramDrawable;
      if (paramDrawable.isStateful()) {
        paramDrawable.setState(getDrawableState());
      }
      if (isAttachedToWindow())
      {
        boolean bool;
        if ((getWindowVisibility() == 0) && (isShown())) {
          bool = true;
        } else {
          bool = false;
        }
        paramDrawable.setVisible(bool, false);
      }
      applyBackgroundTint();
      paramDrawable.setCallback(this);
      if ((mPrivateFlags & 0x80) != 0)
      {
        mPrivateFlags &= 0xFF7F;
        i = 1;
      }
    }
    else
    {
      mBackground = null;
      if (((mViewFlags & 0x80) != 0) && (mDefaultFocusHighlight == null) && ((mForegroundInfo == null) || (mForegroundInfo.mDrawable == null))) {
        mPrivateFlags |= 0x80;
      }
      i = 1;
    }
    computeOpaqueFlags();
    if (i != 0) {
      requestLayout();
    }
    mBackgroundSizeChanged = true;
    invalidate(true);
    invalidateOutline();
  }
  
  @RemotableViewMethod
  public void setBackgroundResource(int paramInt)
  {
    if ((paramInt != 0) && (paramInt == mBackgroundResource)) {
      return;
    }
    Drawable localDrawable = null;
    if (paramInt != 0) {
      localDrawable = mContext.getDrawable(paramInt);
    }
    setBackground(localDrawable);
    mBackgroundResource = paramInt;
  }
  
  public void setBackgroundTintList(ColorStateList paramColorStateList)
  {
    if (mBackgroundTint == null) {
      mBackgroundTint = new TintInfo();
    }
    mBackgroundTint.mTintList = paramColorStateList;
    mBackgroundTint.mHasTintList = true;
    applyBackgroundTint();
  }
  
  public void setBackgroundTintMode(PorterDuff.Mode paramMode)
  {
    if (mBackgroundTint == null) {
      mBackgroundTint = new TintInfo();
    }
    mBackgroundTint.mTintMode = paramMode;
    mBackgroundTint.mHasTintMode = true;
    applyBackgroundTint();
  }
  
  public final void setBottom(int paramInt)
  {
    if (paramInt != mBottom)
    {
      boolean bool = hasIdentityMatrix();
      if (bool)
      {
        if (mAttachInfo != null)
        {
          if (paramInt < mBottom) {
            i = mBottom;
          } else {
            i = paramInt;
          }
          invalidate(0, 0, mRight - mLeft, i - mTop);
        }
      }
      else {
        invalidate(true);
      }
      int j = mRight - mLeft;
      int k = mBottom;
      int i = mTop;
      mBottom = paramInt;
      mRenderNode.setBottom(mBottom);
      sizeChange(j, mBottom - mTop, j, k - i);
      if (!bool)
      {
        mPrivateFlags |= 0x20;
        invalidate(true);
      }
      mBackgroundSizeChanged = true;
      mDefaultFocusHighlightSizeChanged = true;
      if (mForegroundInfo != null) {
        ForegroundInfo.access$1802(mForegroundInfo, true);
      }
      invalidateParentIfNeeded();
      if ((mPrivateFlags2 & 0x10000000) == 268435456) {
        invalidateParentIfNeeded();
      }
    }
  }
  
  public void setCameraDistance(float paramFloat)
  {
    float f = mResources.getDisplayMetrics().densityDpi;
    invalidateViewProperty(true, false);
    mRenderNode.setCameraDistance(-Math.abs(paramFloat) / f);
    invalidateViewProperty(false, false);
    invalidateParentIfNeededAndWasQuickRejected();
  }
  
  public void setClickable(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 16384;
    } else {
      i = 0;
    }
    setFlags(i, 16384);
  }
  
  public void setClipBounds(Rect paramRect)
  {
    if ((paramRect != mClipBounds) && ((paramRect == null) || (!paramRect.equals(mClipBounds))))
    {
      if (paramRect != null)
      {
        if (mClipBounds == null) {
          mClipBounds = new Rect(paramRect);
        } else {
          mClipBounds.set(paramRect);
        }
      }
      else {
        mClipBounds = null;
      }
      mRenderNode.setClipBounds(mClipBounds);
      invalidateViewProperty(false, false);
      return;
    }
  }
  
  public void setClipToOutline(boolean paramBoolean)
  {
    damageInParent();
    if (getClipToOutline() != paramBoolean) {
      mRenderNode.setClipToOutline(paramBoolean);
    }
  }
  
  @RemotableViewMethod
  public void setContentDescription(CharSequence paramCharSequence)
  {
    if (mContentDescription == null)
    {
      if (paramCharSequence != null) {}
    }
    else if (mContentDescription.equals(paramCharSequence)) {
      return;
    }
    mContentDescription = paramCharSequence;
    int i;
    if ((paramCharSequence != null) && (paramCharSequence.length() > 0)) {
      i = 1;
    } else {
      i = 0;
    }
    if ((i != 0) && (getImportantForAccessibility() == 0))
    {
      setImportantForAccessibility(1);
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
    else
    {
      notifyViewAccessibilityStateChangedIfNeeded(4);
    }
  }
  
  public void setContextClickable(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 8388608;
    } else {
      i = 0;
    }
    setFlags(i, 8388608);
  }
  
  public void setDefaultFocusHighlightEnabled(boolean paramBoolean)
  {
    mDefaultFocusHighlightEnabled = paramBoolean;
  }
  
  public void setDisabledSystemUiVisibility(int paramInt)
  {
    if ((mAttachInfo != null) && (mAttachInfo.mDisabledSystemUiVisibility != paramInt))
    {
      mAttachInfo.mDisabledSystemUiVisibility = paramInt;
      if (mParent != null) {
        mParent.recomputeViewAttributes(this);
      }
    }
  }
  
  void setDisplayListProperties(RenderNode paramRenderNode)
  {
    if (paramRenderNode != null)
    {
      paramRenderNode.setHasOverlappingRendering(getHasOverlappingRendering());
      boolean bool;
      if (((mParent instanceof ViewGroup)) && (((ViewGroup)mParent).getClipChildren())) {
        bool = true;
      } else {
        bool = false;
      }
      paramRenderNode.setClipToBounds(bool);
      float f1 = 1.0F;
      float f2 = f1;
      if ((mParent instanceof ViewGroup))
      {
        f2 = f1;
        if ((mParent).mGroupFlags & 0x800) != 0)
        {
          ViewGroup localViewGroup = (ViewGroup)mParent;
          Transformation localTransformation = localViewGroup.getChildTransformation();
          f2 = f1;
          if (localViewGroup.getChildStaticTransformation(this, localTransformation))
          {
            int i = localTransformation.getTransformationType();
            f2 = f1;
            if (i != 0)
            {
              if ((i & 0x1) != 0) {
                f1 = localTransformation.getAlpha();
              }
              f2 = f1;
              if ((i & 0x2) != 0)
              {
                paramRenderNode.setStaticMatrix(localTransformation.getMatrix());
                f2 = f1;
              }
            }
          }
        }
      }
      if (mTransformationInfo != null)
      {
        f2 *= getFinalAlpha();
        f1 = f2;
        if (f2 < 1.0F)
        {
          f1 = f2;
          if (onSetAlpha((int)(255.0F * f2))) {
            f1 = 1.0F;
          }
        }
        paramRenderNode.setAlpha(f1);
      }
      else if (f2 < 1.0F)
      {
        paramRenderNode.setAlpha(f2);
      }
    }
  }
  
  @Deprecated
  public void setDrawingCacheBackgroundColor(int paramInt)
  {
    if (paramInt != mDrawingCacheBackgroundColor)
    {
      mDrawingCacheBackgroundColor = paramInt;
      mPrivateFlags &= 0xFFFF7FFF;
    }
  }
  
  @Deprecated
  public void setDrawingCacheEnabled(boolean paramBoolean)
  {
    int i = 0;
    mCachingFailed = false;
    if (paramBoolean) {
      i = 32768;
    }
    setFlags(i, 32768);
  }
  
  @Deprecated
  public void setDrawingCacheQuality(int paramInt)
  {
    setFlags(paramInt, 1572864);
  }
  
  public void setDuplicateParentStateEnabled(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 4194304;
    } else {
      i = 0;
    }
    setFlags(i, 4194304);
  }
  
  public void setElevation(float paramFloat)
  {
    if (paramFloat != getElevation())
    {
      paramFloat = sanitizeFloatPropertyValue(paramFloat, "elevation");
      invalidateViewProperty(true, false);
      mRenderNode.setElevation(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
    }
  }
  
  @RemotableViewMethod
  public void setEnabled(boolean paramBoolean)
  {
    if (paramBoolean == isEnabled()) {
      return;
    }
    int i;
    if (paramBoolean) {
      i = 0;
    } else {
      i = 32;
    }
    setFlags(i, 32);
    refreshDrawableState();
    invalidate(true);
    if (!paramBoolean) {
      cancelPendingInputEvents();
    }
  }
  
  public void setFadingEdgeLength(int paramInt)
  {
    initScrollCache();
    mScrollCache.fadingEdgeLength = paramInt;
  }
  
  public void setFilterTouchesWhenObscured(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 1024;
    } else {
      i = 0;
    }
    setFlags(i, 1024);
  }
  
  public void setFitsSystemWindows(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 2;
    } else {
      i = 0;
    }
    setFlags(i, 2);
  }
  
  void setFlags(int paramInt1, int paramInt2)
  {
    boolean bool1 = AccessibilityManager.getInstance(mContext).isEnabled();
    int i;
    if ((bool1) && (includeForAccessibility())) {
      i = 1;
    } else {
      i = 0;
    }
    int j = mViewFlags;
    mViewFlags = (mViewFlags & paramInt2 | paramInt1 & paramInt2);
    int k = mViewFlags ^ j;
    if (k == 0) {
      return;
    }
    int m = mPrivateFlags;
    boolean bool2 = false;
    int n = 0;
    paramInt2 = k;
    int i1 = n;
    if ((mViewFlags & 0x10) != 0)
    {
      paramInt2 = k;
      i1 = n;
      if ((k & 0x4011) != 0)
      {
        if ((mViewFlags & 0x4000) != 0) {
          paramInt2 = 1;
        } else {
          paramInt2 = 0;
        }
        mViewFlags = (mViewFlags & 0xFFFFFFFE | paramInt2);
        i1 = j & 0x1 ^ paramInt2 & 0x1;
        paramInt2 = k & 0xFFFFFFFE | i1;
      }
    }
    boolean bool3 = bool2;
    if ((paramInt2 & 0x1) != 0)
    {
      bool3 = bool2;
      if ((m & 0x10) != 0) {
        if (((j & 0x1) == 1) && ((m & 0x2) != 0))
        {
          clearFocus();
          bool3 = bool2;
          if ((mParent instanceof ViewGroup))
          {
            ((ViewGroup)mParent).clearFocusedInCluster();
            bool3 = bool2;
          }
        }
        else
        {
          bool3 = bool2;
          if ((j & 0x1) == 0)
          {
            bool3 = bool2;
            if ((m & 0x2) == 0)
            {
              bool3 = bool2;
              if (mParent != null)
              {
                ViewRootImpl localViewRootImpl = getViewRootImpl();
                if ((sAutoFocusableOffUIThreadWontNotifyParents) && (i1 != 0) && (localViewRootImpl != null))
                {
                  bool3 = bool2;
                  if (mThread != Thread.currentThread()) {}
                }
                else
                {
                  bool3 = canTakeFocus();
                }
              }
            }
          }
        }
      }
    }
    paramInt1 &= 0xC;
    bool2 = bool3;
    if (paramInt1 == 0)
    {
      bool2 = bool3;
      if ((paramInt2 & 0xC) != 0)
      {
        mPrivateFlags |= 0x20;
        invalidate(true);
        needGlobalAttributesUpdate(true);
        bool2 = hasSize();
      }
    }
    bool3 = bool2;
    if ((paramInt2 & 0x20) != 0) {
      if ((mViewFlags & 0x20) == 0)
      {
        bool3 = canTakeFocus();
      }
      else
      {
        bool3 = bool2;
        if (isFocused())
        {
          clearFocus();
          bool3 = bool2;
        }
      }
    }
    if ((bool3) && (mParent != null)) {
      mParent.focusableViewAvailable(this);
    }
    if ((paramInt2 & 0x8) != 0)
    {
      needGlobalAttributesUpdate(false);
      requestLayout();
      if ((mViewFlags & 0xC) == 8)
      {
        if (hasFocus())
        {
          clearFocus();
          if ((mParent instanceof ViewGroup)) {
            ((ViewGroup)mParent).clearFocusedInCluster();
          }
        }
        clearAccessibilityFocus();
        destroyDrawingCache();
        if ((mParent instanceof View)) {
          ((View)mParent).invalidate(true);
        }
        mPrivateFlags |= 0x20;
      }
      if (mAttachInfo != null) {
        mAttachInfo.mViewVisibilityChanged = true;
      }
    }
    if ((paramInt2 & 0x4) != 0)
    {
      needGlobalAttributesUpdate(false);
      mPrivateFlags |= 0x20;
      if (((mViewFlags & 0xC) == 4) && (getRootView() != this))
      {
        if (hasFocus())
        {
          clearFocus();
          if ((mParent instanceof ViewGroup)) {
            ((ViewGroup)mParent).clearFocusedInCluster();
          }
        }
        clearAccessibilityFocus();
      }
      if (mAttachInfo != null) {
        mAttachInfo.mViewVisibilityChanged = true;
      }
    }
    if ((paramInt2 & 0xC) != 0)
    {
      if ((paramInt1 != 0) && (mAttachInfo != null)) {
        cleanupDraw();
      }
      if ((mParent instanceof ViewGroup))
      {
        ((ViewGroup)mParent).onChildVisibilityChanged(this, paramInt2 & 0xC, paramInt1);
        ((View)mParent).invalidate(true);
      }
      else if (mParent != null)
      {
        mParent.invalidateChild(this, null);
      }
      if (mAttachInfo != null)
      {
        dispatchVisibilityChanged(this, paramInt1);
        if ((mParent != null) && (getWindowVisibility() == 0) && ((!(mParent instanceof ViewGroup)) || (((ViewGroup)mParent).isShown())))
        {
          if (paramInt1 == 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          dispatchVisibilityAggregated(bool2);
        }
        notifySubtreeAccessibilityStateChangedIfNeeded();
      }
    }
    if ((0x20000 & paramInt2) != 0) {
      destroyDrawingCache();
    }
    if ((0x8000 & paramInt2) != 0)
    {
      destroyDrawingCache();
      mPrivateFlags &= 0xFFFF7FFF;
      invalidateParentCaches();
    }
    if ((0x180000 & paramInt2) != 0)
    {
      destroyDrawingCache();
      mPrivateFlags &= 0xFFFF7FFF;
    }
    if ((paramInt2 & 0x80) != 0)
    {
      if ((mViewFlags & 0x80) != 0)
      {
        if ((mBackground == null) && (mDefaultFocusHighlight == null) && ((mForegroundInfo == null) || (mForegroundInfo.mDrawable == null))) {
          mPrivateFlags |= 0x80;
        } else {
          mPrivateFlags &= 0xFF7F;
        }
      }
      else {
        mPrivateFlags &= 0xFF7F;
      }
      requestLayout();
      invalidate(true);
    }
    if (((0x4000000 & paramInt2) != 0) && (mParent != null) && (mAttachInfo != null) && (!mAttachInfo.mRecomputeGlobalAttributes)) {
      mParent.recomputeViewAttributes(this);
    }
    if (bool1)
    {
      paramInt1 = paramInt2;
      if (isAccessibilityPane()) {
        paramInt1 = paramInt2 & 0xFFFFFFF3;
      }
      if (((paramInt1 & 0x1) == 0) && ((paramInt1 & 0xC) == 0) && ((paramInt1 & 0x4000) == 0) && ((0x200000 & paramInt1) == 0) && ((0x800000 & paramInt1) == 0))
      {
        if ((paramInt1 & 0x20) != 0) {
          notifyViewAccessibilityStateChangedIfNeeded(0);
        }
      }
      else if (i != includeForAccessibility()) {
        notifySubtreeAccessibilityStateChangedIfNeeded();
      } else {
        notifyViewAccessibilityStateChangedIfNeeded(0);
      }
    }
  }
  
  public void setFocusable(int paramInt)
  {
    if ((paramInt & 0x11) == 0) {
      setFlags(0, 262144);
    }
    setFlags(paramInt, 17);
  }
  
  public void setFocusable(boolean paramBoolean)
  {
    setFocusable(paramBoolean);
  }
  
  public void setFocusableInTouchMode(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 262144;
    } else {
      i = 0;
    }
    setFlags(i, 262144);
    if (paramBoolean) {
      setFlags(1, 17);
    }
  }
  
  public void setFocusedByDefault(boolean paramBoolean)
  {
    boolean bool;
    if ((mPrivateFlags3 & 0x40000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (paramBoolean == bool) {
      return;
    }
    if (paramBoolean) {
      mPrivateFlags3 |= 0x40000;
    } else {
      mPrivateFlags3 &= 0xFFFBFFFF;
    }
    if ((mParent instanceof ViewGroup)) {
      if (paramBoolean) {
        ((ViewGroup)mParent).setDefaultFocus(this);
      } else {
        ((ViewGroup)mParent).clearDefaultFocus(this);
      }
    }
  }
  
  public final void setFocusedInCluster()
  {
    setFocusedInCluster(findKeyboardNavigationCluster());
  }
  
  public void setForeground(Drawable paramDrawable)
  {
    if (mForegroundInfo == null)
    {
      if (paramDrawable == null) {
        return;
      }
      mForegroundInfo = new ForegroundInfo(null);
    }
    if (paramDrawable == mForegroundInfo.mDrawable) {
      return;
    }
    if (mForegroundInfo.mDrawable != null)
    {
      if (isAttachedToWindow()) {
        mForegroundInfo.mDrawable.setVisible(false, false);
      }
      mForegroundInfo.mDrawable.setCallback(null);
      unscheduleDrawable(mForegroundInfo.mDrawable);
    }
    ForegroundInfo.access$1202(mForegroundInfo, paramDrawable);
    ForegroundInfo localForegroundInfo = mForegroundInfo;
    boolean bool = true;
    ForegroundInfo.access$1802(localForegroundInfo, true);
    if (paramDrawable != null)
    {
      if ((mPrivateFlags & 0x80) != 0) {
        mPrivateFlags &= 0xFF7F;
      }
      paramDrawable.setLayoutDirection(getLayoutDirection());
      if (paramDrawable.isStateful()) {
        paramDrawable.setState(getDrawableState());
      }
      applyForegroundTint();
      if (isAttachedToWindow())
      {
        if ((getWindowVisibility() != 0) || (!isShown())) {
          bool = false;
        }
        paramDrawable.setVisible(bool, false);
      }
      paramDrawable.setCallback(this);
    }
    else if (((mViewFlags & 0x80) != 0) && (mBackground == null) && (mDefaultFocusHighlight == null))
    {
      mPrivateFlags |= 0x80;
    }
    requestLayout();
    invalidate();
  }
  
  public void setForegroundGravity(int paramInt)
  {
    if (mForegroundInfo == null) {
      mForegroundInfo = new ForegroundInfo(null);
    }
    if (mForegroundInfo.mGravity != paramInt)
    {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0) {
        i = paramInt | 0x800003;
      }
      paramInt = i;
      if ((i & 0x70) == 0) {
        paramInt = i | 0x30;
      }
      ForegroundInfo.access$2202(mForegroundInfo, paramInt);
      requestLayout();
    }
  }
  
  public void setForegroundTintList(ColorStateList paramColorStateList)
  {
    if (mForegroundInfo == null) {
      mForegroundInfo = new ForegroundInfo(null);
    }
    if (mForegroundInfo.mTintInfo == null) {
      ForegroundInfo.access$2302(mForegroundInfo, new TintInfo());
    }
    mForegroundInfo.mTintInfo.mTintList = paramColorStateList;
    mForegroundInfo.mTintInfo.mHasTintList = true;
    applyForegroundTint();
  }
  
  public void setForegroundTintMode(PorterDuff.Mode paramMode)
  {
    if (mForegroundInfo == null) {
      mForegroundInfo = new ForegroundInfo(null);
    }
    if (mForegroundInfo.mTintInfo == null) {
      ForegroundInfo.access$2302(mForegroundInfo, new TintInfo());
    }
    mForegroundInfo.mTintInfo.mTintMode = paramMode;
    mForegroundInfo.mTintInfo.mHasTintMode = true;
    applyForegroundTint();
  }
  
  protected boolean setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool1 = false;
    if ((mLeft != paramInt1) || (mRight != paramInt3) || (mTop != paramInt2) || (mBottom != paramInt4))
    {
      boolean bool2 = true;
      int i = mPrivateFlags;
      int j = mRight - mLeft;
      int k = mBottom - mTop;
      int m = paramInt3 - paramInt1;
      int n = paramInt4 - paramInt2;
      if ((m == j) && (n == k)) {
        bool1 = false;
      } else {
        bool1 = true;
      }
      invalidate(bool1);
      mLeft = paramInt1;
      mTop = paramInt2;
      mRight = paramInt3;
      mBottom = paramInt4;
      mRenderNode.setLeftTopRightBottom(mLeft, mTop, mRight, mBottom);
      mPrivateFlags |= 0x10;
      if (bool1) {
        sizeChange(m, n, j, k);
      }
      if (((mViewFlags & 0xC) == 0) || (mGhostView != null))
      {
        mPrivateFlags |= 0x20;
        invalidate(bool1);
        invalidateParentCaches();
      }
      mPrivateFlags |= i & 0x20;
      mBackgroundSizeChanged = true;
      mDefaultFocusHighlightSizeChanged = true;
      if (mForegroundInfo != null) {
        ForegroundInfo.access$1802(mForegroundInfo, true);
      }
      notifySubtreeAccessibilityStateChangedIfNeeded();
      bool1 = bool2;
    }
    return bool1;
  }
  
  public void setHapticFeedbackEnabled(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 268435456;
    } else {
      i = 0;
    }
    setFlags(i, 268435456);
  }
  
  public void setHasTransientState(boolean paramBoolean)
  {
    boolean bool = hasTransientState();
    if (paramBoolean) {
      i = mTransientStateCount + 1;
    } else {
      i = mTransientStateCount - 1;
    }
    mTransientStateCount = i;
    int j = mTransientStateCount;
    int i = 0;
    if (j < 0)
    {
      mTransientStateCount = 0;
      Log.e("View", "hasTransientState decremented below 0: unmatched pair of setHasTransientState calls");
    }
    else if (((paramBoolean) && (mTransientStateCount == 1)) || ((!paramBoolean) && (mTransientStateCount == 0)))
    {
      j = mPrivateFlags2;
      if (paramBoolean) {
        i = Integer.MIN_VALUE;
      }
      mPrivateFlags2 = (j & 0x7FFFFFFF | i);
      paramBoolean = hasTransientState();
      if ((mParent != null) && (paramBoolean != bool)) {
        try
        {
          mParent.childHasTransientStateChanged(this, paramBoolean);
        }
        catch (AbstractMethodError localAbstractMethodError)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(mParent.getClass().getSimpleName());
          localStringBuilder.append(" does not fully implement ViewParent");
          Log.e("View", localStringBuilder.toString(), localAbstractMethodError);
        }
      }
    }
  }
  
  public void setHorizontalFadingEdgeEnabled(boolean paramBoolean)
  {
    if (isHorizontalFadingEdgeEnabled() != paramBoolean)
    {
      if (paramBoolean) {
        initScrollCache();
      }
      mViewFlags ^= 0x1000;
    }
  }
  
  public void setHorizontalScrollBarEnabled(boolean paramBoolean)
  {
    if (isHorizontalScrollBarEnabled() != paramBoolean)
    {
      mViewFlags ^= 0x100;
      computeOpaqueFlags();
      resolvePadding();
    }
  }
  
  public void setHovered(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if ((mPrivateFlags & 0x10000000) == 0)
      {
        mPrivateFlags = (0x10000000 | mPrivateFlags);
        refreshDrawableState();
        onHoverChanged(true);
      }
    }
    else if ((0x10000000 & mPrivateFlags) != 0)
    {
      mPrivateFlags &= 0xEFFFFFFF;
      refreshDrawableState();
      onHoverChanged(false);
    }
  }
  
  public void setId(int paramInt)
  {
    mID = paramInt;
    if ((mID == -1) && (mLabelForId != -1)) {
      mID = generateViewId();
    }
  }
  
  public void setImportantForAccessibility(int paramInt)
  {
    int i = getImportantForAccessibility();
    if (paramInt != i)
    {
      boolean bool1 = true;
      boolean bool2;
      if (paramInt == 4) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      if ((paramInt == 2) || (bool2))
      {
        View localView = findAccessibilityFocusHost(bool2);
        if (localView != null) {
          localView.clearAccessibilityFocus();
        }
      }
      if ((i != 0) && (paramInt != 0)) {
        i = 0;
      } else {
        i = 1;
      }
      if ((i != 0) && (includeForAccessibility())) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      mPrivateFlags2 &= 0xFF8FFFFF;
      mPrivateFlags2 |= paramInt << 20 & 0x700000;
      if ((i != 0) && (bool2 == includeForAccessibility())) {
        notifyViewAccessibilityStateChangedIfNeeded(0);
      } else {
        notifySubtreeAccessibilityStateChangedIfNeeded();
      }
    }
  }
  
  public void setImportantForAutofill(int paramInt)
  {
    mPrivateFlags3 &= 0xFF87FFFF;
    mPrivateFlags3 |= paramInt << 19 & 0x780000;
  }
  
  public void setIsRootNamespace(boolean paramBoolean)
  {
    if (paramBoolean) {
      mPrivateFlags |= 0x8;
    } else {
      mPrivateFlags &= 0xFFFFFFF7;
    }
  }
  
  public void setKeepScreenOn(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 67108864;
    } else {
      i = 0;
    }
    setFlags(i, 67108864);
  }
  
  public void setKeyboardNavigationCluster(boolean paramBoolean)
  {
    if (paramBoolean) {
      mPrivateFlags3 |= 0x8000;
    } else {
      mPrivateFlags3 &= 0xFFFF7FFF;
    }
  }
  
  @RemotableViewMethod
  public void setLabelFor(int paramInt)
  {
    if (mLabelForId == paramInt) {
      return;
    }
    mLabelForId = paramInt;
    if ((mLabelForId != -1) && (mID == -1)) {
      mID = generateViewId();
    }
    notifyViewAccessibilityStateChangedIfNeeded(0);
  }
  
  public void setLayerPaint(Paint paramPaint)
  {
    int i = getLayerType();
    if (i != 0)
    {
      mLayerPaint = paramPaint;
      if (i == 2)
      {
        if (mRenderNode.setLayerPaint(paramPaint)) {
          invalidateViewProperty(false, false);
        }
      }
      else {
        invalidate();
      }
    }
  }
  
  public void setLayerType(int paramInt, Paint paramPaint)
  {
    if ((paramInt >= 0) && (paramInt <= 2))
    {
      if (!mRenderNode.setLayerType(paramInt))
      {
        setLayerPaint(paramPaint);
        return;
      }
      if (paramInt != 1) {
        destroyDrawingCache();
      }
      mLayerType = paramInt;
      if (mLayerType == 0) {
        paramPaint = null;
      }
      mLayerPaint = paramPaint;
      mRenderNode.setLayerPaint(mLayerPaint);
      invalidateParentCaches();
      invalidate(true);
      return;
    }
    throw new IllegalArgumentException("Layer type can only be one of: LAYER_TYPE_NONE, LAYER_TYPE_SOFTWARE or LAYER_TYPE_HARDWARE");
  }
  
  @RemotableViewMethod
  public void setLayoutDirection(int paramInt)
  {
    if (getRawLayoutDirection() != paramInt)
    {
      mPrivateFlags2 &= 0xFFFFFFF3;
      resetRtlProperties();
      mPrivateFlags2 |= paramInt << 2 & 0xC;
      resolveRtlPropertiesIfNeeded();
      requestLayout();
      invalidate(true);
    }
  }
  
  public void setLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (paramLayoutParams != null)
    {
      mLayoutParams = paramLayoutParams;
      resolveLayoutParams();
      if ((mParent instanceof ViewGroup)) {
        ((ViewGroup)mParent).onSetLayoutParams(this, paramLayoutParams);
      }
      requestLayout();
      return;
    }
    throw new NullPointerException("Layout parameters cannot be null");
  }
  
  public final void setLeft(int paramInt)
  {
    if (paramInt != mLeft)
    {
      boolean bool = hasIdentityMatrix();
      if (bool)
      {
        if (mAttachInfo != null)
        {
          if (paramInt < mLeft)
          {
            i = paramInt;
            j = paramInt - mLeft;
          }
          else
          {
            i = mLeft;
            j = 0;
          }
          invalidate(j, 0, mRight - i, mBottom - mTop);
        }
      }
      else {
        invalidate(true);
      }
      int j = mRight;
      int i = mLeft;
      int k = mBottom - mTop;
      mLeft = paramInt;
      mRenderNode.setLeft(paramInt);
      sizeChange(mRight - mLeft, k, j - i, k);
      if (!bool)
      {
        mPrivateFlags |= 0x20;
        invalidate(true);
      }
      mBackgroundSizeChanged = true;
      mDefaultFocusHighlightSizeChanged = true;
      if (mForegroundInfo != null) {
        ForegroundInfo.access$1802(mForegroundInfo, true);
      }
      invalidateParentIfNeeded();
      if ((mPrivateFlags2 & 0x10000000) == 268435456) {
        invalidateParentIfNeeded();
      }
    }
  }
  
  public void setLeftTopRightBottom(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    setFrame(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setLongClickable(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 2097152;
    } else {
      i = 0;
    }
    setFlags(i, 2097152);
  }
  
  protected final void setMeasuredDimension(int paramInt1, int paramInt2)
  {
    boolean bool = isLayoutModeOptical(this);
    int i = paramInt1;
    int j = paramInt2;
    if (bool != isLayoutModeOptical(mParent))
    {
      Insets localInsets = getOpticalInsets();
      j = left + right;
      int k = top + bottom;
      if (!bool) {
        j = -j;
      }
      i = paramInt1 + j;
      if (bool) {
        paramInt1 = k;
      } else {
        paramInt1 = -k;
      }
      j = paramInt2 + paramInt1;
    }
    setMeasuredDimensionRaw(i, j);
  }
  
  @RemotableViewMethod
  public void setMinimumHeight(int paramInt)
  {
    mMinHeight = paramInt;
    requestLayout();
  }
  
  public void setMinimumWidth(int paramInt)
  {
    mMinWidth = paramInt;
    requestLayout();
  }
  
  public void setNestedScrollingEnabled(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      mPrivateFlags3 |= 0x80;
    }
    else
    {
      stopNestedScroll();
      mPrivateFlags3 &= 0xFF7F;
    }
  }
  
  public void setNextClusterForwardId(int paramInt)
  {
    mNextClusterForwardId = paramInt;
  }
  
  public void setNextFocusDownId(int paramInt)
  {
    mNextFocusDownId = paramInt;
  }
  
  public void setNextFocusForwardId(int paramInt)
  {
    mNextFocusForwardId = paramInt;
  }
  
  public void setNextFocusLeftId(int paramInt)
  {
    mNextFocusLeftId = paramInt;
  }
  
  public void setNextFocusRightId(int paramInt)
  {
    mNextFocusRightId = paramInt;
  }
  
  public void setNextFocusUpId(int paramInt)
  {
    mNextFocusUpId = paramInt;
  }
  
  public void setNotifyAutofillManagerOnClick(boolean paramBoolean)
  {
    if (paramBoolean) {
      mPrivateFlags |= 0x20000000;
    } else {
      mPrivateFlags &= 0xDFFFFFFF;
    }
  }
  
  public void setOnApplyWindowInsetsListener(OnApplyWindowInsetsListener paramOnApplyWindowInsetsListener)
  {
    getListenerInfomOnApplyWindowInsetsListener = paramOnApplyWindowInsetsListener;
  }
  
  public void setOnCapturedPointerListener(OnCapturedPointerListener paramOnCapturedPointerListener)
  {
    getListenerInfomOnCapturedPointerListener = paramOnCapturedPointerListener;
  }
  
  public void setOnClickListener(OnClickListener paramOnClickListener)
  {
    if (!isClickable()) {
      setClickable(true);
    }
    getListenerInfomOnClickListener = paramOnClickListener;
  }
  
  public void setOnContextClickListener(OnContextClickListener paramOnContextClickListener)
  {
    if (!isContextClickable()) {
      setContextClickable(true);
    }
    getListenerInfomOnContextClickListener = paramOnContextClickListener;
  }
  
  public void setOnCreateContextMenuListener(OnCreateContextMenuListener paramOnCreateContextMenuListener)
  {
    if (!isLongClickable()) {
      setLongClickable(true);
    }
    getListenerInfomOnCreateContextMenuListener = paramOnCreateContextMenuListener;
  }
  
  public void setOnDragListener(OnDragListener paramOnDragListener)
  {
    ListenerInfo.access$802(getListenerInfo(), paramOnDragListener);
  }
  
  public void setOnFocusChangeListener(OnFocusChangeListener paramOnFocusChangeListener)
  {
    getListenerInfomOnFocusChangeListener = paramOnFocusChangeListener;
  }
  
  public void setOnGenericMotionListener(OnGenericMotionListener paramOnGenericMotionListener)
  {
    ListenerInfo.access$602(getListenerInfo(), paramOnGenericMotionListener);
  }
  
  public void setOnHoverListener(OnHoverListener paramOnHoverListener)
  {
    ListenerInfo.access$702(getListenerInfo(), paramOnHoverListener);
  }
  
  public void setOnKeyListener(OnKeyListener paramOnKeyListener)
  {
    ListenerInfo.access$402(getListenerInfo(), paramOnKeyListener);
  }
  
  public void setOnLongClickListener(OnLongClickListener paramOnLongClickListener)
  {
    if (!isLongClickable()) {
      setLongClickable(true);
    }
    getListenerInfomOnLongClickListener = paramOnLongClickListener;
  }
  
  public void setOnScrollChangeListener(OnScrollChangeListener paramOnScrollChangeListener)
  {
    getListenerInfomOnScrollChangeListener = paramOnScrollChangeListener;
  }
  
  public void setOnSystemUiVisibilityChangeListener(OnSystemUiVisibilityChangeListener paramOnSystemUiVisibilityChangeListener)
  {
    ListenerInfo.access$1402(getListenerInfo(), paramOnSystemUiVisibilityChangeListener);
    if ((mParent != null) && (mAttachInfo != null) && (!mAttachInfo.mRecomputeGlobalAttributes)) {
      mParent.recomputeViewAttributes(this);
    }
  }
  
  public void setOnTouchListener(OnTouchListener paramOnTouchListener)
  {
    ListenerInfo.access$502(getListenerInfo(), paramOnTouchListener);
  }
  
  public void setOpticalInsets(Insets paramInsets)
  {
    mLayoutInsets = paramInsets;
  }
  
  public void setOutlineAmbientShadowColor(int paramInt)
  {
    if (mRenderNode.setAmbientShadowColor(paramInt)) {
      invalidateViewProperty(true, true);
    }
  }
  
  public void setOutlineProvider(ViewOutlineProvider paramViewOutlineProvider)
  {
    mOutlineProvider = paramViewOutlineProvider;
    invalidateOutline();
  }
  
  public void setOutlineSpotShadowColor(int paramInt)
  {
    if (mRenderNode.setSpotShadowColor(paramInt)) {
      invalidateViewProperty(true, true);
    }
  }
  
  public void setOverScrollMode(int paramInt)
  {
    if ((paramInt != 0) && (paramInt != 1) && (paramInt != 2))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Invalid overscroll mode ");
      localStringBuilder.append(paramInt);
      throw new IllegalArgumentException(localStringBuilder.toString());
    }
    mOverScrollMode = paramInt;
  }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    resetResolvedPaddingInternal();
    mUserPaddingStart = Integer.MIN_VALUE;
    mUserPaddingEnd = Integer.MIN_VALUE;
    mUserPaddingLeftInitial = paramInt1;
    mUserPaddingRightInitial = paramInt3;
    mLeftPaddingDefined = true;
    mRightPaddingDefined = true;
    internalSetPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void setPaddingRelative(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    resetResolvedPaddingInternal();
    mUserPaddingStart = paramInt1;
    mUserPaddingEnd = paramInt3;
    mLeftPaddingDefined = true;
    mRightPaddingDefined = true;
    if (getLayoutDirection() != 1)
    {
      mUserPaddingLeftInitial = paramInt1;
      mUserPaddingRightInitial = paramInt3;
      internalSetPadding(paramInt1, paramInt2, paramInt3, paramInt4);
    }
    else
    {
      mUserPaddingLeftInitial = paramInt3;
      mUserPaddingRightInitial = paramInt1;
      internalSetPadding(paramInt3, paramInt2, paramInt1, paramInt4);
    }
  }
  
  public void setPivotX(float paramFloat)
  {
    if ((!mRenderNode.isPivotExplicitlySet()) || (paramFloat != getPivotX()))
    {
      invalidateViewProperty(true, false);
      mRenderNode.setPivotX(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
    }
  }
  
  public void setPivotY(float paramFloat)
  {
    if ((!mRenderNode.isPivotExplicitlySet()) || (paramFloat != getPivotY()))
    {
      invalidateViewProperty(true, false);
      mRenderNode.setPivotY(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
    }
  }
  
  public void setPointerIcon(PointerIcon paramPointerIcon)
  {
    mPointerIcon = paramPointerIcon;
    if ((mAttachInfo != null) && (!mAttachInfo.mHandlingPointerEvent))
    {
      try
      {
        mAttachInfo.mSession.updatePointerIcon(mAttachInfo.mWindow);
      }
      catch (RemoteException paramPointerIcon) {}
      return;
    }
  }
  
  public void setPressed(boolean paramBoolean)
  {
    int i = mPrivateFlags;
    int j = 0;
    boolean bool;
    if ((i & 0x4000) == 16384) {
      bool = true;
    } else {
      bool = false;
    }
    if (paramBoolean != bool) {
      j = 1;
    }
    if (paramBoolean) {
      mPrivateFlags = (0x4000 | mPrivateFlags);
    } else {
      mPrivateFlags &= 0xBFFF;
    }
    if (j != 0) {
      refreshDrawableState();
    }
    dispatchSetPressed(paramBoolean);
  }
  
  public void setRevealClip(boolean paramBoolean, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    mRenderNode.setRevealClip(paramBoolean, paramFloat1, paramFloat2, paramFloat3);
    invalidateViewProperty(false, false);
  }
  
  public final void setRevealOnFocusHint(boolean paramBoolean)
  {
    if (paramBoolean) {
      mPrivateFlags3 &= 0xFBFFFFFF;
    } else {
      mPrivateFlags3 |= 0x4000000;
    }
  }
  
  public final void setRight(int paramInt)
  {
    if (paramInt != mRight)
    {
      boolean bool = hasIdentityMatrix();
      if (bool)
      {
        if (mAttachInfo != null)
        {
          if (paramInt < mRight) {
            i = mRight;
          } else {
            i = paramInt;
          }
          invalidate(0, 0, i - mLeft, mBottom - mTop);
        }
      }
      else {
        invalidate(true);
      }
      int i = mRight;
      int j = mLeft;
      int k = mBottom - mTop;
      mRight = paramInt;
      mRenderNode.setRight(mRight);
      sizeChange(mRight - mLeft, k, i - j, k);
      if (!bool)
      {
        mPrivateFlags |= 0x20;
        invalidate(true);
      }
      mBackgroundSizeChanged = true;
      mDefaultFocusHighlightSizeChanged = true;
      if (mForegroundInfo != null) {
        ForegroundInfo.access$1802(mForegroundInfo, true);
      }
      invalidateParentIfNeeded();
      if ((mPrivateFlags2 & 0x10000000) == 268435456) {
        invalidateParentIfNeeded();
      }
    }
  }
  
  public void setRotation(float paramFloat)
  {
    if (paramFloat != getRotation())
    {
      invalidateViewProperty(true, false);
      mRenderNode.setRotation(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void setRotationX(float paramFloat)
  {
    if (paramFloat != getRotationX())
    {
      invalidateViewProperty(true, false);
      mRenderNode.setRotationX(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void setRotationY(float paramFloat)
  {
    if (paramFloat != getRotationY())
    {
      invalidateViewProperty(true, false);
      mRenderNode.setRotationY(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void setSaveEnabled(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 0;
    } else {
      i = 65536;
    }
    setFlags(i, 65536);
  }
  
  public void setSaveFromParentEnabled(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 0;
    } else {
      i = 536870912;
    }
    setFlags(i, 536870912);
  }
  
  public void setScaleX(float paramFloat)
  {
    if (paramFloat != getScaleX())
    {
      paramFloat = sanitizeFloatPropertyValue(paramFloat, "scaleX");
      invalidateViewProperty(true, false);
      mRenderNode.setScaleX(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void setScaleY(float paramFloat)
  {
    if (paramFloat != getScaleY())
    {
      paramFloat = sanitizeFloatPropertyValue(paramFloat, "scaleY");
      invalidateViewProperty(true, false);
      mRenderNode.setScaleY(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void setScreenReaderFocusable(boolean paramBoolean)
  {
    updatePflags3AndNotifyA11yIfChanged(268435456, paramBoolean);
  }
  
  public void setScrollBarDefaultDelayBeforeFade(int paramInt)
  {
    getScrollCachescrollBarDefaultDelayBeforeFade = paramInt;
  }
  
  public void setScrollBarFadeDuration(int paramInt)
  {
    getScrollCachescrollBarFadeDuration = paramInt;
  }
  
  public void setScrollBarSize(int paramInt)
  {
    getScrollCachescrollBarSize = paramInt;
  }
  
  public void setScrollBarStyle(int paramInt)
  {
    if (paramInt != (mViewFlags & 0x3000000))
    {
      mViewFlags = (mViewFlags & 0xFCFFFFFF | 0x3000000 & paramInt);
      computeOpaqueFlags();
      resolvePadding();
    }
  }
  
  public void setScrollContainer(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      if ((mAttachInfo != null) && ((mPrivateFlags & 0x100000) == 0))
      {
        mAttachInfo.mScrollContainers.add(this);
        mPrivateFlags = (0x100000 | mPrivateFlags);
      }
      mPrivateFlags |= 0x80000;
    }
    else
    {
      if ((0x100000 & mPrivateFlags) != 0) {
        mAttachInfo.mScrollContainers.remove(this);
      }
      mPrivateFlags &= 0xFFE7FFFF;
    }
  }
  
  public void setScrollIndicators(int paramInt)
  {
    setScrollIndicators(paramInt, 63);
  }
  
  public void setScrollIndicators(int paramInt1, int paramInt2)
  {
    paramInt2 = paramInt2 << 8 & 0x3F00;
    paramInt1 = paramInt1 << 8 & paramInt2;
    paramInt2 = mPrivateFlags3 & paramInt2 | paramInt1;
    if (mPrivateFlags3 != paramInt2)
    {
      mPrivateFlags3 = paramInt2;
      if (paramInt1 != 0) {
        initializeScrollIndicatorsInternal();
      }
      invalidate();
    }
  }
  
  public void setScrollX(int paramInt)
  {
    scrollTo(paramInt, mScrollY);
  }
  
  public void setScrollY(int paramInt)
  {
    scrollTo(mScrollX, paramInt);
  }
  
  public void setScrollbarFadingEnabled(boolean paramBoolean)
  {
    initScrollCache();
    ScrollabilityCache localScrollabilityCache = mScrollCache;
    fadeScrollBars = paramBoolean;
    if (paramBoolean) {
      state = 0;
    } else {
      state = 1;
    }
  }
  
  public void setSelected(boolean paramBoolean)
  {
    boolean bool;
    if ((mPrivateFlags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    if (bool != paramBoolean)
    {
      int i = mPrivateFlags;
      int j;
      if (paramBoolean) {
        j = 4;
      } else {
        j = 0;
      }
      mPrivateFlags = (i & 0xFFFFFFFB | j);
      if (!paramBoolean) {
        resetPressedState();
      }
      invalidate(true);
      refreshDrawableState();
      dispatchSetSelected(paramBoolean);
      if (paramBoolean) {
        sendAccessibilityEvent(4);
      } else {
        notifyViewAccessibilityStateChangedIfNeeded(0);
      }
    }
  }
  
  public void setSoundEffectsEnabled(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 134217728;
    } else {
      i = 0;
    }
    setFlags(i, 134217728);
  }
  
  public void setStateListAnimator(StateListAnimator paramStateListAnimator)
  {
    if (mStateListAnimator == paramStateListAnimator) {
      return;
    }
    if (mStateListAnimator != null) {
      mStateListAnimator.setTarget(null);
    }
    mStateListAnimator = paramStateListAnimator;
    if (paramStateListAnimator != null)
    {
      paramStateListAnimator.setTarget(this);
      if (isAttachedToWindow()) {
        paramStateListAnimator.setState(getDrawableState());
      }
    }
  }
  
  public void setSystemUiVisibility(int paramInt)
  {
    if (paramInt != mSystemUiVisibility)
    {
      mSystemUiVisibility = paramInt;
      if ((mParent != null) && (mAttachInfo != null) && (!mAttachInfo.mRecomputeGlobalAttributes)) {
        mParent.recomputeViewAttributes(this);
      }
    }
  }
  
  public void setTag(int paramInt, Object paramObject)
  {
    if (paramInt >>> 24 >= 2)
    {
      setKeyedTag(paramInt, paramObject);
      return;
    }
    throw new IllegalArgumentException("The key must be an application-specific resource id.");
  }
  
  public void setTag(Object paramObject)
  {
    mTag = paramObject;
  }
  
  public void setTagInternal(int paramInt, Object paramObject)
  {
    if (paramInt >>> 24 == 1)
    {
      setKeyedTag(paramInt, paramObject);
      return;
    }
    throw new IllegalArgumentException("The key must be a framework-specific resource id.");
  }
  
  public void setTextAlignment(int paramInt)
  {
    if (paramInt != getRawTextAlignment())
    {
      mPrivateFlags2 &= 0xFFFF1FFF;
      resetResolvedTextAlignment();
      mPrivateFlags2 |= paramInt << 13 & 0xE000;
      resolveTextAlignment();
      onRtlPropertiesChanged(getLayoutDirection());
      requestLayout();
      invalidate(true);
    }
  }
  
  public void setTextDirection(int paramInt)
  {
    if (getRawTextDirection() != paramInt)
    {
      mPrivateFlags2 &= 0xFE3F;
      resetResolvedTextDirection();
      mPrivateFlags2 |= paramInt << 6 & 0x1C0;
      resolveTextDirection();
      onRtlPropertiesChanged(getLayoutDirection());
      requestLayout();
      invalidate(true);
    }
  }
  
  public void setTooltip(CharSequence paramCharSequence)
  {
    setTooltipText(paramCharSequence);
  }
  
  public void setTooltipText(CharSequence paramCharSequence)
  {
    if (TextUtils.isEmpty(paramCharSequence))
    {
      setFlags(0, 1073741824);
      hideTooltip();
      mTooltipInfo = null;
    }
    else
    {
      setFlags(1073741824, 1073741824);
      if (mTooltipInfo == null)
      {
        mTooltipInfo = new TooltipInfo(null);
        mTooltipInfo.mShowTooltipRunnable = new _..Lambda.View.llq76MkPXP4bNcb9oJt_msw0fnQ(this);
        mTooltipInfo.mHideTooltipRunnable = new _..Lambda.QI1s392qW8l6mC24bcy9050SkuY(this);
        mTooltipInfo.mHoverSlop = ViewConfiguration.get(mContext).getScaledHoverSlop();
        mTooltipInfo.clearAnchorPos();
      }
      mTooltipInfo.mTooltipText = paramCharSequence;
    }
  }
  
  public final void setTop(int paramInt)
  {
    if (paramInt != mTop)
    {
      boolean bool = hasIdentityMatrix();
      if (bool)
      {
        if (mAttachInfo != null)
        {
          if (paramInt < mTop)
          {
            i = paramInt;
            j = paramInt - mTop;
          }
          else
          {
            i = mTop;
            j = 0;
          }
          invalidate(0, j, mRight - mLeft, mBottom - i);
        }
      }
      else {
        invalidate(true);
      }
      int i = mRight - mLeft;
      int k = mBottom;
      int j = mTop;
      mTop = paramInt;
      mRenderNode.setTop(mTop);
      sizeChange(i, mBottom - mTop, i, k - j);
      if (!bool)
      {
        mPrivateFlags |= 0x20;
        invalidate(true);
      }
      mBackgroundSizeChanged = true;
      mDefaultFocusHighlightSizeChanged = true;
      if (mForegroundInfo != null) {
        ForegroundInfo.access$1802(mForegroundInfo, true);
      }
      invalidateParentIfNeeded();
      if ((mPrivateFlags2 & 0x10000000) == 268435456) {
        invalidateParentIfNeeded();
      }
    }
  }
  
  public void setTouchDelegate(TouchDelegate paramTouchDelegate)
  {
    mTouchDelegate = paramTouchDelegate;
  }
  
  public void setTransitionAlpha(float paramFloat)
  {
    ensureTransformationInfo();
    if (mTransformationInfo.mTransitionAlpha != paramFloat)
    {
      mTransformationInfo.mTransitionAlpha = paramFloat;
      mPrivateFlags &= 0xFFFBFFFF;
      invalidateViewProperty(true, false);
      mRenderNode.setAlpha(getFinalAlpha());
    }
  }
  
  public final void setTransitionName(String paramString)
  {
    mTransitionName = paramString;
  }
  
  public void setTransitionVisibility(int paramInt)
  {
    mViewFlags = (mViewFlags & 0xFFFFFFF3 | paramInt);
  }
  
  public void setTranslationX(float paramFloat)
  {
    if (paramFloat != getTranslationX())
    {
      invalidateViewProperty(true, false);
      mRenderNode.setTranslationX(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void setTranslationY(float paramFloat)
  {
    if (paramFloat != getTranslationY())
    {
      invalidateViewProperty(true, false);
      mRenderNode.setTranslationY(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
      notifySubtreeAccessibilityStateChangedIfNeeded();
    }
  }
  
  public void setTranslationZ(float paramFloat)
  {
    if (paramFloat != getTranslationZ())
    {
      paramFloat = sanitizeFloatPropertyValue(paramFloat, "translationZ");
      invalidateViewProperty(true, false);
      mRenderNode.setTranslationZ(paramFloat);
      invalidateViewProperty(false, true);
      invalidateParentIfNeededAndWasQuickRejected();
    }
  }
  
  public void setVerticalFadingEdgeEnabled(boolean paramBoolean)
  {
    if (isVerticalFadingEdgeEnabled() != paramBoolean)
    {
      if (paramBoolean) {
        initScrollCache();
      }
      mViewFlags ^= 0x2000;
    }
  }
  
  public void setVerticalScrollBarEnabled(boolean paramBoolean)
  {
    if (isVerticalScrollBarEnabled() != paramBoolean)
    {
      mViewFlags ^= 0x200;
      computeOpaqueFlags();
      resolvePadding();
    }
  }
  
  public void setVerticalScrollbarPosition(int paramInt)
  {
    if (mVerticalScrollbarPosition != paramInt)
    {
      mVerticalScrollbarPosition = paramInt;
      computeOpaqueFlags();
      resolvePadding();
    }
  }
  
  @RemotableViewMethod
  public void setVisibility(int paramInt)
  {
    setFlags(paramInt, 12);
  }
  
  @Deprecated
  public void setWillNotCacheDrawing(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 131072;
    } else {
      i = 0;
    }
    setFlags(i, 131072);
  }
  
  public void setWillNotDraw(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 128;
    } else {
      i = 0;
    }
    setFlags(i, 128);
  }
  
  public void setX(float paramFloat)
  {
    setTranslationX(paramFloat - mLeft);
  }
  
  public void setY(float paramFloat)
  {
    setTranslationY(paramFloat - mTop);
  }
  
  public void setZ(float paramFloat)
  {
    setTranslationZ(paramFloat - getElevation());
  }
  
  boolean shouldDrawRoundScrollbar()
  {
    boolean bool1 = mResources.getConfiguration().isScreenRound();
    boolean bool2 = false;
    if ((bool1) && (mAttachInfo != null))
    {
      View localView = getRootView();
      WindowInsets localWindowInsets = getRootWindowInsets();
      int i = getHeight();
      int j = getWidth();
      int k = localView.getHeight();
      int m = localView.getWidth();
      if ((i == k) && (j == m))
      {
        getLocationInWindow(mAttachInfo.mTmpLocation);
        if ((mAttachInfo.mTmpLocation[0] == localWindowInsets.getStableInsetLeft()) && (mAttachInfo.mTmpLocation[1] == localWindowInsets.getStableInsetTop())) {
          bool2 = true;
        }
        return bool2;
      }
      return false;
    }
    return false;
  }
  
  public boolean showContextMenu()
  {
    return getParent().showContextMenuForChild(this);
  }
  
  public boolean showContextMenu(float paramFloat1, float paramFloat2)
  {
    return getParent().showContextMenuForChild(this, paramFloat1, paramFloat2);
  }
  
  public ActionMode startActionMode(ActionMode.Callback paramCallback)
  {
    return startActionMode(paramCallback, 0);
  }
  
  public ActionMode startActionMode(ActionMode.Callback paramCallback, int paramInt)
  {
    ViewParent localViewParent = getParent();
    if (localViewParent == null) {
      return null;
    }
    try
    {
      ActionMode localActionMode = localViewParent.startActionModeForChild(this, paramCallback, paramInt);
      return localActionMode;
    }
    catch (AbstractMethodError localAbstractMethodError) {}
    return localViewParent.startActionModeForChild(this, paramCallback);
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("@android:view:");
    localStringBuilder.append(System.identityHashCode(this));
    mStartActivityRequestWho = localStringBuilder.toString();
    getContext().startActivityForResult(mStartActivityRequestWho, paramIntent, paramInt, null);
  }
  
  public void startAnimation(Animation paramAnimation)
  {
    paramAnimation.setStartTime(-1L);
    setAnimation(paramAnimation);
    invalidateParentCaches();
    invalidate(true);
  }
  
  @Deprecated
  public final boolean startDrag(ClipData paramClipData, DragShadowBuilder paramDragShadowBuilder, Object paramObject, int paramInt)
  {
    return startDragAndDrop(paramClipData, paramDragShadowBuilder, paramObject, paramInt);
  }
  
  /* Error */
  public final boolean startDragAndDrop(ClipData paramClipData, DragShadowBuilder paramDragShadowBuilder, Object paramObject, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4: ifnonnull +15 -> 19
    //   7: ldc_w 652
    //   10: ldc_w 7002
    //   13: invokestatic 1943	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   16: pop
    //   17: iconst_0
    //   18: ireturn
    //   19: aload_1
    //   20: ifnull +30 -> 50
    //   23: iload 4
    //   25: sipush 256
    //   28: iand
    //   29: ifeq +9 -> 38
    //   32: iconst_1
    //   33: istore 5
    //   35: goto +6 -> 41
    //   38: iconst_0
    //   39: istore 5
    //   41: aload_1
    //   42: iload 5
    //   44: invokevirtual 7007	android/content/ClipData:prepareToLeaveProcess	(Z)V
    //   47: goto +3 -> 50
    //   50: new 5148	android/graphics/Point
    //   53: dup
    //   54: invokespecial 5244	android/graphics/Point:<init>	()V
    //   57: astore 6
    //   59: new 5148	android/graphics/Point
    //   62: dup
    //   63: invokespecial 5244	android/graphics/Point:<init>	()V
    //   66: astore 7
    //   68: aload_2
    //   69: aload 6
    //   71: aload 7
    //   73: invokevirtual 7011	android/view/View$DragShadowBuilder:onProvideShadowMetrics	(Landroid/graphics/Point;Landroid/graphics/Point;)V
    //   76: aload 6
    //   78: getfield 5246	android/graphics/Point:x	I
    //   81: iflt +529 -> 610
    //   84: aload 6
    //   86: getfield 5248	android/graphics/Point:y	I
    //   89: iflt +521 -> 610
    //   92: aload 7
    //   94: getfield 5246	android/graphics/Point:x	I
    //   97: iflt +513 -> 610
    //   100: aload 7
    //   102: getfield 5248	android/graphics/Point:y	I
    //   105: iflt +505 -> 610
    //   108: aload 6
    //   110: getfield 5246	android/graphics/Point:x	I
    //   113: ifeq +11 -> 124
    //   116: aload 6
    //   118: getfield 5248	android/graphics/Point:y	I
    //   121: ifne +21 -> 142
    //   124: getstatic 1273	android/view/View:sAcceptZeroSizeDragShadow	Z
    //   127: ifeq +472 -> 599
    //   130: aload 6
    //   132: iconst_1
    //   133: putfield 5246	android/graphics/Point:x	I
    //   136: aload 6
    //   138: iconst_1
    //   139: putfield 5248	android/graphics/Point:y	I
    //   142: aload_0
    //   143: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   146: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   149: ifnull +13 -> 162
    //   152: aload_0
    //   153: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   156: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   159: invokevirtual 7020	android/view/Surface:release	()V
    //   162: aload_0
    //   163: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   166: new 7017	android/view/Surface
    //   169: dup
    //   170: invokespecial 7021	android/view/Surface:<init>	()V
    //   173: putfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   176: aload_0
    //   177: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   180: aconst_null
    //   181: putfield 3816	android/view/View$AttachInfo:mDragToken	Landroid/os/IBinder;
    //   184: aload_0
    //   185: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   188: getfield 1994	android/view/View$AttachInfo:mViewRootImpl	Landroid/view/ViewRootImpl;
    //   191: astore 8
    //   193: new 7023	android/view/SurfaceSession
    //   196: dup
    //   197: aload 8
    //   199: getfield 7026	android/view/ViewRootImpl:mSurface	Landroid/view/Surface;
    //   202: invokespecial 7029	android/view/SurfaceSession:<init>	(Landroid/view/Surface;)V
    //   205: astore 9
    //   207: new 7031	android/view/SurfaceControl$Builder
    //   210: dup
    //   211: aload 9
    //   213: invokespecial 7034	android/view/SurfaceControl$Builder:<init>	(Landroid/view/SurfaceSession;)V
    //   216: ldc_w 7036
    //   219: invokevirtual 7040	android/view/SurfaceControl$Builder:setName	(Ljava/lang/String;)Landroid/view/SurfaceControl$Builder;
    //   222: aload 6
    //   224: getfield 5246	android/graphics/Point:x	I
    //   227: aload 6
    //   229: getfield 5248	android/graphics/Point:y	I
    //   232: invokevirtual 7044	android/view/SurfaceControl$Builder:setSize	(II)Landroid/view/SurfaceControl$Builder;
    //   235: bipush -3
    //   237: invokevirtual 7048	android/view/SurfaceControl$Builder:setFormat	(I)Landroid/view/SurfaceControl$Builder;
    //   240: invokevirtual 7052	android/view/SurfaceControl$Builder:build	()Landroid/view/SurfaceControl;
    //   243: astore 10
    //   245: aload_0
    //   246: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   249: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   252: aload 10
    //   254: invokevirtual 7056	android/view/Surface:copyFrom	(Landroid/view/SurfaceControl;)V
    //   257: aload_0
    //   258: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   261: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   264: aconst_null
    //   265: invokevirtual 7060	android/view/Surface:lockCanvas	(Landroid/graphics/Rect;)Landroid/graphics/Canvas;
    //   268: astore 11
    //   270: aload 11
    //   272: iconst_0
    //   273: getstatic 7065	android/graphics/PorterDuff$Mode:CLEAR	Landroid/graphics/PorterDuff$Mode;
    //   276: invokevirtual 7069	android/graphics/Canvas:drawColor	(ILandroid/graphics/PorterDuff$Mode;)V
    //   279: aload_2
    //   280: aload 11
    //   282: invokevirtual 7072	android/view/View$DragShadowBuilder:onDrawShadow	(Landroid/graphics/Canvas;)V
    //   285: aload_0
    //   286: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   289: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   292: aload 11
    //   294: invokevirtual 7075	android/view/Surface:unlockCanvasAndPost	(Landroid/graphics/Canvas;)V
    //   297: aload 8
    //   299: aload_3
    //   300: invokevirtual 7078	android/view/ViewRootImpl:setLocalDragState	(Ljava/lang/Object;)V
    //   303: aload 8
    //   305: aload 6
    //   307: invokevirtual 7082	android/view/ViewRootImpl:getLastTouchPoint	(Landroid/graphics/Point;)V
    //   310: aload_0
    //   311: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   314: astore_3
    //   315: aload_0
    //   316: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   319: getfield 3820	android/view/View$AttachInfo:mSession	Landroid/view/IWindowSession;
    //   322: astore 11
    //   324: aload_0
    //   325: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   328: getfield 5449	android/view/View$AttachInfo:mWindow	Landroid/view/IWindow;
    //   331: astore_2
    //   332: aload 8
    //   334: invokevirtual 7085	android/view/ViewRootImpl:getLastTouchSource	()I
    //   337: istore 12
    //   339: aload 6
    //   341: getfield 5246	android/graphics/Point:x	I
    //   344: i2f
    //   345: fstore 13
    //   347: aload 6
    //   349: getfield 5248	android/graphics/Point:y	I
    //   352: i2f
    //   353: fstore 14
    //   355: aload 7
    //   357: getfield 5246	android/graphics/Point:x	I
    //   360: i2f
    //   361: fstore 15
    //   363: aload 7
    //   365: getfield 5248	android/graphics/Point:y	I
    //   368: istore 16
    //   370: iload 16
    //   372: i2f
    //   373: fstore 17
    //   375: aload_3
    //   376: aload 11
    //   378: aload_2
    //   379: iload 4
    //   381: aload 10
    //   383: iload 12
    //   385: fload 13
    //   387: fload 14
    //   389: fload 15
    //   391: fload 17
    //   393: aload_1
    //   394: invokeinterface 7089 10 0
    //   399: putfield 3816	android/view/View$AttachInfo:mDragToken	Landroid/os/IBinder;
    //   402: aload_0
    //   403: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   406: getfield 3816	android/view/View$AttachInfo:mDragToken	Landroid/os/IBinder;
    //   409: astore_1
    //   410: aload_1
    //   411: ifnull +9 -> 420
    //   414: iconst_1
    //   415: istore 5
    //   417: goto +6 -> 423
    //   420: iconst_0
    //   421: istore 5
    //   423: aload_0
    //   424: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   427: getfield 3816	android/view/View$AttachInfo:mDragToken	Landroid/os/IBinder;
    //   430: ifnonnull +27 -> 457
    //   433: aload_0
    //   434: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   437: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   440: invokevirtual 7092	android/view/Surface:destroy	()V
    //   443: aload_0
    //   444: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   447: aconst_null
    //   448: putfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   451: aload 8
    //   453: aconst_null
    //   454: invokevirtual 7078	android/view/ViewRootImpl:setLocalDragState	(Ljava/lang/Object;)V
    //   457: aload 9
    //   459: invokevirtual 7095	android/view/SurfaceSession:kill	()V
    //   462: iload 5
    //   464: ireturn
    //   465: astore_1
    //   466: goto +92 -> 558
    //   469: astore_1
    //   470: goto +35 -> 505
    //   473: astore_1
    //   474: goto +27 -> 501
    //   477: astore_1
    //   478: goto +27 -> 505
    //   481: astore_1
    //   482: aload_0
    //   483: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   486: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   489: aload 11
    //   491: invokevirtual 7075	android/view/Surface:unlockCanvasAndPost	(Landroid/graphics/Canvas;)V
    //   494: aload_1
    //   495: athrow
    //   496: astore_1
    //   497: goto +8 -> 505
    //   500: astore_1
    //   501: goto +57 -> 558
    //   504: astore_1
    //   505: ldc_w 652
    //   508: ldc_w 7097
    //   511: aload_1
    //   512: invokestatic 3795	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   515: pop
    //   516: aload_0
    //   517: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   520: getfield 3816	android/view/View$AttachInfo:mDragToken	Landroid/os/IBinder;
    //   523: ifnonnull +27 -> 550
    //   526: aload_0
    //   527: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   530: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   533: invokevirtual 7092	android/view/Surface:destroy	()V
    //   536: aload_0
    //   537: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   540: aconst_null
    //   541: putfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   544: aload 8
    //   546: aconst_null
    //   547: invokevirtual 7078	android/view/ViewRootImpl:setLocalDragState	(Ljava/lang/Object;)V
    //   550: aload 9
    //   552: invokevirtual 7095	android/view/SurfaceSession:kill	()V
    //   555: iconst_0
    //   556: ireturn
    //   557: astore_1
    //   558: aload_0
    //   559: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   562: getfield 3816	android/view/View$AttachInfo:mDragToken	Landroid/os/IBinder;
    //   565: ifnonnull +27 -> 592
    //   568: aload_0
    //   569: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   572: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   575: invokevirtual 7092	android/view/Surface:destroy	()V
    //   578: aload_0
    //   579: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   582: aconst_null
    //   583: putfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   586: aload 8
    //   588: aconst_null
    //   589: invokevirtual 7078	android/view/ViewRootImpl:setLocalDragState	(Ljava/lang/Object;)V
    //   592: aload 9
    //   594: invokevirtual 7095	android/view/SurfaceSession:kill	()V
    //   597: aload_1
    //   598: athrow
    //   599: new 1547	java/lang/IllegalStateException
    //   602: dup
    //   603: ldc_w 7099
    //   606: invokespecial 1550	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   609: athrow
    //   610: new 1547	java/lang/IllegalStateException
    //   613: dup
    //   614: ldc_w 7101
    //   617: invokespecial 1550	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   620: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	621	0	this	View
    //   0	621	1	paramClipData	ClipData
    //   0	621	2	paramDragShadowBuilder	DragShadowBuilder
    //   0	621	3	paramObject	Object
    //   0	621	4	paramInt	int
    //   33	430	5	bool	boolean
    //   57	291	6	localPoint1	Point
    //   66	298	7	localPoint2	Point
    //   191	396	8	localViewRootImpl	ViewRootImpl
    //   205	388	9	localSurfaceSession	SurfaceSession
    //   243	139	10	localSurfaceControl	SurfaceControl
    //   268	222	11	localObject	Object
    //   337	47	12	i	int
    //   345	41	13	f1	float
    //   353	35	14	f2	float
    //   361	29	15	f3	float
    //   368	3	16	j	int
    //   373	19	17	f4	float
    // Exception table:
    //   from	to	target	type
    //   375	410	465	finally
    //   375	410	469	java/lang/Exception
    //   339	370	473	finally
    //   339	370	477	java/lang/Exception
    //   270	285	481	finally
    //   482	496	496	java/lang/Exception
    //   245	270	500	finally
    //   285	297	500	finally
    //   297	339	500	finally
    //   245	270	504	java/lang/Exception
    //   285	297	504	java/lang/Exception
    //   297	339	504	java/lang/Exception
    //   482	496	557	finally
    //   505	516	557	finally
  }
  
  public final boolean startMovingTask(float paramFloat1, float paramFloat2)
  {
    try
    {
      boolean bool = mAttachInfo.mSession.startMovingTask(mAttachInfo.mWindow, paramFloat1, paramFloat2);
      return bool;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("View", "Unable to start moving", localRemoteException);
    }
    return false;
  }
  
  public boolean startNestedScroll(int paramInt)
  {
    if (hasNestedScrollingParent()) {
      return true;
    }
    if (isNestedScrollingEnabled())
    {
      ViewParent localViewParent = getParent();
      View localView = this;
      while (localViewParent != null)
      {
        try
        {
          if (localViewParent.onStartNestedScroll(localView, this, paramInt))
          {
            mNestedScrollingParent = localViewParent;
            localViewParent.onNestedScrollAccepted(localView, this, paramInt);
            return true;
          }
        }
        catch (AbstractMethodError localAbstractMethodError)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append("ViewParent ");
          localStringBuilder.append(localViewParent);
          localStringBuilder.append(" does not implement interface method onStartNestedScroll");
          Log.e("View", localStringBuilder.toString(), localAbstractMethodError);
        }
        if ((localViewParent instanceof View)) {
          localView = (View)localViewParent;
        }
        localViewParent = localViewParent.getParent();
      }
    }
    return false;
  }
  
  public void stopNestedScroll()
  {
    if (mNestedScrollingParent != null)
    {
      mNestedScrollingParent.onStopNestedScroll(this);
      mNestedScrollingParent = null;
    }
  }
  
  public boolean toGlobalMotionEvent(MotionEvent paramMotionEvent)
  {
    Object localObject = mAttachInfo;
    if (localObject == null) {
      return false;
    }
    localObject = mTmpMatrix;
    ((Matrix)localObject).set(Matrix.IDENTITY_MATRIX);
    transformMatrixToGlobal((Matrix)localObject);
    paramMotionEvent.transform((Matrix)localObject);
    return true;
  }
  
  public boolean toLocalMotionEvent(MotionEvent paramMotionEvent)
  {
    Object localObject = mAttachInfo;
    if (localObject == null) {
      return false;
    }
    localObject = mTmpMatrix;
    ((Matrix)localObject).set(Matrix.IDENTITY_MATRIX);
    transformMatrixToLocal((Matrix)localObject);
    paramMotionEvent.transform((Matrix)localObject);
    return true;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append(getClass().getName());
    localStringBuilder.append('{');
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(' ');
    int i = mViewFlags & 0xC;
    int j = 86;
    int k = 73;
    int m = 46;
    if (i != 0)
    {
      if (i != 4)
      {
        if (i != 8) {
          localStringBuilder.append('.');
        } else {
          localStringBuilder.append('G');
        }
      }
      else {
        localStringBuilder.append('I');
      }
    }
    else {
      localStringBuilder.append('V');
    }
    int n = mViewFlags;
    i = 70;
    if ((n & 0x1) == 1)
    {
      n = 70;
      i1 = n;
    }
    else
    {
      n = 46;
      i1 = n;
    }
    localStringBuilder.append(i1);
    if ((mViewFlags & 0x20) == 0)
    {
      n = 69;
      i1 = n;
    }
    else
    {
      n = 46;
      i1 = n;
    }
    localStringBuilder.append(i1);
    if ((mViewFlags & 0x80) == 128)
    {
      n = 46;
      i1 = n;
    }
    else
    {
      n = 68;
      i1 = n;
    }
    localStringBuilder.append(i1);
    int i2 = mViewFlags;
    n = 72;
    if ((i2 & 0x100) != 0)
    {
      i2 = 72;
      i1 = i2;
    }
    else
    {
      i2 = 46;
      i1 = i2;
    }
    localStringBuilder.append(i1);
    if ((mViewFlags & 0x200) != 0)
    {
      i1 = j;
    }
    else
    {
      j = 46;
      i1 = j;
    }
    localStringBuilder.append(i1);
    if ((mViewFlags & 0x4000) != 0)
    {
      j = 67;
      i1 = j;
    }
    else
    {
      j = 46;
      i1 = j;
    }
    localStringBuilder.append(i1);
    if ((mViewFlags & 0x200000) != 0)
    {
      j = 76;
      i1 = j;
    }
    else
    {
      j = 46;
      i1 = j;
    }
    localStringBuilder.append(i1);
    if ((mViewFlags & 0x800000) != 0)
    {
      j = 88;
      i1 = j;
    }
    else
    {
      j = 46;
      i1 = j;
    }
    localStringBuilder.append(i1);
    localStringBuilder.append(' ');
    if ((mPrivateFlags & 0x8) != 0)
    {
      j = 82;
      i1 = j;
    }
    else
    {
      j = 46;
      i1 = j;
    }
    localStringBuilder.append(i1);
    if ((mPrivateFlags & 0x2) != 0)
    {
      i1 = i;
    }
    else
    {
      i = 46;
      i1 = i;
    }
    localStringBuilder.append(i1);
    if ((mPrivateFlags & 0x4) != 0)
    {
      i = 83;
      i1 = i;
    }
    else
    {
      i = 46;
      i1 = i;
    }
    localStringBuilder.append(i1);
    if ((mPrivateFlags & 0x2000000) != 0)
    {
      localStringBuilder.append('p');
    }
    else
    {
      if ((mPrivateFlags & 0x4000) != 0)
      {
        i = 80;
        i1 = i;
      }
      else
      {
        i = 46;
        i1 = i;
      }
      localStringBuilder.append(i1);
    }
    if ((mPrivateFlags & 0x10000000) != 0)
    {
      i1 = n;
    }
    else
    {
      i = 46;
      i1 = i;
    }
    localStringBuilder.append(i1);
    if ((mPrivateFlags & 0x40000000) != 0)
    {
      i = 65;
      i1 = i;
    }
    else
    {
      i = 46;
      i1 = i;
    }
    localStringBuilder.append(i1);
    if ((mPrivateFlags & 0x80000000) != 0)
    {
      i1 = k;
    }
    else
    {
      k = 46;
      i1 = k;
    }
    localStringBuilder.append(i1);
    int i1 = m;
    if ((mPrivateFlags & 0x600000) != 0)
    {
      m = 68;
      i1 = m;
    }
    localStringBuilder.append(i1);
    localStringBuilder.append(' ');
    localStringBuilder.append(mLeft);
    localStringBuilder.append(',');
    localStringBuilder.append(mTop);
    localStringBuilder.append('-');
    localStringBuilder.append(mRight);
    localStringBuilder.append(',');
    localStringBuilder.append(mBottom);
    m = getId();
    if (m != -1)
    {
      localStringBuilder.append(" #");
      localStringBuilder.append(Integer.toHexString(m));
      Object localObject = mResources;
      if ((m > 0) && (Resources.resourceHasPackage(m)) && (localObject != null))
      {
        k = 0xFF000000 & m;
        String str2;
        if (k != 16777216)
        {
          if (k != 2130706432) {
            try
            {
              String str1 = ((Resources)localObject).getResourcePackageName(m);
            }
            catch (Resources.NotFoundException localNotFoundException)
            {
              break label930;
            }
          } else {
            str2 = "app";
          }
        }
        else {
          str2 = "android";
        }
        String str3 = ((Resources)localObject).getResourceTypeName(m);
        localObject = ((Resources)localObject).getResourceEntryName(m);
        localStringBuilder.append(" ");
        localStringBuilder.append(str2);
        localStringBuilder.append(":");
        localStringBuilder.append(str3);
        localStringBuilder.append("/");
        localStringBuilder.append((String)localObject);
      }
    }
    label930:
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void transformFromViewToWindowSpace(int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length >= 2))
    {
      if (mAttachInfo == null)
      {
        paramArrayOfInt[1] = 0;
        paramArrayOfInt[0] = 0;
        return;
      }
      float[] arrayOfFloat = mAttachInfo.mTmpTransformLocation;
      arrayOfFloat[0] = paramArrayOfInt[0];
      arrayOfFloat[1] = paramArrayOfInt[1];
      if (!hasIdentityMatrix()) {
        getMatrix().mapPoints(arrayOfFloat);
      }
      arrayOfFloat[0] += mLeft;
      arrayOfFloat[1] += mTop;
      for (Object localObject = mParent; (localObject instanceof View); localObject = mParent)
      {
        localObject = (View)localObject;
        arrayOfFloat[0] -= mScrollX;
        arrayOfFloat[1] -= mScrollY;
        if (!((View)localObject).hasIdentityMatrix()) {
          ((View)localObject).getMatrix().mapPoints(arrayOfFloat);
        }
        arrayOfFloat[0] += mLeft;
        arrayOfFloat[1] += mTop;
      }
      if ((localObject instanceof ViewRootImpl))
      {
        localObject = (ViewRootImpl)localObject;
        arrayOfFloat[1] -= mCurScrollY;
      }
      paramArrayOfInt[0] = Math.round(arrayOfFloat[0]);
      paramArrayOfInt[1] = Math.round(arrayOfFloat[1]);
      return;
    }
    throw new IllegalArgumentException("inOutLocation must be an array of two integers");
  }
  
  public void transformMatrixToGlobal(Matrix paramMatrix)
  {
    Object localObject = mParent;
    if ((localObject instanceof View))
    {
      localObject = (View)localObject;
      ((View)localObject).transformMatrixToGlobal(paramMatrix);
      paramMatrix.preTranslate(-mScrollX, -mScrollY);
    }
    else if ((localObject instanceof ViewRootImpl))
    {
      localObject = (ViewRootImpl)localObject;
      ((ViewRootImpl)localObject).transformMatrixToGlobal(paramMatrix);
      paramMatrix.preTranslate(0.0F, -mCurScrollY);
    }
    paramMatrix.preTranslate(mLeft, mTop);
    if (!hasIdentityMatrix()) {
      paramMatrix.preConcat(getMatrix());
    }
  }
  
  public void transformMatrixToLocal(Matrix paramMatrix)
  {
    Object localObject = mParent;
    if ((localObject instanceof View))
    {
      localObject = (View)localObject;
      ((View)localObject).transformMatrixToLocal(paramMatrix);
      paramMatrix.postTranslate(mScrollX, mScrollY);
    }
    else if ((localObject instanceof ViewRootImpl))
    {
      localObject = (ViewRootImpl)localObject;
      ((ViewRootImpl)localObject).transformMatrixToLocal(paramMatrix);
      paramMatrix.postTranslate(0.0F, mCurScrollY);
    }
    paramMatrix.postTranslate(-mLeft, -mTop);
    if (!hasIdentityMatrix()) {
      paramMatrix.postConcat(getInverseMatrix());
    }
  }
  
  void transformRect(Rect paramRect)
  {
    if (!getMatrix().isIdentity())
    {
      RectF localRectF = mAttachInfo.mTmpTransformRect;
      localRectF.set(paramRect);
      getMatrix().mapRect(localRectF);
      paramRect.set((int)Math.floor(left), (int)Math.floor(top), (int)Math.ceil(right), (int)Math.ceil(bottom));
    }
  }
  
  void unFocus(View paramView)
  {
    clearFocusInternal(paramView, false, false);
  }
  
  public void unscheduleDrawable(Drawable paramDrawable)
  {
    if ((mAttachInfo != null) && (paramDrawable != null)) {
      mAttachInfo.mViewRootImpl.mChoreographer.removeCallbacks(1, null, paramDrawable);
    }
  }
  
  public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
  {
    if ((verifyDrawable(paramDrawable)) && (paramRunnable != null))
    {
      if (mAttachInfo != null) {
        mAttachInfo.mViewRootImpl.mChoreographer.removeCallbacks(1, paramRunnable, paramDrawable);
      }
      getRunQueue().removeCallbacks(paramRunnable);
    }
  }
  
  public RenderNode updateDisplayListIfDirty()
  {
    RenderNode localRenderNode = mRenderNode;
    if (!canHaveDisplayList()) {
      return localRenderNode;
    }
    DisplayListCanvas localDisplayListCanvas;
    if (((mPrivateFlags & 0x8000) != 0) && (localRenderNode.isValid()) && (!mRecreateDisplayList))
    {
      mPrivateFlags |= 0x8020;
      mPrivateFlags &= 0xFF9FFFFF;
    }
    else
    {
      if ((localRenderNode.isValid()) && (!mRecreateDisplayList))
      {
        mPrivateFlags |= 0x8020;
        mPrivateFlags &= 0xFF9FFFFF;
        dispatchGetDisplayList();
        return localRenderNode;
      }
      mRecreateDisplayList = true;
      int i = mRight;
      int j = mLeft;
      int k = mBottom;
      int m = mTop;
      int n = getLayerType();
      localDisplayListCanvas = localRenderNode.start(i - j, k - m);
      if (n == 1) {
        try
        {
          buildDrawingCache(true);
          Bitmap localBitmap = getDrawingCache(true);
          if (localBitmap != null) {
            localDisplayListCanvas.drawBitmap(localBitmap, 0.0F, 0.0F, mLayerPaint);
          }
        }
        finally
        {
          break label339;
        }
      }
      computeScroll();
      localDisplayListCanvas.translate(-mScrollX, -mScrollY);
      mPrivateFlags |= 0x8020;
      mPrivateFlags &= 0xFF9FFFFF;
      if ((mPrivateFlags & 0x80) == 128)
      {
        dispatchDraw(localDisplayListCanvas);
        drawAutofilledHighlight(localDisplayListCanvas);
        if ((mOverlay != null) && (!mOverlay.isEmpty())) {
          mOverlay.getOverlayView().draw(localDisplayListCanvas);
        }
        if (debugDraw()) {
          debugDrawFocus(localDisplayListCanvas);
        }
      }
      else
      {
        draw(localDisplayListCanvas);
      }
      localRenderNode.end(localDisplayListCanvas);
      setDisplayListProperties(localRenderNode);
    }
    return localRenderNode;
    label339:
    localRenderNode.end(localDisplayListCanvas);
    setDisplayListProperties(localRenderNode);
    throw localObject;
  }
  
  /* Error */
  public final void updateDragShadow(DragShadowBuilder paramDragShadowBuilder)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   4: ifnonnull +14 -> 18
    //   7: ldc_w 652
    //   10: ldc_w 7201
    //   13: invokestatic 1943	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   16: pop
    //   17: return
    //   18: aload_0
    //   19: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   22: getfield 3816	android/view/View$AttachInfo:mDragToken	Landroid/os/IBinder;
    //   25: ifnull +71 -> 96
    //   28: aload_0
    //   29: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   32: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   35: aconst_null
    //   36: invokevirtual 7060	android/view/Surface:lockCanvas	(Landroid/graphics/Rect;)Landroid/graphics/Canvas;
    //   39: astore_2
    //   40: aload_2
    //   41: iconst_0
    //   42: getstatic 7065	android/graphics/PorterDuff$Mode:CLEAR	Landroid/graphics/PorterDuff$Mode;
    //   45: invokevirtual 7069	android/graphics/Canvas:drawColor	(ILandroid/graphics/PorterDuff$Mode;)V
    //   48: aload_1
    //   49: aload_2
    //   50: invokevirtual 7072	android/view/View$DragShadowBuilder:onDrawShadow	(Landroid/graphics/Canvas;)V
    //   53: aload_0
    //   54: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   57: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   60: aload_2
    //   61: invokevirtual 7075	android/view/Surface:unlockCanvasAndPost	(Landroid/graphics/Canvas;)V
    //   64: goto +29 -> 93
    //   67: astore_1
    //   68: aload_0
    //   69: getfield 1741	android/view/View:mAttachInfo	Landroid/view/View$AttachInfo;
    //   72: getfield 7015	android/view/View$AttachInfo:mDragSurface	Landroid/view/Surface;
    //   75: aload_2
    //   76: invokevirtual 7075	android/view/Surface:unlockCanvasAndPost	(Landroid/graphics/Canvas;)V
    //   79: aload_1
    //   80: athrow
    //   81: astore_1
    //   82: ldc_w 652
    //   85: ldc_w 7203
    //   88: aload_1
    //   89: invokestatic 3795	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   92: pop
    //   93: goto +13 -> 106
    //   96: ldc_w 652
    //   99: ldc_w 7205
    //   102: invokestatic 3104	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   105: pop
    //   106: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	107	0	this	View
    //   0	107	1	paramDragShadowBuilder	DragShadowBuilder
    //   39	37	2	localCanvas	Canvas
    // Exception table:
    //   from	to	target	type
    //   40	53	67	finally
    //   28	40	81	java/lang/Exception
    //   53	64	81	java/lang/Exception
    //   68	81	81	java/lang/Exception
  }
  
  boolean updateLocalSystemUiVisibility(int paramInt1, int paramInt2)
  {
    paramInt1 = mSystemUiVisibility & paramInt2 | paramInt1 & paramInt2;
    if (paramInt1 != mSystemUiVisibility)
    {
      setSystemUiVisibility(paramInt1);
      return true;
    }
    return false;
  }
  
  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    boolean bool;
    if ((paramDrawable != mBackground) && ((mForegroundInfo == null) || (mForegroundInfo.mDrawable != paramDrawable)) && (mDefaultFocusHighlight != paramDrawable)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  @Deprecated
  public boolean willNotCacheDrawing()
  {
    boolean bool;
    if ((mViewFlags & 0x20000) == 131072) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  @ViewDebug.ExportedProperty(category="drawing")
  public boolean willNotDraw()
  {
    boolean bool;
    if ((mViewFlags & 0x80) == 128) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static class AccessibilityDelegate
  {
    public AccessibilityDelegate() {}
    
    public void addExtraDataToAccessibilityNodeInfo(View paramView, AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString, Bundle paramBundle)
    {
      paramView.addExtraDataToAccessibilityNodeInfo(paramAccessibilityNodeInfo, paramString, paramBundle);
    }
    
    public AccessibilityNodeInfo createAccessibilityNodeInfo(View paramView)
    {
      return paramView.createAccessibilityNodeInfoInternal();
    }
    
    public boolean dispatchPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      return paramView.dispatchPopulateAccessibilityEventInternal(paramAccessibilityEvent);
    }
    
    public AccessibilityNodeProvider getAccessibilityNodeProvider(View paramView)
    {
      return null;
    }
    
    public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      paramView.onInitializeAccessibilityEventInternal(paramAccessibilityEvent);
    }
    
    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfo paramAccessibilityNodeInfo)
    {
      paramView.onInitializeAccessibilityNodeInfoInternal(paramAccessibilityNodeInfo);
    }
    
    public void onPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      paramView.onPopulateAccessibilityEventInternal(paramAccessibilityEvent);
    }
    
    public boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      return paramViewGroup.onRequestSendAccessibilityEventInternal(paramView, paramAccessibilityEvent);
    }
    
    public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
    {
      return paramView.performAccessibilityActionInternal(paramInt, paramBundle);
    }
    
    public void sendAccessibilityEvent(View paramView, int paramInt)
    {
      paramView.sendAccessibilityEventInternal(paramInt);
    }
    
    public void sendAccessibilityEventUnchecked(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      paramView.sendAccessibilityEventUncheckedInternal(paramAccessibilityEvent);
    }
  }
  
  static final class AttachInfo
  {
    int mAccessibilityFetchFlags;
    Drawable mAccessibilityFocusDrawable;
    int mAccessibilityWindowId = -1;
    boolean mAlwaysConsumeNavBar;
    float mApplicationScale;
    Drawable mAutofilledDrawable;
    Canvas mCanvas;
    final Rect mContentInsets = new Rect();
    boolean mDebugLayout = SystemProperties.getBoolean("debug.layout", false);
    int mDisabledSystemUiVisibility;
    Display mDisplay;
    final DisplayCutout.ParcelableWrapper mDisplayCutout = new DisplayCutout.ParcelableWrapper(DisplayCutout.NO_CUTOUT);
    int mDisplayState = 0;
    public Surface mDragSurface;
    IBinder mDragToken;
    long mDrawingTime;
    List<View> mEmptyPartialLayoutViews;
    boolean mForceReportNewAttributes;
    final ViewTreeObserver.InternalInsetsInfo mGivenInternalInsets = new ViewTreeObserver.InternalInsetsInfo();
    int mGlobalSystemUiVisibility = -1;
    final Handler mHandler;
    boolean mHandlingPointerEvent;
    boolean mHardwareAccelerated;
    boolean mHardwareAccelerationRequested;
    boolean mHasNonEmptyGivenInternalInsets;
    boolean mHasSystemUiListeners;
    boolean mHasWindowFocus;
    IWindowId mIWindowId;
    boolean mIgnoreDirtyState;
    boolean mInTouchMode;
    final int[] mInvalidateChildLocation = new int[2];
    boolean mKeepScreenOn;
    final KeyEvent.DispatcherState mKeyDispatchState = new KeyEvent.DispatcherState();
    boolean mNeedsUpdateLightCenter;
    final Rect mOutsets = new Rect();
    final Rect mOverscanInsets = new Rect();
    boolean mOverscanRequested;
    IBinder mPanelParentWindowToken;
    List<View> mPartialLayoutViews = new ArrayList();
    List<RenderNode> mPendingAnimatingRenderNodes;
    final Point mPoint = new Point();
    boolean mRecomputeGlobalAttributes;
    final Callbacks mRootCallbacks;
    View mRootView;
    boolean mScalingRequired;
    final ArrayList<View> mScrollContainers = new ArrayList();
    final IWindowSession mSession;
    boolean mSetIgnoreDirtyState = false;
    final Rect mStableInsets = new Rect();
    int mSystemUiVisibility;
    final ArrayList<View> mTempArrayList = new ArrayList(24);
    ThreadedRenderer mThreadedRenderer;
    final Rect mTmpInvalRect = new Rect();
    final int[] mTmpLocation = new int[2];
    final Matrix mTmpMatrix = new Matrix();
    final Outline mTmpOutline = new Outline();
    final List<RectF> mTmpRectList = new ArrayList();
    final float[] mTmpTransformLocation = new float[2];
    final RectF mTmpTransformRect = new RectF();
    final RectF mTmpTransformRect1 = new RectF();
    final Transformation mTmpTransformation = new Transformation();
    View mTooltipHost;
    final int[] mTransparentLocation = new int[2];
    final ViewTreeObserver mTreeObserver;
    boolean mUnbufferedDispatchRequested;
    boolean mUse32BitDrawingCache;
    View mViewRequestingLayout;
    final ViewRootImpl mViewRootImpl;
    boolean mViewScrollChanged;
    boolean mViewVisibilityChanged;
    final Rect mVisibleInsets = new Rect();
    final IWindow mWindow;
    WindowId mWindowId;
    int mWindowLeft;
    final IBinder mWindowToken;
    int mWindowTop;
    int mWindowVisibility;
    
    AttachInfo(IWindowSession paramIWindowSession, IWindow paramIWindow, Display paramDisplay, ViewRootImpl paramViewRootImpl, Handler paramHandler, Callbacks paramCallbacks, Context paramContext)
    {
      mSession = paramIWindowSession;
      mWindow = paramIWindow;
      mWindowToken = paramIWindow.asBinder();
      mDisplay = paramDisplay;
      mViewRootImpl = paramViewRootImpl;
      mHandler = paramHandler;
      mRootCallbacks = paramCallbacks;
      mTreeObserver = new ViewTreeObserver(paramContext);
    }
    
    static abstract interface Callbacks
    {
      public abstract boolean performHapticFeedback(int paramInt, boolean paramBoolean);
      
      public abstract void playSoundEffect(int paramInt);
    }
    
    static class InvalidateInfo
    {
      private static final int POOL_LIMIT = 10;
      private static final Pools.SynchronizedPool<InvalidateInfo> sPool = new Pools.SynchronizedPool(10);
      int bottom;
      int left;
      int right;
      View target;
      int top;
      
      InvalidateInfo() {}
      
      public static InvalidateInfo obtain()
      {
        InvalidateInfo localInvalidateInfo = (InvalidateInfo)sPool.acquire();
        if (localInvalidateInfo == null) {
          localInvalidateInfo = new InvalidateInfo();
        }
        return localInvalidateInfo;
      }
      
      public void recycle()
      {
        target = null;
        sPool.release(this);
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AutofillFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AutofillImportance {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface AutofillType {}
  
  public static class BaseSavedState
    extends AbsSavedState
  {
    static final int AUTOFILL_ID = 4;
    public static final Parcelable.Creator<BaseSavedState> CREATOR = new Parcelable.ClassLoaderCreator()
    {
      public View.BaseSavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new View.BaseSavedState(paramAnonymousParcel);
      }
      
      public View.BaseSavedState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
      {
        return new View.BaseSavedState(paramAnonymousParcel, paramAnonymousClassLoader);
      }
      
      public View.BaseSavedState[] newArray(int paramAnonymousInt)
      {
        return new View.BaseSavedState[paramAnonymousInt];
      }
    };
    static final int IS_AUTOFILLED = 2;
    static final int START_ACTIVITY_REQUESTED_WHO_SAVED = 1;
    int mAutofillViewId;
    boolean mIsAutofilled;
    int mSavedData;
    String mStartActivityRequestWhoSaved;
    
    public BaseSavedState(Parcel paramParcel)
    {
      this(paramParcel, null);
    }
    
    public BaseSavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super(paramClassLoader);
      mSavedData = paramParcel.readInt();
      mStartActivityRequestWhoSaved = paramParcel.readString();
      mIsAutofilled = paramParcel.readBoolean();
      mAutofillViewId = paramParcel.readInt();
    }
    
    public BaseSavedState(Parcelable paramParcelable)
    {
      super();
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      paramParcel.writeInt(mSavedData);
      paramParcel.writeString(mStartActivityRequestWhoSaved);
      paramParcel.writeBoolean(mIsAutofilled);
      paramParcel.writeInt(mAutofillViewId);
    }
  }
  
  private final class CheckForLongPress
    implements Runnable
  {
    private boolean mOriginalPressedState;
    private int mOriginalWindowAttachCount;
    private float mX;
    private float mY;
    
    private CheckForLongPress() {}
    
    public void rememberPressedState()
    {
      mOriginalPressedState = isPressed();
    }
    
    public void rememberWindowAttachCount()
    {
      mOriginalWindowAttachCount = mWindowAttachCount;
    }
    
    public void run()
    {
      if ((mOriginalPressedState == isPressed()) && (mParent != null) && (mOriginalWindowAttachCount == mWindowAttachCount) && (performLongClick(mX, mY))) {
        View.access$2802(View.this, true);
      }
    }
    
    public void setAnchor(float paramFloat1, float paramFloat2)
    {
      mX = paramFloat1;
      mY = paramFloat2;
    }
  }
  
  private final class CheckForTap
    implements Runnable
  {
    public float x;
    public float y;
    
    private CheckForTap() {}
    
    public void run()
    {
      View localView = View.this;
      mPrivateFlags &= 0xFDFFFFFF;
      View.this.setPressed(true, x, y);
      View.this.checkForLongClick(ViewConfiguration.getTapTimeout(), x, y);
    }
  }
  
  private static class DeclaredOnClickListener
    implements View.OnClickListener
  {
    private final View mHostView;
    private final String mMethodName;
    private Context mResolvedContext;
    private Method mResolvedMethod;
    
    public DeclaredOnClickListener(View paramView, String paramString)
    {
      mHostView = paramView;
      mMethodName = paramString;
    }
    
    private void resolveMethod(Context paramContext, String paramString)
    {
      while (paramContext != null)
      {
        try
        {
          if (!paramContext.isRestricted())
          {
            paramString = paramContext.getClass().getMethod(mMethodName, new Class[] { View.class });
            if (paramString != null)
            {
              mResolvedMethod = paramString;
              mResolvedContext = paramContext;
              return;
            }
          }
        }
        catch (NoSuchMethodException paramString) {}
        if ((paramContext instanceof ContextWrapper)) {
          paramContext = ((ContextWrapper)paramContext).getBaseContext();
        } else {
          paramContext = null;
        }
      }
      int i = mHostView.getId();
      if (i == -1)
      {
        paramContext = "";
      }
      else
      {
        paramContext = new StringBuilder();
        paramContext.append(" with id '");
        paramContext.append(mHostView.getContext().getResources().getResourceEntryName(i));
        paramContext.append("'");
        paramContext = paramContext.toString();
      }
      paramString = new StringBuilder();
      paramString.append("Could not find method ");
      paramString.append(mMethodName);
      paramString.append("(View) in a parent or ancestor Context for android:onClick attribute defined on view ");
      paramString.append(mHostView.getClass());
      paramString.append(paramContext);
      throw new IllegalStateException(paramString.toString());
    }
    
    public void onClick(View paramView)
    {
      if (mResolvedMethod == null) {
        resolveMethod(mHostView.getContext(), mMethodName);
      }
      try
      {
        mResolvedMethod.invoke(mResolvedContext, new Object[] { paramView });
        return;
      }
      catch (InvocationTargetException paramView)
      {
        throw new IllegalStateException("Could not execute method for android:onClick", paramView);
      }
      catch (IllegalAccessException paramView)
      {
        throw new IllegalStateException("Could not execute non-public method for android:onClick", paramView);
      }
    }
  }
  
  public static class DragShadowBuilder
  {
    private final WeakReference<View> mView;
    
    public DragShadowBuilder()
    {
      mView = new WeakReference(null);
    }
    
    public DragShadowBuilder(View paramView)
    {
      mView = new WeakReference(paramView);
    }
    
    public final View getView()
    {
      return (View)mView.get();
    }
    
    public void onDrawShadow(Canvas paramCanvas)
    {
      View localView = (View)mView.get();
      if (localView != null) {
        localView.draw(paramCanvas);
      } else {
        Log.e("View", "Asked to draw drag shadow but no view");
      }
    }
    
    public void onProvideShadowMetrics(Point paramPoint1, Point paramPoint2)
    {
      View localView = (View)mView.get();
      if (localView != null)
      {
        paramPoint1.set(localView.getWidth(), localView.getHeight());
        paramPoint2.set(x / 2, y / 2);
      }
      else
      {
        Log.e("View", "Asked for drag thumb metrics but no view");
      }
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface DrawingCacheQuality {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FindViewFlags {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FocusDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FocusRealDirection {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Focusable {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface FocusableMode {}
  
  private static class ForegroundInfo
  {
    private boolean mBoundsChanged = true;
    private Drawable mDrawable;
    private int mGravity = 119;
    private boolean mInsidePadding = true;
    private final Rect mOverlayBounds = new Rect();
    private final Rect mSelfBounds = new Rect();
    private View.TintInfo mTintInfo;
    
    private ForegroundInfo() {}
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LayoutDir {}
  
  static class ListenerInfo
  {
    View.OnApplyWindowInsetsListener mOnApplyWindowInsetsListener;
    private CopyOnWriteArrayList<View.OnAttachStateChangeListener> mOnAttachStateChangeListeners;
    View.OnCapturedPointerListener mOnCapturedPointerListener;
    public View.OnClickListener mOnClickListener;
    protected View.OnContextClickListener mOnContextClickListener;
    protected View.OnCreateContextMenuListener mOnCreateContextMenuListener;
    private View.OnDragListener mOnDragListener;
    protected View.OnFocusChangeListener mOnFocusChangeListener;
    private View.OnGenericMotionListener mOnGenericMotionListener;
    private View.OnHoverListener mOnHoverListener;
    private View.OnKeyListener mOnKeyListener;
    private ArrayList<View.OnLayoutChangeListener> mOnLayoutChangeListeners;
    protected View.OnLongClickListener mOnLongClickListener;
    protected View.OnScrollChangeListener mOnScrollChangeListener;
    private View.OnSystemUiVisibilityChangeListener mOnSystemUiVisibilityChangeListener;
    private View.OnTouchListener mOnTouchListener;
    private ArrayList<View.OnUnhandledKeyEventListener> mUnhandledKeyListeners;
    
    ListenerInfo() {}
  }
  
  private static class MatchIdPredicate
    implements Predicate<View>
  {
    public int mId;
    
    private MatchIdPredicate() {}
    
    public boolean test(View paramView)
    {
      boolean bool;
      if (mID == mId) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  private static class MatchLabelForPredicate
    implements Predicate<View>
  {
    private int mLabeledId;
    
    private MatchLabelForPredicate() {}
    
    public boolean test(View paramView)
    {
      boolean bool;
      if (mLabelForId == mLabeledId) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
  }
  
  public static class MeasureSpec
  {
    public static final int AT_MOST = Integer.MIN_VALUE;
    public static final int EXACTLY = 1073741824;
    private static final int MODE_MASK = -1073741824;
    private static final int MODE_SHIFT = 30;
    public static final int UNSPECIFIED = 0;
    
    public MeasureSpec() {}
    
    static int adjust(int paramInt1, int paramInt2)
    {
      int i = getMode(paramInt1);
      int j = getSize(paramInt1);
      if (i == 0) {
        return makeMeasureSpec(j, 0);
      }
      int k = j + paramInt2;
      j = k;
      if (k < 0)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("MeasureSpec.adjust: new size would be negative! (");
        localStringBuilder.append(k);
        localStringBuilder.append(") spec: ");
        localStringBuilder.append(toString(paramInt1));
        localStringBuilder.append(" delta: ");
        localStringBuilder.append(paramInt2);
        Log.e("View", localStringBuilder.toString());
        j = 0;
      }
      return makeMeasureSpec(j, i);
    }
    
    public static int getMode(int paramInt)
    {
      return 0xC0000000 & paramInt;
    }
    
    public static int getSize(int paramInt)
    {
      return 0x3FFFFFFF & paramInt;
    }
    
    public static int makeMeasureSpec(int paramInt1, int paramInt2)
    {
      if (View.sUseBrokenMakeMeasureSpec) {
        return paramInt1 + paramInt2;
      }
      return 0x3FFFFFFF & paramInt1 | 0xC0000000 & paramInt2;
    }
    
    public static int makeSafeMeasureSpec(int paramInt1, int paramInt2)
    {
      if ((View.sUseZeroUnspecifiedMeasureSpec) && (paramInt2 == 0)) {
        return 0;
      }
      return makeMeasureSpec(paramInt1, paramInt2);
    }
    
    public static String toString(int paramInt)
    {
      int i = getMode(paramInt);
      paramInt = getSize(paramInt);
      StringBuilder localStringBuilder = new StringBuilder("MeasureSpec: ");
      if (i == 0)
      {
        localStringBuilder.append("UNSPECIFIED ");
      }
      else if (i == 1073741824)
      {
        localStringBuilder.append("EXACTLY ");
      }
      else if (i == Integer.MIN_VALUE)
      {
        localStringBuilder.append("AT_MOST ");
      }
      else
      {
        localStringBuilder.append(i);
        localStringBuilder.append(" ");
      }
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface MeasureSpecMode {}
  }
  
  public static abstract interface OnApplyWindowInsetsListener
  {
    public abstract WindowInsets onApplyWindowInsets(View paramView, WindowInsets paramWindowInsets);
  }
  
  public static abstract interface OnAttachStateChangeListener
  {
    public abstract void onViewAttachedToWindow(View paramView);
    
    public abstract void onViewDetachedFromWindow(View paramView);
  }
  
  public static abstract interface OnCapturedPointerListener
  {
    public abstract boolean onCapturedPointer(View paramView, MotionEvent paramMotionEvent);
  }
  
  public static abstract interface OnClickListener
  {
    public abstract void onClick(View paramView);
  }
  
  public static abstract interface OnContextClickListener
  {
    public abstract boolean onContextClick(View paramView);
  }
  
  public static abstract interface OnCreateContextMenuListener
  {
    public abstract void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo);
  }
  
  public static abstract interface OnDragListener
  {
    public abstract boolean onDrag(View paramView, DragEvent paramDragEvent);
  }
  
  public static abstract interface OnFocusChangeListener
  {
    public abstract void onFocusChange(View paramView, boolean paramBoolean);
  }
  
  public static abstract interface OnGenericMotionListener
  {
    public abstract boolean onGenericMotion(View paramView, MotionEvent paramMotionEvent);
  }
  
  public static abstract interface OnHoverListener
  {
    public abstract boolean onHover(View paramView, MotionEvent paramMotionEvent);
  }
  
  public static abstract interface OnKeyListener
  {
    public abstract boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent);
  }
  
  public static abstract interface OnLayoutChangeListener
  {
    public abstract void onLayoutChange(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
  }
  
  public static abstract interface OnLongClickListener
  {
    public abstract boolean onLongClick(View paramView);
  }
  
  public static abstract interface OnScrollChangeListener
  {
    public abstract void onScrollChange(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  }
  
  public static abstract interface OnSystemUiVisibilityChangeListener
  {
    public abstract void onSystemUiVisibilityChange(int paramInt);
  }
  
  public static abstract interface OnTouchListener
  {
    public abstract boolean onTouch(View paramView, MotionEvent paramMotionEvent);
  }
  
  public static abstract interface OnUnhandledKeyEventListener
  {
    public abstract boolean onUnhandledKeyEvent(View paramView, KeyEvent paramKeyEvent);
  }
  
  private final class PerformClick
    implements Runnable
  {
    private PerformClick() {}
    
    public void run()
    {
      View.this.performClickInternal();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ResolvedLayoutDir {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScrollBarStyle {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScrollIndicators {}
  
  private static class ScrollabilityCache
    implements Runnable
  {
    public static final int DRAGGING_HORIZONTAL_SCROLL_BAR = 2;
    public static final int DRAGGING_VERTICAL_SCROLL_BAR = 1;
    public static final int FADING = 2;
    public static final int NOT_DRAGGING = 0;
    public static final int OFF = 0;
    public static final int ON = 1;
    private static final float[] OPAQUE = { 255.0F };
    private static final float[] TRANSPARENT = { 0.0F };
    public boolean fadeScrollBars;
    public long fadeStartTime;
    public int fadingEdgeLength;
    public View host;
    public float[] interpolatorValues;
    private int mLastColor;
    public final Rect mScrollBarBounds = new Rect();
    public float mScrollBarDraggingPos = 0.0F;
    public int mScrollBarDraggingState = 0;
    public final Rect mScrollBarTouchBounds = new Rect();
    public final Matrix matrix;
    public final Paint paint;
    public ScrollBarDrawable scrollBar;
    public int scrollBarDefaultDelayBeforeFade;
    public int scrollBarFadeDuration;
    public final Interpolator scrollBarInterpolator = new Interpolator(1, 2);
    public int scrollBarMinTouchTarget;
    public int scrollBarSize;
    public Shader shader;
    public int state = 0;
    
    public ScrollabilityCache(ViewConfiguration paramViewConfiguration, View paramView)
    {
      fadingEdgeLength = paramViewConfiguration.getScaledFadingEdgeLength();
      scrollBarSize = paramViewConfiguration.getScaledScrollBarSize();
      scrollBarMinTouchTarget = paramViewConfiguration.getScaledMinScrollbarTouchTarget();
      scrollBarDefaultDelayBeforeFade = ViewConfiguration.getScrollDefaultDelay();
      scrollBarFadeDuration = ViewConfiguration.getScrollBarFadeDuration();
      paint = new Paint();
      matrix = new Matrix();
      shader = new LinearGradient(0.0F, 0.0F, 0.0F, 1.0F, -16777216, 0, Shader.TileMode.CLAMP);
      paint.setShader(shader);
      paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
      host = paramView;
    }
    
    public void run()
    {
      long l = AnimationUtils.currentAnimationTimeMillis();
      if (l >= fadeStartTime)
      {
        int i = (int)l;
        Interpolator localInterpolator = scrollBarInterpolator;
        localInterpolator.setKeyFrame(0, i, OPAQUE);
        localInterpolator.setKeyFrame(0 + 1, i + scrollBarFadeDuration, TRANSPARENT);
        state = 2;
        host.invalidate(true);
      }
    }
    
    public void setFadeColor(int paramInt)
    {
      if (paramInt != mLastColor)
      {
        mLastColor = paramInt;
        if (paramInt != 0)
        {
          shader = new LinearGradient(0.0F, 0.0F, 0.0F, 1.0F, paramInt | 0xFF000000, paramInt & 0xFFFFFF, Shader.TileMode.CLAMP);
          paint.setShader(shader);
          paint.setXfermode(null);
        }
        else
        {
          shader = new LinearGradient(0.0F, 0.0F, 0.0F, 1.0F, -16777216, 0, Shader.TileMode.CLAMP);
          paint.setShader(shader);
          paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        }
      }
    }
  }
  
  private class SendViewScrolledAccessibilityEvent
    implements Runnable
  {
    public int mDeltaX;
    public int mDeltaY;
    public volatile boolean mIsPending;
    
    private SendViewScrolledAccessibilityEvent() {}
    
    private void reset()
    {
      mIsPending = false;
      mDeltaX = 0;
      mDeltaY = 0;
    }
    
    public void post(int paramInt1, int paramInt2)
    {
      mDeltaX += paramInt1;
      mDeltaY += paramInt2;
      if (!mIsPending)
      {
        mIsPending = true;
        postDelayed(this, ViewConfiguration.getSendRecurringAccessibilityEventsInterval());
      }
    }
    
    public void run()
    {
      if (AccessibilityManager.getInstance(mContext).isEnabled())
      {
        AccessibilityEvent localAccessibilityEvent = AccessibilityEvent.obtain(4096);
        localAccessibilityEvent.setScrollDeltaX(mDeltaX);
        localAccessibilityEvent.setScrollDeltaY(mDeltaY);
        sendAccessibilityEventUnchecked(localAccessibilityEvent);
      }
      reset();
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TextAlignment {}
  
  static class TintInfo
  {
    boolean mHasTintList;
    boolean mHasTintMode;
    ColorStateList mTintList;
    PorterDuff.Mode mTintMode;
    
    TintInfo() {}
  }
  
  private static class TooltipInfo
  {
    int mAnchorX;
    int mAnchorY;
    Runnable mHideTooltipRunnable;
    int mHoverSlop;
    Runnable mShowTooltipRunnable;
    boolean mTooltipFromLongClick;
    TooltipPopup mTooltipPopup;
    CharSequence mTooltipText;
    
    private TooltipInfo() {}
    
    private void clearAnchorPos()
    {
      mAnchorX = Integer.MAX_VALUE;
      mAnchorY = Integer.MAX_VALUE;
    }
    
    private boolean updateAnchorPos(MotionEvent paramMotionEvent)
    {
      int i = (int)paramMotionEvent.getX();
      int j = (int)paramMotionEvent.getY();
      if ((Math.abs(i - mAnchorX) <= mHoverSlop) && (Math.abs(j - mAnchorY) <= mHoverSlop)) {
        return false;
      }
      mAnchorX = i;
      mAnchorY = j;
      return true;
    }
  }
  
  static class TransformationInfo
  {
    @ViewDebug.ExportedProperty
    float mAlpha = 1.0F;
    private Matrix mInverseMatrix;
    private final Matrix mMatrix = new Matrix();
    float mTransitionAlpha = 1.0F;
    
    TransformationInfo() {}
  }
  
  private final class UnsetPressedState
    implements Runnable
  {
    private UnsetPressedState() {}
    
    public void run()
    {
      setPressed(false);
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Visibility {}
  
  private static class VisibilityChangeForAutofillHandler
    extends Handler
  {
    private final AutofillManager mAfm;
    private final View mView;
    
    private VisibilityChangeForAutofillHandler(AutofillManager paramAutofillManager, View paramView)
    {
      mAfm = paramAutofillManager;
      mView = paramView;
    }
    
    public void handleMessage(Message paramMessage)
    {
      mAfm.notifyViewVisibilityChanged(mView, mView.isShown());
    }
  }
}
