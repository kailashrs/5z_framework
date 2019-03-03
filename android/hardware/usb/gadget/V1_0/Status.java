package android.hardware.usb.gadget.V1_0;

import java.util.ArrayList;

public final class Status
{
  public static final int CONFIGURATION_NOT_SUPPORTED = 4;
  public static final int ERROR = 1;
  public static final int FUNCTIONS_APPLIED = 2;
  public static final int FUNCTIONS_NOT_APPLIED = 3;
  public static final int SUCCESS = 0;
  
  public Status() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("SUCCESS");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ERROR");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("FUNCTIONS_APPLIED");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("FUNCTIONS_NOT_APPLIED");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CONFIGURATION_NOT_SUPPORTED");
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
      return "SUCCESS";
    }
    if (paramInt == 1) {
      return "ERROR";
    }
    if (paramInt == 2) {
      return "FUNCTIONS_APPLIED";
    }
    if (paramInt == 3) {
      return "FUNCTIONS_NOT_APPLIED";
    }
    if (paramInt == 4) {
      return "CONFIGURATION_NOT_SUPPORTED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
