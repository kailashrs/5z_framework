package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class PcoDataInfo
{
  public String bearerProto = new String();
  public int cid;
  public final ArrayList<Byte> contents = new ArrayList();
  public int pcoId;
  
  public PcoDataInfo() {}
  
  public static final ArrayList<PcoDataInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 48, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      PcoDataInfo localPcoDataInfo = new PcoDataInfo();
      localPcoDataInfo.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 48);
      localArrayList.add(localPcoDataInfo);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<PcoDataInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 48);
    while (j < i)
    {
      ((PcoDataInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 48);
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
    if (paramObject.getClass() != PcoDataInfo.class) {
      return false;
    }
    paramObject = (PcoDataInfo)paramObject;
    if (cid != cid) {
      return false;
    }
    if (!HidlSupport.deepEquals(bearerProto, bearerProto)) {
      return false;
    }
    if (pcoId != pcoId) {
      return false;
    }
    return HidlSupport.deepEquals(contents, contents);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(cid))), Integer.valueOf(HidlSupport.deepHashCode(bearerProto)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(pcoId))), Integer.valueOf(HidlSupport.deepHashCode(contents)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    cid = paramHwBlob.getInt32(paramLong + 0L);
    bearerProto = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(bearerProto.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
    pcoId = paramHwBlob.getInt32(paramLong + 24L);
    int i = paramHwBlob.getInt32(paramLong + 32L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 32L + 0L, true);
    contents.clear();
    for (int j = 0; j < i; j++)
    {
      byte b = paramHwParcel.getInt8(j * 1);
      contents.add(Byte.valueOf(b));
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(48L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".cid = ");
    localStringBuilder.append(cid);
    localStringBuilder.append(", .bearerProto = ");
    localStringBuilder.append(bearerProto);
    localStringBuilder.append(", .pcoId = ");
    localStringBuilder.append(pcoId);
    localStringBuilder.append(", .contents = ");
    localStringBuilder.append(contents);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(paramLong + 0L, cid);
    paramHwBlob.putString(paramLong + 8L, bearerProto);
    paramHwBlob.putInt32(24L + paramLong, pcoId);
    int i = contents.size();
    paramHwBlob.putInt32(paramLong + 32L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 32L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 1);
    while (j < i)
    {
      localHwBlob.putInt8(j * 1, ((Byte)contents.get(j)).byteValue());
      j++;
    }
    paramHwBlob.putBlob(32L + paramLong + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(48);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
