package android.hardware.radio.V1_1;

import java.util.ArrayList;

public final class EutranBands
{
  public static final int BAND_1 = 1;
  public static final int BAND_10 = 10;
  public static final int BAND_11 = 11;
  public static final int BAND_12 = 12;
  public static final int BAND_13 = 13;
  public static final int BAND_14 = 14;
  public static final int BAND_17 = 17;
  public static final int BAND_18 = 18;
  public static final int BAND_19 = 19;
  public static final int BAND_2 = 2;
  public static final int BAND_20 = 20;
  public static final int BAND_21 = 21;
  public static final int BAND_22 = 22;
  public static final int BAND_23 = 23;
  public static final int BAND_24 = 24;
  public static final int BAND_25 = 25;
  public static final int BAND_26 = 26;
  public static final int BAND_27 = 27;
  public static final int BAND_28 = 28;
  public static final int BAND_3 = 3;
  public static final int BAND_30 = 30;
  public static final int BAND_31 = 31;
  public static final int BAND_33 = 33;
  public static final int BAND_34 = 34;
  public static final int BAND_35 = 35;
  public static final int BAND_36 = 36;
  public static final int BAND_37 = 37;
  public static final int BAND_38 = 38;
  public static final int BAND_39 = 39;
  public static final int BAND_4 = 4;
  public static final int BAND_40 = 40;
  public static final int BAND_41 = 41;
  public static final int BAND_42 = 42;
  public static final int BAND_43 = 43;
  public static final int BAND_44 = 44;
  public static final int BAND_45 = 45;
  public static final int BAND_46 = 46;
  public static final int BAND_47 = 47;
  public static final int BAND_48 = 48;
  public static final int BAND_5 = 5;
  public static final int BAND_6 = 6;
  public static final int BAND_65 = 65;
  public static final int BAND_66 = 66;
  public static final int BAND_68 = 68;
  public static final int BAND_7 = 7;
  public static final int BAND_70 = 70;
  public static final int BAND_8 = 8;
  public static final int BAND_9 = 9;
  
