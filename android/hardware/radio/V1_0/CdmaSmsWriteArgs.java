package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaSmsWriteArgs
{
  public final CdmaSmsMessage message = new CdmaSmsMessage();
  public int status;
  
  public CdmaSmsWriteArgs() {}
  
  public static final ArrayList<CdmaSmsWriteArgs> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 96, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CdmaSmsWriteArgs();
      ((CdmaSmsWriteArgs)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 96);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaSmsWriteArgs> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 96);
    while (j < i)
    {
      ((CdmaSmsWriteArgs)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 96);
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
    if (paramObject.getClass() != CdmaSmsWriteArgs.class) {
      return false;
    }
    paramObject = (CdmaSmsWriteArgs)paramObject;
    if (status != status) {
      return false;
    }
    return HidlSupport.deepEquals(message, message);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(status))), Integer.valueOf(HidlSupport.deepHashCode(message)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    status = paramHwBlob.getInt32(0L + paramLong);
    message.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 8L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(96L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".status = ");
    localStringBuilder.append(CdmaSmsWriteArgsStatus.toString(status));
    localStringBuilder.append(", .message = ");
    localStringBuilder.append(message);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, status);
    message.writeEmbeddedToBlob(paramHwBlob, 8L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(96);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
