package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class HardwareConfigModem
{
  public int maxData;
  public int maxStandby;
  public int maxVoice;
  public int rat;
  public int rilModel;
  
  public HardwareConfigModem() {}
  
  public static final ArrayList<HardwareConfigModem> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 20, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new HardwareConfigModem();
      ((HardwareConfigModem)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 20);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<HardwareConfigModem> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 20);
    while (j < i)
    {
      ((HardwareConfigModem)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 20);
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
    if (paramObject.getClass() != HardwareConfigModem.class) {
      return false;
    }
    paramObject = (HardwareConfigModem)paramObject;
    if (rilModel != rilModel) {
      return false;
    }
    if (rat != rat) {
      return false;
    }
    if (maxVoice != maxVoice) {
      return false;
    }
    if (maxData != maxData) {
      return false;
    }
    return maxStandby == maxStandby;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rilModel))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(rat))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxVoice))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxData))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxStandby))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    rilModel = paramHwBlob.getInt32(0L + paramLong);
    rat = paramHwBlob.getInt32(4L + paramLong);
    maxVoice = paramHwBlob.getInt32(8L + paramLong);
    maxData = paramHwBlob.getInt32(12L + paramLong);
    maxStandby = paramHwBlob.getInt32(16L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(20L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".rilModel = ");
    localStringBuilder.append(rilModel);
    localStringBuilder.append(", .rat = ");
    localStringBuilder.append(rat);
    localStringBuilder.append(", .maxVoice = ");
    localStringBuilder.append(maxVoice);
    localStringBuilder.append(", .maxData = ");
    localStringBuilder.append(maxData);
    localStringBuilder.append(", .maxStandby = ");
    localStringBuilder.append(maxStandby);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, rilModel);
    paramHwBlob.putInt32(4L + paramLong, rat);
    paramHwBlob.putInt32(8L + paramLong, maxVoice);
    paramHwBlob.putInt32(12L + paramLong, maxData);
    paramHwBlob.putInt32(16L + paramLong, maxStandby);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(20);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
