package android.hardware.vibrator.V1_0;

import java.util.ArrayList;

public final class Effect
{
  public static final int CLICK = 0;
  public static final int DOUBLE_CLICK = 1;
  
  public Effect() {}
  
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
      return "CLICK";
    }
    if (paramInt == 1) {
      return "DOUBLE_CLICK";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
