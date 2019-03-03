package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SimRefreshType
{
  public static final int SIM_FILE_UPDATE = 0;
  public static final int SIM_INIT = 1;
  public static final int SIM_RESET = 2;
  
  public SimRefreshType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("SIM_FILE_UPDATE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SIM_INIT");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("SIM_RESET");
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
      return "SIM_FILE_UPDATE";
    }
    if (paramInt == 1) {
      return "SIM_INIT";
    }
    if (paramInt == 2) {
      return "SIM_RESET";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
