package android.hardware.radio.V1_2;

import java.util.ArrayList;

public final class AccessNetwork
{
  public static final int CDMA2000 = 4;
  public static final int EUTRAN = 3;
  public static final int GERAN = 1;
  public static final int IWLAN = 5;
  public static final int UTRAN = 2;
  
  public AccessNetwork() {}
  
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
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CDMA2000");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("IWLAN");
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
    if (paramInt == 1) {
      return "GERAN";
    }
    if (paramInt == 2) {
      return "UTRAN";
    }
    if (paramInt == 3) {
      return "EUTRAN";
    }
    if (paramInt == 4) {
      return "CDMA2000";
    }
    if (paramInt == 5) {
      return "IWLAN";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
