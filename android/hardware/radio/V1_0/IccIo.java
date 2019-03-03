package android.hardware.radio.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class IccIo
{
  public String aid = new String();
  public int command;
  public String data = new String();
  public int fileId;
  public int p1;
  public int p2;
  public int p3;
  public String path = new String();
  public String pin2 = new String();
  
  public IccIo() {}
  
  public static final ArrayList<IccIo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 88, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new IccIo();
      ((IccIo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 88);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<IccIo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 88);
    while (j < i)
    {
      ((IccIo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 88);
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
    if (paramObject.getClass() != IccIo.class) {
      return false;
    }
    paramObject = (IccIo)paramObject;
    if (command != command) {
      return false;
    }
    if (fileId != fileId) {
      return false;
    }
    if (!HidlSupport.deepEquals(path, path)) {
      return false;
    }
    if (p1 != p1) {
      return false;
    }
    if (p2 != p2) {
      return false;
    }
    if (p3 != p3) {
      return false;
    }
    if (!HidlSupport.deepEquals(data, data)) {
      return false;
    }
    if (!HidlSupport.deepEquals(pin2, pin2)) {
      return false;
    }
    return HidlSupport.deepEquals(aid, aid);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(command))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(fileId))), Integer.valueOf(HidlSupport.deepHashCode(path)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(p1))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(p2))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(p3))), Integer.valueOf(HidlSupport.deepHashCode(data)), Integer.valueOf(HidlSupport.deepHashCode(pin2)), Integer.valueOf(HidlSupport.deepHashCode(aid)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    command = paramHwBlob.getInt32(paramLong + 0L);
    fileId = paramHwBlob.getInt32(paramLong + 4L);
    path = paramHwBlob.getString(paramLong + 8L);
    paramHwParcel.readEmbeddedBuffer(path.getBytes().length + 1, paramHwBlob.handle(), paramLong + 8L + 0L, false);
    p1 = paramHwBlob.getInt32(paramLong + 24L);
    p2 = paramHwBlob.getInt32(paramLong + 28L);
    p3 = paramHwBlob.getInt32(paramLong + 32L);
    data = paramHwBlob.getString(paramLong + 40L);
    paramHwParcel.readEmbeddedBuffer(data.getBytes().length + 1, paramHwBlob.handle(), paramLong + 40L + 0L, false);
    pin2 = paramHwBlob.getString(paramLong + 56L);
    paramHwParcel.readEmbeddedBuffer(pin2.getBytes().length + 1, paramHwBlob.handle(), paramLong + 56L + 0L, false);
    aid = paramHwBlob.getString(paramLong + 72L);
    paramHwParcel.readEmbeddedBuffer(aid.getBytes().length + 1, paramHwBlob.handle(), paramLong + 72L + 0L, false);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(88L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".command = ");
    localStringBuilder.append(command);
    localStringBuilder.append(", .fileId = ");
    localStringBuilder.append(fileId);
    localStringBuilder.append(", .path = ");
    localStringBuilder.append(path);
    localStringBuilder.append(", .p1 = ");
    localStringBuilder.append(p1);
    localStringBuilder.append(", .p2 = ");
    localStringBuilder.append(p2);
    localStringBuilder.append(", .p3 = ");
    localStringBuilder.append(p3);
    localStringBuilder.append(", .data = ");
    localStringBuilder.append(data);
    localStringBuilder.append(", .pin2 = ");
    localStringBuilder.append(pin2);
    localStringBuilder.append(", .aid = ");
    localStringBuilder.append(aid);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, command);
    paramHwBlob.putInt32(4L + paramLong, fileId);
    paramHwBlob.putString(8L + paramLong, path);
    paramHwBlob.putInt32(24L + paramLong, p1);
    paramHwBlob.putInt32(28L + paramLong, p2);
    paramHwBlob.putInt32(32L + paramLong, p3);
    paramHwBlob.putString(40L + paramLong, data);
    paramHwBlob.putString(56L + paramLong, pin2);
    paramHwBlob.putString(72L + paramLong, aid);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(88);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
}
