package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioTechnology
{
  public static final int EDGE = 2;
  public static final int EHRPD = 13;
  public static final int EVDO_0 = 7;
  public static final int EVDO_A = 8;
  public static final int EVDO_B = 12;
  public static final int GPRS = 1;
  public static final int GSM = 16;
  public static final int HSDPA = 9;
  public static final int HSPA = 11;
  public static final int HSPAP = 15;
  public static final int HSUPA = 10;
  public static final int IS95A = 4;
  public static final int IS95B = 5;
  public static final int IWLAN = 18;
  public static final int LTE = 14;
  public static final int LTE_CA = 19;
  public static final int ONE_X_RTT = 6;
  public static final int TD_SCDMA = 17;
  public static final int UMTS = 3;
  public static final int UNKNOWN = 0;
  
  public RadioTechnology() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("GPRS");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("EDGE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("UMTS");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("IS95A");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("IS95B");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("ONE_X_RTT");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("EVDO_0");
      i = j | 0x7;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("EVDO_A");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("HSDPA");
      i = j | 0x9;
    }
    j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("HSUPA");
      j = i | 0xA;
    }
    int k = j;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("HSPA");
      k = j | 0xB;
    }
    i = k;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("EVDO_B");
      i = k | 0xC;
    }
    j = i;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("EHRPD");
      j = i | 0xD;
    }
    i = j;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("LTE");
      i = j | 0xE;
    }
    j = i;
    if ((paramInt & 0xF) == 15)
    {
      localArrayList.add("HSPAP");
      j = i | 0xF;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("GSM");
      i = j | 0x10;
    }
    j = i;
    if ((paramInt & 0x11) == 17)
    {
      localArrayList.add("TD_SCDMA");
      j = i | 0x11;
    }
    i = j;
    if ((paramInt & 0x12) == 18)
    {
      localArrayList.add("IWLAN");
      i = j | 0x12;
    }
    j = i;
    if ((paramInt & 0x13) == 19)
    {
      localArrayList.add("LTE_CA");
      j = i | 0x13;
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
      return "GPRS";
    }
    if (paramInt == 2) {
      return "EDGE";
    }
    if (paramInt == 3) {
      return "UMTS";
    }
    if (paramInt == 4) {
      return "IS95A";
    }
    if (paramInt == 5) {
      return "IS95B";
    }
    if (paramInt == 6) {
      return "ONE_X_RTT";
    }
    if (paramInt == 7) {
      return "EVDO_0";
    }
    if (paramInt == 8) {
      return "EVDO_A";
    }
    if (paramInt == 9) {
      return "HSDPA";
    }
    if (paramInt == 10) {
      return "HSUPA";
    }
    if (paramInt == 11) {
      return "HSPA";
    }
    if (paramInt == 12) {
      return "EVDO_B";
    }
    if (paramInt == 13) {
      return "EHRPD";
    }
    if (paramInt == 14) {
      return "LTE";
    }
    if (paramInt == 15) {
      return "HSPAP";
    }
    if (paramInt == 16) {
      return "GSM";
    }
    if (paramInt == 17) {
      return "TD_SCDMA";
    }
    if (paramInt == 18) {
      return "IWLAN";
    }
    if (paramInt == 19) {
      return "LTE_CA";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
