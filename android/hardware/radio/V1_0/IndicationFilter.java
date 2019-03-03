package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class IndicationFilter
{
  public static final int ALL = 7;
  public static final int DATA_CALL_DORMANCY_CHANGED = 4;
  public static final int FULL_NETWORK_STATE = 2;
  public static final int NONE = 0;
  public static final int SIGNAL_STRENGTH = 1;
  
  public IndicationFilter() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NONE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SIGNAL_STRENGTH");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("FULL_NETWORK_STATE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("DATA_CALL_DORMANCY_CHANGED");
      i = j | 0x4;
    }
    j = i;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("ALL");
      j = i | 0x7;
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
      return "NONE";
    }
    if (paramInt == 1) {
      return "SIGNAL_STRENGTH";
    }
    if (paramInt == 2) {
      return "FULL_NETWORK_STATE";
    }
    if (paramInt == 4) {
      return "DATA_CALL_DORMANCY_CHANGED";
    }
    if (paramInt == 7) {
      return "ALL";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
