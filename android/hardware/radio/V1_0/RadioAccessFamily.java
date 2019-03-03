package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioAccessFamily
{
  public static final int EDGE = 4;
  public static final int EHRPD = 8192;
  public static final int EVDO_0 = 128;
  public static final int EVDO_A = 256;
  public static final int EVDO_B = 4096;
  public static final int GPRS = 2;
  public static final int GSM = 65536;
  public static final int HSDPA = 512;
  public static final int HSPA = 2048;
  public static final int HSPAP = 32768;
  public static final int HSUPA = 1024;
  public static final int IS95A = 16;
  public static final int IS95B = 32;
  public static final int LTE = 16384;
  public static final int LTE_CA = 524288;
  public static final int ONE_X_RTT = 64;
  public static final int TD_SCDMA = 131072;
  public static final int UMTS = 8;
  public static final int UNKNOWN = 1;
  
  public RadioAccessFamily() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("UNKNOWN");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("GPRS");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("EDGE");
      i = j | 0x4;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("UMTS");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("IS95A");
      i = j | 0x10;
    }
    j = i;
    if ((paramInt & 0x20) == 32)
    {
      localArrayList.add("IS95B");
      j = i | 0x20;
    }
    i = j;
    if ((paramInt & 0x40) == 64)
    {
      localArrayList.add("ONE_X_RTT");
      i = j | 0x40;
    }
    j = i;
    if ((paramInt & 0x80) == 128)
    {
      localArrayList.add("EVDO_0");
      j = i | 0x80;
    }
    i = j;
    if ((paramInt & 0x100) == 256)
    {
      localArrayList.add("EVDO_A");
      i = j | 0x100;
    }
    j = i;
    if ((paramInt & 0x200) == 512)
    {
      localArrayList.add("HSDPA");
      j = i | 0x200;
    }
    i = j;
    if ((paramInt & 0x400) == 1024)
    {
      localArrayList.add("HSUPA");
      i = j | 0x400;
    }
    j = i;
    if ((paramInt & 0x800) == 2048)
    {
      localArrayList.add("HSPA");
      j = i | 0x800;
    }
    i = j;
    if ((paramInt & 0x1000) == 4096)
    {
      localArrayList.add("EVDO_B");
      i = j | 0x1000;
    }
    j = i;
    if ((paramInt & 0x2000) == 8192)
    {
      localArrayList.add("EHRPD");
      j = i | 0x2000;
    }
    int k = j;
    if ((paramInt & 0x4000) == 16384)
    {
      localArrayList.add("LTE");
      k = j | 0x4000;
    }
    i = k;
    if ((0x8000 & paramInt) == 32768)
    {
      localArrayList.add("HSPAP");
      i = k | 0x8000;
    }
    j = i;
    if ((0x10000 & paramInt) == 65536)
    {
      localArrayList.add("GSM");
      j = i | 0x10000;
    }
    i = j;
    if ((0x20000 & paramInt) == 131072)
    {
      localArrayList.add("TD_SCDMA");
      i = j | 0x20000;
    }
    j = i;
    if ((0x80000 & paramInt) == 524288)
    {
      localArrayList.add("LTE_CA");
      j = i | 0x80000;
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
      return "UNKNOWN";
    }
    if (paramInt == 2) {
      return "GPRS";
    }
    if (paramInt == 4) {
      return "EDGE";
    }
    if (paramInt == 8) {
      return "UMTS";
    }
    if (paramInt == 16) {
      return "IS95A";
    }
    if (paramInt == 32) {
      return "IS95B";
    }
    if (paramInt == 64) {
      return "ONE_X_RTT";
    }
    if (paramInt == 128) {
      return "EVDO_0";
    }
    if (paramInt == 256) {
      return "EVDO_A";
    }
    if (paramInt == 512) {
      return "HSDPA";
    }
    if (paramInt == 1024) {
      return "HSUPA";
    }
    if (paramInt == 2048) {
      return "HSPA";
    }
    if (paramInt == 4096) {
      return "EVDO_B";
    }
    if (paramInt == 8192) {
      return "EHRPD";
    }
    if (paramInt == 16384) {
      return "LTE";
    }
    if (paramInt == 32768) {
      return "HSPAP";
    }
    if (paramInt == 65536) {
      return "GSM";
    }
    if (paramInt == 131072) {
      return "TD_SCDMA";
    }
    if (paramInt == 524288) {
      return "LTE_CA";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
