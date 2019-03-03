package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SuppSvcNotification
{
  public int code;
  public int index;
  public boolean isMT;
  public String number = new String();
  public int type;
  
  public SuppSvcNotification() {}
  
  public static final ArrayList<SuppSvcNotification> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      SuppSvcNotification localSuppSvcNotification = new SuppSvcNotification();
      localSuppSvcNotification.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localSuppSvcNotification);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SuppSvcNotification> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((SuppSvcNotification)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != SuppSvcNotification.class) {
      return false;
    }
    paramObject = (SuppSvcNotification)paramObject;
    if (isMT != isMT) {
      return false;
    }
    if (code != code) {
      return false;
    }
    if (index != index) {
      return false;
    }
    if (type != type) {
      return false;
    }
    return HidlSupport.deepEquals(number, number);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(isMT))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(code))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(index))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(number)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    isMT = paramHwBlob.getBool(paramLong + 0L);
    code = paramHwBlob.getInt32(paramLong + 4L);
    index = paramHwBlob.getInt32(paramLong + 8L);
    type = paramHwBlob.getInt32(paramLong + 12L);
    number = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(number.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(32L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".isMT = ");
    localStringBuilder.append(isMT);
    localStringBuilder.append(", .code = ");
    localStringBuilder.append(code);
    localStringBuilder.append(", .index = ");
    localStringBuilder.append(index);
    localStringBuilder.append(", .type = ");
    localStringBuilder.append(type);
    localStringBuilder.append(", .number = ");
    localStringBuilder.append(number);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putBool(0L + paramLong, isMT);
    paramHwBlob.putInt32(4L + paramLong, code);
    paramHwBlob.putInt32(8L + paramLong, index);
    paramHwBlob.putInt32(12L + paramLong, type);
    paramHwBlob.putString(16L + paramLong, number);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
