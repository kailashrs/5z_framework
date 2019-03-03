package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class HardwareConfig
{
  public final ArrayList<HardwareConfigModem> modem = new ArrayList();
  public final ArrayList<HardwareConfigSim> sim = new ArrayList();
  public int state;
  public int type;
  public String uuid = new String();
  
  public HardwareConfig() {}
  
  public static final ArrayList<HardwareConfig> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 64, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      HardwareConfig localHardwareConfig = new HardwareConfig();
      localHardwareConfig.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 64);
      localArrayList.add(localHardwareConfig);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<HardwareConfig> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 64);
    while (j < i)
    {
      ((HardwareConfig)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 64);
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
    if (paramObject.getClass() != HardwareConfig.class) {
      return false;
    }
    paramObject = (HardwareConfig)paramObject;
    if (type != type) {
      return false;
    }
    if (!HidlSupport.deepEquals(uuid, uuid)) {
      return false;
    }
    if (state != state) {
      return false;
    }
    if (!HidlSupport.deepEquals(modem, modem)) {
      return false;
    }
    return HidlSupport.deepEquals(sim, sim);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(uuid)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(state))), Integer.valueOf(HidlSupport.deepHashCode(modem)), Integer.valueOf(HidlSupport.deepHashCode(sim)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    type = paramHwBlob.getInt32(paramLong + 0L);
    uuid = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(uuid.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
    state = paramHwBlob.getInt32(paramLong + 24L);
    int i = paramHwBlob.getInt32(paramLong + 32L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 20, paramHwBlob.handle(), paramLong + 32L + 0L, true);
    modem.clear();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      HardwareConfigModem localHardwareConfigModem = new HardwareConfigModem();
      localHardwareConfigModem.readEmbeddedFromParcel(paramHwParcel, localHwBlob, k * 20);
      modem.add(localHardwareConfigModem);
    }
    i = paramHwBlob.getInt32(paramLong + 48L + 8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, paramHwBlob.handle(), 0L + (paramLong + 48L), true);
    sim.clear();
    for (k = j; k < i; k++)
    {
      paramHwBlob = new HardwareConfigSim();
      paramHwBlob.readEmbeddedFromParcel(paramHwParcel, localHwBlob, k * 16);
      sim.add(paramHwBlob);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(64L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".type = ");
    localStringBuilder.append(HardwareConfigType.toString(type));
    localStringBuilder.append(", .uuid = ");
    localStringBuilder.append(uuid);
    localStringBuilder.append(", .state = ");
    localStringBuilder.append(HardwareConfigState.toString(state));
    localStringBuilder.append(", .modem = ");
    localStringBuilder.append(modem);
    localStringBuilder.append(", .sim = ");
    localStringBuilder.append(sim);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, type);
    paramHwBlob.putString(paramLong + 8L, uuid);
    paramHwBlob.putInt32(paramLong + 24L, state);
    int i = modem.size();
    paramHwBlob.putInt32(paramLong + 32L + 8L, i);
    paramHwBlob.putBool(paramLong + 32L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 20);
    for (int j = 0; j < i; j++) {
      ((HardwareConfigModem)modem.get(j)).writeEmbeddedToBlob(localHwBlob, j * 20);
    }
    paramHwBlob.putBlob(paramLong + 32L + 0L, localHwBlob);
    i = sim.size();
    paramHwBlob.putInt32(paramLong + 48L + 8L, i);
    j = 0;
    paramHwBlob.putBool(paramLong + 48L + 12L, false);
    localHwBlob = new HwBlob(i * 16);
    while (j < i)
    {
      ((HardwareConfigSim)sim.get(j)).writeEmbeddedToBlob(localHwBlob, j * 16);
      j++;
    }
    paramHwBlob.putBlob(paramLong + 48L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(64);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
