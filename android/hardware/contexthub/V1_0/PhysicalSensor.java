package android.hardware.contexthub.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class PhysicalSensor
{
  public int fifoMaxCount;
  public int fifoReservedCount;
  public long maxDelayMs;
  public long minDelayMs;
  public String name = new String();
  public float peakPowerMw;
  public int sensorType;
  public String type = new String();
  public String vendor = new String();
  public int version;
  
  public PhysicalSensor() {}
  
  public static final ArrayList<PhysicalSensor> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 96, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new PhysicalSensor();
      ((PhysicalSensor)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 96);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<PhysicalSensor> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 96);
    while (j < i)
    {
      ((PhysicalSensor)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 96);
      j++;
    }
    localHwBlob1.putBlob(0L, localHwBlob2);
    paramHwParcel.writeBuffer(localHwBlob1);
  }
  
  public final boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (paramObject == null) {
      return false;
    }
    if (paramObject.getClass() != PhysicalSensor.class) {
      return false;
    }
    paramObject = (PhysicalSensor)paramObject;
    if (sensorType != sensorType) {
      return false;
    }
    if (!HidlSupport.deepEquals(type, type)) {
      return false;
    }
    if (!HidlSupport.deepEquals(name, name)) {
      return false;
    }
    if (!HidlSupport.deepEquals(vendor, vendor)) {
      return false;
    }
    if (version != version) {
      return false;
    }
    if (fifoReservedCount != fifoReservedCount) {
      return false;
    }
    if (fifoMaxCount != fifoMaxCount) {
      return false;
    }
    if (minDelayMs != minDelayMs) {
      return false;
    }
    if (maxDelayMs != maxDelayMs) {
      return false;
    }
    return peakPowerMw == peakPowerMw;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(sensorType))), Integer.valueOf(HidlSupport.deepHashCode(type)), Integer.valueOf(HidlSupport.deepHashCode(name)), Integer.valueOf(HidlSupport.deepHashCode(vendor)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(version))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(fifoReservedCount))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(fifoMaxCount))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(minDelayMs))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(maxDelayMs))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(peakPowerMw))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    sensorType = paramHwBlob.getInt32(paramLong + 0L);
    type = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(type.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
    name = paramHwBlob.getString(paramLong + 24L);
    paramHwParcel.readEmbeddedBuffer(name.getBytes().length + 1, paramHwBlob.handle(), paramLong + 24L + 0L, false);
    vendor = paramHwBlob.getString(paramLong + 40L);
    paramHwParcel.readEmbeddedBuffer(vendor.getBytes().length + 1, paramHwBlob.handle(), paramLong + 40L + 0L, false);
    version = paramHwBlob.getInt32(paramLong + 56L);
    fifoReservedCount = paramHwBlob.getInt32(paramLong + 60L);
    fifoMaxCount = paramHwBlob.getInt32(paramLong + 64L);
    minDelayMs = paramHwBlob.getInt64(paramLong + 72L);
    maxDelayMs = paramHwBlob.getInt64(paramLong + 80L);
    peakPowerMw = paramHwBlob.getFloat(paramLong + 88L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(96L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".sensorType = ");
    localStringBuilder.append(SensorType.toString(sensorType));
    localStringBuilder.append(", .type = ");
    localStringBuilder.append(type);
    localStringBuilder.append(", .name = ");
    localStringBuilder.append(name);
    localStringBuilder.append(", .vendor = ");
    localStringBuilder.append(vendor);
    localStringBuilder.append(", .version = ");
    localStringBuilder.append(version);
    localStringBuilder.append(", .fifoReservedCount = ");
    localStringBuilder.append(fifoReservedCount);
    localStringBuilder.append(", .fifoMaxCount = ");
    localStringBuilder.append(fifoMaxCount);
    localStringBuilder.append(", .minDelayMs = ");
    localStringBuilder.append(minDelayMs);
    localStringBuilder.append(", .maxDelayMs = ");
    localStringBuilder.append(maxDelayMs);
    localStringBuilder.append(", .peakPowerMw = ");
    localStringBuilder.append(peakPowerMw);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, sensorType);
    paramHwBlob.putString(8L + paramLong, type);
    paramHwBlob.putString(24L + paramLong, name);
    paramHwBlob.putString(40L + paramLong, vendor);
    paramHwBlob.putInt32(56L + paramLong, version);
    paramHwBlob.putInt32(60L + paramLong, fifoReservedCount);
    paramHwBlob.putInt32(64L + paramLong, fifoMaxCount);
    paramHwBlob.putInt64(72L + paramLong, minDelayMs);
    paramHwBlob.putInt64(80L + paramLong, maxDelayMs);
    paramHwBlob.putFloat(88L + paramLong, peakPowerMw);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(96);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
