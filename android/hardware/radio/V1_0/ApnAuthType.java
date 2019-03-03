package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class ApnAuthType
{
  public static final int NO_PAP_CHAP = 2;
  public static final int NO_PAP_NO_CHAP = 0;
  public static final int PAP_CHAP = 3;
  public static final int PAP_NO_CHAP = 1;
  
  public ApnAuthType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NO_PAP_NO_CHAP");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("PAP_NO_CHAP");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("NO_PAP_CHAP");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("PAP_CHAP");
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
      return "NO_PAP_NO_CHAP";
    }
    if (paramInt == 1) {
      return "PAP_NO_CHAP";
    }
    if (paramInt == 2) {
      return "NO_PAP_CHAP";
    }
    if (paramInt == 3) {
      return "PAP_CHAP";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
