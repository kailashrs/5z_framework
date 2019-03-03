package android.content.pm;

import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Printer;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ActivityInfo
  extends ComponentInfo
  implements Parcelable
{
  public static final int COLOR_MODE_DEFAULT = 0;
  public static final int COLOR_MODE_HDR = 2;
  public static final int COLOR_MODE_WIDE_COLOR_GAMUT = 1;
  public static final int CONFIG_ASSETS_PATHS = Integer.MIN_VALUE;
  public static final int CONFIG_COLOR_MODE = 16384;
  public static final int CONFIG_DENSITY = 4096;
  public static final int CONFIG_FLIPFONT = 268435456;
  public static final int CONFIG_FONT_SCALE = 1073741824;
  public static final int CONFIG_KEYBOARD = 16;
  public static final int CONFIG_KEYBOARD_HIDDEN = 32;
  public static final int CONFIG_LAYOUT_DIRECTION = 8192;
  public static final int CONFIG_LOCALE = 4;
  public static final int CONFIG_MCC = 1;
  public static final int CONFIG_MNC = 2;
  public static int[] CONFIG_NATIVE_BITS = { 2, 1, 4, 8, 16, 32, 64, 128, 2048, 4096, 512, 8192, 256, 16384, 65536, 32768 };
  public static final int CONFIG_NAVIGATION = 64;
  public static final int CONFIG_ORIENTATION = 128;
  public static final int CONFIG_SCREEN_LAYOUT = 256;
  public static final int CONFIG_SCREEN_SIZE = 1024;
  public static final int CONFIG_SMALLEST_SCREEN_SIZE = 2048;
  public static final int CONFIG_TOUCHSCREEN = 8;
  public static final int CONFIG_UI_MODE = 512;
  public static final int CONFIG_WINDOW_CONFIGURATION = 536870912;
  public static final Parcelable.Creator<ActivityInfo> CREATOR = new Parcelable.Creator()
  {
    public ActivityInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new ActivityInfo(paramAnonymousParcel, null);
    }
    
    public ActivityInfo[] newArray(int paramAnonymousInt)
    {
      return new ActivityInfo[paramAnonymousInt];
    }
  };
  public static final int DOCUMENT_LAUNCH_ALWAYS = 2;
  public static final int DOCUMENT_LAUNCH_INTO_EXISTING = 1;
  public static final int DOCUMENT_LAUNCH_NEVER = 3;
  public static final int DOCUMENT_LAUNCH_NONE = 0;
  public static final int FLAG_ALLOW_EMBEDDED = Integer.MIN_VALUE;
  public static final int FLAG_ALLOW_TASK_REPARENTING = 64;
  public static final int FLAG_ALWAYS_FOCUSABLE = 262144;
  public static final int FLAG_ALWAYS_RETAIN_TASK_STATE = 8;
  public static final int FLAG_AUTO_REMOVE_FROM_RECENTS = 8192;
  public static final int FLAG_CLEAR_TASK_ON_LAUNCH = 4;
  public static final int FLAG_ENABLE_VR_MODE = 32768;
  public static final int FLAG_EXCLUDE_FROM_RECENTS = 32;
  public static final int FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS = 256;
  public static final int FLAG_FINISH_ON_TASK_LAUNCH = 2;
  public static final int FLAG_HARDWARE_ACCELERATED = 512;
  public static final int FLAG_IMMERSIVE = 2048;
  public static final int FLAG_IMPLICITLY_VISIBLE_TO_INSTANT_APP = 2097152;
  public static final int FLAG_MULTIPROCESS = 1;
  public static final int FLAG_NO_HISTORY = 128;
  public static final int FLAG_RELINQUISH_TASK_IDENTITY = 4096;
  public static final int FLAG_RESUME_WHILE_PAUSING = 16384;
  public static final int FLAG_SHOW_FOR_ALL_USERS = 1024;
  public static final int FLAG_SHOW_WHEN_LOCKED = 8388608;
  public static final int FLAG_SINGLE_USER = 1073741824;
  public static final int FLAG_STATE_NOT_NEEDED = 16;
  public static final int FLAG_SUPPORTS_PICTURE_IN_PICTURE = 4194304;
  public static final int FLAG_SYSTEM_USER_ONLY = 536870912;
  public static final int FLAG_TURN_SCREEN_ON = 16777216;
  public static final int FLAG_VISIBLE_TO_INSTANT_APP = 1048576;
  public static final int LAUNCH_MULTIPLE = 0;
  public static final int LAUNCH_SINGLE_INSTANCE = 3;
  public static final int LAUNCH_SINGLE_TASK = 2;
  public static final int LAUNCH_SINGLE_TOP = 1;
  public static final int LOCK_TASK_LAUNCH_MODE_ALWAYS = 2;
  public static final int LOCK_TASK_LAUNCH_MODE_DEFAULT = 0;
  public static final int LOCK_TASK_LAUNCH_MODE_IF_WHITELISTED = 3;
  public static final int LOCK_TASK_LAUNCH_MODE_NEVER = 1;
  public static final int NOTCH_UI_MODE_AVAILABLE = 16;
  public static final int NOTCH_UI_MODE_FILL = 32;
  public static final int PERSIST_ACROSS_REBOOTS = 2;
  public static final int PERSIST_NEVER = 1;
  public static final int PERSIST_ROOT_ONLY = 0;
  public static final int RESIZE_MODE_FORCE_RESIZABLE_LANDSCAPE_ONLY = 5;
  public static final int RESIZE_MODE_FORCE_RESIZABLE_PORTRAIT_ONLY = 6;
  public static final int RESIZE_MODE_FORCE_RESIZABLE_PRESERVE_ORIENTATION = 7;
  public static final int RESIZE_MODE_FORCE_RESIZEABLE = 4;
  public static final int RESIZE_MODE_RESIZEABLE = 2;
  public static final int RESIZE_MODE_RESIZEABLE_AND_PIPABLE_DEPRECATED = 3;
  public static final int RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION = 1;
  public static final int RESIZE_MODE_UNRESIZEABLE = 0;
  public static final int SCALE_MODE_AVAILABLE = 1;
  public static final int SCALE_MODE_FIT_SCREEN = 2;
  public static final int SCREEN_ORIENTATION_BEHIND = 3;
  public static final int SCREEN_ORIENTATION_FULL_SENSOR = 10;
  public static final int SCREEN_ORIENTATION_FULL_USER = 13;
  public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
  public static final int SCREEN_ORIENTATION_LOCKED = 14;
  public static final int SCREEN_ORIENTATION_NOSENSOR = 5;
  public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
  public static final int SCREEN_ORIENTATION_REVERSE_LANDSCAPE = 8;
  public static final int SCREEN_ORIENTATION_REVERSE_PORTRAIT = 9;
  public static final int SCREEN_ORIENTATION_SENSOR = 4;
  public static final int SCREEN_ORIENTATION_SENSOR_LANDSCAPE = 6;
  public static final int SCREEN_ORIENTATION_SENSOR_PORTRAIT = 7;
  public static final int SCREEN_ORIENTATION_UNSET = -2;
  public static final int SCREEN_ORIENTATION_UNSPECIFIED = -1;
  public static final int SCREEN_ORIENTATION_USER = 2;
  public static final int SCREEN_ORIENTATION_USER_LANDSCAPE = 11;
  public static final int SCREEN_ORIENTATION_USER_PORTRAIT = 12;
  public static final int UIOPTION_SPLIT_ACTION_BAR_WHEN_NARROW = 1;
  public int colorMode = 0;
  public int configChanges;
  public int documentLaunchMode;
  public int flags;
  public int launchMode;
  public String launchToken;
  public int lockTaskLaunchMode;
  public float maxAspectRatio;
  public int maxRecents;
  public String parentActivityName;
  public String permission;
  public int persistableMode;
  public String requestedVrComponent;
  public int resizeMode = 2;
  public int rotationAnimation = -1;
  public int screenOrientation = -1;
  public int softInputMode;
  public String targetActivity;
  public String taskAffinity;
  public int theme;
  public int uiOptions = 0;
  public WindowLayout windowLayout;
  
  public ActivityInfo() {}
  
  public ActivityInfo(ActivityInfo paramActivityInfo)
  {
    super(paramActivityInfo);
    theme = theme;
    launchMode = launchMode;
    documentLaunchMode = documentLaunchMode;
    permission = permission;
    taskAffinity = taskAffinity;
    targetActivity = targetActivity;
    flags = flags;
    screenOrientation = screenOrientation;
    configChanges = configChanges;
    softInputMode = softInputMode;
    uiOptions = uiOptions;
    parentActivityName = parentActivityName;
    maxRecents = maxRecents;
    lockTaskLaunchMode = lockTaskLaunchMode;
    windowLayout = windowLayout;
    resizeMode = resizeMode;
    requestedVrComponent = requestedVrComponent;
    rotationAnimation = rotationAnimation;
    colorMode = colorMode;
    maxAspectRatio = maxAspectRatio;
  }
  
  private ActivityInfo(Parcel paramParcel)
  {
    super(paramParcel);
    theme = paramParcel.readInt();
    launchMode = paramParcel.readInt();
    documentLaunchMode = paramParcel.readInt();
    permission = paramParcel.readString();
    taskAffinity = paramParcel.readString();
    targetActivity = paramParcel.readString();
    launchToken = paramParcel.readString();
    flags = paramParcel.readInt();
    screenOrientation = paramParcel.readInt();
    configChanges = paramParcel.readInt();
    softInputMode = paramParcel.readInt();
    uiOptions = paramParcel.readInt();
    parentActivityName = paramParcel.readString();
    persistableMode = paramParcel.readInt();
    maxRecents = paramParcel.readInt();
    lockTaskLaunchMode = paramParcel.readInt();
    if (paramParcel.readInt() == 1) {
      windowLayout = new WindowLayout(paramParcel);
    }
    resizeMode = paramParcel.readInt();
    requestedVrComponent = paramParcel.readString();
    rotationAnimation = paramParcel.readInt();
    colorMode = paramParcel.readInt();
    maxAspectRatio = paramParcel.readFloat();
  }
  
  public static int activityInfoConfigJavaToNative(int paramInt)
  {
    int i = 0;
    int j = 0;
    while (j < CONFIG_NATIVE_BITS.length)
    {
      int k = i;
      if ((1 << j & paramInt) != 0) {
        k = i | CONFIG_NATIVE_BITS[j];
      }
      j++;
      i = k;
    }
    return i;
  }
  
  public static int activityInfoConfigNativeToJava(int paramInt)
  {
    int i = 0;
    int j = 0;
    while (j < CONFIG_NATIVE_BITS.length)
    {
      int k = i;
      if ((CONFIG_NATIVE_BITS[j] & paramInt) != 0) {
        k = i | 1 << j;
      }
      j++;
      i = k;
    }
    return i;
  }
  
  public static String colorModeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 2: 
      return "COLOR_MODE_HDR";
    case 1: 
      return "COLOR_MODE_WIDE_COLOR_GAMUT";
    }
    return "COLOR_MODE_DEFAULT";
  }
  
  public static boolean isFixedOrientationLandscape(int paramInt)
  {
    boolean bool;
    if ((paramInt != 0) && (paramInt != 6) && (paramInt != 8) && (paramInt != 11)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isFixedOrientationPortrait(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 1)
    {
      bool2 = bool1;
      if (paramInt != 7)
      {
        bool2 = bool1;
        if (paramInt != 9) {
          if (paramInt == 12) {
            bool2 = bool1;
          } else {
            bool2 = false;
          }
        }
      }
    }
    return bool2;
  }
  
  public static boolean isPreserveOrientationMode(int paramInt)
  {
    boolean bool;
    if ((paramInt != 6) && (paramInt != 5) && (paramInt != 7)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public static boolean isResizeableMode(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = bool1;
    if (paramInt != 2)
    {
      bool2 = bool1;
      if (paramInt != 4)
      {
        bool2 = bool1;
        if (paramInt != 6)
        {
          bool2 = bool1;
          if (paramInt != 5)
          {
            bool2 = bool1;
            if (paramInt != 7) {
              if (paramInt == 1) {
                bool2 = bool1;
              } else {
                bool2 = false;
              }
            }
          }
        }
      }
    }
    return bool2;
  }
  
  public static boolean isTranslucentOrFloating(TypedArray paramTypedArray)
  {
    boolean bool1 = false;
    boolean bool2 = paramTypedArray.getBoolean(5, false);
    int i;
    if ((!paramTypedArray.hasValue(5)) && (paramTypedArray.getBoolean(25, false))) {
      i = 1;
    } else {
      i = 0;
    }
    if ((!paramTypedArray.getBoolean(4, false)) && (!bool2) && (i == 0)) {
      return bool1;
    }
    bool1 = true;
    return bool1;
  }
  
  public static final String lockTaskLaunchModeToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown=");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 3: 
      return "LOCK_TASK_LAUNCH_MODE_IF_WHITELISTED";
    case 2: 
      return "LOCK_TASK_LAUNCH_MODE_ALWAYS";
    case 1: 
      return "LOCK_TASK_LAUNCH_MODE_NEVER";
    }
    return "LOCK_TASK_LAUNCH_MODE_DEFAULT";
  }
  
  private String persistableModeToString()
  {
    switch (persistableMode)
    {
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("UNKNOWN=");
      localStringBuilder.append(persistableMode);
      return localStringBuilder.toString();
    case 2: 
      return "PERSIST_ACROSS_REBOOTS";
    case 1: 
      return "PERSIST_NEVER";
    }
    return "PERSIST_ROOT_ONLY";
  }
  
  public static String resizeModeToString(int paramInt)
  {
    switch (paramInt)
    {
    case 3: 
    default: 
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("unknown=");
      localStringBuilder.append(paramInt);
      return localStringBuilder.toString();
    case 7: 
      return "RESIZE_MODE_FORCE_RESIZABLE_PRESERVE_ORIENTATION";
    case 6: 
      return "RESIZE_MODE_FORCE_RESIZABLE_PORTRAIT_ONLY";
    case 5: 
      return "RESIZE_MODE_FORCE_RESIZABLE_LANDSCAPE_ONLY";
    case 4: 
      return "RESIZE_MODE_FORCE_RESIZEABLE";
    case 2: 
      return "RESIZE_MODE_RESIZEABLE";
    case 1: 
      return "RESIZE_MODE_RESIZEABLE_VIA_SDK_VERSION";
    }
    return "RESIZE_MODE_UNRESIZEABLE";
  }
  
  public static String screenOrientationToString(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return Integer.toString(paramInt);
    case 14: 
      return "SCREEN_ORIENTATION_LOCKED";
    case 13: 
      return "SCREEN_ORIENTATION_FULL_USER";
    case 12: 
      return "SCREEN_ORIENTATION_USER_PORTRAIT";
    case 11: 
      return "SCREEN_ORIENTATION_USER_LANDSCAPE";
    case 10: 
      return "SCREEN_ORIENTATION_FULL_SENSOR";
    case 9: 
      return "SCREEN_ORIENTATION_REVERSE_PORTRAIT";
    case 8: 
      return "SCREEN_ORIENTATION_REVERSE_LANDSCAPE";
    case 7: 
      return "SCREEN_ORIENTATION_SENSOR_PORTRAIT";
    case 6: 
      return "SCREEN_ORIENTATION_SENSOR_LANDSCAPE";
    case 5: 
      return "SCREEN_ORIENTATION_NOSENSOR";
    case 4: 
      return "SCREEN_ORIENTATION_SENSOR";
    case 3: 
      return "SCREEN_ORIENTATION_BEHIND";
    case 2: 
      return "SCREEN_ORIENTATION_USER";
    case 1: 
      return "SCREEN_ORIENTATION_PORTRAIT";
    case 0: 
      return "SCREEN_ORIENTATION_LANDSCAPE";
    case -1: 
      return "SCREEN_ORIENTATION_UNSPECIFIED";
    }
    return "SCREEN_ORIENTATION_UNSET";
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void dump(Printer paramPrinter, String paramString)
  {
    dump(paramPrinter, paramString, 3);
  }
  
  public void dump(Printer paramPrinter, String paramString, int paramInt)
  {
    super.dumpFront(paramPrinter, paramString);
    if (permission != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("permission=");
      localStringBuilder.append(permission);
      paramPrinter.println(localStringBuilder.toString());
    }
    if ((paramInt & 0x1) != 0)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("taskAffinity=");
      localStringBuilder.append(taskAffinity);
      localStringBuilder.append(" targetActivity=");
      localStringBuilder.append(targetActivity);
      localStringBuilder.append(" persistableMode=");
      localStringBuilder.append(persistableModeToString());
      paramPrinter.println(localStringBuilder.toString());
    }
    if ((launchMode != 0) || (flags != 0) || (theme != 0))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("launchMode=");
      localStringBuilder.append(launchMode);
      localStringBuilder.append(" flags=0x");
      localStringBuilder.append(Integer.toHexString(flags));
      localStringBuilder.append(" theme=0x");
      localStringBuilder.append(Integer.toHexString(theme));
      paramPrinter.println(localStringBuilder.toString());
    }
    if ((screenOrientation != -1) || (configChanges != 0) || (softInputMode != 0))
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("screenOrientation=");
      localStringBuilder.append(screenOrientation);
      localStringBuilder.append(" configChanges=0x");
      localStringBuilder.append(Integer.toHexString(configChanges));
      localStringBuilder.append(" softInputMode=0x");
      localStringBuilder.append(Integer.toHexString(softInputMode));
      paramPrinter.println(localStringBuilder.toString());
    }
    if (uiOptions != 0)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append(" uiOptions=0x");
      localStringBuilder.append(Integer.toHexString(uiOptions));
      paramPrinter.println(localStringBuilder.toString());
    }
    if ((paramInt & 0x1) != 0)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("lockTaskLaunchMode=");
      localStringBuilder.append(lockTaskLaunchModeToString(lockTaskLaunchMode));
      paramPrinter.println(localStringBuilder.toString());
    }
    if (windowLayout != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("windowLayout=");
      localStringBuilder.append(windowLayout.width);
      localStringBuilder.append("|");
      localStringBuilder.append(windowLayout.widthFraction);
      localStringBuilder.append(", ");
      localStringBuilder.append(windowLayout.height);
      localStringBuilder.append("|");
      localStringBuilder.append(windowLayout.heightFraction);
      localStringBuilder.append(", ");
      localStringBuilder.append(windowLayout.gravity);
      paramPrinter.println(localStringBuilder.toString());
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString);
    localStringBuilder.append("resizeMode=");
    localStringBuilder.append(resizeModeToString(resizeMode));
    paramPrinter.println(localStringBuilder.toString());
    if (requestedVrComponent != null)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("requestedVrComponent=");
      localStringBuilder.append(requestedVrComponent);
      paramPrinter.println(localStringBuilder.toString());
    }
    if (maxAspectRatio != 0.0F)
    {
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("maxAspectRatio=");
      localStringBuilder.append(maxAspectRatio);
      paramPrinter.println(localStringBuilder.toString());
    }
    super.dumpBack(paramPrinter, paramString, paramInt);
  }
  
  public int getRealConfigChanged()
  {
    int i;
    if (applicationInfo.targetSdkVersion < 13) {
      i = configChanges | 0x400 | 0x800;
    } else {
      i = configChanges;
    }
    return i;
  }
  
  public final int getThemeResource()
  {
    int i;
    if (theme != 0) {
      i = theme;
    } else {
      i = applicationInfo.theme;
    }
    return i;
  }
  
  boolean isFixedOrientation()
  {
    boolean bool;
    if ((!isFixedOrientationLandscape()) && (!isFixedOrientationPortrait()) && (screenOrientation != 14)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  boolean isFixedOrientationLandscape()
  {
    return isFixedOrientationLandscape(screenOrientation);
  }
  
  boolean isFixedOrientationPortrait()
  {
    return isFixedOrientationPortrait(screenOrientation);
  }
  
  public boolean supportsPictureInPicture()
  {
    boolean bool;
    if ((flags & 0x400000) != 0) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("ActivityInfo{");
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    localStringBuilder.append(" ");
    localStringBuilder.append(name);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(theme);
    paramParcel.writeInt(launchMode);
    paramParcel.writeInt(documentLaunchMode);
    paramParcel.writeString(permission);
    paramParcel.writeString(taskAffinity);
    paramParcel.writeString(targetActivity);
    paramParcel.writeString(launchToken);
    paramParcel.writeInt(flags);
    paramParcel.writeInt(screenOrientation);
    paramParcel.writeInt(configChanges);
    paramParcel.writeInt(softInputMode);
    paramParcel.writeInt(uiOptions);
    paramParcel.writeString(parentActivityName);
    paramParcel.writeInt(persistableMode);
    paramParcel.writeInt(maxRecents);
    paramParcel.writeInt(lockTaskLaunchMode);
    if (windowLayout != null)
    {
      paramParcel.writeInt(1);
      paramParcel.writeInt(windowLayout.width);
      paramParcel.writeFloat(windowLayout.widthFraction);
      paramParcel.writeInt(windowLayout.height);
      paramParcel.writeFloat(windowLayout.heightFraction);
      paramParcel.writeInt(windowLayout.gravity);
      paramParcel.writeInt(windowLayout.minWidth);
      paramParcel.writeInt(windowLayout.minHeight);
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeInt(resizeMode);
    paramParcel.writeString(requestedVrComponent);
    paramParcel.writeInt(rotationAnimation);
    paramParcel.writeInt(colorMode);
    paramParcel.writeFloat(maxAspectRatio);
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ColorMode {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Config {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface ScreenOrientation {}
  
  public static final class WindowLayout
  {
    public final int gravity;
    public final int height;
    public final float heightFraction;
    public final int minHeight;
    public final int minWidth;
    public final int width;
    public final float widthFraction;
    
    public WindowLayout(int paramInt1, float paramFloat1, int paramInt2, float paramFloat2, int paramInt3, int paramInt4, int paramInt5)
    {
      width = paramInt1;
      widthFraction = paramFloat1;
      height = paramInt2;
      heightFraction = paramFloat2;
      gravity = paramInt3;
      minWidth = paramInt4;
      minHeight = paramInt5;
    }
    
    WindowLayout(Parcel paramParcel)
    {
      width = paramParcel.readInt();
      widthFraction = paramParcel.readFloat();
      height = paramParcel.readInt();
      heightFraction = paramParcel.readFloat();
      gravity = paramParcel.readInt();
      minWidth = paramParcel.readInt();
      minHeight = paramParcel.readInt();
    }
  }
}
