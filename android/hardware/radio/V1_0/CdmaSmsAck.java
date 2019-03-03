package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaSmsAck
{
  public int errorClass;
  public int smsCauseCode;
  
  public CdmaSmsAck() {}
  
  public static final ArrayList<CdmaSmsAck> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 8, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CdmaSmsAck localCdmaSmsAck = new CdmaSmsAck();
      localCdmaSmsAck.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 8);
      localArrayList.add(localCdmaSmsAck);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaSmsAck> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 8);
    while (j < i)
    {
      ((CdmaSmsAck)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 8);
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
    if (paramObject.getClass() != CdmaSmsAck.class) {
      return false;
    }
    paramObject = (CdmaSmsAck)paramObject;
    if (errorClass != errorClass) {
      return false;
    }
    return smsCauseCode == smsCauseCode;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(errorClass))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(smsCauseCode))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    errorClass = paramHwBlob.getInt32(0L + paramLong);
    smsCauseCode = paramHwBlob.getInt32(4L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(8L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".errorClass = ");
    localStringBuilder.append(CdmaSmsErrorClass.toString(errorClass));
    localStringBuilder.append(", .smsCauseCode = ");
    localStringBuilder.append(smsCauseCode);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, errorClass);
    paramHwBlob.putInt32(4L + paramLong, smsCauseCode);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(8);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
