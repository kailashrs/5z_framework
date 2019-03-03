package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaT53AudioControlInfoRecord
{
  public byte downLink;
  public byte upLink;
  
  public CdmaT53AudioControlInfoRecord() {}
  
  public static final ArrayList<CdmaT53AudioControlInfoRecord> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 2, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CdmaT53AudioControlInfoRecord localCdmaT53AudioControlInfoRecord = new CdmaT53AudioControlInfoRecord();
      localCdmaT53AudioControlInfoRecord.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 2);
      localArrayList.add(localCdmaT53AudioControlInfoRecord);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaT53AudioControlInfoRecord> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 2);
    while (j < i)
    {
      ((CdmaT53AudioControlInfoRecord)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 2);
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
    if (paramObject.getClass() != CdmaT53AudioControlInfoRecord.class) {
      return false;
    }
    paramObject = (CdmaT53AudioControlInfoRecord)paramObject;
    if (upLink != upLink) {
      return false;
    }
    return downLink == downLink;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(upLink))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(downLink))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    upLink = paramHwBlob.getInt8(0L + paramLong);
    downLink = paramHwBlob.getInt8(1L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(2L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".upLink = ");
    localStringBuilder.append(upLink);
    localStringBuilder.append(", .downLink = ");
    localStringBuilder.append(downLink);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt8(0L + paramLong, upLink);
    paramHwBlob.putInt8(1L + paramLong, downLink);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(2);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
