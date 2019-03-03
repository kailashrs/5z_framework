package android.hardware.contexthub.V1_0;

import java.util.ArrayList;

public final class HubMemoryType
{
  public static final int MAIN = 0;
  public static final int SECONDARY = 1;
  public static final int TCM = 2;
  
  public HubMemoryType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("MAIN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SECONDARY");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("TCM");
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
      return "MAIN";
    }
    if (paramInt == 1) {
      return "SECONDARY";
    }
    if (paramInt == 2) {
      return "TCM";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
