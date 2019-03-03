package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class GsmSmsMessage
{
  public String pdu = new String();
  public String smscPdu = new String();
  
  public GsmSmsMessage() {}
  
  public static final ArrayList<GsmSmsMessage> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      GsmSmsMessage localGsmSmsMessage = new GsmSmsMessage();
      localGsmSmsMessage.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localGsmSmsMessage);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<GsmSmsMessage> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((GsmSmsMessage)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != GsmSmsMessage.class) {
      return false;
    }
    paramObject = (GsmSmsMessage)paramObject;
    if (!HidlSupport.deepEquals(smscPdu, smscPdu)) {
      return false;
    }
    return HidlSupport.deepEquals(pdu, pdu);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(smscPdu)), Integer.valueOf(HidlSupport.deepHashCode(pdu)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    smscPdu = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(smscPdu.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    pdu = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(pdu.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(32L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".smscPdu = ");
    localStringBuilder.append(smscPdu);
    localStringBuilder.append(", .pdu = ");
    localStringBuilder.append(pdu);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, smscPdu);
    paramHwBlob.putString(16L + paramLong, pdu);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
