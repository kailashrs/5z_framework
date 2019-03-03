package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaSmsDigitMode
{
  public static final int EIGHT_BIT = 1;
  public static final int FOUR_BIT = 0;
  
  public CdmaSmsDigitMode() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("FOUR_BIT");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("EIGHT_BIT");
      i = 0x0 | 0x1;
    }
    if (paramInt != i)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(i & paramInt));
      localArrayList.add(localStringBuilder.toString());
    }
    return String.join(" | ", localArrayList);
  }
  
  public static final String toString(int paramInt)
  {
    if (paramInt == 0) {
      return "FOUR_BIT";
    }
    if (paramInt == 1) {
      return "EIGHT_BIT";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
