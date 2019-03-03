package android.hardware.radio.V1_2;

import java.util.ArrayList;

public final class ScanIntervalRange
{
  public static final int MAX = 300;
  public static final int MIN = 5;
  
  public ScanIntervalRange() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("MIN");
      i = 0x0 | 0x5;
    }
    int j = i;
    if ((paramInt & 0x12C) == 300)
    {
      localArrayList.add("MAX");
      j = i | 0x12C;
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
    if (paramInt == 5) {
      return "MIN";
    }
    if (paramInt == 300) {
      return "MAX";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
