package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class LceDataInfo
{
  public byte confidenceLevel;
  public int lastHopCapacityKbps;
  public boolean lceSuspended;
  
  public LceDataInfo() {}
  
  public static final ArrayList<LceDataInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 8, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      LceDataInfo localLceDataInfo = new LceDataInfo();
      localLceDataInfo.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 8);
      localArrayList.add(localLceDataInfo);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<LceDataInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 8);
    while (j < i)
    {
      ((LceDataInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 8);
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
    if (paramObject.getClass() != LceDataInfo.class) {
      return false;
    }
    paramObject = (LceDataInfo)paramObject;
    if (lastHopCapacityKbps != lastHopCapacityKbps) {
      return false;
    }
    if (confidenceLevel != confidenceLevel) {
      return false;
    }
    return lceSuspended == lceSuspended;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(lastHopCapacityKbps))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(confidenceLevel))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(lceSuspended))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    lastHopCapacityKbps = paramHwBlob.getInt32(0L + paramLong);
    confidenceLevel = paramHwBlob.getInt8(4L + paramLong);
    lceSuspended = paramHwBlob.getBool(5L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(8L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".lastHopCapacityKbps = ");
    localStringBuilder.append(lastHopCapacityKbps);
    localStringBuilder.append(", .confidenceLevel = ");
    localStringBuilder.append(confidenceLevel);
    localStringBuilder.append(", .lceSuspended = ");
    localStringBuilder.append(lceSuspended);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, lastHopCapacityKbps);
    paramHwBlob.putInt8(4L + paramLong, confidenceLevel);
    paramHwBlob.putBool(5L + paramLong, lceSuspended);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(8);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
