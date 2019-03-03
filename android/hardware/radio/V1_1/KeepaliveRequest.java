package android.hardware.radio.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class KeepaliveRequest
{
  public int cid;
  public final ArrayList<Byte> destinationAddress = new ArrayList();
  public int destinationPort;
  public int maxKeepaliveIntervalMillis;
  public final ArrayList<Byte> sourceAddress = new ArrayList();
  public int sourcePort;
  public int type;
  
  public KeepaliveRequest() {}
  
  public static final ArrayList<KeepaliveRequest> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 64, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      KeepaliveRequest localKeepaliveRequest = new KeepaliveRequest();
      localKeepaliveRequest.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 64);
      localArrayList.add(localKeepaliveRequest);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<KeepaliveRequest> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 64);
    while (j < i)
    {
      ((KeepaliveRequest)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 64);
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
    if (paramObject.getClass() != KeepaliveRequest.class) {
      return false;
    }
    paramObject = (KeepaliveRequest)paramObject;
    if (type != type) {
      return false;
    }
    if (!HidlSupport.deepEquals(sourceAddress, sourceAddress)) {
      return false;
    }
    if (sourcePort != sourcePort) {
      return false;
    }
    if (!HidlSupport.deepEquals(destinationAddress, destinationAddress)) {
      return false;
    }
    if (destinationPort != destinationPort) {
      return false;
    }
    if (maxKeepaliveIntervalMillis != maxKeepaliveIntervalMillis) {
      return false;
    }
    return cid == cid;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(sourceAddress)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(sourcePort))), Integer.valueOf(HidlSupport.deepHashCode(destinationAddress)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(destinationPort))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxKeepaliveIntervalMillis))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cid))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    type = paramHwBlob.getInt32(paramLong + 0L);
    int i = paramHwBlob.getInt32(paramLong + 8L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 8L + 0L, true);
    sourceAddress.clear();
    int j = 0;
    byte b;
    for (int k = 0; k < i; k++)
    {
      b = localHwBlob.getInt8(k * 1);
      sourceAddress.add(Byte.valueOf(b));
    }
    sourcePort = paramHwBlob.getInt32(paramLong + 24L);
    i = paramHwBlob.getInt32(paramLong + 32L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 32L + 0L, true);
    destinationAddress.clear();
    for (k = j; k < i; k++)
    {
      b = paramHwParcel.getInt8(k * 1);
      destinationAddress.add(Byte.valueOf(b));
    }
    destinationPort = paramHwBlob.getInt32(paramLong + 48L);
    maxKeepaliveIntervalMillis = paramHwBlob.getInt32(paramLong + 52L);
    cid = paramHwBlob.getInt32(paramLong + 56L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(64L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".type = ");
    localStringBuilder.append(KeepaliveType.toString(type));
    localStringBuilder.append(", .sourceAddress = ");
    localStringBuilder.append(sourceAddress);
    localStringBuilder.append(", .sourcePort = ");
    localStringBuilder.append(sourcePort);
    localStringBuilder.append(", .destinationAddress = ");
    localStringBuilder.append(destinationAddress);
    localStringBuilder.append(", .destinationPort = ");
    localStringBuilder.append(destinationPort);
    localStringBuilder.append(", .maxKeepaliveIntervalMillis = ");
    localStringBuilder.append(maxKeepaliveIntervalMillis);
    localStringBuilder.append(", .cid = ");
    localStringBuilder.append(cid);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, type);
    int i = sourceAddress.size();
    paramHwBlob.putInt32(paramLong + 8L + 8L, i);
    paramHwBlob.putBool(paramLong + 8L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 1);
    for (int j = 0; j < i; j++) {
      localHwBlob.putInt8(j * 1, ((Byte)sourceAddress.get(j)).byteValue());
    }
    paramHwBlob.putBlob(paramLong + 8L + 0L, localHwBlob);
    paramHwBlob.putInt32(paramLong + 24L, sourcePort);
    i = destinationAddress.size();
    paramHwBlob.putInt32(paramLong + 32L + 8L, i);
    j = 0;
    paramHwBlob.putBool(paramLong + 32L + 12L, false);
    localHwBlob = new HwBlob(i * 1);
    while (j < i)
    {
      localHwBlob.putInt8(j * 1, ((Byte)destinationAddress.get(j)).byteValue());
      j++;
    }
    paramHwBlob.putBlob(paramLong + 32L + 0L, localHwBlob);
    paramHwBlob.putInt32(paramLong + 48L, destinationPort);
    paramHwBlob.putInt32(paramLong + 52L, maxKeepaliveIntervalMillis);
    paramHwBlob.putInt32(paramLong + 56L, cid);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(64);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
