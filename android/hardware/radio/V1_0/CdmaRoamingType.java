package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaRoamingType
{
  public static final int AFFILIATED_ROAM = 1;
  public static final int ANY_ROAM = 2;
  public static final int HOME_NETWORK = 0;
  
  public CdmaRoamingType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("HOME_NETWORK");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("AFFILIATED_ROAM");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("ANY_ROAM");
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
      return "HOME_NETWORK";
    }
    if (paramInt == 1) {
      return "AFFILIATED_ROAM";
    }
    if (paramInt == 2) {
      return "ANY_ROAM";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
