package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaSmsNumberPlan
{
  public static final int DATA = 3;
  public static final int PRIVATE = 9;
  public static final int RESERVED_10 = 10;
  public static final int RESERVED_11 = 11;
  public static final int RESERVED_12 = 12;
  public static final int RESERVED_13 = 13;
  public static final int RESERVED_14 = 14;
  public static final int RESERVED_15 = 15;
  public static final int RESERVED_2 = 2;
  public static final int RESERVED_5 = 5;
  public static final int RESERVED_6 = 6;
  public static final int RESERVED_7 = 7;
  public static final int RESERVED_8 = 8;
  public static final int TELEPHONY = 1;
  public static final int TELEX = 4;
  public static final int UNKNOWN = 0;
  
  public CdmaSmsNumberPlan() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("TELEPHONY");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("RESERVED_2");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("DATA");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("TELEX");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("RESERVED_5");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("RESERVED_6");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("RESERVED_7");
      i = j | 0x7;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("RESERVED_8");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("PRIVATE");
      i = j | 0x9;
    }
    j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("RESERVED_10");
      j = i | 0xA;
    }
    i = j;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("RESERVED_11");
      i = j | 0xB;
    }
    int k = i;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("RESERVED_12");
      k = i | 0xC;
    }
    j = k;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("RESERVED_13");
      j = k | 0xD;
    }
    i = j;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("RESERVED_14");
      i = j | 0xE;
    }
    j = i;
    if ((paramInt & 0xF) == 15)
    {
      localArrayList.add("RESERVED_15");
      j = i | 0xF;
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
      return "UNKNOWN";
    }
    if (paramInt == 1) {
      return "TELEPHONY";
    }
    if (paramInt == 2) {
      return "RESERVED_2";
    }
    if (paramInt == 3) {
      return "DATA";
    }
    if (paramInt == 4) {
      return "TELEX";
    }
    if (paramInt == 5) {
      return "RESERVED_5";
    }
    if (paramInt == 6) {
      return "RESERVED_6";
    }
    if (paramInt == 7) {
      return "RESERVED_7";
    }
    if (paramInt == 8) {
      return "RESERVED_8";
    }
    if (paramInt == 9) {
      return "PRIVATE";
    }
    if (paramInt == 10) {
      return "RESERVED_10";
    }
    if (paramInt == 11) {
      return "RESERVED_11";
    }
    if (paramInt == 12) {
      return "RESERVED_12";
    }
    if (paramInt == 13) {
      return "RESERVED_13";
    }
    if (paramInt == 14) {
      return "RESERVED_14";
    }
    if (paramInt == 15) {
      return "RESERVED_15";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
