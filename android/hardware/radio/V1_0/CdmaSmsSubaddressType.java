package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaSmsSubaddressType
{
  public static final int NSAP = 0;
  public static final int USER_SPECIFIED = 1;
  
  public CdmaSmsSubaddressType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NSAP");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("USER_SPECIFIED");
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
      return "NSAP";
    }
    if (paramInt == 1) {
      return "USER_SPECIFIED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
