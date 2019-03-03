package android.hardware.contexthub.V1_0;

import java.util.ArrayList;

public final class HubMemoryFlag
{
  public static final int EXEC = 4;
  public static final int READ = 1;
  public static final int WRITE = 2;
  
  public HubMemoryFlag() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("READ");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("WRITE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("EXEC");
      i = j | 0x4;
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
      return "READ";
    }
    if (paramInt == 2) {
      return "WRITE";
    }
    if (paramInt == 4) {
      return "EXEC";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
