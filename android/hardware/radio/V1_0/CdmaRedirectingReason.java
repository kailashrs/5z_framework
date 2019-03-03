package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaRedirectingReason
{
  public static final int CALLED_DTE_OUT_OF_ORDER = 9;
  public static final int CALL_FORWARDING_BUSY = 1;
  public static final int CALL_FORWARDING_BY_THE_CALLED_DTE = 10;
  public static final int CALL_FORWARDING_NO_REPLY = 2;
  public static final int CALL_FORWARDING_UNCONDITIONAL = 15;
  public static final int RESERVED = 16;
  public static final int UNKNOWN = 0;
  
  public CdmaRedirectingReason() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("CALL_FORWARDING_BUSY");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("CALL_FORWARDING_NO_REPLY");
      j = i | 0x2;
    }
    int k = j;
    if ((paramInt & 0x9) == 9)
    {
      localArrayList.add("CALLED_DTE_OUT_OF_ORDER");
      k = j | 0x9;
    }
    i = k;
    if ((paramInt & 0xA) == 10)
    {
      localArrayList.add("CALL_FORWARDING_BY_THE_CALLED_DTE");
      i = k | 0xA;
    }
    j = i;
    if ((paramInt & 0xF) == 15)
    {
      localArrayList.add("CALL_FORWARDING_UNCONDITIONAL");
      j = i | 0xF;
    }
    i = j;
    if ((paramInt & 0x10) == 16)
    {
      localArrayList.add("RESERVED");
      i = j | 0x10;
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
      return "UNKNOWN";
    }
    if (paramInt == 1) {
      return "CALL_FORWARDING_BUSY";
    }
    if (paramInt == 2) {
      return "CALL_FORWARDING_NO_REPLY";
    }
    if (paramInt == 9) {
      return "CALLED_DTE_OUT_OF_ORDER";
    }
    if (paramInt == 10) {
      return "CALL_FORWARDING_BY_THE_CALLED_DTE";
    }
    if (paramInt == 15) {
      return "CALL_FORWARDING_UNCONDITIONAL";
    }
    if (paramInt == 16) {
      return "RESERVED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
