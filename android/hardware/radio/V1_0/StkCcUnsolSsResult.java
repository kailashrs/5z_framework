package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class StkCcUnsolSsResult
{
  public final ArrayList<CfData> cfData = new ArrayList();
  public int requestType;
  public int result;
  public int serviceClass;
  public int serviceType;
  public final ArrayList<SsInfoData> ssInfo = new ArrayList();
  public int teleserviceType;
  
  public StkCcUnsolSsResult() {}
  
  public static final ArrayList<StkCcUnsolSsResult> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new StkCcUnsolSsResult();
      ((StkCcUnsolSsResult)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 56);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<StkCcUnsolSsResult> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 56);
    while (j < i)
    {
      ((StkCcUnsolSsResult)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 56);
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
    if (paramObject.getClass() != StkCcUnsolSsResult.class) {
      return false;
    }
    paramObject = (StkCcUnsolSsResult)paramObject;
    if (serviceType != serviceType) {
      return false;
    }
    if (requestType != requestType) {
      return false;
    }
    if (teleserviceType != teleserviceType) {
      return false;
    }
    if (!HidlSupport.deepEquals(Integer.valueOf(serviceClass), Integer.valueOf(serviceClass))) {
      return false;
    }
    if (result != result) {
      return false;
    }
    if (!HidlSupport.deepEquals(ssInfo, ssInfo)) {
      return false;
    }
    return HidlSupport.deepEquals(cfData, cfData);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(serviceType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(requestType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(teleserviceType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(serviceClass))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(result))), Integer.valueOf(HidlSupport.deepHashCode(ssInfo)), Integer.valueOf(HidlSupport.deepHashCode(cfData)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    serviceType = paramHwBlob.getInt32(paramLong + 0L);
    requestType = paramHwBlob.getInt32(paramLong + 4L);
    teleserviceType = paramHwBlob.getInt32(paramLong + 8L);
    serviceClass = paramHwBlob.getInt32(paramLong + 12L);
    result = paramHwBlob.getInt32(paramLong + 16L);
    int i = paramHwBlob.getInt32(paramLong + 24L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, paramHwBlob.handle(), paramLong + 24L + 0L, true);
    ssInfo.clear();
    int j = 0;
    for (int k = 0; k < i; k++)
    {
      SsInfoData localSsInfoData = new SsInfoData();
      localSsInfoData.readEmbeddedFromParcel(paramHwParcel, localHwBlob, k * 16);
      ssInfo.add(localSsInfoData);
    }
    i = paramHwBlob.getInt32(paramLong + 40L + 8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, paramHwBlob.handle(), 0L + (paramLong + 40L), true);
    cfData.clear();
    for (k = j; k < i; k++)
    {
      paramHwBlob = new CfData();
      paramHwBlob.readEmbeddedFromParcel(paramHwParcel, localHwBlob, k * 16);
      cfData.add(paramHwBlob);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(56L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".serviceType = ");
    localStringBuilder.append(SsServiceType.toString(serviceType));
    localStringBuilder.append(", .requestType = ");
    localStringBuilder.append(SsRequestType.toString(requestType));
    localStringBuilder.append(", .teleserviceType = ");
    localStringBuilder.append(SsTeleserviceType.toString(teleserviceType));
    localStringBuilder.append(", .serviceClass = ");
    localStringBuilder.append(SuppServiceClass.dumpBitfield(serviceClass));
    localStringBuilder.append(", .result = ");
    localStringBuilder.append(RadioError.toString(result));
    localStringBuilder.append(", .ssInfo = ");
    localStringBuilder.append(ssInfo);
    localStringBuilder.append(", .cfData = ");
    localStringBuilder.append(cfData);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, serviceType);
    paramHwBlob.putInt32(paramLong + 4L, requestType);
    paramHwBlob.putInt32(paramLong + 8L, teleserviceType);
    paramHwBlob.putInt32(paramLong + 12L, serviceClass);
    paramHwBlob.putInt32(paramLong + 16L, result);
    int i = ssInfo.size();
    paramHwBlob.putInt32(paramLong + 24L + 8L, i);
    paramHwBlob.putBool(paramLong + 24L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 16);
    for (int j = 0; j < i; j++) {
      ((SsInfoData)ssInfo.get(j)).writeEmbeddedToBlob(localHwBlob, j * 16);
    }
    paramHwBlob.putBlob(paramLong + 24L + 0L, localHwBlob);
    i = cfData.size();
    paramHwBlob.putInt32(paramLong + 40L + 8L, i);
    j = 0;
    paramHwBlob.putBool(paramLong + 40L + 12L, false);
    localHwBlob = new HwBlob(i * 16);
    while (j < i)
    {
      ((CfData)cfData.get(j)).writeEmbeddedToBlob(localHwBlob, j * 16);
      j++;
    }
    paramHwBlob.putBlob(paramLong + 40L + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(56);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
