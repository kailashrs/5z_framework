package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class Clir
{
  public static final int DEFAULT = 0;
  public static final int INVOCATION = 1;
  public static final int SUPPRESSION = 2;
  
  public Clir() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("DEFAULT");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("INVOCATION");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("SUPPRESSION");
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
      return "DEFAULT";
    }
    if (paramInt == 1) {
      return "INVOCATION";
    }
    if (paramInt == 2) {
      return "SUPPRESSION";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
