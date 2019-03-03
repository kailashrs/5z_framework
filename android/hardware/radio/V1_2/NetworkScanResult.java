package android.hardware.radio.V1_2;

import android.hardware.radio.V1_0.RadioError;
import android.hardware.radio.V1_1.ScanStatus;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class NetworkScanResult
{
  public int error;
  public final ArrayList<CellInfo> networkInfos = new ArrayList();
  public int status;
  
  public NetworkScanResult() {}
  
  public static final ArrayList<NetworkScanResult> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new NetworkScanResult();
      ((NetworkScanResult)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<NetworkScanResult> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((NetworkScanResult)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != NetworkScanResult.class) {
      return false;
    }
    paramObject = (NetworkScanResult)paramObject;
    if (status != status) {
      return false;
    }
    if (error != error) {
      return false;
    }
    return HidlSupport.deepEquals(networkInfos, networkInfos);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(status))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(error))), Integer.valueOf(HidlSupport.deepHashCode(networkInfos)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    status = paramHwBlob.getInt32(paramLong + 0L);
    error = paramHwBlob.getInt32(paramLong + 4L);
    int i = paramHwBlob.getInt32(paramLong + 8L + 8L);
    paramHwBlob = paramHwParcel.readEmbeddedBuffer(i * 112, paramHwBlob.handle(), paramLong + 8L + 0L, true);
    networkInfos.clear();
    for (int j = 0; j < i; j++)
    {
      CellInfo localCellInfo = new CellInfo();
      localCellInfo.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, j * 112);
      networkInfos.add(localCellInfo);
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
    localStringBuilder.append(".status = ");
    localStringBuilder.append(ScanStatus.toString(status));
    localStringBuilder.append(", .error = ");
    localStringBuilder.append(RadioError.toString(error));
    localStringBuilder.append(", .networkInfos = ");
    localStringBuilder.append(networkInfos);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, status);
    paramHwBlob.putInt32(4L + paramLong, error);
    int i = networkInfos.size();
    paramHwBlob.putInt32(paramLong + 8L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 8L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 112);
    while (j < i)
    {
      ((CellInfo)networkInfos.get(j)).writeEmbeddedToBlob(localHwBlob, j * 112);
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
