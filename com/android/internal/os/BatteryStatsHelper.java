package com.android.internal.os;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.BatteryStats;
import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.SparseArray;
import android.util.SparseLongArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IBatteryStats;
import com.android.internal.app.IBatteryStats.Stub;
import com.android.internal.util.ArrayUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class BatteryStatsHelper
{
  static final boolean DEBUG = false;
  private static final String TAG = BatteryStatsHelper.class.getSimpleName();
  private static Intent sBatteryBroadcastXfer;
  private static ArrayMap<File, BatteryStats> sFileXfer = new ArrayMap();
  private static BatteryStats sStatsXfer;
  private Intent mBatteryBroadcast;
  private IBatteryStats mBatteryInfo;
  long mBatteryRealtimeUs;
  long mBatteryTimeRemainingUs;
  long mBatteryUptimeUs;
  PowerCalculator mBluetoothPowerCalculator;
  private final List<BatterySipper> mBluetoothSippers = new ArrayList();
  PowerCalculator mCameraPowerCalculator;
  long mChargeTimeRemainingUs;
  private final boolean mCollectBatteryBroadcast;
  private double mComputedPower;
  private final Context mContext;
  PowerCalculator mCpuPowerCalculator;
  PowerCalculator mFlashlightPowerCalculator;
  boolean mHasBluetoothPowerReporting = false;
  boolean mHasWifiPowerReporting = false;
  private double mMaxDrainedPower;
  private double mMaxPower = 1.0D;
  private double mMaxRealPower = 1.0D;
  PowerCalculator mMediaPowerCalculator;
  PowerCalculator mMemoryPowerCalculator;
  private double mMinDrainedPower;
  MobileRadioPowerCalculator mMobileRadioPowerCalculator;
  private final List<BatterySipper> mMobilemsppList = new ArrayList();
  private PackageManager mPackageManager;
  private PowerProfile mPowerProfile;
  long mRawRealtimeUs;
  long mRawUptimeUs;
  PowerCalculator mSensorPowerCalculator;
  private String[] mServicepackageArray;
  private BatteryStats mStats;
  private long mStatsPeriod = 0L;
  private int mStatsType = 0;
  private String[] mSystemPackageArray;
  private double mTotalPower;
  long mTypeBatteryRealtimeUs;
  long mTypeBatteryUptimeUs;
  private final List<BatterySipper> mUsageList = new ArrayList();
  private final SparseArray<List<BatterySipper>> mUserSippers = new SparseArray();
  PowerCalculator mWakelockPowerCalculator;
  private final boolean mWifiOnly;
  PowerCalculator mWifiPowerCalculator;
  private final List<BatterySipper> mWifiSippers = new ArrayList();
  
  public BatteryStatsHelper(Context paramContext)
  {
    this(paramContext, true);
  }
  
  public BatteryStatsHelper(Context paramContext, boolean paramBoolean)
  {
    this(paramContext, paramBoolean, checkWifiOnly(paramContext));
  }
  
  public BatteryStatsHelper(Context paramContext, boolean paramBoolean1, boolean paramBoolean2)
  {
    mContext = paramContext;
    mCollectBatteryBroadcast = paramBoolean1;
    mWifiOnly = paramBoolean2;
    mPackageManager = paramContext.getPackageManager();
    paramContext = paramContext.getResources();
    mSystemPackageArray = paramContext.getStringArray(17235991);
    mServicepackageArray = paramContext.getStringArray(17235990);
  }
  
  private void addAmbientDisplayUsage()
  {
    long l = mStats.getScreenDozeTime(mRawRealtimeUs, mStatsType) / 1000L;
    double d = mPowerProfile.getAveragePower("ambient.on") * l / 3600000.0D;
    if (d > 0.0D) {
      addEntry(BatterySipper.DrainType.AMBIENT_DISPLAY, l, d);
    }
  }
  
  private void addBluetoothUsage()
  {
    BatterySipper localBatterySipper = new BatterySipper(BatterySipper.DrainType.BLUETOOTH, null, 0.0D);
    mBluetoothPowerCalculator.calculateRemaining(localBatterySipper, mStats, mRawRealtimeUs, mRawUptimeUs, mStatsType);
    aggregateSippers(localBatterySipper, mBluetoothSippers, "Bluetooth");
    if (totalPowerMah > 0.0D) {
      mUsageList.add(localBatterySipper);
    }
  }
  
  private BatterySipper addEntry(BatterySipper.DrainType paramDrainType, long paramLong, double paramDouble)
  {
    paramDrainType = new BatterySipper(paramDrainType, null, 0.0D);
    usagePowerMah = paramDouble;
    usageTimeMs = paramLong;
    paramDrainType.sumPower();
    mUsageList.add(paramDrainType);
    return paramDrainType;
  }
  
  private void addIdleUsage()
  {
    double d = (mTypeBatteryRealtimeUs / 1000L * mPowerProfile.getAveragePower("cpu.suspend") + mTypeBatteryUptimeUs / 1000L * mPowerProfile.getAveragePower("cpu.idle")) / 3600000.0D;
    if (d != 0.0D) {
      addEntry(BatterySipper.DrainType.IDLE, mTypeBatteryRealtimeUs / 1000L, d);
    }
  }
  
  private void addMemoryUsage()
  {
    BatterySipper localBatterySipper = new BatterySipper(BatterySipper.DrainType.MEMORY, null, 0.0D);
    mMemoryPowerCalculator.calculateRemaining(localBatterySipper, mStats, mRawRealtimeUs, mRawUptimeUs, mStatsType);
    localBatterySipper.sumPower();
    if (totalPowerMah > 0.0D) {
      mUsageList.add(localBatterySipper);
    }
  }
  
  private void addPhoneUsage()
  {
    long l = mStats.getPhoneOnTime(mRawRealtimeUs, mStatsType) / 1000L;
    double d = mPowerProfile.getAveragePower("radio.active") * l / 3600000.0D;
    if (d != 0.0D) {
      addEntry(BatterySipper.DrainType.PHONE, l, d);
    }
  }
  
  private void addRadioUsage()
  {
    BatterySipper localBatterySipper = new BatterySipper(BatterySipper.DrainType.CELL, null, 0.0D);
    mMobileRadioPowerCalculator.calculateRemaining(localBatterySipper, mStats, mRawRealtimeUs, mRawUptimeUs, mStatsType);
    localBatterySipper.sumPower();
    if (totalPowerMah > 0.0D) {
      mUsageList.add(localBatterySipper);
    }
  }
  
  private void addScreenUsage()
  {
    long l = mStats.getScreenOnTime(mRawRealtimeUs, mStatsType) / 1000L;
    double d1 = 0.0D + l * mPowerProfile.getAveragePower("screen.on");
    double d2 = mPowerProfile.getAveragePower("screen.full");
    for (int i = 0; i < 5; i++)
    {
      double d3 = (i + 0.5F) * d2 / 5.0D;
      d1 += mStats.getScreenBrightnessTime(i, mRawRealtimeUs, mStatsType) / 1000L * d3;
    }
    d1 /= 3600000.0D;
    if (d1 != 0.0D) {
      addEntry(BatterySipper.DrainType.SCREEN, l, d1);
    }
  }
  
  private void addUserUsage()
  {
    for (int i = 0; i < mUserSippers.size(); i++)
    {
      int j = mUserSippers.keyAt(i);
      BatterySipper localBatterySipper = new BatterySipper(BatterySipper.DrainType.USER, null, 0.0D);
      userId = j;
      aggregateSippers(localBatterySipper, (List)mUserSippers.valueAt(i), "User");
      mUsageList.add(localBatterySipper);
    }
  }
  
  private void addWiFiUsage()
  {
    BatterySipper localBatterySipper = new BatterySipper(BatterySipper.DrainType.WIFI, null, 0.0D);
    mWifiPowerCalculator.calculateRemaining(localBatterySipper, mStats, mRawRealtimeUs, mRawUptimeUs, mStatsType);
    aggregateSippers(localBatterySipper, mWifiSippers, "WIFI");
    if (totalPowerMah > 0.0D) {
      mUsageList.add(localBatterySipper);
    }
  }
  
  private void aggregateSippers(BatterySipper paramBatterySipper, List<BatterySipper> paramList, String paramString)
  {
    for (int i = 0; i < paramList.size(); i++) {
      paramBatterySipper.add((BatterySipper)paramList.get(i));
    }
    paramBatterySipper.computeMobilemspp();
    paramBatterySipper.sumPower();
  }
  
  public static boolean checkHasBluetoothPowerReporting(BatteryStats paramBatteryStats, PowerProfile paramPowerProfile)
  {
    boolean bool;
    if ((paramBatteryStats.hasBluetoothActivityReporting()) && (paramPowerProfile.getAveragePower("bluetooth.controller.idle") != 0.0D) && (paramPowerProfile.getAveragePower("bluetooth.controller.rx") != 0.0D) && (paramPowerProfile.getAveragePower("bluetooth.controller.tx") != 0.0D)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean checkHasWifiPowerReporting(BatteryStats paramBatteryStats, PowerProfile paramPowerProfile)
  {
    boolean bool;
    if ((paramBatteryStats.hasWifiActivityReporting()) && (paramPowerProfile.getAveragePower("wifi.controller.idle") != 0.0D) && (paramPowerProfile.getAveragePower("wifi.controller.rx") != 0.0D) && (paramPowerProfile.getAveragePower("wifi.controller.tx") != 0.0D)) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public static boolean checkWifiOnly(Context paramContext)
  {
    paramContext = (ConnectivityManager)paramContext.getSystemService("connectivity");
    if (paramContext == null) {
      return false;
    }
    return paramContext.isNetworkSupported(0) ^ true;
  }
  
  public static void dropFile(Context paramContext, String paramString)
  {
    makeFilePath(paramContext, paramString).delete();
  }
  
  /* Error */
  private static BatteryStatsImpl getStats(IBatteryStats paramIBatteryStats)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokeinterface 395 1 0
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull +123 -> 131
    //   11: new 397	android/os/ParcelFileDescriptor$AutoCloseInputStream
    //   14: astore_2
    //   15: aload_2
    //   16: aload_1
    //   17: invokespecial 400	android/os/ParcelFileDescriptor$AutoCloseInputStream:<init>	(Landroid/os/ParcelFileDescriptor;)V
    //   20: aconst_null
    //   21: astore_3
    //   22: aload_3
    //   23: astore_0
    //   24: aload_2
    //   25: aload_1
    //   26: invokevirtual 406	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   29: invokestatic 412	android/os/MemoryFile:getSize	(Ljava/io/FileDescriptor;)I
    //   32: invokestatic 416	com/android/internal/os/BatteryStatsHelper:readFully	(Ljava/io/FileInputStream;I)[B
    //   35: astore_1
    //   36: aload_3
    //   37: astore_0
    //   38: invokestatic 422	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   41: astore 4
    //   43: aload_3
    //   44: astore_0
    //   45: aload 4
    //   47: aload_1
    //   48: iconst_0
    //   49: aload_1
    //   50: arraylength
    //   51: invokevirtual 426	android/os/Parcel:unmarshall	([BII)V
    //   54: aload_3
    //   55: astore_0
    //   56: aload 4
    //   58: iconst_0
    //   59: invokevirtual 430	android/os/Parcel:setDataPosition	(I)V
    //   62: aload_3
    //   63: astore_0
    //   64: getstatic 436	com/android/internal/os/BatteryStatsImpl:CREATOR	Landroid/os/Parcelable$Creator;
    //   67: aload 4
    //   69: invokeinterface 442 2 0
    //   74: checkcast 432	com/android/internal/os/BatteryStatsImpl
    //   77: astore_3
    //   78: aload_2
    //   79: invokevirtual 447	java/io/FileInputStream:close	()V
    //   82: aload_3
    //   83: areturn
    //   84: astore_3
    //   85: goto +8 -> 93
    //   88: astore_3
    //   89: aload_3
    //   90: astore_0
    //   91: aload_3
    //   92: athrow
    //   93: aload_0
    //   94: ifnull +19 -> 113
    //   97: aload_2
    //   98: invokevirtual 447	java/io/FileInputStream:close	()V
    //   101: goto +16 -> 117
    //   104: astore_2
    //   105: aload_0
    //   106: aload_2
    //   107: invokevirtual 451	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   110: goto +7 -> 117
    //   113: aload_2
    //   114: invokevirtual 447	java/io/FileInputStream:close	()V
    //   117: aload_3
    //   118: athrow
    //   119: astore_0
    //   120: getstatic 85	com/android/internal/os/BatteryStatsHelper:TAG	Ljava/lang/String;
    //   123: ldc_w 453
    //   126: aload_0
    //   127: invokestatic 459	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   130: pop
    //   131: goto +15 -> 146
    //   134: astore_0
    //   135: getstatic 85	com/android/internal/os/BatteryStatsHelper:TAG	Ljava/lang/String;
    //   138: ldc_w 461
    //   141: aload_0
    //   142: invokestatic 459	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   145: pop
    //   146: new 432	com/android/internal/os/BatteryStatsImpl
    //   149: dup
    //   150: invokespecial 462	com/android/internal/os/BatteryStatsImpl:<init>	()V
    //   153: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	154	0	paramIBatteryStats	IBatteryStats
    //   6	44	1	localObject1	Object
    //   14	84	2	localAutoCloseInputStream	android.os.ParcelFileDescriptor.AutoCloseInputStream
    //   104	10	2	localThrowable1	Throwable
    //   21	62	3	localBatteryStatsImpl	BatteryStatsImpl
    //   84	1	3	localObject2	Object
    //   88	30	3	localThrowable2	Throwable
    //   41	27	4	localParcel	android.os.Parcel
    // Exception table:
    //   from	to	target	type
    //   24	36	84	finally
    //   38	43	84	finally
    //   45	54	84	finally
    //   56	62	84	finally
    //   64	78	84	finally
    //   91	93	84	finally
    //   24	36	88	java/lang/Throwable
    //   38	43	88	java/lang/Throwable
    //   45	54	88	java/lang/Throwable
    //   56	62	88	java/lang/Throwable
    //   64	78	88	java/lang/Throwable
    //   97	101	104	java/lang/Throwable
    //   11	20	119	java/io/IOException
    //   78	82	119	java/io/IOException
    //   97	101	119	java/io/IOException
    //   105	110	119	java/io/IOException
    //   113	117	119	java/io/IOException
    //   117	119	119	java/io/IOException
    //   0	7	134	android/os/RemoteException
    //   11	20	134	android/os/RemoteException
    //   78	82	134	android/os/RemoteException
    //   97	101	134	android/os/RemoteException
    //   105	110	134	android/os/RemoteException
    //   113	117	134	android/os/RemoteException
    //   117	119	134	android/os/RemoteException
    //   120	131	134	android/os/RemoteException
  }
  
  private void load()
  {
    if (mBatteryInfo == null) {
      return;
    }
    mStats = getStats(mBatteryInfo);
    if (mCollectBatteryBroadcast) {
      mBatteryBroadcast = mContext.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }
  }
  
  private static File makeFilePath(Context paramContext, String paramString)
  {
    return new File(paramContext.getFilesDir(), paramString);
  }
  
  public static String makemAh(double paramDouble)
  {
    if (paramDouble == 0.0D) {
      return "0";
    }
    String str;
    if (paramDouble < 1.0E-5D) {
      str = "%.8f";
    }
    for (;;)
    {
      break;
      if (paramDouble < 1.0E-4D) {
        str = "%.7f";
      } else if (paramDouble < 0.001D) {
        str = "%.6f";
      } else if (paramDouble < 0.01D) {
        str = "%.5f";
      } else if (paramDouble < 0.1D) {
        str = "%.4f";
      } else if (paramDouble < 1.0D) {
        str = "%.3f";
      } else if (paramDouble < 10.0D) {
        str = "%.2f";
      } else if (paramDouble < 100.0D) {
        str = "%.1f";
      } else {
        str = "%.0f";
      }
    }
    return String.format(Locale.ENGLISH, str, new Object[] { Double.valueOf(paramDouble) });
  }
  
  private void processAppUsage(SparseArray<UserHandle> paramSparseArray)
  {
    Object localObject1 = paramSparseArray.get(-1);
    int i = 0;
    int j;
    if (localObject1 != null) {
      j = 1;
    } else {
      j = 0;
    }
    mStatsPeriod = mTypeBatteryRealtimeUs;
    Object localObject2 = null;
    localObject1 = mStats.getUidStats();
    int k = ((SparseArray)localObject1).size();
    while (i < k)
    {
      Object localObject3 = (BatteryStats.Uid)((SparseArray)localObject1).valueAt(i);
      BatterySipper localBatterySipper = new BatterySipper(BatterySipper.DrainType.APP, (BatteryStats.Uid)localObject3, 0.0D);
      mCpuPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      mWakelockPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      mMobileRadioPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      mWifiPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      mBluetoothPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      mSensorPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      mCameraPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      mFlashlightPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      mMediaPowerCalculator.calculateApp(localBatterySipper, (BatteryStats.Uid)localObject3, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      Object localObject4;
      if (localBatterySipper.sumPower() == 0.0D)
      {
        localObject4 = localObject2;
        if (((BatteryStats.Uid)localObject3).getUid() != 0) {}
      }
      else
      {
        int m = localBatterySipper.getUid();
        int n = UserHandle.getUserId(m);
        if (m == 1010)
        {
          mWifiSippers.add(localBatterySipper);
        }
        else if (m == 1002)
        {
          mBluetoothSippers.add(localBatterySipper);
        }
        else if ((j == 0) && (paramSparseArray.get(n) == null) && (UserHandle.getAppId(m) >= 10000))
        {
          localObject3 = (List)mUserSippers.get(n);
          localObject4 = localObject3;
          if (localObject3 == null)
          {
            localObject4 = new ArrayList();
            mUserSippers.put(n, localObject4);
          }
          ((List)localObject4).add(localBatterySipper);
        }
        else
        {
          mUsageList.add(localBatterySipper);
        }
        localObject4 = localObject2;
        if (m == 0) {
          localObject4 = localBatterySipper;
        }
      }
      i++;
      localObject2 = localObject4;
    }
    if (localObject2 != null)
    {
      mWakelockPowerCalculator.calculateRemaining(localObject2, mStats, mRawRealtimeUs, mRawUptimeUs, mStatsType);
      localObject2.sumPower();
    }
  }
  
  private void processMiscUsage()
  {
    addUserUsage();
    addPhoneUsage();
    addScreenUsage();
    addAmbientDisplayUsage();
    addWiFiUsage();
    addBluetoothUsage();
    addMemoryUsage();
    addIdleUsage();
    if (!mWifiOnly) {
      addRadioUsage();
    }
  }
  
  public static byte[] readFully(FileInputStream paramFileInputStream)
    throws IOException
  {
    return readFully(paramFileInputStream, paramFileInputStream.available());
  }
  
  public static byte[] readFully(FileInputStream paramFileInputStream, int paramInt)
    throws IOException
  {
    int i = 0;
    Object localObject1 = new byte[paramInt];
    paramInt = i;
    for (;;)
    {
      i = paramFileInputStream.read((byte[])localObject1, paramInt, localObject1.length - paramInt);
      if (i <= 0) {
        return localObject1;
      }
      paramInt += i;
      i = paramFileInputStream.available();
      Object localObject2 = localObject1;
      if (i > localObject1.length - paramInt)
      {
        localObject2 = new byte[paramInt + i];
        System.arraycopy((byte[])localObject1, 0, (byte[])localObject2, 0, paramInt);
      }
      localObject1 = localObject2;
    }
  }
  
  /* Error */
  public static BatteryStats statsFromFile(Context paramContext, String paramString)
  {
    // Byte code:
    //   0: getstatic 92	com/android/internal/os/BatteryStatsHelper:sFileXfer	Landroid/util/ArrayMap;
    //   3: astore_2
    //   4: aload_2
    //   5: monitorenter
    //   6: aload_0
    //   7: aload_1
    //   8: invokestatic 376	com/android/internal/os/BatteryStatsHelper:makeFilePath	(Landroid/content/Context;Ljava/lang/String;)Ljava/io/File;
    //   11: astore_3
    //   12: getstatic 92	com/android/internal/os/BatteryStatsHelper:sFileXfer	Landroid/util/ArrayMap;
    //   15: aload_3
    //   16: invokevirtual 626	android/util/ArrayMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   19: checkcast 170	android/os/BatteryStats
    //   22: astore_0
    //   23: aload_0
    //   24: ifnull +7 -> 31
    //   27: aload_2
    //   28: monitorexit
    //   29: aload_0
    //   30: areturn
    //   31: aconst_null
    //   32: astore 4
    //   34: aconst_null
    //   35: astore 5
    //   37: aload 5
    //   39: astore_0
    //   40: aload 4
    //   42: astore_1
    //   43: new 444	java/io/FileInputStream
    //   46: astore 6
    //   48: aload 5
    //   50: astore_0
    //   51: aload 4
    //   53: astore_1
    //   54: aload 6
    //   56: aload_3
    //   57: invokespecial 629	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   60: aload 6
    //   62: astore_0
    //   63: aload 6
    //   65: astore_1
    //   66: aload 6
    //   68: invokestatic 631	com/android/internal/os/BatteryStatsHelper:readFully	(Ljava/io/FileInputStream;)[B
    //   71: astore 4
    //   73: aload 6
    //   75: astore_0
    //   76: aload 6
    //   78: astore_1
    //   79: invokestatic 422	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   82: astore 5
    //   84: aload 6
    //   86: astore_0
    //   87: aload 6
    //   89: astore_1
    //   90: aload 5
    //   92: aload 4
    //   94: iconst_0
    //   95: aload 4
    //   97: arraylength
    //   98: invokevirtual 426	android/os/Parcel:unmarshall	([BII)V
    //   101: aload 6
    //   103: astore_0
    //   104: aload 6
    //   106: astore_1
    //   107: aload 5
    //   109: iconst_0
    //   110: invokevirtual 430	android/os/Parcel:setDataPosition	(I)V
    //   113: aload 6
    //   115: astore_0
    //   116: aload 6
    //   118: astore_1
    //   119: getstatic 436	com/android/internal/os/BatteryStatsImpl:CREATOR	Landroid/os/Parcelable$Creator;
    //   122: aload 5
    //   124: invokeinterface 442 2 0
    //   129: checkcast 170	android/os/BatteryStats
    //   132: astore 5
    //   134: aload 6
    //   136: invokevirtual 447	java/io/FileInputStream:close	()V
    //   139: goto +4 -> 143
    //   142: astore_0
    //   143: aload_2
    //   144: monitorexit
    //   145: aload 5
    //   147: areturn
    //   148: astore_1
    //   149: goto +49 -> 198
    //   152: astore 6
    //   154: aload_1
    //   155: astore_0
    //   156: getstatic 85	com/android/internal/os/BatteryStatsHelper:TAG	Ljava/lang/String;
    //   159: ldc_w 633
    //   162: aload 6
    //   164: invokestatic 459	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   167: pop
    //   168: aload_1
    //   169: ifnull +14 -> 183
    //   172: aload_1
    //   173: invokevirtual 447	java/io/FileInputStream:close	()V
    //   176: goto +7 -> 183
    //   179: astore_0
    //   180: goto -4 -> 176
    //   183: aload_2
    //   184: monitorexit
    //   185: ldc_w 635
    //   188: invokestatic 641	android/os/ServiceManager:getService	(Ljava/lang/String;)Landroid/os/IBinder;
    //   191: invokestatic 647	com/android/internal/app/IBatteryStats$Stub:asInterface	(Landroid/os/IBinder;)Lcom/android/internal/app/IBatteryStats;
    //   194: invokestatic 467	com/android/internal/os/BatteryStatsHelper:getStats	(Lcom/android/internal/app/IBatteryStats;)Lcom/android/internal/os/BatteryStatsImpl;
    //   197: areturn
    //   198: aload_0
    //   199: ifnull +11 -> 210
    //   202: aload_0
    //   203: invokevirtual 447	java/io/FileInputStream:close	()V
    //   206: goto +4 -> 210
    //   209: astore_0
    //   210: aload_1
    //   211: athrow
    //   212: astore_0
    //   213: aload_2
    //   214: monitorexit
    //   215: aload_0
    //   216: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	217	0	paramContext	Context
    //   0	217	1	paramString	String
    //   3	211	2	localArrayMap	ArrayMap
    //   11	46	3	localFile	File
    //   32	64	4	arrayOfByte	byte[]
    //   35	111	5	localObject	Object
    //   46	89	6	localFileInputStream	FileInputStream
    //   152	11	6	localIOException	IOException
    // Exception table:
    //   from	to	target	type
    //   134	139	142	java/io/IOException
    //   43	48	148	finally
    //   54	60	148	finally
    //   66	73	148	finally
    //   79	84	148	finally
    //   90	101	148	finally
    //   107	113	148	finally
    //   119	134	148	finally
    //   156	168	148	finally
    //   43	48	152	java/io/IOException
    //   54	60	152	java/io/IOException
    //   66	73	152	java/io/IOException
    //   79	84	152	java/io/IOException
    //   90	101	152	java/io/IOException
    //   107	113	152	java/io/IOException
    //   119	134	152	java/io/IOException
    //   172	176	179	java/io/IOException
    //   202	206	209	java/io/IOException
    //   6	23	212	finally
    //   27	29	212	finally
    //   134	139	212	finally
    //   143	145	212	finally
    //   172	176	212	finally
    //   183	185	212	finally
    //   202	206	212	finally
    //   210	212	212	finally
    //   213	215	212	finally
  }
  
  public void clearStats()
  {
    mStats = null;
  }
  
  public long convertMsToUs(long paramLong)
  {
    return 1000L * paramLong;
  }
  
  public long convertUsToMs(long paramLong)
  {
    return paramLong / 1000L;
  }
  
  public void create(BatteryStats paramBatteryStats)
  {
    mPowerProfile = new PowerProfile(mContext);
    mStats = paramBatteryStats;
  }
  
  public void create(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      mStats = sStatsXfer;
      mBatteryBroadcast = sBatteryBroadcastXfer;
    }
    mBatteryInfo = IBatteryStats.Stub.asInterface(ServiceManager.getService("batterystats"));
    mPowerProfile = new PowerProfile(mContext);
  }
  
  public Intent getBatteryBroadcast()
  {
    if ((mBatteryBroadcast == null) && (mCollectBatteryBroadcast)) {
      load();
    }
    return mBatteryBroadcast;
  }
  
  public double getComputedPower()
  {
    return mComputedPower;
  }
  
  @VisibleForTesting
  public long getForegroundActivityTotalTimeUs(BatteryStats.Uid paramUid, long paramLong)
  {
    paramUid = paramUid.getForegroundActivityTimer();
    if (paramUid != null) {
      return paramUid.getTotalTimeLocked(paramLong, 0);
    }
    return 0L;
  }
  
  public double getMaxDrainedPower()
  {
    return mMaxDrainedPower;
  }
  
  public double getMaxPower()
  {
    return mMaxPower;
  }
  
  public double getMaxRealPower()
  {
    return mMaxRealPower;
  }
  
  public double getMinDrainedPower()
  {
    return mMinDrainedPower;
  }
  
  public List<BatterySipper> getMobilemsppList()
  {
    return mMobilemsppList;
  }
  
  public PowerProfile getPowerProfile()
  {
    return mPowerProfile;
  }
  
  @VisibleForTesting
  public long getProcessForegroundTimeMs(BatteryStats.Uid paramUid, int paramInt)
  {
    long l1 = convertMsToUs(SystemClock.elapsedRealtime());
    int[] arrayOfInt = new int[1];
    int i = 0;
    arrayOfInt[0] = 0;
    long l2 = 0L;
    int j = arrayOfInt.length;
    while (i < j)
    {
      l2 += paramUid.getProcessStateTime(arrayOfInt[i], l1, paramInt);
      i++;
    }
    return convertUsToMs(Math.min(l2, getForegroundActivityTotalTimeUs(paramUid, l1)));
  }
  
  public BatteryStats getStats()
  {
    if (mStats == null) {
      load();
    }
    return mStats;
  }
  
  public long getStatsPeriod()
  {
    return mStatsPeriod;
  }
  
  public int getStatsType()
  {
    return mStatsType;
  }
  
  public double getTotalPower()
  {
    return mTotalPower;
  }
  
  public List<BatterySipper> getUsageList()
  {
    return mUsageList;
  }
  
  public boolean isTypeService(BatterySipper paramBatterySipper)
  {
    String[] arrayOfString = mPackageManager.getPackagesForUid(paramBatterySipper.getUid());
    if (arrayOfString == null) {
      return false;
    }
    int i = arrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      paramBatterySipper = arrayOfString[j];
      if (ArrayUtils.contains(mServicepackageArray, paramBatterySipper)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean isTypeSystem(BatterySipper paramBatterySipper)
  {
    int i;
    if (uidObj == null) {
      i = -1;
    } else {
      i = paramBatterySipper.getUid();
    }
    mPackages = mPackageManager.getPackagesForUid(i);
    if ((i >= 0) && (i < 10000)) {
      return true;
    }
    if (mPackages != null) {
      for (paramBatterySipper : mPackages) {
        if (ArrayUtils.contains(mSystemPackageArray, paramBatterySipper)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public void refreshStats(int paramInt1, int paramInt2)
  {
    SparseArray localSparseArray = new SparseArray(1);
    localSparseArray.put(paramInt2, new UserHandle(paramInt2));
    refreshStats(paramInt1, localSparseArray);
  }
  
  public void refreshStats(int paramInt, SparseArray<UserHandle> paramSparseArray)
  {
    refreshStats(paramInt, paramSparseArray, SystemClock.elapsedRealtime() * 1000L, SystemClock.uptimeMillis() * 1000L);
  }
  
  public void refreshStats(int paramInt, SparseArray<UserHandle> paramSparseArray, long paramLong1, long paramLong2)
  {
    BatteryStatsHelper localBatteryStatsHelper = this;
    getStats();
    mMaxPower = 0.0D;
    mMaxRealPower = 0.0D;
    mComputedPower = 0.0D;
    mTotalPower = 0.0D;
    mUsageList.clear();
    mWifiSippers.clear();
    mBluetoothSippers.clear();
    mUserSippers.clear();
    mMobilemsppList.clear();
    if (mStats == null) {
      return;
    }
    if (mCpuPowerCalculator == null) {
      mCpuPowerCalculator = new CpuPowerCalculator(mPowerProfile);
    }
    mCpuPowerCalculator.reset();
    if (mMemoryPowerCalculator == null) {
      mMemoryPowerCalculator = new MemoryPowerCalculator(mPowerProfile);
    }
    mMemoryPowerCalculator.reset();
    if (mWakelockPowerCalculator == null) {
      mWakelockPowerCalculator = new WakelockPowerCalculator(mPowerProfile);
    }
    mWakelockPowerCalculator.reset();
    if (mMobileRadioPowerCalculator == null) {
      mMobileRadioPowerCalculator = new MobileRadioPowerCalculator(mPowerProfile, mStats);
    }
    mMobileRadioPowerCalculator.reset(mStats);
    boolean bool = checkHasWifiPowerReporting(mStats, mPowerProfile);
    Object localObject;
    if ((mWifiPowerCalculator == null) || (bool != mHasWifiPowerReporting))
    {
      if (bool) {
        localObject = new WifiPowerCalculator(mPowerProfile);
      } else {
        localObject = new WifiPowerEstimator(mPowerProfile);
      }
      mWifiPowerCalculator = ((PowerCalculator)localObject);
      mHasWifiPowerReporting = bool;
    }
    mWifiPowerCalculator.reset();
    bool = checkHasBluetoothPowerReporting(mStats, mPowerProfile);
    if ((mBluetoothPowerCalculator == null) || (bool != mHasBluetoothPowerReporting))
    {
      mBluetoothPowerCalculator = new BluetoothPowerCalculator(mPowerProfile);
      mHasBluetoothPowerReporting = bool;
    }
    mBluetoothPowerCalculator.reset();
    mSensorPowerCalculator = new SensorPowerCalculator(mPowerProfile, (SensorManager)mContext.getSystemService("sensor"), mStats, paramLong1, paramInt);
    mSensorPowerCalculator.reset();
    if (mCameraPowerCalculator == null) {
      mCameraPowerCalculator = new CameraPowerCalculator(mPowerProfile);
    }
    mCameraPowerCalculator.reset();
    if (mFlashlightPowerCalculator == null) {
      mFlashlightPowerCalculator = new FlashlightPowerCalculator(mPowerProfile);
    }
    mFlashlightPowerCalculator.reset();
    if (mMediaPowerCalculator == null) {
      mMediaPowerCalculator = new MediaPowerCalculator(mPowerProfile);
    }
    mMediaPowerCalculator.reset();
    mStatsType = paramInt;
    mRawUptimeUs = paramLong2;
    mRawRealtimeUs = paramLong1;
    mBatteryUptimeUs = mStats.getBatteryUptime(paramLong2);
    mBatteryRealtimeUs = mStats.getBatteryRealtime(paramLong1);
    mTypeBatteryUptimeUs = mStats.computeBatteryUptime(paramLong2, mStatsType);
    mTypeBatteryRealtimeUs = mStats.computeBatteryRealtime(paramLong1, mStatsType);
    mBatteryTimeRemainingUs = mStats.computeBatteryTimeRemaining(paramLong1);
    mChargeTimeRemainingUs = mStats.computeChargeTimeRemaining(paramLong1);
    mMinDrainedPower = (mStats.getLowDischargeAmountSinceCharge() * mPowerProfile.getBatteryCapacity() / 100.0D);
    mMaxDrainedPower = (mStats.getHighDischargeAmountSinceCharge() * mPowerProfile.getBatteryCapacity() / 100.0D);
    localBatteryStatsHelper.processAppUsage(paramSparseArray);
    int i = 0;
    for (paramInt = 0; paramInt < mUsageList.size(); paramInt++)
    {
      paramSparseArray = (BatterySipper)mUsageList.get(paramInt);
      paramSparseArray.computeMobilemspp();
      if (mobilemspp != 0.0D) {
        mMobilemsppList.add(paramSparseArray);
      }
    }
    int j;
    for (paramInt = 0; paramInt < mUserSippers.size(); paramInt++)
    {
      paramSparseArray = (List)mUserSippers.valueAt(paramInt);
      for (j = 0; j < paramSparseArray.size(); j++)
      {
        localObject = (BatterySipper)paramSparseArray.get(j);
        ((BatterySipper)localObject).computeMobilemspp();
        if (mobilemspp != 0.0D) {
          mMobilemsppList.add(localObject);
        }
      }
    }
    Collections.sort(mMobilemsppList, new Comparator()
    {
      public int compare(BatterySipper paramAnonymousBatterySipper1, BatterySipper paramAnonymousBatterySipper2)
      {
        return Double.compare(mobilemspp, mobilemspp);
      }
    });
    processMiscUsage();
    Collections.sort(mUsageList);
    if (!mUsageList.isEmpty())
    {
      d1 = mUsageList.get(0)).totalPowerMah;
      mMaxPower = d1;
      mMaxRealPower = d1;
      j = mUsageList.size();
      for (paramInt = i; paramInt < j; paramInt++) {
        mComputedPower += mUsageList.get(paramInt)).totalPowerMah;
      }
    }
    mTotalPower = mComputedPower;
    if (mStats.getLowDischargeAmountSinceCharge() > 1) {
      if (mMinDrainedPower > mComputedPower)
      {
        d1 = mMinDrainedPower - mComputedPower;
        mTotalPower = mMinDrainedPower;
        paramSparseArray = new BatterySipper(BatterySipper.DrainType.UNACCOUNTED, null, d1);
        j = Collections.binarySearch(mUsageList, paramSparseArray);
        paramInt = j;
        if (j < 0) {
          paramInt = -(j + 1);
        }
        mUsageList.add(paramInt, paramSparseArray);
        mMaxPower = Math.max(mMaxPower, d1);
      }
      else if (mMaxDrainedPower < mComputedPower)
      {
        d1 = mComputedPower - mMaxDrainedPower;
        paramSparseArray = new BatterySipper(BatterySipper.DrainType.OVERCOUNTED, null, d1);
        j = Collections.binarySearch(mUsageList, paramSparseArray);
        paramInt = j;
        if (j < 0) {
          paramInt = -(j + 1);
        }
        mUsageList.add(paramInt, paramSparseArray);
        mMaxPower = Math.max(mMaxPower, d1);
      }
    }
    double d1 = localBatteryStatsHelper.removeHiddenBatterySippers(mUsageList);
    double d2 = getTotalPower() - d1;
    if (Math.abs(d2) > 0.001D)
    {
      paramInt = 0;
      j = mUsageList.size();
      while (paramInt < j)
      {
        paramSparseArray = (BatterySipper)mUsageList.get(paramInt);
        if (!shouldHide)
        {
          proportionalSmearMah = ((totalPowerMah + screenPowerMah) / d2 * d1);
          paramSparseArray.sumPower();
        }
        paramInt++;
      }
    }
  }
  
  public void refreshStats(int paramInt, List<UserHandle> paramList)
  {
    int i = paramList.size();
    SparseArray localSparseArray = new SparseArray(i);
    for (int j = 0; j < i; j++)
    {
      UserHandle localUserHandle = (UserHandle)paramList.get(j);
      localSparseArray.put(localUserHandle.getIdentifier(), localUserHandle);
    }
    refreshStats(paramInt, localSparseArray);
  }
  
  public double removeHiddenBatterySippers(List<BatterySipper> paramList)
  {
    double d1 = 0.0D;
    Object localObject = null;
    int i = paramList.size() - 1;
    while (i >= 0)
    {
      BatterySipper localBatterySipper = (BatterySipper)paramList.get(i);
      shouldHide = shouldHideSipper(localBatterySipper);
      double d2 = d1;
      if (shouldHide)
      {
        d2 = d1;
        if (drainType != BatterySipper.DrainType.OVERCOUNTED)
        {
          d2 = d1;
          if (drainType != BatterySipper.DrainType.SCREEN)
          {
            d2 = d1;
            if (drainType != BatterySipper.DrainType.AMBIENT_DISPLAY)
            {
              d2 = d1;
              if (drainType != BatterySipper.DrainType.UNACCOUNTED)
              {
                d2 = d1;
                if (drainType != BatterySipper.DrainType.BLUETOOTH)
                {
                  d2 = d1;
                  if (drainType != BatterySipper.DrainType.WIFI)
                  {
                    d2 = d1;
                    if (drainType != BatterySipper.DrainType.IDLE) {
                      d2 = d1 + totalPowerMah;
                    }
                  }
                }
              }
            }
          }
        }
      }
      if (drainType == BatterySipper.DrainType.SCREEN) {
        localObject = localBatterySipper;
      }
      i--;
      d1 = d2;
    }
    smearScreenBatterySipper(paramList, localObject);
    return d1;
  }
  
  @VisibleForTesting
  public void setPackageManager(PackageManager paramPackageManager)
  {
    mPackageManager = paramPackageManager;
  }
  
  @VisibleForTesting
  public void setServicePackageArray(String[] paramArrayOfString)
  {
    mServicepackageArray = paramArrayOfString;
  }
  
  @VisibleForTesting
  public void setSystemPackageArray(String[] paramArrayOfString)
  {
    mSystemPackageArray = paramArrayOfString;
  }
  
  public boolean shouldHideSipper(BatterySipper paramBatterySipper)
  {
    BatterySipper.DrainType localDrainType = drainType;
    boolean bool;
    if ((localDrainType != BatterySipper.DrainType.IDLE) && (localDrainType != BatterySipper.DrainType.CELL) && (localDrainType != BatterySipper.DrainType.SCREEN) && (localDrainType != BatterySipper.DrainType.AMBIENT_DISPLAY) && (localDrainType != BatterySipper.DrainType.UNACCOUNTED) && (localDrainType != BatterySipper.DrainType.OVERCOUNTED) && (!isTypeService(paramBatterySipper)) && (!isTypeSystem(paramBatterySipper))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void smearScreenBatterySipper(List<BatterySipper> paramList, BatterySipper paramBatterySipper)
  {
    long l1 = 0L;
    SparseLongArray localSparseLongArray = new SparseLongArray();
    int i = 0;
    int j = paramList.size();
    while (i < j)
    {
      BatteryStats.Uid localUid = getuidObj;
      long l2 = l1;
      if (localUid != null)
      {
        l2 = getProcessForegroundTimeMs(localUid, 0);
        localSparseLongArray.put(localUid.getUid(), l2);
        l2 = l1 + l2;
      }
      i++;
      l1 = l2;
    }
    if ((paramBatterySipper != null) && (l1 >= 600000L))
    {
      double d = totalPowerMah;
      i = 0;
      j = paramList.size();
      while (i < j)
      {
        paramBatterySipper = (BatterySipper)paramList.get(i);
        screenPowerMah = (localSparseLongArray.get(paramBatterySipper.getUid(), 0L) * d / l1);
        i++;
      }
    }
  }
  
  public void storeState()
  {
    sStatsXfer = mStats;
    sBatteryBroadcastXfer = mBatteryBroadcast;
  }
  
  /* Error */
  public void storeStatsHistoryInFile(String paramString)
  {
    // Byte code:
    //   0: getstatic 92	com/android/internal/os/BatteryStatsHelper:sFileXfer	Landroid/util/ArrayMap;
    //   3: astore_2
    //   4: aload_2
    //   5: monitorenter
    //   6: aload_0
    //   7: getfield 135	com/android/internal/os/BatteryStatsHelper:mContext	Landroid/content/Context;
    //   10: aload_1
    //   11: invokestatic 376	com/android/internal/os/BatteryStatsHelper:makeFilePath	(Landroid/content/Context;Ljava/lang/String;)Ljava/io/File;
    //   14: astore_3
    //   15: getstatic 92	com/android/internal/os/BatteryStatsHelper:sFileXfer	Landroid/util/ArrayMap;
    //   18: aload_3
    //   19: aload_0
    //   20: invokevirtual 761	com/android/internal/os/BatteryStatsHelper:getStats	()Landroid/os/BatteryStats;
    //   23: invokevirtual 952	android/util/ArrayMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   26: pop
    //   27: aconst_null
    //   28: astore 4
    //   30: aconst_null
    //   31: astore 5
    //   33: aload 5
    //   35: astore_1
    //   36: aload 4
    //   38: astore 6
    //   40: new 954	java/io/FileOutputStream
    //   43: astore 7
    //   45: aload 5
    //   47: astore_1
    //   48: aload 4
    //   50: astore 6
    //   52: aload 7
    //   54: aload_3
    //   55: invokespecial 955	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   58: aload 7
    //   60: astore 5
    //   62: aload 5
    //   64: astore_1
    //   65: aload 5
    //   67: astore 6
    //   69: invokestatic 422	android/os/Parcel:obtain	()Landroid/os/Parcel;
    //   72: astore 7
    //   74: aload 5
    //   76: astore_1
    //   77: aload 5
    //   79: astore 6
    //   81: aload_0
    //   82: invokevirtual 761	com/android/internal/os/BatteryStatsHelper:getStats	()Landroid/os/BatteryStats;
    //   85: aload 7
    //   87: iconst_0
    //   88: invokevirtual 959	android/os/BatteryStats:writeToParcelWithoutUids	(Landroid/os/Parcel;I)V
    //   91: aload 5
    //   93: astore_1
    //   94: aload 5
    //   96: astore 6
    //   98: aload 5
    //   100: aload 7
    //   102: invokevirtual 963	android/os/Parcel:marshall	()[B
    //   105: invokevirtual 967	java/io/FileOutputStream:write	([B)V
    //   108: aload 5
    //   110: invokevirtual 968	java/io/FileOutputStream:close	()V
    //   113: goto +35 -> 148
    //   116: astore 6
    //   118: goto +40 -> 158
    //   121: astore 5
    //   123: aload 6
    //   125: astore_1
    //   126: getstatic 85	com/android/internal/os/BatteryStatsHelper:TAG	Ljava/lang/String;
    //   129: ldc_w 970
    //   132: aload 5
    //   134: invokestatic 459	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   137: pop
    //   138: aload 6
    //   140: ifnull +15 -> 155
    //   143: aload 6
    //   145: invokevirtual 968	java/io/FileOutputStream:close	()V
    //   148: goto +7 -> 155
    //   151: astore_1
    //   152: goto -4 -> 148
    //   155: aload_2
    //   156: monitorexit
    //   157: return
    //   158: aload_1
    //   159: ifnull +11 -> 170
    //   162: aload_1
    //   163: invokevirtual 968	java/io/FileOutputStream:close	()V
    //   166: goto +4 -> 170
    //   169: astore_1
    //   170: aload 6
    //   172: athrow
    //   173: astore_1
    //   174: aload_2
    //   175: monitorexit
    //   176: aload_1
    //   177: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	178	0	this	BatteryStatsHelper
    //   0	178	1	paramString	String
    //   3	172	2	localArrayMap	ArrayMap
    //   14	41	3	localFile	File
    //   28	21	4	localObject1	Object
    //   31	78	5	localObject2	Object
    //   121	12	5	localIOException	IOException
    //   38	59	6	localObject3	Object
    //   116	55	6	localObject4	Object
    //   43	58	7	localObject5	Object
    // Exception table:
    //   from	to	target	type
    //   40	45	116	finally
    //   52	58	116	finally
    //   69	74	116	finally
    //   81	91	116	finally
    //   98	108	116	finally
    //   126	138	116	finally
    //   40	45	121	java/io/IOException
    //   52	58	121	java/io/IOException
    //   69	74	121	java/io/IOException
    //   81	91	121	java/io/IOException
    //   98	108	121	java/io/IOException
    //   108	113	151	java/io/IOException
    //   143	148	151	java/io/IOException
    //   162	166	169	java/io/IOException
    //   6	27	173	finally
    //   108	113	173	finally
    //   143	148	173	finally
    //   155	157	173	finally
    //   162	166	173	finally
    //   170	173	173	finally
    //   174	176	173	finally
  }
}
