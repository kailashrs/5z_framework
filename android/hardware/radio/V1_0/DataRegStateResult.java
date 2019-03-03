package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class DataRegStateResult
{
  public final CellIdentity cellIdentity = new CellIdentity();
  public int maxDataCalls;
  public int rat;
  public int reasonDataDenied;
  public int regState;
  
  public DataRegStateResult() {}
  
  public static final ArrayList<DataRegStateResult> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 104, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      DataRegStateResult localDataRegStateResult = new DataRegStateResult();
      localDataRegStateResult.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 104);
      localArrayList.add(localDataRegStateResult);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<DataRegStateResult> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 104);
    while (j < i)
    {
      ((DataRegStateResult)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 104);
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
    if (paramObject.getClass() != DataRegStateResult.class) {
      return false;
    }
    paramObject = (DataRegStateResult)paramObject;
    if (regState != regState) {
      return false;
    }
    if (rat != rat) {
      return false;
    }
    if (reasonDataDenied != reasonDataDenied) {
      return false;
    }
    if (maxDataCalls != maxDataCalls) {
      return false;
    }
    return HidlSupport.deepEquals(cellIdentity, cellIdentity);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(regState))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rat))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(reasonDataDenied))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxDataCalls))), Integer.valueOf(HidlSupport.deepHashCode(cellIdentity)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    regState = paramHwBlob.getInt32(0L + paramLong);
    rat = paramHwBlob.getInt32(4L + paramLong);
    reasonDataDenied = paramHwBlob.getInt32(8L + paramLong);
    maxDataCalls = paramHwBlob.getInt32(12L + paramLong);
    cellIdentity.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 16L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(104L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".regState = ");
    localStringBuilder.append(RegState.toString(regState));
    localStringBuilder.append(", .rat = ");
    localStringBuilder.append(rat);
    localStringBuilder.append(", .reasonDataDenied = ");
    localStringBuilder.append(reasonDataDenied);
    localStringBuilder.append(", .maxDataCalls = ");
    localStringBuilder.append(maxDataCalls);
    localStringBuilder.append(", .cellIdentity = ");
    localStringBuilder.append(cellIdentity);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, regState);
    paramHwBlob.putInt32(4L + paramLong, rat);
    paramHwBlob.putInt32(8L + paramLong, reasonDataDenied);
    paramHwBlob.putInt32(12L + paramLong, maxDataCalls);
    cellIdentity.writeEmbeddedToBlob(paramHwBlob, 16L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(104);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
