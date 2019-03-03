package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SmsWriteArgs
{
  public String pdu = new String();
  public String smsc = new String();
  public int status;
  
  public SmsWriteArgs() {}
  
  public static final ArrayList<SmsWriteArgs> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 40, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      SmsWriteArgs localSmsWriteArgs = new SmsWriteArgs();
      localSmsWriteArgs.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 40);
      localArrayList.add(localSmsWriteArgs);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SmsWriteArgs> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 40);
    while (j < i)
    {
      ((SmsWriteArgs)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 40);
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
    if (paramObject.getClass() != SmsWriteArgs.class) {
      return false;
    }
    paramObject = (SmsWriteArgs)paramObject;
    if (status != status) {
      return false;
    }
    if (!HidlSupport.deepEquals(pdu, pdu)) {
      return false;
    }
    return HidlSupport.deepEquals(smsc, smsc);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(status))), Integer.valueOf(HidlSupport.deepHashCode(pdu)), Integer.valueOf(HidlSupport.deepHashCode(smsc)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    status = paramHwBlob.getInt32(paramLong + 0L);
    pdu = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(pdu.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
    smsc = paramHwBlob.getString(paramLong + 24L);
    paramHwParcel.readEmbeddedBuffer(smsc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 24L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(40L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".status = ");
    localStringBuilder.append(SmsWriteArgsStatus.toString(status));
    localStringBuilder.append(", .pdu = ");
    localStringBuilder.append(pdu);
    localStringBuilder.append(", .smsc = ");
    localStringBuilder.append(smsc);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, status);
    paramHwBlob.putString(8L + paramLong, pdu);
    paramHwBlob.putString(24L + paramLong, smsc);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(40);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
