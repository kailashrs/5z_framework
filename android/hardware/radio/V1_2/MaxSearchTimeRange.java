package android.hardware.radio.V1_2;

import java.util.ArrayList;

public final class MaxSearchTimeRange
{
  public static final int MAX = 3600;
  public static final int MIN = 60;
  
  public MaxSearchTimeRange() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x3C) == 60)
    {
      localArrayList.add("MIN");
      i = 0x0 | 0x3C;
    }
    int j = i;
    if ((paramInt & 0xE10) == 3600)
    {
      localArrayList.add("MAX");
      j = i | 0xE10;
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
    if (paramInt == 60) {
      return "MIN";
    }
    if (paramInt == 3600) {
      return "MAX";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
