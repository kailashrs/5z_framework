package android.hardware.radio.V1_1;

import java.util.ArrayList;

public final class CardPowerState
{
  public static final int POWER_DOWN = 0;
  public static final int POWER_UP = 1;
  public static final int POWER_UP_PASS_THROUGH = 2;
  
  public CardPowerState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("POWER_DOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("POWER_UP");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("POWER_UP_PASS_THROUGH");
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
      return "POWER_DOWN";
    }
    if (paramInt == 1) {
      return "POWER_UP";
    }
    if (paramInt == 2) {
      return "POWER_UP_PASS_THROUGH";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
