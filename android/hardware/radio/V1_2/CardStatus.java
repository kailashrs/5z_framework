package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CardStatus
{
  public String atr = new String();
  public final android.hardware.radio.V1_0.CardStatus base = new android.hardware.radio.V1_0.CardStatus();
  public String iccid = new String();
  public int physicalSlotId;
  
  public CardStatus() {}
  
  public static final ArrayList<CardStatus> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 80, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CardStatus();
      ((CardStatus)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 80);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CardStatus> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 80);
    while (j < i)
    {
      ((CardStatus)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 80);
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
    if (paramObject.getClass() != CardStatus.class) {
      return false;
    }
    paramObject = (CardStatus)paramObject;
    if (!HidlSupport.deepEquals(base, base)) {
      return false;
    }
    if (physicalSlotId != physicalSlotId) {
      return false;
    }
    if (!HidlSupport.deepEquals(atr, atr)) {
      return false;
    }
    return HidlSupport.deepEquals(iccid, iccid);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(base)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(physicalSlotId))), Integer.valueOf(HidlSupport.deepHashCode(atr)), Integer.valueOf(HidlSupport.deepHashCode(iccid)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    base.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, paramLong + 0L);
    physicalSlotId = paramHwBlob.getInt32(paramLong + 40L);
    atr = paramHwBlob.getString(paramLong + 48L);
    paramHwParcel.readEmbeddedBuffer(atr.getBytes().length + 1, paramHwBlob.handle(), paramLong + 48L + 0L, false);
    iccid = paramHwBlob.getString(paramLong + 64L);
    paramHwParcel.readEmbeddedBuffer(iccid.getBytes().length + 1, paramHwBlob.handle(), paramLong + 64L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(80L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".base = ");
    localStringBuilder.append(base);
    localStringBuilder.append(", .physicalSlotId = ");
    localStringBuilder.append(physicalSlotId);
    localStringBuilder.append(", .atr = ");
    localStringBuilder.append(atr);
    localStringBuilder.append(", .iccid = ");
    localStringBuilder.append(iccid);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    base.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    paramHwBlob.putInt32(40L + paramLong, physicalSlotId);
    paramHwBlob.putString(48L + paramLong, atr);
    paramHwBlob.putString(64L + paramLong, iccid);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(80);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
