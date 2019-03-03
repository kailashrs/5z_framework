package android.hardware.radio.V1_1;

import java.util.ArrayList;

public final class GeranBands
{
  public static final int BAND_450 = 3;
  public static final int BAND_480 = 4;
  public static final int BAND_710 = 5;
  public static final int BAND_750 = 6;
  public static final int BAND_850 = 8;
  public static final int BAND_DCS1800 = 12;
  public static final int BAND_E900 = 10;
  public static final int BAND_ER900 = 14;
  public static final int BAND_P900 = 9;
  public static final int BAND_PCS1900 = 13;
  public static final int BAND_R900 = 11;
  public static final int BAND_T380 = 1;
  public static final int BAND_T410 = 2;
  public static final int BAND_T810 = 7;
  
  public GeranBands() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("BAND_T380");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("BAND_T410");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("BAND_450");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("BAND_480");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("BAND_710");
      i = j | 0x5;
    }
    int k = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("BAND_750");
      k = i | 0x6;
    }
    j = k;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("BAND_T810");
      j = k | 0x7;
    }
    i = j;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("BAND_850");
      i = j | 0x8;
    }
    j = i;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("BAND_P900");
      j = i | 0x9;
    }
    i = j;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("BAND_E900");
      i = j | 0xA;
    }
    k = i;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("BAND_R900");
      k = i | 0xB;
    }
    j = k;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("BAND_DCS1800");
      j = k | 0xC;
    }
    i = j;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("BAND_PCS1900");
      i = j | 0xD;
    }
    j = i;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("BAND_ER900");
      j = i | 0xE;
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
      return "BAND_T380";
    }
    if (paramInt == 2) {
      return "BAND_T410";
    }
    if (paramInt == 3) {
      return "BAND_450";
    }
    if (paramInt == 4) {
      return "BAND_480";
    }
    if (paramInt == 5) {
      return "BAND_710";
    }
    if (paramInt == 6) {
      return "BAND_750";
    }
    if (paramInt == 7) {
      return "BAND_T810";
    }
    if (paramInt == 8) {
      return "BAND_850";
    }
    if (paramInt == 9) {
      return "BAND_P900";
    }
    if (paramInt == 10) {
      return "BAND_E900";
    }
    if (paramInt == 11) {
      return "BAND_R900";
    }
    if (paramInt == 12) {
      return "BAND_DCS1800";
    }
    if (paramInt == 13) {
      return "BAND_PCS1900";
    }
    if (paramInt == 14) {
      return "BAND_ER900";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
