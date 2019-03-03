package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class SelectUiccSub
{
  public int actStatus;
  public int appIndex;
  public int slot;
  public int subType;
  
  public SelectUiccSub() {}
  
  public static final ArrayList<SelectUiccSub> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new SelectUiccSub();
      ((SelectUiccSub)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 16);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<SelectUiccSub> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 16);
    while (j < i)
    {
      ((SelectUiccSub)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 16);
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
    if (paramObject.getClass() != SelectUiccSub.class) {
      return false;
    }
    paramObject = (SelectUiccSub)paramObject;
    if (slot != slot) {
      return false;
    }
    if (appIndex != appIndex) {
      return false;
    }
    if (subType != subType) {
      return false;
    }
    return actStatus == actStatus;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(slot))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(appIndex))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(subType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(actStatus))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    slot = paramHwBlob.getInt32(0L + paramLong);
    appIndex = paramHwBlob.getInt32(4L + paramLong);
    subType = paramHwBlob.getInt32(8L + paramLong);
    actStatus = paramHwBlob.getInt32(12L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(16L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".slot = ");
    localStringBuilder.append(slot);
    localStringBuilder.append(", .appIndex = ");
    localStringBuilder.append(appIndex);
    localStringBuilder.append(", .subType = ");
    localStringBuilder.append(SubscriptionType.toString(subType));
    localStringBuilder.append(", .actStatus = ");
    localStringBuilder.append(UiccSubActStatus.toString(actStatus));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, slot);
    paramHwBlob.putInt32(4L + paramLong, appIndex);
    paramHwBlob.putInt32(8L + paramLong, subType);
    paramHwBlob.putInt32(12L + paramLong, actStatus);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(16);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
