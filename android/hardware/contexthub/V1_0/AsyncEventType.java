package android.hardware.contexthub.V1_0;

import java.util.ArrayList;

public final class AsyncEventType
{
  public static final int RESTARTED = 1;
  
  public AsyncEventType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("RESTARTED");
      i = 0x0 | 0x1;
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
    if (paramInt == 1) {
      return "RESTARTED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
