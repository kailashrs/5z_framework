package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SimRefreshResult
{
  public String aid = new String();
  public int efId;
  public int type;
  
  public SimRefreshResult() {}
  
  public static final ArrayList<SimRefreshResult> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new SimRefreshResult();
      ((SimRefreshResult)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SimRefreshResult> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((SimRefreshResult)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != SimRefreshResult.class) {
      return false;
    }
    paramObject = (SimRefreshResult)paramObject;
    if (type != type) {
      return false;
    }
    if (efId != efId) {
      return false;
    }
    return HidlSupport.deepEquals(aid, aid);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(efId))), Integer.valueOf(HidlSupport.deepHashCode(aid)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    type = paramHwBlob.getInt32(paramLong + 0L);
    efId = paramHwBlob.getInt32(paramLong + 4L);
    aid = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(aid.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(24L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".type = ");
    localStringBuilder.append(SimRefreshType.toString(type));
    localStringBuilder.append(", .efId = ");
    localStringBuilder.append(efId);
    localStringBuilder.append(", .aid = ");
    localStringBuilder.append(aid);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, type);
    paramHwBlob.putInt32(4L + paramLong, efId);
    paramHwBlob.putString(8L + paramLong, aid);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(24);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
