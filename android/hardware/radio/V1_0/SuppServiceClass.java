package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SuppServiceClass
{
  public static final int DATA = 2;
  public static final int DATA_ASYNC = 32;
  public static final int DATA_SYNC = 16;
  public static final int FAX = 4;
  public static final int MAX = 128;
  public static final int NONE = 0;
  public static final int PACKET = 64;
  public static final int PAD = 128;
  public static final int SMS = 8;
  public static final int VOICE = 1;
  
  public SuppServiceClass() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NONE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("VOICE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("DATA");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("FAX");
      i = j | 0x4;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("SMS");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("DATA_SYNC");
      i = j | 0x10;
    }
    int k = i;
    if ((paramInt & 0x20) == 32)
    {
      localArrayList.add("DATA_ASYNC");
      k = i | 0x20;
    }
    j = k;
    if ((paramInt & 0x40) == 64)
    {
      localArrayList.add("PACKET");
      j = k | 0x40;
    }
    i = j;
    if ((paramInt & 0x80) == 128)
    {
      localArrayList.add("PAD");
      i = j | 0x80;
    }
    j = i;
    if ((paramInt & 0x80) == 128)
    {
      localArrayList.add("MAX");
      j = i | 0x80;
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
      return "NONE";
    }
    if (paramInt == 1) {
      return "VOICE";
    }
    if (paramInt == 2) {
      return "DATA";
    }
    if (paramInt == 4) {
      return "FAX";
    }
    if (paramInt == 8) {
      return "SMS";
    }
    if (paramInt == 16) {
      return "DATA_SYNC";
    }
    if (paramInt == 32) {
      return "DATA_ASYNC";
    }
    if (paramInt == 64) {
      return "PACKET";
    }
    if (paramInt == 128) {
      return "PAD";
    }
    if (paramInt == 128) {
      return "MAX";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
