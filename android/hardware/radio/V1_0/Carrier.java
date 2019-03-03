package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class Carrier
{
  public String matchData = new String();
  public int matchType;
  public String mcc = new String();
  public String mnc = new String();
  
  public Carrier() {}
  
  public static final ArrayList<Carrier> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      Carrier localCarrier = new Carrier();
      localCarrier.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 56);
      localArrayList.add(localCarrier);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<Carrier> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 56);
    while (j < i)
    {
      ((Carrier)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 56);
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
    if (paramObject.getClass() != Carrier.class) {
      return false;
    }
    paramObject = (Carrier)paramObject;
    if (!HidlSupport.deepEquals(mcc, mcc)) {
      return false;
    }
    if (!HidlSupport.deepEquals(mnc, mnc)) {
      return false;
    }
    if (matchType != matchType) {
      return false;
    }
    return HidlSupport.deepEquals(matchData, matchData);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(mcc)), Integer.valueOf(HidlSupport.deepHashCode(mnc)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(matchType))), Integer.valueOf(HidlSupport.deepHashCode(matchData)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    mcc = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(mcc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    mnc = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(mnc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    matchType = paramHwBlob.getInt32(paramLong + 32L);
    matchData = paramHwBlob.getString(paramLong + 40L);
    paramHwParcel.readEmbeddedBuffer(matchData.getBytes().length + 1, paramHwBlob.handle(), paramLong + 40L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(56L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".mcc = ");
    localStringBuilder.append(mcc);
    localStringBuilder.append(", .mnc = ");
    localStringBuilder.append(mnc);
    localStringBuilder.append(", .matchType = ");
    localStringBuilder.append(CarrierMatchType.toString(matchType));
    localStringBuilder.append(", .matchData = ");
    localStringBuilder.append(matchData);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, mcc);
    paramHwBlob.putString(16L + paramLong, mnc);
    paramHwBlob.putInt32(32L + paramLong, matchType);
    paramHwBlob.putString(40L + paramLong, matchData);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(56);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
