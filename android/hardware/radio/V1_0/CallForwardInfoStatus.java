package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CallForwardInfoStatus
{
  public static final int DISABLE = 0;
  public static final int ENABLE = 1;
  public static final int ERASURE = 4;
  public static final int INTERROGATE = 2;
  public static final int REGISTRATION = 3;
  
  public CallForwardInfoStatus() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("DISABLE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ENABLE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("INTERROGATE");
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
      return "DISABLE";
    }
    if (paramInt == 1) {
      return "ENABLE";
    }
    if (paramInt == 2) {
      return "INTERROGATE";
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
