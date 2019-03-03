package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellInfoTdscdma
{
  public final CellIdentityTdscdma cellIdentityTdscdma = new CellIdentityTdscdma();
  public final TdscdmaSignalStrength signalStrengthTdscdma = new TdscdmaSignalStrength();
  
  public CellInfoTdscdma() {}
  
  public static final ArrayList<CellInfoTdscdma> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 104, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CellInfoTdscdma();
      ((CellInfoTdscdma)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 104);
      localArrayList.add(localObject);
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
    HwBlob localHwBlob2 = new HwBlob(i * 104);
    while (j < i)
    {
      ((CellInfoTdscdma)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 104);
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
    signalStrengthTdscdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 88L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(104L), 0L);
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
    signalStrengthTdscdma.writeEmbeddedToBlob(paramHwBlob, 88L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(104);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
