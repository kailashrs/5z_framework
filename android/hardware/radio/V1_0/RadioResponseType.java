package android.hardware.radio.V1_0;

import java.util.ArrayList;

public final class RadioResponseType
{
  public static final int SOLICITED = 0;
  public static final int SOLICITED_ACK = 1;
  public static final int SOLICITED_ACK_EXP = 2;
  
  public RadioResponseType() {}
  
  public static final String dumpBitfield(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    localArrayList.add("SOLICITED");
    if ((paramInt & 0x1) == 1)
    {
      localArrayList.add("SOLICITED_ACK");
      i = 0x0 | 0x1;
    }
    int j = i;
    if ((paramInt & 0x2) == 2)
    {
      localArrayList.add("SOLICITED_ACK_EXP");
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
      return "SOLICITED";
    }
    if (paramInt == 1) {
      return "SOLICITED_ACK";
    }
    if (paramInt == 2) {
      return "SOLICITED_ACK_EXP";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("0x");
    localStringBuilder.append(Integer.toHexString(paramInt));
    return localStringBuilder.toString();
  }
}
