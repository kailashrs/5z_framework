package android.hardware.radio.V1_2;

import android.hardware.radio.V1_0.CdmaSignalStrength;
import android.hardware.radio.V1_0.EvdoSignalStrength;
import android.hardware.radio.V1_0.GsmSignalStrength;
import android.hardware.radio.V1_0.LteSignalStrength;
import android.hardware.radio.V1_0.TdScdmaSignalStrength;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SignalStrength
{
  public final CdmaSignalStrength cdma = new CdmaSignalStrength();
  public final EvdoSignalStrength evdo = new EvdoSignalStrength();
  public final GsmSignalStrength gsm = new GsmSignalStrength();
  public final LteSignalStrength lte = new LteSignalStrength();
  public final TdScdmaSignalStrength tdScdma = new TdScdmaSignalStrength();
  public final WcdmaSignalStrength wcdma = new WcdmaSignalStrength();
  
  public SignalStrength() {}
  
  public static final ArrayList<SignalStrength> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 76, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      SignalStrength localSignalStrength = new SignalStrength();
      localSignalStrength.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 76);
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
    HwBlob localHwBlob2 = new HwBlob(i * 76);
    while (j < i)
    {
      ((SignalStrength)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 76);
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
    if (!HidlSupport.deepEquals(gsm, gsm)) {
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
    if (!HidlSupport.deepEquals(tdScdma, tdScdma)) {
      return false;
    }
    return HidlSupport.deepEquals(wcdma, wcdma);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(gsm)), Integer.valueOf(HidlSupport.deepHashCode(cdma)), Integer.valueOf(HidlSupport.deepHashCode(evdo)), Integer.valueOf(HidlSupport.deepHashCode(lte)), Integer.valueOf(HidlSupport.deepHashCode(tdScdma)), Integer.valueOf(HidlSupport.deepHashCode(wcdma)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    gsm.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    cdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 12L + paramLong);
    evdo.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 20L + paramLong);
    lte.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 32L + paramLong);
    tdScdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 56L + paramLong);
    wcdma.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 60L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(76L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".gsm = ");
    localStringBuilder.append(gsm);
    localStringBuilder.append(", .cdma = ");
    localStringBuilder.append(cdma);
    localStringBuilder.append(", .evdo = ");
    localStringBuilder.append(evdo);
    localStringBuilder.append(", .lte = ");
    localStringBuilder.append(lte);
    localStringBuilder.append(", .tdScdma = ");
    localStringBuilder.append(tdScdma);
    localStringBuilder.append(", .wcdma = ");
    localStringBuilder.append(wcdma);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    gsm.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    cdma.writeEmbeddedToBlob(paramHwBlob, 12L + paramLong);
    evdo.writeEmbeddedToBlob(paramHwBlob, 20L + paramLong);
    lte.writeEmbeddedToBlob(paramHwBlob, 32L + paramLong);
    tdScdma.writeEmbeddedToBlob(paramHwBlob, 56L + paramLong);
    wcdma.writeEmbeddedToBlob(paramHwBlob, 60L + paramLong);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(76);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
