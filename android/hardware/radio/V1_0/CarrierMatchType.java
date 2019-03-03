package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CarrierMatchType
{
  public static final int ALL = 0;
  public static final int GID1 = 3;
  public static final int GID2 = 4;
  public static final int IMSI_PREFIX = 2;
  public static final int SPN = 1;
  
  public CarrierMatchType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ALL");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SPN");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("IMSI_PREFIX");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("GID1");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("GID2");
      j = i | 0x4;
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
      return "ALL";
    }
    if (paramInt == 1) {
      return "SPN";
    }
    if (paramInt == 2) {
      return "IMSI_PREFIX";
    }
    if (paramInt == 3) {
      return "GID1";
    }
    if (paramInt == 4) {
      return "GID2";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
