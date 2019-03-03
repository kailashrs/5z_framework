package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaSubscriptionSource
{
  public static final int NV = 1;
  public static final int RUIM_SIM = 0;
  
  public CdmaSubscriptionSource() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("RUIM_SIM");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("NV");
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
      return "RUIM_SIM";
    }
    if (paramInt == 1) {
      return "NV";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
