package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaBroadcastSmsConfigInfo
{
  public int language;
  public boolean selected;
  public int serviceCategory;
  
  public CdmaBroadcastSmsConfigInfo() {}
  
  public static final ArrayList<CdmaBroadcastSmsConfigInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 12, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CdmaBroadcastSmsConfigInfo();
      ((CdmaBroadcastSmsConfigInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 12);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaBroadcastSmsConfigInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 12);
    while (j < i)
    {
      ((CdmaBroadcastSmsConfigInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 12);
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
    if (paramObject.getClass() != CdmaBroadcastSmsConfigInfo.class) {
      return false;
    }
    paramObject = (CdmaBroadcastSmsConfigInfo)paramObject;
    if (serviceCategory != serviceCategory) {
      return false;
    }
    if (language != language) {
      return false;
    }
    return selected == selected;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(serviceCategory))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(language))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(selected))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    serviceCategory = paramHwBlob.getInt32(0L + paramLong);
    language = paramHwBlob.getInt32(4L + paramLong);
    selected = paramHwBlob.getBool(8L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(12L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".serviceCategory = ");
    localStringBuilder.append(serviceCategory);
    localStringBuilder.append(", .language = ");
    localStringBuilder.append(language);
    localStringBuilder.append(", .selected = ");
    localStringBuilder.append(selected);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, serviceCategory);
    paramHwBlob.putInt32(4L + paramLong, language);
    paramHwBlob.putBool(8L + paramLong, selected);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(12);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
