package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CallPresentation
{
  public static final int ALLOWED = 0;
  public static final int PAYPHONE = 3;
  public static final int RESTRICTED = 1;
  public static final int UNKNOWN = 2;
  
  public CallPresentation() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ALLOWED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("RESTRICTED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("UNKNOWN");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("PAYPHONE");
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
    if (paramInt == 0) {
      return "ALLOWED";
    }
    if (paramInt == 1) {
      return "RESTRICTED";
    }
    if (paramInt == 2) {
      return "UNKNOWN";
    }
    if (paramInt == 3) {
      return "PAYPHONE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
