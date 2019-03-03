package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class WcdmaSignalStrength
{
  public final android.hardware.radio.V1_0.WcdmaSignalStrength base = new android.hardware.radio.V1_0.WcdmaSignalStrength();
  public int ecno;
  public int rscp;
  
  public WcdmaSignalStrength() {}
  
  public static final ArrayList<WcdmaSignalStrength> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new WcdmaSignalStrength();
      ((WcdmaSignalStrength)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 16);
      localArrayList.add(localObject);
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
    HwBlob localHwBlob2 = new HwBlob(i * 16);
    while (j < i)
    {
      ((WcdmaSignalStrength)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 16);
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
    if (!HidlSupport.deepEquals(base, base)) {
      return false;
    }
    if (rscp != rscp) {
      return false;
    }
    return ecno == ecno;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(base)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rscp))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(ecno))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    base.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    rscp = paramHwBlob.getInt32(8L + paramLong);
    ecno = paramHwBlob.getInt32(12L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(16L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".base = ");
    localStringBuilder.append(base);
    localStringBuilder.append(", .rscp = ");
    localStringBuilder.append(rscp);
    localStringBuilder.append(", .ecno = ");
    localStringBuilder.append(ecno);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    base.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    paramHwBlob.putInt32(8L + paramLong, rscp);
    paramHwBlob.putInt32(12L + paramLong, ecno);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(16);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
