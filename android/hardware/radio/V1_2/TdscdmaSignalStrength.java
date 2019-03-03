package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class TdscdmaSignalStrength
{
  public int bitErrorRate;
  public int rscp;
  public int signalStrength;
  
  public TdscdmaSignalStrength() {}
  
  public static final ArrayList<TdscdmaSignalStrength> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 12, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new TdscdmaSignalStrength();
      ((TdscdmaSignalStrength)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 12);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<TdscdmaSignalStrength> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 12);
    while (j < i)
    {
      ((TdscdmaSignalStrength)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 12);
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
    if (paramObject.getClass() != TdscdmaSignalStrength.class) {
      return false;
    }
    paramObject = (TdscdmaSignalStrength)paramObject;
    if (signalStrength != signalStrength) {
      return false;
    }
    if (bitErrorRate != bitErrorRate) {
      return false;
    }
    return rscp == rscp;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(signalStrength))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(bitErrorRate))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rscp))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    signalStrength = paramHwBlob.getInt32(0L + paramLong);
    bitErrorRate = paramHwBlob.getInt32(4L + paramLong);
    rscp = paramHwBlob.getInt32(8L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(12L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".signalStrength = ");
    localStringBuilder.append(signalStrength);
    localStringBuilder.append(", .bitErrorRate = ");
    localStringBuilder.append(bitErrorRate);
    localStringBuilder.append(", .rscp = ");
    localStringBuilder.append(rscp);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, signalStrength);
    paramHwBlob.putInt32(4L + paramLong, bitErrorRate);
    paramHwBlob.putInt32(8L + paramLong, rscp);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(12);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
