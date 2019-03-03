package android.hardware.radio.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class ImsiEncryptionInfo
{
  public final ArrayList<Byte> carrierKey = new ArrayList();
  public long expirationTime;
  public String keyIdentifier = new String();
  public String mcc = new String();
  public String mnc = new String();
  
  public ImsiEncryptionInfo() {}
  
  public static final ArrayList<ImsiEncryptionInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 72, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new ImsiEncryptionInfo();
      ((ImsiEncryptionInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 72);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<ImsiEncryptionInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 72);
    while (j < i)
    {
      ((ImsiEncryptionInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 72);
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
    if (paramObject.getClass() != ImsiEncryptionInfo.class) {
      return false;
    }
    paramObject = (ImsiEncryptionInfo)paramObject;
    if (!HidlSupport.deepEquals(mcc, mcc)) {
      return false;
    }
    if (!HidlSupport.deepEquals(mnc, mnc)) {
      return false;
    }
    if (!HidlSupport.deepEquals(carrierKey, carrierKey)) {
      return false;
    }
    if (!HidlSupport.deepEquals(keyIdentifier, keyIdentifier)) {
      return false;
    }
    return expirationTime == expirationTime;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(mcc)), Integer.valueOf(HidlSupport.deepHashCode(mnc)), Integer.valueOf(HidlSupport.deepHashCode(carrierKey)), Integer.valueOf(HidlSupport.deepHashCode(keyIdentifier)), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(expirationTime))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    mcc = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(mcc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    mnc = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(mnc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    int i = paramHwBlob.getInt32(paramLong + 32L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 32L + 0L, true);
    carrierKey.clear();
    for (int j = 0; j < i; j++)
    {
      byte b = localHwBlob.getInt8(j * 1);
      carrierKey.add(Byte.valueOf(b));
    }
    keyIdentifier = paramHwBlob.getString(paramLong + 48L);
    paramHwParcel.readEmbeddedBuffer(keyIdentifier.getBytes().length + 1, paramHwBlob.handle(), paramLong + 48L + 0L, false);
    expirationTime = paramHwBlob.getInt64(paramLong + 64L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(72L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".mcc = ");
    localStringBuilder.append(mcc);
    localStringBuilder.append(", .mnc = ");
    localStringBuilder.append(mnc);
    localStringBuilder.append(", .carrierKey = ");
    localStringBuilder.append(carrierKey);
    localStringBuilder.append(", .keyIdentifier = ");
    localStringBuilder.append(keyIdentifier);
    localStringBuilder.append(", .expirationTime = ");
    localStringBuilder.append(expirationTime);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(paramLong + 0L, mcc);
    paramHwBlob.putString(16L + paramLong, mnc);
    int i = carrierKey.size();
    paramHwBlob.putInt32(paramLong + 32L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 32L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 1);
    while (j < i)
    {
      localHwBlob.putInt8(j * 1, ((Byte)carrierKey.get(j)).byteValue());
      j++;
    }
    paramHwBlob.putBlob(32L + paramLong + 0L, localHwBlob);
    paramHwBlob.putString(48L + paramLong, keyIdentifier);
    paramHwBlob.putInt64(64L + paramLong, expirationTime);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(72);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
