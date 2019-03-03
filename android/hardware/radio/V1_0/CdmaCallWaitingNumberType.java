package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CdmaCallWaitingNumberType
{
  public static final int INTERNATIONAL = 1;
  public static final int NATIONAL = 2;
  public static final int NETWORK_SPECIFIC = 3;
  public static final int SUBSCRIBER = 4;
  public static final int UNKNOWN = 0;
  
  public CdmaCallWaitingNumberType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("INTERNATIONAL");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("NATIONAL");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("NETWORK_SPECIFIC");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("SUBSCRIBER");
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
      return "UNKNOWN";
    }
    if (paramInt == 1) {
      return "INTERNATIONAL";
    }
    if (paramInt == 2) {
      return "NATIONAL";
    }
    if (paramInt == 3) {
      return "NETWORK_SPECIFIC";
    }
    if (paramInt == 4) {
      return "SUBSCRIBER";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
