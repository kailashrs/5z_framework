package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SignalStrength
{
  public final CdmaSignalStrength cdma = new CdmaSignalStrength();
  public final EvdoSignalStrength evdo = new EvdoSignalStrength();
  public final GsmSignalStrength gw = new GsmSignalStrength();
  public final LteSignalStrength lte = new LteSignalStrength();
  public final TdScdmaSignalStrength tdScdma = new TdScdmaSignalStrength();
  
  public SignalStrength() {}
  
  public static final ArrayList<SignalStrength> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 60, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      SignalStrength localSignalStrength = new SignalStrength();
      localSignalStrength.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 60);
      localArrayList.add(localSignalStrength);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SignalStrength> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 60);
    while (j < i)
    {
      ((SignalStrength)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 60);
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
    if (paramObject.getClass() != SignalStrength.class) {
      return false;
    }
    paramObject = (SignalStrength)paramObject;
    if (!HidlSupport.deepEquals(gw, gw)) {
      return false;
    }
    if (!HidlSupport.deepEquals(cdma, cdma)) {
      return false;
    }
    if (!HidlSupport.deepEquals(evdo, evdo)) {
      return false;
    }
    if (!HidlSupport.deepEquals(lte, lte)) {
      return false;
    }
    return HidlSupport.deepEquals(tdScdma, tdScdma);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(gw)), Integer.valueOf(HidlSupport.deepHashCode(cdma)), Integer.valueOf(HidlSupport.deepHashCode(evdo)), Integer.valueOf(HidlSupport.deepHashCode(lte)), Integer.valueOf(HidlSupport.deepHashCode(tdScdma)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    gw.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    cdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 12L + paramLong);
    evdo.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 20L + paramLong);
    lte.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 32L + paramLong);
    tdScdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 56L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(60L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".gw = ");
    localStringBuilder.append(gw);
    localStringBuilder.append(", .cdma = ");
    localStringBuilder.append(cdma);
    localStringBuilder.append(", .evdo = ");
    localStringBuilder.append(evdo);
    localStringBuilder.append(", .lte = ");
    localStringBuilder.append(lte);
    localStringBuilder.append(", .tdScdma = ");
    localStringBuilder.append(tdScdma);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    gw.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    cdma.writeEmbeddedToBlob(paramHwBlob, 12L + paramLong);
    evdo.writeEmbeddedToBlob(paramHwBlob, 20L + paramLong);
    lte.writeEmbeddedToBlob(paramHwBlob, 32L + paramLong);
    tdScdma.writeEmbeddedToBlob(paramHwBlob, 56L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(60);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
