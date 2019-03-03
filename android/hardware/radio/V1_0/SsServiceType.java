package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SsServiceType
{
  public static final int ALL_BARRING = 16;
  public static final int BAIC = 14;
  public static final int BAIC_ROAMING = 15;
  public static final int BAOC = 11;
  public static final int BAOIC = 12;
  public static final int BAOIC_EXC_HOME = 13;
  public static final int CFU = 0;
  public static final int CF_ALL = 4;
  public static final int CF_ALL_CONDITIONAL = 5;
  public static final int CF_BUSY = 1;
  public static final int CF_NOT_REACHABLE = 3;
  public static final int CF_NO_REPLY = 2;
  public static final int CLIP = 6;
  public static final int CLIR = 7;
  public static final int COLP = 8;
  public static final int COLR = 9;
  public static final int INCOMING_BARRING = 18;
  public static final int OUTGOING_BARRING = 17;
  public static final int WAIT = 10;
  
  public SsServiceType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("CFU");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("CF_BUSY");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("CF_NO_REPLY");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("CF_NOT_REACHABLE");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CF_ALL");
      j = i | 0x4;
    }
    int k = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("CF_ALL_CONDITIONAL");
      k = j | 0x5;
    }
    i = k;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("CLIP");
      i = k | 0x6;
    }
    j = i;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("CLIR");
      j = i | 0x7;
    }
    i = j;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("COLP");
      i = j | 0x8;
    }
    j = i;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("COLR");
      j = i | 0x9;
    }
    i = j;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("WAIT");
      i = j | 0xA;
    }
    k = i;
    if ((paramInt & 0xB) == 11)
    {
      localArrayList.add("BAOC");
      k = i | 0xB;
    }
    j = k;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("BAOIC");
      j = k | 0xC;
    }
    i = j;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("BAOIC_EXC_HOME");
      i = j | 0xD;
    }
    j = i;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("BAIC");
      j = i | 0xE;
    }
    i = j;
    if ((paramInt & 0xF) == 15)
    {
      localArrayList.add("BAIC_ROAMING");
      i = j | 0xF;
    }
    j = i;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("ALL_BARRING");
      j = i | 0x10;
    }
    i = j;
    if ((paramInt & 0x11) == 17)
    {
      localArrayList.add("OUTGOING_BARRING");
      i = j | 0x11;
    }
    j = i;
    if ((paramInt & 0x12) == 18)
    {
      localArrayList.add("INCOMING_BARRING");
      j = i | 0x12;
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
      return "CFU";
    }
    if (paramInt == 1) {
      return "CF_BUSY";
    }
    if (paramInt == 2) {
      return "CF_NO_REPLY";
    }
    if (paramInt == 3) {
      return "CF_NOT_REACHABLE";
    }
    if (paramInt == 4) {
      return "CF_ALL";
    }
    if (paramInt == 5) {
      return "CF_ALL_CONDITIONAL";
    }
    if (paramInt == 6) {
      return "CLIP";
    }
    if (paramInt == 7) {
      return "CLIR";
    }
    if (paramInt == 8) {
      return "COLP";
    }
    if (paramInt == 9) {
      return "COLR";
    }
    if (paramInt == 10) {
      return "WAIT";
    }
    if (paramInt == 11) {
      return "BAOC";
    }
    if (paramInt == 12) {
      return "BAOIC";
    }
    if (paramInt == 13) {
      return "BAOIC_EXC_HOME";
    }
    if (paramInt == 14) {
      return "BAIC";
    }
    if (paramInt == 15) {
      return "BAIC_ROAMING";
    }
    if (paramInt == 16) {
      return "ALL_BARRING";
    }
    if (paramInt == 17) {
      return "OUTGOING_BARRING";
    }
    if (paramInt == 18) {
      return "INCOMING_BARRING";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
