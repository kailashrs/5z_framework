package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SapTransferProtocol
{
  public static final int T0 = 0;
  public static final int T1 = 1;
  
  public SapTransferProtocol() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("T0");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("T1");
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
      return "T0";
    }
    if (paramInt == 1) {
      return "T1";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
