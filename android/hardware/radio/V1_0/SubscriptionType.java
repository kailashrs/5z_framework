package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SubscriptionType
{
  public static final int SUBSCRIPTION_1 = 0;
  public static final int SUBSCRIPTION_2 = 1;
  public static final int SUBSCRIPTION_3 = 2;
  
  public SubscriptionType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("SUBSCRIPTION_1");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SUBSCRIPTION_2");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("SUBSCRIPTION_3");
      j = i | 0x2;
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
      return "SUBSCRIPTION_1";
    }
    if (paramInt == 1) {
      return "SUBSCRIPTION_2";
    }
    if (paramInt == 2) {
      return "SUBSCRIPTION_3";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
