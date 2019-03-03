package android.hardware.radio.V1_0;

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
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CellInfoWcdma();
      ((CellInfoWcdma)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 56);
      localArrayList.add(localObject);
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
    HwBlob localHwBlob2 = new HwBlob(i * 56);
    while (j < i)
    {
      ((CellInfoWcdma)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 56);
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
    signalStrengthWcdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 48L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(56L), 0L);
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
    signalStrengthWcdma.writeEmbeddedToBlob(paramHwBlob, 48L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(56);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
