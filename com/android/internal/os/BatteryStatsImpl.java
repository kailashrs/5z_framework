package com.android.internal.os;

import android.app.ActivityManager;
import android.bluetooth.BluetoothActivityEnergyInfo;
import android.bluetooth.UidTraffic;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.NetworkStats;
import android.net.NetworkStats.Entry;
import android.net.Uri;
import android.net.wifi.WifiActivityEnergyInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryStats;
import android.os.BatteryStats.ControllerActivityCounter;
import android.os.BatteryStats.Counter;
import android.os.BatteryStats.DailyItem;
import android.os.BatteryStats.HistoryEventTracker;
import android.os.BatteryStats.HistoryItem;
import android.os.BatteryStats.HistoryPrinter;
import android.os.BatteryStats.HistoryStepDetails;
import android.os.BatteryStats.HistoryTag;
import android.os.BatteryStats.LevelStepTracker;
import android.os.BatteryStats.LongCounter;
import android.os.BatteryStats.LongCounterArray;
import android.os.BatteryStats.PackageChange;
import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;
import android.os.BatteryStats.Uid.Pid;
import android.os.BatteryStats.Uid.Pkg;
import android.os.BatteryStats.Uid.Pkg.Serv;
import android.os.BatteryStats.Uid.Proc;
import android.os.BatteryStats.Uid.Proc.ExcessivePower;
import android.os.BatteryStats.Uid.Sensor;
import android.os.BatteryStats.Uid.Wakelock;
import android.os.Build;
import android.os.Build.FEATURES;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBatteryPropertiesRegistrar;
import android.os.IBatteryPropertiesRegistrar.Stub;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.WorkSource;
import android.os.WorkSource.WorkChain;
import android.os.connectivity.CellularBatteryStats;
import android.os.connectivity.GpsBatteryStats;
import android.os.connectivity.WifiBatteryStats;
import android.provider.Settings.Global;
import android.telephony.ModemActivityInfo;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.IntArray;
import android.util.KeyValueListParser;
import android.util.Log;
import android.util.LogWriter;
import android.util.LongSparseArray;
import android.util.LongSparseLongArray;
import android.util.MutableInt;
import android.util.Pools.Pool;
import android.util.Pools.SynchronizedPool;
import android.util.Printer;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.StatsLog;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.annotations.VisibleForTesting.Visibility;
import com.android.internal.logging.EventLogTags;
import com.android.internal.net.NetworkStatsFactory;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.JournaledFile;
import com.android.internal.util.XmlUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public class BatteryStatsImpl
  extends BatteryStats
{
  static final int BATTERY_DELTA_LEVEL_FLAG = 1;
  public static final int BATTERY_PLUGGED_NONE = 0;
  public static final Parcelable.Creator<BatteryStatsImpl> CREATOR = new Parcelable.Creator()
  {
    public BatteryStatsImpl createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BatteryStatsImpl(paramAnonymousParcel);
    }
    
    public BatteryStatsImpl[] newArray(int paramAnonymousInt)
    {
      return new BatteryStatsImpl[paramAnonymousInt];
    }
  };
  private static final boolean DEBUG = false;
  public static final boolean DEBUG_ENERGY = false;
  private static final boolean DEBUG_ENERGY_CPU = false;
  private static final boolean DEBUG_HISTORY = false;
  private static final boolean DEBUG_MEMORY = false;
  static final long DELAY_UPDATE_WAKELOCKS = 5000L;
  static final int DELTA_BATTERY_CHARGE_FLAG = 16777216;
  static final int DELTA_BATTERY_LEVEL_FLAG = 524288;
  static final int DELTA_EVENT_FLAG = 8388608;
  static final int DELTA_STATE2_FLAG = 2097152;
  static final int DELTA_STATE_FLAG = 1048576;
  static final int DELTA_STATE_MASK = -33554432;
  static final int DELTA_TIME_ABS = 524285;
  static final int DELTA_TIME_INT = 524286;
  static final int DELTA_TIME_LONG = 524287;
  static final int DELTA_TIME_MASK = 524287;
  static final int DELTA_WAKELOCK_FLAG = 4194304;
  private static final int MAGIC = -1166707595;
  static final int MAX_DAILY_ITEMS = 10;
  static final int MAX_HISTORY_BUFFER;
  private static final int MAX_HISTORY_ITEMS;
  static final int MAX_LEVEL_STEPS = 200;
  static final int MAX_MAX_HISTORY_BUFFER;
  private static final int MAX_MAX_HISTORY_ITEMS;
  private static final int MAX_WAKELOCKS_PER_UID;
  static final int MSG_REPORT_CHARGING = 3;
  static final int MSG_REPORT_CPU_UPDATE_NEEDED = 1;
  static final int MSG_REPORT_POWER_CHANGE = 2;
  static final int MSG_REPORT_RESET_STATS = 4;
  private static final int NUM_BT_TX_LEVELS = 1;
  private static final int NUM_WIFI_TX_LEVELS = 1;
  private static final long RPM_STATS_UPDATE_FREQ_MS = 1000L;
  static final int STATE_BATTERY_HEALTH_MASK = 7;
  static final int STATE_BATTERY_HEALTH_SHIFT = 26;
  static final int STATE_BATTERY_MASK = -16777216;
  static final int STATE_BATTERY_PLUG_MASK = 3;
  static final int STATE_BATTERY_PLUG_SHIFT = 24;
  static final int STATE_BATTERY_STATUS_MASK = 7;
  static final int STATE_BATTERY_STATUS_SHIFT = 29;
  private static final String TAG = "BatteryStatsImpl";
  private static final int USB_DATA_CONNECTED = 2;
  private static final int USB_DATA_DISCONNECTED = 1;
  private static final int USB_DATA_UNKNOWN = 0;
  private static final boolean USE_OLD_HISTORY = false;
  private static final int VERSION = 177;
  @VisibleForTesting
  public static final int WAKE_LOCK_WEIGHT = 50;
  final BatteryStats.HistoryEventTracker mActiveEvents = new BatteryStats.HistoryEventTracker();
  int mActiveHistoryStates = -1;
  int mActiveHistoryStates2 = -1;
  EasyTimer mAudioOnEasyTimer;
  int mAudioOnNesting;
  StopwatchTimer mAudioOnTimer;
  final ArrayList<StopwatchTimer> mAudioTurnedOnTimers = new ArrayList();
  EasyTimer mBleScanningEasyTimer;
  ControllerActivityCounterImpl mBluetoothActivity;
  int mBluetoothScanNesting;
  final ArrayList<StopwatchTimer> mBluetoothScanOnTimers = new ArrayList();
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  protected StopwatchTimer mBluetoothScanTimer;
  private BatteryCallback mCallback;
  EasyTimer mCameraOnEasyTimer;
  int mCameraOnNesting;
  StopwatchTimer mCameraOnTimer;
  final ArrayList<StopwatchTimer> mCameraTurnedOnTimers = new ArrayList();
  int mChangedStates = 0;
  int mChangedStates2 = 0;
  final BatteryStats.LevelStepTracker mChargeStepTracker = new BatteryStats.LevelStepTracker(200);
  boolean mCharging = true;
  EasyTimer mChargingOnEasyTimer;
  public final AtomicFile mCheckinFile;
  protected Clocks mClocks;
  @GuardedBy("this")
  private final Constants mConstants;
  private long[] mCpuFreqs;
  @GuardedBy("this")
  private long mCpuTimeReadsTrackingStartTime = SystemClock.uptimeMillis();
  final BatteryStats.HistoryStepDetails mCurHistoryStepDetails = new BatteryStats.HistoryStepDetails();
  long mCurStepCpuSystemTime;
  long mCurStepCpuUserTime;
  int mCurStepMode = 0;
  long mCurStepStatIOWaitTime;
  long mCurStepStatIdleTime;
  long mCurStepStatIrqTime;
  long mCurStepStatSoftIrqTime;
  long mCurStepStatSystemTime;
  long mCurStepStatUserTime;
  int mCurrentBatteryLevel;
  final BatteryStats.LevelStepTracker mDailyChargeStepTracker = new BatteryStats.LevelStepTracker(400);
  final BatteryStats.LevelStepTracker mDailyDischargeStepTracker = new BatteryStats.LevelStepTracker(400);
  public final AtomicFile mDailyFile;
  final ArrayList<BatteryStats.DailyItem> mDailyItems = new ArrayList();
  ArrayList<BatteryStats.PackageChange> mDailyPackageChanges;
  long mDailyStartTime = 0L;
  int mDeviceIdleMode;
  StopwatchTimer mDeviceIdleModeFullTimer;
  StopwatchTimer mDeviceIdleModeLightTimer;
  boolean mDeviceIdling;
  StopwatchTimer mDeviceIdlingTimer;
  boolean mDeviceLightIdling;
  StopwatchTimer mDeviceLightIdlingTimer;
  int mDischargeAmountScreenDoze;
  int mDischargeAmountScreenDozeSinceCharge;
  int mDischargeAmountScreenOff;
  int mDischargeAmountScreenOffSinceCharge;
  int mDischargeAmountScreenOn;
  int mDischargeAmountScreenOnSinceCharge;
  private LongSamplingCounter mDischargeCounter;
  int mDischargeCurrentLevel;
  private LongSamplingCounter mDischargeDeepDozeCounter;
  private LongSamplingCounter mDischargeLightDozeCounter;
  int mDischargePlugLevel;
  private LongSamplingCounter mDischargeScreenDozeCounter;
  int mDischargeScreenDozeUnplugLevel;
  private LongSamplingCounter mDischargeScreenOffCounter;
  int mDischargeScreenOffUnplugLevel;
  int mDischargeScreenOnUnplugLevel;
  int mDischargeStartLevel;
  final BatteryStats.LevelStepTracker mDischargeStepTracker = new BatteryStats.LevelStepTracker(200);
  int mDischargeUnplugLevel;
  boolean mDistributeWakelockCpu;
  final ArrayList<StopwatchTimer> mDrawTimers = new ArrayList();
  String mEndPlatformVersion;
  private int mEstimatedBatteryCapacity = -1;
  private ExternalStatsSync mExternalSync = null;
  private final JournaledFile mFile;
  EasyTimer mFlashLightEasyTimer;
  int mFlashlightOnNesting;
  StopwatchTimer mFlashlightOnTimer;
  final ArrayList<StopwatchTimer> mFlashlightTurnedOnTimers = new ArrayList();
  final ArrayList<StopwatchTimer> mFullTimers = new ArrayList();
  final ArrayList<StopwatchTimer> mFullWifiLockTimers = new ArrayList();
  boolean mGlobalWifiRunning;
  StopwatchTimer mGlobalWifiRunningTimer;
  int mGpsNesting;
  EasyTimer mGpsOnEasyTimer;
  int mGpsSignalQualityBin = -1;
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  protected final StopwatchTimer[] mGpsSignalQualityTimer = new StopwatchTimer[2];
  public Handler mHandler;
  boolean mHasBluetoothReporting = false;
  boolean mHasModemReporting = false;
  boolean mHasWifiReporting = false;
  protected boolean mHaveBatteryLevel = false;
  int mHighDischargeAmountSinceCharge;
  BatteryStats.HistoryItem mHistory;
  final BatteryStats.HistoryItem mHistoryAddTmp = new BatteryStats.HistoryItem();
  long mHistoryBaseTime;
  final Parcel mHistoryBuffer = Parcel.obtain();
  int mHistoryBufferLastPos = -1;
  BatteryStats.HistoryItem mHistoryCache;
  final BatteryStats.HistoryItem mHistoryCur = new BatteryStats.HistoryItem();
  BatteryStats.HistoryItem mHistoryEnd;
  private BatteryStats.HistoryItem mHistoryIterator;
  BatteryStats.HistoryItem mHistoryLastEnd;
  final BatteryStats.HistoryItem mHistoryLastLastWritten = new BatteryStats.HistoryItem();
  final BatteryStats.HistoryItem mHistoryLastWritten = new BatteryStats.HistoryItem();
  boolean mHistoryOverflow = false;
  final BatteryStats.HistoryItem mHistoryReadTmp = new BatteryStats.HistoryItem();
  final HashMap<BatteryStats.HistoryTag, Integer> mHistoryTagPool = new HashMap();
  int mInitStepMode = 0;
  private String mInitialAcquireWakeName;
  private int mInitialAcquireWakeUid = -1;
  boolean mInteractive;
  StopwatchTimer mInteractiveTimer;
  boolean mIsCellularTxPowerHigh = false;
  final SparseIntArray mIsolatedUids = new SparseIntArray();
  private boolean mIteratingHistory;
  @VisibleForTesting
  protected KernelCpuSpeedReader[] mKernelCpuSpeedReaders;
  private final KernelMemoryBandwidthStats mKernelMemoryBandwidthStats = new KernelMemoryBandwidthStats();
  private final LongSparseArray<SamplingTimer> mKernelMemoryStats = new LongSparseArray();
  @VisibleForTesting
  protected KernelSingleUidTimeReader mKernelSingleUidTimeReader;
  @VisibleForTesting
  protected KernelUidCpuActiveTimeReader mKernelUidCpuActiveTimeReader = new KernelUidCpuActiveTimeReader();
  @VisibleForTesting
  protected KernelUidCpuClusterTimeReader mKernelUidCpuClusterTimeReader = new KernelUidCpuClusterTimeReader();
  @VisibleForTesting
  protected KernelUidCpuFreqTimeReader mKernelUidCpuFreqTimeReader = new KernelUidCpuFreqTimeReader();
  @VisibleForTesting
  protected KernelUidCpuTimeReader mKernelUidCpuTimeReader = new KernelUidCpuTimeReader();
  private final KernelWakelockReader mKernelWakelockReader = new KernelWakelockReader();
  private final HashMap<String, SamplingTimer> mKernelWakelockStats = new HashMap();
  private final BluetoothActivityInfoCache mLastBluetoothActivityInfo = new BluetoothActivityInfoCache(null);
  int mLastChargeStepLevel;
  int mLastChargingStateLevel;
  int mLastDischargeStepLevel;
  long mLastHistoryElapsedRealtime = 0L;
  BatteryStats.HistoryStepDetails mLastHistoryStepDetails = null;
  byte mLastHistoryStepLevel = (byte)0;
  long mLastIdleTimeStart;
  private ModemActivityInfo mLastModemActivityInfo = new ModemActivityInfo(0L, 0, 0, new int[0], 0, 0);
  @GuardedBy("mModemNetworkLock")
  private NetworkStats mLastModemNetworkStats = new NetworkStats(0L, -1);
  @VisibleForTesting
  protected ArrayList<StopwatchTimer> mLastPartialTimers = new ArrayList();
  private long mLastRpmStatsUpdateTimeMs = -1000L;
  long mLastStepCpuSystemTime;
  long mLastStepCpuUserTime;
  long mLastStepStatIOWaitTime;
  long mLastStepStatIdleTime;
  long mLastStepStatIrqTime;
  long mLastStepStatSoftIrqTime;
  long mLastStepStatSystemTime;
  long mLastStepStatUserTime;
  String mLastWakeupReason = null;
  long mLastWakeupUptimeMs = 0L;
  @GuardedBy("mWifiNetworkLock")
  private NetworkStats mLastWifiNetworkStats = new NetworkStats(0L, -1);
  long mLastWriteTime = 0L;
  private int mLoadedNumConnectivityChange;
  long mLongestFullIdleTime;
  long mLongestLightIdleTime;
  int mLowDischargeAmountSinceCharge;
  EasyTimer mLowPowerModeEasyTimer;
  int mMaxChargeStepLevel;
  private int mMaxLearnedBatteryCapacity = -1;
  int mMinDischargeStepLevel;
  private int mMinLearnedBatteryCapacity = -1;
  LongSamplingCounter mMobileRadioActiveAdjustedTime;
  StopwatchTimer mMobileRadioActivePerAppTimer;
  long mMobileRadioActiveStartTime;
  StopwatchTimer mMobileRadioActiveTimer;
  LongSamplingCounter mMobileRadioActiveUnknownCount;
  LongSamplingCounter mMobileRadioActiveUnknownTime;
  EasyTimer mMobileRadioOnEasyTimer;
  int mMobileRadioPowerState = 1;
  int mModStepMode = 0;
  ControllerActivityCounterImpl mModemActivity;
  @GuardedBy("mModemNetworkLock")
  private String[] mModemIfaces = EmptyArray.STRING;
  private final Object mModemNetworkLock = new Object();
  final LongSamplingCounter[] mNetworkByteActivityCounters = new LongSamplingCounter[10];
  final LongSamplingCounter[] mNetworkPacketActivityCounters = new LongSamplingCounter[10];
  private final NetworkStatsFactory mNetworkStatsFactory = new NetworkStatsFactory();
  private final Pools.Pool<NetworkStats> mNetworkStatsPool = new Pools.SynchronizedPool(6);
  int mNextHistoryTagIdx = 0;
  long mNextMaxDailyDeadline = 0L;
  long mNextMinDailyDeadline = 0L;
  boolean mNoAutoReset;
  @GuardedBy("this")
  private int mNumAllUidCpuTimeReads;
  @GuardedBy("this")
  private long mNumBatchedSingleUidCpuTimeReads;
  private int mNumConnectivityChange;
  int mNumHistoryItems;
  int mNumHistoryTagChars = 0;
  @GuardedBy("this")
  private long mNumSingleUidCpuTimeReads;
  @GuardedBy("this")
  private int mNumUidsRemoved;
  boolean mOnBattery;
  @VisibleForTesting
  protected boolean mOnBatteryInternal;
  protected final TimeBase mOnBatteryScreenOffTimeBase = new TimeBase();
  protected final TimeBase mOnBatteryTimeBase = new TimeBase();
  @VisibleForTesting
  protected ArrayList<StopwatchTimer> mPartialTimers = new ArrayList();
  @GuardedBy("this")
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  protected Queue<UidToRemove> mPendingRemovedUids = new LinkedList();
  @GuardedBy("this")
  @VisibleForTesting
  protected final SparseIntArray mPendingUids = new SparseIntArray();
  Parcel mPendingWrite = null;
  @GuardedBy("this")
  public boolean mPerProcStateCpuTimesAvailable = true;
  int mPhoneDataConnectionType = -1;
  final StopwatchTimer[] mPhoneDataConnectionsTimer = new StopwatchTimer[21];
  EasyTimer mPhoneInCallEasyTimer;
  boolean mPhoneOn;
  StopwatchTimer mPhoneOnTimer;
  EasyTimer mPhoneScanningEasyTimer;
  private int mPhoneServiceState = -1;
  private int mPhoneServiceStateRaw = -1;
  StopwatchTimer mPhoneSignalScanningTimer;
  int mPhoneSignalStrengthBin = -1;
  int mPhoneSignalStrengthBinRaw = -1;
  final StopwatchTimer[] mPhoneSignalStrengthsTimer = new StopwatchTimer[6];
  private int mPhoneSimStateRaw = -1;
  private final PlatformIdleStateCallback mPlatformIdleStateCallback;
  EasyTimer mPluggedEasyTimer;
  @VisibleForTesting
  protected PowerProfile mPowerProfile;
  boolean mPowerSaveModeEnabled;
  StopwatchTimer mPowerSaveModeEnabledTimer;
  boolean mPretendScreenOff;
  int mReadHistoryChars;
  final BatteryStats.HistoryStepDetails mReadHistoryStepDetails = new BatteryStats.HistoryStepDetails();
  String[] mReadHistoryStrings;
  int[] mReadHistoryUids;
  private boolean mReadOverflow;
  long mRealtime;
  long mRealtimeStart;
  public boolean mRecordAllHistory;
  protected boolean mRecordingHistory = false;
  private final HashMap<String, SamplingTimer> mRpmStats = new HashMap();
  int mScreenBrightnessBin = -1;
  final StopwatchTimer[] mScreenBrightnessTimer = new StopwatchTimer[5];
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  protected StopwatchTimer mScreenDozeTimer;
  private final HashMap<String, SamplingTimer> mScreenOffRpmStats = new HashMap();
  EasyTimer mScreenOnDurEasyTimer;
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  protected StopwatchTimer mScreenOnTimer;
  @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
  protected int mScreenState = 0;
  int mSensorNesting;
  EasyTimer mSensorOnEasyTimer;
  final SparseArray<ArrayList<StopwatchTimer>> mSensorTimers = new SparseArray();
  boolean mShuttingDown;
  boolean mSoftAPOn;
  long mStartClockTime;
  int mStartCount;
  String mStartPlatformVersion;
  long mTempTotalCpuSystemTimeUs;
  long mTempTotalCpuUserTimeUs;
  final BatteryStats.HistoryStepDetails mTmpHistoryStepDetails = new BatteryStats.HistoryStepDetails();
  private final RpmStats mTmpRpmStats = new RpmStats();
  private final KernelWakelockStats mTmpWakelockStats = new KernelWakelockStats();
  long mTrackRunningHistoryElapsedRealtime = 0L;
  long mTrackRunningHistoryUptime = 0L;
  final SparseArray<Uid> mUidStats = new SparseArray();
  private int mUnpluggedNumConnectivityChange;
  long mUptime;
  long mUptimeStart;
  int mUsbDataState = 0;
  @VisibleForTesting
  protected UserInfoProvider mUserInfoProvider = null;
  EasyTimer mVideoOnEasyTimer;
  int mVideoOnNesting;
  StopwatchTimer mVideoOnTimer;
  final ArrayList<StopwatchTimer> mVideoTurnedOnTimers = new ArrayList();
  long[][] mWakeLockAllocationsUs;
  boolean mWakeLockImportant;
  int mWakeLockNesting;
  private final HashMap<String, SamplingTimer> mWakeupReasonStats = new HashMap();
  StopwatchTimer mWifiActiveTimer;
  ControllerActivityCounterImpl mWifiActivity;
  final SparseArray<ArrayList<StopwatchTimer>> mWifiBatchedScanTimers = new SparseArray();
  EasyTimer mWifiFullLockEasyTimer;
  int mWifiFullLockNesting = 0;
  EasyTimer mWifiHotspotOnEasyTimer;
  @GuardedBy("mWifiNetworkLock")
  private String[] mWifiIfaces = EmptyArray.STRING;
  EasyTimer mWifiMultiCastOnEasyTimer;
  int mWifiMulticastNesting = 0;
  final ArrayList<StopwatchTimer> mWifiMulticastTimers = new ArrayList();
  StopwatchTimer mWifiMulticastWakelockTimer;
  private final Object mWifiNetworkLock = new Object();
  boolean mWifiOn;
  EasyTimer mWifiOnEasyTimer;
  StopwatchTimer mWifiOnTimer;
  int mWifiRadioPowerState = 1;
  EasyTimer mWifiRunningEasyTimer;
  final ArrayList<StopwatchTimer> mWifiRunningTimers = new ArrayList();
  EasyTimer mWifiScanEasyTimer;
  int mWifiScanNesting = 0;
  final ArrayList<StopwatchTimer> mWifiScanTimers = new ArrayList();
  int mWifiSignalStrengthBin = -1;
  final StopwatchTimer[] mWifiSignalStrengthsTimer = new StopwatchTimer[5];
  int mWifiState = -1;
  final StopwatchTimer[] mWifiStateTimer = new StopwatchTimer[8];
  int mWifiSupplState = -1;
  final StopwatchTimer[] mWifiSupplStateTimer = new StopwatchTimer[13];
  final ArrayList<StopwatchTimer> mWindowTimers = new ArrayList();
  final ReentrantLock mWriteLock = new ReentrantLock();
  
  static
  {
    if ((!ActivityManager.isLowRamDeviceStatic()) && (!ActivityManager.isAsusLowRamDeviceStatic()))
    {
      MAX_HISTORY_ITEMS = 4000;
      MAX_MAX_HISTORY_ITEMS = 6000;
      MAX_WAKELOCKS_PER_UID = 200;
      MAX_HISTORY_BUFFER = 524288;
      MAX_MAX_HISTORY_BUFFER = 655360;
    }
    else
    {
      MAX_HISTORY_ITEMS = 800;
      MAX_MAX_HISTORY_ITEMS = 1200;
      MAX_WAKELOCKS_PER_UID = 40;
      MAX_HISTORY_BUFFER = 98304;
      MAX_MAX_HISTORY_BUFFER = 131072;
    }
  }
  
  public BatteryStatsImpl()
  {
    this(new SystemClocks());
  }
  
  public BatteryStatsImpl(Parcel paramParcel)
  {
    this(new SystemClocks(), paramParcel);
  }
  
  public BatteryStatsImpl(Clocks paramClocks)
  {
    init(paramClocks);
    mFile = null;
    mCheckinFile = null;
    mDailyFile = null;
    mHandler = null;
    mPlatformIdleStateCallback = null;
    mUserInfoProvider = null;
    mConstants = new Constants(mHandler);
    clearHistoryLocked();
  }
  
  public BatteryStatsImpl(Clocks paramClocks, Parcel paramParcel)
  {
    init(paramClocks);
    mFile = null;
    mCheckinFile = null;
    mDailyFile = null;
    mHandler = null;
    mExternalSync = null;
    mConstants = new Constants(mHandler);
    clearHistoryLocked();
    readFromParcel(paramParcel);
    mPlatformIdleStateCallback = null;
  }
  
  private BatteryStatsImpl(Clocks paramClocks, File paramFile, Handler paramHandler, PlatformIdleStateCallback paramPlatformIdleStateCallback, UserInfoProvider paramUserInfoProvider)
  {
    init(paramClocks);
    if (paramFile != null) {
      mFile = new JournaledFile(new File(paramFile, "batterystats.bin"), new File(paramFile, "batterystats.bin.tmp"));
    } else {
      mFile = null;
    }
    mCheckinFile = new AtomicFile(new File(paramFile, "batterystats-checkin.bin"));
    mDailyFile = new AtomicFile(new File(paramFile, "batterystats-daily.xml"));
    mHandler = new MyHandler(paramHandler.getLooper());
    mConstants = new Constants(mHandler);
    mStartCount += 1;
    mScreenOnTimer = new StopwatchTimer(mClocks, null, -1, null, mOnBatteryTimeBase);
    mScreenDozeTimer = new StopwatchTimer(mClocks, null, -1, null, mOnBatteryTimeBase);
    for (int i = 0; i < 5; i++) {
      mScreenBrightnessTimer[i] = new StopwatchTimer(mClocks, null, -100 - i, null, mOnBatteryTimeBase);
    }
    mInteractiveTimer = new StopwatchTimer(mClocks, null, -10, null, mOnBatteryTimeBase);
    mPowerSaveModeEnabledTimer = new StopwatchTimer(mClocks, null, -2, null, mOnBatteryTimeBase);
    mDeviceIdleModeLightTimer = new StopwatchTimer(mClocks, null, -11, null, mOnBatteryTimeBase);
    mDeviceIdleModeFullTimer = new StopwatchTimer(mClocks, null, -14, null, mOnBatteryTimeBase);
    mDeviceLightIdlingTimer = new StopwatchTimer(mClocks, null, -15, null, mOnBatteryTimeBase);
    mDeviceIdlingTimer = new StopwatchTimer(mClocks, null, -12, null, mOnBatteryTimeBase);
    mPhoneOnTimer = new StopwatchTimer(mClocks, null, -3, null, mOnBatteryTimeBase);
    for (i = 0; i < 6; i++) {
      mPhoneSignalStrengthsTimer[i] = new StopwatchTimer(mClocks, null, 65336 - i, null, mOnBatteryTimeBase);
    }
    mPhoneSignalScanningTimer = new StopwatchTimer(mClocks, null, 65337, null, mOnBatteryTimeBase);
    for (i = 0; i < 21; i++) {
      mPhoneDataConnectionsTimer[i] = new StopwatchTimer(mClocks, null, 65236 - i, null, mOnBatteryTimeBase);
    }
    for (i = 0; i < 10; i++)
    {
      mNetworkByteActivityCounters[i] = new LongSamplingCounter(mOnBatteryTimeBase);
      mNetworkPacketActivityCounters[i] = new LongSamplingCounter(mOnBatteryTimeBase);
    }
    mWifiActivity = new ControllerActivityCounterImpl(mOnBatteryTimeBase, 1);
    mBluetoothActivity = new ControllerActivityCounterImpl(mOnBatteryTimeBase, 1);
    mModemActivity = new ControllerActivityCounterImpl(mOnBatteryTimeBase, 5);
    mMobileRadioActiveTimer = new StopwatchTimer(mClocks, null, 65136, null, mOnBatteryTimeBase);
    mMobileRadioActivePerAppTimer = new StopwatchTimer(mClocks, null, 65135, null, mOnBatteryTimeBase);
    mMobileRadioActiveAdjustedTime = new LongSamplingCounter(mOnBatteryTimeBase);
    mMobileRadioActiveUnknownTime = new LongSamplingCounter(mOnBatteryTimeBase);
    mMobileRadioActiveUnknownCount = new LongSamplingCounter(mOnBatteryTimeBase);
    mWifiMulticastWakelockTimer = new StopwatchTimer(mClocks, null, 23, null, mOnBatteryTimeBase);
    mWifiOnTimer = new StopwatchTimer(mClocks, null, -4, null, mOnBatteryTimeBase);
    mGlobalWifiRunningTimer = new StopwatchTimer(mClocks, null, -5, null, mOnBatteryTimeBase);
    for (i = 0; i < 8; i++) {
      mWifiStateTimer[i] = new StopwatchTimer(mClocks, null, 64936 - i, null, mOnBatteryTimeBase);
    }
    for (i = 0; i < 13; i++) {
      mWifiSupplStateTimer[i] = new StopwatchTimer(mClocks, null, 64836 - i, null, mOnBatteryTimeBase);
    }
    for (i = 0; i < 5; i++) {
      mWifiSignalStrengthsTimer[i] = new StopwatchTimer(mClocks, null, 64736 - i, null, mOnBatteryTimeBase);
    }
    mWifiActiveTimer = new StopwatchTimer(mClocks, null, 64636, null, mOnBatteryTimeBase);
    for (i = 0; i < 2; i++) {
      mGpsSignalQualityTimer[i] = new StopwatchTimer(mClocks, null, 64536 - i, null, mOnBatteryTimeBase);
    }
    mAudioOnTimer = new StopwatchTimer(mClocks, null, -7, null, mOnBatteryTimeBase);
    mVideoOnTimer = new StopwatchTimer(mClocks, null, -8, null, mOnBatteryTimeBase);
    mFlashlightOnTimer = new StopwatchTimer(mClocks, null, -9, null, mOnBatteryTimeBase);
    mCameraOnTimer = new StopwatchTimer(mClocks, null, -13, null, mOnBatteryTimeBase);
    mBluetoothScanTimer = new StopwatchTimer(mClocks, null, -14, null, mOnBatteryTimeBase);
    mDischargeScreenOffCounter = new LongSamplingCounter(mOnBatteryScreenOffTimeBase);
    mDischargeScreenDozeCounter = new LongSamplingCounter(mOnBatteryTimeBase);
    mDischargeLightDozeCounter = new LongSamplingCounter(mOnBatteryTimeBase);
    mDischargeDeepDozeCounter = new LongSamplingCounter(mOnBatteryTimeBase);
    mDischargeCounter = new LongSamplingCounter(mOnBatteryTimeBase);
    mOnBatteryInternal = false;
    mOnBattery = false;
    initTimes(mClocks.uptimeMillis() * 1000L, mClocks.elapsedRealtime() * 1000L);
    paramClocks = Build.ID;
    mEndPlatformVersion = paramClocks;
    mStartPlatformVersion = paramClocks;
    mDischargeStartLevel = 0;
    mDischargeUnplugLevel = 0;
    mDischargePlugLevel = -1;
    mDischargeCurrentLevel = 0;
    mCurrentBatteryLevel = 0;
    initDischarge();
    clearHistoryLocked();
    updateDailyDeadlineLocked();
    mPlatformIdleStateCallback = paramPlatformIdleStateCallback;
    mUserInfoProvider = paramUserInfoProvider;
    mScreenOnDurEasyTimer = new EasyTimer();
    mMobileRadioOnEasyTimer = new EasyTimer();
    mWifiOnEasyTimer = new EasyTimer();
    mGpsOnEasyTimer = new EasyTimer();
    mSensorOnEasyTimer = new EasyTimer();
    mWifiScanEasyTimer = new EasyTimer();
    mWifiFullLockEasyTimer = new EasyTimer();
    mWifiRunningEasyTimer = new EasyTimer();
    mWifiMultiCastOnEasyTimer = new EasyTimer();
    mAudioOnEasyTimer = new EasyTimer();
    mCameraOnEasyTimer = new EasyTimer();
    mVideoOnEasyTimer = new EasyTimer();
    mLowPowerModeEasyTimer = new EasyTimer();
    mFlashLightEasyTimer = new EasyTimer();
    mPhoneInCallEasyTimer = new EasyTimer();
    mPhoneInCallEasyTimer = new EasyTimer();
    mPhoneScanningEasyTimer = new EasyTimer();
    mBleScanningEasyTimer = new EasyTimer();
    mWifiHotspotOnEasyTimer = new EasyTimer();
    mPluggedEasyTimer = new EasyTimer();
    mChargingOnEasyTimer = new EasyTimer();
  }
  
  public BatteryStatsImpl(File paramFile, Handler paramHandler, PlatformIdleStateCallback paramPlatformIdleStateCallback, UserInfoProvider paramUserInfoProvider)
  {
    this(new SystemClocks(), paramFile, paramHandler, paramPlatformIdleStateCallback, paramUserInfoProvider);
  }
  
  private void addHistoryBufferLocked(long paramLong, byte paramByte, BatteryStats.HistoryItem paramHistoryItem)
  {
    if (!mIteratingHistory)
    {
      mHistoryBufferLastPos = mHistoryBuffer.dataPosition();
      mHistoryLastLastWritten.setTo(mHistoryLastWritten);
      mHistoryLastWritten.setTo(mHistoryBaseTime + paramLong, paramByte, paramHistoryItem);
      BatteryStats.HistoryItem localHistoryItem = mHistoryLastWritten;
      states &= mActiveHistoryStates;
      localHistoryItem = mHistoryLastWritten;
      states2 &= mActiveHistoryStates2;
      writeHistoryDelta(mHistoryBuffer, mHistoryLastWritten, mHistoryLastLastWritten);
      mLastHistoryElapsedRealtime = paramLong;
      wakelockTag = null;
      wakeReasonTag = null;
      eventCode = 0;
      eventTag = null;
      return;
    }
    throw new IllegalStateException("Can't do this while iterating history!");
  }
  
  private void addModemTxPowerToHistory(ModemActivityInfo paramModemActivityInfo)
  {
    if (paramModemActivityInfo == null) {
      return;
    }
    try
    {
      paramModemActivityInfo = paramModemActivityInfo.getTxTimeMillis();
      if ((paramModemActivityInfo != null) && (paramModemActivityInfo.length == 5))
      {
        long l1 = mClocks.elapsedRealtime();
        long l2 = mClocks.uptimeMillis();
        int i = 0;
        int j = 1;
        while (j < paramModemActivityInfo.length)
        {
          int k = i;
          if (paramModemActivityInfo[j] > paramModemActivityInfo[i]) {
            k = j;
          }
          j++;
          i = k;
        }
        if (i == 4)
        {
          if (!mIsCellularTxPowerHigh)
          {
            paramModemActivityInfo = mHistoryCur;
            states2 |= 0x80000;
            addHistoryRecordLocked(l1, l2);
            mIsCellularTxPowerHigh = true;
          }
          return;
        }
        if (mIsCellularTxPowerHigh)
        {
          paramModemActivityInfo = mHistoryCur;
          states2 &= 0xFFF7FFFF;
          addHistoryRecordLocked(l1, l2);
          mIsCellularTxPowerHigh = false;
        }
        return;
      }
      return;
    }
    finally {}
  }
  
  private void addPackageChange(BatteryStats.PackageChange paramPackageChange)
  {
    if (mDailyPackageChanges == null) {
      mDailyPackageChanges = new ArrayList();
    }
    mDailyPackageChanges.add(paramPackageChange);
  }
  
  private int buildBatteryLevelInt(BatteryStats.HistoryItem paramHistoryItem)
  {
    return batteryLevel << 25 & 0xFE000000 | batteryTemperature << 15 & 0x1FF8000 | batteryVoltage << '\001' & 0x7FFE;
  }
  
  private int buildStateInt(BatteryStats.HistoryItem paramHistoryItem)
  {
    int i = 0;
    if ((batteryPlugType & 0x1) != 0) {
      i = 1;
    } else if ((batteryPlugType & 0x2) != 0) {
      i = 2;
    } else if ((batteryPlugType & 0x4) != 0) {
      i = 3;
    }
    return (batteryStatus & 0x7) << 29 | (batteryHealth & 0x7) << 26 | (i & 0x3) << 24 | states & 0xFFFFFF;
  }
  
  private void computeHistoryStepDetails(BatteryStats.HistoryStepDetails paramHistoryStepDetails1, BatteryStats.HistoryStepDetails paramHistoryStepDetails2)
  {
    BatteryStats.HistoryStepDetails localHistoryStepDetails;
    if (paramHistoryStepDetails2 != null) {
      localHistoryStepDetails = mTmpHistoryStepDetails;
    } else {
      localHistoryStepDetails = paramHistoryStepDetails1;
    }
    requestImmediateCpuUpdate();
    int i = 0;
    int j = 0;
    if (paramHistoryStepDetails2 == null)
    {
      i = mUidStats.size();
      while (j < i)
      {
        paramHistoryStepDetails1 = (Uid)mUidStats.valueAt(j);
        mLastStepUserTime = mCurStepUserTime;
        mLastStepSystemTime = mCurStepSystemTime;
        j++;
      }
      mLastStepCpuUserTime = mCurStepCpuUserTime;
      mLastStepCpuSystemTime = mCurStepCpuSystemTime;
      mLastStepStatUserTime = mCurStepStatUserTime;
      mLastStepStatSystemTime = mCurStepStatSystemTime;
      mLastStepStatIOWaitTime = mCurStepStatIOWaitTime;
      mLastStepStatIrqTime = mCurStepStatIrqTime;
      mLastStepStatSoftIrqTime = mCurStepStatSoftIrqTime;
      mLastStepStatIdleTime = mCurStepStatIdleTime;
      localHistoryStepDetails.clear();
      return;
    }
    userTime = ((int)(mCurStepCpuUserTime - mLastStepCpuUserTime));
    systemTime = ((int)(mCurStepCpuSystemTime - mLastStepCpuSystemTime));
    statUserTime = ((int)(mCurStepStatUserTime - mLastStepStatUserTime));
    statSystemTime = ((int)(mCurStepStatSystemTime - mLastStepStatSystemTime));
    statIOWaitTime = ((int)(mCurStepStatIOWaitTime - mLastStepStatIOWaitTime));
    statIrqTime = ((int)(mCurStepStatIrqTime - mLastStepStatIrqTime));
    statSoftIrqTime = ((int)(mCurStepStatSoftIrqTime - mLastStepStatSoftIrqTime));
    statIdlTime = ((int)(mCurStepStatIdleTime - mLastStepStatIdleTime));
    appCpuUid3 = -1;
    appCpuUid2 = -1;
    appCpuUid1 = -1;
    appCpuUTime3 = 0;
    appCpuUTime2 = 0;
    appCpuUTime1 = 0;
    appCpuSTime3 = 0;
    appCpuSTime2 = 0;
    appCpuSTime1 = 0;
    int k = mUidStats.size();
    for (j = i; j < k; j++)
    {
      paramHistoryStepDetails2 = (Uid)mUidStats.valueAt(j);
      int m = (int)(mCurStepUserTime - mLastStepUserTime);
      int n = (int)(mCurStepSystemTime - mLastStepSystemTime);
      i = m + n;
      mLastStepUserTime = mCurStepUserTime;
      mLastStepSystemTime = mCurStepSystemTime;
      if (i > appCpuUTime3 + appCpuSTime3) {
        if (i <= appCpuUTime2 + appCpuSTime2)
        {
          appCpuUid3 = mUid;
          appCpuUTime3 = m;
          appCpuSTime3 = n;
        }
        else
        {
          appCpuUid3 = appCpuUid2;
          appCpuUTime3 = appCpuUTime2;
          appCpuSTime3 = appCpuSTime2;
          if (i <= appCpuUTime1 + appCpuSTime1)
          {
            appCpuUid2 = mUid;
            appCpuUTime2 = m;
            appCpuSTime2 = n;
          }
          else
          {
            appCpuUid2 = appCpuUid1;
            appCpuUTime2 = appCpuUTime1;
            appCpuSTime2 = appCpuSTime1;
            appCpuUid1 = mUid;
            appCpuUTime1 = m;
            appCpuSTime1 = n;
          }
        }
      }
    }
    mLastStepCpuUserTime = mCurStepCpuUserTime;
    mLastStepCpuSystemTime = mCurStepCpuSystemTime;
    mLastStepStatUserTime = mCurStepStatUserTime;
    mLastStepStatSystemTime = mCurStepStatSystemTime;
    mLastStepStatIOWaitTime = mCurStepStatIOWaitTime;
    mLastStepStatIrqTime = mCurStepStatIrqTime;
    mLastStepStatSoftIrqTime = mCurStepStatSoftIrqTime;
    mLastStepStatIdleTime = mCurStepStatIdleTime;
  }
  
  private long computeTimePerLevel(long[] paramArrayOfLong, int paramInt)
  {
    if (paramInt <= 0) {
      return -1L;
    }
    long l = 0L;
    for (int i = 0; i < paramInt; i++) {
      l += (paramArrayOfLong[i] & 0xFFFFFFFFFF);
    }
    return l / paramInt;
  }
  
  private static void detachLongCounterIfNotNull(LongSamplingCounter paramLongSamplingCounter)
  {
    if (paramLongSamplingCounter != null) {
      paramLongSamplingCounter.detach();
    }
  }
  
  private static void detachTimerIfNotNull(Timer paramTimer)
  {
    if (paramTimer != null) {
      paramTimer.detach();
    }
  }
  
  private static String[] excludeFromStringArray(String[] paramArrayOfString, String paramString)
  {
    int i = ArrayUtils.indexOf(paramArrayOfString, paramString);
    if (i >= 0)
    {
      paramString = new String[paramArrayOfString.length - 1];
      if (i > 0) {
        System.arraycopy(paramArrayOfString, 0, paramString, 0, i);
      }
      if (i < paramArrayOfString.length - 1) {
        System.arraycopy(paramArrayOfString, i + 1, paramString, i, paramArrayOfString.length - i - 1);
      }
      return paramString;
    }
    return paramArrayOfString;
  }
  
  private int fixPhoneServiceState(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    if (mPhoneSimStateRaw == 1)
    {
      i = paramInt1;
      if (paramInt1 == 1)
      {
        i = paramInt1;
        if (paramInt2 > 0) {
          i = 0;
        }
      }
    }
    return i;
  }
  
  private int getAttributionUid(int paramInt, WorkSource.WorkChain paramWorkChain)
  {
    if (paramWorkChain != null) {
      return mapUid(paramWorkChain.getAttributionUid());
    }
    return mapUid(paramInt);
  }
  
  private ModemActivityInfo getDeltaModemActivityInfo(ModemActivityInfo paramModemActivityInfo)
  {
    if (paramModemActivityInfo == null) {
      return null;
    }
    Object localObject = new int[5];
    for (int i = 0; i < 5; i++) {
      localObject[i] = (paramModemActivityInfo.getTxTimeMillis()[i] - mLastModemActivityInfo.getTxTimeMillis()[i]);
    }
    localObject = new ModemActivityInfo(paramModemActivityInfo.getTimestamp(), paramModemActivityInfo.getSleepTimeMillis() - mLastModemActivityInfo.getSleepTimeMillis(), paramModemActivityInfo.getIdleTimeMillis() - mLastModemActivityInfo.getIdleTimeMillis(), (int[])localObject, paramModemActivityInfo.getRxTimeMillis() - mLastModemActivityInfo.getRxTimeMillis(), paramModemActivityInfo.getEnergyUsed() - mLastModemActivityInfo.getEnergyUsed());
    mLastModemActivityInfo = paramModemActivityInfo;
    return localObject;
  }
  
  private int getPowerManagerWakeLockLevel(int paramInt)
  {
    if (paramInt != 18)
    {
      switch (paramInt)
      {
      default: 
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Illegal wakelock type in batterystats: ");
        localStringBuilder.append(paramInt);
        Slog.e("BatteryStatsImpl", localStringBuilder.toString());
        return -1;
      case 2: 
        Slog.e("BatteryStatsImpl", "Illegal window wakelock type observed in batterystats.");
        return -1;
      case 1: 
        return 26;
      }
      return 1;
    }
    return 128;
  }
  
  private static String[] includeInStringArray(String[] paramArrayOfString, String paramString)
  {
    if (ArrayUtils.indexOf(paramArrayOfString, paramString) >= 0) {
      return paramArrayOfString;
    }
    String[] arrayOfString = new String[paramArrayOfString.length + 1];
    System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramArrayOfString.length);
    arrayOfString[paramArrayOfString.length] = paramString;
    return arrayOfString;
  }
  
  private void init(Clocks paramClocks)
  {
    mClocks = paramClocks;
  }
  
  private void initActiveHistoryEventsLocked(long paramLong1, long paramLong2)
  {
    for (int i = 0; i < 22; i++) {
      if ((mRecordAllHistory) || (i != 1))
      {
        Object localObject = mActiveEvents.getStateForEvent(i);
        if (localObject != null)
        {
          localObject = ((HashMap)localObject).entrySet().iterator();
          while (((Iterator)localObject).hasNext())
          {
            Map.Entry localEntry = (Map.Entry)((Iterator)localObject).next();
            SparseIntArray localSparseIntArray = (SparseIntArray)localEntry.getValue();
            for (int j = 0; j < localSparseIntArray.size(); j++) {
              addHistoryEventLocked(paramLong1, paramLong2, i, (String)localEntry.getKey(), localSparseIntArray.keyAt(j));
            }
          }
        }
      }
    }
  }
  
  @GuardedBy("this")
  private boolean initKernelSingleUidTimeReaderLocked()
  {
    KernelSingleUidTimeReader localKernelSingleUidTimeReader = mKernelSingleUidTimeReader;
    boolean bool1 = false;
    if (localKernelSingleUidTimeReader == null)
    {
      if (mPowerProfile == null) {
        return false;
      }
      if (mCpuFreqs == null) {
        mCpuFreqs = mKernelUidCpuFreqTimeReader.readFreqs(mPowerProfile);
      }
      if (mCpuFreqs != null)
      {
        mKernelSingleUidTimeReader = new KernelSingleUidTimeReader(mCpuFreqs.length);
      }
      else
      {
        mPerProcStateCpuTimesAvailable = mKernelUidCpuFreqTimeReader.allUidTimesAvailable();
        return false;
      }
    }
    boolean bool2 = bool1;
    if (mKernelUidCpuFreqTimeReader.allUidTimesAvailable())
    {
      bool2 = bool1;
      if (mKernelSingleUidTimeReader.singleUidCpuTimesAvailable()) {
        bool2 = true;
      }
    }
    mPerProcStateCpuTimesAvailable = bool2;
    return true;
  }
  
  public static boolean isOnBattery(int paramInt1, int paramInt2)
  {
    boolean bool = true;
    if ((paramInt1 != 0) || (paramInt2 == 1)) {
      bool = false;
    }
    return bool;
  }
  
  private void noteAlarmStartOrFinishLocked(int paramInt1, String paramString, WorkSource paramWorkSource, int paramInt2)
  {
    if (!mRecordAllHistory) {
      return;
    }
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    int i = 0;
    if (paramWorkSource != null)
    {
      for (int j = 0; j < paramWorkSource.size(); j++)
      {
        paramInt2 = mapUid(paramWorkSource.get(j));
        if (mActiveEvents.updateState(paramInt1, paramString, paramInt2, i)) {
          addHistoryEventLocked(l1, l2, paramInt1, paramString, paramInt2);
        }
      }
      paramWorkSource = paramWorkSource.getWorkChains();
      int k = paramInt2;
      if (paramWorkSource != null) {
        for (j = i;; j++)
        {
          k = paramInt2;
          if (j >= paramWorkSource.size()) {
            break;
          }
          paramInt2 = mapUid(((WorkSource.WorkChain)paramWorkSource.get(j)).getAttributionUid());
          if (mActiveEvents.updateState(paramInt1, paramString, paramInt2, i)) {
            addHistoryEventLocked(l1, l2, paramInt1, paramString, paramInt2);
          }
        }
      }
    }
    else
    {
      paramInt2 = mapUid(paramInt2);
      if (mActiveEvents.updateState(paramInt1, paramString, paramInt2, 0)) {
        addHistoryEventLocked(l1, l2, paramInt1, paramString, paramInt2);
      }
    }
  }
  
  private void noteBluetoothScanStartedLocked(WorkSource.WorkChain paramWorkChain, int paramInt, boolean paramBoolean)
  {
    paramInt = getAttributionUid(paramInt, paramWorkChain);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mBluetoothScanNesting == 0)
    {
      paramWorkChain = mHistoryCur;
      states2 |= 0x100000;
      addHistoryRecordLocked(l1, l2);
      mBluetoothScanTimer.startRunningLocked(l1);
    }
    mBluetoothScanNesting += 1;
    getUidStatsLocked(paramInt).noteBluetoothScanStartedLocked(l1, paramBoolean);
    mBleScanningEasyTimer.startRunningLocked(l1);
  }
  
  private void noteBluetoothScanStoppedLocked(WorkSource.WorkChain paramWorkChain, int paramInt, boolean paramBoolean)
  {
    paramInt = getAttributionUid(paramInt, paramWorkChain);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    mBluetoothScanNesting -= 1;
    if (mBluetoothScanNesting == 0)
    {
      paramWorkChain = mHistoryCur;
      states2 &= 0xFFEFFFFF;
      addHistoryRecordLocked(l1, l2);
      mBluetoothScanTimer.stopRunningLocked(l1);
    }
    getUidStatsLocked(paramInt).noteBluetoothScanStoppedLocked(l1, paramBoolean);
    mBleScanningEasyTimer.stopRunningLocked(l1);
  }
  
  private void noteLongPartialWakeLockFinishInternal(String paramString1, String paramString2, int paramInt)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (paramString2 != null) {
      paramString1 = paramString2;
    }
    if (!mActiveEvents.updateState(16404, paramString1, paramInt, 0)) {
      return;
    }
    addHistoryEventLocked(l1, l2, 16404, paramString1, paramInt);
  }
  
  private void noteLongPartialWakeLockStartInternal(String paramString1, String paramString2, int paramInt)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (paramString2 != null) {
      paramString1 = paramString2;
    }
    if (!mActiveEvents.updateState(32788, paramString1, paramInt, 0)) {
      return;
    }
    addHistoryEventLocked(l1, l2, 32788, paramString1, paramInt);
  }
  
  private void noteMobileRadioApWakeupLocked(long paramLong1, long paramLong2, int paramInt)
  {
    paramInt = mapUid(paramInt);
    addHistoryEventLocked(paramLong1, paramLong2, 19, "", paramInt);
    getUidStatsLocked(paramInt).noteMobileRadioApWakeupLocked();
  }
  
  private void noteStartGpsLocked(int paramInt, WorkSource.WorkChain paramWorkChain)
  {
    paramInt = getAttributionUid(paramInt, paramWorkChain);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mGpsNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states |= 0x20000000;
      addHistoryRecordLocked(l1, l2);
    }
    mGpsNesting += 1;
    if (paramWorkChain == null) {
      StatsLog.write_non_chained(6, paramInt, null, 1);
    } else {
      StatsLog.write(6, paramWorkChain.getUids(), paramWorkChain.getTags(), 1);
    }
    getUidStatsLocked(paramInt).noteStartGps(l1);
    mGpsOnEasyTimer.startRunningLocked(l1);
  }
  
  private void noteStopGpsLocked(int paramInt, WorkSource.WorkChain paramWorkChain)
  {
    paramInt = getAttributionUid(paramInt, paramWorkChain);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    mGpsNesting -= 1;
    if (mGpsNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states &= 0xDFFFFFFF;
      addHistoryRecordLocked(l1, l2);
      stopAllGpsSignalQualityTimersLocked(-1);
      mGpsSignalQualityBin = -1;
    }
    if (paramWorkChain == null) {
      StatsLog.write_non_chained(6, paramInt, null, 0);
    } else {
      StatsLog.write(6, paramWorkChain.getUids(), paramWorkChain.getTags(), 0);
    }
    getUidStatsLocked(paramInt).noteStopGps(l1);
    mGpsOnEasyTimer.stopRunningLocked(l1);
  }
  
  private void noteUsbConnectionStateLocked(boolean paramBoolean)
  {
    int i;
    if (paramBoolean) {
      i = 2;
    } else {
      i = 1;
    }
    if (mUsbDataState != i)
    {
      mUsbDataState = i;
      BatteryStats.HistoryItem localHistoryItem;
      if (paramBoolean)
      {
        localHistoryItem = mHistoryCur;
        states2 |= 0x40000;
      }
      else
      {
        localHistoryItem = mHistoryCur;
        states2 &= 0xFFFBFFFF;
      }
      addHistoryRecordLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis());
    }
  }
  
  private void noteWifiRadioApWakeupLocked(long paramLong1, long paramLong2, int paramInt)
  {
    paramInt = mapUid(paramInt);
    addHistoryEventLocked(paramLong1, paramLong2, 19, "", paramInt);
    getUidStatsLocked(paramInt).noteWifiRadioApWakeupLocked();
  }
  
  private void readBatteryLevelInt(int paramInt, BatteryStats.HistoryItem paramHistoryItem)
  {
    batteryLevel = ((byte)(byte)((0xFE000000 & paramInt) >>> 25));
    batteryTemperature = ((short)(short)((0x1FF8000 & paramInt) >>> 15));
    batteryVoltage = ((char)(char)((paramInt & 0x7FFE) >>> 1));
  }
  
  private void readDailyItemsLocked(XmlPullParser paramXmlPullParser)
  {
    try
    {
      int i;
      do
      {
        i = paramXmlPullParser.next();
      } while ((i != 2) && (i != 1));
      if (i == 2)
      {
        i = paramXmlPullParser.getDepth();
        for (;;)
        {
          int j = paramXmlPullParser.next();
          if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
            break;
          }
          if ((j != 3) && (j != 4)) {
            if (paramXmlPullParser.getName().equals("item"))
            {
              readDailyItemTagLocked(paramXmlPullParser);
            }
            else
            {
              localStringBuilder1 = new java/lang/StringBuilder;
              localStringBuilder1.<init>();
              localStringBuilder1.append("Unknown element under <daily-items>: ");
              localStringBuilder1.append(paramXmlPullParser.getName());
              Slog.w("BatteryStatsImpl", localStringBuilder1.toString());
              XmlUtils.skipCurrentTag(paramXmlPullParser);
            }
          }
        }
      }
      paramXmlPullParser = new java/lang/IllegalStateException;
      paramXmlPullParser.<init>("no start tag found");
      throw paramXmlPullParser;
    }
    catch (IndexOutOfBoundsException paramXmlPullParser)
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Failed parsing daily ");
      localStringBuilder1.append(paramXmlPullParser);
      Slog.w("BatteryStatsImpl", localStringBuilder1.toString());
    }
    catch (IOException paramXmlPullParser)
    {
      localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Failed parsing daily ");
      localStringBuilder1.append(paramXmlPullParser);
      Slog.w("BatteryStatsImpl", localStringBuilder1.toString());
    }
    catch (XmlPullParserException paramXmlPullParser)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      localStringBuilder1.append("Failed parsing daily ");
      localStringBuilder1.append(paramXmlPullParser);
      Slog.w("BatteryStatsImpl", localStringBuilder1.toString());
    }
    catch (NumberFormatException localNumberFormatException)
    {
      paramXmlPullParser = new StringBuilder();
      paramXmlPullParser.append("Failed parsing daily ");
      paramXmlPullParser.append(localNumberFormatException);
      Slog.w("BatteryStatsImpl", paramXmlPullParser.toString());
    }
    catch (NullPointerException paramXmlPullParser)
    {
      localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("Failed parsing daily ");
      localStringBuilder2.append(paramXmlPullParser);
      Slog.w("BatteryStatsImpl", localStringBuilder2.toString());
    }
    catch (IllegalStateException paramXmlPullParser)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      localStringBuilder2.append("Failed parsing daily ");
      localStringBuilder2.append(paramXmlPullParser);
      Slog.w("BatteryStatsImpl", localStringBuilder2.toString());
    }
  }
  
  private void readHistoryTag(int paramInt, BatteryStats.HistoryTag paramHistoryTag)
  {
    string = mReadHistoryStrings[paramInt];
    uid = mReadHistoryUids[paramInt];
    poolIdx = paramInt;
  }
  
  private NetworkStats readNetworkStatsLocked(String[] paramArrayOfString)
  {
    try
    {
      if (!ArrayUtils.isEmpty(paramArrayOfString))
      {
        NetworkStats localNetworkStats = mNetworkStatsFactory.readNetworkStatsDetail(-1, paramArrayOfString, 0, (NetworkStats)mNetworkStatsPool.acquire());
        return localNetworkStats;
      }
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("failed to read network stats for ifaces: ");
      localStringBuilder.append(Arrays.toString(paramArrayOfString));
      Slog.e("BatteryStatsImpl", localStringBuilder.toString());
    }
    return null;
  }
  
  private void recordCurrentTimeChangeLocked(long paramLong1, long paramLong2, long paramLong3)
  {
    if (mRecordingHistory)
    {
      mHistoryCur.currentTime = paramLong1;
      addHistoryBufferLocked(paramLong2, (byte)5, mHistoryCur);
      mHistoryCur.currentTime = 0L;
    }
  }
  
  private void recordShutdownLocked(long paramLong1, long paramLong2)
  {
    if (mRecordingHistory)
    {
      mHistoryCur.currentTime = System.currentTimeMillis();
      addHistoryBufferLocked(paramLong1, (byte)8, mHistoryCur);
      mHistoryCur.currentTime = 0L;
    }
  }
  
  private void registerUsbStateReceiver(Context paramContext)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.hardware.usb.action.USB_STATE");
    paramContext.registerReceiver(new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent arg2)
      {
        boolean bool = ???.getBooleanExtra("connected", false);
        synchronized (BatteryStatsImpl.this)
        {
          BatteryStatsImpl.this.noteUsbConnectionStateLocked(bool);
          return;
        }
      }
    }, localIntentFilter);
    try
    {
      if (mUsbDataState == 0)
      {
        paramContext = paramContext.registerReceiver(null, localIntentFilter);
        boolean bool1 = false;
        boolean bool2 = bool1;
        if (paramContext != null)
        {
          bool2 = bool1;
          if (paramContext.getBooleanExtra("connected", false)) {
            bool2 = true;
          }
        }
        noteUsbConnectionStateLocked(bool2);
      }
      return;
    }
    finally {}
  }
  
  private void reportChangesToStatsLog(BatteryStats.HistoryItem paramHistoryItem, int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramHistoryItem == null) || (batteryStatus != paramInt1)) {
      StatsLog.write(31, paramInt1);
    }
    if ((paramHistoryItem == null) || (batteryPlugType != paramInt2)) {
      StatsLog.write(32, paramInt2);
    }
    if ((paramHistoryItem == null) || (batteryLevel != paramInt3)) {
      StatsLog.write(30, paramInt3);
    }
  }
  
  private void requestImmediateCpuUpdate()
  {
    mExternalSync.scheduleCpuSyncDueToWakelockChange(0L);
  }
  
  private void requestWakelockCpuUpdate()
  {
    mExternalSync.scheduleCpuSyncDueToWakelockChange(5000L);
  }
  
  private void resetAllStatsLocked()
  {
    long l1 = mClocks.uptimeMillis();
    long l2 = mClocks.elapsedRealtime();
    mStartCount = 0;
    initTimes(l1 * 1000L, l2 * 1000L);
    mScreenOnTimer.reset(false);
    mScreenDozeTimer.reset(false);
    for (int i = 0; i < 5; i++) {
      mScreenBrightnessTimer[i].reset(false);
    }
    if (mPowerProfile != null) {
      mEstimatedBatteryCapacity = ((int)mPowerProfile.getBatteryCapacity());
    } else {
      mEstimatedBatteryCapacity = -1;
    }
    mMinLearnedBatteryCapacity = -1;
    mMaxLearnedBatteryCapacity = -1;
    mInteractiveTimer.reset(false);
    mPowerSaveModeEnabledTimer.reset(false);
    mLastIdleTimeStart = l2;
    mLongestLightIdleTime = 0L;
    mLongestFullIdleTime = 0L;
    mDeviceIdleModeLightTimer.reset(false);
    mDeviceIdleModeFullTimer.reset(false);
    mDeviceLightIdlingTimer.reset(false);
    mDeviceIdlingTimer.reset(false);
    mPhoneOnTimer.reset(false);
    mAudioOnTimer.reset(false);
    mVideoOnTimer.reset(false);
    mFlashlightOnTimer.reset(false);
    mCameraOnTimer.reset(false);
    mBluetoothScanTimer.reset(false);
    for (i = 0; i < 6; i++) {
      mPhoneSignalStrengthsTimer[i].reset(false);
    }
    mPhoneSignalScanningTimer.reset(false);
    for (i = 0; i < 21; i++) {
      mPhoneDataConnectionsTimer[i].reset(false);
    }
    for (i = 0; i < 10; i++)
    {
      mNetworkByteActivityCounters[i].reset(false);
      mNetworkPacketActivityCounters[i].reset(false);
    }
    mMobileRadioActiveTimer.reset(false);
    mMobileRadioActivePerAppTimer.reset(false);
    mMobileRadioActiveAdjustedTime.reset(false);
    mMobileRadioActiveUnknownTime.reset(false);
    mMobileRadioActiveUnknownCount.reset(false);
    mWifiOnTimer.reset(false);
    mGlobalWifiRunningTimer.reset(false);
    for (i = 0; i < 8; i++) {
      mWifiStateTimer[i].reset(false);
    }
    for (i = 0; i < 13; i++) {
      mWifiSupplStateTimer[i].reset(false);
    }
    for (i = 0; i < 5; i++) {
      mWifiSignalStrengthsTimer[i].reset(false);
    }
    mWifiMulticastWakelockTimer.reset(false);
    mWifiActiveTimer.reset(false);
    mWifiActivity.reset(false);
    for (i = 0; i < 2; i++) {
      mGpsSignalQualityTimer[i].reset(false);
    }
    mBluetoothActivity.reset(false);
    mModemActivity.reset(false);
    mUnpluggedNumConnectivityChange = 0;
    mLoadedNumConnectivityChange = 0;
    mNumConnectivityChange = 0;
    int j;
    for (i = 0; i < mUidStats.size(); i = j + 1)
    {
      j = i;
      if (((Uid)mUidStats.valueAt(i)).reset(l1 * 1000L, l2 * 1000L))
      {
        mUidStats.remove(mUidStats.keyAt(i));
        j = i - 1;
      }
    }
    Object localObject1;
    Object localObject2;
    if (mRpmStats.size() > 0)
    {
      localObject1 = mRpmStats.values().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (SamplingTimer)((Iterator)localObject1).next();
        mOnBatteryTimeBase.remove((TimeBaseObs)localObject2);
      }
      mRpmStats.clear();
    }
    if (mScreenOffRpmStats.size() > 0)
    {
      localObject2 = mScreenOffRpmStats.values().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (SamplingTimer)((Iterator)localObject2).next();
        mOnBatteryScreenOffTimeBase.remove((TimeBaseObs)localObject1);
      }
      mScreenOffRpmStats.clear();
    }
    if (mKernelWakelockStats.size() > 0)
    {
      localObject1 = mKernelWakelockStats.values().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject2 = (SamplingTimer)((Iterator)localObject1).next();
        mOnBatteryScreenOffTimeBase.remove((TimeBaseObs)localObject2);
      }
      mKernelWakelockStats.clear();
    }
    if (mKernelMemoryStats.size() > 0)
    {
      for (i = 0; i < mKernelMemoryStats.size(); i++) {
        mOnBatteryTimeBase.remove((TimeBaseObs)mKernelMemoryStats.valueAt(i));
      }
      mKernelMemoryStats.clear();
    }
    if (mWakeupReasonStats.size() > 0)
    {
      localObject2 = mWakeupReasonStats.values().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject1 = (SamplingTimer)((Iterator)localObject2).next();
        mOnBatteryTimeBase.remove((TimeBaseObs)localObject1);
      }
      mWakeupReasonStats.clear();
    }
    mLastHistoryStepDetails = null;
    mLastStepCpuSystemTime = 0L;
    mLastStepCpuUserTime = 0L;
    mCurStepCpuSystemTime = 0L;
    mCurStepCpuUserTime = 0L;
    mCurStepCpuUserTime = 0L;
    mLastStepCpuUserTime = 0L;
    mCurStepCpuSystemTime = 0L;
    mLastStepCpuSystemTime = 0L;
    mCurStepStatUserTime = 0L;
    mLastStepStatUserTime = 0L;
    mCurStepStatSystemTime = 0L;
    mLastStepStatSystemTime = 0L;
    mCurStepStatIOWaitTime = 0L;
    mLastStepStatIOWaitTime = 0L;
    mCurStepStatIrqTime = 0L;
    mLastStepStatIrqTime = 0L;
    mCurStepStatSoftIrqTime = 0L;
    mLastStepStatSoftIrqTime = 0L;
    mCurStepStatIdleTime = 0L;
    mLastStepStatIdleTime = 0L;
    mNumAllUidCpuTimeReads = 0;
    mNumUidsRemoved = 0;
    initDischarge();
    clearHistoryLocked();
    mHandler.sendEmptyMessage(4);
  }
  
  private static void resetLongCounterIfNotNull(LongSamplingCounter paramLongSamplingCounter, boolean paramBoolean)
  {
    if (paramLongSamplingCounter != null) {
      paramLongSamplingCounter.reset(paramBoolean);
    }
  }
  
  private static boolean resetTimerIfNotNull(DualTimer paramDualTimer, boolean paramBoolean)
  {
    if (paramDualTimer != null) {
      return paramDualTimer.reset(paramBoolean);
    }
    return true;
  }
  
  private static boolean resetTimerIfNotNull(Timer paramTimer, boolean paramBoolean)
  {
    if (paramTimer != null) {
      return paramTimer.reset(paramBoolean);
    }
    return true;
  }
  
  private void scheduleSyncExternalStatsLocked(String paramString, int paramInt)
  {
    if (mExternalSync != null) {
      mExternalSync.scheduleSync(paramString, paramInt);
    }
  }
  
  private void startRecordingHistory(long paramLong1, long paramLong2, boolean paramBoolean)
  {
    mRecordingHistory = true;
    mHistoryCur.currentTime = System.currentTimeMillis();
    byte b1;
    byte b2;
    if (paramBoolean)
    {
      b1 = 7;
      b2 = b1;
    }
    else
    {
      b1 = 5;
      b2 = b1;
    }
    addHistoryBufferLocked(paramLong1, b2, mHistoryCur);
    mHistoryCur.currentTime = 0L;
    if (paramBoolean) {
      initActiveHistoryEventsLocked(paramLong1, paramLong2);
    }
  }
  
  private void updateAllPhoneStateLocked(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    int j = 0;
    mPhoneServiceStateRaw = paramInt1;
    mPhoneSimStateRaw = paramInt2;
    mPhoneSignalStrengthBinRaw = paramInt3;
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    int k = paramInt1;
    if (paramInt2 == 1)
    {
      k = paramInt1;
      if (paramInt1 == 1)
      {
        k = paramInt1;
        if (paramInt3 > 0) {
          k = 0;
        }
      }
    }
    BatteryStats.HistoryItem localHistoryItem;
    if (k == 3)
    {
      paramInt3 = -1;
      paramInt1 = j;
    }
    else if (k == 0)
    {
      paramInt1 = j;
    }
    else
    {
      paramInt1 = j;
      if (k == 1)
      {
        paramInt2 = 1;
        int m = 0;
        i = paramInt2;
        paramInt1 = j;
        paramInt3 = m;
        if (!mPhoneSignalScanningTimer.isRunningLocked())
        {
          localHistoryItem = mHistoryCur;
          states |= 0x200000;
          paramInt1 = 1;
          mPhoneSignalScanningTimer.startRunningLocked(l1);
          mPhoneScanningEasyTimer.startRunningLocked(l1);
          paramInt3 = m;
          i = paramInt2;
        }
      }
    }
    paramInt2 = paramInt1;
    if (i == 0)
    {
      paramInt2 = paramInt1;
      if (mPhoneSignalScanningTimer.isRunningLocked())
      {
        localHistoryItem = mHistoryCur;
        states &= 0xFFDFFFFF;
        paramInt2 = 1;
        mPhoneSignalScanningTimer.stopRunningLocked(l1);
        mPhoneScanningEasyTimer.stopRunningLocked(l1);
      }
    }
    paramInt1 = paramInt2;
    if (mPhoneServiceState != k)
    {
      mHistoryCur.states = (mHistoryCur.states & 0xFE3F | k << 6);
      paramInt1 = 1;
      mPhoneServiceState = k;
    }
    paramInt2 = paramInt1;
    if (mPhoneSignalStrengthBin != paramInt3)
    {
      if (mPhoneSignalStrengthBin >= 0) {
        mPhoneSignalStrengthsTimer[mPhoneSignalStrengthBin].stopRunningLocked(l1);
      }
      if (paramInt3 >= 0)
      {
        if (!mPhoneSignalStrengthsTimer[paramInt3].isRunningLocked()) {
          mPhoneSignalStrengthsTimer[paramInt3].startRunningLocked(l1);
        }
        mHistoryCur.states = (mHistoryCur.states & 0xFFFFFFC7 | paramInt3 << 3);
        paramInt1 = 1;
        StatsLog.write(40, paramInt3);
      }
      else
      {
        stopAllPhoneSignalStrengthTimersLocked(-1);
      }
      mPhoneSignalStrengthBin = paramInt3;
      paramInt2 = paramInt1;
    }
    if (paramInt2 != 0) {
      addHistoryRecordLocked(l1, l2);
    }
  }
  
  private void updateBatteryPropertiesLocked()
  {
    try
    {
      IBatteryPropertiesRegistrar.Stub.asInterface(ServiceManager.getService("batteryproperties")).scheduleUpdate();
    }
    catch (RemoteException localRemoteException) {}
  }
  
  private void updateNewDischargeScreenLevelLocked(int paramInt)
  {
    if (isScreenOn(paramInt))
    {
      mDischargeScreenOnUnplugLevel = mDischargeCurrentLevel;
      mDischargeScreenOffUnplugLevel = 0;
      mDischargeScreenDozeUnplugLevel = 0;
    }
    else if (isScreenDoze(paramInt))
    {
      mDischargeScreenOnUnplugLevel = 0;
      mDischargeScreenDozeUnplugLevel = mDischargeCurrentLevel;
      mDischargeScreenOffUnplugLevel = 0;
    }
    else if (isScreenOff(paramInt))
    {
      mDischargeScreenOnUnplugLevel = 0;
      mDischargeScreenDozeUnplugLevel = 0;
      mDischargeScreenOffUnplugLevel = mDischargeCurrentLevel;
    }
  }
  
  private void updateOldDischargeScreenLevelLocked(int paramInt)
  {
    if (isScreenOn(paramInt))
    {
      paramInt = mDischargeScreenOnUnplugLevel - mDischargeCurrentLevel;
      if (paramInt > 0)
      {
        mDischargeAmountScreenOn += paramInt;
        mDischargeAmountScreenOnSinceCharge += paramInt;
      }
    }
    else if (isScreenDoze(paramInt))
    {
      paramInt = mDischargeScreenDozeUnplugLevel - mDischargeCurrentLevel;
      if (paramInt > 0)
      {
        mDischargeAmountScreenDoze += paramInt;
        mDischargeAmountScreenDozeSinceCharge += paramInt;
      }
    }
    else if (isScreenOff(paramInt))
    {
      paramInt = mDischargeScreenOffUnplugLevel - mDischargeCurrentLevel;
      if (paramInt > 0)
      {
        mDischargeAmountScreenOff += paramInt;
        mDischargeAmountScreenOffSinceCharge += paramInt;
      }
    }
  }
  
  private void writeDailyItemsLocked(XmlSerializer paramXmlSerializer)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    paramXmlSerializer.startDocument(null, Boolean.valueOf(true));
    paramXmlSerializer.startTag(null, "daily-items");
    for (int i = 0; i < mDailyItems.size(); i++)
    {
      BatteryStats.DailyItem localDailyItem = (BatteryStats.DailyItem)mDailyItems.get(i);
      paramXmlSerializer.startTag(null, "item");
      paramXmlSerializer.attribute(null, "start", Long.toString(mStartTime));
      paramXmlSerializer.attribute(null, "end", Long.toString(mEndTime));
      writeDailyLevelSteps(paramXmlSerializer, "dis", mDischargeSteps, localStringBuilder);
      writeDailyLevelSteps(paramXmlSerializer, "chg", mChargeSteps, localStringBuilder);
      if (mPackageChanges != null) {
        for (int j = 0; j < mPackageChanges.size(); j++)
        {
          BatteryStats.PackageChange localPackageChange = (BatteryStats.PackageChange)mPackageChanges.get(j);
          if (mUpdate)
          {
            paramXmlSerializer.startTag(null, "upd");
            paramXmlSerializer.attribute(null, "pkg", mPackageName);
            paramXmlSerializer.attribute(null, "ver", Long.toString(mVersionCode));
            paramXmlSerializer.endTag(null, "upd");
          }
          else
          {
            paramXmlSerializer.startTag(null, "rem");
            paramXmlSerializer.attribute(null, "pkg", mPackageName);
            paramXmlSerializer.endTag(null, "rem");
          }
        }
      }
      paramXmlSerializer.endTag(null, "item");
    }
    paramXmlSerializer.endTag(null, "daily-items");
    paramXmlSerializer.endDocument();
  }
  
  private void writeDailyLevelSteps(XmlSerializer paramXmlSerializer, String paramString, BatteryStats.LevelStepTracker paramLevelStepTracker, StringBuilder paramStringBuilder)
    throws IOException
  {
    if (paramLevelStepTracker != null)
    {
      paramXmlSerializer.startTag(null, paramString);
      paramXmlSerializer.attribute(null, "n", Integer.toString(mNumStepDurations));
      for (int i = 0; i < mNumStepDurations; i++)
      {
        paramXmlSerializer.startTag(null, "s");
        paramStringBuilder.setLength(0);
        paramLevelStepTracker.encodeEntryAt(i, paramStringBuilder);
        paramXmlSerializer.attribute(null, "v", paramStringBuilder.toString());
        paramXmlSerializer.endTag(null, "s");
      }
      paramXmlSerializer.endTag(null, paramString);
    }
  }
  
  private int writeHistoryTag(BatteryStats.HistoryTag paramHistoryTag)
  {
    Object localObject = (Integer)mHistoryTagPool.get(paramHistoryTag);
    int i;
    if (localObject != null)
    {
      i = ((Integer)localObject).intValue();
    }
    else
    {
      i = mNextHistoryTagIdx;
      localObject = new BatteryStats.HistoryTag();
      ((BatteryStats.HistoryTag)localObject).setTo(paramHistoryTag);
      poolIdx = i;
      mHistoryTagPool.put(localObject, Integer.valueOf(i));
      mNextHistoryTagIdx += 1;
      mNumHistoryTagChars += string.length() + 1;
    }
    return i;
  }
  
  @VisibleForTesting
  public long[] addCpuTimes(long[] paramArrayOfLong1, long[] paramArrayOfLong2)
  {
    if ((paramArrayOfLong1 != null) && (paramArrayOfLong2 != null))
    {
      for (int i = paramArrayOfLong1.length - 1; i >= 0; i--) {
        paramArrayOfLong1[i] += paramArrayOfLong2[i];
      }
      return paramArrayOfLong1;
    }
    if (paramArrayOfLong1 == null) {
      if (paramArrayOfLong2 == null) {
        paramArrayOfLong1 = null;
      } else {
        paramArrayOfLong1 = paramArrayOfLong2;
      }
    }
    return paramArrayOfLong1;
  }
  
  void addHistoryBufferLocked(long paramLong, BatteryStats.HistoryItem paramHistoryItem)
  {
    if ((mHaveBatteryLevel) && (mRecordingHistory))
    {
      long l1 = mHistoryBaseTime;
      long l2 = mHistoryLastWritten.time;
      int i = mHistoryLastWritten.states;
      int j = states;
      int k = mActiveHistoryStates;
      int m = mHistoryLastWritten.states2;
      int n = states2;
      int i1 = mActiveHistoryStates2;
      int i2 = mHistoryLastWritten.states;
      int i3 = mHistoryLastLastWritten.states;
      int i4 = mHistoryLastWritten.states2;
      int i5 = mHistoryLastLastWritten.states2;
      if ((mHistoryBufferLastPos >= 0) && (mHistoryLastWritten.cmd == 0) && (l1 + paramLong - l2 < 1000L) && (((i ^ j & k) & (i2 ^ i3)) == 0) && (((m ^ n & i1) & (i4 ^ i5)) == 0) && ((mHistoryLastWritten.wakelockTag == null) || (wakelockTag == null)) && ((mHistoryLastWritten.wakeReasonTag == null) || (wakeReasonTag == null)) && (mHistoryLastWritten.stepDetails == null) && ((mHistoryLastWritten.eventCode == 0) || (eventCode == 0)) && (mHistoryLastWritten.batteryLevel == batteryLevel) && (mHistoryLastWritten.batteryStatus == batteryStatus) && (mHistoryLastWritten.batteryHealth == batteryHealth) && (mHistoryLastWritten.batteryPlugType == batteryPlugType) && (mHistoryLastWritten.batteryTemperature == batteryTemperature) && (mHistoryLastWritten.batteryVoltage == batteryVoltage))
      {
        mHistoryBuffer.setDataSize(mHistoryBufferLastPos);
        mHistoryBuffer.setDataPosition(mHistoryBufferLastPos);
        mHistoryBufferLastPos = -1;
        paramLong = mHistoryLastWritten.time - mHistoryBaseTime;
        if (mHistoryLastWritten.wakelockTag != null)
        {
          wakelockTag = localWakelockTag;
          wakelockTag.setTo(mHistoryLastWritten.wakelockTag);
        }
        if (mHistoryLastWritten.wakeReasonTag != null)
        {
          wakeReasonTag = localWakeReasonTag;
          wakeReasonTag.setTo(mHistoryLastWritten.wakeReasonTag);
        }
        if (mHistoryLastWritten.eventCode != 0)
        {
          eventCode = mHistoryLastWritten.eventCode;
          eventTag = localEventTag;
          eventTag.setTo(mHistoryLastWritten.eventTag);
        }
        mHistoryLastWritten.setTo(mHistoryLastLastWritten);
      }
      i = 0;
      m = mHistoryBuffer.dataSize();
      if (m >= MAX_MAX_HISTORY_BUFFER * 3)
      {
        resetAllStatsLocked();
        i = 1;
      }
      else if (m >= MAX_HISTORY_BUFFER)
      {
        if (!mHistoryOverflow)
        {
          mHistoryOverflow = true;
          addHistoryBufferLocked(paramLong, (byte)0, paramHistoryItem);
          addHistoryBufferLocked(paramLong, (byte)6, paramHistoryItem);
          return;
        }
        i = 0;
        i1 = states & 0xFFE30000 & mActiveHistoryStates;
        if (mHistoryLastWritten.states != i1)
        {
          i = mActiveHistoryStates;
          mActiveHistoryStates &= (i1 | 0x1CFFFF);
          if (i != mActiveHistoryStates) {
            i = 1;
          } else {
            i = 0;
          }
          i = 0x0 | i;
        }
        i1 = states2 & 0x683F0000 & mActiveHistoryStates2;
        if (mHistoryLastWritten.states2 != i1)
        {
          i3 = mActiveHistoryStates2;
          mActiveHistoryStates2 &= (0x97C0FFFF | i1);
          if (i3 != mActiveHistoryStates2) {
            i1 = 1;
          } else {
            i1 = 0;
          }
          i |= i1;
        }
        if ((i == 0) && (mHistoryLastWritten.batteryLevel == batteryLevel) && ((m >= MAX_MAX_HISTORY_BUFFER) || (((mHistoryLastWritten.states ^ states) & 0x1C0000) == 0) || (((mHistoryLastWritten.states2 ^ states2) & 0x97C00000) == 0))) {
          return;
        }
        addHistoryBufferLocked(paramLong, (byte)0, paramHistoryItem);
        return;
      }
      if ((m == 0) || (i != 0))
      {
        currentTime = System.currentTimeMillis();
        if (i != 0) {
          addHistoryBufferLocked(paramLong, (byte)6, paramHistoryItem);
        }
        addHistoryBufferLocked(paramLong, (byte)7, paramHistoryItem);
      }
      addHistoryBufferLocked(paramLong, (byte)0, paramHistoryItem);
      return;
    }
  }
  
  public void addHistoryEventLocked(long paramLong1, long paramLong2, int paramInt1, String paramString, int paramInt2)
  {
    mHistoryCur.eventCode = paramInt1;
    mHistoryCur.eventTag = mHistoryCur.localEventTag;
    mHistoryCur.eventTag.string = paramString;
    mHistoryCur.eventTag.uid = paramInt2;
    addHistoryRecordLocked(paramLong1, paramLong2);
  }
  
  void addHistoryRecordInnerLocked(long paramLong, BatteryStats.HistoryItem paramHistoryItem)
  {
    addHistoryBufferLocked(paramLong, paramHistoryItem);
  }
  
  void addHistoryRecordLocked(long paramLong1, long paramLong2)
  {
    if (mTrackRunningHistoryElapsedRealtime != 0L)
    {
      long l1 = paramLong1 - mTrackRunningHistoryElapsedRealtime;
      long l2 = paramLong2 - mTrackRunningHistoryUptime;
      if (l2 < l1 - 20L)
      {
        mHistoryAddTmp.setTo(mHistoryLastWritten);
        mHistoryAddTmp.wakelockTag = null;
        mHistoryAddTmp.wakeReasonTag = null;
        mHistoryAddTmp.eventCode = 0;
        localHistoryItem = mHistoryAddTmp;
        states &= 0x7FFFFFFF;
        addHistoryRecordInnerLocked(paramLong1 - (l1 - l2), mHistoryAddTmp);
      }
    }
    BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
    states |= 0x80000000;
    mTrackRunningHistoryElapsedRealtime = paramLong1;
    mTrackRunningHistoryUptime = paramLong2;
    addHistoryRecordInnerLocked(paramLong1, mHistoryCur);
  }
  
  void addHistoryRecordLocked(long paramLong1, long paramLong2, byte paramByte, BatteryStats.HistoryItem paramHistoryItem)
  {
    BatteryStats.HistoryItem localHistoryItem = mHistoryCache;
    if (localHistoryItem != null) {
      mHistoryCache = next;
    } else {
      localHistoryItem = new BatteryStats.HistoryItem();
    }
    localHistoryItem.setTo(mHistoryBaseTime + paramLong1, paramByte, paramHistoryItem);
    addHistoryRecordLocked(localHistoryItem);
  }
  
  void addHistoryRecordLocked(BatteryStats.HistoryItem paramHistoryItem)
  {
    mNumHistoryItems += 1;
    next = null;
    mHistoryLastEnd = mHistoryEnd;
    if (mHistoryEnd != null)
    {
      mHistoryEnd.next = paramHistoryItem;
      mHistoryEnd = paramHistoryItem;
    }
    else
    {
      mHistoryEnd = paramHistoryItem;
      mHistory = paramHistoryItem;
    }
  }
  
  public void addIsolatedUidLocked(int paramInt1, int paramInt2)
  {
    mIsolatedUids.put(paramInt1, paramInt2);
    StatsLog.write(43, paramInt2, paramInt1, 1);
    getUidStatsLocked(paramInt2).addIsolatedUid(paramInt1);
  }
  
  void aggregateLastWakeupUptimeLocked(long paramLong)
  {
    if (mLastWakeupReason != null)
    {
      paramLong -= mLastWakeupUptimeMs;
      getWakeupReasonTimerLocked(mLastWakeupReason).add(paramLong * 1000L, 1);
      StatsLog.write(36, mLastWakeupReason, 1000L * paramLong);
      mLastWakeupReason = null;
    }
  }
  
  void clearHistoryLocked()
  {
    mHistoryBaseTime = 0L;
    mLastHistoryElapsedRealtime = 0L;
    mTrackRunningHistoryElapsedRealtime = 0L;
    mTrackRunningHistoryUptime = 0L;
    mHistoryBuffer.setDataSize(0);
    mHistoryBuffer.setDataPosition(0);
    mHistoryBuffer.setDataCapacity(MAX_HISTORY_BUFFER / 2);
    mHistoryLastLastWritten.clear();
    mHistoryLastWritten.clear();
    mHistoryTagPool.clear();
    mNextHistoryTagIdx = 0;
    mNumHistoryTagChars = 0;
    mHistoryBufferLastPos = -1;
    mHistoryOverflow = false;
    mActiveHistoryStates = -1;
    mActiveHistoryStates2 = -1;
  }
  
  public void clearPendingRemovedUids()
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mConstants.UID_REMOVE_DELAY_MS;
    while ((!mPendingRemovedUids.isEmpty()) && (mPendingRemovedUids.peek()).timeAddedInQueue < l1 - l2)) {
      ((UidToRemove)mPendingRemovedUids.poll()).remove();
    }
  }
  
  public void commitCurrentHistoryBatchLocked()
  {
    mHistoryLastWritten.cmd = ((byte)-1);
  }
  
  /* Error */
  public void commitPendingDataToDisk()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 944	com/android/internal/os/BatteryStatsImpl:mPendingWrite	Landroid/os/Parcel;
    //   6: astore_1
    //   7: aload_0
    //   8: aconst_null
    //   9: putfield 944	com/android/internal/os/BatteryStatsImpl:mPendingWrite	Landroid/os/Parcel;
    //   12: aload_1
    //   13: ifnonnull +6 -> 19
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_0
    //   22: getfield 949	com/android/internal/os/BatteryStatsImpl:mWriteLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   25: invokevirtual 2262	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   28: invokestatic 645	android/os/SystemClock:uptimeMillis	()J
    //   31: lstore_2
    //   32: new 2264	java/io/FileOutputStream
    //   35: astore 4
    //   37: aload 4
    //   39: aload_0
    //   40: getfield 954	com/android/internal/os/BatteryStatsImpl:mFile	Lcom/android/internal/util/JournaledFile;
    //   43: invokevirtual 2268	com/android/internal/util/JournaledFile:chooseForWrite	()Ljava/io/File;
    //   46: invokespecial 2269	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   49: aload 4
    //   51: aload_1
    //   52: invokevirtual 2273	android/os/Parcel:marshall	()[B
    //   55: invokevirtual 2276	java/io/FileOutputStream:write	([B)V
    //   58: aload 4
    //   60: invokevirtual 2279	java/io/FileOutputStream:flush	()V
    //   63: aload 4
    //   65: invokestatic 2285	android/os/FileUtils:sync	(Ljava/io/FileOutputStream;)Z
    //   68: pop
    //   69: aload 4
    //   71: invokevirtual 2288	java/io/FileOutputStream:close	()V
    //   74: aload_0
    //   75: getfield 954	com/android/internal/os/BatteryStatsImpl:mFile	Lcom/android/internal/util/JournaledFile;
    //   78: invokevirtual 2291	com/android/internal/util/JournaledFile:commit	()V
    //   81: ldc_w 2293
    //   84: invokestatic 645	android/os/SystemClock:uptimeMillis	()J
    //   87: lload_2
    //   88: lsub
    //   89: invokestatic 2299	com/android/internal/logging/EventLogTags:writeCommitSysConfigFile	(Ljava/lang/String;J)V
    //   92: goto +29 -> 121
    //   95: astore 4
    //   97: goto +36 -> 133
    //   100: astore 4
    //   102: ldc_w 2301
    //   105: ldc_w 2303
    //   108: aload 4
    //   110: invokestatic 2306	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   113: pop
    //   114: aload_0
    //   115: getfield 954	com/android/internal/os/BatteryStatsImpl:mFile	Lcom/android/internal/util/JournaledFile;
    //   118: invokevirtual 2309	com/android/internal/util/JournaledFile:rollback	()V
    //   121: aload_1
    //   122: invokevirtual 2312	android/os/Parcel:recycle	()V
    //   125: aload_0
    //   126: getfield 949	com/android/internal/os/BatteryStatsImpl:mWriteLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   129: invokevirtual 2315	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   132: return
    //   133: aload_1
    //   134: invokevirtual 2312	android/os/Parcel:recycle	()V
    //   137: aload_0
    //   138: getfield 949	com/android/internal/os/BatteryStatsImpl:mWriteLock	Ljava/util/concurrent/locks/ReentrantLock;
    //   141: invokevirtual 2315	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   144: aload 4
    //   146: athrow
    //   147: astore_1
    //   148: aload_0
    //   149: monitorexit
    //   150: aload_1
    //   151: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	152	0	this	BatteryStatsImpl
    //   6	128	1	localParcel	Parcel
    //   147	4	1	localObject1	Object
    //   31	57	2	l	long
    //   35	35	4	localFileOutputStream	FileOutputStream
    //   95	1	4	localObject2	Object
    //   100	45	4	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   28	92	95	finally
    //   102	121	95	finally
    //   28	92	100	java/io/IOException
    //   2	12	147	finally
    //   16	18	147	finally
    //   19	21	147	finally
    //   148	150	147	finally
  }
  
  public long computeBatteryRealtime(long paramLong, int paramInt)
  {
    return mOnBatteryTimeBase.computeRealtime(paramLong, paramInt);
  }
  
  public long computeBatteryScreenOffRealtime(long paramLong, int paramInt)
  {
    return mOnBatteryScreenOffTimeBase.computeRealtime(paramLong, paramInt);
  }
  
  public long computeBatteryScreenOffUptime(long paramLong, int paramInt)
  {
    return mOnBatteryScreenOffTimeBase.computeUptime(paramLong, paramInt);
  }
  
  public long computeBatteryTimeRemaining(long paramLong)
  {
    if (!mOnBattery) {
      return -1L;
    }
    if (mDischargeStepTracker.mNumStepDurations < 1) {
      return -1L;
    }
    paramLong = mDischargeStepTracker.computeTimePerLevel();
    if (paramLong <= 0L) {
      return -1L;
    }
    return mCurrentBatteryLevel * paramLong * 1000L;
  }
  
  public long computeBatteryUptime(long paramLong, int paramInt)
  {
    return mOnBatteryTimeBase.computeUptime(paramLong, paramInt);
  }
  
  public long computeChargeTimeRemaining(long paramLong)
  {
    if (mOnBattery) {
      return -1L;
    }
    if (mChargeStepTracker.mNumStepDurations < 1) {
      return -1L;
    }
    paramLong = mChargeStepTracker.computeTimePerLevel();
    if (paramLong <= 0L) {
      return -1L;
    }
    return (100 - mCurrentBatteryLevel) * paramLong * 1000L;
  }
  
  public long computeRealtime(long paramLong, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0L;
    case 2: 
      return paramLong - mOnBatteryTimeBase.getRealtimeStart();
    case 1: 
      return paramLong - mRealtimeStart;
    }
    return mRealtime + (paramLong - mRealtimeStart);
  }
  
  public long computeUptime(long paramLong, int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0L;
    case 2: 
      return paramLong - mOnBatteryTimeBase.getUptimeStart();
    case 1: 
      return paramLong - mUptimeStart;
    }
    return mUptime + (paramLong - mUptimeStart);
  }
  
  public void copyFromAllUidsCpuTimes()
  {
    try
    {
      copyFromAllUidsCpuTimes(mOnBatteryTimeBase.isRunning(), mOnBatteryScreenOffTimeBase.isRunning());
      return;
    }
    finally {}
  }
  
  public void copyFromAllUidsCpuTimes(boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      if (!mConstants.TRACK_CPU_TIMES_BY_PROC_STATE) {
        return;
      }
      if (!initKernelSingleUidTimeReaderLocked()) {
        return;
      }
      SparseArray localSparseArray = mKernelUidCpuFreqTimeReader.getAllUidCpuFreqTimeMs();
      if (mKernelSingleUidTimeReader.hasStaleData())
      {
        mKernelSingleUidTimeReader.setAllUidsCpuTimesMs(localSparseArray);
        mKernelSingleUidTimeReader.markDataAsStale(false);
        mPendingUids.clear();
        return;
      }
      for (int i = localSparseArray.size() - 1; i >= 0; i--)
      {
        int j = localSparseArray.keyAt(i);
        Uid localUid = getAvailableUidStatsLocked(mapUid(j));
        if (localUid != null)
        {
          long[] arrayOfLong = (long[])localSparseArray.valueAt(i);
          if (arrayOfLong != null)
          {
            arrayOfLong = mKernelSingleUidTimeReader.computeDelta(j, (long[])arrayOfLong.clone());
            if ((paramBoolean1) && (arrayOfLong != null))
            {
              int k = mPendingUids.indexOfKey(j);
              if (k >= 0)
              {
                j = mPendingUids.valueAt(k);
                mPendingUids.removeAt(k);
              }
              else
              {
                j = mProcessState;
              }
              if ((j >= 0) && (j < 7))
              {
                localUid.addProcStateTimesMs(j, arrayOfLong, paramBoolean1);
                localUid.addProcStateScreenOffTimesMs(j, arrayOfLong, paramBoolean2);
              }
            }
          }
        }
      }
      return;
    }
    finally {}
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  @GuardedBy("this")
  public void dumpConstantsLocked(PrintWriter paramPrintWriter)
  {
    mConstants.dumpLocked(paramPrintWriter);
  }
  
  @GuardedBy("this")
  public void dumpCpuStatsLocked(PrintWriter paramPrintWriter)
  {
    int i = mUidStats.size();
    paramPrintWriter.println("Per UID CPU user & system time in ms:");
    int k;
    Object localObject;
    for (int j = 0; j < i; j++)
    {
      k = mUidStats.keyAt(j);
      localObject = (Uid)mUidStats.get(k);
      paramPrintWriter.print("  ");
      paramPrintWriter.print(k);
      paramPrintWriter.print(": ");
      paramPrintWriter.print(((Uid)localObject).getUserCpuTimeUs(0) / 1000L);
      paramPrintWriter.print(" ");
      paramPrintWriter.println(((Uid)localObject).getSystemCpuTimeUs(0) / 1000L);
    }
    paramPrintWriter.println("Per UID CPU active time in ms:");
    for (j = 0; j < i; j++)
    {
      k = mUidStats.keyAt(j);
      localObject = (Uid)mUidStats.get(k);
      if (((Uid)localObject).getCpuActiveTime() > 0L)
      {
        paramPrintWriter.print("  ");
        paramPrintWriter.print(k);
        paramPrintWriter.print(": ");
        paramPrintWriter.println(((Uid)localObject).getCpuActiveTime());
      }
    }
    paramPrintWriter.println("Per UID CPU cluster time in ms:");
    for (j = 0; j < i; j++)
    {
      k = mUidStats.keyAt(j);
      localObject = ((Uid)mUidStats.get(k)).getCpuClusterTimes();
      if (localObject != null)
      {
        paramPrintWriter.print("  ");
        paramPrintWriter.print(k);
        paramPrintWriter.print(": ");
        paramPrintWriter.println(Arrays.toString((long[])localObject));
      }
    }
    paramPrintWriter.println("Per UID CPU frequency time in ms:");
    for (j = 0; j < i; j++)
    {
      k = mUidStats.keyAt(j);
      localObject = ((Uid)mUidStats.get(k)).getCpuFreqTimes(0);
      if (localObject != null)
      {
        paramPrintWriter.print("  ");
        paramPrintWriter.print(k);
        paramPrintWriter.print(": ");
        paramPrintWriter.println(Arrays.toString((long[])localObject));
      }
    }
  }
  
  public void dumpLocked(Context paramContext, PrintWriter paramPrintWriter, int paramInt1, int paramInt2, long paramLong)
  {
    super.dumpLocked(paramContext, paramPrintWriter, paramInt1, paramInt2, paramLong);
    paramPrintWriter.print("Total cpu time reads: ");
    paramPrintWriter.println(mNumSingleUidCpuTimeReads);
    paramPrintWriter.print("Batched cpu time reads: ");
    paramPrintWriter.println(mNumBatchedSingleUidCpuTimeReads);
    paramPrintWriter.print("Batching Duration (min): ");
    paramPrintWriter.println((mClocks.uptimeMillis() - mCpuTimeReadsTrackingStartTime) / 60000L);
    paramPrintWriter.print("All UID cpu time reads since the later of device start or stats reset: ");
    paramPrintWriter.println(mNumAllUidCpuTimeReads);
    paramPrintWriter.print("UIDs removed since the later of device start or stats reset: ");
    paramPrintWriter.println(mNumUidsRemoved);
  }
  
  boolean ensureStartClockTime(long paramLong)
  {
    if (((paramLong > 31536000000L) && (mStartClockTime < paramLong - 31536000000L)) || (mStartClockTime > paramLong))
    {
      mStartClockTime = (paramLong - (mClocks.elapsedRealtime() - mRealtimeStart / 1000L));
      return true;
    }
    return false;
  }
  
  public void finishAddingCpuLocked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    mCurStepCpuUserTime += paramInt1;
    mCurStepCpuSystemTime += paramInt2;
    mCurStepStatUserTime += paramInt3;
    mCurStepStatSystemTime += paramInt4;
    mCurStepStatIOWaitTime += paramInt5;
    mCurStepStatIrqTime += paramInt6;
    mCurStepStatSoftIrqTime += paramInt7;
    mCurStepStatIdleTime += paramInt8;
  }
  
  public void finishIteratingHistoryLocked()
  {
    mIteratingHistory = false;
    mHistoryBuffer.setDataPosition(mHistoryBuffer.dataSize());
    mReadHistoryStrings = null;
  }
  
  public void finishIteratingOldHistoryLocked()
  {
    mIteratingHistory = false;
    mHistoryBuffer.setDataPosition(mHistoryBuffer.dataSize());
    mHistoryIterator = null;
  }
  
  public int getAudioOn()
  {
    return mAudioOnEasyTimer.getStatus();
  }
  
  public Uid getAvailableUidStatsLocked(int paramInt)
  {
    return (Uid)mUidStats.get(paramInt);
  }
  
  public long getAwakeTimeBattery()
  {
    return computeBatteryUptime(getBatteryUptimeLocked(), 1);
  }
  
  public long getAwakeTimePlugged()
  {
    return mClocks.uptimeMillis() * 1000L - getAwakeTimeBattery();
  }
  
  public int getBatteryLevel()
  {
    return mCurrentBatteryLevel;
  }
  
  public long getBatteryRealtime(long paramLong)
  {
    return mOnBatteryTimeBase.getRealtime(paramLong);
  }
  
  public long getBatteryUptime(long paramLong)
  {
    return mOnBatteryTimeBase.getUptime(paramLong);
  }
  
  protected long getBatteryUptimeLocked()
  {
    return mOnBatteryTimeBase.getUptime(mClocks.uptimeMillis() * 1000L);
  }
  
  public int getBleScanning()
  {
    return mBleScanningEasyTimer.getStatus();
  }
  
  public BatteryStats.ControllerActivityCounter getBluetoothControllerActivity()
  {
    return mBluetoothActivity;
  }
  
  public long getBluetoothScanTime(long paramLong, int paramInt)
  {
    return mBluetoothScanTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public int getCameraOn()
  {
    return mCameraOnEasyTimer.getStatus();
  }
  
  public long getCameraOnTime(long paramLong, int paramInt)
  {
    return mCameraOnTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public CellularBatteryStats getCellularBatteryStats()
  {
    CellularBatteryStats localCellularBatteryStats = new CellularBatteryStats();
    int i = 0;
    long l1 = SystemClock.elapsedRealtime() * 1000L;
    BatteryStats.ControllerActivityCounter localControllerActivityCounter = getModemControllerActivity();
    long l2 = localControllerActivityCounter.getSleepTimeCounter().getCountLocked(0);
    long l3 = localControllerActivityCounter.getIdleTimeCounter().getCountLocked(0);
    long l4 = localControllerActivityCounter.getRxTimeCounter().getCountLocked(0);
    long l5 = localControllerActivityCounter.getPowerCounter().getCountLocked(0);
    long[] arrayOfLong1 = new long[21];
    for (int j = 0; j < arrayOfLong1.length; j++) {
      arrayOfLong1[j] = (getPhoneDataConnectionTime(j, l1, 0) / 1000L);
    }
    long[] arrayOfLong2 = new long[6];
    for (j = 0; j < arrayOfLong2.length; j++) {
      arrayOfLong2[j] = (getPhoneSignalStrengthTime(j, l1, 0) / 1000L);
    }
    long[] arrayOfLong3 = new long[Math.min(5, localControllerActivityCounter.getTxTimeCounters().length)];
    long l6 = 0L;
    for (j = 0; j < arrayOfLong3.length; j++)
    {
      arrayOfLong3[j] = localControllerActivityCounter.getTxTimeCounters()[j].getCountLocked(0);
      l6 += arrayOfLong3[j];
    }
    localCellularBatteryStats.setLoggingDurationMs(computeBatteryRealtime(l1, 0) / 1000L);
    localCellularBatteryStats.setKernelActiveTimeMs(getMobileRadioActiveTime(l1, 0) / 1000L);
    localCellularBatteryStats.setNumPacketsTx(getNetworkActivityPackets(1, 0));
    localCellularBatteryStats.setNumBytesTx(getNetworkActivityBytes(1, 0));
    localCellularBatteryStats.setNumPacketsRx(getNetworkActivityPackets(0, 0));
    localCellularBatteryStats.setNumBytesRx(getNetworkActivityBytes(0, 0));
    localCellularBatteryStats.setSleepTimeMs(l2);
    localCellularBatteryStats.setIdleTimeMs(l3);
    localCellularBatteryStats.setRxTimeMs(l4);
    localCellularBatteryStats.setEnergyConsumedMaMs(l5);
    localCellularBatteryStats.setTimeInRatMs(arrayOfLong1);
    localCellularBatteryStats.setTimeInRxSignalStrengthLevelMs(arrayOfLong2);
    localCellularBatteryStats.setTxTimeMs(arrayOfLong3);
    return localCellularBatteryStats;
  }
  
  public BatteryStats.LevelStepTracker getChargeLevelStepTracker()
  {
    return mChargeStepTracker;
  }
  
  public long[] getCpuFreqs()
  {
    return mCpuFreqs;
  }
  
  public long getCurrentDailyStartTime()
  {
    return mDailyStartTime;
  }
  
  public BatteryStats.LevelStepTracker getDailyChargeLevelStepTracker()
  {
    return mDailyChargeStepTracker;
  }
  
  public BatteryStats.LevelStepTracker getDailyDischargeLevelStepTracker()
  {
    return mDailyDischargeStepTracker;
  }
  
  public BatteryStats.DailyItem getDailyItemLocked(int paramInt)
  {
    paramInt = mDailyItems.size() - 1 - paramInt;
    BatteryStats.DailyItem localDailyItem;
    if (paramInt >= 0) {
      localDailyItem = (BatteryStats.DailyItem)mDailyItems.get(paramInt);
    } else {
      localDailyItem = null;
    }
    return localDailyItem;
  }
  
  public ArrayList<BatteryStats.PackageChange> getDailyPackageChanges()
  {
    return mDailyPackageChanges;
  }
  
  public int getDeviceIdleModeCount(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return 0;
    case 2: 
      return mDeviceIdleModeFullTimer.getCountLocked(paramInt2);
    }
    return mDeviceIdleModeLightTimer.getCountLocked(paramInt2);
  }
  
  public long getDeviceIdleModeTime(int paramInt1, long paramLong, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return 0L;
    case 2: 
      return mDeviceIdleModeFullTimer.getTotalTimeLocked(paramLong, paramInt2);
    }
    return mDeviceIdleModeLightTimer.getTotalTimeLocked(paramLong, paramInt2);
  }
  
  public int getDeviceIdlingCount(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return 0;
    case 2: 
      return mDeviceIdlingTimer.getCountLocked(paramInt2);
    }
    return mDeviceLightIdlingTimer.getCountLocked(paramInt2);
  }
  
  public long getDeviceIdlingTime(int paramInt1, long paramLong, int paramInt2)
  {
    switch (paramInt1)
    {
    default: 
      return 0L;
    case 2: 
      return mDeviceIdlingTimer.getTotalTimeLocked(paramLong, paramInt2);
    }
    return mDeviceLightIdlingTimer.getTotalTimeLocked(paramLong, paramInt2);
  }
  
  public int getDischargeAmount(int paramInt)
  {
    if (paramInt == 0) {
      paramInt = getHighDischargeAmountSinceCharge();
    } else {
      paramInt = getDischargeStartLevel() - getDischargeCurrentLevel();
    }
    int i = paramInt;
    if (paramInt < 0) {
      i = 0;
    }
    return i;
  }
  
  public int getDischargeAmountScreenDoze()
  {
    try
    {
      int i = mDischargeAmountScreenDoze;
      int j = i;
      if (mOnBattery)
      {
        j = i;
        if (isScreenDoze(mScreenState))
        {
          j = i;
          if (mDischargeCurrentLevel < mDischargeScreenDozeUnplugLevel) {
            j = i + (mDischargeScreenDozeUnplugLevel - mDischargeCurrentLevel);
          }
        }
      }
      return j;
    }
    finally {}
  }
  
  public int getDischargeAmountScreenDozeSinceCharge()
  {
    try
    {
      int i = mDischargeAmountScreenDozeSinceCharge;
      int j = i;
      if (mOnBattery)
      {
        j = i;
        if (isScreenDoze(mScreenState))
        {
          j = i;
          if (mDischargeCurrentLevel < mDischargeScreenDozeUnplugLevel) {
            j = i + (mDischargeScreenDozeUnplugLevel - mDischargeCurrentLevel);
          }
        }
      }
      return j;
    }
    finally {}
  }
  
  public int getDischargeAmountScreenOff()
  {
    try
    {
      int i = mDischargeAmountScreenOff;
      int j = i;
      if (mOnBattery)
      {
        j = i;
        if (isScreenOff(mScreenState))
        {
          j = i;
          if (mDischargeCurrentLevel < mDischargeScreenOffUnplugLevel) {
            j = i + (mDischargeScreenOffUnplugLevel - mDischargeCurrentLevel);
          }
        }
      }
      i = getDischargeAmountScreenDoze();
      return i + j;
    }
    finally {}
  }
  
  public int getDischargeAmountScreenOffSinceCharge()
  {
    try
    {
      int i = mDischargeAmountScreenOffSinceCharge;
      int j = i;
      if (mOnBattery)
      {
        j = i;
        if (isScreenOff(mScreenState))
        {
          j = i;
          if (mDischargeCurrentLevel < mDischargeScreenOffUnplugLevel) {
            j = i + (mDischargeScreenOffUnplugLevel - mDischargeCurrentLevel);
          }
        }
      }
      i = getDischargeAmountScreenDozeSinceCharge();
      return i + j;
    }
    finally {}
  }
  
  public int getDischargeAmountScreenOn()
  {
    try
    {
      int i = mDischargeAmountScreenOn;
      int j = i;
      if (mOnBattery)
      {
        j = i;
        if (isScreenOn(mScreenState))
        {
          j = i;
          if (mDischargeCurrentLevel < mDischargeScreenOnUnplugLevel) {
            j = i + (mDischargeScreenOnUnplugLevel - mDischargeCurrentLevel);
          }
        }
      }
      return j;
    }
    finally {}
  }
  
  public int getDischargeAmountScreenOnSinceCharge()
  {
    try
    {
      int i = mDischargeAmountScreenOnSinceCharge;
      int j = i;
      if (mOnBattery)
      {
        j = i;
        if (isScreenOn(mScreenState))
        {
          j = i;
          if (mDischargeCurrentLevel < mDischargeScreenOnUnplugLevel) {
            j = i + (mDischargeScreenOnUnplugLevel - mDischargeCurrentLevel);
          }
        }
      }
      return j;
    }
    finally {}
  }
  
  public int getDischargeCurrentLevel()
  {
    try
    {
      int i = getDischargeCurrentLevelLocked();
      return i;
    }
    finally {}
  }
  
  public int getDischargeCurrentLevelLocked()
  {
    return mDischargeCurrentLevel;
  }
  
  public BatteryStats.LevelStepTracker getDischargeLevelStepTracker()
  {
    return mDischargeStepTracker;
  }
  
  public int getDischargeStartLevel()
  {
    try
    {
      int i = getDischargeStartLevelLocked();
      return i;
    }
    finally {}
  }
  
  public int getDischargeStartLevelLocked()
  {
    return mDischargeUnplugLevel;
  }
  
  public String getEndPlatformVersion()
  {
    return mEndPlatformVersion;
  }
  
  public int getEstimatedBatteryCapacity()
  {
    return mEstimatedBatteryCapacity;
  }
  
  public long getExternalStatsCollectionRateLimitMs()
  {
    try
    {
      long l = mConstants.EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS;
      return l;
    }
    finally {}
  }
  
  public int getFlashLight()
  {
    return mFlashLightEasyTimer.getStatus();
  }
  
  public long getFlashlightOnCount(int paramInt)
  {
    return mFlashlightOnTimer.getCountLocked(paramInt);
  }
  
  public long getFlashlightOnTime(long paramLong, int paramInt)
  {
    return mFlashlightOnTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public long getGlobalWifiRunningTime(long paramLong, int paramInt)
  {
    return mGlobalWifiRunningTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public long getGpsBatteryDrainMaMs()
  {
    if (mPowerProfile.getAveragePower("gps.voltage") / 1000.0D == 0.0D) {
      return 0L;
    }
    long l = SystemClock.elapsedRealtime();
    double d = 0.0D;
    for (int i = 0; i < 2; i++) {
      d += mPowerProfile.getAveragePower("gps.signalqualitybased", i) * (getGpsSignalQualityTime(i, l * 1000L, 0) / 1000L);
    }
    return d;
  }
  
  public GpsBatteryStats getGpsBatteryStats()
  {
    GpsBatteryStats localGpsBatteryStats = new GpsBatteryStats();
    long l = SystemClock.elapsedRealtime() * 1000L;
    localGpsBatteryStats.setLoggingDurationMs(computeBatteryRealtime(l, 0) / 1000L);
    localGpsBatteryStats.setEnergyConsumedMaMs(getGpsBatteryDrainMaMs());
    long[] arrayOfLong = new long[2];
    for (int i = 0; i < arrayOfLong.length; i++) {
      arrayOfLong[i] = (getGpsSignalQualityTime(i, l, 0) / 1000L);
    }
    localGpsBatteryStats.setTimeInGpsSignalQualityLevel(arrayOfLong);
    return localGpsBatteryStats;
  }
  
  public int getGpsOn()
  {
    return mGpsOnEasyTimer.getStatus();
  }
  
  public long getGpsSignalQualityTime(int paramInt1, long paramLong, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < 2)) {
      return mGpsSignalQualityTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
    }
    return 0L;
  }
  
  public int getHighDischargeAmountSinceCharge()
  {
    try
    {
      int i = mHighDischargeAmountSinceCharge;
      int j = i;
      if (mOnBattery)
      {
        j = i;
        if (mDischargeCurrentLevel < mDischargeUnplugLevel) {
          j = i + (mDischargeUnplugLevel - mDischargeCurrentLevel);
        }
      }
      return j;
    }
    finally {}
  }
  
  public long getHistoryBaseTime()
  {
    return mHistoryBaseTime;
  }
  
  public int getHistoryStringPoolBytes()
  {
    return mReadHistoryStrings.length * 12 + mReadHistoryChars * 2;
  }
  
  public int getHistoryStringPoolSize()
  {
    return mReadHistoryStrings.length;
  }
  
  public String getHistoryTagPoolString(int paramInt)
  {
    return mReadHistoryStrings[paramInt];
  }
  
  public int getHistoryTagPoolUid(int paramInt)
  {
    return mReadHistoryUids[paramInt];
  }
  
  public int getHistoryTotalSize()
  {
    return MAX_HISTORY_BUFFER;
  }
  
  public int getHistoryUsedSize()
  {
    return mHistoryBuffer.dataSize();
  }
  
  public long getInteractiveTime(long paramLong, int paramInt)
  {
    return mInteractiveTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public boolean getIsOnBattery()
  {
    return mOnBattery;
  }
  
  public LongSparseArray<SamplingTimer> getKernelMemoryStats()
  {
    return mKernelMemoryStats;
  }
  
  public SamplingTimer getKernelMemoryTimerLocked(long paramLong)
  {
    SamplingTimer localSamplingTimer1 = (SamplingTimer)mKernelMemoryStats.get(paramLong);
    SamplingTimer localSamplingTimer2 = localSamplingTimer1;
    if (localSamplingTimer1 == null)
    {
      localSamplingTimer2 = new SamplingTimer(mClocks, mOnBatteryTimeBase);
      mKernelMemoryStats.put(paramLong, localSamplingTimer2);
    }
    return localSamplingTimer2;
  }
  
  public Map<String, ? extends Timer> getKernelWakelockStats()
  {
    return mKernelWakelockStats;
  }
  
  public SamplingTimer getKernelWakelockTimerLocked(String paramString)
  {
    SamplingTimer localSamplingTimer1 = (SamplingTimer)mKernelWakelockStats.get(paramString);
    SamplingTimer localSamplingTimer2 = localSamplingTimer1;
    if (localSamplingTimer1 == null)
    {
      localSamplingTimer2 = new SamplingTimer(mClocks, mOnBatteryScreenOffTimeBase);
      mKernelWakelockStats.put(paramString, localSamplingTimer2);
    }
    return localSamplingTimer2;
  }
  
  public long getLastAudioOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mAudioOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastBleScanning()
  {
    long l = SystemClock.elapsedRealtime();
    return mBleScanningEasyTimer.getExecutionTime(l);
  }
  
  public long getLastCameraOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mCameraOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastChargingOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mChargingOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastFlashLight()
  {
    long l = SystemClock.elapsedRealtime();
    return mFlashLightEasyTimer.getExecutionTime(l);
  }
  
  public long getLastGpsOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mGpsOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastLowPowerMode()
  {
    long l = SystemClock.elapsedRealtime();
    return mLowPowerModeEasyTimer.getExecutionTime(l);
  }
  
  public long getLastMobileRadioOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mMobileRadioOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastPhoneInCall()
  {
    long l = SystemClock.elapsedRealtime();
    return mPhoneInCallEasyTimer.getExecutionTime(l);
  }
  
  public long getLastPhoneScanning()
  {
    long l = SystemClock.elapsedRealtime();
    return mPhoneScanningEasyTimer.getExecutionTime(l);
  }
  
  public long getLastPlugged()
  {
    long l = SystemClock.elapsedRealtime();
    return mPluggedEasyTimer.getExecutionTime(l);
  }
  
  public long getLastSensorOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mSensorOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastVideoOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mVideoOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastWifiFullLock()
  {
    long l = SystemClock.elapsedRealtime();
    return mWifiFullLockEasyTimer.getExecutionTime(l);
  }
  
  public long getLastWifiHotspotOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mWifiHotspotOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastWifiMultiCastOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mWifiMultiCastOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastWifiOn()
  {
    long l = SystemClock.elapsedRealtime();
    return mWifiOnEasyTimer.getExecutionTime(l);
  }
  
  public long getLastWifiRunning()
  {
    long l = SystemClock.elapsedRealtime();
    return mWifiRunningEasyTimer.getExecutionTime(l);
  }
  
  public long getLastWifiScan()
  {
    long l = SystemClock.elapsedRealtime();
    return mWifiScanEasyTimer.getExecutionTime(l);
  }
  
  public long getLongestDeviceIdleModeTime(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0L;
    case 2: 
      return mLongestFullIdleTime;
    }
    return mLongestLightIdleTime;
  }
  
  public int getLowDischargeAmountSinceCharge()
  {
    try
    {
      int i = mLowDischargeAmountSinceCharge;
      int j = i;
      if (mOnBattery)
      {
        j = i;
        if (mDischargeCurrentLevel < mDischargeUnplugLevel) {
          j = i + (mDischargeUnplugLevel - mDischargeCurrentLevel - 1);
        }
      }
      return j;
    }
    finally {}
  }
  
  public int getLowPowerMode()
  {
    return mLowPowerModeEasyTimer.getStatus();
  }
  
  public int getMaxLearnedBatteryCapacity()
  {
    return mMaxLearnedBatteryCapacity;
  }
  
  public int getMinLearnedBatteryCapacity()
  {
    return mMinLearnedBatteryCapacity;
  }
  
  public String[] getMobileIfaces()
  {
    synchronized (mModemNetworkLock)
    {
      String[] arrayOfString = mModemIfaces;
      return arrayOfString;
    }
  }
  
  public long getMobileRadioActiveAdjustedTime(int paramInt)
  {
    return mMobileRadioActiveAdjustedTime.getCountLocked(paramInt);
  }
  
  public int getMobileRadioActiveCount(int paramInt)
  {
    return mMobileRadioActiveTimer.getCountLocked(paramInt);
  }
  
  public long getMobileRadioActiveTime(long paramLong, int paramInt)
  {
    return mMobileRadioActiveTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public int getMobileRadioActiveUnknownCount(int paramInt)
  {
    return (int)mMobileRadioActiveUnknownCount.getCountLocked(paramInt);
  }
  
  public long getMobileRadioActiveUnknownTime(int paramInt)
  {
    return mMobileRadioActiveUnknownTime.getCountLocked(paramInt);
  }
  
  public int getMobileRadioOn()
  {
    return mMobileRadioOnEasyTimer.getStatus();
  }
  
  public BatteryStats.ControllerActivityCounter getModemControllerActivity()
  {
    return mModemActivity;
  }
  
  public long getNetworkActivityBytes(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < mNetworkByteActivityCounters.length)) {
      return mNetworkByteActivityCounters[paramInt1].getCountLocked(paramInt2);
    }
    return 0L;
  }
  
  public long getNetworkActivityPackets(int paramInt1, int paramInt2)
  {
    if ((paramInt1 >= 0) && (paramInt1 < mNetworkPacketActivityCounters.length)) {
      return mNetworkPacketActivityCounters[paramInt1].getCountLocked(paramInt2);
    }
    return 0L;
  }
  
  public boolean getNextHistoryLocked(BatteryStats.HistoryItem paramHistoryItem)
  {
    int i = mHistoryBuffer.dataPosition();
    if (i == 0) {
      paramHistoryItem.clear();
    }
    if (i >= mHistoryBuffer.dataSize()) {
      i = 1;
    } else {
      i = 0;
    }
    if (i != 0) {
      return false;
    }
    long l1 = time;
    long l2 = currentTime;
    readHistoryDelta(mHistoryBuffer, paramHistoryItem);
    if ((cmd != 5) && (cmd != 7) && (l2 != 0L)) {
      currentTime = (time - l1 + l2);
    }
    return true;
  }
  
  public long getNextMaxDailyDeadline()
  {
    return mNextMaxDailyDeadline;
  }
  
  public long getNextMinDailyDeadline()
  {
    return mNextMinDailyDeadline;
  }
  
  public boolean getNextOldHistoryLocked(BatteryStats.HistoryItem paramHistoryItem)
  {
    int i;
    if (mHistoryBuffer.dataPosition() >= mHistoryBuffer.dataSize()) {
      i = 1;
    } else {
      i = 0;
    }
    if (i == 0)
    {
      readHistoryDelta(mHistoryBuffer, mHistoryReadTmp);
      boolean bool1 = mReadOverflow;
      boolean bool2;
      if (mHistoryReadTmp.cmd == 6) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mReadOverflow = (bool1 | bool2);
    }
    Object localObject = mHistoryIterator;
    if (localObject == null)
    {
      if ((!mReadOverflow) && (i == 0)) {
        Slog.w("BatteryStatsImpl", "Old history ends before new history!");
      }
      return false;
    }
    paramHistoryItem.setTo((BatteryStats.HistoryItem)localObject);
    mHistoryIterator = next;
    if (!mReadOverflow) {
      if (i != 0)
      {
        Slog.w("BatteryStatsImpl", "New history ends before old history!");
      }
      else if (!paramHistoryItem.same(mHistoryReadTmp))
      {
        localObject = new FastPrintWriter(new LogWriter(5, "BatteryStatsImpl"));
        ((PrintWriter)localObject).println("Histories differ!");
        ((PrintWriter)localObject).println("Old history:");
        new BatteryStats.HistoryPrinter().printNextItem((PrintWriter)localObject, paramHistoryItem, 0L, false, true);
        ((PrintWriter)localObject).println("New history:");
        new BatteryStats.HistoryPrinter().printNextItem((PrintWriter)localObject, mHistoryReadTmp, 0L, false, true);
        ((PrintWriter)localObject).flush();
      }
    }
    return true;
  }
  
  public int getNumConnectivityChange(int paramInt)
  {
    int i = mNumConnectivityChange;
    int j;
    if (paramInt == 1)
    {
      j = i - mLoadedNumConnectivityChange;
    }
    else
    {
      j = i;
      if (paramInt == 2) {
        j = i - mUnpluggedNumConnectivityChange;
      }
    }
    return j;
  }
  
  public BatteryStatsImpl.Uid.Pkg getPackageStatsLocked(int paramInt, String paramString)
  {
    return getUidStatsLocked(mapUid(paramInt)).getPackageStatsLocked(paramString);
  }
  
  public int getParcelVersion()
  {
    return 177;
  }
  
  public int getPhoneDataConnectionCount(int paramInt1, int paramInt2)
  {
    return mPhoneDataConnectionsTimer[paramInt1].getCountLocked(paramInt2);
  }
  
  public long getPhoneDataConnectionTime(int paramInt1, long paramLong, int paramInt2)
  {
    return mPhoneDataConnectionsTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
  }
  
  public Timer getPhoneDataConnectionTimer(int paramInt)
  {
    return mPhoneDataConnectionsTimer[paramInt];
  }
  
  public int getPhoneInCall()
  {
    return mPhoneInCallEasyTimer.getStatus();
  }
  
  public int getPhoneOnCount(int paramInt)
  {
    return mPhoneOnTimer.getCountLocked(paramInt);
  }
  
  public long getPhoneOnTime(long paramLong, int paramInt)
  {
    return mPhoneOnTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public int getPhoneScanning()
  {
    return mPhoneScanningEasyTimer.getStatus();
  }
  
  public long getPhoneSignalScanningTime(long paramLong, int paramInt)
  {
    return mPhoneSignalScanningTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public Timer getPhoneSignalScanningTimer()
  {
    return mPhoneSignalScanningTimer;
  }
  
  public int getPhoneSignalStrengthCount(int paramInt1, int paramInt2)
  {
    return mPhoneSignalStrengthsTimer[paramInt1].getCountLocked(paramInt2);
  }
  
  public long getPhoneSignalStrengthTime(int paramInt1, long paramLong, int paramInt2)
  {
    return mPhoneSignalStrengthsTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
  }
  
  public Timer getPhoneSignalStrengthTimer(int paramInt)
  {
    return mPhoneSignalStrengthsTimer[paramInt];
  }
  
  public int getPowerSaveModeEnabledCount(int paramInt)
  {
    return mPowerSaveModeEnabledTimer.getCountLocked(paramInt);
  }
  
  public long getPowerSaveModeEnabledTime(long paramLong, int paramInt)
  {
    return mPowerSaveModeEnabledTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public BatteryStatsImpl.Uid.Proc getProcessStatsLocked(int paramInt, String paramString)
  {
    return getUidStatsLocked(mapUid(paramInt)).getProcessStatsLocked(paramString);
  }
  
  public long getProcessWakeTime(int paramInt1, int paramInt2, long paramLong)
  {
    paramInt1 = mapUid(paramInt1);
    Object localObject = (Uid)mUidStats.get(paramInt1);
    long l1 = 0L;
    if (localObject != null)
    {
      localObject = (BatteryStats.Uid.Pid)mPids.get(paramInt2);
      if (localObject != null)
      {
        long l2 = mWakeSumMs;
        if (mWakeNesting > 0) {
          l1 = paramLong - mWakeStartMs;
        }
        return l2 + l1;
      }
    }
    return 0L;
  }
  
  public Map<String, ? extends Timer> getRpmStats()
  {
    return mRpmStats;
  }
  
  public SamplingTimer getRpmTimerLocked(String paramString)
  {
    SamplingTimer localSamplingTimer1 = (SamplingTimer)mRpmStats.get(paramString);
    SamplingTimer localSamplingTimer2 = localSamplingTimer1;
    if (localSamplingTimer1 == null)
    {
      localSamplingTimer2 = new SamplingTimer(mClocks, mOnBatteryTimeBase);
      mRpmStats.put(paramString, localSamplingTimer2);
    }
    return localSamplingTimer2;
  }
  
  public long getScreenBrightnessTime(int paramInt1, long paramLong, int paramInt2)
  {
    return mScreenBrightnessTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
  }
  
  public Timer getScreenBrightnessTimer(int paramInt)
  {
    return mScreenBrightnessTimer[paramInt];
  }
  
  public int getScreenDozeCount(int paramInt)
  {
    return mScreenDozeTimer.getCountLocked(paramInt);
  }
  
  public long getScreenDozeTime(long paramLong, int paramInt)
  {
    return mScreenDozeTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public Map<String, ? extends Timer> getScreenOffRpmStats()
  {
    return mScreenOffRpmStats;
  }
  
  public SamplingTimer getScreenOffRpmTimerLocked(String paramString)
  {
    SamplingTimer localSamplingTimer1 = (SamplingTimer)mScreenOffRpmStats.get(paramString);
    SamplingTimer localSamplingTimer2 = localSamplingTimer1;
    if (localSamplingTimer1 == null)
    {
      localSamplingTimer2 = new SamplingTimer(mClocks, mOnBatteryScreenOffTimeBase);
      mScreenOffRpmStats.put(paramString, localSamplingTimer2);
    }
    return localSamplingTimer2;
  }
  
  public int getScreenOnCount(int paramInt)
  {
    return mScreenOnTimer.getCountLocked(paramInt);
  }
  
  public long getScreenOnDur()
  {
    long l = SystemClock.elapsedRealtime();
    return mScreenOnDurEasyTimer.getExecutionTime(l);
  }
  
  public long getScreenOnTime(long paramLong, int paramInt)
  {
    return mScreenOnTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public int getSensorOn()
  {
    return mSensorOnEasyTimer.getStatus();
  }
  
  public BatteryStatsImpl.Uid.Pkg.Serv getServiceStatsLocked(int paramInt, String paramString1, String paramString2)
  {
    return getUidStatsLocked(mapUid(paramInt)).getServiceStatsLocked(paramString1, paramString2);
  }
  
  public long getStartClockTime()
  {
    long l = System.currentTimeMillis();
    if (ensureStartClockTime(l)) {
      recordCurrentTimeChangeLocked(l, mClocks.elapsedRealtime(), mClocks.uptimeMillis());
    }
    return mStartClockTime;
  }
  
  public int getStartCount()
  {
    return mStartCount;
  }
  
  public String getStartPlatformVersion()
  {
    return mStartPlatformVersion;
  }
  
  public long getUahDischarge(int paramInt)
  {
    return mDischargeCounter.getCountLocked(paramInt);
  }
  
  public long getUahDischargeDeepDoze(int paramInt)
  {
    return mDischargeDeepDozeCounter.getCountLocked(paramInt);
  }
  
  public long getUahDischargeLightDoze(int paramInt)
  {
    return mDischargeLightDozeCounter.getCountLocked(paramInt);
  }
  
  public long getUahDischargeScreenDoze(int paramInt)
  {
    return mDischargeScreenDozeCounter.getCountLocked(paramInt);
  }
  
  public long getUahDischargeScreenOff(int paramInt)
  {
    return mDischargeScreenOffCounter.getCountLocked(paramInt);
  }
  
  public SparseArray<? extends BatteryStats.Uid> getUidStats()
  {
    return mUidStats;
  }
  
  public Uid getUidStatsLocked(int paramInt)
  {
    Uid localUid1 = (Uid)mUidStats.get(paramInt);
    Uid localUid2 = localUid1;
    if (localUid1 == null)
    {
      localUid2 = new Uid(this, paramInt);
      mUidStats.put(paramInt, localUid2);
    }
    return localUid2;
  }
  
  public int getVideoOn()
  {
    return mVideoOnEasyTimer.getStatus();
  }
  
  public Map<String, ? extends Timer> getWakeupReasonStats()
  {
    return mWakeupReasonStats;
  }
  
  public SamplingTimer getWakeupReasonTimerLocked(String paramString)
  {
    SamplingTimer localSamplingTimer1 = (SamplingTimer)mWakeupReasonStats.get(paramString);
    SamplingTimer localSamplingTimer2 = localSamplingTimer1;
    if (localSamplingTimer1 == null)
    {
      localSamplingTimer2 = new SamplingTimer(mClocks, mOnBatteryTimeBase);
      mWakeupReasonStats.put(paramString, localSamplingTimer2);
    }
    return localSamplingTimer2;
  }
  
  public long getWifiActiveTime(long paramLong, int paramInt)
  {
    return mWifiActiveTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public WifiBatteryStats getWifiBatteryStats()
  {
    WifiBatteryStats localWifiBatteryStats = new WifiBatteryStats();
    int i = 0;
    long l1 = SystemClock.elapsedRealtime() * 1000L;
    Object localObject = getWifiControllerActivity();
    long l2 = ((BatteryStats.ControllerActivityCounter)localObject).getIdleTimeCounter().getCountLocked(0);
    long l3 = ((BatteryStats.ControllerActivityCounter)localObject).getScanTimeCounter().getCountLocked(0);
    long l4 = ((BatteryStats.ControllerActivityCounter)localObject).getRxTimeCounter().getCountLocked(0);
    long l5 = localObject.getTxTimeCounters()[0].getCountLocked(0);
    long l6 = computeBatteryRealtime(SystemClock.elapsedRealtime() * 1000L, 0) / 1000L;
    long l7 = ((BatteryStats.ControllerActivityCounter)localObject).getPowerCounter().getCountLocked(0);
    long l8 = 0L;
    for (int j = 0; j < mUidStats.size(); j++) {
      l8 += mUidStats.valueAt(j)).mWifiScanTimer.getCountLocked(0);
    }
    long[] arrayOfLong1 = new long[8];
    for (j = 0; j < 8; j++) {
      arrayOfLong1[j] = (getWifiStateTime(j, l1, 0) / 1000L);
    }
    localObject = new long[13];
    for (j = 0; j < 13; j++) {
      localObject[j] = (getWifiSupplStateTime(j, l1, 0) / 1000L);
    }
    long[] arrayOfLong2 = new long[5];
    for (j = 0; j < 5; j++) {
      arrayOfLong2[j] = (getWifiSignalStrengthTime(j, l1, 0) / 1000L);
    }
    localWifiBatteryStats.setLoggingDurationMs(computeBatteryRealtime(l1, 0) / 1000L);
    localWifiBatteryStats.setKernelActiveTimeMs(getWifiActiveTime(l1, 0) / 1000L);
    localWifiBatteryStats.setNumPacketsTx(getNetworkActivityPackets(3, 0));
    localWifiBatteryStats.setNumBytesTx(getNetworkActivityBytes(3, 0));
    localWifiBatteryStats.setNumPacketsRx(getNetworkActivityPackets(2, 0));
    localWifiBatteryStats.setNumBytesRx(getNetworkActivityBytes(2, 0));
    localWifiBatteryStats.setSleepTimeMs(l6 - (l2 + l4 + l5));
    localWifiBatteryStats.setIdleTimeMs(l2);
    localWifiBatteryStats.setRxTimeMs(l4);
    localWifiBatteryStats.setTxTimeMs(l5);
    localWifiBatteryStats.setScanTimeMs(l3);
    localWifiBatteryStats.setEnergyConsumedMaMs(l7);
    localWifiBatteryStats.setNumAppScanRequest(l8);
    localWifiBatteryStats.setTimeInStateMs(arrayOfLong1);
    localWifiBatteryStats.setTimeInSupplicantStateMs((long[])localObject);
    localWifiBatteryStats.setTimeInRxSignalStrengthLevelMs(arrayOfLong2);
    return localWifiBatteryStats;
  }
  
  public BatteryStats.ControllerActivityCounter getWifiControllerActivity()
  {
    return mWifiActivity;
  }
  
  public int getWifiFullLock()
  {
    return mWifiFullLockEasyTimer.getStatus();
  }
  
  public int getWifiHotspotOn()
  {
    return mWifiHotspotOnEasyTimer.getStatus();
  }
  
  public String[] getWifiIfaces()
  {
    synchronized (mWifiNetworkLock)
    {
      String[] arrayOfString = mWifiIfaces;
      return arrayOfString;
    }
  }
  
  public int getWifiMultiCastOn()
  {
    return mWifiMultiCastOnEasyTimer.getStatus();
  }
  
  public int getWifiMulticastWakelockCount(int paramInt)
  {
    return mWifiMulticastWakelockTimer.getCountLocked(paramInt);
  }
  
  public long getWifiMulticastWakelockTime(long paramLong, int paramInt)
  {
    return mWifiMulticastWakelockTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public int getWifiOn()
  {
    if (mWifiOnEasyTimer.isKnownStatus())
    {
      int i;
      if (mWifiOn) {
        i = 1;
      } else {
        i = -1;
      }
      return i;
    }
    return 0;
  }
  
  public long getWifiOnTime(long paramLong, int paramInt)
  {
    return mWifiOnTimer.getTotalTimeLocked(paramLong, paramInt);
  }
  
  public int getWifiRunning()
  {
    if (mWifiRunningEasyTimer.isKnownStatus())
    {
      int i;
      if (mGlobalWifiRunning) {
        i = 1;
      } else {
        i = -1;
      }
      return i;
    }
    return 0;
  }
  
  public int getWifiScan()
  {
    return mWifiScanEasyTimer.getStatus();
  }
  
  public int getWifiSignalStrengthCount(int paramInt1, int paramInt2)
  {
    return mWifiSignalStrengthsTimer[paramInt1].getCountLocked(paramInt2);
  }
  
  public long getWifiSignalStrengthTime(int paramInt1, long paramLong, int paramInt2)
  {
    return mWifiSignalStrengthsTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
  }
  
  public Timer getWifiSignalStrengthTimer(int paramInt)
  {
    return mWifiSignalStrengthsTimer[paramInt];
  }
  
  public int getWifiStateCount(int paramInt1, int paramInt2)
  {
    return mWifiStateTimer[paramInt1].getCountLocked(paramInt2);
  }
  
  public long getWifiStateTime(int paramInt1, long paramLong, int paramInt2)
  {
    return mWifiStateTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
  }
  
  public Timer getWifiStateTimer(int paramInt)
  {
    return mWifiStateTimer[paramInt];
  }
  
  public int getWifiSupplStateCount(int paramInt1, int paramInt2)
  {
    return mWifiSupplStateTimer[paramInt1].getCountLocked(paramInt2);
  }
  
  public long getWifiSupplStateTime(int paramInt1, long paramLong, int paramInt2)
  {
    return mWifiSupplStateTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
  }
  
  public Timer getWifiSupplStateTimer(int paramInt)
  {
    return mWifiSupplStateTimer[paramInt];
  }
  
  public boolean hasBluetoothActivityReporting()
  {
    return mHasBluetoothReporting;
  }
  
  public boolean hasModemActivityReporting()
  {
    return mHasModemReporting;
  }
  
  public boolean hasWifiActivityReporting()
  {
    return mHasWifiReporting;
  }
  
  void initDischarge()
  {
    mLowDischargeAmountSinceCharge = 0;
    mHighDischargeAmountSinceCharge = 0;
    mDischargeAmountScreenOn = 0;
    mDischargeAmountScreenOnSinceCharge = 0;
    mDischargeAmountScreenOff = 0;
    mDischargeAmountScreenOffSinceCharge = 0;
    mDischargeAmountScreenDoze = 0;
    mDischargeAmountScreenDozeSinceCharge = 0;
    mDischargeStepTracker.init();
    mChargeStepTracker.init();
    mDischargeScreenOffCounter.reset(false);
    mDischargeScreenDozeCounter.reset(false);
    mDischargeLightDozeCounter.reset(false);
    mDischargeDeepDozeCounter.reset(false);
    mDischargeCounter.reset(false);
  }
  
  void initTimes(long paramLong1, long paramLong2)
  {
    mStartClockTime = System.currentTimeMillis();
    mOnBatteryTimeBase.init(paramLong1, paramLong2);
    mOnBatteryScreenOffTimeBase.init(paramLong1, paramLong2);
    mRealtime = 0L;
    mUptime = 0L;
    mRealtimeStart = paramLong2;
    mUptimeStart = paramLong1;
  }
  
  public boolean isCharging()
  {
    return mCharging;
  }
  
  public boolean isOnBattery()
  {
    return mOnBattery;
  }
  
  public boolean isOnBatteryLocked()
  {
    return mOnBatteryTimeBase.isRunning();
  }
  
  public boolean isOnBatteryScreenOffLocked()
  {
    return mOnBatteryScreenOffTimeBase.isRunning();
  }
  
  public boolean isScreenDoze(int paramInt)
  {
    boolean bool;
    if ((paramInt != 3) && (paramInt != 4)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isScreenOff(int paramInt)
  {
    boolean bool = true;
    if (paramInt != 1) {
      bool = false;
    }
    return bool;
  }
  
  public boolean isScreenOn(int paramInt)
  {
    boolean bool;
    if ((paramInt != 2) && (paramInt != 5) && (paramInt != 6)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int mapUid(int paramInt)
  {
    int i = mIsolatedUids.get(paramInt, -1);
    if (i > 0) {
      paramInt = i;
    }
    return paramInt;
  }
  
  @VisibleForTesting
  public void markPartialTimersAsEligible()
  {
    if (ArrayUtils.referenceEquals(mPartialTimers, mLastPartialTimers)) {
      for (i = mPartialTimers.size() - 1; i >= 0; i--) {
        mPartialTimers.get(i)).mInList = true;
      }
    }
    int j;
    for (int i = mLastPartialTimers.size() - 1;; i--)
    {
      j = 0;
      if (i < 0) {
        break;
      }
      mLastPartialTimers.get(i)).mInList = false;
    }
    mLastPartialTimers.clear();
    int k = mPartialTimers.size();
    for (i = j; i < k; i++)
    {
      StopwatchTimer localStopwatchTimer = (StopwatchTimer)mPartialTimers.get(i);
      mInList = true;
      mLastPartialTimers.add(localStopwatchTimer);
    }
  }
  
  public void noteActivityPausedLocked(int paramInt)
  {
    getUidStatsLocked(mapUid(paramInt)).noteActivityPausedLocked(mClocks.elapsedRealtime());
  }
  
  public void noteActivityResumedLocked(int paramInt)
  {
    getUidStatsLocked(mapUid(paramInt)).noteActivityResumedLocked(mClocks.elapsedRealtime());
  }
  
  public void noteAlarmFinishLocked(String paramString, WorkSource paramWorkSource, int paramInt)
  {
    noteAlarmStartOrFinishLocked(16397, paramString, paramWorkSource, paramInt);
  }
  
  public void noteAlarmStartLocked(String paramString, WorkSource paramWorkSource, int paramInt)
  {
    noteAlarmStartOrFinishLocked(32781, paramString, paramWorkSource, paramInt);
  }
  
  public void noteAudioOffLocked(int paramInt)
  {
    if (mAudioOnNesting == 0) {
      return;
    }
    int i = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    paramInt = mAudioOnNesting - 1;
    mAudioOnNesting = paramInt;
    if (paramInt == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states &= 0xFFBFFFFF;
      addHistoryRecordLocked(l1, l2);
      mAudioOnTimer.stopRunningLocked(l1);
    }
    getUidStatsLocked(i).noteAudioTurnedOffLocked(l1);
    mAudioOnEasyTimer.stopRunningLocked(l1);
  }
  
  public void noteAudioOnLocked(int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mAudioOnNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states |= 0x400000;
      addHistoryRecordLocked(l1, l2);
      mAudioOnTimer.startRunningLocked(l1);
    }
    mAudioOnNesting += 1;
    getUidStatsLocked(paramInt).noteAudioTurnedOnLocked(l1);
    mAudioOnEasyTimer.startRunningLocked(l1);
  }
  
  public void noteBluetoothScanResultsFromSourceLocked(WorkSource paramWorkSource, int paramInt)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      getUidStatsLocked(mapUid(paramWorkSource.get(k))).noteBluetoothScanResultsLocked(paramInt);
      StatsLog.write_non_chained(4, paramWorkSource.get(k), paramWorkSource.getName(k), paramInt);
    }
    ArrayList localArrayList = paramWorkSource.getWorkChains();
    if (localArrayList != null) {
      for (k = j; k < localArrayList.size(); k++)
      {
        paramWorkSource = (WorkSource.WorkChain)localArrayList.get(k);
        getUidStatsLocked(mapUid(paramWorkSource.getAttributionUid())).noteBluetoothScanResultsLocked(paramInt);
        StatsLog.write(4, paramWorkSource.getUids(), paramWorkSource.getTags(), paramInt);
      }
    }
  }
  
  public void noteBluetoothScanStartedFromSourceLocked(WorkSource paramWorkSource, boolean paramBoolean)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      noteBluetoothScanStartedLocked(null, paramWorkSource.get(k), paramBoolean);
    }
    paramWorkSource = paramWorkSource.getWorkChains();
    if (paramWorkSource != null) {
      for (k = j; k < paramWorkSource.size(); k++) {
        noteBluetoothScanStartedLocked((WorkSource.WorkChain)paramWorkSource.get(k), -1, paramBoolean);
      }
    }
  }
  
  public void noteBluetoothScanStoppedFromSourceLocked(WorkSource paramWorkSource, boolean paramBoolean)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      noteBluetoothScanStoppedLocked(null, paramWorkSource.get(k), paramBoolean);
    }
    paramWorkSource = paramWorkSource.getWorkChains();
    if (paramWorkSource != null) {
      for (k = j; k < paramWorkSource.size(); k++) {
        noteBluetoothScanStoppedLocked((WorkSource.WorkChain)paramWorkSource.get(k), -1, paramBoolean);
      }
    }
  }
  
  public void noteCameraOffLocked(int paramInt)
  {
    if (mCameraOnNesting == 0) {
      return;
    }
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    int i = mCameraOnNesting - 1;
    mCameraOnNesting = i;
    if (i == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xFFDFFFFF;
      addHistoryRecordLocked(l1, l2);
      mCameraOnTimer.stopRunningLocked(l1);
    }
    getUidStatsLocked(paramInt).noteCameraTurnedOffLocked(l1);
    mCameraOnEasyTimer.stopRunningLocked(l1);
  }
  
  public void noteCameraOnLocked(int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    int i = mCameraOnNesting;
    mCameraOnNesting = (i + 1);
    if (i == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 |= 0x200000;
      addHistoryRecordLocked(l1, l2);
      mCameraOnTimer.startRunningLocked(l1);
    }
    getUidStatsLocked(paramInt).noteCameraTurnedOnLocked(l1);
    mCameraOnEasyTimer.startRunningLocked(l1);
  }
  
  public void noteChangeWakelockFromSourceLocked(WorkSource paramWorkSource1, int paramInt1, String paramString1, String paramString2, int paramInt2, WorkSource paramWorkSource2, int paramInt3, String paramString3, String paramString4, int paramInt4, boolean paramBoolean)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    ArrayList[] arrayOfArrayList = WorkSource.diffChains(paramWorkSource1, paramWorkSource2);
    int i = paramWorkSource2.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      noteStartWakeLocked(paramWorkSource2.get(k), paramInt3, null, paramString3, paramString4, paramInt4, paramBoolean, l1, l2);
    }
    if (arrayOfArrayList != null)
    {
      paramWorkSource2 = arrayOfArrayList[0];
      if (paramWorkSource2 != null) {
        for (i = 0; i < paramWorkSource2.size(); i++)
        {
          WorkSource.WorkChain localWorkChain = (WorkSource.WorkChain)paramWorkSource2.get(i);
          noteStartWakeLocked(localWorkChain.getAttributionUid(), paramInt3, localWorkChain, paramString3, paramString4, paramInt4, paramBoolean, l1, l2);
        }
      }
    }
    paramInt4 = paramWorkSource1.size();
    for (paramInt3 = 0; paramInt3 < paramInt4; paramInt3++) {
      noteStopWakeLocked(paramWorkSource1.get(paramInt3), paramInt1, null, paramString1, paramString2, paramInt2, l1, l2);
    }
    if (arrayOfArrayList != null)
    {
      paramWorkSource1 = arrayOfArrayList[1];
      if (paramWorkSource1 != null) {
        for (paramInt3 = j; paramInt3 < paramWorkSource1.size(); paramInt3++)
        {
          paramWorkSource2 = (WorkSource.WorkChain)paramWorkSource1.get(paramInt3);
          noteStopWakeLocked(paramWorkSource2.getAttributionUid(), paramInt1, paramWorkSource2, paramString1, paramString2, paramInt2, l1, l2);
        }
      }
    }
  }
  
  public void noteConnectivityChangedLocked(int paramInt, String paramString)
  {
    addHistoryEventLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis(), 9, paramString, paramInt);
    mNumConnectivityChange += 1;
  }
  
  public void noteCurrentTimeChangedLocked()
  {
    long l = System.currentTimeMillis();
    recordCurrentTimeChangeLocked(l, mClocks.elapsedRealtime(), mClocks.uptimeMillis());
    ensureStartClockTime(l);
  }
  
  public void noteDeviceIdleModeLocked(int paramInt1, String paramString, int paramInt2)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    int i = 0;
    if (paramInt1 == 2) {
      bool1 = true;
    } else {
      bool1 = false;
    }
    boolean bool2 = bool1;
    if (mDeviceIdling)
    {
      bool2 = bool1;
      if (!bool1)
      {
        bool2 = bool1;
        if (paramString == null) {
          bool2 = true;
        }
      }
    }
    boolean bool3;
    if (paramInt1 == 1) {
      bool3 = true;
    } else {
      bool3 = false;
    }
    boolean bool1 = bool3;
    if (mDeviceLightIdling)
    {
      bool1 = bool3;
      if (!bool3)
      {
        bool1 = bool3;
        if (!bool2)
        {
          bool1 = bool3;
          if (paramString == null) {
            bool1 = true;
          }
        }
      }
    }
    if (paramString != null)
    {
      if ((!mDeviceIdling) && (!mDeviceLightIdling)) {
        break label163;
      }
      addHistoryEventLocked(l1, l2, 10, paramString, paramInt2);
    }
    label163:
    if ((mDeviceIdling != bool2) || (mDeviceLightIdling != bool1))
    {
      if (bool2) {
        paramInt2 = 2;
      }
      for (;;)
      {
        break;
        if (bool1) {
          paramInt2 = 1;
        } else {
          paramInt2 = 0;
        }
      }
      StatsLog.write(22, paramInt2);
    }
    if (mDeviceIdling != bool2)
    {
      mDeviceIdling = bool2;
      paramInt2 = i;
      if (bool2) {
        paramInt2 = 8;
      }
      i = mModStepMode;
      mModStepMode = (0x8 & mCurStepMode ^ paramInt2 | i);
      mCurStepMode = (mCurStepMode & 0xFFFFFFF7 | paramInt2);
      if (bool2) {
        mDeviceIdlingTimer.startRunningLocked(l1);
      } else {
        mDeviceIdlingTimer.stopRunningLocked(l1);
      }
    }
    if (mDeviceLightIdling != bool1)
    {
      mDeviceLightIdling = bool1;
      if (bool1) {
        mDeviceLightIdlingTimer.startRunningLocked(l1);
      } else {
        mDeviceLightIdlingTimer.stopRunningLocked(l1);
      }
    }
    if (mDeviceIdleMode != paramInt1)
    {
      mHistoryCur.states2 = (mHistoryCur.states2 & 0xF9FFFFFF | paramInt1 << 25);
      addHistoryRecordLocked(l1, l2);
      l2 = l1 - mLastIdleTimeStart;
      mLastIdleTimeStart = l1;
      if (mDeviceIdleMode == 1)
      {
        if (l2 > mLongestLightIdleTime) {
          mLongestLightIdleTime = l2;
        }
        mDeviceIdleModeLightTimer.stopRunningLocked(l1);
      }
      else if (mDeviceIdleMode == 2)
      {
        if (l2 > mLongestFullIdleTime) {
          mLongestFullIdleTime = l2;
        }
        mDeviceIdleModeFullTimer.stopRunningLocked(l1);
      }
      if (paramInt1 == 1) {
        mDeviceIdleModeLightTimer.startRunningLocked(l1);
      } else if (paramInt1 == 2) {
        mDeviceIdleModeFullTimer.startRunningLocked(l1);
      }
      mDeviceIdleMode = paramInt1;
      StatsLog.write(21, paramInt1);
    }
  }
  
  public void noteEventLocked(int paramInt1, String paramString, int paramInt2)
  {
    paramInt2 = mapUid(paramInt2);
    if (!mActiveEvents.updateState(paramInt1, paramString, paramInt2, 0)) {
      return;
    }
    addHistoryEventLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis(), paramInt1, paramString, paramInt2);
  }
  
  public void noteFlashlightOffLocked(int paramInt)
  {
    if (mFlashlightOnNesting == 0) {
      return;
    }
    int i = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    paramInt = mFlashlightOnNesting - 1;
    mFlashlightOnNesting = paramInt;
    if (paramInt == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xF7FFFFFF;
      addHistoryRecordLocked(l1, l2);
      mFlashlightOnTimer.stopRunningLocked(l1);
    }
    getUidStatsLocked(i).noteFlashlightTurnedOffLocked(l1);
    mFlashLightEasyTimer.stopRunningLocked(l1);
  }
  
  public void noteFlashlightOnLocked(int paramInt)
  {
    int i = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    paramInt = mFlashlightOnNesting;
    mFlashlightOnNesting = (paramInt + 1);
    if (paramInt == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 |= 0x8000000;
      addHistoryRecordLocked(l1, l2);
      mFlashlightOnTimer.startRunningLocked(l1);
    }
    getUidStatsLocked(i).noteFlashlightTurnedOnLocked(l1);
    mFlashLightEasyTimer.startRunningLocked(l1);
  }
  
  public void noteFullWifiLockAcquiredFromSourceLocked(WorkSource paramWorkSource)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      noteFullWifiLockAcquiredLocked(mapUid(paramWorkSource.get(k)));
      StatsLog.write_non_chained(37, paramWorkSource.get(k), paramWorkSource.getName(k), 1);
    }
    ArrayList localArrayList = paramWorkSource.getWorkChains();
    if (localArrayList != null) {
      for (k = j; k < localArrayList.size(); k++)
      {
        paramWorkSource = (WorkSource.WorkChain)localArrayList.get(k);
        noteFullWifiLockAcquiredLocked(mapUid(paramWorkSource.getAttributionUid()));
        StatsLog.write(37, paramWorkSource.getUids(), paramWorkSource.getTags(), 1);
      }
    }
  }
  
  public void noteFullWifiLockAcquiredLocked(int paramInt)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mWifiFullLockNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states |= 0x10000000;
      addHistoryRecordLocked(l1, l2);
    }
    mWifiFullLockNesting += 1;
    getUidStatsLocked(paramInt).noteFullWifiLockAcquiredLocked(l1);
    mWifiFullLockEasyTimer.startRunningLocked(l1);
  }
  
  public void noteFullWifiLockReleasedFromSourceLocked(WorkSource paramWorkSource)
  {
    int i = paramWorkSource.size();
    for (int j = 0; j < i; j++)
    {
      noteFullWifiLockReleasedLocked(mapUid(paramWorkSource.get(j)));
      StatsLog.write_non_chained(37, paramWorkSource.get(j), paramWorkSource.getName(j), 0);
    }
    paramWorkSource = paramWorkSource.getWorkChains();
    if (paramWorkSource != null) {
      for (j = 0; j < paramWorkSource.size(); j++)
      {
        WorkSource.WorkChain localWorkChain = (WorkSource.WorkChain)paramWorkSource.get(j);
        noteFullWifiLockReleasedLocked(mapUid(localWorkChain.getAttributionUid()));
        StatsLog.write(37, localWorkChain.getUids(), localWorkChain.getTags(), 0);
      }
    }
  }
  
  public void noteFullWifiLockReleasedLocked(int paramInt)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    mWifiFullLockNesting -= 1;
    if (mWifiFullLockNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states &= 0xEFFFFFFF;
      addHistoryRecordLocked(l1, l2);
    }
    getUidStatsLocked(paramInt).noteFullWifiLockReleasedLocked(l1);
    mWifiFullLockEasyTimer.stopRunningLocked(l1);
  }
  
  public void noteGpsChangedLocked(WorkSource paramWorkSource1, WorkSource paramWorkSource2)
  {
    int i = 0;
    for (int j = 0; j < paramWorkSource2.size(); j++) {
      noteStartGpsLocked(paramWorkSource2.get(j), null);
    }
    for (j = 0; j < paramWorkSource1.size(); j++) {
      noteStopGpsLocked(paramWorkSource1.get(j), null);
    }
    paramWorkSource1 = WorkSource.diffChains(paramWorkSource1, paramWorkSource2);
    if (paramWorkSource1 != null)
    {
      if (paramWorkSource1[0] != null)
      {
        paramWorkSource2 = paramWorkSource1[0];
        for (j = 0; j < paramWorkSource2.size(); j++) {
          noteStartGpsLocked(-1, (WorkSource.WorkChain)paramWorkSource2.get(j));
        }
      }
      if (paramWorkSource1[1] != null)
      {
        paramWorkSource1 = paramWorkSource1[1];
        for (j = i; j < paramWorkSource1.size(); j++) {
          noteStopGpsLocked(-1, (WorkSource.WorkChain)paramWorkSource1.get(j));
        }
      }
    }
  }
  
  public void noteGpsSignalQualityLocked(int paramInt)
  {
    if (mGpsNesting == 0) {
      return;
    }
    if ((paramInt >= 0) && (paramInt < 2))
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      if (mGpsSignalQualityBin != paramInt)
      {
        if (mGpsSignalQualityBin >= 0) {
          mGpsSignalQualityTimer[mGpsSignalQualityBin].stopRunningLocked(l1);
        }
        if (!mGpsSignalQualityTimer[paramInt].isRunningLocked()) {
          mGpsSignalQualityTimer[paramInt].startRunningLocked(l1);
        }
        mHistoryCur.states2 = (mHistoryCur.states2 & 0xFF7F | paramInt << 7);
        addHistoryRecordLocked(l1, l2);
        mGpsSignalQualityBin = paramInt;
      }
      return;
    }
    stopAllGpsSignalQualityTimersLocked(-1);
  }
  
  public void noteInteractiveLocked(boolean paramBoolean)
  {
    if (mInteractive != paramBoolean)
    {
      long l = mClocks.elapsedRealtime();
      mInteractive = paramBoolean;
      if (paramBoolean) {
        mInteractiveTimer.startRunningLocked(l);
      } else {
        mInteractiveTimer.stopRunningLocked(l);
      }
    }
  }
  
  public void noteJobFinishLocked(String paramString, int paramInt1, int paramInt2)
  {
    paramInt1 = mapUid(paramInt1);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    getUidStatsLocked(paramInt1).noteStopJobLocked(paramString, l1, paramInt2);
    if (!mActiveEvents.updateState(16390, paramString, paramInt1, 0)) {
      return;
    }
    addHistoryEventLocked(l1, l2, 16390, paramString, paramInt1);
  }
  
  public void noteJobStartLocked(String paramString, int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    getUidStatsLocked(paramInt).noteStartJobLocked(paramString, l1);
    if (!mActiveEvents.updateState(32774, paramString, paramInt, 0)) {
      return;
    }
    addHistoryEventLocked(l1, l2, 32774, paramString, paramInt);
  }
  
  public void noteJobsDeferredLocked(int paramInt1, int paramInt2, long paramLong)
  {
    getUidStatsLocked(mapUid(paramInt1)).noteJobsDeferredLocked(paramInt2, paramLong);
  }
  
  public void noteLongPartialWakelockFinish(String paramString1, String paramString2, int paramInt)
  {
    StatsLog.write_non_chained(11, paramInt, null, paramString1, paramString2, 0);
    noteLongPartialWakeLockFinishInternal(paramString1, paramString2, mapUid(paramInt));
  }
  
  public void noteLongPartialWakelockFinishFromSource(String paramString1, String paramString2, WorkSource paramWorkSource)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      noteLongPartialWakeLockFinishInternal(paramString1, paramString2, mapUid(paramWorkSource.get(k)));
      StatsLog.write_non_chained(11, paramWorkSource.get(k), paramWorkSource.getName(k), paramString1, paramString2, 0);
    }
    ArrayList localArrayList = paramWorkSource.getWorkChains();
    if (localArrayList != null) {
      for (k = j; k < localArrayList.size(); k++)
      {
        paramWorkSource = (WorkSource.WorkChain)localArrayList.get(k);
        noteLongPartialWakeLockFinishInternal(paramString1, paramString2, paramWorkSource.getAttributionUid());
        StatsLog.write(11, paramWorkSource.getUids(), paramWorkSource.getTags(), paramString1, paramString2, 0);
      }
    }
  }
  
  public void noteLongPartialWakelockStart(String paramString1, String paramString2, int paramInt)
  {
    StatsLog.write_non_chained(11, paramInt, null, paramString1, paramString2, 1);
    noteLongPartialWakeLockStartInternal(paramString1, paramString2, mapUid(paramInt));
  }
  
  public void noteLongPartialWakelockStartFromSource(String paramString1, String paramString2, WorkSource paramWorkSource)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      noteLongPartialWakeLockStartInternal(paramString1, paramString2, mapUid(paramWorkSource.get(k)));
      StatsLog.write_non_chained(11, paramWorkSource.get(k), paramWorkSource.getName(k), paramString1, paramString2, 1);
    }
    ArrayList localArrayList = paramWorkSource.getWorkChains();
    if (localArrayList != null) {
      for (k = j; k < localArrayList.size(); k++)
      {
        paramWorkSource = (WorkSource.WorkChain)localArrayList.get(k);
        noteLongPartialWakeLockStartInternal(paramString1, paramString2, paramWorkSource.getAttributionUid());
        StatsLog.write(11, paramWorkSource.getUids(), paramWorkSource.getTags(), paramString1, paramString2, 1);
      }
    }
  }
  
  public boolean noteMobileRadioPowerStateLocked(int paramInt1, long paramLong, int paramInt2)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mMobileRadioPowerState != paramInt1)
    {
      int i;
      if ((paramInt1 != 2) && (paramInt1 != 3)) {
        i = 0;
      } else {
        i = 1;
      }
      long l3;
      Object localObject;
      if (i != 0)
      {
        if (paramInt2 > 0) {
          noteMobileRadioApWakeupLocked(l1, l2, paramInt2);
        }
        l3 = paramLong / 1000000L;
        paramLong = l3;
        mMobileRadioActiveStartTime = l3;
        localObject = mHistoryCur;
        states |= 0x2000000;
      }
      else
      {
        paramLong /= 1000000L;
        l3 = mMobileRadioActiveStartTime;
        if (paramLong < l3)
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Data connection inactive timestamp ");
          ((StringBuilder)localObject).append(paramLong);
          ((StringBuilder)localObject).append(" is before start time ");
          ((StringBuilder)localObject).append(l3);
          Slog.wtf("BatteryStatsImpl", ((StringBuilder)localObject).toString());
          paramLong = l1;
        }
        for (;;)
        {
          break;
          if (paramLong < l1) {
            mMobileRadioActiveAdjustedTime.addCountLocked(l1 - paramLong);
          }
        }
        localObject = mHistoryCur;
        states &= 0xFDFFFFFF;
      }
      addHistoryRecordLocked(l1, l2);
      mMobileRadioPowerState = paramInt1;
      StatsLog.write_non_chained(12, paramInt2, null, paramInt1);
      if (i != 0)
      {
        mMobileRadioActiveTimer.startRunningLocked(l1);
        mMobileRadioActivePerAppTimer.startRunningLocked(l1);
        mMobileRadioOnEasyTimer.startRunningLocked(l1);
      }
      else
      {
        mMobileRadioActiveTimer.stopRunningLocked(paramLong);
        mMobileRadioActivePerAppTimer.stopRunningLocked(paramLong);
        mMobileRadioOnEasyTimer.stopRunningLocked(paramLong);
        return true;
      }
    }
    return false;
  }
  
  public void noteNetworkInterfaceTypeLocked(String paramString, int paramInt)
  {
    if (TextUtils.isEmpty(paramString)) {
      return;
    }
    synchronized (mModemNetworkLock)
    {
      if (ConnectivityManager.isNetworkTypeMobile(paramInt)) {
        mModemIfaces = includeInStringArray(mModemIfaces, paramString);
      } else {
        mModemIfaces = excludeFromStringArray(mModemIfaces, paramString);
      }
      synchronized (mWifiNetworkLock)
      {
        if (ConnectivityManager.isNetworkTypeWifi(paramInt)) {
          mWifiIfaces = includeInStringArray(mWifiIfaces, paramString);
        } else {
          mWifiIfaces = excludeFromStringArray(mWifiIfaces, paramString);
        }
        return;
      }
    }
  }
  
  public void notePackageInstalledLocked(String paramString, long paramLong)
  {
    addHistoryEventLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis(), 11, paramString, (int)paramLong);
    BatteryStats.PackageChange localPackageChange = new BatteryStats.PackageChange();
    mPackageName = paramString;
    mUpdate = true;
    mVersionCode = paramLong;
    addPackageChange(localPackageChange);
  }
  
  public void notePackageUninstalledLocked(String paramString)
  {
    addHistoryEventLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis(), 12, paramString, 0);
    BatteryStats.PackageChange localPackageChange = new BatteryStats.PackageChange();
    mPackageName = paramString;
    mUpdate = true;
    addPackageChange(localPackageChange);
  }
  
  public void notePhoneDataConnectionStateLocked(int paramInt, boolean paramBoolean)
  {
    int i = 0;
    if (paramBoolean) {
      if ((paramInt > 0) && (paramInt <= 19)) {
        i = paramInt;
      } else {
        i = 20;
      }
    }
    if (mPhoneDataConnectionType != i)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      mHistoryCur.states = (mHistoryCur.states & 0xC1FF | i << 9);
      addHistoryRecordLocked(l1, l2);
      if (mPhoneDataConnectionType >= 0) {
        mPhoneDataConnectionsTimer[mPhoneDataConnectionType].stopRunningLocked(l1);
      }
      mPhoneDataConnectionType = i;
      mPhoneDataConnectionsTimer[i].startRunningLocked(l1);
    }
  }
  
  public void notePhoneOffLocked()
  {
    if (mPhoneOn)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xFF7FFFFF;
      addHistoryRecordLocked(l1, l2);
      mPhoneOn = false;
      mPhoneOnTimer.stopRunningLocked(l1);
      mPhoneInCallEasyTimer.stopRunningLocked(l1);
    }
  }
  
  public void notePhoneOnLocked()
  {
    if (!mPhoneOn)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 |= 0x800000;
      addHistoryRecordLocked(l1, l2);
      mPhoneOn = true;
      mPhoneOnTimer.startRunningLocked(l1);
      mPhoneInCallEasyTimer.startRunningLocked(l1);
    }
  }
  
  public void notePhoneSignalStrengthLocked(SignalStrength paramSignalStrength)
  {
    int i = paramSignalStrength.getLevel();
    updateAllPhoneStateLocked(mPhoneServiceStateRaw, mPhoneSimStateRaw, i);
  }
  
  public void notePhoneStateLocked(int paramInt1, int paramInt2)
  {
    updateAllPhoneStateLocked(paramInt1, paramInt2, mPhoneSignalStrengthBinRaw);
  }
  
  public void notePowerSaveModeLocked(boolean paramBoolean)
  {
    if (mPowerSaveModeEnabled != paramBoolean)
    {
      int i = 0;
      int j;
      if (paramBoolean) {
        j = 4;
      } else {
        j = 0;
      }
      int k = mModStepMode;
      mModStepMode = (0x4 & mCurStepMode ^ j | k);
      mCurStepMode = (mCurStepMode & 0xFFFFFFFB | j);
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      mPowerSaveModeEnabled = paramBoolean;
      BatteryStats.HistoryItem localHistoryItem;
      if (paramBoolean)
      {
        localHistoryItem = mHistoryCur;
        states2 |= 0x80000000;
        mPowerSaveModeEnabledTimer.startRunningLocked(l1);
        mLowPowerModeEasyTimer.startRunningLocked(l1);
      }
      else
      {
        localHistoryItem = mHistoryCur;
        states2 &= 0x7FFFFFFF;
        mPowerSaveModeEnabledTimer.stopRunningLocked(l1);
        mLowPowerModeEasyTimer.stopRunningLocked(l1);
      }
      addHistoryRecordLocked(l1, l2);
      if (paramBoolean) {
        j = 1;
      } else {
        j = i;
      }
      StatsLog.write(20, j);
    }
  }
  
  public void noteProcessAnrLocked(String paramString, int paramInt)
  {
    paramInt = mapUid(paramInt);
    if (isOnBattery()) {
      getUidStatsLocked(paramInt).getProcessStatsLocked(paramString).incNumAnrsLocked();
    }
  }
  
  public void noteProcessCrashLocked(String paramString, int paramInt)
  {
    paramInt = mapUid(paramInt);
    if (isOnBattery()) {
      getUidStatsLocked(paramInt).getProcessStatsLocked(paramString).incNumCrashesLocked();
    }
  }
  
  public void noteProcessDiedLocked(int paramInt1, int paramInt2)
  {
    paramInt1 = mapUid(paramInt1);
    Uid localUid = (Uid)mUidStats.get(paramInt1);
    if (localUid != null) {
      mPids.remove(paramInt2);
    }
  }
  
  public void noteProcessFinishLocked(String paramString, int paramInt)
  {
    paramInt = mapUid(paramInt);
    if (!mActiveEvents.updateState(16385, paramString, paramInt, 0)) {
      return;
    }
    if (!mRecordAllHistory) {
      return;
    }
    addHistoryEventLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis(), 16385, paramString, paramInt);
  }
  
  public void noteProcessStartLocked(String paramString, int paramInt)
  {
    paramInt = mapUid(paramInt);
    if (isOnBattery()) {
      getUidStatsLocked(paramInt).getProcessStatsLocked(paramString).incStartsLocked();
    }
    if (!mActiveEvents.updateState(32769, paramString, paramInt, 0)) {
      return;
    }
    if (!mRecordAllHistory) {
      return;
    }
    addHistoryEventLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis(), 32769, paramString, paramInt);
  }
  
  public void noteResetAudioLocked()
  {
    if (mAudioOnNesting > 0)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      int i = 0;
      mAudioOnNesting = 0;
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states &= 0xFFBFFFFF;
      addHistoryRecordLocked(l1, l2);
      mAudioOnTimer.stopAllRunningLocked(l1);
      while (i < mUidStats.size())
      {
        ((Uid)mUidStats.valueAt(i)).noteResetAudioLocked(l1);
        i++;
      }
      mAudioOnEasyTimer.stopAllRunningLocked(l1);
    }
  }
  
  public void noteResetBluetoothScanLocked()
  {
    if (mBluetoothScanNesting > 0)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      int i = 0;
      mBluetoothScanNesting = 0;
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xFFEFFFFF;
      addHistoryRecordLocked(l1, l2);
      mBluetoothScanTimer.stopAllRunningLocked(l1);
      while (i < mUidStats.size())
      {
        ((Uid)mUidStats.valueAt(i)).noteResetBluetoothScanLocked(l1);
        i++;
      }
      mBleScanningEasyTimer.stopAllRunningLocked(l1);
    }
  }
  
  public void noteResetCameraLocked()
  {
    if (mCameraOnNesting > 0)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      int i = 0;
      mCameraOnNesting = 0;
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xFFDFFFFF;
      addHistoryRecordLocked(l1, l2);
      mCameraOnTimer.stopAllRunningLocked(l1);
      while (i < mUidStats.size())
      {
        ((Uid)mUidStats.valueAt(i)).noteResetCameraLocked(l1);
        i++;
      }
      mCameraOnEasyTimer.stopAllRunningLocked(l1);
    }
  }
  
  public void noteResetFlashlightLocked()
  {
    if (mFlashlightOnNesting > 0)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      int i = 0;
      mFlashlightOnNesting = 0;
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xF7FFFFFF;
      addHistoryRecordLocked(l1, l2);
      mFlashlightOnTimer.stopAllRunningLocked(l1);
      while (i < mUidStats.size())
      {
        ((Uid)mUidStats.valueAt(i)).noteResetFlashlightLocked(l1);
        i++;
      }
      mFlashLightEasyTimer.stopAllRunningLocked(l1);
    }
  }
  
  public void noteResetVideoLocked()
  {
    if (mVideoOnNesting > 0)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      int i = 0;
      mAudioOnNesting = 0;
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xBFFFFFFF;
      addHistoryRecordLocked(l1, l2);
      mVideoOnTimer.stopAllRunningLocked(l1);
      while (i < mUidStats.size())
      {
        ((Uid)mUidStats.valueAt(i)).noteResetVideoLocked(l1);
        i++;
      }
      mVideoOnEasyTimer.stopAllRunningLocked(l1);
    }
  }
  
  public void noteScreenBrightnessLocked(int paramInt)
  {
    int i = paramInt / 51;
    if (i < 0)
    {
      paramInt = 0;
    }
    else
    {
      paramInt = i;
      if (i >= 5) {
        paramInt = 4;
      }
    }
    if (mScreenBrightnessBin != paramInt)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      mHistoryCur.states = (mHistoryCur.states & 0xFFFFFFF8 | paramInt << 0);
      addHistoryRecordLocked(l1, l2);
      if (mScreenState == 2)
      {
        if (mScreenBrightnessBin >= 0) {
          mScreenBrightnessTimer[mScreenBrightnessBin].stopRunningLocked(l1);
        }
        mScreenBrightnessTimer[paramInt].startRunningLocked(l1);
      }
      mScreenBrightnessBin = paramInt;
    }
  }
  
  @GuardedBy("this")
  public void noteScreenStateLocked(int paramInt)
  {
    if (mPretendScreenOff) {
      paramInt = 1;
    }
    int i = paramInt;
    Object localObject;
    if (paramInt > 4) {
      if (paramInt != 5)
      {
        localObject = new StringBuilder();
        ((StringBuilder)localObject).append("Unknown screen state (not mapped): ");
        ((StringBuilder)localObject).append(paramInt);
        Slog.wtf("BatteryStatsImpl", ((StringBuilder)localObject).toString());
        i = paramInt;
      }
      else
      {
        i = 2;
      }
    }
    if (mScreenState != i)
    {
      recordDailyStatsIfNeededLocked(true);
      int j = mScreenState;
      mScreenState = i;
      if (i != 0)
      {
        paramInt = i - 1;
        if ((paramInt & 0x3) == paramInt)
        {
          if ((Build.FEATURES.ENABLE_ALWAYS_ON) && (mOnBattery))
          {
            if (((j == 3) && (i == 4)) || ((j == 4) && (i == 3)))
            {
              mCurStepMode = (mCurStepMode & 0xFFFFFFFC | paramInt);
            }
            else
            {
              mModStepMode |= mCurStepMode & 0x3 ^ paramInt;
              mCurStepMode = (mCurStepMode & 0xFFFFFFFC | paramInt);
            }
          }
          else
          {
            mModStepMode |= mCurStepMode & 0x3 ^ paramInt;
            mCurStepMode = (mCurStepMode & 0xFFFFFFFC | paramInt);
          }
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unexpected screen state: ");
          ((StringBuilder)localObject).append(i);
          Slog.wtf("BatteryStatsImpl", ((StringBuilder)localObject).toString());
        }
      }
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      paramInt = 0;
      if (isScreenDoze(i))
      {
        localObject = mHistoryCur;
        states |= 0x40000;
        mScreenDozeTimer.startRunningLocked(l1);
        paramInt = 1;
      }
      else if (isScreenDoze(j))
      {
        localObject = mHistoryCur;
        states &= 0xFFFBFFFF;
        mScreenDozeTimer.stopRunningLocked(l1);
        paramInt = 1;
      }
      if (isScreenOn(i))
      {
        localObject = mHistoryCur;
        states |= 0x100000;
        mScreenOnTimer.startRunningLocked(l1);
        if (mScreenBrightnessBin >= 0) {
          mScreenBrightnessTimer[mScreenBrightnessBin].startRunningLocked(l1);
        }
        paramInt = 1;
        mScreenOnDurEasyTimer.startRunningLocked(l1);
      }
      else if (isScreenOn(j))
      {
        localObject = mHistoryCur;
        states &= 0xFFEFFFFF;
        mScreenOnTimer.stopRunningLocked(l1);
        if (mScreenBrightnessBin >= 0) {
          mScreenBrightnessTimer[mScreenBrightnessBin].stopRunningLocked(l1);
        }
        paramInt = 1;
        mScreenOnDurEasyTimer.stopRunningLocked(l1);
      }
      if (paramInt != 0) {
        addHistoryRecordLocked(l1, l2);
      }
      mExternalSync.scheduleCpuSyncDueToScreenStateChange(mOnBatteryTimeBase.isRunning(), mOnBatteryScreenOffTimeBase.isRunning());
      if (isScreenOn(i))
      {
        updateTimeBasesLocked(mOnBatteryTimeBase.isRunning(), i, mClocks.uptimeMillis() * 1000L, l1 * 1000L);
        noteStartWakeLocked(-1, -1, null, "screen", null, 0, false, l1, l2);
      }
      else if (isScreenOn(j))
      {
        noteStopWakeLocked(-1, -1, null, "screen", "screen", 0, l1, l2);
        updateTimeBasesLocked(mOnBatteryTimeBase.isRunning(), i, mClocks.uptimeMillis() * 1000L, l1 * 1000L);
      }
      if (mOnBatteryInternal) {
        updateDischargeScreenLevelsLocked(j, i);
      }
    }
  }
  
  public void noteSoftAPOffLocked()
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mSoftAPOn)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xFFFDFFFF;
      addHistoryRecordLocked(l1, l2);
      mSoftAPOn = false;
      mWifiHotspotOnEasyTimer.stopRunningLocked(l1);
    }
  }
  
  public void noteSoftAPOnLocked()
  {
    if (!mSoftAPOn)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 |= 0x20000;
      addHistoryRecordLocked(l1, l2);
      mSoftAPOn = true;
      mWifiHotspotOnEasyTimer.startRunningLocked(l1);
    }
  }
  
  public void noteStartSensorLocked(int paramInt1, int paramInt2)
  {
    paramInt1 = mapUid(paramInt1);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mSensorNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states |= 0x800000;
      addHistoryRecordLocked(l1, l2);
    }
    mSensorNesting += 1;
    getUidStatsLocked(paramInt1).noteStartSensor(paramInt2, l1);
    mSensorOnEasyTimer.startRunningLocked(l1);
  }
  
  public void noteStartWakeFromSourceLocked(WorkSource paramWorkSource, int paramInt1, String paramString1, String paramString2, int paramInt2, boolean paramBoolean)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      noteStartWakeLocked(paramWorkSource.get(k), paramInt1, null, paramString1, paramString2, paramInt2, paramBoolean, l1, l2);
    }
    paramWorkSource = paramWorkSource.getWorkChains();
    Object localObject = paramWorkSource;
    if (paramWorkSource != null) {
      for (i = j;; i++)
      {
        localObject = paramWorkSource;
        if (i >= paramWorkSource.size()) {
          break;
        }
        localObject = (WorkSource.WorkChain)paramWorkSource.get(i);
        noteStartWakeLocked(((WorkSource.WorkChain)localObject).getAttributionUid(), paramInt1, (WorkSource.WorkChain)localObject, paramString1, paramString2, paramInt2, paramBoolean, l1, l2);
      }
    }
  }
  
  public void noteStartWakeLocked(int paramInt1, int paramInt2, WorkSource.WorkChain paramWorkChain, String paramString1, String paramString2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2)
  {
    paramInt1 = mapUid(paramInt1);
    if (paramInt3 == 0)
    {
      aggregateLastWakeupUptimeLocked(paramLong2);
      if (paramString2 == null) {
        paramString2 = paramString1;
      }
      if ((mRecordAllHistory) && (mActiveEvents.updateState(32773, paramString2, paramInt1, 0))) {
        addHistoryEventLocked(paramLong1, paramLong2, 32773, paramString2, paramInt1);
      }
      Object localObject;
      if (mWakeLockNesting == 0)
      {
        localObject = mHistoryCur;
        states |= 0x40000000;
        mHistoryCur.wakelockTag = mHistoryCur.localWakelockTag;
        localObject = mHistoryCur.wakelockTag;
        mInitialAcquireWakeName = paramString2;
        string = paramString2;
        paramString2 = mHistoryCur.wakelockTag;
        mInitialAcquireWakeUid = paramInt1;
        uid = paramInt1;
        mWakeLockImportant = (paramBoolean ^ true);
        addHistoryRecordLocked(paramLong1, paramLong2);
      }
      else if ((!mWakeLockImportant) && (!paramBoolean) && (mHistoryLastWritten.cmd == 0))
      {
        if (mHistoryLastWritten.wakelockTag != null)
        {
          mHistoryLastWritten.wakelockTag = null;
          mHistoryCur.wakelockTag = mHistoryCur.localWakelockTag;
          localObject = mHistoryCur.wakelockTag;
          mInitialAcquireWakeName = paramString2;
          string = paramString2;
          paramString2 = mHistoryCur.wakelockTag;
          mInitialAcquireWakeUid = paramInt1;
          uid = paramInt1;
          addHistoryRecordLocked(paramLong1, paramLong2);
        }
        mWakeLockImportant = true;
      }
      mWakeLockNesting += 1;
    }
    if (paramInt1 >= 0)
    {
      if (mOnBatteryScreenOffTimeBase.isRunning()) {
        requestWakelockCpuUpdate();
      }
      getUidStatsLocked(paramInt1).noteStartWakeLocked(paramInt2, paramString1, paramInt3, paramLong1);
      if (paramWorkChain != null) {
        StatsLog.write(10, paramWorkChain.getUids(), paramWorkChain.getTags(), getPowerManagerWakeLockLevel(paramInt3), paramString1, 1);
      } else {
        StatsLog.write_non_chained(10, paramInt1, null, getPowerManagerWakeLockLevel(paramInt3), paramString1, 1);
      }
    }
  }
  
  public void noteStopSensorLocked(int paramInt1, int paramInt2)
  {
    paramInt1 = mapUid(paramInt1);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    mSensorNesting -= 1;
    if (mSensorNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states &= 0xFF7FFFFF;
      addHistoryRecordLocked(l1, l2);
    }
    getUidStatsLocked(paramInt1).noteStopSensor(paramInt2, l1);
    mSensorOnEasyTimer.stopRunningLocked(l1);
  }
  
  public void noteStopWakeFromSourceLocked(WorkSource paramWorkSource, int paramInt1, String paramString1, String paramString2, int paramInt2)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      noteStopWakeLocked(paramWorkSource.get(k), paramInt1, null, paramString1, paramString2, paramInt2, l1, l2);
    }
    paramWorkSource = paramWorkSource.getWorkChains();
    Object localObject = paramWorkSource;
    if (paramWorkSource != null) {
      for (i = j;; i++)
      {
        localObject = paramWorkSource;
        if (i >= paramWorkSource.size()) {
          break;
        }
        localObject = (WorkSource.WorkChain)paramWorkSource.get(i);
        noteStopWakeLocked(((WorkSource.WorkChain)localObject).getAttributionUid(), paramInt1, (WorkSource.WorkChain)localObject, paramString1, paramString2, paramInt2, l1, l2);
      }
    }
  }
  
  public void noteStopWakeLocked(int paramInt1, int paramInt2, WorkSource.WorkChain paramWorkChain, String paramString1, String paramString2, int paramInt3, long paramLong1, long paramLong2)
  {
    paramInt1 = mapUid(paramInt1);
    if (paramInt3 == 0)
    {
      mWakeLockNesting -= 1;
      if (mRecordAllHistory)
      {
        if (paramString2 == null) {
          paramString2 = paramString1;
        }
        if (mActiveEvents.updateState(16389, paramString2, paramInt1, 0)) {
          addHistoryEventLocked(paramLong1, paramLong2, 16389, paramString2, paramInt1);
        }
      }
      if (mWakeLockNesting == 0)
      {
        paramString2 = mHistoryCur;
        states &= 0xBFFFFFFF;
        mInitialAcquireWakeName = null;
        mInitialAcquireWakeUid = -1;
        addHistoryRecordLocked(paramLong1, paramLong2);
      }
      else {}
    }
    if (paramInt1 >= 0)
    {
      if (mOnBatteryScreenOffTimeBase.isRunning()) {
        requestWakelockCpuUpdate();
      }
      getUidStatsLocked(paramInt1).noteStopWakeLocked(paramInt2, paramString1, paramInt3, paramLong1);
      if (paramWorkChain != null) {
        StatsLog.write(10, paramWorkChain.getUids(), paramWorkChain.getTags(), getPowerManagerWakeLockLevel(paramInt3), paramString1, 0);
      } else {
        StatsLog.write_non_chained(10, paramInt1, null, getPowerManagerWakeLockLevel(paramInt3), paramString1, 0);
      }
    }
  }
  
  public void noteSyncFinishLocked(String paramString, int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    getUidStatsLocked(paramInt).noteStopSyncLocked(paramString, l1);
    if (!mActiveEvents.updateState(16388, paramString, paramInt, 0)) {
      return;
    }
    addHistoryEventLocked(l1, l2, 16388, paramString, paramInt);
  }
  
  public void noteSyncStartLocked(String paramString, int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    getUidStatsLocked(paramInt).noteStartSyncLocked(paramString, l1);
    if (!mActiveEvents.updateState(32772, paramString, paramInt, 0)) {
      return;
    }
    addHistoryEventLocked(l1, l2, 32772, paramString, paramInt);
  }
  
  public void noteUidProcessStateLocked(int paramInt1, int paramInt2)
  {
    if (paramInt1 != mapUid(paramInt1)) {
      return;
    }
    getUidStatsLocked(paramInt1).updateUidProcessStateLocked(paramInt2);
  }
  
  public void noteUserActivityLocked(int paramInt1, int paramInt2)
  {
    if (mOnBatteryInternal) {
      getUidStatsLocked(mapUid(paramInt1)).noteUserActivityLocked(paramInt2);
    }
  }
  
  public void noteVibratorOffLocked(int paramInt)
  {
    getUidStatsLocked(mapUid(paramInt)).noteVibratorOffLocked();
  }
  
  public void noteVibratorOnLocked(int paramInt, long paramLong)
  {
    getUidStatsLocked(mapUid(paramInt)).noteVibratorOnLocked(paramLong);
  }
  
  public void noteVideoOffLocked(int paramInt)
  {
    if (mVideoOnNesting == 0) {
      return;
    }
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    int i = mVideoOnNesting - 1;
    mVideoOnNesting = i;
    if (i == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xBFFFFFFF;
      addHistoryRecordLocked(l1, l2);
      mVideoOnTimer.stopRunningLocked(l1);
    }
    getUidStatsLocked(paramInt).noteVideoTurnedOffLocked(l1);
    mVideoOnEasyTimer.stopRunningLocked(l1);
  }
  
  public void noteVideoOnLocked(int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mVideoOnNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 |= 0x40000000;
      addHistoryRecordLocked(l1, l2);
      mVideoOnTimer.startRunningLocked(l1);
    }
    mVideoOnNesting += 1;
    getUidStatsLocked(paramInt).noteVideoTurnedOnLocked(l1);
    mVideoOnEasyTimer.startRunningLocked(l1);
  }
  
  public void noteWakeUpLocked(String paramString, int paramInt)
  {
    addHistoryEventLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis(), 18, paramString, paramInt);
  }
  
  public void noteWakeupReasonLocked(String paramString)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    aggregateLastWakeupUptimeLocked(l2);
    mHistoryCur.wakeReasonTag = mHistoryCur.localWakeReasonTag;
    mHistoryCur.wakeReasonTag.string = paramString;
    mHistoryCur.wakeReasonTag.uid = 0;
    mLastWakeupReason = paramString;
    mLastWakeupUptimeMs = l2;
    addHistoryRecordLocked(l1, l2);
  }
  
  public void noteWakupAlarmLocked(String paramString1, int paramInt, WorkSource paramWorkSource, String paramString2)
  {
    if (paramWorkSource != null)
    {
      int i = 0;
      Object localObject;
      for (int j = 0; j < paramWorkSource.size(); j++)
      {
        paramInt = paramWorkSource.get(j);
        localObject = paramWorkSource.getName(j);
        if (isOnBattery())
        {
          if (localObject == null) {
            localObject = paramString1;
          }
          getPackageStatsLocked(paramInt, (String)localObject).noteWakeupAlarmLocked(paramString2);
        }
        StatsLog.write_non_chained(35, paramWorkSource.get(j), paramWorkSource.getName(j), paramString2);
      }
      paramWorkSource = paramWorkSource.getWorkChains();
      j = paramInt;
      if (paramWorkSource != null) {
        for (;;)
        {
          j = paramInt;
          if (i >= paramWorkSource.size()) {
            break;
          }
          localObject = (WorkSource.WorkChain)paramWorkSource.get(i);
          paramInt = ((WorkSource.WorkChain)localObject).getAttributionUid();
          if (isOnBattery()) {
            getPackageStatsLocked(paramInt, paramString1).noteWakeupAlarmLocked(paramString2);
          }
          StatsLog.write(35, ((WorkSource.WorkChain)localObject).getUids(), ((WorkSource.WorkChain)localObject).getTags(), paramString2);
          i++;
        }
      }
    }
    else
    {
      if (isOnBattery()) {
        getPackageStatsLocked(paramInt, paramString1).noteWakeupAlarmLocked(paramString2);
      }
      StatsLog.write_non_chained(35, paramInt, null, paramString2);
    }
  }
  
  public void noteWifiBatchedScanStartedFromSourceLocked(WorkSource paramWorkSource, int paramInt)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      noteWifiBatchedScanStartedLocked(paramWorkSource.get(k), paramInt);
    }
    paramWorkSource = paramWorkSource.getWorkChains();
    if (paramWorkSource != null) {
      for (k = j; k < paramWorkSource.size(); k++) {
        noteWifiBatchedScanStartedLocked(((WorkSource.WorkChain)paramWorkSource.get(k)).getAttributionUid(), paramInt);
      }
    }
  }
  
  public void noteWifiBatchedScanStartedLocked(int paramInt1, int paramInt2)
  {
    paramInt1 = mapUid(paramInt1);
    long l = mClocks.elapsedRealtime();
    getUidStatsLocked(paramInt1).noteWifiBatchedScanStartedLocked(paramInt2, l);
  }
  
  public void noteWifiBatchedScanStoppedFromSourceLocked(WorkSource paramWorkSource)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++) {
      noteWifiBatchedScanStoppedLocked(paramWorkSource.get(k));
    }
    paramWorkSource = paramWorkSource.getWorkChains();
    if (paramWorkSource != null) {
      for (k = j; k < paramWorkSource.size(); k++) {
        noteWifiBatchedScanStoppedLocked(((WorkSource.WorkChain)paramWorkSource.get(k)).getAttributionUid());
      }
    }
  }
  
  public void noteWifiBatchedScanStoppedLocked(int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l = mClocks.elapsedRealtime();
    getUidStatsLocked(paramInt).noteWifiBatchedScanStoppedLocked(l);
  }
  
  public void noteWifiMulticastDisabledLocked(int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    mWifiMulticastNesting -= 1;
    if (mWifiMulticastNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states &= 0xFFFEFFFF;
      addHistoryRecordLocked(l1, l2);
      if (mWifiMulticastWakelockTimer.isRunningLocked()) {
        mWifiMulticastWakelockTimer.stopRunningLocked(l1);
      }
    }
    getUidStatsLocked(paramInt).noteWifiMulticastDisabledLocked(l1);
    mWifiMultiCastOnEasyTimer.stopRunningLocked(l1);
  }
  
  public void noteWifiMulticastEnabledLocked(int paramInt)
  {
    paramInt = mapUid(paramInt);
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mWifiMulticastNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states |= 0x10000;
      addHistoryRecordLocked(l1, l2);
      if (!mWifiMulticastWakelockTimer.isRunningLocked()) {
        mWifiMulticastWakelockTimer.startRunningLocked(l1);
      }
    }
    mWifiMulticastNesting += 1;
    getUidStatsLocked(paramInt).noteWifiMulticastEnabledLocked(l1);
    mWifiMultiCastOnEasyTimer.startRunningLocked(l1);
  }
  
  public void noteWifiOffLocked()
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mWifiOn)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xEFFFFFFF;
      addHistoryRecordLocked(l1, l2);
      mWifiOn = false;
      mWifiOnTimer.stopRunningLocked(l1);
      scheduleSyncExternalStatsLocked("wifi-on", 2);
      mWifiOnEasyTimer.stopRunningLocked(l1);
    }
  }
  
  public void noteWifiOnLocked()
  {
    if (!mWifiOn)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 |= 0x10000000;
      addHistoryRecordLocked(l1, l2);
      mWifiOn = true;
      mWifiOnTimer.startRunningLocked(l1);
      scheduleSyncExternalStatsLocked("wifi-off", 2);
      mWifiOnEasyTimer.startRunningLocked(l1);
    }
  }
  
  public void noteWifiRadioPowerState(int paramInt1, long paramLong, int paramInt2)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mWifiRadioPowerState != paramInt1)
    {
      int i;
      if ((paramInt1 != 2) && (paramInt1 != 3)) {
        i = 0;
      } else {
        i = 1;
      }
      BatteryStats.HistoryItem localHistoryItem;
      if (i != 0)
      {
        if (paramInt2 > 0) {
          noteWifiRadioApWakeupLocked(l1, l2, paramInt2);
        }
        localHistoryItem = mHistoryCur;
        states |= 0x4000000;
        mWifiActiveTimer.startRunningLocked(l1);
      }
      else
      {
        localHistoryItem = mHistoryCur;
        states &= 0xFBFFFFFF;
        mWifiActiveTimer.stopRunningLocked(paramLong / 1000000L);
      }
      addHistoryRecordLocked(l1, l2);
      mWifiRadioPowerState = paramInt1;
      StatsLog.write_non_chained(13, paramInt2, null, paramInt1);
    }
  }
  
  public void noteWifiRssiChangedLocked(int paramInt)
  {
    paramInt = WifiManager.calculateSignalLevel(paramInt, 5);
    if (mWifiSignalStrengthBin != paramInt)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      if (mWifiSignalStrengthBin >= 0) {
        mWifiSignalStrengthsTimer[mWifiSignalStrengthBin].stopRunningLocked(l1);
      }
      if (paramInt >= 0)
      {
        if (!mWifiSignalStrengthsTimer[paramInt].isRunningLocked()) {
          mWifiSignalStrengthsTimer[paramInt].startRunningLocked(l1);
        }
        mHistoryCur.states2 = (mHistoryCur.states2 & 0xFFFFFF8F | paramInt << 4);
        addHistoryRecordLocked(l1, l2);
      }
      else
      {
        stopAllWifiSignalStrengthTimersLocked(-1);
      }
      StatsLog.write(38, paramInt);
      mWifiSignalStrengthBin = paramInt;
    }
  }
  
  public void noteWifiRunningChangedLocked(WorkSource paramWorkSource1, WorkSource paramWorkSource2)
  {
    if (mGlobalWifiRunning)
    {
      long l = mClocks.elapsedRealtime();
      int i = paramWorkSource1.size();
      int j = 0;
      for (int k = 0; k < i; k++) {
        getUidStatsLocked(mapUid(paramWorkSource1.get(k))).noteWifiStoppedLocked(l);
      }
      paramWorkSource1 = paramWorkSource1.getWorkChains();
      if (paramWorkSource1 != null) {
        for (k = 0; k < paramWorkSource1.size(); k++) {
          getUidStatsLocked(mapUid(((WorkSource.WorkChain)paramWorkSource1.get(k)).getAttributionUid())).noteWifiStoppedLocked(l);
        }
      }
      i = paramWorkSource2.size();
      for (k = 0; k < i; k++) {
        getUidStatsLocked(mapUid(paramWorkSource2.get(k))).noteWifiRunningLocked(l);
      }
      paramWorkSource1 = paramWorkSource2.getWorkChains();
      if (paramWorkSource1 != null) {
        for (k = j; k < paramWorkSource1.size(); k++) {
          getUidStatsLocked(mapUid(((WorkSource.WorkChain)paramWorkSource1.get(k)).getAttributionUid())).noteWifiRunningLocked(l);
        }
      }
    }
    else
    {
      Log.w("BatteryStatsImpl", "noteWifiRunningChangedLocked -- called while WIFI not running");
    }
  }
  
  public void noteWifiRunningLocked(WorkSource paramWorkSource)
  {
    if (!mGlobalWifiRunning)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 |= 0x20000000;
      addHistoryRecordLocked(l1, l2);
      mGlobalWifiRunning = true;
      mGlobalWifiRunningTimer.startRunningLocked(l1);
      int i = paramWorkSource.size();
      int j = 0;
      for (int k = 0; k < i; k++) {
        getUidStatsLocked(mapUid(paramWorkSource.get(k))).noteWifiRunningLocked(l1);
      }
      paramWorkSource = paramWorkSource.getWorkChains();
      if (paramWorkSource != null) {
        for (k = j; k < paramWorkSource.size(); k++) {
          getUidStatsLocked(mapUid(((WorkSource.WorkChain)paramWorkSource.get(k)).getAttributionUid())).noteWifiRunningLocked(l1);
        }
      }
      scheduleSyncExternalStatsLocked("wifi-running", 2);
      mWifiRunningEasyTimer.startRunningLocked(l1);
    }
    else
    {
      Log.w("BatteryStatsImpl", "noteWifiRunningLocked -- called while WIFI running");
    }
  }
  
  public void noteWifiScanStartedFromSourceLocked(WorkSource paramWorkSource)
  {
    int i = paramWorkSource.size();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      noteWifiScanStartedLocked(mapUid(paramWorkSource.get(k)));
      StatsLog.write_non_chained(39, paramWorkSource.get(k), paramWorkSource.getName(k), 1);
    }
    ArrayList localArrayList = paramWorkSource.getWorkChains();
    if (localArrayList != null) {
      for (k = j; k < localArrayList.size(); k++)
      {
        paramWorkSource = (WorkSource.WorkChain)localArrayList.get(k);
        noteWifiScanStartedLocked(mapUid(paramWorkSource.getAttributionUid()));
        StatsLog.write(39, paramWorkSource.getUids(), paramWorkSource.getTags(), 1);
      }
    }
  }
  
  public void noteWifiScanStartedLocked(int paramInt)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    if (mWifiScanNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states |= 0x8000000;
      addHistoryRecordLocked(l1, l2);
    }
    mWifiScanNesting += 1;
    getUidStatsLocked(paramInt).noteWifiScanStartedLocked(l1);
    mWifiScanEasyTimer.startRunningLocked(l1);
  }
  
  public void noteWifiScanStoppedFromSourceLocked(WorkSource paramWorkSource)
  {
    int i = paramWorkSource.size();
    for (int j = 0; j < i; j++)
    {
      noteWifiScanStoppedLocked(mapUid(paramWorkSource.get(j)));
      StatsLog.write_non_chained(39, paramWorkSource.get(j), paramWorkSource.getName(j), 0);
    }
    paramWorkSource = paramWorkSource.getWorkChains();
    if (paramWorkSource != null) {
      for (j = 0; j < paramWorkSource.size(); j++)
      {
        WorkSource.WorkChain localWorkChain = (WorkSource.WorkChain)paramWorkSource.get(j);
        noteWifiScanStoppedLocked(mapUid(localWorkChain.getAttributionUid()));
        StatsLog.write(39, localWorkChain.getUids(), localWorkChain.getTags(), 0);
      }
    }
  }
  
  public void noteWifiScanStoppedLocked(int paramInt)
  {
    long l1 = mClocks.elapsedRealtime();
    long l2 = mClocks.uptimeMillis();
    mWifiScanNesting -= 1;
    if (mWifiScanNesting == 0)
    {
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states &= 0xF7FFFFFF;
      addHistoryRecordLocked(l1, l2);
    }
    getUidStatsLocked(paramInt).noteWifiScanStoppedLocked(l1);
    mWifiScanEasyTimer.stopRunningLocked(l1);
  }
  
  public void noteWifiStateLocked(int paramInt, String paramString)
  {
    if (mWifiState != paramInt)
    {
      long l = mClocks.elapsedRealtime();
      if (mWifiState >= 0) {
        mWifiStateTimer[mWifiState].stopRunningLocked(l);
      }
      mWifiState = paramInt;
      mWifiStateTimer[paramInt].startRunningLocked(l);
      scheduleSyncExternalStatsLocked("wifi-state", 2);
    }
  }
  
  public void noteWifiStoppedLocked(WorkSource paramWorkSource)
  {
    if (mGlobalWifiRunning)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      BatteryStats.HistoryItem localHistoryItem = mHistoryCur;
      states2 &= 0xDFFFFFFF;
      addHistoryRecordLocked(l1, l2);
      int i = 0;
      mGlobalWifiRunning = false;
      mGlobalWifiRunningTimer.stopRunningLocked(l1);
      int j = paramWorkSource.size();
      for (int k = 0; k < j; k++) {
        getUidStatsLocked(mapUid(paramWorkSource.get(k))).noteWifiStoppedLocked(l1);
      }
      paramWorkSource = paramWorkSource.getWorkChains();
      if (paramWorkSource != null) {
        for (k = i; k < paramWorkSource.size(); k++) {
          getUidStatsLocked(mapUid(((WorkSource.WorkChain)paramWorkSource.get(k)).getAttributionUid())).noteWifiStoppedLocked(l1);
        }
      }
      scheduleSyncExternalStatsLocked("wifi-stopped", 2);
      mWifiRunningEasyTimer.stopRunningLocked(l1);
    }
    else
    {
      Log.w("BatteryStatsImpl", "noteWifiStoppedLocked -- called while WIFI not running");
    }
  }
  
  public void noteWifiSupplicantStateChangedLocked(int paramInt, boolean paramBoolean)
  {
    if (mWifiSupplState != paramInt)
    {
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      if (mWifiSupplState >= 0) {
        mWifiSupplStateTimer[mWifiSupplState].stopRunningLocked(l1);
      }
      mWifiSupplState = paramInt;
      mWifiSupplStateTimer[paramInt].startRunningLocked(l1);
      mHistoryCur.states2 = (mHistoryCur.states2 & 0xFFFFFFF0 | paramInt << 0);
      addHistoryRecordLocked(l1, l2);
    }
  }
  
  public void onCleanupUserLocked(int paramInt)
  {
    int i = UserHandle.getUid(paramInt, 0);
    paramInt = UserHandle.getUid(paramInt, 99999);
    mPendingRemovedUids.add(new UidToRemove(i, paramInt, mClocks.elapsedRealtime()));
  }
  
  public void onUserRemovedLocked(int paramInt)
  {
    int i = UserHandle.getUid(paramInt, 0);
    int j = UserHandle.getUid(paramInt, 99999);
    mUidStats.put(i, null);
    mUidStats.put(j, null);
    paramInt = mUidStats.indexOfKey(i);
    i = mUidStats.indexOfKey(j);
    mUidStats.removeAtRange(paramInt, i - paramInt + 1);
  }
  
  public void postBatteryNeedsCpuUpdateMsg()
  {
    mHandler.sendEmptyMessage(1);
  }
  
  public void prepareForDumpLocked()
  {
    pullPendingStateUpdatesLocked();
    getStartClockTime();
  }
  
  public void pullPendingStateUpdatesLocked()
  {
    if (mOnBatteryInternal) {
      updateDischargeScreenLevelsLocked(mScreenState, mScreenState);
    }
  }
  
  void readDailyItemTagDetailsLocked(XmlPullParser paramXmlPullParser, BatteryStats.DailyItem paramDailyItem, boolean paramBoolean, String paramString)
    throws NumberFormatException, XmlPullParserException, IOException
  {
    Object localObject = paramXmlPullParser.getAttributeValue(null, "n");
    if (localObject == null)
    {
      paramDailyItem = new StringBuilder();
      paramDailyItem.append("Missing 'n' attribute at ");
      paramDailyItem.append(paramXmlPullParser.getPositionDescription());
      Slog.w("BatteryStatsImpl", paramDailyItem.toString());
      XmlUtils.skipCurrentTag(paramXmlPullParser);
      return;
    }
    int i = Integer.parseInt((String)localObject);
    localObject = new BatteryStats.LevelStepTracker(i);
    if (paramBoolean) {
      mChargeSteps = ((BatteryStats.LevelStepTracker)localObject);
    } else {
      mDischargeSteps = ((BatteryStats.LevelStepTracker)localObject);
    }
    int j = 0;
    int k = paramXmlPullParser.getDepth();
    for (;;)
    {
      int m = paramXmlPullParser.next();
      if ((m == 1) || ((m == 3) && (paramXmlPullParser.getDepth() <= k))) {
        break;
      }
      if ((m != 3) && (m != 4))
      {
        if ("s".equals(paramXmlPullParser.getName()))
        {
          m = j;
          if (j < i)
          {
            paramDailyItem = paramXmlPullParser.getAttributeValue(null, "v");
            m = j;
            if (paramDailyItem != null)
            {
              ((BatteryStats.LevelStepTracker)localObject).decodeEntryAt(j, paramDailyItem);
              m = j + 1;
            }
          }
        }
        else
        {
          paramDailyItem = new StringBuilder();
          paramDailyItem.append("Unknown element under <");
          paramDailyItem.append(paramString);
          paramDailyItem.append(">: ");
          paramDailyItem.append(paramXmlPullParser.getName());
          Slog.w("BatteryStatsImpl", paramDailyItem.toString());
          XmlUtils.skipCurrentTag(paramXmlPullParser);
          m = j;
        }
        j = m;
      }
    }
    mNumStepDurations = j;
  }
  
  void readDailyItemTagLocked(XmlPullParser paramXmlPullParser)
    throws NumberFormatException, XmlPullParserException, IOException
  {
    BatteryStats.DailyItem localDailyItem = new BatteryStats.DailyItem();
    Object localObject = paramXmlPullParser.getAttributeValue(null, "start");
    if (localObject != null) {
      mStartTime = Long.parseLong((String)localObject);
    }
    localObject = paramXmlPullParser.getAttributeValue(null, "end");
    if (localObject != null) {
      mEndTime = Long.parseLong((String)localObject);
    }
    int i = paramXmlPullParser.getDepth();
    for (;;)
    {
      int j = paramXmlPullParser.next();
      if ((j == 1) || ((j == 3) && (paramXmlPullParser.getDepth() <= i))) {
        break;
      }
      if ((j != 3) && (j != 4))
      {
        localObject = paramXmlPullParser.getName();
        if (((String)localObject).equals("dis"))
        {
          readDailyItemTagDetailsLocked(paramXmlPullParser, localDailyItem, false, "dis");
        }
        else if (((String)localObject).equals("chg"))
        {
          readDailyItemTagDetailsLocked(paramXmlPullParser, localDailyItem, true, "chg");
        }
        else if (((String)localObject).equals("upd"))
        {
          if (mPackageChanges == null) {
            mPackageChanges = new ArrayList();
          }
          BatteryStats.PackageChange localPackageChange = new BatteryStats.PackageChange();
          mUpdate = true;
          mPackageName = paramXmlPullParser.getAttributeValue(null, "pkg");
          localObject = paramXmlPullParser.getAttributeValue(null, "ver");
          long l;
          if (localObject != null) {
            l = Long.parseLong((String)localObject);
          } else {
            l = 0L;
          }
          mVersionCode = l;
          mPackageChanges.add(localPackageChange);
          XmlUtils.skipCurrentTag(paramXmlPullParser);
        }
        else if (((String)localObject).equals("rem"))
        {
          if (mPackageChanges == null) {
            mPackageChanges = new ArrayList();
          }
          localObject = new BatteryStats.PackageChange();
          mUpdate = false;
          mPackageName = paramXmlPullParser.getAttributeValue(null, "pkg");
          mPackageChanges.add(localObject);
          XmlUtils.skipCurrentTag(paramXmlPullParser);
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Unknown element under <item>: ");
          ((StringBuilder)localObject).append(paramXmlPullParser.getName());
          Slog.w("BatteryStatsImpl", ((StringBuilder)localObject).toString());
          XmlUtils.skipCurrentTag(paramXmlPullParser);
        }
      }
    }
    mDailyItems.add(localDailyItem);
  }
  
  /* Error */
  public void readDailyStatsLocked()
  {
    // Byte code:
    //   0: new 1475	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 1476	java/lang/StringBuilder:<init>	()V
    //   7: astore_1
    //   8: aload_1
    //   9: ldc_w 3498
    //   12: invokevirtual 1482	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   15: pop
    //   16: aload_1
    //   17: aload_0
    //   18: getfield 958	com/android/internal/os/BatteryStatsImpl:mDailyFile	Lcom/android/internal/os/AtomicFile;
    //   21: invokevirtual 3501	com/android/internal/os/AtomicFile:getBaseFile	()Ljava/io/File;
    //   24: invokevirtual 1814	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: ldc -68
    //   30: aload_1
    //   31: invokevirtual 1489	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   34: invokestatic 1622	android/util/Slog:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   37: pop
    //   38: aload_0
    //   39: getfield 857	com/android/internal/os/BatteryStatsImpl:mDailyItems	Ljava/util/ArrayList;
    //   42: invokevirtual 3001	java/util/ArrayList:clear	()V
    //   45: aload_0
    //   46: getfield 958	com/android/internal/os/BatteryStatsImpl:mDailyFile	Lcom/android/internal/os/AtomicFile;
    //   49: invokevirtual 3505	com/android/internal/os/AtomicFile:openRead	()Ljava/io/FileInputStream;
    //   52: astore_1
    //   53: invokestatic 3511	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   56: astore_2
    //   57: aload_2
    //   58: aload_1
    //   59: getstatic 3517	java/nio/charset/StandardCharsets:UTF_8	Ljava/nio/charset/Charset;
    //   62: invokevirtual 3522	java/nio/charset/Charset:name	()Ljava/lang/String;
    //   65: invokeinterface 3526 3 0
    //   70: aload_0
    //   71: aload_2
    //   72: invokespecial 3528	com/android/internal/os/BatteryStatsImpl:readDailyItemsLocked	(Lorg/xmlpull/v1/XmlPullParser;)V
    //   75: aload_1
    //   76: invokevirtual 3531	java/io/FileInputStream:close	()V
    //   79: goto +19 -> 98
    //   82: astore_2
    //   83: aload_1
    //   84: invokevirtual 3531	java/io/FileInputStream:close	()V
    //   87: goto +4 -> 91
    //   90: astore_1
    //   91: aload_2
    //   92: athrow
    //   93: astore_2
    //   94: aload_1
    //   95: invokevirtual 3531	java/io/FileInputStream:close	()V
    //   98: goto +4 -> 102
    //   101: astore_1
    //   102: return
    //   103: astore_1
    //   104: return
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	105	0	this	BatteryStatsImpl
    //   7	77	1	localObject1	Object
    //   90	5	1	localIOException1	IOException
    //   101	1	1	localIOException2	IOException
    //   103	1	1	localFileNotFoundException	java.io.FileNotFoundException
    //   56	16	2	localXmlPullParser	XmlPullParser
    //   82	10	2	localObject2	Object
    //   93	1	2	localXmlPullParserException	XmlPullParserException
    // Exception table:
    //   from	to	target	type
    //   53	75	82	finally
    //   83	87	90	java/io/IOException
    //   53	75	93	org/xmlpull/v1/XmlPullParserException
    //   75	79	101	java/io/IOException
    //   94	98	101	java/io/IOException
    //   45	53	103	java/io/FileNotFoundException
  }
  
  public void readFromParcel(Parcel paramParcel)
  {
    readFromParcelLocked(paramParcel);
  }
  
  void readFromParcelLocked(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    if (i == -1166707595)
    {
      int j = 0;
      readHistory(paramParcel, false);
      mStartCount = paramParcel.readInt();
      mStartClockTime = paramParcel.readLong();
      mStartPlatformVersion = paramParcel.readString();
      mEndPlatformVersion = paramParcel.readString();
      mUptime = paramParcel.readLong();
      mUptimeStart = paramParcel.readLong();
      mRealtime = paramParcel.readLong();
      mRealtimeStart = paramParcel.readLong();
      i = paramParcel.readInt();
      boolean bool1 = true;
      boolean bool2;
      if (i != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mOnBattery = bool2;
      mEstimatedBatteryCapacity = paramParcel.readInt();
      mMinLearnedBatteryCapacity = paramParcel.readInt();
      mMaxLearnedBatteryCapacity = paramParcel.readInt();
      mOnBatteryInternal = false;
      mOnBatteryTimeBase.readFromParcel(paramParcel);
      mOnBatteryScreenOffTimeBase.readFromParcel(paramParcel);
      mScreenState = 0;
      mScreenOnTimer = new StopwatchTimer(mClocks, null, -1, null, mOnBatteryTimeBase, paramParcel);
      mScreenDozeTimer = new StopwatchTimer(mClocks, null, -1, null, mOnBatteryTimeBase, paramParcel);
      for (i = 0; i < 5; i++) {
        mScreenBrightnessTimer[i] = new StopwatchTimer(mClocks, null, -100 - i, null, mOnBatteryTimeBase, paramParcel);
      }
      mInteractive = false;
      mInteractiveTimer = new StopwatchTimer(mClocks, null, -10, null, mOnBatteryTimeBase, paramParcel);
      mPhoneOn = false;
      mPowerSaveModeEnabledTimer = new StopwatchTimer(mClocks, null, -2, null, mOnBatteryTimeBase, paramParcel);
      mLongestLightIdleTime = paramParcel.readLong();
      mLongestFullIdleTime = paramParcel.readLong();
      mDeviceIdleModeLightTimer = new StopwatchTimer(mClocks, null, -14, null, mOnBatteryTimeBase, paramParcel);
      mDeviceIdleModeFullTimer = new StopwatchTimer(mClocks, null, -11, null, mOnBatteryTimeBase, paramParcel);
      mDeviceLightIdlingTimer = new StopwatchTimer(mClocks, null, -15, null, mOnBatteryTimeBase, paramParcel);
      mDeviceIdlingTimer = new StopwatchTimer(mClocks, null, -12, null, mOnBatteryTimeBase, paramParcel);
      mPhoneOnTimer = new StopwatchTimer(mClocks, null, -3, null, mOnBatteryTimeBase, paramParcel);
      for (i = 0; i < 6; i++) {
        mPhoneSignalStrengthsTimer[i] = new StopwatchTimer(mClocks, null, 65336 - i, null, mOnBatteryTimeBase, paramParcel);
      }
      mPhoneSignalScanningTimer = new StopwatchTimer(mClocks, null, 65337, null, mOnBatteryTimeBase, paramParcel);
      for (i = 0; i < 21; i++) {
        mPhoneDataConnectionsTimer[i] = new StopwatchTimer(mClocks, null, 65236 - i, null, mOnBatteryTimeBase, paramParcel);
      }
      for (i = 0; i < 10; i++)
      {
        mNetworkByteActivityCounters[i] = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
        mNetworkPacketActivityCounters[i] = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
      }
      mMobileRadioPowerState = 1;
      mMobileRadioActiveTimer = new StopwatchTimer(mClocks, null, 65136, null, mOnBatteryTimeBase, paramParcel);
      mMobileRadioActivePerAppTimer = new StopwatchTimer(mClocks, null, 65135, null, mOnBatteryTimeBase, paramParcel);
      mMobileRadioActiveAdjustedTime = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
      mMobileRadioActiveUnknownTime = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
      mMobileRadioActiveUnknownCount = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
      mWifiMulticastWakelockTimer = new StopwatchTimer(mClocks, null, -4, null, mOnBatteryTimeBase, paramParcel);
      mWifiRadioPowerState = 1;
      mWifiOn = false;
      mWifiOnTimer = new StopwatchTimer(mClocks, null, -4, null, mOnBatteryTimeBase, paramParcel);
      mGlobalWifiRunning = false;
      mGlobalWifiRunningTimer = new StopwatchTimer(mClocks, null, -5, null, mOnBatteryTimeBase, paramParcel);
      for (i = 0; i < 8; i++) {
        mWifiStateTimer[i] = new StopwatchTimer(mClocks, null, 64936 - i, null, mOnBatteryTimeBase, paramParcel);
      }
      for (i = 0; i < 13; i++) {
        mWifiSupplStateTimer[i] = new StopwatchTimer(mClocks, null, 64836 - i, null, mOnBatteryTimeBase, paramParcel);
      }
      for (i = 0; i < 5; i++) {
        mWifiSignalStrengthsTimer[i] = new StopwatchTimer(mClocks, null, 64736 - i, null, mOnBatteryTimeBase, paramParcel);
      }
      mWifiActiveTimer = new StopwatchTimer(mClocks, null, 64636, null, mOnBatteryTimeBase, paramParcel);
      mWifiActivity = new ControllerActivityCounterImpl(mOnBatteryTimeBase, 1, paramParcel);
      for (i = 0; i < 2; i++) {
        mGpsSignalQualityTimer[i] = new StopwatchTimer(mClocks, null, 64536 - i, null, mOnBatteryTimeBase, paramParcel);
      }
      mBluetoothActivity = new ControllerActivityCounterImpl(mOnBatteryTimeBase, 1, paramParcel);
      mModemActivity = new ControllerActivityCounterImpl(mOnBatteryTimeBase, 5, paramParcel);
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mHasWifiReporting = bool2;
      if (paramParcel.readInt() != 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      mHasBluetoothReporting = bool2;
      if (paramParcel.readInt() != 0) {
        bool2 = bool1;
      } else {
        bool2 = false;
      }
      mHasModemReporting = bool2;
      mNumConnectivityChange = paramParcel.readInt();
      mLoadedNumConnectivityChange = paramParcel.readInt();
      mUnpluggedNumConnectivityChange = paramParcel.readInt();
      mAudioOnNesting = 0;
      mAudioOnTimer = new StopwatchTimer(mClocks, null, -7, null, mOnBatteryTimeBase);
      mVideoOnNesting = 0;
      mVideoOnTimer = new StopwatchTimer(mClocks, null, -8, null, mOnBatteryTimeBase);
      mFlashlightOnNesting = 0;
      mFlashlightOnTimer = new StopwatchTimer(mClocks, null, -9, null, mOnBatteryTimeBase, paramParcel);
      mCameraOnNesting = 0;
      mCameraOnTimer = new StopwatchTimer(mClocks, null, -13, null, mOnBatteryTimeBase, paramParcel);
      mBluetoothScanNesting = 0;
      mBluetoothScanTimer = new StopwatchTimer(mClocks, null, -14, null, mOnBatteryTimeBase, paramParcel);
      mIsCellularTxPowerHigh = false;
      mDischargeUnplugLevel = paramParcel.readInt();
      mDischargePlugLevel = paramParcel.readInt();
      mDischargeCurrentLevel = paramParcel.readInt();
      mCurrentBatteryLevel = paramParcel.readInt();
      mLowDischargeAmountSinceCharge = paramParcel.readInt();
      mHighDischargeAmountSinceCharge = paramParcel.readInt();
      mDischargeAmountScreenOn = paramParcel.readInt();
      mDischargeAmountScreenOnSinceCharge = paramParcel.readInt();
      mDischargeAmountScreenOff = paramParcel.readInt();
      mDischargeAmountScreenOffSinceCharge = paramParcel.readInt();
      mDischargeAmountScreenDoze = paramParcel.readInt();
      mDischargeAmountScreenDozeSinceCharge = paramParcel.readInt();
      mDischargeStepTracker.readFromParcel(paramParcel);
      mChargeStepTracker.readFromParcel(paramParcel);
      mDischargeCounter = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
      mDischargeScreenOffCounter = new LongSamplingCounter(mOnBatteryScreenOffTimeBase, paramParcel);
      mDischargeScreenDozeCounter = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
      mDischargeLightDozeCounter = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
      mDischargeDeepDozeCounter = new LongSamplingCounter(mOnBatteryTimeBase, paramParcel);
      mLastWriteTime = paramParcel.readLong();
      mRpmStats.clear();
      int k = paramParcel.readInt();
      Object localObject1;
      Object localObject2;
      for (i = 0; i < k; i++) {
        if (paramParcel.readInt() != 0)
        {
          localObject1 = paramParcel.readString();
          localObject2 = new SamplingTimer(mClocks, mOnBatteryTimeBase, paramParcel);
          mRpmStats.put(localObject1, localObject2);
        }
      }
      mScreenOffRpmStats.clear();
      k = paramParcel.readInt();
      for (i = 0; i < k; i++) {
        if (paramParcel.readInt() != 0)
        {
          localObject1 = paramParcel.readString();
          localObject2 = new SamplingTimer(mClocks, mOnBatteryScreenOffTimeBase, paramParcel);
          mScreenOffRpmStats.put(localObject1, localObject2);
        }
      }
      mKernelWakelockStats.clear();
      k = paramParcel.readInt();
      for (i = 0; i < k; i++) {
        if (paramParcel.readInt() != 0)
        {
          localObject2 = paramParcel.readString();
          localObject1 = new SamplingTimer(mClocks, mOnBatteryScreenOffTimeBase, paramParcel);
          mKernelWakelockStats.put(localObject2, localObject1);
        }
      }
      mWakeupReasonStats.clear();
      k = paramParcel.readInt();
      for (i = 0; i < k; i++) {
        if (paramParcel.readInt() != 0)
        {
          localObject2 = paramParcel.readString();
          localObject1 = new SamplingTimer(mClocks, mOnBatteryTimeBase, paramParcel);
          mWakeupReasonStats.put(localObject2, localObject1);
        }
      }
      mKernelMemoryStats.clear();
      k = paramParcel.readInt();
      for (i = 0; i < k; i++) {
        if (paramParcel.readInt() != 0)
        {
          long l = paramParcel.readLong();
          localObject2 = new SamplingTimer(mClocks, mOnBatteryTimeBase, paramParcel);
          mKernelMemoryStats.put(Long.valueOf(l).longValue(), localObject2);
        }
      }
      mPartialTimers.clear();
      mFullTimers.clear();
      mWindowTimers.clear();
      mWifiRunningTimers.clear();
      mFullWifiLockTimers.clear();
      mWifiScanTimers.clear();
      mWifiBatchedScanTimers.clear();
      mWifiMulticastTimers.clear();
      mAudioTurnedOnTimers.clear();
      mVideoTurnedOnTimers.clear();
      mFlashlightTurnedOnTimers.clear();
      mCameraTurnedOnTimers.clear();
      k = paramParcel.readInt();
      mUidStats.clear();
      for (i = j; i < k; i++)
      {
        j = paramParcel.readInt();
        localObject2 = new Uid(this, j);
        ((Uid)localObject2).readFromParcelLocked(mOnBatteryTimeBase, mOnBatteryScreenOffTimeBase, paramParcel);
        mUidStats.append(j, localObject2);
      }
      return;
    }
    paramParcel = new StringBuilder();
    paramParcel.append("Bad magic number: #");
    paramParcel.append(Integer.toHexString(i));
    throw new ParcelFormatException(paramParcel.toString());
  }
  
  void readHistory(Parcel paramParcel, boolean paramBoolean)
    throws ParcelFormatException
  {
    long l = paramParcel.readLong();
    Object localObject = mHistoryBuffer;
    int i = 0;
    ((Parcel)localObject).setDataSize(0);
    mHistoryBuffer.setDataPosition(0);
    mHistoryTagPool.clear();
    mNextHistoryTagIdx = 0;
    mNumHistoryTagChars = 0;
    int j = paramParcel.readInt();
    while (i < j)
    {
      int k = paramParcel.readInt();
      String str = paramParcel.readString();
      if (str != null)
      {
        int m = paramParcel.readInt();
        localObject = new BatteryStats.HistoryTag();
        string = str;
        uid = m;
        poolIdx = k;
        mHistoryTagPool.put(localObject, Integer.valueOf(k));
        if (k >= mNextHistoryTagIdx) {
          mNextHistoryTagIdx = (k + 1);
        }
        mNumHistoryTagChars += string.length() + 1;
        i++;
      }
      else
      {
        throw new ParcelFormatException("null history tag string");
      }
    }
    j = paramParcel.readInt();
    i = paramParcel.dataPosition();
    if (j < MAX_MAX_HISTORY_BUFFER * 3)
    {
      if ((j & 0xFFFFFFFC) == j)
      {
        mHistoryBuffer.appendFrom(paramParcel, i, j);
        paramParcel.setDataPosition(i + j);
        if (paramBoolean) {
          readOldHistory(paramParcel);
        }
        mHistoryBaseTime = l;
        if (mHistoryBaseTime > 0L)
        {
          l = mClocks.elapsedRealtime();
          mHistoryBaseTime = (mHistoryBaseTime - l + 1L);
        }
        return;
      }
      paramParcel = new StringBuilder();
      paramParcel.append("File corrupt: history data buffer not aligned ");
      paramParcel.append(j);
      throw new ParcelFormatException(paramParcel.toString());
    }
    paramParcel = new StringBuilder();
    paramParcel.append("File corrupt: history data buffer too large ");
    paramParcel.append(j);
    throw new ParcelFormatException(paramParcel.toString());
  }
  
  public void readHistoryDelta(Parcel paramParcel, BatteryStats.HistoryItem paramHistoryItem)
  {
    int i = paramParcel.readInt();
    int j = 0x7FFFF & i;
    cmd = ((byte)0);
    numReadInts = 1;
    if (j < 524285)
    {
      time += j;
    }
    else
    {
      if (j == 524285)
      {
        time = paramParcel.readLong();
        numReadInts += 2;
        paramHistoryItem.readFromParcel(paramParcel);
        return;
      }
      if (j == 524286)
      {
        j = paramParcel.readInt();
        time += j;
        numReadInts += 1;
      }
      else
      {
        long l = paramParcel.readLong();
        time += l;
        numReadInts += 2;
      }
    }
    if ((0x80000 & i) != 0)
    {
      j = paramParcel.readInt();
      readBatteryLevelInt(j, paramHistoryItem);
      numReadInts += 1;
    }
    else
    {
      j = 0;
    }
    int k;
    if ((0x100000 & i) != 0)
    {
      k = paramParcel.readInt();
      states = (0xFFFFFF & k | 0xFE000000 & i);
      batteryStatus = ((byte)(byte)(k >> 29 & 0x7));
      batteryHealth = ((byte)(byte)(k >> 26 & 0x7));
      batteryPlugType = ((byte)(byte)(k >> 24 & 0x3));
      switch (batteryPlugType)
      {
      default: 
        break;
      case 3: 
        batteryPlugType = ((byte)4);
        break;
      case 2: 
        batteryPlugType = ((byte)2);
        break;
      case 1: 
        batteryPlugType = ((byte)1);
      }
      numReadInts += 1;
    }
    else
    {
      states = (i & 0xFE000000 | states & 0xFFFFFF);
    }
    if ((0x200000 & i) != 0) {
      states2 = paramParcel.readInt();
    }
    if ((0x400000 & i) != 0)
    {
      int m = paramParcel.readInt();
      k = m & 0xFFFF;
      m = m >> 16 & 0xFFFF;
      if (k != 65535)
      {
        wakelockTag = localWakelockTag;
        readHistoryTag(k, wakelockTag);
      }
      else
      {
        wakelockTag = null;
      }
      if (m != 65535)
      {
        wakeReasonTag = localWakeReasonTag;
        readHistoryTag(m, wakeReasonTag);
      }
      else
      {
        wakeReasonTag = null;
      }
      numReadInts += 1;
    }
    else
    {
      wakelockTag = null;
      wakeReasonTag = null;
    }
    if ((0x800000 & i) != 0)
    {
      eventTag = localEventTag;
      k = paramParcel.readInt();
      eventCode = (k & 0xFFFF);
      readHistoryTag(k >> 16 & 0xFFFF, eventTag);
      numReadInts += 1;
    }
    else
    {
      eventCode = 0;
    }
    if ((j & 0x1) != 0)
    {
      stepDetails = mReadHistoryStepDetails;
      stepDetails.readFromParcel(paramParcel);
    }
    else
    {
      stepDetails = null;
    }
    if ((0x1000000 & i) != 0) {
      batteryChargeUAh = paramParcel.readInt();
    }
  }
  
  @VisibleForTesting
  public void readKernelUidCpuActiveTimesLocked(boolean paramBoolean)
  {
    long l = mClocks.uptimeMillis();
    mKernelUidCpuActiveTimeReader.readDelta(new _..Lambda.BatteryStatsImpl.mMCK0IbpOZu45KINuNCoRayjoDU(this, paramBoolean));
    l = mClocks.uptimeMillis() - l;
    if (l >= 100L)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Reading cpu active times took ");
      localStringBuilder.append(l);
      localStringBuilder.append("ms");
      Slog.d("BatteryStatsImpl", localStringBuilder.toString());
    }
  }
  
  @VisibleForTesting
  public void readKernelUidCpuClusterTimesLocked(boolean paramBoolean)
  {
    long l = mClocks.uptimeMillis();
    mKernelUidCpuClusterTimeReader.readDelta(new _..Lambda.BatteryStatsImpl.WJBQdQHGlhcwV7yfM8vNEWWvVp0(this, paramBoolean));
    l = mClocks.uptimeMillis() - l;
    if (l >= 100L)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Reading cpu cluster times took ");
      localStringBuilder.append(l);
      localStringBuilder.append("ms");
      Slog.d("BatteryStatsImpl", localStringBuilder.toString());
    }
  }
  
  @VisibleForTesting
  public void readKernelUidCpuFreqTimesLocked(ArrayList<StopwatchTimer> paramArrayList, boolean paramBoolean1, boolean paramBoolean2)
  {
    boolean bool1 = mKernelUidCpuFreqTimeReader.perClusterTimesAvailable();
    int i;
    if (paramArrayList == null) {
      i = 0;
    } else {
      i = paramArrayList.size();
    }
    int j = mPowerProfile.getNumCpuClusters();
    mWakeLockAllocationsUs = null;
    long l1 = mClocks.uptimeMillis();
    mKernelUidCpuFreqTimeReader.readDelta(new _..Lambda.BatteryStatsImpl.qYIdEyLMO9XI4FHBl_g5LWknDZQ(this, paramBoolean1, paramBoolean2, bool1, j, i));
    l1 = mClocks.uptimeMillis() - l1;
    Object localObject;
    if (l1 >= 100L)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Reading cpu freq times took ");
      ((StringBuilder)localObject).append(l1);
      ((StringBuilder)localObject).append("ms");
      Slog.d("BatteryStatsImpl", ((StringBuilder)localObject).toString());
    }
    long l2 = l1;
    boolean bool2 = bool1;
    if (mWakeLockAllocationsUs != null)
    {
      int k = 0;
      paramBoolean2 = bool1;
      for (;;)
      {
        l2 = l1;
        bool2 = paramBoolean2;
        if (k >= i) {
          break;
        }
        localObject = getmUid;
        if ((mCpuClusterSpeedTimesUs == null) || (mCpuClusterSpeedTimesUs.length != j)) {
          mCpuClusterSpeedTimesUs = new LongSamplingCounter[j][];
        }
        for (int m = 0; m < j; m++)
        {
          int n = mPowerProfile.getNumSpeedStepsInCpuCluster(m);
          if ((mCpuClusterSpeedTimesUs[m] == null) || (mCpuClusterSpeedTimesUs[m].length != n)) {
            mCpuClusterSpeedTimesUs[m] = new LongSamplingCounter[n];
          }
          LongSamplingCounter[] arrayOfLongSamplingCounter = mCpuClusterSpeedTimesUs[m];
          for (int i1 = 0; i1 < n; i1++)
          {
            if (arrayOfLongSamplingCounter[i1] == null) {
              arrayOfLongSamplingCounter[i1] = new LongSamplingCounter(mOnBatteryTimeBase);
            }
            l2 = mWakeLockAllocationsUs[m][i1] / (i - k);
            arrayOfLongSamplingCounter[i1].addCountLocked(l2, paramBoolean1);
            long[] arrayOfLong = mWakeLockAllocationsUs[m];
            arrayOfLong[i1] -= l2;
          }
        }
        k++;
      }
    }
  }
  
  @VisibleForTesting
  public void readKernelUidCpuTimesLocked(ArrayList<StopwatchTimer> paramArrayList, SparseLongArray paramSparseLongArray, boolean paramBoolean)
  {
    mTempTotalCpuSystemTimeUs = 0L;
    mTempTotalCpuUserTimeUs = 0L;
    int i;
    if (paramArrayList == null) {
      i = 0;
    } else {
      i = paramArrayList.size();
    }
    long l1 = mClocks.uptimeMillis();
    mKernelUidCpuTimeReader.readDelta(new _..Lambda.BatteryStatsImpl.cVkGM5pv4uMLFgnMwqPRDhEl5a0(this, i, paramBoolean, paramSparseLongArray));
    long l2 = mClocks.uptimeMillis() - l1;
    Object localObject;
    if (l2 >= 100L)
    {
      localObject = new StringBuilder();
      ((StringBuilder)localObject).append("Reading cpu stats took ");
      ((StringBuilder)localObject).append(l2);
      ((StringBuilder)localObject).append("ms");
      Slog.d("BatteryStatsImpl", ((StringBuilder)localObject).toString());
    }
    int j = i;
    l2 = l1;
    if (i > 0)
    {
      mTempTotalCpuUserTimeUs = (mTempTotalCpuUserTimeUs * 50L / 100L);
      mTempTotalCpuSystemTimeUs = (mTempTotalCpuSystemTimeUs * 50L / 100L);
      for (int k = 0;; k++)
      {
        j = i;
        l2 = l1;
        if (k >= i) {
          break;
        }
        localObject = (StopwatchTimer)paramArrayList.get(k);
        int m = (int)(mTempTotalCpuUserTimeUs / (i - k));
        int n = (int)(mTempTotalCpuSystemTimeUs / (i - k));
        mUid.mUserCpuTime.addCountLocked(m, paramBoolean);
        mUid.mSystemCpuTime.addCountLocked(n, paramBoolean);
        if (paramSparseLongArray != null)
        {
          j = mUid.getUid();
          paramSparseLongArray.put(j, paramSparseLongArray.get(j, 0L) + m + n);
        }
        mUid.getProcessStatsLocked("*wakelock*").addCpuTimeLocked(m / 1000, n / 1000, paramBoolean);
        mTempTotalCpuUserTimeUs -= m;
        mTempTotalCpuSystemTimeUs -= n;
      }
    }
  }
  
  public void readLocked()
  {
    if (mDailyFile != null) {
      readDailyStatsLocked();
    }
    if (mFile == null)
    {
      Slog.w("BatteryStats", "readLocked: no file associated with this instance");
      return;
    }
    mUidStats.clear();
    try
    {
      Object localObject = mFile.chooseForRead();
      if (!((File)localObject).exists()) {
        return;
      }
      FileInputStream localFileInputStream = new java/io/FileInputStream;
      localFileInputStream.<init>((File)localObject);
      byte[] arrayOfByte = BatteryStatsHelper.readFully(localFileInputStream);
      localObject = Parcel.obtain();
      ((Parcel)localObject).unmarshall(arrayOfByte, 0, arrayOfByte.length);
      ((Parcel)localObject).setDataPosition(0);
      localFileInputStream.close();
      readSummaryFromParcel((Parcel)localObject);
    }
    catch (Exception localException)
    {
      Slog.e("BatteryStats", "Error reading battery statistics", localException);
      resetAllStatsLocked();
    }
    mEndPlatformVersion = Build.ID;
    if (mHistoryBuffer.dataPosition() > 0)
    {
      mRecordingHistory = true;
      long l1 = mClocks.elapsedRealtime();
      long l2 = mClocks.uptimeMillis();
      addHistoryBufferLocked(l1, (byte)4, mHistoryCur);
      startRecordingHistory(l1, l2, false);
    }
    recordDailyStatsIfNeededLocked(false);
  }
  
  void readOldHistory(Parcel paramParcel) {}
  
  public void readSummaryFromParcel(Parcel paramParcel)
    throws ParcelFormatException
  {
    int i = paramParcel.readInt();
    if (i != 177)
    {
      paramParcel = new StringBuilder();
      paramParcel.append("readFromParcel: version got ");
      paramParcel.append(i);
      paramParcel.append(", expected ");
      paramParcel.append(177);
      paramParcel.append("; erasing old stats");
      Slog.w("BatteryStats", paramParcel.toString());
      return;
    }
    readHistory(paramParcel, true);
    mStartCount = paramParcel.readInt();
    mUptime = paramParcel.readLong();
    mRealtime = paramParcel.readLong();
    mStartClockTime = paramParcel.readLong();
    mStartPlatformVersion = paramParcel.readString();
    mEndPlatformVersion = paramParcel.readString();
    mOnBatteryTimeBase.readSummaryFromParcel(paramParcel);
    mOnBatteryScreenOffTimeBase.readSummaryFromParcel(paramParcel);
    mDischargeUnplugLevel = paramParcel.readInt();
    mDischargePlugLevel = paramParcel.readInt();
    mDischargeCurrentLevel = paramParcel.readInt();
    mCurrentBatteryLevel = paramParcel.readInt();
    mEstimatedBatteryCapacity = paramParcel.readInt();
    mMinLearnedBatteryCapacity = paramParcel.readInt();
    mMaxLearnedBatteryCapacity = paramParcel.readInt();
    mLowDischargeAmountSinceCharge = paramParcel.readInt();
    mHighDischargeAmountSinceCharge = paramParcel.readInt();
    mDischargeAmountScreenOnSinceCharge = paramParcel.readInt();
    mDischargeAmountScreenOffSinceCharge = paramParcel.readInt();
    mDischargeAmountScreenDozeSinceCharge = paramParcel.readInt();
    mDischargeStepTracker.readFromParcel(paramParcel);
    mChargeStepTracker.readFromParcel(paramParcel);
    mDailyDischargeStepTracker.readFromParcel(paramParcel);
    mDailyChargeStepTracker.readFromParcel(paramParcel);
    mDischargeCounter.readSummaryFromParcelLocked(paramParcel);
    mDischargeScreenOffCounter.readSummaryFromParcelLocked(paramParcel);
    mDischargeScreenDozeCounter.readSummaryFromParcelLocked(paramParcel);
    mDischargeLightDozeCounter.readSummaryFromParcelLocked(paramParcel);
    mDischargeDeepDozeCounter.readSummaryFromParcelLocked(paramParcel);
    int j = paramParcel.readInt();
    Object localObject1;
    boolean bool;
    if (j > 0)
    {
      mDailyPackageChanges = new ArrayList(j);
      for (;;)
      {
        k = j;
        if (j <= 0) {
          break;
        }
        j--;
        localObject1 = new BatteryStats.PackageChange();
        mPackageName = paramParcel.readString();
        if (paramParcel.readInt() != 0) {
          bool = true;
        } else {
          bool = false;
        }
        mUpdate = bool;
        mVersionCode = paramParcel.readLong();
        mDailyPackageChanges.add(localObject1);
      }
    }
    mDailyPackageChanges = null;
    int k = j;
    mDailyStartTime = paramParcel.readLong();
    mNextMinDailyDeadline = paramParcel.readLong();
    mNextMaxDailyDeadline = paramParcel.readLong();
    mStartCount += 1;
    mScreenState = 0;
    mScreenOnTimer.readSummaryFromParcelLocked(paramParcel);
    mScreenDozeTimer.readSummaryFromParcelLocked(paramParcel);
    for (j = 0; j < 5; j++) {
      mScreenBrightnessTimer[j].readSummaryFromParcelLocked(paramParcel);
    }
    mInteractive = false;
    mInteractiveTimer.readSummaryFromParcelLocked(paramParcel);
    mPhoneOn = false;
    mPowerSaveModeEnabledTimer.readSummaryFromParcelLocked(paramParcel);
    mLongestLightIdleTime = paramParcel.readLong();
    mLongestFullIdleTime = paramParcel.readLong();
    mDeviceIdleModeLightTimer.readSummaryFromParcelLocked(paramParcel);
    mDeviceIdleModeFullTimer.readSummaryFromParcelLocked(paramParcel);
    mDeviceLightIdlingTimer.readSummaryFromParcelLocked(paramParcel);
    mDeviceIdlingTimer.readSummaryFromParcelLocked(paramParcel);
    mPhoneOnTimer.readSummaryFromParcelLocked(paramParcel);
    for (j = 0; j < 6; j++) {
      mPhoneSignalStrengthsTimer[j].readSummaryFromParcelLocked(paramParcel);
    }
    mPhoneSignalScanningTimer.readSummaryFromParcelLocked(paramParcel);
    for (j = 0; j < 21; j++) {
      mPhoneDataConnectionsTimer[j].readSummaryFromParcelLocked(paramParcel);
    }
    for (j = 0; j < 10; j++)
    {
      mNetworkByteActivityCounters[j].readSummaryFromParcelLocked(paramParcel);
      mNetworkPacketActivityCounters[j].readSummaryFromParcelLocked(paramParcel);
    }
    mMobileRadioPowerState = 1;
    mMobileRadioActiveTimer.readSummaryFromParcelLocked(paramParcel);
    mMobileRadioActivePerAppTimer.readSummaryFromParcelLocked(paramParcel);
    mMobileRadioActiveAdjustedTime.readSummaryFromParcelLocked(paramParcel);
    mMobileRadioActiveUnknownTime.readSummaryFromParcelLocked(paramParcel);
    mMobileRadioActiveUnknownCount.readSummaryFromParcelLocked(paramParcel);
    mWifiMulticastWakelockTimer.readSummaryFromParcelLocked(paramParcel);
    mWifiRadioPowerState = 1;
    mWifiOn = false;
    mWifiOnTimer.readSummaryFromParcelLocked(paramParcel);
    mGlobalWifiRunning = false;
    mGlobalWifiRunningTimer.readSummaryFromParcelLocked(paramParcel);
    for (j = 0; j < 8; j++) {
      mWifiStateTimer[j].readSummaryFromParcelLocked(paramParcel);
    }
    for (j = 0; j < 13; j++) {
      mWifiSupplStateTimer[j].readSummaryFromParcelLocked(paramParcel);
    }
    for (j = 0; j < 5; j++) {
      mWifiSignalStrengthsTimer[j].readSummaryFromParcelLocked(paramParcel);
    }
    mWifiActiveTimer.readSummaryFromParcelLocked(paramParcel);
    mWifiActivity.readSummaryFromParcel(paramParcel);
    for (j = 0; j < 2; j++) {
      mGpsSignalQualityTimer[j].readSummaryFromParcelLocked(paramParcel);
    }
    mBluetoothActivity.readSummaryFromParcel(paramParcel);
    mModemActivity.readSummaryFromParcel(paramParcel);
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mHasWifiReporting = bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mHasBluetoothReporting = bool;
    if (paramParcel.readInt() != 0) {
      bool = true;
    } else {
      bool = false;
    }
    mHasModemReporting = bool;
    j = paramParcel.readInt();
    mLoadedNumConnectivityChange = j;
    mNumConnectivityChange = j;
    mFlashlightOnNesting = 0;
    mFlashlightOnTimer.readSummaryFromParcelLocked(paramParcel);
    mCameraOnNesting = 0;
    mCameraOnTimer.readSummaryFromParcelLocked(paramParcel);
    mBluetoothScanNesting = 0;
    mBluetoothScanTimer.readSummaryFromParcelLocked(paramParcel);
    mIsCellularTxPowerHigh = false;
    int m = paramParcel.readInt();
    if (m <= 10000)
    {
      for (j = 0; j < m; j++) {
        if (paramParcel.readInt() != 0) {
          getRpmTimerLocked(paramParcel.readString()).readSummaryFromParcelLocked(paramParcel);
        }
      }
      int n = paramParcel.readInt();
      if (n <= 10000)
      {
        for (j = 0; j < n; j++) {
          if (paramParcel.readInt() != 0) {
            getScreenOffRpmTimerLocked(paramParcel.readString()).readSummaryFromParcelLocked(paramParcel);
          }
        }
        int i1 = paramParcel.readInt();
        if (i1 <= 10000)
        {
          for (j = 0; j < i1; j++) {
            if (paramParcel.readInt() != 0) {
              getKernelWakelockTimerLocked(paramParcel.readString()).readSummaryFromParcelLocked(paramParcel);
            }
          }
          int i2 = paramParcel.readInt();
          if (i2 <= 10000)
          {
            for (j = 0; j < i2; j++) {
              if (paramParcel.readInt() != 0) {
                getWakeupReasonTimerLocked(paramParcel.readString()).readSummaryFromParcelLocked(paramParcel);
              }
            }
            int i3 = paramParcel.readInt();
            for (j = 0; j < i3; j++) {
              if (paramParcel.readInt() != 0) {
                getKernelMemoryTimerLocked(paramParcel.readLong()).readSummaryFromParcelLocked(paramParcel);
              }
            }
            int i4 = paramParcel.readInt();
            if (i4 <= 10000)
            {
              int i5 = 0;
              j = i;
              int i6;
              int i7;
              int i8;
              int i9;
              int i10;
              for (;;)
              {
                bool = false;
                if (i5 >= i4) {
                  break label3281;
                }
                i6 = paramParcel.readInt();
                localObject1 = new Uid(this, i6);
                mUidStats.put(i6, localObject1);
                mOnBatteryBackgroundTimeBase.readSummaryFromParcel(paramParcel);
                mOnBatteryScreenOffBackgroundTimeBase.readSummaryFromParcel(paramParcel);
                mWifiRunning = false;
                if (paramParcel.readInt() != 0) {
                  mWifiRunningTimer.readSummaryFromParcelLocked(paramParcel);
                }
                mFullWifiLockOut = false;
                if (paramParcel.readInt() != 0) {
                  mFullWifiLockTimer.readSummaryFromParcelLocked(paramParcel);
                }
                mWifiScanStarted = false;
                if (paramParcel.readInt() != 0) {
                  mWifiScanTimer.readSummaryFromParcelLocked(paramParcel);
                }
                mWifiBatchedScanBinStarted = -1;
                for (i = 0; i < 5; i++) {
                  if (paramParcel.readInt() != 0)
                  {
                    ((Uid)localObject1).makeWifiBatchedScanBin(i, null);
                    mWifiBatchedScanTimer[i].readSummaryFromParcelLocked(paramParcel);
                  }
                }
                mWifiMulticastEnabled = false;
                if (paramParcel.readInt() != 0) {
                  mWifiMulticastTimer.readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createAudioTurnedOnTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createVideoTurnedOnTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createFlashlightTurnedOnTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createCameraTurnedOnTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createForegroundActivityTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createForegroundServiceTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createAggregatedPartialWakelockTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createBluetoothScanTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createBluetoothUnoptimizedScanTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createBluetoothScanResultCounterLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createBluetoothScanResultBgCounterLocked().readSummaryFromParcelLocked(paramParcel);
                }
                mProcessState = 19;
                for (i = 0; i < 7; i++) {
                  if (paramParcel.readInt() != 0)
                  {
                    ((Uid)localObject1).makeProcessState(i, null);
                    mProcessStateTimer[i].readSummaryFromParcelLocked(paramParcel);
                  }
                }
                if (paramParcel.readInt() != 0) {
                  ((Uid)localObject1).createVibratorOnTimerLocked().readSummaryFromParcelLocked(paramParcel);
                }
                if (paramParcel.readInt() != 0)
                {
                  if (mUserActivityCounters == null) {
                    ((Uid)localObject1).initUserActivityLocked();
                  }
                  for (i = 0; i < 4; i++) {
                    mUserActivityCounters[i].readSummaryFromParcelLocked(paramParcel);
                  }
                }
                if (paramParcel.readInt() != 0)
                {
                  if (mNetworkByteActivityCounters == null) {
                    ((Uid)localObject1).initNetworkActivityLocked();
                  }
                  for (i = 0; i < 10; i++)
                  {
                    mNetworkByteActivityCounters[i].readSummaryFromParcelLocked(paramParcel);
                    mNetworkPacketActivityCounters[i].readSummaryFromParcelLocked(paramParcel);
                  }
                  mMobileRadioActiveTime.readSummaryFromParcelLocked(paramParcel);
                  mMobileRadioActiveCount.readSummaryFromParcelLocked(paramParcel);
                }
                mUserCpuTime.readSummaryFromParcelLocked(paramParcel);
                mSystemCpuTime.readSummaryFromParcelLocked(paramParcel);
                if (paramParcel.readInt() != 0)
                {
                  i7 = paramParcel.readInt();
                  if ((mPowerProfile != null) && (mPowerProfile.getNumCpuClusters() != i7)) {
                    throw new ParcelFormatException("Incompatible cpu cluster arrangement");
                  }
                  mCpuClusterSpeedTimesUs = new LongSamplingCounter[i7][];
                  i = 0;
                  while (i < i7)
                  {
                    if (paramParcel.readInt() != 0)
                    {
                      i8 = paramParcel.readInt();
                      if ((mPowerProfile != null) && (mPowerProfile.getNumSpeedStepsInCpuCluster(i) != i8))
                      {
                        paramParcel = new StringBuilder();
                        paramParcel.append("File corrupt: too many speed bins ");
                        paramParcel.append(i8);
                        throw new ParcelFormatException(paramParcel.toString());
                      }
                      mCpuClusterSpeedTimesUs[i] = new LongSamplingCounter[i8];
                      for (i9 = 0; i9 < i8; i9++) {
                        if (paramParcel.readInt() != 0)
                        {
                          mCpuClusterSpeedTimesUs[i][i9] = new LongSamplingCounter(mOnBatteryTimeBase);
                          mCpuClusterSpeedTimesUs[i][i9].readSummaryFromParcelLocked(paramParcel);
                        }
                      }
                    }
                    else
                    {
                      mCpuClusterSpeedTimesUs[i] = null;
                    }
                    i++;
                    bool = false;
                  }
                  i = k;
                  k = j;
                  j = i;
                }
                else
                {
                  i = j;
                  j = k;
                  mCpuClusterSpeedTimesUs = null;
                  k = i;
                }
                mCpuFreqTimeMs = LongSamplingCounterArray.readSummaryFromParcelLocked(paramParcel, mOnBatteryTimeBase);
                mScreenOffCpuFreqTimeMs = LongSamplingCounterArray.readSummaryFromParcelLocked(paramParcel, mOnBatteryScreenOffTimeBase);
                mCpuActiveTimeMs.readSummaryFromParcelLocked(paramParcel);
                mCpuClusterTimesMs.readSummaryFromParcelLocked(paramParcel);
                i8 = paramParcel.readInt();
                if (i8 == 7)
                {
                  mProcStateTimeMs = new LongSamplingCounterArray[i8];
                  for (i = 0; i < i8; i++) {
                    mProcStateTimeMs[i] = LongSamplingCounterArray.readSummaryFromParcelLocked(paramParcel, mOnBatteryTimeBase);
                  }
                }
                mProcStateTimeMs = null;
                i8 = paramParcel.readInt();
                if (i8 == 7)
                {
                  mProcStateScreenOffTimeMs = new LongSamplingCounterArray[i8];
                  for (i = 0; i < i8; i++) {
                    mProcStateScreenOffTimeMs[i] = LongSamplingCounterArray.readSummaryFromParcelLocked(paramParcel, mOnBatteryScreenOffTimeBase);
                  }
                }
                mProcStateScreenOffTimeMs = null;
                if (paramParcel.readInt() != 0)
                {
                  Uid.access$1902((Uid)localObject1, new LongSamplingCounter(mOnBatteryTimeBase));
                  mMobileRadioApWakeupCount.readSummaryFromParcelLocked(paramParcel);
                }
                else
                {
                  Uid.access$1902((Uid)localObject1, null);
                }
                if (paramParcel.readInt() != 0)
                {
                  Uid.access$2002((Uid)localObject1, new LongSamplingCounter(mOnBatteryTimeBase));
                  mWifiRadioApWakeupCount.readSummaryFromParcelLocked(paramParcel);
                }
                else
                {
                  Uid.access$2002((Uid)localObject1, null);
                }
                i10 = paramParcel.readInt();
                if (i10 > MAX_WAKELOCKS_PER_UID + 1) {
                  break label3246;
                }
                for (i = 0; i < i10; i++) {
                  ((Uid)localObject1).readWakeSummaryFromParcelLocked(paramParcel.readString(), paramParcel);
                }
                i7 = paramParcel.readInt();
                if (i7 > MAX_WAKELOCKS_PER_UID + 1) {
                  break label3211;
                }
                for (i = 0; i < i7; i++) {
                  ((Uid)localObject1).readSyncSummaryFromParcelLocked(paramParcel.readString(), paramParcel);
                }
                i9 = paramParcel.readInt();
                if (i9 > MAX_WAKELOCKS_PER_UID + 1) {
                  break label3176;
                }
                for (i = 0; i < i9; i++) {
                  ((Uid)localObject1).readJobSummaryFromParcelLocked(paramParcel.readString(), paramParcel);
                }
                ((Uid)localObject1).readJobCompletionsFromParcelLocked(paramParcel);
                mJobsDeferredEventCount.readSummaryFromParcelLocked(paramParcel);
                mJobsDeferredCount.readSummaryFromParcelLocked(paramParcel);
                mJobsFreshnessTimeMs.readSummaryFromParcelLocked(paramParcel);
                i9 = 0;
                i = i6;
                while (i9 < JOB_FRESHNESS_BUCKETS.length)
                {
                  if (paramParcel.readInt() != 0)
                  {
                    mJobsFreshnessBuckets[i9] = new Counter(mBsi.mOnBatteryTimeBase);
                    mJobsFreshnessBuckets[i9].readSummaryFromParcelLocked(paramParcel);
                  }
                  i9++;
                }
                i8 = paramParcel.readInt();
                if (i8 > 1000) {
                  break label3141;
                }
                for (i = 0; i < i8; i++)
                {
                  i9 = paramParcel.readInt();
                  if (paramParcel.readInt() != 0) {
                    ((Uid)localObject1).getSensorTimerLocked(i9, true).readSummaryFromParcelLocked(paramParcel);
                  }
                }
                i9 = paramParcel.readInt();
                if (i9 > 1000) {
                  break label3106;
                }
                i = 0;
                i8 = i10;
                Object localObject2;
                long l;
                while (i < i9)
                {
                  localObject2 = ((Uid)localObject1).getProcessStatsLocked(paramParcel.readString());
                  l = paramParcel.readLong();
                  mLoadedUserTime = l;
                  mUserTime = l;
                  l = paramParcel.readLong();
                  mLoadedSystemTime = l;
                  mSystemTime = l;
                  l = paramParcel.readLong();
                  mLoadedForegroundTime = l;
                  mForegroundTime = l;
                  i6 = paramParcel.readInt();
                  mLoadedStarts = i6;
                  mStarts = i6;
                  i6 = paramParcel.readInt();
                  mLoadedNumCrashes = i6;
                  mNumCrashes = i6;
                  i6 = paramParcel.readInt();
                  mLoadedNumAnrs = i6;
                  mNumAnrs = i6;
                  ((BatteryStatsImpl.Uid.Proc)localObject2).readExcessivePowerFromParcelLocked(paramParcel);
                  i++;
                }
                i6 = paramParcel.readInt();
                if (i6 > 10000) {
                  break;
                }
                i8 = 0;
                i = i2;
                i2 = i7;
                while (i8 < i6)
                {
                  localObject2 = paramParcel.readString();
                  BatteryStatsImpl.Uid.Pkg localPkg = ((Uid)localObject1).getPackageStatsLocked((String)localObject2);
                  i7 = paramParcel.readInt();
                  if (i7 <= 1000)
                  {
                    mWakeupAlarms.clear();
                    Object localObject3;
                    for (i9 = 0; i9 < i7; i9++)
                    {
                      localObject3 = paramParcel.readString();
                      Counter localCounter = new Counter(mOnBatteryScreenOffTimeBase);
                      localCounter.readSummaryFromParcelLocked(paramParcel);
                      mWakeupAlarms.put(localObject3, localCounter);
                    }
                    i9 = paramParcel.readInt();
                    if (i9 <= 1000)
                    {
                      for (i2 = 0; i2 < i9; i2++)
                      {
                        localObject3 = ((Uid)localObject1).getServiceStatsLocked((String)localObject2, paramParcel.readString());
                        l = paramParcel.readLong();
                        mLoadedStartTime = l;
                        mStartTime = l;
                        i7 = paramParcel.readInt();
                        mLoadedStarts = i7;
                        mStarts = i7;
                        i7 = paramParcel.readInt();
                        mLoadedLaunches = i7;
                        mLaunches = i7;
                      }
                      i8++;
                      i2 = i9;
                    }
                    else
                    {
                      paramParcel = new StringBuilder();
                      paramParcel.append("File corrupt: too many services ");
                      paramParcel.append(i9);
                      throw new ParcelFormatException(paramParcel.toString());
                    }
                  }
                  else
                  {
                    paramParcel = new StringBuilder();
                    paramParcel.append("File corrupt: too many wakeup alarms ");
                    paramParcel.append(i7);
                    throw new ParcelFormatException(paramParcel.toString());
                  }
                }
                i5++;
                i2 = j;
                j = k;
                k = i2;
                i2 = i;
              }
              paramParcel = new StringBuilder();
              paramParcel.append("File corrupt: too many packages ");
              paramParcel.append(i6);
              throw new ParcelFormatException(paramParcel.toString());
              label3106:
              paramParcel = new StringBuilder();
              paramParcel.append("File corrupt: too many processes ");
              paramParcel.append(i9);
              throw new ParcelFormatException(paramParcel.toString());
              label3141:
              paramParcel = new StringBuilder();
              paramParcel.append("File corrupt: too many sensors ");
              paramParcel.append(i8);
              throw new ParcelFormatException(paramParcel.toString());
              label3176:
              paramParcel = new StringBuilder();
              paramParcel.append("File corrupt: too many job timers ");
              paramParcel.append(i9);
              throw new ParcelFormatException(paramParcel.toString());
              label3211:
              paramParcel = new StringBuilder();
              paramParcel.append("File corrupt: too many syncs ");
              paramParcel.append(i7);
              throw new ParcelFormatException(paramParcel.toString());
              label3246:
              paramParcel = new StringBuilder();
              paramParcel.append("File corrupt: too many wake locks ");
              paramParcel.append(i10);
              throw new ParcelFormatException(paramParcel.toString());
              label3281:
              return;
            }
            paramParcel = new StringBuilder();
            paramParcel.append("File corrupt: too many uids ");
            paramParcel.append(i4);
            throw new ParcelFormatException(paramParcel.toString());
          }
          paramParcel = new StringBuilder();
          paramParcel.append("File corrupt: too many wakeup reasons ");
          paramParcel.append(i2);
          throw new ParcelFormatException(paramParcel.toString());
        }
        paramParcel = new StringBuilder();
        paramParcel.append("File corrupt: too many kernel wake locks ");
        paramParcel.append(i1);
        throw new ParcelFormatException(paramParcel.toString());
      }
      paramParcel = new StringBuilder();
      paramParcel.append("File corrupt: too many screen-off rpm stats ");
      paramParcel.append(n);
      throw new ParcelFormatException(paramParcel.toString());
    }
    paramParcel = new StringBuilder();
    paramParcel.append("File corrupt: too many rpm stats ");
    paramParcel.append(m);
    throw new ParcelFormatException(paramParcel.toString());
  }
  
  public void recordDailyStatsIfNeededLocked(boolean paramBoolean)
  {
    long l = System.currentTimeMillis();
    if (l >= mNextMaxDailyDeadline) {
      recordDailyStatsLocked();
    } else if ((paramBoolean) && (l >= mNextMinDailyDeadline)) {
      recordDailyStatsLocked();
    } else if (l < mDailyStartTime - 86400000L) {
      recordDailyStatsLocked();
    }
  }
  
  public void recordDailyStatsLocked()
  {
    Object localObject1 = new BatteryStats.DailyItem();
    mStartTime = mDailyStartTime;
    mEndTime = System.currentTimeMillis();
    int i = 0;
    if (mDailyDischargeStepTracker.mNumStepDurations > 0)
    {
      i = 1;
      mDischargeSteps = new BatteryStats.LevelStepTracker(mDailyDischargeStepTracker.mNumStepDurations, mDailyDischargeStepTracker.mStepDurations);
    }
    if (mDailyChargeStepTracker.mNumStepDurations > 0)
    {
      i = 1;
      mChargeSteps = new BatteryStats.LevelStepTracker(mDailyChargeStepTracker.mNumStepDurations, mDailyChargeStepTracker.mStepDurations);
    }
    if (mDailyPackageChanges != null)
    {
      i = 1;
      mPackageChanges = mDailyPackageChanges;
      mDailyPackageChanges = null;
    }
    mDailyDischargeStepTracker.init();
    mDailyChargeStepTracker.init();
    updateDailyDeadlineLocked();
    if (i != 0)
    {
      long l1 = SystemClock.uptimeMillis();
      mDailyItems.add(localObject1);
      while (mDailyItems.size() > 10) {
        mDailyItems.remove(0);
      }
      localObject1 = new ByteArrayOutputStream();
      try
      {
        Object localObject2 = new com/android/internal/util/FastXmlSerializer;
        ((FastXmlSerializer)localObject2).<init>();
        ((XmlSerializer)localObject2).setOutput((OutputStream)localObject1, StandardCharsets.UTF_8.name());
        writeDailyItemsLocked((XmlSerializer)localObject2);
        long l2 = SystemClock.uptimeMillis();
        localObject2 = BackgroundThread.getHandler();
        Runnable local2 = new com/android/internal/os/BatteryStatsImpl$2;
        local2.<init>(this, (ByteArrayOutputStream)localObject1, l2 - l1);
        ((Handler)localObject2).post(local2);
      }
      catch (IOException localIOException) {}
    }
  }
  
  @GuardedBy("this")
  public void removeIsolatedUidLocked(int paramInt)
  {
    StatsLog.write(43, mIsolatedUids.get(paramInt, -1), paramInt, 0);
    int i = mIsolatedUids.indexOfKey(paramInt);
    if (i >= 0)
    {
      getUidStatsLocked(mIsolatedUids.valueAt(i)).removeIsolatedUid(paramInt);
      mIsolatedUids.removeAt(i);
    }
    mPendingRemovedUids.add(new UidToRemove(paramInt, mClocks.elapsedRealtime()));
  }
  
  public void removeUidStatsLocked(int paramInt)
  {
    mUidStats.remove(paramInt);
    mPendingRemovedUids.add(new UidToRemove(paramInt, mClocks.elapsedRealtime()));
  }
  
  public void reportExcessiveCpuLocked(int paramInt, String paramString, long paramLong1, long paramLong2)
  {
    paramInt = mapUid(paramInt);
    Uid localUid = (Uid)mUidStats.get(paramInt);
    if (localUid != null) {
      localUid.reportExcessiveCpuLocked(paramString, paramLong1, paramLong2);
    }
  }
  
  public void resetAllStatsCmdLocked()
  {
    resetAllStatsLocked();
    long l1 = mClocks.uptimeMillis();
    long l2 = l1 * 1000L;
    long l3 = mClocks.elapsedRealtime();
    long l4 = 1000L * l3;
    mDischargeStartLevel = mHistoryCur.batteryLevel;
    pullPendingStateUpdatesLocked();
    addHistoryRecordLocked(l3, l1);
    int i = mHistoryCur.batteryLevel;
    mCurrentBatteryLevel = i;
    mDischargePlugLevel = i;
    mDischargeUnplugLevel = i;
    mDischargeCurrentLevel = i;
    mOnBatteryTimeBase.reset(l2, l4);
    mOnBatteryScreenOffTimeBase.reset(l2, l4);
    if ((mHistoryCur.states & 0x80000) == 0)
    {
      if (isScreenOn(mScreenState))
      {
        mDischargeScreenOnUnplugLevel = mHistoryCur.batteryLevel;
        mDischargeScreenDozeUnplugLevel = 0;
        mDischargeScreenOffUnplugLevel = 0;
      }
      else if (isScreenDoze(mScreenState))
      {
        mDischargeScreenOnUnplugLevel = 0;
        mDischargeScreenDozeUnplugLevel = mHistoryCur.batteryLevel;
        mDischargeScreenOffUnplugLevel = 0;
      }
      else
      {
        mDischargeScreenOnUnplugLevel = 0;
        mDischargeScreenDozeUnplugLevel = 0;
        mDischargeScreenOffUnplugLevel = mHistoryCur.batteryLevel;
      }
      mDischargeAmountScreenOn = 0;
      mDischargeAmountScreenOff = 0;
      mDischargeAmountScreenDoze = 0;
    }
    initActiveHistoryEventsLocked(l3, l1);
  }
  
  public void scheduleRemoveIsolatedUidLocked(int paramInt1, int paramInt2)
  {
    if ((mIsolatedUids.get(paramInt1, -1) == paramInt2) && (mExternalSync != null)) {
      mExternalSync.scheduleCpuSyncDueToRemovedUid(paramInt1);
    }
  }
  
  @GuardedBy("this")
  public void setBatteryStateLocked(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    int i = Math.max(0, paramInt5);
    BatteryStats.HistoryItem localHistoryItem;
    if (mHaveBatteryLevel) {
      localHistoryItem = mHistoryCur;
    } else {
      localHistoryItem = null;
    }
    reportChangesToStatsLog(localHistoryItem, paramInt1, paramInt3, paramInt4);
    boolean bool1 = isOnBattery(paramInt3, paramInt1);
    long l1 = mClocks.uptimeMillis();
    long l2 = mClocks.elapsedRealtime();
    if (!mHaveBatteryLevel)
    {
      mHaveBatteryLevel = true;
      if (bool1 == mOnBattery) {
        if (bool1)
        {
          localHistoryItem = mHistoryCur;
          states &= 0xFFF7FFFF;
        }
        else
        {
          localHistoryItem = mHistoryCur;
          states |= 0x80000;
        }
      }
      localHistoryItem = mHistoryCur;
      states2 |= 0x1000000;
      mHistoryCur.batteryStatus = ((byte)(byte)paramInt1);
      mHistoryCur.batteryLevel = ((byte)(byte)paramInt4);
      mHistoryCur.batteryChargeUAh = paramInt7;
      mLastDischargeStepLevel = paramInt4;
      mLastChargeStepLevel = paramInt4;
      mMinDischargeStepLevel = paramInt4;
      mMaxChargeStepLevel = paramInt4;
      mLastChargingStateLevel = paramInt4;
    }
    else if ((mCurrentBatteryLevel != paramInt4) || (mOnBattery != bool1))
    {
      if ((paramInt4 >= 100) && (bool1)) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      recordDailyStatsIfNeededLocked(bool2);
    }
    paramInt5 = mHistoryCur.batteryStatus;
    if (bool1)
    {
      mDischargeCurrentLevel = paramInt4;
      if (!mRecordingHistory)
      {
        mRecordingHistory = true;
        startRecordingHistory(l2, l1, true);
      }
    }
    else if ((paramInt4 < 96) && (paramInt1 != 1) && (!mRecordingHistory))
    {
      mRecordingHistory = true;
      startRecordingHistory(l2, l1, true);
    }
    boolean bool2 = bool1;
    mCurrentBatteryLevel = paramInt4;
    if (mDischargePlugLevel < 0) {
      mDischargePlugLevel = paramInt4;
    }
    long l3;
    if (bool2 != mOnBattery)
    {
      mHistoryCur.batteryLevel = ((byte)(byte)paramInt4);
      mHistoryCur.batteryStatus = ((byte)(byte)paramInt1);
      mHistoryCur.batteryHealth = ((byte)(byte)paramInt2);
      mHistoryCur.batteryPlugType = ((byte)(byte)paramInt3);
      mHistoryCur.batteryTemperature = ((short)(short)i);
      mHistoryCur.batteryVoltage = ((char)(char)paramInt6);
      if (paramInt7 < mHistoryCur.batteryChargeUAh)
      {
        l3 = mHistoryCur.batteryChargeUAh - paramInt7;
        mDischargeCounter.addCountLocked(l3);
        mDischargeScreenOffCounter.addCountLocked(l3);
        if (isScreenDoze(mScreenState)) {
          mDischargeScreenDozeCounter.addCountLocked(l3);
        }
        if (mDeviceIdleMode == 1) {
          mDischargeLightDozeCounter.addCountLocked(l3);
        } else if (mDeviceIdleMode == 2) {
          mDischargeDeepDozeCounter.addCountLocked(l3);
        }
      }
      mHistoryCur.batteryChargeUAh = paramInt7;
      setOnBatteryLocked(l2, l1, bool2, paramInt5, paramInt4, paramInt7);
    }
    else
    {
      paramInt5 = 0;
      if (mHistoryCur.batteryLevel != paramInt4)
      {
        mHistoryCur.batteryLevel = ((byte)(byte)paramInt4);
        paramInt5 = 1;
        mExternalSync.scheduleSyncDueToBatteryLevelChange(mConstants.BATTERY_LEVEL_COLLECTION_DELAY_MS);
      }
      if (mHistoryCur.batteryStatus != paramInt1)
      {
        mHistoryCur.batteryStatus = ((byte)(byte)paramInt1);
        paramInt5 = 1;
      }
      if (mHistoryCur.batteryHealth != paramInt2)
      {
        mHistoryCur.batteryHealth = ((byte)(byte)paramInt2);
        paramInt5 = 1;
      }
      if (mHistoryCur.batteryPlugType != paramInt3)
      {
        mHistoryCur.batteryPlugType = ((byte)(byte)paramInt3);
        paramInt5 = 1;
      }
      if ((i >= mHistoryCur.batteryTemperature + 10) || (i <= mHistoryCur.batteryTemperature - 10))
      {
        mHistoryCur.batteryTemperature = ((short)(short)i);
        paramInt5 = 1;
      }
      if ((paramInt6 > mHistoryCur.batteryVoltage + '\024') || (paramInt6 < mHistoryCur.batteryVoltage - '\024'))
      {
        mHistoryCur.batteryVoltage = ((char)(char)paramInt6);
        paramInt5 = 1;
      }
      if ((paramInt7 < mHistoryCur.batteryChargeUAh + 10) && (paramInt7 > mHistoryCur.batteryChargeUAh - 10)) {
        break label921;
      }
      if (paramInt7 < mHistoryCur.batteryChargeUAh)
      {
        l3 = mHistoryCur.batteryChargeUAh - paramInt7;
        mDischargeCounter.addCountLocked(l3);
        mDischargeScreenOffCounter.addCountLocked(l3);
        if (isScreenDoze(mScreenState)) {
          mDischargeScreenDozeCounter.addCountLocked(l3);
        }
        if (mDeviceIdleMode == 1) {
          mDischargeLightDozeCounter.addCountLocked(l3);
        } else if (mDeviceIdleMode == 2) {
          mDischargeDeepDozeCounter.addCountLocked(l3);
        }
      }
      mHistoryCur.batteryChargeUAh = paramInt7;
      paramInt5 = 1;
      label921:
      l3 = mInitStepMode << 48 | mModStepMode << 56 | (paramInt4 & 0xFF) << 40;
      if (bool2)
      {
        paramInt2 = paramInt5 | setChargingLocked(false);
        paramInt3 = paramInt2;
        if (mLastDischargeStepLevel != paramInt4)
        {
          paramInt3 = paramInt2;
          if (mMinDischargeStepLevel > paramInt4)
          {
            mDischargeStepTracker.addLevelSteps(mLastDischargeStepLevel - paramInt4, l3, l2);
            mDailyDischargeStepTracker.addLevelSteps(mLastDischargeStepLevel - paramInt4, l3, l2);
            mLastDischargeStepLevel = paramInt4;
            mMinDischargeStepLevel = paramInt4;
            mInitStepMode = mCurStepMode;
            mModStepMode = 0;
            paramInt3 = paramInt2;
          }
        }
      }
      else
      {
        paramInt3 = paramInt5;
        if (paramInt4 >= 90)
        {
          paramInt3 = paramInt5 | setChargingLocked(true);
          mLastChargeStepLevel = paramInt4;
        }
        if (!mCharging)
        {
          paramInt2 = paramInt3;
          if (mLastChargeStepLevel < paramInt4)
          {
            paramInt2 = paramInt3 | setChargingLocked(true);
            mLastChargeStepLevel = paramInt4;
          }
        }
        else
        {
          paramInt2 = paramInt3;
          if (mLastChargeStepLevel > paramInt4)
          {
            paramInt2 = paramInt3 | setChargingLocked(false);
            mLastChargeStepLevel = paramInt4;
          }
        }
        paramInt3 = paramInt2;
        if (mLastChargeStepLevel != paramInt4)
        {
          paramInt3 = paramInt2;
          if (mMaxChargeStepLevel < paramInt4)
          {
            mChargeStepTracker.addLevelSteps(paramInt4 - mLastChargeStepLevel, l3, l2);
            mDailyChargeStepTracker.addLevelSteps(paramInt4 - mLastChargeStepLevel, l3, l2);
            mLastChargeStepLevel = paramInt4;
            mMaxChargeStepLevel = paramInt4;
            mInitStepMode = mCurStepMode;
            mModStepMode = 0;
            paramInt3 = paramInt2;
          }
        }
      }
      if (paramInt3 != 0) {
        addHistoryRecordLocked(l2, l1);
      }
    }
    if ((!bool2) && ((paramInt1 == 5) || (paramInt1 == 1))) {
      mRecordingHistory = false;
    }
    if (mMinLearnedBatteryCapacity == -1) {
      mMinLearnedBatteryCapacity = paramInt8;
    } else {
      Math.min(mMinLearnedBatteryCapacity, paramInt8);
    }
    mMaxLearnedBatteryCapacity = Math.max(mMaxLearnedBatteryCapacity, paramInt8);
    if (paramInt1 == 2) {
      mChargingOnEasyTimer.startRunningLocked(l2);
    } else {
      mChargingOnEasyTimer.stopAllRunningLocked(l2);
    }
    if (!bool2) {
      mPluggedEasyTimer.startRunningLocked(l2);
    } else {
      mPluggedEasyTimer.stopAllRunningLocked(l2);
    }
  }
  
  public void setCallback(BatteryCallback paramBatteryCallback)
  {
    mCallback = paramBatteryCallback;
  }
  
  boolean setChargingLocked(boolean paramBoolean)
  {
    if (mCharging != paramBoolean)
    {
      mCharging = paramBoolean;
      BatteryStats.HistoryItem localHistoryItem;
      if (paramBoolean)
      {
        localHistoryItem = mHistoryCur;
        states2 |= 0x1000000;
      }
      else
      {
        localHistoryItem = mHistoryCur;
        states2 &= 0xFEFFFFFF;
      }
      mHandler.sendEmptyMessage(3);
      return true;
    }
    return false;
  }
  
  public void setExternalStatsSyncLocked(ExternalStatsSync paramExternalStatsSync)
  {
    mExternalSync = paramExternalStatsSync;
  }
  
  public void setNoAutoReset(boolean paramBoolean)
  {
    mNoAutoReset = paramBoolean;
  }
  
  @GuardedBy("this")
  protected void setOnBatteryLocked(long paramLong1, long paramLong2, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool1 = false;
    boolean bool2 = false;
    boolean bool3 = false;
    Object localObject = mHandler.obtainMessage(2);
    arg1 = paramBoolean;
    mHandler.sendMessage((Message)localObject);
    long l1 = paramLong2 * 1000L;
    long l2 = paramLong1 * 1000L;
    int i = mScreenState;
    if (paramBoolean)
    {
      boolean bool4 = false;
      boolean bool5;
      if (!mNoAutoReset)
      {
        if ((paramInt1 != 5) && (paramInt2 < 90) && ((mDischargeCurrentLevel >= 20) || (paramInt2 < 80)))
        {
          bool5 = bool4;
          paramBoolean = bool3;
          if (getHighDischargeAmountSinceCharge() >= 200)
          {
            bool5 = bool4;
            paramBoolean = bool3;
            if (mHistoryBuffer.dataSize() < MAX_HISTORY_BUFFER) {}
          }
        }
        else
        {
          localObject = new StringBuilder();
          ((StringBuilder)localObject).append("Resetting battery stats: level=");
          ((StringBuilder)localObject).append(paramInt2);
          ((StringBuilder)localObject).append(" status=");
          ((StringBuilder)localObject).append(paramInt1);
          ((StringBuilder)localObject).append(" dischargeLevel=");
          ((StringBuilder)localObject).append(mDischargeCurrentLevel);
          ((StringBuilder)localObject).append(" lowAmount=");
          ((StringBuilder)localObject).append(getLowDischargeAmountSinceCharge());
          ((StringBuilder)localObject).append(" highAmount=");
          ((StringBuilder)localObject).append(getHighDischargeAmountSinceCharge());
          Slog.i("BatteryStatsImpl", ((StringBuilder)localObject).toString());
          if (getLowDischargeAmountSinceCharge() >= 20)
          {
            long l3 = SystemClock.uptimeMillis();
            localObject = Parcel.obtain();
            writeSummaryToParcel((Parcel)localObject, true);
            long l4 = SystemClock.uptimeMillis();
            BackgroundThread.getHandler().post(new Runnable()
            {
              /* Error */
              public void run()
              {
                // Byte code:
                //   0: aload_0
                //   1: getfield 21	com/android/internal/os/BatteryStatsImpl$3:this$0	Lcom/android/internal/os/BatteryStatsImpl;
                //   4: getfield 36	com/android/internal/os/BatteryStatsImpl:mCheckinFile	Lcom/android/internal/os/AtomicFile;
                //   7: astore_1
                //   8: aload_1
                //   9: monitorenter
                //   10: invokestatic 42	android/os/SystemClock:uptimeMillis	()J
                //   13: lstore_2
                //   14: aconst_null
                //   15: astore 4
                //   17: aload_0
                //   18: getfield 21	com/android/internal/os/BatteryStatsImpl$3:this$0	Lcom/android/internal/os/BatteryStatsImpl;
                //   21: getfield 36	com/android/internal/os/BatteryStatsImpl:mCheckinFile	Lcom/android/internal/os/AtomicFile;
                //   24: invokevirtual 48	com/android/internal/os/AtomicFile:startWrite	()Ljava/io/FileOutputStream;
                //   27: astore 5
                //   29: aload 5
                //   31: astore 4
                //   33: aload 5
                //   35: aload_0
                //   36: getfield 23	com/android/internal/os/BatteryStatsImpl$3:val$parcel	Landroid/os/Parcel;
                //   39: invokevirtual 54	android/os/Parcel:marshall	()[B
                //   42: invokevirtual 60	java/io/FileOutputStream:write	([B)V
                //   45: aload 5
                //   47: astore 4
                //   49: aload 5
                //   51: invokevirtual 63	java/io/FileOutputStream:flush	()V
                //   54: aload 5
                //   56: astore 4
                //   58: aload 5
                //   60: invokestatic 69	android/os/FileUtils:sync	(Ljava/io/FileOutputStream;)Z
                //   63: pop
                //   64: aload 5
                //   66: astore 4
                //   68: aload 5
                //   70: invokevirtual 72	java/io/FileOutputStream:close	()V
                //   73: aload 5
                //   75: astore 4
                //   77: aload_0
                //   78: getfield 21	com/android/internal/os/BatteryStatsImpl$3:this$0	Lcom/android/internal/os/BatteryStatsImpl;
                //   81: getfield 36	com/android/internal/os/BatteryStatsImpl:mCheckinFile	Lcom/android/internal/os/AtomicFile;
                //   84: aload 5
                //   86: invokevirtual 76	com/android/internal/os/AtomicFile:finishWrite	(Ljava/io/FileOutputStream;)V
                //   89: aload 5
                //   91: astore 4
                //   93: ldc 78
                //   95: aload_0
                //   96: getfield 25	com/android/internal/os/BatteryStatsImpl$3:val$initialTime	J
                //   99: invokestatic 42	android/os/SystemClock:uptimeMillis	()J
                //   102: ladd
                //   103: lload_2
                //   104: lsub
                //   105: invokestatic 84	com/android/internal/logging/EventLogTags:writeCommitSysConfigFile	(Ljava/lang/String;J)V
                //   108: aload_0
                //   109: getfield 23	com/android/internal/os/BatteryStatsImpl$3:val$parcel	Landroid/os/Parcel;
                //   112: astore 4
                //   114: goto +38 -> 152
                //   117: astore 4
                //   119: goto +41 -> 160
                //   122: astore 5
                //   124: ldc 86
                //   126: ldc 88
                //   128: aload 5
                //   130: invokestatic 94	android/util/Slog:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
                //   133: pop
                //   134: aload_0
                //   135: getfield 21	com/android/internal/os/BatteryStatsImpl$3:this$0	Lcom/android/internal/os/BatteryStatsImpl;
                //   138: getfield 36	com/android/internal/os/BatteryStatsImpl:mCheckinFile	Lcom/android/internal/os/AtomicFile;
                //   141: aload 4
                //   143: invokevirtual 97	com/android/internal/os/AtomicFile:failWrite	(Ljava/io/FileOutputStream;)V
                //   146: aload_0
                //   147: getfield 23	com/android/internal/os/BatteryStatsImpl$3:val$parcel	Landroid/os/Parcel;
                //   150: astore 4
                //   152: aload 4
                //   154: invokevirtual 100	android/os/Parcel:recycle	()V
                //   157: aload_1
                //   158: monitorexit
                //   159: return
                //   160: aload_0
                //   161: getfield 23	com/android/internal/os/BatteryStatsImpl$3:val$parcel	Landroid/os/Parcel;
                //   164: invokevirtual 100	android/os/Parcel:recycle	()V
                //   167: aload 4
                //   169: athrow
                //   170: astore 4
                //   172: aload_1
                //   173: monitorexit
                //   174: aload 4
                //   176: athrow
                // Local variable table:
                //   start	length	slot	name	signature
                //   0	177	0	this	3
                //   7	166	1	localAtomicFile	AtomicFile
                //   13	91	2	l	long
                //   15	98	4	localObject1	Object
                //   117	25	4	localFileOutputStream1	FileOutputStream
                //   150	18	4	localParcel	Parcel
                //   170	5	4	localObject2	Object
                //   27	63	5	localFileOutputStream2	FileOutputStream
                //   122	7	5	localIOException	IOException
                // Exception table:
                //   from	to	target	type
                //   17	29	117	finally
                //   33	45	117	finally
                //   49	54	117	finally
                //   58	64	117	finally
                //   68	73	117	finally
                //   77	89	117	finally
                //   93	108	117	finally
                //   124	146	117	finally
                //   17	29	122	java/io/IOException
                //   33	45	122	java/io/IOException
                //   49	54	122	java/io/IOException
                //   58	64	122	java/io/IOException
                //   68	73	122	java/io/IOException
                //   77	89	122	java/io/IOException
                //   93	108	122	java/io/IOException
                //   10	14	170	finally
                //   108	114	170	finally
                //   146	152	170	finally
                //   152	157	170	finally
                //   157	159	170	finally
                //   160	170	170	finally
                //   172	174	170	finally
              }
            });
          }
          resetAllStatsLocked();
          if ((paramInt3 > 0) && (paramInt2 > 0)) {
            mEstimatedBatteryCapacity = ((int)(paramInt3 / 1000 / (paramInt2 / 100.0D)));
          }
          mDischargeStartLevel = paramInt2;
          bool5 = true;
          mDischargeStepTracker.init();
          paramBoolean = true;
        }
      }
      else
      {
        bool5 = false;
        paramBoolean = bool1;
      }
      if (mCharging) {
        setChargingLocked(false);
      }
      mLastChargingStateLevel = paramInt2;
      mOnBatteryInternal = true;
      mOnBattery = true;
      mLastDischargeStepLevel = paramInt2;
      mMinDischargeStepLevel = paramInt2;
      mDischargeStepTracker.clearTime();
      mDailyDischargeStepTracker.clearTime();
      mInitStepMode = mCurStepMode;
      mModStepMode = 0;
      pullPendingStateUpdatesLocked();
      mHistoryCur.batteryLevel = ((byte)(byte)paramInt2);
      localObject = mHistoryCur;
      states &= 0xFFF7FFFF;
      if (bool5)
      {
        mRecordingHistory = true;
        startRecordingHistory(paramLong1, paramLong2, bool5);
      }
      addHistoryRecordLocked(paramLong1, paramLong2);
      mDischargeUnplugLevel = paramInt2;
      mDischargeCurrentLevel = paramInt2;
      if (isScreenOn(i))
      {
        mDischargeScreenOnUnplugLevel = paramInt2;
        mDischargeScreenDozeUnplugLevel = 0;
        mDischargeScreenOffUnplugLevel = 0;
      }
      else if (isScreenDoze(i))
      {
        mDischargeScreenOnUnplugLevel = 0;
        mDischargeScreenDozeUnplugLevel = paramInt2;
        mDischargeScreenOffUnplugLevel = 0;
      }
      else
      {
        mDischargeScreenOnUnplugLevel = 0;
        mDischargeScreenDozeUnplugLevel = 0;
        mDischargeScreenOffUnplugLevel = paramInt2;
      }
      mDischargeAmountScreenOn = 0;
      mDischargeAmountScreenDoze = 0;
      mDischargeAmountScreenOff = 0;
      updateTimeBasesLocked(true, i, l1, l2);
    }
    else
    {
      mLastChargingStateLevel = paramInt2;
      mOnBatteryInternal = false;
      mOnBattery = false;
      pullPendingStateUpdatesLocked();
      mHistoryCur.batteryLevel = ((byte)(byte)paramInt2);
      localObject = mHistoryCur;
      states |= 0x80000;
      addHistoryRecordLocked(paramLong1, paramLong2);
      mDischargePlugLevel = paramInt2;
      mDischargeCurrentLevel = paramInt2;
      if (paramInt2 < mDischargeUnplugLevel)
      {
        mLowDischargeAmountSinceCharge += mDischargeUnplugLevel - paramInt2 - 1;
        mHighDischargeAmountSinceCharge += mDischargeUnplugLevel - paramInt2;
      }
      updateDischargeScreenLevelsLocked(i, i);
      updateTimeBasesLocked(false, i, l1, l2);
      mChargeStepTracker.init();
      mLastChargeStepLevel = paramInt2;
      mMaxChargeStepLevel = paramInt2;
      mInitStepMode = mCurStepMode;
      mModStepMode = 0;
      paramBoolean = bool2;
    }
    if (((paramBoolean) || (mLastWriteTime + 60000L < paramLong1)) && (mFile != null)) {
      writeAsyncLocked();
    }
  }
  
  public void setPowerProfileLocked(PowerProfile paramPowerProfile)
  {
    mPowerProfile = paramPowerProfile;
    int i = mPowerProfile.getNumCpuClusters();
    mKernelCpuSpeedReaders = new KernelCpuSpeedReader[i];
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      int m = mPowerProfile.getNumSpeedStepsInCpuCluster(k);
      mKernelCpuSpeedReaders[k] = new KernelCpuSpeedReader(j, m);
      j += mPowerProfile.getNumCoresInCpuCluster(k);
    }
    if (mEstimatedBatteryCapacity == -1) {
      mEstimatedBatteryCapacity = ((int)mPowerProfile.getBatteryCapacity());
    }
  }
  
  public void setPretendScreenOff(boolean paramBoolean)
  {
    if (mPretendScreenOff != paramBoolean)
    {
      mPretendScreenOff = paramBoolean;
      int i;
      if (paramBoolean) {
        i = 1;
      } else {
        i = 2;
      }
      noteScreenStateLocked(i);
    }
  }
  
  public void setRadioScanningTimeoutLocked(long paramLong)
  {
    if (mPhoneSignalScanningTimer != null) {
      mPhoneSignalScanningTimer.setTimeout(paramLong);
    }
  }
  
  public void setRecordAllHistoryLocked(boolean paramBoolean)
  {
    mRecordAllHistory = paramBoolean;
    Object localObject1;
    long l1;
    long l2;
    Object localObject2;
    Object localObject3;
    int i;
    if (!paramBoolean)
    {
      mActiveEvents.removeEvents(5);
      mActiveEvents.removeEvents(13);
      localObject1 = mActiveEvents.getStateForEvent(1);
      if (localObject1 != null)
      {
        l1 = mClocks.elapsedRealtime();
        l2 = mClocks.uptimeMillis();
        localObject1 = ((HashMap)localObject1).entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (Map.Entry)((Iterator)localObject1).next();
          localObject3 = (SparseIntArray)((Map.Entry)localObject2).getValue();
          for (i = 0; i < ((SparseIntArray)localObject3).size(); i++) {
            addHistoryEventLocked(l1, l2, 16385, (String)((Map.Entry)localObject2).getKey(), ((SparseIntArray)localObject3).keyAt(i));
          }
        }
      }
    }
    else
    {
      localObject1 = mActiveEvents.getStateForEvent(1);
      if (localObject1 != null)
      {
        l2 = mClocks.elapsedRealtime();
        l1 = mClocks.uptimeMillis();
        localObject1 = ((HashMap)localObject1).entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject3 = (Map.Entry)((Iterator)localObject1).next();
          localObject2 = (SparseIntArray)((Map.Entry)localObject3).getValue();
          for (i = 0; i < ((SparseIntArray)localObject2).size(); i++) {
            addHistoryEventLocked(l2, l1, 32769, (String)((Map.Entry)localObject3).getKey(), ((SparseIntArray)localObject2).keyAt(i));
          }
        }
      }
    }
  }
  
  public void shutdownLocked()
  {
    recordShutdownLocked(mClocks.elapsedRealtime(), mClocks.uptimeMillis());
    writeSyncLocked();
    mShuttingDown = true;
  }
  
  public boolean startAddingCpuLocked()
  {
    mExternalSync.cancelCpuSyncDueToWakelockChange();
    return mOnBatteryInternal;
  }
  
  public boolean startIteratingHistoryLocked()
  {
    if (mHistoryBuffer.dataSize() <= 0) {
      return false;
    }
    mHistoryBuffer.setDataPosition(0);
    mReadOverflow = false;
    mIteratingHistory = true;
    mReadHistoryStrings = new String[mHistoryTagPool.size()];
    mReadHistoryUids = new int[mHistoryTagPool.size()];
    mReadHistoryChars = 0;
    Iterator localIterator = mHistoryTagPool.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      BatteryStats.HistoryTag localHistoryTag = (BatteryStats.HistoryTag)localEntry.getKey();
      int i = ((Integer)localEntry.getValue()).intValue();
      mReadHistoryStrings[i] = string;
      mReadHistoryUids[i] = uid;
      mReadHistoryChars += string.length() + 1;
    }
    return true;
  }
  
  public boolean startIteratingOldHistoryLocked()
  {
    BatteryStats.HistoryItem localHistoryItem = mHistory;
    mHistoryIterator = localHistoryItem;
    if (localHistoryItem == null) {
      return false;
    }
    mHistoryBuffer.setDataPosition(0);
    mHistoryReadTmp.clear();
    mReadOverflow = false;
    mIteratingHistory = true;
    return true;
  }
  
  void stopAllGpsSignalQualityTimersLocked(int paramInt)
  {
    long l = mClocks.elapsedRealtime();
    for (int i = 0; i < 2; i++) {
      if (i != paramInt) {
        while (mGpsSignalQualityTimer[i].isRunningLocked()) {
          mGpsSignalQualityTimer[i].stopRunningLocked(l);
        }
      }
    }
  }
  
  void stopAllPhoneSignalStrengthTimersLocked(int paramInt)
  {
    long l = mClocks.elapsedRealtime();
    for (int i = 0; i < 6; i++) {
      if (i != paramInt) {
        while (mPhoneSignalStrengthsTimer[i].isRunningLocked()) {
          mPhoneSignalStrengthsTimer[i].stopRunningLocked(l);
        }
      }
    }
  }
  
  void stopAllWifiSignalStrengthTimersLocked(int paramInt)
  {
    long l = mClocks.elapsedRealtime();
    for (int i = 0; i < 5; i++) {
      if (i != paramInt) {
        while (mWifiSignalStrengthsTimer[i].isRunningLocked()) {
          mWifiSignalStrengthsTimer[i].stopRunningLocked(l);
        }
      }
    }
  }
  
  public void systemServicesReady(Context paramContext)
  {
    mConstants.startObserving(paramContext.getContentResolver());
    registerUsbStateReceiver(paramContext);
  }
  
  public boolean trackPerProcStateCpuTimes()
  {
    boolean bool;
    if ((mConstants.TRACK_CPU_TIMES_BY_PROC_STATE) && (mPerProcStateCpuTimesAvailable)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void updateBluetoothStateLocked(BluetoothActivityEnergyInfo paramBluetoothActivityEnergyInfo)
  {
    Object localObject1 = this;
    if ((paramBluetoothActivityEnergyInfo != null) && (mOnBatteryInternal))
    {
      mHasBluetoothReporting = true;
      long l1 = mClocks.elapsedRealtime();
      long l2 = paramBluetoothActivityEnergyInfo.getControllerRxTimeMillis() - mLastBluetoothActivityInfo.rxTimeMs;
      long l3 = paramBluetoothActivityEnergyInfo.getControllerTxTimeMillis() - mLastBluetoothActivityInfo.txTimeMs;
      long l4 = paramBluetoothActivityEnergyInfo.getControllerIdleTimeMillis() - mLastBluetoothActivityInfo.idleTimeMs;
      int i = mUidStats.size();
      long l5 = 0L;
      Object localObject2;
      for (int j = 0; j < i; j++)
      {
        localObject2 = (Uid)mUidStats.valueAt(j);
        if (mBluetoothScanTimer != null) {
          l5 += mBluetoothScanTimer.getTimeSinceMarkLocked(l1 * 1000L) / 1000L;
        }
      }
      if (l5 > l2) {
        j = 1;
      } else {
        j = 0;
      }
      int k;
      if (l5 > l3) {
        k = 1;
      } else {
        k = 0;
      }
      long l6 = l2;
      long l7 = l3;
      long l8;
      long l10;
      for (int m = 0; m < i; m++)
      {
        localObject1 = (Uid)mUidStats.valueAt(m);
        if (mBluetoothScanTimer != null)
        {
          l8 = mBluetoothScanTimer.getTimeSinceMarkLocked(l1 * 1000L) / 1000L;
          if (l8 > 0L)
          {
            mBluetoothScanTimer.setMark(l1);
            long l9 = l8;
            l10 = l9;
            if (j != 0) {
              l10 = l2 * l9 / l5;
            }
            l9 = l8;
            if (k != 0) {
              l9 = l3 * l8 / l5;
            }
            localObject1 = ((Uid)localObject1).getOrCreateBluetoothControllerActivityLocked();
            ((ControllerActivityCounterImpl)localObject1).getRxTimeCounter().addCountLocked(l10);
            localObject1.getTxTimeCounters()[0].addCountLocked(l9);
            l6 -= l10;
            l7 -= l9;
          }
        }
      }
      l1 = 0L;
      localObject1 = paramBluetoothActivityEnergyInfo.getUidTraffic();
      if (localObject1 != null) {
        j = localObject1.length;
      } else {
        j = 0;
      }
      l5 = 0L;
      for (i = 0; i < j; i++)
      {
        localObject2 = localObject1[i];
        l10 = ((UidTraffic)localObject2).getRxBytes() - mLastBluetoothActivityInfo.uidRxBytes.get(((UidTraffic)localObject2).getUid());
        l8 = ((UidTraffic)localObject2).getTxBytes() - mLastBluetoothActivityInfo.uidTxBytes.get(((UidTraffic)localObject2).getUid());
        mNetworkByteActivityCounters[4].addCountLocked(l10);
        mNetworkByteActivityCounters[5].addCountLocked(l8);
        localObject2 = getUidStatsLocked(mapUid(((UidTraffic)localObject2).getUid()));
        ((Uid)localObject2).noteNetworkActivityLocked(4, l10, 0L);
        ((Uid)localObject2).noteNetworkActivityLocked(5, l8, 0L);
        l1 += l10;
        l5 += l8;
      }
      if (((l5 != 0L) || (l1 != 0L)) && ((l6 != 0L) || (l7 != 0L)))
      {
        k = 0;
        while (k < j)
        {
          localObject2 = localObject1[k];
          i = ((UidTraffic)localObject2).getUid();
          l8 = ((UidTraffic)localObject2).getRxBytes() - mLastBluetoothActivityInfo.uidRxBytes.get(i);
          l10 = ((UidTraffic)localObject2).getTxBytes() - mLastBluetoothActivityInfo.uidTxBytes.get(i);
          localObject2 = getUidStatsLocked(mapUid(i));
          localObject2 = ((Uid)localObject2).getOrCreateBluetoothControllerActivityLocked();
          if ((l1 > 0L) && (l8 > 0L))
          {
            l8 = l6 * l8 / l1;
            ((ControllerActivityCounterImpl)localObject2).getRxTimeCounter().addCountLocked(l8);
            l6 -= l8;
          }
          l8 = l7;
          if (l5 > 0L)
          {
            l8 = l7;
            if (l10 > 0L)
            {
              l8 = l7 * l10 / l5;
              localObject2.getTxTimeCounters()[0].addCountLocked(l8);
              l8 = l7 - l8;
            }
          }
          k++;
          l7 = l8;
        }
      }
      mBluetoothActivity.getRxTimeCounter().addCountLocked(l2);
      mBluetoothActivity.getTxTimeCounters()[0].addCountLocked(l3);
      mBluetoothActivity.getIdleTimeCounter().addCountLocked(l4);
      double d = mPowerProfile.getAveragePower("bluetooth.controller.voltage") / 1000.0D;
      if (d != 0.0D) {
        mBluetoothActivity.getPowerCounter().addCountLocked(((paramBluetoothActivityEnergyInfo.getControllerEnergyUsed() - mLastBluetoothActivityInfo.energy) / d));
      }
      mLastBluetoothActivityInfo.set(paramBluetoothActivityEnergyInfo);
      return;
    }
  }
  
  @VisibleForTesting
  public void updateClusterSpeedTimes(SparseLongArray paramSparseLongArray, boolean paramBoolean)
  {
    Object localObject1 = this;
    long[][] arrayOfLong = new long[mKernelCpuSpeedReaders.length][];
    long l1 = 0L;
    int i = 0;
    long l2;
    int j;
    while (i < mKernelCpuSpeedReaders.length)
    {
      arrayOfLong[i] = mKernelCpuSpeedReaders[i].readDelta();
      l2 = l1;
      if (arrayOfLong[i] != null) {
        for (j = arrayOfLong[i].length - 1;; j--)
        {
          l2 = l1;
          if (j < 0) {
            break;
          }
          l1 += arrayOfLong[i][j];
        }
      }
      i++;
      l1 = l2;
    }
    localObject1 = arrayOfLong;
    if (l1 != 0L)
    {
      int k = paramSparseLongArray.size();
      for (i = 0;; i++)
      {
        Object localObject2 = paramSparseLongArray;
        BatteryStatsImpl localBatteryStatsImpl = this;
        localObject1 = arrayOfLong;
        if (i >= k) {
          break;
        }
        localObject1 = localBatteryStatsImpl.getUidStatsLocked(((SparseLongArray)localObject2).keyAt(i));
        l2 = ((SparseLongArray)localObject2).valueAt(i);
        j = mPowerProfile.getNumCpuClusters();
        if ((mCpuClusterSpeedTimesUs == null) || (mCpuClusterSpeedTimesUs.length != j)) {
          mCpuClusterSpeedTimesUs = new LongSamplingCounter[j][];
        }
        for (j = 0; j < arrayOfLong.length; j++)
        {
          int m = arrayOfLong[j].length;
          if ((mCpuClusterSpeedTimesUs[j] == null) || (m != mCpuClusterSpeedTimesUs[j].length)) {
            mCpuClusterSpeedTimesUs[j] = new LongSamplingCounter[m];
          }
          localObject2 = mCpuClusterSpeedTimesUs[j];
          for (int n = 0; n < m; n++)
          {
            if (localObject2[n] == null) {
              localObject2[n] = new LongSamplingCounter(mOnBatteryTimeBase);
            }
            localObject2[n].addCountLocked(arrayOfLong[j][n] * l2 / l1, paramBoolean);
          }
        }
      }
    }
  }
  
  @GuardedBy("this")
  public void updateCpuTimeLocked(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (mPowerProfile == null) {
      return;
    }
    if (mCpuFreqs == null) {
      mCpuFreqs = mKernelUidCpuFreqTimeReader.readFreqs(mPowerProfile);
    }
    Object localObject1 = null;
    int i;
    if (paramBoolean2)
    {
      localObject2 = new ArrayList();
      for (i = mPartialTimers.size() - 1;; i--)
      {
        localObject1 = localObject2;
        if (i < 0) {
          break;
        }
        localObject1 = (StopwatchTimer)mPartialTimers.get(i);
        if ((mInList) && (mUid != null) && (mUid.mUid != 1000)) {
          ((ArrayList)localObject2).add(localObject1);
        }
      }
    }
    markPartialTimersAsEligible();
    Object localObject2 = null;
    if (!paramBoolean1)
    {
      mKernelUidCpuTimeReader.readDelta(null);
      mKernelUidCpuFreqTimeReader.readDelta(null);
      mNumAllUidCpuTimeReads += 2;
      if (mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME)
      {
        mKernelUidCpuActiveTimeReader.readDelta(null);
        mKernelUidCpuClusterTimeReader.readDelta(null);
        mNumAllUidCpuTimeReads += 2;
      }
      for (i = mKernelCpuSpeedReaders.length - 1; i >= 0; i--) {
        mKernelCpuSpeedReaders[i].readDelta();
      }
      return;
    }
    mUserInfoProvider.refreshUserIds();
    if (!mKernelUidCpuFreqTimeReader.perClusterTimesAvailable()) {
      localObject2 = new SparseLongArray();
    }
    readKernelUidCpuTimesLocked((ArrayList)localObject1, (SparseLongArray)localObject2, paramBoolean1);
    if (localObject2 != null) {
      updateClusterSpeedTimes((SparseLongArray)localObject2, paramBoolean1);
    }
    readKernelUidCpuFreqTimesLocked((ArrayList)localObject1, paramBoolean1, paramBoolean2);
    mNumAllUidCpuTimeReads += 2;
    if (mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME)
    {
      readKernelUidCpuActiveTimesLocked(paramBoolean1);
      readKernelUidCpuClusterTimesLocked(paramBoolean1);
      mNumAllUidCpuTimeReads += 2;
    }
  }
  
  public void updateDailyDeadlineLocked()
  {
    long l = System.currentTimeMillis();
    mDailyStartTime = l;
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(l);
    localCalendar.set(6, localCalendar.get(6) + 1);
    localCalendar.set(14, 0);
    localCalendar.set(13, 0);
    localCalendar.set(12, 0);
    localCalendar.set(11, 1);
    mNextMinDailyDeadline = localCalendar.getTimeInMillis();
    localCalendar.set(11, 3);
    mNextMaxDailyDeadline = localCalendar.getTimeInMillis();
  }
  
  void updateDischargeScreenLevelsLocked(int paramInt1, int paramInt2)
  {
    updateOldDischargeScreenLevelLocked(paramInt1);
    updateNewDischargeScreenLevelLocked(paramInt2);
  }
  
  public void updateKernelMemoryBandwidthLocked()
  {
    mKernelMemoryBandwidthStats.updateStats();
    LongSparseLongArray localLongSparseLongArray = mKernelMemoryBandwidthStats.getBandwidthEntries();
    int i = localLongSparseLongArray.size();
    for (int j = 0; j < i; j++)
    {
      int k = mKernelMemoryStats.indexOfKey(localLongSparseLongArray.keyAt(j));
      SamplingTimer localSamplingTimer;
      if (k >= 0)
      {
        localSamplingTimer = (SamplingTimer)mKernelMemoryStats.valueAt(k);
      }
      else
      {
        localSamplingTimer = new SamplingTimer(mClocks, mOnBatteryTimeBase);
        mKernelMemoryStats.put(localLongSparseLongArray.keyAt(j), localSamplingTimer);
      }
      localSamplingTimer.update(localLongSparseLongArray.valueAt(j), 1);
    }
  }
  
  public void updateKernelWakelocksLocked()
  {
    KernelWakelockStats localKernelWakelockStats = mKernelWakelockReader.readKernelWakelockStats(mTmpWakelockStats);
    if (localKernelWakelockStats == null)
    {
      Slog.w("BatteryStatsImpl", "Couldn't get kernel wake lock stats");
      return;
    }
    Iterator localIterator = localKernelWakelockStats.entrySet().iterator();
    Object localObject1;
    while (localIterator.hasNext())
    {
      localObject1 = (Map.Entry)localIterator.next();
      String str = (String)((Map.Entry)localObject1).getKey();
      KernelWakelockStats.Entry localEntry = (KernelWakelockStats.Entry)((Map.Entry)localObject1).getValue();
      localObject2 = (SamplingTimer)mKernelWakelockStats.get(str);
      localObject1 = localObject2;
      if (localObject2 == null)
      {
        localObject1 = new SamplingTimer(mClocks, mOnBatteryScreenOffTimeBase);
        mKernelWakelockStats.put(str, localObject1);
      }
      ((SamplingTimer)localObject1).update(mTotalTime, mCount);
      ((SamplingTimer)localObject1).setUpdateVersion(mVersion);
    }
    int i = 0;
    Object localObject2 = mKernelWakelockStats.entrySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject1 = (SamplingTimer)((Map.Entry)((Iterator)localObject2).next()).getValue();
      int j = i;
      if (((SamplingTimer)localObject1).getUpdateVersion() != kernelWakelockVersion)
      {
        ((SamplingTimer)localObject1).endSample();
        j = i + 1;
      }
      i = j;
    }
    if (localKernelWakelockStats.isEmpty()) {
      Slog.wtf("BatteryStatsImpl", "All kernel wakelocks had time of zero");
    }
    if (i == mKernelWakelockStats.size())
    {
      localObject1 = new StringBuilder();
      ((StringBuilder)localObject1).append("All kernel wakelocks were set stale. new version=");
      ((StringBuilder)localObject1).append(kernelWakelockVersion);
      Slog.wtf("BatteryStatsImpl", ((StringBuilder)localObject1).toString());
    }
  }
  
  /* Error */
  public void updateMobileRadioState(ModemActivityInfo paramModemActivityInfo)
  {
    // Byte code:
    //   0: aload_0
    //   1: astore_2
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial 4358	com/android/internal/os/BatteryStatsImpl:getDeltaModemActivityInfo	(Landroid/telephony/ModemActivityInfo;)Landroid/telephony/ModemActivityInfo;
    //   7: astore_1
    //   8: aload_2
    //   9: aload_1
    //   10: invokespecial 4360	com/android/internal/os/BatteryStatsImpl:addModemTxPowerToHistory	(Landroid/telephony/ModemActivityInfo;)V
    //   13: aconst_null
    //   14: astore_3
    //   15: aload_2
    //   16: getfield 926	com/android/internal/os/BatteryStatsImpl:mModemNetworkLock	Ljava/lang/Object;
    //   19: astore 4
    //   21: aload 4
    //   23: monitorenter
    //   24: aload_2
    //   25: aload_2
    //   26: getfield 928	com/android/internal/os/BatteryStatsImpl:mModemIfaces	[Ljava/lang/String;
    //   29: invokespecial 4362	com/android/internal/os/BatteryStatsImpl:readNetworkStatsLocked	([Ljava/lang/String;)Landroid/net/NetworkStats;
    //   32: astore 5
    //   34: aload 5
    //   36: ifnull +54 -> 90
    //   39: aload 5
    //   41: aload_2
    //   42: getfield 930	com/android/internal/os/BatteryStatsImpl:mLastModemNetworkStats	Landroid/net/NetworkStats;
    //   45: aconst_null
    //   46: aconst_null
    //   47: aload_2
    //   48: getfield 905	com/android/internal/os/BatteryStatsImpl:mNetworkStatsPool	Landroid/util/Pools$Pool;
    //   51: invokeinterface 1842 1 0
    //   56: checkcast 919	android/net/NetworkStats
    //   59: invokestatic 4366	android/net/NetworkStats:subtract	(Landroid/net/NetworkStats;Landroid/net/NetworkStats;Landroid/net/NetworkStats$NonMonotonicObserver;Ljava/lang/Object;Landroid/net/NetworkStats;)Landroid/net/NetworkStats;
    //   62: astore_3
    //   63: aload_2
    //   64: getfield 905	com/android/internal/os/BatteryStatsImpl:mNetworkStatsPool	Landroid/util/Pools$Pool;
    //   67: aload_2
    //   68: getfield 930	com/android/internal/os/BatteryStatsImpl:mLastModemNetworkStats	Landroid/net/NetworkStats;
    //   71: invokeinterface 4369 2 0
    //   76: pop
    //   77: aload_2
    //   78: aload 5
    //   80: putfield 930	com/android/internal/os/BatteryStatsImpl:mLastModemNetworkStats	Landroid/net/NetworkStats;
    //   83: goto +7 -> 90
    //   86: astore_1
    //   87: goto +1100 -> 1187
    //   90: aload 4
    //   92: monitorexit
    //   93: aload_0
    //   94: monitorenter
    //   95: aload_1
    //   96: astore 4
    //   98: aload_2
    //   99: getfield 1085	com/android/internal/os/BatteryStatsImpl:mOnBatteryInternal	Z
    //   102: istore 6
    //   104: iload 6
    //   106: ifne +34 -> 140
    //   109: aload_3
    //   110: ifnull +24 -> 134
    //   113: aload_1
    //   114: astore 5
    //   116: aload_2
    //   117: getfield 905	com/android/internal/os/BatteryStatsImpl:mNetworkStatsPool	Landroid/util/Pools$Pool;
    //   120: aload_3
    //   121: invokeinterface 4369 2 0
    //   126: pop
    //   127: goto +7 -> 134
    //   130: astore_1
    //   131: goto +1047 -> 1178
    //   134: aload_1
    //   135: astore 5
    //   137: aload_0
    //   138: monitorexit
    //   139: return
    //   140: aload_1
    //   141: ifnull +297 -> 438
    //   144: aload_1
    //   145: astore 5
    //   147: aload_2
    //   148: iconst_1
    //   149: putfield 810	com/android/internal/os/BatteryStatsImpl:mHasModemReporting	Z
    //   152: aload_1
    //   153: astore 5
    //   155: aload_2
    //   156: getfield 1045	com/android/internal/os/BatteryStatsImpl:mModemActivity	Lcom/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl;
    //   159: invokevirtual 4239	com/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl:getIdleTimeCounter	()Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   162: aload_1
    //   163: invokevirtual 1466	android/telephony/ModemActivityInfo:getIdleTimeMillis	()I
    //   166: i2l
    //   167: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   170: aload_1
    //   171: astore 5
    //   173: aload_2
    //   174: getfield 1045	com/android/internal/os/BatteryStatsImpl:mModemActivity	Lcom/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl;
    //   177: invokevirtual 4371	com/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl:getSleepTimeCounter	()Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   180: aload_1
    //   181: invokevirtual 1463	android/telephony/ModemActivityInfo:getSleepTimeMillis	()I
    //   184: i2l
    //   185: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   188: aload_1
    //   189: astore 5
    //   191: aload_2
    //   192: getfield 1045	com/android/internal/os/BatteryStatsImpl:mModemActivity	Lcom/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl;
    //   195: invokevirtual 4209	com/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl:getRxTimeCounter	()Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   198: aload_1
    //   199: invokevirtual 1469	android/telephony/ModemActivityInfo:getRxTimeMillis	()I
    //   202: i2l
    //   203: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   206: iconst_0
    //   207: istore 7
    //   209: iload 7
    //   211: iconst_5
    //   212: if_icmpge +33 -> 245
    //   215: aload_1
    //   216: astore 5
    //   218: aload_2
    //   219: getfield 1045	com/android/internal/os/BatteryStatsImpl:mModemActivity	Lcom/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl;
    //   222: invokevirtual 4212	com/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl:getTxTimeCounters	()[Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   225: iload 7
    //   227: aaload
    //   228: aload_1
    //   229: invokevirtual 1265	android/telephony/ModemActivityInfo:getTxTimeMillis	()[I
    //   232: iload 7
    //   234: iaload
    //   235: i2l
    //   236: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   239: iinc 7 1
    //   242: goto -33 -> 209
    //   245: aload_1
    //   246: astore 5
    //   248: aload_2
    //   249: getfield 1545	com/android/internal/os/BatteryStatsImpl:mPowerProfile	Lcom/android/internal/os/PowerProfile;
    //   252: ldc_w 4373
    //   255: invokevirtual 2675	com/android/internal/os/PowerProfile:getAveragePower	(Ljava/lang/String;)D
    //   258: ldc2_w 2676
    //   261: ddiv
    //   262: dconst_0
    //   263: dcmpl
    //   264: ifeq +174 -> 438
    //   267: aload_1
    //   268: astore 5
    //   270: aload_1
    //   271: invokevirtual 1463	android/telephony/ModemActivityInfo:getSleepTimeMillis	()I
    //   274: i2d
    //   275: dstore 8
    //   277: aload_1
    //   278: astore 5
    //   280: aload_2
    //   281: getfield 1545	com/android/internal/os/BatteryStatsImpl:mPowerProfile	Lcom/android/internal/os/PowerProfile;
    //   284: ldc_w 4375
    //   287: invokevirtual 2675	com/android/internal/os/PowerProfile:getAveragePower	(Ljava/lang/String;)D
    //   290: dstore 10
    //   292: aload_1
    //   293: astore 5
    //   295: aload_1
    //   296: invokevirtual 1466	android/telephony/ModemActivityInfo:getIdleTimeMillis	()I
    //   299: i2d
    //   300: dstore 12
    //   302: aload_1
    //   303: astore 5
    //   305: aload_2
    //   306: getfield 1545	com/android/internal/os/BatteryStatsImpl:mPowerProfile	Lcom/android/internal/os/PowerProfile;
    //   309: ldc_w 4377
    //   312: invokevirtual 2675	com/android/internal/os/PowerProfile:getAveragePower	(Ljava/lang/String;)D
    //   315: dstore 14
    //   317: aload_1
    //   318: astore 5
    //   320: aload_1
    //   321: invokevirtual 1469	android/telephony/ModemActivityInfo:getRxTimeMillis	()I
    //   324: i2d
    //   325: dstore 16
    //   327: aload_1
    //   328: astore 5
    //   330: aload_2
    //   331: getfield 1545	com/android/internal/os/BatteryStatsImpl:mPowerProfile	Lcom/android/internal/os/PowerProfile;
    //   334: ldc_w 4379
    //   337: invokevirtual 2675	com/android/internal/os/PowerProfile:getAveragePower	(Ljava/lang/String;)D
    //   340: dstore 18
    //   342: aload_1
    //   343: astore 5
    //   345: aload_1
    //   346: invokevirtual 1265	android/telephony/ModemActivityInfo:getTxTimeMillis	()[I
    //   349: astore 4
    //   351: dload 8
    //   353: dload 10
    //   355: dmul
    //   356: dload 12
    //   358: dload 14
    //   360: dmul
    //   361: dadd
    //   362: dload 16
    //   364: dload 18
    //   366: dmul
    //   367: dadd
    //   368: dstore 8
    //   370: iconst_0
    //   371: istore 7
    //   373: aload_1
    //   374: astore 5
    //   376: iload 7
    //   378: aload 4
    //   380: arraylength
    //   381: bipush 6
    //   383: invokestatic 2562	java/lang/Math:min	(II)I
    //   386: if_icmpge +36 -> 422
    //   389: aload_1
    //   390: astore 5
    //   392: dload 8
    //   394: aload 4
    //   396: iload 7
    //   398: iaload
    //   399: i2d
    //   400: aload_2
    //   401: getfield 1545	com/android/internal/os/BatteryStatsImpl:mPowerProfile	Lcom/android/internal/os/PowerProfile;
    //   404: ldc_w 4381
    //   407: iload 7
    //   409: invokevirtual 2682	com/android/internal/os/PowerProfile:getAveragePower	(Ljava/lang/String;I)D
    //   412: dmul
    //   413: dadd
    //   414: dstore 8
    //   416: iinc 7 1
    //   419: goto -46 -> 373
    //   422: aload_1
    //   423: astore 5
    //   425: aload_2
    //   426: getfield 1045	com/android/internal/os/BatteryStatsImpl:mModemActivity	Lcom/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl;
    //   429: invokevirtual 4243	com/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl:getPowerCounter	()Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   432: dload 8
    //   434: d2l
    //   435: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   438: aload_1
    //   439: astore 4
    //   441: aload_2
    //   442: getfield 1010	com/android/internal/os/BatteryStatsImpl:mClocks	Lcom/android/internal/os/BatteryStatsImpl$Clocks;
    //   445: invokeinterface 1091 1 0
    //   450: lstore 20
    //   452: aload_1
    //   453: astore 4
    //   455: aload_2
    //   456: getfield 1049	com/android/internal/os/BatteryStatsImpl:mMobileRadioActivePerAppTimer	Lcom/android/internal/os/BatteryStatsImpl$StopwatchTimer;
    //   459: ldc2_w 171
    //   462: lload 20
    //   464: lmul
    //   465: invokevirtual 4382	com/android/internal/os/BatteryStatsImpl$StopwatchTimer:getTimeSinceMarkLocked	(J)J
    //   468: lstore 22
    //   470: aload_1
    //   471: astore 4
    //   473: aload_2
    //   474: getfield 1049	com/android/internal/os/BatteryStatsImpl:mMobileRadioActivePerAppTimer	Lcom/android/internal/os/BatteryStatsImpl$StopwatchTimer;
    //   477: lload 20
    //   479: invokevirtual 4383	com/android/internal/os/BatteryStatsImpl$StopwatchTimer:setMark	(J)V
    //   482: aload_3
    //   483: ifnull +691 -> 1174
    //   486: aload_1
    //   487: astore 4
    //   489: new 4385	android/net/NetworkStats$Entry
    //   492: astore 5
    //   494: aload_1
    //   495: astore 4
    //   497: aload 5
    //   499: invokespecial 4386	android/net/NetworkStats$Entry:<init>	()V
    //   502: aload_1
    //   503: astore 4
    //   505: aload_3
    //   506: invokevirtual 4387	android/net/NetworkStats:size	()I
    //   509: istore 24
    //   511: lconst_0
    //   512: lstore 25
    //   514: lconst_0
    //   515: lstore 27
    //   517: iconst_0
    //   518: istore 7
    //   520: aload 5
    //   522: astore 4
    //   524: iload 7
    //   526: iload 24
    //   528: if_icmpge +260 -> 788
    //   531: aload_1
    //   532: astore 5
    //   534: aload_3
    //   535: iload 7
    //   537: aload 4
    //   539: invokevirtual 4391	android/net/NetworkStats:getValues	(ILandroid/net/NetworkStats$Entry;)Landroid/net/NetworkStats$Entry;
    //   542: astore 4
    //   544: aload_1
    //   545: astore 5
    //   547: aload 4
    //   549: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   552: lconst_0
    //   553: lcmp
    //   554: ifne +19 -> 573
    //   557: aload_1
    //   558: astore 5
    //   560: aload 4
    //   562: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   565: lconst_0
    //   566: lcmp
    //   567: ifne +6 -> 573
    //   570: goto +208 -> 778
    //   573: aload_1
    //   574: astore 5
    //   576: lload 27
    //   578: aload 4
    //   580: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   583: ladd
    //   584: lstore 27
    //   586: aload_1
    //   587: astore 5
    //   589: lload 25
    //   591: aload 4
    //   593: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   596: ladd
    //   597: lstore 25
    //   599: aload_1
    //   600: astore 5
    //   602: aload_2
    //   603: aload_2
    //   604: aload 4
    //   606: getfield 4398	android/net/NetworkStats$Entry:uid	I
    //   609: invokevirtual 1455	com/android/internal/os/BatteryStatsImpl:mapUid	(I)I
    //   612: invokevirtual 1593	com/android/internal/os/BatteryStatsImpl:getUidStatsLocked	(I)Lcom/android/internal/os/BatteryStatsImpl$Uid;
    //   615: astore 29
    //   617: aload_1
    //   618: astore 5
    //   620: aload 4
    //   622: getfield 4401	android/net/NetworkStats$Entry:rxBytes	J
    //   625: lstore 30
    //   627: aload_1
    //   628: astore_2
    //   629: aload 29
    //   631: iconst_0
    //   632: lload 30
    //   634: aload 4
    //   636: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   639: invokevirtual 4237	com/android/internal/os/BatteryStatsImpl$Uid:noteNetworkActivityLocked	(IJJ)V
    //   642: aload_1
    //   643: astore_2
    //   644: aload 29
    //   646: iconst_1
    //   647: aload 4
    //   649: getfield 4404	android/net/NetworkStats$Entry:txBytes	J
    //   652: aload 4
    //   654: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   657: invokevirtual 4237	com/android/internal/os/BatteryStatsImpl$Uid:noteNetworkActivityLocked	(IJJ)V
    //   660: aload_1
    //   661: astore_2
    //   662: aload 4
    //   664: getfield 4406	android/net/NetworkStats$Entry:set	I
    //   667: ifne +41 -> 708
    //   670: aload_1
    //   671: astore_2
    //   672: aload 29
    //   674: bipush 6
    //   676: aload 4
    //   678: getfield 4401	android/net/NetworkStats$Entry:rxBytes	J
    //   681: aload 4
    //   683: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   686: invokevirtual 4237	com/android/internal/os/BatteryStatsImpl$Uid:noteNetworkActivityLocked	(IJJ)V
    //   689: aload_1
    //   690: astore_2
    //   691: aload 29
    //   693: bipush 7
    //   695: aload 4
    //   697: getfield 4404	android/net/NetworkStats$Entry:txBytes	J
    //   700: aload 4
    //   702: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   705: invokevirtual 4237	com/android/internal/os/BatteryStatsImpl$Uid:noteNetworkActivityLocked	(IJJ)V
    //   708: aload_0
    //   709: astore_2
    //   710: aload_1
    //   711: astore 5
    //   713: aload_2
    //   714: getfield 802	com/android/internal/os/BatteryStatsImpl:mNetworkByteActivityCounters	[Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   717: iconst_0
    //   718: aaload
    //   719: aload 4
    //   721: getfield 4401	android/net/NetworkStats$Entry:rxBytes	J
    //   724: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   727: aload_1
    //   728: astore 5
    //   730: aload_2
    //   731: getfield 802	com/android/internal/os/BatteryStatsImpl:mNetworkByteActivityCounters	[Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   734: iconst_1
    //   735: aaload
    //   736: aload 4
    //   738: getfield 4404	android/net/NetworkStats$Entry:txBytes	J
    //   741: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   744: aload_1
    //   745: astore 5
    //   747: aload_2
    //   748: getfield 804	com/android/internal/os/BatteryStatsImpl:mNetworkPacketActivityCounters	[Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   751: iconst_0
    //   752: aaload
    //   753: aload 4
    //   755: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   758: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   761: aload_1
    //   762: astore 5
    //   764: aload_2
    //   765: getfield 804	com/android/internal/os/BatteryStatsImpl:mNetworkPacketActivityCounters	[Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   768: iconst_1
    //   769: aaload
    //   770: aload 4
    //   772: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   775: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   778: iinc 7 1
    //   781: goto -257 -> 524
    //   784: astore_1
    //   785: goto +393 -> 1178
    //   788: lload 27
    //   790: lload 25
    //   792: ladd
    //   793: lstore 20
    //   795: lload 20
    //   797: lconst_0
    //   798: lcmp
    //   799: ifle +332 -> 1131
    //   802: iconst_0
    //   803: istore 7
    //   805: aload 4
    //   807: astore 5
    //   809: iload 7
    //   811: iload 24
    //   813: if_icmpge +315 -> 1128
    //   816: aload_1
    //   817: astore 4
    //   819: aload_3
    //   820: iload 7
    //   822: aload 5
    //   824: invokevirtual 4391	android/net/NetworkStats:getValues	(ILandroid/net/NetworkStats$Entry;)Landroid/net/NetworkStats$Entry;
    //   827: astore 29
    //   829: aload_1
    //   830: astore 4
    //   832: aload 29
    //   834: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   837: lstore 30
    //   839: lload 30
    //   841: lconst_0
    //   842: lcmp
    //   843: ifne +23 -> 866
    //   846: aload_1
    //   847: astore 5
    //   849: aload 29
    //   851: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   854: lstore 30
    //   856: lload 30
    //   858: lconst_0
    //   859: lcmp
    //   860: ifne +6 -> 866
    //   863: goto +249 -> 1112
    //   866: aload_1
    //   867: astore 4
    //   869: aload_2
    //   870: aload_2
    //   871: aload 29
    //   873: getfield 4398	android/net/NetworkStats$Entry:uid	I
    //   876: invokevirtual 1455	com/android/internal/os/BatteryStatsImpl:mapUid	(I)I
    //   879: invokevirtual 1593	com/android/internal/os/BatteryStatsImpl:getUidStatsLocked	(I)Lcom/android/internal/os/BatteryStatsImpl$Uid;
    //   882: astore 5
    //   884: aload_1
    //   885: astore 4
    //   887: aload 29
    //   889: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   892: lstore 30
    //   894: aload_1
    //   895: astore 4
    //   897: lload 30
    //   899: aload 29
    //   901: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   904: ladd
    //   905: lstore 30
    //   907: aload_1
    //   908: astore 4
    //   910: lload 22
    //   912: lload 30
    //   914: lmul
    //   915: lload 20
    //   917: ldiv
    //   918: lstore 32
    //   920: aload_1
    //   921: astore 4
    //   923: aload 5
    //   925: lload 32
    //   927: invokevirtual 4409	com/android/internal/os/BatteryStatsImpl$Uid:noteMobileRadioActiveTimeLocked	(J)V
    //   930: lload 22
    //   932: lload 32
    //   934: lsub
    //   935: lstore 22
    //   937: aload_1
    //   938: astore_2
    //   939: aload_1
    //   940: ifnull +163 -> 1103
    //   943: aload_1
    //   944: astore 4
    //   946: aload 5
    //   948: invokevirtual 4412	com/android/internal/os/BatteryStatsImpl$Uid:getOrCreateModemControllerActivityLocked	()Lcom/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl;
    //   951: astore 5
    //   953: lload 27
    //   955: lconst_0
    //   956: lcmp
    //   957: ifle +51 -> 1008
    //   960: aload_1
    //   961: astore_2
    //   962: aload 29
    //   964: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   967: lconst_0
    //   968: lcmp
    //   969: ifle +36 -> 1005
    //   972: aload_1
    //   973: astore_2
    //   974: aload 29
    //   976: getfield 4394	android/net/NetworkStats$Entry:rxPackets	J
    //   979: aload_1
    //   980: invokevirtual 1469	android/telephony/ModemActivityInfo:getRxTimeMillis	()I
    //   983: i2l
    //   984: lmul
    //   985: lload 27
    //   987: ldiv
    //   988: lstore 32
    //   990: aload_1
    //   991: astore_2
    //   992: aload 5
    //   994: invokevirtual 4209	com/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl:getRxTimeCounter	()Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   997: lload 32
    //   999: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   1002: goto +6 -> 1008
    //   1005: goto +3 -> 1008
    //   1008: aload_1
    //   1009: astore_2
    //   1010: lload 25
    //   1012: lconst_0
    //   1013: lcmp
    //   1014: ifle +89 -> 1103
    //   1017: aload_1
    //   1018: astore_2
    //   1019: aload_1
    //   1020: astore 4
    //   1022: aload 29
    //   1024: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   1027: lconst_0
    //   1028: lcmp
    //   1029: ifle +74 -> 1103
    //   1032: iconst_0
    //   1033: istore 34
    //   1035: aload_1
    //   1036: astore_2
    //   1037: iload 34
    //   1039: iconst_5
    //   1040: if_icmpge +63 -> 1103
    //   1043: aload_1
    //   1044: astore 4
    //   1046: aload 29
    //   1048: getfield 4397	android/net/NetworkStats$Entry:txPackets	J
    //   1051: lstore 35
    //   1053: aload_1
    //   1054: astore 4
    //   1056: aload_1
    //   1057: invokevirtual 1265	android/telephony/ModemActivityInfo:getTxTimeMillis	()[I
    //   1060: iload 34
    //   1062: iaload
    //   1063: istore 37
    //   1065: iload 37
    //   1067: i2l
    //   1068: lstore 32
    //   1070: lload 35
    //   1072: lload 32
    //   1074: lmul
    //   1075: lload 25
    //   1077: ldiv
    //   1078: lstore 32
    //   1080: aload 5
    //   1082: invokevirtual 4212	com/android/internal/os/BatteryStatsImpl$ControllerActivityCounterImpl:getTxTimeCounters	()[Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   1085: iload 34
    //   1087: aaload
    //   1088: lload 32
    //   1090: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   1093: iinc 34 1
    //   1096: goto -61 -> 1035
    //   1099: astore_1
    //   1100: goto -315 -> 785
    //   1103: aload_2
    //   1104: astore_1
    //   1105: lload 20
    //   1107: lload 30
    //   1109: lsub
    //   1110: lstore 20
    //   1112: iinc 7 1
    //   1115: aload_0
    //   1116: astore_2
    //   1117: aload 29
    //   1119: astore 5
    //   1121: goto -312 -> 809
    //   1124: astore_1
    //   1125: goto +53 -> 1178
    //   1128: goto +3 -> 1131
    //   1131: lload 22
    //   1133: lconst_0
    //   1134: lcmp
    //   1135: ifle +25 -> 1160
    //   1138: aload_0
    //   1139: astore_1
    //   1140: aload_1
    //   1141: getfield 1053	com/android/internal/os/BatteryStatsImpl:mMobileRadioActiveUnknownTime	Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   1144: lload 22
    //   1146: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   1149: aload_1
    //   1150: getfield 1055	com/android/internal/os/BatteryStatsImpl:mMobileRadioActiveUnknownCount	Lcom/android/internal/os/BatteryStatsImpl$LongSamplingCounter;
    //   1153: lconst_1
    //   1154: invokevirtual 3162	com/android/internal/os/BatteryStatsImpl$LongSamplingCounter:addCountLocked	(J)V
    //   1157: goto +3 -> 1160
    //   1160: aload_0
    //   1161: getfield 905	com/android/internal/os/BatteryStatsImpl:mNetworkStatsPool	Landroid/util/Pools$Pool;
    //   1164: aload_3
    //   1165: invokeinterface 4369 2 0
    //   1170: pop
    //   1171: goto +3 -> 1174
    //   1174: aload_0
    //   1175: monitorexit
    //   1176: return
    //   1177: astore_1
    //   1178: aload_0
    //   1179: monitorexit
    //   1180: aload_1
    //   1181: athrow
    //   1182: astore_1
    //   1183: goto -5 -> 1178
    //   1186: astore_1
    //   1187: aload 4
    //   1189: monitorexit
    //   1190: aload_1
    //   1191: athrow
    //   1192: astore_1
    //   1193: goto -6 -> 1187
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	1196	0	this	BatteryStatsImpl
    //   0	1196	1	paramModemActivityInfo	ModemActivityInfo
    //   1	1116	2	localObject1	Object
    //   14	1151	3	localNetworkStats	NetworkStats
    //   19	1169	4	localObject2	Object
    //   32	1088	5	localObject3	Object
    //   102	3	6	bool	boolean
    //   207	906	7	i	int
    //   275	158	8	d1	double
    //   290	64	10	d2	double
    //   300	57	12	d3	double
    //   315	44	14	d4	double
    //   325	38	16	d5	double
    //   340	25	18	d6	double
    //   450	661	20	l1	long
    //   468	677	22	l2	long
    //   509	305	24	j	int
    //   512	564	25	l3	long
    //   515	471	27	l4	long
    //   615	503	29	localObject4	Object
    //   625	483	30	l5	long
    //   918	171	32	l6	long
    //   1033	61	34	k	int
    //   1051	20	35	l7	long
    //   1063	3	37	m	int
    // Exception table:
    //   from	to	target	type
    //   39	83	86	finally
    //   116	127	130	finally
    //   137	139	130	finally
    //   147	152	130	finally
    //   155	170	130	finally
    //   173	188	130	finally
    //   191	206	130	finally
    //   218	239	130	finally
    //   248	267	130	finally
    //   270	277	130	finally
    //   280	292	130	finally
    //   295	302	130	finally
    //   305	317	130	finally
    //   320	327	130	finally
    //   330	342	130	finally
    //   345	351	130	finally
    //   376	389	130	finally
    //   392	416	130	finally
    //   425	438	130	finally
    //   534	544	130	finally
    //   547	557	130	finally
    //   560	570	130	finally
    //   576	586	130	finally
    //   589	599	130	finally
    //   602	617	130	finally
    //   620	627	130	finally
    //   713	727	130	finally
    //   730	744	130	finally
    //   747	761	130	finally
    //   764	778	130	finally
    //   849	856	130	finally
    //   629	642	784	finally
    //   644	660	784	finally
    //   662	670	784	finally
    //   672	689	784	finally
    //   691	708	784	finally
    //   962	972	784	finally
    //   974	990	784	finally
    //   992	1002	784	finally
    //   1070	1093	1099	finally
    //   897	907	1124	finally
    //   910	920	1124	finally
    //   923	930	1124	finally
    //   946	953	1124	finally
    //   1022	1032	1124	finally
    //   1046	1053	1124	finally
    //   1056	1065	1124	finally
    //   98	104	1177	finally
    //   441	452	1177	finally
    //   455	470	1177	finally
    //   473	482	1177	finally
    //   489	494	1177	finally
    //   497	502	1177	finally
    //   505	511	1177	finally
    //   819	829	1177	finally
    //   832	839	1177	finally
    //   869	884	1177	finally
    //   887	894	1177	finally
    //   1140	1157	1182	finally
    //   1160	1171	1182	finally
    //   1174	1176	1182	finally
    //   1178	1180	1182	finally
    //   24	34	1186	finally
    //   90	93	1186	finally
    //   1187	1190	1192	finally
  }
  
  public void updateProcStateCpuTimes(boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      if (!mConstants.TRACK_CPU_TIMES_BY_PROC_STATE) {
        return;
      }
      if (!initKernelSingleUidTimeReaderLocked()) {
        return;
      }
      if (mKernelSingleUidTimeReader.hasStaleData())
      {
        mPendingUids.clear();
        return;
      }
      if (mPendingUids.size() == 0) {
        return;
      }
      SparseIntArray localSparseIntArray = mPendingUids.clone();
      mPendingUids.clear();
      int i = localSparseIntArray.size() - 1;
      while (i >= 0)
      {
        int j = localSparseIntArray.keyAt(i);
        int k = localSparseIntArray.valueAt(i);
        try
        {
          Uid localUid = getAvailableUidStatsLocked(j);
          if (localUid == null) {
            break label292;
          }
          Object localObject1;
          int m;
          if (mChildUids == null)
          {
            localObject1 = null;
          }
          else
          {
            localObject2 = mChildUids.toArray();
            for (m = localObject2.length - 1;; m--)
            {
              localObject1 = localObject2;
              if (m < 0) {
                break;
              }
              localObject2[m] = mChildUids.get(m);
            }
          }
          Object localObject2 = mKernelSingleUidTimeReader.readDeltaMs(j);
          Object localObject6 = localObject2;
          if (localObject1 != null) {
            for (m = localObject1.length - 1;; m--)
            {
              localObject6 = localObject2;
              if (m < 0) {
                break;
              }
              localObject2 = addCpuTimes((long[])localObject2, mKernelSingleUidTimeReader.readDeltaMs(localObject1[m]));
            }
          }
          if ((paramBoolean1) && (localObject6 != null)) {
            try
            {
              localUid.addProcStateTimesMs(k, localObject6, paramBoolean1);
              localUid.addProcStateScreenOffTimesMs(k, localObject6, paramBoolean2);
            }
            finally {}
          }
          label292:
          i--;
        }
        finally {}
      }
      return;
    }
    finally {}
  }
  
  public void updateRpmStatsLocked()
  {
    if (mPlatformIdleStateCallback == null) {
      return;
    }
    long l = SystemClock.elapsedRealtime();
    if (l - mLastRpmStatsUpdateTimeMs >= 1000L)
    {
      mPlatformIdleStateCallback.fillLowPowerStats(mTmpRpmStats);
      mLastRpmStatsUpdateTimeMs = l;
    }
    Object localObject1 = mTmpRpmStats.mPlatformLowPowerStats.entrySet().iterator();
    Object localObject2;
    int i;
    Object localObject4;
    Object localObject5;
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      localObject3 = (String)((Map.Entry)localObject2).getKey();
      l = getValuemTimeMs;
      i = getValuemCount;
      getRpmTimerLocked((String)localObject3).update(l * 1000L, i);
      localObject4 = getValuemVoters.entrySet().iterator();
      while (((Iterator)localObject4).hasNext())
      {
        localObject2 = (Map.Entry)((Iterator)localObject4).next();
        localObject5 = new StringBuilder();
        ((StringBuilder)localObject5).append((String)localObject3);
        ((StringBuilder)localObject5).append(".");
        ((StringBuilder)localObject5).append((String)((Map.Entry)localObject2).getKey());
        localObject5 = ((StringBuilder)localObject5).toString();
        l = getValuemTimeMs;
        i = getValuemCount;
        getRpmTimerLocked((String)localObject5).update(l * 1000L, i);
      }
    }
    Object localObject3 = mTmpRpmStats.mSubsystemLowPowerStats.entrySet().iterator();
    while (((Iterator)localObject3).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject3).next();
      localObject1 = (String)((Map.Entry)localObject2).getKey();
      localObject2 = getValuemStates.entrySet().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject4 = (Map.Entry)((Iterator)localObject2).next();
        localObject5 = new StringBuilder();
        ((StringBuilder)localObject5).append((String)localObject1);
        ((StringBuilder)localObject5).append(".");
        ((StringBuilder)localObject5).append((String)((Map.Entry)localObject4).getKey());
        localObject5 = ((StringBuilder)localObject5).toString();
        l = getValuemTimeMs;
        i = getValuemCount;
        getRpmTimerLocked((String)localObject5).update(l * 1000L, i);
      }
    }
  }
  
  @GuardedBy("this")
  public void updateTimeBasesLocked(boolean paramBoolean, int paramInt, long paramLong1, long paramLong2)
  {
    boolean bool = isScreenOn(paramInt) ^ true;
    int i;
    if (paramBoolean != mOnBatteryTimeBase.isRunning()) {
      i = 1;
    } else {
      i = 0;
    }
    int j;
    if ((paramBoolean) && (bool)) {
      j = 1;
    } else {
      j = 0;
    }
    if (j != mOnBatteryScreenOffTimeBase.isRunning()) {
      paramInt = 1;
    } else {
      paramInt = 0;
    }
    if ((paramInt != 0) || (i != 0))
    {
      if (paramInt != 0)
      {
        updateKernelWakelocksLocked();
        updateBatteryPropertiesLocked();
      }
      if (i != 0) {
        updateRpmStatsLocked();
      }
      mOnBatteryTimeBase.setRunning(paramBoolean, paramLong1, paramLong2);
      if (i != 0) {
        for (i = mUidStats.size() - 1; i >= 0; i--) {
          ((Uid)mUidStats.valueAt(i)).updateOnBatteryBgTimeBase(paramLong1, paramLong2);
        }
      }
      if (paramInt != 0)
      {
        TimeBase localTimeBase = mOnBatteryScreenOffTimeBase;
        if ((paramBoolean) && (bool)) {
          paramBoolean = true;
        } else {
          paramBoolean = false;
        }
        localTimeBase.setRunning(paramBoolean, paramLong1, paramLong2);
        for (paramInt = mUidStats.size() - 1; paramInt >= 0; paramInt--) {
          ((Uid)mUidStats.valueAt(paramInt)).updateOnBatteryScreenOffBgTimeBase(paramLong1, paramLong2);
        }
      }
    }
  }
  
  public void updateWifiState(WifiActivityEnergyInfo paramWifiActivityEnergyInfo)
  {
    Object localObject1 = null;
    synchronized (mWifiNetworkLock)
    {
      Object localObject3 = readNetworkStatsLocked(mWifiIfaces);
      if (localObject3 != null)
      {
        localObject1 = NetworkStats.subtract((NetworkStats)localObject3, mLastWifiNetworkStats, null, null, (NetworkStats)mNetworkStatsPool.acquire());
        mNetworkStatsPool.release(mLastWifiNetworkStats);
        mLastWifiNetworkStats = ((NetworkStats)localObject3);
      }
      try
      {
        if (!mOnBatteryInternal)
        {
          if (localObject1 != null) {
            mNetworkStatsPool.release(localObject1);
          }
          return;
        }
        long l1 = mClocks.elapsedRealtime();
        localObject3 = new android/util/SparseLongArray;
        ((SparseLongArray)localObject3).<init>();
        Object localObject4 = new android/util/SparseLongArray;
        ((SparseLongArray)localObject4).<init>();
        long l2 = 0L;
        long l3;
        long l4;
        long l5;
        Object localObject5;
        if (localObject1 != null)
        {
          ??? = new android/net/NetworkStats$Entry;
          ((NetworkStats.Entry)???).<init>();
          i = ((NetworkStats)localObject1).size();
          l3 = 0L;
          l4 = 0L;
          j = 0;
          while (j < i)
          {
            ??? = ((NetworkStats)localObject1).getValues(j, (NetworkStats.Entry)???);
            if ((rxBytes == 0L) && (txBytes == 0L))
            {
              l5 = l4;
              l2 = l3;
            }
            else
            {
              localObject5 = getUidStatsLocked(mapUid(uid));
              if (rxBytes != 0L)
              {
                ((Uid)localObject5).noteNetworkActivityLocked(2, rxBytes, rxPackets);
                if (set == 0) {
                  ((Uid)localObject5).noteNetworkActivityLocked(8, rxBytes, rxPackets);
                }
                mNetworkByteActivityCounters[2].addCountLocked(rxBytes);
                mNetworkPacketActivityCounters[2].addCountLocked(rxPackets);
                ((SparseLongArray)localObject3).put(((Uid)localObject5).getUid(), rxPackets);
                l3 += rxPackets;
              }
              l5 = l4;
              l2 = l3;
              if (txBytes != 0L)
              {
                ((Uid)localObject5).noteNetworkActivityLocked(3, txBytes, txPackets);
                if (set == 0) {
                  ((Uid)localObject5).noteNetworkActivityLocked(9, txBytes, txPackets);
                }
                mNetworkByteActivityCounters[3].addCountLocked(txBytes);
                mNetworkPacketActivityCounters[3].addCountLocked(txPackets);
                ((SparseLongArray)localObject4).put(((Uid)localObject5).getUid(), txPackets);
                l5 = l4 + txPackets;
                l2 = l3;
              }
            }
            j++;
            l4 = l5;
            l3 = l2;
          }
          l2 = l1;
          mNetworkStatsPool.release(localObject1);
          localObject1 = null;
          l1 = l4;
          l4 = l3;
          l3 = l2;
        }
        else
        {
          l3 = l1;
          l4 = 0L;
          l1 = l2;
        }
        if (paramWifiActivityEnergyInfo != null) {}
        for (;;)
        {
          try
          {
            mHasWifiReporting = true;
            l6 = paramWifiActivityEnergyInfo.getControllerTxTimeMillis();
            l7 = paramWifiActivityEnergyInfo.getControllerRxTimeMillis();
            l8 = paramWifiActivityEnergyInfo.getControllerScanTimeMillis();
            l9 = paramWifiActivityEnergyInfo.getControllerIdleTimeMillis();
            l10 = l7;
            l11 = l6;
            l5 = 0L;
            i = mUidStats.size();
            l2 = 0L;
            j = 0;
            if (j < i)
            {
              ??? = localObject1;
              localObject1 = ???;
            }
          }
          finally
          {
            long l6;
            long l7;
            long l8;
            long l9;
            long l10;
            long l11;
            int k;
            double d;
            break label1142;
          }
          try
          {
            localObject5 = (Uid)mUidStats.valueAt(j);
            localObject1 = ???;
            l5 += mWifiScanTimer.getTimeSinceMarkLocked(l3 * 1000L) / 1000L;
            localObject1 = ???;
            l2 += mFullWifiLockTimer.getTimeSinceMarkLocked(l3 * 1000L) / 1000L;
            j++;
            localObject1 = ???;
          }
          finally
          {
            break label1142;
          }
        }
        ??? = localObject1;
        k = 0;
        int j = i;
        for (int i = k; i < j; i++)
        {
          localObject1 = ???;
          Uid localUid = (Uid)mUidStats.valueAt(i);
          localObject1 = ???;
          long l12 = mWifiScanTimer.getTimeSinceMarkLocked(l3 * 1000L) / 1000L;
          if (l12 > 0L)
          {
            localObject1 = ???;
            mWifiScanTimer.setMark(l3);
            long l13 = l12;
            l8 = l13;
            if (l5 > l7)
            {
              localObject1 = ???;
              l8 = l7 * l13 / l5;
            }
            l13 = l12;
            if (l5 > l6)
            {
              localObject1 = ???;
              l13 = l6 * l12 / l5;
            }
            localObject1 = ???;
            localObject5 = localUid.getOrCreateWifiControllerActivityLocked();
            localObject1 = ???;
            ((ControllerActivityCounterImpl)localObject5).getRxTimeCounter().addCountLocked(l8);
            localObject1 = ???;
            localObject5.getTxTimeCounters()[0].addCountLocked(l13);
            l10 -= l8;
            l11 -= l13;
          }
          localObject1 = ???;
          l8 = mFullWifiLockTimer.getTimeSinceMarkLocked(l3 * 1000L) / 1000L;
          if (l8 > 0L)
          {
            localObject1 = ???;
            mFullWifiLockTimer.setMark(l3);
            localObject1 = ???;
            l8 = l8 * l9 / l2;
            localObject1 = ???;
            localUid.getOrCreateWifiControllerActivityLocked().getIdleTimeCounter().addCountLocked(l8);
          }
        }
        for (j = 0;; j++)
        {
          localObject1 = ???;
          if (j >= ((SparseLongArray)localObject4).size()) {
            break;
          }
          localObject1 = ???;
          localObject5 = getUidStatsLocked(((SparseLongArray)localObject4).keyAt(j));
          localObject1 = ???;
          l3 = ((SparseLongArray)localObject4).valueAt(j) * l11 / l1;
          localObject1 = ???;
          localObject5.getOrCreateWifiControllerActivityLocked().getTxTimeCounters()[0].addCountLocked(l3);
        }
        for (j = 0;; j++)
        {
          localObject1 = ???;
          if (j >= ((SparseLongArray)localObject3).size()) {
            break;
          }
          localObject1 = ???;
          localObject4 = getUidStatsLocked(((SparseLongArray)localObject3).keyAt(j));
          localObject1 = ???;
          l3 = ((SparseLongArray)localObject3).valueAt(j) * l10 / l4;
          localObject1 = ???;
          ((Uid)localObject4).getOrCreateWifiControllerActivityLocked().getRxTimeCounter().addCountLocked(l3);
        }
        localObject1 = ???;
        mWifiActivity.getRxTimeCounter().addCountLocked(paramWifiActivityEnergyInfo.getControllerRxTimeMillis());
        localObject1 = ???;
        mWifiActivity.getTxTimeCounters()[0].addCountLocked(paramWifiActivityEnergyInfo.getControllerTxTimeMillis());
        localObject1 = ???;
        mWifiActivity.getScanTimeCounter().addCountLocked(paramWifiActivityEnergyInfo.getControllerScanTimeMillis());
        localObject1 = ???;
        mWifiActivity.getIdleTimeCounter().addCountLocked(paramWifiActivityEnergyInfo.getControllerIdleTimeMillis());
        localObject1 = ???;
        d = mPowerProfile.getAveragePower("wifi.controller.voltage") / 1000.0D;
        localObject1 = ???;
        if (d != 0.0D)
        {
          localObject1 = ???;
          mWifiActivity.getPowerCounter().addCountLocked((paramWifiActivityEnergyInfo.getControllerEnergyUsed() / d));
          localObject1 = ???;
        }
        return;
      }
      finally {}
      label1142:
      throw paramWifiActivityEnergyInfo;
    }
  }
  
  public void writeAsyncLocked()
  {
    writeLocked(false);
  }
  
  void writeHistory(Parcel paramParcel, boolean paramBoolean1, boolean paramBoolean2)
  {
    paramParcel.writeLong(mHistoryBaseTime + mLastHistoryElapsedRealtime);
    if (!paramBoolean1)
    {
      paramParcel.writeInt(0);
      paramParcel.writeInt(0);
      return;
    }
    paramParcel.writeInt(mHistoryTagPool.size());
    Iterator localIterator = mHistoryTagPool.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      BatteryStats.HistoryTag localHistoryTag = (BatteryStats.HistoryTag)localEntry.getKey();
      paramParcel.writeInt(((Integer)localEntry.getValue()).intValue());
      paramParcel.writeString(string);
      paramParcel.writeInt(uid);
    }
    paramParcel.writeInt(mHistoryBuffer.dataSize());
    paramParcel.appendFrom(mHistoryBuffer, 0, mHistoryBuffer.dataSize());
    if (paramBoolean2) {
      writeOldHistory(paramParcel);
    }
  }
  
  public void writeHistoryDelta(Parcel paramParcel, BatteryStats.HistoryItem paramHistoryItem1, BatteryStats.HistoryItem paramHistoryItem2)
  {
    if ((paramHistoryItem2 != null) && (cmd == 0))
    {
      long l = time - time;
      int i = buildBatteryLevelInt(paramHistoryItem2);
      int j = buildStateInt(paramHistoryItem2);
      int k;
      if ((l >= 0L) && (l <= 2147483647L))
      {
        if (l >= 524285L) {
          k = 524286;
        } else {
          k = (int)l;
        }
      }
      else {
        k = 524287;
      }
      int m = states & 0xFE000000 | k;
      int n;
      if (mLastHistoryStepLevel > batteryLevel) {
        n = 1;
      } else {
        n = 0;
      }
      int i1;
      if ((n == 0) && (mLastHistoryStepDetails != null)) {
        i1 = 0;
      } else {
        i1 = 1;
      }
      int i2 = buildBatteryLevelInt(paramHistoryItem1) | n;
      int i3;
      if (i2 != i) {
        i3 = 1;
      } else {
        i3 = 0;
      }
      i = m;
      if (i3 != 0) {
        i = m | 0x80000;
      }
      int i4 = buildStateInt(paramHistoryItem1);
      int i5;
      if (i4 != j) {
        i5 = 1;
      } else {
        i5 = 0;
      }
      m = i;
      if (i5 != 0) {
        m = i | 0x100000;
      }
      int i6;
      if (states2 != states2) {
        i6 = 1;
      } else {
        i6 = 0;
      }
      i = m;
      if (i6 != 0) {
        i = m | 0x200000;
      }
      if (wakelockTag == null)
      {
        j = i;
        if (wakeReasonTag == null) {}
      }
      else
      {
        j = i | 0x400000;
      }
      m = j;
      if (eventCode != 0) {
        m = j | 0x800000;
      }
      if (batteryChargeUAh != batteryChargeUAh) {
        i = 1;
      } else {
        i = 0;
      }
      j = m;
      if (i != 0) {
        j = m | 0x1000000;
      }
      paramParcel.writeInt(j);
      if (k >= 524286) {
        if (k == 524286) {
          paramParcel.writeInt((int)l);
        } else {
          paramParcel.writeLong(l);
        }
      }
      if (i3 != 0) {
        paramParcel.writeInt(i2);
      }
      if (i5 != 0) {
        paramParcel.writeInt(i4);
      }
      if (i6 != 0) {
        paramParcel.writeInt(states2);
      }
      if ((wakelockTag != null) || (wakeReasonTag != null))
      {
        if (wakelockTag != null) {
          k = writeHistoryTag(wakelockTag);
        } else {
          k = 65535;
        }
        if (wakeReasonTag != null) {
          m = writeHistoryTag(wakeReasonTag);
        } else {
          m = 65535;
        }
        paramParcel.writeInt(m << 16 | k);
      }
      if (eventCode != 0)
      {
        k = writeHistoryTag(eventTag);
        paramParcel.writeInt(eventCode & 0xFFFF | k << 16);
      }
      if (i1 != 0)
      {
        if (mPlatformIdleStateCallback != null)
        {
          mCurHistoryStepDetails.statPlatformIdleState = mPlatformIdleStateCallback.getPlatformLowPowerStats();
          mCurHistoryStepDetails.statSubsystemPowerState = mPlatformIdleStateCallback.getSubsystemLowPowerStats();
        }
        computeHistoryStepDetails(mCurHistoryStepDetails, mLastHistoryStepDetails);
        if (n != 0) {
          mCurHistoryStepDetails.writeToParcel(paramParcel);
        }
        stepDetails = mCurHistoryStepDetails;
        mLastHistoryStepDetails = mCurHistoryStepDetails;
      }
      else
      {
        stepDetails = null;
      }
      if (mLastHistoryStepLevel < batteryLevel) {
        mLastHistoryStepDetails = null;
      }
      mLastHistoryStepLevel = ((byte)batteryLevel);
      if (i != 0) {
        paramParcel.writeInt(batteryChargeUAh);
      }
      return;
    }
    paramParcel.writeInt(524285);
    paramHistoryItem1.writeToParcel(paramParcel, 0);
  }
  
  void writeLocked(boolean paramBoolean)
  {
    if (mFile == null)
    {
      Slog.w("BatteryStats", "writeLocked: no file associated with this instance");
      return;
    }
    if (mShuttingDown) {
      return;
    }
    Parcel localParcel = Parcel.obtain();
    writeSummaryToParcel(localParcel, true);
    mLastWriteTime = mClocks.elapsedRealtime();
    if (mPendingWrite != null) {
      mPendingWrite.recycle();
    }
    mPendingWrite = localParcel;
    if (paramBoolean) {
      commitPendingDataToDisk();
    } else {
      BackgroundThread.getHandler().post(new Runnable()
      {
        public void run()
        {
          commitPendingDataToDisk();
        }
      });
    }
  }
  
  void writeOldHistory(Parcel paramParcel) {}
  
  public void writeSummaryToParcel(Parcel paramParcel, boolean paramBoolean)
  {
    Object localObject1 = this;
    pullPendingStateUpdatesLocked();
    long l1 = getStartClockTime();
    long l2 = mClocks.uptimeMillis() * 1000L;
    long l3 = mClocks.elapsedRealtime() * 1000L;
    paramParcel.writeInt(177);
    ((BatteryStatsImpl)localObject1).writeHistory(paramParcel, paramBoolean, true);
    paramParcel.writeInt(mStartCount);
    paramParcel.writeLong(((BatteryStatsImpl)localObject1).computeUptime(l2, 0));
    paramParcel.writeLong(((BatteryStatsImpl)localObject1).computeRealtime(l3, 0));
    paramParcel.writeLong(l1);
    paramParcel.writeString(mStartPlatformVersion);
    paramParcel.writeString(mEndPlatformVersion);
    mOnBatteryTimeBase.writeSummaryToParcel(paramParcel, l2, l3);
    mOnBatteryScreenOffTimeBase.writeSummaryToParcel(paramParcel, l2, l3);
    paramParcel.writeInt(mDischargeUnplugLevel);
    paramParcel.writeInt(mDischargePlugLevel);
    paramParcel.writeInt(mDischargeCurrentLevel);
    paramParcel.writeInt(mCurrentBatteryLevel);
    paramParcel.writeInt(mEstimatedBatteryCapacity);
    paramParcel.writeInt(mMinLearnedBatteryCapacity);
    paramParcel.writeInt(mMaxLearnedBatteryCapacity);
    paramParcel.writeInt(getLowDischargeAmountSinceCharge());
    paramParcel.writeInt(getHighDischargeAmountSinceCharge());
    paramParcel.writeInt(getDischargeAmountScreenOnSinceCharge());
    paramParcel.writeInt(getDischargeAmountScreenOffSinceCharge());
    paramParcel.writeInt(getDischargeAmountScreenDozeSinceCharge());
    mDischargeStepTracker.writeToParcel(paramParcel);
    mChargeStepTracker.writeToParcel(paramParcel);
    mDailyDischargeStepTracker.writeToParcel(paramParcel);
    mDailyChargeStepTracker.writeToParcel(paramParcel);
    mDischargeCounter.writeSummaryFromParcelLocked(paramParcel);
    mDischargeScreenOffCounter.writeSummaryFromParcelLocked(paramParcel);
    mDischargeScreenDozeCounter.writeSummaryFromParcelLocked(paramParcel);
    mDischargeLightDozeCounter.writeSummaryFromParcelLocked(paramParcel);
    mDischargeDeepDozeCounter.writeSummaryFromParcelLocked(paramParcel);
    if (mDailyPackageChanges != null)
    {
      i = mDailyPackageChanges.size();
      paramParcel.writeInt(i);
      for (j = 0; j < i; j++)
      {
        localObject2 = (BatteryStats.PackageChange)mDailyPackageChanges.get(j);
        paramParcel.writeString(mPackageName);
        paramParcel.writeInt(mUpdate);
        paramParcel.writeLong(mVersionCode);
      }
    }
    else
    {
      paramParcel.writeInt(0);
    }
    paramParcel.writeLong(mDailyStartTime);
    paramParcel.writeLong(mNextMinDailyDeadline);
    paramParcel.writeLong(mNextMaxDailyDeadline);
    mScreenOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mScreenDozeTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    for (int j = 0; j < 5; j++) {
      mScreenBrightnessTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
    }
    mInteractiveTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mPowerSaveModeEnabledTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    paramParcel.writeLong(mLongestLightIdleTime);
    paramParcel.writeLong(mLongestFullIdleTime);
    mDeviceIdleModeLightTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mDeviceIdleModeFullTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mDeviceLightIdlingTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mDeviceIdlingTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mPhoneOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    for (j = 0; j < 6; j++) {
      mPhoneSignalStrengthsTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
    }
    mPhoneSignalScanningTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    for (j = 0; j < 21; j++) {
      mPhoneDataConnectionsTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
    }
    for (j = 0; j < 10; j++)
    {
      mNetworkByteActivityCounters[j].writeSummaryFromParcelLocked(paramParcel);
      mNetworkPacketActivityCounters[j].writeSummaryFromParcelLocked(paramParcel);
    }
    mMobileRadioActiveTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mMobileRadioActivePerAppTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mMobileRadioActiveAdjustedTime.writeSummaryFromParcelLocked(paramParcel);
    mMobileRadioActiveUnknownTime.writeSummaryFromParcelLocked(paramParcel);
    mMobileRadioActiveUnknownCount.writeSummaryFromParcelLocked(paramParcel);
    mWifiMulticastWakelockTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mWifiOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mGlobalWifiRunningTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    for (j = 0; j < 8; j++) {
      mWifiStateTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
    }
    for (j = 0; j < 13; j++) {
      mWifiSupplStateTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
    }
    for (j = 0; j < 5; j++) {
      mWifiSignalStrengthsTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
    }
    mWifiActiveTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mWifiActivity.writeSummaryToParcel(paramParcel);
    for (j = 0; j < 2; j++) {
      mGpsSignalQualityTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
    }
    mBluetoothActivity.writeSummaryToParcel(paramParcel);
    mModemActivity.writeSummaryToParcel(paramParcel);
    paramParcel.writeInt(mHasWifiReporting);
    paramParcel.writeInt(mHasBluetoothReporting);
    paramParcel.writeInt(mHasModemReporting);
    paramParcel.writeInt(mNumConnectivityChange);
    mFlashlightOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mCameraOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    mBluetoothScanTimer.writeSummaryFromParcelLocked(paramParcel, l3);
    paramParcel.writeInt(mRpmStats.size());
    Object localObject3 = mRpmStats.entrySet().iterator();
    while (((Iterator)localObject3).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject3).next();
      localObject4 = (Timer)((Map.Entry)localObject2).getValue();
      if (localObject4 != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString((String)((Map.Entry)localObject2).getKey());
        ((Timer)localObject4).writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    paramParcel.writeInt(mScreenOffRpmStats.size());
    Object localObject4 = mScreenOffRpmStats.entrySet().iterator();
    while (((Iterator)localObject4).hasNext())
    {
      localObject3 = (Map.Entry)((Iterator)localObject4).next();
      localObject2 = (Timer)((Map.Entry)localObject3).getValue();
      if (localObject2 != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString((String)((Map.Entry)localObject3).getKey());
        ((Timer)localObject2).writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    paramParcel.writeInt(mKernelWakelockStats.size());
    Object localObject2 = mKernelWakelockStats.entrySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (Map.Entry)((Iterator)localObject2).next();
      localObject4 = (Timer)((Map.Entry)localObject3).getValue();
      if (localObject4 != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString((String)((Map.Entry)localObject3).getKey());
        ((Timer)localObject4).writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    paramParcel.writeInt(mWakeupReasonStats.size());
    localObject4 = mWakeupReasonStats.entrySet().iterator();
    while (((Iterator)localObject4).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject4).next();
      localObject3 = (SamplingTimer)((Map.Entry)localObject2).getValue();
      if (localObject3 != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString((String)((Map.Entry)localObject2).getKey());
        ((SamplingTimer)localObject3).writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    paramParcel.writeInt(mKernelMemoryStats.size());
    for (j = 0; j < mKernelMemoryStats.size(); j++)
    {
      localObject2 = (Timer)mKernelMemoryStats.valueAt(j);
      if (localObject2 != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeLong(mKernelMemoryStats.keyAt(j));
        ((Timer)localObject2).writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    int i = mUidStats.size();
    paramParcel.writeInt(i);
    for (int k = 0;; k++)
    {
      localObject1 = this;
      if (k >= i) {
        break;
      }
      paramParcel.writeInt(mUidStats.keyAt(k));
      localObject4 = (Uid)mUidStats.valueAt(k);
      mOnBatteryBackgroundTimeBase.writeSummaryToParcel(paramParcel, l2, l3);
      mOnBatteryScreenOffBackgroundTimeBase.writeSummaryToParcel(paramParcel, l2, l3);
      if (mWifiRunningTimer != null)
      {
        paramParcel.writeInt(1);
        mWifiRunningTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mFullWifiLockTimer != null)
      {
        paramParcel.writeInt(1);
        mFullWifiLockTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mWifiScanTimer != null)
      {
        paramParcel.writeInt(1);
        mWifiScanTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      for (j = 0; j < 5; j++) {
        if (mWifiBatchedScanTimer[j] != null)
        {
          paramParcel.writeInt(1);
          mWifiBatchedScanTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(0);
        }
      }
      if (mWifiMulticastTimer != null)
      {
        paramParcel.writeInt(1);
        mWifiMulticastTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mAudioTurnedOnTimer != null)
      {
        paramParcel.writeInt(1);
        mAudioTurnedOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mVideoTurnedOnTimer != null)
      {
        paramParcel.writeInt(1);
        mVideoTurnedOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mFlashlightTurnedOnTimer != null)
      {
        paramParcel.writeInt(1);
        mFlashlightTurnedOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mCameraTurnedOnTimer != null)
      {
        paramParcel.writeInt(1);
        mCameraTurnedOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mForegroundActivityTimer != null)
      {
        paramParcel.writeInt(1);
        mForegroundActivityTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mForegroundServiceTimer != null)
      {
        paramParcel.writeInt(1);
        mForegroundServiceTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mAggregatedPartialWakelockTimer != null)
      {
        paramParcel.writeInt(1);
        mAggregatedPartialWakelockTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothScanTimer != null)
      {
        paramParcel.writeInt(1);
        mBluetoothScanTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothUnoptimizedScanTimer != null)
      {
        paramParcel.writeInt(1);
        mBluetoothUnoptimizedScanTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothScanResultCounter != null)
      {
        paramParcel.writeInt(1);
        mBluetoothScanResultCounter.writeSummaryFromParcelLocked(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothScanResultBgCounter != null)
      {
        paramParcel.writeInt(1);
        mBluetoothScanResultBgCounter.writeSummaryFromParcelLocked(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      for (j = 0; j < 7; j++) {
        if (mProcessStateTimer[j] != null)
        {
          paramParcel.writeInt(1);
          mProcessStateTimer[j].writeSummaryFromParcelLocked(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(0);
        }
      }
      if (mVibratorOnTimer != null)
      {
        paramParcel.writeInt(1);
        mVibratorOnTimer.writeSummaryFromParcelLocked(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mUserActivityCounters == null)
      {
        paramParcel.writeInt(0);
      }
      else
      {
        paramParcel.writeInt(1);
        for (j = 0; j < 4; j++) {
          mUserActivityCounters[j].writeSummaryFromParcelLocked(paramParcel);
        }
      }
      if (mNetworkByteActivityCounters == null)
      {
        paramParcel.writeInt(0);
      }
      else
      {
        paramParcel.writeInt(1);
        for (j = 0; j < 10; j++)
        {
          mNetworkByteActivityCounters[j].writeSummaryFromParcelLocked(paramParcel);
          mNetworkPacketActivityCounters[j].writeSummaryFromParcelLocked(paramParcel);
        }
        mMobileRadioActiveTime.writeSummaryFromParcelLocked(paramParcel);
        mMobileRadioActiveCount.writeSummaryFromParcelLocked(paramParcel);
      }
      mUserCpuTime.writeSummaryFromParcelLocked(paramParcel);
      mSystemCpuTime.writeSummaryFromParcelLocked(paramParcel);
      if (mCpuClusterSpeedTimesUs != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeInt(mCpuClusterSpeedTimesUs.length);
        for (localObject3 : mCpuClusterSpeedTimesUs) {
          if (localObject3 != null)
          {
            paramParcel.writeInt(1);
            paramParcel.writeInt(localObject3.length);
            n = localObject3.length;
            for (i1 = 0; i1 < n; i1++)
            {
              localObject1 = localObject3[i1];
              if (localObject1 != null)
              {
                paramParcel.writeInt(1);
                ((LongSamplingCounter)localObject1).writeSummaryFromParcelLocked(paramParcel);
              }
              else
              {
                paramParcel.writeInt(0);
              }
            }
          }
          else
          {
            paramParcel.writeInt(0);
          }
        }
      }
      paramParcel.writeInt(0);
      LongSamplingCounterArray.writeSummaryToParcelLocked(paramParcel, mCpuFreqTimeMs);
      LongSamplingCounterArray.writeSummaryToParcelLocked(paramParcel, mScreenOffCpuFreqTimeMs);
      mCpuActiveTimeMs.writeSummaryFromParcelLocked(paramParcel);
      mCpuClusterTimesMs.writeSummaryToParcelLocked(paramParcel);
      if (mProcStateTimeMs != null)
      {
        paramParcel.writeInt(mProcStateTimeMs.length);
        localObject1 = mProcStateTimeMs;
        i1 = localObject1.length;
        for (j = 0; j < i1; j++) {
          LongSamplingCounterArray.writeSummaryToParcelLocked(paramParcel, localObject1[j]);
        }
      }
      paramParcel.writeInt(0);
      if (mProcStateScreenOffTimeMs != null)
      {
        paramParcel.writeInt(mProcStateScreenOffTimeMs.length);
        localObject1 = mProcStateScreenOffTimeMs;
        i1 = localObject1.length;
        for (j = 0; j < i1; j++) {
          LongSamplingCounterArray.writeSummaryToParcelLocked(paramParcel, localObject1[j]);
        }
      }
      paramParcel.writeInt(0);
      if (mMobileRadioApWakeupCount != null)
      {
        paramParcel.writeInt(1);
        mMobileRadioApWakeupCount.writeSummaryFromParcelLocked(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mWifiRadioApWakeupCount != null)
      {
        paramParcel.writeInt(1);
        mWifiRadioApWakeupCount.writeSummaryFromParcelLocked(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      localObject1 = mWakelockStats.getMap();
      int i1 = ((ArrayMap)localObject1).size();
      paramParcel.writeInt(i1);
      for (j = 0; j < i1; j++)
      {
        paramParcel.writeString((String)((ArrayMap)localObject1).keyAt(j));
        localObject2 = (BatteryStatsImpl.Uid.Wakelock)((ArrayMap)localObject1).valueAt(j);
        if (mTimerFull != null)
        {
          paramParcel.writeInt(1);
          mTimerFull.writeSummaryFromParcelLocked(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(0);
        }
        if (mTimerPartial != null)
        {
          paramParcel.writeInt(1);
          mTimerPartial.writeSummaryFromParcelLocked(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(0);
        }
        if (mTimerWindow != null)
        {
          paramParcel.writeInt(1);
          mTimerWindow.writeSummaryFromParcelLocked(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(0);
        }
        if (mTimerDraw != null)
        {
          paramParcel.writeInt(1);
          mTimerDraw.writeSummaryFromParcelLocked(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(0);
        }
      }
      localObject2 = mSyncStats.getMap();
      ??? = ((ArrayMap)localObject2).size();
      paramParcel.writeInt(???);
      for (j = 0; j < ???; j++)
      {
        paramParcel.writeString((String)((ArrayMap)localObject2).keyAt(j));
        ((DualTimer)((ArrayMap)localObject2).valueAt(j)).writeSummaryFromParcelLocked(paramParcel, l3);
      }
      localObject3 = mJobStats.getMap();
      int n = ((ArrayMap)localObject3).size();
      paramParcel.writeInt(n);
      for (j = 0; j < n; j++)
      {
        paramParcel.writeString((String)((ArrayMap)localObject3).keyAt(j));
        ((DualTimer)((ArrayMap)localObject3).valueAt(j)).writeSummaryFromParcelLocked(paramParcel, l3);
      }
      ((Uid)localObject4).writeJobCompletionsToParcelLocked(paramParcel);
      mJobsDeferredEventCount.writeSummaryFromParcelLocked(paramParcel);
      mJobsDeferredCount.writeSummaryFromParcelLocked(paramParcel);
      mJobsFreshnessTimeMs.writeSummaryFromParcelLocked(paramParcel);
      for (j = 0; j < JOB_FRESHNESS_BUCKETS.length; j++) {
        if (mJobsFreshnessBuckets[j] != null)
        {
          paramParcel.writeInt(1);
          mJobsFreshnessBuckets[j].writeSummaryFromParcelLocked(paramParcel);
        }
        else
        {
          paramParcel.writeInt(0);
        }
      }
      n = mSensorStats.size();
      paramParcel.writeInt(n);
      for (j = 0; j < n; j++)
      {
        paramParcel.writeInt(mSensorStats.keyAt(j));
        localObject3 = (BatteryStatsImpl.Uid.Sensor)mSensorStats.valueAt(j);
        if (mTimer != null)
        {
          paramParcel.writeInt(1);
          mTimer.writeSummaryFromParcelLocked(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(0);
        }
      }
      n = mProcessStats.size();
      paramParcel.writeInt(n);
      j = 0;
      i1 = ???;
      localObject1 = localObject2;
      while (j < n)
      {
        paramParcel.writeString((String)mProcessStats.keyAt(j));
        localObject2 = (BatteryStatsImpl.Uid.Proc)mProcessStats.valueAt(j);
        paramParcel.writeLong(mUserTime);
        paramParcel.writeLong(mSystemTime);
        paramParcel.writeLong(mForegroundTime);
        paramParcel.writeInt(mStarts);
        paramParcel.writeInt(mNumCrashes);
        paramParcel.writeInt(mNumAnrs);
        ((BatteryStatsImpl.Uid.Proc)localObject2).writeExcessivePowerToParcelLocked(paramParcel);
        j++;
      }
      j = mPackageStats.size();
      paramParcel.writeInt(j);
      if (j > 0)
      {
        localObject1 = mPackageStats.entrySet().iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject4 = (Map.Entry)((Iterator)localObject1).next();
          paramParcel.writeString((String)((Map.Entry)localObject4).getKey());
          localObject2 = (BatteryStatsImpl.Uid.Pkg)((Map.Entry)localObject4).getValue();
          ??? = mWakeupAlarms.size();
          paramParcel.writeInt(???);
          for (i1 = 0; i1 < ???; i1++)
          {
            paramParcel.writeString((String)mWakeupAlarms.keyAt(i1));
            ((Counter)mWakeupAlarms.valueAt(i1)).writeSummaryFromParcelLocked(paramParcel);
          }
          ??? = mServiceStats.size();
          paramParcel.writeInt(???);
          for (i1 = 0; i1 < ???; i1++)
          {
            paramParcel.writeString((String)mServiceStats.keyAt(i1));
            localObject3 = (BatteryStatsImpl.Uid.Pkg.Serv)mServiceStats.valueAt(i1);
            paramParcel.writeLong(((BatteryStatsImpl.Uid.Pkg.Serv)localObject3).getStartTimeToNowLocked(mOnBatteryTimeBase.getUptime(l2)));
            paramParcel.writeInt(mStarts);
            paramParcel.writeInt(mLaunches);
          }
        }
      }
    }
  }
  
  public void writeSyncLocked()
  {
    writeLocked(true);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    writeToParcelLocked(paramParcel, true, paramInt);
  }
  
  void writeToParcelLocked(Parcel paramParcel, boolean paramBoolean, int paramInt)
  {
    pullPendingStateUpdatesLocked();
    long l1 = getStartClockTime();
    long l2 = mClocks.uptimeMillis() * 1000L;
    long l3 = mClocks.elapsedRealtime() * 1000L;
    mOnBatteryTimeBase.getRealtime(l3);
    mOnBatteryScreenOffTimeBase.getRealtime(l3);
    paramParcel.writeInt(-1166707595);
    writeHistory(paramParcel, true, false);
    paramParcel.writeInt(mStartCount);
    paramParcel.writeLong(l1);
    paramParcel.writeString(mStartPlatformVersion);
    paramParcel.writeString(mEndPlatformVersion);
    paramParcel.writeLong(mUptime);
    paramParcel.writeLong(mUptimeStart);
    paramParcel.writeLong(mRealtime);
    paramParcel.writeLong(mRealtimeStart);
    paramParcel.writeInt(mOnBattery);
    paramParcel.writeInt(mEstimatedBatteryCapacity);
    paramParcel.writeInt(mMinLearnedBatteryCapacity);
    paramParcel.writeInt(mMaxLearnedBatteryCapacity);
    Object localObject1 = mOnBatteryTimeBase;
    paramInt = 0;
    ((TimeBase)localObject1).writeToParcel(paramParcel, l2, l3);
    mOnBatteryScreenOffTimeBase.writeToParcel(paramParcel, l2, l3);
    mScreenOnTimer.writeToParcel(paramParcel, l3);
    mScreenDozeTimer.writeToParcel(paramParcel, l3);
    for (int i = paramInt; i < 5; i++) {
      mScreenBrightnessTimer[i].writeToParcel(paramParcel, l3);
    }
    mInteractiveTimer.writeToParcel(paramParcel, l3);
    mPowerSaveModeEnabledTimer.writeToParcel(paramParcel, l3);
    paramParcel.writeLong(mLongestLightIdleTime);
    paramParcel.writeLong(mLongestFullIdleTime);
    mDeviceIdleModeLightTimer.writeToParcel(paramParcel, l3);
    mDeviceIdleModeFullTimer.writeToParcel(paramParcel, l3);
    mDeviceLightIdlingTimer.writeToParcel(paramParcel, l3);
    mDeviceIdlingTimer.writeToParcel(paramParcel, l3);
    mPhoneOnTimer.writeToParcel(paramParcel, l3);
    for (i = paramInt; i < 6; i++) {
      mPhoneSignalStrengthsTimer[i].writeToParcel(paramParcel, l3);
    }
    mPhoneSignalScanningTimer.writeToParcel(paramParcel, l3);
    for (i = paramInt; i < 21; i++) {
      mPhoneDataConnectionsTimer[i].writeToParcel(paramParcel, l3);
    }
    for (i = paramInt; i < 10; i++)
    {
      mNetworkByteActivityCounters[i].writeToParcel(paramParcel);
      mNetworkPacketActivityCounters[i].writeToParcel(paramParcel);
    }
    mMobileRadioActiveTimer.writeToParcel(paramParcel, l3);
    mMobileRadioActivePerAppTimer.writeToParcel(paramParcel, l3);
    mMobileRadioActiveAdjustedTime.writeToParcel(paramParcel);
    mMobileRadioActiveUnknownTime.writeToParcel(paramParcel);
    mMobileRadioActiveUnknownCount.writeToParcel(paramParcel);
    mWifiMulticastWakelockTimer.writeToParcel(paramParcel, l3);
    mWifiOnTimer.writeToParcel(paramParcel, l3);
    mGlobalWifiRunningTimer.writeToParcel(paramParcel, l3);
    for (i = paramInt; i < 8; i++) {
      mWifiStateTimer[i].writeToParcel(paramParcel, l3);
    }
    for (i = paramInt; i < 13; i++) {
      mWifiSupplStateTimer[i].writeToParcel(paramParcel, l3);
    }
    for (i = paramInt; i < 5; i++) {
      mWifiSignalStrengthsTimer[i].writeToParcel(paramParcel, l3);
    }
    mWifiActiveTimer.writeToParcel(paramParcel, l3);
    mWifiActivity.writeToParcel(paramParcel, paramInt);
    for (i = paramInt; i < 2; i++) {
      mGpsSignalQualityTimer[i].writeToParcel(paramParcel, l3);
    }
    mBluetoothActivity.writeToParcel(paramParcel, paramInt);
    mModemActivity.writeToParcel(paramParcel, paramInt);
    paramParcel.writeInt(mHasWifiReporting);
    paramParcel.writeInt(mHasBluetoothReporting);
    paramParcel.writeInt(mHasModemReporting);
    paramParcel.writeInt(mNumConnectivityChange);
    paramParcel.writeInt(mLoadedNumConnectivityChange);
    paramParcel.writeInt(mUnpluggedNumConnectivityChange);
    mFlashlightOnTimer.writeToParcel(paramParcel, l3);
    mCameraOnTimer.writeToParcel(paramParcel, l3);
    mBluetoothScanTimer.writeToParcel(paramParcel, l3);
    paramParcel.writeInt(mDischargeUnplugLevel);
    paramParcel.writeInt(mDischargePlugLevel);
    paramParcel.writeInt(mDischargeCurrentLevel);
    paramParcel.writeInt(mCurrentBatteryLevel);
    paramParcel.writeInt(mLowDischargeAmountSinceCharge);
    paramParcel.writeInt(mHighDischargeAmountSinceCharge);
    paramParcel.writeInt(mDischargeAmountScreenOn);
    paramParcel.writeInt(mDischargeAmountScreenOnSinceCharge);
    paramParcel.writeInt(mDischargeAmountScreenOff);
    paramParcel.writeInt(mDischargeAmountScreenOffSinceCharge);
    paramParcel.writeInt(mDischargeAmountScreenDoze);
    paramParcel.writeInt(mDischargeAmountScreenDozeSinceCharge);
    mDischargeStepTracker.writeToParcel(paramParcel);
    mChargeStepTracker.writeToParcel(paramParcel);
    mDischargeCounter.writeToParcel(paramParcel);
    mDischargeScreenOffCounter.writeToParcel(paramParcel);
    mDischargeScreenDozeCounter.writeToParcel(paramParcel);
    mDischargeLightDozeCounter.writeToParcel(paramParcel);
    mDischargeDeepDozeCounter.writeToParcel(paramParcel);
    paramParcel.writeLong(mLastWriteTime);
    paramParcel.writeInt(mRpmStats.size());
    Object localObject2 = mRpmStats.entrySet().iterator();
    Object localObject3;
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (Map.Entry)((Iterator)localObject2).next();
      localObject1 = (SamplingTimer)((Map.Entry)localObject3).getValue();
      if (localObject1 != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString((String)((Map.Entry)localObject3).getKey());
        ((SamplingTimer)localObject1).writeToParcel(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(paramInt);
      }
    }
    paramParcel.writeInt(mScreenOffRpmStats.size());
    localObject2 = mScreenOffRpmStats.entrySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (Map.Entry)((Iterator)localObject2).next();
      localObject1 = (SamplingTimer)((Map.Entry)localObject3).getValue();
      if (localObject1 != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeString((String)((Map.Entry)localObject3).getKey());
        ((SamplingTimer)localObject1).writeToParcel(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(paramInt);
      }
    }
    if (paramBoolean)
    {
      paramParcel.writeInt(mKernelWakelockStats.size());
      localObject1 = mKernelWakelockStats.entrySet().iterator();
      while (((Iterator)localObject1).hasNext())
      {
        localObject3 = (Map.Entry)((Iterator)localObject1).next();
        localObject2 = (SamplingTimer)((Map.Entry)localObject3).getValue();
        if (localObject2 != null)
        {
          paramParcel.writeInt(1);
          paramParcel.writeString((String)((Map.Entry)localObject3).getKey());
          ((SamplingTimer)localObject2).writeToParcel(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(paramInt);
        }
      }
      paramParcel.writeInt(mWakeupReasonStats.size());
      localObject3 = mWakeupReasonStats.entrySet().iterator();
      while (((Iterator)localObject3).hasNext())
      {
        localObject1 = (Map.Entry)((Iterator)localObject3).next();
        localObject2 = (SamplingTimer)((Map.Entry)localObject1).getValue();
        if (localObject2 != null)
        {
          paramParcel.writeInt(1);
          paramParcel.writeString((String)((Map.Entry)localObject1).getKey());
          ((SamplingTimer)localObject2).writeToParcel(paramParcel, l3);
        }
        else
        {
          paramParcel.writeInt(paramInt);
        }
      }
    }
    paramParcel.writeInt(paramInt);
    paramParcel.writeInt(paramInt);
    paramParcel.writeInt(mKernelMemoryStats.size());
    for (i = paramInt; i < mKernelMemoryStats.size(); i++)
    {
      localObject1 = (SamplingTimer)mKernelMemoryStats.valueAt(i);
      if (localObject1 != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeLong(mKernelMemoryStats.keyAt(i));
        ((SamplingTimer)localObject1).writeToParcel(paramParcel, l3);
      }
      else
      {
        paramParcel.writeInt(paramInt);
      }
    }
    if (paramBoolean)
    {
      i = mUidStats.size();
      paramParcel.writeInt(i);
      while (paramInt < i)
      {
        paramParcel.writeInt(mUidStats.keyAt(paramInt));
        ((Uid)mUidStats.valueAt(paramInt)).writeToParcelLocked(paramParcel, l2, l3);
        paramInt++;
      }
    }
    else
    {
      paramParcel.writeInt(paramInt);
    }
  }
  
  public void writeToParcelWithoutUids(Parcel paramParcel, int paramInt)
  {
    writeToParcelLocked(paramParcel, false, paramInt);
  }
  
  public static class BatchTimer
    extends BatteryStatsImpl.Timer
  {
    boolean mInDischarge;
    long mLastAddedDuration;
    long mLastAddedTime;
    final BatteryStatsImpl.Uid mUid;
    
    BatchTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.Uid paramUid, int paramInt, BatteryStatsImpl.TimeBase paramTimeBase)
    {
      super(paramInt, paramTimeBase);
      mUid = paramUid;
      mInDischarge = paramTimeBase.isRunning();
    }
    
    BatchTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.Uid paramUid, int paramInt, BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      super(paramInt, paramTimeBase, paramParcel);
      mUid = paramUid;
      mLastAddedTime = paramParcel.readLong();
      mLastAddedDuration = paramParcel.readLong();
      mInDischarge = paramTimeBase.isRunning();
    }
    
    private long computeOverage(long paramLong)
    {
      if (mLastAddedTime > 0L) {
        return mLastTime + mLastAddedDuration - paramLong;
      }
      return 0L;
    }
    
    private void recomputeLastDuration(long paramLong, boolean paramBoolean)
    {
      long l = computeOverage(paramLong);
      if (l > 0L)
      {
        if (mInDischarge) {
          mTotalTime -= l;
        }
        if (paramBoolean)
        {
          mLastAddedTime = 0L;
        }
        else
        {
          mLastAddedTime = paramLong;
          mLastAddedDuration -= l;
        }
      }
    }
    
    public void abortLastDuration(BatteryStatsImpl paramBatteryStatsImpl)
    {
      recomputeLastDuration(mClocks.elapsedRealtime() * 1000L, true);
    }
    
    public void addDuration(BatteryStatsImpl paramBatteryStatsImpl, long paramLong)
    {
      long l = mClocks.elapsedRealtime() * 1000L;
      recomputeLastDuration(l, true);
      mLastAddedTime = l;
      mLastAddedDuration = (1000L * paramLong);
      if (mInDischarge)
      {
        mTotalTime += mLastAddedDuration;
        mCount += 1;
      }
    }
    
    protected int computeCurrentCountLocked()
    {
      return mCount;
    }
    
    protected long computeRunTimeLocked(long paramLong)
    {
      paramLong = computeOverage(mClocks.elapsedRealtime() * 1000L);
      if (paramLong > 0L)
      {
        mTotalTime = paramLong;
        return paramLong;
      }
      return mTotalTime;
    }
    
    public void logState(Printer paramPrinter, String paramString)
    {
      super.logState(paramPrinter, paramString);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mLastAddedTime=");
      localStringBuilder.append(mLastAddedTime);
      localStringBuilder.append(" mLastAddedDuration=");
      localStringBuilder.append(mLastAddedDuration);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
    {
      recomputeLastDuration(paramLong1, false);
      mInDischarge = true;
      if (mLastAddedTime == paramLong1) {
        mTotalTime += mLastAddedDuration;
      }
      super.onTimeStarted(paramLong1, paramLong2, paramLong3);
    }
    
    public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3)
    {
      recomputeLastDuration(mClocks.elapsedRealtime() * 1000L, false);
      mInDischarge = false;
      super.onTimeStopped(paramLong1, paramLong2, paramLong3);
    }
    
    public boolean reset(boolean paramBoolean)
    {
      long l = mClocks.elapsedRealtime() * 1000L;
      boolean bool = true;
      recomputeLastDuration(l, true);
      int i;
      if (mLastAddedTime == l) {
        i = 1;
      } else {
        i = 0;
      }
      if ((i == 0) && (paramBoolean)) {
        paramBoolean = true;
      } else {
        paramBoolean = false;
      }
      super.reset(paramBoolean);
      if (i == 0) {
        paramBoolean = bool;
      } else {
        paramBoolean = false;
      }
      return paramBoolean;
    }
    
    public void writeToParcel(Parcel paramParcel, long paramLong)
    {
      super.writeToParcel(paramParcel, paramLong);
      paramParcel.writeLong(mLastAddedTime);
      paramParcel.writeLong(mLastAddedDuration);
    }
  }
  
  public static abstract interface BatteryCallback
  {
    public abstract void batteryNeedsCpuUpdate();
    
    public abstract void batteryPowerChanged(boolean paramBoolean);
    
    public abstract void batterySendBroadcast(Intent paramIntent);
    
    public abstract void batteryStatsReset();
  }
  
  private final class BluetoothActivityInfoCache
  {
    long energy;
    long idleTimeMs;
    long rxTimeMs;
    long txTimeMs;
    SparseLongArray uidRxBytes = new SparseLongArray();
    SparseLongArray uidTxBytes = new SparseLongArray();
    
    private BluetoothActivityInfoCache() {}
    
    void set(BluetoothActivityEnergyInfo paramBluetoothActivityEnergyInfo)
    {
      idleTimeMs = paramBluetoothActivityEnergyInfo.getControllerIdleTimeMillis();
      rxTimeMs = paramBluetoothActivityEnergyInfo.getControllerRxTimeMillis();
      txTimeMs = paramBluetoothActivityEnergyInfo.getControllerTxTimeMillis();
      energy = paramBluetoothActivityEnergyInfo.getControllerEnergyUsed();
      if (paramBluetoothActivityEnergyInfo.getUidTraffic() != null) {
        for (Object localObject : paramBluetoothActivityEnergyInfo.getUidTraffic())
        {
          uidRxBytes.put(localObject.getUid(), localObject.getRxBytes());
          uidTxBytes.put(localObject.getUid(), localObject.getTxBytes());
        }
      }
    }
  }
  
  public static abstract interface Clocks
  {
    public abstract long elapsedRealtime();
    
    public abstract long uptimeMillis();
  }
  
  @VisibleForTesting
  public final class Constants
    extends ContentObserver
  {
    private static final long DEFAULT_BATTERY_LEVEL_COLLECTION_DELAY_MS = 300000L;
    private static final long DEFAULT_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = 600000L;
    private static final long DEFAULT_KERNEL_UID_READERS_THROTTLE_TIME = 10000L;
    private static final long DEFAULT_PROC_STATE_CPU_TIMES_READ_DELAY_MS = 5000L;
    private static final boolean DEFAULT_TRACK_CPU_ACTIVE_CLUSTER_TIME = true;
    private static final boolean DEFAULT_TRACK_CPU_TIMES_BY_PROC_STATE = true;
    private static final long DEFAULT_UID_REMOVE_DELAY_MS = 300000L;
    public static final String KEY_BATTERY_LEVEL_COLLECTION_DELAY_MS = "battery_level_collection_delay_ms";
    public static final String KEY_EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = "external_stats_collection_rate_limit_ms";
    public static final String KEY_KERNEL_UID_READERS_THROTTLE_TIME = "kernel_uid_readers_throttle_time";
    public static final String KEY_PROC_STATE_CPU_TIMES_READ_DELAY_MS = "proc_state_cpu_times_read_delay_ms";
    public static final String KEY_TRACK_CPU_ACTIVE_CLUSTER_TIME = "track_cpu_active_cluster_time";
    public static final String KEY_TRACK_CPU_TIMES_BY_PROC_STATE = "track_cpu_times_by_proc_state";
    public static final String KEY_UID_REMOVE_DELAY_MS = "uid_remove_delay_ms";
    public long BATTERY_LEVEL_COLLECTION_DELAY_MS = 300000L;
    public long EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = 600000L;
    public long KERNEL_UID_READERS_THROTTLE_TIME = 10000L;
    public long PROC_STATE_CPU_TIMES_READ_DELAY_MS = 5000L;
    public boolean TRACK_CPU_ACTIVE_CLUSTER_TIME = true;
    public boolean TRACK_CPU_TIMES_BY_PROC_STATE = true;
    public long UID_REMOVE_DELAY_MS = 300000L;
    private final KeyValueListParser mParser = new KeyValueListParser(',');
    private ContentResolver mResolver;
    
    public Constants(Handler paramHandler)
    {
      super();
    }
    
    private void updateConstants()
    {
      try
      {
        synchronized (BatteryStatsImpl.this)
        {
          mParser.setString(Settings.Global.getString(mResolver, "battery_stats_constants"));
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Slog.e("BatteryStatsImpl", "Bad batterystats settings", localIllegalArgumentException);
        updateTrackCpuTimesByProcStateLocked(TRACK_CPU_TIMES_BY_PROC_STATE, mParser.getBoolean("track_cpu_times_by_proc_state", true));
        TRACK_CPU_ACTIVE_CLUSTER_TIME = mParser.getBoolean("track_cpu_active_cluster_time", true);
        updateProcStateCpuTimesReadDelayMs(PROC_STATE_CPU_TIMES_READ_DELAY_MS, mParser.getLong("proc_state_cpu_times_read_delay_ms", 5000L));
        updateKernelUidReadersThrottleTime(KERNEL_UID_READERS_THROTTLE_TIME, mParser.getLong("kernel_uid_readers_throttle_time", 10000L));
        updateUidRemoveDelay(mParser.getLong("uid_remove_delay_ms", 300000L));
        EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS = mParser.getLong("external_stats_collection_rate_limit_ms", 600000L);
        BATTERY_LEVEL_COLLECTION_DELAY_MS = mParser.getLong("battery_level_collection_delay_ms", 300000L);
        return;
      }
    }
    
    private void updateKernelUidReadersThrottleTime(long paramLong1, long paramLong2)
    {
      KERNEL_UID_READERS_THROTTLE_TIME = paramLong2;
      if (paramLong1 != paramLong2)
      {
        mKernelUidCpuTimeReader.setThrottleInterval(KERNEL_UID_READERS_THROTTLE_TIME);
        mKernelUidCpuFreqTimeReader.setThrottleInterval(KERNEL_UID_READERS_THROTTLE_TIME);
        mKernelUidCpuActiveTimeReader.setThrottleInterval(KERNEL_UID_READERS_THROTTLE_TIME);
        mKernelUidCpuClusterTimeReader.setThrottleInterval(KERNEL_UID_READERS_THROTTLE_TIME);
      }
    }
    
    private void updateProcStateCpuTimesReadDelayMs(long paramLong1, long paramLong2)
    {
      PROC_STATE_CPU_TIMES_READ_DELAY_MS = paramLong2;
      if (paramLong1 != paramLong2)
      {
        BatteryStatsImpl.access$1402(BatteryStatsImpl.this, 0L);
        BatteryStatsImpl.access$1502(BatteryStatsImpl.this, 0L);
        BatteryStatsImpl.access$1702(BatteryStatsImpl.this, mClocks.uptimeMillis());
      }
    }
    
    private void updateTrackCpuTimesByProcStateLocked(boolean paramBoolean1, boolean paramBoolean2)
    {
      TRACK_CPU_TIMES_BY_PROC_STATE = paramBoolean2;
      if ((paramBoolean2) && (!paramBoolean1))
      {
        mKernelSingleUidTimeReader.markDataAsStale(true);
        mExternalSync.scheduleCpuSyncDueToSettingChange();
        BatteryStatsImpl.access$1402(BatteryStatsImpl.this, 0L);
        BatteryStatsImpl.access$1502(BatteryStatsImpl.this, 0L);
        BatteryStatsImpl.access$1702(BatteryStatsImpl.this, mClocks.uptimeMillis());
      }
    }
    
    private void updateUidRemoveDelay(long paramLong)
    {
      UID_REMOVE_DELAY_MS = paramLong;
      clearPendingRemovedUids();
    }
    
    public void dumpLocked(PrintWriter paramPrintWriter)
    {
      paramPrintWriter.print("track_cpu_times_by_proc_state");
      paramPrintWriter.print("=");
      paramPrintWriter.println(TRACK_CPU_TIMES_BY_PROC_STATE);
      paramPrintWriter.print("track_cpu_active_cluster_time");
      paramPrintWriter.print("=");
      paramPrintWriter.println(TRACK_CPU_ACTIVE_CLUSTER_TIME);
      paramPrintWriter.print("proc_state_cpu_times_read_delay_ms");
      paramPrintWriter.print("=");
      paramPrintWriter.println(PROC_STATE_CPU_TIMES_READ_DELAY_MS);
      paramPrintWriter.print("kernel_uid_readers_throttle_time");
      paramPrintWriter.print("=");
      paramPrintWriter.println(KERNEL_UID_READERS_THROTTLE_TIME);
      paramPrintWriter.print("external_stats_collection_rate_limit_ms");
      paramPrintWriter.print("=");
      paramPrintWriter.println(EXTERNAL_STATS_COLLECTION_RATE_LIMIT_MS);
      paramPrintWriter.print("battery_level_collection_delay_ms");
      paramPrintWriter.print("=");
      paramPrintWriter.println(BATTERY_LEVEL_COLLECTION_DELAY_MS);
    }
    
    public void onChange(boolean paramBoolean, Uri paramUri)
    {
      updateConstants();
    }
    
    public void startObserving(ContentResolver paramContentResolver)
    {
      mResolver = paramContentResolver;
      mResolver.registerContentObserver(Settings.Global.getUriFor("battery_stats_constants"), false, this);
      updateConstants();
    }
  }
  
  public static class ControllerActivityCounterImpl
    extends BatteryStats.ControllerActivityCounter
    implements Parcelable
  {
    private final BatteryStatsImpl.LongSamplingCounter mIdleTimeMillis;
    private final BatteryStatsImpl.LongSamplingCounter mPowerDrainMaMs;
    private final BatteryStatsImpl.LongSamplingCounter mRxTimeMillis;
    private final BatteryStatsImpl.LongSamplingCounter mScanTimeMillis;
    private final BatteryStatsImpl.LongSamplingCounter mSleepTimeMillis;
    private final BatteryStatsImpl.LongSamplingCounter[] mTxTimeMillis;
    
    public ControllerActivityCounterImpl(BatteryStatsImpl.TimeBase paramTimeBase, int paramInt)
    {
      mIdleTimeMillis = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase);
      mScanTimeMillis = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase);
      mSleepTimeMillis = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase);
      mRxTimeMillis = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase);
      mTxTimeMillis = new BatteryStatsImpl.LongSamplingCounter[paramInt];
      for (int i = 0; i < paramInt; i++) {
        mTxTimeMillis[i] = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase);
      }
      mPowerDrainMaMs = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase);
    }
    
    public ControllerActivityCounterImpl(BatteryStatsImpl.TimeBase paramTimeBase, int paramInt, Parcel paramParcel)
    {
      mIdleTimeMillis = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase, paramParcel);
      mScanTimeMillis = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase, paramParcel);
      mSleepTimeMillis = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase, paramParcel);
      mRxTimeMillis = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase, paramParcel);
      if (paramParcel.readInt() == paramInt)
      {
        mTxTimeMillis = new BatteryStatsImpl.LongSamplingCounter[paramInt];
        for (int i = 0; i < paramInt; i++) {
          mTxTimeMillis[i] = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase, paramParcel);
        }
        mPowerDrainMaMs = new BatteryStatsImpl.LongSamplingCounter(paramTimeBase, paramParcel);
        return;
      }
      throw new ParcelFormatException("inconsistent tx state lengths");
    }
    
    public int describeContents()
    {
      return 0;
    }
    
    public void detach()
    {
      mIdleTimeMillis.detach();
      mScanTimeMillis.detach();
      mSleepTimeMillis.detach();
      mRxTimeMillis.detach();
      BatteryStatsImpl.LongSamplingCounter[] arrayOfLongSamplingCounter = mTxTimeMillis;
      int i = arrayOfLongSamplingCounter.length;
      for (int j = 0; j < i; j++) {
        arrayOfLongSamplingCounter[j].detach();
      }
      mPowerDrainMaMs.detach();
    }
    
    public BatteryStatsImpl.LongSamplingCounter getIdleTimeCounter()
    {
      return mIdleTimeMillis;
    }
    
    public BatteryStatsImpl.LongSamplingCounter getPowerCounter()
    {
      return mPowerDrainMaMs;
    }
    
    public BatteryStatsImpl.LongSamplingCounter getRxTimeCounter()
    {
      return mRxTimeMillis;
    }
    
    public BatteryStatsImpl.LongSamplingCounter getScanTimeCounter()
    {
      return mScanTimeMillis;
    }
    
    public BatteryStatsImpl.LongSamplingCounter getSleepTimeCounter()
    {
      return mSleepTimeMillis;
    }
    
    public BatteryStatsImpl.LongSamplingCounter[] getTxTimeCounters()
    {
      return mTxTimeMillis;
    }
    
    public void readSummaryFromParcel(Parcel paramParcel)
    {
      mIdleTimeMillis.readSummaryFromParcelLocked(paramParcel);
      mScanTimeMillis.readSummaryFromParcelLocked(paramParcel);
      mSleepTimeMillis.readSummaryFromParcelLocked(paramParcel);
      mRxTimeMillis.readSummaryFromParcelLocked(paramParcel);
      if (paramParcel.readInt() == mTxTimeMillis.length)
      {
        BatteryStatsImpl.LongSamplingCounter[] arrayOfLongSamplingCounter = mTxTimeMillis;
        int i = arrayOfLongSamplingCounter.length;
        for (int j = 0; j < i; j++) {
          arrayOfLongSamplingCounter[j].readSummaryFromParcelLocked(paramParcel);
        }
        mPowerDrainMaMs.readSummaryFromParcelLocked(paramParcel);
        return;
      }
      throw new ParcelFormatException("inconsistent tx state lengths");
    }
    
    public void reset(boolean paramBoolean)
    {
      mIdleTimeMillis.reset(paramBoolean);
      mScanTimeMillis.reset(paramBoolean);
      mSleepTimeMillis.reset(paramBoolean);
      mRxTimeMillis.reset(paramBoolean);
      BatteryStatsImpl.LongSamplingCounter[] arrayOfLongSamplingCounter = mTxTimeMillis;
      int i = arrayOfLongSamplingCounter.length;
      for (int j = 0; j < i; j++) {
        arrayOfLongSamplingCounter[j].reset(paramBoolean);
      }
      mPowerDrainMaMs.reset(paramBoolean);
    }
    
    public void writeSummaryToParcel(Parcel paramParcel)
    {
      mIdleTimeMillis.writeSummaryFromParcelLocked(paramParcel);
      mScanTimeMillis.writeSummaryFromParcelLocked(paramParcel);
      mSleepTimeMillis.writeSummaryFromParcelLocked(paramParcel);
      mRxTimeMillis.writeSummaryFromParcelLocked(paramParcel);
      paramParcel.writeInt(mTxTimeMillis.length);
      BatteryStatsImpl.LongSamplingCounter[] arrayOfLongSamplingCounter = mTxTimeMillis;
      int i = arrayOfLongSamplingCounter.length;
      for (int j = 0; j < i; j++) {
        arrayOfLongSamplingCounter[j].writeSummaryFromParcelLocked(paramParcel);
      }
      mPowerDrainMaMs.writeSummaryFromParcelLocked(paramParcel);
    }
    
    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      mIdleTimeMillis.writeToParcel(paramParcel);
      mScanTimeMillis.writeToParcel(paramParcel);
      mSleepTimeMillis.writeToParcel(paramParcel);
      mRxTimeMillis.writeToParcel(paramParcel);
      paramParcel.writeInt(mTxTimeMillis.length);
      BatteryStatsImpl.LongSamplingCounter[] arrayOfLongSamplingCounter = mTxTimeMillis;
      int i = arrayOfLongSamplingCounter.length;
      for (paramInt = 0; paramInt < i; paramInt++) {
        arrayOfLongSamplingCounter[paramInt].writeToParcel(paramParcel);
      }
      mPowerDrainMaMs.writeToParcel(paramParcel);
    }
  }
  
  public static class Counter
    extends BatteryStats.Counter
    implements BatteryStatsImpl.TimeBaseObs
  {
    final AtomicInteger mCount = new AtomicInteger();
    int mLoadedCount;
    int mPluggedCount;
    final BatteryStatsImpl.TimeBase mTimeBase;
    int mUnpluggedCount;
    
    public Counter(BatteryStatsImpl.TimeBase paramTimeBase)
    {
      mTimeBase = paramTimeBase;
      paramTimeBase.add(this);
    }
    
    public Counter(BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      mTimeBase = paramTimeBase;
      mPluggedCount = paramParcel.readInt();
      mCount.set(mPluggedCount);
      mLoadedCount = paramParcel.readInt();
      mUnpluggedCount = paramParcel.readInt();
      paramTimeBase.add(this);
    }
    
    public static Counter readCounterFromParcel(BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      if (paramParcel.readInt() == 0) {
        return null;
      }
      return new Counter(paramTimeBase, paramParcel);
    }
    
    public static void writeCounterToParcel(Parcel paramParcel, Counter paramCounter)
    {
      if (paramCounter == null)
      {
        paramParcel.writeInt(0);
        return;
      }
      paramParcel.writeInt(1);
      paramCounter.writeToParcel(paramParcel);
    }
    
    void addAtomic(int paramInt)
    {
      if (mTimeBase.isRunning()) {
        mCount.addAndGet(paramInt);
      }
    }
    
    void detach()
    {
      mTimeBase.remove(this);
    }
    
    public int getCountLocked(int paramInt)
    {
      int i = mCount.get();
      int j;
      if (paramInt == 2)
      {
        j = i - mUnpluggedCount;
      }
      else
      {
        j = i;
        if (paramInt != 0) {
          j = i - mLoadedCount;
        }
      }
      return j;
    }
    
    public void logState(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mCount=");
      localStringBuilder.append(mCount.get());
      localStringBuilder.append(" mLoadedCount=");
      localStringBuilder.append(mLoadedCount);
      localStringBuilder.append(" mUnpluggedCount=");
      localStringBuilder.append(mUnpluggedCount);
      localStringBuilder.append(" mPluggedCount=");
      localStringBuilder.append(mPluggedCount);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
    {
      mUnpluggedCount = mPluggedCount;
    }
    
    public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3)
    {
      mPluggedCount = mCount.get();
    }
    
    @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
    public void readSummaryFromParcelLocked(Parcel paramParcel)
    {
      mLoadedCount = paramParcel.readInt();
      mCount.set(mLoadedCount);
      int i = mLoadedCount;
      mPluggedCount = i;
      mUnpluggedCount = i;
    }
    
    void reset(boolean paramBoolean)
    {
      mCount.set(0);
      mUnpluggedCount = 0;
      mPluggedCount = 0;
      mLoadedCount = 0;
      if (paramBoolean) {
        detach();
      }
    }
    
    @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
    public void stepAtomic()
    {
      if (mTimeBase.isRunning()) {
        mCount.incrementAndGet();
      }
    }
    
    @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
    public void writeSummaryFromParcelLocked(Parcel paramParcel)
    {
      paramParcel.writeInt(mCount.get());
    }
    
    public void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeInt(mCount.get());
      paramParcel.writeInt(mLoadedCount);
      paramParcel.writeInt(mUnpluggedCount);
    }
  }
  
  public static class DualTimer
    extends BatteryStatsImpl.DurationTimer
  {
    private final BatteryStatsImpl.DurationTimer mSubTimer;
    
    public DualTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.Uid paramUid, int paramInt, ArrayList<BatteryStatsImpl.StopwatchTimer> paramArrayList, BatteryStatsImpl.TimeBase paramTimeBase1, BatteryStatsImpl.TimeBase paramTimeBase2)
    {
      super(paramUid, paramInt, paramArrayList, paramTimeBase1);
      mSubTimer = new BatteryStatsImpl.DurationTimer(paramClocks, paramUid, paramInt, null, paramTimeBase2);
    }
    
    public DualTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.Uid paramUid, int paramInt, ArrayList<BatteryStatsImpl.StopwatchTimer> paramArrayList, BatteryStatsImpl.TimeBase paramTimeBase1, BatteryStatsImpl.TimeBase paramTimeBase2, Parcel paramParcel)
    {
      super(paramUid, paramInt, paramArrayList, paramTimeBase1, paramParcel);
      mSubTimer = new BatteryStatsImpl.DurationTimer(paramClocks, paramUid, paramInt, null, paramTimeBase2, paramParcel);
    }
    
    public void detach()
    {
      mSubTimer.detach();
      super.detach();
    }
    
    public BatteryStatsImpl.DurationTimer getSubTimer()
    {
      return mSubTimer;
    }
    
    public void readSummaryFromParcelLocked(Parcel paramParcel)
    {
      super.readSummaryFromParcelLocked(paramParcel);
      mSubTimer.readSummaryFromParcelLocked(paramParcel);
    }
    
    public boolean reset(boolean paramBoolean)
    {
      BatteryStatsImpl.DurationTimer localDurationTimer = mSubTimer;
      boolean bool = false;
      if (!(false | localDurationTimer.reset(false) ^ true | super.reset(paramBoolean) ^ true)) {
        bool = true;
      }
      return bool;
    }
    
    public void startRunningLocked(long paramLong)
    {
      super.startRunningLocked(paramLong);
      mSubTimer.startRunningLocked(paramLong);
    }
    
    public void stopAllRunningLocked(long paramLong)
    {
      super.stopAllRunningLocked(paramLong);
      mSubTimer.stopAllRunningLocked(paramLong);
    }
    
    public void stopRunningLocked(long paramLong)
    {
      super.stopRunningLocked(paramLong);
      mSubTimer.stopRunningLocked(paramLong);
    }
    
    public void writeSummaryFromParcelLocked(Parcel paramParcel, long paramLong)
    {
      super.writeSummaryFromParcelLocked(paramParcel, paramLong);
      mSubTimer.writeSummaryFromParcelLocked(paramParcel, paramLong);
    }
    
    public void writeToParcel(Parcel paramParcel, long paramLong)
    {
      super.writeToParcel(paramParcel, paramLong);
      mSubTimer.writeToParcel(paramParcel, paramLong);
    }
  }
  
  public static class DurationTimer
    extends BatteryStatsImpl.StopwatchTimer
  {
    long mCurrentDurationMs;
    long mMaxDurationMs;
    long mStartTimeMs = -1L;
    long mTotalDurationMs;
    
    public DurationTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.Uid paramUid, int paramInt, ArrayList<BatteryStatsImpl.StopwatchTimer> paramArrayList, BatteryStatsImpl.TimeBase paramTimeBase)
    {
      super(paramUid, paramInt, paramArrayList, paramTimeBase);
    }
    
    public DurationTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.Uid paramUid, int paramInt, ArrayList<BatteryStatsImpl.StopwatchTimer> paramArrayList, BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      super(paramUid, paramInt, paramArrayList, paramTimeBase, paramParcel);
      mMaxDurationMs = paramParcel.readLong();
      mTotalDurationMs = paramParcel.readLong();
      mCurrentDurationMs = paramParcel.readLong();
    }
    
    public long getCurrentDurationMsLocked(long paramLong)
    {
      long l1 = mCurrentDurationMs;
      long l2 = l1;
      if (mNesting > 0)
      {
        l2 = l1;
        if (mTimeBase.isRunning()) {
          l2 = l1 + (mTimeBase.getRealtime(paramLong * 1000L) / 1000L - mStartTimeMs);
        }
      }
      return l2;
    }
    
    public long getMaxDurationMsLocked(long paramLong)
    {
      if (mNesting > 0)
      {
        paramLong = getCurrentDurationMsLocked(paramLong);
        if (paramLong > mMaxDurationMs) {
          return paramLong;
        }
      }
      return mMaxDurationMs;
    }
    
    public long getTotalDurationMsLocked(long paramLong)
    {
      return mTotalDurationMs + getCurrentDurationMsLocked(paramLong);
    }
    
    public void logState(Printer paramPrinter, String paramString)
    {
      super.logState(paramPrinter, paramString);
    }
    
    public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
    {
      super.onTimeStarted(paramLong1, paramLong2, paramLong3);
      if (mNesting > 0) {
        mStartTimeMs = (paramLong3 / 1000L);
      }
    }
    
    public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3)
    {
      super.onTimeStopped(paramLong1, paramLong2, paramLong3);
      if (mNesting > 0) {
        mCurrentDurationMs += paramLong3 / 1000L - mStartTimeMs;
      }
      mStartTimeMs = -1L;
    }
    
    public void readSummaryFromParcelLocked(Parcel paramParcel)
    {
      super.readSummaryFromParcelLocked(paramParcel);
      mMaxDurationMs = paramParcel.readLong();
      mTotalDurationMs = paramParcel.readLong();
      mStartTimeMs = -1L;
      mCurrentDurationMs = 0L;
    }
    
    public boolean reset(boolean paramBoolean)
    {
      paramBoolean = super.reset(paramBoolean);
      mMaxDurationMs = 0L;
      mTotalDurationMs = 0L;
      mCurrentDurationMs = 0L;
      if (mNesting > 0) {
        mStartTimeMs = (mTimeBase.getRealtime(mClocks.elapsedRealtime() * 1000L) / 1000L);
      } else {
        mStartTimeMs = -1L;
      }
      return paramBoolean;
    }
    
    public void startRunningLocked(long paramLong)
    {
      super.startRunningLocked(paramLong);
      if ((mNesting == 1) && (mTimeBase.isRunning())) {
        mStartTimeMs = (mTimeBase.getRealtime(paramLong * 1000L) / 1000L);
      }
    }
    
    public void stopRunningLocked(long paramLong)
    {
      if (mNesting == 1)
      {
        long l = getCurrentDurationMsLocked(paramLong);
        mTotalDurationMs += l;
        if (l > mMaxDurationMs) {
          mMaxDurationMs = l;
        }
        mStartTimeMs = -1L;
        mCurrentDurationMs = 0L;
      }
      super.stopRunningLocked(paramLong);
    }
    
    public void writeSummaryFromParcelLocked(Parcel paramParcel, long paramLong)
    {
      super.writeSummaryFromParcelLocked(paramParcel, paramLong);
      paramParcel.writeLong(getMaxDurationMsLocked(paramLong / 1000L));
      paramParcel.writeLong(getTotalDurationMsLocked(paramLong / 1000L));
    }
    
    public void writeToParcel(Parcel paramParcel, long paramLong)
    {
      super.writeToParcel(paramParcel, paramLong);
      paramParcel.writeLong(getMaxDurationMsLocked(paramLong / 1000L));
      paramParcel.writeLong(mTotalDurationMs);
      paramParcel.writeLong(getCurrentDurationMsLocked(paramLong / 1000L));
    }
  }
  
  public static class EasyTimer
  {
    boolean mIsKnownStatus = false;
    int mNesting = 0;
    long mStartTimeMs = 0L;
    long mStopTimeMs = 0L;
    
    public EasyTimer() {}
    
    public long getExecutionTime(long paramLong)
    {
      if ((mStartTimeMs > mStopTimeMs) && (paramLong > mStartTimeMs)) {
        return paramLong - mStartTimeMs;
      }
      if ((mStopTimeMs > mStartTimeMs) && (mStopTimeMs > 0L) && (paramLong > mStopTimeMs)) {
        return paramLong - mStopTimeMs;
      }
      return 86400L;
    }
    
    public int getStatus()
    {
      if (mIsKnownStatus)
      {
        int i;
        if (isActive()) {
          i = 1;
        } else {
          i = -1;
        }
        return i;
      }
      return 0;
    }
    
    public boolean isActive()
    {
      boolean bool;
      if (mNesting > 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean isKnownStatus()
    {
      return mIsKnownStatus;
    }
    
    public void startRunningLocked(long paramLong)
    {
      mIsKnownStatus = true;
      int i = mNesting;
      mNesting = (i + 1);
      if (i == 0) {
        mStartTimeMs = paramLong;
      }
    }
    
    public void stopAllRunningLocked(long paramLong)
    {
      mIsKnownStatus = true;
      if (mNesting > 0)
      {
        mNesting = 1;
        stopRunningLocked(paramLong);
      }
    }
    
    public void stopRunningLocked(long paramLong)
    {
      mIsKnownStatus = true;
      if (mNesting == 0) {
        return;
      }
      int i = mNesting - 1;
      mNesting = i;
      if (i == 0) {
        mStopTimeMs = paramLong;
      }
    }
  }
  
  public static abstract interface ExternalStatsSync
  {
    public static final int UPDATE_ALL = 31;
    public static final int UPDATE_BT = 8;
    public static final int UPDATE_CPU = 1;
    public static final int UPDATE_RADIO = 4;
    public static final int UPDATE_RPM = 16;
    public static final int UPDATE_WIFI = 2;
    
    public abstract void cancelCpuSyncDueToWakelockChange();
    
    public abstract Future<?> scheduleCopyFromAllUidsCpuTimes(boolean paramBoolean1, boolean paramBoolean2);
    
    public abstract Future<?> scheduleCpuSyncDueToRemovedUid(int paramInt);
    
    public abstract Future<?> scheduleCpuSyncDueToScreenStateChange(boolean paramBoolean1, boolean paramBoolean2);
    
    public abstract Future<?> scheduleCpuSyncDueToSettingChange();
    
    public abstract Future<?> scheduleCpuSyncDueToWakelockChange(long paramLong);
    
    public abstract Future<?> scheduleReadProcStateCpuTimes(boolean paramBoolean1, boolean paramBoolean2, long paramLong);
    
    public abstract Future<?> scheduleSync(String paramString, int paramInt);
    
    public abstract Future<?> scheduleSyncDueToBatteryLevelChange(long paramLong);
  }
  
  @VisibleForTesting
  public static class LongSamplingCounter
    extends BatteryStats.LongCounter
    implements BatteryStatsImpl.TimeBaseObs
  {
    public long mCount;
    public long mCurrentCount;
    public long mLoadedCount;
    final BatteryStatsImpl.TimeBase mTimeBase;
    public long mUnpluggedCount;
    
    public LongSamplingCounter(BatteryStatsImpl.TimeBase paramTimeBase)
    {
      mTimeBase = paramTimeBase;
      paramTimeBase.add(this);
    }
    
    public LongSamplingCounter(BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      mTimeBase = paramTimeBase;
      mCount = paramParcel.readLong();
      mCurrentCount = paramParcel.readLong();
      mLoadedCount = paramParcel.readLong();
      mUnpluggedCount = paramParcel.readLong();
      paramTimeBase.add(this);
    }
    
    public void addCountLocked(long paramLong)
    {
      update(mCurrentCount + paramLong, mTimeBase.isRunning());
    }
    
    public void addCountLocked(long paramLong, boolean paramBoolean)
    {
      update(mCurrentCount + paramLong, paramBoolean);
    }
    
    public void detach()
    {
      mTimeBase.remove(this);
    }
    
    public long getCountLocked(int paramInt)
    {
      long l1 = mCount;
      long l2;
      if (paramInt == 2)
      {
        l2 = l1 - mUnpluggedCount;
      }
      else
      {
        l2 = l1;
        if (paramInt != 0) {
          l2 = l1 - mLoadedCount;
        }
      }
      return l2;
    }
    
    public void logState(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mCount=");
      localStringBuilder.append(mCount);
      localStringBuilder.append(" mCurrentCount=");
      localStringBuilder.append(mCurrentCount);
      localStringBuilder.append(" mLoadedCount=");
      localStringBuilder.append(mLoadedCount);
      localStringBuilder.append(" mUnpluggedCount=");
      localStringBuilder.append(mUnpluggedCount);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
    {
      mUnpluggedCount = mCount;
    }
    
    public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3) {}
    
    public void readSummaryFromParcelLocked(Parcel paramParcel)
    {
      long l = paramParcel.readLong();
      mLoadedCount = l;
      mUnpluggedCount = l;
      mCount = l;
    }
    
    public void reset(boolean paramBoolean)
    {
      mCount = 0L;
      mUnpluggedCount = 0L;
      mLoadedCount = 0L;
      if (paramBoolean) {
        detach();
      }
    }
    
    public void update(long paramLong)
    {
      update(paramLong, mTimeBase.isRunning());
    }
    
    public void update(long paramLong, boolean paramBoolean)
    {
      if (paramLong < mCurrentCount) {
        mCurrentCount = 0L;
      }
      if (paramBoolean) {
        mCount += paramLong - mCurrentCount;
      }
      mCurrentCount = paramLong;
    }
    
    public void writeSummaryFromParcelLocked(Parcel paramParcel)
    {
      paramParcel.writeLong(mCount);
    }
    
    public void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeLong(mCount);
      paramParcel.writeLong(mCurrentCount);
      paramParcel.writeLong(mLoadedCount);
      paramParcel.writeLong(mUnpluggedCount);
    }
  }
  
  @VisibleForTesting
  public static class LongSamplingCounterArray
    extends BatteryStats.LongCounterArray
    implements BatteryStatsImpl.TimeBaseObs
  {
    public long[] mCounts;
    public long[] mLoadedCounts;
    final BatteryStatsImpl.TimeBase mTimeBase;
    public long[] mUnpluggedCounts;
    
    public LongSamplingCounterArray(BatteryStatsImpl.TimeBase paramTimeBase)
    {
      mTimeBase = paramTimeBase;
      paramTimeBase.add(this);
    }
    
    private LongSamplingCounterArray(BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      mTimeBase = paramTimeBase;
      mCounts = paramParcel.createLongArray();
      mLoadedCounts = paramParcel.createLongArray();
      mUnpluggedCounts = paramParcel.createLongArray();
      paramTimeBase.add(this);
    }
    
    private static long[] copyArray(long[] paramArrayOfLong1, long[] paramArrayOfLong2)
    {
      if (paramArrayOfLong1 == null) {
        return null;
      }
      long[] arrayOfLong = paramArrayOfLong2;
      if (paramArrayOfLong2 == null) {
        arrayOfLong = new long[paramArrayOfLong1.length];
      }
      System.arraycopy(paramArrayOfLong1, 0, arrayOfLong, 0, paramArrayOfLong1.length);
      return arrayOfLong;
    }
    
    private static void fillArray(long[] paramArrayOfLong, long paramLong)
    {
      if (paramArrayOfLong != null) {
        Arrays.fill(paramArrayOfLong, paramLong);
      }
    }
    
    public static LongSamplingCounterArray readFromParcel(Parcel paramParcel, BatteryStatsImpl.TimeBase paramTimeBase)
    {
      if (paramParcel.readInt() != 0) {
        return new LongSamplingCounterArray(paramTimeBase, paramParcel);
      }
      return null;
    }
    
    public static LongSamplingCounterArray readSummaryFromParcelLocked(Parcel paramParcel, BatteryStatsImpl.TimeBase paramTimeBase)
    {
      if (paramParcel.readInt() != 0)
      {
        paramTimeBase = new LongSamplingCounterArray(paramTimeBase);
        paramTimeBase.readSummaryFromParcelLocked(paramParcel);
        return paramTimeBase;
      }
      return null;
    }
    
    private void readSummaryFromParcelLocked(Parcel paramParcel)
    {
      mCounts = paramParcel.createLongArray();
      mLoadedCounts = copyArray(mCounts, mLoadedCounts);
      mUnpluggedCounts = copyArray(mCounts, mUnpluggedCounts);
    }
    
    private static void subtract(long[] paramArrayOfLong1, long[] paramArrayOfLong2)
    {
      if (paramArrayOfLong2 == null) {
        return;
      }
      for (int i = 0; i < paramArrayOfLong1.length; i++) {
        paramArrayOfLong1[i] -= paramArrayOfLong2[i];
      }
    }
    
    private void writeSummaryToParcelLocked(Parcel paramParcel)
    {
      paramParcel.writeLongArray(mCounts);
    }
    
    public static void writeSummaryToParcelLocked(Parcel paramParcel, LongSamplingCounterArray paramLongSamplingCounterArray)
    {
      if (paramLongSamplingCounterArray != null)
      {
        paramParcel.writeInt(1);
        paramLongSamplingCounterArray.writeSummaryToParcelLocked(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    
    private void writeToParcel(Parcel paramParcel)
    {
      paramParcel.writeLongArray(mCounts);
      paramParcel.writeLongArray(mLoadedCounts);
      paramParcel.writeLongArray(mUnpluggedCounts);
    }
    
    public static void writeToParcel(Parcel paramParcel, LongSamplingCounterArray paramLongSamplingCounterArray)
    {
      if (paramLongSamplingCounterArray != null)
      {
        paramParcel.writeInt(1);
        paramLongSamplingCounterArray.writeToParcel(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    
    public void addCountLocked(long[] paramArrayOfLong)
    {
      addCountLocked(paramArrayOfLong, mTimeBase.isRunning());
    }
    
    public void addCountLocked(long[] paramArrayOfLong, boolean paramBoolean)
    {
      if (paramArrayOfLong == null) {
        return;
      }
      if (paramBoolean)
      {
        if (mCounts == null) {
          mCounts = new long[paramArrayOfLong.length];
        }
        for (int i = 0; i < paramArrayOfLong.length; i++)
        {
          long[] arrayOfLong = mCounts;
          arrayOfLong[i] += paramArrayOfLong[i];
        }
      }
    }
    
    public void detach()
    {
      mTimeBase.remove(this);
    }
    
    public long[] getCountsLocked(int paramInt)
    {
      long[] arrayOfLong = copyArray(mCounts, null);
      if (paramInt == 2) {
        subtract(arrayOfLong, mUnpluggedCounts);
      } else if (paramInt != 0) {
        subtract(arrayOfLong, mLoadedCounts);
      }
      return arrayOfLong;
    }
    
    public int getSize()
    {
      int i;
      if (mCounts == null) {
        i = 0;
      } else {
        i = mCounts.length;
      }
      return i;
    }
    
    public void logState(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mCounts=");
      localStringBuilder.append(Arrays.toString(mCounts));
      localStringBuilder.append(" mLoadedCounts=");
      localStringBuilder.append(Arrays.toString(mLoadedCounts));
      localStringBuilder.append(" mUnpluggedCounts=");
      localStringBuilder.append(Arrays.toString(mUnpluggedCounts));
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
    {
      mUnpluggedCounts = copyArray(mCounts, mUnpluggedCounts);
    }
    
    public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3) {}
    
    public void reset(boolean paramBoolean)
    {
      fillArray(mCounts, 0L);
      fillArray(mLoadedCounts, 0L);
      fillArray(mUnpluggedCounts, 0L);
      if (paramBoolean) {
        detach();
      }
    }
  }
  
  final class MyHandler
    extends Handler
  {
    public MyHandler(Looper paramLooper)
    {
      super(null, true);
    }
    
    public void handleMessage(Message paramMessage)
    {
      BatteryStatsImpl.BatteryCallback localBatteryCallback = mCallback;
      switch (what)
      {
      default: 
        break;
      case 4: 
        if (localBatteryCallback != null) {
          localBatteryCallback.batteryStatsReset();
        }
        break;
      case 3: 
        if (localBatteryCallback != null) {
          synchronized (BatteryStatsImpl.this)
          {
            if (mCharging) {
              paramMessage = "android.os.action.CHARGING";
            } else {
              paramMessage = "android.os.action.DISCHARGING";
            }
            paramMessage = new Intent(paramMessage);
            paramMessage.addFlags(67108864);
            localBatteryCallback.batterySendBroadcast(paramMessage);
          }
        }
        break;
      case 2: 
        if (localBatteryCallback != null)
        {
          boolean bool;
          if (arg1 != 0) {
            bool = true;
          } else {
            bool = false;
          }
          localBatteryCallback.batteryPowerChanged(bool);
        }
        break;
      case 1: 
        if (localBatteryCallback != null) {
          localBatteryCallback.batteryNeedsCpuUpdate();
        }
        break;
      }
    }
  }
  
  public abstract class OverflowArrayMap<T>
  {
    private static final String OVERFLOW_NAME = "*overflow*";
    ArrayMap<String, MutableInt> mActiveOverflow;
    T mCurOverflow;
    long mLastCleanupTime;
    long mLastClearTime;
    long mLastOverflowFinishTime;
    long mLastOverflowTime;
    final ArrayMap<String, T> mMap = new ArrayMap();
    final int mUid;
    
    public OverflowArrayMap(int paramInt)
    {
      mUid = paramInt;
    }
    
    public void add(String paramString, T paramT)
    {
      String str = paramString;
      if (paramString == null) {
        str = "";
      }
      mMap.put(str, paramT);
      if ("*overflow*".equals(str)) {
        mCurOverflow = paramT;
      }
    }
    
    public void cleanup()
    {
      mLastCleanupTime = SystemClock.elapsedRealtime();
      if ((mActiveOverflow != null) && (mActiveOverflow.size() == 0)) {
        mActiveOverflow = null;
      }
      StringBuilder localStringBuilder;
      if (mActiveOverflow == null)
      {
        if (mMap.containsKey("*overflow*"))
        {
          localStringBuilder = new StringBuilder();
          localStringBuilder.append("Cleaning up with no active overflow, but have overflow entry ");
          localStringBuilder.append(mMap.get("*overflow*"));
          Slog.wtf("BatteryStatsImpl", localStringBuilder.toString());
          mMap.remove("*overflow*");
        }
        mCurOverflow = null;
      }
      else if ((mCurOverflow == null) || (!mMap.containsKey("*overflow*")))
      {
        localStringBuilder = new StringBuilder();
        localStringBuilder.append("Cleaning up with active overflow, but no overflow entry: cur=");
        localStringBuilder.append(mCurOverflow);
        localStringBuilder.append(" map=");
        localStringBuilder.append(mMap.get("*overflow*"));
        Slog.wtf("BatteryStatsImpl", localStringBuilder.toString());
      }
    }
    
    public void clear()
    {
      mLastClearTime = SystemClock.elapsedRealtime();
      mMap.clear();
      mCurOverflow = null;
      mActiveOverflow = null;
    }
    
    public ArrayMap<String, T> getMap()
    {
      return mMap;
    }
    
    public abstract T instantiateObject();
    
    public T startObject(String paramString)
    {
      String str = paramString;
      if (paramString == null) {
        str = "";
      }
      paramString = mMap.get(str);
      if (paramString != null) {
        return paramString;
      }
      Object localObject;
      if (mActiveOverflow != null)
      {
        MutableInt localMutableInt = (MutableInt)mActiveOverflow.get(str);
        if (localMutableInt != null)
        {
          localObject = mCurOverflow;
          paramString = localObject;
          if (localObject == null)
          {
            paramString = new StringBuilder();
            paramString.append("Have active overflow ");
            paramString.append(str);
            paramString.append(" but null overflow");
            Slog.wtf("BatteryStatsImpl", paramString.toString());
            paramString = instantiateObject();
            mCurOverflow = paramString;
            mMap.put("*overflow*", paramString);
          }
          value += 1;
          return paramString;
        }
      }
      if (mMap.size() >= BatteryStatsImpl.MAX_WAKELOCKS_PER_UID)
      {
        localObject = mCurOverflow;
        paramString = localObject;
        if (localObject == null)
        {
          paramString = instantiateObject();
          mCurOverflow = paramString;
          mMap.put("*overflow*", paramString);
        }
        if (mActiveOverflow == null) {
          mActiveOverflow = new ArrayMap();
        }
        mActiveOverflow.put(str, new MutableInt(1));
        mLastOverflowTime = SystemClock.elapsedRealtime();
        return paramString;
      }
      paramString = instantiateObject();
      mMap.put(str, paramString);
      return paramString;
    }
    
    public T stopObject(String paramString)
    {
      String str = paramString;
      if (paramString == null) {
        str = "";
      }
      paramString = mMap.get(str);
      if (paramString != null) {
        return paramString;
      }
      if (mActiveOverflow != null)
      {
        paramString = (MutableInt)mActiveOverflow.get(str);
        if (paramString != null)
        {
          Object localObject = mCurOverflow;
          if (localObject != null)
          {
            value -= 1;
            if (value <= 0)
            {
              mActiveOverflow.remove(str);
              mLastOverflowFinishTime = SystemClock.elapsedRealtime();
            }
            return localObject;
          }
        }
      }
      paramString = new StringBuilder();
      paramString.append("Unable to find object for ");
      paramString.append(str);
      paramString.append(" in uid ");
      paramString.append(mUid);
      paramString.append(" mapsize=");
      paramString.append(mMap.size());
      paramString.append(" activeoverflow=");
      paramString.append(mActiveOverflow);
      paramString.append(" curoverflow=");
      paramString.append(mCurOverflow);
      long l = SystemClock.elapsedRealtime();
      if (mLastOverflowTime != 0L)
      {
        paramString.append(" lastOverflowTime=");
        TimeUtils.formatDuration(mLastOverflowTime - l, paramString);
      }
      if (mLastOverflowFinishTime != 0L)
      {
        paramString.append(" lastOverflowFinishTime=");
        TimeUtils.formatDuration(mLastOverflowFinishTime - l, paramString);
      }
      if (mLastClearTime != 0L)
      {
        paramString.append(" lastClearTime=");
        TimeUtils.formatDuration(mLastClearTime - l, paramString);
      }
      if (mLastCleanupTime != 0L)
      {
        paramString.append(" lastCleanupTime=");
        TimeUtils.formatDuration(mLastCleanupTime - l, paramString);
      }
      Slog.wtf("BatteryStatsImpl", paramString.toString());
      return null;
    }
  }
  
  public static abstract interface PlatformIdleStateCallback
  {
    public abstract void fillLowPowerStats(RpmStats paramRpmStats);
    
    public abstract String getPlatformLowPowerStats();
    
    public abstract String getSubsystemLowPowerStats();
  }
  
  public static class SamplingTimer
    extends BatteryStatsImpl.Timer
  {
    int mCurrentReportedCount;
    long mCurrentReportedTotalTime;
    boolean mTimeBaseRunning;
    boolean mTrackingReportedValues;
    int mUnpluggedReportedCount;
    long mUnpluggedReportedTotalTime;
    int mUpdateVersion;
    
    @VisibleForTesting
    public SamplingTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.TimeBase paramTimeBase)
    {
      super(0, paramTimeBase);
      mTrackingReportedValues = false;
      mTimeBaseRunning = paramTimeBase.isRunning();
    }
    
    @VisibleForTesting
    public SamplingTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      super(0, paramTimeBase, paramParcel);
      mCurrentReportedCount = paramParcel.readInt();
      mUnpluggedReportedCount = paramParcel.readInt();
      mCurrentReportedTotalTime = paramParcel.readLong();
      mUnpluggedReportedTotalTime = paramParcel.readLong();
      if (paramParcel.readInt() == 1) {
        bool = true;
      }
      mTrackingReportedValues = bool;
      mTimeBaseRunning = paramTimeBase.isRunning();
    }
    
    public void add(long paramLong, int paramInt)
    {
      update(mCurrentReportedTotalTime + paramLong, mCurrentReportedCount + paramInt);
    }
    
    protected int computeCurrentCountLocked()
    {
      int i = mCount;
      int j;
      if ((mTimeBaseRunning) && (mTrackingReportedValues)) {
        j = mCurrentReportedCount - mUnpluggedReportedCount;
      } else {
        j = 0;
      }
      return i + j;
    }
    
    protected long computeRunTimeLocked(long paramLong)
    {
      long l = mTotalTime;
      if ((mTimeBaseRunning) && (mTrackingReportedValues)) {
        paramLong = mCurrentReportedTotalTime - mUnpluggedReportedTotalTime;
      } else {
        paramLong = 0L;
      }
      return l + paramLong;
    }
    
    public void endSample()
    {
      mTotalTime = computeRunTimeLocked(0L);
      mCount = computeCurrentCountLocked();
      mCurrentReportedTotalTime = 0L;
      mUnpluggedReportedTotalTime = 0L;
      mCurrentReportedCount = 0;
      mUnpluggedReportedCount = 0;
    }
    
    public int getUpdateVersion()
    {
      return mUpdateVersion;
    }
    
    public void logState(Printer paramPrinter, String paramString)
    {
      super.logState(paramPrinter, paramString);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mCurrentReportedCount=");
      localStringBuilder.append(mCurrentReportedCount);
      localStringBuilder.append(" mUnpluggedReportedCount=");
      localStringBuilder.append(mUnpluggedReportedCount);
      localStringBuilder.append(" mCurrentReportedTotalTime=");
      localStringBuilder.append(mCurrentReportedTotalTime);
      localStringBuilder.append(" mUnpluggedReportedTotalTime=");
      localStringBuilder.append(mUnpluggedReportedTotalTime);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
    {
      super.onTimeStarted(paramLong1, paramLong2, paramLong3);
      if (mTrackingReportedValues)
      {
        mUnpluggedReportedTotalTime = mCurrentReportedTotalTime;
        mUnpluggedReportedCount = mCurrentReportedCount;
      }
      mTimeBaseRunning = true;
    }
    
    public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3)
    {
      super.onTimeStopped(paramLong1, paramLong2, paramLong3);
      mTimeBaseRunning = false;
    }
    
    public boolean reset(boolean paramBoolean)
    {
      super.reset(paramBoolean);
      mTrackingReportedValues = false;
      mUnpluggedReportedTotalTime = 0L;
      mUnpluggedReportedCount = 0;
      return true;
    }
    
    public void setUpdateVersion(int paramInt)
    {
      mUpdateVersion = paramInt;
    }
    
    public void update(long paramLong, int paramInt)
    {
      if ((mTimeBaseRunning) && (!mTrackingReportedValues))
      {
        mUnpluggedReportedTotalTime = paramLong;
        mUnpluggedReportedCount = paramInt;
      }
      mTrackingReportedValues = true;
      if ((paramLong < mCurrentReportedTotalTime) || (paramInt < mCurrentReportedCount)) {
        endSample();
      }
      mCurrentReportedTotalTime = paramLong;
      mCurrentReportedCount = paramInt;
    }
    
    public void writeToParcel(Parcel paramParcel, long paramLong)
    {
      super.writeToParcel(paramParcel, paramLong);
      paramParcel.writeInt(mCurrentReportedCount);
      paramParcel.writeInt(mUnpluggedReportedCount);
      paramParcel.writeLong(mCurrentReportedTotalTime);
      paramParcel.writeLong(mUnpluggedReportedTotalTime);
      paramParcel.writeInt(mTrackingReportedValues);
    }
  }
  
  public static class StopwatchTimer
    extends BatteryStatsImpl.Timer
  {
    long mAcquireTime = -1L;
    @VisibleForTesting
    public boolean mInList;
    int mNesting;
    long mTimeout;
    final ArrayList<StopwatchTimer> mTimerPool;
    final BatteryStatsImpl.Uid mUid;
    long mUpdateTime;
    
    public StopwatchTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.Uid paramUid, int paramInt, ArrayList<StopwatchTimer> paramArrayList, BatteryStatsImpl.TimeBase paramTimeBase)
    {
      super(paramInt, paramTimeBase);
      mUid = paramUid;
      mTimerPool = paramArrayList;
    }
    
    public StopwatchTimer(BatteryStatsImpl.Clocks paramClocks, BatteryStatsImpl.Uid paramUid, int paramInt, ArrayList<StopwatchTimer> paramArrayList, BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      super(paramInt, paramTimeBase, paramParcel);
      mUid = paramUid;
      mTimerPool = paramArrayList;
      mUpdateTime = paramParcel.readLong();
    }
    
    private static long refreshTimersLocked(long paramLong, ArrayList<StopwatchTimer> paramArrayList, StopwatchTimer paramStopwatchTimer)
    {
      long l1 = 0L;
      int i = paramArrayList.size();
      int j = i - 1;
      while (j >= 0)
      {
        StopwatchTimer localStopwatchTimer = (StopwatchTimer)paramArrayList.get(j);
        long l2 = paramLong - mUpdateTime;
        long l3 = l1;
        if (l2 > 0L)
        {
          l3 = l2 / i;
          if (localStopwatchTimer == paramStopwatchTimer) {
            l1 = l3;
          }
          mTotalTime += l3;
          l3 = l1;
        }
        mUpdateTime = paramLong;
        j--;
        l1 = l3;
      }
      return l1;
    }
    
    protected int computeCurrentCountLocked()
    {
      return mCount;
    }
    
    protected long computeRunTimeLocked(long paramLong)
    {
      long l1 = mTimeout;
      long l2 = 0L;
      long l3 = paramLong;
      if (l1 > 0L)
      {
        l3 = paramLong;
        if (paramLong > mUpdateTime + mTimeout) {
          l3 = mUpdateTime + mTimeout;
        }
      }
      l1 = mTotalTime;
      paramLong = l2;
      if (mNesting > 0)
      {
        paramLong = mUpdateTime;
        int i;
        if (mTimerPool != null) {
          i = mTimerPool.size();
        } else {
          i = 1;
        }
        paramLong = (l3 - paramLong) / i;
      }
      return l1 + paramLong;
    }
    
    public void detach()
    {
      super.detach();
      if (mTimerPool != null) {
        mTimerPool.remove(this);
      }
    }
    
    public boolean isRunningLocked()
    {
      boolean bool;
      if (mNesting > 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public void logState(Printer paramPrinter, String paramString)
    {
      super.logState(paramPrinter, paramString);
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mNesting=");
      localStringBuilder.append(mNesting);
      localStringBuilder.append(" mUpdateTime=");
      localStringBuilder.append(mUpdateTime);
      localStringBuilder.append(" mAcquireTime=");
      localStringBuilder.append(mAcquireTime);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3)
    {
      if (mNesting > 0)
      {
        super.onTimeStopped(paramLong1, paramLong2, paramLong3);
        mUpdateTime = paramLong3;
      }
    }
    
    public void readSummaryFromParcelLocked(Parcel paramParcel)
    {
      super.readSummaryFromParcelLocked(paramParcel);
      mNesting = 0;
    }
    
    public boolean reset(boolean paramBoolean)
    {
      int i = mNesting;
      boolean bool1 = false;
      boolean bool2;
      if (i <= 0) {
        bool2 = true;
      } else {
        bool2 = false;
      }
      boolean bool3 = bool1;
      if (bool2)
      {
        bool3 = bool1;
        if (paramBoolean) {
          bool3 = true;
        }
      }
      super.reset(bool3);
      if (mNesting > 0) {
        mUpdateTime = mTimeBase.getRealtime(mClocks.elapsedRealtime() * 1000L);
      }
      mAcquireTime = -1L;
      return bool2;
    }
    
    public void setMark(long paramLong)
    {
      paramLong = mTimeBase.getRealtime(1000L * paramLong);
      if (mNesting > 0) {
        if (mTimerPool != null)
        {
          refreshTimersLocked(paramLong, mTimerPool, this);
        }
        else
        {
          mTotalTime += paramLong - mUpdateTime;
          mUpdateTime = paramLong;
        }
      }
      mTimeBeforeMark = mTotalTime;
    }
    
    public void setTimeout(long paramLong)
    {
      mTimeout = paramLong;
    }
    
    public void startRunningLocked(long paramLong)
    {
      int i = mNesting;
      mNesting = (i + 1);
      if (i == 0)
      {
        paramLong = mTimeBase.getRealtime(1000L * paramLong);
        mUpdateTime = paramLong;
        if (mTimerPool != null)
        {
          refreshTimersLocked(paramLong, mTimerPool, null);
          mTimerPool.add(this);
        }
        if (mTimeBase.isRunning())
        {
          mCount += 1;
          mAcquireTime = mTotalTime;
        }
        else
        {
          mAcquireTime = -1L;
        }
      }
    }
    
    public void stopAllRunningLocked(long paramLong)
    {
      if (mNesting > 0)
      {
        mNesting = 1;
        stopRunningLocked(paramLong);
      }
    }
    
    public void stopRunningLocked(long paramLong)
    {
      if (mNesting == 0) {
        return;
      }
      int i = mNesting - 1;
      mNesting = i;
      if (i == 0)
      {
        paramLong = mTimeBase.getRealtime(1000L * paramLong);
        if (mTimerPool != null)
        {
          refreshTimersLocked(paramLong, mTimerPool, null);
          mTimerPool.remove(this);
        }
        else
        {
          mNesting = 1;
          mTotalTime = computeRunTimeLocked(paramLong);
          mNesting = 0;
        }
        if ((mAcquireTime >= 0L) && (mTotalTime == mAcquireTime)) {
          mCount -= 1;
        }
      }
    }
    
    public void writeToParcel(Parcel paramParcel, long paramLong)
    {
      super.writeToParcel(paramParcel, paramLong);
      paramParcel.writeLong(mUpdateTime);
    }
  }
  
  public static class SystemClocks
    implements BatteryStatsImpl.Clocks
  {
    public SystemClocks() {}
    
    public long elapsedRealtime()
    {
      return SystemClock.elapsedRealtime();
    }
    
    public long uptimeMillis()
    {
      return SystemClock.uptimeMillis();
    }
  }
  
  public static class TimeBase
  {
    protected final ArrayList<BatteryStatsImpl.TimeBaseObs> mObservers = new ArrayList();
    protected long mPastRealtime;
    protected long mPastUptime;
    protected long mRealtime;
    protected long mRealtimeStart;
    protected boolean mRunning;
    protected long mUnpluggedRealtime;
    protected long mUnpluggedUptime;
    protected long mUptime;
    protected long mUptimeStart;
    
    public TimeBase() {}
    
    public void add(BatteryStatsImpl.TimeBaseObs paramTimeBaseObs)
    {
      mObservers.add(paramTimeBaseObs);
    }
    
    public long computeRealtime(long paramLong, int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return 0L;
      case 2: 
        return getRealtime(paramLong) - mUnpluggedRealtime;
      case 1: 
        return getRealtime(paramLong);
      }
      return mRealtime + getRealtime(paramLong);
    }
    
    public long computeUptime(long paramLong, int paramInt)
    {
      switch (paramInt)
      {
      default: 
        return 0L;
      case 2: 
        return getUptime(paramLong) - mUnpluggedUptime;
      case 1: 
        return getUptime(paramLong);
      }
      return mUptime + getUptime(paramLong);
    }
    
    public void dump(PrintWriter paramPrintWriter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mRunning=");
      paramPrintWriter.println(mRunning);
      localStringBuilder.setLength(0);
      localStringBuilder.append(paramString);
      localStringBuilder.append("mUptime=");
      BatteryStats.formatTimeMs(localStringBuilder, mUptime / 1000L);
      paramPrintWriter.println(localStringBuilder.toString());
      localStringBuilder.setLength(0);
      localStringBuilder.append(paramString);
      localStringBuilder.append("mRealtime=");
      BatteryStats.formatTimeMs(localStringBuilder, mRealtime / 1000L);
      paramPrintWriter.println(localStringBuilder.toString());
      localStringBuilder.setLength(0);
      localStringBuilder.append(paramString);
      localStringBuilder.append("mPastUptime=");
      BatteryStats.formatTimeMs(localStringBuilder, mPastUptime / 1000L);
      localStringBuilder.append("mUptimeStart=");
      BatteryStats.formatTimeMs(localStringBuilder, mUptimeStart / 1000L);
      localStringBuilder.append("mUnpluggedUptime=");
      BatteryStats.formatTimeMs(localStringBuilder, mUnpluggedUptime / 1000L);
      paramPrintWriter.println(localStringBuilder.toString());
      localStringBuilder.setLength(0);
      localStringBuilder.append(paramString);
      localStringBuilder.append("mPastRealtime=");
      BatteryStats.formatTimeMs(localStringBuilder, mPastRealtime / 1000L);
      localStringBuilder.append("mRealtimeStart=");
      BatteryStats.formatTimeMs(localStringBuilder, mRealtimeStart / 1000L);
      localStringBuilder.append("mUnpluggedRealtime=");
      BatteryStats.formatTimeMs(localStringBuilder, mUnpluggedRealtime / 1000L);
      paramPrintWriter.println(localStringBuilder.toString());
    }
    
    public long getRealtime(long paramLong)
    {
      long l1 = mPastRealtime;
      long l2 = l1;
      if (mRunning) {
        l2 = l1 + (paramLong - mRealtimeStart);
      }
      return l2;
    }
    
    public long getRealtimeStart()
    {
      return mRealtimeStart;
    }
    
    public long getUptime(long paramLong)
    {
      long l1 = mPastUptime;
      long l2 = l1;
      if (mRunning) {
        l2 = l1 + (paramLong - mUptimeStart);
      }
      return l2;
    }
    
    public long getUptimeStart()
    {
      return mUptimeStart;
    }
    
    public boolean hasObserver(BatteryStatsImpl.TimeBaseObs paramTimeBaseObs)
    {
      return mObservers.contains(paramTimeBaseObs);
    }
    
    public void init(long paramLong1, long paramLong2)
    {
      mRealtime = 0L;
      mUptime = 0L;
      mPastUptime = 0L;
      mPastRealtime = 0L;
      mUptimeStart = paramLong1;
      mRealtimeStart = paramLong2;
      mUnpluggedUptime = getUptime(mUptimeStart);
      mUnpluggedRealtime = getRealtime(mRealtimeStart);
    }
    
    public boolean isRunning()
    {
      return mRunning;
    }
    
    public void readFromParcel(Parcel paramParcel)
    {
      mRunning = false;
      mUptime = paramParcel.readLong();
      mPastUptime = paramParcel.readLong();
      mUptimeStart = paramParcel.readLong();
      mRealtime = paramParcel.readLong();
      mPastRealtime = paramParcel.readLong();
      mRealtimeStart = paramParcel.readLong();
      mUnpluggedUptime = paramParcel.readLong();
      mUnpluggedRealtime = paramParcel.readLong();
    }
    
    public void readSummaryFromParcel(Parcel paramParcel)
    {
      mUptime = paramParcel.readLong();
      mRealtime = paramParcel.readLong();
    }
    
    public void remove(BatteryStatsImpl.TimeBaseObs paramTimeBaseObs)
    {
      if (!mObservers.remove(paramTimeBaseObs))
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Removed unknown observer: ");
        localStringBuilder.append(paramTimeBaseObs);
        Slog.wtf("BatteryStatsImpl", localStringBuilder.toString());
      }
    }
    
    public void reset(long paramLong1, long paramLong2)
    {
      if (!mRunning)
      {
        mPastUptime = 0L;
        mPastRealtime = 0L;
      }
      else
      {
        mUptimeStart = paramLong1;
        mRealtimeStart = paramLong2;
        mUnpluggedUptime = getUptime(paramLong1);
        mUnpluggedRealtime = getRealtime(paramLong2);
      }
    }
    
    public boolean setRunning(boolean paramBoolean, long paramLong1, long paramLong2)
    {
      if (mRunning != paramBoolean)
      {
        mRunning = paramBoolean;
        long l;
        int i;
        if (paramBoolean)
        {
          mUptimeStart = paramLong1;
          mRealtimeStart = paramLong2;
          l = getUptime(paramLong1);
          mUnpluggedUptime = l;
          paramLong1 = getRealtime(paramLong2);
          mUnpluggedRealtime = paramLong1;
          for (i = mObservers.size() - 1; i >= 0; i--) {
            ((BatteryStatsImpl.TimeBaseObs)mObservers.get(i)).onTimeStarted(paramLong2, l, paramLong1);
          }
        }
        else
        {
          mPastUptime += paramLong1 - mUptimeStart;
          mPastRealtime += paramLong2 - mRealtimeStart;
          paramLong1 = getUptime(paramLong1);
          l = getRealtime(paramLong2);
          for (i = mObservers.size() - 1; i >= 0; i--) {
            ((BatteryStatsImpl.TimeBaseObs)mObservers.get(i)).onTimeStopped(paramLong2, paramLong1, l);
          }
        }
        return true;
      }
      return false;
    }
    
    public void writeSummaryToParcel(Parcel paramParcel, long paramLong1, long paramLong2)
    {
      paramParcel.writeLong(computeUptime(paramLong1, 0));
      paramParcel.writeLong(computeRealtime(paramLong2, 0));
    }
    
    public void writeToParcel(Parcel paramParcel, long paramLong1, long paramLong2)
    {
      paramLong1 = getUptime(paramLong1);
      paramLong2 = getRealtime(paramLong2);
      paramParcel.writeLong(mUptime);
      paramParcel.writeLong(paramLong1);
      paramParcel.writeLong(mUptimeStart);
      paramParcel.writeLong(mRealtime);
      paramParcel.writeLong(paramLong2);
      paramParcel.writeLong(mRealtimeStart);
      paramParcel.writeLong(mUnpluggedUptime);
      paramParcel.writeLong(mUnpluggedRealtime);
    }
  }
  
  public static abstract interface TimeBaseObs
  {
    public abstract void onTimeStarted(long paramLong1, long paramLong2, long paramLong3);
    
    public abstract void onTimeStopped(long paramLong1, long paramLong2, long paramLong3);
  }
  
  public static abstract class Timer
    extends BatteryStats.Timer
    implements BatteryStatsImpl.TimeBaseObs
  {
    protected final BatteryStatsImpl.Clocks mClocks;
    protected int mCount;
    protected int mLastCount;
    protected long mLastTime;
    protected int mLoadedCount;
    protected long mLoadedTime;
    protected final BatteryStatsImpl.TimeBase mTimeBase;
    protected long mTimeBeforeMark;
    protected long mTotalTime;
    protected final int mType;
    protected int mUnpluggedCount;
    protected long mUnpluggedTime;
    
    public Timer(BatteryStatsImpl.Clocks paramClocks, int paramInt, BatteryStatsImpl.TimeBase paramTimeBase)
    {
      mClocks = paramClocks;
      mType = paramInt;
      mTimeBase = paramTimeBase;
      paramTimeBase.add(this);
    }
    
    public Timer(BatteryStatsImpl.Clocks paramClocks, int paramInt, BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
    {
      mClocks = paramClocks;
      mType = paramInt;
      mTimeBase = paramTimeBase;
      mCount = paramParcel.readInt();
      mLoadedCount = paramParcel.readInt();
      mLastCount = 0;
      mUnpluggedCount = paramParcel.readInt();
      mTotalTime = paramParcel.readLong();
      mLoadedTime = paramParcel.readLong();
      mLastTime = 0L;
      mUnpluggedTime = paramParcel.readLong();
      mTimeBeforeMark = paramParcel.readLong();
      paramTimeBase.add(this);
    }
    
    public static void writeTimerToParcel(Parcel paramParcel, Timer paramTimer, long paramLong)
    {
      if (paramTimer == null)
      {
        paramParcel.writeInt(0);
        return;
      }
      paramParcel.writeInt(1);
      paramTimer.writeToParcel(paramParcel, paramLong);
    }
    
    protected abstract int computeCurrentCountLocked();
    
    protected abstract long computeRunTimeLocked(long paramLong);
    
    public void detach()
    {
      mTimeBase.remove(this);
    }
    
    public int getCountLocked(int paramInt)
    {
      int i = computeCurrentCountLocked();
      int j;
      if (paramInt == 2)
      {
        j = i - mUnpluggedCount;
      }
      else
      {
        j = i;
        if (paramInt != 0) {
          j = i - mLoadedCount;
        }
      }
      return j;
    }
    
    public long getTimeSinceMarkLocked(long paramLong)
    {
      return computeRunTimeLocked(mTimeBase.getRealtime(paramLong)) - mTimeBeforeMark;
    }
    
    public long getTotalTimeLocked(long paramLong, int paramInt)
    {
      long l = computeRunTimeLocked(mTimeBase.getRealtime(paramLong));
      if (paramInt == 2)
      {
        paramLong = l - mUnpluggedTime;
      }
      else
      {
        paramLong = l;
        if (paramInt != 0) {
          paramLong = l - mLoadedTime;
        }
      }
      return paramLong;
    }
    
    public void logState(Printer paramPrinter, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mCount=");
      localStringBuilder.append(mCount);
      localStringBuilder.append(" mLoadedCount=");
      localStringBuilder.append(mLoadedCount);
      localStringBuilder.append(" mLastCount=");
      localStringBuilder.append(mLastCount);
      localStringBuilder.append(" mUnpluggedCount=");
      localStringBuilder.append(mUnpluggedCount);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mTotalTime=");
      localStringBuilder.append(mTotalTime);
      localStringBuilder.append(" mLoadedTime=");
      localStringBuilder.append(mLoadedTime);
      paramPrinter.println(localStringBuilder.toString());
      localStringBuilder = new StringBuilder();
      localStringBuilder.append(paramString);
      localStringBuilder.append("mLastTime=");
      localStringBuilder.append(mLastTime);
      localStringBuilder.append(" mUnpluggedTime=");
      localStringBuilder.append(mUnpluggedTime);
      paramPrinter.println(localStringBuilder.toString());
    }
    
    public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
    {
      mUnpluggedTime = computeRunTimeLocked(paramLong3);
      mUnpluggedCount = computeCurrentCountLocked();
    }
    
    public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3)
    {
      mTotalTime = computeRunTimeLocked(paramLong3);
      mCount = computeCurrentCountLocked();
    }
    
    public void readSummaryFromParcelLocked(Parcel paramParcel)
    {
      long l = paramParcel.readLong();
      mLoadedTime = l;
      mTotalTime = l;
      mLastTime = 0L;
      mUnpluggedTime = mTotalTime;
      int i = paramParcel.readInt();
      mLoadedCount = i;
      mCount = i;
      mLastCount = 0;
      mUnpluggedCount = mCount;
      mTimeBeforeMark = mTotalTime;
    }
    
    public boolean reset(boolean paramBoolean)
    {
      mTimeBeforeMark = 0L;
      mLastTime = 0L;
      mLoadedTime = 0L;
      mTotalTime = 0L;
      mLastCount = 0;
      mLoadedCount = 0;
      mCount = 0;
      if (paramBoolean) {
        detach();
      }
      return true;
    }
    
    public void writeSummaryFromParcelLocked(Parcel paramParcel, long paramLong)
    {
      paramParcel.writeLong(computeRunTimeLocked(mTimeBase.getRealtime(paramLong)));
      paramParcel.writeInt(computeCurrentCountLocked());
    }
    
    public void writeToParcel(Parcel paramParcel, long paramLong)
    {
      paramParcel.writeInt(computeCurrentCountLocked());
      paramParcel.writeInt(mLoadedCount);
      paramParcel.writeInt(mUnpluggedCount);
      paramParcel.writeLong(computeRunTimeLocked(mTimeBase.getRealtime(paramLong)));
      paramParcel.writeLong(mLoadedTime);
      paramParcel.writeLong(mUnpluggedTime);
      paramParcel.writeLong(mTimeBeforeMark);
    }
  }
  
  public static class Uid
    extends BatteryStats.Uid
  {
    static final int NO_BATCHED_SCAN_STARTED = -1;
    BatteryStatsImpl.DualTimer mAggregatedPartialWakelockTimer;
    BatteryStatsImpl.StopwatchTimer mAudioTurnedOnTimer;
    private BatteryStatsImpl.ControllerActivityCounterImpl mBluetoothControllerActivity;
    BatteryStatsImpl.Counter mBluetoothScanResultBgCounter;
    BatteryStatsImpl.Counter mBluetoothScanResultCounter;
    BatteryStatsImpl.DualTimer mBluetoothScanTimer;
    BatteryStatsImpl.DualTimer mBluetoothUnoptimizedScanTimer;
    protected BatteryStatsImpl mBsi;
    BatteryStatsImpl.StopwatchTimer mCameraTurnedOnTimer;
    IntArray mChildUids;
    BatteryStatsImpl.LongSamplingCounter mCpuActiveTimeMs;
    BatteryStatsImpl.LongSamplingCounter[][] mCpuClusterSpeedTimesUs;
    BatteryStatsImpl.LongSamplingCounterArray mCpuClusterTimesMs;
    BatteryStatsImpl.LongSamplingCounterArray mCpuFreqTimeMs;
    long mCurStepSystemTime;
    long mCurStepUserTime;
    BatteryStatsImpl.StopwatchTimer mFlashlightTurnedOnTimer;
    BatteryStatsImpl.StopwatchTimer mForegroundActivityTimer;
    BatteryStatsImpl.StopwatchTimer mForegroundServiceTimer;
    boolean mFullWifiLockOut;
    BatteryStatsImpl.StopwatchTimer mFullWifiLockTimer;
    boolean mInForegroundService = false;
    final ArrayMap<String, SparseIntArray> mJobCompletions = new ArrayMap();
    final BatteryStatsImpl.OverflowArrayMap<BatteryStatsImpl.DualTimer> mJobStats;
    BatteryStatsImpl.Counter mJobsDeferredCount;
    BatteryStatsImpl.Counter mJobsDeferredEventCount;
    final BatteryStatsImpl.Counter[] mJobsFreshnessBuckets;
    BatteryStatsImpl.LongSamplingCounter mJobsFreshnessTimeMs;
    long mLastStepSystemTime;
    long mLastStepUserTime;
    BatteryStatsImpl.LongSamplingCounter mMobileRadioActiveCount;
    BatteryStatsImpl.LongSamplingCounter mMobileRadioActiveTime;
    private BatteryStatsImpl.LongSamplingCounter mMobileRadioApWakeupCount;
    private BatteryStatsImpl.ControllerActivityCounterImpl mModemControllerActivity;
    BatteryStatsImpl.LongSamplingCounter[] mNetworkByteActivityCounters;
    BatteryStatsImpl.LongSamplingCounter[] mNetworkPacketActivityCounters;
    @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
    public final BatteryStatsImpl.TimeBase mOnBatteryBackgroundTimeBase;
    @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
    public final BatteryStatsImpl.TimeBase mOnBatteryScreenOffBackgroundTimeBase;
    final ArrayMap<String, Pkg> mPackageStats = new ArrayMap();
    final SparseArray<BatteryStats.Uid.Pid> mPids = new SparseArray();
    BatteryStatsImpl.LongSamplingCounterArray[] mProcStateScreenOffTimeMs;
    BatteryStatsImpl.LongSamplingCounterArray[] mProcStateTimeMs;
    int mProcessState = 19;
    BatteryStatsImpl.StopwatchTimer[] mProcessStateTimer;
    final ArrayMap<String, Proc> mProcessStats = new ArrayMap();
    BatteryStatsImpl.LongSamplingCounterArray mScreenOffCpuFreqTimeMs;
    final SparseArray<Sensor> mSensorStats = new SparseArray();
    final BatteryStatsImpl.OverflowArrayMap<BatteryStatsImpl.DualTimer> mSyncStats;
    BatteryStatsImpl.LongSamplingCounter mSystemCpuTime;
    final int mUid;
    BatteryStatsImpl.Counter[] mUserActivityCounters;
    BatteryStatsImpl.LongSamplingCounter mUserCpuTime;
    BatteryStatsImpl.BatchTimer mVibratorOnTimer;
    BatteryStatsImpl.StopwatchTimer mVideoTurnedOnTimer;
    final BatteryStatsImpl.OverflowArrayMap<Wakelock> mWakelockStats;
    int mWifiBatchedScanBinStarted = -1;
    BatteryStatsImpl.StopwatchTimer[] mWifiBatchedScanTimer;
    private BatteryStatsImpl.ControllerActivityCounterImpl mWifiControllerActivity;
    boolean mWifiMulticastEnabled;
    BatteryStatsImpl.StopwatchTimer mWifiMulticastTimer;
    private BatteryStatsImpl.LongSamplingCounter mWifiRadioApWakeupCount;
    boolean mWifiRunning;
    BatteryStatsImpl.StopwatchTimer mWifiRunningTimer;
    boolean mWifiScanStarted;
    BatteryStatsImpl.DualTimer mWifiScanTimer;
    
    public Uid(BatteryStatsImpl paramBatteryStatsImpl, int paramInt)
    {
      mBsi = paramBatteryStatsImpl;
      mUid = paramInt;
      mOnBatteryBackgroundTimeBase = new BatteryStatsImpl.TimeBase();
      mOnBatteryBackgroundTimeBase.init(mBsi.mClocks.uptimeMillis() * 1000L, mBsi.mClocks.elapsedRealtime() * 1000L);
      mOnBatteryScreenOffBackgroundTimeBase = new BatteryStatsImpl.TimeBase();
      mOnBatteryScreenOffBackgroundTimeBase.init(mBsi.mClocks.uptimeMillis() * 1000L, mBsi.mClocks.elapsedRealtime() * 1000L);
      mUserCpuTime = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
      mSystemCpuTime = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
      mCpuActiveTimeMs = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
      mCpuClusterTimesMs = new BatteryStatsImpl.LongSamplingCounterArray(mBsi.mOnBatteryTimeBase);
      paramBatteryStatsImpl = mBsi;
      Objects.requireNonNull(paramBatteryStatsImpl);
      mWakelockStats = new BatteryStatsImpl.OverflowArrayMap(paramBatteryStatsImpl, paramInt)
      {
        public BatteryStatsImpl.Uid.Wakelock instantiateObject()
        {
          return new BatteryStatsImpl.Uid.Wakelock(mBsi, BatteryStatsImpl.Uid.this);
        }
      };
      paramBatteryStatsImpl = mBsi;
      Objects.requireNonNull(paramBatteryStatsImpl);
      mSyncStats = new BatteryStatsImpl.OverflowArrayMap(paramBatteryStatsImpl, paramInt)
      {
        public BatteryStatsImpl.DualTimer instantiateObject()
        {
          return new BatteryStatsImpl.DualTimer(mBsi.mClocks, BatteryStatsImpl.Uid.this, 13, null, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase);
        }
      };
      paramBatteryStatsImpl = mBsi;
      Objects.requireNonNull(paramBatteryStatsImpl);
      mJobStats = new BatteryStatsImpl.OverflowArrayMap(paramBatteryStatsImpl, paramInt)
      {
        public BatteryStatsImpl.DualTimer instantiateObject()
        {
          return new BatteryStatsImpl.DualTimer(mBsi.mClocks, BatteryStatsImpl.Uid.this, 14, null, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase);
        }
      };
      mWifiRunningTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 4, mBsi.mWifiRunningTimers, mBsi.mOnBatteryTimeBase);
      mFullWifiLockTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 5, mBsi.mFullWifiLockTimers, mBsi.mOnBatteryTimeBase);
      mWifiScanTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 6, mBsi.mWifiScanTimers, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase);
      mWifiBatchedScanTimer = new BatteryStatsImpl.StopwatchTimer[5];
      mWifiMulticastTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 7, mBsi.mWifiMulticastTimers, mBsi.mOnBatteryTimeBase);
      mProcessStateTimer = new BatteryStatsImpl.StopwatchTimer[7];
      mJobsDeferredEventCount = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase);
      mJobsDeferredCount = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase);
      mJobsFreshnessTimeMs = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
      mJobsFreshnessBuckets = new BatteryStatsImpl.Counter[BatteryStats.JOB_FRESHNESS_BUCKETS.length];
    }
    
    private void addProcStateScreenOffTimesMs(int paramInt, long[] paramArrayOfLong, boolean paramBoolean)
    {
      if (mProcStateScreenOffTimeMs == null) {
        mProcStateScreenOffTimeMs = new BatteryStatsImpl.LongSamplingCounterArray[7];
      }
      if ((mProcStateScreenOffTimeMs[paramInt] == null) || (mProcStateScreenOffTimeMs[paramInt].getSize() != paramArrayOfLong.length)) {
        mProcStateScreenOffTimeMs[paramInt] = new BatteryStatsImpl.LongSamplingCounterArray(mBsi.mOnBatteryScreenOffTimeBase);
      }
      mProcStateScreenOffTimeMs[paramInt].addCountLocked(paramArrayOfLong, paramBoolean);
    }
    
    private void addProcStateTimesMs(int paramInt, long[] paramArrayOfLong, boolean paramBoolean)
    {
      if (mProcStateTimeMs == null) {
        mProcStateTimeMs = new BatteryStatsImpl.LongSamplingCounterArray[7];
      }
      if ((mProcStateTimeMs[paramInt] == null) || (mProcStateTimeMs[paramInt].getSize() != paramArrayOfLong.length)) {
        mProcStateTimeMs[paramInt] = new BatteryStatsImpl.LongSamplingCounterArray(mBsi.mOnBatteryTimeBase);
      }
      mProcStateTimeMs[paramInt].addCountLocked(paramArrayOfLong, paramBoolean);
    }
    
    private long[] nullIfAllZeros(BatteryStatsImpl.LongSamplingCounterArray paramLongSamplingCounterArray, int paramInt)
    {
      if (paramLongSamplingCounterArray == null) {
        return null;
      }
      paramLongSamplingCounterArray = paramLongSamplingCounterArray.getCountsLocked(paramInt);
      if (paramLongSamplingCounterArray == null) {
        return null;
      }
      for (paramInt = paramLongSamplingCounterArray.length - 1; paramInt >= 0; paramInt--) {
        if (paramLongSamplingCounterArray[paramInt] != 0L) {
          return paramLongSamplingCounterArray;
        }
      }
      return null;
    }
    
    public void addIsolatedUid(int paramInt)
    {
      if (mChildUids == null) {
        mChildUids = new IntArray();
      } else if (mChildUids.indexOf(paramInt) >= 0) {
        return;
      }
      mChildUids.add(paramInt);
    }
    
    public BatteryStatsImpl.DualTimer createAggregatedPartialWakelockTimerLocked()
    {
      if (mAggregatedPartialWakelockTimer == null) {
        mAggregatedPartialWakelockTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 20, null, mBsi.mOnBatteryScreenOffTimeBase, mOnBatteryScreenOffBackgroundTimeBase);
      }
      return mAggregatedPartialWakelockTimer;
    }
    
    public BatteryStatsImpl.StopwatchTimer createAudioTurnedOnTimerLocked()
    {
      if (mAudioTurnedOnTimer == null) {
        mAudioTurnedOnTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 15, mBsi.mAudioTurnedOnTimers, mBsi.mOnBatteryTimeBase);
      }
      return mAudioTurnedOnTimer;
    }
    
    public BatteryStatsImpl.Counter createBluetoothScanResultBgCounterLocked()
    {
      if (mBluetoothScanResultBgCounter == null) {
        mBluetoothScanResultBgCounter = new BatteryStatsImpl.Counter(mOnBatteryBackgroundTimeBase);
      }
      return mBluetoothScanResultBgCounter;
    }
    
    public BatteryStatsImpl.Counter createBluetoothScanResultCounterLocked()
    {
      if (mBluetoothScanResultCounter == null) {
        mBluetoothScanResultCounter = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase);
      }
      return mBluetoothScanResultCounter;
    }
    
    public BatteryStatsImpl.DualTimer createBluetoothScanTimerLocked()
    {
      if (mBluetoothScanTimer == null) {
        mBluetoothScanTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 19, mBsi.mBluetoothScanOnTimers, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase);
      }
      return mBluetoothScanTimer;
    }
    
    public BatteryStatsImpl.DualTimer createBluetoothUnoptimizedScanTimerLocked()
    {
      if (mBluetoothUnoptimizedScanTimer == null) {
        mBluetoothUnoptimizedScanTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 21, null, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase);
      }
      return mBluetoothUnoptimizedScanTimer;
    }
    
    public BatteryStatsImpl.StopwatchTimer createCameraTurnedOnTimerLocked()
    {
      if (mCameraTurnedOnTimer == null) {
        mCameraTurnedOnTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 17, mBsi.mCameraTurnedOnTimers, mBsi.mOnBatteryTimeBase);
      }
      return mCameraTurnedOnTimer;
    }
    
    public BatteryStatsImpl.StopwatchTimer createFlashlightTurnedOnTimerLocked()
    {
      if (mFlashlightTurnedOnTimer == null) {
        mFlashlightTurnedOnTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 16, mBsi.mFlashlightTurnedOnTimers, mBsi.mOnBatteryTimeBase);
      }
      return mFlashlightTurnedOnTimer;
    }
    
    public BatteryStatsImpl.StopwatchTimer createForegroundActivityTimerLocked()
    {
      if (mForegroundActivityTimer == null) {
        mForegroundActivityTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 10, null, mBsi.mOnBatteryTimeBase);
      }
      return mForegroundActivityTimer;
    }
    
    public BatteryStatsImpl.StopwatchTimer createForegroundServiceTimerLocked()
    {
      if (mForegroundServiceTimer == null) {
        mForegroundServiceTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 22, null, mBsi.mOnBatteryTimeBase);
      }
      return mForegroundServiceTimer;
    }
    
    public BatteryStatsImpl.BatchTimer createVibratorOnTimerLocked()
    {
      if (mVibratorOnTimer == null) {
        mVibratorOnTimer = new BatteryStatsImpl.BatchTimer(mBsi.mClocks, this, 9, mBsi.mOnBatteryTimeBase);
      }
      return mVibratorOnTimer;
    }
    
    public BatteryStatsImpl.StopwatchTimer createVideoTurnedOnTimerLocked()
    {
      if (mVideoTurnedOnTimer == null) {
        mVideoTurnedOnTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 8, mBsi.mVideoTurnedOnTimers, mBsi.mOnBatteryTimeBase);
      }
      return mVideoTurnedOnTimer;
    }
    
    public BatteryStatsImpl.Timer getAggregatedPartialWakelockTimer()
    {
      return mAggregatedPartialWakelockTimer;
    }
    
    public BatteryStatsImpl.Timer getAudioTurnedOnTimer()
    {
      return mAudioTurnedOnTimer;
    }
    
    public BatteryStatsImpl getBatteryStats()
    {
      return mBsi;
    }
    
    public BatteryStats.ControllerActivityCounter getBluetoothControllerActivity()
    {
      return mBluetoothControllerActivity;
    }
    
    public BatteryStatsImpl.Timer getBluetoothScanBackgroundTimer()
    {
      if (mBluetoothScanTimer == null) {
        return null;
      }
      return mBluetoothScanTimer.getSubTimer();
    }
    
    public BatteryStatsImpl.Counter getBluetoothScanResultBgCounter()
    {
      return mBluetoothScanResultBgCounter;
    }
    
    public BatteryStatsImpl.Counter getBluetoothScanResultCounter()
    {
      return mBluetoothScanResultCounter;
    }
    
    public BatteryStatsImpl.Timer getBluetoothScanTimer()
    {
      return mBluetoothScanTimer;
    }
    
    public BatteryStatsImpl.Timer getBluetoothUnoptimizedScanBackgroundTimer()
    {
      if (mBluetoothUnoptimizedScanTimer == null) {
        return null;
      }
      return mBluetoothUnoptimizedScanTimer.getSubTimer();
    }
    
    public BatteryStatsImpl.Timer getBluetoothUnoptimizedScanTimer()
    {
      return mBluetoothUnoptimizedScanTimer;
    }
    
    public BatteryStatsImpl.Timer getCameraTurnedOnTimer()
    {
      return mCameraTurnedOnTimer;
    }
    
    public long getCpuActiveTime()
    {
      return mCpuActiveTimeMs.getCountLocked(0);
    }
    
    public long[] getCpuClusterTimes()
    {
      return nullIfAllZeros(mCpuClusterTimesMs, 0);
    }
    
    public long[] getCpuFreqTimes(int paramInt)
    {
      return nullIfAllZeros(mCpuFreqTimeMs, paramInt);
    }
    
    public long[] getCpuFreqTimes(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt1 < 7))
      {
        if (mProcStateTimeMs == null) {
          return null;
        }
        if (!mBsi.mPerProcStateCpuTimesAvailable)
        {
          mProcStateTimeMs = null;
          return null;
        }
        return nullIfAllZeros(mProcStateTimeMs[paramInt2], paramInt1);
      }
      return null;
    }
    
    public void getDeferredJobsCheckinLineLocked(StringBuilder paramStringBuilder, int paramInt)
    {
      int i = 0;
      paramStringBuilder.setLength(0);
      int j = mJobsDeferredEventCount.getCountLocked(paramInt);
      if (j == 0) {
        return;
      }
      int k = mJobsDeferredCount.getCountLocked(paramInt);
      long l = mJobsFreshnessTimeMs.getCountLocked(paramInt);
      paramStringBuilder.append(j);
      paramStringBuilder.append(',');
      paramStringBuilder.append(k);
      paramStringBuilder.append(',');
      paramStringBuilder.append(l);
      while (i < BatteryStats.JOB_FRESHNESS_BUCKETS.length)
      {
        if (mJobsFreshnessBuckets[i] == null)
        {
          paramStringBuilder.append(",0");
        }
        else
        {
          paramStringBuilder.append(",");
          paramStringBuilder.append(mJobsFreshnessBuckets[i].getCountLocked(paramInt));
        }
        i++;
      }
    }
    
    public void getDeferredJobsLineLocked(StringBuilder paramStringBuilder, int paramInt)
    {
      int i = 0;
      paramStringBuilder.setLength(0);
      int j = mJobsDeferredEventCount.getCountLocked(paramInt);
      if (j == 0) {
        return;
      }
      int k = mJobsDeferredCount.getCountLocked(paramInt);
      long l = mJobsFreshnessTimeMs.getCountLocked(paramInt);
      paramStringBuilder.append("times=");
      paramStringBuilder.append(j);
      paramStringBuilder.append(", ");
      paramStringBuilder.append("count=");
      paramStringBuilder.append(k);
      paramStringBuilder.append(", ");
      paramStringBuilder.append("totalLatencyMs=");
      paramStringBuilder.append(l);
      paramStringBuilder.append(", ");
      while (i < BatteryStats.JOB_FRESHNESS_BUCKETS.length)
      {
        paramStringBuilder.append("<");
        paramStringBuilder.append(BatteryStats.JOB_FRESHNESS_BUCKETS[i]);
        paramStringBuilder.append("ms=");
        if (mJobsFreshnessBuckets[i] == null) {
          paramStringBuilder.append("0");
        } else {
          paramStringBuilder.append(mJobsFreshnessBuckets[i].getCountLocked(paramInt));
        }
        paramStringBuilder.append(" ");
        i++;
      }
    }
    
    public BatteryStatsImpl.Timer getFlashlightTurnedOnTimer()
    {
      return mFlashlightTurnedOnTimer;
    }
    
    public BatteryStatsImpl.Timer getForegroundActivityTimer()
    {
      return mForegroundActivityTimer;
    }
    
    public BatteryStatsImpl.Timer getForegroundServiceTimer()
    {
      return mForegroundServiceTimer;
    }
    
    public long getFullWifiLockTime(long paramLong, int paramInt)
    {
      if (mFullWifiLockTimer == null) {
        return 0L;
      }
      return mFullWifiLockTimer.getTotalTimeLocked(paramLong, paramInt);
    }
    
    public ArrayMap<String, SparseIntArray> getJobCompletionStats()
    {
      return mJobCompletions;
    }
    
    public ArrayMap<String, ? extends BatteryStats.Timer> getJobStats()
    {
      return mJobStats.getMap();
    }
    
    public int getMobileRadioActiveCount(int paramInt)
    {
      if (mMobileRadioActiveCount != null) {
        paramInt = (int)mMobileRadioActiveCount.getCountLocked(paramInt);
      } else {
        paramInt = 0;
      }
      return paramInt;
    }
    
    public long getMobileRadioActiveTime(int paramInt)
    {
      long l;
      if (mMobileRadioActiveTime != null) {
        l = mMobileRadioActiveTime.getCountLocked(paramInt);
      } else {
        l = 0L;
      }
      return l;
    }
    
    public long getMobileRadioApWakeupCount(int paramInt)
    {
      if (mMobileRadioApWakeupCount != null) {
        return mMobileRadioApWakeupCount.getCountLocked(paramInt);
      }
      return 0L;
    }
    
    public BatteryStats.ControllerActivityCounter getModemControllerActivity()
    {
      return mModemControllerActivity;
    }
    
    public BatteryStatsImpl.Timer getMulticastWakelockStats()
    {
      return mWifiMulticastTimer;
    }
    
    public long getNetworkActivityBytes(int paramInt1, int paramInt2)
    {
      if ((mNetworkByteActivityCounters != null) && (paramInt1 >= 0) && (paramInt1 < mNetworkByteActivityCounters.length)) {
        return mNetworkByteActivityCounters[paramInt1].getCountLocked(paramInt2);
      }
      return 0L;
    }
    
    public long getNetworkActivityPackets(int paramInt1, int paramInt2)
    {
      if ((mNetworkPacketActivityCounters != null) && (paramInt1 >= 0) && (paramInt1 < mNetworkPacketActivityCounters.length)) {
        return mNetworkPacketActivityCounters[paramInt1].getCountLocked(paramInt2);
      }
      return 0L;
    }
    
    public BatteryStatsImpl.ControllerActivityCounterImpl getOrCreateBluetoothControllerActivityLocked()
    {
      if (mBluetoothControllerActivity == null) {
        mBluetoothControllerActivity = new BatteryStatsImpl.ControllerActivityCounterImpl(mBsi.mOnBatteryTimeBase, 1);
      }
      return mBluetoothControllerActivity;
    }
    
    public BatteryStatsImpl.ControllerActivityCounterImpl getOrCreateModemControllerActivityLocked()
    {
      if (mModemControllerActivity == null) {
        mModemControllerActivity = new BatteryStatsImpl.ControllerActivityCounterImpl(mBsi.mOnBatteryTimeBase, 5);
      }
      return mModemControllerActivity;
    }
    
    public BatteryStatsImpl.ControllerActivityCounterImpl getOrCreateWifiControllerActivityLocked()
    {
      if (mWifiControllerActivity == null) {
        mWifiControllerActivity = new BatteryStatsImpl.ControllerActivityCounterImpl(mBsi.mOnBatteryTimeBase, 1);
      }
      return mWifiControllerActivity;
    }
    
    public ArrayMap<String, ? extends BatteryStats.Uid.Pkg> getPackageStats()
    {
      return mPackageStats;
    }
    
    public Pkg getPackageStatsLocked(String paramString)
    {
      Pkg localPkg1 = (Pkg)mPackageStats.get(paramString);
      Pkg localPkg2 = localPkg1;
      if (localPkg1 == null)
      {
        localPkg2 = new Pkg(mBsi);
        mPackageStats.put(paramString, localPkg2);
      }
      return localPkg2;
    }
    
    public SparseArray<? extends BatteryStats.Uid.Pid> getPidStats()
    {
      return mPids;
    }
    
    public BatteryStats.Uid.Pid getPidStatsLocked(int paramInt)
    {
      BatteryStats.Uid.Pid localPid1 = (BatteryStats.Uid.Pid)mPids.get(paramInt);
      BatteryStats.Uid.Pid localPid2 = localPid1;
      if (localPid1 == null)
      {
        localPid2 = new BatteryStats.Uid.Pid(this);
        mPids.put(paramInt, localPid2);
      }
      return localPid2;
    }
    
    public long getProcessStateTime(int paramInt1, long paramLong, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt1 < 7))
      {
        if (mProcessStateTimer[paramInt1] == null) {
          return 0L;
        }
        return mProcessStateTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
      }
      return 0L;
    }
    
    public BatteryStatsImpl.Timer getProcessStateTimer(int paramInt)
    {
      if ((paramInt >= 0) && (paramInt < 7)) {
        return mProcessStateTimer[paramInt];
      }
      return null;
    }
    
    public ArrayMap<String, ? extends BatteryStats.Uid.Proc> getProcessStats()
    {
      return mProcessStats;
    }
    
    public Proc getProcessStatsLocked(String paramString)
    {
      Proc localProc1 = (Proc)mProcessStats.get(paramString);
      Proc localProc2 = localProc1;
      if (localProc1 == null)
      {
        localProc2 = new Proc(mBsi, paramString);
        mProcessStats.put(paramString, localProc2);
      }
      return localProc2;
    }
    
    public long[] getScreenOffCpuFreqTimes(int paramInt)
    {
      return nullIfAllZeros(mScreenOffCpuFreqTimeMs, paramInt);
    }
    
    public long[] getScreenOffCpuFreqTimes(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt1 < 7))
      {
        if (mProcStateScreenOffTimeMs == null) {
          return null;
        }
        if (!mBsi.mPerProcStateCpuTimesAvailable)
        {
          mProcStateScreenOffTimeMs = null;
          return null;
        }
        return nullIfAllZeros(mProcStateScreenOffTimeMs[paramInt2], paramInt1);
      }
      return null;
    }
    
    public SparseArray<? extends BatteryStats.Uid.Sensor> getSensorStats()
    {
      return mSensorStats;
    }
    
    public BatteryStatsImpl.DualTimer getSensorTimerLocked(int paramInt, boolean paramBoolean)
    {
      Object localObject1 = (Sensor)mSensorStats.get(paramInt);
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        if (!paramBoolean) {
          return null;
        }
        localObject2 = new Sensor(mBsi, this, paramInt);
        mSensorStats.put(paramInt, localObject2);
      }
      localObject1 = mTimer;
      if (localObject1 != null) {
        return localObject1;
      }
      ArrayList localArrayList = (ArrayList)mBsi.mSensorTimers.get(paramInt);
      localObject1 = localArrayList;
      if (localArrayList == null)
      {
        localObject1 = new ArrayList();
        mBsi.mSensorTimers.put(paramInt, localObject1);
      }
      localObject1 = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 3, (ArrayList)localObject1, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase);
      mTimer = ((BatteryStatsImpl.DualTimer)localObject1);
      return localObject1;
    }
    
    public BatteryStatsImpl.Uid.Pkg.Serv getServiceStatsLocked(String paramString1, String paramString2)
    {
      Pkg localPkg = getPackageStatsLocked(paramString1);
      BatteryStatsImpl.Uid.Pkg.Serv localServ = (BatteryStatsImpl.Uid.Pkg.Serv)mServiceStats.get(paramString2);
      paramString1 = localServ;
      if (localServ == null)
      {
        paramString1 = localPkg.newServiceStatsLocked();
        mServiceStats.put(paramString2, paramString1);
      }
      return paramString1;
    }
    
    public ArrayMap<String, ? extends BatteryStats.Timer> getSyncStats()
    {
      return mSyncStats.getMap();
    }
    
    public long getSystemCpuTimeUs(int paramInt)
    {
      return mSystemCpuTime.getCountLocked(paramInt);
    }
    
    public long getTimeAtCpuSpeed(int paramInt1, int paramInt2, int paramInt3)
    {
      if ((mCpuClusterSpeedTimesUs != null) && (paramInt1 >= 0) && (paramInt1 < mCpuClusterSpeedTimesUs.length))
      {
        Object localObject = mCpuClusterSpeedTimesUs[paramInt1];
        if ((localObject != null) && (paramInt2 >= 0) && (paramInt2 < localObject.length))
        {
          localObject = localObject[paramInt2];
          if (localObject != null) {
            return ((BatteryStatsImpl.LongSamplingCounter)localObject).getCountLocked(paramInt3);
          }
        }
      }
      return 0L;
    }
    
    public int getUid()
    {
      return mUid;
    }
    
    public int getUserActivityCount(int paramInt1, int paramInt2)
    {
      if (mUserActivityCounters == null) {
        return 0;
      }
      return mUserActivityCounters[paramInt1].getCountLocked(paramInt2);
    }
    
    public long getUserCpuTimeUs(int paramInt)
    {
      return mUserCpuTime.getCountLocked(paramInt);
    }
    
    public BatteryStatsImpl.Timer getVibratorOnTimer()
    {
      return mVibratorOnTimer;
    }
    
    public BatteryStatsImpl.Timer getVideoTurnedOnTimer()
    {
      return mVideoTurnedOnTimer;
    }
    
    public ArrayMap<String, ? extends BatteryStats.Uid.Wakelock> getWakelockStats()
    {
      return mWakelockStats.getMap();
    }
    
    public BatteryStatsImpl.StopwatchTimer getWakelockTimerLocked(Wakelock paramWakelock, int paramInt)
    {
      if (paramWakelock == null) {
        return null;
      }
      if (paramInt != 18)
      {
        switch (paramInt)
        {
        default: 
          paramWakelock = new StringBuilder();
          paramWakelock.append("type=");
          paramWakelock.append(paramInt);
          throw new IllegalArgumentException(paramWakelock.toString());
        case 2: 
          localObject1 = mTimerWindow;
          localObject2 = localObject1;
          if (localObject1 == null)
          {
            localObject2 = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 2, mBsi.mWindowTimers, mBsi.mOnBatteryTimeBase);
            mTimerWindow = ((BatteryStatsImpl.StopwatchTimer)localObject2);
          }
          return localObject2;
        case 1: 
          localObject1 = mTimerFull;
          localObject2 = localObject1;
          if (localObject1 == null)
          {
            localObject2 = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 1, mBsi.mFullTimers, mBsi.mOnBatteryTimeBase);
            mTimerFull = ((BatteryStatsImpl.StopwatchTimer)localObject2);
          }
          return localObject2;
        }
        localObject1 = mTimerPartial;
        localObject2 = localObject1;
        if (localObject1 == null)
        {
          localObject2 = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 0, mBsi.mPartialTimers, mBsi.mOnBatteryScreenOffTimeBase, mOnBatteryScreenOffBackgroundTimeBase);
          mTimerPartial = ((BatteryStatsImpl.DualTimer)localObject2);
        }
        return localObject2;
      }
      Object localObject1 = mTimerDraw;
      Object localObject2 = localObject1;
      if (localObject1 == null)
      {
        localObject2 = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 18, mBsi.mDrawTimers, mBsi.mOnBatteryTimeBase);
        mTimerDraw = ((BatteryStatsImpl.StopwatchTimer)localObject2);
      }
      return localObject2;
    }
    
    public int getWifiBatchedScanCount(int paramInt1, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt1 < 5))
      {
        if (mWifiBatchedScanTimer[paramInt1] == null) {
          return 0;
        }
        return mWifiBatchedScanTimer[paramInt1].getCountLocked(paramInt2);
      }
      return 0;
    }
    
    public long getWifiBatchedScanTime(int paramInt1, long paramLong, int paramInt2)
    {
      if ((paramInt1 >= 0) && (paramInt1 < 5))
      {
        if (mWifiBatchedScanTimer[paramInt1] == null) {
          return 0L;
        }
        return mWifiBatchedScanTimer[paramInt1].getTotalTimeLocked(paramLong, paramInt2);
      }
      return 0L;
    }
    
    public BatteryStats.ControllerActivityCounter getWifiControllerActivity()
    {
      return mWifiControllerActivity;
    }
    
    public long getWifiMulticastTime(long paramLong, int paramInt)
    {
      if (mWifiMulticastTimer == null) {
        return 0L;
      }
      return mWifiMulticastTimer.getTotalTimeLocked(paramLong, paramInt);
    }
    
    public long getWifiRadioApWakeupCount(int paramInt)
    {
      if (mWifiRadioApWakeupCount != null) {
        return mWifiRadioApWakeupCount.getCountLocked(paramInt);
      }
      return 0L;
    }
    
    public long getWifiRunningTime(long paramLong, int paramInt)
    {
      if (mWifiRunningTimer == null) {
        return 0L;
      }
      return mWifiRunningTimer.getTotalTimeLocked(paramLong, paramInt);
    }
    
    public long getWifiScanActualTime(long paramLong)
    {
      if (mWifiScanTimer == null) {
        return 0L;
      }
      paramLong = (500L + paramLong) / 1000L;
      return mWifiScanTimer.getTotalDurationMsLocked(paramLong) * 1000L;
    }
    
    public int getWifiScanBackgroundCount(int paramInt)
    {
      if ((mWifiScanTimer != null) && (mWifiScanTimer.getSubTimer() != null)) {
        return mWifiScanTimer.getSubTimer().getCountLocked(paramInt);
      }
      return 0;
    }
    
    public long getWifiScanBackgroundTime(long paramLong)
    {
      if ((mWifiScanTimer != null) && (mWifiScanTimer.getSubTimer() != null))
      {
        paramLong = (500L + paramLong) / 1000L;
        return mWifiScanTimer.getSubTimer().getTotalDurationMsLocked(paramLong) * 1000L;
      }
      return 0L;
    }
    
    public BatteryStatsImpl.Timer getWifiScanBackgroundTimer()
    {
      if (mWifiScanTimer == null) {
        return null;
      }
      return mWifiScanTimer.getSubTimer();
    }
    
    public int getWifiScanCount(int paramInt)
    {
      if (mWifiScanTimer == null) {
        return 0;
      }
      return mWifiScanTimer.getCountLocked(paramInt);
    }
    
    public long getWifiScanTime(long paramLong, int paramInt)
    {
      if (mWifiScanTimer == null) {
        return 0L;
      }
      return mWifiScanTimer.getTotalTimeLocked(paramLong, paramInt);
    }
    
    public BatteryStatsImpl.Timer getWifiScanTimer()
    {
      return mWifiScanTimer;
    }
    
    public boolean hasNetworkActivity()
    {
      boolean bool;
      if (mNetworkByteActivityCounters != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    public boolean hasUserActivity()
    {
      boolean bool;
      if (mUserActivityCounters != null) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void initNetworkActivityLocked()
    {
      mNetworkByteActivityCounters = new BatteryStatsImpl.LongSamplingCounter[10];
      mNetworkPacketActivityCounters = new BatteryStatsImpl.LongSamplingCounter[10];
      for (int i = 0; i < 10; i++)
      {
        mNetworkByteActivityCounters[i] = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
        mNetworkPacketActivityCounters[i] = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
      }
      mMobileRadioActiveTime = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
      mMobileRadioActiveCount = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
    }
    
    void initUserActivityLocked()
    {
      mUserActivityCounters = new BatteryStatsImpl.Counter[4];
      for (int i = 0; i < 4; i++) {
        mUserActivityCounters[i] = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase);
      }
    }
    
    public boolean isInBackground()
    {
      boolean bool;
      if (mProcessState >= 3) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    void makeProcessState(int paramInt, Parcel paramParcel)
    {
      if ((paramInt >= 0) && (paramInt < 7))
      {
        if (paramParcel == null) {
          mProcessStateTimer[paramInt] = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 12, null, mBsi.mOnBatteryTimeBase);
        } else {
          mProcessStateTimer[paramInt] = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 12, null, mBsi.mOnBatteryTimeBase, paramParcel);
        }
        return;
      }
    }
    
    void makeWifiBatchedScanBin(int paramInt, Parcel paramParcel)
    {
      if ((paramInt >= 0) && (paramInt < 5))
      {
        ArrayList localArrayList1 = (ArrayList)mBsi.mWifiBatchedScanTimers.get(paramInt);
        ArrayList localArrayList2 = localArrayList1;
        if (localArrayList1 == null)
        {
          localArrayList2 = new ArrayList();
          mBsi.mWifiBatchedScanTimers.put(paramInt, localArrayList2);
        }
        if (paramParcel == null) {
          mWifiBatchedScanTimer[paramInt] = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 11, localArrayList2, mBsi.mOnBatteryTimeBase);
        } else {
          mWifiBatchedScanTimer[paramInt] = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 11, localArrayList2, mBsi.mOnBatteryTimeBase, paramParcel);
        }
        return;
      }
    }
    
    public void noteActivityPausedLocked(long paramLong)
    {
      if (mForegroundActivityTimer != null) {
        mForegroundActivityTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteActivityResumedLocked(long paramLong)
    {
      createForegroundActivityTimerLocked().startRunningLocked(paramLong);
    }
    
    public void noteAudioTurnedOffLocked(long paramLong)
    {
      if (mAudioTurnedOnTimer != null) {
        mAudioTurnedOnTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteAudioTurnedOnLocked(long paramLong)
    {
      createAudioTurnedOnTimerLocked().startRunningLocked(paramLong);
    }
    
    public void noteBluetoothScanResultsLocked(int paramInt)
    {
      createBluetoothScanResultCounterLocked().addAtomic(paramInt);
      createBluetoothScanResultBgCounterLocked().addAtomic(paramInt);
    }
    
    public void noteBluetoothScanStartedLocked(long paramLong, boolean paramBoolean)
    {
      createBluetoothScanTimerLocked().startRunningLocked(paramLong);
      if (paramBoolean) {
        createBluetoothUnoptimizedScanTimerLocked().startRunningLocked(paramLong);
      }
    }
    
    public void noteBluetoothScanStoppedLocked(long paramLong, boolean paramBoolean)
    {
      if (mBluetoothScanTimer != null) {
        mBluetoothScanTimer.stopRunningLocked(paramLong);
      }
      if ((paramBoolean) && (mBluetoothUnoptimizedScanTimer != null)) {
        mBluetoothUnoptimizedScanTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteCameraTurnedOffLocked(long paramLong)
    {
      if (mCameraTurnedOnTimer != null) {
        mCameraTurnedOnTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteCameraTurnedOnLocked(long paramLong)
    {
      createCameraTurnedOnTimerLocked().startRunningLocked(paramLong);
    }
    
    public void noteFlashlightTurnedOffLocked(long paramLong)
    {
      if (mFlashlightTurnedOnTimer != null) {
        mFlashlightTurnedOnTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteFlashlightTurnedOnLocked(long paramLong)
    {
      createFlashlightTurnedOnTimerLocked().startRunningLocked(paramLong);
    }
    
    public void noteForegroundServicePausedLocked(long paramLong)
    {
      if (mForegroundServiceTimer != null) {
        mForegroundServiceTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteForegroundServiceResumedLocked(long paramLong)
    {
      createForegroundServiceTimerLocked().startRunningLocked(paramLong);
    }
    
    public void noteFullWifiLockAcquiredLocked(long paramLong)
    {
      if (!mFullWifiLockOut)
      {
        mFullWifiLockOut = true;
        if (mFullWifiLockTimer == null) {
          mFullWifiLockTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 5, mBsi.mFullWifiLockTimers, mBsi.mOnBatteryTimeBase);
        }
        mFullWifiLockTimer.startRunningLocked(paramLong);
      }
    }
    
    public void noteFullWifiLockReleasedLocked(long paramLong)
    {
      if (mFullWifiLockOut)
      {
        mFullWifiLockOut = false;
        mFullWifiLockTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteJobsDeferredLocked(int paramInt, long paramLong)
    {
      mJobsDeferredEventCount.addAtomic(1);
      mJobsDeferredCount.addAtomic(paramInt);
      if (paramLong != 0L)
      {
        mJobsFreshnessTimeMs.addCountLocked(paramLong);
        for (paramInt = 0; paramInt < BatteryStats.JOB_FRESHNESS_BUCKETS.length; paramInt++) {
          if (paramLong < BatteryStats.JOB_FRESHNESS_BUCKETS[paramInt])
          {
            if (mJobsFreshnessBuckets[paramInt] == null) {
              mJobsFreshnessBuckets[paramInt] = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase);
            }
            mJobsFreshnessBuckets[paramInt].addAtomic(1);
            break;
          }
        }
      }
    }
    
    void noteMobileRadioActiveTimeLocked(long paramLong)
    {
      if (mNetworkByteActivityCounters == null) {
        initNetworkActivityLocked();
      }
      mMobileRadioActiveTime.addCountLocked(paramLong);
      mMobileRadioActiveCount.addCountLocked(1L);
    }
    
    public void noteMobileRadioApWakeupLocked()
    {
      if (mMobileRadioApWakeupCount == null) {
        mMobileRadioApWakeupCount = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
      }
      mMobileRadioApWakeupCount.addCountLocked(1L);
    }
    
    void noteNetworkActivityLocked(int paramInt, long paramLong1, long paramLong2)
    {
      if (mNetworkByteActivityCounters == null) {
        initNetworkActivityLocked();
      }
      if ((paramInt >= 0) && (paramInt < 10))
      {
        mNetworkByteActivityCounters[paramInt].addCountLocked(paramLong1);
        mNetworkPacketActivityCounters[paramInt].addCountLocked(paramLong2);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown network activity type ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" was specified.");
        Slog.w("BatteryStatsImpl", localStringBuilder.toString(), new Throwable());
      }
    }
    
    public void noteResetAudioLocked(long paramLong)
    {
      if (mAudioTurnedOnTimer != null) {
        mAudioTurnedOnTimer.stopAllRunningLocked(paramLong);
      }
    }
    
    public void noteResetBluetoothScanLocked(long paramLong)
    {
      if (mBluetoothScanTimer != null) {
        mBluetoothScanTimer.stopAllRunningLocked(paramLong);
      }
      if (mBluetoothUnoptimizedScanTimer != null) {
        mBluetoothUnoptimizedScanTimer.stopAllRunningLocked(paramLong);
      }
    }
    
    public void noteResetCameraLocked(long paramLong)
    {
      if (mCameraTurnedOnTimer != null) {
        mCameraTurnedOnTimer.stopAllRunningLocked(paramLong);
      }
    }
    
    public void noteResetFlashlightLocked(long paramLong)
    {
      if (mFlashlightTurnedOnTimer != null) {
        mFlashlightTurnedOnTimer.stopAllRunningLocked(paramLong);
      }
    }
    
    public void noteResetVideoLocked(long paramLong)
    {
      if (mVideoTurnedOnTimer != null) {
        mVideoTurnedOnTimer.stopAllRunningLocked(paramLong);
      }
    }
    
    public void noteStartGps(long paramLong)
    {
      noteStartSensor(55536, paramLong);
    }
    
    public void noteStartJobLocked(String paramString, long paramLong)
    {
      paramString = (BatteryStatsImpl.DualTimer)mJobStats.startObject(paramString);
      if (paramString != null) {
        paramString.startRunningLocked(paramLong);
      }
    }
    
    public void noteStartSensor(int paramInt, long paramLong)
    {
      getSensorTimerLocked(paramInt, true).startRunningLocked(paramLong);
    }
    
    public void noteStartSyncLocked(String paramString, long paramLong)
    {
      paramString = (BatteryStatsImpl.DualTimer)mSyncStats.startObject(paramString);
      if (paramString != null) {
        paramString.startRunningLocked(paramLong);
      }
    }
    
    public void noteStartWakeLocked(int paramInt1, String paramString, int paramInt2, long paramLong)
    {
      paramString = (Wakelock)mWakelockStats.startObject(paramString);
      if (paramString != null) {
        getWakelockTimerLocked(paramString, paramInt2).startRunningLocked(paramLong);
      }
      if (paramInt2 == 0)
      {
        createAggregatedPartialWakelockTimerLocked().startRunningLocked(paramLong);
        if (paramInt1 >= 0)
        {
          paramString = getPidStatsLocked(paramInt1);
          paramInt1 = mWakeNesting;
          mWakeNesting = (paramInt1 + 1);
          if (paramInt1 == 0) {
            mWakeStartMs = paramLong;
          }
        }
      }
    }
    
    public void noteStopGps(long paramLong)
    {
      noteStopSensor(55536, paramLong);
    }
    
    public void noteStopJobLocked(String paramString, long paramLong, int paramInt)
    {
      Object localObject = (BatteryStatsImpl.DualTimer)mJobStats.stopObject(paramString);
      if (localObject != null) {
        ((BatteryStatsImpl.DualTimer)localObject).stopRunningLocked(paramLong);
      }
      if (mBsi.mOnBatteryTimeBase.isRunning())
      {
        SparseIntArray localSparseIntArray = (SparseIntArray)mJobCompletions.get(paramString);
        localObject = localSparseIntArray;
        if (localSparseIntArray == null)
        {
          localObject = new SparseIntArray();
          mJobCompletions.put(paramString, localObject);
        }
        ((SparseIntArray)localObject).put(paramInt, ((SparseIntArray)localObject).get(paramInt, 0) + 1);
      }
    }
    
    public void noteStopSensor(int paramInt, long paramLong)
    {
      BatteryStatsImpl.DualTimer localDualTimer = getSensorTimerLocked(paramInt, false);
      if (localDualTimer != null) {
        localDualTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteStopSyncLocked(String paramString, long paramLong)
    {
      paramString = (BatteryStatsImpl.DualTimer)mSyncStats.stopObject(paramString);
      if (paramString != null) {
        paramString.stopRunningLocked(paramLong);
      }
    }
    
    public void noteStopWakeLocked(int paramInt1, String paramString, int paramInt2, long paramLong)
    {
      paramString = (Wakelock)mWakelockStats.stopObject(paramString);
      if (paramString != null) {
        getWakelockTimerLocked(paramString, paramInt2).stopRunningLocked(paramLong);
      }
      if (paramInt2 == 0)
      {
        if (mAggregatedPartialWakelockTimer != null) {
          mAggregatedPartialWakelockTimer.stopRunningLocked(paramLong);
        }
        if (paramInt1 >= 0)
        {
          paramString = (BatteryStats.Uid.Pid)mPids.get(paramInt1);
          if ((paramString != null) && (mWakeNesting > 0))
          {
            paramInt1 = mWakeNesting;
            mWakeNesting = (paramInt1 - 1);
            if (paramInt1 == 1)
            {
              mWakeSumMs += paramLong - mWakeStartMs;
              mWakeStartMs = 0L;
            }
          }
        }
      }
    }
    
    public void noteUserActivityLocked(int paramInt)
    {
      if (mUserActivityCounters == null) {
        initUserActivityLocked();
      }
      if ((paramInt >= 0) && (paramInt < 4))
      {
        mUserActivityCounters[paramInt].stepAtomic();
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("Unknown user activity type ");
        localStringBuilder.append(paramInt);
        localStringBuilder.append(" was specified.");
        Slog.w("BatteryStatsImpl", localStringBuilder.toString(), new Throwable());
      }
    }
    
    public void noteVibratorOffLocked()
    {
      if (mVibratorOnTimer != null) {
        mVibratorOnTimer.abortLastDuration(mBsi);
      }
    }
    
    public void noteVibratorOnLocked(long paramLong)
    {
      createVibratorOnTimerLocked().addDuration(mBsi, paramLong);
    }
    
    public void noteVideoTurnedOffLocked(long paramLong)
    {
      if (mVideoTurnedOnTimer != null) {
        mVideoTurnedOnTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteVideoTurnedOnLocked(long paramLong)
    {
      createVideoTurnedOnTimerLocked().startRunningLocked(paramLong);
    }
    
    public void noteWifiBatchedScanStartedLocked(int paramInt, long paramLong)
    {
      for (int i = 0; (paramInt > 8) && (i < 4); i++) {
        paramInt >>= 3;
      }
      if (mWifiBatchedScanBinStarted == i) {
        return;
      }
      if (mWifiBatchedScanBinStarted != -1) {
        mWifiBatchedScanTimer[mWifiBatchedScanBinStarted].stopRunningLocked(paramLong);
      }
      mWifiBatchedScanBinStarted = i;
      if (mWifiBatchedScanTimer[i] == null) {
        makeWifiBatchedScanBin(i, null);
      }
      mWifiBatchedScanTimer[i].startRunningLocked(paramLong);
    }
    
    public void noteWifiBatchedScanStoppedLocked(long paramLong)
    {
      if (mWifiBatchedScanBinStarted != -1)
      {
        mWifiBatchedScanTimer[mWifiBatchedScanBinStarted].stopRunningLocked(paramLong);
        mWifiBatchedScanBinStarted = -1;
      }
    }
    
    public void noteWifiMulticastDisabledLocked(long paramLong)
    {
      if (mWifiMulticastEnabled)
      {
        mWifiMulticastEnabled = false;
        mWifiMulticastTimer.stopRunningLocked(paramLong);
        StatsLog.write_non_chained(53, getUid(), null, 0);
      }
    }
    
    public void noteWifiMulticastEnabledLocked(long paramLong)
    {
      if (!mWifiMulticastEnabled)
      {
        mWifiMulticastEnabled = true;
        if (mWifiMulticastTimer == null) {
          mWifiMulticastTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 7, mBsi.mWifiMulticastTimers, mBsi.mOnBatteryTimeBase);
        }
        mWifiMulticastTimer.startRunningLocked(paramLong);
        StatsLog.write_non_chained(53, getUid(), null, 1);
      }
    }
    
    public void noteWifiRadioApWakeupLocked()
    {
      if (mWifiRadioApWakeupCount == null) {
        mWifiRadioApWakeupCount = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase);
      }
      mWifiRadioApWakeupCount.addCountLocked(1L);
    }
    
    public void noteWifiRunningLocked(long paramLong)
    {
      if (!mWifiRunning)
      {
        mWifiRunning = true;
        if (mWifiRunningTimer == null) {
          mWifiRunningTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 4, mBsi.mWifiRunningTimers, mBsi.mOnBatteryTimeBase);
        }
        mWifiRunningTimer.startRunningLocked(paramLong);
      }
    }
    
    public void noteWifiScanStartedLocked(long paramLong)
    {
      if (!mWifiScanStarted)
      {
        mWifiScanStarted = true;
        if (mWifiScanTimer == null) {
          mWifiScanTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 6, mBsi.mWifiScanTimers, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase);
        }
        mWifiScanTimer.startRunningLocked(paramLong);
      }
    }
    
    public void noteWifiScanStoppedLocked(long paramLong)
    {
      if (mWifiScanStarted)
      {
        mWifiScanStarted = false;
        mWifiScanTimer.stopRunningLocked(paramLong);
      }
    }
    
    public void noteWifiStoppedLocked(long paramLong)
    {
      if (mWifiRunning)
      {
        mWifiRunning = false;
        mWifiRunningTimer.stopRunningLocked(paramLong);
      }
    }
    
    void readFromParcelLocked(BatteryStatsImpl.TimeBase paramTimeBase1, BatteryStatsImpl.TimeBase paramTimeBase2, Parcel paramParcel)
    {
      mOnBatteryBackgroundTimeBase.readFromParcel(paramParcel);
      mOnBatteryScreenOffBackgroundTimeBase.readFromParcel(paramParcel);
      int i = paramParcel.readInt();
      mWakelockStats.clear();
      for (int j = 0; j < i; j++)
      {
        String str = paramParcel.readString();
        Wakelock localWakelock = new Wakelock(mBsi, this);
        localWakelock.readFromParcelLocked(paramTimeBase1, paramTimeBase2, mOnBatteryScreenOffBackgroundTimeBase, paramParcel);
        mWakelockStats.add(str, localWakelock);
      }
      int k = paramParcel.readInt();
      mSyncStats.clear();
      int m = 0;
      j = i;
      for (i = m; i < k; i++)
      {
        paramTimeBase1 = paramParcel.readString();
        if (paramParcel.readInt() != 0) {
          mSyncStats.add(paramTimeBase1, new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 13, null, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase, paramParcel));
        }
      }
      j = paramParcel.readInt();
      mJobStats.clear();
      for (i = 0; i < j; i++)
      {
        paramTimeBase1 = paramParcel.readString();
        if (paramParcel.readInt() != 0) {
          mJobStats.add(paramTimeBase1, new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 14, null, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase, paramParcel));
        }
      }
      readJobCompletionsFromParcelLocked(paramParcel);
      mJobsDeferredEventCount = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase, paramParcel);
      mJobsDeferredCount = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase, paramParcel);
      mJobsFreshnessTimeMs = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
      for (j = 0; j < BatteryStats.JOB_FRESHNESS_BUCKETS.length; j++) {
        mJobsFreshnessBuckets[j] = BatteryStatsImpl.Counter.readCounterFromParcel(mBsi.mOnBatteryTimeBase, paramParcel);
      }
      i = paramParcel.readInt();
      mSensorStats.clear();
      for (j = 0; j < i; j++)
      {
        m = paramParcel.readInt();
        paramTimeBase1 = new Sensor(mBsi, this, m);
        paramTimeBase1.readFromParcelLocked(mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase, paramParcel);
        mSensorStats.put(m, paramTimeBase1);
      }
      i = paramParcel.readInt();
      mProcessStats.clear();
      for (j = 0; j < i; j++)
      {
        paramTimeBase2 = paramParcel.readString();
        paramTimeBase1 = new Proc(mBsi, paramTimeBase2);
        paramTimeBase1.readFromParcelLocked(paramParcel);
        mProcessStats.put(paramTimeBase2, paramTimeBase1);
      }
      i = paramParcel.readInt();
      mPackageStats.clear();
      for (j = 0; j < i; j++)
      {
        paramTimeBase2 = paramParcel.readString();
        paramTimeBase1 = new Pkg(mBsi);
        paramTimeBase1.readFromParcelLocked(paramParcel);
        mPackageStats.put(paramTimeBase2, paramTimeBase1);
      }
      mWifiRunning = false;
      if (paramParcel.readInt() != 0) {
        mWifiRunningTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 4, mBsi.mWifiRunningTimers, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mWifiRunningTimer = null;
      }
      mFullWifiLockOut = false;
      if (paramParcel.readInt() != 0) {
        mFullWifiLockTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 5, mBsi.mFullWifiLockTimers, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mFullWifiLockTimer = null;
      }
      mWifiScanStarted = false;
      if (paramParcel.readInt() != 0) {
        mWifiScanTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 6, mBsi.mWifiScanTimers, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase, paramParcel);
      } else {
        mWifiScanTimer = null;
      }
      paramTimeBase1 = null;
      mWifiBatchedScanBinStarted = -1;
      for (j = 0; j < 5; j++) {
        if (paramParcel.readInt() != 0) {
          makeWifiBatchedScanBin(j, paramParcel);
        } else {
          mWifiBatchedScanTimer[j] = paramTimeBase1;
        }
      }
      mWifiMulticastEnabled = false;
      if (paramParcel.readInt() != 0) {
        mWifiMulticastTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 7, mBsi.mWifiMulticastTimers, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mWifiMulticastTimer = paramTimeBase1;
      }
      m = 0;
      if (paramParcel.readInt() != 0) {
        mAudioTurnedOnTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 15, mBsi.mAudioTurnedOnTimers, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mAudioTurnedOnTimer = paramTimeBase1;
      }
      if (paramParcel.readInt() != 0) {
        mVideoTurnedOnTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 8, mBsi.mVideoTurnedOnTimers, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mVideoTurnedOnTimer = paramTimeBase1;
      }
      if (paramParcel.readInt() != 0) {
        mFlashlightTurnedOnTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 16, mBsi.mFlashlightTurnedOnTimers, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mFlashlightTurnedOnTimer = paramTimeBase1;
      }
      if (paramParcel.readInt() != 0) {
        mCameraTurnedOnTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 17, mBsi.mCameraTurnedOnTimers, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mCameraTurnedOnTimer = paramTimeBase1;
      }
      if (paramParcel.readInt() != 0) {
        mForegroundActivityTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 10, null, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mForegroundActivityTimer = paramTimeBase1;
      }
      if (paramParcel.readInt() != 0) {
        mForegroundServiceTimer = new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, this, 22, null, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mForegroundServiceTimer = paramTimeBase1;
      }
      if (paramParcel.readInt() != 0) {
        mAggregatedPartialWakelockTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 20, null, mBsi.mOnBatteryScreenOffTimeBase, mOnBatteryScreenOffBackgroundTimeBase, paramParcel);
      } else {
        mAggregatedPartialWakelockTimer = null;
      }
      if (paramParcel.readInt() != 0) {
        mBluetoothScanTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 19, mBsi.mBluetoothScanOnTimers, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase, paramParcel);
      } else {
        mBluetoothScanTimer = null;
      }
      if (paramParcel.readInt() != 0) {
        mBluetoothUnoptimizedScanTimer = new BatteryStatsImpl.DualTimer(mBsi.mClocks, this, 21, null, mBsi.mOnBatteryTimeBase, mOnBatteryBackgroundTimeBase, paramParcel);
      } else {
        mBluetoothUnoptimizedScanTimer = null;
      }
      if (paramParcel.readInt() != 0) {
        mBluetoothScanResultCounter = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mBluetoothScanResultCounter = null;
      }
      if (paramParcel.readInt() != 0) {
        mBluetoothScanResultBgCounter = new BatteryStatsImpl.Counter(mOnBatteryBackgroundTimeBase, paramParcel);
      } else {
        mBluetoothScanResultBgCounter = null;
      }
      mProcessState = 19;
      for (j = 0; j < 7; j++) {
        if (paramParcel.readInt() != 0) {
          makeProcessState(j, paramParcel);
        } else {
          mProcessStateTimer[j] = null;
        }
      }
      if (paramParcel.readInt() != 0) {
        mVibratorOnTimer = new BatteryStatsImpl.BatchTimer(mBsi.mClocks, this, 9, mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mVibratorOnTimer = null;
      }
      if (paramParcel.readInt() != 0)
      {
        mUserActivityCounters = new BatteryStatsImpl.Counter[4];
        for (j = 0; j < 4; j++) {
          mUserActivityCounters[j] = new BatteryStatsImpl.Counter(mBsi.mOnBatteryTimeBase, paramParcel);
        }
      }
      mUserActivityCounters = null;
      if (paramParcel.readInt() != 0)
      {
        mNetworkByteActivityCounters = new BatteryStatsImpl.LongSamplingCounter[10];
        mNetworkPacketActivityCounters = new BatteryStatsImpl.LongSamplingCounter[10];
        for (j = 0; j < 10; j++)
        {
          mNetworkByteActivityCounters[j] = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
          mNetworkPacketActivityCounters[j] = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
        }
        mMobileRadioActiveTime = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
        mMobileRadioActiveCount = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
      }
      else
      {
        mNetworkByteActivityCounters = null;
        mNetworkPacketActivityCounters = null;
      }
      if (paramParcel.readInt() != 0) {
        mWifiControllerActivity = new BatteryStatsImpl.ControllerActivityCounterImpl(mBsi.mOnBatteryTimeBase, 1, paramParcel);
      } else {
        mWifiControllerActivity = null;
      }
      if (paramParcel.readInt() != 0) {
        mBluetoothControllerActivity = new BatteryStatsImpl.ControllerActivityCounterImpl(mBsi.mOnBatteryTimeBase, 1, paramParcel);
      } else {
        mBluetoothControllerActivity = null;
      }
      if (paramParcel.readInt() != 0) {
        mModemControllerActivity = new BatteryStatsImpl.ControllerActivityCounterImpl(mBsi.mOnBatteryTimeBase, 5, paramParcel);
      } else {
        mModemControllerActivity = null;
      }
      mUserCpuTime = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
      mSystemCpuTime = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
      if (paramParcel.readInt() != 0)
      {
        k = paramParcel.readInt();
        if ((mBsi.mPowerProfile != null) && (mBsi.mPowerProfile.getNumCpuClusters() != k)) {
          throw new ParcelFormatException("Incompatible number of cpu clusters");
        }
        mCpuClusterSpeedTimesUs = new BatteryStatsImpl.LongSamplingCounter[k][];
        for (j = 0; j < k; j++) {
          if (paramParcel.readInt() != 0)
          {
            int n = paramParcel.readInt();
            if ((mBsi.mPowerProfile != null) && (mBsi.mPowerProfile.getNumSpeedStepsInCpuCluster(j) != n)) {
              throw new ParcelFormatException("Incompatible number of cpu speeds");
            }
            paramTimeBase1 = new BatteryStatsImpl.LongSamplingCounter[n];
            mCpuClusterSpeedTimesUs[j] = paramTimeBase1;
            for (i = 0; i < n; i++) {
              if (paramParcel.readInt() != 0) {
                paramTimeBase1[i] = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
              }
            }
          }
          else
          {
            mCpuClusterSpeedTimesUs[j] = null;
          }
        }
      }
      else
      {
        mCpuClusterSpeedTimesUs = null;
      }
      mCpuFreqTimeMs = BatteryStatsImpl.LongSamplingCounterArray.readFromParcel(paramParcel, mBsi.mOnBatteryTimeBase);
      mScreenOffCpuFreqTimeMs = BatteryStatsImpl.LongSamplingCounterArray.readFromParcel(paramParcel, mBsi.mOnBatteryScreenOffTimeBase);
      mCpuActiveTimeMs = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
      mCpuClusterTimesMs = new BatteryStatsImpl.LongSamplingCounterArray(mBsi.mOnBatteryTimeBase, paramParcel, null);
      i = paramParcel.readInt();
      if (i == 7)
      {
        mProcStateTimeMs = new BatteryStatsImpl.LongSamplingCounterArray[i];
        for (j = 0; j < i; j++) {
          mProcStateTimeMs[j] = BatteryStatsImpl.LongSamplingCounterArray.readFromParcel(paramParcel, mBsi.mOnBatteryTimeBase);
        }
      }
      mProcStateTimeMs = null;
      i = paramParcel.readInt();
      if (i == 7)
      {
        mProcStateScreenOffTimeMs = new BatteryStatsImpl.LongSamplingCounterArray[i];
        for (j = m; j < i; j++) {
          mProcStateScreenOffTimeMs[j] = BatteryStatsImpl.LongSamplingCounterArray.readFromParcel(paramParcel, mBsi.mOnBatteryScreenOffTimeBase);
        }
      }
      mProcStateScreenOffTimeMs = null;
      if (paramParcel.readInt() != 0) {
        mMobileRadioApWakeupCount = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mMobileRadioApWakeupCount = null;
      }
      if (paramParcel.readInt() != 0) {
        mWifiRadioApWakeupCount = new BatteryStatsImpl.LongSamplingCounter(mBsi.mOnBatteryTimeBase, paramParcel);
      } else {
        mWifiRadioApWakeupCount = null;
      }
    }
    
    void readJobCompletionsFromParcelLocked(Parcel paramParcel)
    {
      int i = paramParcel.readInt();
      mJobCompletions.clear();
      for (int j = 0; j < i; j++)
      {
        String str = paramParcel.readString();
        int k = paramParcel.readInt();
        if (k > 0)
        {
          SparseIntArray localSparseIntArray = new SparseIntArray();
          for (int m = 0; m < k; m++) {
            localSparseIntArray.put(paramParcel.readInt(), paramParcel.readInt());
          }
          mJobCompletions.put(str, localSparseIntArray);
        }
      }
    }
    
    public void readJobSummaryFromParcelLocked(String paramString, Parcel paramParcel)
    {
      BatteryStatsImpl.DualTimer localDualTimer = (BatteryStatsImpl.DualTimer)mJobStats.instantiateObject();
      localDualTimer.readSummaryFromParcelLocked(paramParcel);
      mJobStats.add(paramString, localDualTimer);
    }
    
    public void readSyncSummaryFromParcelLocked(String paramString, Parcel paramParcel)
    {
      BatteryStatsImpl.DualTimer localDualTimer = (BatteryStatsImpl.DualTimer)mSyncStats.instantiateObject();
      localDualTimer.readSummaryFromParcelLocked(paramParcel);
      mSyncStats.add(paramString, localDualTimer);
    }
    
    public void readWakeSummaryFromParcelLocked(String paramString, Parcel paramParcel)
    {
      Wakelock localWakelock = new Wakelock(mBsi, this);
      mWakelockStats.add(paramString, localWakelock);
      if (paramParcel.readInt() != 0) {
        getWakelockTimerLocked(localWakelock, 1).readSummaryFromParcelLocked(paramParcel);
      }
      if (paramParcel.readInt() != 0) {
        getWakelockTimerLocked(localWakelock, 0).readSummaryFromParcelLocked(paramParcel);
      }
      if (paramParcel.readInt() != 0) {
        getWakelockTimerLocked(localWakelock, 2).readSummaryFromParcelLocked(paramParcel);
      }
      if (paramParcel.readInt() != 0) {
        getWakelockTimerLocked(localWakelock, 18).readSummaryFromParcelLocked(paramParcel);
      }
    }
    
    public void removeIsolatedUid(int paramInt)
    {
      if (mChildUids == null) {
        paramInt = -1;
      } else {
        paramInt = mChildUids.indexOf(paramInt);
      }
      if (paramInt < 0) {
        return;
      }
      mChildUids.remove(paramInt);
    }
    
    public void reportExcessiveCpuLocked(String paramString, long paramLong1, long paramLong2)
    {
      paramString = getProcessStatsLocked(paramString);
      if (paramString != null) {
        paramString.addExcessiveCpu(paramLong1, paramLong2);
      }
    }
    
    @VisibleForTesting(visibility=VisibleForTesting.Visibility.PACKAGE)
    public boolean reset(long paramLong1, long paramLong2)
    {
      int i = 0;
      mOnBatteryBackgroundTimeBase.init(paramLong1, paramLong2);
      mOnBatteryScreenOffBackgroundTimeBase.init(paramLong1, paramLong2);
      if (mWifiRunningTimer != null) {
        i = false | mWifiRunningTimer.reset(false) ^ true | mWifiRunning;
      }
      int j = i;
      if (mFullWifiLockTimer != null) {
        j = i | mFullWifiLockTimer.reset(false) ^ true | mFullWifiLockOut;
      }
      i = j;
      if (mWifiScanTimer != null) {
        i = j | mWifiScanTimer.reset(false) ^ true | mWifiScanStarted;
      }
      j = i;
      int m;
      if (mWifiBatchedScanTimer != null)
      {
        m = 0;
        while (m < 5)
        {
          j = i;
          if (mWifiBatchedScanTimer[m] != null) {
            j = i | mWifiBatchedScanTimer[m].reset(false) ^ true;
          }
          m++;
          i = j;
        }
        if (mWifiBatchedScanBinStarted != -1) {
          j = 1;
        } else {
          j = 0;
        }
        j |= i;
      }
      i = j;
      if (mWifiMulticastTimer != null) {
        i = j | mWifiMulticastTimer.reset(false) ^ true | mWifiMulticastEnabled;
      }
      i = i | BatteryStatsImpl.resetTimerIfNotNull(mAudioTurnedOnTimer, false) ^ true | BatteryStatsImpl.resetTimerIfNotNull(mVideoTurnedOnTimer, false) ^ true | BatteryStatsImpl.resetTimerIfNotNull(mFlashlightTurnedOnTimer, false) ^ true | BatteryStatsImpl.resetTimerIfNotNull(mCameraTurnedOnTimer, false) ^ true | BatteryStatsImpl.resetTimerIfNotNull(mForegroundActivityTimer, false) ^ true | BatteryStatsImpl.resetTimerIfNotNull(mForegroundServiceTimer, false) ^ true | BatteryStatsImpl.resetTimerIfNotNull(mAggregatedPartialWakelockTimer, false) ^ true | BatteryStatsImpl.resetTimerIfNotNull(mBluetoothScanTimer, false) ^ true | BatteryStatsImpl.resetTimerIfNotNull(mBluetoothUnoptimizedScanTimer, false) ^ true;
      if (mBluetoothScanResultCounter != null) {
        mBluetoothScanResultCounter.reset(false);
      }
      if (mBluetoothScanResultBgCounter != null) {
        mBluetoothScanResultBgCounter.reset(false);
      }
      j = i;
      if (mProcessStateTimer != null)
      {
        j = 0;
        while (j < 7)
        {
          m = i;
          if (mProcessStateTimer[j] != null) {
            m = i | mProcessStateTimer[j].reset(false) ^ true;
          }
          j++;
          i = m;
        }
        if (mProcessState != 19) {
          j = 1;
        } else {
          j = 0;
        }
        j |= i;
      }
      i = j;
      if (mVibratorOnTimer != null) {
        if (mVibratorOnTimer.reset(false))
        {
          mVibratorOnTimer.detach();
          mVibratorOnTimer = null;
          i = j;
        }
        else
        {
          i = 1;
        }
      }
      if (mUserActivityCounters != null) {
        for (j = 0; j < 4; j++) {
          mUserActivityCounters[j].reset(false);
        }
      }
      if (mNetworkByteActivityCounters != null)
      {
        for (j = 0; j < 10; j++)
        {
          mNetworkByteActivityCounters[j].reset(false);
          mNetworkPacketActivityCounters[j].reset(false);
        }
        mMobileRadioActiveTime.reset(false);
        mMobileRadioActiveCount.reset(false);
      }
      if (mWifiControllerActivity != null) {
        mWifiControllerActivity.reset(false);
      }
      if (mBluetoothControllerActivity != null) {
        mBluetoothControllerActivity.reset(false);
      }
      if (mModemControllerActivity != null) {
        mModemControllerActivity.reset(false);
      }
      mUserCpuTime.reset(false);
      mSystemCpuTime.reset(false);
      if (mCpuClusterSpeedTimesUs != null) {
        for (localObject1 : mCpuClusterSpeedTimesUs) {
          if (localObject1 != null)
          {
            int i6 = localObject1.length;
            for (m = 0; m < i6; m++)
            {
              localObject2 = localObject1[m];
              if (localObject2 != null) {
                ((BatteryStatsImpl.LongSamplingCounter)localObject2).reset(false);
              }
            }
          }
        }
      }
      if (mCpuFreqTimeMs != null) {
        mCpuFreqTimeMs.reset(false);
      }
      if (mScreenOffCpuFreqTimeMs != null) {
        mScreenOffCpuFreqTimeMs.reset(false);
      }
      mCpuActiveTimeMs.reset(false);
      mCpuClusterTimesMs.reset(false);
      if (mProcStateTimeMs != null) {
        for (localObject1 : mProcStateTimeMs) {
          if (localObject1 != null) {
            ((BatteryStatsImpl.LongSamplingCounterArray)localObject1).reset(false);
          }
        }
      }
      if (mProcStateScreenOffTimeMs != null) {
        for (localObject1 : mProcStateScreenOffTimeMs) {
          if (localObject1 != null) {
            ((BatteryStatsImpl.LongSamplingCounterArray)localObject1).reset(false);
          }
        }
      }
      BatteryStatsImpl.resetLongCounterIfNotNull(mMobileRadioApWakeupCount, false);
      BatteryStatsImpl.resetLongCounterIfNotNull(mWifiRadioApWakeupCount, false);
      Object localObject2 = mWakelockStats.getMap();
      for (int k = ((ArrayMap)localObject2).size() - 1; k >= 0; k--) {
        if (((Wakelock)((ArrayMap)localObject2).valueAt(k)).reset()) {
          ((ArrayMap)localObject2).removeAt(k);
        } else {
          i = 1;
        }
      }
      mWakelockStats.cleanup();
      Object localObject1 = mSyncStats.getMap();
      for (k = ((ArrayMap)localObject1).size() - 1; k >= 0; k--)
      {
        localObject2 = (BatteryStatsImpl.DualTimer)((ArrayMap)localObject1).valueAt(k);
        if (((BatteryStatsImpl.DualTimer)localObject2).reset(false))
        {
          ((ArrayMap)localObject1).removeAt(k);
          ((BatteryStatsImpl.DualTimer)localObject2).detach();
        }
        else
        {
          i = 1;
        }
      }
      mSyncStats.cleanup();
      localObject1 = mJobStats.getMap();
      for (k = ((ArrayMap)localObject1).size() - 1; k >= 0; k--)
      {
        localObject2 = (BatteryStatsImpl.DualTimer)((ArrayMap)localObject1).valueAt(k);
        if (((BatteryStatsImpl.DualTimer)localObject2).reset(false))
        {
          ((ArrayMap)localObject1).removeAt(k);
          ((BatteryStatsImpl.DualTimer)localObject2).detach();
        }
        else
        {
          i = 1;
        }
      }
      mJobStats.cleanup();
      mJobCompletions.clear();
      mJobsDeferredEventCount.reset(false);
      mJobsDeferredCount.reset(false);
      mJobsFreshnessTimeMs.reset(false);
      for (k = 0; k < BatteryStats.JOB_FRESHNESS_BUCKETS.length; k++) {
        if (mJobsFreshnessBuckets[k] != null) {
          mJobsFreshnessBuckets[k].reset(false);
        }
      }
      for (k = mSensorStats.size() - 1; k >= 0; k--) {
        if (((Sensor)mSensorStats.valueAt(k)).reset()) {
          mSensorStats.removeAt(k);
        } else {
          i = 1;
        }
      }
      for (k = mProcessStats.size() - 1; k >= 0; k--) {
        ((Proc)mProcessStats.valueAt(k)).detach();
      }
      mProcessStats.clear();
      k = i;
      int i2;
      if (mPids.size() > 0) {
        for (i2 = mPids.size() - 1;; i2--)
        {
          k = i;
          if (i2 < 0) {
            break;
          }
          if (mPids.valueAt(i2)).mWakeNesting > 0) {
            i = 1;
          } else {
            mPids.removeAt(i2);
          }
        }
      }
      if (mPackageStats.size() > 0)
      {
        localObject2 = mPackageStats.entrySet().iterator();
        while (((Iterator)localObject2).hasNext())
        {
          localObject1 = (Pkg)((Map.Entry)((Iterator)localObject2).next()).getValue();
          ((Pkg)localObject1).detach();
          if (mServiceStats.size() > 0)
          {
            localObject1 = mServiceStats.entrySet().iterator();
            while (((Iterator)localObject1).hasNext()) {
              ((BatteryStatsImpl.Uid.Pkg.Serv)((Map.Entry)((Iterator)localObject1).next()).getValue()).detach();
            }
          }
        }
        mPackageStats.clear();
      }
      mLastStepSystemTime = 0L;
      mLastStepUserTime = 0L;
      mCurStepSystemTime = 0L;
      mCurStepUserTime = 0L;
      if (k == 0)
      {
        if (mWifiRunningTimer != null) {
          mWifiRunningTimer.detach();
        }
        if (mFullWifiLockTimer != null) {
          mFullWifiLockTimer.detach();
        }
        if (mWifiScanTimer != null) {
          mWifiScanTimer.detach();
        }
        for (i = 0; i < 5; i++) {
          if (mWifiBatchedScanTimer[i] != null) {
            mWifiBatchedScanTimer[i].detach();
          }
        }
        if (mWifiMulticastTimer != null) {
          mWifiMulticastTimer.detach();
        }
        if (mAudioTurnedOnTimer != null)
        {
          mAudioTurnedOnTimer.detach();
          mAudioTurnedOnTimer = null;
        }
        if (mVideoTurnedOnTimer != null)
        {
          mVideoTurnedOnTimer.detach();
          mVideoTurnedOnTimer = null;
        }
        if (mFlashlightTurnedOnTimer != null)
        {
          mFlashlightTurnedOnTimer.detach();
          mFlashlightTurnedOnTimer = null;
        }
        if (mCameraTurnedOnTimer != null)
        {
          mCameraTurnedOnTimer.detach();
          mCameraTurnedOnTimer = null;
        }
        if (mForegroundActivityTimer != null)
        {
          mForegroundActivityTimer.detach();
          mForegroundActivityTimer = null;
        }
        if (mForegroundServiceTimer != null)
        {
          mForegroundServiceTimer.detach();
          mForegroundServiceTimer = null;
        }
        if (mAggregatedPartialWakelockTimer != null)
        {
          mAggregatedPartialWakelockTimer.detach();
          mAggregatedPartialWakelockTimer = null;
        }
        if (mBluetoothScanTimer != null)
        {
          mBluetoothScanTimer.detach();
          mBluetoothScanTimer = null;
        }
        if (mBluetoothUnoptimizedScanTimer != null)
        {
          mBluetoothUnoptimizedScanTimer.detach();
          mBluetoothUnoptimizedScanTimer = null;
        }
        if (mBluetoothScanResultCounter != null)
        {
          mBluetoothScanResultCounter.detach();
          mBluetoothScanResultCounter = null;
        }
        if (mBluetoothScanResultBgCounter != null)
        {
          mBluetoothScanResultBgCounter.detach();
          mBluetoothScanResultBgCounter = null;
        }
        if (mUserActivityCounters != null) {
          for (i = 0; i < 4; i++) {
            mUserActivityCounters[i].detach();
          }
        }
        if (mNetworkByteActivityCounters != null) {
          for (i = 0; i < 10; i++)
          {
            mNetworkByteActivityCounters[i].detach();
            mNetworkPacketActivityCounters[i].detach();
          }
        }
        if (mWifiControllerActivity != null) {
          mWifiControllerActivity.detach();
        }
        if (mBluetoothControllerActivity != null) {
          mBluetoothControllerActivity.detach();
        }
        if (mModemControllerActivity != null) {
          mModemControllerActivity.detach();
        }
        mPids.clear();
        mUserCpuTime.detach();
        mSystemCpuTime.detach();
        if (mCpuClusterSpeedTimesUs != null) {
          for (??? : mCpuClusterSpeedTimesUs) {
            if (??? != null)
            {
              int i7 = ???.length;
              for (i2 = 0; i2 < i7; i2++)
              {
                localObject1 = ???[i2];
                if (localObject1 != null) {
                  ((BatteryStatsImpl.LongSamplingCounter)localObject1).detach();
                }
              }
            }
          }
        }
        if (mCpuFreqTimeMs != null) {
          mCpuFreqTimeMs.detach();
        }
        if (mScreenOffCpuFreqTimeMs != null) {
          mScreenOffCpuFreqTimeMs.detach();
        }
        mCpuActiveTimeMs.detach();
        mCpuClusterTimesMs.detach();
        if (mProcStateTimeMs != null) {
          for (localObject2 : mProcStateTimeMs) {
            if (localObject2 != null) {
              ((BatteryStatsImpl.LongSamplingCounterArray)localObject2).detach();
            }
          }
        }
        if (mProcStateScreenOffTimeMs != null) {
          for (localObject1 : mProcStateScreenOffTimeMs) {
            if (localObject1 != null) {
              ((BatteryStatsImpl.LongSamplingCounterArray)localObject1).detach();
            }
          }
        }
        BatteryStatsImpl.detachLongCounterIfNotNull(mMobileRadioApWakeupCount);
        BatteryStatsImpl.detachLongCounterIfNotNull(mWifiRadioApWakeupCount);
      }
      boolean bool;
      if (k == 0) {
        bool = true;
      } else {
        bool = false;
      }
      return bool;
    }
    
    @VisibleForTesting
    public void setProcessStateForTest(int paramInt)
    {
      mProcessState = paramInt;
    }
    
    public boolean updateOnBatteryBgTimeBase(long paramLong1, long paramLong2)
    {
      boolean bool;
      if ((mBsi.mOnBatteryTimeBase.isRunning()) && (isInBackground())) {
        bool = true;
      } else {
        bool = false;
      }
      return mOnBatteryBackgroundTimeBase.setRunning(bool, paramLong1, paramLong2);
    }
    
    public boolean updateOnBatteryScreenOffBgTimeBase(long paramLong1, long paramLong2)
    {
      boolean bool;
      if ((mBsi.mOnBatteryScreenOffTimeBase.isRunning()) && (isInBackground())) {
        bool = true;
      } else {
        bool = false;
      }
      return mOnBatteryScreenOffBackgroundTimeBase.setRunning(bool, paramLong1, paramLong2);
    }
    
    @GuardedBy("mBsi")
    public void updateUidProcessStateLocked(int paramInt)
    {
      boolean bool;
      if (paramInt == 3) {
        bool = true;
      } else {
        bool = false;
      }
      paramInt = BatteryStats.mapToInternalProcessState(paramInt);
      if ((mProcessState == paramInt) && (bool == mInForegroundService)) {
        return;
      }
      long l1 = mBsi.mClocks.elapsedRealtime();
      if (mProcessState != paramInt)
      {
        long l2 = mBsi.mClocks.uptimeMillis();
        if (mProcessState != 19)
        {
          mProcessStateTimer[mProcessState].stopRunningLocked(l1);
          if (mBsi.trackPerProcStateCpuTimes())
          {
            if (mBsi.mPendingUids.size() == 0)
            {
              mBsi.mExternalSync.scheduleReadProcStateCpuTimes(mBsi.mOnBatteryTimeBase.isRunning(), mBsi.mOnBatteryScreenOffTimeBase.isRunning(), mBsi.mConstants.PROC_STATE_CPU_TIMES_READ_DELAY_MS);
              BatteryStatsImpl.access$1408(mBsi);
            }
            else
            {
              BatteryStatsImpl.access$1508(mBsi);
            }
            if ((mBsi.mPendingUids.indexOfKey(mUid) < 0) || (ArrayUtils.contains(CRITICAL_PROC_STATES, mProcessState))) {
              mBsi.mPendingUids.put(mUid, mProcessState);
            }
          }
          else
          {
            mBsi.mPendingUids.clear();
          }
        }
        mProcessState = paramInt;
        if (paramInt != 19)
        {
          if (mProcessStateTimer[paramInt] == null) {
            makeProcessState(paramInt, null);
          }
          mProcessStateTimer[paramInt].startRunningLocked(l1);
        }
        updateOnBatteryBgTimeBase(l2 * 1000L, l1 * 1000L);
        updateOnBatteryScreenOffBgTimeBase(l2 * 1000L, 1000L * l1);
      }
      if (bool != mInForegroundService)
      {
        if (bool) {
          noteForegroundServiceResumedLocked(l1);
        } else {
          noteForegroundServicePausedLocked(l1);
        }
        mInForegroundService = bool;
      }
    }
    
    void writeJobCompletionsToParcelLocked(Parcel paramParcel)
    {
      int i = mJobCompletions.size();
      paramParcel.writeInt(i);
      for (int j = 0; j < i; j++)
      {
        paramParcel.writeString((String)mJobCompletions.keyAt(j));
        SparseIntArray localSparseIntArray = (SparseIntArray)mJobCompletions.valueAt(j);
        int k = localSparseIntArray.size();
        paramParcel.writeInt(k);
        for (int m = 0; m < k; m++)
        {
          paramParcel.writeInt(localSparseIntArray.keyAt(m));
          paramParcel.writeInt(localSparseIntArray.valueAt(m));
        }
      }
    }
    
    void writeToParcelLocked(Parcel paramParcel, long paramLong1, long paramLong2)
    {
      mOnBatteryBackgroundTimeBase.writeToParcel(paramParcel, paramLong1, paramLong2);
      mOnBatteryScreenOffBackgroundTimeBase.writeToParcel(paramParcel, paramLong1, paramLong2);
      ArrayMap localArrayMap = mWakelockStats.getMap();
      int i = localArrayMap.size();
      paramParcel.writeInt(i);
      int j = 0;
      for (int k = 0; k < i; k++)
      {
        paramParcel.writeString((String)localArrayMap.keyAt(k));
        ((Wakelock)localArrayMap.valueAt(k)).writeToParcelLocked(paramParcel, paramLong2);
      }
      Object localObject1 = mSyncStats.getMap();
      int m = ((ArrayMap)localObject1).size();
      paramParcel.writeInt(m);
      for (k = 0; k < m; k++)
      {
        paramParcel.writeString((String)((ArrayMap)localObject1).keyAt(k));
        BatteryStatsImpl.Timer.writeTimerToParcel(paramParcel, (BatteryStatsImpl.DualTimer)((ArrayMap)localObject1).valueAt(k), paramLong2);
      }
      Object localObject2 = mJobStats.getMap();
      m = ((ArrayMap)localObject2).size();
      paramParcel.writeInt(m);
      for (k = 0; k < m; k++)
      {
        paramParcel.writeString((String)((ArrayMap)localObject2).keyAt(k));
        BatteryStatsImpl.Timer.writeTimerToParcel(paramParcel, (BatteryStatsImpl.DualTimer)((ArrayMap)localObject2).valueAt(k), paramLong2);
      }
      writeJobCompletionsToParcelLocked(paramParcel);
      mJobsDeferredEventCount.writeToParcel(paramParcel);
      mJobsDeferredCount.writeToParcel(paramParcel);
      mJobsFreshnessTimeMs.writeToParcel(paramParcel);
      for (k = 0; k < BatteryStats.JOB_FRESHNESS_BUCKETS.length; k++) {
        BatteryStatsImpl.Counter.writeCounterToParcel(paramParcel, mJobsFreshnessBuckets[k]);
      }
      m = mSensorStats.size();
      paramParcel.writeInt(m);
      for (k = 0; k < m; k++)
      {
        paramParcel.writeInt(mSensorStats.keyAt(k));
        ((Sensor)mSensorStats.valueAt(k)).writeToParcelLocked(paramParcel, paramLong2);
      }
      m = mProcessStats.size();
      paramParcel.writeInt(m);
      for (k = 0; k < m; k++)
      {
        paramParcel.writeString((String)mProcessStats.keyAt(k));
        ((Proc)mProcessStats.valueAt(k)).writeToParcelLocked(paramParcel);
      }
      paramParcel.writeInt(mPackageStats.size());
      Iterator localIterator = mPackageStats.entrySet().iterator();
      while (localIterator.hasNext())
      {
        localObject2 = (Map.Entry)localIterator.next();
        paramParcel.writeString((String)((Map.Entry)localObject2).getKey());
        ((Pkg)((Map.Entry)localObject2).getValue()).writeToParcelLocked(paramParcel);
      }
      if (mWifiRunningTimer != null)
      {
        paramParcel.writeInt(1);
        mWifiRunningTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mFullWifiLockTimer != null)
      {
        paramParcel.writeInt(1);
        mFullWifiLockTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mWifiScanTimer != null)
      {
        paramParcel.writeInt(1);
        mWifiScanTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      for (k = 0; k < 5; k++) {
        if (mWifiBatchedScanTimer[k] != null)
        {
          paramParcel.writeInt(1);
          mWifiBatchedScanTimer[k].writeToParcel(paramParcel, paramLong2);
        }
        else
        {
          paramParcel.writeInt(0);
        }
      }
      if (mWifiMulticastTimer != null)
      {
        paramParcel.writeInt(1);
        mWifiMulticastTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mAudioTurnedOnTimer != null)
      {
        paramParcel.writeInt(1);
        mAudioTurnedOnTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mVideoTurnedOnTimer != null)
      {
        paramParcel.writeInt(1);
        mVideoTurnedOnTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mFlashlightTurnedOnTimer != null)
      {
        paramParcel.writeInt(1);
        mFlashlightTurnedOnTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mCameraTurnedOnTimer != null)
      {
        paramParcel.writeInt(1);
        mCameraTurnedOnTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mForegroundActivityTimer != null)
      {
        paramParcel.writeInt(1);
        mForegroundActivityTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mForegroundServiceTimer != null)
      {
        paramParcel.writeInt(1);
        mForegroundServiceTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mAggregatedPartialWakelockTimer != null)
      {
        paramParcel.writeInt(1);
        mAggregatedPartialWakelockTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothScanTimer != null)
      {
        paramParcel.writeInt(1);
        mBluetoothScanTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothUnoptimizedScanTimer != null)
      {
        paramParcel.writeInt(1);
        mBluetoothUnoptimizedScanTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothScanResultCounter != null)
      {
        paramParcel.writeInt(1);
        mBluetoothScanResultCounter.writeToParcel(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothScanResultBgCounter != null)
      {
        paramParcel.writeInt(1);
        mBluetoothScanResultBgCounter.writeToParcel(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      for (k = 0; k < 7; k++) {
        if (mProcessStateTimer[k] != null)
        {
          paramParcel.writeInt(1);
          mProcessStateTimer[k].writeToParcel(paramParcel, paramLong2);
        }
        else
        {
          paramParcel.writeInt(0);
        }
      }
      if (mVibratorOnTimer != null)
      {
        paramParcel.writeInt(1);
        mVibratorOnTimer.writeToParcel(paramParcel, paramLong2);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mUserActivityCounters != null)
      {
        paramParcel.writeInt(1);
        for (k = 0; k < 4; k++) {
          mUserActivityCounters[k].writeToParcel(paramParcel);
        }
      }
      paramParcel.writeInt(0);
      if (mNetworkByteActivityCounters != null)
      {
        paramParcel.writeInt(1);
        for (k = 0; k < 10; k++)
        {
          mNetworkByteActivityCounters[k].writeToParcel(paramParcel);
          mNetworkPacketActivityCounters[k].writeToParcel(paramParcel);
        }
        mMobileRadioActiveTime.writeToParcel(paramParcel);
        mMobileRadioActiveCount.writeToParcel(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mWifiControllerActivity != null)
      {
        paramParcel.writeInt(1);
        mWifiControllerActivity.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mBluetoothControllerActivity != null)
      {
        paramParcel.writeInt(1);
        mBluetoothControllerActivity.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mModemControllerActivity != null)
      {
        paramParcel.writeInt(1);
        mModemControllerActivity.writeToParcel(paramParcel, 0);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      mUserCpuTime.writeToParcel(paramParcel);
      mSystemCpuTime.writeToParcel(paramParcel);
      if (mCpuClusterSpeedTimesUs != null)
      {
        paramParcel.writeInt(1);
        paramParcel.writeInt(mCpuClusterSpeedTimesUs.length);
        localObject2 = mCpuClusterSpeedTimesUs;
        int n = localObject2.length;
        for (k = j; k < n; k++)
        {
          localIterator = localObject2[k];
          if (localIterator != null)
          {
            paramParcel.writeInt(1);
            paramParcel.writeInt(localIterator.length);
            m = localIterator.length;
            for (j = 0; j < m; j++)
            {
              Object localObject3 = localIterator[j];
              if (localObject3 != null)
              {
                paramParcel.writeInt(1);
                localObject3.writeToParcel(paramParcel);
              }
              else
              {
                paramParcel.writeInt(0);
              }
            }
          }
          else
          {
            paramParcel.writeInt(0);
          }
        }
      }
      else
      {
        paramParcel.writeInt(0);
      }
      BatteryStatsImpl.LongSamplingCounterArray.writeToParcel(paramParcel, mCpuFreqTimeMs);
      BatteryStatsImpl.LongSamplingCounterArray.writeToParcel(paramParcel, mScreenOffCpuFreqTimeMs);
      mCpuActiveTimeMs.writeToParcel(paramParcel);
      mCpuClusterTimesMs.writeToParcel(paramParcel);
      if (mProcStateTimeMs != null)
      {
        paramParcel.writeInt(mProcStateTimeMs.length);
        localObject1 = mProcStateTimeMs;
        i = localObject1.length;
        for (k = 0; k < i; k++) {
          BatteryStatsImpl.LongSamplingCounterArray.writeToParcel(paramParcel, localObject1[k]);
        }
      }
      paramParcel.writeInt(0);
      if (mProcStateScreenOffTimeMs != null)
      {
        paramParcel.writeInt(mProcStateScreenOffTimeMs.length);
        localObject1 = mProcStateScreenOffTimeMs;
        i = localObject1.length;
        for (k = 0; k < i; k++) {
          BatteryStatsImpl.LongSamplingCounterArray.writeToParcel(paramParcel, localObject1[k]);
        }
      }
      paramParcel.writeInt(0);
      if (mMobileRadioApWakeupCount != null)
      {
        paramParcel.writeInt(1);
        mMobileRadioApWakeupCount.writeToParcel(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
      if (mWifiRadioApWakeupCount != null)
      {
        paramParcel.writeInt(1);
        mWifiRadioApWakeupCount.writeToParcel(paramParcel);
      }
      else
      {
        paramParcel.writeInt(0);
      }
    }
    
    public static class Pkg
      extends BatteryStats.Uid.Pkg
      implements BatteryStatsImpl.TimeBaseObs
    {
      protected BatteryStatsImpl mBsi;
      final ArrayMap<String, Serv> mServiceStats = new ArrayMap();
      ArrayMap<String, BatteryStatsImpl.Counter> mWakeupAlarms = new ArrayMap();
      
      public Pkg(BatteryStatsImpl paramBatteryStatsImpl)
      {
        mBsi = paramBatteryStatsImpl;
        mBsi.mOnBatteryScreenOffTimeBase.add(this);
      }
      
      void detach()
      {
        mBsi.mOnBatteryScreenOffTimeBase.remove(this);
      }
      
      public ArrayMap<String, ? extends BatteryStats.Uid.Pkg.Serv> getServiceStats()
      {
        return mServiceStats;
      }
      
      public ArrayMap<String, ? extends BatteryStats.Counter> getWakeupAlarmStats()
      {
        return mWakeupAlarms;
      }
      
      final Serv newServiceStatsLocked()
      {
        return new Serv(mBsi);
      }
      
      public void noteWakeupAlarmLocked(String paramString)
      {
        BatteryStatsImpl.Counter localCounter1 = (BatteryStatsImpl.Counter)mWakeupAlarms.get(paramString);
        BatteryStatsImpl.Counter localCounter2 = localCounter1;
        if (localCounter1 == null)
        {
          localCounter2 = new BatteryStatsImpl.Counter(mBsi.mOnBatteryScreenOffTimeBase);
          mWakeupAlarms.put(paramString, localCounter2);
        }
        localCounter2.stepAtomic();
      }
      
      public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3) {}
      
      public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3) {}
      
      void readFromParcelLocked(Parcel paramParcel)
      {
        int i = paramParcel.readInt();
        mWakeupAlarms.clear();
        int j = 0;
        Object localObject;
        for (int k = 0; k < i; k++)
        {
          localObject = paramParcel.readString();
          mWakeupAlarms.put(localObject, new BatteryStatsImpl.Counter(mBsi.mOnBatteryScreenOffTimeBase, paramParcel));
        }
        i = paramParcel.readInt();
        mServiceStats.clear();
        for (k = j; k < i; k++)
        {
          String str = paramParcel.readString();
          localObject = new Serv(mBsi);
          mServiceStats.put(str, localObject);
          ((Serv)localObject).readFromParcelLocked(paramParcel);
        }
      }
      
      void writeToParcelLocked(Parcel paramParcel)
      {
        int i = mWakeupAlarms.size();
        paramParcel.writeInt(i);
        int j = 0;
        for (int k = 0; k < i; k++)
        {
          paramParcel.writeString((String)mWakeupAlarms.keyAt(k));
          ((BatteryStatsImpl.Counter)mWakeupAlarms.valueAt(k)).writeToParcel(paramParcel);
        }
        i = mServiceStats.size();
        paramParcel.writeInt(i);
        for (k = j; k < i; k++)
        {
          paramParcel.writeString((String)mServiceStats.keyAt(k));
          ((Serv)mServiceStats.valueAt(k)).writeToParcelLocked(paramParcel);
        }
      }
      
      public static class Serv
        extends BatteryStats.Uid.Pkg.Serv
        implements BatteryStatsImpl.TimeBaseObs
      {
        protected BatteryStatsImpl mBsi;
        protected int mLastLaunches;
        protected long mLastStartTime;
        protected int mLastStarts;
        protected boolean mLaunched;
        protected long mLaunchedSince;
        protected long mLaunchedTime;
        protected int mLaunches;
        protected int mLoadedLaunches;
        protected long mLoadedStartTime;
        protected int mLoadedStarts;
        protected BatteryStatsImpl.Uid.Pkg mPkg;
        protected boolean mRunning;
        protected long mRunningSince;
        protected long mStartTime;
        protected int mStarts;
        protected int mUnpluggedLaunches;
        protected long mUnpluggedStartTime;
        protected int mUnpluggedStarts;
        
        public Serv(BatteryStatsImpl paramBatteryStatsImpl)
        {
          mBsi = paramBatteryStatsImpl;
          mBsi.mOnBatteryTimeBase.add(this);
        }
        
        public void detach()
        {
          mBsi.mOnBatteryTimeBase.remove(this);
        }
        
        public BatteryStatsImpl getBatteryStats()
        {
          return mBsi;
        }
        
        public long getLaunchTimeToNowLocked(long paramLong)
        {
          if (!mLaunched) {
            return mLaunchedTime;
          }
          return mLaunchedTime + paramLong - mLaunchedSince;
        }
        
        public int getLaunches(int paramInt)
        {
          int i = mLaunches;
          int j;
          if (paramInt == 1)
          {
            j = i - mLoadedLaunches;
          }
          else
          {
            j = i;
            if (paramInt == 2) {
              j = i - mUnpluggedLaunches;
            }
          }
          return j;
        }
        
        public long getStartTime(long paramLong, int paramInt)
        {
          long l = getStartTimeToNowLocked(paramLong);
          if (paramInt == 1)
          {
            paramLong = l - mLoadedStartTime;
          }
          else
          {
            paramLong = l;
            if (paramInt == 2) {
              paramLong = l - mUnpluggedStartTime;
            }
          }
          return paramLong;
        }
        
        public long getStartTimeToNowLocked(long paramLong)
        {
          if (!mRunning) {
            return mStartTime;
          }
          return mStartTime + paramLong - mRunningSince;
        }
        
        public int getStarts(int paramInt)
        {
          int i = mStarts;
          int j;
          if (paramInt == 1)
          {
            j = i - mLoadedStarts;
          }
          else
          {
            j = i;
            if (paramInt == 2) {
              j = i - mUnpluggedStarts;
            }
          }
          return j;
        }
        
        public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
        {
          mUnpluggedStartTime = getStartTimeToNowLocked(paramLong2);
          mUnpluggedStarts = mStarts;
          mUnpluggedLaunches = mLaunches;
        }
        
        public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3) {}
        
        public void readFromParcelLocked(Parcel paramParcel)
        {
          mStartTime = paramParcel.readLong();
          mRunningSince = paramParcel.readLong();
          int i = paramParcel.readInt();
          boolean bool1 = true;
          boolean bool2;
          if (i != 0) {
            bool2 = true;
          } else {
            bool2 = false;
          }
          mRunning = bool2;
          mStarts = paramParcel.readInt();
          mLaunchedTime = paramParcel.readLong();
          mLaunchedSince = paramParcel.readLong();
          if (paramParcel.readInt() != 0) {
            bool2 = bool1;
          } else {
            bool2 = false;
          }
          mLaunched = bool2;
          mLaunches = paramParcel.readInt();
          mLoadedStartTime = paramParcel.readLong();
          mLoadedStarts = paramParcel.readInt();
          mLoadedLaunches = paramParcel.readInt();
          mLastStartTime = 0L;
          mLastStarts = 0;
          mLastLaunches = 0;
          mUnpluggedStartTime = paramParcel.readLong();
          mUnpluggedStarts = paramParcel.readInt();
          mUnpluggedLaunches = paramParcel.readInt();
        }
        
        public void startLaunchedLocked()
        {
          if (!mLaunched)
          {
            mLaunches += 1;
            mLaunchedSince = mBsi.getBatteryUptimeLocked();
            mLaunched = true;
          }
        }
        
        public void startRunningLocked()
        {
          if (!mRunning)
          {
            mStarts += 1;
            mRunningSince = mBsi.getBatteryUptimeLocked();
            mRunning = true;
          }
        }
        
        public void stopLaunchedLocked()
        {
          if (mLaunched)
          {
            long l = mBsi.getBatteryUptimeLocked() - mLaunchedSince;
            if (l > 0L) {
              mLaunchedTime += l;
            } else {
              mLaunches -= 1;
            }
            mLaunched = false;
          }
        }
        
        public void stopRunningLocked()
        {
          if (mRunning)
          {
            long l = mBsi.getBatteryUptimeLocked() - mRunningSince;
            if (l > 0L) {
              mStartTime += l;
            } else {
              mStarts -= 1;
            }
            mRunning = false;
          }
        }
        
        public void writeToParcelLocked(Parcel paramParcel)
        {
          paramParcel.writeLong(mStartTime);
          paramParcel.writeLong(mRunningSince);
          paramParcel.writeInt(mRunning);
          paramParcel.writeInt(mStarts);
          paramParcel.writeLong(mLaunchedTime);
          paramParcel.writeLong(mLaunchedSince);
          paramParcel.writeInt(mLaunched);
          paramParcel.writeInt(mLaunches);
          paramParcel.writeLong(mLoadedStartTime);
          paramParcel.writeInt(mLoadedStarts);
          paramParcel.writeInt(mLoadedLaunches);
          paramParcel.writeLong(mUnpluggedStartTime);
          paramParcel.writeInt(mUnpluggedStarts);
          paramParcel.writeInt(mUnpluggedLaunches);
        }
      }
    }
    
    public static class Proc
      extends BatteryStats.Uid.Proc
      implements BatteryStatsImpl.TimeBaseObs
    {
      boolean mActive = true;
      protected BatteryStatsImpl mBsi;
      ArrayList<BatteryStats.Uid.Proc.ExcessivePower> mExcessivePower;
      long mForegroundTime;
      long mLoadedForegroundTime;
      int mLoadedNumAnrs;
      int mLoadedNumCrashes;
      int mLoadedStarts;
      long mLoadedSystemTime;
      long mLoadedUserTime;
      final String mName;
      int mNumAnrs;
      int mNumCrashes;
      int mStarts;
      long mSystemTime;
      long mUnpluggedForegroundTime;
      int mUnpluggedNumAnrs;
      int mUnpluggedNumCrashes;
      int mUnpluggedStarts;
      long mUnpluggedSystemTime;
      long mUnpluggedUserTime;
      long mUserTime;
      
      public Proc(BatteryStatsImpl paramBatteryStatsImpl, String paramString)
      {
        mBsi = paramBatteryStatsImpl;
        mName = paramString;
        mBsi.mOnBatteryTimeBase.add(this);
      }
      
      public void addCpuTimeLocked(int paramInt1, int paramInt2)
      {
        addCpuTimeLocked(paramInt1, paramInt2, mBsi.mOnBatteryTimeBase.isRunning());
      }
      
      public void addCpuTimeLocked(int paramInt1, int paramInt2, boolean paramBoolean)
      {
        if (paramBoolean)
        {
          mUserTime += paramInt1;
          mSystemTime += paramInt2;
        }
      }
      
      public void addExcessiveCpu(long paramLong1, long paramLong2)
      {
        if (mExcessivePower == null) {
          mExcessivePower = new ArrayList();
        }
        BatteryStats.Uid.Proc.ExcessivePower localExcessivePower = new BatteryStats.Uid.Proc.ExcessivePower();
        type = 2;
        overTime = paramLong1;
        usedTime = paramLong2;
        mExcessivePower.add(localExcessivePower);
      }
      
      public void addForegroundTimeLocked(long paramLong)
      {
        mForegroundTime += paramLong;
      }
      
      public int countExcessivePowers()
      {
        int i;
        if (mExcessivePower != null) {
          i = mExcessivePower.size();
        } else {
          i = 0;
        }
        return i;
      }
      
      void detach()
      {
        mActive = false;
        mBsi.mOnBatteryTimeBase.remove(this);
      }
      
      public BatteryStats.Uid.Proc.ExcessivePower getExcessivePower(int paramInt)
      {
        if (mExcessivePower != null) {
          return (BatteryStats.Uid.Proc.ExcessivePower)mExcessivePower.get(paramInt);
        }
        return null;
      }
      
      public long getForegroundTime(int paramInt)
      {
        long l1 = mForegroundTime;
        long l2;
        if (paramInt == 1)
        {
          l2 = l1 - mLoadedForegroundTime;
        }
        else
        {
          l2 = l1;
          if (paramInt == 2) {
            l2 = l1 - mUnpluggedForegroundTime;
          }
        }
        return l2;
      }
      
      public int getNumAnrs(int paramInt)
      {
        int i = mNumAnrs;
        int j;
        if (paramInt == 1)
        {
          j = i - mLoadedNumAnrs;
        }
        else
        {
          j = i;
          if (paramInt == 2) {
            j = i - mUnpluggedNumAnrs;
          }
        }
        return j;
      }
      
      public int getNumCrashes(int paramInt)
      {
        int i = mNumCrashes;
        int j;
        if (paramInt == 1)
        {
          j = i - mLoadedNumCrashes;
        }
        else
        {
          j = i;
          if (paramInt == 2) {
            j = i - mUnpluggedNumCrashes;
          }
        }
        return j;
      }
      
      public int getStarts(int paramInt)
      {
        int i = mStarts;
        int j;
        if (paramInt == 1)
        {
          j = i - mLoadedStarts;
        }
        else
        {
          j = i;
          if (paramInt == 2) {
            j = i - mUnpluggedStarts;
          }
        }
        return j;
      }
      
      public long getSystemTime(int paramInt)
      {
        long l1 = mSystemTime;
        long l2;
        if (paramInt == 1)
        {
          l2 = l1 - mLoadedSystemTime;
        }
        else
        {
          l2 = l1;
          if (paramInt == 2) {
            l2 = l1 - mUnpluggedSystemTime;
          }
        }
        return l2;
      }
      
      public long getUserTime(int paramInt)
      {
        long l1 = mUserTime;
        long l2;
        if (paramInt == 1)
        {
          l2 = l1 - mLoadedUserTime;
        }
        else
        {
          l2 = l1;
          if (paramInt == 2) {
            l2 = l1 - mUnpluggedUserTime;
          }
        }
        return l2;
      }
      
      public void incNumAnrsLocked()
      {
        mNumAnrs += 1;
      }
      
      public void incNumCrashesLocked()
      {
        mNumCrashes += 1;
      }
      
      public void incStartsLocked()
      {
        mStarts += 1;
      }
      
      public boolean isActive()
      {
        return mActive;
      }
      
      public void onTimeStarted(long paramLong1, long paramLong2, long paramLong3)
      {
        mUnpluggedUserTime = mUserTime;
        mUnpluggedSystemTime = mSystemTime;
        mUnpluggedForegroundTime = mForegroundTime;
        mUnpluggedStarts = mStarts;
        mUnpluggedNumCrashes = mNumCrashes;
        mUnpluggedNumAnrs = mNumAnrs;
      }
      
      public void onTimeStopped(long paramLong1, long paramLong2, long paramLong3) {}
      
      void readExcessivePowerFromParcelLocked(Parcel paramParcel)
      {
        int i = paramParcel.readInt();
        if (i == 0)
        {
          mExcessivePower = null;
          return;
        }
        if (i <= 10000)
        {
          mExcessivePower = new ArrayList();
          for (int j = 0; j < i; j++)
          {
            BatteryStats.Uid.Proc.ExcessivePower localExcessivePower = new BatteryStats.Uid.Proc.ExcessivePower();
            type = paramParcel.readInt();
            overTime = paramParcel.readLong();
            usedTime = paramParcel.readLong();
            mExcessivePower.add(localExcessivePower);
          }
          return;
        }
        paramParcel = new StringBuilder();
        paramParcel.append("File corrupt: too many excessive power entries ");
        paramParcel.append(i);
        throw new ParcelFormatException(paramParcel.toString());
      }
      
      void readFromParcelLocked(Parcel paramParcel)
      {
        mUserTime = paramParcel.readLong();
        mSystemTime = paramParcel.readLong();
        mForegroundTime = paramParcel.readLong();
        mStarts = paramParcel.readInt();
        mNumCrashes = paramParcel.readInt();
        mNumAnrs = paramParcel.readInt();
        mLoadedUserTime = paramParcel.readLong();
        mLoadedSystemTime = paramParcel.readLong();
        mLoadedForegroundTime = paramParcel.readLong();
        mLoadedStarts = paramParcel.readInt();
        mLoadedNumCrashes = paramParcel.readInt();
        mLoadedNumAnrs = paramParcel.readInt();
        mUnpluggedUserTime = paramParcel.readLong();
        mUnpluggedSystemTime = paramParcel.readLong();
        mUnpluggedForegroundTime = paramParcel.readLong();
        mUnpluggedStarts = paramParcel.readInt();
        mUnpluggedNumCrashes = paramParcel.readInt();
        mUnpluggedNumAnrs = paramParcel.readInt();
        readExcessivePowerFromParcelLocked(paramParcel);
      }
      
      void writeExcessivePowerToParcelLocked(Parcel paramParcel)
      {
        Object localObject = mExcessivePower;
        int i = 0;
        if (localObject == null)
        {
          paramParcel.writeInt(0);
          return;
        }
        int j = mExcessivePower.size();
        paramParcel.writeInt(j);
        while (i < j)
        {
          localObject = (BatteryStats.Uid.Proc.ExcessivePower)mExcessivePower.get(i);
          paramParcel.writeInt(type);
          paramParcel.writeLong(overTime);
          paramParcel.writeLong(usedTime);
          i++;
        }
      }
      
      void writeToParcelLocked(Parcel paramParcel)
      {
        paramParcel.writeLong(mUserTime);
        paramParcel.writeLong(mSystemTime);
        paramParcel.writeLong(mForegroundTime);
        paramParcel.writeInt(mStarts);
        paramParcel.writeInt(mNumCrashes);
        paramParcel.writeInt(mNumAnrs);
        paramParcel.writeLong(mLoadedUserTime);
        paramParcel.writeLong(mLoadedSystemTime);
        paramParcel.writeLong(mLoadedForegroundTime);
        paramParcel.writeInt(mLoadedStarts);
        paramParcel.writeInt(mLoadedNumCrashes);
        paramParcel.writeInt(mLoadedNumAnrs);
        paramParcel.writeLong(mUnpluggedUserTime);
        paramParcel.writeLong(mUnpluggedSystemTime);
        paramParcel.writeLong(mUnpluggedForegroundTime);
        paramParcel.writeInt(mUnpluggedStarts);
        paramParcel.writeInt(mUnpluggedNumCrashes);
        paramParcel.writeInt(mUnpluggedNumAnrs);
        writeExcessivePowerToParcelLocked(paramParcel);
      }
    }
    
    public static class Sensor
      extends BatteryStats.Uid.Sensor
    {
      protected BatteryStatsImpl mBsi;
      final int mHandle;
      BatteryStatsImpl.DualTimer mTimer;
      protected BatteryStatsImpl.Uid mUid;
      
      public Sensor(BatteryStatsImpl paramBatteryStatsImpl, BatteryStatsImpl.Uid paramUid, int paramInt)
      {
        mBsi = paramBatteryStatsImpl;
        mUid = paramUid;
        mHandle = paramInt;
      }
      
      private BatteryStatsImpl.DualTimer readTimersFromParcel(BatteryStatsImpl.TimeBase paramTimeBase1, BatteryStatsImpl.TimeBase paramTimeBase2, Parcel paramParcel)
      {
        if (paramParcel.readInt() == 0) {
          return null;
        }
        ArrayList localArrayList1 = (ArrayList)mBsi.mSensorTimers.get(mHandle);
        ArrayList localArrayList2 = localArrayList1;
        if (localArrayList1 == null)
        {
          localArrayList2 = new ArrayList();
          mBsi.mSensorTimers.put(mHandle, localArrayList2);
        }
        return new BatteryStatsImpl.DualTimer(mBsi.mClocks, mUid, 0, localArrayList2, paramTimeBase1, paramTimeBase2, paramParcel);
      }
      
      public int getHandle()
      {
        return mHandle;
      }
      
      public BatteryStatsImpl.Timer getSensorBackgroundTime()
      {
        if (mTimer == null) {
          return null;
        }
        return mTimer.getSubTimer();
      }
      
      public BatteryStatsImpl.Timer getSensorTime()
      {
        return mTimer;
      }
      
      void readFromParcelLocked(BatteryStatsImpl.TimeBase paramTimeBase1, BatteryStatsImpl.TimeBase paramTimeBase2, Parcel paramParcel)
      {
        mTimer = readTimersFromParcel(paramTimeBase1, paramTimeBase2, paramParcel);
      }
      
      boolean reset()
      {
        if (mTimer.reset(true))
        {
          mTimer = null;
          return true;
        }
        return false;
      }
      
      void writeToParcelLocked(Parcel paramParcel, long paramLong)
      {
        BatteryStatsImpl.Timer.writeTimerToParcel(paramParcel, mTimer, paramLong);
      }
    }
    
    public static class Wakelock
      extends BatteryStats.Uid.Wakelock
    {
      protected BatteryStatsImpl mBsi;
      BatteryStatsImpl.StopwatchTimer mTimerDraw;
      BatteryStatsImpl.StopwatchTimer mTimerFull;
      BatteryStatsImpl.DualTimer mTimerPartial;
      BatteryStatsImpl.StopwatchTimer mTimerWindow;
      protected BatteryStatsImpl.Uid mUid;
      
      public Wakelock(BatteryStatsImpl paramBatteryStatsImpl, BatteryStatsImpl.Uid paramUid)
      {
        mBsi = paramBatteryStatsImpl;
        mUid = paramUid;
      }
      
      private BatteryStatsImpl.DualTimer readDualTimerFromParcel(int paramInt, ArrayList<BatteryStatsImpl.StopwatchTimer> paramArrayList, BatteryStatsImpl.TimeBase paramTimeBase1, BatteryStatsImpl.TimeBase paramTimeBase2, Parcel paramParcel)
      {
        if (paramParcel.readInt() == 0) {
          return null;
        }
        return new BatteryStatsImpl.DualTimer(mBsi.mClocks, mUid, paramInt, paramArrayList, paramTimeBase1, paramTimeBase2, paramParcel);
      }
      
      private BatteryStatsImpl.StopwatchTimer readStopwatchTimerFromParcel(int paramInt, ArrayList<BatteryStatsImpl.StopwatchTimer> paramArrayList, BatteryStatsImpl.TimeBase paramTimeBase, Parcel paramParcel)
      {
        if (paramParcel.readInt() == 0) {
          return null;
        }
        return new BatteryStatsImpl.StopwatchTimer(mBsi.mClocks, mUid, paramInt, paramArrayList, paramTimeBase, paramParcel);
      }
      
      public BatteryStatsImpl.Timer getWakeTime(int paramInt)
      {
        if (paramInt != 18)
        {
          switch (paramInt)
          {
          default: 
            StringBuilder localStringBuilder = new StringBuilder();
            localStringBuilder.append("type = ");
            localStringBuilder.append(paramInt);
            throw new IllegalArgumentException(localStringBuilder.toString());
          case 2: 
            return mTimerWindow;
          case 1: 
            return mTimerFull;
          }
          return mTimerPartial;
        }
        return mTimerDraw;
      }
      
      void readFromParcelLocked(BatteryStatsImpl.TimeBase paramTimeBase1, BatteryStatsImpl.TimeBase paramTimeBase2, BatteryStatsImpl.TimeBase paramTimeBase3, Parcel paramParcel)
      {
        mTimerPartial = readDualTimerFromParcel(0, mBsi.mPartialTimers, paramTimeBase2, paramTimeBase3, paramParcel);
        mTimerFull = readStopwatchTimerFromParcel(1, mBsi.mFullTimers, paramTimeBase1, paramParcel);
        mTimerWindow = readStopwatchTimerFromParcel(2, mBsi.mWindowTimers, paramTimeBase1, paramParcel);
        mTimerDraw = readStopwatchTimerFromParcel(18, mBsi.mDrawTimers, paramTimeBase1, paramParcel);
      }
      
      boolean reset()
      {
        boolean bool1 = false;
        BatteryStatsImpl.StopwatchTimer localStopwatchTimer = mTimerFull;
        boolean bool2 = true;
        if (localStopwatchTimer != null) {
          bool1 = false | mTimerFull.reset(false) ^ true;
        }
        boolean bool3 = bool1;
        if (mTimerPartial != null) {
          bool3 = bool1 | mTimerPartial.reset(false) ^ true;
        }
        bool1 = bool3;
        if (mTimerWindow != null) {
          bool1 = bool3 | mTimerWindow.reset(false) ^ true;
        }
        bool3 = bool1;
        if (mTimerDraw != null) {
          bool3 = bool1 | mTimerDraw.reset(false) ^ true;
        }
        if (!bool3)
        {
          if (mTimerFull != null)
          {
            mTimerFull.detach();
            mTimerFull = null;
          }
          if (mTimerPartial != null)
          {
            mTimerPartial.detach();
            mTimerPartial = null;
          }
          if (mTimerWindow != null)
          {
            mTimerWindow.detach();
            mTimerWindow = null;
          }
          if (mTimerDraw != null)
          {
            mTimerDraw.detach();
            mTimerDraw = null;
          }
        }
        if (bool3) {
          bool2 = false;
        }
        return bool2;
      }
      
      void writeToParcelLocked(Parcel paramParcel, long paramLong)
      {
        BatteryStatsImpl.Timer.writeTimerToParcel(paramParcel, mTimerPartial, paramLong);
        BatteryStatsImpl.Timer.writeTimerToParcel(paramParcel, mTimerFull, paramLong);
        BatteryStatsImpl.Timer.writeTimerToParcel(paramParcel, mTimerWindow, paramLong);
        BatteryStatsImpl.Timer.writeTimerToParcel(paramParcel, mTimerDraw, paramLong);
      }
    }
  }
  
  @VisibleForTesting
  public final class UidToRemove
  {
    int endUid;
    int startUid;
    long timeAddedInQueue;
    
    public UidToRemove(int paramInt1, int paramInt2, long paramLong)
    {
      startUid = paramInt1;
      endUid = paramInt2;
      timeAddedInQueue = paramLong;
    }
    
    public UidToRemove(int paramInt, long paramLong)
    {
      this(paramInt, paramInt, paramLong);
    }
    
    void remove()
    {
      if (startUid == endUid)
      {
        mKernelUidCpuTimeReader.removeUid(startUid);
        mKernelUidCpuFreqTimeReader.removeUid(startUid);
        if (mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME)
        {
          mKernelUidCpuActiveTimeReader.removeUid(startUid);
          mKernelUidCpuClusterTimeReader.removeUid(startUid);
        }
        if (mKernelSingleUidTimeReader != null) {
          mKernelSingleUidTimeReader.removeUid(startUid);
        }
        BatteryStatsImpl.access$108(BatteryStatsImpl.this);
      }
      else if (startUid < endUid)
      {
        mKernelUidCpuFreqTimeReader.removeUidsInRange(startUid, endUid);
        mKernelUidCpuTimeReader.removeUidsInRange(startUid, endUid);
        if (mConstants.TRACK_CPU_ACTIVE_CLUSTER_TIME)
        {
          mKernelUidCpuActiveTimeReader.removeUidsInRange(startUid, endUid);
          mKernelUidCpuClusterTimeReader.removeUidsInRange(startUid, endUid);
        }
        if (mKernelSingleUidTimeReader != null) {
          mKernelSingleUidTimeReader.removeUidsInRange(startUid, endUid);
        }
        BatteryStatsImpl.access$108(BatteryStatsImpl.this);
      }
      else
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("End UID ");
        localStringBuilder.append(endUid);
        localStringBuilder.append(" is smaller than start UID ");
        localStringBuilder.append(startUid);
        Slog.w("BatteryStatsImpl", localStringBuilder.toString());
      }
    }
  }
  
  public static abstract class UserInfoProvider
  {
    private int[] userIds;
    
    public UserInfoProvider() {}
    
    @VisibleForTesting
    public boolean exists(int paramInt)
    {
      boolean bool;
      if (userIds != null) {
        bool = ArrayUtils.contains(userIds, paramInt);
      } else {
        bool = true;
      }
      return bool;
    }
    
    protected abstract int[] getUserIds();
    
    @VisibleForTesting
    public final void refreshUserIds()
    {
      userIds = getUserIds();
    }
  }
}
