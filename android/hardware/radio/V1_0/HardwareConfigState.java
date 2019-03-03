package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class HardwareConfigState
{
  public static final int DISABLED = 2;
  public static final int ENABLED = 0;
  public static final int STANDBY = 1;
  
  public HardwareConfigState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ENABLED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("STANDBY");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("DISABLED");
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
      return "ENABLED";
    }
    if (paramInt == 1) {
      return "STANDBY";
    }
    if (paramInt == 2) {
      return "DISABLED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
