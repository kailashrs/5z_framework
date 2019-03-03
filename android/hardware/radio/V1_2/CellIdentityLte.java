package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellIdentityLte
{
  public int bandwidth;
  public final android.hardware.radio.V1_0.CellIdentityLte base = new android.hardware.radio.V1_0.CellIdentityLte();
  public final CellIdentityOperatorNames operatorNames = new CellIdentityOperatorNames();
  
  public CellIdentityLte() {}
  
  public static final ArrayList<CellIdentityLte> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 88, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CellIdentityLte();
      ((CellIdentityLte)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 88);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellIdentityLte> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 88);
    while (j < i)
    {
      ((CellIdentityLte)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 88);
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
    if (paramObject.getClass() != CellIdentityLte.class) {
      return false;
    }
    paramObject = (CellIdentityLte)paramObject;
    if (!HidlSupport.deepEquals(base, base)) {
      return false;
    }
    if (!HidlSupport.deepEquals(operatorNames, operatorNames)) {
      return false;
    }
    return bandwidth == bandwidth;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(base)), Integer.valueOf(HidlSupport.deepHashCode(operatorNames)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(bandwidth))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    base.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    operatorNames.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 48L + paramLong);
    bandwidth = paramHwBlob.getInt32(80L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(88L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".base = ");
    localStringBuilder.append(base);
    localStringBuilder.append(", .operatorNames = ");
    localStringBuilder.append(operatorNames);
    localStringBuilder.append(", .bandwidth = ");
    localStringBuilder.append(bandwidth);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    base.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    operatorNames.writeEmbeddedToBlob(paramHwBlob, 48L + paramLong);
    paramHwBlob.putInt32(80L + paramLong, bandwidth);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(88);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
