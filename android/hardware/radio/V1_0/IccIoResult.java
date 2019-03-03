package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class IccIoResult
{
  public String simResponse = new String();
  public int sw1;
  public int sw2;
  
  public IccIoResult() {}
  
  public static final ArrayList<IccIoResult> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new IccIoResult();
      ((IccIoResult)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<IccIoResult> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((IccIoResult)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != IccIoResult.class) {
      return false;
    }
    paramObject = (IccIoResult)paramObject;
    if (sw1 != sw1) {
      return false;
    }
    if (sw2 != sw2) {
      return false;
    }
    return HidlSupport.deepEquals(simResponse, simResponse);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(sw1))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(sw2))), Integer.valueOf(HidlSupport.deepHashCode(simResponse)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    sw1 = paramHwBlob.getInt32(paramLong + 0L);
    sw2 = paramHwBlob.getInt32(paramLong + 4L);
    simResponse = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(simResponse.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(24L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".sw1 = ");
    localStringBuilder.append(sw1);
    localStringBuilder.append(", .sw2 = ");
    localStringBuilder.append(sw2);
    localStringBuilder.append(", .simResponse = ");
    localStringBuilder.append(simResponse);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, sw1);
    paramHwBlob.putInt32(4L + paramLong, sw2);
    paramHwBlob.putString(8L + paramLong, simResponse);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(24);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
