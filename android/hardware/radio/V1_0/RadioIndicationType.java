package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioIndicationType
{
  public static final int UNSOLICITED = 0;
  public static final int UNSOLICITED_ACK_EXP = 1;
  
  public RadioIndicationType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNSOLICITED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("UNSOLICITED_ACK_EXP");
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
      return "UNSOLICITED";
    }
    if (paramInt == 1) {
      return "UNSOLICITED_ACK_EXP";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
