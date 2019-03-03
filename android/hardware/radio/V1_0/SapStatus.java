package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SapStatus
{
  public static final int CARD_INSERTED = 4;
  public static final int CARD_NOT_ACCESSIBLE = 2;
  public static final int CARD_REMOVED = 3;
  public static final int CARD_RESET = 1;
  public static final int RECOVERED = 5;
  public static final int UNKNOWN_ERROR = 0;
  
  public SapStatus() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN_ERROR");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("CARD_RESET");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("CARD_NOT_ACCESSIBLE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("CARD_REMOVED");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CARD_INSERTED");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("RECOVERED");
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
      return "UNKNOWN_ERROR";
    }
    if (paramInt == 1) {
      return "CARD_RESET";
    }
    if (paramInt == 2) {
      return "CARD_NOT_ACCESSIBLE";
    }
    if (paramInt == 3) {
      return "CARD_REMOVED";
    }
    if (paramInt == 4) {
      return "CARD_INSERTED";
    }
    if (paramInt == 5) {
      return "RECOVERED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
