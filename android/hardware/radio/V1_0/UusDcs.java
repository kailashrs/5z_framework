package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class UusDcs
{
  public static final int IA5C = 4;
  public static final int OSIHLP = 1;
  public static final int RMCF = 3;
  public static final int USP = 0;
  public static final int X244 = 2;
  
  public UusDcs() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("USP");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("OSIHLP");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("X244");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("RMCF");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("IA5C");
      j = i | 0x4;
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
      return "USP";
    }
    if (paramInt == 1) {
      return "OSIHLP";
    }
    if (paramInt == 2) {
      return "X244";
    }
    if (paramInt == 3) {
      return "RMCF";
    }
    if (paramInt == 4) {
      return "IA5C";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
