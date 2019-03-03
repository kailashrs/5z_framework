package android.hardware.radio.V1_1;

import java.util.ArrayList;

public final class RadioAccessNetworks
{
  public static final int EUTRAN = 3;
  public static final int GERAN = 1;
  public static final int UTRAN = 2;
  
  public RadioAccessNetworks() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("GERAN");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("UTRAN");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("EUTRAN");
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
    if (paramInt == 1) {
      return "GERAN";
    }
    if (paramInt == 2) {
      return "UTRAN";
    }
    if (paramInt == 3) {
      return "EUTRAN";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
