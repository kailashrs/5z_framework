package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaSignalInfoRecord
{
  public byte alertPitch;
  public boolean isPresent;
  public byte signal;
  public byte signalType;
  
  public CdmaSignalInfoRecord() {}
  
  public static final ArrayList<CdmaSignalInfoRecord> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 4, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CdmaSignalInfoRecord localCdmaSignalInfoRecord = new CdmaSignalInfoRecord();
      localCdmaSignalInfoRecord.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 4);
      localArrayList.add(localCdmaSignalInfoRecord);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaSignalInfoRecord> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 4);
    while (j < i)
    {
      ((CdmaSignalInfoRecord)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 4);
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
    if (paramObject.getClass() != CdmaSignalInfoRecord.class) {
      return false;
    }
    paramObject = (CdmaSignalInfoRecord)paramObject;
    if (isPresent != isPresent) {
      return false;
    }
    if (signalType != signalType) {
      return false;
    }
    if (alertPitch != alertPitch) {
      return false;
    }
    return signal == signal;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(isPresent))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(signalType))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(alertPitch))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(signal))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    isPresent = paramHwBlob.getBool(0L + paramLong);
    signalType = paramHwBlob.getInt8(1L + paramLong);
    alertPitch = paramHwBlob.getInt8(2L + paramLong);
    signal = paramHwBlob.getInt8(3L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(4L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".isPresent = ");
    localStringBuilder.append(isPresent);
    localStringBuilder.append(", .signalType = ");
    localStringBuilder.append(signalType);
    localStringBuilder.append(", .alertPitch = ");
    localStringBuilder.append(alertPitch);
    localStringBuilder.append(", .signal = ");
    localStringBuilder.append(signal);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putBool(0L + paramLong, isPresent);
    paramHwBlob.putInt8(1L + paramLong, signalType);
    paramHwBlob.putInt8(2L + paramLong, alertPitch);
    paramHwBlob.putInt8(3L + paramLong, signal);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(4);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
