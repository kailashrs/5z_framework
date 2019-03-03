package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaCallWaitingNumberPlan
{
  public static final int DATA = 3;
  public static final int ISDN = 1;
  public static final int NATIONAL = 8;
  public static final int PRIVATE = 9;
  public static final int TELEX = 4;
  public static final int UNKNOWN = 0;
  
  public CdmaCallWaitingNumberPlan() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ISDN");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("DATA");
      j = i | 0x3;
    }
    i = j;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("TELEX");
      i = j | 0x4;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("NATIONAL");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("PRIVATE");
      i = j | 0x9;
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
      return "ISDN";
    }
    if (paramInt == 3) {
      return "DATA";
    }
    if (paramInt == 4) {
      return "TELEX";
    }
    if (paramInt == 8) {
      return "NATIONAL";
    }
    if (paramInt == 9) {
      return "PRIVATE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
