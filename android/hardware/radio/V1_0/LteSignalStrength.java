package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class LteSignalStrength
{
  public int cqi;
  public int rsrp;
  public int rsrq;
  public int rssnr;
  public int signalStrength;
  public int timingAdvance;
  
  public LteSignalStrength() {}
  
  public static final ArrayList<LteSignalStrength> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      LteSignalStrength localLteSignalStrength = new LteSignalStrength();
      localLteSignalStrength.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localLteSignalStrength);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<LteSignalStrength> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((LteSignalStrength)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != LteSignalStrength.class) {
      return false;
    }
    paramObject = (LteSignalStrength)paramObject;
    if (signalStrength != signalStrength) {
      return false;
    }
    if (rsrp != rsrp) {
      return false;
    }
    if (rsrq != rsrq) {
      return false;
    }
    if (rssnr != rssnr) {
      return false;
    }
    if (cqi != cqi) {
      return false;
    }
    return timingAdvance == timingAdvance;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(signalStrength))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rsrp))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rsrq))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rssnr))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cqi))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(timingAdvance))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    signalStrength = paramHwBlob.getInt32(0L + paramLong);
    rsrp = paramHwBlob.getInt32(4L + paramLong);
    rsrq = paramHwBlob.getInt32(8L + paramLong);
    rssnr = paramHwBlob.getInt32(12L + paramLong);
    cqi = paramHwBlob.getInt32(16L + paramLong);
    timingAdvance = paramHwBlob.getInt32(20L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(24L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".signalStrength = ");
    localStringBuilder.append(signalStrength);
    localStringBuilder.append(", .rsrp = ");
    localStringBuilder.append(rsrp);
    localStringBuilder.append(", .rsrq = ");
    localStringBuilder.append(rsrq);
    localStringBuilder.append(", .rssnr = ");
    localStringBuilder.append(rssnr);
    localStringBuilder.append(", .cqi = ");
    localStringBuilder.append(cqi);
    localStringBuilder.append(", .timingAdvance = ");
    localStringBuilder.append(timingAdvance);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, signalStrength);
    paramHwBlob.putInt32(4L + paramLong, rsrp);
    paramHwBlob.putInt32(8L + paramLong, rsrq);
    paramHwBlob.putInt32(12L + paramLong, rssnr);
    paramHwBlob.putInt32(16L + paramLong, cqi);
    paramHwBlob.putInt32(20L + paramLong, timingAdvance);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(24);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
