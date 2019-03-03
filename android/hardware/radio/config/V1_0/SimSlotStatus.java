package android.hardware.radio.config.V1_0;

import android.hardware.radio.V1_0.CardState;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SimSlotStatus
{
  public String atr = new String();
  public int cardState;
  public String iccid = new String();
  public int logicalSlotId;
  public int slotState;
  
  public SimSlotStatus() {}
  
  public static final ArrayList<SimSlotStatus> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 48, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new SimSlotStatus();
      ((SimSlotStatus)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 48);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SimSlotStatus> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 48);
    while (j < i)
    {
      ((SimSlotStatus)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 48);
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
    if (paramObject.getClass() != SimSlotStatus.class) {
      return false;
    }
    paramObject = (SimSlotStatus)paramObject;
    if (cardState != cardState) {
      return false;
    }
    if (slotState != slotState) {
      return false;
    }
    if (!HidlSupport.deepEquals(atr, atr)) {
      return false;
    }
    if (logicalSlotId != logicalSlotId) {
      return false;
    }
    return HidlSupport.deepEquals(iccid, iccid);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cardState))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(slotState))), Integer.valueOf(HidlSupport.deepHashCode(atr)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(logicalSlotId))), Integer.valueOf(HidlSupport.deepHashCode(iccid)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cardState = paramHwBlob.getInt32(paramLong + 0L);
    slotState = paramHwBlob.getInt32(paramLong + 4L);
    atr = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(atr.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
    logicalSlotId = paramHwBlob.getInt32(paramLong + 24L);
    iccid = paramHwBlob.getString(paramLong + 32L);
    paramHwParcel.readEmbeddedBuffer(iccid.getBytes().length + 1, paramHwBlob.handle(), paramLong + 32L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(48L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cardState = ");
    localStringBuilder.append(CardState.toString(cardState));
    localStringBuilder.append(", .slotState = ");
    localStringBuilder.append(SlotState.toString(slotState));
    localStringBuilder.append(", .atr = ");
    localStringBuilder.append(atr);
    localStringBuilder.append(", .logicalSlotId = ");
    localStringBuilder.append(logicalSlotId);
    localStringBuilder.append(", .iccid = ");
    localStringBuilder.append(iccid);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, cardState);
    paramHwBlob.putInt32(4L + paramLong, slotState);
    paramHwBlob.putString(8L + paramLong, atr);
    paramHwBlob.putInt32(24L + paramLong, logicalSlotId);
    paramHwBlob.putString(32L + paramLong, iccid);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(48);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
