package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaRedirectingNumberInfoRecord
{
  public final CdmaNumberInfoRecord redirectingNumber = new CdmaNumberInfoRecord();
  public int redirectingReason;
  
  public CdmaRedirectingNumberInfoRecord() {}
  
  public static final ArrayList<CdmaRedirectingNumberInfoRecord> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CdmaRedirectingNumberInfoRecord();
      ((CdmaRedirectingNumberInfoRecord)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaRedirectingNumberInfoRecord> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((CdmaRedirectingNumberInfoRecord)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != CdmaRedirectingNumberInfoRecord.class) {
      return false;
    }
    paramObject = (CdmaRedirectingNumberInfoRecord)paramObject;
    if (!HidlSupport.deepEquals(redirectingNumber, redirectingNumber)) {
      return false;
    }
    return redirectingReason == redirectingReason;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(redirectingNumber)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(redirectingReason))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    redirectingNumber.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, 0L + paramLong);
    redirectingReason = paramHwBlob.getInt32(24L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(32L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".redirectingNumber = ");
    localStringBuilder.append(redirectingNumber);
    localStringBuilder.append(", .redirectingReason = ");
    localStringBuilder.append(CdmaRedirectingReason.toString(redirectingReason));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    redirectingNumber.writeEmbeddedToBlob(paramHwBlob, 0L + paramLong);
    paramHwBlob.putInt32(24L + paramLong, redirectingReason);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
