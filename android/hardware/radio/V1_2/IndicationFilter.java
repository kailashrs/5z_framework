package android.hardware.radio.V1_2;

import java.util.ArrayList;

public final class IndicationFilter
{
  public static final int ALL = -1;
  public static final int DATA_CALL_DORMANCY_CHANGED = 4;
  public static final int FULL_NETWORK_STATE = 2;
  public static final int LINK_CAPACITY_ESTIMATE = 8;
  public static final int NONE = 0;
  public static final int PHYSICAL_CHANNEL_CONFIG = 16;
  public static final int SIGNAL_STRENGTH = 1;
  
  public IndicationFilter() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NONE");
    if ((paramInt & 0xFFFFFFFF) == -1)
    {
      localArrayList.add("ALL");
      i = 0x0 | 0xFFFFFFFF;
    }
    int j = i;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SIGNAL_STRENGTH");
      j = i | 0x1;
    }
    i = j;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("FULL_NETWORK_STATE");
      i = j | 0x2;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("DATA_CALL_DORMANCY_CHANGED");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("LINK_CAPACITY_ESTIMATE");
      i = j | 0x8;
    }
    j = i;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("PHYSICAL_CHANNEL_CONFIG");
      j = i | 0x10;
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
    if (paramInt == -1) {
      return "ALL";
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
    if (paramInt == 8) {
      return "LINK_CAPACITY_ESTIMATE";
    }
    if (paramInt == 16) {
      return "PHYSICAL_CHANNEL_CONFIG";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
