package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CdmaSmsAddress
{
  public int digitMode;
  public final ArrayList<Byte> digits = new ArrayList();
  public int numberMode;
  public int numberPlan;
  public int numberType;
  
  public CdmaSmsAddress() {}
  
  public static final ArrayList<CdmaSmsAddress> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CdmaSmsAddress();
      ((CdmaSmsAddress)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CdmaSmsAddress> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((CdmaSmsAddress)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != CdmaSmsAddress.class) {
      return false;
    }
    paramObject = (CdmaSmsAddress)paramObject;
    if (digitMode != digitMode) {
      return false;
    }
    if (numberMode != numberMode) {
      return false;
    }
    if (numberType != numberType) {
      return false;
    }
    if (numberPlan != numberPlan) {
      return false;
    }
    return HidlSupport.deepEquals(digits, digits);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(digitMode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(numberMode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(numberType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(numberPlan))), Integer.valueOf(HidlSupport.deepHashCode(digits)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    digitMode = paramHwBlob.getInt32(paramLong + 0L);
    numberMode = paramHwBlob.getInt32(paramLong + 4L);
    numberType = paramHwBlob.getInt32(paramLong + 8L);
    numberPlan = paramHwBlob.getInt32(paramLong + 12L);
    int i = paramHwBlob.getInt32(paramLong + 16L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 16L + 0L, true);
    digits.clear();
    for (int j = 0; j < i; j++)
    {
      byte b = paramHwParcel.getInt8(j * 1);
      digits.add(Byte.valueOf(b));
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(32L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".digitMode = ");
    localStringBuilder.append(CdmaSmsDigitMode.toString(digitMode));
    localStringBuilder.append(", .numberMode = ");
    localStringBuilder.append(CdmaSmsNumberMode.toString(numberMode));
    localStringBuilder.append(", .numberType = ");
    localStringBuilder.append(CdmaSmsNumberType.toString(numberType));
    localStringBuilder.append(", .numberPlan = ");
    localStringBuilder.append(CdmaSmsNumberPlan.toString(numberPlan));
    localStringBuilder.append(", .digits = ");
    localStringBuilder.append(digits);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, digitMode);
    paramHwBlob.putInt32(4L + paramLong, numberMode);
    paramHwBlob.putInt32(paramLong + 8L, numberType);
    paramHwBlob.putInt32(paramLong + 12L, numberPlan);
    int i = digits.size();
    paramHwBlob.putInt32(paramLong + 16L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 16L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 1);
    while (j < i)
    {
      localHwBlob.putInt8(j * 1, ((Byte)digits.get(j)).byteValue());
      j++;
    }
    paramHwBlob.putBlob(16L + paramLong + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
