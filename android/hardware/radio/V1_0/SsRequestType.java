package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SsRequestType
{
  public static final int ACTIVATION = 0;
  public static final int DEACTIVATION = 1;
  public static final int ERASURE = 4;
  public static final int INTERROGATION = 2;
  public static final int REGISTRATION = 3;
  
  public SsRequestType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ACTIVATION");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("DEACTIVATION");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("INTERROGATION");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("REGISTRATION");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("ERASURE");
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
      return "ACTIVATION";
    }
    if (paramInt == 1) {
      return "DEACTIVATION";
    }
    if (paramInt == 2) {
      return "INTERROGATION";
    }
    if (paramInt == 3) {
      return "REGISTRATION";
    }
    if (paramInt == 4) {
      return "ERASURE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
