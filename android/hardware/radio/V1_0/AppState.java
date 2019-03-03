package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class AppState
{
  public static final int DETECTED = 1;
  public static final int PIN = 2;
  public static final int PUK = 3;
  public static final int READY = 5;
  public static final int SUBSCRIPTION_PERSO = 4;
  public static final int UNKNOWN = 0;
  
  public AppState() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("UNKNOWN");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("DETECTED");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("PIN");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("PUK");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("SUBSCRIPTION_PERSO");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("READY");
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
      return "UNKNOWN";
    }
    if (paramInt == 1) {
      return "DETECTED";
    }
    if (paramInt == 2) {
      return "PIN";
    }
    if (paramInt == 3) {
      return "PUK";
    }
    if (paramInt == 4) {
      return "SUBSCRIPTION_PERSO";
    }
    if (paramInt == 5) {
      return "READY";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
