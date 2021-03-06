package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class HardwareConfigSim
{
  public String modemUuid = new String();
  
  public HardwareConfigSim() {}
  
  public static final ArrayList<HardwareConfigSim> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      HardwareConfigSim localHardwareConfigSim = new HardwareConfigSim();
      localHardwareConfigSim.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 16);
      localArrayList.add(localHardwareConfigSim);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<HardwareConfigSim> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 16);
    while (j < i)
    {
      ((HardwareConfigSim)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 16);
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
    if (paramObject.getClass() != HardwareConfigSim.class) {
      return false;
    }
    paramObject = (HardwareConfigSim)paramObject;
    return HidlSupport.deepEquals(modemUuid, modemUuid);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(modemUuid)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    modemUuid = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(modemUuid.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(16L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".modemUuid = ");
    localStringBuilder.append(modemUuid);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, modemUuid);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(16);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
