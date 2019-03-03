package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class DataProfileInfoType
{
  public static final int COMMON = 0;
  public static final int THREE_GPP = 1;
  public static final int THREE_GPP2 = 2;
  
  public DataProfileInfoType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("COMMON");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("THREE_GPP");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("THREE_GPP2");
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
      return "COMMON";
    }
    if (paramInt == 1) {
      return "THREE_GPP";
    }
    if (paramInt == 2) {
      return "THREE_GPP2";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
