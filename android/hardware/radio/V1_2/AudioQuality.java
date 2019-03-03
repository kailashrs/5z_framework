package android.hardware.radio.V1_2;

import java.util.ArrayList;

public final class AudioQuality
{
  public static final int AMR = 1;
  public static final int AMR_WB = 2;
  public static final int EVRC = 6;
  public static final int EVRC_B = 7;
  public static final int EVRC_NW = 9;
  public static final int EVRC_WB = 8;
  public static final int GSM_EFR = 3;
  public static final int GSM_FR = 4;
  public static final int GSM_HR = 5;
  public static final int UNSPECIFIED = 0;
  
  public AudioQuality() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNSPECIFIED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("AMR");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("AMR_WB");
      j = i | 0x2;
    }
    int k = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("GSM_EFR");
      k = j | 0x3;
    }
    i = k;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("GSM_FR");
      i = k | 0x4;
    }
    j = i;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("GSM_HR");
      j = i | 0x5;
    }
    i = j;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("EVRC");
      i = j | 0x6;
    }
    k = i;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("EVRC_B");
      k = i | 0x7;
    }
    j = k;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("EVRC_WB");
      j = k | 0x8;
    }
    i = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("EVRC_NW");
      i = j | 0x9;
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
      return "UNSPECIFIED";
    }
    if (paramInt == 1) {
      return "AMR";
    }
    if (paramInt == 2) {
      return "AMR_WB";
    }
    if (paramInt == 3) {
      return "GSM_EFR";
    }
    if (paramInt == 4) {
      return "GSM_FR";
    }
    if (paramInt == 5) {
      return "GSM_HR";
    }
    if (paramInt == 6) {
      return "EVRC";
    }
    if (paramInt == 7) {
      return "EVRC_B";
    }
    if (paramInt == 8) {
      return "EVRC_WB";
    }
    if (paramInt == 9) {
      return "EVRC_NW";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
