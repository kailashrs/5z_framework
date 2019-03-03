package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class WcdmaSignalStrength
{
  public int bitErrorRate;
  public int signalStrength;
  
  public WcdmaSignalStrength() {}
  
  public static final ArrayList<WcdmaSignalStrength> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 8, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      WcdmaSignalStrength localWcdmaSignalStrength = new WcdmaSignalStrength();
      localWcdmaSignalStrength.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 8);
      localArrayList.add(localWcdmaSignalStrength);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<WcdmaSignalStrength> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 8);
    while (j < i)
    {
      ((WcdmaSignalStrength)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 8);
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
    if (paramObject.getClass() != WcdmaSignalStrength.class) {
      return false;
    }
    paramObject = (WcdmaSignalStrength)paramObject;
    if (signalStrength != signalStrength) {
      return false;
    }
    return bitErrorRate == bitErrorRate;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(signalStrength))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(bitErrorRate))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    signalStrength = paramHwBlob.getInt32(0L + paramLong);
    bitErrorRate = paramHwBlob.getInt32(4L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(8L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".signalStrength = ");
    localStringBuilder.append(signalStrength);
    localStringBuilder.append(", .bitErrorRate = ");
    localStringBuilder.append(bitErrorRate);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, signalStrength);
    paramHwBlob.putInt32(4L + paramLong, bitErrorRate);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(8);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
