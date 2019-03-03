package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class GsmBroadcastSmsConfigInfo
{
  public int fromCodeScheme;
  public int fromServiceId;
  public boolean selected;
  public int toCodeScheme;
  public int toServiceId;
  
  public GsmBroadcastSmsConfigInfo() {}
  
  public static final ArrayList<GsmBroadcastSmsConfigInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 20, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new GsmBroadcastSmsConfigInfo();
      ((GsmBroadcastSmsConfigInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 20);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<GsmBroadcastSmsConfigInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 20);
    while (j < i)
    {
      ((GsmBroadcastSmsConfigInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 20);
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
    if (paramObject.getClass() != GsmBroadcastSmsConfigInfo.class) {
      return false;
    }
    paramObject = (GsmBroadcastSmsConfigInfo)paramObject;
    if (fromServiceId != fromServiceId) {
      return false;
    }
    if (toServiceId != toServiceId) {
      return false;
    }
    if (fromCodeScheme != fromCodeScheme) {
      return false;
    }
    if (toCodeScheme != toCodeScheme) {
      return false;
    }
    return selected == selected;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(fromServiceId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(toServiceId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(fromCodeScheme))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(toCodeScheme))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(selected))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    fromServiceId = paramHwBlob.getInt32(0L + paramLong);
    toServiceId = paramHwBlob.getInt32(4L + paramLong);
    fromCodeScheme = paramHwBlob.getInt32(8L + paramLong);
    toCodeScheme = paramHwBlob.getInt32(12L + paramLong);
    selected = paramHwBlob.getBool(16L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(20L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".fromServiceId = ");
    localStringBuilder.append(fromServiceId);
    localStringBuilder.append(", .toServiceId = ");
    localStringBuilder.append(toServiceId);
    localStringBuilder.append(", .fromCodeScheme = ");
    localStringBuilder.append(fromCodeScheme);
    localStringBuilder.append(", .toCodeScheme = ");
    localStringBuilder.append(toCodeScheme);
    localStringBuilder.append(", .selected = ");
    localStringBuilder.append(selected);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, fromServiceId);
    paramHwBlob.putInt32(4L + paramLong, toServiceId);
    paramHwBlob.putInt32(8L + paramLong, fromCodeScheme);
    paramHwBlob.putInt32(12L + paramLong, toCodeScheme);
    paramHwBlob.putBool(16L + paramLong, selected);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(20);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
