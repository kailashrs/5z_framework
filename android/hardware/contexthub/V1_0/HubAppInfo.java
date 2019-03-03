package android.hardware.contexthub.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class HubAppInfo
{
  public long appId;
  public boolean enabled;
  public final ArrayList<MemRange> memUsage = new ArrayList();
  public int version;
  
  public HubAppInfo() {}
  
  public static final ArrayList<HubAppInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 40, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      HubAppInfo localHubAppInfo = new HubAppInfo();
      localHubAppInfo.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 40);
      localArrayList.add(localHubAppInfo);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<HubAppInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 40);
    while (j < i)
    {
      ((HubAppInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 40);
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
    if (paramObject.getClass() != HubAppInfo.class) {
      return false;
    }
    paramObject = (HubAppInfo)paramObject;
    if (appId != appId) {
      return false;
    }
    if (version != version) {
      return false;
    }
    if (!HidlSupport.deepEquals(memUsage, memUsage)) {
      return false;
    }
    return enabled == enabled;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(appId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(version))), Integer.valueOf(HidlSupport.deepHashCode(memUsage)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(enabled))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    appId = paramHwBlob.getInt64(paramLong + 0L);
    version = paramHwBlob.getInt32(paramLong + 8L);
    int i = paramHwBlob.getInt32(paramLong + 16L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, paramHwBlob.handle(), paramLong + 16L + 0L, true);
    memUsage.clear();
    for (int j = 0; j < i; j++)
    {
      MemRange localMemRange = new MemRange();
      localMemRange.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 16);
      memUsage.add(localMemRange);
    }
    enabled = paramHwBlob.getBool(paramLong + 32L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(40L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".appId = ");
    localStringBuilder.append(appId);
    localStringBuilder.append(", .version = ");
    localStringBuilder.append(version);
    localStringBuilder.append(", .memUsage = ");
    localStringBuilder.append(memUsage);
    localStringBuilder.append(", .enabled = ");
    localStringBuilder.append(enabled);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt64(paramLong + 0L, appId);
    paramHwBlob.putInt32(paramLong + 8L, version);
    int i = memUsage.size();
    paramHwBlob.putInt32(paramLong + 16L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 16L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 16);
    while (j < i)
    {
      ((MemRange)memUsage.get(j)).writeEmbeddedToBlob(localHwBlob, j * 16);
      j++;
    }
    paramHwBlob.putBlob(16L + paramLong + 0L, localHwBlob);
    paramHwBlob.putBool(32L + paramLong, enabled);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(40);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
