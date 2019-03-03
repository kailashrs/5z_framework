package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class LinkCapacityEstimate
{
  public int downlinkCapacityKbps;
  public int uplinkCapacityKbps;
  
  public LinkCapacityEstimate() {}
  
  public static final ArrayList<LinkCapacityEstimate> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 8, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      LinkCapacityEstimate localLinkCapacityEstimate = new LinkCapacityEstimate();
      localLinkCapacityEstimate.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 8);
      localArrayList.add(localLinkCapacityEstimate);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<LinkCapacityEstimate> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 8);
    while (j < i)
    {
      ((LinkCapacityEstimate)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 8);
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
    if (paramObject.getClass() != LinkCapacityEstimate.class) {
      return false;
    }
    paramObject = (LinkCapacityEstimate)paramObject;
    if (downlinkCapacityKbps != downlinkCapacityKbps) {
      return false;
    }
    return uplinkCapacityKbps == uplinkCapacityKbps;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(downlinkCapacityKbps))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(uplinkCapacityKbps))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    downlinkCapacityKbps = paramHwBlob.getInt32(0L + paramLong);
    uplinkCapacityKbps = paramHwBlob.getInt32(4L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(8L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".downlinkCapacityKbps = ");
    localStringBuilder.append(downlinkCapacityKbps);
    localStringBuilder.append(", .uplinkCapacityKbps = ");
    localStringBuilder.append(uplinkCapacityKbps);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, downlinkCapacityKbps);
    paramHwBlob.putInt32(4L + paramLong, uplinkCapacityKbps);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(8);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
