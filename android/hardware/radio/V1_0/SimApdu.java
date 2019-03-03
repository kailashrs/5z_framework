package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SimApdu
{
  public int cla;
  public String data = new String();
  public int instruction;
  public int p1;
  public int p2;
  public int p3;
  public int sessionId;
  
  public SimApdu() {}
  
  public static final ArrayList<SimApdu> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 40, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new SimApdu();
      ((SimApdu)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 40);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SimApdu> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 40);
    while (j < i)
    {
      ((SimApdu)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 40);
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
    if (paramObject.getClass() != SimApdu.class) {
      return false;
    }
    paramObject = (SimApdu)paramObject;
    if (sessionId != sessionId) {
      return false;
    }
    if (cla != cla) {
      return false;
    }
    if (instruction != instruction) {
      return false;
    }
    if (p1 != p1) {
      return false;
    }
    if (p2 != p2) {
      return false;
    }
    if (p3 != p3) {
      return false;
    }
    return HidlSupport.deepEquals(data, data);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(sessionId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cla))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(instruction))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(p1))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(p2))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(p3))), Integer.valueOf(HidlSupport.deepHashCode(data)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    sessionId = paramHwBlob.getInt32(paramLong + 0L);
    cla = paramHwBlob.getInt32(paramLong + 4L);
    instruction = paramHwBlob.getInt32(paramLong + 8L);
    p1 = paramHwBlob.getInt32(paramLong + 12L);
    p2 = paramHwBlob.getInt32(paramLong + 16L);
    p3 = paramHwBlob.getInt32(paramLong + 20L);
    data = paramHwBlob.getString(paramLong + 24L);
    paramHwParcel.readEmbeddedBuffer(data.getBytes().length + 1, paramHwBlob.handle(), paramLong + 24L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(40L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".sessionId = ");
    localStringBuilder.append(sessionId);
    localStringBuilder.append(", .cla = ");
    localStringBuilder.append(cla);
    localStringBuilder.append(", .instruction = ");
    localStringBuilder.append(instruction);
    localStringBuilder.append(", .p1 = ");
    localStringBuilder.append(p1);
    localStringBuilder.append(", .p2 = ");
    localStringBuilder.append(p2);
    localStringBuilder.append(", .p3 = ");
    localStringBuilder.append(p3);
    localStringBuilder.append(", .data = ");
    localStringBuilder.append(data);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, sessionId);
    paramHwBlob.putInt32(4L + paramLong, cla);
    paramHwBlob.putInt32(8L + paramLong, instruction);
    paramHwBlob.putInt32(12L + paramLong, p1);
    paramHwBlob.putInt32(16L + paramLong, p2);
    paramHwBlob.putInt32(20L + paramLong, p3);
    paramHwBlob.putString(24L + paramLong, data);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(40);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
