package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellInfoGsm
{
  public final CellIdentityGsm cellIdentityGsm = new CellIdentityGsm();
  public final GsmSignalStrength signalStrengthGsm = new GsmSignalStrength();
  
  public CellInfoGsm() {}
  
  public static final ArrayList<CellInfoGsm> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 64, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CellInfoGsm localCellInfoGsm = new CellInfoGsm();
      localCellInfoGsm.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 64);
      localArrayList.add(localCellInfoGsm);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellInfoGsm> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 64);
    while (j < i)
    {
      ((CellInfoGsm)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 64);
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
    if (paramObject.getClass() != CellInfoGsm.class) {
      return false;
    }
    paramObject = (CellInfoGsm)paramObject;
    if (!HidlSupport.deepEquals(cellIdentityGsm, cellIdentityGsm)) {
      return false;
    }
    return HidlSupport.deepEquals(signalStrengthGsm, signalStrengthGsm);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(cellIdentityGsm)), Integer.valueOf(HidlSupport.deepHashCode(signalStrengthGsm)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityGsm.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    signalStrengthGsm.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 48L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(64L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cellIdentityGsm = ");
    localStringBuilder.append(cellIdentityGsm);
    localStringBuilder.append(", .signalStrengthGsm = ");
    localStringBuilder.append(signalStrengthGsm);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityGsm.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    signalStrengthGsm.writeEmbeddedToBlob(paramHwBlob, 48L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(64);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
