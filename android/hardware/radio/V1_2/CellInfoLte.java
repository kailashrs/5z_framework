package android.hardware.radio.V1_2;

import android.hardware.radio.V1_0.LteSignalStrength;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellInfoLte
{
  public final CellIdentityLte cellIdentityLte = new CellIdentityLte();
  public final LteSignalStrength signalStrengthLte = new LteSignalStrength();
  
  public CellInfoLte() {}
  
  public static final ArrayList<CellInfoLte> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 112, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CellInfoLte();
      ((CellInfoLte)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 112);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellInfoLte> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 112);
    while (j < i)
    {
      ((CellInfoLte)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 112);
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
    if (paramObject.getClass() != CellInfoLte.class) {
      return false;
    }
    paramObject = (CellInfoLte)paramObject;
    if (!HidlSupport.deepEquals(cellIdentityLte, cellIdentityLte)) {
      return false;
    }
    return HidlSupport.deepEquals(signalStrengthLte, signalStrengthLte);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(cellIdentityLte)), Integer.valueOf(HidlSupport.deepHashCode(signalStrengthLte)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityLte.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    signalStrengthLte.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 88L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(112L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cellIdentityLte = ");
    localStringBuilder.append(cellIdentityLte);
    localStringBuilder.append(", .signalStrengthLte = ");
    localStringBuilder.append(signalStrengthLte);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    cellIdentityLte.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    signalStrengthLte.writeEmbeddedToBlob(paramHwBlob, 88L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(112);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
