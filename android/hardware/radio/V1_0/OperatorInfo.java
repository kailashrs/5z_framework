package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class OperatorInfo
{
  public String alphaLong = new String();
  public String alphaShort = new String();
  public String operatorNumeric = new String();
  public int status;
  
  public OperatorInfo() {}
  
  public static final ArrayList<OperatorInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new OperatorInfo();
      ((OperatorInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 56);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<OperatorInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 56);
    while (j < i)
    {
      ((OperatorInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 56);
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
    if (paramObject.getClass() != OperatorInfo.class) {
      return false;
    }
    paramObject = (OperatorInfo)paramObject;
    if (!HidlSupport.deepEquals(alphaLong, alphaLong)) {
      return false;
    }
    if (!HidlSupport.deepEquals(alphaShort, alphaShort)) {
      return false;
    }
    if (!HidlSupport.deepEquals(operatorNumeric, operatorNumeric)) {
      return false;
    }
    return status == status;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(alphaLong)), Integer.valueOf(HidlSupport.deepHashCode(alphaShort)), Integer.valueOf(HidlSupport.deepHashCode(operatorNumeric)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(status))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    alphaLong = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(alphaLong.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    alphaShort = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(alphaShort.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    operatorNumeric = paramHwBlob.getString(paramLong + 32L);
    paramHwParcel.readEmbeddedBuffer(operatorNumeric.getBytes().length + 1, paramHwBlob.handle(), paramLong + 32L + 0L, false);
    status = paramHwBlob.getInt32(paramLong + 48L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(56L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".alphaLong = ");
    localStringBuilder.append(alphaLong);
    localStringBuilder.append(", .alphaShort = ");
    localStringBuilder.append(alphaShort);
    localStringBuilder.append(", .operatorNumeric = ");
    localStringBuilder.append(operatorNumeric);
    localStringBuilder.append(", .status = ");
    localStringBuilder.append(OperatorStatus.toString(status));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, alphaLong);
    paramHwBlob.putString(16L + paramLong, alphaShort);
    paramHwBlob.putString(32L + paramLong, operatorNumeric);
    paramHwBlob.putInt32(48L + paramLong, status);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(56);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
