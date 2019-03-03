package android.hardware.contexthub.V1_0;

import java.util.ArrayList;

public final class Result
{
  public static final int BAD_PARAMS = 2;
  public static final int NOT_INIT = 3;
  public static final int OK = 0;
  public static final int TRANSACTION_FAILED = 4;
  public static final int TRANSACTION_PENDING = 5;
  public static final int UNKNOWN_FAILURE = 1;
  
  public Result() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("OK");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("UNKNOWN_FAILURE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("BAD_PARAMS");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("NOT_INIT");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("TRANSACTION_FAILED");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("TRANSACTION_PENDING");
      i = j | 0x5;
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
      return "OK";
    }
    if (paramInt == 1) {
      return "UNKNOWN_FAILURE";
    }
    if (paramInt == 2) {
      return "BAD_PARAMS";
    }
    if (paramInt == 3) {
      return "NOT_INIT";
    }
    if (paramInt == 4) {
      return "TRANSACTION_FAILED";
    }
    if (paramInt == 5) {
      return "TRANSACTION_PENDING";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
