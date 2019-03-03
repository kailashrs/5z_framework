package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class ResetNvType
{
  public static final int ERASE = 1;
  public static final int FACTORY_RESET = 2;
  public static final int RELOAD = 0;
  
  public ResetNvType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("RELOAD");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ERASE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("FACTORY_RESET");
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
      return "RELOAD";
    }
    if (paramInt == 1) {
      return "ERASE";
    }
    if (paramInt == 2) {
      return "FACTORY_RESET";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
