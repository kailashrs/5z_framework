package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class TtyMode
{
  public static final int FULL = 1;
  public static final int HCO = 2;
  public static final int OFF = 0;
  public static final int VCO = 3;
  
  public TtyMode() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("OFF");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("FULL");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("HCO");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("VCO");
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
      return "OFF";
    }
    if (paramInt == 1) {
      return "FULL";
    }
    if (paramInt == 2) {
      return "HCO";
    }
    if (paramInt == 3) {
      return "VCO";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
