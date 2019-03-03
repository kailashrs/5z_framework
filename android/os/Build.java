package android.os;

import android.annotation.SystemApi;
import android.text.TextUtils;
import android.util.Slog;
import dalvik.system.VMRuntime;
import java.io.File;
import java.util.Objects;

public class Build
{
  public static final String ASUSCID;
  public static final String ASUSSKU;
  public static final String BOARD;
  public static final String BOOTLOADER;
  public static final String BRAND;
  public static final String CHARACTERISTICS;
  public static final String COUNTRYCODE;
  @Deprecated
  public static final String CPU_ABI;
  @Deprecated
  public static final String CPU_ABI2;
  public static final String DEVICE;
  public static final String DISPLAY;
  public static final String FINGERPRINT;
  public static final String HARDWARE;
  public static final String HOST;
  public static final String ID = getString("ro.build.id");
  public static final boolean ISASUSCNSKU;
  public static final boolean ISASUSVERMAX;
  public static final boolean IS_CONTAINER;
  public static final boolean IS_DEBUGGABLE;
  public static final boolean IS_EMULATOR;
  public static final boolean IS_ENG;
  public static final boolean IS_TREBLE_ENABLED;
  public static final boolean IS_USER;
  public static final boolean IS_USERDEBUG;
  public static final String MANUFACTURER;
  public static final String MODEL;
  @SystemApi
  public static final boolean PERMISSIONS_REVIEW_REQUIRED;
  public static final String PRODUCT;
  @Deprecated
  public static final String RADIO;
  @Deprecated
  public static final String SERIAL;
  public static final String[] SUPPORTED_32_BIT_ABIS;
  public static final String[] SUPPORTED_64_BIT_ABIS;
  public static final String[] SUPPORTED_ABIS;
  private static final String TAG = "Build";
  public static final String TAGS;
  public static final long TIME;
  public static final String TYPE;
  public static final String UNKNOWN = "unknown";
  public static final String USER;
  
  static
  {
    DISPLAY = getString("ro.build.display.id");
    PRODUCT = getString("ro.product.name");
    DEVICE = getString("ro.product.device");
    BOARD = getString("ro.product.board");
    MANUFACTURER = getString("ro.product.manufacturer");
    BRAND = getString("ro.product.brand");
    MODEL = getString("ro.product.model");
    BOOTLOADER = getString("ro.bootloader");
    ASUSSKU = getString("ro.build.asus.sku", "WW");
    ASUSCID = getString("ro.config.CID", "unknown");
    COUNTRYCODE = getString("ro.config.versatility", "WW");
    CHARACTERISTICS = getString("ro.build.characteristics");
    boolean bool1 = ASUSSKU.toUpperCase().equals("ATT");
    boolean bool2 = true;
    if ((!bool1) && (!ASUSSKU.toUpperCase().equals("VZW"))) {
      bool1 = false;
    } else {
      bool1 = true;
    }
    ISASUSVERMAX = bool1;
    ISASUSCNSKU = "CN".equals(ASUSSKU);
    RADIO = getString("gsm.version.baseband");
    HARDWARE = getString("ro.hardware");
    IS_EMULATOR = getString("ro.kernel.qemu").equals("1");
    SERIAL = getString("no.such.thing");
    SUPPORTED_ABIS = getStringList("ro.product.cpu.abilist", ",");
    SUPPORTED_32_BIT_ABIS = getStringList("ro.product.cpu.abilist32", ",");
    SUPPORTED_64_BIT_ABIS = getStringList("ro.product.cpu.abilist64", ",");
    String[] arrayOfString;
    if (VMRuntime.getRuntime().is64Bit()) {
      arrayOfString = SUPPORTED_64_BIT_ABIS;
    } else {
      arrayOfString = SUPPORTED_32_BIT_ABIS;
    }
    CPU_ABI = arrayOfString[0];
    if (arrayOfString.length > 1) {
      CPU_ABI2 = arrayOfString[1];
    } else {
      CPU_ABI2 = "";
    }
    TYPE = getString("ro.build.type");
    TAGS = getString("ro.build.tags");
    FINGERPRINT = deriveFingerprint();
    IS_TREBLE_ENABLED = SystemProperties.getBoolean("ro.treble.enabled", false);
    TIME = getLong("ro.build.date.utc") * 1000L;
    USER = getString("ro.build.user");
    HOST = getString("ro.build.host");
    if (SystemProperties.getInt("ro.debuggable", 0) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    IS_DEBUGGABLE = bool1;
    IS_ENG = "eng".equals(TYPE);
    IS_USERDEBUG = "userdebug".equals(TYPE);
    IS_USER = "user".equals(TYPE);
    IS_CONTAINER = SystemProperties.getBoolean("ro.boot.container", false);
    if (SystemProperties.getInt("ro.permission_review_required", 0) == 1) {
      bool1 = bool2;
    } else {
      bool1 = false;
    }
    PERMISSIONS_REVIEW_REQUIRED = bool1;
  }
  
  public Build() {}
  
  private static String deriveFingerprint()
  {
    String str = SystemProperties.get("ro.build.fingerprint");
    Object localObject = str;
    if (TextUtils.isEmpty(str))
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append(getString("ro.product.brand"));
      ((StringBuilder)localObject).append('/');
      ((StringBuilder)localObject).append(getString("ro.product.name"));
      ((StringBuilder)localObject).append('/');
      ((StringBuilder)localObject).append(getString("ro.product.device"));
      ((StringBuilder)localObject).append(':');
      ((StringBuilder)localObject).append(getString("ro.build.version.release"));
      ((StringBuilder)localObject).append('/');
      ((StringBuilder)localObject).append(getString("ro.build.id"));
      ((StringBuilder)localObject).append('/');
      ((StringBuilder)localObject).append(getString("ro.build.version.incremental"));
      ((StringBuilder)localObject).append(':');
      ((StringBuilder)localObject).append(getString("ro.build.type"));
      ((StringBuilder)localObject).append('/');
      ((StringBuilder)localObject).append(getString("ro.build.tags"));
      localObject = ((StringBuilder)localObject).toString();
    }
    return localObject;
  }
  
