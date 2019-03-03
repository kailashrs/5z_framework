package android.hardware.radio.V1_2;

import android.hardware.radio.V1_0.CellInfoType;
import android.hardware.radio.V1_0.TimeStampType;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellInfo
{
  public final ArrayList<CellInfoCdma> cdma = new ArrayList();
  public int cellInfoType;
  public int connectionStatus;
  public final ArrayList<CellInfoGsm> gsm = new ArrayList();
  public final ArrayList<CellInfoLte> lte = new ArrayList();
  public boolean registered;
  public final ArrayList<CellInfoTdscdma> tdscdma = new ArrayList();
  public long timeStamp;
  public int timeStampType;
  public final ArrayList<CellInfoWcdma> wcdma = new ArrayList();
  
  public CellInfo() {}
  
  public static final ArrayList<CellInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 112, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CellInfo();
      ((CellInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 112);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 112);
    while (j < i)
    {
      ((CellInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 112);
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
    if (paramObject.getClass() != CellInfo.class) {
      return false;
    }
    paramObject = (CellInfo)paramObject;
    if (cellInfoType != cellInfoType) {
      return false;
    }
    if (registered != registered) {
      return false;
    }
    if (timeStampType != timeStampType) {
      return false;
    }
    if (timeStamp != timeStamp) {
      return false;
    }
    if (!HidlSupport.deepEquals(gsm, gsm)) {
      return false;
    }
    if (!HidlSupport.deepEquals(cdma, cdma)) {
      return false;
    }
    if (!HidlSupport.deepEquals(lte, lte)) {
      return false;
    }
    if (!HidlSupport.deepEquals(wcdma, wcdma)) {
      return false;
    }
    if (!HidlSupport.deepEquals(tdscdma, tdscdma)) {
      return false;
    }
    return connectionStatus == connectionStatus;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cellInfoType))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(registered))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(timeStampType))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(timeStamp))), Integer.valueOf(HidlSupport.deepHashCode(gsm)), Integer.valueOf(HidlSupport.deepHashCode(cdma)), Integer.valueOf(HidlSupport.deepHashCode(lte)), Integer.valueOf(HidlSupport.deepHashCode(wcdma)), Integer.valueOf(HidlSupport.deepHashCode(tdscdma)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(connectionStatus))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cellInfoType = paramHwBlob.getInt32(paramLong + 0L);
    registered = paramHwBlob.getBool(paramLong + 4L);
    timeStampType = paramHwBlob.getInt32(paramLong + 8L);
    timeStamp = paramHwBlob.getInt64(paramLong + 16L);
    int i = paramHwBlob.getInt32(paramLong + 24L + 8L);
    Object localObject1 = paramHwParcel.readEmbeddedBuffer(i * 96, paramHwBlob.handle(), paramLong + 24L + 0L, true);
    gsm.clear();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      localObject2 = new CellInfoGsm();
      ((CellInfoGsm)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 96);
      gsm.add(localObject2);
    }
    i = paramHwBlob.getInt32(paramLong + 40L + 8L);
    localObject1 = paramHwParcel.readEmbeddedBuffer(i * 80, paramHwBlob.handle(), paramLong + 40L + 0L, true);
    cdma.clear();
    for (k = 0; k < i; k++)
    {
      localObject2 = new CellInfoCdma();
      ((CellInfoCdma)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 80);
      cdma.add(localObject2);
    }
    i = paramHwBlob.getInt32(paramLong + 56L + 8L);
    localObject1 = paramHwParcel.readEmbeddedBuffer(i * 112, paramHwBlob.handle(), paramLong + 56L + 0L, true);
    lte.clear();
    for (k = 0; k < i; k++)
    {
      localObject2 = new CellInfoLte();
      ((CellInfoLte)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 112);
      lte.add(localObject2);
    }
    i = paramHwBlob.getInt32(paramLong + 72L + 8L);
    Object localObject2 = paramHwParcel.readEmbeddedBuffer(i * 96, paramHwBlob.handle(), paramLong + 72L + 0L, true);
    wcdma.clear();
    for (k = 0; k < i; k++)
    {
      localObject1 = new CellInfoWcdma();
      ((CellInfoWcdma)localObject1).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject2, k * 96);
      wcdma.add(localObject1);
    }
    i = paramHwBlob.getInt32(paramLong + 88L + 8L);
    localObject1 = paramHwParcel.readEmbeddedBuffer(i * 104, paramHwBlob.handle(), 0L + (paramLong + 88L), true);
    tdscdma.clear();
    for (k = j; k < i; k++)
    {
      localObject2 = new CellInfoTdscdma();
      ((CellInfoTdscdma)localObject2).readEmbeddedFromParcel(paramHwParcel, (HwBlob)localObject1, k * 104);
      tdscdma.add(localObject2);
    }
    connectionStatus = paramHwBlob.getInt32(paramLong + 104L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(112L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cellInfoType = ");
    localStringBuilder.append(CellInfoType.toString(cellInfoType));
    localStringBuilder.append(", .registered = ");
    localStringBuilder.append(registered);
    localStringBuilder.append(", .timeStampType = ");
    localStringBuilder.append(TimeStampType.toString(timeStampType));
    localStringBuilder.append(", .timeStamp = ");
    localStringBuilder.append(timeStamp);
    localStringBuilder.append(", .gsm = ");
    localStringBuilder.append(gsm);
    localStringBuilder.append(", .cdma = ");
    localStringBuilder.append(cdma);
    localStringBuilder.append(", .lte = ");
    localStringBuilder.append(lte);
    localStringBuilder.append(", .wcdma = ");
    localStringBuilder.append(wcdma);
    localStringBuilder.append(", .tdscdma = ");
    localStringBuilder.append(tdscdma);
    localStringBuilder.append(", .connectionStatus = ");
    localStringBuilder.append(CellConnectionStatus.toString(connectionStatus));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, cellInfoType);
    paramHwBlob.putBool(paramLong + 4L, registered);
    paramHwBlob.putInt32(paramLong + 8L, timeStampType);
    paramHwBlob.putInt64(paramLong + 16L, timeStamp);
    int i = gsm.size();
    paramHwBlob.putInt32(paramLong + 24L + 8L, i);
    paramHwBlob.putBool(paramLong + 24L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 96);
    for (int j = 0; j < i; j++) {
      ((CellInfoGsm)gsm.get(j)).writeEmbeddedToBlob(localHwBlob, j * 96);
    }
    paramHwBlob.putBlob(paramLong + 24L + 0L, localHwBlob);
    i = cdma.size();
    paramHwBlob.putInt32(paramLong + 40L + 8L, i);
    paramHwBlob.putBool(paramLong + 40L + 12L, false);
    localHwBlob = new HwBlob(i * 80);
    for (j = 0; j < i; j++) {
      ((CellInfoCdma)cdma.get(j)).writeEmbeddedToBlob(localHwBlob, j * 80);
    }
    paramHwBlob.putBlob(paramLong + 40L + 0L, localHwBlob);
    i = lte.size();
    paramHwBlob.putInt32(paramLong + 56L + 8L, i);
    paramHwBlob.putBool(paramLong + 56L + 12L, false);
    localHwBlob = new HwBlob(i * 112);
    for (j = 0; j < i; j++) {
      ((CellInfoLte)lte.get(j)).writeEmbeddedToBlob(localHwBlob, j * 112);
    }
    paramHwBlob.putBlob(paramLong + 56L + 0L, localHwBlob);
    i = wcdma.size();
    paramHwBlob.putInt32(paramLong + 72L + 8L, i);
    paramHwBlob.putBool(paramLong + 72L + 12L, false);
    localHwBlob = new HwBlob(i * 96);
    for (j = 0; j < i; j++) {
      ((CellInfoWcdma)wcdma.get(j)).writeEmbeddedToBlob(localHwBlob, j * 96);
    }
    paramHwBlob.putBlob(paramLong + 72L + 0L, localHwBlob);
    i = tdscdma.size();
    paramHwBlob.putInt32(paramLong + 88L + 8L, i);
    j = 0;
    paramHwBlob.putBool(paramLong + 88L + 12L, false);
    localHwBlob = new HwBlob(i * 104);
    while (j < i)
    {
      ((CellInfoTdscdma)tdscdma.get(j)).writeEmbeddedToBlob(localHwBlob, j * 104);
      j++;
    }
    paramHwBlob.putBlob(paramLong + 88L + 0L, localHwBlob);
    paramHwBlob.putInt32(paramLong + 104L, connectionStatus);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(112);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
