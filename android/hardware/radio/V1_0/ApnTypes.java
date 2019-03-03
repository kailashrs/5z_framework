package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class ApnTypes
{
  public static final int ALL = 1023;
  public static final int CBS = 128;
  public static final int DEFAULT = 1;
  public static final int DUN = 8;
  public static final int EMERGENCY = 512;
  public static final int FOTA = 32;
  public static final int HIPRI = 16;
  public static final int IA = 256;
  public static final int IMS = 64;
  public static final int MMS = 2;
  public static final int NONE = 0;
  public static final int SUPL = 4;
  
  public ApnTypes() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NONE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("DEFAULT");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("MMS");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("SUPL");
      i = j | 0x4;
    }
    j = i;
    if ((paramInt & 0x8) == 8)
    {
      localArrayList.add("DUN");
      j = i | 0x8;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("HIPRI");
      i = j | 0x10;
    }
    j = i;
    if ((paramInt & 0x20) == 32)
    {
      localArrayList.add("FOTA");
      j = i | 0x20;
    }
    i = j;
    if ((paramInt & 0x40) == 64)
    {
      localArrayList.add("IMS");
      i = j | 0x40;
    }
    j = i;
    if ((paramInt & 0x80) == 128)
    {
      localArrayList.add("CBS");
      j = i | 0x80;
    }
    i = j;
    if ((paramInt & 0x100) == 256)
    {
      localArrayList.add("IA");
      i = j | 0x100;
    }
    j = i;
    if ((paramInt & 0x200) == 512)
    {
      localArrayList.add("EMERGENCY");
      j = i | 0x200;
    }
    i = j;
    if ((paramInt & 0x3FF) == 1023)
    {
      localArrayList.add("ALL");
      i = j | 0x3FF;
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
      return "NONE";
    }
    if (paramInt == 1) {
      return "DEFAULT";
    }
    if (paramInt == 2) {
      return "MMS";
    }
    if (paramInt == 4) {
      return "SUPL";
    }
    if (paramInt == 8) {
      return "DUN";
    }
    if (paramInt == 16) {
      return "HIPRI";
    }
    if (paramInt == 32) {
      return "FOTA";
    }
    if (paramInt == 64) {
      return "IMS";
    }
    if (paramInt == 128) {
      return "CBS";
    }
    if (paramInt == 256) {
      return "IA";
    }
    if (paramInt == 512) {
      return "EMERGENCY";
    }
    if (paramInt == 1023) {
      return "ALL";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
