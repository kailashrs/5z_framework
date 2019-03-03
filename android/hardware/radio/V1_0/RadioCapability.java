package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class RadioCapability
{
  public String logicalModemUuid = new String();
  public int phase;
  public int raf;
  public int session;
  public int status;
  
  public RadioCapability() {}
  
  public static final ArrayList<RadioCapability> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 40, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new RadioCapability();
      ((RadioCapability)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 40);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<RadioCapability> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 40);
    while (j < i)
    {
      ((RadioCapability)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 40);
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
    if (paramObject.getClass() != RadioCapability.class) {
      return false;
    }
    paramObject = (RadioCapability)paramObject;
    if (session != session) {
      return false;
    }
    if (phase != phase) {
      return false;
    }
    if (!HidlSupport.deepEquals(Integer.valueOf(raf), Integer.valueOf(raf))) {
      return false;
    }
    if (!HidlSupport.deepEquals(logicalModemUuid, logicalModemUuid)) {
      return false;
    }
    return status == status;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(session))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(phase))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(raf))), Integer.valueOf(HidlSupport.deepHashCode(logicalModemUuid)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(status))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    session = paramHwBlob.getInt32(paramLong + 0L);
    phase = paramHwBlob.getInt32(paramLong + 4L);
    raf = paramHwBlob.getInt32(paramLong + 8L);
    logicalModemUuid = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(logicalModemUuid.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    status = paramHwBlob.getInt32(paramLong + 32L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(40L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".session = ");
    localStringBuilder.append(session);
    localStringBuilder.append(", .phase = ");
    localStringBuilder.append(RadioCapabilityPhase.toString(phase));
    localStringBuilder.append(", .raf = ");
    localStringBuilder.append(RadioAccessFamily.dumpBitfield(raf));
    localStringBuilder.append(", .logicalModemUuid = ");
    localStringBuilder.append(logicalModemUuid);
    localStringBuilder.append(", .status = ");
    localStringBuilder.append(RadioCapabilityStatus.toString(status));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, session);
    paramHwBlob.putInt32(4L + paramLong, phase);
    paramHwBlob.putInt32(8L + paramLong, raf);
    paramHwBlob.putString(16L + paramLong, logicalModemUuid);
    paramHwBlob.putInt32(32L + paramLong, status);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(40);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
