package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class HardwareConfigType
{
  public static final int MODEM = 0;
  public static final int SIM = 1;
  
  public HardwareConfigType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("MODEM");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SIM");
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
      return "MODEM";
    }
    if (paramInt == 1) {
      return "SIM";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
