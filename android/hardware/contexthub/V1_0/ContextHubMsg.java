package android.hardware.contexthub.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class ContextHubMsg
{
  public long appName;
  public short hostEndPoint;
  public final ArrayList<Byte> msg = new ArrayList();
  public int msgType;
  
  public ContextHubMsg() {}
  
  public static final ArrayList<ContextHubMsg> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new ContextHubMsg();
      ((ContextHubMsg)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<ContextHubMsg> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((ContextHubMsg)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != ContextHubMsg.class) {
      return false;
    }
    paramObject = (ContextHubMsg)paramObject;
    if (appName != appName) {
      return false;
    }
    if (hostEndPoint != hostEndPoint) {
      return false;
    }
    if (msgType != msgType) {
      return false;
    }
    return HidlSupport.deepEquals(msg, msg);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(appName))), Integer.valueOf(HidlSupport.deepHashCode(Short.valueOf(hostEndPoint))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(msgType))), Integer.valueOf(HidlSupport.deepHashCode(msg)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    appName = paramHwBlob.getInt64(paramLong + 0L);
    hostEndPoint = paramHwBlob.getInt16(paramLong + 8L);
    msgType = paramHwBlob.getInt32(paramLong + 12L);
    int i = paramHwBlob.getInt32(paramLong + 16L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 16L + 0L, true);
    msg.clear();
    for (int j = 0; j < i; j++)
    {
      byte b = paramHwParcel.getInt8(j * 1);
      msg.add(Byte.valueOf(b));
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(32L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".appName = ");
    localStringBuilder.append(appName);
    localStringBuilder.append(", .hostEndPoint = ");
    localStringBuilder.append(hostEndPoint);
    localStringBuilder.append(", .msgType = ");
    localStringBuilder.append(msgType);
    localStringBuilder.append(", .msg = ");
    localStringBuilder.append(msg);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt64(paramLong + 0L, appName);
    paramHwBlob.putInt16(paramLong + 8L, hostEndPoint);
    paramHwBlob.putInt32(paramLong + 12L, msgType);
    int i = msg.size();
    paramHwBlob.putInt32(paramLong + 16L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 16L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 1);
    while (j < i)
    {
      localHwBlob.putInt8(j * 1, ((Byte)msg.get(j)).byteValue());
      j++;
    }
    paramHwBlob.putBlob(16L + paramLong + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
