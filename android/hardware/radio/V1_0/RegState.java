package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RegState
{
  public static final int NOT_REG_MT_NOT_SEARCHING_OP = 0;
  public static final int NOT_REG_MT_NOT_SEARCHING_OP_EM = 10;
  public static final int NOT_REG_MT_SEARCHING_OP = 2;
  public static final int NOT_REG_MT_SEARCHING_OP_EM = 12;
  public static final int REG_DENIED = 3;
  public static final int REG_DENIED_EM = 13;
  public static final int REG_HOME = 1;
  public static final int REG_ROAMING = 5;
  public static final int UNKNOWN = 4;
  public static final int UNKNOWN_EM = 14;
  
  public RegState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NOT_REG_MT_NOT_SEARCHING_OP");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("REG_HOME");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("NOT_REG_MT_SEARCHING_OP");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("REG_DENIED");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("UNKNOWN");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("REG_ROAMING");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("NOT_REG_MT_NOT_SEARCHING_OP_EM");
      j = i | 0xA;
    }
    i = j;
    if ((paramInt & 0xC) == 12)
    {
      localArrayList.add("NOT_REG_MT_SEARCHING_OP_EM");
      i = j | 0xC;
    }
    j = i;
    if ((paramInt & 0xD) == 13)
    {
      localArrayList.add("REG_DENIED_EM");
      j = i | 0xD;
    }
    i = j;
    if ((paramInt & 0xE) == 14)
    {
      localArrayList.add("UNKNOWN_EM");
      i = j | 0xE;
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
      return "NOT_REG_MT_NOT_SEARCHING_OP";
    }
    if (paramInt == 1) {
      return "REG_HOME";
    }
    if (paramInt == 2) {
      return "NOT_REG_MT_SEARCHING_OP";
    }
    if (paramInt == 3) {
      return "REG_DENIED";
    }
    if (paramInt == 4) {
      return "UNKNOWN";
    }
    if (paramInt == 5) {
      return "REG_ROAMING";
    }
    if (paramInt == 10) {
      return "NOT_REG_MT_NOT_SEARCHING_OP_EM";
    }
    if (paramInt == 12) {
      return "NOT_REG_MT_SEARCHING_OP_EM";
    }
    if (paramInt == 13) {
      return "REG_DENIED_EM";
    }
    if (paramInt == 14) {
      return "UNKNOWN_EM";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
