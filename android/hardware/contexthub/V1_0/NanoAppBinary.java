package android.hardware.contexthub.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class NanoAppBinary
{
  public long appId;
  public int appVersion;
  public final ArrayList<Byte> customBinary = new ArrayList();
  public int flags;
  public byte targetChreApiMajorVersion;
  public byte targetChreApiMinorVersion;
  
  public NanoAppBinary() {}
  
  public static final ArrayList<NanoAppBinary> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 40, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new NanoAppBinary();
      ((NanoAppBinary)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 40);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<NanoAppBinary> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 40);
    while (j < i)
    {
      ((NanoAppBinary)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 40);
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
    if (paramObject.getClass() != NanoAppBinary.class) {
      return false;
    }
    paramObject = (NanoAppBinary)paramObject;
    if (appId != appId) {
      return false;
    }
    if (appVersion != appVersion) {
      return false;
    }
    if (!HidlSupport.deepEquals(Integer.valueOf(flags), Integer.valueOf(flags))) {
      return false;
    }
    if (targetChreApiMajorVersion != targetChreApiMajorVersion) {
      return false;
    }
    if (targetChreApiMinorVersion != targetChreApiMinorVersion) {
      return false;
    }
    return HidlSupport.deepEquals(customBinary, customBinary);
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(appId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(appVersion))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(flags))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(targetChreApiMajorVersion))), Integer.valueOf(HidlSupport.deepHashCode(Byte.valueOf(targetChreApiMinorVersion))), Integer.valueOf(HidlSupport.deepHashCode(customBinary)) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    appId = paramHwBlob.getInt64(paramLong + 0L);
    appVersion = paramHwBlob.getInt32(paramLong + 8L);
    flags = paramHwBlob.getInt32(paramLong + 12L);
    targetChreApiMajorVersion = paramHwBlob.getInt8(paramLong + 16L);
    targetChreApiMinorVersion = paramHwBlob.getInt8(paramLong + 17L);
    int i = paramHwBlob.getInt32(paramLong + 24L + 8L);
    paramHwParcel = paramHwParcel.readEmbeddedBuffer(i * 1, paramHwBlob.handle(), paramLong + 24L + 0L, true);
    customBinary.clear();
    for (int j = 0; j < i; j++)
    {
      byte b = paramHwParcel.getInt8(j * 1);
      customBinary.add(Byte.valueOf(b));
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
    localStringBuilder.append(".appId = ");
    localStringBuilder.append(appId);
    localStringBuilder.append(", .appVersion = ");
    localStringBuilder.append(appVersion);
    localStringBuilder.append(", .flags = ");
    localStringBuilder.append(NanoAppFlags.dumpBitfield(flags));
    localStringBuilder.append(", .targetChreApiMajorVersion = ");
    localStringBuilder.append(targetChreApiMajorVersion);
    localStringBuilder.append(", .targetChreApiMinorVersion = ");
    localStringBuilder.append(targetChreApiMinorVersion);
    localStringBuilder.append(", .customBinary = ");
    localStringBuilder.append(customBinary);
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt64(paramLong + 0L, appId);
    paramHwBlob.putInt32(paramLong + 8L, appVersion);
    paramHwBlob.putInt32(paramLong + 12L, flags);
    paramHwBlob.putInt8(16L + paramLong, targetChreApiMajorVersion);
    paramHwBlob.putInt8(17L + paramLong, targetChreApiMinorVersion);
    int i = customBinary.size();
    paramHwBlob.putInt32(paramLong + 24L + 8L, i);
    int j = 0;
    paramHwBlob.putBool(paramLong + 24L + 12L, false);
    HwBlob localHwBlob = new HwBlob(i * 1);
    while (j < i)
    {
      localHwBlob.putInt8(j * 1, ((Byte)customBinary.get(j)).byteValue());
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
