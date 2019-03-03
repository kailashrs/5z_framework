package android.hardware.contexthub.V1_0;

import java.util.ArrayList;

public final class TransactionResult
{
  public static final int FAILURE = 1;
  public static final int SUCCESS = 0;
  
  public TransactionResult() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("SUCCESS");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("FAILURE");
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
    if (paramInt == 0) {
      return "SUCCESS";
    }
    if (paramInt == 1) {
      return "FAILURE";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
