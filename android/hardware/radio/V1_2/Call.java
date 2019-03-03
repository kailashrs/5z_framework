package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class Call
{
  public int audioQuality;
  public final android.hardware.radio.V1_0.Call base = new android.hardware.radio.V1_0.Call();
  
  public Call() {}
  
  public static final ArrayList<Call> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 96, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      Call localCall = new Call();
      localCall.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 96);
      localArrayList.add(localCall);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<Call> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 96);
    while (j < i)
    {
      ((Call)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 96);
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
    if (paramObject.getClass() != Call.class) {
      return false;
    }
    paramObject = (Call)paramObject;
    if (!HidlSupport.deepEquals(base, base)) {
      return false;
    }
    return audioQuality == audioQuality;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(base)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(audioQuality))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    base.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    audioQuality = paramHwBlob.getInt32(88L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(96L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".base = ");
    localStringBuilder.append(base);
    localStringBuilder.append(", .audioQuality = ");
    localStringBuilder.append(AudioQuality.toString(audioQuality));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    base.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    paramHwBlob.putInt32(88L + paramLong, audioQuality);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(96);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
