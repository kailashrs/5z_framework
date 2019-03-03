package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellIdentityCdma
{
  public int baseStationId;
  public int latitude;
  public int longitude;
  public int networkId;
  public int systemId;
  
  public CellIdentityCdma() {}
  
  public static final ArrayList<CellIdentityCdma> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 20, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CellIdentityCdma localCellIdentityCdma = new CellIdentityCdma();
      localCellIdentityCdma.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 20);
      localArrayList.add(localCellIdentityCdma);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellIdentityCdma> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 20);
    while (j < i)
    {
      ((CellIdentityCdma)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 20);
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
    if (paramObject.getClass() != CellIdentityCdma.class) {
      return false;
    }
    paramObject = (CellIdentityCdma)paramObject;
    if (networkId != networkId) {
      return false;
    }
    if (systemId != systemId) {
      return false;
    }
    if (baseStationId != baseStationId) {
      return false;
    }
    if (longitude != longitude) {
      return false;
    }
    return latitude == latitude;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(networkId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(systemId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(baseStationId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(longitude))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(latitude))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    networkId = paramHwBlob.getInt32(0L + paramLong);
    systemId = paramHwBlob.getInt32(4L + paramLong);
    baseStationId = paramHwBlob.getInt32(8L + paramLong);
    longitude = paramHwBlob.getInt32(12L + paramLong);
    latitude = paramHwBlob.getInt32(16L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(20L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".networkId = ");
    localStringBuilder.append(networkId);
    localStringBuilder.append(", .systemId = ");
    localStringBuilder.append(systemId);
    localStringBuilder.append(", .baseStationId = ");
    localStringBuilder.append(baseStationId);
    localStringBuilder.append(", .longitude = ");
    localStringBuilder.append(longitude);
    localStringBuilder.append(", .latitude = ");
    localStringBuilder.append(latitude);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, networkId);
    paramHwBlob.putInt32(4L + paramLong, systemId);
    paramHwBlob.putInt32(8L + paramLong, baseStationId);
    paramHwBlob.putInt32(12L + paramLong, longitude);
    paramHwBlob.putInt32(16L + paramLong, latitude);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(20);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
