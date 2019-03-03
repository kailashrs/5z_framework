package android.hardware.radio.V1_1;

import java.util.ArrayList;

public final class KeepaliveStatusCode
{
  public static final int ACTIVE = 0;
  public static final int INACTIVE = 1;
  public static final int PENDING = 2;
  
  public KeepaliveStatusCode() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ACTIVE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("INACTIVE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("PENDING");
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
      return "ACTIVE";
    }
    if (paramInt == 1) {
      return "INACTIVE";
    }
    if (paramInt == 2) {
      return "PENDING";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
