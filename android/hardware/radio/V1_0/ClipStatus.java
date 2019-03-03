package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class ClipStatus
{
  public static final int CLIP_PROVISIONED = 0;
  public static final int CLIP_UNPROVISIONED = 1;
  public static final int UNKNOWN = 2;
  
  public ClipStatus() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("CLIP_PROVISIONED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("CLIP_UNPROVISIONED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("UNKNOWN");
      j = i | 0x2;
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
      return "CLIP_PROVISIONED";
    }
    if (paramInt == 1) {
      return "CLIP_UNPROVISIONED";
    }
    if (paramInt == 2) {
      return "UNKNOWN";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
