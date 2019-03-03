package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SsInfoData
{
  public final ArrayList<Integer> ssInfo = new ArrayList();
  
  public SsInfoData() {}
  
  public static final ArrayList<SsInfoData> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new SsInfoData();
      ((SsInfoData)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 16);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SsInfoData> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 16);
    while (j < i)
    {
      ((SsInfoData)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 16);
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
    if (paramObject.getClass() != SsInfoData.class) {
      return false;
    }
    paramObject = (SsInfoData)paramObject;
    return HidlSupport.deepEquals(ssInfo, ssInfo);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(ssInfo)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    int i = paramHwBlob.getInt32(paramLong + 0L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 4, paramHwBlob.handle(), paramLong + 0L + 0L, true);
    ssInfo.clear();
    for (int j = 0; j < i; j++)
    {
      int k = paramHwParcel.getInt32(j * 4);
      ssInfo.add(Integer.valueOf(k));
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(16L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".ssInfo = ");
    localStringBuilder.append(ssInfo);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    int i = ssInfo.size();
    paramHwBlob.putInt32(paramLong + 0L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 0L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 4);
    while (j < i)
    {
      localHwBlob.putInt32(j * 4, ((Integer)ssInfo.get(j)).intValue());
      j++;
    }
    paramHwBlob.putBlob(paramLong + 0L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(16);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
