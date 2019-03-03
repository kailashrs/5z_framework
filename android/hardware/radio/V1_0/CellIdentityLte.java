package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class CellIdentityLte
{
  public int ci;
  public int earfcn;
  public String mcc = new String();
  public String mnc = new String();
  public int pci;
  public int tac;
  
  public CellIdentityLte() {}
  
  public static final ArrayList<CellIdentityLte> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 48, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new CellIdentityLte();
      ((CellIdentityLte)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 48);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<CellIdentityLte> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 48);
    while (j < i)
    {
      ((CellIdentityLte)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 48);
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
    if (paramObject.getClass() != CellIdentityLte.class) {
      return false;
    }
    paramObject = (CellIdentityLte)paramObject;
    if (!HidlSupport.deepEquals(mcc, mcc)) {
      return false;
    }
    if (!HidlSupport.deepEquals(mnc, mnc)) {
      return false;
    }
    if (ci != ci) {
      return false;
    }
    if (pci != pci) {
      return false;
    }
    if (tac != tac) {
      return false;
    }
    return earfcn == earfcn;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(mcc)), Integer.valueOf(HidlSupport.deepHashCode(mnc)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(ci))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(pci))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(tac))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(earfcn))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    mcc = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(mcc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    mnc = paramHwBlob.getString(paramLong + 16L);
    paramHwParcel.readEmbeddedBuffer(mnc.getBytes().length + 1, paramHwBlob.handle(), paramLong + 16L + 0L, false);
    ci = paramHwBlob.getInt32(paramLong + 32L);
    pci = paramHwBlob.getInt32(paramLong + 36L);
    tac = paramHwBlob.getInt32(paramLong + 40L);
    earfcn = paramHwBlob.getInt32(paramLong + 44L);
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
    localStringBuilder.append(", .ci = ");
    localStringBuilder.append(ci);
    localStringBuilder.append(", .pci = ");
    localStringBuilder.append(pci);
    localStringBuilder.append(", .tac = ");
    localStringBuilder.append(tac);
    localStringBuilder.append(", .earfcn = ");
    localStringBuilder.append(earfcn);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(0L + paramLong, mcc);
    paramHwBlob.putString(16L + paramLong, mnc);
    paramHwBlob.putInt32(32L + paramLong, ci);
    paramHwBlob.putInt32(36L + paramLong, pci);
    paramHwBlob.putInt32(40L + paramLong, tac);
    paramHwBlob.putInt32(44L + paramLong, earfcn);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(48);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
