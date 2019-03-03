package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CarrierRestrictions
{
  public final ArrayList<Carrier> allowedCarriers = new ArrayList();
  public final ArrayList<Carrier> excludedCarriers = new ArrayList();
  
  public CarrierRestrictions() {}
  
  public static final ArrayList<CarrierRestrictions> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CarrierRestrictions localCarrierRestrictions = new CarrierRestrictions();
      localCarrierRestrictions.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localCarrierRestrictions);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CarrierRestrictions> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((CarrierRestrictions)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != CarrierRestrictions.class) {
      return false;
    }
    paramObject = (CarrierRestrictions)paramObject;
    if (!HidlSupport.deepEquals(allowedCarriers, allowedCarriers)) {
      return false;
    }
    return HidlSupport.deepEquals(excludedCarriers, excludedCarriers);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(allowedCarriers)), Integer.valueOf(HidlSupport.deepHashCode(excludedCarriers)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    int i = paramHwBlob.getInt32(paramLong + 0L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, paramHwBlob.handle(), paramLong + 0L + 0L, true);
    allowedCarriers.clear();
    int j = 0;
    Carrier localCarrier;
    for (int k = 0; k < i; k++)
    {
      localCarrier = new Carrier();
      localCarrier.readEmbeddedFromParcel(paramHwParcel, localHwBlob, k * 56);
      allowedCarriers.add(localCarrier);
    }
    i = paramHwBlob.getInt32(paramLong + 16L + 8L);
    paramHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, paramHwBlob.handle(), 0L + (paramLong + 16L), true);
    excludedCarriers.clear();
    for (k = j; k < i; k++)
    {
      localCarrier = new Carrier();
      localCarrier.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, k * 56);
      excludedCarriers.add(localCarrier);
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
    localStringBuilder.append(".allowedCarriers = ");
    localStringBuilder.append(allowedCarriers);
    localStringBuilder.append(", .excludedCarriers = ");
    localStringBuilder.append(excludedCarriers);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    int i = allowedCarriers.size();
    paramHwBlob.putInt32(paramLong + 0L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 0L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 56);
    for (int k = 0; k < i; k++) {
      ((Carrier)allowedCarriers.get(k)).writeEmbeddedToBlob(localHwBlob, k * 56);
    }
    paramHwBlob.putBlob(paramLong + 0L + 0L, localHwBlob);
    i = excludedCarriers.size();
    paramHwBlob.putInt32(paramLong + 16L + 8L, i);
    paramHwBlob.putBool(paramLong + 16L + 12L, false);
    localHwBlob = new HwBlob(i * 56);
    for (k = j; k < i; k++) {
      ((Carrier)excludedCarriers.get(k)).writeEmbeddedToBlob(localHwBlob, k * 56);
    }
    paramHwBlob.putBlob(paramLong + 16L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
