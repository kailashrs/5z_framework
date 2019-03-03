package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaSmsMessage
{
  public final CdmaSmsAddress address = new CdmaSmsAddress();
  public final ArrayList<Byte> bearerData = new ArrayList();
  public boolean isServicePresent;
  public int serviceCategory;
  public final CdmaSmsSubaddress subAddress = new CdmaSmsSubaddress();
  public int teleserviceId;
  
  public CdmaSmsMessage() {}
  
  public static final ArrayList<CdmaSmsMessage> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 88, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CdmaSmsMessage localCdmaSmsMessage = new CdmaSmsMessage();
      localCdmaSmsMessage.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 88);
      localArrayList.add(localCdmaSmsMessage);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaSmsMessage> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 88);
    while (j < i)
    {
      ((CdmaSmsMessage)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 88);
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
    if (paramObject.getClass() != CdmaSmsMessage.class) {
      return false;
    }
    paramObject = (CdmaSmsMessage)paramObject;
    if (teleserviceId != teleserviceId) {
      return false;
    }
    if (isServicePresent != isServicePresent) {
      return false;
    }
    if (serviceCategory != serviceCategory) {
      return false;
    }
    if (!HidlSupport.deepEquals(address, address)) {
      return false;
    }
    if (!HidlSupport.deepEquals(subAddress, subAddress)) {
      return false;
    }
    return HidlSupport.deepEquals(bearerData, bearerData);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(teleserviceId))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(isServicePresent))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(serviceCategory))), Integer.valueOf(HidlSupport.deepHashCode(address)), Integer.valueOf(HidlSupport.deepHashCode(subAddress)), Integer.valueOf(HidlSupport.deepHashCode(bearerData)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    teleserviceId = paramHwBlob.getInt32(paramLong + 0L);
    isServicePresent = paramHwBlob.getBool(paramLong + 4L);
    serviceCategory = paramHwBlob.getInt32(paramLong + 8L);
    address.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, paramLong + 16L);
    subAddress.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, paramLong + 48L);
    int i = paramHwBlob.getInt32(paramLong + 72L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 72L + 0L, true);
    bearerData.clear();
    for (int j = 0; j < i; j++)
    {
      byte b = paramHwParcel.getInt8(j * 1);
      bearerData.add(Byte.valueOf(b));
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(88L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".teleserviceId = ");
    localStringBuilder.append(teleserviceId);
    localStringBuilder.append(", .isServicePresent = ");
    localStringBuilder.append(isServicePresent);
    localStringBuilder.append(", .serviceCategory = ");
    localStringBuilder.append(serviceCategory);
    localStringBuilder.append(", .address = ");
    localStringBuilder.append(address);
    localStringBuilder.append(", .subAddress = ");
    localStringBuilder.append(subAddress);
    localStringBuilder.append(", .bearerData = ");
    localStringBuilder.append(bearerData);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, teleserviceId);
    paramHwBlob.putBool(4L + paramLong, isServicePresent);
    paramHwBlob.putInt32(paramLong + 8L, serviceCategory);
    address.writeEmbeddedToBlob(paramHwBlob, 16L + paramLong);
    subAddress.writeEmbeddedToBlob(paramHwBlob, 48L + paramLong);
    int i = bearerData.size();
    paramHwBlob.putInt32(paramLong + 72L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 72L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 1);
    while (j < i)
    {
      localHwBlob.putInt8(j * 1, ((Byte)bearerData.get(j)).byteValue());
      j++;
    }
    paramHwBlob.putBlob(72L + paramLong + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(88);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
