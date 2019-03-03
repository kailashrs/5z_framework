package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaSmsErrorClass
{
  public static final int ERROR = 1;
  public static final int NO_ERROR = 0;
  
  public CdmaSmsErrorClass() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NO_ERROR");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ERROR");
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
      return "NO_ERROR";
    }
    if (paramInt == 1) {
      return "ERROR";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
