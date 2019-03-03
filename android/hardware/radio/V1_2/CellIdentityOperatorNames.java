package android.hardware.radio.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellIdentityOperatorNames
{
  public String alphaLong = new String();
  public String alphaShort = new String();
  
  public CellIdentityOperatorNames() {}
  
  public static final ArrayList<CellIdentityOperatorNames> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 32, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CellIdentityOperatorNames localCellIdentityOperatorNames = new CellIdentityOperatorNames();
      localCellIdentityOperatorNames.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 32);
      localArrayList.add(localCellIdentityOperatorNames);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellIdentityOperatorNames> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 32);
    while (j < i)
    {
      ((CellIdentityOperatorNames)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 32);
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
    if (paramObject.getClass() != CellIdentityOperatorNames.class) {
      return false;
    }
    paramObject = (CellIdentityOperatorNames)paramObject;
    if (!HidlSupport.deepEquals(alphaLong, alphaLong)) {
      return false;
    }
    return HidlSupport.deepEquals(alphaShort, alphaShort);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(alphaLong)), Integer.valueOf(HidlSupport.deepHashCode(alphaShort)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    alphaLong = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(alphaLong.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    alphaShort = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(alphaShort.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(32L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".alphaLong = ");
    localStringBuilder.append(alphaLong);
    localStringBuilder.append(", .alphaShort = ");
    localStringBuilder.append(alphaShort);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, alphaLong);
    paramHwBlob.putString(16L + paramLong, alphaShort);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(32);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
