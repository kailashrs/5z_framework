package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaNumberInfoRecord
{
  public String number = new String();
  public byte numberPlan;
  public byte numberType;
  public byte pi;
  public byte si;
  
  public CdmaNumberInfoRecord() {}
  
  public static final ArrayList<CdmaNumberInfoRecord> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CdmaNumberInfoRecord localCdmaNumberInfoRecord = new CdmaNumberInfoRecord();
      localCdmaNumberInfoRecord.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localCdmaNumberInfoRecord);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaNumberInfoRecord> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((CdmaNumberInfoRecord)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != CdmaNumberInfoRecord.class) {
      return false;
    }
    paramObject = (CdmaNumberInfoRecord)paramObject;
    if (!HidlSupport.deepEquals(number, number)) {
      return false;
    }
    if (numberType != numberType) {
      return false;
    }
    if (numberPlan != numberPlan) {
      return false;
    }
    if (pi != pi) {
      return false;
    }
    return si == si;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(number)), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(numberType))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(numberPlan))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(pi))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(si))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    number = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(number.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    numberType = paramHwBlob.getInt8(16L + paramLong);
    numberPlan = paramHwBlob.getInt8(17L + paramLong);
    pi = paramHwBlob.getInt8(18L + paramLong);
    si = paramHwBlob.getInt8(19L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(24L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".number = ");
    localStringBuilder.append(number);
    localStringBuilder.append(", .numberType = ");
    localStringBuilder.append(numberType);
    localStringBuilder.append(", .numberPlan = ");
    localStringBuilder.append(numberPlan);
    localStringBuilder.append(", .pi = ");
    localStringBuilder.append(pi);
    localStringBuilder.append(", .si = ");
    localStringBuilder.append(si);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, number);
    paramHwBlob.putInt8(16L + paramLong, numberType);
    paramHwBlob.putInt8(17L + paramLong, numberPlan);
    paramHwBlob.putInt8(18L + paramLong, pi);
    paramHwBlob.putInt8(19L + paramLong, si);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(24);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
