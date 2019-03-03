package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaCallWaiting
{
  public String name = new String();
  public String number = new String();
  public int numberPlan;
  public int numberPresentation;
  public int numberType;
  public final CdmaSignalInfoRecord signalInfoRecord = new CdmaSignalInfoRecord();
  
  public CdmaCallWaiting() {}
  
  public static final ArrayList<CdmaCallWaiting> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CdmaCallWaiting localCdmaCallWaiting = new CdmaCallWaiting();
      localCdmaCallWaiting.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 56);
      localArrayList.add(localCdmaCallWaiting);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaCallWaiting> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 56);
    while (j < i)
    {
      ((CdmaCallWaiting)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 56);
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
    if (paramObject.getClass() != CdmaCallWaiting.class) {
      return false;
    }
    paramObject = (CdmaCallWaiting)paramObject;
    if (!HidlSupport.deepEquals(number, number)) {
      return false;
    }
    if (numberPresentation != numberPresentation) {
      return false;
    }
    if (!HidlSupport.deepEquals(name, name)) {
      return false;
    }
    if (!HidlSupport.deepEquals(signalInfoRecord, signalInfoRecord)) {
      return false;
    }
    if (numberType != numberType) {
      return false;
    }
    return numberPlan == numberPlan;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(number)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(numberPresentation))), Integer.valueOf(HidlSupport.deepHashCode(name)), Integer.valueOf(HidlSupport.deepHashCode(signalInfoRecord)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(numberType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(numberPlan))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    number = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(number.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    numberPresentation = paramHwBlob.getInt32(paramLong + 16L);
    name = paramHwBlob.getString(paramLong + 24L);
    paramHwParcel.readEmbeddedBuffer(name.getBytes().length + 1, paramHwBlob.handle(), paramLong + 24L + 0L, false);
    signalInfoRecord.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, paramLong + 40L);
    numberType = paramHwBlob.getInt32(paramLong + 44L);
    numberPlan = paramHwBlob.getInt32(paramLong + 48L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(56L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".number = ");
    localStringBuilder.append(number);
    localStringBuilder.append(", .numberPresentation = ");
    localStringBuilder.append(CdmaCallWaitingNumberPresentation.toString(numberPresentation));
    localStringBuilder.append(", .name = ");
    localStringBuilder.append(name);
    localStringBuilder.append(", .signalInfoRecord = ");
    localStringBuilder.append(signalInfoRecord);
    localStringBuilder.append(", .numberType = ");
    localStringBuilder.append(CdmaCallWaitingNumberType.toString(numberType));
    localStringBuilder.append(", .numberPlan = ");
    localStringBuilder.append(CdmaCallWaitingNumberPlan.toString(numberPlan));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, number);
    paramHwBlob.putInt32(16L + paramLong, numberPresentation);
    paramHwBlob.putString(24L + paramLong, name);
    signalInfoRecord.writeEmbeddedToBlob(paramHwBlob, 40L + paramLong);
    paramHwBlob.putInt32(44L + paramLong, numberType);
    paramHwBlob.putInt32(48L + paramLong, numberPlan);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(56);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
