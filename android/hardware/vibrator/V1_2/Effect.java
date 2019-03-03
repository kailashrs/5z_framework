package android.hardware.vibrator.V1_2;

import java.util.ArrayList;

public final class Effect
{
  public static final int CLICK = 0;
  public static final int DOUBLE_CLICK = 1;
  public static final int HEAVY_CLICK = 5;
  public static final int POP = 4;
  public static final int RINGTONE_1 = 6;
  public static final int RINGTONE_10 = 15;
  public static final int RINGTONE_11 = 16;
  public static final int RINGTONE_12 = 17;
  public static final int RINGTONE_13 = 18;
  public static final int RINGTONE_14 = 19;
  public static final int RINGTONE_15 = 20;
  public static final int RINGTONE_2 = 7;
  public static final int RINGTONE_3 = 8;
  public static final int RINGTONE_4 = 9;
  public static final int RINGTONE_5 = 10;
  public static final int RINGTONE_6 = 11;
  public static final int RINGTONE_7 = 12;
  public static final int RINGTONE_8 = 13;
  public static final int RINGTONE_9 = 14;
  public static final int THUD = 3;
  public static final int TICK = 2;
  
  public Effect() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("CLICK");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("DOUBLE_CLICK");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("TICK");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("THUD");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("POP");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("HEAVY_CLICK");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("RINGTONE_1");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("RINGTONE_2");
      i = j | 0x7;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("RINGTONE_3");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("RINGTONE_4");
      i = j | 0x9;
    }
    j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("RINGTONE_5");
      j = i | 0xA;
    }
    int k = j;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("RINGTONE_6");
      k = j | 0xB;
    }
    i = k;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("RINGTONE_7");
      i = k | 0xC;
    }
    j = i;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("RINGTONE_8");
      j = i | 0xD;
    }
    i = j;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("RINGTONE_9");
      i = j | 0xE;
    }
    j = i;
    if ((paramInt & 0xF) == 15)
    {
      localArrayList.add("RINGTONE_10");
      j = i | 0xF;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("RINGTONE_11");
      i = j | 0x10;
    }
    k = i;
    if ((paramInt & 0x11) == 17)
    {
      localArrayList.add("RINGTONE_12");
      k = i | 0x11;
    }
    j = k;
    if ((paramInt & 0x12) == 18)
    {
      localArrayList.add("RINGTONE_13");
      j = k | 0x12;
    }
    i = j;
    if ((paramInt & 0x13) == 19)
    {
      localArrayList.add("RINGTONE_14");
      i = j | 0x13;
    }
    j = i;
    if ((paramInt & 0x14) == 20)
    {
      localArrayList.add("RINGTONE_15");
      j = i | 0x14;
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
      return "CLICK";
    }
    if (paramInt == 1) {
      return "DOUBLE_CLICK";
    }
    if (paramInt == 2) {
      return "TICK";
    }
    if (paramInt == 3) {
      return "THUD";
    }
    if (paramInt == 4) {
      return "POP";
    }
    if (paramInt == 5) {
      return "HEAVY_CLICK";
    }
    if (paramInt == 6) {
      return "RINGTONE_1";
    }
    if (paramInt == 7) {
      return "RINGTONE_2";
    }
    if (paramInt == 8) {
      return "RINGTONE_3";
    }
    if (paramInt == 9) {
      return "RINGTONE_4";
    }
    if (paramInt == 10) {
      return "RINGTONE_5";
    }
    if (paramInt == 11) {
      return "RINGTONE_6";
    }
    if (paramInt == 12) {
      return "RINGTONE_7";
    }
    if (paramInt == 13) {
      return "RINGTONE_8";
    }
    if (paramInt == 14) {
      return "RINGTONE_9";
    }
    if (paramInt == 15) {
      return "RINGTONE_10";
    }
    if (paramInt == 16) {
      return "RINGTONE_11";
    }
    if (paramInt == 17) {
      return "RINGTONE_12";
    }
    if (paramInt == 18) {
      return "RINGTONE_13";
    }
    if (paramInt == 19) {
      return "RINGTONE_14";
    }
    if (paramInt == 20) {
      return "RINGTONE_15";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
