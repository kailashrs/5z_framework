package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellInfoTdscdma
{
  public final CellIdentityTdscdma cellIdentityTdscdma = new CellIdentityTdscdma();
  public final TdScdmaSignalStrength signalStrengthTdscdma = new TdScdmaSignalStrength();
  
  public CellInfoTdscdma() {}
  
  public static final ArrayList<CellInfoTdscdma> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CellInfoTdscdma localCellInfoTdscdma = new CellInfoTdscdma();
      localCellInfoTdscdma.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 56);
      localArrayList.add(localCellInfoTdscdma);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellInfoTdscdma> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 56);
    while (j < i)
    {
      ((CellInfoTdscdma)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 56);
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
    if (paramObject.getClass() != CellInfoTdscdma.class) {
      return false;
    }
    paramObject = (CellInfoTdscdma)paramObject;
    if (!HidlSupport.deepEquals(cellIdentityTdscdma, cellIdentityTdscdma)) {
      return false;
    }
    return HidlSupport.deepEquals(signalStrengthTdscdma, signalStrengthTdscdma);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(cellIdentityTdscdma)), Integer.valueOf(HidlSupport.deepHashCode(signalStrengthTdscdma)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityTdscdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    signalStrengthTdscdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 48L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(56L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cellIdentityTdscdma = ");
    localStringBuilder.append(cellIdentityTdscdma);
    localStringBuilder.append(", .signalStrengthTdscdma = ");
    localStringBuilder.append(signalStrengthTdscdma);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityTdscdma.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    signalStrengthTdscdma.writeEmbeddedToBlob(paramHwBlob, 48L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(56);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
