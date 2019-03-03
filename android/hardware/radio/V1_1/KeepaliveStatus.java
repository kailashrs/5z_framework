package android.hardware.radio.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class KeepaliveStatus
{
  public int code;
  public int sessionHandle;
  
  public KeepaliveStatus() {}
  
  public static final ArrayList<KeepaliveStatus> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 8, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      KeepaliveStatus localKeepaliveStatus = new KeepaliveStatus();
      localKeepaliveStatus.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 8);
      localArrayList.add(localKeepaliveStatus);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<KeepaliveStatus> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 8);
    while (j < i)
    {
      ((KeepaliveStatus)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 8);
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
    if (paramObject.getClass() != KeepaliveStatus.class) {
      return false;
    }
    paramObject = (KeepaliveStatus)paramObject;
    if (sessionHandle != sessionHandle) {
      return false;
    }
    return code == code;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(sessionHandle))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(code))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    sessionHandle = paramHwBlob.getInt32(0L + paramLong);
    code = paramHwBlob.getInt32(4L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(8L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".sessionHandle = ");
    localStringBuilder.append(sessionHandle);
    localStringBuilder.append(", .code = ");
    localStringBuilder.append(KeepaliveStatusCode.toString(code));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, sessionHandle);
    paramHwBlob.putInt32(4L + paramLong, code);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(8);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
