package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class Dial
{
  public String address = new String();
  public int clir;
  public final ArrayList<UusInfo> uusInfo = new ArrayList();
  
  public Dial() {}
  
  public static final ArrayList<Dial> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    HwBlob localHwBlob = paramHwParcel.readBuffer(16L);
    int i = localHwBlob.getInt32(8L);
    localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 40, localHwBlob.handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      Dial localDial = new Dial();
      localDial.readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 40);
      localArrayList.add(localDial);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<Dial> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 40);
    while (j < i)
    {
      ((Dial)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 40);
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
    if (paramObject.getClass() != Dial.class) {
      return false;
    }
    paramObject = (Dial)paramObject;
    if (!HidlSupport.deepEquals(address, address)) {
      return false;
    }
    if (clir != clir) {
      return false;
    }
    return HidlSupport.deepEquals(uusInfo, uusInfo);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(address)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(clir))), Integer.valueOf(HidlSupport.deepHashCode(uusInfo)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    address = paramHwBlob.getString(paramLong + 0L);
    paramHwParcel.readEmbeddedBuffer(address.getBytes().length + 1, paramHwBlob.handle(), paramLong + 0L + 0L, false);
    clir = paramHwBlob.getInt32(paramLong + 16L);
    int i = paramHwBlob.getInt32(paramLong + 24L + 8L);
    paramHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, paramHwBlob.handle(), paramLong + 24L + 0L, true);
    uusInfo.clear();
    for (int j = 0; j < i; j++)
    {
      UusInfo localUusInfo = new UusInfo();
      localUusInfo.readEmbeddedFromParcel(paramHwParcel, paramHwBlob, j * 24);
      uusInfo.add(localUusInfo);
    }
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(40L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".address = ");
    localStringBuilder.append(address);
    localStringBuilder.append(", .clir = ");
    localStringBuilder.append(Clir.toString(clir));
    localStringBuilder.append(", .uusInfo = ");
    localStringBuilder.append(uusInfo);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putString(paramLong + 0L, address);
    paramHwBlob.putInt32(16L + paramLong, clir);
    int i = uusInfo.size();
    paramHwBlob.putInt32(paramLong + 24L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 24L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 24);
    while (j < i)
    {
      ((UusInfo)uusInfo.get(j)).writeEmbeddedToBlob(localHwBlob, j * 24);
      j++;
    }
    paramHwBlob.putBlob(24L + paramLong + 0L, localHwBlob);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(40);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
