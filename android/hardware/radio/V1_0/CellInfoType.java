package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CellInfoType
{
  public static final int CDMA = 2;
  public static final int GSM = 1;
  public static final int LTE = 3;
  public static final int NONE = 0;
  public static final int TD_SCDMA = 5;
  public static final int WCDMA = 4;
  
  public CellInfoType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NONE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("GSM");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("CDMA");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("LTE");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("WCDMA");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("TD_SCDMA");
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
      return "NONE";
    }
    if (paramInt == 1) {
      return "GSM";
    }
    if (paramInt == 2) {
      return "CDMA";
    }
    if (paramInt == 3) {
      return "LTE";
    }
    if (paramInt == 4) {
      return "WCDMA";
    }
    if (paramInt == 5) {
      return "TD_SCDMA";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
