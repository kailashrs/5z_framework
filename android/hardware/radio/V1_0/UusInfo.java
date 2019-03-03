package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class UusInfo
{
  public String uusData = new String();
  public int uusDcs;
  public int uusType;
  
  public UusInfo() {}
  
  public static final ArrayList<UusInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new UusInfo();
      ((UusInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<UusInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((UusInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != UusInfo.class) {
      return false;
    }
    paramObject = (UusInfo)paramObject;
    if (uusType != uusType) {
      return false;
    }
    if (uusDcs != uusDcs) {
      return false;
    }
    return HidlSupport.deepEquals(uusData, uusData);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(uusType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(uusDcs))), Integer.valueOf(HidlSupport.deepHashCode(uusData)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    uusType = paramHwBlob.getInt32(paramLong + 0L);
    uusDcs = paramHwBlob.getInt32(paramLong + 4L);
    uusData = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(uusData.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(24L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".uusType = ");
    localStringBuilder.append(UusType.toString(uusType));
    localStringBuilder.append(", .uusDcs = ");
    localStringBuilder.append(UusDcs.toString(uusDcs));
    localStringBuilder.append(", .uusData = ");
    localStringBuilder.append(uusData);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, uusType);
    paramHwBlob.putInt32(4L + paramLong, uusDcs);
    paramHwBlob.putString(8L + paramLong, uusData);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(24);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
