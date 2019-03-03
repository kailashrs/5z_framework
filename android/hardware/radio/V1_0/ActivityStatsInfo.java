package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public final class ActivityStatsInfo
{
  public int idleModeTimeMs;
  public int rxModeTimeMs;
  public int sleepModeTimeMs;
  public final int[] txmModetimeMs = new int[5];
  
  public ActivityStatsInfo() {}
  
  public static final ArrayList<ActivityStatsInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new ActivityStatsInfo();
      ((ActivityStatsInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<ActivityStatsInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((ActivityStatsInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != ActivityStatsInfo.class) {
      return false;
    }
    paramObject = (ActivityStatsInfo)paramObject;
    if (sleepModeTimeMs != sleepModeTimeMs) {
      return false;
    }
    if (idleModeTimeMs != idleModeTimeMs) {
      return false;
    }
    if (!HidlSupport.deepEquals(txmModetimeMs, txmModetimeMs)) {
      return false;
    }
    return rxModeTimeMs == rxModeTimeMs;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(sleepModeTimeMs))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(idleModeTimeMs))), Integer.valueOf(HidlSupport.deepHashCode(txmModetimeMs)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rxModeTimeMs))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    sleepModeTimeMs = paramHwBlob.getInt32(0L + paramLong);
    idleModeTimeMs = paramHwBlob.getInt32(4L + paramLong);
    paramHwBlob.copyToInt32Array(8L + paramLong, txmModetimeMs, 5);
    rxModeTimeMs = paramHwBlob.getInt32(28L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(32L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".sleepModeTimeMs = ");
    localStringBuilder.append(sleepModeTimeMs);
    localStringBuilder.append(", .idleModeTimeMs = ");
    localStringBuilder.append(idleModeTimeMs);
    localStringBuilder.append(", .txmModetimeMs = ");
    localStringBuilder.append(Arrays.toString(txmModetimeMs));
    localStringBuilder.append(", .rxModeTimeMs = ");
    localStringBuilder.append(rxModeTimeMs);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, sleepModeTimeMs);
    paramHwBlob.putInt32(4L + paramLong, idleModeTimeMs);
    paramHwBlob.putInt32Array(8L + paramLong, txmModetimeMs);
    paramHwBlob.putInt32(28L + paramLong, rxModeTimeMs);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
