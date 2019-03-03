package android.hardware.radio.V1_2;

import java.util.ArrayList;

public final class IncrementalResultsPeriodicityRange
{
  public static final int MAX = 10;
  public static final int MIN = 1;
  
  public IncrementalResultsPeriodicityRange() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("MIN");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("MAX");
      j = i | 0xA;
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
    if (paramInt == 1) {
      return "MIN";
    }
    if (paramInt == 10) {
      return "MAX";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
