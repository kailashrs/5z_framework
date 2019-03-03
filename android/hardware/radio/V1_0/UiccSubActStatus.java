package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class UiccSubActStatus
{
  public static final int ACTIVATE = 1;
  public static final int DEACTIVATE = 0;
  
  public UiccSubActStatus() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("DEACTIVATE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ACTIVATE");
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
      return "DEACTIVATE";
    }
    if (paramInt == 1) {
      return "ACTIVATE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
