package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SendSmsResult
{
  public String ackPDU = new String();
  public int errorCode;
  public int messageRef;
  
  public SendSmsResult() {}
  
  public static final ArrayList<SendSmsResult> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      SendSmsResult localSendSmsResult = new SendSmsResult();
      localSendSmsResult.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localSendSmsResult);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SendSmsResult> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((SendSmsResult)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != SendSmsResult.class) {
      return false;
    }
    paramObject = (SendSmsResult)paramObject;
    if (messageRef != messageRef) {
      return false;
    }
    if (!HidlSupport.deepEquals(ackPDU, ackPDU)) {
      return false;
    }
    return errorCode == errorCode;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(messageRef))), Integer.valueOf(HidlSupport.deepHashCode(ackPDU)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(errorCode))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    messageRef = paramHwBlob.getInt32(paramLong + 0L);
    ackPDU = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(ackPDU.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
    errorCode = paramHwBlob.getInt32(paramLong + 24L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(32L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".messageRef = ");
    localStringBuilder.append(messageRef);
    localStringBuilder.append(", .ackPDU = ");
    localStringBuilder.append(ackPDU);
    localStringBuilder.append(", .errorCode = ");
    localStringBuilder.append(errorCode);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, messageRef);
    paramHwBlob.putString(8L + paramLong, ackPDU);
    paramHwBlob.putInt32(24L + paramLong, errorCode);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
