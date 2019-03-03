package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioState
{
  public static final int OFF = 0;
  public static final int ON = 10;
  public static final int UNAVAILABLE = 1;
  
  public RadioState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("OFF");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("UNAVAILABLE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("ON");
      j = i | 0xA;
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
      return "OFF";
    }
    if (paramInt == 1) {
      return "UNAVAILABLE";
    }
    if (paramInt == 10) {
      return "ON";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
