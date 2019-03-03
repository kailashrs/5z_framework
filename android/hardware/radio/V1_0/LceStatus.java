package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class LceStatus
{
  public static final int ACTIVE = 2;
  public static final int NOT_SUPPORTED = 0;
  public static final int STOPPED = 1;
  
  public LceStatus() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NOT_SUPPORTED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("STOPPED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("ACTIVE");
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
      return "NOT_SUPPORTED";
    }
    if (paramInt == 1) {
      return "STOPPED";
    }
    if (paramInt == 2) {
      return "ACTIVE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
