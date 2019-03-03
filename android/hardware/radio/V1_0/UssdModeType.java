package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class UssdModeType
{
  public static final int LOCAL_CLIENT = 3;
  public static final int NOTIFY = 0;
  public static final int NOT_SUPPORTED = 4;
  public static final int NW_RELEASE = 2;
  public static final int NW_TIMEOUT = 5;
  public static final int REQUEST = 1;
  
  public UssdModeType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("NOTIFY");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("REQUEST");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("NW_RELEASE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("LOCAL_CLIENT");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("NOT_SUPPORTED");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("NW_TIMEOUT");
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
      return "NOTIFY";
    }
    if (paramInt == 1) {
      return "REQUEST";
    }
    if (paramInt == 2) {
      return "NW_RELEASE";
    }
    if (paramInt == 3) {
      return "LOCAL_CLIENT";
    }
    if (paramInt == 4) {
      return "NOT_SUPPORTED";
    }
    if (paramInt == 5) {
      return "NW_TIMEOUT";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
