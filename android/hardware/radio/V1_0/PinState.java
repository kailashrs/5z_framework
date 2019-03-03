package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class PinState
{
  public static final int DISABLED = 3;
  public static final int ENABLED_BLOCKED = 4;
  public static final int ENABLED_NOT_VERIFIED = 1;
  public static final int ENABLED_PERM_BLOCKED = 5;
  public static final int ENABLED_VERIFIED = 2;
  public static final int UNKNOWN = 0;
  
  public PinState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ENABLED_NOT_VERIFIED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("ENABLED_VERIFIED");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("DISABLED");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("ENABLED_BLOCKED");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("ENABLED_PERM_BLOCKED");
      i = j | 0x5;
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
      return "UNKNOWN";
    }
    if (paramInt == 1) {
      return "ENABLED_NOT_VERIFIED";
    }
    if (paramInt == 2) {
      return "ENABLED_VERIFIED";
    }
    if (paramInt == 3) {
      return "DISABLED";
    }
    if (paramInt == 4) {
      return "ENABLED_BLOCKED";
    }
    if (paramInt == 5) {
      return "ENABLED_PERM_BLOCKED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
