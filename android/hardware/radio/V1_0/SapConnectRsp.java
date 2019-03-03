package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SapConnectRsp
{
  public static final int CONNECT_FAILURE = 1;
  public static final int CONNECT_OK_CALL_ONGOING = 4;
  public static final int MSG_SIZE_TOO_LARGE = 2;
  public static final int MSG_SIZE_TOO_SMALL = 3;
  public static final int SUCCESS = 0;
  
  public SapConnectRsp() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("SUCCESS");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("CONNECT_FAILURE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("MSG_SIZE_TOO_LARGE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("MSG_SIZE_TOO_SMALL");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CONNECT_OK_CALL_ONGOING");
      j = i | 0x4;
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
      return "SUCCESS";
    }
    if (paramInt == 1) {
      return "CONNECT_FAILURE";
    }
    if (paramInt == 2) {
      return "MSG_SIZE_TOO_LARGE";
    }
    if (paramInt == 3) {
      return "MSG_SIZE_TOO_SMALL";
    }
    if (paramInt == 4) {
      return "CONNECT_OK_CALL_ONGOING";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
