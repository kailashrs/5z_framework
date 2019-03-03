package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaSmsNumberMode
{
  public static final int DATA_NETWORK = 1;
  public static final int NOT_DATA_NETWORK = 0;
  
  public CdmaSmsNumberMode() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NOT_DATA_NETWORK");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("DATA_NETWORK");
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
      return "NOT_DATA_NETWORK";
    }
    if (paramInt == 1) {
      return "DATA_NETWORK";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
