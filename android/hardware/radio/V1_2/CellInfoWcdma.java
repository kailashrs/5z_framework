package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellInfoWcdma
{
  public final CellIdentityWcdma cellIdentityWcdma = new CellIdentityWcdma();
  public final WcdmaSignalStrength signalStrengthWcdma = new WcdmaSignalStrength();
  
  public CellInfoWcdma() {}
  
  public static final ArrayList<CellInfoWcdma> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 96, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CellInfoWcdma localCellInfoWcdma = new CellInfoWcdma();
      localCellInfoWcdma.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 96);
      localArrayList.add(localCellInfoWcdma);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellInfoWcdma> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 96);
    while (j < i)
    {
      ((CellInfoWcdma)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 96);
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
    if (paramObject.getClass() != CellInfoWcdma.class) {
      return false;
    }
    paramObject = (CellInfoWcdma)paramObject;
    if (!HidlSupport.deepEquals(cellIdentityWcdma, cellIdentityWcdma)) {
      return false;
    }
    return HidlSupport.deepEquals(signalStrengthWcdma, signalStrengthWcdma);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(cellIdentityWcdma)), Integer.valueOf(HidlSupport.deepHashCode(signalStrengthWcdma)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityWcdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    signalStrengthWcdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 80L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(96L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cellIdentityWcdma = ");
    localStringBuilder.append(cellIdentityWcdma);
    localStringBuilder.append(", .signalStrengthWcdma = ");
    localStringBuilder.append(signalStrengthWcdma);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityWcdma.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    signalStrengthWcdma.writeEmbeddedToBlob(paramHwBlob, 80L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(96);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
