package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellIdentityWcdma
{
  public int cid;
  public int lac;
  public String mcc = new String();
  public String mnc = new String();
  public int psc;
  public int uarfcn;
  
  public CellIdentityWcdma() {}
  
  public static final ArrayList<CellIdentityWcdma> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 48, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      CellIdentityWcdma localCellIdentityWcdma = new CellIdentityWcdma();
      localCellIdentityWcdma.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 48);
      localArrayList.add(localCellIdentityWcdma);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellIdentityWcdma> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 48);
    while (j < i)
    {
      ((CellIdentityWcdma)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 48);
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
    if (paramObject.getClass() != CellIdentityWcdma.class) {
      return false;
    }
    paramObject = (CellIdentityWcdma)paramObject;
    if (!HidlSupport.deepEquals(mcc, mcc)) {
      return false;
    }
    if (!HidlSupport.deepEquals(mnc, mnc)) {
      return false;
    }
    if (lac != lac) {
      return false;
    }
    if (cid != cid) {
      return false;
    }
    if (psc != psc) {
      return false;
    }
    return uarfcn == uarfcn;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(mcc)), Integer.valueOf(HidlSupport.deepHashCode(mnc)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(lac))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cid))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(psc))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(uarfcn))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    mcc = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(mcc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    mnc = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(mnc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    lac = paramHwBlob.getInt32(paramLong + 32L);
    cid = paramHwBlob.getInt32(paramLong + 36L);
    psc = paramHwBlob.getInt32(paramLong + 40L);
    uarfcn = paramHwBlob.getInt32(paramLong + 44L);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(48L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".mcc = ");
    localStringBuilder.append(mcc);
    localStringBuilder.append(", .mnc = ");
    localStringBuilder.append(mnc);
    localStringBuilder.append(", .lac = ");
    localStringBuilder.append(lac);
    localStringBuilder.append(", .cid = ");
    localStringBuilder.append(cid);
    localStringBuilder.append(", .psc = ");
    localStringBuilder.append(psc);
    localStringBuilder.append(", .uarfcn = ");
    localStringBuilder.append(uarfcn);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, mcc);
    paramHwBlob.putString(16L + paramLong, mnc);
    paramHwBlob.putInt32(32L + paramLong, lac);
    paramHwBlob.putInt32(36L + paramLong, cid);
    paramHwBlob.putInt32(40L + paramLong, psc);
    paramHwBlob.putInt32(44L + paramLong, uarfcn);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(48);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
