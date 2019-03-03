package android.hardware.cas.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class HidlCasPluginDescriptor
{
  public int caSystemId;
  public String name = new String();
  
  public HidlCasPluginDescriptor() {}
  
  public static final ArrayList<HidlCasPluginDescriptor> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      HidlCasPluginDescriptor localHidlCasPluginDescriptor = new HidlCasPluginDescriptor();
      localHidlCasPluginDescriptor.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localHidlCasPluginDescriptor);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<HidlCasPluginDescriptor> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((HidlCasPluginDescriptor)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != HidlCasPluginDescriptor.class) {
      return false;
    }
    paramObject = (HidlCasPluginDescriptor)paramObject;
    if (caSystemId != caSystemId) {
      return false;
    }
    return HidlSupport.deepEquals(name, name);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(caSystemId))), Integer.valueOf(HidlSupport.deepHashCode(name)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    caSystemId = paramHwBlob.getInt32(paramLong + 0L);
    name = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(name.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(24L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".caSystemId = ");
    localStringBuilder.append(caSystemId);
    localStringBuilder.append(", .name = ");
    localStringBuilder.append(name);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, caSystemId);
    paramHwBlob.putString(8L + paramLong, name);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(24);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
