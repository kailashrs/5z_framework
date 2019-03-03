package android.hardware.radio.V1_2;

import android.hardware.radio.V1_0.CdmaSignalStrength;
import android.hardware.radio.V1_0.EvdoSignalStrength;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellInfoCdma
{
  public final CellIdentityCdma cellIdentityCdma = new CellIdentityCdma();
  public final CdmaSignalStrength signalStrengthCdma = new CdmaSignalStrength();
  public final EvdoSignalStrength signalStrengthEvdo = new EvdoSignalStrength();
  
  public CellInfoCdma() {}
  
  public static final ArrayList<CellInfoCdma> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 80, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CellInfoCdma localCellInfoCdma = new CellInfoCdma();
      localCellInfoCdma.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 80);
      localArrayList.add(localCellInfoCdma);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellInfoCdma> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 80);
    while (j < i)
    {
      ((CellInfoCdma)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 80);
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
    if (paramObject.getClass() != CellInfoCdma.class) {
      return false;
    }
    paramObject = (CellInfoCdma)paramObject;
    if (!HidlSupport.deepEquals(cellIdentityCdma, cellIdentityCdma)) {
      return false;
    }
    if (!HidlSupport.deepEquals(signalStrengthCdma, signalStrengthCdma)) {
      return false;
    }
    return HidlSupport.deepEquals(signalStrengthEvdo, signalStrengthEvdo);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(cellIdentityCdma)), Integer.valueOf(HidlSupport.deepHashCode(signalStrengthCdma)), Integer.valueOf(HidlSupport.deepHashCode(signalStrengthEvdo)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityCdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    signalStrengthCdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 56L + paramLong);
    signalStrengthEvdo.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 64L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(80L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cellIdentityCdma = ");
    localStringBuilder.append(cellIdentityCdma);
    localStringBuilder.append(", .signalStrengthCdma = ");
    localStringBuilder.append(signalStrengthCdma);
    localStringBuilder.append(", .signalStrengthEvdo = ");
    localStringBuilder.append(signalStrengthEvdo);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityCdma.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    signalStrengthCdma.writeEmbeddedToBlob(paramHwBlob, 56L + paramLong);
    signalStrengthEvdo.writeEmbeddedToBlob(paramHwBlob, 64L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(80);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
