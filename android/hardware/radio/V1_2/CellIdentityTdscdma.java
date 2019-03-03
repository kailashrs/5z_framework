package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellIdentityTdscdma
{
  public final android.hardware.radio.V1_0.CellIdentityTdscdma base = new android.hardware.radio.V1_0.CellIdentityTdscdma();
  public final CellIdentityOperatorNames operatorNames = new CellIdentityOperatorNames();
  public int uarfcn;
  
  public CellIdentityTdscdma() {}
  
  public static final ArrayList<CellIdentityTdscdma> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 88, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CellIdentityTdscdma localCellIdentityTdscdma = new CellIdentityTdscdma();
      localCellIdentityTdscdma.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 88);
      localArrayList.add(localCellIdentityTdscdma);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellIdentityTdscdma> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 88);
    while (j < i)
    {
      ((CellIdentityTdscdma)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 88);
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
    if (paramObject.getClass() != CellIdentityTdscdma.class) {
      return false;
    }
    paramObject = (CellIdentityTdscdma)paramObject;
    if (!HidlSupport.deepEquals(base, base)) {
      return false;
    }
    if (uarfcn != uarfcn) {
      return false;
    }
    return HidlSupport.deepEquals(operatorNames, operatorNames);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(base)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(uarfcn))), Integer.valueOf(HidlSupport.deepHashCode(operatorNames)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    base.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    uarfcn = paramHwBlob.getInt32(48L + paramLong);
    operatorNames.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 56L + paramLong);
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
    localStringBuilder.append(", .uarfcn = ");
    localStringBuilder.append(uarfcn);
    localStringBuilder.append(", .operatorNames = ");
    localStringBuilder.append(operatorNames);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    base.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    paramHwBlob.putInt32(48L + paramLong, uarfcn);
    operatorNames.writeEmbeddedToBlob(paramHwBlob, 56L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(88);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
