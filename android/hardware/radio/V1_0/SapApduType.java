package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SapApduType
{
  public static final int APDU = 0;
  public static final int APDU7816 = 1;
  
  public SapApduType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("APDU");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("APDU7816");
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
      return "APDU";
    }
    if (paramInt == 1) {
      return "APDU7816";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