  public static void ensureFingerprintProperty()
  {
    if (TextUtils.isEmpty(SystemProperties.get("ro.build.fingerprint"))) {
      try
      {
        SystemProperties.set("ro.build.fingerprint", FINGERPRINT);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Slog.e("Build", "Failed to set fingerprint property", localIllegalArgumentException);
      }
    }
  }
  
  private static long getLong(String paramString)
  {
    try
    {
      long l = Long.parseLong(SystemProperties.get(paramString));
      return l;
    }
    catch (NumberFormatException paramString) {}
    return -1L;
  }
  
  public static int getOnReceiveTimeLimit()
  {
    int i = 50;
    if (IS_DEBUGGABLE) {
      i = SystemProperties.getInt("debug.asus.onreceive_timelimit", 50);
    }
    return i;
  }
  
  public static int getQueryTimeLimit()
  {
    int i = 50;
    if (IS_DEBUGGABLE) {
      i = SystemProperties.getInt("debug.asus.query_timelimit", 50);
    }
    return i;
  }
  
  public static String getRadioVersion()
  {
    return SystemProperties.get("gsm.version.baseband", null);
  }
  
  public static String getSerial()
  {
    Object localObject = IDeviceIdentifiersPolicyService.Stub.asInterface(ServiceManager.getService("device_identifiers"));
    try
    {
      localObject = ((IDeviceIdentifiersPolicyService)localObject).getSerial();
      return localObject;
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.rethrowFromSystemServer();
    }
    return "unknown";
  }
  
  private static String getString(String paramString)
  {
    return SystemProperties.get(paramString, "unknown");
  }
  
  private static String getString(String paramString1, String paramString2)
  {
    return SystemProperties.get(paramString1, paramString2);
  }
  
  private static String[] getStringList(String paramString1, String paramString2)
  {
    paramString1 = SystemProperties.get(paramString1);
    if (paramString1.isEmpty()) {
      return new String[0];
    }
    return paramString1.split(paramString2);
  }
  
