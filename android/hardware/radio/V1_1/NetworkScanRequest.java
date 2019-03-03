package android.hardware.radio.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class NetworkScanRequest
{
  public int interval;
  public final ArrayList<RadioAccessSpecifier> specifiers = new ArrayList();
  public int type;
  
  public NetworkScanRequest() {}
  
  public static final ArrayList<NetworkScanRequest> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new NetworkScanRequest();
      ((NetworkScanRequest)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<NetworkScanRequest> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((NetworkScanRequest)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != NetworkScanRequest.class) {
      return false;
    }
    paramObject = (NetworkScanRequest)paramObject;
    if (type != type) {
      return false;
    }
    if (interval != interval) {
      return false;
    }
    return HidlSupport.deepEquals(specifiers, specifiers);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(interval))), Integer.valueOf(HidlSupport.deepHashCode(specifiers)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    type = paramHwBlob.getInt32(paramLong + 0L);
    interval = paramHwBlob.getInt32(paramLong + 4L);
    int i = paramHwBlob.getInt32(paramLong + 8L + 8L);
    paramHwBlob = paramHwParcel.readEmbeddedBuffer(i * 72, paramHwBlob.handle(), paramLong + 8L + 0L, true);
    specifiers.clear();
    for (int j = 0; j < i; j++)
    {
      RadioAccessSpecifier localRadioAccessSpecifier = new RadioAccessSpecifier();
      localRadioAccessSpecifier.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, j * 72);
      specifiers.add(localRadioAccessSpecifier);
    }
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
    localStringBuilder.append(ScanType.toString(type));
    localStringBuilder.append(", .interval = ");
    localStringBuilder.append(interval);
    localStringBuilder.append(", .specifiers = ");
    localStringBuilder.append(specifiers);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, type);
    paramHwBlob.putInt32(4L + paramLong, interval);
    int i = specifiers.size();
    paramHwBlob.putInt32(paramLong + 8L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 8L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 72);
    while (j < i)
    {
      ((RadioAccessSpecifier)specifiers.get(j)).writeEmbeddedToBlob(localHwBlob, j * 72);
      j++;
    }
    paramHwBlob.putBlob(8L + paramLong + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(24);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
