package android.hardware.vibrator.V1_0;

import java.util.ArrayList;

public final class Status
{
  public static final int BAD_VALUE = 2;
  public static final int OK = 0;
  public static final int UNKNOWN_ERROR = 1;
  public static final int UNSUPPORTED_OPERATION = 3;
  
  public Status() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("OK");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("UNKNOWN_ERROR");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("BAD_VALUE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("UNSUPPORTED_OPERATION");
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
      return "OK";
    }
    if (paramInt == 1) {
      return "UNKNOWN_ERROR";
    }
    if (paramInt == 2) {
      return "BAD_VALUE";
    }
    if (paramInt == 3) {
      return "UNSUPPORTED_OPERATION";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
