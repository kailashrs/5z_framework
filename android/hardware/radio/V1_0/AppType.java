package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class AppType
{
  public static final int CSIM = 4;
  public static final int ISIM = 5;
  public static final int RUIM = 3;
  public static final int SIM = 1;
  public static final int UNKNOWN = 0;
  public static final int USIM = 2;
  
  public AppType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SIM");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("USIM");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("RUIM");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CSIM");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("ISIM");
      i = j | 0x5;
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
      return "UNKNOWN";
    }
    if (paramInt == 1) {
      return "SIM";
    }
    if (paramInt == 2) {
      return "USIM";
    }
    if (paramInt == 3) {
      return "RUIM";
    }
    if (paramInt == 4) {
      return "CSIM";
    }
    if (paramInt == 5) {
      return "ISIM";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
