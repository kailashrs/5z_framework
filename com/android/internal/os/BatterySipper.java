package com.android.internal.os;

import android.os.BatteryStats.Uid;

public class BatterySipper
  implements Comparable<BatterySipper>
{
  public double audioPowerMah;
  public long audioTimeMs;
  public double bluetoothPowerMah;
  public long bluetoothRunningTimeMs;
  public long btRxBytes;
  public long btTxBytes;
  public double cameraPowerMah;
  public long cameraTimeMs;
  public long cpuFgTimeMs;
  public double cpuPowerMah;
  public long cpuTimeMs;
  public DrainType drainType;
  public double flashlightPowerMah;
  public long flashlightTimeMs;
  public double gpsPowerMah;
  public long gpsTimeMs;
  public String[] mPackages;
  public long mobileActive;
  public int mobileActiveCount;
  public double mobileRadioPowerMah;
  public long mobileRxBytes;
  public long mobileRxPackets;
  public long mobileTxBytes;
  public long mobileTxPackets;
  public double mobilemspp;
  public double noCoveragePercent;
  public String packageWithHighestDrain;
  public double percent;
  public double proportionalSmearMah;
  public double screenPowerMah;
  public double sensorPowerMah;
  public boolean shouldHide;
  public double totalPowerMah;
  public double totalSmearedPowerMah;
  public BatteryStats.Uid uidObj;
  public double usagePowerMah;
  public long usageTimeMs;
  public int userId;
  public double videoPowerMah;
  public long videoTimeMs;
  public double wakeLockPowerMah;
  public long wakeLockTimeMs;
  public double wifiPowerMah;
  public long wifiRunningTimeMs;
  public long wifiRxBytes;
  public long wifiRxPackets;
  public long wifiTxBytes;
  public long wifiTxPackets;
  
  public BatterySipper(DrainType paramDrainType, BatteryStats.Uid paramUid, double paramDouble)
  {
    totalPowerMah = paramDouble;
    drainType = paramDrainType;
    uidObj = paramUid;
  }
  
  public void add(BatterySipper paramBatterySipper)
  {
    totalPowerMah += totalPowerMah;
    usageTimeMs += usageTimeMs;
    usagePowerMah += usagePowerMah;
    audioTimeMs += audioTimeMs;
    cpuTimeMs += cpuTimeMs;
    gpsTimeMs += gpsTimeMs;
    wifiRunningTimeMs += wifiRunningTimeMs;
    cpuFgTimeMs += cpuFgTimeMs;
    videoTimeMs += videoTimeMs;
    wakeLockTimeMs += wakeLockTimeMs;
    cameraTimeMs += cameraTimeMs;
    flashlightTimeMs += flashlightTimeMs;
    bluetoothRunningTimeMs += bluetoothRunningTimeMs;
    mobileRxPackets += mobileRxPackets;
    mobileTxPackets += mobileTxPackets;
    mobileActive += mobileActive;
    mobileActiveCount += mobileActiveCount;
    wifiRxPackets += wifiRxPackets;
    wifiTxPackets += wifiTxPackets;
    mobileRxBytes += mobileRxBytes;
    mobileTxBytes += mobileTxBytes;
    wifiRxBytes += wifiRxBytes;
    wifiTxBytes += wifiTxBytes;
    btRxBytes += btRxBytes;
    btTxBytes += btTxBytes;
    audioPowerMah += audioPowerMah;
    wifiPowerMah += wifiPowerMah;
    gpsPowerMah += gpsPowerMah;
    cpuPowerMah += cpuPowerMah;
    sensorPowerMah += sensorPowerMah;
    mobileRadioPowerMah += mobileRadioPowerMah;
    wakeLockPowerMah += wakeLockPowerMah;
    cameraPowerMah += cameraPowerMah;
    flashlightPowerMah += flashlightPowerMah;
    bluetoothPowerMah += bluetoothPowerMah;
    screenPowerMah += screenPowerMah;
    videoPowerMah += videoPowerMah;
    proportionalSmearMah += proportionalSmearMah;
    totalSmearedPowerMah += totalSmearedPowerMah;
  }
  
  public int compareTo(BatterySipper paramBatterySipper)
  {
    if (drainType != drainType)
    {
      if (drainType == DrainType.OVERCOUNTED) {
        return 1;
      }
      if (drainType == DrainType.OVERCOUNTED) {
        return -1;
      }
    }
    return Double.compare(totalPowerMah, totalPowerMah);
  }
  
  public void computeMobilemspp()
  {
    long l = mobileRxPackets + mobileTxPackets;
    double d;
    if (l > 0L) {
      d = mobileActive / l;
    } else {
      d = 0.0D;
    }
    mobilemspp = d;
  }
  
  public String[] getPackages()
  {
    return mPackages;
  }
  
  public int getUid()
  {
    if (uidObj == null) {
      return 0;
    }
    return uidObj.getUid();
  }
  
  public double sumPower()
  {
    totalPowerMah = (usagePowerMah + wifiPowerMah + gpsPowerMah + cpuPowerMah + sensorPowerMah + mobileRadioPowerMah + wakeLockPowerMah + cameraPowerMah + flashlightPowerMah + bluetoothPowerMah + audioPowerMah + videoPowerMah);
    totalSmearedPowerMah = (totalPowerMah + screenPowerMah + proportionalSmearMah);
    return totalPowerMah;
  }
  
  public static enum DrainType
  {
    AMBIENT_DISPLAY,  APP,  BLUETOOTH,  CAMERA,  CELL,  FLASHLIGHT,  IDLE,  MEMORY,  OVERCOUNTED,  PHONE,  SCREEN,  UNACCOUNTED,  USER,  WIFI;
    
    private DrainType() {}
  }
}
