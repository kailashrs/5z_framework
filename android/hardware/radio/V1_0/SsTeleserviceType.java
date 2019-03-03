package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SsTeleserviceType
{
  public static final int ALL_DATA_TELESERVICES = 3;
  public static final int ALL_TELESERVICES_EXCEPT_SMS = 5;
  public static final int ALL_TELESEVICES = 1;
  public static final int ALL_TELE_AND_BEARER_SERVICES = 0;
  public static final int SMS_SERVICES = 4;
  public static final int TELEPHONY = 2;
  
  public SsTeleserviceType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("ALL_TELE_AND_BEARER_SERVICES");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("ALL_TELESEVICES");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("TELEPHONY");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("ALL_DATA_TELESERVICES");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("SMS_SERVICES");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("ALL_TELESERVICES_EXCEPT_SMS");
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
      return "ALL_TELE_AND_BEARER_SERVICES";
    }
    if (paramInt == 1) {
      return "ALL_TELESEVICES";
    }
    if (paramInt == 2) {
      return "TELEPHONY";
    }
    if (paramInt == 3) {
      return "ALL_DATA_TELESERVICES";
    }
    if (paramInt == 4) {
      return "SMS_SERVICES";
    }
    if (paramInt == 5) {
      return "ALL_TELESERVICES_EXCEPT_SMS";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
