package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioCapabilityPhase
{
  public static final int APPLY = 2;
  public static final int CONFIGURED = 0;
  public static final int FINISH = 4;
  public static final int START = 1;
  public static final int UNSOL_RSP = 3;
  
  public RadioCapabilityPhase() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("CONFIGURED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("START");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("APPLY");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("UNSOL_RSP");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("FINISH");
      j = i | 0x4;
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
      return "CONFIGURED";
    }
    if (paramInt == 1) {
      return "START";
    }
    if (paramInt == 2) {
      return "APPLY";
    }
    if (paramInt == 3) {
      return "UNSOL_RSP";
    }
    if (paramInt == 4) {
      return "FINISH";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
