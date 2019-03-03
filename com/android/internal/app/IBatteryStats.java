package com.android.internal.app;

import android.bluetooth.BluetoothActivityEnergyInfo;
import android.content.Intent;
import android.net.wifi.WifiActivityEnergyInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.WorkSource;
import android.os.connectivity.CellularBatteryStats;
import android.os.connectivity.GpsBatteryStats;
import android.os.connectivity.WifiBatteryStats;
import android.os.health.HealthStatsParceler;
import android.telephony.ModemActivityInfo;
import android.telephony.SignalStrength;

public abstract interface IBatteryStats
  extends IInterface
{
  public abstract long computeBatteryTimeRemaining()
    throws RemoteException;
  
  public abstract long computeChargeTimeRemaining()
    throws RemoteException;
  
  public abstract long getAwakeTimeBattery()
    throws RemoteException;
  
  public abstract long getAwakeTimePlugged()
    throws RemoteException;
  
  public abstract Intent getBatteryInfo()
    throws RemoteException;
  
  public abstract CellularBatteryStats getCellularBatteryStats()
    throws RemoteException;
  
  public abstract GpsBatteryStats getGpsBatteryStats()
    throws RemoteException;
  
  public abstract byte[] getStatistics()
    throws RemoteException;
  
  public abstract ParcelFileDescriptor getStatisticsStream()
    throws RemoteException;
  
  public abstract WifiBatteryStats getWifiBatteryStats()
    throws RemoteException;
  
  public abstract boolean isCharging()
    throws RemoteException;
  
  public abstract void noteBleScanResults(WorkSource paramWorkSource, int paramInt)
    throws RemoteException;
  
  public abstract void noteBleScanStarted(WorkSource paramWorkSource, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void noteBleScanStopped(WorkSource paramWorkSource, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo paramBluetoothActivityEnergyInfo)
    throws RemoteException;
  
  public abstract void noteChangeWakelockFromSource(WorkSource paramWorkSource1, int paramInt1, String paramString1, String paramString2, int paramInt2, WorkSource paramWorkSource2, int paramInt3, String paramString3, String paramString4, int paramInt4, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void noteConnectivityChanged(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void noteDeviceIdleMode(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void noteEvent(int paramInt1, String paramString, int paramInt2)
    throws RemoteException;
  
  public abstract void noteFlashlightOff(int paramInt)
    throws RemoteException;
  
  public abstract void noteFlashlightOn(int paramInt)
    throws RemoteException;
  
  public abstract void noteFullWifiLockAcquired(int paramInt)
    throws RemoteException;
  
  public abstract void noteFullWifiLockAcquiredFromSource(WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteFullWifiLockReleased(int paramInt)
    throws RemoteException;
  
  public abstract void noteFullWifiLockReleasedFromSource(WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteGpsChanged(WorkSource paramWorkSource1, WorkSource paramWorkSource2)
    throws RemoteException;
  
  public abstract void noteGpsSignalQuality(int paramInt)
    throws RemoteException;
  
  public abstract void noteInteractive(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void noteJobFinish(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void noteJobStart(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void noteLongPartialWakelockFinish(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void noteLongPartialWakelockFinishFromSource(String paramString1, String paramString2, WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteLongPartialWakelockStart(String paramString1, String paramString2, int paramInt)
    throws RemoteException;
  
  public abstract void noteLongPartialWakelockStartFromSource(String paramString1, String paramString2, WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteMobileRadioPowerState(int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public abstract void noteModemControllerActivity(ModemActivityInfo paramModemActivityInfo)
    throws RemoteException;
  
  public abstract void noteNetworkInterfaceType(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void noteNetworkStatsEnabled()
    throws RemoteException;
  
  public abstract void notePhoneDataConnectionState(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void notePhoneOff()
    throws RemoteException;
  
  public abstract void notePhoneOn()
    throws RemoteException;
  
  public abstract void notePhoneSignalStrength(SignalStrength paramSignalStrength)
    throws RemoteException;
  
  public abstract void notePhoneState(int paramInt)
    throws RemoteException;
  
  public abstract void noteResetAudio()
    throws RemoteException;
  
  public abstract void noteResetBleScan()
    throws RemoteException;
  
  public abstract void noteResetCamera()
    throws RemoteException;
  
  public abstract void noteResetFlashlight()
    throws RemoteException;
  
  public abstract void noteResetVideo()
    throws RemoteException;
  
  public abstract void noteScreenBrightness(int paramInt)
    throws RemoteException;
  
  public abstract void noteScreenState(int paramInt)
    throws RemoteException;
  
  public abstract void noteStartAudio(int paramInt)
    throws RemoteException;
  
  public abstract void noteStartCamera(int paramInt)
    throws RemoteException;
  
  public abstract void noteStartSensor(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void noteStartVideo(int paramInt)
    throws RemoteException;
  
  public abstract void noteStartWakelock(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void noteStartWakelockFromSource(WorkSource paramWorkSource, int paramInt1, String paramString1, String paramString2, int paramInt2, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void noteStopAudio(int paramInt)
    throws RemoteException;
  
  public abstract void noteStopCamera(int paramInt)
    throws RemoteException;
  
  public abstract void noteStopSensor(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void noteStopVideo(int paramInt)
    throws RemoteException;
  
  public abstract void noteStopWakelock(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3)
    throws RemoteException;
  
  public abstract void noteStopWakelockFromSource(WorkSource paramWorkSource, int paramInt1, String paramString1, String paramString2, int paramInt2)
    throws RemoteException;
  
  public abstract void noteSyncFinish(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void noteSyncStart(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void noteUserActivity(int paramInt1, int paramInt2)
    throws RemoteException;
  
  public abstract void noteVibratorOff(int paramInt)
    throws RemoteException;
  
  public abstract void noteVibratorOn(int paramInt, long paramLong)
    throws RemoteException;
  
  public abstract void noteWakeUp(String paramString, int paramInt)
    throws RemoteException;
  
  public abstract void noteWifiBatchedScanStartedFromSource(WorkSource paramWorkSource, int paramInt)
    throws RemoteException;
  
  public abstract void noteWifiBatchedScanStoppedFromSource(WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteWifiControllerActivity(WifiActivityEnergyInfo paramWifiActivityEnergyInfo)
    throws RemoteException;
  
  public abstract void noteWifiMulticastDisabled(int paramInt)
    throws RemoteException;
  
  public abstract void noteWifiMulticastEnabled(int paramInt)
    throws RemoteException;
  
  public abstract void noteWifiOff()
    throws RemoteException;
  
  public abstract void noteWifiOn()
    throws RemoteException;
  
  public abstract void noteWifiRadioPowerState(int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;
  
  public abstract void noteWifiRssiChanged(int paramInt)
    throws RemoteException;
  
  public abstract void noteWifiRunning(WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteWifiRunningChanged(WorkSource paramWorkSource1, WorkSource paramWorkSource2)
    throws RemoteException;
  
  public abstract void noteWifiScanStarted(int paramInt)
    throws RemoteException;
  
  public abstract void noteWifiScanStartedFromSource(WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteWifiScanStopped(int paramInt)
    throws RemoteException;
  
  public abstract void noteWifiScanStoppedFromSource(WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteWifiState(int paramInt, String paramString)
    throws RemoteException;
  
  public abstract void noteWifiStopped(WorkSource paramWorkSource)
    throws RemoteException;
  
  public abstract void noteWifiSupplicantStateChanged(int paramInt, boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setBatteryState(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
    throws RemoteException;
  
  public abstract HealthStatsParceler takeUidSnapshot(int paramInt)
    throws RemoteException;
  
  public abstract HealthStatsParceler[] takeUidSnapshots(int[] paramArrayOfInt)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IBatteryStats
  {
    private static final String DESCRIPTOR = "com.android.internal.app.IBatteryStats";
    static final int TRANSACTION_computeBatteryTimeRemaining = 18;
    static final int TRANSACTION_computeChargeTimeRemaining = 19;
    static final int TRANSACTION_getAwakeTimeBattery = 75;
    static final int TRANSACTION_getAwakeTimePlugged = 76;
    static final int TRANSACTION_getBatteryInfo = 89;
    static final int TRANSACTION_getCellularBatteryStats = 81;
    static final int TRANSACTION_getGpsBatteryStats = 83;
    static final int TRANSACTION_getStatistics = 15;
    static final int TRANSACTION_getStatisticsStream = 16;
    static final int TRANSACTION_getWifiBatteryStats = 82;
    static final int TRANSACTION_isCharging = 17;
    static final int TRANSACTION_noteBleScanResults = 80;
    static final int TRANSACTION_noteBleScanStarted = 77;
    static final int TRANSACTION_noteBleScanStopped = 78;
    static final int TRANSACTION_noteBluetoothControllerActivity = 86;
    static final int TRANSACTION_noteChangeWakelockFromSource = 28;
    static final int TRANSACTION_noteConnectivityChanged = 43;
    static final int TRANSACTION_noteDeviceIdleMode = 73;
    static final int TRANSACTION_noteEvent = 20;
    static final int TRANSACTION_noteFlashlightOff = 10;
    static final int TRANSACTION_noteFlashlightOn = 9;
    static final int TRANSACTION_noteFullWifiLockAcquired = 58;
    static final int TRANSACTION_noteFullWifiLockAcquiredFromSource = 64;
    static final int TRANSACTION_noteFullWifiLockReleased = 59;
    static final int TRANSACTION_noteFullWifiLockReleasedFromSource = 65;
    static final int TRANSACTION_noteGpsChanged = 36;
    static final int TRANSACTION_noteGpsSignalQuality = 37;
    static final int TRANSACTION_noteInteractive = 42;
    static final int TRANSACTION_noteJobFinish = 24;
    static final int TRANSACTION_noteJobStart = 23;
    static final int TRANSACTION_noteLongPartialWakelockFinish = 32;
    static final int TRANSACTION_noteLongPartialWakelockFinishFromSource = 33;
    static final int TRANSACTION_noteLongPartialWakelockStart = 30;
    static final int TRANSACTION_noteLongPartialWakelockStartFromSource = 31;
    static final int TRANSACTION_noteMobileRadioPowerState = 44;
    static final int TRANSACTION_noteModemControllerActivity = 87;
    static final int TRANSACTION_noteNetworkInterfaceType = 71;
    static final int TRANSACTION_noteNetworkStatsEnabled = 72;
    static final int TRANSACTION_notePhoneDataConnectionState = 48;
    static final int TRANSACTION_notePhoneOff = 46;
    static final int TRANSACTION_notePhoneOn = 45;
    static final int TRANSACTION_notePhoneSignalStrength = 47;
    static final int TRANSACTION_notePhoneState = 49;
    static final int TRANSACTION_noteResetAudio = 8;
    static final int TRANSACTION_noteResetBleScan = 79;
    static final int TRANSACTION_noteResetCamera = 13;
    static final int TRANSACTION_noteResetFlashlight = 14;
    static final int TRANSACTION_noteResetVideo = 7;
    static final int TRANSACTION_noteScreenBrightness = 39;
    static final int TRANSACTION_noteScreenState = 38;
    static final int TRANSACTION_noteStartAudio = 5;
    static final int TRANSACTION_noteStartCamera = 11;
    static final int TRANSACTION_noteStartSensor = 1;
    static final int TRANSACTION_noteStartVideo = 3;
    static final int TRANSACTION_noteStartWakelock = 25;
    static final int TRANSACTION_noteStartWakelockFromSource = 27;
    static final int TRANSACTION_noteStopAudio = 6;
    static final int TRANSACTION_noteStopCamera = 12;
    static final int TRANSACTION_noteStopSensor = 2;
    static final int TRANSACTION_noteStopVideo = 4;
    static final int TRANSACTION_noteStopWakelock = 26;
    static final int TRANSACTION_noteStopWakelockFromSource = 29;
    static final int TRANSACTION_noteSyncFinish = 22;
    static final int TRANSACTION_noteSyncStart = 21;
    static final int TRANSACTION_noteUserActivity = 40;
    static final int TRANSACTION_noteVibratorOff = 35;
    static final int TRANSACTION_noteVibratorOn = 34;
    static final int TRANSACTION_noteWakeUp = 41;
    static final int TRANSACTION_noteWifiBatchedScanStartedFromSource = 68;
    static final int TRANSACTION_noteWifiBatchedScanStoppedFromSource = 69;
    static final int TRANSACTION_noteWifiControllerActivity = 88;
    static final int TRANSACTION_noteWifiMulticastDisabled = 63;
    static final int TRANSACTION_noteWifiMulticastEnabled = 62;
    static final int TRANSACTION_noteWifiOff = 51;
    static final int TRANSACTION_noteWifiOn = 50;
    static final int TRANSACTION_noteWifiRadioPowerState = 70;
    static final int TRANSACTION_noteWifiRssiChanged = 57;
    static final int TRANSACTION_noteWifiRunning = 52;
    static final int TRANSACTION_noteWifiRunningChanged = 53;
    static final int TRANSACTION_noteWifiScanStarted = 60;
    static final int TRANSACTION_noteWifiScanStartedFromSource = 66;
    static final int TRANSACTION_noteWifiScanStopped = 61;
    static final int TRANSACTION_noteWifiScanStoppedFromSource = 67;
    static final int TRANSACTION_noteWifiState = 55;
    static final int TRANSACTION_noteWifiStopped = 54;
    static final int TRANSACTION_noteWifiSupplicantStateChanged = 56;
    static final int TRANSACTION_setBatteryState = 74;
    static final int TRANSACTION_takeUidSnapshot = 84;
    static final int TRANSACTION_takeUidSnapshots = 85;
    
    public Stub()
    {
      attachInterface(this, "com.android.internal.app.IBatteryStats");
    }
    
    public static IBatteryStats asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      }
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.android.internal.app.IBatteryStats");
      if ((localIInterface != null) && ((localIInterface instanceof IBatteryStats))) {
        return (IBatteryStats)localIInterface;
      }
      return new Proxy(paramIBinder);
    }
    
    public IBinder asBinder()
    {
      return this;
    }
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      if (paramInt1 != 1598968902)
      {
        boolean bool1 = false;
        boolean bool2 = false;
        boolean bool3 = false;
        boolean bool4 = false;
        boolean bool5 = false;
        Object localObject1 = null;
        Object localObject2 = null;
        Object localObject3 = null;
        Object localObject4 = null;
        Object localObject5 = null;
        Object localObject6 = null;
        Object localObject7 = null;
        Object localObject8 = null;
        Object localObject9 = null;
        Object localObject10 = null;
        Object localObject11 = null;
        Object localObject12 = null;
        String str1 = null;
        Object localObject13 = null;
        Object localObject14 = null;
        String str2 = null;
        Object localObject15 = null;
        String str3 = null;
        Object localObject16 = null;
        String str4 = null;
        long l;
        int j;
        switch (paramInt1)
        {
        default: 
          return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
        case 89: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramParcel1 = getBatteryInfo();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 88: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WifiActivityEnergyInfo)WifiActivityEnergyInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str4;
          }
          noteWifiControllerActivity(paramParcel1);
          return true;
        case 87: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (ModemActivityInfo)ModemActivityInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject1;
          }
          noteModemControllerActivity(paramParcel1);
          return true;
        case 86: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (BluetoothActivityEnergyInfo)BluetoothActivityEnergyInfo.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject2;
          }
          noteBluetoothControllerActivity(paramParcel1);
          return true;
        case 85: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramParcel1 = takeUidSnapshots(paramParcel1.createIntArray());
          paramParcel2.writeNoException();
          paramParcel2.writeTypedArray(paramParcel1, 1);
          return true;
        case 84: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramParcel1 = takeUidSnapshot(paramParcel1.readInt());
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 83: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramParcel1 = getGpsBatteryStats();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 82: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramParcel1 = getWifiBatteryStats();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 81: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramParcel1 = getCellularBatteryStats();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 80: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = localObject3;
          }
          noteBleScanResults((WorkSource)localObject15, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 79: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteResetBleScan();
          paramParcel2.writeNoException();
          return true;
        case 78: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = localObject4;
          }
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          noteBleScanStopped((WorkSource)localObject15, bool5);
          paramParcel2.writeNoException();
          return true;
        case 77: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = localObject5;
          }
          bool5 = bool1;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          noteBleScanStarted((WorkSource)localObject15, bool5);
          paramParcel2.writeNoException();
          return true;
        case 76: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          l = getAwakeTimePlugged();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 75: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          l = getAwakeTimeBattery();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 74: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          setBatteryState(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 73: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteDeviceIdleMode(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 72: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteNetworkStatsEnabled();
          paramParcel2.writeNoException();
          return true;
        case 71: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteNetworkInterfaceType(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 70: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiRadioPowerState(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 69: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject6;
          }
          noteWifiBatchedScanStoppedFromSource(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 68: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = localObject7;
          }
          noteWifiBatchedScanStartedFromSource((WorkSource)localObject15, paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 67: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject8;
          }
          noteWifiScanStoppedFromSource(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 66: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject9;
          }
          noteWifiScanStartedFromSource(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 65: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject10;
          }
          noteFullWifiLockReleasedFromSource(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 64: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject11;
          }
          noteFullWifiLockAcquiredFromSource(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 63: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiMulticastDisabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 62: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiMulticastEnabled(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 61: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiScanStopped(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 60: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiScanStarted(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 59: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteFullWifiLockReleased(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 58: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteFullWifiLockAcquired(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 57: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiRssiChanged(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 56: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramInt1 = paramParcel1.readInt();
          bool5 = bool2;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          noteWifiSupplicantStateChanged(paramInt1, bool5);
          paramParcel2.writeNoException();
          return true;
        case 55: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiState(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 54: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject12;
          }
          noteWifiStopped(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 53: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str1;
          }
          noteWifiRunningChanged((WorkSource)localObject15, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 52: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject13;
          }
          noteWifiRunning(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 51: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiOff();
          paramParcel2.writeNoException();
          return true;
        case 50: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWifiOn();
          paramParcel2.writeNoException();
          return true;
        case 49: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          notePhoneState(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 48: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramInt1 = paramParcel1.readInt();
          bool5 = bool3;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          notePhoneDataConnectionState(paramInt1, bool5);
          paramParcel2.writeNoException();
          return true;
        case 47: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (SignalStrength)SignalStrength.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = localObject14;
          }
          notePhoneSignalStrength(paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 46: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          notePhoneOff();
          paramParcel2.writeNoException();
          return true;
        case 45: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          notePhoneOn();
          paramParcel2.writeNoException();
          return true;
        case 44: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteMobileRadioPowerState(paramParcel1.readInt(), paramParcel1.readLong(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 43: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteConnectivityChanged(paramParcel1.readInt(), paramParcel1.readString());
          paramParcel2.writeNoException();
          return true;
        case 42: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          bool5 = bool4;
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          }
          noteInteractive(bool5);
          paramParcel2.writeNoException();
          return true;
        case 41: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteWakeUp(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 40: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteUserActivity(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 39: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteScreenBrightness(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 38: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteScreenState(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 37: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteGpsSignalQuality(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 36: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = null;
          }
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str2;
          }
          noteGpsChanged((WorkSource)localObject15, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 35: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteVibratorOff(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 34: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteVibratorOn(paramParcel1.readInt(), paramParcel1.readLong());
          paramParcel2.writeNoException();
          return true;
        case 33: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          localObject16 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = (Parcel)localObject15;
          }
          noteLongPartialWakelockFinishFromSource((String)localObject16, str1, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 32: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteLongPartialWakelockFinish(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 31: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          localObject15 = paramParcel1.readString();
          localObject16 = paramParcel1.readString();
          if (paramParcel1.readInt() != 0) {
            paramParcel1 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            paramParcel1 = str3;
          }
          noteLongPartialWakelockStartFromSource((String)localObject15, (String)localObject16, paramParcel1);
          paramParcel2.writeNoException();
          return true;
        case 30: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteLongPartialWakelockStart(paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 29: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = null;
          }
          noteStopWakelockFromSource((WorkSource)localObject15, paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 28: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = null;
          }
          int i = paramParcel1.readInt();
          str1 = paramParcel1.readString();
          str2 = paramParcel1.readString();
          j = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            localObject16 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          }
          for (;;)
          {
            break;
          }
          paramInt1 = paramParcel1.readInt();
          str4 = paramParcel1.readString();
          str3 = paramParcel1.readString();
          paramInt2 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          noteChangeWakelockFromSource((WorkSource)localObject15, i, str1, str2, j, (WorkSource)localObject16, paramInt1, str4, str3, paramInt2, bool5);
          paramParcel2.writeNoException();
          return true;
        case 27: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          if (paramParcel1.readInt() != 0) {
            localObject15 = (WorkSource)WorkSource.CREATOR.createFromParcel(paramParcel1);
          } else {
            localObject15 = null;
          }
          paramInt2 = paramParcel1.readInt();
          localObject16 = paramParcel1.readString();
          str1 = paramParcel1.readString();
          paramInt1 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          noteStartWakelockFromSource((WorkSource)localObject15, paramInt2, (String)localObject16, str1, paramInt1, bool5);
          paramParcel2.writeNoException();
          return true;
        case 26: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteStopWakelock(paramParcel1.readInt(), paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 25: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramInt2 = paramParcel1.readInt();
          paramInt1 = paramParcel1.readInt();
          localObject15 = paramParcel1.readString();
          localObject16 = paramParcel1.readString();
          j = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0) {
            bool5 = true;
          } else {
            bool5 = false;
          }
          noteStartWakelock(paramInt2, paramInt1, (String)localObject15, (String)localObject16, j, bool5);
          paramParcel2.writeNoException();
          return true;
        case 24: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteJobFinish(paramParcel1.readString(), paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 23: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteJobStart(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 22: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteSyncFinish(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 21: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteSyncStart(paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 20: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteEvent(paramParcel1.readInt(), paramParcel1.readString(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 19: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          l = computeChargeTimeRemaining();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 18: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          l = computeBatteryTimeRemaining();
          paramParcel2.writeNoException();
          paramParcel2.writeLong(l);
          return true;
        case 17: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramInt1 = isCharging();
          paramParcel2.writeNoException();
          paramParcel2.writeInt(paramInt1);
          return true;
        case 16: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramParcel1 = getStatisticsStream();
          paramParcel2.writeNoException();
          if (paramParcel1 != null)
          {
            paramParcel2.writeInt(1);
            paramParcel1.writeToParcel(paramParcel2, 1);
          }
          else
          {
            paramParcel2.writeInt(0);
          }
          return true;
        case 15: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          paramParcel1 = getStatistics();
          paramParcel2.writeNoException();
          paramParcel2.writeByteArray(paramParcel1);
          return true;
        case 14: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteResetFlashlight();
          paramParcel2.writeNoException();
          return true;
        case 13: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteResetCamera();
          paramParcel2.writeNoException();
          return true;
        case 12: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteStopCamera(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 11: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteStartCamera(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 10: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteFlashlightOff(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 9: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteFlashlightOn(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 8: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteResetAudio();
          paramParcel2.writeNoException();
          return true;
        case 7: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteResetVideo();
          paramParcel2.writeNoException();
          return true;
        case 6: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteStopAudio(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 5: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteStartAudio(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 4: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteStopVideo(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 3: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteStartVideo(paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        case 2: 
          paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
          noteStopSensor(paramParcel1.readInt(), paramParcel1.readInt());
          paramParcel2.writeNoException();
          return true;
        }
        paramParcel1.enforceInterface("com.android.internal.app.IBatteryStats");
        noteStartSensor(paramParcel1.readInt(), paramParcel1.readInt());
        paramParcel2.writeNoException();
        return true;
      }
      paramParcel2.writeString("com.android.internal.app.IBatteryStats");
      return true;
    }
    
    private static class Proxy
      implements IBatteryStats
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      }
      
      public IBinder asBinder()
      {
        return mRemote;
      }
      
      public long computeBatteryTimeRemaining()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long computeChargeTimeRemaining()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getAwakeTimeBattery()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(75, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public long getAwakeTimePlugged()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(76, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public Intent getBatteryInfo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(89, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Intent localIntent;
          if (localParcel2.readInt() != 0) {
            localIntent = (Intent)Intent.CREATOR.createFromParcel(localParcel2);
          } else {
            localIntent = null;
          }
          return localIntent;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public CellularBatteryStats getCellularBatteryStats()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(81, localParcel1, localParcel2, 0);
          localParcel2.readException();
          CellularBatteryStats localCellularBatteryStats;
          if (localParcel2.readInt() != 0) {
            localCellularBatteryStats = (CellularBatteryStats)CellularBatteryStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localCellularBatteryStats = null;
          }
          return localCellularBatteryStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public GpsBatteryStats getGpsBatteryStats()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(83, localParcel1, localParcel2, 0);
          localParcel2.readException();
          GpsBatteryStats localGpsBatteryStats;
          if (localParcel2.readInt() != 0) {
            localGpsBatteryStats = (GpsBatteryStats)GpsBatteryStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localGpsBatteryStats = null;
          }
          return localGpsBatteryStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public String getInterfaceDescriptor()
      {
        return "com.android.internal.app.IBatteryStats";
      }
      
      public byte[] getStatistics()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(15, localParcel1, localParcel2, 0);
          localParcel2.readException();
          byte[] arrayOfByte = localParcel2.createByteArray();
          return arrayOfByte;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public ParcelFileDescriptor getStatisticsStream()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(16, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ParcelFileDescriptor localParcelFileDescriptor;
          if (localParcel2.readInt() != 0) {
            localParcelFileDescriptor = (ParcelFileDescriptor)ParcelFileDescriptor.CREATOR.createFromParcel(localParcel2);
          } else {
            localParcelFileDescriptor = null;
          }
          return localParcelFileDescriptor;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public WifiBatteryStats getWifiBatteryStats()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(82, localParcel1, localParcel2, 0);
          localParcel2.readException();
          WifiBatteryStats localWifiBatteryStats;
          if (localParcel2.readInt() != 0) {
            localWifiBatteryStats = (WifiBatteryStats)WifiBatteryStats.CREATOR.createFromParcel(localParcel2);
          } else {
            localWifiBatteryStats = null;
          }
          return localWifiBatteryStats;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public boolean isCharging()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          IBinder localIBinder = mRemote;
          boolean bool = false;
          localIBinder.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0) {
            bool = true;
          }
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteBleScanResults(WorkSource paramWorkSource, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(80, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteBleScanStarted(WorkSource paramWorkSource, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(77, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteBleScanStopped(WorkSource paramWorkSource, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(78, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteBluetoothControllerActivity(BluetoothActivityEnergyInfo paramBluetoothActivityEnergyInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramBluetoothActivityEnergyInfo != null)
          {
            localParcel.writeInt(1);
            paramBluetoothActivityEnergyInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(86, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      /* Error */
      public void noteChangeWakelockFromSource(WorkSource paramWorkSource1, int paramInt1, String paramString1, String paramString2, int paramInt2, WorkSource paramWorkSource2, int paramInt3, String paramString3, String paramString4, int paramInt4, boolean paramBoolean)
        throws RemoteException
      {
        // Byte code:
        //   0: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   3: astore 12
        //   5: invokestatic 32	android/os/Parcel:obtain	()Landroid/os/Parcel;
        //   8: astore 13
        //   10: aload 12
        //   12: ldc 34
        //   14: invokevirtual 38	android/os/Parcel:writeInterfaceToken	(Ljava/lang/String;)V
        //   17: aload_1
        //   18: ifnull +19 -> 37
        //   21: aload 12
        //   23: iconst_1
        //   24: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   27: aload_1
        //   28: aload 12
        //   30: iconst_0
        //   31: invokevirtual 116	android/os/WorkSource:writeToParcel	(Landroid/os/Parcel;I)V
        //   34: goto +9 -> 43
        //   37: aload 12
        //   39: iconst_0
        //   40: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   43: aload 12
        //   45: iload_2
        //   46: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   49: aload 12
        //   51: aload_3
        //   52: invokevirtual 129	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   55: aload 12
        //   57: aload 4
        //   59: invokevirtual 129	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   62: aload 12
        //   64: iload 5
        //   66: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   69: aload 6
        //   71: ifnull +20 -> 91
        //   74: aload 12
        //   76: iconst_1
        //   77: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   80: aload 6
        //   82: aload 12
        //   84: iconst_0
        //   85: invokevirtual 116	android/os/WorkSource:writeToParcel	(Landroid/os/Parcel;I)V
        //   88: goto +9 -> 97
        //   91: aload 12
        //   93: iconst_0
        //   94: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   97: aload 12
        //   99: iload 7
        //   101: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   104: aload 12
        //   106: aload 8
        //   108: invokevirtual 129	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   111: aload 12
        //   113: aload 9
        //   115: invokevirtual 129	android/os/Parcel:writeString	(Ljava/lang/String;)V
        //   118: aload 12
        //   120: iload 10
        //   122: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   125: aload 12
        //   127: iload 11
        //   129: invokevirtual 110	android/os/Parcel:writeInt	(I)V
        //   132: aload_0
        //   133: getfield 19	com/android/internal/app/IBatteryStats$Stub$Proxy:mRemote	Landroid/os/IBinder;
        //   136: bipush 28
        //   138: aload 12
        //   140: aload 13
        //   142: iconst_0
        //   143: invokeinterface 44 5 0
        //   148: pop
        //   149: aload 13
        //   151: invokevirtual 47	android/os/Parcel:readException	()V
        //   154: aload 13
        //   156: invokevirtual 53	android/os/Parcel:recycle	()V
        //   159: aload 12
        //   161: invokevirtual 53	android/os/Parcel:recycle	()V
        //   164: return
        //   165: astore_1
        //   166: goto +40 -> 206
        //   169: astore_1
        //   170: goto +36 -> 206
        //   173: astore_1
        //   174: goto +32 -> 206
        //   177: astore_1
        //   178: goto +28 -> 206
        //   181: astore_1
        //   182: goto +24 -> 206
        //   185: astore_1
        //   186: goto +20 -> 206
        //   189: astore_1
        //   190: goto +16 -> 206
        //   193: astore_1
        //   194: goto +12 -> 206
        //   197: astore_1
        //   198: goto +8 -> 206
        //   201: astore_1
        //   202: goto +4 -> 206
        //   205: astore_1
        //   206: aload 13
        //   208: invokevirtual 53	android/os/Parcel:recycle	()V
        //   211: aload 12
        //   213: invokevirtual 53	android/os/Parcel:recycle	()V
        //   216: aload_1
        //   217: athrow
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	218	0	this	Proxy
        //   0	218	1	paramWorkSource1	WorkSource
        //   0	218	2	paramInt1	int
        //   0	218	3	paramString1	String
        //   0	218	4	paramString2	String
        //   0	218	5	paramInt2	int
        //   0	218	6	paramWorkSource2	WorkSource
        //   0	218	7	paramInt3	int
        //   0	218	8	paramString3	String
        //   0	218	9	paramString4	String
        //   0	218	10	paramInt4	int
        //   0	218	11	paramBoolean	boolean
        //   3	209	12	localParcel1	Parcel
        //   8	199	13	localParcel2	Parcel
        // Exception table:
        //   from	to	target	type
        //   132	154	165	finally
        //   125	132	169	finally
        //   118	125	173	finally
        //   111	118	177	finally
        //   104	111	181	finally
        //   97	104	185	finally
        //   62	69	189	finally
        //   74	88	189	finally
        //   91	97	189	finally
        //   55	62	193	finally
        //   49	55	197	finally
        //   43	49	201	finally
        //   10	17	205	finally
        //   21	34	205	finally
        //   37	43	205	finally
      }
      
      public void noteConnectivityChanged(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteDeviceIdleMode(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(73, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteEvent(int paramInt1, String paramString, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteFlashlightOff(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteFlashlightOn(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteFullWifiLockAcquired(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(58, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteFullWifiLockAcquiredFromSource(WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(64, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteFullWifiLockReleased(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(59, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteFullWifiLockReleasedFromSource(WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(65, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteGpsChanged(WorkSource paramWorkSource1, WorkSource paramWorkSource2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource1 != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramWorkSource2 != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteGpsSignalQuality(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteInteractive(boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(42, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteJobFinish(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteJobStart(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteLongPartialWakelockFinish(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(32, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteLongPartialWakelockFinishFromSource(String paramString1, String paramString2, WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteLongPartialWakelockStart(String paramString1, String paramString2, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt);
          mRemote.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteLongPartialWakelockStartFromSource(String paramString1, String paramString2, WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteMobileRadioPowerState(int paramInt1, long paramLong, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(44, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteModemControllerActivity(ModemActivityInfo paramModemActivityInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramModemActivityInfo != null)
          {
            localParcel.writeInt(1);
            paramModemActivityInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(87, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void noteNetworkInterfaceType(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(71, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteNetworkStatsEnabled()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(72, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notePhoneDataConnectionState(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(48, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notePhoneOff()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(46, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notePhoneOn()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(45, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notePhoneSignalStrength(SignalStrength paramSignalStrength)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramSignalStrength != null)
          {
            localParcel1.writeInt(1);
            paramSignalStrength.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(47, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void notePhoneState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(49, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteResetAudio()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteResetBleScan()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(79, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteResetCamera()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteResetFlashlight()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteResetVideo()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteScreenBrightness(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteScreenState(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStartAudio(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStartCamera(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(11, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStartSensor(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStartVideo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStartWakelock(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStartWakelockFromSource(WorkSource paramWorkSource, int paramInt1, String paramString1, String paramString2, int paramInt2, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(27, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStopAudio(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStopCamera(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(12, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStopSensor(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStopVideo(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStopWakelock(int paramInt1, int paramInt2, String paramString1, String paramString2, int paramInt3)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt3);
          mRemote.transact(26, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteStopWakelockFromSource(WorkSource paramWorkSource, int paramInt1, String paramString1, String paramString2, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt1);
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(29, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteSyncFinish(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteSyncStart(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(21, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteUserActivity(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(40, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteVibratorOff(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteVibratorOn(int paramInt, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          localParcel1.writeLong(paramLong);
          mRemote.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWakeUp(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          mRemote.transact(41, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiBatchedScanStartedFromSource(WorkSource paramWorkSource, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          localParcel1.writeInt(paramInt);
          mRemote.transact(68, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiBatchedScanStoppedFromSource(WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(69, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiControllerActivity(WifiActivityEnergyInfo paramWifiActivityEnergyInfo)
        throws RemoteException
      {
        Parcel localParcel = Parcel.obtain();
        try
        {
          localParcel.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWifiActivityEnergyInfo != null)
          {
            localParcel.writeInt(1);
            paramWifiActivityEnergyInfo.writeToParcel(localParcel, 0);
          }
          else
          {
            localParcel.writeInt(0);
          }
          mRemote.transact(88, localParcel, null, 1);
          return;
        }
        finally
        {
          localParcel.recycle();
        }
      }
      
      public void noteWifiMulticastDisabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(63, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiMulticastEnabled(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(62, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiOff()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(51, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiOn()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          mRemote.transact(50, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiRadioPowerState(int paramInt1, long paramLong, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeLong(paramLong);
          localParcel1.writeInt(paramInt2);
          mRemote.transact(70, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiRssiChanged(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(57, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiRunning(WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(52, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiRunningChanged(WorkSource paramWorkSource1, WorkSource paramWorkSource2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource1 != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource1.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          if (paramWorkSource2 != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource2.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(53, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiScanStarted(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(60, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiScanStartedFromSource(WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(66, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiScanStopped(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(61, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiScanStoppedFromSource(WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(67, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiState(int paramInt, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          localParcel1.writeString(paramString);
          mRemote.transact(55, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiStopped(WorkSource paramWorkSource)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          if (paramWorkSource != null)
          {
            localParcel1.writeInt(1);
            paramWorkSource.writeToParcel(localParcel1, 0);
          }
          else
          {
            localParcel1.writeInt(0);
          }
          mRemote.transact(54, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void noteWifiSupplicantStateChanged(int paramInt, boolean paramBoolean)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          localParcel1.writeInt(paramBoolean);
          mRemote.transact(56, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public void setBatteryState(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          localParcel1.writeInt(paramInt3);
          localParcel1.writeInt(paramInt4);
          localParcel1.writeInt(paramInt5);
          localParcel1.writeInt(paramInt6);
          localParcel1.writeInt(paramInt7);
          localParcel1.writeInt(paramInt8);
          mRemote.transact(74, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public HealthStatsParceler takeUidSnapshot(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeInt(paramInt);
          mRemote.transact(84, localParcel1, localParcel2, 0);
          localParcel2.readException();
          HealthStatsParceler localHealthStatsParceler;
          if (localParcel2.readInt() != 0) {
            localHealthStatsParceler = (HealthStatsParceler)HealthStatsParceler.CREATOR.createFromParcel(localParcel2);
          } else {
            localHealthStatsParceler = null;
          }
          return localHealthStatsParceler;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
      
      public HealthStatsParceler[] takeUidSnapshots(int[] paramArrayOfInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.android.internal.app.IBatteryStats");
          localParcel1.writeIntArray(paramArrayOfInt);
          mRemote.transact(85, localParcel1, localParcel2, 0);
          localParcel2.readException();
          paramArrayOfInt = (HealthStatsParceler[])localParcel2.createTypedArray(HealthStatsParceler.CREATOR);
          return paramArrayOfInt;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}
