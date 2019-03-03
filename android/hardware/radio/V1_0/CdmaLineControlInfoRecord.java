package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaLineControlInfoRecord
{
  public byte lineCtrlPolarityIncluded;
  public byte lineCtrlPowerDenial;
  public byte lineCtrlReverse;
  public byte lineCtrlToggle;
  
  public CdmaLineControlInfoRecord() {}
  
  public static final ArrayList<CdmaLineControlInfoRecord> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 4, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CdmaLineControlInfoRecord();
      ((CdmaLineControlInfoRecord)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 4);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaLineControlInfoRecord> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 4);
    while (j < i)
    {
      ((CdmaLineControlInfoRecord)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 4);
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
    if (paramObject.getClass() != CdmaLineControlInfoRecord.class) {
      return false;
    }
    paramObject = (CdmaLineControlInfoRecord)paramObject;
    if (lineCtrlPolarityIncluded != lineCtrlPolarityIncluded) {
      return false;
    }
    if (lineCtrlToggle != lineCtrlToggle) {
      return false;
    }
    if (lineCtrlReverse != lineCtrlReverse) {
      return false;
    }
    return lineCtrlPowerDenial == lineCtrlPowerDenial;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(lineCtrlPolarityIncluded))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(lineCtrlToggle))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(lineCtrlReverse))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(lineCtrlPowerDenial))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    lineCtrlPolarityIncluded = paramHwBlob.getInt8(0L + paramLong);
    lineCtrlToggle = paramHwBlob.getInt8(1L + paramLong);
    lineCtrlReverse = paramHwBlob.getInt8(2L + paramLong);
    lineCtrlPowerDenial = paramHwBlob.getInt8(3L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(4L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".lineCtrlPolarityIncluded = ");
    localStringBuilder.append(lineCtrlPolarityIncluded);
    localStringBuilder.append(", .lineCtrlToggle = ");
    localStringBuilder.append(lineCtrlToggle);
    localStringBuilder.append(", .lineCtrlReverse = ");
    localStringBuilder.append(lineCtrlReverse);
    localStringBuilder.append(", .lineCtrlPowerDenial = ");
    localStringBuilder.append(lineCtrlPowerDenial);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt8(0L + paramLong, lineCtrlPolarityIncluded);
    paramHwBlob.putInt8(1L + paramLong, lineCtrlToggle);
    paramHwBlob.putInt8(2L + paramLong, lineCtrlReverse);
    paramHwBlob.putInt8(3L + paramLong, lineCtrlPowerDenial);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(4);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
