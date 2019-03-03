package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class DataProfileId
{
  public static final int CBS = 4;
  public static final int DEFAULT = 0;
  public static final int FOTA = 3;
  public static final int IMS = 2;
  public static final int INVALID = -1;
  public static final int OEM_BASE = 1000;
  public static final int TETHERED = 1;
  
  public DataProfileId() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("DEFAULT");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("TETHERED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("IMS");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("FOTA");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CBS");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x3E8) == 1000)
    {
      localArrayList.add("OEM_BASE");
      i = j | 0x3E8;
    }
    j = i;
    if ((paramInt & 0xFFFFFFFF) == -1)
    {
      localArrayList.add("INVALID");
      j = i | 0xFFFFFFFF;
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
      return "DEFAULT";
    }
    if (paramInt == 1) {
      return "TETHERED";
    }
    if (paramInt == 2) {
      return "IMS";
    }
    if (paramInt == 3) {
      return "FOTA";
    }
    if (paramInt == 4) {
      return "CBS";
    }
    if (paramInt == 1000) {
      return "OEM_BASE";
    }
    if (paramInt == -1) {
      return "INVALID";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
