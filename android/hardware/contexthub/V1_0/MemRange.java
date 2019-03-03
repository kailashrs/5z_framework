package android.hardware.contexthub.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class MemRange
{
  public int flags;
  public int freeBytes;
  public int totalBytes;
  public int type;
  
  public MemRange() {}
  
  public static final ArrayList<MemRange> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new MemRange();
      ((MemRange)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 16);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<MemRange> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 16);
    while (j < i)
    {
      ((MemRange)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 16);
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
    if (paramObject.getClass() != MemRange.class) {
      return false;
    }
    paramObject = (MemRange)paramObject;
    if (totalBytes != totalBytes) {
      return false;
    }
    if (freeBytes != freeBytes) {
      return false;
    }
    if (type != type) {
      return false;
    }
    return HidlSupport.deepEquals(Integer.valueOf(flags), Integer.valueOf(flags));
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(totalBytes))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(freeBytes))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(flags))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    totalBytes = paramHwBlob.getInt32(0L + paramLong);
    freeBytes = paramHwBlob.getInt32(4L + paramLong);
    type = paramHwBlob.getInt32(8L + paramLong);
    flags = paramHwBlob.getInt32(12L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(16L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".totalBytes = ");
    localStringBuilder.append(totalBytes);
    localStringBuilder.append(", .freeBytes = ");
    localStringBuilder.append(freeBytes);
    localStringBuilder.append(", .type = ");
    localStringBuilder.append(HubMemoryType.toString(type));
    localStringBuilder.append(", .flags = ");
    localStringBuilder.append(HubMemoryFlag.dumpBitfield(flags));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, totalBytes);
    paramHwBlob.putInt32(4L + paramLong, freeBytes);
    paramHwBlob.putInt32(8L + paramLong, type);
    paramHwBlob.putInt32(12L + paramLong, flags);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(16);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
