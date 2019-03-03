package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CardState
{
  public static final int ABSENT = 0;
  public static final int ERROR = 2;
  public static final int PRESENT = 1;
  public static final int RESTRICTED = 3;
  
  public CardState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ABSENT");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("PRESENT");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("ERROR");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("RESTRICTED");
      i = j | 0x3;
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
      return "ABSENT";
    }
    if (paramInt == 1) {
      return "PRESENT";
    }
    if (paramInt == 2) {
      return "ERROR";
    }
    if (paramInt == 3) {
      return "RESTRICTED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
