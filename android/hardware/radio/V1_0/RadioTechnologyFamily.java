package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioTechnologyFamily
{
  public static final int THREE_GPP = 0;
  public static final int THREE_GPP2 = 1;
  
  public RadioTechnologyFamily() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("THREE_GPP");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("THREE_GPP2");
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
      return "THREE_GPP";
    }
    if (paramInt == 1) {
      return "THREE_GPP2";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