  public static boolean isBuildConsistent()
  {
    boolean bool1 = IS_ENG;
    boolean bool2 = true;
    if (bool1) {
      return true;
    }
    if (IS_TREBLE_ENABLED)
    {
      int i = VintfObject.verifyWithoutAvb();
      if (i != 0)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Vendor interface is incompatible, error=");
        ((StringBuilder)localObject).append(String.valueOf(i));
        Slog.e("Build", ((StringBuilder)localObject).toString());
      }
      if (i != 0) {
        bool2 = false;
      }
      return bool2;
    }
    Object localObject = SystemProperties.get("ro.build.fingerprint");
    String str = SystemProperties.get("ro.vendor.build.fingerprint");
    SystemProperties.get("ro.bootimage.build.fingerprint");
    SystemProperties.get("ro.build.expect.bootloader");
    SystemProperties.get("ro.bootloader");
    SystemProperties.get("ro.build.expect.baseband");
    SystemProperties.get("gsm.version.baseband");
    if (TextUtils.isEmpty((CharSequence)localObject))
    {
      Slog.e("Build", "Required ro.build.fingerprint is empty!");
      return false;
    }
    if ((!TextUtils.isEmpty(str)) && (!Objects.equals(localObject, str)))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Mismatched fingerprints; system reported ");
      localStringBuilder.append((String)localObject);
      localStringBuilder.append(" but vendor reported ");
      localStringBuilder.append(str);
      Slog.e("Build", localStringBuilder.toString());
      return false;
    }
    return true;
  }
  
  public static boolean isPerformanceDebugging()
  {
    boolean bool = false;
    if (IS_DEBUGGABLE)
    {
      bool = false;
      if (SystemProperties.getInt("debug.asus.performance", 0) == 1) {
        bool = true;
      }
    }
    return bool;
  }
  
  public static boolean isRamEnoughForOptiFlex()
  {
    return FEATURES.IS_RAM_ENOUGH_FOR_OPTIFLEX;
  }
  
  public static class FEATURES
  {
    public static boolean ENABLE_ALWAYS_ON;
    public static boolean ENABLE_APPWIDGET_UPDATE_EARLY = SystemProperties.getBoolean("persist.asus.widgetearly", true);
    public static final boolean ENABLE_APP_SCALING;
    public static boolean ENABLE_ASUS_DATA_TRANSFER;
    public static boolean ENABLE_ASUS_EVTLOG;
    public static final boolean ENABLE_AUTO_START;
    public static final boolean ENABLE_COLORFUL_NAV;
    public static boolean ENABLE_DOZEMODE_LOG = true;
    public static boolean ENABLE_FINGERPRINT_AS_HOME;
    public static boolean ENABLE_FINGERPRINT_EARLYWAKEUP;
    public static boolean ENABLE_GESTURE_DETECTOR_SERVICE;
    public static boolean ENABLE_INADVERTENTTOUCH;
    public static boolean ENABLE_LONG_SCREENSHOT;
    public static boolean ENABLE_MISC_SETUP;
    public static final boolean ENABLE_NAV_VIS_CTRL;
    public static final boolean ENABLE_NOTCH_UI;
    public static boolean ENABLE_ONE_HAND_CTRL;
    public static boolean ENABLE_OPTIFLEX;
    public static boolean ENABLE_POINTER_LISTENER;
    public static boolean ENABLE_PROXIMITY_MECHANISM = true;
    public static boolean ENABLE_SHIELD_KEYCODE;
    public static boolean ENABLE_SPLENDID_WIDECOLORGAMUT;
    public static final boolean ENABLE_START_TRACKER;
    public static boolean ENABLE_SYSTEMMONITOR_SERVICE;
    public static boolean ENABLE_TWIN_APPS;
    public static boolean HAS_GLOVE_MODE;
    public static boolean HAS_TOUCHPAD;
    public static boolean HAS_TRANSCOVER;
    public static boolean IS_CN_NOTIFICATION;
    public static boolean IS_RAM_ENOUGH_FOR_OPTIFLEX;
    public static final boolean VZW;
    
    static
    {
      ENABLE_ASUS_EVTLOG = new File("/proc/asusevtlog").exists();
      ENABLE_TWIN_APPS = false;
      ENABLE_ALWAYS_ON = false;
      ENABLE_FINGERPRINT_EARLYWAKEUP = false;
      ENABLE_COLORFUL_NAV = SystemProperties.getBoolean("persist.sys.colornav.enable", true);
      ENABLE_NAV_VIS_CTRL = SystemProperties.getBoolean("persist.sys.navctrl.enable", true);
      ENABLE_APP_SCALING = SystemProperties.getBoolean("persist.sys.scaling.enable", true);
      ENABLE_NOTCH_UI = SystemProperties.getBoolean("persist.sys.notchui.enable", false);
      ENABLE_AUTO_START = SystemProperties.getBoolean("persist.sys.autostart.enable", true);
      boolean bool;
      if ((!SystemProperties.getBoolean("persist.sys.starttracker.enable", false)) && (!Build.ASUSSKU.toUpperCase().equals("CN")) && (!Build.ASUSSKU.toUpperCase().equals("CUCC")) && (!Build.ASUSSKU.toUpperCase().equals("CMCC"))) {
        bool = false;
      } else {
        bool = true;
      }
      ENABLE_START_TRACKER = bool;
      if ((Build.PRODUCT.length() >= 4) && (Build.PRODUCT.substring(0, 4).equals("VZW_"))) {
        bool = true;
      } else {
        bool = false;
      }
      VZW = bool;
      ENABLE_SYSTEMMONITOR_SERVICE = true;
      ENABLE_OPTIFLEX = SystemProperties.getBoolean("asus.software.optiflex", false);
      IS_RAM_ENOUGH_FOR_OPTIFLEX = false;
      ENABLE_MISC_SETUP = true;
      HAS_TRANSCOVER = false;
      if ((!Build.PRODUCT.startsWith("CN")) && (!Build.PRODUCT.startsWith("cn"))) {
        bool = false;
      } else {
        bool = true;
      }
      IS_CN_NOTIFICATION = bool;
      HAS_GLOVE_MODE = false;
      ENABLE_FINGERPRINT_AS_HOME = "front_woh".startsWith(SystemProperties.get("ro.hardware.fp_position"));
      ENABLE_POINTER_LISTENER = true;
      if (((Build.PRODUCT.startsWith("CN")) || (Build.PRODUCT.startsWith("cn"))) && (SystemProperties.getBoolean("ro.asus.cnsmartscreenshot", false)) && (ENABLE_POINTER_LISTENER)) {
        bool = true;
      } else {
        bool = false;
      }
      ENABLE_LONG_SCREENSHOT = bool;
      ENABLE_GESTURE_DETECTOR_SERVICE = false;
      if ((!Build.PRODUCT.startsWith("CN")) && (!Build.PRODUCT.startsWith("cn"))) {
        bool = false;
      } else {
        bool = true;
      }
      ENABLE_SHIELD_KEYCODE = bool;
      if ((!Build.PRODUCT.startsWith("CN")) && (!Build.PRODUCT.startsWith("cn"))) {
        bool = true;
      } else {
        bool = false;
      }
      ENABLE_INADVERTENTTOUCH = bool;
      HAS_TOUCHPAD = false;
      ENABLE_ASUS_DATA_TRANSFER = true;
      ENABLE_SPLENDID_WIDECOLORGAMUT = true;
      ENABLE_ONE_HAND_CTRL = SystemProperties.getBoolean("persist.sys.onehandctrl.enable", false);
    }
    
    public FEATURES() {}
  }
  
  public static class VERSION
  {
    public static final String[] ACTIVE_CODENAMES;
    private static final String[] ALL_CODENAMES;
    public static final String BASE_OS;
    public static final String CODENAME;
    public static final int FIRST_SDK_INT;
    public static final String INCREMENTAL = Build.getString("ro.build.version.incremental");
    public static final int MIN_SUPPORTED_TARGET_SDK_INT = SystemProperties.getInt("ro.build.version.min_supported_target_sdk", 0);
    public static final int PREVIEW_SDK_INT;
    public static final String RELEASE = Build.getString("ro.build.version.release");
    public static final int RESOURCES_SDK_INT;
    @Deprecated
    public static final String SDK;
    public static final int SDK_INT;
    public static final String SECURITY_PATCH;
    
    static
    {
      BASE_OS = SystemProperties.get("ro.build.version.base_os", "");
      SECURITY_PATCH = SystemProperties.get("ro.build.version.security_patch", "");
      SDK = Build.getString("ro.build.version.sdk");
      SDK_INT = SystemProperties.getInt("ro.build.version.sdk", 0);
      FIRST_SDK_INT = SystemProperties.getInt("ro.product.first_api_level", 0);
      PREVIEW_SDK_INT = SystemProperties.getInt("ro.build.version.preview_sdk", 0);
      CODENAME = Build.getString("ro.build.version.codename");
      ALL_CODENAMES = Build.getStringList("ro.build.version.all_codenames", ",");
      String[] arrayOfString;
      if ("REL".equals(ALL_CODENAMES[0])) {
        arrayOfString = new String[0];
      } else {
        arrayOfString = ALL_CODENAMES;
      }
      ACTIVE_CODENAMES = arrayOfString;
      RESOURCES_SDK_INT = SDK_INT + ACTIVE_CODENAMES.length;
    }
    
    public VERSION() {}
  }
  
  public static class VERSION_CODES
  {
    public static final int BASE = 1;
    public static final int BASE_1_1 = 2;
    public static final int CUPCAKE = 3;
    public static final int CUR_DEVELOPMENT = 10000;
    public static final int DONUT = 4;
    public static final int ECLAIR = 5;
    public static final int ECLAIR_0_1 = 6;
    public static final int ECLAIR_MR1 = 7;
    public static final int FROYO = 8;
    public static final int GINGERBREAD = 9;
    public static final int GINGERBREAD_MR1 = 10;
    public static final int HONEYCOMB = 11;
    public static final int HONEYCOMB_MR1 = 12;
    public static final int HONEYCOMB_MR2 = 13;
    public static final int ICE_CREAM_SANDWICH = 14;
    public static final int ICE_CREAM_SANDWICH_MR1 = 15;
    public static final int JELLY_BEAN = 16;
    public static final int JELLY_BEAN_MR1 = 17;
    public static final int JELLY_BEAN_MR2 = 18;
    public static final int KITKAT = 19;
    public static final int KITKAT_WATCH = 20;
    public static final int L = 21;
    public static final int LOLLIPOP = 21;
    public static final int LOLLIPOP_MR1 = 22;
    public static final int M = 23;
    public static final int N = 24;
    public static final int N_MR1 = 25;
    public static final int O = 26;
    public static final int O_MR1 = 27;
    public static final int P = 28;
    
    public VERSION_CODES() {}
  }
}
