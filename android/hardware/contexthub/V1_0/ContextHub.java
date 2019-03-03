package android.hardware.contexthub.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class ContextHub
{
  public byte chreApiMajorVersion;
  public byte chreApiMinorVersion;
  public short chrePatchVersion;
  public long chrePlatformId;
  public final ArrayList<PhysicalSensor> connectedSensors = new ArrayList();
  public int hubId;
  public int maxSupportedMsgLen;
  public String name = new String();
  public float peakMips;
  public float peakPowerDrawMw;
  public int platformVersion;
  public float sleepPowerDrawMw;
  public float stoppedPowerDrawMw;
  public String toolchain = new String();
  public int toolchainVersion;
  public String vendor = new String();
  
  public ContextHub() {}
  
  public static final ArrayList<ContextHub> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 120, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new ContextHub();
      ((ContextHub)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 120);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<ContextHub> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 120);
    while (j < i)
    {
      ((ContextHub)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 120);
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
    if (paramObject.getClass() != ContextHub.class) {
      return false;
    }
    paramObject = (ContextHub)paramObject;
    if (!HidlSupport.deepEquals(name, name)) {
      return false;
    }
    if (!HidlSupport.deepEquals(vendor, vendor)) {
      return false;
    }
    if (!HidlSupport.deepEquals(toolchain, toolchain)) {
      return false;
    }
    if (platformVersion != platformVersion) {
      return false;
    }
    if (toolchainVersion != toolchainVersion) {
      return false;
    }
    if (hubId != hubId) {
      return false;
    }
    if (peakMips != peakMips) {
      return false;
    }
    if (stoppedPowerDrawMw != stoppedPowerDrawMw) {
      return false;
    }
    if (sleepPowerDrawMw != sleepPowerDrawMw) {
      return false;
    }
    if (peakPowerDrawMw != peakPowerDrawMw) {
      return false;
    }
    if (!HidlSupport.deepEquals(connectedSensors, connectedSensors)) {
      return false;
    }
    if (maxSupportedMsgLen != maxSupportedMsgLen) {
      return false;
    }
    if (chrePlatformId != chrePlatformId) {
      return false;
    }
    if (chreApiMajorVersion != chreApiMajorVersion) {
      return false;
    }
    if (chreApiMinorVersion != chreApiMinorVersion) {
      return false;
    }
    return chrePatchVersion == chrePatchVersion;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(name)), Integer.valueOf(HidlSupport.deepHashCode(vendor)), Integer.valueOf(HidlSupport.deepHashCode(toolchain)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(platformVersion))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(toolchainVersion))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(hubId))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(peakMips))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(stoppedPowerDrawMw))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(sleepPowerDrawMw))), Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(peakPowerDrawMw))), Integer.valueOf(HidlSupport.deepHashCode(connectedSensors)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxSupportedMsgLen))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(chrePlatformId))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(chreApiMajorVersion))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(chreApiMinorVersion))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(chrePatchVersion))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    name = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(name.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    vendor = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(vendor.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    toolchain = paramHwBlob.getString(paramLong + 32L);
    paramHwParcel.readEmbeddedBuffer(toolchain.getBytes().length + 1, paramHwBlob.handle(), paramLong + 32L + 0L, false);
    platformVersion = paramHwBlob.getInt32(paramLong + 48L);
    toolchainVersion = paramHwBlob.getInt32(paramLong + 52L);
    hubId = paramHwBlob.getInt32(paramLong + 56L);
    peakMips = paramHwBlob.getFloat(paramLong + 60L);
    stoppedPowerDrawMw = paramHwBlob.getFloat(paramLong + 64L);
    sleepPowerDrawMw = paramHwBlob.getFloat(paramLong + 68L);
    peakPowerDrawMw = paramHwBlob.getFloat(paramLong + 72L);
    int i = paramHwBlob.getInt32(paramLong + 80L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 96, paramHwBlob.handle(), paramLong + 80L + 0L, true);
    connectedSensors.clear();
    for (int j = 0; j < i; j++)
    {
      PhysicalSensor localPhysicalSensor = new PhysicalSensor();
      localPhysicalSensor.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 96);
      connectedSensors.add(localPhysicalSensor);
    }
    maxSupportedMsgLen = paramHwBlob.getInt32(paramLong + 96L);
    chrePlatformId = paramHwBlob.getInt64(paramLong + 104L);
    chreApiMajorVersion = paramHwBlob.getInt8(paramLong + 112L);
    chreApiMinorVersion = paramHwBlob.getInt8(paramLong + 113L);
    chrePatchVersion = paramHwBlob.getInt16(paramLong + 114L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(120L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".name = ");
    localStringBuilder.append(name);
    localStringBuilder.append(", .vendor = ");
    localStringBuilder.append(vendor);
    localStringBuilder.append(", .toolchain = ");
    localStringBuilder.append(toolchain);
    localStringBuilder.append(", .platformVersion = ");
    localStringBuilder.append(platformVersion);
    localStringBuilder.append(", .toolchainVersion = ");
    localStringBuilder.append(toolchainVersion);
    localStringBuilder.append(", .hubId = ");
    localStringBuilder.append(hubId);
    localStringBuilder.append(", .peakMips = ");
    localStringBuilder.append(peakMips);
    localStringBuilder.append(", .stoppedPowerDrawMw = ");
    localStringBuilder.append(stoppedPowerDrawMw);
    localStringBuilder.append(", .sleepPowerDrawMw = ");
    localStringBuilder.append(sleepPowerDrawMw);
    localStringBuilder.append(", .peakPowerDrawMw = ");
    localStringBuilder.append(peakPowerDrawMw);
    localStringBuilder.append(", .connectedSensors = ");
    localStringBuilder.append(connectedSensors);
    localStringBuilder.append(", .maxSupportedMsgLen = ");
    localStringBuilder.append(maxSupportedMsgLen);
    localStringBuilder.append(", .chrePlatformId = ");
    localStringBuilder.append(chrePlatformId);
    localStringBuilder.append(", .chreApiMajorVersion = ");
    localStringBuilder.append(chreApiMajorVersion);
    localStringBuilder.append(", .chreApiMinorVersion = ");
    localStringBuilder.append(chreApiMinorVersion);
    localStringBuilder.append(", .chrePatchVersion = ");
    localStringBuilder.append(chrePatchVersion);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(paramLong + 0L, name);
    paramHwBlob.putString(16L + paramLong, vendor);
    paramHwBlob.putString(32L + paramLong, toolchain);
    paramHwBlob.putInt32(48L + paramLong, platformVersion);
    paramHwBlob.putInt32(52L + paramLong, toolchainVersion);
    paramHwBlob.putInt32(56L + paramLong, hubId);
    paramHwBlob.putFloat(60L + paramLong, peakMips);
    paramHwBlob.putFloat(64L + paramLong, stoppedPowerDrawMw);
    paramHwBlob.putFloat(68L + paramLong, sleepPowerDrawMw);
    paramHwBlob.putFloat(72L + paramLong, peakPowerDrawMw);
    int i = connectedSensors.size();
    paramHwBlob.putInt32(paramLong + 80L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 80L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 96);
    while (j < i)
    {
      ((PhysicalSensor)connectedSensors.get(j)).writeEmbeddedToBlob(localHwBlob, j * 96);
      j++;
    }
    paramHwBlob.putBlob(80L + paramLong + 0L, localHwBlob);
    paramHwBlob.putInt32(96L + paramLong, maxSupportedMsgLen);
    paramHwBlob.putInt64(104L + paramLong, chrePlatformId);
    paramHwBlob.putInt8(112L + paramLong, chreApiMajorVersion);
    paramHwBlob.putInt8(113L + paramLong, chreApiMinorVersion);
    paramHwBlob.putInt16(114L + paramLong, chrePatchVersion);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(120);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
