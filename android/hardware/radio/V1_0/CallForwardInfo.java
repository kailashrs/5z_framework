package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CallForwardInfo
{
  public String number = new String();
  public int reason;
  public int serviceClass;
  public int status;
  public int timeSeconds;
  public int toa;
  
  public CallForwardInfo() {}
  
  public static final ArrayList<CallForwardInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 40, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CallForwardInfo();
      ((CallForwardInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 40);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CallForwardInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 40);
    while (j < i)
    {
      ((CallForwardInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 40);
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
    if (paramObject.getClass() != CallForwardInfo.class) {
      return false;
    }
    paramObject = (CallForwardInfo)paramObject;
    if (status != status) {
      return false;
    }
    if (reason != reason) {
      return false;
    }
    if (serviceClass != serviceClass) {
      return false;
    }
    if (toa != toa) {
      return false;
    }
    if (!HidlSupport.deepEquals(number, number)) {
      return false;
    }
    return timeSeconds == timeSeconds;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(status))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(reason))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(serviceClass))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(toa))), Integer.valueOf(HidlSupport.deepHashCode(number)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(timeSeconds))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    status = paramHwBlob.getInt32(paramLong + 0L);
    reason = paramHwBlob.getInt32(paramLong + 4L);
    serviceClass = paramHwBlob.getInt32(paramLong + 8L);
    toa = paramHwBlob.getInt32(paramLong + 12L);
    number = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(number.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    timeSeconds = paramHwBlob.getInt32(paramLong + 32L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(40L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".status = ");
    localStringBuilder.append(CallForwardInfoStatus.toString(status));
    localStringBuilder.append(", .reason = ");
    localStringBuilder.append(reason);
    localStringBuilder.append(", .serviceClass = ");
    localStringBuilder.append(serviceClass);
    localStringBuilder.append(", .toa = ");
    localStringBuilder.append(toa);
    localStringBuilder.append(", .number = ");
    localStringBuilder.append(number);
    localStringBuilder.append(", .timeSeconds = ");
    localStringBuilder.append(timeSeconds);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, status);
    paramHwBlob.putInt32(4L + paramLong, reason);
    paramHwBlob.putInt32(8L + paramLong, serviceClass);
    paramHwBlob.putInt32(12L + paramLong, toa);
    paramHwBlob.putString(16L + paramLong, number);
    paramHwBlob.putInt32(32L + paramLong, timeSeconds);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(40);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
