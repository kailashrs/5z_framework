package android.hardware.radio.V1_2;

import android.hardware.radio.V1_1.RadioAccessSpecifier;
import android.hardware.radio.V1_1.ScanType;
import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class NetworkScanRequest
{
  public boolean incrementalResults;
  public int incrementalResultsPeriodicity;
  public int interval;
  public int maxSearchTime;
  public final ArrayList<String> mccMncs = new ArrayList();
  public final ArrayList<RadioAccessSpecifier> specifiers = new ArrayList();
  public int type;
  
  public NetworkScanRequest() {}
  
  public static final ArrayList<NetworkScanRequest> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 56, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new NetworkScanRequest();
      ((NetworkScanRequest)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 56);
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
    HwBlob localHwBlob2 = new HwBlob(i * 56);
    while (j < i)
    {
      ((NetworkScanRequest)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 56);
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
    if (!HidlSupport.deepEquals(specifiers, specifiers)) {
      return false;
    }
    if (maxSearchTime != maxSearchTime) {
      return false;
    }
    if (incrementalResults != incrementalResults) {
      return false;
    }
    if (incrementalResultsPeriodicity != incrementalResultsPeriodicity) {
      return false;
    }
    return HidlSupport.deepEquals(mccMncs, mccMncs);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(interval))), Integer.valueOf(HidlSupport.deepHashCode(specifiers)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(maxSearchTime))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(incrementalResults))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(incrementalResultsPeriodicity))), Integer.valueOf(HidlSupport.deepHashCode(mccMncs)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    type = paramHwBlob.getInt32(paramLong + 0L);
    interval = paramHwBlob.getInt32(paramLong + 4L);
    int i = paramHwBlob.getInt32(paramLong + 8L + 8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 72, paramHwBlob.handle(), paramLong + 8L + 0L, true);
    specifiers.clear();
    Object localObject;
    for (int j = 0; j < i; j++)
    {
      localObject = new RadioAccessSpecifier();
      ((RadioAccessSpecifier)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 72);
      specifiers.add(localObject);
    }
    maxSearchTime = paramHwBlob.getInt32(paramLong + 24L);
    incrementalResults = paramHwBlob.getBool(paramLong + 28L);
    incrementalResultsPeriodicity = paramHwBlob.getInt32(paramLong + 32L);
    i = paramHwBlob.getInt32(paramLong + 40L + 8L);
    paramHwBlob = paramHwParcel.readEmbeddedBuffer(i * 16, paramHwBlob.handle(), paramLong + 40L + 0L, true);
    mccMncs.clear();
    for (j = 0; j < i; j++)
    {
      new String();
      localObject = paramHwBlob.getString(j * 16);
      paramHwParcel.readEmbeddedBuffer(((String)localObject).getBytes().length + 1, paramHwBlob.handle(), j * 16 + 0, false);
      mccMncs.add(localObject);
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
    localStringBuilder.append(".type = ");
    localStringBuilder.append(ScanType.toString(type));
    localStringBuilder.append(", .interval = ");
    localStringBuilder.append(interval);
    localStringBuilder.append(", .specifiers = ");
    localStringBuilder.append(specifiers);
    localStringBuilder.append(", .maxSearchTime = ");
    localStringBuilder.append(maxSearchTime);
    localStringBuilder.append(", .incrementalResults = ");
    localStringBuilder.append(incrementalResults);
    localStringBuilder.append(", .incrementalResultsPeriodicity = ");
    localStringBuilder.append(incrementalResultsPeriodicity);
    localStringBuilder.append(", .mccMncs = ");
    localStringBuilder.append(mccMncs);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, type);
    paramHwBlob.putInt32(paramLong + 4L, interval);
    int i = specifiers.size();
    paramHwBlob.putInt32(paramLong + 8L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 8L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 72);
    for (int k = 0; k < i; k++) {
      ((RadioAccessSpecifier)specifiers.get(k)).writeEmbeddedToBlob(localHwBlob, k * 72);
    }
    paramHwBlob.putBlob(paramLong + 8L + 0L, localHwBlob);
    paramHwBlob.putInt32(paramLong + 24L, maxSearchTime);
    paramHwBlob.putBool(paramLong + 28L, incrementalResults);
    paramHwBlob.putInt32(paramLong + 32L, incrementalResultsPeriodicity);
    i = mccMncs.size();
    paramHwBlob.putInt32(paramLong + 40L + 8L, i);
    paramHwBlob.putBool(paramLong + 40L + 12L, false);
    localHwBlob = new HwBlob(i * 16);
    for (k = j; k < i; k++) {
      localHwBlob.putString(k * 16, (String)mccMncs.get(k));
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
