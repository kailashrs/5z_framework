package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class CallState
{
  public static final int ACTIVE = 0;
  public static final int ALERTING = 3;
  public static final int DIALING = 2;
  public static final int HOLDING = 1;
  public static final int INCOMING = 4;
  public static final int WAITING = 5;
  
  public CallState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ACTIVE");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("HOLDING");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("DIALING");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("ALERTING");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("INCOMING");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("WAITING");
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
      return "ACTIVE";
    }
    if (paramInt == 1) {
      return "HOLDING";
    }
    if (paramInt == 2) {
      return "DIALING";
    }
    if (paramInt == 3) {
      return "ALERTING";
    }
    if (paramInt == 4) {
      return "INCOMING";
    }
    if (paramInt == 5) {
      return "WAITING";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
