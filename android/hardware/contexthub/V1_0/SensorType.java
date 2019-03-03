package android.hardware.contexthub.V1_0;

import java.util.ArrayList;

public final class SensorType
{
  public static final int ACCELEROMETER = 1;
  public static final int AMBIENT_LIGHT_SENSOR = 6;
  public static final int AUDIO = 768;
  public static final int BAROMETER = 4;
  public static final int BLE = 1280;
  public static final int CAMERA = 1024;
  public static final int GPS = 256;
  public static final int GYROSCOPE = 2;
  public static final int INSTANT_MOTION_DETECT = 8;
  public static final int MAGNETOMETER = 3;
  public static final int PRIVATE_SENSOR_BASE = 65536;
  public static final int PROXIMITY_SENSOR = 5;
  public static final int RESERVED = 0;
  public static final int STATIONARY_DETECT = 7;
  public static final int WIFI = 512;
  public static final int WWAN = 1536;
  
  public SensorType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("RESERVED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ACCELEROMETER");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("GYROSCOPE");
      j = i | 0x2;
    }
    int k = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("MAGNETOMETER");
      k = j | 0x3;
    }
    i = k;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("BAROMETER");
      i = k | 0x4;
    }
    j = i;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("PROXIMITY_SENSOR");
      j = i | 0x5;
    }
    k = j;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("AMBIENT_LIGHT_SENSOR");
      k = j | 0x6;
    }
    i = k;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("STATIONARY_DETECT");
      i = k | 0x7;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("INSTANT_MOTION_DETECT");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x100) == 256)
    {
      localArrayList.add("GPS");
      i = j | 0x100;
    }
    k = i;
    if ((paramInt & 0x200) == 512)
    {
      localArrayList.add("WIFI");
      k = i | 0x200;
    }
    j = k;
    if ((paramInt & 0x300) == 768)
    {
      localArrayList.add("AUDIO");
      j = k | 0x300;
    }
    i = j;
    if ((paramInt & 0x400) == 1024)
    {
      localArrayList.add("CAMERA");
      i = j | 0x400;
    }
    k = i;
    if ((paramInt & 0x500) == 1280)
    {
      localArrayList.add("BLE");
      k = i | 0x500;
    }
    j = k;
    if ((paramInt & 0x600) == 1536)
    {
      localArrayList.add("WWAN");
      j = k | 0x600;
    }
    i = j;
    if ((paramInt & 0x10000) == 65536)
    {
      localArrayList.add("PRIVATE_SENSOR_BASE");
      i = j | 0x10000;
    }
    if (paramInt != i)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(i & paramInt));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(int paramInt)
  {
    if (paramInt == 0) {
      return "RESERVED";
    }
    if (paramInt == 1) {
      return "ACCELEROMETER";
    }
    if (paramInt == 2) {
      return "GYROSCOPE";
    }
    if (paramInt == 3) {
      return "MAGNETOMETER";
    }
    if (paramInt == 4) {
      return "BAROMETER";
    }
    if (paramInt == 5) {
      return "PROXIMITY_SENSOR";
    }
    if (paramInt == 6) {
      return "AMBIENT_LIGHT_SENSOR";
    }
    if (paramInt == 7) {
      return "STATIONARY_DETECT";
    }
    if (paramInt == 8) {
      return "INSTANT_MOTION_DETECT";
    }
    if (paramInt == 256) {
      return "GPS";
    }
    if (paramInt == 512) {
      return "WIFI";
    }
    if (paramInt == 768) {
      return "AUDIO";
    }
    if (paramInt == 1024) {
      return "CAMERA";
    }
    if (paramInt == 1280) {
      return "BLE";
    }
    if (paramInt == 1536) {
      return "WWAN";
    }
    if (paramInt == 65536) {
      return "PRIVATE_SENSOR_BASE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
