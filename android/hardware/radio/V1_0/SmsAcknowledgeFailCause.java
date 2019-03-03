package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SmsAcknowledgeFailCause
{
  public static final int MEMORY_CAPACITY_EXCEEDED = 211;
  public static final int UNSPECIFIED_ERROR = 255;
  
  public SmsAcknowledgeFailCause() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0xD3) == 211)
    {
      localArrayList.add("MEMORY_CAPACITY_EXCEEDED");
      i = 0x0 | 0xD3;
    }
    int j = i;
    if ((paramInt & 0xFF) == 255)
    {
      localArrayList.add("UNSPECIFIED_ERROR");
      j = i | 0xFF;
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
    if (paramInt == 211) {
      return "MEMORY_CAPACITY_EXCEEDED";
    }
    if (paramInt == 255) {
      return "UNSPECIFIED_ERROR";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
