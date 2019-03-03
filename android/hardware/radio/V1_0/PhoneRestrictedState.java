package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class PhoneRestrictedState
{
  public static final int CS_ALL = 4;
  public static final int CS_EMERGENCY = 1;
  public static final int CS_NORMAL = 2;
  public static final int NONE = 0;
  public static final int PS_ALL = 16;
  
  public PhoneRestrictedState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NONE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("CS_EMERGENCY");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("CS_NORMAL");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CS_ALL");
      i = j | 0x4;
    }
    j = i;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("PS_ALL");
      j = i | 0x10;
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
      return "NONE";
    }
    if (paramInt == 1) {
      return "CS_EMERGENCY";
    }
    if (paramInt == 2) {
      return "CS_NORMAL";
    }
    if (paramInt == 4) {
      return "CS_ALL";
    }
    if (paramInt == 16) {
      return "PS_ALL";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
