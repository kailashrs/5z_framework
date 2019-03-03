package android.hardware.contexthub.V1_0;

import java.util.ArrayList;

public final class NanoAppFlags
{
  public static final int ENCRYPTED = 2;
  public static final int SIGNED = 1;
  
  public NanoAppFlags() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SIGNED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("ENCRYPTED");
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
    if (paramInt == 1) {
      return "SIGNED";
    }
    if (paramInt == 2) {
      return "ENCRYPTED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
