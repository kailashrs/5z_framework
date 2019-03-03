package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class SapResultCode
{
  public static final int CARD_ALREADY_POWERED_OFF = 3;
  public static final int CARD_ALREADY_POWERED_ON = 5;
  public static final int CARD_NOT_ACCESSSIBLE = 2;
  public static final int CARD_REMOVED = 4;
  public static final int DATA_NOT_AVAILABLE = 6;
  public static final int GENERIC_FAILURE = 1;
  public static final int NOT_SUPPORTED = 7;
  public static final int SUCCESS = 0;
  
  public SapResultCode() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("SUCCESS");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("GENERIC_FAILURE");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("CARD_NOT_ACCESSSIBLE");
      j = i | 0x2;
    }
    i = j;
    if ((paramInt & 0x3) == 3)
    {
      localArrayList.add("CARD_ALREADY_POWERED_OFF");
      i = j | 0x3;
    }
    j = i;
    if ((paramInt & 0x4) == 4)
    {
      localArrayList.add("CARD_REMOVED");
      j = i | 0x4;
    }
    i = j;
    if ((paramInt & 0x5) == 5)
    {
      localArrayList.add("CARD_ALREADY_POWERED_ON");
      i = j | 0x5;
    }
    j = i;
    if ((paramInt & 0x6) == 6)
    {
      localArrayList.add("DATA_NOT_AVAILABLE");
      j = i | 0x6;
    }
    i = j;
    if ((paramInt & 0x7) == 7)
    {
      localArrayList.add("NOT_SUPPORTED");
      i = j | 0x7;
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
      return "GENERIC_FAILURE";
    }
    if (paramInt == 2) {
      return "CARD_NOT_ACCESSSIBLE";
    }
    if (paramInt == 3) {
      return "CARD_ALREADY_POWERED_OFF";
    }
    if (paramInt == 4) {
      return "CARD_REMOVED";
    }
    if (paramInt == 5) {
      return "CARD_ALREADY_POWERED_ON";
    }
    if (paramInt == 6) {
      return "DATA_NOT_AVAILABLE";
    }
    if (paramInt == 7) {
      return "NOT_SUPPORTED";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
