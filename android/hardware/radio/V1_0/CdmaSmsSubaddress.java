package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaSmsSubaddress
{
  public final ArrayList<Byte> digits = new ArrayList();
  public boolean odd;
  public int subaddressType;
  
  public CdmaSmsSubaddress() {}
  
  public static final ArrayList<CdmaSmsSubaddress> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CdmaSmsSubaddress();
      ((CdmaSmsSubaddress)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaSmsSubaddress> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((CdmaSmsSubaddress)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != CdmaSmsSubaddress.class) {
      return false;
    }
    paramObject = (CdmaSmsSubaddress)paramObject;
    if (subaddressType != subaddressType) {
      return false;
    }
    if (odd != odd) {
      return false;
    }
    return HidlSupport.deepEquals(digits, digits);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(subaddressType))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(odd))), Integer.valueOf(HidlSupport.deepHashCode(digits)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    subaddressType = paramHwBlob.getInt32(paramLong + 0L);
    odd = paramHwBlob.getBool(paramLong + 4L);
    int i = paramHwBlob.getInt32(paramLong + 8L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 8L + 0L, true);
    digits.clear();
    for (int j = 0; j < i; j++)
    {
      byte b = paramHwParcel.getInt8(j * 1);
      digits.add(Byte.valueOf(b));
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
    localStringBuilder.append(".subaddressType = ");
    localStringBuilder.append(CdmaSmsSubaddressType.toString(subaddressType));
    localStringBuilder.append(", .odd = ");
    localStringBuilder.append(odd);
    localStringBuilder.append(", .digits = ");
    localStringBuilder.append(digits);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, subaddressType);
    paramHwBlob.putBool(4L + paramLong, odd);
    int i = digits.size();
    paramHwBlob.putInt32(paramLong + 8L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 8L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 1);
    while (j < i)
    {
      localHwBlob.putInt8(j * 1, ((Byte)digits.get(j)).byteValue());
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
