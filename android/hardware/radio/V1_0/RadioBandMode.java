package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioBandMode
{
  public static final int BAND_MODE_10_800M_2 = 15;
  public static final int BAND_MODE_5_450M = 10;
  public static final int BAND_MODE_7_700M_2 = 12;
  public static final int BAND_MODE_8_1800M = 13;
  public static final int BAND_MODE_9_900M = 14;
  public static final int BAND_MODE_AUS = 4;
  public static final int BAND_MODE_AUS_2 = 5;
  public static final int BAND_MODE_AWS = 17;
  public static final int BAND_MODE_CELL_800 = 6;
  public static final int BAND_MODE_EURO = 1;
  public static final int BAND_MODE_EURO_PAMR_400M = 16;
  public static final int BAND_MODE_IMT2000 = 11;
  public static final int BAND_MODE_JPN = 3;
  public static final int BAND_MODE_JTACS = 8;
  public static final int BAND_MODE_KOREA_PCS = 9;
  public static final int BAND_MODE_PCS = 7;
  public static final int BAND_MODE_UNSPECIFIED = 0;
  public static final int BAND_MODE_USA = 2;
  public static final int BAND_MODE_USA_2500M = 18;
  
  public RadioBandMode() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("BAND_MODE_UNSPECIFIED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("BAND_MODE_EURO");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("BAND_MODE_USA");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("BAND_MODE_JPN");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("BAND_MODE_AUS");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("BAND_MODE_AUS_2");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("BAND_MODE_CELL_800");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("BAND_MODE_PCS");
      i = j | 0x7;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("BAND_MODE_JTACS");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("BAND_MODE_KOREA_PCS");
      i = j | 0x9;
    }
    j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("BAND_MODE_5_450M");
      j = i | 0xA;
    }
    i = j;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("BAND_MODE_IMT2000");
      i = j | 0xB;
    }
    int k = i;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("BAND_MODE_7_700M_2");
      k = i | 0xC;
    }
    j = k;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("BAND_MODE_8_1800M");
      j = k | 0xD;
    }
    i = j;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("BAND_MODE_9_900M");
      i = j | 0xE;
    }
    j = i;
    if ((paramInt & 0xF) == 15)
    {
      localArrayList.add("BAND_MODE_10_800M_2");
      j = i | 0xF;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("BAND_MODE_EURO_PAMR_400M");
      i = j | 0x10;
    }
    j = i;
    if ((paramInt & 0x11) == 17)
    {
      localArrayList.add("BAND_MODE_AWS");
      j = i | 0x11;
    }
    i = j;
    if ((paramInt & 0x12) == 18)
    {
      localArrayList.add("BAND_MODE_USA_2500M");
      i = j | 0x12;
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
      return "BAND_MODE_UNSPECIFIED";
    }
    if (paramInt == 1) {
      return "BAND_MODE_EURO";
    }
    if (paramInt == 2) {
      return "BAND_MODE_USA";
    }
    if (paramInt == 3) {
      return "BAND_MODE_JPN";
    }
    if (paramInt == 4) {
      return "BAND_MODE_AUS";
    }
    if (paramInt == 5) {
      return "BAND_MODE_AUS_2";
    }
    if (paramInt == 6) {
      return "BAND_MODE_CELL_800";
    }
    if (paramInt == 7) {
      return "BAND_MODE_PCS";
    }
    if (paramInt == 8) {
      return "BAND_MODE_JTACS";
    }
    if (paramInt == 9) {
      return "BAND_MODE_KOREA_PCS";
    }
    if (paramInt == 10) {
      return "BAND_MODE_5_450M";
    }
    if (paramInt == 11) {
      return "BAND_MODE_IMT2000";
    }
    if (paramInt == 12) {
      return "BAND_MODE_7_700M_2";
    }
    if (paramInt == 13) {
      return "BAND_MODE_8_1800M";
    }
    if (paramInt == 14) {
      return "BAND_MODE_9_900M";
    }
    if (paramInt == 15) {
      return "BAND_MODE_10_800M_2";
    }
    if (paramInt == 16) {
      return "BAND_MODE_EURO_PAMR_400M";
    }
    if (paramInt == 17) {
      return "BAND_MODE_AWS";
    }
    if (paramInt == 18) {
      return "BAND_MODE_USA_2500M";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
