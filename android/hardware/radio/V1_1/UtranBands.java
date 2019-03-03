package android.hardware.radio.V1_1;

import java.util.ArrayList;

public final class UtranBands
{
  public static final int BAND_1 = 1;
  public static final int BAND_10 = 10;
  public static final int BAND_11 = 11;
  public static final int BAND_12 = 12;
  public static final int BAND_13 = 13;
  public static final int BAND_14 = 14;
  public static final int BAND_19 = 19;
  public static final int BAND_2 = 2;
  public static final int BAND_20 = 20;
  public static final int BAND_21 = 21;
  public static final int BAND_22 = 22;
  public static final int BAND_25 = 25;
  public static final int BAND_26 = 26;
  public static final int BAND_3 = 3;
  public static final int BAND_4 = 4;
  public static final int BAND_5 = 5;
  public static final int BAND_6 = 6;
  public static final int BAND_7 = 7;
  public static final int BAND_8 = 8;
  public static final int BAND_9 = 9;
  
  public UtranBands() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("BAND_1");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("BAND_2");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("BAND_3");
      i = j | 0x3;
    }
    int k = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("BAND_4");
      k = i | 0x4;
    }
    j = k;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("BAND_5");
      j = k | 0x5;
    }
    i = j;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("BAND_6");
      i = j | 0x6;
    }
    j = i;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("BAND_7");
      j = i | 0x7;
    }
    i = j;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("BAND_8");
      i = j | 0x8;
    }
    j = i;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("BAND_9");
      j = i | 0x9;
    }
    k = j;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("BAND_10");
      k = j | 0xA;
    }
    i = k;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("BAND_11");
      i = k | 0xB;
    }
    j = i;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("BAND_12");
      j = i | 0xC;
    }
    k = j;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("BAND_13");
      k = j | 0xD;
    }
    i = k;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("BAND_14");
      i = k | 0xE;
    }
    j = i;
    if ((paramInt & 0x13) == 19)
    {
      localArrayList.add("BAND_19");
      j = i | 0x13;
    }
    i = j;
    if ((paramInt & 0x14) == 20)
    {
      localArrayList.add("BAND_20");
      i = j | 0x14;
    }
    j = i;
    if ((paramInt & 0x15) == 21)
    {
      localArrayList.add("BAND_21");
      j = i | 0x15;
    }
    i = j;
    if ((paramInt & 0x16) == 22)
    {
      localArrayList.add("BAND_22");
      i = j | 0x16;
    }
    j = i;
    if ((paramInt & 0x19) == 25)
    {
      localArrayList.add("BAND_25");
      j = i | 0x19;
    }
    i = j;
    if ((paramInt & 0x1A) == 26)
    {
      localArrayList.add("BAND_26");
      i = j | 0x1A;
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
    if (paramInt == 1) {
      return "BAND_1";
    }
    if (paramInt == 2) {
      return "BAND_2";
    }
    if (paramInt == 3) {
      return "BAND_3";
    }
    if (paramInt == 4) {
      return "BAND_4";
    }
    if (paramInt == 5) {
      return "BAND_5";
    }
    if (paramInt == 6) {
      return "BAND_6";
    }
    if (paramInt == 7) {
      return "BAND_7";
    }
    if (paramInt == 8) {
      return "BAND_8";
    }
    if (paramInt == 9) {
      return "BAND_9";
    }
    if (paramInt == 10) {
      return "BAND_10";
    }
    if (paramInt == 11) {
      return "BAND_11";
    }
    if (paramInt == 12) {
      return "BAND_12";
    }
    if (paramInt == 13) {
      return "BAND_13";
    }
    if (paramInt == 14) {
      return "BAND_14";
    }
    if (paramInt == 19) {
      return "BAND_19";
    }
    if (paramInt == 20) {
      return "BAND_20";
    }
    if (paramInt == 21) {
      return "BAND_21";
    }
    if (paramInt == 22) {
      return "BAND_22";
    }
    if (paramInt == 25) {
      return "BAND_25";
    }
    if (paramInt == 26) {
      return "BAND_26";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
