package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SrvccState
{
  public static final int HANDOVER_CANCELED = 3;
  public static final int HANDOVER_COMPLETED = 1;
  public static final int HANDOVER_FAILED = 2;
  public static final int HANDOVER_STARTED = 0;
  
  public SrvccState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("HANDOVER_STARTED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("HANDOVER_COMPLETED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("HANDOVER_FAILED");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("HANDOVER_CANCELED");
      i = j | 0x3;
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
      return "HANDOVER_STARTED";
    }
    if (paramInt == 1) {
      return "HANDOVER_COMPLETED";
    }
    if (paramInt == 2) {
      return "HANDOVER_FAILED";
    }
    if (paramInt == 3) {
      return "HANDOVER_CANCELED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
