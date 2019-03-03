package android.hidl.base.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

public final class DebugInfo
{
  public int arch;
  public int pid;
  public long ptr;
  
  public DebugInfo() {}
  
  public static final ArrayList<DebugInfo> readVectorFromParcel(HwParcel paramHwParcel)
  {
    ArrayList localArrayList = new ArrayList();
    Object localObject = paramHwParcel.readBuffer(16L);
    int i = ((HwBlob)localObject).getInt32(8L);
    HwBlob localHwBlob = paramHwParcel.readEmbeddedBuffer(i * 24, ((HwBlob)localObject).handle(), 0L, true);
    localArrayList.clear();
    for (int j = 0; j < i; j++)
    {
      localObject = new DebugInfo();
      ((DebugInfo)localObject).readEmbeddedFromParcel(paramHwParcel, localHwBlob, j * 24);
      localArrayList.add(localObject);
    }
    return localArrayList;
  }
  
  public static final void writeVectorToParcel(HwParcel paramHwParcel, ArrayList<DebugInfo> paramArrayList)
  {
    HwBlob localHwBlob1 = new HwBlob(16);
    int i = paramArrayList.size();
    localHwBlob1.putInt32(8L, i);
    int j = 0;
    localHwBlob1.putBool(12L, false);
    HwBlob localHwBlob2 = new HwBlob(i * 24);
    while (j < i)
    {
      ((DebugInfo)paramArrayList.get(j)).writeEmbeddedToBlob(localHwBlob2, j * 24);
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
    if (paramObject.getClass() != DebugInfo.class) {
      return false;
    }
    paramObject = (DebugInfo)paramObject;
    if (pid != pid) {
      return false;
    }
    if (ptr != ptr) {
      return false;
    }
    return arch == arch;
  }
  
  public final int hashCode()
  {
    return Objects.hash(new Object[] { Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(pid))), Integer.valueOf(HidlSupport.deepHashCode(Long.valueOf(ptr))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(arch))) });
  }
  
  public final void readEmbeddedFromParcel(HwParcel paramHwParcel, HwBlob paramHwBlob, long paramLong)
  {
    pid = paramHwBlob.getInt32(0L + paramLong);
    ptr = paramHwBlob.getInt64(8L + paramLong);
    arch = paramHwBlob.getInt32(16L + paramLong);
  }
  
  public final void readFromParcel(HwParcel paramHwParcel)
  {
    readEmbeddedFromParcel(paramHwParcel, paramHwParcel.readBuffer(24L), 0L);
  }
  
  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("{");
    localStringBuilder.append(".pid = ");
    localStringBuilder.append(pid);
    localStringBuilder.append(", .ptr = ");
    localStringBuilder.append(ptr);
    localStringBuilder.append(", .arch = ");
    localStringBuilder.append(Architecture.toString(arch));
    localStringBuilder.append("}");
    return localStringBuilder.toString();
  }
  
  public final void writeEmbeddedToBlob(HwBlob paramHwBlob, long paramLong)
  {
    paramHwBlob.putInt32(0L + paramLong, pid);
    paramHwBlob.putInt64(8L + paramLong, ptr);
    paramHwBlob.putInt32(16L + paramLong, arch);
  }
  
  public final void writeToParcel(HwParcel paramHwParcel)
  {
    HwBlob localHwBlob = new HwBlob(24);
    writeEmbeddedToBlob(localHwBlob, 0L);
    paramHwParcel.writeBuffer(localHwBlob);
  }
  
  public static final class Architecture
  {
    public static final int IS_32BIT = 2;
    public static final int IS_64BIT = 1;
    public static final int UNKNOWN = 0;
    
    public Architecture() {}
    
    public static final String dumpBitfield(int paramInt)
    {
      ArrayList localArrayList = new ArrayList();
      int i = 0;
      localArrayList.add("UNKNOWN");
      if ((paramInt & 0x1) == 1)
      {
        localArrayList.add("IS_64BIT");
        i = 0x0 | 0x1;
      }
      int j = i;
      if ((paramInt & 0x2) == 2)
      {
        localArrayList.add("IS_32BIT");
        j = i | 0x2;
      }
      if (paramInt != j)
      {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("0x");
        localStringBuilder.append(Integer.toHexString(j & paramInt));
        localArrayList.add(localStringBuilder.toString());
      }
      return String.join(" | ", localArrayList);
    }
    
    public static final String toString(int paramInt)
    {
      if (paramInt == 0) {
        return "UNKNOWN";
      }
      if (paramInt == 1) {
        return "IS_64BIT";
      }
      if (paramInt == 2) {
        return "IS_32BIT";
      }
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(paramInt));
      return localStringBuilder.toString();
    }
  }
}
