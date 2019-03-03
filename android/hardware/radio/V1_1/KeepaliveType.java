package android.hardware.radio.V1_1;

import java.util.ArrayList;

public final class KeepaliveType
{
  public static final int NATT_IPV4 = 0;
  public static final int NATT_IPV6 = 1;
  
  public KeepaliveType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NATT_IPV4");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("NATT_IPV6");
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
      return "NATT_IPV4";
    }
    if (paramInt == 1) {
      return "NATT_IPV6";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
