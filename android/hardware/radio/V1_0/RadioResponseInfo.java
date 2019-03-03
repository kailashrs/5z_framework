package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class RadioResponseInfo
{
  public int error;
  public int serial;
  public int type;
  
  public RadioResponseInfo() {}
  
  public static final ArrayList<RadioResponseInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 12, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new RadioResponseInfo();
      ((RadioResponseInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 12);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<RadioResponseInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 12);
    while (j < i)
    {
      ((RadioResponseInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 12);
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
    if (paramObject.getClass() != RadioResponseInfo.class) {
      return false;
    }
    paramObject = (RadioResponseInfo)paramObject;
    if (type != type) {
      return false;
    }
    if (serial != serial) {
      return false;
    }
    return error == error;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(serial))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(error))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    type = paramHwBlob.getInt32(0L + paramLong);
    serial = paramHwBlob.getInt32(4L + paramLong);
    error = paramHwBlob.getInt32(8L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(12L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".type = ");
    localStringBuilder.append(RadioResponseType.toString(type));
    localStringBuilder.append(", .serial = ");
    localStringBuilder.append(serial);
    localStringBuilder.append(", .error = ");
    localStringBuilder.append(RadioError.toString(error));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, type);
    paramHwBlob.putInt32(4L + paramLong, serial);
    paramHwBlob.putInt32(8L + paramLong, error);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(12);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
