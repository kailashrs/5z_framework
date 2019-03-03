package android.hardware.vibrator.V1_1;

import java.util.ArrayList;

public final class Effect_1_1
{
  public static final int CLICK = 0;
  public static final int DOUBLE_CLICK = 1;
  public static final int TICK = 2;
  
  public Effect_1_1() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("CLICK");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("DOUBLE_CLICK");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("TICK");
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
      return "CLICK";
    }
    if (paramInt == 1) {
      return "DOUBLE_CLICK";
    }
    if (paramInt == 2) {
      return "TICK";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
