package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellIdentity
{
  public final ArrayList<CellIdentityCdma> cellIdentityCdma = new ArrayList();
  public final ArrayList<CellIdentityGsm> cellIdentityGsm = new ArrayList();
  public final ArrayList<CellIdentityLte> cellIdentityLte = new ArrayList();
  public final ArrayList<CellIdentityTdscdma> cellIdentityTdscdma = new ArrayList();
  public final ArrayList<CellIdentityWcdma> cellIdentityWcdma = new ArrayList();
  public int cellInfoType;
  
  public CellIdentity() {}
  
  public static final ArrayList<CellIdentity> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 88, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CellIdentity();
      ((CellIdentity)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 88);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellIdentity> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 88);
    while (j < i)
    {
      ((CellIdentity)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 88);
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
    if (paramObject.getClass() != CellIdentity.class) {
      return false;
    }
    paramObject = (CellIdentity)paramObject;
    if (cellInfoType != cellInfoType) {
      return false;
    }
    if (!HidlSupport.deepEquals(cellIdentityGsm, cellIdentityGsm)) {
      return false;
    }
    if (!HidlSupport.deepEquals(cellIdentityWcdma, cellIdentityWcdma)) {
      return false;
    }
    if (!HidlSupport.deepEquals(cellIdentityCdma, cellIdentityCdma)) {
      return false;
    }
    if (!HidlSupport.deepEquals(cellIdentityLte, cellIdentityLte)) {
      return false;
    }
    return HidlSupport.deepEquals(cellIdentityTdscdma, cellIdentityTdscdma);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cellInfoType))), Integer.valueOf(HidlSupport.deepHashCode(cellIdentityGsm)), Integer.valueOf(HidlSupport.deepHashCode(cellIdentityWcdma)), Integer.valueOf(HidlSupport.deepHashCode(cellIdentityCdma)), Integer.valueOf(HidlSupport.deepHashCode(cellIdentityLte)), Integer.valueOf(HidlSupport.deepHashCode(cellIdentityTdscdma)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cellInfoType = paramHwBlob.getInt32(paramLong + 0L);
    int i = paramHwBlob.getInt32(paramLong + 8L + 8L);
    Object localObject1 = paramHwParcel.readEmbeddedBuffer(i * 48, paramHwBlob.handle(), paramLong + 8L + 0L, true);
    cellIdentityGsm.clear();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      localObject2 = new CellIdentityGsm();
      ((CellIdentityGsm)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 48);
      cellIdentityGsm.add(localObject2);
    }
    i = paramHwBlob.getInt32(paramLong + 24L + 8L);
    Object localObject2 = paramHwParcel.readEmbeddedBuffer(i * 48, paramHwBlob.handle(), paramLong + 24L + 0L, true);
    cellIdentityWcdma.clear();
    for (k = 0; k < i; k++)
    {
      localObject1 = new CellIdentityWcdma();
      ((CellIdentityWcdma)localObject1).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject2, k * 48);
      cellIdentityWcdma.add(localObject1);
    }
    i = paramHwBlob.getInt32(paramLong + 40L + 8L);
    localObject2 = paramHwParcel.readEmbeddedBuffer(i * 20, paramHwBlob.handle(), paramLong + 40L + 0L, true);
    cellIdentityCdma.clear();
    for (k = 0; k < i; k++)
    {
      localObject1 = new CellIdentityCdma();
      ((CellIdentityCdma)localObject1).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject2, k * 20);
      cellIdentityCdma.add(localObject1);
    }
    i = paramHwBlob.getInt32(paramLong + 56L + 8L);
    localObject2 = paramHwParcel.readEmbeddedBuffer(i * 48, paramHwBlob.handle(), paramLong + 56L + 0L, true);
    cellIdentityLte.clear();
    for (k = 0; k < i; k++)
    {
      localObject1 = new CellIdentityLte();
      ((CellIdentityLte)localObject1).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject2, k * 48);
      cellIdentityLte.add(localObject1);
    }
    i = paramHwBlob.getInt32(paramLong + 72L + 8L);
    localObject2 = paramHwParcel.readEmbeddedBuffer(i * 48, paramHwBlob.handle(), 0L + (paramLong + 72L), true);
    cellIdentityTdscdma.clear();
    for (k = j; k < i; k++)
    {
      paramHwBlob = new CellIdentityTdscdma();
      paramHwBlob.readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject2, k * 48);
      cellIdentityTdscdma.add(paramHwBlob);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(88L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cellInfoType = ");
    localStringBuilder.append(CellInfoType.toString(cellInfoType));
    localStringBuilder.append(", .cellIdentityGsm = ");
    localStringBuilder.append(cellIdentityGsm);
    localStringBuilder.append(", .cellIdentityWcdma = ");
    localStringBuilder.append(cellIdentityWcdma);
    localStringBuilder.append(", .cellIdentityCdma = ");
    localStringBuilder.append(cellIdentityCdma);
    localStringBuilder.append(", .cellIdentityLte = ");
    localStringBuilder.append(cellIdentityLte);
    localStringBuilder.append(", .cellIdentityTdscdma = ");
    localStringBuilder.append(cellIdentityTdscdma);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, cellInfoType);
    int i = cellIdentityGsm.size();
    paramHwBlob.putInt32(paramLong + 8L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 8L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 48);
    for (int k = 0; k < i; k++) {
      ((CellIdentityGsm)cellIdentityGsm.get(k)).writeEmbeddedToBlob(localHwBlob, k * 48);
    }
    paramHwBlob.putBlob(paramLong + 8L + 0L, localHwBlob);
    i = cellIdentityWcdma.size();
    paramHwBlob.putInt32(paramLong + 24L + 8L, i);
    paramHwBlob.putBool(paramLong + 24L + 12L, false);
    localHwBlob = new HwBlob(i * 48);
    for (k = 0; k < i; k++) {
      ((CellIdentityWcdma)cellIdentityWcdma.get(k)).writeEmbeddedToBlob(localHwBlob, k * 48);
    }
    paramHwBlob.putBlob(paramLong + 24L + 0L, localHwBlob);
    i = cellIdentityCdma.size();
    paramHwBlob.putInt32(paramLong + 40L + 8L, i);
    paramHwBlob.putBool(paramLong + 40L + 12L, false);
    localHwBlob = new HwBlob(i * 20);
    for (k = 0; k < i; k++) {
      ((CellIdentityCdma)cellIdentityCdma.get(k)).writeEmbeddedToBlob(localHwBlob, k * 20);
    }
    paramHwBlob.putBlob(paramLong + 40L + 0L, localHwBlob);
    i = cellIdentityLte.size();
    paramHwBlob.putInt32(paramLong + 56L + 8L, i);
    paramHwBlob.putBool(paramLong + 56L + 12L, false);
    localHwBlob = new HwBlob(i * 48);
    for (k = 0; k < i; k++) {
      ((CellIdentityLte)cellIdentityLte.get(k)).writeEmbeddedToBlob(localHwBlob, k * 48);
    }
    paramHwBlob.putBlob(paramLong + 56L + 0L, localHwBlob);
    i = cellIdentityTdscdma.size();
    paramHwBlob.putInt32(paramLong + 72L + 8L, i);
    paramHwBlob.putBool(paramLong + 72L + 12L, false);
    localHwBlob = new HwBlob(i * 48);
    for (k = j; k < i; k++) {
      ((CellIdentityTdscdma)cellIdentityTdscdma.get(k)).writeEmbeddedToBlob(localHwBlob, k * 48);
    }
    paramHwBlob.putBlob(paramLong + 72L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(88);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
