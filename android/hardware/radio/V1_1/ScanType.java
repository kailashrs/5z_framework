package android.hardware.radio.V1_1;

import java.util.ArrayList;

public final class ScanType
{
  public static final int ONE_SHOT = 0;
  public static final int PERIODIC = 1;
  
  public ScanType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ONE_SHOT");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("PERIODIC");
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
      return "ONE_SHOT";
    }
    if (paramInt == 1) {
      return "PERIODIC";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