  public EutranBands() {}
  
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
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("BAND_4");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("BAND_5");
      i = j | 0x5;
    }
    int k = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("BAND_6");
      k = i | 0x6;
    }
    j = k;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("BAND_7");
      j = k | 0x7;
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
    i = j;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("BAND_13");
      i = j | 0xD;
    }
    k = i;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("BAND_14");
      k = i | 0xE;
    }
    j = k;
    if ((paramInt & 0x11) == 17)
    {
      localArrayList.add("BAND_17");
      j = k | 0x11;
    }
    i = j;
    if ((paramInt & 0x12) == 18)
    {
      localArrayList.add("BAND_18");
      i = j | 0x12;
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
    if ((paramInt & 0x17) == 23)
    {
      localArrayList.add("BAND_23");
      j = i | 0x17;
    }
    i = j;
    if ((paramInt & 0x18) == 24)
    {
      localArrayList.add("BAND_24");
      i = j | 0x18;
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
    j = i;
    if ((paramInt & 0x1B) == 27)
    {
      localArrayList.add("BAND_27");
      j = i | 0x1B;
    }
    k = j;
    if ((paramInt & 0x1C) == 28)
    {
      localArrayList.add("BAND_28");
      k = j | 0x1C;
    }
    i = k;
    if ((paramInt & 0x1E) == 30)
    {
      localArrayList.add("BAND_30");
      i = k | 0x1E;
    }
    j = i;
    if ((paramInt & 0x1F) == 31)
    {
      localArrayList.add("BAND_31");
      j = i | 0x1F;
    }
    i = j;
    if ((paramInt & 0x21) == 33)
    {
      localArrayList.add("BAND_33");
      i = j | 0x21;
    }
    j = i;
    if ((paramInt & 0x22) == 34)
    {
      localArrayList.add("BAND_34");
      j = i | 0x22;
    }
    i = j;
    if ((paramInt & 0x23) == 35)
    {
      localArrayList.add("BAND_35");
      i = j | 0x23;
    }
    j = i;
    if ((paramInt & 0x24) == 36)
    {
      localArrayList.add("BAND_36");
      j = i | 0x24;
    }
    i = j;
    if ((paramInt & 0x25) == 37)
    {
      localArrayList.add("BAND_37");
      i = j | 0x25;
    }
    j = i;
    if ((paramInt & 0x26) == 38)
    {
      localArrayList.add("BAND_38");
      j = i | 0x26;
    }
    i = j;
    if ((paramInt & 0x27) == 39)
    {
      localArrayList.add("BAND_39");
      i = j | 0x27;
    }
    k = i;
    if ((paramInt & 0x28) == 40)
    {
      localArrayList.add("BAND_40");
      k = i | 0x28;
    }
    j = k;
    if ((paramInt & 0x29) == 41)
    {
      localArrayList.add("BAND_41");
      j = k | 0x29;
    }
    i = j;
    if ((paramInt & 0x2A) == 42)
    {
      localArrayList.add("BAND_42");
      i = j | 0x2A;
    }
    j = i;
    if ((paramInt & 0x2B) == 43)
    {
      localArrayList.add("BAND_43");
      j = i | 0x2B;
    }
    i = j;
    if ((paramInt & 0x2C) == 44)
    {
      localArrayList.add("BAND_44");
      i = j | 0x2C;
    }
    j = i;
    if ((paramInt & 0x2D) == 45)
    {
      localArrayList.add("BAND_45");
      j = i | 0x2D;
    }
    i = j;
    if ((paramInt & 0x2E) == 46)
    {
      localArrayList.add("BAND_46");
      i = j | 0x2E;
    }
    j = i;
    if ((paramInt & 0x2F) == 47)
    {
      localArrayList.add("BAND_47");
      j = i | 0x2F;
    }
    i = j;
    if ((paramInt & 0x30) == 48)
    {
      localArrayList.add("BAND_48");
      i = j | 0x30;
    }
    j = i;
    if ((paramInt & 0x41) == 65)
    {
      localArrayList.add("BAND_65");
      j = i | 0x41;
    }
    i = j;
    if ((paramInt & 0x42) == 66)
    {
      localArrayList.add("BAND_66");
      i = j | 0x42;
    }
    j = i;
    if ((paramInt & 0x44) == 68)
    {
      localArrayList.add("BAND_68");
      j = i | 0x44;
    }
    i = j;
    if ((paramInt & 0x46) == 70)
    {
      localArrayList.add("BAND_70");
      i = j | 0x46;
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
    if (paramInt == 17) {
      return "BAND_17";
    }
    if (paramInt == 18) {
      return "BAND_18";
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
    if (paramInt == 23) {
      return "BAND_23";
    }
    if (paramInt == 24) {
      return "BAND_24";
    }
    if (paramInt == 25) {
      return "BAND_25";
    }
    if (paramInt == 26) {
      return "BAND_26";
    }
    if (paramInt == 27) {
      return "BAND_27";
    }
    if (paramInt == 28) {
      return "BAND_28";
    }
    if (paramInt == 30) {
      return "BAND_30";
    }
    if (paramInt == 31) {
      return "BAND_31";
    }
    if (paramInt == 33) {
      return "BAND_33";
    }
    if (paramInt == 34) {
      return "BAND_34";
    }
    if (paramInt == 35) {
      return "BAND_35";
    }
    if (paramInt == 36) {
      return "BAND_36";
    }
    if (paramInt == 37) {
      return "BAND_37";
    }
    if (paramInt == 38) {
      return "BAND_38";
    }
    if (paramInt == 39) {
      return "BAND_39";
    }
    if (paramInt == 40) {
      return "BAND_40";
    }
    if (paramInt == 41) {
      return "BAND_41";
    }
    if (paramInt == 42) {
      return "BAND_42";
    }
    if (paramInt == 43) {
      return "BAND_43";
    }
    if (paramInt == 44) {
      return "BAND_44";
    }
    if (paramInt == 45) {
      return "BAND_45";
    }
    if (paramInt == 46) {
      return "BAND_46";
    }
    if (paramInt == 47) {
      return "BAND_47";
    }
    if (paramInt == 48) {
      return "BAND_48";
    }
    if (paramInt == 65) {
      return "BAND_65";
    }
    if (paramInt == 66) {
      return "BAND_66";
    }
    if (paramInt == 68) {
      return "BAND_68";
    }
    if (paramInt == 70) {
      return "BAND_70";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
