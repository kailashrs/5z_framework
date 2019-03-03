package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class EvdoSignalStrength
{
  public int dbm;
  public int ecio;
  public int signalNoiseRatio;
  
  public EvdoSignalStrength() {}
  
  public static final ArrayList<EvdoSignalStrength> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 12, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new EvdoSignalStrength();
      ((EvdoSignalStrength)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 12);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<EvdoSignalStrength> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 12);
    while (j < i)
    {
      ((EvdoSignalStrength)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 12);
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
    if (paramObject.getClass() != EvdoSignalStrength.class) {
      return false;
    }
    paramObject = (EvdoSignalStrength)paramObject;
    if (dbm != dbm) {
      return false;
    }
    if (ecio != ecio) {
      return false;
    }
    return signalNoiseRatio == signalNoiseRatio;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(dbm))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(ecio))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(signalNoiseRatio))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    dbm = paramHwBlob.getInt32(0L + paramLong);
    ecio = paramHwBlob.getInt32(4L + paramLong);
    signalNoiseRatio = paramHwBlob.getInt32(8L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(12L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".dbm = ");
    localStringBuilder.append(dbm);
    localStringBuilder.append(", .ecio = ");
    localStringBuilder.append(ecio);
    localStringBuilder.append(", .signalNoiseRatio = ");
    localStringBuilder.append(signalNoiseRatio);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, dbm);
    paramHwBlob.putInt32(4L + paramLong, ecio);
    paramHwBlob.putInt32(8L + paramLong, signalNoiseRatio);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(12);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
