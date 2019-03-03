package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaInformationRecords
{
  public final ArrayList<CdmaInformationRecord> infoRec = new ArrayList();
  
  public CdmaInformationRecords() {}
  
  public static final ArrayList<CdmaInformationRecords> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CdmaInformationRecords();
      ((CdmaInformationRecords)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 16);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaInformationRecords> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 16);
    while (j < i)
    {
      ((CdmaInformationRecords)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 16);
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
    if (paramObject.getClass() != CdmaInformationRecords.class) {
      return false;
    }
    paramObject = (CdmaInformationRecords)paramObject;
    return HidlSupport.deepEquals(infoRec, infoRec);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(infoRec)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    int i = paramHwBlob.getInt32(paramLong + 0L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 120, paramHwBlob.handle(), paramLong + 0L + 0L, true);
    infoRec.clear();
    for (int j = 0; j < i; j++)
    {
      paramHwBlob = new CdmaInformationRecord();
      paramHwBlob.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 120);
      infoRec.add(paramHwBlob);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(16L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".infoRec = ");
    localStringBuilder.append(infoRec);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    int i = infoRec.size();
    paramHwBlob.putInt32(paramLong + 0L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 0L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 120);
    while (j < i)
    {
      ((CdmaInformationRecord)infoRec.get(j)).writeEmbeddedToBlob(localHwBlob, j * 120);
      j++;
    }
    paramHwBlob.putBlob(paramLong + 0L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(16);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
