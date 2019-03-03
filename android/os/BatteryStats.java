package android.os;

import android.app.job.JobParameters;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.telephony.SignalStrength;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import android.util.MutableBoolean;
import android.util.Pair;
import android.util.Printer;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BatterySipper;
import com.android.internal.os.BatterySipper.DrainType;
import com.android.internal.os.BatteryStatsHelper;
import com.android.internal.os.PowerProfile;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class BatteryStats
  implements Parcelable
{
  private static final String AGGREGATED_WAKELOCK_DATA = "awl";
  public static final int AGGREGATED_WAKE_TYPE_PARTIAL = 20;
  private static final String APK_DATA = "apk";
  private static final String AUDIO_DATA = "aud";
  public static final int AUDIO_TURNED_ON = 15;
  private static final String BATTERY_DATA = "bt";
  private static final String BATTERY_DISCHARGE_DATA = "dc";
  private static final String BATTERY_LEVEL_DATA = "lv";
  private static final int BATTERY_STATS_CHECKIN_VERSION = 9;
  private static final String BLUETOOTH_CONTROLLER_DATA = "ble";
  private static final String BLUETOOTH_MISC_DATA = "blem";
  public static final int BLUETOOTH_SCAN_ON = 19;
  public static final int BLUETOOTH_UNOPTIMIZED_SCAN_ON = 21;
  private static final long BYTES_PER_GB = 1073741824L;
  private static final long BYTES_PER_KB = 1024L;
  private static final long BYTES_PER_MB = 1048576L;
  private static final String CAMERA_DATA = "cam";
  public static final int CAMERA_TURNED_ON = 17;
  private static final String CELLULAR_CONTROLLER_NAME = "Cellular";
  private static final String CHARGE_STEP_DATA = "csd";
  private static final String CHARGE_TIME_REMAIN_DATA = "ctr";
  static final int CHECKIN_VERSION = 32;
  private static final String CPU_DATA = "cpu";
  private static final String CPU_TIMES_AT_FREQ_DATA = "ctf";
  private static final String DATA_CONNECTION_COUNT_DATA = "dcc";
  static final String[] DATA_CONNECTION_NAMES;
  public static final int DATA_CONNECTION_NONE = 0;
  public static final int DATA_CONNECTION_OTHER = 20;
  private static final String DATA_CONNECTION_TIME_DATA = "dct";
  public static final int DEVICE_IDLE_MODE_DEEP = 2;
  public static final int DEVICE_IDLE_MODE_LIGHT = 1;
  public static final int DEVICE_IDLE_MODE_OFF = 0;
  private static final String DISCHARGE_STEP_DATA = "dsd";
  private static final String DISCHARGE_TIME_REMAIN_DATA = "dtr";
  public static final int DUMP_CHARGED_ONLY = 2;
  public static final int DUMP_DAILY_ONLY = 4;
  public static final int DUMP_DEVICE_WIFI_ONLY = 64;
  public static final int DUMP_HISTORY_ONLY = 8;
  public static final int DUMP_INCLUDE_HISTORY = 16;
  public static final int DUMP_VERBOSE = 32;
  private static final String FLASHLIGHT_DATA = "fla";
  public static final int FLASHLIGHT_TURNED_ON = 16;
  public static final int FOREGROUND_ACTIVITY = 10;
  private static final String FOREGROUND_ACTIVITY_DATA = "fg";
  public static final int FOREGROUND_SERVICE = 22;
  private static final String FOREGROUND_SERVICE_DATA = "fgs";
  public static final int FULL_WIFI_LOCK = 5;
  private static final String GLOBAL_BLUETOOTH_CONTROLLER_DATA = "gble";
  private static final String GLOBAL_CPU_FREQ_DATA = "gcf";
  private static final String GLOBAL_MODEM_CONTROLLER_DATA = "gmcd";
  private static final String GLOBAL_NETWORK_DATA = "gn";
  private static final String GLOBAL_WIFI_CONTROLLER_DATA = "gwfcd";
  private static final String GLOBAL_WIFI_DATA = "gwfl";
  private static final String HISTORY_DATA = "h";
  public static final String[] HISTORY_EVENT_CHECKIN_NAMES;
  public static final IntToString[] HISTORY_EVENT_INT_FORMATTERS;
  public static final String[] HISTORY_EVENT_NAMES;
  public static final BitDescription[] HISTORY_STATE2_DESCRIPTIONS;
  public static final BitDescription[] HISTORY_STATE_DESCRIPTIONS;
  private static final String HISTORY_STRING_POOL = "hsp";
  public static final int JOB = 14;
  private static final String JOBS_DEFERRED_DATA = "jbd";
  private static final String JOB_COMPLETION_DATA = "jbc";
  private static final String JOB_DATA = "jb";
  public static final long[] JOB_FRESHNESS_BUCKETS;
  private static final String KERNEL_WAKELOCK_DATA = "kwl";
  private static final boolean LOCAL_LOGV = false;
  public static final int MAX_TRACKED_SCREEN_STATE = 4;
  private static final String MISC_DATA = "m";
  private static final String MODEM_CONTROLLER_DATA = "mcd";
  public static final int NETWORK_BT_RX_DATA = 4;
  public static final int NETWORK_BT_TX_DATA = 5;
  private static final String NETWORK_DATA = "nt";
  public static final int NETWORK_MOBILE_BG_RX_DATA = 6;
  public static final int NETWORK_MOBILE_BG_TX_DATA = 7;
  public static final int NETWORK_MOBILE_RX_DATA = 0;
  public static final int NETWORK_MOBILE_TX_DATA = 1;
  public static final int NETWORK_WIFI_BG_RX_DATA = 8;
  public static final int NETWORK_WIFI_BG_TX_DATA = 9;
  public static final int NETWORK_WIFI_RX_DATA = 2;
  public static final int NETWORK_WIFI_TX_DATA = 3;
  public static final int NUM_DATA_CONNECTION_TYPES = 21;
  public static final int NUM_NETWORK_ACTIVITY_TYPES = 10;
  public static final int NUM_SCREEN_BRIGHTNESS_BINS = 5;
  public static final int NUM_WIFI_SIGNAL_STRENGTH_BINS = 5;
  public static final int NUM_WIFI_STATES = 8;
  public static final int NUM_WIFI_SUPPL_STATES = 13;
  private static final String POWER_USE_ITEM_DATA = "pwi";
  private static final String POWER_USE_SUMMARY_DATA = "pws";
  private static final String PROCESS_DATA = "pr";
  public static final int PROCESS_STATE = 12;
  private static final String RESOURCE_POWER_MANAGER_DATA = "rpm";
  public static final String RESULT_RECEIVER_CONTROLLER_KEY = "controller_activity";
  public static final int SCREEN_BRIGHTNESS_BRIGHT = 4;
  public static final int SCREEN_BRIGHTNESS_DARK = 0;
  private static final String SCREEN_BRIGHTNESS_DATA = "br";
  public static final int SCREEN_BRIGHTNESS_DIM = 1;
  public static final int SCREEN_BRIGHTNESS_LIGHT = 3;
  public static final int SCREEN_BRIGHTNESS_MEDIUM = 2;
  static final String[] SCREEN_BRIGHTNESS_NAMES;
  static final String[] SCREEN_BRIGHTNESS_SHORT_NAMES;
  protected static final boolean SCREEN_OFF_RPM_STATS_ENABLED = false;
  public static final int SENSOR = 3;
  private static final String SENSOR_DATA = "sr";
  public static final String SERVICE_NAME = "batterystats";
  private static final String SIGNAL_SCANNING_TIME_DATA = "sst";
  private static final String SIGNAL_STRENGTH_COUNT_DATA = "sgc";
  private static final String SIGNAL_STRENGTH_TIME_DATA = "sgt";
  private static final String STATE_TIME_DATA = "st";
  public static final int STATS_CURRENT = 1;
  public static final int STATS_SINCE_CHARGED = 0;
  public static final int STATS_SINCE_UNPLUGGED = 2;
  private static final String[] STAT_NAMES = { "l", "c", "u" };
  public static final long STEP_LEVEL_INITIAL_MODE_MASK = 71776119061217280L;
  public static final int STEP_LEVEL_INITIAL_MODE_SHIFT = 48;
  public static final long STEP_LEVEL_LEVEL_MASK = 280375465082880L;
  public static final int STEP_LEVEL_LEVEL_SHIFT = 40;
  public static final int[] STEP_LEVEL_MODES_OF_INTEREST;
  public static final int STEP_LEVEL_MODE_DEVICE_IDLE = 8;
  public static final String[] STEP_LEVEL_MODE_LABELS = { "screen off", "screen off power save", "screen off device idle", "screen on", "screen on power save", "screen doze", "screen doze power save", "screen doze-suspend", "screen doze-suspend power save", "screen doze-suspend device idle" };
  public static final int STEP_LEVEL_MODE_POWER_SAVE = 4;
  public static final int STEP_LEVEL_MODE_SCREEN_STATE = 3;
  public static final int[] STEP_LEVEL_MODE_VALUES;
  public static final long STEP_LEVEL_MODIFIED_MODE_MASK = -72057594037927936L;
  public static final int STEP_LEVEL_MODIFIED_MODE_SHIFT = 56;
  public static final long STEP_LEVEL_TIME_MASK = 1099511627775L;
  public static final int SYNC = 13;
  private static final String SYNC_DATA = "sy";
  private static final String TAG = "BatteryStats";
  private static final String UID_DATA = "uid";
  @VisibleForTesting
  public static final String UID_TIMES_TYPE_ALL = "A";
  private static final String USER_ACTIVITY_DATA = "ua";
  private static final String VERSION_DATA = "vers";
  private static final String VIBRATOR_DATA = "vib";
  public static final int VIBRATOR_ON = 9;
  private static final String VIDEO_DATA = "vid";
  public static final int VIDEO_TURNED_ON = 8;
  private static final String WAKELOCK_DATA = "wl";
  private static final String WAKEUP_ALARM_DATA = "wua";
  private static final String WAKEUP_REASON_DATA = "wr";
  public static final int WAKE_TYPE_DRAW = 18;
  public static final int WAKE_TYPE_FULL = 1;
  public static final int WAKE_TYPE_PARTIAL = 0;
  public static final int WAKE_TYPE_WINDOW = 2;
  public static final int WIFI_AGGREGATE_MULTICAST_ENABLED = 23;
  public static final int WIFI_BATCHED_SCAN = 11;
  private static final String WIFI_CONTROLLER_DATA = "wfcd";
  private static final String WIFI_CONTROLLER_NAME = "WiFi";
  private static final String WIFI_DATA = "wfl";
  private static final String WIFI_MULTICAST_DATA = "wmc";
  public static final int WIFI_MULTICAST_ENABLED = 7;
  private static final String WIFI_MULTICAST_TOTAL_DATA = "wmct";
  public static final int WIFI_RUNNING = 4;
  public static final int WIFI_SCAN = 6;
  private static final String WIFI_SIGNAL_STRENGTH_COUNT_DATA = "wsgc";
  private static final String WIFI_SIGNAL_STRENGTH_TIME_DATA = "wsgt";
  private static final String WIFI_STATE_COUNT_DATA = "wsc";
  static final String[] WIFI_STATE_NAMES;
  public static final int WIFI_STATE_OFF = 0;
  public static final int WIFI_STATE_OFF_SCANNING = 1;
  public static final int WIFI_STATE_ON_CONNECTED_P2P = 5;
  public static final int WIFI_STATE_ON_CONNECTED_STA = 4;
  public static final int WIFI_STATE_ON_CONNECTED_STA_P2P = 6;
  public static final int WIFI_STATE_ON_DISCONNECTED = 3;
  public static final int WIFI_STATE_ON_NO_NETWORKS = 2;
  public static final int WIFI_STATE_SOFT_AP = 7;
  private static final String WIFI_STATE_TIME_DATA = "wst";
  public static final int WIFI_SUPPL_STATE_ASSOCIATED = 7;
  public static final int WIFI_SUPPL_STATE_ASSOCIATING = 6;
  public static final int WIFI_SUPPL_STATE_AUTHENTICATING = 5;
  public static final int WIFI_SUPPL_STATE_COMPLETED = 10;
  private static final String WIFI_SUPPL_STATE_COUNT_DATA = "wssc";
  public static final int WIFI_SUPPL_STATE_DISCONNECTED = 1;
  public static final int WIFI_SUPPL_STATE_DORMANT = 11;
  public static final int WIFI_SUPPL_STATE_FOUR_WAY_HANDSHAKE = 8;
  public static final int WIFI_SUPPL_STATE_GROUP_HANDSHAKE = 9;
  public static final int WIFI_SUPPL_STATE_INACTIVE = 3;
  public static final int WIFI_SUPPL_STATE_INTERFACE_DISABLED = 2;
  public static final int WIFI_SUPPL_STATE_INVALID = 0;
  static final String[] WIFI_SUPPL_STATE_NAMES;
  public static final int WIFI_SUPPL_STATE_SCANNING = 4;
  static final String[] WIFI_SUPPL_STATE_SHORT_NAMES;
  private static final String WIFI_SUPPL_STATE_TIME_DATA = "wsst";
  public static final int WIFI_SUPPL_STATE_UNINITIALIZED = 12;
  private static final IntToString sIntToString;
  private static final IntToString sUidToString;
  private final StringBuilder mFormatBuilder = new StringBuilder(32);
  private final Formatter mFormatter = new Formatter(mFormatBuilder);
  
  static
  {
    JOB_FRESHNESS_BUCKETS = new long[] { 3600000L, 7200000L, 14400000L, 28800000L, Long.MAX_VALUE };
    SCREEN_BRIGHTNESS_NAMES = new String[] { "dark", "dim", "medium", "light", "bright" };
    SCREEN_BRIGHTNESS_SHORT_NAMES = new String[] { "0", "1", "2", "3", "4" };
    DATA_CONNECTION_NAMES = new String[] { "none", "gprs", "edge", "umts", "cdma", "evdo_0", "evdo_A", "1xrtt", "hsdpa", "hsupa", "hspa", "iden", "evdo_b", "lte", "ehrpd", "hspap", "gsm", "td_scdma", "iwlan", "lte_ca", "other" };
    WIFI_SUPPL_STATE_NAMES = new String[] { "invalid", "disconn", "disabled", "inactive", "scanning", "authenticating", "associating", "associated", "4-way-handshake", "group-handshake", "completed", "dormant", "uninit" };
    WIFI_SUPPL_STATE_SHORT_NAMES = new String[] { "inv", "dsc", "dis", "inact", "scan", "auth", "ascing", "asced", "4-way", "group", "compl", "dorm", "uninit" };
    HISTORY_STATE_DESCRIPTIONS = new BitDescription[] { new BitDescription(Integer.MIN_VALUE, "running", "r"), new BitDescription(1073741824, "wake_lock", "w"), new BitDescription(8388608, "sensor", "s"), new BitDescription(536870912, "gps", "g"), new BitDescription(268435456, "wifi_full_lock", "Wl"), new BitDescription(134217728, "wifi_scan", "Ws"), new BitDescription(65536, "wifi_multicast", "Wm"), new BitDescription(67108864, "wifi_radio", "Wr"), new BitDescription(33554432, "mobile_radio", "Pr"), new BitDescription(2097152, "phone_scanning", "Psc"), new BitDescription(4194304, "audio", "a"), new BitDescription(1048576, "screen", "S"), new BitDescription(524288, "plugged", "BP"), new BitDescription(262144, "screen_doze", "Sd"), new BitDescription(15872, 9, "data_conn", "Pcn", DATA_CONNECTION_NAMES, DATA_CONNECTION_NAMES), new BitDescription(448, 6, "phone_state", "Pst", new String[] { "in", "out", "emergency", "off" }, new String[] { "in", "out", "em", "off" }), new BitDescription(56, 3, "phone_signal_strength", "Pss", SignalStrength.SIGNAL_STRENGTH_NAMES, new String[] { "0", "1", "2", "3", "4", "5" }), new BitDescription(7, 0, "brightness", "Sb", SCREEN_BRIGHTNESS_NAMES, SCREEN_BRIGHTNESS_SHORT_NAMES) };
    HISTORY_STATE2_DESCRIPTIONS = new BitDescription[] { new BitDescription(Integer.MIN_VALUE, "power_save", "ps"), new BitDescription(1073741824, "video", "v"), new BitDescription(536870912, "wifi_running", "Ww"), new BitDescription(268435456, "wifi", "W"), new BitDescription(134217728, "flashlight", "fl"), new BitDescription(100663296, 25, "device_idle", "di", new String[] { "off", "light", "full", "???" }, new String[] { "off", "light", "full", "???" }), new BitDescription(16777216, "charging", "ch"), new BitDescription(262144, "usb_data", "Ud"), new BitDescription(8388608, "phone_in_call", "Pcl"), new BitDescription(4194304, "bluetooth", "b"), new BitDescription(112, 4, "wifi_signal_strength", "Wss", new String[] { "0", "1", "2", "3", "4" }, new String[] { "0", "1", "2", "3", "4" }), new BitDescription(15, 0, "wifi_suppl", "Wsp", WIFI_SUPPL_STATE_NAMES, WIFI_SUPPL_STATE_SHORT_NAMES), new BitDescription(2097152, "camera", "ca"), new BitDescription(1048576, "ble_scan", "bles"), new BitDescription(524288, "cellular_high_tx_power", "Chtp"), new BitDescription(128, 7, "gps_signal_quality", "Gss", new String[] { "poor", "good" }, new String[] { "poor", "good" }), new BitDescription(131072, "soft_ap", "Sap") };
    HISTORY_EVENT_NAMES = new String[] { "null", "proc", "fg", "top", "sync", "wake_lock_in", "job", "user", "userfg", "conn", "active", "pkginst", "pkgunin", "alarm", "stats", "pkginactive", "pkgactive", "tmpwhitelist", "screenwake", "wakeupap", "longwake", "est_capacity" };
    HISTORY_EVENT_CHECKIN_NAMES = new String[] { "Enl", "Epr", "Efg", "Etp", "Esy", "Ewl", "Ejb", "Eur", "Euf", "Ecn", "Eac", "Epi", "Epu", "Eal", "Est", "Eai", "Eaa", "Etw", "Esw", "Ewa", "Elw", "Eec" };
    sUidToString = _..Lambda.IyvVQC_0mKtsfXbnO0kDL64hrk0.INSTANCE;
    sIntToString = _..Lambda.BatteryStats.q1UvBdLgHRZVzc68BxdksTmbuCw.INSTANCE;
    HISTORY_EVENT_INT_FORMATTERS = new IntToString[] { sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sUidToString, sIntToString };
    WIFI_STATE_NAMES = new String[] { "off", "scanning", "no_net", "disconn", "sta", "p2p", "sta_p2p", "soft_ap" };
    STEP_LEVEL_MODES_OF_INTEREST = new int[] { 7, 15, 11, 7, 7, 7, 7, 7, 15, 11 };
    STEP_LEVEL_MODE_VALUES = new int[] { 0, 4, 8, 1, 5, 2, 6, 3, 7, 11 };
  }
  
  public BatteryStats() {}
  
  private static long computeWakeLock(Timer paramTimer, long paramLong, int paramInt)
  {
    if (paramTimer != null) {
      return (500L + paramTimer.getTotalTimeLocked(paramLong, paramInt)) / 1000L;
    }
    return 0L;
  }
  
  private static boolean controllerActivityHasData(ControllerActivityCounter paramControllerActivityCounter, int paramInt)
  {
    if (paramControllerActivityCounter == null) {
      return false;
    }
    if ((paramControllerActivityCounter.getIdleTimeCounter().getCountLocked(paramInt) == 0L) && (paramControllerActivityCounter.getRxTimeCounter().getCountLocked(paramInt) == 0L) && (paramControllerActivityCounter.getPowerCounter().getCountLocked(paramInt) == 0L))
    {
      paramControllerActivityCounter = paramControllerActivityCounter.getTxTimeCounters();
      int i = paramControllerActivityCounter.length;
      for (int j = 0; j < i; j++) {
        if (paramControllerActivityCounter[j].getCountLocked(paramInt) != 0L) {
          return true;
        }
      }
      return false;
    }
    return true;
  }
  
  private static final void dumpControllerActivityLine(PrintWriter paramPrintWriter, int paramInt1, String paramString1, String paramString2, ControllerActivityCounter paramControllerActivityCounter, int paramInt2)
  {
    if (!controllerActivityHasData(paramControllerActivityCounter, paramInt2)) {
      return;
    }
    dumpLineHeader(paramPrintWriter, paramInt1, paramString1, paramString2);
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramControllerActivityCounter.getIdleTimeCounter().getCountLocked(paramInt2));
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramControllerActivityCounter.getRxTimeCounter().getCountLocked(paramInt2));
    paramPrintWriter.print(",");
    paramPrintWriter.print(paramControllerActivityCounter.getPowerCounter().getCountLocked(paramInt2) / 3600000L);
    for (paramString2 : paramControllerActivityCounter.getTxTimeCounters())
    {
      paramPrintWriter.print(",");
      paramPrintWriter.print(paramString2.getCountLocked(paramInt2));
    }
    paramPrintWriter.println();
  }
  
  private static void dumpControllerActivityProto(ProtoOutputStream paramProtoOutputStream, long paramLong, ControllerActivityCounter paramControllerActivityCounter, int paramInt)
  {
    if (!controllerActivityHasData(paramControllerActivityCounter, paramInt)) {
      return;
    }
    long l = paramProtoOutputStream.start(paramLong);
    paramProtoOutputStream.write(1112396529665L, paramControllerActivityCounter.getIdleTimeCounter().getCountLocked(paramInt));
    paramProtoOutputStream.write(1112396529666L, paramControllerActivityCounter.getRxTimeCounter().getCountLocked(paramInt));
    paramProtoOutputStream.write(1112396529667L, paramControllerActivityCounter.getPowerCounter().getCountLocked(paramInt) / 3600000L);
    LongCounter[] arrayOfLongCounter = paramControllerActivityCounter.getTxTimeCounters();
    for (int i = 0; i < arrayOfLongCounter.length; i++)
    {
      paramControllerActivityCounter = arrayOfLongCounter[i];
      paramLong = paramProtoOutputStream.start(2246267895812L);
      paramProtoOutputStream.write(1120986464257L, i);
      paramProtoOutputStream.write(1112396529666L, paramControllerActivityCounter.getCountLocked(paramInt));
      paramProtoOutputStream.end(paramLong);
    }
    paramProtoOutputStream.end(l);
  }
  
  private void dumpDailyLevelStepSummary(PrintWriter paramPrintWriter, String paramString1, String paramString2, LevelStepTracker paramLevelStepTracker, StringBuilder paramStringBuilder, int[] paramArrayOfInt)
  {
    if (paramLevelStepTracker == null) {
      return;
    }
    long l = paramLevelStepTracker.computeTimeEstimate(0L, 0L, paramArrayOfInt);
    if (l >= 0L)
    {
      paramPrintWriter.print(paramString1);
      paramPrintWriter.print(paramString2);
      paramPrintWriter.print(" total time: ");
      paramStringBuilder.setLength(0);
      formatTimeMs(paramStringBuilder, l);
      paramPrintWriter.print(paramStringBuilder);
      paramPrintWriter.print(" (from ");
      paramPrintWriter.print(paramArrayOfInt[0]);
      paramPrintWriter.println(" steps)");
    }
    for (int i = 0; i < STEP_LEVEL_MODES_OF_INTEREST.length; i++)
    {
      l = paramLevelStepTracker.computeTimeEstimate(STEP_LEVEL_MODES_OF_INTEREST[i], STEP_LEVEL_MODE_VALUES[i], paramArrayOfInt);
      if (l > 0L)
      {
        paramPrintWriter.print(paramString1);
        paramPrintWriter.print(paramString2);
        paramPrintWriter.print(" ");
        paramPrintWriter.print(STEP_LEVEL_MODE_LABELS[i]);
        paramPrintWriter.print(" time: ");
        paramStringBuilder.setLength(0);
        formatTimeMs(paramStringBuilder, l);
        paramPrintWriter.print(paramStringBuilder);
        paramPrintWriter.print(" (from ");
        paramPrintWriter.print(paramArrayOfInt[0]);
        paramPrintWriter.println(" steps)");
      }
    }
  }
  
  private void dumpDailyPackageChanges(PrintWriter paramPrintWriter, String paramString, ArrayList<PackageChange> paramArrayList)
  {
    if (paramArrayList == null) {
      return;
    }
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("Package changes:");
    for (int i = 0; i < paramArrayList.size(); i++)
    {
      PackageChange localPackageChange = (PackageChange)paramArrayList.get(i);
      if (mUpdate)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  Update ");
        paramPrintWriter.print(mPackageName);
        paramPrintWriter.print(" vers=");
        paramPrintWriter.println(mVersionCode);
      }
      else
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  Uninstall ");
        paramPrintWriter.println(mPackageName);
      }
    }
  }
  
  private static void dumpDurationSteps(ProtoOutputStream paramProtoOutputStream, long paramLong, LevelStepTracker paramLevelStepTracker)
  {
    if (paramLevelStepTracker == null) {
      return;
    }
    int i = mNumStepDurations;
    for (int j = 0; j < i; j++)
    {
      long l1 = paramProtoOutputStream.start(paramLong);
      paramProtoOutputStream.write(1112396529665L, paramLevelStepTracker.getDurationAt(j));
      paramProtoOutputStream.write(1120986464258L, paramLevelStepTracker.getLevelAt(j));
      long l2 = paramLevelStepTracker.getInitModeAt(j);
      long l3 = paramLevelStepTracker.getModModeAt(j);
      int k = 0;
      int m = 1;
      if ((l3 & 0x3) == 0L) {
        switch ((int)(0x3 & l2) + 1)
        {
        default: 
          k = 5;
          break;
        case 4: 
          k = 4;
          break;
        case 3: 
          k = 3;
          break;
        case 2: 
          k = 1;
          break;
        case 1: 
          k = 2;
        }
      }
      paramProtoOutputStream.write(1159641169923L, k);
      k = 0;
      int n = 2;
      if ((l3 & 0x4) == 0L) {
        if ((0x4 & l2) != 0L) {
          k = m;
        } else {
          k = 2;
        }
      }
      paramProtoOutputStream.write(1159641169924L, k);
      k = 0;
      if ((l3 & 0x8) == 0L) {
        if ((0x8 & l2) != 0L) {
          k = n;
        } else {
          k = 3;
        }
      }
      paramProtoOutputStream.write(1159641169925L, k);
      paramProtoOutputStream.end(l1);
    }
  }
  
  private static boolean dumpDurationSteps(PrintWriter paramPrintWriter, String paramString1, String paramString2, LevelStepTracker paramLevelStepTracker, boolean paramBoolean)
  {
    Object localObject = paramLevelStepTracker;
    if (localObject == null) {
      return false;
    }
    int i = mNumStepDurations;
    if (i <= 0) {
      return false;
    }
    if (!paramBoolean) {
      paramPrintWriter.println(paramString2);
    }
    String[] arrayOfString = new String[5];
    for (int j = 0;; j++)
    {
      localObject = paramLevelStepTracker;
      if (j >= i) {
        break;
      }
      long l1 = ((LevelStepTracker)localObject).getDurationAt(j);
      int k = ((LevelStepTracker)localObject).getLevelAt(j);
      long l2 = ((LevelStepTracker)localObject).getInitModeAt(j);
      long l3 = ((LevelStepTracker)localObject).getModModeAt(j);
      if (paramBoolean)
      {
        arrayOfString[0] = Long.toString(l1);
        arrayOfString[1] = Integer.toString(k);
        if ((l3 & 0x3) == 0L) {
          switch ((int)(l2 & 0x3) + 1)
          {
          default: 
            arrayOfString[2] = "?";
            break;
          case 4: 
            arrayOfString[2] = "sds";
            break;
          case 3: 
            arrayOfString[2] = "sd";
            break;
          case 2: 
            arrayOfString[2] = "s+";
            break;
          case 1: 
            arrayOfString[2] = "s-";
            break;
          }
        } else {
          arrayOfString[2] = "";
        }
        if ((l3 & 0x4) == 0L)
        {
          if ((l2 & 0x4) != 0L) {
            localObject = "p+";
          } else {
            localObject = "p-";
          }
          arrayOfString[3] = localObject;
        }
        else
        {
          arrayOfString[3] = "";
        }
        if ((l3 & 0x8) == 0L)
        {
          if ((0x8 & l2) != 0L) {
            localObject = "i+";
          } else {
            localObject = "i-";
          }
          arrayOfString[4] = localObject;
        }
        else
        {
          arrayOfString[4] = "";
        }
        dumpLine(paramPrintWriter, 0, "i", paramString2, (Object[])arrayOfString);
      }
      else
      {
        paramPrintWriter.print(paramString1);
        paramPrintWriter.print("#");
        paramPrintWriter.print(j);
        paramPrintWriter.print(": ");
        TimeUtils.formatDuration(l1, paramPrintWriter);
        paramPrintWriter.print(" to ");
        paramPrintWriter.print(k);
        int m = 0;
        if ((l3 & 0x3) == 0L)
        {
          paramPrintWriter.print(" (");
          switch ((int)(l2 & 0x3) + 1)
          {
          default: 
            paramPrintWriter.print("screen-?");
            break;
          case 4: 
            paramPrintWriter.print("screen-doze-suspend");
            break;
          case 3: 
            paramPrintWriter.print("screen-doze");
            break;
          case 2: 
            paramPrintWriter.print("screen-on");
            break;
          case 1: 
            paramPrintWriter.print("screen-off");
          }
          m = 1;
        }
        k = m;
        if ((l3 & 0x4) == 0L)
        {
          if (m != 0) {
            localObject = ", ";
          } else {
            localObject = " (";
          }
          paramPrintWriter.print((String)localObject);
          if ((l2 & 0x4) != 0L) {
            localObject = "power-save-on";
          } else {
            localObject = "power-save-off";
          }
          paramPrintWriter.print((String)localObject);
          k = 1;
        }
        m = k;
        if ((l3 & 0x8) == 0L)
        {
          if (k != 0) {
            localObject = ", ";
          } else {
            localObject = " (";
          }
          paramPrintWriter.print((String)localObject);
          if ((l2 & 0x8) != 0L) {
            localObject = "device-idle-on";
          } else {
            localObject = "device-idle-off";
          }
          paramPrintWriter.print((String)localObject);
          m = 1;
        }
        if (m != 0) {
          paramPrintWriter.print(")");
        }
        paramPrintWriter.println();
      }
    }
    return true;
  }
  
  private void dumpHistoryLocked(PrintWriter paramPrintWriter, int paramInt, long paramLong, boolean paramBoolean)
  {
    HistoryPrinter localHistoryPrinter = new HistoryPrinter();
    HistoryItem localHistoryItem = new HistoryItem();
    int i = 0;
    long l1 = -1L;
    long l2 = -1L;
    Object localObject1 = null;
    while (getNextHistoryLocked(localHistoryItem))
    {
      l2 = time;
      long l3 = l1;
      if (l1 < 0L) {
        l3 = l2;
      }
      if (time >= paramLong)
      {
        boolean bool;
        if ((paramLong >= 0L) && (i == 0))
        {
          if ((cmd != 5) && (cmd != 7) && (cmd != 4) && (cmd != 8))
          {
            if (currentTime != 0L)
            {
              i = cmd;
              cmd = ((byte)5);
              if ((paramInt & 0x20) != 0) {
                bool = true;
              } else {
                bool = false;
              }
              localHistoryPrinter.printNextItem(paramPrintWriter, localHistoryItem, l3, paramBoolean, bool);
              cmd = ((byte)i);
              i = 1;
            }
          }
          else
          {
            if ((paramInt & 0x20) != 0) {
              bool = true;
            } else {
              bool = false;
            }
            i = 1;
            localHistoryPrinter.printNextItem(paramPrintWriter, localHistoryItem, l3, paramBoolean, bool);
            cmd = ((byte)0);
          }
          if (localObject1 != null)
          {
            if (cmd != 0)
            {
              if ((paramInt & 0x20) != 0) {
                bool = true;
              } else {
                bool = false;
              }
              localHistoryPrinter.printNextItem(paramPrintWriter, localHistoryItem, l3, paramBoolean, bool);
              cmd = ((byte)0);
            }
            int j = eventCode;
            Object localObject2 = eventTag;
            eventTag = new HistoryTag();
            int k = 0;
            Object localObject3 = localObject1;
            for (localObject1 = localObject2; k < 22; localObject1 = localObject2)
            {
              Object localObject4 = localObject3.getStateForEvent(k);
              if (localObject4 == null)
              {
                localObject2 = localObject3;
                localObject3 = localObject1;
                localObject1 = localObject2;
              }
              else
              {
                localObject2 = ((HashMap)localObject4).entrySet().iterator();
                while (((Iterator)localObject2).hasNext())
                {
                  Map.Entry localEntry = (Map.Entry)((Iterator)localObject2).next();
                  SparseIntArray localSparseIntArray = (SparseIntArray)localEntry.getValue();
                  int m = 0;
                  Object localObject5 = localObject1;
                  localObject1 = localObject4;
                  localObject4 = localEntry;
                  while (m < localSparseIntArray.size())
                  {
                    eventCode = k;
                    eventTag.string = ((String)((Map.Entry)localObject4).getKey());
                    eventTag.uid = localSparseIntArray.keyAt(m);
                    eventTag.poolIdx = localSparseIntArray.valueAt(m);
                    if ((paramInt & 0x20) != 0) {
                      bool = true;
                    } else {
                      bool = false;
                    }
                    localHistoryPrinter.printNextItem(paramPrintWriter, localHistoryItem, l3, paramBoolean, bool);
                    wakeReasonTag = null;
                    wakelockTag = null;
                    m++;
                  }
                  localObject4 = localObject1;
                  localObject1 = localObject5;
                }
                localObject2 = localObject1;
                localObject1 = localObject3;
                localObject3 = localObject2;
              }
              k++;
              localObject2 = localObject3;
              localObject3 = localObject1;
            }
            eventCode = j;
            eventTag = ((HistoryTag)localObject1);
            localObject1 = null;
          }
          else {}
        }
        if ((paramInt & 0x20) != 0) {
          bool = true;
        } else {
          bool = false;
        }
        l1 = l3;
        localHistoryPrinter.printNextItem(paramPrintWriter, localHistoryItem, l1, paramBoolean, bool);
      }
      else
      {
        l1 = l3;
      }
    }
    if (paramLong >= 0L)
    {
      commitCurrentHistoryBatchLocked();
      if (paramBoolean) {
        localObject1 = "NEXT: ";
      } else {
        localObject1 = "  NEXT: ";
      }
      paramPrintWriter.print((String)localObject1);
      paramPrintWriter.println(1L + l2);
    }
  }
  
  private static final void dumpLine(PrintWriter paramPrintWriter, int paramInt, String paramString1, String paramString2, Object... paramVarArgs)
  {
    dumpLineHeader(paramPrintWriter, paramInt, paramString1, paramString2);
    int i = paramVarArgs.length;
    for (paramInt = 0; paramInt < i; paramInt++)
    {
      paramString1 = paramVarArgs[paramInt];
      paramPrintWriter.print(',');
      paramPrintWriter.print(paramString1);
    }
    paramPrintWriter.println();
  }
  
  private static final void dumpLineHeader(PrintWriter paramPrintWriter, int paramInt, String paramString1, String paramString2)
  {
    paramPrintWriter.print(9);
    paramPrintWriter.print(',');
    paramPrintWriter.print(paramInt);
    paramPrintWriter.print(',');
    paramPrintWriter.print(paramString1);
    paramPrintWriter.print(',');
    paramPrintWriter.print(paramString2);
  }
  
  private void dumpProtoAppsLocked(ProtoOutputStream paramProtoOutputStream, BatteryStatsHelper paramBatteryStatsHelper, List<ApplicationInfo> paramList)
  {
    int i = 0;
    int j = 0;
    long l1 = SystemClock.uptimeMillis() * 1000L;
    long l2 = SystemClock.elapsedRealtime();
    long l3 = l2 * 1000L;
    long l4 = getBatteryUptime(l1);
    Object localObject1 = new SparseArray();
    Object localObject2;
    int m;
    if (paramList != null) {
      for (k = 0; k < paramList.size(); k++)
      {
        localObject2 = (ApplicationInfo)paramList.get(k);
        m = UserHandle.getAppId(uid);
        localObject3 = (ArrayList)((SparseArray)localObject1).get(m);
        if (localObject3 == null)
        {
          localObject3 = new ArrayList();
          ((SparseArray)localObject1).put(m, localObject3);
        }
        ((ArrayList)localObject3).add(packageName);
      }
    }
    Object localObject3 = new SparseArray();
    paramBatteryStatsHelper = paramBatteryStatsHelper.getUsageList();
    paramList = paramBatteryStatsHelper;
    if (paramBatteryStatsHelper != null) {
      for (k = 0;; k++)
      {
        paramList = paramBatteryStatsHelper;
        i = j;
        if (k >= paramBatteryStatsHelper.size()) {
          break;
        }
        paramList = (BatterySipper)paramBatteryStatsHelper.get(k);
        if (drainType == BatterySipper.DrainType.APP) {
          ((SparseArray)localObject3).put(uidObj.getUid(), paramList);
        }
      }
    }
    Object localObject4 = getUidStats();
    int k = ((SparseArray)localObject4).size();
    int n = 0;
    List<ApplicationInfo> localList = paramList;
    paramBatteryStatsHelper = (BatteryStatsHelper)localObject3;
    paramList = (List<ApplicationInfo>)localObject1;
    while (n < k)
    {
      long l5 = paramProtoOutputStream.start(2246267895813L);
      localObject3 = (Uid)((SparseArray)localObject4).valueAt(n);
      int i1 = ((SparseArray)localObject4).keyAt(n);
      paramProtoOutputStream.write(1120986464257L, i1);
      m = UserHandle.getAppId(i1);
      localObject1 = (ArrayList)paramList.get(m);
      if (localObject1 == null) {
        localObject1 = new ArrayList();
      }
      localObject2 = ((Uid)localObject3).getPackageStats();
      m = ((ArrayMap)localObject2).size() - 1;
      int i3;
      int i4;
      while (m >= 0)
      {
        localObject5 = (String)((ArrayMap)localObject2).keyAt(m);
        localObject6 = ((BatteryStats.Uid.Pkg)((ArrayMap)localObject2).valueAt(m)).getServiceStats();
        if (((ArrayMap)localObject6).size() == 0)
        {
          l6 = l4;
          localObject5 = localObject1;
          l4 = l2;
          localObject1 = paramList;
          l2 = l6;
          paramList = paramBatteryStatsHelper;
          paramBatteryStatsHelper = (BatteryStatsHelper)localObject5;
        }
        else
        {
          l7 = paramProtoOutputStream.start(2246267895810L);
          paramProtoOutputStream.write(1138166333441L, (String)localObject5);
          ((ArrayList)localObject1).remove(localObject5);
          for (i2 = ((ArrayMap)localObject6).size() - 1; i2 >= 0; i2--)
          {
            localObject7 = (BatteryStats.Uid.Pkg.Serv)((ArrayMap)localObject6).valueAt(i2);
            l6 = roundUsToMs(((BatteryStats.Uid.Pkg.Serv)localObject7).getStartTime(l4, 0));
            i3 = ((BatteryStats.Uid.Pkg.Serv)localObject7).getStarts(0);
            i4 = ((BatteryStats.Uid.Pkg.Serv)localObject7).getLaunches(0);
            if ((l6 != 0L) || (i3 != 0) || (i4 != 0))
            {
              l8 = paramProtoOutputStream.start(2246267895810L);
              paramProtoOutputStream.write(1138166333441L, (String)((ArrayMap)localObject6).keyAt(i2));
              paramProtoOutputStream.write(1112396529666L, l6);
              paramProtoOutputStream.write(1120986464259L, i3);
              paramProtoOutputStream.write(1120986464260L, i4);
              paramProtoOutputStream.end(l8);
            }
          }
          localObject5 = paramList;
          paramList = paramBatteryStatsHelper;
          paramBatteryStatsHelper = (BatteryStatsHelper)localObject1;
          l6 = l2;
          paramProtoOutputStream.end(l7);
          l2 = l4;
          localObject1 = localObject5;
          l4 = l6;
        }
        m--;
        localObject6 = localObject4;
        l6 = l3;
        l3 = l4;
        localObject5 = localObject1;
        localObject4 = paramList;
        localObject1 = paramBatteryStatsHelper;
        paramList = (List<ApplicationInfo>)localObject5;
        l4 = l2;
        paramBatteryStatsHelper = (BatteryStatsHelper)localObject4;
        localObject4 = localObject6;
        l2 = l3;
        l3 = l6;
      }
      long l7 = l3;
      localObject1 = ((ArrayList)localObject1).iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject5 = (String)((Iterator)localObject1).next();
        l3 = paramProtoOutputStream.start(2246267895810L);
        paramProtoOutputStream.write(1138166333441L, (String)localObject5);
        paramProtoOutputStream.end(l3);
      }
      if (((Uid)localObject3).getAggregatedPartialWakelockTimer() != null)
      {
        localObject1 = ((Uid)localObject3).getAggregatedPartialWakelockTimer();
        l3 = l2;
        l6 = ((Timer)localObject1).getTotalDurationMsLocked(l3);
        localObject1 = ((Timer)localObject1).getSubTimer();
        if (localObject1 != null) {
          l3 = ((Timer)localObject1).getTotalDurationMsLocked(l3);
        } else {
          l3 = 0L;
        }
        l8 = paramProtoOutputStream.start(1146756268056L);
        paramProtoOutputStream.write(1112396529665L, l6);
        paramProtoOutputStream.write(1112396529666L, l3);
        paramProtoOutputStream.end(l8);
      }
      long l6 = l2;
      localObject1 = ((Uid)localObject3).getAudioTurnedOnTimer();
      l2 = l5;
      m = k;
      dumpTimer(paramProtoOutputStream, 1146756268040L, (Timer)localObject1, l7, 0);
      dumpControllerActivityProto(paramProtoOutputStream, 1146756268035L, ((Uid)localObject3).getBluetoothControllerActivity(), 0);
      localObject1 = ((Uid)localObject3).getBluetoothScanTimer();
      if (localObject1 != null)
      {
        l3 = paramProtoOutputStream.start(1146756268038L);
        dumpTimer(paramProtoOutputStream, 1146756268033L, (Timer)localObject1, l7, 0);
        dumpTimer(paramProtoOutputStream, 1146756268034L, ((Uid)localObject3).getBluetoothScanBackgroundTimer(), l7, 0);
        dumpTimer(paramProtoOutputStream, 1146756268035L, ((Uid)localObject3).getBluetoothUnoptimizedScanTimer(), l7, 0);
        dumpTimer(paramProtoOutputStream, 1146756268036L, ((Uid)localObject3).getBluetoothUnoptimizedScanBackgroundTimer(), l7, 0);
        if (((Uid)localObject3).getBluetoothScanResultCounter() != null) {
          k = ((Uid)localObject3).getBluetoothScanResultCounter().getCountLocked(0);
        } else {
          k = 0;
        }
        paramProtoOutputStream.write(1120986464261L, k);
        if (((Uid)localObject3).getBluetoothScanResultBgCounter() != null) {
          k = ((Uid)localObject3).getBluetoothScanResultBgCounter().getCountLocked(0);
        } else {
          k = 0;
        }
        paramProtoOutputStream.write(1120986464262L, k);
        paramProtoOutputStream.end(l3);
      }
      dumpTimer(paramProtoOutputStream, 1146756268041L, ((Uid)localObject3).getCameraTurnedOnTimer(), l7, 0);
      long l8 = paramProtoOutputStream.start(1146756268039L);
      paramProtoOutputStream.write(1112396529665L, roundUsToMs(((Uid)localObject3).getUserCpuTimeUs(0)));
      paramProtoOutputStream.write(1112396529666L, roundUsToMs(((Uid)localObject3).getSystemCpuTimeUs(0)));
      Object localObject6 = getCpuFreqs();
      Object localObject5 = localObject2;
      k = m;
      l3 = l6;
      l5 = l2;
      if (localObject6 != null)
      {
        localObject7 = ((Uid)localObject3).getCpuFreqTimes(0);
        localObject5 = localObject2;
        k = m;
        l3 = l6;
        l5 = l2;
        if (localObject7 != null)
        {
          localObject5 = localObject2;
          k = m;
          l3 = l6;
          l5 = l2;
          if (localObject7.length == localObject6.length)
          {
            localObject5 = ((Uid)localObject3).getScreenOffCpuFreqTimes(0);
            localObject1 = localObject5;
            if (localObject5 == null) {
              localObject1 = new long[localObject7.length];
            }
            for (i2 = 0;; i2++)
            {
              localObject5 = localObject2;
              k = m;
              l3 = l6;
              l5 = l2;
              if (i2 >= localObject7.length) {
                break;
              }
              l3 = paramProtoOutputStream.start(2246267895811L);
              paramProtoOutputStream.write(1120986464257L, i2 + 1);
              paramProtoOutputStream.write(1112396529666L, localObject7[i2]);
              paramProtoOutputStream.write(1112396529667L, localObject1[i2]);
              paramProtoOutputStream.end(l3);
            }
          }
        }
      }
      m = 0;
      l2 = l8;
      while (m < 7)
      {
        localObject7 = ((Uid)localObject3).getCpuFreqTimes(0, m);
        if ((localObject7 != null) && (localObject7.length == localObject6.length))
        {
          localObject2 = ((Uid)localObject3).getScreenOffCpuFreqTimes(0, m);
          localObject1 = localObject2;
          if (localObject2 == null) {
            localObject1 = new long[localObject7.length];
          }
          l6 = paramProtoOutputStream.start(2246267895812L);
          paramProtoOutputStream.write(1159641169921L, m);
          for (i2 = 0; i2 < localObject7.length; i2++)
          {
            l8 = paramProtoOutputStream.start(2246267895810L);
            paramProtoOutputStream.write(1120986464257L, i2 + 1);
            paramProtoOutputStream.write(1112396529666L, localObject7[i2]);
            paramProtoOutputStream.write(1112396529667L, localObject1[i2]);
            paramProtoOutputStream.end(l8);
          }
          paramProtoOutputStream.end(l6);
        }
        m++;
      }
      paramProtoOutputStream.end(l2);
      localObject1 = ((Uid)localObject3).getFlashlightTurnedOnTimer();
      localObject2 = localObject6;
      dumpTimer(paramProtoOutputStream, 1146756268042L, (Timer)localObject1, l7, 0);
      dumpTimer(paramProtoOutputStream, 1146756268043L, ((Uid)localObject3).getForegroundActivityTimer(), l7, 0);
      dumpTimer(paramProtoOutputStream, 1146756268044L, ((Uid)localObject3).getForegroundServiceTimer(), l7, 0);
      localObject1 = ((Uid)localObject3).getJobCompletionStats();
      localObject6 = new int[5];
      Object tmp1488_1486 = localObject6;
      tmp1488_1486[0] = 0;
      Object tmp1492_1488 = tmp1488_1486;
      tmp1492_1488[1] = 1;
      Object tmp1496_1492 = tmp1492_1488;
      tmp1496_1492[2] = 2;
      Object tmp1500_1496 = tmp1496_1492;
      tmp1500_1496[3] = 3;
      Object tmp1504_1500 = tmp1500_1496;
      tmp1504_1500[4] = 4;
      tmp1504_1500;
      for (m = 0; m < ((ArrayMap)localObject1).size(); m++)
      {
        tmp1496_1492 = (SparseIntArray)((ArrayMap)localObject1).valueAt(m);
        if (tmp1496_1492 != null)
        {
          l6 = paramProtoOutputStream.start(2246267895824L);
          paramProtoOutputStream.write(1138166333441L, (String)((ArrayMap)localObject1).keyAt(m));
          tmp1504_1500 = localObject6.length;
          for (tmp1492_1488 = 0; tmp1492_1488 < tmp1504_1500; tmp1492_1488++)
          {
            tmp1500_1496 = localObject6[tmp1492_1488];
            l2 = paramProtoOutputStream.start(2246267895810L);
            paramProtoOutputStream.write(1159641169921L, tmp1500_1496);
            paramProtoOutputStream.write(1120986464258L, ((SparseIntArray)tmp1496_1492).get(tmp1500_1496, 0));
            paramProtoOutputStream.end(l2);
          }
          paramProtoOutputStream.end(l6);
        }
      }
      localObject6 = ((Uid)localObject3).getJobStats();
      for (m = ((ArrayMap)localObject6).size() - 1; m >= 0; m--)
      {
        localObject1 = (Timer)((ArrayMap)localObject6).valueAt(m);
        localObject2 = ((Timer)localObject1).getSubTimer();
        l2 = paramProtoOutputStream.start(2246267895823L);
        paramProtoOutputStream.write(1138166333441L, (String)((ArrayMap)localObject6).keyAt(m));
        dumpTimer(paramProtoOutputStream, 1146756268034L, (Timer)localObject1, l7, 0);
        dumpTimer(paramProtoOutputStream, 1146756268035L, (Timer)localObject2, l7, 0);
        paramProtoOutputStream.end(l2);
      }
      dumpControllerActivityProto(paramProtoOutputStream, 1146756268036L, ((Uid)localObject3).getModemControllerActivity(), 0);
      l6 = paramProtoOutputStream.start(1146756268049L);
      paramProtoOutputStream.write(1112396529665L, ((Uid)localObject3).getNetworkActivityBytes(0, 0));
      paramProtoOutputStream.write(1112396529666L, ((Uid)localObject3).getNetworkActivityBytes(1, 0));
      paramProtoOutputStream.write(1112396529667L, ((Uid)localObject3).getNetworkActivityBytes(2, 0));
      paramProtoOutputStream.write(1112396529668L, ((Uid)localObject3).getNetworkActivityBytes(3, 0));
      paramProtoOutputStream.write(1112396529669L, ((Uid)localObject3).getNetworkActivityBytes(4, 0));
      paramProtoOutputStream.write(1112396529670L, ((Uid)localObject3).getNetworkActivityBytes(5, 0));
      paramProtoOutputStream.write(1112396529671L, ((Uid)localObject3).getNetworkActivityPackets(0, 0));
      paramProtoOutputStream.write(1112396529672L, ((Uid)localObject3).getNetworkActivityPackets(1, 0));
      paramProtoOutputStream.write(1112396529673L, ((Uid)localObject3).getNetworkActivityPackets(2, 0));
      paramProtoOutputStream.write(1112396529674L, ((Uid)localObject3).getNetworkActivityPackets(3, 0));
      paramProtoOutputStream.write(1112396529675L, roundUsToMs(((Uid)localObject3).getMobileRadioActiveTime(0)));
      paramProtoOutputStream.write(1120986464268L, ((Uid)localObject3).getMobileRadioActiveCount(0));
      paramProtoOutputStream.write(1120986464269L, ((Uid)localObject3).getMobileRadioApWakeupCount(0));
      paramProtoOutputStream.write(1120986464270L, ((Uid)localObject3).getWifiRadioApWakeupCount(0));
      paramProtoOutputStream.write(1112396529679L, ((Uid)localObject3).getNetworkActivityBytes(6, 0));
      paramProtoOutputStream.write(1112396529680L, ((Uid)localObject3).getNetworkActivityBytes(7, 0));
      paramProtoOutputStream.write(1112396529681L, ((Uid)localObject3).getNetworkActivityBytes(8, 0));
      paramProtoOutputStream.write(1112396529682L, ((Uid)localObject3).getNetworkActivityBytes(9, 0));
      paramProtoOutputStream.write(1112396529683L, ((Uid)localObject3).getNetworkActivityPackets(6, 0));
      paramProtoOutputStream.write(1112396529684L, ((Uid)localObject3).getNetworkActivityPackets(7, 0));
      paramProtoOutputStream.write(1112396529685L, ((Uid)localObject3).getNetworkActivityPackets(8, 0));
      paramProtoOutputStream.write(1112396529686L, ((Uid)localObject3).getNetworkActivityPackets(9, 0));
      paramProtoOutputStream.end(l6);
      m = i1;
      localObject2 = (BatterySipper)paramBatteryStatsHelper.get(m);
      if (localObject2 != null)
      {
        l2 = paramProtoOutputStream.start(1146756268050L);
        paramProtoOutputStream.write(1103806595073L, totalPowerMah);
        paramProtoOutputStream.write(1133871366146L, shouldHide);
        paramProtoOutputStream.write(1103806595075L, screenPowerMah);
        paramProtoOutputStream.write(1103806595076L, proportionalSmearMah);
        paramProtoOutputStream.end(l2);
      }
      localObject1 = ((Uid)localObject3).getProcessStats();
      for (Object tmp1492_1488 = ((ArrayMap)localObject1).size() - 1; tmp1492_1488 >= 0; tmp1492_1488--)
      {
        tmp1496_1492 = (BatteryStats.Uid.Proc)((ArrayMap)localObject1).valueAt(tmp1492_1488);
        l2 = paramProtoOutputStream.start(2246267895827L);
        paramProtoOutputStream.write(1138166333441L, (String)((ArrayMap)localObject1).keyAt(tmp1492_1488));
        paramProtoOutputStream.write(1112396529666L, ((BatteryStats.Uid.Proc)tmp1496_1492).getUserTime(0));
        paramProtoOutputStream.write(1112396529667L, ((BatteryStats.Uid.Proc)tmp1496_1492).getSystemTime(0));
        paramProtoOutputStream.write(1112396529668L, ((BatteryStats.Uid.Proc)tmp1496_1492).getForegroundTime(0));
        paramProtoOutputStream.write(1120986464261L, ((BatteryStats.Uid.Proc)tmp1496_1492).getStarts(0));
        paramProtoOutputStream.write(1120986464262L, ((BatteryStats.Uid.Proc)tmp1496_1492).getNumAnrs(0));
        paramProtoOutputStream.write(1120986464263L, ((BatteryStats.Uid.Proc)tmp1496_1492).getNumCrashes(0));
        paramProtoOutputStream.end(l2);
      }
      Object tmp1496_1492 = ((Uid)localObject3).getSensorStats();
      tmp1492_1488 = 0;
      localObject1 = localObject2;
      while (tmp1492_1488 < ((SparseArray)tmp1496_1492).size())
      {
        Object localObject8 = (BatteryStats.Uid.Sensor)((SparseArray)tmp1496_1492).valueAt(tmp1492_1488);
        localObject2 = ((BatteryStats.Uid.Sensor)localObject8).getSensorTime();
        if (localObject2 != null)
        {
          localObject8 = ((BatteryStats.Uid.Sensor)localObject8).getSensorBackgroundTime();
          i1 = ((SparseArray)tmp1496_1492).keyAt(tmp1492_1488);
          l2 = paramProtoOutputStream.start(2246267895829L);
          paramProtoOutputStream.write(1120986464257L, i1);
          dumpTimer(paramProtoOutputStream, 1146756268034L, (Timer)localObject2, l7, 0);
          dumpTimer(paramProtoOutputStream, 1146756268035L, (Timer)localObject8, l7, 0);
          paramProtoOutputStream.end(l2);
        }
        tmp1492_1488++;
      }
      m = 0;
      l2 = l7;
      while (m < 7)
      {
        l7 = roundUsToMs(((Uid)localObject3).getProcessStateTime(m, l2, 0));
        if (l7 != 0L)
        {
          l8 = paramProtoOutputStream.start(2246267895828L);
          paramProtoOutputStream.write(1159641169921L, m);
          paramProtoOutputStream.write(1112396529666L, l7);
          paramProtoOutputStream.end(l8);
        }
        m++;
      }
      localObject1 = ((Uid)localObject3).getSyncStats();
      for (m = ((ArrayMap)localObject1).size() - 1; m >= 0; m--)
      {
        localObject2 = (Timer)((ArrayMap)localObject1).valueAt(m);
        tmp1496_1492 = ((Timer)localObject2).getSubTimer();
        l6 = paramProtoOutputStream.start(2246267895830L);
        paramProtoOutputStream.write(1138166333441L, (String)((ArrayMap)localObject1).keyAt(m));
        dumpTimer(paramProtoOutputStream, 1146756268034L, (Timer)localObject2, l2, 0);
        dumpTimer(paramProtoOutputStream, 1146756268035L, (Timer)tmp1496_1492, l2, 0);
        paramProtoOutputStream.end(l6);
      }
      localObject2 = localObject1;
      if (((Uid)localObject3).hasUserActivity()) {
        for (m = 0;; m++)
        {
          localObject2 = localObject1;
          if (m >= 4) {
            break;
          }
          tmp1492_1488 = ((Uid)localObject3).getUserActivityCount(m, 0);
          if (tmp1492_1488 != 0)
          {
            l6 = paramProtoOutputStream.start(2246267895831L);
            paramProtoOutputStream.write(1159641169921L, m);
            paramProtoOutputStream.write(1120986464258L, tmp1492_1488);
            paramProtoOutputStream.end(l6);
          }
        }
      }
      dumpTimer(paramProtoOutputStream, 1146756268045L, ((Uid)localObject3).getVibratorOnTimer(), l2, 0);
      dumpTimer(paramProtoOutputStream, 1146756268046L, ((Uid)localObject3).getVideoTurnedOnTimer(), l2, 0);
      localObject2 = ((Uid)localObject3).getWakelockStats();
      m = ((ArrayMap)localObject2).size() - 1;
      localObject1 = localObject6;
      while (m >= 0)
      {
        tmp1496_1492 = (BatteryStats.Uid.Wakelock)((ArrayMap)localObject2).valueAt(m);
        l6 = paramProtoOutputStream.start(2246267895833L);
        paramProtoOutputStream.write(1138166333441L, (String)((ArrayMap)localObject2).keyAt(m));
        dumpTimer(paramProtoOutputStream, 1146756268034L, ((BatteryStats.Uid.Wakelock)tmp1496_1492).getWakeTime(1), l2, 0);
        localObject6 = ((BatteryStats.Uid.Wakelock)tmp1496_1492).getWakeTime(0);
        if (localObject6 != null)
        {
          dumpTimer(paramProtoOutputStream, 1146756268035L, (Timer)localObject6, l2, 0);
          dumpTimer(paramProtoOutputStream, 1146756268036L, ((Timer)localObject6).getSubTimer(), l2, 0);
        }
        dumpTimer(paramProtoOutputStream, 1146756268037L, ((BatteryStats.Uid.Wakelock)tmp1496_1492).getWakeTime(2), l2, 0);
        paramProtoOutputStream.end(l6);
        m--;
      }
      dumpTimer(paramProtoOutputStream, 1146756268060L, ((Uid)localObject3).getMulticastWakelockStats(), l2, 0);
      for (m = ((ArrayMap)localObject5).size() - 1; m >= 0; m--)
      {
        localObject1 = ((BatteryStats.Uid.Pkg)((ArrayMap)localObject5).valueAt(m)).getWakeupAlarmStats();
        for (tmp1492_1488 = ((ArrayMap)localObject1).size() - 1; tmp1492_1488 >= 0; tmp1492_1488--)
        {
          l6 = paramProtoOutputStream.start(2246267895834L);
          paramProtoOutputStream.write(1138166333441L, (String)((ArrayMap)localObject1).keyAt(tmp1492_1488));
          paramProtoOutputStream.write(1120986464258L, ((Counter)((ArrayMap)localObject1).valueAt(tmp1492_1488)).getCountLocked(0));
          paramProtoOutputStream.end(l6);
        }
      }
      dumpControllerActivityProto(paramProtoOutputStream, 1146756268037L, ((Uid)localObject3).getWifiControllerActivity(), 0);
      l6 = paramProtoOutputStream.start(1146756268059L);
      paramProtoOutputStream.write(1112396529665L, roundUsToMs(((Uid)localObject3).getFullWifiLockTime(l2, 0)));
      dumpTimer(paramProtoOutputStream, 1146756268035L, ((Uid)localObject3).getWifiScanTimer(), l2, 0);
      paramProtoOutputStream.write(1112396529666L, roundUsToMs(((Uid)localObject3).getWifiRunningTime(l2, 0)));
      dumpTimer(paramProtoOutputStream, 1146756268036L, ((Uid)localObject3).getWifiScanBackgroundTimer(), l2, 0);
      paramProtoOutputStream.end(l6);
      paramProtoOutputStream.end(l5);
      n++;
      l6 = l2;
      l2 = l3;
      l3 = l6;
    }
  }
  
  private void dumpProtoHistoryLocked(ProtoOutputStream paramProtoOutputStream, int paramInt, long paramLong)
  {
    Object localObject1 = this;
    if (!startIteratingHistoryLocked()) {
      return;
    }
    paramProtoOutputStream.write(1120986464257L, 32);
    paramProtoOutputStream.write(1112396529666L, getParcelVersion());
    paramProtoOutputStream.write(1138166333443L, getStartPlatformVersion());
    paramProtoOutputStream.write(1138166333444L, getEndPlatformVersion());
    int i = 0;
    try
    {
      while (i < getHistoryStringPoolSize())
      {
        l1 = paramProtoOutputStream.start(2246267895813L);
        paramProtoOutputStream.write(1120986464257L, i);
        paramProtoOutputStream.write(1120986464258L, ((BatteryStats)localObject1).getHistoryTagPoolUid(i));
        paramProtoOutputStream.write(1138166333443L, ((BatteryStats)localObject1).getHistoryTagPoolString(i));
        paramProtoOutputStream.end(l1);
        i++;
      }
      HistoryPrinter localHistoryPrinter = new android/os/BatteryStats$HistoryPrinter;
      localHistoryPrinter.<init>();
      HistoryItem localHistoryItem = new android/os/BatteryStats$HistoryItem;
      localHistoryItem.<init>();
      i = 0;
      long l2 = -1L;
      long l1 = -1L;
      localObject1 = null;
      while (getNextHistoryLocked(localHistoryItem))
      {
        l1 = time;
        long l3 = l2;
        if (l2 < 0L) {
          l3 = l1;
        }
        if (time >= paramLong)
        {
          boolean bool;
          if ((paramLong >= 0L) && (i == 0))
          {
            if ((cmd != 5) && (cmd != 7) && (cmd != 4) && (cmd != 8))
            {
              if (currentTime != 0L)
              {
                i = cmd;
                cmd = ((byte)5);
                if ((paramInt & 0x20) != 0) {
                  bool = true;
                } else {
                  bool = false;
                }
                localHistoryPrinter.printNextItem(paramProtoOutputStream, localHistoryItem, l3, bool);
                cmd = ((byte)i);
                i = 1;
              }
            }
            else
            {
              i = 1;
              if ((paramInt & 0x20) != 0) {
                bool = true;
              } else {
                bool = false;
              }
              localHistoryPrinter.printNextItem(paramProtoOutputStream, localHistoryItem, l3, bool);
              cmd = ((byte)0);
            }
            if (localObject1 != null)
            {
              if (cmd != 0)
              {
                if ((paramInt & 0x20) != 0) {
                  bool = true;
                } else {
                  bool = false;
                }
                localHistoryPrinter.printNextItem(paramProtoOutputStream, localHistoryItem, l3, bool);
                cmd = ((byte)0);
              }
              int j = eventCode;
              Object localObject2 = eventTag;
              Object localObject3 = new android/os/BatteryStats$HistoryTag;
              ((HistoryTag)localObject3).<init>();
              eventTag = ((HistoryTag)localObject3);
              for (int k = 0; k < 22; k++)
              {
                Object localObject4 = ((HistoryEventTracker)localObject1).getStateForEvent(k);
                if (localObject4 != null)
                {
                  Iterator localIterator = ((HashMap)localObject4).entrySet().iterator();
                  while (localIterator.hasNext())
                  {
                    Map.Entry localEntry = (Map.Entry)localIterator.next();
                    localObject3 = (SparseIntArray)localEntry.getValue();
                    int m = 0;
                    Object localObject5 = localObject2;
                    localObject2 = localObject4;
                    localObject4 = localEntry;
                    while (m < ((SparseIntArray)localObject3).size())
                    {
                      eventCode = k;
                      eventTag.string = ((String)((Map.Entry)localObject4).getKey());
                      eventTag.uid = ((SparseIntArray)localObject3).keyAt(m);
                      eventTag.poolIdx = ((SparseIntArray)localObject3).valueAt(m);
                      if ((paramInt & 0x20) != 0) {
                        bool = true;
                      } else {
                        bool = false;
                      }
                      localHistoryPrinter.printNextItem(paramProtoOutputStream, localHistoryItem, l3, bool);
                      wakeReasonTag = null;
                      wakelockTag = null;
                      m++;
                    }
                    localObject4 = localObject2;
                    localObject2 = localObject5;
                  }
                }
              }
              eventCode = j;
              eventTag = ((HistoryTag)localObject2);
              localObject1 = null;
            }
            else {}
          }
          if ((paramInt & 0x20) != 0) {
            bool = true;
          } else {
            bool = false;
          }
          localHistoryPrinter.printNextItem(paramProtoOutputStream, localHistoryItem, l3, bool);
          l2 = l3;
        }
        else
        {
          l2 = l3;
        }
      }
      if (paramLong >= 0L)
      {
        commitCurrentHistoryBatchLocked();
        localObject1 = new java/lang/StringBuilder;
        ((StringBuilder)localObject1).<init>();
        ((StringBuilder)localObject1).append("NEXT: ");
        ((StringBuilder)localObject1).append(l1 + 1L);
        paramProtoOutputStream.write(2237677961222L, ((StringBuilder)localObject1).toString());
      }
      return;
    }
    finally
    {
      finishIteratingHistoryLocked();
    }
  }
  
  private void dumpProtoSystemLocked(ProtoOutputStream paramProtoOutputStream, BatteryStatsHelper paramBatteryStatsHelper)
  {
    long l1 = paramProtoOutputStream.start(1146756268038L);
    long l2 = SystemClock.uptimeMillis() * 1000L;
    long l3 = SystemClock.elapsedRealtime() * 1000L;
    long l4 = paramProtoOutputStream.start(1146756268033L);
    paramProtoOutputStream.write(1112396529665L, getStartClockTime());
    paramProtoOutputStream.write(1112396529666L, getStartCount());
    paramProtoOutputStream.write(1112396529667L, computeRealtime(l3, 0) / 1000L);
    paramProtoOutputStream.write(1112396529668L, computeUptime(l2, 0) / 1000L);
    paramProtoOutputStream.write(1112396529669L, computeBatteryRealtime(l3, 0) / 1000L);
    paramProtoOutputStream.write(1112396529670L, computeBatteryUptime(l2, 0) / 1000L);
    paramProtoOutputStream.write(1112396529671L, computeBatteryScreenOffRealtime(l3, 0) / 1000L);
    paramProtoOutputStream.write(1112396529672L, computeBatteryScreenOffUptime(l2, 0) / 1000L);
    paramProtoOutputStream.write(1112396529673L, getScreenDozeTime(l3, 0) / 1000L);
    paramProtoOutputStream.write(1112396529674L, getEstimatedBatteryCapacity());
    paramProtoOutputStream.write(1112396529675L, getMinLearnedBatteryCapacity());
    paramProtoOutputStream.write(1112396529676L, getMaxLearnedBatteryCapacity());
    paramProtoOutputStream.end(l4);
    l4 = paramProtoOutputStream.start(1146756268034L);
    paramProtoOutputStream.write(1120986464257L, getLowDischargeAmountSinceCharge());
    paramProtoOutputStream.write(1120986464258L, getHighDischargeAmountSinceCharge());
    paramProtoOutputStream.write(1120986464259L, getDischargeAmountScreenOnSinceCharge());
    paramProtoOutputStream.write(1120986464260L, getDischargeAmountScreenOffSinceCharge());
    paramProtoOutputStream.write(1120986464261L, getDischargeAmountScreenDozeSinceCharge());
    paramProtoOutputStream.write(1112396529670L, getUahDischarge(0) / 1000L);
    paramProtoOutputStream.write(1112396529671L, getUahDischargeScreenOff(0) / 1000L);
    paramProtoOutputStream.write(1112396529672L, getUahDischargeScreenDoze(0) / 1000L);
    paramProtoOutputStream.write(1112396529673L, getUahDischargeLightDoze(0) / 1000L);
    paramProtoOutputStream.write(1112396529674L, getUahDischargeDeepDoze(0) / 1000L);
    paramProtoOutputStream.end(l4);
    l4 = computeChargeTimeRemaining(l3);
    if (l4 >= 0L) {
      paramProtoOutputStream.write(1112396529667L, l4 / 1000L);
    }
    for (;;)
    {
      break;
      l4 = computeBatteryTimeRemaining(l3);
      if (l4 >= 0L) {
        paramProtoOutputStream.write(1112396529668L, l4 / 1000L);
      } else {
        paramProtoOutputStream.write(1112396529668L, -1);
      }
    }
    dumpDurationSteps(paramProtoOutputStream, 2246267895813L, getChargeLevelStepTracker());
    int i = 0;
    l2 = l4;
    l4 = l3;
    for (;;)
    {
      boolean bool = true;
      if (i >= 21) {
        break;
      }
      if (i != 0) {
        bool = false;
      }
      j = i;
      if (i == 20) {
        j = 0;
      }
      l3 = paramProtoOutputStream.start(2246267895816L);
      if (bool) {
        paramProtoOutputStream.write(1133871366146L, bool);
      } else {
        paramProtoOutputStream.write(1159641169921L, j);
      }
      localObject1 = getPhoneDataConnectionTimer(i);
      dumpTimer(paramProtoOutputStream, 1146756268035L, (Timer)localObject1, l4, 0);
      paramProtoOutputStream.end(l3);
      i++;
    }
    dumpDurationSteps(paramProtoOutputStream, 2246267895814L, getDischargeLevelStepTracker());
    Object localObject1 = getCpuFreqs();
    if (localObject1 != null)
    {
      j = localObject1.length;
      for (i = 0; i < j; i++) {
        paramProtoOutputStream.write(2211908157447L, localObject1[i]);
      }
    }
    dumpControllerActivityProto(paramProtoOutputStream, 1146756268041L, getBluetoothControllerActivity(), 0);
    dumpControllerActivityProto(paramProtoOutputStream, 1146756268042L, getModemControllerActivity(), 0);
    l2 = paramProtoOutputStream.start(1146756268044L);
    paramProtoOutputStream.write(1112396529665L, getNetworkActivityBytes(0, 0));
    paramProtoOutputStream.write(1112396529666L, getNetworkActivityBytes(1, 0));
    paramProtoOutputStream.write(1112396529669L, getNetworkActivityPackets(0, 0));
    paramProtoOutputStream.write(1112396529670L, getNetworkActivityPackets(1, 0));
    paramProtoOutputStream.write(1112396529667L, getNetworkActivityBytes(2, 0));
    paramProtoOutputStream.write(1112396529668L, getNetworkActivityBytes(3, 0));
    paramProtoOutputStream.write(1112396529671L, getNetworkActivityPackets(2, 0));
    paramProtoOutputStream.write(1112396529672L, getNetworkActivityPackets(3, 0));
    paramProtoOutputStream.write(1112396529673L, getNetworkActivityBytes(4, 0));
    paramProtoOutputStream.write(1112396529674L, getNetworkActivityBytes(5, 0));
    paramProtoOutputStream.end(l2);
    dumpControllerActivityProto(paramProtoOutputStream, 1146756268043L, getWifiControllerActivity(), 0);
    l2 = paramProtoOutputStream.start(1146756268045L);
    l3 = getWifiOnTime(l4, 0) / 1000L;
    Object localObject2 = paramProtoOutputStream;
    ((ProtoOutputStream)localObject2).write(1112396529665L, l3);
    ((ProtoOutputStream)localObject2).write(1112396529666L, getGlobalWifiRunningTime(l4, 0) / 1000L);
    ((ProtoOutputStream)localObject2).end(l2);
    localObject1 = getKernelWakelockStats();
    Object localObject3 = ((Map)localObject1).entrySet().iterator();
    while (((Iterator)localObject3).hasNext())
    {
      localObject4 = (Map.Entry)((Iterator)localObject3).next();
      l3 = ((ProtoOutputStream)localObject2).start(2246267895822L);
      ((ProtoOutputStream)localObject2).write(1138166333441L, (String)((Map.Entry)localObject4).getKey());
      dumpTimer((ProtoOutputStream)localObject2, 1146756268034L, (Timer)((Map.Entry)localObject4).getValue(), l4, 0);
      ((ProtoOutputStream)localObject2).end(l3);
    }
    Object localObject4 = getUidStats();
    long l5 = 0L;
    l2 = 0L;
    i = 0;
    Object localObject5;
    Object localObject6;
    for (l3 = l4; i < ((SparseArray)localObject4).size(); l3 = l4)
    {
      localObject3 = (Uid)((SparseArray)localObject4).valueAt(i);
      localObject1 = ((Uid)localObject3).getWakelockStats();
      j = ((ArrayMap)localObject1).size() - 1;
      l4 = l3;
      l3 = l5;
      while (j >= 0)
      {
        localObject5 = (BatteryStats.Uid.Wakelock)((ArrayMap)localObject1).valueAt(j);
        localObject6 = ((BatteryStats.Uid.Wakelock)localObject5).getWakeTime(1);
        if (localObject6 != null) {
          l3 += ((Timer)localObject6).getTotalTimeLocked(l4, 0);
        }
        l5 = l4;
        localObject6 = ((BatteryStats.Uid.Wakelock)localObject5).getWakeTime(0);
        l4 = l2;
        if (localObject6 != null) {
          l4 = l2 + ((Timer)localObject6).getTotalTimeLocked(l5, 0);
        }
        j--;
        l2 = l4;
        l4 = l5;
      }
      i++;
      l5 = l3;
    }
    l4 = ((ProtoOutputStream)localObject2).start(1146756268047L);
    ((ProtoOutputStream)localObject2).write(1112396529665L, getScreenOnTime(l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1112396529666L, getPhoneOnTime(l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1112396529667L, l5 / 1000L);
    ((ProtoOutputStream)localObject2).write(1112396529668L, l2 / 1000L);
    ((ProtoOutputStream)localObject2).write(1112396529669L, getMobileRadioActiveTime(l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1112396529670L, getMobileRadioActiveAdjustedTime(0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1120986464263L, getMobileRadioActiveCount(0));
    ((ProtoOutputStream)localObject2).write(1120986464264L, getMobileRadioActiveUnknownTime(0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1112396529673L, getInteractiveTime(l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1112396529674L, getPowerSaveModeEnabledTime(l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1120986464267L, getNumConnectivityChange(0));
    ((ProtoOutputStream)localObject2).write(1112396529676L, getDeviceIdleModeTime(2, l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1120986464269L, getDeviceIdleModeCount(2, 0));
    ((ProtoOutputStream)localObject2).write(1112396529678L, getDeviceIdlingTime(2, l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1120986464271L, getDeviceIdlingCount(2, 0));
    ((ProtoOutputStream)localObject2).write(1112396529680L, getLongestDeviceIdleModeTime(2));
    ((ProtoOutputStream)localObject2).write(1112396529681L, getDeviceIdleModeTime(1, l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1120986464274L, getDeviceIdleModeCount(1, 0));
    ((ProtoOutputStream)localObject2).write(1112396529683L, getDeviceIdlingTime(1, l3, 0) / 1000L);
    ((ProtoOutputStream)localObject2).write(1120986464276L, getDeviceIdlingCount(1, 0));
    ((ProtoOutputStream)localObject2).write(1112396529685L, getLongestDeviceIdleModeTime(1));
    ((ProtoOutputStream)localObject2).end(l4);
    l4 = getWifiMulticastWakelockTime(l3, 0);
    int k = getWifiMulticastWakelockCount(0);
    l2 = ((ProtoOutputStream)localObject2).start(1146756268055L);
    ((ProtoOutputStream)localObject2).write(1112396529665L, l4 / 1000L);
    ((ProtoOutputStream)localObject2).write(1120986464258L, k);
    ((ProtoOutputStream)localObject2).end(l2);
    localObject1 = paramBatteryStatsHelper.getUsageList();
    l5 = l2;
    l5 = l4;
    if (localObject1 != null) {
      label1893:
      for (j = 0;; j++)
      {
        l5 = l2;
        l5 = l4;
        if (j >= ((List)localObject1).size()) {
          break;
        }
        localObject3 = (BatterySipper)((List)localObject1).get(j);
        i = 0;
        int m = 0;
        switch (2.$SwitchMap$com$android$internal$os$BatterySipper$DrainType[drainType.ordinal()])
        {
        }
        for (;;)
        {
          break;
          i = 12;
          continue;
          i = 11;
          continue;
          i = 10;
          continue;
          i = 9;
          continue;
          i = 8;
          m = UserHandle.getUid(userId, 0);
          break;
          break label1893;
          i = 6;
          break;
          i = 7;
          break;
          i = 5;
          break;
          i = 4;
          break;
          i = 3;
          break;
          i = 2;
          break;
          i = 1;
          break;
          i = 13;
        }
        l5 = ((ProtoOutputStream)localObject2).start(2246267895825L);
        ((ProtoOutputStream)localObject2).write(1159641169921L, i);
        ((ProtoOutputStream)localObject2).write(1120986464258L, m);
        ((ProtoOutputStream)localObject2).write(1103806595075L, totalPowerMah);
        ((ProtoOutputStream)localObject2).write(1133871366148L, shouldHide);
        ((ProtoOutputStream)localObject2).write(1103806595077L, screenPowerMah);
        ((ProtoOutputStream)localObject2).write(1103806595078L, proportionalSmearMah);
        ((ProtoOutputStream)localObject2).end(l5);
      }
    }
    l4 = ((ProtoOutputStream)localObject2).start(1146756268050L);
    ((ProtoOutputStream)localObject2).write(1103806595073L, paramBatteryStatsHelper.getPowerProfile().getBatteryCapacity());
    ((ProtoOutputStream)localObject2).write(1103806595074L, paramBatteryStatsHelper.getComputedPower());
    ((ProtoOutputStream)localObject2).write(1103806595075L, paramBatteryStatsHelper.getMinDrainedPower());
    ((ProtoOutputStream)localObject2).write(1103806595076L, paramBatteryStatsHelper.getMaxDrainedPower());
    ((ProtoOutputStream)localObject2).end(l4);
    localObject4 = getRpmStats();
    localObject2 = getScreenOffRpmStats();
    localObject3 = ((Map)localObject4).entrySet().iterator();
    paramBatteryStatsHelper = (BatteryStatsHelper)localObject1;
    i = k;
    localObject1 = localObject4;
    for (;;)
    {
      localObject4 = paramProtoOutputStream;
      if (!((Iterator)localObject3).hasNext()) {
        break;
      }
      localObject6 = (Map.Entry)((Iterator)localObject3).next();
      l2 = ((ProtoOutputStream)localObject4).start(2246267895827L);
      localObject5 = (String)((Map.Entry)localObject6).getKey();
      localObject4 = paramProtoOutputStream;
      ((ProtoOutputStream)localObject4).write(1138166333441L, (String)localObject5);
      dumpTimer((ProtoOutputStream)localObject4, 1146756268034L, (Timer)((Map.Entry)localObject6).getValue(), l3, 0);
      dumpTimer((ProtoOutputStream)localObject4, 1146756268035L, (Timer)((Map)localObject2).get(((Map.Entry)localObject6).getKey()), l3, 0);
      ((ProtoOutputStream)localObject4).end(l2);
    }
    i = 0;
    for (int j = i; j < 5; j++)
    {
      l4 = ((ProtoOutputStream)localObject4).start(2246267895828L);
      ((ProtoOutputStream)localObject4).write(1159641169921L, j);
      dumpTimer((ProtoOutputStream)localObject4, 1146756268034L, getScreenBrightnessTimer(j), l3, 0);
      ((ProtoOutputStream)localObject4).end(l4);
    }
    dumpTimer((ProtoOutputStream)localObject4, 1146756268053L, getPhoneSignalScanningTimer(), l3, 0);
    for (j = i; j < 6; j++)
    {
      l4 = ((ProtoOutputStream)localObject4).start(2246267895824L);
      ((ProtoOutputStream)localObject4).write(1159641169921L, j);
      dumpTimer((ProtoOutputStream)localObject4, 1146756268034L, getPhoneSignalStrengthTimer(j), l3, 0);
      ((ProtoOutputStream)localObject4).end(l4);
    }
    paramProtoOutputStream = getWakeupReasonStats().entrySet().iterator();
    while (paramProtoOutputStream.hasNext())
    {
      paramBatteryStatsHelper = (Map.Entry)paramProtoOutputStream.next();
      l4 = ((ProtoOutputStream)localObject4).start(2246267895830L);
      ((ProtoOutputStream)localObject4).write(1138166333441L, (String)paramBatteryStatsHelper.getKey());
      dumpTimer((ProtoOutputStream)localObject4, 1146756268034L, (Timer)paramBatteryStatsHelper.getValue(), l3, 0);
      ((ProtoOutputStream)localObject4).end(l4);
    }
    for (j = i; j < 5; j++)
    {
      l4 = ((ProtoOutputStream)localObject4).start(2246267895832L);
      ((ProtoOutputStream)localObject4).write(1159641169921L, j);
      dumpTimer((ProtoOutputStream)localObject4, 1146756268034L, getWifiSignalStrengthTimer(j), l3, 0);
      ((ProtoOutputStream)localObject4).end(l4);
    }
    for (j = i; j < 8; j++)
    {
      l4 = ((ProtoOutputStream)localObject4).start(2246267895833L);
      ((ProtoOutputStream)localObject4).write(1159641169921L, j);
      dumpTimer((ProtoOutputStream)localObject4, 1146756268034L, getWifiStateTimer(j), l3, 0);
      ((ProtoOutputStream)localObject4).end(l4);
    }
    while (i < 13)
    {
      l4 = ((ProtoOutputStream)localObject4).start(2246267895834L);
      ((ProtoOutputStream)localObject4).write(1159641169921L, i);
      dumpTimer((ProtoOutputStream)localObject4, 1146756268034L, getWifiSupplStateTimer(i), l3, 0);
      ((ProtoOutputStream)localObject4).end(l4);
      i++;
    }
    ((ProtoOutputStream)localObject4).end(l1);
  }
  
  private static boolean dumpTimeEstimate(PrintWriter paramPrintWriter, String paramString1, String paramString2, String paramString3, long paramLong)
  {
    if (paramLong < 0L) {
      return false;
    }
    paramPrintWriter.print(paramString1);
    paramPrintWriter.print(paramString2);
    paramPrintWriter.print(paramString3);
    paramString1 = new StringBuilder(64);
    formatTimeMs(paramString1, paramLong);
    paramPrintWriter.print(paramString1);
    paramPrintWriter.println();
    return true;
  }
  
  private static void dumpTimer(ProtoOutputStream paramProtoOutputStream, long paramLong1, Timer paramTimer, long paramLong2, int paramInt)
  {
    if (paramTimer == null) {
      return;
    }
    long l1 = roundUsToMs(paramTimer.getTotalTimeLocked(paramLong2, paramInt));
    paramInt = paramTimer.getCountLocked(paramInt);
    long l2 = paramTimer.getMaxDurationMsLocked(paramLong2 / 1000L);
    long l3 = paramTimer.getCurrentDurationMsLocked(paramLong2 / 1000L);
    paramLong2 = paramTimer.getTotalDurationMsLocked(paramLong2 / 1000L);
    if ((l1 != 0L) || (paramInt != 0) || (l2 != -1L) || (l3 != -1L) || (paramLong2 != -1L))
    {
      paramLong1 = paramProtoOutputStream.start(paramLong1);
      paramProtoOutputStream.write(1112396529665L, l1);
      paramProtoOutputStream.write(1112396529666L, paramInt);
      if (l2 != -1L) {
        paramProtoOutputStream.write(1112396529667L, l2);
      }
      if (l3 != -1L) {
        paramProtoOutputStream.write(1112396529668L, l3);
      }
      if (paramLong2 != -1L) {
        paramProtoOutputStream.write(1112396529669L, paramLong2);
      }
      paramProtoOutputStream.end(paramLong1);
    }
  }
  
  private static final void dumpTimer(PrintWriter paramPrintWriter, int paramInt1, String paramString1, String paramString2, Timer paramTimer, long paramLong, int paramInt2)
  {
    if (paramTimer != null)
    {
      paramLong = roundUsToMs(paramTimer.getTotalTimeLocked(paramLong, paramInt2));
      paramInt2 = paramTimer.getCountLocked(paramInt2);
      if ((paramLong != 0L) || (paramInt2 != 0)) {
        dumpLine(paramPrintWriter, paramInt1, paramString1, paramString2, new Object[] { Long.valueOf(paramLong), Integer.valueOf(paramInt2) });
      }
    }
  }
  
  public static final void formatTimeMs(StringBuilder paramStringBuilder, long paramLong)
  {
    long l = paramLong / 1000L;
    formatTimeRaw(paramStringBuilder, l);
    paramStringBuilder.append(paramLong - 1000L * l);
    paramStringBuilder.append("ms ");
  }
  
  public static final void formatTimeMsNoSpace(StringBuilder paramStringBuilder, long paramLong)
  {
    long l = paramLong / 1000L;
    formatTimeRaw(paramStringBuilder, l);
    paramStringBuilder.append(paramLong - 1000L * l);
    paramStringBuilder.append("ms");
  }
  
  private static final void formatTimeRaw(StringBuilder paramStringBuilder, long paramLong)
  {
    long l1 = paramLong / 86400L;
    if (l1 != 0L)
    {
      paramStringBuilder.append(l1);
      paramStringBuilder.append("d ");
    }
    l1 = l1 * 60L * 60L * 24L;
    long l2 = (paramLong - l1) / 3600L;
    if ((l2 != 0L) || (l1 != 0L))
    {
      paramStringBuilder.append(l2);
      paramStringBuilder.append("h ");
    }
    l1 += l2 * 60L * 60L;
    l2 = (paramLong - l1) / 60L;
    if ((l2 != 0L) || (l1 != 0L))
    {
      paramStringBuilder.append(l2);
      paramStringBuilder.append("m ");
    }
    l1 += 60L * l2;
    if ((paramLong != 0L) || (l1 != 0L))
    {
      paramStringBuilder.append(paramLong - l1);
      paramStringBuilder.append("s ");
    }
  }
  
  public static int mapToInternalProcessState(int paramInt)
  {
    if (paramInt == 19) {
      return 19;
    }
    if (paramInt == 2) {
      return 0;
    }
    if (paramInt == 3) {
      return 1;
    }
    if (paramInt <= 5) {
      return 2;
    }
    if (paramInt <= 10) {
      return 3;
    }
    if (paramInt <= 11) {
      return 4;
    }
    if (paramInt <= 12) {
      return 5;
    }
    return 6;
  }
  
  static void printBitDescriptions(StringBuilder paramStringBuilder, int paramInt1, int paramInt2, HistoryTag paramHistoryTag, BitDescription[] paramArrayOfBitDescription, boolean paramBoolean)
  {
    int i = paramInt1 ^ paramInt2;
    if (i == 0) {
      return;
    }
    int j = 0;
    paramInt1 = 0;
    while (paramInt1 < paramArrayOfBitDescription.length)
    {
      BitDescription localBitDescription = paramArrayOfBitDescription[paramInt1];
      int k = j;
      if ((mask & i) != 0)
      {
        String str;
        if (paramBoolean) {
          str = " ";
        } else {
          str = ",";
        }
        paramStringBuilder.append(str);
        if (shift < 0)
        {
          if ((mask & paramInt2) != 0) {
            str = "+";
          } else {
            str = "-";
          }
          paramStringBuilder.append(str);
          if (paramBoolean) {
            str = name;
          } else {
            str = shortName;
          }
          paramStringBuilder.append(str);
          k = j;
          if (mask == 1073741824)
          {
            k = j;
            if (paramHistoryTag != null)
            {
              k = 1;
              paramStringBuilder.append("=");
              if (paramBoolean)
              {
                UserHandle.formatUid(paramStringBuilder, uid);
                paramStringBuilder.append(":\"");
                paramStringBuilder.append(string);
                paramStringBuilder.append("\"");
              }
              else
              {
                paramStringBuilder.append(poolIdx);
              }
            }
          }
        }
        else
        {
          if (paramBoolean) {
            str = name;
          } else {
            str = shortName;
          }
          paramStringBuilder.append(str);
          paramStringBuilder.append("=");
          k = (mask & paramInt2) >> shift;
          if ((values != null) && (k >= 0) && (k < values.length))
          {
            if (paramBoolean) {
              str = values[k];
            } else {
              str = shortValues[k];
            }
            paramStringBuilder.append(str);
            k = j;
          }
          else
          {
            paramStringBuilder.append(k);
            k = j;
          }
        }
      }
      paramInt1++;
      j = k;
    }
    if ((j == 0) && (paramHistoryTag != null))
    {
      if (paramBoolean) {
        paramArrayOfBitDescription = " wake_lock=";
      } else {
        paramArrayOfBitDescription = ",w=";
      }
      paramStringBuilder.append(paramArrayOfBitDescription);
      if (paramBoolean)
      {
        UserHandle.formatUid(paramStringBuilder, uid);
        paramStringBuilder.append(":\"");
        paramStringBuilder.append(string);
        paramStringBuilder.append("\"");
      }
      else
      {
        paramStringBuilder.append(poolIdx);
      }
    }
  }
  
  private final void printControllerActivity(PrintWriter paramPrintWriter, StringBuilder paramStringBuilder, String paramString1, String paramString2, ControllerActivityCounter paramControllerActivityCounter, int paramInt)
  {
    long l1 = paramControllerActivityCounter.getIdleTimeCounter().getCountLocked(paramInt);
    long l2 = paramControllerActivityCounter.getRxTimeCounter().getCountLocked(paramInt);
    long l3 = paramControllerActivityCounter.getPowerCounter().getCountLocked(paramInt);
    long l4 = computeBatteryRealtime(SystemClock.elapsedRealtime() * 1000L, paramInt) / 1000L;
    Object localObject = paramControllerActivityCounter.getTxTimeCounters();
    long l5 = 0L;
    int i = localObject.length;
    for (int j = 0; j < i; j++) {
      l5 += localObject[j].getCountLocked(paramInt);
    }
    if (paramString2.equals("WiFi"))
    {
      long l6 = paramControllerActivityCounter.getScanTimeCounter().getCountLocked(paramInt);
      paramStringBuilder.setLength(0);
      paramStringBuilder.append(paramString1);
      paramStringBuilder.append("     ");
      paramStringBuilder.append(paramString2);
      paramStringBuilder.append(" Scan time:  ");
      formatTimeMs(paramStringBuilder, l6);
      paramStringBuilder.append("(");
      paramStringBuilder.append(formatRatioLocked(l6, l4));
      paramStringBuilder.append(")");
      paramPrintWriter.println(paramStringBuilder.toString());
      l5 = l4 - (l1 + l2 + l5);
      paramStringBuilder.setLength(0);
      paramStringBuilder.append(paramString1);
      paramStringBuilder.append("     ");
      paramStringBuilder.append(paramString2);
      paramStringBuilder.append(" Sleep time:  ");
      formatTimeMs(paramStringBuilder, l5);
      paramStringBuilder.append("(");
      paramStringBuilder.append(formatRatioLocked(l5, l4));
      paramStringBuilder.append(")");
      paramPrintWriter.println(paramStringBuilder.toString());
    }
    if (paramString2.equals("Cellular"))
    {
      l5 = paramControllerActivityCounter.getSleepTimeCounter().getCountLocked(paramInt);
      paramStringBuilder.setLength(0);
      paramStringBuilder.append(paramString1);
      paramStringBuilder.append("     ");
      paramStringBuilder.append(paramString2);
      paramStringBuilder.append(" Sleep time:  ");
      formatTimeMs(paramStringBuilder, l5);
      paramStringBuilder.append("(");
      paramStringBuilder.append(formatRatioLocked(l5, l4));
      paramStringBuilder.append(")");
      paramPrintWriter.println(paramStringBuilder.toString());
    }
    paramStringBuilder.setLength(0);
    paramStringBuilder.append(paramString1);
    paramStringBuilder.append("     ");
    paramStringBuilder.append(paramString2);
    paramStringBuilder.append(" Idle time:   ");
    formatTimeMs(paramStringBuilder, l1);
    paramStringBuilder.append("(");
    paramStringBuilder.append(formatRatioLocked(l1, l4));
    paramStringBuilder.append(")");
    paramPrintWriter.println(paramStringBuilder.toString());
    paramStringBuilder.setLength(0);
    paramStringBuilder.append(paramString1);
    paramStringBuilder.append("     ");
    paramStringBuilder.append(paramString2);
    paramStringBuilder.append(" Rx time:     ");
    formatTimeMs(paramStringBuilder, l2);
    paramStringBuilder.append("(");
    paramStringBuilder.append(formatRatioLocked(l2, l4));
    paramStringBuilder.append(")");
    paramPrintWriter.println(paramStringBuilder.toString());
    paramStringBuilder.setLength(0);
    paramStringBuilder.append(paramString1);
    paramStringBuilder.append("     ");
    paramStringBuilder.append(paramString2);
    paramStringBuilder.append(" Tx time:     ");
    i = -1;
    if ((paramString2.hashCode() == -851952246) && (paramString2.equals("Cellular"))) {
      i = 0;
    }
    if (i != 0) {
      localObject = new String[] { "[0]", "[1]", "[2]", "[3]", "[4]" };
    } else {
      localObject = new String[] { "   less than 0dBm: ", "   0dBm to 8dBm: ", "   8dBm to 15dBm: ", "   15dBm to 20dBm: ", "   above 20dBm: " };
    }
    i = Math.min(paramControllerActivityCounter.getTxTimeCounters().length, localObject.length);
    if (i > 1)
    {
      paramPrintWriter.println(paramStringBuilder.toString());
      for (j = 0; j < i; j++)
      {
        l5 = paramControllerActivityCounter.getTxTimeCounters()[j].getCountLocked(paramInt);
        paramStringBuilder.setLength(0);
        paramStringBuilder.append(paramString1);
        paramStringBuilder.append("    ");
        paramStringBuilder.append(localObject[j]);
        paramStringBuilder.append(" ");
        formatTimeMs(paramStringBuilder, l5);
        paramStringBuilder.append("(");
        paramStringBuilder.append(formatRatioLocked(l5, l4));
        paramStringBuilder.append(")");
        paramPrintWriter.println(paramStringBuilder.toString());
      }
    }
    else
    {
      l5 = paramControllerActivityCounter.getTxTimeCounters()[0].getCountLocked(paramInt);
      formatTimeMs(paramStringBuilder, l5);
      paramStringBuilder.append("(");
      paramStringBuilder.append(formatRatioLocked(l5, l4));
      paramStringBuilder.append(")");
      paramPrintWriter.println(paramStringBuilder.toString());
    }
    if (l3 > 0L)
    {
      paramStringBuilder.setLength(0);
      paramStringBuilder.append(paramString1);
      paramStringBuilder.append("     ");
      paramStringBuilder.append(paramString2);
      paramStringBuilder.append(" Battery drain: ");
      paramStringBuilder.append(BatteryStatsHelper.makemAh(l3 / 3600000.0D));
      paramStringBuilder.append("mAh");
      paramPrintWriter.println(paramStringBuilder.toString());
    }
  }
  
  private final void printControllerActivityIfInteresting(PrintWriter paramPrintWriter, StringBuilder paramStringBuilder, String paramString1, String paramString2, ControllerActivityCounter paramControllerActivityCounter, int paramInt)
  {
    if (controllerActivityHasData(paramControllerActivityCounter, paramInt)) {
      printControllerActivity(paramPrintWriter, paramStringBuilder, paramString1, paramString2, paramControllerActivityCounter, paramInt);
    }
  }
  
  private void printSizeValue(PrintWriter paramPrintWriter, long paramLong)
  {
    float f1 = (float)paramLong;
    String str = "";
    float f2 = f1;
    if (f1 >= 10240.0F)
    {
      str = "KB";
      f2 = f1 / 1024.0F;
    }
    float f3 = f2;
    if (f2 >= 10240.0F)
    {
      str = "MB";
      f3 = f2 / 1024.0F;
    }
    f1 = f3;
    if (f3 >= 10240.0F)
    {
      str = "GB";
      f1 = f3 / 1024.0F;
    }
    f2 = f1;
    if (f1 >= 10240.0F)
    {
      str = "TB";
      f2 = f1 / 1024.0F;
    }
    f1 = f2;
    if (f2 >= 10240.0F)
    {
      str = "PB";
      f1 = f2 / 1024.0F;
    }
    paramPrintWriter.print((int)f1);
    paramPrintWriter.print(str);
  }
  
  private static final boolean printTimer(PrintWriter paramPrintWriter, StringBuilder paramStringBuilder, Timer paramTimer, long paramLong, int paramInt, String paramString1, String paramString2)
  {
    if (paramTimer != null)
    {
      long l = (paramTimer.getTotalTimeLocked(paramLong, paramInt) + 500L) / 1000L;
      paramInt = paramTimer.getCountLocked(paramInt);
      if (l != 0L)
      {
        paramStringBuilder.setLength(0);
        paramStringBuilder.append(paramString1);
        paramStringBuilder.append("    ");
        paramStringBuilder.append(paramString2);
        paramStringBuilder.append(": ");
        formatTimeMs(paramStringBuilder, l);
        paramStringBuilder.append("realtime (");
        paramStringBuilder.append(paramInt);
        paramStringBuilder.append(" times)");
        l = paramTimer.getMaxDurationMsLocked(paramLong / 1000L);
        if (l >= 0L)
        {
          paramStringBuilder.append(" max=");
          paramStringBuilder.append(l);
        }
        if (paramTimer.isRunningLocked())
        {
          paramLong = paramTimer.getCurrentDurationMsLocked(paramLong / 1000L);
          if (paramLong >= 0L)
          {
            paramStringBuilder.append(" (running for ");
            paramStringBuilder.append(paramLong);
            paramStringBuilder.append("ms)");
          }
          else
          {
            paramStringBuilder.append(" (running)");
          }
        }
        paramPrintWriter.println(paramStringBuilder.toString());
        return true;
      }
    }
    return false;
  }
  
  private static final String printWakeLock(StringBuilder paramStringBuilder, Timer paramTimer, long paramLong, String paramString1, int paramInt, String paramString2)
  {
    if (paramTimer != null)
    {
      long l1 = computeWakeLock(paramTimer, paramLong, paramInt);
      paramInt = paramTimer.getCountLocked(paramInt);
      if (l1 != 0L)
      {
        paramStringBuilder.append(paramString2);
        formatTimeMs(paramStringBuilder, l1);
        if (paramString1 != null)
        {
          paramStringBuilder.append(paramString1);
          paramStringBuilder.append(' ');
        }
        paramStringBuilder.append('(');
        paramStringBuilder.append(paramInt);
        paramStringBuilder.append(" times)");
        long l2 = paramTimer.getMaxDurationMsLocked(paramLong / 1000L);
        if (l2 >= 0L)
        {
          paramStringBuilder.append(" max=");
          paramStringBuilder.append(l2);
        }
        l2 = paramTimer.getTotalDurationMsLocked(paramLong / 1000L);
        if (l2 > l1)
        {
          paramStringBuilder.append(" actual=");
          paramStringBuilder.append(l2);
        }
        if (paramTimer.isRunningLocked())
        {
          paramLong = paramTimer.getCurrentDurationMsLocked(paramLong / 1000L);
          if (paramLong >= 0L)
          {
            paramStringBuilder.append(" (running for ");
            paramStringBuilder.append(paramLong);
            paramStringBuilder.append("ms)");
          }
          else
          {
            paramStringBuilder.append(" (running)");
          }
        }
        return ", ";
      }
    }
    return paramString2;
  }
  
  private static final String printWakeLockCheckin(StringBuilder paramStringBuilder, Timer paramTimer, long paramLong, String paramString1, int paramInt, String paramString2)
  {
    int i = 0;
    long l1 = 0L;
    long l2 = 0L;
    long l3 = 0L;
    long l4;
    if (paramTimer != null)
    {
      l4 = paramTimer.getTotalTimeLocked(paramLong, paramInt);
      paramInt = paramTimer.getCountLocked(paramInt);
      l2 = paramTimer.getCurrentDurationMsLocked(paramLong / 1000L);
      l1 = paramTimer.getMaxDurationMsLocked(paramLong / 1000L);
      l3 = paramTimer.getTotalDurationMsLocked(paramLong / 1000L);
      paramLong = l2;
    }
    else
    {
      l4 = 0L;
      paramLong = l2;
      paramInt = i;
    }
    paramStringBuilder.append(paramString2);
    paramStringBuilder.append((l4 + 500L) / 1000L);
    paramStringBuilder.append(',');
    if (paramString1 != null)
    {
      paramTimer = new StringBuilder();
      paramTimer.append(paramString1);
      paramTimer.append(",");
      paramTimer = paramTimer.toString();
    }
    else
    {
      paramTimer = "";
    }
    paramStringBuilder.append(paramTimer);
    paramStringBuilder.append(paramInt);
    paramStringBuilder.append(',');
    paramStringBuilder.append(paramLong);
    paramStringBuilder.append(',');
    paramStringBuilder.append(l1);
    if (paramString1 != null)
    {
      paramStringBuilder.append(',');
      paramStringBuilder.append(l3);
    }
    return ",";
  }
  
  private void printmAh(PrintWriter paramPrintWriter, double paramDouble)
  {
    paramPrintWriter.print(BatteryStatsHelper.makemAh(paramDouble));
  }
  
  private void printmAh(StringBuilder paramStringBuilder, double paramDouble)
  {
    paramStringBuilder.append(BatteryStatsHelper.makemAh(paramDouble));
  }
  
  private static long roundUsToMs(long paramLong)
  {
    return (500L + paramLong) / 1000L;
  }
  
  public abstract void commitCurrentHistoryBatchLocked();
  
  public abstract long computeBatteryRealtime(long paramLong, int paramInt);
  
  public abstract long computeBatteryScreenOffRealtime(long paramLong, int paramInt);
  
  public abstract long computeBatteryScreenOffUptime(long paramLong, int paramInt);
  
  public abstract long computeBatteryTimeRemaining(long paramLong);
  
  public abstract long computeBatteryUptime(long paramLong, int paramInt);
  
  public abstract long computeChargeTimeRemaining(long paramLong);
  
  public abstract long computeRealtime(long paramLong, int paramInt);
  
  public abstract long computeUptime(long paramLong, int paramInt);
  
  public final void dumpCheckinLocked(Context paramContext, PrintWriter paramPrintWriter, int paramInt1, int paramInt2)
  {
    dumpCheckinLocked(paramContext, paramPrintWriter, paramInt1, paramInt2, BatteryStatsHelper.checkWifiOnly(paramContext));
  }
  
  public final void dumpCheckinLocked(Context paramContext, PrintWriter paramPrintWriter, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Object localObject1 = paramPrintWriter;
    int i = paramInt1;
    long l1 = SystemClock.uptimeMillis() * 1000L;
    long l2 = SystemClock.elapsedRealtime();
    long l3 = l2 * 1000L;
    long l4 = getBatteryUptime(l1);
    long l5 = computeBatteryUptime(l1, i);
    long l6 = computeBatteryRealtime(l3, i);
    long l7 = computeBatteryScreenOffUptime(l1, i);
    long l8 = computeBatteryScreenOffRealtime(l3, i);
    long l9 = computeRealtime(l3, i);
    long l10 = computeUptime(l1, i);
    long l11 = getScreenOnTime(l3, i);
    long l12 = getScreenDozeTime(l3, i);
    long l13 = getInteractiveTime(l3, i);
    long l14 = getPowerSaveModeEnabledTime(l3, i);
    long l15 = getDeviceIdleModeTime(1, l3, i);
    long l16 = getDeviceIdleModeTime(2, l3, i);
    long l17 = getDeviceIdlingTime(1, l3, i);
    long l18 = getDeviceIdlingTime(2, l3, i);
    int j = getNumConnectivityChange(i);
    long l19 = getPhoneOnTime(l3, i);
    long l20 = getUahDischarge(i);
    long l21 = getUahDischargeScreenOff(i);
    l1 = getUahDischargeScreenDoze(i);
    long l22 = getUahDischargeLightDoze(i);
    long l23 = getUahDischargeDeepDoze(i);
    Object localObject2 = new StringBuilder(128);
    SparseArray localSparseArray = getUidStats();
    int k = localSparseArray.size();
    Object localObject3 = STAT_NAMES[i];
    if (i == 0) {
      localObject4 = Integer.valueOf(getStartCount());
    } else {
      localObject4 = "N/A";
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject3, "bt", new Object[] { localObject4, Long.valueOf(l6 / 1000L), Long.valueOf(l5 / 1000L), Long.valueOf(l9 / 1000L), Long.valueOf(l10 / 1000L), Long.valueOf(getStartClockTime()), Long.valueOf(l8 / 1000L), Long.valueOf(l7 / 1000L), Integer.valueOf(getEstimatedBatteryCapacity()), Integer.valueOf(getMinLearnedBatteryCapacity()), Integer.valueOf(getMaxLearnedBatteryCapacity()), Long.valueOf(l12 / 1000L) });
    l5 = 0L;
    int m = 0;
    l6 = 0L;
    while (m < k)
    {
      localObject4 = (Uid)localSparseArray.valueAt(m);
      localObject4 = ((Uid)localObject4).getWakelockStats();
      n = ((ArrayMap)localObject4).size() - 1;
      while (n >= 0)
      {
        localObject5 = (BatteryStats.Uid.Wakelock)((ArrayMap)localObject4).valueAt(n);
        localObject6 = ((BatteryStats.Uid.Wakelock)localObject5).getWakeTime(1);
        if (localObject6 != null) {
          l6 += ((Timer)localObject6).getTotalTimeLocked(l3, i);
        }
        localObject6 = ((BatteryStats.Uid.Wakelock)localObject5).getWakeTime(0);
        l7 = l5;
        if (localObject6 != null) {
          l7 = l5 + ((Timer)localObject6).getTotalTimeLocked(l3, i);
        }
        n--;
        l5 = l7;
      }
      m++;
    }
    l7 = getNetworkActivityBytes(0, i);
    l10 = getNetworkActivityBytes(1, i);
    l12 = getNetworkActivityBytes(2, i);
    l8 = getNetworkActivityBytes(3, i);
    l9 = getNetworkActivityPackets(0, i);
    Object localObject4 = localObject2;
    dumpLine((PrintWriter)localObject1, 0, (String)localObject3, "gn", new Object[] { Long.valueOf(l7), Long.valueOf(l10), Long.valueOf(l12), Long.valueOf(l8), Long.valueOf(l9), Long.valueOf(getNetworkActivityPackets(1, i)), Long.valueOf(getNetworkActivityPackets(2, i)), Long.valueOf(getNetworkActivityPackets(3, i)), Long.valueOf(getNetworkActivityBytes(4, i)), Long.valueOf(getNetworkActivityBytes(5, i)) });
    localObject2 = getModemControllerActivity();
    m = j;
    j = k;
    l7 = l3;
    l3 = l4;
    dumpControllerActivityLine((PrintWriter)localObject1, 0, (String)localObject3, "gmcd", (ControllerActivityCounter)localObject2, i);
    l4 = getWifiOnTime(l7, i);
    l8 = getGlobalWifiRunningTime(l7, i);
    dumpLine((PrintWriter)localObject1, 0, (String)localObject3, "gwfl", new Object[] { Long.valueOf(l4 / 1000L), Long.valueOf(l8 / 1000L), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) });
    dumpControllerActivityLine((PrintWriter)localObject1, 0, (String)localObject3, "gwfcd", getWifiControllerActivity(), i);
    dumpControllerActivityLine((PrintWriter)localObject1, 0, (String)localObject3, "gble", getBluetoothControllerActivity(), i);
    l4 = l11 / 1000L;
    l19 /= 1000L;
    l6 /= 1000L;
    l11 = l5 / 1000L;
    l8 = getMobileRadioActiveTime(l7, i) / 1000L;
    l5 = getMobileRadioActiveAdjustedTime(i) / 1000L;
    l13 /= 1000L;
    l14 /= 1000L;
    l16 /= 1000L;
    int n = getDeviceIdleModeCount(2, i);
    l9 = l18 / 1000L;
    int i1 = getDeviceIdlingCount(2, i);
    int i2 = getMobileRadioActiveCount(i);
    l18 = getMobileRadioActiveUnknownTime(i) / 1000L;
    l15 /= 1000L;
    k = getDeviceIdleModeCount(1, i);
    l12 = l17 / 1000L;
    int i3 = getDeviceIdlingCount(1, i);
    l17 = getLongestDeviceIdleModeTime(1);
    l10 = getLongestDeviceIdleModeTime(2);
    localObject2 = localObject3;
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "m", new Object[] { Long.valueOf(l4), Long.valueOf(l19), Long.valueOf(l6), Long.valueOf(l11), Long.valueOf(l8), Long.valueOf(l5), Long.valueOf(l13), Long.valueOf(l14), Integer.valueOf(m), Long.valueOf(l16), Integer.valueOf(n), Long.valueOf(l9), Integer.valueOf(i1), Integer.valueOf(i2), Long.valueOf(l18), Long.valueOf(l15), Integer.valueOf(k), Long.valueOf(l12), Integer.valueOf(i3), Long.valueOf(l17), Long.valueOf(l10) });
    Object localObject6 = new Object[5];
    for (k = 0; k < 5; k++) {
      localObject6[k] = Long.valueOf(getScreenBrightnessTime(k, l7, i) / 1000L);
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "br", (Object[])localObject6);
    localObject6 = new Object[6];
    for (k = 0; k < 6; k++) {
      localObject6[k] = Long.valueOf(getPhoneSignalStrengthTime(k, l7, i) / 1000L);
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "sgt", (Object[])localObject6);
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "sst", new Object[] { Long.valueOf(getPhoneSignalScanningTime(l7, i) / 1000L) });
    for (k = 0; k < 6; k++) {
      localObject6[k] = Integer.valueOf(getPhoneSignalStrengthCount(k, i));
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "sgc", (Object[])localObject6);
    localObject6 = new Object[21];
    for (k = 0; k < 21; k++) {
      localObject6[k] = Long.valueOf(getPhoneDataConnectionTime(k, l7, i) / 1000L);
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "dct", (Object[])localObject6);
    for (k = 0; k < 21; k++) {
      localObject6[k] = Integer.valueOf(getPhoneDataConnectionCount(k, i));
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "dcc", (Object[])localObject6);
    localObject6 = new Object[8];
    for (k = 0; k < 8; k++) {
      localObject6[k] = Long.valueOf(getWifiStateTime(k, l7, i) / 1000L);
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "wst", (Object[])localObject6);
    for (k = 0; k < 8; k++) {
      localObject6[k] = Integer.valueOf(getWifiStateCount(k, i));
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "wsc", (Object[])localObject6);
    localObject6 = new Object[13];
    for (k = 0; k < 13; k++) {
      localObject6[k] = Long.valueOf(getWifiSupplStateTime(k, l7, i) / 1000L);
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "wsst", (Object[])localObject6);
    for (k = 0; k < 13; k++) {
      localObject6[k] = Integer.valueOf(getWifiSupplStateCount(k, i));
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "wssc", (Object[])localObject6);
    localObject6 = new Object[5];
    for (k = 0; k < 5; k++) {
      localObject6[k] = Long.valueOf(getWifiSignalStrengthTime(k, l7, i) / 1000L);
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "wsgt", (Object[])localObject6);
    for (k = 0; k < 5; k++) {
      localObject6[k] = Integer.valueOf(getWifiSignalStrengthCount(k, i));
    }
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "wsgc", (Object[])localObject6);
    l5 = getWifiMulticastWakelockTime(l7, i);
    k = getWifiMulticastWakelockCount(i);
    dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "wmct", new Object[] { Long.valueOf(l5 / 1000L), Integer.valueOf(k) });
    if (i == 2) {
      dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "lv", new Object[] { Integer.valueOf(getDischargeStartLevel()), Integer.valueOf(getDischargeCurrentLevel()) });
    }
    if (i == 2) {
      dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "dc", new Object[] { Integer.valueOf(getDischargeStartLevel() - getDischargeCurrentLevel()), Integer.valueOf(getDischargeStartLevel() - getDischargeCurrentLevel()), Integer.valueOf(getDischargeAmountScreenOn()), Integer.valueOf(getDischargeAmountScreenOff()), Long.valueOf(l20 / 1000L), Long.valueOf(l21 / 1000L), Integer.valueOf(getDischargeAmountScreenDoze()), Long.valueOf(l1 / 1000L), Long.valueOf(l22 / 1000L), Long.valueOf(l23 / 1000L) });
    } else {
      dumpLine((PrintWriter)localObject1, 0, (String)localObject2, "dc", new Object[] { Integer.valueOf(getLowDischargeAmountSinceCharge()), Integer.valueOf(getHighDischargeAmountSinceCharge()), Integer.valueOf(getDischargeAmountScreenOnSinceCharge()), Integer.valueOf(getDischargeAmountScreenOffSinceCharge()), Long.valueOf(l20 / 1000L), Long.valueOf(l21 / 1000L), Integer.valueOf(getDischargeAmountScreenDozeSinceCharge()), Long.valueOf(l1 / 1000L), Long.valueOf(l22 / 1000L), Long.valueOf(l23 / 1000L) });
    }
    n = 3;
    Object localObject8;
    if (paramInt2 < 0)
    {
      localObject7 = getKernelWakelockStats();
      localObject6 = localObject7;
      k = m;
      l5 = l3;
      k = n;
      localObject6 = localObject4;
      if (((Map)localObject7).size() > 0)
      {
        localObject5 = ((Map)localObject7).entrySet().iterator();
        localObject2 = localObject4;
        localObject4 = localObject7;
        for (;;)
        {
          localObject6 = localObject4;
          k = m;
          l5 = l3;
          k = n;
          localObject6 = localObject2;
          if (!((Iterator)localObject5).hasNext()) {
            break;
          }
          localObject6 = (Map.Entry)((Iterator)localObject5).next();
          ((StringBuilder)localObject2).setLength(0);
          printWakeLockCheckin((StringBuilder)localObject2, (Timer)((Map.Entry)localObject6).getValue(), l7, null, i, "");
          localObject7 = new StringBuilder();
          ((StringBuilder)localObject7).append("\"");
          ((StringBuilder)localObject7).append((String)((Map.Entry)localObject6).getKey());
          ((StringBuilder)localObject7).append("\"");
          dumpLine((PrintWriter)localObject1, 0, (String)localObject3, "kwl", new Object[] { ((StringBuilder)localObject7).toString(), ((StringBuilder)localObject2).toString() });
        }
      }
      m = k;
      l3 = l2;
      localObject4 = localObject6;
      localObject6 = getWakeupReasonStats();
      l2 = l3;
      if (((Map)localObject6).size() > 0)
      {
        localObject2 = ((Map)localObject6).entrySet().iterator();
        for (;;)
        {
          l2 = l3;
          if (!((Iterator)localObject2).hasNext()) {
            break;
          }
          localObject8 = (Map.Entry)((Iterator)localObject2).next();
          l2 = ((Timer)((Map.Entry)localObject8).getValue()).getTotalTimeLocked(l7, i);
          k = ((Timer)((Map.Entry)localObject8).getValue()).getCountLocked(i);
          localObject5 = new Object[m];
          localObject7 = new StringBuilder();
          ((StringBuilder)localObject7).append("\"");
          ((StringBuilder)localObject7).append((String)((Map.Entry)localObject8).getKey());
          ((StringBuilder)localObject7).append("\"");
          localObject5[0] = ((StringBuilder)localObject7).toString();
          localObject5[1] = Long.valueOf((l2 + 500L) / 1000L);
          localObject5[2] = Integer.valueOf(k);
          dumpLine((PrintWriter)localObject1, 0, (String)localObject3, "wr", (Object[])localObject5);
          m = 3;
        }
      }
      l3 = l5;
    }
    localObject6 = getRpmStats();
    localObject2 = getScreenOffRpmStats();
    Object localObject7 = localObject6;
    localObject7 = localObject2;
    if (((Map)localObject6).size() > 0)
    {
      localObject5 = ((Map)localObject6).entrySet().iterator();
      for (;;)
      {
        localObject7 = localObject6;
        localObject7 = localObject2;
        if (!((Iterator)localObject5).hasNext()) {
          break;
        }
        localObject7 = (Map.Entry)((Iterator)localObject5).next();
        ((StringBuilder)localObject4).setLength(0);
        localObject8 = (Timer)((Map.Entry)localObject7).getValue();
        l5 = (((Timer)localObject8).getTotalTimeLocked(l7, i) + 500L) / 1000L;
        m = ((Timer)localObject8).getCountLocked(i);
        localObject8 = (Timer)((Map)localObject2).get(((Map.Entry)localObject7).getKey());
        if (localObject8 != null) {
          l6 = (((Timer)localObject8).getTotalTimeLocked(l7, i) + 500L) / 1000L;
        }
        if (localObject8 != null) {
          ((Timer)localObject8).getCountLocked(i);
        }
        localObject8 = new StringBuilder();
        ((StringBuilder)localObject8).append("\"");
        ((StringBuilder)localObject8).append((String)((Map.Entry)localObject7).getKey());
        ((StringBuilder)localObject8).append("\"");
        dumpLine((PrintWriter)localObject1, 0, (String)localObject3, "rpm", new Object[] { ((StringBuilder)localObject8).toString(), Long.valueOf(l5), Integer.valueOf(m) });
      }
    }
    localObject6 = new BatteryStatsHelper(paramContext, false, paramBoolean);
    ((BatteryStatsHelper)localObject6).create(this);
    ((BatteryStatsHelper)localObject6).refreshStats(i, -1);
    localObject2 = ((BatteryStatsHelper)localObject6).getUsageList();
    if ((localObject2 != null) && (((List)localObject2).size() > 0))
    {
      dumpLine((PrintWriter)localObject1, 0, (String)localObject3, "pws", new Object[] { BatteryStatsHelper.makemAh(((BatteryStatsHelper)localObject6).getPowerProfile().getBatteryCapacity()), BatteryStatsHelper.makemAh(((BatteryStatsHelper)localObject6).getComputedPower()), BatteryStatsHelper.makemAh(((BatteryStatsHelper)localObject6).getMinDrainedPower()), BatteryStatsHelper.makemAh(((BatteryStatsHelper)localObject6).getMaxDrainedPower()) });
      m = 0;
      for (k = 0; k < ((List)localObject2).size(); k++)
      {
        localObject5 = (BatterySipper)((List)localObject2).get(k);
        switch (2.$SwitchMap$com$android$internal$os$BatterySipper$DrainType[drainType.ordinal()])
        {
        default: 
          paramContext = "???";
          break;
        case 14: 
          paramContext = "memory";
          break;
        case 13: 
          paramContext = "camera";
          break;
        case 12: 
          paramContext = "over";
          break;
        case 11: 
          paramContext = "unacc";
          break;
        case 10: 
          m = UserHandle.getUid(userId, 0);
          paramContext = "user";
          break;
        case 9: 
          m = uidObj.getUid();
          paramContext = "uid";
          break;
        case 8: 
          paramContext = "flashlight";
          break;
        case 7: 
          paramContext = "scrn";
          break;
        case 6: 
          paramContext = "blue";
          break;
        case 5: 
          paramContext = "wifi";
          break;
        case 4: 
          paramContext = "phone";
          break;
        case 3: 
          paramContext = "cell";
          break;
        case 2: 
          paramContext = "idle";
          break;
        case 1: 
          paramContext = "ambi";
        }
        dumpLine((PrintWriter)localObject1, m, (String)localObject3, "pwi", new Object[] { paramContext, BatteryStatsHelper.makemAh(totalPowerMah), Integer.valueOf(shouldHide), BatteryStatsHelper.makemAh(screenPowerMah), BatteryStatsHelper.makemAh(proportionalSmearMah) });
      }
      paramContext = (Context)localObject2;
    }
    else
    {
      paramContext = (Context)localObject2;
    }
    localObject6 = getCpuFreqs();
    if (localObject6 != null)
    {
      localObject5 = localObject4;
      ((StringBuilder)localObject5).setLength(0);
      for (m = 0; m < localObject6.length; m++)
      {
        localObject7 = new StringBuilder();
        if (m == 0) {
          localObject2 = "";
        } else {
          localObject2 = ",";
        }
        ((StringBuilder)localObject7).append((String)localObject2);
        ((StringBuilder)localObject7).append(localObject6[m]);
        ((StringBuilder)localObject5).append(((StringBuilder)localObject7).toString());
      }
      dumpLine((PrintWriter)localObject1, 0, (String)localObject3, "gcf", new Object[] { ((StringBuilder)localObject5).toString() });
    }
    localObject2 = localObject4;
    n = 0;
    Object localObject5 = paramContext;
    k = j;
    localObject4 = localObject6;
    l5 = l7;
    m = i;
    localObject6 = localObject1;
    paramContext = (Context)localObject3;
    while (n < k)
    {
      i = localSparseArray.keyAt(n);
      if ((paramInt2 >= 0) && (i != paramInt2))
      {
        localObject1 = localObject2;
        l7 = l5;
        localObject2 = localObject6;
        localObject6 = paramContext;
        l5 = l3;
        l3 = l2;
        l2 = l7;
        paramContext = (Context)localObject1;
      }
      else
      {
        localObject1 = (Uid)localSparseArray.valueAt(n);
        l4 = ((Uid)localObject1).getNetworkActivityBytes(0, m);
        l16 = ((Uid)localObject1).getNetworkActivityBytes(1, m);
        localObject6 = localObject2;
        l1 = ((Uid)localObject1).getNetworkActivityBytes(2, m);
        l10 = ((Uid)localObject1).getNetworkActivityBytes(3, m);
        l15 = ((Uid)localObject1).getNetworkActivityPackets(0, m);
        l22 = ((Uid)localObject1).getNetworkActivityPackets(1, m);
        l13 = ((Uid)localObject1).getMobileRadioActiveTime(m);
        j = ((Uid)localObject1).getMobileRadioActiveCount(m);
        l21 = ((Uid)localObject1).getMobileRadioApWakeupCount(m);
        l6 = ((Uid)localObject1).getNetworkActivityPackets(2, m);
        l19 = ((Uid)localObject1).getNetworkActivityPackets(3, m);
        l12 = ((Uid)localObject1).getWifiRadioApWakeupCount(m);
        l7 = ((Uid)localObject1).getNetworkActivityBytes(4, m);
        l18 = ((Uid)localObject1).getNetworkActivityBytes(5, m);
        l9 = ((Uid)localObject1).getNetworkActivityBytes(6, m);
        l8 = ((Uid)localObject1).getNetworkActivityBytes(7, m);
        l14 = ((Uid)localObject1).getNetworkActivityBytes(8, m);
        l17 = ((Uid)localObject1).getNetworkActivityBytes(9, m);
        l23 = ((Uid)localObject1).getNetworkActivityPackets(6, m);
        long l24 = ((Uid)localObject1).getNetworkActivityPackets(7, m);
        l11 = ((Uid)localObject1).getNetworkActivityPackets(8, m);
        l20 = ((Uid)localObject1).getNetworkActivityPackets(9, m);
        if ((l4 <= 0L) && (l16 <= 0L) && (l1 <= 0L) && (l10 <= 0L) && (l15 <= 0L) && (l22 <= 0L) && (l6 <= 0L) && (l19 <= 0L) && (l13 <= 0L) && (j <= 0) && (l7 <= 0L) && (l18 <= 0L) && (l21 <= 0L) && (l12 <= 0L) && (l9 <= 0L) && (l8 <= 0L) && (l14 <= 0L) && (l17 <= 0L) && (l23 <= 0L) && (l24 <= 0L) && (l11 <= 0L) && (l20 <= 0L)) {
          break label4193;
        }
        dumpLine(paramPrintWriter, i, paramContext, "nt", new Object[] { Long.valueOf(l4), Long.valueOf(l16), Long.valueOf(l1), Long.valueOf(l10), Long.valueOf(l15), Long.valueOf(l22), Long.valueOf(l6), Long.valueOf(l19), Long.valueOf(l13), Integer.valueOf(j), Long.valueOf(l7), Long.valueOf(l18), Long.valueOf(l21), Long.valueOf(l12), Long.valueOf(l9), Long.valueOf(l8), Long.valueOf(l14), Long.valueOf(l17), Long.valueOf(l23), Long.valueOf(l24), Long.valueOf(l11), Long.valueOf(l20) });
        label4193:
        dumpControllerActivityLine(paramPrintWriter, i, paramContext, "mcd", ((Uid)localObject1).getModemControllerActivity(), paramInt1);
        l4 = ((Uid)localObject1).getFullWifiLockTime(l5, paramInt1);
        l6 = ((Uid)localObject1).getWifiScanTime(l5, paramInt1);
        m = ((Uid)localObject1).getWifiScanCount(paramInt1);
        j = ((Uid)localObject1).getWifiScanBackgroundCount(paramInt1);
        l21 = (((Uid)localObject1).getWifiScanActualTime(l5) + 500L) / 1000L;
        l7 = (((Uid)localObject1).getWifiScanBackgroundTime(l5) + 500L) / 1000L;
        l1 = ((Uid)localObject1).getWifiRunningTime(l5, paramInt1);
        if ((l4 == 0L) && (l6 == 0L) && (m == 0) && (j == 0) && (l21 == 0L) && (l7 == 0L) && (l1 == 0L)) {
          break label4435;
        }
        dumpLine(paramPrintWriter, i, paramContext, "wfl", new Object[] { Long.valueOf(l4), Long.valueOf(l6), Long.valueOf(l1), Integer.valueOf(m), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(j), Long.valueOf(l21), Long.valueOf(l7) });
        label4435:
        localObject2 = paramPrintWriter;
        localObject7 = paramContext;
        paramContext = ((Uid)localObject1).getWifiControllerActivity();
        i1 = paramInt1;
        dumpControllerActivityLine((PrintWriter)localObject2, i, (String)localObject7, "wfcd", paramContext, i1);
        localObject8 = ((Uid)localObject1).getBluetoothScanTimer();
        if (localObject8 != null)
        {
          l22 = (((Timer)localObject8).getTotalTimeLocked(l5, i1) + 500L) / 1000L;
          if (l22 != 0L)
          {
            i3 = ((Timer)localObject8).getCountLocked(i1);
            paramContext = ((Uid)localObject1).getBluetoothScanBackgroundTimer();
            if (paramContext != null) {
              m = paramContext.getCountLocked(i1);
            } else {
              m = 0;
            }
            l21 = l2;
            l20 = ((Timer)localObject8).getTotalDurationMsLocked(l21);
            if (paramContext != null) {
              l7 = paramContext.getTotalDurationMsLocked(l21);
            } else {
              l7 = 0L;
            }
            if (((Uid)localObject1).getBluetoothScanResultCounter() != null) {
              j = ((Uid)localObject1).getBluetoothScanResultCounter().getCountLocked(i1);
            } else {
              j = 0;
            }
            if (((Uid)localObject1).getBluetoothScanResultBgCounter() != null) {
              i2 = ((Uid)localObject1).getBluetoothScanResultBgCounter().getCountLocked(i1);
            } else {
              i2 = 0;
            }
            paramContext = ((Uid)localObject1).getBluetoothUnoptimizedScanTimer();
            if (paramContext != null) {
              l6 = paramContext.getTotalDurationMsLocked(l21);
            } else {
              l6 = 0L;
            }
            if (paramContext != null) {
              l4 = paramContext.getMaxDurationMsLocked(l21);
            } else {
              l4 = 0L;
            }
            paramContext = ((Uid)localObject1).getBluetoothUnoptimizedScanBackgroundTimer();
            if (paramContext != null) {
              l1 = paramContext.getTotalDurationMsLocked(l21);
            } else {
              l1 = 0L;
            }
            if (paramContext != null) {
              l21 = paramContext.getMaxDurationMsLocked(l21);
            } else {
              l21 = 0L;
            }
            dumpLine((PrintWriter)localObject2, i, (String)localObject7, "blem", new Object[] { Long.valueOf(l22), Integer.valueOf(i3), Integer.valueOf(m), Long.valueOf(l20), Long.valueOf(l7), Integer.valueOf(j), Integer.valueOf(i2), Long.valueOf(l6), Long.valueOf(l1), Long.valueOf(l4), Long.valueOf(l21) });
          }
          else {}
        }
        paramContext = ((Uid)localObject1).getBluetoothControllerActivity();
        dumpControllerActivityLine((PrintWriter)localObject2, i, (String)localObject7, "ble", paramContext, i1);
        if (((Uid)localObject1).hasUserActivity())
        {
          paramContext = new Object[4];
          j = 0;
          for (m = 0; m < 4; m++)
          {
            i2 = ((Uid)localObject1).getUserActivityCount(m, i1);
            paramContext[m] = Integer.valueOf(i2);
            if (i2 != 0) {
              j = 1;
            }
          }
          if (j != 0) {
            dumpLine((PrintWriter)localObject2, i, (String)localObject7, "ua", paramContext);
          }
        }
        if (((Uid)localObject1).getAggregatedPartialWakelockTimer() != null)
        {
          paramContext = ((Uid)localObject1).getAggregatedPartialWakelockTimer();
          l6 = paramContext.getTotalDurationMsLocked(l2);
          paramContext = paramContext.getSubTimer();
          if (paramContext != null) {
            l7 = paramContext.getTotalDurationMsLocked(l2);
          } else {
            l7 = 0L;
          }
          dumpLine((PrintWriter)localObject2, i, (String)localObject7, "awl", new Object[] { Long.valueOf(l6), Long.valueOf(l7) });
        }
        Object localObject9 = ((Uid)localObject1).getWakelockStats();
        m = ((ArrayMap)localObject9).size() - 1;
        paramContext = (Context)localObject6;
        localObject6 = localObject1;
        Object localObject10;
        while (m >= 0)
        {
          localObject3 = (BatteryStats.Uid.Wakelock)((ArrayMap)localObject9).valueAt(m);
          paramContext.setLength(0);
          localObject10 = printWakeLockCheckin(paramContext, ((BatteryStats.Uid.Wakelock)localObject3).getWakeTime(1), l5, "f", i1, "");
          localObject1 = ((BatteryStats.Uid.Wakelock)localObject3).getWakeTime(0);
          localObject10 = printWakeLockCheckin(paramContext, (Timer)localObject1, l5, "p", i1, (String)localObject10);
          if (localObject1 != null) {}
          for (localObject1 = ((Timer)localObject1).getSubTimer();; localObject1 = null) {
            break;
          }
          localObject1 = printWakeLockCheckin(paramContext, (Timer)localObject1, l5, "bp", i1, (String)localObject10);
          printWakeLockCheckin(paramContext, ((BatteryStats.Uid.Wakelock)localObject3).getWakeTime(2), l5, "w", i1, (String)localObject1);
          if (paramContext.length() > 0)
          {
            localObject3 = (String)((ArrayMap)localObject9).keyAt(m);
            localObject1 = localObject3;
            if (((String)localObject3).indexOf(',') >= 0) {
              localObject1 = ((String)localObject3).replace(',', '_');
            }
            localObject3 = localObject1;
            if (((String)localObject1).indexOf('\n') >= 0) {
              localObject3 = ((String)localObject1).replace('\n', '_');
            }
            localObject1 = localObject3;
            if (((String)localObject3).indexOf('\r') >= 0) {
              localObject1 = ((String)localObject3).replace('\r', '_');
            }
            dumpLine((PrintWriter)localObject2, i, (String)localObject7, "wl", new Object[] { localObject1, paramContext.toString() });
          }
          m--;
        }
        localObject3 = localObject2;
        localObject1 = ((Uid)localObject6).getMulticastWakelockStats();
        if (localObject1 != null)
        {
          l7 = ((Timer)localObject1).getTotalTimeLocked(l5, i1) / 1000L;
          m = ((Timer)localObject1).getCountLocked(i1);
          if (l7 > 0L) {
            dumpLine((PrintWriter)localObject3, i, (String)localObject7, "wmc", new Object[] { Long.valueOf(l7), Integer.valueOf(m) });
          } else {}
        }
        localObject8 = ((Uid)localObject6).getSyncStats();
        m = ((ArrayMap)localObject8).size() - 1;
        localObject2 = localObject9;
        while (m >= 0)
        {
          localObject9 = (Timer)((ArrayMap)localObject8).valueAt(m);
          l6 = (((Timer)localObject9).getTotalTimeLocked(l5, i1) + 500L) / 1000L;
          i2 = ((Timer)localObject9).getCountLocked(i1);
          localObject9 = ((Timer)localObject9).getSubTimer();
          if (localObject9 != null) {
            l7 = ((Timer)localObject9).getTotalDurationMsLocked(l2);
          } else {
            l7 = -1L;
          }
          if (localObject9 != null) {
            j = ((Timer)localObject9).getCountLocked(i1);
          } else {
            j = -1;
          }
          if (l6 != 0L)
          {
            localObject9 = new StringBuilder();
            ((StringBuilder)localObject9).append("\"");
            ((StringBuilder)localObject9).append((String)((ArrayMap)localObject8).keyAt(m));
            ((StringBuilder)localObject9).append("\"");
            dumpLine((PrintWriter)localObject3, i, (String)localObject7, "sy", new Object[] { ((StringBuilder)localObject9).toString(), Long.valueOf(l6), Integer.valueOf(i2), Long.valueOf(l7), Integer.valueOf(j) });
          }
          m--;
        }
        localObject1 = ((Uid)localObject6).getJobStats();
        m = ((ArrayMap)localObject1).size() - 1;
        localObject2 = localObject8;
        while (m >= 0)
        {
          localObject8 = (Timer)((ArrayMap)localObject1).valueAt(m);
          l6 = (((Timer)localObject8).getTotalTimeLocked(l5, i1) + 500L) / 1000L;
          i2 = ((Timer)localObject8).getCountLocked(i1);
          localObject8 = ((Timer)localObject8).getSubTimer();
          if (localObject8 != null) {
            l7 = ((Timer)localObject8).getTotalDurationMsLocked(l2);
          } else {
            l7 = -1L;
          }
          if (localObject8 != null) {
            j = ((Timer)localObject8).getCountLocked(i1);
          } else {
            j = -1;
          }
          if (l6 != 0L)
          {
            localObject8 = new StringBuilder();
            ((StringBuilder)localObject8).append("\"");
            ((StringBuilder)localObject8).append((String)((ArrayMap)localObject1).keyAt(m));
            ((StringBuilder)localObject8).append("\"");
            dumpLine((PrintWriter)localObject3, i, (String)localObject7, "jb", new Object[] { ((StringBuilder)localObject8).toString(), Long.valueOf(l6), Integer.valueOf(i2), Long.valueOf(l7), Integer.valueOf(j) });
          }
          m--;
        }
        localObject1 = ((Uid)localObject6).getJobCompletionStats();
        for (m = ((ArrayMap)localObject1).size() - 1; m >= 0; m--)
        {
          localObject8 = (SparseIntArray)((ArrayMap)localObject1).valueAt(m);
          if (localObject8 != null)
          {
            localObject2 = new StringBuilder();
            ((StringBuilder)localObject2).append("\"");
            ((StringBuilder)localObject2).append((String)((ArrayMap)localObject1).keyAt(m));
            ((StringBuilder)localObject2).append("\"");
            dumpLine((PrintWriter)localObject3, i, (String)localObject7, "jbc", new Object[] { ((StringBuilder)localObject2).toString(), Integer.valueOf(((SparseIntArray)localObject8).get(0, 0)), Integer.valueOf(((SparseIntArray)localObject8).get(1, 0)), Integer.valueOf(((SparseIntArray)localObject8).get(2, 0)), Integer.valueOf(((SparseIntArray)localObject8).get(3, 0)), Integer.valueOf(((SparseIntArray)localObject8).get(4, 0)) });
          }
        }
        ((Uid)localObject6).getDeferredJobsCheckinLineLocked(paramContext, i1);
        if (paramContext.length() > 0) {
          dumpLine((PrintWriter)localObject3, i, (String)localObject7, "jbd", new Object[] { paramContext.toString() });
        }
        localObject2 = ((Uid)localObject6).getFlashlightTurnedOnTimer();
        l7 = l2;
        localObject1 = localObject7;
        dumpTimer((PrintWriter)localObject3, i, (String)localObject7, "fla", (Timer)localObject2, l5, i1);
        dumpTimer((PrintWriter)localObject3, i, (String)localObject1, "cam", ((Uid)localObject6).getCameraTurnedOnTimer(), l5, i1);
        dumpTimer((PrintWriter)localObject3, i, (String)localObject1, "vid", ((Uid)localObject6).getVideoTurnedOnTimer(), l5, i1);
        dumpTimer((PrintWriter)localObject3, i, (String)localObject1, "aud", ((Uid)localObject6).getAudioTurnedOnTimer(), l5, i1);
        localObject2 = ((Uid)localObject6).getSensorStats();
        m = ((SparseArray)localObject2).size();
        j = 0;
        l2 = l5;
        l5 = l7;
        while (j < m)
        {
          localObject8 = (BatteryStats.Uid.Sensor)((SparseArray)localObject2).valueAt(j);
          i3 = ((SparseArray)localObject2).keyAt(j);
          localObject7 = ((BatteryStats.Uid.Sensor)localObject8).getSensorTime();
          if (localObject7 != null)
          {
            l4 = (((Timer)localObject7).getTotalTimeLocked(l2, i1) + 500L) / 1000L;
            if (l4 != 0L)
            {
              int i4 = ((Timer)localObject7).getCountLocked(i1);
              localObject8 = ((BatteryStats.Uid.Sensor)localObject8).getSensorBackgroundTime();
              if (localObject8 != null) {
                i2 = ((Timer)localObject8).getCountLocked(i1);
              } else {
                i2 = 0;
              }
              l6 = ((Timer)localObject7).getTotalDurationMsLocked(l5);
              if (localObject8 != null) {
                l7 = ((Timer)localObject8).getTotalDurationMsLocked(l5);
              } else {
                l7 = 0L;
              }
              dumpLine((PrintWriter)localObject3, i, (String)localObject1, "sr", new Object[] { Integer.valueOf(i3), Long.valueOf(l4), Integer.valueOf(i4), Integer.valueOf(i2), Long.valueOf(l6), Long.valueOf(l7) });
            }
            else {}
          }
          j++;
        }
        localObject7 = ((Uid)localObject6).getVibratorOnTimer();
        dumpTimer((PrintWriter)localObject3, i, (String)localObject1, "vib", (Timer)localObject7, l2, i1);
        dumpTimer((PrintWriter)localObject3, i, (String)localObject1, "fg", ((Uid)localObject6).getForegroundActivityTimer(), l2, i1);
        dumpTimer((PrintWriter)localObject3, i, (String)localObject1, "fgs", ((Uid)localObject6).getForegroundServiceTimer(), l2, i1);
        localObject7 = new Object[7];
        l7 = 0L;
        j = 0;
        while (j < 7)
        {
          l6 = ((Uid)localObject6).getProcessStateTime(j, l2, i1);
          localObject7[j] = Long.valueOf((l6 + 500L) / 1000L);
          j++;
          l7 += l6;
        }
        if (l7 > 0L) {
          dumpLine((PrintWriter)localObject3, i, (String)localObject1, "st", (Object[])localObject7);
        }
        l7 = ((Uid)localObject6).getUserCpuTimeUs(i1);
        l6 = ((Uid)localObject6).getSystemCpuTimeUs(i1);
        if ((l7 <= 0L) && (l6 <= 0L)) {
          break label6593;
        }
        dumpLine((PrintWriter)localObject3, i, (String)localObject1, "cpu", new Object[] { Long.valueOf(l7 / 1000L), Long.valueOf(l6 / 1000L), Integer.valueOf(0) });
        label6593:
        if (localObject4 != null)
        {
          localObject9 = ((Uid)localObject6).getCpuFreqTimes(i1);
          if (localObject9 != null) {
            if (localObject9.length == localObject4.length)
            {
              localObject8 = paramContext;
              ((StringBuilder)localObject8).setLength(0);
              for (j = 0; j < localObject9.length; j++)
              {
                localObject10 = new StringBuilder();
                if (j == 0) {
                  localObject7 = "";
                } else {
                  localObject7 = ",";
                }
                ((StringBuilder)localObject10).append((String)localObject7);
                ((StringBuilder)localObject10).append(localObject9[j]);
                ((StringBuilder)localObject8).append(((StringBuilder)localObject10).toString());
              }
              localObject7 = ((Uid)localObject6).getScreenOffCpuFreqTimes(i1);
              if (localObject7 != null) {
                for (j = 0; j < localObject7.length; j++)
                {
                  localObject10 = new StringBuilder();
                  ((StringBuilder)localObject10).append(",");
                  ((StringBuilder)localObject10).append(localObject7[j]);
                  ((StringBuilder)localObject8).append(((StringBuilder)localObject10).toString());
                }
              }
              for (j = 0; j < localObject9.length; j++) {
                ((StringBuilder)localObject8).append(",0");
              }
              dumpLine((PrintWriter)localObject3, i, (String)localObject1, "ctf", new Object[] { "A", Integer.valueOf(localObject9.length), ((StringBuilder)localObject8).toString() });
            }
            else {}
          }
          localObject8 = paramContext;
          for (j = 0; j < 7; j++)
          {
            localObject9 = ((Uid)localObject6).getCpuFreqTimes(i1, j);
            if ((localObject9 != null) && (localObject9.length == localObject4.length))
            {
              ((StringBuilder)localObject8).setLength(0);
              for (i2 = 0; i2 < localObject9.length; i2++)
              {
                localObject10 = new StringBuilder();
                if (i2 == 0) {
                  localObject7 = "";
                } else {
                  localObject7 = ",";
                }
                ((StringBuilder)localObject10).append((String)localObject7);
                ((StringBuilder)localObject10).append(localObject9[i2]);
                ((StringBuilder)localObject8).append(((StringBuilder)localObject10).toString());
              }
              localObject10 = ((Uid)localObject6).getScreenOffCpuFreqTimes(i1, j);
              if (localObject10 != null) {
                for (i2 = 0; i2 < localObject10.length; i2++)
                {
                  localObject7 = new StringBuilder();
                  ((StringBuilder)localObject7).append(",");
                  ((StringBuilder)localObject7).append(localObject10[i2]);
                  ((StringBuilder)localObject8).append(((StringBuilder)localObject7).toString());
                }
              }
              for (i2 = 0; i2 < localObject9.length; i2++) {
                ((StringBuilder)localObject8).append(",0");
              }
              dumpLine((PrintWriter)localObject3, i, (String)localObject1, "ctf", new Object[] { Uid.UID_PROCESS_TYPES[j], Integer.valueOf(localObject9.length), ((StringBuilder)localObject8).toString() });
            }
          }
        }
        localObject2 = localObject4;
        localObject4 = paramContext;
        localObject7 = ((Uid)localObject6).getProcessStats();
        j = ((ArrayMap)localObject7).size() - 1;
        paramContext = (Context)localObject2;
        for (;;)
        {
          i2 = paramInt1;
          if (j < 0) {
            break;
          }
          localObject2 = (BatteryStats.Uid.Proc)((ArrayMap)localObject7).valueAt(j);
          l4 = ((BatteryStats.Uid.Proc)localObject2).getUserTime(i2);
          l6 = ((BatteryStats.Uid.Proc)localObject2).getSystemTime(i2);
          l7 = ((BatteryStats.Uid.Proc)localObject2).getForegroundTime(i2);
          i1 = ((BatteryStats.Uid.Proc)localObject2).getStarts(i2);
          i3 = ((BatteryStats.Uid.Proc)localObject2).getNumCrashes(i2);
          i2 = ((BatteryStats.Uid.Proc)localObject2).getNumAnrs(i2);
          if ((l4 == 0L) && (l6 == 0L) && (l7 == 0L) && (i1 == 0) && (i2 == 0) && (i3 == 0)) {
            break label7374;
          }
          localObject2 = new StringBuilder();
          ((StringBuilder)localObject2).append("\"");
          ((StringBuilder)localObject2).append((String)((ArrayMap)localObject7).keyAt(j));
          ((StringBuilder)localObject2).append("\"");
          dumpLine((PrintWriter)localObject3, i, (String)localObject1, "pr", new Object[] { ((StringBuilder)localObject2).toString(), Long.valueOf(l4), Long.valueOf(l6), Long.valueOf(l7), Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(i3) });
          label7374:
          j--;
        }
        localObject2 = paramContext;
        paramContext = (Context)localObject6;
        localObject9 = paramContext.getPackageStats();
        for (m = ((ArrayMap)localObject9).size() - 1; m >= 0; m--)
        {
          localObject8 = (BatteryStats.Uid.Pkg)((ArrayMap)localObject9).valueAt(m);
          j = 0;
          localObject6 = ((BatteryStats.Uid.Pkg)localObject8).getWakeupAlarmStats();
          for (i2 = ((ArrayMap)localObject6).size() - 1; i2 >= 0; i2--)
          {
            i1 = ((Counter)((ArrayMap)localObject6).valueAt(i2)).getCountLocked(paramInt1);
            j += i1;
            dumpLine((PrintWriter)localObject3, i, (String)localObject1, "wua", new Object[] { ((String)((ArrayMap)localObject6).keyAt(i2)).replace(',', '_'), Integer.valueOf(i1) });
          }
          localObject10 = ((BatteryStats.Uid.Pkg)localObject8).getServiceStats();
          for (i2 = ((ArrayMap)localObject10).size() - 1; i2 >= 0; i2--)
          {
            BatteryStats.Uid.Pkg.Serv localServ = (BatteryStats.Uid.Pkg.Serv)((ArrayMap)localObject10).valueAt(i2);
            l7 = localServ.getStartTime(l3, paramInt1);
            i3 = localServ.getStarts(paramInt1);
            i1 = localServ.getLaunches(paramInt1);
            if ((l7 == 0L) && (i3 == 0) && (i1 == 0)) {
              continue;
            }
            dumpLine((PrintWriter)localObject3, i, (String)localObject1, "apk", new Object[] { Integer.valueOf(j), ((ArrayMap)localObject9).keyAt(m), ((ArrayMap)localObject10).keyAt(i2), Long.valueOf(l7 / 1000L), Integer.valueOf(i3), Integer.valueOf(i1) });
          }
        }
        l7 = l3;
        m = paramInt1;
        paramContext = (Context)localObject4;
        localObject4 = localObject2;
        l3 = l5;
        l5 = l7;
        localObject2 = localObject3;
        localObject6 = localObject1;
      }
      n++;
      localObject1 = localObject6;
      localObject6 = localObject2;
      l7 = l5;
      l6 = l3;
      localObject2 = paramContext;
      paramContext = (Context)localObject1;
      l5 = l2;
      l3 = l7;
      l2 = l6;
    }
  }
  
  public void dumpCheckinLocked(Context paramContext, PrintWriter paramPrintWriter, List<ApplicationInfo> paramList, int paramInt, long paramLong)
  {
    prepareForDumpLocked();
    boolean bool = false;
    dumpLine(paramPrintWriter, 0, "i", "vers", new Object[] { Integer.valueOf(32), Integer.valueOf(getParcelVersion()), getStartPlatformVersion(), getEndPlatformVersion() });
    getHistoryBaseTime();
    SystemClock.elapsedRealtime();
    int i;
    if (((paramInt & 0x18) != 0) && (startIteratingHistoryLocked()))
    {
      i = 0;
      try
      {
        while (i < getHistoryStringPoolSize())
        {
          paramPrintWriter.print(9);
          paramPrintWriter.print(',');
          paramPrintWriter.print("hsp");
          paramPrintWriter.print(',');
          paramPrintWriter.print(i);
          paramPrintWriter.print(",");
          paramPrintWriter.print(getHistoryTagPoolUid(i));
          paramPrintWriter.print(",\"");
          paramPrintWriter.print(getHistoryTagPoolString(i).replace("\\", "\\\\").replace("\"", "\\\""));
          paramPrintWriter.print("\"");
          paramPrintWriter.println();
          i++;
        }
        dumpHistoryLocked(paramPrintWriter, paramInt, paramLong, true);
      }
      finally
      {
        finishIteratingHistoryLocked();
      }
    }
    if ((paramInt & 0x8) != 0) {
      return;
    }
    if (paramList != null)
    {
      Object localObject1 = new SparseArray();
      Object localObject3;
      for (i = 0; i < paramList.size(); i++)
      {
        localObject2 = (ApplicationInfo)paramList.get(i);
        localObject3 = (Pair)((SparseArray)localObject1).get(UserHandle.getAppId(uid));
        localObject4 = localObject3;
        if (localObject3 == null)
        {
          localObject4 = new Pair(new ArrayList(), new MutableBoolean(false));
          ((SparseArray)localObject1).put(UserHandle.getAppId(uid), localObject4);
        }
        ((ArrayList)first).add(packageName);
      }
      Object localObject4 = getUidStats();
      int j = ((SparseArray)localObject4).size();
      Object localObject2 = new String[2];
      i = 0;
      paramList = (List<ApplicationInfo>)localObject1;
      while (i < j)
      {
        int k = UserHandle.getAppId(((SparseArray)localObject4).keyAt(i));
        Pair localPair = (Pair)paramList.get(k);
        localObject3 = paramList;
        localObject1 = localObject4;
        if (localPair != null)
        {
          localObject3 = paramList;
          localObject1 = localObject4;
          if (!second).value)
          {
            second).value = true;
            for (int m = 0;; m++)
            {
              localObject3 = paramList;
              localObject1 = localObject4;
              if (m >= ((ArrayList)first).size()) {
                break;
              }
              localObject2[0] = Integer.toString(k);
              localObject2[1] = ((String)((ArrayList)first).get(m));
              dumpLine(paramPrintWriter, 0, "i", "uid", (Object[])localObject2);
            }
          }
        }
        i++;
        paramList = (List<ApplicationInfo>)localObject3;
        localObject4 = localObject1;
      }
    }
    if ((paramInt & 0x4) == 0)
    {
      dumpDurationSteps(paramPrintWriter, "", "dsd", getDischargeLevelStepTracker(), true);
      paramList = new String[1];
      paramLong = computeBatteryTimeRemaining(SystemClock.elapsedRealtime() * 1000L);
      if (paramLong >= 0L)
      {
        paramList[0] = Long.toString(paramLong);
        dumpLine(paramPrintWriter, 0, "i", "dtr", (Object[])paramList);
      }
      dumpDurationSteps(paramPrintWriter, "", "csd", getChargeLevelStepTracker(), true);
      paramLong = computeChargeTimeRemaining(SystemClock.elapsedRealtime() * 1000L);
      if (paramLong >= 0L)
      {
        paramList[0] = Long.toString(paramLong);
        dumpLine(paramPrintWriter, 0, "i", "ctr", (Object[])paramList);
      }
      if ((paramInt & 0x40) != 0) {
        bool = true;
      }
      dumpCheckinLocked(paramContext, paramPrintWriter, 0, -1, bool);
    }
  }
  
  public void dumpLocked(Context paramContext, PrintWriter paramPrintWriter, int paramInt1, int paramInt2, long paramLong)
  {
    prepareForDumpLocked();
    int i;
    if ((paramInt1 & 0xE) != 0) {
      i = 1;
    } else {
      i = 0;
    }
    long l1;
    long l2;
    if (((paramInt1 & 0x8) != 0) || (i == 0))
    {
      l1 = getHistoryTotalSize();
      l2 = getHistoryUsedSize();
      if (startIteratingHistoryLocked()) {}
      try
      {
        paramPrintWriter.print("Battery History (");
        paramPrintWriter.print(100L * l2 / l1);
        paramPrintWriter.print("% used, ");
        printSizeValue(paramPrintWriter, l2);
        paramPrintWriter.print(" used of ");
        printSizeValue(paramPrintWriter, l1);
        paramPrintWriter.print(", ");
        paramPrintWriter.print(getHistoryStringPoolSize());
        paramPrintWriter.print(" strings using ");
        printSizeValue(paramPrintWriter, getHistoryStringPoolBytes());
        paramPrintWriter.println("):");
        try
        {
          label165:
          localObject1 = new android/os/BatteryStats$HistoryItem;
          ((HistoryItem)localObject1).<init>();
          paramPrintWriter.println("Old battery History:");
          localObject2 = new android/os/BatteryStats$HistoryPrinter;
          ((HistoryPrinter)localObject2).<init>();
          for (l2 = -1L; getNextOldHistoryLocked((HistoryItem)localObject1); l2 = paramLong)
          {
            paramLong = l2;
            if (l2 < 0L) {
              paramLong = time;
            }
            if ((paramInt1 & 0x20) != 0) {
              bool = true;
            } else {
              bool = false;
            }
            ((HistoryPrinter)localObject2).printNextItem(paramPrintWriter, (HistoryItem)localObject1, paramLong, false, bool);
          }
          paramPrintWriter.println();
        }
        finally
        {
          finishIteratingOldHistoryLocked();
        }
      }
      finally
      {
        try
        {
          dumpHistoryLocked(paramPrintWriter, paramInt1, paramLong, false);
          paramPrintWriter.println();
          finishIteratingHistoryLocked();
        }
        finally
        {
          break label165;
          paramContext = finally;
          finishIteratingHistoryLocked();
        }
      }
    }
    boolean bool;
    if ((i != 0) && ((paramInt1 & 0x6) == 0)) {
      return;
    }
    if (i == 0)
    {
      localObject1 = getUidStats();
      int j = ((SparseArray)localObject1).size();
      l1 = SystemClock.elapsedRealtime();
      k = 0;
      m = 0;
      while (m < j)
      {
        localObject2 = ((Uid)((SparseArray)localObject1).valueAt(m)).getPidStats();
        int n = k;
        if (localObject2 != null)
        {
          int i1 = 0;
          while (i1 < ((SparseArray)localObject2).size())
          {
            localObject3 = (BatteryStats.Uid.Pid)((SparseArray)localObject2).valueAt(i1);
            n = k;
            if (k == 0)
            {
              paramPrintWriter.println("Per-PID Stats:");
              n = 1;
            }
            l2 = mWakeSumMs;
            if (mWakeNesting > 0) {
              paramLong = l1 - mWakeStartMs;
            } else {
              paramLong = 0L;
            }
            paramPrintWriter.print("  PID ");
            paramPrintWriter.print(((SparseArray)localObject2).keyAt(i1));
            paramPrintWriter.print(" wake time: ");
            TimeUtils.formatDuration(l2 + paramLong, paramPrintWriter);
            paramPrintWriter.println("");
            i1++;
            k = n;
          }
          n = k;
        }
        m++;
        k = n;
      }
      if (k != 0) {
        paramPrintWriter.println();
      }
    }
    if ((i != 0) && ((paramInt1 & 0x2) == 0)) {
      break label717;
    }
    if (dumpDurationSteps(paramPrintWriter, "  ", "Discharge step durations:", getDischargeLevelStepTracker(), false))
    {
      paramLong = computeBatteryTimeRemaining(SystemClock.elapsedRealtime() * 1000L);
      if (paramLong >= 0L)
      {
        paramPrintWriter.print("  Estimated discharge time remaining: ");
        TimeUtils.formatDuration(paramLong / 1000L, paramPrintWriter);
        paramPrintWriter.println();
      }
      localObject1 = getDischargeLevelStepTracker();
      for (k = 0; k < STEP_LEVEL_MODES_OF_INTEREST.length; k++) {
        dumpTimeEstimate(paramPrintWriter, "  Estimated ", STEP_LEVEL_MODE_LABELS[k], " time: ", ((LevelStepTracker)localObject1).computeTimeEstimate(STEP_LEVEL_MODES_OF_INTEREST[k], STEP_LEVEL_MODE_VALUES[k], null));
      }
      paramPrintWriter.println();
    }
    if (dumpDurationSteps(paramPrintWriter, "  ", "Charge step durations:", getChargeLevelStepTracker(), false))
    {
      paramLong = computeChargeTimeRemaining(SystemClock.elapsedRealtime() * 1000L);
      if (paramLong >= 0L)
      {
        paramPrintWriter.print("  Estimated charge time remaining: ");
        TimeUtils.formatDuration(paramLong / 1000L, paramPrintWriter);
        paramPrintWriter.println();
      }
      paramPrintWriter.println();
    }
    label717:
    if ((i != 0) && ((paramInt1 & 0x4) == 0)) {
      break label1274;
    }
    paramPrintWriter.println("Daily stats:");
    paramPrintWriter.print("  Current start time: ");
    paramPrintWriter.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getCurrentDailyStartTime()).toString());
    paramPrintWriter.print("  Next min deadline: ");
    paramPrintWriter.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getNextMinDailyDeadline()).toString());
    paramPrintWriter.print("  Next max deadline: ");
    paramPrintWriter.println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getNextMaxDailyDeadline()).toString());
    StringBuilder localStringBuilder = new StringBuilder(64);
    Object localObject1 = new int[1];
    Object localObject2 = getDailyDischargeLevelStepTracker();
    Object localObject3 = getDailyChargeLevelStepTracker();
    ArrayList localArrayList = getDailyPackageChanges();
    if ((mNumStepDurations <= 0) && (mNumStepDurations <= 0) && (localArrayList == null)) {
      break label1023;
    }
    if (((paramInt1 & 0x4) == 0) && (i != 0))
    {
      paramPrintWriter.println("  Current daily steps:");
      dumpDailyLevelStepSummary(paramPrintWriter, "    ", "Discharge", (LevelStepTracker)localObject2, localStringBuilder, (int[])localObject1);
      dumpDailyLevelStepSummary(paramPrintWriter, "    ", "Charge", (LevelStepTracker)localObject3, localStringBuilder, (int[])localObject1);
    }
    else
    {
      bool = false;
      Object localObject4 = localObject1;
      if (dumpDurationSteps(paramPrintWriter, "    ", "  Current daily discharge step durations:", (LevelStepTracker)localObject2, bool)) {
        dumpDailyLevelStepSummary(paramPrintWriter, "      ", "Discharge", (LevelStepTracker)localObject2, localStringBuilder, localObject4);
      }
      if (dumpDurationSteps(paramPrintWriter, "    ", "  Current daily charge step durations:", (LevelStepTracker)localObject3, bool)) {
        dumpDailyLevelStepSummary(paramPrintWriter, "      ", "Charge", (LevelStepTracker)localObject3, localStringBuilder, localObject4);
      }
      dumpDailyPackageChanges(paramPrintWriter, "    ", localArrayList);
    }
    label1023:
    int m = 0;
    for (int k = m;; k++)
    {
      localObject2 = getDailyItemLocked(k);
      if (localObject2 == null) {
        break;
      }
      if ((paramInt1 & 0x4) != 0) {
        paramPrintWriter.println();
      }
      paramPrintWriter.print("  Daily from ");
      paramPrintWriter.print(DateFormat.format("yyyy-MM-dd-HH-mm-ss", mStartTime).toString());
      paramPrintWriter.print(" to ");
      paramPrintWriter.print(DateFormat.format("yyyy-MM-dd-HH-mm-ss", mEndTime).toString());
      paramPrintWriter.println(":");
      if (((paramInt1 & 0x4) == 0) && (i != 0))
      {
        dumpDailyLevelStepSummary(paramPrintWriter, "    ", "Discharge", mDischargeSteps, localStringBuilder, (int[])localObject1);
        dumpDailyLevelStepSummary(paramPrintWriter, "    ", "Charge", mChargeSteps, localStringBuilder, (int[])localObject1);
      }
      else
      {
        if (dumpDurationSteps(paramPrintWriter, "      ", "    Discharge step durations:", mDischargeSteps, m)) {
          dumpDailyLevelStepSummary(paramPrintWriter, "        ", "Discharge", mDischargeSteps, localStringBuilder, (int[])localObject1);
        }
        if (dumpDurationSteps(paramPrintWriter, "      ", "    Charge step durations:", mChargeSteps, m)) {
          dumpDailyLevelStepSummary(paramPrintWriter, "        ", "Charge", mChargeSteps, localStringBuilder, (int[])localObject1);
        }
        dumpDailyPackageChanges(paramPrintWriter, "    ", mPackageChanges);
      }
    }
    paramPrintWriter.println();
    label1274:
    if ((i == 0) || ((paramInt1 & 0x2) != 0))
    {
      paramPrintWriter.println("Statistics since last charge:");
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("  System starts: ");
      ((StringBuilder)localObject1).append(getStartCount());
      ((StringBuilder)localObject1).append(", currently on battery: ");
      ((StringBuilder)localObject1).append(getIsOnBattery());
      paramPrintWriter.println(((StringBuilder)localObject1).toString());
      if ((paramInt1 & 0x40) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      dumpLocked(paramContext, paramPrintWriter, "", 0, paramInt2, bool);
      paramPrintWriter.println();
    }
  }
  
  public final void dumpLocked(Context paramContext, PrintWriter paramPrintWriter, String paramString, int paramInt1, int paramInt2)
  {
    dumpLocked(paramContext, paramPrintWriter, paramString, paramInt1, paramInt2, BatteryStatsHelper.checkWifiOnly(paramContext));
  }
  
  public final void dumpLocked(Context paramContext, PrintWriter paramPrintWriter, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Object localObject1 = this;
    Object localObject2 = paramPrintWriter;
    long l1 = SystemClock.uptimeMillis() * 1000L;
    long l2 = SystemClock.elapsedRealtime() * 1000L;
    long l3 = (l2 + 500L) / 1000L;
    long l4 = ((BatteryStats)localObject1).getBatteryUptime(l1);
    long l5 = ((BatteryStats)localObject1).computeBatteryUptime(l1, paramInt1);
    long l6 = ((BatteryStats)localObject1).computeBatteryRealtime(l2, paramInt1);
    long l7 = ((BatteryStats)localObject1).computeRealtime(l2, paramInt1);
    long l8 = ((BatteryStats)localObject1).computeUptime(l1, paramInt1);
    long l9 = ((BatteryStats)localObject1).computeBatteryScreenOffUptime(l1, paramInt1);
    long l10 = ((BatteryStats)localObject1).computeBatteryScreenOffRealtime(l2, paramInt1);
    long l11 = ((BatteryStats)localObject1).computeBatteryTimeRemaining(l2);
    long l12 = ((BatteryStats)localObject1).computeChargeTimeRemaining(l2);
    l1 = ((BatteryStats)localObject1).getScreenDozeTime(l2, paramInt1);
    Object localObject3 = new StringBuilder(128);
    Object localObject4 = getUidStats();
    int i = ((SparseArray)localObject4).size();
    int k = getEstimatedBatteryCapacity();
    if (k > 0)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Estimated battery capacity: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(k));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    k = getMinLearnedBatteryCapacity();
    if (k > 0)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Min learned battery capacity: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(k / 1000));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    k = getMaxLearnedBatteryCapacity();
    if (k > 0)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Max learned battery capacity: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(k / 1000));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Time on battery: ");
    formatTimeMs((StringBuilder)localObject3, l6 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l6, l7));
    ((StringBuilder)localObject3).append(") realtime, ");
    formatTimeMs((StringBuilder)localObject3, l5 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l5, l6));
    ((StringBuilder)localObject3).append(") uptime");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Time on battery screen off: ");
    formatTimeMs((StringBuilder)localObject3, l10 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l10, l6));
    ((StringBuilder)localObject3).append(") realtime, ");
    formatTimeMs((StringBuilder)localObject3, l9 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l9, l6));
    ((StringBuilder)localObject3).append(") uptime");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Time on battery screen doze: ");
    formatTimeMs((StringBuilder)localObject3, l1 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l1, l6));
    ((StringBuilder)localObject3).append(")");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Total run time: ");
    formatTimeMs((StringBuilder)localObject3, l7 / 1000L);
    ((StringBuilder)localObject3).append("realtime, ");
    formatTimeMs((StringBuilder)localObject3, l8 / 1000L);
    ((StringBuilder)localObject3).append("uptime");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    if (l11 >= 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Battery time remaining: ");
      formatTimeMs((StringBuilder)localObject3, l11 / 1000L);
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    if (l12 >= 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Charge time remaining: ");
      formatTimeMs((StringBuilder)localObject3, l12 / 1000L);
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    l1 = ((BatteryStats)localObject1).getUahDischarge(paramInt1);
    if (l1 >= 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Discharge: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(l1 / 1000.0D));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    l8 = ((BatteryStats)localObject1).getUahDischargeScreenOff(paramInt1);
    if (l8 >= 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Screen off discharge: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(l8 / 1000.0D));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    l5 = ((BatteryStats)localObject1).getUahDischargeScreenDoze(paramInt1);
    if (l5 >= 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Screen doze discharge: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(l5 / 1000.0D));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    l1 -= l8;
    if (l1 >= 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Screen on discharge: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(l1 / 1000.0D));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    l1 = ((BatteryStats)localObject1).getUahDischargeLightDoze(paramInt1);
    if (l1 >= 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Device light doze discharge: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(l1 / 1000.0D));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    l1 = ((BatteryStats)localObject1).getUahDischargeDeepDoze(paramInt1);
    if (l1 >= 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Device deep doze discharge: ");
      ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(l1 / 1000.0D));
      ((StringBuilder)localObject3).append(" mAh");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    ((PrintWriter)localObject2).print("  Start clock time: ");
    ((PrintWriter)localObject2).println(DateFormat.format("yyyy-MM-dd-HH-mm-ss", getStartClockTime()).toString());
    l11 = ((BatteryStats)localObject1).getScreenOnTime(l2, paramInt1);
    l7 = ((BatteryStats)localObject1).getInteractiveTime(l2, paramInt1);
    l9 = ((BatteryStats)localObject1).getPowerSaveModeEnabledTime(l2, paramInt1);
    long l13 = ((BatteryStats)localObject1).getDeviceIdleModeTime(1, l2, paramInt1);
    long l14 = ((BatteryStats)localObject1).getDeviceIdleModeTime(2, l2, paramInt1);
    long l15 = ((BatteryStats)localObject1).getDeviceIdlingTime(1, l2, paramInt1);
    l12 = ((BatteryStats)localObject1).getDeviceIdlingTime(2, l2, paramInt1);
    l1 = ((BatteryStats)localObject1).getPhoneOnTime(l2, paramInt1);
    ((BatteryStats)localObject1).getGlobalWifiRunningTime(l2, paramInt1);
    ((BatteryStats)localObject1).getWifiOnTime(l2, paramInt1);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Screen on: ");
    formatTimeMs((StringBuilder)localObject3, l11 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l11, l6));
    ((StringBuilder)localObject3).append(") ");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).getScreenOnCount(paramInt1));
    ((StringBuilder)localObject3).append("x, Interactive: ");
    formatTimeMs((StringBuilder)localObject3, l7 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l7, l6));
    ((StringBuilder)localObject3).append(")");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Screen brightnesses:");
    int m = 0;
    for (k = 0; k < 5; k++)
    {
      l16 = ((BatteryStats)localObject1).getScreenBrightnessTime(k, l2, paramInt1);
      if (l16 != 0L)
      {
        ((StringBuilder)localObject3).append("\n    ");
        ((StringBuilder)localObject3).append(paramString);
        ((StringBuilder)localObject3).append(SCREEN_BRIGHTNESS_NAMES[k]);
        ((StringBuilder)localObject3).append(" ");
        formatTimeMs((StringBuilder)localObject3, l16 / 1000L);
        ((StringBuilder)localObject3).append("(");
        ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l16, l11));
        ((StringBuilder)localObject3).append(")");
        m = 1;
      }
    }
    if (m == 0) {
      ((StringBuilder)localObject3).append(" (no activity)");
    }
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    if (l9 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Power save mode enabled: ");
      formatTimeMs((StringBuilder)localObject3, l9 / 1000L);
      ((StringBuilder)localObject3).append("(");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l9, l6));
      ((StringBuilder)localObject3).append(")");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    if (l15 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Device light idling: ");
      formatTimeMs((StringBuilder)localObject3, l15 / 1000L);
      ((StringBuilder)localObject3).append("(");
      localObject1 = this;
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l15, l6));
      ((StringBuilder)localObject3).append(") ");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).getDeviceIdlingCount(1, paramInt1));
      ((StringBuilder)localObject3).append("x");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    localObject1 = this;
    if (l13 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Idle mode light time: ");
      formatTimeMs((StringBuilder)localObject3, l13 / 1000L);
      ((StringBuilder)localObject3).append("(");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l13, l6));
      ((StringBuilder)localObject3).append(") ");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).getDeviceIdleModeCount(1, paramInt1));
      ((StringBuilder)localObject3).append("x");
      ((StringBuilder)localObject3).append(" -- longest ");
      formatTimeMs((StringBuilder)localObject3, ((BatteryStats)localObject1).getLongestDeviceIdleModeTime(1));
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    if (l12 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Device full idling: ");
      formatTimeMs((StringBuilder)localObject3, l12 / 1000L);
      ((StringBuilder)localObject3).append("(");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l12, l6));
      ((StringBuilder)localObject3).append(") ");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).getDeviceIdlingCount(2, paramInt1));
      ((StringBuilder)localObject3).append("x");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    if (l14 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Idle mode full time: ");
      formatTimeMs((StringBuilder)localObject3, l14 / 1000L);
      ((StringBuilder)localObject3).append("(");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l14, l6));
      ((StringBuilder)localObject3).append(") ");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).getDeviceIdleModeCount(2, paramInt1));
      ((StringBuilder)localObject3).append("x");
      ((StringBuilder)localObject3).append(" -- longest ");
      formatTimeMs((StringBuilder)localObject3, ((BatteryStats)localObject1).getLongestDeviceIdleModeTime(2));
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    if (l1 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Active phone call: ");
      formatTimeMs((StringBuilder)localObject3, l1 / 1000L);
      ((StringBuilder)localObject3).append("(");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l1, l6));
      ((StringBuilder)localObject3).append(") ");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).getPhoneOnCount(paramInt1));
      ((StringBuilder)localObject3).append("x");
    }
    k = ((BatteryStats)localObject1).getNumConnectivityChange(paramInt1);
    if (k != 0)
    {
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("  Connectivity changes: ");
      ((PrintWriter)localObject2).println(k);
    }
    Object localObject5 = new ArrayList();
    l12 = 0L;
    l1 = 0L;
    k = 0;
    while (k < i)
    {
      localObject6 = (Uid)((SparseArray)localObject4).valueAt(k);
      localObject7 = ((Uid)localObject6).getWakelockStats();
      m = ((ArrayMap)localObject7).size() - 1;
      for (l14 = l1; m >= 0; l14 = l1)
      {
        localObject8 = (BatteryStats.Uid.Wakelock)((ArrayMap)localObject7).valueAt(m);
        localObject9 = ((BatteryStats.Uid.Wakelock)localObject8).getWakeTime(1);
        l1 = l14;
        if (localObject9 != null) {
          l1 = l14 + ((Timer)localObject9).getTotalTimeLocked(l2, paramInt1);
        }
        localObject8 = ((BatteryStats.Uid.Wakelock)localObject8).getWakeTime(0);
        if (localObject8 != null)
        {
          l14 = ((Timer)localObject8).getTotalTimeLocked(l2, paramInt1);
          if (l14 > 0L)
          {
            if (paramInt2 < 0) {
              ((ArrayList)localObject5).add(new TimerEntry((String)((ArrayMap)localObject7).keyAt(m), ((Uid)localObject6).getUid(), (Timer)localObject8, l14));
            }
            l12 += l14;
          }
        }
        m--;
      }
      k++;
      l1 = l14;
    }
    long l17 = ((BatteryStats)localObject1).getNetworkActivityBytes(0, paramInt1);
    long l16 = ((BatteryStats)localObject1).getNetworkActivityBytes(1, paramInt1);
    long l18 = ((BatteryStats)localObject1).getNetworkActivityBytes(2, paramInt1);
    long l19 = ((BatteryStats)localObject1).getNetworkActivityBytes(3, paramInt1);
    l9 = ((BatteryStats)localObject1).getNetworkActivityPackets(0, paramInt1);
    l14 = ((BatteryStats)localObject1).getNetworkActivityPackets(1, paramInt1);
    long l20 = ((BatteryStats)localObject1).getNetworkActivityPackets(2, paramInt1);
    l15 = ((BatteryStats)localObject1).getNetworkActivityPackets(3, paramInt1);
    long l21 = ((BatteryStats)localObject1).getNetworkActivityBytes(4, paramInt1);
    long l22 = ((BatteryStats)localObject1).getNetworkActivityBytes(5, paramInt1);
    if (l1 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Total full wakelock time: ");
      formatTimeMsNoSpace((StringBuilder)localObject3, (l1 + 500L) / 1000L);
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    if (l12 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Total partial wakelock time: ");
      formatTimeMsNoSpace((StringBuilder)localObject3, (l12 + 500L) / 1000L);
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    l1 = ((BatteryStats)localObject1).getWifiMulticastWakelockTime(l2, paramInt1);
    k = ((BatteryStats)localObject1).getWifiMulticastWakelockCount(paramInt1);
    if (l1 != 0L)
    {
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Total WiFi Multicast wakelock Count: ");
      ((StringBuilder)localObject3).append(k);
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  Total WiFi Multicast wakelock time: ");
      formatTimeMsNoSpace((StringBuilder)localObject3, (l1 + 500L) / 1000L);
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    ((PrintWriter)localObject2).println("");
    paramPrintWriter.print(paramString);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  CONNECTIVITY POWER SUMMARY START");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    paramPrintWriter.print(paramString);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Logging duration for connectivity statistics: ");
    formatTimeMs((StringBuilder)localObject3, l6 / 1000L);
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Cellular Statistics:");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    paramPrintWriter.print(paramString);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("     Cellular kernel active time: ");
    l13 = ((BatteryStats)localObject1).getMobileRadioActiveTime(l2, paramInt1);
    l1 = l2;
    formatTimeMs((StringBuilder)localObject3, l13 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l13, l6));
    ((StringBuilder)localObject3).append(")");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((PrintWriter)localObject2).print("     Cellular data received: ");
    ((PrintWriter)localObject2).println(((BatteryStats)localObject1).formatBytesLocked(l17));
    ((PrintWriter)localObject2).print("     Cellular data sent: ");
    ((PrintWriter)localObject2).println(((BatteryStats)localObject1).formatBytesLocked(l16));
    ((PrintWriter)localObject2).print("     Cellular packets received: ");
    ((PrintWriter)localObject2).println(l9);
    ((PrintWriter)localObject2).print("     Cellular packets sent: ");
    ((PrintWriter)localObject2).println(l14);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("     Cellular Radio Access Technology:");
    m = 0;
    k = 0;
    l12 = l16;
    while (k < 21)
    {
      l2 = ((BatteryStats)localObject1).getPhoneDataConnectionTime(k, l1, paramInt1);
      if (l2 != 0L)
      {
        ((StringBuilder)localObject3).append("\n       ");
        ((StringBuilder)localObject3).append(paramString);
        ((StringBuilder)localObject3).append(DATA_CONNECTION_NAMES[k]);
        ((StringBuilder)localObject3).append(" ");
        formatTimeMs((StringBuilder)localObject3, l2 / 1000L);
        ((StringBuilder)localObject3).append("(");
        ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l2, l6));
        ((StringBuilder)localObject3).append(") ");
        m = 1;
      }
      k++;
    }
    if (m == 0) {
      ((StringBuilder)localObject3).append(" (no activity)");
    }
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("     Cellular Rx signal strength (RSRP):");
    Object localObject6 = new String[5];
    localObject6[0] = "very poor (less than -128dBm): ";
    localObject6[1] = "poor (-128dBm to -118dBm): ";
    localObject6[2] = "moderate (-118dBm to -108dBm): ";
    localObject6[3] = "good (-108dBm to -98dBm): ";
    localObject6[4] = "great (greater than -98dBm): ";
    int n = Math.min(6, localObject6.length);
    m = 0;
    for (k = 0; k < n; k++)
    {
      l12 = ((BatteryStats)localObject1).getPhoneSignalStrengthTime(k, l1, paramInt1);
      if (l12 != 0L)
      {
        ((StringBuilder)localObject3).append("\n       ");
        ((StringBuilder)localObject3).append(paramString);
        ((StringBuilder)localObject3).append(localObject6[k]);
        ((StringBuilder)localObject3).append(" ");
        formatTimeMs((StringBuilder)localObject3, l12 / 1000L);
        ((StringBuilder)localObject3).append("(");
        ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l12, l6));
        ((StringBuilder)localObject3).append(") ");
        m = 1;
      }
    }
    if (m == 0) {
      ((StringBuilder)localObject3).append(" (no activity)");
    }
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    localObject6 = getModemControllerActivity();
    l9 = l4;
    l14 = l3;
    l3 = l13;
    Object localObject7 = localObject4;
    ((BatteryStats)localObject1).printControllerActivity((PrintWriter)localObject2, (StringBuilder)localObject3, paramString, "Cellular", (ControllerActivityCounter)localObject6, paramInt1);
    paramPrintWriter.print(paramString);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Wifi Statistics:");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    paramPrintWriter.print(paramString);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("     Wifi kernel active time: ");
    l4 = ((BatteryStats)localObject1).getWifiActiveTime(l1, paramInt1);
    formatTimeMs((StringBuilder)localObject3, l4 / 1000L);
    ((StringBuilder)localObject3).append("(");
    ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l4, l6));
    ((StringBuilder)localObject3).append(")");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((PrintWriter)localObject2).print("     Wifi data received: ");
    ((PrintWriter)localObject2).println(((BatteryStats)localObject1).formatBytesLocked(l18));
    ((PrintWriter)localObject2).print("     Wifi data sent: ");
    ((PrintWriter)localObject2).println(((BatteryStats)localObject1).formatBytesLocked(l19));
    ((PrintWriter)localObject2).print("     Wifi packets received: ");
    ((PrintWriter)localObject2).println(l20);
    ((PrintWriter)localObject2).print("     Wifi packets sent: ");
    l12 = l15;
    ((PrintWriter)localObject2).println(l12);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("     Wifi states:");
    m = 0;
    for (k = 0; k < 8; k++)
    {
      l2 = ((BatteryStats)localObject1).getWifiStateTime(k, l1, paramInt1);
      if (l2 != 0L)
      {
        ((StringBuilder)localObject3).append("\n       ");
        ((StringBuilder)localObject3).append(WIFI_STATE_NAMES[k]);
        ((StringBuilder)localObject3).append(" ");
        formatTimeMs((StringBuilder)localObject3, l2 / 1000L);
        ((StringBuilder)localObject3).append("(");
        ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l2, l6));
        ((StringBuilder)localObject3).append(") ");
        m = 1;
      }
    }
    if (m == 0) {
      ((StringBuilder)localObject3).append(" (no activity)");
    }
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("     Wifi supplicant states:");
    m = 0;
    for (k = 0; k < 13; k++)
    {
      l12 = ((BatteryStats)localObject1).getWifiSupplStateTime(k, l1, paramInt1);
      if (l12 != 0L)
      {
        ((StringBuilder)localObject3).append("\n       ");
        ((StringBuilder)localObject3).append(WIFI_SUPPL_STATE_NAMES[k]);
        ((StringBuilder)localObject3).append(" ");
        formatTimeMs((StringBuilder)localObject3, l12 / 1000L);
        ((StringBuilder)localObject3).append("(");
        ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l12, l6));
        ((StringBuilder)localObject3).append(") ");
        m = 1;
      }
    }
    if (m == 0) {
      ((StringBuilder)localObject3).append(" (no activity)");
    }
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("     Wifi Rx signal strength (RSSI):");
    localObject4 = new String[] { "very poor (less than -88.75dBm): ", "poor (-88.75 to -77.5dBm): ", "moderate (-77.5dBm to -66.25dBm): ", "good (-66.25dBm to -55dBm): ", "great (greater than -55dBm): " };
    k = Math.min(5, localObject4.length);
    n = 0;
    for (m = 0; m < k; m++)
    {
      l12 = ((BatteryStats)localObject1).getWifiSignalStrengthTime(m, l1, paramInt1);
      if (l12 != 0L)
      {
        ((StringBuilder)localObject3).append("\n    ");
        ((StringBuilder)localObject3).append(paramString);
        ((StringBuilder)localObject3).append("     ");
        ((StringBuilder)localObject3).append(localObject4[m]);
        formatTimeMs((StringBuilder)localObject3, l12 / 1000L);
        ((StringBuilder)localObject3).append("(");
        ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l12, l6));
        ((StringBuilder)localObject3).append(") ");
        n = 1;
      }
    }
    if (n == 0) {
      ((StringBuilder)localObject3).append(" (no activity)");
    }
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((BatteryStats)localObject1).printControllerActivity((PrintWriter)localObject2, (StringBuilder)localObject3, paramString, "WiFi", getWifiControllerActivity(), paramInt1);
    paramPrintWriter.print(paramString);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  GPS Statistics:");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("     GPS signal quality (Top 4 Average CN0):");
    localObject4 = new String[] { "poor (less than 20 dBHz): ", "good (greater than 20 dBHz): " };
    m = Math.min(2, localObject4.length);
    for (k = 0; k < m; k++)
    {
      l12 = ((BatteryStats)localObject1).getGpsSignalQualityTime(k, l1, paramInt1);
      ((StringBuilder)localObject3).append("\n    ");
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("  ");
      ((StringBuilder)localObject3).append(localObject4[k]);
      formatTimeMs((StringBuilder)localObject3, l12 / 1000L);
      ((StringBuilder)localObject3).append("(");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l12, l6));
      ((StringBuilder)localObject3).append(") ");
    }
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    l12 = getGpsBatteryDrainMaMs();
    if (l12 > 0L)
    {
      paramPrintWriter.print(paramString);
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("     Battery Drain (mAh): ");
      ((StringBuilder)localObject3).append(Double.toString(l12 / 3600000.0D));
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    }
    paramPrintWriter.print(paramString);
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  CONNECTIVITY POWER SUMMARY END");
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    ((PrintWriter)localObject2).println("");
    paramPrintWriter.print(paramString);
    ((PrintWriter)localObject2).print("  Bluetooth total received: ");
    ((PrintWriter)localObject2).print(((BatteryStats)localObject1).formatBytesLocked(l21));
    ((PrintWriter)localObject2).print(", sent: ");
    ((PrintWriter)localObject2).println(((BatteryStats)localObject1).formatBytesLocked(l22));
    l12 = ((BatteryStats)localObject1).getBluetoothScanTime(l1, paramInt1);
    l12 /= 1000L;
    ((StringBuilder)localObject3).setLength(0);
    ((StringBuilder)localObject3).append(paramString);
    ((StringBuilder)localObject3).append("  Bluetooth scan time: ");
    formatTimeMs((StringBuilder)localObject3, l12);
    ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
    localObject6 = getBluetoothControllerActivity();
    ((BatteryStats)localObject1).printControllerActivity((PrintWriter)localObject2, (StringBuilder)localObject3, paramString, "Bluetooth", (ControllerActivityCounter)localObject6, paramInt1);
    paramPrintWriter.println();
    if (paramInt1 == 2)
    {
      if (getIsOnBattery())
      {
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject2).println("  Device is currently unplugged");
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject2).print("    Discharge cycle start level: ");
        ((PrintWriter)localObject2).println(getDischargeStartLevel());
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject2).print("    Discharge cycle current level: ");
        ((PrintWriter)localObject2).println(getDischargeCurrentLevel());
      }
      else
      {
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject2).println("  Device is currently plugged into power");
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject2).print("    Last discharge cycle start level: ");
        ((PrintWriter)localObject2).println(getDischargeStartLevel());
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject2).print("    Last discharge cycle end level: ");
        ((PrintWriter)localObject2).println(getDischargeCurrentLevel());
      }
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Amount discharged while screen on: ");
      ((PrintWriter)localObject2).println(getDischargeAmountScreenOn());
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Amount discharged while screen off: ");
      ((PrintWriter)localObject2).println(getDischargeAmountScreenOff());
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Amount discharged while screen doze: ");
      ((PrintWriter)localObject2).println(getDischargeAmountScreenDoze());
      ((PrintWriter)localObject2).println(" ");
    }
    else
    {
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).println("  Device battery use since last full charge");
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Amount discharged (lower bound): ");
      ((PrintWriter)localObject2).println(getLowDischargeAmountSinceCharge());
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Amount discharged (upper bound): ");
      ((PrintWriter)localObject2).println(getHighDischargeAmountSinceCharge());
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Amount discharged while screen on: ");
      ((PrintWriter)localObject2).println(getDischargeAmountScreenOnSinceCharge());
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Amount discharged while screen off: ");
      ((PrintWriter)localObject2).println(getDischargeAmountScreenOffSinceCharge());
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Amount discharged while screen doze: ");
      ((PrintWriter)localObject2).println(getDischargeAmountScreenDozeSinceCharge());
      paramPrintWriter.println();
    }
    localObject6 = new BatteryStatsHelper(paramContext, false, paramBoolean);
    ((BatteryStatsHelper)localObject6).create((BatteryStats)localObject1);
    ((BatteryStatsHelper)localObject6).refreshStats(paramInt1, -1);
    Object localObject8 = ((BatteryStatsHelper)localObject6).getUsageList();
    if ((localObject8 != null) && (((List)localObject8).size() > 0))
    {
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).println("  Estimated power use (mAh):");
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).print("    Capacity: ");
      ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, ((BatteryStatsHelper)localObject6).getPowerProfile().getBatteryCapacity());
      ((PrintWriter)localObject2).print(", Computed drain: ");
      ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, ((BatteryStatsHelper)localObject6).getComputedPower());
      ((PrintWriter)localObject2).print(", actual drain: ");
      ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, ((BatteryStatsHelper)localObject6).getMinDrainedPower());
      if (((BatteryStatsHelper)localObject6).getMinDrainedPower() != ((BatteryStatsHelper)localObject6).getMaxDrainedPower())
      {
        ((PrintWriter)localObject2).print("-");
        ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, ((BatteryStatsHelper)localObject6).getMaxDrainedPower());
      }
      paramPrintWriter.println();
      k = 0;
      paramContext = (Context)localObject4;
      while (k < ((List)localObject8).size())
      {
        localObject4 = (BatterySipper)((List)localObject8).get(k);
        paramPrintWriter.print(paramString);
        switch (2.$SwitchMap$com$android$internal$os$BatterySipper$DrainType[drainType.ordinal()])
        {
        default: 
          ((PrintWriter)localObject2).print("    ???: ");
          break;
        case 13: 
          ((PrintWriter)localObject2).print("    Camera: ");
          break;
        case 12: 
          ((PrintWriter)localObject2).print("    Over-counted: ");
          break;
        case 11: 
          ((PrintWriter)localObject2).print("    Unaccounted: ");
          break;
        case 10: 
          ((PrintWriter)localObject2).print("    User ");
          ((PrintWriter)localObject2).print(userId);
          ((PrintWriter)localObject2).print(": ");
          break;
        case 9: 
          ((PrintWriter)localObject2).print("    Uid ");
          UserHandle.formatUid((PrintWriter)localObject2, uidObj.getUid());
          ((PrintWriter)localObject2).print(": ");
          break;
        case 8: 
          ((PrintWriter)localObject2).print("    Flashlight: ");
          break;
        case 7: 
          ((PrintWriter)localObject2).print("    Screen: ");
          break;
        case 6: 
          ((PrintWriter)localObject2).print("    Bluetooth: ");
          break;
        case 5: 
          ((PrintWriter)localObject2).print("    Wifi: ");
          break;
        case 4: 
          ((PrintWriter)localObject2).print("    Phone calls: ");
          break;
        case 3: 
          ((PrintWriter)localObject2).print("    Cell standby: ");
          break;
        case 2: 
          ((PrintWriter)localObject2).print("    Idle: ");
          break;
        case 1: 
          ((PrintWriter)localObject2).print("    Ambient display: ");
        }
        ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, totalPowerMah);
        if (usagePowerMah != totalPowerMah)
        {
          ((PrintWriter)localObject2).print(" (");
          if (usagePowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" usage=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, usagePowerMah);
          }
          if (cpuPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" cpu=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, cpuPowerMah);
          }
          if (wakeLockPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" wake=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, wakeLockPowerMah);
          }
          if (mobileRadioPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" radio=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, mobileRadioPowerMah);
          }
          if (wifiPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" wifi=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, wifiPowerMah);
          }
          if (bluetoothPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" bt=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, bluetoothPowerMah);
          }
          if (gpsPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" gps=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, gpsPowerMah);
          }
          if (sensorPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" sensor=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, sensorPowerMah);
          }
          if (cameraPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" camera=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, cameraPowerMah);
          }
          if (flashlightPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" flash=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, flashlightPowerMah);
          }
          ((PrintWriter)localObject2).print(" )");
        }
        if (totalSmearedPowerMah != totalPowerMah)
        {
          ((PrintWriter)localObject2).print(" Including smearing: ");
          ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, totalSmearedPowerMah);
          ((PrintWriter)localObject2).print(" (");
          if (screenPowerMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" screen=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, screenPowerMah);
          }
          if (proportionalSmearMah != 0.0D)
          {
            ((PrintWriter)localObject2).print(" proportional=");
            ((BatteryStats)localObject1).printmAh((PrintWriter)localObject2, proportionalSmearMah);
          }
          ((PrintWriter)localObject2).print(" )");
        }
        if (shouldHide) {
          ((PrintWriter)localObject2).print(" Excluded from smearing");
        }
        paramPrintWriter.println();
        k++;
      }
      paramPrintWriter.println();
    }
    else
    {
      paramContext = (Context)localObject4;
    }
    paramContext = ((BatteryStatsHelper)localObject6).getMobilemsppList();
    if ((paramContext != null) && (paramContext.size() > 0))
    {
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).println("  Per-app mobile ms per packet:");
      l12 = 0L;
      for (k = 0; k < paramContext.size(); k++)
      {
        localObject4 = (BatterySipper)paramContext.get(k);
        ((StringBuilder)localObject3).setLength(0);
        ((StringBuilder)localObject3).append(paramString);
        ((StringBuilder)localObject3).append("    Uid ");
        UserHandle.formatUid((StringBuilder)localObject3, uidObj.getUid());
        ((StringBuilder)localObject3).append(": ");
        ((StringBuilder)localObject3).append(BatteryStatsHelper.makemAh(mobilemspp));
        ((StringBuilder)localObject3).append(" (");
        ((StringBuilder)localObject3).append(mobileRxPackets + mobileTxPackets);
        ((StringBuilder)localObject3).append(" packets over ");
        formatTimeMsNoSpace((StringBuilder)localObject3, mobileActive);
        ((StringBuilder)localObject3).append(") ");
        ((StringBuilder)localObject3).append(mobileActiveCount);
        ((StringBuilder)localObject3).append("x");
        ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
        l12 += mobileActive;
      }
      ((StringBuilder)localObject3).setLength(0);
      ((StringBuilder)localObject3).append(paramString);
      ((StringBuilder)localObject3).append("    TOTAL TIME: ");
      formatTimeMs((StringBuilder)localObject3, l12);
      ((StringBuilder)localObject3).append("(");
      ((StringBuilder)localObject3).append(((BatteryStats)localObject1).formatRatioLocked(l12, l6));
      ((StringBuilder)localObject3).append(")");
      ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
      paramPrintWriter.println();
    }
    localObject1 = new Comparator()
    {
      public int compare(BatteryStats.TimerEntry paramAnonymousTimerEntry1, BatteryStats.TimerEntry paramAnonymousTimerEntry2)
      {
        long l1 = mTime;
        long l2 = mTime;
        if (l1 < l2) {
          return 1;
        }
        if (l1 > l2) {
          return -1;
        }
        return 0;
      }
    };
    Object localObject10;
    if (paramInt2 < 0)
    {
      localObject8 = getKernelWakelockStats();
      if (((Map)localObject8).size() > 0)
      {
        localObject4 = new ArrayList();
        localObject9 = ((Map)localObject8).entrySet().iterator();
        paramContext = (Context)localObject6;
        while (((Iterator)localObject9).hasNext())
        {
          localObject6 = (Map.Entry)((Iterator)localObject9).next();
          localObject10 = (Timer)((Map.Entry)localObject6).getValue();
          l12 = computeWakeLock((Timer)localObject10, l1, paramInt1);
          if (l12 > 0L) {
            ((ArrayList)localObject4).add(new TimerEntry((String)((Map.Entry)localObject6).getKey(), 0, (Timer)localObject10, l12));
          }
        }
        if (((ArrayList)localObject4).size() > 0)
        {
          Collections.sort((List)localObject4, (Comparator)localObject1);
          paramPrintWriter.print(paramString);
          ((PrintWriter)localObject2).println("  All kernel wake locks:");
          for (k = 0; k < ((ArrayList)localObject4).size(); k++)
          {
            localObject6 = (TimerEntry)((ArrayList)localObject4).get(k);
            ((StringBuilder)localObject3).setLength(0);
            ((StringBuilder)localObject3).append(paramString);
            ((StringBuilder)localObject3).append("  Kernel Wake lock ");
            ((StringBuilder)localObject3).append(mName);
            if (!printWakeLock((StringBuilder)localObject3, mTimer, l1, null, paramInt1, ": ").equals(": "))
            {
              ((StringBuilder)localObject3).append(" realtime");
              ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
            }
          }
          paramPrintWriter.println();
        }
        else {}
      }
      paramContext = (Context)localObject5;
      if (paramContext.size() > 0)
      {
        Collections.sort(paramContext, (Comparator)localObject1);
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject2).println("  All partial wake locks:");
        k = 0;
        localObject4 = localObject8;
        while (k < paramContext.size())
        {
          localObject5 = (TimerEntry)paramContext.get(k);
          ((StringBuilder)localObject3).setLength(0);
          ((StringBuilder)localObject3).append("  Wake lock ");
          UserHandle.formatUid((StringBuilder)localObject3, mId);
          ((StringBuilder)localObject3).append(" ");
          ((StringBuilder)localObject3).append(mName);
          printWakeLock((StringBuilder)localObject3, mTimer, l1, null, paramInt1, ": ");
          ((StringBuilder)localObject3).append(" realtime");
          ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
          k++;
        }
        paramContext.clear();
        paramPrintWriter.println();
        localObject4 = paramContext;
      }
      else
      {
        localObject4 = paramContext;
      }
      localObject5 = getWakeupReasonStats();
      paramContext = (Context)localObject4;
      l12 = l1;
      if (((Map)localObject5).size() > 0)
      {
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject2).println("  All wakeup reasons:");
        localObject6 = new ArrayList();
        paramContext = ((Map)localObject5).entrySet().iterator();
        while (paramContext.hasNext())
        {
          localObject8 = (Map.Entry)paramContext.next();
          localObject9 = (Timer)((Map.Entry)localObject8).getValue();
          ((ArrayList)localObject6).add(new TimerEntry((String)((Map.Entry)localObject8).getKey(), 0, (Timer)localObject9, ((Timer)localObject9).getCountLocked(paramInt1)));
        }
        Collections.sort((List)localObject6, (Comparator)localObject1);
        k = 0;
        paramContext = (Context)localObject5;
        localObject5 = localObject6;
        while (k < ((ArrayList)localObject5).size())
        {
          localObject6 = (TimerEntry)((ArrayList)localObject5).get(k);
          ((StringBuilder)localObject3).setLength(0);
          ((StringBuilder)localObject3).append(paramString);
          ((StringBuilder)localObject3).append("  Wakeup reason ");
          ((StringBuilder)localObject3).append(mName);
          printWakeLock((StringBuilder)localObject3, mTimer, l1, null, paramInt1, ": ");
          ((StringBuilder)localObject3).append(" realtime");
          ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
          k++;
        }
        paramPrintWriter.println();
        paramContext = (Context)localObject4;
        l12 = l1;
      }
    }
    else
    {
      paramContext = (Context)localObject5;
      l12 = l1;
    }
    localObject6 = getKernelMemoryStats();
    if (((LongSparseArray)localObject6).size() > 0)
    {
      ((PrintWriter)localObject2).println("  Memory Stats");
      for (k = 0; k < ((LongSparseArray)localObject6).size(); k++)
      {
        ((StringBuilder)localObject3).setLength(0);
        ((StringBuilder)localObject3).append("  Bandwidth ");
        ((StringBuilder)localObject3).append(((LongSparseArray)localObject6).keyAt(k));
        ((StringBuilder)localObject3).append(" Time ");
        ((StringBuilder)localObject3).append(((Timer)((LongSparseArray)localObject6).valueAt(k)).getTotalTimeLocked(l12, paramInt1));
        ((PrintWriter)localObject2).println(((StringBuilder)localObject3).toString());
      }
      paramPrintWriter.println();
    }
    Object localObject9 = getRpmStats();
    if (((Map)localObject9).size() > 0)
    {
      paramPrintWriter.print(paramString);
      ((PrintWriter)localObject2).println("  Resource Power Manager Stats");
      localObject2 = localObject6;
      localObject4 = localObject3;
      l1 = l6;
      localObject8 = localObject1;
      localObject8 = paramContext;
      l4 = l10;
      l4 = l5;
      l4 = l8;
      l4 = l7;
      l4 = l11;
      if (((Map)localObject9).size() > 0)
      {
        localObject10 = ((Map)localObject9).entrySet().iterator();
        localObject5 = paramContext;
        paramContext = (Context)localObject6;
        for (;;)
        {
          localObject2 = paramContext;
          localObject4 = localObject3;
          l1 = l6;
          localObject8 = localObject1;
          localObject8 = localObject5;
          l4 = l10;
          l4 = l5;
          l4 = l8;
          l4 = l7;
          l4 = l11;
          if (!((Iterator)localObject10).hasNext()) {
            break;
          }
          localObject6 = (Map.Entry)((Iterator)localObject10).next();
          localObject4 = (String)((Map.Entry)localObject6).getKey();
          printTimer(paramPrintWriter, (StringBuilder)localObject3, (Timer)((Map.Entry)localObject6).getValue(), l12, paramInt1, paramString, (String)localObject4);
        }
      }
      localObject3 = localObject2;
      paramContext = (Context)localObject4;
      paramPrintWriter.println();
      l10 = l1;
    }
    else
    {
      paramContext = (Context)localObject3;
      l10 = l6;
      localObject3 = localObject6;
    }
    localObject1 = getCpuFreqs();
    if (localObject1 != null)
    {
      localObject4 = paramContext;
      ((StringBuilder)localObject4).setLength(0);
      ((StringBuilder)localObject4).append("  CPU freqs:");
      for (k = 0; k < localObject1.length; k++)
      {
        localObject5 = new StringBuilder();
        ((StringBuilder)localObject5).append(" ");
        ((StringBuilder)localObject5).append(localObject1[k]);
        ((StringBuilder)localObject4).append(((StringBuilder)localObject5).toString());
      }
      paramPrintWriter.println(((StringBuilder)localObject4).toString());
      paramPrintWriter.println();
    }
    localObject4 = paramPrintWriter;
    int i1 = 0;
    localObject5 = localObject3;
    localObject3 = localObject7;
    l5 = l3;
    l6 = l14;
    l1 = l9;
    localObject6 = localObject9;
    l8 = l12;
    while (i1 < i)
    {
      k = ((SparseArray)localObject3).keyAt(i1);
      if ((paramInt2 >= 0) && (k != paramInt2) && (k != 1000))
      {
        l7 = l8;
        l8 = l1;
        l1 = l6;
        l6 = l5;
        l5 = l7;
      }
      else
      {
        localObject2 = (Uid)((SparseArray)localObject3).valueAt(i1);
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject4).print("  ");
        UserHandle.formatUid((PrintWriter)localObject4, k);
        ((PrintWriter)localObject4).println(":");
        n = 0;
        long l23 = ((Uid)localObject2).getNetworkActivityBytes(0, paramInt1);
        l7 = ((Uid)localObject2).getNetworkActivityBytes(1, paramInt1);
        l4 = ((Uid)localObject2).getNetworkActivityBytes(2, paramInt1);
        l18 = ((Uid)localObject2).getNetworkActivityBytes(3, paramInt1);
        l2 = ((Uid)localObject2).getNetworkActivityBytes(4, paramInt1);
        l15 = ((Uid)localObject2).getNetworkActivityBytes(5, paramInt1);
        l17 = ((Uid)localObject2).getNetworkActivityPackets(0, paramInt1);
        l21 = ((Uid)localObject2).getNetworkActivityPackets(1, paramInt1);
        l3 = ((Uid)localObject2).getNetworkActivityPackets(2, paramInt1);
        l16 = ((Uid)localObject2).getNetworkActivityPackets(3, paramInt1);
        l20 = ((Uid)localObject2).getMobileRadioActiveTime(paramInt1);
        int i2 = ((Uid)localObject2).getMobileRadioActiveCount(paramInt1);
        l22 = ((Uid)localObject2).getFullWifiLockTime(l8, paramInt1);
        l13 = ((Uid)localObject2).getWifiScanTime(l8, paramInt1);
        k = ((Uid)localObject2).getWifiScanCount(paramInt1);
        m = ((Uid)localObject2).getWifiScanBackgroundCount(paramInt1);
        l9 = ((Uid)localObject2).getWifiScanActualTime(l8);
        l11 = ((Uid)localObject2).getWifiScanBackgroundTime(l8);
        l14 = ((Uid)localObject2).getWifiRunningTime(l8, paramInt1);
        l19 = ((Uid)localObject2).getMobileRadioApWakeupCount(paramInt1);
        l12 = ((Uid)localObject2).getWifiRadioApWakeupCount(paramInt1);
        if ((l23 <= 0L) && (l7 <= 0L) && (l17 <= 0L) && (l21 <= 0L)) {
          break label8167;
        }
        localObject4 = paramPrintWriter;
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject4).print("    Mobile network: ");
        localObject7 = this;
        ((PrintWriter)localObject4).print(((BatteryStats)localObject7).formatBytesLocked(l23));
        ((PrintWriter)localObject4).print(" received, ");
        ((PrintWriter)localObject4).print(((BatteryStats)localObject7).formatBytesLocked(l7));
        ((PrintWriter)localObject4).print(" sent (packets ");
        ((PrintWriter)localObject4).print(l17);
        ((PrintWriter)localObject4).print(" received, ");
        ((PrintWriter)localObject4).print(l21);
        ((PrintWriter)localObject4).println(" sent)");
        label8167:
        localObject4 = paramPrintWriter;
        localObject9 = this;
        if ((l20 <= 0L) && (i2 <= 0)) {
          break label8346;
        }
        localObject7 = paramContext;
        ((StringBuilder)localObject7).setLength(0);
        ((StringBuilder)localObject7).append(paramString);
        ((StringBuilder)localObject7).append("    Mobile radio active: ");
        formatTimeMs((StringBuilder)localObject7, l20 / 1000L);
        ((StringBuilder)localObject7).append("(");
        ((StringBuilder)localObject7).append(((BatteryStats)localObject9).formatRatioLocked(l20, l5));
        ((StringBuilder)localObject7).append(") ");
        ((StringBuilder)localObject7).append(i2);
        ((StringBuilder)localObject7).append("x");
        l21 = l17 + l21;
        l7 = l21;
        if (l21 == 0L) {
          l7 = 1L;
        }
        ((StringBuilder)localObject7).append(" @ ");
        ((StringBuilder)localObject7).append(BatteryStatsHelper.makemAh(l20 / 1000L / l7));
        ((StringBuilder)localObject7).append(" mspp");
        ((PrintWriter)localObject4).println(((StringBuilder)localObject7).toString());
        label8346:
        if (l19 > 0L)
        {
          paramContext.setLength(0);
          paramContext.append(paramString);
          paramContext.append("    Mobile radio AP wakeups: ");
          paramContext.append(l19);
          ((PrintWriter)localObject4).println(paramContext.toString());
        }
        localObject7 = paramString;
        localObject8 = new StringBuilder();
        ((StringBuilder)localObject8).append((String)localObject7);
        ((StringBuilder)localObject8).append("  ");
        localObject10 = ((StringBuilder)localObject8).toString();
        localObject8 = ((Uid)localObject2).getModemControllerActivity();
        l7 = l5;
        ((BatteryStats)localObject9).printControllerActivityIfInteresting((PrintWriter)localObject4, paramContext, (String)localObject10, "Cellular", (ControllerActivityCounter)localObject8, paramInt1);
        if ((l4 <= 0L) && (l18 <= 0L)) {
          if (l3 <= 0L)
          {
            if (l16 <= 0L) {
              break label8577;
            }
          }
          else {}
        }
        paramPrintWriter.print(paramString);
        localObject4 = paramPrintWriter;
        ((PrintWriter)localObject4).print("    Wi-Fi network: ");
        ((PrintWriter)localObject4).print(((BatteryStats)localObject9).formatBytesLocked(l4));
        ((PrintWriter)localObject4).print(" received, ");
        ((PrintWriter)localObject4).print(((BatteryStats)localObject9).formatBytesLocked(l18));
        ((PrintWriter)localObject4).print(" sent (packets ");
        ((PrintWriter)localObject4).print(l3);
        ((PrintWriter)localObject4).print(" received, ");
        ((PrintWriter)localObject4).print(l16);
        ((PrintWriter)localObject4).println(" sent)");
        label8577:
        if (l22 == 0L) {
          if (l13 == 0L)
          {
            if (k == 0) {
              if (m == 0)
              {
                l4 = l9;
                if (l4 == 0L)
                {
                  l5 = l11;
                  if (l5 == 0L)
                  {
                    l3 = l14;
                    if (l3 != 0L) {
                      break label8666;
                    }
                    localObject4 = paramPrintWriter;
                    l5 = l3;
                    l9 = l4;
                    break label9076;
                  }
                }
                else {}
              }
            }
          }
          else {}
        }
        label8666:
        i2 = k;
        l5 = l14;
        localObject4 = paramContext;
        ((StringBuilder)localObject4).setLength(0);
        ((StringBuilder)localObject4).append((String)localObject7);
        ((StringBuilder)localObject4).append("    Wifi Running: ");
        formatTimeMs((StringBuilder)localObject4, l5 / 1000L);
        ((StringBuilder)localObject4).append("(");
        ((StringBuilder)localObject4).append(((BatteryStats)localObject9).formatRatioLocked(l5, l10));
        ((StringBuilder)localObject4).append(")\n");
        ((StringBuilder)localObject4).append((String)localObject7);
        ((StringBuilder)localObject4).append("    Full Wifi Lock: ");
        formatTimeMs((StringBuilder)localObject4, l22 / 1000L);
        ((StringBuilder)localObject4).append("(");
        ((StringBuilder)localObject4).append(((BatteryStats)localObject9).formatRatioLocked(l22, l10));
        ((StringBuilder)localObject4).append(")\n");
        ((StringBuilder)localObject4).append((String)localObject7);
        ((StringBuilder)localObject4).append("    Wifi Scan (blamed): ");
        formatTimeMs((StringBuilder)localObject4, l13 / 1000L);
        ((StringBuilder)localObject4).append("(");
        ((StringBuilder)localObject4).append(((BatteryStats)localObject9).formatRatioLocked(l13, l10));
        ((StringBuilder)localObject4).append(") ");
        ((StringBuilder)localObject4).append(i2);
        ((StringBuilder)localObject4).append("x\n");
        ((StringBuilder)localObject4).append((String)localObject7);
        ((StringBuilder)localObject4).append("    Wifi Scan (actual): ");
        formatTimeMs((StringBuilder)localObject4, l9 / 1000L);
        ((StringBuilder)localObject4).append("(");
        l14 = l8;
        ((StringBuilder)localObject4).append(((BatteryStats)localObject9).formatRatioLocked(l9, ((BatteryStats)localObject9).computeBatteryRealtime(l14, 0)));
        ((StringBuilder)localObject4).append(") ");
        ((StringBuilder)localObject4).append(i2);
        ((StringBuilder)localObject4).append("x\n");
        ((StringBuilder)localObject4).append((String)localObject7);
        ((StringBuilder)localObject4).append("    Background Wifi Scan: ");
        formatTimeMs((StringBuilder)localObject4, l11 / 1000L);
        ((StringBuilder)localObject4).append("(");
        ((StringBuilder)localObject4).append(((BatteryStats)localObject9).formatRatioLocked(l11, ((BatteryStats)localObject9).computeBatteryRealtime(l14, 0)));
        ((StringBuilder)localObject4).append(") ");
        ((StringBuilder)localObject4).append(m);
        ((StringBuilder)localObject4).append("x");
        localObject8 = ((StringBuilder)localObject4).toString();
        localObject4 = paramPrintWriter;
        ((PrintWriter)localObject4).println((String)localObject8);
        l5 = l11;
        label9076:
        l5 = l8;
        if (l12 > 0L)
        {
          paramContext.setLength(0);
          paramContext.append((String)localObject7);
          paramContext.append("    WiFi AP wakeups: ");
          paramContext.append(l12);
          ((PrintWriter)localObject4).println(paramContext.toString());
        }
        localObject8 = new StringBuilder();
        ((StringBuilder)localObject8).append((String)localObject7);
        ((StringBuilder)localObject8).append("  ");
        Object localObject11 = ((StringBuilder)localObject8).toString();
        localObject10 = ((Uid)localObject2).getWifiControllerActivity();
        l11 = l10;
        localObject8 = localObject4;
        ((BatteryStats)localObject9).printControllerActivityIfInteresting((PrintWriter)localObject4, paramContext, (String)localObject11, "WiFi", (ControllerActivityCounter)localObject10, paramInt1);
        if ((l2 <= 0L) && (l15 <= 0L)) {
          break label9261;
        }
        paramPrintWriter.print(paramString);
        ((PrintWriter)localObject8).print("    Bluetooth network: ");
        ((PrintWriter)localObject8).print(((BatteryStats)localObject9).formatBytesLocked(l2));
        ((PrintWriter)localObject8).print(" received, ");
        ((PrintWriter)localObject8).print(((BatteryStats)localObject9).formatBytesLocked(l15));
        ((PrintWriter)localObject8).println(" sent");
        label9261:
        l9 = l15;
        l12 = l2;
        localObject9 = ((Uid)localObject2).getBluetoothScanTimer();
        if (localObject9 != null)
        {
          l2 = (((Timer)localObject9).getTotalTimeLocked(l5, paramInt1) + 500L) / 1000L;
          if (l2 != 0L)
          {
            i2 = ((Timer)localObject9).getCountLocked(paramInt1);
            localObject4 = ((Uid)localObject2).getBluetoothScanBackgroundTimer();
            if (localObject4 != null) {
              k = ((Timer)localObject4).getCountLocked(paramInt1);
            } else {
              k = 0;
            }
            l15 = ((Timer)localObject9).getTotalDurationMsLocked(l6);
            if (localObject4 != null) {
              l8 = ((Timer)localObject4).getTotalDurationMsLocked(l6);
            } else {
              l8 = 0L;
            }
            if (((Uid)localObject2).getBluetoothScanResultCounter() != null) {
              m = ((Uid)localObject2).getBluetoothScanResultCounter().getCountLocked(paramInt1);
            } else {
              m = 0;
            }
            if (((Uid)localObject2).getBluetoothScanResultBgCounter() != null) {
              n = ((Uid)localObject2).getBluetoothScanResultBgCounter().getCountLocked(paramInt1);
            } else {
              n = 0;
            }
            localObject10 = ((Uid)localObject2).getBluetoothUnoptimizedScanTimer();
            if (localObject10 != null) {
              l14 = ((Timer)localObject10).getTotalDurationMsLocked(l6);
            } else {
              l14 = 0L;
            }
            if (localObject10 != null) {
              l3 = ((Timer)localObject10).getMaxDurationMsLocked(l6);
            } else {
              l3 = 0L;
            }
            localObject8 = ((Uid)localObject2).getBluetoothUnoptimizedScanBackgroundTimer();
            if (localObject8 != null) {
              l10 = ((Timer)localObject8).getTotalDurationMsLocked(l6);
            } else {
              l10 = 0L;
            }
            if (localObject8 != null) {
              l4 = ((Timer)localObject8).getMaxDurationMsLocked(l6);
            } else {
              l4 = 0L;
            }
            paramContext.setLength(0);
            if (l15 != l2)
            {
              paramContext.append((String)localObject7);
              paramContext.append("    Bluetooth Scan (total blamed realtime): ");
              formatTimeMs(paramContext, l2);
              paramContext.append(" (");
              paramContext.append(i2);
              paramContext.append(" times)");
              if (((Timer)localObject9).isRunningLocked()) {
                paramContext.append(" (currently running)");
              }
              paramContext.append("\n");
            }
            paramContext.append((String)localObject7);
            paramContext.append("    Bluetooth Scan (total actual realtime): ");
            formatTimeMs(paramContext, l15);
            paramContext.append(" (");
            paramContext.append(i2);
            paramContext.append(" times)");
            if (((Timer)localObject9).isRunningLocked()) {
              paramContext.append(" (currently running)");
            }
            paramContext.append("\n");
            if ((l8 <= 0L) && (k <= 0)) {
              break label9758;
            }
            paramContext.append((String)localObject7);
            paramContext.append("    Bluetooth Scan (background realtime): ");
            formatTimeMs(paramContext, l8);
            paramContext.append(" (");
            paramContext.append(k);
            paramContext.append(" times)");
            if ((localObject4 != null) && (((Timer)localObject4).isRunningLocked())) {
              paramContext.append(" (currently running in background)");
            }
            paramContext.append("\n");
            label9758:
            paramContext.append((String)localObject7);
            paramContext.append("    Bluetooth Scan Results: ");
            paramContext.append(m);
            paramContext.append(" (");
            paramContext.append(n);
            paramContext.append(" in background)");
            if ((l14 <= 0L) && (l10 <= 0L)) {
              break label9980;
            }
            paramContext.append("\n");
            paramContext.append((String)localObject7);
            paramContext.append("    Unoptimized Bluetooth Scan (realtime): ");
            formatTimeMs(paramContext, l14);
            paramContext.append(" (max ");
            formatTimeMs(paramContext, l3);
            paramContext.append(")");
            if ((localObject10 != null) && (((Timer)localObject10).isRunningLocked())) {
              paramContext.append(" (currently running unoptimized)");
            }
            if ((localObject8 != null) && (l10 > 0L))
            {
              paramContext.append("\n");
              paramContext.append((String)localObject7);
              paramContext.append("    Unoptimized Bluetooth Scan (background realtime): ");
              formatTimeMs(paramContext, l10);
              paramContext.append(" (max ");
              formatTimeMs(paramContext, l4);
              paramContext.append(")");
              if (((Timer)localObject8).isRunningLocked()) {
                paramContext.append(" (currently running unoptimized in background)");
              }
            }
            label9980:
            localObject8 = paramContext.toString();
            localObject4 = paramPrintWriter;
            ((PrintWriter)localObject4).println((String)localObject8);
            k = 1;
          }
          else
          {
            localObject4 = localObject8;
            k = n;
          }
        }
        else
        {
          k = n;
          localObject4 = localObject8;
        }
        localObject8 = localObject9;
        l8 = l6;
        l6 = l5;
        if (((Uid)localObject2).hasUserActivity())
        {
          m = 0;
          n = 0;
          while (n < 4)
          {
            i3 = ((Uid)localObject2).getUserActivityCount(n, paramInt1);
            i2 = m;
            if (i3 != 0)
            {
              if (m == 0)
              {
                paramContext.setLength(0);
                paramContext.append("    User activity: ");
                m = 1;
              }
              else
              {
                paramContext.append(", ");
              }
              paramContext.append(i3);
              paramContext.append(" ");
              paramContext.append(Uid.USER_ACTIVITY_TYPES[n]);
              i2 = m;
            }
            n++;
            m = i2;
          }
          if (m != 0) {
            ((PrintWriter)localObject4).println(paramContext.toString());
          }
        }
        localObject9 = ((Uid)localObject2).getWakelockStats();
        l5 = 0L;
        m = ((ArrayMap)localObject9).size() - 1;
        l2 = 0L;
        n = 0;
        l10 = 0L;
        l4 = 0L;
        l14 = l9;
        l3 = l12;
        l12 = l2;
        l9 = l4;
        while (m >= 0)
        {
          localObject10 = (BatteryStats.Uid.Wakelock)((ArrayMap)localObject9).valueAt(m);
          paramContext.setLength(0);
          paramContext.append((String)localObject7);
          paramContext.append("    Wake lock ");
          paramContext.append((String)((ArrayMap)localObject9).keyAt(m));
          localObject11 = printWakeLock(paramContext, ((BatteryStats.Uid.Wakelock)localObject10).getWakeTime(1), l6, "full", paramInt1, ": ");
          localObject7 = ((BatteryStats.Uid.Wakelock)localObject10).getWakeTime(0);
          localObject11 = printWakeLock(paramContext, (Timer)localObject7, l6, "partial", paramInt1, (String)localObject11);
          if (localObject7 != null) {}
          for (localObject7 = ((Timer)localObject7).getSubTimer();; localObject7 = null) {
            break;
          }
          localObject7 = printWakeLock(paramContext, (Timer)localObject7, l6, "background partial", paramInt1, (String)localObject11);
          localObject7 = printWakeLock(paramContext, ((BatteryStats.Uid.Wakelock)localObject10).getWakeTime(2), l6, "window", paramInt1, (String)localObject7);
          printWakeLock(paramContext, ((BatteryStats.Uid.Wakelock)localObject10).getWakeTime(18), l6, "draw", paramInt1, (String)localObject7);
          paramContext.append(" realtime");
          ((PrintWriter)localObject4).println(paramContext.toString());
          k = 1;
          n++;
          l9 = computeWakeLock(((BatteryStats.Uid.Wakelock)localObject10).getWakeTime(1), l6, paramInt1) + l9;
          l15 = computeWakeLock(((BatteryStats.Uid.Wakelock)localObject10).getWakeTime(0), l6, paramInt1);
          l2 = computeWakeLock(((BatteryStats.Uid.Wakelock)localObject10).getWakeTime(2), l6, paramInt1);
          l4 = computeWakeLock(((BatteryStats.Uid.Wakelock)localObject10).getWakeTime(18), l6, paramInt1);
          m--;
          l5 = l15 + l5;
          l10 += l2;
          localObject7 = paramString;
          l12 += l4;
        }
        l14 = l5;
        if (n > 1)
        {
          if (((Uid)localObject2).getAggregatedPartialWakelockTimer() != null)
          {
            localObject7 = ((Uid)localObject2).getAggregatedPartialWakelockTimer();
            l3 = ((Timer)localObject7).getTotalDurationMsLocked(l8);
            localObject7 = ((Timer)localObject7).getSubTimer();
            if (localObject7 != null) {
              l5 = ((Timer)localObject7).getTotalDurationMsLocked(l8);
            } else {
              l5 = 0L;
            }
          }
          else
          {
            l5 = 0L;
            l3 = 0L;
          }
          if ((l3 == 0L) && (l5 == 0L) && (l9 == 0L) && (l14 == 0L) && (l10 == 0L)) {
            break label10883;
          }
          paramContext.setLength(0);
          paramContext.append(paramString);
          paramContext.append("    TOTAL wake: ");
          n = 0;
          if (l9 != 0L)
          {
            n = 1;
            formatTimeMs(paramContext, l9);
            paramContext.append("full");
          }
          m = n;
          if (l14 != 0L)
          {
            if (n != 0) {
              paramContext.append(", ");
            }
            m = 1;
            formatTimeMs(paramContext, l14);
            paramContext.append("blamed partial");
          }
          n = m;
          if (l3 != 0L)
          {
            if (m != 0) {
              paramContext.append(", ");
            }
            n = 1;
            formatTimeMs(paramContext, l3);
            paramContext.append("actual partial");
          }
          m = n;
          if (l5 != 0L)
          {
            if (n != 0) {
              paramContext.append(", ");
            }
            m = 1;
            formatTimeMs(paramContext, l5);
            paramContext.append("actual background partial");
          }
          n = m;
          if (l10 != 0L)
          {
            if (m != 0) {
              paramContext.append(", ");
            }
            n = 1;
            formatTimeMs(paramContext, l10);
            paramContext.append("window");
          }
          if (l12 != 0L)
          {
            if (n != 0) {
              paramContext.append(",");
            }
            formatTimeMs(paramContext, l12);
            paramContext.append("draw");
          }
          paramContext.append(" realtime");
          ((PrintWriter)localObject4).println(paramContext.toString());
        }
        label10883:
        localObject10 = paramString;
        localObject7 = ((Uid)localObject2).getMulticastWakelockStats();
        if (localObject7 != null)
        {
          m = paramInt1;
          l10 = ((Timer)localObject7).getTotalTimeLocked(l6, m);
          m = ((Timer)localObject7).getCountLocked(m);
          if (l10 > 0L)
          {
            paramContext.setLength(0);
            paramContext.append((String)localObject10);
            paramContext.append("    WiFi Multicast Wakelock");
            paramContext.append(" count = ");
            paramContext.append(m);
            paramContext.append(" time = ");
            formatTimeMsNoSpace(paramContext, (l10 + 500L) / 1000L);
            ((PrintWriter)localObject4).println(paramContext.toString());
          }
          else {}
        }
        l10 = l6;
        int i3 = paramInt1;
        localObject8 = ((Uid)localObject2).getSyncStats();
        m = ((ArrayMap)localObject8).size() - 1;
        l6 = l8;
        while (m >= 0)
        {
          localObject9 = (Timer)((ArrayMap)localObject8).valueAt(m);
          l8 = (((Timer)localObject9).getTotalTimeLocked(l10, i3) + 500L) / 1000L;
          n = ((Timer)localObject9).getCountLocked(i3);
          localObject9 = ((Timer)localObject9).getSubTimer();
          if (localObject9 != null) {
            l5 = ((Timer)localObject9).getTotalDurationMsLocked(l6);
          } else {
            l5 = -1L;
          }
          if (localObject9 != null) {
            k = ((Timer)localObject9).getCountLocked(i3);
          } else {
            k = -1;
          }
          paramContext.setLength(0);
          paramContext.append((String)localObject10);
          paramContext.append("    Sync ");
          paramContext.append((String)((ArrayMap)localObject8).keyAt(m));
          paramContext.append(": ");
          if (l8 != 0L)
          {
            formatTimeMs(paramContext, l8);
            paramContext.append("realtime (");
            paramContext.append(n);
            paramContext.append(" times)");
            if (l5 > 0L)
            {
              paramContext.append(", ");
              formatTimeMs(paramContext, l5);
              paramContext.append("background (");
              paramContext.append(k);
              paramContext.append(" times)");
            }
          }
          else
          {
            paramContext.append("(not used)");
          }
          ((PrintWriter)localObject4).println(paramContext.toString());
          k = 1;
          m--;
        }
        localObject7 = ((Uid)localObject2).getJobStats();
        for (m = ((ArrayMap)localObject7).size() - 1; m >= 0; m--)
        {
          localObject8 = (Timer)((ArrayMap)localObject7).valueAt(m);
          l8 = (((Timer)localObject8).getTotalTimeLocked(l10, i3) + 500L) / 1000L;
          n = ((Timer)localObject8).getCountLocked(i3);
          localObject8 = ((Timer)localObject8).getSubTimer();
          if (localObject8 != null) {
            l5 = ((Timer)localObject8).getTotalDurationMsLocked(l6);
          } else {
            l5 = -1L;
          }
          if (localObject8 != null) {
            k = ((Timer)localObject8).getCountLocked(i3);
          } else {
            k = -1;
          }
          paramContext.setLength(0);
          paramContext.append((String)localObject10);
          paramContext.append("    Job ");
          paramContext.append((String)((ArrayMap)localObject7).keyAt(m));
          paramContext.append(": ");
          if (l8 != 0L)
          {
            formatTimeMs(paramContext, l8);
            paramContext.append("realtime (");
            paramContext.append(n);
            paramContext.append(" times)");
            if (l5 > 0L)
            {
              paramContext.append(", ");
              formatTimeMs(paramContext, l5);
              paramContext.append("background (");
              paramContext.append(k);
              paramContext.append(" times)");
            }
          }
          else
          {
            paramContext.append("(not used)");
          }
          ((PrintWriter)localObject4).println(paramContext.toString());
          k = 1;
        }
        localObject7 = ((Uid)localObject2).getJobCompletionStats();
        for (m = ((ArrayMap)localObject7).size() - 1; m >= 0; m--)
        {
          localObject8 = (SparseIntArray)((ArrayMap)localObject7).valueAt(m);
          if (localObject8 != null)
          {
            paramPrintWriter.print(paramString);
            ((PrintWriter)localObject4).print("    Job Completions ");
            ((PrintWriter)localObject4).print((String)((ArrayMap)localObject7).keyAt(m));
            ((PrintWriter)localObject4).print(":");
            for (n = 0; n < ((SparseIntArray)localObject8).size(); n++)
            {
              ((PrintWriter)localObject4).print(" ");
              ((PrintWriter)localObject4).print(JobParameters.getReasonName(((SparseIntArray)localObject8).keyAt(n)));
              ((PrintWriter)localObject4).print("(");
              ((PrintWriter)localObject4).print(((SparseIntArray)localObject8).valueAt(n));
              ((PrintWriter)localObject4).print("x)");
            }
            paramPrintWriter.println();
          }
        }
        ((Uid)localObject2).getDeferredJobsLineLocked(paramContext, i3);
        if (paramContext.length() > 0)
        {
          ((PrintWriter)localObject4).print("    Jobs deferred on launch ");
          ((PrintWriter)localObject4).println(paramContext.toString());
        }
        localObject7 = ((Uid)localObject2).getFlashlightTurnedOnTimer();
        localObject9 = localObject2;
        l5 = l6;
        i2 = i;
        l6 = l10;
        localObject2 = paramContext;
        int i4 = printTimer((PrintWriter)localObject4, paramContext, (Timer)localObject7, l10, i3, (String)localObject10, "Flashlight");
        paramBoolean = printTimer(paramPrintWriter, (StringBuilder)localObject2, ((Uid)localObject9).getCameraTurnedOnTimer(), l6, i3, (String)localObject10, "Camera");
        int i6 = printTimer(paramPrintWriter, (StringBuilder)localObject2, ((Uid)localObject9).getVideoTurnedOnTimer(), l6, i3, (String)localObject10, "Video");
        int i7 = printTimer(paramPrintWriter, (StringBuilder)localObject2, ((Uid)localObject9).getAudioTurnedOnTimer(), l6, i3, (String)localObject10, "Audio");
        localObject4 = ((Uid)localObject9).getSensorStats();
        i = ((SparseArray)localObject4).size();
        n = k | i4 | paramBoolean | i6 | i7;
        m = 0;
        paramContext = (Context)localObject2;
        l10 = l5;
        k = i2;
        for (;;)
        {
          i2 = paramInt1;
          if (m >= i) {
            break;
          }
          localObject7 = (BatteryStats.Uid.Sensor)((SparseArray)localObject4).valueAt(m);
          ((SparseArray)localObject4).keyAt(m);
          paramContext.setLength(0);
          paramContext.append((String)localObject10);
          paramContext.append("    Sensor ");
          n = ((BatteryStats.Uid.Sensor)localObject7).getHandle();
          if (n == 55536) {
            paramContext.append("GPS");
          } else {
            paramContext.append(n);
          }
          paramContext.append(": ");
          localObject2 = ((BatteryStats.Uid.Sensor)localObject7).getSensorTime();
          if (localObject2 != null)
          {
            l12 = (((Timer)localObject2).getTotalTimeLocked(l6, i2) + 500L) / 1000L;
            i3 = ((Timer)localObject2).getCountLocked(i2);
            localObject7 = ((BatteryStats.Uid.Sensor)localObject7).getSensorBackgroundTime();
            if (localObject7 != null) {
              n = ((Timer)localObject7).getCountLocked(i2);
            } else {
              n = 0;
            }
            l8 = ((Timer)localObject2).getTotalDurationMsLocked(l10);
            if (localObject7 != null) {
              l5 = ((Timer)localObject7).getTotalDurationMsLocked(l10);
            } else {
              l5 = 0L;
            }
            if (l12 != 0L)
            {
              if (l8 != l12)
              {
                formatTimeMs(paramContext, l12);
                paramContext.append("blamed realtime, ");
              }
              formatTimeMs(paramContext, l8);
              paramContext.append("realtime (");
              paramContext.append(i3);
              paramContext.append(" times)");
              if ((l5 == 0L) && (n <= 0)) {
                break label12196;
              }
              paramContext.append(", ");
              formatTimeMs(paramContext, l5);
              paramContext.append("background (");
              paramContext.append(n);
              paramContext.append(" times)");
            }
            else
            {
              paramContext.append("(not used)");
            }
          }
          else
          {
            label12196:
            paramContext.append("(not used)");
          }
          paramPrintWriter.println(paramContext.toString());
          n = 1;
          m++;
        }
        localObject2 = paramPrintWriter;
        m = k;
        l12 = l10;
        localObject7 = localObject3;
        localObject11 = ((Uid)localObject9).getVibratorOnTimer();
        localObject8 = paramContext;
        i2 = paramInt1;
        k = i;
        localObject3 = localObject4;
        int i8 = printTimer((PrintWriter)localObject2, paramContext, (Timer)localObject11, l6, i2, (String)localObject10, "Vibrator");
        paramBoolean = printTimer((PrintWriter)localObject2, (StringBuilder)localObject8, ((Uid)localObject9).getForegroundActivityTimer(), l6, i2, (String)localObject10, "Foreground activities");
        int i5 = printTimer((PrintWriter)localObject2, (StringBuilder)localObject8, ((Uid)localObject9).getForegroundServiceTimer(), l6, i2, (String)localObject10, "Foreground services");
        l10 = 0L;
        int j = n | i8 | paramBoolean | i5;
        for (n = 0; n < 7; n++)
        {
          l5 = ((Uid)localObject9).getProcessStateTime(n, l6, i2);
          if (l5 > 0L)
          {
            l10 += l5;
            ((StringBuilder)localObject8).setLength(0);
            ((StringBuilder)localObject8).append((String)localObject10);
            ((StringBuilder)localObject8).append("    ");
            ((StringBuilder)localObject8).append(Uid.PROCESS_STATE_NAMES[n]);
            ((StringBuilder)localObject8).append(" for: ");
            formatTimeMs((StringBuilder)localObject8, (l5 + 500L) / 1000L);
            ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
            j = 1;
          }
        }
        if (l10 > 0L)
        {
          ((StringBuilder)localObject8).setLength(0);
          ((StringBuilder)localObject8).append((String)localObject10);
          ((StringBuilder)localObject8).append("    Total running: ");
          formatTimeMs((StringBuilder)localObject8, (l10 + 500L) / 1000L);
          ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
        }
        l10 = ((Uid)localObject9).getUserCpuTimeUs(i2);
        l5 = ((Uid)localObject9).getSystemCpuTimeUs(i2);
        if ((l10 <= 0L) && (l5 <= 0L)) {
          break label12617;
        }
        ((StringBuilder)localObject8).setLength(0);
        ((StringBuilder)localObject8).append((String)localObject10);
        ((StringBuilder)localObject8).append("    Total cpu time: u=");
        formatTimeMs((StringBuilder)localObject8, l10 / 1000L);
        ((StringBuilder)localObject8).append("s=");
        formatTimeMs((StringBuilder)localObject8, l5 / 1000L);
        ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
        label12617:
        localObject10 = ((Uid)localObject9).getCpuFreqTimes(i2);
        if (localObject10 != null)
        {
          ((StringBuilder)localObject8).setLength(0);
          ((StringBuilder)localObject8).append("    Total cpu time per freq:");
          for (k = 0; k < localObject10.length; k++)
          {
            paramContext = new StringBuilder();
            paramContext.append(" ");
            paramContext.append(localObject10[k]);
            ((StringBuilder)localObject8).append(paramContext.toString());
          }
          ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
        }
        localObject4 = ((Uid)localObject9).getScreenOffCpuFreqTimes(i2);
        if (localObject4 != null)
        {
          ((StringBuilder)localObject8).setLength(0);
          ((StringBuilder)localObject8).append("    Total screen-off cpu time per freq:");
          for (k = 0; k < localObject4.length; k++)
          {
            paramContext = new StringBuilder();
            paramContext.append(" ");
            paramContext.append(localObject4[k]);
            ((StringBuilder)localObject8).append(paramContext.toString());
          }
          ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
        }
        k = 0;
        l10 = l5;
        while (k < 7)
        {
          paramContext = ((Uid)localObject9).getCpuFreqTimes(i2, k);
          if (paramContext != null)
          {
            ((StringBuilder)localObject8).setLength(0);
            localObject3 = new StringBuilder();
            ((StringBuilder)localObject3).append("    Cpu times per freq at state ");
            ((StringBuilder)localObject3).append(Uid.PROCESS_STATE_NAMES[k]);
            ((StringBuilder)localObject3).append(":");
            ((StringBuilder)localObject8).append(((StringBuilder)localObject3).toString());
            for (n = 0; n < paramContext.length; n++)
            {
              localObject3 = new StringBuilder();
              ((StringBuilder)localObject3).append(" ");
              ((StringBuilder)localObject3).append(paramContext[n]);
              ((StringBuilder)localObject8).append(((StringBuilder)localObject3).toString());
            }
            ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
          }
          paramContext = ((Uid)localObject9).getScreenOffCpuFreqTimes(i2, k);
          if (paramContext != null)
          {
            ((StringBuilder)localObject8).setLength(0);
            localObject3 = new StringBuilder();
            ((StringBuilder)localObject3).append("   Screen-off cpu times per freq at state ");
            ((StringBuilder)localObject3).append(Uid.PROCESS_STATE_NAMES[k]);
            ((StringBuilder)localObject3).append(":");
            ((StringBuilder)localObject8).append(((StringBuilder)localObject3).toString());
            for (n = 0; n < paramContext.length; n++)
            {
              localObject3 = new StringBuilder();
              ((StringBuilder)localObject3).append(" ");
              ((StringBuilder)localObject3).append(paramContext[n]);
              ((StringBuilder)localObject8).append(((StringBuilder)localObject3).toString());
            }
            ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
          }
          k++;
        }
        l9 = l6;
        paramContext = ((Uid)localObject9).getProcessStats();
        n = paramContext.size() - 1;
        localObject3 = localObject9;
        localObject9 = localObject10;
        for (;;)
        {
          localObject11 = paramString;
          k = paramInt1;
          if (n < 0) {
            break;
          }
          localObject10 = (BatteryStats.Uid.Proc)paramContext.valueAt(n);
          l5 = ((BatteryStats.Uid.Proc)localObject10).getUserTime(k);
          l10 = ((BatteryStats.Uid.Proc)localObject10).getSystemTime(k);
          l6 = ((BatteryStats.Uid.Proc)localObject10).getForegroundTime(k);
          i2 = ((BatteryStats.Uid.Proc)localObject10).getStarts(k);
          i3 = ((BatteryStats.Uid.Proc)localObject10).getNumCrashes(k);
          int i9 = ((BatteryStats.Uid.Proc)localObject10).getNumAnrs(k);
          if (k == 0) {
            k = ((BatteryStats.Uid.Proc)localObject10).countExcessivePowers();
          } else {
            k = 0;
          }
          if ((l5 == 0L) && (l10 == 0L) && (l6 == 0L) && (i2 == 0) && (k == 0) && (i3 == 0) && (i9 == 0)) {
            break label13722;
          }
          ((StringBuilder)localObject8).setLength(0);
          ((StringBuilder)localObject8).append((String)localObject11);
          ((StringBuilder)localObject8).append("    Proc ");
          ((StringBuilder)localObject8).append((String)paramContext.keyAt(n));
          ((StringBuilder)localObject8).append(":\n");
          ((StringBuilder)localObject8).append((String)localObject11);
          ((StringBuilder)localObject8).append("      CPU: ");
          formatTimeMs((StringBuilder)localObject8, l5);
          ((StringBuilder)localObject8).append("usr + ");
          formatTimeMs((StringBuilder)localObject8, l10);
          ((StringBuilder)localObject8).append("krn ; ");
          formatTimeMs((StringBuilder)localObject8, l6);
          ((StringBuilder)localObject8).append("fg");
          if ((i2 == 0) && (i3 == 0) && (i9 == 0)) {
            break label13539;
          }
          ((StringBuilder)localObject8).append("\n");
          ((StringBuilder)localObject8).append((String)localObject11);
          ((StringBuilder)localObject8).append("      ");
          j = 0;
          if (i2 != 0)
          {
            j = 1;
            ((StringBuilder)localObject8).append(i2);
            ((StringBuilder)localObject8).append(" starts");
          }
          i2 = j;
          if (i3 != 0)
          {
            if (j != 0) {
              ((StringBuilder)localObject8).append(", ");
            }
            i2 = 1;
            ((StringBuilder)localObject8).append(i3);
            ((StringBuilder)localObject8).append(" crashes");
          }
          if (i9 != 0)
          {
            if (i2 != 0) {
              ((StringBuilder)localObject8).append(", ");
            }
            ((StringBuilder)localObject8).append(i9);
            ((StringBuilder)localObject8).append(" anrs");
          }
          label13539:
          ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
          int i10 = 0;
          j = i9;
          i2 = i3;
          for (i3 = i10; i3 < k; i3++)
          {
            localObject11 = ((BatteryStats.Uid.Proc)localObject10).getExcessivePower(i3);
            if (localObject11 != null)
            {
              paramPrintWriter.print(paramString);
              ((PrintWriter)localObject2).print("      * Killed for ");
              if (type == 2) {
                ((PrintWriter)localObject2).print("cpu");
              } else {
                ((PrintWriter)localObject2).print("unknown");
              }
              ((PrintWriter)localObject2).print(" use: ");
              TimeUtils.formatDuration(usedTime, (PrintWriter)localObject2);
              ((PrintWriter)localObject2).print(" over ");
              TimeUtils.formatDuration(overTime, (PrintWriter)localObject2);
              if (overTime != 0L)
              {
                ((PrintWriter)localObject2).print(" (");
                ((PrintWriter)localObject2).print(usedTime * 100L / overTime);
                ((PrintWriter)localObject2).println("%)");
              }
              else {}
            }
          }
          j = 1;
          label13722:
          n--;
        }
        paramContext = ((Uid)localObject3).getPackageStats();
        k = paramContext.size() - 1;
        n = j;
        while (k >= 0)
        {
          paramPrintWriter.print(paramString);
          ((PrintWriter)localObject2).print("    Apk ");
          ((PrintWriter)localObject2).print((String)paramContext.keyAt(k));
          ((PrintWriter)localObject2).println(":");
          j = 0;
          localObject9 = (BatteryStats.Uid.Pkg)paramContext.valueAt(k);
          localObject4 = ((BatteryStats.Uid.Pkg)localObject9).getWakeupAlarmStats();
          for (n = ((ArrayMap)localObject4).size() - 1; n >= 0; n--)
          {
            paramPrintWriter.print(paramString);
            ((PrintWriter)localObject2).print("      Wakeup alarm ");
            ((PrintWriter)localObject2).print((String)((ArrayMap)localObject4).keyAt(n));
            ((PrintWriter)localObject2).print(": ");
            ((PrintWriter)localObject2).print(((Counter)((ArrayMap)localObject4).valueAt(n)).getCountLocked(paramInt1));
            ((PrintWriter)localObject2).println(" times");
            j = 1;
          }
          localObject10 = ((BatteryStats.Uid.Pkg)localObject9).getServiceStats();
          for (n = ((ArrayMap)localObject10).size() - 1; n >= 0; n--)
          {
            localObject11 = (BatteryStats.Uid.Pkg.Serv)((ArrayMap)localObject10).valueAt(n);
            l6 = ((BatteryStats.Uid.Pkg.Serv)localObject11).getStartTime(l1, paramInt1);
            i3 = ((BatteryStats.Uid.Pkg.Serv)localObject11).getStarts(paramInt1);
            i2 = ((BatteryStats.Uid.Pkg.Serv)localObject11).getLaunches(paramInt1);
            if ((l6 == 0L) && (i3 == 0) && (i2 == 0)) {
              continue;
            }
            ((StringBuilder)localObject8).setLength(0);
            ((StringBuilder)localObject8).append(paramString);
            ((StringBuilder)localObject8).append("      Service ");
            ((StringBuilder)localObject8).append((String)((ArrayMap)localObject10).keyAt(n));
            ((StringBuilder)localObject8).append(":\n");
            ((StringBuilder)localObject8).append(paramString);
            ((StringBuilder)localObject8).append("        Created for: ");
            formatTimeMs((StringBuilder)localObject8, l6 / 1000L);
            ((StringBuilder)localObject8).append("uptime\n");
            ((StringBuilder)localObject8).append(paramString);
            ((StringBuilder)localObject8).append("        Starts: ");
            ((StringBuilder)localObject8).append(i3);
            ((StringBuilder)localObject8).append(", launches: ");
            ((StringBuilder)localObject8).append(i2);
            ((PrintWriter)localObject2).println(((StringBuilder)localObject8).toString());
            j = 1;
          }
          if (j == 0)
          {
            paramPrintWriter.print(paramString);
            ((PrintWriter)localObject2).println("      (nothing executed)");
          }
          n = 1;
          k--;
        }
        l14 = l1;
        localObject4 = localObject2;
        paramContext = (Context)localObject8;
        l8 = l14;
        l6 = l7;
        l10 = l11;
        j = m;
        localObject3 = localObject7;
        l1 = l12;
        l5 = l9;
        if (n == 0)
        {
          paramPrintWriter.print(paramString);
          ((PrintWriter)localObject2).println("    (nothing executed)");
          l5 = l9;
          l1 = l12;
          localObject3 = localObject7;
          j = m;
          l10 = l11;
          l6 = l7;
          l8 = l14;
          paramContext = (Context)localObject8;
          localObject4 = localObject2;
        }
      }
      i1++;
      l11 = l8;
      l7 = l6;
      l6 = l1;
      l8 = l5;
      l1 = l11;
      l5 = l7;
    }
  }
  
  public void dumpProtoLocked(Context paramContext, FileDescriptor paramFileDescriptor, List<ApplicationInfo> paramList, int paramInt, long paramLong)
  {
    paramFileDescriptor = new ProtoOutputStream(paramFileDescriptor);
    prepareForDumpLocked();
    if ((paramInt & 0x18) != 0)
    {
      dumpProtoHistoryLocked(paramFileDescriptor, paramInt, paramLong);
      paramFileDescriptor.flush();
      return;
    }
    paramLong = paramFileDescriptor.start(1146756268033L);
    paramFileDescriptor.write(1120986464257L, 32);
    paramFileDescriptor.write(1112396529666L, getParcelVersion());
    paramFileDescriptor.write(1138166333443L, getStartPlatformVersion());
    paramFileDescriptor.write(1138166333444L, getEndPlatformVersion());
    if ((paramInt & 0x4) == 0)
    {
      boolean bool;
      if ((paramInt & 0x40) != 0) {
        bool = true;
      } else {
        bool = false;
      }
      paramContext = new BatteryStatsHelper(paramContext, false, bool);
      paramContext.create(this);
      paramContext.refreshStats(0, -1);
      dumpProtoAppsLocked(paramFileDescriptor, paramContext, paramList);
      dumpProtoSystemLocked(paramFileDescriptor, paramContext);
    }
    paramFileDescriptor.end(paramLong);
    paramFileDescriptor.flush();
  }
  
  public abstract void finishIteratingHistoryLocked();
  
  public abstract void finishIteratingOldHistoryLocked();
  
  final String formatBytesLocked(long paramLong)
  {
    mFormatBuilder.setLength(0);
    if (paramLong < 1024L)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramLong);
      localStringBuilder.append("B");
      return localStringBuilder.toString();
    }
    if (paramLong < 1048576L)
    {
      mFormatter.format("%.2fKB", new Object[] { Double.valueOf(paramLong / 1024.0D) });
      return mFormatBuilder.toString();
    }
    if (paramLong < 1073741824L)
    {
      mFormatter.format("%.2fMB", new Object[] { Double.valueOf(paramLong / 1048576.0D) });
      return mFormatBuilder.toString();
    }
    mFormatter.format("%.2fGB", new Object[] { Double.valueOf(paramLong / 1.073741824E9D) });
    return mFormatBuilder.toString();
  }
  
  public final String formatRatioLocked(long paramLong1, long paramLong2)
  {
    if (paramLong2 == 0L) {
      return "--%";
    }
    float f = (float)paramLong1 / (float)paramLong2;
    mFormatBuilder.setLength(0);
    mFormatter.format("%.1f%%", new Object[] { Float.valueOf(f * 100.0F) });
    return mFormatBuilder.toString();
  }
  
  public abstract long getBatteryRealtime(long paramLong);
  
  public abstract long getBatteryUptime(long paramLong);
  
  public abstract ControllerActivityCounter getBluetoothControllerActivity();
  
  public abstract long getBluetoothScanTime(long paramLong, int paramInt);
  
  public abstract long getCameraOnTime(long paramLong, int paramInt);
  
  public abstract LevelStepTracker getChargeLevelStepTracker();
  
  public abstract long[] getCpuFreqs();
  
  public abstract long getCurrentDailyStartTime();
  
  public abstract LevelStepTracker getDailyChargeLevelStepTracker();
  
  public abstract LevelStepTracker getDailyDischargeLevelStepTracker();
  
  public abstract DailyItem getDailyItemLocked(int paramInt);
  
  public abstract ArrayList<PackageChange> getDailyPackageChanges();
  
  public abstract int getDeviceIdleModeCount(int paramInt1, int paramInt2);
  
  public abstract long getDeviceIdleModeTime(int paramInt1, long paramLong, int paramInt2);
  
  public abstract int getDeviceIdlingCount(int paramInt1, int paramInt2);
  
  public abstract long getDeviceIdlingTime(int paramInt1, long paramLong, int paramInt2);
  
  public abstract int getDischargeAmount(int paramInt);
  
  public abstract int getDischargeAmountScreenDoze();
  
  public abstract int getDischargeAmountScreenDozeSinceCharge();
  
  public abstract int getDischargeAmountScreenOff();
  
  public abstract int getDischargeAmountScreenOffSinceCharge();
  
  public abstract int getDischargeAmountScreenOn();
  
  public abstract int getDischargeAmountScreenOnSinceCharge();
  
  public abstract int getDischargeCurrentLevel();
  
  public abstract LevelStepTracker getDischargeLevelStepTracker();
  
  public abstract int getDischargeStartLevel();
  
  public abstract String getEndPlatformVersion();
  
  public abstract int getEstimatedBatteryCapacity();
  
  public abstract long getFlashlightOnCount(int paramInt);
  
  public abstract long getFlashlightOnTime(long paramLong, int paramInt);
  
  public abstract long getGlobalWifiRunningTime(long paramLong, int paramInt);
  
  public abstract long getGpsBatteryDrainMaMs();
  
  public abstract long getGpsSignalQualityTime(int paramInt1, long paramLong, int paramInt2);
  
  public abstract int getHighDischargeAmountSinceCharge();
  
  public abstract long getHistoryBaseTime();
  
  public abstract int getHistoryStringPoolBytes();
  
  public abstract int getHistoryStringPoolSize();
  
  public abstract String getHistoryTagPoolString(int paramInt);
  
  public abstract int getHistoryTagPoolUid(int paramInt);
  
  public abstract int getHistoryTotalSize();
  
  public abstract int getHistoryUsedSize();
  
  public abstract long getInteractiveTime(long paramLong, int paramInt);
  
  public abstract boolean getIsOnBattery();
  
  public abstract LongSparseArray<? extends Timer> getKernelMemoryStats();
  
  public abstract Map<String, ? extends Timer> getKernelWakelockStats();
  
  public abstract long getLongestDeviceIdleModeTime(int paramInt);
  
  public abstract int getLowDischargeAmountSinceCharge();
  
  public abstract int getMaxLearnedBatteryCapacity();
  
  public abstract int getMinLearnedBatteryCapacity();
  
  public abstract long getMobileRadioActiveAdjustedTime(int paramInt);
  
  public abstract int getMobileRadioActiveCount(int paramInt);
  
  public abstract long getMobileRadioActiveTime(long paramLong, int paramInt);
  
  public abstract int getMobileRadioActiveUnknownCount(int paramInt);
  
  public abstract long getMobileRadioActiveUnknownTime(int paramInt);
  
  public abstract ControllerActivityCounter getModemControllerActivity();
  
  public abstract long getNetworkActivityBytes(int paramInt1, int paramInt2);
  
  public abstract long getNetworkActivityPackets(int paramInt1, int paramInt2);
  
  public abstract boolean getNextHistoryLocked(HistoryItem paramHistoryItem);
  
  public abstract long getNextMaxDailyDeadline();
  
  public abstract long getNextMinDailyDeadline();
  
  public abstract boolean getNextOldHistoryLocked(HistoryItem paramHistoryItem);
  
  public abstract int getNumConnectivityChange(int paramInt);
  
  public abstract int getParcelVersion();
  
  public abstract int getPhoneDataConnectionCount(int paramInt1, int paramInt2);
  
  public abstract long getPhoneDataConnectionTime(int paramInt1, long paramLong, int paramInt2);
  
  public abstract Timer getPhoneDataConnectionTimer(int paramInt);
  
  public abstract int getPhoneOnCount(int paramInt);
  
  public abstract long getPhoneOnTime(long paramLong, int paramInt);
  
  public abstract long getPhoneSignalScanningTime(long paramLong, int paramInt);
  
  public abstract Timer getPhoneSignalScanningTimer();
  
  public abstract int getPhoneSignalStrengthCount(int paramInt1, int paramInt2);
  
  public abstract long getPhoneSignalStrengthTime(int paramInt1, long paramLong, int paramInt2);
  
  protected abstract Timer getPhoneSignalStrengthTimer(int paramInt);
  
  public abstract int getPowerSaveModeEnabledCount(int paramInt);
  
  public abstract long getPowerSaveModeEnabledTime(long paramLong, int paramInt);
  
  public abstract Map<String, ? extends Timer> getRpmStats();
  
  public abstract long getScreenBrightnessTime(int paramInt1, long paramLong, int paramInt2);
  
  public abstract Timer getScreenBrightnessTimer(int paramInt);
  
  public abstract int getScreenDozeCount(int paramInt);
  
  public abstract long getScreenDozeTime(long paramLong, int paramInt);
  
  public abstract Map<String, ? extends Timer> getScreenOffRpmStats();
  
  public abstract int getScreenOnCount(int paramInt);
  
  public abstract long getScreenOnTime(long paramLong, int paramInt);
  
  public abstract long getStartClockTime();
  
  public abstract int getStartCount();
  
  public abstract String getStartPlatformVersion();
  
  public abstract long getUahDischarge(int paramInt);
  
  public abstract long getUahDischargeDeepDoze(int paramInt);
  
  public abstract long getUahDischargeLightDoze(int paramInt);
  
  public abstract long getUahDischargeScreenDoze(int paramInt);
  
  public abstract long getUahDischargeScreenOff(int paramInt);
  
  public abstract SparseArray<? extends Uid> getUidStats();
  
  public abstract Map<String, ? extends Timer> getWakeupReasonStats();
  
  public abstract long getWifiActiveTime(long paramLong, int paramInt);
  
  public abstract ControllerActivityCounter getWifiControllerActivity();
  
  public abstract int getWifiMulticastWakelockCount(int paramInt);
  
  public abstract long getWifiMulticastWakelockTime(long paramLong, int paramInt);
  
  public abstract long getWifiOnTime(long paramLong, int paramInt);
  
  public abstract int getWifiSignalStrengthCount(int paramInt1, int paramInt2);
  
  public abstract long getWifiSignalStrengthTime(int paramInt1, long paramLong, int paramInt2);
  
  public abstract Timer getWifiSignalStrengthTimer(int paramInt);
  
  public abstract int getWifiStateCount(int paramInt1, int paramInt2);
  
  public abstract long getWifiStateTime(int paramInt1, long paramLong, int paramInt2);
  
  public abstract Timer getWifiStateTimer(int paramInt);
  
  public abstract int getWifiSupplStateCount(int paramInt1, int paramInt2);
  
  public abstract long getWifiSupplStateTime(int paramInt1, long paramLong, int paramInt2);
  
  public abstract Timer getWifiSupplStateTimer(int paramInt);
  
  public abstract boolean hasBluetoothActivityReporting();
  
  public abstract boolean hasModemActivityReporting();
  
  public abstract boolean hasWifiActivityReporting();
  
  public void prepareForDumpLocked() {}
  
  public abstract boolean startIteratingHistoryLocked();
  
  public abstract boolean startIteratingOldHistoryLocked();
  
  public abstract void writeToParcelWithoutUids(Parcel paramParcel, int paramInt);
  
  public static final class BitDescription
  {
    public final int mask;
    public final String name;
    public final int shift;
    public final String shortName;
    public final String[] shortValues;
    public final String[] values;
    
    public BitDescription(int paramInt1, int paramInt2, String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
    {
      mask = paramInt1;
      shift = paramInt2;
      name = paramString1;
      shortName = paramString2;
      values = paramArrayOfString1;
      shortValues = paramArrayOfString2;
    }
    
    public BitDescription(int paramInt, String paramString1, String paramString2)
    {
      mask = paramInt;
      shift = -1;
      name = paramString1;
      shortName = paramString2;
      values = null;
      shortValues = null;
    }
  }
  
  public static abstract class ControllerActivityCounter
  {
    public ControllerActivityCounter() {}
    
    public abstract BatteryStats.LongCounter getIdleTimeCounter();
    
    public abstract BatteryStats.LongCounter getPowerCounter();
    
    public abstract BatteryStats.LongCounter getRxTimeCounter();
    
    public abstract BatteryStats.LongCounter getScanTimeCounter();
    
    public abstract BatteryStats.LongCounter getSleepTimeCounter();
    
    public abstract BatteryStats.LongCounter[] getTxTimeCounters();
  }
  
  public static abstract class Counter
  {
    public Counter() {}
    
    public abstract int getCountLocked(int paramInt);
    
    public abstract void logState(Printer paramPrinter, String paramString);
  }
  
  public static final class DailyItem
  {
    public BatteryStats.LevelStepTracker mChargeSteps;
    public BatteryStats.LevelStepTracker mDischargeSteps;
    public long mEndTime;
    public ArrayList<BatteryStats.PackageChange> mPackageChanges;
    public long mStartTime;
    
    public DailyItem() {}
  }
  
  public static final class HistoryEventTracker
  {
    private final HashMap<String, SparseIntArray>[] mActiveEvents = new HashMap[22];
    
    public HistoryEventTracker() {}
    
    public HashMap<String, SparseIntArray> getStateForEvent(int paramInt)
    {
      return mActiveEvents[paramInt];
    }
    
    public void removeEvents(int paramInt)
    {
      mActiveEvents[(0xFFFF3FFF & paramInt)] = null;
    }
    
    public boolean updateState(int paramInt1, String paramString, int paramInt2, int paramInt3)
    {
      Object localObject1;
      Object localObject2;
      if ((0x8000 & paramInt1) != 0)
      {
        paramInt1 &= 0xFFFF3FFF;
        localObject1 = mActiveEvents[paramInt1];
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = new HashMap();
          mActiveEvents[paramInt1] = localObject2;
        }
        SparseIntArray localSparseIntArray = (SparseIntArray)((HashMap)localObject2).get(paramString);
        localObject1 = localSparseIntArray;
        if (localSparseIntArray == null)
        {
          localObject1 = new SparseIntArray();
          ((HashMap)localObject2).put(paramString, localObject1);
        }
        if (((SparseIntArray)localObject1).indexOfKey(paramInt2) >= 0) {
          return false;
        }
        ((SparseIntArray)localObject1).put(paramInt2, paramInt3);
      }
      else if ((paramInt1 & 0x4000) != 0)
      {
        localObject2 = mActiveEvents[(paramInt1 & 0xFFFF3FFF)];
        if (localObject2 == null) {
          return false;
        }
        localObject1 = (SparseIntArray)((HashMap)localObject2).get(paramString);
        if (localObject1 == null) {
          return false;
        }
        paramInt1 = ((SparseIntArray)localObject1).indexOfKey(paramInt2);
        if (paramInt1 < 0) {
          return false;
        }
        ((SparseIntArray)localObject1).removeAt(paramInt1);
        if (((SparseIntArray)localObject1).size() <= 0) {
          ((HashMap)localObject2).remove(paramString);
        }
      }
      return true;
    }
  }
  
  public static final class HistoryItem
    implements Parcelable
  {
    public static final byte CMD_CURRENT_TIME = 5;
    public static final byte CMD_NULL = -1;
    public static final byte CMD_OVERFLOW = 6;
    public static final byte CMD_RESET = 7;
    public static final byte CMD_SHUTDOWN = 8;
    public static final byte CMD_START = 4;
    public static final byte CMD_UPDATE = 0;
    public static final int EVENT_ACTIVE = 10;
    public static final int EVENT_ALARM = 13;
    public static final int EVENT_ALARM_FINISH = 16397;
    public static final int EVENT_ALARM_START = 32781;
    public static final int EVENT_COLLECT_EXTERNAL_STATS = 14;
    public static final int EVENT_CONNECTIVITY_CHANGED = 9;
    public static final int EVENT_COUNT = 22;
    public static final int EVENT_FLAG_FINISH = 16384;
    public static final int EVENT_FLAG_START = 32768;
    public static final int EVENT_FOREGROUND = 2;
    public static final int EVENT_FOREGROUND_FINISH = 16386;
    public static final int EVENT_FOREGROUND_START = 32770;
    public static final int EVENT_JOB = 6;
    public static final int EVENT_JOB_FINISH = 16390;
    public static final int EVENT_JOB_START = 32774;
    public static final int EVENT_LONG_WAKE_LOCK = 20;
    public static final int EVENT_LONG_WAKE_LOCK_FINISH = 16404;
    public static final int EVENT_LONG_WAKE_LOCK_START = 32788;
    public static final int EVENT_NONE = 0;
    public static final int EVENT_PACKAGE_ACTIVE = 16;
    public static final int EVENT_PACKAGE_INACTIVE = 15;
    public static final int EVENT_PACKAGE_INSTALLED = 11;
    public static final int EVENT_PACKAGE_UNINSTALLED = 12;
    public static final int EVENT_PROC = 1;
    public static final int EVENT_PROC_FINISH = 16385;
    public static final int EVENT_PROC_START = 32769;
    public static final int EVENT_SCREEN_WAKE_UP = 18;
    public static final int EVENT_SYNC = 4;
    public static final int EVENT_SYNC_FINISH = 16388;
    public static final int EVENT_SYNC_START = 32772;
    public static final int EVENT_TEMP_WHITELIST = 17;
    public static final int EVENT_TEMP_WHITELIST_FINISH = 16401;
    public static final int EVENT_TEMP_WHITELIST_START = 32785;
    public static final int EVENT_TOP = 3;
    public static final int EVENT_TOP_FINISH = 16387;
    public static final int EVENT_TOP_START = 32771;
    public static final int EVENT_TYPE_MASK = -49153;
    public static final int EVENT_USER_FOREGROUND = 8;
    public static final int EVENT_USER_FOREGROUND_FINISH = 16392;
    public static final int EVENT_USER_FOREGROUND_START = 32776;
    public static final int EVENT_USER_RUNNING = 7;
    public static final int EVENT_USER_RUNNING_FINISH = 16391;
    public static final int EVENT_USER_RUNNING_START = 32775;
    public static final int EVENT_WAKEUP_AP = 19;
    public static final int EVENT_WAKE_LOCK = 5;
    public static final int EVENT_WAKE_LOCK_FINISH = 16389;
    public static final int EVENT_WAKE_LOCK_START = 32773;
    public static final int MOST_INTERESTING_STATES = 1835008;
    public static final int MOST_INTERESTING_STATES2 = -1749024768;
    public static final int SETTLE_TO_ZERO_STATES = -1900544;
    public static final int SETTLE_TO_ZERO_STATES2 = 1748959232;
    public static final int STATE2_BLUETOOTH_ON_FLAG = 4194304;
    public static final int STATE2_BLUETOOTH_SCAN_FLAG = 1048576;
    public static final int STATE2_CAMERA_FLAG = 2097152;
    public static final int STATE2_CELLULAR_HIGH_TX_POWER_FLAG = 524288;
    public static final int STATE2_CHARGING_FLAG = 16777216;
    public static final int STATE2_DEVICE_IDLE_MASK = 100663296;
    public static final int STATE2_DEVICE_IDLE_SHIFT = 25;
    public static final int STATE2_FLASHLIGHT_FLAG = 134217728;
    public static final int STATE2_GPS_SIGNAL_QUALITY_MASK = 128;
    public static final int STATE2_GPS_SIGNAL_QUALITY_SHIFT = 7;
    public static final int STATE2_PHONE_IN_CALL_FLAG = 8388608;
    public static final int STATE2_POWER_SAVE_FLAG = Integer.MIN_VALUE;
    public static final int STATE2_SOFTAP_ON_FLAG = 131072;
    public static final int STATE2_USB_DATA_LINK_FLAG = 262144;
    public static final int STATE2_VIDEO_ON_FLAG = 1073741824;
    public static final int STATE2_WIFI_ON_FLAG = 268435456;
    public static final int STATE2_WIFI_RUNNING_FLAG = 536870912;
    public static final int STATE2_WIFI_SIGNAL_STRENGTH_MASK = 112;
    public static final int STATE2_WIFI_SIGNAL_STRENGTH_SHIFT = 4;
    public static final int STATE2_WIFI_SUPPL_STATE_MASK = 15;
    public static final int STATE2_WIFI_SUPPL_STATE_SHIFT = 0;
    public static final int STATE_AUDIO_ON_FLAG = 4194304;
    public static final int STATE_BATTERY_PLUGGED_FLAG = 524288;
    public static final int STATE_BRIGHTNESS_MASK = 7;
    public static final int STATE_BRIGHTNESS_SHIFT = 0;
    public static final int STATE_CPU_RUNNING_FLAG = Integer.MIN_VALUE;
    public static final int STATE_DATA_CONNECTION_MASK = 15872;
    public static final int STATE_DATA_CONNECTION_SHIFT = 9;
    public static final int STATE_GPS_ON_FLAG = 536870912;
    public static final int STATE_MOBILE_RADIO_ACTIVE_FLAG = 33554432;
    public static final int STATE_PHONE_SCANNING_FLAG = 2097152;
    public static final int STATE_PHONE_SIGNAL_STRENGTH_MASK = 56;
    public static final int STATE_PHONE_SIGNAL_STRENGTH_SHIFT = 3;
    public static final int STATE_PHONE_STATE_MASK = 448;
    public static final int STATE_PHONE_STATE_SHIFT = 6;
    private static final int STATE_RESERVED_0 = 16777216;
    public static final int STATE_SCREEN_DOZE_FLAG = 262144;
    public static final int STATE_SCREEN_ON_FLAG = 1048576;
    public static final int STATE_SENSOR_ON_FLAG = 8388608;
    public static final int STATE_WAKE_LOCK_FLAG = 1073741824;
    public static final int STATE_WIFI_FULL_LOCK_FLAG = 268435456;
    public static final int STATE_WIFI_MULTICAST_ON_FLAG = 65536;
    public static final int STATE_WIFI_RADIO_ACTIVE_FLAG = 67108864;
    public static final int STATE_WIFI_SCAN_FLAG = 134217728;
    public int batteryChargeUAh;
    public byte batteryHealth;
    public byte batteryLevel;
    public byte batteryPlugType;
    public byte batteryStatus;
    public short batteryTemperature;
    public char batteryVoltage;
    public byte cmd = (byte)-1;
    public long currentTime;
    public int eventCode;
    public BatteryStats.HistoryTag eventTag;
    public final BatteryStats.HistoryTag localEventTag = new BatteryStats.HistoryTag();
    public final BatteryStats.HistoryTag localWakeReasonTag = new BatteryStats.HistoryTag();
    public final BatteryStats.HistoryTag localWakelockTag = new BatteryStats.HistoryTag();
    public HistoryItem next;
    public int numReadInts;
    public int states;
    public int states2;
    public BatteryStats.HistoryStepDetails stepDetails;
    public long time;
    public BatteryStats.HistoryTag wakeReasonTag;
    public BatteryStats.HistoryTag wakelockTag;
    
    public HistoryItem() {}
    
    public HistoryItem(long paramLong, Parcel paramParcel)
    {
      time = paramLong;
      numReadInts = 2;
      readFromParcel(paramParcel);
    }
    
    private void setToCommon(HistoryItem paramHistoryItem)
    {
      batteryLevel = ((byte)batteryLevel);
      batteryStatus = ((byte)batteryStatus);
      batteryHealth = ((byte)batteryHealth);
      batteryPlugType = ((byte)batteryPlugType);
      batteryTemperature = ((short)batteryTemperature);
      batteryVoltage = ((char)batteryVoltage);
      batteryChargeUAh = batteryChargeUAh;
      states = states;
      states2 = states2;
      if (wakelockTag != null)
      {
        wakelockTag = localWakelockTag;
        wakelockTag.setTo(wakelockTag);
      }
      else
      {
        wakelockTag = null;
      }
      if (wakeReasonTag != null)
      {
        wakeReasonTag = localWakeReasonTag;
        wakeReasonTag.setTo(wakeReasonTag);
      }
      else
      {
        wakeReasonTag = null;
      }
      eventCode = eventCode;
      if (eventTag != null)
      {
        eventTag = localEventTag;
        eventTag.setTo(eventTag);
      }
      else
      {
        eventTag = null;
      }
      currentTime = currentTime;
    }
    
    public void clear()
    {
      time = 0L;
      cmd = ((byte)-1);
      batteryLevel = ((byte)0);
      batteryStatus = ((byte)0);
      batteryHealth = ((byte)0);
      batteryPlugType = ((byte)0);
      batteryTemperature = ((short)0);
      batteryVoltage = ((char)0);
      batteryChargeUAh = 0;
      states = 0;
      states2 = 0;
      wakelockTag = null;
      wakeReasonTag = null;
      eventCode = 0;
      eventTag = null;
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public boolean isDeltaData()
    {
      boolean bool;
      if (cmd == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      int i = paramParcel.dataPosition();
      int j = paramParcel.readInt();
      cmd = ((byte)(byte)(j & 0xFF));
      batteryLevel = ((byte)(byte)(j >> 8 & 0xFF));
      batteryStatus = ((byte)(byte)(j >> 16 & 0xF));
      batteryHealth = ((byte)(byte)(j >> 20 & 0xF));
      batteryPlugType = ((byte)(byte)(j >> 24 & 0xF));
      int k = paramParcel.readInt();
      batteryTemperature = ((short)(short)(k & 0xFFFF));
      batteryVoltage = ((char)(char)(0xFFFF & k >> 16));
      batteryChargeUAh = paramParcel.readInt();
      states = paramParcel.readInt();
      states2 = paramParcel.readInt();
      if ((0x10000000 & j) != 0)
      {
        wakelockTag = localWakelockTag;
        wakelockTag.readFromParcel(paramParcel);
      }
      else
      {
        wakelockTag = null;
      }
      if ((0x20000000 & j) != 0)
      {
        wakeReasonTag = localWakeReasonTag;
        wakeReasonTag.readFromParcel(paramParcel);
      }
      else
      {
        wakeReasonTag = null;
      }
      if ((0x40000000 & j) != 0)
      {
        eventCode = paramParcel.readInt();
        eventTag = localEventTag;
        eventTag.readFromParcel(paramParcel);
      }
      else
      {
        eventCode = 0;
        eventTag = null;
      }
      if ((cmd != 5) && (cmd != 7)) {
        currentTime = 0L;
      } else {
        currentTime = paramParcel.readLong();
      }
      numReadInts += (paramParcel.dataPosition() - i) / 4;
    }
    
    public boolean same(HistoryItem paramHistoryItem)
    {
      if ((sameNonEvent(paramHistoryItem)) && (eventCode == eventCode))
      {
        if (wakelockTag != wakelockTag) {
          if ((wakelockTag != null) && (wakelockTag != null))
          {
            if (!wakelockTag.equals(wakelockTag)) {
              return false;
            }
          }
          else {
            return false;
          }
        }
        if (wakeReasonTag != wakeReasonTag) {
          if ((wakeReasonTag != null) && (wakeReasonTag != null))
          {
            if (!wakeReasonTag.equals(wakeReasonTag)) {
              return false;
            }
          }
          else {
            return false;
          }
        }
        if (eventTag != eventTag) {
          if ((eventTag != null) && (eventTag != null))
          {
            if (!eventTag.equals(eventTag)) {
              return false;
            }
          }
          else {
            return false;
          }
        }
        return true;
      }
      return false;
    }
    
    public boolean sameNonEvent(HistoryItem paramHistoryItem)
    {
      boolean bool;
      if ((batteryLevel == batteryLevel) && (batteryStatus == batteryStatus) && (batteryHealth == batteryHealth) && (batteryPlugType == batteryPlugType) && (batteryTemperature == batteryTemperature) && (batteryVoltage == batteryVoltage) && (batteryChargeUAh == batteryChargeUAh) && (states == states) && (states2 == states2) && (currentTime == currentTime)) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void setTo(long paramLong, byte paramByte, HistoryItem paramHistoryItem)
    {
      time = paramLong;
      cmd = ((byte)paramByte);
      setToCommon(paramHistoryItem);
    }
    
    public void setTo(HistoryItem paramHistoryItem)
    {
      time = time;
      cmd = ((byte)cmd);
      setToCommon(paramHistoryItem);
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeLong(time);
      int i = cmd;
      int j = batteryLevel;
      int k = batteryStatus;
      int m = batteryHealth;
      int n = batteryPlugType;
      BatteryStats.HistoryTag localHistoryTag = wakelockTag;
      int i1 = 0;
      int i2;
      if (localHistoryTag != null) {
        i2 = 268435456;
      } else {
        i2 = 0;
      }
      int i3;
      if (wakeReasonTag != null) {
        i3 = 536870912;
      } else {
        i3 = 0;
      }
      if (eventCode != 0) {
        i1 = 1073741824;
      }
      paramParcel.writeInt(i & 0xFF | j << 8 & 0xFF00 | k << 16 & 0xF0000 | m << 20 & 0xF00000 | n << 24 & 0xF000000 | i2 | i3 | i1);
      paramParcel.writeInt(batteryTemperature & 0xFFFF | batteryVoltage << '\020' & 0xFFFF0000);
      paramParcel.writeInt(batteryChargeUAh);
      paramParcel.writeInt(states);
      paramParcel.writeInt(states2);
      if (wakelockTag != null) {
        wakelockTag.writeToParcel(paramParcel, paramInt);
      }
      if (wakeReasonTag != null) {
        wakeReasonTag.writeToParcel(paramParcel, paramInt);
      }
      if (eventCode != 0)
      {
        paramParcel.writeInt(eventCode);
        eventTag.writeToParcel(paramParcel, paramInt);
      }
      if ((cmd == 5) || (cmd == 7)) {
        paramParcel.writeLong(currentTime);
      }
    }
  }
  
  public static class HistoryPrinter
  {
    long lastTime = -1L;
    int oldChargeMAh = -1;
    int oldHealth = -1;
    int oldLevel = -1;
    int oldPlug = -1;
    int oldState = 0;
    int oldState2 = 0;
    int oldStatus = -1;
    int oldTemp = -1;
    int oldVolt = -1;
    
    public HistoryPrinter() {}
    
    private String printNextItem(BatteryStats.HistoryItem paramHistoryItem, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      if (!paramBoolean1)
      {
        localStringBuilder.append("  ");
        TimeUtils.formatDuration(time - paramLong, localStringBuilder, 19);
        localStringBuilder.append(" (");
        localStringBuilder.append(numReadInts);
        localStringBuilder.append(") ");
      }
      else
      {
        localStringBuilder.append(9);
        localStringBuilder.append(',');
        localStringBuilder.append("h");
        localStringBuilder.append(',');
        if (lastTime < 0L) {
          localStringBuilder.append(time - paramLong);
        } else {
          localStringBuilder.append(time - lastTime);
        }
        lastTime = time;
      }
      if (cmd == 4)
      {
        if (paramBoolean1) {
          localStringBuilder.append(":");
        }
        localStringBuilder.append("START\n");
        reset();
      }
      else if ((cmd != 5) && (cmd != 7))
      {
        if (cmd == 8)
        {
          if (paramBoolean1) {
            localStringBuilder.append(":");
          }
          localStringBuilder.append("SHUTDOWN\n");
        }
        else if (cmd == 6)
        {
          if (paramBoolean1) {
            localStringBuilder.append(":");
          }
          localStringBuilder.append("*OVERFLOW*\n");
        }
        else
        {
          if (!paramBoolean1)
          {
            if (batteryLevel < 10) {
              localStringBuilder.append("00");
            } else if (batteryLevel < 100) {
              localStringBuilder.append("0");
            }
            localStringBuilder.append(batteryLevel);
            if (paramBoolean2)
            {
              localStringBuilder.append(" ");
              if (states >= 0) {
                if (states < 16) {
                  localStringBuilder.append("0000000");
                } else if (states < 256) {
                  localStringBuilder.append("000000");
                } else if (states < 4096) {
                  localStringBuilder.append("00000");
                } else if (states < 65536) {
                  localStringBuilder.append("0000");
                } else if (states < 1048576) {
                  localStringBuilder.append("000");
                } else if (states < 16777216) {
                  localStringBuilder.append("00");
                } else if (states < 268435456) {
                  localStringBuilder.append("0");
                }
              }
              localStringBuilder.append(Integer.toHexString(states));
            }
          }
          else if (oldLevel != batteryLevel)
          {
            oldLevel = batteryLevel;
            localStringBuilder.append(",Bl=");
            localStringBuilder.append(batteryLevel);
          }
          Object localObject;
          if (oldStatus != batteryStatus)
          {
            oldStatus = batteryStatus;
            if (paramBoolean1) {
              localObject = ",Bs=";
            } else {
              localObject = " status=";
            }
            localStringBuilder.append((String)localObject);
            switch (oldStatus)
            {
            default: 
              localStringBuilder.append(oldStatus);
              break;
            case 5: 
              if (paramBoolean1) {
                localObject = "f";
              } else {
                localObject = "full";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 4: 
              if (paramBoolean1) {
                localObject = "n";
              } else {
                localObject = "not-charging";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 3: 
              if (paramBoolean1) {
                localObject = "d";
              } else {
                localObject = "discharging";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 2: 
              if (paramBoolean1) {
                localObject = "c";
              } else {
                localObject = "charging";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 1: 
              if (paramBoolean1) {
                localObject = "?";
              } else {
                localObject = "unknown";
              }
              localStringBuilder.append((String)localObject);
            }
          }
          if (oldHealth != batteryHealth)
          {
            oldHealth = batteryHealth;
            if (paramBoolean1) {
              localObject = ",Bh=";
            } else {
              localObject = " health=";
            }
            localStringBuilder.append((String)localObject);
            switch (oldHealth)
            {
            default: 
              localStringBuilder.append(oldHealth);
              break;
            case 7: 
              if (paramBoolean1) {
                localObject = "c";
              } else {
                localObject = "cold";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 6: 
              if (paramBoolean1) {
                localObject = "f";
              } else {
                localObject = "failure";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 5: 
              if (paramBoolean1) {
                localObject = "v";
              } else {
                localObject = "over-voltage";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 4: 
              if (paramBoolean1) {
                localObject = "d";
              } else {
                localObject = "dead";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 3: 
              if (paramBoolean1) {
                localObject = "h";
              } else {
                localObject = "overheat";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 2: 
              if (paramBoolean1) {
                localObject = "g";
              } else {
                localObject = "good";
              }
              localStringBuilder.append((String)localObject);
              break;
            case 1: 
              if (paramBoolean1) {
                localObject = "?";
              } else {
                localObject = "unknown";
              }
              localStringBuilder.append((String)localObject);
            }
          }
          if (oldPlug != batteryPlugType)
          {
            oldPlug = batteryPlugType;
            if (paramBoolean1) {
              localObject = ",Bp=";
            } else {
              localObject = " plug=";
            }
            localStringBuilder.append((String)localObject);
            i = oldPlug;
            if (i != 4)
            {
              switch (i)
              {
              default: 
                localStringBuilder.append(oldPlug);
                break;
              case 2: 
                if (paramBoolean1) {
                  localObject = "u";
                } else {
                  localObject = "usb";
                }
                localStringBuilder.append((String)localObject);
                break;
              case 1: 
                if (paramBoolean1) {
                  localObject = "a";
                } else {
                  localObject = "ac";
                }
                localStringBuilder.append((String)localObject);
                break;
              case 0: 
                if (paramBoolean1) {
                  localObject = "n";
                } else {
                  localObject = "none";
                }
                localStringBuilder.append((String)localObject);
                break;
              }
            }
            else
            {
              if (paramBoolean1) {
                localObject = "w";
              } else {
                localObject = "wireless";
              }
              localStringBuilder.append((String)localObject);
            }
          }
          if (oldTemp != batteryTemperature)
          {
            oldTemp = batteryTemperature;
            if (paramBoolean1) {
              localObject = ",Bt=";
            } else {
              localObject = " temp=";
            }
            localStringBuilder.append((String)localObject);
            localStringBuilder.append(oldTemp);
          }
          if (oldVolt != batteryVoltage)
          {
            oldVolt = batteryVoltage;
            if (paramBoolean1) {
              localObject = ",Bv=";
            } else {
              localObject = " volt=";
            }
            localStringBuilder.append((String)localObject);
            localStringBuilder.append(oldVolt);
          }
          int i = batteryChargeUAh / 1000;
          if (oldChargeMAh != i)
          {
            oldChargeMAh = i;
            if (paramBoolean1) {
              localObject = ",Bcc=";
            } else {
              localObject = " charge=";
            }
            localStringBuilder.append((String)localObject);
            localStringBuilder.append(oldChargeMAh);
          }
          BatteryStats.printBitDescriptions(localStringBuilder, oldState, states, wakelockTag, BatteryStats.HISTORY_STATE_DESCRIPTIONS, paramBoolean1 ^ true);
          BatteryStats.printBitDescriptions(localStringBuilder, oldState2, states2, null, BatteryStats.HISTORY_STATE2_DESCRIPTIONS, paramBoolean1 ^ true);
          if (wakeReasonTag != null) {
            if (paramBoolean1)
            {
              localStringBuilder.append(",wr=");
              localStringBuilder.append(wakeReasonTag.poolIdx);
            }
            else
            {
              localStringBuilder.append(" wake_reason=");
              localStringBuilder.append(wakeReasonTag.uid);
              localStringBuilder.append(":\"");
              localStringBuilder.append(wakeReasonTag.string);
              localStringBuilder.append("\"");
            }
          }
          if (eventCode != 0)
          {
            if (paramBoolean1) {
              localObject = ",";
            } else {
              localObject = " ";
            }
            localStringBuilder.append((String)localObject);
            if ((eventCode & 0x8000) != 0) {
              localStringBuilder.append("+");
            } else if ((eventCode & 0x4000) != 0) {
              localStringBuilder.append("-");
            }
            if (paramBoolean1) {
              localObject = BatteryStats.HISTORY_EVENT_CHECKIN_NAMES;
            } else {
              localObject = BatteryStats.HISTORY_EVENT_NAMES;
            }
            i = eventCode & 0xFFFF3FFF;
            if ((i >= 0) && (i < localObject.length))
            {
              localStringBuilder.append(localObject[i]);
            }
            else
            {
              if (paramBoolean1) {
                localObject = "Ev";
              } else {
                localObject = "event";
              }
              localStringBuilder.append((String)localObject);
              localStringBuilder.append(i);
            }
            localStringBuilder.append("=");
            if (paramBoolean1)
            {
              localStringBuilder.append(eventTag.poolIdx);
            }
            else
            {
              localStringBuilder.append(BatteryStats.HISTORY_EVENT_INT_FORMATTERS[i].applyAsString(eventTag.uid));
              localStringBuilder.append(":\"");
              localStringBuilder.append(eventTag.string);
              localStringBuilder.append("\"");
            }
          }
          localStringBuilder.append("\n");
          if (stepDetails != null) {
            if (!paramBoolean1)
            {
              localStringBuilder.append("                 Details: cpu=");
              localStringBuilder.append(stepDetails.userTime);
              localStringBuilder.append("u+");
              localStringBuilder.append(stepDetails.systemTime);
              localStringBuilder.append("s");
              if (stepDetails.appCpuUid1 >= 0)
              {
                localStringBuilder.append(" (");
                printStepCpuUidDetails(localStringBuilder, stepDetails.appCpuUid1, stepDetails.appCpuUTime1, stepDetails.appCpuSTime1);
                if (stepDetails.appCpuUid2 >= 0)
                {
                  localStringBuilder.append(", ");
                  printStepCpuUidDetails(localStringBuilder, stepDetails.appCpuUid2, stepDetails.appCpuUTime2, stepDetails.appCpuSTime2);
                }
                if (stepDetails.appCpuUid3 >= 0)
                {
                  localStringBuilder.append(", ");
                  printStepCpuUidDetails(localStringBuilder, stepDetails.appCpuUid3, stepDetails.appCpuUTime3, stepDetails.appCpuSTime3);
                }
                localStringBuilder.append(')');
              }
              localStringBuilder.append("\n");
              localStringBuilder.append("                          /proc/stat=");
              localStringBuilder.append(stepDetails.statUserTime);
              localStringBuilder.append(" usr, ");
              localStringBuilder.append(stepDetails.statSystemTime);
              localStringBuilder.append(" sys, ");
              localStringBuilder.append(stepDetails.statIOWaitTime);
              localStringBuilder.append(" io, ");
              localStringBuilder.append(stepDetails.statIrqTime);
              localStringBuilder.append(" irq, ");
              localStringBuilder.append(stepDetails.statSoftIrqTime);
              localStringBuilder.append(" sirq, ");
              localStringBuilder.append(stepDetails.statIdlTime);
              localStringBuilder.append(" idle");
              int j = stepDetails.statUserTime + stepDetails.statSystemTime + stepDetails.statIOWaitTime + stepDetails.statIrqTime + stepDetails.statSoftIrqTime;
              i = stepDetails.statIdlTime + j;
              if (i > 0)
              {
                localStringBuilder.append(" (");
                localStringBuilder.append(String.format("%.1f%%", new Object[] { Float.valueOf(j / i * 100.0F) }));
                localStringBuilder.append(" of ");
                localObject = new StringBuilder(64);
                BatteryStats.formatTimeMsNoSpace((StringBuilder)localObject, i * 10);
                localStringBuilder.append((CharSequence)localObject);
                localStringBuilder.append(")");
              }
              localStringBuilder.append(", PlatformIdleStat ");
              localStringBuilder.append(stepDetails.statPlatformIdleState);
              localStringBuilder.append("\n");
              localStringBuilder.append(", SubsystemPowerState ");
              localStringBuilder.append(stepDetails.statSubsystemPowerState);
              localStringBuilder.append("\n");
            }
            else
            {
              localStringBuilder.append(9);
              localStringBuilder.append(',');
              localStringBuilder.append("h");
              localStringBuilder.append(",0,Dcpu=");
              localStringBuilder.append(stepDetails.userTime);
              localStringBuilder.append(":");
              localStringBuilder.append(stepDetails.systemTime);
              if (stepDetails.appCpuUid1 >= 0)
              {
                printStepCpuUidCheckinDetails(localStringBuilder, stepDetails.appCpuUid1, stepDetails.appCpuUTime1, stepDetails.appCpuSTime1);
                if (stepDetails.appCpuUid2 >= 0) {
                  printStepCpuUidCheckinDetails(localStringBuilder, stepDetails.appCpuUid2, stepDetails.appCpuUTime2, stepDetails.appCpuSTime2);
                }
                if (stepDetails.appCpuUid3 >= 0) {
                  printStepCpuUidCheckinDetails(localStringBuilder, stepDetails.appCpuUid3, stepDetails.appCpuUTime3, stepDetails.appCpuSTime3);
                }
              }
              localStringBuilder.append("\n");
              localStringBuilder.append(9);
              localStringBuilder.append(',');
              localStringBuilder.append("h");
              localStringBuilder.append(",0,Dpst=");
              localStringBuilder.append(stepDetails.statUserTime);
              localStringBuilder.append(',');
              localStringBuilder.append(stepDetails.statSystemTime);
              localStringBuilder.append(',');
              localStringBuilder.append(stepDetails.statIOWaitTime);
              localStringBuilder.append(',');
              localStringBuilder.append(stepDetails.statIrqTime);
              localStringBuilder.append(',');
              localStringBuilder.append(stepDetails.statSoftIrqTime);
              localStringBuilder.append(',');
              localStringBuilder.append(stepDetails.statIdlTime);
              localStringBuilder.append(',');
              if (stepDetails.statPlatformIdleState != null)
              {
                localStringBuilder.append(stepDetails.statPlatformIdleState);
                if (stepDetails.statSubsystemPowerState != null) {
                  localStringBuilder.append(',');
                }
              }
              if (stepDetails.statSubsystemPowerState != null) {
                localStringBuilder.append(stepDetails.statSubsystemPowerState);
              }
              localStringBuilder.append("\n");
            }
          }
          oldState = states;
          oldState2 = states2;
        }
      }
      else
      {
        if (paramBoolean1) {
          localStringBuilder.append(":");
        }
        if (cmd == 7)
        {
          localStringBuilder.append("RESET:");
          reset();
        }
        localStringBuilder.append("TIME:");
        if (paramBoolean1)
        {
          localStringBuilder.append(currentTime);
          localStringBuilder.append("\n");
        }
        else
        {
          localStringBuilder.append(" ");
          localStringBuilder.append(DateFormat.format("yyyy-MM-dd-HH-mm-ss", currentTime).toString());
          localStringBuilder.append("\n");
        }
      }
      return localStringBuilder.toString();
    }
    
    private void printStepCpuUidCheckinDetails(StringBuilder paramStringBuilder, int paramInt1, int paramInt2, int paramInt3)
    {
      paramStringBuilder.append('/');
      paramStringBuilder.append(paramInt1);
      paramStringBuilder.append(":");
      paramStringBuilder.append(paramInt2);
      paramStringBuilder.append(":");
      paramStringBuilder.append(paramInt3);
    }
    
    private void printStepCpuUidDetails(StringBuilder paramStringBuilder, int paramInt1, int paramInt2, int paramInt3)
    {
      UserHandle.formatUid(paramStringBuilder, paramInt1);
      paramStringBuilder.append("=");
      paramStringBuilder.append(paramInt2);
      paramStringBuilder.append("u+");
      paramStringBuilder.append(paramInt3);
      paramStringBuilder.append("s");
    }
    
    public void printNextItem(ProtoOutputStream paramProtoOutputStream, BatteryStats.HistoryItem paramHistoryItem, long paramLong, boolean paramBoolean)
    {
      paramHistoryItem = printNextItem(paramHistoryItem, paramLong, true, paramBoolean).split("\n");
      int i = paramHistoryItem.length;
      for (int j = 0; j < i; j++) {
        paramProtoOutputStream.write(2237677961222L, paramHistoryItem[j]);
      }
    }
    
    public void printNextItem(PrintWriter paramPrintWriter, BatteryStats.HistoryItem paramHistoryItem, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
    {
      paramPrintWriter.print(printNextItem(paramHistoryItem, paramLong, paramBoolean1, paramBoolean2));
    }
    
    void reset()
    {
      oldState2 = 0;
      oldState = 0;
      oldLevel = -1;
      oldStatus = -1;
      oldHealth = -1;
      oldPlug = -1;
      oldTemp = -1;
      oldVolt = -1;
      oldChargeMAh = -1;
    }
  }
  
  public static final class HistoryStepDetails
  {
    public int appCpuSTime1;
    public int appCpuSTime2;
    public int appCpuSTime3;
    public int appCpuUTime1;
    public int appCpuUTime2;
    public int appCpuUTime3;
    public int appCpuUid1;
    public int appCpuUid2;
    public int appCpuUid3;
    public int statIOWaitTime;
    public int statIdlTime;
    public int statIrqTime;
    public String statPlatformIdleState;
    public int statSoftIrqTime;
    public String statSubsystemPowerState;
    public int statSystemTime;
    public int statUserTime;
    public int systemTime;
    public int userTime;
    
    public HistoryStepDetails()
    {
      clear();
    }
    
    public void clear()
    {
      systemTime = 0;
      userTime = 0;
      appCpuUid3 = -1;
      appCpuUid2 = -1;
      appCpuUid1 = -1;
      appCpuSTime3 = 0;
      appCpuUTime3 = 0;
      appCpuSTime2 = 0;
      appCpuUTime2 = 0;
      appCpuSTime1 = 0;
      appCpuUTime1 = 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      userTime = paramParcel.readInt();
      systemTime = paramParcel.readInt();
      appCpuUid1 = paramParcel.readInt();
      appCpuUTime1 = paramParcel.readInt();
      appCpuSTime1 = paramParcel.readInt();
      appCpuUid2 = paramParcel.readInt();
      appCpuUTime2 = paramParcel.readInt();
      appCpuSTime2 = paramParcel.readInt();
      appCpuUid3 = paramParcel.readInt();
      appCpuUTime3 = paramParcel.readInt();
      appCpuSTime3 = paramParcel.readInt();
      statUserTime = paramParcel.readInt();
      statSystemTime = paramParcel.readInt();
      statIOWaitTime = paramParcel.readInt();
      statIrqTime = paramParcel.readInt();
      statSoftIrqTime = paramParcel.readInt();
      statIdlTime = paramParcel.readInt();
      statPlatformIdleState = paramParcel.readString();
      statSubsystemPowerState = paramParcel.readString();
    }
    
    public void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeInt(userTime);
      paramParcel.writeInt(systemTime);
      paramParcel.writeInt(appCpuUid1);
      paramParcel.writeInt(appCpuUTime1);
      paramParcel.writeInt(appCpuSTime1);
      paramParcel.writeInt(appCpuUid2);
      paramParcel.writeInt(appCpuUTime2);
      paramParcel.writeInt(appCpuSTime2);
      paramParcel.writeInt(appCpuUid3);
      paramParcel.writeInt(appCpuUTime3);
      paramParcel.writeInt(appCpuSTime3);
      paramParcel.writeInt(statUserTime);
      paramParcel.writeInt(statSystemTime);
      paramParcel.writeInt(statIOWaitTime);
      paramParcel.writeInt(statIrqTime);
      paramParcel.writeInt(statSoftIrqTime);
      paramParcel.writeInt(statIdlTime);
      paramParcel.writeString(statPlatformIdleState);
      paramParcel.writeString(statSubsystemPowerState);
    }
  }
  
  public static final class HistoryTag
  {
    public int poolIdx;
    public String string;
    public int uid;
    
    public HistoryTag() {}
    
    public boolean equals(Object paramObject)
    {
      if (this == paramObject) {
        return true;
      }
      if ((paramObject != null) && (getClass() == paramObject.getClass()))
      {
        paramObject = (HistoryTag)paramObject;
        if (uid != uid) {
          return false;
        }
        return string.equals(string);
      }
      return false;
    }
    
    public int hashCode()
    {
      return 31 * string.hashCode() + uid;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      string = paramParcel.readString();
      uid = paramParcel.readInt();
      poolIdx = -1;
    }
    
    public void setTo(HistoryTag paramHistoryTag)
    {
      string = string;
      uid = uid;
      poolIdx = poolIdx;
    }
    
    public void setTo(String paramString, int paramInt)
    {
      string = paramString;
      uid = paramInt;
      poolIdx = -1;
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      paramParcel.writeString(string);
      paramParcel.writeInt(uid);
    }
  }
  
  @FunctionalInterface
  public static abstract interface IntToString
  {
    public abstract String applyAsString(int paramInt);
  }
  
  public static final class LevelStepTracker
  {
    public long mLastStepTime = -1L;
    public int mNumStepDurations;
    public final long[] mStepDurations;
    
    public LevelStepTracker(int paramInt)
    {
      mStepDurations = new long[paramInt];
    }
    
    public LevelStepTracker(int paramInt, long[] paramArrayOfLong)
    {
      mNumStepDurations = paramInt;
      mStepDurations = new long[paramInt];
      System.arraycopy(paramArrayOfLong, 0, mStepDurations, 0, paramInt);
    }
    
    private void appendHex(long paramLong, int paramInt, StringBuilder paramStringBuilder)
    {
      int i = 0;
      while (paramInt >= 0)
      {
        int j = (int)(paramLong >> paramInt & 0xF);
        paramInt -= 4;
        if ((i != 0) || (j != 0))
        {
          i = 1;
          if ((j >= 0) && (j <= 9)) {
            paramStringBuilder.append((char)(48 + j));
          } else {
            paramStringBuilder.append((char)(97 + j - 10));
          }
        }
      }
    }
    
    public void addLevelSteps(int paramInt, long paramLong1, long paramLong2)
    {
      int i = mNumStepDurations;
      long l1 = mLastStepTime;
      int j = i;
      if (l1 >= 0L)
      {
        j = i;
        if (paramInt > 0)
        {
          long[] arrayOfLong = mStepDurations;
          l1 = paramLong2 - l1;
          j = 0;
          while (j < paramInt)
          {
            System.arraycopy(arrayOfLong, 0, arrayOfLong, 1, arrayOfLong.length - 1);
            long l2 = l1 / (paramInt - j);
            long l3 = l1 - l2;
            l1 = l2;
            if (l2 > 1099511627775L) {
              l1 = 1099511627775L;
            }
            arrayOfLong[0] = (l1 | paramLong1);
            j++;
            l1 = l3;
          }
          paramInt = i + paramInt;
          j = paramInt;
          if (paramInt > arrayOfLong.length) {
            j = arrayOfLong.length;
          }
        }
      }
      mNumStepDurations = j;
      mLastStepTime = paramLong2;
    }
    
    public void clearTime()
    {
      mLastStepTime = -1L;
    }
    
    public long computeTimeEstimate(long paramLong1, long paramLong2, int[] paramArrayOfInt)
    {
      long[] arrayOfLong = mStepDurations;
      int i = mNumStepDurations;
      if (i <= 0) {
        return -1L;
      }
      int j = 0;
      long l1 = 0L;
      int k = 0;
      while (k < i)
      {
        long l2 = arrayOfLong[k];
        int m = j;
        long l3 = l1;
        if (((arrayOfLong[k] & 0xFF00000000000000) >> 56 & paramLong1) == 0L)
        {
          m = j;
          l3 = l1;
          if (((l2 & 0xFF000000000000) >> 48 & paramLong1) == paramLong2)
          {
            m = j + 1;
            l3 = l1 + (arrayOfLong[k] & 0xFFFFFFFFFF);
          }
        }
        k++;
        j = m;
        l1 = l3;
      }
      if (j <= 0) {
        return -1L;
      }
      if (paramArrayOfInt != null) {
        paramArrayOfInt[0] = j;
      }
      return l1 / j * 100L;
    }
    
    public long computeTimePerLevel()
    {
      long[] arrayOfLong = mStepDurations;
      int i = mNumStepDurations;
      if (i <= 0) {
        return -1L;
      }
      long l = 0L;
      for (int j = 0; j < i; j++) {
        l += (arrayOfLong[j] & 0xFFFFFFFFFF);
      }
      return l / i;
    }
    
    public void decodeEntryAt(int paramInt, String paramString)
    {
      int i = paramString.length();
      int j = 0;
      int k;
      for (long l1 = 0L; j < i; l1 = l2)
      {
        k = paramString.charAt(j);
        if (k == 45) {
          break;
        }
        j++;
        switch (k)
        {
        default: 
          l2 = l1;
          break;
        case 122: 
          l2 = l1 | 0x3000000000000;
          break;
        case 112: 
          l2 = l1 | 0x4000000000000;
          break;
        case 111: 
          l2 = l1 | 0x1000000000000;
          break;
        case 105: 
          l2 = l1 | 0x8000000000000;
          break;
        case 102: 
          l2 = l1 | 0L;
          break;
        case 100: 
          l2 = l1 | 0x2000000000000;
          break;
        case 90: 
          l2 = l1 | 0x300000000000000;
          break;
        case 80: 
          l2 = l1 | 0x400000000000000;
          break;
        case 79: 
          l2 = l1 | 0x100000000000000;
          break;
        case 73: 
          l2 = l1 | 0x800000000000000;
          break;
        case 70: 
          l2 = l1 | 0L;
          break;
        case 68: 
          l2 = l1 | 0x200000000000000;
        }
      }
      j++;
      long l3 = 0L;
      if (j < i)
      {
        k = paramString.charAt(j);
        if (k != 45)
        {
          j++;
          l3 <<= 4;
          if ((k >= 48) && (k <= 57)) {
            l2 = l3 + (k - 48);
          }
          for (;;)
          {
            l3 = l2;
            break;
            if ((k >= 97) && (k <= 102))
            {
              l2 = l3 + (k - 97 + 10);
            }
            else
            {
              l2 = l3;
              if (k >= 65)
              {
                l2 = l3;
                if (k <= 70) {
                  l2 = l3 + (k - 65 + 10);
                }
              }
            }
          }
        }
      }
      j++;
      long l2 = 0L;
      long l4 = l3;
      if (j < i)
      {
        k = paramString.charAt(j);
        if (k != 45)
        {
          j++;
          long l5 = l2 << 4;
          if ((k >= 48) && (k <= 57)) {
            l2 = l5 + (k - 48);
          }
          for (;;)
          {
            break;
            if ((k >= 97) && (k <= 102))
            {
              l2 = l5 + (k - 97 + 10);
            }
            else
            {
              l2 = l5;
              if (k >= 65)
              {
                l2 = l5;
                if (k <= 70) {
                  l2 = l5 + (k - 65 + 10);
                }
              }
            }
          }
        }
      }
      mStepDurations[paramInt] = (l2 & 0xFFFFFFFFFF | l1 | l3 << 40 & 0xFF0000000000);
    }
    
    public void encodeEntryAt(int paramInt, StringBuilder paramStringBuilder)
    {
      long l = mStepDurations[paramInt];
      int i = (int)((0xFF0000000000 & l) >> 40);
      paramInt = (int)((0xFF000000000000 & l) >> 48);
      int j = (int)((0xFF00000000000000 & l) >> 56);
      switch ((paramInt & 0x3) + 1)
      {
      default: 
        break;
      case 4: 
        paramStringBuilder.append('z');
        break;
      case 3: 
        paramStringBuilder.append('d');
        break;
      case 2: 
        paramStringBuilder.append('o');
        break;
      case 1: 
        paramStringBuilder.append('f');
      }
      if ((paramInt & 0x4) != 0) {
        paramStringBuilder.append('p');
      }
      if ((paramInt & 0x8) != 0) {
        paramStringBuilder.append('i');
      }
      switch ((j & 0x3) + 1)
      {
      default: 
        break;
      case 4: 
        paramStringBuilder.append('Z');
        break;
      case 3: 
        paramStringBuilder.append('D');
        break;
      case 2: 
        paramStringBuilder.append('O');
        break;
      case 1: 
        paramStringBuilder.append('F');
      }
      if ((j & 0x4) != 0) {
        paramStringBuilder.append('P');
      }
      if ((j & 0x8) != 0) {
        paramStringBuilder.append('I');
      }
      paramStringBuilder.append('-');
      appendHex(i, 4, paramStringBuilder);
      paramStringBuilder.append('-');
      appendHex(0xFFFFFFFFFF & l, 36, paramStringBuilder);
    }
    
    public long getDurationAt(int paramInt)
    {
      return mStepDurations[paramInt] & 0xFFFFFFFFFF;
    }
    
    public int getInitModeAt(int paramInt)
    {
      return (int)((mStepDurations[paramInt] & 0xFF000000000000) >> 48);
    }
    
    public int getLevelAt(int paramInt)
    {
      return (int)((mStepDurations[paramInt] & 0xFF0000000000) >> 40);
    }
    
    public int getModModeAt(int paramInt)
    {
      return (int)((mStepDurations[paramInt] & 0xFF00000000000000) >> 56);
    }
    
    public void init()
    {
      mLastStepTime = -1L;
      mNumStepDurations = 0;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      if (i <= mStepDurations.length)
      {
        mNumStepDurations = i;
        for (int j = 0; j < i; j++) {
          mStepDurations[j] = paramParcel.readLong();
        }
        return;
      }
      paramParcel = new StringBuilder();
      paramParcel.append("more step durations than available: ");
      paramParcel.append(i);
      throw new ParcelFormatException(paramParcel.toString());
    }
    
    public void writeToParcel(Parcel paramParcel)
    {
      int i = mNumStepDurations;
      paramParcel.writeInt(i);
      for (int j = 0; j < i; j++) {
        paramParcel.writeLong(mStepDurations[j]);
      }
    }
  }
  
  public static abstract class LongCounter
  {
    public LongCounter() {}
    
    public abstract long getCountLocked(int paramInt);
    
    public abstract void logState(Printer paramPrinter, String paramString);
  }
  
  public static abstract class LongCounterArray
  {
    public LongCounterArray() {}
    
    public abstract long[] getCountsLocked(int paramInt);
    
    public abstract void logState(Printer paramPrinter, String paramString);
  }
  
  public static final class PackageChange
  {
    public String mPackageName;
    public boolean mUpdate;
    public long mVersionCode;
    
    public PackageChange() {}
  }
  
  public static abstract class Timer
  {
    public Timer() {}
    
    public abstract int getCountLocked(int paramInt);
    
    public long getCurrentDurationMsLocked(long paramLong)
    {
      return -1L;
    }
    
    public long getMaxDurationMsLocked(long paramLong)
    {
      return -1L;
    }
    
    public Timer getSubTimer()
    {
      return null;
    }
    
    public abstract long getTimeSinceMarkLocked(long paramLong);
    
    public long getTotalDurationMsLocked(long paramLong)
    {
      return -1L;
    }
    
    public abstract long getTotalTimeLocked(long paramLong, int paramInt);
    
    public boolean isRunningLocked()
    {
      return false;
    }
    
    public abstract void logState(Printer paramPrinter, String paramString);
  }
  
  static final class TimerEntry
  {
    final int mId;
    final String mName;
    final long mTime;
    final BatteryStats.Timer mTimer;
    
    TimerEntry(String paramString, int paramInt, BatteryStats.Timer paramTimer, long paramLong)
    {
      mName = paramString;
      mId = paramInt;
      mTimer = paramTimer;
      mTime = paramLong;
    }
  }
  
  public static abstract class Uid
  {
    public static final int[] CRITICAL_PROC_STATES = { 0, 1, 2 };
    public static final int NUM_PROCESS_STATE = 7;
    public static final int NUM_USER_ACTIVITY_TYPES = 4;
    public static final int NUM_WIFI_BATCHED_SCAN_BINS = 5;
    public static final int PROCESS_STATE_BACKGROUND = 3;
    public static final int PROCESS_STATE_CACHED = 6;
    public static final int PROCESS_STATE_FOREGROUND = 2;
    public static final int PROCESS_STATE_FOREGROUND_SERVICE = 1;
    public static final int PROCESS_STATE_HEAVY_WEIGHT = 5;
    static final String[] PROCESS_STATE_NAMES = { "Top", "Fg Service", "Foreground", "Background", "Top Sleeping", "Heavy Weight", "Cached" };
    public static final int PROCESS_STATE_TOP = 0;
    public static final int PROCESS_STATE_TOP_SLEEPING = 4;
    @VisibleForTesting
    public static final String[] UID_PROCESS_TYPES = { "T", "FS", "F", "B", "TS", "HW", "C" };
    static final String[] USER_ACTIVITY_TYPES = { "other", "button", "touch", "accessibility" };
    
    public Uid() {}
    
    public abstract BatteryStats.Timer getAggregatedPartialWakelockTimer();
    
    public abstract BatteryStats.Timer getAudioTurnedOnTimer();
    
    public abstract BatteryStats.ControllerActivityCounter getBluetoothControllerActivity();
    
    public abstract BatteryStats.Timer getBluetoothScanBackgroundTimer();
    
    public abstract BatteryStats.Counter getBluetoothScanResultBgCounter();
    
    public abstract BatteryStats.Counter getBluetoothScanResultCounter();
    
    public abstract BatteryStats.Timer getBluetoothScanTimer();
    
    public abstract BatteryStats.Timer getBluetoothUnoptimizedScanBackgroundTimer();
    
    public abstract BatteryStats.Timer getBluetoothUnoptimizedScanTimer();
    
    public abstract BatteryStats.Timer getCameraTurnedOnTimer();
    
    public abstract long getCpuActiveTime();
    
    public abstract long[] getCpuClusterTimes();
    
    public abstract long[] getCpuFreqTimes(int paramInt);
    
    public abstract long[] getCpuFreqTimes(int paramInt1, int paramInt2);
    
    public abstract void getDeferredJobsCheckinLineLocked(StringBuilder paramStringBuilder, int paramInt);
    
    public abstract void getDeferredJobsLineLocked(StringBuilder paramStringBuilder, int paramInt);
    
    public abstract BatteryStats.Timer getFlashlightTurnedOnTimer();
    
    public abstract BatteryStats.Timer getForegroundActivityTimer();
    
    public abstract BatteryStats.Timer getForegroundServiceTimer();
    
    public abstract long getFullWifiLockTime(long paramLong, int paramInt);
    
    public abstract ArrayMap<String, SparseIntArray> getJobCompletionStats();
    
    public abstract ArrayMap<String, ? extends BatteryStats.Timer> getJobStats();
    
    public abstract int getMobileRadioActiveCount(int paramInt);
    
    public abstract long getMobileRadioActiveTime(int paramInt);
    
    public abstract long getMobileRadioApWakeupCount(int paramInt);
    
    public abstract BatteryStats.ControllerActivityCounter getModemControllerActivity();
    
    public abstract BatteryStats.Timer getMulticastWakelockStats();
    
    public abstract long getNetworkActivityBytes(int paramInt1, int paramInt2);
    
    public abstract long getNetworkActivityPackets(int paramInt1, int paramInt2);
    
    public abstract ArrayMap<String, ? extends Pkg> getPackageStats();
    
    public abstract SparseArray<? extends Pid> getPidStats();
    
    public abstract long getProcessStateTime(int paramInt1, long paramLong, int paramInt2);
    
    public abstract BatteryStats.Timer getProcessStateTimer(int paramInt);
    
    public abstract ArrayMap<String, ? extends Proc> getProcessStats();
    
    public abstract long[] getScreenOffCpuFreqTimes(int paramInt);
    
    public abstract long[] getScreenOffCpuFreqTimes(int paramInt1, int paramInt2);
    
    public abstract SparseArray<? extends Sensor> getSensorStats();
    
    public abstract ArrayMap<String, ? extends BatteryStats.Timer> getSyncStats();
    
    public abstract long getSystemCpuTimeUs(int paramInt);
    
    public abstract long getTimeAtCpuSpeed(int paramInt1, int paramInt2, int paramInt3);
    
    public abstract int getUid();
    
    public abstract int getUserActivityCount(int paramInt1, int paramInt2);
    
    public abstract long getUserCpuTimeUs(int paramInt);
    
    public abstract BatteryStats.Timer getVibratorOnTimer();
    
    public abstract BatteryStats.Timer getVideoTurnedOnTimer();
    
    public abstract ArrayMap<String, ? extends Wakelock> getWakelockStats();
    
    public abstract int getWifiBatchedScanCount(int paramInt1, int paramInt2);
    
    public abstract long getWifiBatchedScanTime(int paramInt1, long paramLong, int paramInt2);
    
    public abstract BatteryStats.ControllerActivityCounter getWifiControllerActivity();
    
    public abstract long getWifiMulticastTime(long paramLong, int paramInt);
    
    public abstract long getWifiRadioApWakeupCount(int paramInt);
    
    public abstract long getWifiRunningTime(long paramLong, int paramInt);
    
    public abstract long getWifiScanActualTime(long paramLong);
    
    public abstract int getWifiScanBackgroundCount(int paramInt);
    
    public abstract long getWifiScanBackgroundTime(long paramLong);
    
    public abstract BatteryStats.Timer getWifiScanBackgroundTimer();
    
    public abstract int getWifiScanCount(int paramInt);
    
    public abstract long getWifiScanTime(long paramLong, int paramInt);
    
    public abstract BatteryStats.Timer getWifiScanTimer();
    
    public abstract boolean hasNetworkActivity();
    
    public abstract boolean hasUserActivity();
    
    public abstract void noteActivityPausedLocked(long paramLong);
    
    public abstract void noteActivityResumedLocked(long paramLong);
    
    public abstract void noteFullWifiLockAcquiredLocked(long paramLong);
    
    public abstract void noteFullWifiLockReleasedLocked(long paramLong);
    
    public abstract void noteUserActivityLocked(int paramInt);
    
    public abstract void noteWifiBatchedScanStartedLocked(int paramInt, long paramLong);
    
    public abstract void noteWifiBatchedScanStoppedLocked(long paramLong);
    
    public abstract void noteWifiMulticastDisabledLocked(long paramLong);
    
    public abstract void noteWifiMulticastEnabledLocked(long paramLong);
    
    public abstract void noteWifiRunningLocked(long paramLong);
    
    public abstract void noteWifiScanStartedLocked(long paramLong);
    
    public abstract void noteWifiScanStoppedLocked(long paramLong);
    
    public abstract void noteWifiStoppedLocked(long paramLong);
    
    public class Pid
    {
      public int mWakeNesting;
      public long mWakeStartMs;
      public long mWakeSumMs;
      
      public Pid() {}
    }
    
    public static abstract class Pkg
    {
      public Pkg() {}
      
      public abstract ArrayMap<String, ? extends Serv> getServiceStats();
      
      public abstract ArrayMap<String, ? extends BatteryStats.Counter> getWakeupAlarmStats();
      
      public static abstract class Serv
      {
        public Serv() {}
        
        public abstract int getLaunches(int paramInt);
        
        public abstract long getStartTime(long paramLong, int paramInt);
        
        public abstract int getStarts(int paramInt);
      }
    }
    
    public static abstract class Proc
    {
      public Proc() {}
      
      public abstract int countExcessivePowers();
      
      public abstract ExcessivePower getExcessivePower(int paramInt);
      
      public abstract long getForegroundTime(int paramInt);
      
      public abstract int getNumAnrs(int paramInt);
      
      public abstract int getNumCrashes(int paramInt);
      
      public abstract int getStarts(int paramInt);
      
      public abstract long getSystemTime(int paramInt);
      
      public abstract long getUserTime(int paramInt);
      
      public abstract boolean isActive();
      
      public static class ExcessivePower
      {
        public static final int TYPE_CPU = 2;
        public static final int TYPE_WAKE = 1;
        public long overTime;
        public int type;
        public long usedTime;
        
        public ExcessivePower() {}
      }
    }
    
    public static abstract class Sensor
    {
      public static final int GPS = -10000;
      
      public Sensor() {}
      
      public abstract int getHandle();
      
      public abstract BatteryStats.Timer getSensorBackgroundTime();
      
      public abstract BatteryStats.Timer getSensorTime();
    }
    
    public static abstract class Wakelock
    {
      public Wakelock() {}
      
      public abstract BatteryStats.Timer getWakeTime(int paramInt);
    }
  }
}
